/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.hqc;

class GF2PolynomialCalculator {
    private final int VEC_N_SIZE_64;
    private final int PARAM_N;
    private final long RED_MASK;

    GF2PolynomialCalculator(int n2, int n3, long l2) {
        this.VEC_N_SIZE_64 = n2;
        this.PARAM_N = n3;
        this.RED_MASK = l2;
    }

    protected void multLongs(long[] lArray, long[] lArray2, long[] lArray3) {
        long[] lArray4 = new long[this.VEC_N_SIZE_64 << 3];
        long[] lArray5 = new long[(this.VEC_N_SIZE_64 << 1) + 1];
        this.karatsuba(lArray5, 0, lArray2, 0, lArray3, 0, this.VEC_N_SIZE_64, lArray4, 0);
        this.reduce(lArray, lArray5);
    }

    private void base_mul(long[] lArray, int n2, long l2, long l3) {
        long l4;
        int n3;
        long l5 = 0L;
        long l6 = 0L;
        long[] lArray2 = new long[16];
        long[] lArray3 = new long[4];
        lArray2[0] = 0L;
        lArray2[1] = l3 & 0xFFFFFFFFFFFFFFFL;
        lArray2[2] = lArray2[1] << 1;
        lArray2[3] = lArray2[2] ^ lArray2[1];
        lArray2[4] = lArray2[2] << 1;
        lArray2[5] = lArray2[4] ^ lArray2[1];
        lArray2[6] = lArray2[3] << 1;
        lArray2[7] = lArray2[6] ^ lArray2[1];
        lArray2[8] = lArray2[4] << 1;
        lArray2[9] = lArray2[8] ^ lArray2[1];
        lArray2[10] = lArray2[5] << 1;
        lArray2[11] = lArray2[10] ^ lArray2[1];
        lArray2[12] = lArray2[6] << 1;
        lArray2[13] = lArray2[12] ^ lArray2[1];
        lArray2[14] = lArray2[7] << 1;
        lArray2[15] = lArray2[14] ^ lArray2[1];
        long l7 = 0L;
        long l8 = l2 & 0xFL;
        for (n3 = 0; n3 < 16; ++n3) {
            l4 = l8 - (long)n3;
            l7 ^= lArray2[n3] & -(1L - ((l4 | -l4) >>> 63));
        }
        l6 = l7;
        l5 = 0L;
        for (n3 = 4; n3 < 64; n3 = (int)((byte)(n3 + 4))) {
            l7 = 0L;
            l4 = l2 >> n3 & 0xFL;
            for (int i2 = 0; i2 < 16; ++i2) {
                long l9 = l4 - (long)i2;
                l7 ^= lArray2[i2] & -(1L - ((l9 | -l9) >>> 63));
            }
            l6 ^= l7 << n3;
            l5 ^= l7 >>> 64 - n3;
        }
        lArray3[0] = -(l3 >> 60 & 1L);
        lArray3[1] = -(l3 >> 61 & 1L);
        lArray3[2] = -(l3 >> 62 & 1L);
        lArray3[3] = -(l3 >> 63 & 1L);
        l6 ^= l2 << 60 & lArray3[0];
        l5 ^= l2 >>> 4 & lArray3[0];
        l6 ^= l2 << 61 & lArray3[1];
        l5 ^= l2 >>> 3 & lArray3[1];
        l6 ^= l2 << 62 & lArray3[2];
        l5 ^= l2 >>> 2 & lArray3[2];
        lArray[0 + n2] = l6 ^= l2 << 63 & lArray3[3];
        lArray[1 + n2] = l5 ^= l2 >>> 1 & lArray3[3];
    }

    private void karatsuba_add1(long[] lArray, int n2, long[] lArray2, int n3, long[] lArray3, int n4, long[] lArray4, int n5, int n6, int n7) {
        for (int i2 = 0; i2 < n7; ++i2) {
            lArray[i2 + n2] = lArray3[i2 + n4] ^ lArray3[i2 + n6 + n4];
            lArray2[i2 + n3] = lArray4[i2 + n5] ^ lArray4[i2 + n6 + n5];
        }
        if (n7 < n6) {
            lArray[n7 + n2] = lArray3[n7 + n4];
            lArray2[n7 + n3] = lArray4[n7 + n5];
        }
    }

    private void karatsuba_add2(long[] lArray, int n2, long[] lArray2, int n3, long[] lArray3, int n4, int n5, int n6) {
        int n7;
        for (n7 = 0; n7 < 2 * n5; ++n7) {
            lArray2[n7 + n3] = lArray2[n7 + n3] ^ lArray[n7 + n2];
        }
        for (n7 = 0; n7 < 2 * n6; ++n7) {
            lArray2[n7 + n3] = lArray2[n7 + n3] ^ lArray3[n7 + n4];
        }
        for (n7 = 0; n7 < 2 * n5; ++n7) {
            lArray[n7 + n5 + n2] = lArray[n7 + n5 + n2] ^ lArray2[n7 + n3];
        }
    }

    private void karatsuba(long[] lArray, int n2, long[] lArray2, int n3, long[] lArray3, int n4, int n5, long[] lArray4, int n6) {
        if (n5 == 1) {
            this.base_mul(lArray, n2, lArray2[0 + n3], lArray3[0 + n4]);
            return;
        }
        int n7 = n5 / 2;
        int n8 = (n5 + 1) / 2;
        int n9 = n6;
        int n10 = n9 + n8;
        int n11 = n10 + n8;
        int n12 = n2 + n8 * 2;
        int n13 = n3 + n8;
        int n14 = n4 + n8;
        this.karatsuba(lArray, n2, lArray2, n3, lArray3, n4, n8, lArray4, n6 += 4 * n8);
        this.karatsuba(lArray, n12, lArray2, n13, lArray3, n14, n7, lArray4, n6);
        this.karatsuba_add1(lArray4, n9, lArray4, n10, lArray2, n3, lArray3, n4, n8, n7);
        this.karatsuba(lArray4, n11, lArray4, n9, lArray4, n10, n8, lArray4, n6);
        this.karatsuba_add2(lArray, n2, lArray4, n11, lArray, n12, n8, n7);
    }

    private void reduce(long[] lArray, long[] lArray2) {
        for (int i2 = 0; i2 < this.VEC_N_SIZE_64; ++i2) {
            long l2 = lArray2[i2 + this.VEC_N_SIZE_64 - 1] >>> (this.PARAM_N & 0x3F);
            long l3 = lArray2[i2 + this.VEC_N_SIZE_64] << (int)(64L - ((long)this.PARAM_N & 0x3FL));
            lArray[i2] = lArray2[i2] ^ l2 ^ l3;
        }
        int n2 = this.VEC_N_SIZE_64 - 1;
        lArray[n2] = lArray[n2] & this.RED_MASK;
    }

    static void addLongs(long[] lArray, long[] lArray2, long[] lArray3) {
        for (int i2 = 0; i2 < lArray2.length; ++i2) {
            lArray[i2] = lArray2[i2] ^ lArray3[i2];
        }
    }
}

