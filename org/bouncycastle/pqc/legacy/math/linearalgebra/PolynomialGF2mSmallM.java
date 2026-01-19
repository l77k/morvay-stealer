/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.linearalgebra;

import java.security.SecureRandom;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2mField;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2mVector;
import org.bouncycastle.pqc.legacy.math.linearalgebra.IntUtils;
import org.bouncycastle.pqc.legacy.math.linearalgebra.RandUtils;

public class PolynomialGF2mSmallM {
    private GF2mField field;
    private int degree;
    private int[] coefficients;
    public static final char RANDOM_IRREDUCIBLE_POLYNOMIAL = 'I';

    public PolynomialGF2mSmallM(GF2mField gF2mField) {
        this.field = gF2mField;
        this.degree = -1;
        this.coefficients = new int[1];
    }

    public PolynomialGF2mSmallM(GF2mField gF2mField, int n2, char c2, SecureRandom secureRandom) {
        this.field = gF2mField;
        switch (c2) {
            case 'I': {
                this.coefficients = this.createRandomIrreduciblePolynomial(n2, secureRandom);
                break;
            }
            default: {
                throw new IllegalArgumentException(" Error: type " + c2 + " is not defined for GF2smallmPolynomial");
            }
        }
        this.computeDegree();
    }

    private int[] createRandomIrreduciblePolynomial(int n2, SecureRandom secureRandom) {
        int n3;
        int[] nArray = new int[n2 + 1];
        nArray[n2] = 1;
        nArray[0] = this.field.getRandomNonZeroElement(secureRandom);
        for (n3 = 1; n3 < n2; ++n3) {
            nArray[n3] = this.field.getRandomElement(secureRandom);
        }
        while (!this.isIrreducible(nArray)) {
            n3 = RandUtils.nextInt(secureRandom, n2);
            if (n3 == 0) {
                nArray[0] = this.field.getRandomNonZeroElement(secureRandom);
                continue;
            }
            nArray[n3] = this.field.getRandomElement(secureRandom);
        }
        return nArray;
    }

    public PolynomialGF2mSmallM(GF2mField gF2mField, int n2) {
        this.field = gF2mField;
        this.degree = n2;
        this.coefficients = new int[n2 + 1];
        this.coefficients[n2] = 1;
    }

    public PolynomialGF2mSmallM(GF2mField gF2mField, int[] nArray) {
        this.field = gF2mField;
        this.coefficients = PolynomialGF2mSmallM.normalForm(nArray);
        this.computeDegree();
    }

    public PolynomialGF2mSmallM(GF2mField gF2mField, byte[] byArray) {
        int n2;
        this.field = gF2mField;
        int n3 = 1;
        for (n2 = 8; gF2mField.getDegree() > n2; n2 += 8) {
            ++n3;
        }
        if (byArray.length % n3 != 0) {
            throw new IllegalArgumentException(" Error: byte array is not encoded polynomial over given finite field GF2m");
        }
        this.coefficients = new int[byArray.length / n3];
        n3 = 0;
        for (int i2 = 0; i2 < this.coefficients.length; ++i2) {
            for (int i3 = 0; i3 < n2; i3 += 8) {
                int n4 = i2;
                this.coefficients[n4] = this.coefficients[n4] ^ (byArray[n3++] & 0xFF) << i3;
            }
            if (this.field.isElementOfThisField(this.coefficients[i2])) continue;
            throw new IllegalArgumentException(" Error: byte array is not encoded polynomial over given finite field GF2m");
        }
        if (this.coefficients.length != 1 && this.coefficients[this.coefficients.length - 1] == 0) {
            throw new IllegalArgumentException(" Error: byte array is not encoded polynomial over given finite field GF2m");
        }
        this.computeDegree();
    }

    public PolynomialGF2mSmallM(PolynomialGF2mSmallM polynomialGF2mSmallM) {
        this.field = polynomialGF2mSmallM.field;
        this.degree = polynomialGF2mSmallM.degree;
        this.coefficients = IntUtils.clone(polynomialGF2mSmallM.coefficients);
    }

    public PolynomialGF2mSmallM(GF2mVector gF2mVector) {
        this(gF2mVector.getField(), gF2mVector.getIntArrayForm());
    }

