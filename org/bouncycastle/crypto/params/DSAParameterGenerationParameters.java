/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.params;

import java.security.SecureRandom;

public class DSAParameterGenerationParameters {
    public static final int DIGITAL_SIGNATURE_USAGE = 1;
    public static final int KEY_ESTABLISHMENT_USAGE = 2;
    private final int l;
    private final int n;
    private final int usageIndex;
    private final int certainty;
    private final SecureRandom random;

    public DSAParameterGenerationParameters(int n2, int n3, int n4, SecureRandom secureRandom) {
        this(n2, n3, n4, secureRandom, -1);
    }

    public DSAParameterGenerationParameters(int n2, int n3, int n4, SecureRandom secureRandom, int n5) {
        this.l = n2;
        this.n = n3;
        this.certainty = n4;
        this.usageIndex = n5;
        this.random = secureRandom;
    }

    public int getL() {
        return this.l;
    }

    public int getN() {
        return this.n;
    }

    public int getCertainty() {
        return this.certainty;
    }

    public SecureRandom getRandom() {
        return this.random;
    }

    public int getUsageIndex() {
        return this.usageIndex;
    }
}

