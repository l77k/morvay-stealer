/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.cmce;

import org.bouncycastle.pqc.crypto.cmce.BENES;
import org.bouncycastle.pqc.crypto.cmce.Utils;

class BENES12
extends BENES {
    public BENES12(int n2, int n3, int n4) {
        super(n2, n3, n4);
    }

    static void layerBenes(long[] lArray, long[] lArray2, int n2) {
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
            }
        }
    }

    private void apply_benes(byte[] byArray, byte[] byArray2, int n2) {
        int n3;
        int n4;
        int n5;
        int n6;
        long[] lArray = new long[64];
        long[] lArray2 = new long[64];
        for (n6 = 0; n6 < 64; ++n6) {
            lArray[n6] = Utils.load8(byArray, n6 * 8);
        }
        if (n2 == 0) {
            n5 = 256;
            n4 = this.SYS_T * 2 + 40;
        } else {
            n5 = -256;
            n4 = this.SYS_T * 2 + 40 + (2 * this.GFBITS - 2) * 256;
        }
        BENES12.transpose_64x64(lArray, lArray);
        for (n3 = 0; n3 <= 5; ++n3) {
            for (n6 = 0; n6 < 64; ++n6) {
                lArray2[n6] = Utils.load4(byArray2, n4 + n6 * 4);
            }
            BENES12.transpose_64x64(lArray2, lArray2);
            BENES12.layerBenes(lArray, lArray2, n3);
            n4 += n5;
        }
        BENES12.transpose_64x64(lArray, lArray);
        for (n3 = 0; n3 <= 5; ++n3) {
            for (n6 = 0; n6 < 32; ++n6) {
                lArray2[n6] = Utils.load8(byArray2, n4 + n6 * 8);
            }
            BENES12.layerBenes(lArray, lArray2, n3);
            n4 += n5;
        }
        for (n3 = 4; n3 >= 0; --n3) {
            for (n6 = 0; n6 < 32; ++n6) {
                lArray2[n6] = Utils.load8(byArray2, n4 + n6 * 8);
            }
            BENES12.layerBenes(lArray, lArray2, n3);
            n4 += n5;
        }
        BENES12.transpose_64x64(lArray, lArray);
        for (n3 = 5; n3 >= 0; --n3) {
            for (n6 = 0; n6 < 64; ++n6) {
                lArray2[n6] = Utils.load4(byArray2, n4 + n6 * 4);
            }
            BENES12.transpose_64x64(lArray2, lArray2);
            BENES12.layerBenes(lArray, lArray2, n3);
            n4 += n5;
        }
        BENES12.transpose_64x64(lArray, lArray);
        for (n6 = 0; n6 < 64; ++n6) {
            Utils.store8(byArray, n6 * 8, lArray[n6]);
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
        for (n3 = 0; n3 < this.GFBITS; ++n3) {
            this.apply_benes(byArray2[n3], byArray, 0);
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

