/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.output;

import java.io.Closeable;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import org.apache.commons.io.IOUtils;

public class ProxyWriter
extends FilterWriter {
    public ProxyWriter(Writer delegate) {
        super(delegate);
    }

    protected void afterWrite(int n2) throws IOException {
    }

    @Override
    public Writer append(char c2) throws IOException {
        try {
            this.beforeWrite(1);
            this.out.append(c2);
            this.afterWrite(1);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
        return this;
    }

    @Override
    public Writer append(CharSequence csq) throws IOException {
        try {
            int len = IOUtils.length(csq);
            this.beforeWrite(len);
            this.out.append(csq);
            this.afterWrite(len);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
        return this;
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        try {
            this.beforeWrite(end - start);
            this.out.append(csq, start, end);
            this.afterWrite(end - start);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
        return this;
    }

    protected void beforeWrite(int n2) throws IOException {
    }

    @Override
    public void close() throws IOException {
        IOUtils.close((Closeable)this.out, this::handleIOException);
    }

    @Override
    public void flush() throws IOException {
        try {
            this.out.flush();
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    protected void handleIOException(IOException e2) throws IOException {
        throw e2;
    }

    @Override
    public void write(char[] cbuf) throws IOException {
        try {
            int len = IOUtils.length(cbuf);
            this.beforeWrite(len);
            this.out.write(cbuf);
            this.afterWrite(len);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        try {
            this.beforeWrite(len);
            this.out.write(cbuf, off, len);
            this.afterWrite(len);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    @Override
    public void write(int c2) throws IOException {
        try {
            this.beforeWrite(1);
            this.out.write(c2);
            this.afterWrite(1);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    @Override
    public void write(String str) throws IOException {
        try {
            int len = IOUtils.length(str);
            this.beforeWrite(len);
            this.out.write(str);
            this.afterWrite(len);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        try {
            this.beforeWrite(len);
            this.out.write(str, off, len);
            this.afterWrite(len);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }
}

