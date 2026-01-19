/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.gemss;

import org.bouncycastle.pqc.crypto.gemss.Pointer;

abstract class Mul_GF2x {
    Mul_GF2x() {
    }

    public abstract void mul_gf2x(Pointer var1, Pointer var2, Pointer var3);

    public abstract void sqr_gf2x(long[] var1, long[] var2, int var3);

    public abstract void mul_gf2x_xor(Pointer var1, Pointer var2, Pointer var3);

    private static long SQR32_NO_SIMD_GF2X(long l2) {
        l2 = (l2 ^ l2 << 16) & 0xFFFF0000FFFFL;
        l2 = (l2 ^ l2 << 8) & 0xFF00FF00FF00FFL;
        l2 = (l2 ^ l2 << 4) & 0xF0F0F0F0F0F0F0FL;
        l2 = (l2 ^ l2 << 2) & 0x3333333333333333L;
        return (l2 ^ l2 << 1) & 0x5555555555555555L;
    }

    private static long SQR64LOW_NO_SIMD_GF2X(long l2) {
        l2 = (l2 & 0xFFFFFFFFL ^ l2 << 16) & 0xFFFF0000FFFFL;
        l2 = (l2 ^ l2 << 8) & 0xFF00FF00FF00FFL;
        l2 = (l2 ^ l2 << 4) & 0xF0F0F0F0F0F0F0FL;
        l2 = (l2 ^ l2 << 2) & 0x3333333333333333L;
        return (l2 ^ l2 << 1) & 0x5555555555555555L;
    }

    private static void SQR64_NO_SIMD_GF2X(long[] lArray, int n2, long l2) {
        lArray[n2 + 1] = Mul_GF2x.SQR32_NO_SIMD_GF2X(l2 >>> 32);
        lArray[n2] = Mul_GF2x.SQR64LOW_NO_SIMD_GF2X(l2);
    }

    private static void SQR128_NO_SIMD_GF2X(long[] lArray, int n2, long[] lArray2, int n3) {
        Mul_GF2x.SQR64_NO_SIMD_GF2X(lArray, n2 + 2, lArray2[n3 + 1]);
        Mul_GF2x.SQR64_NO_SIMD_GF2X(lArray, n2, lArray2[n3]);
    }

    private static void SQR256_NO_SIMD_GF2X(long[] lArray, int n2, long[] lArray2, int n3) {
        Mul_GF2x.SQR128_NO_SIMD_GF2X(lArray, n2 + 4, lArray2, n3 + 2);
        Mul_GF2x.SQR128_NO_SIMD_GF2X(lArray, n2, lArray2, n3);
    }

    private static long MUL32_NO_SIMD_GF2X(long l2, long l3) {
        long l4 = -(l3 & 1L) & l2;
        l4 ^= (-(l3 >>> 1 & 1L) & l2) << 1;
        l4 ^= (-(l3 >>> 2 & 1L) & l2) << 2;
        l4 ^= (-(l3 >>> 3 & 1L) & l2) << 3;
        l4 ^= (-(l3 >>> 4 & 1L) & l2) << 4;
        l4 ^= (-(l3 >>> 5 & 1L) & l2) << 5;
        l4 ^= (-(l3 >>> 6 & 1L) & l2) << 6;
        l4 ^= (-(l3 >>> 7 & 1L) & l2) << 7;
        l4 ^= (-(l3 >>> 8 & 1L) & l2) << 8;
        l4 ^= (-(l3 >>> 9 & 1L) & l2) << 9;
        l4 ^= (-(l3 >>> 10 & 1L) & l2) << 10;
        l4 ^= (-(l3 >>> 11 & 1L) & l2) << 11;
        l4 ^= (-(l3 >>> 12 & 1L) & l2) << 12;
        l4 ^= (-(l3 >>> 13 & 1L) & l2) << 13;
        l4 ^= (-(l3 >>> 14 & 1L) & l2) << 14;
        l4 ^= (-(l3 >>> 15 & 1L) & l2) << 15;
        l4 ^= (-(l3 >>> 16 & 1L) & l2) << 16;
        l4 ^= (-(l3 >>> 17 & 1L) & l2) << 17;
        l4 ^= (-(l3 >>> 18 & 1L) & l2) << 18;
        l4 ^= (-(l3 >>> 19 & 1L) & l2) << 19;
        l4 ^= (-(l3 >>> 20 & 1L) & l2) << 20;
        l4 ^= (-(l3 >>> 21 & 1L) & l2) << 21;
        l4 ^= (-(l3 >>> 22 & 1L) & l2) << 22;
        l4 ^= (-(l3 >>> 23 & 1L) & l2) << 23;
        l4 ^= (-(l3 >>> 24 & 1L) & l2) << 24;
        l4 ^= (-(l3 >>> 25 & 1L) & l2) << 25;
        l4 ^= (-(l3 >>> 26 & 1L) & l2) << 26;
        l4 ^= (-(l3 >>> 27 & 1L) & l2) << 27;
        l4 ^= (-(l3 >>> 28 & 1L) & l2) << 28;
        l4 ^= (-(l3 >>> 29 & 1L) & l2) << 29;
        l4 ^= (-(l3 >>> 30 & 1L) & l2) << 30;
        return l4 ^= (-(l3 >>> 31 & 1L) & l2) << 31;
    }

