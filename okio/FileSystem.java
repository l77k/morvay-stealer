/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.SourceDebugExtension
 */
package okio;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.JvmName;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.sequences.Sequence;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.FileHandle;
import okio.FileMetadata;
import okio.JvmSystemFileSystem;
import okio.NioFileSystemWrappingFileSystem;
import okio.NioSystemFileSystem;
import okio.Okio;
import okio.Path;
import okio.Sink;
import okio.Source;
import okio.internal.-FileSystem;
import okio.internal.ResourceFileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u000b\b&\u0018\u0000 72\u00060\u0001j\u0002`\u0002:\u00017B\u0007\u00a2\u0006\u0004\b\u0003\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H&J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\u0007\u001a\u00020\u0006J\u0012\u0010\n\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0007\u001a\u00020\u0006H&J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0007\u001a\u00020\u0006J\u0016\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00060\u000e2\u0006\u0010\u000f\u001a\u00020\u0006H&J\u0018\u0010\u0010\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u0006H&J \u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u000f\u001a\u00020\u00062\b\b\u0002\u0010\u0013\u001a\u00020\fH\u0016J\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u000f\u001a\u00020\u0006J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0006H&J$\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00062\b\b\u0002\u0010\u0018\u001a\u00020\f2\b\b\u0002\u0010\u0019\u001a\u00020\fH&J\u000e\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0006J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0016\u001a\u00020\u0006H&J:\u0010\u001c\u001a\u0002H\u001d\"\u0004\b\u0000\u0010\u001d2\u0006\u0010\u0016\u001a\u00020\u00062\u0017\u0010\u001e\u001a\u0013\u0012\u0004\u0012\u00020 \u0012\u0004\u0012\u0002H\u001d0\u001f\u00a2\u0006\u0002\b!H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\"\u0010#J\u001a\u0010$\u001a\u00020%2\u0006\u0010\u0016\u001a\u00020\u00062\b\b\u0002\u0010\u0018\u001a\u00020\fH&J\u000e\u0010$\u001a\u00020%2\u0006\u0010\u0016\u001a\u00020\u0006JD\u0010&\u001a\u0002H\u001d\"\u0004\b\u0000\u0010\u001d2\u0006\u0010\u0016\u001a\u00020\u00062\b\b\u0002\u0010\u0018\u001a\u00020\f2\u0017\u0010'\u001a\u0013\u0012\u0004\u0012\u00020(\u0012\u0004\u0012\u0002H\u001d0\u001f\u00a2\u0006\u0002\b!H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b)\u0010*J\u001a\u0010+\u001a\u00020%2\u0006\u0010\u0016\u001a\u00020\u00062\b\b\u0002\u0010\u0019\u001a\u00020\fH&J\u000e\u0010+\u001a\u00020%2\u0006\u0010\u0016\u001a\u00020\u0006J\u001a\u0010,\u001a\u00020-2\u0006\u0010\u000f\u001a\u00020\u00062\b\b\u0002\u0010\u0018\u001a\u00020\fH&J\u000e\u0010,\u001a\u00020-2\u0006\u0010\u000f\u001a\u00020\u0006J\u0018\u0010.\u001a\u00020-2\u0006\u0010\u000f\u001a\u00020\u00062\b\b\u0002\u0010\u0018\u001a\u00020\fJ\u000e\u0010.\u001a\u00020-2\u0006\u0010\u000f\u001a\u00020\u0006J\u0018\u0010/\u001a\u00020-2\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u00100\u001a\u00020\u0006H&J\u0018\u00101\u001a\u00020-2\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u00100\u001a\u00020\u0006H\u0016J\u001a\u00102\u001a\u00020-2\u0006\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\u0019\u001a\u00020\fH&J\u000e\u00102\u001a\u00020-2\u0006\u0010\u0007\u001a\u00020\u0006J\u001a\u00103\u001a\u00020-2\u0006\u00104\u001a\u00020\u00062\b\b\u0002\u0010\u0019\u001a\u00020\fH\u0016J\u000e\u00103\u001a\u00020-2\u0006\u00104\u001a\u00020\u0006J\u0018\u00105\u001a\u00020-2\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u00100\u001a\u00020\u0006H&J\b\u00106\u001a\u00020-H\u0016\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u00068"}, d2={"Lokio/FileSystem;", "Ljava/io/Closeable;", "Lokio/Closeable;", "<init>", "()V", "canonicalize", "Lokio/Path;", "path", "metadata", "Lokio/FileMetadata;", "metadataOrNull", "exists", "", "list", "", "dir", "listOrNull", "listRecursively", "Lkotlin/sequences/Sequence;", "followSymlinks", "openReadOnly", "Lokio/FileHandle;", "file", "openReadWrite", "mustCreate", "mustExist", "source", "Lokio/Source;", "read", "T", "readerAction", "Lkotlin/Function1;", "Lokio/BufferedSource;", "Lkotlin/ExtensionFunctionType;", "-read", "(Lokio/Path;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "sink", "Lokio/Sink;", "write", "writerAction", "Lokio/BufferedSink;", "-write", "(Lokio/Path;ZLkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "appendingSink", "createDirectory", "", "createDirectories", "atomicMove", "target", "copy", "delete", "deleteRecursively", "fileOrDirectory", "createSymlink", "close", "Companion", "okio"})
@SourceDebugExtension(value={"SMAP\nFileSystem.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FileSystem.kt\nokio/FileSystem\n+ 2 Okio.kt\nokio/Okio__OkioKt\n*L\n1#1,177:1\n52#2,22:178\n52#2,22:200\n*S KotlinDebug\n*F\n+ 1 FileSystem.kt\nokio/FileSystem\n*L\n67#1:178,22\n81#1:200,22\n*E\n"})
public abstract class FileSystem
implements Closeable {
    @NotNull
    public static final Companion Companion;
    @JvmField
    @NotNull
    public static final FileSystem SYSTEM;
    @JvmField
    @NotNull
    public static final Path SYSTEM_TEMPORARY_DIRECTORY;
    @JvmField
    @NotNull
    public static final FileSystem RESOURCES;

    @NotNull
    public abstract Path canonicalize(@NotNull Path var1) throws IOException;

    @NotNull
    public final FileMetadata metadata(@NotNull Path path) throws IOException {
        Intrinsics.checkNotNullParameter(path, "path");
        return -FileSystem.commonMetadata(this, path);
    }

    @Nullable
    public abstract FileMetadata metadataOrNull(@NotNull Path var1) throws IOException;

    public final boolean exists(@NotNull Path path) throws IOException {
        Intrinsics.checkNotNullParameter(path, "path");
        return -FileSystem.commonExists(this, path);
    }

    @NotNull
    public abstract List<Path> list(@NotNull Path var1) throws IOException;

    @Nullable
    public abstract List<Path> listOrNull(@NotNull Path var1);

    @NotNull
    public Sequence<Path> listRecursively(@NotNull Path dir, boolean followSymlinks) {
        Intrinsics.checkNotNullParameter(dir, "dir");
        return -FileSystem.commonListRecursively(this, dir, followSymlinks);
    }

    public static /* synthetic */ Sequence listRecursively$default(FileSystem fileSystem, Path path, boolean bl, int n2, Object object) {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: listRecursively");
        }
        if ((n2 & 2) != 0) {
            bl = false;
        }
        return fileSystem.listRecursively(path, bl);
    }

    @NotNull
    public final Sequence<Path> listRecursively(@NotNull Path dir) {
        Intrinsics.checkNotNullParameter(dir, "dir");
        return this.listRecursively(dir, false);
    }

    @NotNull
    public abstract FileHandle openReadOnly(@NotNull Path var1) throws IOException;

    @NotNull
    public abstract FileHandle openReadWrite(@NotNull Path var1, boolean var2, boolean var3) throws IOException;

    public static /* synthetic */ FileHandle openReadWrite$default(FileSystem fileSystem, Path path, boolean bl, boolean bl2, int n2, Object object) throws IOException {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: openReadWrite");
        }
        if ((n2 & 2) != 0) {
            bl = false;
        }
        if ((n2 & 4) != 0) {
            bl2 = false;
        }
        return fileSystem.openReadWrite(path, bl, bl2);
    }

    @NotNull
    public final FileHandle openReadWrite(@NotNull Path file) throws IOException {
        Intrinsics.checkNotNullParameter(file, "file");
        return this.openReadWrite(file, false, false);
    }

    @NotNull
    public abstract Source source(@NotNull Path var1) throws IOException;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @JvmName(name="-read")
    public final <T> T -read(@NotNull Path file, @NotNull Function1<? super BufferedSource, ? extends T> readerAction) throws IOException {
        T t2;
        Throwable thrown$iv;
        block14: {
            Intrinsics.checkNotNullParameter(file, "file");
            Intrinsics.checkNotNullParameter(readerAction, "readerAction");
            boolean bl = false;
            Closeable $this$use$iv = Okio.buffer(this.source(file));
            boolean $i$f$use = false;
            thrown$iv = null;
            try {
                BufferedSource it = (BufferedSource)$this$use$iv;
                boolean bl2 = false;
                t2 = readerAction.invoke(it);
            }
            catch (Throwable t$iv) {
                try {
                    thrown$iv = t$iv;
                    t2 = null;
                    break block14;
                }
                catch (Throwable throwable) {
                    throw throwable;
                }
                finally {
                    block15: {
                        InlineMarker.finallyStart(1);
                        try {
                            Closeable closeable = $this$use$iv;
                            if (closeable != null) {
                                closeable.close();
                            }
                        }
                        catch (Throwable t$iv2) {
                            if (thrown$iv == null) {
                                thrown$iv = t$iv2;
                                break block15;
                            }
                            ExceptionsKt.addSuppressed(thrown$iv, t$iv2);
                        }
                    }
                    InlineMarker.finallyEnd(1);
                }
            }
            InlineMarker.finallyStart(1);
            try {
                Closeable closeable = $this$use$iv;
                if (closeable != null) {
                    closeable.close();
                }
            }
            catch (Throwable t$iv) {
                thrown$iv = t$iv;
            }
            InlineMarker.finallyEnd(1);
        }
        T result$iv = t2;
        Throwable throwable = thrown$iv;
        if (throwable == null) return result$iv;
        throw throwable;
    }

    @NotNull
    public abstract Sink sink(@NotNull Path var1, boolean var2) throws IOException;

    public static /* synthetic */ Sink sink$default(FileSystem fileSystem, Path path, boolean bl, int n2, Object object) throws IOException {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: sink");
        }
        if ((n2 & 2) != 0) {
            bl = false;
        }
        return fileSystem.sink(path, bl);
    }

    @NotNull
    public final Sink sink(@NotNull Path file) throws IOException {
        Intrinsics.checkNotNullParameter(file, "file");
        return this.sink(file, false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @JvmName(name="-write")
    public final <T> T -write(@NotNull Path file, boolean mustCreate, @NotNull Function1<? super BufferedSink, ? extends T> writerAction) throws IOException {
        T t2;
        Throwable thrown$iv;
        block14: {
            Intrinsics.checkNotNullParameter(file, "file");
            Intrinsics.checkNotNullParameter(writerAction, "writerAction");
            boolean bl = false;
            Closeable $this$use$iv = Okio.buffer(this.sink(file, mustCreate));
            boolean $i$f$use = false;
            thrown$iv = null;
            try {
                BufferedSink it = (BufferedSink)$this$use$iv;
                boolean bl2 = false;
                t2 = writerAction.invoke(it);
            }
            catch (Throwable t$iv) {
                try {
                    thrown$iv = t$iv;
                    t2 = null;
                    break block14;
                }
                catch (Throwable throwable) {
                    throw throwable;
                }
                finally {
                    block15: {
                        InlineMarker.finallyStart(1);
                        try {
                            Closeable closeable = $this$use$iv;
                            if (closeable != null) {
                                closeable.close();
                            }
                        }
                        catch (Throwable t$iv2) {
                            if (thrown$iv == null) {
                                thrown$iv = t$iv2;
                                break block15;
                            }
                            ExceptionsKt.addSuppressed(thrown$iv, t$iv2);
                        }
                    }
                    InlineMarker.finallyEnd(1);
                }
            }
            InlineMarker.finallyStart(1);
            try {
                Closeable closeable = $this$use$iv;
                if (closeable != null) {
                    closeable.close();
                }
            }
            catch (Throwable t$iv) {
                thrown$iv = t$iv;
            }
            InlineMarker.finallyEnd(1);
        }
        T result$iv = t2;
        Throwable throwable = thrown$iv;
        if (throwable == null) return result$iv;
        throw throwable;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static /* synthetic */ Object -write$default(FileSystem $this, Path file, boolean mustCreate, Function1 writerAction, int n2, Object object) throws IOException {
        Object var9_10;
        Throwable thrown$iv;
        block16: {
            if (object != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: write");
            }
            if ((n2 & 2) != 0) {
                mustCreate = false;
            }
            Intrinsics.checkNotNullParameter(file, "file");
            Intrinsics.checkNotNullParameter(writerAction, "writerAction");
            boolean bl = false;
            Closeable $this$use$iv = Okio.buffer($this.sink(file, mustCreate));
            boolean $i$f$use = false;
            thrown$iv = null;
            try {
                BufferedSink it = (BufferedSink)$this$use$iv;
                boolean bl2 = false;
                var9_10 = writerAction.invoke(it);
            }
            catch (Throwable t$iv) {
                try {
                    thrown$iv = t$iv;
                    var9_10 = null;
                    break block16;
                }
                catch (Throwable throwable) {
                    throw throwable;
                }
                finally {
                    block17: {
                        InlineMarker.finallyStart(1);
                        try {
                            Closeable closeable = $this$use$iv;
                            if (closeable != null) {
                                closeable.close();
                            }
                        }
                        catch (Throwable t$iv2) {
                            if (thrown$iv == null) {
                                thrown$iv = t$iv2;
                                break block17;
                            }
                            ExceptionsKt.addSuppressed(thrown$iv, t$iv2);
                        }
                    }
                    InlineMarker.finallyEnd(1);
                }
            }
            InlineMarker.finallyStart(1);
            try {
                Closeable closeable = $this$use$iv;
                if (closeable != null) {
                    closeable.close();
                }
            }
            catch (Throwable t$iv) {
                thrown$iv = t$iv;
            }
            InlineMarker.finallyEnd(1);
        }
        Object result$iv = var9_10;
        Throwable throwable = thrown$iv;
        if (throwable == null) return result$iv;
        throw throwable;
    }

    @NotNull
    public abstract Sink appendingSink(@NotNull Path var1, boolean var2) throws IOException;

    public static /* synthetic */ Sink appendingSink$default(FileSystem fileSystem, Path path, boolean bl, int n2, Object object) throws IOException {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: appendingSink");
        }
        if ((n2 & 2) != 0) {
            bl = false;
        }
        return fileSystem.appendingSink(path, bl);
    }

    @NotNull
    public final Sink appendingSink(@NotNull Path file) throws IOException {
        Intrinsics.checkNotNullParameter(file, "file");
        return this.appendingSink(file, false);
    }

    public abstract void createDirectory(@NotNull Path var1, boolean var2) throws IOException;

    public static /* synthetic */ void createDirectory$default(FileSystem fileSystem, Path path, boolean bl, int n2, Object object) throws IOException {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: createDirectory");
        }
        if ((n2 & 2) != 0) {
            bl = false;
        }
        fileSystem.createDirectory(path, bl);
    }

    public final void createDirectory(@NotNull Path dir) throws IOException {
        Intrinsics.checkNotNullParameter(dir, "dir");
        this.createDirectory(dir, false);
    }

    public final void createDirectories(@NotNull Path dir, boolean mustCreate) throws IOException {
        Intrinsics.checkNotNullParameter(dir, "dir");
        -FileSystem.commonCreateDirectories(this, dir, mustCreate);
    }

    public static /* synthetic */ void createDirectories$default(FileSystem fileSystem, Path path, boolean bl, int n2, Object object) throws IOException {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: createDirectories");
        }
        if ((n2 & 2) != 0) {
            bl = false;
        }
        fileSystem.createDirectories(path, bl);
    }

    public final void createDirectories(@NotNull Path dir) throws IOException {
        Intrinsics.checkNotNullParameter(dir, "dir");
        this.createDirectories(dir, false);
    }

    public abstract void atomicMove(@NotNull Path var1, @NotNull Path var2) throws IOException;

    public void copy(@NotNull Path source2, @NotNull Path target) throws IOException {
        Intrinsics.checkNotNullParameter(source2, "source");
        Intrinsics.checkNotNullParameter(target, "target");
        -FileSystem.commonCopy(this, source2, target);
    }

    public abstract void delete(@NotNull Path var1, boolean var2) throws IOException;

    public static /* synthetic */ void delete$default(FileSystem fileSystem, Path path, boolean bl, int n2, Object object) throws IOException {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: delete");
        }
        if ((n2 & 2) != 0) {
            bl = false;
        }
        fileSystem.delete(path, bl);
    }

    public final void delete(@NotNull Path path) throws IOException {
        Intrinsics.checkNotNullParameter(path, "path");
        this.delete(path, false);
    }

    public void deleteRecursively(@NotNull Path fileOrDirectory, boolean mustExist) throws IOException {
        Intrinsics.checkNotNullParameter(fileOrDirectory, "fileOrDirectory");
        -FileSystem.commonDeleteRecursively(this, fileOrDirectory, mustExist);
    }

    public static /* synthetic */ void deleteRecursively$default(FileSystem fileSystem, Path path, boolean bl, int n2, Object object) throws IOException {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: deleteRecursively");
        }
        if ((n2 & 2) != 0) {
            bl = false;
        }
        fileSystem.deleteRecursively(path, bl);
    }

    public final void deleteRecursively(@NotNull Path fileOrDirectory) throws IOException {
        Intrinsics.checkNotNullParameter(fileOrDirectory, "fileOrDirectory");
        this.deleteRecursively(fileOrDirectory, false);
    }

    public abstract void createSymlink(@NotNull Path var1, @NotNull Path var2) throws IOException;

    @Override
    public void close() throws IOException {
    }

    @JvmStatic
    @JvmName(name="get")
    @NotNull
    public static final FileSystem get(@NotNull java.nio.file.FileSystem $this$get) {
        return Companion.get($this$get);
    }

    static {
        JvmSystemFileSystem jvmSystemFileSystem;
        Companion $this$SYSTEM_u24lambda_u242 = Companion = new Companion(null);
        boolean bl = false;
        try {
            Class.forName("java.nio.file.Files");
            jvmSystemFileSystem = new NioSystemFileSystem();
        }
        catch (ClassNotFoundException e2) {
            jvmSystemFileSystem = new JvmSystemFileSystem();
        }
        SYSTEM = jvmSystemFileSystem;
        String string = System.getProperty("java.io.tmpdir");
        Intrinsics.checkNotNullExpressionValue(string, "getProperty(...)");
        SYSTEM_TEMPORARY_DIRECTORY = Path.Companion.get$default(Path.Companion, string, false, 1, null);
        ClassLoader classLoader = ResourceFileSystem.class.getClassLoader();
        Intrinsics.checkNotNullExpressionValue(classLoader, "getClassLoader(...)");
        RESOURCES = new ResourceFileSystem(classLoader, false, null, 4, null);
    }

    @Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0011\u0010\t\u001a\u00020\u0005*\u00020\nH\u0007\u00a2\u0006\u0002\b\u000bR\u0010\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u00020\u00078\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2={"Lokio/FileSystem$Companion;", "", "<init>", "()V", "SYSTEM", "Lokio/FileSystem;", "SYSTEM_TEMPORARY_DIRECTORY", "Lokio/Path;", "RESOURCES", "asOkioFileSystem", "Ljava/nio/file/FileSystem;", "get", "okio"})
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        @JvmName(name="get")
        @NotNull
        public final FileSystem get(@NotNull java.nio.file.FileSystem $this$asOkioFileSystem) {
            Intrinsics.checkNotNullParameter($this$asOkioFileSystem, "<this>");
            return new NioFileSystemWrappingFileSystem($this$asOkioFileSystem);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

