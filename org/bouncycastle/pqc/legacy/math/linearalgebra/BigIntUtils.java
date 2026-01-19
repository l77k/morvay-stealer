/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.linearalgebra;

import java.math.BigInteger;

public final class BigIntUtils {
    private BigIntUtils() {
    }

    public static boolean equals(BigInteger[] bigIntegerArray, BigInteger[] bigIntegerArray2) {
        int n2 = 0;
        if (bigIntegerArray.length != bigIntegerArray2.length) {
            return false;
        }
        for (int i2 = 0; i2 < bigIntegerArray.length; ++i2) {
            n2 |= bigIntegerArray[i2].compareTo(bigIntegerArray2[i2]);
        }
        return n2 == 0;
    }

    public static void fill(BigInteger[] bigIntegerArray, BigInteger bigInteger) {
        for (int i2 = bigIntegerArray.length - 1; i2 >= 0; --i2) {
            bigIntegerArray[i2] = bigInteger;
        }
    }

    public static BigInteger[] subArray(BigInteger[] bigIntegerArray, int n2, int n3) {
        BigInteger[] bigIntegerArray2 = new BigInteger[n3 - n2];
        System.arraycopy(bigIntegerArray, n2, bigIntegerArray2, 0, n3 - n2);
        return bigIntegerArray2;
    }

    public static int[] toIntArray(BigInteger[] bigIntegerArray) {
        int[] nArray = new int[bigIntegerArray.length];
        for (int i2 = 0; i2 < bigIntegerArray.length; ++i2) {
            nArray[i2] = bigIntegerArray[i2].intValue();
        }
        return nArray;
    }

    public static int[] toIntArrayModQ(int n2, BigInteger[] bigIntegerArray) {
        BigInteger bigInteger = BigInteger.valueOf(n2);
        int[] nArray = new int[bigIntegerArray.length];
        for (int i2 = 0; i2 < bigIntegerArray.length; ++i2) {
            nArray[i2] = bigIntegerArray[i2].mod(bigInteger).intValue();
        }
        return nArray;
    }

    public static byte[] toMinimalByteArray(BigInteger bigInteger) {
        byte[] byArray = bigInteger.toByteArray();
        if (byArray.length == 1 || (bigInteger.bitLength() & 7) != 0) {
            return byArray;
        }
        byte[] byArray2 = new byte[bigInteger.bitLength() >> 3];
        System.arraycopy(byArray, 1, byArray2, 0, byArray2.length);
        return byArray2;
    }
}