    public int getDegree() {
        int n2 = this.coefficients.length - 1;
        if (this.coefficients[n2] == 0) {
            return -1;
        }
        return n2;
    }

    public int getHeadCoefficient() {
        if (this.degree == -1) {
            return 0;
        }
        return this.coefficients[this.degree];
    }

    private static int headCoefficient(int[] nArray) {
        int n2 = PolynomialGF2mSmallM.computeDegree(nArray);
        if (n2 == -1) {
            return 0;
        }
        return nArray[n2];
    }

    public int getCoefficient(int n2) {
        if (n2 < 0 || n2 > this.degree) {
            return 0;
        }
        return this.coefficients[n2];
    }

    public byte[] getEncoded() {
        int n2;
        int n3 = 1;
        for (n2 = 8; this.field.getDegree() > n2; n2 += 8) {
            ++n3;
        }
        byte[] byArray = new byte[this.coefficients.length * n3];
        n3 = 0;
        for (int i2 = 0; i2 < this.coefficients.length; ++i2) {
            for (int i3 = 0; i3 < n2; i3 += 8) {
                byArray[n3++] = (byte)(this.coefficients[i2] >>> i3);
            }
        }
        return byArray;
    }

    public int evaluateAt(int n2) {
        int n3 = this.coefficients[this.degree];
        for (int i2 = this.degree - 1; i2 >= 0; --i2) {
            n3 = this.field.mult(n3, n2) ^ this.coefficients[i2];
        }
        return n3;
    }

    public PolynomialGF2mSmallM add(PolynomialGF2mSmallM polynomialGF2mSmallM) {
        int[] nArray = this.add(this.coefficients, polynomialGF2mSmallM.coefficients);
        return new PolynomialGF2mSmallM(this.field, nArray);
    }

    public void addToThis(PolynomialGF2mSmallM polynomialGF2mSmallM) {
        this.coefficients = this.add(this.coefficients, polynomialGF2mSmallM.coefficients);
        this.computeDegree();
    }

    private int[] add(int[] nArray, int[] nArray2) {
        int[] nArray3;
        int[] nArray4;
        if (nArray.length < nArray2.length) {
            nArray4 = new int[nArray2.length];
            System.arraycopy(nArray2, 0, nArray4, 0, nArray2.length);
            nArray3 = nArray;
        } else {
            nArray4 = new int[nArray.length];
            System.arraycopy(nArray, 0, nArray4, 0, nArray.length);
            nArray3 = nArray2;
        }
        for (int i2 = nArray3.length - 1; i2 >= 0; --i2) {
            nArray4[i2] = this.field.add(nArray4[i2], nArray3[i2]);
        }
        return nArray4;
    }

    public PolynomialGF2mSmallM addMonomial(int n2) {
        int[] nArray = new int[n2 + 1];
        nArray[n2] = 1;
        int[] nArray2 = this.add(this.coefficients, nArray);
        return new PolynomialGF2mSmallM(this.field, nArray2);
    }

    public PolynomialGF2mSmallM multWithElement(int n2) {
        if (!this.field.isElementOfThisField(n2)) {
            throw new ArithmeticException("Not an element of the finite field this polynomial is defined over.");
        }
        int[] nArray = this.multWithElement(this.coefficients, n2);
        return new PolynomialGF2mSmallM(this.field, nArray);
    }

    public void multThisWithElement(int n2) {
        if (!this.field.isElementOfThisField(n2)) {
            throw new ArithmeticException("Not an element of the finite field this polynomial is defined over.");
        }
        this.coefficients = this.multWithElement(this.coefficients, n2);
        this.computeDegree();
    }

    private int[] multWithElement(int[] nArray, int n2) {
        int n3 = PolynomialGF2mSmallM.computeDegree(nArray);
        if (n3 == -1 || n2 == 0) {
            return new int[1];
        }
        if (n2 == 1) {
            return IntUtils.clone(nArray);
        }
        int[] nArray2 = new int[n3 + 1];
        for (int i2 = n3; i2 >= 0; --i2) {
            nArray2[i2] = this.field.mult(nArray[i2], n2);
        }
        return nArray2;
    }

    public PolynomialGF2mSmallM multWithMonomial(int n2) {
        int[] nArray = PolynomialGF2mSmallM.multWithMonomial(this.coefficients, n2);
        return new PolynomialGF2mSmallM(this.field, nArray);
    }

