/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.raw.Interleave;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat256;

public class SecT239Field {
    private static final long M47 = 0x7FFFFFFFFFFFL;
    private static final long M60 = 0xFFFFFFFFFFFFFFFL;

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
        lArray3[7] = lArray[7] ^ lArray2[7];
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
        return Nat.fromBigInteger64(239, bigInteger);
    }

    public static void halfTrace(long[] lArray, long[] lArray2) {
        long[] lArray3 = Nat256.createExt64();
        Nat256.copy64(lArray, lArray2);
        for (int i2 = 1; i2 < 239; i2 += 2) {
            SecT239Field.implSquare(lArray2, lArray3);
            SecT239Field.reduce(lArray3, lArray2);
            SecT239Field.implSquare(lArray2, lArray3);
            SecT239Field.reduce(lArray3, lArray2);
            SecT239Field.addTo(lArray, lArray2);
        }
    }

    public static void invert(long[] lArray, long[] lArray2) {
        if (Nat256.isZero64(lArray)) {
            throw new IllegalStateException();
        }
        long[] lArray3 = Nat256.create64();
        long[] lArray4 = Nat256.create64();
        SecT239Field.square(lArray, lArray3);
        SecT239Field.multiply(lArray3, lArray, lArray3);
        SecT239Field.square(lArray3, lArray3);
        SecT239Field.multiply(lArray3, lArray, lArray3);
        SecT239Field.squareN(lArray3, 3, lArray4);
        SecT239Field.multiply(lArray4, lArray3, lArray4);
        SecT239Field.square(lArray4, lArray4);
        SecT239Field.multiply(lArray4, lArray, lArray4);
        SecT239Field.squareN(lArray4, 7, lArray3);
        SecT239Field.multiply(lArray3, lArray4, lArray3);
        SecT239Field.squareN(lArray3, 14, lArray4);
        SecT239Field.multiply(lArray4, lArray3, lArray4);
        SecT239Field.square(lArray4, lArray4);
        SecT239Field.multiply(lArray4, lArray, lArray4);
        SecT239Field.squareN(lArray4, 29, lArray3);
        SecT239Field.multiply(lArray3, lArray4, lArray3);
        SecT239Field.square(lArray3, lArray3);
        SecT239Field.multiply(lArray3, lArray, lArray3);
        SecT239Field.squareN(lArray3, 59, lArray4);
        SecT239Field.multiply(lArray4, lArray3, lArray4);
        SecT239Field.square(lArray4, lArray4);
        SecT239Field.multiply(lArray4, lArray, lArray4);
        SecT239Field.squareN(lArray4, 119, lArray3);
        SecT239Field.multiply(lArray3, lArray4, lArray3);
        SecT239Field.square(lArray3, lArray2);
    }

    public static void multiply(long[] lArray, long[] lArray2, long[] lArray3) {
        long[] lArray4 = Nat256.createExt64();
        SecT239Field.implMultiply(lArray, lArray2, lArray4);
        SecT239Field.reduce(lArray4, lArray3);
    }

    public static void multiplyAddToExt(long[] lArray, long[] lArray2, long[] lArray3) {
        long[] lArray4 = Nat256.createExt64();
        SecT239Field.implMultiply(lArray, lArray2, lArray4);
        SecT239Field.addExt(lArray3, lArray4, lArray3);
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
        l5 ^= l9 << 17;
        l6 ^= l9 >>> 47;
        l7 ^= l9 << 47;
        l4 ^= (l8 ^= l9 >>> 17) << 17;
        l5 ^= l8 >>> 47;
        l6 ^= l8 << 47;
        l3 ^= (l7 ^= l8 >>> 17) << 17;
        l4 ^= l7 >>> 47;
        l5 ^= l7 << 47;
        long l10 = (l5 ^= l6 >>> 17) >>> 47;
        lArray2[0] = (l2 ^= (l6 ^= l7 >>> 17) << 17) ^ l10;
        lArray2[1] = l3 ^= l6 >>> 47;
        lArray2[2] = (l4 ^= l6 << 47) ^ l10 << 30;
        lArray2[3] = l5 & 0x7FFFFFFFFFFFL;
    }

    public static void reduce17(long[] lArray, int n2) {
        long l2 = lArray[n2 + 3];
        long l3 = l2 >>> 47;
        int n3 = n2;
        lArray[n3] = lArray[n3] ^ l3;
        int n4 = n2 + 2;
        lArray[n4] = lArray[n4] ^ l3 << 30;
        lArray[n2 + 3] = l2 & 0x7FFFFFFFFFFFL;
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
        long l8 = l7 >>> 49;
        long l9 = l5 >>> 49 | l7 << 15;
        l7 ^= l5 << 15;
        long[] lArray3 = Nat256.createExt64();
        int[] nArray = new int[]{39, 120};
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            int n2 = nArray[i2] >>> 6;
            int n3 = nArray[i2] & 0x3F;
            int n4 = n2;
            lArray3[n4] = lArray3[n4] ^ l5 << n3;
            int n5 = n2 + 1;
            lArray3[n5] = lArray3[n5] ^ (l7 << n3 | l5 >>> -n3);
            int n6 = n2 + 2;
            lArray3[n6] = lArray3[n6] ^ (l9 << n3 | l7 >>> -n3);
            int n7 = n2 + 3;
            lArray3[n7] = lArray3[n7] ^ (l8 << n3 | l9 >>> -n3);
            int n8 = n2 + 4;
            lArray3[n8] = lArray3[n8] ^ l8 >>> -n3;
        }
        SecT239Field.reduce(lArray3, lArray2);
        lArray2[0] = lArray2[0] ^ l4;
        lArray2[1] = lArray2[1] ^ l6;
    }

    public static void square(long[] lArray, long[] lArray2) {
        long[] lArray3 = Nat256.createExt64();
        SecT239Field.implSquare(lArray, lArray3);
        SecT239Field.reduce(lArray3, lArray2);
    }

    public static void squareAddToExt(long[] lArray, long[] lArray2) {
        long[] lArray3 = Nat256.createExt64();
        SecT239Field.implSquare(lArray, lArray3);
        SecT239Field.addExt(lArray2, lArray3, lArray2);
    }

    public static void squareN(long[] lArray, int n2, long[] lArray2) {
        long[] lArray3 = Nat256.createExt64();
        SecT239Field.implSquare(lArray, lArray3);
        SecT239Field.reduce(lArray3, lArray2);
        while (--n2 > 0) {
            SecT239Field.implSquare(lArray2, lArray3);
            SecT239Field.reduce(lArray3, lArray2);
        }
    }

    public static int trace(long[] lArray) {
        return (int)(lArray[0] ^ lArray[1] >>> 17 ^ lArray[2] >>> 34) & 1;
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
        lArray[0] = l2 ^ l3 << 60;
        lArray[1] = l3 >>> 4 ^ l4 << 56;
        lArray[2] = l4 >>> 8 ^ l5 << 52;
        lArray[3] = l5 >>> 12 ^ l6 << 48;
        lArray[4] = l6 >>> 16 ^ l7 << 44;
        lArray[5] = l7 >>> 20 ^ l8 << 40;
        lArray[6] = l8 >>> 24 ^ l9 << 36;
        lArray[7] = l9 >>> 28;
    }

    protected static void implExpand(long[] lArray, long[] lArray2) {
        long l2 = lArray[0];
        long l3 = lArray[1];
        long l4 = lArray[2];
        long l5 = lArray[3];
        lArray2[0] = l2 & 0xFFFFFFFFFFFFFFFL;
        lArray2[1] = (l2 >>> 60 ^ l3 << 4) & 0xFFFFFFFFFFFFFFFL;
        lArray2[2] = (l3 >>> 56 ^ l4 << 8) & 0xFFFFFFFFFFFFFFFL;
        lArray2[3] = l4 >>> 52 ^ l5 << 12;
    }

    protected static void implMultiply(long[] lArray, long[] lArray2, long[] lArray3) {
        int n2;
        long[] lArray4 = new long[4];
        long[] lArray5 = new long[4];
        SecT239Field.implExpand(lArray, lArray4);
        SecT239Field.implExpand(lArray2, lArray5);
        long[] lArray6 = new long[8];
        SecT239Field.implMulwAcc(lArray6, lArray4[0], lArray5[0], lArray3, 0);
        SecT239Field.implMulwAcc(lArray6, lArray4[1], lArray5[1], lArray3, 1);
        SecT239Field.implMulwAcc(lArray6, lArray4[2], lArray5[2], lArray3, 2);
        SecT239Field.implMulwAcc(lArray6, lArray4[3], lArray5[3], lArray3, 3);
        for (n2 = 5; n2 > 0; --n2) {
            int n3 = n2;
            lArray3[n3] = lArray3[n3] ^ lArray3[n2 - 1];
        }
        SecT239Field.implMulwAcc(lArray6, lArray4[0] ^ lArray4[1], lArray5[0] ^ lArray5[1], lArray3, 1);
        SecT239Field.implMulwAcc(lArray6, lArray4[2] ^ lArray4[3], lArray5[2] ^ lArray5[3], lArray3, 3);
        for (n2 = 7; n2 > 1; --n2) {
            int n4 = n2;
            lArray3[n4] = lArray3[n4] ^ lArray3[n2 - 2];
        }
        long l2 = lArray4[0] ^ lArray4[2];
        long l3 = lArray4[1] ^ lArray4[3];
        long l4 = lArray5[0] ^ lArray5[2];
        long l5 = lArray5[1] ^ lArray5[3];
        SecT239Field.implMulwAcc(lArray6, l2 ^ l3, l4 ^ l5, lArray3, 3);
        long[] lArray7 = new long[3];
        SecT239Field.implMulwAcc(lArray6, l2, l4, lArray7, 0);
        SecT239Field.implMulwAcc(lArray6, l3, l5, lArray7, 1);
        long l6 = lArray7[0];
        long l7 = lArray7[1];
        long l8 = lArray7[2];
        lArray3[2] = lArray3[2] ^ l6;
        lArray3[3] = lArray3[3] ^ (l6 ^ l7);
        lArray3[4] = lArray3[4] ^ (l8 ^ l7);
        lArray3[5] = lArray3[5] ^ l8;
        SecT239Field.implCompactExt(lArray3);
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
        lArray2[n5] = lArray2[n5] ^ l5 & 0xFFFFFFFFFFFFFFFL;
        int n6 = n2 + 1;
        lArray2[n6] = lArray2[n6] ^ (l5 >>> 60 ^ (l4 ^= (l2 & 0x820820820820820L & l3 << 4 >> 63) >>> 5) << 4);
    }

    protected static void implSquare(long[] lArray, long[] lArray2) {
        Interleave.expand64To128(lArray, 0, 4, lArray2, 0);
    }
}

