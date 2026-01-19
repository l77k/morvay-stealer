/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.commons.io.IOExceptionList;
import org.apache.commons.io.function.IOConsumer;

public class FilterCollectionWriter
extends Writer {
    protected final Collection<Writer> EMPTY_WRITERS = Collections.emptyList();
    protected final Collection<Writer> writers;

    protected FilterCollectionWriter(Collection<Writer> writers) {
        this.writers = writers == null ? this.EMPTY_WRITERS : writers;
    }

    protected FilterCollectionWriter(Writer ... writers) {
        this.writers = writers == null ? this.EMPTY_WRITERS : Arrays.asList(writers);
    }

    @Override
    public Writer append(char c2) throws IOException {
        return this.forAllWriters(w2 -> w2.append(c2));
    }

    @Override
    public Writer append(CharSequence csq) throws IOException {
        return this.forAllWriters(w2 -> w2.append(csq));
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        return this.forAllWriters(w2 -> w2.append(csq, start, end));
    }

    @Override
    public void close() throws IOException {
        this.forAllWriters(Writer::close);
    }

    @Override
    public void flush() throws IOException {
        this.forAllWriters(Writer::flush);
    }

    private FilterCollectionWriter forAllWriters(IOConsumer<Writer> action) throws IOExceptionList {
        IOConsumer.forAll(action, this.writers());
        return this;
    }

    @Override
    public void write(char[] cbuf) throws IOException {
        this.forAllWriters(w2 -> w2.write(cbuf));
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        this.forAllWriters(w2 -> w2.write(cbuf, off, len));
    }

    @Override
    public void write(int c2) throws IOException {
        this.forAllWriters(w2 -> w2.write(c2));
    }

    @Override
    public void write(String str) throws IOException {
        this.forAllWriters(w2 -> w2.write(str));
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        this.forAllWriters(w2 -> w2.write(str, off, len));
    }

    private Stream<Writer> writers() {
        return this.writers.stream().filter(Objects::nonNull);
    }
}

