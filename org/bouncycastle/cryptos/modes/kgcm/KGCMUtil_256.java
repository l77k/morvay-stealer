/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.modes.kgcm;

import org.bouncycastle.math.raw.Interleave;

public class KGCMUtil_256 {
    public static final int SIZE = 4;

    public static void add(long[] lArray, long[] lArray2, long[] lArray3) {
        lArray3[0] = lArray[0] ^ lArray2[0];
        lArray3[1] = lArray[1] ^ lArray2[1];
        lArray3[2] = lArray[2] ^ lArray2[2];
        lArray3[3] = lArray[3] ^ lArray2[3];
    }

    public static void copy(long[] lArray, long[] lArray2) {
        lArray2[0] = lArray[0];
        lArray2[1] = lArray[1];
        lArray2[2] = lArray[2];
        lArray2[3] = lArray[3];
    }

    public static boolean equal(long[] lArray, long[] lArray2) {
        long l2 = 0L;
        l2 |= lArray[0] ^ lArray2[0];
        l2 |= lArray[1] ^ lArray2[1];
        l2 |= lArray[2] ^ lArray2[2];
        return (l2 |= lArray[3] ^ lArray2[3]) == 0L;
    }

    public static void multiply(long[] lArray, long[] lArray2, long[] lArray3) {
        long l2;
        long l3;
        long l4 = lArray[0];
        long l5 = lArray[1];
        long l6 = lArray[2];
        long l7 = lArray[3];
        long l8 = lArray2[0];
        long l9 = lArray2[1];
        long l10 = lArray2[2];
        long l11 = lArray2[3];
        long l12 = 0L;
        long l13 = 0L;
        long l14 = 0L;
        long l15 = 0L;
        long l16 = 0L;
        for (int i2 = 0; i2 < 64; ++i2) {
            long l17 = -(l4 & 1L);
            l4 >>>= 1;
            l12 ^= l8 & l17;
            l13 ^= l9 & l17;
            l14 ^= l10 & l17;
            l15 ^= l11 & l17;
            l3 = -(l5 & 1L);
            l5 >>>= 1;
            l13 ^= l8 & l3;
            l14 ^= l9 & l3;
            l15 ^= l10 & l3;
            l16 ^= l11 & l3;
            l2 = l11 >> 63;
            l11 = l11 << 1 | l10 >>> 63;
            l10 = l10 << 1 | l9 >>> 63;
            l9 = l9 << 1 | l8 >>> 63;
            l8 = l8 << 1 ^ l2 & 0x425L;
        }
        long l18 = l11;
        l11 = l10;
        l10 = l9;
        l9 = l8 ^ l18 >>> 62 ^ l18 >>> 59 ^ l18 >>> 54;
        l8 = l18 ^ l18 << 2 ^ l18 << 5 ^ l18 << 10;
        for (int i3 = 0; i3 < 64; ++i3) {
            l3 = -(l6 & 1L);
            l6 >>>= 1;
            l12 ^= l8 & l3;
            l13 ^= l9 & l3;
            l14 ^= l10 & l3;
            l15 ^= l11 & l3;
            l2 = -(l7 & 1L);
            l7 >>>= 1;
            l13 ^= l8 & l2;
            l14 ^= l9 & l2;
            l15 ^= l10 & l2;
            l16 ^= l11 & l2;
            long l19 = l11 >> 63;
            l11 = l11 << 1 | l10 >>> 63;
            l10 = l10 << 1 | l9 >>> 63;
            l9 = l9 << 1 | l8 >>> 63;
            l8 = l8 << 1 ^ l19 & 0x425L;
        }
        lArray3[0] = l12 ^= l16 ^ l16 << 2 ^ l16 << 5 ^ l16 << 10;
        lArray3[1] = l13 ^= l16 >>> 62 ^ l16 >>> 59 ^ l16 >>> 54;
        lArray3[2] = l14;
        lArray3[3] = l15;
    }

    public static void multiplyX(long[] lArray, long[] lArray2) {
        long l2 = lArray[0];
        long l3 = lArray[1];
        long l4 = lArray[2];
        long l5 = lArray[3];
        long l6 = l5 >> 63;
        lArray2[0] = l2 << 1 ^ l6 & 0x425L;
        lArray2[1] = l3 << 1 | l2 >>> 63;
        lArray2[2] = l4 << 1 | l3 >>> 63;
        lArray2[3] = l5 << 1 | l4 >>> 63;
    }

    public static void multiplyX8(long[] lArray, long[] lArray2) {
        long l2 = lArray[0];
        long l3 = lArray[1];
        long l4 = lArray[2];
        long l5 = lArray[3];
        long l6 = l5 >>> 56;
        lArray2[0] = l2 << 8 ^ l6 ^ l6 << 2 ^ l6 << 5 ^ l6 << 10;
        lArray2[1] = l3 << 8 | l2 >>> 56;
        lArray2[2] = l4 << 8 | l3 >>> 56;
        lArray2[3] = l5 << 8 | l4 >>> 56;
    }

    public static void one(long[] lArray) {
        lArray[0] = 1L;
        lArray[1] = 0L;
        lArray[2] = 0L;
        lArray[3] = 0L;
    }

    public static void square(long[] lArray, long[] lArray2) {
        int n2;
        long[] lArray3 = new long[8];
        for (n2 = 0; n2 < 4; ++n2) {
            Interleave.expand64To128(lArray[n2], lArray3, n2 << 1);
        }
        n2 = 8;
        while (--n2 >= 4) {
            long l2 = lArray3[n2];
            int n3 = n2 - 4;
            lArray3[n3] = lArray3[n3] ^ (l2 ^ l2 << 2 ^ l2 << 5 ^ l2 << 10);
            int n4 = n2 - 4 + 1;
            lArray3[n4] = lArray3[n4] ^ (l2 >>> 62 ^ l2 >>> 59 ^ l2 >>> 54);
        }
        KGCMUtil_256.copy(lArray3, lArray2);
    }

    public static void x(long[] lArray) {
        lArray[0] = 2L;
        lArray[1] = 0L;
        lArray[2] = 0L;
        lArray[3] = 0L;
    }

    public static void zero(long[] lArray) {
        lArray[0] = 0L;
        lArray[1] = 0L;
        lArray[2] = 0L;
        lArray[3] = 0L;
    }
}

