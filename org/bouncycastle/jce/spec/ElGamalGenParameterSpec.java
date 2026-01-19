/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jce.spec;

import java.security.spec.AlgorithmParameterSpec;

public class ElGamalGenParameterSpec
implements AlgorithmParameterSpec {
    private int primeSize;

    public ElGamalGenParameterSpec(int n2) {
        this.primeSize = n2;
    }

    public int getPrimeSize() {
        return this.primeSize;
    }
}