    private static void MUL64_NO_SIMD_GF2X(long[] lArray, int n2, long l2, long l3) {
        long l4 = -(l3 & 1L) & l2;
        long l5 = -(l3 >>> 63) & l2;
        l4 ^= l5 << 63;
        long l6 = l5 >>> 1;
        l5 = -(l3 >>> 1 & 1L) & l2;
        l4 ^= l5 << 1;
        l6 ^= l5 >>> 63;
        l5 = -(l3 >>> 2 & 1L) & l2;
        l4 ^= l5 << 2;
        l6 ^= l5 >>> 62;
        l5 = -(l3 >>> 3 & 1L) & l2;
        l4 ^= l5 << 3;
        l6 ^= l5 >>> 61;
        l5 = -(l3 >>> 4 & 1L) & l2;
        l4 ^= l5 << 4;
        l6 ^= l5 >>> 60;
        l5 = -(l3 >>> 5 & 1L) & l2;
        l4 ^= l5 << 5;
        l6 ^= l5 >>> 59;
        l5 = -(l3 >>> 6 & 1L) & l2;
        l4 ^= l5 << 6;
        l6 ^= l5 >>> 58;
        l5 = -(l3 >>> 7 & 1L) & l2;
        l4 ^= l5 << 7;
        l6 ^= l5 >>> 57;
        l5 = -(l3 >>> 8 & 1L) & l2;
        l4 ^= l5 << 8;
        l6 ^= l5 >>> 56;
        l5 = -(l3 >>> 9 & 1L) & l2;
        l4 ^= l5 << 9;
        l6 ^= l5 >>> 55;
        l5 = -(l3 >>> 10 & 1L) & l2;
        l4 ^= l5 << 10;
        l6 ^= l5 >>> 54;
        l5 = -(l3 >>> 11 & 1L) & l2;
        l4 ^= l5 << 11;
        l6 ^= l5 >>> 53;
        l5 = -(l3 >>> 12 & 1L) & l2;
        l4 ^= l5 << 12;
        l6 ^= l5 >>> 52;
        l5 = -(l3 >>> 13 & 1L) & l2;
        l4 ^= l5 << 13;
        l6 ^= l5 >>> 51;
        l5 = -(l3 >>> 14 & 1L) & l2;
        l4 ^= l5 << 14;
        l6 ^= l5 >>> 50;
        l5 = -(l3 >>> 15 & 1L) & l2;
        l4 ^= l5 << 15;
        l6 ^= l5 >>> 49;
        l5 = -(l3 >>> 16 & 1L) & l2;
        l4 ^= l5 << 16;
        l6 ^= l5 >>> 48;
        l5 = -(l3 >>> 17 & 1L) & l2;
        l4 ^= l5 << 17;
        l6 ^= l5 >>> 47;
        l5 = -(l3 >>> 18 & 1L) & l2;
        l4 ^= l5 << 18;
        l6 ^= l5 >>> 46;
        l5 = -(l3 >>> 19 & 1L) & l2;
        l4 ^= l5 << 19;
        l6 ^= l5 >>> 45;
        l5 = -(l3 >>> 20 & 1L) & l2;
        l4 ^= l5 << 20;
        l6 ^= l5 >>> 44;
        l5 = -(l3 >>> 21 & 1L) & l2;
        l4 ^= l5 << 21;
        l6 ^= l5 >>> 43;
        l5 = -(l3 >>> 22 & 1L) & l2;
        l4 ^= l5 << 22;
        l6 ^= l5 >>> 42;
        l5 = -(l3 >>> 23 & 1L) & l2;
        l4 ^= l5 << 23;
        l6 ^= l5 >>> 41;
        l5 = -(l3 >>> 24 & 1L) & l2;
        l4 ^= l5 << 24;
        l6 ^= l5 >>> 40;
        l5 = -(l3 >>> 25 & 1L) & l2;
        l4 ^= l5 << 25;
        l6 ^= l5 >>> 39;
        l5 = -(l3 >>> 26 & 1L) & l2;
        l4 ^= l5 << 26;
        l6 ^= l5 >>> 38;
        l5 = -(l3 >>> 27 & 1L) & l2;
        l4 ^= l5 << 27;
        l6 ^= l5 >>> 37;
        l5 = -(l3 >>> 28 & 1L) & l2;
        l4 ^= l5 << 28;
        l6 ^= l5 >>> 36;
        l5 = -(l3 >>> 29 & 1L) & l2;
        l4 ^= l5 << 29;
        l6 ^= l5 >>> 35;
        l5 = -(l3 >>> 30 & 1L) & l2;
        l4 ^= l5 << 30;
        l6 ^= l5 >>> 34;
        l5 = -(l3 >>> 31 & 1L) & l2;
        l4 ^= l5 << 31;
        l6 ^= l5 >>> 33;
        l5 = -(l3 >>> 32 & 1L) & l2;
        l4 ^= l5 << 32;
        l6 ^= l5 >>> 32;
        l5 = -(l3 >>> 33 & 1L) & l2;
        l4 ^= l5 << 33;
        l6 ^= l5 >>> 31;
        l5 = -(l3 >>> 34 & 1L) & l2;
        l4 ^= l5 << 34;
        l6 ^= l5 >>> 30;
        l5 = -(l3 >>> 35 & 1L) & l2;
        l4 ^= l5 << 35;
        l6 ^= l5 >>> 29;
        l5 = -(l3 >>> 36 & 1L) & l2;
        l4 ^= l5 << 36;
        l6 ^= l5 >>> 28;
        l5 = -(l3 >>> 37 & 1L) & l2;
        l4 ^= l5 << 37;
        l6 ^= l5 >>> 27;
        l5 = -(l3 >>> 38 & 1L) & l2;
        l4 ^= l5 << 38;
        l6 ^= l5 >>> 26;
        l5 = -(l3 >>> 39 & 1L) & l2;
        l4 ^= l5 << 39;
        l6 ^= l5 >>> 25;
        l5 = -(l3 >>> 40 & 1L) & l2;
        l4 ^= l5 << 40;
        l6 ^= l5 >>> 24;
        l5 = -(l3 >>> 41 & 1L) & l2;
        l4 ^= l5 << 41;
        l6 ^= l5 >>> 23;
        l5 = -(l3 >>> 42 & 1L) & l2;
        l4 ^= l5 << 42;
        l6 ^= l5 >>> 22;
        l5 = -(l3 >>> 43 & 1L) & l2;
        l4 ^= l5 << 43;
        l6 ^= l5 >>> 21;
        l5 = -(l3 >>> 44 & 1L) & l2;
        l4 ^= l5 << 44;
        l6 ^= l5 >>> 20;
        l5 = -(l3 >>> 45 & 1L) & l2;
        l4 ^= l5 << 45;
        l6 ^= l5 >>> 19;
        l5 = -(l3 >>> 46 & 1L) & l2;
        l4 ^= l5 << 46;
        l6 ^= l5 >>> 18;
        l5 = -(l3 >>> 47 & 1L) & l2;
        l4 ^= l5 << 47;
        l6 ^= l5 >>> 17;
        l5 = -(l3 >>> 48 & 1L) & l2;
        l4 ^= l5 << 48;
        l6 ^= l5 >>> 16;
        l5 = -(l3 >>> 49 & 1L) & l2;
        l4 ^= l5 << 49;
        l6 ^= l5 >>> 15;
        l5 = -(l3 >>> 50 & 1L) & l2;
        l4 ^= l5 << 50;
        l6 ^= l5 >>> 14;
        l5 = -(l3 >>> 51 & 1L) & l2;
        l4 ^= l5 << 51;
        l6 ^= l5 >>> 13;
        l5 = -(l3 >>> 52 & 1L) & l2;
        l4 ^= l5 << 52;
        l6 ^= l5 >>> 12;
        l5 = -(l3 >>> 53 & 1L) & l2;
        l4 ^= l5 << 53;
        l6 ^= l5 >>> 11;
        l5 = -(l3 >>> 54 & 1L) & l2;
        l4 ^= l5 << 54;
        l6 ^= l5 >>> 10;
        l5 = -(l3 >>> 55 & 1L) & l2;
        l4 ^= l5 << 55;
        l6 ^= l5 >>> 9;
        l5 = -(l3 >>> 56 & 1L) & l2;
        l4 ^= l5 << 56;
        l6 ^= l5 >>> 8;
        l5 = -(l3 >>> 57 & 1L) & l2;
        l4 ^= l5 << 57;
        l6 ^= l5 >>> 7;
        l5 = -(l3 >>> 58 & 1L) & l2;
        l4 ^= l5 << 58;
        l6 ^= l5 >>> 6;
        l5 = -(l3 >>> 59 & 1L) & l2;
        l4 ^= l5 << 59;
        l6 ^= l5 >>> 5;
        l5 = -(l3 >>> 60 & 1L) & l2;
        l4 ^= l5 << 60;
        l6 ^= l5 >>> 4;
        l5 = -(l3 >>> 61 & 1L) & l2;
        l4 ^= l5 << 61;
        l6 ^= l5 >>> 3;
        l5 = -(l3 >>> 62 & 1L) & l2;
        lArray[n2] = l4 ^ l5 << 62;
        lArray[n2 + 1] = l6 ^ l5 >>> 2;
    }

