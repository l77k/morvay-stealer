/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.gemss;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.crypto.digests.SHAKEDigest;
import org.bouncycastle.pqc.crypto.gemss.GeMSSUtils;
import org.bouncycastle.pqc.crypto.gemss.Mul_GF2x;
import org.bouncycastle.pqc.crypto.gemss.Pointer;
import org.bouncycastle.pqc.crypto.gemss.PointerUnion;
import org.bouncycastle.pqc.crypto.gemss.Rem_GF2n;
import org.bouncycastle.pqc.crypto.gemss.SecretKeyHFE;
import org.bouncycastle.util.Pack;

class GeMSSEngine {
    private SecureRandom random;
    final int HFEn;
    final int HFEv;
    final int HFEDELTA;
    final int NB_ITE;
    final int HFEDeg;
    final int HFEDegI;
    final int HFEDegJ;
    final int HFEnv;
    final int HFEm;
    final int NB_BITS_UINT = 64;
    final int HFEnq;
    final int HFEnr;
    int HFE_odd_degree;
    int NB_WORD_GFqn;
    int NB_WORD_GF2nv;
    int NB_MONOMIAL_VINEGAR;
    int NB_MONOMIAL_PK;
    final int HFEnvq;
    final int HFEnvr;
    int LTRIANGULAR_NV_SIZE;
    final int LTRIANGULAR_N_SIZE;
    final int SIZE_SEED_SK;
    final int NB_WORD_MUL;
    int NB_WORD_MMUL;
    int MQv_GFqn_SIZE;
    final boolean ENABLED_REMOVE_ODD_DEGREE;
    final int MATRIXnv_SIZE;
    final int HFEmq;
    final int HFEmr;
    int NB_WORD_GF2m;
    final int HFEvq;
    final int HFEvr;
    final int NB_WORD_GFqv;
    final int HFEmq8;
    final int HFEmr8;
    final int NB_BYTES_GFqm;
    final int ACCESS_last_equations8;
    final int NB_BYTES_EQUATION;
    final int HFENr8;
    final int NB_WORD_UNCOMP_EQ;
    final int HFENr8c;
    final int LOST_BITS;
    final int NB_WORD_GF2nvm;
    final int SIZE_SIGN_UNCOMPRESSED;
    final int SIZE_DIGEST;
    final int SIZE_DIGEST_UINT;
    final int HFEnvr8;
    final int NB_BYTES_GFqnv;
    final int VAL_BITS_M;
    final long MASK_GF2m;
    final int LEN_UNROLLED_64 = 4;
    int NB_COEFS_HFEPOLY;
    int NB_UINT_HFEVPOLY;
    final int MATRIXn_SIZE;
    final long MASK_GF2n;
    final int NB_BYTES_GFqn;
    private int buffer;
    final int SIZE_ROW;
    final int ShakeBitStrength;
    final int Sha3BitStrength;
    SHA3Digest sha3Digest;
    final int MLv_GFqn_SIZE;
    int II;
    int POW_II;
    int KP;
    int KX;
    int HFEn_1rightmost;
    int HFEn1h_rightmost;
    Mul_GF2x mul;
    Rem_GF2n rem;
    Pointer Buffer_NB_WORD_MUL;
    Pointer Buffer_NB_WORD_GFqn;

    public GeMSSEngine(int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        int n10;
        this.HFEn = n3;
        this.HFEv = n4;
        this.HFEDELTA = n5;
        this.NB_ITE = n6;
        this.HFEDeg = n7;
        this.HFEDegI = n8;
        this.HFEDegJ = n9;
        this.NB_BYTES_GFqn = (n3 >>> 3) + ((n3 & 7) != 0 ? 1 : 0);
        this.SIZE_ROW = n8 + 1;
        this.HFEnv = n3 + n4;
        this.HFEnq = n3 >>> 6;
        this.HFEnr = n3 & 0x3F;
        this.HFEnvq = this.HFEnv >>> 6;
        this.HFEnvr = this.HFEnv & 0x3F;
        this.SIZE_SEED_SK = n2 >>> 3;
        this.NB_WORD_MUL = (n3 - 1 << 1 >>> 6) + 1;
        switch (this.NB_WORD_MUL) {
            case 6: {
                this.mul = new Mul_GF2x.Mul6();
                break;
            }
            case 9: {
                this.mul = new Mul_GF2x.Mul9();
                break;
            }
            case 12: {
                this.mul = new Mul_GF2x.Mul12();
                break;
            }
            case 13: {
                this.mul = new Mul_GF2x.Mul13();
                break;
            }
            case 17: {
                this.mul = new Mul_GF2x.Mul17();
            }
        }
        int n11 = n3 & 0x3F;
        int n12 = 64 - n11;
        this.HFEm = n3 - n5;
        this.HFEmq = this.HFEm >>> 6;
        this.HFEmr = this.HFEm & 0x3F;
        this.HFEvq = n4 >>> 6;
        this.HFEvr = n4 & 0x3F;
        this.NB_WORD_GFqv = this.HFEvr != 0 ? this.HFEvq + 1 : this.HFEvq;
        this.HFEmq8 = this.HFEm >>> 3;
        this.HFEmr8 = this.HFEm & 7;
        this.NB_BYTES_GFqm = this.HFEmq8 + (this.HFEmr8 != 0 ? 1 : 0);
        this.NB_WORD_UNCOMP_EQ = (this.HFEnvq * (this.HFEnvq + 1) >>> 1) * 64 + (this.HFEnvq + 1) * this.HFEnvr;
        this.HFEnvr8 = this.HFEnv & 7;
        this.NB_BYTES_GFqnv = (this.HFEnv >>> 3) + (this.HFEnvr8 != 0 ? 1 : 0);
        this.VAL_BITS_M = Math.min(n5 + n4, 8 - this.HFEmr8);
        this.MASK_GF2m = GeMSSUtils.maskUINT(this.HFEmr);
        this.MASK_GF2n = GeMSSUtils.maskUINT(this.HFEnr);
        this.NB_WORD_GFqn = this.HFEnq + (this.HFEnr != 0 ? 1 : 0);
        this.LTRIANGULAR_N_SIZE = (this.HFEnq * (this.HFEnq + 1) >>> 1) * 64 + this.NB_WORD_GFqn * this.HFEnr;
        this.MATRIXn_SIZE = n3 * this.NB_WORD_GFqn;
        this.NB_WORD_GF2nv = this.HFEnvq + (this.HFEnvr != 0 ? 1 : 0);
        this.MATRIXnv_SIZE = this.HFEnv * this.NB_WORD_GF2nv;
        this.LTRIANGULAR_NV_SIZE = (this.HFEnvq * (this.HFEnvq + 1) >>> 1) * 64 + this.NB_WORD_GF2nv * this.HFEnvr;
        this.NB_MONOMIAL_VINEGAR = (n4 * (n4 + 1) >>> 1) + 1;
        this.NB_MONOMIAL_PK = (this.HFEnv * (this.HFEnv + 1) >>> 1) + 1;
        this.MQv_GFqn_SIZE = this.NB_MONOMIAL_VINEGAR * this.NB_WORD_GFqn;
        this.ACCESS_last_equations8 = this.NB_MONOMIAL_PK * this.HFEmq8;
        this.NB_BYTES_EQUATION = this.NB_MONOMIAL_PK + 7 >>> 3;
        this.HFENr8 = this.NB_MONOMIAL_PK & 7;
        this.HFENr8c = 8 - this.HFENr8 & 7;
        this.LOST_BITS = (this.HFEmr8 - 1) * this.HFENr8c;
        this.NB_WORD_MMUL = (n3 - 1 << 1 >>> 6) + 1;
        int n13 = 0;
        int n14 = 0;
        int n15 = 0;
        int n16 = 0;
        switch (n3) {
            case 174: {
                n10 = 13;
                break;
            }
            case 175: {
                n10 = 16;
                break;
            }
            case 177: {
                n10 = 8;
                break;
            }
            case 178: {
                n10 = 31;
                break;
            }
            case 265: {
                n10 = 42;
                break;
            }
            case 266: {
                n10 = 47;
                break;
            }
            case 268: {
                n10 = 25;
                break;
            }
            case 270: {
                n10 = 53;
                break;
            }
            case 271: {
                n10 = 58;
                break;
            }
            case 354: {
                n10 = 99;
                break;
            }
            case 358: {
                n10 = 57;
                break;
            }
            case 364: {
                n10 = 9;
                break;
            }
            case 366: {
                n10 = 29;
                break;
            }
            case 402: {
                n10 = 171;
                break;
            }
            case 537: {
                n10 = 10;
                n14 = 2;
                n13 = 1;
                break;
            }
            case 544: {
                n10 = 128;
                n14 = 3;
                n13 = 1;
                break;
            }
            default: {
                throw new IllegalArgumentException("error: need to add support for HFEn=" + n3);
            }
        }
        if (n14 != 0) {
            n15 = 64 - n13;
            n16 = 64 - n14;
        }
        int n17 = 64 - (n10 & 0x3F);
        if ((n7 & 1) == 0) {
            this.ENABLED_REMOVE_ODD_DEGREE = true;
            this.HFE_odd_degree = (1 << n8) + 1;
            if ((n7 & 1) != 0) {
                throw new IllegalArgumentException("HFEDeg is odd, so to remove the leading term would decrease the degree.");
            }
            if (this.HFE_odd_degree > n7) {
                throw new IllegalArgumentException("It is useless to remove 0 term.");
            }
            if (this.HFE_odd_degree <= 1) {
                throw new IllegalArgumentException("The case where the term X^3 is removing is not implemented.");
            }
            this.NB_COEFS_HFEPOLY = 2 + n9 + (n8 * (n8 - 1) >>> 1) + n8;
        } else {
            this.ENABLED_REMOVE_ODD_DEGREE = false;
            this.NB_COEFS_HFEPOLY = 2 + n9 + (n8 * (n8 + 1) >>> 1);
        }
        this.NB_WORD_GF2m = this.HFEmq + (this.HFEmr != 0 ? 1 : 0);
        this.NB_WORD_GF2nvm = this.NB_WORD_GF2nv - this.NB_WORD_GF2m + (this.HFEmr != 0 ? 1 : 0);
        this.SIZE_SIGN_UNCOMPRESSED = this.NB_WORD_GF2nv + (n6 - 1) * this.NB_WORD_GF2nvm;
        if (n2 <= 128) {
            this.SIZE_DIGEST = 32;
            this.SIZE_DIGEST_UINT = 4;
            this.ShakeBitStrength = 128;
            this.Sha3BitStrength = 256;
        } else if (n2 <= 192) {
            this.SIZE_DIGEST = 48;
            this.SIZE_DIGEST_UINT = 6;
            this.ShakeBitStrength = 256;
            this.Sha3BitStrength = 384;
        } else {
            this.SIZE_DIGEST = 64;
            this.SIZE_DIGEST_UINT = 8;
            this.ShakeBitStrength = 256;
            this.Sha3BitStrength = 512;
        }
        this.sha3Digest = new SHA3Digest(this.Sha3BitStrength);
        this.NB_UINT_HFEVPOLY = (this.NB_COEFS_HFEPOLY + (this.NB_MONOMIAL_VINEGAR - 1) + (n8 + 1) * n4) * this.NB_WORD_GFqn;
        this.MLv_GFqn_SIZE = (n4 + 1) * this.NB_WORD_GFqn;
        if (n7 <= 34 || n3 > 196 && n7 < 256) {
            this.II = n7 == 17 ? 4 : 6;
            this.POW_II = 1 << this.II;
            this.KP = (n7 >>> this.II) + (n7 % this.POW_II != 0 ? 1 : 0);
            this.KX = n7 - this.KP;
        }
        if (n14 != 0) {
            this.rem = n3 == 544 && n10 == 128 ? new Rem_GF2n.REM544_PENTANOMIAL_K3_IS_128_GF2X(n13, n14, n11, n12, n15, n16, this.MASK_GF2n) : new Rem_GF2n.REM544_PENTANOMIAL_GF2X(n13, n14, n10, n11, n12, n15, n16, n17, this.MASK_GF2n);
        } else if (n3 > 256 && n3 < 289 && n10 > 32 && n10 < 64) {
            this.rem = new Rem_GF2n.REM288_SPECIALIZED_TRINOMIAL_GF2X(n10, n11, n12, n17, this.MASK_GF2n);
        } else if (n3 == 354) {
            this.rem = new Rem_GF2n.REM384_SPECIALIZED_TRINOMIAL_GF2X(n10, n11, n12, n17, this.MASK_GF2n);
        } else if (n3 == 358) {
            this.rem = new Rem_GF2n.REM384_SPECIALIZED358_TRINOMIAL_GF2X(n10, n11, n12, n17, this.MASK_GF2n);
        } else if (n3 == 402) {
            this.rem = new Rem_GF2n.REM402_SPECIALIZED_TRINOMIAL_GF2X(n10, n11, n12, n17, this.MASK_GF2n);
        } else {
            switch (this.NB_WORD_MUL) {
                case 6: {
                    this.rem = new Rem_GF2n.REM192_SPECIALIZED_TRINOMIAL_GF2X(n10, n11, n12, n17, this.MASK_GF2n);
                    break;
                }
                case 9: {
                    this.rem = new Rem_GF2n.REM288_SPECIALIZED_TRINOMIAL_GF2X(n10, n11, n12, n17, this.MASK_GF2n);
                    break;
                }
                case 12: {
                    this.rem = new Rem_GF2n.REM384_TRINOMIAL_GF2X(n10, n11, n12, n17, this.MASK_GF2n);
                }
            }
        }
        this.Buffer_NB_WORD_MUL = new Pointer(this.NB_WORD_MUL);
        this.Buffer_NB_WORD_GFqn = new Pointer(this.NB_WORD_GFqn);
        this.HFEn_1rightmost = 31;
        int n18 = n3 - 1;
        while (n18 >>> this.HFEn_1rightmost == 0) {
            --this.HFEn_1rightmost;
        }
        n18 = n3 + 1 >>> 1;
        this.HFEn1h_rightmost = 31;
        while (n18 >>> this.HFEn1h_rightmost == 0) {
            --this.HFEn1h_rightmost;
        }
        --this.HFEn1h_rightmost;
    }

