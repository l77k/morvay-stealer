/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.SourceDebugExtension
 */
package okio.internal;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import kotlin.ExceptionsKt;
import kotlin.KotlinNothingValueException;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;
import okio.BufferedSource;
import okio.FileHandle;
import okio.FileSystem;
import okio.Okio;
import okio.Path;
import okio.ZipFileSystem;
import okio.internal.EocdRecord;
import okio.internal.ZipEntry;
import okio.internal._ZlibJvmKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={2, 1, 0}, k=2, xi=48, d1={"\u0000d\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000e\n\u0002\b\u0003\u001a.\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0014\b\u0002\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u00180\u0016H\u0000\u001a\"\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00170\u001a2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00170\u001cH\u0002\u001a\f\u0010\u001d\u001a\u00020\u0017*\u00020\u001eH\u0000\u001a\f\u0010\u001f\u001a\u00020 *\u00020\u001eH\u0002\u001a\u0014\u0010!\u001a\u00020 *\u00020\u001e2\u0006\u0010\"\u001a\u00020 H\u0002\u001a.\u0010#\u001a\u00020$*\u00020\u001e2\u0006\u0010%\u001a\u00020\u00012\u0018\u0010&\u001a\u0014\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020$0'H\u0002\u001a\f\u0010(\u001a\u00020$*\u00020\u001eH\u0000\u001a\u0014\u0010)\u001a\u00020\u0017*\u00020\u001e2\u0006\u0010*\u001a\u00020\u0017H\u0000\u001a\u0018\u0010+\u001a\u0004\u0018\u00010\u0017*\u00020\u001e2\b\u0010*\u001a\u0004\u0018\u00010\u0017H\u0002\u001a\u0010\u0010,\u001a\u00020\u000b2\u0006\u0010-\u001a\u00020\u000bH\u0000\u001a\u001f\u0010.\u001a\u0004\u0018\u00010\u000b2\u0006\u0010/\u001a\u00020\u00012\u0006\u00100\u001a\u00020\u0001H\u0000\u00a2\u0006\u0002\u00101\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0001X\u0080T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\u0001X\u0080T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\t\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\n\u001a\u00020\u000bX\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\f\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\r\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u000e\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u0018\u00102\u001a\u000203*\u00020\u00018BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b4\u00105\u00a8\u00066"}, d2={"LOCAL_FILE_HEADER_SIGNATURE", "", "CENTRAL_FILE_HEADER_SIGNATURE", "END_OF_CENTRAL_DIRECTORY_SIGNATURE", "ZIP64_LOCATOR_SIGNATURE", "ZIP64_EOCD_RECORD_SIGNATURE", "COMPRESSION_METHOD_DEFLATED", "COMPRESSION_METHOD_STORED", "BIT_FLAG_ENCRYPTED", "BIT_FLAG_UNSUPPORTED_MASK", "MAX_ZIP_ENTRY_AND_ARCHIVE_SIZE", "", "HEADER_ID_ZIP64_EXTENDED_INFO", "HEADER_ID_NTFS_EXTRA", "HEADER_ID_EXTENDED_TIMESTAMP", "openZip", "Lokio/ZipFileSystem;", "zipPath", "Lokio/Path;", "fileSystem", "Lokio/FileSystem;", "predicate", "Lkotlin/Function1;", "Lokio/internal/ZipEntry;", "", "buildIndex", "", "entries", "", "readCentralDirectoryZipEntry", "Lokio/BufferedSource;", "readEocdRecord", "Lokio/internal/EocdRecord;", "readZip64EocdRecord", "regularRecord", "readExtra", "", "extraSize", "block", "Lkotlin/Function2;", "skipLocalHeader", "readLocalHeader", "centralDirectoryZipEntry", "readOrSkipLocalHeader", "filetimeToEpochMillis", "filetime", "dosDateTimeToEpochMillis", "date", "time", "(II)Ljava/lang/Long;", "hex", "", "getHex", "(I)Ljava/lang/String;", "okio"})
@SourceDebugExtension(value={"SMAP\nZipFiles.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ZipFiles.kt\nokio/internal/ZipFilesKt\n+ 2 Okio.kt\nokio/Okio__OkioKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,503:1\n52#2,4:504\n52#2,4:508\n52#2,22:512\n60#2,10:534\n56#2,3:544\n71#2,3:547\n52#2,22:550\n60#2,10:572\n56#2,3:582\n71#2,3:585\n1053#3:588\n*S KotlinDebug\n*F\n+ 1 ZipFiles.kt\nokio/internal/ZipFilesKt\n*L\n66#1:504,4\n101#1:508,4\n109#1:512,22\n101#1:534,10\n101#1:544,3\n101#1:547,3\n125#1:550,22\n66#1:572,10\n66#1:582,3\n66#1:585,3\n155#1:588\n*E\n"})
public final class ZipFilesKt {
    private static final int LOCAL_FILE_HEADER_SIGNATURE = 67324752;
    private static final int CENTRAL_FILE_HEADER_SIGNATURE = 33639248;
    private static final int END_OF_CENTRAL_DIRECTORY_SIGNATURE = 101010256;
    private static final int ZIP64_LOCATOR_SIGNATURE = 117853008;
    private static final int ZIP64_EOCD_RECORD_SIGNATURE = 101075792;
    public static final int COMPRESSION_METHOD_DEFLATED = 8;
    public static final int COMPRESSION_METHOD_STORED = 0;
    private static final int BIT_FLAG_ENCRYPTED = 1;
    private static final int BIT_FLAG_UNSUPPORTED_MASK = 1;
    private static final long MAX_ZIP_ENTRY_AND_ARCHIVE_SIZE = 0xFFFFFFFFL;
    private static final int HEADER_ID_ZIP64_EXTENDED_INFO = 1;
    private static final int HEADER_ID_NTFS_EXTRA = 10;
    private static final int HEADER_ID_EXTENDED_TIMESTAMP = 21589;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     */
    @NotNull
    public static final ZipFileSystem openZip(@NotNull Path zipPath, @NotNull FileSystem fileSystem, @NotNull Function1<? super ZipEntry, Boolean> predicate) throws IOException {
        Object var41_59;
        Throwable thrown$iv;
        block85: {
            Unit unit;
            Throwable thrown$iv2;
            List entries;
            String comment;
            Closeable $this$use$iv;
            block83: {
                EocdRecord record;
                FileHandle fileHandle;
                block87: {
                    Object zip64LocatorSource;
                    Throwable thrown$iv3;
                    block81: {
                        Closeable $this$use$iv2;
                        block88: {
                            Unit $i$a$-use-ZipFilesKt$openZip$2$1$2333333332;
                            Throwable thrown$iv4;
                            block79: {
                                long eocdOffset;
                                block78: {
                                    Intrinsics.checkNotNullParameter(zipPath, "zipPath");
                                    Intrinsics.checkNotNullParameter(fileSystem, "fileSystem");
                                    Intrinsics.checkNotNullParameter(predicate, "predicate");
                                    $this$use$iv = fileSystem.openReadOnly(zipPath);
                                    boolean $i$f$use = false;
                                    thrown$iv = null;
                                    fileHandle = (FileHandle)$this$use$iv;
                                    boolean bl = false;
                                    long scanOffset = fileHandle.size() - (long)22;
                                    if (scanOffset < 0L) {
                                        throw new IOException("not a zip: size=" + fileHandle.size());
                                    }
                                    long stopOffset = Math.max(scanOffset - 65536L, 0L);
                                    eocdOffset = 0L;
                                    record = null;
                                    comment = null;
                                    do {
                                        try (BufferedSource source2 = Okio.buffer(fileHandle.source(scanOffset));){
                                            if (source2.readIntLe() != 101010256) continue;
                                            eocdOffset = scanOffset;
                                            record = ZipFilesKt.readEocdRecord(source2);
                                            comment = source2.readUtf8(record.getCommentByteCount());
                                            break block78;
                                        }
                                    } while ((scanOffset += -1L) >= stopOffset);
                                    throw new IOException("not a zip: end of central directory signature not found");
                                }
                                long zip64LocatorOffset = eocdOffset - (long)20;
                                if (zip64LocatorOffset <= 0L) break block87;
                                $this$use$iv2 = Okio.buffer(fileHandle.source(zip64LocatorOffset));
                                boolean $i$f$use = false;
                                thrown$iv3 = null;
                                zip64LocatorSource = (BufferedSource)$this$use$iv2;
                                boolean bl = false;
                                if (zip64LocatorSource.readIntLe() != 117853008) break block88;
                                int diskWithCentralDir = zip64LocatorSource.readIntLe();
                                long zip64EocdRecordOffset = zip64LocatorSource.readLongLe();
                                int numDisks = zip64LocatorSource.readIntLe();
                                if (numDisks != 1 || diskWithCentralDir != 0) {
                                    throw new IOException("unsupported zip: spanned");
                                }
                                Closeable $this$use$iv3 = Okio.buffer(fileHandle.source(zip64EocdRecordOffset));
                                boolean $i$f$use2 = false;
                                thrown$iv4 = null;
                                BufferedSource zip64EocdSource = (BufferedSource)$this$use$iv3;
                                boolean $i$a$-use-ZipFilesKt$openZip$2$1$2333333332 = false;
                                int zip64EocdSignature = zip64EocdSource.readIntLe();
                                if (zip64EocdSignature != 101075792) {
                                    throw new IOException("bad zip: expected " + ZipFilesKt.getHex(101075792) + " but was " + ZipFilesKt.getHex(zip64EocdSignature));
                                }
                                record = ZipFilesKt.readZip64EocdRecord(zip64EocdSource, record);
                                $i$a$-use-ZipFilesKt$openZip$2$1$2333333332 = Unit.INSTANCE;
                                try {
                                    Unit unit2;
                                    Closeable closeable = $this$use$iv3;
                                    if (closeable != null) {
                                        closeable.close();
                                        unit2 = Unit.INSTANCE;
                                        break block79;
                                    }
                                    unit2 = null;
                                }
                                catch (Throwable t$iv) {
                                    thrown$iv4 = t$iv;
                                }
                                break block79;
                                catch (Throwable t$iv) {
                                    try {
                                        thrown$iv4 = t$iv;
                                        $i$a$-use-ZipFilesKt$openZip$2$1$2333333332 = null;
                                    }
                                    catch (Throwable throwable) {
                                        block80: {
                                            try {
                                                Unit unit3;
                                                Closeable closeable = $this$use$iv3;
                                                if (closeable != null) {
                                                    closeable.close();
                                                    unit3 = Unit.INSTANCE;
                                                } else {
                                                    unit3 = null;
                                                }
                                            }
                                            catch (Throwable t$iv2) {
                                                if (thrown$iv4 == null) {
                                                    thrown$iv4 = t$iv2;
                                                    break block80;
                                                }
                                                ExceptionsKt.addSuppressed(thrown$iv4, t$iv2);
                                            }
                                        }
                                        throw throwable;
                                    }
                                    try {
                                        Unit unit4;
                                        Closeable closeable = $this$use$iv3;
                                        if (closeable != null) {
                                            closeable.close();
                                            unit4 = Unit.INSTANCE;
                                        }
                                        unit4 = null;
                                    }
                                    catch (Throwable t$iv3) {
                                        if (thrown$iv4 == null) {
                                            thrown$iv4 = t$iv3;
                                            break block79;
                                        }
                                        ExceptionsKt.addSuppressed(thrown$iv4, t$iv3);
                                    }
                                }
                            }
                            Unit result$iv = $i$a$-use-ZipFilesKt$openZip$2$1$2333333332;
                            Throwable throwable = thrown$iv4;
                            if (throwable != null) {
                                throw throwable;
                            }
                        }
                        zip64LocatorSource = Unit.INSTANCE;
                        try {
                            Unit unit5;
                            Closeable closeable = $this$use$iv2;
                            if (closeable != null) {
                                closeable.close();
                                unit5 = Unit.INSTANCE;
                                break block81;
                            }
                            unit5 = null;
                        }
                        catch (Throwable t$iv) {
                            thrown$iv3 = t$iv;
                        }
                        break block81;
                        catch (Throwable t$iv422222222) {
                            try {
                                thrown$iv3 = t$iv422222222;
                                zip64LocatorSource = null;
                            }
                            catch (Throwable t$iv422222222) {
                                block82: {
                                    try {
                                        Unit unit6;
                                        Closeable closeable = $this$use$iv2;
                                        if (closeable != null) {
                                            closeable.close();
                                            unit6 = Unit.INSTANCE;
                                        } else {
                                            unit6 = null;
                                        }
                                    }
                                    catch (Throwable t$iv5) {
                                        if (thrown$iv3 == null) {
                                            thrown$iv3 = t$iv5;
                                            break block82;
                                        }
                                        ExceptionsKt.addSuppressed(thrown$iv3, t$iv5);
                                    }
                                }
                                throw t$iv422222222;
                            }
                            try {
                                Unit unit7;
                                Closeable closeable = $this$use$iv2;
                                if (closeable != null) {
                                    closeable.close();
                                    unit7 = Unit.INSTANCE;
                                }
                                unit7 = null;
                            }
                            catch (Throwable t$iv6) {
                                if (thrown$iv3 == null) {
                                    thrown$iv3 = t$iv6;
                                    break block81;
                                }
                                ExceptionsKt.addSuppressed(thrown$iv3, t$iv6);
                            }
                        }
                    }
                    Object result$iv = zip64LocatorSource;
                    Throwable throwable = thrown$iv3;
                    if (throwable != null) {
                        throw throwable;
                    }
                }
                entries = new ArrayList();
                Closeable $this$use$iv4 = Okio.buffer(fileHandle.source(record.getCentralDirectoryOffset()));
                boolean $i$f$use = false;
                thrown$iv2 = null;
                BufferedSource source3 = (BufferedSource)$this$use$iv4;
                boolean bl = false;
                long l2 = record.getEntryCount();
                for (long i2 = 0L; i2 < l2; ++i2) {
                    ZipEntry entry = ZipFilesKt.readCentralDirectoryZipEntry(source3);
                    if (entry.getOffset() >= record.getCentralDirectoryOffset()) {
                        throw new IOException("bad zip: local file header offset >= central directory offset");
                    }
                    if (!predicate.invoke(entry).booleanValue()) continue;
                    ((Collection)entries).add(entry);
                }
                unit = Unit.INSTANCE;
                try {
                    Unit unit8;
                    Closeable closeable = $this$use$iv4;
                    if (closeable != null) {
                        closeable.close();
                        unit8 = Unit.INSTANCE;
                        break block83;
                    }
                    unit8 = null;
                }
                catch (Throwable t$iv) {
                    thrown$iv2 = t$iv;
                }
                break block83;
                catch (Throwable t$iv) {
                    try {
                        thrown$iv2 = t$iv;
                        unit = null;
                    }
                    catch (Throwable throwable) {
                        block84: {
                            try {
                                Unit unit9;
                                Closeable closeable = $this$use$iv4;
                                if (closeable != null) {
                                    closeable.close();
                                    unit9 = Unit.INSTANCE;
                                } else {
                                    unit9 = null;
                                }
                            }
                            catch (Throwable t$iv7) {
                                if (thrown$iv2 == null) {
                                    thrown$iv2 = t$iv7;
                                    break block84;
                                }
                                ExceptionsKt.addSuppressed(thrown$iv2, t$iv7);
                            }
                        }
                        throw throwable;
                    }
                    try {
                        Unit unit10;
                        Closeable closeable = $this$use$iv4;
                        if (closeable != null) {
                            closeable.close();
                            unit10 = Unit.INSTANCE;
                        }
                        unit10 = null;
                    }
                    catch (Throwable t$iv8) {
                        if (thrown$iv2 == null) {
                            thrown$iv2 = t$iv8;
                            break block83;
                        }
                        ExceptionsKt.addSuppressed(thrown$iv2, t$iv8);
                    }
                }
            }
            Unit result$iv = unit;
            Throwable throwable = thrown$iv2;
            if (throwable != null) {
                throw throwable;
            }
            Map<Path, ZipEntry> index = ZipFilesKt.buildIndex(entries);
            ZipFileSystem zipFileSystem = new ZipFileSystem(zipPath, fileSystem, index, comment);
            try {
                Unit unit11;
                Closeable closeable = $this$use$iv;
                if (closeable != null) {
                    closeable.close();
                    unit11 = Unit.INSTANCE;
                } else {
                    unit11 = null;
                }
            }
            catch (Throwable bl) {
                // empty catch block
            }
            return zipFileSystem;
            catch (Throwable t$iv) {
                try {
                    thrown$iv = t$iv;
                    var41_59 = null;
                }
                catch (Throwable throwable2) {
                    block86: {
                        try {
                            Unit unit12;
                            Closeable closeable = $this$use$iv;
                            if (closeable != null) {
                                closeable.close();
                                unit12 = Unit.INSTANCE;
                            } else {
                                unit12 = null;
                            }
                        }
                        catch (Throwable t$iv9) {
                            if (thrown$iv == null) {
                                thrown$iv = t$iv9;
                                break block86;
                            }
                            ExceptionsKt.addSuppressed(thrown$iv, t$iv9);
                        }
                    }
                    throw throwable2;
                }
                try {
                    Unit unit13;
                    Closeable closeable = $this$use$iv;
                    if (closeable != null) {
                        closeable.close();
                        unit13 = Unit.INSTANCE;
                    }
                    unit13 = null;
                }
                catch (Throwable t$iv10) {
                    if (thrown$iv == null) {
                        thrown$iv = t$iv10;
                        break block85;
                    }
                    ExceptionsKt.addSuppressed(thrown$iv, t$iv10);
                }
            }
        }
        Object result$iv = var41_59;
        Throwable throwable = thrown$iv;
        if (throwable != null) {
            throw throwable;
        }
        throw new KotlinNothingValueException();
    }

