/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.CryptoServicePurpose;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.constraints.ConstraintUtils;
import org.bouncycastle.crypto.constraints.DefaultServiceProperties;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.math.Primes;
import org.bouncycastle.math.ec.WNafUtil;
import org.bouncycastle.util.BigIntegers;

public class RSAKeyPairGenerator
implements AsymmetricCipherKeyPairGenerator {
    private static final BigInteger ONE = BigInteger.valueOf(1L);
    private RSAKeyGenerationParameters param;

    @Override
    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.param = (RSAKeyGenerationParameters)keyGenerationParameters;
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties("RSAKeyGen", ConstraintUtils.bitsOfSecurityForFF(keyGenerationParameters.getStrength()), null, CryptoServicePurpose.KEYGEN));
    }

    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = null;
        boolean bl = false;
        int n2 = this.param.getStrength();
        int n3 = (n2 + 1) / 2;
        int n4 = n2 - n3;
        int n5 = n2 / 2 - 100;
        if (n5 < n2 / 3) {
            n5 = n2 / 3;
        }
        int n6 = n2 >> 2;
        BigInteger bigInteger = BigInteger.valueOf(2L).pow(n2 / 2);
        BigInteger bigInteger2 = ONE.shiftLeft(n2 - 1);
        BigInteger bigInteger3 = ONE.shiftLeft(n5);
        while (!bl) {
            BigInteger bigInteger4;
            BigInteger bigInteger5;
            BigInteger bigInteger6;
            BigInteger bigInteger7;
            BigInteger bigInteger8;
            BigInteger bigInteger9;
            BigInteger bigInteger10;
            BigInteger bigInteger11;
            BigInteger bigInteger12 = this.param.getPublicExponent();
            BigInteger bigInteger13 = this.chooseRandomPrime(n3, bigInteger12, bigInteger2);
            while (true) {
                if ((bigInteger11 = (bigInteger10 = this.chooseRandomPrime(n4, bigInteger12, bigInteger2)).subtract(bigInteger13).abs()).bitLength() < n5 || bigInteger11.compareTo(bigInteger3) <= 0) {
                    continue;
                }
                bigInteger9 = bigInteger13.multiply(bigInteger10);
                if (bigInteger9.bitLength() != n2) {
                    bigInteger13 = bigInteger13.max(bigInteger10);
                    continue;
                }
                if (WNafUtil.getNafWeight(bigInteger9) >= n6) break;
                bigInteger13 = this.chooseRandomPrime(n3, bigInteger12, bigInteger2);
            }
            if (bigInteger13.compareTo(bigInteger10) < 0) {
                bigInteger8 = bigInteger13;
                bigInteger13 = bigInteger10;
                bigInteger10 = bigInteger8;
            }
            if ((bigInteger7 = bigInteger12.modInverse(bigInteger6 = (bigInteger5 = bigInteger13.subtract(ONE)).divide(bigInteger8 = bigInteger5.gcd(bigInteger4 = bigInteger10.subtract(ONE))).multiply(bigInteger4))).compareTo(bigInteger) <= 0) continue;
            bl = true;
            bigInteger11 = bigInteger7.remainder(bigInteger5);
            BigInteger bigInteger14 = bigInteger7.remainder(bigInteger4);
            BigInteger bigInteger15 = BigIntegers.modOddInverse(bigInteger13, bigInteger10);
            asymmetricCipherKeyPair = new AsymmetricCipherKeyPair(new RSAKeyParameters(false, bigInteger9, bigInteger12, true), new RSAPrivateCrtKeyParameters(bigInteger9, bigInteger12, bigInteger7, bigInteger13, bigInteger10, bigInteger11, bigInteger14, bigInteger15, true));
        }
        return asymmetricCipherKeyPair;
    }

    protected BigInteger chooseRandomPrime(int n2, BigInteger bigInteger, BigInteger bigInteger2) {
        for (int i2 = 0; i2 != 5 * n2; ++i2) {
            BigInteger bigInteger3 = BigIntegers.createRandomPrime(n2, 1, this.param.getRandom());
            if (bigInteger3.mod(bigInteger).equals(ONE) || bigInteger3.multiply(bigInteger3).compareTo(bigInteger2) < 0 || !this.isProbablePrime(bigInteger3) || !bigInteger.gcd(bigInteger3.subtract(ONE)).equals(ONE)) continue;
            return bigInteger3;
        }
        throw new IllegalStateException("unable to generate prime number for RSA key");
    }

    protected boolean isProbablePrime(BigInteger bigInteger) {
        int n2 = RSAKeyPairGenerator.getNumberOfIterations(bigInteger.bitLength(), this.param.getCertainty());
        return !Primes.hasAnySmallFactors(bigInteger) && Primes.isMRProbablePrime(bigInteger, this.param.getRandom(), n2);
    }

    private static int getNumberOfIterations(int n2, int n3) {
        if (n2 >= 1536) {
            return n3 <= 100 ? 3 : (n3 <= 128 ? 4 : 4 + (n3 - 128 + 1) / 2);
        }
        if (n2 >= 1024) {
            return n3 <= 100 ? 4 : (n3 <= 112 ? 5 : 5 + (n3 - 112 + 1) / 2);
        }
        if (n2 >= 512) {
            return n3 <= 80 ? 5 : (n3 <= 100 ? 7 : 7 + (n3 - 100 + 1) / 2);
        }
        return n3 <= 80 ? 40 : 40 + (n3 - 80 + 1) / 2;
    }
}

