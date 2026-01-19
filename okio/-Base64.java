/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import okio.ByteString;
import okio._JvmPlatformKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={2, 1, 0}, k=2, xi=48, d1={"\u0000\u0012\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0003\u001a\u000e\u0010\u0006\u001a\u0004\u0018\u00010\u0001*\u00020\u0007H\u0000\u001a\u0016\u0010\b\u001a\u00020\u0007*\u00020\u00012\b\b\u0002\u0010\t\u001a\u00020\u0001H\u0000\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003\"\u0014\u0010\u0004\u001a\u00020\u0001X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0003\u00a8\u0006\n"}, d2={"BASE64", "", "getBASE64", "()[B", "BASE64_URL_SAFE", "getBASE64_URL_SAFE", "decodeBase64ToArray", "", "encodeBase64", "map", "okio"})
@JvmName(name="-Base64")
public final class -Base64 {
    @NotNull
    private static final byte[] BASE64 = ByteString.Companion.encodeUtf8("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/").getData$okio();
    @NotNull
    private static final byte[] BASE64_URL_SAFE = ByteString.Companion.encodeUtf8("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_").getData$okio();

    @NotNull
    public static final byte[] getBASE64() {
        return BASE64;
    }

    @NotNull
    public static final byte[] getBASE64_URL_SAFE() {
        return BASE64_URL_SAFE;
    }

    @Nullable
    public static final byte[] decodeBase64ToArray(@NotNull String $this$decodeBase64ToArray) {
        char c2;
        int limit;
        Intrinsics.checkNotNullParameter($this$decodeBase64ToArray, "<this>");
        for (limit = $this$decodeBase64ToArray.length(); limit > 0 && ((c2 = $this$decodeBase64ToArray.charAt(limit - 1)) == '=' || c2 == '\n' || c2 == '\r' || c2 == ' ' || c2 == '\t'); --limit) {
        }
        byte[] out = new byte[(int)((long)limit * 6L / 8L)];
        int outCount = 0;
        int inCount = 0;
        int word = 0;
        int n2 = limit;
        for (int pos = 0; pos < n2; ++pos) {
            char c3 = $this$decodeBase64ToArray.charAt(pos);
            int bits = 0;
            boolean bl = 'A' <= c3 ? c3 < '[' : false;
            if (bl) {
                bits = c3 - 65;
            } else {
                boolean bl2 = 'a' <= c3 ? c3 < '{' : false;
                if (bl2) {
                    bits = c3 - 71;
                } else {
                    boolean bl3 = '0' <= c3 ? c3 < ':' : false;
                    if (bl3) {
                        bits = c3 + 4;
                    } else if (c3 == '+' || c3 == '-') {
                        bits = 62;
                    } else if (c3 == '/' || c3 == '_') {
                        bits = 63;
                    } else {
                        if (c3 == '\n' || c3 == '\r' || c3 == ' ' || c3 == '\t') continue;
                        return null;
                    }
                }
            }
            word = word << 6 | bits;
            if (++inCount % 4 != 0) continue;
            out[outCount++] = (byte)(word >> 16);
            out[outCount++] = (byte)(word >> 8);
            out[outCount++] = (byte)word;
        }
        int lastWordChars = inCount % 4;
        switch (lastWordChars) {
            case 1: {
                return null;
            }
            case 2: {
                out[outCount++] = (byte)((word <<= 12) >> 16);
                break;
            }
            case 3: {
                out[outCount++] = (byte)((word <<= 6) >> 16);
                out[outCount++] = (byte)(word >> 8);
            }
        }
        if (outCount == out.length) {
            return out;
        }
        byte[] byArray = Arrays.copyOf(out, outCount);
        Intrinsics.checkNotNullExpressionValue(byArray, "copyOf(...)");
        return byArray;
    }

    @NotNull
    public static final String encodeBase64(@NotNull byte[] $this$encodeBase64, @NotNull byte[] map) {
        Intrinsics.checkNotNullParameter($this$encodeBase64, "<this>");
        Intrinsics.checkNotNullParameter(map, "map");
        int length = ($this$encodeBase64.length + 2) / 3 * 4;
        byte[] out = new byte[length];
        int index = 0;
        int end = $this$encodeBase64.length - $this$encodeBase64.length % 3;
        int i2 = 0;
        while (i2 < end) {
            byte b0 = $this$encodeBase64[i2++];
            byte b1 = $this$encodeBase64[i2++];
            byte b2 = $this$encodeBase64[i2++];
            out[index++] = map[(b0 & 0xFF) >> 2];
            out[index++] = map[(b0 & 3) << 4 | (b1 & 0xFF) >> 4];
            out[index++] = map[(b1 & 0xF) << 2 | (b2 & 0xFF) >> 6];
            out[index++] = map[b2 & 0x3F];
        }
        switch ($this$encodeBase64.length - end) {
            case 1: {
                byte b0 = $this$encodeBase64[i2];
                out[index++] = map[(b0 & 0xFF) >> 2];
                out[index++] = map[(b0 & 3) << 4];
                out[index++] = 61;
                out[index] = 61;
                break;
            }
            case 2: {
                byte b0 = $this$encodeBase64[i2++];
                byte b1 = $this$encodeBase64[i2];
                out[index++] = map[(b0 & 0xFF) >> 2];
                out[index++] = map[(b0 & 3) << 4 | (b1 & 0xFF) >> 4];
                out[index++] = map[(b1 & 0xF) << 2];
                out[index] = 61;
            }
        }
        return _JvmPlatformKt.toUtf8String(out);
    }

    public static /* synthetic */ String encodeBase64$default(byte[] byArray, byte[] byArray2, int n2, Object object) {
        if ((n2 & 1) != 0) {
            byArray2 = BASE64;
        }
        return -Base64.encodeBase64(byArray, byArray2);
    }
}

