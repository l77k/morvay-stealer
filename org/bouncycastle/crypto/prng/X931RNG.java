/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.prng;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.prng.EntropySource;

public class X931RNG {
    private static final long BLOCK64_RESEED_MAX = 32768L;
    private static final long BLOCK128_RESEED_MAX = 0x800000L;
    private static final int BLOCK64_MAX_BITS_REQUEST = 4096;
    private static final int BLOCK128_MAX_BITS_REQUEST = 262144;
    private final BlockCipher engine;
    private final EntropySource entropySource;
    private final byte[] DT;
    private final byte[] I;
    private final byte[] R;
    private byte[] V;
    private long reseedCounter = 1L;

    public X931RNG(BlockCipher blockCipher, byte[] byArray, EntropySource entropySource) {
        this.engine = blockCipher;
        this.entropySource = entropySource;
        this.DT = new byte[blockCipher.getBlockSize()];
        System.arraycopy(byArray, 0, this.DT, 0, this.DT.length);
        this.I = new byte[blockCipher.getBlockSize()];
        this.R = new byte[blockCipher.getBlockSize()];
    }

    int generate(byte[] byArray, boolean bl) {
        int n2;
        if (this.R.length == 8) {
            if (this.reseedCounter > 32768L) {
                return -1;
            }
            if (X931RNG.isTooLarge(byArray, 512)) {
                throw new IllegalArgumentException("Number of bits per request limited to 4096");
            }
        } else {
            if (this.reseedCounter > 0x800000L) {
                return -1;
            }
            if (X931RNG.isTooLarge(byArray, 32768)) {
                throw new IllegalArgumentException("Number of bits per request limited to 262144");
            }
        }
        if (bl || this.V == null) {
            this.V = this.entropySource.getEntropy();
            if (this.V.length != this.engine.getBlockSize()) {
                throw new IllegalStateException("Insufficient entropy returned");
            }
        }
        int n3 = byArray.length / this.R.length;
        for (n2 = 0; n2 < n3; ++n2) {
            this.engine.processBlock(this.DT, 0, this.I, 0);
            this.process(this.R, this.I, this.V);
            this.process(this.V, this.R, this.I);
            System.arraycopy(this.R, 0, byArray, n2 * this.R.length, this.R.length);
            this.increment(this.DT);
        }
        n2 = byArray.length - n3 * this.R.length;
        if (n2 > 0) {
            this.engine.processBlock(this.DT, 0, this.I, 0);
            this.process(this.R, this.I, this.V);
            this.process(this.V, this.R, this.I);
            System.arraycopy(this.R, 0, byArray, n3 * this.R.length, n2);
            this.increment(this.DT);
        }
        ++this.reseedCounter;
        return byArray.length * 8;
    }

    void reseed() {
        this.V = this.entropySource.getEntropy();
        if (this.V.length != this.engine.getBlockSize()) {
            throw new IllegalStateException("Insufficient entropy returned");
        }
        this.reseedCounter = 1L;
    }

    EntropySource getEntropySource() {
        return this.entropySource;
    }

    private void process(byte[] byArray, byte[] byArray2, byte[] byArray3) {
        for (int i2 = 0; i2 != byArray.length; ++i2) {
            byArray[i2] = (byte)(byArray2[i2] ^ byArray3[i2]);
        }
        this.engine.processBlock(byArray, 0, byArray, 0);
    }

    private void increment(byte[] byArray) {
        int n2 = byArray.length - 1;
        while (n2 >= 0) {
            int n3 = n2--;
            byArray[n3] = (byte)(byArray[n3] + 1);
            if (byArray[n3] != 0) break;
        }
    }

    private static boolean isTooLarge(byte[] byArray, int n2) {
        return byArray != null && byArray.length > n2;
    }
}

