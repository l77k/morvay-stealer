/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.ntru.polynomial;

import org.bouncycastle.pqc.legacy.math.ntru.polynomial.IntegerPolynomial;
import org.bouncycastle.util.Arrays;

public class LongPolynomial2 {
    private long[] coeffs;
    private int numCoeffs;

    public LongPolynomial2(IntegerPolynomial integerPolynomial) {
        this.numCoeffs = integerPolynomial.coeffs.length;
        this.coeffs = new long[(this.numCoeffs + 1) / 2];
        int n2 = 0;
        int n3 = 0;
        while (n3 < this.numCoeffs) {
            long l2;
            int n4;
            for (n4 = integerPolynomial.coeffs[n3++]; n4 < 0; n4 += 2048) {
            }
            long l3 = l2 = n3 < this.numCoeffs ? (long)integerPolynomial.coeffs[n3++] : 0L;
            while (l2 < 0L) {
                l2 += 2048L;
            }
            this.coeffs[n2] = (long)n4 + (l2 << 24);
            ++n2;
        }
    }

    private LongPolynomial2(long[] lArray) {
        this.coeffs = lArray;
    }

    private LongPolynomial2(int n2) {
        this.coeffs = new long[n2];
    }

    public LongPolynomial2 mult(LongPolynomial2 longPolynomial2) {
        int n2 = this.coeffs.length;
        if (longPolynomial2.coeffs.length != n2 || this.numCoeffs != longPolynomial2.numCoeffs) {
            throw new IllegalArgumentException("Number of coefficients must be the same");
        }
        LongPolynomial2 longPolynomial22 = this.multRecursive(longPolynomial2);
        if (longPolynomial22.coeffs.length > n2) {
            if (this.numCoeffs % 2 == 0) {
                for (int i2 = n2; i2 < longPolynomial22.coeffs.length; ++i2) {
                    longPolynomial22.coeffs[i2 - n2] = longPolynomial22.coeffs[i2 - n2] + longPolynomial22.coeffs[i2] & 0x7FF0007FFL;
                }
                longPolynomial22.coeffs = Arrays.copyOf(longPolynomial22.coeffs, n2);
            } else {
                for (int i3 = n2; i3 < longPolynomial22.coeffs.length; ++i3) {
                    longPolynomial22.coeffs[i3 - n2] = longPolynomial22.coeffs[i3 - n2] + (longPolynomial22.coeffs[i3 - 1] >> 24);
                    longPolynomial22.coeffs[i3 - n2] = longPolynomial22.coeffs[i3 - n2] + ((longPolynomial22.coeffs[i3] & 0x7FFL) << 24);
                    int n3 = i3 - n2;
                    longPolynomial22.coeffs[n3] = longPolynomial22.coeffs[n3] & 0x7FF0007FFL;
                }
                longPolynomial22.coeffs = Arrays.copyOf(longPolynomial22.coeffs, n2);
                int n4 = longPolynomial22.coeffs.length - 1;
                longPolynomial22.coeffs[n4] = longPolynomial22.coeffs[n4] & 0x7FFL;
            }
        }
        longPolynomial22 = new LongPolynomial2(longPolynomial22.coeffs);
        longPolynomial22.numCoeffs = this.numCoeffs;
        return longPolynomial22;
    }

    public IntegerPolynomial toIntegerPolynomial() {
        int[] nArray = new int[this.numCoeffs];
        int n2 = 0;
        for (int i2 = 0; i2 < this.coeffs.length; ++i2) {
            nArray[n2++] = (int)(this.coeffs[i2] & 0x7FFL);
            if (n2 >= this.numCoeffs) continue;
            nArray[n2++] = (int)(this.coeffs[i2] >> 24 & 0x7FFL);
        }
        return new IntegerPolynomial(nArray);
    }

