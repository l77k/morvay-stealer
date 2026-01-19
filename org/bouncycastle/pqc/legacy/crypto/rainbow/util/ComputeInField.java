/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.rainbow.util;

import org.bouncycastle.pqc.legacy.crypto.rainbow.util.GF2Field;

public class ComputeInField {
    private short[][] A;
    short[] x;

    public short[] solveEquation(short[][] sArray, short[] sArray2) {
        if (sArray.length != sArray2.length) {
            return null;
        }
        try {
            int n2;
            this.A = new short[sArray.length][sArray.length + 1];
            this.x = new short[sArray.length];
            for (n2 = 0; n2 < sArray.length; ++n2) {
                for (int i2 = 0; i2 < sArray[0].length; ++i2) {
                    this.A[n2][i2] = sArray[n2][i2];
                }
            }
            for (n2 = 0; n2 < sArray2.length; ++n2) {
                this.A[n2][sArray2.length] = GF2Field.addElem(sArray2[n2], this.A[n2][sArray2.length]);
            }
            this.computeZerosUnder(false);
            this.substitute();
            return this.x;
        }
        catch (RuntimeException runtimeException) {
            return null;
        }
    }

    public short[][] inverse(short[][] sArray) {
        try {
            int n2;
            int n3;
            this.A = new short[sArray.length][2 * sArray.length];
            if (sArray.length != sArray[0].length) {
                throw new RuntimeException("The matrix is not invertible. Please choose another one!");
            }
            for (n3 = 0; n3 < sArray.length; ++n3) {
                for (n2 = 0; n2 < sArray.length; ++n2) {
                    this.A[n3][n2] = sArray[n3][n2];
                }
                for (n2 = sArray.length; n2 < 2 * sArray.length; ++n2) {
                    this.A[n3][n2] = 0;
                }
                this.A[n3][n3 + this.A.length] = 1;
            }
            this.computeZerosUnder(true);
            for (n3 = 0; n3 < this.A.length; ++n3) {
                short s2 = GF2Field.invElem(this.A[n3][n3]);
                for (n2 = n3; n2 < 2 * this.A.length; ++n2) {
                    this.A[n3][n2] = GF2Field.multElem(this.A[n3][n2], s2);
                }
            }
            this.computeZerosAbove();
            short[][] sArray2 = new short[this.A.length][this.A.length];
            for (n3 = 0; n3 < this.A.length; ++n3) {
                for (n2 = this.A.length; n2 < 2 * this.A.length; ++n2) {
                    sArray2[n3][n2 - this.A.length] = this.A[n3][n2];
                }
            }
            return sArray2;
        }
        catch (RuntimeException runtimeException) {
            return null;
        }
    }

    private void computeZerosUnder(boolean bl) throws RuntimeException {
        short s2 = 0;
        int n2 = bl ? 2 * this.A.length : this.A.length + 1;
        for (int i2 = 0; i2 < this.A.length - 1; ++i2) {
            for (int i3 = i2 + 1; i3 < this.A.length; ++i3) {
                short s3 = this.A[i3][i2];
                short s4 = GF2Field.invElem(this.A[i2][i2]);
                if (s4 == 0) {
                    throw new IllegalStateException("Matrix not invertible! We have to choose another one!");
                }
                for (int i4 = i2; i4 < n2; ++i4) {
                    s2 = GF2Field.multElem(this.A[i2][i4], s4);
                    s2 = GF2Field.multElem(s3, s2);
                    this.A[i3][i4] = GF2Field.addElem(this.A[i3][i4], s2);
                }
            }
        }
    }

    private void computeZerosAbove() throws RuntimeException {
        short s2 = 0;
        for (int i2 = this.A.length - 1; i2 > 0; --i2) {
            for (int i3 = i2 - 1; i3 >= 0; --i3) {
                short s3 = this.A[i3][i2];
                short s4 = GF2Field.invElem(this.A[i2][i2]);
                if (s4 == 0) {
                    throw new RuntimeException("The matrix is not invertible");
                }
                for (int i4 = i2; i4 < 2 * this.A.length; ++i4) {
                    s2 = GF2Field.multElem(this.A[i2][i4], s4);
                    s2 = GF2Field.multElem(s3, s2);
                    this.A[i3][i4] = GF2Field.addElem(this.A[i3][i4], s2);
                }
            }
        }
    }

