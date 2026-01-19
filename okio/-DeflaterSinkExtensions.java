/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.util.zip.Deflater;
import kotlin.Metadata;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import okio.DeflaterSink;
import okio.Sink;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={2, 1, 0}, k=2, xi=48, d1={"\u0000\u0018\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a \u0010\u0000\u001a\u00020\u0001*\u00020\u00022\f\b\u0002\u0010\u0003\u001a\u00060\u0004j\u0002`\u0005H\u0086\b\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u0007"}, d2={"deflate", "Lokio/DeflaterSink;", "Lokio/Sink;", "deflater", "Ljava/util/zip/Deflater;", "Lokio/Deflater;", "(Lokio/Sink;Ljava/util/zip/Deflater;)Lokio/DeflaterSink;", "okio"})
@JvmName(name="-DeflaterSinkExtensions")
public final class -DeflaterSinkExtensions {
    @NotNull
    public static final DeflaterSink deflate(@NotNull Sink $this$deflate, @NotNull Deflater deflater) {
        Intrinsics.checkNotNullParameter($this$deflate, "<this>");
        Intrinsics.checkNotNullParameter(deflater, "deflater");
        boolean $i$f$deflate = false;
        return new DeflaterSink($this$deflate, deflater);
    }

    public static /* synthetic */ DeflaterSink deflate$default(Sink $this$deflate_u24default, Deflater deflater, int n2, Object object) {
        if ((n2 & 1) != 0) {
            deflater = new Deflater();
        }
        Intrinsics.checkNotNullParameter($this$deflate_u24default, "<this>");
        Intrinsics.checkNotNullParameter(deflater, "deflater");
        boolean $i$f$deflate = false;
        return new DeflaterSink($this$deflate_u24default, deflater);
    }
}

