/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.raw;

import java.math.BigInteger;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Pack;

public abstract class Nat {
    private static final long M = 0xFFFFFFFFL;

    public static int add(int n2, int[] nArray, int[] nArray2, int[] nArray3) {
        long l2 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray3[i2] = (int)(l2 += ((long)nArray[i2] & 0xFFFFFFFFL) + ((long)nArray2[i2] & 0xFFFFFFFFL));
            l2 >>>= 32;
        }
        return (int)l2;
    }

    public static int add33At(int n2, int n3, int[] nArray, int n4) {
        long l2 = ((long)nArray[n4 + 0] & 0xFFFFFFFFL) + ((long)n3 & 0xFFFFFFFFL);
        nArray[n4 + 0] = (int)l2;
        l2 >>>= 32;
        nArray[n4 + 1] = (int)(l2 += ((long)nArray[n4 + 1] & 0xFFFFFFFFL) + 1L);
        return (l2 >>>= 32) == 0L ? 0 : Nat.incAt(n2, nArray, n4 + 2);
    }

    public static int add33At(int n2, int n3, int[] nArray, int n4, int n5) {
        long l2 = ((long)nArray[n4 + n5] & 0xFFFFFFFFL) + ((long)n3 & 0xFFFFFFFFL);
        nArray[n4 + n5] = (int)l2;
        l2 >>>= 32;
        nArray[n4 + n5 + 1] = (int)(l2 += ((long)nArray[n4 + n5 + 1] & 0xFFFFFFFFL) + 1L);
        return (l2 >>>= 32) == 0L ? 0 : Nat.incAt(n2, nArray, n4, n5 + 2);
    }

    public static int add33To(int n2, int n3, int[] nArray) {
        long l2 = ((long)nArray[0] & 0xFFFFFFFFL) + ((long)n3 & 0xFFFFFFFFL);
        nArray[0] = (int)l2;
        l2 >>>= 32;
        nArray[1] = (int)(l2 += ((long)nArray[1] & 0xFFFFFFFFL) + 1L);
        return (l2 >>>= 32) == 0L ? 0 : Nat.incAt(n2, nArray, 2);
    }

    public static int add33To(int n2, int n3, int[] nArray, int n4) {
        long l2 = ((long)nArray[n4 + 0] & 0xFFFFFFFFL) + ((long)n3 & 0xFFFFFFFFL);
        nArray[n4 + 0] = (int)l2;
        l2 >>>= 32;
        nArray[n4 + 1] = (int)(l2 += ((long)nArray[n4 + 1] & 0xFFFFFFFFL) + 1L);
        return (l2 >>>= 32) == 0L ? 0 : Nat.incAt(n2, nArray, n4, 2);
    }

    public static int addBothTo(int n2, int[] nArray, int[] nArray2, int[] nArray3) {
        long l2 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray3[i2] = (int)(l2 += ((long)nArray[i2] & 0xFFFFFFFFL) + ((long)nArray2[i2] & 0xFFFFFFFFL) + ((long)nArray3[i2] & 0xFFFFFFFFL));
            l2 >>>= 32;
        }
        return (int)l2;
    }

    public static int addBothTo(int n2, int[] nArray, int n3, int[] nArray2, int n4, int[] nArray3, int n5) {
        long l2 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray3[n5 + i2] = (int)(l2 += ((long)nArray[n3 + i2] & 0xFFFFFFFFL) + ((long)nArray2[n4 + i2] & 0xFFFFFFFFL) + ((long)nArray3[n5 + i2] & 0xFFFFFFFFL));
            l2 >>>= 32;
        }
        return (int)l2;
    }

    public static int addDWordAt(int n2, long l2, int[] nArray, int n3) {
        long l3 = ((long)nArray[n3 + 0] & 0xFFFFFFFFL) + (l2 & 0xFFFFFFFFL);
        nArray[n3 + 0] = (int)l3;
        l3 >>>= 32;
        nArray[n3 + 1] = (int)(l3 += ((long)nArray[n3 + 1] & 0xFFFFFFFFL) + (l2 >>> 32));
        return (l3 >>>= 32) == 0L ? 0 : Nat.incAt(n2, nArray, n3 + 2);
    }

    public static int addDWordAt(int n2, long l2, int[] nArray, int n3, int n4) {
        long l3 = ((long)nArray[n3 + n4] & 0xFFFFFFFFL) + (l2 & 0xFFFFFFFFL);
        nArray[n3 + n4] = (int)l3;
        l3 >>>= 32;
        nArray[n3 + n4 + 1] = (int)(l3 += ((long)nArray[n3 + n4 + 1] & 0xFFFFFFFFL) + (l2 >>> 32));
        return (l3 >>>= 32) == 0L ? 0 : Nat.incAt(n2, nArray, n3, n4 + 2);
    }

    public static int addDWordTo(int n2, long l2, int[] nArray) {
        long l3 = ((long)nArray[0] & 0xFFFFFFFFL) + (l2 & 0xFFFFFFFFL);
        nArray[0] = (int)l3;
        l3 >>>= 32;
        nArray[1] = (int)(l3 += ((long)nArray[1] & 0xFFFFFFFFL) + (l2 >>> 32));
        return (l3 >>>= 32) == 0L ? 0 : Nat.incAt(n2, nArray, 2);
    }

    public static int addDWordTo(int n2, long l2, int[] nArray, int n3) {
        long l3 = ((long)nArray[n3 + 0] & 0xFFFFFFFFL) + (l2 & 0xFFFFFFFFL);
        nArray[n3 + 0] = (int)l3;
        l3 >>>= 32;
        nArray[n3 + 1] = (int)(l3 += ((long)nArray[n3 + 1] & 0xFFFFFFFFL) + (l2 >>> 32));
        return (l3 >>>= 32) == 0L ? 0 : Nat.incAt(n2, nArray, n3, 2);
    }

    public static int addTo(int n2, int[] nArray, int[] nArray2) {
        long l2 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray2[i2] = (int)(l2 += ((long)nArray[i2] & 0xFFFFFFFFL) + ((long)nArray2[i2] & 0xFFFFFFFFL));
            l2 >>>= 32;
        }
        return (int)l2;
    }

    public static int addTo(int n2, int[] nArray, int n3, int[] nArray2, int n4) {
        long l2 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray2[n4 + i2] = (int)(l2 += ((long)nArray[n3 + i2] & 0xFFFFFFFFL) + ((long)nArray2[n4 + i2] & 0xFFFFFFFFL));
            l2 >>>= 32;
        }
        return (int)l2;
    }

    public static int addTo(int n2, int[] nArray, int n3, int[] nArray2, int n4, int n5) {
        long l2 = (long)n5 & 0xFFFFFFFFL;
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray2[n4 + i2] = (int)(l2 += ((long)nArray[n3 + i2] & 0xFFFFFFFFL) + ((long)nArray2[n4 + i2] & 0xFFFFFFFFL));
            l2 >>>= 32;
        }
        return (int)l2;
    }

    public static int addToEachOther(int n2, int[] nArray, int n3, int[] nArray2, int n4) {
        long l2 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray[n3 + i2] = (int)(l2 += ((long)nArray[n3 + i2] & 0xFFFFFFFFL) + ((long)nArray2[n4 + i2] & 0xFFFFFFFFL));
            nArray2[n4 + i2] = (int)l2;
            l2 >>>= 32;
        }
        return (int)l2;
    }

    public static int addWordAt(int n2, int n3, int[] nArray, int n4) {
        long l2 = ((long)n3 & 0xFFFFFFFFL) + ((long)nArray[n4] & 0xFFFFFFFFL);
        nArray[n4] = (int)l2;
        return (l2 >>>= 32) == 0L ? 0 : Nat.incAt(n2, nArray, n4 + 1);
    }

    public static int addWordAt(int n2, int n3, int[] nArray, int n4, int n5) {
        long l2 = ((long)n3 & 0xFFFFFFFFL) + ((long)nArray[n4 + n5] & 0xFFFFFFFFL);
        nArray[n4 + n5] = (int)l2;
        return (l2 >>>= 32) == 0L ? 0 : Nat.incAt(n2, nArray, n4, n5 + 1);
    }

    public static int addWordTo(int n2, int n3, int[] nArray) {
        long l2 = ((long)n3 & 0xFFFFFFFFL) + ((long)nArray[0] & 0xFFFFFFFFL);
        nArray[0] = (int)l2;
        return (l2 >>>= 32) == 0L ? 0 : Nat.incAt(n2, nArray, 1);
    }

    public static int addWordTo(int n2, int n3, int[] nArray, int n4) {
        long l2 = ((long)n3 & 0xFFFFFFFFL) + ((long)nArray[n4] & 0xFFFFFFFFL);
        nArray[n4] = (int)l2;
        return (l2 >>>= 32) == 0L ? 0 : Nat.incAt(n2, nArray, n4, 1);
    }

    public static int cadd(int n2, int n3, int[] nArray, int[] nArray2, int[] nArray3) {
        long l2 = (long)(-(n3 & 1)) & 0xFFFFFFFFL;
        long l3 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray3[i2] = (int)(l3 += ((long)nArray[i2] & 0xFFFFFFFFL) + ((long)nArray2[i2] & l2));
            l3 >>>= 32;
        }
        return (int)l3;
    }

    public static int caddTo(int n2, int n3, int[] nArray, int[] nArray2) {
        long l2 = (long)(-(n3 & 1)) & 0xFFFFFFFFL;
        long l3 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray2[i2] = (int)(l3 += ((long)nArray2[i2] & 0xFFFFFFFFL) + ((long)nArray[i2] & l2));
            l3 >>>= 32;
        }
        return (int)l3;
    }

    public static void cmov(int n2, int n3, int[] nArray, int n4, int[] nArray2, int n5) {
        n3 = -(n3 & 1);
        for (int i2 = 0; i2 < n2; ++i2) {
            int n6 = nArray2[n5 + i2];
            int n7 = n6 ^ nArray[n4 + i2];
            nArray2[n5 + i2] = n6 ^= n7 & n3;
        }
    }

    public static int compare(int n2, int[] nArray, int[] nArray2) {
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            int n3 = nArray[i2] ^ Integer.MIN_VALUE;
            int n4 = nArray2[i2] ^ Integer.MIN_VALUE;
            if (n3 < n4) {
                return -1;
            }
            if (n3 <= n4) continue;
            return 1;
        }
        return 0;
    }

    public static int compare(int n2, int[] nArray, int n3, int[] nArray2, int n4) {
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            int n5 = nArray[n3 + i2] ^ Integer.MIN_VALUE;
            int n6 = nArray2[n4 + i2] ^ Integer.MIN_VALUE;
            if (n5 < n6) {
                return -1;
            }
            if (n5 <= n6) continue;
            return 1;
        }
        return 0;
    }

    public static int[] copy(int n2, int[] nArray) {
        int[] nArray2 = new int[n2];
        System.arraycopy(nArray, 0, nArray2, 0, n2);
        return nArray2;
    }

    public static void copy(int n2, int[] nArray, int[] nArray2) {
        System.arraycopy(nArray, 0, nArray2, 0, n2);
    }

    public static void copy(int n2, int[] nArray, int n3, int[] nArray2, int n4) {
        System.arraycopy(nArray, n3, nArray2, n4, n2);
    }

    public static long[] copy64(int n2, long[] lArray) {
        long[] lArray2 = new long[n2];
        System.arraycopy(lArray, 0, lArray2, 0, n2);
        return lArray2;
    }

    public static void copy64(int n2, long[] lArray, long[] lArray2) {
        System.arraycopy(lArray, 0, lArray2, 0, n2);
    }

    public static void copy64(int n2, long[] lArray, int n3, long[] lArray2, int n4) {
        System.arraycopy(lArray, n3, lArray2, n4, n2);
    }

    public static int[] create(int n2) {
        return new int[n2];
    }

    public static long[] create64(int n2) {
        return new long[n2];
    }

    public static int csub(int n2, int n3, int[] nArray, int[] nArray2, int[] nArray3) {
        long l2 = (long)(-(n3 & 1)) & 0xFFFFFFFFL;
        long l3 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray3[i2] = (int)(l3 += ((long)nArray[i2] & 0xFFFFFFFFL) - ((long)nArray2[i2] & l2));
            l3 >>= 32;
        }
        return (int)l3;
    }

    public static int csub(int n2, int n3, int[] nArray, int n4, int[] nArray2, int n5, int[] nArray3, int n6) {
        long l2 = (long)(-(n3 & 1)) & 0xFFFFFFFFL;
        long l3 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray3[n6 + i2] = (int)(l3 += ((long)nArray[n4 + i2] & 0xFFFFFFFFL) - ((long)nArray2[n5 + i2] & l2));
            l3 >>= 32;
        }
        return (int)l3;
    }

    public static int dec(int n2, int[] nArray) {
        int n3 = 0;
        while (n3 < n2) {
            int n4 = n3++;
            nArray[n4] = nArray[n4] - 1;
            if (nArray[n4] == -1) continue;
            return 0;
        }
        return -1;
    }

    public static int dec(int n2, int[] nArray, int[] nArray2) {
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3;
            nArray2[i2] = n3 = nArray[i2] - 1;
            if (n3 == -1) continue;
            while (i2 < n2) {
                nArray2[i2] = nArray[i2];
                ++i2;
            }
            return 0;
        }
        return -1;
    }

    public static int decAt(int n2, int[] nArray, int n3) {
        int n4 = n3;
        while (n4 < n2) {
            int n5 = n4++;
            nArray[n5] = nArray[n5] - 1;
            if (nArray[n5] == -1) continue;
            return 0;
        }
        return -1;
    }

    public static int decAt(int n2, int[] nArray, int n3, int n4) {
        for (int i2 = n4; i2 < n2; ++i2) {
            int n5 = n3 + i2;
            nArray[n5] = nArray[n5] - 1;
            if (nArray[n5] == -1) continue;
            return 0;
        }
        return -1;
    }

    public static boolean diff(int n2, int[] nArray, int n3, int[] nArray2, int n4, int[] nArray3, int n5) {
        boolean bl = Nat.gte(n2, nArray, n3, nArray2, n4);
        if (bl) {
            Nat.sub(n2, nArray, n3, nArray2, n4, nArray3, n5);
        } else {
            Nat.sub(n2, nArray2, n4, nArray, n3, nArray3, n5);
        }
        return bl;
    }

    public static boolean eq(int n2, int[] nArray, int[] nArray2) {
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            if (nArray[i2] == nArray2[i2]) continue;
            return false;
        }
        return true;
    }

    public static int equalTo(int n2, int[] nArray, int n3) {
        int n4 = nArray[0] ^ n3;
        for (int i2 = 1; i2 < n2; ++i2) {
            n4 |= nArray[i2];
        }
        n4 = n4 >>> 1 | n4 & 1;
        return n4 - 1 >> 31;
    }

    public static int equalTo(int n2, int[] nArray, int n3, int n4) {
        int n5 = nArray[n3] ^ n4;
        for (int i2 = 1; i2 < n2; ++i2) {
            n5 |= nArray[n3 + i2];
        }
        n5 = n5 >>> 1 | n5 & 1;
        return n5 - 1 >> 31;
    }

    public static int equalTo(int n2, int[] nArray, int[] nArray2) {
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            n3 |= nArray[i2] ^ nArray2[i2];
        }
        n3 = n3 >>> 1 | n3 & 1;
        return n3 - 1 >> 31;
    }

    public static int equalTo(int n2, int[] nArray, int n3, int[] nArray2, int n4) {
        int n5 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            n5 |= nArray[n3 + i2] ^ nArray2[n4 + i2];
        }
        n5 = n5 >>> 1 | n5 & 1;
        return n5 - 1 >> 31;
    }

    public static int equalToZero(int n2, int[] nArray) {
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            n3 |= nArray[i2];
        }
        n3 = n3 >>> 1 | n3 & 1;
        return n3 - 1 >> 31;
    }

    public static int equalToZero(int n2, int[] nArray, int n3) {
        int n4 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            n4 |= nArray[n3 + i2];
        }
        n4 = n4 >>> 1 | n4 & 1;
        return n4 - 1 >> 31;
    }

    public static int[] fromBigInteger(int n2, BigInteger bigInteger) {
        if (bigInteger.signum() < 0 || bigInteger.bitLength() > n2) {
            throw new IllegalArgumentException();
        }
        int n3 = n2 + 31 >> 5;
        int[] nArray = Nat.create(n3);
        for (int i2 = 0; i2 < n3; ++i2) {
            nArray[i2] = bigInteger.intValue();
            bigInteger = bigInteger.shiftRight(32);
        }
        return nArray;
    }

    public static long[] fromBigInteger64(int n2, BigInteger bigInteger) {
        if (bigInteger.signum() < 0 || bigInteger.bitLength() > n2) {
            throw new IllegalArgumentException();
        }
        int n3 = n2 + 63 >> 6;
        long[] lArray = Nat.create64(n3);
        for (int i2 = 0; i2 < n3; ++i2) {
            lArray[i2] = bigInteger.longValue();
            bigInteger = bigInteger.shiftRight(64);
        }
        return lArray;
    }

    public static int getBit(int[] nArray, int n2) {
        if (n2 == 0) {
            return nArray[0] & 1;
        }
        int n3 = n2 >> 5;
        if (n3 < 0 || n3 >= nArray.length) {
            return 0;
        }
        int n4 = n2 & 0x1F;
        return nArray[n3] >>> n4 & 1;
    }

    public static int getBitLength(int n2, int[] nArray) {
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            int n3 = nArray[i2];
            if (n3 == 0) continue;
            return i2 * 32 + 32 - Integers.numberOfLeadingZeros(n3);
        }
        return 0;
    }

    public static int getBitLength(int n2, int[] nArray, int n3) {
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            int n4 = nArray[n3 + i2];
            if (n4 == 0) continue;
            return i2 * 32 + 32 - Integers.numberOfLeadingZeros(n4);
        }
        return 0;
    }

    public static boolean gte(int n2, int[] nArray, int[] nArray2) {
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            int n3 = nArray[i2] ^ Integer.MIN_VALUE;
            int n4 = nArray2[i2] ^ Integer.MIN_VALUE;
            if (n3 < n4) {
                return false;
            }
            if (n3 <= n4) continue;
            return true;
        }
        return true;
    }

    public static boolean gte(int n2, int[] nArray, int n3, int[] nArray2, int n4) {
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            int n5 = nArray[n3 + i2] ^ Integer.MIN_VALUE;
            int n6 = nArray2[n4 + i2] ^ Integer.MIN_VALUE;
            if (n5 < n6) {
                return false;
            }
            if (n5 <= n6) continue;
            return true;
        }
        return true;
    }

    public static int inc(int n2, int[] nArray) {
        int n3 = 0;
        while (n3 < n2) {
            int n4 = n3++;
            nArray[n4] = nArray[n4] + 1;
            if (nArray[n4] == 0) continue;
            return 0;
        }
        return 1;
    }

    public static int inc(int n2, int[] nArray, int[] nArray2) {
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3;
            nArray2[i2] = n3 = nArray[i2] + 1;
            if (n3 == 0) continue;
            while (i2 < n2) {
                nArray2[i2] = nArray[i2];
                ++i2;
            }
            return 0;
        }
        return 1;
    }

    public static int incAt(int n2, int[] nArray, int n3) {
        int n4 = n3;
        while (n4 < n2) {
            int n5 = n4++;
            nArray[n5] = nArray[n5] + 1;
            if (nArray[n5] == 0) continue;
            return 0;
        }
        return 1;
    }

    public static int incAt(int n2, int[] nArray, int n3, int n4) {
        for (int i2 = n4; i2 < n2; ++i2) {
            int n5 = n3 + i2;
            nArray[n5] = nArray[n5] + 1;
            if (nArray[n5] == 0) continue;
            return 0;
        }
        return 1;
    }

    public static boolean isOne(int n2, int[] nArray) {
        if (nArray[0] != 1) {
            return false;
        }
        for (int i2 = 1; i2 < n2; ++i2) {
            if (nArray[i2] == 0) continue;
            return false;
        }
        return true;
    }

    public static boolean isZero(int n2, int[] nArray) {
        for (int i2 = 0; i2 < n2; ++i2) {
            if (nArray[i2] == 0) continue;
            return false;
        }
        return true;
    }

    public static int lessThan(int n2, int[] nArray, int[] nArray2) {
        long l2 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            l2 += ((long)nArray[i2] & 0xFFFFFFFFL) - ((long)nArray2[i2] & 0xFFFFFFFFL);
            l2 >>= 32;
        }
        return (int)l2;
    }

    public static int lessThan(int n2, int[] nArray, int n3, int[] nArray2, int n4) {
        long l2 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            l2 += ((long)nArray[n3 + i2] & 0xFFFFFFFFL) - ((long)nArray2[n4 + i2] & 0xFFFFFFFFL);
            l2 >>= 32;
        }
        return (int)l2;
    }

    public static void mul(int n2, int[] nArray, int[] nArray2, int[] nArray3) {
        nArray3[n2] = Nat.mulWord(n2, nArray[0], nArray2, nArray3);
        for (int i2 = 1; i2 < n2; ++i2) {
            nArray3[i2 + n2] = Nat.mulWordAddTo(n2, nArray[i2], nArray2, 0, nArray3, i2);
        }
    }

    public static void mul(int n2, int[] nArray, int n3, int[] nArray2, int n4, int[] nArray3, int n5) {
        nArray3[n5 + n2] = Nat.mulWord(n2, nArray[n3], nArray2, n4, nArray3, n5);
        for (int i2 = 1; i2 < n2; ++i2) {
            nArray3[n5 + i2 + n2] = Nat.mulWordAddTo(n2, nArray[n3 + i2], nArray2, n4, nArray3, n5 + i2);
        }
    }

    public static void mul(int[] nArray, int n2, int n3, int[] nArray2, int n4, int n5, int[] nArray3, int n6) {
        nArray3[n6 + n5] = Nat.mulWord(n5, nArray[n2], nArray2, n4, nArray3, n6);
        for (int i2 = 1; i2 < n3; ++i2) {
            nArray3[n6 + i2 + n5] = Nat.mulWordAddTo(n5, nArray[n2 + i2], nArray2, n4, nArray3, n6 + i2);
        }
    }

    public static int mulAddTo(int n2, int[] nArray, int[] nArray2, int[] nArray3) {
        long l2 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            l2 += (long)Nat.mulWordAddTo(n2, nArray[i2], nArray2, 0, nArray3, i2) & 0xFFFFFFFFL;
            nArray3[i2 + n2] = (int)(l2 += (long)nArray3[i2 + n2] & 0xFFFFFFFFL);
            l2 >>>= 32;
        }
        return (int)l2;
    }

    public static int mulAddTo(int n2, int[] nArray, int n3, int[] nArray2, int n4, int[] nArray3, int n5) {
        long l2 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            l2 += (long)Nat.mulWordAddTo(n2, nArray[n3 + i2], nArray2, n4, nArray3, n5) & 0xFFFFFFFFL;
            nArray3[n5 + n2] = (int)(l2 += (long)nArray3[n5 + n2] & 0xFFFFFFFFL);
            l2 >>>= 32;
            ++n5;
        }
        return (int)l2;
    }

    public static int mul31BothAdd(int n2, int n3, int[] nArray, int n4, int[] nArray2, int[] nArray3, int n5) {
        long l2 = 0L;
        long l3 = (long)n3 & 0xFFFFFFFFL;
        long l4 = (long)n4 & 0xFFFFFFFFL;
        int n6 = 0;
        do {
            nArray3[n5 + n6] = (int)(l2 += l3 * ((long)nArray[n6] & 0xFFFFFFFFL) + l4 * ((long)nArray2[n6] & 0xFFFFFFFFL) + ((long)nArray3[n5 + n6] & 0xFFFFFFFFL));
            l2 >>>= 32;
        } while (++n6 < n2);
        return (int)l2;
    }

    public static int mulWord(int n2, int n3, int[] nArray, int[] nArray2) {
        long l2 = 0L;
        long l3 = (long)n3 & 0xFFFFFFFFL;
        int n4 = 0;
        do {
            nArray2[n4] = (int)(l2 += l3 * ((long)nArray[n4] & 0xFFFFFFFFL));
            l2 >>>= 32;
        } while (++n4 < n2);
        return (int)l2;
    }

    public static int mulWord(int n2, int n3, int[] nArray, int n4, int[] nArray2, int n5) {
        long l2 = 0L;
        long l3 = (long)n3 & 0xFFFFFFFFL;
        int n6 = 0;
        do {
            nArray2[n5 + n6] = (int)(l2 += l3 * ((long)nArray[n4 + n6] & 0xFFFFFFFFL));
            l2 >>>= 32;
        } while (++n6 < n2);
        return (int)l2;
    }

    public static int mulWordAddTo(int n2, int n3, int[] nArray, int n4, int[] nArray2, int n5) {
        long l2 = 0L;
        long l3 = (long)n3 & 0xFFFFFFFFL;
        int n6 = 0;
        do {
            nArray2[n5 + n6] = (int)(l2 += l3 * ((long)nArray[n4 + n6] & 0xFFFFFFFFL) + ((long)nArray2[n5 + n6] & 0xFFFFFFFFL));
            l2 >>>= 32;
        } while (++n6 < n2);
        return (int)l2;
    }

    public static int mulWordDwordAddAt(int n2, int n3, long l2, int[] nArray, int n4) {
        long l3 = 0L;
        long l4 = (long)n3 & 0xFFFFFFFFL;
        nArray[n4 + 0] = (int)(l3 += l4 * (l2 & 0xFFFFFFFFL) + ((long)nArray[n4 + 0] & 0xFFFFFFFFL));
        l3 >>>= 32;
        nArray[n4 + 1] = (int)(l3 += l4 * (l2 >>> 32) + ((long)nArray[n4 + 1] & 0xFFFFFFFFL));
        l3 >>>= 32;
        nArray[n4 + 2] = (int)(l3 += (long)nArray[n4 + 2] & 0xFFFFFFFFL);
        return (l3 >>>= 32) == 0L ? 0 : Nat.incAt(n2, nArray, n4 + 3);
    }

    public static int shiftDownBit(int n2, int[] nArray, int n3) {
        int n4 = n2;
        while (--n4 >= 0) {
            int n5 = nArray[n4];
            nArray[n4] = n5 >>> 1 | n3 << 31;
            n3 = n5;
        }
        return n3 << 31;
    }

    public static int shiftDownBit(int n2, int[] nArray, int n3, int n4) {
        int n5 = n2;
        while (--n5 >= 0) {
            int n6 = nArray[n3 + n5];
            nArray[n3 + n5] = n6 >>> 1 | n4 << 31;
            n4 = n6;
        }
        return n4 << 31;
    }

    public static int shiftDownBit(int n2, int[] nArray, int n3, int[] nArray2) {
        int n4 = n2;
        while (--n4 >= 0) {
            int n5 = nArray[n4];
            nArray2[n4] = n5 >>> 1 | n3 << 31;
            n3 = n5;
        }
        return n3 << 31;
    }

    public static int shiftDownBit(int n2, int[] nArray, int n3, int n4, int[] nArray2, int n5) {
        int n6 = n2;
        while (--n6 >= 0) {
            int n7 = nArray[n3 + n6];
            nArray2[n5 + n6] = n7 >>> 1 | n4 << 31;
            n4 = n7;
        }
        return n4 << 31;
    }

    public static int shiftDownBits(int n2, int[] nArray, int n3, int n4) {
        int n5 = n2;
        while (--n5 >= 0) {
            int n6 = nArray[n5];
            nArray[n5] = n6 >>> n3 | n4 << -n3;
            n4 = n6;
        }
        return n4 << -n3;
    }

    public static int shiftDownBits(int n2, int[] nArray, int n3, int n4, int n5) {
        int n6 = n2;
        while (--n6 >= 0) {
            int n7 = nArray[n3 + n6];
            nArray[n3 + n6] = n7 >>> n4 | n5 << -n4;
            n5 = n7;
        }
        return n5 << -n4;
    }

    public static int shiftDownBits(int n2, int[] nArray, int n3, int n4, int[] nArray2) {
        int n5 = n2;
        while (--n5 >= 0) {
            int n6 = nArray[n5];
            nArray2[n5] = n6 >>> n3 | n4 << -n3;
            n4 = n6;
        }
        return n4 << -n3;
    }

    public static int shiftDownBits(int n2, int[] nArray, int n3, int n4, int n5, int[] nArray2, int n6) {
        int n7 = n2;
        while (--n7 >= 0) {
            int n8 = nArray[n3 + n7];
            nArray2[n6 + n7] = n8 >>> n4 | n5 << -n4;
            n5 = n8;
        }
        return n5 << -n4;
    }

    public static int shiftDownWord(int n2, int[] nArray, int n3) {
        int n4 = n2;
        while (--n4 >= 0) {
            int n5 = nArray[n4];
            nArray[n4] = n3;
            n3 = n5;
        }
        return n3;
    }

    public static int shiftUpBit(int n2, int[] nArray, int n3) {
        for (int i2 = 0; i2 < n2; ++i2) {
            int n4 = nArray[i2];
            nArray[i2] = n4 << 1 | n3 >>> 31;
            n3 = n4;
        }
        return n3 >>> 31;
    }

    public static int shiftUpBit(int n2, int[] nArray, int n3, int n4) {
        for (int i2 = 0; i2 < n2; ++i2) {
            int n5 = nArray[n3 + i2];
            nArray[n3 + i2] = n5 << 1 | n4 >>> 31;
            n4 = n5;
        }
        return n4 >>> 31;
    }

    public static int shiftUpBit(int n2, int[] nArray, int n3, int[] nArray2) {
        for (int i2 = 0; i2 < n2; ++i2) {
            int n4 = nArray[i2];
            nArray2[i2] = n4 << 1 | n3 >>> 31;
            n3 = n4;
        }
        return n3 >>> 31;
    }

    public static int shiftUpBit(int n2, int[] nArray, int n3, int n4, int[] nArray2, int n5) {
        for (int i2 = 0; i2 < n2; ++i2) {
            int n6 = nArray[n3 + i2];
            nArray2[n5 + i2] = n6 << 1 | n4 >>> 31;
            n4 = n6;
        }
        return n4 >>> 31;
    }

    public static long shiftUpBit64(int n2, long[] lArray, int n3, long l2, long[] lArray2, int n4) {
        for (int i2 = 0; i2 < n2; ++i2) {
            long l3 = lArray[n3 + i2];
            lArray2[n4 + i2] = l3 << 1 | l2 >>> 63;
            l2 = l3;
        }
        return l2 >>> 63;
    }

    public static int shiftUpBits(int n2, int[] nArray, int n3, int n4) {
        for (int i2 = 0; i2 < n2; ++i2) {
            int n5 = nArray[i2];
            nArray[i2] = n5 << n3 | n4 >>> -n3;
            n4 = n5;
        }
        return n4 >>> -n3;
    }

    public static int shiftUpBits(int n2, int[] nArray, int n3, int n4, int n5) {
        for (int i2 = 0; i2 < n2; ++i2) {
            int n6 = nArray[n3 + i2];
            nArray[n3 + i2] = n6 << n4 | n5 >>> -n4;
            n5 = n6;
        }
        return n5 >>> -n4;
    }

    public static long shiftUpBits64(int n2, long[] lArray, int n3, int n4, long l2) {
        for (int i2 = 0; i2 < n2; ++i2) {
            long l3 = lArray[n3 + i2];
            lArray[n3 + i2] = l3 << n4 | l2 >>> -n4;
            l2 = l3;
        }
        return l2 >>> -n4;
    }

    public static int shiftUpBits(int n2, int[] nArray, int n3, int n4, int[] nArray2) {
        for (int i2 = 0; i2 < n2; ++i2) {
            int n5 = nArray[i2];
            nArray2[i2] = n5 << n3 | n4 >>> -n3;
            n4 = n5;
        }
        return n4 >>> -n3;
    }

    public static int shiftUpBits(int n2, int[] nArray, int n3, int n4, int n5, int[] nArray2, int n6) {
        for (int i2 = 0; i2 < n2; ++i2) {
            int n7 = nArray[n3 + i2];
            nArray2[n6 + i2] = n7 << n4 | n5 >>> -n4;
            n5 = n7;
        }
        return n5 >>> -n4;
    }

    public static long shiftUpBits64(int n2, long[] lArray, int n3, int n4, long l2, long[] lArray2, int n5) {
        for (int i2 = 0; i2 < n2; ++i2) {
            long l3 = lArray[n3 + i2];
            lArray2[n5 + i2] = l3 << n4 | l2 >>> -n4;
            l2 = l3;
        }
        return l2 >>> -n4;
    }

    public static void square(int n2, int[] nArray, int[] nArray2) {
        long l2;
        int n3 = n2 << 1;
        int n4 = 0;
        int n5 = n2;
        int n6 = n3;
        do {
            l2 = (long)nArray[--n5] & 0xFFFFFFFFL;
            long l3 = l2 * l2;
            nArray2[--n6] = n4 << 31 | (int)(l3 >>> 33);
            nArray2[--n6] = (int)(l3 >>> 1);
            n4 = (int)l3;
        } while (n5 > 0);
        l2 = 0L;
        int n7 = 2;
        for (int i2 = 1; i2 < n2; ++i2) {
            l2 += (long)Nat.squareWordAddTo(nArray, i2, nArray2) & 0xFFFFFFFFL;
            l2 += (long)nArray2[n7] & 0xFFFFFFFFL;
            nArray2[n7++] = (int)l2;
            l2 >>>= 32;
            l2 += (long)nArray2[n7] & 0xFFFFFFFFL;
            nArray2[n7++] = (int)l2;
            l2 >>>= 32;
        }
        Nat.shiftUpBit(n3, nArray2, nArray[0] << 31);
    }

    public static void square(int n2, int[] nArray, int n3, int[] nArray2, int n4) {
        long l2;
        int n5 = n2 << 1;
        int n6 = 0;
        int n7 = n2;
        int n8 = n5;
        do {
            l2 = (long)nArray[n3 + --n7] & 0xFFFFFFFFL;
            long l3 = l2 * l2;
            nArray2[n4 + --n8] = n6 << 31 | (int)(l3 >>> 33);
            nArray2[n4 + --n8] = (int)(l3 >>> 1);
            n6 = (int)l3;
        } while (n7 > 0);
        l2 = 0L;
        int n9 = n4 + 2;
        for (int i2 = 1; i2 < n2; ++i2) {
            l2 += (long)Nat.squareWordAddTo(nArray, n3, i2, nArray2, n4) & 0xFFFFFFFFL;
            l2 += (long)nArray2[n9] & 0xFFFFFFFFL;
            nArray2[n9++] = (int)l2;
            l2 >>>= 32;
            l2 += (long)nArray2[n9] & 0xFFFFFFFFL;
            nArray2[n9++] = (int)l2;
            l2 >>>= 32;
        }
        Nat.shiftUpBit(n5, nArray2, n4, nArray[n3] << 31);
    }

    public static int squareWordAddTo(int[] nArray, int n2, int[] nArray2) {
        long l2 = 0L;
        long l3 = (long)nArray[n2] & 0xFFFFFFFFL;
        int n3 = 0;
        do {
            nArray2[n2 + n3] = (int)(l2 += l3 * ((long)nArray[n3] & 0xFFFFFFFFL) + ((long)nArray2[n2 + n3] & 0xFFFFFFFFL));
            l2 >>>= 32;
        } while (++n3 < n2);
        return (int)l2;
    }

    public static int squareWordAddTo(int[] nArray, int n2, int n3, int[] nArray2, int n4) {
        long l2 = 0L;
        long l3 = (long)nArray[n2 + n3] & 0xFFFFFFFFL;
        int n5 = 0;
        do {
            nArray2[n3 + n4] = (int)(l2 += l3 * ((long)nArray[n2 + n5] & 0xFFFFFFFFL) + ((long)nArray2[n3 + n4] & 0xFFFFFFFFL));
            l2 >>>= 32;
            ++n4;
        } while (++n5 < n3);
        return (int)l2;
    }

    public static int sub(int n2, int[] nArray, int[] nArray2, int[] nArray3) {
        long l2 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray3[i2] = (int)(l2 += ((long)nArray[i2] & 0xFFFFFFFFL) - ((long)nArray2[i2] & 0xFFFFFFFFL));
            l2 >>= 32;
        }
        return (int)l2;
    }

    public static int sub(int n2, int[] nArray, int n3, int[] nArray2, int n4, int[] nArray3, int n5) {
        long l2 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray3[n5 + i2] = (int)(l2 += ((long)nArray[n3 + i2] & 0xFFFFFFFFL) - ((long)nArray2[n4 + i2] & 0xFFFFFFFFL));
            l2 >>= 32;
        }
        return (int)l2;
    }

    public static int sub33At(int n2, int n3, int[] nArray, int n4) {
        long l2 = ((long)nArray[n4 + 0] & 0xFFFFFFFFL) - ((long)n3 & 0xFFFFFFFFL);
        nArray[n4 + 0] = (int)l2;
        l2 >>= 32;
        nArray[n4 + 1] = (int)(l2 += ((long)nArray[n4 + 1] & 0xFFFFFFFFL) - 1L);
        return (l2 >>= 32) == 0L ? 0 : Nat.decAt(n2, nArray, n4 + 2);
    }

    public static int sub33At(int n2, int n3, int[] nArray, int n4, int n5) {
        long l2 = ((long)nArray[n4 + n5] & 0xFFFFFFFFL) - ((long)n3 & 0xFFFFFFFFL);
        nArray[n4 + n5] = (int)l2;
        l2 >>= 32;
        nArray[n4 + n5 + 1] = (int)(l2 += ((long)nArray[n4 + n5 + 1] & 0xFFFFFFFFL) - 1L);
        return (l2 >>= 32) == 0L ? 0 : Nat.decAt(n2, nArray, n4, n5 + 2);
    }

    public static int sub33From(int n2, int n3, int[] nArray) {
        long l2 = ((long)nArray[0] & 0xFFFFFFFFL) - ((long)n3 & 0xFFFFFFFFL);
        nArray[0] = (int)l2;
        l2 >>= 32;
        nArray[1] = (int)(l2 += ((long)nArray[1] & 0xFFFFFFFFL) - 1L);
        return (l2 >>= 32) == 0L ? 0 : Nat.decAt(n2, nArray, 2);
    }

    public static int sub33From(int n2, int n3, int[] nArray, int n4) {
        long l2 = ((long)nArray[n4 + 0] & 0xFFFFFFFFL) - ((long)n3 & 0xFFFFFFFFL);
        nArray[n4 + 0] = (int)l2;
        l2 >>= 32;
        nArray[n4 + 1] = (int)(l2 += ((long)nArray[n4 + 1] & 0xFFFFFFFFL) - 1L);
        return (l2 >>= 32) == 0L ? 0 : Nat.decAt(n2, nArray, n4, 2);
    }

    public static int subBothFrom(int n2, int[] nArray, int[] nArray2, int[] nArray3) {
        long l2 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray3[i2] = (int)(l2 += ((long)nArray3[i2] & 0xFFFFFFFFL) - ((long)nArray[i2] & 0xFFFFFFFFL) - ((long)nArray2[i2] & 0xFFFFFFFFL));
            l2 >>= 32;
        }
        return (int)l2;
    }

    public static int subBothFrom(int n2, int[] nArray, int n3, int[] nArray2, int n4, int[] nArray3, int n5) {
        long l2 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray3[n5 + i2] = (int)(l2 += ((long)nArray3[n5 + i2] & 0xFFFFFFFFL) - ((long)nArray[n3 + i2] & 0xFFFFFFFFL) - ((long)nArray2[n4 + i2] & 0xFFFFFFFFL));
            l2 >>= 32;
        }
        return (int)l2;
    }

    public static int subDWordAt(int n2, long l2, int[] nArray, int n3) {
        long l3 = ((long)nArray[n3 + 0] & 0xFFFFFFFFL) - (l2 & 0xFFFFFFFFL);
        nArray[n3 + 0] = (int)l3;
        l3 >>= 32;
        nArray[n3 + 1] = (int)(l3 += ((long)nArray[n3 + 1] & 0xFFFFFFFFL) - (l2 >>> 32));
        return (l3 >>= 32) == 0L ? 0 : Nat.decAt(n2, nArray, n3 + 2);
    }

    public static int subDWordAt(int n2, long l2, int[] nArray, int n3, int n4) {
        long l3 = ((long)nArray[n3 + n4] & 0xFFFFFFFFL) - (l2 & 0xFFFFFFFFL);
        nArray[n3 + n4] = (int)l3;
        l3 >>= 32;
        nArray[n3 + n4 + 1] = (int)(l3 += ((long)nArray[n3 + n4 + 1] & 0xFFFFFFFFL) - (l2 >>> 32));
        return (l3 >>= 32) == 0L ? 0 : Nat.decAt(n2, nArray, n3, n4 + 2);
    }

    public static int subDWordFrom(int n2, long l2, int[] nArray) {
        long l3 = ((long)nArray[0] & 0xFFFFFFFFL) - (l2 & 0xFFFFFFFFL);
        nArray[0] = (int)l3;
        l3 >>= 32;
        nArray[1] = (int)(l3 += ((long)nArray[1] & 0xFFFFFFFFL) - (l2 >>> 32));
        return (l3 >>= 32) == 0L ? 0 : Nat.decAt(n2, nArray, 2);
    }

    public static int subDWordFrom(int n2, long l2, int[] nArray, int n3) {
        long l3 = ((long)nArray[n3 + 0] & 0xFFFFFFFFL) - (l2 & 0xFFFFFFFFL);
        nArray[n3 + 0] = (int)l3;
        l3 >>= 32;
        nArray[n3 + 1] = (int)(l3 += ((long)nArray[n3 + 1] & 0xFFFFFFFFL) - (l2 >>> 32));
        return (l3 >>= 32) == 0L ? 0 : Nat.decAt(n2, nArray, n3, 2);
    }

    public static int subFrom(int n2, int[] nArray, int[] nArray2) {
        long l2 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray2[i2] = (int)(l2 += ((long)nArray2[i2] & 0xFFFFFFFFL) - ((long)nArray[i2] & 0xFFFFFFFFL));
            l2 >>= 32;
        }
        return (int)l2;
    }

    public static int subFrom(int n2, int[] nArray, int n3, int[] nArray2, int n4) {
        long l2 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray2[n4 + i2] = (int)(l2 += ((long)nArray2[n4 + i2] & 0xFFFFFFFFL) - ((long)nArray[n3 + i2] & 0xFFFFFFFFL));
            l2 >>= 32;
        }
        return (int)l2;
    }

    public static int subWordAt(int n2, int n3, int[] nArray, int n4) {
        long l2 = ((long)nArray[n4] & 0xFFFFFFFFL) - ((long)n3 & 0xFFFFFFFFL);
        nArray[n4] = (int)l2;
        return (l2 >>= 32) == 0L ? 0 : Nat.decAt(n2, nArray, n4 + 1);
    }

    public static int subWordAt(int n2, int n3, int[] nArray, int n4, int n5) {
        long l2 = ((long)nArray[n4 + n5] & 0xFFFFFFFFL) - ((long)n3 & 0xFFFFFFFFL);
        nArray[n4 + n5] = (int)l2;
        return (l2 >>= 32) == 0L ? 0 : Nat.decAt(n2, nArray, n4, n5 + 1);
    }

    public static int subWordFrom(int n2, int n3, int[] nArray) {
        long l2 = ((long)nArray[0] & 0xFFFFFFFFL) - ((long)n3 & 0xFFFFFFFFL);
        nArray[0] = (int)l2;
        return (l2 >>= 32) == 0L ? 0 : Nat.decAt(n2, nArray, 1);
    }

    public static int subWordFrom(int n2, int n3, int[] nArray, int n4) {
        long l2 = ((long)nArray[n4 + 0] & 0xFFFFFFFFL) - ((long)n3 & 0xFFFFFFFFL);
        nArray[n4 + 0] = (int)l2;
        return (l2 >>= 32) == 0L ? 0 : Nat.decAt(n2, nArray, n4, 1);
    }

    public static BigInteger toBigInteger(int n2, int[] nArray) {
        byte[] byArray = new byte[n2 << 2];
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3 = nArray[i2];
            if (n3 == 0) continue;
            Pack.intToBigEndian(n3, byArray, n2 - 1 - i2 << 2);
        }
        return new BigInteger(1, byArray);
    }

    public static void zero(int n2, int[] nArray) {
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray[i2] = 0;
        }
    }

    public static void zero(int n2, int[] nArray, int n3) {
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray[n3 + i2] = 0;
        }
    }

    public static void zero64(int n2, long[] lArray) {
        for (int i2 = 0; i2 < n2; ++i2) {
            lArray[i2] = 0L;
        }
    }
}

