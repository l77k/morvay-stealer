/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.digests.SparkleDigest;
import org.bouncycastle.crypto.engines.AEADBufferBaseEngine;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Pack;

public class SparkleEngine
extends AEADBufferBaseEngine {
    private static final int[] RCON = new int[]{-1209970334, -1083090816, 951376470, 844003128, -1156479509, 1333558103, -809524792, -1028445891};
    private final int[] state;
    private final int[] k;
    private final int[] npub;
    private boolean encrypted;
    private final int m_bufferSizeDecrypt;
    private final int SPARKLE_STEPS_SLIM;
    private final int SPARKLE_STEPS_BIG;
    private final int KEY_WORDS;
    private final int TAG_WORDS;
    private final int STATE_WORDS;
    private final int RATE_WORDS;
    private final int CAP_MASK;
    private final int _A0;
    private final int _A1;
    private final int _M2;
    private final int _M3;

    public SparkleEngine(SparkleParameters sparkleParameters) {
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        switch (sparkleParameters.ordinal()) {
            case 0: {
                n6 = 128;
                n5 = 128;
                n4 = 128;
                n3 = 256;
                n2 = 128;
                this.SPARKLE_STEPS_SLIM = 7;
                this.SPARKLE_STEPS_BIG = 10;
                this.algorithmName = "SCHWAEMM128-128";
                break;
            }
            case 1: {
                n6 = 128;
                n5 = 256;
                n4 = 128;
                n3 = 384;
                n2 = 128;
                this.SPARKLE_STEPS_SLIM = 7;
                this.SPARKLE_STEPS_BIG = 11;
                this.algorithmName = "SCHWAEMM256-128";
                break;
            }
            case 2: {
                n6 = 192;
                n5 = 192;
                n4 = 192;
                n3 = 384;
                n2 = 192;
                this.SPARKLE_STEPS_SLIM = 7;
                this.SPARKLE_STEPS_BIG = 11;
                this.algorithmName = "SCHWAEMM192-192";
                break;
            }
            case 3: {
                n6 = 256;
                n5 = 256;
                n4 = 256;
                n3 = 512;
                n2 = 256;
                this.SPARKLE_STEPS_SLIM = 8;
                this.SPARKLE_STEPS_BIG = 12;
                this.algorithmName = "SCHWAEMM256-256";
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid definition of SCHWAEMM instance");
            }
        }
        this.KEY_WORDS = n6 >>> 5;
        this.KEY_SIZE = n6 >>> 3;
        this.TAG_WORDS = n4 >>> 5;
        this.MAC_SIZE = n4 >>> 3;
        this.STATE_WORDS = n3 >>> 5;
        this.RATE_WORDS = n5 >>> 5;
        this.IV_SIZE = n5 >>> 3;
        int n7 = n2 >>> 6;
        int n8 = n2 >>> 5;
        this.CAP_MASK = this.RATE_WORDS > n8 ? n8 - 1 : -1;
        this._A0 = 1 << n7 << 24;
        this._A1 = (1 ^ 1 << n7) << 24;
        this._M2 = (2 ^ 1 << n7) << 24;
        this._M3 = (3 ^ 1 << n7) << 24;
        this.state = new int[this.STATE_WORDS];
        this.k = new int[this.KEY_WORDS];
        this.npub = new int[this.RATE_WORDS];
        this.AADBufferSize = this.BlockSize = this.IV_SIZE;
        this.m_bufferSizeDecrypt = this.IV_SIZE + this.MAC_SIZE;
        this.m_buf = new byte[this.m_bufferSizeDecrypt];
        this.m_aad = new byte[this.BlockSize];
    }

    @Override
    protected void init(byte[] byArray, byte[] byArray2) throws IllegalArgumentException {
        Pack.littleEndianToInt(byArray, 0, this.k);
        Pack.littleEndianToInt(byArray2, 0, this.npub);
        this.initialised = true;
        this.m_state = this.forEncryption ? AEADBufferBaseEngine.State.EncInit : AEADBufferBaseEngine.State.DecInit;
        this.reset();
    }

    @Override
    protected void processFinalBlock(byte[] byArray, int n2) {
        if (this.encrypted || this.m_bufPos > 0) {
            int n3;
            int n4 = this.STATE_WORDS - 1;
            this.state[n4] = this.state[n4] ^ (this.m_bufPos < this.IV_SIZE ? this._M2 : this._M3);
            int[] nArray = new int[this.RATE_WORDS];
            for (n3 = 0; n3 < this.m_bufPos; ++n3) {
                int n5 = n3 >>> 2;
                nArray[n5] = nArray[n5] | (this.m_buf[n3] & 0xFF) << ((n3 & 3) << 3);
            }
            if (this.m_bufPos < this.IV_SIZE) {
                if (!this.forEncryption) {
                    n3 = (this.m_bufPos & 3) << 3;
                    int n6 = this.m_bufPos >>> 2;
                    nArray[n6] = nArray[n6] | this.state[this.m_bufPos >>> 2] >>> n3 << n3;
                    n3 = (this.m_bufPos >>> 2) + 1;
                    System.arraycopy(this.state, n3, nArray, n3, this.RATE_WORDS - n3);
                }
                int n7 = this.m_bufPos >>> 2;
                nArray[n7] = nArray[n7] ^ 128 << ((this.m_bufPos & 3) << 3);
            }
            n3 = 0;
            while (n3 < this.RATE_WORDS / 2) {
                int n8 = n3 + this.RATE_WORDS / 2;
                int n9 = this.state[n3];
                int n10 = this.state[n8];
                if (this.forEncryption) {
                    this.state[n3] = n10 ^ nArray[n3] ^ this.state[this.RATE_WORDS + n3];
                    this.state[n8] = n9 ^ n10 ^ nArray[n8] ^ this.state[this.RATE_WORDS + (n8 & this.CAP_MASK)];
                } else {
                    this.state[n3] = n9 ^ n10 ^ nArray[n3] ^ this.state[this.RATE_WORDS + n3];
                    this.state[n8] = n9 ^ nArray[n8] ^ this.state[this.RATE_WORDS + (n8 & this.CAP_MASK)];
                }
                int n11 = n3++;
                nArray[n11] = nArray[n11] ^ n9;
                int n12 = n8;
                nArray[n12] = nArray[n12] ^ n10;
            }
            for (n3 = 0; n3 < this.m_bufPos; ++n3) {
                byArray[n2++] = (byte)(nArray[n3 >>> 2] >>> ((n3 & 3) << 3));
            }
            SparkleEngine.sparkle_opt(this.state, this.SPARKLE_STEPS_BIG);
        }
        for (int i2 = 0; i2 < this.KEY_WORDS; ++i2) {
            int n13 = this.RATE_WORDS + i2;
            this.state[n13] = this.state[n13] ^ this.k[i2];
        }
        this.mac = new byte[this.MAC_SIZE];
        Pack.intToLittleEndian(this.state, this.RATE_WORDS, this.TAG_WORDS, this.mac, 0);
    }

    @Override
    protected void processBufferAAD(byte[] byArray, int n2) {
        for (int i2 = 0; i2 < this.RATE_WORDS / 2; ++i2) {
            int n3 = i2 + this.RATE_WORDS / 2;
            int n4 = this.state[i2];
            int n5 = this.state[n3];
            int n6 = Pack.littleEndianToInt(byArray, n2 + i2 * 4);
            int n7 = Pack.littleEndianToInt(byArray, n2 + n3 * 4);
            this.state[i2] = n5 ^ n6 ^ this.state[this.RATE_WORDS + i2];
            this.state[n3] = n4 ^ n5 ^ n7 ^ this.state[this.RATE_WORDS + (n3 & this.CAP_MASK)];
        }
        SparkleEngine.sparkle_opt(this.state, this.SPARKLE_STEPS_SLIM);
    }

    private void processBufferDecrypt(byte[] byArray, int n2, byte[] byArray2, int n3) {
        for (int i2 = 0; i2 < this.RATE_WORDS / 2; ++i2) {
            int n4 = i2 + this.RATE_WORDS / 2;
            int n5 = this.state[i2];
            int n6 = this.state[n4];
            int n7 = Pack.littleEndianToInt(byArray, n2 + i2 * 4);
            int n8 = Pack.littleEndianToInt(byArray, n2 + n4 * 4);
            this.state[i2] = n5 ^ n6 ^ n7 ^ this.state[this.RATE_WORDS + i2];
            this.state[n4] = n5 ^ n8 ^ this.state[this.RATE_WORDS + (n4 & this.CAP_MASK)];
            Pack.intToLittleEndian(n7 ^ n5, byArray2, n3 + i2 * 4);
            Pack.intToLittleEndian(n8 ^ n6, byArray2, n3 + n4 * 4);
        }
        SparkleEngine.sparkle_opt(this.state, this.SPARKLE_STEPS_SLIM);
        this.encrypted = true;
    }

    @Override
    protected void processBuffer(byte[] byArray, int n2, byte[] byArray2, int n3) {
        if (this.forEncryption) {
            this.processBufferEncrypt(byArray, n2, byArray2, n3);
        } else {
            this.processBufferDecrypt(byArray, n2, byArray2, n3);
        }
    }

    private void processBufferEncrypt(byte[] byArray, int n2, byte[] byArray2, int n3) {
        for (int i2 = 0; i2 < this.RATE_WORDS / 2; ++i2) {
            int n4 = i2 + this.RATE_WORDS / 2;
            int n5 = this.state[i2];
            int n6 = this.state[n4];
            int n7 = Pack.littleEndianToInt(byArray, n2 + i2 * 4);
            int n8 = Pack.littleEndianToInt(byArray, n2 + n4 * 4);
            this.state[i2] = n6 ^ n7 ^ this.state[this.RATE_WORDS + i2];
            this.state[n4] = n5 ^ n6 ^ n8 ^ this.state[this.RATE_WORDS + (n4 & this.CAP_MASK)];
            Pack.intToLittleEndian(n7 ^ n5, byArray2, n3 + i2 * 4);
            Pack.intToLittleEndian(n8 ^ n6, byArray2, n3 + n4 * 4);
        }
        SparkleEngine.sparkle_opt(this.state, this.SPARKLE_STEPS_SLIM);
        this.encrypted = true;
    }

    @Override
    protected void processFinalAAD() {
        if (this.m_aadPos < this.BlockSize) {
            int n2 = this.STATE_WORDS - 1;
            this.state[n2] = this.state[n2] ^ this._A0;
            this.m_aad[this.m_aadPos] = -128;
            while (++this.m_aadPos < this.BlockSize) {
                this.m_aad[this.m_aadPos] = 0;
            }
        } else {
            int n3 = this.STATE_WORDS - 1;
            this.state[n3] = this.state[n3] ^ this._A1;
        }
        for (int i2 = 0; i2 < this.RATE_WORDS / 2; ++i2) {
            int n4 = i2 + this.RATE_WORDS / 2;
            int n5 = this.state[i2];
            int n6 = this.state[n4];
            int n7 = Pack.littleEndianToInt(this.m_aad, i2 * 4);
            int n8 = Pack.littleEndianToInt(this.m_aad, n4 * 4);
            this.state[i2] = n6 ^ n7 ^ this.state[this.RATE_WORDS + i2];
            this.state[n4] = n5 ^ n6 ^ n8 ^ this.state[this.RATE_WORDS + (n4 & this.CAP_MASK)];
        }
        SparkleEngine.sparkle_opt(this.state, this.SPARKLE_STEPS_BIG);
    }

    @Override
    protected void reset(boolean bl) {
        this.bufferReset();
        this.encrypted = false;
        System.arraycopy(this.npub, 0, this.state, 0, this.RATE_WORDS);
        System.arraycopy(this.k, 0, this.state, this.RATE_WORDS, this.KEY_WORDS);
        SparkleEngine.sparkle_opt(this.state, this.SPARKLE_STEPS_BIG);
        super.reset(bl);
    }

    private static int ELL(int n2) {
        return Integers.rotateRight(n2, 16) ^ n2 & 0xFFFF;
    }

    private static void sparkle_opt(int[] nArray, int n2) {
        switch (nArray.length) {
            case 8: {
                SparkleEngine.sparkle_opt8(nArray, n2);
                break;
            }
            case 12: {
                SparkleEngine.sparkle_opt12(nArray, n2);
                break;
            }
            case 16: {
                SparkleEngine.sparkle_opt16(nArray, n2);
                break;
            }
            default: {
                throw new IllegalStateException();
            }
        }
    }

    static void sparkle_opt8(int[] nArray, int n2) {
        int n3 = nArray[0];
        int n4 = nArray[1];
        int n5 = nArray[2];
        int n6 = nArray[3];
        int n7 = nArray[4];
        int n8 = nArray[5];
        int n9 = nArray[6];
        int n10 = nArray[7];
        for (int i2 = 0; i2 < n2; ++i2) {
            n6 ^= i2;
            int n11 = RCON[0];
            n3 += Integers.rotateRight(n4 ^= RCON[i2 & 7], 31);
            n4 ^= Integers.rotateRight(n3, 24);
            n3 ^= n11;
            n3 += Integers.rotateRight(n4, 17);
            n4 ^= Integers.rotateRight(n3, 17);
            n3 ^= n11;
            n3 += n4;
            n4 ^= Integers.rotateRight(n3, 31);
            n3 ^= n11;
            n3 += Integers.rotateRight(n4, 24);
            n4 ^= Integers.rotateRight(n3, 16);
            n3 ^= n11;
            n11 = RCON[1];
            n5 += Integers.rotateRight(n6, 31);
            n6 ^= Integers.rotateRight(n5, 24);
            n5 ^= n11;
            n5 += Integers.rotateRight(n6, 17);
            n6 ^= Integers.rotateRight(n5, 17);
            n5 ^= n11;
            n5 += n6;
            n6 ^= Integers.rotateRight(n5, 31);
            n5 ^= n11;
            n5 += Integers.rotateRight(n6, 24);
            n6 ^= Integers.rotateRight(n5, 16);
            n5 ^= n11;
            n11 = RCON[2];
            n7 += Integers.rotateRight(n8, 31);
            n8 ^= Integers.rotateRight(n7, 24);
            n7 ^= n11;
            n7 += Integers.rotateRight(n8, 17);
            n8 ^= Integers.rotateRight(n7, 17);
            n7 ^= n11;
            n7 += n8;
            n8 ^= Integers.rotateRight(n7, 31);
            n7 ^= n11;
            n7 += Integers.rotateRight(n8, 24);
            n8 ^= Integers.rotateRight(n7, 16);
            n7 ^= n11;
            n11 = RCON[3];
            n9 += Integers.rotateRight(n10, 31);
            n10 ^= Integers.rotateRight(n9, 24);
            n9 ^= n11;
            n9 += Integers.rotateRight(n10, 17);
            n10 ^= Integers.rotateRight(n9, 17);
            n9 ^= n11;
            n9 += n10;
            n10 ^= Integers.rotateRight(n9, 31);
            n9 ^= n11;
            n9 += Integers.rotateRight(n10, 24);
            n10 ^= Integers.rotateRight(n9, 16);
            n9 ^= n11;
            n11 = SparkleEngine.ELL(n3 ^ n5);
            int n12 = SparkleEngine.ELL(n4 ^ n6);
            int n13 = n3 ^ n7;
            int n14 = n4 ^ n8;
            int n15 = n5 ^ n9;
            int n16 = n6 ^ n10;
            n7 = n3;
            n8 = n4;
            n9 = n5;
            n10 = n6;
            n3 = n15 ^ n12;
            n4 = n16 ^ n11;
            n5 = n13 ^ n12;
            n6 = n14 ^ n11;
        }
        nArray[0] = n3;
        nArray[1] = n4;
        nArray[2] = n5;
        nArray[3] = n6;
        nArray[4] = n7;
        nArray[5] = n8;
        nArray[6] = n9;
        nArray[7] = n10;
    }

    static void sparkle_opt12(int[] nArray, int n2) {
        int n3 = nArray[0];
        int n4 = nArray[1];
        int n5 = nArray[2];
        int n6 = nArray[3];
        int n7 = nArray[4];
        int n8 = nArray[5];
        int n9 = nArray[6];
        int n10 = nArray[7];
        int n11 = nArray[8];
        int n12 = nArray[9];
        int n13 = nArray[10];
        int n14 = nArray[11];
        for (int i2 = 0; i2 < n2; ++i2) {
            n6 ^= i2;
            int n15 = RCON[0];
            n3 += Integers.rotateRight(n4 ^= RCON[i2 & 7], 31);
            n4 ^= Integers.rotateRight(n3, 24);
            n3 ^= n15;
            n3 += Integers.rotateRight(n4, 17);
            n4 ^= Integers.rotateRight(n3, 17);
            n3 ^= n15;
            n3 += n4;
            n4 ^= Integers.rotateRight(n3, 31);
            n3 ^= n15;
            n3 += Integers.rotateRight(n4, 24);
            n4 ^= Integers.rotateRight(n3, 16);
            n3 ^= n15;
            n15 = RCON[1];
            n5 += Integers.rotateRight(n6, 31);
            n6 ^= Integers.rotateRight(n5, 24);
            n5 ^= n15;
            n5 += Integers.rotateRight(n6, 17);
            n6 ^= Integers.rotateRight(n5, 17);
            n5 ^= n15;
            n5 += n6;
            n6 ^= Integers.rotateRight(n5, 31);
            n5 ^= n15;
            n5 += Integers.rotateRight(n6, 24);
            n6 ^= Integers.rotateRight(n5, 16);
            n5 ^= n15;
            n15 = RCON[2];
            n7 += Integers.rotateRight(n8, 31);
            n8 ^= Integers.rotateRight(n7, 24);
            n7 ^= n15;
            n7 += Integers.rotateRight(n8, 17);
            n8 ^= Integers.rotateRight(n7, 17);
            n7 ^= n15;
            n7 += n8;
            n8 ^= Integers.rotateRight(n7, 31);
            n7 ^= n15;
            n7 += Integers.rotateRight(n8, 24);
            n8 ^= Integers.rotateRight(n7, 16);
            n7 ^= n15;
            n15 = RCON[3];
            n9 += Integers.rotateRight(n10, 31);
            n10 ^= Integers.rotateRight(n9, 24);
            n9 ^= n15;
            n9 += Integers.rotateRight(n10, 17);
            n10 ^= Integers.rotateRight(n9, 17);
            n9 ^= n15;
            n9 += n10;
            n10 ^= Integers.rotateRight(n9, 31);
            n9 ^= n15;
            n9 += Integers.rotateRight(n10, 24);
            n10 ^= Integers.rotateRight(n9, 16);
            n9 ^= n15;
            n15 = RCON[4];
            n11 += Integers.rotateRight(n12, 31);
            n12 ^= Integers.rotateRight(n11, 24);
            n11 ^= n15;
            n11 += Integers.rotateRight(n12, 17);
            n12 ^= Integers.rotateRight(n11, 17);
            n11 ^= n15;
            n11 += n12;
            n12 ^= Integers.rotateRight(n11, 31);
            n11 ^= n15;
            n11 += Integers.rotateRight(n12, 24);
            n12 ^= Integers.rotateRight(n11, 16);
            n11 ^= n15;
            n15 = RCON[5];
            n13 += Integers.rotateRight(n14, 31);
            n14 ^= Integers.rotateRight(n13, 24);
            n13 ^= n15;
            n13 += Integers.rotateRight(n14, 17);
            n14 ^= Integers.rotateRight(n13, 17);
            n13 ^= n15;
            n13 += n14;
            n14 ^= Integers.rotateRight(n13, 31);
            n13 ^= n15;
            n13 += Integers.rotateRight(n14, 24);
            n14 ^= Integers.rotateRight(n13, 16);
            n13 ^= n15;
            n15 = SparkleEngine.ELL(n3 ^ n5 ^ n7);
            int n16 = SparkleEngine.ELL(n4 ^ n6 ^ n8);
            int n17 = n3 ^ n9;
            int n18 = n4 ^ n10;
            int n19 = n5 ^ n11;
            int n20 = n6 ^ n12;
            int n21 = n7 ^ n13;
            int n22 = n8 ^ n14;
            n9 = n3;
            n10 = n4;
            n11 = n5;
            n12 = n6;
            n13 = n7;
            n14 = n8;
            n3 = n19 ^ n16;
            n4 = n20 ^ n15;
            n5 = n21 ^ n16;
            n6 = n22 ^ n15;
            n7 = n17 ^ n16;
            n8 = n18 ^ n15;
        }
        nArray[0] = n3;
        nArray[1] = n4;
        nArray[2] = n5;
        nArray[3] = n6;
        nArray[4] = n7;
        nArray[5] = n8;
        nArray[6] = n9;
        nArray[7] = n10;
        nArray[8] = n11;
        nArray[9] = n12;
        nArray[10] = n13;
        nArray[11] = n14;
    }

    public static void sparkle_opt12(SparkleDigest.Friend friend, int[] nArray, int n2) {
        if (null == friend) {
            throw new NullPointerException("This method is only for use by SparkleDigest");
        }
        SparkleEngine.sparkle_opt12(nArray, n2);
    }

    static void sparkle_opt16(int[] nArray, int n2) {
        int n3 = nArray[0];
        int n4 = nArray[1];
        int n5 = nArray[2];
        int n6 = nArray[3];
        int n7 = nArray[4];
        int n8 = nArray[5];
        int n9 = nArray[6];
        int n10 = nArray[7];
        int n11 = nArray[8];
        int n12 = nArray[9];
        int n13 = nArray[10];
        int n14 = nArray[11];
        int n15 = nArray[12];
        int n16 = nArray[13];
        int n17 = nArray[14];
        int n18 = nArray[15];
        for (int i2 = 0; i2 < n2; ++i2) {
            n6 ^= i2;
            int n19 = RCON[0];
            n3 += Integers.rotateRight(n4 ^= RCON[i2 & 7], 31);
            n4 ^= Integers.rotateRight(n3, 24);
            n3 ^= n19;
            n3 += Integers.rotateRight(n4, 17);
            n4 ^= Integers.rotateRight(n3, 17);
            n3 ^= n19;
            n3 += n4;
            n4 ^= Integers.rotateRight(n3, 31);
            n3 ^= n19;
            n3 += Integers.rotateRight(n4, 24);
            n4 ^= Integers.rotateRight(n3, 16);
            n3 ^= n19;
            n19 = RCON[1];
            n5 += Integers.rotateRight(n6, 31);
            n6 ^= Integers.rotateRight(n5, 24);
            n5 ^= n19;
            n5 += Integers.rotateRight(n6, 17);
            n6 ^= Integers.rotateRight(n5, 17);
            n5 ^= n19;
            n5 += n6;
            n6 ^= Integers.rotateRight(n5, 31);
            n5 ^= n19;
            n5 += Integers.rotateRight(n6, 24);
            n6 ^= Integers.rotateRight(n5, 16);
            n5 ^= n19;
            n19 = RCON[2];
            n7 += Integers.rotateRight(n8, 31);
            n8 ^= Integers.rotateRight(n7, 24);
            n7 ^= n19;
            n7 += Integers.rotateRight(n8, 17);
            n8 ^= Integers.rotateRight(n7, 17);
            n7 ^= n19;
            n7 += n8;
            n8 ^= Integers.rotateRight(n7, 31);
            n7 ^= n19;
            n7 += Integers.rotateRight(n8, 24);
            n8 ^= Integers.rotateRight(n7, 16);
            n7 ^= n19;
            n19 = RCON[3];
            n9 += Integers.rotateRight(n10, 31);
            n10 ^= Integers.rotateRight(n9, 24);
            n9 ^= n19;
            n9 += Integers.rotateRight(n10, 17);
            n10 ^= Integers.rotateRight(n9, 17);
            n9 ^= n19;
            n9 += n10;
            n10 ^= Integers.rotateRight(n9, 31);
            n9 ^= n19;
            n9 += Integers.rotateRight(n10, 24);
            n10 ^= Integers.rotateRight(n9, 16);
            n9 ^= n19;
            n19 = RCON[4];
            n11 += Integers.rotateRight(n12, 31);
            n12 ^= Integers.rotateRight(n11, 24);
            n11 ^= n19;
            n11 += Integers.rotateRight(n12, 17);
            n12 ^= Integers.rotateRight(n11, 17);
            n11 ^= n19;
            n11 += n12;
            n12 ^= Integers.rotateRight(n11, 31);
            n11 ^= n19;
            n11 += Integers.rotateRight(n12, 24);
            n12 ^= Integers.rotateRight(n11, 16);
            n11 ^= n19;
            n19 = RCON[5];
            n13 += Integers.rotateRight(n14, 31);
            n14 ^= Integers.rotateRight(n13, 24);
            n13 ^= n19;
            n13 += Integers.rotateRight(n14, 17);
            n14 ^= Integers.rotateRight(n13, 17);
            n13 ^= n19;
            n13 += n14;
            n14 ^= Integers.rotateRight(n13, 31);
            n13 ^= n19;
            n13 += Integers.rotateRight(n14, 24);
            n14 ^= Integers.rotateRight(n13, 16);
            n13 ^= n19;
            n19 = RCON[6];
            n15 += Integers.rotateRight(n16, 31);
            n16 ^= Integers.rotateRight(n15, 24);
            n15 ^= n19;
            n15 += Integers.rotateRight(n16, 17);
            n16 ^= Integers.rotateRight(n15, 17);
            n15 ^= n19;
            n15 += n16;
            n16 ^= Integers.rotateRight(n15, 31);
            n15 ^= n19;
            n15 += Integers.rotateRight(n16, 24);
            n16 ^= Integers.rotateRight(n15, 16);
            n15 ^= n19;
            n19 = RCON[7];
            n17 += Integers.rotateRight(n18, 31);
            n18 ^= Integers.rotateRight(n17, 24);
            n17 ^= n19;
            n17 += Integers.rotateRight(n18, 17);
            n18 ^= Integers.rotateRight(n17, 17);
            n17 ^= n19;
            n17 += n18;
            n18 ^= Integers.rotateRight(n17, 31);
            n17 ^= n19;
            n17 += Integers.rotateRight(n18, 24);
            n18 ^= Integers.rotateRight(n17, 16);
            n17 ^= n19;
            n19 = SparkleEngine.ELL(n3 ^ n5 ^ n7 ^ n9);
            int n20 = SparkleEngine.ELL(n4 ^ n6 ^ n8 ^ n10);
            int n21 = n3 ^ n11;
            int n22 = n4 ^ n12;
            int n23 = n5 ^ n13;
            int n24 = n6 ^ n14;
            int n25 = n7 ^ n15;
            int n26 = n8 ^ n16;
            int n27 = n9 ^ n17;
            int n28 = n10 ^ n18;
            n11 = n3;
            n12 = n4;
            n13 = n5;
            n14 = n6;
            n15 = n7;
            n16 = n8;
            n17 = n9;
            n18 = n10;
            n3 = n23 ^ n20;
            n4 = n24 ^ n19;
            n5 = n25 ^ n20;
            n6 = n26 ^ n19;
            n7 = n27 ^ n20;
            n8 = n28 ^ n19;
            n9 = n21 ^ n20;
            n10 = n22 ^ n19;
        }
        nArray[0] = n3;
        nArray[1] = n4;
        nArray[2] = n5;
        nArray[3] = n6;
        nArray[4] = n7;
        nArray[5] = n8;
        nArray[6] = n9;
        nArray[7] = n10;
        nArray[8] = n11;
        nArray[9] = n12;
        nArray[10] = n13;
        nArray[11] = n14;
        nArray[12] = n15;
        nArray[13] = n16;
        nArray[14] = n17;
        nArray[15] = n18;
    }

    public static void sparkle_opt16(SparkleDigest.Friend friend, int[] nArray, int n2) {
        if (null == friend) {
            throw new NullPointerException("This method is only for use by SparkleDigest");
        }
        SparkleEngine.sparkle_opt16(nArray, n2);
    }

    public static enum SparkleParameters {
        SCHWAEMM128_128,
        SCHWAEMM256_128,
        SCHWAEMM192_192,
        SCHWAEMM256_256;

    }
}

