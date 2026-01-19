/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.crystals.dilithium;

import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumEngine;
import org.bouncycastle.pqc.crypto.crystals.dilithium.PolyVecK;
import org.bouncycastle.pqc.crypto.crystals.dilithium.PolyVecL;

class PolyVecMatrix {
    private final int dilithiumK;
    private final int dilithiumL;
    private final PolyVecL[] mat;

    public PolyVecMatrix(DilithiumEngine dilithiumEngine) {
        this.dilithiumK = dilithiumEngine.getDilithiumK();
        this.dilithiumL = dilithiumEngine.getDilithiumL();
        this.mat = new PolyVecL[this.dilithiumK];
        for (int i2 = 0; i2 < this.dilithiumK; ++i2) {
            this.mat[i2] = new PolyVecL(dilithiumEngine);
        }
    }

    public void pointwiseMontgomery(PolyVecK polyVecK, PolyVecL polyVecL) {
        for (int i2 = 0; i2 < this.dilithiumK; ++i2) {
            polyVecK.getVectorIndex(i2).pointwiseAccountMontgomery(this.mat[i2], polyVecL);
        }
    }

    public void expandMatrix(byte[] byArray) {
        for (int i2 = 0; i2 < this.dilithiumK; ++i2) {
            for (int i3 = 0; i3 < this.dilithiumL; ++i3) {
                this.mat[i2].getVectorIndex(i3).uniformBlocks(byArray, (short)((i2 << 8) + i3));
            }
        }
    }

    private String addString() {
        String string = "[";
        for (int i2 = 0; i2 < this.dilithiumK; ++i2) {
            string = string + "Outer Matrix " + i2 + " [";
            string = string + this.mat[i2].toString();
            string = i2 == this.dilithiumK - 1 ? string + "]\n" : string + "],\n";
        }
        string = string + "]\n";
        return string;
    }

    public String toString(String string) {
        return string.concat(": \n" + this.addString());
    }
}