    private static int[] multWithMonomial(int[] nArray, int n2) {
        int n3 = PolynomialGF2mSmallM.computeDegree(nArray);
        if (n3 == -1) {
            return new int[1];
        }
        int[] nArray2 = new int[n3 + n2 + 1];
        System.arraycopy(nArray, 0, nArray2, n2, n3 + 1);
        return nArray2;
    }

    public PolynomialGF2mSmallM[] div(PolynomialGF2mSmallM polynomialGF2mSmallM) {
        int[][] nArray = this.div(this.coefficients, polynomialGF2mSmallM.coefficients);
        return new PolynomialGF2mSmallM[]{new PolynomialGF2mSmallM(this.field, nArray[0]), new PolynomialGF2mSmallM(this.field, nArray[1])};
    }

    private int[][] div(int[] nArray, int[] nArray2) {
        int n2 = PolynomialGF2mSmallM.computeDegree(nArray2);
        int n3 = PolynomialGF2mSmallM.computeDegree(nArray) + 1;
        if (n2 == -1) {
            throw new ArithmeticException("Division by zero.");
        }
        int[][] nArrayArray = new int[][]{new int[1], new int[n3]};
        int n4 = PolynomialGF2mSmallM.headCoefficient(nArray2);
        n4 = this.field.inverse(n4);
        nArrayArray[0][0] = 0;
        System.arraycopy(nArray, 0, nArrayArray[1], 0, nArrayArray[1].length);
        while (n2 <= PolynomialGF2mSmallM.computeDegree(nArrayArray[1])) {
            int[] nArray3 = new int[]{this.field.mult(PolynomialGF2mSmallM.headCoefficient(nArrayArray[1]), n4)};
            int[] nArray4 = this.multWithElement(nArray2, nArray3[0]);
            int n5 = PolynomialGF2mSmallM.computeDegree(nArrayArray[1]) - n2;
            nArray4 = PolynomialGF2mSmallM.multWithMonomial(nArray4, n5);
            nArray3 = PolynomialGF2mSmallM.multWithMonomial(nArray3, n5);
            nArrayArray[0] = this.add(nArray3, nArrayArray[0]);
            nArrayArray[1] = this.add(nArray4, nArrayArray[1]);
        }
        return nArrayArray;
    }

    public PolynomialGF2mSmallM gcd(PolynomialGF2mSmallM polynomialGF2mSmallM) {
        int[] nArray = this.gcd(this.coefficients, polynomialGF2mSmallM.coefficients);
        return new PolynomialGF2mSmallM(this.field, nArray);
    }

    private int[] gcd(int[] nArray, int[] nArray2) {
        int[] nArray3 = nArray;
        int[] nArray4 = nArray2;
        if (PolynomialGF2mSmallM.computeDegree(nArray3) == -1) {
            return nArray4;
        }
        while (PolynomialGF2mSmallM.computeDegree(nArray4) != -1) {
            int[] nArray5 = this.mod(nArray3, nArray4);
            nArray3 = new int[nArray4.length];
            System.arraycopy(nArray4, 0, nArray3, 0, nArray3.length);
            nArray4 = new int[nArray5.length];
            System.arraycopy(nArray5, 0, nArray4, 0, nArray4.length);
        }
        int n2 = this.field.inverse(PolynomialGF2mSmallM.headCoefficient(nArray3));
        return this.multWithElement(nArray3, n2);
    }

    public PolynomialGF2mSmallM multiply(PolynomialGF2mSmallM polynomialGF2mSmallM) {
        int[] nArray = this.multiply(this.coefficients, polynomialGF2mSmallM.coefficients);
        return new PolynomialGF2mSmallM(this.field, nArray);
    }

