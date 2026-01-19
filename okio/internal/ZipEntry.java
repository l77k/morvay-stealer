/*
 * Decompiled with CFR 0.152.
 */
package okio.internal;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okio.Path;
import okio.internal.ZipFilesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b$\n\u0002\u0010!\n\u0002\b\f\b\u0000\u0018\u00002\u00020\u0001B\u00b1\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\t\u0012\b\b\u0002\u0010\u000b\u001a\u00020\t\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\t\u0012\b\b\u0002\u0010\u000f\u001a\u00020\r\u0012\b\b\u0002\u0010\u0010\u001a\u00020\r\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\r\u0012\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\r\u0012\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\r\u00a2\u0006\u0004\b\u0017\u0010\u0018J-\u00105\u001a\u00020\u00002\b\u0010\u0014\u001a\u0004\u0018\u00010\r2\b\u0010\u0015\u001a\u0004\u0018\u00010\r2\b\u0010\u0016\u001a\u0004\u0018\u00010\rH\u0000\u00a2\u0006\u0004\b6\u00107R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u001bR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0011\u0010\n\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u001fR\u0011\u0010\u000b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001fR\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0011\u0010\u000e\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001fR\u0011\u0010\u000f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010#R\u0011\u0010\u0010\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010#R\u0015\u0010\u0011\u001a\u0004\u0018\u00010\t\u00a2\u0006\n\n\u0002\u0010)\u001a\u0004\b'\u0010(R\u0015\u0010\u0012\u001a\u0004\u0018\u00010\t\u00a2\u0006\n\n\u0002\u0010)\u001a\u0004\b*\u0010(R\u0015\u0010\u0013\u001a\u0004\u0018\u00010\t\u00a2\u0006\n\n\u0002\u0010)\u001a\u0004\b+\u0010(R\u0015\u0010\u0014\u001a\u0004\u0018\u00010\r\u00a2\u0006\n\n\u0002\u0010.\u001a\u0004\b,\u0010-R\u0015\u0010\u0015\u001a\u0004\u0018\u00010\r\u00a2\u0006\n\n\u0002\u0010.\u001a\u0004\b/\u0010-R\u0015\u0010\u0016\u001a\u0004\u0018\u00010\r\u00a2\u0006\n\n\u0002\u0010.\u001a\u0004\b0\u0010-R\u0017\u00101\u001a\b\u0012\u0004\u0012\u00020\u000302\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u00104R\u0016\u00108\u001a\u0004\u0018\u00010\t8@X\u0080\u0004\u00a2\u0006\u0006\u001a\u0004\b9\u0010(R\u0016\u0010:\u001a\u0004\u0018\u00010\t8@X\u0080\u0004\u00a2\u0006\u0006\u001a\u0004\b;\u0010(R\u0016\u0010<\u001a\u0004\u0018\u00010\t8@X\u0080\u0004\u00a2\u0006\u0006\u001a\u0004\b=\u0010(\u00a8\u0006>"}, d2={"Lokio/internal/ZipEntry;", "", "canonicalPath", "Lokio/Path;", "isDirectory", "", "comment", "", "crc", "", "compressedSize", "size", "compressionMethod", "", "offset", "dosLastModifiedAtDate", "dosLastModifiedAtTime", "ntfsLastModifiedAtFiletime", "ntfsLastAccessedAtFiletime", "ntfsCreatedAtFiletime", "extendedLastModifiedAtSeconds", "extendedLastAccessedAtSeconds", "extendedCreatedAtSeconds", "<init>", "(Lokio/Path;ZLjava/lang/String;JJJIJIILjava/lang/Long;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;)V", "getCanonicalPath", "()Lokio/Path;", "()Z", "getComment", "()Ljava/lang/String;", "getCrc", "()J", "getCompressedSize", "getSize", "getCompressionMethod", "()I", "getOffset", "getDosLastModifiedAtDate", "getDosLastModifiedAtTime", "getNtfsLastModifiedAtFiletime", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getNtfsLastAccessedAtFiletime", "getNtfsCreatedAtFiletime", "getExtendedLastModifiedAtSeconds", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getExtendedLastAccessedAtSeconds", "getExtendedCreatedAtSeconds", "children", "", "getChildren", "()Ljava/util/List;", "copy", "copy$okio", "(Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;)Lokio/internal/ZipEntry;", "lastAccessedAtMillis", "getLastAccessedAtMillis$okio", "lastModifiedAtMillis", "getLastModifiedAtMillis$okio", "createdAtMillis", "getCreatedAtMillis$okio", "okio"})
public final class ZipEntry {
    @NotNull
    private final Path canonicalPath;
    private final boolean isDirectory;
    @NotNull
    private final String comment;
    private final long crc;
    private final long compressedSize;
    private final long size;
    private final int compressionMethod;
    private final long offset;
    private final int dosLastModifiedAtDate;
    private final int dosLastModifiedAtTime;
    @Nullable
    private final Long ntfsLastModifiedAtFiletime;
    @Nullable
    private final Long ntfsLastAccessedAtFiletime;
    @Nullable
    private final Long ntfsCreatedAtFiletime;
    @Nullable
    private final Integer extendedLastModifiedAtSeconds;
    @Nullable
    private final Integer extendedLastAccessedAtSeconds;
    @Nullable
    private final Integer extendedCreatedAtSeconds;
    @NotNull
    private final List<Path> children;