    private void substitute() throws IllegalStateException {
        short s2 = GF2Field.invElem(this.A[this.A.length - 1][this.A.length - 1]);
        if (s2 == 0) {
            throw new IllegalStateException("The equation system is not solvable");
        }
        this.x[this.A.length - 1] = GF2Field.multElem(this.A[this.A.length - 1][this.A.length], s2);
        for (int i2 = this.A.length - 2; i2 >= 0; --i2) {
            short s3 = this.A[i2][this.A.length];
            for (int i3 = this.A.length - 1; i3 > i2; --i3) {
                s2 = GF2Field.multElem(this.A[i2][i3], this.x[i3]);
                s3 = GF2Field.addElem(s3, s2);
            }
            s2 = GF2Field.invElem(this.A[i2][i2]);
            if (s2 == 0) {
                throw new IllegalStateException("Not solvable equation system");
            }
            this.x[i2] = GF2Field.multElem(s3, s2);
        }
    }

    public short[][] multiplyMatrix(short[][] sArray, short[][] sArray2) throws RuntimeException {
        if (sArray[0].length != sArray2.length) {
            throw new RuntimeException("Multiplication is not possible!");
        }
        short s2 = 0;
        this.A = new short[sArray.length][sArray2[0].length];
        for (int i2 = 0; i2 < sArray.length; ++i2) {
            for (int i3 = 0; i3 < sArray2.length; ++i3) {
                for (int i4 = 0; i4 < sArray2[0].length; ++i4) {
                    s2 = GF2Field.multElem(sArray[i2][i3], sArray2[i3][i4]);
                    this.A[i2][i4] = GF2Field.addElem(this.A[i2][i4], s2);
                }
            }
        }
        return this.A;
    }

    public short[] multiplyMatrix(short[][] sArray, short[] sArray2) throws RuntimeException {
        if (sArray[0].length != sArray2.length) {
            throw new RuntimeException("Multiplication is not possible!");
        }
        short s2 = 0;
        short[] sArray3 = new short[sArray.length];
        for (int i2 = 0; i2 < sArray.length; ++i2) {
            for (int i3 = 0; i3 < sArray2.length; ++i3) {
                s2 = GF2Field.multElem(sArray[i2][i3], sArray2[i3]);
                sArray3[i2] = GF2Field.addElem(sArray3[i2], s2);
            }
        }
        return sArray3;
    }

    public short[] addVect(short[] sArray, short[] sArray2) {
        if (sArray.length != sArray2.length) {
            throw new RuntimeException("Multiplication is not possible!");
        }
        short[] sArray3 = new short[sArray.length];
        for (int i2 = 0; i2 < sArray3.length; ++i2) {
            sArray3[i2] = GF2Field.addElem(sArray[i2], sArray2[i2]);
        }
        return sArray3;
    }

    public short[][] multVects(short[] sArray, short[] sArray2) {
        if (sArray.length != sArray2.length) {
            throw new RuntimeException("Multiplication is not possible!");
        }
        short[][] sArray3 = new short[sArray.length][sArray2.length];
        for (int i2 = 0; i2 < sArray.length; ++i2) {
            for (int i3 = 0; i3 < sArray2.length; ++i3) {
                sArray3[i2][i3] = GF2Field.multElem(sArray[i2], sArray2[i3]);
            }
        }
        return sArray3;
    }

    public short[] multVect(short s2, short[] sArray) {
        short[] sArray2 = new short[sArray.length];
        for (int i2 = 0; i2 < sArray2.length; ++i2) {
            sArray2[i2] = GF2Field.multElem(s2, sArray[i2]);
        }
        return sArray2;
    }

    public short[][] multMatrix(short s2, short[][] sArray) {
        short[][] sArray2 = new short[sArray.length][sArray[0].length];
        for (int i2 = 0; i2 < sArray.length; ++i2) {
            for (int i3 = 0; i3 < sArray[0].length; ++i3) {
                sArray2[i2][i3] = GF2Field.multElem(s2, sArray[i2][i3]);
            }
        }
        return sArray2;
    }

    public short[][] addSquareMatrix(short[][] sArray, short[][] sArray2) {
        if (sArray.length != sArray2.length || sArray[0].length != sArray2[0].length) {
            throw new RuntimeException("Addition is not possible!");
        }
        short[][] sArray3 = new short[sArray.length][sArray.length];
        for (int i2 = 0; i2 < sArray.length; ++i2) {
            for (int i3 = 0; i3 < sArray2.length; ++i3) {
                sArray3[i2][i3] = GF2Field.addElem(sArray[i2][i3], sArray2[i2][i3]);
            }
        }
        return sArray3;
    }
}

