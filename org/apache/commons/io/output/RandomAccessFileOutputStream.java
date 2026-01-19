/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import org.apache.commons.io.build.AbstractStreamBuilder;

public final class RandomAccessFileOutputStream
extends OutputStream {
    private final RandomAccessFile randomAccessFile;

    public static Builder builder() {
        return new Builder();
    }

    private RandomAccessFileOutputStream(RandomAccessFile randomAccessFile) {
        this.randomAccessFile = Objects.requireNonNull(randomAccessFile);
    }

    @Override
    public void close() throws IOException {
        this.randomAccessFile.close();
        super.close();
    }

    @Override
    public void flush() throws IOException {
        this.randomAccessFile.getChannel().force(true);
        super.flush();
    }

    @Override
    public void write(int b2) throws IOException {
        this.randomAccessFile.write(b2);
    }

    public static final class Builder
    extends AbstractStreamBuilder<RandomAccessFileOutputStream, Builder> {
        private Builder() {
            this.setOpenOptions(StandardOpenOption.WRITE);
        }

        @Override
        public RandomAccessFileOutputStream get() throws IOException {
            return new RandomAccessFileOutputStream(this.getRandomAccessFile());
        }
    }
}

