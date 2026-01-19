/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.digests.AsconBaseDigest;
import org.bouncycastle.util.Pack;

public class AsconDigest
extends AsconBaseDigest {
    AsconParameters asconParameters;
    private final String algorithmName;

    public AsconDigest(AsconParameters asconParameters) {
        this.asconParameters = asconParameters;
        switch (asconParameters.ordinal()) {
            case 0: {
                this.ASCON_PB_ROUNDS = 12;
                this.algorithmName = "Ascon-Hash";
                break;
            }
            case 1: {
                this.ASCON_PB_ROUNDS = 8;
                this.algorithmName = "Ascon-HashA";
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid parameter settings for Ascon Hash");
            }
        }
        this.reset();
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
    public void reset() {
        super.reset();
        switch (this.asconParameters.ordinal()) {
            case 1: {
                this.x0 = 92044056785660070L;
                this.x1 = 8326807761760157607L;
                this.x2 = 3371194088139667532L;
                this.x3 = -2956994353054992515L;
                this.x4 = -6828509670848688761L;
                break;
            }
            case 0: {
                this.x0 = -1255492011513352131L;
                this.x1 = -8380609354527731710L;
                this.x2 = -5437372128236807582L;
                this.x3 = 4834782570098516968L;
                this.x4 = 3787428097924915520L;
            }
        }
    }

    public static enum AsconParameters {
        AsconHash,
        AsconHashA;

    }
}