    void genSecretMQS_gf2_opt(Pointer pointer, Pointer pointer2) {
        int n2;
        int n3;
        int n4;
        Pointer pointer3 = new Pointer(this.NB_WORD_GFqn);
        Pointer pointer4 = new Pointer((this.HFEDegI + 1) * (this.HFEv + 1) * this.NB_WORD_GFqn);
        Pointer pointer5 = new Pointer(pointer2, this.MQv_GFqn_SIZE);
        for (n4 = 0; n4 <= this.HFEDegI; ++n4) {
            for (n3 = 0; n3 <= this.HFEv; ++n3) {
                pointer4.copyFrom((n3 * (this.HFEDegI + 1) + n4) * this.NB_WORD_GFqn, pointer5, 0, this.NB_WORD_GFqn);
                pointer5.move(this.NB_WORD_GFqn);
            }
            pointer5.move(n4 * this.NB_WORD_GFqn);
        }
        Pointer pointer6 = new Pointer(this.SIZE_ROW * (this.HFEn - 1) * this.NB_WORD_GFqn);
        for (n4 = 1; n4 < this.HFEn; ++n4) {
            pointer6.set(n4 >>> 6, 1L << (n4 & 0x3F));
            for (int i2 = 0; i2 < this.HFEDegI; ++i2) {
                this.sqr_gf2n(pointer6, this.NB_WORD_GFqn, pointer6, 0);
                pointer6.move(this.NB_WORD_GFqn);
            }
            pointer6.move(this.NB_WORD_GFqn);
        }
        pointer6.indexReset();
        pointer.copyFrom(pointer2, this.NB_WORD_GFqn);
        pointer2.move(this.MQv_GFqn_SIZE);
        pointer.move(this.NB_WORD_GFqn);
        Pointer pointer7 = new Pointer(this.HFEDegI * this.HFEn * this.NB_WORD_GFqn);
        this.special_buffer(pointer7, pointer2, pointer6);
        Pointer pointer8 = new Pointer(pointer7);
        Pointer pointer9 = new Pointer(pointer7);
        pointer.copyFrom(pointer9, this.NB_WORD_GFqn);
        pointer9.move(this.NB_WORD_GFqn);
        pointer.setXorMatrix_NoMove(pointer9, this.NB_WORD_GFqn, this.HFEDegI - 1);
        pointer5.changeIndex(pointer4);
        pointer.setXorMatrix(pointer5, this.NB_WORD_GFqn, this.HFEDegI + 1);
        Pointer pointer10 = new Pointer(pointer6, this.NB_WORD_GFqn);
        for (n2 = 1; n2 < this.HFEn; ++n2) {
            this.dotProduct_gf2n(pointer, pointer10, pointer8, this.HFEDegI);
            pointer10.move(this.SIZE_ROW * this.NB_WORD_GFqn);
            pointer.setXorMatrix(pointer9, this.NB_WORD_GFqn, this.HFEDegI);
        }
        while (n2 < this.HFEnv) {
            pointer.copyFrom(pointer5, this.NB_WORD_GFqn);
            pointer5.move(this.NB_WORD_GFqn);
            pointer.setXorMatrix(pointer5, this.NB_WORD_GFqn, this.HFEDegI);
            ++n2;
        }
        Pointer pointer11 = new Pointer(pointer6, this.NB_WORD_GFqn);
        Pointer pointer12 = new Pointer(this.NB_WORD_MUL);
        for (n3 = 1; n3 < this.HFEn; ++n3) {
            pointer8.move(this.HFEDegI * this.NB_WORD_GFqn);
            pointer10.changeIndex(pointer11);
            pointer9.changeIndex(pointer8);
            this.mul.mul_gf2x(this.Buffer_NB_WORD_MUL, pointer4, new Pointer(pointer10, -this.NB_WORD_GFqn));
            for (n4 = 1; n4 <= this.HFEDegI; ++n4) {
                pointer3.setRangeFromXor(0, pointer9, 0, pointer4, n4 * this.NB_WORD_GFqn, this.NB_WORD_GFqn);
                this.mul_xorrange(this.Buffer_NB_WORD_MUL, pointer3, pointer10);
                pointer9.move(this.NB_WORD_GFqn);
                pointer10.move(this.NB_WORD_GFqn);
            }
            pointer10.move(this.NB_WORD_GFqn);
            this.rem_gf2n(pointer, 0, this.Buffer_NB_WORD_MUL);
            pointer.move(this.NB_WORD_GFqn);
            for (n2 = n3 + 1; n2 < this.HFEn; ++n2) {
                int n5 = pointer10.getIndex();
                int n6 = pointer8.getIndex();
                int n7 = pointer11.getIndex();
                int n8 = pointer9.getIndex();
                this.mul_move(pointer12, pointer10, pointer8);
                this.for_mul_xorrange_move(pointer12, pointer10, pointer8, this.HFEDegI - 1);
                this.for_mul_xorrange_move(pointer12, pointer11, pointer9, this.HFEDegI);
                this.rem_gf2n(pointer, 0, pointer12);
                pointer10.changeIndex(n5 + this.SIZE_ROW * this.NB_WORD_GFqn);
                pointer8.changeIndex(n6);
                pointer11.changeIndex(n7);
                pointer9.changeIndex(n8 + this.HFEDegI * this.NB_WORD_GFqn);
                pointer.move(this.NB_WORD_GFqn);
            }
            pointer5.changeIndex(pointer4);
            pointer11.move(-this.NB_WORD_GFqn);
            while (n2 < this.HFEnv) {
                pointer5.move((this.HFEDegI + 1) * this.NB_WORD_GFqn);
                this.dotProduct_gf2n(pointer, pointer11, pointer5, this.HFEDegI + 1);
                pointer.move(this.NB_WORD_GFqn);
                ++n2;
            }
            pointer11.move(this.NB_WORD_GFqn + this.SIZE_ROW * this.NB_WORD_GFqn);
        }
        pointer2.move(this.NB_WORD_GFqn - this.MQv_GFqn_SIZE);
        pointer.copyFrom(pointer2, this.NB_WORD_GFqn * (this.NB_MONOMIAL_VINEGAR - 1));
        pointer.indexReset();
        pointer2.indexReset();
    }

    private void special_buffer(Pointer pointer, Pointer pointer2, Pointer pointer3) {
        int n2;
        int n3 = pointer2.getIndex();
        pointer2.move(this.NB_WORD_GFqn * (this.HFEv + 1) << 1);
        pointer.copyFrom(pointer2, this.NB_WORD_GFqn);
        pointer.move(this.NB_WORD_GFqn);
        Pointer pointer4 = new Pointer(pointer2, this.NB_WORD_GFqn * (this.HFEv + 2));
        for (n2 = 2; n2 < this.SIZE_ROW - 1; ++n2) {
            this.copy_move_matrix_move(pointer, pointer4, n2 - 1);
        }
        if (this.ENABLED_REMOVE_ODD_DEGREE) {
            while (n2 < this.SIZE_ROW - 1) {
                this.copy_move_matrix_move(pointer, pointer4, n2 - 2);
                ++n2;
            }
        }
        pointer.set1_gf2n(0, this.NB_WORD_GFqn);
        pointer.setXorMatrix(pointer4, this.NB_WORD_GFqn, this.HFEDegJ);
        for (int i2 = 0; i2 < this.HFEn - 1; ++i2) {
            this.mul_gf2n(pointer, pointer3, pointer2);
            pointer.move(this.NB_WORD_GFqn);
            pointer4.changeIndex(pointer2, this.NB_WORD_GFqn * (this.HFEv + 2));
            for (n2 = 2; n2 < this.HFEDegI; ++n2) {
                this.dotproduct_move_move(pointer, pointer4, pointer3, n2);
            }
            if (this.ENABLED_REMOVE_ODD_DEGREE) {
                pointer3.move(this.NB_WORD_GFqn);
                while (n2 < this.SIZE_ROW - 1) {
                    this.dotproduct_move_move(pointer, pointer4, pointer3, n2 - 1);
                    ++n2;
                }
                pointer3.move(-this.NB_WORD_GFqn);
            }
            if (this.HFEDegJ == 0) {
                pointer.copyFrom(pointer3, this.NB_WORD_GFqn);
                pointer.move(this.NB_WORD_GFqn);
                pointer3.move(this.SIZE_ROW * this.NB_WORD_GFqn);
                continue;
            }
            this.dotProduct_gf2n(pointer, pointer3, pointer4, this.HFEDegJ);
            pointer3.move(this.HFEDegJ * this.NB_WORD_GFqn);
            pointer.setXorRange_SelfMove(pointer3, this.NB_WORD_GFqn);
            pointer3.move((this.SIZE_ROW - this.HFEDegJ) * this.NB_WORD_GFqn);
        }
        pointer.indexReset();
        pointer2.changeIndex(n3);
        pointer3.indexReset();
    }

    private void copy_move_matrix_move(Pointer pointer, Pointer pointer2, int n2) {
        pointer.copyFrom(pointer2, this.NB_WORD_GFqn);
        pointer2.move(this.NB_WORD_GFqn);
        pointer.setXorMatrix(pointer2, this.NB_WORD_GFqn, n2);
        pointer2.move(this.NB_WORD_GFqn * (this.HFEv + 1));
    }

    private void dotproduct_move_move(Pointer pointer, Pointer pointer2, Pointer pointer3, int n2) {
        this.dotProduct_gf2n(pointer, pointer3, pointer2, n2);
        pointer.move(this.NB_WORD_GFqn);
        pointer2.move((n2 + this.HFEv + 1) * this.NB_WORD_GFqn);
    }

    private void dotProduct_gf2n(Pointer pointer, Pointer pointer2, Pointer pointer3, int n2) {
        Pointer pointer4 = new Pointer(this.NB_WORD_MUL);
        int n3 = pointer2.getIndex();
        int n4 = pointer3.getIndex();
        this.mul_move(pointer4, pointer2, pointer3);
        this.for_mul_xorrange_move(pointer4, pointer2, pointer3, n2 - 1);
        this.rem_gf2n(pointer, 0, pointer4);
        pointer2.changeIndex(n3);
        pointer3.changeIndex(n4);
    }

    void mul_gf2n(Pointer pointer, Pointer pointer2, int n2, Pointer pointer3) {
        int n3 = pointer2.getIndex();
        pointer2.move(n2);
        this.mul.mul_gf2x(this.Buffer_NB_WORD_MUL, pointer2, pointer3);
        pointer2.changeIndex(n3);
        this.rem_gf2n(pointer, 0, this.Buffer_NB_WORD_MUL);
    }

    void mul_gf2n(Pointer pointer, Pointer pointer2, Pointer pointer3) {
        this.mul.mul_gf2x(this.Buffer_NB_WORD_MUL, pointer2, pointer3);
        this.rem_gf2n(pointer, 0, this.Buffer_NB_WORD_MUL);
    }

    void for_mul_xorrange_move(Pointer pointer, Pointer pointer2, Pointer pointer3, int n2) {
        for (int i2 = 0; i2 < n2; ++i2) {
            this.mul.mul_gf2x_xor(pointer, pointer2, pointer3);
            pointer2.move(this.NB_WORD_GFqn);
            pointer3.move(this.NB_WORD_GFqn);
        }
    }

    void mul_move(Pointer pointer, Pointer pointer2, Pointer pointer3) {
        this.mul.mul_gf2x(pointer, pointer2, pointer3);
        pointer2.move(this.NB_WORD_GFqn);
        pointer3.move(this.NB_WORD_GFqn);
    }

    public void mul_xorrange(Pointer pointer, Pointer pointer2, Pointer pointer3) {
        this.mul.mul_gf2x_xor(pointer, pointer2, pointer3);
    }

    public void mul_rem_xorrange(Pointer pointer, Pointer pointer2, Pointer pointer3) {
        this.mul.mul_gf2x(this.Buffer_NB_WORD_MUL, pointer2, pointer3);
        this.rem.rem_gf2n_xor(pointer.array, pointer.cp, this.Buffer_NB_WORD_MUL.array);
    }

    public void mul_rem_xorrange(Pointer pointer, Pointer pointer2, Pointer pointer3, int n2) {
        int n3 = pointer3.getIndex();
        pointer3.move(n2);
        this.mul.mul_gf2x(this.Buffer_NB_WORD_MUL, pointer2, pointer3);
        this.rem.rem_gf2n_xor(pointer.array, pointer.cp, this.Buffer_NB_WORD_MUL.array);
        pointer3.changeIndex(n3);
    }

    private void rem_gf2n(Pointer pointer, int n2, Pointer pointer2) {
        this.rem.rem_gf2n(pointer.array, n2 += pointer.getIndex(), pointer2.array);
    }

    private void sqr_gf2n(Pointer pointer, int n2, Pointer pointer2, int n3) {
        this.mul.sqr_gf2x(this.Buffer_NB_WORD_MUL.array, pointer2.array, n3 += pointer2.cp);
        this.rem_gf2n(pointer, n2, this.Buffer_NB_WORD_MUL);
    }

    private void sqr_gf2n(Pointer pointer, Pointer pointer2) {
        this.mul.sqr_gf2x(this.Buffer_NB_WORD_MUL.array, pointer2.array, pointer2.cp);
        this.rem.rem_gf2n(pointer.array, pointer.cp, this.Buffer_NB_WORD_MUL.array);
    }

    void cleanLowerMatrix(Pointer pointer, FunctionParams functionParams) {
        int n2;
        int n3;
        int n4;
        switch (functionParams.ordinal()) {
            case 2: {
                n4 = this.HFEnq;
                n3 = this.HFEnr;
                break;
            }
            case 0: {
                n4 = this.HFEnvq;
                n3 = this.HFEnvr;
                break;
            }
            default: {
                throw new IllegalArgumentException("");
            }
        }
        Pointer pointer2 = new Pointer(pointer);
        for (n2 = 1; n2 <= n4; ++n2) {
            this.for_and_xor_shift_incre_move(pointer2, n2, 64);
            pointer2.moveIncremental();
        }
        this.for_and_xor_shift_incre_move(pointer2, n2, n3);
    }

    private void for_and_xor_shift_incre_move(Pointer pointer, int n2, int n3) {
        long l2 = 0L;
        for (int i2 = 0; i2 < n3; ++i2) {
            pointer.setAnd(l2);
            pointer.setXor(1L << i2);
            l2 <<= 1;
            ++l2;
            pointer.move(n2);
        }
    }

