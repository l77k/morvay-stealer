/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.util.io;

import java.io.OutputStream;

public class LimitedBuffer
extends OutputStream {
    private final byte[] buf;
    private int count;

    public LimitedBuffer(int n2) {
        this.buf = new byte[n2];
        this.count = 0;
    }

    public int copyTo(byte[] byArray, int n2) {
        System.arraycopy(this.buf, 0, byArray, n2, this.count);
        return this.count;
    }

    public int limit() {
        return this.buf.length;
    }

    public void reset() {
        this.count = 0;
    }

    public int size() {
        return this.count;
    }

    @Override
    public void write(int n2) {
        this.buf[this.count++] = (byte)n2;
    }

    @Override
    public void write(byte[] byArray) {
        System.arraycopy(byArray, 0, this.buf, this.count, byArray.length);
        this.count += byArray.length;
    }

    @Override
    public void write(byte[] byArray, int n2, int n3) {
        System.arraycopy(byArray, n2, this.buf, this.count, n3);
        this.count += n3;
    }
}

