/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.raw.Interleave;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat192;

public class SecT163Field {
    private static final long M35 = 0x7FFFFFFFFL;
    private static final long M55 = 0x7FFFFFFFFFFFFFL;
    private static final long[] ROOT_Z = new long[]{-5270498306774157648L, 5270498306774195053L, 0x492492492L};

    public static void add(long[] lArray, long[] lArray2, long[] lArray3) {
        lArray3[0] = lArray[0] ^ lArray2[0];
        lArray3[1] = lArray[1] ^ lArray2[1];
        lArray3[2] = lArray[2] ^ lArray2[2];
    }

    public static void addExt(long[] lArray, long[] lArray2, long[] lArray3) {
        lArray3[0] = lArray[0] ^ lArray2[0];
        lArray3[1] = lArray[1] ^ lArray2[1];
        lArray3[2] = lArray[2] ^ lArray2[2];
        lArray3[3] = lArray[3] ^ lArray2[3];
        lArray3[4] = lArray[4] ^ lArray2[4];
        lArray3[5] = lArray[5] ^ lArray2[5];
    }

    public static void addOne(long[] lArray, long[] lArray2) {
        lArray2[0] = lArray[0] ^ 1L;
        lArray2[1] = lArray[1];
        lArray2[2] = lArray[2];
    }

    private static void addTo(long[] lArray, long[] lArray2) {
        lArray2[0] = lArray2[0] ^ lArray[0];
        lArray2[1] = lArray2[1] ^ lArray[1];
        lArray2[2] = lArray2[2] ^ lArray[2];
    }

    public static long[] fromBigInteger(BigInteger bigInteger) {
        return Nat.fromBigInteger64(163, bigInteger);
    }

    public static void halfTrace(long[] lArray, long[] lArray2) {
        long[] lArray3 = Nat192.createExt64();
        Nat192.copy64(lArray, lArray2);
        for (int i2 = 1; i2 < 163; i2 += 2) {
            SecT163Field.implSquare(lArray2, lArray3);
            SecT163Field.reduce(lArray3, lArray2);
            SecT163Field.implSquare(lArray2, lArray3);
            SecT163Field.reduce(lArray3, lArray2);
            SecT163Field.addTo(lArray, lArray2);
        }
    }

    public static void invert(long[] lArray, long[] lArray2) {
        if (Nat192.isZero64(lArray)) {
            throw new IllegalStateException();
        }
        long[] lArray3 = Nat192.create64();
        long[] lArray4 = Nat192.create64();
        SecT163Field.square(lArray, lArray3);
        SecT163Field.squareN(lArray3, 1, lArray4);
        SecT163Field.multiply(lArray3, lArray4, lArray3);
        SecT163Field.squareN(lArray4, 1, lArray4);
        SecT163Field.multiply(lArray3, lArray4, lArray3);
        SecT163Field.squareN(lArray3, 3, lArray4);
        SecT163Field.multiply(lArray3, lArray4, lArray3);
        SecT163Field.squareN(lArray4, 3, lArray4);
        SecT163Field.multiply(lArray3, lArray4, lArray3);
        SecT163Field.squareN(lArray3, 9, lArray4);
        SecT163Field.multiply(lArray3, lArray4, lArray3);
        SecT163Field.squareN(lArray4, 9, lArray4);
        SecT163Field.multiply(lArray3, lArray4, lArray3);
        SecT163Field.squareN(lArray3, 27, lArray4);
        SecT163Field.multiply(lArray3, lArray4, lArray3);
        SecT163Field.squareN(lArray4, 27, lArray4);
        SecT163Field.multiply(lArray3, lArray4, lArray3);
        SecT163Field.squareN(lArray3, 81, lArray4);
        SecT163Field.multiply(lArray3, lArray4, lArray2);
    }

    public static void multiply(long[] lArray, long[] lArray2, long[] lArray3) {
        long[] lArray4 = new long[8];
        SecT163Field.implMultiply(lArray, lArray2, lArray4);
        SecT163Field.reduce(lArray4, lArray3);
    }

    public static void multiplyAddToExt(long[] lArray, long[] lArray2, long[] lArray3) {
        long[] lArray4 = new long[8];
        SecT163Field.implMultiply(lArray, lArray2, lArray4);
        SecT163Field.addExt(lArray3, lArray4, lArray3);
    }