    void invMatrixLU_gf2(Pointer pointer, Pointer pointer2, Pointer pointer3, FunctionParams functionParams) {
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        Pointer pointer4 = new Pointer(pointer2);
        Pointer pointer5 = new Pointer(pointer2);
        Pointer pointer6 = new Pointer(pointer3);
        switch (functionParams.ordinal()) {
            case 0: {
                n7 = this.HFEnvq;
                n6 = this.HFEnv - 1;
                n5 = this.NB_WORD_GF2nv;
                n4 = this.HFEnvr;
                n3 = this.LTRIANGULAR_NV_SIZE;
                break;
            }
            case 2: {
                pointer.setRangeClear(0, this.MATRIXn_SIZE);
                n7 = this.HFEnq;
                n6 = this.HFEn - 1;
                n5 = this.NB_WORD_GFqn;
                n4 = this.HFEnr;
                n3 = this.LTRIANGULAR_N_SIZE;
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid Input");
            }
        }
        Pointer pointer7 = new Pointer(pointer);
        Pointer pointer8 = new Pointer(pointer);
        int n8 = 0;
        for (n2 = 0; n2 < n7; ++n2) {
            n8 = this.loop_xor_loop_move_xorandmask_move(pointer7, pointer8, pointer4, pointer5, n8, n2, 64, n6, n5);
            pointer5.moveIncremental();
        }
        if (n4 > 1) {
            this.loop_xor_loop_move_xorandmask_move(pointer7, pointer8, pointer4, pointer5, n8, n2, n4 - 1, n6, n5);
            pointer7.setXor(n2, 1L << n4 - 1);
            pointer7.move(n5);
        } else if (n4 == 1) {
            pointer7.set(n2, 1L);
            pointer7.move(n5);
        }
        pointer6.move(n3);
        for (n8 = n6; n8 > 0; --n8) {
            pointer6.move(-1 - (n8 >>> 6));
            pointer7.move(-n5);
            pointer8.changeIndex(pointer);
            for (int i2 = 0; i2 < n8; ++i2) {
                pointer8.setXorRangeAndMask(pointer7, n5, -(pointer6.get(i2 >>> 6) >>> (i2 & 0x3F) & 1L));
                pointer8.move(n5);
            }
        }
    }

    private int loop_xor_loop_move_xorandmask_move(Pointer pointer, Pointer pointer2, Pointer pointer3, Pointer pointer4, int n2, int n3, int n4, int n5, int n6) {
        int n7 = 0;
        while (n7 < n4) {
            pointer.setXor(n3, 1L << n7);
            pointer2.changeIndex(pointer);
            pointer3.changeIndex(pointer4);
            for (int i2 = n2; i2 < n5; ++i2) {
                pointer2.move(n6);
                pointer3.move((i2 >>> 6) + 1);
                pointer2.setXorRangeAndMask(pointer, n3 + 1, -(pointer3.get() >>> n7 & 1L));
            }
            pointer.move(n6);
            pointer4.move(n3 + 1);
            ++n7;
            ++n2;
        }
        return n2;
    }

    void vecMatProduct(Pointer pointer, Pointer pointer2, Pointer pointer3, FunctionParams functionParams) {
        int n2;
        long l2;
        int n3;
        int n4;
        int n5;
        int n6 = 0;
        int n7 = 0;
        Pointer pointer4 = new Pointer(pointer3);
        switch (functionParams.ordinal()) {
            case 0: {
                pointer.setRangeClear(0, this.NB_WORD_GF2nv);
                n5 = this.HFEnvq;
                n4 = this.NB_WORD_GF2nv;
                n3 = this.NB_WORD_GF2nv;
                break;
            }
            case 1: {
                pointer.setRangeClear(0, this.NB_WORD_GFqn);
                n4 = this.NB_WORD_GFqn;
                n3 = this.NB_WORD_GFqn;
                n5 = this.HFEvq;
                break;
            }
            case 2: {
                pointer.setRangeClear(0, this.NB_WORD_GFqn);
                n4 = this.NB_WORD_GFqn;
                n3 = this.NB_WORD_GFqn;
                n5 = this.HFEnq;
                break;
            }
            case 3: {
                pointer.setRangeClear(0, this.NB_WORD_GF2m);
                n5 = this.HFEnq;
                n4 = this.NB_WORD_GF2m;
                n3 = this.NB_WORD_GFqn;
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid input for vecMatProduct");
            }
        }
        while (n6 < n5) {
            l2 = pointer2.get(n6);
            while (n7 < 64) {
                pointer.setXorRangeAndMask(pointer4, n4, -(l2 & 1L));
                pointer4.move(n3);
                l2 >>>= 1;
                ++n7;
            }
            n7 = 0;
            ++n6;
        }
        switch (functionParams.ordinal()) {
            case 0: {
                if (this.HFEnvr == 0) {
                    return;
                }
                l2 = pointer2.get(this.HFEnvq);
                n2 = this.HFEnvr;
                break;
            }
            case 1: {
                if (this.HFEvr == 0) {
                    return;
                }
                l2 = pointer2.get(this.HFEvq);
                n2 = this.HFEvr;
                break;
            }
            case 2: 
            case 3: {
                l2 = pointer2.get(this.HFEnq);
                n2 = this.HFEnr;
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid input for vecMatProduct");
            }
        }
        while (n7 < n2) {
            pointer.setXorRangeAndMask(pointer4, n4, -(l2 & 1L));
            pointer4.move(n3);
            l2 >>>= 1;
            ++n7;
        }
        if (functionParams == FunctionParams.M && this.HFEmr != 0) {
            pointer.setAnd(this.NB_WORD_GF2m - 1, this.MASK_GF2m);
        }
    }

    private long convMQ_uncompressL_gf2(Pointer pointer, PointerUnion pointerUnion) {
        PointerUnion pointerUnion2 = new PointerUnion(pointerUnion);
        int n2 = this.for_setpk2_end_move_plus(pointer, pointerUnion2, this.HFEnvq);
        if (this.HFEnvr != 0) {
            this.setPk2Value(pointer, pointerUnion2, n2, this.HFEnvq, this.HFEnvr + 1);
        }
        return pointerUnion.get() & 1L;
    }

    private int setPk2Value(Pointer pointer, PointerUnion pointerUnion, int n2, int n3, int n4) {
        for (int i2 = 1; i2 < n4; ++i2) {
            if ((n2 & 0x3F) != 0) {
                pointer.setRangePointerUnion(pointerUnion, n3, n2 & 0x3F);
                pointer.set(n3, pointerUnion.get(n3) >>> (n2 & 0x3F));
                if ((n2 & 0x3F) + i2 > 64) {
                    pointer.setXor(n3, pointerUnion.get(n3 + 1) << 64 - (n2 & 0x3F));
                }
                if ((n2 & 0x3F) + i2 >= 64) {
                    pointerUnion.moveIncremental();
                }
            } else {
                pointer.setRangePointerUnion(pointerUnion, n3 + 1);
            }
            pointerUnion.move(n3);
            pointer.setAnd(n3, (1L << i2) - 1L);
            pointer.move(n3 + 1);
            n2 += (n3 << 6) + i2;
        }
        return n2;
    }

    private void setPk2_endValue(Pointer pointer, PointerUnion pointerUnion, int n2, int n3) {
        if ((n2 & 0x3F) != 0) {
            pointer.setRangePointerUnion(pointerUnion, n3 + 1, n2 & 0x3F);
        } else {
            pointer.setRangePointerUnion(pointerUnion, n3 + 1);
        }
    }

    private long convMQ_last_uncompressL_gf2(Pointer pointer, PointerUnion pointerUnion) {
        PointerUnion pointerUnion2 = new PointerUnion(pointerUnion);
        int n2 = this.HFEnv - 1;
        int n3 = n2 >>> 6;
        int n4 = n2 & 0x3F;
        int n5 = this.for_setpk2_end_move_plus(pointer, pointerUnion2, n3);
        if (n4 != 0) {
            n5 = this.setPk2Value(pointer, pointerUnion2, n5, n3, n4 + 1);
        }
        n2 = this.HFEnv - this.LOST_BITS;
        int n6 = n2 >>> 6;
        int n7 = n2 & 0x3F;
        int n8 = n6;
        if (n7 != 0) {
            int n9 = n7;
            if ((n5 & 0x3F) != 0) {
                if ((this.NB_MONOMIAL_PK - this.LOST_BITS + 7 >>> 3 & 7) != 0) {
                    int n10 = this.HFEnv - (64 - (this.NB_MONOMIAL_PK - this.LOST_BITS - this.HFEnvr & 0x3F) & 0x3F) >>> 6;
                    pointer.setRangePointerUnion_Check(pointerUnion2, n10, n5);
                    n2 = n10;
                    pointer.set(n2, pointerUnion2.getWithCheck(n2) >>> (n5 & 0x3F));
                    if (n10 < n6) {
                        long l2 = pointerUnion2.getWithCheck(n2 + 1);
                        pointer.setXor(n2, l2 << 64 - (n5 & 0x3F));
                        pointer.set(n2 + 1, l2 >>> (n5 & 0x3F));
                    } else if ((n5 & 0x3F) + n9 > 64) {
                        pointer.setXor(n2, pointerUnion2.getWithCheck(n2 + 1) << 64 - (n5 & 0x3F));
                    }
                } else {
                    pointer.setRangePointerUnion(pointerUnion2, n8, n5 & 0x3F);
                    pointer.set(n8, pointerUnion2.get(n8) >>> (n5 & 0x3F));
                    if ((n5 & 0x3F) + n9 > 64) {
                        pointer.setXor(n8, pointerUnion2.get(n8 + 1) << 64 - (n5 & 0x3F));
                    }
                }
            } else if ((this.NB_MONOMIAL_PK - this.LOST_BITS + 7 >>> 3 & 7) != 0) {
                pointer.setRangePointerUnion(pointerUnion2, n8);
                pointer.set(n8, pointerUnion2.getWithCheck(n8));
            } else {
                pointer.setRangePointerUnion(pointerUnion2, n8 + 1);
            }
        } else if (n6 != 0) {
            if ((n5 & 0x3F) != 0) {
                if ((this.NB_MONOMIAL_PK - this.LOST_BITS + 7 >>> 3 & 7) != 0) {
                    pointer.setRangePointerUnion(pointerUnion2, n8 - 1, n5 & 0x3F);
                    n2 = n8 - 1;
                    pointer.set(n2, pointerUnion2.get(n2) >>> (n5 & 0x3F));
                    pointer.setXor(n2, pointerUnion2.getWithCheck(n2 + 1) << 64 - (n5 & 0x3F));
                } else {
                    pointer.setRangePointerUnion(pointerUnion2, n8, n5 & 0x3F);
                }
            } else {
                pointer.setRangePointerUnion(pointerUnion2, n8);
            }
        }
        return pointerUnion.get() & 1L;
    }

    private int for_setpk2_end_move_plus(Pointer pointer, PointerUnion pointerUnion, int n2) {
        int n3 = 1;
        for (int i2 = 0; i2 < n2; ++i2) {
            n3 = this.setPk2Value(pointer, pointerUnion, n3, i2, 64);
            this.setPk2_endValue(pointer, pointerUnion, n3, i2);
            pointerUnion.move(i2 + 1);
            pointer.move(i2 + 1);
            n3 += i2 + 1 << 6;
        }
        return n3;
    }

    public int sign_openHFE_huncomp_pk(byte[] byArray, int n2, byte[] byArray2, PointerUnion pointerUnion, PointerUnion pointerUnion2) {
        int n3;
        Pointer pointer = new Pointer(this.SIZE_SIGN_UNCOMPRESSED);
        Pointer pointer2 = new Pointer(this.NB_WORD_GF2nv);
        Pointer pointer3 = new Pointer(this.NB_WORD_GF2nv);
        Pointer pointer4 = new Pointer(pointer2);
        Pointer pointer5 = new Pointer(pointer3);
        byte[] byArray3 = new byte[64];
        Pointer pointer6 = new Pointer(this.NB_ITE * this.SIZE_DIGEST_UINT);
        int n4 = 0;
        long l2 = pointerUnion2.get();
        pointerUnion2.move(1);
        this.uncompress_signHFE(pointer, byArray2);
        this.getSHA3Hash(pointer6, 0, 64, byArray, n4, n2, byArray3);
        for (n3 = 1; n3 < this.NB_ITE; ++n3) {
            this.getSHA3Hash(pointer6, n3 * this.SIZE_DIGEST_UINT, 64, byArray3, 0, this.SIZE_DIGEST, byArray3);
            pointer6.setAnd(this.SIZE_DIGEST_UINT * (n3 - 1) + this.NB_WORD_GF2m - 1, this.MASK_GF2m);
        }
        pointer6.setAnd(this.SIZE_DIGEST_UINT * (n3 - 1) + this.NB_WORD_GF2m - 1, this.MASK_GF2m);
        this.evalMQShybrid8_uncomp_nocst_gf2_m(pointer4, pointer, pointerUnion, pointerUnion2);
        pointer4.setXor(this.HFEmq, l2);
        for (n3 = this.NB_ITE - 1; n3 > 0; --n3) {
            pointer4.setXorRange(pointer6, n3 * this.SIZE_DIGEST_UINT, this.NB_WORD_GF2m);
            int n5 = this.NB_WORD_GF2nv + (this.NB_ITE - 1 - n3) * this.NB_WORD_GF2nvm;
            pointer4.setAnd(this.NB_WORD_GF2m - 1, this.MASK_GF2m);
            pointer4.setXor(this.NB_WORD_GF2m - 1, pointer.get(n5));
            if (this.NB_WORD_GF2nvm != 1) {
                pointer4.copyFrom(this.NB_WORD_GF2m, pointer, ++n5, this.NB_WORD_GF2nvm - 1);
            }
            this.evalMQShybrid8_uncomp_nocst_gf2_m(pointer5, pointer4, pointerUnion, pointerUnion2);
            pointer5.setXor(this.HFEmq, l2);
            pointer5.swap(pointer4);
        }
        return pointer4.isEqual_nocst_gf2(pointer6, this.NB_WORD_GF2m);
    }

    private void getSHA3Hash(Pointer pointer, int n2, int n3, byte[] byArray, int n4, int n5, byte[] byArray2) {
        this.sha3Digest.update(byArray, n4, n5);
        this.sha3Digest.doFinal(byArray2, 0);
        pointer.fill(n2, byArray2, 0, n3);
    }

    private void evalMQShybrid8_uncomp_nocst_gf2_m(Pointer pointer, Pointer pointer2, PointerUnion pointerUnion, PointerUnion pointerUnion2) {
        PointerUnion pointerUnion3 = new PointerUnion(pointerUnion2);
        this.evalMQSnocst8_quo_gf2(pointer, pointer2, pointerUnion);
        if (this.HFEmr < 8) {
            pointer.set(this.HFEmq, 0L);
        }
        for (int i2 = this.HFEmr - this.HFEmr8; i2 < this.HFEmr; ++i2) {
            pointer.setXor(this.HFEmq, this.evalMQnocst_unrolled_no_simd_gf2(pointer2, pointerUnion3) << i2);
            pointerUnion3.move(this.NB_WORD_UNCOMP_EQ);
        }
    }

