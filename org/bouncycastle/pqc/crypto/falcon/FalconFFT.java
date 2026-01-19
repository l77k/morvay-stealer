/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.falcon;

import org.bouncycastle.pqc.crypto.falcon.ComplexNumberWrapper;
import org.bouncycastle.pqc.crypto.falcon.FPREngine;
import org.bouncycastle.pqc.crypto.falcon.FalconFPR;

class FalconFFT {
    FPREngine fpr = new FPREngine();

    FalconFFT() {
    }

    ComplexNumberWrapper FPC_ADD(FalconFPR falconFPR, FalconFPR falconFPR2, FalconFPR falconFPR3, FalconFPR falconFPR4) {
        FalconFPR falconFPR5 = this.fpr.fpr_add(falconFPR, falconFPR3);
        FalconFPR falconFPR6 = this.fpr.fpr_add(falconFPR2, falconFPR4);
        return new ComplexNumberWrapper(falconFPR5, falconFPR6);
    }

    ComplexNumberWrapper FPC_SUB(FalconFPR falconFPR, FalconFPR falconFPR2, FalconFPR falconFPR3, FalconFPR falconFPR4) {
        FalconFPR falconFPR5 = this.fpr.fpr_sub(falconFPR, falconFPR3);
        FalconFPR falconFPR6 = this.fpr.fpr_sub(falconFPR2, falconFPR4);
        return new ComplexNumberWrapper(falconFPR5, falconFPR6);
    }

    ComplexNumberWrapper FPC_MUL(FalconFPR falconFPR, FalconFPR falconFPR2, FalconFPR falconFPR3, FalconFPR falconFPR4) {
        FalconFPR falconFPR5 = falconFPR;
        FalconFPR falconFPR6 = falconFPR2;
        FalconFPR falconFPR7 = falconFPR3;
        FalconFPR falconFPR8 = falconFPR4;
        FalconFPR falconFPR9 = this.fpr.fpr_sub(this.fpr.fpr_mul(falconFPR5, falconFPR7), this.fpr.fpr_mul(falconFPR6, falconFPR8));
        FalconFPR falconFPR10 = this.fpr.fpr_add(this.fpr.fpr_mul(falconFPR5, falconFPR8), this.fpr.fpr_mul(falconFPR6, falconFPR7));
        return new ComplexNumberWrapper(falconFPR9, falconFPR10);
    }

    ComplexNumberWrapper FPC_SQR(FalconFPR falconFPR, FalconFPR falconFPR2) {
        FalconFPR falconFPR3 = falconFPR;
        FalconFPR falconFPR4 = falconFPR2;
        FalconFPR falconFPR5 = this.fpr.fpr_sub(this.fpr.fpr_sqr(falconFPR3), this.fpr.fpr_sqr(falconFPR4));
        FalconFPR falconFPR6 = this.fpr.fpr_double(this.fpr.fpr_mul(falconFPR3, falconFPR4));
        return new ComplexNumberWrapper(falconFPR5, falconFPR6);
    }

    ComplexNumberWrapper FPC_INV(FalconFPR falconFPR, FalconFPR falconFPR2) {
        FalconFPR falconFPR3 = falconFPR;
        FalconFPR falconFPR4 = falconFPR2;
        FalconFPR falconFPR5 = this.fpr.fpr_add(this.fpr.fpr_sqr(falconFPR3), this.fpr.fpr_sqr(falconFPR4));
        falconFPR5 = this.fpr.fpr_inv(falconFPR5);
        FalconFPR falconFPR6 = this.fpr.fpr_mul(falconFPR3, falconFPR5);
        FalconFPR falconFPR7 = this.fpr.fpr_mul(this.fpr.fpr_neg(falconFPR4), falconFPR5);
        return new ComplexNumberWrapper(falconFPR6, falconFPR7);
    }

