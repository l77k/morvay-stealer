/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.engines.AsconBaseEngine;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class AsconAEAD128
extends AsconBaseEngine {
    public AsconAEAD128() {
        this.KEY_SIZE = 16;
        this.IV_SIZE = 16;
        this.MAC_SIZE = 16;
        this.ASCON_AEAD_RATE = 16;
        this.ASCON_IV = 17594342703105L;
        this.algorithmName = "Ascon-AEAD128";
        this.nr = 8;
        this.m_bufferSizeDecrypt = this.ASCON_AEAD_RATE + this.MAC_SIZE;
        this.m_buf = new byte[this.m_bufferSizeDecrypt];
        this.dsep = Long.MIN_VALUE;
    }

    @Override
    protected long pad(int n2) {
        return 1L << (n2 << 3);
    }

    @Override
    protected long loadBytes(byte[] byArray, int n2) {
        return Pack.littleEndianToLong(byArray, n2);
    }

    @Override
    protected void setBytes(long l2, byte[] byArray, int n2) {
        Pack.longToLittleEndian(l2, byArray, n2);
    }

    @Override
    protected void ascon_aeadinit() {
        this.x0 = this.ASCON_IV;
        this.x1 = this.K0;
        this.x2 = this.K1;
        this.x3 = this.N0;
        this.x4 = this.N1;
        this.p(12);
        this.x3 ^= this.K0;
        this.x4 ^= this.K1;
    }

    @Override
    protected void processFinalAadBlock() {
        Arrays.fill(this.m_buf, this.m_bufPos, this.m_buf.length, (byte)0);
        if (this.m_bufPos >= 8) {
            this.x0 ^= Pack.littleEndianToLong(this.m_buf, 0);
            this.x1 ^= Pack.littleEndianToLong(this.m_buf, 8) ^ this.pad(this.m_bufPos);
        } else {
            this.x0 ^= Pack.littleEndianToLong(this.m_buf, 0) ^ this.pad(this.m_bufPos);
        }
    }

    @Override
    protected void processFinalDecrypt(byte[] byArray, int n2, byte[] byArray2, int n3) {
        if (n2 >= 8) {
            long l2 = Pack.littleEndianToLong(byArray, 0);
            long l3 = Pack.littleEndianToLong(byArray, 8, n2 -= 8);
            Pack.longToLittleEndian(this.x0 ^ l2, byArray2, n3);
            Pack.longToLittleEndian(this.x1 ^ l3, byArray2, n3 + 8, n2);
            this.x0 = l2;
            this.x1 &= -(1L << (n2 << 3));
            this.x1 |= l3;
            this.x1 ^= this.pad(n2);
        } else {
            if (n2 != 0) {
                long l4 = Pack.littleEndianToLong(byArray, 0, n2);
                Pack.longToLittleEndian(this.x0 ^ l4, byArray2, n3, n2);
                this.x0 &= -(1L << (n2 << 3));
                this.x0 |= l4;
            }
            this.x0 ^= this.pad(n2);
        }
        this.finishData(AsconBaseEngine.State.DecFinal);
    }

    @Override
    protected void processFinalEncrypt(byte[] byArray, int n2, byte[] byArray2, int n3) {
        if (n2 >= 8) {
            this.x0 ^= Pack.littleEndianToLong(byArray, 0);
            this.x1 ^= Pack.littleEndianToLong(byArray, 8, n2 -= 8);
            Pack.longToLittleEndian(this.x0, byArray2, n3);
            Pack.longToLittleEndian(this.x1, byArray2, n3 + 8);
            this.x1 ^= this.pad(n2);
        } else {
            if (n2 != 0) {
                this.x0 ^= Pack.littleEndianToLong(byArray, 0, n2);
                Pack.longToLittleEndian(this.x0, byArray2, n3, n2);
            }
            this.x0 ^= this.pad(n2);
        }
        this.finishData(AsconBaseEngine.State.EncFinal);
    }

    private void finishData(AsconBaseEngine.State state) {
        this.x2 ^= this.K0;
        this.x3 ^= this.K1;
        this.p(12);
        this.x3 ^= this.K0;
        this.x4 ^= this.K1;
        this.m_state = state;
    }

    @Override
    protected void init(byte[] byArray, byte[] byArray2) throws IllegalArgumentException {
        this.K0 = Pack.littleEndianToLong(byArray, 0);
        this.K1 = Pack.littleEndianToLong(byArray, 8);
        this.N0 = Pack.littleEndianToLong(byArray2, 0);
        this.N1 = Pack.littleEndianToLong(byArray2, 8);
        this.m_state = this.forEncryption ? AsconBaseEngine.State.EncInit : AsconBaseEngine.State.DecInit;
        this.reset(true);
    }

    @Override
    public String getAlgorithmVersion() {
        return "v1.3";
    }
}

