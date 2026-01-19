/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.linearalgebra;

import org.bouncycastle.pqc.legacy.math.linearalgebra.IntegerFunctions;

public final class BigEndianConversions {
    private BigEndianConversions() {
    }

    public static byte[] I2OSP(int n2) {
        byte[] byArray = new byte[]{(byte)(n2 >>> 24), (byte)(n2 >>> 16), (byte)(n2 >>> 8), (byte)n2};
        return byArray;
    }

    public static byte[] I2OSP(int n2, int n3) throws ArithmeticException {
        if (n2 < 0) {
            return null;
        }
        int n4 = IntegerFunctions.ceilLog256(n2);
        if (n4 > n3) {
            throw new ArithmeticException("Cannot encode given integer into specified number of octets.");
        }
        byte[] byArray = new byte[n3];
        for (int i2 = n3 - 1; i2 >= n3 - n4; --i2) {
            byArray[i2] = (byte)(n2 >>> 8 * (n3 - 1 - i2));
        }
        return byArray;
    }

    public static void I2OSP(int n2, byte[] byArray, int n3) {
        byArray[n3++] = (byte)(n2 >>> 24);
        byArray[n3++] = (byte)(n2 >>> 16);
        byArray[n3++] = (byte)(n2 >>> 8);
        byArray[n3] = (byte)n2;
    }

    public static byte[] I2OSP(long l2) {
        byte[] byArray = new byte[]{(byte)(l2 >>> 56), (byte)(l2 >>> 48), (byte)(l2 >>> 40), (byte)(l2 >>> 32), (byte)(l2 >>> 24), (byte)(l2 >>> 16), (byte)(l2 >>> 8), (byte)l2};
        return byArray;
    }

    public static void I2OSP(long l2, byte[] byArray, int n2) {
        byArray[n2++] = (byte)(l2 >>> 56);
        byArray[n2++] = (byte)(l2 >>> 48);
        byArray[n2++] = (byte)(l2 >>> 40);
        byArray[n2++] = (byte)(l2 >>> 32);
        byArray[n2++] = (byte)(l2 >>> 24);
        byArray[n2++] = (byte)(l2 >>> 16);
        byArray[n2++] = (byte)(l2 >>> 8);
        byArray[n2] = (byte)l2;
    }

    public static void I2OSP(int n2, byte[] byArray, int n3, int n4) {
        for (int i2 = n4 - 1; i2 >= 0; --i2) {
            byArray[n3 + i2] = (byte)(n2 >>> 8 * (n4 - 1 - i2));
        }
    }

    public static int OS2IP(byte[] byArray) {
        if (byArray.length > 4) {
            throw new ArithmeticException("invalid input length");
        }
        if (byArray.length == 0) {
            return 0;
        }
        int n2 = 0;
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            n2 |= (byArray[i2] & 0xFF) << 8 * (byArray.length - 1 - i2);
        }
        return n2;
    }

    public static int OS2IP(byte[] byArray, int n2) {
        int n3 = (byArray[n2++] & 0xFF) << 24;
        n3 |= (byArray[n2++] & 0xFF) << 16;
        n3 |= (byArray[n2++] & 0xFF) << 8;
        return n3 |= byArray[n2] & 0xFF;
    }

    public static int OS2IP(byte[] byArray, int n2, int n3) {
        if (byArray.length == 0 || byArray.length < n2 + n3 - 1) {
            return 0;
        }
        int n4 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            n4 |= (byArray[n2 + i2] & 0xFF) << 8 * (n3 - i2 - 1);
        }
        return n4;
    }

    public static long OS2LIP(byte[] byArray, int n2) {
        long l2 = ((long)byArray[n2++] & 0xFFL) << 56;
        l2 |= ((long)byArray[n2++] & 0xFFL) << 48;
        l2 |= ((long)byArray[n2++] & 0xFFL) << 40;
        l2 |= ((long)byArray[n2++] & 0xFFL) << 32;
        l2 |= ((long)byArray[n2++] & 0xFFL) << 24;
        l2 |= (long)((byArray[n2++] & 0xFF) << 16);
        l2 |= (long)((byArray[n2++] & 0xFF) << 8);
        return l2 |= (long)(byArray[n2] & 0xFF);
    }

    public static byte[] toByteArray(int[] nArray) {
        byte[] byArray = new byte[nArray.length << 2];
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            BigEndianConversions.I2OSP(nArray[i2], byArray, i2 << 2);
        }
        return byArray;
    }

    public static byte[] toByteArray(int[] nArray, int n2) {
        int n3 = nArray.length;
        byte[] byArray = new byte[n2];
        int n4 = 0;
        int n5 = 0;
        while (n5 <= n3 - 2) {
            BigEndianConversions.I2OSP(nArray[n5], byArray, n4);
            ++n5;
            n4 += 4;
        }
        BigEndianConversions.I2OSP(nArray[n3 - 1], byArray, n4, n2 - n4);
        return byArray;
    }

    public static int[] toIntArray(byte[] byArray) {
        int n2 = (byArray.length + 3) / 4;
        int n3 = byArray.length & 3;
        int[] nArray = new int[n2];
        int n4 = 0;
        int n5 = 0;
        while (n5 <= n2 - 2) {
            nArray[n5] = BigEndianConversions.OS2IP(byArray, n4);
            ++n5;
            n4 += 4;
        }
        nArray[n2 - 1] = n3 != 0 ? BigEndianConversions.OS2IP(byArray, n4, n3) : BigEndianConversions.OS2IP(byArray, n4);
        return nArray;
    }
}

