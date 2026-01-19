/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.raw.Interleave;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat448;

public class SecT409Field {
    private static final long M25 = 0x1FFFFFFL;
    private static final long M59 = 0x7FFFFFFFFFFFFFFL;

    public static void add(long[] lArray, long[] lArray2, long[] lArray3) {
        lArray3[0] = lArray[0] ^ lArray2[0];
        lArray3[1] = lArray[1] ^ lArray2[1];
        lArray3[2] = lArray[2] ^ lArray2[2];
        lArray3[3] = lArray[3] ^ lArray2[3];
        lArray3[4] = lArray[4] ^ lArray2[4];
        lArray3[5] = lArray[5] ^ lArray2[5];
        lArray3[6] = lArray[6] ^ lArray2[6];
    }

    public static void addExt(long[] lArray, long[] lArray2, long[] lArray3) {
        for (int i2 = 0; i2 < 13; ++i2) {
            lArray3[i2] = lArray[i2] ^ lArray2[i2];
        }
    }

    public static void addOne(long[] lArray, long[] lArray2) {
        lArray2[0] = lArray[0] ^ 1L;
        lArray2[1] = lArray[1];
        lArray2[2] = lArray[2];
        lArray2[3] = lArray[3];
        lArray2[4] = lArray[4];
        lArray2[5] = lArray[5];
        lArray2[6] = lArray[6];
    }

    private static void addTo(long[] lArray, long[] lArray2) {
        lArray2[0] = lArray2[0] ^ lArray[0];
        lArray2[1] = lArray2[1] ^ lArray[1];
        lArray2[2] = lArray2[2] ^ lArray[2];
        lArray2[3] = lArray2[3] ^ lArray[3];
        lArray2[4] = lArray2[4] ^ lArray[4];
        lArray2[5] = lArray2[5] ^ lArray[5];
        lArray2[6] = lArray2[6] ^ lArray[6];
    }

    public static long[] fromBigInteger(BigInteger bigInteger) {
        return Nat.fromBigInteger64(409, bigInteger);
    }

    public static void halfTrace(long[] lArray, long[] lArray2) {
        long[] lArray3 = Nat.create64(13);
        Nat448.copy64(lArray, lArray2);
        for (int i2 = 1; i2 < 409; i2 += 2) {
            SecT409Field.implSquare(lArray2, lArray3);
            SecT409Field.reduce(lArray3, lArray2);
            SecT409Field.implSquare(lArray2, lArray3);
            SecT409Field.reduce(lArray3, lArray2);
            SecT409Field.addTo(lArray, lArray2);
        }
    }

    public static void invert(long[] lArray, long[] lArray2) {
        if (Nat448.isZero64(lArray)) {
            throw new IllegalStateException();
        }
        long[] lArray3 = Nat448.create64();
        long[] lArray4 = Nat448.create64();
        long[] lArray5 = Nat448.create64();
        SecT409Field.square(lArray, lArray3);
        SecT409Field.squareN(lArray3, 1, lArray4);
        SecT409Field.multiply(lArray3, lArray4, lArray3);
        SecT409Field.squareN(lArray4, 1, lArray4);
        SecT409Field.multiply(lArray3, lArray4, lArray3);
        SecT409Field.squareN(lArray3, 3, lArray4);
        SecT409Field.multiply(lArray3, lArray4, lArray3);
        SecT409Field.squareN(lArray3, 6, lArray4);
        SecT409Field.multiply(lArray3, lArray4, lArray3);
        SecT409Field.squareN(lArray3, 12, lArray4);
        SecT409Field.multiply(lArray3, lArray4, lArray5);
        SecT409Field.squareN(lArray5, 24, lArray3);
        SecT409Field.squareN(lArray3, 24, lArray4);
        SecT409Field.multiply(lArray3, lArray4, lArray3);
        SecT409Field.squareN(lArray3, 48, lArray4);
        SecT409Field.multiply(lArray3, lArray4, lArray3);
        SecT409Field.squareN(lArray3, 96, lArray4);
        SecT409Field.multiply(lArray3, lArray4, lArray3);
        SecT409Field.squareN(lArray3, 192, lArray4);
        SecT409Field.multiply(lArray3, lArray4, lArray3);
        SecT409Field.multiply(lArray3, lArray5, lArray2);
    }

    public static void multiply(long[] lArray, long[] lArray2, long[] lArray3) {
        long[] lArray4 = Nat448.createExt64();
        SecT409Field.implMultiply(lArray, lArray2, lArray4);
        SecT409Field.reduce(lArray4, lArray3);
    }