    private void uncompress_signHFE(Pointer pointer, byte[] byArray) {
        PointerUnion pointerUnion = new PointerUnion(pointer);
        int n2 = (1 << this.HFEnvr8) - 1;
        pointerUnion.fillBytes(0, byArray, 0, this.NB_BYTES_GFqnv);
        if (this.HFEnvr8 != 0) {
            pointerUnion.setAndByte(this.NB_BYTES_GFqnv - 1, n2);
        }
        int n3 = this.HFEnv;
        pointerUnion.moveNextBytes((this.NB_WORD_GF2nv << 3) + (this.HFEmq8 & 7));
        for (int i2 = 1; i2 < this.NB_ITE; ++i2) {
            int n4;
            int n5 = Math.min(this.HFEDELTA + this.HFEv, 8 - (n3 & 7) & 7);
            if ((n3 & 7) != 0) {
                pointerUnion.setXorByte((byArray[n3 >>> 3] & 0xFF) >>> (n3 & 7) << this.HFEmr8);
                int n6 = n5 - this.VAL_BITS_M;
                if (n6 >= 0) {
                    pointerUnion.moveNextByte();
                }
                if (n6 > 0) {
                    pointerUnion.setXorByte((byArray[(n3 += this.VAL_BITS_M) >>> 3] & 0xFF) >>> (n3 & 7));
                    n3 += n6;
                } else {
                    n3 += n5;
                }
            }
            int n7 = this.HFEDELTA + this.HFEv - n5;
            int n8 = this.HFEm + n5 & 7;
            if (n8 != 0) {
                for (n4 = 0; n4 < n7 - 1 >>> 3; ++n4) {
                    pointerUnion.setXorByte((byArray[n3 >>> 3] & 0xFF) << n8);
                    pointerUnion.moveNextByte();
                    pointerUnion.setXorByte((byArray[n3 >>> 3] & 0xFF) >>> 8 - n8);
                    n3 += 8;
                }
                pointerUnion.setXorByte((byArray[n3 >>> 3] & 0xFF) << n8);
                pointerUnion.moveNextByte();
                n7 = (n7 + 7 & 7) + 1;
                if (n7 > 8 - n8) {
                    pointerUnion.setByte((byArray[n3 >>> 3] & 0xFF) >>> 8 - n8);
                    pointerUnion.moveNextByte();
                }
                n3 += n7;
            } else {
                for (n4 = 0; n4 < n7 + 7 >>> 3; ++n4) {
                    pointerUnion.setByte(byArray[n3 >>> 3]);
                    n3 += 8;
                    pointerUnion.moveNextByte();
                }
                n3 -= 8 - (n7 & 7) & 7;
            }
            if (this.HFEnvr8 != 0) {
                pointerUnion.setAndByte(-1, n2);
            }
            pointerUnion.moveNextBytes((8 - (this.NB_BYTES_GFqnv & 7) & 7) + (this.HFEmq8 & 7));
        }
    }

    private void evalMQSnocst8_quo_gf2(Pointer pointer, Pointer pointer2, PointerUnion pointerUnion) {
        int n2;
        long l2;
        int n3 = this.HFEnv;
        int n4 = this.HFEm >>> 3 != 0 ? this.HFEm >>> 3 << 3 : this.HFEm;
        int n5 = (n4 & 7) != 0 ? (n4 >>> 3) + 1 : n4 >>> 3;
        int n6 = (n5 >>> 3) + ((n5 & 7) != 0 ? 1 : 0);
        PointerUnion pointerUnion2 = new PointerUnion(pointerUnion);
        System.arraycopy(pointerUnion2.getArray(), 0, pointer.getArray(), pointer.getIndex(), n6);
        pointerUnion2.moveNextBytes(n5);
        for (int i2 = 0; i2 < this.HFEnvq; ++i2) {
            l2 = pointer2.get(i2);
            n2 = 0;
            while (n2 < 64) {
                if ((l2 & 1L) != 0L) {
                    pointer.setXorRange(0, pointerUnion2, 0, n6);
                    pointerUnion2.moveNextBytes(n5);
                    long l3 = l2 >>> 1;
                    this.LOOPJR_UNROLLED_64(pointer, pointerUnion2, n2 + 1, 64, l3, n5, n6);
                    for (int i3 = i2 + 1; i3 < this.HFEnvq; ++i3) {
                        l3 = pointer2.get(i3);
                        this.LOOPJR_UNROLLED_64(pointer, pointerUnion2, 0, 64, l3, n5, n6);
                    }
                    if (this.HFEnvr != 0) {
                        this.choose_LOOPJR(pointer, pointerUnion2, 0, pointer2.get(this.HFEnvq), n5, n6);
                    }
                } else {
                    pointerUnion2.moveNextBytes(n3 * n5);
                }
                l2 >>>= 1;
                ++n2;
                --n3;
            }
        }
        if (this.HFEnvr != 0) {
            l2 = pointer2.get(this.HFEnvq);
            n2 = 0;
            while (n2 < this.HFEnvr) {
                if ((l2 & 1L) != 0L) {
                    pointer.setXorRange(0, pointerUnion2, 0, n6);
                    pointerUnion2.moveNextBytes(n5);
                    this.choose_LOOPJR(pointer, pointerUnion2, n2 + 1, l2 >>> 1, n5, n6);
                } else {
                    pointerUnion2.moveNextBytes(n3 * n5);
                }
                l2 >>>= 1;
                ++n2;
                --n3;
            }
        }
        if ((n4 & 0x3F) != 0) {
            pointer.setAnd(n6 - 1, (1L << (n4 & 0x3F)) - 1L);
        }
    }

    private void choose_LOOPJR(Pointer pointer, PointerUnion pointerUnion, int n2, long l2, int n3, int n4) {
        if (this.HFEnvr < 8) {
            this.LOOPJR_NOCST_64(pointer, pointerUnion, n2, this.HFEnvr, l2, n3, n4);
        } else {
            this.LOOPJR_UNROLLED_64(pointer, pointerUnion, n2, this.HFEnvr, l2, n3, n4);
        }
    }

    private void LOOPJR_UNROLLED_64(Pointer pointer, PointerUnion pointerUnion, int n2, int n3, long l2, int n4, int n5) {
        int n6;
        for (n6 = n2; n6 < n3 - 4 + 1; n6 += 4) {
            l2 = this.LOOPJR_NOCST_64(pointer, pointerUnion, 0, 4, l2, n4, n5);
        }
        this.LOOPJR_NOCST_64(pointer, pointerUnion, n6, n3, l2, n4, n5);
    }

    private long LOOPJR_NOCST_64(Pointer pointer, PointerUnion pointerUnion, int n2, int n3, long l2, int n4, int n5) {
        for (int i2 = n2; i2 < n3; ++i2) {
            if ((l2 & 1L) != 0L) {
                pointer.setXorRange(0, pointerUnion, 0, n5);
            }
            pointerUnion.moveNextBytes(n4);
            l2 >>>= 1;
        }
        return l2;
    }

    private long evalMQnocst_unrolled_no_simd_gf2(Pointer pointer, PointerUnion pointerUnion) {
        int n2;
        long l2 = 0L;
        int n3 = 64;
        PointerUnion pointerUnion2 = new PointerUnion(pointerUnion);
        long l3 = pointer.get();
        for (n2 = 0; n2 < n3; ++n2) {
            if ((l3 >>> n2 & 1L) == 0L) continue;
            l2 ^= pointerUnion2.get(n2) & l3;
        }
        pointerUnion2.move(64);
        for (int i2 = 1; i2 < this.NB_WORD_GF2nv; ++i2) {
            n3 = this.NB_WORD_GF2nv == i2 + 1 && this.HFEnvr != 0 ? this.HFEnvr : 64;
            l3 = pointer.get(i2);
            for (n2 = 0; n2 < n3; ++n2) {
                if ((l3 >>> n2 & 1L) != 0L) {
                    l2 ^= pointerUnion2.getDotProduct(0, pointer, 0, i2 + 1);
                }
                pointerUnion2.move(i2 + 1);
            }
        }
        l2 = GeMSSUtils.XORBITS_UINT(l2);
        return l2;
    }

    public void signHFE_FeistelPatarin(SecureRandom secureRandom, byte[] byArray, byte[] byArray2, int n2, int n3, byte[] byArray3) {
        byte[] byArray4;
        this.random = secureRandom;
        Pointer pointer = new Pointer(this.NB_WORD_GFqn);
        Pointer pointer2 = new Pointer(this.SIZE_DIGEST_UINT);
        Pointer pointer3 = new Pointer(this.SIZE_DIGEST_UINT);
        Pointer pointer4 = new Pointer(pointer3);
        int n4 = this.HFEv & 7;
        int n5 = (this.HFEv >>> 3) + (n4 != 0 ? 1 : 0);
        long l2 = GeMSSUtils.maskUINT(this.HFEvr);
        long l3 = 0L;
        SecretKeyHFE secretKeyHFE = new SecretKeyHFE(this);
        Pointer pointer5 = new Pointer(this.NB_WORD_GFqv);
        Pointer[] pointerArray = new Pointer[this.HFEDegI + 1];
        this.precSignHFE(secretKeyHFE, pointerArray, byArray3);
        Pointer pointer6 = new Pointer(secretKeyHFE.F_struct.poly);
        Pointer pointer7 = new Pointer(pointer2);
        byte[] byArray5 = new byte[this.Sha3BitStrength >>> 3];
        this.getSHA3Hash(pointer7, 0, byArray5.length, byArray2, n2, n3, byArray5);
        Pointer pointer8 = new Pointer(this.SIZE_SIGN_UNCOMPRESSED);
        Pointer pointer9 = new Pointer(this.NB_WORD_GF2nv);
        PointerUnion pointerUnion = new PointerUnion(pointer9);
        for (int i2 = 1; i2 <= this.NB_ITE; ++i2) {
            pointer9.setRangeFromXor(pointer8, pointer7, this.NB_WORD_GF2m);
            if (this.HFEmr8 != 0) {
                pointer9.setAnd(this.NB_WORD_GF2m - 1, this.MASK_GF2m);
                l3 = pointerUnion.getByte(this.HFEmq8);
            }
            do {
                if (this.HFEmr8 != 0) {
                    pointerUnion.fillRandomBytes(this.HFEmq8, secureRandom, this.NB_BYTES_GFqn - this.NB_BYTES_GFqm + 1);
                    pointerUnion.setAndThenXorByte(this.HFEmq8, -(1L << this.HFEmr8), l3);
                } else {
                    pointerUnion.fillRandomBytes(this.NB_BYTES_GFqm, secureRandom, this.NB_BYTES_GFqn - this.NB_BYTES_GFqm);
                }
                if ((this.HFEn & 7) != 0) {
                    pointer9.setAnd(this.NB_WORD_GFqn - 1, this.MASK_GF2n);
                }
                this.vecMatProduct(pointer, pointer9, secretKeyHFE.T, FunctionParams.N);
                pointer5.fillRandom(0, secureRandom, n5);
                if (n4 != 0) {
                    pointer5.setAnd(this.NB_WORD_GFqv - 1, l2);
                }
                this.evalMQSv_unrolled_gf2(pointer6, pointer5, secretKeyHFE.F_HFEv);
                for (int i3 = 0; i3 <= this.HFEDegI; ++i3) {
                    this.vecMatProduct(this.Buffer_NB_WORD_GFqn, pointer5, new Pointer(pointerArray[i3], this.NB_WORD_GFqn), FunctionParams.V);
                    pointer6.setRangeFromXor(this.NB_WORD_GFqn * ((i3 * (i3 + 1) >>> 1) + 1), pointerArray[i3], 0, this.Buffer_NB_WORD_GFqn, 0, this.NB_WORD_GFqn);
                }
            } while (this.chooseRootHFE_gf2nx(pointer9, secretKeyHFE.F_struct, pointer) == 0);
            pointer9.setXor(this.NB_WORD_GFqn - 1, pointer5.get() << this.HFEnr);
            pointer9.setRangeRotate(this.NB_WORD_GFqn, pointer5, 0, this.NB_WORD_GFqv - 1, 64 - this.HFEnr);
            if (this.NB_WORD_GFqn + this.NB_WORD_GFqv == this.NB_WORD_GF2nv) {
                pointer9.set(this.NB_WORD_GFqn + this.NB_WORD_GFqv - 1, pointer5.get(this.NB_WORD_GFqv - 1) >>> 64 - this.HFEnr);
            }
            this.vecMatProduct(pointer8, pointer9, secretKeyHFE.S, FunctionParams.NV);
            if (i2 == this.NB_ITE) continue;
            int n6 = this.NB_WORD_GF2nv + (this.NB_ITE - 1 - i2) * this.NB_WORD_GF2nvm;
            pointer8.copyFrom(n6, pointer8, this.NB_WORD_GF2nv - this.NB_WORD_GF2nvm, this.NB_WORD_GF2nvm);
            if (this.HFEmr != 0) {
                pointer8.setAnd(n6, this.MASK_GF2m ^ 0xFFFFFFFFFFFFFFFFL);
            }
            byArray4 = pointer7.toBytes(this.SIZE_DIGEST);
            this.getSHA3Hash(pointer4, 0, this.SIZE_DIGEST, byArray4, 0, byArray4.length, byArray4);
            pointer4.swap(pointer7);
        }
        if (this.NB_ITE == 1) {
            byArray4 = pointer8.toBytes(pointer8.getLength() << 3);
            System.arraycopy(byArray4, 0, byArray, 0, this.NB_BYTES_GFqnv);
        } else {
            this.compress_signHFE(byArray, pointer8);
        }
    }

    private void precSignHFE(SecretKeyHFE secretKeyHFE, Pointer[] pointerArray, byte[] byArray) {
        int n2;
        int n3;
        this.precSignHFESeed(secretKeyHFE, byArray);
        this.initListDifferences_gf2nx(secretKeyHFE.F_struct.L);
        Pointer pointer = new Pointer(secretKeyHFE.F_HFEv);
        int n4 = this.NB_COEFS_HFEPOLY * this.NB_WORD_GFqn;
        Pointer pointer2 = new Pointer(n4);
        pointerArray[0] = new Pointer(pointer, this.MQv_GFqn_SIZE);
        pointer.changeIndex(pointerArray[0], this.MLv_GFqn_SIZE);
        Pointer pointer3 = new Pointer(pointer2, 2 * this.NB_WORD_GFqn);
        for (n3 = 0; n3 < this.HFEDegI; ++n3) {
            n2 = n3 - ((1 << n3) + 1 > this.HFE_odd_degree && this.ENABLED_REMOVE_ODD_DEGREE ? 1 : 0);
            pointer3.copyFrom(pointer, n2 * this.NB_WORD_GFqn);
            pointer.move(n2 * this.NB_WORD_GFqn);
            pointer3.move(n2 * this.NB_WORD_GFqn);
            pointerArray[n3 + 1] = new Pointer(pointer);
            pointer.move(this.MLv_GFqn_SIZE);
            pointer3.move(this.NB_WORD_GFqn);
        }
        if (this.HFEDegJ != 0) {
            n2 = (1 << n3) + 1 <= this.HFE_odd_degree ? 0 : 1;
            pointer3.copyFrom(pointer, (this.HFEDegJ - n2) * this.NB_WORD_GFqn);
        }
        secretKeyHFE.F_struct.poly = new Pointer(pointer2);
    }