    public static /* synthetic */ ZipFileSystem openZip$default(Path path, FileSystem fileSystem, Function1 function1, int n2, Object object) throws IOException {
        if ((n2 & 4) != 0) {
            function1 = ZipFilesKt::openZip$lambda$0;
        }
        return ZipFilesKt.openZip(path, fileSystem, function1);
    }

    private static final Map<Path, ZipEntry> buildIndex(List<ZipEntry> entries) {
        Path root = Path.Companion.get$default(Path.Companion, "/", false, 1, null);
        Pair[] pairArray = new Pair[]{TuplesKt.to(root, new ZipEntry(root, true, null, 0L, 0L, 0L, 0, 0L, 0, 0, null, null, null, null, null, null, 65532, null))};
        Map<Path, ZipEntry> result = MapsKt.mutableMapOf(pairArray);
        Iterable $this$sortedBy$iv = entries;
        boolean $i$f$sortedBy = false;
        block0: for (ZipEntry entry : CollectionsKt.sortedWith($this$sortedBy$iv, new Comparator(){

            public final int compare(T a2, T b2) {
                ZipEntry it = (ZipEntry)a2;
                boolean bl = false;
                Comparable comparable = it.getCanonicalPath();
                it = (ZipEntry)b2;
                Comparable comparable2 = comparable;
                bl = false;
                return ComparisonsKt.compareValues(comparable2, (Comparable)it.getCanonicalPath());
            }
        })) {
            ZipEntry replaced = result.put(entry.getCanonicalPath(), entry);
            if (replaced != null) continue;
            ZipEntry child = entry;
            while (child.getCanonicalPath().parent() != null) {
                Path parentPath;
                ZipEntry parentEntry = result.get(parentPath);
                if (parentEntry != null) {
                    ((Collection)parentEntry.getChildren()).add(child.getCanonicalPath());
                    continue block0;
                }
                parentEntry = new ZipEntry(parentPath, true, null, 0L, 0L, 0L, 0, 0L, 0, 0, null, null, null, null, null, null, 65532, null);
                result.put(parentPath, parentEntry);
                ((Collection)parentEntry.getChildren()).add(child.getCanonicalPath());
                child = parentEntry;
            }
        }
        return result;
    }