    ComplexNumberWrapper FPC_DIV(FalconFPR falconFPR, FalconFPR falconFPR2, FalconFPR falconFPR3, FalconFPR falconFPR4) {
        FalconFPR falconFPR5 = falconFPR;
        FalconFPR falconFPR6 = falconFPR2;
        FalconFPR falconFPR7 = falconFPR3;
        FalconFPR falconFPR8 = falconFPR4;
        FalconFPR falconFPR9 = this.fpr.fpr_add(this.fpr.fpr_sqr(falconFPR7), this.fpr.fpr_sqr(falconFPR8));
        falconFPR9 = this.fpr.fpr_inv(falconFPR9);
        falconFPR7 = this.fpr.fpr_mul(falconFPR7, falconFPR9);
        falconFPR8 = this.fpr.fpr_mul(this.fpr.fpr_neg(falconFPR8), falconFPR9);
        FalconFPR falconFPR10 = this.fpr.fpr_sub(this.fpr.fpr_mul(falconFPR5, falconFPR7), this.fpr.fpr_mul(falconFPR6, falconFPR8));
        FalconFPR falconFPR11 = this.fpr.fpr_add(this.fpr.fpr_mul(falconFPR5, falconFPR8), this.fpr.fpr_mul(falconFPR6, falconFPR7));
        return new ComplexNumberWrapper(falconFPR10, falconFPR11);
    }

    void FFT(FalconFPR[] falconFPRArray, int n2, int n3) {
        int n4;
        int n5 = 1 << n3;
        int n6 = n4 = n5 >> 1;
        int n7 = 1;
        int n8 = 2;
        while (n7 < n3) {
            int n9 = n6 >> 1;
            int n10 = n8 >> 1;
            int n11 = 0;
            int n12 = 0;
            while (n11 < n10) {
                int n13 = n12 + n9;
                FalconFPR falconFPR = this.fpr.fpr_gm_tab[(n8 + n11 << 1) + 0];
                FalconFPR falconFPR2 = this.fpr.fpr_gm_tab[(n8 + n11 << 1) + 1];
                for (int i2 = n12; i2 < n13; ++i2) {
                    FalconFPR falconFPR3 = falconFPRArray[n2 + i2];
                    FalconFPR falconFPR4 = falconFPRArray[n2 + i2 + n4];
                    FalconFPR falconFPR5 = falconFPRArray[n2 + i2 + n9];
                    FalconFPR falconFPR6 = falconFPRArray[n2 + i2 + n9 + n4];
                    ComplexNumberWrapper complexNumberWrapper = this.FPC_MUL(falconFPR5, falconFPR6, falconFPR, falconFPR2);
                    falconFPR5 = complexNumberWrapper.re;
                    falconFPR6 = complexNumberWrapper.im;
                    complexNumberWrapper = this.FPC_ADD(falconFPR3, falconFPR4, falconFPR5, falconFPR6);
                    falconFPRArray[n2 + i2] = complexNumberWrapper.re;
                    falconFPRArray[n2 + i2 + n4] = complexNumberWrapper.im;
                    complexNumberWrapper = this.FPC_SUB(falconFPR3, falconFPR4, falconFPR5, falconFPR6);
                    falconFPRArray[n2 + i2 + n9] = complexNumberWrapper.re;
                    falconFPRArray[n2 + i2 + n9 + n4] = complexNumberWrapper.im;
                }
                ++n11;
                n12 += n6;
            }
            n6 = n9;
            ++n7;
            n8 <<= 1;
        }
    }

