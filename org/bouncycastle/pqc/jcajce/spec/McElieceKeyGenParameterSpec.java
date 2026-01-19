/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.jcajce.spec;

import java.security.InvalidParameterException;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.pqc.legacy.math.linearalgebra.PolynomialRingGF2;

public class McElieceKeyGenParameterSpec
implements AlgorithmParameterSpec {
    public static final int DEFAULT_M = 11;
    public static final int DEFAULT_T = 50;
    private int m;
    private int t;
    private int n;
    private int fieldPoly;

    public McElieceKeyGenParameterSpec() {
        this(11, 50);
    }

    public McElieceKeyGenParameterSpec(int n2) {
        if (n2 < 1) {
            throw new IllegalArgumentException("key size must be positive");
        }
        this.m = 0;
        this.n = 1;
        while (this.n < n2) {
            this.n <<= 1;
            ++this.m;
        }
        this.t = this.n >>> 1;
        this.t /= this.m;
        this.fieldPoly = PolynomialRingGF2.getIrreduciblePolynomial(this.m);
    }

    public McElieceKeyGenParameterSpec(int n2, int n3) throws InvalidParameterException {
        if (n2 < 1) {
            throw new IllegalArgumentException("m must be positive");
        }
        if (n2 > 32) {
            throw new IllegalArgumentException("m is too large");
        }
        this.m = n2;
        this.n = 1 << n2;
        if (n3 < 0) {
            throw new IllegalArgumentException("t must be positive");
        }
        if (n3 > this.n) {
            throw new IllegalArgumentException("t must be less than n = 2^m");
        }
        this.t = n3;
        this.fieldPoly = PolynomialRingGF2.getIrreduciblePolynomial(n2);
    }

    public McElieceKeyGenParameterSpec(int n2, int n3, int n4) {
        this.m = n2;
        if (n2 < 1) {
            throw new IllegalArgumentException("m must be positive");
        }
        if (n2 > 32) {
            throw new IllegalArgumentException(" m is too large");
        }
        this.n = 1 << n2;
        this.t = n3;
        if (n3 < 0) {
            throw new IllegalArgumentException("t must be positive");
        }
        if (n3 > this.n) {
            throw new IllegalArgumentException("t must be less than n = 2^m");
        }
        if (PolynomialRingGF2.degree(n4) != n2 || !PolynomialRingGF2.isIrreducible(n4)) {
            throw new IllegalArgumentException("polynomial is not a field polynomial for GF(2^m)");
        }
        this.fieldPoly = n4;
    }

    public int getM() {
        return this.m;
    }

    public int getN() {
        return this.n;
    }

    public int getT() {
        return this.t;
    }

    public int getFieldPoly() {
        return this.fieldPoly;
    }
}