    public static void reduce(long[] lArray, long[] lArray2) {
        long l2 = lArray[0];
        long l3 = lArray[1];
        long l4 = lArray[2];
        long l5 = lArray[3];
        long l6 = lArray[4];
        long l7 = lArray[5];
        l4 ^= l7 << 29 ^ l7 << 32 ^ l7 << 35 ^ l7 << 36;
        l3 ^= l6 << 29 ^ l6 << 32 ^ l6 << 35 ^ l6 << 36;
        long l8 = (l4 ^= l6 >>> 35 ^ l6 >>> 32 ^ l6 >>> 29 ^ l6 >>> 28) >>> 35;
        lArray2[0] = (l2 ^= (l5 ^= l7 >>> 35 ^ l7 >>> 32 ^ l7 >>> 29 ^ l7 >>> 28) << 29 ^ l5 << 32 ^ l5 << 35 ^ l5 << 36) ^ l8 ^ l8 << 3 ^ l8 << 6 ^ l8 << 7;
        lArray2[1] = l3 ^= l5 >>> 35 ^ l5 >>> 32 ^ l5 >>> 29 ^ l5 >>> 28;
        lArray2[2] = l4 & 0x7FFFFFFFFL;
    }

    public static void reduce29(long[] lArray, int n2) {
        long l2 = lArray[n2 + 2];
        long l3 = l2 >>> 35;
        int n3 = n2;
        lArray[n3] = lArray[n3] ^ (l3 ^ l3 << 3 ^ l3 << 6 ^ l3 << 7);
        lArray[n2 + 2] = l2 & 0x7FFFFFFFFL;
    }

    public static void sqrt(long[] lArray, long[] lArray2) {
        long[] lArray3 = Nat192.create64();
        long l2 = Interleave.unshuffle(lArray[0]);
        long l3 = Interleave.unshuffle(lArray[1]);
        long l4 = l2 & 0xFFFFFFFFL | l3 << 32;
        lArray3[0] = l2 >>> 32 | l3 & 0xFFFFFFFF00000000L;
        l2 = Interleave.unshuffle(lArray[2]);
        long l5 = l2 & 0xFFFFFFFFL;
        lArray3[1] = l2 >>> 32;
        SecT163Field.multiply(lArray3, ROOT_Z, lArray2);
        lArray2[0] = lArray2[0] ^ l4;
        lArray2[1] = lArray2[1] ^ l5;
    }

    public static void square(long[] lArray, long[] lArray2) {
        long[] lArray3 = Nat192.createExt64();
        SecT163Field.implSquare(lArray, lArray3);
        SecT163Field.reduce(lArray3, lArray2);
    }

    public static void squareAddToExt(long[] lArray, long[] lArray2) {
        long[] lArray3 = Nat192.createExt64();
        SecT163Field.implSquare(lArray, lArray3);
        SecT163Field.addExt(lArray2, lArray3, lArray2);
    }

    public static void squareN(long[] lArray, int n2, long[] lArray2) {
        long[] lArray3 = Nat192.createExt64();
        SecT163Field.implSquare(lArray, lArray3);
        SecT163Field.reduce(lArray3, lArray2);
        while (--n2 > 0) {
            SecT163Field.implSquare(lArray2, lArray3);
            SecT163Field.reduce(lArray3, lArray2);
        }
    }

    public static int trace(long[] lArray) {
        return (int)(lArray[0] ^ lArray[2] >>> 29) & 1;
    }

    protected static void implCompactExt(long[] lArray) {
        long l2 = lArray[0];
        long l3 = lArray[1];
        long l4 = lArray[2];
        long l5 = lArray[3];
        long l6 = lArray[4];
        long l7 = lArray[5];
        lArray[0] = l2 ^ l3 << 55;
        lArray[1] = l3 >>> 9 ^ l4 << 46;
        lArray[2] = l4 >>> 18 ^ l5 << 37;
        lArray[3] = l5 >>> 27 ^ l6 << 28;
        lArray[4] = l6 >>> 36 ^ l7 << 19;
        lArray[5] = l7 >>> 45;
    }

