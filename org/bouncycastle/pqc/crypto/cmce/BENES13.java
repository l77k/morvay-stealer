/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.cmce;

import org.bouncycastle.pqc.crypto.cmce.BENES;
import org.bouncycastle.pqc.crypto.cmce.Utils;

class BENES13
extends BENES {
    public BENES13(int n2, int n3, int n4) {
        super(n2, n3, n4);
    }

    static void layer_in(long[] lArray, long[] lArray2, int n2) {
        int n3 = 0;
        int n4 = 1 << n2;
        for (int i2 = 0; i2 < 64; i2 += n4 * 2) {
            for (int i3 = i2; i3 < i2 + n4; ++i3) {
                long l2 = lArray[i3 + 0] ^ lArray[i3 + n4];
                int n5 = n3++;
                int n6 = i3 + 0;
                lArray[n6] = lArray[n6] ^ (l2 &= lArray2[n5]);
                int n7 = i3 + n4;
                lArray[n7] = lArray[n7] ^ l2;
                l2 = lArray[64 + i3 + 0] ^ lArray[64 + i3 + n4];
                int n8 = n3++;
                int n9 = 64 + i3 + 0;
                lArray[n9] = lArray[n9] ^ (l2 &= lArray2[n8]);
                int n10 = 64 + i3 + n4;
                lArray[n10] = lArray[n10] ^ l2;
            }
        }
    }

    static void layer_ex(long[] lArray, long[] lArray2, int n2) {
        int n3 = 0;
        int n4 = 1 << n2;
        for (int i2 = 0; i2 < 128; i2 += n4 * 2) {
            for (int i3 = i2; i3 < i2 + n4; ++i3) {
                long l2 = lArray[i3 + 0] ^ lArray[i3 + n4];
                int n5 = n3++;
                int n6 = i3 + 0;
                lArray[n6] = lArray[n6] ^ (l2 &= lArray2[n5]);
                int n7 = i3 + n4;
                lArray[n7] = lArray[n7] ^ l2;
            }
        }
    }

    void apply_benes(byte[] byArray, byte[] byArray2, int n2) {
        int n3;
        int n4;
        int n5;
        int n6 = 0;
        int n7 = 0;
        long[] lArray = new long[128];
        long[] lArray2 = new long[128];
        long[] lArray3 = new long[64];
        long[] lArray4 = new long[64];
        if (n2 == 0) {
            n7 = this.SYS_T * 2 + 40;
            n5 = 0;
        } else {
            n7 = this.SYS_T * 2 + 40 + 12288;
            n5 = -1024;
        }
        for (n4 = 0; n4 < 64; ++n4) {
            lArray[n4 + 0] = Utils.load8(byArray, n6 + n4 * 16 + 0);
            lArray[n4 + 64] = Utils.load8(byArray, n6 + n4 * 16 + 8);
        }
        BENES13.transpose_64x64(lArray2, lArray, 0);
        BENES13.transpose_64x64(lArray2, lArray, 64);
        for (n3 = 0; n3 <= 6; ++n3) {
            for (n4 = 0; n4 < 64; ++n4) {
                lArray3[n4] = Utils.load8(byArray2, n7);
                n7 += 8;
            }
            n7 += n5;
            BENES13.transpose_64x64(lArray4, lArray3);
            BENES13.layer_ex(lArray2, lArray4, n3);
        }
        BENES13.transpose_64x64(lArray, lArray2, 0);
        BENES13.transpose_64x64(lArray, lArray2, 64);
        for (n3 = 0; n3 <= 5; ++n3) {
            for (n4 = 0; n4 < 64; ++n4) {
                lArray3[n4] = Utils.load8(byArray2, n7);
                n7 += 8;
            }
            n7 += n5;
            BENES13.layer_in(lArray, lArray3, n3);
        }
        for (n3 = 4; n3 >= 0; --n3) {
            for (n4 = 0; n4 < 64; ++n4) {
                lArray3[n4] = Utils.load8(byArray2, n7);
                n7 += 8;
            }
            n7 += n5;
            BENES13.layer_in(lArray, lArray3, n3);
        }
        BENES13.transpose_64x64(lArray2, lArray, 0);
        BENES13.transpose_64x64(lArray2, lArray, 64);
        for (n3 = 6; n3 >= 0; --n3) {
            for (n4 = 0; n4 < 64; ++n4) {
                lArray3[n4] = Utils.load8(byArray2, n7);
                n7 += 8;
            }
            n7 += n5;
            BENES13.transpose_64x64(lArray4, lArray3);
            BENES13.layer_ex(lArray2, lArray4, n3);
        }
        BENES13.transpose_64x64(lArray, lArray2, 0);
        BENES13.transpose_64x64(lArray, lArray2, 64);
        for (n4 = 0; n4 < 64; ++n4) {
            Utils.store8(byArray, n6 + n4 * 16 + 0, lArray[0 + n4]);
            Utils.store8(byArray, n6 + n4 * 16 + 8, lArray[64 + n4]);
        }
    }

    @Override
    public void support_gen(short[] sArray, byte[] byArray) {
        int n2;
        int n3;
        byte[][] byArray2 = new byte[this.GFBITS][(1 << this.GFBITS) / 8];
        for (n3 = 0; n3 < this.GFBITS; ++n3) {
            for (n2 = 0; n2 < (1 << this.GFBITS) / 8; ++n2) {
                byArray2[n3][n2] = 0;
            }
        }
        for (n3 = 0; n3 < 1 << this.GFBITS; ++n3) {
            short s2 = Utils.bitrev((short)n3, this.GFBITS);
            for (n2 = 0; n2 < this.GFBITS; ++n2) {
                byte[] byArray3 = byArray2[n2];
                int n4 = n3 / 8;
                byArray3[n4] = (byte)(byArray3[n4] | (s2 >> n2 & 1) << n3 % 8);
            }
        }
        for (n2 = 0; n2 < this.GFBITS; ++n2) {
            this.apply_benes(byArray2[n2], byArray, 0);
        }
        for (n3 = 0; n3 < this.SYS_N; ++n3) {
            sArray[n3] = 0;
            for (n2 = this.GFBITS - 1; n2 >= 0; --n2) {
                int n5 = n3;
                sArray[n5] = (short)(sArray[n5] << 1);
                int n6 = n3;
                sArray[n6] = (short)(sArray[n6] | byArray2[n2][n3 / 8] >> n3 % 8 & 1);
            }
        }
    }
}

