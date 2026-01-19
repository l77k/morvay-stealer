/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.raw.Interleave;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat256;

public class SecT193Field {
    private static final long M01 = 1L;
    private static final long M49 = 0x1FFFFFFFFFFFFL;

    public static void add(long[] lArray, long[] lArray2, long[] lArray3) {
        lArray3[0] = lArray[0] ^ lArray2[0];
        lArray3[1] = lArray[1] ^ lArray2[1];
        lArray3[2] = lArray[2] ^ lArray2[2];
        lArray3[3] = lArray[3] ^ lArray2[3];
    }

    public static void addExt(long[] lArray, long[] lArray2, long[] lArray3) {
        lArray3[0] = lArray[0] ^ lArray2[0];
        lArray3[1] = lArray[1] ^ lArray2[1];
        lArray3[2] = lArray[2] ^ lArray2[2];
        lArray3[3] = lArray[3] ^ lArray2[3];
        lArray3[4] = lArray[4] ^ lArray2[4];
        lArray3[5] = lArray[5] ^ lArray2[5];
        lArray3[6] = lArray[6] ^ lArray2[6];
    }

    public static void addOne(long[] lArray, long[] lArray2) {
        lArray2[0] = lArray[0] ^ 1L;
        lArray2[1] = lArray[1];
        lArray2[2] = lArray[2];
        lArray2[3] = lArray[3];
    }

    private static void addTo(long[] lArray, long[] lArray2) {
        lArray2[0] = lArray2[0] ^ lArray[0];
        lArray2[1] = lArray2[1] ^ lArray[1];
        lArray2[2] = lArray2[2] ^ lArray[2];
        lArray2[3] = lArray2[3] ^ lArray[3];
    }

    public static long[] fromBigInteger(BigInteger bigInteger) {
        return Nat.fromBigInteger64(193, bigInteger);
    }

    public static void halfTrace(long[] lArray, long[] lArray2) {
        long[] lArray3 = Nat256.createExt64();
        Nat256.copy64(lArray, lArray2);
        for (int i2 = 1; i2 < 193; i2 += 2) {
            SecT193Field.implSquare(lArray2, lArray3);
            SecT193Field.reduce(lArray3, lArray2);
            SecT193Field.implSquare(lArray2, lArray3);
            SecT193Field.reduce(lArray3, lArray2);
            SecT193Field.addTo(lArray, lArray2);
        }
    }

    public static void invert(long[] lArray, long[] lArray2) {
        if (Nat256.isZero64(lArray)) {
            throw new IllegalStateException();
        }
        long[] lArray3 = Nat256.create64();
        long[] lArray4 = Nat256.create64();
        SecT193Field.square(lArray, lArray3);
        SecT193Field.squareN(lArray3, 1, lArray4);
        SecT193Field.multiply(lArray3, lArray4, lArray3);
        SecT193Field.squareN(lArray4, 1, lArray4);
        SecT193Field.multiply(lArray3, lArray4, lArray3);
        SecT193Field.squareN(lArray3, 3, lArray4);
        SecT193Field.multiply(lArray3, lArray4, lArray3);
        SecT193Field.squareN(lArray3, 6, lArray4);
        SecT193Field.multiply(lArray3, lArray4, lArray3);
        SecT193Field.squareN(lArray3, 12, lArray4);
        SecT193Field.multiply(lArray3, lArray4, lArray3);
        SecT193Field.squareN(lArray3, 24, lArray4);
        SecT193Field.multiply(lArray3, lArray4, lArray3);
        SecT193Field.squareN(lArray3, 48, lArray4);
        SecT193Field.multiply(lArray3, lArray4, lArray3);
        SecT193Field.squareN(lArray3, 96, lArray4);
        SecT193Field.multiply(lArray3, lArray4, lArray2);
    }

    public static void multiply(long[] lArray, long[] lArray2, long[] lArray3) {
        long[] lArray4 = Nat256.createExt64();
        SecT193Field.implMultiply(lArray, lArray2, lArray4);
        SecT193Field.reduce(lArray4, lArray3);
    }