    public ZipEntry(@NotNull Path canonicalPath, boolean isDirectory, @NotNull String comment, long crc, long compressedSize, long size, int compressionMethod, long offset, int dosLastModifiedAtDate, int dosLastModifiedAtTime, @Nullable Long ntfsLastModifiedAtFiletime, @Nullable Long ntfsLastAccessedAtFiletime, @Nullable Long ntfsCreatedAtFiletime, @Nullable Integer extendedLastModifiedAtSeconds, @Nullable Integer extendedLastAccessedAtSeconds, @Nullable Integer extendedCreatedAtSeconds) {
        Intrinsics.checkNotNullParameter(canonicalPath, "canonicalPath");
        Intrinsics.checkNotNullParameter(comment, "comment");
        this.canonicalPath = canonicalPath;
        this.isDirectory = isDirectory;
        this.comment = comment;
        this.crc = crc;
        this.compressedSize = compressedSize;
        this.size = size;
        this.compressionMethod = compressionMethod;
        this.offset = offset;
        this.dosLastModifiedAtDate = dosLastModifiedAtDate;
        this.dosLastModifiedAtTime = dosLastModifiedAtTime;
        this.ntfsLastModifiedAtFiletime = ntfsLastModifiedAtFiletime;
        this.ntfsLastAccessedAtFiletime = ntfsLastAccessedAtFiletime;
        this.ntfsCreatedAtFiletime = ntfsCreatedAtFiletime;
        this.extendedLastModifiedAtSeconds = extendedLastModifiedAtSeconds;
        this.extendedLastAccessedAtSeconds = extendedLastAccessedAtSeconds;
        this.extendedCreatedAtSeconds = extendedCreatedAtSeconds;
        this.children = new ArrayList();
    }