    void iFFT(FalconFPR[] falconFPRArray, int n2, int n3) {
        int n4;
        int n5 = 1 << n3;
        int n6 = 1;
        int n7 = n5;
        int n8 = n5 >> 1;
        for (n4 = n3; n4 > 1; --n4) {
            int n9 = n7 >> 1;
            int n10 = n6 << 1;
            int n11 = 0;
            for (int i2 = 0; i2 < n8; i2 += n10) {
                int n12 = i2 + n6;
                FalconFPR falconFPR = this.fpr.fpr_gm_tab[(n9 + n11 << 1) + 0];
                FalconFPR falconFPR2 = this.fpr.fpr_neg(this.fpr.fpr_gm_tab[(n9 + n11 << 1) + 1]);
                for (int i3 = i2; i3 < n12; ++i3) {
                    FalconFPR falconFPR3 = falconFPRArray[n2 + i3];
                    FalconFPR falconFPR4 = falconFPRArray[n2 + i3 + n8];
                    FalconFPR falconFPR5 = falconFPRArray[n2 + i3 + n6];
                    FalconFPR falconFPR6 = falconFPRArray[n2 + i3 + n6 + n8];
                    ComplexNumberWrapper complexNumberWrapper = this.FPC_ADD(falconFPR3, falconFPR4, falconFPR5, falconFPR6);
                    falconFPRArray[n2 + i3] = complexNumberWrapper.re;
                    falconFPRArray[n2 + i3 + n8] = complexNumberWrapper.im;
                    complexNumberWrapper = this.FPC_SUB(falconFPR3, falconFPR4, falconFPR5, falconFPR6);
                    falconFPR3 = complexNumberWrapper.re;
                    falconFPR4 = complexNumberWrapper.im;
                    complexNumberWrapper = this.FPC_MUL(falconFPR3, falconFPR4, falconFPR, falconFPR2);
                    falconFPRArray[n2 + i3 + n6] = complexNumberWrapper.re;
                    falconFPRArray[n2 + i3 + n6 + n8] = complexNumberWrapper.im;
                }
                ++n11;
            }
            n6 = n10;
            n7 = n9;
        }
        if (n3 > 0) {
            FalconFPR falconFPR = this.fpr.fpr_p2_tab[n3];
            for (n4 = 0; n4 < n5; ++n4) {
                falconFPRArray[n2 + n4] = this.fpr.fpr_mul(falconFPRArray[n2 + n4], falconFPR);
            }
        }
    }

    void poly_add(FalconFPR[] falconFPRArray, int n2, FalconFPR[] falconFPRArray2, int n3, int n4) {
        int n5 = 1 << n4;
        for (int i2 = 0; i2 < n5; ++i2) {
            falconFPRArray[n2 + i2] = this.fpr.fpr_add(falconFPRArray[n2 + i2], falconFPRArray2[n3 + i2]);
        }
    }

    void poly_sub(FalconFPR[] falconFPRArray, int n2, FalconFPR[] falconFPRArray2, int n3, int n4) {
        int n5 = 1 << n4;
        for (int i2 = 0; i2 < n5; ++i2) {
            falconFPRArray[n2 + i2] = this.fpr.fpr_sub(falconFPRArray[n2 + i2], falconFPRArray2[n3 + i2]);
        }
    }

    void poly_neg(FalconFPR[] falconFPRArray, int n2, int n3) {
        int n4 = 1 << n3;
        for (int i2 = 0; i2 < n4; ++i2) {
            falconFPRArray[n2 + i2] = this.fpr.fpr_neg(falconFPRArray[n2 + i2]);
        }
    }

    void poly_adj_fft(FalconFPR[] falconFPRArray, int n2, int n3) {
        int n4 = 1 << n3;
        for (int i2 = n4 >> 1; i2 < n4; ++i2) {
            falconFPRArray[n2 + i2] = this.fpr.fpr_neg(falconFPRArray[n2 + i2]);
        }
    }

    void poly_mul_fft(FalconFPR[] falconFPRArray, int n2, FalconFPR[] falconFPRArray2, int n3, int n4) {
        int n5 = 1 << n4;
        int n6 = n5 >> 1;
        for (int i2 = 0; i2 < n6; ++i2) {
            FalconFPR falconFPR = falconFPRArray[n2 + i2];
            FalconFPR falconFPR2 = falconFPRArray[n2 + i2 + n6];
            FalconFPR falconFPR3 = falconFPRArray2[n3 + i2];
            FalconFPR falconFPR4 = falconFPRArray2[n3 + i2 + n6];
            ComplexNumberWrapper complexNumberWrapper = this.FPC_MUL(falconFPR, falconFPR2, falconFPR3, falconFPR4);
            falconFPRArray[n2 + i2] = complexNumberWrapper.re;
            falconFPRArray[n2 + i2 + n6] = complexNumberWrapper.im;
        }
    }

