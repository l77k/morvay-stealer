/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.raw.Interleave;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat320;

public class SecT283Field {
    private static final long M27 = 0x7FFFFFFL;
    private static final long M57 = 0x1FFFFFFFFFFFFFFL;
    private static final long[] ROOT_Z = new long[]{878416384462358536L, 0x30C30C30C30C30C3L, -9076969306111048948L, 0x820820820820820L, 0x2082082L};

    public static void add(long[] lArray, long[] lArray2, long[] lArray3) {
        lArray3[0] = lArray[0] ^ lArray2[0];
        lArray3[1] = lArray[1] ^ lArray2[1];
        lArray3[2] = lArray[2] ^ lArray2[2];
        lArray3[3] = lArray[3] ^ lArray2[3];
        lArray3[4] = lArray[4] ^ lArray2[4];
    }

    public static void addExt(long[] lArray, long[] lArray2, long[] lArray3) {
        lArray3[0] = lArray[0] ^ lArray2[0];
        lArray3[1] = lArray[1] ^ lArray2[1];
        lArray3[2] = lArray[2] ^ lArray2[2];
        lArray3[3] = lArray[3] ^ lArray2[3];
        lArray3[4] = lArray[4] ^ lArray2[4];
        lArray3[5] = lArray[5] ^ lArray2[5];
        lArray3[6] = lArray[6] ^ lArray2[6];
        lArray3[7] = lArray[7] ^ lArray2[7];
        lArray3[8] = lArray[8] ^ lArray2[8];
    }

    public static void addOne(long[] lArray, long[] lArray2) {
        lArray2[0] = lArray[0] ^ 1L;
        lArray2[1] = lArray[1];
        lArray2[2] = lArray[2];
        lArray2[3] = lArray[3];
        lArray2[4] = lArray[4];
    }

    private static void addTo(long[] lArray, long[] lArray2) {
        lArray2[0] = lArray2[0] ^ lArray[0];
        lArray2[1] = lArray2[1] ^ lArray[1];
        lArray2[2] = lArray2[2] ^ lArray[2];
        lArray2[3] = lArray2[3] ^ lArray[3];
        lArray2[4] = lArray2[4] ^ lArray[4];
    }

    public static long[] fromBigInteger(BigInteger bigInteger) {
        return Nat.fromBigInteger64(283, bigInteger);
    }

    public static void halfTrace(long[] lArray, long[] lArray2) {
        long[] lArray3 = Nat.create64(9);
        Nat320.copy64(lArray, lArray2);
        for (int i2 = 1; i2 < 283; i2 += 2) {
            SecT283Field.implSquare(lArray2, lArray3);
            SecT283Field.reduce(lArray3, lArray2);
            SecT283Field.implSquare(lArray2, lArray3);
            SecT283Field.reduce(lArray3, lArray2);
            SecT283Field.addTo(lArray, lArray2);
        }
    }

    public static void invert(long[] lArray, long[] lArray2) {
        if (Nat320.isZero64(lArray)) {
            throw new IllegalStateException();
        }
        long[] lArray3 = Nat320.create64();
        long[] lArray4 = Nat320.create64();
        SecT283Field.square(lArray, lArray3);
        SecT283Field.multiply(lArray3, lArray, lArray3);
        SecT283Field.squareN(lArray3, 2, lArray4);
        SecT283Field.multiply(lArray4, lArray3, lArray4);
        SecT283Field.squareN(lArray4, 4, lArray3);
        SecT283Field.multiply(lArray3, lArray4, lArray3);
        SecT283Field.squareN(lArray3, 8, lArray4);
        SecT283Field.multiply(lArray4, lArray3, lArray4);
        SecT283Field.square(lArray4, lArray4);
        SecT283Field.multiply(lArray4, lArray, lArray4);
        SecT283Field.squareN(lArray4, 17, lArray3);
        SecT283Field.multiply(lArray3, lArray4, lArray3);
        SecT283Field.square(lArray3, lArray3);
        SecT283Field.multiply(lArray3, lArray, lArray3);
        SecT283Field.squareN(lArray3, 35, lArray4);
        SecT283Field.multiply(lArray4, lArray3, lArray4);
        SecT283Field.squareN(lArray4, 70, lArray3);
        SecT283Field.multiply(lArray3, lArray4, lArray3);
        SecT283Field.square(lArray3, lArray3);
        SecT283Field.multiply(lArray3, lArray, lArray3);
        SecT283Field.squareN(lArray3, 141, lArray4);
        SecT283Field.multiply(lArray4, lArray3, lArray4);
        SecT283Field.square(lArray4, lArray2);
    }

