/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.modes.kgcm;

import org.bouncycastle.math.raw.Interleave;

public class KGCMUtil_512 {
    public static final int SIZE = 8;

    public static void add(long[] lArray, long[] lArray2, long[] lArray3) {
        lArray3[0] = lArray[0] ^ lArray2[0];
        lArray3[1] = lArray[1] ^ lArray2[1];
        lArray3[2] = lArray[2] ^ lArray2[2];
        lArray3[3] = lArray[3] ^ lArray2[3];
        lArray3[4] = lArray[4] ^ lArray2[4];
        lArray3[5] = lArray[5] ^ lArray2[5];
        lArray3[6] = lArray[6] ^ lArray2[6];
        lArray3[7] = lArray[7] ^ lArray2[7];
    }

    public static void copy(long[] lArray, long[] lArray2) {
        lArray2[0] = lArray[0];
        lArray2[1] = lArray[1];
        lArray2[2] = lArray[2];
        lArray2[3] = lArray[3];
        lArray2[4] = lArray[4];
        lArray2[5] = lArray[5];
        lArray2[6] = lArray[6];
        lArray2[7] = lArray[7];
    }

    public static boolean equal(long[] lArray, long[] lArray2) {
        long l2 = 0L;
        l2 |= lArray[0] ^ lArray2[0];
        l2 |= lArray[1] ^ lArray2[1];
        l2 |= lArray[2] ^ lArray2[2];
        l2 |= lArray[3] ^ lArray2[3];
        l2 |= lArray[4] ^ lArray2[4];
        l2 |= lArray[5] ^ lArray2[5];
        l2 |= lArray[6] ^ lArray2[6];
        return (l2 |= lArray[7] ^ lArray2[7]) == 0L;
    }

    public static void multiply(long[] lArray, long[] lArray2, long[] lArray3) {
        long l2 = lArray2[0];
        long l3 = lArray2[1];
        long l4 = lArray2[2];
        long l5 = lArray2[3];
        long l6 = lArray2[4];
        long l7 = lArray2[5];
        long l8 = lArray2[6];
        long l9 = lArray2[7];
        long l10 = 0L;
        long l11 = 0L;
        long l12 = 0L;
        long l13 = 0L;
        long l14 = 0L;
        long l15 = 0L;
        long l16 = 0L;
        long l17 = 0L;
        long l18 = 0L;
        for (int i2 = 0; i2 < 8; i2 += 2) {
            long l19 = lArray[i2];
            long l20 = lArray[i2 + 1];
            for (int i3 = 0; i3 < 64; ++i3) {
                long l21 = -(l19 & 1L);
                l19 >>>= 1;
                l10 ^= l2 & l21;
                l11 ^= l3 & l21;
                l12 ^= l4 & l21;
                l13 ^= l5 & l21;
                l14 ^= l6 & l21;
                l15 ^= l7 & l21;
                l16 ^= l8 & l21;
                l17 ^= l9 & l21;
                long l22 = -(l20 & 1L);
                l20 >>>= 1;
                l11 ^= l2 & l22;
                l12 ^= l3 & l22;
                l13 ^= l4 & l22;
                l14 ^= l5 & l22;
                l15 ^= l6 & l22;
                l16 ^= l7 & l22;
                l17 ^= l8 & l22;
                l18 ^= l9 & l22;
                long l23 = l9 >> 63;
                l9 = l9 << 1 | l8 >>> 63;
                l8 = l8 << 1 | l7 >>> 63;
                l7 = l7 << 1 | l6 >>> 63;
                l6 = l6 << 1 | l5 >>> 63;
                l5 = l5 << 1 | l4 >>> 63;
                l4 = l4 << 1 | l3 >>> 63;
                l3 = l3 << 1 | l2 >>> 63;
                l2 = l2 << 1 ^ l23 & 0x125L;
            }
            long l24 = l9;
            l9 = l8;
            l8 = l7;
            l7 = l6;
            l6 = l5;
            l5 = l4;
            l4 = l3;
            l3 = l2 ^ l24 >>> 62 ^ l24 >>> 59 ^ l24 >>> 56;
            l2 = l24 ^ l24 << 2 ^ l24 << 5 ^ l24 << 8;
        }
        lArray3[0] = l10 ^= l18 ^ l18 << 2 ^ l18 << 5 ^ l18 << 8;
        lArray3[1] = l11 ^= l18 >>> 62 ^ l18 >>> 59 ^ l18 >>> 56;
        lArray3[2] = l12;
        lArray3[3] = l13;
        lArray3[4] = l14;
        lArray3[5] = l15;
        lArray3[6] = l16;
        lArray3[7] = l17;
    }

