/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;

public abstract class Primes {
    public static final int SMALL_FACTOR_LIMIT = 211;
    private static final BigInteger ONE = BigInteger.valueOf(1L);
    private static final BigInteger TWO = BigInteger.valueOf(2L);
    private static final BigInteger THREE = BigInteger.valueOf(3L);

    public static STOutput generateSTRandomPrime(Digest digest, int n2, byte[] byArray) {
        if (digest == null) {
            throw new IllegalArgumentException("'hash' cannot be null");
        }
        if (n2 < 2) {
            throw new IllegalArgumentException("'length' must be >= 2");
        }
        if (byArray == null || byArray.length == 0) {
            throw new IllegalArgumentException("'inputSeed' cannot be null or empty");
        }
        return Primes.implSTRandomPrime(digest, n2, Arrays.clone(byArray));
    }

    public static MROutput enhancedMRProbablePrimeTest(BigInteger bigInteger, SecureRandom secureRandom, int n2) {
        Primes.checkCandidate(bigInteger, "candidate");
        if (secureRandom == null) {
            throw new IllegalArgumentException("'random' cannot be null");
        }
        if (n2 < 1) {
            throw new IllegalArgumentException("'iterations' must be > 0");
        }
        if (bigInteger.bitLength() == 2) {
            return MROutput.probablyPrime();
        }
        if (!bigInteger.testBit(0)) {
            return MROutput.provablyCompositeWithFactor(Primes.TWO);
        }
        BigInteger bigInteger2 = bigInteger;
        BigInteger bigInteger3 = bigInteger.subtract(ONE);
        BigInteger bigInteger4 = bigInteger.subtract(TWO);
        int n3 = bigInteger3.getLowestSetBit();
        BigInteger bigInteger5 = bigInteger3.shiftRight(n3);
        for (int i2 = 0; i2 < n2; ++i2) {
            BigInteger bigInteger6 = BigIntegers.createRandomInRange(TWO, bigInteger4, secureRandom);
            BigInteger bigInteger7 = bigInteger6.gcd(bigInteger2);
            if (bigInteger7.compareTo(ONE) > 0) {
                return MROutput.provablyCompositeWithFactor(bigInteger7);
            }
            BigInteger bigInteger8 = bigInteger6.modPow(bigInteger5, bigInteger2);
            if (bigInteger8.equals(ONE) || bigInteger8.equals(bigInteger3)) continue;
            boolean bl = false;
            BigInteger bigInteger9 = bigInteger8;
            for (int i3 = 1; i3 < n3; ++i3) {
                if ((bigInteger8 = bigInteger8.modPow(TWO, bigInteger2)).equals(bigInteger3)) {
                    bl = true;
                    break;
                }
                if (bigInteger8.equals(ONE)) break;
                bigInteger9 = bigInteger8;
            }
            if (bl) continue;
            if (!bigInteger8.equals(ONE)) {
                bigInteger9 = bigInteger8;
                if (!(bigInteger8 = bigInteger8.modPow(TWO, bigInteger2)).equals(ONE)) {
                    bigInteger9 = bigInteger8;
                }
            }
            if ((bigInteger7 = bigInteger9.subtract(ONE).gcd(bigInteger2)).compareTo(ONE) > 0) {
                return MROutput.provablyCompositeWithFactor(bigInteger7);
            }
            return MROutput.provablyCompositeNotPrimePower();
        }
        return MROutput.probablyPrime();
    }

    public static boolean hasAnySmallFactors(BigInteger bigInteger) {
        Primes.checkCandidate(bigInteger, "candidate");
        return Primes.implHasAnySmallFactors(bigInteger);
    }

