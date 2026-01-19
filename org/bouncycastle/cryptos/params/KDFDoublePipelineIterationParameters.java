/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.util.Arrays;

public final class KDFDoublePipelineIterationParameters
implements DerivationParameters {
    private static final int UNUSED_R = 32;
    private final byte[] ki;
    private final boolean useCounter;
    private final int r;
    private final byte[] fixedInputData;

    private KDFDoublePipelineIterationParameters(byte[] byArray, byte[] byArray2, int n2, boolean bl) {
        if (byArray == null) {
            throw new IllegalArgumentException("A KDF requires Ki (a seed) as input");
        }
        this.ki = Arrays.clone(byArray);
        this.fixedInputData = byArray2 == null ? new byte[0] : Arrays.clone(byArray2);
        if (n2 != 8 && n2 != 16 && n2 != 24 && n2 != 32) {
            throw new IllegalArgumentException("Length of counter should be 8, 16, 24 or 32");
        }
        this.r = n2;
        this.useCounter = bl;
    }

    public static KDFDoublePipelineIterationParameters createWithCounter(byte[] byArray, byte[] byArray2, int n2) {
        return new KDFDoublePipelineIterationParameters(byArray, byArray2, n2, true);
    }

    public static KDFDoublePipelineIterationParameters createWithoutCounter(byte[] byArray, byte[] byArray2) {
        return new KDFDoublePipelineIterationParameters(byArray, byArray2, 32, false);
    }

    public byte[] getKI() {
        return this.ki;
    }

    public boolean useCounter() {
        return this.useCounter;
    }

    public int getR() {
        return this.r;
    }

    public byte[] getFixedInputData() {
        return Arrays.clone(this.fixedInputData);
    }
}

