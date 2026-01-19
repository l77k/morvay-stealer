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
import org.bouncycastle.util.Pack;

public class AESLightEngine
implements BlockCipher {
    private static final byte[] S = new byte[]{99, 124, 119, 123, -14, 107, 111, -59, 48, 1, 103, 43, -2, -41, -85, 118, -54, -126, -55, 125, -6, 89, 71, -16, -83, -44, -94, -81, -100, -92, 114, -64, -73, -3, -109, 38, 54, 63, -9, -52, 52, -91, -27, -15, 113, -40, 49, 21, 4, -57, 35, -61, 24, -106, 5, -102, 7, 18, -128, -30, -21, 39, -78, 117, 9, -125, 44, 26, 27, 110, 90, -96, 82, 59, -42, -77, 41, -29, 47, -124, 83, -47, 0, -19, 32, -4, -79, 91, 106, -53, -66, 57, 74, 76, 88, -49, -48, -17, -86, -5, 67, 77, 51, -123, 69, -7, 2, 127, 80, 60, -97, -88, 81, -93, 64, -113, -110, -99, 56, -11, -68, -74, -38, 33, 16, -1, -13, -46, -51, 12, 19, -20, 95, -105, 68, 23, -60, -89, 126, 61, 100, 93, 25, 115, 96, -127, 79, -36, 34, 42, -112, -120, 70, -18, -72, 20, -34, 94, 11, -37, -32, 50, 58, 10, 73, 6, 36, 92, -62, -45, -84, 98, -111, -107, -28, 121, -25, -56, 55, 109, -115, -43, 78, -87, 108, 86, -12, -22, 101, 122, -82, 8, -70, 120, 37, 46, 28, -90, -76, -58, -24, -35, 116, 31, 75, -67, -117, -118, 112, 62, -75, 102, 72, 3, -10, 14, 97, 53, 87, -71, -122, -63, 29, -98, -31, -8, -104, 17, 105, -39, -114, -108, -101, 30, -121, -23, -50, 85, 40, -33, -116, -95, -119, 13, -65, -26, 66, 104, 65, -103, 45, 15, -80, 84, -69, 22};
    private static final byte[] Si = new byte[]{82, 9, 106, -43, 48, 54, -91, 56, -65, 64, -93, -98, -127, -13, -41, -5, 124, -29, 57, -126, -101, 47, -1, -121, 52, -114, 67, 68, -60, -34, -23, -53, 84, 123, -108, 50, -90, -62, 35, 61, -18, 76, -107, 11, 66, -6, -61, 78, 8, 46, -95, 102, 40, -39, 36, -78, 118, 91, -94, 73, 109, -117, -47, 37, 114, -8, -10, 100, -122, 104, -104, 22, -44, -92, 92, -52, 93, 101, -74, -110, 108, 112, 72, 80, -3, -19, -71, -38, 94, 21, 70, 87, -89, -115, -99, -124, -112, -40, -85, 0, -116, -68, -45, 10, -9, -28, 88, 5, -72, -77, 69, 6, -48, 44, 30, -113, -54, 63, 15, 2, -63, -81, -67, 3, 1, 19, -118, 107, 58, -111, 17, 65, 79, 103, -36, -22, -105, -14, -49, -50, -16, -76, -26, 115, -106, -84, 116, 34, -25, -83, 53, -123, -30, -7, 55, -24, 28, 117, -33, 110, 71, -15, 26, 113, 29, 41, -59, -119, 111, -73, 98, 14, -86, 24, -66, 27, -4, 86, 62, 75, -58, -46, 121, 32, -102, -37, -64, -2, 120, -51, 90, -12, 31, -35, -88, 51, -120, 7, -57, 49, -79, 18, 16, 89, 39, -128, -20, 95, 96, 81, 127, -87, 25, -75, 74, 13, 45, -27, 122, -97, -109, -55, -100, -17, -96, -32, 59, 77, -82, 42, -11, -80, -56, -21, -69, 60, -125, 83, -103, 97, 23, 43, 4, 126, -70, 119, -42, 38, -31, 105, 20, 99, 85, 33, 12, 125};
    private static final int[] rcon = new int[]{1, 2, 4, 8, 16, 32, 64, 128, 27, 54, 108, 216, 171, 77, 154, 47, 94, 188, 99, 198, 151, 53, 106, 212, 179, 125, 250, 239, 197, 145};
    private static final int m1 = -2139062144;
    private static final int m2 = 0x7F7F7F7F;
    private static final int m3 = 27;
    private static final int m4 = -1061109568;
    private static final int m5 = 0x3F3F3F3F;
    private int ROUNDS;
    private int[][] WorkingKey = null;
    private boolean forEncryption;
    private static final int BLOCK_SIZE = 16;

    private static int shift(int n2, int n3) {
        return n2 >>> n3 | n2 << -n3;
    }

    private static int FFmulX(int n2) {
        return (n2 & 0x7F7F7F7F) << 1 ^ ((n2 & 0x80808080) >>> 7) * 27;
    }

    private static int FFmulX2(int n2) {
        int n3 = (n2 & 0x3F3F3F3F) << 2;
        int n4 = n2 & 0xC0C0C0C0;
        n4 ^= n4 >>> 1;
        return n3 ^ n4 >>> 2 ^ n4 >>> 5;
    }

    private static int mcol(int n2) {
        int n3 = AESLightEngine.shift(n2, 8);
        int n4 = n2 ^ n3;
        return AESLightEngine.shift(n4, 16) ^ n3 ^ AESLightEngine.FFmulX(n4);
    }

    private static int inv_mcol(int n2) {
        int n3 = n2;
        int n4 = n3 ^ AESLightEngine.shift(n3, 8);
        n3 ^= AESLightEngine.FFmulX(n4);
        n4 ^= AESLightEngine.FFmulX2(n3);
        return n3 ^= n4 ^ AESLightEngine.shift(n4, 16);
    }

    private static int subWord(int n2) {
        return S[n2 & 0xFF] & 0xFF | (S[n2 >> 8 & 0xFF] & 0xFF) << 8 | (S[n2 >> 16 & 0xFF] & 0xFF) << 16 | S[n2 >> 24 & 0xFF] << 24;
    }

    private int[][] generateWorkingKey(byte[] byArray, boolean bl) {
        int n2;
        int n3;
        int n4 = byArray.length;
        if (n4 < 16 || n4 > 32 || (n4 & 7) != 0) {
            throw new IllegalArgumentException("Key length not 128/192/256 bits.");
        }
        int n5 = n4 >>> 2;
        this.ROUNDS = n5 + 6;
        int[][] nArray = new int[this.ROUNDS + 1][4];
        block0 : switch (n5) {
            case 4: {
                int n6;
                int n7;
                int n8;
                int n9;
                nArray[0][0] = n3 = Pack.littleEndianToInt(byArray, 0);
                nArray[0][1] = n2 = Pack.littleEndianToInt(byArray, 4);
                nArray[0][2] = n9 = Pack.littleEndianToInt(byArray, 8);
                nArray[0][3] = n8 = Pack.littleEndianToInt(byArray, 12);
                for (n7 = 1; n7 <= 10; ++n7) {
                    n6 = AESLightEngine.subWord(AESLightEngine.shift(n8, 8)) ^ rcon[n7 - 1];
                    nArray[n7][0] = n3 ^= n6;
                    nArray[n7][1] = n2 ^= n3;
                    nArray[n7][2] = n9 ^= n2;
                    nArray[n7][3] = n8 ^= n9;
                }
                break;
            }
            case 6: {
                int n10;
                int n8;
                int n9;
                nArray[0][0] = n3 = Pack.littleEndianToInt(byArray, 0);
                nArray[0][1] = n2 = Pack.littleEndianToInt(byArray, 4);
                nArray[0][2] = n9 = Pack.littleEndianToInt(byArray, 8);
                nArray[0][3] = n8 = Pack.littleEndianToInt(byArray, 12);
                int n7 = Pack.littleEndianToInt(byArray, 16);
                int n6 = Pack.littleEndianToInt(byArray, 20);
                int n11 = 1;
                int n12 = 1;
                while (true) {
                    nArray[n11][0] = n7;
                    nArray[n11][1] = n6;
                    n10 = AESLightEngine.subWord(AESLightEngine.shift(n6, 8)) ^ n12;
                    n12 <<= 1;
                    nArray[n11][2] = n3 ^= n10;
                    nArray[n11][3] = n2 ^= n3;
                    nArray[n11 + 1][0] = n9 ^= n2;
                    nArray[n11 + 1][1] = n8 ^= n9;
                    nArray[n11 + 1][2] = n7 ^= n8;
                    nArray[n11 + 1][3] = n6 ^= n7;
                    n10 = AESLightEngine.subWord(AESLightEngine.shift(n6, 8)) ^ n12;
                    n12 <<= 1;
                    nArray[n11 + 2][0] = n3 ^= n10;
                    nArray[n11 + 2][1] = n2 ^= n3;
                    nArray[n11 + 2][2] = n9 ^= n2;
                    nArray[n11 + 2][3] = n8 ^= n9;
                    if ((n11 += 3) >= 13) break block0;
                    n6 ^= (n7 ^= n8);
                }
            }
            case 8: {
                int n12;
                int n11;
                int n6;
                int n7;
                int n8;
                int n9;
                nArray[0][0] = n3 = Pack.littleEndianToInt(byArray, 0);
                nArray[0][1] = n2 = Pack.littleEndianToInt(byArray, 4);
                nArray[0][2] = n9 = Pack.littleEndianToInt(byArray, 8);
                nArray[0][3] = n8 = Pack.littleEndianToInt(byArray, 12);
                nArray[1][0] = n7 = Pack.littleEndianToInt(byArray, 16);
                nArray[1][1] = n6 = Pack.littleEndianToInt(byArray, 20);
                nArray[1][2] = n11 = Pack.littleEndianToInt(byArray, 24);
                nArray[1][3] = n12 = Pack.littleEndianToInt(byArray, 28);
                int n10 = 2;
                int n13 = 1;
                while (true) {
                    int n14 = AESLightEngine.subWord(AESLightEngine.shift(n12, 8)) ^ n13;
                    n13 <<= 1;
                    nArray[n10][0] = n3 ^= n14;
                    nArray[n10][1] = n2 ^= n3;
                    nArray[n10][2] = n9 ^= n2;
                    nArray[n10][3] = n8 ^= n9;
                    if (++n10 >= 15) break block0;
                    n14 = AESLightEngine.subWord(n8);
                    nArray[n10][0] = n7 ^= n14;
                    nArray[n10][1] = n6 ^= n7;
                    nArray[n10][2] = n11 ^= n6;
                    nArray[n10][3] = n12 ^= n11;
                    ++n10;
                }
            }
            default: {
                throw new IllegalStateException("Should never get here");
            }
        }
        if (!bl) {
            for (n3 = 1; n3 < this.ROUNDS; ++n3) {
                for (n2 = 0; n2 < 4; ++n2) {
                    nArray[n3][n2] = AESLightEngine.inv_mcol(nArray[n3][n2]);
                }
            }
        }
        return nArray;
    }

    public AESLightEngine() {
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), this.bitsOfSecurity()));
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.WorkingKey = this.generateWorkingKey(((KeyParameter)cipherParameters).getKey(), bl);
            this.forEncryption = bl;
            CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), this.bitsOfSecurity(), cipherParameters, Utils.getPurpose(bl)));
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to AES init - " + cipherParameters.getClass().getName());
    }

    @Override
    public String getAlgorithmName() {
        return "AES";
    }

    @Override
    public int getBlockSize() {
        return 16;
    }

    @Override
    public int processBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        if (this.WorkingKey == null) {
            throw new IllegalStateException("AES engine not initialised");
        }
        if (n2 > byArray.length - 16) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 > byArray2.length - 16) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this.forEncryption) {
            this.encryptBlock(byArray, n2, byArray2, n3, this.WorkingKey);
        } else {
            this.decryptBlock(byArray, n2, byArray2, n3, this.WorkingKey);
        }
        return 16;
    }

    @Override
    public void reset() {
    }

    private void encryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3, int[][] nArray) {
        int n4;
        int n5;
        int n6;
        int n7 = Pack.littleEndianToInt(byArray, n2 + 0);
        int n8 = Pack.littleEndianToInt(byArray, n2 + 4);
        int n9 = Pack.littleEndianToInt(byArray, n2 + 8);
        int n10 = Pack.littleEndianToInt(byArray, n2 + 12);
        int n11 = n7 ^ nArray[0][0];
        int n12 = n8 ^ nArray[0][1];
        int n13 = n9 ^ nArray[0][2];
        int n14 = 1;
        int n15 = n10 ^ nArray[0][3];
        while (n14 < this.ROUNDS - 1) {
            n6 = AESLightEngine.mcol(S[n11 & 0xFF] & 0xFF ^ (S[n12 >> 8 & 0xFF] & 0xFF) << 8 ^ (S[n13 >> 16 & 0xFF] & 0xFF) << 16 ^ S[n15 >> 24 & 0xFF] << 24) ^ nArray[n14][0];
            n5 = AESLightEngine.mcol(S[n12 & 0xFF] & 0xFF ^ (S[n13 >> 8 & 0xFF] & 0xFF) << 8 ^ (S[n15 >> 16 & 0xFF] & 0xFF) << 16 ^ S[n11 >> 24 & 0xFF] << 24) ^ nArray[n14][1];
            n4 = AESLightEngine.mcol(S[n13 & 0xFF] & 0xFF ^ (S[n15 >> 8 & 0xFF] & 0xFF) << 8 ^ (S[n11 >> 16 & 0xFF] & 0xFF) << 16 ^ S[n12 >> 24 & 0xFF] << 24) ^ nArray[n14][2];
            n15 = AESLightEngine.mcol(S[n15 & 0xFF] & 0xFF ^ (S[n11 >> 8 & 0xFF] & 0xFF) << 8 ^ (S[n12 >> 16 & 0xFF] & 0xFF) << 16 ^ S[n13 >> 24 & 0xFF] << 24) ^ nArray[n14++][3];
            n11 = AESLightEngine.mcol(S[n6 & 0xFF] & 0xFF ^ (S[n5 >> 8 & 0xFF] & 0xFF) << 8 ^ (S[n4 >> 16 & 0xFF] & 0xFF) << 16 ^ S[n15 >> 24 & 0xFF] << 24) ^ nArray[n14][0];
            n12 = AESLightEngine.mcol(S[n5 & 0xFF] & 0xFF ^ (S[n4 >> 8 & 0xFF] & 0xFF) << 8 ^ (S[n15 >> 16 & 0xFF] & 0xFF) << 16 ^ S[n6 >> 24 & 0xFF] << 24) ^ nArray[n14][1];
            n13 = AESLightEngine.mcol(S[n4 & 0xFF] & 0xFF ^ (S[n15 >> 8 & 0xFF] & 0xFF) << 8 ^ (S[n6 >> 16 & 0xFF] & 0xFF) << 16 ^ S[n5 >> 24 & 0xFF] << 24) ^ nArray[n14][2];
            n15 = AESLightEngine.mcol(S[n15 & 0xFF] & 0xFF ^ (S[n6 >> 8 & 0xFF] & 0xFF) << 8 ^ (S[n5 >> 16 & 0xFF] & 0xFF) << 16 ^ S[n4 >> 24 & 0xFF] << 24) ^ nArray[n14++][3];
        }
        n6 = AESLightEngine.mcol(S[n11 & 0xFF] & 0xFF ^ (S[n12 >> 8 & 0xFF] & 0xFF) << 8 ^ (S[n13 >> 16 & 0xFF] & 0xFF) << 16 ^ S[n15 >> 24 & 0xFF] << 24) ^ nArray[n14][0];
        n5 = AESLightEngine.mcol(S[n12 & 0xFF] & 0xFF ^ (S[n13 >> 8 & 0xFF] & 0xFF) << 8 ^ (S[n15 >> 16 & 0xFF] & 0xFF) << 16 ^ S[n11 >> 24 & 0xFF] << 24) ^ nArray[n14][1];
        n4 = AESLightEngine.mcol(S[n13 & 0xFF] & 0xFF ^ (S[n15 >> 8 & 0xFF] & 0xFF) << 8 ^ (S[n11 >> 16 & 0xFF] & 0xFF) << 16 ^ S[n12 >> 24 & 0xFF] << 24) ^ nArray[n14][2];
        n15 = AESLightEngine.mcol(S[n15 & 0xFF] & 0xFF ^ (S[n11 >> 8 & 0xFF] & 0xFF) << 8 ^ (S[n12 >> 16 & 0xFF] & 0xFF) << 16 ^ S[n13 >> 24 & 0xFF] << 24) ^ nArray[n14++][3];
        n7 = S[n6 & 0xFF] & 0xFF ^ (S[n5 >> 8 & 0xFF] & 0xFF) << 8 ^ (S[n4 >> 16 & 0xFF] & 0xFF) << 16 ^ S[n15 >> 24 & 0xFF] << 24 ^ nArray[n14][0];
        n8 = S[n5 & 0xFF] & 0xFF ^ (S[n4 >> 8 & 0xFF] & 0xFF) << 8 ^ (S[n15 >> 16 & 0xFF] & 0xFF) << 16 ^ S[n6 >> 24 & 0xFF] << 24 ^ nArray[n14][1];
        n9 = S[n4 & 0xFF] & 0xFF ^ (S[n15 >> 8 & 0xFF] & 0xFF) << 8 ^ (S[n6 >> 16 & 0xFF] & 0xFF) << 16 ^ S[n5 >> 24 & 0xFF] << 24 ^ nArray[n14][2];
        n10 = S[n15 & 0xFF] & 0xFF ^ (S[n6 >> 8 & 0xFF] & 0xFF) << 8 ^ (S[n5 >> 16 & 0xFF] & 0xFF) << 16 ^ S[n4 >> 24 & 0xFF] << 24 ^ nArray[n14][3];
        Pack.intToLittleEndian(n7, byArray2, n3 + 0);
        Pack.intToLittleEndian(n8, byArray2, n3 + 4);
        Pack.intToLittleEndian(n9, byArray2, n3 + 8);
        Pack.intToLittleEndian(n10, byArray2, n3 + 12);
    }

    private void decryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3, int[][] nArray) {
        int n4;
        int n5;
        int n6;
        int n7 = Pack.littleEndianToInt(byArray, n2 + 0);
        int n8 = Pack.littleEndianToInt(byArray, n2 + 4);
        int n9 = Pack.littleEndianToInt(byArray, n2 + 8);
        int n10 = Pack.littleEndianToInt(byArray, n2 + 12);
        int n11 = n7 ^ nArray[this.ROUNDS][0];
        int n12 = n8 ^ nArray[this.ROUNDS][1];
        int n13 = n9 ^ nArray[this.ROUNDS][2];
        int n14 = this.ROUNDS - 1;
        int n15 = n10 ^ nArray[this.ROUNDS][3];
        while (n14 > 1) {
            n6 = AESLightEngine.inv_mcol(Si[n11 & 0xFF] & 0xFF ^ (Si[n15 >> 8 & 0xFF] & 0xFF) << 8 ^ (Si[n13 >> 16 & 0xFF] & 0xFF) << 16 ^ Si[n12 >> 24 & 0xFF] << 24) ^ nArray[n14][0];
            n5 = AESLightEngine.inv_mcol(Si[n12 & 0xFF] & 0xFF ^ (Si[n11 >> 8 & 0xFF] & 0xFF) << 8 ^ (Si[n15 >> 16 & 0xFF] & 0xFF) << 16 ^ Si[n13 >> 24 & 0xFF] << 24) ^ nArray[n14][1];
            n4 = AESLightEngine.inv_mcol(Si[n13 & 0xFF] & 0xFF ^ (Si[n12 >> 8 & 0xFF] & 0xFF) << 8 ^ (Si[n11 >> 16 & 0xFF] & 0xFF) << 16 ^ Si[n15 >> 24 & 0xFF] << 24) ^ nArray[n14][2];
            n15 = AESLightEngine.inv_mcol(Si[n15 & 0xFF] & 0xFF ^ (Si[n13 >> 8 & 0xFF] & 0xFF) << 8 ^ (Si[n12 >> 16 & 0xFF] & 0xFF) << 16 ^ Si[n11 >> 24 & 0xFF] << 24) ^ nArray[n14--][3];
            n11 = AESLightEngine.inv_mcol(Si[n6 & 0xFF] & 0xFF ^ (Si[n15 >> 8 & 0xFF] & 0xFF) << 8 ^ (Si[n4 >> 16 & 0xFF] & 0xFF) << 16 ^ Si[n5 >> 24 & 0xFF] << 24) ^ nArray[n14][0];
            n12 = AESLightEngine.inv_mcol(Si[n5 & 0xFF] & 0xFF ^ (Si[n6 >> 8 & 0xFF] & 0xFF) << 8 ^ (Si[n15 >> 16 & 0xFF] & 0xFF) << 16 ^ Si[n4 >> 24 & 0xFF] << 24) ^ nArray[n14][1];
            n13 = AESLightEngine.inv_mcol(Si[n4 & 0xFF] & 0xFF ^ (Si[n5 >> 8 & 0xFF] & 0xFF) << 8 ^ (Si[n6 >> 16 & 0xFF] & 0xFF) << 16 ^ Si[n15 >> 24 & 0xFF] << 24) ^ nArray[n14][2];
            n15 = AESLightEngine.inv_mcol(Si[n15 & 0xFF] & 0xFF ^ (Si[n4 >> 8 & 0xFF] & 0xFF) << 8 ^ (Si[n5 >> 16 & 0xFF] & 0xFF) << 16 ^ Si[n6 >> 24 & 0xFF] << 24) ^ nArray[n14--][3];
        }
        n6 = AESLightEngine.inv_mcol(Si[n11 & 0xFF] & 0xFF ^ (Si[n15 >> 8 & 0xFF] & 0xFF) << 8 ^ (Si[n13 >> 16 & 0xFF] & 0xFF) << 16 ^ Si[n12 >> 24 & 0xFF] << 24) ^ nArray[n14][0];
        n5 = AESLightEngine.inv_mcol(Si[n12 & 0xFF] & 0xFF ^ (Si[n11 >> 8 & 0xFF] & 0xFF) << 8 ^ (Si[n15 >> 16 & 0xFF] & 0xFF) << 16 ^ Si[n13 >> 24 & 0xFF] << 24) ^ nArray[n14][1];
        n4 = AESLightEngine.inv_mcol(Si[n13 & 0xFF] & 0xFF ^ (Si[n12 >> 8 & 0xFF] & 0xFF) << 8 ^ (Si[n11 >> 16 & 0xFF] & 0xFF) << 16 ^ Si[n15 >> 24 & 0xFF] << 24) ^ nArray[n14][2];
        n15 = AESLightEngine.inv_mcol(Si[n15 & 0xFF] & 0xFF ^ (Si[n13 >> 8 & 0xFF] & 0xFF) << 8 ^ (Si[n12 >> 16 & 0xFF] & 0xFF) << 16 ^ Si[n11 >> 24 & 0xFF] << 24) ^ nArray[n14][3];
        n7 = Si[n6 & 0xFF] & 0xFF ^ (Si[n15 >> 8 & 0xFF] & 0xFF) << 8 ^ (Si[n4 >> 16 & 0xFF] & 0xFF) << 16 ^ Si[n5 >> 24 & 0xFF] << 24 ^ nArray[0][0];
        n8 = Si[n5 & 0xFF] & 0xFF ^ (Si[n6 >> 8 & 0xFF] & 0xFF) << 8 ^ (Si[n15 >> 16 & 0xFF] & 0xFF) << 16 ^ Si[n4 >> 24 & 0xFF] << 24 ^ nArray[0][1];
        n9 = Si[n4 & 0xFF] & 0xFF ^ (Si[n5 >> 8 & 0xFF] & 0xFF) << 8 ^ (Si[n6 >> 16 & 0xFF] & 0xFF) << 16 ^ Si[n15 >> 24 & 0xFF] << 24 ^ nArray[0][2];
        n10 = Si[n15 & 0xFF] & 0xFF ^ (Si[n4 >> 8 & 0xFF] & 0xFF) << 8 ^ (Si[n5 >> 16 & 0xFF] & 0xFF) << 16 ^ Si[n6 >> 24 & 0xFF] << 24 ^ nArray[0][3];
        Pack.intToLittleEndian(n7, byArray2, n3 + 0);
        Pack.intToLittleEndian(n8, byArray2, n3 + 4);
        Pack.intToLittleEndian(n9, byArray2, n3 + 8);
        Pack.intToLittleEndian(n10, byArray2, n3 + 12);
    }

    private int bitsOfSecurity() {
        if (this.WorkingKey == null) {
            return 256;
        }
        return this.WorkingKey.length - 7 << 5;
    }
}

