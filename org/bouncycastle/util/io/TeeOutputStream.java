/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.util.io;

import java.io.IOException;
import java.io.OutputStream;

public class TeeOutputStream
extends OutputStream {
    private OutputStream output1;
    private OutputStream output2;

    public TeeOutputStream(OutputStream outputStream2, OutputStream outputStream3) {
        this.output1 = outputStream2;
        this.output2 = outputStream3;
    }

    @Override
    public void write(byte[] byArray) throws IOException {
        this.output1.write(byArray);
        this.output2.write(byArray);
    }

    @Override
    public void write(byte[] byArray, int n2, int n3) throws IOException {
        this.output1.write(byArray, n2, n3);
        this.output2.write(byArray, n2, n3);
    }

    @Override
    public void write(int n2) throws IOException {
        this.output1.write(n2);
        this.output2.write(n2);
    }

    @Override
    public void flush() throws IOException {
        this.output1.flush();
        this.output2.flush();
    }

    @Override
    public void close() throws IOException {
        this.output1.close();
        this.output2.close();
    }
}

