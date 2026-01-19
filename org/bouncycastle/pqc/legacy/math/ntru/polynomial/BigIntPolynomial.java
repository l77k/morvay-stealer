/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.ntru.polynomial;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.BigDecimalPolynomial;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.Constants;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.IntegerPolynomial;
import org.bouncycastle.util.Arrays;

public class BigIntPolynomial {
    private static final double LOG_10_2 = Math.log10(2.0);
    BigInteger[] coeffs;

    BigIntPolynomial(int n2) {
        this.coeffs = new BigInteger[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            this.coeffs[i2] = Constants.BIGINT_ZERO;
        }
    }

    BigIntPolynomial(BigInteger[] bigIntegerArray) {
        this.coeffs = bigIntegerArray;
    }

    public BigIntPolynomial(IntegerPolynomial integerPolynomial) {
        this.coeffs = new BigInteger[integerPolynomial.coeffs.length];
        for (int i2 = 0; i2 < this.coeffs.length; ++i2) {
            this.coeffs[i2] = BigInteger.valueOf(integerPolynomial.coeffs[i2]);
        }
    }

    static BigIntPolynomial generateRandomSmall(int n2, int n3, int n4) {
        int n5;
        ArrayList<BigInteger> arrayList = new ArrayList<BigInteger>();
        for (n5 = 0; n5 < n3; ++n5) {
            arrayList.add(Constants.BIGINT_ONE);
        }
        for (n5 = 0; n5 < n4; ++n5) {
            arrayList.add(BigInteger.valueOf(-1L));
        }
        while (arrayList.size() < n2) {
            arrayList.add(Constants.BIGINT_ZERO);
        }
        Collections.shuffle(arrayList, CryptoServicesRegistrar.getSecureRandom());
        BigIntPolynomial bigIntPolynomial = new BigIntPolynomial(n2);
        for (int i2 = 0; i2 < arrayList.size(); ++i2) {
            bigIntPolynomial.coeffs[i2] = (BigInteger)arrayList.get(i2);
        }
        return bigIntPolynomial;
    }

    public BigIntPolynomial mult(BigIntPolynomial bigIntPolynomial) {
        int n2 = this.coeffs.length;
        if (bigIntPolynomial.coeffs.length != n2) {
            throw new IllegalArgumentException("Number of coefficients must be the same");
        }
        BigIntPolynomial bigIntPolynomial2 = this.multRecursive(bigIntPolynomial);
        if (bigIntPolynomial2.coeffs.length > n2) {
            for (int i2 = n2; i2 < bigIntPolynomial2.coeffs.length; ++i2) {
                bigIntPolynomial2.coeffs[i2 - n2] = bigIntPolynomial2.coeffs[i2 - n2].add(bigIntPolynomial2.coeffs[i2]);
            }
            bigIntPolynomial2.coeffs = Arrays.copyOf(bigIntPolynomial2.coeffs, n2);
        }
        return bigIntPolynomial2;
    }

    private BigIntPolynomial multRecursive(BigIntPolynomial bigIntPolynomial) {
        int n2;
        BigInteger[] bigIntegerArray = this.coeffs;
        BigInteger[] bigIntegerArray2 = bigIntPolynomial.coeffs;
        int n3 = bigIntPolynomial.coeffs.length;
        if (n3 <= 1) {
            BigInteger[] bigIntegerArray3 = Arrays.clone(this.coeffs);
            for (int i2 = 0; i2 < this.coeffs.length; ++i2) {
                bigIntegerArray3[i2] = bigIntegerArray3[i2].multiply(bigIntPolynomial.coeffs[0]);
            }
            return new BigIntPolynomial(bigIntegerArray3);
        }
        int n4 = n3 / 2;
        BigIntPolynomial bigIntPolynomial2 = new BigIntPolynomial(Arrays.copyOf(bigIntegerArray, n4));
        BigIntPolynomial bigIntPolynomial3 = new BigIntPolynomial(Arrays.copyOfRange(bigIntegerArray, n4, n3));
        BigIntPolynomial bigIntPolynomial4 = new BigIntPolynomial(Arrays.copyOf(bigIntegerArray2, n4));
        BigIntPolynomial bigIntPolynomial5 = new BigIntPolynomial(Arrays.copyOfRange(bigIntegerArray2, n4, n3));
        BigIntPolynomial bigIntPolynomial6 = (BigIntPolynomial)bigIntPolynomial2.clone();
        bigIntPolynomial6.add(bigIntPolynomial3);
        BigIntPolynomial bigIntPolynomial7 = (BigIntPolynomial)bigIntPolynomial4.clone();
        bigIntPolynomial7.add(bigIntPolynomial5);
        BigIntPolynomial bigIntPolynomial8 = bigIntPolynomial2.multRecursive(bigIntPolynomial4);
        BigIntPolynomial bigIntPolynomial9 = bigIntPolynomial3.multRecursive(bigIntPolynomial5);
        BigIntPolynomial bigIntPolynomial10 = bigIntPolynomial6.multRecursive(bigIntPolynomial7);
        bigIntPolynomial10.sub(bigIntPolynomial8);
        bigIntPolynomial10.sub(bigIntPolynomial9);
        BigIntPolynomial bigIntPolynomial11 = new BigIntPolynomial(2 * n3 - 1);
        for (n2 = 0; n2 < bigIntPolynomial8.coeffs.length; ++n2) {
            bigIntPolynomial11.coeffs[n2] = bigIntPolynomial8.coeffs[n2];
        }
        for (n2 = 0; n2 < bigIntPolynomial10.coeffs.length; ++n2) {
            bigIntPolynomial11.coeffs[n4 + n2] = bigIntPolynomial11.coeffs[n4 + n2].add(bigIntPolynomial10.coeffs[n2]);
        }
        for (n2 = 0; n2 < bigIntPolynomial9.coeffs.length; ++n2) {
            bigIntPolynomial11.coeffs[2 * n4 + n2] = bigIntPolynomial11.coeffs[2 * n4 + n2].add(bigIntPolynomial9.coeffs[n2]);
        }
        return bigIntPolynomial11;
    }

