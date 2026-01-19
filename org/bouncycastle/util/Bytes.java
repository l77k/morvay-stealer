/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.util;

public class Bytes {
    public static final int BYTES = 1;
    public static final int SIZE = 8;

    public static void xor(int n2, byte[] byArray, byte[] byArray2, byte[] byArray3) {
        for (int i2 = 0; i2 < n2; ++i2) {
            byArray3[i2] = (byte)(byArray[i2] ^ byArray2[i2]);
        }
    }

    public static void xor(int n2, byte[] byArray, int n3, byte[] byArray2, int n4, byte[] byArray3, int n5) {
        for (int i2 = 0; i2 < n2; ++i2) {
            byArray3[n5 + i2] = (byte)(byArray[n3 + i2] ^ byArray2[n4 + i2]);
        }
    }

    public static void xorTo(int n2, byte[] byArray, byte[] byArray2) {
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3 = i2;
            byArray2[n3] = (byte)(byArray2[n3] ^ byArray[i2]);
        }
    }

    public static void xorTo(int n2, byte[] byArray, int n3, byte[] byArray2, int n4) {
        for (int i2 = 0; i2 < n2; ++i2) {
            int n5 = n4 + i2;
            byArray2[n5] = (byte)(byArray2[n5] ^ byArray[n3 + i2]);
        }
    }
}

