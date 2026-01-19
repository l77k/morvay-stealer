/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.modes.CBCBlockCipher;

public class BlockCipherMac
implements Mac {
    private byte[] mac;
    private byte[] buf;
    private int bufOff;
    private BlockCipher cipher;
    private int macSize;

    public BlockCipherMac(BlockCipher blockCipher) {
        this(blockCipher, blockCipher.getBlockSize() * 8 / 2);
    }

    public BlockCipherMac(BlockCipher blockCipher, int n2) {
        if (n2 % 8 != 0) {
            throw new IllegalArgumentException("MAC size must be multiple of 8");
        }
        this.cipher = new CBCBlockCipher(blockCipher);
        this.macSize = n2 / 8;
        this.mac = new byte[blockCipher.getBlockSize()];
        this.buf = new byte[blockCipher.getBlockSize()];
        this.bufOff = 0;
    }

    @Override
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName();
    }

    @Override
    public void init(CipherParameters cipherParameters) {
        this.reset();
        this.cipher.init(true, cipherParameters);
    }

    @Override
    public int getMacSize() {
        return this.macSize;
    }

    @Override
    public void update(byte by) {
        if (this.bufOff == this.buf.length) {
            this.cipher.processBlock(this.buf, 0, this.mac, 0);
            this.bufOff = 0;
        }
        this.buf[this.bufOff++] = by;
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) {
        if (n3 < 0) {
            throw new IllegalArgumentException("Can't have a negative input length!");
        }
        int n4 = this.cipher.getBlockSize();
        int n5 = 0;
        int n6 = n4 - this.bufOff;
        if (n3 > n6) {
            System.arraycopy(byArray, n2, this.buf, this.bufOff, n6);
            n5 += this.cipher.processBlock(this.buf, 0, this.mac, 0);
            this.bufOff = 0;
            n3 -= n6;
            n2 += n6;
            while (n3 > n4) {
                n5 += this.cipher.processBlock(byArray, n2, this.mac, 0);
                n3 -= n4;
                n2 += n4;
            }
        }
        System.arraycopy(byArray, n2, this.buf, this.bufOff, n3);
        this.bufOff += n3;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) {
        int n3 = this.cipher.getBlockSize();
        while (this.bufOff < n3) {
            this.buf[this.bufOff] = 0;
            ++this.bufOff;
        }
        this.cipher.processBlock(this.buf, 0, this.mac, 0);
        System.arraycopy(this.mac, 0, byArray, n2, this.macSize);
        this.reset();
        return this.macSize;
    }

    @Override
    public void reset() {
        for (int i2 = 0; i2 < this.buf.length; ++i2) {
            this.buf[i2] = 0;
        }
        this.bufOff = 0;
        this.cipher.reset();
    }
}