    private int[] multiply(int[] nArray, int[] nArray2) {
        int[] nArray3;
        int[] nArray4;
        if (PolynomialGF2mSmallM.computeDegree(nArray) < PolynomialGF2mSmallM.computeDegree(nArray2)) {
            nArray4 = nArray2;
            nArray3 = nArray;
        } else {
            nArray4 = nArray;
            nArray3 = nArray2;
        }
        nArray4 = PolynomialGF2mSmallM.normalForm(nArray4);
        nArray3 = PolynomialGF2mSmallM.normalForm(nArray3);
        if (nArray3.length == 1) {
            return this.multWithElement(nArray4, nArray3[0]);
        }
        int n2 = nArray4.length;
        int n3 = nArray3.length;
        int[] nArray5 = new int[n2 + n3 - 1];
        if (n3 != n2) {
            int[] nArray6 = new int[n3];
            int[] nArray7 = new int[n2 - n3];
            System.arraycopy(nArray4, 0, nArray6, 0, nArray6.length);
            System.arraycopy(nArray4, n3, nArray7, 0, nArray7.length);
            nArray6 = this.multiply(nArray6, nArray3);
            nArray7 = this.multiply(nArray7, nArray3);
            nArray7 = PolynomialGF2mSmallM.multWithMonomial(nArray7, n3);
            nArray5 = this.add(nArray6, nArray7);
        } else {
            n3 = n2 + 1 >>> 1;
            int n4 = n2 - n3;
            int[] nArray8 = new int[n3];
            int[] nArray9 = new int[n3];
            int[] nArray10 = new int[n4];
            int[] nArray11 = new int[n4];
            System.arraycopy(nArray4, 0, nArray8, 0, nArray8.length);
            System.arraycopy(nArray4, n3, nArray10, 0, nArray10.length);
            System.arraycopy(nArray3, 0, nArray9, 0, nArray9.length);
            System.arraycopy(nArray3, n3, nArray11, 0, nArray11.length);
            int[] nArray12 = this.add(nArray8, nArray10);
            int[] nArray13 = this.add(nArray9, nArray11);
            int[] nArray14 = this.multiply(nArray8, nArray9);
            int[] nArray15 = this.multiply(nArray12, nArray13);
            int[] nArray16 = this.multiply(nArray10, nArray11);
            nArray15 = this.add(nArray15, nArray14);
            nArray15 = this.add(nArray15, nArray16);
            nArray16 = PolynomialGF2mSmallM.multWithMonomial(nArray16, n3);
            nArray5 = this.add(nArray15, nArray16);
            nArray5 = PolynomialGF2mSmallM.multWithMonomial(nArray5, n3);
            nArray5 = this.add(nArray5, nArray14);
        }
        return nArray5;
    }

    private boolean isIrreducible(int[] nArray) {
        if (nArray[0] == 0) {
            return false;
        }
        int n2 = PolynomialGF2mSmallM.computeDegree(nArray) >> 1;
        int[] nArray2 = new int[]{0, 1};
        int[] nArray3 = new int[]{0, 1};
        int n3 = this.field.getDegree();
        for (int i2 = 0; i2 < n2; ++i2) {
            for (int i3 = n3 - 1; i3 >= 0; --i3) {
                nArray2 = this.modMultiply(nArray2, nArray2, nArray);
            }
            int[] nArray4 = this.gcd(this.add(nArray2 = PolynomialGF2mSmallM.normalForm(nArray2), nArray3), nArray);
            if (PolynomialGF2mSmallM.computeDegree(nArray4) == 0) continue;
            return false;
        }
        return true;
    }

    public PolynomialGF2mSmallM mod(PolynomialGF2mSmallM polynomialGF2mSmallM) {
        int[] nArray = this.mod(this.coefficients, polynomialGF2mSmallM.coefficients);
        return new PolynomialGF2mSmallM(this.field, nArray);
    }

    private int[] mod(int[] nArray, int[] nArray2) {
        int n2 = PolynomialGF2mSmallM.computeDegree(nArray2);
        if (n2 == -1) {
            throw new ArithmeticException("Division by zero");
        }
        int[] nArray3 = new int[nArray.length];
        int n3 = PolynomialGF2mSmallM.headCoefficient(nArray2);
        n3 = this.field.inverse(n3);
        System.arraycopy(nArray, 0, nArray3, 0, nArray3.length);
        while (n2 <= PolynomialGF2mSmallM.computeDegree(nArray3)) {
            int n4 = this.field.mult(PolynomialGF2mSmallM.headCoefficient(nArray3), n3);
            int[] nArray4 = PolynomialGF2mSmallM.multWithMonomial(nArray2, PolynomialGF2mSmallM.computeDegree(nArray3) - n2);
            nArray4 = this.multWithElement(nArray4, n4);
            nArray3 = this.add(nArray4, nArray3);
        }
        return nArray3;
    }

