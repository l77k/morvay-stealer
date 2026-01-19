/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.Xof;
import org.bouncycastle.crypto.digests.AsconBaseDigest;
import org.bouncycastle.util.Pack;

public class AsconXof128
extends AsconBaseDigest
implements Xof {
    private boolean m_squeezing = false;

    public AsconXof128() {
        this.reset();
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
    protected long loadBytes(byte[] byArray, int n2, int n3) {
        return Pack.littleEndianToLong(byArray, n2, n3);
    }

    @Override
    protected void setBytes(long l2, byte[] byArray, int n2) {
        Pack.longToLittleEndian(l2, byArray, n2);
    }

    @Override
    protected void setBytes(long l2, byte[] byArray, int n2, int n3) {
        Pack.longToLittleEndian(l2, byArray, n2, n3);
    }

    @Override
    protected void padAndAbsorb() {
        this.m_squeezing = true;
        super.padAndAbsorb();
    }

    @Override
    public String getAlgorithmName() {
        return "Ascon-XOF-128";
    }

    @Override
    public void update(byte by) {
        if (this.m_squeezing) {
            throw new IllegalArgumentException("attempt to absorb while squeezing");
        }
        super.update(by);
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) {
        if (this.m_squeezing) {
            throw new IllegalArgumentException("attempt to absorb while squeezing");
        }
        super.update(byArray, n2, n3);
    }

    @Override
    public int doOutput(byte[] byArray, int n2, int n3) {
        return this.hash(byArray, n2, n3);
    }

    @Override
    public int doFinal(byte[] byArray, int n2, int n3) {
        int n4 = this.doOutput(byArray, n2, n3);
        this.reset();
        return n4;
    }

    @Override
    public int getByteLength() {
        return 8;
    }

    @Override
    public void reset() {
        this.m_squeezing = false;
        super.reset();
        this.x0 = -2701369817892108309L;
        this.x1 = -3711838248891385495L;
        this.x2 = -1778763697082575311L;
        this.x3 = 1072114354614917324L;
        this.x4 = -2282070310009238562L;
    }
}