    protected static void implMultiply(long[] lArray, long[] lArray2, long[] lArray3) {
        long l2 = lArray[0];
        long l3 = lArray[1];
        long l4 = lArray[2];
        l4 = l3 >>> 46 ^ l4 << 18;
        l3 = (l2 >>> 55 ^ l3 << 9) & 0x7FFFFFFFFFFFFFL;
        l2 &= 0x7FFFFFFFFFFFFFL;
        long l5 = lArray2[0];
        long l6 = lArray2[1];
        long l7 = lArray2[2];
        l7 = l6 >>> 46 ^ l7 << 18;
        l6 = (l5 >>> 55 ^ l6 << 9) & 0x7FFFFFFFFFFFFFL;
        long[] lArray4 = lArray3;
        long[] lArray5 = new long[10];
        SecT163Field.implMulw(lArray4, l2, l5 &= 0x7FFFFFFFFFFFFFL, lArray5, 0);
        SecT163Field.implMulw(lArray4, l4, l7, lArray5, 2);
        long l8 = l2 ^ l3 ^ l4;
        long l9 = l5 ^ l6 ^ l7;
        SecT163Field.implMulw(lArray4, l8, l9, lArray5, 4);
        long l10 = l3 << 1 ^ l4 << 2;
        long l11 = l6 << 1 ^ l7 << 2;
        SecT163Field.implMulw(lArray4, l2 ^ l10, l5 ^ l11, lArray5, 6);
        SecT163Field.implMulw(lArray4, l8 ^ l10, l9 ^ l11, lArray5, 8);
        long l12 = lArray5[6] ^ lArray5[8];
        long l13 = lArray5[7] ^ lArray5[9];
        long l14 = l12 << 1 ^ lArray5[6];
        long l15 = l12 ^ l13 << 1 ^ lArray5[7];
        long l16 = l13;
        long l17 = lArray5[0];
        long l18 = lArray5[1] ^ lArray5[0] ^ lArray5[4];
        long l19 = lArray5[1] ^ lArray5[5];
        long l20 = l17 ^ l14 ^ lArray5[2] << 4 ^ lArray5[2] << 1;
        long l21 = l18 ^ l15 ^ lArray5[3] << 4 ^ lArray5[3] << 1;
        long l22 = l19 ^ l16;
        l21 ^= l20 >>> 55;
        l20 &= 0x7FFFFFFFFFFFFFL;
        l22 ^= l21 >>> 55;
        l20 = l20 >>> 1 ^ ((l21 &= 0x7FFFFFFFFFFFFFL) & 1L) << 54;
        l21 = l21 >>> 1 ^ (l22 & 1L) << 54;
        l22 >>>= 1;
        l20 ^= l20 << 1;
        l20 ^= l20 << 2;
        l20 ^= l20 << 4;
        l20 ^= l20 << 8;
        l20 ^= l20 << 16;
        l20 ^= l20 << 32;
        l21 ^= (l20 &= 0x7FFFFFFFFFFFFFL) >>> 54;
        l21 ^= l21 << 1;
        l21 ^= l21 << 2;
        l21 ^= l21 << 4;
        l21 ^= l21 << 8;
        l21 ^= l21 << 16;
        l21 ^= l21 << 32;
        l22 ^= (l21 &= 0x7FFFFFFFFFFFFFL) >>> 54;
        l22 ^= l22 << 1;
        l22 ^= l22 << 2;
        l22 ^= l22 << 4;
        l22 ^= l22 << 8;
        l22 ^= l22 << 16;
        l22 ^= l22 << 32;
        lArray3[0] = l17;
        lArray3[1] = l18 ^ l20 ^ lArray5[2];
        lArray3[2] = l19 ^ l21 ^ l20 ^ lArray5[3];
        lArray3[3] = l22 ^ l21;
        lArray3[4] = l22 ^ lArray5[2];
        lArray3[5] = lArray5[3];
        SecT163Field.implCompactExt(lArray3);
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
        long l5 = lArray[n3 & 3];
        int n4 = 47;
        do {
            n3 = (int)(l2 >>> n4);
            long l6 = lArray[n3 & 7] ^ lArray[n3 >>> 3 & 7] << 3 ^ lArray[n3 >>> 6 & 7] << 6;
            l5 ^= l6 << n4;
            l4 ^= l6 >>> -n4;
        } while ((n4 -= 9) > 0);
        lArray2[n2] = l5 & 0x7FFFFFFFFFFFFFL;
        lArray2[n2 + 1] = l5 >>> 55 ^ l4 << 9;
    }

    protected static void implSquare(long[] lArray, long[] lArray2) {
        Interleave.expand64To128(lArray, 0, 3, lArray2, 0);
    }
}