    void poly_muladj_fft(FalconFPR[] falconFPRArray, int n2, FalconFPR[] falconFPRArray2, int n3, int n4) {
        int n5 = 1 << n4;
        int n6 = n5 >> 1;
        for (int i2 = 0; i2 < n6; ++i2) {
            FalconFPR falconFPR = falconFPRArray[n2 + i2];
            FalconFPR falconFPR2 = falconFPRArray[n2 + i2 + n6];
            FalconFPR falconFPR3 = falconFPRArray2[n3 + i2];
            FalconFPR falconFPR4 = this.fpr.fpr_neg(falconFPRArray2[n3 + i2 + n6]);
            ComplexNumberWrapper complexNumberWrapper = this.FPC_MUL(falconFPR, falconFPR2, falconFPR3, falconFPR4);
            falconFPRArray[n2 + i2] = complexNumberWrapper.re;
            falconFPRArray[n2 + i2 + n6] = complexNumberWrapper.im;
        }
    }

    void poly_mulselfadj_fft(FalconFPR[] falconFPRArray, int n2, int n3) {
        int n4 = 1 << n3;
        int n5 = n4 >> 1;
        for (int i2 = 0; i2 < n5; ++i2) {
            FalconFPR falconFPR = falconFPRArray[n2 + i2];
            FalconFPR falconFPR2 = falconFPRArray[n2 + i2 + n5];
            falconFPRArray[n2 + i2] = this.fpr.fpr_add(this.fpr.fpr_sqr(falconFPR), this.fpr.fpr_sqr(falconFPR2));
            falconFPRArray[n2 + i2 + n5] = this.fpr.fpr_zero;
        }
    }

    void poly_mulconst(FalconFPR[] falconFPRArray, int n2, FalconFPR falconFPR, int n3) {
        int n4 = 1 << n3;
        for (int i2 = 0; i2 < n4; ++i2) {
            falconFPRArray[n2 + i2] = this.fpr.fpr_mul(falconFPRArray[n2 + i2], falconFPR);
        }
    }

    void poly_div_fft(FalconFPR[] falconFPRArray, int n2, FalconFPR[] falconFPRArray2, int n3, int n4) {
        int n5 = 1 << n4;
        int n6 = n5 >> 1;
        for (int i2 = 0; i2 < n6; ++i2) {
            FalconFPR falconFPR = falconFPRArray[n2 + i2];
            FalconFPR falconFPR2 = falconFPRArray[n2 + i2 + n6];
            FalconFPR falconFPR3 = falconFPRArray2[n3 + i2];
            FalconFPR falconFPR4 = falconFPRArray2[n3 + i2 + n6];
            ComplexNumberWrapper complexNumberWrapper = this.FPC_DIV(falconFPR, falconFPR2, falconFPR3, falconFPR4);
            falconFPRArray[n2 + i2] = complexNumberWrapper.re;
            falconFPRArray[n2 + i2 + n6] = complexNumberWrapper.im;
        }
    }

    void poly_invnorm2_fft(FalconFPR[] falconFPRArray, int n2, FalconFPR[] falconFPRArray2, int n3, FalconFPR[] falconFPRArray3, int n4, int n5) {
        int n6 = 1 << n5;
        int n7 = n6 >> 1;
        for (int i2 = 0; i2 < n7; ++i2) {
            FalconFPR falconFPR = falconFPRArray2[n3 + i2];
            FalconFPR falconFPR2 = falconFPRArray2[n3 + i2 + n7];
            FalconFPR falconFPR3 = falconFPRArray3[n4 + i2];
            FalconFPR falconFPR4 = falconFPRArray3[n4 + i2 + n7];
            falconFPRArray[n2 + i2] = this.fpr.fpr_inv(this.fpr.fpr_add(this.fpr.fpr_add(this.fpr.fpr_sqr(falconFPR), this.fpr.fpr_sqr(falconFPR2)), this.fpr.fpr_add(this.fpr.fpr_sqr(falconFPR3), this.fpr.fpr_sqr(falconFPR4))));
        }
    }

