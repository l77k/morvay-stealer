/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.util.io;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.util.Arrays;

public class BufferingOutputStream
extends OutputStream {
    private final OutputStream other;
    private final byte[] buf;
    private int bufOff;

    public BufferingOutputStream(OutputStream outputStream2) {
        this.other = outputStream2;
        this.buf = new byte[4096];
    }

    public BufferingOutputStream(OutputStream outputStream2, int n2) {
        this.other = outputStream2;
        this.buf = new byte[n2];
    }

    @Override
    public void write(byte[] byArray, int n2, int n3) throws IOException {
        if (n3 < this.buf.length - this.bufOff) {
            System.arraycopy(byArray, n2, this.buf, this.bufOff, n3);
            this.bufOff += n3;
        } else {
            int n4 = this.buf.length - this.bufOff;
            System.arraycopy(byArray, n2, this.buf, this.bufOff, n4);
            this.bufOff += n4;
            this.flush();
            n2 += n4;
            n3 -= n4;
            while (n3 >= this.buf.length) {
                this.other.write(byArray, n2, this.buf.length);
                n2 += this.buf.length;
                n3 -= this.buf.length;
            }
            if (n3 > 0) {
                System.arraycopy(byArray, n2, this.buf, this.bufOff, n3);
                this.bufOff += n3;
            }
        }
    }

    @Override
    public void write(int n2) throws IOException {
        this.buf[this.bufOff++] = (byte)n2;
        if (this.bufOff == this.buf.length) {
            this.flush();
        }
    }

    @Override
    public void flush() throws IOException {
        this.other.write(this.buf, 0, this.bufOff);
        this.bufOff = 0;
        Arrays.fill(this.buf, (byte)0);
    }

    @Override
    public void close() throws IOException {
        this.flush();
        this.other.close();
    }
}

