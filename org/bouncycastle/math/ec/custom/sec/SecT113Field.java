/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.raw.Interleave;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat128;

public class SecT113Field {
    private static final long M49 = 0x1FFFFFFFFFFFFL;
    private static final long M57 = 0x1FFFFFFFFFFFFFFL;

    public static void add(long[] lArray, long[] lArray2, long[] lArray3) {
        lArray3[0] = lArray[0] ^ lArray2[0];
        lArray3[1] = lArray[1] ^ lArray2[1];
    }

    public static void addExt(long[] lArray, long[] lArray2, long[] lArray3) {
        lArray3[0] = lArray[0] ^ lArray2[0];
        lArray3[1] = lArray[1] ^ lArray2[1];
        lArray3[2] = lArray[2] ^ lArray2[2];
        lArray3[3] = lArray[3] ^ lArray2[3];
    }

    public static void addOne(long[] lArray, long[] lArray2) {
        lArray2[0] = lArray[0] ^ 1L;
        lArray2[1] = lArray[1];
    }

    private static void addTo(long[] lArray, long[] lArray2) {
        lArray2[0] = lArray2[0] ^ lArray[0];
        lArray2[1] = lArray2[1] ^ lArray[1];
    }

    public static long[] fromBigInteger(BigInteger bigInteger) {
        return Nat.fromBigInteger64(113, bigInteger);
    }

    public static void halfTrace(long[] lArray, long[] lArray2) {
        long[] lArray3 = Nat128.createExt64();
        Nat128.copy64(lArray, lArray2);
        for (int i2 = 1; i2 < 113; i2 += 2) {
            SecT113Field.implSquare(lArray2, lArray3);
            SecT113Field.reduce(lArray3, lArray2);
            SecT113Field.implSquare(lArray2, lArray3);
            SecT113Field.reduce(lArray3, lArray2);
            SecT113Field.addTo(lArray, lArray2);
        }
    }

    public static void invert(long[] lArray, long[] lArray2) {
        if (Nat128.isZero64(lArray)) {
            throw new IllegalStateException();
        }
        long[] lArray3 = Nat128.create64();
        long[] lArray4 = Nat128.create64();
        SecT113Field.square(lArray, lArray3);
        SecT113Field.multiply(lArray3, lArray, lArray3);
        SecT113Field.square(lArray3, lArray3);
        SecT113Field.multiply(lArray3, lArray, lArray3);
        SecT113Field.squareN(lArray3, 3, lArray4);
        SecT113Field.multiply(lArray4, lArray3, lArray4);
        SecT113Field.square(lArray4, lArray4);
        SecT113Field.multiply(lArray4, lArray, lArray4);
        SecT113Field.squareN(lArray4, 7, lArray3);
        SecT113Field.multiply(lArray3, lArray4, lArray3);
        SecT113Field.squareN(lArray3, 14, lArray4);
        SecT113Field.multiply(lArray4, lArray3, lArray4);
        SecT113Field.squareN(lArray4, 28, lArray3);
        SecT113Field.multiply(lArray3, lArray4, lArray3);
        SecT113Field.squareN(lArray3, 56, lArray4);
        SecT113Field.multiply(lArray4, lArray3, lArray4);
        SecT113Field.square(lArray4, lArray2);
    }

    public static void multiply(long[] lArray, long[] lArray2, long[] lArray3) {
        long[] lArray4 = new long[8];
        SecT113Field.implMultiply(lArray, lArray2, lArray4);
        SecT113Field.reduce(lArray4, lArray3);
    }

    public static void multiplyAddToExt(long[] lArray, long[] lArray2, long[] lArray3) {
        long[] lArray4 = new long[8];
        SecT113Field.implMultiply(lArray, lArray2, lArray4);
        SecT113Field.addExt(lArray3, lArray4, lArray3);
    }

