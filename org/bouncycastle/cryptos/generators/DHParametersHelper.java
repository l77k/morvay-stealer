/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.math.ec.WNafUtil;
import org.bouncycastle.util.BigIntegers;

class DHParametersHelper {
    private static final BigInteger ONE = BigInteger.valueOf(1L);
    private static final BigInteger TWO = BigInteger.valueOf(2L);

    DHParametersHelper() {
    }

    static BigInteger[] generateSafePrimes(int n2, int n3, SecureRandom secureRandom) {
        BigInteger bigInteger;
        BigInteger bigInteger2;
        int n4 = n2 - 1;
        int n5 = n2 >>> 2;
        while (!(bigInteger2 = (bigInteger = BigIntegers.createRandomPrime(n4, 2, secureRandom)).shiftLeft(1).add(ONE)).isProbablePrime(n3) || n3 > 2 && !bigInteger.isProbablePrime(n3 - 2) || WNafUtil.getNafWeight(bigInteger2) < n5) {
        }
        return new BigInteger[]{bigInteger2, bigInteger};
    }

    static BigInteger selectGenerator(BigInteger bigInteger, BigInteger bigInteger2, SecureRandom secureRandom) {
        BigInteger bigInteger3;
        BigInteger bigInteger4;
        BigInteger bigInteger5 = bigInteger.subtract(TWO);
        while ((bigInteger4 = (bigInteger3 = BigIntegers.createRandomInRange(TWO, bigInteger5, secureRandom)).modPow(TWO, bigInteger)).equals(ONE)) {
        }
        return bigInteger4;
    }
}