    @NotNull
    public static final ZipEntry readCentralDirectoryZipEntry(@NotNull BufferedSource $this$readCentralDirectoryZipEntry) throws IOException {
        Intrinsics.checkNotNullParameter($this$readCentralDirectoryZipEntry, "<this>");
        int signature = $this$readCentralDirectoryZipEntry.readIntLe();
        if (signature != 33639248) {
            throw new IOException("bad zip: expected " + ZipFilesKt.getHex(33639248) + " but was " + ZipFilesKt.getHex(signature));
        }
        $this$readCentralDirectoryZipEntry.skip(4L);
        int bitFlag = $this$readCentralDirectoryZipEntry.readShortLe() & 0xFFFF;
        if ((bitFlag & 1) != 0) {
            throw new IOException("unsupported zip: general purpose bit flag=" + ZipFilesKt.getHex(bitFlag));
        }
        int compressionMethod = $this$readCentralDirectoryZipEntry.readShortLe() & 0xFFFF;
        int dosLastModifiedTime = $this$readCentralDirectoryZipEntry.readShortLe() & 0xFFFF;
        int dosLastModifiedDate = $this$readCentralDirectoryZipEntry.readShortLe() & 0xFFFF;
        long crc = (long)$this$readCentralDirectoryZipEntry.readIntLe() & 0xFFFFFFFFL;
        Ref.LongRef compressedSize = new Ref.LongRef();
        compressedSize.element = (long)$this$readCentralDirectoryZipEntry.readIntLe() & 0xFFFFFFFFL;
        Ref.LongRef size = new Ref.LongRef();
        size.element = (long)$this$readCentralDirectoryZipEntry.readIntLe() & 0xFFFFFFFFL;
        int nameSize = $this$readCentralDirectoryZipEntry.readShortLe() & 0xFFFF;
        int extraSize = $this$readCentralDirectoryZipEntry.readShortLe() & 0xFFFF;
        int commentByteCount = $this$readCentralDirectoryZipEntry.readShortLe() & 0xFFFF;
        $this$readCentralDirectoryZipEntry.skip(8L);
        Ref.LongRef offset = new Ref.LongRef();
        offset.element = (long)$this$readCentralDirectoryZipEntry.readIntLe() & 0xFFFFFFFFL;
        String name = $this$readCentralDirectoryZipEntry.readUtf8(nameSize);
        if (StringsKt.contains$default((CharSequence)name, '\u0000', false, 2, null)) {
            throw new IOException("bad zip: filename contains 0x00");
        }
        BufferedSource $this$readCentralDirectoryZipEntry_u24lambda_u246 = $this$readCentralDirectoryZipEntry;
        boolean bl = false;
        long result = 0L;
        if (size.element == 0xFFFFFFFFL) {
            result += (long)8;
        }
        if (compressedSize.element == 0xFFFFFFFFL) {
            result += (long)8;
        }
        if (offset.element == 0xFFFFFFFFL) {
            result += (long)8;
        }
        long requiredZip64ExtraSize = result;
        Ref.ObjectRef ntfsLastModifiedAtFiletime = new Ref.ObjectRef();
        Ref.ObjectRef ntfsLastAccessedAtFiletime = new Ref.ObjectRef();
        Ref.ObjectRef ntfsCreatedAtFiletime = new Ref.ObjectRef();
        Ref.BooleanRef hasZip64Extra = new Ref.BooleanRef();
        ZipFilesKt.readExtra($this$readCentralDirectoryZipEntry, extraSize, (arg_0, arg_1) -> ZipFilesKt.readCentralDirectoryZipEntry$lambda$8(hasZip64Extra, requiredZip64ExtraSize, size, $this$readCentralDirectoryZipEntry, compressedSize, offset, ntfsLastModifiedAtFiletime, ntfsLastAccessedAtFiletime, ntfsCreatedAtFiletime, arg_0, arg_1));
        if (requiredZip64ExtraSize > 0L && !hasZip64Extra.element) {
            throw new IOException("bad zip: zip64 extra required but absent");
        }
        String comment = $this$readCentralDirectoryZipEntry.readUtf8(commentByteCount);
        Path canonicalPath = Path.Companion.get$default(Path.Companion, "/", false, 1, null).resolve(name);
        boolean isDirectory = StringsKt.endsWith$default(name, "/", false, 2, null);
        return new ZipEntry(canonicalPath, isDirectory, comment, crc, compressedSize.element, size.element, compressionMethod, offset.element, dosLastModifiedDate, dosLastModifiedTime, (Long)ntfsLastModifiedAtFiletime.element, (Long)ntfsLastAccessedAtFiletime.element, (Long)ntfsCreatedAtFiletime.element, null, null, null, 57344, null);
    }