    private void precSignHFESeed(SecretKeyHFE secretKeyHFE, byte[] byArray) {
        int n2 = this.NB_UINT_HFEVPOLY + (this.LTRIANGULAR_NV_SIZE + this.LTRIANGULAR_N_SIZE << 1);
        secretKeyHFE.sk_uncomp = new Pointer(n2 + this.MATRIXnv_SIZE + this.MATRIXn_SIZE);
        SHAKEDigest sHAKEDigest = new SHAKEDigest(this.ShakeBitStrength);
        sHAKEDigest.update(byArray, 0, this.SIZE_SEED_SK);
        byte[] byArray2 = new byte[n2 << 3];
        sHAKEDigest.doFinal(byArray2, 0, byArray2.length);
        secretKeyHFE.sk_uncomp.fill(0, byArray2, 0, byArray2.length);
        secretKeyHFE.S = new Pointer(secretKeyHFE.sk_uncomp, n2);
        secretKeyHFE.T = new Pointer(secretKeyHFE.S, this.MATRIXnv_SIZE);
        secretKeyHFE.F_HFEv = new Pointer(secretKeyHFE.sk_uncomp);
        this.cleanMonicHFEv_gf2nx(secretKeyHFE.F_HFEv);
        Pointer pointer = new Pointer(secretKeyHFE.sk_uncomp, this.NB_UINT_HFEVPOLY);
        Pointer pointer2 = new Pointer(pointer, this.LTRIANGULAR_NV_SIZE);
        this.cleanLowerMatrix(pointer, FunctionParams.NV);
        this.cleanLowerMatrix(pointer2, FunctionParams.NV);
        this.mulMatricesLU_gf2(secretKeyHFE.S, pointer, pointer2, FunctionParams.NV);
        pointer.move(this.LTRIANGULAR_NV_SIZE << 1);
        pointer2.changeIndex(pointer, this.LTRIANGULAR_N_SIZE);
        this.cleanLowerMatrix(pointer, FunctionParams.N);
        this.cleanLowerMatrix(pointer2, FunctionParams.N);
        this.mulMatricesLU_gf2(secretKeyHFE.T, pointer, pointer2, FunctionParams.N);
    }

    void cleanMonicHFEv_gf2nx(Pointer pointer) {
        for (int i2 = this.NB_WORD_GFqn - 1; i2 < this.NB_UINT_HFEVPOLY; i2 += this.NB_WORD_GFqn) {
            pointer.setAnd(i2, this.MASK_GF2n);
        }
    }

    private void mulMatricesLU_gf2(Pointer pointer, Pointer pointer2, Pointer pointer3, FunctionParams functionParams) {
        int n2;
        boolean bl;
        int n3;
        int n4;
        int n5 = pointer.getIndex();
        switch (functionParams.ordinal()) {
            case 2: {
                n4 = this.HFEnq;
                n3 = this.HFEnr;
                bl = true;
                break;
            }
            case 0: {
                n4 = this.HFEnvq;
                n3 = this.HFEnvr;
                bl = this.HFEnvr != 0;
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid parameter for MULMATRICESLU_GF2");
            }
        }
        Pointer pointer4 = new Pointer(pointer2);
        for (n2 = 1; n2 <= n4; ++n2) {
            this.LOOPIR(pointer, pointer4, pointer3, 64, n4, n3, n2, bl);
        }
        this.LOOPIR(pointer, pointer4, pointer3, n3, n4, n3, n2, bl);
        pointer.changeIndex(n5);
    }

    private void LOOPIR(Pointer pointer, Pointer pointer2, Pointer pointer3, int n2, int n3, int n4, int n5, boolean bl) {
        for (int i2 = 0; i2 < n2; ++i2) {
            int n6;
            Pointer pointer4 = new Pointer(pointer3);
            for (n6 = 1; n6 <= n3; ++n6) {
                this.LOOPJR(pointer, pointer2, pointer4, 64, n5, n6);
            }
            if (bl) {
                this.LOOPJR(pointer, pointer2, pointer4, n4, n5, n6);
            }
            pointer2.move(n5);
        }
    }

    private void LOOPJR(Pointer pointer, Pointer pointer2, Pointer pointer3, int n2, int n3, int n4) {
        int n5 = Math.min(n3, n4);
        pointer.set(0L);
        for (int i2 = 0; i2 < n2; ++i2) {
            long l2 = pointer2.getDotProduct(0, pointer3, 0, n5);
            l2 = GeMSSUtils.XORBITS_UINT(l2);
            pointer.setXor(l2 << i2);
            pointer3.move(n4);
        }
        pointer.moveIncremental();
    }

    private int setArrayL(int[] nArray, int n2, int n3, int n4) {
        for (int i2 = n3; i2 < n4; ++i2) {
            nArray[n2++] = this.NB_WORD_GFqn << i2;
        }
        return n2;
    }

    private void initListDifferences_gf2nx(int[] nArray) {
        int n2;
        int n3 = 2;
        nArray[1] = this.NB_WORD_GFqn;
        for (n2 = 0; n2 < this.HFEDegI; ++n2) {
            if (this.ENABLED_REMOVE_ODD_DEGREE && (1 << n2) + 1 > this.HFE_odd_degree) {
                if (n2 != 0) {
                    nArray[n3++] = this.NB_WORD_GFqn << 1;
                }
                n3 = this.setArrayL(nArray, n3, 1, n2);
                continue;
            }
            nArray[n3++] = this.NB_WORD_GFqn;
            n3 = this.setArrayL(nArray, n3, 0, n2);
        }
        if (this.HFEDegJ != 0) {
            if (this.ENABLED_REMOVE_ODD_DEGREE && (1 << n2) + 1 > this.HFE_odd_degree) {
                nArray[n3++] = this.NB_WORD_GFqn << 1;
                this.setArrayL(nArray, n3, 1, this.HFEDegJ - 1);
            } else {
                nArray[n3++] = this.NB_WORD_GFqn;
                this.setArrayL(nArray, n3, 0, this.HFEDegJ - 1);
            }
        }
    }

    void evalMQSv_unrolled_gf2(Pointer pointer, Pointer pointer2, Pointer pointer3) {
        int n2;
        Pointer pointer4 = new Pointer(this.HFEv);
        int n3 = this.HFEv >>> 6;
        int n4 = this.HFEv & 0x3F;
        int n5 = (this.HFEn >>> 6) + ((this.HFEn & 0x3F) != 0 ? 1 : 0);
        int n6 = pointer3.getIndex();
        Pointer pointer5 = new Pointer(n5);
        int n7 = 0;
        for (n2 = 0; n2 < n3; ++n2) {
            n7 = pointer4.setRange_xi(pointer2.get(n2), n7, 64);
        }
        if (n4 != 0) {
            pointer4.setRange_xi(pointer2.get(n2), n7, n4);
        }
        pointer.copyFrom(pointer3, n5);
        pointer3.move(n5);
        for (n2 = 0; n2 < this.HFEv; ++n2) {
            int n8;
            pointer5.copyFrom(pointer3, n5);
            pointer3.move(n5);
            for (n8 = n2 + 1; n8 < this.HFEv - 3; n8 += 4) {
                pointer5.setXorRangeAndMaskMove(pointer3, n5, pointer4.get(n8));
                pointer5.setXorRangeAndMaskMove(pointer3, n5, pointer4.get(n8 + 1));
                pointer5.setXorRangeAndMaskMove(pointer3, n5, pointer4.get(n8 + 2));
                pointer5.setXorRangeAndMaskMove(pointer3, n5, pointer4.get(n8 + 3));
            }
            while (n8 < this.HFEv) {
                pointer5.setXorRangeAndMaskMove(pointer3, n5, pointer4.get(n8));
                ++n8;
            }
            pointer.setXorRangeAndMask(pointer5, n5, pointer4.get(n2));
        }
        pointer3.changeIndex(n6);
    }

    private int chooseRootHFE_gf2nx(Pointer pointer, SecretKeyHFE.complete_sparse_monic_gf2nx complete_sparse_monic_gf2nx2, Pointer pointer2) {
        int n2;
        Pointer pointer3 = new Pointer(this.SIZE_DIGEST_UINT);
        Pointer pointer4 = new Pointer(((this.HFEDeg << 1) - 1) * this.NB_WORD_GFqn);
        Pointer pointer5 = new Pointer((this.HFEDeg + 1) * this.NB_WORD_GFqn);
        Pointer pointer6 = new Pointer(this.NB_WORD_GFqn);
        pointer6.setRangeFromXor(complete_sparse_monic_gf2nx2.poly, pointer2, this.NB_WORD_GFqn);
        if (this.HFEDeg <= 34 || this.HFEn > 196 && this.HFEDeg < 256) {
            this.frobeniusMap_multisqr_HFE_gf2nx(pointer4, complete_sparse_monic_gf2nx2, pointer6);
        } else {
            n2 = 2 << this.HFEDegI;
            pointer4.set(n2 * this.NB_WORD_GFqn, 1L);
            this.divsqr_r_HFE_cstdeg_gf2nx(pointer4, n2, n2, this.HFEDeg, complete_sparse_monic_gf2nx2, pointer6);
            this.for_sqr_divsqr(pointer4, this.HFEDegI + 1, this.HFEn, complete_sparse_monic_gf2nx2, pointer6);
        }
        pointer4.setXor(this.NB_WORD_GFqn, 1L);
        n2 = pointer5.getIndex();
        pointer5.copyFrom(complete_sparse_monic_gf2nx2.poly, this.NB_WORD_GFqn);
        this.for_copy_move(pointer5, complete_sparse_monic_gf2nx2);
        pointer5.changeIndex(n2);
        pointer5.set(this.HFEDeg * this.NB_WORD_GFqn, 1L);
        pointer5.setXorRange(pointer2, this.NB_WORD_GFqn);
        n2 = pointer4.getD_for_not0_or_plus(this.NB_WORD_GFqn, this.HFEDeg - 1);
        n2 = this.gcd_gf2nx(pointer5, this.HFEDeg, pointer4, n2);
        if (this.buffer != 0) {
            pointer4.swap(pointer5);
        }
        if (pointer4.is0_gf2n(0, this.NB_WORD_GFqn) == 0) {
            return 0;
        }
        this.convMonic_gf2nx(pointer5, n2);
        Pointer pointer7 = new Pointer(n2 * this.NB_WORD_GFqn);
        this.findRootsSplit_gf2nx(pointer7, pointer5, n2);
        if (n2 == 1) {
            pointer.copyFrom(pointer7, this.NB_WORD_GFqn);
        } else {
            this.fast_sort_gf2n(pointer7, n2);
            this.getSHA3Hash(pointer3, 0, this.Sha3BitStrength >>> 3, pointer2.toBytes(this.NB_BYTES_GFqn), 0, this.NB_BYTES_GFqn, new byte[this.Sha3BitStrength >>> 3]);
            pointer.copyFrom(0, pointer7, (int)GeMSSEngine.remainderUnsigned(pointer3.get(), n2) * this.NB_WORD_GFqn, this.NB_WORD_GFqn);
        }
        return n2;
    }

    private int gcd_gf2nx(Pointer pointer, int n2, Pointer pointer2, int n3) {
        Pointer pointer3 = new Pointer(this.NB_WORD_GFqn);
        this.buffer = 0;
        while (n3 != 0) {
            if (n3 << 1 > n2) {
                n2 = this.div_r_gf2nx(pointer, n2, pointer2, n3);
            } else {
                this.inv_gf2n(pointer3, pointer2, n3 * this.NB_WORD_GFqn);
                pointer2.set1_gf2n(n3 * this.NB_WORD_GFqn, this.NB_WORD_GFqn);
                this.for_mul(pointer2, pointer3, n3 - 1);
                n2 = this.div_r_monic_gf2nx(pointer, n2, pointer2, n3);
            }
            Pointer pointer4 = pointer;
            pointer = pointer2;
            pointer2 = pointer4;
            int n4 = n2;
            n2 = n3;
            n3 = n4;
            this.buffer = 1 - this.buffer;
        }
        return n2;
    }

    private void for_mul(Pointer pointer, Pointer pointer2, int n2) {
        Pointer pointer3 = new Pointer(pointer, n2 * this.NB_WORD_GFqn);
        for (int i2 = n2; i2 != -1; --i2) {
            this.mul_gf2n(pointer3, pointer3, pointer2);
            pointer3.move(-this.NB_WORD_GFqn);
        }
    }

