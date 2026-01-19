/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.linearalgebra;

import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2Polynomial;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2nElement;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2nField;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2nONBElement;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2nONBField;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2nPolynomialElement;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2nPolynomialField;
import org.bouncycastle.util.Arrays;

public class GF2nPolynomial {
    private GF2nElement[] coeff;
    private int size;

    public GF2nPolynomial(int n2, GF2nElement gF2nElement) {
        this.size = n2;
        this.coeff = new GF2nElement[this.size];
        for (int i2 = 0; i2 < this.size; ++i2) {
            this.coeff[i2] = (GF2nElement)gF2nElement.clone();
        }
    }

    private GF2nPolynomial(int n2) {
        this.size = n2;
        this.coeff = new GF2nElement[this.size];
    }

    public GF2nPolynomial(GF2nPolynomial gF2nPolynomial) {
        this.coeff = new GF2nElement[gF2nPolynomial.size];
        this.size = gF2nPolynomial.size;
        for (int i2 = 0; i2 < this.size; ++i2) {
            this.coeff[i2] = (GF2nElement)gF2nPolynomial.coeff[i2].clone();
        }
    }

    public GF2nPolynomial(GF2Polynomial gF2Polynomial, GF2nField gF2nField) {
        this.size = gF2nField.getDegree() + 1;
        this.coeff = new GF2nElement[this.size];
        if (gF2nField instanceof GF2nONBField) {
            for (int i2 = 0; i2 < this.size; ++i2) {
                this.coeff[i2] = gF2Polynomial.testBit(i2) ? GF2nONBElement.ONE((GF2nONBField)gF2nField) : GF2nONBElement.ZERO((GF2nONBField)gF2nField);
            }
        } else if (gF2nField instanceof GF2nPolynomialField) {
            for (int i3 = 0; i3 < this.size; ++i3) {
                this.coeff[i3] = gF2Polynomial.testBit(i3) ? GF2nPolynomialElement.ONE((GF2nPolynomialField)gF2nField) : GF2nPolynomialElement.ZERO((GF2nPolynomialField)gF2nField);
            }
        } else {
            throw new IllegalArgumentException("PolynomialGF2n(Bitstring, GF2nField): B1 must be an instance of GF2nONBField or GF2nPolynomialField!");
        }
    }

    public final void assignZeroToElements() {
        for (int i2 = 0; i2 < this.size; ++i2) {
            this.coeff[i2].assignZero();
        }
    }

    public final int size() {
        return this.size;
    }

    public final int getDegree() {
        for (int i2 = this.size - 1; i2 >= 0; --i2) {
            if (this.coeff[i2].isZero()) continue;
            return i2;
        }
        return -1;
    }

    public final void enlarge(int n2) {
        if (n2 <= this.size) {
            return;
        }
        GF2nElement[] gF2nElementArray = new GF2nElement[n2];
        System.arraycopy(this.coeff, 0, gF2nElementArray, 0, this.size);
        GF2nField gF2nField = this.coeff[0].getField();
        if (this.coeff[0] instanceof GF2nPolynomialElement) {
            for (int i2 = this.size; i2 < n2; ++i2) {
                gF2nElementArray[i2] = GF2nPolynomialElement.ZERO((GF2nPolynomialField)gF2nField);
            }
        } else if (this.coeff[0] instanceof GF2nONBElement) {
            for (int i3 = this.size; i3 < n2; ++i3) {
                gF2nElementArray[i3] = GF2nONBElement.ZERO((GF2nONBField)gF2nField);
            }
        }
        this.size = n2;
        this.coeff = gF2nElementArray;
    }

    public final void shrink() {
        int n2;
        for (n2 = this.size - 1; this.coeff[n2].isZero() && n2 > 0; --n2) {
        }
        if (++n2 < this.size) {
            GF2nElement[] gF2nElementArray = new GF2nElement[n2];
            System.arraycopy(this.coeff, 0, gF2nElementArray, 0, n2);
            this.coeff = gF2nElementArray;
            this.size = n2;
        }
    }

    public final void set(int n2, GF2nElement gF2nElement) {
        if (!(gF2nElement instanceof GF2nPolynomialElement) && !(gF2nElement instanceof GF2nONBElement)) {
            throw new IllegalArgumentException("PolynomialGF2n.set f must be an instance of either GF2nPolynomialElement or GF2nONBElement!");
        }
        this.coeff[n2] = (GF2nElement)gF2nElement.clone();
    }

    public final GF2nElement at(int n2) {
        return this.coeff[n2];
    }

