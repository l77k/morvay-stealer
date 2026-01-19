/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.SourceDebugExtension
 */
package okio;

import kotlin.Metadata;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import okio.Buffer;
import okio.ByteString;
import okio.internal.-ByteString;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={2, 1, 0}, k=2, xi=48, d1={"\u0000F\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\n\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0005\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a \u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0003H\u0000\u001a\f\u0010\u0006\u001a\u00020\u0007*\u00020\u0007H\u0000\u001a\f\u0010\u0006\u001a\u00020\b*\u00020\bH\u0000\u001a\f\u0010\u0006\u001a\u00020\u0003*\u00020\u0003H\u0000\u001a\u0015\u0010\t\u001a\u00020\b*\u00020\b2\u0006\u0010\n\u001a\u00020\bH\u0080\f\u001a\u0015\u0010\u000b\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\n\u001a\u00020\bH\u0080\f\u001a\u0015\u0010\f\u001a\u00020\b*\u00020\r2\u0006\u0010\u000e\u001a\u00020\bH\u0080\f\u001a\u0015\u0010\u000f\u001a\u00020\b*\u00020\r2\u0006\u0010\u000e\u001a\u00020\bH\u0080\f\u001a\u0015\u0010\u0010\u001a\u00020\b*\u00020\r2\u0006\u0010\u000e\u001a\u00020\bH\u0080\f\u001a\u0015\u0010\u0010\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0003H\u0080\f\u001a\u0015\u0010\u0011\u001a\u00020\r*\u00020\r2\u0006\u0010\u000e\u001a\u00020\rH\u0080\f\u001a\u0015\u0010\u0010\u001a\u00020\u0003*\u00020\b2\u0006\u0010\u000e\u001a\u00020\u0003H\u0080\f\u001a\u0019\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\bH\u0080\b\u001a\u0019\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\u0003H\u0080\b\u001a0\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0013\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\bH\u0000\u001a\f\u0010\u001a\u001a\u00020\u001b*\u00020\rH\u0000\u001a\f\u0010\u001a\u001a\u00020\u001b*\u00020\bH\u0000\u001a\f\u0010\u001a\u001a\u00020\u001b*\u00020\u0003H\u0000\u001a\u0010\u0010 \u001a\u00020\u001d2\u0006\u0010!\u001a\u00020\u001dH\u0000\u001a\u0014\u0010 \u001a\u00020\b*\u00020%2\u0006\u0010&\u001a\u00020\bH\u0000\u001a\u0014\u0010 \u001a\u00020\b*\u00020\u00172\u0006\u0010'\u001a\u00020\bH\u0000\"\u0014\u0010\u001c\u001a\u00020\u001dX\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001f\"\u0014\u0010\"\u001a\u00020\bX\u0080D\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$\u00a8\u0006("}, d2={"checkOffsetAndCount", "", "size", "", "offset", "byteCount", "reverseBytes", "", "", "leftRotate", "bitCount", "rightRotate", "shr", "", "other", "shl", "and", "xor", "minOf", "a", "b", "arrayRangeEquals", "", "", "aOffset", "bOffset", "toHexString", "", "DEFAULT__new_UnsafeCursor", "Lokio/Buffer$UnsafeCursor;", "getDEFAULT__new_UnsafeCursor", "()Lokio/Buffer$UnsafeCursor;", "resolveDefaultParameter", "unsafeCursor", "DEFAULT__ByteString_size", "getDEFAULT__ByteString_size", "()I", "Lokio/ByteString;", "position", "sizeParam", "okio"})
@JvmName(name="-SegmentedByteString")
@SourceDebugExtension(value={"SMAP\nUtil.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,185:1\n67#1:186\n73#1:187\n*S KotlinDebug\n*F\n+ 1 Util.kt\nokio/-SegmentedByteString\n*L\n105#1:186\n106#1:187\n*E\n"})
public final class -SegmentedByteString {
    @NotNull
    private static final Buffer.UnsafeCursor DEFAULT__new_UnsafeCursor = new Buffer.UnsafeCursor();
    private static final int DEFAULT__ByteString_size = -1234567890;

    public static final void checkOffsetAndCount(long size, long offset, long byteCount) {
        if ((offset | byteCount) < 0L || offset > size || size - offset < byteCount) {
            throw new ArrayIndexOutOfBoundsException("size=" + size + " offset=" + offset + " byteCount=" + byteCount);
        }
    }