    private static void MUL64_NO_SIMD_GF2X_XOR(long[] lArray, int n2, long l2, long l3) {
        long l4 = -(l3 & 1L) & l2;
        long l5 = -(l3 >>> 63) & l2;
        l4 ^= l5 << 63;
        long l6 = l5 >>> 1;
        l5 = -(l3 >>> 1 & 1L) & l2;
        l4 ^= l5 << 1;
        l6 ^= l5 >>> 63;
        l5 = -(l3 >>> 2 & 1L) & l2;
        l4 ^= l5 << 2;
        l6 ^= l5 >>> 62;
        l5 = -(l3 >>> 3 & 1L) & l2;
        l4 ^= l5 << 3;
        l6 ^= l5 >>> 61;
        l5 = -(l3 >>> 4 & 1L) & l2;
        l4 ^= l5 << 4;
        l6 ^= l5 >>> 60;
        l5 = -(l3 >>> 5 & 1L) & l2;
        l4 ^= l5 << 5;
        l6 ^= l5 >>> 59;
        l5 = -(l3 >>> 6 & 1L) & l2;
        l4 ^= l5 << 6;
        l6 ^= l5 >>> 58;
        l5 = -(l3 >>> 7 & 1L) & l2;
        l4 ^= l5 << 7;
        l6 ^= l5 >>> 57;
        l5 = -(l3 >>> 8 & 1L) & l2;
        l4 ^= l5 << 8;
        l6 ^= l5 >>> 56;
        l5 = -(l3 >>> 9 & 1L) & l2;
        l4 ^= l5 << 9;
        l6 ^= l5 >>> 55;
        l5 = -(l3 >>> 10 & 1L) & l2;
        l4 ^= l5 << 10;
        l6 ^= l5 >>> 54;
        l5 = -(l3 >>> 11 & 1L) & l2;
        l4 ^= l5 << 11;
        l6 ^= l5 >>> 53;
        l5 = -(l3 >>> 12 & 1L) & l2;
        l4 ^= l5 << 12;
        l6 ^= l5 >>> 52;
        l5 = -(l3 >>> 13 & 1L) & l2;
        l4 ^= l5 << 13;
        l6 ^= l5 >>> 51;
        l5 = -(l3 >>> 14 & 1L) & l2;
        l4 ^= l5 << 14;
        l6 ^= l5 >>> 50;
        l5 = -(l3 >>> 15 & 1L) & l2;
        l4 ^= l5 << 15;
        l6 ^= l5 >>> 49;
        l5 = -(l3 >>> 16 & 1L) & l2;
        l4 ^= l5 << 16;
        l6 ^= l5 >>> 48;
        l5 = -(l3 >>> 17 & 1L) & l2;
        l4 ^= l5 << 17;
        l6 ^= l5 >>> 47;
        l5 = -(l3 >>> 18 & 1L) & l2;
        l4 ^= l5 << 18;
        l6 ^= l5 >>> 46;
        l5 = -(l3 >>> 19 & 1L) & l2;
        l4 ^= l5 << 19;
        l6 ^= l5 >>> 45;
        l5 = -(l3 >>> 20 & 1L) & l2;
        l4 ^= l5 << 20;
        l6 ^= l5 >>> 44;
        l5 = -(l3 >>> 21 & 1L) & l2;
        l4 ^= l5 << 21;
        l6 ^= l5 >>> 43;
        l5 = -(l3 >>> 22 & 1L) & l2;
        l4 ^= l5 << 22;
        l6 ^= l5 >>> 42;
        l5 = -(l3 >>> 23 & 1L) & l2;
        l4 ^= l5 << 23;
        l6 ^= l5 >>> 41;
        l5 = -(l3 >>> 24 & 1L) & l2;
        l4 ^= l5 << 24;
        l6 ^= l5 >>> 40;
        l5 = -(l3 >>> 25 & 1L) & l2;
        l4 ^= l5 << 25;
        l6 ^= l5 >>> 39;
        l5 = -(l3 >>> 26 & 1L) & l2;
        l4 ^= l5 << 26;
        l6 ^= l5 >>> 38;
        l5 = -(l3 >>> 27 & 1L) & l2;
        l4 ^= l5 << 27;
        l6 ^= l5 >>> 37;
        l5 = -(l3 >>> 28 & 1L) & l2;
        l4 ^= l5 << 28;
        l6 ^= l5 >>> 36;
        l5 = -(l3 >>> 29 & 1L) & l2;
        l4 ^= l5 << 29;
        l6 ^= l5 >>> 35;
        l5 = -(l3 >>> 30 & 1L) & l2;
        l4 ^= l5 << 30;
        l6 ^= l5 >>> 34;
        l5 = -(l3 >>> 31 & 1L) & l2;
        l4 ^= l5 << 31;
        l6 ^= l5 >>> 33;
        l5 = -(l3 >>> 32 & 1L) & l2;
        l4 ^= l5 << 32;
        l6 ^= l5 >>> 32;
        l5 = -(l3 >>> 33 & 1L) & l2;
        l4 ^= l5 << 33;
        l6 ^= l5 >>> 31;
        l5 = -(l3 >>> 34 & 1L) & l2;
        l4 ^= l5 << 34;
        l6 ^= l5 >>> 30;
        l5 = -(l3 >>> 35 & 1L) & l2;
        l4 ^= l5 << 35;
        l6 ^= l5 >>> 29;
        l5 = -(l3 >>> 36 & 1L) & l2;
        l4 ^= l5 << 36;
        l6 ^= l5 >>> 28;
        l5 = -(l3 >>> 37 & 1L) & l2;
        l4 ^= l5 << 37;
        l6 ^= l5 >>> 27;
        l5 = -(l3 >>> 38 & 1L) & l2;
        l4 ^= l5 << 38;
        l6 ^= l5 >>> 26;
        l5 = -(l3 >>> 39 & 1L) & l2;
        l4 ^= l5 << 39;
        l6 ^= l5 >>> 25;
        l5 = -(l3 >>> 40 & 1L) & l2;
        l4 ^= l5 << 40;
        l6 ^= l5 >>> 24;
        l5 = -(l3 >>> 41 & 1L) & l2;
        l4 ^= l5 << 41;
        l6 ^= l5 >>> 23;
        l5 = -(l3 >>> 42 & 1L) & l2;
        l4 ^= l5 << 42;
        l6 ^= l5 >>> 22;
        l5 = -(l3 >>> 43 & 1L) & l2;
        l4 ^= l5 << 43;
        l6 ^= l5 >>> 21;
        l5 = -(l3 >>> 44 & 1L) & l2;
        l4 ^= l5 << 44;
        l6 ^= l5 >>> 20;
        l5 = -(l3 >>> 45 & 1L) & l2;
        l4 ^= l5 << 45;
        l6 ^= l5 >>> 19;
        l5 = -(l3 >>> 46 & 1L) & l2;
        l4 ^= l5 << 46;
        l6 ^= l5 >>> 18;
        l5 = -(l3 >>> 47 & 1L) & l2;
        l4 ^= l5 << 47;
        l6 ^= l5 >>> 17;
        l5 = -(l3 >>> 48 & 1L) & l2;
        l4 ^= l5 << 48;
        l6 ^= l5 >>> 16;
        l5 = -(l3 >>> 49 & 1L) & l2;
        l4 ^= l5 << 49;
        l6 ^= l5 >>> 15;
        l5 = -(l3 >>> 50 & 1L) & l2;
        l4 ^= l5 << 50;
        l6 ^= l5 >>> 14;
        l5 = -(l3 >>> 51 & 1L) & l2;
        l4 ^= l5 << 51;
        l6 ^= l5 >>> 13;
        l5 = -(l3 >>> 52 & 1L) & l2;
        l4 ^= l5 << 52;
        l6 ^= l5 >>> 12;
        l5 = -(l3 >>> 53 & 1L) & l2;
        l4 ^= l5 << 53;
        l6 ^= l5 >>> 11;
        l5 = -(l3 >>> 54 & 1L) & l2;
        l4 ^= l5 << 54;
        l6 ^= l5 >>> 10;
        l5 = -(l3 >>> 55 & 1L) & l2;
        l4 ^= l5 << 55;
        l6 ^= l5 >>> 9;
        l5 = -(l3 >>> 56 & 1L) & l2;
        l4 ^= l5 << 56;
        l6 ^= l5 >>> 8;
        l5 = -(l3 >>> 57 & 1L) & l2;
        l4 ^= l5 << 57;
        l6 ^= l5 >>> 7;
        l5 = -(l3 >>> 58 & 1L) & l2;
        l4 ^= l5 << 58;
        l6 ^= l5 >>> 6;
        l5 = -(l3 >>> 59 & 1L) & l2;
        l4 ^= l5 << 59;
        l6 ^= l5 >>> 5;
        l5 = -(l3 >>> 60 & 1L) & l2;
        l4 ^= l5 << 60;
        l6 ^= l5 >>> 4;
        l5 = -(l3 >>> 61 & 1L) & l2;
        l4 ^= l5 << 61;
        l6 ^= l5 >>> 3;
        l5 = -(l3 >>> 62 & 1L) & l2;
        int n3 = n2;
        lArray[n3] = lArray[n3] ^ (l4 ^ l5 << 62);
        int n4 = n2 + 1;
        lArray[n4] = lArray[n4] ^ (l6 ^ l5 >>> 2);
    }

