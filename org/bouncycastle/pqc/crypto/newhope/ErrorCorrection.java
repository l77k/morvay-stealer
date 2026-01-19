/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.newhope;

import org.bouncycastle.pqc.crypto.newhope.ChaCha20;
import org.bouncycastle.util.Arrays;

class ErrorCorrection {
    ErrorCorrection() {
    }

    static int abs(int n2) {
        int n3 = n2 >> 31;
        return (n2 ^ n3) - n3;
    }

    static int f(int[] nArray, int n2, int n3, int n4) {
        int n5 = n4 * 2730;
        int n6 = n5 >> 25;
        n5 = n4 - n6 * 12289;
        n5 = 12288 - n5;
        int n7 = (n6 -= (n5 >>= 31)) & 1;
        int n8 = n6 >> 1;
        nArray[n2] = n8 + n7;
        n7 = --n6 & 1;
        nArray[n3] = (n6 >> 1) + n7;
        return ErrorCorrection.abs(n4 - nArray[n2] * 2 * 12289);
    }

    static int g(int n2) {
        int n3 = n2 * 2730;
        int n4 = n3 >> 27;
        n3 = n2 - n4 * 49156;
        n3 = 49155 - n3;
        int n5 = (n4 -= (n3 >>= 31)) & 1;
        n4 = (n4 >> 1) + n5;
        return ErrorCorrection.abs((n4 *= 98312) - n2);
    }

    static void helpRec(short[] sArray, short[] sArray2, byte[] byArray, byte by) {
        byte[] byArray2 = new byte[8];
        byArray2[0] = by;
        byte[] byArray3 = new byte[32];
        ChaCha20.process(byArray, byArray2, byArray3, 0, byArray3.length);
        int[] nArray = new int[8];
        int[] nArray2 = new int[4];
        for (int i2 = 0; i2 < 256; ++i2) {
            int n2 = byArray3[i2 >>> 3] >>> (i2 & 7) & 1;
            int n3 = ErrorCorrection.f(nArray, 0, 4, 8 * sArray2[0 + i2] + 4 * n2);
            n3 += ErrorCorrection.f(nArray, 1, 5, 8 * sArray2[256 + i2] + 4 * n2);
            n3 += ErrorCorrection.f(nArray, 2, 6, 8 * sArray2[512 + i2] + 4 * n2);
            n3 += ErrorCorrection.f(nArray, 3, 7, 8 * sArray2[768 + i2] + 4 * n2);
            n3 = 24577 - n3 >> 31;
            nArray2[0] = ~n3 & nArray[0] ^ n3 & nArray[4];
            nArray2[1] = ~n3 & nArray[1] ^ n3 & nArray[5];
            nArray2[2] = ~n3 & nArray[2] ^ n3 & nArray[6];
            nArray2[3] = ~n3 & nArray[3] ^ n3 & nArray[7];
            sArray[0 + i2] = (short)(nArray2[0] - nArray2[3] & 3);
            sArray[256 + i2] = (short)(nArray2[1] - nArray2[3] & 3);
            sArray[512 + i2] = (short)(nArray2[2] - nArray2[3] & 3);
            sArray[768 + i2] = (short)(-n3 + 2 * nArray2[3] & 3);
        }
    }

    static short LDDecode(int n2, int n3, int n4, int n5) {
        int n6 = ErrorCorrection.g(n2);
        n6 += ErrorCorrection.g(n3);
        n6 += ErrorCorrection.g(n4);
        n6 += ErrorCorrection.g(n5);
        return (short)((n6 -= 98312) >>> 31);
    }

    static void rec(byte[] byArray, short[] sArray, short[] sArray2) {
        Arrays.fill(byArray, (byte)0);
        int[] nArray = new int[4];
        for (int i2 = 0; i2 < 256; ++i2) {
            nArray[0] = 196624 + 8 * sArray[0 + i2] - 12289 * (2 * sArray2[0 + i2] + sArray2[768 + i2]);
            nArray[1] = 196624 + 8 * sArray[256 + i2] - 12289 * (2 * sArray2[256 + i2] + sArray2[768 + i2]);
            nArray[2] = 196624 + 8 * sArray[512 + i2] - 12289 * (2 * sArray2[512 + i2] + sArray2[768 + i2]);
            nArray[3] = 196624 + 8 * sArray[768 + i2] - 12289 * sArray2[768 + i2];
            int n2 = i2 >>> 3;
            byArray[n2] = (byte)(byArray[n2] | ErrorCorrection.LDDecode(nArray[0], nArray[1], nArray[2], nArray[3]) << (i2 & 7));
        }
    }
}

