/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.engines.DSTU7624Engine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Arrays;

public class DSTU7624Mac
implements Mac {
    private static final int BITS_IN_BYTE = 8;
    private byte[] buf;
    private int bufOff;
    private int macSize;
    private int blockSize;
    private DSTU7624Engine engine;
    private byte[] c;
    private byte[] cTemp;
    private byte[] kDelta;
    private boolean initCalled = false;

    public DSTU7624Mac(int n2, int n3) {
        this.engine = new DSTU7624Engine(n2);
        this.blockSize = n2 / 8;
        this.macSize = n3 / 8;
        this.c = new byte[this.blockSize];
        this.kDelta = new byte[this.blockSize];
        this.cTemp = new byte[this.blockSize];
        this.buf = new byte[this.blockSize];
    }

    @Override
    public void init(CipherParameters cipherParameters) throws IllegalArgumentException {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("Invalid parameter passed to DSTU7624Mac");
        }
        this.engine.init(true, cipherParameters);
        this.initCalled = true;
        this.reset();
    }

    @Override
    public String getAlgorithmName() {
        return "DSTU7624Mac";
    }

    @Override
    public int getMacSize() {
        return this.macSize;
    }

    @Override
    public void update(byte by) {
        if (this.bufOff == this.buf.length) {
            this.processBlock(this.buf, 0);
            this.bufOff = 0;
        }
        this.buf[this.bufOff++] = by;
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) {
        if (n3 < 0) {
            throw new IllegalArgumentException("can't have a negative input length!");
        }
        int n4 = this.engine.getBlockSize();
        int n5 = n4 - this.bufOff;
        if (n3 > n5) {
            System.arraycopy(byArray, n2, this.buf, this.bufOff, n5);
            this.processBlock(this.buf, 0);
            this.bufOff = 0;
            n3 -= n5;
            n2 += n5;
            while (n3 > n4) {
                this.processBlock(byArray, n2);
                n3 -= n4;
                n2 += n4;
            }
        }
        System.arraycopy(byArray, n2, this.buf, this.bufOff, n3);
        this.bufOff += n3;
    }

    private void processBlock(byte[] byArray, int n2) {
        this.xor(this.c, 0, byArray, n2, this.cTemp);
        this.engine.processBlock(this.cTemp, 0, this.c, 0);
    }

    @Override
    public int doFinal(byte[] byArray, int n2) throws DataLengthException, IllegalStateException {
        if (this.bufOff % this.buf.length != 0) {
            throw new DataLengthException("input must be a multiple of blocksize");
        }
        this.xor(this.c, 0, this.buf, 0, this.cTemp);
        this.xor(this.cTemp, 0, this.kDelta, 0, this.c);
        this.engine.processBlock(this.c, 0, this.c, 0);
        if (this.macSize + n2 > byArray.length) {
            throw new OutputLengthException("output buffer too short");
        }
        System.arraycopy(this.c, 0, byArray, n2, this.macSize);
        this.reset();
        return this.macSize;
    }

    @Override
    public void reset() {
        Arrays.fill(this.c, (byte)0);
        Arrays.fill(this.cTemp, (byte)0);
        Arrays.fill(this.kDelta, (byte)0);
        Arrays.fill(this.buf, (byte)0);
        this.engine.reset();
        if (this.initCalled) {
            this.engine.processBlock(this.kDelta, 0, this.kDelta, 0);
        }
        this.bufOff = 0;
    }

    private void xor(byte[] byArray, int n2, byte[] byArray2, int n3, byte[] byArray3) {
        if (byArray.length - n2 < this.blockSize || byArray2.length - n3 < this.blockSize || byArray3.length < this.blockSize) {
            throw new IllegalArgumentException("some of input buffers too short");
        }
        for (int i2 = 0; i2 < this.blockSize; ++i2) {
            byArray3[i2] = (byte)(byArray[i2 + n2] ^ byArray2[i2 + n3]);
        }
    }
}

