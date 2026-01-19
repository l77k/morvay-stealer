/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jcajce.provider.drbg;

import java.security.SecureRandom;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.crypto.prng.EntropySourceProvider;
import org.bouncycastle.jcajce.provider.drbg.IncrementalEntropySource;

class IncrementalEntropySourceProvider
implements EntropySourceProvider {
    private final SecureRandom random;
    private final boolean predictionResistant;

    public IncrementalEntropySourceProvider(SecureRandom secureRandom, boolean bl) {
        this.random = secureRandom;
        this.predictionResistant = bl;
    }

    @Override
    public EntropySource get(final int n2) {
        return new IncrementalEntropySource(){
            final int numBytes;
            {
                this.numBytes = (n2 + 7) / 8;
            }

            @Override
            public boolean isPredictionResistant() {
                return IncrementalEntropySourceProvider.this.predictionResistant;
            }

            @Override
            public byte[] getEntropy() {
                try {
                    return this.getEntropy(0L);
                }
                catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("initial entropy fetch interrupted");
                }
            }

            @Override
            public byte[] getEntropy(long l2) throws InterruptedException {
                byte[] byArray;
                int n22;
                byte[] byArray2 = new byte[this.numBytes];
                for (n22 = 0; n22 < this.numBytes / 8; ++n22) {
                    IncrementalEntropySourceProvider.sleep(l2);
                    byArray = IncrementalEntropySourceProvider.this.random.generateSeed(8);
                    System.arraycopy(byArray, 0, byArray2, n22 * 8, byArray.length);
                }
                n22 = this.numBytes - this.numBytes / 8 * 8;
                if (n22 != 0) {
                    IncrementalEntropySourceProvider.sleep(l2);
                    byArray = IncrementalEntropySourceProvider.this.random.generateSeed(n22);
                    System.arraycopy(byArray, 0, byArray2, byArray2.length - byArray.length, byArray.length);
                }
                return byArray2;
            }

            @Override
            public int entropySize() {
                return n2;
            }
        };
    }

    private static void sleep(long l2) throws InterruptedException {
        if (l2 != 0L) {
            Thread.sleep(l2);
        }
    }
}

