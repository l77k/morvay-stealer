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

public class RIPEMD256Digest
extends GeneralDigest {
    private static final int DIGEST_LENGTH = 32;
    private int H0;
    private int H1;
    private int H2;
    private int H3;
    private int H4;
    private int H5;
    private int H6;
    private int H7;
    private int[] X = new int[16];
    private int xOff;

    public RIPEMD256Digest() {
        this(CryptoServicePurpose.ANY);
    }

    public RIPEMD256Digest(CryptoServicePurpose cryptoServicePurpose) {
        super(cryptoServicePurpose);
        CryptoServicesRegistrar.checkConstraints(Utils.getDefaultProperties(this, 128, cryptoServicePurpose));
        this.reset();
    }

    public RIPEMD256Digest(RIPEMD256Digest rIPEMD256Digest) {
        super(rIPEMD256Digest.purpose);
        CryptoServicesRegistrar.checkConstraints(Utils.getDefaultProperties(this, 128, this.purpose));
        this.copyIn(rIPEMD256Digest);
    }

    private void copyIn(RIPEMD256Digest rIPEMD256Digest) {
        super.copyIn(rIPEMD256Digest);
        this.H0 = rIPEMD256Digest.H0;
        this.H1 = rIPEMD256Digest.H1;
        this.H2 = rIPEMD256Digest.H2;
        this.H3 = rIPEMD256Digest.H3;
        this.H4 = rIPEMD256Digest.H4;
        this.H5 = rIPEMD256Digest.H5;
        this.H6 = rIPEMD256Digest.H6;
        this.H7 = rIPEMD256Digest.H7;
        System.arraycopy(rIPEMD256Digest.X, 0, this.X, 0, rIPEMD256Digest.X.length);
        this.xOff = rIPEMD256Digest.xOff;
    }

    @Override
    public String getAlgorithmName() {
        return "RIPEMD256";
    }

    @Override
    public int getDigestSize() {
        return 32;
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
        Pack.intToLittleEndian(this.H0, byArray, n2);
        Pack.intToLittleEndian(this.H1, byArray, n2 + 4);
        Pack.intToLittleEndian(this.H2, byArray, n2 + 8);
        Pack.intToLittleEndian(this.H3, byArray, n2 + 12);
        Pack.intToLittleEndian(this.H4, byArray, n2 + 16);
        Pack.intToLittleEndian(this.H5, byArray, n2 + 20);
        Pack.intToLittleEndian(this.H6, byArray, n2 + 24);
        Pack.intToLittleEndian(this.H7, byArray, n2 + 28);
        this.reset();
        return 32;
    }

    @Override
    public void reset() {
        super.reset();
        this.H0 = 1732584193;
        this.H1 = -271733879;
        this.H2 = -1732584194;
        this.H3 = 271733878;
        this.H4 = 1985229328;
        this.H5 = -19088744;
        this.H6 = -1985229329;
        this.H7 = 19088743;
        this.xOff = 0;
        for (int i2 = 0; i2 != this.X.length; ++i2) {
            this.X[i2] = 0;
        }
    }

    private int RL(int n2, int n3) {
        return n2 << n3 | n2 >>> 32 - n3;
    }

    private int f1(int n2, int n3, int n4) {
        return n2 ^ n3 ^ n4;
    }

    private int f2(int n2, int n3, int n4) {
        return n2 & n3 | ~n2 & n4;
    }

    private int f3(int n2, int n3, int n4) {
        return (n2 | ~n3) ^ n4;
    }

    private int f4(int n2, int n3, int n4) {
        return n2 & n4 | n3 & ~n4;
    }

    private int F1(int n2, int n3, int n4, int n5, int n6, int n7) {
        return this.RL(n2 + this.f1(n3, n4, n5) + n6, n7);
    }

    private int F2(int n2, int n3, int n4, int n5, int n6, int n7) {
        return this.RL(n2 + this.f2(n3, n4, n5) + n6 + 1518500249, n7);
    }

    private int F3(int n2, int n3, int n4, int n5, int n6, int n7) {
        return this.RL(n2 + this.f3(n3, n4, n5) + n6 + 1859775393, n7);
    }

    private int F4(int n2, int n3, int n4, int n5, int n6, int n7) {
        return this.RL(n2 + this.f4(n3, n4, n5) + n6 + -1894007588, n7);
    }

    private int FF1(int n2, int n3, int n4, int n5, int n6, int n7) {
        return this.RL(n2 + this.f1(n3, n4, n5) + n6, n7);
    }

    private int FF2(int n2, int n3, int n4, int n5, int n6, int n7) {
        return this.RL(n2 + this.f2(n3, n4, n5) + n6 + 1836072691, n7);
    }

    private int FF3(int n2, int n3, int n4, int n5, int n6, int n7) {
        return this.RL(n2 + this.f3(n3, n4, n5) + n6 + 1548603684, n7);
    }

    private int FF4(int n2, int n3, int n4, int n5, int n6, int n7) {
        return this.RL(n2 + this.f4(n3, n4, n5) + n6 + 1352829926, n7);
    }

    @Override
    protected void processBlock() {
        int n2 = this.H0;
        int n3 = this.H1;
        int n4 = this.H2;
        int n5 = this.H3;
        int n6 = this.H4;
        int n7 = this.H5;
        int n8 = this.H6;
        int n9 = this.H7;
        n2 = this.F1(n2, n3, n4, n5, this.X[0], 11);
        n5 = this.F1(n5, n2, n3, n4, this.X[1], 14);
        n4 = this.F1(n4, n5, n2, n3, this.X[2], 15);
        n3 = this.F1(n3, n4, n5, n2, this.X[3], 12);
        n2 = this.F1(n2, n3, n4, n5, this.X[4], 5);
        n5 = this.F1(n5, n2, n3, n4, this.X[5], 8);
        n4 = this.F1(n4, n5, n2, n3, this.X[6], 7);
        n3 = this.F1(n3, n4, n5, n2, this.X[7], 9);
        n2 = this.F1(n2, n3, n4, n5, this.X[8], 11);
        n5 = this.F1(n5, n2, n3, n4, this.X[9], 13);
        n4 = this.F1(n4, n5, n2, n3, this.X[10], 14);
        n3 = this.F1(n3, n4, n5, n2, this.X[11], 15);
        n2 = this.F1(n2, n3, n4, n5, this.X[12], 6);
        n5 = this.F1(n5, n2, n3, n4, this.X[13], 7);
        n4 = this.F1(n4, n5, n2, n3, this.X[14], 9);
        n3 = this.F1(n3, n4, n5, n2, this.X[15], 8);
        n6 = this.FF4(n6, n7, n8, n9, this.X[5], 8);
        n9 = this.FF4(n9, n6, n7, n8, this.X[14], 9);
        n8 = this.FF4(n8, n9, n6, n7, this.X[7], 9);
        n7 = this.FF4(n7, n8, n9, n6, this.X[0], 11);
        n6 = this.FF4(n6, n7, n8, n9, this.X[9], 13);
        n9 = this.FF4(n9, n6, n7, n8, this.X[2], 15);
        n8 = this.FF4(n8, n9, n6, n7, this.X[11], 15);
        n7 = this.FF4(n7, n8, n9, n6, this.X[4], 5);
        n6 = this.FF4(n6, n7, n8, n9, this.X[13], 7);
        n9 = this.FF4(n9, n6, n7, n8, this.X[6], 7);
        n8 = this.FF4(n8, n9, n6, n7, this.X[15], 8);
        n7 = this.FF4(n7, n8, n9, n6, this.X[8], 11);
        n6 = this.FF4(n6, n7, n8, n9, this.X[1], 14);
        n9 = this.FF4(n9, n6, n7, n8, this.X[10], 14);
        n8 = this.FF4(n8, n9, n6, n7, this.X[3], 12);
        n7 = this.FF4(n7, n8, n9, n6, this.X[12], 6);
        int n10 = n2;
        n2 = n6;
        n6 = n10;
        n2 = this.F2(n2, n3, n4, n5, this.X[7], 7);
        n5 = this.F2(n5, n2, n3, n4, this.X[4], 6);
        n4 = this.F2(n4, n5, n2, n3, this.X[13], 8);
        n3 = this.F2(n3, n4, n5, n2, this.X[1], 13);
        n2 = this.F2(n2, n3, n4, n5, this.X[10], 11);
        n5 = this.F2(n5, n2, n3, n4, this.X[6], 9);
        n4 = this.F2(n4, n5, n2, n3, this.X[15], 7);
        n3 = this.F2(n3, n4, n5, n2, this.X[3], 15);
        n2 = this.F2(n2, n3, n4, n5, this.X[12], 7);
        n5 = this.F2(n5, n2, n3, n4, this.X[0], 12);
        n4 = this.F2(n4, n5, n2, n3, this.X[9], 15);
        n3 = this.F2(n3, n4, n5, n2, this.X[5], 9);
        n2 = this.F2(n2, n3, n4, n5, this.X[2], 11);
        n5 = this.F2(n5, n2, n3, n4, this.X[14], 7);
        n4 = this.F2(n4, n5, n2, n3, this.X[11], 13);
        n3 = this.F2(n3, n4, n5, n2, this.X[8], 12);
        n6 = this.FF3(n6, n7, n8, n9, this.X[6], 9);
        n9 = this.FF3(n9, n6, n7, n8, this.X[11], 13);
        n8 = this.FF3(n8, n9, n6, n7, this.X[3], 15);
        n7 = this.FF3(n7, n8, n9, n6, this.X[7], 7);
        n6 = this.FF3(n6, n7, n8, n9, this.X[0], 12);
        n9 = this.FF3(n9, n6, n7, n8, this.X[13], 8);
        n8 = this.FF3(n8, n9, n6, n7, this.X[5], 9);
        n7 = this.FF3(n7, n8, n9, n6, this.X[10], 11);
        n6 = this.FF3(n6, n7, n8, n9, this.X[14], 7);
        n9 = this.FF3(n9, n6, n7, n8, this.X[15], 7);
        n8 = this.FF3(n8, n9, n6, n7, this.X[8], 12);
        n7 = this.FF3(n7, n8, n9, n6, this.X[12], 7);
        n6 = this.FF3(n6, n7, n8, n9, this.X[4], 6);
        n9 = this.FF3(n9, n6, n7, n8, this.X[9], 15);
        n8 = this.FF3(n8, n9, n6, n7, this.X[1], 13);
        n7 = this.FF3(n7, n8, n9, n6, this.X[2], 11);
        n10 = n3;
        n3 = n7;
        n7 = n10;
        n2 = this.F3(n2, n3, n4, n5, this.X[3], 11);
        n5 = this.F3(n5, n2, n3, n4, this.X[10], 13);
        n4 = this.F3(n4, n5, n2, n3, this.X[14], 6);
        n3 = this.F3(n3, n4, n5, n2, this.X[4], 7);
        n2 = this.F3(n2, n3, n4, n5, this.X[9], 14);
        n5 = this.F3(n5, n2, n3, n4, this.X[15], 9);
        n4 = this.F3(n4, n5, n2, n3, this.X[8], 13);
        n3 = this.F3(n3, n4, n5, n2, this.X[1], 15);
        n2 = this.F3(n2, n3, n4, n5, this.X[2], 14);
        n5 = this.F3(n5, n2, n3, n4, this.X[7], 8);
        n4 = this.F3(n4, n5, n2, n3, this.X[0], 13);
        n3 = this.F3(n3, n4, n5, n2, this.X[6], 6);
        n2 = this.F3(n2, n3, n4, n5, this.X[13], 5);
        n5 = this.F3(n5, n2, n3, n4, this.X[11], 12);
        n4 = this.F3(n4, n5, n2, n3, this.X[5], 7);
        n3 = this.F3(n3, n4, n5, n2, this.X[12], 5);
        n6 = this.FF2(n6, n7, n8, n9, this.X[15], 9);
        n9 = this.FF2(n9, n6, n7, n8, this.X[5], 7);
        n8 = this.FF2(n8, n9, n6, n7, this.X[1], 15);
        n7 = this.FF2(n7, n8, n9, n6, this.X[3], 11);
        n6 = this.FF2(n6, n7, n8, n9, this.X[7], 8);
        n9 = this.FF2(n9, n6, n7, n8, this.X[14], 6);
        n8 = this.FF2(n8, n9, n6, n7, this.X[6], 6);
        n7 = this.FF2(n7, n8, n9, n6, this.X[9], 14);
        n6 = this.FF2(n6, n7, n8, n9, this.X[11], 12);
        n9 = this.FF2(n9, n6, n7, n8, this.X[8], 13);
        n8 = this.FF2(n8, n9, n6, n7, this.X[12], 5);
        n7 = this.FF2(n7, n8, n9, n6, this.X[2], 14);
        n6 = this.FF2(n6, n7, n8, n9, this.X[10], 13);
        n9 = this.FF2(n9, n6, n7, n8, this.X[0], 13);
        n8 = this.FF2(n8, n9, n6, n7, this.X[4], 7);
        n7 = this.FF2(n7, n8, n9, n6, this.X[13], 5);
        n10 = n4;
        n4 = n8;
        n8 = n10;
        n2 = this.F4(n2, n3, n4, n5, this.X[1], 11);
        n5 = this.F4(n5, n2, n3, n4, this.X[9], 12);
        n4 = this.F4(n4, n5, n2, n3, this.X[11], 14);
        n3 = this.F4(n3, n4, n5, n2, this.X[10], 15);
        n2 = this.F4(n2, n3, n4, n5, this.X[0], 14);
        n5 = this.F4(n5, n2, n3, n4, this.X[8], 15);
        n4 = this.F4(n4, n5, n2, n3, this.X[12], 9);
        n3 = this.F4(n3, n4, n5, n2, this.X[4], 8);
        n2 = this.F4(n2, n3, n4, n5, this.X[13], 9);
        n5 = this.F4(n5, n2, n3, n4, this.X[3], 14);
        n4 = this.F4(n4, n5, n2, n3, this.X[7], 5);
        n3 = this.F4(n3, n4, n5, n2, this.X[15], 6);
        n2 = this.F4(n2, n3, n4, n5, this.X[14], 8);
        n5 = this.F4(n5, n2, n3, n4, this.X[5], 6);
        n4 = this.F4(n4, n5, n2, n3, this.X[6], 5);
        n3 = this.F4(n3, n4, n5, n2, this.X[2], 12);
        n6 = this.FF1(n6, n7, n8, n9, this.X[8], 15);
        n9 = this.FF1(n9, n6, n7, n8, this.X[6], 5);
        n8 = this.FF1(n8, n9, n6, n7, this.X[4], 8);
        n7 = this.FF1(n7, n8, n9, n6, this.X[1], 11);
        n6 = this.FF1(n6, n7, n8, n9, this.X[3], 14);
        n9 = this.FF1(n9, n6, n7, n8, this.X[11], 14);
        n8 = this.FF1(n8, n9, n6, n7, this.X[15], 6);
        n7 = this.FF1(n7, n8, n9, n6, this.X[0], 14);
        n6 = this.FF1(n6, n7, n8, n9, this.X[5], 6);
        n9 = this.FF1(n9, n6, n7, n8, this.X[12], 9);
        n8 = this.FF1(n8, n9, n6, n7, this.X[2], 12);
        n7 = this.FF1(n7, n8, n9, n6, this.X[13], 9);
        n6 = this.FF1(n6, n7, n8, n9, this.X[9], 12);
        n9 = this.FF1(n9, n6, n7, n8, this.X[7], 5);
        n8 = this.FF1(n8, n9, n6, n7, this.X[10], 15);
        n7 = this.FF1(n7, n8, n9, n6, this.X[14], 8);
        n10 = n5;
        n5 = n9;
        n9 = n10;
        this.H0 += n2;
        this.H1 += n3;
        this.H2 += n4;
        this.H3 += n5;
        this.H4 += n6;
        this.H5 += n7;
        this.H6 += n8;
        this.H7 += n9;
        this.xOff = 0;
        for (int i2 = 0; i2 != this.X.length; ++i2) {
            this.X[i2] = 0;
        }
    }

    @Override
    public Memoable copy() {
        return new RIPEMD256Digest(this);
    }

    @Override
    public void reset(Memoable memoable) {
        RIPEMD256Digest rIPEMD256Digest = (RIPEMD256Digest)memoable;
        this.copyIn(rIPEMD256Digest);
    }

    @Override
    protected CryptoServiceProperties cryptoServiceProperties() {
        return Utils.getDefaultProperties(this, this.purpose);
    }
}

