/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.linearalgebra;

import java.security.SecureRandom;
import java.util.Vector;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2Polynomial;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2nElement;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2nField;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2nONBElement;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2nONBField;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2nPolynomial;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2nPolynomialElement;

public class GF2nPolynomialField
extends GF2nField {
    GF2Polynomial[] squaringMatrix;
    private boolean isTrinomial = false;
    private boolean isPentanomial = false;
    private int tc;
    private int[] pc = new int[3];

    public GF2nPolynomialField(int n2, SecureRandom secureRandom) {
        super(secureRandom);
        if (n2 < 3) {
            throw new IllegalArgumentException("k must be at least 3");
        }
        this.mDegree = n2;
        this.computeFieldPolynomial();
        this.computeSquaringMatrix();
        this.fields = new Vector();
        this.matrices = new Vector();
    }

    public GF2nPolynomialField(int n2, SecureRandom secureRandom, boolean bl) {
        super(secureRandom);
        if (n2 < 3) {
            throw new IllegalArgumentException("k must be at least 3");
        }
        this.mDegree = n2;
        if (bl) {
            this.computeFieldPolynomial();
        } else {
            this.computeFieldPolynomial2();
        }
        this.computeSquaringMatrix();
        this.fields = new Vector();
        this.matrices = new Vector();
    }

    public GF2nPolynomialField(int n2, SecureRandom secureRandom, GF2Polynomial gF2Polynomial) throws RuntimeException {
        super(secureRandom);
        if (n2 < 3) {
            throw new IllegalArgumentException("degree must be at least 3");
        }
        if (gF2Polynomial.getLength() != n2 + 1) {
            throw new RuntimeException();
        }
        if (!gF2Polynomial.isIrreducible()) {
            throw new RuntimeException();
        }
        this.mDegree = n2;
        this.fieldPolynomial = gF2Polynomial;
        this.computeSquaringMatrix();
        int n3 = 2;
        for (int i2 = 1; i2 < this.fieldPolynomial.getLength() - 1; ++i2) {
            if (!this.fieldPolynomial.testBit(i2)) continue;
            if (++n3 == 3) {
                this.tc = i2;
            }
            if (n3 > 5) continue;
            this.pc[n3 - 3] = i2;
        }
        if (n3 == 3) {
            this.isTrinomial = true;
        }
        if (n3 == 5) {
            this.isPentanomial = true;
        }
        this.fields = new Vector();
        this.matrices = new Vector();
    }

    public boolean isTrinomial() {
        return this.isTrinomial;
    }

    public boolean isPentanomial() {
        return this.isPentanomial;
    }

    public int getTc() throws RuntimeException {
        if (!this.isTrinomial) {
            throw new RuntimeException();
        }
        return this.tc;
    }

    public int[] getPc() throws RuntimeException {
        if (!this.isPentanomial) {
            throw new RuntimeException();
        }
        int[] nArray = new int[3];
        System.arraycopy(this.pc, 0, nArray, 0, 3);
        return nArray;
    }

    public GF2Polynomial getSquaringVector(int n2) {
        return new GF2Polynomial(this.squaringMatrix[n2]);
    }

    @Override
    protected GF2nElement getRandomRoot(GF2Polynomial gF2Polynomial) {
        GF2nPolynomial gF2nPolynomial = new GF2nPolynomial(gF2Polynomial, this);
        int n2 = gF2nPolynomial.getDegree();
        while (n2 > 1) {
            GF2nPolynomial gF2nPolynomial2;
            int n3;
            do {
                GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this, this.random);
                GF2nPolynomial gF2nPolynomial3 = new GF2nPolynomial(2, GF2nPolynomialElement.ZERO(this));
                gF2nPolynomial3.set(1, gF2nPolynomialElement);
                GF2nPolynomial gF2nPolynomial4 = new GF2nPolynomial(gF2nPolynomial3);
                for (int i2 = 1; i2 <= this.mDegree - 1; ++i2) {
                    gF2nPolynomial4 = gF2nPolynomial4.multiplyAndReduce(gF2nPolynomial4, gF2nPolynomial);
                    gF2nPolynomial4 = gF2nPolynomial4.add(gF2nPolynomial3);
                }
                gF2nPolynomial2 = gF2nPolynomial4.gcd(gF2nPolynomial);
                n3 = gF2nPolynomial2.getDegree();
                n2 = gF2nPolynomial.getDegree();
            } while (n3 == 0 || n3 == n2);
            gF2nPolynomial = n3 << 1 > n2 ? gF2nPolynomial.quotient(gF2nPolynomial2) : new GF2nPolynomial(gF2nPolynomial2);
            n2 = gF2nPolynomial.getDegree();
        }
        return gF2nPolynomial.at(0);
    }

    @Override
    protected void computeCOBMatrix(GF2nField gF2nField) {
        GF2nElement[] gF2nElementArray;
        GF2nElement gF2nElement;
        int n2;
        if (this.mDegree != gF2nField.mDegree) {
            throw new IllegalArgumentException("GF2nPolynomialField.computeCOBMatrix: B1 has a different degree and thus cannot be coverted to!");
        }
        if (gF2nField instanceof GF2nONBField) {
            gF2nField.computeCOBMatrix(this);
            return;
        }
        GF2Polynomial[] gF2PolynomialArray = new GF2Polynomial[this.mDegree];
        for (n2 = 0; n2 < this.mDegree; ++n2) {
            gF2PolynomialArray[n2] = new GF2Polynomial(this.mDegree);
        }
        while ((gF2nElement = gF2nField.getRandomRoot(this.fieldPolynomial)).isZero()) {
        }
        if (gF2nElement instanceof GF2nONBElement) {
            gF2nElementArray = new GF2nONBElement[this.mDegree];
            gF2nElementArray[this.mDegree - 1] = GF2nONBElement.ONE((GF2nONBField)gF2nField);
        } else {
            gF2nElementArray = new GF2nPolynomialElement[this.mDegree];
            gF2nElementArray[this.mDegree - 1] = GF2nPolynomialElement.ONE((GF2nPolynomialField)gF2nField);
        }
        gF2nElementArray[this.mDegree - 2] = gF2nElement;
        for (n2 = this.mDegree - 3; n2 >= 0; --n2) {
            gF2nElementArray[n2] = (GF2nElement)gF2nElementArray[n2 + 1].multiply(gF2nElement);
        }
        if (gF2nField instanceof GF2nONBField) {
            for (n2 = 0; n2 < this.mDegree; ++n2) {
                for (int i2 = 0; i2 < this.mDegree; ++i2) {
                    if (!gF2nElementArray[n2].testBit(this.mDegree - i2 - 1)) continue;
                    gF2PolynomialArray[this.mDegree - i2 - 1].setBit(this.mDegree - n2 - 1);
                }
            }
        } else {
            for (n2 = 0; n2 < this.mDegree; ++n2) {
                for (int i3 = 0; i3 < this.mDegree; ++i3) {
                    if (!gF2nElementArray[n2].testBit(i3)) continue;
                    gF2PolynomialArray[this.mDegree - i3 - 1].setBit(this.mDegree - n2 - 1);
                }
            }
        }
        this.fields.addElement(gF2nField);
        this.matrices.addElement(gF2PolynomialArray);
        gF2nField.fields.addElement(this);
        gF2nField.matrices.addElement(this.invertMatrix(gF2PolynomialArray));
    }

    private void computeSquaringMatrix() {
        int n2;
        GF2Polynomial[] gF2PolynomialArray = new GF2Polynomial[this.mDegree - 1];
        this.squaringMatrix = new GF2Polynomial[this.mDegree];
        for (n2 = 0; n2 < this.squaringMatrix.length; ++n2) {
            this.squaringMatrix[n2] = new GF2Polynomial(this.mDegree, "ZERO");
        }
        for (n2 = 0; n2 < this.mDegree - 1; ++n2) {
            gF2PolynomialArray[n2] = new GF2Polynomial(1, "ONE").shiftLeft(this.mDegree + n2).remainder(this.fieldPolynomial);
        }
        for (n2 = 1; n2 <= Math.abs(this.mDegree >> 1); ++n2) {
            for (int i2 = 1; i2 <= this.mDegree; ++i2) {
                if (!gF2PolynomialArray[this.mDegree - (n2 << 1)].testBit(this.mDegree - i2)) continue;
                this.squaringMatrix[i2 - 1].setBit(this.mDegree - n2);
            }
        }
        for (n2 = Math.abs(this.mDegree >> 1) + 1; n2 <= this.mDegree; ++n2) {
            this.squaringMatrix[(n2 << 1) - this.mDegree - 1].setBit(this.mDegree - n2);
        }
    }

    @Override
    protected void computeFieldPolynomial() {
        if (this.testTrinomials()) {
            return;
        }
        if (this.testPentanomials()) {
            return;
        }
        this.testRandom();
    }

    protected void computeFieldPolynomial2() {
        if (this.testTrinomials()) {
            return;
        }
        if (this.testPentanomials()) {
            return;
        }
        this.testRandom();
    }

    private boolean testTrinomials() {
        boolean bl = false;
        int n2 = 0;
        this.fieldPolynomial = new GF2Polynomial(this.mDegree + 1);
        this.fieldPolynomial.setBit(0);
        this.fieldPolynomial.setBit(this.mDegree);
        for (int i2 = 1; i2 < this.mDegree && !bl; ++i2) {
            this.fieldPolynomial.setBit(i2);
            bl = this.fieldPolynomial.isIrreducible();
            ++n2;
            if (bl) {
                this.isTrinomial = true;
                this.tc = i2;
                return bl;
            }
            this.fieldPolynomial.resetBit(i2);
            bl = this.fieldPolynomial.isIrreducible();
        }
        return bl;
    }

    private boolean testPentanomials() {
        boolean bl = false;
        int n2 = 0;
        this.fieldPolynomial = new GF2Polynomial(this.mDegree + 1);
        this.fieldPolynomial.setBit(0);
        this.fieldPolynomial.setBit(this.mDegree);
        for (int i2 = 1; i2 <= this.mDegree - 3 && !bl; ++i2) {
            this.fieldPolynomial.setBit(i2);
            for (int i3 = i2 + 1; i3 <= this.mDegree - 2 && !bl; ++i3) {
                this.fieldPolynomial.setBit(i3);
                for (int i4 = i3 + 1; i4 <= this.mDegree - 1 && !bl; ++i4) {
                    this.fieldPolynomial.setBit(i4);
                    if ((this.mDegree & 1) != 0 | (i2 & 1) != 0 | (i3 & 1) != 0 | (i4 & 1) != 0) {
                        bl = this.fieldPolynomial.isIrreducible();
                        ++n2;
                        if (bl) {
                            this.isPentanomial = true;
                            this.pc[0] = i2;
                            this.pc[1] = i3;
                            this.pc[2] = i4;
                            return bl;
                        }
                    }
                    this.fieldPolynomial.resetBit(i4);
                }
                this.fieldPolynomial.resetBit(i3);
            }
            this.fieldPolynomial.resetBit(i2);
        }
        return bl;
    }

    private boolean testRandom() {
        boolean bl = false;
        this.fieldPolynomial = new GF2Polynomial(this.mDegree + 1);
        int n2 = 0;
        while (!bl) {
            ++n2;
            this.fieldPolynomial.randomize();
            this.fieldPolynomial.setBit(this.mDegree);
            this.fieldPolynomial.setBit(0);
            if (!this.fieldPolynomial.isIrreducible()) continue;
            bl = true;
            return bl;
        }
        return bl;
    }
}

