/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class ParametersWithUKM
implements CipherParameters {
    private byte[] ukm;
    private CipherParameters parameters;

    public ParametersWithUKM(CipherParameters cipherParameters, byte[] byArray) {
        this(cipherParameters, byArray, 0, byArray.length);
    }

    public ParametersWithUKM(CipherParameters cipherParameters, byte[] byArray, int n2, int n3) {
        this.ukm = new byte[n3];
        this.parameters = cipherParameters;
        System.arraycopy(byArray, n2, this.ukm, 0, n3);
    }

    public byte[] getUKM() {
        return this.ukm;
    }

    public CipherParameters getParameters() {
        return this.parameters;
    }
}