    public static void multiplyAddToExt(long[] lArray, long[] lArray2, long[] lArray3) {
        long[] lArray4 = Nat256.createExt64();
        SecT193Field.implMultiply(lArray, lArray2, lArray4);
        SecT193Field.addExt(lArray3, lArray4, lArray3);
    }

    public static void reduce(long[] lArray, long[] lArray2) {
        long l2 = lArray[0];
        long l3 = lArray[1];
        long l4 = lArray[2];
        long l5 = lArray[3];
        long l6 = lArray[4];
        long l7 = lArray[5];
        long l8 = lArray[6];
        l4 ^= l8 << 63;
        l5 ^= l8 >>> 1 ^ l8 << 14;
        l3 ^= l7 << 63;
        l4 ^= l7 >>> 1 ^ l7 << 14;
        long l9 = (l5 ^= l7 >>> 50) >>> 1;
        lArray2[0] = (l2 ^= (l6 ^= l8 >>> 50) << 63) ^ l9 ^ l9 << 15;
        lArray2[1] = (l3 ^= l6 >>> 1 ^ l6 << 14) ^ l9 >>> 49;
        lArray2[2] = l4 ^= l6 >>> 50;
        lArray2[3] = l5 & 1L;
    }

    public static void reduce63(long[] lArray, int n2) {
        long l2 = lArray[n2 + 3];
        long l3 = l2 >>> 1;
        int n3 = n2;
        lArray[n3] = lArray[n3] ^ (l3 ^ l3 << 15);
        int n4 = n2 + 1;
        lArray[n4] = lArray[n4] ^ l3 >>> 49;
        lArray[n2 + 3] = l2 & 1L;
    }

    public static void sqrt(long[] lArray, long[] lArray2) {
        long l2 = Interleave.unshuffle(lArray[0]);
        long l3 = Interleave.unshuffle(lArray[1]);
        long l4 = l2 & 0xFFFFFFFFL | l3 << 32;
        long l5 = l2 >>> 32 | l3 & 0xFFFFFFFF00000000L;
        l2 = Interleave.unshuffle(lArray[2]);
        long l6 = l2 & 0xFFFFFFFFL ^ lArray[3] << 32;
        long l7 = l2 >>> 32;
        lArray2[0] = l4 ^ l5 << 8;
        lArray2[1] = l6 ^ l7 << 8 ^ l5 >>> 56 ^ l5 << 33;
        lArray2[2] = l7 >>> 56 ^ l7 << 33 ^ l5 >>> 31;
        lArray2[3] = l7 >>> 31;
    }

    public static void square(long[] lArray, long[] lArray2) {
        long[] lArray3 = Nat256.createExt64();
        SecT193Field.implSquare(lArray, lArray3);
        SecT193Field.reduce(lArray3, lArray2);
    }

    public static void squareAddToExt(long[] lArray, long[] lArray2) {
        long[] lArray3 = Nat256.createExt64();
        SecT193Field.implSquare(lArray, lArray3);
        SecT193Field.addExt(lArray2, lArray3, lArray2);
    }

