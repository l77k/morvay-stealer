/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.SourceDebugExtension
 */
package okio;

import java.io.IOException;
import java.io.InputStream;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.Buffer;
import okio.Okio;
import okio.Segment;
import okio.SegmentPool;
import okio.Source;
import okio.Timeout;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0012\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0004\b\u0006\u0010\u0007J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\tH\u0016J\b\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u0004\u001a\u00020\u0005H\u0016J\b\u0010\u000f\u001a\u00020\u0010H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2={"Lokio/InputStreamSource;", "Lokio/Source;", "input", "Ljava/io/InputStream;", "timeout", "Lokio/Timeout;", "<init>", "(Ljava/io/InputStream;Lokio/Timeout;)V", "read", "", "sink", "Lokio/Buffer;", "byteCount", "close", "", "toString", "", "okio"})
@SourceDebugExtension(value={"SMAP\nJvmOkio.kt\nKotlin\n*S Kotlin\n*F\n+ 1 JvmOkio.kt\nokio/InputStreamSource\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,236:1\n1#2:237\n85#3:238\n*S KotlinDebug\n*F\n+ 1 JvmOkio.kt\nokio/InputStreamSource\n*L\n92#1:238\n*E\n"})
class InputStreamSource
implements Source {
    @NotNull
    private final InputStream input;
    @NotNull
    private final Timeout timeout;

    public InputStreamSource(@NotNull InputStream input, @NotNull Timeout timeout2) {
        Intrinsics.checkNotNullParameter(input, "input");
        Intrinsics.checkNotNullParameter(timeout2, "timeout");
        this.input = input;
        this.timeout = timeout2;
    }

    @Override
    public long read(@NotNull Buffer sink2, long byteCount) {
        Intrinsics.checkNotNullParameter(sink2, "sink");
        if (byteCount == 0L) {
            return 0L;
        }
        if (!(byteCount >= 0L)) {
            boolean $i$a$-require-InputStreamSource$read$22 = false;
            String $i$a$-require-InputStreamSource$read$22 = "byteCount < 0: " + byteCount;
            throw new IllegalArgumentException($i$a$-require-InputStreamSource$read$22.toString());
        }
        try {
            this.timeout.throwIfReached();
            Segment tail = sink2.writableSegment$okio(1);
            int b$iv = 8192 - tail.limit;
            boolean $i$f$minOf = false;
            int maxToCopy = (int)Math.min(byteCount, (long)b$iv);
            int bytesRead = this.input.read(tail.data, tail.limit, maxToCopy);
            if (bytesRead == -1) {
                if (tail.pos == tail.limit) {
                    sink2.head = tail.pop();
                    SegmentPool.recycle(tail);
                }
                return -1L;
            }
            tail.limit += bytesRead;
            sink2.setSize$okio(sink2.size() + (long)bytesRead);
            return bytesRead;
        }
        catch (AssertionError e2) {
            if (Okio.isAndroidGetsocknameError(e2)) {
                throw new IOException((Throwable)((Object)e2));
            }
            throw e2;
        }
    }

    @Override
    public void close() {
        this.input.close();
    }

    @Override
    @NotNull
    public Timeout timeout() {
        return this.timeout;
    }

    @NotNull
    public String toString() {
        return "source(" + this.input + ')';
    }
}

