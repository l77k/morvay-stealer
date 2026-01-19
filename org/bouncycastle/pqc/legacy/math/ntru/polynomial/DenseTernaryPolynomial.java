/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.ntru.polynomial;

import java.security.SecureRandom;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.IntegerPolynomial;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.LongPolynomial5;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.TernaryPolynomial;
import org.bouncycastle.pqc.legacy.math.ntru.util.Util;
import org.bouncycastle.util.Arrays;

public class DenseTernaryPolynomial
extends IntegerPolynomial
implements TernaryPolynomial {
    DenseTernaryPolynomial(int n2) {
        super(n2);
        this.checkTernarity();
    }

    public DenseTernaryPolynomial(IntegerPolynomial integerPolynomial) {
        this(integerPolynomial.coeffs);
    }

    public DenseTernaryPolynomial(int[] nArray) {
        super(nArray);
        this.checkTernarity();
    }

    private void checkTernarity() {
        for (int i2 = 0; i2 != this.coeffs.length; ++i2) {
            int n2 = this.coeffs[i2];
            if (n2 >= -1 && n2 <= 1) continue;
            throw new IllegalStateException("Illegal value: " + n2 + ", must be one of {-1, 0, 1}");
        }
    }

    public static DenseTernaryPolynomial generateRandom(int n2, int n3, int n4, SecureRandom secureRandom) {
        int[] nArray = Util.generateRandomTernary(n2, n3, n4, secureRandom);
        return new DenseTernaryPolynomial(nArray);
    }

    public static DenseTernaryPolynomial generateRandom(int n2, SecureRandom secureRandom) {
        DenseTernaryPolynomial denseTernaryPolynomial = new DenseTernaryPolynomial(n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            denseTernaryPolynomial.coeffs[i2] = secureRandom.nextInt(3) - 1;
        }
        return denseTernaryPolynomial;
    }

    @Override
    public IntegerPolynomial mult(IntegerPolynomial integerPolynomial, int n2) {
        if (n2 == 2048) {
            IntegerPolynomial integerPolynomial2 = (IntegerPolynomial)integerPolynomial.clone();
            integerPolynomial2.modPositive(2048);
            LongPolynomial5 longPolynomial5 = new LongPolynomial5(integerPolynomial2);
            return longPolynomial5.mult(this).toIntegerPolynomial();
        }
        return super.mult(integerPolynomial, n2);
    }

    @Override
    public int[] getOnes() {
        int n2 = this.coeffs.length;
        int[] nArray = new int[n2];
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n4 = this.coeffs[i2];
            if (n4 != 1) continue;
            nArray[n3++] = i2;
        }
        return Arrays.copyOf(nArray, n3);
    }

    @Override
    public int[] getNegOnes() {
        int n2 = this.coeffs.length;
        int[] nArray = new int[n2];
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n4 = this.coeffs[i2];
            if (n4 != -1) continue;
            nArray[n3++] = i2;
        }
        return Arrays.copyOf(nArray, n3);
    }

    @Override
    public int size() {
        return this.coeffs.length;
    }
}

