/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.input;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Objects;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.input.AbstractInputStream;

public class RandomAccessFileInputStream
extends AbstractInputStream {
    private final boolean propagateClose;
    private final RandomAccessFile randomAccessFile;

    public static Builder builder() {
        return new Builder();
    }

    @Deprecated
    public RandomAccessFileInputStream(RandomAccessFile file) {
        this(file, false);
    }

    @Deprecated
    public RandomAccessFileInputStream(RandomAccessFile file, boolean propagateClose) {
        this.randomAccessFile = Objects.requireNonNull(file, "file");
        this.propagateClose = propagateClose;
    }

    @Override
    public int available() throws IOException {
        long avail = this.availableLong();
        if (avail > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int)avail;
    }

    public long availableLong() throws IOException {
        return this.isClosed() ? 0L : this.randomAccessFile.length() - this.randomAccessFile.getFilePointer();
    }

    @Override
    public void close() throws IOException {
        super.close();
        if (this.propagateClose) {
            this.randomAccessFile.close();
        }
    }

    public RandomAccessFile getRandomAccessFile() {
        return this.randomAccessFile;
    }

    public boolean isCloseOnClose() {
        return this.propagateClose;
    }

    @Override
    public int read() throws IOException {
        return this.randomAccessFile.read();
    }

    @Override
    public int read(byte[] bytes) throws IOException {
        return this.randomAccessFile.read(bytes);
    }

    @Override
    public int read(byte[] bytes, int offset, int length) throws IOException {
        return this.randomAccessFile.read(bytes, offset, length);
    }

    @Override
    public long skip(long skipCount) throws IOException {
        long newPos;
        long fileLength;
        if (skipCount <= 0L) {
            return 0L;
        }
        long filePointer = this.randomAccessFile.getFilePointer();
        if (filePointer >= (fileLength = this.randomAccessFile.length())) {
            return 0L;
        }
        long targetPos = filePointer + skipCount;
        long l2 = newPos = targetPos > fileLength ? fileLength - 1L : targetPos;
        if (newPos > 0L) {
            this.randomAccessFile.seek(newPos);
        }
        return this.randomAccessFile.getFilePointer() - filePointer;
    }

    public static class Builder
    extends AbstractStreamBuilder<RandomAccessFileInputStream, Builder> {
        private boolean propagateClose;

        @Override
        public RandomAccessFileInputStream get() throws IOException {
            return new RandomAccessFileInputStream(this.getRandomAccessFile(), this.propagateClose);
        }

        public Builder setCloseOnClose(boolean propagateClose) {
            this.propagateClose = propagateClose;
            return this;
        }

        @Override
        public Builder setRandomAccessFile(RandomAccessFile randomAccessFile) {
            return (Builder)super.setRandomAccessFile(randomAccessFile);
        }
    }
}