    public PolynomialGF2mSmallM modMultiply(PolynomialGF2mSmallM polynomialGF2mSmallM, PolynomialGF2mSmallM polynomialGF2mSmallM2) {
        int[] nArray = this.modMultiply(this.coefficients, polynomialGF2mSmallM.coefficients, polynomialGF2mSmallM2.coefficients);
        return new PolynomialGF2mSmallM(this.field, nArray);
    }

    public PolynomialGF2mSmallM modSquareMatrix(PolynomialGF2mSmallM[] polynomialGF2mSmallMArray) {
        int n2;
        int n3 = polynomialGF2mSmallMArray.length;
        int[] nArray = new int[n3];
        int[] nArray2 = new int[n3];
        for (n2 = 0; n2 < this.coefficients.length; ++n2) {
            nArray2[n2] = this.field.mult(this.coefficients[n2], this.coefficients[n2]);
        }
        for (n2 = 0; n2 < n3; ++n2) {
            for (int i2 = 0; i2 < n3; ++i2) {
                if (n2 >= polynomialGF2mSmallMArray[i2].coefficients.length) continue;
                int n4 = this.field.mult(polynomialGF2mSmallMArray[i2].coefficients[n2], nArray2[i2]);
                nArray[n2] = this.field.add(nArray[n2], n4);
            }
        }
        return new PolynomialGF2mSmallM(this.field, nArray);
    }

    private int[] modMultiply(int[] nArray, int[] nArray2, int[] nArray3) {
        return this.mod(this.multiply(nArray, nArray2), nArray3);
    }

    public PolynomialGF2mSmallM modSquareRoot(PolynomialGF2mSmallM polynomialGF2mSmallM) {
        int[] nArray = IntUtils.clone(this.coefficients);
        int[] nArray2 = this.modMultiply(nArray, nArray, polynomialGF2mSmallM.coefficients);
        while (!PolynomialGF2mSmallM.isEqual(nArray2, this.coefficients)) {
            nArray = PolynomialGF2mSmallM.normalForm(nArray2);
            nArray2 = this.modMultiply(nArray, nArray, polynomialGF2mSmallM.coefficients);
        }
        return new PolynomialGF2mSmallM(this.field, nArray);
    }

    public PolynomialGF2mSmallM modSquareRootMatrix(PolynomialGF2mSmallM[] polynomialGF2mSmallMArray) {
        int n2;
        int n3 = polynomialGF2mSmallMArray.length;
        int[] nArray = new int[n3];
        for (n2 = 0; n2 < n3; ++n2) {
            for (int i2 = 0; i2 < n3; ++i2) {
                if (n2 >= polynomialGF2mSmallMArray[i2].coefficients.length || i2 >= this.coefficients.length) continue;
                int n4 = this.field.mult(polynomialGF2mSmallMArray[i2].coefficients[n2], this.coefficients[i2]);
                nArray[n2] = this.field.add(nArray[n2], n4);
            }
        }
        for (n2 = 0; n2 < n3; ++n2) {
            nArray[n2] = this.field.sqRoot(nArray[n2]);
        }
        return new PolynomialGF2mSmallM(this.field, nArray);
    }

    public PolynomialGF2mSmallM modDiv(PolynomialGF2mSmallM polynomialGF2mSmallM, PolynomialGF2mSmallM polynomialGF2mSmallM2) {
        int[] nArray = this.modDiv(this.coefficients, polynomialGF2mSmallM.coefficients, polynomialGF2mSmallM2.coefficients);
        return new PolynomialGF2mSmallM(this.field, nArray);
    }

    private int[] modDiv(int[] nArray, int[] nArray2, int[] nArray3) {
        int[] nArray4 = PolynomialGF2mSmallM.normalForm(nArray3);
        int[] nArray5 = this.mod(nArray2, nArray3);
        int[] nArray6 = new int[]{0};
        int[] nArray7 = this.mod(nArray, nArray3);
        while (PolynomialGF2mSmallM.computeDegree(nArray5) != -1) {
            int[][] nArray8 = this.div(nArray4, nArray5);
            nArray4 = PolynomialGF2mSmallM.normalForm(nArray5);
            nArray5 = PolynomialGF2mSmallM.normalForm(nArray8[1]);
            int[] nArray9 = this.add(nArray6, this.modMultiply(nArray8[0], nArray7, nArray3));
            nArray6 = PolynomialGF2mSmallM.normalForm(nArray7);
            nArray7 = PolynomialGF2mSmallM.normalForm(nArray9);
        }
        int n2 = PolynomialGF2mSmallM.headCoefficient(nArray4);
        nArray6 = this.multWithElement(nArray6, this.field.inverse(n2));
        return nArray6;
    }

