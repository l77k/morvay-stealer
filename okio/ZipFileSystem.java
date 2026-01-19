/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.SourceDebugExtension
 */
package okio;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.BufferedSource;
import okio.FileHandle;
import okio.FileMetadata;
import okio.FileSystem;
import okio.InflaterSource;
import okio.Okio;
import okio.Path;
import okio.Sink;
import okio.Source;
import okio.internal.FixedLengthSource;
import okio.internal.ZipEntry;
import okio.internal.ZipFilesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0000\u0018\u0000 (2\u00020\u0001:\u0001(B7\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0001\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00070\u0006\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t\u00a2\u0006\u0004\b\n\u0010\u000bJ\u0010\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u0003H\u0016J\u0010\u0010\u000e\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u0003H\u0002J\u0012\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\r\u001a\u00020\u0003H\u0016J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0003H\u0016J \u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0016H\u0016J\u0016\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00030\u00192\u0006\u0010\u001a\u001a\u00020\u0003H\u0016J\u0018\u0010\u001b\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u00192\u0006\u0010\u001a\u001a\u00020\u0003H\u0016J \u0010\u0018\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u00192\u0006\u0010\u001a\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u0016H\u0002J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u0013\u001a\u00020\u0003H\u0016J\u0018\u0010\u001f\u001a\u00020 2\u0006\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0018\u0010!\u001a\u00020 2\u0006\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u0016H\u0016J\u0018\u0010\"\u001a\u00020#2\u0006\u0010\u001a\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0018\u0010$\u001a\u00020#2\u0006\u0010\u001d\u001a\u00020\u00032\u0006\u0010%\u001a\u00020\u0003H\u0016J\u0018\u0010&\u001a\u00020#2\u0006\u0010\r\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u0016H\u0016J\u0018\u0010'\u001a\u00020#2\u0006\u0010\u001d\u001a\u00020\u00032\u0006\u0010%\u001a\u00020\u0003H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006)"}, d2={"Lokio/ZipFileSystem;", "Lokio/FileSystem;", "zipPath", "Lokio/Path;", "fileSystem", "entries", "", "Lokio/internal/ZipEntry;", "comment", "", "<init>", "(Lokio/Path;Lokio/FileSystem;Ljava/util/Map;Ljava/lang/String;)V", "canonicalize", "path", "canonicalizeInternal", "metadataOrNull", "Lokio/FileMetadata;", "openReadOnly", "Lokio/FileHandle;", "file", "openReadWrite", "mustCreate", "", "mustExist", "list", "", "dir", "listOrNull", "throwOnFailure", "source", "Lokio/Source;", "sink", "Lokio/Sink;", "appendingSink", "createDirectory", "", "atomicMove", "target", "delete", "createSymlink", "Companion", "okio"})
@SourceDebugExtension(value={"SMAP\nZipFileSystem.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ZipFileSystem.kt\nokio/ZipFileSystem\n+ 2 Okio.kt\nokio/Okio__OkioKt\n*L\n1#1,142:1\n52#2,4:143\n52#2,22:147\n60#2,10:169\n56#2,3:179\n71#2,3:182\n52#2,22:185\n*S KotlinDebug\n*F\n+ 1 ZipFileSystem.kt\nokio/ZipFileSystem\n*L\n55#1:143,4\n56#1:147,22\n55#1:169,10\n55#1:179,3\n55#1:182,3\n99#1:185,22\n*E\n"})
public final class ZipFileSystem
extends FileSystem {
    @NotNull
    private static final Companion Companion = new Companion(null);
    @NotNull
    private final Path zipPath;
    @NotNull
    private final FileSystem fileSystem;
    @NotNull
    private final Map<Path, ZipEntry> entries;
    @Nullable
    private final String comment;
    @NotNull
    private static final Path ROOT = Path.Companion.get$default(Path.Companion, "/", false, 1, null);

    public ZipFileSystem(@NotNull Path zipPath, @NotNull FileSystem fileSystem, @NotNull Map<Path, ZipEntry> entries, @Nullable String comment) {
        Intrinsics.checkNotNullParameter(zipPath, "zipPath");
        Intrinsics.checkNotNullParameter(fileSystem, "fileSystem");
        Intrinsics.checkNotNullParameter(entries, "entries");
        this.zipPath = zipPath;
        this.fileSystem = fileSystem;
        this.entries = entries;
        this.comment = comment;
    }

    @Override
    @NotNull
    public Path canonicalize(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "path");
        Path canonical = this.canonicalizeInternal(path);
        if (!this.entries.containsKey(canonical)) {
            throw new FileNotFoundException(String.valueOf(path));
        }
        return canonical;
    }

    private final Path canonicalizeInternal(Path path) {
        return ROOT.resolve(path, true);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    @Nullable
    public FileMetadata metadataOrNull(@NotNull Path path) {
        Long l2;
        ZipEntry zipEntry;
        Intrinsics.checkNotNullParameter(path, "path");
        Path canonicalPath = this.canonicalizeInternal(path);
        ZipEntry zipEntry2 = this.entries.get(canonicalPath);
        if (zipEntry2 == null) {
            return null;
        }
        ZipEntry centralDirectoryEntry = zipEntry2;
        if (centralDirectoryEntry.getOffset() != -1L) {
            ZipEntry zipEntry22;
            Throwable thrown$iv;
            block36: {
                Closeable $this$use$iv = this.fileSystem.openReadOnly(this.zipPath);
                boolean $i$f$use = false;
                thrown$iv = null;
                try {
                    ZipEntry zipEntry3;
                    Throwable thrown$iv2;
                    block34: {
                        FileHandle fileHandle = (FileHandle)$this$use$iv;
                        boolean bl = false;
                        Closeable $this$use$iv2 = Okio.buffer(fileHandle.source(centralDirectoryEntry.getOffset()));
                        boolean $i$f$use2 = false;
                        thrown$iv2 = null;
                        try {
                            BufferedSource source2 = (BufferedSource)$this$use$iv2;
                            boolean bl2 = false;
                            zipEntry3 = ZipFilesKt.readLocalHeader(source2, centralDirectoryEntry);
                        }
                        catch (Throwable t$iv) {
                            try {
                                thrown$iv2 = t$iv;
                                zipEntry3 = null;
                                break block34;
                            }
                            catch (Throwable throwable) {
                                throw throwable;
                            }
                            finally {
                                block35: {
                                    try {
                                        Closeable closeable = $this$use$iv2;
                                        if (closeable != null) {
                                            closeable.close();
                                        }
                                    }
                                    catch (Throwable t$iv2) {
                                        if (thrown$iv2 == null) {
                                            thrown$iv2 = t$iv2;
                                            break block35;
                                        }
                                        ExceptionsKt.addSuppressed(thrown$iv2, t$iv2);
                                    }
                                }
                            }
                        }
                        try {
                            Closeable closeable = $this$use$iv2;
                            if (closeable != null) {
                                closeable.close();
                            }
                        }
                        catch (Throwable t$iv) {
                            thrown$iv2 = t$iv;
                        }
                    }
                    ZipEntry result$iv = zipEntry3;
                    Throwable throwable = thrown$iv2;
                    if (throwable != null) {
                        throw throwable;
                    }
                    zipEntry22 = (ZipEntry)((Object)result$iv);
                }
                catch (Throwable t$iv) {
                    try {
                        thrown$iv = t$iv;
                        zipEntry22 = null;
                        break block36;
                    }
                    catch (Throwable throwable2) {
                        throw throwable2;
                    }
                    finally {
                        block37: {
                            try {
                                Closeable closeable = $this$use$iv;
                                if (closeable != null) {
                                    closeable.close();
                                }
                            }
                            catch (Throwable t$iv3) {
                                if (thrown$iv == null) {
                                    thrown$iv = t$iv3;
                                    break block37;
                                }
                                ExceptionsKt.addSuppressed(thrown$iv, t$iv3);
                            }
                        }
                    }
                }
                try {
                    Closeable closeable = $this$use$iv;
                    if (closeable != null) {
                        closeable.close();
                    }
                }
                catch (Throwable t$iv) {
                    thrown$iv = t$iv;
                }
            }
            ZipEntry result$iv = zipEntry22;
            Throwable throwable = thrown$iv;
            if (throwable != null) {
                throw throwable;
            }
            zipEntry = (ZipEntry)((Object)result$iv);
        } else {
            zipEntry = centralDirectoryEntry;
        }
        ZipEntry fullEntry = zipEntry;
        boolean bl = !fullEntry.isDirectory();
        boolean bl2 = fullEntry.isDirectory();
        if (fullEntry.isDirectory()) {
            l2 = null;
            return new FileMetadata(bl, bl2, null, l2, fullEntry.getCreatedAtMillis$okio(), fullEntry.getLastModifiedAtMillis$okio(), fullEntry.getLastAccessedAtMillis$okio(), null, 128, null);
        }
        l2 = fullEntry.getSize();
        return new FileMetadata(bl, bl2, null, l2, fullEntry.getCreatedAtMillis$okio(), fullEntry.getLastModifiedAtMillis$okio(), fullEntry.getLastAccessedAtMillis$okio(), null, 128, null);
    }

    @Override
    @NotNull
    public FileHandle openReadOnly(@NotNull Path file) {
        Intrinsics.checkNotNullParameter(file, "file");
        throw new UnsupportedOperationException("not implemented yet!");
    }

    @Override
    @NotNull
    public FileHandle openReadWrite(@NotNull Path file, boolean mustCreate, boolean mustExist) {
        Intrinsics.checkNotNullParameter(file, "file");
        throw new IOException("zip entries are not writable");
    }

    @Override
    @NotNull
    public List<Path> list(@NotNull Path dir) {
        Intrinsics.checkNotNullParameter(dir, "dir");
        List<Path> list = this.list(dir, true);
        Intrinsics.checkNotNull(list);
        return list;
    }

    @Override
    @Nullable
    public List<Path> listOrNull(@NotNull Path dir) {
        Intrinsics.checkNotNullParameter(dir, "dir");
        return this.list(dir, false);
    }

    private final List<Path> list(Path dir, boolean throwOnFailure) {
        Path canonicalDir = this.canonicalizeInternal(dir);
        ZipEntry zipEntry = this.entries.get(canonicalDir);
        if (zipEntry == null) {
            if (throwOnFailure) {
                throw new IOException("not a directory: " + dir);
            }
            return null;
        }
        ZipEntry entry = zipEntry;
        return CollectionsKt.toList((Iterable)entry.getChildren());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    @NotNull
    public Source source(@NotNull Path file) throws IOException {
        Source source2;
        BufferedSource bufferedSource;
        Throwable thrown$iv;
        ZipEntry entry;
        block17: {
            Intrinsics.checkNotNullParameter(file, "file");
            Path canonicalPath = this.canonicalizeInternal(file);
            ZipEntry zipEntry = this.entries.get(canonicalPath);
            if (zipEntry == null) {
                throw new FileNotFoundException("no such file: " + file);
            }
            entry = zipEntry;
            Closeable $this$use$iv = this.fileSystem.openReadOnly(this.zipPath);
            boolean $i$f$use = false;
            thrown$iv = null;
            try {
                FileHandle fileHandle = (FileHandle)$this$use$iv;
                boolean bl = false;
                bufferedSource = Okio.buffer(fileHandle.source(entry.getOffset()));
            }
            catch (Throwable t$iv) {
                try {
                    thrown$iv = t$iv;
                    bufferedSource = null;
                    break block17;
                }
                catch (Throwable throwable) {
                    throw throwable;
                }
                finally {
                    block18: {
                        try {
                            Closeable closeable = $this$use$iv;
                            if (closeable != null) {
                                closeable.close();
                            }
                        }
                        catch (Throwable t$iv2) {
                            if (thrown$iv == null) {
                                thrown$iv = t$iv2;
                                break block18;
                            }
                            ExceptionsKt.addSuppressed(thrown$iv, t$iv2);
                        }
                    }
                }
            }
            try {
                Closeable closeable = $this$use$iv;
                if (closeable != null) {
                    closeable.close();
                }
            }
            catch (Throwable t$iv) {
                thrown$iv = t$iv;
            }
        }
        BufferedSource result$iv = bufferedSource;
        Throwable throwable = thrown$iv;
        if (throwable != null) {
            throw throwable;
        }
        BufferedSource source3 = (BufferedSource)((Object)result$iv);
        ZipFilesKt.skipLocalHeader(source3);
        if (entry.getCompressionMethod() == 0) {
            source2 = new FixedLengthSource(source3, entry.getSize(), true);
            return source2;
        }
        InflaterSource inflaterSource = new InflaterSource(new FixedLengthSource(source3, entry.getCompressedSize(), true), new Inflater(true));
        source2 = new FixedLengthSource(inflaterSource, entry.getSize(), false);
        return source2;
    }

    @Override
    @NotNull
    public Sink sink(@NotNull Path file, boolean mustCreate) {
        Intrinsics.checkNotNullParameter(file, "file");
        throw new IOException("zip file systems are read-only");
    }

    @Override
    @NotNull
    public Sink appendingSink(@NotNull Path file, boolean mustExist) {
        Intrinsics.checkNotNullParameter(file, "file");
        throw new IOException("zip file systems are read-only");
    }

    @Override
    public void createDirectory(@NotNull Path dir, boolean mustCreate) {
        Intrinsics.checkNotNullParameter(dir, "dir");
        throw new IOException("zip file systems are read-only");
    }

    @Override
    public void atomicMove(@NotNull Path source2, @NotNull Path target) {
        Intrinsics.checkNotNullParameter(source2, "source");
        Intrinsics.checkNotNullParameter(target, "target");
        throw new IOException("zip file systems are read-only");
    }

    @Override
    public void delete(@NotNull Path path, boolean mustExist) {
        Intrinsics.checkNotNullParameter(path, "path");
        throw new IOException("zip file systems are read-only");
    }

    @Override
    public void createSymlink(@NotNull Path source2, @NotNull Path target) {
        Intrinsics.checkNotNullParameter(source2, "source");
        Intrinsics.checkNotNullParameter(target, "target");
        throw new IOException("zip file systems are read-only");
    }

    @Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\b"}, d2={"Lokio/ZipFileSystem$Companion;", "", "<init>", "()V", "ROOT", "Lokio/Path;", "getROOT", "()Lokio/Path;", "okio"})
    private static final class Companion {
        private Companion() {
        }

        @NotNull
        public final Path getROOT() {
            return ROOT;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