    public static void multiplyAddToExt(long[] lArray, long[] lArray2, long[] lArray3) {
        long[] lArray4 = Nat448.createExt64();
        SecT409Field.implMultiply(lArray, lArray2, lArray4);
        SecT409Field.addExt(lArray3, lArray4, lArray3);
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
        long l10 = lArray[12];
        l7 ^= l10 << 39;
        l8 ^= l10 >>> 25 ^ l10 << 62;
        l9 ^= l10 >>> 2;
        l10 = lArray[11];
        l6 ^= l10 << 39;
        l7 ^= l10 >>> 25 ^ l10 << 62;
        l8 ^= l10 >>> 2;
        l10 = lArray[10];
        l5 ^= l10 << 39;
        l6 ^= l10 >>> 25 ^ l10 << 62;
        l7 ^= l10 >>> 2;
        l10 = lArray[9];
        l4 ^= l10 << 39;
        l5 ^= l10 >>> 25 ^ l10 << 62;
        l6 ^= l10 >>> 2;
        l10 = lArray[8];
        l3 ^= l10 << 39;
        l4 ^= l10 >>> 25 ^ l10 << 62;
        l5 ^= l10 >>> 2;
        l10 = l9;
        long l11 = l8 >>> 25;
        lArray2[0] = (l2 ^= l10 << 39) ^ l11;
        lArray2[1] = (l3 ^= l10 >>> 25 ^ l10 << 62) ^ l11 << 23;
        lArray2[2] = l4 ^= l10 >>> 2;
        lArray2[3] = l5;
        lArray2[4] = l6;
        lArray2[5] = l7;
        lArray2[6] = l8 & 0x1FFFFFFL;
    }

    public static void reduce39(long[] lArray, int n2) {
        long l2 = lArray[n2 + 6];
        long l3 = l2 >>> 25;
        int n3 = n2;
        lArray[n3] = lArray[n3] ^ l3;
        int n4 = n2 + 1;
        lArray[n4] = lArray[n4] ^ l3 << 23;
        lArray[n2 + 6] = l2 & 0x1FFFFFFL;
    }

    public static void sqrt(long[] lArray, long[] lArray2) {
        long l2 = Interleave.unshuffle(lArray[0]);
        long l3 = Interleave.unshuffle(lArray[1]);
        long l4 = l2 & 0xFFFFFFFFL | l3 << 32;
        long l5 = l2 >>> 32 | l3 & 0xFFFFFFFF00000000L;
        l2 = Interleave.unshuffle(lArray[2]);
        l3 = Interleave.unshuffle(lArray[3]);
        long l6 = l2 & 0xFFFFFFFFL | l3 << 32;
        long l7 = l2 >>> 32 | l3 & 0xFFFFFFFF00000000L;
        l2 = Interleave.unshuffle(lArray[4]);
        l3 = Interleave.unshuffle(lArray[5]);
        long l8 = l2 & 0xFFFFFFFFL | l3 << 32;
        long l9 = l2 >>> 32 | l3 & 0xFFFFFFFF00000000L;
        l2 = Interleave.unshuffle(lArray[6]);
        long l10 = l2 & 0xFFFFFFFFL;
        long l11 = l2 >>> 32;
        lArray2[0] = l4 ^ l5 << 44;
        lArray2[1] = l6 ^ l7 << 44 ^ l5 >>> 20;
        lArray2[2] = l8 ^ l9 << 44 ^ l7 >>> 20;
        lArray2[3] = l10 ^ l11 << 44 ^ l9 >>> 20 ^ l5 << 13;
        lArray2[4] = l11 >>> 20 ^ l7 << 13 ^ l5 >>> 51;
        lArray2[5] = l9 << 13 ^ l7 >>> 51;
        lArray2[6] = l11 << 13 ^ l9 >>> 51;
    }

    public static void square(long[] lArray, long[] lArray2) {
        long[] lArray3 = Nat.create64(13);
        SecT409Field.implSquare(lArray, lArray3);
        SecT409Field.reduce(lArray3, lArray2);
    }

    public static void squareAddToExt(long[] lArray, long[] lArray2) {
        long[] lArray3 = Nat.create64(13);
        SecT409Field.implSquare(lArray, lArray3);
        SecT409Field.addExt(lArray2, lArray3, lArray2);
    }

