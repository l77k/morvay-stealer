/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.falcon;

import org.bouncycastle.pqc.crypto.falcon.FPREngine;
import org.bouncycastle.pqc.crypto.falcon.FalconCommon;
import org.bouncycastle.pqc.crypto.falcon.FalconFFT;
import org.bouncycastle.pqc.crypto.falcon.FalconFPR;
import org.bouncycastle.pqc.crypto.falcon.SHAKE256;
import org.bouncycastle.pqc.crypto.falcon.SamplerCtx;
import org.bouncycastle.pqc.crypto.falcon.SamplerZ;

class FalconSign {
    FPREngine fpr = new FPREngine();
    FalconFFT fft = new FalconFFT();
    FalconCommon common = new FalconCommon();

    FalconSign() {
    }

    private static int MKN(int n2) {
        return 1 << n2;
    }

    int ffLDL_treesize(int n2) {
        return n2 + 1 << n2;
    }

    void ffLDL_fft_inner(FalconFPR[] falconFPRArray, int n2, FalconFPR[] falconFPRArray2, int n3, FalconFPR[] falconFPRArray3, int n4, int n5, FalconFPR[] falconFPRArray4, int n6) {
        int n7 = FalconSign.MKN(n5);
        if (n7 == 1) {
            falconFPRArray[n2 + 0] = falconFPRArray2[n3 + 0];
            return;
        }
        int n8 = n7 >> 1;
        this.fft.poly_LDLmv_fft(falconFPRArray4, n6, falconFPRArray, n2, falconFPRArray2, n3, falconFPRArray3, n4, falconFPRArray2, n3, n5);
        this.fft.poly_split_fft(falconFPRArray3, n4, falconFPRArray3, n4 + n8, falconFPRArray2, n3, n5);
        this.fft.poly_split_fft(falconFPRArray2, n3, falconFPRArray2, n3 + n8, falconFPRArray4, n6, n5);
        this.ffLDL_fft_inner(falconFPRArray, n2 + n7, falconFPRArray3, n4, falconFPRArray3, n4 + n8, n5 - 1, falconFPRArray4, n6);
        this.ffLDL_fft_inner(falconFPRArray, n2 + n7 + this.ffLDL_treesize(n5 - 1), falconFPRArray2, n3, falconFPRArray2, n3 + n8, n5 - 1, falconFPRArray4, n6);
    }

    void ffLDL_fft(FalconFPR[] falconFPRArray, int n2, FalconFPR[] falconFPRArray2, int n3, FalconFPR[] falconFPRArray3, int n4, FalconFPR[] falconFPRArray4, int n5, int n6, FalconFPR[] falconFPRArray5, int n7) {
        int n8 = FalconSign.MKN(n6);
        if (n8 == 1) {
            falconFPRArray[n2 + 0] = falconFPRArray2[n3 + 0];
            return;
        }
        int n9 = n8 >> 1;
        int n10 = n7;
        int n11 = n7 + n8;
        System.arraycopy(falconFPRArray2, n3, falconFPRArray5, n10, n8);
        this.fft.poly_LDLmv_fft(falconFPRArray5, n11, falconFPRArray, n2, falconFPRArray2, n3, falconFPRArray3, n4, falconFPRArray4, n5, n6);
        this.fft.poly_split_fft(falconFPRArray5, n7 += n8 << 1, falconFPRArray5, n7 + n9, falconFPRArray5, n10, n6);
        this.fft.poly_split_fft(falconFPRArray5, n10, falconFPRArray5, n10 + n9, falconFPRArray5, n11, n6);
        System.arraycopy(falconFPRArray5, n7, falconFPRArray5, n11, n8);
        this.ffLDL_fft_inner(falconFPRArray, n2 + n8, falconFPRArray5, n11, falconFPRArray5, n11 + n9, n6 - 1, falconFPRArray5, n7);
        this.ffLDL_fft_inner(falconFPRArray, n2 + n8 + this.ffLDL_treesize(n6 - 1), falconFPRArray5, n10, falconFPRArray5, n10 + n9, n6 - 1, falconFPRArray5, n7);
    }

    void ffLDL_binary_normalize(FalconFPR[] falconFPRArray, int n2, int n3, int n4) {
        int n5 = FalconSign.MKN(n4);
        if (n5 == 1) {
            falconFPRArray[n2 + 0] = this.fpr.fpr_mul(this.fpr.fpr_sqrt(falconFPRArray[n2 + 0]), this.fpr.fpr_inv_sigma[n3]);
        } else {
            this.ffLDL_binary_normalize(falconFPRArray, n2 + n5, n3, n4 - 1);
            this.ffLDL_binary_normalize(falconFPRArray, n2 + n5 + this.ffLDL_treesize(n4 - 1), n3, n4 - 1);
        }
    }