    public PolynomialGF2mSmallM modInverse(PolynomialGF2mSmallM polynomialGF2mSmallM) {
        int[] nArray = new int[]{1};
        int[] nArray2 = this.modDiv(nArray, this.coefficients, polynomialGF2mSmallM.coefficients);
        return new PolynomialGF2mSmallM(this.field, nArray2);
    }

    public PolynomialGF2mSmallM[] modPolynomialToFracton(PolynomialGF2mSmallM polynomialGF2mSmallM) {
        int n2 = polynomialGF2mSmallM.degree >> 1;
        int[] nArray = PolynomialGF2mSmallM.normalForm(polynomialGF2mSmallM.coefficients);
        int[] nArray2 = this.mod(this.coefficients, polynomialGF2mSmallM.coefficients);
        int[] nArray3 = new int[]{0};
        int[] nArray4 = new int[]{1};
        while (PolynomialGF2mSmallM.computeDegree(nArray2) > n2) {
            int[][] nArray5 = this.div(nArray, nArray2);
            nArray = nArray2;
            nArray2 = nArray5[1];
            int[] nArray6 = this.add(nArray3, this.modMultiply(nArray5[0], nArray4, polynomialGF2mSmallM.coefficients));
            nArray3 = nArray4;
            nArray4 = nArray6;
        }
        return new PolynomialGF2mSmallM[]{new PolynomialGF2mSmallM(this.field, nArray2), new PolynomialGF2mSmallM(this.field, nArray4)};
    }

    public boolean equals(Object object) {
        if (object == null || !(object instanceof PolynomialGF2mSmallM)) {
            return false;
        }
        PolynomialGF2mSmallM polynomialGF2mSmallM = (PolynomialGF2mSmallM)object;
        return this.field.equals(polynomialGF2mSmallM.field) && this.degree == polynomialGF2mSmallM.degree && PolynomialGF2mSmallM.isEqual(this.coefficients, polynomialGF2mSmallM.coefficients);
    }

    private static boolean isEqual(int[] nArray, int[] nArray2) {
        int n2;
        int n3 = PolynomialGF2mSmallM.computeDegree(nArray);
        if (n3 != (n2 = PolynomialGF2mSmallM.computeDegree(nArray2))) {
            return false;
        }
        for (int i2 = 0; i2 <= n3; ++i2) {
            if (nArray[i2] == nArray2[i2]) continue;
            return false;
        }
        return true;
    }

    public int hashCode() {
        int n2 = this.field.hashCode();
        for (int i2 = 0; i2 < this.coefficients.length; ++i2) {
            n2 = n2 * 31 + this.coefficients[i2];
        }
        return n2;
    }

    public String toString() {
        String string = " Polynomial over " + this.field.toString() + ": \n";
        for (int i2 = 0; i2 < this.coefficients.length; ++i2) {
            string = string + this.field.elementToStr(this.coefficients[i2]) + "Y^" + i2 + "+";
        }
        string = string + ";";
        return string;
    }

    private void computeDegree() {
        this.degree = this.coefficients.length - 1;
        while (this.degree >= 0 && this.coefficients[this.degree] == 0) {
            --this.degree;
        }
    }

    private static int computeDegree(int[] nArray) {
        int n2;
        for (n2 = nArray.length - 1; n2 >= 0 && nArray[n2] == 0; --n2) {
        }
        return n2;
    }

    private static int[] normalForm(int[] nArray) {
        int n2 = PolynomialGF2mSmallM.computeDegree(nArray);
        if (n2 == -1) {
            return new int[1];
        }
        if (nArray.length == n2 + 1) {
            return IntUtils.clone(nArray);
        }
        int[] nArray2 = new int[n2 + 1];
        System.arraycopy(nArray, 0, nArray2, 0, n2 + 1);
        return nArray2;
    }
}