    public static void squareN(long[] lArray, int n2, long[] lArray2) {
        long[] lArray3 = Nat.create64(13);
        SecT409Field.implSquare(lArray, lArray3);
        SecT409Field.reduce(lArray3, lArray2);
        while (--n2 > 0) {
            SecT409Field.implSquare(lArray2, lArray3);
            SecT409Field.reduce(lArray3, lArray2);
        }
    }

    public static int trace(long[] lArray) {
        return (int)lArray[0] & 1;
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
        long l12 = lArray[10];
        long l13 = lArray[11];
        long l14 = lArray[12];
        long l15 = lArray[13];
        lArray[0] = l2 ^ l3 << 59;
        lArray[1] = l3 >>> 5 ^ l4 << 54;
        lArray[2] = l4 >>> 10 ^ l5 << 49;
        lArray[3] = l5 >>> 15 ^ l6 << 44;
        lArray[4] = l6 >>> 20 ^ l7 << 39;
        lArray[5] = l7 >>> 25 ^ l8 << 34;
        lArray[6] = l8 >>> 30 ^ l9 << 29;
        lArray[7] = l9 >>> 35 ^ l10 << 24;
        lArray[8] = l10 >>> 40 ^ l11 << 19;
        lArray[9] = l11 >>> 45 ^ l12 << 14;
        lArray[10] = l12 >>> 50 ^ l13 << 9;
        lArray[11] = l13 >>> 55 ^ l14 << 4 ^ l15 << 63;
        lArray[12] = l15 >>> 1;
    }

    protected static void implExpand(long[] lArray, long[] lArray2) {
        long l2 = lArray[0];
        long l3 = lArray[1];
        long l4 = lArray[2];
        long l5 = lArray[3];
        long l6 = lArray[4];
        long l7 = lArray[5];
        long l8 = lArray[6];
        lArray2[0] = l2 & 0x7FFFFFFFFFFFFFFL;
        lArray2[1] = (l2 >>> 59 ^ l3 << 5) & 0x7FFFFFFFFFFFFFFL;
        lArray2[2] = (l3 >>> 54 ^ l4 << 10) & 0x7FFFFFFFFFFFFFFL;
        lArray2[3] = (l4 >>> 49 ^ l5 << 15) & 0x7FFFFFFFFFFFFFFL;
        lArray2[4] = (l5 >>> 44 ^ l6 << 20) & 0x7FFFFFFFFFFFFFFL;
        lArray2[5] = (l6 >>> 39 ^ l7 << 25) & 0x7FFFFFFFFFFFFFFL;
        lArray2[6] = l7 >>> 34 ^ l8 << 30;
    }

