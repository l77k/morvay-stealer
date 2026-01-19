/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.picnic;

import org.bouncycastle.pqc.crypto.picnic.KMatricesWithPointer;
import org.bouncycastle.pqc.crypto.picnic.PicnicEngine;
import org.bouncycastle.pqc.crypto.picnic.Utils;
import org.bouncycastle.util.Pack;

class Tape {
    byte[][] tapes;
    int pos;
    int nTapes;
    private PicnicEngine engine;

    public Tape(PicnicEngine picnicEngine) {
        this.engine = picnicEngine;
        this.tapes = new byte[picnicEngine.numMPCParties][2 * picnicEngine.andSizeBytes];
        this.pos = 0;
        this.nTapes = picnicEngine.numMPCParties;
    }

    protected void setAuxBits(byte[] byArray) {
        int n2 = this.engine.numMPCParties - 1;
        int n3 = 0;
        int n4 = this.engine.stateSizeBits;
        for (int i2 = 0; i2 < this.engine.numRounds; ++i2) {
            for (int i3 = 0; i3 < n4; ++i3) {
                Utils.setBit(this.tapes[n2], n4 + n4 * 2 * i2 + i3, Utils.getBit(byArray, n3++));
            }
        }
    }

    protected void computeAuxTape(byte[] byArray) {
        int[] nArray = new int[16];
        int[] nArray2 = new int[16];
        int[] nArray3 = new int[16];
        int[] nArray4 = new int[16];
        int[] nArray5 = new int[16];
        nArray5[this.engine.stateSizeWords - 1] = 0;
        this.tapesToParityBits(nArray5, this.engine.stateSizeBits);
        KMatricesWithPointer kMatricesWithPointer = this.engine.lowmcConstants.KMatrixInv(this.engine);
        this.engine.matrix_mul(nArray4, nArray5, kMatricesWithPointer.getData(), kMatricesWithPointer.getMatrixPointer());
        if (byArray != null) {
            Pack.intToLittleEndian(nArray4, 0, this.engine.stateSizeWords, byArray, 0);
        }
        for (int i2 = this.engine.numRounds; i2 > 0; --i2) {
            kMatricesWithPointer = this.engine.lowmcConstants.KMatrix(this.engine, i2);
            this.engine.matrix_mul(nArray, nArray4, kMatricesWithPointer.getData(), kMatricesWithPointer.getMatrixPointer());
            this.engine.xor_array(nArray2, nArray2, nArray, 0);
            kMatricesWithPointer = this.engine.lowmcConstants.LMatrixInv(this.engine, i2 - 1);
            this.engine.matrix_mul(nArray3, nArray2, kMatricesWithPointer.getData(), kMatricesWithPointer.getMatrixPointer());
            if (i2 == 1) {
                System.arraycopy(nArray5, 0, nArray2, 0, nArray5.length);
            } else {
                this.pos = this.engine.stateSizeBits * 2 * (i2 - 1);
                this.tapesToParityBits(nArray2, this.engine.stateSizeBits);
            }
            this.pos = this.engine.stateSizeBits * 2 * (i2 - 1) + this.engine.stateSizeBits;
            this.engine.aux_mpc_sbox(nArray2, nArray3, this);
        }
        this.pos = 0;
    }

    private void tapesToParityBits(int[] nArray, int n2) {
        for (int i2 = 0; i2 < n2; ++i2) {
            Utils.setBitInWordArray(nArray, i2, Utils.parity16(this.tapesToWord()));
        }
    }

    protected int tapesToWord() {
        int n2 = 0;
        int n3 = this.pos >>> 3;
        int n4 = this.pos & 7 ^ 7;
        int n5 = 1 << n4;
        n2 |= (this.tapes[0][n3] & n5) << 7;
        n2 |= (this.tapes[1][n3] & n5) << 6;
        n2 |= (this.tapes[2][n3] & n5) << 5;
        n2 |= (this.tapes[3][n3] & n5) << 4;
        n2 |= (this.tapes[4][n3] & n5) << 3;
        n2 |= (this.tapes[5][n3] & n5) << 2;
        n2 |= (this.tapes[6][n3] & n5) << 1;
        n2 |= (this.tapes[7][n3] & n5) << 0;
        n2 |= (this.tapes[8][n3] & n5) << 15;
        n2 |= (this.tapes[9][n3] & n5) << 14;
        n2 |= (this.tapes[10][n3] & n5) << 13;
        n2 |= (this.tapes[11][n3] & n5) << 12;
        n2 |= (this.tapes[12][n3] & n5) << 11;
        n2 |= (this.tapes[13][n3] & n5) << 10;
        n2 |= (this.tapes[14][n3] & n5) << 9;
        ++this.pos;
        return (n2 |= (this.tapes[15][n3] & n5) << 8) >>> n4;
    }
}

