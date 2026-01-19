/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.SourceDebugExtension
 */
package okio.internal;

import java.io.EOFException;
import kotlin.Metadata;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.CharsKt;
import okio.-SegmentedByteString;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;
import okio.Options;
import okio.PeekSource;
import okio.RealBufferedSource;
import okio.Sink;
import okio.Timeout;
import okio.internal.-Buffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={2, 1, 0}, k=2, xi=48, d1={"\u0000h\n\u0000\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\n\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001d\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0001H\u0080\b\u001a\r\u0010\u0006\u001a\u00020\u0007*\u00020\u0002H\u0080\b\u001a\u0015\u0010\b\u001a\u00020\t*\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0001H\u0080\b\u001a\u0015\u0010\n\u001a\u00020\u0007*\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0001H\u0080\b\u001a\r\u0010\u000b\u001a\u00020\f*\u00020\u0002H\u0080\b\u001a\r\u0010\r\u001a\u00020\u000e*\u00020\u0002H\u0080\b\u001a\u0015\u0010\r\u001a\u00020\u000e*\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0001H\u0080\b\u001a\u0015\u0010\u000f\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0011\u001a\u00020\u0012H\u0080\b\u001a\r\u0010\u0013\u001a\u00020\u0014*\u00020\u0002H\u0080\b\u001a\u0015\u0010\u0013\u001a\u00020\u0014*\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0001H\u0080\b\u001a\u0015\u0010\u0015\u001a\u00020\t*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0014H\u0080\b\u001a%\u0010\u0000\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0005\u001a\u00020\u0010H\u0080\b\u001a\u001d\u0010\u0015\u001a\u00020\t*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0001H\u0080\b\u001a\u0015\u0010\u0017\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0018H\u0080\b\u001a\r\u0010\u0019\u001a\u00020\u001a*\u00020\u0002H\u0080\b\u001a\u0015\u0010\u0019\u001a\u00020\u001a*\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0001H\u0080\b\u001a\u000f\u0010\u001b\u001a\u0004\u0018\u00010\u001a*\u00020\u0002H\u0080\b\u001a\u0015\u0010\u001c\u001a\u00020\u001a*\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u0001H\u0080\b\u001a\r\u0010\u001e\u001a\u00020\u0010*\u00020\u0002H\u0080\b\u001a\r\u0010\u001f\u001a\u00020 *\u00020\u0002H\u0080\b\u001a\r\u0010!\u001a\u00020 *\u00020\u0002H\u0080\b\u001a\r\u0010\"\u001a\u00020\u0010*\u00020\u0002H\u0080\b\u001a\r\u0010#\u001a\u00020\u0010*\u00020\u0002H\u0080\b\u001a\r\u0010$\u001a\u00020\u0001*\u00020\u0002H\u0080\b\u001a\r\u0010%\u001a\u00020\u0001*\u00020\u0002H\u0080\b\u001a\r\u0010&\u001a\u00020\u0001*\u00020\u0002H\u0080\b\u001a\r\u0010'\u001a\u00020\u0001*\u00020\u0002H\u0080\b\u001a\u0015\u0010(\u001a\u00020\t*\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0001H\u0080\b\u001a%\u0010)\u001a\u00020\u0001*\u00020\u00022\u0006\u0010*\u001a\u00020\f2\u0006\u0010+\u001a\u00020\u00012\u0006\u0010,\u001a\u00020\u0001H\u0080\b\u001a\u001d\u0010)\u001a\u00020\u0001*\u00020\u00022\u0006\u0010-\u001a\u00020\u000e2\u0006\u0010+\u001a\u00020\u0001H\u0080\b\u001a\u001d\u0010.\u001a\u00020\u0001*\u00020\u00022\u0006\u0010/\u001a\u00020\u000e2\u0006\u0010+\u001a\u00020\u0001H\u0080\b\u001a-\u00100\u001a\u00020\u0007*\u00020\u00022\u0006\u0010\u0016\u001a\u00020\u00012\u0006\u0010-\u001a\u00020\u000e2\u0006\u00101\u001a\u00020\u00102\u0006\u0010\u0005\u001a\u00020\u0010H\u0080\b\u001a\r\u00102\u001a\u000203*\u00020\u0002H\u0080\b\u001a\r\u00104\u001a\u00020\t*\u00020\u0002H\u0080\b\u001a\r\u00105\u001a\u000206*\u00020\u0002H\u0080\b\u001a\r\u00107\u001a\u00020\u001a*\u00020\u0002H\u0080\b\u00a8\u00068"}, d2={"commonRead", "", "Lokio/RealBufferedSource;", "sink", "Lokio/Buffer;", "byteCount", "commonExhausted", "", "commonRequire", "", "commonRequest", "commonReadByte", "", "commonReadByteString", "Lokio/ByteString;", "commonSelect", "", "options", "Lokio/Options;", "commonReadByteArray", "", "commonReadFully", "offset", "commonReadAll", "Lokio/Sink;", "commonReadUtf8", "", "commonReadUtf8Line", "commonReadUtf8LineStrict", "limit", "commonReadUtf8CodePoint", "commonReadShort", "", "commonReadShortLe", "commonReadInt", "commonReadIntLe", "commonReadLong", "commonReadLongLe", "commonReadDecimalLong", "commonReadHexadecimalUnsignedLong", "commonSkip", "commonIndexOf", "b", "fromIndex", "toIndex", "bytes", "commonIndexOfElement", "targetBytes", "commonRangeEquals", "bytesOffset", "commonPeek", "Lokio/BufferedSource;", "commonClose", "commonTimeout", "Lokio/Timeout;", "commonToString", "okio"})
@JvmName(name="-RealBufferedSource")
@SourceDebugExtension(value={"SMAP\nRealBufferedSource.kt\nKotlin\n*S Kotlin\n*F\n+ 1 RealBufferedSource.kt\nokio/internal/-RealBufferedSource\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 RealBufferedSource.kt\nokio/RealBufferedSource\n+ 4 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,404:1\n1#2:405\n63#3:406\n63#3:407\n63#3:408\n63#3:409\n63#3:410\n63#3:411\n63#3:412\n63#3:413\n63#3:414\n63#3:415\n63#3:416\n63#3:417\n63#3:418\n63#3:419\n63#3:420\n63#3:421\n63#3:422\n63#3:423\n63#3:424\n63#3:425\n63#3:426\n63#3:427\n63#3:428\n63#3:430\n63#3:431\n63#3:432\n63#3:433\n63#3:434\n63#3:435\n63#3:436\n63#3:437\n63#3:438\n63#3:439\n63#3:440\n63#3:441\n63#3:442\n63#3:443\n63#3:444\n63#3:445\n63#3:446\n63#3:447\n63#3:448\n63#3:449\n63#3:451\n63#3:452\n63#3:453\n63#3:454\n63#3:455\n63#3:456\n63#3:457\n63#3:458\n63#3:459\n63#3:460\n63#3:461\n63#3:462\n63#3:463\n63#3:464\n63#3:465\n63#3:466\n63#3:467\n63#3:468\n63#3:469\n63#3:470\n63#3:471\n63#3:472\n63#3:473\n63#3:474\n63#3:475\n63#3:476\n63#3:477\n88#4:429\n88#4:450\n*S KotlinDebug\n*F\n+ 1 RealBufferedSource.kt\nokio/internal/-RealBufferedSource\n*L\n41#1:406\n43#1:407\n47#1:408\n48#1:409\n53#1:410\n63#1:411\n64#1:412\n71#1:413\n75#1:414\n76#1:415\n81#1:416\n88#1:417\n95#1:418\n100#1:419\n108#1:420\n109#1:421\n114#1:422\n123#1:423\n124#1:424\n131#1:425\n137#1:426\n139#1:427\n143#1:428\n144#1:430\n152#1:431\n156#1:432\n161#1:433\n162#1:434\n165#1:435\n168#1:436\n169#1:437\n170#1:438\n176#1:439\n177#1:440\n182#1:441\n189#1:442\n190#1:443\n195#1:444\n203#1:445\n205#1:446\n206#1:447\n208#1:448\n211#1:449\n213#1:451\n221#1:452\n228#1:453\n233#1:454\n238#1:455\n243#1:456\n248#1:457\n253#1:458\n258#1:459\n266#1:460\n277#1:461\n285#1:462\n299#1:463\n306#1:464\n309#1:465\n310#1:466\n321#1:467\n326#1:468\n327#1:469\n340#1:470\n343#1:471\n344#1:472\n356#1:473\n359#1:474\n360#1:475\n385#1:476\n398#1:477\n143#1:429\n211#1:450\n*E\n"})
public final class -RealBufferedSource {
    public static final long commonRead(@NotNull RealBufferedSource $this$commonRead, @NotNull Buffer sink2, long byteCount) {
        boolean $i$f$getBuffer;
        RealBufferedSource this_$iv22;
        Intrinsics.checkNotNullParameter($this$commonRead, "<this>");
        Intrinsics.checkNotNullParameter(sink2, "sink");
        boolean $i$f$commonRead = false;
        if (!(byteCount >= 0L)) {
            boolean $i$a$-require--RealBufferedSource$commonRead$22 = false;
            String $i$a$-require--RealBufferedSource$commonRead$22 = "byteCount < 0: " + byteCount;
            throw new IllegalArgumentException($i$a$-require--RealBufferedSource$commonRead$22.toString());
        }
        if (!(!$this$commonRead.closed)) {
            boolean $i$a$-check--RealBufferedSource$commonRead$32 = false;
            String $i$a$-check--RealBufferedSource$commonRead$32 = "closed";
            throw new IllegalStateException($i$a$-check--RealBufferedSource$commonRead$32.toString());
        }
        RealBufferedSource this_$iv = $this$commonRead;
        boolean $i$f$getBuffer2 = false;
        if (this_$iv.bufferField.size() == 0L) {
            if (byteCount == 0L) {
                return 0L;
            }
            this_$iv22 = $this$commonRead;
            $i$f$getBuffer = false;
            long read = $this$commonRead.source.read(this_$iv22.bufferField, 8192L);
            if (read == -1L) {
                return -1L;
            }
        }
        this_$iv22 = $this$commonRead;
        $i$f$getBuffer = false;
        long this_$iv22 = this_$iv22.bufferField.size();
        long toRead = Math.min(byteCount, this_$iv22);
        RealBufferedSource this_$iv3 = $this$commonRead;
        $i$f$getBuffer = false;
        return this_$iv3.bufferField.read(sink2, toRead);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final boolean commonExhausted(@NotNull RealBufferedSource $this$commonExhausted) {
        Intrinsics.checkNotNullParameter($this$commonExhausted, "<this>");
        boolean $i$f$commonExhausted = false;
        if (!(!$this$commonExhausted.closed)) {
            boolean $i$a$-check--RealBufferedSource$commonExhausted$22 = false;
            String $i$a$-check--RealBufferedSource$commonExhausted$22 = "closed";
            throw new IllegalStateException($i$a$-check--RealBufferedSource$commonExhausted$22.toString());
        }
        RealBufferedSource this_$iv = $this$commonExhausted;
        boolean $i$f$getBuffer = false;
        if (!this_$iv.bufferField.exhausted()) return false;
        this_$iv = $this$commonExhausted;
        $i$f$getBuffer = false;
        if ($this$commonExhausted.source.read(this_$iv.bufferField, 8192L) != -1L) return false;
        return true;
    }

    public static final void commonRequire(@NotNull RealBufferedSource $this$commonRequire, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonRequire, "<this>");
        boolean $i$f$commonRequire = false;
        if (!$this$commonRequire.request(byteCount)) {
            throw new EOFException();
        }
    }

