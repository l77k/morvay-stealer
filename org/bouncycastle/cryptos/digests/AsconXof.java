/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.Xof;
import org.bouncycastle.crypto.digests.AsconBaseDigest;
import org.bouncycastle.util.Pack;

public class AsconXof
extends AsconBaseDigest
implements Xof {
    AsconParameters asconParameters;
    private final String algorithmName;
    private boolean m_squeezing = false;

    public AsconXof(AsconParameters asconParameters) {
        this.asconParameters = asconParameters;
        switch (asconParameters.ordinal()) {
            case 0: {
                this.ASCON_PB_ROUNDS = 12;
                this.algorithmName = "Ascon-Xof";
                break;
            }
            case 1: {
                this.ASCON_PB_ROUNDS = 8;
                this.algorithmName = "Ascon-XofA";
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid parameter settings for Ascon Hash");
            }
        }
        this.reset();
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
    protected void padAndAbsorb() {
        this.m_squeezing = true;
        super.padAndAbsorb();
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
    protected long loadBytes(byte[] byArray, int n2, int n3) {
        return Pack.bigEndianToLong(byArray, n2, n3);
    }

    @Override
    protected void setBytes(long l2, byte[] byArray, int n2) {
        Pack.longToBigEndian(l2, byArray, n2);
    }

    @Override
    protected void setBytes(long l2, byte[] byArray, int n2, int n3) {
        Pack.longToBigEndian(l2, byArray, n2, n3);
    }

    @Override
    public String getAlgorithmName() {
        return this.algorithmName;
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
        super.reset();
        this.m_squeezing = false;
        switch (this.asconParameters.ordinal()) {
            case 0: {
                this.x0 = -5368810569253202922L;
                this.x1 = 3121280575360345120L;
                this.x2 = 7395939140700676632L;
                this.x3 = 6533890155656471820L;
                this.x4 = 5710016986865767350L;
                break;
            }
            case 1: {
                this.x0 = 4940560291654768690L;
                this.x1 = -3635129828240960206L;
                this.x2 = -597534922722107095L;
                this.x3 = 2623493988082852443L;
                this.x4 = -6283826724160825537L;
            }
        }
    }

    public static enum AsconParameters {
        AsconXof,
        AsconXofA;

    }
}

