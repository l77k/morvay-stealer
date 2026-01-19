/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.rainbow.util;

public class RainbowUtil {
    public static int[] convertArraytoInt(byte[] byArray) {
        int[] nArray = new int[byArray.length];
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            nArray[i2] = byArray[i2] & 0xFF;
        }
        return nArray;
    }

    public static short[] convertArray(byte[] byArray) {
        short[] sArray = new short[byArray.length];
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            sArray[i2] = (short)(byArray[i2] & 0xFF);
        }
        return sArray;
    }

    public static short[][] convertArray(byte[][] byArray) {
        short[][] sArray = new short[byArray.length][byArray[0].length];
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            for (int i3 = 0; i3 < byArray[0].length; ++i3) {
                sArray[i2][i3] = (short)(byArray[i2][i3] & 0xFF);
            }
        }
        return sArray;
    }

    public static short[][][] convertArray(byte[][][] byArray) {
        short[][][] sArray = new short[byArray.length][byArray[0].length][byArray[0][0].length];
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            for (int i3 = 0; i3 < byArray[0].length; ++i3) {
                for (int i4 = 0; i4 < byArray[0][0].length; ++i4) {
                    sArray[i2][i3][i4] = (short)(byArray[i2][i3][i4] & 0xFF);
                }
            }
        }
        return sArray;
    }

    public static byte[] convertIntArray(int[] nArray) {
        byte[] byArray = new byte[nArray.length];
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            byArray[i2] = (byte)nArray[i2];
        }
        return byArray;
    }

    public static byte[] convertArray(short[] sArray) {
        byte[] byArray = new byte[sArray.length];
        for (int i2 = 0; i2 < sArray.length; ++i2) {
            byArray[i2] = (byte)sArray[i2];
        }
        return byArray;
    }

    public static byte[][] convertArray(short[][] sArray) {
        byte[][] byArray = new byte[sArray.length][sArray[0].length];
        for (int i2 = 0; i2 < sArray.length; ++i2) {
            for (int i3 = 0; i3 < sArray[0].length; ++i3) {
                byArray[i2][i3] = (byte)sArray[i2][i3];
            }
        }
        return byArray;
    }

    public static byte[][][] convertArray(short[][][] sArray) {
        byte[][][] byArray = new byte[sArray.length][sArray[0].length][sArray[0][0].length];
        for (int i2 = 0; i2 < sArray.length; ++i2) {
            for (int i3 = 0; i3 < sArray[0].length; ++i3) {
                for (int i4 = 0; i4 < sArray[0][0].length; ++i4) {
                    byArray[i2][i3][i4] = (byte)sArray[i2][i3][i4];
                }
            }
        }
        return byArray;
    }

    public static boolean equals(short[] sArray, short[] sArray2) {
        if (sArray.length != sArray2.length) {
            return false;
        }
        boolean bl = true;
        for (int i2 = sArray.length - 1; i2 >= 0; --i2) {
            bl &= sArray[i2] == sArray2[i2];
        }
        return bl;
    }

    public static boolean equals(short[][] sArray, short[][] sArray2) {
        if (sArray.length != sArray2.length) {
            return false;
        }
        boolean bl = true;
        for (int i2 = sArray.length - 1; i2 >= 0; --i2) {
            bl &= RainbowUtil.equals(sArray[i2], sArray2[i2]);
        }
        return bl;
    }

    public static boolean equals(short[][][] sArray, short[][][] sArray2) {
        if (sArray.length != sArray2.length) {
            return false;
        }
        boolean bl = true;
        for (int i2 = sArray.length - 1; i2 >= 0; --i2) {
            bl &= RainbowUtil.equals(sArray[i2], sArray2[i2]);
        }
        return bl;
    }
}

