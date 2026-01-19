/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import java.io.ByteArrayOutputStream;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Bytes;

public class PhotonBeetleDigest
implements Digest {
    private byte[] state;
    private byte[][] state_2d;
    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private final int INITIAL_RATE_INBYTES = 16;
    private int RATE_INBYTES = 4;
    private int SQUEEZE_RATE_INBYTES = 16;
    private int STATE_INBYTES = 32;
    private int TAG_INBYTES = 32;
    private int LAST_THREE_BITS_OFFSET = 5;
    private int ROUND = 12;
    private int D = 8;
    private int Dq = 3;
    private int Dr = 7;
    private int DSquare = 64;
    private int S = 4;
    private int S_1 = 3;
    private byte[][] RC = new byte[][]{{1, 3, 7, 14, 13, 11, 6, 12, 9, 2, 5, 10}, {0, 2, 6, 15, 12, 10, 7, 13, 8, 3, 4, 11}, {2, 0, 4, 13, 14, 8, 5, 15, 10, 1, 6, 9}, {6, 4, 0, 9, 10, 12, 1, 11, 14, 5, 2, 13}, {14, 12, 8, 1, 2, 4, 9, 3, 6, 13, 10, 5}, {15, 13, 9, 0, 3, 5, 8, 2, 7, 12, 11, 4}, {13, 15, 11, 2, 1, 7, 10, 0, 5, 14, 9, 6}, {9, 11, 15, 6, 5, 3, 14, 4, 1, 10, 13, 2}};
    private byte[][] MixColMatrix = new byte[][]{{2, 4, 2, 11, 2, 8, 5, 6}, {12, 9, 8, 13, 7, 7, 5, 2}, {4, 4, 13, 13, 9, 4, 13, 9}, {1, 6, 5, 1, 12, 13, 15, 14}, {15, 12, 9, 13, 14, 5, 14, 13}, {9, 14, 5, 15, 4, 12, 9, 6}, {12, 2, 2, 10, 3, 1, 1, 14}, {15, 1, 13, 10, 5, 10, 2, 3}};
    private byte[] sbox = new byte[]{12, 5, 6, 11, 9, 0, 10, 13, 3, 14, 15, 8, 4, 7, 1, 2};

    public PhotonBeetleDigest() {
        this.state = new byte[this.STATE_INBYTES];
        this.state_2d = new byte[this.D][this.D];
    }

    @Override
    public String getAlgorithmName() {
        return "Photon-Beetle Hash";
    }

    @Override
    public int getDigestSize() {
        return this.TAG_INBYTES;
    }

    @Override
    public void update(byte by) {
        this.buffer.write(by);
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) {
        if (n2 + n3 > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        this.buffer.write(byArray, n2, n3);
    }

    @Override
    public int doFinal(byte[] byArray, int n2) {
        if (32 + n2 > byArray.length) {
            throw new OutputLengthException("output buffer is too short");
        }
        byte[] byArray2 = this.buffer.toByteArray();
        int n3 = byArray2.length;
        if (n3 == 0) {
            int n4 = this.STATE_INBYTES - 1;
            this.state[n4] = (byte)(this.state[n4] ^ 1 << this.LAST_THREE_BITS_OFFSET);
        } else if (n3 <= 16) {
            System.arraycopy(byArray2, 0, this.state, 0, n3);
            if (n3 < 16) {
                int n5 = n3;
                this.state[n5] = (byte)(this.state[n5] ^ 1);
            }
            int n6 = this.STATE_INBYTES - 1;
            this.state[n6] = (byte)(this.state[n6] ^ (n3 < 16 ? 1 : 2) << this.LAST_THREE_BITS_OFFSET);
        } else {
            int n7;
            System.arraycopy(byArray2, 0, this.state, 0, 16);
            int n8 = ((n3 -= 16) + this.RATE_INBYTES - 1) / this.RATE_INBYTES;
            for (n7 = 0; n7 < n8 - 1; ++n7) {
                this.PHOTON_Permutation();
                Bytes.xorTo(this.RATE_INBYTES, byArray2, 16 + n7 * this.RATE_INBYTES, this.state, 0);
            }
            this.PHOTON_Permutation();
            int n9 = n3 - n7 * this.RATE_INBYTES;
            Bytes.xorTo(n9, byArray2, 16 + n7 * this.RATE_INBYTES, this.state, 0);
            if (n9 < this.RATE_INBYTES) {
                int n10 = n9;
                this.state[n10] = (byte)(this.state[n10] ^ 1);
            }
            int n11 = this.STATE_INBYTES - 1;
            this.state[n11] = (byte)(this.state[n11] ^ (n3 % this.RATE_INBYTES == 0 ? 1 : 2) << this.LAST_THREE_BITS_OFFSET);
        }
        this.PHOTON_Permutation();
        System.arraycopy(this.state, 0, byArray, n2, this.SQUEEZE_RATE_INBYTES);
        this.PHOTON_Permutation();
        System.arraycopy(this.state, 0, byArray, n2 + this.SQUEEZE_RATE_INBYTES, this.TAG_INBYTES - this.SQUEEZE_RATE_INBYTES);
        this.reset();
        return this.TAG_INBYTES;
    }

    @Override
    public void reset() {
        this.buffer.reset();
        Arrays.fill(this.state, (byte)0);
    }

    void PHOTON_Permutation() {
        int n2;
        for (n2 = 0; n2 < this.DSquare; ++n2) {
            this.state_2d[n2 >>> this.Dq][n2 & this.Dr] = (byte)((this.state[n2 >> 1] & 0xFF) >>> 4 * (n2 & 1) & 0xF);
        }
        for (int i2 = 0; i2 < this.ROUND; ++i2) {
            int n3;
            for (n2 = 0; n2 < this.D; ++n2) {
                byte[] byArray = this.state_2d[n2];
                byArray[0] = (byte)(byArray[0] ^ this.RC[n2][i2]);
            }
            for (n2 = 0; n2 < this.D; ++n2) {
                for (n3 = 0; n3 < this.D; ++n3) {
                    this.state_2d[n2][n3] = this.sbox[this.state_2d[n2][n3]];
                }
            }
            for (n2 = 1; n2 < this.D; ++n2) {
                System.arraycopy(this.state_2d[n2], 0, this.state, 0, this.D);
                System.arraycopy(this.state, n2, this.state_2d[n2], 0, this.D - n2);
                System.arraycopy(this.state, 0, this.state_2d[n2], this.D - n2, n2);
            }
            for (n3 = 0; n3 < this.D; ++n3) {
                for (n2 = 0; n2 < this.D; ++n2) {
                    int n4;
                    int n5;
                    int n6 = 0;
                    for (int i3 = 0; i3 < this.D; ++i3) {
                        n5 = this.MixColMatrix[n2][i3];
                        n4 = this.state_2d[i3][n3];
                        n6 ^= n5 * (n4 & 1);
                        n6 ^= n5 * (n4 & 2);
                        n6 ^= n5 * (n4 & 4);
                        n6 ^= n5 * (n4 & 8);
                    }
                    n5 = n6 >>> 4;
                    n6 = n6 & 0xF ^ n5 ^ n5 << 1;
                    n4 = n6 >>> 4;
                    n6 = n6 & 0xF ^ n4 ^ n4 << 1;
                    this.state[n2] = (byte)n6;
                }
                for (n2 = 0; n2 < this.D; ++n2) {
                    this.state_2d[n2][n3] = this.state[n2];
                }
            }
        }
        for (n2 = 0; n2 < this.DSquare; n2 += 2) {
            this.state[n2 >>> 1] = (byte)(this.state_2d[n2 >>> this.Dq][n2 & this.Dr] & 0xF | (this.state_2d[n2 >>> this.Dq][n2 + 1 & this.Dr] & 0xF) << 4);
        }
    }
}

