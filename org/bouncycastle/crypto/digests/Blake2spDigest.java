/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.digests.Blake2sDigest;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class Blake2spDigest
implements ExtendedDigest {
    private int bufferPos = 0;
    private int keyLength = 0;
    private int digestLength;
    private int fanout;
    private int depth;
    private int nodeOffset = 0;
    private long innerHashLength;
    private Blake2sDigest[] S = new Blake2sDigest[8];
    private Blake2sDigest root;
    private byte[] buffer = null;
    private byte[] salt = null;
    private byte[] param = null;
    private byte[] key = null;
    private final int BLAKE2S_BLOCKBYTES = 64;
    private final int BLAKE2S_KEYBYTES = 32;
    private final int BLAKE2S_OUTBYTES = 32;
    private final int PARALLELISM_DEGREE = 8;
    private final byte[] singleByte = new byte[1];

    public Blake2spDigest(byte[] byArray) {
        this.param = new byte[32];
        this.buffer = new byte[512];
        this.init(byArray);
    }

    @Override
    public String getAlgorithmName() {
        return "BLAKE2sp";
    }

    @Override
    public int getDigestSize() {
        return this.digestLength;
    }

    @Override
    public void update(byte by) {
        this.singleByte[0] = by;
        this.update(this.singleByte, 0, 1);
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) {
        int n4;
        int n5 = this.bufferPos;
        int n6 = 512 - n5;
        if (n5 != 0 && n3 >= n6) {
            System.arraycopy(byArray, n2, this.buffer, n5, n6);
            for (n4 = 0; n4 < 8; ++n4) {
                this.S[n4].update(this.buffer, n4 * 64, 64);
            }
            n2 += n6;
            n3 -= n6;
            n5 = 0;
        }
        for (n4 = 0; n4 < 8; ++n4) {
            int n7 = n2;
            n7 += n4 * 64;
            for (int i2 = n3; i2 >= 512; i2 -= 512) {
                this.S[n4].update(byArray, n7, 64);
                n7 += 512;
            }
        }
        n2 += n3 - n3 % 512;
        if ((n3 %= 512) > 0) {
            System.arraycopy(byArray, n2, this.buffer, n5, n3);
        }
        this.bufferPos = n5 + n3;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) {
        int n3;
        byte[][] byArray2 = new byte[8][32];
        int n4 = 0;
        for (n3 = 0; n3 < 8; ++n3) {
            if (this.bufferPos > n3 * 64) {
                n4 = this.bufferPos - n3 * 64;
                if (n4 > 64) {
                    n4 = 64;
                }
                this.S[n3].update(this.buffer, n3 * 64, n4);
            }
            this.S[n3].doFinal(byArray2[n3], 0);
        }
        for (n3 = 0; n3 < 8; ++n3) {
            this.root.update(byArray2[n3], 0, 32);
        }
        n3 = this.root.doFinal(byArray, n2);
        this.reset();
        return n3;
    }

    @Override
    public void reset() {
        this.bufferPos = 0;
        this.digestLength = 32;
        this.root.reset();
        for (int i2 = 0; i2 < 8; ++i2) {
            this.S[i2].reset();
        }
        this.root.setAsLastNode();
        this.S[7].setAsLastNode();
        if (this.key != null) {
            byte[] byArray = new byte[64];
            System.arraycopy(this.key, 0, byArray, 0, this.keyLength);
            for (int i3 = 0; i3 < 8; ++i3) {
                this.S[i3].update(byArray, 0, 64);
            }
        }
    }

    @Override
    public int getByteLength() {
        return 64;
    }

    private void init(byte[] byArray) {
        if (byArray != null && byArray.length > 0) {
            this.keyLength = byArray.length;
            if (this.keyLength > 32) {
                throw new IllegalArgumentException("Keys > 32 bytes are not supported");
            }
            this.key = Arrays.clone(byArray);
        }
        this.bufferPos = 0;
        this.digestLength = 32;
        this.fanout = 8;
        this.depth = 2;
        this.innerHashLength = 32L;
        this.param[0] = (byte)this.digestLength;
        this.param[1] = (byte)this.keyLength;
        this.param[2] = (byte)this.fanout;
        this.param[3] = (byte)this.depth;
        Pack.intToLittleEndian(0, this.param, 8);
        this.param[14] = 1;
        this.param[15] = (byte)this.innerHashLength;
        this.root = new Blake2sDigest(null, this.param);
        Pack.intToLittleEndian(this.nodeOffset, this.param, 8);
        this.param[14] = 0;
        for (int i2 = 0; i2 < 8; ++i2) {
            Pack.intToLittleEndian(i2, this.param, 8);
            this.S[i2] = new Blake2sDigest(null, this.param);
        }
        this.root.setAsLastNode();
        this.S[7].setAsLastNode();
        if (byArray != null && this.keyLength > 0) {
            byte[] byArray2 = new byte[64];
            System.arraycopy(byArray, 0, byArray2, 0, this.keyLength);
            for (int i3 = 0; i3 < 8; ++i3) {
                this.S[i3].update(byArray2, 0, 64);
            }
        }
    }
}

