/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.MultiBlockCipher;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.StreamCipher;

public class BufferedBlockCipher {
    protected byte[] buf;
    protected int bufOff;
    protected boolean forEncryption;
    protected BlockCipher cipher;
    protected MultiBlockCipher mbCipher;
    protected boolean partialBlockOkay;
    protected boolean pgpCFB;

    BufferedBlockCipher() {
    }

    public BufferedBlockCipher(BlockCipher blockCipher) {
        this.cipher = blockCipher;
        if (blockCipher instanceof MultiBlockCipher) {
            this.mbCipher = (MultiBlockCipher)blockCipher;
            this.buf = new byte[this.mbCipher.getMultiBlockSize()];
        } else {
            this.mbCipher = null;
            this.buf = new byte[blockCipher.getBlockSize()];
        }
        this.bufOff = 0;
        String string = blockCipher.getAlgorithmName();
        int n2 = string.indexOf(47) + 1;
        boolean bl = this.pgpCFB = n2 > 0 && string.startsWith("PGP", n2);
        this.partialBlockOkay = this.pgpCFB || blockCipher instanceof StreamCipher ? true : n2 > 0 && string.startsWith("OpenPGP", n2);
    }

    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    public void init(boolean bl, CipherParameters cipherParameters) throws IllegalArgumentException {
        this.forEncryption = bl;
        this.reset();
        this.cipher.init(bl, cipherParameters);
    }

    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }

    public int getUpdateOutputSize(int n2) {
        int n3 = n2 + this.bufOff;
        int n4 = this.pgpCFB ? (this.forEncryption ? n3 % this.buf.length - (this.cipher.getBlockSize() + 2) : n3 % this.buf.length) : n3 % this.buf.length;
        return n3 - n4;
    }

    public int getOutputSize(int n2) {
        if (this.pgpCFB && this.forEncryption) {
            return n2 + this.bufOff + (this.cipher.getBlockSize() + 2);
        }
        return n2 + this.bufOff;
    }

    public int processByte(byte by, byte[] byArray, int n2) throws DataLengthException, IllegalStateException {
        int n3 = 0;
        this.buf[this.bufOff++] = by;
        if (this.bufOff == this.buf.length) {
            n3 = this.cipher.processBlock(this.buf, 0, byArray, n2);
            this.bufOff = 0;
        }
        return n3;
    }

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
            if (this.mbCipher != null) {
                int n9 = n3 / this.mbCipher.getMultiBlockSize();
                if (n9 > 0) {
                    n7 += this.mbCipher.processBlocks(byArray, n2, n9, byArray2, n4 + n7);
                    int n10 = n9 * this.mbCipher.getMultiBlockSize();
                    n3 -= n10;
                    n2 += n10;
                }
            } else {
                while (n3 > this.buf.length) {
                    n7 += this.cipher.processBlock(byArray, n2, byArray2, n4 + n7);
                    n3 -= n5;
                    n2 += n5;
                }
            }
        }
        System.arraycopy(byArray, n2, this.buf, this.bufOff, n3);
        this.bufOff += n3;
        if (this.bufOff == this.buf.length) {
            n7 += this.cipher.processBlock(this.buf, 0, byArray2, n4 + n7);
            this.bufOff = 0;
        }
        return n7;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int doFinal(byte[] byArray, int n2) throws DataLengthException, IllegalStateException, InvalidCipherTextException {
        try {
            int n3 = 0;
            if (n2 + this.bufOff > byArray.length) {
                throw new OutputLengthException("output buffer too short for doFinal()");
            }
            if (this.bufOff != 0) {
                if (!this.partialBlockOkay) {
                    throw new DataLengthException("data not block size aligned");
                }
                this.cipher.processBlock(this.buf, 0, this.buf, 0);
                n3 = this.bufOff;
                this.bufOff = 0;
                System.arraycopy(this.buf, 0, byArray, n2, n3);
            }
            int n4 = n3;
            return n4;
        }
        finally {
            this.reset();
        }
    }

    public void reset() {
        for (int i2 = 0; i2 < this.buf.length; ++i2) {
            this.buf[i2] = 0;
        }
        this.bufOff = 0;
        this.cipher.reset();
    }
}

