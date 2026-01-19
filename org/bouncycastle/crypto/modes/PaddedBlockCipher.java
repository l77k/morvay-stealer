/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DefaultBufferedBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.OutputLengthException;

public class PaddedBlockCipher
extends DefaultBufferedBlockCipher {
    public PaddedBlockCipher(BlockCipher blockCipher) {
        this.cipher = blockCipher;
        this.buf = new byte[blockCipher.getBlockSize()];
        this.bufOff = 0;
    }

    @Override
    public int getOutputSize(int n2) {
        int n3 = n2 + this.bufOff;
        int n4 = n3 % this.buf.length;
        if (n4 == 0) {
            if (this.forEncryption) {
                return n3 + this.buf.length;
            }
            return n3;
        }
        return n3 - n4 + this.buf.length;
    }

    @Override
    public int getUpdateOutputSize(int n2) {
        int n3 = n2 + this.bufOff;
        int n4 = n3 % this.buf.length;
        if (n4 == 0) {
            return n3 - this.buf.length;
        }
        return n3 - n4;
    }

    @Override
    public int processByte(byte by, byte[] byArray, int n2) throws DataLengthException, IllegalStateException {
        int n3 = 0;
        if (this.bufOff == this.buf.length) {
            n3 = this.cipher.processBlock(this.buf, 0, byArray, n2);
            this.bufOff = 0;
        }
        this.buf[this.bufOff++] = by;
        return n3;
    }

    @Override
    public int processBytes(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws DataLengthException, IllegalStateException {
        if (n3 < 0) {
            throw new IllegalArgumentException("Can't have a negative input length!");
        }
        int n5 = this.getBlockSize();
        int n6 = this.getUpdateOutputSize(n3);
        if (n6 > 0 && n4 + n6 > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        int n7 = 0;
        int n8 = this.buf.length - this.bufOff;
        if (n3 > n8) {
            System.arraycopy(byArray, n2, this.buf, this.bufOff, n8);
            n7 += this.cipher.processBlock(this.buf, 0, byArray2, n4);
            this.bufOff = 0;
            n3 -= n8;
            n2 += n8;
            while (n3 > this.buf.length) {
                n7 += this.cipher.processBlock(byArray, n2, byArray2, n4 + n7);
                n3 -= n5;
                n2 += n5;
            }
        }
        System.arraycopy(byArray, n2, this.buf, this.bufOff, n3);
        this.bufOff += n3;
        return n7;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) throws DataLengthException, IllegalStateException, InvalidCipherTextException {
        int n3 = this.cipher.getBlockSize();
        int n4 = 0;
        if (this.forEncryption) {
            if (this.bufOff == n3) {
                if (n2 + 2 * n3 > byArray.length) {
                    throw new OutputLengthException("output buffer too short");
                }
                n4 = this.cipher.processBlock(this.buf, 0, byArray, n2);
                this.bufOff = 0;
            }
            byte by = (byte)(n3 - this.bufOff);
            while (this.bufOff < n3) {
                this.buf[this.bufOff] = by;
                ++this.bufOff;
            }
            n4 += this.cipher.processBlock(this.buf, 0, byArray, n2 + n4);
        } else {
            if (this.bufOff != n3) {
                throw new DataLengthException("last block incomplete in decryption");
            }
            n4 = this.cipher.processBlock(this.buf, 0, this.buf, 0);
            this.bufOff = 0;
            int n5 = this.buf[n3 - 1] & 0xFF;
            if (n5 > n3) {
                throw new InvalidCipherTextException("pad block corrupted");
            }
            System.arraycopy(this.buf, 0, byArray, n2, n4 -= n5);
        }
        this.reset();
        return n4;
    }
}

