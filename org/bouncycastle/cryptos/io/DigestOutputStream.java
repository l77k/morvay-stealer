/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.io;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.crypto.Digest;

public class DigestOutputStream
extends OutputStream {
    protected Digest digest;

    public DigestOutputStream(Digest digest) {
        this.digest = digest;
    }

    @Override
    public void write(int n2) throws IOException {
        this.digest.update((byte)n2);
    }

    @Override
    public void write(byte[] byArray, int n2, int n3) throws IOException {
        this.digest.update(byArray, n2, n3);
    }

    public byte[] getDigest() {
        byte[] byArray = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(byArray, 0);
        return byArray;
    }
}