    public /* synthetic */ ZipEntry(Path path, boolean bl, String string, long l2, long l3, long l4, int n2, long l5, int n3, int n4, Long l6, Long l7, Long l8, Integer n5, Integer n6, Integer n7, int n8, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n8 & 2) != 0) {
            bl = false;
        }
        if ((n8 & 4) != 0) {
            string = "";
        }
        if ((n8 & 8) != 0) {
            l2 = -1L;
        }
        if ((n8 & 0x10) != 0) {
            l3 = -1L;
        }
        if ((n8 & 0x20) != 0) {
            l4 = -1L;
        }
        if ((n8 & 0x40) != 0) {
            n2 = -1;
        }
        if ((n8 & 0x80) != 0) {
            l5 = -1L;
        }
        if ((n8 & 0x100) != 0) {
            n3 = -1;
        }
        if ((n8 & 0x200) != 0) {
            n4 = -1;
        }
        if ((n8 & 0x400) != 0) {
            l6 = null;
        }
        if ((n8 & 0x800) != 0) {
            l7 = null;
        }
        if ((n8 & 0x1000) != 0) {
            l8 = null;
        }
        if ((n8 & 0x2000) != 0) {
            n5 = null;
        }
        if ((n8 & 0x4000) != 0) {
            n6 = null;
        }
        if ((n8 & 0x8000) != 0) {
            n7 = null;
        }
        this(path, bl, string, l2, l3, l4, n2, l5, n3, n4, l6, l7, l8, n5, n6, n7);
    }

    @NotNull
    public final Path getCanonicalPath() {
        return this.canonicalPath;
    }

    public final boolean isDirectory() {
        return this.isDirectory;
    }

    @NotNull
    public final String getComment() {
        return this.comment;
    }

    public final long getCrc() {
        return this.crc;
    }

    public final long getCompressedSize() {
        return this.compressedSize;
    }

    public final long getSize() {
        return this.size;
    }

    public final int getCompressionMethod() {
        return this.compressionMethod;
    }

    public final long getOffset() {
        return this.offset;
    }

    public final int getDosLastModifiedAtDate() {
        return this.dosLastModifiedAtDate;
    }

    public final int getDosLastModifiedAtTime() {
        return this.dosLastModifiedAtTime;
    }

    @Nullable
    public final Long getNtfsLastModifiedAtFiletime() {
        return this.ntfsLastModifiedAtFiletime;
    }

    @Nullable
    public final Long getNtfsLastAccessedAtFiletime() {
        return this.ntfsLastAccessedAtFiletime;
    }

    @Nullable
    public final Long getNtfsCreatedAtFiletime() {
        return this.ntfsCreatedAtFiletime;
    }

    @Nullable
    public final Integer getExtendedLastModifiedAtSeconds() {
        return this.extendedLastModifiedAtSeconds;
    }

    @Nullable
    public final Integer getExtendedLastAccessedAtSeconds() {
        return this.extendedLastAccessedAtSeconds;
    }

    @Nullable
    public final Integer getExtendedCreatedAtSeconds() {
        return this.extendedCreatedAtSeconds;
    }

    @NotNull
    public final List<Path> getChildren() {
        return this.children;
    }

    @NotNull
    public final ZipEntry copy$okio(@Nullable Integer extendedLastModifiedAtSeconds, @Nullable Integer extendedLastAccessedAtSeconds, @Nullable Integer extendedCreatedAtSeconds) {
        return new ZipEntry(this.canonicalPath, this.isDirectory, this.comment, this.crc, this.compressedSize, this.size, this.compressionMethod, this.offset, this.dosLastModifiedAtDate, this.dosLastModifiedAtTime, this.ntfsLastModifiedAtFiletime, this.ntfsLastAccessedAtFiletime, this.ntfsCreatedAtFiletime, extendedLastModifiedAtSeconds, extendedLastAccessedAtSeconds, extendedCreatedAtSeconds);
    }

    @Nullable
    public final Long getLastAccessedAtMillis$okio() {
        return this.ntfsLastAccessedAtFiletime != null ? Long.valueOf(ZipFilesKt.filetimeToEpochMillis(this.ntfsLastAccessedAtFiletime)) : (this.extendedLastAccessedAtSeconds != null ? Long.valueOf((long)this.extendedLastAccessedAtSeconds.intValue() * 1000L) : null);
    }

    @Nullable
    public final Long getLastModifiedAtMillis$okio() {
        return this.ntfsLastModifiedAtFiletime != null ? Long.valueOf(ZipFilesKt.filetimeToEpochMillis(this.ntfsLastModifiedAtFiletime)) : (this.extendedLastModifiedAtSeconds != null ? Long.valueOf((long)this.extendedLastModifiedAtSeconds.intValue() * 1000L) : (this.dosLastModifiedAtTime != -1 ? ZipFilesKt.dosDateTimeToEpochMillis(this.dosLastModifiedAtDate, this.dosLastModifiedAtTime) : null));
    }

    @Nullable
    public final Long getCreatedAtMillis$okio() {
        return this.ntfsCreatedAtFiletime != null ? Long.valueOf(ZipFilesKt.filetimeToEpochMillis(this.ntfsCreatedAtFiletime)) : (this.extendedCreatedAtSeconds != null ? Long.valueOf((long)this.extendedCreatedAtSeconds.intValue() * 1000L) : null);
    }
}

