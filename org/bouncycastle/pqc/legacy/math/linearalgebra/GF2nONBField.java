/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.linearalgebra;

import java.security.SecureRandom;
import java.util.Random;
import java.util.Vector;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2Polynomial;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2nElement;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2nField;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2nONBElement;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2nPolynomial;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2nPolynomialElement;
import org.bouncycastle.pqc.legacy.math.linearalgebra.IntegerFunctions;

public class GF2nONBField
extends GF2nField {
    private static final int MAXLONG = 64;
    private int mLength;
    private int mBit;
    private int mType;
    int[][] mMult;

    public GF2nONBField(int n2, SecureRandom secureRandom) throws RuntimeException {
        super(secureRandom);
        if (n2 < 3) {
            throw new IllegalArgumentException("k must be at least 3");
        }
        this.mDegree = n2;
        this.mLength = this.mDegree / 64;
        this.mBit = this.mDegree & 0x3F;
        if (this.mBit == 0) {
            this.mBit = 64;
        } else {
            ++this.mLength;
        }
        this.computeType();
        if (this.mType < 3) {
            this.mMult = new int[this.mDegree][2];
            for (int i2 = 0; i2 < this.mDegree; ++i2) {
                this.mMult[i2][0] = -1;
                this.mMult[i2][1] = -1;
            }
        } else {
            throw new RuntimeException("\nThe type of this field is " + this.mType);
        }
        this.computeMultMatrix();
        this.computeFieldPolynomial();
        this.fields = new Vector();
        this.matrices = new Vector();
    }

    int getONBLength() {
        return this.mLength;
    }

    int getONBBit() {
        return this.mBit;
    }

    @Override
    protected GF2nElement getRandomRoot(GF2Polynomial gF2Polynomial) {
        GF2nPolynomial gF2nPolynomial = new GF2nPolynomial(gF2Polynomial, this);
        int n2 = gF2nPolynomial.getDegree();
        while (n2 > 1) {
            GF2nPolynomial gF2nPolynomial2;
            int n3;
            do {
                GF2nONBElement gF2nONBElement = new GF2nONBElement(this, this.random);
                GF2nPolynomial gF2nPolynomial3 = new GF2nPolynomial(2, GF2nONBElement.ZERO(this));
                gF2nPolynomial3.set(1, gF2nONBElement);
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
        GF2nElement gF2nElement;
        int n2;
        if (this.mDegree != gF2nField.mDegree) {
            throw new IllegalArgumentException("GF2nField.computeCOBMatrix: B1 has a different degree and thus cannot be coverted to!");
        }
        GF2Polynomial[] gF2PolynomialArray = new GF2Polynomial[this.mDegree];
        for (n2 = 0; n2 < this.mDegree; ++n2) {
            gF2PolynomialArray[n2] = new GF2Polynomial(this.mDegree);
        }
        while ((gF2nElement = gF2nField.getRandomRoot(this.fieldPolynomial)).isZero()) {
        }
        GF2nPolynomialElement[] gF2nPolynomialElementArray = new GF2nPolynomialElement[this.mDegree];
        gF2nPolynomialElementArray[0] = (GF2nElement)gF2nElement.clone();
        for (n2 = 1; n2 < this.mDegree; ++n2) {
            gF2nPolynomialElementArray[n2] = ((GF2nElement)gF2nPolynomialElementArray[n2 - 1]).square();
        }
        for (n2 = 0; n2 < this.mDegree; ++n2) {
            for (int i2 = 0; i2 < this.mDegree; ++i2) {
                if (!((GF2nElement)gF2nPolynomialElementArray[n2]).testBit(i2)) continue;
                gF2PolynomialArray[this.mDegree - i2 - 1].setBit(this.mDegree - n2 - 1);
            }
        }
        this.fields.addElement(gF2nField);
        this.matrices.addElement(gF2PolynomialArray);
        gF2nField.fields.addElement(this);
        gF2nField.matrices.addElement(this.invertMatrix(gF2PolynomialArray));
    }

    @Override
    protected void computeFieldPolynomial() {
        if (this.mType == 1) {
            this.fieldPolynomial = new GF2Polynomial(this.mDegree + 1, "ALL");
        } else if (this.mType == 2) {
            GF2Polynomial gF2Polynomial = new GF2Polynomial(this.mDegree + 1, "ONE");
            GF2Polynomial gF2Polynomial2 = new GF2Polynomial(this.mDegree + 1, "X");
            gF2Polynomial2.addToThis(gF2Polynomial);
            for (int i2 = 1; i2 < this.mDegree; ++i2) {
                GF2Polynomial gF2Polynomial3 = gF2Polynomial;
                gF2Polynomial = gF2Polynomial2;
                gF2Polynomial2 = gF2Polynomial.shiftLeft();
                gF2Polynomial2.addToThis(gF2Polynomial3);
            }
            this.fieldPolynomial = gF2Polynomial2;
        }
    }

    int[][] invMatrix(int[][] nArray) {
        int n2;
        int[][] nArray2 = new int[this.mDegree][this.mDegree];
        nArray2 = nArray;
        int[][] nArray3 = new int[this.mDegree][this.mDegree];
        for (n2 = 0; n2 < this.mDegree; ++n2) {
            nArray3[n2][n2] = 1;
        }
        for (n2 = 0; n2 < this.mDegree; ++n2) {
            for (int i2 = n2; i2 < this.mDegree; ++i2) {
                nArray2[this.mDegree - 1 - n2][i2] = nArray2[n2][n2];
            }
        }
        return null;
    }

    private void computeType() throws RuntimeException {
        if ((this.mDegree & 7) == 0) {
            throw new RuntimeException("The extension degree is divisible by 8!");
        }
        int n2 = 0;
        int n3 = 0;
        this.mType = 1;
        int n4 = 0;
        while (n4 != 1) {
            n2 = this.mType * this.mDegree + 1;
            if (IntegerFunctions.isPrime(n2)) {
                n3 = IntegerFunctions.order(2, n2);
                n4 = IntegerFunctions.gcd(this.mType * this.mDegree / n3, this.mDegree);
            }
            ++this.mType;
        }
        --this.mType;
        if (this.mType == 1 && IntegerFunctions.isPrime(n2 = (this.mDegree << 1) + 1) && (n4 = IntegerFunctions.gcd((this.mDegree << 1) / (n3 = IntegerFunctions.order(2, n2)), this.mDegree)) == 1) {
            ++this.mType;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void computeMultMatrix() {
        int n2;
        int n3;
        if ((this.mType & 7) == 0) throw new RuntimeException("bisher nur fuer Gausssche Normalbasen implementiert");
        int n4 = this.mType * this.mDegree + 1;
        int[] nArray = new int[n4];
        int n5 = this.mType == 1 ? 1 : (this.mType == 2 ? n4 - 1 : this.elementOfOrder(this.mType, n4));
        int n6 = 1;
        for (n3 = 0; n3 < this.mType; ++n3) {
            int n7 = n6;
            for (n2 = 0; n2 < this.mDegree; ++n2) {
                nArray[n7] = n2;
                if ((n7 = (n7 << 1) % n4) >= 0) continue;
                n7 += n4;
            }
            if ((n6 = n5 * n6 % n4) >= 0) continue;
            n6 += n4;
        }
        if (this.mType == 1) {
            for (n3 = 1; n3 < n4 - 1; ++n3) {
                if (this.mMult[nArray[n3 + 1]][0] == -1) {
                    this.mMult[nArray[n3 + 1]][0] = nArray[n4 - n3];
                    continue;
                }
                this.mMult[nArray[n3 + 1]][1] = nArray[n4 - n3];
            }
            n3 = this.mDegree >> 1;
            for (n2 = 1; n2 <= n3; ++n2) {
                if (this.mMult[n2 - 1][0] == -1) {
                    this.mMult[n2 - 1][0] = n3 + n2 - 1;
                } else {
                    this.mMult[n2 - 1][1] = n3 + n2 - 1;
                }
                if (this.mMult[n3 + n2 - 1][0] == -1) {
                    this.mMult[n3 + n2 - 1][0] = n2 - 1;
                    continue;
                }
                this.mMult[n3 + n2 - 1][1] = n2 - 1;
            }
            return;
        } else {
            if (this.mType != 2) throw new RuntimeException("only type 1 or type 2 implemented");
            for (n3 = 1; n3 < n4 - 1; ++n3) {
                if (this.mMult[nArray[n3 + 1]][0] == -1) {
                    this.mMult[nArray[n3 + 1]][0] = nArray[n4 - n3];
                    continue;
                }
                this.mMult[nArray[n3 + 1]][1] = nArray[n4 - n3];
            }
        }
    }

    private int elementOfOrder(int n2, int n3) {
        Random random = new Random();
        int n4 = 0;
        while (n4 == 0) {
            n4 = random.nextInt();
            if ((n4 %= n3 - 1) >= 0) continue;
            n4 += n3 - 1;
        }
        int n5 = IntegerFunctions.order(n4, n3);
        while (n5 % n2 != 0 || n5 == 0) {
            while (n4 == 0) {
                n4 = random.nextInt();
                if ((n4 %= n3 - 1) >= 0) continue;
                n4 += n3 - 1;
            }
            n5 = IntegerFunctions.order(n4, n3);
        }
        int n6 = n4;
        n5 = n2 / n5;
        for (int i2 = 2; i2 <= n5; ++i2) {
            n6 *= n4;
        }
        return n6;
    }
}

