/*
 * Decompiled with CFR 0.152.
 */
package okio;

import kotlin.Metadata;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import okio.FileSystem;

@Metadata(mv={2, 1, 0}, k=2, xi=48, d1={"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\"\u001c\u0010\u0000\u001a\u00020\u0001*\u00020\u00028\u00c6\u0002\u00a2\u0006\f\u0012\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2={"SYSTEM", "Lokio/FileSystem;", "Lokio/FileSystem$Companion;", "getSYSTEM$annotations", "(Lokio/FileSystem$Companion;)V", "getSYSTEM", "(Lokio/FileSystem$Companion;)Lokio/FileSystem;", "okio"})
@JvmName(name="SystemFileSystem")
public final class SystemFileSystem {
    public static final /* synthetic */ FileSystem getSYSTEM(FileSystem.Companion $this$SYSTEM) {
        Intrinsics.checkNotNullParameter($this$SYSTEM, "<this>");
        boolean $i$f$getSYSTEM = false;
        return FileSystem.SYSTEM;
    }

    public static /* synthetic */ void getSYSTEM$annotations(FileSystem.Companion companion) {
    }
}