    public static void squareN(long[] lArray, int n2, long[] lArray2) {
        long[] lArray3 = Nat256.createExt64();
        SecT193Field.implSquare(lArray, lArray3);
        SecT193Field.reduce(lArray3, lArray2);
        while (--n2 > 0) {
            SecT193Field.implSquare(lArray2, lArray3);
            SecT193Field.reduce(lArray3, lArray2);
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
        lArray[0] = l2 ^ l3 << 49;
        lArray[1] = l3 >>> 15 ^ l4 << 34;
        lArray[2] = l4 >>> 30 ^ l5 << 19;
        lArray[3] = l5 >>> 45 ^ l6 << 4 ^ l7 << 53;
        lArray[4] = l6 >>> 60 ^ l8 << 38 ^ l7 >>> 11;
        lArray[5] = l8 >>> 26 ^ l9 << 23;
        lArray[6] = l9 >>> 41;
        lArray[7] = 0L;
    }

    protected static void implExpand(long[] lArray, long[] lArray2) {
        long l2 = lArray[0];
        long l3 = lArray[1];
        long l4 = lArray[2];
        long l5 = lArray[3];
        lArray2[0] = l2 & 0x1FFFFFFFFFFFFL;
        lArray2[1] = (l2 >>> 49 ^ l3 << 15) & 0x1FFFFFFFFFFFFL;
        lArray2[2] = (l3 >>> 34 ^ l4 << 30) & 0x1FFFFFFFFFFFFL;
        lArray2[3] = l4 >>> 19 ^ l5 << 45;
    }

    protected static void implMultiply(long[] lArray, long[] lArray2, long[] lArray3) {
        int n2;
        long[] lArray4 = new long[4];
        long[] lArray5 = new long[4];
        SecT193Field.implExpand(lArray, lArray4);
        SecT193Field.implExpand(lArray2, lArray5);
        long[] lArray6 = new long[8];
        SecT193Field.implMulwAcc(lArray6, lArray4[0], lArray5[0], lArray3, 0);
        SecT193Field.implMulwAcc(lArray6, lArray4[1], lArray5[1], lArray3, 1);
        SecT193Field.implMulwAcc(lArray6, lArray4[2], lArray5[2], lArray3, 2);
        SecT193Field.implMulwAcc(lArray6, lArray4[3], lArray5[3], lArray3, 3);
        for (n2 = 5; n2 > 0; --n2) {
            int n3 = n2;
            lArray3[n3] = lArray3[n3] ^ lArray3[n2 - 1];
        }
        SecT193Field.implMulwAcc(lArray6, lArray4[0] ^ lArray4[1], lArray5[0] ^ lArray5[1], lArray3, 1);
        SecT193Field.implMulwAcc(lArray6, lArray4[2] ^ lArray4[3], lArray5[2] ^ lArray5[3], lArray3, 3);
        for (n2 = 7; n2 > 1; --n2) {
            int n4 = n2;
            lArray3[n4] = lArray3[n4] ^ lArray3[n2 - 2];
        }
        long l2 = lArray4[0] ^ lArray4[2];
        long l3 = lArray4[1] ^ lArray4[3];
        long l4 = lArray5[0] ^ lArray5[2];
        long l5 = lArray5[1] ^ lArray5[3];
        SecT193Field.implMulwAcc(lArray6, l2 ^ l3, l4 ^ l5, lArray3, 3);
        long[] lArray7 = new long[3];
        SecT193Field.implMulwAcc(lArray6, l2, l4, lArray7, 0);
        SecT193Field.implMulwAcc(lArray6, l3, l5, lArray7, 1);
        long l6 = lArray7[0];
        long l7 = lArray7[1];
        long l8 = lArray7[2];
        lArray3[2] = lArray3[2] ^ l6;
        lArray3[3] = lArray3[3] ^ (l6 ^ l7);
        lArray3[4] = lArray3[4] ^ (l8 ^ l7);
        lArray3[5] = lArray3[5] ^ l8;
        SecT193Field.implCompactExt(lArray3);
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
        int n4 = 36;
        do {
            n3 = (int)(l2 >>> n4);
            long l6 = lArray[n3 & 7] ^ lArray[n3 >>> 3 & 7] << 3 ^ lArray[n3 >>> 6 & 7] << 6 ^ lArray[n3 >>> 9 & 7] << 9 ^ lArray[n3 >>> 12 & 7] << 12;
            l5 ^= l6 << n4;
            l4 ^= l6 >>> -n4;
        } while ((n4 -= 15) > 0);
        int n5 = n2;
        lArray2[n5] = lArray2[n5] ^ l5 & 0x1FFFFFFFFFFFFL;
        int n6 = n2 + 1;
        lArray2[n6] = lArray2[n6] ^ (l5 >>> 49 ^ l4 << 15);
    }

    protected static void implSquare(long[] lArray, long[] lArray2) {
        Interleave.expand64To128(lArray, 0, 3, lArray2, 0);
        lArray2[6] = lArray[3] & 1L;
    }
}

