/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.hqc;

import org.bouncycastle.pqc.crypto.hqc.GFCalculator;
import org.bouncycastle.pqc.crypto.hqc.Utils;

class FastFourierTransform {
    FastFourierTransform() {
    }

    static void fastFourierTransform(int[] nArray, int[] nArray2, int n2, int n3) {
        int n4;
        int n5 = 8;
        int n6 = 128;
        int n7 = 1 << n3;
        int[] nArray3 = new int[n7];
        int[] nArray4 = new int[n7];
        int[] nArray5 = new int[n5 - 1];
        int[] nArray6 = new int[n6];
        int[] nArray7 = new int[n6];
        int[] nArray8 = new int[n5 - 1];
        int[] nArray9 = new int[n6];
        FastFourierTransform.computeFFTBetas(nArray8, n5);
        FastFourierTransform.computeSubsetSum(nArray9, nArray8, n5 - 1);
        FastFourierTransform.computeRadix(nArray3, nArray4, nArray2, n3, n3);
        for (n4 = 0; n4 < n5 - 1; ++n4) {
            nArray5[n4] = GFCalculator.mult(nArray8[n4], nArray8[n4]) ^ nArray8[n4];
        }
        FastFourierTransform.computeFFTRec(nArray6, nArray3, (n2 + 1) / 2, n5 - 1, n3 - 1, nArray5, n3, n5);
        FastFourierTransform.computeFFTRec(nArray7, nArray4, n2 / 2, n5 - 1, n3 - 1, nArray5, n3, n5);
        n4 = 1;
        n4 = 1 << n5 - 1;
        System.arraycopy(nArray7, 0, nArray, n4, n4);
        nArray[0] = nArray6[0];
        int n8 = n4;
        nArray[n8] = nArray[n8] ^ nArray6[0];
        for (int i2 = 1; i2 < n4; ++i2) {
            nArray[i2] = nArray6[i2] ^ GFCalculator.mult(nArray9[i2], nArray7[i2]);
            int n9 = n4 + i2;
            nArray[n9] = nArray[n9] ^ nArray[i2];
        }
    }

    static void computeFFTBetas(int[] nArray, int n2) {
        for (int i2 = 0; i2 < n2 - 1; ++i2) {
            nArray[i2] = 1 << n2 - 1 - i2;
        }
    }

