/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package org.jsoup.internal;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.jsoup.helper.Validate;
import org.jsoup.internal.SoftPool;
import org.jspecify.annotations.Nullable;

class SimpleBufferedInput
extends FilterInputStream {
    static final int BufferSize = 8192;
    static final SoftPool<byte[]> BufferPool = new SoftPool<byte[]>(() -> new byte[8192]);
    private byte @Nullable [] byteBuf;
    private int bufPos;
    private int bufLength;
    private int bufMark = -1;
    private boolean inReadFully = false;

    SimpleBufferedInput(@Nullable InputStream in) {
        super(in);
        if (in == null) {
            this.inReadFully = true;
        }
    }

    @Override
    public int read() throws IOException {
        if (this.bufPos >= this.bufLength) {
            this.fill();
            if (this.bufPos >= this.bufLength) {
                return -1;
            }
        }
        return this.getBuf()[this.bufPos++] & 0xFF;
    }

    @Override
    public int read(byte[] dest, int offset, int desiredLen) throws IOException {
        int read;
        Validate.notNull(dest);
        if (offset < 0 || desiredLen < 0 || desiredLen > dest.length - offset) {
            throw new IndexOutOfBoundsException();
        }
        if (desiredLen == 0) {
            return 0;
        }
        int bufAvail = this.bufLength - this.bufPos;
        if (bufAvail <= 0) {
            if (!this.inReadFully && this.bufMark < 0) {
                int read2 = this.in.read(dest, offset, desiredLen);
                this.closeIfDone(read2);
                return read2;
            }
            this.fill();
            bufAvail = this.bufLength - this.bufPos;
        }
        if ((read = Math.min(bufAvail, desiredLen)) <= 0) {
            return -1;
        }
        System.arraycopy(this.getBuf(), this.bufPos, dest, offset, read);
        this.bufPos += read;
        return read;
    }

    private void fill() throws IOException {
        if (this.inReadFully) {
            return;
        }
        if (this.byteBuf == null) {
            this.byteBuf = BufferPool.borrow();
        }
        if (this.bufMark < 0) {
            this.bufPos = 0;
        } else if (this.bufPos >= 8192) {
            if (this.bufMark > 0) {
                int size = this.bufPos - this.bufMark;
                System.arraycopy(this.byteBuf, this.bufMark, this.byteBuf, 0, size);
                this.bufPos = size;
                this.bufMark = 0;
            } else {
                this.bufMark = -1;
                this.bufPos = 0;
            }
        }
        this.bufLength = this.bufPos;
        int read = this.in.read(this.byteBuf, this.bufPos, this.byteBuf.length - this.bufPos);
        if (read > 0) {
            this.bufLength = read + this.bufPos;
            while (this.byteBuf.length - this.bufLength > 0 && this.in.available() >= 1 && (read = this.in.read(this.byteBuf, this.bufLength, this.byteBuf.length - this.bufLength)) > 0) {
                this.bufLength += read;
            }
        }
        this.closeIfDone(read);
    }

    private void closeIfDone(int read) throws IOException {
        if (read == -1) {
            this.inReadFully = true;
            super.close();
        }
    }

    byte[] getBuf() {
        Validate.notNull(this.byteBuf);
        return this.byteBuf;
    }

    boolean baseReadFully() {
        return this.inReadFully;
    }

    @Override
    public int available() throws IOException {
        if (this.byteBuf != null && this.bufLength - this.bufPos > 0) {
            return this.bufLength - this.bufPos;
        }
        return this.inReadFully ? 0 : this.in.available();
    }

    @Override
    public void mark(int readlimit) {
        if (readlimit > 8192) {
            throw new IllegalArgumentException("Read-ahead limit is greater than buffer size");
        }
        this.bufMark = this.bufPos;
    }

    @Override
    public void reset() throws IOException {
        if (this.bufMark < 0) {
            throw new IOException("Resetting to invalid mark");
        }
        this.bufPos = this.bufMark;
    }

    @Override
    public void close() throws IOException {
        if (this.in != null) {
            super.close();
        }
        if (this.byteBuf == null) {
            return;
        }
        BufferPool.release(this.byteBuf);
        this.byteBuf = null;
    }
}

