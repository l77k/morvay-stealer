/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.math.ec.WNafUtil;
import org.bouncycastle.util.BigIntegers;

class DHKeyGeneratorHelper {
    static final DHKeyGeneratorHelper INSTANCE = new DHKeyGeneratorHelper();
    private static final BigInteger ONE = BigInteger.valueOf(1L);
    private static final BigInteger TWO = BigInteger.valueOf(2L);

    private DHKeyGeneratorHelper() {
    }

    BigInteger calculatePrivate(DHParameters dHParameters, SecureRandom secureRandom) {
        BigInteger bigInteger;
        BigInteger bigInteger2;
        int n2 = dHParameters.getL();
        if (n2 != 0) {
            BigInteger bigInteger3;
            int n3 = n2 >>> 2;
            while (WNafUtil.getNafWeight(bigInteger3 = BigIntegers.createRandomBigInteger(n2, secureRandom).setBit(n2 - 1)) < n3) {
            }
            return bigInteger3;
        }
        BigInteger bigInteger4 = TWO;
        int n4 = dHParameters.getM();
        if (n4 != 0) {
            bigInteger4 = ONE.shiftLeft(n4 - 1);
        }
        if ((bigInteger2 = dHParameters.getQ()) == null) {
            bigInteger2 = dHParameters.getP();
        }
        BigInteger bigInteger5 = bigInteger2.subtract(TWO);
        int n5 = bigInteger5.bitLength() >>> 2;
        while (WNafUtil.getNafWeight(bigInteger = BigIntegers.createRandomInRange(bigInteger4, bigInteger5, secureRandom)) < n5) {
        }
        return bigInteger;
    }

    BigInteger calculatePublic(DHParameters dHParameters, BigInteger bigInteger) {
        return dHParameters.getG().modPow(bigInteger, dHParameters.getP());
    }
}

