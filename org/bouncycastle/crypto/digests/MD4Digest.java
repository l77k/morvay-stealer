/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.CryptoServiceProperties;
import org.bouncycastle.crypto.CryptoServicePurpose;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.digests.GeneralDigest;
import org.bouncycastle.crypto.digests.Utils;
import org.bouncycastle.util.Memoable;
import org.bouncycastle.util.Pack;

public class MD4Digest
extends GeneralDigest {
    private static final int DIGEST_LENGTH = 16;
    private int H1;
    private int H2;
    private int H3;
    private int H4;
    private int[] X = new int[16];
    private int xOff;
    private static final int S11 = 3;
    private static final int S12 = 7;
    private static final int S13 = 11;
    private static final int S14 = 19;
    private static final int S21 = 3;
    private static final int S22 = 5;
    private static final int S23 = 9;
    private static final int S24 = 13;
    private static final int S31 = 3;
    private static final int S32 = 9;
    private static final int S33 = 11;
    private static final int S34 = 15;

    public MD4Digest() {
        this(CryptoServicePurpose.ANY);
    }

    public MD4Digest(CryptoServicePurpose cryptoServicePurpose) {
        super(cryptoServicePurpose);
        CryptoServicesRegistrar.checkConstraints(Utils.getDefaultProperties(this, 64, cryptoServicePurpose));
        this.reset();
    }

    public MD4Digest(MD4Digest mD4Digest) {
        super(mD4Digest.purpose);
        CryptoServicesRegistrar.checkConstraints(Utils.getDefaultProperties(this, 64, this.purpose));
        this.copyIn(mD4Digest);
    }

    private void copyIn(MD4Digest mD4Digest) {
        super.copyIn(mD4Digest);
        this.H1 = mD4Digest.H1;
        this.H2 = mD4Digest.H2;
        this.H3 = mD4Digest.H3;
        this.H4 = mD4Digest.H4;
        System.arraycopy(mD4Digest.X, 0, this.X, 0, mD4Digest.X.length);
        this.xOff = mD4Digest.xOff;
    }

    @Override
    public String getAlgorithmName() {
        return "MD4";
    }

    @Override
    public int getDigestSize() {
        return 16;
    }

    @Override
    protected void processWord(byte[] byArray, int n2) {
        this.X[this.xOff++] = Pack.littleEndianToInt(byArray, n2);
        if (this.xOff == 16) {
            this.processBlock();
        }
    }

    @Override
    protected void processLength(long l2) {
        if (this.xOff > 14) {
            this.processBlock();
        }
        this.X[14] = (int)(l2 & 0xFFFFFFFFFFFFFFFFL);
        this.X[15] = (int)(l2 >>> 32);
    }

    @Override
    public int doFinal(byte[] byArray, int n2) {
        this.finish();
        Pack.intToLittleEndian(this.H1, byArray, n2);
        Pack.intToLittleEndian(this.H2, byArray, n2 + 4);
        Pack.intToLittleEndian(this.H3, byArray, n2 + 8);
        Pack.intToLittleEndian(this.H4, byArray, n2 + 12);
        this.reset();
        return 16;
    }

    @Override
    public void reset() {
        super.reset();
        this.H1 = 1732584193;
        this.H2 = -271733879;
        this.H3 = -1732584194;
        this.H4 = 271733878;
        this.xOff = 0;
        for (int i2 = 0; i2 != this.X.length; ++i2) {
            this.X[i2] = 0;
        }
    }

    private int rotateLeft(int n2, int n3) {
        return n2 << n3 | n2 >>> 32 - n3;
    }

    private int F(int n2, int n3, int n4) {
        return n2 & n3 | ~n2 & n4;
    }

    private int G(int n2, int n3, int n4) {
        return n2 & n3 | n2 & n4 | n3 & n4;
    }

    private int H(int n2, int n3, int n4) {
        return n2 ^ n3 ^ n4;
    }

    @Override
    protected void processBlock() {
        int n2 = this.H1;
        int n3 = this.H2;
        int n4 = this.H3;
        int n5 = this.H4;
        n2 = this.rotateLeft(n2 + this.F(n3, n4, n5) + this.X[0], 3);
        n5 = this.rotateLeft(n5 + this.F(n2, n3, n4) + this.X[1], 7);
        n4 = this.rotateLeft(n4 + this.F(n5, n2, n3) + this.X[2], 11);
        n3 = this.rotateLeft(n3 + this.F(n4, n5, n2) + this.X[3], 19);
        n2 = this.rotateLeft(n2 + this.F(n3, n4, n5) + this.X[4], 3);
        n5 = this.rotateLeft(n5 + this.F(n2, n3, n4) + this.X[5], 7);
        n4 = this.rotateLeft(n4 + this.F(n5, n2, n3) + this.X[6], 11);
        n3 = this.rotateLeft(n3 + this.F(n4, n5, n2) + this.X[7], 19);
        n2 = this.rotateLeft(n2 + this.F(n3, n4, n5) + this.X[8], 3);
        n5 = this.rotateLeft(n5 + this.F(n2, n3, n4) + this.X[9], 7);
        n4 = this.rotateLeft(n4 + this.F(n5, n2, n3) + this.X[10], 11);
        n3 = this.rotateLeft(n3 + this.F(n4, n5, n2) + this.X[11], 19);
        n2 = this.rotateLeft(n2 + this.F(n3, n4, n5) + this.X[12], 3);
        n5 = this.rotateLeft(n5 + this.F(n2, n3, n4) + this.X[13], 7);
        n4 = this.rotateLeft(n4 + this.F(n5, n2, n3) + this.X[14], 11);
        n3 = this.rotateLeft(n3 + this.F(n4, n5, n2) + this.X[15], 19);
        n2 = this.rotateLeft(n2 + this.G(n3, n4, n5) + this.X[0] + 1518500249, 3);
        n5 = this.rotateLeft(n5 + this.G(n2, n3, n4) + this.X[4] + 1518500249, 5);
        n4 = this.rotateLeft(n4 + this.G(n5, n2, n3) + this.X[8] + 1518500249, 9);
        n3 = this.rotateLeft(n3 + this.G(n4, n5, n2) + this.X[12] + 1518500249, 13);
        n2 = this.rotateLeft(n2 + this.G(n3, n4, n5) + this.X[1] + 1518500249, 3);
        n5 = this.rotateLeft(n5 + this.G(n2, n3, n4) + this.X[5] + 1518500249, 5);
        n4 = this.rotateLeft(n4 + this.G(n5, n2, n3) + this.X[9] + 1518500249, 9);
        n3 = this.rotateLeft(n3 + this.G(n4, n5, n2) + this.X[13] + 1518500249, 13);
        n2 = this.rotateLeft(n2 + this.G(n3, n4, n5) + this.X[2] + 1518500249, 3);
        n5 = this.rotateLeft(n5 + this.G(n2, n3, n4) + this.X[6] + 1518500249, 5);
        n4 = this.rotateLeft(n4 + this.G(n5, n2, n3) + this.X[10] + 1518500249, 9);
        n3 = this.rotateLeft(n3 + this.G(n4, n5, n2) + this.X[14] + 1518500249, 13);
        n2 = this.rotateLeft(n2 + this.G(n3, n4, n5) + this.X[3] + 1518500249, 3);
        n5 = this.rotateLeft(n5 + this.G(n2, n3, n4) + this.X[7] + 1518500249, 5);
        n4 = this.rotateLeft(n4 + this.G(n5, n2, n3) + this.X[11] + 1518500249, 9);
        n3 = this.rotateLeft(n3 + this.G(n4, n5, n2) + this.X[15] + 1518500249, 13);
        n2 = this.rotateLeft(n2 + this.H(n3, n4, n5) + this.X[0] + 1859775393, 3);
        n5 = this.rotateLeft(n5 + this.H(n2, n3, n4) + this.X[8] + 1859775393, 9);
        n4 = this.rotateLeft(n4 + this.H(n5, n2, n3) + this.X[4] + 1859775393, 11);
        n3 = this.rotateLeft(n3 + this.H(n4, n5, n2) + this.X[12] + 1859775393, 15);
        n2 = this.rotateLeft(n2 + this.H(n3, n4, n5) + this.X[2] + 1859775393, 3);
        n5 = this.rotateLeft(n5 + this.H(n2, n3, n4) + this.X[10] + 1859775393, 9);
        n4 = this.rotateLeft(n4 + this.H(n5, n2, n3) + this.X[6] + 1859775393, 11);
        n3 = this.rotateLeft(n3 + this.H(n4, n5, n2) + this.X[14] + 1859775393, 15);
        n2 = this.rotateLeft(n2 + this.H(n3, n4, n5) + this.X[1] + 1859775393, 3);
        n5 = this.rotateLeft(n5 + this.H(n2, n3, n4) + this.X[9] + 1859775393, 9);
        n4 = this.rotateLeft(n4 + this.H(n5, n2, n3) + this.X[5] + 1859775393, 11);
        n3 = this.rotateLeft(n3 + this.H(n4, n5, n2) + this.X[13] + 1859775393, 15);
        n2 = this.rotateLeft(n2 + this.H(n3, n4, n5) + this.X[3] + 1859775393, 3);
        n5 = this.rotateLeft(n5 + this.H(n2, n3, n4) + this.X[11] + 1859775393, 9);
        n4 = this.rotateLeft(n4 + this.H(n5, n2, n3) + this.X[7] + 1859775393, 11);
        n3 = this.rotateLeft(n3 + this.H(n4, n5, n2) + this.X[15] + 1859775393, 15);
        this.H1 += n2;
        this.H2 += n3;
        this.H3 += n4;
        this.H4 += n5;
        this.xOff = 0;
        for (int i2 = 0; i2 != this.X.length; ++i2) {
            this.X[i2] = 0;
        }
    }

    @Override
    public Memoable copy() {
        return new MD4Digest(this);
    }

    @Override
    public void reset(Memoable memoable) {
        MD4Digest mD4Digest = (MD4Digest)memoable;
        this.copyIn(mD4Digest);
    }

    @Override
    protected CryptoServiceProperties cryptoServiceProperties() {
        return Utils.getDefaultProperties(this, this.purpose);
    }
}

