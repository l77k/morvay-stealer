/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.gmss.util;

public class GMSSUtil {
    public byte[] intToBytesLittleEndian(int n2) {
        byte[] byArray = new byte[]{(byte)(n2 & 0xFF), (byte)(n2 >> 8 & 0xFF), (byte)(n2 >> 16 & 0xFF), (byte)(n2 >> 24 & 0xFF)};
        return byArray;
    }

    public int bytesToIntLittleEndian(byte[] byArray) {
        return byArray[0] & 0xFF | (byArray[1] & 0xFF) << 8 | (byArray[2] & 0xFF) << 16 | (byArray[3] & 0xFF) << 24;
    }

    public int bytesToIntLittleEndian(byte[] byArray, int n2) {
        return byArray[n2++] & 0xFF | (byArray[n2++] & 0xFF) << 8 | (byArray[n2++] & 0xFF) << 16 | (byArray[n2] & 0xFF) << 24;
    }

    public byte[] concatenateArray(byte[][] byArray) {
        byte[] byArray2 = new byte[byArray.length * byArray[0].length];
        int n2 = 0;
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            System.arraycopy(byArray[i2], 0, byArray2, n2, byArray[i2].length);
            n2 += byArray[i2].length;
        }
        return byArray2;
    }

    public void printArray(String string, byte[][] byArray) {
        System.out.println(string);
        int n2 = 0;
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            for (int i3 = 0; i3 < byArray[0].length; ++i3) {
                System.out.println(n2 + "; " + byArray[i2][i3]);
                ++n2;
            }
        }
    }

    public void printArray(String string, byte[] byArray) {
        System.out.println(string);
        int n2 = 0;
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            System.out.println(n2 + "; " + byArray[i2]);
            ++n2;
        }
    }

    public boolean testPowerOfTwo(int n2) {
        int n3;
        for (n3 = 1; n3 < n2; n3 <<= 1) {
        }
        return n2 == n3;
    }

    public int getLog(int n2) {
        int n3 = 1;
        int n4 = 2;
        while (n4 < n2) {
            n4 <<= 1;
            ++n3;
        }
        return n3;
    }
}