    public static boolean isMRProbablePrime(BigInteger bigInteger, SecureRandom secureRandom, int n2) {
        Primes.checkCandidate(bigInteger, "candidate");
        if (secureRandom == null) {
            throw new IllegalArgumentException("'random' cannot be null");
        }
        if (n2 < 1) {
            throw new IllegalArgumentException("'iterations' must be > 0");
        }
        if (bigInteger.bitLength() == 2) {
            return true;
        }
        if (!bigInteger.testBit(0)) {
            return false;
        }
        BigInteger bigInteger2 = bigInteger;
        BigInteger bigInteger3 = bigInteger.subtract(ONE);
        BigInteger bigInteger4 = bigInteger.subtract(TWO);
        int n3 = bigInteger3.getLowestSetBit();
        BigInteger bigInteger5 = bigInteger3.shiftRight(n3);
        for (int i2 = 0; i2 < n2; ++i2) {
            BigInteger bigInteger6 = BigIntegers.createRandomInRange(TWO, bigInteger4, secureRandom);
            if (Primes.implMRProbablePrimeToBase(bigInteger2, bigInteger3, bigInteger5, n3, bigInteger6)) continue;
            return false;
        }
        return true;
    }

    public static boolean isMRProbablePrimeToBase(BigInteger bigInteger, BigInteger bigInteger2) {
        Primes.checkCandidate(bigInteger, "candidate");
        Primes.checkCandidate(bigInteger2, "base");
        if (bigInteger2.compareTo(bigInteger.subtract(ONE)) >= 0) {
            throw new IllegalArgumentException("'base' must be < ('candidate' - 1)");
        }
        if (bigInteger.bitLength() == 2) {
            return true;
        }
        BigInteger bigInteger3 = bigInteger;
        BigInteger bigInteger4 = bigInteger.subtract(ONE);
        int n2 = bigInteger4.getLowestSetBit();
        BigInteger bigInteger5 = bigInteger4.shiftRight(n2);
        return Primes.implMRProbablePrimeToBase(bigInteger3, bigInteger4, bigInteger5, n2, bigInteger2);
    }

    private static void checkCandidate(BigInteger bigInteger, String string) {
        if (bigInteger == null || bigInteger.signum() < 1 || bigInteger.bitLength() < 2) {
            throw new IllegalArgumentException("'" + string + "' must be non-null and >= 2");
        }
    }

    private static boolean implHasAnySmallFactors(BigInteger bigInteger) {
        int n2 = 223092870;
        int n3 = bigInteger.mod(BigInteger.valueOf(n2)).intValue();
        if (n3 % 2 == 0 || n3 % 3 == 0 || n3 % 5 == 0 || n3 % 7 == 0 || n3 % 11 == 0 || n3 % 13 == 0 || n3 % 17 == 0 || n3 % 19 == 0 || n3 % 23 == 0) {
            return true;
        }
        n2 = 58642669;
        n3 = bigInteger.mod(BigInteger.valueOf(n2)).intValue();
        if (n3 % 29 == 0 || n3 % 31 == 0 || n3 % 37 == 0 || n3 % 41 == 0 || n3 % 43 == 0) {
            return true;
        }
        n2 = 600662303;
        n3 = bigInteger.mod(BigInteger.valueOf(n2)).intValue();
        if (n3 % 47 == 0 || n3 % 53 == 0 || n3 % 59 == 0 || n3 % 61 == 0 || n3 % 67 == 0) {
            return true;
        }
        n2 = 33984931;
        n3 = bigInteger.mod(BigInteger.valueOf(n2)).intValue();
        if (n3 % 71 == 0 || n3 % 73 == 0 || n3 % 79 == 0 || n3 % 83 == 0) {
            return true;
        }
        n2 = 89809099;
        n3 = bigInteger.mod(BigInteger.valueOf(n2)).intValue();
        if (n3 % 89 == 0 || n3 % 97 == 0 || n3 % 101 == 0 || n3 % 103 == 0) {
            return true;
        }
        n2 = 167375713;
        n3 = bigInteger.mod(BigInteger.valueOf(n2)).intValue();
        if (n3 % 107 == 0 || n3 % 109 == 0 || n3 % 113 == 0 || n3 % 127 == 0) {
            return true;
        }
        n2 = 371700317;
        n3 = bigInteger.mod(BigInteger.valueOf(n2)).intValue();
        if (n3 % 131 == 0 || n3 % 137 == 0 || n3 % 139 == 0 || n3 % 149 == 0) {
            return true;
        }
        n2 = 645328247;
        n3 = bigInteger.mod(BigInteger.valueOf(n2)).intValue();
        if (n3 % 151 == 0 || n3 % 157 == 0 || n3 % 163 == 0 || n3 % 167 == 0) {
            return true;
        }
        n2 = 1070560157;
        n3 = bigInteger.mod(BigInteger.valueOf(n2)).intValue();
        if (n3 % 173 == 0 || n3 % 179 == 0 || n3 % 181 == 0 || n3 % 191 == 0) {
            return true;
        }
        n2 = 1596463769;
        n3 = bigInteger.mod(BigInteger.valueOf(n2)).intValue();
        return n3 % 193 == 0 || n3 % 197 == 0 || n3 % 199 == 0 || n3 % 211 == 0;
    }