    public static void multiply(long[] lArray, long[] lArray2, long[] lArray3) {
        long[] lArray4 = Nat320.createExt64();
        SecT283Field.implMultiply(lArray, lArray2, lArray4);
        SecT283Field.reduce(lArray4, lArray3);
    }

    public static void multiplyAddToExt(long[] lArray, long[] lArray2, long[] lArray3) {
        long[] lArray4 = Nat320.createExt64();
        SecT283Field.implMultiply(lArray, lArray2, lArray4);
        SecT283Field.addExt(lArray3, lArray4, lArray3);
    }

    public static void reduce(long[] lArray, long[] lArray2) {
        long l2 = lArray[0];
        long l3 = lArray[1];
        long l4 = lArray[2];
        long l5 = lArray[3];
        long l6 = lArray[4];
        long l7 = lArray[5];
        long l8 = lArray[6];
        long l9 = lArray[7];
        long l10 = lArray[8];
        l5 ^= l10 << 37 ^ l10 << 42 ^ l10 << 44 ^ l10 << 49;
        l4 ^= l9 << 37 ^ l9 << 42 ^ l9 << 44 ^ l9 << 49;
        l5 ^= l9 >>> 27 ^ l9 >>> 22 ^ l9 >>> 20 ^ l9 >>> 15;
        l3 ^= l8 << 37 ^ l8 << 42 ^ l8 << 44 ^ l8 << 49;
        long l11 = (l6 ^= l10 >>> 27 ^ l10 >>> 22 ^ l10 >>> 20 ^ l10 >>> 15) >>> 27;
        lArray2[0] = (l2 ^= l7 << 37 ^ l7 << 42 ^ l7 << 44 ^ l7 << 49) ^ l11 ^ l11 << 5 ^ l11 << 7 ^ l11 << 12;
        lArray2[1] = l3 ^= l7 >>> 27 ^ l7 >>> 22 ^ l7 >>> 20 ^ l7 >>> 15;
        lArray2[2] = l4 ^= l8 >>> 27 ^ l8 >>> 22 ^ l8 >>> 20 ^ l8 >>> 15;
        lArray2[3] = l5;
        lArray2[4] = l6 & 0x7FFFFFFL;
    }

    public static void reduce37(long[] lArray, int n2) {
        long l2 = lArray[n2 + 4];
        long l3 = l2 >>> 27;
        int n3 = n2;
        lArray[n3] = lArray[n3] ^ (l3 ^ l3 << 5 ^ l3 << 7 ^ l3 << 12);
        lArray[n2 + 4] = l2 & 0x7FFFFFFL;
    }

    public static void sqrt(long[] lArray, long[] lArray2) {
        long[] lArray3 = Nat320.create64();
        long l2 = Interleave.unshuffle(lArray[0]);
        long l3 = Interleave.unshuffle(lArray[1]);
        long l4 = l2 & 0xFFFFFFFFL | l3 << 32;
        lArray3[0] = l2 >>> 32 | l3 & 0xFFFFFFFF00000000L;
        l2 = Interleave.unshuffle(lArray[2]);
        l3 = Interleave.unshuffle(lArray[3]);
        long l5 = l2 & 0xFFFFFFFFL | l3 << 32;
        lArray3[1] = l2 >>> 32 | l3 & 0xFFFFFFFF00000000L;
        l2 = Interleave.unshuffle(lArray[4]);
        long l6 = l2 & 0xFFFFFFFFL;
        lArray3[2] = l2 >>> 32;
        SecT283Field.multiply(lArray3, ROOT_Z, lArray2);
        lArray2[0] = lArray2[0] ^ l4;
        lArray2[1] = lArray2[1] ^ l5;
        lArray2[2] = lArray2[2] ^ l6;
    }

    public static void square(long[] lArray, long[] lArray2) {
        long[] lArray3 = Nat.create64(9);
        SecT283Field.implSquare(lArray, lArray3);
        SecT283Field.reduce(lArray3, lArray2);
    }

