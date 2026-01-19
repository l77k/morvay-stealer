/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.crystals.dilithium;

import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumEngine;
import org.bouncycastle.pqc.crypto.crystals.dilithium.Poly;

class PolyVecK {
    Poly[] vec;
    private DilithiumEngine engine;
    private int mode;
    private int polyVecBytes;
    private int dilithiumK;
    private int dilithiumL;

    public PolyVecK(DilithiumEngine dilithiumEngine) {
        this.engine = dilithiumEngine;
        this.mode = dilithiumEngine.getDilithiumMode();
        this.dilithiumK = dilithiumEngine.getDilithiumK();
        this.dilithiumL = dilithiumEngine.getDilithiumL();
        this.vec = new Poly[this.dilithiumK];
        for (int i2 = 0; i2 < this.dilithiumK; ++i2) {
            this.vec[i2] = new Poly(dilithiumEngine);
        }
    }

    public PolyVecK() throws Exception {
        throw new Exception("Requires Parameter");
    }

    public Poly getVectorIndex(int n2) {
        return this.vec[n2];
    }

    public void setVectorIndex(int n2, Poly poly) {
        this.vec[n2] = poly;
    }

    public void uniformEta(byte[] byArray, short s2) {
        short s3 = s2;
        for (int i2 = 0; i2 < this.dilithiumK; ++i2) {
            short s4 = s3;
            s3 = (short)(s3 + 1);
            this.getVectorIndex(i2).uniformEta(byArray, s4);
        }
    }

    public void reduce() {
        for (int i2 = 0; i2 < this.dilithiumK; ++i2) {
            this.getVectorIndex(i2).reduce();
        }
    }

    public void invNttToMont() {
        for (int i2 = 0; i2 < this.dilithiumK; ++i2) {
            this.getVectorIndex(i2).invNttToMont();
        }
    }

    public void addPolyVecK(PolyVecK polyVecK) {
        for (int i2 = 0; i2 < this.dilithiumK; ++i2) {
            this.getVectorIndex(i2).addPoly(polyVecK.getVectorIndex(i2));
        }
    }

    public void conditionalAddQ() {
        for (int i2 = 0; i2 < this.dilithiumK; ++i2) {
            this.getVectorIndex(i2).conditionalAddQ();
        }
    }

    public void power2Round(PolyVecK polyVecK) {
        for (int i2 = 0; i2 < this.dilithiumK; ++i2) {
            this.getVectorIndex(i2).power2Round(polyVecK.getVectorIndex(i2));
        }
    }

    public void polyVecNtt() {
        for (int i2 = 0; i2 < this.dilithiumK; ++i2) {
            this.vec[i2].polyNtt();
        }
    }

    public void decompose(PolyVecK polyVecK) {
        for (int i2 = 0; i2 < this.dilithiumK; ++i2) {
            this.getVectorIndex(i2).decompose(polyVecK.getVectorIndex(i2));
        }
    }

    public byte[] packW1() {
        byte[] byArray = new byte[this.dilithiumK * this.engine.getDilithiumPolyW1PackedBytes()];
        for (int i2 = 0; i2 < this.dilithiumK; ++i2) {
            System.arraycopy(this.getVectorIndex(i2).w1Pack(), 0, byArray, i2 * this.engine.getDilithiumPolyW1PackedBytes(), this.engine.getDilithiumPolyW1PackedBytes());
        }
        return byArray;
    }

    public void pointwisePolyMontgomery(Poly poly, PolyVecK polyVecK) {
        for (int i2 = 0; i2 < this.dilithiumK; ++i2) {
            this.getVectorIndex(i2).pointwiseMontgomery(poly, polyVecK.getVectorIndex(i2));
        }
    }

    public void subtract(PolyVecK polyVecK) {
        for (int i2 = 0; i2 < this.dilithiumK; ++i2) {
            this.getVectorIndex(i2).subtract(polyVecK.getVectorIndex(i2));
        }
    }

    public boolean checkNorm(int n2) {
        for (int i2 = 0; i2 < this.dilithiumK; ++i2) {
            if (!this.getVectorIndex(i2).checkNorm(n2)) continue;
            return true;
        }
        return false;
    }

    public int makeHint(PolyVecK polyVecK, PolyVecK polyVecK2) {
        int n2 = 0;
        for (int i2 = 0; i2 < this.dilithiumK; ++i2) {
            n2 += this.getVectorIndex(i2).polyMakeHint(polyVecK.getVectorIndex(i2), polyVecK2.getVectorIndex(i2));
        }
        return n2;
    }

    public void useHint(PolyVecK polyVecK, PolyVecK polyVecK2) {
        for (int i2 = 0; i2 < this.dilithiumK; ++i2) {
            this.getVectorIndex(i2).polyUseHint(polyVecK.getVectorIndex(i2), polyVecK2.getVectorIndex(i2));
        }
    }

    public void shiftLeft() {
        for (int i2 = 0; i2 < this.dilithiumK; ++i2) {
            this.getVectorIndex(i2).shiftLeft();
        }
    }

    public String toString() {
        String string = "[";
        for (int i2 = 0; i2 < this.dilithiumK; ++i2) {
            string = string + i2 + " " + this.getVectorIndex(i2).toString();
            if (i2 == this.dilithiumK - 1) continue;
            string = string + ",\n";
        }
        string = string + "]";
        return string;
    }

    public String toString(String string) {
        return string + ": " + this.toString();
    }
}

