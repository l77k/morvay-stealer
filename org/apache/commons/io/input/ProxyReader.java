/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.input;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import org.apache.commons.io.IOUtils;

public abstract class ProxyReader
extends FilterReader {
    public ProxyReader(Reader delegate) {
        super(delegate);
    }

    protected void afterRead(int n2) throws IOException {
    }

    protected void beforeRead(int n2) throws IOException {
    }

    @Override
    public void close() throws IOException {
        try {
            this.in.close();
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    protected void handleIOException(IOException e2) throws IOException {
        throw e2;
    }

    @Override
    public synchronized void mark(int idx) throws IOException {
        try {
            this.in.mark(idx);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    @Override
    public boolean markSupported() {
        return this.in.markSupported();
    }

    @Override
    public int read() throws IOException {
        try {
            this.beforeRead(1);
            int c2 = this.in.read();
            this.afterRead(c2 != -1 ? 1 : -1);
            return c2;
        }
        catch (IOException e2) {
            this.handleIOException(e2);
            return -1;
        }
    }

    @Override
    public int read(char[] chr) throws IOException {
        try {
            this.beforeRead(IOUtils.length(chr));
            int n2 = this.in.read(chr);
            this.afterRead(n2);
            return n2;
        }
        catch (IOException e2) {
            this.handleIOException(e2);
            return -1;
        }
    }

    @Override
    public int read(char[] chr, int st, int len) throws IOException {
        try {
            this.beforeRead(len);
            int n2 = this.in.read(chr, st, len);
            this.afterRead(n2);
            return n2;
        }
        catch (IOException e2) {
            this.handleIOException(e2);
            return -1;
        }
    }

    @Override
    public int read(CharBuffer target) throws IOException {
        try {
            this.beforeRead(IOUtils.length(target));
            int n2 = this.in.read(target);
            this.afterRead(n2);
            return n2;
        }
        catch (IOException e2) {
            this.handleIOException(e2);
            return -1;
        }
    }

    @Override
    public boolean ready() throws IOException {
        try {
            return this.in.ready();
        }
        catch (IOException e2) {
            this.handleIOException(e2);
            return false;
        }
    }

    @Override
    public synchronized void reset() throws IOException {
        try {
            this.in.reset();
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    @Override
    public long skip(long ln) throws IOException {
        try {
            return this.in.skip(ln);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
            return 0L;
        }
    }
}

