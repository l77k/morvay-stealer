/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;

public class BufferedAsymmetricBlockCipher {
    protected byte[] buf;
    protected int bufOff;
    private final AsymmetricBlockCipher cipher;

    public BufferedAsymmetricBlockCipher(AsymmetricBlockCipher asymmetricBlockCipher) {
        this.cipher = asymmetricBlockCipher;
    }

    public AsymmetricBlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    public int getBufferPosition() {
        return this.bufOff;
    }

    public void init(boolean bl, CipherParameters cipherParameters) {
        this.reset();
        this.cipher.init(bl, cipherParameters);
        this.buf = new byte[this.cipher.getInputBlockSize() + (bl ? 1 : 0)];
        this.bufOff = 0;
    }

    public int getInputBlockSize() {
        return this.cipher.getInputBlockSize();
    }

    public int getOutputBlockSize() {
        return this.cipher.getOutputBlockSize();
    }

    public void processByte(byte by) {
        if (this.bufOff >= this.buf.length) {
            throw new DataLengthException("attempt to process message too long for cipher");
        }
        this.buf[this.bufOff++] = by;
    }

    public void processBytes(byte[] byArray, int n2, int n3) {
        if (n3 == 0) {
            return;
        }
        if (n3 < 0) {
            throw new IllegalArgumentException("Can't have a negative input length!");
        }
        if (this.bufOff + n3 > this.buf.length) {
            throw new DataLengthException("attempt to process message too long for cipher");
        }
        System.arraycopy(byArray, n2, this.buf, this.bufOff, n3);
        this.bufOff += n3;
    }

    public byte[] doFinal() throws InvalidCipherTextException {
        byte[] byArray = this.cipher.processBlock(this.buf, 0, this.bufOff);
        this.reset();
        return byArray;
    }

    public void reset() {
        if (this.buf != null) {
            for (int i2 = 0; i2 < this.buf.length; ++i2) {
                this.buf[i2] = 0;
            }
        }
        this.bufOff = 0;
    }
}