    private static final EocdRecord readEocdRecord(BufferedSource $this$readEocdRecord) throws IOException {
        long totalEntryCount;
        int diskNumber = $this$readEocdRecord.readShortLe() & 0xFFFF;
        int diskWithCentralDir = $this$readEocdRecord.readShortLe() & 0xFFFF;
        long entryCount = $this$readEocdRecord.readShortLe() & 0xFFFF;
        if (entryCount != (totalEntryCount = (long)($this$readEocdRecord.readShortLe() & 0xFFFF)) || diskNumber != 0 || diskWithCentralDir != 0) {
            throw new IOException("unsupported zip: spanned");
        }
        $this$readEocdRecord.skip(4L);
        long centralDirectoryOffset = (long)$this$readEocdRecord.readIntLe() & 0xFFFFFFFFL;
        int commentByteCount = $this$readEocdRecord.readShortLe() & 0xFFFF;
        return new EocdRecord(entryCount, centralDirectoryOffset, commentByteCount);
    }

    private static final EocdRecord readZip64EocdRecord(BufferedSource $this$readZip64EocdRecord, EocdRecord regularRecord) throws IOException {
        $this$readZip64EocdRecord.skip(12L);
        int diskNumber = $this$readZip64EocdRecord.readIntLe();
        int diskWithCentralDirStart = $this$readZip64EocdRecord.readIntLe();
        long entryCount = $this$readZip64EocdRecord.readLongLe();
        long totalEntryCount = $this$readZip64EocdRecord.readLongLe();
        if (entryCount != totalEntryCount || diskNumber != 0 || diskWithCentralDirStart != 0) {
            throw new IOException("unsupported zip: spanned");
        }
        $this$readZip64EocdRecord.skip(8L);
        long centralDirectoryOffset = $this$readZip64EocdRecord.readLongLe();
        return new EocdRecord(entryCount, centralDirectoryOffset, regularRecord.getCommentByteCount());
    }

