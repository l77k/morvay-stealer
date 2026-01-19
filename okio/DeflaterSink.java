/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.SourceDebugExtension
 */
package okio;

import java.io.IOException;
import java.util.zip.Deflater;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.-SegmentedByteString;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Segment;
import okio.SegmentPool;
import okio.Sink;
import okio.Timeout;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\u0019\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0004\b\u0006\u0010\u0007B\u0019\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0004\b\u0006\u0010\bJ\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0010\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\nH\u0002J\b\u0010\u0013\u001a\u00020\fH\u0016J\r\u0010\u0014\u001a\u00020\fH\u0000\u00a2\u0006\u0002\b\u0015J\b\u0010\u0016\u001a\u00020\fH\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u001aH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2={"Lokio/DeflaterSink;", "Lokio/Sink;", "sink", "Lokio/BufferedSink;", "deflater", "Ljava/util/zip/Deflater;", "<init>", "(Lokio/BufferedSink;Ljava/util/zip/Deflater;)V", "(Lokio/Sink;Ljava/util/zip/Deflater;)V", "closed", "", "write", "", "source", "Lokio/Buffer;", "byteCount", "", "deflate", "syncFlush", "flush", "finishDeflate", "finishDeflate$okio", "close", "timeout", "Lokio/Timeout;", "toString", "", "okio"})
@SourceDebugExtension(value={"SMAP\nDeflaterSink.kt\nKotlin\n*S Kotlin\n*F\n+ 1 DeflaterSink.kt\nokio/DeflaterSink\n+ 2 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,135:1\n85#2:136\n*S KotlinDebug\n*F\n+ 1 DeflaterSink.kt\nokio/DeflaterSink\n*L\n38#1:136\n*E\n"})
public final class DeflaterSink
implements Sink {
    @NotNull
    private final BufferedSink sink;
    @NotNull
    private final Deflater deflater;
    private boolean closed;

    public DeflaterSink(@NotNull BufferedSink sink2, @NotNull Deflater deflater) {
        Intrinsics.checkNotNullParameter(sink2, "sink");
        Intrinsics.checkNotNullParameter(deflater, "deflater");
        this.sink = sink2;
        this.deflater = deflater;
    }

    public DeflaterSink(@NotNull Sink sink2, @NotNull Deflater deflater) {
        Intrinsics.checkNotNullParameter(sink2, "sink");
        Intrinsics.checkNotNullParameter(deflater, "deflater");
        this(Okio.buffer(sink2), deflater);
    }

    @Override
    public void write(@NotNull Buffer source2, long byteCount) throws IOException {
        int toDeflate;
        Intrinsics.checkNotNullParameter(source2, "source");
        -SegmentedByteString.checkOffsetAndCount(source2.size(), 0L, byteCount);
        for (long remaining = byteCount; remaining > 0L; remaining -= (long)toDeflate) {
            Segment head;
            Intrinsics.checkNotNull(source2.head);
            int b$iv = head.limit - head.pos;
            boolean $i$f$minOf = false;
            toDeflate = (int)Math.min(remaining, (long)b$iv);
            this.deflater.setInput(head.data, head.pos, toDeflate);
            this.deflate(false);
            source2.setSize$okio(source2.size() - (long)toDeflate);
            head.pos += toDeflate;
            if (head.pos != head.limit) continue;
            source2.head = head.pop();
            SegmentPool.recycle(head);
        }
    }

    private final void deflate(boolean syncFlush) {
        Segment s2;
        Buffer buffer = this.sink.getBuffer();
        while (true) {
            int n2;
            s2 = buffer.writableSegment$okio(1);
            try {
                n2 = syncFlush ? this.deflater.deflate(s2.data, s2.limit, 8192 - s2.limit, 2) : this.deflater.deflate(s2.data, s2.limit, 8192 - s2.limit);
            }
            catch (NullPointerException npe) {
                throw new IOException("Deflater already closed", npe);
            }
            int deflated = n2;
            if (deflated > 0) {
                s2.limit += deflated;
                buffer.setSize$okio(buffer.size() + (long)deflated);
                this.sink.emitCompleteSegments();
                continue;
            }
            if (this.deflater.needsInput()) break;
        }
        if (s2.pos == s2.limit) {
            buffer.head = s2.pop();
            SegmentPool.recycle(s2);
        }
    }

    @Override
    public void flush() throws IOException {
        this.deflate(true);
        this.sink.flush();
    }

    public final void finishDeflate$okio() {
        this.deflater.finish();
        this.deflate(false);
    }

    @Override
    public void close() throws IOException {
        Throwable thrown;
        block9: {
            block8: {
                if (this.closed) {
                    return;
                }
                thrown = null;
                try {
                    this.finishDeflate$okio();
                }
                catch (Throwable e2) {
                    thrown = e2;
                }
                try {
                    this.deflater.end();
                }
                catch (Throwable e3) {
                    if (thrown != null) break block8;
                    thrown = e3;
                }
            }
            try {
                this.sink.close();
            }
            catch (Throwable e4) {
                if (thrown != null) break block9;
                thrown = e4;
            }
        }
        this.closed = true;
        Throwable throwable = thrown;
        if (throwable != null) {
            throw throwable;
        }
    }

    @Override
    @NotNull
    public Timeout timeout() {
        return this.sink.timeout();
    }

    @NotNull
    public String toString() {
        return "DeflaterSink(" + this.sink + ')';
    }
}

