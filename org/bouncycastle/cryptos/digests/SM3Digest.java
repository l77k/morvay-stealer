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

public class SM3Digest
extends GeneralDigest {
    private static final int DIGEST_LENGTH = 32;
    private static final int BLOCK_SIZE = 16;
    private int[] V = new int[8];
    private int[] inwords = new int[16];
    private int xOff;
    private int[] W = new int[68];
    private static final int[] T;

    public SM3Digest() {
        this(CryptoServicePurpose.ANY);
    }

    public SM3Digest(CryptoServicePurpose cryptoServicePurpose) {
        super(cryptoServicePurpose);
        CryptoServicesRegistrar.checkConstraints(this.cryptoServiceProperties());
        this.reset();
    }

    public SM3Digest(SM3Digest sM3Digest) {
        super(sM3Digest);
        CryptoServicesRegistrar.checkConstraints(this.cryptoServiceProperties());
        this.copyIn(sM3Digest);
    }

    private void copyIn(SM3Digest sM3Digest) {
        System.arraycopy(sM3Digest.V, 0, this.V, 0, this.V.length);
        System.arraycopy(sM3Digest.inwords, 0, this.inwords, 0, this.inwords.length);
        this.xOff = sM3Digest.xOff;
    }

    @Override
    public String getAlgorithmName() {
        return "SM3";
    }

    @Override
    public int getDigestSize() {
        return 32;
    }

    @Override
    public Memoable copy() {
        return new SM3Digest(this);
    }

    @Override
    public void reset(Memoable memoable) {
        SM3Digest sM3Digest = (SM3Digest)memoable;
        super.copyIn(sM3Digest);
        this.copyIn(sM3Digest);
    }

    @Override
    public void reset() {
        super.reset();
        this.V[0] = 1937774191;
        this.V[1] = 1226093241;
        this.V[2] = 388252375;
        this.V[3] = -628488704;
        this.V[4] = -1452330820;
        this.V[5] = 372324522;
        this.V[6] = -477237683;
        this.V[7] = -1325724082;
        this.xOff = 0;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) {
        this.finish();
        Pack.intToBigEndian(this.V, byArray, n2);
        this.reset();
        return 32;
    }

    @Override
    protected void processWord(byte[] byArray, int n2) {
        this.inwords[this.xOff++] = Pack.bigEndianToInt(byArray, n2);
        if (this.xOff >= 16) {
            this.processBlock();
        }
    }

    @Override
    protected void processLength(long l2) {
        if (this.xOff > 14) {
            this.inwords[this.xOff] = 0;
            ++this.xOff;
            this.processBlock();
        }
        while (this.xOff < 14) {
            this.inwords[this.xOff] = 0;
            ++this.xOff;
        }
        this.inwords[this.xOff++] = (int)(l2 >>> 32);
        this.inwords[this.xOff++] = (int)l2;
    }

    private int P0(int n2) {
        int n3 = n2 << 9 | n2 >>> 23;
        int n4 = n2 << 17 | n2 >>> 15;
        return n2 ^ n3 ^ n4;
    }

    private int P1(int n2) {
        int n3 = n2 << 15 | n2 >>> 17;
        int n4 = n2 << 23 | n2 >>> 9;
        return n2 ^ n3 ^ n4;
    }

    private int FF0(int n2, int n3, int n4) {
        return n2 ^ n3 ^ n4;
    }

    private int FF1(int n2, int n3, int n4) {
        return n2 & n3 | n2 & n4 | n3 & n4;
    }

    private int GG0(int n2, int n3, int n4) {
        return n2 ^ n3 ^ n4;
    }

    private int GG1(int n2, int n3, int n4) {
        return n2 & n3 | ~n2 & n4;
    }

    @Override
    protected void processBlock() {
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        int n10;
        int n11;
        int n12;
        int n13;
        int n14;
        int n15;
        for (n15 = 0; n15 < 16; ++n15) {
            this.W[n15] = this.inwords[n15];
        }
        for (n15 = 16; n15 < 68; ++n15) {
            n14 = this.W[n15 - 3];
            n13 = n14 << 15 | n14 >>> 17;
            n12 = this.W[n15 - 13];
            n11 = n12 << 7 | n12 >>> 25;
            this.W[n15] = this.P1(this.W[n15 - 16] ^ this.W[n15 - 9] ^ n13) ^ n11 ^ this.W[n15 - 6];
        }
        n15 = this.V[0];
        n14 = this.V[1];
        n13 = this.V[2];
        n12 = this.V[3];
        n11 = this.V[4];
        int n16 = this.V[5];
        int n17 = this.V[6];
        int n18 = this.V[7];
        for (n10 = 0; n10 < 16; ++n10) {
            n9 = n15 << 12 | n15 >>> 20;
            n8 = n9 + n11 + T[n10];
            n7 = n8 << 7 | n8 >>> 25;
            n6 = n7 ^ n9;
            n5 = this.W[n10];
            n4 = n5 ^ this.W[n10 + 4];
            n3 = this.FF0(n15, n14, n13) + n12 + n6 + n4;
            n2 = this.GG0(n11, n16, n17) + n18 + n7 + n5;
            n12 = n13;
            n13 = n14 << 9 | n14 >>> 23;
            n14 = n15;
            n15 = n3;
            n18 = n17;
            n17 = n16 << 19 | n16 >>> 13;
            n16 = n11;
            n11 = this.P0(n2);
        }
        for (n10 = 16; n10 < 64; ++n10) {
            n9 = n15 << 12 | n15 >>> 20;
            n8 = n9 + n11 + T[n10];
            n7 = n8 << 7 | n8 >>> 25;
            n6 = n7 ^ n9;
            n5 = this.W[n10];
            n4 = n5 ^ this.W[n10 + 4];
            n3 = this.FF1(n15, n14, n13) + n12 + n6 + n4;
            n2 = this.GG1(n11, n16, n17) + n18 + n7 + n5;
            n12 = n13;
            n13 = n14 << 9 | n14 >>> 23;
            n14 = n15;
            n15 = n3;
            n18 = n17;
            n17 = n16 << 19 | n16 >>> 13;
            n16 = n11;
            n11 = this.P0(n2);
        }
        this.V[0] = this.V[0] ^ n15;
        this.V[1] = this.V[1] ^ n14;
        this.V[2] = this.V[2] ^ n13;
        this.V[3] = this.V[3] ^ n12;
        this.V[4] = this.V[4] ^ n11;
        this.V[5] = this.V[5] ^ n16;
        this.V[6] = this.V[6] ^ n17;
        this.V[7] = this.V[7] ^ n18;
        this.xOff = 0;
    }

    @Override
    protected CryptoServiceProperties cryptoServiceProperties() {
        return Utils.getDefaultProperties(this, 256, this.purpose);
    }

    static {
        int n2;
        int n3;
        T = new int[64];
        for (n3 = 0; n3 < 16; ++n3) {
            n2 = 2043430169;
            SM3Digest.T[n3] = n2 << n3 | n2 >>> 32 - n3;
        }
        for (n3 = 16; n3 < 64; ++n3) {
            n2 = n3 % 32;
            int n4 = 2055708042;
            SM3Digest.T[n3] = n4 << n2 | n4 >>> 32 - n2;
        }
    }
}

