/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.engines.AEADBaseEngine;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Longs;

abstract class AsconBaseEngine
extends AEADBaseEngine {
    protected State m_state = State.Uninitialized;
    protected int nr;
    protected int ASCON_AEAD_RATE;
    protected long K0;
    protected long K1;
    protected long N0;
    protected long N1;
    protected long ASCON_IV;
    protected long x0;
    protected long x1;
    protected long x2;
    protected long x3;
    protected long x4;
    protected int m_bufferSizeDecrypt;
    protected byte[] m_buf;
    protected int m_bufPos = 0;
    protected long dsep;

    AsconBaseEngine() {
    }

    protected abstract long pad(int var1);

    protected abstract long loadBytes(byte[] var1, int var2);

    protected abstract void setBytes(long var1, byte[] var3, int var4);

    private void round(long l2) {
        long l3 = this.x0 ^ this.x1 ^ this.x2 ^ this.x3 ^ l2 ^ this.x1 & (this.x0 ^ this.x2 ^ this.x4 ^ l2);
        long l4 = this.x0 ^ this.x2 ^ this.x3 ^ this.x4 ^ l2 ^ (this.x1 ^ this.x2 ^ l2) & (this.x1 ^ this.x3);
        long l5 = this.x1 ^ this.x2 ^ this.x4 ^ l2 ^ this.x3 & this.x4;
        long l6 = this.x0 ^ this.x1 ^ this.x2 ^ l2 ^ (this.x0 ^ 0xFFFFFFFFFFFFFFFFL) & (this.x3 ^ this.x4);
        long l7 = this.x1 ^ this.x3 ^ this.x4 ^ (this.x0 ^ this.x4) & this.x1;
        this.x0 = l3 ^ Longs.rotateRight(l3, 19) ^ Longs.rotateRight(l3, 28);
        this.x1 = l4 ^ Longs.rotateRight(l4, 39) ^ Longs.rotateRight(l4, 61);
        this.x2 = l5 ^ Longs.rotateRight(l5, 1) ^ Longs.rotateRight(l5, 6) ^ 0xFFFFFFFFFFFFFFFFL;
        this.x3 = l6 ^ Longs.rotateRight(l6, 10) ^ Longs.rotateRight(l6, 17);
        this.x4 = l7 ^ Longs.rotateRight(l7, 7) ^ Longs.rotateRight(l7, 41);
    }

    protected void p(int n2) {
        if (n2 == 12) {
            this.round(240L);
            this.round(225L);
            this.round(210L);
            this.round(195L);
        }
        if (n2 >= 8) {
            this.round(180L);
            this.round(165L);
        }
        this.round(150L);
        this.round(135L);
        this.round(120L);
        this.round(105L);
        this.round(90L);
        this.round(75L);
    }

    protected abstract void ascon_aeadinit();

    protected void checkAAD() {
        switch (this.m_state.ordinal()) {
            case 5: {
                this.m_state = State.DecAad;
                break;
            }
            case 1: {
                this.m_state = State.EncAad;
                break;
            }
            case 2: 
            case 6: {
                break;
            }
            case 4: {
                throw new IllegalStateException(this.getAlgorithmName() + " cannot be reused for encryption");
            }
            default: {
                throw new IllegalStateException(this.getAlgorithmName() + " needs to be initialized");
            }
        }
    }

    protected boolean checkData() {
        switch (this.m_state.ordinal()) {
            case 5: 
            case 6: {
                this.finishAAD(State.DecData);
                return false;
            }
            case 1: 
            case 2: {
                this.finishAAD(State.EncData);
                return true;
            }
            case 7: {
                return false;
            }
            case 3: {
                return true;
            }
            case 4: {
                throw new IllegalStateException(this.getAlgorithmName() + " cannot be reused for encryption");
            }
        }
        throw new IllegalStateException(this.getAlgorithmName() + " needs to be initialized");
    }

    private void finishAAD(State state) {
        switch (this.m_state.ordinal()) {
            case 2: 
            case 6: {
                this.processFinalAadBlock();
                this.p(this.nr);
                break;
            }
        }
        this.x4 ^= this.dsep;
        this.m_bufPos = 0;
        this.m_state = state;
    }

    protected abstract void processFinalAadBlock();

    protected abstract void processFinalDecrypt(byte[] var1, int var2, byte[] var3, int var4);

    protected abstract void processFinalEncrypt(byte[] var1, int var2, byte[] var3, int var4);

    protected void processBufferAAD(byte[] byArray, int n2) {
        this.x0 ^= this.loadBytes(byArray, n2);
        if (this.ASCON_AEAD_RATE == 16) {
            this.x1 ^= this.loadBytes(byArray, 8 + n2);
        }
        this.p(this.nr);
    }

    protected void processBufferDecrypt(byte[] byArray, int n2, byte[] byArray2, int n3) {
        if (n3 + this.ASCON_AEAD_RATE > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        long l2 = this.loadBytes(byArray, n2);
        this.setBytes(this.x0 ^ l2, byArray2, n3);
        this.x0 = l2;
        if (this.ASCON_AEAD_RATE == 16) {
            long l3 = this.loadBytes(byArray, n2 + 8);
            this.setBytes(this.x1 ^ l3, byArray2, n3 + 8);
            this.x1 = l3;
        }
        this.p(this.nr);
    }

    protected void processBufferEncrypt(byte[] byArray, int n2, byte[] byArray2, int n3) {
        if (n3 + this.ASCON_AEAD_RATE > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        this.x0 ^= this.loadBytes(byArray, n2);
        this.setBytes(this.x0, byArray2, n3);
        if (this.ASCON_AEAD_RATE == 16) {
            this.x1 ^= this.loadBytes(byArray, n2 + 8);
            this.setBytes(this.x1, byArray2, n3 + 8);
        }
        this.p(this.nr);
    }

    @Override
    public void processAADByte(byte by) {
        this.checkAAD();
        this.m_buf[this.m_bufPos] = by;
        if (++this.m_bufPos == this.ASCON_AEAD_RATE) {
            this.processBufferAAD(this.m_buf, 0);
            this.m_bufPos = 0;
        }
    }

    @Override
    public void processAADBytes(byte[] byArray, int n2, int n3) {
        if (n2 + n3 > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 <= 0) {
            return;
        }
        this.checkAAD();
        if (this.m_bufPos > 0) {
            int n4 = this.ASCON_AEAD_RATE - this.m_bufPos;
            if (n3 < n4) {
                System.arraycopy(byArray, n2, this.m_buf, this.m_bufPos, n3);
                this.m_bufPos += n3;
                return;
            }
            System.arraycopy(byArray, n2, this.m_buf, this.m_bufPos, n4);
            n2 += n4;
            n3 -= n4;
            this.processBufferAAD(this.m_buf, 0);
        }
        while (n3 >= this.ASCON_AEAD_RATE) {
            this.processBufferAAD(byArray, n2);
            n2 += this.ASCON_AEAD_RATE;
            n3 -= this.ASCON_AEAD_RATE;
        }
        System.arraycopy(byArray, n2, this.m_buf, 0, n3);
        this.m_bufPos = n3;
    }

    @Override
    public int processBytes(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws DataLengthException {
        if (n2 + n3 > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        boolean bl = this.checkData();
        int n5 = 0;
        if (bl) {
            if (this.m_bufPos > 0) {
                int n6 = this.ASCON_AEAD_RATE - this.m_bufPos;
                if (n3 < n6) {
                    System.arraycopy(byArray, n2, this.m_buf, this.m_bufPos, n3);
                    this.m_bufPos += n3;
                    return 0;
                }
                System.arraycopy(byArray, n2, this.m_buf, this.m_bufPos, n6);
                n2 += n6;
                n3 -= n6;
                this.processBufferEncrypt(this.m_buf, 0, byArray2, n4);
                n5 = this.ASCON_AEAD_RATE;
            }
            while (n3 >= this.ASCON_AEAD_RATE) {
                this.processBufferEncrypt(byArray, n2, byArray2, n4 + n5);
                n2 += this.ASCON_AEAD_RATE;
                n3 -= this.ASCON_AEAD_RATE;
                n5 += this.ASCON_AEAD_RATE;
            }
        } else {
            int n7 = this.m_bufferSizeDecrypt - this.m_bufPos;
            if (n3 < n7) {
                System.arraycopy(byArray, n2, this.m_buf, this.m_bufPos, n3);
                this.m_bufPos += n3;
                return 0;
            }
            while (this.m_bufPos >= this.ASCON_AEAD_RATE) {
                this.processBufferDecrypt(this.m_buf, 0, byArray2, n4 + n5);
                this.m_bufPos -= this.ASCON_AEAD_RATE;
                System.arraycopy(this.m_buf, this.ASCON_AEAD_RATE, this.m_buf, 0, this.m_bufPos);
                n5 += this.ASCON_AEAD_RATE;
                if (n3 >= (n7 += this.ASCON_AEAD_RATE)) continue;
                System.arraycopy(byArray, n2, this.m_buf, this.m_bufPos, n3);
                this.m_bufPos += n3;
                return n5;
            }
            n7 = this.ASCON_AEAD_RATE - this.m_bufPos;
            System.arraycopy(byArray, n2, this.m_buf, this.m_bufPos, n7);
            n2 += n7;
            n3 -= n7;
            this.processBufferDecrypt(this.m_buf, 0, byArray2, n4 + n5);
            n5 += this.ASCON_AEAD_RATE;
            while (n3 >= this.m_bufferSizeDecrypt) {
                this.processBufferDecrypt(byArray, n2, byArray2, n4 + n5);
                n2 += this.ASCON_AEAD_RATE;
                n3 -= this.ASCON_AEAD_RATE;
                n5 += this.ASCON_AEAD_RATE;
            }
        }
        System.arraycopy(byArray, n2, this.m_buf, 0, n3);
        this.m_bufPos = n3;
        return n5;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) throws IllegalStateException, InvalidCipherTextException, DataLengthException {
        int n3;
        boolean bl = this.checkData();
        if (bl) {
            n3 = this.m_bufPos + this.MAC_SIZE;
            if (n2 + n3 > byArray.length) {
                throw new OutputLengthException("output buffer too short");
            }
            this.processFinalEncrypt(this.m_buf, this.m_bufPos, byArray, n2);
            this.mac = new byte[this.MAC_SIZE];
            this.setBytes(this.x3, this.mac, 0);
            this.setBytes(this.x4, this.mac, 8);
            System.arraycopy(this.mac, 0, byArray, n2 + this.m_bufPos, this.MAC_SIZE);
            this.reset(false);
        } else {
            if (this.m_bufPos < this.MAC_SIZE) {
                throw new InvalidCipherTextException("data too short");
            }
            this.m_bufPos -= this.MAC_SIZE;
            n3 = this.m_bufPos;
            if (n2 + n3 > byArray.length) {
                throw new OutputLengthException("output buffer too short");
            }
            this.processFinalDecrypt(this.m_buf, this.m_bufPos, byArray, n2);
            this.x3 ^= this.loadBytes(this.m_buf, this.m_bufPos);
            this.x4 ^= this.loadBytes(this.m_buf, this.m_bufPos + 8);
            if ((this.x3 | this.x4) != 0L) {
                throw new InvalidCipherTextException("mac check in " + this.getAlgorithmName() + " failed");
            }
            this.reset(true);
        }
        return n3;
    }

    @Override
    public int getUpdateOutputSize(int n2) {
        int n3 = Math.max(0, n2);
        switch (this.m_state.ordinal()) {
            case 5: 
            case 6: {
                n3 = Math.max(0, n3 - this.MAC_SIZE);
                break;
            }
            case 7: 
            case 8: {
                n3 = Math.max(0, n3 + this.m_bufPos - this.MAC_SIZE);
                break;
            }
            case 3: 
            case 4: {
                n3 += this.m_bufPos;
                break;
            }
        }
        return n3 - n3 % this.ASCON_AEAD_RATE;
    }

    @Override
    public int getOutputSize(int n2) {
        int n3 = Math.max(0, n2);
        switch (this.m_state.ordinal()) {
            case 5: 
            case 6: {
                return Math.max(0, n3 - this.MAC_SIZE);
            }
            case 7: 
            case 8: {
                return Math.max(0, n3 + this.m_bufPos - this.MAC_SIZE);
            }
            case 3: 
            case 4: {
                return n3 + this.m_bufPos + this.MAC_SIZE;
            }
        }
        return n3 + this.MAC_SIZE;
    }

    @Override
    protected void reset(boolean bl) {
        Arrays.clear(this.m_buf);
        this.m_bufPos = 0;
        switch (this.m_state.ordinal()) {
            case 1: 
            case 5: {
                break;
            }
            case 6: 
            case 7: 
            case 8: {
                this.m_state = State.DecInit;
                break;
            }
            case 2: 
            case 3: 
            case 4: {
                this.m_state = State.EncFinal;
                return;
            }
            default: {
                throw new IllegalStateException(this.getAlgorithmName() + " needs to be initialized");
            }
        }
        this.ascon_aeadinit();
        super.reset(bl);
    }

    public abstract String getAlgorithmVersion();

    protected static enum State {
        Uninitialized,
        EncInit,
        EncAad,
        EncData,
        EncFinal,
        DecInit,
        DecAad,
        DecData,
        DecFinal;

    }
}

