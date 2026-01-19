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

public class LEAEngine
implements BlockCipher {
    private static final int BASEROUNDS = 16;
    private static final int NUMWORDS = 4;
    private static final int NUMWORDS128 = 4;
    private static final int MASK128 = 3;
    private static final int NUMWORDS192 = 6;
    private static final int NUMWORDS256 = 8;
    private static final int MASK256 = 7;
    private static final int BLOCKSIZE = 16;
    private static final int KEY0 = 0;
    private static final int KEY1 = 1;
    private static final int KEY2 = 2;
    private static final int KEY3 = 3;
    private static final int KEY4 = 4;
    private static final int KEY5 = 5;
    private static final int ROT1 = 1;
    private static final int ROT3 = 3;
    private static final int ROT5 = 5;
    private static final int ROT6 = 6;
    private static final int ROT9 = 9;
    private static final int ROT11 = 11;
    private static final int ROT13 = 13;
    private static final int ROT17 = 17;
    private static final int[] DELTA = new int[]{-1007687205, 1147300610, 2044886154, 2027892972, 1902027934, -947529206, -531697110, -440137385};
    private final int[] theBlock = new int[4];
    private int theRounds;
    private int[][] theRoundKeys;
    private boolean forEncryption;

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("Invalid parameter passed to LEA init - " + cipherParameters.getClass().getName());
        }
        byte[] byArray = ((KeyParameter)cipherParameters).getKey();
        int n2 = byArray.length;
        if ((n2 << 1) % 16 != 0 || n2 < 16 || n2 > 32) {
            throw new IllegalArgumentException("KeyBitSize must be 128, 192 or 256");
        }
        this.forEncryption = bl;
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), n2 * 8, cipherParameters, Utils.getPurpose(this.forEncryption)));
        this.generateRoundKeys(byArray);
    }

    @Override
    public void reset() {
    }

    @Override
    public String getAlgorithmName() {
        return "LEA";
    }

    @Override
    public int getBlockSize() {
        return 16;
    }

    @Override
    public int processBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        LEAEngine.checkBuffer(byArray, n2, false);
        LEAEngine.checkBuffer(byArray2, n3, true);
        return this.forEncryption ? this.encryptBlock(byArray, n2, byArray2, n3) : this.decryptBlock(byArray, n2, byArray2, n3);
    }

    private static int bufLength(byte[] byArray) {
        return byArray == null ? 0 : byArray.length;
    }

    private static void checkBuffer(byte[] byArray, int n2, boolean bl) {
        boolean bl2;
        int n3 = LEAEngine.bufLength(byArray);
        int n4 = n2 + 16;
        boolean bl3 = bl2 = n2 < 0 || n4 < 0;
        if (bl2 || n4 > n3) {
            throw bl ? new OutputLengthException("Output buffer too short.") : new DataLengthException("Input buffer too short.");
        }
    }

    private int encryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        Pack.littleEndianToInt(byArray, n2, this.theBlock, 0, 4);
        for (int i2 = 0; i2 < this.theRounds; ++i2) {
            this.encryptRound(i2);
        }
        Pack.intToLittleEndian(this.theBlock, byArray2, n3);
        return 16;
    }

    private void encryptRound(int n2) {
        int[] nArray = this.theRoundKeys[n2];
        int n3 = (3 + n2) % 4;
        int n4 = LEAEngine.leftIndex(n3);
        this.theBlock[n3] = LEAEngine.ror32((this.theBlock[n4] ^ nArray[4]) + (this.theBlock[n3] ^ nArray[5]), 3);
        n3 = n4;
        n4 = LEAEngine.leftIndex(n3);
        this.theBlock[n3] = LEAEngine.ror32((this.theBlock[n4] ^ nArray[2]) + (this.theBlock[n3] ^ nArray[3]), 5);
        n3 = n4;
        n4 = LEAEngine.leftIndex(n3);
        this.theBlock[n3] = LEAEngine.rol32((this.theBlock[n4] ^ nArray[0]) + (this.theBlock[n3] ^ nArray[1]), 9);
    }

    private static int leftIndex(int n2) {
        return n2 == 0 ? 3 : n2 - 1;
    }

    private int decryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        Pack.littleEndianToInt(byArray, n2, this.theBlock, 0, 4);
        for (int i2 = this.theRounds - 1; i2 >= 0; --i2) {
            this.decryptRound(i2);
        }
        Pack.intToLittleEndian(this.theBlock, byArray2, n3);
        return 16;
    }

    private void decryptRound(int n2) {
        int[] nArray = this.theRoundKeys[n2];
        int n3 = n2 % 4;
        int n4 = LEAEngine.rightIndex(n3);
        this.theBlock[n4] = LEAEngine.ror32(this.theBlock[n4], 9) - (this.theBlock[n3] ^ nArray[0]) ^ nArray[1];
        n3 = n4;
        n4 = LEAEngine.rightIndex(n4);
        this.theBlock[n4] = LEAEngine.rol32(this.theBlock[n4], 5) - (this.theBlock[n3] ^ nArray[2]) ^ nArray[3];
        n3 = n4;
        n4 = LEAEngine.rightIndex(n4);
        this.theBlock[n4] = LEAEngine.rol32(this.theBlock[n4], 3) - (this.theBlock[n3] ^ nArray[4]) ^ nArray[5];
    }

    private static int rightIndex(int n2) {
        return n2 == 3 ? 0 : n2 + 1;
    }

    private void generateRoundKeys(byte[] byArray) {
        this.theRounds = (byArray.length >> 1) + 16;
        this.theRoundKeys = new int[this.theRounds][6];
        int n2 = byArray.length / 4;
        int[] nArray = new int[n2];
        Pack.littleEndianToInt(byArray, 0, nArray, 0, n2);
        switch (n2) {
            case 4: {
                this.generate128RoundKeys(nArray);
                break;
            }
            case 6: {
                this.generate192RoundKeys(nArray);
                break;
            }
            default: {
                this.generate256RoundKeys(nArray);
            }
        }
    }

    private void generate128RoundKeys(int[] nArray) {
        for (int i2 = 0; i2 < this.theRounds; ++i2) {
            int n2;
            int n3 = LEAEngine.rol32(DELTA[i2 & 3], i2);
            nArray[n2 = 0] = LEAEngine.rol32(nArray[n2++] + n3, 1);
            nArray[n2] = LEAEngine.rol32(nArray[n2] + LEAEngine.rol32(n3, n2++), 3);
            nArray[n2] = LEAEngine.rol32(nArray[n2] + LEAEngine.rol32(n3, n2++), 6);
            nArray[n2] = LEAEngine.rol32(nArray[n2] + LEAEngine.rol32(n3, n2), 11);
            int[] nArray2 = this.theRoundKeys[i2];
            nArray2[0] = nArray[0];
            nArray2[1] = nArray[1];
            nArray2[2] = nArray[2];
            nArray2[3] = nArray[1];
            nArray2[4] = nArray[3];
            nArray2[5] = nArray[1];
        }
    }

    private void generate192RoundKeys(int[] nArray) {
        for (int i2 = 0; i2 < this.theRounds; ++i2) {
            int n2;
            int n3 = LEAEngine.rol32(DELTA[i2 % 6], i2);
            nArray[n2 = 0] = LEAEngine.rol32(nArray[n2] + LEAEngine.rol32(n3, n2++), 1);
            nArray[n2] = LEAEngine.rol32(nArray[n2] + LEAEngine.rol32(n3, n2++), 3);
            nArray[n2] = LEAEngine.rol32(nArray[n2] + LEAEngine.rol32(n3, n2++), 6);
            nArray[n2] = LEAEngine.rol32(nArray[n2] + LEAEngine.rol32(n3, n2++), 11);
            nArray[n2] = LEAEngine.rol32(nArray[n2] + LEAEngine.rol32(n3, n2++), 13);
            nArray[n2] = LEAEngine.rol32(nArray[n2] + LEAEngine.rol32(n3, n2++), 17);
            System.arraycopy(nArray, 0, this.theRoundKeys[i2], 0, n2);
        }
    }

    private void generate256RoundKeys(int[] nArray) {
        int n2 = 0;
        for (int i2 = 0; i2 < this.theRounds; ++i2) {
            int n3 = LEAEngine.rol32(DELTA[i2 & 7], i2);
            int[] nArray2 = this.theRoundKeys[i2];
            int n4 = 0;
            nArray2[n4] = LEAEngine.rol32(nArray[n2 & 7] + n3, 1);
            nArray[n2++ & 7] = nArray2[n4++];
            nArray2[n4] = LEAEngine.rol32(nArray[n2 & 7] + LEAEngine.rol32(n3, n4), 3);
            nArray[n2++ & 7] = nArray2[n4++];
            nArray2[n4] = LEAEngine.rol32(nArray[n2 & 7] + LEAEngine.rol32(n3, n4), 6);
            nArray[n2++ & 7] = nArray2[n4++];
            nArray2[n4] = LEAEngine.rol32(nArray[n2 & 7] + LEAEngine.rol32(n3, n4), 11);
            nArray[n2++ & 7] = nArray2[n4++];
            nArray2[n4] = LEAEngine.rol32(nArray[n2 & 7] + LEAEngine.rol32(n3, n4), 13);
            nArray[n2++ & 7] = nArray2[n4++];
            nArray2[n4] = LEAEngine.rol32(nArray[n2 & 7] + LEAEngine.rol32(n3, n4), 17);
            nArray[n2++ & 7] = nArray2[n4];
        }
    }

    private static int rol32(int n2, int n3) {
        return n2 << n3 | n2 >>> 32 - n3;
    }

    private static int ror32(int n2, int n3) {
        return n2 >>> n3 | n2 << 32 - n3;
    }
}

