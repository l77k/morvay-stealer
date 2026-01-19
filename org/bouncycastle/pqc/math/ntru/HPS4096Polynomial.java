/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.math.ntru;

import org.bouncycastle.pqc.math.ntru.HPSPolynomial;
import org.bouncycastle.pqc.math.ntru.parameters.NTRUHPSParameterSet;

public class HPS4096Polynomial
extends HPSPolynomial {
    public HPS4096Polynomial(NTRUHPSParameterSet nTRUHPSParameterSet) {
        super(nTRUHPSParameterSet);
    }

    @Override
    public byte[] sqToBytes(int n2) {
        byte[] byArray = new byte[n2];
        int n3 = this.params.q();
        for (int i2 = 0; i2 < this.params.packDegree() / 2; ++i2) {
            byArray[3 * i2 + 0] = (byte)(HPS4096Polynomial.modQ(this.coeffs[2 * i2 + 0] & 0xFFFF, n3) & 0xFF);
            byArray[3 * i2 + 1] = (byte)(HPS4096Polynomial.modQ(this.coeffs[2 * i2 + 0] & 0xFFFF, n3) >>> 8 | (HPS4096Polynomial.modQ(this.coeffs[2 * i2 + 1] & 0xFFFF, n3) & 0xF) << 4);
            byArray[3 * i2 + 2] = (byte)(HPS4096Polynomial.modQ(this.coeffs[2 * i2 + 1] & 0xFFFF, n3) >>> 4);
        }
        return byArray;
    }

    @Override
    public void sqFromBytes(byte[] byArray) {
        for (int i2 = 0; i2 < this.params.packDegree() / 2; ++i2) {
            this.coeffs[2 * i2 + 0] = (short)((byArray[3 * i2 + 0] & 0xFF) >>> 0 | ((short)(byArray[3 * i2 + 1] & 0xFF) & 0xF) << 8);
            this.coeffs[2 * i2 + 1] = (short)((byArray[3 * i2 + 1] & 0xFF) >>> 4 | ((short)(byArray[3 * i2 + 2] & 0xFF) & 0xFF) << 4);
        }
        this.coeffs[this.params.n() - 1] = 0;
    }
}

