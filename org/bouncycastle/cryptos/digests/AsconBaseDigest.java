/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Longs;

abstract class AsconBaseDigest
implements ExtendedDigest {
    protected long x0;
    protected long x1;
    protected long x2;
    protected long x3;
    protected long x4;
    protected final int CRYPTO_BYTES = 32;
    protected final int ASCON_HASH_RATE = 8;
    protected int ASCON_PB_ROUNDS = 12;
    protected final byte[] m_buf = new byte[8];
    protected int m_bufPos = 0;

    AsconBaseDigest() {
    }

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

    protected abstract long pad(int var1);

    protected abstract long loadBytes(byte[] var1, int var2);

    protected abstract long loadBytes(byte[] var1, int var2, int var3);

    protected abstract void setBytes(long var1, byte[] var3, int var4);

    protected abstract void setBytes(long var1, byte[] var3, int var4, int var5);

    @Override
    public int getDigestSize() {
        return 32;
    }

    @Override
    public int getByteLength() {
        return 8;
    }

    @Override
    public void update(byte by) {
        this.m_buf[this.m_bufPos] = by;
        if (++this.m_bufPos == 8) {
            this.x0 ^= this.loadBytes(this.m_buf, 0);
            this.p(this.ASCON_PB_ROUNDS);
            this.m_bufPos = 0;
        }
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) {
        int n4;
        if (n2 + n3 > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        int n5 = 8 - this.m_bufPos;
        if (n3 < n5) {
            System.arraycopy(byArray, n2, this.m_buf, this.m_bufPos, n3);
            this.m_bufPos += n3;
            return;
        }
        int n6 = 0;
        if (this.m_bufPos > 0) {
            System.arraycopy(byArray, n2, this.m_buf, this.m_bufPos, n5);
            n6 += n5;
            this.x0 ^= this.loadBytes(this.m_buf, 0);
            this.p(this.ASCON_PB_ROUNDS);
        }
        while ((n4 = n3 - n6) >= 8) {
            this.x0 ^= this.loadBytes(byArray, n2 + n6);
            this.p(this.ASCON_PB_ROUNDS);
            n6 += 8;
        }
        System.arraycopy(byArray, n2 + n6, this.m_buf, 0, n4);
        this.m_bufPos = n4;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) {
        return this.hash(byArray, n2, 32);
    }

    protected void padAndAbsorb() {
        this.x0 ^= this.loadBytes(this.m_buf, 0, this.m_bufPos);
        this.x0 ^= this.pad(this.m_bufPos);
        this.p(12);
    }

    protected void squeeze(byte[] byArray, int n2, int n3) {
        while (n3 > 8) {
            this.setBytes(this.x0, byArray, n2);
            this.p(this.ASCON_PB_ROUNDS);
            n2 += 8;
            n3 -= 8;
        }
        this.setBytes(this.x0, byArray, n2, n3);
        this.reset();
    }

    protected int hash(byte[] byArray, int n2, int n3) {
        if (32 + n2 > byArray.length) {
            throw new OutputLengthException("output buffer is too short");
        }
        this.padAndAbsorb();
        this.squeeze(byArray, n2, n3);
        return n3;
    }

    @Override
    public void reset() {
        Arrays.clear(this.m_buf);
        this.m_bufPos = 0;
    }
}

