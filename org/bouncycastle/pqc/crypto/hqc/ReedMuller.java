/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.hqc;

import org.bouncycastle.pqc.crypto.hqc.Utils;
import org.bouncycastle.util.Arrays;

class ReedMuller {
    ReedMuller() {
    }

    static void encodeSub(Codeword codeword, int n2) {
        int n3 = ReedMuller.Bit0Mask(n2 >> 7);
        n3 ^= ReedMuller.Bit0Mask(n2 >> 0) & 0xAAAAAAAA;
        n3 ^= ReedMuller.Bit0Mask(n2 >> 1) & 0xCCCCCCCC;
        n3 ^= ReedMuller.Bit0Mask(n2 >> 2) & 0xF0F0F0F0;
        n3 ^= ReedMuller.Bit0Mask(n2 >> 3) & 0xFF00FF00;
        codeword.type32[0] = n3 ^= ReedMuller.Bit0Mask(n2 >> 4) & 0xFFFF0000;
        codeword.type32[1] = n3 ^= ReedMuller.Bit0Mask(n2 >> 5);
        codeword.type32[3] = n3 ^= ReedMuller.Bit0Mask(n2 >> 6);
        codeword.type32[2] = n3 ^= ReedMuller.Bit0Mask(n2 >> 5);
    }

    private static void hadamardTransform(int[] nArray, int[] nArray2) {
        int[] nArray3 = Arrays.clone(nArray);
        int[] nArray4 = Arrays.clone(nArray2);
        for (int i2 = 0; i2 < 7; ++i2) {
            for (int i3 = 0; i3 < 64; ++i3) {
                nArray4[i3] = nArray3[2 * i3] + nArray3[2 * i3 + 1];
                nArray4[i3 + 64] = nArray3[2 * i3] - nArray3[2 * i3 + 1];
            }
            int[] nArray5 = nArray3;
            nArray3 = nArray4;
            nArray4 = nArray5;
        }
        System.arraycopy(nArray4, 0, nArray, 0, nArray.length);
        System.arraycopy(nArray3, 0, nArray2, 0, nArray2.length);
    }

    private static void expandThenSum(int[] nArray, Codeword[] codewordArray, int n2, int n3) {
        int n4;
        int n5;
        for (n5 = 0; n5 < 4; ++n5) {
            for (n4 = 0; n4 < 32; ++n4) {
                long l2 = codewordArray[0 + n2].type32[n5] >> n4 & 1;
                nArray[n5 * 32 + n4] = codewordArray[0 + n2].type32[n5] >> n4 & 1;
            }
        }
        for (n5 = 1; n5 < n3; ++n5) {
            for (n4 = 0; n4 < 4; ++n4) {
                for (int i2 = 0; i2 < 32; ++i2) {
                    int n6 = n4 * 32 + i2;
                    nArray[n6] = nArray[n6] + (codewordArray[n5 + n2].type32[n4] >> i2 & 1);
                }
            }
        }
    }

    private static int findPeaks(int[] nArray) {
        int n2;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        for (n2 = 0; n2 < 128; ++n2) {
            int n6 = nArray[n2];
            int n7 = n6 > 0 ? -1 : 0;
            int n8 = n7 & n6 | ~n7 & -n6;
            n4 = n8 > n3 ? n6 : n4;
            n5 = n8 > n3 ? n2 : n5;
            n3 = n8 > n3 ? n8 : n3;
        }
        n2 = n4 > 0 ? 1 : 0;
        return n5 |= 128 * n2;
    }

    private static int Bit0Mask(int n2) {
        return -(n2 & 1) & 0xFFFFFFFF;
    }

    public static void encode(long[] lArray, byte[] byArray, int n2, int n3) {
        int n4;
        int n5;
        int n6;
        byte[] byArray2 = Arrays.clone(byArray);
        Codeword[] codewordArray = new Codeword[n2 * n3];
        for (n6 = 0; n6 < codewordArray.length; ++n6) {
            codewordArray[n6] = new Codeword();
        }
        for (n6 = 0; n6 < n2; ++n6) {
            n5 = n6 * n3;
            ReedMuller.encodeSub(codewordArray[n5], byArray2[n6]);
            for (n4 = 1; n4 < n3; ++n4) {
                codewordArray[n5 + n4] = codewordArray[n5];
            }
        }
        int[] nArray = new int[codewordArray.length * 4];
        n5 = 0;
        for (n4 = 0; n4 < codewordArray.length; ++n4) {
            System.arraycopy(codewordArray[n4].type32, 0, nArray, n5, codewordArray[n4].type32.length);
            n5 += 4;
        }
        Utils.fromByte32ArrayToLongArray(lArray, nArray);
    }

    public static void decode(byte[] byArray, long[] lArray, int n2, int n3) {
        int n4;
        byte[] byArray2 = Arrays.clone(byArray);
        Codeword[] codewordArray = new Codeword[lArray.length / 2];
        int[] nArray = new int[lArray.length * 2];
        Utils.fromLongArrayToByte32Array(nArray, lArray);
        for (int i2 = 0; i2 < codewordArray.length; ++i2) {
            codewordArray[i2] = new Codeword();
            for (n4 = 0; n4 < 4; ++n4) {
                codewordArray[i2].type32[n4] = nArray[i2 * 4 + n4];
            }
        }
        int[] nArray2 = new int[128];
        for (n4 = 0; n4 < n2; ++n4) {
            ReedMuller.expandThenSum(nArray2, codewordArray, n4 * n3, n3);
            int[] nArray3 = new int[128];
            ReedMuller.hadamardTransform(nArray2, nArray3);
            nArray3[0] = nArray3[0] - 64 * n3;
            byArray2[n4] = (byte)ReedMuller.findPeaks(nArray3);
        }
        int[] nArray4 = new int[codewordArray.length * 4];
        int n5 = 0;
        for (int i3 = 0; i3 < codewordArray.length; ++i3) {
            System.arraycopy(codewordArray[i3].type32, 0, nArray4, n5, codewordArray[i3].type32.length);
            n5 += 4;
        }
        Utils.fromByte32ArrayToLongArray(lArray, nArray4);
        System.arraycopy(byArray2, 0, byArray, 0, byArray.length);
    }

    static class Codeword {
        int[] type32 = new int[4];
        int[] type8 = new int[16];
    }
}

