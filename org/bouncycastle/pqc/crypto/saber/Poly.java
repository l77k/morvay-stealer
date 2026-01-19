/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.saber;

import org.bouncycastle.pqc.crypto.saber.SABEREngine;
import org.bouncycastle.pqc.crypto.saber.Utils;

class Poly {
    private static final int KARATSUBA_N = 64;
    private static int SCHB_N = 16;
    private final int N_RES;
    private final int N_SB;
    private final int N_SB_RES;
    private final int SABER_N;
    private final int SABER_L;
    private final SABEREngine engine;
    private final Utils utils;

    public Poly(SABEREngine sABEREngine) {
        this.engine = sABEREngine;
        this.SABER_L = sABEREngine.getSABER_L();
        this.SABER_N = sABEREngine.getSABER_N();
        this.N_RES = this.SABER_N << 1;
        this.N_SB = this.SABER_N >> 2;
        this.N_SB_RES = 2 * this.N_SB - 1;
        this.utils = sABEREngine.getUtils();
    }

    public void GenMatrix(short[][][] sArray, byte[] byArray) {
        byte[] byArray2 = new byte[this.SABER_L * this.engine.getSABER_POLYVECBYTES()];
        this.engine.symmetric.prf(byArray2, byArray, this.engine.getSABER_SEEDBYTES(), byArray2.length);
        for (int i2 = 0; i2 < this.SABER_L; ++i2) {
            this.utils.BS2POLVECq(byArray2, i2 * this.engine.getSABER_POLYVECBYTES(), sArray[i2]);
        }
    }

    public void GenSecret(short[][] sArray, byte[] byArray) {
        byte[] byArray2 = new byte[this.SABER_L * this.engine.getSABER_POLYCOINBYTES()];
        this.engine.symmetric.prf(byArray2, byArray, this.engine.getSABER_NOISE_SEEDBYTES(), byArray2.length);
        for (int i2 = 0; i2 < this.SABER_L; ++i2) {
            if (!this.engine.usingEffectiveMasking) {
                this.cbd(sArray[i2], byArray2, i2 * this.engine.getSABER_POLYCOINBYTES());
                continue;
            }
            for (int i3 = 0; i3 < this.SABER_N / 4; ++i3) {
                sArray[i2][4 * i3] = (short)((byArray2[i3 + i2 * this.engine.getSABER_POLYCOINBYTES()] & 3 ^ 2) - 2);
                sArray[i2][4 * i3 + 1] = (short)((byArray2[i3 + i2 * this.engine.getSABER_POLYCOINBYTES()] >>> 2 & 3 ^ 2) - 2);
                sArray[i2][4 * i3 + 2] = (short)((byArray2[i3 + i2 * this.engine.getSABER_POLYCOINBYTES()] >>> 4 & 3 ^ 2) - 2);
                sArray[i2][4 * i3 + 3] = (short)((byArray2[i3 + i2 * this.engine.getSABER_POLYCOINBYTES()] >>> 6 & 3 ^ 2) - 2);
            }
        }
    }

    private long load_littleendian(byte[] byArray, int n2, int n3) {
        long l2 = byArray[n2 + 0] & 0xFF;
        for (int i2 = 1; i2 < n3; ++i2) {
            l2 |= (long)(byArray[n2 + i2] & 0xFF) << 8 * i2;
        }
        return l2;
    }

