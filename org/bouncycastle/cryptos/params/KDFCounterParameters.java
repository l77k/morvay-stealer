/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.util.Arrays;

public final class KDFCounterParameters
implements DerivationParameters {
    private byte[] ki;
    private byte[] fixedInputDataCounterPrefix;
    private byte[] fixedInputDataCounterSuffix;
    private int r;

    public KDFCounterParameters(byte[] byArray, byte[] byArray2, int n2) {
        this(byArray, null, byArray2, n2);
    }

    public KDFCounterParameters(byte[] byArray, byte[] byArray2, byte[] byArray3, int n2) {
        if (byArray == null) {
            throw new IllegalArgumentException("A KDF requires Ki (a seed) as input");
        }
        this.ki = Arrays.clone(byArray);
        this.fixedInputDataCounterPrefix = byArray2 == null ? new byte[0] : Arrays.clone(byArray2);
        this.fixedInputDataCounterSuffix = byArray3 == null ? new byte[0] : Arrays.clone(byArray3);
        if (n2 != 8 && n2 != 16 && n2 != 24 && n2 != 32) {
            throw new IllegalArgumentException("Length of counter should be 8, 16, 24 or 32");
        }
        this.r = n2;
    }

    public byte[] getKI() {
        return this.ki;
    }

    public byte[] getFixedInputData() {
        return Arrays.clone(this.fixedInputDataCounterSuffix);
    }

    public byte[] getFixedInputDataCounterPrefix() {
        return Arrays.clone(this.fixedInputDataCounterPrefix);
    }

    public byte[] getFixedInputDataCounterSuffix() {
        return Arrays.clone(this.fixedInputDataCounterSuffix);
    }

    public int getR() {
        return this.r;
    }
}

