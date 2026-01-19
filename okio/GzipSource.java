/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.SourceDebugExtension
 */
package okio;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Inflater;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import okio.-SegmentedByteString;
import okio.Buffer;
import okio.InflaterSource;
import okio.RealBufferedSource;
import okio.Segment;
import okio.Source;
import okio.Timeout;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u00a2\u0006\u0004\b\u0003\u0010\u0004J\u0018\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0013H\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0002J\b\u0010\u0019\u001a\u00020\u0018H\u0002J\b\u0010\u001a\u001a\u00020\u001bH\u0016J\b\u0010\u001c\u001a\u00020\u0018H\u0016J \u0010\u001d\u001a\u00020\u00182\u0006\u0010\u001e\u001a\u00020\u00152\u0006\u0010\u001f\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u0013H\u0002J \u0010 \u001a\u00020\u00182\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020$H\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\u00060\tj\u0002`\nX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\u00060\u000fj\u0002`\u0010X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0011\u00a8\u0006&"}, d2={"Lokio/GzipSource;", "Lokio/Source;", "source", "<init>", "(Lokio/Source;)V", "section", "", "Lokio/RealBufferedSource;", "inflater", "Ljava/util/zip/Inflater;", "Lokio/Inflater;", "Ljava/util/zip/Inflater;", "inflaterSource", "Lokio/InflaterSource;", "crc", "Ljava/util/zip/CRC32;", "Lokio/internal/CRC32;", "Ljava/util/zip/CRC32;", "read", "", "sink", "Lokio/Buffer;", "byteCount", "consumeHeader", "", "consumeTrailer", "timeout", "Lokio/Timeout;", "close", "updateCrc", "buffer", "offset", "checkEqual", "name", "", "expected", "", "actual", "okio"})
@SourceDebugExtension(value={"SMAP\nGzipSource.kt\nKotlin\n*S Kotlin\n*F\n+ 1 GzipSource.kt\nokio/GzipSource\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 RealBufferedSource.kt\nokio/RealBufferedSource\n+ 4 GzipSource.kt\nokio/-GzipSourceExtensions\n+ 5 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,222:1\n1#2:223\n63#3:224\n63#3:226\n63#3:228\n63#3:229\n63#3:230\n63#3:232\n63#3:234\n204#4:225\n204#4:227\n204#4:231\n204#4:233\n88#5:235\n*S KotlinDebug\n*F\n+ 1 GzipSource.kt\nokio/GzipSource\n*L\n103#1:224\n105#1:226\n117#1:228\n118#1:229\n120#1:230\n131#1:232\n142#1:234\n104#1:225\n115#1:227\n128#1:231\n139#1:233\n185#1:235\n*E\n"})
public final class GzipSource
implements Source {
    private byte section;
    @NotNull
    private final RealBufferedSource source;
    @NotNull
    private final Inflater inflater;
    @NotNull
    private final InflaterSource inflaterSource;
    @NotNull
    private final CRC32 crc;

    public GzipSource(@NotNull Source source2) {
        Intrinsics.checkNotNullParameter(source2, "source");
        this.source = new RealBufferedSource(source2);
        this.inflater = new Inflater(true);
        this.inflaterSource = new InflaterSource(this.source, this.inflater);
        this.crc = new CRC32();
    }

    @Override
    public long read(@NotNull Buffer sink2, long byteCount) throws IOException {
        Intrinsics.checkNotNullParameter(sink2, "sink");
        if (!(byteCount >= 0L)) {
            boolean bl = false;
            String string = "byteCount < 0: " + byteCount;
            throw new IllegalArgumentException(string.toString());
        }
        if (byteCount == 0L) {
            return 0L;
        }
        if (this.section == 0) {
            this.consumeHeader();
            this.section = 1;
        }
        if (this.section == 1) {
            long offset = sink2.size();
            long result = this.inflaterSource.read(sink2, byteCount);
            if (result != -1L) {
                this.updateCrc(sink2, offset, result);
                return result;
            }
            this.section = (byte)2;
        }
        if (this.section == 2) {
            this.consumeTrailer();
            this.section = (byte)3;
            if (!this.source.exhausted()) {
                throw new IOException("gzip finished without exhausting source");
            }
        }
        return -1L;
    }

    /*
     * WARNING - void declaration
     */
    private final void consumeHeader() throws IOException {
        boolean $i$f$getBuffer;
        void $this$getBit$iv;
        byte $i$f$getBuffer2;
        void $this$getBit$iv2;
        boolean fhcrc;
        byte flags;
        this.source.require(10L);
        RealBufferedSource this_$iv = this.source;
        byte $i$f$getBuffer3 = 0;
        $i$f$getBuffer3 = flags = this_$iv.bufferField.getByte(3L);
        int bit$iv = 1;
        boolean $i$f$getBit = false;
        boolean bl = fhcrc = ($this$getBit$iv2 >> bit$iv & 1) == 1;
        if (fhcrc) {
            RealBufferedSource this_$iv2 = this.source;
            $i$f$getBuffer2 = 0;
            this.updateCrc(this_$iv2.bufferField, 0L, 10L);
        }
        short id1id2 = this.source.readShort();
        this.checkEqual("ID1ID2", 8075, id1id2);
        this.source.skip(8L);
        $i$f$getBuffer2 = flags;
        int bit$iv2 = 2;
        boolean $i$f$getBit2 = false;
        if (($this$getBit$iv >> bit$iv2 & 1) == 1) {
            this.source.require(2L);
            if (fhcrc) {
                RealBufferedSource this_$iv3 = this.source;
                boolean $i$f$getBuffer4 = false;
                this.updateCrc(this_$iv3.bufferField, 0L, 2L);
            }
            RealBufferedSource this_$iv4 = this.source;
            $i$f$getBuffer = false;
            long xlen = this_$iv4.bufferField.readShortLe() & 0xFFFF;
            this.source.require(xlen);
            if (fhcrc) {
                this_$iv4 = this.source;
                $i$f$getBuffer = false;
                this.updateCrc(this_$iv4.bufferField, 0L, xlen);
            }
            this.source.skip(xlen);
        }
        byte xlen = flags;
        bit$iv2 = 3;
        $i$f$getBit2 = false;
        if (($this$getBit$iv >> bit$iv2 & 1) == 1) {
            long index = this.source.indexOf((byte)0);
            if (index == -1L) {
                throw new EOFException();
            }
            if (fhcrc) {
                RealBufferedSource this_$iv5 = this.source;
                $i$f$getBuffer = false;
                this.updateCrc(this_$iv5.bufferField, 0L, index + 1L);
            }
            this.source.skip(index + 1L);
        }
        byte index = flags;
        bit$iv2 = 4;
        $i$f$getBit = false;
        if (($this$getBit$iv >> bit$iv2 & 1) == 1) {
            long index2 = this.source.indexOf((byte)0);
            if (index2 == -1L) {
                throw new EOFException();
            }
            if (fhcrc) {
                RealBufferedSource this_$iv6 = this.source;
                $i$f$getBuffer = false;
                this.updateCrc(this_$iv6.bufferField, 0L, index2 + 1L);
            }
            this.source.skip(index2 + 1L);
        }
        if (fhcrc) {
            this.checkEqual("FHCRC", this.source.readShortLe(), (short)this.crc.getValue());
            this.crc.reset();
        }
    }

    private final void consumeTrailer() throws IOException {
        this.checkEqual("CRC", this.source.readIntLe(), (int)this.crc.getValue());
        this.checkEqual("ISIZE", this.source.readIntLe(), (int)this.inflater.getBytesWritten());
    }

    @Override
    @NotNull
    public Timeout timeout() {
        return this.source.timeout();
    }

    @Override
    public void close() throws IOException {
        this.inflaterSource.close();
    }

    private final void updateCrc(Buffer buffer, long offset, long byteCount) {
        long offset2;
        long byteCount2 = byteCount;
        Segment segment = buffer.head;
        Intrinsics.checkNotNull(segment);
        Segment s2 = segment;
        for (offset2 = offset; offset2 >= (long)(s2.limit - s2.pos); offset2 -= (long)(s2.limit - s2.pos)) {
            Intrinsics.checkNotNull(s2.next);
        }
        while (byteCount2 > 0L) {
            int pos = (int)((long)s2.pos + offset2);
            int a$iv = s2.limit - pos;
            boolean $i$f$minOf = false;
            int toUpdate = (int)Math.min((long)a$iv, byteCount2);
            this.crc.update(s2.data, pos, toUpdate);
            byteCount2 -= (long)toUpdate;
            offset2 = 0L;
            Intrinsics.checkNotNull(s2.next);
        }
    }

    private final void checkEqual(String name, int expected, int actual) {
        if (actual != expected) {
            throw new IOException(name + ": actual 0x" + StringsKt.padStart(-SegmentedByteString.toHexString(actual), 8, '0') + " != expected 0x" + StringsKt.padStart(-SegmentedByteString.toHexString(expected), 8, '0'));
        }
    }
}

