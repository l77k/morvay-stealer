/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.ntru;

import org.bouncycastle.pqc.crypto.ntru.PolynomialPair;
import org.bouncycastle.pqc.math.ntru.HPSPolynomial;
import org.bouncycastle.pqc.math.ntru.HRSSPolynomial;
import org.bouncycastle.pqc.math.ntru.Polynomial;
import org.bouncycastle.pqc.math.ntru.parameters.NTRUHPSParameterSet;
import org.bouncycastle.pqc.math.ntru.parameters.NTRUHRSSParameterSet;
import org.bouncycastle.pqc.math.ntru.parameters.NTRUParameterSet;
import org.bouncycastle.util.Arrays;

class NTRUSampling {
    private final NTRUParameterSet params;

    public NTRUSampling(NTRUParameterSet nTRUParameterSet) {
        this.params = nTRUParameterSet;
    }

    public PolynomialPair sampleFg(byte[] byArray) {
        if (this.params instanceof NTRUHRSSParameterSet) {
            HRSSPolynomial hRSSPolynomial = this.sampleIidPlus(Arrays.copyOfRange(byArray, 0, this.params.sampleIidBytes()));
            HRSSPolynomial hRSSPolynomial2 = this.sampleIidPlus(Arrays.copyOfRange(byArray, this.params.sampleIidBytes(), byArray.length));
            return new PolynomialPair(hRSSPolynomial, hRSSPolynomial2);
        }
        if (this.params instanceof NTRUHPSParameterSet) {
            HPSPolynomial hPSPolynomial = (HPSPolynomial)this.sampleIid(Arrays.copyOfRange(byArray, 0, this.params.sampleIidBytes()));
            HPSPolynomial hPSPolynomial2 = this.sampleFixedType(Arrays.copyOfRange(byArray, this.params.sampleIidBytes(), byArray.length));
            return new PolynomialPair(hPSPolynomial, hPSPolynomial2);
        }
        throw new IllegalArgumentException("Invalid polynomial type");
    }

    public PolynomialPair sampleRm(byte[] byArray) {
        if (this.params instanceof NTRUHRSSParameterSet) {
            HRSSPolynomial hRSSPolynomial = (HRSSPolynomial)this.sampleIid(Arrays.copyOfRange(byArray, 0, this.params.sampleIidBytes()));
            HRSSPolynomial hRSSPolynomial2 = (HRSSPolynomial)this.sampleIid(Arrays.copyOfRange(byArray, this.params.sampleIidBytes(), byArray.length));
            return new PolynomialPair(hRSSPolynomial, hRSSPolynomial2);
        }
        if (this.params instanceof NTRUHPSParameterSet) {
            HPSPolynomial hPSPolynomial = (HPSPolynomial)this.sampleIid(Arrays.copyOfRange(byArray, 0, this.params.sampleIidBytes()));
            HPSPolynomial hPSPolynomial2 = this.sampleFixedType(Arrays.copyOfRange(byArray, this.params.sampleIidBytes(), byArray.length));
            return new PolynomialPair(hPSPolynomial, hPSPolynomial2);
        }
        throw new IllegalArgumentException("Invalid polynomial type");
    }

    public Polynomial sampleIid(byte[] byArray) {
        Polynomial polynomial = this.params.createPolynomial();
        for (int i2 = 0; i2 < this.params.n() - 1; ++i2) {
            polynomial.coeffs[i2] = (short)NTRUSampling.mod3(byArray[i2] & 0xFF);
        }
        polynomial.coeffs[this.params.n() - 1] = 0;
        return polynomial;
    }

