/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.agreement.kdf;

import org.bouncycastle.crypto.DerivationParameters;

public class GSKKDFParameters
implements DerivationParameters {
    private final byte[] z;
    private final int startCounter;
    private final byte[] nonce;

    public GSKKDFParameters(byte[] byArray, int n2) {
        this(byArray, n2, null);
    }

    public GSKKDFParameters(byte[] byArray, int n2, byte[] byArray2) {
        this.z = byArray;
        this.startCounter = n2;
        this.nonce = byArray2;
    }

    public byte[] getZ() {
        return this.z;
    }

    public int getStartCounter() {
        return this.startCounter;
    }

    public byte[] getNonce() {
        return this.nonce;
    }
}