    private static void mul128_no_simd_gf2x(long[] lArray, int n2, long[] lArray2, int n3, long[] lArray3, int n4) {
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray, n2, lArray2[n3], lArray3[n4]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray, n2 + 2, lArray2[n3 + 1], lArray3[n4 + 1]);
        int n5 = n2 + 2;
        lArray[n5] = lArray[n5] ^ lArray[n2 + 1];
        lArray[n2 + 1] = lArray[n2] ^ lArray[n2 + 2];
        int n6 = n2 + 2;
        lArray[n6] = lArray[n6] ^ lArray[n2 + 3];
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, n2 + 1, lArray2[n3] ^ lArray2[n3 + 1], lArray3[n4] ^ lArray3[n4 + 1]);
    }

    private static void mul128_no_simd_gf2x(long[] lArray, int n2, long l2, long l3, long l4, long l5) {
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray, n2, l2, l4);
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray, n2 + 2, l3, l5);
        int n3 = n2 + 2;
        lArray[n3] = lArray[n3] ^ lArray[n2 + 1];
        lArray[n2 + 1] = lArray[n2] ^ lArray[n2 + 2];
        int n4 = n2 + 2;
        lArray[n4] = lArray[n4] ^ lArray[n2 + 3];
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, n2 + 1, l2 ^ l3, l4 ^ l5);
    }

    private static void mul128_no_simd_gf2x_xor(long[] lArray, int n2, long l2, long l3, long l4, long l5, long[] lArray2) {
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray2, 0, l2, l4);
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray2, 2, l3, l5);
        int n3 = n2;
        lArray[n3] = lArray[n3] ^ lArray2[0];
        lArray2[2] = lArray2[2] ^ lArray2[1];
        int n4 = n2 + 1;
        lArray[n4] = lArray[n4] ^ (lArray2[0] ^ lArray2[2]);
        int n5 = n2 + 2;
        lArray[n5] = lArray[n5] ^ (lArray2[2] ^ lArray2[3]);
        int n6 = n2 + 3;
        lArray[n6] = lArray[n6] ^ lArray2[3];
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, n2 + 1, l2 ^ l3, l4 ^ l5);
    }

    public static void mul192_no_simd_gf2x(long[] lArray, int n2, long[] lArray2, int n3, long[] lArray3, int n4) {
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray, n2, lArray2[n3], lArray3[n4]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray, n2 + 4, lArray2[n3 + 2], lArray3[n4 + 2]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray, n2 + 2, lArray2[n3 + 1], lArray3[n4 + 1]);
        int n5 = n2 + 1;
        lArray[n5] = lArray[n5] ^ lArray[n2 + 2];
        int n6 = n2 + 3;
        lArray[n6] = lArray[n6] ^ lArray[n2 + 4];
        lArray[n2 + 4] = lArray[n2 + 3] ^ lArray[n2 + 5];
        lArray[n2 + 2] = lArray[n2 + 3] ^ lArray[n2 + 1] ^ lArray[n2];
        lArray[n2 + 3] = lArray[n2 + 1] ^ lArray[n2 + 4];
        int n7 = n2 + 1;
        lArray[n7] = lArray[n7] ^ lArray[n2];
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, n2 + 1, lArray2[n3] ^ lArray2[n3 + 1], lArray3[n4] ^ lArray3[n4 + 1]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, n2 + 3, lArray2[n3 + 1] ^ lArray2[n3 + 2], lArray3[n4 + 1] ^ lArray3[n4 + 2]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, n2 + 2, lArray2[n3] ^ lArray2[n3 + 2], lArray3[n4] ^ lArray3[n4 + 2]);
    }

    public static void mul192_no_simd_gf2x_xor(long[] lArray, int n2, long[] lArray2, int n3, long[] lArray3, int n4, long[] lArray4) {
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray4, 0, lArray2[n3], lArray3[n4]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray4, 4, lArray2[n3 + 2], lArray3[n4 + 2]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray4, 2, lArray2[n3 + 1], lArray3[n4 + 1]);
        int n5 = n2;
        lArray[n5] = lArray[n5] ^ lArray4[0];
        lArray4[1] = lArray4[1] ^ lArray4[2];
        lArray4[3] = lArray4[3] ^ lArray4[4];
        lArray4[4] = lArray4[3] ^ lArray4[5];
        lArray4[0] = lArray4[0] ^ lArray4[1];
        int n6 = n2 + 1;
        lArray[n6] = lArray[n6] ^ lArray4[0];
        int n7 = n2 + 2;
        lArray[n7] = lArray[n7] ^ (lArray4[3] ^ lArray4[0]);
        int n8 = n2 + 3;
        lArray[n8] = lArray[n8] ^ (lArray4[1] ^ lArray4[4]);
        int n9 = n2 + 4;
        lArray[n9] = lArray[n9] ^ lArray4[4];
        int n10 = n2 + 5;
        lArray[n10] = lArray[n10] ^ lArray4[5];
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, n2 + 1, lArray2[n3] ^ lArray2[n3 + 1], lArray3[n4] ^ lArray3[n4 + 1]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, n2 + 3, lArray2[n3 + 1] ^ lArray2[n3 + 2], lArray3[n4 + 1] ^ lArray3[n4 + 2]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, n2 + 2, lArray2[n3] ^ lArray2[n3 + 2], lArray3[n4] ^ lArray3[n4 + 2]);
    }

    private static void mul288_no_simd_gf2x(long[] lArray, int n2, long[] lArray2, int n3, long[] lArray3, int n4, long[] lArray4) {
        Mul_GF2x.mul128_no_simd_gf2x(lArray, n2, lArray2[n3], lArray2[n3 + 1], lArray3[n4], lArray3[n4 + 1]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray, n2 + 4, lArray2[n3 + 2], lArray3[n4 + 2]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray, n2 + 7, lArray2[n3 + 3], lArray3[n4 + 3]);
        int n5 = n2 + 7;
        lArray[n5] = lArray[n5] ^ lArray[n2 + 5];
        int n6 = n2 + 8;
        lArray[n6] = lArray[n6] ^ Mul_GF2x.MUL32_NO_SIMD_GF2X(lArray2[n3 + 4], lArray3[n4 + 4]);
        lArray[n2 + 5] = lArray[n2 + 7] ^ lArray[n2 + 4];
        int n7 = n2 + 7;
        lArray[n7] = lArray[n7] ^ lArray[n2 + 8];
        lArray[n2 + 6] = lArray[n2 + 7] ^ lArray[n2 + 4];
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, n2 + 5, lArray2[n3 + 2] ^ lArray2[n3 + 3], lArray3[n4 + 2] ^ lArray3[n4 + 3]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, n2 + 7, lArray2[n3 + 3] ^ lArray2[n3 + 4], lArray3[n4 + 3] ^ lArray3[n4 + 4]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, n2 + 6, lArray2[n3 + 2] ^ lArray2[n3 + 4], lArray3[n4 + 2] ^ lArray3[n4 + 4]);
        int n8 = n2 + 4;
        lArray[n8] = lArray[n8] ^ lArray[n2 + 2];
        int n9 = n2 + 5;
        lArray[n9] = lArray[n9] ^ lArray[n2 + 3];
        long l2 = lArray2[n3] ^ lArray2[n3 + 2];
        long l3 = lArray2[n3 + 1] ^ lArray2[n3 + 3];
        long l4 = lArray3[n4] ^ lArray3[n4 + 2];
        long l5 = lArray3[n4 + 1] ^ lArray3[n4 + 3];
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray4, 0, l2, l4);
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray4, 2, l3, l5);
        lArray4[2] = lArray4[2] ^ lArray4[1];
        lArray4[3] = lArray4[3] ^ Mul_GF2x.MUL32_NO_SIMD_GF2X(lArray2[n3 + 4], lArray3[n4 + 4]);
        lArray[n2 + 2] = lArray[n2 + 4] ^ lArray[n2] ^ lArray4[0];
        lArray[n2 + 3] = lArray[n2 + 5] ^ lArray[n2 + 1] ^ lArray4[2] ^ lArray4[0];
        lArray4[2] = lArray4[2] ^ lArray4[3];
        int n10 = n2 + 4;
        lArray[n10] = lArray[n10] ^ (lArray[n2 + 6] ^ lArray4[2] ^ lArray4[0]);
        int n11 = n2 + 5;
        lArray[n11] = lArray[n11] ^ (lArray[n2 + 7] ^ lArray4[2]);
        int n12 = n2 + 6;
        lArray[n12] = lArray[n12] ^ (lArray[n2 + 8] ^ lArray4[3]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, n2 + 3, l2 ^ l3, l4 ^ l5);
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, n2 + 5, l3 ^ lArray2[n3 + 4], l5 ^ lArray3[n4 + 4]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, n2 + 4, l2 ^ lArray2[n3 + 4], l4 ^ lArray3[n4 + 4]);
    }

    private static void mul288_no_simd_gf2x_xor(long[] lArray, int n2, long[] lArray2, int n3, long[] lArray3, int n4, long[] lArray4) {
        Mul_GF2x.mul128_no_simd_gf2x(lArray4, 0, lArray2[n3], lArray2[n3 + 1], lArray3[n4], lArray3[n4 + 1]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray4, 4, lArray2[n3 + 2], lArray3[n4 + 2]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray4, 7, lArray2[n3 + 3], lArray3[n4 + 3]);
        lArray4[7] = lArray4[7] ^ lArray4[5];
        lArray4[8] = lArray4[8] ^ Mul_GF2x.MUL32_NO_SIMD_GF2X(lArray2[n3 + 4], lArray3[n4 + 4]);
        lArray4[5] = lArray4[7] ^ lArray4[4];
        lArray4[7] = lArray4[7] ^ lArray4[8];
        lArray4[6] = lArray4[7] ^ lArray4[4];
        lArray4[4] = lArray4[4] ^ lArray4[2];
        lArray4[5] = lArray4[5] ^ lArray4[3];
        int n5 = n2;
        lArray[n5] = lArray[n5] ^ lArray4[0];
        int n6 = n2 + 1;
        lArray[n6] = lArray[n6] ^ lArray4[1];
        int n7 = n2 + 2;
        lArray[n7] = lArray[n7] ^ (lArray4[4] ^ lArray4[0]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray4, 5, lArray2[n3 + 2] ^ lArray2[n3 + 3], lArray3[n4 + 2] ^ lArray3[n4 + 3]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray4, 7, lArray2[n3 + 3] ^ lArray2[n3 + 4], lArray3[n4 + 3] ^ lArray3[n4 + 4]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray4, 6, lArray2[n3 + 2] ^ lArray2[n3 + 4], lArray3[n4 + 2] ^ lArray3[n4 + 4]);
        int n8 = n2 + 3;
        lArray[n8] = lArray[n8] ^ (lArray4[5] ^ lArray4[1]);
        int n9 = n2 + 4;
        lArray[n9] = lArray[n9] ^ (lArray4[4] ^ lArray4[6]);
        int n10 = n2 + 5;
        lArray[n10] = lArray[n10] ^ (lArray4[5] ^ lArray4[7]);
        int n11 = n2 + 6;
        lArray[n11] = lArray[n11] ^ (lArray4[6] ^ lArray4[8]);
        int n12 = n2 + 7;
        lArray[n12] = lArray[n12] ^ lArray4[7];
        int n13 = n2 + 8;
        lArray[n13] = lArray[n13] ^ lArray4[8];
        long l2 = lArray2[n3] ^ lArray2[n3 + 2];
        long l3 = lArray2[n3 + 1] ^ lArray2[n3 + 3];
        long l4 = lArray3[n4] ^ lArray3[n4 + 2];
        long l5 = lArray3[n4 + 1] ^ lArray3[n4 + 3];
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray4, 0, l2, l4);
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray4, 2, l3, l5);
        lArray4[2] = lArray4[2] ^ lArray4[1];
        lArray4[3] = lArray4[3] ^ Mul_GF2x.MUL32_NO_SIMD_GF2X(lArray2[n3 + 4], lArray3[n4 + 4]);
        int n14 = n2 + 2;
        lArray[n14] = lArray[n14] ^ lArray4[0];
        int n15 = n2 + 3;
        lArray[n15] = lArray[n15] ^ (lArray4[2] ^ lArray4[0]);
        lArray4[2] = lArray4[2] ^ lArray4[3];
        int n16 = n2 + 4;
        lArray[n16] = lArray[n16] ^ (lArray4[2] ^ lArray4[0]);
        int n17 = n2 + 5;
        lArray[n17] = lArray[n17] ^ lArray4[2];
        int n18 = n2 + 6;
        lArray[n18] = lArray[n18] ^ lArray4[3];
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, n2 + 3, l2 ^ l3, l4 ^ l5);
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, n2 + 5, l3 ^ lArray2[n3 + 4], l5 ^ lArray3[n4 + 4]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, n2 + 4, l2 ^ lArray2[n3 + 4], l4 ^ lArray3[n4 + 4]);
    }

    private static void mul384_no_simd_gf2x(long[] lArray, long[] lArray2, int n2, long[] lArray3, int n3, long[] lArray4) {
        Mul_GF2x.mul192_no_simd_gf2x(lArray, 0, lArray2, n2, lArray3, n3);
        Mul_GF2x.mul192_no_simd_gf2x(lArray, 6, lArray2, n2 + 3, lArray3, n3 + 3);
        long l2 = lArray2[n2] ^ lArray2[n2 + 3];
        long l3 = lArray2[n2 + 1] ^ lArray2[n2 + 4];
        long l4 = lArray2[n2 + 2] ^ lArray2[n2 + 5];
        long l5 = lArray3[n3] ^ lArray3[n3 + 3];
        long l6 = lArray3[n3 + 1] ^ lArray3[n3 + 4];
        long l7 = lArray3[n3 + 2] ^ lArray3[n3 + 5];
        lArray[6] = lArray[6] ^ lArray[3];
        lArray[7] = lArray[7] ^ lArray[4];
        lArray[8] = lArray[8] ^ lArray[5];
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray4, 0, l2, l5);
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray4, 4, l4, l7);
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray4, 2, l3, l6);
        lArray[3] = lArray[6] ^ lArray[0] ^ lArray4[0];
        lArray4[1] = lArray4[1] ^ lArray4[2];
        lArray4[0] = lArray4[0] ^ lArray4[1];
        lArray4[3] = lArray4[3] ^ lArray4[4];
        lArray4[4] = lArray4[3] ^ lArray4[5];
        lArray[5] = lArray[8] ^ lArray[2] ^ lArray4[3] ^ lArray4[0];
        lArray[6] = lArray[6] ^ (lArray[9] ^ lArray4[1] ^ lArray4[4]);
        lArray[4] = lArray[7] ^ lArray[1] ^ lArray4[0];
        lArray[7] = lArray[7] ^ (lArray[10] ^ lArray4[4]);
        lArray[8] = lArray[8] ^ (lArray[11] ^ lArray4[5]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, 4, l2 ^ l3, l5 ^ l6);
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, 6, l3 ^ l4, l6 ^ l7);
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, 5, l2 ^ l4, l5 ^ l7);
    }

    private static void mul384_no_simd_gf2x_xor(long[] lArray, long[] lArray2, int n2, long[] lArray3, int n3, long[] lArray4) {
        Mul_GF2x.mul192_no_simd_gf2x(lArray4, 0, lArray2, n2, lArray3, n3);
        Mul_GF2x.mul192_no_simd_gf2x(lArray4, 6, lArray2, n2 + 3, lArray3, n3 + 3);
        long l2 = lArray2[n2] ^ lArray2[n2 + 3];
        long l3 = lArray2[n2 + 1] ^ lArray2[n2 + 4];
        long l4 = lArray2[n2 + 2] ^ lArray2[n2 + 5];
        long l5 = lArray3[n3] ^ lArray3[n3 + 3];
        long l6 = lArray3[n3 + 1] ^ lArray3[n3 + 4];
        long l7 = lArray3[n3 + 2] ^ lArray3[n3 + 5];
        lArray4[6] = lArray4[6] ^ lArray4[3];
        lArray4[7] = lArray4[7] ^ lArray4[4];
        lArray4[8] = lArray4[8] ^ lArray4[5];
        lArray[0] = lArray[0] ^ lArray4[0];
        lArray[1] = lArray[1] ^ lArray4[1];
        lArray[2] = lArray[2] ^ lArray4[2];
        lArray[3] = lArray[3] ^ (lArray4[6] ^ lArray4[0]);
        lArray[5] = lArray[5] ^ (lArray4[8] ^ lArray4[2]);
        lArray[6] = lArray[6] ^ (lArray4[6] ^ lArray4[9]);
        lArray[4] = lArray[4] ^ (lArray4[7] ^ lArray4[1]);
        lArray[7] = lArray[7] ^ (lArray4[7] ^ lArray4[10]);
        lArray[8] = lArray[8] ^ (lArray4[8] ^ lArray4[11]);
        lArray[9] = lArray[9] ^ lArray4[9];
        lArray[10] = lArray[10] ^ lArray4[10];
        lArray[11] = lArray[11] ^ lArray4[11];
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray4, 0, l2, l5);
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray4, 4, l4, l7);
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray4, 2, l3, l6);
        lArray[3] = lArray[3] ^ lArray4[0];
        lArray4[1] = lArray4[1] ^ lArray4[2];
        lArray4[0] = lArray4[0] ^ lArray4[1];
        lArray4[3] = lArray4[3] ^ lArray4[4];
        lArray4[4] = lArray4[3] ^ lArray4[5];
        lArray[5] = lArray[5] ^ (lArray4[3] ^ lArray4[0]);
        lArray[6] = lArray[6] ^ (lArray4[1] ^ lArray4[4]);
        lArray[4] = lArray[4] ^ lArray4[0];
        lArray[7] = lArray[7] ^ lArray4[4];
        lArray[8] = lArray[8] ^ lArray4[5];
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, 4, l2 ^ l3, l5 ^ l6);
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, 6, l3 ^ l4, l6 ^ l7);
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, 5, l2 ^ l4, l5 ^ l7);
    }

    private static void mul416_no_simd_gf2x(long[] lArray, long[] lArray2, int n2, long[] lArray3, int n3, long[] lArray4) {
        Mul_GF2x.mul192_no_simd_gf2x(lArray, 0, lArray2, n2, lArray3, n3);
        Mul_GF2x.mul128_no_simd_gf2x(lArray, 6, lArray2[n2 + 3], lArray2[n2 + 4], lArray3[n3 + 3], lArray3[n3 + 4]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray, 10, lArray2[n2 + 5], lArray3[n3 + 5]);
        lArray[12] = Mul_GF2x.MUL32_NO_SIMD_GF2X(lArray2[n2 + 6], lArray3[n3 + 6]) ^ lArray[11];
        lArray[11] = lArray[10] ^ lArray[12];
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray, 11, lArray2[n2 + 5] ^ lArray2[n2 + 6], lArray3[n3 + 5] ^ lArray3[n3 + 6]);
        lArray[8] = lArray[8] ^ lArray[10];
        lArray[11] = lArray[11] ^ lArray[9];
        lArray[10] = lArray[8] ^ lArray[12];
        lArray[8] = lArray[8] ^ lArray[6];
        lArray[9] = lArray[11] ^ lArray[7];
        Mul_GF2x.mul128_no_simd_gf2x_xor(lArray, 8, lArray2[n2 + 3] ^ lArray2[n2 + 5], lArray2[n2 + 4] ^ lArray2[n2 + 6], lArray3[n3 + 3] ^ lArray3[n3 + 5], lArray3[n3 + 4] ^ lArray3[n3 + 6], lArray4);
        long l2 = lArray2[n2] ^ lArray2[n2 + 3];
        long l3 = lArray2[n2 + 1] ^ lArray2[n2 + 4];
        long l4 = lArray2[n2 + 2] ^ lArray2[n2 + 5];
        long l5 = lArray2[n2 + 6];
        long l6 = lArray3[n3] ^ lArray3[n3 + 3];
        long l7 = lArray3[n3 + 1] ^ lArray3[n3 + 4];
        long l8 = lArray3[n3 + 2] ^ lArray3[n3 + 5];
        long l9 = lArray3[n3 + 6];
        lArray[6] = lArray[6] ^ lArray[3];
        lArray[7] = lArray[7] ^ lArray[4];
        lArray[8] = lArray[8] ^ lArray[5];
        Mul_GF2x.mul128_no_simd_gf2x(lArray4, 0, l2, l3, l6, l7);
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray4, 4, l4, l8);
        lArray4[6] = Mul_GF2x.MUL32_NO_SIMD_GF2X(l5, l9) ^ lArray4[5];
        lArray4[5] = lArray4[4] ^ lArray4[6];
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray4, 5, l4 ^ l5, l8 ^ l9);
        lArray[3] = lArray[6] ^ lArray[0] ^ lArray4[0];
        lArray[4] = lArray[7] ^ lArray[1] ^ lArray4[1];
        lArray4[2] = lArray4[2] ^ lArray4[4];
        lArray4[3] = lArray4[3] ^ lArray4[5];
        lArray[5] = lArray[8] ^ lArray[2] ^ lArray4[2] ^ lArray4[0];
        lArray[6] = lArray[6] ^ (lArray[9] ^ lArray4[3] ^ lArray4[1]);
        lArray[7] = lArray[7] ^ (lArray[10] ^ lArray4[2] ^ lArray4[6]);
        lArray[8] = lArray[8] ^ (lArray[11] ^ lArray4[3]);
        lArray[9] = lArray[9] ^ (lArray[12] ^ lArray4[6]);
        Mul_GF2x.mul128_no_simd_gf2x_xor(lArray, 5, l2 ^ l4, l3 ^ l5, l6 ^ l8, l7 ^ l9, lArray4);
    }

    private static void mul416_no_simd_gf2x_xor(long[] lArray, long[] lArray2, int n2, long[] lArray3, int n3, long[] lArray4, long[] lArray5) {
        Mul_GF2x.mul192_no_simd_gf2x(lArray4, 0, lArray2, n2, lArray3, n3);
        Mul_GF2x.mul128_no_simd_gf2x(lArray4, 6, lArray2[n2 + 3], lArray2[n2 + 4], lArray3[n3 + 3], lArray3[n3 + 4]);
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray4, 10, lArray2[n2 + 5], lArray3[n3 + 5]);
        lArray4[12] = Mul_GF2x.MUL32_NO_SIMD_GF2X(lArray2[n2 + 6], lArray3[n3 + 6]) ^ lArray4[11];
        lArray4[11] = lArray4[10] ^ lArray4[12];
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray4, 11, lArray2[n2 + 5] ^ lArray2[n2 + 6], lArray3[n3 + 5] ^ lArray3[n3 + 6]);
        lArray4[8] = lArray4[8] ^ lArray4[10];
        lArray4[11] = lArray4[11] ^ lArray4[9];
        lArray4[10] = lArray4[8] ^ lArray4[12];
        lArray4[8] = lArray4[8] ^ lArray4[6];
        lArray4[9] = lArray4[11] ^ lArray4[7];
        lArray4[6] = lArray4[6] ^ lArray4[3];
        lArray4[7] = lArray4[7] ^ lArray4[4];
        lArray4[8] = lArray4[8] ^ lArray4[5];
        Mul_GF2x.mul128_no_simd_gf2x_xor(lArray4, 8, lArray2[n2 + 3] ^ lArray2[n2 + 5], lArray2[n2 + 4] ^ lArray2[n2 + 6], lArray3[n3 + 3] ^ lArray3[n3 + 5], lArray3[n3 + 4] ^ lArray3[n3 + 6], lArray5);
        lArray[0] = lArray[0] ^ lArray4[0];
        lArray[1] = lArray[1] ^ lArray4[1];
        lArray[2] = lArray[2] ^ lArray4[2];
        lArray[3] = lArray[3] ^ (lArray4[6] ^ lArray4[0]);
        lArray[4] = lArray[4] ^ (lArray4[7] ^ lArray4[1]);
        lArray[5] = lArray[5] ^ (lArray4[8] ^ lArray4[2]);
        lArray[6] = lArray[6] ^ (lArray4[6] ^ lArray4[9]);
        lArray[7] = lArray[7] ^ (lArray4[7] ^ lArray4[10]);
        lArray[8] = lArray[8] ^ (lArray4[8] ^ lArray4[11]);
        lArray[9] = lArray[9] ^ (lArray4[9] ^ lArray4[12]);
        lArray[10] = lArray[10] ^ lArray4[10];
        lArray[11] = lArray[11] ^ lArray4[11];
        lArray[12] = lArray[12] ^ lArray4[12];
        long l2 = lArray2[n2] ^ lArray2[n2 + 3];
        long l3 = lArray2[n2 + 1] ^ lArray2[n2 + 4];
        long l4 = lArray2[n2 + 2] ^ lArray2[n2 + 5];
        long l5 = lArray2[n2 + 6];
        long l6 = lArray3[n3] ^ lArray3[n3 + 3];
        long l7 = lArray3[n3 + 1] ^ lArray3[n3 + 4];
        long l8 = lArray3[n3 + 2] ^ lArray3[n3 + 5];
        long l9 = lArray3[n3 + 6];
        Mul_GF2x.mul128_no_simd_gf2x(lArray4, 0, l2, l3, l6, l7);
        Mul_GF2x.MUL64_NO_SIMD_GF2X(lArray4, 4, l4, l8);
        lArray4[6] = Mul_GF2x.MUL32_NO_SIMD_GF2X(l5, l9) ^ lArray4[5];
        lArray4[5] = lArray4[4] ^ lArray4[6];
        Mul_GF2x.MUL64_NO_SIMD_GF2X_XOR(lArray4, 5, l4 ^ l5, l8 ^ l9);
        lArray[3] = lArray[3] ^ lArray4[0];
        lArray[4] = lArray[4] ^ lArray4[1];
        lArray4[2] = lArray4[2] ^ lArray4[4];
        lArray4[3] = lArray4[3] ^ lArray4[5];
        lArray[5] = lArray[5] ^ (lArray4[2] ^ lArray4[0]);
        lArray[6] = lArray[6] ^ (lArray4[3] ^ lArray4[1]);
        lArray[7] = lArray[7] ^ (lArray4[2] ^ lArray4[6]);
        lArray[8] = lArray[8] ^ lArray4[3];
        lArray[9] = lArray[9] ^ lArray4[6];
        Mul_GF2x.mul128_no_simd_gf2x_xor(lArray, 5, l2 ^ l4, l3 ^ l5, l6 ^ l8, l7 ^ l9, lArray4);
    }

    private static void mul544_no_simd_gf2x(long[] lArray, long[] lArray2, int n2, long[] lArray3, int n3, long[] lArray4, long[] lArray5, long[] lArray6) {
        Mul_GF2x.mul128_no_simd_gf2x(lArray, 0, lArray2[n2], lArray2[n2 + 1], lArray3[n3], lArray3[n3 + 1]);
        Mul_GF2x.mul128_no_simd_gf2x(lArray, 4, lArray2[n2 + 2], lArray2[n2 + 3], lArray3[n3 + 2], lArray3[n3 + 3]);
        lArray[4] = lArray[4] ^ lArray[2];
        lArray[5] = lArray[5] ^ lArray[3];
        lArray[2] = lArray[4] ^ lArray[0];
        lArray[3] = lArray[5] ^ lArray[1];
        lArray[4] = lArray[4] ^ lArray[6];
        lArray[5] = lArray[5] ^ lArray[7];
        Mul_GF2x.mul128_no_simd_gf2x_xor(lArray, 2, lArray2[n2] ^ lArray2[n2 + 2], lArray2[n2 + 1] ^ lArray2[n2 + 3], lArray3[n3] ^ lArray3[n3 + 2], lArray3[n3 + 1] ^ lArray3[n3 + 3], lArray6);
        Mul_GF2x.mul288_no_simd_gf2x(lArray, 8, lArray2, n2 + 4, lArray3, n3 + 4, lArray6);
        lArray[8] = lArray[8] ^ lArray[4];
        lArray[9] = lArray[9] ^ lArray[5];
        lArray[10] = lArray[10] ^ lArray[6];
        lArray[11] = lArray[11] ^ lArray[7];
        lArray[4] = lArray[8] ^ lArray[0];
        lArray[5] = lArray[9] ^ lArray[1];
        lArray[6] = lArray[10] ^ lArray[2];
        lArray[7] = lArray[11] ^ lArray[3];
        lArray[8] = lArray[8] ^ lArray[12];
        lArray[9] = lArray[9] ^ lArray[13];
        lArray[10] = lArray[10] ^ lArray[14];
        lArray[11] = lArray[11] ^ lArray[15];
        lArray[12] = lArray[12] ^ lArray[16];
        lArray4[0] = lArray2[n2] ^ lArray2[n2 + 4];
        lArray4[1] = lArray2[n2 + 1] ^ lArray2[n2 + 5];
        lArray4[2] = lArray2[n2 + 2] ^ lArray2[n2 + 6];
        lArray4[3] = lArray2[n2 + 3] ^ lArray2[n2 + 7];
        lArray4[4] = lArray2[n2 + 8];
        lArray5[0] = lArray3[n3] ^ lArray3[n3 + 4];
        lArray5[1] = lArray3[n3 + 1] ^ lArray3[n3 + 5];
        lArray5[2] = lArray3[n3 + 2] ^ lArray3[n3 + 6];
        lArray5[3] = lArray3[n3 + 3] ^ lArray3[n3 + 7];
        lArray5[4] = lArray3[n3 + 8];
        Mul_GF2x.mul288_no_simd_gf2x_xor(lArray, 4, lArray4, 0, lArray5, 0, lArray6);
    }

    private static void mul544_no_simd_gf2x_xor(long[] lArray, long[] lArray2, int n2, long[] lArray3, int n3, long[] lArray4, long[] lArray5, long[] lArray6, long[] lArray7) {
        Mul_GF2x.mul128_no_simd_gf2x(lArray6, 0, lArray2[n2], lArray2[n2 + 1], lArray3[n3], lArray3[n3 + 1]);
        Mul_GF2x.mul128_no_simd_gf2x(lArray6, 4, lArray2[n2 + 2], lArray2[n2 + 3], lArray3[n3 + 2], lArray3[n3 + 3]);
        lArray6[4] = lArray6[4] ^ lArray6[2];
        lArray6[5] = lArray6[5] ^ lArray6[3];
        lArray6[2] = lArray6[4] ^ lArray6[0];
        lArray6[3] = lArray6[5] ^ lArray6[1];
        lArray6[4] = lArray6[4] ^ lArray6[6];
        lArray6[5] = lArray6[5] ^ lArray6[7];
        Mul_GF2x.mul128_no_simd_gf2x_xor(lArray6, 2, lArray2[n2] ^ lArray2[n2 + 2], lArray2[n2 + 1] ^ lArray2[n2 + 3], lArray3[n3] ^ lArray3[n3 + 2], lArray3[n3 + 1] ^ lArray3[n3 + 3], lArray7);
        Mul_GF2x.mul288_no_simd_gf2x(lArray6, 8, lArray2, n2 + 4, lArray3, n3 + 4, lArray7);
        lArray6[8] = lArray6[8] ^ lArray6[4];
        lArray6[9] = lArray6[9] ^ lArray6[5];
        lArray6[10] = lArray6[10] ^ lArray6[6];
        lArray6[11] = lArray6[11] ^ lArray6[7];
        lArray[0] = lArray[0] ^ lArray6[0];
        lArray[1] = lArray[1] ^ lArray6[1];
        lArray[2] = lArray[2] ^ lArray6[2];
        lArray[3] = lArray[3] ^ lArray6[3];
        lArray[4] = lArray[4] ^ (lArray6[8] ^ lArray6[0]);
        lArray[5] = lArray[5] ^ (lArray6[9] ^ lArray6[1]);
        lArray[6] = lArray[6] ^ (lArray6[10] ^ lArray6[2]);
        lArray[7] = lArray[7] ^ (lArray6[11] ^ lArray6[3]);
        lArray[8] = lArray[8] ^ (lArray6[8] ^ lArray6[12]);
        lArray[9] = lArray[9] ^ (lArray6[9] ^ lArray6[13]);
        lArray[10] = lArray[10] ^ (lArray6[10] ^ lArray6[14]);
        lArray[11] = lArray[11] ^ (lArray6[11] ^ lArray6[15]);
        lArray[12] = lArray[12] ^ (lArray6[12] ^ lArray6[16]);
        lArray[13] = lArray[13] ^ lArray6[13];
        lArray[14] = lArray[14] ^ lArray6[14];
        lArray[15] = lArray[15] ^ lArray6[15];
        lArray[16] = lArray[16] ^ lArray6[16];
        lArray4[0] = lArray2[n2] ^ lArray2[n2 + 4];
        lArray4[1] = lArray2[n2 + 1] ^ lArray2[n2 + 5];
        lArray4[2] = lArray2[n2 + 2] ^ lArray2[n2 + 6];
        lArray4[3] = lArray2[n2 + 3] ^ lArray2[n2 + 7];
        lArray4[4] = lArray2[n2 + 8];
        lArray5[0] = lArray3[n3] ^ lArray3[n3 + 4];
        lArray5[1] = lArray3[n3 + 1] ^ lArray3[n3 + 5];
        lArray5[2] = lArray3[n3 + 2] ^ lArray3[n3 + 6];
        lArray5[3] = lArray3[n3 + 3] ^ lArray3[n3 + 7];
        lArray5[4] = lArray3[n3 + 8];
        Mul_GF2x.mul288_no_simd_gf2x_xor(lArray, 4, lArray4, 0, lArray5, 0, lArray6);
    }

    public static class Mul12
    extends Mul_GF2x {
        private long[] Buffer = new long[12];

        @Override
        public void mul_gf2x(Pointer pointer, Pointer pointer2, Pointer pointer3) {
            Mul_GF2x.mul384_no_simd_gf2x(pointer.array, pointer2.array, pointer2.cp, pointer3.array, pointer3.cp, this.Buffer);
        }

        @Override
        public void sqr_gf2x(long[] lArray, long[] lArray2, int n2) {
            Mul_GF2x.SQR128_NO_SIMD_GF2X(lArray, 8, lArray2, n2 + 4);
            Mul_GF2x.SQR256_NO_SIMD_GF2X(lArray, 0, lArray2, n2);
        }

        @Override
        public void mul_gf2x_xor(Pointer pointer, Pointer pointer2, Pointer pointer3) {
            Mul_GF2x.mul384_no_simd_gf2x_xor(pointer.array, pointer2.array, pointer2.cp, pointer3.array, pointer3.cp, this.Buffer);
        }
    }

    public static class Mul13
    extends Mul_GF2x {
        private long[] Buffer = new long[13];
        private long[] Buffer2 = new long[4];

        @Override
        public void mul_gf2x(Pointer pointer, Pointer pointer2, Pointer pointer3) {
            Mul_GF2x.mul416_no_simd_gf2x(pointer.array, pointer2.array, pointer2.cp, pointer3.array, pointer3.cp, this.Buffer);
        }

        @Override
        public void sqr_gf2x(long[] lArray, long[] lArray2, int n2) {
            lArray[12] = Mul_GF2x.SQR32_NO_SIMD_GF2X(lArray2[n2 + 6]);
            Mul_GF2x.SQR128_NO_SIMD_GF2X(lArray, 8, lArray2, n2 + 4);
            Mul_GF2x.SQR256_NO_SIMD_GF2X(lArray, 0, lArray2, n2);
        }

        @Override
        public void mul_gf2x_xor(Pointer pointer, Pointer pointer2, Pointer pointer3) {
            Mul_GF2x.mul416_no_simd_gf2x_xor(pointer.array, pointer2.array, pointer2.cp, pointer3.array, pointer3.cp, this.Buffer, this.Buffer2);
        }
    }

    public static class Mul17
    extends Mul_GF2x {
        private long[] AA = new long[5];
        private long[] BB = new long[5];
        private long[] Buffer1 = new long[17];
        private long[] Buffer2 = new long[4];

        @Override
        public void mul_gf2x(Pointer pointer, Pointer pointer2, Pointer pointer3) {
            Mul_GF2x.mul544_no_simd_gf2x(pointer.array, pointer2.array, pointer2.cp, pointer3.array, pointer3.cp, this.AA, this.BB, this.Buffer1);
        }

        @Override
        public void sqr_gf2x(long[] lArray, long[] lArray2, int n2) {
            lArray[16] = Mul_GF2x.SQR32_NO_SIMD_GF2X(lArray2[n2 + 8]);
            Mul_GF2x.SQR256_NO_SIMD_GF2X(lArray, 8, lArray2, n2 + 4);
            Mul_GF2x.SQR256_NO_SIMD_GF2X(lArray, 0, lArray2, n2);
        }

        @Override
        public void mul_gf2x_xor(Pointer pointer, Pointer pointer2, Pointer pointer3) {
            Mul_GF2x.mul544_no_simd_gf2x_xor(pointer.array, pointer2.array, pointer2.cp, pointer3.array, pointer3.cp, this.AA, this.BB, this.Buffer1, this.Buffer2);
        }
    }

    public static class Mul6
    extends Mul_GF2x {
        private long[] Buffer = new long[6];

        @Override
        public void mul_gf2x(Pointer pointer, Pointer pointer2, Pointer pointer3) {
            Mul6.mul192_no_simd_gf2x(pointer.array, 0, pointer2.array, pointer2.cp, pointer3.array, pointer3.cp);
        }

        @Override
        public void sqr_gf2x(long[] lArray, long[] lArray2, int n2) {
            Mul_GF2x.SQR64_NO_SIMD_GF2X(lArray, 4, lArray2[n2 + 2]);
            Mul_GF2x.SQR128_NO_SIMD_GF2X(lArray, 0, lArray2, n2);
        }

        @Override
        public void mul_gf2x_xor(Pointer pointer, Pointer pointer2, Pointer pointer3) {
            Mul6.mul192_no_simd_gf2x_xor(pointer.array, pointer.cp, pointer2.array, pointer2.cp, pointer3.array, pointer3.cp, this.Buffer);
        }
    }

    public static class Mul9
    extends Mul_GF2x {
        private long[] Buffer = new long[9];

        @Override
        public void mul_gf2x(Pointer pointer, Pointer pointer2, Pointer pointer3) {
            Mul_GF2x.mul288_no_simd_gf2x(pointer.array, 0, pointer2.array, pointer2.cp, pointer3.array, pointer3.cp, this.Buffer);
        }

        @Override
        public void sqr_gf2x(long[] lArray, long[] lArray2, int n2) {
            lArray[8] = Mul_GF2x.SQR32_NO_SIMD_GF2X(lArray2[n2 + 4]);
            Mul_GF2x.SQR256_NO_SIMD_GF2X(lArray, 0, lArray2, n2);
        }

        @Override
        public void mul_gf2x_xor(Pointer pointer, Pointer pointer2, Pointer pointer3) {
            Mul_GF2x.mul288_no_simd_gf2x_xor(pointer.array, pointer.cp, pointer2.array, pointer2.cp, pointer3.array, pointer3.cp, this.Buffer);
        }
    }
}

