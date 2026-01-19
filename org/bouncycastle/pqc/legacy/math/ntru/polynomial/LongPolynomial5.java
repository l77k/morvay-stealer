/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.ntru.polynomial;

import org.bouncycastle.pqc.legacy.math.ntru.polynomial.IntegerPolynomial;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.TernaryPolynomial;
import org.bouncycastle.util.Arrays;

public class LongPolynomial5 {
    private long[] coeffs;
    private int numCoeffs;

    public LongPolynomial5(IntegerPolynomial integerPolynomial) {
        this.numCoeffs = integerPolynomial.coeffs.length;
        this.coeffs = new long[(this.numCoeffs + 4) / 5];
        int n2 = 0;
        int n3 = 0;
        for (int i2 = 0; i2 < this.numCoeffs; ++i2) {
            int n4 = n2++;
            this.coeffs[n4] = this.coeffs[n4] | (long)integerPolynomial.coeffs[i2] << n3;
            if ((n3 += 12) < 60) continue;
            n3 = 0;
        }
    }

    private LongPolynomial5(long[] lArray, int n2) {
        this.coeffs = lArray;
        this.numCoeffs = n2;
    }

    public LongPolynomial5 mult(TernaryPolynomial ternaryPolynomial) {
        int n2;
        long l2;
        long l3;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        long[][] lArray = new long[5][this.coeffs.length + (ternaryPolynomial.size() + 4) / 5 - 1];
        int[] nArray = ternaryPolynomial.getOnes();
        for (int i2 = 0; i2 != nArray.length; ++i2) {
            n8 = nArray[i2];
            n7 = n8 / 5;
            n6 = n8 - n7 * 5;
            for (n5 = 0; n5 < this.coeffs.length; ++n5) {
                lArray[n6][n7] = lArray[n6][n7] + this.coeffs[n5] & 0x7FF7FF7FF7FF7FFL;
                ++n7;
            }
        }
        int[] nArray2 = ternaryPolynomial.getNegOnes();
        for (n8 = 0; n8 != nArray2.length; ++n8) {
            n7 = nArray2[n8];
            n6 = n7 / 5;
            n5 = n7 - n6 * 5;
            for (int i3 = 0; i3 < this.coeffs.length; ++i3) {
                lArray[n5][n6] = 0x800800800800800L + lArray[n5][n6] - this.coeffs[i3] & 0x7FF7FF7FF7FF7FFL;
                ++n6;
            }
        }
        long[] lArray2 = Arrays.copyOf(lArray[0], lArray[0].length + 1);
        for (n7 = 1; n7 <= 4; ++n7) {
            n6 = n7 * 12;
            n5 = 60 - n6;
            long l4 = (1L << n5) - 1L;
            n4 = lArray[n7].length;
            for (n3 = 0; n3 < n4; ++n3) {
                l3 = lArray[n7][n3] >> n5;
                l2 = lArray[n7][n3] & l4;
                lArray2[n3] = lArray2[n3] + (l2 << n6) & 0x7FF7FF7FF7FF7FFL;
                n2 = n3 + 1;
                lArray2[n2] = lArray2[n2] + l3 & 0x7FF7FF7FF7FF7FFL;
            }
        }
        n7 = 12 * (this.numCoeffs % 5);
        for (n6 = this.coeffs.length - 1; n6 < lArray2.length; ++n6) {
            int n9;
            long l5;
            if (n6 == this.coeffs.length - 1) {
                l5 = this.numCoeffs == 5 ? 0L : lArray2[n6] >> n7;
                n9 = 0;
            } else {
                l5 = lArray2[n6];
                n9 = n6 * 5 - this.numCoeffs;
            }
            n4 = n9 / 5;
            n3 = n9 - n4 * 5;
            l3 = l5 << 12 * n3;
            l2 = l5 >> 12 * (5 - n3);
            lArray2[n4] = lArray2[n4] + l3 & 0x7FF7FF7FF7FF7FFL;
            n2 = n4 + 1;
            if (n2 >= this.coeffs.length) continue;
            lArray2[n2] = lArray2[n2] + l2 & 0x7FF7FF7FF7FF7FFL;
        }
        return new LongPolynomial5(lArray2, this.numCoeffs);
    }

    public IntegerPolynomial toIntegerPolynomial() {
        int[] nArray = new int[this.numCoeffs];
        int n2 = 0;
        int n3 = 0;
        for (int i2 = 0; i2 < this.numCoeffs; ++i2) {
            nArray[i2] = (int)(this.coeffs[n2] >> n3 & 0x7FFL);
            if ((n3 += 12) < 60) continue;
            n3 = 0;
            ++n2;
        }
        return new IntegerPolynomial(nArray);
    }
}

