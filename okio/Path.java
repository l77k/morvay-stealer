/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.SourceDebugExtension
 */
package okio;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmField;
import kotlin.jvm.JvmName;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.Buffer;
import okio.ByteString;
import okio.internal.-Path;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\f\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0004\u0018\u0000 /2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001/B\u0011\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u0016\u0010\u001e\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020\rH\u0087\u0002\u00a2\u0006\u0002\b J\u0016\u0010\u001e\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020\u0003H\u0087\u0002\u00a2\u0006\u0002\b J\u0016\u0010\u001e\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020\u0000H\u0087\u0002\u00a2\u0006\u0002\b J\u0018\u0010 \u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020\r2\b\b\u0002\u0010!\u001a\u00020\u0013J\u0018\u0010 \u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020\u00032\b\b\u0002\u0010!\u001a\u00020\u0013J\u0018\u0010 \u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020\u00002\b\b\u0002\u0010!\u001a\u00020\u0013J\u000e\u0010\"\u001a\u00020\u00002\u0006\u0010#\u001a\u00020\u0000J\u0006\u0010$\u001a\u00020\u0000J\u0006\u0010%\u001a\u00020&J\u0006\u0010'\u001a\u00020(J\u0011\u0010)\u001a\u00020*2\u0006\u0010#\u001a\u00020\u0000H\u0096\u0002J\u0013\u0010+\u001a\u00020\u00132\b\u0010#\u001a\u0004\u0018\u00010,H\u0096\u0002J\b\u0010-\u001a\u00020*H\u0016J\b\u0010.\u001a\u00020\rH\u0016R\u0014\u0010\u0002\u001a\u00020\u0003X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0013\u0010\b\u001a\u0004\u0018\u00010\u00008F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f8F\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00030\f8F\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\u000fR\u0011\u0010\u0012\u001a\u00020\u00138F\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0014R\u0011\u0010\u0015\u001a\u00020\u00138F\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0014R\u0013\u0010\u0016\u001a\u0004\u0018\u00010\u00178G\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0018R\u0011\u0010\u0019\u001a\u00020\u00038G\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u0007R\u0011\u0010\u001a\u001a\u00020\r8G\u00a2\u0006\u0006\u001a\u0004\b\u001a\u0010\u001bR\u0013\u0010\u001c\u001a\u0004\u0018\u00010\u00008G\u00a2\u0006\u0006\u001a\u0004\b\u001c\u0010\nR\u0011\u0010\u001d\u001a\u00020\u00138F\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u0014\u00a8\u00060"}, d2={"Lokio/Path;", "", "bytes", "Lokio/ByteString;", "<init>", "(Lokio/ByteString;)V", "getBytes$okio", "()Lokio/ByteString;", "root", "getRoot", "()Lokio/Path;", "segments", "", "", "getSegments", "()Ljava/util/List;", "segmentsBytes", "getSegmentsBytes", "isAbsolute", "", "()Z", "isRelative", "volumeLetter", "", "()Ljava/lang/Character;", "nameBytes", "name", "()Ljava/lang/String;", "parent", "isRoot", "div", "child", "resolve", "normalize", "relativeTo", "other", "normalized", "toFile", "Ljava/io/File;", "toNioPath", "Ljava/nio/file/Path;", "compareTo", "", "equals", "", "hashCode", "toString", "Companion", "okio"})
@SourceDebugExtension(value={"SMAP\nPath.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Path.kt\nokio/Path\n+ 2 Path.kt\nokio/internal/-Path\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,132:1\n39#2,3:133\n47#2,28:136\n53#2,22:168\n106#2:190\n111#2:191\n116#2,6:192\n133#2,5:198\n143#2:203\n148#2,25:204\n188#2:229\n193#2,11:230\n198#2,6:241\n193#2,11:247\n198#2,6:258\n222#2,41:264\n267#2:305\n281#2:306\n286#2:307\n291#2:308\n296#2:309\n1557#3:164\n1628#3,3:165\n*S KotlinDebug\n*F\n+ 1 Path.kt\nokio/Path\n*L\n44#1:133,3\n47#1:136,28\n50#1:168,22\n53#1:190\n56#1:191\n60#1:192,6\n64#1:198,5\n68#1:203\n72#1:204,25\n75#1:229\n78#1:230,11\n81#1:241,6\n87#1:247,11\n90#1:258,6\n95#1:264,41\n97#1:305\n104#1:306\n106#1:307\n108#1:308\n110#1:309\n47#1:164\n47#1:165,3\n*E\n"})
public final class Path
implements Comparable<Path> {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final ByteString bytes;
    @JvmField
    @NotNull
    public static final String DIRECTORY_SEPARATOR;

    public Path(@NotNull ByteString bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        this.bytes = bytes;
    }

    @NotNull
    public final ByteString getBytes$okio() {
        return this.bytes;
    }

    @Nullable
    public final Path getRoot() {
        Path $this$commonRoot$iv = this;
        boolean $i$f$commonRoot = false;
        int rootLength$iv = -Path.access$rootLength($this$commonRoot$iv);
        return rootLength$iv == -1 ? null : new Path($this$commonRoot$iv.getBytes$okio().substring(0, rootLength$iv));
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final List<String> getSegments() {
        void $this$mapTo$iv$iv$iv;
        Path $this$commonSegments$iv = this;
        boolean $i$f$commonSegments = false;
        Path $this$commonSegmentsBytes$iv$iv = $this$commonSegments$iv;
        boolean $i$f$commonSegmentsBytes = false;
        Iterable result$iv$iv = new ArrayList();
        int segmentStart$iv$iv = -Path.access$rootLength($this$commonSegmentsBytes$iv$iv);
        if (segmentStart$iv$iv == -1) {
            segmentStart$iv$iv = 0;
        } else if (segmentStart$iv$iv < $this$commonSegmentsBytes$iv$iv.getBytes$okio().size() && $this$commonSegmentsBytes$iv$iv.getBytes$okio().getByte(segmentStart$iv$iv) == 92) {
            ++segmentStart$iv$iv;
        }
        int n2 = $this$commonSegmentsBytes$iv$iv.getBytes$okio().size();
        for (int i$iv$iv = segmentStart$iv$iv; i$iv$iv < n2; ++i$iv$iv) {
            if ($this$commonSegmentsBytes$iv$iv.getBytes$okio().getByte(i$iv$iv) != 47 && $this$commonSegmentsBytes$iv$iv.getBytes$okio().getByte(i$iv$iv) != 92) continue;
            ((Collection)result$iv$iv).add($this$commonSegmentsBytes$iv$iv.getBytes$okio().substring(segmentStart$iv$iv, i$iv$iv));
            segmentStart$iv$iv = i$iv$iv + 1;
        }
        if (segmentStart$iv$iv < $this$commonSegmentsBytes$iv$iv.getBytes$okio().size()) {
            ((Collection)result$iv$iv).add($this$commonSegmentsBytes$iv$iv.getBytes$okio().substring(segmentStart$iv$iv, $this$commonSegmentsBytes$iv$iv.getBytes$okio().size()));
        }
        Iterable $this$map$iv$iv = result$iv$iv;
        boolean $i$f$map = false;
        result$iv$iv = $this$map$iv$iv;
        Collection destination$iv$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv$iv, 10));
        boolean $i$f$mapTo = false;
        for (Object item$iv$iv$iv : $this$mapTo$iv$iv$iv) {
            void it$iv;
            ByteString byteString = (ByteString)item$iv$iv$iv;
            Collection collection = destination$iv$iv$iv;
            boolean bl = false;
            collection.add(it$iv.utf8());
        }
        return (List)destination$iv$iv$iv;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final List<ByteString> getSegmentsBytes() {
        void var3_3;
        Path $this$commonSegmentsBytes$iv = this;
        boolean $i$f$commonSegmentsBytes = false;
        List result$iv = new ArrayList();
        int segmentStart$iv = -Path.access$rootLength($this$commonSegmentsBytes$iv);
        if (segmentStart$iv == -1) {
            segmentStart$iv = 0;
        } else if (segmentStart$iv < $this$commonSegmentsBytes$iv.getBytes$okio().size() && $this$commonSegmentsBytes$iv.getBytes$okio().getByte(segmentStart$iv) == 92) {
            ++segmentStart$iv;
        }
        int n2 = $this$commonSegmentsBytes$iv.getBytes$okio().size();
        for (int i$iv = segmentStart$iv; i$iv < n2; ++i$iv) {
            if ($this$commonSegmentsBytes$iv.getBytes$okio().getByte(i$iv) != 47 && $this$commonSegmentsBytes$iv.getBytes$okio().getByte(i$iv) != 92) continue;
            ((Collection)result$iv).add($this$commonSegmentsBytes$iv.getBytes$okio().substring(segmentStart$iv, i$iv));
            segmentStart$iv = i$iv + 1;
        }
        if (segmentStart$iv < $this$commonSegmentsBytes$iv.getBytes$okio().size()) {
            ((Collection)result$iv).add($this$commonSegmentsBytes$iv.getBytes$okio().substring(segmentStart$iv, $this$commonSegmentsBytes$iv.getBytes$okio().size()));
        }
        return var3_3;
    }

    public final boolean isAbsolute() {
        Path $this$commonIsAbsolute$iv = this;
        boolean $i$f$commonIsAbsolute = false;
        return -Path.access$rootLength($this$commonIsAbsolute$iv) != -1;
    }

    public final boolean isRelative() {
        Path $this$commonIsRelative$iv = this;
        boolean $i$f$commonIsRelative = false;
        return -Path.access$rootLength($this$commonIsRelative$iv) == -1;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @JvmName(name="volumeLetter")
    @Nullable
    public final Character volumeLetter() {
        Path $this$commonVolumeLetter$iv = this;
        boolean $i$f$commonVolumeLetter = false;
        if (ByteString.indexOf$default($this$commonVolumeLetter$iv.getBytes$okio(), -Path.access$getSLASH$p(), 0, 2, null) != -1) {
            return null;
        }
        if ($this$commonVolumeLetter$iv.getBytes$okio().size() < 2) {
            return null;
        }
        if ($this$commonVolumeLetter$iv.getBytes$okio().getByte(1) != 58) {
            return null;
        }
        char c$iv = (char)$this$commonVolumeLetter$iv.getBytes$okio().getByte(0);
        if (!('a' <= c$iv ? c$iv < '{' : false)) {
            if ('A' > c$iv) return null;
            if (c$iv >= '[') return null;
            boolean bl = true;
            if (!bl) {
                return null;
            }
        }
        Character c2 = Character.valueOf(c$iv);
        return c2;
    }

    @JvmName(name="nameBytes")
    @NotNull
    public final ByteString nameBytes() {
        Path $this$commonNameBytes$iv = this;
        boolean $i$f$commonNameBytes = false;
        int lastSlash$iv = -Path.access$getIndexOfLastSlash($this$commonNameBytes$iv);
        return lastSlash$iv != -1 ? ByteString.substring$default($this$commonNameBytes$iv.getBytes$okio(), lastSlash$iv + 1, 0, 2, null) : ($this$commonNameBytes$iv.volumeLetter() != null && $this$commonNameBytes$iv.getBytes$okio().size() == 2 ? ByteString.EMPTY : $this$commonNameBytes$iv.getBytes$okio());
    }

    @JvmName(name="name")
    @NotNull
    public final String name() {
        Path $this$commonName$iv = this;
        boolean $i$f$commonName = false;
        return $this$commonName$iv.nameBytes().utf8();
    }

    @JvmName(name="parent")
    @Nullable
    public final Path parent() {
        Object object;
        Path $this$commonParent$iv = this;
        boolean $i$f$commonParent = false;
        if (Intrinsics.areEqual($this$commonParent$iv.getBytes$okio(), -Path.access$getDOT$p()) || Intrinsics.areEqual($this$commonParent$iv.getBytes$okio(), -Path.access$getSLASH$p()) || Intrinsics.areEqual($this$commonParent$iv.getBytes$okio(), -Path.access$getBACKSLASH$p()) || -Path.access$lastSegmentIsDotDot($this$commonParent$iv)) {
            object = null;
        } else {
            int lastSlash$iv = -Path.access$getIndexOfLastSlash($this$commonParent$iv);
            object = lastSlash$iv == 2 && $this$commonParent$iv.volumeLetter() != null ? ($this$commonParent$iv.getBytes$okio().size() == 3 ? null : new Path(ByteString.substring$default($this$commonParent$iv.getBytes$okio(), 0, 3, 1, null))) : (lastSlash$iv == 1 && $this$commonParent$iv.getBytes$okio().startsWith(-Path.access$getBACKSLASH$p()) ? null : (lastSlash$iv == -1 && $this$commonParent$iv.volumeLetter() != null ? ($this$commonParent$iv.getBytes$okio().size() == 2 ? null : new Path(ByteString.substring$default($this$commonParent$iv.getBytes$okio(), 0, 2, 1, null))) : (lastSlash$iv == -1 ? new Path(-Path.access$getDOT$p()) : (lastSlash$iv == 0 ? new Path(ByteString.substring$default($this$commonParent$iv.getBytes$okio(), 0, 1, 1, null)) : new Path(ByteString.substring$default($this$commonParent$iv.getBytes$okio(), 0, lastSlash$iv, 1, null))))));
        }
        return object;
    }

    public final boolean isRoot() {
        Path $this$commonIsRoot$iv = this;
        boolean $i$f$commonIsRoot = false;
        return -Path.access$rootLength($this$commonIsRoot$iv) == $this$commonIsRoot$iv.getBytes$okio().size();
    }

    /*
     * WARNING - void declaration
     */
    @JvmName(name="resolve")
    @NotNull
    public final Path resolve(@NotNull String child) {
        void $this$commonResolve$iv$iv;
        void $this$commonResolve$iv;
        Intrinsics.checkNotNullParameter(child, "child");
        Path path = this;
        boolean normalize$iv = false;
        boolean $i$f$commonResolve = false;
        void var5_5 = $this$commonResolve$iv;
        Buffer child$iv$iv = new Buffer().writeUtf8(child);
        boolean $i$f$commonResolve2 = false;
        return -Path.commonResolve((Path)$this$commonResolve$iv$iv, -Path.toPath(child$iv$iv, false), normalize$iv);
    }

    /*
     * WARNING - void declaration
     */
    @JvmName(name="resolve")
    @NotNull
    public final Path resolve(@NotNull ByteString child) {
        void $this$commonResolve$iv$iv;
        void $this$commonResolve$iv;
        Intrinsics.checkNotNullParameter(child, "child");
        Path path = this;
        boolean normalize$iv = false;
        boolean $i$f$commonResolve = false;
        void var5_5 = $this$commonResolve$iv;
        Buffer child$iv$iv = new Buffer().write(child);
        boolean $i$f$commonResolve2 = false;
        return -Path.commonResolve((Path)$this$commonResolve$iv$iv, -Path.toPath(child$iv$iv, false), normalize$iv);
    }

    @JvmName(name="resolve")
    @NotNull
    public final Path resolve(@NotNull Path child) {
        Intrinsics.checkNotNullParameter(child, "child");
        return -Path.commonResolve(this, child, false);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final Path resolve(@NotNull String child, boolean normalize) {
        void $this$commonResolve$iv$iv;
        Intrinsics.checkNotNullParameter(child, "child");
        Path $this$commonResolve$iv = this;
        boolean $i$f$commonResolve = false;
        Path path = $this$commonResolve$iv;
        Buffer child$iv$iv = new Buffer().writeUtf8(child);
        boolean $i$f$commonResolve2 = false;
        return -Path.commonResolve((Path)$this$commonResolve$iv$iv, -Path.toPath(child$iv$iv, false), normalize);
    }

    public static /* synthetic */ Path resolve$default(Path path, String string, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        return path.resolve(string, bl);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final Path resolve(@NotNull ByteString child, boolean normalize) {
        void $this$commonResolve$iv$iv;
        Intrinsics.checkNotNullParameter(child, "child");
        Path $this$commonResolve$iv = this;
        boolean $i$f$commonResolve = false;
        Path path = $this$commonResolve$iv;
        Buffer child$iv$iv = new Buffer().write(child);
        boolean $i$f$commonResolve2 = false;
        return -Path.commonResolve((Path)$this$commonResolve$iv$iv, -Path.toPath(child$iv$iv, false), normalize);
    }

    public static /* synthetic */ Path resolve$default(Path path, ByteString byteString, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        return path.resolve(byteString, bl);
    }

    @NotNull
    public final Path resolve(@NotNull Path child, boolean normalize) {
        Intrinsics.checkNotNullParameter(child, "child");
        return -Path.commonResolve(this, child, normalize);
    }

    public static /* synthetic */ Path resolve$default(Path path, Path path2, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        return path.resolve(path2, bl);
    }

    @NotNull
    public final Path relativeTo(@NotNull Path other) {
        Path path;
        int firstNewSegmentIndex$iv;
        Intrinsics.checkNotNullParameter(other, "other");
        Path $this$commonRelativeTo$iv = this;
        boolean $i$f$commonRelativeTo = false;
        if (!Intrinsics.areEqual($this$commonRelativeTo$iv.getRoot(), other.getRoot())) {
            boolean $i$a$-require--Path$commonRelativeTo$1$iv22 = false;
            String $i$a$-require--Path$commonRelativeTo$1$iv22 = "Paths of different roots cannot be relative to each other: " + $this$commonRelativeTo$iv + " and " + other;
            throw new IllegalArgumentException($i$a$-require--Path$commonRelativeTo$1$iv22.toString());
        }
        List<ByteString> thisSegments$iv = $this$commonRelativeTo$iv.getSegmentsBytes();
        List<ByteString> otherSegments$iv = other.getSegmentsBytes();
        int minSegmentsSize$iv = Math.min(thisSegments$iv.size(), otherSegments$iv.size());
        for (firstNewSegmentIndex$iv = 0; firstNewSegmentIndex$iv < minSegmentsSize$iv && Intrinsics.areEqual(thisSegments$iv.get(firstNewSegmentIndex$iv), otherSegments$iv.get(firstNewSegmentIndex$iv)); ++firstNewSegmentIndex$iv) {
        }
        if (firstNewSegmentIndex$iv == minSegmentsSize$iv && $this$commonRelativeTo$iv.getBytes$okio().size() == other.getBytes$okio().size()) {
            path = okio.Path$Companion.get$default(Companion, ".", false, 1, null);
        } else {
            if (!(otherSegments$iv.subList(firstNewSegmentIndex$iv, otherSegments$iv.size()).indexOf(-Path.access$getDOT_DOT$p()) == -1)) {
                boolean $i$a$-require--Path$commonRelativeTo$2$iv22 = false;
                String $i$a$-require--Path$commonRelativeTo$2$iv22 = "Impossible relative path to resolve: " + $this$commonRelativeTo$iv + " and " + other;
                throw new IllegalArgumentException($i$a$-require--Path$commonRelativeTo$2$iv22.toString());
            }
            if (Intrinsics.areEqual(other.getBytes$okio(), -Path.access$getDOT$p())) {
                path = $this$commonRelativeTo$iv;
            } else {
                int i$iv;
                Buffer buffer$iv = new Buffer();
                ByteString byteString = -Path.access$getSlash(other);
                if (byteString == null && (byteString = -Path.access$getSlash($this$commonRelativeTo$iv)) == null) {
                    byteString = -Path.access$toSlash(DIRECTORY_SEPARATOR);
                }
                ByteString slash$iv = byteString;
                int n2 = otherSegments$iv.size();
                for (i$iv = firstNewSegmentIndex$iv; i$iv < n2; ++i$iv) {
                    buffer$iv.write(-Path.access$getDOT_DOT$p());
                    buffer$iv.write(slash$iv);
                }
                n2 = thisSegments$iv.size();
                for (i$iv = firstNewSegmentIndex$iv; i$iv < n2; ++i$iv) {
                    buffer$iv.write(thisSegments$iv.get(i$iv));
                    buffer$iv.write(slash$iv);
                }
                path = -Path.toPath(buffer$iv, false);
            }
        }
        return path;
    }

    @NotNull
    public final Path normalized() {
        Path $this$commonNormalized$iv = this;
        boolean $i$f$commonNormalized = false;
        return Companion.get($this$commonNormalized$iv.toString(), true);
    }

    @NotNull
    public final File toFile() {
        return new File(this.toString());
    }

    @NotNull
    public final java.nio.file.Path toNioPath() {
        java.nio.file.Path path = Paths.get(this.toString(), new String[0]);
        Intrinsics.checkNotNullExpressionValue(path, "get(...)");
        return path;
    }

    @Override
    public int compareTo(@NotNull Path other) {
        Intrinsics.checkNotNullParameter(other, "other");
        Path $this$commonCompareTo$iv = this;
        boolean $i$f$commonCompareTo = false;
        return $this$commonCompareTo$iv.getBytes$okio().compareTo(other.getBytes$okio());
    }

    public boolean equals(@Nullable Object other) {
        Path $this$commonEquals$iv = this;
        boolean $i$f$commonEquals = false;
        return other instanceof Path && Intrinsics.areEqual(((Path)other).getBytes$okio(), $this$commonEquals$iv.getBytes$okio());
    }

    public int hashCode() {
        Path $this$commonHashCode$iv = this;
        boolean $i$f$commonHashCode = false;
        return $this$commonHashCode$iv.getBytes$okio().hashCode();
    }

    @NotNull
    public String toString() {
        Path $this$commonToString$iv = this;
        boolean $i$f$commonToString = false;
        return $this$commonToString$iv.getBytes$okio().utf8();
    }

    @JvmStatic
    @JvmName(name="get")
    @JvmOverloads
    @NotNull
    public static final Path get(@NotNull String $this$get, boolean normalize) {
        return Companion.get($this$get, normalize);
    }

    @JvmStatic
    @JvmName(name="get")
    @JvmOverloads
    @NotNull
    public static final Path get(@NotNull File $this$get, boolean normalize) {
        return Companion.get($this$get, normalize);
    }

    @JvmStatic
    @JvmName(name="get")
    @JvmOverloads
    @NotNull
    public static final Path get(@NotNull java.nio.file.Path $this$get, boolean normalize) {
        return Companion.get($this$get, normalize);
    }

    @JvmStatic
    @JvmName(name="get")
    @JvmOverloads
    @NotNull
    public static final Path get(@NotNull String $this$get) {
        return Companion.get($this$get);
    }

    @JvmStatic
    @JvmName(name="get")
    @JvmOverloads
    @NotNull
    public static final Path get(@NotNull File $this$get) {
        return Companion.get($this$get);
    }

    @JvmStatic
    @JvmName(name="get")
    @JvmOverloads
    @NotNull
    public static final Path get(@NotNull java.nio.file.Path $this$get) {
        return Companion.get($this$get);
    }

    static {
        String string = File.separator;
        Intrinsics.checkNotNullExpressionValue(string, "separator");
        DIRECTORY_SEPARATOR = string;
    }

    @Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u001b\u0010\u0006\u001a\u00020\u0007*\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u00a2\u0006\u0002\b\nJ\u001b\u0010\u000b\u001a\u00020\u0007*\u00020\f2\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u00a2\u0006\u0002\b\nJ\u001b\u0010\u000b\u001a\u00020\u0007*\u00020\r2\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u00a2\u0006\u0002\b\nR\u0010\u0010\u0004\u001a\u00020\u00058\u0006X\u0087D\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2={"Lokio/Path$Companion;", "", "<init>", "()V", "DIRECTORY_SEPARATOR", "", "toPath", "Lokio/Path;", "normalize", "", "get", "toOkioPath", "Ljava/io/File;", "Ljava/nio/file/Path;", "okio"})
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        @JvmName(name="get")
        @JvmOverloads
        @NotNull
        public final Path get(@NotNull String $this$toPath, boolean normalize) {
            Intrinsics.checkNotNullParameter($this$toPath, "<this>");
            return -Path.commonToPath($this$toPath, normalize);
        }

        public static /* synthetic */ Path get$default(Companion companion, String string, boolean bl, int n2, Object object) {
            if ((n2 & 1) != 0) {
                bl = false;
            }
            return companion.get(string, bl);
        }

        @JvmStatic
        @JvmName(name="get")
        @JvmOverloads
        @NotNull
        public final Path get(@NotNull File $this$toOkioPath, boolean normalize) {
            Intrinsics.checkNotNullParameter($this$toOkioPath, "<this>");
            String string = $this$toOkioPath.toString();
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            return this.get(string, normalize);
        }

        public static /* synthetic */ Path get$default(Companion companion, File file, boolean bl, int n2, Object object) {
            if ((n2 & 1) != 0) {
                bl = false;
            }
            return companion.get(file, bl);
        }

        @JvmStatic
        @JvmName(name="get")
        @JvmOverloads
        @NotNull
        public final Path get(@NotNull java.nio.file.Path $this$toOkioPath, boolean normalize) {
            Intrinsics.checkNotNullParameter($this$toOkioPath, "<this>");
            return this.get(((Object)$this$toOkioPath).toString(), normalize);
        }

        public static /* synthetic */ Path get$default(Companion companion, java.nio.file.Path path, boolean bl, int n2, Object object) {
            if ((n2 & 1) != 0) {
                bl = false;
            }
            return companion.get(path, bl);
        }

        @JvmStatic
        @JvmName(name="get")
        @JvmOverloads
        @NotNull
        public final Path get(@NotNull String $this$toPath) {
            Intrinsics.checkNotNullParameter($this$toPath, "<this>");
            return okio.Path$Companion.get$default(this, $this$toPath, false, 1, null);
        }

        @JvmStatic
        @JvmName(name="get")
        @JvmOverloads
        @NotNull
        public final Path get(@NotNull File $this$toOkioPath) {
            Intrinsics.checkNotNullParameter($this$toOkioPath, "<this>");
            return okio.Path$Companion.get$default(this, $this$toOkioPath, false, 1, null);
        }

        @JvmStatic
        @JvmName(name="get")
        @JvmOverloads
        @NotNull
        public final Path get(@NotNull java.nio.file.Path $this$toOkioPath) {
            Intrinsics.checkNotNullParameter($this$toOkioPath, "<this>");
            return okio.Path$Companion.get$default(this, $this$toOkioPath, false, 1, null);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

