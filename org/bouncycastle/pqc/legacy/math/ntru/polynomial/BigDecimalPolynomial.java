/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.ntru.polynomial;

import java.math.BigDecimal;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.BigIntPolynomial;

public class BigDecimalPolynomial {
    private static final BigDecimal ZERO = new BigDecimal("0");
    private static final BigDecimal ONE_HALF = new BigDecimal("0.5");
    BigDecimal[] coeffs;

    BigDecimalPolynomial(int n2) {
        this.coeffs = new BigDecimal[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            this.coeffs[i2] = ZERO;
        }
    }

    BigDecimalPolynomial(BigDecimal[] bigDecimalArray) {
        this.coeffs = bigDecimalArray;
    }

    public BigDecimalPolynomial(BigIntPolynomial bigIntPolynomial) {
        int n2 = bigIntPolynomial.coeffs.length;
        this.coeffs = new BigDecimal[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            this.coeffs[i2] = new BigDecimal(bigIntPolynomial.coeffs[i2]);
        }
    }

    public void halve() {
        for (int i2 = 0; i2 < this.coeffs.length; ++i2) {
            this.coeffs[i2] = this.coeffs[i2].multiply(ONE_HALF);
        }
    }

    public BigDecimalPolynomial mult(BigIntPolynomial bigIntPolynomial) {
        return this.mult(new BigDecimalPolynomial(bigIntPolynomial));
    }

    public BigDecimalPolynomial mult(BigDecimalPolynomial bigDecimalPolynomial) {
        int n2 = this.coeffs.length;
        if (bigDecimalPolynomial.coeffs.length != n2) {
            throw new IllegalArgumentException("Number of coefficients must be the same");
        }
        BigDecimalPolynomial bigDecimalPolynomial2 = this.multRecursive(bigDecimalPolynomial);
        if (bigDecimalPolynomial2.coeffs.length > n2) {
            for (int i2 = n2; i2 < bigDecimalPolynomial2.coeffs.length; ++i2) {
                bigDecimalPolynomial2.coeffs[i2 - n2] = bigDecimalPolynomial2.coeffs[i2 - n2].add(bigDecimalPolynomial2.coeffs[i2]);
            }
            bigDecimalPolynomial2.coeffs = this.copyOf(bigDecimalPolynomial2.coeffs, n2);
        }
        return bigDecimalPolynomial2;
    }

    private BigDecimalPolynomial multRecursive(BigDecimalPolynomial bigDecimalPolynomial) {
        int n2;
        BigDecimal[] bigDecimalArray = this.coeffs;
        BigDecimal[] bigDecimalArray2 = bigDecimalPolynomial.coeffs;
        int n3 = bigDecimalPolynomial.coeffs.length;
        if (n3 <= 1) {
            BigDecimal[] bigDecimalArray3 = (BigDecimal[])this.coeffs.clone();
            for (int i2 = 0; i2 < this.coeffs.length; ++i2) {
                bigDecimalArray3[i2] = bigDecimalArray3[i2].multiply(bigDecimalPolynomial.coeffs[0]);
            }
            return new BigDecimalPolynomial(bigDecimalArray3);
        }
        int n4 = n3 / 2;
        BigDecimalPolynomial bigDecimalPolynomial2 = new BigDecimalPolynomial(this.copyOf(bigDecimalArray, n4));
        BigDecimalPolynomial bigDecimalPolynomial3 = new BigDecimalPolynomial(this.copyOfRange(bigDecimalArray, n4, n3));
        BigDecimalPolynomial bigDecimalPolynomial4 = new BigDecimalPolynomial(this.copyOf(bigDecimalArray2, n4));
        BigDecimalPolynomial bigDecimalPolynomial5 = new BigDecimalPolynomial(this.copyOfRange(bigDecimalArray2, n4, n3));
        BigDecimalPolynomial bigDecimalPolynomial6 = (BigDecimalPolynomial)bigDecimalPolynomial2.clone();
        bigDecimalPolynomial6.add(bigDecimalPolynomial3);
        BigDecimalPolynomial bigDecimalPolynomial7 = (BigDecimalPolynomial)bigDecimalPolynomial4.clone();
        bigDecimalPolynomial7.add(bigDecimalPolynomial5);
        BigDecimalPolynomial bigDecimalPolynomial8 = bigDecimalPolynomial2.multRecursive(bigDecimalPolynomial4);
        BigDecimalPolynomial bigDecimalPolynomial9 = bigDecimalPolynomial3.multRecursive(bigDecimalPolynomial5);
        BigDecimalPolynomial bigDecimalPolynomial10 = bigDecimalPolynomial6.multRecursive(bigDecimalPolynomial7);
        bigDecimalPolynomial10.sub(bigDecimalPolynomial8);
        bigDecimalPolynomial10.sub(bigDecimalPolynomial9);
        BigDecimalPolynomial bigDecimalPolynomial11 = new BigDecimalPolynomial(2 * n3 - 1);
        for (n2 = 0; n2 < bigDecimalPolynomial8.coeffs.length; ++n2) {
            bigDecimalPolynomial11.coeffs[n2] = bigDecimalPolynomial8.coeffs[n2];
        }
        for (n2 = 0; n2 < bigDecimalPolynomial10.coeffs.length; ++n2) {
            bigDecimalPolynomial11.coeffs[n4 + n2] = bigDecimalPolynomial11.coeffs[n4 + n2].add(bigDecimalPolynomial10.coeffs[n2]);
        }
        for (n2 = 0; n2 < bigDecimalPolynomial9.coeffs.length; ++n2) {
            bigDecimalPolynomial11.coeffs[2 * n4 + n2] = bigDecimalPolynomial11.coeffs[2 * n4 + n2].add(bigDecimalPolynomial9.coeffs[n2]);
        }
        return bigDecimalPolynomial11;
    }

