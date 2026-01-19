/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.engines.AsconBaseEngine;
import org.bouncycastle.util.Pack;

public class AsconEngine
extends AsconBaseEngine {
    private final AsconParameters asconParameters;
    private long K2;

    public AsconEngine(AsconParameters asconParameters) {
        this.asconParameters = asconParameters;
        this.IV_SIZE = 16;
        this.MAC_SIZE = 16;
        switch (asconParameters.ordinal()) {
            case 0: {
                this.KEY_SIZE = 20;
                this.ASCON_AEAD_RATE = 8;
                this.ASCON_IV = -6899501409222262784L;
                this.algorithmName = "Ascon-80pq AEAD";
                break;
            }
            case 1: {
                this.KEY_SIZE = 16;
                this.ASCON_AEAD_RATE = 16;
                this.ASCON_IV = -9187330011336540160L;
                this.algorithmName = "Ascon-128a AEAD";
                break;
            }
            case 2: {
                this.KEY_SIZE = 16;
                this.ASCON_AEAD_RATE = 8;
                this.ASCON_IV = -9205344418435956736L;
                this.algorithmName = "Ascon-128 AEAD";
                break;
            }
            default: {
                throw new IllegalArgumentException("invalid parameter setting for ASCON AEAD");
            }
        }
        this.nr = this.ASCON_AEAD_RATE == 8 ? 6 : 8;
        this.m_bufferSizeDecrypt = this.ASCON_AEAD_RATE + this.MAC_SIZE;
        this.m_buf = new byte[this.m_bufferSizeDecrypt];
        this.dsep = 1L;
    }

    @Override
    protected long pad(int n2) {
        return 128L << 56 - (n2 << 3);
    }

    @Override
    protected long loadBytes(byte[] byArray, int n2) {
        return Pack.bigEndianToLong(byArray, n2);
    }

    @Override
    protected void setBytes(long l2, byte[] byArray, int n2) {
        Pack.longToBigEndian(l2, byArray, n2);
    }

    @Override
    protected void ascon_aeadinit() {
        this.x0 = this.ASCON_IV;
        if (this.KEY_SIZE == 20) {
            this.x0 ^= this.K0;
        }
        this.x1 = this.K1;
        this.x2 = this.K2;
        this.x3 = this.N0;
        this.x4 = this.N1;
        this.p(12);
        if (this.KEY_SIZE == 20) {
            this.x2 ^= this.K0;
        }
        this.x3 ^= this.K1;
        this.x4 ^= this.K2;
    }

    @Override
    protected void processFinalAadBlock() {
        this.m_buf[this.m_bufPos] = -128;
        if (this.m_bufPos >= 8) {
            this.x0 ^= Pack.bigEndianToLong(this.m_buf, 0);
            this.x1 ^= Pack.bigEndianToLong(this.m_buf, 8) & -1L << 56 - (this.m_bufPos - 8 << 3);
        } else {
            this.x0 ^= Pack.bigEndianToLong(this.m_buf, 0) & -1L << 56 - (this.m_bufPos << 3);
        }
    }

    @Override
    protected void processFinalDecrypt(byte[] byArray, int n2, byte[] byArray2, int n3) {
        if (n2 >= 8) {
            long l2 = Pack.bigEndianToLong(byArray, 0);
            this.x0 ^= l2;
            Pack.longToBigEndian(this.x0, byArray2, n3);
            this.x0 = l2;
            n3 += 8;
            this.x1 ^= this.pad(n2 -= 8);
            if (n2 != 0) {
                long l3 = Pack.littleEndianToLong_High(byArray, 8, n2);
                this.x1 ^= l3;
                Pack.longToLittleEndian_High(this.x1, byArray2, n3, n2);
                this.x1 &= -1L >>> (n2 << 3);
                this.x1 ^= l3;
            }
        } else {
            this.x0 ^= this.pad(n2);
            if (n2 != 0) {
                long l4 = Pack.littleEndianToLong_High(byArray, 0, n2);
                this.x0 ^= l4;
                Pack.longToLittleEndian_High(this.x0, byArray2, n3, n2);
                this.x0 &= -1L >>> (n2 << 3);
                this.x0 ^= l4;
            }
        }
        this.finishData(AsconBaseEngine.State.DecFinal);
    }

    @Override
    protected void processFinalEncrypt(byte[] byArray, int n2, byte[] byArray2, int n3) {
        if (n2 >= 8) {
            this.x0 ^= Pack.bigEndianToLong(byArray, 0);
            Pack.longToBigEndian(this.x0, byArray2, n3);
            n3 += 8;
            this.x1 ^= this.pad(n2 -= 8);
            if (n2 != 0) {
                this.x1 ^= Pack.littleEndianToLong_High(byArray, 8, n2);
                Pack.longToLittleEndian_High(this.x1, byArray2, n3, n2);
            }
        } else {
            this.x0 ^= this.pad(n2);
            if (n2 != 0) {
                this.x0 ^= Pack.littleEndianToLong_High(byArray, 0, n2);
                Pack.longToLittleEndian_High(this.x0, byArray2, n3, n2);
            }
        }
        this.finishData(AsconBaseEngine.State.EncFinal);
    }

    private void finishData(AsconBaseEngine.State state) {
        switch (this.asconParameters.ordinal()) {
            case 2: {
                this.x1 ^= this.K1;
                this.x2 ^= this.K2;
                break;
            }
            case 1: {
                this.x2 ^= this.K1;
                this.x3 ^= this.K2;
                break;
            }
            case 0: {
                this.x1 ^= this.K0 << 32 | this.K1 >> 32;
                this.x2 ^= this.K1 << 32 | this.K2 >> 32;
                this.x3 ^= this.K2 << 32;
                break;
            }
            default: {
                throw new IllegalStateException();
            }
        }
        this.p(12);
        this.x3 ^= this.K1;
        this.x4 ^= this.K2;
        this.m_state = state;
    }

    @Override
    protected void init(byte[] byArray, byte[] byArray2) throws IllegalArgumentException {
        this.N0 = Pack.bigEndianToLong(byArray2, 0);
        this.N1 = Pack.bigEndianToLong(byArray2, 8);
        if (this.KEY_SIZE == 16) {
            this.K1 = Pack.bigEndianToLong(byArray, 0);
            this.K2 = Pack.bigEndianToLong(byArray, 8);
        } else if (this.KEY_SIZE == 20) {
            this.K0 = Pack.bigEndianToInt(byArray, 0);
            this.K1 = Pack.bigEndianToLong(byArray, 4);
            this.K2 = Pack.bigEndianToLong(byArray, 12);
        } else {
            throw new IllegalStateException();
        }
        this.m_state = this.forEncryption ? AsconBaseEngine.State.EncInit : AsconBaseEngine.State.DecInit;
        this.reset(true);
    }

    @Override
    public String getAlgorithmVersion() {
        return "v1.2";
    }

    public static enum AsconParameters {
        ascon80pq,
        ascon128a,
        ascon128;

    }
}