    public static void squareAddToExt(long[] lArray, long[] lArray2) {
        long[] lArray3 = Nat.create64(9);
        SecT283Field.implSquare(lArray, lArray3);
        SecT283Field.addExt(lArray2, lArray3, lArray2);
    }

    public static void squareN(long[] lArray, int n2, long[] lArray2) {
        long[] lArray3 = Nat.create64(9);
        SecT283Field.implSquare(lArray, lArray3);
        SecT283Field.reduce(lArray3, lArray2);
        while (--n2 > 0) {
            SecT283Field.implSquare(lArray2, lArray3);
            SecT283Field.reduce(lArray3, lArray2);
        }
    }

    public static int trace(long[] lArray) {
        return (int)(lArray[0] ^ lArray[4] >>> 15) & 1;
    }

    protected static void implCompactExt(long[] lArray) {
        long l2 = lArray[0];
        long l3 = lArray[1];
        long l4 = lArray[2];
        long l5 = lArray[3];
        long l6 = lArray[4];
        long l7 = lArray[5];
        long l8 = lArray[6];
        long l9 = lArray[7];
        long l10 = lArray[8];
        long l11 = lArray[9];
        lArray[0] = l2 ^ l3 << 57;
        lArray[1] = l3 >>> 7 ^ l4 << 50;
        lArray[2] = l4 >>> 14 ^ l5 << 43;
        lArray[3] = l5 >>> 21 ^ l6 << 36;
        lArray[4] = l6 >>> 28 ^ l7 << 29;
        lArray[5] = l7 >>> 35 ^ l8 << 22;
        lArray[6] = l8 >>> 42 ^ l9 << 15;
        lArray[7] = l9 >>> 49 ^ l10 << 8;
        lArray[8] = l10 >>> 56 ^ l11 << 1;
        lArray[9] = l11 >>> 63;
    }

    protected static void implExpand(long[] lArray, long[] lArray2) {
        long l2 = lArray[0];
        long l3 = lArray[1];
        long l4 = lArray[2];
        long l5 = lArray[3];
        long l6 = lArray[4];
        lArray2[0] = l2 & 0x1FFFFFFFFFFFFFFL;
        lArray2[1] = (l2 >>> 57 ^ l3 << 7) & 0x1FFFFFFFFFFFFFFL;
        lArray2[2] = (l3 >>> 50 ^ l4 << 14) & 0x1FFFFFFFFFFFFFFL;
        lArray2[3] = (l4 >>> 43 ^ l5 << 21) & 0x1FFFFFFFFFFFFFFL;
        lArray2[4] = l5 >>> 36 ^ l6 << 28;
    }

