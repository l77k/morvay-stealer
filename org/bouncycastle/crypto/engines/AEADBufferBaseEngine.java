/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.engines.AEADBaseEngine;
import org.bouncycastle.util.Arrays;

abstract class AEADBufferBaseEngine
extends AEADBaseEngine {
    protected byte[] m_buf;
    protected byte[] m_aad;
    protected int m_bufPos;
    protected int m_aadPos;
    protected boolean aadFinished;
    protected boolean initialised = false;
    protected int AADBufferSize;
    protected int BlockSize;
    protected State m_state = State.Uninitialized;

    AEADBufferBaseEngine() {
    }

    @Override
    public void processAADByte(byte by) {
        this.checkAAD();
        if (this.m_aadPos == this.AADBufferSize) {
            this.processBufferAAD(this.m_aad, 0);
            this.m_aadPos = 0;
        }
        this.m_aad[this.m_aadPos++] = by;
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
        if (this.m_aadPos > 0) {
            int n4 = this.AADBufferSize - this.m_aadPos;
            if (n3 <= n4) {
                System.arraycopy(byArray, n2, this.m_aad, this.m_aadPos, n3);
                this.m_aadPos += n3;
                return;
            }
            System.arraycopy(byArray, n2, this.m_aad, this.m_aadPos, n4);
            n2 += n4;
            n3 -= n4;
            this.processBufferAAD(this.m_aad, 0);
            this.m_aadPos = 0;
        }
        while (n3 > this.AADBufferSize) {
            this.processBufferAAD(byArray, n2);
            n2 += this.AADBufferSize;
            n3 -= this.AADBufferSize;
        }
        System.arraycopy(byArray, n2, this.m_aad, this.m_aadPos, n3);
        this.m_aadPos += n3;
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
                int n6 = this.BlockSize - this.m_bufPos;
                if (n3 <= n6) {
                    System.arraycopy(byArray, n2, this.m_buf, this.m_bufPos, n3);
                    this.m_bufPos += n3;
                    return 0;
                }
                System.arraycopy(byArray, n2, this.m_buf, this.m_bufPos, n6);
                n2 += n6;
                n3 -= n6;
                this.validateAndProcessBuffer(this.m_buf, 0, byArray2, n4);
                n5 = this.BlockSize;
            }
            while (n3 > this.BlockSize) {
                this.validateAndProcessBuffer(byArray, n2, byArray2, n4 + n5);
                n2 += this.BlockSize;
                n3 -= this.BlockSize;
                n5 += this.BlockSize;
            }
        } else {
            int n7 = this.BlockSize + this.MAC_SIZE - this.m_bufPos;
            if (n3 <= n7) {
                System.arraycopy(byArray, n2, this.m_buf, this.m_bufPos, n3);
                this.m_bufPos += n3;
                return 0;
            }
            if (this.BlockSize >= this.MAC_SIZE) {
                if (this.m_bufPos > this.BlockSize) {
                    this.validateAndProcessBuffer(this.m_buf, 0, byArray2, n4);
                    this.m_bufPos -= this.BlockSize;
                    System.arraycopy(this.m_buf, this.BlockSize, this.m_buf, 0, this.m_bufPos);
                    n5 = this.BlockSize;
                    if (n3 <= (n7 += this.BlockSize)) {
                        System.arraycopy(byArray, n2, this.m_buf, this.m_bufPos, n3);
                        this.m_bufPos += n3;
                        return n5;
                    }
                }
                n7 = this.BlockSize - this.m_bufPos;
                System.arraycopy(byArray, n2, this.m_buf, this.m_bufPos, n7);
                n2 += n7;
                n3 -= n7;
                this.validateAndProcessBuffer(this.m_buf, 0, byArray2, n4 + n5);
                n5 += this.BlockSize;
            } else {
                while (this.m_bufPos > this.BlockSize && n3 + this.m_bufPos > this.BlockSize + this.MAC_SIZE) {
                    this.validateAndProcessBuffer(this.m_buf, n5, byArray2, n4 + n5);
                    this.m_bufPos -= this.BlockSize;
                    n5 += this.BlockSize;
                }
                if (this.m_bufPos != 0) {
                    System.arraycopy(this.m_buf, n5, this.m_buf, 0, this.m_bufPos);
                    if (this.m_bufPos + n3 > this.BlockSize + this.MAC_SIZE) {
                        n7 = Math.max(this.BlockSize - this.m_bufPos, 0);
                        System.arraycopy(byArray, n2, this.m_buf, this.m_bufPos, n7);
                        n2 += n7;
                        this.validateAndProcessBuffer(this.m_buf, 0, byArray2, n4 + n5);
                        n5 += this.BlockSize;
                        n3 -= n7;
                    } else {
                        System.arraycopy(byArray, n2, this.m_buf, this.m_bufPos, n3);
                        this.m_bufPos += n3;
                        return n5;
                    }
                }
            }
            while (n3 > this.BlockSize + this.MAC_SIZE) {
                this.validateAndProcessBuffer(byArray, n2, byArray2, n4 + n5);
                n2 += this.BlockSize;
                n3 -= this.BlockSize;
                n5 += this.BlockSize;
            }
        }
        System.arraycopy(byArray, n2, this.m_buf, 0, n3);
        this.m_bufPos = n3;
        return n5;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) throws IllegalStateException, InvalidCipherTextException {
        int n3;
        boolean bl = this.checkData();
        if (bl) {
            n3 = this.m_bufPos + this.MAC_SIZE;
        } else {
            if (this.m_bufPos < this.MAC_SIZE) {
                throw new InvalidCipherTextException("data too short");
            }
            this.m_bufPos -= this.MAC_SIZE;
            n3 = this.m_bufPos;
        }
        if (n2 > byArray.length - n3) {
            throw new OutputLengthException("output buffer too short");
        }
        this.processFinalBlock(byArray, n2);
        if (bl) {
            System.arraycopy(this.mac, 0, byArray, n2 + n3 - this.MAC_SIZE, this.MAC_SIZE);
        } else if (!Arrays.constantTimeAreEqual(this.MAC_SIZE, this.mac, 0, this.m_buf, this.m_bufPos)) {
            throw new InvalidCipherTextException(this.algorithmName + " mac does not match");
        }
        this.reset(!bl);
        return n3;
    }

    public int getBlockSize() {
        return this.BlockSize;
    }

    @Override
    public int getUpdateOutputSize(int n2) {
        int n3 = Math.max(0, n2) - 1;
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
                n3 = Math.max(0, n3 + this.m_bufPos);
                break;
            }
        }
        return n3 - n3 % this.BlockSize;
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
                this.processFinalAAD();
                break;
            }
        }
        this.m_aadPos = 0;
        this.m_state = state;
    }

    protected void bufferReset() {
        Arrays.fill(this.m_buf, (byte)0);
        Arrays.fill(this.m_aad, (byte)0);
        this.m_bufPos = 0;
        this.m_aadPos = 0;
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
    }

    protected void validateAndProcessBuffer(byte[] byArray, int n2, byte[] byArray2, int n3) {
        if (n3 > byArray2.length - this.BlockSize) {
            throw new OutputLengthException("output buffer too short");
        }
        this.processBuffer(byArray, n2, byArray2, n3);
    }

    protected abstract void processFinalBlock(byte[] var1, int var2);

    protected abstract void processBufferAAD(byte[] var1, int var2);

    protected abstract void processFinalAAD();

    protected abstract void processBuffer(byte[] var1, int var2, byte[] var3, int var4);

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

