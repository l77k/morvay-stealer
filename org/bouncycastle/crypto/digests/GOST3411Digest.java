/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CryptoServiceProperties;
import org.bouncycastle.crypto.CryptoServicePurpose;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.digests.Utils;
import org.bouncycastle.crypto.engines.GOST28147Engine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithSBox;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Memoable;
import org.bouncycastle.util.Pack;

public class GOST3411Digest
implements ExtendedDigest,
Memoable {
    private static final int DIGEST_LENGTH = 32;
    private final CryptoServicePurpose purpose;
    private byte[] H = new byte[32];
    private byte[] L = new byte[32];
    private byte[] M = new byte[32];
    private byte[] Sum = new byte[32];
    private byte[][] C = new byte[4][32];
    private byte[] xBuf = new byte[32];
    private int xBufOff;
    private long byteCount;
    private BlockCipher cipher = new GOST28147Engine();
    private byte[] sBox;
    private byte[] K = new byte[32];
    byte[] a = new byte[8];
    short[] wS = new short[16];
    short[] w_S = new short[16];
    byte[] S = new byte[32];
    byte[] U = new byte[32];
    byte[] V = new byte[32];
    byte[] W = new byte[32];
    private static final byte[] C2 = new byte[]{0, -1, 0, -1, 0, -1, 0, -1, -1, 0, -1, 0, -1, 0, -1, 0, 0, -1, -1, 0, -1, 0, 0, -1, -1, 0, 0, 0, -1, -1, 0, -1};

    public GOST3411Digest() {
        this(CryptoServicePurpose.ANY);
    }

    public GOST3411Digest(CryptoServicePurpose cryptoServicePurpose) {
        this.purpose = cryptoServicePurpose;
        CryptoServicesRegistrar.checkConstraints(this.cryptoServiceProperties());
        this.sBox = GOST28147Engine.getSBox("D-A");
        this.cipher.init(true, new ParametersWithSBox(null, this.sBox));
        this.reset();
    }

    public GOST3411Digest(byte[] byArray) {
        this(byArray, CryptoServicePurpose.ANY);
    }

    public GOST3411Digest(byte[] byArray, CryptoServicePurpose cryptoServicePurpose) {
        this.purpose = cryptoServicePurpose;
        CryptoServicesRegistrar.checkConstraints(this.cryptoServiceProperties());
        this.sBox = Arrays.clone(byArray);
        this.cipher.init(true, new ParametersWithSBox(null, this.sBox));
        this.reset();
    }

    public GOST3411Digest(GOST3411Digest gOST3411Digest) {
        this.purpose = gOST3411Digest.purpose;
        CryptoServicesRegistrar.checkConstraints(this.cryptoServiceProperties());
        this.reset(gOST3411Digest);
    }

    @Override
    public String getAlgorithmName() {
        return "GOST3411";
    }

    @Override
    public int getDigestSize() {
        return 32;
    }

    @Override
    public void update(byte by) {
        this.xBuf[this.xBufOff++] = by;
        if (this.xBufOff == this.xBuf.length) {
            this.sumByteArray(this.xBuf);
            this.processBlock(this.xBuf, 0);
            this.xBufOff = 0;
        }
        ++this.byteCount;
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) {
        while (this.xBufOff != 0 && n3 > 0) {
            this.update(byArray[n2]);
            ++n2;
            --n3;
        }
        while (n3 >= this.xBuf.length) {
            System.arraycopy(byArray, n2, this.xBuf, 0, this.xBuf.length);
            this.sumByteArray(this.xBuf);
            this.processBlock(this.xBuf, 0);
            n2 += this.xBuf.length;
            n3 -= this.xBuf.length;
            this.byteCount += (long)this.xBuf.length;
        }
        while (n3 > 0) {
            this.update(byArray[n2]);
            ++n2;
            --n3;
        }
    }

    private byte[] P(byte[] byArray) {
        for (int i2 = 0; i2 < 8; ++i2) {
            this.K[4 * i2] = byArray[i2];
            this.K[1 + 4 * i2] = byArray[8 + i2];
            this.K[2 + 4 * i2] = byArray[16 + i2];
            this.K[3 + 4 * i2] = byArray[24 + i2];
        }
        return this.K;
    }

    private byte[] A(byte[] byArray) {
        for (int i2 = 0; i2 < 8; ++i2) {
            this.a[i2] = (byte)(byArray[i2] ^ byArray[i2 + 8]);
        }
        System.arraycopy(byArray, 8, byArray, 0, 24);
        System.arraycopy(this.a, 0, byArray, 24, 8);
        return byArray;
    }

    private void E(byte[] byArray, byte[] byArray2, int n2, byte[] byArray3, int n3) {
        this.cipher.init(true, new KeyParameter(byArray));
        this.cipher.processBlock(byArray3, n3, byArray2, n2);
    }

    private void fw(byte[] byArray) {
        this.cpyBytesToShort(byArray, this.wS);
        this.w_S[15] = (short)(this.wS[0] ^ this.wS[1] ^ this.wS[2] ^ this.wS[3] ^ this.wS[12] ^ this.wS[15]);
        System.arraycopy(this.wS, 1, this.w_S, 0, 15);
        this.cpyShortToBytes(this.w_S, byArray);
    }

    protected void processBlock(byte[] byArray, int n2) {
        int n3;
        System.arraycopy(byArray, n2, this.M, 0, 32);
        System.arraycopy(this.H, 0, this.U, 0, 32);
        System.arraycopy(this.M, 0, this.V, 0, 32);
        for (n3 = 0; n3 < 32; ++n3) {
            this.W[n3] = (byte)(this.U[n3] ^ this.V[n3]);
        }
        this.E(this.P(this.W), this.S, 0, this.H, 0);
        for (n3 = 1; n3 < 4; ++n3) {
            int n4;
            byte[] byArray2 = this.A(this.U);
            for (n4 = 0; n4 < 32; ++n4) {
                this.U[n4] = (byte)(byArray2[n4] ^ this.C[n3][n4]);
            }
            this.V = this.A(this.A(this.V));
            for (n4 = 0; n4 < 32; ++n4) {
                this.W[n4] = (byte)(this.U[n4] ^ this.V[n4]);
            }
            this.E(this.P(this.W), this.S, n3 * 8, this.H, n3 * 8);
        }
        for (n3 = 0; n3 < 12; ++n3) {
            this.fw(this.S);
        }
        for (n3 = 0; n3 < 32; ++n3) {
            this.S[n3] = (byte)(this.S[n3] ^ this.M[n3]);
        }
        this.fw(this.S);
        for (n3 = 0; n3 < 32; ++n3) {
            this.S[n3] = (byte)(this.H[n3] ^ this.S[n3]);
        }
        for (n3 = 0; n3 < 61; ++n3) {
            this.fw(this.S);
        }
        System.arraycopy(this.S, 0, this.H, 0, this.H.length);
    }

    private void finish() {
        Pack.longToLittleEndian(this.byteCount * 8L, this.L, 0);
        while (this.xBufOff != 0) {
            this.update((byte)0);
        }
        this.processBlock(this.L, 0);
        this.processBlock(this.Sum, 0);
    }

    @Override
    public int doFinal(byte[] byArray, int n2) {
        this.finish();
        System.arraycopy(this.H, 0, byArray, n2, this.H.length);
        this.reset();
        return 32;
    }

    @Override
    public void reset() {
        int n2;
        this.byteCount = 0L;
        this.xBufOff = 0;
        for (n2 = 0; n2 < this.H.length; ++n2) {
            this.H[n2] = 0;
        }
        for (n2 = 0; n2 < this.L.length; ++n2) {
            this.L[n2] = 0;
        }
        for (n2 = 0; n2 < this.M.length; ++n2) {
            this.M[n2] = 0;
        }
        for (n2 = 0; n2 < this.C[1].length; ++n2) {
            this.C[1][n2] = 0;
        }
        for (n2 = 0; n2 < this.C[3].length; ++n2) {
            this.C[3][n2] = 0;
        }
        for (n2 = 0; n2 < this.Sum.length; ++n2) {
            this.Sum[n2] = 0;
        }
        for (n2 = 0; n2 < this.xBuf.length; ++n2) {
            this.xBuf[n2] = 0;
        }
        System.arraycopy(C2, 0, this.C[2], 0, C2.length);
    }

    private void sumByteArray(byte[] byArray) {
        int n2 = 0;
        for (int i2 = 0; i2 != this.Sum.length; ++i2) {
            int n3 = (this.Sum[i2] & 0xFF) + (byArray[i2] & 0xFF) + n2;
            this.Sum[i2] = (byte)n3;
            n2 = n3 >>> 8;
        }
    }

    private void cpyBytesToShort(byte[] byArray, short[] sArray) {
        for (int i2 = 0; i2 < byArray.length / 2; ++i2) {
            sArray[i2] = (short)(byArray[i2 * 2 + 1] << 8 & 0xFF00 | byArray[i2 * 2] & 0xFF);
        }
    }

    private void cpyShortToBytes(short[] sArray, byte[] byArray) {
        for (int i2 = 0; i2 < byArray.length / 2; ++i2) {
            byArray[i2 * 2 + 1] = (byte)(sArray[i2] >> 8);
            byArray[i2 * 2] = (byte)sArray[i2];
        }
    }

    @Override
    public int getByteLength() {
        return 32;
    }

    @Override
    public Memoable copy() {
        return new GOST3411Digest(this);
    }

    @Override
    public void reset(Memoable memoable) {
        GOST3411Digest gOST3411Digest = (GOST3411Digest)memoable;
        this.sBox = gOST3411Digest.sBox;
        this.cipher.init(true, new ParametersWithSBox(null, this.sBox));
        this.reset();
        System.arraycopy(gOST3411Digest.H, 0, this.H, 0, gOST3411Digest.H.length);
        System.arraycopy(gOST3411Digest.L, 0, this.L, 0, gOST3411Digest.L.length);
        System.arraycopy(gOST3411Digest.M, 0, this.M, 0, gOST3411Digest.M.length);
        System.arraycopy(gOST3411Digest.Sum, 0, this.Sum, 0, gOST3411Digest.Sum.length);
        System.arraycopy(gOST3411Digest.C[1], 0, this.C[1], 0, gOST3411Digest.C[1].length);
        System.arraycopy(gOST3411Digest.C[2], 0, this.C[2], 0, gOST3411Digest.C[2].length);
        System.arraycopy(gOST3411Digest.C[3], 0, this.C[3], 0, gOST3411Digest.C[3].length);
        System.arraycopy(gOST3411Digest.xBuf, 0, this.xBuf, 0, gOST3411Digest.xBuf.length);
        this.xBufOff = gOST3411Digest.xBufOff;
        this.byteCount = gOST3411Digest.byteCount;
    }

    protected CryptoServiceProperties cryptoServiceProperties() {
        return Utils.getDefaultProperties(this, 256, this.purpose);
    }
}

