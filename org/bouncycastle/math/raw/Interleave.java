/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.raw;

import org.bouncycastle.math.raw.Bits;

public class Interleave {
    private static final long M32 = 0x55555555L;
    private static final long M64 = 0x5555555555555555L;
    private static final long M64R = -6148914691236517206L;

    public static int expand8to16(int n2) {
        n2 &= 0xFF;
        n2 = (n2 | n2 << 4) & 0xF0F;
        n2 = (n2 | n2 << 2) & 0x3333;
        n2 = (n2 | n2 << 1) & 0x5555;
        return n2;
    }

    public static int expand16to32(int n2) {
        n2 &= 0xFFFF;
        n2 = (n2 | n2 << 8) & 0xFF00FF;
        n2 = (n2 | n2 << 4) & 0xF0F0F0F;
        n2 = (n2 | n2 << 2) & 0x33333333;
        n2 = (n2 | n2 << 1) & 0x55555555;
        return n2;
    }

    public static long expand32to64(int n2) {
        n2 = Bits.bitPermuteStep(n2, 65280, 8);
        n2 = Bits.bitPermuteStep(n2, 0xF000F0, 4);
        n2 = Bits.bitPermuteStep(n2, 0xC0C0C0C, 2);
        n2 = Bits.bitPermuteStep(n2, 0x22222222, 1);
        return ((long)(n2 >>> 1) & 0x55555555L) << 32 | (long)n2 & 0x55555555L;
    }

    public static void expand64To128(long l2, long[] lArray, int n2) {
        l2 = Bits.bitPermuteStep(l2, 0xFFFF0000L, 16);
        l2 = Bits.bitPermuteStep(l2, 0xFF000000FF00L, 8);
        l2 = Bits.bitPermuteStep(l2, 0xF000F000F000F0L, 4);
        l2 = Bits.bitPermuteStep(l2, 0xC0C0C0C0C0C0C0CL, 2);
        l2 = Bits.bitPermuteStep(l2, 0x2222222222222222L, 1);
        lArray[n2] = l2 & 0x5555555555555555L;
        lArray[n2 + 1] = l2 >>> 1 & 0x5555555555555555L;
    }

    public static void expand64To128(long[] lArray, int n2, int n3, long[] lArray2, int n4) {
        for (int i2 = 0; i2 < n3; ++i2) {
            Interleave.expand64To128(lArray[n2 + i2], lArray2, n4);
            n4 += 2;
        }
    }

    public static void expand64To128Rev(long l2, long[] lArray, int n2) {
        l2 = Bits.bitPermuteStep(l2, 0xFFFF0000L, 16);
        l2 = Bits.bitPermuteStep(l2, 0xFF000000FF00L, 8);
        l2 = Bits.bitPermuteStep(l2, 0xF000F000F000F0L, 4);
        l2 = Bits.bitPermuteStep(l2, 0xC0C0C0C0C0C0C0CL, 2);
        l2 = Bits.bitPermuteStep(l2, 0x2222222222222222L, 1);
        lArray[n2] = l2 & 0xAAAAAAAAAAAAAAAAL;
        lArray[n2 + 1] = l2 << 1 & 0xAAAAAAAAAAAAAAAAL;
    }

    public static int shuffle(int n2) {
        n2 = Bits.bitPermuteStep(n2, 65280, 8);
        n2 = Bits.bitPermuteStep(n2, 0xF000F0, 4);
        n2 = Bits.bitPermuteStep(n2, 0xC0C0C0C, 2);
        n2 = Bits.bitPermuteStep(n2, 0x22222222, 1);
        return n2;
    }

    public static long shuffle(long l2) {
        l2 = Bits.bitPermuteStep(l2, 0xFFFF0000L, 16);
        l2 = Bits.bitPermuteStep(l2, 0xFF000000FF00L, 8);
        l2 = Bits.bitPermuteStep(l2, 0xF000F000F000F0L, 4);
        l2 = Bits.bitPermuteStep(l2, 0xC0C0C0C0C0C0C0CL, 2);
        l2 = Bits.bitPermuteStep(l2, 0x2222222222222222L, 1);
        return l2;
    }

    public static int shuffle2(int n2) {
        n2 = Bits.bitPermuteStep(n2, 0xAA00AA, 7);
        n2 = Bits.bitPermuteStep(n2, 52428, 14);
        n2 = Bits.bitPermuteStep(n2, 0xF000F0, 4);
        n2 = Bits.bitPermuteStep(n2, 65280, 8);
        return n2;
    }

    public static long shuffle2(long l2) {
        l2 = Bits.bitPermuteStep(l2, 0xFF00FF00L, 24);
        l2 = Bits.bitPermuteStep(l2, 0xCC00CC00CC00CCL, 6);
        l2 = Bits.bitPermuteStep(l2, 0xF0F00000F0F0L, 12);
        l2 = Bits.bitPermuteStep(l2, 0xA0A0A0A0A0A0A0AL, 3);
        return l2;
    }

    public static long shuffle3(long l2) {
        l2 = Bits.bitPermuteStep(l2, 0xAA00AA00AA00AAL, 7);
        l2 = Bits.bitPermuteStep(l2, 0xCCCC0000CCCCL, 14);
        l2 = Bits.bitPermuteStep(l2, 0xF0F0F0F0L, 28);
        return l2;
    }

    public static int unshuffle(int n2) {
        n2 = Bits.bitPermuteStep(n2, 0x22222222, 1);
        n2 = Bits.bitPermuteStep(n2, 0xC0C0C0C, 2);
        n2 = Bits.bitPermuteStep(n2, 0xF000F0, 4);
        n2 = Bits.bitPermuteStep(n2, 65280, 8);
        return n2;
    }

    public static long unshuffle(long l2) {
        l2 = Bits.bitPermuteStep(l2, 0x2222222222222222L, 1);
        l2 = Bits.bitPermuteStep(l2, 0xC0C0C0C0C0C0C0CL, 2);
        l2 = Bits.bitPermuteStep(l2, 0xF000F000F000F0L, 4);
        l2 = Bits.bitPermuteStep(l2, 0xFF000000FF00L, 8);
        l2 = Bits.bitPermuteStep(l2, 0xFFFF0000L, 16);
        return l2;
    }

    public static int unshuffle2(int n2) {
        n2 = Bits.bitPermuteStep(n2, 65280, 8);
        n2 = Bits.bitPermuteStep(n2, 0xF000F0, 4);
        n2 = Bits.bitPermuteStep(n2, 52428, 14);
        n2 = Bits.bitPermuteStep(n2, 0xAA00AA, 7);
        return n2;
    }

    public static long unshuffle2(long l2) {
        l2 = Bits.bitPermuteStep(l2, 0xA0A0A0A0A0A0A0AL, 3);
        l2 = Bits.bitPermuteStep(l2, 0xF0F00000F0F0L, 12);
        l2 = Bits.bitPermuteStep(l2, 0xCC00CC00CC00CCL, 6);
        l2 = Bits.bitPermuteStep(l2, 0xFF00FF00L, 24);
        return l2;
    }

    public static long unshuffle3(long l2) {
        return Interleave.shuffle3(l2);
    }
}

