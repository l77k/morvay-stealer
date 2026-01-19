/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.SourceDebugExtension
 */
package okio.internal;

import java.io.EOFException;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.-SegmentedByteString;
import okio.Buffer;
import okio.ByteString;
import okio.Options;
import okio.Segment;
import okio.SegmentPool;
import okio.SegmentedByteString;
import okio.Sink;
import okio.Source;
import okio._JvmPlatformKt;
import okio.internal._Utf8Kt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={2, 1, 0}, k=2, xi=48, d1={"\u0000\u0086\u0001\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\n\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0016\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\n\u001a0\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u0005H\u0000\u001a\u0014\u0010\u000e\u001a\u00020\u000f*\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0000\u001a?\u0010\u0013\u001a\u0002H\u0014\"\u0004\b\u0000\u0010\u0014*\u00020\u00102\u0006\u0010\u0015\u001a\u00020\u00122\u001a\u0010\u0016\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u00010\t\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u0002H\u00140\u0017H\u0080\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0018\u001a\u001e\u0010\u0019\u001a\u00020\u0005*\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u001b2\b\b\u0002\u0010\u001c\u001a\u00020\u0007H\u0000\u001a%\u0010\u001d\u001a\u00020\u0010*\u00020\u00102\u0006\u0010\u001e\u001a\u00020\u00102\u0006\u0010\u001f\u001a\u00020\u00122\u0006\u0010 \u001a\u00020\u0012H\u0080\b\u001a\r\u0010!\u001a\u00020\u0012*\u00020\u0010H\u0080\b\u001a\r\u0010\"\u001a\u00020#*\u00020\u0010H\u0080\b\u001a\r\u0010$\u001a\u00020%*\u00020\u0010H\u0080\b\u001a\r\u0010&\u001a\u00020\u0005*\u00020\u0010H\u0080\b\u001a\r\u0010'\u001a\u00020\u0012*\u00020\u0010H\u0080\b\u001a\u0015\u0010(\u001a\u00020#*\u00020\u00102\u0006\u0010)\u001a\u00020\u0012H\u0080\b\u001a\r\u0010*\u001a\u00020+*\u00020\u0010H\u0080\b\u001a\u0015\u0010,\u001a\u00020+*\u00020\u00102\u0006\u0010 \u001a\u00020\u0012H\u0080\b\u001a)\u0010-\u001a\u00020\u0010*\u00020\u00102\u0006\u0010.\u001a\u00020/2\b\b\u0002\u0010\u001f\u001a\u00020\u00052\b\b\u0002\u0010 \u001a\u00020\u0005H\u0080\b\u001a\u0015\u00100\u001a\u00020\u0010*\u00020\u00102\u0006\u00101\u001a\u00020\u0012H\u0080\b\u001a\u0010\u00102\u001a\u00020\u00052\u0006\u00101\u001a\u00020\u0012H\u0002\u001a\u0015\u00105\u001a\u00020\u0010*\u00020\u00102\u0006\u00101\u001a\u00020\u0012H\u0080\b\u001a\u0015\u00106\u001a\u00020\t*\u00020\u00102\u0006\u00107\u001a\u00020\u0005H\u0080\b\u001a\u0015\u0010-\u001a\u00020\u0010*\u00020\u00102\u0006\u00108\u001a\u00020\u0001H\u0080\b\u001a%\u0010-\u001a\u00020\u0010*\u00020\u00102\u0006\u00108\u001a\u00020\u00012\u0006\u0010\u001f\u001a\u00020\u00052\u0006\u0010 \u001a\u00020\u0005H\u0080\b\u001a\r\u00109\u001a\u00020\u0001*\u00020\u0010H\u0080\b\u001a\u0015\u00109\u001a\u00020\u0001*\u00020\u00102\u0006\u0010 \u001a\u00020\u0012H\u0080\b\u001a\u0015\u0010:\u001a\u00020\u0005*\u00020\u00102\u0006\u0010;\u001a\u00020\u0001H\u0080\b\u001a\u0015\u0010<\u001a\u00020+*\u00020\u00102\u0006\u0010;\u001a\u00020\u0001H\u0080\b\u001a%\u0010:\u001a\u00020\u0005*\u00020\u00102\u0006\u0010;\u001a\u00020\u00012\u0006\u0010\u001f\u001a\u00020\u00052\u0006\u0010 \u001a\u00020\u0005H\u0080\b\u001a\r\u0010?\u001a\u00020\u0012*\u00020\u0010H\u0080\b\u001a\r\u0010@\u001a\u00020\u0012*\u00020\u0010H\u0080\b\u001a\r\u0010A\u001a\u00020/*\u00020\u0010H\u0080\b\u001a\u0015\u0010A\u001a\u00020/*\u00020\u00102\u0006\u0010 \u001a\u00020\u0012H\u0080\b\u001a\u0015\u0010B\u001a\u00020\u0005*\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u001bH\u0080\b\u001a\u001d\u0010<\u001a\u00020+*\u00020\u00102\u0006\u0010;\u001a\u00020\u00102\u0006\u0010 \u001a\u00020\u0012H\u0080\b\u001a\u0015\u0010C\u001a\u00020\u0012*\u00020\u00102\u0006\u0010;\u001a\u00020DH\u0080\b\u001a\u0015\u0010E\u001a\u00020\u000f*\u00020\u00102\u0006\u0010 \u001a\u00020\u0012H\u0080\b\u001a\u000f\u0010F\u001a\u0004\u0018\u00010\u000f*\u00020\u0010H\u0080\b\u001a\u0015\u0010G\u001a\u00020\u000f*\u00020\u00102\u0006\u0010H\u001a\u00020\u0012H\u0080\b\u001a\r\u0010I\u001a\u00020\u0005*\u00020\u0010H\u0080\b\u001a%\u0010J\u001a\u00020\u0010*\u00020\u00102\u0006\u0010K\u001a\u00020\u000f2\u0006\u0010L\u001a\u00020\u00052\u0006\u0010M\u001a\u00020\u0005H\u0080\b\u001a\u0015\u0010N\u001a\u00020\u0010*\u00020\u00102\u0006\u0010O\u001a\u00020\u0005H\u0080\b\u001a\u0015\u0010P\u001a\u00020\u0012*\u00020\u00102\u0006\u00108\u001a\u00020QH\u0080\b\u001a\u001d\u0010-\u001a\u00020\u0010*\u00020\u00102\u0006\u00108\u001a\u00020Q2\u0006\u0010 \u001a\u00020\u0012H\u0080\b\u001a\u0015\u0010R\u001a\u00020\u0010*\u00020\u00102\u0006\u0010S\u001a\u00020\u0005H\u0080\b\u001a\u0015\u0010T\u001a\u00020\u0010*\u00020\u00102\u0006\u0010U\u001a\u00020\u0005H\u0080\b\u001a\u0015\u0010V\u001a\u00020\u0010*\u00020\u00102\u0006\u0010W\u001a\u00020\u0005H\u0080\b\u001a\u0015\u0010X\u001a\u00020\u0010*\u00020\u00102\u0006\u00101\u001a\u00020\u0012H\u0080\b\u001a\u001d\u0010-\u001a\u00020+*\u00020\u00102\u0006\u00108\u001a\u00020\u00102\u0006\u0010 \u001a\u00020\u0012H\u0080\b\u001a\u001d\u0010:\u001a\u00020\u0012*\u00020\u00102\u0006\u0010;\u001a\u00020\u00102\u0006\u0010 \u001a\u00020\u0012H\u0080\b\u001a%\u0010Y\u001a\u00020\u0012*\u00020\u00102\u0006\u0010S\u001a\u00020#2\u0006\u0010\u0015\u001a\u00020\u00122\u0006\u0010Z\u001a\u00020\u0012H\u0080\b\u001a\u001d\u0010Y\u001a\u00020\u0012*\u00020\u00102\u0006\u0010\u000b\u001a\u00020/2\u0006\u0010\u0015\u001a\u00020\u0012H\u0080\b\u001a\u001d\u0010[\u001a\u00020\u0012*\u00020\u00102\u0006\u0010\\\u001a\u00020/2\u0006\u0010\u0015\u001a\u00020\u0012H\u0080\b\u001a-\u0010]\u001a\u00020\u0007*\u00020\u00102\u0006\u0010\u001f\u001a\u00020\u00122\u0006\u0010\u000b\u001a\u00020/2\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010 \u001a\u00020\u0005H\u0080\b\u001a\u0017\u0010^\u001a\u00020\u0007*\u00020\u00102\b\u0010_\u001a\u0004\u0018\u00010`H\u0080\b\u001a\r\u0010a\u001a\u00020\u0005*\u00020\u0010H\u0080\b\u001a\r\u0010b\u001a\u00020\u0010*\u00020\u0010H\u0080\b\u001a\r\u0010c\u001a\u00020/*\u00020\u0010H\u0080\b\u001a\u0015\u0010c\u001a\u00020/*\u00020\u00102\u0006\u0010 \u001a\u00020\u0005H\u0080\b\u001a\u0014\u0010d\u001a\u00020e*\u00020\u00102\u0006\u0010f\u001a\u00020eH\u0000\u001a\u0014\u0010g\u001a\u00020e*\u00020\u00102\u0006\u0010f\u001a\u00020eH\u0000\u001a\r\u0010h\u001a\u00020\u0005*\u00020eH\u0080\b\u001a\u0015\u0010i\u001a\u00020\u0005*\u00020e2\u0006\u0010\u001f\u001a\u00020\u0012H\u0080\b\u001a\u0015\u0010j\u001a\u00020\u0012*\u00020e2\u0006\u0010k\u001a\u00020\u0012H\u0080\b\u001a\u0015\u0010l\u001a\u00020\u0012*\u00020e2\u0006\u0010m\u001a\u00020\u0005H\u0080\b\u001a\r\u0010n\u001a\u00020+*\u00020eH\u0080\b\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0080T\u00a2\u0006\u0002\n\u0000\"\u000e\u00103\u001a\u000204X\u0082\u0004\u00a2\u0006\u0002\n\u0000\"\u000e\u0010=\u001a\u00020\u0012X\u0080T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010>\u001a\u00020\u0012X\u0080T\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006o"}, d2={"HEX_DIGIT_BYTES", "", "getHEX_DIGIT_BYTES", "()[B", "SEGMENTING_THRESHOLD", "", "rangeEquals", "", "segment", "Lokio/Segment;", "segmentPos", "bytes", "bytesOffset", "bytesLimit", "readUtf8Line", "", "Lokio/Buffer;", "newline", "", "seek", "T", "fromIndex", "lambda", "Lkotlin/Function2;", "(Lokio/Buffer;JLkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "selectPrefix", "options", "Lokio/Options;", "selectTruncated", "commonCopyTo", "out", "offset", "byteCount", "commonCompleteSegmentByteCount", "commonReadByte", "", "commonReadShort", "", "commonReadInt", "commonReadLong", "commonGet", "pos", "commonClear", "", "commonSkip", "commonWrite", "byteString", "Lokio/ByteString;", "commonWriteDecimalLong", "v", "countDigitsIn", "DigitCountToLargestValue", "", "commonWriteHexadecimalUnsignedLong", "commonWritableSegment", "minimumCapacity", "source", "commonReadByteArray", "commonRead", "sink", "commonReadFully", "OVERFLOW_ZONE", "OVERFLOW_DIGIT_START", "commonReadDecimalLong", "commonReadHexadecimalUnsignedLong", "commonReadByteString", "commonSelect", "commonReadAll", "Lokio/Sink;", "commonReadUtf8", "commonReadUtf8Line", "commonReadUtf8LineStrict", "limit", "commonReadUtf8CodePoint", "commonWriteUtf8", "string", "beginIndex", "endIndex", "commonWriteUtf8CodePoint", "codePoint", "commonWriteAll", "Lokio/Source;", "commonWriteByte", "b", "commonWriteShort", "s", "commonWriteInt", "i", "commonWriteLong", "commonIndexOf", "toIndex", "commonIndexOfElement", "targetBytes", "commonRangeEquals", "commonEquals", "other", "", "commonHashCode", "commonCopy", "commonSnapshot", "commonReadUnsafe", "Lokio/Buffer$UnsafeCursor;", "unsafeCursor", "commonReadAndWriteUnsafe", "commonNext", "commonSeek", "commonResizeBuffer", "newSize", "commonExpandBuffer", "minByteCount", "commonClose", "okio"})
@JvmName(name="-Buffer")
@SourceDebugExtension(value={"SMAP\nBuffer.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Buffer.kt\nokio/internal/-Buffer\n+ 2 Util.kt\nokio/-SegmentedByteString\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,1700:1\n110#1,20:1723\n110#1,20:1756\n110#1:1776\n112#1,18:1778\n110#1,20:1796\n73#2:1701\n73#2:1702\n73#2:1703\n73#2:1704\n73#2:1705\n73#2:1706\n73#2:1707\n73#2:1708\n73#2:1709\n73#2:1710\n73#2:1711\n73#2:1712\n82#2:1713\n82#2:1714\n76#2:1715\n76#2:1716\n76#2:1717\n76#2:1718\n76#2:1719\n76#2:1720\n76#2:1721\n76#2:1722\n85#2:1743\n88#2:1745\n73#2:1746\n73#2:1747\n73#2:1748\n73#2:1749\n73#2:1750\n73#2:1751\n73#2:1752\n73#2:1753\n73#2:1754\n73#2:1755\n88#2:1777\n85#2:1816\n1#3:1744\n*S KotlinDebug\n*F\n+ 1 Buffer.kt\nokio/internal/-Buffer\n*L\n413#1:1723,20\n1262#1:1756,20\n1293#1:1776\n1293#1:1778,18\n1327#1:1796,20\n176#1:1701\n200#1:1702\n319#1:1703\n324#1:1704\n347#1:1705\n348#1:1706\n349#1:1707\n350#1:1708\n356#1:1709\n357#1:1710\n358#1:1711\n359#1:1712\n383#1:1713\n384#1:1714\n390#1:1715\n391#1:1716\n392#1:1717\n393#1:1718\n394#1:1719\n395#1:1720\n396#1:1721\n397#1:1722\n425#1:1743\n858#1:1745\n876#1:1746\n878#1:1747\n882#1:1748\n884#1:1749\n888#1:1750\n890#1:1751\n894#1:1752\n896#1:1753\n916#1:1754\n919#1:1755\n1306#1:1777\n1646#1:1816\n*E\n"})
public final class -Buffer {
    @NotNull
    private static final byte[] HEX_DIGIT_BYTES = _JvmPlatformKt.asUtf8ToByteArray("0123456789abcdef");
    public static final int SEGMENTING_THRESHOLD = 4096;
    @NotNull
    private static final long[] DigitCountToLargestValue;
    public static final long OVERFLOW_ZONE = -922337203685477580L;
    public static final long OVERFLOW_DIGIT_START = -7L;

    @NotNull
    public static final byte[] getHEX_DIGIT_BYTES() {
        return HEX_DIGIT_BYTES;
    }

    public static final boolean rangeEquals(@NotNull Segment segment, int segmentPos, @NotNull byte[] bytes, int bytesOffset, int bytesLimit) {
        Intrinsics.checkNotNullParameter(segment, "segment");
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        Segment segment2 = segment;
        int segmentPos2 = segmentPos;
        int segmentLimit = segment2.limit;
        byte[] data = segment2.data;
        for (int i2 = bytesOffset; i2 < bytesLimit; ++i2) {
            if (segmentPos2 == segmentLimit) {
                Intrinsics.checkNotNull(segment2.next);
                data = segment2.data;
                segmentPos2 = segment2.pos;
                segmentLimit = segment2.limit;
            }
            if (data[segmentPos2] != bytes[i2]) {
                return false;
            }
            ++segmentPos2;
        }
        return true;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final String readUtf8Line(@NotNull Buffer $this$readUtf8Line, long newline) {
        String string;
        Intrinsics.checkNotNullParameter($this$readUtf8Line, "<this>");
        if (newline > 0L && $this$readUtf8Line.getByte(newline - 1L) == 13) {
            String result = $this$readUtf8Line.readUtf8(newline - 1L);
            $this$readUtf8Line.skip(2L);
            string = result;
        } else {
            void var3_3;
            String result = $this$readUtf8Line.readUtf8(newline);
            $this$readUtf8Line.skip(1L);
            string = var3_3;
        }
        return string;
    }

    public static final <T> T seek(@NotNull Buffer $this$seek, long fromIndex, @NotNull Function2<? super Segment, ? super Long, ? extends T> lambda) {
        long nextOffset;
        Intrinsics.checkNotNullParameter($this$seek, "<this>");
        Intrinsics.checkNotNullParameter(lambda, "lambda");
        boolean $i$f$seek = false;
        Segment segment = $this$seek.head;
        if (segment == null) {
            return lambda.invoke(null, -1L);
        }
        Segment s2 = segment;
        if ($this$seek.size() - fromIndex < fromIndex) {
            long offset;
            for (offset = $this$seek.size(); offset > fromIndex; offset -= (long)(s2.limit - s2.pos)) {
                Intrinsics.checkNotNull(s2.prev);
            }
            return lambda.invoke(s2, offset);
        }
        long offset = 0L;
        while ((nextOffset = offset + (long)(s2.limit - s2.pos)) <= fromIndex) {
            Intrinsics.checkNotNull(s2.next);
            offset = nextOffset;
        }
        return lambda.invoke(s2, offset);
    }

    /*
     * WARNING - void declaration
     */
    public static final int selectPrefix(@NotNull Buffer $this$selectPrefix, @NotNull Options options, boolean selectTruncated) {
        Segment head;
        Intrinsics.checkNotNullParameter($this$selectPrefix, "<this>");
        Intrinsics.checkNotNullParameter(options, "options");
        Segment segment = $this$selectPrefix.head;
        if (segment == null) {
            return selectTruncated ? -2 : -1;
        }
        Segment s2 = head = segment;
        byte[] data = head.data;
        int pos = head.pos;
        int limit = head.limit;
        int[] trie = options.getTrie$okio();
        int triePos = 0;
        int prefixIndex = -1;
        block0: while (true) {
            int n2;
            int possiblePrefixIndex;
            int scanOrSelect = trie[triePos++];
            if ((possiblePrefixIndex = trie[triePos++]) != -1) {
                prefixIndex = possiblePrefixIndex;
            }
            int nextStep = 0;
            if (s2 == null) break;
            if (scanOrSelect < 0) {
                boolean scanComplete;
                int scanByteCount = -1 * scanOrSelect;
                int trieLimit = triePos + scanByteCount;
                do {
                    void $this$and$iv;
                    byte by = data[pos++];
                    int other$iv = 255;
                    boolean $i$f$and = false;
                    n2 = $this$and$iv & other$iv;
                    if (n2 != trie[triePos++]) {
                        return prefixIndex;
                    }
                    boolean bl = scanComplete = triePos == trieLimit;
                    if (pos != limit) continue;
                    Segment segment2 = s2;
                    Intrinsics.checkNotNull(segment2);
                    Intrinsics.checkNotNull(segment2.next);
                    pos = s2.pos;
                    data = s2.data;
                    limit = s2.limit;
                    if (s2 != head) continue;
                    if (!scanComplete) break block0;
                    s2 = null;
                } while (!scanComplete);
                nextStep = trie[triePos];
            } else {
                void $this$and$iv;
                int selectChoiceCount = scanOrSelect;
                n2 = data[pos++];
                int other$iv = 255;
                boolean $i$f$and = false;
                byte = $this$and$iv & other$iv;
                int selectLimit = triePos + selectChoiceCount;
                while (true) {
                    if (triePos == selectLimit) {
                        return prefixIndex;
                    }
                    if (byte == trie[triePos]) break;
                    ++triePos;
                }
                nextStep = trie[triePos + selectChoiceCount];
                if (pos == limit) {
                    Intrinsics.checkNotNull(s2.next);
                    pos = s2.pos;
                    data = s2.data;
                    limit = s2.limit;
                    if (s2 == head) {
                        s2 = null;
                    }
                }
            }
            if (nextStep >= 0) {
                return nextStep;
            }
            triePos = -nextStep;
        }
        if (selectTruncated) {
            return -2;
        }
        return prefixIndex;
    }

    public static /* synthetic */ int selectPrefix$default(Buffer buffer, Options options, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        return -Buffer.selectPrefix(buffer, options, bl);
    }

    @NotNull
    public static final Buffer commonCopyTo(@NotNull Buffer $this$commonCopyTo, @NotNull Buffer out, long offset, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonCopyTo, "<this>");
        Intrinsics.checkNotNullParameter(out, "out");
        boolean $i$f$commonCopyTo = false;
        long offset2 = offset;
        long byteCount2 = byteCount;
        -SegmentedByteString.checkOffsetAndCount($this$commonCopyTo.size(), offset2, byteCount2);
        if (byteCount2 == 0L) {
            return $this$commonCopyTo;
        }
        out.setSize$okio(out.size() + byteCount2);
        Segment s2 = $this$commonCopyTo.head;
        while (true) {
            Segment segment = s2;
            Intrinsics.checkNotNull(segment);
            if (offset2 < (long)(segment.limit - s2.pos)) break;
            offset2 -= (long)(s2.limit - s2.pos);
            s2 = s2.next;
        }
        while (byteCount2 > 0L) {
            Segment segment = s2;
            Intrinsics.checkNotNull(segment);
            Segment copy = segment.sharedCopy();
            copy.pos += (int)offset2;
            copy.limit = Math.min(copy.pos + (int)byteCount2, copy.limit);
            if (out.head == null) {
                out.head = copy.next = (copy.prev = copy);
            } else {
                Segment segment2 = out.head;
                Intrinsics.checkNotNull(segment2);
                Segment segment3 = segment2.prev;
                Intrinsics.checkNotNull(segment3);
                segment3.push(copy);
            }
            byteCount2 -= (long)(copy.limit - copy.pos);
            offset2 = 0L;
            s2 = s2.next;
        }
        return $this$commonCopyTo;
    }

    public static final long commonCompleteSegmentByteCount(@NotNull Buffer $this$commonCompleteSegmentByteCount) {
        Intrinsics.checkNotNullParameter($this$commonCompleteSegmentByteCount, "<this>");
        boolean $i$f$commonCompleteSegmentByteCount = false;
        long result = $this$commonCompleteSegmentByteCount.size();
        if (result == 0L) {
            return 0L;
        }
        Segment segment = $this$commonCompleteSegmentByteCount.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment.prev;
        Intrinsics.checkNotNull(segment2);
        Segment tail = segment2;
        if (tail.limit < 8192 && tail.owner) {
            result -= (long)(tail.limit - tail.pos);
        }
        return result;
    }

    public static final byte commonReadByte(@NotNull Buffer $this$commonReadByte) {
        Intrinsics.checkNotNullParameter($this$commonReadByte, "<this>");
        boolean $i$f$commonReadByte = false;
        if ($this$commonReadByte.size() == 0L) {
            throw new EOFException();
        }
        Segment segment = $this$commonReadByte.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment;
        int pos = segment2.pos;
        int limit = segment2.limit;
        byte[] data = segment2.data;
        byte b2 = data[pos++];
        $this$commonReadByte.setSize$okio($this$commonReadByte.size() - 1L);
        if (pos == limit) {
            $this$commonReadByte.head = segment2.pop();
            SegmentPool.recycle(segment2);
        } else {
            segment2.pos = pos;
        }
        return b2;
    }

    public static final short commonReadShort(@NotNull Buffer $this$commonReadShort) {
        byte $this$and$iv;
        Intrinsics.checkNotNullParameter($this$commonReadShort, "<this>");
        boolean $i$f$commonReadShort = false;
        if ($this$commonReadShort.size() < 2L) {
            throw new EOFException();
        }
        Segment segment = $this$commonReadShort.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment;
        int pos = segment2.pos;
        int limit = segment2.limit;
        if (limit - pos < 2) {
            byte $this$and$iv2;
            byte by = $this$commonReadShort.readByte();
            int other$iv = 255;
            boolean $i$f$and = false;
            int n2 = ($this$and$iv2 & other$iv) << 8;
            $this$and$iv2 = $this$commonReadShort.readByte();
            other$iv = 255;
            $i$f$and = false;
            int s2 = n2 | $this$and$iv2 & other$iv;
            return (short)s2;
        }
        byte[] data = segment2.data;
        byte other$iv = data[pos++];
        int other$iv2 = 255;
        boolean $i$f$and = false;
        int n3 = ($this$and$iv & other$iv2) << 8;
        $this$and$iv = data[pos++];
        other$iv2 = 255;
        $i$f$and = false;
        int s3 = n3 | $this$and$iv & other$iv2;
        $this$commonReadShort.setSize$okio($this$commonReadShort.size() - 2L);
        if (pos == limit) {
            $this$commonReadShort.head = segment2.pop();
            SegmentPool.recycle(segment2);
        } else {
            segment2.pos = pos;
        }
        return (short)s3;
    }

    public static final int commonReadInt(@NotNull Buffer $this$commonReadInt) {
        byte $this$and$iv;
        Intrinsics.checkNotNullParameter($this$commonReadInt, "<this>");
        boolean $i$f$commonReadInt = false;
        if ($this$commonReadInt.size() < 4L) {
            throw new EOFException();
        }
        Segment segment = $this$commonReadInt.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment;
        int pos = segment2.pos;
        int limit = segment2.limit;
        if ((long)(limit - pos) < 4L) {
            byte $this$and$iv2;
            byte by = $this$commonReadInt.readByte();
            int other$iv = 255;
            boolean $i$f$and = false;
            int n2 = ($this$and$iv2 & other$iv) << 24;
            $this$and$iv2 = $this$commonReadInt.readByte();
            other$iv = 255;
            $i$f$and = false;
            int n3 = n2 | ($this$and$iv2 & other$iv) << 16;
            $this$and$iv2 = $this$commonReadInt.readByte();
            other$iv = 255;
            $i$f$and = false;
            int n4 = n3 | ($this$and$iv2 & other$iv) << 8;
            $this$and$iv2 = $this$commonReadInt.readByte();
            other$iv = 255;
            $i$f$and = false;
            return n4 | $this$and$iv2 & other$iv;
        }
        byte[] data = segment2.data;
        byte $i$f$and = data[pos++];
        int other$iv = 255;
        boolean $i$f$and2 = false;
        int n5 = ($this$and$iv & other$iv) << 24;
        $this$and$iv = data[pos++];
        other$iv = 255;
        $i$f$and2 = false;
        int n6 = n5 | ($this$and$iv & other$iv) << 16;
        $this$and$iv = data[pos++];
        other$iv = 255;
        $i$f$and2 = false;
        int n7 = n6 | ($this$and$iv & other$iv) << 8;
        $this$and$iv = data[pos++];
        other$iv = 255;
        $i$f$and2 = false;
        int i2 = n7 | $this$and$iv & other$iv;
        $this$commonReadInt.setSize$okio($this$commonReadInt.size() - 4L);
        if (pos == limit) {
            $this$commonReadInt.head = segment2.pop();
            SegmentPool.recycle(segment2);
        } else {
            segment2.pos = pos;
        }
        return i2;
    }

    public static final long commonReadLong(@NotNull Buffer $this$commonReadLong) {
        byte $this$and$iv;
        Intrinsics.checkNotNullParameter($this$commonReadLong, "<this>");
        boolean $i$f$commonReadLong = false;
        if ($this$commonReadLong.size() < 8L) {
            throw new EOFException();
        }
        Segment segment = $this$commonReadLong.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment;
        int pos = segment2.pos;
        int limit = segment2.limit;
        if ((long)(limit - pos) < 8L) {
            int $this$and$iv2;
            int n2 = $this$commonReadLong.readInt();
            long other$iv = 0xFFFFFFFFL;
            boolean $i$f$and = false;
            long l2 = ((long)$this$and$iv2 & other$iv) << 32;
            $this$and$iv2 = $this$commonReadLong.readInt();
            other$iv = 0xFFFFFFFFL;
            $i$f$and = false;
            return l2 | (long)$this$and$iv2 & other$iv;
        }
        byte[] data = segment2.data;
        byte $i$f$and = data[pos++];
        long other$iv = 255L;
        boolean $i$f$and2 = false;
        long l3 = ((long)$this$and$iv & other$iv) << 56;
        $this$and$iv = data[pos++];
        other$iv = 255L;
        $i$f$and2 = false;
        long l4 = l3 | ((long)$this$and$iv & other$iv) << 48;
        $this$and$iv = data[pos++];
        other$iv = 255L;
        $i$f$and2 = false;
        long l5 = l4 | ((long)$this$and$iv & other$iv) << 40;
        $this$and$iv = data[pos++];
        other$iv = 255L;
        $i$f$and2 = false;
        long l6 = l5 | ((long)$this$and$iv & other$iv) << 32;
        $this$and$iv = data[pos++];
        other$iv = 255L;
        $i$f$and2 = false;
        long l7 = l6 | ((long)$this$and$iv & other$iv) << 24;
        $this$and$iv = data[pos++];
        other$iv = 255L;
        $i$f$and2 = false;
        long l8 = l7 | ((long)$this$and$iv & other$iv) << 16;
        $this$and$iv = data[pos++];
        other$iv = 255L;
        $i$f$and2 = false;
        long l9 = l8 | ((long)$this$and$iv & other$iv) << 8;
        $this$and$iv = data[pos++];
        other$iv = 255L;
        $i$f$and2 = false;
        long v2 = l9 | (long)$this$and$iv & other$iv;
        $this$commonReadLong.setSize$okio($this$commonReadLong.size() - 8L);
        if (pos == limit) {
            $this$commonReadLong.head = segment2.pop();
            SegmentPool.recycle(segment2);
        } else {
            segment2.pos = pos;
        }
        return v2;
    }

    /*
     * WARNING - void declaration
     */
    public static final byte commonGet(@NotNull Buffer $this$commonGet, long pos) {
        void offset;
        long nextOffset$iv;
        Intrinsics.checkNotNullParameter($this$commonGet, "<this>");
        boolean $i$f$commonGet = false;
        -SegmentedByteString.checkOffsetAndCount($this$commonGet.size(), pos, 1L);
        Buffer $this$seek$iv = $this$commonGet;
        boolean $i$f$seek = false;
        Segment segment = $this$seek$iv.head;
        if (segment == null) {
            void offset2;
            long l2 = -1L;
            Object s2 = null;
            boolean bl = false;
            Object v1 = s2;
            Intrinsics.checkNotNull(v1);
            return v1.data[(int)((long)s2.pos + pos - offset2)];
        }
        Segment s$iv = segment;
        if ($this$seek$iv.size() - pos < pos) {
            void offset3;
            long offset$iv;
            for (offset$iv = $this$seek$iv.size(); offset$iv > pos; offset$iv -= (long)(s$iv.limit - s$iv.pos)) {
                Intrinsics.checkNotNull(s$iv.prev);
            }
            long l3 = offset$iv;
            Segment s3 = s$iv;
            boolean bl = false;
            Segment segment2 = s3;
            Intrinsics.checkNotNull(segment2);
            return segment2.data[(int)((long)s3.pos + pos - offset3)];
        }
        long offset$iv = 0L;
        while ((nextOffset$iv = offset$iv + (long)(s$iv.limit - s$iv.pos)) <= pos) {
            Intrinsics.checkNotNull(s$iv.next);
            offset$iv = nextOffset$iv;
        }
        long l4 = offset$iv;
        Segment s4 = s$iv;
        boolean bl = false;
        Segment segment3 = s4;
        Intrinsics.checkNotNull(segment3);
        return segment3.data[(int)((long)s4.pos + pos - offset)];
    }

    public static final void commonClear(@NotNull Buffer $this$commonClear) {
        Intrinsics.checkNotNullParameter($this$commonClear, "<this>");
        boolean $i$f$commonClear = false;
        $this$commonClear.skip($this$commonClear.size());
    }

    public static final void commonSkip(@NotNull Buffer $this$commonSkip, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonSkip, "<this>");
        boolean $i$f$commonSkip = false;
        long byteCount2 = byteCount;
        while (byteCount2 > 0L) {
            Segment head;
            if ($this$commonSkip.head == null) {
                throw new EOFException();
            }
            int b$iv = head.limit - head.pos;
            boolean $i$f$minOf = false;
            int toSkip = (int)Math.min(byteCount2, (long)b$iv);
            $this$commonSkip.setSize$okio($this$commonSkip.size() - (long)toSkip);
            byteCount2 -= (long)toSkip;
            head.pos += toSkip;
            if (head.pos != head.limit) continue;
            $this$commonSkip.head = head.pop();
            SegmentPool.recycle(head);
        }
    }

    @NotNull
    public static final Buffer commonWrite(@NotNull Buffer $this$commonWrite, @NotNull ByteString byteString, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonWrite, "<this>");
        Intrinsics.checkNotNullParameter(byteString, "byteString");
        boolean $i$f$commonWrite = false;
        byteString.write$okio($this$commonWrite, offset, byteCount);
        return $this$commonWrite;
    }

    public static /* synthetic */ Buffer commonWrite$default(Buffer $this$commonWrite_u24default, ByteString byteString, int offset, int byteCount, int n2, Object object) {
        if ((n2 & 2) != 0) {
            offset = 0;
        }
        if ((n2 & 4) != 0) {
            byteCount = byteString.size();
        }
        Intrinsics.checkNotNullParameter($this$commonWrite_u24default, "<this>");
        Intrinsics.checkNotNullParameter(byteString, "byteString");
        boolean $i$f$commonWrite = false;
        byteString.write$okio($this$commonWrite_u24default, offset, byteCount);
        return $this$commonWrite_u24default;
    }

    @NotNull
    public static final Buffer commonWriteDecimalLong(@NotNull Buffer $this$commonWriteDecimalLong, long v2) {
        Intrinsics.checkNotNullParameter($this$commonWriteDecimalLong, "<this>");
        boolean $i$f$commonWriteDecimalLong = false;
        long v3 = v2;
        if (v3 == 0L) {
            return $this$commonWriteDecimalLong.writeByte(48);
        }
        boolean negative = false;
        if (v3 < 0L) {
            if ((v3 = -v3) < 0L) {
                return $this$commonWriteDecimalLong.writeUtf8("-9223372036854775808");
            }
            negative = true;
        }
        int width = -Buffer.countDigitsIn(v3);
        if (negative) {
            ++width;
        }
        Segment tail = $this$commonWriteDecimalLong.writableSegment$okio(width);
        byte[] data = tail.data;
        int pos = tail.limit + width;
        while (v3 != 0L) {
            int digit = (int)(v3 % (long)10);
            data[--pos] = -Buffer.getHEX_DIGIT_BYTES()[digit];
            v3 /= (long)10;
        }
        if (negative) {
            data[--pos] = 45;
        }
        tail.limit += width;
        $this$commonWriteDecimalLong.setSize$okio($this$commonWriteDecimalLong.size() + (long)width);
        return $this$commonWriteDecimalLong;
    }

    private static final int countDigitsIn(long v2) {
        int guess;
        return guess + (v2 > DigitCountToLargestValue[guess = (64 - Long.numberOfLeadingZeros(v2)) * 10 >>> 5] ? 1 : 0);
    }

    @NotNull
    public static final Buffer commonWriteHexadecimalUnsignedLong(@NotNull Buffer $this$commonWriteHexadecimalUnsignedLong, long v2) {
        Intrinsics.checkNotNullParameter($this$commonWriteHexadecimalUnsignedLong, "<this>");
        boolean $i$f$commonWriteHexadecimalUnsignedLong = false;
        long v3 = v2;
        if (v3 == 0L) {
            return $this$commonWriteHexadecimalUnsignedLong.writeByte(48);
        }
        long x2 = v3;
        x2 |= x2 >>> 1;
        x2 |= x2 >>> 2;
        x2 |= x2 >>> 4;
        x2 |= x2 >>> 8;
        x2 |= x2 >>> 16;
        x2 |= x2 >>> 32;
        x2 -= x2 >>> 1 & 0x5555555555555555L;
        x2 = (x2 >>> 2 & 0x3333333333333333L) + (x2 & 0x3333333333333333L);
        x2 = (x2 >>> 4) + x2 & 0xF0F0F0F0F0F0F0FL;
        x2 += x2 >>> 8;
        x2 += x2 >>> 16;
        x2 = (x2 & 0x3FL) + (x2 >>> 32 & 0x3FL);
        int width = (int)((x2 + (long)3) / (long)4);
        Segment tail = $this$commonWriteHexadecimalUnsignedLong.writableSegment$okio(width);
        byte[] data = tail.data;
        int start = tail.limit;
        for (int pos = tail.limit + width - 1; pos >= start; --pos) {
            data[pos] = -Buffer.getHEX_DIGIT_BYTES()[(int)(v3 & 0xFL)];
            v3 >>>= 4;
        }
        tail.limit += width;
        $this$commonWriteHexadecimalUnsignedLong.setSize$okio($this$commonWriteHexadecimalUnsignedLong.size() + (long)width);
        return $this$commonWriteHexadecimalUnsignedLong;
    }

    @NotNull
    public static final Segment commonWritableSegment(@NotNull Buffer $this$commonWritableSegment, int minimumCapacity) {
        Segment tail;
        Intrinsics.checkNotNullParameter($this$commonWritableSegment, "<this>");
        boolean $i$f$commonWritableSegment = false;
        if (!(minimumCapacity >= 1 && minimumCapacity <= 8192)) {
            boolean bl = false;
            String string = "unexpected capacity";
            throw new IllegalArgumentException(string.toString());
        }
        if ($this$commonWritableSegment.head == null) {
            Segment result;
            $this$commonWritableSegment.head = result = SegmentPool.take();
            result.prev = result;
            result.next = result;
            return result;
        }
        Segment segment = $this$commonWritableSegment.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = tail = segment.prev;
        Intrinsics.checkNotNull(segment2);
        if (segment2.limit + minimumCapacity > 8192 || !tail.owner) {
            tail = tail.push(SegmentPool.take());
        }
        return tail;
    }

    @NotNull
    public static final Buffer commonWrite(@NotNull Buffer $this$commonWrite, @NotNull byte[] source2) {
        Intrinsics.checkNotNullParameter($this$commonWrite, "<this>");
        Intrinsics.checkNotNullParameter(source2, "source");
        boolean $i$f$commonWrite = false;
        return $this$commonWrite.write(source2, 0, source2.length);
    }

    @NotNull
    public static final Buffer commonWrite(@NotNull Buffer $this$commonWrite, @NotNull byte[] source2, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonWrite, "<this>");
        Intrinsics.checkNotNullParameter(source2, "source");
        boolean $i$f$commonWrite = false;
        int offset2 = offset;
        -SegmentedByteString.checkOffsetAndCount(source2.length, offset2, byteCount);
        int limit = offset2 + byteCount;
        while (offset2 < limit) {
            Segment tail = $this$commonWrite.writableSegment$okio(1);
            int toCopy = Math.min(limit - offset2, 8192 - tail.limit);
            ArraysKt.copyInto(source2, tail.data, tail.limit, offset2, offset2 + toCopy);
            offset2 += toCopy;
            tail.limit += toCopy;
        }
        $this$commonWrite.setSize$okio($this$commonWrite.size() + (long)byteCount);
        return $this$commonWrite;
    }

    @NotNull
    public static final byte[] commonReadByteArray(@NotNull Buffer $this$commonReadByteArray) {
        Intrinsics.checkNotNullParameter($this$commonReadByteArray, "<this>");
        boolean $i$f$commonReadByteArray = false;
        return $this$commonReadByteArray.readByteArray($this$commonReadByteArray.size());
    }

    @NotNull
    public static final byte[] commonReadByteArray(@NotNull Buffer $this$commonReadByteArray, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonReadByteArray, "<this>");
        boolean $i$f$commonReadByteArray = false;
        if (!(byteCount >= 0L && byteCount <= Integer.MAX_VALUE)) {
            boolean bl = false;
            String string = "byteCount: " + byteCount;
            throw new IllegalArgumentException(string.toString());
        }
        if ($this$commonReadByteArray.size() < byteCount) {
            throw new EOFException();
        }
        byte[] result = new byte[(int)byteCount];
        $this$commonReadByteArray.readFully(result);
        return result;
    }

    public static final int commonRead(@NotNull Buffer $this$commonRead, @NotNull byte[] sink2) {
        Intrinsics.checkNotNullParameter($this$commonRead, "<this>");
        Intrinsics.checkNotNullParameter(sink2, "sink");
        boolean $i$f$commonRead = false;
        return $this$commonRead.read(sink2, 0, sink2.length);
    }

    public static final void commonReadFully(@NotNull Buffer $this$commonReadFully, @NotNull byte[] sink2) {
        int read;
        Intrinsics.checkNotNullParameter($this$commonReadFully, "<this>");
        Intrinsics.checkNotNullParameter(sink2, "sink");
        boolean $i$f$commonReadFully = false;
        for (int offset = 0; offset < sink2.length; offset += read) {
            read = $this$commonReadFully.read(sink2, offset, sink2.length - offset);
            if (read != -1) continue;
            throw new EOFException();
        }
    }

    public static final int commonRead(@NotNull Buffer $this$commonRead, @NotNull byte[] sink2, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonRead, "<this>");
        Intrinsics.checkNotNullParameter(sink2, "sink");
        boolean $i$f$commonRead = false;
        -SegmentedByteString.checkOffsetAndCount(sink2.length, offset, byteCount);
        Segment segment = $this$commonRead.head;
        if (segment == null) {
            return -1;
        }
        Segment s2 = segment;
        int toCopy = Math.min(byteCount, s2.limit - s2.pos);
        ArraysKt.copyInto(s2.data, sink2, offset, s2.pos, s2.pos + toCopy);
        s2.pos += toCopy;
        $this$commonRead.setSize$okio($this$commonRead.size() - (long)toCopy);
        if (s2.pos == s2.limit) {
            $this$commonRead.head = s2.pop();
            SegmentPool.recycle(s2);
        }
        return toCopy;
    }

    public static final long commonReadDecimalLong(@NotNull Buffer $this$commonReadDecimalLong) {
        int minimumSeen;
        Intrinsics.checkNotNullParameter($this$commonReadDecimalLong, "<this>");
        boolean $i$f$commonReadDecimalLong = false;
        if ($this$commonReadDecimalLong.size() == 0L) {
            throw new EOFException();
        }
        long value = 0L;
        int seen = 0;
        boolean negative = false;
        boolean done = false;
        long overflowDigit = -7L;
        do {
            Segment segment;
            Intrinsics.checkNotNull($this$commonReadDecimalLong.head);
            byte[] data = segment.data;
            int pos = segment.pos;
            int limit = segment.limit;
            while (pos < limit) {
                byte b2 = data[pos];
                if (b2 >= 48 && b2 <= 57) {
                    int digit = 48 - b2;
                    if (value < -922337203685477580L || value == -922337203685477580L && (long)digit < overflowDigit) {
                        Buffer buffer = new Buffer().writeDecimalLong(value).writeByte(b2);
                        if (!negative) {
                            buffer.readByte();
                        }
                        throw new NumberFormatException("Number too large: " + buffer.readUtf8());
                    }
                    value *= 10L;
                    value += (long)digit;
                } else if (b2 == 45 && seen == 0) {
                    negative = true;
                    --overflowDigit;
                } else {
                    done = true;
                    break;
                }
                ++pos;
                ++seen;
            }
            if (pos == limit) {
                $this$commonReadDecimalLong.head = segment.pop();
                SegmentPool.recycle(segment);
                continue;
            }
            segment.pos = pos;
        } while (!done && $this$commonReadDecimalLong.head != null);
        $this$commonReadDecimalLong.setSize$okio($this$commonReadDecimalLong.size() - (long)seen);
        int n2 = minimumSeen = negative ? 2 : 1;
        if (seen < minimumSeen) {
            if ($this$commonReadDecimalLong.size() == 0L) {
                throw new EOFException();
            }
            String expected = negative ? "Expected a digit" : "Expected a digit or '-'";
            throw new NumberFormatException(expected + " but was 0x" + -SegmentedByteString.toHexString($this$commonReadDecimalLong.getByte(0L)));
        }
        return negative ? value : -value;
    }

    public static final long commonReadHexadecimalUnsignedLong(@NotNull Buffer $this$commonReadHexadecimalUnsignedLong) {
        Intrinsics.checkNotNullParameter($this$commonReadHexadecimalUnsignedLong, "<this>");
        boolean $i$f$commonReadHexadecimalUnsignedLong = false;
        if ($this$commonReadHexadecimalUnsignedLong.size() == 0L) {
            throw new EOFException();
        }
        long value = 0L;
        int seen = 0;
        boolean done = false;
        do {
            Segment segment;
            Intrinsics.checkNotNull($this$commonReadHexadecimalUnsignedLong.head);
            byte[] data = segment.data;
            int pos = segment.pos;
            int limit = segment.limit;
            while (pos < limit) {
                int digit = 0;
                byte b2 = data[pos];
                if (b2 >= 48 && b2 <= 57) {
                    digit = b2 - 48;
                } else if (b2 >= 97 && b2 <= 102) {
                    digit = b2 - 97 + 10;
                } else if (b2 >= 65 && b2 <= 70) {
                    digit = b2 - 65 + 10;
                } else {
                    if (seen == 0) {
                        throw new NumberFormatException("Expected leading [0-9a-fA-F] character but was 0x" + -SegmentedByteString.toHexString(b2));
                    }
                    done = true;
                    break;
                }
                if ((value & 0xF000000000000000L) != 0L) {
                    Buffer buffer = new Buffer().writeHexadecimalUnsignedLong(value).writeByte(b2);
                    throw new NumberFormatException("Number too large: " + buffer.readUtf8());
                }
                value <<= 4;
                value |= (long)digit;
                ++pos;
                ++seen;
            }
            if (pos == limit) {
                $this$commonReadHexadecimalUnsignedLong.head = segment.pop();
                SegmentPool.recycle(segment);
                continue;
            }
            segment.pos = pos;
        } while (!done && $this$commonReadHexadecimalUnsignedLong.head != null);
        $this$commonReadHexadecimalUnsignedLong.setSize$okio($this$commonReadHexadecimalUnsignedLong.size() - (long)seen);
        return value;
    }

    @NotNull
    public static final ByteString commonReadByteString(@NotNull Buffer $this$commonReadByteString) {
        Intrinsics.checkNotNullParameter($this$commonReadByteString, "<this>");
        boolean $i$f$commonReadByteString = false;
        return $this$commonReadByteString.readByteString($this$commonReadByteString.size());
    }

    @NotNull
    public static final ByteString commonReadByteString(@NotNull Buffer $this$commonReadByteString, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonReadByteString, "<this>");
        boolean $i$f$commonReadByteString = false;
        if (!(byteCount >= 0L && byteCount <= Integer.MAX_VALUE)) {
            boolean $i$a$-require--Buffer$commonReadByteString$22 = false;
            String $i$a$-require--Buffer$commonReadByteString$22 = "byteCount: " + byteCount;
            throw new IllegalArgumentException($i$a$-require--Buffer$commonReadByteString$22.toString());
        }
        if ($this$commonReadByteString.size() < byteCount) {
            throw new EOFException();
        }
        if (byteCount >= 4096L) {
            ByteString byteString;
            ByteString it = byteString = $this$commonReadByteString.snapshot((int)byteCount);
            boolean bl = false;
            $this$commonReadByteString.skip(byteCount);
            return byteString;
        }
        return new ByteString($this$commonReadByteString.readByteArray(byteCount));
    }

    public static final int commonSelect(@NotNull Buffer $this$commonSelect, @NotNull Options options) {
        Intrinsics.checkNotNullParameter($this$commonSelect, "<this>");
        Intrinsics.checkNotNullParameter(options, "options");
        boolean $i$f$commonSelect = false;
        int index = -Buffer.selectPrefix$default($this$commonSelect, options, false, 2, null);
        if (index == -1) {
            return -1;
        }
        int selectedSize = options.getByteStrings$okio()[index].size();
        $this$commonSelect.skip(selectedSize);
        return index;
    }

    public static final void commonReadFully(@NotNull Buffer $this$commonReadFully, @NotNull Buffer sink2, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonReadFully, "<this>");
        Intrinsics.checkNotNullParameter(sink2, "sink");
        boolean $i$f$commonReadFully = false;
        if ($this$commonReadFully.size() < byteCount) {
            sink2.write($this$commonReadFully, $this$commonReadFully.size());
            throw new EOFException();
        }
        sink2.write($this$commonReadFully, byteCount);
    }

    public static final long commonReadAll(@NotNull Buffer $this$commonReadAll, @NotNull Sink sink2) {
        Intrinsics.checkNotNullParameter($this$commonReadAll, "<this>");
        Intrinsics.checkNotNullParameter(sink2, "sink");
        boolean $i$f$commonReadAll = false;
        long byteCount = $this$commonReadAll.size();
        if (byteCount > 0L) {
            sink2.write($this$commonReadAll, byteCount);
        }
        return byteCount;
    }

    @NotNull
    public static final String commonReadUtf8(@NotNull Buffer $this$commonReadUtf8, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonReadUtf8, "<this>");
        boolean $i$f$commonReadUtf8 = false;
        if (!(byteCount >= 0L && byteCount <= Integer.MAX_VALUE)) {
            boolean $i$a$-require--Buffer$commonReadUtf8$22 = false;
            String $i$a$-require--Buffer$commonReadUtf8$22 = "byteCount: " + byteCount;
            throw new IllegalArgumentException($i$a$-require--Buffer$commonReadUtf8$22.toString());
        }
        if ($this$commonReadUtf8.size() < byteCount) {
            throw new EOFException();
        }
        if (byteCount == 0L) {
            return "";
        }
        Segment segment = $this$commonReadUtf8.head;
        Intrinsics.checkNotNull(segment);
        Segment s2 = segment;
        if ((long)s2.pos + byteCount > (long)s2.limit) {
            return _Utf8Kt.commonToUtf8String$default($this$commonReadUtf8.readByteArray(byteCount), 0, 0, 3, null);
        }
        String result = _Utf8Kt.commonToUtf8String(s2.data, s2.pos, s2.pos + (int)byteCount);
        s2.pos += (int)byteCount;
        $this$commonReadUtf8.setSize$okio($this$commonReadUtf8.size() - byteCount);
        if (s2.pos == s2.limit) {
            $this$commonReadUtf8.head = s2.pop();
            SegmentPool.recycle(s2);
        }
        return result;
    }

    @Nullable
    public static final String commonReadUtf8Line(@NotNull Buffer $this$commonReadUtf8Line) {
        Intrinsics.checkNotNullParameter($this$commonReadUtf8Line, "<this>");
        boolean $i$f$commonReadUtf8Line = false;
        long newline = $this$commonReadUtf8Line.indexOf((byte)10);
        return newline != -1L ? -Buffer.readUtf8Line($this$commonReadUtf8Line, newline) : ($this$commonReadUtf8Line.size() != 0L ? $this$commonReadUtf8Line.readUtf8($this$commonReadUtf8Line.size()) : null);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final String commonReadUtf8LineStrict(@NotNull Buffer $this$commonReadUtf8LineStrict, long limit) {
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
            return -Buffer.readUtf8Line($this$commonReadUtf8LineStrict, newline);
        }
        if (scanLength < $this$commonReadUtf8LineStrict.size() && $this$commonReadUtf8LineStrict.getByte(scanLength - 1L) == 13 && $this$commonReadUtf8LineStrict.getByte(scanLength) == 10) {
            return -Buffer.readUtf8Line($this$commonReadUtf8LineStrict, scanLength);
        }
        Buffer data = new Buffer();
        int n2 = 32;
        long b$iv = $this$commonReadUtf8LineStrict.size();
        boolean $i$f$minOf = false;
        $this$commonReadUtf8LineStrict.copyTo(data, 0L, Math.min((long)a$iv, b$iv));
        throw new EOFException("\\n not found: limit=" + Math.min($this$commonReadUtf8LineStrict.size(), limit) + " content=" + data.readByteString().hex() + '\u2026');
    }

    public static final int commonReadUtf8CodePoint(@NotNull Buffer $this$commonReadUtf8CodePoint) {
        byte $this$and$iv;
        Intrinsics.checkNotNullParameter($this$commonReadUtf8CodePoint, "<this>");
        boolean $i$f$commonReadUtf8CodePoint = false;
        if ($this$commonReadUtf8CodePoint.size() == 0L) {
            throw new EOFException();
        }
        byte b0 = $this$commonReadUtf8CodePoint.getByte(0L);
        int codePoint = 0;
        int byteCount = 0;
        int min = 0;
        byte by = b0;
        int other$iv = 128;
        byte $i$f$and = 0;
        if (($this$and$iv & other$iv) == 0) {
            $this$and$iv = b0;
            other$iv = 127;
            $i$f$and = 0;
            codePoint = $this$and$iv & other$iv;
            byteCount = 1;
            min = 0;
        } else {
            $this$and$iv = b0;
            other$iv = 224;
            $i$f$and = 0;
            if (($this$and$iv & other$iv) == 192) {
                $this$and$iv = b0;
                other$iv = 31;
                $i$f$and = 0;
                codePoint = $this$and$iv & other$iv;
                byteCount = 2;
                min = 128;
            } else {
                $this$and$iv = b0;
                other$iv = 240;
                $i$f$and = 0;
                if (($this$and$iv & other$iv) == 224) {
                    $this$and$iv = b0;
                    other$iv = 15;
                    $i$f$and = 0;
                    codePoint = $this$and$iv & other$iv;
                    byteCount = 3;
                    min = 2048;
                } else {
                    $this$and$iv = b0;
                    other$iv = 248;
                    $i$f$and = 0;
                    if (($this$and$iv & other$iv) == 240) {
                        $this$and$iv = b0;
                        other$iv = 7;
                        $i$f$and = 0;
                        codePoint = $this$and$iv & other$iv;
                        byteCount = 4;
                        min = 65536;
                    } else {
                        $this$commonReadUtf8CodePoint.skip(1L);
                        return 65533;
                    }
                }
            }
        }
        if ($this$commonReadUtf8CodePoint.size() < (long)byteCount) {
            throw new EOFException("size < " + byteCount + ": " + $this$commonReadUtf8CodePoint.size() + " (to read code point prefixed 0x" + -SegmentedByteString.toHexString(b0) + ')');
        }
        for (int i2 = 1; i2 < byteCount; ++i2) {
            byte $this$and$iv2;
            byte b2;
            $i$f$and = b2 = $this$commonReadUtf8CodePoint.getByte(i2);
            int other$iv2 = 192;
            boolean $i$f$and2 = false;
            if (($this$and$iv2 & other$iv2) == 128) {
                codePoint <<= 6;
                $this$and$iv2 = b2;
                other$iv2 = 63;
                $i$f$and2 = false;
                codePoint |= $this$and$iv2 & other$iv2;
                continue;
            }
            $this$commonReadUtf8CodePoint.skip(i2);
            return 65533;
        }
        $this$commonReadUtf8CodePoint.skip(byteCount);
        return codePoint > 0x10FFFF ? 65533 : ((55296 <= codePoint ? codePoint < 57344 : false) ? 65533 : (codePoint < min ? 65533 : codePoint));
    }

    @NotNull
    public static final Buffer commonWriteUtf8(@NotNull Buffer $this$commonWriteUtf8, @NotNull String string, int beginIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$commonWriteUtf8, "<this>");
        Intrinsics.checkNotNullParameter(string, "string");
        boolean $i$f$commonWriteUtf8 = false;
        if (!(beginIndex >= 0)) {
            boolean $i$a$-require--Buffer$commonWriteUtf8$42 = false;
            String $i$a$-require--Buffer$commonWriteUtf8$42 = "beginIndex < 0: " + beginIndex;
            throw new IllegalArgumentException($i$a$-require--Buffer$commonWriteUtf8$42.toString());
        }
        if (!(endIndex >= beginIndex)) {
            boolean $i$a$-require--Buffer$commonWriteUtf8$52 = false;
            String $i$a$-require--Buffer$commonWriteUtf8$52 = "endIndex < beginIndex: " + endIndex + " < " + beginIndex;
            throw new IllegalArgumentException($i$a$-require--Buffer$commonWriteUtf8$52.toString());
        }
        if (!(endIndex <= string.length())) {
            boolean $i$a$-require--Buffer$commonWriteUtf8$62 = false;
            String $i$a$-require--Buffer$commonWriteUtf8$62 = "endIndex > string.length: " + endIndex + " > " + string.length();
            throw new IllegalArgumentException($i$a$-require--Buffer$commonWriteUtf8$62.toString());
        }
        int i2 = beginIndex;
        while (i2 < endIndex) {
            char low;
            char c2;
            block12: {
                block11: {
                    c2 = string.charAt(i2);
                    if (c2 < '\u0080') {
                        Segment tail = $this$commonWriteUtf8.writableSegment$okio(1);
                        byte[] data = tail.data;
                        int segmentOffset = tail.limit - i2;
                        int runLimit = Math.min(endIndex, 8192 - segmentOffset);
                        data[segmentOffset + i2++] = (byte)c2;
                        while (i2 < runLimit && (c2 = string.charAt(i2)) < '\u0080') {
                            data[segmentOffset + i2++] = (byte)c2;
                        }
                        int runSize = i2 + segmentOffset - tail.limit;
                        tail.limit += runSize;
                        $this$commonWriteUtf8.setSize$okio($this$commonWriteUtf8.size() + (long)runSize);
                        continue;
                    }
                    if (c2 < '\u0800') {
                        Segment tail = $this$commonWriteUtf8.writableSegment$okio(2);
                        tail.data[tail.limit] = (byte)(c2 >> 6 | 0xC0);
                        tail.data[tail.limit + 1] = (byte)(c2 & 0x3F | 0x80);
                        tail.limit += 2;
                        $this$commonWriteUtf8.setSize$okio($this$commonWriteUtf8.size() + 2L);
                        ++i2;
                        continue;
                    }
                    if (c2 < '\ud800' || c2 > '\udfff') {
                        Segment tail = $this$commonWriteUtf8.writableSegment$okio(3);
                        tail.data[tail.limit] = (byte)(c2 >> 12 | 0xE0);
                        tail.data[tail.limit + 1] = (byte)(c2 >> 6 & 0x3F | 0x80);
                        tail.data[tail.limit + 2] = (byte)(c2 & 0x3F | 0x80);
                        tail.limit += 3;
                        $this$commonWriteUtf8.setSize$okio($this$commonWriteUtf8.size() + 3L);
                        ++i2;
                        continue;
                    }
                    char c3 = low = i2 + 1 < endIndex ? string.charAt(i2 + 1) : (char)'\u0000';
                    if (c2 > '\udbff') break block11;
                    if ('\udc00' <= low ? low < '\ue000' : false) break block12;
                }
                $this$commonWriteUtf8.writeByte(63);
                ++i2;
                continue;
            }
            int codePoint = 65536 + ((c2 & 0x3FF) << 10 | low & 0x3FF);
            Segment tail = $this$commonWriteUtf8.writableSegment$okio(4);
            tail.data[tail.limit] = (byte)(codePoint >> 18 | 0xF0);
            tail.data[tail.limit + 1] = (byte)(codePoint >> 12 & 0x3F | 0x80);
            tail.data[tail.limit + 2] = (byte)(codePoint >> 6 & 0x3F | 0x80);
            tail.data[tail.limit + 3] = (byte)(codePoint & 0x3F | 0x80);
            tail.limit += 4;
            $this$commonWriteUtf8.setSize$okio($this$commonWriteUtf8.size() + 4L);
            i2 += 2;
        }
        return $this$commonWriteUtf8;
    }

    @NotNull
    public static final Buffer commonWriteUtf8CodePoint(@NotNull Buffer $this$commonWriteUtf8CodePoint, int codePoint) {
        Intrinsics.checkNotNullParameter($this$commonWriteUtf8CodePoint, "<this>");
        boolean $i$f$commonWriteUtf8CodePoint = false;
        if (codePoint < 128) {
            $this$commonWriteUtf8CodePoint.writeByte(codePoint);
        } else if (codePoint < 2048) {
            Segment tail = $this$commonWriteUtf8CodePoint.writableSegment$okio(2);
            tail.data[tail.limit] = (byte)(codePoint >> 6 | 0xC0);
            tail.data[tail.limit + 1] = (byte)(codePoint & 0x3F | 0x80);
            tail.limit += 2;
            $this$commonWriteUtf8CodePoint.setSize$okio($this$commonWriteUtf8CodePoint.size() + 2L);
        } else {
            boolean bl = 55296 <= codePoint ? codePoint < 57344 : false;
            if (bl) {
                $this$commonWriteUtf8CodePoint.writeByte(63);
            } else if (codePoint < 65536) {
                Segment tail = $this$commonWriteUtf8CodePoint.writableSegment$okio(3);
                tail.data[tail.limit] = (byte)(codePoint >> 12 | 0xE0);
                tail.data[tail.limit + 1] = (byte)(codePoint >> 6 & 0x3F | 0x80);
                tail.data[tail.limit + 2] = (byte)(codePoint & 0x3F | 0x80);
                tail.limit += 3;
                $this$commonWriteUtf8CodePoint.setSize$okio($this$commonWriteUtf8CodePoint.size() + 3L);
            } else if (codePoint <= 0x10FFFF) {
                Segment tail = $this$commonWriteUtf8CodePoint.writableSegment$okio(4);
                tail.data[tail.limit] = (byte)(codePoint >> 18 | 0xF0);
                tail.data[tail.limit + 1] = (byte)(codePoint >> 12 & 0x3F | 0x80);
                tail.data[tail.limit + 2] = (byte)(codePoint >> 6 & 0x3F | 0x80);
                tail.data[tail.limit + 3] = (byte)(codePoint & 0x3F | 0x80);
                tail.limit += 4;
                $this$commonWriteUtf8CodePoint.setSize$okio($this$commonWriteUtf8CodePoint.size() + 4L);
            } else {
                throw new IllegalArgumentException("Unexpected code point: 0x" + -SegmentedByteString.toHexString(codePoint));
            }
        }
        return $this$commonWriteUtf8CodePoint;
    }

    public static final long commonWriteAll(@NotNull Buffer $this$commonWriteAll, @NotNull Source source2) {
        long readCount;
        Intrinsics.checkNotNullParameter($this$commonWriteAll, "<this>");
        Intrinsics.checkNotNullParameter(source2, "source");
        boolean $i$f$commonWriteAll = false;
        long totalBytesRead = 0L;
        while ((readCount = source2.read($this$commonWriteAll, 8192L)) != -1L) {
            totalBytesRead += readCount;
        }
        return totalBytesRead;
    }

    @NotNull
    public static final Buffer commonWrite(@NotNull Buffer $this$commonWrite, @NotNull Source source2, long byteCount) {
        long read;
        Intrinsics.checkNotNullParameter($this$commonWrite, "<this>");
        Intrinsics.checkNotNullParameter(source2, "source");
        boolean $i$f$commonWrite = false;
        for (long byteCount2 = byteCount; byteCount2 > 0L; byteCount2 -= read) {
            read = source2.read($this$commonWrite, byteCount2);
            if (read != -1L) continue;
            throw new EOFException();
        }
        return $this$commonWrite;
    }

    @NotNull
    public static final Buffer commonWriteByte(@NotNull Buffer $this$commonWriteByte, int b2) {
        Intrinsics.checkNotNullParameter($this$commonWriteByte, "<this>");
        boolean $i$f$commonWriteByte = false;
        Segment tail = $this$commonWriteByte.writableSegment$okio(1);
        int n2 = tail.limit;
        tail.limit = n2 + 1;
        tail.data[n2] = (byte)b2;
        $this$commonWriteByte.setSize$okio($this$commonWriteByte.size() + 1L);
        return $this$commonWriteByte;
    }

    @NotNull
    public static final Buffer commonWriteShort(@NotNull Buffer $this$commonWriteShort, int s2) {
        Intrinsics.checkNotNullParameter($this$commonWriteShort, "<this>");
        boolean $i$f$commonWriteShort = false;
        Segment tail = $this$commonWriteShort.writableSegment$okio(2);
        byte[] data = tail.data;
        int limit = tail.limit;
        data[limit++] = (byte)(s2 >>> 8 & 0xFF);
        data[limit++] = (byte)(s2 & 0xFF);
        tail.limit = limit;
        $this$commonWriteShort.setSize$okio($this$commonWriteShort.size() + 2L);
        return $this$commonWriteShort;
    }

    @NotNull
    public static final Buffer commonWriteInt(@NotNull Buffer $this$commonWriteInt, int i2) {
        Intrinsics.checkNotNullParameter($this$commonWriteInt, "<this>");
        boolean $i$f$commonWriteInt = false;
        Segment tail = $this$commonWriteInt.writableSegment$okio(4);
        byte[] data = tail.data;
        int limit = tail.limit;
        data[limit++] = (byte)(i2 >>> 24 & 0xFF);
        data[limit++] = (byte)(i2 >>> 16 & 0xFF);
        data[limit++] = (byte)(i2 >>> 8 & 0xFF);
        data[limit++] = (byte)(i2 & 0xFF);
        tail.limit = limit;
        $this$commonWriteInt.setSize$okio($this$commonWriteInt.size() + 4L);
        return $this$commonWriteInt;
    }

    @NotNull
    public static final Buffer commonWriteLong(@NotNull Buffer $this$commonWriteLong, long v2) {
        Intrinsics.checkNotNullParameter($this$commonWriteLong, "<this>");
        boolean $i$f$commonWriteLong = false;
        Segment tail = $this$commonWriteLong.writableSegment$okio(8);
        byte[] data = tail.data;
        int limit = tail.limit;
        data[limit++] = (byte)(v2 >>> 56 & 0xFFL);
        data[limit++] = (byte)(v2 >>> 48 & 0xFFL);
        data[limit++] = (byte)(v2 >>> 40 & 0xFFL);
        data[limit++] = (byte)(v2 >>> 32 & 0xFFL);
        data[limit++] = (byte)(v2 >>> 24 & 0xFFL);
        data[limit++] = (byte)(v2 >>> 16 & 0xFFL);
        data[limit++] = (byte)(v2 >>> 8 & 0xFFL);
        data[limit++] = (byte)(v2 & 0xFFL);
        tail.limit = limit;
        $this$commonWriteLong.setSize$okio($this$commonWriteLong.size() + 8L);
        return $this$commonWriteLong;
    }

    public static final void commonWrite(@NotNull Buffer $this$commonWrite, @NotNull Buffer source2, long byteCount) {
        long movedByteCount;
        long byteCount2;
        Intrinsics.checkNotNullParameter($this$commonWrite, "<this>");
        Intrinsics.checkNotNullParameter(source2, "source");
        boolean $i$f$commonWrite = false;
        if (!(source2 != $this$commonWrite)) {
            boolean $i$a$-require--Buffer$commonWrite$22 = false;
            String $i$a$-require--Buffer$commonWrite$22 = "source == this";
            throw new IllegalArgumentException($i$a$-require--Buffer$commonWrite$22.toString());
        }
        -SegmentedByteString.checkOffsetAndCount(source2.size(), 0L, byteCount2);
        for (byteCount2 = byteCount; byteCount2 > 0L; byteCount2 -= movedByteCount) {
            Segment segmentToMove;
            Segment segment = source2.head;
            Intrinsics.checkNotNull(segment);
            int n2 = segment.limit;
            Segment segment2 = source2.head;
            Intrinsics.checkNotNull(segment2);
            if (byteCount2 < (long)(n2 - segment2.pos)) {
                Segment tail;
                Segment segment3;
                if ($this$commonWrite.head != null) {
                    Segment segment4 = $this$commonWrite.head;
                    Intrinsics.checkNotNull(segment4);
                    segment3 = segment4.prev;
                } else {
                    segment3 = tail = null;
                }
                if (tail != null && tail.owner && byteCount2 + (long)tail.limit - (long)(tail.shared ? 0 : tail.pos) <= 8192L) {
                    Segment segment5 = source2.head;
                    Intrinsics.checkNotNull(segment5);
                    segment5.writeTo(tail, (int)byteCount2);
                    source2.setSize$okio(source2.size() - byteCount2);
                    $this$commonWrite.setSize$okio($this$commonWrite.size() + byteCount2);
                    return;
                }
                Segment segment6 = source2.head;
                Intrinsics.checkNotNull(segment6);
                source2.head = segment6.split((int)byteCount2);
            }
            Segment segment7 = segmentToMove = source2.head;
            Intrinsics.checkNotNull(segment7);
            movedByteCount = segment7.limit - segmentToMove.pos;
            source2.head = segmentToMove.pop();
            if ($this$commonWrite.head == null) {
                $this$commonWrite.head = segmentToMove;
                segmentToMove.next = segmentToMove.prev = segmentToMove;
            } else {
                Segment tail;
                Segment segment8 = $this$commonWrite.head;
                Intrinsics.checkNotNull(segment8);
                Segment segment9 = tail = segment8.prev;
                Intrinsics.checkNotNull(segment9);
                tail = segment9.push(segmentToMove);
                tail.compact();
            }
            source2.setSize$okio(source2.size() - movedByteCount);
            $this$commonWrite.setSize$okio($this$commonWrite.size() + movedByteCount);
        }
    }

    public static final long commonRead(@NotNull Buffer $this$commonRead, @NotNull Buffer sink2, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonRead, "<this>");
        Intrinsics.checkNotNullParameter(sink2, "sink");
        boolean $i$f$commonRead = false;
        long byteCount2 = 0L;
        byteCount2 = byteCount;
        if (!(byteCount2 >= 0L)) {
            boolean bl = false;
            String string = "byteCount < 0: " + byteCount2;
            throw new IllegalArgumentException(string.toString());
        }
        if ($this$commonRead.size() == 0L) {
            return -1L;
        }
        if (byteCount2 > $this$commonRead.size()) {
            byteCount2 = $this$commonRead.size();
        }
        sink2.write($this$commonRead, byteCount2);
        return byteCount2;
    }

    /*
     * WARNING - void declaration
     */
    public static final long commonIndexOf(@NotNull Buffer $this$commonIndexOf, byte b2, long fromIndex, long toIndex) {
        void offset;
        long nextOffset$iv;
        void $this$seek$iv;
        Intrinsics.checkNotNullParameter($this$commonIndexOf, "<this>");
        boolean $i$f$commonIndexOf = false;
        long fromIndex2 = 0L;
        fromIndex2 = fromIndex;
        long toIndex2 = 0L;
        toIndex2 = toIndex;
        if (!(0L <= fromIndex2 ? fromIndex2 <= toIndex2 : false)) {
            boolean $i$a$-require--Buffer$commonIndexOf$22 = false;
            String $i$a$-require--Buffer$commonIndexOf$22 = "size=" + $this$commonIndexOf.size() + " fromIndex=" + fromIndex2 + " toIndex=" + toIndex2;
            throw new IllegalArgumentException($i$a$-require--Buffer$commonIndexOf$22.toString());
        }
        if (toIndex2 > $this$commonIndexOf.size()) {
            toIndex2 = $this$commonIndexOf.size();
        }
        if (fromIndex2 == toIndex2) {
            return -1L;
        }
        Buffer buffer = $this$commonIndexOf;
        long fromIndex$iv = fromIndex2;
        boolean $i$f$seek = false;
        Segment segment = $this$seek$iv.head;
        if (segment == null) {
            long l2 = -1L;
            Object s2 = null;
            boolean bl = false;
            return -1L;
        }
        Segment s$iv = segment;
        if ($this$seek$iv.size() - fromIndex$iv < fromIndex$iv) {
            long offset$iv;
            for (offset$iv = $this$seek$iv.size(); offset$iv > fromIndex$iv; offset$iv -= (long)(s$iv.limit - s$iv.pos)) {
                Intrinsics.checkNotNull(s$iv.prev);
            }
            long offset2 = offset$iv;
            Segment s3 = s$iv;
            boolean bl = false;
            Segment segment2 = s3;
            if (segment2 == null) {
                return -1L;
            }
            Segment s4 = segment2;
            long offset3 = offset2;
            while (offset3 < toIndex2) {
                byte[] data = s4.data;
                int limit = (int)Math.min((long)s4.limit, (long)s4.pos + toIndex2 - offset3);
                for (int pos = (int)((long)s4.pos + fromIndex2 - offset3); pos < limit; ++pos) {
                    if (data[pos] != b2) continue;
                    return (long)(pos - s4.pos) + offset3;
                }
                fromIndex2 = offset3 += (long)(s4.limit - s4.pos);
                Intrinsics.checkNotNull(s4.next);
            }
            return -1L;
        }
        long offset$iv = 0L;
        while ((nextOffset$iv = offset$iv + (long)(s$iv.limit - s$iv.pos)) <= fromIndex$iv) {
            Intrinsics.checkNotNull(s$iv.next);
            offset$iv = nextOffset$iv;
        }
        long l3 = offset$iv;
        Segment s5 = s$iv;
        boolean bl = false;
        Segment segment3 = s5;
        if (segment3 == null) {
            return -1L;
        }
        Segment s6 = segment3;
        void offset4 = offset;
        while (offset4 < toIndex2) {
            byte[] data = s6.data;
            int limit = (int)Math.min((long)s6.limit, (long)s6.pos + toIndex2 - offset4);
            for (int pos = (int)((long)s6.pos + fromIndex2 - offset4); pos < limit; ++pos) {
                if (data[pos] != b2) continue;
                return (long)(pos - s6.pos) + offset4;
            }
            fromIndex2 = offset4 += (long)(s6.limit - s6.pos);
            Intrinsics.checkNotNull(s6.next);
        }
        return -1L;
    }

    /*
     * WARNING - void declaration
     */
    public static final long commonIndexOf(@NotNull Buffer $this$commonIndexOf, @NotNull ByteString bytes, long fromIndex) {
        void offset;
        long nextOffset$iv;
        void $this$seek$iv;
        Intrinsics.checkNotNullParameter($this$commonIndexOf, "<this>");
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        boolean $i$f$commonIndexOf = false;
        long fromIndex2 = 0L;
        fromIndex2 = fromIndex;
        if (!(bytes.size() > 0)) {
            boolean $i$a$-require--Buffer$commonIndexOf$52 = false;
            String $i$a$-require--Buffer$commonIndexOf$52 = "bytes is empty";
            throw new IllegalArgumentException($i$a$-require--Buffer$commonIndexOf$52.toString());
        }
        if (!(fromIndex2 >= 0L)) {
            boolean $i$a$-require--Buffer$commonIndexOf$62 = false;
            String $i$a$-require--Buffer$commonIndexOf$62 = "fromIndex < 0: " + fromIndex2;
            throw new IllegalArgumentException($i$a$-require--Buffer$commonIndexOf$62.toString());
        }
        Buffer buffer = $this$commonIndexOf;
        long fromIndex$iv = fromIndex2;
        boolean $i$f$seek = false;
        Segment segment = $this$seek$iv.head;
        if (segment == null) {
            long l2 = -1L;
            Object s2 = null;
            boolean bl = false;
            return -1L;
        }
        Segment s$iv = segment;
        if ($this$seek$iv.size() - fromIndex$iv < fromIndex$iv) {
            long offset$iv;
            for (offset$iv = $this$seek$iv.size(); offset$iv > fromIndex$iv; offset$iv -= (long)(s$iv.limit - s$iv.pos)) {
                Intrinsics.checkNotNull(s$iv.prev);
            }
            long offset2 = offset$iv;
            Segment s3 = s$iv;
            boolean bl = false;
            Segment segment2 = s3;
            if (segment2 == null) {
                return -1L;
            }
            Segment s4 = segment2;
            long offset3 = offset2;
            byte[] targetByteArray = bytes.internalArray$okio();
            byte b0 = targetByteArray[0];
            int bytesSize = bytes.size();
            long resultLimit = $this$commonIndexOf.size() - (long)bytesSize + 1L;
            while (offset3 < resultLimit) {
                void a$iv;
                byte[] data = s4.data;
                int n2 = s4.limit;
                long b$iv = (long)s4.pos + resultLimit - offset3;
                boolean $i$f$minOf = false;
                int segmentLimit = (int)Math.min((long)a$iv, b$iv);
                for (int pos = (int)((long)s4.pos + fromIndex2 - offset3); pos < segmentLimit; ++pos) {
                    if (data[pos] != b0 || !-Buffer.rangeEquals(s4, pos + 1, targetByteArray, 1, bytesSize)) continue;
                    return (long)(pos - s4.pos) + offset3;
                }
                fromIndex2 = offset3 += (long)(s4.limit - s4.pos);
                Intrinsics.checkNotNull(s4.next);
            }
            return -1L;
        }
        long offset$iv = 0L;
        while ((nextOffset$iv = offset$iv + (long)(s$iv.limit - s$iv.pos)) <= fromIndex$iv) {
            Intrinsics.checkNotNull(s$iv.next);
            offset$iv = nextOffset$iv;
        }
        long l3 = offset$iv;
        Segment s5 = s$iv;
        boolean bl = false;
        Segment segment3 = s5;
        if (segment3 == null) {
            return -1L;
        }
        Segment s6 = segment3;
        void offset4 = offset;
        byte[] targetByteArray = bytes.internalArray$okio();
        byte b0 = targetByteArray[0];
        int bytesSize = bytes.size();
        long resultLimit = $this$commonIndexOf.size() - (long)bytesSize + 1L;
        while (offset4 < resultLimit) {
            void a$iv;
            byte[] data = s6.data;
            int n3 = s6.limit;
            long b$iv = (long)s6.pos + resultLimit - offset4;
            boolean $i$f$minOf = false;
            int segmentLimit = (int)Math.min((long)a$iv, b$iv);
            for (int pos = (int)((long)s6.pos + fromIndex2 - offset4); pos < segmentLimit; ++pos) {
                if (data[pos] != b0 || !-Buffer.rangeEquals(s6, pos + 1, targetByteArray, 1, bytesSize)) continue;
                return (long)(pos - s6.pos) + offset4;
            }
            fromIndex2 = offset4 += (long)(s6.limit - s6.pos);
            Intrinsics.checkNotNull(s6.next);
        }
        return -1L;
    }

    /*
     * WARNING - void declaration
     */
    public static final long commonIndexOfElement(@NotNull Buffer $this$commonIndexOfElement, @NotNull ByteString targetBytes, long fromIndex) {
        void offset;
        long nextOffset$iv;
        void $this$seek$iv;
        Intrinsics.checkNotNullParameter($this$commonIndexOfElement, "<this>");
        Intrinsics.checkNotNullParameter(targetBytes, "targetBytes");
        boolean $i$f$commonIndexOfElement = false;
        long fromIndex2 = 0L;
        fromIndex2 = fromIndex;
        if (!(fromIndex2 >= 0L)) {
            boolean $i$a$-require--Buffer$commonIndexOfElement$22 = false;
            String $i$a$-require--Buffer$commonIndexOfElement$22 = "fromIndex < 0: " + fromIndex2;
            throw new IllegalArgumentException($i$a$-require--Buffer$commonIndexOfElement$22.toString());
        }
        Buffer buffer = $this$commonIndexOfElement;
        long fromIndex$iv = fromIndex2;
        boolean $i$f$seek = false;
        Segment segment = $this$seek$iv.head;
        if (segment == null) {
            long l2 = -1L;
            Object s2 = null;
            boolean bl = false;
            return -1L;
        }
        Segment s$iv = segment;
        if ($this$seek$iv.size() - fromIndex$iv < fromIndex$iv) {
            long offset$iv;
            for (offset$iv = $this$seek$iv.size(); offset$iv > fromIndex$iv; offset$iv -= (long)(s$iv.limit - s$iv.pos)) {
                Intrinsics.checkNotNull(s$iv.prev);
            }
            long offset2 = offset$iv;
            Segment s3 = s$iv;
            boolean bl = false;
            Segment segment2 = s3;
            if (segment2 == null) {
                return -1L;
            }
            Segment s4 = segment2;
            long offset3 = offset2;
            if (targetBytes.size() == 2) {
                byte b0 = targetBytes.getByte(0);
                byte b1 = targetBytes.getByte(1);
                while (offset3 < $this$commonIndexOfElement.size()) {
                    byte[] data = s4.data;
                    int limit = s4.limit;
                    for (int pos = (int)((long)s4.pos + fromIndex2 - offset3); pos < limit; ++pos) {
                        byte b2 = data[pos];
                        if (b2 != b0 && b2 != b1) continue;
                        return (long)(pos - s4.pos) + offset3;
                    }
                    fromIndex2 = offset3 += (long)(s4.limit - s4.pos);
                    Intrinsics.checkNotNull(s4.next);
                }
            } else {
                byte[] targetByteArray = targetBytes.internalArray$okio();
                while (offset3 < $this$commonIndexOfElement.size()) {
                    byte[] data = s4.data;
                    int limit = s4.limit;
                    for (int pos = (int)((long)s4.pos + fromIndex2 - offset3); pos < limit; ++pos) {
                        byte b3 = data[pos];
                        for (byte t2 : targetByteArray) {
                            if (b3 != t2) continue;
                            return (long)(pos - s4.pos) + offset3;
                        }
                    }
                    fromIndex2 = offset3 += (long)(s4.limit - s4.pos);
                    Intrinsics.checkNotNull(s4.next);
                }
            }
            return -1L;
        }
        long offset$iv = 0L;
        while ((nextOffset$iv = offset$iv + (long)(s$iv.limit - s$iv.pos)) <= fromIndex$iv) {
            Intrinsics.checkNotNull(s$iv.next);
            offset$iv = nextOffset$iv;
        }
        long l3 = offset$iv;
        Segment s5 = s$iv;
        boolean bl = false;
        Segment segment3 = s5;
        if (segment3 == null) {
            return -1L;
        }
        Segment s6 = segment3;
        void offset4 = offset;
        if (targetBytes.size() == 2) {
            byte b0 = targetBytes.getByte(0);
            byte b1 = targetBytes.getByte(1);
            while (offset4 < $this$commonIndexOfElement.size()) {
                byte[] data = s6.data;
                int limit = s6.limit;
                for (int pos = (int)((long)s6.pos + fromIndex2 - offset4); pos < limit; ++pos) {
                    byte b4 = data[pos];
                    if (b4 != b0 && b4 != b1) continue;
                    return (long)(pos - s6.pos) + offset4;
                }
                fromIndex2 = offset4 += (long)(s6.limit - s6.pos);
                Intrinsics.checkNotNull(s6.next);
            }
        } else {
            byte[] targetByteArray = targetBytes.internalArray$okio();
            while (offset4 < $this$commonIndexOfElement.size()) {
                byte[] data = s6.data;
                int limit = s6.limit;
                for (int pos = (int)((long)s6.pos + fromIndex2 - offset4); pos < limit; ++pos) {
                    byte b5 = data[pos];
                    for (byte t3 : targetByteArray) {
                        if (b5 != t3) continue;
                        return (long)(pos - s6.pos) + offset4;
                    }
                }
                fromIndex2 = offset4 += (long)(s6.limit - s6.pos);
                Intrinsics.checkNotNull(s6.next);
            }
        }
        return -1L;
    }

    public static final boolean commonRangeEquals(@NotNull Buffer $this$commonRangeEquals, long offset, @NotNull ByteString bytes, int bytesOffset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonRangeEquals, "<this>");
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        boolean $i$f$commonRangeEquals = false;
        if (offset < 0L || bytesOffset < 0 || byteCount < 0 || $this$commonRangeEquals.size() - offset < (long)byteCount || bytes.size() - bytesOffset < byteCount) {
            return false;
        }
        for (int i2 = 0; i2 < byteCount; ++i2) {
            if ($this$commonRangeEquals.getByte(offset + (long)i2) == bytes.getByte(bytesOffset + i2)) continue;
            return false;
        }
        return true;
    }

    public static final boolean commonEquals(@NotNull Buffer $this$commonEquals, @Nullable Object other) {
        Intrinsics.checkNotNullParameter($this$commonEquals, "<this>");
        boolean $i$f$commonEquals = false;
        if ($this$commonEquals == other) {
            return true;
        }
        if (!(other instanceof Buffer)) {
            return false;
        }
        if ($this$commonEquals.size() != ((Buffer)other).size()) {
            return false;
        }
        if ($this$commonEquals.size() == 0L) {
            return true;
        }
        Segment segment = $this$commonEquals.head;
        Intrinsics.checkNotNull(segment);
        Segment sa = segment;
        Segment segment2 = ((Buffer)other).head;
        Intrinsics.checkNotNull(segment2);
        Segment sb = segment2;
        int posA = sa.pos;
        int posB = sb.pos;
        long count = 0L;
        for (long pos = 0L; pos < $this$commonEquals.size(); pos += count) {
            count = Math.min(sa.limit - posA, sb.limit - posB);
            long l2 = count;
            for (long i2 = 0L; i2 < l2; ++i2) {
                if (sa.data[posA++] == sb.data[posB++]) continue;
                return false;
            }
            if (posA == sa.limit) {
                Intrinsics.checkNotNull(sa.next);
                posA = sa.pos;
            }
            if (posB != sb.limit) continue;
            Intrinsics.checkNotNull(sb.next);
            posB = sb.pos;
        }
        return true;
    }

    public static final int commonHashCode(@NotNull Buffer $this$commonHashCode) {
        Intrinsics.checkNotNullParameter($this$commonHashCode, "<this>");
        boolean $i$f$commonHashCode = false;
        Segment segment = $this$commonHashCode.head;
        if (segment == null) {
            return 0;
        }
        Segment s2 = segment;
        int result = 1;
        do {
            int limit = s2.limit;
            for (int pos = s2.pos; pos < limit; ++pos) {
                result = 31 * result + s2.data[pos];
            }
            Intrinsics.checkNotNull(s2.next);
        } while (s2 != $this$commonHashCode.head);
        return result;
    }

    @NotNull
    public static final Buffer commonCopy(@NotNull Buffer $this$commonCopy) {
        Segment headCopy;
        Intrinsics.checkNotNullParameter($this$commonCopy, "<this>");
        boolean $i$f$commonCopy = false;
        Buffer result = new Buffer();
        if ($this$commonCopy.size() == 0L) {
            return result;
        }
        Segment segment = $this$commonCopy.head;
        Intrinsics.checkNotNull(segment);
        Segment head = segment;
        headCopy.next = headCopy.prev = (result.head = (headCopy = head.sharedCopy()));
        Segment s2 = head.next;
        while (s2 != head) {
            Segment segment2 = headCopy.prev;
            Intrinsics.checkNotNull(segment2);
            Segment segment3 = s2;
            Intrinsics.checkNotNull(segment3);
            segment2.push(segment3.sharedCopy());
            s2 = s2.next;
        }
        result.setSize$okio($this$commonCopy.size());
        return result;
    }

    @NotNull
    public static final ByteString commonSnapshot(@NotNull Buffer $this$commonSnapshot) {
        Intrinsics.checkNotNullParameter($this$commonSnapshot, "<this>");
        boolean $i$f$commonSnapshot = false;
        if (!($this$commonSnapshot.size() <= Integer.MAX_VALUE)) {
            boolean bl = false;
            String string = "size > Int.MAX_VALUE: " + $this$commonSnapshot.size();
            throw new IllegalStateException(string.toString());
        }
        return $this$commonSnapshot.snapshot((int)$this$commonSnapshot.size());
    }

    @NotNull
    public static final ByteString commonSnapshot(@NotNull Buffer $this$commonSnapshot, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonSnapshot, "<this>");
        boolean $i$f$commonSnapshot = false;
        if (byteCount == 0) {
            return ByteString.EMPTY;
        }
        -SegmentedByteString.checkOffsetAndCount($this$commonSnapshot.size(), 0L, byteCount);
        int offset = 0;
        int segmentCount = 0;
        Segment s2 = $this$commonSnapshot.head;
        while (offset < byteCount) {
            Segment segment = s2;
            Intrinsics.checkNotNull(segment);
            if (segment.limit == s2.pos) {
                throw new AssertionError((Object)"s.limit == s.pos");
            }
            offset += s2.limit - s2.pos;
            ++segmentCount;
            s2 = s2.next;
        }
        byte[][] segments = new byte[segmentCount][];
        int[] directory = new int[segmentCount * 2];
        offset = 0;
        segmentCount = 0;
        s2 = $this$commonSnapshot.head;
        while (offset < byteCount) {
            Segment segment = s2;
            Intrinsics.checkNotNull(segment);
            segments[segmentCount] = segment.data;
            directory[segmentCount] = Math.min(offset += s2.limit - s2.pos, byteCount);
            directory[segmentCount + ((Object[])segments).length] = s2.pos;
            s2.shared = true;
            ++segmentCount;
            s2 = s2.next;
        }
        return new SegmentedByteString(segments, directory);
    }

    @NotNull
    public static final Buffer.UnsafeCursor commonReadUnsafe(@NotNull Buffer $this$commonReadUnsafe, @NotNull Buffer.UnsafeCursor unsafeCursor) {
        Intrinsics.checkNotNullParameter($this$commonReadUnsafe, "<this>");
        Intrinsics.checkNotNullParameter(unsafeCursor, "unsafeCursor");
        Buffer.UnsafeCursor unsafeCursor2 = -SegmentedByteString.resolveDefaultParameter(unsafeCursor);
        if (!(unsafeCursor2.buffer == null)) {
            boolean bl = false;
            String string = "already attached to a buffer";
            throw new IllegalStateException(string.toString());
        }
        unsafeCursor2.buffer = $this$commonReadUnsafe;
        unsafeCursor2.readWrite = false;
        return unsafeCursor2;
    }

    @NotNull
    public static final Buffer.UnsafeCursor commonReadAndWriteUnsafe(@NotNull Buffer $this$commonReadAndWriteUnsafe, @NotNull Buffer.UnsafeCursor unsafeCursor) {
        Intrinsics.checkNotNullParameter($this$commonReadAndWriteUnsafe, "<this>");
        Intrinsics.checkNotNullParameter(unsafeCursor, "unsafeCursor");
        Buffer.UnsafeCursor unsafeCursor2 = -SegmentedByteString.resolveDefaultParameter(unsafeCursor);
        if (!(unsafeCursor2.buffer == null)) {
            boolean bl = false;
            String string = "already attached to a buffer";
            throw new IllegalStateException(string.toString());
        }
        unsafeCursor2.buffer = $this$commonReadAndWriteUnsafe;
        unsafeCursor2.readWrite = true;
        return unsafeCursor2;
    }

    public static final int commonNext(@NotNull Buffer.UnsafeCursor $this$commonNext) {
        Intrinsics.checkNotNullParameter($this$commonNext, "<this>");
        boolean $i$f$commonNext = false;
        long l2 = $this$commonNext.offset;
        Buffer buffer = $this$commonNext.buffer;
        Intrinsics.checkNotNull(buffer);
        if (!(l2 != buffer.size())) {
            boolean bl = false;
            String string = "no more bytes";
            throw new IllegalStateException(string.toString());
        }
        return $this$commonNext.offset == -1L ? $this$commonNext.seek(0L) : $this$commonNext.seek($this$commonNext.offset + (long)($this$commonNext.end - $this$commonNext.start));
    }

    public static final int commonSeek(@NotNull Buffer.UnsafeCursor $this$commonSeek, long offset) {
        Intrinsics.checkNotNullParameter($this$commonSeek, "<this>");
        boolean $i$f$commonSeek = false;
        Buffer buffer = $this$commonSeek.buffer;
        if (buffer == null) {
            boolean bl = false;
            String string = "not attached to a buffer";
            throw new IllegalStateException(string.toString());
        }
        Buffer buffer2 = buffer;
        if (offset < -1L || offset > buffer2.size()) {
            throw new ArrayIndexOutOfBoundsException("offset=" + offset + " > size=" + buffer2.size());
        }
        if (offset == -1L || offset == buffer2.size()) {
            $this$commonSeek.setSegment$okio(null);
            $this$commonSeek.offset = offset;
            $this$commonSeek.data = null;
            $this$commonSeek.start = -1;
            $this$commonSeek.end = -1;
            return -1;
        }
        long min = 0L;
        long max = buffer2.size();
        Segment head = buffer2.head;
        Segment tail = buffer2.head;
        if ($this$commonSeek.getSegment$okio() != null) {
            long l2 = $this$commonSeek.offset;
            int n2 = $this$commonSeek.start;
            Segment segment = $this$commonSeek.getSegment$okio();
            Intrinsics.checkNotNull(segment);
            long segmentOffset = l2 - (long)(n2 - segment.pos);
            if (segmentOffset > offset) {
                max = segmentOffset;
                tail = $this$commonSeek.getSegment$okio();
            } else {
                min = segmentOffset;
                head = $this$commonSeek.getSegment$okio();
            }
        }
        Segment next = null;
        long nextOffset = 0L;
        if (max - offset > offset - min) {
            next = head;
            nextOffset = min;
            while (true) {
                Segment segment = next;
                Intrinsics.checkNotNull(segment);
                if (offset >= nextOffset + (long)(segment.limit - next.pos)) {
                    nextOffset += (long)(next.limit - next.pos);
                    next = next.next;
                    continue;
                }
                break;
            }
        } else {
            Segment segment;
            next = tail;
            for (nextOffset = max; nextOffset > offset; nextOffset -= (long)(segment.limit - next.pos)) {
                Segment segment2 = next;
                Intrinsics.checkNotNull(segment2);
                segment = next = segment2.prev;
                Intrinsics.checkNotNull(segment);
            }
        }
        if ($this$commonSeek.readWrite) {
            Segment segment = next;
            Intrinsics.checkNotNull(segment);
            if (segment.shared) {
                Segment unsharedNext = next.unsharedCopy();
                if (buffer2.head == next) {
                    buffer2.head = unsharedNext;
                }
                next = next.push(unsharedNext);
                Segment segment3 = next.prev;
                Intrinsics.checkNotNull(segment3);
                segment3.pop();
            }
        }
        $this$commonSeek.setSegment$okio(next);
        $this$commonSeek.offset = offset;
        Segment segment = next;
        Intrinsics.checkNotNull(segment);
        $this$commonSeek.data = segment.data;
        $this$commonSeek.start = next.pos + (int)(offset - nextOffset);
        $this$commonSeek.end = next.limit;
        return $this$commonSeek.end - $this$commonSeek.start;
    }

    public static final long commonResizeBuffer(@NotNull Buffer.UnsafeCursor $this$commonResizeBuffer, long newSize) {
        Intrinsics.checkNotNullParameter($this$commonResizeBuffer, "<this>");
        boolean $i$f$commonResizeBuffer = false;
        Buffer buffer = $this$commonResizeBuffer.buffer;
        if (buffer == null) {
            boolean $i$a$-checkNotNull--Buffer$commonResizeBuffer$buffer$22 = false;
            String $i$a$-checkNotNull--Buffer$commonResizeBuffer$buffer$22 = "not attached to a buffer";
            throw new IllegalStateException($i$a$-checkNotNull--Buffer$commonResizeBuffer$buffer$22.toString());
        }
        Buffer buffer2 = buffer;
        if (!$this$commonResizeBuffer.readWrite) {
            boolean bl = false;
            String string = "resizeBuffer() only permitted for read/write buffers";
            throw new IllegalStateException(string.toString());
        }
        long oldSize = buffer2.size();
        if (newSize <= oldSize) {
            int tailSize;
            if (!(newSize >= 0L)) {
                boolean $i$a$-require--Buffer$commonResizeBuffer$32 = false;
                String $i$a$-require--Buffer$commonResizeBuffer$32 = "newSize < 0: " + newSize;
                throw new IllegalArgumentException($i$a$-require--Buffer$commonResizeBuffer$32.toString());
            }
            for (long bytesToSubtract = oldSize - newSize; bytesToSubtract > 0L; bytesToSubtract -= (long)tailSize) {
                Segment tail;
                Segment segment = buffer2.head;
                Intrinsics.checkNotNull(segment);
                Segment segment2 = tail = segment.prev;
                Intrinsics.checkNotNull(segment2);
                tailSize = segment2.limit - tail.pos;
                if ((long)tailSize <= bytesToSubtract) {
                    buffer2.head = tail.pop();
                    SegmentPool.recycle(tail);
                    continue;
                }
                tail.limit -= (int)bytesToSubtract;
                break;
            }
            $this$commonResizeBuffer.setSegment$okio(null);
            $this$commonResizeBuffer.offset = newSize;
            $this$commonResizeBuffer.data = null;
            $this$commonResizeBuffer.start = -1;
            $this$commonResizeBuffer.end = -1;
        } else if (newSize > oldSize) {
            int segmentBytesToAdd;
            boolean needsToSeek = true;
            for (long bytesToAdd = newSize - oldSize; bytesToAdd > 0L; bytesToAdd -= (long)segmentBytesToAdd) {
                Segment tail = buffer2.writableSegment$okio(1);
                int b$iv = 8192 - tail.limit;
                boolean $i$f$minOf = false;
                segmentBytesToAdd = (int)Math.min(bytesToAdd, (long)b$iv);
                tail.limit += segmentBytesToAdd;
                if (!needsToSeek) continue;
                $this$commonResizeBuffer.setSegment$okio(tail);
                $this$commonResizeBuffer.offset = oldSize;
                $this$commonResizeBuffer.data = tail.data;
                $this$commonResizeBuffer.start = tail.limit - segmentBytesToAdd;
                $this$commonResizeBuffer.end = tail.limit;
                needsToSeek = false;
            }
        }
        buffer2.setSize$okio(newSize);
        return oldSize;
    }

    public static final long commonExpandBuffer(@NotNull Buffer.UnsafeCursor $this$commonExpandBuffer, int minByteCount) {
        Intrinsics.checkNotNullParameter($this$commonExpandBuffer, "<this>");
        boolean $i$f$commonExpandBuffer = false;
        if (!(minByteCount > 0)) {
            boolean $i$a$-require--Buffer$commonExpandBuffer$32 = false;
            String $i$a$-require--Buffer$commonExpandBuffer$32 = "minByteCount <= 0: " + minByteCount;
            throw new IllegalArgumentException($i$a$-require--Buffer$commonExpandBuffer$32.toString());
        }
        if (!(minByteCount <= 8192)) {
            boolean $i$a$-require--Buffer$commonExpandBuffer$42 = false;
            String $i$a$-require--Buffer$commonExpandBuffer$42 = "minByteCount > Segment.SIZE: " + minByteCount;
            throw new IllegalArgumentException($i$a$-require--Buffer$commonExpandBuffer$42.toString());
        }
        Buffer buffer = $this$commonExpandBuffer.buffer;
        if (buffer == null) {
            boolean $i$a$-checkNotNull--Buffer$commonExpandBuffer$buffer$22 = false;
            String $i$a$-checkNotNull--Buffer$commonExpandBuffer$buffer$22 = "not attached to a buffer";
            throw new IllegalStateException($i$a$-checkNotNull--Buffer$commonExpandBuffer$buffer$22.toString());
        }
        Buffer buffer2 = buffer;
        if (!$this$commonExpandBuffer.readWrite) {
            boolean bl = false;
            String string = "expandBuffer() only permitted for read/write buffers";
            throw new IllegalStateException(string.toString());
        }
        long oldSize = buffer2.size();
        Segment tail = buffer2.writableSegment$okio(minByteCount);
        int result = 8192 - tail.limit;
        tail.limit = 8192;
        buffer2.setSize$okio(oldSize + (long)result);
        $this$commonExpandBuffer.setSegment$okio(tail);
        $this$commonExpandBuffer.offset = oldSize;
        $this$commonExpandBuffer.data = tail.data;
        $this$commonExpandBuffer.start = 8192 - result;
        $this$commonExpandBuffer.end = 8192;
        return result;
    }

    public static final void commonClose(@NotNull Buffer.UnsafeCursor $this$commonClose) {
        Intrinsics.checkNotNullParameter($this$commonClose, "<this>");
        boolean $i$f$commonClose = false;
        if (!($this$commonClose.buffer != null)) {
            boolean bl = false;
            String string = "not attached to a buffer";
            throw new IllegalStateException(string.toString());
        }
        $this$commonClose.buffer = null;
        $this$commonClose.setSegment$okio(null);
        $this$commonClose.offset = -1L;
        $this$commonClose.data = null;
        $this$commonClose.start = -1;
        $this$commonClose.end = -1;
    }

    static {
        long[] lArray = new long[]{-1L, 9L, 99L, 999L, 9999L, 99999L, 999999L, 9999999L, 99999999L, 999999999L, 9999999999L, 99999999999L, 999999999999L, 9999999999999L, 99999999999999L, 999999999999999L, 9999999999999999L, 99999999999999999L, 999999999999999999L, Long.MAX_VALUE};
        DigitCountToLargestValue = lArray;
    }
}

