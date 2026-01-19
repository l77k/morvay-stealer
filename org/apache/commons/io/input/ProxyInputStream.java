/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.input;

import java.io.Closeable;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.function.Erase;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.function.IOIntConsumer;
import org.apache.commons.io.input.Input;

public abstract class ProxyInputStream
extends FilterInputStream {
    private boolean closed;
    private final IOConsumer<IOException> exceptionHandler = Erase::rethrow;
    private final IOIntConsumer afterRead;

    protected ProxyInputStream(AbstractBuilder<?, ?> builder) throws IOException {
        this(builder.getInputStream(), builder);
    }

    public ProxyInputStream(InputStream proxy) {
        super(proxy);
        this.afterRead = IOIntConsumer.NOOP;
    }

    protected ProxyInputStream(InputStream proxy, AbstractBuilder<?, ?> builder) {
        super(proxy);
        this.afterRead = builder.getAfterRead() != null ? builder.getAfterRead() : IOIntConsumer.NOOP;
    }

    protected void afterRead(int n2) throws IOException {
        this.afterRead.accept(n2);
    }

    @Override
    public int available() throws IOException {
        if (this.in != null && !this.isClosed()) {
            try {
                return this.in.available();
            }
            catch (IOException e2) {
                this.handleIOException(e2);
            }
        }
        return 0;
    }

    protected void beforeRead(int n2) throws IOException {
    }

    void checkOpen() throws IOException {
        Input.checkOpen(!this.isClosed());
    }

    @Override
    public void close() throws IOException {
        IOUtils.close((Closeable)this.in, this::handleIOException);
        this.closed = true;
    }

    protected void handleIOException(IOException e2) throws IOException {
        this.exceptionHandler.accept(e2);
    }

    boolean isClosed() {
        return this.closed;
    }

    @Override
    public synchronized void mark(int readLimit) {
        if (this.in != null) {
            this.in.mark(readLimit);
        }
    }

    @Override
    public boolean markSupported() {
        return this.in != null && this.in.markSupported();
    }

    @Override
    public int read() throws IOException {
        try {
            this.beforeRead(1);
            int b2 = this.in.read();
            this.afterRead(b2 != -1 ? 1 : -1);
            return b2;
        }
        catch (IOException e2) {
            this.handleIOException(e2);
            return -1;
        }
    }

    @Override
    public int read(byte[] b2) throws IOException {
        try {
            this.beforeRead(IOUtils.length(b2));
            int n2 = this.in.read(b2);
            this.afterRead(n2);
            return n2;
        }
        catch (IOException e2) {
            this.handleIOException(e2);
            return -1;
        }
    }

    @Override
    public int read(byte[] b2, int off, int len) throws IOException {
        try {
            this.beforeRead(len);
            int n2 = this.in.read(b2, off, len);
            this.afterRead(n2);
            return n2;
        }
        catch (IOException e2) {
            this.handleIOException(e2);
            return -1;
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

    void setIn(InputStream in) {
        this.in = in;
    }

    @Override
    public long skip(long n2) throws IOException {
        try {
            return this.in.skip(n2);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
            return 0L;
        }
    }

    public InputStream unwrap() {
        return this.in;
    }

    protected static abstract class AbstractBuilder<T, B extends AbstractStreamBuilder<T, B>>
    extends AbstractStreamBuilder<T, B> {
        private IOIntConsumer afterRead;

        protected AbstractBuilder() {
        }

        public IOIntConsumer getAfterRead() {
            return this.afterRead;
        }

        public B setAfterRead(IOIntConsumer afterRead) {
            this.afterRead = afterRead;
            return (B)((AbstractStreamBuilder)this.asThis());
        }
    }
}

