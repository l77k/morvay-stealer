/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.prng;

import org.bouncycastle.crypto.prng.RandomGenerator;

public class ReversedWindowGenerator
implements RandomGenerator {
    private final RandomGenerator generator;
    private byte[] window;
    private int windowCount;

    public ReversedWindowGenerator(RandomGenerator randomGenerator, int n2) {
        if (randomGenerator == null) {
            throw new IllegalArgumentException("generator cannot be null");
        }
        if (n2 < 2) {
            throw new IllegalArgumentException("windowSize must be at least 2");
        }
        this.generator = randomGenerator;
        this.window = new byte[n2];
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void addSeedMaterial(byte[] byArray) {
        ReversedWindowGenerator reversedWindowGenerator = this;
        synchronized (reversedWindowGenerator) {
            this.windowCount = 0;
            this.generator.addSeedMaterial(byArray);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void addSeedMaterial(long l2) {
        ReversedWindowGenerator reversedWindowGenerator = this;
        synchronized (reversedWindowGenerator) {
            this.windowCount = 0;
            this.generator.addSeedMaterial(l2);
        }
    }

    @Override
    public void nextBytes(byte[] byArray) {
        this.doNextBytes(byArray, 0, byArray.length);
    }

    @Override
    public void nextBytes(byte[] byArray, int n2, int n3) {
        this.doNextBytes(byArray, n2, n3);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void doNextBytes(byte[] byArray, int n2, int n3) {
        ReversedWindowGenerator reversedWindowGenerator = this;
        synchronized (reversedWindowGenerator) {
            int n4 = 0;
            while (n4 < n3) {
                if (this.windowCount < 1) {
                    this.generator.nextBytes(this.window, 0, this.window.length);
                    this.windowCount = this.window.length;
                }
                byArray[n2 + n4++] = this.window[--this.windowCount];
            }
        }
    }
}