    void smallints_to_fpr(FalconFPR[] falconFPRArray, int n2, byte[] byArray, int n3, int n4) {
        int n5 = FalconSign.MKN(n4);
        for (int i2 = 0; i2 < n5; ++i2) {
            falconFPRArray[n2 + i2] = this.fpr.fpr_of(byArray[n3 + i2]);
        }
    }

    int skoff_b00(int n2) {
        return 0;
    }

    int skoff_b01(int n2) {
        return FalconSign.MKN(n2);
    }

    int skoff_b10(int n2) {
        return 2 * FalconSign.MKN(n2);
    }

    int skoff_b11(int n2) {
        return 3 * FalconSign.MKN(n2);
    }

    int skoff_tree(int n2) {
        return 4 * FalconSign.MKN(n2);
    }

    void expand_privkey(FalconFPR[] falconFPRArray, int n2, byte[] byArray, int n3, byte[] byArray2, int n4, byte[] byArray3, int n5, byte[] byArray4, int n6, int n7, FalconFPR[] falconFPRArray2, int n8) {
        int n9 = FalconSign.MKN(n7);
        int n10 = n2 + this.skoff_b00(n7);
        int n11 = n2 + this.skoff_b01(n7);
        int n12 = n2 + this.skoff_b10(n7);
        int n13 = n2 + this.skoff_b11(n7);
        int n14 = n2 + this.skoff_tree(n7);
        int n15 = n11;
        int n16 = n10;
        int n17 = n13;
        int n18 = n12;
        this.smallints_to_fpr(falconFPRArray, n15, byArray, n3, n7);
        this.smallints_to_fpr(falconFPRArray, n16, byArray2, n4, n7);
        this.smallints_to_fpr(falconFPRArray, n17, byArray3, n5, n7);
        this.smallints_to_fpr(falconFPRArray, n18, byArray4, n6, n7);
        this.fft.FFT(falconFPRArray, n15, n7);
        this.fft.FFT(falconFPRArray, n16, n7);
        this.fft.FFT(falconFPRArray, n17, n7);
        this.fft.FFT(falconFPRArray, n18, n7);
        this.fft.poly_neg(falconFPRArray, n15, n7);
        this.fft.poly_neg(falconFPRArray, n17, n7);
        int n19 = n8;
        int n20 = n19 + n9;
        int n21 = n20 + n9;
        int n22 = n21 + n9;
        System.arraycopy(falconFPRArray, n10, falconFPRArray2, n19, n9);
        this.fft.poly_mulselfadj_fft(falconFPRArray2, n19, n7);
        System.arraycopy(falconFPRArray, n11, falconFPRArray2, n22, n9);
        this.fft.poly_mulselfadj_fft(falconFPRArray2, n22, n7);
        this.fft.poly_add(falconFPRArray2, n19, falconFPRArray2, n22, n7);
        System.arraycopy(falconFPRArray, n10, falconFPRArray2, n20, n9);
        this.fft.poly_muladj_fft(falconFPRArray2, n20, falconFPRArray, n12, n7);
        System.arraycopy(falconFPRArray, n11, falconFPRArray2, n22, n9);
        this.fft.poly_muladj_fft(falconFPRArray2, n22, falconFPRArray, n13, n7);
        this.fft.poly_add(falconFPRArray2, n20, falconFPRArray2, n22, n7);
        System.arraycopy(falconFPRArray, n12, falconFPRArray2, n21, n9);
        this.fft.poly_mulselfadj_fft(falconFPRArray2, n21, n7);
        System.arraycopy(falconFPRArray, n13, falconFPRArray2, n22, n9);
        this.fft.poly_mulselfadj_fft(falconFPRArray2, n22, n7);
        this.fft.poly_add(falconFPRArray2, n21, falconFPRArray2, n22, n7);
        this.ffLDL_fft(falconFPRArray, n14, falconFPRArray2, n19, falconFPRArray2, n20, falconFPRArray2, n21, n7, falconFPRArray2, n22);
        this.ffLDL_binary_normalize(falconFPRArray, n14, n7, n7);
    }

