/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.CryptoServiceProperties;
import org.bouncycastle.crypto.CryptoServicePurpose;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.digests.EncodableDigest;
import org.bouncycastle.crypto.digests.GeneralDigest;
import org.bouncycastle.crypto.digests.Utils;
import org.bouncycastle.util.Memoable;
import org.bouncycastle.util.Pack;

public class SHA1Digest
extends GeneralDigest
implements EncodableDigest {
    private static final int DIGEST_LENGTH = 20;
    private int H1;
    private int H2;
    private int H3;
    private int H4;
    private int H5;
    private int[] X = new int[80];
    private int xOff;
    private static final int Y1 = 1518500249;
    private static final int Y2 = 1859775393;
    private static final int Y3 = -1894007588;
    private static final int Y4 = -899497514;

    public SHA1Digest() {
        this(CryptoServicePurpose.ANY);
    }

    public SHA1Digest(CryptoServicePurpose cryptoServicePurpose) {
        super(cryptoServicePurpose);
        CryptoServicesRegistrar.checkConstraints(this.cryptoServiceProperties());
        this.reset();
    }

    public SHA1Digest(SHA1Digest sHA1Digest) {
        super(sHA1Digest);
        CryptoServicesRegistrar.checkConstraints(this.cryptoServiceProperties());
        this.copyIn(sHA1Digest);
    }

    public SHA1Digest(byte[] byArray) {
        super(byArray);
        CryptoServicesRegistrar.checkConstraints(this.cryptoServiceProperties());
        this.H1 = Pack.bigEndianToInt(byArray, 16);
        this.H2 = Pack.bigEndianToInt(byArray, 20);
        this.H3 = Pack.bigEndianToInt(byArray, 24);
        this.H4 = Pack.bigEndianToInt(byArray, 28);
        this.H5 = Pack.bigEndianToInt(byArray, 32);
        this.xOff = Pack.bigEndianToInt(byArray, 36);
        for (int i2 = 0; i2 != this.xOff; ++i2) {
            this.X[i2] = Pack.bigEndianToInt(byArray, 40 + i2 * 4);
        }
    }

    private void copyIn(SHA1Digest sHA1Digest) {
        this.H1 = sHA1Digest.H1;
        this.H2 = sHA1Digest.H2;
        this.H3 = sHA1Digest.H3;
        this.H4 = sHA1Digest.H4;
        this.H5 = sHA1Digest.H5;
        System.arraycopy(sHA1Digest.X, 0, this.X, 0, sHA1Digest.X.length);
        this.xOff = sHA1Digest.xOff;
    }

    @Override
    public String getAlgorithmName() {
        return "SHA-1";
    }

    @Override
    public int getDigestSize() {
        return 20;
    }

    @Override
    protected void processWord(byte[] byArray, int n2) {
        this.X[this.xOff] = Pack.bigEndianToInt(byArray, n2);
        if (++this.xOff == 16) {
            this.processBlock();
        }
    }

    @Override
    protected void processLength(long l2) {
        if (this.xOff > 14) {
            this.processBlock();
        }
        this.X[14] = (int)(l2 >>> 32);
        this.X[15] = (int)l2;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) {
        this.finish();
        Pack.intToBigEndian(this.H1, byArray, n2);
        Pack.intToBigEndian(this.H2, byArray, n2 + 4);
        Pack.intToBigEndian(this.H3, byArray, n2 + 8);
        Pack.intToBigEndian(this.H4, byArray, n2 + 12);
        Pack.intToBigEndian(this.H5, byArray, n2 + 16);
        this.reset();
        return 20;
    }

    @Override
    public void reset() {
        super.reset();
        this.H1 = 1732584193;
        this.H2 = -271733879;
        this.H3 = -1732584194;
        this.H4 = 271733878;
        this.H5 = -1009589776;
        this.xOff = 0;
        for (int i2 = 0; i2 != this.X.length; ++i2) {
            this.X[i2] = 0;
        }
    }

    private int f(int n2, int n3, int n4) {
        return n2 & n3 | ~n2 & n4;
    }

    private int h(int n2, int n3, int n4) {
        return n2 ^ n3 ^ n4;
    }

    private int g(int n2, int n3, int n4) {
        return n2 & n3 | n2 & n4 | n3 & n4;
    }

    @Override
    protected void processBlock() {
        int n2;
        int n3;
        int n4;
        for (n4 = 16; n4 < 80; ++n4) {
            n3 = this.X[n4 - 3] ^ this.X[n4 - 8] ^ this.X[n4 - 14] ^ this.X[n4 - 16];
            this.X[n4] = n3 << 1 | n3 >>> 31;
        }
        n4 = this.H1;
        n3 = this.H2;
        int n5 = this.H3;
        int n6 = this.H4;
        int n7 = this.H5;
        int n8 = 0;
        for (n2 = 0; n2 < 4; ++n2) {
            n7 += (n4 << 5 | n4 >>> 27) + this.f(n3, n5, n6) + this.X[n8++] + 1518500249;
            n3 = n3 << 30 | n3 >>> 2;
            n6 += (n7 << 5 | n7 >>> 27) + this.f(n4, n3, n5) + this.X[n8++] + 1518500249;
            n4 = n4 << 30 | n4 >>> 2;
            n5 += (n6 << 5 | n6 >>> 27) + this.f(n7, n4, n3) + this.X[n8++] + 1518500249;
            n7 = n7 << 30 | n7 >>> 2;
            n3 += (n5 << 5 | n5 >>> 27) + this.f(n6, n7, n4) + this.X[n8++] + 1518500249;
            n6 = n6 << 30 | n6 >>> 2;
            n4 += (n3 << 5 | n3 >>> 27) + this.f(n5, n6, n7) + this.X[n8++] + 1518500249;
            n5 = n5 << 30 | n5 >>> 2;
        }
        for (n2 = 0; n2 < 4; ++n2) {
            n7 += (n4 << 5 | n4 >>> 27) + this.h(n3, n5, n6) + this.X[n8++] + 1859775393;
            n3 = n3 << 30 | n3 >>> 2;
            n6 += (n7 << 5 | n7 >>> 27) + this.h(n4, n3, n5) + this.X[n8++] + 1859775393;
            n4 = n4 << 30 | n4 >>> 2;
            n5 += (n6 << 5 | n6 >>> 27) + this.h(n7, n4, n3) + this.X[n8++] + 1859775393;
            n7 = n7 << 30 | n7 >>> 2;
            n3 += (n5 << 5 | n5 >>> 27) + this.h(n6, n7, n4) + this.X[n8++] + 1859775393;
            n6 = n6 << 30 | n6 >>> 2;
            n4 += (n3 << 5 | n3 >>> 27) + this.h(n5, n6, n7) + this.X[n8++] + 1859775393;
            n5 = n5 << 30 | n5 >>> 2;
        }
        for (n2 = 0; n2 < 4; ++n2) {
            n7 += (n4 << 5 | n4 >>> 27) + this.g(n3, n5, n6) + this.X[n8++] + -1894007588;
            n3 = n3 << 30 | n3 >>> 2;
            n6 += (n7 << 5 | n7 >>> 27) + this.g(n4, n3, n5) + this.X[n8++] + -1894007588;
            n4 = n4 << 30 | n4 >>> 2;
            n5 += (n6 << 5 | n6 >>> 27) + this.g(n7, n4, n3) + this.X[n8++] + -1894007588;
            n7 = n7 << 30 | n7 >>> 2;
            n3 += (n5 << 5 | n5 >>> 27) + this.g(n6, n7, n4) + this.X[n8++] + -1894007588;
            n6 = n6 << 30 | n6 >>> 2;
            n4 += (n3 << 5 | n3 >>> 27) + this.g(n5, n6, n7) + this.X[n8++] + -1894007588;
            n5 = n5 << 30 | n5 >>> 2;
        }
        for (n2 = 0; n2 <= 3; ++n2) {
            n7 += (n4 << 5 | n4 >>> 27) + this.h(n3, n5, n6) + this.X[n8++] + -899497514;
            n3 = n3 << 30 | n3 >>> 2;
            n6 += (n7 << 5 | n7 >>> 27) + this.h(n4, n3, n5) + this.X[n8++] + -899497514;
            n4 = n4 << 30 | n4 >>> 2;
            n5 += (n6 << 5 | n6 >>> 27) + this.h(n7, n4, n3) + this.X[n8++] + -899497514;
            n7 = n7 << 30 | n7 >>> 2;
            n3 += (n5 << 5 | n5 >>> 27) + this.h(n6, n7, n4) + this.X[n8++] + -899497514;
            n6 = n6 << 30 | n6 >>> 2;
            n4 += (n3 << 5 | n3 >>> 27) + this.h(n5, n6, n7) + this.X[n8++] + -899497514;
            n5 = n5 << 30 | n5 >>> 2;
        }
        this.H1 += n4;
        this.H2 += n3;
        this.H3 += n5;
        this.H4 += n6;
        this.H5 += n7;
        this.xOff = 0;
        for (n2 = 0; n2 < 16; ++n2) {
            this.X[n2] = 0;
        }
    }

    @Override
    public Memoable copy() {
        return new SHA1Digest(this);
    }

    @Override
    public void reset(Memoable memoable) {
        SHA1Digest sHA1Digest = (SHA1Digest)memoable;
        super.copyIn(sHA1Digest);
        this.copyIn(sHA1Digest);
    }

    @Override
    public byte[] getEncodedState() {
        byte[] byArray = new byte[40 + this.xOff * 4 + 1];
        super.populateState(byArray);
        Pack.intToBigEndian(this.H1, byArray, 16);
        Pack.intToBigEndian(this.H2, byArray, 20);
        Pack.intToBigEndian(this.H3, byArray, 24);
        Pack.intToBigEndian(this.H4, byArray, 28);
        Pack.intToBigEndian(this.H5, byArray, 32);
        Pack.intToBigEndian(this.xOff, byArray, 36);
        for (int i2 = 0; i2 != this.xOff; ++i2) {
            Pack.intToBigEndian(this.X[i2], byArray, 40 + i2 * 4);
        }
        byArray[byArray.length - 1] = (byte)this.purpose.ordinal();
        return byArray;
    }

    @Override
    protected CryptoServiceProperties cryptoServiceProperties() {
        return Utils.getDefaultProperties(this, 128, this.purpose);
    }
}

