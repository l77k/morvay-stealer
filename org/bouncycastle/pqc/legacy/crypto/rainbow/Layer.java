/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.rainbow;

import java.security.SecureRandom;
import org.bouncycastle.pqc.legacy.crypto.rainbow.util.GF2Field;
import org.bouncycastle.pqc.legacy.crypto.rainbow.util.RainbowUtil;
import org.bouncycastle.util.Arrays;

public class Layer {
    private int vi;
    private int viNext;
    private int oi;
    private short[][][] coeff_alpha;
    private short[][][] coeff_beta;
    private short[][] coeff_gamma;
    private short[] coeff_eta;

    public Layer(byte by, byte by2, short[][][] sArray, short[][][] sArray2, short[][] sArray3, short[] sArray4) {
        this.vi = by & 0xFF;
        this.viNext = by2 & 0xFF;
        this.oi = this.viNext - this.vi;
        this.coeff_alpha = sArray;
        this.coeff_beta = sArray2;
        this.coeff_gamma = sArray3;
        this.coeff_eta = sArray4;
    }

    public Layer(int n2, int n3, SecureRandom secureRandom) {
        int n4;
        int n5;
        int n6;
        this.vi = n2;
        this.viNext = n3;
        this.oi = n3 - n2;
        this.coeff_alpha = new short[this.oi][this.oi][this.vi];
        this.coeff_beta = new short[this.oi][this.vi][this.vi];
        this.coeff_gamma = new short[this.oi][this.viNext];
        this.coeff_eta = new short[this.oi];
        int n7 = this.oi;
        for (n6 = 0; n6 < n7; ++n6) {
            for (n5 = 0; n5 < this.oi; ++n5) {
                for (n4 = 0; n4 < this.vi; ++n4) {
                    this.coeff_alpha[n6][n5][n4] = (short)(secureRandom.nextInt() & 0xFF);
                }
            }
        }
        for (n6 = 0; n6 < n7; ++n6) {
            for (n5 = 0; n5 < this.vi; ++n5) {
                for (n4 = 0; n4 < this.vi; ++n4) {
                    this.coeff_beta[n6][n5][n4] = (short)(secureRandom.nextInt() & 0xFF);
                }
            }
        }
        for (n6 = 0; n6 < n7; ++n6) {
            for (n5 = 0; n5 < this.viNext; ++n5) {
                this.coeff_gamma[n6][n5] = (short)(secureRandom.nextInt() & 0xFF);
            }
        }
        for (n6 = 0; n6 < n7; ++n6) {
            this.coeff_eta[n6] = (short)(secureRandom.nextInt() & 0xFF);
        }
    }

    public short[][] plugInVinegars(short[] sArray) {
        int n2;
        int n3;
        int n4;
        short s2 = 0;
        short[][] sArray2 = new short[this.oi][this.oi + 1];
        short[] sArray3 = new short[this.oi];
        for (n4 = 0; n4 < this.oi; ++n4) {
            for (n3 = 0; n3 < this.vi; ++n3) {
                for (n2 = 0; n2 < this.vi; ++n2) {
                    s2 = GF2Field.multElem(this.coeff_beta[n4][n3][n2], sArray[n3]);
                    s2 = GF2Field.multElem(s2, sArray[n2]);
                    sArray3[n4] = GF2Field.addElem(sArray3[n4], s2);
                }
            }
        }
        for (n4 = 0; n4 < this.oi; ++n4) {
            for (n3 = 0; n3 < this.oi; ++n3) {
                for (n2 = 0; n2 < this.vi; ++n2) {
                    s2 = GF2Field.multElem(this.coeff_alpha[n4][n3][n2], sArray[n2]);
                    sArray2[n4][n3] = GF2Field.addElem(sArray2[n4][n3], s2);
                }
            }
        }
        for (n4 = 0; n4 < this.oi; ++n4) {
            for (n3 = 0; n3 < this.vi; ++n3) {
                s2 = GF2Field.multElem(this.coeff_gamma[n4][n3], sArray[n3]);
                sArray3[n4] = GF2Field.addElem(sArray3[n4], s2);
            }
        }
        for (n4 = 0; n4 < this.oi; ++n4) {
            for (n3 = this.vi; n3 < this.viNext; ++n3) {
                sArray2[n4][n3 - this.vi] = GF2Field.addElem(this.coeff_gamma[n4][n3], sArray2[n4][n3 - this.vi]);
            }
        }
        for (n4 = 0; n4 < this.oi; ++n4) {
            sArray3[n4] = GF2Field.addElem(sArray3[n4], this.coeff_eta[n4]);
        }
        for (n4 = 0; n4 < this.oi; ++n4) {
            sArray2[n4][this.oi] = sArray3[n4];
        }
        return sArray2;
    }

    public int getVi() {
        return this.vi;
    }

    public int getViNext() {
        return this.viNext;
    }

    public int getOi() {
        return this.oi;
    }

    public short[][][] getCoeffAlpha() {
        return this.coeff_alpha;
    }

    public short[][][] getCoeffBeta() {
        return this.coeff_beta;
    }

    public short[][] getCoeffGamma() {
        return this.coeff_gamma;
    }

    public short[] getCoeffEta() {
        return this.coeff_eta;
    }

    public boolean equals(Object object) {
        if (object == null || !(object instanceof Layer)) {
            return false;
        }
        Layer layer = (Layer)object;
        return this.vi == layer.getVi() && this.viNext == layer.getViNext() && this.oi == layer.getOi() && RainbowUtil.equals(this.coeff_alpha, layer.getCoeffAlpha()) && RainbowUtil.equals(this.coeff_beta, layer.getCoeffBeta()) && RainbowUtil.equals(this.coeff_gamma, layer.getCoeffGamma()) && RainbowUtil.equals(this.coeff_eta, layer.getCoeffEta());
    }

    public int hashCode() {
        int n2 = this.vi;
        n2 = n2 * 37 + this.viNext;
        n2 = n2 * 37 + this.oi;
        n2 = n2 * 37 + Arrays.hashCode(this.coeff_alpha);
        n2 = n2 * 37 + Arrays.hashCode(this.coeff_beta);
        n2 = n2 * 37 + Arrays.hashCode(this.coeff_gamma);
        n2 = n2 * 37 + Arrays.hashCode(this.coeff_eta);
        return n2;
    }
}