    void poly_add_muladj_fft(FalconFPR[] falconFPRArray, int n2, FalconFPR[] falconFPRArray2, int n3, FalconFPR[] falconFPRArray3, int n4, FalconFPR[] falconFPRArray4, int n5, FalconFPR[] falconFPRArray5, int n6, int n7) {
        int n8 = 1 << n7;
        int n9 = n8 >> 1;
        for (int i2 = 0; i2 < n9; ++i2) {
            FalconFPR falconFPR = falconFPRArray2[n3 + i2];
            FalconFPR falconFPR2 = falconFPRArray2[n3 + i2 + n9];
            FalconFPR falconFPR3 = falconFPRArray3[n4 + i2];
            FalconFPR falconFPR4 = falconFPRArray3[n4 + i2 + n9];
            FalconFPR falconFPR5 = falconFPRArray4[n5 + i2];
            FalconFPR falconFPR6 = falconFPRArray4[n5 + i2 + n9];
            FalconFPR falconFPR7 = falconFPRArray5[n6 + i2];
            FalconFPR falconFPR8 = falconFPRArray5[n6 + i2 + n9];
            ComplexNumberWrapper complexNumberWrapper = this.FPC_MUL(falconFPR, falconFPR2, falconFPR5, this.fpr.fpr_neg(falconFPR6));
            FalconFPR falconFPR9 = complexNumberWrapper.re;
            FalconFPR falconFPR10 = complexNumberWrapper.im;
            complexNumberWrapper = this.FPC_MUL(falconFPR3, falconFPR4, falconFPR7, this.fpr.fpr_neg(falconFPR8));
            FalconFPR falconFPR11 = complexNumberWrapper.re;
            FalconFPR falconFPR12 = complexNumberWrapper.im;
            falconFPRArray[n2 + i2] = this.fpr.fpr_add(falconFPR9, falconFPR11);
            falconFPRArray[n2 + i2 + n9] = this.fpr.fpr_add(falconFPR10, falconFPR12);
        }
    }

    void poly_mul_autoadj_fft(FalconFPR[] falconFPRArray, int n2, FalconFPR[] falconFPRArray2, int n3, int n4) {
        int n5 = 1 << n4;
        int n6 = n5 >> 1;
        for (int i2 = 0; i2 < n6; ++i2) {
            falconFPRArray[n2 + i2] = this.fpr.fpr_mul(falconFPRArray[n2 + i2], falconFPRArray2[n3 + i2]);
            falconFPRArray[n2 + i2 + n6] = this.fpr.fpr_mul(falconFPRArray[n2 + i2 + n6], falconFPRArray2[n3 + i2]);
        }
    }

    void poly_div_autoadj_fft(FalconFPR[] falconFPRArray, int n2, FalconFPR[] falconFPRArray2, int n3, int n4) {
        int n5 = 1 << n4;
        int n6 = n5 >> 1;
        for (int i2 = 0; i2 < n6; ++i2) {
            FalconFPR falconFPR = this.fpr.fpr_inv(falconFPRArray2[n3 + i2]);
            falconFPRArray[n2 + i2] = this.fpr.fpr_mul(falconFPRArray[n2 + i2], falconFPR);
            falconFPRArray[n2 + i2 + n6] = this.fpr.fpr_mul(falconFPRArray[n2 + i2 + n6], falconFPR);
        }
    }

