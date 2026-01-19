/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.linearalgebra;

public final class LittleEndianConversions {
    private LittleEndianConversions() {
    }

    public static int OS2IP(byte[] byArray) {
        return byArray[0] & 0xFF | (byArray[1] & 0xFF) << 8 | (byArray[2] & 0xFF) << 16 | (byArray[3] & 0xFF) << 24;
    }

    public static int OS2IP(byte[] byArray, int n2) {
        int n3 = byArray[n2++] & 0xFF;
        n3 |= (byArray[n2++] & 0xFF) << 8;
        n3 |= (byArray[n2++] & 0xFF) << 16;
        return n3 |= (byArray[n2] & 0xFF) << 24;
    }

    public static int OS2IP(byte[] byArray, int n2, int n3) {
        int n4 = 0;
        for (int i2 = n3 - 1; i2 >= 0; --i2) {
            n4 |= (byArray[n2 + i2] & 0xFF) << 8 * i2;
        }
        return n4;
    }

    public static long OS2LIP(byte[] byArray, int n2) {
        long l2 = byArray[n2++] & 0xFF;
        l2 |= (long)((byArray[n2++] & 0xFF) << 8);
        l2 |= (long)((byArray[n2++] & 0xFF) << 16);
        l2 |= ((long)byArray[n2++] & 0xFFL) << 24;
        l2 |= ((long)byArray[n2++] & 0xFFL) << 32;
        l2 |= ((long)byArray[n2++] & 0xFFL) << 40;
        l2 |= ((long)byArray[n2++] & 0xFFL) << 48;
        return l2 |= ((long)byArray[n2++] & 0xFFL) << 56;
    }

    public static byte[] I2OSP(int n2) {
        byte[] byArray = new byte[]{(byte)n2, (byte)(n2 >>> 8), (byte)(n2 >>> 16), (byte)(n2 >>> 24)};
        return byArray;
    }

    public static void I2OSP(int n2, byte[] byArray, int n3) {
        byArray[n3++] = (byte)n2;
        byArray[n3++] = (byte)(n2 >>> 8);
        byArray[n3++] = (byte)(n2 >>> 16);
        byArray[n3++] = (byte)(n2 >>> 24);
    }

    public static void I2OSP(int n2, byte[] byArray, int n3, int n4) {
        for (int i2 = n4 - 1; i2 >= 0; --i2) {
            byArray[n3 + i2] = (byte)(n2 >>> 8 * i2);
        }
    }

    public static byte[] I2OSP(long l2) {
        byte[] byArray = new byte[]{(byte)l2, (byte)(l2 >>> 8), (byte)(l2 >>> 16), (byte)(l2 >>> 24), (byte)(l2 >>> 32), (byte)(l2 >>> 40), (byte)(l2 >>> 48), (byte)(l2 >>> 56)};
        return byArray;
    }

    public static void I2OSP(long l2, byte[] byArray, int n2) {
        byArray[n2++] = (byte)l2;
        byArray[n2++] = (byte)(l2 >>> 8);
        byArray[n2++] = (byte)(l2 >>> 16);
        byArray[n2++] = (byte)(l2 >>> 24);
        byArray[n2++] = (byte)(l2 >>> 32);
        byArray[n2++] = (byte)(l2 >>> 40);
        byArray[n2++] = (byte)(l2 >>> 48);
        byArray[n2] = (byte)(l2 >>> 56);
    }

    public static byte[] toByteArray(int[] nArray, int n2) {
        int n3 = nArray.length;
        byte[] byArray = new byte[n2];
        int n4 = 0;
        int n5 = 0;
        while (n5 <= n3 - 2) {
            LittleEndianConversions.I2OSP(nArray[n5], byArray, n4);
            ++n5;
            n4 += 4;
        }
        LittleEndianConversions.I2OSP(nArray[n3 - 1], byArray, n4, n2 - n4);
        return byArray;
    }

    public static int[] toIntArray(byte[] byArray) {
        int n2 = (byArray.length + 3) / 4;
        int n3 = byArray.length & 3;
        int[] nArray = new int[n2];
        int n4 = 0;
        int n5 = 0;
        while (n5 <= n2 - 2) {
            nArray[n5] = LittleEndianConversions.OS2IP(byArray, n4);
            ++n5;
            n4 += 4;
        }
        nArray[n2 - 1] = n3 != 0 ? LittleEndianConversions.OS2IP(byArray, n4, n3) : LittleEndianConversions.OS2IP(byArray, n4);
        return nArray;
    }
}