    private void frobeniusMap_multisqr_HFE_gf2nx(Pointer pointer, SecretKeyHFE.complete_sparse_monic_gf2nx complete_sparse_monic_gf2nx2, Pointer pointer2) {
        int n2;
        Pointer pointer3 = new Pointer();
        Pointer pointer4 = new Pointer(this.HFEDeg * this.NB_WORD_GFqn);
        Pointer pointer5 = new Pointer();
        Pointer pointer6 = new Pointer((this.KX * this.HFEDeg + this.POW_II) * this.NB_WORD_GFqn);
        int n3 = this.POW_II * this.KP - this.HFEDeg;
        Pointer pointer7 = new Pointer(pointer6, this.NB_WORD_GFqn * n3);
        pointer7.copyFrom(pointer2, this.NB_WORD_GFqn);
        this.for_copy_move(pointer7, complete_sparse_monic_gf2nx2);
        this.divsqr_r_HFE_cstdeg_gf2nx(pointer6, n3 - 1 + this.HFEDeg, n3 - 1, 0, complete_sparse_monic_gf2nx2, pointer2);
        for (n2 = this.KP + 1; n2 < this.HFEDeg; ++n2) {
            pointer7.changeIndex(pointer6, this.HFEDeg * this.NB_WORD_GFqn);
            pointer7.setRangeClear(0, this.POW_II * this.NB_WORD_GFqn);
            pointer7.copyFrom(this.POW_II * this.NB_WORD_GFqn, pointer6, 0, this.HFEDeg * this.NB_WORD_GFqn);
            pointer6.changeIndex(pointer7);
            this.divsqr_r_HFE_cstdeg_gf2nx(pointer6, this.POW_II - 1 + this.HFEDeg, this.POW_II - 1, 0, complete_sparse_monic_gf2nx2, pointer2);
        }
        pointer6.indexReset();
        pointer.copyFrom(0, pointer6, ((1 << this.HFEDegI) - this.KP) * this.HFEDeg * this.NB_WORD_GFqn, this.HFEDeg * this.NB_WORD_GFqn);
        for (int i2 = 0; i2 < (this.HFEn - this.HFEDegI - this.II) / this.II; ++i2) {
            this.loop_sqr(pointer4, pointer);
            for (n3 = 1; n3 < this.II; ++n3) {
                this.loop_sqr(pointer4, pointer4);
            }
            pointer5.changeIndex(pointer4, this.KP * this.NB_WORD_GFqn);
            pointer7.changeIndex(pointer6);
            pointer3.changeIndex(pointer);
            for (n2 = 0; n2 < this.HFEDeg; ++n2) {
                this.mul_gf2n(pointer3, pointer7, pointer5);
                pointer3.move(this.NB_WORD_GFqn);
                pointer7.move(this.NB_WORD_GFqn);
            }
            for (n3 = this.KP + 1; n3 < this.HFEDeg; ++n3) {
                pointer5.move(this.NB_WORD_GFqn);
                pointer3.changeIndex(pointer);
                for (n2 = 0; n2 < this.HFEDeg; ++n2) {
                    this.mul_rem_xorrange(pointer3, pointer7, pointer5);
                    pointer3.move(this.NB_WORD_GFqn);
                    pointer7.move(this.NB_WORD_GFqn);
                }
            }
            for (n3 = 0; n3 < this.KP; ++n3) {
                pointer.setXorRange(n3 * this.POW_II * this.NB_WORD_GFqn, pointer4, n3 * this.NB_WORD_GFqn, this.NB_WORD_GFqn);
            }
        }
        this.for_sqr_divsqr(pointer, 0, (this.HFEn - this.HFEDegI) % this.II, complete_sparse_monic_gf2nx2, pointer2);
    }

    private void for_sqr_divsqr(Pointer pointer, int n2, int n3, SecretKeyHFE.complete_sparse_monic_gf2nx complete_sparse_monic_gf2nx2, Pointer pointer2) {
        for (int i2 = n2; i2 < n3; ++i2) {
            this.sqr_gf2nx(pointer, this.HFEDeg - 1);
            this.divsqr_r_HFE_cstdeg_gf2nx(pointer, this.HFEDeg - 1 << 1, this.HFEDeg - 1 << 1, this.HFEDeg, complete_sparse_monic_gf2nx2, pointer2);
        }
    }

    private void loop_sqr(Pointer pointer, Pointer pointer2) {
        for (int i2 = 0; i2 < this.HFEDeg; ++i2) {
            this.sqr_gf2n(pointer, i2 * this.NB_WORD_GFqn, pointer2, i2 * this.NB_WORD_GFqn);
        }
    }

    private void for_copy_move(Pointer pointer, SecretKeyHFE.complete_sparse_monic_gf2nx complete_sparse_monic_gf2nx2) {
        int n2 = 1;
        int n3 = this.NB_WORD_GFqn;
        while (n2 < this.NB_COEFS_HFEPOLY) {
            pointer.move(complete_sparse_monic_gf2nx2.L[n2]);
            pointer.copyFrom(0, complete_sparse_monic_gf2nx2.poly, n2 * this.NB_WORD_GFqn, this.NB_WORD_GFqn);
            ++n2;
            n3 += this.NB_WORD_GFqn;
        }
    }

    private void divsqr_r_HFE_cstdeg_gf2nx(Pointer pointer, int n2, int n3, int n4, SecretKeyHFE.complete_sparse_monic_gf2nx complete_sparse_monic_gf2nx2, Pointer pointer2) {
        Pointer pointer3 = new Pointer(pointer, n2 * this.NB_WORD_GFqn);
        Pointer pointer4 = new Pointer();
        for (int i2 = n3; i2 >= n4; --i2) {
            pointer4.changeIndex(pointer3, -this.HFEDeg * this.NB_WORD_GFqn);
            this.mul_rem_xorrange(pointer4, pointer3, pointer2);
            for (int i3 = 1; i3 < this.NB_COEFS_HFEPOLY; ++i3) {
                pointer4.move(complete_sparse_monic_gf2nx2.L[i3]);
                this.mul_rem_xorrange(pointer4, pointer3, complete_sparse_monic_gf2nx2.poly, i3 * this.NB_WORD_GFqn);
            }
            pointer3.move(-this.NB_WORD_GFqn);
        }
    }

    private void sqr_gf2nx(Pointer pointer, int n2) {
        int n3 = this.NB_WORD_GFqn * n2;
        int n4 = pointer.getIndex();
        pointer.move(n3);
        Pointer pointer2 = new Pointer(pointer, n3);
        for (n3 = 0; n3 < n2; ++n3) {
            this.sqr_gf2n(pointer2, pointer);
            pointer.move(-this.NB_WORD_GFqn);
            pointer2.move(-this.NB_WORD_GFqn);
            pointer2.setRangeClear(0, this.NB_WORD_GFqn);
            pointer2.move(-this.NB_WORD_GFqn);
        }
        this.sqr_gf2n(pointer, pointer);
        pointer.changeIndex(n4);
    }

    int div_r_gf2nx(Pointer pointer, int n2, Pointer pointer2, int n3) {
        Pointer pointer3 = new Pointer(this.NB_WORD_GFqn);
        Pointer pointer4 = new Pointer(this.NB_WORD_GFqn);
        Pointer pointer5 = new Pointer(pointer);
        this.inv_gf2n(pointer4, pointer2, n3 * this.NB_WORD_GFqn);
        while (n2 >= n3 && (n2 = pointer.searchDegree(n2, n3, this.NB_WORD_GFqn)) >= n3) {
            pointer5.changeIndex((n2 - n3) * this.NB_WORD_GFqn);
            this.mul_gf2n(pointer3, pointer, n2 * this.NB_WORD_GFqn, pointer4);
            this.for_mul_rem_xor_move(pointer5, pointer3, pointer2, 0, n3);
            --n2;
        }
        n2 = pointer.searchDegree(n2, 1, this.NB_WORD_GFqn);
        return n2;
    }

    private void div_q_monic_gf2nx(Pointer pointer, int n2, Pointer pointer2, int n3) {
        Pointer pointer3 = new Pointer();
        Pointer pointer4 = new Pointer();
        while (n2 >= n3 && (n2 = pointer.searchDegree(n2, n3, this.NB_WORD_GFqn)) >= n3) {
            pointer3.changeIndex(pointer, n2 * this.NB_WORD_GFqn);
            int n4 = Math.max(0, (n3 << 1) - n2);
            pointer4.changeIndex(pointer, (n2 - n3 + n4) * this.NB_WORD_GFqn);
            this.for_mul_rem_xor_move(pointer4, pointer3, pointer2, n4, n3);
            --n2;
        }
    }

    private int div_r_monic_gf2nx(Pointer pointer, int n2, Pointer pointer2, int n3) {
        Pointer pointer3 = new Pointer();
        Pointer pointer4 = new Pointer();
        while (n2 >= n3 && (n2 = pointer.searchDegree(n2, n3, this.NB_WORD_GFqn)) >= n3) {
            pointer3.changeIndex(pointer, n2 * this.NB_WORD_GFqn);
            pointer4.changeIndex(pointer3, -n3 * this.NB_WORD_GFqn);
            this.for_mul_rem_xor_move(pointer4, pointer3, pointer2, 0, n3);
            --n2;
        }
        if (n2 == -1) {
            ++n2;
        }
        n2 = pointer.searchDegree(n2, 1, this.NB_WORD_GFqn);
        return n2;
    }

    private void for_mul_rem_xor_move(Pointer pointer, Pointer pointer2, Pointer pointer3, int n2, int n3) {
        int n4 = n2;
        int n5 = n2 * this.NB_WORD_GFqn;
        while (n4 < n3) {
            this.mul_rem_xorrange(pointer, pointer2, pointer3, n5);
            pointer.move(this.NB_WORD_GFqn);
            ++n4;
            n5 += this.NB_WORD_GFqn;
        }
    }

    private void inv_gf2n(Pointer pointer, Pointer pointer2, int n2) {
        int n3 = pointer2.getIndex();
        pointer2.move(n2);
        Pointer pointer3 = new Pointer(this.NB_WORD_GFqn);
        pointer.copyFrom(pointer2, this.NB_WORD_GFqn);
        for (int i2 = this.HFEn_1rightmost - 1; i2 != -1; --i2) {
            int n4 = this.HFEn - 1 >>> i2 + 1;
            this.sqr_gf2n(pointer3, pointer);
            for (int i3 = 1; i3 < n4; ++i3) {
                this.sqr_gf2n(pointer3, pointer3);
            }
            this.mul_gf2n(pointer, pointer, pointer3);
            if ((this.HFEn - 1 >>> i2 & 1) == 0) continue;
            this.sqr_gf2n(pointer3, pointer);
            this.mul_gf2n(pointer, pointer2, pointer3);
        }
        this.sqr_gf2n(pointer, pointer);
        pointer2.changeIndex(n3);
    }

    private void convMonic_gf2nx(Pointer pointer, int n2) {
        Pointer pointer2 = new Pointer(this.NB_WORD_GFqn);
        int n3 = pointer.getIndex();
        pointer.move(n2 * this.NB_WORD_GFqn);
        this.inv_gf2n(pointer2, pointer, 0);
        pointer.set1_gf2n(0, this.NB_WORD_GFqn);
        for (int i2 = n2 - 1; i2 != -1; --i2) {
            pointer.move(-this.NB_WORD_GFqn);
            this.mul_gf2n(pointer, pointer, pointer2);
        }
        pointer.changeIndex(n3);
    }

    private void findRootsSplit_gf2nx(Pointer pointer, Pointer pointer2, int n2) {
        int n3;
        int n4;
        if (n2 == 1) {
            pointer.copyFrom(pointer2, this.NB_WORD_GFqn);
            return;
        }
        if ((this.HFEn & 1) != 0 && n2 == 2) {
            this.findRootsSplit2_HT_gf2nx(pointer, pointer2);
            return;
        }
        Pointer pointer3 = new Pointer(((n2 << 1) - 1) * this.NB_WORD_GFqn);
        Pointer pointer4 = new Pointer(n2 * this.NB_WORD_GFqn);
        Pointer pointer5 = new Pointer((n2 + 1) * this.NB_WORD_GFqn);
        Pointer pointer6 = new Pointer(this.NB_WORD_GFqn);
        do {
            pointer3.setRangeClear(0, ((n2 << 1) - 1) * this.NB_WORD_GFqn);
            pointer4.setRangeClear(0, n2 * this.NB_WORD_GFqn);
            do {
                pointer4.fillRandom(this.NB_WORD_GFqn, this.random, this.NB_BYTES_GFqn);
                pointer4.setAnd((this.NB_WORD_GFqn << 1) - 1, this.MASK_GF2n);
            } while (pointer4.is0_gf2n(this.NB_WORD_GFqn, this.NB_WORD_GFqn) != 0);
            pointer5.copyFrom(pointer2, (n2 + 1) * this.NB_WORD_GFqn);
            this.traceMap_gf2nx(pointer4, pointer3, pointer5, n2);
            int n5 = pointer4.searchDegree(n2 - 1, 1, this.NB_WORD_GFqn);
            n4 = this.gcd_gf2nx(pointer5, n2, pointer4, n5);
            n3 = this.buffer;
        } while (n4 == 0 || n4 == n2);
        if (n3 != 0) {
            pointer4.swap(pointer5);
        }
        this.inv_gf2n(pointer6, pointer5, n4 * this.NB_WORD_GFqn);
        pointer5.set1_gf2n(n4 * this.NB_WORD_GFqn, this.NB_WORD_GFqn);
        this.for_mul(pointer5, pointer6, n4 - 1);
        this.div_q_monic_gf2nx(pointer2, n2, pointer5, n4);
        this.findRootsSplit_gf2nx(pointer, pointer5, n4);
        this.findRootsSplit_gf2nx(new Pointer(pointer, n4 * this.NB_WORD_GFqn), new Pointer(pointer2, n4 * this.NB_WORD_GFqn), n2 - n4);
    }

    void findRootsSplit2_HT_gf2nx(Pointer pointer, Pointer pointer2) {
        Pointer pointer3 = new Pointer(this.NB_WORD_GFqn);
        Pointer pointer4 = new Pointer(this.NB_WORD_GFqn);
        int n2 = pointer2.getIndex();
        this.sqr_gf2n(pointer3, 0, pointer2, this.NB_WORD_GFqn);
        this.inv_gf2n(pointer, pointer3, 0);
        this.mul_gf2n(pointer3, pointer2, pointer);
        this.findRootsSplit_x2_x_c_HT_gf2nx(pointer4, pointer3);
        pointer2.move(this.NB_WORD_GFqn);
        this.mul_gf2n(pointer, pointer4, pointer2);
        pointer.setRangeFromXor(this.NB_WORD_GFqn, pointer, 0, pointer2, 0, this.NB_WORD_GFqn);
        pointer2.changeIndex(n2);
    }

    void findRootsSplit_x2_x_c_HT_gf2nx(Pointer pointer, Pointer pointer2) {
        Pointer pointer3 = new Pointer(this.NB_WORD_GFqn);
        int n2 = this.HFEn + 1 >>> 1;
        pointer.copyFrom(pointer2, this.NB_WORD_GFqn);
        int n3 = 1;
        for (int i2 = this.HFEn1h_rightmost; i2 != -1; --i2) {
            n3 <<= 1;
            this.sqr_gf2n(pointer3, pointer);
            for (int i3 = 1; i3 < n3; ++i3) {
                this.sqr_gf2n(pointer3, pointer3);
            }
            pointer.setXorRange(pointer3, this.NB_WORD_GFqn);
            n3 = n2 >>> i2;
            if ((n3 & 1) == 0) continue;
            this.sqr_gf2n(pointer3, pointer);
            this.sqr_gf2n(pointer, pointer3);
            pointer.setXorRange(pointer2, this.NB_WORD_GFqn);
        }
    }