    void ffSampling_fft_dyntree(SamplerZ samplerZ, SamplerCtx samplerCtx, FalconFPR[] falconFPRArray, int n2, FalconFPR[] falconFPRArray2, int n3, FalconFPR[] falconFPRArray3, int n4, FalconFPR[] falconFPRArray4, int n5, FalconFPR[] falconFPRArray5, int n6, int n7, int n8, FalconFPR[] falconFPRArray6, int n9) {
        if (n8 == 0) {
            FalconFPR falconFPR = falconFPRArray3[n4 + 0];
            falconFPR = this.fpr.fpr_mul(this.fpr.fpr_sqrt(falconFPR), this.fpr.fpr_inv_sigma[n7]);
            falconFPRArray[n2 + 0] = this.fpr.fpr_of(samplerZ.sample(samplerCtx, falconFPRArray[n2 + 0], falconFPR));
            falconFPRArray2[n3 + 0] = this.fpr.fpr_of(samplerZ.sample(samplerCtx, falconFPRArray2[n3 + 0], falconFPR));
            return;
        }
        int n10 = 1 << n8;
        int n11 = n10 >> 1;
        this.fft.poly_LDL_fft(falconFPRArray3, n4, falconFPRArray4, n5, falconFPRArray5, n6, n8);
        this.fft.poly_split_fft(falconFPRArray6, n9, falconFPRArray6, n9 + n11, falconFPRArray3, n4, n8);
        System.arraycopy(falconFPRArray6, n9, falconFPRArray3, n4, n10);
        this.fft.poly_split_fft(falconFPRArray6, n9, falconFPRArray6, n9 + n11, falconFPRArray5, n6, n8);
        System.arraycopy(falconFPRArray6, n9, falconFPRArray5, n6, n10);
        System.arraycopy(falconFPRArray4, n5, falconFPRArray6, n9, n10);
        System.arraycopy(falconFPRArray3, n4, falconFPRArray4, n5, n11);
        System.arraycopy(falconFPRArray5, n6, falconFPRArray4, n5 + n11, n11);
        int n12 = n9 + n10;
        this.fft.poly_split_fft(falconFPRArray6, n12, falconFPRArray6, n12 + n11, falconFPRArray2, n3, n8);
        this.ffSampling_fft_dyntree(samplerZ, samplerCtx, falconFPRArray6, n12, falconFPRArray6, n12 + n11, falconFPRArray5, n6, falconFPRArray5, n6 + n11, falconFPRArray4, n5 + n11, n7, n8 - 1, falconFPRArray6, n12 + n10);
        this.fft.poly_merge_fft(falconFPRArray6, n9 + (n10 << 1), falconFPRArray6, n12, falconFPRArray6, n12 + n11, n8);
        System.arraycopy(falconFPRArray2, n3, falconFPRArray6, n12, n10);
        this.fft.poly_sub(falconFPRArray6, n12, falconFPRArray6, n9 + (n10 << 1), n8);
        System.arraycopy(falconFPRArray6, n9 + (n10 << 1), falconFPRArray2, n3, n10);
        this.fft.poly_mul_fft(falconFPRArray6, n9, falconFPRArray6, n12, n8);
        this.fft.poly_add(falconFPRArray, n2, falconFPRArray6, n9, n8);
        int n13 = n9;
        this.fft.poly_split_fft(falconFPRArray6, n13, falconFPRArray6, n13 + n11, falconFPRArray, n2, n8);
        this.ffSampling_fft_dyntree(samplerZ, samplerCtx, falconFPRArray6, n13, falconFPRArray6, n13 + n11, falconFPRArray3, n4, falconFPRArray3, n4 + n11, falconFPRArray4, n5, n7, n8 - 1, falconFPRArray6, n13 + n10);
        this.fft.poly_merge_fft(falconFPRArray, n2, falconFPRArray6, n13, falconFPRArray6, n13 + n11, n8);
    }

