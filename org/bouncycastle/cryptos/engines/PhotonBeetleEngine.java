/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.engines.AEADBufferBaseEngine;

public class PhotonBeetleEngine
extends AEADBufferBaseEngine {
    private boolean input_empty;
    private byte[] K;
    private byte[] N;
    private byte[] state;
    private byte[][] state_2d;
    private int aadLen;
    private int messageLen;
    private final int RATE_INBYTES_HALF;
    private final int STATE_INBYTES;
    private final int LAST_THREE_BITS_OFFSET;
    private final int D = 8;
    private final byte[][] RC = new byte[][]{{1, 3, 7, 14, 13, 11, 6, 12, 9, 2, 5, 10}, {0, 2, 6, 15, 12, 10, 7, 13, 8, 3, 4, 11}, {2, 0, 4, 13, 14, 8, 5, 15, 10, 1, 6, 9}, {6, 4, 0, 9, 10, 12, 1, 11, 14, 5, 2, 13}, {14, 12, 8, 1, 2, 4, 9, 3, 6, 13, 10, 5}, {15, 13, 9, 0, 3, 5, 8, 2, 7, 12, 11, 4}, {13, 15, 11, 2, 1, 7, 10, 0, 5, 14, 9, 6}, {9, 11, 15, 6, 5, 3, 14, 4, 1, 10, 13, 2}};
    private final byte[][] MixColMatrix = new byte[][]{{2, 4, 2, 11, 2, 8, 5, 6}, {12, 9, 8, 13, 7, 7, 5, 2}, {4, 4, 13, 13, 9, 4, 13, 9}, {1, 6, 5, 1, 12, 13, 15, 14}, {15, 12, 9, 13, 14, 5, 14, 13}, {9, 14, 5, 15, 4, 12, 9, 6}, {12, 2, 2, 10, 3, 1, 1, 14}, {15, 1, 13, 10, 5, 10, 2, 3}};
    private final byte[] sbox = new byte[]{12, 5, 6, 11, 9, 0, 10, 13, 3, 14, 15, 8, 4, 7, 1, 2};

    public PhotonBeetleEngine(PhotonBeetleParameters photonBeetleParameters) {
        this.KEY_SIZE = 16;
        this.IV_SIZE = 16;
        this.MAC_SIZE = 16;
        int n2 = 0;
        int n3 = 0;
        switch (photonBeetleParameters.ordinal()) {
            case 0: {
                n3 = 32;
                n2 = 224;
                break;
            }
            case 1: {
                n3 = 128;
                n2 = 128;
            }
        }
        this.AADBufferSize = this.BlockSize = n3 + 7 >>> 3;
        this.RATE_INBYTES_HALF = this.BlockSize >>> 1;
        int n4 = n3 + n2;
        this.STATE_INBYTES = n4 + 7 >>> 3;
        this.LAST_THREE_BITS_OFFSET = n4 - (this.STATE_INBYTES - 1 << 3) - 3;
        this.initialised = false;
        this.algorithmName = "Photon-Beetle AEAD";
        this.m_aad = new byte[this.AADBufferSize];
    }

    @Override
    protected void init(byte[] byArray, byte[] byArray2) throws IllegalArgumentException {
        this.K = byArray;
        this.N = byArray2;
        this.state = new byte[this.STATE_INBYTES];
        this.state_2d = new byte[8][8];
        this.mac = new byte[this.MAC_SIZE];
        this.initialised = true;
        this.m_buf = new byte[this.BlockSize + (this.forEncryption ? 0 : this.MAC_SIZE)];
        this.m_state = this.forEncryption ? AEADBufferBaseEngine.State.EncInit : AEADBufferBaseEngine.State.DecInit;
        this.reset(false);
    }

    @Override
    protected void processBufferAAD(byte[] byArray, int n2) {
        this.PHOTON_Permutation();
        this.XOR(byArray, n2, this.BlockSize);
    }

    @Override
    protected void processBuffer(byte[] byArray, int n2, byte[] byArray2, int n3) {
        this.PHOTON_Permutation();
        this.rhoohr(byArray2, n3, byArray, n2, this.BlockSize);
    }

    @Override
    public void processAADByte(byte by) {
        ++this.aadLen;
        super.processAADByte(by);
    }

    @Override
    public void processAADBytes(byte[] byArray, int n2, int n3) {
        this.aadLen += n3;
        super.processAADBytes(byArray, n2, n3);
    }

    @Override
    public int processBytes(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws DataLengthException {
        this.messageLen += n3;
        return super.processBytes(byArray, n2, n3, byArray2, n4);
    }

    @Override
    protected void processFinalBlock(byte[] byArray, int n2) {
        int n3 = this.messageLen - (this.forEncryption ? 0 : this.MAC_SIZE);
        int n4 = this.m_bufPos;
        if (this.aadLen != 0 || n3 != 0) {
            this.input_empty = false;
        }
        byte by = this.select(this.aadLen != 0, n3 % this.BlockSize == 0, (byte)5, (byte)6);
        if (n3 != 0) {
            if (n4 != 0) {
                this.PHOTON_Permutation();
                this.rhoohr(byArray, n2, this.m_buf, 0, n4);
                if (n4 < this.BlockSize) {
                    int n5 = n4;
                    this.state[n5] = (byte)(this.state[n5] ^ 1);
                }
            }
            int n6 = this.STATE_INBYTES - 1;
            this.state[n6] = (byte)(this.state[n6] ^ by << this.LAST_THREE_BITS_OFFSET);
        }
        if (this.input_empty) {
            int n7 = this.STATE_INBYTES - 1;
            this.state[n7] = (byte)(this.state[n7] ^ 1 << this.LAST_THREE_BITS_OFFSET);
        }
        this.PHOTON_Permutation();
        this.mac = new byte[this.MAC_SIZE];
        System.arraycopy(this.state, 0, this.mac, 0, this.MAC_SIZE);
    }

    @Override
    protected void processFinalAAD() {
        if (!this.aadFinished) {
            if (this.aadLen != 0) {
                if (this.m_aadPos != 0) {
                    this.PHOTON_Permutation();
                    this.XOR(this.m_aad, 0, this.m_aadPos);
                    if (this.m_aadPos < this.BlockSize) {
                        int n2 = this.m_aadPos;
                        this.state[n2] = (byte)(this.state[n2] ^ 1);
                    }
                }
                int n3 = this.STATE_INBYTES - 1;
                this.state[n3] = (byte)(this.state[n3] ^ this.select(this.messageLen - (this.forEncryption ? 0 : this.MAC_SIZE) > 0, this.aadLen % this.BlockSize == 0, (byte)3, (byte)4) << this.LAST_THREE_BITS_OFFSET);
            }
            this.m_aadPos = 0;
            this.aadFinished = true;
        }
    }

    @Override
    protected void reset(boolean bl) {
        if (!this.initialised) {
            throw new IllegalArgumentException("Need call init function before encryption/decryption");
        }
        this.bufferReset();
        this.input_empty = true;
        this.aadLen = 0;
        this.aadFinished = false;
        this.messageLen = 0;
        System.arraycopy(this.K, 0, this.state, 0, this.K.length);
        System.arraycopy(this.N, 0, this.state, this.K.length, this.N.length);
        super.reset(bl);
    }

    private void PHOTON_Permutation() {
        int n2;
        int n3 = 3;
        int n4 = 7;
        int n5 = 64;
        for (n2 = 0; n2 < n5; ++n2) {
            this.state_2d[n2 >>> n3][n2 & n4] = (byte)((this.state[n2 >> 1] & 0xFF) >>> 4 * (n2 & 1) & 0xF);
        }
        int n6 = 12;
        for (int i2 = 0; i2 < n6; ++i2) {
            int n7;
            for (n2 = 0; n2 < 8; ++n2) {
                byte[] byArray = this.state_2d[n2];
                byArray[0] = (byte)(byArray[0] ^ this.RC[n2][i2]);
            }
            for (n2 = 0; n2 < 8; ++n2) {
                for (n7 = 0; n7 < 8; ++n7) {
                    this.state_2d[n2][n7] = this.sbox[this.state_2d[n2][n7]];
                }
            }
            for (n2 = 1; n2 < 8; ++n2) {
                System.arraycopy(this.state_2d[n2], 0, this.state, 0, 8);
                System.arraycopy(this.state, n2, this.state_2d[n2], 0, 8 - n2);
                System.arraycopy(this.state, 0, this.state_2d[n2], 8 - n2, n2);
            }
            for (n7 = 0; n7 < 8; ++n7) {
                for (n2 = 0; n2 < 8; ++n2) {
                    int n8;
                    int n9;
                    int n10 = 0;
                    for (int i3 = 0; i3 < 8; ++i3) {
                        n9 = this.MixColMatrix[n2][i3];
                        n8 = this.state_2d[i3][n7];
                        n10 ^= n9 * (n8 & 1);
                        n10 ^= n9 * (n8 & 2);
                        n10 ^= n9 * (n8 & 4);
                        n10 ^= n9 * (n8 & 8);
                    }
                    n9 = n10 >>> 4;
                    n10 = n10 & 0xF ^ n9 ^ n9 << 1;
                    n8 = n10 >>> 4;
                    n10 = n10 & 0xF ^ n8 ^ n8 << 1;
                    this.state[n2] = (byte)n10;
                }
                for (n2 = 0; n2 < 8; ++n2) {
                    this.state_2d[n2][n7] = this.state[n2];
                }
            }
        }
        for (n2 = 0; n2 < n5; n2 += 2) {
            this.state[n2 >>> 1] = (byte)(this.state_2d[n2 >>> n3][n2 & n4] & 0xF | (this.state_2d[n2 >>> n3][n2 + 1 & n4] & 0xF) << 4);
        }
    }

    private byte select(boolean bl, boolean bl2, byte by, byte by2) {
        if (bl && bl2) {
            return 1;
        }
        if (bl) {
            return 2;
        }
        if (bl2) {
            return by;
        }
        return by2;
    }

    private void rhoohr(byte[] byArray, int n2, byte[] byArray2, int n3, int n4) {
        int n5;
        byte[] byArray3 = this.state_2d[0];
        int n6 = Math.min(n4, this.RATE_INBYTES_HALF);
        for (n5 = 0; n5 < this.RATE_INBYTES_HALF - 1; ++n5) {
            byArray3[n5] = (byte)((this.state[n5] & 0xFF) >>> 1 | (this.state[n5 + 1] & 1) << 7);
        }
        byArray3[this.RATE_INBYTES_HALF - 1] = (byte)((this.state[n5] & 0xFF) >>> 1 | (this.state[0] & 1) << 7);
        n5 = 0;
        while (n5 < n6) {
            byArray[n5 + n2] = (byte)(this.state[n5 + this.RATE_INBYTES_HALF] ^ byArray2[n5++ + n3]);
        }
        while (n5 < n4) {
            byArray[n5 + n2] = (byte)(byArray3[n5 - this.RATE_INBYTES_HALF] ^ byArray2[n5++ + n3]);
        }
        if (this.forEncryption) {
            this.XOR(byArray2, n3, n4);
        } else {
            this.XOR(byArray, n2, n4);
        }
    }

    private void XOR(byte[] byArray, int n2, int n3) {
        int n4 = 0;
        while (n4 < n3) {
            int n5 = n4++;
            this.state[n5] = (byte)(this.state[n5] ^ byArray[n2++]);
        }
    }

    public static enum PhotonBeetleParameters {
        pb32,
        pb128;

    }
}