    private void traceMap_gf2nx(Pointer pointer, Pointer pointer2, Pointer pointer3, int n2) {
        int n3 = 1;
        while (1 << n3 < n2) {
            this.sqr_gf2n(pointer, this.NB_WORD_GFqn << n3, pointer, this.NB_WORD_GFqn << n3 - 1);
            ++n3;
        }
        if (n3 < this.HFEn) {
            this.sqr_gf2n(pointer2, this.NB_WORD_GFqn << n3, pointer, this.NB_WORD_GFqn << n3 - 1);
            this.div_r_monic_cst_gf2nx(pointer2, 1 << n3, pointer3, n2);
            pointer.setXorRange(pointer2, n2 * this.NB_WORD_GFqn);
            ++n3;
            while (n3 < this.HFEn) {
                this.sqr_gf2nx(pointer2, n2 - 1);
                this.div_r_monic_cst_gf2nx(pointer2, n2 - 1 << 1, pointer3, n2);
                pointer.setXorRange(pointer2, n2 * this.NB_WORD_GFqn);
                ++n3;
            }
        }
    }

    private void div_r_monic_cst_gf2nx(Pointer pointer, int n2, Pointer pointer2, int n3) {
        Pointer pointer3 = new Pointer();
        int n4 = pointer.getIndex();
        pointer.move(n2 * this.NB_WORD_GFqn);
        while (n2 >= n3) {
            pointer3.changeIndex(pointer, -n3 * this.NB_WORD_GFqn);
            this.for_mul_rem_xor_move(pointer3, pointer, pointer2, 0, n3);
            pointer.move(-this.NB_WORD_GFqn);
            --n2;
        }
        pointer.changeIndex(n4);
    }

    void fast_sort_gf2n(Pointer pointer, int n2) {
        int n3;
        int n4;
        int n5;
        Pointer pointer2 = new Pointer(this.NB_WORD_GFqn);
        Pointer pointer3 = new Pointer(this.NB_WORD_GFqn);
        Pointer pointer4 = new Pointer();
        Pointer pointer5 = new Pointer();
        for (int i2 = n5 = GeMSSUtils.Highest_One(n2 - 1); i2 > 1; i2 >>>= 1) {
            int n6 = n2 / (i2 << 1);
            int n7 = Math.max(0, n2 - (i2 << 1) * n6 - i2);
            pointer4.changeIndex(pointer);
            pointer5.changeIndex(pointer, i2 * this.NB_WORD_GFqn);
            for (n4 = 0; n4 < n6; ++n4) {
                this.for_casct_move(pointer4, pointer5, pointer3, i2, 1);
                pointer4.move(i2 * this.NB_WORD_GFqn);
                pointer5.move(i2 * this.NB_WORD_GFqn);
            }
            this.for_casct_move(pointer4, pointer5, pointer3, n7, 1);
            n4 = 0;
            for (n3 = n5; n3 > i2; n3 >>>= 1) {
                while (n4 < n2 - n3) {
                    if ((n4 & i2) == 0) {
                        pointer5.changeIndex(pointer, (n4 + i2) * this.NB_WORD_GFqn);
                        this.copy_for_casct(pointer2, pointer5, pointer, pointer4, pointer3, n3, n4);
                        pointer5.copyFrom(pointer2, this.NB_WORD_GFqn);
                    }
                    ++n4;
                }
            }
        }
        pointer4.changeIndex(pointer);
        pointer5.changeIndex(pointer, this.NB_WORD_GFqn);
        this.for_casct_move(pointer4, pointer5, pointer3, n2 - 1, 2);
        pointer5.changeIndex(pointer, this.NB_WORD_GFqn);
        n4 = 0;
        for (n3 = n5; n3 > 1; n3 >>>= 1) {
            while (n4 < n2 - n3) {
                this.copy_for_casct(pointer2, pointer5, pointer, pointer4, pointer3, n3, n4);
                pointer5.copyFrom(pointer2, this.NB_WORD_GFqn);
                pointer5.move(this.NB_WORD_GFqn << 1);
                n4 += 2;
            }
        }
    }

    private void copy_for_casct(Pointer pointer, Pointer pointer2, Pointer pointer3, Pointer pointer4, Pointer pointer5, int n2, int n3) {
        pointer.copyFrom(pointer2, this.NB_WORD_GFqn);
        for (int i2 = n2; i2 > 1; i2 >>>= 1) {
            pointer4.changeIndex(pointer3, (n3 + i2) * this.NB_WORD_GFqn);
            this.CMP_AND_SWAP_CST_TIME(pointer, pointer4, pointer5);
        }
    }

    private void for_casct_move(Pointer pointer, Pointer pointer2, Pointer pointer3, int n2, int n3) {
        int n4 = this.NB_WORD_GFqn * n3;
        for (int i2 = 0; i2 < n2; i2 += n3) {
            this.CMP_AND_SWAP_CST_TIME(pointer, pointer2, pointer3);
            pointer.move(n4);
            pointer2.move(n4);
        }
    }

    private void CMP_AND_SWAP_CST_TIME(Pointer pointer, Pointer pointer2, Pointer pointer3) {
        long l2;
        int n2;
        long l3 = 0L;
        long l4 = 0L;
        for (n2 = this.NB_WORD_GFqn - 1; n2 > 0; --n2) {
            l2 = pointer2.get(n2) ^ pointer.get(n2);
            l2 = GeMSSUtils.ORBITS_UINT(l2);
            l4 += (l3 |= l2);
        }
        l3 = 0L;
        for (n2 = 0; n2 < this.NB_WORD_GFqn; ++n2) {
            l2 = (long)n2 ^ l4;
            l2 = GeMSSUtils.NORBITS_UINT(l2);
            l3 |= -l2 & GeMSSUtils.CMP_LT_UINT(pointer2.get(n2), pointer.get(n2));
        }
        pointer3.setRangeFromXorAndMask_xor(pointer, pointer2, -l3, this.NB_WORD_GFqn);
    }

    public void compress_signHFE(byte[] byArray, Pointer pointer) {
        byte[] byArray2 = pointer.toBytes(pointer.getLength() << 3);
        System.arraycopy(byArray2, 0, byArray, 0, this.NB_BYTES_GFqnv);
        int n2 = this.HFEnv;
        int n3 = (this.NB_WORD_GF2nv << 3) + (this.HFEmq8 & 7);
        for (int i2 = 1; i2 < this.NB_ITE; ++i2) {
            int n4;
            int n5 = Math.min(this.HFEDELTA + this.HFEv, 8 - (n2 & 7) & 7);
            if ((n2 & 7) != 0) {
                if (this.HFEmr8 != 0) {
                    int n6 = n2 >>> 3;
                    byArray[n6] = (byte)(byArray[n6] ^ (byArray2[n3] & 0xFF) >>> this.HFEmr8 << (n2 & 7));
                    int n7 = n5 - this.VAL_BITS_M;
                    if (n7 >= 0) {
                        ++n3;
                    }
                    if (n7 > 0) {
                        int n8 = (n2 += this.VAL_BITS_M) >>> 3;
                        byArray[n8] = (byte)(byArray[n8] ^ (byArray2[n3] & 0xFF) << (n2 & 7));
                        n2 += n7;
                    } else {
                        n2 += n5;
                    }
                } else {
                    int n9 = n2 >>> 3;
                    byArray[n9] = (byte)(byArray[n9] ^ (byArray2[n3] & 0xFF) << (n2 & 7));
                    n2 += n5;
                }
            }
            int n10 = this.HFEDELTA + this.HFEv - n5;
            int n11 = this.HFEm + n5 & 7;
            if (n11 != 0) {
                for (n4 = 0; n4 < n10 - 1 >>> 3; ++n4) {
                    byArray[n2 >>> 3] = (byte)((byArray2[n3] & 0xFF) >>> n11 ^ (byArray2[++n3] & 0xFF) << 8 - n11);
                    n2 += 8;
                }
                byArray[n2 >>> 3] = (byte)((byArray2[n3++] & 0xFF) >>> n11);
                if ((n10 = (n10 + 7 & 7) + 1) > 8 - n11) {
                    int n12 = n2 >>> 3;
                    byArray[n12] = (byte)(byArray[n12] ^ (byte)((byArray2[n3++] & 0xFF) << 8 - n11));
                }
                n2 += n10;
            } else {
                for (n4 = 0; n4 < n10 + 7 >>> 3; ++n4) {
                    byArray[n2 >>> 3] = byArray2[n3++];
                    n2 += 8;
                }
                n2 -= 8 - (n10 & 7) & 7;
            }
            n3 += (8 - (this.NB_BYTES_GFqnv & 7) & 7) + (this.HFEmq8 & 7);
        }
    }

    void convMQS_one_to_last_mr8_equations_gf2(byte[] byArray, PointerUnion pointerUnion) {
        int n2 = 0;
        pointerUnion.moveNextBytes(this.HFEmq8);
        PointerUnion pointerUnion2 = new PointerUnion(pointerUnion);
        int n3 = this.NB_MONOMIAL_PK >>> 3;
        for (int i2 = 0; i2 < this.HFEmr8; ++i2) {
            int n4;
            pointerUnion2.changeIndex(pointerUnion);
            for (int i3 = 0; i3 < n3; ++i3) {
                int n5 = pointerUnion2.getByte() >>> i2 & 1;
                pointerUnion2.moveNextBytes(this.NB_BYTES_GFqm);
                for (n4 = 1; n4 < 8; ++n4) {
                    n5 ^= (pointerUnion2.getByte() >>> i2 & 1) << n4;
                    pointerUnion2.moveNextBytes(this.NB_BYTES_GFqm);
                }
                byArray[n2++] = (byte)n5;
            }
            if (this.HFENr8 == 0) continue;
            long l2 = pointerUnion2.getWithCheck() >>> i2 & 1L;
            pointerUnion2.moveNextBytes(this.NB_BYTES_GFqm);
            for (n4 = 1; n4 < this.HFENr8; ++n4) {
                l2 ^= (pointerUnion2.getWithCheck() >>> i2 & 1L) << n4;
                pointerUnion2.moveNextBytes(this.NB_BYTES_GFqm);
            }
            byArray[n2++] = (byte)l2;
        }
    }

