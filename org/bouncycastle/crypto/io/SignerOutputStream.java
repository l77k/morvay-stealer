/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.io;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.crypto.Signer;

public class SignerOutputStream
extends OutputStream {
    protected Signer signer;

    public SignerOutputStream(Signer signer) {
        this.signer = signer;
    }

    @Override
    public void write(int n2) throws IOException {
        this.signer.update((byte)n2);
    }

    @Override
    public void write(byte[] byArray, int n2, int n3) throws IOException {
        this.signer.update(byArray, n2, n3);
    }

    public Signer getSigner() {
        return this.signer;
    }
}

