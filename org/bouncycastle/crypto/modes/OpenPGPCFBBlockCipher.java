/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;

public class OpenPGPCFBBlockCipher
implements BlockCipher {
    private byte[] IV;
    private byte[] FR;
    private byte[] FRE;
    private BlockCipher cipher;
    private int count;
    private int blockSize;
    private boolean forEncryption;

    public OpenPGPCFBBlockCipher(BlockCipher blockCipher) {
        this.cipher = blockCipher;
        this.blockSize = blockCipher.getBlockSize();
        this.IV = new byte[this.blockSize];
        this.FR = new byte[this.blockSize];
        this.FRE = new byte[this.blockSize];
    }

    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    @Override
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/OpenPGPCFB";
    }

    @Override
    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }

    @Override
    public int processBlock(byte[] byArray, int n2, byte[] byArray2, int n3) throws DataLengthException, IllegalStateException {
        return this.forEncryption ? this.encryptBlock(byArray, n2, byArray2, n3) : this.decryptBlock(byArray, n2, byArray2, n3);
    }

    @Override
    public void reset() {
        this.count = 0;
        System.arraycopy(this.IV, 0, this.FR, 0, this.FR.length);
        this.cipher.reset();
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) throws IllegalArgumentException {
        this.forEncryption = bl;
        this.reset();
        this.cipher.init(true, cipherParameters);
    }

    private byte encryptByte(byte by, int n2) {
        return (byte)(this.FRE[n2] ^ by);
    }

    private int encryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) throws DataLengthException, IllegalStateException {
        if (n2 + this.blockSize > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + this.blockSize > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this.count > this.blockSize) {
            this.FR[this.blockSize - 2] = byArray2[n3] = this.encryptByte(byArray[n2], this.blockSize - 2);
            byte by = this.encryptByte(byArray[n2 + 1], this.blockSize - 1);
            byArray2[n3 + 1] = by;
            this.FR[this.blockSize - 1] = by;
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            for (int i2 = 2; i2 < this.blockSize; ++i2) {
                byte by2 = this.encryptByte(byArray[n2 + i2], i2 - 2);
                byArray2[n3 + i2] = by2;
                this.FR[i2 - 2] = by2;
            }
        } else if (this.count == 0) {
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            for (int i3 = 0; i3 < this.blockSize; ++i3) {
                byte by = this.encryptByte(byArray[n2 + i3], i3);
                byArray2[n3 + i3] = by;
                this.FR[i3] = by;
            }
            this.count += this.blockSize;
        } else if (this.count == this.blockSize) {
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            byArray2[n3] = this.encryptByte(byArray[n2], 0);
            byArray2[n3 + 1] = this.encryptByte(byArray[n2 + 1], 1);
            System.arraycopy(this.FR, 2, this.FR, 0, this.blockSize - 2);
            System.arraycopy(byArray2, n3, this.FR, this.blockSize - 2, 2);
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            for (int i4 = 2; i4 < this.blockSize; ++i4) {
                byte by = this.encryptByte(byArray[n2 + i4], i4 - 2);
                byArray2[n3 + i4] = by;
                this.FR[i4 - 2] = by;
            }
            this.count += this.blockSize;
        }
        return this.blockSize;
    }

    private int decryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) throws DataLengthException, IllegalStateException {
        if (n2 + this.blockSize > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + this.blockSize > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this.count > this.blockSize) {
            byte by;
            this.FR[this.blockSize - 2] = by = byArray[n2];
            byArray2[n3] = this.encryptByte(by, this.blockSize - 2);
            this.FR[this.blockSize - 1] = by = byArray[n2 + 1];
            byArray2[n3 + 1] = this.encryptByte(by, this.blockSize - 1);
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            for (int i2 = 2; i2 < this.blockSize; ++i2) {
                this.FR[i2 - 2] = by = byArray[n2 + i2];
                byArray2[n3 + i2] = this.encryptByte(by, i2 - 2);
            }
        } else if (this.count == 0) {
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            for (int i3 = 0; i3 < this.blockSize; ++i3) {
                this.FR[i3] = byArray[n2 + i3];
                byArray2[n3 + i3] = this.encryptByte(byArray[n2 + i3], i3);
            }
            this.count += this.blockSize;
        } else if (this.count == this.blockSize) {
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            byte by = byArray[n2];
            byte by2 = byArray[n2 + 1];
            byArray2[n3] = this.encryptByte(by, 0);
            byArray2[n3 + 1] = this.encryptByte(by2, 1);
            System.arraycopy(this.FR, 2, this.FR, 0, this.blockSize - 2);
            this.FR[this.blockSize - 2] = by;
            this.FR[this.blockSize - 1] = by2;
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            for (int i4 = 2; i4 < this.blockSize; ++i4) {
                byte by3;
                this.FR[i4 - 2] = by3 = byArray[n2 + i4];
                byArray2[n3 + i4] = this.encryptByte(by3, i4 - 2);
            }
            this.count += this.blockSize;
        }
        return this.blockSize;
    }
}

