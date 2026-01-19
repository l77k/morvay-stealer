/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.params.IESParameters;

public class IESWithCipherParameters
extends IESParameters {
    private int cipherKeySize;

    public IESWithCipherParameters(byte[] byArray, byte[] byArray2, int n2, int n3) {
        super(byArray, byArray2, n2);
        this.cipherKeySize = n3;
    }

    public int getCipherKeySize() {
        return this.cipherKeySize;
    }
}

