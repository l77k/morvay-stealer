/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.digests.AsconBaseDigest;
import org.bouncycastle.util.Pack;

public class AsconHash256
extends AsconBaseDigest {
    public AsconHash256() {
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
    public String getAlgorithmName() {
        return "Ascon-Hash256";
    }

    @Override
    public void reset() {
        super.reset();
        this.x0 = -7269279749984954751L;
        this.x1 = 5459383224871899602L;
        this.x2 = -5880230600644446182L;
        this.x3 = 4359436768738168243L;
        this.x4 = 1899470422303676269L;
    }
}

