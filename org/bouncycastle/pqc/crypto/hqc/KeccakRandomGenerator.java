/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.hqc;

import org.bouncycastle.util.Arrays;

class KeccakRandomGenerator {
    private static long[] KeccakRoundConstants = new long[]{1L, 32898L, -9223372036854742902L, -9223372034707259392L, 32907L, 0x80000001L, -9223372034707259263L, -9223372036854743031L, 138L, 136L, 0x80008009L, 0x8000000AL, 0x8000808BL, -9223372036854775669L, -9223372036854742903L, -9223372036854743037L, -9223372036854743038L, -9223372036854775680L, 32778L, -9223372034707292150L, -9223372034707259263L, -9223372036854742912L, 0x80000001L, -9223372034707259384L};
    protected long[] state = new long[26];
    protected byte[] dataQueue = new byte[192];
    protected int rate;
    protected int bitsInQueue;
    protected int fixedOutputLength;

    public KeccakRandomGenerator() {
        this(288);
    }

    public KeccakRandomGenerator(int n2) {
        this.init(n2);
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
        Arrays.fill(this.state, 0L);
        Arrays.fill(this.dataQueue, (byte)0);
        this.bitsInQueue = 0;
        this.fixedOutputLength = (1600 - n2) / 2;
    }

    private static void keccakPermutation(long[] lArray) {
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

    private void keccakIncAbsorb(byte[] byArray, int n2) {
        int n3;
        int n4;
        int n5 = 0;
        int n6 = this.rate >> 3;
        while ((long)n2 + this.state[25] >= (long)n6) {
            n4 = 0;
            while ((long)n4 < (long)n6 - this.state[25]) {
                int n7 = n3 = (int)(this.state[25] + (long)n4) >> 3;
                this.state[n7] = this.state[n7] ^ KeccakRandomGenerator.toUnsignedLong(byArray[n4 + n5] & 0xFF) << (int)(8L * (this.state[25] + (long)n4 & 7L));
                ++n4;
            }
            n2 = (int)((long)n2 - ((long)n6 - this.state[25]));
            n5 = (int)((long)n5 + ((long)n6 - this.state[25]));
            this.state[25] = 0L;
            KeccakRandomGenerator.keccakPermutation(this.state);
        }
        for (n4 = 0; n4 < n2; ++n4) {
            int n8 = n3 = (int)(this.state[25] + (long)n4) >> 3;
            this.state[n8] = this.state[n8] ^ KeccakRandomGenerator.toUnsignedLong(byArray[n4 + n5] & 0xFF) << (int)(8L * (this.state[25] + (long)n4 & 7L));
        }
        this.state[25] = this.state[25] + (long)n2;
    }

    private void keccakIncFinalize(int n2) {
        int n3 = this.rate >> 3;
        int n4 = (int)this.state[25] >> 3;
        this.state[n4] = this.state[n4] ^ KeccakRandomGenerator.toUnsignedLong(n2) << (int)(8L * (this.state[25] & 7L));
        int n5 = n3 - 1 >> 3;
        this.state[n5] = this.state[n5] ^ KeccakRandomGenerator.toUnsignedLong(128) << 8 * (n3 - 1 & 7);
        this.state[25] = 0L;
    }

    private void keccakIncSqueeze(byte[] byArray, int n2) {
        int n3;
        int n4 = this.rate >> 3;
        for (n3 = 0; n3 < n2 && (long)n3 < this.state[25]; ++n3) {
            byArray[n3] = (byte)(this.state[(int)((long)n4 - this.state[25] + (long)n3 >> 3)] >> (int)(8L * ((long)n4 - this.state[25] + (long)n3 & 7L)));
        }
        int n5 = n3;
        n2 -= n3;
        this.state[25] = this.state[25] - (long)n3;
        while (n2 > 0) {
            KeccakRandomGenerator.keccakPermutation(this.state);
            for (n3 = 0; n3 < n2 && n3 < n4; ++n3) {
                byArray[n5 + n3] = (byte)(this.state[n3 >> 3] >> 8 * (n3 & 7));
            }
            n5 += n3;
            n2 -= n3;
            this.state[25] = n4 - n3;
        }
    }

    public void squeeze(byte[] byArray, int n2) {
        this.keccakIncSqueeze(byArray, n2);
    }

    public void randomGeneratorInit(byte[] byArray, byte[] byArray2, int n2, int n3) {
        byte[] byArray3 = new byte[]{1};
        this.keccakIncAbsorb(byArray, n2);
        this.keccakIncAbsorb(byArray2, n3);
        this.keccakIncAbsorb(byArray3, byArray3.length);
        this.keccakIncFinalize(31);
    }

    public void seedExpanderInit(byte[] byArray, int n2) {
        byte[] byArray2 = new byte[]{2};
        this.keccakIncAbsorb(byArray, n2);
        this.keccakIncAbsorb(byArray2, 1);
        this.keccakIncFinalize(31);
    }

    public void expandSeed(byte[] byArray, int n2) {
        int n3 = n2 & 7;
        this.keccakIncSqueeze(byArray, n2 - n3);
        if (n3 != 0) {
            byte[] byArray2 = new byte[8];
            this.keccakIncSqueeze(byArray2, 8);
            System.arraycopy(byArray2, 0, byArray, n2 - n3, n3);
        }
    }

    public void SHAKE256_512_ds(byte[] byArray, byte[] byArray2, int n2, byte[] byArray3) {
        Arrays.fill(this.state, 0L);
        this.keccakIncAbsorb(byArray2, n2);
        this.keccakIncAbsorb(byArray3, byArray3.length);
        this.keccakIncFinalize(31);
        this.keccakIncSqueeze(byArray, 64);
    }

    private static long toUnsignedLong(int n2) {
        return (long)n2 & 0xFFFFFFFFL;
    }
}

