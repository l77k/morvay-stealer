/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.modes.kgcm;

import org.bouncycastle.math.raw.Interleave;

public class KGCMUtil_128 {
    public static final int SIZE = 2;

    public static void add(long[] lArray, long[] lArray2, long[] lArray3) {
        lArray3[0] = lArray[0] ^ lArray2[0];
        lArray3[1] = lArray[1] ^ lArray2[1];
    }

    public static void copy(long[] lArray, long[] lArray2) {
        lArray2[0] = lArray[0];
        lArray2[1] = lArray[1];
    }

    public static boolean equal(long[] lArray, long[] lArray2) {
        long l2 = 0L;
        l2 |= lArray[0] ^ lArray2[0];
        return (l2 |= lArray[1] ^ lArray2[1]) == 0L;
    }

    public static void multiply(long[] lArray, long[] lArray2, long[] lArray3) {
        long l2 = lArray[0];
        long l3 = lArray[1];
        long l4 = lArray2[0];
        long l5 = lArray2[1];
        long l6 = 0L;
        long l7 = 0L;
        long l8 = 0L;
        for (int i2 = 0; i2 < 64; ++i2) {
            long l9 = -(l2 & 1L);
            l2 >>>= 1;
            l6 ^= l4 & l9;
            l7 ^= l5 & l9;
            long l10 = -(l3 & 1L);
            l3 >>>= 1;
            l7 ^= l4 & l10;
            l8 ^= l5 & l10;
            long l11 = l5 >> 63;
            l5 = l5 << 1 | l4 >>> 63;
            l4 = l4 << 1 ^ l11 & 0x87L;
        }
        lArray3[0] = l6 ^= l8 ^ l8 << 1 ^ l8 << 2 ^ l8 << 7;
        lArray3[1] = l7 ^= l8 >>> 63 ^ l8 >>> 62 ^ l8 >>> 57;
    }

    public static void multiplyX(long[] lArray, long[] lArray2) {
        long l2 = lArray[0];
        long l3 = lArray[1];
        long l4 = l3 >> 63;
        lArray2[0] = l2 << 1 ^ l4 & 0x87L;
        lArray2[1] = l3 << 1 | l2 >>> 63;
    }

    public static void multiplyX8(long[] lArray, long[] lArray2) {
        long l2 = lArray[0];
        long l3 = lArray[1];
        long l4 = l3 >>> 56;
        lArray2[0] = l2 << 8 ^ l4 ^ l4 << 1 ^ l4 << 2 ^ l4 << 7;
        lArray2[1] = l3 << 8 | l2 >>> 56;
    }

    public static void one(long[] lArray) {
        lArray[0] = 1L;
        lArray[1] = 0L;
    }

    public static void square(long[] lArray, long[] lArray2) {
        long[] lArray3 = new long[4];
        Interleave.expand64To128(lArray[0], lArray3, 0);
        Interleave.expand64To128(lArray[1], lArray3, 2);
        long l2 = lArray3[0];
        long l3 = lArray3[1];
        long l4 = lArray3[2];
        long l5 = lArray3[3];
        l3 ^= l5 ^ l5 << 1 ^ l5 << 2 ^ l5 << 7;
        lArray2[0] = l2 ^= (l4 ^= l5 >>> 63 ^ l5 >>> 62 ^ l5 >>> 57) ^ l4 << 1 ^ l4 << 2 ^ l4 << 7;
        lArray2[1] = l3 ^= l4 >>> 63 ^ l4 >>> 62 ^ l4 >>> 57;
    }

    public static void x(long[] lArray) {
        lArray[0] = 2L;
        lArray[1] = 0L;
    }

    public static void zero(long[] lArray) {
        lArray[0] = 0L;
        lArray[1] = 0L;
    }
}