    void poly_LDL_fft(FalconFPR[] falconFPRArray, int n2, FalconFPR[] falconFPRArray2, int n3, FalconFPR[] falconFPRArray3, int n4, int n5) {
        int n6 = 1 << n5;
        int n7 = n6 >> 1;
        for (int i2 = 0; i2 < n7; ++i2) {
            FalconFPR falconFPR = falconFPRArray[n2 + i2];
            FalconFPR falconFPR2 = falconFPRArray[n2 + i2 + n7];
            FalconFPR falconFPR3 = falconFPRArray2[n3 + i2];
            FalconFPR falconFPR4 = falconFPRArray2[n3 + i2 + n7];
            FalconFPR falconFPR5 = falconFPRArray3[n4 + i2];
            FalconFPR falconFPR6 = falconFPRArray3[n4 + i2 + n7];
            ComplexNumberWrapper complexNumberWrapper = this.FPC_DIV(falconFPR3, falconFPR4, falconFPR, falconFPR2);
            FalconFPR falconFPR7 = complexNumberWrapper.re;
            FalconFPR falconFPR8 = complexNumberWrapper.im;
            complexNumberWrapper = this.FPC_MUL(falconFPR7, falconFPR8, falconFPR3, this.fpr.fpr_neg(falconFPR4));
            falconFPR3 = complexNumberWrapper.re;
            falconFPR4 = complexNumberWrapper.im;
            complexNumberWrapper = this.FPC_SUB(falconFPR5, falconFPR6, falconFPR3, falconFPR4);
            falconFPRArray3[n4 + i2] = complexNumberWrapper.re;
            falconFPRArray3[n4 + i2 + n7] = complexNumberWrapper.im;
            falconFPRArray2[n3 + i2] = falconFPR7;
            falconFPRArray2[n3 + i2 + n7] = this.fpr.fpr_neg(falconFPR8);
        }
    }

    void poly_LDLmv_fft(FalconFPR[] falconFPRArray, int n2, FalconFPR[] falconFPRArray2, int n3, FalconFPR[] falconFPRArray3, int n4, FalconFPR[] falconFPRArray4, int n5, FalconFPR[] falconFPRArray5, int n6, int n7) {
        int n8 = 1 << n7;
        int n9 = n8 >> 1;
        for (int i2 = 0; i2 < n9; ++i2) {
            FalconFPR falconFPR = falconFPRArray3[n4 + i2];
            FalconFPR falconFPR2 = falconFPRArray3[n4 + i2 + n9];
            FalconFPR falconFPR3 = falconFPRArray4[n5 + i2];
            FalconFPR falconFPR4 = falconFPRArray4[n5 + i2 + n9];
            FalconFPR falconFPR5 = falconFPRArray5[n6 + i2];
            FalconFPR falconFPR6 = falconFPRArray5[n6 + i2 + n9];
            ComplexNumberWrapper complexNumberWrapper = this.FPC_DIV(falconFPR3, falconFPR4, falconFPR, falconFPR2);
            FalconFPR falconFPR7 = complexNumberWrapper.re;
            FalconFPR falconFPR8 = complexNumberWrapper.im;
            complexNumberWrapper = this.FPC_MUL(falconFPR7, falconFPR8, falconFPR3, this.fpr.fpr_neg(falconFPR4));
            falconFPR3 = complexNumberWrapper.re;
            falconFPR4 = complexNumberWrapper.im;
            complexNumberWrapper = this.FPC_SUB(falconFPR5, falconFPR6, falconFPR3, falconFPR4);
            falconFPRArray[n2 + i2] = complexNumberWrapper.re;
            falconFPRArray[n2 + i2 + n9] = complexNumberWrapper.im;
            falconFPRArray2[n3 + i2] = falconFPR7;
            falconFPRArray2[n3 + i2 + n9] = this.fpr.fpr_neg(falconFPR8);
        }
    }