    public static void reduce(long[] lArray, long[] lArray2) {
        long l2 = lArray[0];
        long l3 = lArray[1];
        long l4 = lArray[2];
        long l5 = lArray[3];
        l3 ^= l5 << 15 ^ l5 << 24;
        long l6 = (l3 ^= l4 >>> 49 ^ l4 >>> 40) >>> 49;
        lArray2[0] = (l2 ^= (l4 ^= l5 >>> 49 ^ l5 >>> 40) << 15 ^ l4 << 24) ^ l6 ^ l6 << 9;
        lArray2[1] = l3 & 0x1FFFFFFFFFFFFL;
    }

    public static void reduce15(long[] lArray, int n2) {
        long l2 = lArray[n2 + 1];
        long l3 = l2 >>> 49;
        int n3 = n2;
        lArray[n3] = lArray[n3] ^ (l3 ^ l3 << 9);
        lArray[n2 + 1] = l2 & 0x1FFFFFFFFFFFFL;
    }

    public static void sqrt(long[] lArray, long[] lArray2) {
        long l2 = Interleave.unshuffle(lArray[0]);
        long l3 = Interleave.unshuffle(lArray[1]);
        long l4 = l2 & 0xFFFFFFFFL | l3 << 32;
        long l5 = l2 >>> 32 | l3 & 0xFFFFFFFF00000000L;
        lArray2[0] = l4 ^ l5 << 57 ^ l5 << 5;
        lArray2[1] = l5 >>> 7 ^ l5 >>> 59;
    }

    public static void square(long[] lArray, long[] lArray2) {
        long[] lArray3 = Nat128.createExt64();
        SecT113Field.implSquare(lArray, lArray3);
        SecT113Field.reduce(lArray3, lArray2);
    }

    public static void squareAddToExt(long[] lArray, long[] lArray2) {
        long[] lArray3 = Nat128.createExt64();
        SecT113Field.implSquare(lArray, lArray3);
        SecT113Field.addExt(lArray2, lArray3, lArray2);
    }

    public static void squareN(long[] lArray, int n2, long[] lArray2) {
        long[] lArray3 = Nat128.createExt64();
        SecT113Field.implSquare(lArray, lArray3);
        SecT113Field.reduce(lArray3, lArray2);
        while (--n2 > 0) {
            SecT113Field.implSquare(lArray2, lArray3);
            SecT113Field.reduce(lArray3, lArray2);
        }
    }

    public static int trace(long[] lArray) {
        return (int)lArray[0] & 1;
    }

    protected static void implMultiply(long[] lArray, long[] lArray2, long[] lArray3) {
        long l2 = lArray[0];
        long l3 = lArray[1];
        l3 = (l2 >>> 57 ^ l3 << 7) & 0x1FFFFFFFFFFFFFFL;
        l2 &= 0x1FFFFFFFFFFFFFFL;
        long l4 = lArray2[0];
        long l5 = lArray2[1];
        l5 = (l4 >>> 57 ^ l5 << 7) & 0x1FFFFFFFFFFFFFFL;
        long[] lArray4 = lArray3;
        long[] lArray5 = new long[6];
        SecT113Field.implMulw(lArray4, l2, l4 &= 0x1FFFFFFFFFFFFFFL, lArray5, 0);
        SecT113Field.implMulw(lArray4, l3, l5, lArray5, 2);
        SecT113Field.implMulw(lArray4, l2 ^ l3, l4 ^ l5, lArray5, 4);
        long l6 = lArray5[1] ^ lArray5[2];
        long l7 = lArray5[0];
        long l8 = lArray5[3];
        long l9 = lArray5[4] ^ l7 ^ l6;
        long l10 = lArray5[5] ^ l8 ^ l6;
        lArray3[0] = l7 ^ l9 << 57;
        lArray3[1] = l9 >>> 7 ^ l10 << 50;
        lArray3[2] = l10 >>> 14 ^ l8 << 43;
        lArray3[3] = l8 >>> 21;
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
        Interleave.expand64To128(lArray, 0, 2, lArray2, 0);
    }
}