    protected static void implMultiply(long[] lArray, long[] lArray2, long[] lArray3) {
        long l2;
        long l3;
        long l4;
        long l5;
        long l6;
        long l7;
        long l8;
        long l9;
        long[] lArray4 = new long[5];
        long[] lArray5 = new long[5];
        SecT283Field.implExpand(lArray, lArray4);
        SecT283Field.implExpand(lArray2, lArray5);
        long[] lArray6 = lArray3;
        long[] lArray7 = new long[26];
        SecT283Field.implMulw(lArray6, lArray4[0], lArray5[0], lArray7, 0);
        SecT283Field.implMulw(lArray6, lArray4[1], lArray5[1], lArray7, 2);
        SecT283Field.implMulw(lArray6, lArray4[2], lArray5[2], lArray7, 4);
        SecT283Field.implMulw(lArray6, lArray4[3], lArray5[3], lArray7, 6);
        SecT283Field.implMulw(lArray6, lArray4[4], lArray5[4], lArray7, 8);
        long l10 = lArray4[0] ^ lArray4[1];
        long l11 = lArray5[0] ^ lArray5[1];
        long l12 = lArray4[0] ^ lArray4[2];
        long l13 = lArray5[0] ^ lArray5[2];
        long l14 = lArray4[2] ^ lArray4[4];
        long l15 = lArray5[2] ^ lArray5[4];
        long l16 = lArray4[3] ^ lArray4[4];
        long l17 = lArray5[3] ^ lArray5[4];
        SecT283Field.implMulw(lArray6, l12 ^ lArray4[3], l13 ^ lArray5[3], lArray7, 18);
        SecT283Field.implMulw(lArray6, l14 ^ lArray4[1], l15 ^ lArray5[1], lArray7, 20);
        long l18 = l10 ^ l16;
        long l19 = l11 ^ l17;
        long l20 = l18 ^ lArray4[2];
        long l21 = l19 ^ lArray5[2];
        SecT283Field.implMulw(lArray6, l18, l19, lArray7, 22);
        SecT283Field.implMulw(lArray6, l20, l21, lArray7, 24);
        SecT283Field.implMulw(lArray6, l10, l11, lArray7, 10);
        SecT283Field.implMulw(lArray6, l12, l13, lArray7, 12);
        SecT283Field.implMulw(lArray6, l14, l15, lArray7, 14);
        SecT283Field.implMulw(lArray6, l16, l17, lArray7, 16);
        lArray3[0] = lArray7[0];
        lArray3[9] = lArray7[9];
        long l22 = lArray7[0] ^ lArray7[1];
        long l23 = l22 ^ lArray7[2];
        lArray3[1] = l9 = l23 ^ lArray7[10];
        long l24 = lArray7[3] ^ lArray7[4];
        long l25 = lArray7[11] ^ lArray7[12];
        long l26 = l24 ^ l25;
        lArray3[2] = l8 = l23 ^ l26;
        long l27 = l22 ^ l24;
        long l28 = lArray7[5] ^ lArray7[6];
        long l29 = l27 ^ l28;
        long l30 = l29 ^ lArray7[8];
        long l31 = lArray7[13] ^ lArray7[14];
        long l32 = l30 ^ l31;
        long l33 = lArray7[18] ^ lArray7[22];
        long l34 = l33 ^ lArray7[24];
        lArray3[3] = l7 = l32 ^ l34;
        long l35 = lArray7[7] ^ lArray7[8];
        long l36 = l35 ^ lArray7[9];
        lArray3[8] = l6 = l36 ^ lArray7[17];
        long l37 = l36 ^ l28;
        long l38 = lArray7[15] ^ lArray7[16];
        lArray3[7] = l5 = l37 ^ l38;
        long l39 = l5 ^ l9;
        long l40 = lArray7[19] ^ lArray7[20];
        long l41 = lArray7[25] ^ lArray7[24];
        long l42 = lArray7[18] ^ lArray7[23];
        long l43 = l40 ^ l41;
        long l44 = l43 ^ l42;
        lArray3[4] = l4 = l44 ^ l39;
        long l45 = l8 ^ l6;
        long l46 = l43 ^ l45;
        long l47 = lArray7[21] ^ lArray7[22];
        lArray3[5] = l3 = l46 ^ l47;
        long l48 = l30 ^ lArray7[0];
        long l49 = l48 ^ lArray7[9];
        long l50 = l49 ^ l31;
        long l51 = l50 ^ lArray7[21];
        long l52 = l51 ^ lArray7[23];
        lArray3[6] = l2 = l52 ^ lArray7[25];
        SecT283Field.implCompactExt(lArray3);
    }

    protected static void implMulw(long[] lArray, long l2, long l3, long[] lArray2, int n2) {
        lArray[1] = l3;
        lArray[2] = lArray[1] << 1;
        lArray[3] = lArray[2] ^ l3;
        lArray[4] = lArray[2] << 1;
        lArray[5] = lArray[4] ^ l3;
        lArray[6] = lArray[3] << 1;
        lArray[7] = lArray[6] ^ l3;
        int n3 = (int)l2;
        long l4 = 0L;
        long l5 = lArray[n3 & 7];
        int n4 = 48;
        do {
            n3 = (int)(l2 >>> n4);
            long l6 = lArray[n3 & 7] ^ lArray[n3 >>> 3 & 7] << 3 ^ lArray[n3 >>> 6 & 7] << 6;
            l5 ^= l6 << n4;
            l4 ^= l6 >>> -n4;
        } while ((n4 -= 9) > 0);
        lArray2[n2] = l5 & 0x1FFFFFFFFFFFFFFL;
        lArray2[n2 + 1] = l5 >>> 57 ^ (l4 ^= (l2 & 0x100804020100800L & l3 << 7 >> 63) >>> 8) << 7;
    }

    protected static void implSquare(long[] lArray, long[] lArray2) {
        Interleave.expand64To128(lArray, 0, 4, lArray2, 0);
        lArray2[8] = Interleave.expand32to64((int)lArray[4]);
    }
}

