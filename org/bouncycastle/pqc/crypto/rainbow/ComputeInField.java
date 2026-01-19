/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.rainbow;

import org.bouncycastle.pqc.crypto.rainbow.GF2Field;

class ComputeInField {
    public short[] solveEquation(short[][] sArray, short[] sArray2) {
        if (sArray.length != sArray2.length) {
            return null;
        }
        try {
            int n2;
            short[][] sArray3 = new short[sArray.length][sArray.length + 1];
            short[] sArray4 = new short[sArray.length];
            for (n2 = 0; n2 < sArray.length; ++n2) {
                System.arraycopy(sArray[n2], 0, sArray3[n2], 0, sArray[0].length);
                sArray3[n2][sArray2.length] = GF2Field.addElem(sArray2[n2], sArray3[n2][sArray2.length]);
            }
            this.gaussElim(sArray3);
            for (n2 = 0; n2 < sArray3.length; ++n2) {
                sArray4[n2] = sArray3[n2][sArray2.length];
            }
            return sArray4;
        }
        catch (RuntimeException runtimeException) {
            return null;
        }
    }

    public short[][] inverse(short[][] sArray) {
        if (sArray.length != sArray[0].length) {
            throw new RuntimeException("The matrix is not invertible. Please choose another one!");
        }
        try {
            int n2;
            int n3;
            short[][] sArray2 = new short[sArray.length][2 * sArray.length];
            for (n3 = 0; n3 < sArray.length; ++n3) {
                System.arraycopy(sArray[n3], 0, sArray2[n3], 0, sArray.length);
                for (n2 = sArray.length; n2 < 2 * sArray.length; ++n2) {
                    sArray2[n3][n2] = 0;
                }
                sArray2[n3][n3 + sArray2.length] = 1;
            }
            this.gaussElim(sArray2);
            short[][] sArray3 = new short[sArray2.length][sArray2.length];
            for (n3 = 0; n3 < sArray2.length; ++n3) {
                for (n2 = sArray2.length; n2 < 2 * sArray2.length; ++n2) {
                    sArray3[n3][n2 - sArray2.length] = sArray2[n3][n2];
                }
            }
            return sArray3;
        }
        catch (RuntimeException runtimeException) {
            return null;
        }
    }

    private void gaussElim(short[][] sArray) {
        for (int i2 = 0; i2 < sArray.length; ++i2) {
            int n2;
            int n3;
            for (n3 = i2 + 1; n3 < sArray.length; ++n3) {
                if (sArray[i2][i2] != 0) continue;
                for (n2 = i2; n2 < sArray[0].length; ++n2) {
                    sArray[i2][n2] = GF2Field.addElem(sArray[i2][n2], sArray[n3][n2]);
                }
            }
            short s2 = GF2Field.invElem(sArray[i2][i2]);
            if (s2 == 0) {
                throw new RuntimeException("The matrix is not invertible");
            }
            sArray[i2] = this.multVect(s2, sArray[i2]);
            for (n3 = 0; n3 < sArray.length; ++n3) {
                if (i2 == n3) continue;
                short s3 = sArray[n3][i2];
                for (n2 = i2; n2 < sArray[0].length; ++n2) {
                    short s4 = GF2Field.multElem(sArray[i2][n2], s3);
                    sArray[n3][n2] = GF2Field.addElem(sArray[n3][n2], s4);
                }
            }
        }
    }

