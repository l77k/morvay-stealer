/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.util;

public abstract class Pack {
    public static short bigEndianToShort(byte[] byArray, int n2) {
        int n3 = (byArray[n2] & 0xFF) << 8;
        return (short)(n3 |= byArray[++n2] & 0xFF);
    }

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

    public static void bigEndianToInt(byte[] byArray, int n2, int[] nArray, int n3, int n4) {
        for (int i2 = 0; i2 < n4; ++i2) {
            nArray[n3 + i2] = Pack.bigEndianToInt(byArray, n2);
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

    public static void intToBigEndian(int[] nArray, int n2, int n3, byte[] byArray, int n4) {
        for (int i2 = 0; i2 < n3; ++i2) {
            Pack.intToBigEndian(nArray[n2 + i2], byArray, n4);
            n4 += 4;
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

    public static void bigEndianToLong(byte[] byArray, int n2, long[] lArray, int n3, int n4) {
        for (int i2 = 0; i2 < n4; ++i2) {
            lArray[n3 + i2] = Pack.bigEndianToLong(byArray, n2);
            n2 += 8;
        }
    }

    public static long bigEndianToLong(byte[] byArray, int n2, int n3) {
        long l2 = 0L;
        for (int i2 = 0; i2 < n3; ++i2) {
            l2 |= ((long)byArray[i2 + n2] & 0xFFL) << (7 - i2 << 3);
        }
        return l2;
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

    public static void longToBigEndian(long[] lArray, int n2, int n3, byte[] byArray, int n4) {
        for (int i2 = 0; i2 < n3; ++i2) {
            Pack.longToBigEndian(lArray[n2 + i2], byArray, n4);
            n4 += 8;
        }
    }

    public static void longToBigEndian(long l2, byte[] byArray, int n2, int n3) {
        for (int i2 = n3 - 1; i2 >= 0; --i2) {
            byArray[i2 + n2] = (byte)(l2 & 0xFFL);
            l2 >>>= 8;
        }
    }

    public static short littleEndianToShort(byte[] byArray, int n2) {
        int n3 = byArray[n2] & 0xFF;
        return (short)(n3 |= (byArray[++n2] & 0xFF) << 8);
    }

    public static int littleEndianToInt(byte[] byArray, int n2) {
        int n3 = byArray[n2] & 0xFF;
        n3 |= (byArray[++n2] & 0xFF) << 8;
        n3 |= (byArray[++n2] & 0xFF) << 16;
        return n3 |= byArray[++n2] << 24;
    }

    public static int littleEndianToInt_High(byte[] byArray, int n2, int n3) {
        return Pack.littleEndianToInt_Low(byArray, n2, n3) << (4 - n3 << 3);
    }

    public static int littleEndianToInt_Low(byte[] byArray, int n2, int n3) {
        int n4 = byArray[n2] & 0xFF;
        int n5 = 0;
        for (int i2 = 1; i2 < n3; ++i2) {
            n4 |= (byArray[n2 + i2] & 0xFF) << (n5 += 8);
        }
        return n4;
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

    public static int[] littleEndianToInt(byte[] byArray, int n2, int n3) {
        int[] nArray = new int[n3];
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            nArray[i2] = Pack.littleEndianToInt(byArray, n2);
            n2 += 4;
        }
        return nArray;
    }

    public static byte[] shortToLittleEndian(short s2) {
        byte[] byArray = new byte[2];
        Pack.shortToLittleEndian(s2, byArray, 0);
        return byArray;
    }

    public static void shortToLittleEndian(short s2, byte[] byArray, int n2) {
        byArray[n2] = (byte)s2;
        byArray[++n2] = (byte)(s2 >>> 8);
    }

    public static byte[] shortToBigEndian(short s2) {
        byte[] byArray = new byte[2];
        Pack.shortToBigEndian(s2, byArray, 0);
        return byArray;
    }

    public static void shortToBigEndian(short s2, byte[] byArray, int n2) {
        byArray[n2] = (byte)(s2 >>> 8);
        byArray[++n2] = (byte)s2;
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

    public static void intToLittleEndian(int[] nArray, int n2, int n3, byte[] byArray, int n4) {
        for (int i2 = 0; i2 < n3; ++i2) {
            Pack.intToLittleEndian(nArray[n2 + i2], byArray, n4);
            n4 += 4;
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

    public static long littleEndianToLong(byte[] byArray, int n2, int n3) {
        long l2 = 0L;
        for (int i2 = 0; i2 < n3; ++i2) {
            l2 |= ((long)byArray[n2 + i2] & 0xFFL) << (i2 << 3);
        }
        return l2;
    }

    public static void littleEndianToLong(byte[] byArray, int n2, long[] lArray, int n3, int n4) {
        for (int i2 = 0; i2 < n4; ++i2) {
            lArray[n3 + i2] = Pack.littleEndianToLong(byArray, n2);
            n2 += 8;
        }
    }

    public static void longToLittleEndian_High(long l2, byte[] byArray, int n2, int n3) {
        int n4 = 56;
        byArray[n2] = (byte)(l2 >>> n4);
        for (int i2 = 1; i2 < n3; ++i2) {
            byArray[n2 + i2] = (byte)(l2 >>> (n4 -= 8));
        }
    }

    public static void longToLittleEndian(long l2, byte[] byArray, int n2, int n3) {
        for (int i2 = 0; i2 < n3; ++i2) {
            byArray[n2 + i2] = (byte)(l2 >>> (i2 << 3));
        }
    }

    public static long littleEndianToLong_High(byte[] byArray, int n2, int n3) {
        return Pack.littleEndianToLong_Low(byArray, n2, n3) << (8 - n3 << 3);
    }

    public static long littleEndianToLong_Low(byte[] byArray, int n2, int n3) {
        long l2 = byArray[n2] & 0xFF;
        for (int i2 = 1; i2 < n3; ++i2) {
            l2 <<= 8;
            l2 |= (long)(byArray[n2 + i2] & 0xFF);
        }
        return l2;
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

    public static void longToLittleEndian(long[] lArray, int n2, int n3, byte[] byArray, int n4) {
        for (int i2 = 0; i2 < n3; ++i2) {
            Pack.longToLittleEndian(lArray[n2 + i2], byArray, n4);
            n4 += 8;
        }
    }
}