    static void computeSubsetSum(int[] nArray, int[] nArray2, int n2) {
        nArray[0] = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            for (int i3 = 0; i3 < 1 << i2; ++i3) {
                nArray[(1 << i2) + i3] = nArray2[i2] ^ nArray[i3];
            }
        }
    }

    static void computeRadix(int[] nArray, int[] nArray2, int[] nArray3, int n2, int n3) {
        switch (n2) {
            case 4: {
                nArray[4] = nArray3[8] ^ nArray3[12];
                nArray[6] = nArray3[12] ^ nArray3[14];
                nArray[7] = nArray3[14] ^ nArray3[15];
                nArray2[5] = nArray3[11] ^ nArray3[13];
                nArray2[6] = nArray3[13] ^ nArray3[14];
                nArray2[7] = nArray3[15];
                nArray[5] = nArray3[10] ^ nArray3[12] ^ nArray2[5];
                nArray2[4] = nArray3[9] ^ nArray3[13] ^ nArray[5];
                nArray[0] = nArray3[0];
                nArray2[3] = nArray3[7] ^ nArray3[11] ^ nArray3[15];
                nArray[3] = nArray3[6] ^ nArray3[10] ^ nArray3[14] ^ nArray2[3];
                nArray[2] = nArray3[4] ^ nArray[4] ^ nArray[3] ^ nArray2[3];
                nArray2[1] = nArray3[3] ^ nArray3[5] ^ nArray3[9] ^ nArray3[13] ^ nArray2[3];
                nArray2[2] = nArray3[3] ^ nArray2[1] ^ nArray[3];
                nArray[1] = nArray3[2] ^ nArray[2] ^ nArray2[1];
                nArray2[0] = nArray3[1] ^ nArray[1];
                return;
            }
            case 3: {
                nArray[0] = nArray3[0];
                nArray[2] = nArray3[4] ^ nArray3[6];
                nArray[3] = nArray3[6] ^ nArray3[7];
                nArray2[1] = nArray3[3] ^ nArray3[5] ^ nArray3[7];
                nArray2[2] = nArray3[5] ^ nArray3[6];
                nArray2[3] = nArray3[7];
                nArray[1] = nArray3[2] ^ nArray[2] ^ nArray2[1];
                nArray2[0] = nArray3[1] ^ nArray[1];
                return;
            }
            case 2: {
                nArray[0] = nArray3[0];
                nArray[1] = nArray3[2] ^ nArray3[3];
                nArray2[0] = nArray3[1] ^ nArray[1];
                nArray2[1] = nArray3[3];
                return;
            }
            case 1: {
                nArray[0] = nArray3[0];
                nArray2[0] = nArray3[1];
                return;
            }
        }
        FastFourierTransform.computeRadixBig(nArray, nArray2, nArray3, n2, n3);
    }

    static void computeRadixBig(int[] nArray, int[] nArray2, int[] nArray3, int n2, int n3) {
        int n4 = 1;
        n4 <<= n2 - 2;
        int n5 = 1 << n3 - 2;
        int[] nArray4 = new int[2 * n5 + 1];
        int[] nArray5 = new int[2 * n5 + 1];
        int[] nArray6 = new int[n5];
        int[] nArray7 = new int[n5];
        int[] nArray8 = new int[n5];
        int[] nArray9 = new int[n5];
        Utils.copyBytes(nArray3, 3 * n4, nArray4, 0, 2 * n4);
        Utils.copyBytes(nArray3, 3 * n4, nArray4, n4, 2 * n4);
        Utils.copyBytes(nArray3, 0, nArray5, 0, 4 * n4);
        for (int i2 = 0; i2 < n4; ++i2) {
            int n6 = i2;
            nArray4[n6] = nArray4[n6] ^ nArray3[2 * n4 + i2];
            int n7 = n4 + i2;
            nArray5[n7] = nArray5[n7] ^ nArray4[i2];
        }
        FastFourierTransform.computeRadix(nArray6, nArray7, nArray4, n2 - 1, n3);
        FastFourierTransform.computeRadix(nArray8, nArray9, nArray5, n2 - 1, n3);
        Utils.copyBytes(nArray8, 0, nArray, 0, 2 * n4);
        Utils.copyBytes(nArray6, 0, nArray, n4, 2 * n4);
        Utils.copyBytes(nArray9, 0, nArray2, 0, 2 * n4);
        Utils.copyBytes(nArray7, 0, nArray2, n4, 2 * n4);
    }

    static void computeFFTRec(int[] nArray, int[] nArray2, int n2, int n3, int n4, int[] nArray3, int n5, int n6) {
        int n7;
        int n8 = 1 << n5 - 2;
        int n9 = 1 << n6 - 2;
        int[] nArray4 = new int[n8];
        int[] nArray5 = new int[n8];
        int[] nArray6 = new int[n6 - 2];
        int[] nArray7 = new int[n6 - 2];
        int n10 = 1;
        int[] nArray8 = new int[n9];
        int[] nArray9 = new int[n9];
        int[] nArray10 = new int[n9];
        int[] nArray11 = new int[n6 - n5 + 1];
        int n11 = 0;
        if (n4 == 1) {
            int n12;
            for (n12 = 0; n12 < n3; ++n12) {
                nArray11[n12] = GFCalculator.mult(nArray3[n12], nArray2[1]);
            }
            nArray[0] = nArray2[0];
            n11 = 1;
            for (n12 = 0; n12 < n3; ++n12) {
                for (int i2 = 0; i2 < n11; ++i2) {
                    nArray[n11 + i2] = nArray[i2] ^ nArray11[n12];
                }
                n11 <<= 1;
            }
            return;
        }
        if (nArray3[n3 - 1] != 1) {
            n7 = 1;
            n11 = 1;
            n11 <<= n4;
            for (int i3 = 1; i3 < n11; ++i3) {
                n7 = GFCalculator.mult(n7, nArray3[n3 - 1]);
                nArray2[i3] = GFCalculator.mult(n7, nArray2[i3]);
            }
        }
        FastFourierTransform.computeRadix(nArray4, nArray5, nArray2, n4, n5);
        for (n7 = 0; n7 < n3 - 1; ++n7) {
            nArray6[n7] = GFCalculator.mult(nArray3[n7], GFCalculator.inverse(nArray3[n3 - 1]));
            nArray7[n7] = GFCalculator.mult(nArray6[n7], nArray6[n7]) ^ nArray6[n7];
        }
        FastFourierTransform.computeSubsetSum(nArray8, nArray6, n3 - 1);
        FastFourierTransform.computeFFTRec(nArray9, nArray4, (n2 + 1) / 2, n3 - 1, n4 - 1, nArray7, n5, n6);
        n10 = 1;
        n10 <<= n3 - 1 & 0xF;
        if (n2 <= 3) {
            nArray[0] = nArray9[0];
            nArray[n10] = nArray9[0] ^ nArray5[0];
            for (n7 = 1; n7 < n10; ++n7) {
                nArray[n7] = nArray9[n7] ^ GFCalculator.mult(nArray8[n7], nArray5[0]);
                nArray[n10 + n7] = nArray[n7] ^ nArray5[0];
            }
        } else {
            FastFourierTransform.computeFFTRec(nArray10, nArray5, n2 / 2, n3 - 1, n4 - 1, nArray7, n5, n6);
            System.arraycopy(nArray10, 0, nArray, n10, n10);
            nArray[0] = nArray9[0];
            int n13 = n10;
            nArray[n13] = nArray[n13] ^ nArray9[0];
            for (n7 = 1; n7 < n10; ++n7) {
                nArray[n7] = nArray9[n7] ^ GFCalculator.mult(nArray8[n7], nArray10[n7]);
                int n14 = n10 + n7;
                nArray[n14] = nArray[n14] ^ nArray[n7];
            }
        }
    }

    static void fastFourierTransformGetError(byte[] byArray, int[] nArray, int n2, int[] nArray2) {
        int n3 = 8;
        int n4 = 255;
        int[] nArray3 = new int[n3 - 1];
        int[] nArray4 = new int[n2];
        int n5 = n2;
        FastFourierTransform.computeFFTBetas(nArray3, n3);
        FastFourierTransform.computeSubsetSum(nArray4, nArray3, n3 - 1);
        byArray[0] = (byte)(byArray[0] ^ (1 ^ Utils.toUnsigned16Bits(-nArray[0] >> 15)));
        byArray[0] = (byte)(byArray[0] ^ (1 ^ Utils.toUnsigned16Bits(-nArray[n5] >> 15)));
        for (int i2 = 1; i2 < n5; ++i2) {
            int n6;
            int n7 = n6 = n4 - nArray2[nArray4[i2]];
            byArray[n7] = (byte)(byArray[n7] ^ (1 ^ Math.abs(-nArray[i2] >> 15)));
            int n8 = n6 = n4 - nArray2[nArray4[i2] ^ 1];
            byArray[n8] = (byte)(byArray[n8] ^ (1 ^ Math.abs(-nArray[n5 + i2] >> 15)));
        }
    }
}