    void poly_split_fft(FalconFPR[] falconFPRArray, int n2, FalconFPR[] falconFPRArray2, int n3, FalconFPR[] falconFPRArray3, int n4, int n5) {
        int n6 = 1 << n5;
        int n7 = n6 >> 1;
        int n8 = n7 >> 1;
        falconFPRArray[n2 + 0] = falconFPRArray3[n4 + 0];
        falconFPRArray2[n3 + 0] = falconFPRArray3[n4 + n7];
        for (int i2 = 0; i2 < n8; ++i2) {
            FalconFPR falconFPR = falconFPRArray3[n4 + (i2 << 1) + 0];
            FalconFPR falconFPR2 = falconFPRArray3[n4 + (i2 << 1) + 0 + n7];
            FalconFPR falconFPR3 = falconFPRArray3[n4 + (i2 << 1) + 1];
            FalconFPR falconFPR4 = falconFPRArray3[n4 + (i2 << 1) + 1 + n7];
            ComplexNumberWrapper complexNumberWrapper = this.FPC_ADD(falconFPR, falconFPR2, falconFPR3, falconFPR4);
            FalconFPR falconFPR5 = complexNumberWrapper.re;
            FalconFPR falconFPR6 = complexNumberWrapper.im;
            falconFPRArray[n2 + i2] = this.fpr.fpr_half(falconFPR5);
            falconFPRArray[n2 + i2 + n8] = this.fpr.fpr_half(falconFPR6);
            complexNumberWrapper = this.FPC_SUB(falconFPR, falconFPR2, falconFPR3, falconFPR4);
            falconFPR5 = complexNumberWrapper.re;
            falconFPR6 = complexNumberWrapper.im;
            complexNumberWrapper = this.FPC_MUL(falconFPR5, falconFPR6, this.fpr.fpr_gm_tab[(i2 + n7 << 1) + 0], this.fpr.fpr_neg(this.fpr.fpr_gm_tab[(i2 + n7 << 1) + 1]));
            falconFPR5 = complexNumberWrapper.re;
            falconFPR6 = complexNumberWrapper.im;
            falconFPRArray2[n3 + i2] = this.fpr.fpr_half(falconFPR5);
            falconFPRArray2[n3 + i2 + n8] = this.fpr.fpr_half(falconFPR6);
        }
    }

    void poly_merge_fft(FalconFPR[] falconFPRArray, int n2, FalconFPR[] falconFPRArray2, int n3, FalconFPR[] falconFPRArray3, int n4, int n5) {
        int n6 = 1 << n5;
        int n7 = n6 >> 1;
        int n8 = n7 >> 1;
        falconFPRArray[n2 + 0] = falconFPRArray2[n3 + 0];
        falconFPRArray[n2 + n7] = falconFPRArray3[n4 + 0];
        for (int i2 = 0; i2 < n8; ++i2) {
            FalconFPR falconFPR = falconFPRArray2[n3 + i2];
            FalconFPR falconFPR2 = falconFPRArray2[n3 + i2 + n8];
            ComplexNumberWrapper complexNumberWrapper = this.FPC_MUL(falconFPRArray3[n4 + i2], falconFPRArray3[n4 + i2 + n8], this.fpr.fpr_gm_tab[(i2 + n7 << 1) + 0], this.fpr.fpr_gm_tab[(i2 + n7 << 1) + 1]);
            FalconFPR falconFPR3 = complexNumberWrapper.re;
            FalconFPR falconFPR4 = complexNumberWrapper.im;
            complexNumberWrapper = this.FPC_ADD(falconFPR, falconFPR2, falconFPR3, falconFPR4);
            FalconFPR falconFPR5 = complexNumberWrapper.re;
            FalconFPR falconFPR6 = complexNumberWrapper.im;
            falconFPRArray[n2 + (i2 << 1) + 0] = falconFPR5;
            falconFPRArray[n2 + (i2 << 1) + 0 + n7] = falconFPR6;
            complexNumberWrapper = this.FPC_SUB(falconFPR, falconFPR2, falconFPR3, falconFPR4);
            falconFPR5 = complexNumberWrapper.re;
            falconFPR6 = complexNumberWrapper.im;
            falconFPRArray[n2 + (i2 << 1) + 1] = falconFPR5;
            falconFPRArray[n2 + (i2 << 1) + 1 + n7] = falconFPR6;
        }
    }
}

