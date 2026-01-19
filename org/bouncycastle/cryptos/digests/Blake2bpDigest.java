/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.digests.Blake2bDigest;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class Blake2bpDigest
implements ExtendedDigest {
    private int bufferPos = 0;
    private int keyLength = 0;
    private int digestLength;
    private int fanout;
    private int depth;
    private int nodeOffset = 0;
    private long innerHashLength;
    private Blake2bDigest[] S = new Blake2bDigest[4];
    private Blake2bDigest root;
    private byte[] buffer = null;
    private byte[] salt = null;
    private byte[] param = null;
    private byte[] key = null;
    private final int BLAKE2B_BLOCKBYTES = 128;
    private final int BLAKE2B_KEYBYTES = 64;
    private final int BLAKE2B_OUTBYTES = 64;
    private final int PARALLELISM_DEGREE = 4;
    private final byte[] singleByte = new byte[1];

    public Blake2bpDigest(byte[] byArray) {
        this.param = new byte[64];
        this.buffer = new byte[512];
        this.init(byArray);
    }

    @Override
    public String getAlgorithmName() {
        return "BLAKE2bp";
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
        int n6 = 1024 - n5;
        if (n5 != 0 && n3 >= n6) {
            System.arraycopy(byArray, n2, this.buffer, n5, n6);
            for (n4 = 0; n4 < 4; ++n4) {
                this.S[n4].update(this.buffer, n4 * 128, 128);
            }
            n2 += n6;
            n3 -= n6;
            n5 = 0;
        }
        for (n4 = 0; n4 < 4; ++n4) {
            int n7 = n2;
            n7 += n4 * 128;
            for (int i2 = n3; i2 >= 512; i2 -= 512) {
                this.S[n4].update(byArray, n7, 128);
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
        byte[][] byArray2 = new byte[4][64];
        int n4 = 0;
        for (n3 = 0; n3 < 4; ++n3) {
            if (this.bufferPos > n3 * 128) {
                n4 = this.bufferPos - n3 * 128;
                if (n4 > 128) {
                    n4 = 128;
                }
                this.S[n3].update(this.buffer, n3 * 128, n4);
            }
            this.S[n3].doFinal(byArray2[n3], 0);
        }
        for (n3 = 0; n3 < 4; ++n3) {
            this.root.update(byArray2[n3], 0, 64);
        }
        n3 = this.root.doFinal(byArray, n2);
        this.reset();
        return n3;
    }

    @Override
    public void reset() {
        this.bufferPos = 0;
        this.digestLength = 64;
        this.root.reset();
        for (int i2 = 0; i2 < 4; ++i2) {
            this.S[i2].reset();
        }
        this.root.setAsLastNode();
        this.S[3].setAsLastNode();
        if (this.key != null) {
            byte[] byArray = new byte[128];
            System.arraycopy(this.key, 0, byArray, 0, this.keyLength);
            for (int i3 = 0; i3 < 4; ++i3) {
                this.S[i3].update(byArray, 0, 128);
            }
        }
    }

    @Override
    public int getByteLength() {
        return 0;
    }

    private void init(byte[] byArray) {
        if (byArray != null && byArray.length > 0) {
            this.keyLength = byArray.length;
            if (this.keyLength > 64) {
                throw new IllegalArgumentException("Keys > 64 bytes are not supported");
            }
            this.key = Arrays.clone(byArray);
        }
        this.bufferPos = 0;
        this.digestLength = 64;
        this.fanout = 4;
        this.depth = 2;
        this.innerHashLength = 64L;
        this.param[0] = (byte)this.digestLength;
        this.param[1] = (byte)this.keyLength;
        this.param[2] = (byte)this.fanout;
        this.param[3] = (byte)this.depth;
        this.param[16] = 1;
        this.param[17] = (byte)this.innerHashLength;
        this.root = new Blake2bDigest(null, this.param);
        Pack.intToLittleEndian(this.nodeOffset, this.param, 8);
        this.param[16] = 0;
        for (int i2 = 0; i2 < 4; ++i2) {
            Pack.intToLittleEndian(i2, this.param, 8);
            this.S[i2] = new Blake2bDigest(null, this.param);
        }
        this.root.setAsLastNode();
        this.S[3].setAsLastNode();
        if (byArray != null && this.keyLength > 0) {
            byte[] byArray2 = new byte[128];
            System.arraycopy(byArray, 0, byArray2, 0, this.keyLength);
            for (int i3 = 0; i3 < 4; ++i3) {
                this.S[i3].update(byArray2, 0, 128);
            }
        }
    }
}

