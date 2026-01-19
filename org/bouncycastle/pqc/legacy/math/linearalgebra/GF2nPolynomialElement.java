/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.linearalgebra;

import java.math.BigInteger;
import java.util.Random;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2Polynomial;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2nElement;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2nPolynomialField;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GFElement;
import org.bouncycastle.pqc.legacy.math.linearalgebra.IntegerFunctions;

public class GF2nPolynomialElement
extends GF2nElement {
    private static final int[] bitMask = new int[]{1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 0x100000, 0x200000, 0x400000, 0x800000, 0x1000000, 0x2000000, 0x4000000, 0x8000000, 0x10000000, 0x20000000, 0x40000000, Integer.MIN_VALUE, 0};
    private GF2Polynomial polynomial;

    public GF2nPolynomialElement(GF2nPolynomialField gF2nPolynomialField, Random random) {
        this.mField = gF2nPolynomialField;
        this.mDegree = this.mField.getDegree();
        this.polynomial = new GF2Polynomial(this.mDegree);
        this.randomize(random);
    }

    public GF2nPolynomialElement(GF2nPolynomialField gF2nPolynomialField, GF2Polynomial gF2Polynomial) {
        this.mField = gF2nPolynomialField;
        this.mDegree = this.mField.getDegree();
        this.polynomial = new GF2Polynomial(gF2Polynomial);
        this.polynomial.expandN(this.mDegree);
    }

    public GF2nPolynomialElement(GF2nPolynomialField gF2nPolynomialField, byte[] byArray) {
        this.mField = gF2nPolynomialField;
        this.mDegree = this.mField.getDegree();
        this.polynomial = new GF2Polynomial(this.mDegree, byArray);
        this.polynomial.expandN(this.mDegree);
    }

    public GF2nPolynomialElement(GF2nPolynomialField gF2nPolynomialField, int[] nArray) {
        this.mField = gF2nPolynomialField;
        this.mDegree = this.mField.getDegree();
        this.polynomial = new GF2Polynomial(this.mDegree, nArray);
        this.polynomial.expandN(gF2nPolynomialField.mDegree);
    }

    public GF2nPolynomialElement(GF2nPolynomialElement gF2nPolynomialElement) {
        this.mField = gF2nPolynomialElement.mField;
        this.mDegree = gF2nPolynomialElement.mDegree;
        this.polynomial = new GF2Polynomial(gF2nPolynomialElement.polynomial);
    }

    @Override
    public Object clone() {
        return new GF2nPolynomialElement(this);
    }

    @Override
    void assignZero() {
        this.polynomial.assignZero();
    }

    public static GF2nPolynomialElement ZERO(GF2nPolynomialField gF2nPolynomialField) {
        GF2Polynomial gF2Polynomial = new GF2Polynomial(gF2nPolynomialField.getDegree());
        return new GF2nPolynomialElement(gF2nPolynomialField, gF2Polynomial);
    }

    public static GF2nPolynomialElement ONE(GF2nPolynomialField gF2nPolynomialField) {
        GF2Polynomial gF2Polynomial = new GF2Polynomial(gF2nPolynomialField.getDegree(), new int[]{1});
        return new GF2nPolynomialElement(gF2nPolynomialField, gF2Polynomial);
    }

    @Override
    void assignOne() {
        this.polynomial.assignOne();
    }

    private void randomize(Random random) {
        this.polynomial.expandN(this.mDegree);
        this.polynomial.randomize(random);
    }

    @Override
    public boolean isZero() {
        return this.polynomial.isZero();
    }

    @Override
    public boolean isOne() {
        return this.polynomial.isOne();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof GF2nPolynomialElement)) {
            return false;
        }
        GF2nPolynomialElement gF2nPolynomialElement = (GF2nPolynomialElement)object;
        if (this.mField != gF2nPolynomialElement.mField && !this.mField.getFieldPolynomial().equals(gF2nPolynomialElement.mField.getFieldPolynomial())) {
            return false;
        }
        return this.polynomial.equals(gF2nPolynomialElement.polynomial);
    }

    @Override
    public int hashCode() {
        return this.mField.hashCode() + this.polynomial.hashCode();
    }

    private GF2Polynomial getGF2Polynomial() {
        return new GF2Polynomial(this.polynomial);
    }

    @Override
    boolean testBit(int n2) {
        return this.polynomial.testBit(n2);
    }

    @Override
    public boolean testRightmostBit() {
        return this.polynomial.testBit(0);
    }

    @Override
    public GFElement add(GFElement gFElement) throws RuntimeException {
        GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this);
        gF2nPolynomialElement.addToThis(gFElement);
        return gF2nPolynomialElement;
    }

    @Override
    public void addToThis(GFElement gFElement) throws RuntimeException {
        if (!(gFElement instanceof GF2nPolynomialElement)) {
            throw new RuntimeException();
        }
        if (!this.mField.equals(((GF2nPolynomialElement)gFElement).mField)) {
            throw new RuntimeException();
        }
        this.polynomial.addToThis(((GF2nPolynomialElement)gFElement).polynomial);
    }

    @Override
    public GF2nElement increase() {
        GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this);
        gF2nPolynomialElement.increaseThis();
        return gF2nPolynomialElement;
    }

    @Override
    public void increaseThis() {
        this.polynomial.increaseThis();
    }

    @Override
    public GFElement multiply(GFElement gFElement) throws RuntimeException {
        GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this);
        gF2nPolynomialElement.multiplyThisBy(gFElement);
        return gF2nPolynomialElement;
    }

    @Override
    public void multiplyThisBy(GFElement gFElement) throws RuntimeException {
        if (!(gFElement instanceof GF2nPolynomialElement)) {
            throw new RuntimeException();
        }
        if (!this.mField.equals(((GF2nPolynomialElement)gFElement).mField)) {
            throw new RuntimeException();
        }
        if (this.equals(gFElement)) {
            this.squareThis();
            return;
        }
        this.polynomial = this.polynomial.multiply(((GF2nPolynomialElement)gFElement).polynomial);
        this.reduceThis();
    }

    @Override
    public GFElement invert() throws ArithmeticException {
        return this.invertMAIA();
    }

    public GF2nPolynomialElement invertEEA() throws ArithmeticException {
        if (this.isZero()) {
            throw new ArithmeticException();
        }
        GF2Polynomial gF2Polynomial = new GF2Polynomial(this.mDegree + 32, "ONE");
        gF2Polynomial.reduceN();
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(this.mDegree + 32);
        gF2Polynomial2.reduceN();
        GF2Polynomial gF2Polynomial3 = this.getGF2Polynomial();
        GF2Polynomial gF2Polynomial4 = this.mField.getFieldPolynomial();
        gF2Polynomial3.reduceN();
        while (!gF2Polynomial3.isOne()) {
            gF2Polynomial3.reduceN();
            gF2Polynomial4.reduceN();
            int n2 = gF2Polynomial3.getLength() - gF2Polynomial4.getLength();
            if (n2 < 0) {
                GF2Polynomial gF2Polynomial5 = gF2Polynomial3;
                gF2Polynomial3 = gF2Polynomial4;
                gF2Polynomial4 = gF2Polynomial5;
                gF2Polynomial5 = gF2Polynomial;
                gF2Polynomial = gF2Polynomial2;
                gF2Polynomial2 = gF2Polynomial5;
                n2 = -n2;
                gF2Polynomial2.reduceN();
            }
            gF2Polynomial3.shiftLeftAddThis(gF2Polynomial4, n2);
            gF2Polynomial.shiftLeftAddThis(gF2Polynomial2, n2);
        }
        gF2Polynomial.reduceN();
        return new GF2nPolynomialElement((GF2nPolynomialField)this.mField, gF2Polynomial);
    }

    public GF2nPolynomialElement invertSquare() throws ArithmeticException {
        if (this.isZero()) {
            throw new ArithmeticException();
        }
        int n2 = this.mField.getDegree() - 1;
        GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this);
        gF2nPolynomialElement.polynomial.expandN((this.mDegree << 1) + 32);
        gF2nPolynomialElement.polynomial.reduceN();
        int n3 = 1;
        for (int i2 = IntegerFunctions.floorLog(n2) - 1; i2 >= 0; --i2) {
            GF2nPolynomialElement gF2nPolynomialElement2 = new GF2nPolynomialElement(gF2nPolynomialElement);
            for (int i3 = 1; i3 <= n3; ++i3) {
                gF2nPolynomialElement2.squareThisPreCalc();
            }
            gF2nPolynomialElement.multiplyThisBy(gF2nPolynomialElement2);
            n3 <<= 1;
            if ((n2 & bitMask[i2]) == 0) continue;
            gF2nPolynomialElement.squareThisPreCalc();
            gF2nPolynomialElement.multiplyThisBy(this);
            ++n3;
        }
        gF2nPolynomialElement.squareThisPreCalc();
        return gF2nPolynomialElement;
    }

    public GF2nPolynomialElement invertMAIA() throws ArithmeticException {
        if (this.isZero()) {
            throw new ArithmeticException();
        }
        GF2Polynomial gF2Polynomial = new GF2Polynomial(this.mDegree, "ONE");
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(this.mDegree);
        GF2Polynomial gF2Polynomial3 = this.getGF2Polynomial();
        GF2Polynomial gF2Polynomial4 = this.mField.getFieldPolynomial();
        while (true) {
            if (!gF2Polynomial3.testBit(0)) {
                gF2Polynomial3.shiftRightThis();
                if (!gF2Polynomial.testBit(0)) {
                    gF2Polynomial.shiftRightThis();
                    continue;
                }
                gF2Polynomial.addToThis(this.mField.getFieldPolynomial());
                gF2Polynomial.shiftRightThis();
                continue;
            }
            if (gF2Polynomial3.isOne()) {
                return new GF2nPolynomialElement((GF2nPolynomialField)this.mField, gF2Polynomial);
            }
            gF2Polynomial3.reduceN();
            gF2Polynomial4.reduceN();
            if (gF2Polynomial3.getLength() < gF2Polynomial4.getLength()) {
                GF2Polynomial gF2Polynomial5 = gF2Polynomial3;
                gF2Polynomial3 = gF2Polynomial4;
                gF2Polynomial4 = gF2Polynomial5;
                gF2Polynomial5 = gF2Polynomial;
                gF2Polynomial = gF2Polynomial2;
                gF2Polynomial2 = gF2Polynomial5;
            }
            gF2Polynomial3.addToThis(gF2Polynomial4);
            gF2Polynomial.addToThis(gF2Polynomial2);
        }
    }

    @Override
    public GF2nElement square() {
        return this.squarePreCalc();
    }

    @Override
    public void squareThis() {
        this.squareThisPreCalc();
    }

    public GF2nPolynomialElement squareMatrix() {
        GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this);
        gF2nPolynomialElement.squareThisMatrix();
        gF2nPolynomialElement.reduceThis();
        return gF2nPolynomialElement;
    }

    public void squareThisMatrix() {
        GF2Polynomial gF2Polynomial = new GF2Polynomial(this.mDegree);
        for (int i2 = 0; i2 < this.mDegree; ++i2) {
            if (!this.polynomial.vectorMult(((GF2nPolynomialField)this.mField).squaringMatrix[this.mDegree - i2 - 1])) continue;
            gF2Polynomial.setBit(i2);
        }
        this.polynomial = gF2Polynomial;
    }

    public GF2nPolynomialElement squareBitwise() {
        GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this);
        gF2nPolynomialElement.squareThisBitwise();
        gF2nPolynomialElement.reduceThis();
        return gF2nPolynomialElement;
    }

    public void squareThisBitwise() {
        this.polynomial.squareThisBitwise();
        this.reduceThis();
    }

    public GF2nPolynomialElement squarePreCalc() {
        GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this);
        gF2nPolynomialElement.squareThisPreCalc();
        gF2nPolynomialElement.reduceThis();
        return gF2nPolynomialElement;
    }

    public void squareThisPreCalc() {
        this.polynomial.squareThisPreCalc();
        this.reduceThis();
    }

    public GF2nPolynomialElement power(int n2) {
        if (n2 == 1) {
            return new GF2nPolynomialElement(this);
        }
        GF2nPolynomialElement gF2nPolynomialElement = GF2nPolynomialElement.ONE((GF2nPolynomialField)this.mField);
        if (n2 == 0) {
            return gF2nPolynomialElement;
        }
        GF2nPolynomialElement gF2nPolynomialElement2 = new GF2nPolynomialElement(this);
        gF2nPolynomialElement2.polynomial.expandN((gF2nPolynomialElement2.mDegree << 1) + 32);
        gF2nPolynomialElement2.polynomial.reduceN();
        for (int i2 = 0; i2 < this.mDegree; ++i2) {
            if ((n2 & 1 << i2) != 0) {
                gF2nPolynomialElement.multiplyThisBy(gF2nPolynomialElement2);
            }
            gF2nPolynomialElement2.square();
        }
        return gF2nPolynomialElement;
    }

    @Override
    public GF2nElement squareRoot() {
        GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this);
        gF2nPolynomialElement.squareRootThis();
        return gF2nPolynomialElement;
    }

    @Override
    public void squareRootThis() {
        this.polynomial.expandN((this.mDegree << 1) + 32);
        this.polynomial.reduceN();
        for (int i2 = 0; i2 < this.mField.getDegree() - 1; ++i2) {
            this.squareThis();
        }
    }

    @Override
    public GF2nElement solveQuadraticEquation() throws RuntimeException {
        GF2nPolynomialElement gF2nPolynomialElement;
        GF2nPolynomialElement gF2nPolynomialElement2;
        if (this.isZero()) {
            return GF2nPolynomialElement.ZERO((GF2nPolynomialField)this.mField);
        }
        if ((this.mDegree & 1) == 1) {
            return this.halfTrace();
        }
        do {
            GF2nPolynomialElement gF2nPolynomialElement3 = new GF2nPolynomialElement((GF2nPolynomialField)this.mField, new Random());
            gF2nPolynomialElement = GF2nPolynomialElement.ZERO((GF2nPolynomialField)this.mField);
            gF2nPolynomialElement2 = (GF2nPolynomialElement)gF2nPolynomialElement3.clone();
            for (int i2 = 1; i2 < this.mDegree; ++i2) {
                gF2nPolynomialElement.squareThis();
                gF2nPolynomialElement2.squareThis();
                gF2nPolynomialElement.addToThis(gF2nPolynomialElement2.multiply(this));
                gF2nPolynomialElement2.addToThis(gF2nPolynomialElement3);
            }
        } while (gF2nPolynomialElement2.isZero());
        if (!this.equals(gF2nPolynomialElement.square().add(gF2nPolynomialElement))) {
            throw new RuntimeException();
        }
        return gF2nPolynomialElement;
    }

    @Override
    public int trace() {
        GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this);
        for (int i2 = 1; i2 < this.mDegree; ++i2) {
            gF2nPolynomialElement.squareThis();
            gF2nPolynomialElement.addToThis(this);
        }
        if (gF2nPolynomialElement.isOne()) {
            return 1;
        }
        return 0;
    }

    private GF2nPolynomialElement halfTrace() throws RuntimeException {
        if ((this.mDegree & 1) == 0) {
            throw new RuntimeException();
        }
        GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this);
        for (int i2 = 1; i2 <= this.mDegree - 1 >> 1; ++i2) {
            gF2nPolynomialElement.squareThis();
            gF2nPolynomialElement.squareThis();
            gF2nPolynomialElement.addToThis(this);
        }
        return gF2nPolynomialElement;
    }

    private void reduceThis() {
        if (this.polynomial.getLength() > this.mDegree) {
            if (((GF2nPolynomialField)this.mField).isTrinomial()) {
                int n2;
                try {
                    n2 = ((GF2nPolynomialField)this.mField).getTc();
                }
                catch (RuntimeException runtimeException) {
                    throw new RuntimeException("GF2nPolynomialElement.reduce: the field polynomial is not a trinomial");
                }
                if (this.mDegree - n2 <= 32 || this.polynomial.getLength() > this.mDegree << 1) {
                    this.reduceTrinomialBitwise(n2);
                    return;
                }
                this.polynomial.reduceTrinomial(this.mDegree, n2);
                return;
            }
            if (((GF2nPolynomialField)this.mField).isPentanomial()) {
                int[] nArray;
                try {
                    nArray = ((GF2nPolynomialField)this.mField).getPc();
                }
                catch (RuntimeException runtimeException) {
                    throw new RuntimeException("GF2nPolynomialElement.reduce: the field polynomial is not a pentanomial");
                }
                if (this.mDegree - nArray[2] <= 32 || this.polynomial.getLength() > this.mDegree << 1) {
                    this.reducePentanomialBitwise(nArray);
                    return;
                }
                this.polynomial.reducePentanomial(this.mDegree, nArray);
                return;
            }
            this.polynomial = this.polynomial.remainder(this.mField.getFieldPolynomial());
            this.polynomial.expandN(this.mDegree);
            return;
        }
        if (this.polynomial.getLength() < this.mDegree) {
            this.polynomial.expandN(this.mDegree);
        }
    }

    private void reduceTrinomialBitwise(int n2) {
        int n3 = this.mDegree - n2;
        for (int i2 = this.polynomial.getLength() - 1; i2 >= this.mDegree; --i2) {
            if (!this.polynomial.testBit(i2)) continue;
            this.polynomial.xorBit(i2);
            this.polynomial.xorBit(i2 - n3);
            this.polynomial.xorBit(i2 - this.mDegree);
        }
        this.polynomial.reduceN();
        this.polynomial.expandN(this.mDegree);
    }

    private void reducePentanomialBitwise(int[] nArray) {
        int n2 = this.mDegree - nArray[2];
        int n3 = this.mDegree - nArray[1];
        int n4 = this.mDegree - nArray[0];
        for (int i2 = this.polynomial.getLength() - 1; i2 >= this.mDegree; --i2) {
            if (!this.polynomial.testBit(i2)) continue;
            this.polynomial.xorBit(i2);
            this.polynomial.xorBit(i2 - n2);
            this.polynomial.xorBit(i2 - n3);
            this.polynomial.xorBit(i2 - n4);
            this.polynomial.xorBit(i2 - this.mDegree);
        }
        this.polynomial.reduceN();
        this.polynomial.expandN(this.mDegree);
    }

    @Override
    public String toString() {
        return this.polynomial.toString(16);
    }

    @Override
    public String toString(int n2) {
        return this.polynomial.toString(n2);
    }

    @Override
    public byte[] toByteArray() {
        return this.polynomial.toByteArray();
    }

    @Override
    public BigInteger toFlexiBigInt() {
        return this.polynomial.toFlexiBigInt();
    }
}

