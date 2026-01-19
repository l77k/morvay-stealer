/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicePurpose;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.constraints.DefaultServiceProperties;
import org.bouncycastle.crypto.params.KeyParameter;

public class SkipjackEngine
implements BlockCipher {
    static final int BLOCK_SIZE = 8;
    static short[] ftable = new short[]{163, 215, 9, 131, 248, 72, 246, 244, 179, 33, 21, 120, 153, 177, 175, 249, 231, 45, 77, 138, 206, 76, 202, 46, 82, 149, 217, 30, 78, 56, 68, 40, 10, 223, 2, 160, 23, 241, 96, 104, 18, 183, 122, 195, 233, 250, 61, 83, 150, 132, 107, 186, 242, 99, 154, 25, 124, 174, 229, 245, 247, 22, 106, 162, 57, 182, 123, 15, 193, 147, 129, 27, 238, 180, 26, 234, 208, 145, 47, 184, 85, 185, 218, 133, 63, 65, 191, 224, 90, 88, 128, 95, 102, 11, 216, 144, 53, 213, 192, 167, 51, 6, 101, 105, 69, 0, 148, 86, 109, 152, 155, 118, 151, 252, 178, 194, 176, 254, 219, 32, 225, 235, 214, 228, 221, 71, 74, 29, 66, 237, 158, 110, 73, 60, 205, 67, 39, 210, 7, 212, 222, 199, 103, 24, 137, 203, 48, 31, 141, 198, 143, 170, 200, 116, 220, 201, 93, 92, 49, 164, 112, 136, 97, 44, 159, 13, 43, 135, 80, 130, 84, 100, 38, 125, 3, 64, 52, 75, 28, 115, 209, 196, 253, 59, 204, 251, 127, 171, 230, 62, 91, 165, 173, 4, 35, 156, 20, 81, 34, 240, 41, 121, 113, 126, 255, 140, 14, 226, 12, 239, 188, 114, 117, 111, 55, 161, 236, 211, 142, 98, 139, 134, 16, 232, 8, 119, 17, 190, 146, 79, 36, 197, 50, 54, 157, 207, 243, 166, 187, 172, 94, 108, 169, 19, 87, 37, 181, 227, 189, 168, 58, 1, 5, 89, 42, 70};
    private int[] key0;
    private int[] key1;
    private int[] key2;
    private int[] key3;
    private boolean encrypting;

    public SkipjackEngine() {
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), 80));
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("invalid parameter passed to SKIPJACK init - " + cipherParameters.getClass().getName());
        }
        byte[] byArray = ((KeyParameter)cipherParameters).getKey();
        this.encrypting = bl;
        this.key0 = new int[32];
        this.key1 = new int[32];
        this.key2 = new int[32];
        this.key3 = new int[32];
        for (int i2 = 0; i2 < 32; ++i2) {
            this.key0[i2] = byArray[i2 * 4 % 10] & 0xFF;
            this.key1[i2] = byArray[(i2 * 4 + 1) % 10] & 0xFF;
            this.key2[i2] = byArray[(i2 * 4 + 2) % 10] & 0xFF;
            this.key3[i2] = byArray[(i2 * 4 + 3) % 10] & 0xFF;
        }
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), 80, cipherParameters, this.getPurpose()));
    }

    @Override
    public String getAlgorithmName() {
        return "SKIPJACK";
    }

    @Override
    public int getBlockSize() {
        return 8;
    }

    @Override
    public int processBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        if (this.key1 == null) {
            throw new IllegalStateException("SKIPJACK engine not initialised");
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

    @Override
    public void reset() {
    }

    private int g(int n2, int n3) {
        int n4 = n3 >> 8 & 0xFF;
        int n5 = n3 & 0xFF;
        int n6 = ftable[n5 ^ this.key0[n2]] ^ n4;
        int n7 = ftable[n6 ^ this.key1[n2]] ^ n5;
        int n8 = ftable[n7 ^ this.key2[n2]] ^ n6;
        int n9 = ftable[n8 ^ this.key3[n2]] ^ n7;
        return (n8 << 8) + n9;
    }

    public int encryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        int n4 = (byArray[n2 + 0] << 8) + (byArray[n2 + 1] & 0xFF);
        int n5 = (byArray[n2 + 2] << 8) + (byArray[n2 + 3] & 0xFF);
        int n6 = (byArray[n2 + 4] << 8) + (byArray[n2 + 5] & 0xFF);
        int n7 = (byArray[n2 + 6] << 8) + (byArray[n2 + 7] & 0xFF);
        int n8 = 0;
        for (int i2 = 0; i2 < 2; ++i2) {
            int n9;
            int n10;
            for (n10 = 0; n10 < 8; ++n10) {
                n9 = n7;
                n7 = n6;
                n6 = n5;
                n5 = this.g(n8, n4);
                n4 = n5 ^ n9 ^ n8 + 1;
                ++n8;
            }
            for (n10 = 0; n10 < 8; ++n10) {
                n9 = n7;
                n7 = n6;
                n6 = n4 ^ n5 ^ n8 + 1;
                n5 = this.g(n8, n4);
                n4 = n9;
                ++n8;
            }
        }
        byArray2[n3 + 0] = (byte)(n4 >> 8);
        byArray2[n3 + 1] = (byte)n4;
        byArray2[n3 + 2] = (byte)(n5 >> 8);
        byArray2[n3 + 3] = (byte)n5;
        byArray2[n3 + 4] = (byte)(n6 >> 8);
        byArray2[n3 + 5] = (byte)n6;
        byArray2[n3 + 6] = (byte)(n7 >> 8);
        byArray2[n3 + 7] = (byte)n7;
        return 8;
    }

    private int h(int n2, int n3) {
        int n4 = n3 & 0xFF;
        int n5 = n3 >> 8 & 0xFF;
        int n6 = ftable[n5 ^ this.key3[n2]] ^ n4;
        int n7 = ftable[n6 ^ this.key2[n2]] ^ n5;
        int n8 = ftable[n7 ^ this.key1[n2]] ^ n6;
        int n9 = ftable[n8 ^ this.key0[n2]] ^ n7;
        return (n9 << 8) + n8;
    }

    public int decryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        int n4 = (byArray[n2 + 0] << 8) + (byArray[n2 + 1] & 0xFF);
        int n5 = (byArray[n2 + 2] << 8) + (byArray[n2 + 3] & 0xFF);
        int n6 = (byArray[n2 + 4] << 8) + (byArray[n2 + 5] & 0xFF);
        int n7 = (byArray[n2 + 6] << 8) + (byArray[n2 + 7] & 0xFF);
        int n8 = 31;
        for (int i2 = 0; i2 < 2; ++i2) {
            int n9;
            int n10;
            for (n10 = 0; n10 < 8; ++n10) {
                n9 = n6;
                n6 = n7;
                n7 = n4;
                n4 = this.h(n8, n5);
                n5 = n4 ^ n9 ^ n8 + 1;
                --n8;
            }
            for (n10 = 0; n10 < 8; ++n10) {
                n9 = n6;
                n6 = n7;
                n7 = n5 ^ n4 ^ n8 + 1;
                n4 = this.h(n8, n5);
                n5 = n9;
                --n8;
            }
        }
        byArray2[n3 + 0] = (byte)(n4 >> 8);
        byArray2[n3 + 1] = (byte)n4;
        byArray2[n3 + 2] = (byte)(n5 >> 8);
        byArray2[n3 + 3] = (byte)n5;
        byArray2[n3 + 4] = (byte)(n6 >> 8);
        byArray2[n3 + 5] = (byte)n6;
        byArray2[n3 + 6] = (byte)(n7 >> 8);
        byArray2[n3 + 7] = (byte)n7;
        return 8;
    }

    private CryptoServicePurpose getPurpose() {
        if (this.key0 == null) {
            return CryptoServicePurpose.ANY;
        }
        return this.encrypting ? CryptoServicePurpose.ENCRYPTION : CryptoServicePurpose.DECRYPTION;
    }
}