    private void cbd(short[] sArray, byte[] byArray, int n2) {
        block7: {
            int[] nArray;
            int[] nArray2;
            block8: {
                block6: {
                    nArray2 = new int[4];
                    nArray = new int[4];
                    if (this.engine.getSABER_MU() != 6) break block6;
                    for (int i2 = 0; i2 < this.SABER_N / 4; ++i2) {
                        int n3 = (int)this.load_littleendian(byArray, n2 + 3 * i2, 3);
                        int n4 = 0;
                        for (int i3 = 0; i3 < 3; ++i3) {
                            n4 += n3 >> i3 & 0x249249;
                        }
                        nArray2[0] = n4 & 7;
                        nArray[0] = n4 >>> 3 & 7;
                        nArray2[1] = n4 >>> 6 & 7;
                        nArray[1] = n4 >>> 9 & 7;
                        nArray2[2] = n4 >>> 12 & 7;
                        nArray[2] = n4 >>> 15 & 7;
                        nArray2[3] = n4 >>> 18 & 7;
                        nArray[3] = n4 >>> 21;
                        sArray[4 * i2 + 0] = (short)(nArray2[0] - nArray[0]);
                        sArray[4 * i2 + 1] = (short)(nArray2[1] - nArray[1]);
                        sArray[4 * i2 + 2] = (short)(nArray2[2] - nArray[2]);
                        sArray[4 * i2 + 3] = (short)(nArray2[3] - nArray[3]);
                    }
                    break block7;
                }
                if (this.engine.getSABER_MU() != 8) break block8;
                for (int i4 = 0; i4 < this.SABER_N / 4; ++i4) {
                    int n5 = (int)this.load_littleendian(byArray, n2 + 4 * i4, 4);
                    int n6 = 0;
                    for (int i5 = 0; i5 < 4; ++i5) {
                        n6 += n5 >>> i5 & 0x11111111;
                    }
                    nArray2[0] = n6 & 0xF;
                    nArray[0] = n6 >>> 4 & 0xF;
                    nArray2[1] = n6 >>> 8 & 0xF;
                    nArray[1] = n6 >>> 12 & 0xF;
                    nArray2[2] = n6 >>> 16 & 0xF;
                    nArray[2] = n6 >>> 20 & 0xF;
                    nArray2[3] = n6 >>> 24 & 0xF;
                    nArray[3] = n6 >>> 28;
                    sArray[4 * i4 + 0] = (short)(nArray2[0] - nArray[0]);
                    sArray[4 * i4 + 1] = (short)(nArray2[1] - nArray[1]);
                    sArray[4 * i4 + 2] = (short)(nArray2[2] - nArray[2]);
                    sArray[4 * i4 + 3] = (short)(nArray2[3] - nArray[3]);
                }
                break block7;
            }
            if (this.engine.getSABER_MU() != 10) break block7;
            for (int i6 = 0; i6 < this.SABER_N / 4; ++i6) {
                long l2 = this.load_littleendian(byArray, n2 + 5 * i6, 5);
                long l3 = 0L;
                for (int i7 = 0; i7 < 5; ++i7) {
                    l3 += l2 >>> i7 & 0x842108421L;
                }
                nArray2[0] = (int)(l3 & 0x1FL);
                nArray[0] = (int)(l3 >>> 5 & 0x1FL);
                nArray2[1] = (int)(l3 >>> 10 & 0x1FL);
                nArray[1] = (int)(l3 >>> 15 & 0x1FL);
                nArray2[2] = (int)(l3 >>> 20 & 0x1FL);
                nArray[2] = (int)(l3 >>> 25 & 0x1FL);
                nArray2[3] = (int)(l3 >>> 30 & 0x1FL);
                nArray[3] = (int)(l3 >>> 35);
                sArray[4 * i6 + 0] = (short)(nArray2[0] - nArray[0]);
                sArray[4 * i6 + 1] = (short)(nArray2[1] - nArray[1]);
                sArray[4 * i6 + 2] = (short)(nArray2[2] - nArray[2]);
                sArray[4 * i6 + 3] = (short)(nArray2[3] - nArray[3]);
            }
        }
    }

    private short OVERFLOWING_MUL(int n2, int n3) {
        return (short)(n2 * n3);
    }

