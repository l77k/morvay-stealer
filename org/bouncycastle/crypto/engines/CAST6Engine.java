/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.engines.CAST5Engine;

public final class CAST6Engine
extends CAST5Engine {
    protected static final int ROUNDS = 12;
    protected static final int BLOCK_SIZE = 16;
    protected int[] _Kr = new int[48];
    protected int[] _Km = new int[48];
    protected int[] _Tr = new int[192];
    protected int[] _Tm = new int[192];
    private int[] _workingKey = new int[8];

    @Override
    public String getAlgorithmName() {
        return "CAST6";
    }

    @Override
    public void reset() {
    }

    @Override
    public int getBlockSize() {
        return 16;
    }

    @Override
    protected void setKey(byte[] byArray) {
        int n2;
        int n3;
        int n4 = 1518500249;
        int n5 = 1859775393;
        int n6 = 19;
        int n7 = 17;
        for (int i2 = 0; i2 < 24; ++i2) {
            for (n3 = 0; n3 < 8; ++n3) {
                this._Tm[i2 * 8 + n3] = n4;
                n4 += n5;
                this._Tr[i2 * 8 + n3] = n6;
                n6 = n6 + n7 & 0x1F;
            }
        }
        byte[] byArray2 = new byte[64];
        n3 = byArray.length;
        System.arraycopy(byArray, 0, byArray2, 0, n3);
        for (n2 = 0; n2 < 8; ++n2) {
            this._workingKey[n2] = this.BytesTo32bits(byArray2, n2 * 4);
        }
        for (n2 = 0; n2 < 12; ++n2) {
            int n8 = n2 * 2 * 8;
            this._workingKey[6] = this._workingKey[6] ^ this.F1(this._workingKey[7], this._Tm[n8], this._Tr[n8]);
            this._workingKey[5] = this._workingKey[5] ^ this.F2(this._workingKey[6], this._Tm[n8 + 1], this._Tr[n8 + 1]);
            this._workingKey[4] = this._workingKey[4] ^ this.F3(this._workingKey[5], this._Tm[n8 + 2], this._Tr[n8 + 2]);
            this._workingKey[3] = this._workingKey[3] ^ this.F1(this._workingKey[4], this._Tm[n8 + 3], this._Tr[n8 + 3]);
            this._workingKey[2] = this._workingKey[2] ^ this.F2(this._workingKey[3], this._Tm[n8 + 4], this._Tr[n8 + 4]);
            this._workingKey[1] = this._workingKey[1] ^ this.F3(this._workingKey[2], this._Tm[n8 + 5], this._Tr[n8 + 5]);
            this._workingKey[0] = this._workingKey[0] ^ this.F1(this._workingKey[1], this._Tm[n8 + 6], this._Tr[n8 + 6]);
            this._workingKey[7] = this._workingKey[7] ^ this.F2(this._workingKey[0], this._Tm[n8 + 7], this._Tr[n8 + 7]);
            n8 = (n2 * 2 + 1) * 8;
            this._workingKey[6] = this._workingKey[6] ^ this.F1(this._workingKey[7], this._Tm[n8], this._Tr[n8]);
            this._workingKey[5] = this._workingKey[5] ^ this.F2(this._workingKey[6], this._Tm[n8 + 1], this._Tr[n8 + 1]);
            this._workingKey[4] = this._workingKey[4] ^ this.F3(this._workingKey[5], this._Tm[n8 + 2], this._Tr[n8 + 2]);
            this._workingKey[3] = this._workingKey[3] ^ this.F1(this._workingKey[4], this._Tm[n8 + 3], this._Tr[n8 + 3]);
            this._workingKey[2] = this._workingKey[2] ^ this.F2(this._workingKey[3], this._Tm[n8 + 4], this._Tr[n8 + 4]);
            this._workingKey[1] = this._workingKey[1] ^ this.F3(this._workingKey[2], this._Tm[n8 + 5], this._Tr[n8 + 5]);
            this._workingKey[0] = this._workingKey[0] ^ this.F1(this._workingKey[1], this._Tm[n8 + 6], this._Tr[n8 + 6]);
            this._workingKey[7] = this._workingKey[7] ^ this.F2(this._workingKey[0], this._Tm[n8 + 7], this._Tr[n8 + 7]);
            this._Kr[n2 * 4] = this._workingKey[0] & 0x1F;
            this._Kr[n2 * 4 + 1] = this._workingKey[2] & 0x1F;
            this._Kr[n2 * 4 + 2] = this._workingKey[4] & 0x1F;
            this._Kr[n2 * 4 + 3] = this._workingKey[6] & 0x1F;
            this._Km[n2 * 4] = this._workingKey[7];
            this._Km[n2 * 4 + 1] = this._workingKey[5];
            this._Km[n2 * 4 + 2] = this._workingKey[3];
            this._Km[n2 * 4 + 3] = this._workingKey[1];
        }
    }

    @Override
    protected int encryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        int[] nArray = new int[4];
        int n4 = this.BytesTo32bits(byArray, n2);
        int n5 = this.BytesTo32bits(byArray, n2 + 4);
        int n6 = this.BytesTo32bits(byArray, n2 + 8);
        int n7 = this.BytesTo32bits(byArray, n2 + 12);
        this.CAST_Encipher(n4, n5, n6, n7, nArray);
        this.Bits32ToBytes(nArray[0], byArray2, n3);
        this.Bits32ToBytes(nArray[1], byArray2, n3 + 4);
        this.Bits32ToBytes(nArray[2], byArray2, n3 + 8);
        this.Bits32ToBytes(nArray[3], byArray2, n3 + 12);
        return 16;
    }

    @Override
    protected int decryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        int[] nArray = new int[4];
        int n4 = this.BytesTo32bits(byArray, n2);
        int n5 = this.BytesTo32bits(byArray, n2 + 4);
        int n6 = this.BytesTo32bits(byArray, n2 + 8);
        int n7 = this.BytesTo32bits(byArray, n2 + 12);
        this.CAST_Decipher(n4, n5, n6, n7, nArray);
        this.Bits32ToBytes(nArray[0], byArray2, n3);
        this.Bits32ToBytes(nArray[1], byArray2, n3 + 4);
        this.Bits32ToBytes(nArray[2], byArray2, n3 + 8);
        this.Bits32ToBytes(nArray[3], byArray2, n3 + 12);
        return 16;
    }

    protected final void CAST_Encipher(int n2, int n3, int n4, int n5, int[] nArray) {
        int n6;
        int n7;
        for (n7 = 0; n7 < 6; ++n7) {
            n6 = n7 * 4;
            n5 ^= this.F1(n2 ^= this.F3(n3 ^= this.F2(n4 ^= this.F1(n5, this._Km[n6], this._Kr[n6]), this._Km[n6 + 1], this._Kr[n6 + 1]), this._Km[n6 + 2], this._Kr[n6 + 2]), this._Km[n6 + 3], this._Kr[n6 + 3]);
        }
        for (n7 = 6; n7 < 12; ++n7) {
            n6 = n7 * 4;
            n3 ^= this.F2(n4, this._Km[n6 + 1], this._Kr[n6 + 1]);
            n4 ^= this.F1(n5 ^= this.F1(n2 ^= this.F3(n3, this._Km[n6 + 2], this._Kr[n6 + 2]), this._Km[n6 + 3], this._Kr[n6 + 3]), this._Km[n6], this._Kr[n6]);
        }
        nArray[0] = n2;
        nArray[1] = n3;
        nArray[2] = n4;
        nArray[3] = n5;
    }

    protected final void CAST_Decipher(int n2, int n3, int n4, int n5, int[] nArray) {
        int n6;
        int n7;
        for (n7 = 0; n7 < 6; ++n7) {
            n6 = (11 - n7) * 4;
            n5 ^= this.F1(n2 ^= this.F3(n3 ^= this.F2(n4 ^= this.F1(n5, this._Km[n6], this._Kr[n6]), this._Km[n6 + 1], this._Kr[n6 + 1]), this._Km[n6 + 2], this._Kr[n6 + 2]), this._Km[n6 + 3], this._Kr[n6 + 3]);
        }
        for (n7 = 6; n7 < 12; ++n7) {
            n6 = (11 - n7) * 4;
            n3 ^= this.F2(n4, this._Km[n6 + 1], this._Kr[n6 + 1]);
            n4 ^= this.F1(n5 ^= this.F1(n2 ^= this.F3(n3, this._Km[n6 + 2], this._Kr[n6 + 2]), this._Km[n6 + 3], this._Kr[n6 + 3]), this._Km[n6], this._Kr[n6]);
        }
        nArray[0] = n2;
        nArray[1] = n3;
        nArray[2] = n4;
        nArray[3] = n5;
    }
}