    private static final void readExtra(BufferedSource $this$readExtra, int extraSize, Function2<? super Integer, ? super Long, Unit> block) {
        long dataSize;
        for (long remaining = (long)extraSize; remaining != 0L; remaining -= dataSize) {
            if (remaining < 4L) {
                throw new IOException("bad zip: truncated header in extra field");
            }
            int headerId = $this$readExtra.readShortLe() & 0xFFFF;
            dataSize = (long)$this$readExtra.readShortLe() & 0xFFFFL;
            if ((remaining -= (long)4) < dataSize) {
                throw new IOException("bad zip: truncated value in extra field");
            }
            $this$readExtra.require(dataSize);
            long sizeBefore = $this$readExtra.getBuffer().size();
            block.invoke((Integer)headerId, (Long)dataSize);
            long fieldRemaining = dataSize + $this$readExtra.getBuffer().size() - sizeBefore;
            if (fieldRemaining < 0L) {
                throw new IOException("unsupported zip: too many bytes processed for " + headerId);
            }
            if (fieldRemaining <= 0L) continue;
            $this$readExtra.getBuffer().skip(fieldRemaining);
        }
    }

    public static final void skipLocalHeader(@NotNull BufferedSource $this$skipLocalHeader) {
        Intrinsics.checkNotNullParameter($this$skipLocalHeader, "<this>");
        ZipFilesKt.readOrSkipLocalHeader($this$skipLocalHeader, null);
    }

