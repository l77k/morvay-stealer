/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.linearalgebra;

import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2mField;
import org.bouncycastle.pqc.legacy.math.linearalgebra.PolynomialGF2mSmallM;

public class PolynomialRingGF2m {
    private GF2mField field;
    private PolynomialGF2mSmallM p;
    protected PolynomialGF2mSmallM[] sqMatrix;
    protected PolynomialGF2mSmallM[] sqRootMatrix;

    public PolynomialRingGF2m(GF2mField gF2mField, PolynomialGF2mSmallM polynomialGF2mSmallM) {
        this.field = gF2mField;
        this.p = polynomialGF2mSmallM;
        this.computeSquaringMatrix();
        this.computeSquareRootMatrix();
    }

    public PolynomialGF2mSmallM[] getSquaringMatrix() {
        return this.sqMatrix;
    }

    public PolynomialGF2mSmallM[] getSquareRootMatrix() {
        return this.sqRootMatrix;
    }

    private void computeSquaringMatrix() {
        int[] nArray;
        int n2;
        int n3 = this.p.getDegree();
        this.sqMatrix = new PolynomialGF2mSmallM[n3];
        for (n2 = 0; n2 < n3 >> 1; ++n2) {
            nArray = new int[(n2 << 1) + 1];
            nArray[n2 << 1] = 1;
            this.sqMatrix[n2] = new PolynomialGF2mSmallM(this.field, nArray);
        }
        for (n2 = n3 >> 1; n2 < n3; ++n2) {
            nArray = new int[(n2 << 1) + 1];
            nArray[n2 << 1] = 1;
            PolynomialGF2mSmallM polynomialGF2mSmallM = new PolynomialGF2mSmallM(this.field, nArray);
            this.sqMatrix[n2] = polynomialGF2mSmallM.mod(this.p);
        }
    }

    private void computeSquareRootMatrix() {
        int n2;
        int n3 = this.p.getDegree();
        PolynomialGF2mSmallM[] polynomialGF2mSmallMArray = new PolynomialGF2mSmallM[n3];
        for (n2 = n3 - 1; n2 >= 0; --n2) {
            polynomialGF2mSmallMArray[n2] = new PolynomialGF2mSmallM(this.sqMatrix[n2]);
        }
        this.sqRootMatrix = new PolynomialGF2mSmallM[n3];
        for (n2 = n3 - 1; n2 >= 0; --n2) {
            this.sqRootMatrix[n2] = new PolynomialGF2mSmallM(this.field, n2);
        }
        for (n2 = 0; n2 < n3; ++n2) {
            int n4;
            int n5;
            if (polynomialGF2mSmallMArray[n2].getCoefficient(n2) == 0) {
                n5 = 0;
                for (n4 = n2 + 1; n4 < n3; ++n4) {
                    if (polynomialGF2mSmallMArray[n4].getCoefficient(n2) == 0) continue;
                    n5 = 1;
                    PolynomialRingGF2m.swapColumns(polynomialGF2mSmallMArray, n2, n4);
                    PolynomialRingGF2m.swapColumns(this.sqRootMatrix, n2, n4);
                    n4 = n3;
                }
                if (n5 == 0) {
                    throw new ArithmeticException("Squaring matrix is not invertible.");
                }
            }
            n5 = polynomialGF2mSmallMArray[n2].getCoefficient(n2);
            n4 = this.field.inverse(n5);
            polynomialGF2mSmallMArray[n2].multThisWithElement(n4);
            this.sqRootMatrix[n2].multThisWithElement(n4);
            for (int i2 = 0; i2 < n3; ++i2) {
                if (i2 == n2 || (n5 = polynomialGF2mSmallMArray[i2].getCoefficient(n2)) == 0) continue;
                PolynomialGF2mSmallM polynomialGF2mSmallM = polynomialGF2mSmallMArray[n2].multWithElement(n5);
                PolynomialGF2mSmallM polynomialGF2mSmallM2 = this.sqRootMatrix[n2].multWithElement(n5);
                polynomialGF2mSmallMArray[i2].addToThis(polynomialGF2mSmallM);
                this.sqRootMatrix[i2].addToThis(polynomialGF2mSmallM2);
            }
        }
    }

    private static void swapColumns(PolynomialGF2mSmallM[] polynomialGF2mSmallMArray, int n2, int n3) {
        PolynomialGF2mSmallM polynomialGF2mSmallM = polynomialGF2mSmallMArray[n2];
        polynomialGF2mSmallMArray[n2] = polynomialGF2mSmallMArray[n3];
        polynomialGF2mSmallMArray[n3] = polynomialGF2mSmallM;
    }
}