    void ffSampling_fft(SamplerZ samplerZ, SamplerCtx samplerCtx, FalconFPR[] falconFPRArray, int n2, FalconFPR[] falconFPRArray2, int n3, FalconFPR[] falconFPRArray3, int n4, FalconFPR[] falconFPRArray4, int n5, FalconFPR[] falconFPRArray5, int n6, int n7, FalconFPR[] falconFPRArray6, int n8) {
        if (n7 == 2) {
            FalconFPR falconFPR;
            FalconFPR falconFPR2;
            int n9 = n4 + 4;
            int n10 = n4 + 8;
            FalconFPR falconFPR3 = falconFPRArray5[n6 + 0];
            FalconFPR falconFPR4 = falconFPRArray5[n6 + 2];
            FalconFPR falconFPR5 = falconFPRArray5[n6 + 1];
            FalconFPR falconFPR6 = falconFPRArray5[n6 + 3];
            FalconFPR falconFPR7 = this.fpr.fpr_add(falconFPR3, falconFPR5);
            FalconFPR falconFPR8 = this.fpr.fpr_add(falconFPR4, falconFPR6);
            FalconFPR falconFPR9 = this.fpr.fpr_half(falconFPR7);
            FalconFPR falconFPR10 = this.fpr.fpr_half(falconFPR8);
            falconFPR7 = this.fpr.fpr_sub(falconFPR3, falconFPR5);
            falconFPR8 = this.fpr.fpr_sub(falconFPR4, falconFPR6);
            FalconFPR falconFPR11 = this.fpr.fpr_mul(this.fpr.fpr_add(falconFPR7, falconFPR8), this.fpr.fpr_invsqrt8);
            FalconFPR falconFPR12 = this.fpr.fpr_mul(this.fpr.fpr_sub(falconFPR8, falconFPR7), this.fpr.fpr_invsqrt8);
            FalconFPR falconFPR13 = falconFPR11;
            FalconFPR falconFPR14 = falconFPR12;
            FalconFPR falconFPR15 = falconFPRArray3[n10 + 3];
            falconFPR11 = this.fpr.fpr_of(samplerZ.sample(samplerCtx, falconFPR13, falconFPR15));
            falconFPR12 = this.fpr.fpr_of(samplerZ.sample(samplerCtx, falconFPR14, falconFPR15));
            falconFPR3 = this.fpr.fpr_sub(falconFPR13, falconFPR11);
            falconFPR4 = this.fpr.fpr_sub(falconFPR14, falconFPR12);
            falconFPR5 = falconFPRArray3[n10 + 0];
            falconFPR6 = falconFPRArray3[n10 + 1];
            falconFPR7 = this.fpr.fpr_sub(this.fpr.fpr_mul(falconFPR3, falconFPR5), this.fpr.fpr_mul(falconFPR4, falconFPR6));
            falconFPR8 = this.fpr.fpr_add(this.fpr.fpr_mul(falconFPR3, falconFPR6), this.fpr.fpr_mul(falconFPR4, falconFPR5));
            falconFPR13 = this.fpr.fpr_add(falconFPR7, falconFPR9);
            falconFPR14 = this.fpr.fpr_add(falconFPR8, falconFPR10);
            falconFPR15 = falconFPRArray3[n10 + 2];
            falconFPR9 = this.fpr.fpr_of(samplerZ.sample(samplerCtx, falconFPR13, falconFPR15));
            falconFPR10 = this.fpr.fpr_of(samplerZ.sample(samplerCtx, falconFPR14, falconFPR15));
            falconFPR3 = falconFPR9;
            falconFPR4 = falconFPR10;
            falconFPR5 = falconFPR11;
            falconFPR6 = falconFPR12;
            falconFPR7 = this.fpr.fpr_mul(this.fpr.fpr_sub(falconFPR5, falconFPR6), this.fpr.fpr_invsqrt2);
            falconFPR8 = this.fpr.fpr_mul(this.fpr.fpr_add(falconFPR5, falconFPR6), this.fpr.fpr_invsqrt2);
            falconFPRArray2[n3 + 0] = falconFPR9 = this.fpr.fpr_add(falconFPR3, falconFPR7);
            falconFPRArray2[n3 + 2] = falconFPR11 = this.fpr.fpr_add(falconFPR4, falconFPR8);
            falconFPRArray2[n3 + 1] = falconFPR10 = this.fpr.fpr_sub(falconFPR3, falconFPR7);
            falconFPRArray2[n3 + 3] = falconFPR12 = this.fpr.fpr_sub(falconFPR4, falconFPR8);
            falconFPR9 = this.fpr.fpr_sub(falconFPRArray5[n6 + 0], falconFPR9);
            falconFPR10 = this.fpr.fpr_sub(falconFPRArray5[n6 + 1], falconFPR10);
            falconFPR11 = this.fpr.fpr_sub(falconFPRArray5[n6 + 2], falconFPR11);
            falconFPR12 = this.fpr.fpr_sub(falconFPRArray5[n6 + 3], falconFPR12);
            falconFPR3 = falconFPR9;
            falconFPR4 = falconFPR11;
            falconFPR5 = falconFPRArray3[n4 + 0];
            falconFPR6 = falconFPRArray3[n4 + 2];
            falconFPR9 = this.fpr.fpr_sub(this.fpr.fpr_mul(falconFPR3, falconFPR5), this.fpr.fpr_mul(falconFPR4, falconFPR6));
            falconFPR11 = this.fpr.fpr_add(this.fpr.fpr_mul(falconFPR3, falconFPR6), this.fpr.fpr_mul(falconFPR4, falconFPR5));
            falconFPR3 = falconFPR10;
            falconFPR4 = falconFPR12;
            falconFPR5 = falconFPRArray3[n4 + 1];
            falconFPR6 = falconFPRArray3[n4 + 3];
            falconFPR10 = this.fpr.fpr_sub(this.fpr.fpr_mul(falconFPR3, falconFPR5), this.fpr.fpr_mul(falconFPR4, falconFPR6));
            falconFPR12 = this.fpr.fpr_add(this.fpr.fpr_mul(falconFPR3, falconFPR6), this.fpr.fpr_mul(falconFPR4, falconFPR5));
            falconFPR9 = this.fpr.fpr_add(falconFPR9, falconFPRArray4[n5 + 0]);
            falconFPR10 = this.fpr.fpr_add(falconFPR10, falconFPRArray4[n5 + 1]);
            falconFPR11 = this.fpr.fpr_add(falconFPR11, falconFPRArray4[n5 + 2]);
            falconFPR12 = this.fpr.fpr_add(falconFPR12, falconFPRArray4[n5 + 3]);
            falconFPR3 = falconFPR9;
            falconFPR4 = falconFPR11;
            falconFPR5 = falconFPR10;
            falconFPR6 = falconFPR12;
            falconFPR7 = this.fpr.fpr_add(falconFPR3, falconFPR5);
            falconFPR8 = this.fpr.fpr_add(falconFPR4, falconFPR6);
            falconFPR9 = this.fpr.fpr_half(falconFPR7);
            falconFPR10 = this.fpr.fpr_half(falconFPR8);
            falconFPR7 = this.fpr.fpr_sub(falconFPR3, falconFPR5);
            falconFPR8 = this.fpr.fpr_sub(falconFPR4, falconFPR6);
            falconFPR11 = this.fpr.fpr_mul(this.fpr.fpr_add(falconFPR7, falconFPR8), this.fpr.fpr_invsqrt8);
            falconFPR12 = this.fpr.fpr_mul(this.fpr.fpr_sub(falconFPR8, falconFPR7), this.fpr.fpr_invsqrt8);
            falconFPR13 = falconFPR11;
            falconFPR14 = falconFPR12;
            falconFPR15 = falconFPRArray3[n9 + 3];
            falconFPR11 = falconFPR2 = this.fpr.fpr_of(samplerZ.sample(samplerCtx, falconFPR13, falconFPR15));
            falconFPR12 = falconFPR = this.fpr.fpr_of(samplerZ.sample(samplerCtx, falconFPR14, falconFPR15));
            falconFPR3 = this.fpr.fpr_sub(falconFPR13, falconFPR2);
            falconFPR4 = this.fpr.fpr_sub(falconFPR14, falconFPR);
            falconFPR5 = falconFPRArray3[n9 + 0];
            falconFPR6 = falconFPRArray3[n9 + 1];
            falconFPR7 = this.fpr.fpr_sub(this.fpr.fpr_mul(falconFPR3, falconFPR5), this.fpr.fpr_mul(falconFPR4, falconFPR6));
            falconFPR8 = this.fpr.fpr_add(this.fpr.fpr_mul(falconFPR3, falconFPR6), this.fpr.fpr_mul(falconFPR4, falconFPR5));
            falconFPR13 = this.fpr.fpr_add(falconFPR7, falconFPR9);
            falconFPR14 = this.fpr.fpr_add(falconFPR8, falconFPR10);
            falconFPR15 = falconFPRArray3[n9 + 2];
            falconFPR9 = this.fpr.fpr_of(samplerZ.sample(samplerCtx, falconFPR13, falconFPR15));
            falconFPR10 = this.fpr.fpr_of(samplerZ.sample(samplerCtx, falconFPR14, falconFPR15));
            falconFPR3 = falconFPR9;
            falconFPR4 = falconFPR10;
            falconFPR5 = falconFPR11;
            falconFPR6 = falconFPR12;
            falconFPR7 = this.fpr.fpr_mul(this.fpr.fpr_sub(falconFPR5, falconFPR6), this.fpr.fpr_invsqrt2);
            falconFPR8 = this.fpr.fpr_mul(this.fpr.fpr_add(falconFPR5, falconFPR6), this.fpr.fpr_invsqrt2);
            falconFPRArray[n2 + 0] = this.fpr.fpr_add(falconFPR3, falconFPR7);
            falconFPRArray[n2 + 2] = this.fpr.fpr_add(falconFPR4, falconFPR8);
            falconFPRArray[n2 + 1] = this.fpr.fpr_sub(falconFPR3, falconFPR7);
            falconFPRArray[n2 + 3] = this.fpr.fpr_sub(falconFPR4, falconFPR8);
            return;
        }
        if (n7 == 1) {
            FalconFPR falconFPR;
            FalconFPR falconFPR16;
            FalconFPR falconFPR17 = falconFPRArray5[n6 + 0];
            FalconFPR falconFPR18 = falconFPRArray5[n6 + 1];
            FalconFPR falconFPR19 = falconFPRArray3[n4 + 3];
            falconFPRArray2[n3 + 0] = falconFPR16 = this.fpr.fpr_of(samplerZ.sample(samplerCtx, falconFPR17, falconFPR19));
            falconFPRArray2[n3 + 1] = falconFPR = this.fpr.fpr_of(samplerZ.sample(samplerCtx, falconFPR18, falconFPR19));
            FalconFPR falconFPR20 = this.fpr.fpr_sub(falconFPR17, falconFPR16);
            FalconFPR falconFPR21 = this.fpr.fpr_sub(falconFPR18, falconFPR);
            FalconFPR falconFPR22 = falconFPRArray3[n4 + 0];
            FalconFPR falconFPR23 = falconFPRArray3[n4 + 1];
            FalconFPR falconFPR24 = this.fpr.fpr_sub(this.fpr.fpr_mul(falconFPR20, falconFPR22), this.fpr.fpr_mul(falconFPR21, falconFPR23));
            FalconFPR falconFPR25 = this.fpr.fpr_add(this.fpr.fpr_mul(falconFPR20, falconFPR23), this.fpr.fpr_mul(falconFPR21, falconFPR22));
            falconFPR17 = this.fpr.fpr_add(falconFPR24, falconFPRArray4[n5 + 0]);
            falconFPR18 = this.fpr.fpr_add(falconFPR25, falconFPRArray4[n5 + 1]);
            falconFPR19 = falconFPRArray3[n4 + 2];
            falconFPRArray[n2 + 0] = this.fpr.fpr_of(samplerZ.sample(samplerCtx, falconFPR17, falconFPR19));
            falconFPRArray[n2 + 1] = this.fpr.fpr_of(samplerZ.sample(samplerCtx, falconFPR18, falconFPR19));
            return;
        }
        int n11 = 1 << n7;
        int n12 = n11 >> 1;
        int n13 = n4 + n11;
        int n14 = n4 + n11 + this.ffLDL_treesize(n7 - 1);
        this.fft.poly_split_fft(falconFPRArray2, n3, falconFPRArray2, n3 + n12, falconFPRArray5, n6, n7);
        this.ffSampling_fft(samplerZ, samplerCtx, falconFPRArray6, n8, falconFPRArray6, n8 + n12, falconFPRArray3, n14, falconFPRArray2, n3, falconFPRArray2, n3 + n12, n7 - 1, falconFPRArray6, n8 + n11);
        this.fft.poly_merge_fft(falconFPRArray2, n3, falconFPRArray6, n8, falconFPRArray6, n8 + n12, n7);
        System.arraycopy(falconFPRArray5, n6, falconFPRArray6, n8, n11);
        this.fft.poly_sub(falconFPRArray6, n8, falconFPRArray2, n3, n7);
        this.fft.poly_mul_fft(falconFPRArray6, n8, falconFPRArray3, n4, n7);
        this.fft.poly_add(falconFPRArray6, n8, falconFPRArray4, n5, n7);
        this.fft.poly_split_fft(falconFPRArray, n2, falconFPRArray, n2 + n12, falconFPRArray6, n8, n7);
        this.ffSampling_fft(samplerZ, samplerCtx, falconFPRArray6, n8, falconFPRArray6, n8 + n12, falconFPRArray3, n13, falconFPRArray, n2, falconFPRArray, n2 + n12, n7 - 1, falconFPRArray6, n8 + n11);
        this.fft.poly_merge_fft(falconFPRArray, n2, falconFPRArray6, n8, falconFPRArray6, n8 + n12, n7);
    }

