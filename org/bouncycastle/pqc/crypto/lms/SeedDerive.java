/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.lms;

import org.bouncycastle.crypto.Digest;

class SeedDerive {
    private final byte[] I;
    private final byte[] masterSeed;
    private final Digest digest;
    private int q;
    private int j;

    public SeedDerive(byte[] byArray, byte[] byArray2, Digest digest) {
        this.I = byArray;
        this.masterSeed = byArray2;
        this.digest = digest;
    }

    public int getQ() {
        return this.q;
    }

    public void setQ(int n2) {
        this.q = n2;
    }

    public int getJ() {
        return this.j;
    }

    public void setJ(int n2) {
        this.j = n2;
    }

    public byte[] getI() {
        return this.I;
    }

    public byte[] getMasterSeed() {
        return this.masterSeed;
    }

    public byte[] deriveSeed(byte[] byArray, int n2) {
        if (byArray.length - n2 < this.digest.getDigestSize()) {
            throw new IllegalArgumentException("target length is less than digest size.");
        }
        this.digest.update(this.I, 0, this.I.length);
        this.digest.update((byte)(this.q >>> 24));
        this.digest.update((byte)(this.q >>> 16));
        this.digest.update((byte)(this.q >>> 8));
        this.digest.update((byte)this.q);
        this.digest.update((byte)(this.j >>> 8));
        this.digest.update((byte)this.j);
        this.digest.update((byte)-1);
        this.digest.update(this.masterSeed, 0, this.masterSeed.length);
        this.digest.doFinal(byArray, n2);
        return byArray;
    }

    public void deriveSeed(byte[] byArray, boolean bl) {
        this.deriveSeed(byArray, bl, 0);
    }

    public void deriveSeed(byte[] byArray, boolean bl, int n2) {
        this.deriveSeed(byArray, n2);
        if (bl) {
            ++this.j;
        }
    }
}