    public final boolean isZero() {
        for (int i2 = 0; i2 < this.size; ++i2) {
            if (this.coeff[i2] == null || this.coeff[i2].isZero()) continue;
            return false;
        }
        return true;
    }

    public final boolean equals(Object object) {
        if (object == null || !(object instanceof GF2nPolynomial)) {
            return false;
        }
        GF2nPolynomial gF2nPolynomial = (GF2nPolynomial)object;
        if (this.getDegree() != gF2nPolynomial.getDegree()) {
            return false;
        }
        for (int i2 = 0; i2 < this.size; ++i2) {
            if (this.coeff[i2].equals(gF2nPolynomial.coeff[i2])) continue;
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.getDegree() * 7 + Arrays.hashCode(this.coeff);
    }

    public final GF2nPolynomial add(GF2nPolynomial gF2nPolynomial) {
        GF2nPolynomial gF2nPolynomial2;
        if (this.size() >= gF2nPolynomial.size()) {
            int n2;
            gF2nPolynomial2 = new GF2nPolynomial(this.size());
            for (n2 = 0; n2 < gF2nPolynomial.size(); ++n2) {
                gF2nPolynomial2.coeff[n2] = (GF2nElement)this.coeff[n2].add(gF2nPolynomial.coeff[n2]);
            }
            while (n2 < this.size()) {
                gF2nPolynomial2.coeff[n2] = this.coeff[n2];
                ++n2;
            }
        } else {
            int n3;
            gF2nPolynomial2 = new GF2nPolynomial(gF2nPolynomial.size());
            for (n3 = 0; n3 < this.size(); ++n3) {
                gF2nPolynomial2.coeff[n3] = (GF2nElement)this.coeff[n3].add(gF2nPolynomial.coeff[n3]);
            }
            while (n3 < gF2nPolynomial.size()) {
                gF2nPolynomial2.coeff[n3] = gF2nPolynomial.coeff[n3];
                ++n3;
            }
        }
        return gF2nPolynomial2;
    }

    public final GF2nPolynomial scalarMultiply(GF2nElement gF2nElement) {
        GF2nPolynomial gF2nPolynomial = new GF2nPolynomial(this.size());
        for (int i2 = 0; i2 < this.size(); ++i2) {
            gF2nPolynomial.coeff[i2] = (GF2nElement)this.coeff[i2].multiply(gF2nElement);
        }
        return gF2nPolynomial;
    }

    public final GF2nPolynomial multiply(GF2nPolynomial gF2nPolynomial) {
        int n2;
        int n3 = this.size();
        if (n3 != (n2 = gF2nPolynomial.size())) {
            throw new IllegalArgumentException("PolynomialGF2n.multiply: this and b must have the same size!");
        }
        GF2nPolynomial gF2nPolynomial2 = new GF2nPolynomial((n3 << 1) - 1);
        for (int i2 = 0; i2 < this.size(); ++i2) {
            for (int i3 = 0; i3 < gF2nPolynomial.size(); ++i3) {
                gF2nPolynomial2.coeff[i2 + i3] = gF2nPolynomial2.coeff[i2 + i3] == null ? (GF2nElement)this.coeff[i2].multiply(gF2nPolynomial.coeff[i3]) : (GF2nElement)gF2nPolynomial2.coeff[i2 + i3].add(this.coeff[i2].multiply(gF2nPolynomial.coeff[i3]));
            }
        }
        return gF2nPolynomial2;
    }

    public final GF2nPolynomial multiplyAndReduce(GF2nPolynomial gF2nPolynomial, GF2nPolynomial gF2nPolynomial2) {
        return this.multiply(gF2nPolynomial).reduce(gF2nPolynomial2);
    }

    public final GF2nPolynomial reduce(GF2nPolynomial gF2nPolynomial) throws RuntimeException, ArithmeticException {
        return this.remainder(gF2nPolynomial);
    }

    public final void shiftThisLeft(int n2) {
        block3: {
            int n3;
            GF2nField gF2nField;
            block4: {
                if (n2 <= 0) break block3;
                int n4 = this.size;
                gF2nField = this.coeff[0].getField();
                this.enlarge(this.size + n2);
                for (n3 = n4 - 1; n3 >= 0; --n3) {
                    this.coeff[n3 + n2] = this.coeff[n3];
                }
                if (!(this.coeff[0] instanceof GF2nPolynomialElement)) break block4;
                for (n3 = n2 - 1; n3 >= 0; --n3) {
                    this.coeff[n3] = GF2nPolynomialElement.ZERO((GF2nPolynomialField)gF2nField);
                }
                break block3;
            }
            if (!(this.coeff[0] instanceof GF2nONBElement)) break block3;
            for (n3 = n2 - 1; n3 >= 0; --n3) {
                this.coeff[n3] = GF2nONBElement.ZERO((GF2nONBField)gF2nField);
            }
        }
    }

    public final GF2nPolynomial shiftLeft(int n2) {
        if (n2 <= 0) {
            return new GF2nPolynomial(this);
        }
        GF2nPolynomial gF2nPolynomial = new GF2nPolynomial(this.size + n2, this.coeff[0]);
        gF2nPolynomial.assignZeroToElements();
        for (int i2 = 0; i2 < this.size; ++i2) {
            gF2nPolynomial.coeff[i2 + n2] = this.coeff[i2];
        }
        return gF2nPolynomial;
    }

    public final GF2nPolynomial[] divide(GF2nPolynomial gF2nPolynomial) {
        GF2nPolynomial[] gF2nPolynomialArray = new GF2nPolynomial[2];
        GF2nPolynomial gF2nPolynomial2 = new GF2nPolynomial(this);
        gF2nPolynomial2.shrink();
        int n2 = gF2nPolynomial.getDegree();
        GF2nElement gF2nElement = (GF2nElement)gF2nPolynomial.coeff[n2].invert();
        if (gF2nPolynomial2.getDegree() < n2) {
            gF2nPolynomialArray[0] = new GF2nPolynomial(this);
            gF2nPolynomialArray[0].assignZeroToElements();
            gF2nPolynomialArray[0].shrink();
            gF2nPolynomialArray[1] = new GF2nPolynomial(this);
            gF2nPolynomialArray[1].shrink();
            return gF2nPolynomialArray;
        }
        gF2nPolynomialArray[0] = new GF2nPolynomial(this);
        gF2nPolynomialArray[0].assignZeroToElements();
        int n3 = gF2nPolynomial2.getDegree() - n2;
        while (n3 >= 0) {
            GF2nElement gF2nElement2 = (GF2nElement)gF2nPolynomial2.coeff[gF2nPolynomial2.getDegree()].multiply(gF2nElement);
            GF2nPolynomial gF2nPolynomial3 = gF2nPolynomial.scalarMultiply(gF2nElement2);
            gF2nPolynomial3.shiftThisLeft(n3);
            gF2nPolynomial2 = gF2nPolynomial2.add(gF2nPolynomial3);
            gF2nPolynomial2.shrink();
            gF2nPolynomialArray[0].coeff[n3] = (GF2nElement)gF2nElement2.clone();
            n3 = gF2nPolynomial2.getDegree() - n2;
        }
        gF2nPolynomialArray[1] = gF2nPolynomial2;
        gF2nPolynomialArray[0].shrink();
        return gF2nPolynomialArray;
    }

    public final GF2nPolynomial remainder(GF2nPolynomial gF2nPolynomial) throws RuntimeException, ArithmeticException {
        GF2nPolynomial[] gF2nPolynomialArray = new GF2nPolynomial[2];
        gF2nPolynomialArray = this.divide(gF2nPolynomial);
        return gF2nPolynomialArray[1];
    }

    public final GF2nPolynomial quotient(GF2nPolynomial gF2nPolynomial) throws RuntimeException, ArithmeticException {
        GF2nPolynomial[] gF2nPolynomialArray = new GF2nPolynomial[2];
        gF2nPolynomialArray = this.divide(gF2nPolynomial);
        return gF2nPolynomialArray[0];
    }

    public final GF2nPolynomial gcd(GF2nPolynomial gF2nPolynomial) {
        GF2nPolynomial gF2nPolynomial2 = new GF2nPolynomial(this);
        GF2nPolynomial gF2nPolynomial3 = new GF2nPolynomial(gF2nPolynomial);
        gF2nPolynomial2.shrink();
        gF2nPolynomial3.shrink();
        while (!gF2nPolynomial3.isZero()) {
            GF2nPolynomial gF2nPolynomial4 = gF2nPolynomial2.remainder(gF2nPolynomial3);
            gF2nPolynomial2 = gF2nPolynomial3;
            gF2nPolynomial3 = gF2nPolynomial4;
        }
        GF2nElement gF2nElement = gF2nPolynomial2.coeff[gF2nPolynomial2.getDegree()];
        GF2nPolynomial gF2nPolynomial5 = gF2nPolynomial2.scalarMultiply((GF2nElement)gF2nElement.invert());
        return gF2nPolynomial5;
    }
}

