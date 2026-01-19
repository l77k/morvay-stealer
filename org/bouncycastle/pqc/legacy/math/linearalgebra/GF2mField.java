/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.linearalgebra;

import java.security.SecureRandom;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.pqc.legacy.math.linearalgebra.LittleEndianConversions;
import org.bouncycastle.pqc.legacy.math.linearalgebra.PolynomialRingGF2;
import org.bouncycastle.pqc.legacy.math.linearalgebra.RandUtils;

public class GF2mField {
    private int degree = 0;
    private int polynomial;

    public GF2mField(int n2) {
        if (n2 >= 32) {
            throw new IllegalArgumentException(" Error: the degree of field is too large ");
        }
        if (n2 < 1) {
            throw new IllegalArgumentException(" Error: the degree of field is non-positive ");
        }
        this.degree = n2;
        this.polynomial = PolynomialRingGF2.getIrreduciblePolynomial(n2);
    }

    public GF2mField(int n2, int n3) {
        if (n2 != PolynomialRingGF2.degree(n3)) {
            throw new IllegalArgumentException(" Error: the degree is not correct");
        }
        if (!PolynomialRingGF2.isIrreducible(n3)) {
            throw new IllegalArgumentException(" Error: given polynomial is reducible");
        }
        this.degree = n2;
        this.polynomial = n3;
    }

    public GF2mField(byte[] byArray) {
        if (byArray.length != 4) {
            throw new IllegalArgumentException("byte array is not an encoded finite field");
        }
        this.polynomial = LittleEndianConversions.OS2IP(byArray);
        if (!PolynomialRingGF2.isIrreducible(this.polynomial)) {
            throw new IllegalArgumentException("byte array is not an encoded finite field");
        }
        this.degree = PolynomialRingGF2.degree(this.polynomial);
    }

    public GF2mField(GF2mField gF2mField) {
        this.degree = gF2mField.degree;
        this.polynomial = gF2mField.polynomial;
    }

    public int getDegree() {
        return this.degree;
    }

    public int getPolynomial() {
        return this.polynomial;
    }

    public byte[] getEncoded() {
        return LittleEndianConversions.I2OSP(this.polynomial);
    }

    public int add(int n2, int n3) {
        return n2 ^ n3;
    }

    public int mult(int n2, int n3) {
        return PolynomialRingGF2.modMultiply(n2, n3, this.polynomial);
    }

    public int exp(int n2, int n3) {
        if (n3 == 0) {
            return 1;
        }
        if (n2 == 0) {
            return 0;
        }
        if (n2 == 1) {
            return 1;
        }
        int n4 = 1;
        if (n3 < 0) {
            n2 = this.inverse(n2);
            n3 = -n3;
        }
        while (n3 != 0) {
            if ((n3 & 1) == 1) {
                n4 = this.mult(n4, n2);
            }
            n2 = this.mult(n2, n2);
            n3 >>>= 1;
        }
        return n4;
    }

    public int inverse(int n2) {
        int n3 = (1 << this.degree) - 2;
        return this.exp(n2, n3);
    }

    public int sqRoot(int n2) {
        for (int i2 = 1; i2 < this.degree; ++i2) {
            n2 = this.mult(n2, n2);
        }
        return n2;
    }

    public int getRandomElement(SecureRandom secureRandom) {
        int n2 = RandUtils.nextInt(secureRandom, 1 << this.degree);
        return n2;
    }

    public int getRandomNonZeroElement() {
        return this.getRandomNonZeroElement(CryptoServicesRegistrar.getSecureRandom());
    }

    public int getRandomNonZeroElement(SecureRandom secureRandom) {
        int n2;
        int n3 = 0x100000;
        int n4 = RandUtils.nextInt(secureRandom, 1 << this.degree);
        for (n2 = 0; n4 == 0 && n2 < n3; ++n2) {
            n4 = RandUtils.nextInt(secureRandom, 1 << this.degree);
        }
        if (n2 == n3) {
            n4 = 1;
        }
        return n4;
    }

    public boolean isElementOfThisField(int n2) {
        if (this.degree == 31) {
            return n2 >= 0;
        }
        return n2 >= 0 && n2 < 1 << this.degree;
    }

    public String elementToStr(int n2) {
        String string = "";
        for (int i2 = 0; i2 < this.degree; ++i2) {
            string = ((byte)n2 & 1) == 0 ? "0" + string : "1" + string;
            n2 >>>= 1;
        }
        return string;
    }

    public boolean equals(Object object) {
        if (object == null || !(object instanceof GF2mField)) {
            return false;
        }
        GF2mField gF2mField = (GF2mField)object;
        return this.degree == gF2mField.degree && this.polynomial == gF2mField.polynomial;
    }

    public int hashCode() {
        return this.polynomial;
    }

    public String toString() {
        String string = "Finite Field GF(2^" + this.degree + ") = GF(2)[X]/<" + GF2mField.polyToString(this.polynomial) + "> ";
        return string;
    }

    private static String polyToString(int n2) {
        String string = "";
        if (n2 == 0) {
            string = "0";
        } else {
            byte by = (byte)(n2 & 1);
            if (by == 1) {
                string = "1";
            }
            n2 >>>= 1;
            int n3 = 1;
            while (n2 != 0) {
                by = (byte)(n2 & 1);
                if (by == 1) {
                    string = string + "+x^" + n3;
                }
                n2 >>>= 1;
                ++n3;
            }
        }
        return string;
    }
}

