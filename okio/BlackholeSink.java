/*
 * Decompiled with CFR 0.152.
 */
package okio;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okio.Buffer;
import okio.Sink;
import okio.Timeout;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016J\b\u0010\n\u001a\u00020\u0005H\u0016J\b\u0010\u000b\u001a\u00020\fH\u0016J\b\u0010\r\u001a\u00020\u0005H\u0016\u00a8\u0006\u000e"}, d2={"Lokio/BlackholeSink;", "Lokio/Sink;", "<init>", "()V", "write", "", "source", "Lokio/Buffer;", "byteCount", "", "flush", "timeout", "Lokio/Timeout;", "close", "okio"})
final class BlackholeSink
implements Sink {
    @Override
    public void write(@NotNull Buffer source2, long byteCount) {
        Intrinsics.checkNotNullParameter(source2, "source");
        source2.skip(byteCount);
    }

    @Override
    public void flush() {
    }

    @Override
    @NotNull
    public Timeout timeout() {
        return Timeout.NONE;
    }

    @Override
    public void close() {
    }
}

