/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.util.test;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class UncloseableOutputStream
extends FilterOutputStream {
    public UncloseableOutputStream(OutputStream outputStream2) {
        super(outputStream2);
    }

    @Override
    public void close() {
        throw new RuntimeException("close() called on UncloseableOutputStream");
    }

    @Override
    public void write(byte[] byArray, int n2, int n3) throws IOException {
        this.out.write(byArray, n2, n3);
    }
}

