/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.prng;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.prng.RandomGenerator;
import org.bouncycastle.util.Arrays;

public class DigestRandomGenerator
implements RandomGenerator {
    private static long CYCLE_COUNT = 10L;
    private long stateCounter;
    private long seedCounter;
    private Digest digest;
    private byte[] state;
    private byte[] seed;

    public DigestRandomGenerator(Digest digest) {
        this.digest = digest;
        this.seed = new byte[digest.getDigestSize()];
        this.seedCounter = 1L;
        this.state = new byte[digest.getDigestSize()];
        this.stateCounter = 1L;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void addSeedMaterial(byte[] byArray) {
        DigestRandomGenerator digestRandomGenerator = this;
        synchronized (digestRandomGenerator) {
            if (!Arrays.isNullOrEmpty(byArray)) {
                this.digestUpdate(byArray);
            }
            this.digestUpdate(this.seed);
            this.digestDoFinal(this.seed);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void addSeedMaterial(long l2) {
        DigestRandomGenerator digestRandomGenerator = this;
        synchronized (digestRandomGenerator) {
            this.digestAddCounter(l2);
            this.digestUpdate(this.seed);
            this.digestDoFinal(this.seed);
        }
    }

    @Override
    public void nextBytes(byte[] byArray) {
        this.nextBytes(byArray, 0, byArray.length);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void nextBytes(byte[] byArray, int n2, int n3) {
        DigestRandomGenerator digestRandomGenerator = this;
        synchronized (digestRandomGenerator) {
            int n4 = 0;
            this.generateState();
            int n5 = n2 + n3;
            for (int i2 = n2; i2 != n5; ++i2) {
                if (n4 == this.state.length) {
                    this.generateState();
                    n4 = 0;
                }
                byArray[i2] = this.state[n4++];
            }
        }
    }

    private void cycleSeed() {
        this.digestUpdate(this.seed);
        this.digestAddCounter(this.seedCounter++);
        this.digestDoFinal(this.seed);
    }

    private void generateState() {
        this.digestAddCounter(this.stateCounter++);
        this.digestUpdate(this.state);
        this.digestUpdate(this.seed);
        this.digestDoFinal(this.state);
        if (this.stateCounter % CYCLE_COUNT == 0L) {
            this.cycleSeed();
        }
    }

    private void digestAddCounter(long l2) {
        for (int i2 = 0; i2 != 8; ++i2) {
            this.digest.update((byte)l2);
            l2 >>>= 8;
        }
    }

    private void digestUpdate(byte[] byArray) {
        this.digest.update(byArray, 0, byArray.length);
    }

    private void digestDoFinal(byte[] byArray) {
        this.digest.doFinal(byArray, 0);
    }
}