    private static boolean implMRProbablePrimeToBase(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, int n2, BigInteger bigInteger4) {
        BigInteger bigInteger5 = bigInteger4.modPow(bigInteger3, bigInteger);
        if (bigInteger5.equals(ONE) || bigInteger5.equals(bigInteger2)) {
            return true;
        }
        boolean bl = false;
        for (int i2 = 1; i2 < n2; ++i2) {
            if ((bigInteger5 = bigInteger5.modPow(TWO, bigInteger)).equals(bigInteger2)) {
                bl = true;
                break;
            }
            if (!bigInteger5.equals(ONE)) continue;
            return false;
        }
        return bl;
    }

    private static STOutput implSTRandomPrime(Digest digest, int n2, byte[] byArray) {
        int n3 = digest.getDigestSize();
        if (n2 < 33) {
            int n4 = 0;
            byte[] byArray2 = new byte[n3];
            byte[] byArray3 = new byte[n3];
            do {
                Primes.hash(digest, byArray, byArray2, 0);
                Primes.inc(byArray, 1);
                Primes.hash(digest, byArray, byArray3, 0);
                Primes.inc(byArray, 1);
                int n5 = Primes.extract32(byArray2) ^ Primes.extract32(byArray3);
                n5 &= -1 >>> 32 - n2;
                ++n4;
                long l2 = (long)(n5 |= 1 << n2 - 1 | 1) & 0xFFFFFFFFL;
                if (!Primes.isPrime32(l2)) continue;
                return new STOutput(BigInteger.valueOf(l2), byArray, n4);
            } while (n4 <= 4 * n2);
            throw new IllegalStateException("Too many iterations in Shawe-Taylor Random_Prime Routine");
        }
        STOutput sTOutput = Primes.implSTRandomPrime(digest, (n2 + 3) / 2, byArray);
        BigInteger bigInteger = sTOutput.getPrime();
        byArray = sTOutput.getPrimeSeed();
        int n6 = sTOutput.getPrimeGenCounter();
        int n7 = 8 * n3;
        int n8 = (n2 - 1) / n7;
        int n9 = n6;
        BigInteger bigInteger2 = Primes.hashGen(digest, byArray, n8 + 1);
        bigInteger2 = bigInteger2.mod(ONE.shiftLeft(n2 - 1)).setBit(n2 - 1);
        BigInteger bigInteger3 = bigInteger.shiftLeft(1);
        BigInteger bigInteger4 = bigInteger2.subtract(ONE).divide(bigInteger3).add(ONE).shiftLeft(1);
        int n10 = 0;
        BigInteger bigInteger5 = bigInteger4.multiply(bigInteger).add(ONE);
        while (true) {
            if (bigInteger5.bitLength() > n2) {
                bigInteger4 = ONE.shiftLeft(n2 - 1).subtract(ONE).divide(bigInteger3).add(ONE).shiftLeft(1);
                bigInteger5 = bigInteger4.multiply(bigInteger).add(ONE);
            }
            ++n6;
            if (!Primes.implHasAnySmallFactors(bigInteger5)) {
                BigInteger bigInteger6 = Primes.hashGen(digest, byArray, n8 + 1);
                bigInteger6 = bigInteger6.mod(bigInteger5.subtract(THREE)).add(TWO);
                bigInteger4 = bigInteger4.add(BigInteger.valueOf(n10));
                n10 = 0;
                BigInteger bigInteger7 = bigInteger6.modPow(bigInteger4, bigInteger5);
                if (bigInteger5.gcd(bigInteger7.subtract(ONE)).equals(ONE) && bigInteger7.modPow(bigInteger, bigInteger5).equals(ONE)) {
                    return new STOutput(bigInteger5, byArray, n6);
                }
            } else {
                Primes.inc(byArray, n8 + 1);
            }
            if (n6 >= 4 * n2 + n9) {
                throw new IllegalStateException("Too many iterations in Shawe-Taylor Random_Prime Routine");
            }
            n10 += 2;
            bigInteger5 = bigInteger5.add(bigInteger3);
        }
    }

