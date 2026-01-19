/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.mldsa;

import org.bouncycastle.pqc.crypto.mldsa.MLDSAEngine;
import org.bouncycastle.pqc.crypto.mldsa.Poly;

class PolyVecL {
    Poly[] vec;
    private MLDSAEngine engine;
    private int mode;
    private int polyVecBytes;
    private int dilithiumL;
    private int dilithiumK;

    public PolyVecL(MLDSAEngine mLDSAEngine) {
        this.engine = mLDSAEngine;
        this.mode = mLDSAEngine.getDilithiumMode();
        this.dilithiumL = mLDSAEngine.getDilithiumL();
        this.dilithiumK = mLDSAEngine.getDilithiumK();
        this.vec = new Poly[this.dilithiumL];
        for (int i2 = 0; i2 < this.dilithiumL; ++i2) {
            this.vec[i2] = new Poly(mLDSAEngine);
        }
    }

    public PolyVecL() throws Exception {
        throw new Exception("Requires Parameter");
    }

    public Poly getVectorIndex(int n2) {
        return this.vec[n2];
    }

    public void expandMatrix(byte[] byArray, int n2) {
        for (int i2 = 0; i2 < this.dilithiumL; ++i2) {
            this.vec[i2].uniformBlocks(byArray, (short)((n2 << 8) + i2));
        }
    }

    public void uniformEta(byte[] byArray, short s2) {
        short s3 = s2;
        for (int i2 = 0; i2 < this.dilithiumL; ++i2) {
            short s4 = s3;
            s3 = (short)(s3 + 1);
            this.getVectorIndex(i2).uniformEta(byArray, s4);
        }
    }

    public void copyPolyVecL(PolyVecL polyVecL) {
        for (int i2 = 0; i2 < this.dilithiumL; ++i2) {
            for (int i3 = 0; i3 < 256; ++i3) {
                polyVecL.getVectorIndex(i2).setCoeffIndex(i3, this.getVectorIndex(i2).getCoeffIndex(i3));
            }
        }
    }

    public void polyVecNtt() {
        for (int i2 = 0; i2 < this.dilithiumL; ++i2) {
            this.vec[i2].polyNtt();
        }
    }

    public void uniformGamma1(byte[] byArray, short s2) {
        for (int i2 = 0; i2 < this.dilithiumL; ++i2) {
            this.getVectorIndex(i2).uniformGamma1(byArray, (short)(this.dilithiumL * s2 + i2));
        }
    }

    public void pointwisePolyMontgomery(Poly poly, PolyVecL polyVecL) {
        for (int i2 = 0; i2 < this.dilithiumL; ++i2) {
            this.getVectorIndex(i2).pointwiseMontgomery(poly, polyVecL.getVectorIndex(i2));
        }
    }

    public void invNttToMont() {
        for (int i2 = 0; i2 < this.dilithiumL; ++i2) {
            this.getVectorIndex(i2).invNttToMont();
        }
    }

    public void addPolyVecL(PolyVecL polyVecL) {
        for (int i2 = 0; i2 < this.dilithiumL; ++i2) {
            this.getVectorIndex(i2).addPoly(polyVecL.getVectorIndex(i2));
        }
    }

    public void reduce() {
        for (int i2 = 0; i2 < this.dilithiumL; ++i2) {
            this.getVectorIndex(i2).reduce();
        }
    }

    public boolean checkNorm(int n2) {
        for (int i2 = 0; i2 < this.dilithiumL; ++i2) {
            if (!this.getVectorIndex(i2).checkNorm(n2)) continue;
            return true;
        }
        return false;
    }

    public String toString() {
        String string = "\n[";
        for (int i2 = 0; i2 < this.dilithiumL; ++i2) {
            string = string + "Inner Matrix " + i2 + " " + this.getVectorIndex(i2).toString();
            if (i2 == this.dilithiumL - 1) continue;
            string = string + ",\n";
        }
        string = string + "]";
        return string;
    }

    public String toString(String string) {
        return string + ": " + this.toString();
    }
}

