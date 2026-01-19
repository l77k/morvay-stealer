/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.linearalgebra;

import java.security.SecureRandom;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2Vector;
import org.bouncycastle.pqc.legacy.math.linearalgebra.IntUtils;
import org.bouncycastle.pqc.legacy.math.linearalgebra.LittleEndianConversions;
import org.bouncycastle.pqc.legacy.math.linearalgebra.Matrix;
import org.bouncycastle.pqc.legacy.math.linearalgebra.Permutation;
import org.bouncycastle.pqc.legacy.math.linearalgebra.Vector;
import org.bouncycastle.util.Arrays;

public class GF2Matrix
extends Matrix {
    private int[][] matrix;
    private int length;

    public GF2Matrix(byte[] byArray) {
        if (byArray.length < 9) {
            throw new ArithmeticException("given array is not an encoded matrix over GF(2)");
        }
        this.numRows = LittleEndianConversions.OS2IP(byArray, 0);
        this.numColumns = LittleEndianConversions.OS2IP(byArray, 4);
        int n2 = (this.numColumns + 7 >>> 3) * this.numRows;
        if (this.numRows <= 0 || n2 != byArray.length - 8) {
            throw new ArithmeticException("given array is not an encoded matrix over GF(2)");
        }
        this.length = this.numColumns + 31 >>> 5;
        this.matrix = new int[this.numRows][this.length];
        int n3 = this.numColumns >> 5;
        int n4 = this.numColumns & 0x1F;
        int n5 = 8;
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            int n6 = 0;
            while (n6 < n3) {
                this.matrix[i2][n6] = LittleEndianConversions.OS2IP(byArray, n5);
                ++n6;
                n5 += 4;
            }
            for (n6 = 0; n6 < n4; n6 += 8) {
                int[] nArray = this.matrix[i2];
                int n7 = n3;
                nArray[n7] = nArray[n7] ^ (byArray[n5++] & 0xFF) << n6;
            }
        }
    }

    public GF2Matrix(int n2, int[][] nArray) {
        if (nArray[0].length != n2 + 31 >> 5) {
            throw new ArithmeticException("Int array does not match given number of columns.");
        }
        this.numColumns = n2;
        this.numRows = nArray.length;
        this.length = nArray[0].length;
        int n3 = n2 & 0x1F;
        int n4 = n3 == 0 ? -1 : (1 << n3) - 1;
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            int[] nArray2 = nArray[i2];
            int n5 = this.length - 1;
            nArray2[n5] = nArray2[n5] & n4;
        }
        this.matrix = nArray;
    }

    public GF2Matrix(int n2, char c2) {
        this(n2, c2, new SecureRandom());
    }

    public GF2Matrix(int n2, char c2, SecureRandom secureRandom) {
        if (n2 <= 0) {
            throw new ArithmeticException("Size of matrix is non-positive.");
        }
        switch (c2) {
            case 'Z': {
                this.assignZeroMatrix(n2, n2);
                break;
            }
            case 'I': {
                this.assignUnitMatrix(n2);
                break;
            }
            case 'L': {
                this.assignRandomLowerTriangularMatrix(n2, secureRandom);
                break;
            }
            case 'U': {
                this.assignRandomUpperTriangularMatrix(n2, secureRandom);
                break;
            }
            case 'R': {
                this.assignRandomRegularMatrix(n2, secureRandom);
                break;
            }
            default: {
                throw new ArithmeticException("Unknown matrix type.");
            }
        }
    }

    public GF2Matrix(GF2Matrix gF2Matrix) {
        this.numColumns = gF2Matrix.getNumColumns();
        this.numRows = gF2Matrix.getNumRows();
        this.length = gF2Matrix.length;
        this.matrix = new int[gF2Matrix.matrix.length][];
        for (int i2 = 0; i2 < this.matrix.length; ++i2) {
            this.matrix[i2] = IntUtils.clone(gF2Matrix.matrix[i2]);
        }
    }

    private GF2Matrix(int n2, int n3) {
        if (n3 <= 0 || n2 <= 0) {
            throw new ArithmeticException("size of matrix is non-positive");
        }
        this.assignZeroMatrix(n2, n3);
    }

    private void assignZeroMatrix(int n2, int n3) {
        this.numRows = n2;
        this.numColumns = n3;
        this.length = n3 + 31 >>> 5;
        this.matrix = new int[this.numRows][this.length];
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            for (int i3 = 0; i3 < this.length; ++i3) {
                this.matrix[i2][i3] = 0;
            }
        }
    }

    private void assignUnitMatrix(int n2) {
        int n3;
        int n4;
        this.numRows = n2;
        this.numColumns = n2;
        this.length = n2 + 31 >>> 5;
        this.matrix = new int[this.numRows][this.length];
        for (n4 = 0; n4 < this.numRows; ++n4) {
            for (n3 = 0; n3 < this.length; ++n3) {
                this.matrix[n4][n3] = 0;
            }
        }
        for (n4 = 0; n4 < this.numRows; ++n4) {
            n3 = n4 & 0x1F;
            this.matrix[n4][n4 >>> 5] = 1 << n3;
        }
    }

    private void assignRandomLowerTriangularMatrix(int n2, SecureRandom secureRandom) {
        this.numRows = n2;
        this.numColumns = n2;
        this.length = n2 + 31 >>> 5;
        this.matrix = new int[this.numRows][this.length];
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            int n3;
            int n4 = i2 >>> 5;
            int n5 = i2 & 0x1F;
            int n6 = 31 - n5;
            n5 = 1 << n5;
            for (n3 = 0; n3 < n4; ++n3) {
                this.matrix[i2][n3] = secureRandom.nextInt();
            }
            this.matrix[i2][n4] = secureRandom.nextInt() >>> n6 | n5;
            for (n3 = n4 + 1; n3 < this.length; ++n3) {
                this.matrix[i2][n3] = 0;
            }
        }
    }

    private void assignRandomUpperTriangularMatrix(int n2, SecureRandom secureRandom) {
        this.numRows = n2;
        this.numColumns = n2;
        this.length = n2 + 31 >>> 5;
        this.matrix = new int[this.numRows][this.length];
        int n3 = n2 & 0x1F;
        int n4 = n3 == 0 ? -1 : (1 << n3) - 1;
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            int n5;
            int n6;
            int n7 = i2 >>> 5;
            int n8 = n6 = i2 & 0x1F;
            n6 = 1 << n6;
            for (n5 = 0; n5 < n7; ++n5) {
                this.matrix[i2][n5] = 0;
            }
            this.matrix[i2][n7] = secureRandom.nextInt() << n8 | n6;
            for (n5 = n7 + 1; n5 < this.length; ++n5) {
                this.matrix[i2][n5] = secureRandom.nextInt();
            }
            int[] nArray = this.matrix[i2];
            int n9 = this.length - 1;
            nArray[n9] = nArray[n9] & n4;
        }
    }

    private void assignRandomRegularMatrix(int n2, SecureRandom secureRandom) {
        this.numRows = n2;
        this.numColumns = n2;
        this.length = n2 + 31 >>> 5;
        this.matrix = new int[this.numRows][this.length];
        GF2Matrix gF2Matrix = new GF2Matrix(n2, 'L', secureRandom);
        GF2Matrix gF2Matrix2 = new GF2Matrix(n2, 'U', secureRandom);
        GF2Matrix gF2Matrix3 = (GF2Matrix)gF2Matrix.rightMultiply(gF2Matrix2);
        Permutation permutation = new Permutation(n2, secureRandom);
        int[] nArray = permutation.getVector();
        for (int i2 = 0; i2 < n2; ++i2) {
            System.arraycopy(gF2Matrix3.matrix[i2], 0, this.matrix[nArray[i2]], 0, this.length);
        }
    }

    public static GF2Matrix[] createRandomRegularMatrixAndItsInverse(int n2, SecureRandom secureRandom) {
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        GF2Matrix[] gF2MatrixArray = new GF2Matrix[2];
        int n9 = n2 + 31 >> 5;
        GF2Matrix gF2Matrix = new GF2Matrix(n2, 'L', secureRandom);
        GF2Matrix gF2Matrix2 = new GF2Matrix(n2, 'U', secureRandom);
        GF2Matrix gF2Matrix3 = (GF2Matrix)gF2Matrix.rightMultiply(gF2Matrix2);
        Permutation permutation = new Permutation(n2, secureRandom);
        int[] nArray = permutation.getVector();
        int[][] nArray2 = new int[n2][n9];
        for (int i2 = 0; i2 < n2; ++i2) {
            System.arraycopy(gF2Matrix3.matrix[nArray[i2]], 0, nArray2[i2], 0, n9);
        }
        gF2MatrixArray[0] = new GF2Matrix(n2, nArray2);
        GF2Matrix gF2Matrix4 = new GF2Matrix(n2, 'I');
        for (int i3 = 0; i3 < n2; ++i3) {
            n8 = i3 & 0x1F;
            n7 = i3 >>> 5;
            n6 = 1 << n8;
            for (n5 = i3 + 1; n5 < n2; ++n5) {
                n4 = gF2Matrix.matrix[n5][n7] & n6;
                if (n4 == 0) continue;
                for (n3 = 0; n3 <= n7; ++n3) {
                    int[] nArray3 = gF2Matrix4.matrix[n5];
                    int n10 = n3;
                    nArray3[n10] = nArray3[n10] ^ gF2Matrix4.matrix[i3][n3];
                }
            }
        }
        GF2Matrix gF2Matrix5 = new GF2Matrix(n2, 'I');
        for (n8 = n2 - 1; n8 >= 0; --n8) {
            n7 = n8 & 0x1F;
            n6 = n8 >>> 5;
            n5 = 1 << n7;
            for (n4 = n8 - 1; n4 >= 0; --n4) {
                n3 = gF2Matrix2.matrix[n4][n6] & n5;
                if (n3 == 0) continue;
                for (int i4 = n6; i4 < n9; ++i4) {
                    int[] nArray4 = gF2Matrix5.matrix[n4];
                    int n11 = i4;
                    nArray4[n11] = nArray4[n11] ^ gF2Matrix5.matrix[n8][i4];
                }
            }
        }
        gF2MatrixArray[1] = (GF2Matrix)gF2Matrix5.rightMultiply(gF2Matrix4.rightMultiply(permutation));
        return gF2MatrixArray;
    }

    public int[][] getIntArray() {
        return this.matrix;
    }

    public int getLength() {
        return this.length;
    }

    public int[] getRow(int n2) {
        return this.matrix[n2];
    }

    @Override
    public byte[] getEncoded() {
        int n2 = this.numColumns + 7 >>> 3;
        n2 *= this.numRows;
        byte[] byArray = new byte[n2 += 8];
        LittleEndianConversions.I2OSP(this.numRows, byArray, 0);
        LittleEndianConversions.I2OSP(this.numColumns, byArray, 4);
        int n3 = this.numColumns >>> 5;
        int n4 = this.numColumns & 0x1F;
        int n5 = 8;
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            int n6 = 0;
            while (n6 < n3) {
                LittleEndianConversions.I2OSP(this.matrix[i2][n6], byArray, n5);
                ++n6;
                n5 += 4;
            }
            for (n6 = 0; n6 < n4; n6 += 8) {
                byArray[n5++] = (byte)(this.matrix[i2][n3] >>> n6 & 0xFF);
            }
        }
        return byArray;
    }

    public double getHammingWeight() {
        double d2 = 0.0;
        double d3 = 0.0;
        int n2 = this.numColumns & 0x1F;
        int n3 = n2 == 0 ? this.length : this.length - 1;
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            int n4;
            int n5;
            int n6;
            for (n6 = 0; n6 < n3; ++n6) {
                n5 = this.matrix[i2][n6];
                for (n4 = 0; n4 < 32; ++n4) {
                    int n7 = n5 >>> n4 & 1;
                    d2 += (double)n7;
                    d3 += 1.0;
                }
            }
            n6 = this.matrix[i2][this.length - 1];
            for (n5 = 0; n5 < n2; ++n5) {
                n4 = n6 >>> n5 & 1;
                d2 += (double)n4;
                d3 += 1.0;
            }
        }
        return d2 / d3;
    }

    @Override
    public boolean isZero() {
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            for (int i3 = 0; i3 < this.length; ++i3) {
                if (this.matrix[i2][i3] == 0) continue;
                return false;
            }
        }
        return true;
    }

    public GF2Matrix getLeftSubMatrix() {
        if (this.numColumns <= this.numRows) {
            throw new ArithmeticException("empty submatrix");
        }
        int n2 = this.numRows + 31 >> 5;
        int[][] nArray = new int[this.numRows][n2];
        int n3 = (1 << (this.numRows & 0x1F)) - 1;
        if (n3 == 0) {
            n3 = -1;
        }
        for (int i2 = this.numRows - 1; i2 >= 0; --i2) {
            System.arraycopy(this.matrix[i2], 0, nArray[i2], 0, n2);
            int[] nArray2 = nArray[i2];
            int n4 = n2 - 1;
            nArray2[n4] = nArray2[n4] & n3;
        }
        return new GF2Matrix(this.numRows, nArray);
    }

    public GF2Matrix extendLeftCompactForm() {
        int n2 = this.numColumns + this.numRows;
        GF2Matrix gF2Matrix = new GF2Matrix(this.numRows, n2);
        int n3 = this.numRows - 1 + this.numColumns;
        int n4 = this.numRows - 1;
        while (n4 >= 0) {
            System.arraycopy(this.matrix[n4], 0, gF2Matrix.matrix[n4], 0, this.length);
            int[] nArray = gF2Matrix.matrix[n4];
            int n5 = n3 >> 5;
            nArray[n5] = nArray[n5] | 1 << (n3 & 0x1F);
            --n4;
            --n3;
        }
        return gF2Matrix;
    }

    public GF2Matrix getRightSubMatrix() {
        if (this.numColumns <= this.numRows) {
            throw new ArithmeticException("empty submatrix");
        }
        int n2 = this.numRows >> 5;
        int n3 = this.numRows & 0x1F;
        GF2Matrix gF2Matrix = new GF2Matrix(this.numRows, this.numColumns - this.numRows);
        for (int i2 = this.numRows - 1; i2 >= 0; --i2) {
            if (n3 != 0) {
                int n4 = n2;
                for (int i3 = 0; i3 < gF2Matrix.length - 1; ++i3) {
                    gF2Matrix.matrix[i2][i3] = this.matrix[i2][n4++] >>> n3 | this.matrix[i2][n4] << 32 - n3;
                }
                gF2Matrix.matrix[i2][gF2Matrix.length - 1] = this.matrix[i2][n4++] >>> n3;
                if (n4 >= this.length) continue;
                int[] nArray = gF2Matrix.matrix[i2];
                int n5 = gF2Matrix.length - 1;
                nArray[n5] = nArray[n5] | this.matrix[i2][n4] << 32 - n3;
                continue;
            }
            System.arraycopy(this.matrix[i2], n2, gF2Matrix.matrix[i2], 0, gF2Matrix.length);
        }
        return gF2Matrix;
    }

    public GF2Matrix extendRightCompactForm() {
        GF2Matrix gF2Matrix = new GF2Matrix(this.numRows, this.numRows + this.numColumns);
        int n2 = this.numRows >> 5;
        int n3 = this.numRows & 0x1F;
        for (int i2 = this.numRows - 1; i2 >= 0; --i2) {
            int[] nArray = gF2Matrix.matrix[i2];
            int n4 = i2 >> 5;
            nArray[n4] = nArray[n4] | 1 << (i2 & 0x1F);
            if (n3 != 0) {
                int n5;
                int n6 = n2;
                for (n5 = 0; n5 < this.length - 1; ++n5) {
                    int n7 = this.matrix[i2][n5];
                    int[] nArray2 = gF2Matrix.matrix[i2];
                    int n8 = n6++;
                    nArray2[n8] = nArray2[n8] | n7 << n3;
                    int[] nArray3 = gF2Matrix.matrix[i2];
                    int n9 = n6;
                    nArray3[n9] = nArray3[n9] | n7 >>> 32 - n3;
                }
                n5 = this.matrix[i2][this.length - 1];
                int[] nArray4 = gF2Matrix.matrix[i2];
                int n10 = n6++;
                nArray4[n10] = nArray4[n10] | n5 << n3;
                if (n6 >= gF2Matrix.length) continue;
                int[] nArray5 = gF2Matrix.matrix[i2];
                int n11 = n6;
                nArray5[n11] = nArray5[n11] | n5 >>> 32 - n3;
                continue;
            }
            System.arraycopy(this.matrix[i2], 0, gF2Matrix.matrix[i2], n2, this.length);
        }
        return gF2Matrix;
    }

    public Matrix computeTranspose() {
        int[][] nArray = new int[this.numColumns][this.numRows + 31 >>> 5];
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            for (int i3 = 0; i3 < this.numColumns; ++i3) {
                int n2 = i3 >>> 5;
                int n3 = i3 & 0x1F;
                int n4 = this.matrix[i2][n2] >>> n3 & 1;
                int n5 = i2 >>> 5;
                int n6 = i2 & 0x1F;
                if (n4 != 1) continue;
                int[] nArray2 = nArray[i3];
                int n7 = n5;
                nArray2[n7] = nArray2[n7] | 1 << n6;
            }
        }
        return new GF2Matrix(this.numRows, nArray);
    }

    @Override
    public Matrix computeInverse() {
        int n2;
        int n3;
        int n4;
        if (this.numRows != this.numColumns) {
            throw new ArithmeticException("Matrix is not invertible.");
        }
        int[][] nArray = new int[this.numRows][this.length];
        for (int i2 = this.numRows - 1; i2 >= 0; --i2) {
            nArray[i2] = IntUtils.clone(this.matrix[i2]);
        }
        int[][] nArray2 = new int[this.numRows][this.length];
        for (n4 = this.numRows - 1; n4 >= 0; --n4) {
            n3 = n4 >> 5;
            n2 = n4 & 0x1F;
            nArray2[n4][n3] = 1 << n2;
        }
        for (n4 = 0; n4 < this.numRows; ++n4) {
            int n5;
            n3 = n4 >> 5;
            n2 = 1 << (n4 & 0x1F);
            if ((nArray[n4][n3] & n2) == 0) {
                n5 = 0;
                for (int i3 = n4 + 1; i3 < this.numRows; ++i3) {
                    if ((nArray[i3][n3] & n2) == 0) continue;
                    n5 = 1;
                    GF2Matrix.swapRows(nArray, n4, i3);
                    GF2Matrix.swapRows(nArray2, n4, i3);
                    i3 = this.numRows;
                }
                if (n5 == 0) {
                    throw new ArithmeticException("Matrix is not invertible.");
                }
            }
            for (n5 = this.numRows - 1; n5 >= 0; --n5) {
                if (n5 == n4 || (nArray[n5][n3] & n2) == 0) continue;
                GF2Matrix.addToRow(nArray[n4], nArray[n5], n3);
                GF2Matrix.addToRow(nArray2[n4], nArray2[n5], 0);
            }
        }
        return new GF2Matrix(this.numColumns, nArray2);
    }

    public Matrix leftMultiply(Permutation permutation) {
        int[] nArray = permutation.getVector();
        if (nArray.length != this.numRows) {
            throw new ArithmeticException("length mismatch");
        }
        int[][] nArrayArray = new int[this.numRows][];
        for (int i2 = this.numRows - 1; i2 >= 0; --i2) {
            nArrayArray[i2] = IntUtils.clone(this.matrix[nArray[i2]]);
        }
        return new GF2Matrix(this.numRows, nArrayArray);
    }

    @Override
    public Vector leftMultiply(Vector vector) {
        int n2;
        int n3;
        int n4;
        if (!(vector instanceof GF2Vector)) {
            throw new ArithmeticException("vector is not defined over GF(2)");
        }
        if (vector.length != this.numRows) {
            throw new ArithmeticException("length mismatch");
        }
        int[] nArray = ((GF2Vector)vector).getVecArray();
        int[] nArray2 = new int[this.length];
        int n5 = this.numRows >> 5;
        int n6 = 1 << (this.numRows & 0x1F);
        int n7 = 0;
        for (n4 = 0; n4 < n5; ++n4) {
            n3 = 1;
            do {
                if ((n2 = nArray[n4] & n3) != 0) {
                    for (int i2 = 0; i2 < this.length; ++i2) {
                        int n8 = i2;
                        nArray2[n8] = nArray2[n8] ^ this.matrix[n7][i2];
                    }
                }
                ++n7;
            } while ((n3 <<= 1) != 0);
        }
        for (n4 = 1; n4 != n6; n4 <<= 1) {
            n3 = nArray[n5] & n4;
            if (n3 != 0) {
                for (n2 = 0; n2 < this.length; ++n2) {
                    int n9 = n2;
                    nArray2[n9] = nArray2[n9] ^ this.matrix[n7][n2];
                }
            }
            ++n7;
        }
        return new GF2Vector(nArray2, this.numColumns);
    }

    public Vector leftMultiplyLeftCompactForm(Vector vector) {
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        if (!(vector instanceof GF2Vector)) {
            throw new ArithmeticException("vector is not defined over GF(2)");
        }
        if (vector.length != this.numRows) {
            throw new ArithmeticException("length mismatch");
        }
        int[] nArray = ((GF2Vector)vector).getVecArray();
        int[] nArray2 = new int[this.numRows + this.numColumns + 31 >>> 5];
        int n7 = this.numRows >>> 5;
        int n8 = 0;
        for (n6 = 0; n6 < n7; ++n6) {
            n5 = 1;
            do {
                if ((n4 = nArray[n6] & n5) != 0) {
                    for (n3 = 0; n3 < this.length; ++n3) {
                        int n9 = n3;
                        nArray2[n9] = nArray2[n9] ^ this.matrix[n8][n3];
                    }
                    n3 = this.numColumns + n8 >>> 5;
                    n2 = this.numColumns + n8 & 0x1F;
                    int n10 = n3;
                    nArray2[n10] = nArray2[n10] | 1 << n2;
                }
                ++n8;
            } while ((n5 <<= 1) != 0);
        }
        n6 = 1 << (this.numRows & 0x1F);
        for (n5 = 1; n5 != n6; n5 <<= 1) {
            n4 = nArray[n7] & n5;
            if (n4 != 0) {
                for (n3 = 0; n3 < this.length; ++n3) {
                    int n11 = n3;
                    nArray2[n11] = nArray2[n11] ^ this.matrix[n8][n3];
                }
                n3 = this.numColumns + n8 >>> 5;
                n2 = this.numColumns + n8 & 0x1F;
                int n12 = n3;
                nArray2[n12] = nArray2[n12] | 1 << n2;
            }
            ++n8;
        }
        return new GF2Vector(nArray2, this.numRows + this.numColumns);
    }

    @Override
    public Matrix rightMultiply(Matrix matrix) {
        if (!(matrix instanceof GF2Matrix)) {
            throw new ArithmeticException("matrix is not defined over GF(2)");
        }
        if (matrix.numRows != this.numColumns) {
            throw new ArithmeticException("length mismatch");
        }
        GF2Matrix gF2Matrix = (GF2Matrix)matrix;
        GF2Matrix gF2Matrix2 = new GF2Matrix(this.numRows, matrix.numColumns);
        int n2 = this.numColumns & 0x1F;
        int n3 = n2 == 0 ? this.length : this.length - 1;
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            int n4;
            int n5;
            int n6;
            int n7;
            int n8 = 0;
            for (n7 = 0; n7 < n3; ++n7) {
                n6 = this.matrix[i2][n7];
                for (n5 = 0; n5 < 32; ++n5) {
                    n4 = n6 & 1 << n5;
                    if (n4 != 0) {
                        for (int i3 = 0; i3 < gF2Matrix.length; ++i3) {
                            int[] nArray = gF2Matrix2.matrix[i2];
                            int n9 = i3;
                            nArray[n9] = nArray[n9] ^ gF2Matrix.matrix[n8][i3];
                        }
                    }
                    ++n8;
                }
            }
            n7 = this.matrix[i2][this.length - 1];
            for (n6 = 0; n6 < n2; ++n6) {
                n5 = n7 & 1 << n6;
                if (n5 != 0) {
                    for (n4 = 0; n4 < gF2Matrix.length; ++n4) {
                        int[] nArray = gF2Matrix2.matrix[i2];
                        int n10 = n4;
                        nArray[n10] = nArray[n10] ^ gF2Matrix.matrix[n8][n4];
                    }
                }
                ++n8;
            }
        }
        return gF2Matrix2;
    }

    @Override
    public Matrix rightMultiply(Permutation permutation) {
        int[] nArray = permutation.getVector();
        if (nArray.length != this.numColumns) {
            throw new ArithmeticException("length mismatch");
        }
        GF2Matrix gF2Matrix = new GF2Matrix(this.numRows, this.numColumns);
        for (int i2 = this.numColumns - 1; i2 >= 0; --i2) {
            int n2 = i2 >>> 5;
            int n3 = i2 & 0x1F;
            int n4 = nArray[i2] >>> 5;
            int n5 = nArray[i2] & 0x1F;
            for (int i3 = this.numRows - 1; i3 >= 0; --i3) {
                int[] nArray2 = gF2Matrix.matrix[i3];
                int n6 = n2;
                nArray2[n6] = nArray2[n6] | (this.matrix[i3][n4] >>> n5 & 1) << n3;
            }
        }
        return gF2Matrix;
    }

    @Override
    public Vector rightMultiply(Vector vector) {
        if (!(vector instanceof GF2Vector)) {
            throw new ArithmeticException("vector is not defined over GF(2)");
        }
        if (vector.length != this.numColumns) {
            throw new ArithmeticException("length mismatch");
        }
        int[] nArray = ((GF2Vector)vector).getVecArray();
        int[] nArray2 = new int[this.numRows + 31 >>> 5];
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            int n2;
            int n3 = 0;
            for (n2 = 0; n2 < this.length; ++n2) {
                n3 ^= this.matrix[i2][n2] & nArray[n2];
            }
            n2 = 0;
            for (int i3 = 0; i3 < 32; ++i3) {
                n2 ^= n3 >>> i3 & 1;
            }
            if (n2 != 1) continue;
            int n4 = i2 >>> 5;
            nArray2[n4] = nArray2[n4] | 1 << (i2 & 0x1F);
        }
        return new GF2Vector(nArray2, this.numRows);
    }

    public Vector rightMultiplyRightCompactForm(Vector vector) {
        if (!(vector instanceof GF2Vector)) {
            throw new ArithmeticException("vector is not defined over GF(2)");
        }
        if (vector.length != this.numColumns + this.numRows) {
            throw new ArithmeticException("length mismatch");
        }
        int[] nArray = ((GF2Vector)vector).getVecArray();
        int[] nArray2 = new int[this.numRows + 31 >>> 5];
        int n2 = this.numRows >> 5;
        int n3 = this.numRows & 0x1F;
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            int n4;
            int n5;
            int n6 = nArray[i2 >> 5] >>> (i2 & 0x1F) & 1;
            int n7 = n2;
            if (n3 != 0) {
                n5 = 0;
                for (n4 = 0; n4 < this.length - 1; ++n4) {
                    n5 = nArray[n7++] >>> n3 | nArray[n7] << 32 - n3;
                    n6 ^= this.matrix[i2][n4] & n5;
                }
                n5 = nArray[n7++] >>> n3;
                if (n7 < nArray.length) {
                    n5 |= nArray[n7] << 32 - n3;
                }
                n6 ^= this.matrix[i2][this.length - 1] & n5;
            } else {
                for (n5 = 0; n5 < this.length; ++n5) {
                    n6 ^= this.matrix[i2][n5] & nArray[n7++];
                }
            }
            n5 = 0;
            for (n4 = 0; n4 < 32; ++n4) {
                n5 ^= n6 & 1;
                n6 >>>= 1;
            }
            if (n5 != 1) continue;
            int n8 = i2 >> 5;
            nArray2[n8] = nArray2[n8] | 1 << (i2 & 0x1F);
        }
        return new GF2Vector(nArray2, this.numRows);
    }

    public boolean equals(Object object) {
        if (!(object instanceof GF2Matrix)) {
            return false;
        }
        GF2Matrix gF2Matrix = (GF2Matrix)object;
        if (this.numRows != gF2Matrix.numRows || this.numColumns != gF2Matrix.numColumns || this.length != gF2Matrix.length) {
            return false;
        }
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            if (IntUtils.equals(this.matrix[i2], gF2Matrix.matrix[i2])) continue;
            return false;
        }
        return true;
    }

    public int hashCode() {
        int n2 = (this.numRows * 31 + this.numColumns) * 31 + this.length;
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            n2 = n2 * 31 + Arrays.hashCode(this.matrix[i2]);
        }
        return n2;
    }

    @Override
    public String toString() {
        int n2 = this.numColumns & 0x1F;
        int n3 = n2 == 0 ? this.length : this.length - 1;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < this.numRows; ++i2) {
            int n4;
            int n5;
            int n6;
            stringBuffer.append(i2 + ": ");
            for (n6 = 0; n6 < n3; ++n6) {
                n5 = this.matrix[i2][n6];
                for (n4 = 0; n4 < 32; ++n4) {
                    int n7 = n5 >>> n4 & 1;
                    if (n7 == 0) {
                        stringBuffer.append('0');
                        continue;
                    }
                    stringBuffer.append('1');
                }
                stringBuffer.append(' ');
            }
            n6 = this.matrix[i2][this.length - 1];
            for (n5 = 0; n5 < n2; ++n5) {
                n4 = n6 >>> n5 & 1;
                if (n4 == 0) {
                    stringBuffer.append('0');
                    continue;
                }
                stringBuffer.append('1');
            }
            stringBuffer.append('\n');
        }
        return stringBuffer.toString();
    }

    private static void swapRows(int[][] nArray, int n2, int n3) {
        int[] nArray2 = nArray[n2];
        nArray[n2] = nArray[n3];
        nArray[n3] = nArray2;
    }

    private static void addToRow(int[] nArray, int[] nArray2, int n2) {
        for (int i2 = nArray2.length - 1; i2 >= n2; --i2) {
            nArray2[i2] = nArray[i2] ^ nArray2[i2];
        }
    }
}

