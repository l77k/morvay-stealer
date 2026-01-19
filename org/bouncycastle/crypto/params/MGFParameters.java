/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.DerivationParameters;

public class MGFParameters
implements DerivationParameters {
    byte[] seed;

    public MGFParameters(byte[] byArray) {
        this(byArray, 0, byArray.length);
    }

    public MGFParameters(byte[] byArray, int n2, int n3) {
        this.seed = new byte[n3];
        System.arraycopy(byArray, n2, this.seed, 0, n3);
    }

    public byte[] getSeed() {
        return this.seed;
    }
}

