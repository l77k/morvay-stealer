/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.linearalgebra;

import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2mField;
import org.bouncycastle.pqc.legacy.math.linearalgebra.IntUtils;
import org.bouncycastle.pqc.legacy.math.linearalgebra.Matrix;
import org.bouncycastle.pqc.legacy.math.linearalgebra.Permutation;
import org.bouncycastle.pqc.legacy.math.linearalgebra.Vector;

public class GF2mMatrix
extends Matrix {
    protected GF2mField field;
    protected int[][] matrix;

    public GF2mMatrix(GF2mField gF2mField, byte[] byArray) {
        int n2;
        this.field = gF2mField;
        int n3 = 1;
        for (n2 = 8; gF2mField.getDegree() > n2; n2 += 8) {
            ++n3;
        }
        if (byArray.length < 5) {
            throw new IllegalArgumentException(" Error: given array is not encoded matrix over GF(2^m)");
        }
        this.numRows = (byArray[3] & 0xFF) << 24 ^ (byArray[2] & 0xFF) << 16 ^ (byArray[1] & 0xFF) << 8 ^ byArray[0] & 0xFF;
        int n4 = n3 * this.numRows;
        if (this.numRows <= 0 || (byArray.length - 4) % n4 != 0) {
            throw new IllegalArgumentException(" Error: given array is not encoded matrix over GF(2^m)");
        }
        this.numColumns = (byArray.length - 4) / n4;
        this.matrix = new int[this.numRows][this.numColumns];
        n3 = 4;
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            for (int i3 = 0; i3 < this.numColumns; ++i3) {
                for (int i4 = 0; i4 < n2; i4 += 8) {
                    int[] nArray = this.matrix[i2];
                    int n5 = i3;
                    nArray[n5] = nArray[n5] ^ (byArray[n3++] & 0xFF) << i4;
                }
                if (this.field.isElementOfThisField(this.matrix[i2][i3])) continue;
                throw new IllegalArgumentException(" Error: given array is not encoded matrix over GF(2^m)");
            }
        }
    }

    public GF2mMatrix(GF2mMatrix gF2mMatrix) {
        this.numRows = gF2mMatrix.numRows;
        this.numColumns = gF2mMatrix.numColumns;
        this.field = gF2mMatrix.field;
        this.matrix = new int[this.numRows][];
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            this.matrix[i2] = IntUtils.clone(gF2mMatrix.matrix[i2]);
        }
    }

    protected GF2mMatrix(GF2mField gF2mField, int[][] nArray) {
        this.field = gF2mField;
        this.matrix = nArray;
        this.numRows = nArray.length;
        this.numColumns = nArray[0].length;
    }

    @Override
    public byte[] getEncoded() {
        int n2;
        int n3 = 1;
        for (n2 = 8; this.field.getDegree() > n2; n2 += 8) {
            ++n3;
        }
        byte[] byArray = new byte[this.numRows * this.numColumns * n3 + 4];
        byArray[0] = (byte)(this.numRows & 0xFF);
        byArray[1] = (byte)(this.numRows >>> 8 & 0xFF);
        byArray[2] = (byte)(this.numRows >>> 16 & 0xFF);
        byArray[3] = (byte)(this.numRows >>> 24 & 0xFF);
        n3 = 4;
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            for (int i3 = 0; i3 < this.numColumns; ++i3) {
                for (int i4 = 0; i4 < n2; i4 += 8) {
                    byArray[n3++] = (byte)(this.matrix[i2][i3] >>> i4);
                }
            }
        }
        return byArray;
    }

    @Override
    public boolean isZero() {
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            for (int i3 = 0; i3 < this.numColumns; ++i3) {
                if (this.matrix[i2][i3] == 0) continue;
                return false;
            }
        }
        return true;
    }

    @Override
    public Matrix computeInverse() {
        int n2;
        if (this.numRows != this.numColumns) {
            throw new ArithmeticException("Matrix is not invertible.");
        }
        int[][] nArray = new int[this.numRows][this.numRows];
        for (int i2 = this.numRows - 1; i2 >= 0; --i2) {
            nArray[i2] = IntUtils.clone(this.matrix[i2]);
        }
        int[][] nArray2 = new int[this.numRows][this.numRows];
        for (n2 = this.numRows - 1; n2 >= 0; --n2) {
            nArray2[n2][n2] = 1;
        }
        for (n2 = 0; n2 < this.numRows; ++n2) {
            int n3;
            int n4;
            if (nArray[n2][n2] == 0) {
                n4 = 0;
                for (n3 = n2 + 1; n3 < this.numRows; ++n3) {
                    if (nArray[n3][n2] == 0) continue;
                    n4 = 1;
                    GF2mMatrix.swapColumns(nArray, n2, n3);
                    GF2mMatrix.swapColumns(nArray2, n2, n3);
                    n3 = this.numRows;
                }
                if (n4 == 0) {
                    throw new ArithmeticException("Matrix is not invertible.");
                }
            }
            n4 = nArray[n2][n2];
            n3 = this.field.inverse(n4);
            this.multRowWithElementThis(nArray[n2], n3);
            this.multRowWithElementThis(nArray2[n2], n3);
            for (int i3 = 0; i3 < this.numRows; ++i3) {
                if (i3 == n2 || (n4 = nArray[i3][n2]) == 0) continue;
                int[] nArray3 = this.multRowWithElement(nArray[n2], n4);
                int[] nArray4 = this.multRowWithElement(nArray2[n2], n4);
                this.addToRow(nArray3, nArray[i3]);
                this.addToRow(nArray4, nArray2[i3]);
            }
        }
        return new GF2mMatrix(this.field, nArray2);
    }

    private static void swapColumns(int[][] nArray, int n2, int n3) {
        int[] nArray2 = nArray[n2];
        nArray[n2] = nArray[n3];
        nArray[n3] = nArray2;
    }

    private void multRowWithElementThis(int[] nArray, int n2) {
        for (int i2 = nArray.length - 1; i2 >= 0; --i2) {
            nArray[i2] = this.field.mult(nArray[i2], n2);
        }
    }

    private int[] multRowWithElement(int[] nArray, int n2) {
        int[] nArray2 = new int[nArray.length];
        for (int i2 = nArray.length - 1; i2 >= 0; --i2) {
            nArray2[i2] = this.field.mult(nArray[i2], n2);
        }
        return nArray2;
    }

    private void addToRow(int[] nArray, int[] nArray2) {
        for (int i2 = nArray2.length - 1; i2 >= 0; --i2) {
            nArray2[i2] = this.field.add(nArray[i2], nArray2[i2]);
        }
    }

    @Override
    public Matrix rightMultiply(Matrix matrix) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public Matrix rightMultiply(Permutation permutation) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public Vector leftMultiply(Vector vector) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public Vector rightMultiply(Vector vector) {
        throw new RuntimeException("Not implemented.");
    }

    public boolean equals(Object object) {
        if (object == null || !(object instanceof GF2mMatrix)) {
            return false;
        }
        GF2mMatrix gF2mMatrix = (GF2mMatrix)object;
        if (!this.field.equals(gF2mMatrix.field) || gF2mMatrix.numRows != this.numColumns || gF2mMatrix.numColumns != this.numColumns) {
            return false;
        }
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            for (int i3 = 0; i3 < this.numColumns; ++i3) {
                if (this.matrix[i2][i3] == gF2mMatrix.matrix[i2][i3]) continue;
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int n2 = (this.field.hashCode() * 31 + this.numRows) * 31 + this.numColumns;
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            for (int i3 = 0; i3 < this.numColumns; ++i3) {
                n2 = n2 * 31 + this.matrix[i2][i3];
            }
        }
        return n2;
    }

    @Override
    public String toString() {
        String string = this.numRows + " x " + this.numColumns + " Matrix over " + this.field.toString() + ": \n";
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            for (int i3 = 0; i3 < this.numColumns; ++i3) {
                string = string + this.field.elementToStr(this.matrix[i2][i3]) + " : ";
            }
            string = string + "\n";
        }
        return string;
    }
}

