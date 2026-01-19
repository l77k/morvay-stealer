/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.util.zip.Inflater;
import kotlin.Metadata;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import okio.InflaterSource;
import okio.Source;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={2, 1, 0}, k=2, xi=48, d1={"\u0000\u0018\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a \u0010\u0000\u001a\u00020\u0001*\u00020\u00022\f\b\u0002\u0010\u0003\u001a\u00060\u0004j\u0002`\u0005H\u0086\b\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u0007"}, d2={"inflate", "Lokio/InflaterSource;", "Lokio/Source;", "inflater", "Ljava/util/zip/Inflater;", "Lokio/Inflater;", "(Lokio/Source;Ljava/util/zip/Inflater;)Lokio/InflaterSource;", "okio"})
@JvmName(name="-InflaterSourceExtensions")
public final class -InflaterSourceExtensions {
    @NotNull
    public static final InflaterSource inflate(@NotNull Source $this$inflate, @NotNull Inflater inflater) {
        Intrinsics.checkNotNullParameter($this$inflate, "<this>");
        Intrinsics.checkNotNullParameter(inflater, "inflater");
        boolean $i$f$inflate = false;
        return new InflaterSource($this$inflate, inflater);
    }

    public static /* synthetic */ InflaterSource inflate$default(Source $this$inflate_u24default, Inflater inflater, int n2, Object object) {
        if ((n2 & 1) != 0) {
            inflater = new Inflater();
        }
        Intrinsics.checkNotNullParameter($this$inflate_u24default, "<this>");
        Intrinsics.checkNotNullParameter(inflater, "inflater");
        boolean $i$f$inflate = false;
        return new InflaterSource($this$inflate_u24default, inflater);
    }
}