    public static final boolean commonRequest(@NotNull RealBufferedSource $this$commonRequest, long byteCount) {
        block3: {
            RealBufferedSource this_$iv;
            Intrinsics.checkNotNullParameter($this$commonRequest, "<this>");
            boolean $i$f$commonRequest = false;
            if (!(byteCount >= 0L)) {
                boolean $i$a$-require--RealBufferedSource$commonRequest$22 = false;
                String $i$a$-require--RealBufferedSource$commonRequest$22 = "byteCount < 0: " + byteCount;
                throw new IllegalArgumentException($i$a$-require--RealBufferedSource$commonRequest$22.toString());
            }
            if (!(!$this$commonRequest.closed)) {
                boolean $i$a$-check--RealBufferedSource$commonRequest$32 = false;
                String $i$a$-check--RealBufferedSource$commonRequest$32 = "closed";
                throw new IllegalStateException($i$a$-check--RealBufferedSource$commonRequest$32.toString());
            }
            do {
                this_$iv = $this$commonRequest;
                boolean $i$f$getBuffer = false;
                if (this_$iv.bufferField.size() >= byteCount) break block3;
                this_$iv = $this$commonRequest;
                $i$f$getBuffer = false;
            } while ($this$commonRequest.source.read(this_$iv.bufferField, 8192L) != -1L);
            return false;
        }
        return true;
    }