    int do_sign_tree(SamplerZ samplerZ, SamplerCtx samplerCtx, short[] sArray, int n2, FalconFPR[] falconFPRArray, int n3, short[] sArray2, int n4, int n5, FalconFPR[] falconFPRArray2, int n6) {
        int n7;
        int n8 = FalconSign.MKN(n5);
        int n9 = n6;
        int n10 = n9 + n8;
        int n11 = n3 + this.skoff_b00(n5);
        int n12 = n3 + this.skoff_b01(n5);
        int n13 = n3 + this.skoff_b10(n5);
        int n14 = n3 + this.skoff_b11(n5);
        int n15 = n3 + this.skoff_tree(n5);
        for (n7 = 0; n7 < n8; ++n7) {
            falconFPRArray2[n9 + n7] = this.fpr.fpr_of(sArray2[n4 + n7]);
        }
        this.fft.FFT(falconFPRArray2, n9, n5);
        FalconFPR falconFPR = this.fpr.fpr_inverse_of_q;
        System.arraycopy(falconFPRArray2, n9, falconFPRArray2, n10, n8);
        this.fft.poly_mul_fft(falconFPRArray2, n10, falconFPRArray, n12, n5);
        this.fft.poly_mulconst(falconFPRArray2, n10, this.fpr.fpr_neg(falconFPR), n5);
        this.fft.poly_mul_fft(falconFPRArray2, n9, falconFPRArray, n14, n5);
        this.fft.poly_mulconst(falconFPRArray2, n9, falconFPR, n5);
        int n16 = n10 + n8;
        int n17 = n16 + n8;
        this.ffSampling_fft(samplerZ, samplerCtx, falconFPRArray2, n16, falconFPRArray2, n17, falconFPRArray, n15, falconFPRArray2, n9, falconFPRArray2, n10, n5, falconFPRArray2, n17 + n8);
        System.arraycopy(falconFPRArray2, n16, falconFPRArray2, n9, n8);
        System.arraycopy(falconFPRArray2, n17, falconFPRArray2, n10, n8);
        this.fft.poly_mul_fft(falconFPRArray2, n16, falconFPRArray, n11, n5);
        this.fft.poly_mul_fft(falconFPRArray2, n17, falconFPRArray, n13, n5);
        this.fft.poly_add(falconFPRArray2, n16, falconFPRArray2, n17, n5);
        System.arraycopy(falconFPRArray2, n9, falconFPRArray2, n17, n8);
        this.fft.poly_mul_fft(falconFPRArray2, n17, falconFPRArray, n12, n5);
        System.arraycopy(falconFPRArray2, n16, falconFPRArray2, n9, n8);
        this.fft.poly_mul_fft(falconFPRArray2, n10, falconFPRArray, n14, n5);
        this.fft.poly_add(falconFPRArray2, n10, falconFPRArray2, n17, n5);
        this.fft.iFFT(falconFPRArray2, n9, n5);
        this.fft.iFFT(falconFPRArray2, n10, n5);
        short[] sArray3 = new short[n8];
        int n18 = 0;
        int n19 = 0;
        for (n7 = 0; n7 < n8; ++n7) {
            int n20 = (sArray2[n4 + n7] & 0xFFFF) - (int)this.fpr.fpr_rint(falconFPRArray2[n9 + n7]);
            n19 |= (n18 += n20 * n20);
            sArray3[n7] = (short)n20;
        }
        n18 |= -(n19 >>> 31);
        short[] sArray4 = new short[n8];
        for (n7 = 0; n7 < n8; ++n7) {
            sArray4[n7] = (short)(-this.fpr.fpr_rint(falconFPRArray2[n10 + n7]));
        }
        if (this.common.is_short_half(n18, sArray4, 0, n5) != 0) {
            System.arraycopy(sArray4, 0, sArray, n2, n8);
            System.arraycopy(sArray3, 0, falconFPRArray2, n6, n8);
            return 1;
        }
        return 0;
    }

