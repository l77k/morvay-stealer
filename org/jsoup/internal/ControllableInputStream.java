/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package org.jsoup.internal;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import org.jsoup.Progress;
import org.jsoup.helper.Validate;
import org.jsoup.internal.SimpleBufferedInput;
import org.jspecify.annotations.Nullable;

public class ControllableInputStream
extends FilterInputStream {
    private final SimpleBufferedInput buff;
    private int maxSize;
    private long startTime;
    private long timeout = 0L;
    private int remaining;
    private int markPos;
    private boolean interrupted;
    private boolean allowClose = true;
    private @Nullable Progress<?> progress;
    private @Nullable Object progressContext;
    private int contentLength = -1;
    private int readPos = 0;

    private ControllableInputStream(SimpleBufferedInput in, int maxSize) {
        super(in);
        Validate.isTrue(maxSize >= 0);
        this.buff = in;
        this.maxSize = maxSize;
        this.remaining = maxSize;
        this.markPos = -1;
        this.startTime = System.nanoTime();
    }

    public static ControllableInputStream wrap(@Nullable InputStream in, int maxSize) {
        if (in instanceof ControllableInputStream) {
            return (ControllableInputStream)in;
        }
        return new ControllableInputStream(new SimpleBufferedInput(in), maxSize);
    }

    public static ControllableInputStream wrap(InputStream in, int bufferSize, int maxSize) {
        return ControllableInputStream.wrap(in, maxSize);
    }

    @Override
    public int read(byte[] b2, int off, int len) throws IOException {
        boolean capped;
        if (this.readPos == 0) {
            this.emitProgress();
        }
        boolean bl = capped = this.maxSize != 0;
        if (this.interrupted || capped && this.remaining <= 0) {
            return -1;
        }
        if (Thread.currentThread().isInterrupted()) {
            this.interrupted = true;
            return -1;
        }
        if (capped && len > this.remaining) {
            len = this.remaining;
        }
        while (true) {
            if (this.expired()) {
                throw new SocketTimeoutException("Read timeout");
            }
            try {
                int read = super.read(b2, off, len);
                if (read == -1) {
                    this.contentLength = this.readPos;
                } else {
                    this.remaining -= read;
                    this.readPos += read;
                }
                this.emitProgress();
                return read;
            }
            catch (SocketTimeoutException e2) {
                if (!this.expired() && this.timeout != 0L) continue;
                throw e2;
            }
            break;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static ByteBuffer readToByteBuffer(InputStream in, int max) throws IOException {
        Validate.isTrue(max >= 0, "maxSize must be 0 (unlimited) or larger");
        Validate.notNull(in);
        boolean capped = max > 0;
        byte[] readBuf = SimpleBufferedInput.BufferPool.borrow();
        int outSize = capped ? Math.min(max, 8192) : 8192;
        ByteBuffer outBuf = ByteBuffer.allocate(outSize);
        try {
            int read;
            int remaining = max;
            while ((read = in.read(readBuf, 0, capped ? Math.min(remaining, 8192) : 8192)) != -1) {
                if (outBuf.remaining() < read) {
                    int newCapacity = (int)Math.max((double)outBuf.capacity() * 1.5, (double)(outBuf.capacity() + read));
                    ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
                    outBuf.flip();
                    newBuffer.put(outBuf);
                    outBuf = newBuffer;
                }
                outBuf.put(readBuf, 0, read);
                if (!capped || (remaining -= read) > 0) continue;
            }
            outBuf.flip();
            ByteBuffer byteBuffer = outBuf;
            return byteBuffer;
        }
        finally {
            SimpleBufferedInput.BufferPool.release(readBuf);
        }
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        this.remaining = this.maxSize - this.markPos;
        this.readPos = this.markPos;
    }

    @Override
    public void mark(int readlimit) {
        super.mark(readlimit);
        this.markPos = this.maxSize - this.remaining;
    }

    public boolean baseReadFully() {
        return this.buff.baseReadFully();
    }

    public int max() {
        return this.maxSize;
    }

    public void max(int newMax) {
        this.remaining += newMax - this.maxSize;
        this.maxSize = newMax;
    }

    public void allowClose(boolean allowClose) {
        this.allowClose = allowClose;
    }

    @Override
    public void close() throws IOException {
        if (this.allowClose) {
            super.close();
        }
    }

    public ControllableInputStream timeout(long startTimeNanos, long timeoutMillis) {
        this.startTime = startTimeNanos;
        this.timeout = timeoutMillis * 1000000L;
        return this;
    }

    private void emitProgress() {
        if (this.progress == null) {
            return;
        }
        float percent = this.contentLength > 0 ? Math.min(100.0f, (float)this.readPos * 100.0f / (float)this.contentLength) : 0.0f;
        this.progress.onProgress(this.readPos, this.contentLength, percent, this.progressContext);
        if (percent == 100.0f) {
            this.progress = null;
        }
    }

    public <ProgressContext> ControllableInputStream onProgress(int contentLength, Progress<ProgressContext> callback, ProgressContext context) {
        Validate.notNull(callback);
        Validate.notNull(context);
        this.contentLength = contentLength;
        this.progress = callback;
        this.progressContext = context;
        return this;
    }

    private boolean expired() {
        if (this.timeout == 0L) {
            return false;
        }
        long now = System.nanoTime();
        long dur = now - this.startTime;
        return dur > this.timeout;
    }

    public BufferedInputStream inputStream() {
        return new BufferedInputStream(this.buff);
    }
}

