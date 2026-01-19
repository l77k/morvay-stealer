/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.sphincsplus;

import org.bouncycastle.pqc.crypto.sphincsplus.ADRS;
import org.bouncycastle.pqc.crypto.sphincsplus.SPHINCSPlusEngine;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

class WotsPlus {
    private final SPHINCSPlusEngine engine;
    private final int w;

    WotsPlus(SPHINCSPlusEngine sPHINCSPlusEngine) {
        this.engine = sPHINCSPlusEngine;
        this.w = this.engine.WOTS_W;
    }

    byte[] pkGen(byte[] byArray, byte[] byArray2, ADRS aDRS) {
        ADRS aDRS2 = new ADRS(aDRS);
        byte[][] byArrayArray = new byte[this.engine.WOTS_LEN][];
        for (int i2 = 0; i2 < this.engine.WOTS_LEN; ++i2) {
            ADRS aDRS3 = new ADRS(aDRS);
            aDRS3.setTypeAndClear(5);
            aDRS3.setKeyPairAddress(aDRS.getKeyPairAddress());
            aDRS3.setChainAddress(i2);
            aDRS3.setHashAddress(0);
            byte[] byArray3 = this.engine.PRF(byArray2, byArray, aDRS3);
            aDRS3.setTypeAndClear(0);
            aDRS3.setKeyPairAddress(aDRS.getKeyPairAddress());
            aDRS3.setChainAddress(i2);
            aDRS3.setHashAddress(0);
            byArrayArray[i2] = this.chain(byArray3, 0, this.w - 1, byArray2, aDRS3);
        }
        aDRS2.setTypeAndClear(1);
        aDRS2.setKeyPairAddress(aDRS.getKeyPairAddress());
        return this.engine.T_l(byArray2, aDRS2, Arrays.concatenate(byArrayArray));
    }

    byte[] chain(byte[] byArray, int n2, int n3, byte[] byArray2, ADRS aDRS) {
        if (n3 == 0) {
            return Arrays.clone(byArray);
        }
        if (n2 + n3 > this.w - 1) {
            return null;
        }
        byte[] byArray3 = byArray;
        for (int i2 = 0; i2 < n3; ++i2) {
            aDRS.setHashAddress(n2 + i2);
            byArray3 = this.engine.F(byArray2, aDRS, byArray3);
        }
        return byArray3;
    }

    public byte[] sign(byte[] byArray, byte[] byArray2, byte[] byArray3, ADRS aDRS) {
        int n2;
        ADRS aDRS2 = new ADRS(aDRS);
        int[] nArray = new int[this.engine.WOTS_LEN];
        this.base_w(byArray, 0, this.w, nArray, 0, this.engine.WOTS_LEN1);
        int n3 = 0;
        for (n2 = 0; n2 < this.engine.WOTS_LEN1; ++n2) {
            n3 += this.w - 1 - nArray[n2];
        }
        if (this.engine.WOTS_LOGW % 8 != 0) {
            n3 <<= 8 - this.engine.WOTS_LEN2 * this.engine.WOTS_LOGW % 8;
        }
        n2 = (this.engine.WOTS_LEN2 * this.engine.WOTS_LOGW + 7) / 8;
        byte[] byArray4 = Pack.intToBigEndian(n3);
        this.base_w(byArray4, 4 - n2, this.w, nArray, this.engine.WOTS_LEN1, this.engine.WOTS_LEN2);
        byte[][] byArrayArray = new byte[this.engine.WOTS_LEN][];
        for (int i2 = 0; i2 < this.engine.WOTS_LEN; ++i2) {
            aDRS2.setTypeAndClear(5);
            aDRS2.setKeyPairAddress(aDRS.getKeyPairAddress());
            aDRS2.setChainAddress(i2);
            aDRS2.setHashAddress(0);
            byte[] byArray5 = this.engine.PRF(byArray3, byArray2, aDRS2);
            aDRS2.setTypeAndClear(0);
            aDRS2.setKeyPairAddress(aDRS.getKeyPairAddress());
            aDRS2.setChainAddress(i2);
            aDRS2.setHashAddress(0);
            byArrayArray[i2] = this.chain(byArray5, 0, nArray[i2], byArray3, aDRS2);
        }
        return Arrays.concatenate(byArrayArray);
    }

    void base_w(byte[] byArray, int n2, int n3, int[] nArray, int n4, int n5) {
        byte by = 0;
        int n6 = 0;
        for (int i2 = 0; i2 < n5; ++i2) {
            if (n6 == 0) {
                by = byArray[n2++];
                n6 += 8;
            }
            nArray[n4++] = by >>> (n6 -= this.engine.WOTS_LOGW) & n3 - 1;
        }
    }

    public byte[] pkFromSig(byte[] byArray, byte[] byArray2, byte[] byArray3, ADRS aDRS) {
        int n2;
        ADRS aDRS2 = new ADRS(aDRS);
        int[] nArray = new int[this.engine.WOTS_LEN];
        this.base_w(byArray2, 0, this.w, nArray, 0, this.engine.WOTS_LEN1);
        int n3 = 0;
        for (n2 = 0; n2 < this.engine.WOTS_LEN1; ++n2) {
            n3 += this.w - 1 - nArray[n2];
        }
        n2 = (this.engine.WOTS_LEN2 * this.engine.WOTS_LOGW + 7) / 8;
        byte[] byArray4 = Pack.intToBigEndian(n3 <<= 8 - this.engine.WOTS_LEN2 * this.engine.WOTS_LOGW % 8);
        this.base_w(byArray4, 4 - n2, this.w, nArray, this.engine.WOTS_LEN1, this.engine.WOTS_LEN2);
        byte[] byArray5 = new byte[this.engine.N];
        byte[][] byArrayArray = new byte[this.engine.WOTS_LEN][];
        for (int i2 = 0; i2 < this.engine.WOTS_LEN; ++i2) {
            aDRS.setChainAddress(i2);
            System.arraycopy(byArray, i2 * this.engine.N, byArray5, 0, this.engine.N);
            byArrayArray[i2] = this.chain(byArray5, nArray[i2], this.w - 1 - nArray[i2], byArray3, aDRS);
        }
        aDRS2.setTypeAndClear(1);
        aDRS2.setKeyPairAddress(aDRS.getKeyPairAddress());
        return this.engine.T_l(byArray3, aDRS2, Arrays.concatenate(byArrayArray));
    }
}