    private void karatsuba_simple(int[] nArray, int[] nArray2, int[] nArray3) {
        int n2;
        int[] nArray4 = new int[31];
        int[] nArray5 = new int[31];
        int[] nArray6 = new int[31];
        int[] nArray7 = new int[63];
        for (n2 = 0; n2 < 16; ++n2) {
            int n3 = nArray[n2];
            int n4 = nArray[n2 + 16];
            int n5 = nArray[n2 + 32];
            int n6 = nArray[n2 + 48];
            for (int i2 = 0; i2 < 16; ++i2) {
                int n7 = nArray2[i2];
                int n8 = nArray2[i2 + 16];
                nArray3[n2 + i2 + 0] = nArray3[n2 + i2 + 0] + this.OVERFLOWING_MUL(n3, n7);
                nArray3[n2 + i2 + 32] = nArray3[n2 + i2 + 32] + this.OVERFLOWING_MUL(n4, n8);
                int n9 = n7 + n8;
                int n10 = n3 + n4;
                nArray4[n2 + i2] = (int)((long)nArray4[n2 + i2] + (long)n9 * (long)n10);
                n9 = nArray2[i2 + 32];
                n10 = nArray2[i2 + 48];
                nArray3[n2 + i2 + 64] = nArray3[n2 + i2 + 64] + this.OVERFLOWING_MUL(n9, n5);
                nArray3[n2 + i2 + 96] = nArray3[n2 + i2 + 96] + this.OVERFLOWING_MUL(n10, n6);
                int n11 = n5 + n6;
                int n12 = n9 + n10;
                nArray6[n2 + i2] = nArray6[n2 + i2] + this.OVERFLOWING_MUL(n11, n12);
                n7 += n9;
                n9 = n3 + n5;
                nArray7[n2 + i2 + 0] = nArray7[n2 + i2 + 0] + this.OVERFLOWING_MUL(n7, n9);
                n8 += n10;
                n10 = n4 + n6;
                nArray7[n2 + i2 + 32] = nArray7[n2 + i2 + 32] + this.OVERFLOWING_MUL(n8, n10);
                nArray5[n2 + i2] = nArray5[n2 + i2] + this.OVERFLOWING_MUL(n7 += n8, n9 += n10);
            }
        }
        for (n2 = 0; n2 < 31; ++n2) {
            nArray5[n2] = nArray5[n2] - nArray7[n2 + 0] - nArray7[n2 + 32];
            nArray4[n2] = nArray4[n2] - nArray3[n2 + 0] - nArray3[n2 + 32];
            nArray6[n2] = nArray6[n2] - nArray3[n2 + 64] - nArray3[n2 + 96];
        }
        for (n2 = 0; n2 < 31; ++n2) {
            nArray7[n2 + 16] = nArray7[n2 + 16] + nArray5[n2];
            nArray3[n2 + 16] = nArray3[n2 + 16] + nArray4[n2];
            nArray3[n2 + 80] = nArray3[n2 + 80] + nArray6[n2];
        }
        for (n2 = 0; n2 < 63; ++n2) {
            nArray7[n2] = nArray7[n2] - nArray3[n2] - nArray3[n2 + 64];
        }
        for (n2 = 0; n2 < 63; ++n2) {
            nArray3[n2 + 32] = nArray3[n2 + 32] + nArray7[n2];
        }
    }

