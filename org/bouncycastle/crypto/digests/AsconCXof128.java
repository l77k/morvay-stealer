/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.Xof;
import org.bouncycastle.crypto.digests.AsconBaseDigest;
import org.bouncycastle.util.Pack;

public class AsconCXof128
extends AsconBaseDigest
implements Xof {
    private boolean m_squeezing = false;
    private final long z0;
    private final long z1;
    private final long z2;
    private final long z3;
    private final long z4;

    public AsconCXof128() {
        this(new byte[0], 0, 0);
    }

    public AsconCXof128(byte[] byArray) {
        this(byArray, 0, byArray.length);
    }

    public AsconCXof128(byte[] byArray, int n2, int n3) {
        if (n2 + n3 > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 > 256) {
            throw new DataLengthException("customized string is too long");
        }
        this.initState(byArray, n2, n3);
        this.z0 = this.x0;
        this.z1 = this.x1;
        this.z2 = this.x2;
        this.z3 = this.x3;
        this.z4 = this.x4;
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
        return "Ascon-CXOF128";
    }

    @Override
    public int doOutput(byte[] byArray, int n2, int n3) {
        if (32 + n2 > byArray.length) {
            throw new OutputLengthException("output buffer is too short");
        }
        this.padAndAbsorb();
        this.squeeze(byArray, n2, n3);
        return n3;
    }

    @Override
    public int doFinal(byte[] byArray, int n2, int n3) {
        int n4 = this.doOutput(byArray, n2, n3);
        this.reset();
        return n4;
    }

    @Override
    public void reset() {
        super.reset();
        this.m_squeezing = false;
        this.x0 = this.z0;
        this.x1 = this.z1;
        this.x2 = this.z2;
        this.x3 = this.z3;
        this.x4 = this.z4;
    }

    private void initState(byte[] byArray, int n2, int n3) {
        this.x0 = 7445901275803737603L;
        this.x1 = 4886737088792722364L;
        this.x2 = -1616759365661982283L;
        this.x3 = 3076320316797452470L;
        this.x4 = -8124743304765850554L;
        long l2 = (long)n3 << 3;
        Pack.longToLittleEndian(l2, this.m_buf, 0);
        this.p(12);
        this.update(byArray, n2, n3);
        this.padAndAbsorb();
        this.m_squeezing = false;
    }
}

