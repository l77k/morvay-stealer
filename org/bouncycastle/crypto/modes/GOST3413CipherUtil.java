/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.modes;

import org.bouncycastle.util.Arrays;

class GOST3413CipherUtil {
    GOST3413CipherUtil() {
    }

    public static byte[] MSB(byte[] byArray, int n2) {
        return Arrays.copyOf(byArray, n2);
    }

    public static byte[] LSB(byte[] byArray, int n2) {
        byte[] byArray2 = new byte[n2];
        System.arraycopy(byArray, byArray.length - n2, byArray2, 0, n2);
        return byArray2;
    }

    public static byte[] sum(byte[] byArray, byte[] byArray2) {
        byte[] byArray3 = new byte[byArray.length];
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            byArray3[i2] = (byte)(byArray[i2] ^ byArray2[i2]);
        }
        return byArray3;
    }

    public static byte[] copyFromInput(byte[] byArray, int n2, int n3) {
        if (byArray.length < n2 + n3) {
            n2 = byArray.length - n3;
        }
        byte[] byArray2 = new byte[n2];
        System.arraycopy(byArray, n3, byArray2, 0, n2);
        return byArray2;
    }
}