    @NotNull
    public static final ZipEntry readLocalHeader(@NotNull BufferedSource $this$readLocalHeader, @NotNull ZipEntry centralDirectoryZipEntry) {
        Intrinsics.checkNotNullParameter($this$readLocalHeader, "<this>");
        Intrinsics.checkNotNullParameter(centralDirectoryZipEntry, "centralDirectoryZipEntry");
        ZipEntry zipEntry = ZipFilesKt.readOrSkipLocalHeader($this$readLocalHeader, centralDirectoryZipEntry);
        Intrinsics.checkNotNull(zipEntry);
        return zipEntry;
    }

    private static final ZipEntry readOrSkipLocalHeader(BufferedSource $this$readOrSkipLocalHeader, ZipEntry centralDirectoryZipEntry) {
        int signature = $this$readOrSkipLocalHeader.readIntLe();
        if (signature != 67324752) {
            throw new IOException("bad zip: expected " + ZipFilesKt.getHex(67324752) + " but was " + ZipFilesKt.getHex(signature));
        }
        $this$readOrSkipLocalHeader.skip(2L);
        int bitFlag = $this$readOrSkipLocalHeader.readShortLe() & 0xFFFF;
        if ((bitFlag & 1) != 0) {
            throw new IOException("unsupported zip: general purpose bit flag=" + ZipFilesKt.getHex(bitFlag));
        }
        $this$readOrSkipLocalHeader.skip(18L);
        long fileNameLength = (long)$this$readOrSkipLocalHeader.readShortLe() & 0xFFFFL;
        int extraSize = $this$readOrSkipLocalHeader.readShortLe() & 0xFFFF;
        $this$readOrSkipLocalHeader.skip(fileNameLength);
        if (centralDirectoryZipEntry == null) {
            $this$readOrSkipLocalHeader.skip(extraSize);
            return null;
        }
        Ref.ObjectRef extendedLastModifiedAtSeconds = new Ref.ObjectRef();
        Ref.ObjectRef extendedLastAccessedAtSeconds = new Ref.ObjectRef();
        Ref.ObjectRef extendedCreatedAtSeconds = new Ref.ObjectRef();
        ZipFilesKt.readExtra($this$readOrSkipLocalHeader, extraSize, (arg_0, arg_1) -> ZipFilesKt.readOrSkipLocalHeader$lambda$10($this$readOrSkipLocalHeader, extendedLastModifiedAtSeconds, extendedLastAccessedAtSeconds, extendedCreatedAtSeconds, arg_0, arg_1));
        return centralDirectoryZipEntry.copy$okio((Integer)extendedLastModifiedAtSeconds.element, (Integer)extendedLastAccessedAtSeconds.element, (Integer)extendedCreatedAtSeconds.element);
    }