    public static final short reverseBytes(short $this$reverseBytes) {
        int i2 = $this$reverseBytes & 0xFFFF;
        int reversed = (i2 & 0xFF00) >>> 8 | (i2 & 0xFF) << 8;
        return (short)reversed;
    }

    public static final int reverseBytes(int $this$reverseBytes) {
        return ($this$reverseBytes & 0xFF000000) >>> 24 | ($this$reverseBytes & 0xFF0000) >>> 8 | ($this$reverseBytes & 0xFF00) << 8 | ($this$reverseBytes & 0xFF) << 24;
    }

    public static final long reverseBytes(long $this$reverseBytes) {
        return ($this$reverseBytes & 0xFF00000000000000L) >>> 56 | ($this$reverseBytes & 0xFF000000000000L) >>> 40 | ($this$reverseBytes & 0xFF0000000000L) >>> 24 | ($this$reverseBytes & 0xFF00000000L) >>> 8 | ($this$reverseBytes & 0xFF000000L) << 8 | ($this$reverseBytes & 0xFF0000L) << 24 | ($this$reverseBytes & 0xFF00L) << 40 | ($this$reverseBytes & 0xFFL) << 56;
    }

    public static final int leftRotate(int $this$leftRotate, int bitCount) {
        boolean $i$f$leftRotate = false;
        return $this$leftRotate << bitCount | $this$leftRotate >>> 32 - bitCount;
    }

    public static final long rightRotate(long $this$rightRotate, int bitCount) {
        boolean $i$f$rightRotate = false;
        return $this$rightRotate >>> bitCount | $this$rightRotate << 64 - bitCount;
    }

    public static final int shr(byte $this$shr, int other) {
        boolean $i$f$shr = false;
        return $this$shr >> other;
    }

    public static final int shl(byte $this$shl, int other) {
        boolean $i$f$shl = false;
        return $this$shl << other;
    }

    public static final int and(byte $this$and, int other) {
        boolean $i$f$and = false;
        return $this$and & other;
    }

    public static final long and(byte $this$and, long other) {
        boolean $i$f$and = false;
        return (long)$this$and & other;
    }

    public static final byte xor(byte $this$xor, byte other) {
        boolean $i$f$xor = false;
        return (byte)($this$xor ^ other);
    }

    public static final long and(int $this$and, long other) {
        boolean $i$f$and = false;
        return (long)$this$and & other;
    }

    public static final long minOf(long a2, int b2) {
        boolean $i$f$minOf = false;
        return Math.min(a2, (long)b2);
    }

    public static final long minOf(int a2, long b2) {
        boolean $i$f$minOf = false;
        return Math.min((long)a2, b2);
    }

