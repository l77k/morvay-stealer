/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.raw;

import java.math.BigInteger;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.util.Pack;

public abstract class Nat128 {
    private static final long M = 0xFFFFFFFFL;

    public static int add(int[] nArray, int[] nArray2, int[] nArray3) {
        long l2 = 0L;
        nArray3[0] = (int)(l2 += ((long)nArray[0] & 0xFFFFFFFFL) + ((long)nArray2[0] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[1] = (int)(l2 += ((long)nArray[1] & 0xFFFFFFFFL) + ((long)nArray2[1] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[2] = (int)(l2 += ((long)nArray[2] & 0xFFFFFFFFL) + ((long)nArray2[2] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[3] = (int)(l2 += ((long)nArray[3] & 0xFFFFFFFFL) + ((long)nArray2[3] & 0xFFFFFFFFL));
        return (int)(l2 >>>= 32);
    }

    public static int addBothTo(int[] nArray, int[] nArray2, int[] nArray3) {
        long l2 = 0L;
        nArray3[0] = (int)(l2 += ((long)nArray[0] & 0xFFFFFFFFL) + ((long)nArray2[0] & 0xFFFFFFFFL) + ((long)nArray3[0] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[1] = (int)(l2 += ((long)nArray[1] & 0xFFFFFFFFL) + ((long)nArray2[1] & 0xFFFFFFFFL) + ((long)nArray3[1] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[2] = (int)(l2 += ((long)nArray[2] & 0xFFFFFFFFL) + ((long)nArray2[2] & 0xFFFFFFFFL) + ((long)nArray3[2] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[3] = (int)(l2 += ((long)nArray[3] & 0xFFFFFFFFL) + ((long)nArray2[3] & 0xFFFFFFFFL) + ((long)nArray3[3] & 0xFFFFFFFFL));
        return (int)(l2 >>>= 32);
    }

    public static int addTo(int[] nArray, int[] nArray2) {
        long l2 = 0L;
        nArray2[0] = (int)(l2 += ((long)nArray[0] & 0xFFFFFFFFL) + ((long)nArray2[0] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[1] = (int)(l2 += ((long)nArray[1] & 0xFFFFFFFFL) + ((long)nArray2[1] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[2] = (int)(l2 += ((long)nArray[2] & 0xFFFFFFFFL) + ((long)nArray2[2] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[3] = (int)(l2 += ((long)nArray[3] & 0xFFFFFFFFL) + ((long)nArray2[3] & 0xFFFFFFFFL));
        return (int)(l2 >>>= 32);
    }

    public static int addTo(int[] nArray, int n2, int[] nArray2, int n3, int n4) {
        long l2 = (long)n4 & 0xFFFFFFFFL;
        nArray2[n3 + 0] = (int)(l2 += ((long)nArray[n2 + 0] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 0] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[n3 + 1] = (int)(l2 += ((long)nArray[n2 + 1] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 1] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[n3 + 2] = (int)(l2 += ((long)nArray[n2 + 2] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 2] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[n3 + 3] = (int)(l2 += ((long)nArray[n2 + 3] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 3] & 0xFFFFFFFFL));
        return (int)(l2 >>>= 32);
    }

    public static int addToEachOther(int[] nArray, int n2, int[] nArray2, int n3) {
        long l2 = 0L;
        nArray[n2 + 0] = (int)(l2 += ((long)nArray[n2 + 0] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 0] & 0xFFFFFFFFL));
        nArray2[n3 + 0] = (int)l2;
        l2 >>>= 32;
        nArray[n2 + 1] = (int)(l2 += ((long)nArray[n2 + 1] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 1] & 0xFFFFFFFFL));
        nArray2[n3 + 1] = (int)l2;
        l2 >>>= 32;
        nArray[n2 + 2] = (int)(l2 += ((long)nArray[n2 + 2] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 2] & 0xFFFFFFFFL));
        nArray2[n3 + 2] = (int)l2;
        l2 >>>= 32;
        nArray[n2 + 3] = (int)(l2 += ((long)nArray[n2 + 3] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 3] & 0xFFFFFFFFL));
        nArray2[n3 + 3] = (int)l2;
        return (int)(l2 >>>= 32);
    }

    public static void copy(int[] nArray, int[] nArray2) {
        nArray2[0] = nArray[0];
        nArray2[1] = nArray[1];
        nArray2[2] = nArray[2];
        nArray2[3] = nArray[3];
    }

    public static void copy(int[] nArray, int n2, int[] nArray2, int n3) {
        nArray2[n3 + 0] = nArray[n2 + 0];
        nArray2[n3 + 1] = nArray[n2 + 1];
        nArray2[n3 + 2] = nArray[n2 + 2];
        nArray2[n3 + 3] = nArray[n2 + 3];
    }

    public static void copy64(long[] lArray, long[] lArray2) {
        lArray2[0] = lArray[0];
        lArray2[1] = lArray[1];
    }

    public static void copy64(long[] lArray, int n2, long[] lArray2, int n3) {
        lArray2[n3 + 0] = lArray[n2 + 0];
        lArray2[n3 + 1] = lArray[n2 + 1];
    }

    public static int[] create() {
        return new int[4];
    }

    public static long[] create64() {
        return new long[2];
    }

    public static int[] createExt() {
        return new int[8];
    }

    public static long[] createExt64() {
        return new long[4];
    }

    public static boolean diff(int[] nArray, int n2, int[] nArray2, int n3, int[] nArray3, int n4) {
        boolean bl = Nat128.gte(nArray, n2, nArray2, n3);
        if (bl) {
            Nat128.sub(nArray, n2, nArray2, n3, nArray3, n4);
        } else {
            Nat128.sub(nArray2, n3, nArray, n2, nArray3, n4);
        }
        return bl;
    }

    public static boolean eq(int[] nArray, int[] nArray2) {
        for (int i2 = 3; i2 >= 0; --i2) {
            if (nArray[i2] == nArray2[i2]) continue;
            return false;
        }
        return true;
    }

    public static boolean eq64(long[] lArray, long[] lArray2) {
        for (int i2 = 1; i2 >= 0; --i2) {
            if (lArray[i2] == lArray2[i2]) continue;
            return false;
        }
        return true;
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        if (bigInteger.signum() < 0 || bigInteger.bitLength() > 128) {
            throw new IllegalArgumentException();
        }
        int[] nArray = Nat128.create();
        for (int i2 = 0; i2 < 4; ++i2) {
            nArray[i2] = bigInteger.intValue();
            bigInteger = bigInteger.shiftRight(32);
        }
        return nArray;
    }

    public static long[] fromBigInteger64(BigInteger bigInteger) {
        if (bigInteger.signum() < 0 || bigInteger.bitLength() > 128) {
            throw new IllegalArgumentException();
        }
        long[] lArray = Nat128.create64();
        for (int i2 = 0; i2 < 2; ++i2) {
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
        if (n3 < 0 || n3 >= 4) {
            return 0;
        }
        int n4 = n2 & 0x1F;
        return nArray[n3] >>> n4 & 1;
    }

    public static boolean gte(int[] nArray, int[] nArray2) {
        for (int i2 = 3; i2 >= 0; --i2) {
            int n2 = nArray[i2] ^ Integer.MIN_VALUE;
            int n3 = nArray2[i2] ^ Integer.MIN_VALUE;
            if (n2 < n3) {
                return false;
            }
            if (n2 <= n3) continue;
            return true;
        }
        return true;
    }

    public static boolean gte(int[] nArray, int n2, int[] nArray2, int n3) {
        for (int i2 = 3; i2 >= 0; --i2) {
            int n4 = nArray[n2 + i2] ^ Integer.MIN_VALUE;
            int n5 = nArray2[n3 + i2] ^ Integer.MIN_VALUE;
            if (n4 < n5) {
                return false;
            }
            if (n4 <= n5) continue;
            return true;
        }
        return true;
    }

    public static boolean isOne(int[] nArray) {
        if (nArray[0] != 1) {
            return false;
        }
        for (int i2 = 1; i2 < 4; ++i2) {
            if (nArray[i2] == 0) continue;
            return false;
        }
        return true;
    }

    public static boolean isOne64(long[] lArray) {
        if (lArray[0] != 1L) {
            return false;
        }
        for (int i2 = 1; i2 < 2; ++i2) {
            if (lArray[i2] == 0L) continue;
            return false;
        }
        return true;
    }

    public static boolean isZero(int[] nArray) {
        for (int i2 = 0; i2 < 4; ++i2) {
            if (nArray[i2] == 0) continue;
            return false;
        }
        return true;
    }

    public static boolean isZero64(long[] lArray) {
        for (int i2 = 0; i2 < 2; ++i2) {
            if (lArray[i2] == 0L) continue;
            return false;
        }
        return true;
    }

    public static void mul(int[] nArray, int[] nArray2, int[] nArray3) {
        long l2 = (long)nArray2[0] & 0xFFFFFFFFL;
        long l3 = (long)nArray2[1] & 0xFFFFFFFFL;
        long l4 = (long)nArray2[2] & 0xFFFFFFFFL;
        long l5 = (long)nArray2[3] & 0xFFFFFFFFL;
        long l6 = 0L;
        long l7 = (long)nArray[0] & 0xFFFFFFFFL;
        nArray3[0] = (int)(l6 += l7 * l2);
        l6 >>>= 32;
        nArray3[1] = (int)(l6 += l7 * l3);
        l6 >>>= 32;
        nArray3[2] = (int)(l6 += l7 * l4);
        l6 >>>= 32;
        nArray3[3] = (int)(l6 += l7 * l5);
        nArray3[4] = (int)(l6 >>>= 32);
        for (int i2 = 1; i2 < 4; ++i2) {
            long l8 = 0L;
            long l9 = (long)nArray[i2] & 0xFFFFFFFFL;
            nArray3[i2 + 0] = (int)(l8 += l9 * l2 + ((long)nArray3[i2 + 0] & 0xFFFFFFFFL));
            l8 >>>= 32;
            nArray3[i2 + 1] = (int)(l8 += l9 * l3 + ((long)nArray3[i2 + 1] & 0xFFFFFFFFL));
            l8 >>>= 32;
            nArray3[i2 + 2] = (int)(l8 += l9 * l4 + ((long)nArray3[i2 + 2] & 0xFFFFFFFFL));
            l8 >>>= 32;
            nArray3[i2 + 3] = (int)(l8 += l9 * l5 + ((long)nArray3[i2 + 3] & 0xFFFFFFFFL));
            nArray3[i2 + 4] = (int)(l8 >>>= 32);
        }
    }

    public static void mul(int[] nArray, int n2, int[] nArray2, int n3, int[] nArray3, int n4) {
        long l2 = (long)nArray2[n3 + 0] & 0xFFFFFFFFL;
        long l3 = (long)nArray2[n3 + 1] & 0xFFFFFFFFL;
        long l4 = (long)nArray2[n3 + 2] & 0xFFFFFFFFL;
        long l5 = (long)nArray2[n3 + 3] & 0xFFFFFFFFL;
        long l6 = 0L;
        long l7 = (long)nArray[n2 + 0] & 0xFFFFFFFFL;
        nArray3[n4 + 0] = (int)(l6 += l7 * l2);
        l6 >>>= 32;
        nArray3[n4 + 1] = (int)(l6 += l7 * l3);
        l6 >>>= 32;
        nArray3[n4 + 2] = (int)(l6 += l7 * l4);
        l6 >>>= 32;
        nArray3[n4 + 3] = (int)(l6 += l7 * l5);
        nArray3[n4 + 4] = (int)(l6 >>>= 32);
        for (int i2 = 1; i2 < 4; ++i2) {
            long l8 = 0L;
            long l9 = (long)nArray[n2 + i2] & 0xFFFFFFFFL;
            nArray3[n4 + 0] = (int)(l8 += l9 * l2 + ((long)nArray3[++n4 + 0] & 0xFFFFFFFFL));
            l8 >>>= 32;
            nArray3[n4 + 1] = (int)(l8 += l9 * l3 + ((long)nArray3[n4 + 1] & 0xFFFFFFFFL));
            l8 >>>= 32;
            nArray3[n4 + 2] = (int)(l8 += l9 * l4 + ((long)nArray3[n4 + 2] & 0xFFFFFFFFL));
            l8 >>>= 32;
            nArray3[n4 + 3] = (int)(l8 += l9 * l5 + ((long)nArray3[n4 + 3] & 0xFFFFFFFFL));
            nArray3[n4 + 4] = (int)(l8 >>>= 32);
        }
    }

    public static int mulAddTo(int[] nArray, int[] nArray2, int[] nArray3) {
        long l2 = (long)nArray2[0] & 0xFFFFFFFFL;
        long l3 = (long)nArray2[1] & 0xFFFFFFFFL;
        long l4 = (long)nArray2[2] & 0xFFFFFFFFL;
        long l5 = (long)nArray2[3] & 0xFFFFFFFFL;
        long l6 = 0L;
        for (int i2 = 0; i2 < 4; ++i2) {
            long l7 = 0L;
            long l8 = (long)nArray[i2] & 0xFFFFFFFFL;
            nArray3[i2 + 0] = (int)(l7 += l8 * l2 + ((long)nArray3[i2 + 0] & 0xFFFFFFFFL));
            l7 >>>= 32;
            nArray3[i2 + 1] = (int)(l7 += l8 * l3 + ((long)nArray3[i2 + 1] & 0xFFFFFFFFL));
            l7 >>>= 32;
            nArray3[i2 + 2] = (int)(l7 += l8 * l4 + ((long)nArray3[i2 + 2] & 0xFFFFFFFFL));
            l7 >>>= 32;
            nArray3[i2 + 3] = (int)(l7 += l8 * l5 + ((long)nArray3[i2 + 3] & 0xFFFFFFFFL));
            nArray3[i2 + 4] = (int)(l6 += (l7 >>>= 32) + ((long)nArray3[i2 + 4] & 0xFFFFFFFFL));
            l6 >>>= 32;
        }
        return (int)l6;
    }

    public static int mulAddTo(int[] nArray, int n2, int[] nArray2, int n3, int[] nArray3, int n4) {
        long l2 = (long)nArray2[n3 + 0] & 0xFFFFFFFFL;
        long l3 = (long)nArray2[n3 + 1] & 0xFFFFFFFFL;
        long l4 = (long)nArray2[n3 + 2] & 0xFFFFFFFFL;
        long l5 = (long)nArray2[n3 + 3] & 0xFFFFFFFFL;
        long l6 = 0L;
        for (int i2 = 0; i2 < 4; ++i2) {
            long l7 = 0L;
            long l8 = (long)nArray[n2 + i2] & 0xFFFFFFFFL;
            nArray3[n4 + 0] = (int)(l7 += l8 * l2 + ((long)nArray3[n4 + 0] & 0xFFFFFFFFL));
            l7 >>>= 32;
            nArray3[n4 + 1] = (int)(l7 += l8 * l3 + ((long)nArray3[n4 + 1] & 0xFFFFFFFFL));
            l7 >>>= 32;
            nArray3[n4 + 2] = (int)(l7 += l8 * l4 + ((long)nArray3[n4 + 2] & 0xFFFFFFFFL));
            l7 >>>= 32;
            nArray3[n4 + 3] = (int)(l7 += l8 * l5 + ((long)nArray3[n4 + 3] & 0xFFFFFFFFL));
            nArray3[n4 + 4] = (int)(l6 += (l7 >>>= 32) + ((long)nArray3[n4 + 4] & 0xFFFFFFFFL));
            l6 >>>= 32;
            ++n4;
        }
        return (int)l6;
    }

    public static long mul33Add(int n2, int[] nArray, int n3, int[] nArray2, int n4, int[] nArray3, int n5) {
        long l2 = 0L;
        long l3 = (long)n2 & 0xFFFFFFFFL;
        long l4 = (long)nArray[n3 + 0] & 0xFFFFFFFFL;
        nArray3[n5 + 0] = (int)(l2 += l3 * l4 + ((long)nArray2[n4 + 0] & 0xFFFFFFFFL));
        l2 >>>= 32;
        long l5 = (long)nArray[n3 + 1] & 0xFFFFFFFFL;
        nArray3[n5 + 1] = (int)(l2 += l3 * l5 + l4 + ((long)nArray2[n4 + 1] & 0xFFFFFFFFL));
        l2 >>>= 32;
        long l6 = (long)nArray[n3 + 2] & 0xFFFFFFFFL;
        nArray3[n5 + 2] = (int)(l2 += l3 * l6 + l5 + ((long)nArray2[n4 + 2] & 0xFFFFFFFFL));
        l2 >>>= 32;
        long l7 = (long)nArray[n3 + 3] & 0xFFFFFFFFL;
        nArray3[n5 + 3] = (int)(l2 += l3 * l7 + l6 + ((long)nArray2[n4 + 3] & 0xFFFFFFFFL));
        l2 >>>= 32;
        return l2 += l7;
    }

    public static int mulWordAddExt(int n2, int[] nArray, int n3, int[] nArray2, int n4) {
        long l2 = 0L;
        long l3 = (long)n2 & 0xFFFFFFFFL;
        nArray2[n4 + 0] = (int)(l2 += l3 * ((long)nArray[n3 + 0] & 0xFFFFFFFFL) + ((long)nArray2[n4 + 0] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[n4 + 1] = (int)(l2 += l3 * ((long)nArray[n3 + 1] & 0xFFFFFFFFL) + ((long)nArray2[n4 + 1] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[n4 + 2] = (int)(l2 += l3 * ((long)nArray[n3 + 2] & 0xFFFFFFFFL) + ((long)nArray2[n4 + 2] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[n4 + 3] = (int)(l2 += l3 * ((long)nArray[n3 + 3] & 0xFFFFFFFFL) + ((long)nArray2[n4 + 3] & 0xFFFFFFFFL));
        return (int)(l2 >>>= 32);
    }

    public static int mul33DWordAdd(int n2, long l2, int[] nArray, int n3) {
        long l3 = 0L;
        long l4 = (long)n2 & 0xFFFFFFFFL;
        long l5 = l2 & 0xFFFFFFFFL;
        nArray[n3 + 0] = (int)(l3 += l4 * l5 + ((long)nArray[n3 + 0] & 0xFFFFFFFFL));
        l3 >>>= 32;
        long l6 = l2 >>> 32;
        nArray[n3 + 1] = (int)(l3 += l4 * l6 + l5 + ((long)nArray[n3 + 1] & 0xFFFFFFFFL));
        l3 >>>= 32;
        nArray[n3 + 2] = (int)(l3 += l6 + ((long)nArray[n3 + 2] & 0xFFFFFFFFL));
        l3 >>>= 32;
        nArray[n3 + 3] = (int)(l3 += (long)nArray[n3 + 3] & 0xFFFFFFFFL);
        return (int)(l3 >>>= 32);
    }

    public static int mul33WordAdd(int n2, int n3, int[] nArray, int n4) {
        long l2 = 0L;
        long l3 = (long)n2 & 0xFFFFFFFFL;
        long l4 = (long)n3 & 0xFFFFFFFFL;
        nArray[n4 + 0] = (int)(l2 += l4 * l3 + ((long)nArray[n4 + 0] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray[n4 + 1] = (int)(l2 += l4 + ((long)nArray[n4 + 1] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray[n4 + 2] = (int)(l2 += (long)nArray[n4 + 2] & 0xFFFFFFFFL);
        return (l2 >>>= 32) == 0L ? 0 : Nat.incAt(4, nArray, n4, 3);
    }

    public static int mulWordDwordAdd(int n2, long l2, int[] nArray, int n3) {
        long l3 = 0L;
        long l4 = (long)n2 & 0xFFFFFFFFL;
        nArray[n3 + 0] = (int)(l3 += l4 * (l2 & 0xFFFFFFFFL) + ((long)nArray[n3 + 0] & 0xFFFFFFFFL));
        l3 >>>= 32;
        nArray[n3 + 1] = (int)(l3 += l4 * (l2 >>> 32) + ((long)nArray[n3 + 1] & 0xFFFFFFFFL));
        l3 >>>= 32;
        nArray[n3 + 2] = (int)(l3 += (long)nArray[n3 + 2] & 0xFFFFFFFFL);
        return (l3 >>>= 32) == 0L ? 0 : Nat.incAt(4, nArray, n3, 3);
    }

    public static int mulWordsAdd(int n2, int n3, int[] nArray, int n4) {
        long l2 = 0L;
        long l3 = (long)n2 & 0xFFFFFFFFL;
        long l4 = (long)n3 & 0xFFFFFFFFL;
        nArray[n4 + 0] = (int)(l2 += l4 * l3 + ((long)nArray[n4 + 0] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray[n4 + 1] = (int)(l2 += (long)nArray[n4 + 1] & 0xFFFFFFFFL);
        return (l2 >>>= 32) == 0L ? 0 : Nat.incAt(4, nArray, n4, 2);
    }

    public static int mulWord(int n2, int[] nArray, int[] nArray2, int n3) {
        long l2 = 0L;
        long l3 = (long)n2 & 0xFFFFFFFFL;
        int n4 = 0;
        do {
            nArray2[n3 + n4] = (int)(l2 += l3 * ((long)nArray[n4] & 0xFFFFFFFFL));
            l2 >>>= 32;
        } while (++n4 < 4);
        return (int)l2;
    }

    public static void square(int[] nArray, int[] nArray2) {
        long l2;
        long l3;
        long l4 = (long)nArray[0] & 0xFFFFFFFFL;
        int n2 = 0;
        int n3 = 3;
        int n4 = 8;
        do {
            l3 = (long)nArray[n3--] & 0xFFFFFFFFL;
            l2 = l3 * l3;
            nArray2[--n4] = n2 << 31 | (int)(l2 >>> 33);
            nArray2[--n4] = (int)(l2 >>> 1);
            n2 = (int)l2;
        } while (n3 > 0);
        l3 = l4 * l4;
        long l5 = (long)(n2 << 31) & 0xFFFFFFFFL | l3 >>> 33;
        nArray2[0] = (int)l3;
        n2 = (int)(l3 >>> 32) & 1;
        long l6 = (long)nArray[1] & 0xFFFFFFFFL;
        l3 = (long)nArray2[2] & 0xFFFFFFFFL;
        int n5 = (int)(l5 += l6 * l4);
        nArray2[1] = n5 << 1 | n2;
        n2 = n5 >>> 31;
        l3 += l5 >>> 32;
        l2 = (long)nArray[2] & 0xFFFFFFFFL;
        long l7 = (long)nArray2[3] & 0xFFFFFFFFL;
        long l8 = (long)nArray2[4] & 0xFFFFFFFFL;
        n5 = (int)(l3 += l2 * l4);
        nArray2[2] = n5 << 1 | n2;
        n2 = n5 >>> 31;
        l7 &= 0xFFFFFFFFL;
        long l9 = (long)nArray[3] & 0xFFFFFFFFL;
        long l10 = ((long)nArray2[5] & 0xFFFFFFFFL) + ((l8 += (l7 += (l3 >>> 32) + l2 * l6) >>> 32) >>> 32);
        l8 &= 0xFFFFFFFFL;
        long l11 = ((long)nArray2[6] & 0xFFFFFFFFL) + (l10 >>> 32);
        l10 &= 0xFFFFFFFFL;
        n5 = (int)(l7 += l9 * l4);
        nArray2[3] = n5 << 1 | n2;
        n2 = n5 >>> 31;
        l11 += (l10 += ((l8 += (l7 >>> 32) + l9 * l6) >>> 32) + l9 * l2) >>> 32;
        n5 = (int)l8;
        nArray2[4] = n5 << 1 | n2;
        n2 = n5 >>> 31;
        n5 = (int)(l10 &= 0xFFFFFFFFL);
        nArray2[5] = n5 << 1 | n2;
        n2 = n5 >>> 31;
        n5 = (int)l11;
        nArray2[6] = n5 << 1 | n2;
        n2 = n5 >>> 31;
        n5 = nArray2[7] + (int)(l11 >>> 32);
        nArray2[7] = n5 << 1 | n2;
    }

    public static void square(int[] nArray, int n2, int[] nArray2, int n3) {
        long l2;
        long l3;
        long l4 = (long)nArray[n2 + 0] & 0xFFFFFFFFL;
        int n4 = 0;
        int n5 = 3;
        int n6 = 8;
        do {
            l3 = (long)nArray[n2 + n5--] & 0xFFFFFFFFL;
            l2 = l3 * l3;
            nArray2[n3 + --n6] = n4 << 31 | (int)(l2 >>> 33);
            nArray2[n3 + --n6] = (int)(l2 >>> 1);
            n4 = (int)l2;
        } while (n5 > 0);
        l3 = l4 * l4;
        long l5 = (long)(n4 << 31) & 0xFFFFFFFFL | l3 >>> 33;
        nArray2[n3 + 0] = (int)l3;
        n4 = (int)(l3 >>> 32) & 1;
        long l6 = (long)nArray[n2 + 1] & 0xFFFFFFFFL;
        l3 = (long)nArray2[n3 + 2] & 0xFFFFFFFFL;
        int n7 = (int)(l5 += l6 * l4);
        nArray2[n3 + 1] = n7 << 1 | n4;
        n4 = n7 >>> 31;
        l3 += l5 >>> 32;
        l2 = (long)nArray[n2 + 2] & 0xFFFFFFFFL;
        long l7 = (long)nArray2[n3 + 3] & 0xFFFFFFFFL;
        long l8 = (long)nArray2[n3 + 4] & 0xFFFFFFFFL;
        n7 = (int)(l3 += l2 * l4);
        nArray2[n3 + 2] = n7 << 1 | n4;
        n4 = n7 >>> 31;
        l7 &= 0xFFFFFFFFL;
        long l9 = (long)nArray[n2 + 3] & 0xFFFFFFFFL;
        long l10 = ((long)nArray2[n3 + 5] & 0xFFFFFFFFL) + ((l8 += (l7 += (l3 >>> 32) + l2 * l6) >>> 32) >>> 32);
        l8 &= 0xFFFFFFFFL;
        long l11 = ((long)nArray2[n3 + 6] & 0xFFFFFFFFL) + (l10 >>> 32);
        l10 &= 0xFFFFFFFFL;
        n7 = (int)(l7 += l9 * l4);
        nArray2[n3 + 3] = n7 << 1 | n4;
        n4 = n7 >>> 31;
        l11 += (l10 += ((l8 += (l7 >>> 32) + l9 * l6) >>> 32) + l9 * l2) >>> 32;
        n7 = (int)l8;
        nArray2[n3 + 4] = n7 << 1 | n4;
        n4 = n7 >>> 31;
        n7 = (int)l10;
        nArray2[n3 + 5] = n7 << 1 | n4;
        n4 = n7 >>> 31;
        n7 = (int)l11;
        nArray2[n3 + 6] = n7 << 1 | n4;
        n4 = n7 >>> 31;
        n7 = nArray2[n3 + 7] + (int)(l11 >>> 32);
        nArray2[n3 + 7] = n7 << 1 | n4;
    }

    public static int sub(int[] nArray, int[] nArray2, int[] nArray3) {
        long l2 = 0L;
        nArray3[0] = (int)(l2 += ((long)nArray[0] & 0xFFFFFFFFL) - ((long)nArray2[0] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[1] = (int)(l2 += ((long)nArray[1] & 0xFFFFFFFFL) - ((long)nArray2[1] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[2] = (int)(l2 += ((long)nArray[2] & 0xFFFFFFFFL) - ((long)nArray2[2] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[3] = (int)(l2 += ((long)nArray[3] & 0xFFFFFFFFL) - ((long)nArray2[3] & 0xFFFFFFFFL));
        return (int)(l2 >>= 32);
    }

    public static int sub(int[] nArray, int n2, int[] nArray2, int n3, int[] nArray3, int n4) {
        long l2 = 0L;
        nArray3[n4 + 0] = (int)(l2 += ((long)nArray[n2 + 0] & 0xFFFFFFFFL) - ((long)nArray2[n3 + 0] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[n4 + 1] = (int)(l2 += ((long)nArray[n2 + 1] & 0xFFFFFFFFL) - ((long)nArray2[n3 + 1] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[n4 + 2] = (int)(l2 += ((long)nArray[n2 + 2] & 0xFFFFFFFFL) - ((long)nArray2[n3 + 2] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[n4 + 3] = (int)(l2 += ((long)nArray[n2 + 3] & 0xFFFFFFFFL) - ((long)nArray2[n3 + 3] & 0xFFFFFFFFL));
        return (int)(l2 >>= 32);
    }

    public static int subBothFrom(int[] nArray, int[] nArray2, int[] nArray3) {
        long l2 = 0L;
        nArray3[0] = (int)(l2 += ((long)nArray3[0] & 0xFFFFFFFFL) - ((long)nArray[0] & 0xFFFFFFFFL) - ((long)nArray2[0] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[1] = (int)(l2 += ((long)nArray3[1] & 0xFFFFFFFFL) - ((long)nArray[1] & 0xFFFFFFFFL) - ((long)nArray2[1] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[2] = (int)(l2 += ((long)nArray3[2] & 0xFFFFFFFFL) - ((long)nArray[2] & 0xFFFFFFFFL) - ((long)nArray2[2] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[3] = (int)(l2 += ((long)nArray3[3] & 0xFFFFFFFFL) - ((long)nArray[3] & 0xFFFFFFFFL) - ((long)nArray2[3] & 0xFFFFFFFFL));
        return (int)(l2 >>= 32);
    }

    public static int subFrom(int[] nArray, int[] nArray2) {
        long l2 = 0L;
        nArray2[0] = (int)(l2 += ((long)nArray2[0] & 0xFFFFFFFFL) - ((long)nArray[0] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray2[1] = (int)(l2 += ((long)nArray2[1] & 0xFFFFFFFFL) - ((long)nArray[1] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray2[2] = (int)(l2 += ((long)nArray2[2] & 0xFFFFFFFFL) - ((long)nArray[2] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray2[3] = (int)(l2 += ((long)nArray2[3] & 0xFFFFFFFFL) - ((long)nArray[3] & 0xFFFFFFFFL));
        return (int)(l2 >>= 32);
    }

    public static int subFrom(int[] nArray, int n2, int[] nArray2, int n3) {
        long l2 = 0L;
        nArray2[n3 + 0] = (int)(l2 += ((long)nArray2[n3 + 0] & 0xFFFFFFFFL) - ((long)nArray[n2 + 0] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray2[n3 + 1] = (int)(l2 += ((long)nArray2[n3 + 1] & 0xFFFFFFFFL) - ((long)nArray[n2 + 1] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray2[n3 + 2] = (int)(l2 += ((long)nArray2[n3 + 2] & 0xFFFFFFFFL) - ((long)nArray[n2 + 2] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray2[n3 + 3] = (int)(l2 += ((long)nArray2[n3 + 3] & 0xFFFFFFFFL) - ((long)nArray[n2 + 3] & 0xFFFFFFFFL));
        return (int)(l2 >>= 32);
    }

    public static BigInteger toBigInteger(int[] nArray) {
        byte[] byArray = new byte[16];
        for (int i2 = 0; i2 < 4; ++i2) {
            int n2 = nArray[i2];
            if (n2 == 0) continue;
            Pack.intToBigEndian(n2, byArray, 3 - i2 << 2);
        }
        return new BigInteger(1, byArray);
    }

    public static BigInteger toBigInteger64(long[] lArray) {
        byte[] byArray = new byte[16];
        for (int i2 = 0; i2 < 2; ++i2) {
            long l2 = lArray[i2];
            if (l2 == 0L) continue;
            Pack.longToBigEndian(l2, byArray, 1 - i2 << 3);
        }
        return new BigInteger(1, byArray);
    }

    public static void zero(int[] nArray) {
        nArray[0] = 0;
        nArray[1] = 0;
        nArray[2] = 0;
        nArray[3] = 0;
    }
}

