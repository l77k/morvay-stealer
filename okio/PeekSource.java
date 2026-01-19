/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.SourceDebugExtension
 */
package okio;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.Buffer;
import okio.BufferedSource;
import okio.Segment;
import okio.Source;
import okio.Timeout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u0018\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u000fH\u0016J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2={"Lokio/PeekSource;", "Lokio/Source;", "upstream", "Lokio/BufferedSource;", "<init>", "(Lokio/BufferedSource;)V", "buffer", "Lokio/Buffer;", "expectedSegment", "Lokio/Segment;", "expectedPos", "", "closed", "", "pos", "", "read", "sink", "byteCount", "timeout", "Lokio/Timeout;", "close", "", "okio"})
@SourceDebugExtension(value={"SMAP\nPeekSource.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PeekSource.kt\nokio/PeekSource\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,74:1\n1#2:75\n*E\n"})
public final class PeekSource
implements Source {
    @NotNull
    private final BufferedSource upstream;
    @NotNull
    private final Buffer buffer;
    @Nullable
    private Segment expectedSegment;
    private int expectedPos;
    private boolean closed;
    private long pos;

    public PeekSource(@NotNull BufferedSource upstream) {
        Intrinsics.checkNotNullParameter(upstream, "upstream");
        this.upstream = upstream;
        this.buffer = this.upstream.getBuffer();
        this.expectedSegment = this.buffer.head;
        Segment segment = this.buffer.head;
        this.expectedPos = segment != null ? segment.pos : -1;
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public long read(@NotNull Buffer sink, long byteCount) {
        Intrinsics.checkNotNullParameter(sink, "sink");
        if (!(byteCount >= 0L)) {
            $i$a$-require-PeekSource$read$1 = false;
            $i$a$-require-PeekSource$read$1 = "byteCount < 0: " + byteCount;
            throw new IllegalArgumentException($i$a$-require-PeekSource$read$1.toString());
        }
        if (!(this.closed == false)) {
            $i$a$-check-PeekSource$read$2 = false;
            $i$a$-check-PeekSource$read$2 = "closed";
            throw new IllegalStateException($i$a$-check-PeekSource$read$2.toString());
        }
        if (this.expectedSegment == null) ** GOTO lbl-1000
        if (this.expectedSegment == this.buffer.head) {
            v0 = this.buffer.head;
            Intrinsics.checkNotNull(v0);
            ** if (this.expectedPos != v0.pos) goto lbl-1000
        }
        ** GOTO lbl-1000
lbl-1000:
        // 2 sources

        {
            v1 = true;
            ** GOTO lbl19
        }
lbl-1000:
        // 2 sources

        {
            v1 = false;
        }
lbl19:
        // 2 sources

        if (!v1) {
            $i$a$-check-PeekSource$read$3 = false;
            var5_8 = "Peek source is invalid because upstream source was used";
            throw new IllegalStateException(var5_8.toString());
        }
        if (byteCount == 0L) {
            return 0L;
        }
        if (!this.upstream.request(this.pos + 1L)) {
            return -1L;
        }
        if (this.expectedSegment == null && this.buffer.head != null) {
            this.expectedSegment = this.buffer.head;
            v2 = this.buffer.head;
            Intrinsics.checkNotNull(v2);
            this.expectedPos = v2.pos;
        }
        toCopy = Math.min(byteCount, this.buffer.size() - this.pos);
        this.buffer.copyTo(sink, this.pos, toCopy);
        this.pos += toCopy;
        return toCopy;
    }

    @Override
    @NotNull
    public Timeout timeout() {
        return this.upstream.timeout();
    }

    @Override
    public void close() {
        this.closed = true;
    }
}