    void add(BigIntPolynomial bigIntPolynomial, BigInteger bigInteger) {
        this.add(bigIntPolynomial);
        this.mod(bigInteger);
    }

    public void add(BigIntPolynomial bigIntPolynomial) {
        int n2;
        if (bigIntPolynomial.coeffs.length > this.coeffs.length) {
            n2 = this.coeffs.length;
            this.coeffs = Arrays.copyOf(this.coeffs, bigIntPolynomial.coeffs.length);
            for (int i2 = n2; i2 < this.coeffs.length; ++i2) {
                this.coeffs[i2] = Constants.BIGINT_ZERO;
            }
        }
        for (n2 = 0; n2 < bigIntPolynomial.coeffs.length; ++n2) {
            this.coeffs[n2] = this.coeffs[n2].add(bigIntPolynomial.coeffs[n2]);
        }
    }

    public void sub(BigIntPolynomial bigIntPolynomial) {
        int n2;
        if (bigIntPolynomial.coeffs.length > this.coeffs.length) {
            n2 = this.coeffs.length;
            this.coeffs = Arrays.copyOf(this.coeffs, bigIntPolynomial.coeffs.length);
            for (int i2 = n2; i2 < this.coeffs.length; ++i2) {
                this.coeffs[i2] = Constants.BIGINT_ZERO;
            }
        }
        for (n2 = 0; n2 < bigIntPolynomial.coeffs.length; ++n2) {
            this.coeffs[n2] = this.coeffs[n2].subtract(bigIntPolynomial.coeffs[n2]);
        }
    }

    public void mult(BigInteger bigInteger) {
        for (int i2 = 0; i2 < this.coeffs.length; ++i2) {
            this.coeffs[i2] = this.coeffs[i2].multiply(bigInteger);
        }
    }

    void mult(int n2) {
        this.mult(BigInteger.valueOf(n2));
    }

    public void div(BigInteger bigInteger) {
        BigInteger bigInteger2 = bigInteger.add(Constants.BIGINT_ONE).divide(BigInteger.valueOf(2L));
        for (int i2 = 0; i2 < this.coeffs.length; ++i2) {
            this.coeffs[i2] = this.coeffs[i2].compareTo(Constants.BIGINT_ZERO) > 0 ? this.coeffs[i2].add(bigInteger2) : this.coeffs[i2].add(bigInteger2.negate());
            this.coeffs[i2] = this.coeffs[i2].divide(bigInteger);
        }
    }

    public BigDecimalPolynomial div(BigDecimal bigDecimal, int n2) {
        BigInteger bigInteger = this.maxCoeffAbs();
        int n3 = (int)((double)bigInteger.bitLength() * LOG_10_2) + 1;
        BigDecimal bigDecimal2 = Constants.BIGDEC_ONE.divide(bigDecimal, n3 + n2 + 1, 6);
        BigDecimalPolynomial bigDecimalPolynomial = new BigDecimalPolynomial(this.coeffs.length);
        for (int i2 = 0; i2 < this.coeffs.length; ++i2) {
            bigDecimalPolynomial.coeffs[i2] = new BigDecimal(this.coeffs[i2]).multiply(bigDecimal2).setScale(n2, 6);
        }
        return bigDecimalPolynomial;
    }

    public int getMaxCoeffLength() {
        return (int)((double)this.maxCoeffAbs().bitLength() * LOG_10_2) + 1;
    }

    private BigInteger maxCoeffAbs() {
        BigInteger bigInteger = this.coeffs[0].abs();
        for (int i2 = 1; i2 < this.coeffs.length; ++i2) {
            BigInteger bigInteger2 = this.coeffs[i2].abs();
            if (bigInteger2.compareTo(bigInteger) <= 0) continue;
            bigInteger = bigInteger2;
        }
        return bigInteger;
    }

    public void mod(BigInteger bigInteger) {
        for (int i2 = 0; i2 < this.coeffs.length; ++i2) {
            this.coeffs[i2] = this.coeffs[i2].mod(bigInteger);
        }
    }

    BigInteger sumCoeffs() {
        BigInteger bigInteger = Constants.BIGINT_ZERO;
        for (int i2 = 0; i2 < this.coeffs.length; ++i2) {
            bigInteger = bigInteger.add(this.coeffs[i2]);
        }
        return bigInteger;
    }

    public Object clone() {
        return new BigIntPolynomial((BigInteger[])this.coeffs.clone());
    }

    public int hashCode() {
        int n2 = 1;
        n2 = 31 * n2 + Arrays.hashCode(this.coeffs);
        return n2;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        BigIntPolynomial bigIntPolynomial = (BigIntPolynomial)object;
        return Arrays.areEqual(this.coeffs, bigIntPolynomial.coeffs);
    }

    public BigInteger[] getCoeffs() {
        return Arrays.clone(this.coeffs);
    }
}

