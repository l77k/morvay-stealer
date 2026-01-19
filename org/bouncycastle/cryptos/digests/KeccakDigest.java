/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.CryptoServiceProperties;
import org.bouncycastle.crypto.CryptoServicePurpose;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.digests.Utils;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class KeccakDigest
implements ExtendedDigest {
    private static long[] KeccakRoundConstants = new long[]{1L, 32898L, -9223372036854742902L, -9223372034707259392L, 32907L, 0x80000001L, -9223372034707259263L, -9223372036854743031L, 138L, 136L, 0x80008009L, 0x8000000AL, 0x8000808BL, -9223372036854775669L, -9223372036854742903L, -9223372036854743037L, -9223372036854743038L, -9223372036854775680L, 32778L, -9223372034707292150L, -9223372034707259263L, -9223372036854742912L, 0x80000001L, -9223372034707259384L};
    protected final CryptoServicePurpose purpose;
    protected long[] state = new long[25];
    protected byte[] dataQueue = new byte[192];
    protected int rate;
    protected int bitsInQueue;
    protected int fixedOutputLength;
    protected boolean squeezing;

    public KeccakDigest() {
        this(288, CryptoServicePurpose.ANY);
    }

    public KeccakDigest(CryptoServicePurpose cryptoServicePurpose) {
        this(288, cryptoServicePurpose);
    }

    public KeccakDigest(int n2) {
        this(n2, CryptoServicePurpose.ANY);
    }

    public KeccakDigest(int n2, CryptoServicePurpose cryptoServicePurpose) {
        this.purpose = cryptoServicePurpose;
        this.init(n2);
        CryptoServicesRegistrar.checkConstraints(this.cryptoServiceProperties());
    }

    public KeccakDigest(KeccakDigest keccakDigest) {
        this.purpose = keccakDigest.purpose;
        System.arraycopy(keccakDigest.state, 0, this.state, 0, keccakDigest.state.length);
        System.arraycopy(keccakDigest.dataQueue, 0, this.dataQueue, 0, keccakDigest.dataQueue.length);
        this.rate = keccakDigest.rate;
        this.bitsInQueue = keccakDigest.bitsInQueue;
        this.fixedOutputLength = keccakDigest.fixedOutputLength;
        this.squeezing = keccakDigest.squeezing;
        CryptoServicesRegistrar.checkConstraints(this.cryptoServiceProperties());
    }

    @Override
    public String getAlgorithmName() {
        return "Keccak-" + this.fixedOutputLength;
    }

    @Override
    public int getDigestSize() {
        return this.fixedOutputLength / 8;
    }

    @Override
    public void update(byte by) {
        this.absorb(by);
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) {
        this.absorb(byArray, n2, n3);
    }

    @Override
    public int doFinal(byte[] byArray, int n2) {
        this.squeeze(byArray, n2, this.fixedOutputLength);
        this.reset();
        return this.getDigestSize();
    }

    protected int doFinal(byte[] byArray, int n2, byte by, int n3) {
        if (n3 > 0) {
            this.absorbBits(by, n3);
        }
        this.squeeze(byArray, n2, this.fixedOutputLength);
        this.reset();
        return this.getDigestSize();
    }

    @Override
    public void reset() {
        this.init(this.fixedOutputLength);
    }

    @Override
    public int getByteLength() {
        return this.rate / 8;
    }

    private void init(int n2) {
        switch (n2) {
            case 128: 
            case 224: 
            case 256: 
            case 288: 
            case 384: 
            case 512: {
                this.initSponge(1600 - (n2 << 1));
                break;
            }
            default: {
                throw new IllegalArgumentException("bitLength must be one of 128, 224, 256, 288, 384, or 512.");
            }
        }
    }

    private void initSponge(int n2) {
        if (n2 <= 0 || n2 >= 1600 || n2 % 64 != 0) {
            throw new IllegalStateException("invalid rate value");
        }
        this.rate = n2;
        for (int i2 = 0; i2 < this.state.length; ++i2) {
            this.state[i2] = 0L;
        }
        Arrays.fill(this.dataQueue, (byte)0);
        this.bitsInQueue = 0;
        this.squeezing = false;
        this.fixedOutputLength = (1600 - n2) / 2;
    }

    protected void absorb(byte by) {
        if (this.bitsInQueue % 8 != 0) {
            throw new IllegalStateException("attempt to absorb with odd length queue");
        }
        if (this.squeezing) {
            throw new IllegalStateException("attempt to absorb while squeezing");
        }
        this.dataQueue[this.bitsInQueue >>> 3] = by;
        if ((this.bitsInQueue += 8) == this.rate) {
            this.KeccakAbsorb(this.dataQueue, 0);
            this.bitsInQueue = 0;
        }
    }

    protected void absorb(byte[] byArray, int n2, int n3) {
        int n4;
        if (this.bitsInQueue % 8 != 0) {
            throw new IllegalStateException("attempt to absorb with odd length queue");
        }
        if (this.squeezing) {
            throw new IllegalStateException("attempt to absorb while squeezing");
        }
        int n5 = this.rate >>> 3;
        int n6 = this.bitsInQueue >>> 3;
        int n7 = n5 - n6;
        if (n3 < n7) {
            System.arraycopy(byArray, n2, this.dataQueue, n6, n3);
            this.bitsInQueue += n3 << 3;
            return;
        }
        int n8 = 0;
        if (n6 > 0) {
            System.arraycopy(byArray, n2, this.dataQueue, n6, n7);
            n8 += n7;
            this.KeccakAbsorb(this.dataQueue, 0);
        }
        while ((n4 = n3 - n8) >= n5) {
            this.KeccakAbsorb(byArray, n2 + n8);
            n8 += n5;
        }
        System.arraycopy(byArray, n2 + n8, this.dataQueue, 0, n4);
        this.bitsInQueue = n4 << 3;
    }

    protected void absorbBits(int n2, int n3) {
        if (n3 < 1 || n3 > 7) {
            throw new IllegalArgumentException("'bits' must be in the range 1 to 7");
        }
        if (this.bitsInQueue % 8 != 0) {
            throw new IllegalStateException("attempt to absorb with odd length queue");
        }
        if (this.squeezing) {
            throw new IllegalStateException("attempt to absorb while squeezing");
        }
        int n4 = (1 << n3) - 1;
        this.dataQueue[this.bitsInQueue >>> 3] = (byte)(n2 & n4);
        this.bitsInQueue += n3;
    }

    private void padAndSwitchToSqueezingPhase() {
        int n2 = this.bitsInQueue >>> 3;
        this.dataQueue[n2] = (byte)(this.dataQueue[n2] | (byte)(1 << (this.bitsInQueue & 7)));
        if (++this.bitsInQueue == this.rate) {
            this.KeccakAbsorb(this.dataQueue, 0);
        } else {
            int n3 = this.bitsInQueue >>> 6;
            int n4 = this.bitsInQueue & 0x3F;
            int n5 = 0;
            int n6 = 0;
            while (n6 < n3) {
                int n7 = n6++;
                this.state[n7] = this.state[n7] ^ Pack.littleEndianToLong(this.dataQueue, n5);
                n5 += 8;
            }
            if (n4 > 0) {
                long l2 = (1L << n4) - 1L;
                int n8 = n3;
                this.state[n8] = this.state[n8] ^ Pack.littleEndianToLong(this.dataQueue, n5) & l2;
            }
        }
        int n9 = this.rate - 1 >>> 6;
        this.state[n9] = this.state[n9] ^ Long.MIN_VALUE;
        this.bitsInQueue = 0;
        this.squeezing = true;
    }

    protected void squeeze(byte[] byArray, int n2, long l2) {
        int n3;
        if (!this.squeezing) {
            this.padAndSwitchToSqueezingPhase();
        }
        if (l2 % 8L != 0L) {
            throw new IllegalStateException("outputLength not a multiple of 8");
        }
        for (long i2 = 0L; i2 < l2; i2 += (long)n3) {
            if (this.bitsInQueue == 0) {
                this.KeccakExtract();
            }
            n3 = (int)Math.min((long)this.bitsInQueue, l2 - i2);
            System.arraycopy(this.dataQueue, (this.rate - this.bitsInQueue) / 8, byArray, n2 + (int)(i2 / 8L), n3 / 8);
            this.bitsInQueue -= n3;
        }
    }

    private void KeccakAbsorb(byte[] byArray, int n2) {
        int n3 = this.rate >>> 6;
        int n4 = 0;
        while (n4 < n3) {
            int n5 = n4++;
            this.state[n5] = this.state[n5] ^ Pack.littleEndianToLong(byArray, n2);
            n2 += 8;
        }
        this.KeccakPermutation();
    }

    private void KeccakExtract() {
        this.KeccakPermutation();
        Pack.longToLittleEndian(this.state, 0, this.rate >>> 6, this.dataQueue, 0);
        this.bitsInQueue = this.rate;
    }

    private void KeccakPermutation() {
        long[] lArray = this.state;
        long l2 = lArray[0];
        long l3 = lArray[1];
        long l4 = lArray[2];
        long l5 = lArray[3];
        long l6 = lArray[4];
        long l7 = lArray[5];
        long l8 = lArray[6];
        long l9 = lArray[7];
        long l10 = lArray[8];
        long l11 = lArray[9];
        long l12 = lArray[10];
        long l13 = lArray[11];
        long l14 = lArray[12];
        long l15 = lArray[13];
        long l16 = lArray[14];
        long l17 = lArray[15];
        long l18 = lArray[16];
        long l19 = lArray[17];
        long l20 = lArray[18];
        long l21 = lArray[19];
        long l22 = lArray[20];
        long l23 = lArray[21];
        long l24 = lArray[22];
        long l25 = lArray[23];
        long l26 = lArray[24];
        for (int i2 = 0; i2 < 24; ++i2) {
            long l27 = l2 ^ l7 ^ l12 ^ l17 ^ l22;
            long l28 = l3 ^ l8 ^ l13 ^ l18 ^ l23;
            long l29 = l4 ^ l9 ^ l14 ^ l19 ^ l24;
            long l30 = l5 ^ l10 ^ l15 ^ l20 ^ l25;
            long l31 = l6 ^ l11 ^ l16 ^ l21 ^ l26;
            long l32 = (l28 << 1 | l28 >>> -1) ^ l31;
            long l33 = (l29 << 1 | l29 >>> -1) ^ l27;
            long l34 = (l30 << 1 | l30 >>> -1) ^ l28;
            long l35 = (l31 << 1 | l31 >>> -1) ^ l29;
            long l36 = (l27 << 1 | l27 >>> -1) ^ l30;
            l2 ^= l32;
            l7 ^= l32;
            l12 ^= l32;
            l17 ^= l32;
            l22 ^= l32;
            l3 ^= l33;
            l8 ^= l33;
            l13 ^= l33;
            l18 ^= l33;
            l23 ^= l33;
            l4 ^= l34;
            l9 ^= l34;
            l14 ^= l34;
            l19 ^= l34;
            l24 ^= l34;
            l5 ^= l35;
            l10 ^= l35;
            l15 ^= l35;
            l20 ^= l35;
            l25 ^= l35;
            l6 ^= l36;
            l11 ^= l36;
            l16 ^= l36;
            l21 ^= l36;
            l26 ^= l36;
            l28 = l3 << 1 | l3 >>> 63;
            l3 = l8 << 44 | l8 >>> 20;
            l8 = l11 << 20 | l11 >>> 44;
            l11 = l24 << 61 | l24 >>> 3;
            l24 = l16 << 39 | l16 >>> 25;
            l16 = l22 << 18 | l22 >>> 46;
            l22 = l4 << 62 | l4 >>> 2;
            l4 = l14 << 43 | l14 >>> 21;
            l14 = l15 << 25 | l15 >>> 39;
            l15 = l21 << 8 | l21 >>> 56;
            l21 = l25 << 56 | l25 >>> 8;
            l25 = l17 << 41 | l17 >>> 23;
            l17 = l6 << 27 | l6 >>> 37;
            l6 = l26 << 14 | l26 >>> 50;
            l26 = l23 << 2 | l23 >>> 62;
            l23 = l10 << 55 | l10 >>> 9;
            l10 = l18 << 45 | l18 >>> 19;
            l18 = l7 << 36 | l7 >>> 28;
            l7 = l5 << 28 | l5 >>> 36;
            l5 = l20 << 21 | l20 >>> 43;
            l20 = l19 << 15 | l19 >>> 49;
            l19 = l13 << 10 | l13 >>> 54;
            l13 = l9 << 6 | l9 >>> 58;
            l9 = l12 << 3 | l12 >>> 61;
            l12 = l28;
            l27 = l2 ^ (l3 ^ 0xFFFFFFFFFFFFFFFFL) & l4;
            l28 = l3 ^ (l4 ^ 0xFFFFFFFFFFFFFFFFL) & l5;
            l4 ^= (l5 ^ 0xFFFFFFFFFFFFFFFFL) & l6;
            l5 ^= (l6 ^ 0xFFFFFFFFFFFFFFFFL) & l2;
            l6 ^= (l2 ^ 0xFFFFFFFFFFFFFFFFL) & l3;
            l2 = l27;
            l3 = l28;
            l27 = l7 ^ (l8 ^ 0xFFFFFFFFFFFFFFFFL) & l9;
            l28 = l8 ^ (l9 ^ 0xFFFFFFFFFFFFFFFFL) & l10;
            l9 ^= (l10 ^ 0xFFFFFFFFFFFFFFFFL) & l11;
            l10 ^= (l11 ^ 0xFFFFFFFFFFFFFFFFL) & l7;
            l11 ^= (l7 ^ 0xFFFFFFFFFFFFFFFFL) & l8;
            l7 = l27;
            l8 = l28;
            l27 = l12 ^ (l13 ^ 0xFFFFFFFFFFFFFFFFL) & l14;
            l28 = l13 ^ (l14 ^ 0xFFFFFFFFFFFFFFFFL) & l15;
            l14 ^= (l15 ^ 0xFFFFFFFFFFFFFFFFL) & l16;
            l15 ^= (l16 ^ 0xFFFFFFFFFFFFFFFFL) & l12;
            l16 ^= (l12 ^ 0xFFFFFFFFFFFFFFFFL) & l13;
            l12 = l27;
            l13 = l28;
            l27 = l17 ^ (l18 ^ 0xFFFFFFFFFFFFFFFFL) & l19;
            l28 = l18 ^ (l19 ^ 0xFFFFFFFFFFFFFFFFL) & l20;
            l19 ^= (l20 ^ 0xFFFFFFFFFFFFFFFFL) & l21;
            l20 ^= (l21 ^ 0xFFFFFFFFFFFFFFFFL) & l17;
            l21 ^= (l17 ^ 0xFFFFFFFFFFFFFFFFL) & l18;
            l17 = l27;
            l18 = l28;
            l27 = l22 ^ (l23 ^ 0xFFFFFFFFFFFFFFFFL) & l24;
            l28 = l23 ^ (l24 ^ 0xFFFFFFFFFFFFFFFFL) & l25;
            l24 ^= (l25 ^ 0xFFFFFFFFFFFFFFFFL) & l26;
            l25 ^= (l26 ^ 0xFFFFFFFFFFFFFFFFL) & l22;
            l26 ^= (l22 ^ 0xFFFFFFFFFFFFFFFFL) & l23;
            l22 = l27;
            l23 = l28;
            l2 ^= KeccakRoundConstants[i2];
        }
        lArray[0] = l2;
        lArray[1] = l3;
        lArray[2] = l4;
        lArray[3] = l5;
        lArray[4] = l6;
        lArray[5] = l7;
        lArray[6] = l8;
        lArray[7] = l9;
        lArray[8] = l10;
        lArray[9] = l11;
        lArray[10] = l12;
        lArray[11] = l13;
        lArray[12] = l14;
        lArray[13] = l15;
        lArray[14] = l16;
        lArray[15] = l17;
        lArray[16] = l18;
        lArray[17] = l19;
        lArray[18] = l20;
        lArray[19] = l21;
        lArray[20] = l22;
        lArray[21] = l23;
        lArray[22] = l24;
        lArray[23] = l25;
        lArray[24] = l26;
    }

    protected CryptoServiceProperties cryptoServiceProperties() {
        return Utils.getDefaultProperties(this, this.getDigestSize() * 8, this.purpose);
    }
}