    private LongPolynomial2 multRecursive(LongPolynomial2 longPolynomial2) {
        int n2;
        long[] lArray = this.coeffs;
        long[] lArray2 = longPolynomial2.coeffs;
        int n3 = longPolynomial2.coeffs.length;
        if (n3 <= 32) {
            int n4 = 2 * n3;
            LongPolynomial2 longPolynomial22 = new LongPolynomial2(new long[n4]);
            for (int i2 = 0; i2 < n4; ++i2) {
                for (int i3 = Math.max(0, i2 - n3 + 1); i3 <= Math.min(i2, n3 - 1); ++i3) {
                    long l2 = lArray[i2 - i3] * lArray2[i3];
                    long l3 = l2 & 0x7FF000000L + (l2 & 0x7FFL);
                    long l4 = l2 >>> 48 & 0x7FFL;
                    longPolynomial22.coeffs[i2] = longPolynomial22.coeffs[i2] + l3 & 0x7FF0007FFL;
                    longPolynomial22.coeffs[i2 + 1] = longPolynomial22.coeffs[i2 + 1] + l4 & 0x7FF0007FFL;
                }
            }
            return longPolynomial22;
        }
        int n5 = n3 / 2;
        LongPolynomial2 longPolynomial23 = new LongPolynomial2(Arrays.copyOf(lArray, n5));
        LongPolynomial2 longPolynomial24 = new LongPolynomial2(Arrays.copyOfRange(lArray, n5, n3));
        LongPolynomial2 longPolynomial25 = new LongPolynomial2(Arrays.copyOf(lArray2, n5));
        LongPolynomial2 longPolynomial26 = new LongPolynomial2(Arrays.copyOfRange(lArray2, n5, n3));
        LongPolynomial2 longPolynomial27 = (LongPolynomial2)longPolynomial23.clone();
        longPolynomial27.add(longPolynomial24);
        LongPolynomial2 longPolynomial28 = (LongPolynomial2)longPolynomial25.clone();
        longPolynomial28.add(longPolynomial26);
        LongPolynomial2 longPolynomial29 = longPolynomial23.multRecursive(longPolynomial25);
        LongPolynomial2 longPolynomial210 = longPolynomial24.multRecursive(longPolynomial26);
        LongPolynomial2 longPolynomial211 = longPolynomial27.multRecursive(longPolynomial28);
        longPolynomial211.sub(longPolynomial29);
        longPolynomial211.sub(longPolynomial210);
        LongPolynomial2 longPolynomial212 = new LongPolynomial2(2 * n3);
        for (n2 = 0; n2 < longPolynomial29.coeffs.length; ++n2) {
            longPolynomial212.coeffs[n2] = longPolynomial29.coeffs[n2] & 0x7FF0007FFL;
        }
        for (n2 = 0; n2 < longPolynomial211.coeffs.length; ++n2) {
            longPolynomial212.coeffs[n5 + n2] = longPolynomial212.coeffs[n5 + n2] + longPolynomial211.coeffs[n2] & 0x7FF0007FFL;
        }
        for (n2 = 0; n2 < longPolynomial210.coeffs.length; ++n2) {
            longPolynomial212.coeffs[2 * n5 + n2] = longPolynomial212.coeffs[2 * n5 + n2] + longPolynomial210.coeffs[n2] & 0x7FF0007FFL;
        }
        return longPolynomial212;
    }

    private void add(LongPolynomial2 longPolynomial2) {
        if (longPolynomial2.coeffs.length > this.coeffs.length) {
            this.coeffs = Arrays.copyOf(this.coeffs, longPolynomial2.coeffs.length);
        }
        for (int i2 = 0; i2 < longPolynomial2.coeffs.length; ++i2) {
            this.coeffs[i2] = this.coeffs[i2] + longPolynomial2.coeffs[i2] & 0x7FF0007FFL;
        }
    }

    private void sub(LongPolynomial2 longPolynomial2) {
        if (longPolynomial2.coeffs.length > this.coeffs.length) {
            this.coeffs = Arrays.copyOf(this.coeffs, longPolynomial2.coeffs.length);
        }
        for (int i2 = 0; i2 < longPolynomial2.coeffs.length; ++i2) {
            this.coeffs[i2] = 0x800000800000L + this.coeffs[i2] - longPolynomial2.coeffs[i2] & 0x7FF0007FFL;
        }
    }

    public void subAnd(LongPolynomial2 longPolynomial2, int n2) {
        long l2 = ((long)n2 << 24) + (long)n2;
        for (int i2 = 0; i2 < longPolynomial2.coeffs.length; ++i2) {
            this.coeffs[i2] = 0x800000800000L + this.coeffs[i2] - longPolynomial2.coeffs[i2] & l2;
        }
    }

    public void mult2And(int n2) {
        long l2 = ((long)n2 << 24) + (long)n2;
        for (int i2 = 0; i2 < this.coeffs.length; ++i2) {
            this.coeffs[i2] = this.coeffs[i2] << 1 & l2;
        }
    }

    public Object clone() {
        LongPolynomial2 longPolynomial2 = new LongPolynomial2((long[])this.coeffs.clone());
        longPolynomial2.numCoeffs = this.numCoeffs;
        return longPolynomial2;
    }

    public boolean equals(Object object) {
        if (object instanceof LongPolynomial2) {
            return Arrays.areEqual(this.coeffs, ((LongPolynomial2)object).coeffs);
        }
        return false;
    }
}

