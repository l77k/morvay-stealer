/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.SourceDebugExtension
 */
package okio;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;
import okio.Segment;
import okio.SegmentPool;
import okio.Source;
import okio.Timeout;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0019\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0004\b\u0006\u0010\u0007B\u0019\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0004\b\u0006\u0010\bJ\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u000eH\u0016J\u0016\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u000eJ\u0006\u0010\u0013\u001a\u00020\fJ\b\u0010\u0014\u001a\u00020\u0015H\u0002J\b\u0010\u0016\u001a\u00020\u0017H\u0016J\b\u0010\u0018\u001a\u00020\u0015H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2={"Lokio/InflaterSource;", "Lokio/Source;", "source", "Lokio/BufferedSource;", "inflater", "Ljava/util/zip/Inflater;", "<init>", "(Lokio/BufferedSource;Ljava/util/zip/Inflater;)V", "(Lokio/Source;Ljava/util/zip/Inflater;)V", "bufferBytesHeldByInflater", "", "closed", "", "read", "", "sink", "Lokio/Buffer;", "byteCount", "readOrInflate", "refill", "releaseBytesAfterInflate", "", "timeout", "Lokio/Timeout;", "close", "okio"})
@SourceDebugExtension(value={"SMAP\nInflaterSource.kt\nKotlin\n*S Kotlin\n*F\n+ 1 InflaterSource.kt\nokio/InflaterSource\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,132:1\n1#2:133\n85#3:134\n*S KotlinDebug\n*F\n+ 1 InflaterSource.kt\nokio/InflaterSource\n*L\n66#1:134\n*E\n"})
public final class InflaterSource
implements Source {
    @NotNull
    private final BufferedSource source;
    @NotNull
    private final Inflater inflater;
    private int bufferBytesHeldByInflater;
    private boolean closed;

    public InflaterSource(@NotNull BufferedSource source2, @NotNull Inflater inflater) {
        Intrinsics.checkNotNullParameter(source2, "source");
        Intrinsics.checkNotNullParameter(inflater, "inflater");
        this.source = source2;
        this.inflater = inflater;
    }

    public InflaterSource(@NotNull Source source2, @NotNull Inflater inflater) {
        Intrinsics.checkNotNullParameter(source2, "source");
        Intrinsics.checkNotNullParameter(inflater, "inflater");
        this(Okio.buffer(source2), inflater);
    }

    @Override
    public long read(@NotNull Buffer sink2, long byteCount) throws IOException {
        Intrinsics.checkNotNullParameter(sink2, "sink");
        do {
            long bytesInflated;
            if ((bytesInflated = this.readOrInflate(sink2, byteCount)) > 0L) {
                return bytesInflated;
            }
            if (!this.inflater.finished() && !this.inflater.needsDictionary()) continue;
            return -1L;
        } while (!this.source.exhausted());
        throw new EOFException("source exhausted prematurely");
    }

    public final long readOrInflate(@NotNull Buffer sink2, long byteCount) throws IOException {
        Intrinsics.checkNotNullParameter(sink2, "sink");
        if (!(byteCount >= 0L)) {
            boolean $i$a$-require-InflaterSource$readOrInflate$22 = false;
            String $i$a$-require-InflaterSource$readOrInflate$22 = "byteCount < 0: " + byteCount;
            throw new IllegalArgumentException($i$a$-require-InflaterSource$readOrInflate$22.toString());
        }
        if (!(!this.closed)) {
            boolean $i$a$-check-InflaterSource$readOrInflate$32 = false;
            String $i$a$-check-InflaterSource$readOrInflate$32 = "closed";
            throw new IllegalStateException($i$a$-check-InflaterSource$readOrInflate$32.toString());
        }
        if (byteCount == 0L) {
            return 0L;
        }
        try {
            Segment tail = sink2.writableSegment$okio(1);
            int b$iv = 8192 - tail.limit;
            boolean $i$f$minOf = false;
            int toRead = (int)Math.min(byteCount, (long)b$iv);
            this.refill();
            int bytesInflated = this.inflater.inflate(tail.data, tail.limit, toRead);
            this.releaseBytesAfterInflate();
            if (bytesInflated > 0) {
                tail.limit += bytesInflated;
                sink2.setSize$okio(sink2.size() + (long)bytesInflated);
                return bytesInflated;
            }
            if (tail.pos == tail.limit) {
                sink2.head = tail.pop();
                SegmentPool.recycle(tail);
            }
            return 0L;
        }
        catch (DataFormatException e2) {
            throw new IOException(e2);
        }
    }

    public final boolean refill() throws IOException {
        if (!this.inflater.needsInput()) {
            return false;
        }
        if (this.source.exhausted()) {
            return true;
        }
        Segment segment = this.source.getBuffer().head;
        Intrinsics.checkNotNull(segment);
        Segment head = segment;
        this.bufferBytesHeldByInflater = head.limit - head.pos;
        this.inflater.setInput(head.data, head.pos, this.bufferBytesHeldByInflater);
        return false;
    }

    private final void releaseBytesAfterInflate() {
        if (this.bufferBytesHeldByInflater == 0) {
            return;
        }
        int toRelease = this.bufferBytesHeldByInflater - this.inflater.getRemaining();
        this.bufferBytesHeldByInflater -= toRelease;
        this.source.skip(toRelease);
    }

    @Override
    @NotNull
    public Timeout timeout() {
        return this.source.timeout();
    }

    @Override
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.inflater.end();
        this.closed = true;
        this.source.close();
    }
}