    public static void multiplyX(long[] lArray, long[] lArray2) {
        long l2 = lArray[0];
        long l3 = lArray[1];
        long l4 = lArray[2];
        long l5 = lArray[3];
        long l6 = lArray[4];
        long l7 = lArray[5];
        long l8 = lArray[6];
        long l9 = lArray[7];
        long l10 = l9 >> 63;
        lArray2[0] = l2 << 1 ^ l10 & 0x125L;
        lArray2[1] = l3 << 1 | l2 >>> 63;
        lArray2[2] = l4 << 1 | l3 >>> 63;
        lArray2[3] = l5 << 1 | l4 >>> 63;
        lArray2[4] = l6 << 1 | l5 >>> 63;
        lArray2[5] = l7 << 1 | l6 >>> 63;
        lArray2[6] = l8 << 1 | l7 >>> 63;
        lArray2[7] = l9 << 1 | l8 >>> 63;
    }

    public static void multiplyX8(long[] lArray, long[] lArray2) {
        long l2 = lArray[0];
        long l3 = lArray[1];
        long l4 = lArray[2];
        long l5 = lArray[3];
        long l6 = lArray[4];
        long l7 = lArray[5];
        long l8 = lArray[6];
        long l9 = lArray[7];
        long l10 = l9 >>> 56;
        lArray2[0] = l2 << 8 ^ l10 ^ l10 << 2 ^ l10 << 5 ^ l10 << 8;
        lArray2[1] = l3 << 8 | l2 >>> 56;
        lArray2[2] = l4 << 8 | l3 >>> 56;
        lArray2[3] = l5 << 8 | l4 >>> 56;
        lArray2[4] = l6 << 8 | l5 >>> 56;
        lArray2[5] = l7 << 8 | l6 >>> 56;
        lArray2[6] = l8 << 8 | l7 >>> 56;
        lArray2[7] = l9 << 8 | l8 >>> 56;
    }

    public static void one(long[] lArray) {
        lArray[0] = 1L;
        lArray[1] = 0L;
        lArray[2] = 0L;
        lArray[3] = 0L;
        lArray[4] = 0L;
        lArray[5] = 0L;
        lArray[6] = 0L;
        lArray[7] = 0L;
    }

    public static void square(long[] lArray, long[] lArray2) {
        int n2;
        long[] lArray3 = new long[16];
        for (n2 = 0; n2 < 8; ++n2) {
            Interleave.expand64To128(lArray[n2], lArray3, n2 << 1);
        }
        n2 = 16;
        while (--n2 >= 8) {
            long l2 = lArray3[n2];
            int n3 = n2 - 8;
            lArray3[n3] = lArray3[n3] ^ (l2 ^ l2 << 2 ^ l2 << 5 ^ l2 << 8);
            int n4 = n2 - 8 + 1;
            lArray3[n4] = lArray3[n4] ^ (l2 >>> 62 ^ l2 >>> 59 ^ l2 >>> 56);
        }
        KGCMUtil_512.copy(lArray3, lArray2);
    }

    public static void x(long[] lArray) {
        lArray[0] = 2L;
        lArray[1] = 0L;
        lArray[2] = 0L;
        lArray[3] = 0L;
        lArray[4] = 0L;
        lArray[5] = 0L;
        lArray[6] = 0L;
        lArray[7] = 0L;
    }

    public static void zero(long[] lArray) {
        lArray[0] = 0L;
        lArray[1] = 0L;
        lArray[2] = 0L;
        lArray[3] = 0L;
        lArray[4] = 0L;
        lArray[5] = 0L;
        lArray[6] = 0L;
        lArray[7] = 0L;
    }
}

