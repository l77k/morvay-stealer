/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.IOException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okio.FileSystem;
import okio.Path;
import okio.internal.ZipFilesKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={2, 1, 0}, k=5, xi=48, d1={"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003\u00a8\u0006\u0004"}, d2={"openZip", "Lokio/FileSystem;", "zipPath", "Lokio/Path;", "okio"}, xs="okio/Okio")
final class Okio__ZlibOkioKt {
    @NotNull
    public static final FileSystem openZip(@NotNull FileSystem $this$openZip, @NotNull Path zipPath) throws IOException {
        Intrinsics.checkNotNullParameter($this$openZip, "<this>");
        Intrinsics.checkNotNullParameter(zipPath, "zipPath");
        return ZipFilesKt.openZip$default(zipPath, $this$openZip, null, 4, null);
    }
}

