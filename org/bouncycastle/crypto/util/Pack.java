/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.util;

public abstract class Pack {
    public static int bigEndianToInt(byte[] byArray, int n2) {
        int n3 = byArray[n2] << 24;
        n3 |= (byArray[++n2] & 0xFF) << 16;
        n3 |= (byArray[++n2] & 0xFF) << 8;
        return n3 |= byArray[++n2] & 0xFF;
    }

    public static void bigEndianToInt(byte[] byArray, int n2, int[] nArray) {
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            nArray[i2] = Pack.bigEndianToInt(byArray, n2);
            n2 += 4;
        }
    }

    public static byte[] intToBigEndian(int n2) {
        byte[] byArray = new byte[4];
        Pack.intToBigEndian(n2, byArray, 0);
        return byArray;
    }

    public static void intToBigEndian(int n2, byte[] byArray, int n3) {
        byArray[n3] = (byte)(n2 >>> 24);
        byArray[++n3] = (byte)(n2 >>> 16);
        byArray[++n3] = (byte)(n2 >>> 8);
        byArray[++n3] = (byte)n2;
    }

    public static byte[] intToBigEndian(int[] nArray) {
        byte[] byArray = new byte[4 * nArray.length];
        Pack.intToBigEndian(nArray, byArray, 0);
        return byArray;
    }

    public static void intToBigEndian(int[] nArray, byte[] byArray, int n2) {
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            Pack.intToBigEndian(nArray[i2], byArray, n2);
            n2 += 4;
        }
    }

    public static long bigEndianToLong(byte[] byArray, int n2) {
        int n3 = Pack.bigEndianToInt(byArray, n2);
        int n4 = Pack.bigEndianToInt(byArray, n2 + 4);
        return ((long)n3 & 0xFFFFFFFFL) << 32 | (long)n4 & 0xFFFFFFFFL;
    }

    public static void bigEndianToLong(byte[] byArray, int n2, long[] lArray) {
        for (int i2 = 0; i2 < lArray.length; ++i2) {
            lArray[i2] = Pack.bigEndianToLong(byArray, n2);
            n2 += 8;
        }
    }

    public static byte[] longToBigEndian(long l2) {
        byte[] byArray = new byte[8];
        Pack.longToBigEndian(l2, byArray, 0);
        return byArray;
    }

    public static void longToBigEndian(long l2, byte[] byArray, int n2) {
        Pack.intToBigEndian((int)(l2 >>> 32), byArray, n2);
        Pack.intToBigEndian((int)(l2 & 0xFFFFFFFFL), byArray, n2 + 4);
    }

    public static byte[] longToBigEndian(long[] lArray) {
        byte[] byArray = new byte[8 * lArray.length];
        Pack.longToBigEndian(lArray, byArray, 0);
        return byArray;
    }

    public static void longToBigEndian(long[] lArray, byte[] byArray, int n2) {
        for (int i2 = 0; i2 < lArray.length; ++i2) {
            Pack.longToBigEndian(lArray[i2], byArray, n2);
            n2 += 8;
        }
    }

    public static int littleEndianToInt(byte[] byArray, int n2) {
        int n3 = byArray[n2] & 0xFF;
        n3 |= (byArray[++n2] & 0xFF) << 8;
        n3 |= (byArray[++n2] & 0xFF) << 16;
        return n3 |= byArray[++n2] << 24;
    }

    public static void littleEndianToInt(byte[] byArray, int n2, int[] nArray) {
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            nArray[i2] = Pack.littleEndianToInt(byArray, n2);
            n2 += 4;
        }
    }

    public static void littleEndianToInt(byte[] byArray, int n2, int[] nArray, int n3, int n4) {
        for (int i2 = 0; i2 < n4; ++i2) {
            nArray[n3 + i2] = Pack.littleEndianToInt(byArray, n2);
            n2 += 4;
        }
    }

    public static byte[] intToLittleEndian(int n2) {
        byte[] byArray = new byte[4];
        Pack.intToLittleEndian(n2, byArray, 0);
        return byArray;
    }

    public static void intToLittleEndian(int n2, byte[] byArray, int n3) {
        byArray[n3] = (byte)n2;
        byArray[++n3] = (byte)(n2 >>> 8);
        byArray[++n3] = (byte)(n2 >>> 16);
        byArray[++n3] = (byte)(n2 >>> 24);
    }

    public static byte[] intToLittleEndian(int[] nArray) {
        byte[] byArray = new byte[4 * nArray.length];
        Pack.intToLittleEndian(nArray, byArray, 0);
        return byArray;
    }

    public static void intToLittleEndian(int[] nArray, byte[] byArray, int n2) {
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            Pack.intToLittleEndian(nArray[i2], byArray, n2);
            n2 += 4;
        }
    }

    public static long littleEndianToLong(byte[] byArray, int n2) {
        int n3 = Pack.littleEndianToInt(byArray, n2);
        int n4 = Pack.littleEndianToInt(byArray, n2 + 4);
        return ((long)n4 & 0xFFFFFFFFL) << 32 | (long)n3 & 0xFFFFFFFFL;
    }

    public static void littleEndianToLong(byte[] byArray, int n2, long[] lArray) {
        for (int i2 = 0; i2 < lArray.length; ++i2) {
            lArray[i2] = Pack.littleEndianToLong(byArray, n2);
            n2 += 8;
        }
    }

    public static byte[] longToLittleEndian(long l2) {
        byte[] byArray = new byte[8];
        Pack.longToLittleEndian(l2, byArray, 0);
        return byArray;
    }

    public static void longToLittleEndian(long l2, byte[] byArray, int n2) {
        Pack.intToLittleEndian((int)(l2 & 0xFFFFFFFFL), byArray, n2);
        Pack.intToLittleEndian((int)(l2 >>> 32), byArray, n2 + 4);
    }

    public static byte[] longToLittleEndian(long[] lArray) {
        byte[] byArray = new byte[8 * lArray.length];
        Pack.longToLittleEndian(lArray, byArray, 0);
        return byArray;
    }

    public static void longToLittleEndian(long[] lArray, byte[] byArray, int n2) {
        for (int i2 = 0; i2 < lArray.length; ++i2) {
            Pack.longToLittleEndian(lArray[i2], byArray, n2);
            n2 += 8;
        }
    }
}