    void convMQ_UL_gf2(byte[] byArray, byte[] byArray2, int n2) {
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3 = this.ACCESS_last_equations8 + i2 * this.NB_BYTES_EQUATION;
            int n4 = i2 * this.NB_BYTES_EQUATION;
            this.for_setPK(byArray, byArray2, n3, n4, this.HFEnv + 1);
        }
    }

    private int for_setPK(byte[] byArray, byte[] byArray2, int n2, int n3, int n4) {
        byArray[n2] = (byte)(byArray2[n3] & 3);
        int n5 = 2;
        for (int i2 = 2; i2 < n4; ++i2) {
            n5 = this.setPK(byArray, byArray2, i2, n2, n3, n5, this.HFEnv - 1, this.HFEnv - i2);
        }
        return n5;
    }

    private int setPK(byte[] byArray, byte[] byArray2, int n2, int n3, int n4, int n5, int n6, int n7) {
        int n8 = n6;
        while (n8 >= n7) {
            int n9 = n3 + (n5 >>> 3);
            byArray[n9] = (byte)(byArray[n9] ^ (byArray2[n4 + (n2 >>> 3)] >>> (n2 & 7) & 1) << (n5 & 7));
            n2 += n8;
            --n8;
            ++n5;
        }
        this.buffer = n2;
        return n5;
    }

    void convMQS_one_eq_to_hybrid_rep8_comp_gf2(byte[] byArray, PointerUnion pointerUnion, byte[] byArray2) {
        int n2 = 0;
        this.convMQ_UL_gf2(byArray, byArray2, this.HFEmr8);
        for (int i2 = 0; i2 < this.NB_MONOMIAL_PK; ++i2) {
            n2 = pointerUnion.toBytesMove(byArray, n2, this.HFEmq8);
            if (this.HFEmr8 == 0) continue;
            pointerUnion.moveNextByte();
        }
    }

    void convMQS_one_eq_to_hybrid_rep8_uncomp_gf2(byte[] byArray, PointerUnion pointerUnion, byte[] byArray2) {
        int n2 = this.HFEmr8 - 1;
        long l2 = 0L;
        this.convMQ_UL_gf2(byArray, byArray2, n2);
        int n3 = this.ACCESS_last_equations8 + n2 * this.NB_BYTES_EQUATION;
        int n4 = n2 * this.NB_BYTES_EQUATION;
        int n5 = this.for_setPK(byArray, byArray2, n3, n4, this.HFEnv);
        int n6 = this.HFEnv;
        n5 = this.setPK(byArray, byArray2, n6, n3, n4, n5, this.HFEnv - 1, this.LOST_BITS);
        n2 = this.LOST_BITS - 1;
        n6 = this.buffer;
        while (n2 >= 0) {
            l2 ^= (long)(byArray2[n4 + (n6 >>> 3)] >>> (n6 & 7) & 1) << this.LOST_BITS - 1 - n2;
            n6 += n2;
            --n2;
            ++n5;
        }
        n3 = this.ACCESS_last_equations8 - 1;
        for (n2 = 0; n2 < this.HFEmr8 - 1; ++n2) {
            int n7 = n3 += this.NB_BYTES_EQUATION;
            byArray[n7] = (byte)(byArray[n7] ^ (byte)(l2 >>> n2 * this.HFENr8c) << this.HFENr8);
        }
        pointerUnion.indexReset();
        n3 = 0;
        for (int i2 = 0; i2 < this.NB_MONOMIAL_PK; ++i2) {
            n3 = pointerUnion.toBytesMove(byArray, n3, this.HFEmq8);
            pointerUnion.moveNextByte();
        }
    }

    public int crypto_sign_open(byte[] byArray, byte[] byArray2, byte[] byArray3) {
        int n2;
        Pointer pointer;
        PointerUnion pointerUnion = new PointerUnion(byArray);
        long l2 = 0L;
        if (this.HFENr8 != 0 && this.HFEmr8 > 1) {
            pointer = new PointerUnion(pointerUnion);
            ((PointerUnion)pointer).moveNextBytes(this.ACCESS_last_equations8 - 1);
            for (n2 = 0; n2 < this.HFEmr8 - 1; ++n2) {
                ((PointerUnion)pointer).moveNextBytes(this.NB_BYTES_EQUATION);
                l2 ^= ((long)((PointerUnion)pointer).getByte() & 0xFFL) >>> this.HFENr8 << n2 * this.HFENr8c;
            }
        }
        if (this.HFEmr8 != 0) {
            pointer = new Pointer(1 + this.NB_WORD_UNCOMP_EQ * this.HFEmr8);
            long l3 = 0L;
            PointerUnion pointerUnion2 = new PointerUnion(pointerUnion);
            for (n2 = 0; n2 < this.HFEmr8 - 1; ++n2) {
                pointerUnion2.setByteIndex(this.ACCESS_last_equations8 + n2 * this.NB_BYTES_EQUATION);
                l3 ^= this.convMQ_uncompressL_gf2(new Pointer(pointer, 1 + n2 * this.NB_WORD_UNCOMP_EQ), pointerUnion2) << n2;
            }
            pointerUnion2.setByteIndex(this.ACCESS_last_equations8 + n2 * this.NB_BYTES_EQUATION);
            l3 ^= this.convMQ_last_uncompressL_gf2(new Pointer(pointer, 1 + n2 * this.NB_WORD_UNCOMP_EQ), pointerUnion2) << n2;
            if (this.HFENr8 != 0) {
                if (this.HFEnvr == 0) {
                    pointer.setXor((n2 + 1) * this.NB_WORD_UNCOMP_EQ, l2 << 64 - this.LOST_BITS);
                } else if (this.HFEnvr > this.LOST_BITS) {
                    pointer.setXor((n2 + 1) * this.NB_WORD_UNCOMP_EQ, l2 << this.HFEnvr - this.LOST_BITS);
                } else if (this.HFEnvr == this.LOST_BITS) {
                    pointer.set((n2 + 1) * this.NB_WORD_UNCOMP_EQ, l2);
                } else {
                    pointer.setXor((n2 + 1) * this.NB_WORD_UNCOMP_EQ - 1, l2 << 64 - (this.LOST_BITS - this.HFEnvr));
                    pointer.set((n2 + 1) * this.NB_WORD_UNCOMP_EQ, l2 >>> this.LOST_BITS - this.HFEnvr);
                }
            }
            pointer.set(l3 << this.HFEmr - this.HFEmr8);
            return this.sign_openHFE_huncomp_pk(byArray2, byArray2.length, byArray3, pointerUnion, new PointerUnion(pointer));
        }
        pointer = new Pointer(this.SIZE_SIGN_UNCOMPRESSED);
        Pointer pointer2 = new Pointer(this.NB_WORD_GF2nv);
        Pointer pointer3 = new Pointer(pointer2);
        Pointer pointer4 = new Pointer(this.SIZE_DIGEST_UINT);
        pointer.fill(0, byArray3, 0, this.NB_BYTES_GFqnv);
        byte[] byArray4 = new byte[64];
        this.getSHA3Hash(pointer4, 0, 64, byArray2, 0, byArray2.length, byArray4);
        this.evalMQSnocst8_quo_gf2(pointer3, pointer, pointerUnion);
        return pointer3.isEqual_nocst_gf2(pointer4, this.NB_WORD_GF2m);
    }

    void changeVariablesMQS64_gf2(Pointer pointer, Pointer pointer2) {
        int n2;
        int n3;
        int n4;
        Pointer pointer3 = new Pointer();
        Pointer pointer4 = new Pointer(this.HFEnv * this.HFEnv * this.NB_WORD_GFqn);
        Pointer pointer5 = new Pointer(pointer, this.NB_WORD_GFqn);
        Pointer pointer6 = new Pointer(pointer4);
        Pointer pointer7 = new Pointer(pointer2);
        for (int i2 = 0; i2 < this.HFEnv; ++i2) {
            int n5;
            pointer3.changeIndex(pointer5);
            for (n4 = 0; n4 < this.HFEnvq; ++n4) {
                for (n5 = 0; n5 < 64; ++n5) {
                    this.LOOPKR(pointer3, pointer6, pointer7.get() >>> n5, n5, 64);
                    this.LOOPK_COMPLETE(pointer6, pointer7, pointer3, 1, this.HFEnvq - n4);
                }
                pointer7.moveIncremental();
            }
            if (this.HFEnvr == 0) continue;
            for (n5 = 0; n5 < this.HFEnvr; ++n5) {
                this.LOOPKR(pointer3, pointer6, pointer7.get() >>> n5, n5, this.HFEnvr);
                pointer6.move(this.NB_WORD_GFqn);
            }
            pointer7.moveIncremental();
        }
        pointer5.changeIndex(pointer4);
        pointer6.changeIndex(pointer, this.NB_WORD_GFqn);
        Pointer pointer8 = new Pointer(pointer2);
        for (n3 = 0; n3 < this.HFEnvq; ++n3) {
            for (n2 = 0; n2 < 64; ++n2) {
                pointer7.changeIndex(pointer8);
                this.LOOPIR_INIT(pointer6, pointer3, pointer5, pointer7, n2, 64);
                for (n4 = n3 + 1; n4 < this.HFEnvq; ++n4) {
                    this.LOOPIR_INIT(pointer6, pointer3, pointer5, pointer7, 0, 64);
                }
                if (this.HFEnvr != 0) {
                    this.LOOPIR_INIT(pointer6, pointer3, pointer5, pointer7, 0, this.HFEnvr);
                }
                pointer5.changeIndex(pointer3);
                pointer8.move(this.NB_WORD_GF2nv);
            }
        }
        if (this.HFEnvr != 0) {
            for (n2 = 0; n2 < this.HFEnvr; ++n2) {
                pointer7.changeIndex(pointer8);
                pointer3.changeIndex(pointer5);
                this.LOOPIR_INIT(pointer6, pointer3, pointer5, pointer7, n2, this.HFEnvr);
                pointer5.changeIndex(pointer3);
                pointer8.move(this.NB_WORD_GF2nv);
            }
        }
        pointer5.changeIndex(pointer4);
        pointer6.changeIndex(pointer, this.NB_WORD_GFqn);
        pointer7.changeIndex(pointer2);
        for (n3 = 0; n3 < this.HFEnvq; ++n3) {
            for (n2 = 0; n2 < 64; ++n2) {
                pointer6.move(this.NB_WORD_GFqn);
                pointer5.move(this.HFEnv * this.NB_WORD_GFqn);
                pointer3.changeIndex(pointer5);
                this.LOOPIR_LOOPK_COMPLETE(pointer6, pointer7, pointer3, n2 + 1, 64);
                for (n4 = n3 + 1; n4 < this.HFEnvq; ++n4) {
                    this.LOOPIR_LOOPK_COMPLETE(pointer6, pointer7, pointer3, 0, 64);
                }
                if (this.HFEnvr != 0) {
                    this.LOOPIR_LOOPK_COMPLETE(pointer6, pointer7, pointer3, 0, this.HFEnvr);
                }
                pointer7.move(this.NB_WORD_GF2nv);
            }
        }
        if (this.HFEnvr != 0) {
            for (n2 = 0; n2 < this.HFEnvr - 1; ++n2) {
                pointer6.move(this.NB_WORD_GFqn);
                pointer5.move(this.HFEnv * this.NB_WORD_GFqn);
                pointer3.changeIndex(pointer5);
                this.LOOPIR_LOOPK_COMPLETE(pointer6, pointer7, pointer3, n2 + 1, this.HFEnvr);
                pointer7.move(this.NB_WORD_GF2nv);
            }
        }
        pointer.indexReset();
        pointer2.indexReset();
    }

    private void LOOPIR_INIT(Pointer pointer, Pointer pointer2, Pointer pointer3, Pointer pointer4, int n2, int n3) {
        for (int i2 = n2; i2 < n3; ++i2) {
            pointer.setRangeClear(0, this.NB_WORD_GFqn);
            pointer2.changeIndex(pointer3);
            this.LOOPK_COMPLETE(pointer, pointer4, pointer2, 0, this.HFEnvq);
            pointer4.move(this.NB_WORD_GF2nv);
        }
    }

    private void LOOPIR_LOOPK_COMPLETE(Pointer pointer, Pointer pointer2, Pointer pointer3, int n2, int n3) {
        for (int i2 = n2; i2 < n3; ++i2) {
            this.LOOPK_COMPLETE(pointer, pointer2, pointer3, 0, this.HFEnvq);
        }
    }

    private void LOOPK_COMPLETE(Pointer pointer, Pointer pointer2, Pointer pointer3, int n2, int n3) {
        for (int i2 = n2; i2 < n3; ++i2) {
            this.LOOPKR(pointer3, pointer, pointer2.get(i2), 0, 64);
        }
        if (this.HFEnvr != 0) {
            this.LOOPKR(pointer3, pointer, pointer2.get(n3), 0, this.HFEnvr);
        }
        pointer.move(this.NB_WORD_GFqn);
    }

    private void LOOPKR(Pointer pointer, Pointer pointer2, long l2, int n2, int n3) {
        for (int i2 = n2; i2 < n3; ++i2) {
            pointer2.setXorRangeAndMaskMove(pointer, this.NB_WORD_GFqn, -(l2 & 1L));
            l2 >>>= 1;
        }
    }

    int interpolateHFE_FS_ref(Pointer pointer, Pointer pointer2, Pointer pointer3) {
        int n2;
        Pointer pointer4 = new Pointer(this.NB_WORD_GF2nv);
        Pointer pointer5 = new Pointer();
        Pointer pointer6 = new Pointer();
        Pointer pointer7 = new Pointer(this.HFEnv * this.NB_WORD_GFqn);
        pointer.copyFrom(pointer2, this.NB_WORD_GFqn);
        Pointer pointer8 = new Pointer(pointer3);
        Pointer pointer9 = new Pointer(pointer7);
        for (n2 = 0; n2 < this.HFEnv; ++n2) {
            this.evalHFEv_gf2nx(pointer9, pointer2, pointer8);
            pointer9.move(this.NB_WORD_GFqn);
            pointer8.move(this.NB_WORD_GF2nv);
        }
        pointer8.changeIndex(pointer3);
        pointer9.changeIndex(pointer7);
        for (n2 = 0; n2 < this.HFEnv; ++n2) {
            pointer.move(this.NB_WORD_GFqn);
            pointer9.setXorRange(pointer2, this.NB_WORD_GFqn);
            pointer.copyFrom(pointer9, this.NB_WORD_GFqn);
            pointer5.changeIndex(pointer9);
            pointer6.changeIndex(pointer8);
            for (int i2 = n2 + 1; i2 < this.HFEnv; ++i2) {
                pointer.move(this.NB_WORD_GFqn);
                pointer5.move(this.NB_WORD_GFqn);
                pointer6.move(this.NB_WORD_GF2nv);
                pointer4.setRangeFromXor(pointer8, pointer6, this.NB_WORD_GF2nv);
                this.evalHFEv_gf2nx(pointer, pointer2, pointer4);
                pointer.setXorRangeXor(0, pointer9, 0, pointer5, 0, this.NB_WORD_GFqn);
            }
            pointer9.move(this.NB_WORD_GFqn);
            pointer8.move(this.NB_WORD_GF2nv);
        }
        pointer.indexReset();
        return 0;
    }

    void evalHFEv_gf2nx(Pointer pointer, Pointer pointer2, Pointer pointer3) {
        int n2;
        Pointer pointer4 = new Pointer(this.NB_WORD_MUL);
        Pointer pointer5 = new Pointer(this.NB_WORD_MUL);
        Pointer pointer6 = new Pointer((this.HFEDegI + 1) * this.NB_WORD_GFqn);
        Pointer pointer7 = new Pointer();
        int n3 = pointer2.getIndex();
        Pointer pointer8 = new Pointer(this.NB_WORD_GFqv);
        Pointer pointer9 = new Pointer(pointer6, this.NB_WORD_GFqn);
        pointer6.copyFrom(pointer3, this.NB_WORD_GFqn);
        pointer6.setAnd(this.NB_WORD_GFqn - 1, this.MASK_GF2n);
        for (n2 = 1; n2 <= this.HFEDegI; ++n2) {
            this.sqr_gf2n(pointer9, 0, pointer9, -this.NB_WORD_GFqn);
            pointer9.move(this.NB_WORD_GFqn);
        }
        int n4 = this.NB_WORD_GFqn + this.NB_WORD_GFqv == this.NB_WORD_GF2nv ? this.NB_WORD_GFqv : this.NB_WORD_GFqv - 1;
        pointer8.setRangeRotate(0, pointer3, this.NB_WORD_GFqn - 1, n4, 64 - this.HFEnr);
        if (this.NB_WORD_GFqn + this.NB_WORD_GFqv != this.NB_WORD_GF2nv) {
            pointer8.set(n4, pointer3.get(this.NB_WORD_GFqn - 1 + n4) >>> this.HFEnr);
        }
        this.evalMQSv_unrolled_gf2(pointer4, pointer8, pointer2);
        pointer2.move(this.MQv_GFqn_SIZE);
        this.vmpv_xorrange_move(pointer5, pointer8, pointer2);
        pointer9.changeIndex(pointer6);
        this.mul_xorrange(pointer4, pointer9, pointer5);
        for (n2 = 1; n2 < this.HFEDegI; ++n2) {
            this.vmpv_xorrange_move(pointer5, pointer8, pointer2);
            pointer5.setRangeClear(this.NB_WORD_GFqn, this.NB_WORD_MMUL - this.NB_WORD_GFqn);
            pointer7.changeIndex(pointer9);
            this.for_mul_xorrange_move(pointer5, pointer2, pointer7, n2);
            this.rem_gf2n(pointer5, 0, pointer5);
            this.mul_xorrange(pointer4, pointer7, pointer5);
        }
        this.vmpv_xorrange_move(pointer5, pointer8, pointer2);
        pointer7.changeIndex(pointer9);
        if (this.HFEDegJ != 0) {
            pointer5.setRangeClear(this.NB_WORD_GFqn, this.NB_WORD_MMUL - this.NB_WORD_GFqn);
            this.for_mul_xorrange_move(pointer5, pointer2, pointer7, this.HFEDegJ);
            pointer5.setXorRange(pointer7, this.NB_WORD_GFqn);
            this.rem_gf2n(pointer5, 0, pointer5);
        } else {
            pointer5.setRangeFromXor(pointer5, pointer7, this.NB_WORD_GFqn);
        }
        pointer9.move(this.HFEDegI * this.NB_WORD_GFqn);
        this.mul_xorrange(pointer4, pointer9, pointer5);
        this.rem_gf2n(pointer, 0, pointer4);
        pointer2.changeIndex(n3);
    }

    private void vmpv_xorrange_move(Pointer pointer, Pointer pointer2, Pointer pointer3) {
        this.vecMatProduct(pointer, pointer2, new Pointer(pointer3, this.NB_WORD_GFqn), FunctionParams.V);
        pointer.setXorRange(pointer3, this.NB_WORD_GFqn);
        pointer3.move(this.MLv_GFqn_SIZE);
    }

    private static long remainderUnsigned(long l2, long l3) {
        if (l2 > 0L && l3 > 0L) {
            return l2 % l3;
        }
        return new BigInteger(1, Pack.longToBigEndian(l2)).mod(new BigInteger(1, Pack.longToBigEndian(l3))).longValue();
    }

    static enum FunctionParams {
        NV,
        V,
        N,
        M;

    }
}

