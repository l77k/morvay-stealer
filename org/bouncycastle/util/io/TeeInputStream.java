/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TeeInputStream
extends InputStream {
    private final InputStream input;
    private final OutputStream output;

    public TeeInputStream(InputStream inputStream2, OutputStream outputStream2) {
        this.input = inputStream2;
        this.output = outputStream2;
    }

    @Override
    public int available() throws IOException {
        return this.input.available();
    }

    @Override
    public int read(byte[] byArray) throws IOException {
        return this.read(byArray, 0, byArray.length);
    }

    @Override
    public int read(byte[] byArray, int n2, int n3) throws IOException {
        int n4 = this.input.read(byArray, n2, n3);
        if (n4 > 0) {
            this.output.write(byArray, n2, n4);
        }
        return n4;
    }

    @Override
    public int read() throws IOException {
        int n2 = this.input.read();
        if (n2 >= 0) {
            this.output.write(n2);
        }
        return n2;
    }

    @Override
    public void close() throws IOException {
        this.input.close();
        this.output.close();
    }

    public OutputStream getOutputStream() {
        return this.output;
    }
}