    public static final boolean arrayRangeEquals(@NotNull byte[] a2, int aOffset, @NotNull byte[] b2, int bOffset, int byteCount) {
        Intrinsics.checkNotNullParameter(a2, "a");
        Intrinsics.checkNotNullParameter(b2, "b");
        for (int i2 = 0; i2 < byteCount; ++i2) {
            if (a2[i2 + aOffset] == b2[i2 + bOffset]) continue;
            return false;
        }
        return true;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final String toHexString(byte $this$toHexString) {
        void $this$and$iv;
        byte $this$shr$iv;
        char[] result = new char[2];
        byte by = $this$toHexString;
        int other$iv = 4;
        boolean $i$f$shr = false;
        result[0] = -ByteString.getHEX_DIGIT_CHARS()[$this$shr$iv >> other$iv & 0xF];
        $this$shr$iv = $this$toHexString;
        other$iv = 15;
        boolean $i$f$and = false;
        result[1] = -ByteString.getHEX_DIGIT_CHARS()[$this$and$iv & other$iv];
        return StringsKt.concatToString(result);
    }

    @NotNull
    public static final String toHexString(int $this$toHexString) {
        int i2;
        if ($this$toHexString == 0) {
            return "0";
        }
        char[] result = new char[]{-ByteString.getHEX_DIGIT_CHARS()[$this$toHexString >> 28 & 0xF], -ByteString.getHEX_DIGIT_CHARS()[$this$toHexString >> 24 & 0xF], -ByteString.getHEX_DIGIT_CHARS()[$this$toHexString >> 20 & 0xF], -ByteString.getHEX_DIGIT_CHARS()[$this$toHexString >> 16 & 0xF], -ByteString.getHEX_DIGIT_CHARS()[$this$toHexString >> 12 & 0xF], -ByteString.getHEX_DIGIT_CHARS()[$this$toHexString >> 8 & 0xF], -ByteString.getHEX_DIGIT_CHARS()[$this$toHexString >> 4 & 0xF], -ByteString.getHEX_DIGIT_CHARS()[$this$toHexString & 0xF]};
        for (i2 = 0; i2 < result.length && result[i2] == '0'; ++i2) {
        }
        return StringsKt.concatToString(result, i2, result.length);
    }

    @NotNull
    public static final String toHexString(long $this$toHexString) {
        int i2;
        if ($this$toHexString == 0L) {
            return "0";
        }
        char[] result = new char[]{-ByteString.getHEX_DIGIT_CHARS()[(int)($this$toHexString >> 60 & 0xFL)], -ByteString.getHEX_DIGIT_CHARS()[(int)($this$toHexString >> 56 & 0xFL)], -ByteString.getHEX_DIGIT_CHARS()[(int)($this$toHexString >> 52 & 0xFL)], -ByteString.getHEX_DIGIT_CHARS()[(int)($this$toHexString >> 48 & 0xFL)], -ByteString.getHEX_DIGIT_CHARS()[(int)($this$toHexString >> 44 & 0xFL)], -ByteString.getHEX_DIGIT_CHARS()[(int)($this$toHexString >> 40 & 0xFL)], -ByteString.getHEX_DIGIT_CHARS()[(int)($this$toHexString >> 36 & 0xFL)], -ByteString.getHEX_DIGIT_CHARS()[(int)($this$toHexString >> 32 & 0xFL)], -ByteString.getHEX_DIGIT_CHARS()[(int)($this$toHexString >> 28 & 0xFL)], -ByteString.getHEX_DIGIT_CHARS()[(int)($this$toHexString >> 24 & 0xFL)], -ByteString.getHEX_DIGIT_CHARS()[(int)($this$toHexString >> 20 & 0xFL)], -ByteString.getHEX_DIGIT_CHARS()[(int)($this$toHexString >> 16 & 0xFL)], -ByteString.getHEX_DIGIT_CHARS()[(int)($this$toHexString >> 12 & 0xFL)], -ByteString.getHEX_DIGIT_CHARS()[(int)($this$toHexString >> 8 & 0xFL)], -ByteString.getHEX_DIGIT_CHARS()[(int)($this$toHexString >> 4 & 0xFL)], -ByteString.getHEX_DIGIT_CHARS()[(int)($this$toHexString & 0xFL)]};
        for (i2 = 0; i2 < result.length && result[i2] == '0'; ++i2) {
        }
        return StringsKt.concatToString(result, i2, result.length);
    }

    @NotNull
    public static final Buffer.UnsafeCursor getDEFAULT__new_UnsafeCursor() {
        return DEFAULT__new_UnsafeCursor;
    }

    @NotNull
    public static final Buffer.UnsafeCursor resolveDefaultParameter(@NotNull Buffer.UnsafeCursor unsafeCursor) {
        Intrinsics.checkNotNullParameter(unsafeCursor, "unsafeCursor");
        if (unsafeCursor == DEFAULT__new_UnsafeCursor) {
            return new Buffer.UnsafeCursor();
        }
        return unsafeCursor;
    }

    public static final int getDEFAULT__ByteString_size() {
        return DEFAULT__ByteString_size;
    }

    public static final int resolveDefaultParameter(@NotNull ByteString $this$resolveDefaultParameter, int position) {
        Intrinsics.checkNotNullParameter($this$resolveDefaultParameter, "<this>");
        if (position == DEFAULT__ByteString_size) {
            return $this$resolveDefaultParameter.size();
        }
        return position;
    }

    public static final int resolveDefaultParameter(@NotNull byte[] $this$resolveDefaultParameter, int sizeParam) {
        Intrinsics.checkNotNullParameter($this$resolveDefaultParameter, "<this>");
        if (sizeParam == DEFAULT__ByteString_size) {
            return $this$resolveDefaultParameter.length;
        }
        return sizeParam;
    }
}

