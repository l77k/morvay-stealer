/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.CryptoServiceProperties;
import org.bouncycastle.crypto.CryptoServicePurpose;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.util.Memoable;
import org.bouncycastle.util.Pack;

public abstract class GeneralDigest
implements ExtendedDigest,
Memoable {
    private static final int BYTE_LENGTH = 64;
    protected final CryptoServicePurpose purpose;
    private final byte[] xBuf = new byte[4];
    private int xBufOff;
    private long byteCount;

    protected GeneralDigest() {
        this(CryptoServicePurpose.ANY);
    }

    protected GeneralDigest(CryptoServicePurpose cryptoServicePurpose) {
        this.purpose = cryptoServicePurpose;
        this.xBufOff = 0;
    }

    protected GeneralDigest(GeneralDigest generalDigest) {
        this.purpose = generalDigest.purpose;
        this.copyIn(generalDigest);
    }

    protected GeneralDigest(byte[] byArray) {
        CryptoServicePurpose[] cryptoServicePurposeArray = CryptoServicePurpose.values();
        this.purpose = cryptoServicePurposeArray[byArray[byArray.length - 1]];
        System.arraycopy(byArray, 0, this.xBuf, 0, this.xBuf.length);
        this.xBufOff = Pack.bigEndianToInt(byArray, 4);
        this.byteCount = Pack.bigEndianToLong(byArray, 8);
    }

    protected void copyIn(GeneralDigest generalDigest) {
        System.arraycopy(generalDigest.xBuf, 0, this.xBuf, 0, generalDigest.xBuf.length);
        this.xBufOff = generalDigest.xBufOff;
        this.byteCount = generalDigest.byteCount;
    }

    @Override
    public void update(byte by) {
        this.xBuf[this.xBufOff++] = by;
        if (this.xBufOff == this.xBuf.length) {
            this.processWord(this.xBuf, 0);
            this.xBufOff = 0;
        }
        ++this.byteCount;
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) {
        n3 = Math.max(0, n3);
        int n4 = 0;
        if (this.xBufOff != 0) {
            while (n4 < n3) {
                this.xBuf[this.xBufOff++] = byArray[n2 + n4++];
                if (this.xBufOff != 4) continue;
                this.processWord(this.xBuf, 0);
                this.xBufOff = 0;
                break;
            }
        }
        int n5 = n3 - 3;
        while (n4 < n5) {
            this.processWord(byArray, n2 + n4);
            n4 += 4;
        }
        while (n4 < n3) {
            this.xBuf[this.xBufOff++] = byArray[n2 + n4++];
        }
        this.byteCount += (long)n3;
    }

    public void finish() {
        long l2 = this.byteCount << 3;
        this.update((byte)-128);
        while (this.xBufOff != 0) {
            this.update((byte)0);
        }
        this.processLength(l2);
        this.processBlock();
    }

    @Override
    public void reset() {
        this.byteCount = 0L;
        this.xBufOff = 0;
        for (int i2 = 0; i2 < this.xBuf.length; ++i2) {
            this.xBuf[i2] = 0;
        }
    }

    protected void populateState(byte[] byArray) {
        System.arraycopy(this.xBuf, 0, byArray, 0, this.xBufOff);
        Pack.intToBigEndian(this.xBufOff, byArray, 4);
        Pack.longToBigEndian(this.byteCount, byArray, 8);
    }

    @Override
    public int getByteLength() {
        return 64;
    }

    protected abstract void processWord(byte[] var1, int var2);

    protected abstract void processLength(long var1);

    protected abstract void processBlock();

    protected abstract CryptoServiceProperties cryptoServiceProperties();
}

