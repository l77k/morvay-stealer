/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.raw;

import java.math.BigInteger;
import org.bouncycastle.util.Pack;

public abstract class Nat576 {
    public static void copy64(long[] lArray, long[] lArray2) {
        lArray2[0] = lArray[0];
        lArray2[1] = lArray[1];
        lArray2[2] = lArray[2];
        lArray2[3] = lArray[3];
        lArray2[4] = lArray[4];
        lArray2[5] = lArray[5];
        lArray2[6] = lArray[6];
        lArray2[7] = lArray[7];
        lArray2[8] = lArray[8];
    }

    public static void copy64(long[] lArray, int n2, long[] lArray2, int n3) {
        lArray2[n3 + 0] = lArray[n2 + 0];
        lArray2[n3 + 1] = lArray[n2 + 1];
        lArray2[n3 + 2] = lArray[n2 + 2];
        lArray2[n3 + 3] = lArray[n2 + 3];
        lArray2[n3 + 4] = lArray[n2 + 4];
        lArray2[n3 + 5] = lArray[n2 + 5];
        lArray2[n3 + 6] = lArray[n2 + 6];
        lArray2[n3 + 7] = lArray[n2 + 7];
        lArray2[n3 + 8] = lArray[n2 + 8];
    }

    public static long[] create64() {
        return new long[9];
    }

    public static long[] createExt64() {
        return new long[18];
    }

    public static boolean eq64(long[] lArray, long[] lArray2) {
        for (int i2 = 8; i2 >= 0; --i2) {
            if (lArray[i2] == lArray2[i2]) continue;
            return false;
        }
        return true;
    }

    public static long[] fromBigInteger64(BigInteger bigInteger) {
        if (bigInteger.signum() < 0 || bigInteger.bitLength() > 576) {
            throw new IllegalArgumentException();
        }
        long[] lArray = Nat576.create64();
        for (int i2 = 0; i2 < 9; ++i2) {
            lArray[i2] = bigInteger.longValue();
            bigInteger = bigInteger.shiftRight(64);
        }
        return lArray;
    }

    public static boolean isOne64(long[] lArray) {
        if (lArray[0] != 1L) {
            return false;
        }
        for (int i2 = 1; i2 < 9; ++i2) {
            if (lArray[i2] == 0L) continue;
            return false;
        }
        return true;
    }

    public static boolean isZero64(long[] lArray) {
        for (int i2 = 0; i2 < 9; ++i2) {
            if (lArray[i2] == 0L) continue;
            return false;
        }
        return true;
    }

    public static BigInteger toBigInteger64(long[] lArray) {
        byte[] byArray = new byte[72];
        for (int i2 = 0; i2 < 9; ++i2) {
            long l2 = lArray[i2];
            if (l2 == 0L) continue;
            Pack.longToBigEndian(l2, byArray, 8 - i2 << 3);
        }
        return new BigInteger(1, byArray);
    }
}

