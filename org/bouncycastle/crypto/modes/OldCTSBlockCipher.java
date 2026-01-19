/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DefaultBufferedBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.modes.OFBBlockCipher;

public class OldCTSBlockCipher
extends DefaultBufferedBlockCipher {
    private int blockSize;

    public OldCTSBlockCipher(BlockCipher blockCipher) {
        if (blockCipher instanceof OFBBlockCipher || blockCipher instanceof CFBBlockCipher) {
            throw new IllegalArgumentException("CTSBlockCipher can only accept ECB, or CBC ciphers");
        }
        this.cipher = blockCipher;
        this.blockSize = blockCipher.getBlockSize();
        this.buf = new byte[this.blockSize * 2];
        this.bufOff = 0;
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
    public int getOutputSize(int n2) {
        return n2 + this.bufOff;
    }

    @Override
    public int processByte(byte by, byte[] byArray, int n2) throws DataLengthException, IllegalStateException {
        int n3 = 0;
        if (this.bufOff == this.buf.length) {
            n3 = this.cipher.processBlock(this.buf, 0, byArray, n2);
            System.arraycopy(this.buf, this.blockSize, this.buf, 0, this.blockSize);
            this.bufOff = this.blockSize;
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
            System.arraycopy(this.buf, n5, this.buf, 0, n5);
            this.bufOff = n5;
            n3 -= n8;
            n2 += n8;
            while (n3 > n5) {
                System.arraycopy(byArray, n2, this.buf, this.bufOff, n5);
                n7 += this.cipher.processBlock(this.buf, 0, byArray2, n4 + n7);
                System.arraycopy(this.buf, n5, this.buf, 0, n5);
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
        if (this.bufOff + n2 > byArray.length) {
            throw new OutputLengthException("output buffer to small in doFinal");
        }
        int n3 = this.cipher.getBlockSize();
        int n4 = this.bufOff - n3;
        byte[] byArray2 = new byte[n3];
        if (this.forEncryption) {
            int n5;
            this.cipher.processBlock(this.buf, 0, byArray2, 0);
            if (this.bufOff < n3) {
                throw new DataLengthException("need at least one block of input for CTS");
            }
            for (n5 = this.bufOff; n5 != this.buf.length; ++n5) {
                this.buf[n5] = byArray2[n5 - n3];
            }
            for (n5 = n3; n5 != this.bufOff; ++n5) {
                int n6 = n5;
                this.buf[n6] = (byte)(this.buf[n6] ^ byArray2[n5 - n3]);
            }
            if (this.cipher instanceof CBCBlockCipher) {
                BlockCipher blockCipher = ((CBCBlockCipher)this.cipher).getUnderlyingCipher();
                blockCipher.processBlock(this.buf, n3, byArray, n2);
            } else {
                this.cipher.processBlock(this.buf, n3, byArray, n2);
            }
            System.arraycopy(byArray2, 0, byArray, n2 + n3, n4);
        } else {
            byte[] byArray3 = new byte[n3];
            if (this.cipher instanceof CBCBlockCipher) {
                BlockCipher blockCipher = ((CBCBlockCipher)this.cipher).getUnderlyingCipher();
                blockCipher.processBlock(this.buf, 0, byArray2, 0);
            } else {
                this.cipher.processBlock(this.buf, 0, byArray2, 0);
            }
            for (int i2 = n3; i2 != this.bufOff; ++i2) {
                byArray3[i2 - n3] = (byte)(byArray2[i2 - n3] ^ this.buf[i2]);
            }
            System.arraycopy(this.buf, n3, byArray2, 0, n4);
            this.cipher.processBlock(byArray2, 0, byArray, n2);
            System.arraycopy(byArray3, 0, byArray, n2 + n3, n4);
        }
        int n7 = this.bufOff;
        this.reset();
        return n7;
    }
}