    private static int extract32(byte[] byArray) {
        int n2 = 0;
        int n3 = Math.min(4, byArray.length);
        for (int i2 = 0; i2 < n3; ++i2) {
            int n4 = byArray[byArray.length - (i2 + 1)] & 0xFF;
            n2 |= n4 << 8 * i2;
        }
        return n2;
    }

    private static void hash(Digest digest, byte[] byArray, byte[] byArray2, int n2) {
        digest.update(byArray, 0, byArray.length);
        digest.doFinal(byArray2, n2);
    }

    private static BigInteger hashGen(Digest digest, byte[] byArray, int n2) {
        int n3 = digest.getDigestSize();
        int n4 = n2 * n3;
        byte[] byArray2 = new byte[n4];
        for (int i2 = 0; i2 < n2; ++i2) {
            Primes.hash(digest, byArray, byArray2, n4 -= n3);
            Primes.inc(byArray, 1);
        }
        return new BigInteger(1, byArray2);
    }

    private static void inc(byte[] byArray, int n2) {
        int n3 = byArray.length;
        while (n2 > 0 && --n3 >= 0) {
            byArray[n3] = (byte)(n2 += byArray[n3] & 0xFF);
            n2 >>>= 8;
        }
    }

    private static boolean isPrime32(long l2) {
        if (l2 >>> 32 != 0L) {
            throw new IllegalArgumentException("Size limit exceeded");
        }
        if (l2 <= 5L) {
            return l2 == 2L || l2 == 3L || l2 == 5L;
        }
        if ((l2 & 1L) == 0L || l2 % 3L == 0L || l2 % 5L == 0L) {
            return false;
        }
        long[] lArray = new long[]{1L, 7L, 11L, 13L, 17L, 19L, 23L, 29L};
        long l3 = 0L;
        int n2 = 1;
        while (true) {
            if (n2 < lArray.length) {
                long l4 = l3 + lArray[n2];
                if (l2 % l4 == 0L) {
                    return l2 < 30L;
                }
                ++n2;
                continue;
            }
            if ((l3 += 30L) * l3 >= l2) {
                return true;
            }
            n2 = 0;
        }
    }

    public static class MROutput {
        private boolean provablyComposite;
        private BigInteger factor;

        private static MROutput probablyPrime() {
            return new MROutput(false, null);
        }

        private static MROutput provablyCompositeWithFactor(BigInteger bigInteger) {
            return new MROutput(true, bigInteger);
        }

        private static MROutput provablyCompositeNotPrimePower() {
            return new MROutput(true, null);
        }

        private MROutput(boolean bl, BigInteger bigInteger) {
            this.provablyComposite = bl;
            this.factor = bigInteger;
        }

        public BigInteger getFactor() {
            return this.factor;
        }

        public boolean isProvablyComposite() {
            return this.provablyComposite;
        }

        public boolean isNotPrimePower() {
            return this.provablyComposite && this.factor == null;
        }
    }

    public static class STOutput {
        private BigInteger prime;
        private byte[] primeSeed;
        private int primeGenCounter;

        private STOutput(BigInteger bigInteger, byte[] byArray, int n2) {
            this.prime = bigInteger;
            this.primeSeed = byArray;
            this.primeGenCounter = n2;
        }

        public BigInteger getPrime() {
            return this.prime;
        }

        public byte[] getPrimeSeed() {
            return this.primeSeed;
        }

        public int getPrimeGenCounter() {
            return this.primeGenCounter;
        }
    }
}

