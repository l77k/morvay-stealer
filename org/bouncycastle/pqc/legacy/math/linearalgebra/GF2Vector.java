/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.linearalgebra;

import java.security.SecureRandom;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2mField;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2mVector;
import org.bouncycastle.pqc.legacy.math.linearalgebra.IntUtils;
import org.bouncycastle.pqc.legacy.math.linearalgebra.LittleEndianConversions;
import org.bouncycastle.pqc.legacy.math.linearalgebra.Permutation;
import org.bouncycastle.pqc.legacy.math.linearalgebra.RandUtils;
import org.bouncycastle.pqc.legacy.math.linearalgebra.Vector;
import org.bouncycastle.util.Arrays;

public class GF2Vector
extends Vector {
    private int[] v;

    public GF2Vector(int n2) {
        if (n2 < 0) {
            throw new ArithmeticException("Negative length.");
        }
        this.length = n2;
        this.v = new int[n2 + 31 >> 5];
    }

    public GF2Vector(int n2, SecureRandom secureRandom) {
        int n3;
        this.length = n2;
        int n4 = n2 + 31 >> 5;
        this.v = new int[n4];
        for (n3 = n4 - 1; n3 >= 0; --n3) {
            this.v[n3] = secureRandom.nextInt();
        }
        n3 = n2 & 0x1F;
        if (n3 != 0) {
            int n5 = n4 - 1;
            this.v[n5] = this.v[n5] & (1 << n3) - 1;
        }
    }

    public GF2Vector(int n2, int n3, SecureRandom secureRandom) {
        int n4;
        if (n3 > n2) {
            throw new ArithmeticException("The hamming weight is greater than the length of vector.");
        }
        this.length = n2;
        int n5 = n2 + 31 >> 5;
        this.v = new int[n5];
        int[] nArray = new int[n2];
        for (n4 = 0; n4 < n2; ++n4) {
            nArray[n4] = n4;
        }
        n4 = n2;
        for (int i2 = 0; i2 < n3; ++i2) {
            int n6 = RandUtils.nextInt(secureRandom, n4);
            this.setBit(nArray[n6]);
            nArray[n6] = nArray[--n4];
        }
    }

    public GF2Vector(int n2, int[] nArray) {
        if (n2 < 0) {
            throw new ArithmeticException("negative length");
        }
        this.length = n2;
        int n3 = n2 + 31 >> 5;
        if (nArray.length != n3) {
            throw new ArithmeticException("length mismatch");
        }
        this.v = IntUtils.clone(nArray);
        int n4 = n2 & 0x1F;
        if (n4 != 0) {
            int n5 = n3 - 1;
            this.v[n5] = this.v[n5] & (1 << n4) - 1;
        }
    }

    public GF2Vector(GF2Vector gF2Vector) {
        this.length = gF2Vector.length;
        this.v = IntUtils.clone(gF2Vector.v);
    }

    protected GF2Vector(int[] nArray, int n2) {
        this.v = nArray;
        this.length = n2;
    }

    public static GF2Vector OS2VP(int n2, byte[] byArray) {
        if (n2 < 0) {
            throw new ArithmeticException("negative length");
        }
        int n3 = n2 + 7 >> 3;
        if (byArray.length > n3) {
            throw new ArithmeticException("length mismatch");
        }
        return new GF2Vector(n2, LittleEndianConversions.toIntArray(byArray));
    }

    @Override
    public byte[] getEncoded() {
        int n2 = this.length + 7 >> 3;
        return LittleEndianConversions.toByteArray(this.v, n2);
    }

    public int[] getVecArray() {
        return this.v;
    }

    public int getHammingWeight() {
        int n2 = 0;
        for (int i2 = 0; i2 < this.v.length; ++i2) {
            int n3 = this.v[i2];
            for (int i3 = 0; i3 < 32; ++i3) {
                int n4 = n3 & 1;
                if (n4 != 0) {
                    ++n2;
                }
                n3 >>>= 1;
            }
        }
        return n2;
    }

    @Override
    public boolean isZero() {
        for (int i2 = this.v.length - 1; i2 >= 0; --i2) {
            if (this.v[i2] == 0) continue;
            return false;
        }
        return true;
    }

    public int getBit(int n2) {
        if (n2 >= this.length) {
            throw new IndexOutOfBoundsException();
        }
        int n3 = n2 >> 5;
        int n4 = n2 & 0x1F;
        return (this.v[n3] & 1 << n4) >>> n4;
    }

    public void setBit(int n2) {
        if (n2 >= this.length) {
            throw new IndexOutOfBoundsException();
        }
        int n3 = n2 >> 5;
        this.v[n3] = this.v[n3] | 1 << (n2 & 0x1F);
    }

    @Override
    public Vector add(Vector vector) {
        if (!(vector instanceof GF2Vector)) {
            throw new ArithmeticException("vector is not defined over GF(2)");
        }
        GF2Vector gF2Vector = (GF2Vector)vector;
        if (this.length != gF2Vector.length) {
            throw new ArithmeticException("length mismatch");
        }
        int[] nArray = IntUtils.clone(((GF2Vector)vector).v);
        for (int i2 = nArray.length - 1; i2 >= 0; --i2) {
            int n2 = i2;
            nArray[n2] = nArray[n2] ^ this.v[i2];
        }
        return new GF2Vector(this.length, nArray);
    }

    @Override
    public Vector multiply(Permutation permutation) {
        int[] nArray = permutation.getVector();
        if (this.length != nArray.length) {
            throw new ArithmeticException("length mismatch");
        }
        GF2Vector gF2Vector = new GF2Vector(this.length);
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            int n2 = this.v[nArray[i2] >> 5] & 1 << (nArray[i2] & 0x1F);
            if (n2 == 0) continue;
            int n3 = i2 >> 5;
            gF2Vector.v[n3] = gF2Vector.v[n3] | 1 << (i2 & 0x1F);
        }
        return gF2Vector;
    }

    public GF2Vector extractVector(int[] nArray) {
        int n2 = nArray.length;
        if (nArray[n2 - 1] > this.length) {
            throw new ArithmeticException("invalid index set");
        }
        GF2Vector gF2Vector = new GF2Vector(n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3 = this.v[nArray[i2] >> 5] & 1 << (nArray[i2] & 0x1F);
            if (n3 == 0) continue;
            int n4 = i2 >> 5;
            gF2Vector.v[n4] = gF2Vector.v[n4] | 1 << (i2 & 0x1F);
        }
        return gF2Vector;
    }

    public GF2Vector extractLeftVector(int n2) {
        if (n2 > this.length) {
            throw new ArithmeticException("invalid length");
        }
        if (n2 == this.length) {
            return new GF2Vector(this);
        }
        GF2Vector gF2Vector = new GF2Vector(n2);
        int n3 = n2 >> 5;
        int n4 = n2 & 0x1F;
        System.arraycopy(this.v, 0, gF2Vector.v, 0, n3);
        if (n4 != 0) {
            gF2Vector.v[n3] = this.v[n3] & (1 << n4) - 1;
        }
        return gF2Vector;
    }

    public GF2Vector extractRightVector(int n2) {
        if (n2 > this.length) {
            throw new ArithmeticException("invalid length");
        }
        if (n2 == this.length) {
            return new GF2Vector(this);
        }
        GF2Vector gF2Vector = new GF2Vector(n2);
        int n3 = this.length - n2 >> 5;
        int n4 = this.length - n2 & 0x1F;
        int n5 = n2 + 31 >> 5;
        int n6 = n3;
        if (n4 != 0) {
            for (int i2 = 0; i2 < n5 - 1; ++i2) {
                gF2Vector.v[i2] = this.v[n6++] >>> n4 | this.v[n6] << 32 - n4;
            }
            gF2Vector.v[n5 - 1] = this.v[n6++] >>> n4;
            if (n6 < this.v.length) {
                int n7 = n5 - 1;
                gF2Vector.v[n7] = gF2Vector.v[n7] | this.v[n6] << 32 - n4;
            }
        } else {
            System.arraycopy(this.v, n3, gF2Vector.v, 0, n5);
        }
        return gF2Vector;
    }

    public GF2mVector toExtensionFieldVector(GF2mField gF2mField) {
        int n2 = gF2mField.getDegree();
        if (this.length % n2 != 0) {
            throw new ArithmeticException("conversion is impossible");
        }
        int n3 = this.length / n2;
        int[] nArray = new int[n3];
        int n4 = 0;
        for (int i2 = n3 - 1; i2 >= 0; --i2) {
            for (int i3 = gF2mField.getDegree() - 1; i3 >= 0; --i3) {
                int n5 = n4 >>> 5;
                int n6 = n4 & 0x1F;
                int n7 = this.v[n5] >>> n6 & 1;
                if (n7 == 1) {
                    int n8 = i2;
                    nArray[n8] = nArray[n8] ^ 1 << i3;
                }
                ++n4;
            }
        }
        return new GF2mVector(gF2mField, nArray);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GF2Vector)) {
            return false;
        }
        GF2Vector gF2Vector = (GF2Vector)object;
        return this.length == gF2Vector.length && IntUtils.equals(this.v, gF2Vector.v);
    }

    @Override
    public int hashCode() {
        int n2 = this.length;
        n2 = n2 * 31 + Arrays.hashCode(this.v);
        return n2;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < this.length; ++i2) {
            int n2;
            int n3;
            int n4;
            if (i2 != 0 && (i2 & 0x1F) == 0) {
                stringBuffer.append(' ');
            }
            if ((n4 = this.v[n3 = i2 >> 5] & 1 << (n2 = i2 & 0x1F)) == 0) {
                stringBuffer.append('0');
                continue;
            }
            stringBuffer.append('1');
        }
        return stringBuffer.toString();
    }
}

