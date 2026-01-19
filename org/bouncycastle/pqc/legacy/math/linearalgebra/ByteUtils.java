/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.linearalgebra;

import org.bouncycastle.util.Strings;

public final class ByteUtils {
    private static final char[] HEX_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private ByteUtils() {
    }

    public static boolean equals(byte[] byArray, byte[] byArray2) {
        if (byArray == null) {
            return byArray2 == null;
        }
        if (byArray2 == null) {
            return false;
        }
        if (byArray.length != byArray2.length) {
            return false;
        }
        boolean bl = true;
        for (int i2 = byArray.length - 1; i2 >= 0; --i2) {
            bl &= byArray[i2] == byArray2[i2];
        }
        return bl;
    }

    public static boolean equals(byte[][] byArray, byte[][] byArray2) {
        if (byArray.length != byArray2.length) {
            return false;
        }
        boolean bl = true;
        for (int i2 = byArray.length - 1; i2 >= 0; --i2) {
            bl &= ByteUtils.equals(byArray[i2], byArray2[i2]);
        }
        return bl;
    }

    public static boolean equals(byte[][][] byArray, byte[][][] byArray2) {
        if (byArray.length != byArray2.length) {
            return false;
        }
        boolean bl = true;
        for (int i2 = byArray.length - 1; i2 >= 0; --i2) {
            if (byArray[i2].length != byArray2[i2].length) {
                return false;
            }
            for (int i3 = byArray[i2].length - 1; i3 >= 0; --i3) {
                bl &= ByteUtils.equals(byArray[i2][i3], byArray2[i2][i3]);
            }
        }
        return bl;
    }

    public static int deepHashCode(byte[] byArray) {
        int n2 = 1;
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            n2 = 31 * n2 + byArray[i2];
        }
        return n2;
    }

    public static int deepHashCode(byte[][] byArray) {
        int n2 = 1;
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            n2 = 31 * n2 + ByteUtils.deepHashCode(byArray[i2]);
        }
        return n2;
    }

    public static int deepHashCode(byte[][][] byArray) {
        int n2 = 1;
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            n2 = 31 * n2 + ByteUtils.deepHashCode(byArray[i2]);
        }
        return n2;
    }

    public static byte[] clone(byte[] byArray) {
        if (byArray == null) {
            return null;
        }
        byte[] byArray2 = new byte[byArray.length];
        System.arraycopy(byArray, 0, byArray2, 0, byArray.length);
        return byArray2;
    }

    public static byte[] fromHexString(String string) {
        char[] cArray = Strings.toUpperCase(string).toCharArray();
        int n2 = 0;
        for (int i2 = 0; i2 < cArray.length; ++i2) {
            if ((cArray[i2] < '0' || cArray[i2] > '9') && (cArray[i2] < 'A' || cArray[i2] > 'F')) continue;
            ++n2;
        }
        byte[] byArray = new byte[n2 + 1 >> 1];
        int n3 = n2 & 1;
        for (int i3 = 0; i3 < cArray.length; ++i3) {
            if (cArray[i3] >= '0' && cArray[i3] <= '9') {
                int n4 = n3 >> 1;
                byArray[n4] = (byte)(byArray[n4] << 4);
                int n5 = n3 >> 1;
                byArray[n5] = (byte)(byArray[n5] | cArray[i3] - 48);
            } else {
                if (cArray[i3] < 'A' || cArray[i3] > 'F') continue;
                int n6 = n3 >> 1;
                byArray[n6] = (byte)(byArray[n6] << 4);
                int n7 = n3 >> 1;
                byArray[n7] = (byte)(byArray[n7] | cArray[i3] - 65 + 10);
            }
            ++n3;
        }
        return byArray;
    }

    public static String toHexString(byte[] byArray) {
        String string = "";
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            string = string + HEX_CHARS[byArray[i2] >>> 4 & 0xF];
            string = string + HEX_CHARS[byArray[i2] & 0xF];
        }
        return string;
    }

    public static String toHexString(byte[] byArray, String string, String string2) {
        String string3 = new String(string);
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            string3 = string3 + HEX_CHARS[byArray[i2] >>> 4 & 0xF];
            string3 = string3 + HEX_CHARS[byArray[i2] & 0xF];
            if (i2 >= byArray.length - 1) continue;
            string3 = string3 + string2;
        }
        return string3;
    }

    public static String toBinaryString(byte[] byArray) {
        String string = "";
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            byte by = byArray[i2];
            for (int i3 = 0; i3 < 8; ++i3) {
                int n2 = by >>> i3 & 1;
                string = string + n2;
            }
            if (i2 == byArray.length - 1) continue;
            string = string + " ";
        }
        return string;
    }

    public static byte[] xor(byte[] byArray, byte[] byArray2) {
        byte[] byArray3 = new byte[byArray.length];
        for (int i2 = byArray.length - 1; i2 >= 0; --i2) {
            byArray3[i2] = (byte)(byArray[i2] ^ byArray2[i2]);
        }
        return byArray3;
    }

    public static byte[] concatenate(byte[] byArray, byte[] byArray2) {
        byte[] byArray3 = new byte[byArray.length + byArray2.length];
        System.arraycopy(byArray, 0, byArray3, 0, byArray.length);
        System.arraycopy(byArray2, 0, byArray3, byArray.length, byArray2.length);
        return byArray3;
    }

    public static byte[] concatenate(byte[][] byArray) {
        int n2 = byArray[0].length;
        byte[] byArray2 = new byte[byArray.length * n2];
        int n3 = 0;
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            System.arraycopy(byArray[i2], 0, byArray2, n3, n2);
            n3 += n2;
        }
        return byArray2;
    }

    public static byte[][] split(byte[] byArray, int n2) throws ArrayIndexOutOfBoundsException {
        if (n2 > byArray.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        byte[][] byArrayArray = new byte[][]{new byte[n2], new byte[byArray.length - n2]};
        System.arraycopy(byArray, 0, byArrayArray[0], 0, n2);
        System.arraycopy(byArray, n2, byArrayArray[1], 0, byArray.length - n2);
        return byArrayArray;
    }

    public static byte[] subArray(byte[] byArray, int n2, int n3) {
        byte[] byArray2 = new byte[n3 - n2];
        System.arraycopy(byArray, n2, byArray2, 0, n3 - n2);
        return byArray2;
    }

    public static byte[] subArray(byte[] byArray, int n2) {
        return ByteUtils.subArray(byArray, n2, byArray.length);
    }

    public static char[] toCharArray(byte[] byArray) {
        char[] cArray = new char[byArray.length];
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            cArray[i2] = (char)byArray[i2];
        }
        return cArray;
    }
}