    public static final byte commonReadByte(@NotNull RealBufferedSource $this$commonReadByte) {
        Intrinsics.checkNotNullParameter($this$commonReadByte, "<this>");
        boolean $i$f$commonReadByte = false;
        $this$commonReadByte.require(1L);
        RealBufferedSource this_$iv = $this$commonReadByte;
        boolean $i$f$getBuffer = false;
        return this_$iv.bufferField.readByte();
    }

    @NotNull
    public static final ByteString commonReadByteString(@NotNull RealBufferedSource $this$commonReadByteString) {
        Intrinsics.checkNotNullParameter($this$commonReadByteString, "<this>");
        boolean $i$f$commonReadByteString = false;
        RealBufferedSource this_$iv = $this$commonReadByteString;
        boolean $i$f$getBuffer = false;
        this_$iv.bufferField.writeAll($this$commonReadByteString.source);
        this_$iv = $this$commonReadByteString;
        $i$f$getBuffer = false;
        return this_$iv.bufferField.readByteString();
    }

    @NotNull
    public static final ByteString commonReadByteString(@NotNull RealBufferedSource $this$commonReadByteString, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonReadByteString, "<this>");
        boolean $i$f$commonReadByteString = false;
        $this$commonReadByteString.require(byteCount);
        RealBufferedSource this_$iv = $this$commonReadByteString;
        boolean $i$f$getBuffer = false;
        return this_$iv.bufferField.readByteString(byteCount);
    }

    public static final int commonSelect(@NotNull RealBufferedSource $this$commonSelect, @NotNull Options options) {
        int index;
        Intrinsics.checkNotNullParameter($this$commonSelect, "<this>");
        Intrinsics.checkNotNullParameter(options, "options");
        boolean $i$f$commonSelect = false;
        if (!(!$this$commonSelect.closed)) {
            boolean $i$a$-check--RealBufferedSource$commonSelect$22 = false;
            String $i$a$-check--RealBufferedSource$commonSelect$22 = "closed";
            throw new IllegalStateException($i$a$-check--RealBufferedSource$commonSelect$22.toString());
        }
        block4: while (true) {
            RealBufferedSource this_$iv = $this$commonSelect;
            boolean $i$f$getBuffer = false;
            index = -Buffer.selectPrefix(this_$iv.bufferField, options, true);
            switch (index) {
                case -1: {
                    return -1;
                }
                case -2: {
                    RealBufferedSource this_$iv2 = $this$commonSelect;
                    boolean $i$f$getBuffer2 = false;
                    if ($this$commonSelect.source.read(this_$iv2.bufferField, 8192L) != -1L) continue block4;
                    return -1;
                }
            }
            break;
        }
        int selectedSize = options.getByteStrings$okio()[index].size();
        RealBufferedSource this_$iv = $this$commonSelect;
        boolean $i$f$getBuffer = false;
        this_$iv.bufferField.skip(selectedSize);
        return index;
    }

    @NotNull
    public static final byte[] commonReadByteArray(@NotNull RealBufferedSource $this$commonReadByteArray) {
        Intrinsics.checkNotNullParameter($this$commonReadByteArray, "<this>");
        boolean $i$f$commonReadByteArray = false;
        RealBufferedSource this_$iv = $this$commonReadByteArray;
        boolean $i$f$getBuffer = false;
        this_$iv.bufferField.writeAll($this$commonReadByteArray.source);
        this_$iv = $this$commonReadByteArray;
        $i$f$getBuffer = false;
        return this_$iv.bufferField.readByteArray();
    }

    @NotNull
    public static final byte[] commonReadByteArray(@NotNull RealBufferedSource $this$commonReadByteArray, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonReadByteArray, "<this>");
        boolean $i$f$commonReadByteArray = false;
        $this$commonReadByteArray.require(byteCount);
        RealBufferedSource this_$iv = $this$commonReadByteArray;
        boolean $i$f$getBuffer = false;
        return this_$iv.bufferField.readByteArray(byteCount);
    }

    public static final void commonReadFully(@NotNull RealBufferedSource $this$commonReadFully, @NotNull byte[] sink2) {
        Intrinsics.checkNotNullParameter($this$commonReadFully, "<this>");
        Intrinsics.checkNotNullParameter(sink2, "sink");
        boolean $i$f$commonReadFully = false;
        try {
            $this$commonReadFully.require(sink2.length);
        }
        catch (EOFException e2) {
            int offset = 0;
            while (true) {
                RealBufferedSource this_$iv = $this$commonReadFully;
                boolean $i$f$getBuffer = false;
                if (this_$iv.bufferField.size() <= 0L) break;
                RealBufferedSource this_$iv2 = $this$commonReadFully;
                boolean $i$f$getBuffer2 = false;
                this_$iv2 = $this$commonReadFully;
                $i$f$getBuffer2 = false;
                int read = this_$iv2.bufferField.read(sink2, offset, (int)this_$iv2.bufferField.size());
                if (read == -1) {
                    throw new AssertionError();
                }
                offset += read;
            }
            throw e2;
        }
        RealBufferedSource this_$iv = $this$commonReadFully;
        boolean $i$f$getBuffer = false;
        this_$iv.bufferField.readFully(sink2);
    }

    public static final int commonRead(@NotNull RealBufferedSource $this$commonRead, @NotNull byte[] sink2, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonRead, "<this>");
        Intrinsics.checkNotNullParameter(sink2, "sink");
        boolean $i$f$commonRead = false;
        -SegmentedByteString.checkOffsetAndCount(sink2.length, offset, byteCount);
        RealBufferedSource this_$iv = $this$commonRead;
        boolean $i$f$getBuffer = false;
        if (this_$iv.bufferField.size() == 0L) {
            if (byteCount == 0) {
                return 0;
            }
            RealBufferedSource this_$iv2 = $this$commonRead;
            boolean $i$f$getBuffer2 = false;
            long read = $this$commonRead.source.read(this_$iv2.bufferField, 8192L);
            if (read == -1L) {
                return -1;
            }
        }
        RealBufferedSource this_$iv3 = $this$commonRead;
        boolean $i$f$getBuffer3 = false;
        long b$iv = this_$iv3.bufferField.size();
        boolean $i$f$minOf = false;
        int toRead = (int)Math.min((long)byteCount, b$iv);
        this_$iv = $this$commonRead;
        $i$f$getBuffer3 = false;
        return this_$iv.bufferField.read(sink2, offset, toRead);
    }

    public static final void commonReadFully(@NotNull RealBufferedSource $this$commonReadFully, @NotNull Buffer sink2, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonReadFully, "<this>");
        Intrinsics.checkNotNullParameter(sink2, "sink");
        boolean $i$f$commonReadFully = false;
        try {
            $this$commonReadFully.require(byteCount);
        }
        catch (EOFException e2) {
            RealBufferedSource this_$iv = $this$commonReadFully;
            boolean $i$f$getBuffer = false;
            sink2.writeAll(this_$iv.bufferField);
            throw e2;
        }
        RealBufferedSource this_$iv = $this$commonReadFully;
        boolean $i$f$getBuffer = false;
        this_$iv.bufferField.readFully(sink2, byteCount);
    }

    public static final long commonReadAll(@NotNull RealBufferedSource $this$commonReadAll, @NotNull Sink sink2) {
        boolean $i$f$getBuffer;
        RealBufferedSource this_$iv;
        Intrinsics.checkNotNullParameter($this$commonReadAll, "<this>");
        Intrinsics.checkNotNullParameter(sink2, "sink");
        boolean $i$f$commonReadAll = false;
        long totalBytesWritten = 0L;
        while (true) {
            this_$iv = $this$commonReadAll;
            $i$f$getBuffer = false;
            if ($this$commonReadAll.source.read(this_$iv.bufferField, 8192L) == -1L) break;
            RealBufferedSource this_$iv2 = $this$commonReadAll;
            boolean $i$f$getBuffer2 = false;
            long emitByteCount = this_$iv2.bufferField.completeSegmentByteCount();
            if (emitByteCount <= 0L) continue;
            totalBytesWritten += emitByteCount;
            this_$iv2 = $this$commonReadAll;
            $i$f$getBuffer2 = false;
            sink2.write(this_$iv2.bufferField, emitByteCount);
        }
        this_$iv = $this$commonReadAll;
        $i$f$getBuffer = false;
        if (this_$iv.bufferField.size() > 0L) {
            this_$iv = $this$commonReadAll;
            $i$f$getBuffer = false;
            totalBytesWritten += this_$iv.bufferField.size();
            this_$iv = $this$commonReadAll;
            $i$f$getBuffer = false;
            this_$iv = $this$commonReadAll;
            $i$f$getBuffer = false;
            sink2.write(this_$iv.bufferField, this_$iv.bufferField.size());
        }
        return totalBytesWritten;
    }

    @NotNull
    public static final String commonReadUtf8(@NotNull RealBufferedSource $this$commonReadUtf8) {
        Intrinsics.checkNotNullParameter($this$commonReadUtf8, "<this>");
        boolean $i$f$commonReadUtf8 = false;
        RealBufferedSource this_$iv = $this$commonReadUtf8;
        boolean $i$f$getBuffer = false;
        this_$iv.bufferField.writeAll($this$commonReadUtf8.source);
        this_$iv = $this$commonReadUtf8;
        $i$f$getBuffer = false;
        return this_$iv.bufferField.readUtf8();
    }

    @NotNull
    public static final String commonReadUtf8(@NotNull RealBufferedSource $this$commonReadUtf8, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonReadUtf8, "<this>");
        boolean $i$f$commonReadUtf8 = false;
        $this$commonReadUtf8.require(byteCount);
        RealBufferedSource this_$iv = $this$commonReadUtf8;
        boolean $i$f$getBuffer = false;
        return this_$iv.bufferField.readUtf8(byteCount);
    }

    @Nullable
    public static final String commonReadUtf8Line(@NotNull RealBufferedSource $this$commonReadUtf8Line) {
        String string;
        Intrinsics.checkNotNullParameter($this$commonReadUtf8Line, "<this>");
        boolean $i$f$commonReadUtf8Line = false;
        long newline = $this$commonReadUtf8Line.indexOf((byte)10);
        if (newline == -1L) {
            RealBufferedSource this_$iv = $this$commonReadUtf8Line;
            boolean $i$f$getBuffer = false;
            if (this_$iv.bufferField.size() != 0L) {
                this_$iv = $this$commonReadUtf8Line;
                $i$f$getBuffer = false;
                string = $this$commonReadUtf8Line.readUtf8(this_$iv.bufferField.size());
            } else {
                string = null;
            }
        } else {
            RealBufferedSource this_$iv = $this$commonReadUtf8Line;
            boolean $i$f$getBuffer = false;
            string = -Buffer.readUtf8Line(this_$iv.bufferField, newline);
        }
        return string;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final String commonReadUtf8LineStrict(@NotNull RealBufferedSource $this$commonReadUtf8LineStrict, long limit) {
        void a$iv;
        Intrinsics.checkNotNullParameter($this$commonReadUtf8LineStrict, "<this>");
        boolean $i$f$commonReadUtf8LineStrict = false;
        if (!(limit >= 0L)) {
            boolean bl = false;
            String string = "limit < 0: " + limit;
            throw new IllegalArgumentException(string.toString());
        }
        long scanLength = limit == Long.MAX_VALUE ? Long.MAX_VALUE : limit + 1L;
        long newline = $this$commonReadUtf8LineStrict.indexOf((byte)10, 0L, scanLength);
        if (newline != -1L) {
            RealBufferedSource this_$iv = $this$commonReadUtf8LineStrict;
            boolean $i$f$getBuffer = false;
            return -Buffer.readUtf8Line(this_$iv.bufferField, newline);
        }
        if (scanLength < Long.MAX_VALUE && $this$commonReadUtf8LineStrict.request(scanLength)) {
            RealBufferedSource this_$iv = $this$commonReadUtf8LineStrict;
            boolean $i$f$getBuffer = false;
            if (this_$iv.bufferField.getByte(scanLength - 1L) == 13 && $this$commonReadUtf8LineStrict.request(scanLength + 1L)) {
                this_$iv = $this$commonReadUtf8LineStrict;
                $i$f$getBuffer = false;
                if (this_$iv.bufferField.getByte(scanLength) == 10) {
                    this_$iv = $this$commonReadUtf8LineStrict;
                    $i$f$getBuffer = false;
                    return -Buffer.readUtf8Line(this_$iv.bufferField, scanLength);
                }
            }
        }
        Buffer data = new Buffer();
        RealBufferedSource this_$iv22 = $this$commonReadUtf8LineStrict;
        boolean $i$f$getBuffer = false;
        int this_$iv22 = 32;
        RealBufferedSource this_$iv = $this$commonReadUtf8LineStrict;
        boolean $i$f$getBuffer2 = false;
        long b$iv = this_$iv.bufferField.size();
        boolean $i$f$minOf = false;
        this_$iv22.bufferField.copyTo(data, 0L, Math.min((long)a$iv, b$iv));
        RealBufferedSource this_$iv3 = $this$commonReadUtf8LineStrict;
        $i$f$getBuffer = false;
        throw new EOFException("\\n not found: limit=" + Math.min(this_$iv3.bufferField.size(), limit) + " content=" + data.readByteString().hex() + '\u2026');
    }

    public static final int commonReadUtf8CodePoint(@NotNull RealBufferedSource $this$commonReadUtf8CodePoint) {
        Intrinsics.checkNotNullParameter($this$commonReadUtf8CodePoint, "<this>");
        boolean $i$f$commonReadUtf8CodePoint = false;
        $this$commonReadUtf8CodePoint.require(1L);
        RealBufferedSource this_$iv = $this$commonReadUtf8CodePoint;
        boolean $i$f$getBuffer = false;
        byte b0 = this_$iv.bufferField.getByte(0L);
        if ((b0 & 0xE0) == 192) {
            $this$commonReadUtf8CodePoint.require(2L);
        } else if ((b0 & 0xF0) == 224) {
            $this$commonReadUtf8CodePoint.require(3L);
        } else if ((b0 & 0xF8) == 240) {
            $this$commonReadUtf8CodePoint.require(4L);
        }
        this_$iv = $this$commonReadUtf8CodePoint;
        $i$f$getBuffer = false;
        return this_$iv.bufferField.readUtf8CodePoint();
    }

    public static final short commonReadShort(@NotNull RealBufferedSource $this$commonReadShort) {
        Intrinsics.checkNotNullParameter($this$commonReadShort, "<this>");
        boolean $i$f$commonReadShort = false;
        $this$commonReadShort.require(2L);
        RealBufferedSource this_$iv = $this$commonReadShort;
        boolean $i$f$getBuffer = false;
        return this_$iv.bufferField.readShort();
    }

    public static final short commonReadShortLe(@NotNull RealBufferedSource $this$commonReadShortLe) {
        Intrinsics.checkNotNullParameter($this$commonReadShortLe, "<this>");
        boolean $i$f$commonReadShortLe = false;
        $this$commonReadShortLe.require(2L);
        RealBufferedSource this_$iv = $this$commonReadShortLe;
        boolean $i$f$getBuffer = false;
        return this_$iv.bufferField.readShortLe();
    }

    public static final int commonReadInt(@NotNull RealBufferedSource $this$commonReadInt) {
        Intrinsics.checkNotNullParameter($this$commonReadInt, "<this>");
        boolean $i$f$commonReadInt = false;
        $this$commonReadInt.require(4L);
        RealBufferedSource this_$iv = $this$commonReadInt;
        boolean $i$f$getBuffer = false;
        return this_$iv.bufferField.readInt();
    }

    public static final int commonReadIntLe(@NotNull RealBufferedSource $this$commonReadIntLe) {
        Intrinsics.checkNotNullParameter($this$commonReadIntLe, "<this>");
        boolean $i$f$commonReadIntLe = false;
        $this$commonReadIntLe.require(4L);
        RealBufferedSource this_$iv = $this$commonReadIntLe;
        boolean $i$f$getBuffer = false;
        return this_$iv.bufferField.readIntLe();
    }

    public static final long commonReadLong(@NotNull RealBufferedSource $this$commonReadLong) {
        Intrinsics.checkNotNullParameter($this$commonReadLong, "<this>");
        boolean $i$f$commonReadLong = false;
        $this$commonReadLong.require(8L);
        RealBufferedSource this_$iv = $this$commonReadLong;
        boolean $i$f$getBuffer = false;
        return this_$iv.bufferField.readLong();
    }

    public static final long commonReadLongLe(@NotNull RealBufferedSource $this$commonReadLongLe) {
        Intrinsics.checkNotNullParameter($this$commonReadLongLe, "<this>");
        boolean $i$f$commonReadLongLe = false;
        $this$commonReadLongLe.require(8L);
        RealBufferedSource this_$iv = $this$commonReadLongLe;
        boolean $i$f$getBuffer = false;
        return this_$iv.bufferField.readLongLe();
    }

    public static final long commonReadDecimalLong(@NotNull RealBufferedSource $this$commonReadDecimalLong) {
        Intrinsics.checkNotNullParameter($this$commonReadDecimalLong, "<this>");
        boolean $i$f$commonReadDecimalLong = false;
        $this$commonReadDecimalLong.require(1L);
        long pos = 0L;
        while ($this$commonReadDecimalLong.request(pos + 1L)) {
            RealBufferedSource this_$iv22 = $this$commonReadDecimalLong;
            boolean $i$f$getBuffer = false;
            byte b2 = this_$iv22.bufferField.getByte(pos);
            if (!(b2 >= 48 && b2 <= 57 || pos == 0L && b2 == 45)) {
                if (pos != 0L) break;
                StringBuilder stringBuilder = new StringBuilder().append("Expected a digit or '-' but was 0x");
                String string = Integer.toString(b2, CharsKt.checkRadix(16));
                Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                throw new NumberFormatException(stringBuilder.append(string).toString());
            }
            long this_$iv22 = pos;
            pos = this_$iv22 + 1L;
        }
        RealBufferedSource this_$iv = $this$commonReadDecimalLong;
        boolean $i$f$getBuffer = false;
        return this_$iv.bufferField.readDecimalLong();
    }

    public static final long commonReadHexadecimalUnsignedLong(@NotNull RealBufferedSource $this$commonReadHexadecimalUnsignedLong) {
        Intrinsics.checkNotNullParameter($this$commonReadHexadecimalUnsignedLong, "<this>");
        boolean $i$f$commonReadHexadecimalUnsignedLong = false;
        $this$commonReadHexadecimalUnsignedLong.require(1L);
        int pos = 0;
        while ($this$commonReadHexadecimalUnsignedLong.request(pos + 1)) {
            RealBufferedSource this_$iv = $this$commonReadHexadecimalUnsignedLong;
            boolean $i$f$getBuffer = false;
            byte b2 = this_$iv.bufferField.getByte(pos);
            if (!(b2 >= 48 && b2 <= 57 || b2 >= 97 && b2 <= 102 || b2 >= 65 && b2 <= 70)) {
                if (pos != 0) break;
                StringBuilder stringBuilder = new StringBuilder().append("Expected leading [0-9a-fA-F] character but was 0x");
                String string = Integer.toString(b2, CharsKt.checkRadix(16));
                Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                throw new NumberFormatException(stringBuilder.append(string).toString());
            }
            ++pos;
        }
        RealBufferedSource this_$iv = $this$commonReadHexadecimalUnsignedLong;
        boolean $i$f$getBuffer = false;
        return this_$iv.bufferField.readHexadecimalUnsignedLong();
    }

    public static final void commonSkip(@NotNull RealBufferedSource $this$commonSkip, long byteCount) {
        long toSkip;
        Intrinsics.checkNotNullParameter($this$commonSkip, "<this>");
        boolean $i$f$commonSkip = false;
        if (!(!$this$commonSkip.closed)) {
            boolean $i$a$-check--RealBufferedSource$commonSkip$22 = false;
            String $i$a$-check--RealBufferedSource$commonSkip$22 = "closed";
            throw new IllegalStateException($i$a$-check--RealBufferedSource$commonSkip$22.toString());
        }
        for (long byteCount2 = byteCount; byteCount2 > 0L; byteCount2 -= toSkip) {
            RealBufferedSource this_$iv = $this$commonSkip;
            boolean $i$f$getBuffer = false;
            if (this_$iv.bufferField.size() == 0L) {
                this_$iv = $this$commonSkip;
                $i$f$getBuffer = false;
                if ($this$commonSkip.source.read(this_$iv.bufferField, 8192L) == -1L) {
                    throw new EOFException();
                }
            }
            RealBufferedSource this_$iv32 = $this$commonSkip;
            boolean $i$f$getBuffer2 = false;
            long this_$iv32 = this_$iv32.bufferField.size();
            toSkip = Math.min(byteCount2, this_$iv32);
            this_$iv32 = $this$commonSkip;
            $i$f$getBuffer2 = false;
            this_$iv32.bufferField.skip(toSkip);
        }
    }

    public static final long commonIndexOf(@NotNull RealBufferedSource $this$commonIndexOf, byte b2, long fromIndex, long toIndex) {
        Intrinsics.checkNotNullParameter($this$commonIndexOf, "<this>");
        boolean $i$f$commonIndexOf = false;
        long fromIndex2 = 0L;
        fromIndex2 = fromIndex;
        if (!(!$this$commonIndexOf.closed)) {
            boolean $i$a$-check--RealBufferedSource$commonIndexOf$22 = false;
            String $i$a$-check--RealBufferedSource$commonIndexOf$22 = "closed";
            throw new IllegalStateException($i$a$-check--RealBufferedSource$commonIndexOf$22.toString());
        }
        if (!(0L <= fromIndex2 ? fromIndex2 <= toIndex : false)) {
            boolean bl = false;
            String string = "fromIndex=" + fromIndex2 + " toIndex=" + toIndex;
            throw new IllegalArgumentException(string.toString());
        }
        while (fromIndex2 < toIndex) {
            long lastBufferSize;
            block8: {
                block7: {
                    RealBufferedSource this_$iv = $this$commonIndexOf;
                    boolean $i$f$getBuffer = false;
                    long result = this_$iv.bufferField.indexOf(b2, fromIndex2, toIndex);
                    if (result != -1L) {
                        return result;
                    }
                    RealBufferedSource this_$iv2 = $this$commonIndexOf;
                    boolean $i$f$getBuffer2 = false;
                    lastBufferSize = this_$iv2.bufferField.size();
                    if (lastBufferSize >= toIndex) break block7;
                    this_$iv2 = $this$commonIndexOf;
                    $i$f$getBuffer2 = false;
                    if ($this$commonIndexOf.source.read(this_$iv2.bufferField, 8192L) != -1L) break block8;
                }
                return -1L;
            }
            fromIndex2 = Math.max(fromIndex2, lastBufferSize);
        }
        return -1L;
    }

    public static final long commonIndexOf(@NotNull RealBufferedSource $this$commonIndexOf, @NotNull ByteString bytes, long fromIndex) {
        Intrinsics.checkNotNullParameter($this$commonIndexOf, "<this>");
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        boolean $i$f$commonIndexOf = false;
        long fromIndex2 = fromIndex;
        if (!(!$this$commonIndexOf.closed)) {
            boolean bl = false;
            String string = "closed";
            throw new IllegalStateException(string.toString());
        }
        while (true) {
            RealBufferedSource this_$iv = $this$commonIndexOf;
            boolean $i$f$getBuffer = false;
            long result = this_$iv.bufferField.indexOf(bytes, fromIndex2);
            if (result != -1L) {
                return result;
            }
            RealBufferedSource this_$iv2 = $this$commonIndexOf;
            boolean $i$f$getBuffer2 = false;
            long lastBufferSize = this_$iv2.bufferField.size();
            this_$iv2 = $this$commonIndexOf;
            $i$f$getBuffer2 = false;
            if ($this$commonIndexOf.source.read(this_$iv2.bufferField, 8192L) == -1L) {
                return -1L;
            }
            fromIndex2 = Math.max(fromIndex2, lastBufferSize - (long)bytes.size() + 1L);
        }
    }

    public static final long commonIndexOfElement(@NotNull RealBufferedSource $this$commonIndexOfElement, @NotNull ByteString targetBytes, long fromIndex) {
        Intrinsics.checkNotNullParameter($this$commonIndexOfElement, "<this>");
        Intrinsics.checkNotNullParameter(targetBytes, "targetBytes");
        boolean $i$f$commonIndexOfElement = false;
        long fromIndex2 = fromIndex;
        if (!(!$this$commonIndexOfElement.closed)) {
            boolean bl = false;
            String string = "closed";
            throw new IllegalStateException(string.toString());
        }
        while (true) {
            RealBufferedSource this_$iv = $this$commonIndexOfElement;
            boolean $i$f$getBuffer = false;
            long result = this_$iv.bufferField.indexOfElement(targetBytes, fromIndex2);
            if (result != -1L) {
                return result;
            }
            RealBufferedSource this_$iv2 = $this$commonIndexOfElement;
            boolean $i$f$getBuffer2 = false;
            long lastBufferSize = this_$iv2.bufferField.size();
            this_$iv2 = $this$commonIndexOfElement;
            $i$f$getBuffer2 = false;
            if ($this$commonIndexOfElement.source.read(this_$iv2.bufferField, 8192L) == -1L) {
                return -1L;
            }
            fromIndex2 = Math.max(fromIndex2, lastBufferSize);
        }
    }

    public static final boolean commonRangeEquals(@NotNull RealBufferedSource $this$commonRangeEquals, long offset, @NotNull ByteString bytes, int bytesOffset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonRangeEquals, "<this>");
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        boolean $i$f$commonRangeEquals = false;
        if (!(!$this$commonRangeEquals.closed)) {
            boolean $i$a$-check--RealBufferedSource$commonRangeEquals$22 = false;
            String $i$a$-check--RealBufferedSource$commonRangeEquals$22 = "closed";
            throw new IllegalStateException($i$a$-check--RealBufferedSource$commonRangeEquals$22.toString());
        }
        if (offset < 0L || bytesOffset < 0 || byteCount < 0 || bytes.size() - bytesOffset < byteCount) {
            return false;
        }
        for (int i2 = 0; i2 < byteCount; ++i2) {
            long bufferOffset = offset + (long)i2;
            if (!$this$commonRangeEquals.request(bufferOffset + 1L)) {
                return false;
            }
            RealBufferedSource this_$iv = $this$commonRangeEquals;
            boolean $i$f$getBuffer = false;
            if (this_$iv.bufferField.getByte(bufferOffset) == bytes.getByte(bytesOffset + i2)) continue;
            return false;
        }
        return true;
    }

    @NotNull
    public static final BufferedSource commonPeek(@NotNull RealBufferedSource $this$commonPeek) {
        Intrinsics.checkNotNullParameter($this$commonPeek, "<this>");
        boolean $i$f$commonPeek = false;
        return Okio.buffer(new PeekSource($this$commonPeek));
    }

    public static final void commonClose(@NotNull RealBufferedSource $this$commonClose) {
        Intrinsics.checkNotNullParameter($this$commonClose, "<this>");
        boolean $i$f$commonClose = false;
        if ($this$commonClose.closed) {
            return;
        }
        $this$commonClose.closed = true;
        $this$commonClose.source.close();
        RealBufferedSource this_$iv = $this$commonClose;
        boolean $i$f$getBuffer = false;
        this_$iv.bufferField.clear();
    }

    @NotNull
    public static final Timeout commonTimeout(@NotNull RealBufferedSource $this$commonTimeout) {
        Intrinsics.checkNotNullParameter($this$commonTimeout, "<this>");
        boolean $i$f$commonTimeout = false;
        return $this$commonTimeout.source.timeout();
    }

    @NotNull
    public static final String commonToString(@NotNull RealBufferedSource $this$commonToString) {
        Intrinsics.checkNotNullParameter($this$commonToString, "<this>");
        boolean $i$f$commonToString = false;
        return "buffer(" + $this$commonToString.source + ')';
    }
}

