/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.math.ntru;

import org.bouncycastle.pqc.math.ntru.HRSSPolynomial;
import org.bouncycastle.pqc.math.ntru.parameters.NTRUHRSSParameterSet;

public class HRSS1373Polynomial
extends HRSSPolynomial {
    private static final int L = 1376;
    private static final int M = 344;
    private static final int K = 86;

    public HRSS1373Polynomial(NTRUHRSSParameterSet nTRUHRSSParameterSet) {
        super(nTRUHRSSParameterSet);
    }

    @Override
    public byte[] sqToBytes(int n2) {
        byte[] byArray = new byte[n2];
        short[] sArray = new short[4];
        for (int i2 = 0; i2 < this.params.packDegree() / 4; ++i2) {
            for (int i3 = 0; i3 < 4; ++i3) {
                sArray[i3] = (short)HRSS1373Polynomial.modQ(this.coeffs[4 * i2 + i3] & 0xFFFF, this.params.q());
            }
            byArray[7 * i2 + 0] = (byte)(sArray[0] & 0xFF);
            byArray[7 * i2 + 1] = (byte)(sArray[0] >>> 8 | (sArray[1] & 3) << 6);
            byArray[7 * i2 + 2] = (byte)(sArray[1] >>> 2 & 0xFF);
            byArray[7 * i2 + 3] = (byte)(sArray[1] >>> 10 | (sArray[2] & 0xF) << 4);
            byArray[7 * i2 + 4] = (byte)(sArray[2] >>> 4 & 0xFF);
            byArray[7 * i2 + 5] = (byte)(sArray[2] >>> 12 | (sArray[3] & 0x3F) << 2);
            byArray[7 * i2 + 6] = (byte)(sArray[3] >>> 6);
        }
        if (this.params.packDegree() % 4 == 2) {
            sArray[0] = (short)HRSS1373Polynomial.modQ(this.coeffs[this.params.packDegree() - 2] & 0xFFFF, this.params.q());
            sArray[1] = (short)HRSS1373Polynomial.modQ(this.coeffs[this.params.packDegree() - 1] & 0xFFFF, this.params.q());
            byArray[7 * i2 + 0] = (byte)(sArray[0] & 0xFF);
            byArray[7 * i2 + 1] = (byte)(sArray[0] >>> 8 | (sArray[1] & 3) << 6);
            byArray[7 * i2 + 2] = (byte)(sArray[1] >>> 2 & 0xFF);
            byArray[7 * i2 + 3] = (byte)(sArray[1] >>> 10);
        }
        return byArray;
    }

    @Override
    public void sqFromBytes(byte[] byArray) {
        int n2;
        for (n2 = 0; n2 < this.params.packDegree() / 4; ++n2) {
            this.coeffs[4 * n2 + 0] = (short)(byArray[7 * n2 + 0] & 0xFF | ((short)(byArray[7 * n2 + 1] & 0xFF) & 0x3F) << 8);
            this.coeffs[4 * n2 + 1] = (short)((byArray[7 * n2 + 1] & 0xFF) >>> 6 | (short)(byArray[7 * n2 + 2] & 0xFF) << 2 | (short)(byArray[7 * n2 + 3] & 0xF) << 10);
            this.coeffs[4 * n2 + 2] = (short)((byArray[7 * n2 + 3] & 0xFF) >>> 4 | ((short)(byArray[7 * n2 + 4] & 0xFF) & 0xFF) << 4 | (short)(byArray[7 * n2 + 5] & 3) << 12);
            this.coeffs[4 * n2 + 3] = (short)((byArray[7 * n2 + 5] & 0xFF) >>> 2 | (short)(byArray[7 * n2 + 6] & 0xFF) << 6);
        }
        if (this.params.packDegree() % 4 == 2) {
            this.coeffs[4 * n2 + 0] = (short)(byArray[7 * n2 + 0] | (byArray[7 * n2 + 1] & 0x3F) << 8);
            this.coeffs[4 * n2 + 1] = (short)(byArray[7 * n2 + 1] >>> 6 | (short)byArray[7 * n2 + 2] << 2 | ((short)byArray[7 * n2 + 3] & 0xF) << 10);
        }
        this.coeffs[this.params.n() - 1] = 0;
    }
}