    int do_sign_dyn(SamplerZ samplerZ, SamplerCtx samplerCtx, short[] sArray, int n2, byte[] byArray, int n3, byte[] byArray2, int n4, byte[] byArray3, int n5, byte[] byArray4, int n6, short[] sArray2, int n7, int n8, FalconFPR[] falconFPRArray, int n9) {
        int n10;
        int n11 = FalconSign.MKN(n8);
        int n12 = n9;
        int n13 = n12 + n11;
        int n14 = n13 + n11;
        int n15 = n14 + n11;
        this.smallints_to_fpr(falconFPRArray, n13, byArray, n3, n8);
        this.smallints_to_fpr(falconFPRArray, n12, byArray2, n4, n8);
        this.smallints_to_fpr(falconFPRArray, n15, byArray3, n5, n8);
        this.smallints_to_fpr(falconFPRArray, n14, byArray4, n6, n8);
        this.fft.FFT(falconFPRArray, n13, n8);
        this.fft.FFT(falconFPRArray, n12, n8);
        this.fft.FFT(falconFPRArray, n15, n8);
        this.fft.FFT(falconFPRArray, n14, n8);
        this.fft.poly_neg(falconFPRArray, n13, n8);
        this.fft.poly_neg(falconFPRArray, n15, n8);
        int n16 = n15 + n11;
        int n17 = n16 + n11;
        System.arraycopy(falconFPRArray, n13, falconFPRArray, n16, n11);
        this.fft.poly_mulselfadj_fft(falconFPRArray, n16, n8);
        System.arraycopy(falconFPRArray, n12, falconFPRArray, n17, n11);
        this.fft.poly_muladj_fft(falconFPRArray, n17, falconFPRArray, n14, n8);
        this.fft.poly_mulselfadj_fft(falconFPRArray, n12, n8);
        this.fft.poly_add(falconFPRArray, n12, falconFPRArray, n16, n8);
        System.arraycopy(falconFPRArray, n13, falconFPRArray, n16, n11);
        this.fft.poly_muladj_fft(falconFPRArray, n13, falconFPRArray, n15, n8);
        this.fft.poly_add(falconFPRArray, n13, falconFPRArray, n17, n8);
        this.fft.poly_mulselfadj_fft(falconFPRArray, n14, n8);
        System.arraycopy(falconFPRArray, n15, falconFPRArray, n17, n11);
        this.fft.poly_mulselfadj_fft(falconFPRArray, n17, n8);
        this.fft.poly_add(falconFPRArray, n14, falconFPRArray, n17, n8);
        int n18 = n12;
        int n19 = n13;
        int n20 = n14;
        n13 = n16;
        n16 = n13 + n11;
        n17 = n16 + n11;
        for (n10 = 0; n10 < n11; ++n10) {
            falconFPRArray[n16 + n10] = this.fpr.fpr_of(sArray2[n7 + n10]);
        }
        this.fft.FFT(falconFPRArray, n16, n8);
        FalconFPR falconFPR = this.fpr.fpr_inverse_of_q;
        System.arraycopy(falconFPRArray, n16, falconFPRArray, n17, n11);
        this.fft.poly_mul_fft(falconFPRArray, n17, falconFPRArray, n13, n8);
        this.fft.poly_mulconst(falconFPRArray, n17, this.fpr.fpr_neg(falconFPR), n8);
        this.fft.poly_mul_fft(falconFPRArray, n16, falconFPRArray, n15, n8);
        this.fft.poly_mulconst(falconFPRArray, n16, falconFPR, n8);
        System.arraycopy(falconFPRArray, n16, falconFPRArray, n15, 2 * n11);
        n16 = n20 + n11;
        n17 = n16 + n11;
        this.ffSampling_fft_dyntree(samplerZ, samplerCtx, falconFPRArray, n16, falconFPRArray, n17, falconFPRArray, n18, falconFPRArray, n19, falconFPRArray, n20, n8, n8, falconFPRArray, n17 + n11);
        n12 = n9;
        n13 = n12 + n11;
        n14 = n13 + n11;
        n15 = n14 + n11;
        System.arraycopy(falconFPRArray, n16, falconFPRArray, n15 + n11, n11 * 2);
        n16 = n15 + n11;
        n17 = n16 + n11;
        this.smallints_to_fpr(falconFPRArray, n13, byArray, n3, n8);
        this.smallints_to_fpr(falconFPRArray, n12, byArray2, n4, n8);
        this.smallints_to_fpr(falconFPRArray, n15, byArray3, n5, n8);
        this.smallints_to_fpr(falconFPRArray, n14, byArray4, n6, n8);
        this.fft.FFT(falconFPRArray, n13, n8);
        this.fft.FFT(falconFPRArray, n12, n8);
        this.fft.FFT(falconFPRArray, n15, n8);
        this.fft.FFT(falconFPRArray, n14, n8);
        this.fft.poly_neg(falconFPRArray, n13, n8);
        this.fft.poly_neg(falconFPRArray, n15, n8);
        int n21 = n17 + n11;
        int n22 = n21 + n11;
        System.arraycopy(falconFPRArray, n16, falconFPRArray, n21, n11);
        System.arraycopy(falconFPRArray, n17, falconFPRArray, n22, n11);
        this.fft.poly_mul_fft(falconFPRArray, n21, falconFPRArray, n12, n8);
        this.fft.poly_mul_fft(falconFPRArray, n22, falconFPRArray, n14, n8);
        this.fft.poly_add(falconFPRArray, n21, falconFPRArray, n22, n8);
        System.arraycopy(falconFPRArray, n16, falconFPRArray, n22, n11);
        this.fft.poly_mul_fft(falconFPRArray, n22, falconFPRArray, n13, n8);
        System.arraycopy(falconFPRArray, n21, falconFPRArray, n16, n11);
        this.fft.poly_mul_fft(falconFPRArray, n17, falconFPRArray, n15, n8);
        this.fft.poly_add(falconFPRArray, n17, falconFPRArray, n22, n8);
        this.fft.iFFT(falconFPRArray, n16, n8);
        this.fft.iFFT(falconFPRArray, n17, n8);
        short[] sArray3 = new short[n11];
        int n23 = 0;
        int n24 = 0;
        for (n10 = 0; n10 < n11; ++n10) {
            int n25 = (sArray2[n7 + n10] & 0xFFFF) - (int)this.fpr.fpr_rint(falconFPRArray[n16 + n10]);
            n24 |= (n23 += n25 * n25);
            sArray3[n10] = (short)n25;
        }
        n23 |= -(n24 >>> 31);
        short[] sArray4 = new short[n11];
        for (n10 = 0; n10 < n11; ++n10) {
            sArray4[n10] = (short)(-this.fpr.fpr_rint(falconFPRArray[n17 + n10]));
        }
        if (this.common.is_short_half(n23, sArray4, 0, n8) != 0) {
            System.arraycopy(sArray4, 0, sArray, n2, n11);
            return 1;
        }
        return 0;
    }

