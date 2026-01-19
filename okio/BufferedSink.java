/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import okio.Buffer;
import okio.ByteString;
import okio.Sink;
import okio.Source;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bv\u0018\u00002\u00020\u00012\u00020\u0002J\b\u0010\u0003\u001a\u00020\u0004H'J\u0010\u0010\u0007\u001a\u00020\u00002\u0006\u0010\b\u001a\u00020\tH&J \u0010\u0007\u001a\u00020\u00002\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH&J\u0010\u0010\u0007\u001a\u00020\u00002\u0006\u0010\r\u001a\u00020\u000eH&J \u0010\u0007\u001a\u00020\u00002\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH&J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\r\u001a\u00020\u0011H&J\u0018\u0010\u0007\u001a\u00020\u00002\u0006\u0010\r\u001a\u00020\u00112\u0006\u0010\f\u001a\u00020\u0010H&J\u0010\u0010\u0012\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u0014H&J \u0010\u0012\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u000b2\u0006\u0010\u0016\u001a\u00020\u000bH&J\u0010\u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u000bH&J\u0018\u0010\u0019\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u001bH&J(\u0010\u0019\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u000b2\u0006\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\u001a\u001a\u00020\u001bH&J\u0010\u0010\u001c\u001a\u00020\u00002\u0006\u0010\u001d\u001a\u00020\u000bH&J\u0010\u0010\u001e\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020\u000bH&J\u0010\u0010 \u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020\u000bH&J\u0010\u0010!\u001a\u00020\u00002\u0006\u0010\"\u001a\u00020\u000bH&J\u0010\u0010#\u001a\u00020\u00002\u0006\u0010\"\u001a\u00020\u000bH&J\u0010\u0010$\u001a\u00020\u00002\u0006\u0010%\u001a\u00020\u0010H&J\u0010\u0010&\u001a\u00020\u00002\u0006\u0010%\u001a\u00020\u0010H&J\u0010\u0010'\u001a\u00020\u00002\u0006\u0010%\u001a\u00020\u0010H&J\u0010\u0010(\u001a\u00020\u00002\u0006\u0010%\u001a\u00020\u0010H&J\b\u0010)\u001a\u00020*H&J\b\u0010+\u001a\u00020\u0000H&J\b\u0010,\u001a\u00020\u0000H&J\b\u0010-\u001a\u00020.H&R\u0012\u0010\u0003\u001a\u00020\u0004X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\u0082\u0001\u0002\u0004/\u00f8\u0001\u0000\u0082\u0002\u0006\n\u0004\b!0\u0001\u00a8\u00060\u00c0\u0006\u0001"}, d2={"Lokio/BufferedSink;", "Lokio/Sink;", "Ljava/nio/channels/WritableByteChannel;", "buffer", "Lokio/Buffer;", "getBuffer", "()Lokio/Buffer;", "write", "byteString", "Lokio/ByteString;", "offset", "", "byteCount", "source", "", "writeAll", "", "Lokio/Source;", "writeUtf8", "string", "", "beginIndex", "endIndex", "writeUtf8CodePoint", "codePoint", "writeString", "charset", "Ljava/nio/charset/Charset;", "writeByte", "b", "writeShort", "s", "writeShortLe", "writeInt", "i", "writeIntLe", "writeLong", "v", "writeLongLe", "writeDecimalLong", "writeHexadecimalUnsignedLong", "flush", "", "emit", "emitCompleteSegments", "outputStream", "Ljava/io/OutputStream;", "Lokio/RealBufferedSink;", "okio"})
public interface BufferedSink
extends Sink,
WritableByteChannel {
    @Deprecated(message="moved to val: use getBuffer() instead", replaceWith=@ReplaceWith(expression="buffer", imports={}), level=DeprecationLevel.WARNING)
    @NotNull
    public Buffer buffer();

    @NotNull
    public Buffer getBuffer();

    @NotNull
    public BufferedSink write(@NotNull ByteString var1) throws IOException;

    @NotNull
    public BufferedSink write(@NotNull ByteString var1, int var2, int var3) throws IOException;

    @NotNull
    public BufferedSink write(@NotNull byte[] var1) throws IOException;

    @NotNull
    public BufferedSink write(@NotNull byte[] var1, int var2, int var3) throws IOException;

    public long writeAll(@NotNull Source var1) throws IOException;

    @NotNull
    public BufferedSink write(@NotNull Source var1, long var2) throws IOException;

    @NotNull
    public BufferedSink writeUtf8(@NotNull String var1) throws IOException;

    @NotNull
    public BufferedSink writeUtf8(@NotNull String var1, int var2, int var3) throws IOException;

    @NotNull
    public BufferedSink writeUtf8CodePoint(int var1) throws IOException;

    @NotNull
    public BufferedSink writeString(@NotNull String var1, @NotNull Charset var2) throws IOException;

    @NotNull
    public BufferedSink writeString(@NotNull String var1, int var2, int var3, @NotNull Charset var4) throws IOException;

    @NotNull
    public BufferedSink writeByte(int var1) throws IOException;

    @NotNull
    public BufferedSink writeShort(int var1) throws IOException;

    @NotNull
    public BufferedSink writeShortLe(int var1) throws IOException;

    @NotNull
    public BufferedSink writeInt(int var1) throws IOException;

    @NotNull
    public BufferedSink writeIntLe(int var1) throws IOException;

    @NotNull
    public BufferedSink writeLong(long var1) throws IOException;

    @NotNull
    public BufferedSink writeLongLe(long var1) throws IOException;

    @NotNull
    public BufferedSink writeDecimalLong(long var1) throws IOException;

    @NotNull
    public BufferedSink writeHexadecimalUnsignedLong(long var1) throws IOException;

    @Override
    public void flush() throws IOException;

    @NotNull
    public BufferedSink emit() throws IOException;

    @NotNull
    public BufferedSink emitCompleteSegments() throws IOException;

    @NotNull
    public OutputStream outputStream();
}

