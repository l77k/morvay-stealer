/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class ParametersWithIV
implements CipherParameters {
    private byte[] iv;
    private CipherParameters parameters;

    public ParametersWithIV(CipherParameters cipherParameters, byte[] byArray) {
        this(cipherParameters, byArray, 0, byArray.length);
    }

    public ParametersWithIV(CipherParameters cipherParameters, byte[] byArray, int n2, int n3) {
        this.iv = new byte[n3];
        this.parameters = cipherParameters;
        System.arraycopy(byArray, n2, this.iv, 0, n3);
    }

    public byte[] getIV() {
        return this.iv;
    }

    public CipherParameters getParameters() {
        return this.parameters;
    }
}

