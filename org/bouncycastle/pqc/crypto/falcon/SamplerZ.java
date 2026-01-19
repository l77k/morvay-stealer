/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.falcon;

import org.bouncycastle.pqc.crypto.falcon.FPREngine;
import org.bouncycastle.pqc.crypto.falcon.FalconFPR;
import org.bouncycastle.pqc.crypto.falcon.FalconRNG;
import org.bouncycastle.pqc.crypto.falcon.SamplerCtx;

class SamplerZ {
    FPREngine fpr = new FPREngine();

    SamplerZ() {
    }

    int sample(SamplerCtx samplerCtx, FalconFPR falconFPR, FalconFPR falconFPR2) {
        return this.sampler(samplerCtx, falconFPR, falconFPR2);
    }

    int gaussian0_sampler(FalconRNG falconRNG) {
        int[] nArray = new int[]{10745844, 3068844, 3741698, 5559083, 1580863, 8248194, 2260429, 13669192, 2736639, 708981, 4421575, 10046180, 169348, 7122675, 4136815, 30538, 13063405, 7650655, 4132, 14505003, 7826148, 417, 16768101, 11363290, 31, 8444042, 8086568, 1, 12844466, 265321, 0, 1232676, 13644283, 0, 38047, 9111839, 0, 870, 6138264, 0, 14, 12545723, 0, 0, 3104126, 0, 0, 28824, 0, 0, 198, 0, 0, 1};
        long l2 = falconRNG.prng_get_u64();
        int n2 = falconRNG.prng_get_u8() & 0xFF;
        int n3 = (int)l2 & 0xFFFFFF;
        int n4 = (int)(l2 >>> 24) & 0xFFFFFF;
        int n5 = (int)(l2 >>> 48) | n2 << 16;
        int n6 = 0;
        for (int i2 = 0; i2 < nArray.length; i2 += 3) {
            int n7 = nArray[i2 + 2];
            int n8 = nArray[i2 + 1];
            int n9 = nArray[i2 + 0];
            int n10 = n3 - n7 >>> 31;
            n10 = n4 - n8 - n10 >>> 31;
            n10 = n5 - n9 - n10 >>> 31;
            n6 += n10;
        }
        return n6;
    }

    int BerExp(FalconRNG falconRNG, FalconFPR falconFPR, FalconFPR falconFPR2) {
        int n2;
        int n3 = (int)this.fpr.fpr_trunc(this.fpr.fpr_mul(falconFPR, this.fpr.fpr_inv_log2));
        FalconFPR falconFPR3 = this.fpr.fpr_sub(falconFPR, this.fpr.fpr_mul(this.fpr.fpr_of(n3), this.fpr.fpr_log2));
        int n4 = n3;
        n4 ^= (n4 ^ 0x3F) & -(63 - n4 >>> 31);
        n3 = n4;
        long l2 = (this.fpr.fpr_expm_p63(falconFPR3, falconFPR2) << 1) - 1L >>> n3;
        int n5 = 64;
        while ((n2 = (falconRNG.prng_get_u8() & 0xFF) - ((int)(l2 >>> (n5 -= 8)) & 0xFF)) == 0 && n5 > 0) {
        }
        return n2 >>> 31;
    }

    int sampler(SamplerCtx samplerCtx, FalconFPR falconFPR, FalconFPR falconFPR2) {
        int n2;
        int n3;
        FalconFPR falconFPR3;
        SamplerCtx samplerCtx2 = samplerCtx;
        int n4 = (int)this.fpr.fpr_floor(falconFPR);
        FalconFPR falconFPR4 = this.fpr.fpr_sub(falconFPR, this.fpr.fpr_of(n4));
        FalconFPR falconFPR5 = this.fpr.fpr_half(this.fpr.fpr_sqr(falconFPR2));
        FalconFPR falconFPR6 = this.fpr.fpr_mul(falconFPR2, samplerCtx2.sigma_min);
        do {
            n3 = this.gaussian0_sampler(samplerCtx2.p);
            int n5 = samplerCtx2.p.prng_get_u8() & 0xFF & 1;
            n2 = n5 + ((n5 << 1) - 1) * n3;
            falconFPR3 = this.fpr.fpr_mul(this.fpr.fpr_sqr(this.fpr.fpr_sub(this.fpr.fpr_of(n2), falconFPR4)), falconFPR5);
        } while (this.BerExp(samplerCtx2.p, falconFPR3 = this.fpr.fpr_sub(falconFPR3, this.fpr.fpr_mul(this.fpr.fpr_of(n3 * n3), this.fpr.fpr_inv_2sqrsigma0)), falconFPR6) == 0);
        return n4 + n2;
    }
}

