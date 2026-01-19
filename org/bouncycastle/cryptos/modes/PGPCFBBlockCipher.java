/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class PGPCFBBlockCipher
implements BlockCipher {
    private byte[] IV;
    private byte[] FR;
    private byte[] FRE;
    private byte[] tmp;
    private BlockCipher cipher;
    private int count;
    private int blockSize;
    private boolean forEncryption;
    private boolean inlineIv;

    public PGPCFBBlockCipher(BlockCipher blockCipher, boolean bl) {
        this.cipher = blockCipher;
        this.inlineIv = bl;
        this.blockSize = blockCipher.getBlockSize();
        this.IV = new byte[this.blockSize];
        this.FR = new byte[this.blockSize];
        this.FRE = new byte[this.blockSize];
        this.tmp = new byte[this.blockSize];
    }

    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    @Override
    public String getAlgorithmName() {
        if (this.inlineIv) {
            return this.cipher.getAlgorithmName() + "/PGPCFBwithIV";
        }
        return this.cipher.getAlgorithmName() + "/PGPCFB";
    }

    @Override
    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }

    @Override
    public int processBlock(byte[] byArray, int n2, byte[] byArray2, int n3) throws DataLengthException, IllegalStateException {
        if (this.inlineIv) {
            return this.forEncryption ? this.encryptBlockWithIV(byArray, n2, byArray2, n3) : this.decryptBlockWithIV(byArray, n2, byArray2, n3);
        }
        return this.forEncryption ? this.encryptBlock(byArray, n2, byArray2, n3) : this.decryptBlock(byArray, n2, byArray2, n3);
    }

    @Override
    public void reset() {
        this.count = 0;
        for (int i2 = 0; i2 != this.FR.length; ++i2) {
            this.FR[i2] = this.inlineIv ? (byte)0 : this.IV[i2];
        }
        this.cipher.reset();
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) throws IllegalArgumentException {
        this.forEncryption = bl;
        if (cipherParameters instanceof ParametersWithIV) {
            ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            byte[] byArray = parametersWithIV.getIV();
            if (byArray.length < this.IV.length) {
                System.arraycopy(byArray, 0, this.IV, this.IV.length - byArray.length, byArray.length);
                for (int i2 = 0; i2 < this.IV.length - byArray.length; ++i2) {
                    this.IV[i2] = 0;
                }
            } else {
                System.arraycopy(byArray, 0, this.IV, 0, this.IV.length);
            }
            this.reset();
            this.cipher.init(true, parametersWithIV.getParameters());
        } else {
            this.reset();
            this.cipher.init(true, cipherParameters);
        }
    }

    private byte encryptByte(byte by, int n2) {
        return (byte)(this.FRE[n2] ^ by);
    }

    private int encryptBlockWithIV(byte[] byArray, int n2, byte[] byArray2, int n3) throws DataLengthException, IllegalStateException {
        if (n2 + this.blockSize > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (this.count == 0) {
            int n4;
            if (n3 + 2 * this.blockSize + 2 > byArray2.length) {
                throw new OutputLengthException("output buffer too short");
            }
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            for (n4 = 0; n4 < this.blockSize; ++n4) {
                byArray2[n3 + n4] = this.encryptByte(this.IV[n4], n4);
            }
            System.arraycopy(byArray2, n3, this.FR, 0, this.blockSize);
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            byArray2[n3 + this.blockSize] = this.encryptByte(this.IV[this.blockSize - 2], 0);
            byArray2[n3 + this.blockSize + 1] = this.encryptByte(this.IV[this.blockSize - 1], 1);
            System.arraycopy(byArray2, n3 + 2, this.FR, 0, this.blockSize);
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            for (n4 = 0; n4 < this.blockSize; ++n4) {
                byArray2[n3 + this.blockSize + 2 + n4] = this.encryptByte(byArray[n2 + n4], n4);
            }
            System.arraycopy(byArray2, n3 + this.blockSize + 2, this.FR, 0, this.blockSize);
            this.count += 2 * this.blockSize + 2;
            return 2 * this.blockSize + 2;
        }
        if (this.count >= this.blockSize + 2) {
            if (n3 + this.blockSize > byArray2.length) {
                throw new OutputLengthException("output buffer too short");
            }
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            for (int i2 = 0; i2 < this.blockSize; ++i2) {
                byArray2[n3 + i2] = this.encryptByte(byArray[n2 + i2], i2);
            }
            System.arraycopy(byArray2, n3, this.FR, 0, this.blockSize);
        }
        return this.blockSize;
    }

    private int decryptBlockWithIV(byte[] byArray, int n2, byte[] byArray2, int n3) throws DataLengthException, IllegalStateException {
        if (n2 + this.blockSize > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + this.blockSize > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this.count == 0) {
            for (int i2 = 0; i2 < this.blockSize; ++i2) {
                this.FR[i2] = byArray[n2 + i2];
            }
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            this.count += this.blockSize;
            return 0;
        }
        if (this.count == this.blockSize) {
            System.arraycopy(byArray, n2, this.tmp, 0, this.blockSize);
            System.arraycopy(this.FR, 2, this.FR, 0, this.blockSize - 2);
            this.FR[this.blockSize - 2] = this.tmp[0];
            this.FR[this.blockSize - 1] = this.tmp[1];
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            for (int i3 = 0; i3 < this.blockSize - 2; ++i3) {
                byArray2[n3 + i3] = this.encryptByte(this.tmp[i3 + 2], i3);
            }
            System.arraycopy(this.tmp, 2, this.FR, 0, this.blockSize - 2);
            this.count += 2;
            return this.blockSize - 2;
        }
        if (this.count >= this.blockSize + 2) {
            System.arraycopy(byArray, n2, this.tmp, 0, this.blockSize);
            byArray2[n3 + 0] = this.encryptByte(this.tmp[0], this.blockSize - 2);
            byArray2[n3 + 1] = this.encryptByte(this.tmp[1], this.blockSize - 1);
            System.arraycopy(this.tmp, 0, this.FR, this.blockSize - 2, 2);
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            for (int i4 = 0; i4 < this.blockSize - 2; ++i4) {
                byArray2[n3 + i4 + 2] = this.encryptByte(this.tmp[i4 + 2], i4);
            }
            System.arraycopy(this.tmp, 2, this.FR, 0, this.blockSize - 2);
        }
        return this.blockSize;
    }

    private int encryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) throws DataLengthException, IllegalStateException {
        int n4;
        if (n2 + this.blockSize > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + this.blockSize > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        this.cipher.processBlock(this.FR, 0, this.FRE, 0);
        for (n4 = 0; n4 < this.blockSize; ++n4) {
            byArray2[n3 + n4] = this.encryptByte(byArray[n2 + n4], n4);
        }
        for (n4 = 0; n4 < this.blockSize; ++n4) {
            this.FR[n4] = byArray2[n3 + n4];
        }
        return this.blockSize;
    }

    private int decryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) throws DataLengthException, IllegalStateException {
        int n4;
        if (n2 + this.blockSize > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + this.blockSize > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        this.cipher.processBlock(this.FR, 0, this.FRE, 0);
        for (n4 = 0; n4 < this.blockSize; ++n4) {
            byArray2[n3 + n4] = this.encryptByte(byArray[n2 + n4], n4);
        }
        for (n4 = 0; n4 < this.blockSize; ++n4) {
            this.FR[n4] = byArray[n2 + n4];
        }
        return this.blockSize;
    }
}