    public static final long filetimeToEpochMillis(long filetime) {
        return filetime / (long)10000 - 11644473600000L;
    }

    @Nullable
    public static final Long dosDateTimeToEpochMillis(int date, int time) {
        if (time == -1) {
            return null;
        }
        return _ZlibJvmKt.datePartsToEpochMillis(1980 + (date >> 9 & 0x7F), date >> 5 & 0xF, date & 0x1F, time >> 11 & 0x1F, time >> 5 & 0x3F, (time & 0x1F) << 1);
    }

    private static final String getHex(int $this$hex) {
        StringBuilder stringBuilder = new StringBuilder().append("0x");
        String string = Integer.toString($this$hex, CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return stringBuilder.append(string).toString();
    }

    private static final boolean openZip$lambda$0(ZipEntry it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return true;
    }

    private static final Unit readCentralDirectoryZipEntry$lambda$8$lambda$7(Ref.ObjectRef $ntfsLastModifiedAtFiletime, BufferedSource $this_readCentralDirectoryZipEntry, Ref.ObjectRef $ntfsLastAccessedAtFiletime, Ref.ObjectRef $ntfsCreatedAtFiletime, int attributeId, long attributeSize) {
        if (attributeId == 1) {
            if ($ntfsLastModifiedAtFiletime.element != null) {
                throw new IOException("bad zip: NTFS extra attribute tag 0x0001 repeated");
            }
            if (attributeSize != 24L) {
                throw new IOException("bad zip: NTFS extra attribute tag 0x0001 size != 24");
            }
            $ntfsLastModifiedAtFiletime.element = $this_readCentralDirectoryZipEntry.readLongLe();
            $ntfsLastAccessedAtFiletime.element = $this_readCentralDirectoryZipEntry.readLongLe();
            $ntfsCreatedAtFiletime.element = $this_readCentralDirectoryZipEntry.readLongLe();
        }
        return Unit.INSTANCE;
    }

    private static final Unit readCentralDirectoryZipEntry$lambda$8(Ref.BooleanRef $hasZip64Extra, long $requiredZip64ExtraSize, Ref.LongRef $size, BufferedSource $this_readCentralDirectoryZipEntry, Ref.LongRef $compressedSize, Ref.LongRef $offset, Ref.ObjectRef $ntfsLastModifiedAtFiletime, Ref.ObjectRef $ntfsLastAccessedAtFiletime, Ref.ObjectRef $ntfsCreatedAtFiletime, int headerId, long dataSize) {
        switch (headerId) {
            case 1: {
                if ($hasZip64Extra.element) {
                    throw new IOException("bad zip: zip64 extra repeated");
                }
                $hasZip64Extra.element = true;
                if (dataSize < $requiredZip64ExtraSize) {
                    throw new IOException("bad zip: zip64 extra too short");
                }
                $size.element = $size.element == 0xFFFFFFFFL ? $this_readCentralDirectoryZipEntry.readLongLe() : $size.element;
                $compressedSize.element = $compressedSize.element == 0xFFFFFFFFL ? $this_readCentralDirectoryZipEntry.readLongLe() : 0L;
                $offset.element = $offset.element == 0xFFFFFFFFL ? $this_readCentralDirectoryZipEntry.readLongLe() : 0L;
                break;
            }
            case 10: {
                if (dataSize < 4L) {
                    throw new IOException("bad zip: NTFS extra too short");
                }
                $this_readCentralDirectoryZipEntry.skip(4L);
                ZipFilesKt.readExtra($this_readCentralDirectoryZipEntry, (int)(dataSize - 4L), (arg_0, arg_1) -> ZipFilesKt.readCentralDirectoryZipEntry$lambda$8$lambda$7($ntfsLastModifiedAtFiletime, $this_readCentralDirectoryZipEntry, $ntfsLastAccessedAtFiletime, $ntfsCreatedAtFiletime, arg_0, arg_1));
            }
        }
        return Unit.INSTANCE;
    }

    private static final Unit readOrSkipLocalHeader$lambda$10(BufferedSource $this_readOrSkipLocalHeader, Ref.ObjectRef $extendedLastModifiedAtSeconds, Ref.ObjectRef $extendedLastAccessedAtSeconds, Ref.ObjectRef $extendedCreatedAtSeconds, int headerId, long dataSize) {
        if (headerId == 21589) {
            long requiredSize;
            if (dataSize < 1L) {
                throw new IOException("bad zip: extended timestamp extra too short");
            }
            int flags = $this_readOrSkipLocalHeader.readByte() & 0xFF;
            boolean hasLastModifiedAtMillis = (flags & 1) == 1;
            boolean hasLastAccessedAtMillis = (flags & 2) == 2;
            boolean hasCreatedAtMillis = (flags & 4) == 4;
            BufferedSource $this$readOrSkipLocalHeader_u24lambda_u2410_u24lambda_u249 = $this_readOrSkipLocalHeader;
            boolean bl = false;
            long result = 1L;
            if (hasLastModifiedAtMillis) {
                result += 4L;
            }
            if (hasLastAccessedAtMillis) {
                result += 4L;
            }
            if (hasCreatedAtMillis) {
                result += 4L;
            }
            if (dataSize < (requiredSize = result)) {
                throw new IOException("bad zip: extended timestamp extra too short");
            }
            if (hasLastModifiedAtMillis) {
                $extendedLastModifiedAtSeconds.element = $this_readOrSkipLocalHeader.readIntLe();
            }
            if (hasLastAccessedAtMillis) {
                $extendedLastAccessedAtSeconds.element = $this_readOrSkipLocalHeader.readIntLe();
            }
            if (hasCreatedAtMillis) {
                $extendedCreatedAtSeconds.element = $this_readOrSkipLocalHeader.readIntLe();
            }
        }
        return Unit.INSTANCE;
    }
}

