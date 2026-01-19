/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.constraints.DefaultServiceProperties;
import org.bouncycastle.crypto.engines.Utils;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.RC2Parameters;

public class RC2Engine
implements BlockCipher {
    private static byte[] piTable = new byte[]{-39, 120, -7, -60, 25, -35, -75, -19, 40, -23, -3, 121, 74, -96, -40, -99, -58, 126, 55, -125, 43, 118, 83, -114, 98, 76, 100, -120, 68, -117, -5, -94, 23, -102, 89, -11, -121, -77, 79, 19, 97, 69, 109, -115, 9, -127, 125, 50, -67, -113, 64, -21, -122, -73, 123, 11, -16, -107, 33, 34, 92, 107, 78, -126, 84, -42, 101, -109, -50, 96, -78, 28, 115, 86, -64, 20, -89, -116, -15, -36, 18, 117, -54, 31, 59, -66, -28, -47, 66, 61, -44, 48, -93, 60, -74, 38, 111, -65, 14, -38, 70, 105, 7, 87, 39, -14, 29, -101, -68, -108, 67, 3, -8, 17, -57, -10, -112, -17, 62, -25, 6, -61, -43, 47, -56, 102, 30, -41, 8, -24, -22, -34, -128, 82, -18, -9, -124, -86, 114, -84, 53, 77, 106, 42, -106, 26, -46, 113, 90, 21, 73, 116, 75, -97, -48, 94, 4, 24, -92, -20, -62, -32, 65, 110, 15, 81, -53, -52, 36, -111, -81, 80, -95, -12, 112, 57, -103, 124, 58, -123, 35, -72, -76, 122, -4, 2, 54, 91, 37, 85, -105, 49, 45, 93, -6, -104, -29, -118, -110, -82, 5, -33, 41, 16, 103, 108, -70, -55, -45, 0, -26, -49, -31, -98, -88, 44, 99, 22, 1, 63, 88, -30, -119, -87, 13, 56, 52, 27, -85, 51, -1, -80, -69, 72, 12, 95, -71, -79, -51, 46, -59, -13, -37, 71, -27, -91, -100, 119, 10, -90, 32, 104, -2, 127, -63, -83};
    private static final int BLOCK_SIZE = 8;
    private int[] workingKey;
    private boolean encrypting;

    private int[] generateWorkingKey(byte[] byArray, int n2) {
        int n3;
        int n4;
        int n5;
        int[] nArray = new int[128];
        for (n5 = 0; n5 != byArray.length; ++n5) {
            nArray[n5] = byArray[n5] & 0xFF;
        }
        n5 = byArray.length;
        if (n5 < 128) {
            n4 = 0;
            n3 = nArray[n5 - 1];
            do {
                n3 = piTable[n3 + nArray[n4++] & 0xFF] & 0xFF;
                nArray[n5++] = n3;
            } while (n5 < 128);
        }
        n5 = n2 + 7 >> 3;
        nArray[128 - n5] = n3 = piTable[nArray[128 - n5] & 255 >> (7 & -n2)] & 0xFF;
        for (n4 = 128 - n5 - 1; n4 >= 0; --n4) {
            nArray[n4] = n3 = piTable[n3 ^ nArray[n4 + n5]] & 0xFF;
        }
        int[] nArray2 = new int[64];
        for (int i2 = 0; i2 != nArray2.length; ++i2) {
            nArray2[i2] = nArray[2 * i2] + (nArray[2 * i2 + 1] << 8);
        }
        return nArray2;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        byte[] byArray;
        this.encrypting = bl;
        if (cipherParameters instanceof RC2Parameters) {
            RC2Parameters rC2Parameters = (RC2Parameters)cipherParameters;
            this.workingKey = this.generateWorkingKey(rC2Parameters.getKey(), rC2Parameters.getEffectiveKeyBits());
            byArray = rC2Parameters.getKey();
        } else if (cipherParameters instanceof KeyParameter) {
            byArray = ((KeyParameter)cipherParameters).getKey();
            this.workingKey = this.generateWorkingKey(byArray, byArray.length * 8);
        } else {
            throw new IllegalArgumentException("invalid parameter passed to RC2 init - " + cipherParameters.getClass().getName());
        }
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), byArray.length * 8, cipherParameters, Utils.getPurpose(bl)));
    }

    @Override
    public void reset() {
    }

    @Override
    public String getAlgorithmName() {
        return "RC2";
    }

    @Override
    public int getBlockSize() {
        return 8;
    }

    @Override
    public final int processBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        if (this.workingKey == null) {
            throw new IllegalStateException("RC2 engine not initialised");
        }
        if (n2 + 8 > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + 8 > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this.encrypting) {
            this.encryptBlock(byArray, n2, byArray2, n3);
        } else {
            this.decryptBlock(byArray, n2, byArray2, n3);
        }
        return 8;
    }

    private int rotateWordLeft(int n2, int n3) {
        return (n2 &= 0xFFFF) << n3 | n2 >> 16 - n3;
    }

    private void encryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        int n4;
        int n5 = ((byArray[n2 + 7] & 0xFF) << 8) + (byArray[n2 + 6] & 0xFF);
        int n6 = ((byArray[n2 + 5] & 0xFF) << 8) + (byArray[n2 + 4] & 0xFF);
        int n7 = ((byArray[n2 + 3] & 0xFF) << 8) + (byArray[n2 + 2] & 0xFF);
        int n8 = ((byArray[n2 + 1] & 0xFF) << 8) + (byArray[n2 + 0] & 0xFF);
        for (n4 = 0; n4 <= 16; n4 += 4) {
            n8 = this.rotateWordLeft(n8 + (n7 & ~n5) + (n6 & n5) + this.workingKey[n4], 1);
            n7 = this.rotateWordLeft(n7 + (n6 & ~n8) + (n5 & n8) + this.workingKey[n4 + 1], 2);
            n6 = this.rotateWordLeft(n6 + (n5 & ~n7) + (n8 & n7) + this.workingKey[n4 + 2], 3);
            n5 = this.rotateWordLeft(n5 + (n8 & ~n6) + (n7 & n6) + this.workingKey[n4 + 3], 5);
        }
        n6 += this.workingKey[(n7 += this.workingKey[(n8 += this.workingKey[n5 & 0x3F]) & 0x3F]) & 0x3F];
        n5 += this.workingKey[n6 & 0x3F];
        for (n4 = 20; n4 <= 40; n4 += 4) {
            n8 = this.rotateWordLeft(n8 + (n7 & ~n5) + (n6 & n5) + this.workingKey[n4], 1);
            n7 = this.rotateWordLeft(n7 + (n6 & ~n8) + (n5 & n8) + this.workingKey[n4 + 1], 2);
            n6 = this.rotateWordLeft(n6 + (n5 & ~n7) + (n8 & n7) + this.workingKey[n4 + 2], 3);
            n5 = this.rotateWordLeft(n5 + (n8 & ~n6) + (n7 & n6) + this.workingKey[n4 + 3], 5);
        }
        n6 += this.workingKey[(n7 += this.workingKey[(n8 += this.workingKey[n5 & 0x3F]) & 0x3F]) & 0x3F];
        n5 += this.workingKey[n6 & 0x3F];
        for (n4 = 44; n4 < 64; n4 += 4) {
            n8 = this.rotateWordLeft(n8 + (n7 & ~n5) + (n6 & n5) + this.workingKey[n4], 1);
            n7 = this.rotateWordLeft(n7 + (n6 & ~n8) + (n5 & n8) + this.workingKey[n4 + 1], 2);
            n6 = this.rotateWordLeft(n6 + (n5 & ~n7) + (n8 & n7) + this.workingKey[n4 + 2], 3);
            n5 = this.rotateWordLeft(n5 + (n8 & ~n6) + (n7 & n6) + this.workingKey[n4 + 3], 5);
        }
        byArray2[n3 + 0] = (byte)n8;
        byArray2[n3 + 1] = (byte)(n8 >> 8);
        byArray2[n3 + 2] = (byte)n7;
        byArray2[n3 + 3] = (byte)(n7 >> 8);
        byArray2[n3 + 4] = (byte)n6;
        byArray2[n3 + 5] = (byte)(n6 >> 8);
        byArray2[n3 + 6] = (byte)n5;
        byArray2[n3 + 7] = (byte)(n5 >> 8);
    }

    private void decryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        int n4;
        int n5 = ((byArray[n2 + 7] & 0xFF) << 8) + (byArray[n2 + 6] & 0xFF);
        int n6 = ((byArray[n2 + 5] & 0xFF) << 8) + (byArray[n2 + 4] & 0xFF);
        int n7 = ((byArray[n2 + 3] & 0xFF) << 8) + (byArray[n2 + 2] & 0xFF);
        int n8 = ((byArray[n2 + 1] & 0xFF) << 8) + (byArray[n2 + 0] & 0xFF);
        for (n4 = 60; n4 >= 44; n4 -= 4) {
            n5 = this.rotateWordLeft(n5, 11) - ((n8 & ~n6) + (n7 & n6) + this.workingKey[n4 + 3]);
            n6 = this.rotateWordLeft(n6, 13) - ((n5 & ~n7) + (n8 & n7) + this.workingKey[n4 + 2]);
            n7 = this.rotateWordLeft(n7, 14) - ((n6 & ~n8) + (n5 & n8) + this.workingKey[n4 + 1]);
            n8 = this.rotateWordLeft(n8, 15) - ((n7 & ~n5) + (n6 & n5) + this.workingKey[n4]);
        }
        n5 -= this.workingKey[n6 & 0x3F];
        n6 -= this.workingKey[n7 & 0x3F];
        n7 -= this.workingKey[n8 & 0x3F];
        n8 -= this.workingKey[n5 & 0x3F];
        for (n4 = 40; n4 >= 20; n4 -= 4) {
            n5 = this.rotateWordLeft(n5, 11) - ((n8 & ~n6) + (n7 & n6) + this.workingKey[n4 + 3]);
            n6 = this.rotateWordLeft(n6, 13) - ((n5 & ~n7) + (n8 & n7) + this.workingKey[n4 + 2]);
            n7 = this.rotateWordLeft(n7, 14) - ((n6 & ~n8) + (n5 & n8) + this.workingKey[n4 + 1]);
            n8 = this.rotateWordLeft(n8, 15) - ((n7 & ~n5) + (n6 & n5) + this.workingKey[n4]);
        }
        n5 -= this.workingKey[n6 & 0x3F];
        n6 -= this.workingKey[n7 & 0x3F];
        n7 -= this.workingKey[n8 & 0x3F];
        n8 -= this.workingKey[n5 & 0x3F];
        for (n4 = 16; n4 >= 0; n4 -= 4) {
            n5 = this.rotateWordLeft(n5, 11) - ((n8 & ~n6) + (n7 & n6) + this.workingKey[n4 + 3]);
            n6 = this.rotateWordLeft(n6, 13) - ((n5 & ~n7) + (n8 & n7) + this.workingKey[n4 + 2]);
            n7 = this.rotateWordLeft(n7, 14) - ((n6 & ~n8) + (n5 & n8) + this.workingKey[n4 + 1]);
            n8 = this.rotateWordLeft(n8, 15) - ((n7 & ~n5) + (n6 & n5) + this.workingKey[n4]);
        }
        byArray2[n3 + 0] = (byte)n8;
        byArray2[n3 + 1] = (byte)(n8 >> 8);
        byArray2[n3 + 2] = (byte)n7;
        byArray2[n3 + 3] = (byte)(n7 >> 8);
        byArray2[n3 + 4] = (byte)n6;
        byArray2[n3 + 5] = (byte)(n6 >> 8);
        byArray2[n3 + 6] = (byte)n5;
        byArray2[n3 + 7] = (byte)(n5 >> 8);
    }
}