    public short[][] multiplyMatrix(short[][] sArray, short[][] sArray2) throws RuntimeException {
        if (sArray[0].length != sArray2.length) {
            throw new RuntimeException("Multiplication is not possible!");
        }
        short s2 = 0;
        short[][] sArray3 = new short[sArray.length][sArray2[0].length];
        for (int i2 = 0; i2 < sArray.length; ++i2) {
            for (int i3 = 0; i3 < sArray2.length; ++i3) {
                for (int i4 = 0; i4 < sArray2[0].length; ++i4) {
                    s2 = GF2Field.multElem(sArray[i2][i3], sArray2[i3][i4]);
                    sArray3[i2][i4] = GF2Field.addElem(sArray3[i2][i4], s2);
                }
            }
        }
        return sArray3;
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

    public short multiplyMatrix_quad(short[][] sArray, short[] sArray2) throws RuntimeException {
        if (sArray.length != sArray[0].length || sArray[0].length != sArray2.length) {
            throw new RuntimeException("Multiplication is not possible!");
        }
        short s2 = 0;
        short[] sArray3 = new short[sArray.length];
        short s3 = 0;
        for (int i2 = 0; i2 < sArray.length; ++i2) {
            for (int i3 = 0; i3 < sArray2.length; ++i3) {
                s2 = GF2Field.multElem(sArray[i2][i3], sArray2[i3]);
                sArray3[i2] = GF2Field.addElem(sArray3[i2], s2);
            }
            s2 = GF2Field.multElem(sArray3[i2], sArray2[i2]);
            s3 = GF2Field.addElem(s3, s2);
        }
        return s3;
    }

    public short[] addVect(short[] sArray, short[] sArray2) {
        if (sArray.length != sArray2.length) {
            throw new RuntimeException("Addition is not possible! vector1.length: " + sArray.length + " vector2.length: " + sArray2.length);
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

    public short[][] addMatrix(short[][] sArray, short[][] sArray2) {
        if (sArray.length != sArray2.length || sArray[0].length != sArray2[0].length) {
            throw new RuntimeException("Addition is not possible!");
        }
        short[][] sArray3 = new short[sArray.length][sArray[0].length];
        for (int i2 = 0; i2 < sArray.length; ++i2) {
            for (int i3 = 0; i3 < sArray[0].length; ++i3) {
                sArray3[i2][i3] = GF2Field.addElem(sArray[i2][i3], sArray2[i2][i3]);
            }
        }
        return sArray3;
    }

    public short[][] addMatrixTranspose(short[][] sArray) {
        if (sArray.length != sArray[0].length) {
            throw new RuntimeException("Addition is not possible!");
        }
        return this.addMatrix(sArray, this.transpose(sArray));
    }

    public short[][] transpose(short[][] sArray) {
        short[][] sArray2 = new short[sArray[0].length][sArray.length];
        for (int i2 = 0; i2 < sArray.length; ++i2) {
            for (int i3 = 0; i3 < sArray[0].length; ++i3) {
                sArray2[i3][i2] = sArray[i2][i3];
            }
        }
        return sArray2;
    }

    public short[][] to_UT(short[][] sArray) {
        if (sArray.length != sArray[0].length) {
            throw new RuntimeException("Computation to upper triangular matrix is not possible!");
        }
        short[][] sArray2 = new short[sArray.length][sArray.length];
        for (int i2 = 0; i2 < sArray.length; ++i2) {
            sArray2[i2][i2] = sArray[i2][i2];
            for (int i3 = i2 + 1; i3 < sArray[0].length; ++i3) {
                sArray2[i2][i3] = GF2Field.addElem(sArray[i2][i3], sArray[i3][i2]);
            }
        }
        return sArray2;
    }

    public short[][][] obfuscate_l1_polys(short[][] sArray, short[][][] sArray2, short[][][] sArray3) {
        if (sArray2[0].length != sArray3[0].length || sArray2[0][0].length != sArray3[0][0].length || sArray2.length != sArray[0].length || sArray3.length != sArray.length) {
            throw new RuntimeException("Multiplication not possible!");
        }
        short[][][] sArray4 = new short[sArray3.length][sArray3[0].length][sArray3[0][0].length];
        for (int i2 = 0; i2 < sArray2[0].length; ++i2) {
            for (int i3 = 0; i3 < sArray2[0][0].length; ++i3) {
                for (int i4 = 0; i4 < sArray.length; ++i4) {
                    for (int i5 = 0; i5 < sArray[0].length; ++i5) {
                        short s2 = GF2Field.multElem(sArray[i4][i5], sArray2[i5][i2][i3]);
                        sArray4[i4][i2][i3] = GF2Field.addElem(sArray4[i4][i2][i3], s2);
                    }
                    sArray4[i4][i2][i3] = GF2Field.addElem(sArray3[i4][i2][i3], sArray4[i4][i2][i3]);
                }
            }
        }
        return sArray4;
    }
}

