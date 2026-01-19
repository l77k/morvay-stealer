/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.ntru.polynomial;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.BigIntPolynomial;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.IntegerPolynomial;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.TernaryPolynomial;
import org.bouncycastle.pqc.legacy.math.ntru.util.ArrayEncoder;
import org.bouncycastle.pqc.legacy.math.ntru.util.Util;
import org.bouncycastle.util.Arrays;

public class SparseTernaryPolynomial
implements TernaryPolynomial {
    private static final int BITS_PER_INDEX = 11;
    private int N;
    private int[] ones;
    private int[] negOnes;

    SparseTernaryPolynomial(int n2, int[] nArray, int[] nArray2) {
        this.N = n2;
        this.ones = nArray;
        this.negOnes = nArray2;
    }

    public SparseTernaryPolynomial(IntegerPolynomial integerPolynomial) {
        this(integerPolynomial.coeffs);
    }

    public SparseTernaryPolynomial(int[] nArray) {
        this.N = nArray.length;
        this.ones = new int[this.N];
        this.negOnes = new int[this.N];
        int n2 = 0;
        int n3 = 0;
        block5: for (int i2 = 0; i2 < this.N; ++i2) {
            int n4 = nArray[i2];
            switch (n4) {
                case 1: {
                    this.ones[n2++] = i2;
                    continue block5;
                }
                case -1: {
                    this.negOnes[n3++] = i2;
                    continue block5;
                }
                case 0: {
                    continue block5;
                }
                default: {
                    throw new IllegalArgumentException("Illegal value: " + n4 + ", must be one of {-1, 0, 1}");
                }
            }
        }
        this.ones = Arrays.copyOf(this.ones, n2);
        this.negOnes = Arrays.copyOf(this.negOnes, n3);
    }

    public static SparseTernaryPolynomial fromBinary(InputStream inputStream2, int n2, int n3, int n4) throws IOException {
        int n5 = 2048;
        int n6 = 32 - Integer.numberOfLeadingZeros(n5 - 1);
        int n7 = (n3 * n6 + 7) / 8;
        byte[] byArray = Util.readFullLength(inputStream2, n7);
        int[] nArray = ArrayEncoder.decodeModQ(byArray, n3, n5);
        int n8 = (n4 * n6 + 7) / 8;
        byte[] byArray2 = Util.readFullLength(inputStream2, n8);
        int[] nArray2 = ArrayEncoder.decodeModQ(byArray2, n4, n5);
        return new SparseTernaryPolynomial(n2, nArray, nArray2);
    }

    public static SparseTernaryPolynomial generateRandom(int n2, int n3, int n4, SecureRandom secureRandom) {
        int[] nArray = Util.generateRandomTernary(n2, n3, n4, secureRandom);
        return new SparseTernaryPolynomial(nArray);
    }

    @Override
    public IntegerPolynomial mult(IntegerPolynomial integerPolynomial) {
        int n2;
        int n3;
        int n4;
        int n5;
        int[] nArray = integerPolynomial.coeffs;
        if (nArray.length != this.N) {
            throw new IllegalArgumentException("Number of coefficients must be the same");
        }
        int[] nArray2 = new int[this.N];
        for (n5 = 0; n5 != this.ones.length; ++n5) {
            n4 = this.ones[n5];
            n3 = this.N - 1 - n4;
            for (n2 = this.N - 1; n2 >= 0; --n2) {
                int n6 = n2;
                nArray2[n6] = nArray2[n6] + nArray[n3];
                if (--n3 >= 0) continue;
                n3 = this.N - 1;
            }
        }
        for (n5 = 0; n5 != this.negOnes.length; ++n5) {
            n4 = this.negOnes[n5];
            n3 = this.N - 1 - n4;
            for (n2 = this.N - 1; n2 >= 0; --n2) {
                int n7 = n2;
                nArray2[n7] = nArray2[n7] - nArray[n3];
                if (--n3 >= 0) continue;
                n3 = this.N - 1;
            }
        }
        return new IntegerPolynomial(nArray2);
    }

    @Override
    public IntegerPolynomial mult(IntegerPolynomial integerPolynomial, int n2) {
        IntegerPolynomial integerPolynomial2 = this.mult(integerPolynomial);
        integerPolynomial2.mod(n2);
        return integerPolynomial2;
    }

    @Override
    public BigIntPolynomial mult(BigIntPolynomial bigIntPolynomial) {
        int n2;
        int n3;
        int n4;
        int n5;
        BigInteger[] bigIntegerArray = bigIntPolynomial.coeffs;
        if (bigIntegerArray.length != this.N) {
            throw new IllegalArgumentException("Number of coefficients must be the same");
        }
        BigInteger[] bigIntegerArray2 = new BigInteger[this.N];
        for (n5 = 0; n5 < this.N; ++n5) {
            bigIntegerArray2[n5] = BigInteger.ZERO;
        }
        for (n5 = 0; n5 != this.ones.length; ++n5) {
            n4 = this.ones[n5];
            n3 = this.N - 1 - n4;
            for (n2 = this.N - 1; n2 >= 0; --n2) {
                bigIntegerArray2[n2] = bigIntegerArray2[n2].add(bigIntegerArray[n3]);
                if (--n3 >= 0) continue;
                n3 = this.N - 1;
            }
        }
        for (n5 = 0; n5 != this.negOnes.length; ++n5) {
            n4 = this.negOnes[n5];
            n3 = this.N - 1 - n4;
            for (n2 = this.N - 1; n2 >= 0; --n2) {
                bigIntegerArray2[n2] = bigIntegerArray2[n2].subtract(bigIntegerArray[n3]);
                if (--n3 >= 0) continue;
                n3 = this.N - 1;
            }
        }
        return new BigIntPolynomial(bigIntegerArray2);
    }

    @Override
    public int[] getOnes() {
        return this.ones;
    }

    @Override
    public int[] getNegOnes() {
        return this.negOnes;
    }

    public byte[] toBinary() {
        int n2 = 2048;
        byte[] byArray = ArrayEncoder.encodeModQ(this.ones, n2);
        byte[] byArray2 = ArrayEncoder.encodeModQ(this.negOnes, n2);
        byte[] byArray3 = Arrays.copyOf(byArray, byArray.length + byArray2.length);
        System.arraycopy(byArray2, 0, byArray3, byArray.length, byArray2.length);
        return byArray3;
    }

    @Override
    public IntegerPolynomial toIntegerPolynomial() {
        int n2;
        int n3;
        int[] nArray = new int[this.N];
        for (n3 = 0; n3 != this.ones.length; ++n3) {
            n2 = this.ones[n3];
            nArray[n2] = 1;
        }
        for (n3 = 0; n3 != this.negOnes.length; ++n3) {
            n2 = this.negOnes[n3];
            nArray[n2] = -1;
        }
        return new IntegerPolynomial(nArray);
    }

    @Override
    public int size() {
        return this.N;
    }

    @Override
    public void clear() {
        int n2;
        for (n2 = 0; n2 < this.ones.length; ++n2) {
            this.ones[n2] = 0;
        }
        for (n2 = 0; n2 < this.negOnes.length; ++n2) {
            this.negOnes[n2] = 0;
        }
    }

    public int hashCode() {
        int n2 = 1;
        n2 = 31 * n2 + this.N;
        n2 = 31 * n2 + Arrays.hashCode(this.negOnes);
        n2 = 31 * n2 + Arrays.hashCode(this.ones);
        return n2;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        SparseTernaryPolynomial sparseTernaryPolynomial = (SparseTernaryPolynomial)object;
        if (this.N != sparseTernaryPolynomial.N) {
            return false;
        }
        if (!Arrays.areEqual(this.negOnes, sparseTernaryPolynomial.negOnes)) {
            return false;
        }
        return Arrays.areEqual(this.ones, sparseTernaryPolynomial.ones);
    }
}

