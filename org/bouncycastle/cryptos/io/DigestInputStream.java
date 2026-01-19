/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.crypto.Digest;

public class DigestInputStream
extends FilterInputStream {
    protected Digest digest;

    public DigestInputStream(InputStream inputStream2, Digest digest) {
        super(inputStream2);
        this.digest = digest;
    }

    @Override
    public int read() throws IOException {
        int n2 = this.in.read();
        if (n2 >= 0) {
            this.digest.update((byte)n2);
        }
        return n2;
    }

    @Override
    public int read(byte[] byArray, int n2, int n3) throws IOException {
        int n4 = this.in.read(byArray, n2, n3);
        if (n4 > 0) {
            this.digest.update(byArray, n2, n4);
        }
        return n4;
    }

    public Digest getDigest() {
        return this.digest;
    }
}