    protected static void implMultiply(long[] lArray, long[] lArray2, long[] lArray3) {
        long[] lArray4 = new long[7];
        long[] lArray5 = new long[7];
        SecT409Field.implExpand(lArray, lArray4);
        SecT409Field.implExpand(lArray2, lArray5);
        long[] lArray6 = new long[8];
        for (int i2 = 0; i2 < 7; ++i2) {
            SecT409Field.implMulwAcc(lArray6, lArray4[i2], lArray5[i2], lArray3, i2 << 1);
        }
        long l2 = lArray3[0];
        long l3 = lArray3[1];
        lArray3[1] = (l2 ^= lArray3[2]) ^ l3;
        lArray3[2] = (l2 ^= lArray3[4]) ^ (l3 ^= lArray3[3]);
        lArray3[3] = (l2 ^= lArray3[6]) ^ (l3 ^= lArray3[5]);
        lArray3[4] = (l2 ^= lArray3[8]) ^ (l3 ^= lArray3[7]);
        lArray3[5] = (l2 ^= lArray3[10]) ^ (l3 ^= lArray3[9]);
        lArray3[6] = (l2 ^= lArray3[12]) ^ (l3 ^= lArray3[11]);
        long l4 = l2 ^ (l3 ^= lArray3[13]);
        lArray3[7] = lArray3[0] ^ l4;
        lArray3[8] = lArray3[1] ^ l4;
        lArray3[9] = lArray3[2] ^ l4;
        lArray3[10] = lArray3[3] ^ l4;
        lArray3[11] = lArray3[4] ^ l4;
        lArray3[12] = lArray3[5] ^ l4;
        lArray3[13] = lArray3[6] ^ l4;
        SecT409Field.implMulwAcc(lArray6, lArray4[0] ^ lArray4[1], lArray5[0] ^ lArray5[1], lArray3, 1);
        SecT409Field.implMulwAcc(lArray6, lArray4[0] ^ lArray4[2], lArray5[0] ^ lArray5[2], lArray3, 2);
        SecT409Field.implMulwAcc(lArray6, lArray4[0] ^ lArray4[3], lArray5[0] ^ lArray5[3], lArray3, 3);
        SecT409Field.implMulwAcc(lArray6, lArray4[1] ^ lArray4[2], lArray5[1] ^ lArray5[2], lArray3, 3);
        SecT409Field.implMulwAcc(lArray6, lArray4[0] ^ lArray4[4], lArray5[0] ^ lArray5[4], lArray3, 4);
        SecT409Field.implMulwAcc(lArray6, lArray4[1] ^ lArray4[3], lArray5[1] ^ lArray5[3], lArray3, 4);
        SecT409Field.implMulwAcc(lArray6, lArray4[0] ^ lArray4[5], lArray5[0] ^ lArray5[5], lArray3, 5);
        SecT409Field.implMulwAcc(lArray6, lArray4[1] ^ lArray4[4], lArray5[1] ^ lArray5[4], lArray3, 5);
        SecT409Field.implMulwAcc(lArray6, lArray4[2] ^ lArray4[3], lArray5[2] ^ lArray5[3], lArray3, 5);
        SecT409Field.implMulwAcc(lArray6, lArray4[0] ^ lArray4[6], lArray5[0] ^ lArray5[6], lArray3, 6);
        SecT409Field.implMulwAcc(lArray6, lArray4[1] ^ lArray4[5], lArray5[1] ^ lArray5[5], lArray3, 6);
        SecT409Field.implMulwAcc(lArray6, lArray4[2] ^ lArray4[4], lArray5[2] ^ lArray5[4], lArray3, 6);
        SecT409Field.implMulwAcc(lArray6, lArray4[1] ^ lArray4[6], lArray5[1] ^ lArray5[6], lArray3, 7);
        SecT409Field.implMulwAcc(lArray6, lArray4[2] ^ lArray4[5], lArray5[2] ^ lArray5[5], lArray3, 7);
        SecT409Field.implMulwAcc(lArray6, lArray4[3] ^ lArray4[4], lArray5[3] ^ lArray5[4], lArray3, 7);
        SecT409Field.implMulwAcc(lArray6, lArray4[2] ^ lArray4[6], lArray5[2] ^ lArray5[6], lArray3, 8);
        SecT409Field.implMulwAcc(lArray6, lArray4[3] ^ lArray4[5], lArray5[3] ^ lArray5[5], lArray3, 8);
        SecT409Field.implMulwAcc(lArray6, lArray4[3] ^ lArray4[6], lArray5[3] ^ lArray5[6], lArray3, 9);
        SecT409Field.implMulwAcc(lArray6, lArray4[4] ^ lArray4[5], lArray5[4] ^ lArray5[5], lArray3, 9);
        SecT409Field.implMulwAcc(lArray6, lArray4[4] ^ lArray4[6], lArray5[4] ^ lArray5[6], lArray3, 10);
        SecT409Field.implMulwAcc(lArray6, lArray4[5] ^ lArray4[6], lArray5[5] ^ lArray5[6], lArray3, 11);
        SecT409Field.implCompactExt(lArray3);
    }

    protected static void implMulwAcc(long[] lArray, long l2, long l3, long[] lArray2, int n2) {
        lArray[1] = l3;
        lArray[2] = lArray[1] << 1;
        lArray[3] = lArray[2] ^ l3;
        lArray[4] = lArray[2] << 1;
        lArray[5] = lArray[4] ^ l3;
        lArray[6] = lArray[3] << 1;
        lArray[7] = lArray[6] ^ l3;
        int n3 = (int)l2;
        long l4 = 0L;
        long l5 = lArray[n3 & 7] ^ lArray[n3 >>> 3 & 7] << 3;
        int n4 = 54;
        do {
            n3 = (int)(l2 >>> n4);
            long l6 = lArray[n3 & 7] ^ lArray[n3 >>> 3 & 7] << 3;
            l5 ^= l6 << n4;
            l4 ^= l6 >>> -n4;
        } while ((n4 -= 6) > 0);
        int n5 = n2;
        lArray2[n5] = lArray2[n5] ^ l5 & 0x7FFFFFFFFFFFFFFL;
        int n6 = n2 + 1;
        lArray2[n6] = lArray2[n6] ^ (l5 >>> 59 ^ l4 << 5);
    }

    protected static void implSquare(long[] lArray, long[] lArray2) {
        Interleave.expand64To128(lArray, 0, 6, lArray2, 0);
        lArray2[12] = Interleave.expand32to64((int)lArray[6]);
    }
}