    void sign_tree(short[] sArray, int n2, SHAKE256 sHAKE256, FalconFPR[] falconFPRArray, int n3, short[] sArray2, int n4, int n5, FalconFPR[] falconFPRArray2, int n6) {
        SamplerCtx samplerCtx;
        SamplerCtx samplerCtx2;
        SamplerZ samplerZ;
        int n7 = n6;
        do {
            samplerCtx = new SamplerCtx();
            samplerZ = new SamplerZ();
            samplerCtx.sigma_min = this.fpr.fpr_sigma_min[n5];
            samplerCtx.p.prng_init(sHAKE256);
        } while (this.do_sign_tree(samplerZ, samplerCtx2 = samplerCtx, sArray, n2, falconFPRArray, n3, sArray2, n4, n5, falconFPRArray2, n7) == 0);
    }

    void sign_dyn(short[] sArray, int n2, SHAKE256 sHAKE256, byte[] byArray, int n3, byte[] byArray2, int n4, byte[] byArray3, int n5, byte[] byArray4, int n6, short[] sArray2, int n7, int n8, FalconFPR[] falconFPRArray, int n9) {
        SamplerCtx samplerCtx;
        SamplerCtx samplerCtx2;
        SamplerZ samplerZ;
        int n10 = n9;
        do {
            samplerCtx = new SamplerCtx();
            samplerZ = new SamplerZ();
            samplerCtx.sigma_min = this.fpr.fpr_sigma_min[n8];
            samplerCtx.p.prng_init(sHAKE256);
        } while (this.do_sign_dyn(samplerZ, samplerCtx2 = samplerCtx, sArray, n2, byArray, n3, byArray2, n4, byArray3, n5, byArray4, n6, sArray2, n7, n8, falconFPRArray, n10) == 0);
    }
}