    public HPSPolynomial sampleFixedType(byte[] byArray) {
        int n2;
        int n3 = this.params.n();
        int n4 = ((NTRUHPSParameterSet)this.params).weight();
        HPSPolynomial hPSPolynomial = new HPSPolynomial((NTRUHPSParameterSet)this.params);
        int[] nArray = new int[n3 - 1];
        for (n2 = 0; n2 < (n3 - 1) / 4; ++n2) {
            nArray[4 * n2 + 0] = ((byArray[15 * n2 + 0] & 0xFF) << 2) + ((byArray[15 * n2 + 1] & 0xFF) << 10) + ((byArray[15 * n2 + 2] & 0xFF) << 18) + ((byArray[15 * n2 + 3] & 0xFF) << 26);
            nArray[4 * n2 + 1] = ((byArray[15 + n2 * 3] & 0xFF & 0xC0) >> 4) + ((byArray[15 * n2 + 4] & 0xFF) << 4) + ((byArray[15 * n2 + 5] & 0xFF) << 12) + ((byArray[15 * n2 + 6] & 0xFF) << 20) + ((byArray[15 * n2 + 7] & 0xFF) << 28);
            nArray[4 * n2 + 2] = ((byArray[15 + n2 * 7] & 0xFF & 0xF0) >> 2) + ((byArray[15 * n2 + 8] & 0xFF) << 6) + ((byArray[15 * n2 + 9] & 0xFF) << 14) + ((byArray[15 * n2 + 10] & 0xFF) << 22) + ((byArray[15 * n2 + 11] & 0xFF) << 30);
            nArray[4 * n2 + 3] = (byArray[15 * n2 + 11] & 0xFF & 0xFC) + ((byArray[15 * n2 + 12] & 0xFF) << 8) + ((byArray[15 * n2 + 13] & 0xFF) << 16) + ((byArray[15 * n2 + 14] & 0xFF) << 24);
        }
        if (n3 - 1 > (n3 - 1) / 4 * 4) {
            n2 = (n3 - 1) / 4;
            nArray[4 * n2 + 0] = ((byArray[15 * n2 + 0] & 0xFF) << 2) + ((byArray[15 * n2 + 1] & 0xFF) << 10) + ((byArray[15 * n2 + 2] & 0xFF) << 18) + ((byArray[15 * n2 + 3] & 0xFF) << 26);
            nArray[4 * n2 + 1] = ((byArray[15 + n2 * 3] & 0xFF & 0xC0) >> 4) + ((byArray[15 * n2 + 4] & 0xFF) << 4) + ((byArray[15 * n2 + 5] & 0xFF) << 12) + ((byArray[15 * n2 + 6] & 0xFF) << 20) + ((byArray[15 * n2 + 7] & 0xFF) << 28);
        }
        n2 = 0;
        while (n2 < n4 / 2) {
            int n5 = n2++;
            nArray[n5] = nArray[n5] | 1;
        }
        n2 = n4 / 2;
        while (n2 < n4) {
            int n6 = n2++;
            nArray[n6] = nArray[n6] | 2;
        }
        java.util.Arrays.sort(nArray);
        for (n2 = 0; n2 < n3 - 1; ++n2) {
            hPSPolynomial.coeffs[n2] = (short)(nArray[n2] & 3);
        }
        hPSPolynomial.coeffs[n3 - 1] = 0;
        return hPSPolynomial;
    }

    public HRSSPolynomial sampleIidPlus(byte[] byArray) {
        int n2;
        int n3 = this.params.n();
        int n4 = 0;
        HRSSPolynomial hRSSPolynomial = (HRSSPolynomial)this.sampleIid(byArray);
        for (n2 = 0; n2 < n3 - 1; ++n2) {
            hRSSPolynomial.coeffs[n2] = (short)(hRSSPolynomial.coeffs[n2] | -(hRSSPolynomial.coeffs[n2] >>> 1));
        }
        for (n2 = 0; n2 < n3 - 1; ++n2) {
            n4 = (short)(n4 + (short)(hRSSPolynomial.coeffs[n2 + 1] * hRSSPolynomial.coeffs[n2]));
        }
        n4 = (short)(1 | -((n4 & 0xFFFF) >>> 15));
        for (n2 = 0; n2 < n3 - 1; n2 += 2) {
            hRSSPolynomial.coeffs[n2] = (short)(n4 * hRSSPolynomial.coeffs[n2]);
        }
        for (n2 = 0; n2 < n3 - 1; ++n2) {
            hRSSPolynomial.coeffs[n2] = (short)(3 & (hRSSPolynomial.coeffs[n2] & 0xFFFF ^ (hRSSPolynomial.coeffs[n2] & 0xFFFF) >>> 15));
        }
        return hRSSPolynomial;
    }

    private static int mod3(int n2) {
        return n2 % 3;
    }
}