    private void toom_cook_4way(short[] sArray, short[] sArray2, short[] sArray3) {
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        int n10;
        int n11 = 43691;
        int n12 = 36409;
        int n13 = 61167;
        int[] nArray = new int[this.N_SB];
        int[] nArray2 = new int[this.N_SB];
        int[] nArray3 = new int[this.N_SB];
        int[] nArray4 = new int[this.N_SB];
        int[] nArray5 = new int[this.N_SB];
        int[] nArray6 = new int[this.N_SB];
        int[] nArray7 = new int[this.N_SB];
        int[] nArray8 = new int[this.N_SB];
        int[] nArray9 = new int[this.N_SB];
        int[] nArray10 = new int[this.N_SB];
        int[] nArray11 = new int[this.N_SB];
        int[] nArray12 = new int[this.N_SB];
        int[] nArray13 = new int[this.N_SB];
        int[] nArray14 = new int[this.N_SB];
        int[] nArray15 = new int[this.N_SB_RES];
        int[] nArray16 = new int[this.N_SB_RES];
        int[] nArray17 = new int[this.N_SB_RES];
        int[] nArray18 = new int[this.N_SB_RES];
        int[] nArray19 = new int[this.N_SB_RES];
        int[] nArray20 = new int[this.N_SB_RES];
        int[] nArray21 = new int[this.N_SB_RES];
        short[] sArray4 = sArray3;
        for (n10 = 0; n10 < this.N_SB; ++n10) {
            n9 = sArray[n10];
            n8 = sArray[n10 + this.N_SB];
            n7 = sArray[n10 + this.N_SB * 2];
            n6 = sArray[n10 + this.N_SB * 3];
            n5 = n9 + n7;
            n4 = n8 + n6;
            n3 = n5 + n4;
            n2 = n5 - n4;
            nArray3[n10] = n3;
            nArray4[n10] = n2;
            n5 = (short)((n9 << 2) + n7 << 1);
            n4 = (short)((n8 << 2) + n6);
            n3 = (short)(n5 + n4);
            n2 = (short)(n5 - n4);
            nArray5[n10] = n3;
            nArray6[n10] = n2;
            nArray2[n10] = n5 = (int)((short)((n6 << 3) + (n7 << 2) + (n8 << 1) + n9));
            nArray7[n10] = n9;
            nArray[n10] = n6;
        }
        for (n10 = 0; n10 < this.N_SB; ++n10) {
            n9 = sArray2[n10];
            n8 = sArray2[n10 + this.N_SB];
            n7 = sArray2[n10 + this.N_SB * 2];
            n6 = sArray2[n10 + this.N_SB * 3];
            n5 = n9 + n7;
            n4 = n8 + n6;
            n3 = n5 + n4;
            n2 = n5 - n4;
            nArray10[n10] = n3;
            nArray11[n10] = n2;
            n5 = (n9 << 2) + n7 << 1;
            n4 = (n8 << 2) + n6;
            n3 = n5 + n4;
            n2 = n5 - n4;
            nArray12[n10] = n3;
            nArray13[n10] = n2;
            nArray9[n10] = n5 = (n6 << 3) + (n7 << 2) + (n8 << 1) + n9;
            nArray14[n10] = n9;
            nArray8[n10] = n6;
        }
        this.karatsuba_simple(nArray, nArray8, nArray15);
        this.karatsuba_simple(nArray2, nArray9, nArray16);
        this.karatsuba_simple(nArray3, nArray10, nArray17);
        this.karatsuba_simple(nArray4, nArray11, nArray18);
        this.karatsuba_simple(nArray5, nArray12, nArray19);
        this.karatsuba_simple(nArray6, nArray13, nArray20);
        this.karatsuba_simple(nArray7, nArray14, nArray21);
        for (int i2 = 0; i2 < this.N_SB_RES; ++i2) {
            n9 = nArray15[i2];
            n8 = nArray16[i2];
            n7 = nArray17[i2];
            n6 = nArray18[i2];
            n5 = nArray19[i2];
            n4 = nArray20[i2];
            n3 = nArray21[i2];
            n8 += n5;
            n4 -= n5;
            n6 = (n6 & 0xFFFF) - (n7 & 0xFFFF) >>> 1;
            n5 -= n9;
            n5 -= n3 << 6;
            n5 = (n5 << 1) + n4;
            n8 = n8 - ((n7 += n6) << 6) - n7;
            n7 -= n3;
            n5 = ((n5 & 0xFFFF) - (n7 << 3)) * n11 >> 3;
            n4 += (n8 += 45 * (n7 -= n9));
            n8 = ((n8 & 0xFFFF) + ((n6 & 0xFFFF) << 4)) * n12 >> 1;
            n6 = -(n6 + n8);
            n4 = (30 * (n8 & 0xFFFF) - (n4 & 0xFFFF)) * n13 >> 2;
            n7 -= n5;
            n8 -= n4;
            int n14 = i2;
            sArray4[n14] = (short)(sArray4[n14] + (n3 & 0xFFFF));
            int n15 = i2 + 64;
            sArray4[n15] = (short)(sArray4[n15] + (n4 & 0xFFFF));
            int n16 = i2 + 128;
            sArray4[n16] = (short)(sArray4[n16] + (n5 & 0xFFFF));
            int n17 = i2 + 192;
            sArray4[n17] = (short)(sArray4[n17] + (n6 & 0xFFFF));
            int n18 = i2 + 256;
            sArray4[n18] = (short)(sArray4[n18] + (n7 & 0xFFFF));
            int n19 = i2 + 320;
            sArray4[n19] = (short)(sArray4[n19] + (n8 & 0xFFFF));
            int n20 = i2 + 384;
            sArray4[n20] = (short)(sArray4[n20] + (n9 & 0xFFFF));
        }
    }

    private void poly_mul_acc(short[] sArray, short[] sArray2, short[] sArray3) {
        short[] sArray4 = new short[2 * this.SABER_N];
        this.toom_cook_4way(sArray, sArray2, sArray4);
        for (int i2 = this.SABER_N; i2 < 2 * this.SABER_N; ++i2) {
            int n2 = i2 - this.SABER_N;
            sArray3[n2] = (short)(sArray3[n2] + (sArray4[i2 - this.SABER_N] - sArray4[i2]));
        }
    }

    public void MatrixVectorMul(short[][][] sArray, short[][] sArray2, short[][] sArray3, int n2) {
        for (int i2 = 0; i2 < this.SABER_L; ++i2) {
            for (int i3 = 0; i3 < this.SABER_L; ++i3) {
                if (n2 == 1) {
                    this.poly_mul_acc(sArray[i3][i2], sArray2[i3], sArray3[i2]);
                    continue;
                }
                this.poly_mul_acc(sArray[i2][i3], sArray2[i3], sArray3[i2]);
            }
        }
    }

    public void InnerProd(short[][] sArray, short[][] sArray2, short[] sArray3) {
        for (int i2 = 0; i2 < this.SABER_L; ++i2) {
            this.poly_mul_acc(sArray[i2], sArray2[i2], sArray3);
        }
    }
}

