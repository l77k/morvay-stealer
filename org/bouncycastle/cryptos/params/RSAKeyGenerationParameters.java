/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.params;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.KeyGenerationParameters;

public class RSAKeyGenerationParameters
extends KeyGenerationParameters {
    private BigInteger publicExponent;
    private int certainty;

    public RSAKeyGenerationParameters(BigInteger bigInteger, SecureRandom secureRandom, int n2, int n3) {
        super(secureRandom, n2);
        if (n2 < 12) {
            throw new IllegalArgumentException("key strength too small");
        }
        if (!bigInteger.testBit(0)) {
            throw new IllegalArgumentException("public exponent cannot be even");
        }
        this.publicExponent = bigInteger;
        this.certainty = n3;
    }

    public BigInteger getPublicExponent() {
        return this.publicExponent;
    }

    public int getCertainty() {
        return this.certainty;
    }
}

