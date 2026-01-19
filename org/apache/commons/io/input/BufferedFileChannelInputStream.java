/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.input;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.input.ByteBufferCleaner;
import org.apache.commons.io.input.Input;

public final class BufferedFileChannelInputStream
extends InputStream {
    private final ByteBuffer byteBuffer;
    private final FileChannel fileChannel;

    public static Builder builder() {
        return new Builder();
    }

    @Deprecated
    public BufferedFileChannelInputStream(File file) throws IOException {
        this(file, 8192);
    }

    @Deprecated
    public BufferedFileChannelInputStream(File file, int bufferSize) throws IOException {
        this(file.toPath(), bufferSize);
    }

    private BufferedFileChannelInputStream(FileChannel fileChannel, int bufferSize) {
        this.fileChannel = Objects.requireNonNull(fileChannel, "path");
        this.byteBuffer = ByteBuffer.allocateDirect(bufferSize);
        this.byteBuffer.flip();
    }

    @Deprecated
    public BufferedFileChannelInputStream(Path path) throws IOException {
        this(path, 8192);
    }

    @Deprecated
    public BufferedFileChannelInputStream(Path path, int bufferSize) throws IOException {
        this(FileChannel.open(path, StandardOpenOption.READ), bufferSize);
    }

    @Override
    public synchronized int available() throws IOException {
        if (!this.fileChannel.isOpen()) {
            return 0;
        }
        if (!this.refill()) {
            return 0;
        }
        return this.byteBuffer.remaining();
    }

    private void clean(ByteBuffer buffer) {
        if (buffer.isDirect()) {
            this.cleanDirectBuffer(buffer);
        }
    }

    private void cleanDirectBuffer(ByteBuffer buffer) {
        if (ByteBufferCleaner.isSupported()) {
            ByteBufferCleaner.clean(buffer);
        }
    }

    @Override
    public synchronized void close() throws IOException {
        try {
            this.fileChannel.close();
        }
        finally {
            this.clean(this.byteBuffer);
        }
    }

    @Override
    public synchronized int read() throws IOException {
        if (!this.refill()) {
            return -1;
        }
        return this.byteBuffer.get() & 0xFF;
    }

    @Override
    public synchronized int read(byte[] b2, int offset, int len) throws IOException {
        if (offset < 0 || len < 0 || offset + len < 0 || offset + len > b2.length) {
            throw new IndexOutOfBoundsException();
        }
        if (!this.refill()) {
            return -1;
        }
        len = Math.min(len, this.byteBuffer.remaining());
        this.byteBuffer.get(b2, offset, len);
        return len;
    }

    private boolean refill() throws IOException {
        Input.checkOpen(this.fileChannel.isOpen());
        if (!this.byteBuffer.hasRemaining()) {
            this.byteBuffer.clear();
            int nRead = 0;
            while (nRead == 0) {
                nRead = this.fileChannel.read(this.byteBuffer);
            }
            this.byteBuffer.flip();
            return nRead >= 0;
        }
        return true;
    }

    @Override
    public synchronized long skip(long n2) throws IOException {
        if (n2 <= 0L) {
            return 0L;
        }
        if ((long)this.byteBuffer.remaining() >= n2) {
            this.byteBuffer.position(this.byteBuffer.position() + (int)n2);
            return n2;
        }
        long skippedFromBuffer = this.byteBuffer.remaining();
        long toSkipFromFileChannel = n2 - skippedFromBuffer;
        this.byteBuffer.position(0);
        this.byteBuffer.flip();
        return skippedFromBuffer + this.skipFromFileChannel(toSkipFromFileChannel);
    }

    private long skipFromFileChannel(long n2) throws IOException {
        long currentFilePosition = this.fileChannel.position();
        long size = this.fileChannel.size();
        if (n2 > size - currentFilePosition) {
            this.fileChannel.position(size);
            return size - currentFilePosition;
        }
        this.fileChannel.position(currentFilePosition + n2);
        return n2;
    }

    public static class Builder
    extends AbstractStreamBuilder<BufferedFileChannelInputStream, Builder> {
        private FileChannel fileChannel;

        @Override
        public BufferedFileChannelInputStream get() throws IOException {
            return this.fileChannel != null ? new BufferedFileChannelInputStream(this.fileChannel, this.getBufferSize()) : new BufferedFileChannelInputStream(this.getPath(), this.getBufferSize());
        }

        public Builder setFileChannel(FileChannel fileChannel) {
            this.fileChannel = fileChannel;
            return this;
        }
    }
}

