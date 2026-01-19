/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.ntru;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.pqc.legacy.crypto.ntru.NTRUSigningKeyGenerationParameters;
import org.bouncycastle.pqc.legacy.crypto.ntru.NTRUSigningPrivateKeyParameters;
import org.bouncycastle.pqc.legacy.crypto.ntru.NTRUSigningPublicKeyParameters;
import org.bouncycastle.pqc.legacy.math.ntru.euclid.BigIntEuclidean;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.BigDecimalPolynomial;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.BigIntPolynomial;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.DenseTernaryPolynomial;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.IntegerPolynomial;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.Polynomial;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.ProductFormPolynomial;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.Resultant;

public class NTRUSigningKeyPairGenerator
implements AsymmetricCipherKeyPairGenerator {
    private NTRUSigningKeyGenerationParameters params;

    @Override
    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (NTRUSigningKeyGenerationParameters)keyGenerationParameters;
    }

    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        Object object;
        NTRUSigningPublicKeyParameters nTRUSigningPublicKeyParameters = null;
        ExecutorService executorService = Executors.newCachedThreadPool();
        ArrayList<Future<NTRUSigningPrivateKeyParameters.Basis>> arrayList = new ArrayList<Future<NTRUSigningPrivateKeyParameters.Basis>>();
        for (int i2 = this.params.B; i2 >= 0; --i2) {
            arrayList.add(executorService.submit(new BasisGenerationTask()));
        }
        executorService.shutdown();
        ArrayList<NTRUSigningPrivateKeyParameters.Basis> arrayList2 = new ArrayList<NTRUSigningPrivateKeyParameters.Basis>();
        for (int i3 = this.params.B; i3 >= 0; --i3) {
            object = (Future)arrayList.get(i3);
            try {
                arrayList2.add((NTRUSigningPrivateKeyParameters.Basis)object.get());
                if (i3 != this.params.B) continue;
                nTRUSigningPublicKeyParameters = new NTRUSigningPublicKeyParameters(((NTRUSigningPrivateKeyParameters.Basis)object.get()).h, this.params.getSigningParameters());
                continue;
            }
            catch (Exception exception) {
                throw new IllegalStateException(exception);
            }
        }
        NTRUSigningPrivateKeyParameters nTRUSigningPrivateKeyParameters = new NTRUSigningPrivateKeyParameters(arrayList2, nTRUSigningPublicKeyParameters);
        object = new AsymmetricCipherKeyPair(nTRUSigningPublicKeyParameters, nTRUSigningPrivateKeyParameters);
        return object;
    }

    public AsymmetricCipherKeyPair generateKeyPairSingleThread() {
        ArrayList<NTRUSigningPrivateKeyParameters.Basis> arrayList = new ArrayList<NTRUSigningPrivateKeyParameters.Basis>();
        NTRUSigningPublicKeyParameters nTRUSigningPublicKeyParameters = null;
        for (int i2 = this.params.B; i2 >= 0; --i2) {
            NTRUSigningPrivateKeyParameters.Basis basis = this.generateBoundedBasis();
            arrayList.add(basis);
            if (i2 != 0) continue;
            nTRUSigningPublicKeyParameters = new NTRUSigningPublicKeyParameters(basis.h, this.params.getSigningParameters());
        }
        NTRUSigningPrivateKeyParameters nTRUSigningPrivateKeyParameters = new NTRUSigningPrivateKeyParameters(arrayList, nTRUSigningPublicKeyParameters);
        return new AsymmetricCipherKeyPair(nTRUSigningPublicKeyParameters, nTRUSigningPrivateKeyParameters);
    }

    private void minimizeFG(IntegerPolynomial integerPolynomial, IntegerPolynomial integerPolynomial2, IntegerPolynomial integerPolynomial3, IntegerPolynomial integerPolynomial4, int n2) {
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            n3 += 2 * n2 * (integerPolynomial.coeffs[i2] * integerPolynomial.coeffs[i2] + integerPolynomial2.coeffs[i2] * integerPolynomial2.coeffs[i2]);
        }
        n3 -= 4;
        IntegerPolynomial integerPolynomial5 = (IntegerPolynomial)integerPolynomial.clone();
        IntegerPolynomial integerPolynomial6 = (IntegerPolynomial)integerPolynomial2.clone();
        int n4 = 0;
        int n5 = n2;
        for (int i3 = 0; n4 < n5 && i3 < n2; ++i3) {
            int n6;
            int n7 = 0;
            for (int i4 = 0; i4 < n2; ++i4) {
                n6 = integerPolynomial3.coeffs[i4] * integerPolynomial.coeffs[i4];
                int n8 = integerPolynomial4.coeffs[i4] * integerPolynomial2.coeffs[i4];
                int n9 = 4 * n2 * (n6 + n8);
                n7 += n9;
            }
            n6 = 4 * (integerPolynomial3.sumCoeffs() + integerPolynomial4.sumCoeffs());
            if ((n7 -= n6) > n3) {
                integerPolynomial3.sub(integerPolynomial5);
                integerPolynomial4.sub(integerPolynomial6);
                ++n4;
                i3 = 0;
            } else if (n7 < -n3) {
                integerPolynomial3.add(integerPolynomial5);
                integerPolynomial4.add(integerPolynomial6);
                ++n4;
                i3 = 0;
            }
            integerPolynomial5.rotate1();
            integerPolynomial6.rotate1();
        }
    }

    private FGBasis generateBasis() {
        BigIntPolynomial bigIntPolynomial;
        Object object;
        Object object2;
        Object object3;
        Object object4;
        Object object5;
        Object object6;
        BigIntEuclidean bigIntEuclidean;
        Resultant resultant;
        IntegerPolynomial integerPolynomial;
        DenseTernaryPolynomial denseTernaryPolynomial;
        DenseTernaryPolynomial denseTernaryPolynomial2;
        IntegerPolynomial integerPolynomial2;
        IntegerPolynomial integerPolynomial3;
        int n2 = this.params.N;
        int n3 = this.params.q;
        int n4 = this.params.d;
        int n5 = this.params.d1;
        int n6 = this.params.d2;
        int n7 = this.params.d3;
        int n8 = this.params.basisType;
        int n9 = 2 * n2 + 1;
        boolean bl = this.params.primeCheck;
        do {
            denseTernaryPolynomial2 = this.params.polyType == 0 ? DenseTernaryPolynomial.generateRandom(n2, n4 + 1, n4, CryptoServicesRegistrar.getSecureRandom()) : ProductFormPolynomial.generateRandom(n2, n5, n6, n7 + 1, n7, CryptoServicesRegistrar.getSecureRandom());
            integerPolynomial3 = denseTernaryPolynomial2.toIntegerPolynomial();
        } while (bl && integerPolynomial3.resultant((int)n9).res.equals(BigInteger.ZERO) || (integerPolynomial2 = integerPolynomial3.invertFq(n3)) == null);
        Resultant resultant2 = integerPolynomial3.resultant();
        while (true) {
            denseTernaryPolynomial = this.params.polyType == 0 ? DenseTernaryPolynomial.generateRandom(n2, n4 + 1, n4, CryptoServicesRegistrar.getSecureRandom()) : ProductFormPolynomial.generateRandom(n2, n5, n6, n7 + 1, n7, CryptoServicesRegistrar.getSecureRandom());
            integerPolynomial = denseTernaryPolynomial.toIntegerPolynomial();
            if (bl && integerPolynomial.resultant((int)n9).res.equals(BigInteger.ZERO) || integerPolynomial.invertFq(n3) == null) continue;
            resultant = integerPolynomial.resultant();
            bigIntEuclidean = BigIntEuclidean.calculate(resultant2.res, resultant.res);
            if (bigIntEuclidean.gcd.equals(BigInteger.ONE)) break;
        }
        BigIntPolynomial bigIntPolynomial2 = (BigIntPolynomial)resultant2.rho.clone();
        bigIntPolynomial2.mult(bigIntEuclidean.x.multiply(BigInteger.valueOf(n3)));
        BigIntPolynomial bigIntPolynomial3 = (BigIntPolynomial)resultant.rho.clone();
        bigIntPolynomial3.mult(bigIntEuclidean.y.multiply(BigInteger.valueOf(-n3)));
        if (this.params.keyGenAlg == 0) {
            object6 = new int[n2];
            object5 = new int[n2];
            object6[0] = integerPolynomial3.coeffs[0];
            object5[0] = integerPolynomial.coeffs[0];
            for (int i2 = 1; i2 < n2; ++i2) {
                object6[i2] = integerPolynomial3.coeffs[n2 - i2];
                object5[i2] = integerPolynomial.coeffs[n2 - i2];
            }
            object4 = new IntegerPolynomial((int[])object6);
            object3 = new IntegerPolynomial((int[])object5);
            object2 = denseTernaryPolynomial2.mult((IntegerPolynomial)object4);
            ((IntegerPolynomial)object2).add(denseTernaryPolynomial.mult((IntegerPolynomial)object3));
            object = ((IntegerPolynomial)object2).resultant();
            bigIntPolynomial = ((IntegerPolynomial)object4).mult(bigIntPolynomial3);
            bigIntPolynomial.add(((IntegerPolynomial)object3).mult(bigIntPolynomial2));
            bigIntPolynomial = bigIntPolynomial.mult(((Resultant)object).rho);
            bigIntPolynomial.div(((Resultant)object).res);
        } else {
            int n10 = 0;
            for (int i3 = 1; i3 < n2; i3 *= 10) {
                ++n10;
            }
            object5 = resultant2.rho.div(new BigDecimal(resultant2.res), bigIntPolynomial3.getMaxCoeffLength() + 1 + n10);
            object4 = resultant.rho.div(new BigDecimal(resultant.res), bigIntPolynomial2.getMaxCoeffLength() + 1 + n10);
            object3 = ((BigDecimalPolynomial)object5).mult(bigIntPolynomial3);
            ((BigDecimalPolynomial)object3).add(((BigDecimalPolynomial)object4).mult(bigIntPolynomial2));
            ((BigDecimalPolynomial)object3).halve();
            bigIntPolynomial = ((BigDecimalPolynomial)object3).round();
        }
        object6 = (BigIntPolynomial)bigIntPolynomial3.clone();
        ((BigIntPolynomial)object6).sub(denseTernaryPolynomial2.mult(bigIntPolynomial));
        object5 = (BigIntPolynomial)bigIntPolynomial2.clone();
        ((BigIntPolynomial)object5).sub(denseTernaryPolynomial.mult(bigIntPolynomial));
        object4 = new IntegerPolynomial((BigIntPolynomial)object6);
        object3 = new IntegerPolynomial((BigIntPolynomial)object5);
        this.minimizeFG(integerPolynomial3, integerPolynomial, (IntegerPolynomial)object4, (IntegerPolynomial)object3, n2);
        if (n8 == 0) {
            object2 = object4;
            object = denseTernaryPolynomial.mult(integerPolynomial2, n3);
        } else {
            object2 = denseTernaryPolynomial;
            object = ((IntegerPolynomial)object4).mult(integerPolynomial2, n3);
        }
        ((IntegerPolynomial)object).modPositive(n3);
        return new FGBasis(denseTernaryPolynomial2, (Polynomial)object2, (IntegerPolynomial)object, (IntegerPolynomial)object4, (IntegerPolynomial)object3, this.params);
    }

    public NTRUSigningPrivateKeyParameters.Basis generateBoundedBasis() {
        FGBasis fGBasis;
        while (!(fGBasis = this.generateBasis()).isNormOk()) {
        }
        return fGBasis;
    }

    private class BasisGenerationTask
    implements Callable<NTRUSigningPrivateKeyParameters.Basis> {
        private BasisGenerationTask() {
        }

        @Override
        public NTRUSigningPrivateKeyParameters.Basis call() throws Exception {
            return NTRUSigningKeyPairGenerator.this.generateBoundedBasis();
        }
    }

    public static class FGBasis
    extends NTRUSigningPrivateKeyParameters.Basis {
        public IntegerPolynomial F;
        public IntegerPolynomial G;

        FGBasis(Polynomial polynomial, Polynomial polynomial2, IntegerPolynomial integerPolynomial, IntegerPolynomial integerPolynomial2, IntegerPolynomial integerPolynomial3, NTRUSigningKeyGenerationParameters nTRUSigningKeyGenerationParameters) {
            super(polynomial, polynomial2, integerPolynomial, nTRUSigningKeyGenerationParameters);
            this.F = integerPolynomial2;
            this.G = integerPolynomial3;
        }

        boolean isNormOk() {
            double d2 = this.params.keyNormBoundSq;
            int n2 = this.params.q;
            return (double)this.F.centeredNormSq(n2) < d2 && (double)this.G.centeredNormSq(n2) < d2;
        }
    }
}

