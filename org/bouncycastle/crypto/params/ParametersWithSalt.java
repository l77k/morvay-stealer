/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class ParametersWithSalt
implements CipherParameters {
    private byte[] salt;
    private CipherParameters parameters;

    public ParametersWithSalt(CipherParameters cipherParameters, byte[] byArray) {
        this(cipherParameters, byArray, 0, byArray.length);
    }

    public ParametersWithSalt(CipherParameters cipherParameters, byte[] byArray, int n2, int n3) {
        this.salt = new byte[n3];
        this.parameters = cipherParameters;
        System.arraycopy(byArray, n2, this.salt, 0, n3);
    }

    public byte[] getSalt() {
        return this.salt;
    }

    public CipherParameters getParameters() {
        return this.parameters;
    }
}