    public void add(BigDecimalPolynomial bigDecimalPolynomial) {
        int n2;
        if (bigDecimalPolynomial.coeffs.length > this.coeffs.length) {
            n2 = this.coeffs.length;
            this.coeffs = this.copyOf(this.coeffs, bigDecimalPolynomial.coeffs.length);
            for (int i2 = n2; i2 < this.coeffs.length; ++i2) {
                this.coeffs[i2] = ZERO;
            }
        }
        for (n2 = 0; n2 < bigDecimalPolynomial.coeffs.length; ++n2) {
            this.coeffs[n2] = this.coeffs[n2].add(bigDecimalPolynomial.coeffs[n2]);
        }
    }

    void sub(BigDecimalPolynomial bigDecimalPolynomial) {
        int n2;
        if (bigDecimalPolynomial.coeffs.length > this.coeffs.length) {
            n2 = this.coeffs.length;
            this.coeffs = this.copyOf(this.coeffs, bigDecimalPolynomial.coeffs.length);
            for (int i2 = n2; i2 < this.coeffs.length; ++i2) {
                this.coeffs[i2] = ZERO;
            }
        }
        for (n2 = 0; n2 < bigDecimalPolynomial.coeffs.length; ++n2) {
            this.coeffs[n2] = this.coeffs[n2].subtract(bigDecimalPolynomial.coeffs[n2]);
        }
    }

    public BigIntPolynomial round() {
        int n2 = this.coeffs.length;
        BigIntPolynomial bigIntPolynomial = new BigIntPolynomial(n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            bigIntPolynomial.coeffs[i2] = this.coeffs[i2].setScale(0, 6).toBigInteger();
        }
        return bigIntPolynomial;
    }

    public Object clone() {
        return new BigDecimalPolynomial((BigDecimal[])this.coeffs.clone());
    }

    private BigDecimal[] copyOf(BigDecimal[] bigDecimalArray, int n2) {
        BigDecimal[] bigDecimalArray2 = new BigDecimal[n2];
        System.arraycopy(bigDecimalArray, 0, bigDecimalArray2, 0, bigDecimalArray.length < n2 ? bigDecimalArray.length : n2);
        return bigDecimalArray2;
    }

    private BigDecimal[] copyOfRange(BigDecimal[] bigDecimalArray, int n2, int n3) {
        int n4 = n3 - n2;
        BigDecimal[] bigDecimalArray2 = new BigDecimal[n3 - n2];
        System.arraycopy(bigDecimalArray, n2, bigDecimalArray2, 0, bigDecimalArray.length - n2 < n4 ? bigDecimalArray.length - n2 : n4);
        return bigDecimalArray2;
    }

    public BigDecimal[] getCoeffs() {
        BigDecimal[] bigDecimalArray = new BigDecimal[this.coeffs.length];
        System.arraycopy(this.coeffs, 0, bigDecimalArray, 0, this.coeffs.length);
        return bigDecimalArray;
    }
}

