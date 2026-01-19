/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.linearalgebra;

public final class CharUtils {
    private CharUtils() {
    }

    public static char[] clone(char[] cArray) {
        char[] cArray2 = new char[cArray.length];
        System.arraycopy(cArray, 0, cArray2, 0, cArray.length);
        return cArray2;
    }

    public static byte[] toByteArray(char[] cArray) {
        byte[] byArray = new byte[cArray.length];
        for (int i2 = cArray.length - 1; i2 >= 0; --i2) {
            byArray[i2] = (byte)cArray[i2];
        }
        return byArray;
    }

    public static byte[] toByteArrayForPBE(char[] cArray) {
        int n2;
        byte[] byArray = new byte[cArray.length];
        for (n2 = 0; n2 < cArray.length; ++n2) {
            byArray[n2] = (byte)cArray[n2];
        }
        n2 = byArray.length * 2;
        byte[] byArray2 = new byte[n2 + 2];
        int n3 = 0;
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            n3 = i2 * 2;
            byArray2[n3] = 0;
            byArray2[n3 + 1] = byArray[i2];
        }
        byArray2[n2] = 0;
        byArray2[n2 + 1] = 0;
        return byArray2;
    }

    public static boolean equals(char[] cArray, char[] cArray2) {
        if (cArray.length != cArray2.length) {
            return false;
        }
        boolean bl = true;
        for (int i2 = cArray.length - 1; i2 >= 0; --i2) {
            bl &= cArray[i2] == cArray2[i2];
        }
        return bl;
    }
}

