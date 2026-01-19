/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.util.test;

import java.security.SecureRandom;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.crypto.prng.EntropySourceProvider;

public class TestRandomEntropySourceProvider
implements EntropySourceProvider {
    private final SecureRandom _sr = new SecureRandom();
    private final boolean _predictionResistant;

    public TestRandomEntropySourceProvider(boolean bl) {
        this._predictionResistant = bl;
    }

    @Override
    public EntropySource get(final int n2) {
        return new EntropySource(){

            @Override
            public boolean isPredictionResistant() {
                return TestRandomEntropySourceProvider.this._predictionResistant;
            }

            @Override
            public byte[] getEntropy() {
                byte[] byArray = new byte[(n2 + 7) / 8];
                TestRandomEntropySourceProvider.this._sr.nextBytes(byArray);
                return byArray;
            }

            @Override
            public int entropySize() {
                return n2;
            }
        };
    }
}

