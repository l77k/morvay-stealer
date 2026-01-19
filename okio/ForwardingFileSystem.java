/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.SourceDebugExtension
 */
package okio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import okio.FileHandle;
import okio.FileMetadata;
import okio.FileSystem;
import okio.Path;
import okio.Sink;
import okio.Source;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\b&\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u00a2\u0006\u0004\b\u0003\u0010\u0004J \u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0016J\u0018\u0010\f\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\r\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u0016J\u0012\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\b\u001a\u00020\u0007H\u0016J\u0016\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00070\u00112\u0006\u0010\u0012\u001a\u00020\u0007H\u0016J\u0018\u0010\u0013\u001a\n\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u00112\u0006\u0010\u0012\u001a\u00020\u0007H\u0016J\u001e\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00070\u00152\u0006\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u0016\u001a\u00020\u0017H\u0016J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0007H\u0016J \u0010\u001b\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u00072\u0006\u0010\u001c\u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u0017H\u0016J\u0010\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\u001a\u001a\u00020\u0007H\u0016J\u0018\u0010 \u001a\u00020!2\u0006\u0010\u001a\u001a\u00020\u00072\u0006\u0010\u001c\u001a\u00020\u0017H\u0016J\u0018\u0010\"\u001a\u00020!2\u0006\u0010\u001a\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u0017H\u0016J\u0018\u0010#\u001a\u00020$2\u0006\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u001c\u001a\u00020\u0017H\u0016J\u0018\u0010%\u001a\u00020$2\u0006\u0010\u001e\u001a\u00020\u00072\u0006\u0010&\u001a\u00020\u0007H\u0016J\u0018\u0010'\u001a\u00020$2\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u0017H\u0016J\u0018\u0010(\u001a\u00020$2\u0006\u0010\u001e\u001a\u00020\u00072\u0006\u0010&\u001a\u00020\u0007H\u0016J\b\u0010)\u001a\u00020$H\u0016J\b\u0010*\u001a\u00020\nH\u0016R\u0013\u0010\u0002\u001a\u00020\u00018G\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0005\u00a8\u0006+"}, d2={"Lokio/ForwardingFileSystem;", "Lokio/FileSystem;", "delegate", "<init>", "(Lokio/FileSystem;)V", "()Lokio/FileSystem;", "onPathParameter", "Lokio/Path;", "path", "functionName", "", "parameterName", "onPathResult", "canonicalize", "metadataOrNull", "Lokio/FileMetadata;", "list", "", "dir", "listOrNull", "listRecursively", "Lkotlin/sequences/Sequence;", "followSymlinks", "", "openReadOnly", "Lokio/FileHandle;", "file", "openReadWrite", "mustCreate", "mustExist", "source", "Lokio/Source;", "sink", "Lokio/Sink;", "appendingSink", "createDirectory", "", "atomicMove", "target", "delete", "createSymlink", "close", "toString", "okio"})
@SourceDebugExtension(value={"SMAP\nForwardingFileSystem.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ForwardingFileSystem.kt\nokio/ForwardingFileSystem\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,252:1\n1628#2,3:253\n1628#2,3:256\n*S KotlinDebug\n*F\n+ 1 ForwardingFileSystem.kt\nokio/ForwardingFileSystem\n*L\n170#1:253,3\n178#1:256,3\n*E\n"})
public abstract class ForwardingFileSystem
extends FileSystem {
    @NotNull
    private final FileSystem delegate;

    public ForwardingFileSystem(@NotNull FileSystem delegate) {
        Intrinsics.checkNotNullParameter(delegate, "delegate");
        this.delegate = delegate;
    }

    @JvmName(name="delegate")
    @NotNull
    public final FileSystem delegate() {
        return this.delegate;
    }

    @NotNull
    public Path onPathParameter(@NotNull Path path, @NotNull String functionName, @NotNull String parameterName) {
        Intrinsics.checkNotNullParameter(path, "path");
        Intrinsics.checkNotNullParameter(functionName, "functionName");
        Intrinsics.checkNotNullParameter(parameterName, "parameterName");
        return path;
    }

    @NotNull
    public Path onPathResult(@NotNull Path path, @NotNull String functionName) {
        Intrinsics.checkNotNullParameter(path, "path");
        Intrinsics.checkNotNullParameter(functionName, "functionName");
        return path;
    }

    @Override
    @NotNull
    public Path canonicalize(@NotNull Path path) throws IOException {
        Intrinsics.checkNotNullParameter(path, "path");
        Path path2 = this.onPathParameter(path, "canonicalize", "path");
        Path result = this.delegate.canonicalize(path2);
        return this.onPathResult(result, "canonicalize");
    }

    @Override
    @Nullable
    public FileMetadata metadataOrNull(@NotNull Path path) throws IOException {
        Intrinsics.checkNotNullParameter(path, "path");
        Path path2 = this.onPathParameter(path, "metadataOrNull", "path");
        FileMetadata fileMetadata = this.delegate.metadataOrNull(path2);
        if (fileMetadata == null) {
            return null;
        }
        FileMetadata metadataOrNull = fileMetadata;
        if (metadataOrNull.getSymlinkTarget() == null) {
            return metadataOrNull;
        }
        Path symlinkTarget = this.onPathResult(metadataOrNull.getSymlinkTarget(), "metadataOrNull");
        return FileMetadata.copy$default(metadataOrNull, false, false, symlinkTarget, null, null, null, null, null, 251, null);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public List<Path> list(@NotNull Path dir) throws IOException {
        void destination$iv;
        void $this$mapTo$iv;
        Intrinsics.checkNotNullParameter(dir, "dir");
        Path dir2 = this.onPathParameter(dir, "list", "dir");
        List<Path> result = this.delegate.list(dir2);
        Iterable iterable = result;
        Collection collection = new ArrayList();
        boolean $i$f$mapTo = false;
        for (Object item$iv : $this$mapTo$iv) {
            void it;
            Path path = (Path)item$iv;
            void var12_11 = destination$iv;
            boolean bl = false;
            var12_11.add(this.onPathResult((Path)it, "list"));
        }
        List paths = (List)destination$iv;
        CollectionsKt.sort(paths);
        return paths;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @Nullable
    public List<Path> listOrNull(@NotNull Path dir) {
        void destination$iv;
        void $this$mapTo$iv;
        Intrinsics.checkNotNullParameter(dir, "dir");
        Path dir2 = this.onPathParameter(dir, "listOrNull", "dir");
        List<Path> list = this.delegate.listOrNull(dir2);
        if (list == null) {
            return null;
        }
        List<Path> result = list;
        Iterable iterable = result;
        Collection collection = new ArrayList();
        boolean $i$f$mapTo = false;
        for (Object item$iv : $this$mapTo$iv) {
            void it;
            Path path = (Path)item$iv;
            void var12_11 = destination$iv;
            boolean bl = false;
            var12_11.add(this.onPathResult((Path)it, "listOrNull"));
        }
        List paths = (List)destination$iv;
        CollectionsKt.sort(paths);
        return paths;
    }

    @Override
    @NotNull
    public Sequence<Path> listRecursively(@NotNull Path dir, boolean followSymlinks) {
        Intrinsics.checkNotNullParameter(dir, "dir");
        Path dir2 = this.onPathParameter(dir, "listRecursively", "dir");
        Sequence<Path> result = this.delegate.listRecursively(dir2, followSymlinks);
        return SequencesKt.map(result, arg_0 -> ForwardingFileSystem.listRecursively$lambda$2(this, arg_0));
    }

    @Override
    @NotNull
    public FileHandle openReadOnly(@NotNull Path file) throws IOException {
        Intrinsics.checkNotNullParameter(file, "file");
        Path file2 = this.onPathParameter(file, "openReadOnly", "file");
        return this.delegate.openReadOnly(file2);
    }

    @Override
    @NotNull
    public FileHandle openReadWrite(@NotNull Path file, boolean mustCreate, boolean mustExist) throws IOException {
        Intrinsics.checkNotNullParameter(file, "file");
        Path file2 = this.onPathParameter(file, "openReadWrite", "file");
        return this.delegate.openReadWrite(file2, mustCreate, mustExist);
    }

    @Override
    @NotNull
    public Source source(@NotNull Path file) throws IOException {
        Intrinsics.checkNotNullParameter(file, "file");
        Path file2 = this.onPathParameter(file, "source", "file");
        return this.delegate.source(file2);
    }

    @Override
    @NotNull
    public Sink sink(@NotNull Path file, boolean mustCreate) throws IOException {
        Intrinsics.checkNotNullParameter(file, "file");
        Path file2 = this.onPathParameter(file, "sink", "file");
        return this.delegate.sink(file2, mustCreate);
    }

    @Override
    @NotNull
    public Sink appendingSink(@NotNull Path file, boolean mustExist) throws IOException {
        Intrinsics.checkNotNullParameter(file, "file");
        Path file2 = this.onPathParameter(file, "appendingSink", "file");
        return this.delegate.appendingSink(file2, mustExist);
    }

    @Override
    public void createDirectory(@NotNull Path dir, boolean mustCreate) throws IOException {
        Intrinsics.checkNotNullParameter(dir, "dir");
        Path dir2 = this.onPathParameter(dir, "createDirectory", "dir");
        this.delegate.createDirectory(dir2, mustCreate);
    }

    @Override
    public void atomicMove(@NotNull Path source2, @NotNull Path target) throws IOException {
        Intrinsics.checkNotNullParameter(source2, "source");
        Intrinsics.checkNotNullParameter(target, "target");
        Path source3 = this.onPathParameter(source2, "atomicMove", "source");
        Path target2 = this.onPathParameter(target, "atomicMove", "target");
        this.delegate.atomicMove(source3, target2);
    }

    @Override
    public void delete(@NotNull Path path, boolean mustExist) throws IOException {
        Intrinsics.checkNotNullParameter(path, "path");
        Path path2 = this.onPathParameter(path, "delete", "path");
        this.delegate.delete(path2, mustExist);
    }

    @Override
    public void createSymlink(@NotNull Path source2, @NotNull Path target) throws IOException {
        Intrinsics.checkNotNullParameter(source2, "source");
        Intrinsics.checkNotNullParameter(target, "target");
        Path source3 = this.onPathParameter(source2, "createSymlink", "source");
        Path target2 = this.onPathParameter(target, "createSymlink", "target");
        this.delegate.createSymlink(source3, target2);
    }

    @Override
    public void close() throws IOException {
        this.delegate.close();
    }

    @NotNull
    public String toString() {
        return Reflection.getOrCreateKotlinClass(this.getClass()).getSimpleName() + '(' + this.delegate + ')';
    }

    private static final Path listRecursively$lambda$2(ForwardingFileSystem this$0, Path it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return this$0.onPathResult(it, "listRecursively");
    }
}

