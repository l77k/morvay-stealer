/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.SourceDebugExtension
 */
package okio.internal;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import okio.-Base64;
import okio.-SegmentedByteString;
import okio.Buffer;
import okio.ByteString;
import okio._JvmPlatformKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={2, 1, 0}, k=2, xi=48, d1={"\u0000V\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0019\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0010\u0000\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\f\n\u0002\b\u0005\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0080\b\u001a\r\u0010\u0003\u001a\u00020\u0001*\u00020\u0002H\u0080\b\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0002H\u0080\b\u001a\r\u0010\t\u001a\u00020\u0001*\u00020\u0002H\u0080\b\u001a\r\u0010\n\u001a\u00020\u0002*\u00020\u0002H\u0080\b\u001a\r\u0010\u000b\u001a\u00020\u0002*\u00020\u0002H\u0080\b\u001a\u001d\u0010\f\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u0080\b\u001a\u0015\u0010\u0010\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u000eH\u0080\b\u001a\r\u0010\u0013\u001a\u00020\u000e*\u00020\u0002H\u0080\b\u001a\r\u0010\u0014\u001a\u00020\u0015*\u00020\u0002H\u0080\b\u001a\r\u0010\u0016\u001a\u00020\u0015*\u00020\u0002H\u0080\b\u001a-\u0010\u0017\u001a\u00020\u0018*\u00020\u00022\u0006\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u000eH\u0080\b\u001a-\u0010\u0017\u001a\u00020\u0018*\u00020\u00022\u0006\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u00152\u0006\u0010\u001b\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u000eH\u0080\b\u001a-\u0010\u001d\u001a\u00020\u001e*\u00020\u00022\u0006\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u001f\u001a\u00020\u00152\u0006\u0010 \u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u000eH\u0080\b\u001a\u0015\u0010!\u001a\u00020\u0018*\u00020\u00022\u0006\u0010\"\u001a\u00020\u0002H\u0080\b\u001a\u0015\u0010!\u001a\u00020\u0018*\u00020\u00022\u0006\u0010\"\u001a\u00020\u0015H\u0080\b\u001a\u0015\u0010#\u001a\u00020\u0018*\u00020\u00022\u0006\u0010$\u001a\u00020\u0002H\u0080\b\u001a\u0015\u0010#\u001a\u00020\u0018*\u00020\u00022\u0006\u0010$\u001a\u00020\u0015H\u0080\b\u001a\u001d\u0010%\u001a\u00020\u000e*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00152\u0006\u0010&\u001a\u00020\u000eH\u0080\b\u001a\u001d\u0010'\u001a\u00020\u000e*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00022\u0006\u0010&\u001a\u00020\u000eH\u0080\b\u001a\u001d\u0010'\u001a\u00020\u000e*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00152\u0006\u0010&\u001a\u00020\u000eH\u0080\b\u001a\u0017\u0010(\u001a\u00020\u0018*\u00020\u00022\b\u0010\u001a\u001a\u0004\u0018\u00010)H\u0080\b\u001a\r\u0010*\u001a\u00020\u000e*\u00020\u0002H\u0080\b\u001a\u0015\u0010+\u001a\u00020\u000e*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002H\u0080\b\u001a\u0011\u0010,\u001a\u00020\u00022\u0006\u0010-\u001a\u00020\u0015H\u0080\b\u001a\u001d\u0010.\u001a\u00020\u0002*\u00020\u00152\u0006\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u000eH\u0080\b\u001a\r\u0010/\u001a\u00020\u0002*\u00020\u0001H\u0080\b\u001a\u000f\u00100\u001a\u0004\u0018\u00010\u0002*\u00020\u0001H\u0080\b\u001a\r\u00101\u001a\u00020\u0002*\u00020\u0001H\u0080\b\u001a$\u00102\u001a\u00020\u001e*\u00020\u00022\u0006\u00103\u001a\u0002042\u0006\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u000eH\u0000\u001a\u0010\u00105\u001a\u00020\u000e2\u0006\u00106\u001a\u000207H\u0002\u001a\r\u00108\u001a\u00020\u0001*\u00020\u0002H\u0080\b\u001a\u0018\u00109\u001a\u00020\u000e2\u0006\u0010:\u001a\u00020\u00152\u0006\u0010;\u001a\u00020\u000eH\u0002\"\u0014\u0010\u0005\u001a\u00020\u0006X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b\u00a8\u0006<"}, d2={"commonUtf8", "", "Lokio/ByteString;", "commonBase64", "commonBase64Url", "HEX_DIGIT_CHARS", "", "getHEX_DIGIT_CHARS", "()[C", "commonHex", "commonToAsciiLowercase", "commonToAsciiUppercase", "commonSubstring", "beginIndex", "", "endIndex", "commonGetByte", "", "pos", "commonGetSize", "commonToByteArray", "", "commonInternalArray", "commonRangeEquals", "", "offset", "other", "otherOffset", "byteCount", "commonCopyInto", "", "target", "targetOffset", "commonStartsWith", "prefix", "commonEndsWith", "suffix", "commonIndexOf", "fromIndex", "commonLastIndexOf", "commonEquals", "", "commonHashCode", "commonCompareTo", "commonOf", "data", "commonToByteString", "commonEncodeUtf8", "commonDecodeBase64", "commonDecodeHex", "commonWrite", "buffer", "Lokio/Buffer;", "decodeHexDigit", "c", "", "commonToString", "codePointIndexToCharIndex", "s", "codePointCount", "okio"})
@JvmName(name="-ByteString")
@SourceDebugExtension(value={"SMAP\nByteString.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ByteString.kt\nokio/internal/-ByteString\n+ 2 Util.kt\nokio/-SegmentedByteString\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 Utf8.kt\nokio/Utf8\n*L\n1#1,361:1\n129#1,2:367\n131#1,9:370\n67#2:362\n73#2:363\n73#2:365\n73#2:366\n67#2:394\n73#2:406\n1#3:364\n1#3:369\n212#4,7:379\n122#4:386\n219#4,5:387\n122#4:392\n226#4:393\n228#4:395\n397#4,2:396\n122#4:398\n400#4,6:399\n127#4:405\n406#4:407\n122#4:408\n407#4,13:409\n122#4:422\n422#4:423\n122#4:424\n425#4:425\n230#4,3:426\n440#4,3:429\n122#4:432\n443#4:433\n127#4:434\n446#4,10:435\n127#4:445\n456#4:446\n122#4:447\n457#4,4:448\n127#4:452\n461#4:453\n122#4:454\n462#4,14:455\n122#4:469\n477#4,2:470\n122#4:472\n481#4:473\n122#4:474\n484#4:475\n234#4,3:476\n500#4,3:479\n122#4:482\n503#4:483\n127#4:484\n506#4,2:485\n127#4:487\n510#4,10:488\n127#4:498\n520#4:499\n122#4:500\n521#4,4:501\n127#4:505\n525#4:506\n122#4:507\n526#4,4:508\n127#4:512\n530#4:513\n122#4:514\n531#4,15:515\n122#4:530\n547#4,2:531\n122#4:533\n550#4,2:534\n122#4:536\n554#4:537\n122#4:538\n557#4:539\n241#4:540\n122#4:541\n242#4,5:542\n*S KotlinDebug\n*F\n+ 1 ByteString.kt\nokio/internal/-ByteString\n*L\n327#1:367,2\n327#1:370,9\n65#1:362\n66#1:363\n256#1:365\n257#1:366\n346#1:394\n346#1:406\n327#1:369\n346#1:379,7\n351#1:386\n346#1:387,5\n351#1:392\n346#1:393\n346#1:395\n346#1:396,2\n351#1:398\n346#1:399,6\n346#1:405\n346#1:407\n351#1:408\n346#1:409,13\n351#1:422\n346#1:423\n351#1:424\n346#1:425\n346#1:426,3\n346#1:429,3\n351#1:432\n346#1:433\n346#1:434\n346#1:435,10\n346#1:445\n346#1:446\n351#1:447\n346#1:448,4\n346#1:452\n346#1:453\n351#1:454\n346#1:455,14\n351#1:469\n346#1:470,2\n351#1:472\n346#1:473\n351#1:474\n346#1:475\n346#1:476,3\n346#1:479,3\n351#1:482\n346#1:483\n346#1:484\n346#1:485,2\n346#1:487\n346#1:488,10\n346#1:498\n346#1:499\n351#1:500\n346#1:501,4\n346#1:505\n346#1:506\n351#1:507\n346#1:508,4\n346#1:512\n346#1:513\n351#1:514\n346#1:515,15\n351#1:530\n346#1:531,2\n351#1:533\n346#1:534,2\n351#1:536\n346#1:537\n351#1:538\n346#1:539\n346#1:540\n351#1:541\n346#1:542,5\n*E\n"})
public final class -ByteString {
    @NotNull
    private static final char[] HEX_DIGIT_CHARS;

    @NotNull
    public static final String commonUtf8(@NotNull ByteString $this$commonUtf8) {
        Intrinsics.checkNotNullParameter($this$commonUtf8, "<this>");
        boolean $i$f$commonUtf8 = false;
        String result = $this$commonUtf8.getUtf8$okio();
        if (result == null) {
            result = _JvmPlatformKt.toUtf8String($this$commonUtf8.internalArray$okio());
            $this$commonUtf8.setUtf8$okio(result);
        }
        return result;
    }

    @NotNull
    public static final String commonBase64(@NotNull ByteString $this$commonBase64) {
        Intrinsics.checkNotNullParameter($this$commonBase64, "<this>");
        boolean $i$f$commonBase64 = false;
        return -Base64.encodeBase64$default($this$commonBase64.getData$okio(), null, 1, null);
    }

    @NotNull
    public static final String commonBase64Url(@NotNull ByteString $this$commonBase64Url) {
        Intrinsics.checkNotNullParameter($this$commonBase64Url, "<this>");
        boolean $i$f$commonBase64Url = false;
        return -Base64.encodeBase64($this$commonBase64Url.getData$okio(), -Base64.getBASE64_URL_SAFE());
    }

    @NotNull
    public static final char[] getHEX_DIGIT_CHARS() {
        return HEX_DIGIT_CHARS;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final String commonHex(@NotNull ByteString $this$commonHex) {
        Intrinsics.checkNotNullParameter($this$commonHex, "<this>");
        boolean $i$f$commonHex = false;
        char[] result = new char[$this$commonHex.getData$okio().length * 2];
        int c2 = 0;
        for (byte b2 : $this$commonHex.getData$okio()) {
            void $this$and$iv;
            byte $this$shr$iv;
            int n2 = c2++;
            byte by = b2;
            int other$iv = 4;
            boolean $i$f$shr = false;
            result[n2] = -ByteString.getHEX_DIGIT_CHARS()[$this$shr$iv >> other$iv & 0xF];
            int n3 = c2++;
            $this$shr$iv = b2;
            other$iv = 15;
            boolean $i$f$and = false;
            result[n3] = -ByteString.getHEX_DIGIT_CHARS()[$this$and$iv & other$iv];
        }
        return StringsKt.concatToString(result);
    }

    @NotNull
    public static final ByteString commonToAsciiLowercase(@NotNull ByteString $this$commonToAsciiLowercase) {
        Intrinsics.checkNotNullParameter($this$commonToAsciiLowercase, "<this>");
        boolean $i$f$commonToAsciiLowercase = false;
        for (int i2 = 0; i2 < $this$commonToAsciiLowercase.getData$okio().length; ++i2) {
            byte c2 = $this$commonToAsciiLowercase.getData$okio()[i2];
            if (c2 < 65 || c2 > 90) {
                continue;
            }
            byte[] byArray = $this$commonToAsciiLowercase.getData$okio();
            byte[] byArray2 = Arrays.copyOf(byArray, byArray.length);
            Intrinsics.checkNotNullExpressionValue(byArray2, "copyOf(...)");
            byte[] lowercase = byArray2;
            lowercase[i2++] = (byte)(c2 - -32);
            while (i2 < lowercase.length) {
                c2 = lowercase[i2];
                if (c2 < 65 || c2 > 90) {
                    ++i2;
                    continue;
                }
                lowercase[i2] = (byte)(c2 - -32);
                ++i2;
            }
            return new ByteString(lowercase);
        }
        return $this$commonToAsciiLowercase;
    }

    @NotNull
    public static final ByteString commonToAsciiUppercase(@NotNull ByteString $this$commonToAsciiUppercase) {
        Intrinsics.checkNotNullParameter($this$commonToAsciiUppercase, "<this>");
        boolean $i$f$commonToAsciiUppercase = false;
        for (int i2 = 0; i2 < $this$commonToAsciiUppercase.getData$okio().length; ++i2) {
            byte c2 = $this$commonToAsciiUppercase.getData$okio()[i2];
            if (c2 < 97 || c2 > 122) {
                continue;
            }
            byte[] byArray = $this$commonToAsciiUppercase.getData$okio();
            byte[] byArray2 = Arrays.copyOf(byArray, byArray.length);
            Intrinsics.checkNotNullExpressionValue(byArray2, "copyOf(...)");
            byte[] lowercase = byArray2;
            lowercase[i2++] = (byte)(c2 - 32);
            while (i2 < lowercase.length) {
                c2 = lowercase[i2];
                if (c2 < 97 || c2 > 122) {
                    ++i2;
                    continue;
                }
                lowercase[i2] = (byte)(c2 - 32);
                ++i2;
            }
            return new ByteString(lowercase);
        }
        return $this$commonToAsciiUppercase;
    }

    @NotNull
    public static final ByteString commonSubstring(@NotNull ByteString $this$commonSubstring, int beginIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$commonSubstring, "<this>");
        boolean $i$f$commonSubstring = false;
        int endIndex2 = -SegmentedByteString.resolveDefaultParameter($this$commonSubstring, endIndex);
        if (!(beginIndex >= 0)) {
            boolean $i$a$-require--ByteString$commonSubstring$42 = false;
            String $i$a$-require--ByteString$commonSubstring$42 = "beginIndex < 0";
            throw new IllegalArgumentException($i$a$-require--ByteString$commonSubstring$42.toString());
        }
        if (!(endIndex2 <= $this$commonSubstring.getData$okio().length)) {
            boolean bl = false;
            String string = "endIndex > length(" + $this$commonSubstring.getData$okio().length + ')';
            throw new IllegalArgumentException(string.toString());
        }
        int subLen = endIndex2 - beginIndex;
        if (!(subLen >= 0)) {
            boolean bl = false;
            String string = "endIndex < beginIndex";
            throw new IllegalArgumentException(string.toString());
        }
        if (beginIndex == 0 && endIndex2 == $this$commonSubstring.getData$okio().length) {
            return $this$commonSubstring;
        }
        byte[] byArray = $this$commonSubstring.getData$okio();
        return new ByteString(ArraysKt.copyOfRange(byArray, beginIndex, endIndex2));
    }

    public static final byte commonGetByte(@NotNull ByteString $this$commonGetByte, int pos) {
        Intrinsics.checkNotNullParameter($this$commonGetByte, "<this>");
        boolean $i$f$commonGetByte = false;
        return $this$commonGetByte.getData$okio()[pos];
    }

    public static final int commonGetSize(@NotNull ByteString $this$commonGetSize) {
        Intrinsics.checkNotNullParameter($this$commonGetSize, "<this>");
        boolean $i$f$commonGetSize = false;
        return $this$commonGetSize.getData$okio().length;
    }

    @NotNull
    public static final byte[] commonToByteArray(@NotNull ByteString $this$commonToByteArray) {
        Intrinsics.checkNotNullParameter($this$commonToByteArray, "<this>");
        boolean $i$f$commonToByteArray = false;
        byte[] byArray = $this$commonToByteArray.getData$okio();
        byte[] byArray2 = Arrays.copyOf(byArray, byArray.length);
        Intrinsics.checkNotNullExpressionValue(byArray2, "copyOf(...)");
        return byArray2;
    }

    @NotNull
    public static final byte[] commonInternalArray(@NotNull ByteString $this$commonInternalArray) {
        Intrinsics.checkNotNullParameter($this$commonInternalArray, "<this>");
        boolean $i$f$commonInternalArray = false;
        return $this$commonInternalArray.getData$okio();
    }

    public static final boolean commonRangeEquals(@NotNull ByteString $this$commonRangeEquals, int offset, @NotNull ByteString other, int otherOffset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonRangeEquals, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        boolean $i$f$commonRangeEquals = false;
        return other.rangeEquals(otherOffset, $this$commonRangeEquals.getData$okio(), offset, byteCount);
    }

    public static final boolean commonRangeEquals(@NotNull ByteString $this$commonRangeEquals, int offset, @NotNull byte[] other, int otherOffset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonRangeEquals, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        boolean $i$f$commonRangeEquals = false;
        return offset >= 0 && offset <= $this$commonRangeEquals.getData$okio().length - byteCount && otherOffset >= 0 && otherOffset <= other.length - byteCount && -SegmentedByteString.arrayRangeEquals($this$commonRangeEquals.getData$okio(), offset, other, otherOffset, byteCount);
    }

    public static final void commonCopyInto(@NotNull ByteString $this$commonCopyInto, int offset, @NotNull byte[] target, int targetOffset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonCopyInto, "<this>");
        Intrinsics.checkNotNullParameter(target, "target");
        boolean $i$f$commonCopyInto = false;
        ArraysKt.copyInto($this$commonCopyInto.getData$okio(), target, targetOffset, offset, offset + byteCount);
    }

    public static final boolean commonStartsWith(@NotNull ByteString $this$commonStartsWith, @NotNull ByteString prefix) {
        Intrinsics.checkNotNullParameter($this$commonStartsWith, "<this>");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        boolean $i$f$commonStartsWith = false;
        return $this$commonStartsWith.rangeEquals(0, prefix, 0, prefix.size());
    }

    public static final boolean commonStartsWith(@NotNull ByteString $this$commonStartsWith, @NotNull byte[] prefix) {
        Intrinsics.checkNotNullParameter($this$commonStartsWith, "<this>");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        boolean $i$f$commonStartsWith = false;
        return $this$commonStartsWith.rangeEquals(0, prefix, 0, prefix.length);
    }

    public static final boolean commonEndsWith(@NotNull ByteString $this$commonEndsWith, @NotNull ByteString suffix) {
        Intrinsics.checkNotNullParameter($this$commonEndsWith, "<this>");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        boolean $i$f$commonEndsWith = false;
        return $this$commonEndsWith.rangeEquals($this$commonEndsWith.size() - suffix.size(), suffix, 0, suffix.size());
    }

    public static final boolean commonEndsWith(@NotNull ByteString $this$commonEndsWith, @NotNull byte[] suffix) {
        Intrinsics.checkNotNullParameter($this$commonEndsWith, "<this>");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        boolean $i$f$commonEndsWith = false;
        return $this$commonEndsWith.rangeEquals($this$commonEndsWith.size() - suffix.length, suffix, 0, suffix.length);
    }

    public static final int commonIndexOf(@NotNull ByteString $this$commonIndexOf, @NotNull byte[] other, int fromIndex) {
        Intrinsics.checkNotNullParameter($this$commonIndexOf, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        boolean $i$f$commonIndexOf = false;
        int limit = $this$commonIndexOf.getData$okio().length - other.length;
        int i2 = Math.max(fromIndex, 0);
        if (i2 <= limit) {
            while (true) {
                if (-SegmentedByteString.arrayRangeEquals($this$commonIndexOf.getData$okio(), i2, other, 0, other.length)) {
                    return i2;
                }
                if (i2 == limit) break;
                ++i2;
            }
        }
        return -1;
    }

    public static final int commonLastIndexOf(@NotNull ByteString $this$commonLastIndexOf, @NotNull ByteString other, int fromIndex) {
        Intrinsics.checkNotNullParameter($this$commonLastIndexOf, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        boolean $i$f$commonLastIndexOf = false;
        return $this$commonLastIndexOf.lastIndexOf(other.internalArray$okio(), fromIndex);
    }

    public static final int commonLastIndexOf(@NotNull ByteString $this$commonLastIndexOf, @NotNull byte[] other, int fromIndex) {
        Intrinsics.checkNotNullParameter($this$commonLastIndexOf, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        boolean $i$f$commonLastIndexOf = false;
        int fromIndex2 = -SegmentedByteString.resolveDefaultParameter($this$commonLastIndexOf, fromIndex);
        int limit = $this$commonLastIndexOf.getData$okio().length - other.length;
        for (int i2 = Math.min(fromIndex2, limit); -1 < i2; --i2) {
            if (!-SegmentedByteString.arrayRangeEquals($this$commonLastIndexOf.getData$okio(), i2, other, 0, other.length)) continue;
            return i2;
        }
        return -1;
    }

    public static final boolean commonEquals(@NotNull ByteString $this$commonEquals, @Nullable Object other) {
        Intrinsics.checkNotNullParameter($this$commonEquals, "<this>");
        boolean $i$f$commonEquals = false;
        return other == $this$commonEquals ? true : (other instanceof ByteString ? ((ByteString)other).size() == $this$commonEquals.getData$okio().length && ((ByteString)other).rangeEquals(0, $this$commonEquals.getData$okio(), 0, $this$commonEquals.getData$okio().length) : false);
    }

    public static final int commonHashCode(@NotNull ByteString $this$commonHashCode) {
        int n2;
        Intrinsics.checkNotNullParameter($this$commonHashCode, "<this>");
        boolean $i$f$commonHashCode = false;
        int result = $this$commonHashCode.getHashCode$okio();
        if (result != 0) {
            return result;
        }
        int it = n2 = Arrays.hashCode($this$commonHashCode.getData$okio());
        boolean bl = false;
        $this$commonHashCode.setHashCode$okio(it);
        return n2;
    }

    /*
     * WARNING - void declaration
     */
    public static final int commonCompareTo(@NotNull ByteString $this$commonCompareTo, @NotNull ByteString other) {
        Intrinsics.checkNotNullParameter($this$commonCompareTo, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        boolean $i$f$commonCompareTo = false;
        int sizeA = $this$commonCompareTo.size();
        int sizeB = other.size();
        int size = Math.min(sizeA, sizeB);
        for (int i2 = 0; i2 < size; ++i2) {
            void $this$and$iv;
            void $this$and$iv2;
            byte by = $this$commonCompareTo.getByte(i2);
            int other$iv = 255;
            boolean $i$f$and = false;
            int byteA = $this$and$iv2 & other$iv;
            other$iv = other.getByte(i2);
            int other$iv2 = 255;
            boolean $i$f$and2 = false;
            int byteB = $this$and$iv & other$iv2;
            if (byteA == byteB) {
                continue;
            }
            return byteA < byteB ? -1 : 1;
        }
        if (sizeA == sizeB) {
            return 0;
        }
        return sizeA < sizeB ? -1 : 1;
    }

    @NotNull
    public static final ByteString commonOf(@NotNull byte[] data) {
        Intrinsics.checkNotNullParameter(data, "data");
        boolean $i$f$commonOf = false;
        byte[] byArray = Arrays.copyOf(data, data.length);
        Intrinsics.checkNotNullExpressionValue(byArray, "copyOf(...)");
        return new ByteString(byArray);
    }

    @NotNull
    public static final ByteString commonToByteString(@NotNull byte[] $this$commonToByteString, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonToByteString, "<this>");
        boolean $i$f$commonToByteString = false;
        int byteCount2 = -SegmentedByteString.resolveDefaultParameter($this$commonToByteString, byteCount);
        -SegmentedByteString.checkOffsetAndCount($this$commonToByteString.length, offset, byteCount2);
        byte[] byArray = $this$commonToByteString;
        int n2 = offset + byteCount2;
        return new ByteString(ArraysKt.copyOfRange(byArray, offset, n2));
    }

    @NotNull
    public static final ByteString commonEncodeUtf8(@NotNull String $this$commonEncodeUtf8) {
        Intrinsics.checkNotNullParameter($this$commonEncodeUtf8, "<this>");
        boolean $i$f$commonEncodeUtf8 = false;
        ByteString byteString = new ByteString(_JvmPlatformKt.asUtf8ToByteArray($this$commonEncodeUtf8));
        byteString.setUtf8$okio($this$commonEncodeUtf8);
        return byteString;
    }

    @Nullable
    public static final ByteString commonDecodeBase64(@NotNull String $this$commonDecodeBase64) {
        Intrinsics.checkNotNullParameter($this$commonDecodeBase64, "<this>");
        boolean $i$f$commonDecodeBase64 = false;
        byte[] decoded = -Base64.decodeBase64ToArray($this$commonDecodeBase64);
        return decoded != null ? new ByteString(decoded) : null;
    }

    @NotNull
    public static final ByteString commonDecodeHex(@NotNull String $this$commonDecodeHex) {
        Intrinsics.checkNotNullParameter($this$commonDecodeHex, "<this>");
        boolean $i$f$commonDecodeHex = false;
        if (!($this$commonDecodeHex.length() % 2 == 0)) {
            boolean $i$a$-require--ByteString$commonDecodeHex$22 = false;
            String $i$a$-require--ByteString$commonDecodeHex$22 = "Unexpected hex string: " + $this$commonDecodeHex;
            throw new IllegalArgumentException($i$a$-require--ByteString$commonDecodeHex$22.toString());
        }
        byte[] result = new byte[$this$commonDecodeHex.length() / 2];
        int n2 = result.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            int d1 = -ByteString.decodeHexDigit($this$commonDecodeHex.charAt(i2 * 2)) << 4;
            int d2 = -ByteString.decodeHexDigit($this$commonDecodeHex.charAt(i2 * 2 + 1));
            result[i2] = (byte)(d1 + d2);
        }
        return new ByteString(result);
    }

    public static final void commonWrite(@NotNull ByteString $this$commonWrite, @NotNull Buffer buffer, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonWrite, "<this>");
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        buffer.write($this$commonWrite.getData$okio(), offset, byteCount);
    }

    private static final int decodeHexDigit(char c2) {
        int n2;
        char c3 = c2;
        boolean bl = '0' <= c3 ? c3 < ':' : false;
        if (bl) {
            n2 = c2 - 48;
        } else {
            boolean bl2 = 'a' <= c3 ? c3 < 'g' : false;
            if (bl2) {
                n2 = c2 - 97 + 10;
            } else {
                boolean bl3 = 'A' <= c3 ? c3 < 'G' : false;
                if (bl3) {
                    n2 = c2 - 65 + 10;
                } else {
                    throw new IllegalArgumentException("Unexpected hex digit: " + c2);
                }
            }
        }
        return n2;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final String commonToString(@NotNull ByteString $this$commonToString) {
        Intrinsics.checkNotNullParameter($this$commonToString, "<this>");
        boolean $i$f$commonToString = false;
        if ($this$commonToString.getData$okio().length == 0) {
            return "[size=0]";
        }
        int i2 = -ByteString.codePointIndexToCharIndex($this$commonToString.getData$okio(), 64);
        if (i2 == -1) {
            String string;
            if ($this$commonToString.getData$okio().length <= 64) {
                string = "[hex=" + $this$commonToString.hex() + ']';
            } else {
                ByteString byteString;
                void beginIndex$iv;
                void $this$commonSubstring$iv;
                StringBuilder stringBuilder = new StringBuilder().append("[size=").append($this$commonToString.getData$okio().length).append(" hex=");
                ByteString byteString2 = $this$commonToString;
                boolean bl = false;
                int endIndex$iv = 64;
                boolean $i$f$commonSubstring = false;
                int endIndex$iv2 = -SegmentedByteString.resolveDefaultParameter((ByteString)$this$commonSubstring$iv, endIndex$iv);
                if (!(endIndex$iv2 <= $this$commonSubstring$iv.getData$okio().length)) {
                    boolean bl2 = false;
                    String string2 = "endIndex > length(" + $this$commonSubstring$iv.getData$okio().length + ')';
                    throw new IllegalArgumentException(string2.toString());
                }
                int subLen$iv = endIndex$iv2 - beginIndex$iv;
                if (!(subLen$iv >= 0)) {
                    boolean bl3 = false;
                    String string3 = "endIndex < beginIndex";
                    throw new IllegalArgumentException(string3.toString());
                }
                if (endIndex$iv2 == $this$commonSubstring$iv.getData$okio().length) {
                    byteString = $this$commonSubstring$iv;
                } else {
                    byte[] byArray = $this$commonSubstring$iv.getData$okio();
                    ByteString byteString3 = new ByteString(ArraysKt.copyOfRange(byArray, (int)beginIndex$iv, endIndex$iv2));
                    byteString = byteString3;
                }
                string = stringBuilder.append(byteString.hex()).append("\u2026]").toString();
            }
            return string;
        }
        String text = $this$commonToString.utf8();
        String string = text.substring(0, i2);
        Intrinsics.checkNotNullExpressionValue(string, "substring(...)");
        String safeText = StringsKt.replace$default(StringsKt.replace$default(StringsKt.replace$default(string, "\\", "\\\\", false, 4, null), "\n", "\\n", false, 4, null), "\r", "\\r", false, 4, null);
        return i2 < text.length() ? "[size=" + $this$commonToString.getData$okio().length + " text=" + safeText + "\u2026]" : "[text=" + safeText + ']';
    }

    /*
     * Unable to fully structure code
     */
    private static final int codePointIndexToCharIndex(byte[] s, int codePointCount) {
        charCount = 0;
        j = 0;
        var4_4 = s;
        var5_5 = false;
        endIndex$iv = s.length;
        $i$f$processUtf8CodePoints = false;
        index$iv = beginIndex$iv;
        while (index$iv < endIndex$iv) {
            block221: {
                block220: {
                    block219: {
                        block183: {
                            block190: {
                                block207: {
                                    block218: {
                                        block217: {
                                            block216: {
                                                block212: {
                                                    block215: {
                                                        block214: {
                                                            block213: {
                                                                block208: {
                                                                    block211: {
                                                                        block210: {
                                                                            block209: {
                                                                                block203: {
                                                                                    block206: {
                                                                                        block205: {
                                                                                            block204: {
                                                                                                block199: {
                                                                                                    block202: {
                                                                                                        block201: {
                                                                                                            block200: {
                                                                                                                block195: {
                                                                                                                    block198: {
                                                                                                                        block197: {
                                                                                                                            block196: {
                                                                                                                                block191: {
                                                                                                                                    block194: {
                                                                                                                                        block193: {
                                                                                                                                            block192: {
                                                                                                                                                block184: {
                                                                                                                                                    block189: {
                                                                                                                                                        block188: {
                                                                                                                                                            block187: {
                                                                                                                                                                block186: {
                                                                                                                                                                    block185: {
                                                                                                                                                                        block157: {
                                                                                                                                                                            block162: {
                                                                                                                                                                                block175: {
                                                                                                                                                                                    block182: {
                                                                                                                                                                                        block181: {
                                                                                                                                                                                            block180: {
                                                                                                                                                                                                block176: {
                                                                                                                                                                                                    block179: {
                                                                                                                                                                                                        block178: {
                                                                                                                                                                                                            block177: {
                                                                                                                                                                                                                block171: {
                                                                                                                                                                                                                    block174: {
                                                                                                                                                                                                                        block173: {
                                                                                                                                                                                                                            block172: {
                                                                                                                                                                                                                                block167: {
                                                                                                                                                                                                                                    block170: {
                                                                                                                                                                                                                                        block169: {
                                                                                                                                                                                                                                            block168: {
                                                                                                                                                                                                                                                block163: {
                                                                                                                                                                                                                                                    block166: {
                                                                                                                                                                                                                                                        block165: {
                                                                                                                                                                                                                                                            block164: {
                                                                                                                                                                                                                                                                block158: {
                                                                                                                                                                                                                                                                    block161: {
                                                                                                                                                                                                                                                                        block160: {
                                                                                                                                                                                                                                                                            block159: {
                                                                                                                                                                                                                                                                                block139: {
                                                                                                                                                                                                                                                                                    block144: {
                                                                                                                                                                                                                                                                                        block153: {
                                                                                                                                                                                                                                                                                            block156: {
                                                                                                                                                                                                                                                                                                block155: {
                                                                                                                                                                                                                                                                                                    block154: {
                                                                                                                                                                                                                                                                                                        block149: {
                                                                                                                                                                                                                                                                                                            block152: {
                                                                                                                                                                                                                                                                                                                block151: {
                                                                                                                                                                                                                                                                                                                    block150: {
                                                                                                                                                                                                                                                                                                                        block145: {
                                                                                                                                                                                                                                                                                                                            block148: {
                                                                                                                                                                                                                                                                                                                                block147: {
                                                                                                                                                                                                                                                                                                                                    block146: {
                                                                                                                                                                                                                                                                                                                                        block140: {
                                                                                                                                                                                                                                                                                                                                            block143: {
                                                                                                                                                                                                                                                                                                                                                block142: {
                                                                                                                                                                                                                                                                                                                                                    block141: {
                                                                                                                                                                                                                                                                                                                                                        block132: {
                                                                                                                                                                                                                                                                                                                                                            block135: {
                                                                                                                                                                                                                                                                                                                                                                block134: {
                                                                                                                                                                                                                                                                                                                                                                    block133: {
                                                                                                                                                                                                                                                                                                                                                                        b0$iv = $this$processUtf8CodePoints$iv[index$iv];
                                                                                                                                                                                                                                                                                                                                                                        if (b0$iv < 0) break block132;
                                                                                                                                                                                                                                                                                                                                                                        c = b0$iv;
                                                                                                                                                                                                                                                                                                                                                                        $i$a$-processUtf8CodePoints--ByteString$codePointIndexToCharIndex$1 = false;
                                                                                                                                                                                                                                                                                                                                                                        var12_12 = j;
                                                                                                                                                                                                                                                                                                                                                                        j = var12_12 + 1;
                                                                                                                                                                                                                                                                                                                                                                        if (var12_12 == codePointCount) {
                                                                                                                                                                                                                                                                                                                                                                            return charCount;
                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                        if (c == 10 || c == 13) break block133;
                                                                                                                                                                                                                                                                                                                                                                        $i$f$isIsoControl = 0;
                                                                                                                                                                                                                                                                                                                                                                        if (0 <= c ? c < 32 : false) ** GOTO lbl-1000
                                                                                                                                                                                                                                                                                                                                                                        v0 = 127 <= c ? c < 160 : false;
                                                                                                                                                                                                                                                                                                                                                                        if (v0) lbl-1000:
                                                                                                                                                                                                                                                                                                                                                                        // 2 sources

                                                                                                                                                                                                                                                                                                                                                                        {
                                                                                                                                                                                                                                                                                                                                                                            v1 = true;
                                                                                                                                                                                                                                                                                                                                                                        } else {
                                                                                                                                                                                                                                                                                                                                                                            v1 = false;
                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                        if (v1) break block134;
                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                    if (c != 65533) break block135;
                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                return -1;
                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                            charCount += c < 65536 ? 1 : 2;
                                                                                                                                                                                                                                                                                                                                                            ++index$iv;
                                                                                                                                                                                                                                                                                                                                                            while (index$iv < endIndex$iv && $this$processUtf8CodePoints$iv[index$iv] >= 0) {
                                                                                                                                                                                                                                                                                                                                                                block138: {
                                                                                                                                                                                                                                                                                                                                                                    block137: {
                                                                                                                                                                                                                                                                                                                                                                        block136: {
                                                                                                                                                                                                                                                                                                                                                                            c = $this$processUtf8CodePoints$iv[index$iv++];
                                                                                                                                                                                                                                                                                                                                                                            $i$a$-processUtf8CodePoints--ByteString$codePointIndexToCharIndex$1 = false;
                                                                                                                                                                                                                                                                                                                                                                            $i$f$isIsoControl = j;
                                                                                                                                                                                                                                                                                                                                                                            j = $i$f$isIsoControl + 1;
                                                                                                                                                                                                                                                                                                                                                                            if ($i$f$isIsoControl == codePointCount) {
                                                                                                                                                                                                                                                                                                                                                                                return charCount;
                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                            if (c == 10 || c == 13) break block136;
                                                                                                                                                                                                                                                                                                                                                                            $i$f$isIsoControl = 0;
                                                                                                                                                                                                                                                                                                                                                                            if (0 <= c ? c < 32 : false) ** GOTO lbl-1000
                                                                                                                                                                                                                                                                                                                                                                            v2 = 127 <= c ? c < 160 : false;
                                                                                                                                                                                                                                                                                                                                                                            if (v2) lbl-1000:
                                                                                                                                                                                                                                                                                                                                                                            // 2 sources

                                                                                                                                                                                                                                                                                                                                                                            {
                                                                                                                                                                                                                                                                                                                                                                                v3 = true;
                                                                                                                                                                                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                                                                                                                                                                                v3 = false;
                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                            if (v3) break block137;
                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                        if (c != 65533) break block138;
                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                    return -1;
                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                charCount += c < 65536 ? 1 : 2;
                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                            v4 = Unit.INSTANCE;
                                                                                                                                                                                                                                                                                                                                                            continue;
                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                        var13_13 = b0$iv;
                                                                                                                                                                                                                                                                                                                                                        other$iv$iv = 5;
                                                                                                                                                                                                                                                                                                                                                        $i$f$shr = 0;
                                                                                                                                                                                                                                                                                                                                                        if ($this$shr$iv$iv >> other$iv$iv != -2) break block139;
                                                                                                                                                                                                                                                                                                                                                        $this$process2Utf8Bytes$iv$iv = $this$processUtf8CodePoints$iv;
                                                                                                                                                                                                                                                                                                                                                        $i$f$process2Utf8Bytes = false;
                                                                                                                                                                                                                                                                                                                                                        if (endIndex$iv > index$iv + true) break block140;
                                                                                                                                                                                                                                                                                                                                                        $i$f$shr = 65533;
                                                                                                                                                                                                                                                                                                                                                        var16_19 = index$iv;
                                                                                                                                                                                                                                                                                                                                                        $i$a$-process2Utf8Bytes-Utf8$processUtf8CodePoints$1$iv = false;
                                                                                                                                                                                                                                                                                                                                                        c = it$iv;
                                                                                                                                                                                                                                                                                                                                                        $i$a$-processUtf8CodePoints--ByteString$codePointIndexToCharIndex$1 = false;
                                                                                                                                                                                                                                                                                                                                                        $i$f$isIsoControl = j;
                                                                                                                                                                                                                                                                                                                                                        j = $i$f$isIsoControl + 1;
                                                                                                                                                                                                                                                                                                                                                        if ($i$f$isIsoControl == codePointCount) {
                                                                                                                                                                                                                                                                                                                                                            return charCount;
                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                        if (c == 10 || c == 13) break block141;
                                                                                                                                                                                                                                                                                                                                                        $i$f$isIsoControl = 0;
                                                                                                                                                                                                                                                                                                                                                        if (0 <= c ? c < 32 : false) ** GOTO lbl-1000
                                                                                                                                                                                                                                                                                                                                                        v5 = 127 <= c ? c < 160 : false;
                                                                                                                                                                                                                                                                                                                                                        if (v5) lbl-1000:
                                                                                                                                                                                                                                                                                                                                                        // 2 sources

                                                                                                                                                                                                                                                                                                                                                        {
                                                                                                                                                                                                                                                                                                                                                            v6 = true;
                                                                                                                                                                                                                                                                                                                                                        } else {
                                                                                                                                                                                                                                                                                                                                                            v6 = false;
                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                        if (v6) break block142;
                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                    if (c != 65533) break block143;
                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                return -1;
                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                            charCount += c < 65536 ? 1 : 2;
                                                                                                                                                                                                                                                                                                                                            var18_21 = Unit.INSTANCE;
                                                                                                                                                                                                                                                                                                                                            v7 = var16_19;
                                                                                                                                                                                                                                                                                                                                            v8 = 1;
                                                                                                                                                                                                                                                                                                                                            break block144;
                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                        b0$iv$iv = $this$process2Utf8Bytes$iv$iv[index$iv];
                                                                                                                                                                                                                                                                                                                                        b1$iv$iv = $this$process2Utf8Bytes$iv$iv[index$iv + true];
                                                                                                                                                                                                                                                                                                                                        $i$f$isUtf8Continuation = false;
                                                                                                                                                                                                                                                                                                                                        var22_25 = b1$iv$iv;
                                                                                                                                                                                                                                                                                                                                        other$iv$iv$iv$iv = 192;
                                                                                                                                                                                                                                                                                                                                        $i$f$and = false;
                                                                                                                                                                                                                                                                                                                                        if (($this$and$iv$iv$iv$iv & other$iv$iv$iv$iv) == 128) break block145;
                                                                                                                                                                                                                                                                                                                                        it$iv = 65533;
                                                                                                                                                                                                                                                                                                                                        $i$a$-process2Utf8Bytes-Utf8$processUtf8CodePoints$1$iv = false;
                                                                                                                                                                                                                                                                                                                                        c = it$iv;
                                                                                                                                                                                                                                                                                                                                        $i$a$-processUtf8CodePoints--ByteString$codePointIndexToCharIndex$1 = false;
                                                                                                                                                                                                                                                                                                                                        $i$f$isIsoControl = j;
                                                                                                                                                                                                                                                                                                                                        j = $i$f$isIsoControl + 1;
                                                                                                                                                                                                                                                                                                                                        if ($i$f$isIsoControl == codePointCount) {
                                                                                                                                                                                                                                                                                                                                            return charCount;
                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                        if (c == 10 || c == 13) break block146;
                                                                                                                                                                                                                                                                                                                                        $i$f$isIsoControl = 0;
                                                                                                                                                                                                                                                                                                                                        if (0 <= c ? c < 32 : false) ** GOTO lbl-1000
                                                                                                                                                                                                                                                                                                                                        v9 = 127 <= c ? c < 160 : false;
                                                                                                                                                                                                                                                                                                                                        if (v9) lbl-1000:
                                                                                                                                                                                                                                                                                                                                        // 2 sources

                                                                                                                                                                                                                                                                                                                                        {
                                                                                                                                                                                                                                                                                                                                            v10 = true;
                                                                                                                                                                                                                                                                                                                                        } else {
                                                                                                                                                                                                                                                                                                                                            v10 = false;
                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                        if (v10) break block147;
                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                    if (c != 65533) break block148;
                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                return -1;
                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                            charCount += c < 65536 ? 1 : 2;
                                                                                                                                                                                                                                                                                                                            var18_21 = Unit.INSTANCE;
                                                                                                                                                                                                                                                                                                                            v7 = var16_19;
                                                                                                                                                                                                                                                                                                                            v8 = 1;
                                                                                                                                                                                                                                                                                                                            break block144;
                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                        codePoint$iv$iv = 3968 ^ b1$iv$iv ^ b0$iv$iv << 6;
                                                                                                                                                                                                                                                                                                                        if (codePoint$iv$iv >= 128) break block149;
                                                                                                                                                                                                                                                                                                                        it$iv = 65533;
                                                                                                                                                                                                                                                                                                                        $i$a$-process2Utf8Bytes-Utf8$processUtf8CodePoints$1$iv = false;
                                                                                                                                                                                                                                                                                                                        c = it$iv;
                                                                                                                                                                                                                                                                                                                        $i$a$-processUtf8CodePoints--ByteString$codePointIndexToCharIndex$1 = false;
                                                                                                                                                                                                                                                                                                                        $i$f$isIsoControl = j;
                                                                                                                                                                                                                                                                                                                        j = $i$f$isIsoControl + 1;
                                                                                                                                                                                                                                                                                                                        if ($i$f$isIsoControl == codePointCount) {
                                                                                                                                                                                                                                                                                                                            return charCount;
                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                        if (c == 10 || c == 13) break block150;
                                                                                                                                                                                                                                                                                                                        $i$f$isIsoControl = 0;
                                                                                                                                                                                                                                                                                                                        if (0 <= c ? c < 32 : false) ** GOTO lbl-1000
                                                                                                                                                                                                                                                                                                                        v11 = 127 <= c ? c < 160 : false;
                                                                                                                                                                                                                                                                                                                        if (v11) lbl-1000:
                                                                                                                                                                                                                                                                                                                        // 2 sources

                                                                                                                                                                                                                                                                                                                        {
                                                                                                                                                                                                                                                                                                                            v12 = true;
                                                                                                                                                                                                                                                                                                                        } else {
                                                                                                                                                                                                                                                                                                                            v12 = false;
                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                        if (v12) break block151;
                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                    if (c != 65533) break block152;
                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                return -1;
                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                            charCount += c < 65536 ? 1 : 2;
                                                                                                                                                                                                                                                                                                            var18_21 = Unit.INSTANCE;
                                                                                                                                                                                                                                                                                                            v7 = var16_19;
                                                                                                                                                                                                                                                                                                            break block153;
                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                        it$iv = codePoint$iv$iv;
                                                                                                                                                                                                                                                                                                        $i$a$-process2Utf8Bytes-Utf8$processUtf8CodePoints$1$iv = false;
                                                                                                                                                                                                                                                                                                        c = it$iv;
                                                                                                                                                                                                                                                                                                        $i$a$-processUtf8CodePoints--ByteString$codePointIndexToCharIndex$1 = false;
                                                                                                                                                                                                                                                                                                        $i$f$isIsoControl = j;
                                                                                                                                                                                                                                                                                                        j = $i$f$isIsoControl + 1;
                                                                                                                                                                                                                                                                                                        if ($i$f$isIsoControl == codePointCount) {
                                                                                                                                                                                                                                                                                                            return charCount;
                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                        if (c == 10 || c == 13) break block154;
                                                                                                                                                                                                                                                                                                        $i$f$isIsoControl = 0;
                                                                                                                                                                                                                                                                                                        if (0 <= c ? c < 32 : false) ** GOTO lbl-1000
                                                                                                                                                                                                                                                                                                        v13 = 127 <= c ? c < 160 : false;
                                                                                                                                                                                                                                                                                                        if (v13) lbl-1000:
                                                                                                                                                                                                                                                                                                        // 2 sources

                                                                                                                                                                                                                                                                                                        {
                                                                                                                                                                                                                                                                                                            v14 = true;
                                                                                                                                                                                                                                                                                                        } else {
                                                                                                                                                                                                                                                                                                            v14 = false;
                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                        if (v14) break block155;
                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                    if (c != 65533) break block156;
                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                return -1;
                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                            charCount += c < 65536 ? 1 : 2;
                                                                                                                                                                                                                                                                                            var18_21 = Unit.INSTANCE;
                                                                                                                                                                                                                                                                                            v7 = var16_19;
                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                        v8 = 2;
                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                    index$iv = v7 + v8;
                                                                                                                                                                                                                                                                                    v4 = Unit.INSTANCE;
                                                                                                                                                                                                                                                                                    continue;
                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                $this$process2Utf8Bytes$iv$iv = b0$iv;
                                                                                                                                                                                                                                                                                other$iv$iv = 4;
                                                                                                                                                                                                                                                                                $i$f$shr = 0;
                                                                                                                                                                                                                                                                                if ($this$shr$iv$iv >> other$iv$iv != -2) break block157;
                                                                                                                                                                                                                                                                                $this$process3Utf8Bytes$iv$iv = $this$processUtf8CodePoints$iv;
                                                                                                                                                                                                                                                                                $i$f$process3Utf8Bytes = false;
                                                                                                                                                                                                                                                                                if (endIndex$iv > index$iv + 2) break block158;
                                                                                                                                                                                                                                                                                $i$f$shr = 65533;
                                                                                                                                                                                                                                                                                var16_19 = index$iv;
                                                                                                                                                                                                                                                                                $i$a$-process3Utf8Bytes-Utf8$processUtf8CodePoints$2$iv = false;
                                                                                                                                                                                                                                                                                c = it$iv;
                                                                                                                                                                                                                                                                                $i$a$-processUtf8CodePoints--ByteString$codePointIndexToCharIndex$1 = false;
                                                                                                                                                                                                                                                                                $i$f$isIsoControl = j;
                                                                                                                                                                                                                                                                                j = $i$f$isIsoControl + 1;
                                                                                                                                                                                                                                                                                if ($i$f$isIsoControl == codePointCount) {
                                                                                                                                                                                                                                                                                    return charCount;
                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                if (c == 10 || c == 13) break block159;
                                                                                                                                                                                                                                                                                $i$f$isIsoControl = 0;
                                                                                                                                                                                                                                                                                if (0 <= c ? c < 32 : false) ** GOTO lbl-1000
                                                                                                                                                                                                                                                                                v15 = 127 <= c ? c < 160 : false;
                                                                                                                                                                                                                                                                                if (v15) lbl-1000:
                                                                                                                                                                                                                                                                                // 2 sources

                                                                                                                                                                                                                                                                                {
                                                                                                                                                                                                                                                                                    v16 = true;
                                                                                                                                                                                                                                                                                } else {
                                                                                                                                                                                                                                                                                    v16 = false;
                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                if (v16) break block160;
                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                            if (c != 65533) break block161;
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                        return -1;
                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                    charCount += c < 65536 ? 1 : 2;
                                                                                                                                                                                                                                                                    var18_21 = Unit.INSTANCE;
                                                                                                                                                                                                                                                                    v17 = var16_19;
                                                                                                                                                                                                                                                                    if (endIndex$iv <= index$iv + true) ** GOTO lbl-1000
                                                                                                                                                                                                                                                                    byte$iv$iv$iv = $this$process3Utf8Bytes$iv$iv[index$iv + true];
                                                                                                                                                                                                                                                                    $i$f$isUtf8Continuation = false;
                                                                                                                                                                                                                                                                    codePoint$iv$iv = byte$iv$iv$iv;
                                                                                                                                                                                                                                                                    other$iv$iv$iv$iv = 192;
                                                                                                                                                                                                                                                                    $i$f$and = false;
                                                                                                                                                                                                                                                                    if (!(($this$and$iv$iv$iv$iv & other$iv$iv$iv$iv) == 128)) lbl-1000:
                                                                                                                                                                                                                                                                    // 2 sources

                                                                                                                                                                                                                                                                    {
                                                                                                                                                                                                                                                                        v18 = 1;
                                                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                                                        v18 = 2;
                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                    break block162;
                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                b0$iv$iv = $this$process3Utf8Bytes$iv$iv[index$iv];
                                                                                                                                                                                                                                                                b1$iv$iv = $this$process3Utf8Bytes$iv$iv[index$iv + true];
                                                                                                                                                                                                                                                                $i$f$isUtf8Continuation = false;
                                                                                                                                                                                                                                                                other$iv$iv$iv$iv = b1$iv$iv;
                                                                                                                                                                                                                                                                other$iv$iv$iv$iv = 192;
                                                                                                                                                                                                                                                                $i$f$and = false;
                                                                                                                                                                                                                                                                if (($this$and$iv$iv$iv$iv & other$iv$iv$iv$iv) == 128) break block163;
                                                                                                                                                                                                                                                                it$iv = 65533;
                                                                                                                                                                                                                                                                $i$a$-process3Utf8Bytes-Utf8$processUtf8CodePoints$2$iv = false;
                                                                                                                                                                                                                                                                c = it$iv;
                                                                                                                                                                                                                                                                $i$a$-processUtf8CodePoints--ByteString$codePointIndexToCharIndex$1 = false;
                                                                                                                                                                                                                                                                $i$f$isIsoControl = j;
                                                                                                                                                                                                                                                                j = $i$f$isIsoControl + 1;
                                                                                                                                                                                                                                                                if ($i$f$isIsoControl == codePointCount) {
                                                                                                                                                                                                                                                                    return charCount;
                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                if (c == 10 || c == 13) break block164;
                                                                                                                                                                                                                                                                $i$f$isIsoControl = 0;
                                                                                                                                                                                                                                                                if (0 <= c ? c < 32 : false) ** GOTO lbl-1000
                                                                                                                                                                                                                                                                v19 = 127 <= c ? c < 160 : false;
                                                                                                                                                                                                                                                                if (v19) lbl-1000:
                                                                                                                                                                                                                                                                // 2 sources

                                                                                                                                                                                                                                                                {
                                                                                                                                                                                                                                                                    v20 = true;
                                                                                                                                                                                                                                                                } else {
                                                                                                                                                                                                                                                                    v20 = false;
                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                if (v20) break block165;
                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                            if (c != 65533) break block166;
                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                        return -1;
                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                    charCount += c < 65536 ? 1 : 2;
                                                                                                                                                                                                                                                    var18_21 = Unit.INSTANCE;
                                                                                                                                                                                                                                                    v17 = var16_19;
                                                                                                                                                                                                                                                    v18 = 1;
                                                                                                                                                                                                                                                    break block162;
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                b2$iv$iv = $this$process3Utf8Bytes$iv$iv[index$iv + 2];
                                                                                                                                                                                                                                                $i$f$isUtf8Continuation = false;
                                                                                                                                                                                                                                                other$iv$iv$iv$iv = b2$iv$iv;
                                                                                                                                                                                                                                                other$iv$iv$iv$iv = 192;
                                                                                                                                                                                                                                                $i$f$and = false;
                                                                                                                                                                                                                                                if (($this$and$iv$iv$iv$iv & other$iv$iv$iv$iv) == 128) break block167;
                                                                                                                                                                                                                                                it$iv = 65533;
                                                                                                                                                                                                                                                $i$a$-process3Utf8Bytes-Utf8$processUtf8CodePoints$2$iv = false;
                                                                                                                                                                                                                                                c = it$iv;
                                                                                                                                                                                                                                                $i$a$-processUtf8CodePoints--ByteString$codePointIndexToCharIndex$1 = false;
                                                                                                                                                                                                                                                $i$f$isIsoControl = j;
                                                                                                                                                                                                                                                j = $i$f$isIsoControl + 1;
                                                                                                                                                                                                                                                if ($i$f$isIsoControl == codePointCount) {
                                                                                                                                                                                                                                                    return charCount;
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                if (c == 10 || c == 13) break block168;
                                                                                                                                                                                                                                                $i$f$isIsoControl = 0;
                                                                                                                                                                                                                                                if (0 <= c ? c < 32 : false) ** GOTO lbl-1000
                                                                                                                                                                                                                                                v21 = 127 <= c ? c < 160 : false;
                                                                                                                                                                                                                                                if (v21) lbl-1000:
                                                                                                                                                                                                                                                // 2 sources

                                                                                                                                                                                                                                                {
                                                                                                                                                                                                                                                    v22 = true;
                                                                                                                                                                                                                                                } else {
                                                                                                                                                                                                                                                    v22 = false;
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                if (v22) break block169;
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            if (c != 65533) break block170;
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        return -1;
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    charCount += c < 65536 ? 1 : 2;
                                                                                                                                                                                                                                    var18_21 = Unit.INSTANCE;
                                                                                                                                                                                                                                    v17 = var16_19;
                                                                                                                                                                                                                                    v18 = 2;
                                                                                                                                                                                                                                    break block162;
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                codePoint$iv$iv = -123008 ^ b2$iv$iv ^ b1$iv$iv << 6 ^ b0$iv$iv << 12;
                                                                                                                                                                                                                                if (codePoint$iv$iv >= 2048) break block171;
                                                                                                                                                                                                                                it$iv = 65533;
                                                                                                                                                                                                                                $i$a$-process3Utf8Bytes-Utf8$processUtf8CodePoints$2$iv = false;
                                                                                                                                                                                                                                c = it$iv;
                                                                                                                                                                                                                                $i$a$-processUtf8CodePoints--ByteString$codePointIndexToCharIndex$1 = false;
                                                                                                                                                                                                                                $i$f$isIsoControl = j;
                                                                                                                                                                                                                                j = $i$f$isIsoControl + 1;
                                                                                                                                                                                                                                if ($i$f$isIsoControl == codePointCount) {
                                                                                                                                                                                                                                    return charCount;
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                if (c == 10 || c == 13) break block172;
                                                                                                                                                                                                                                $i$f$isIsoControl = 0;
                                                                                                                                                                                                                                if (0 <= c ? c < 32 : false) ** GOTO lbl-1000
                                                                                                                                                                                                                                v23 = 127 <= c ? c < 160 : false;
                                                                                                                                                                                                                                if (v23) lbl-1000:
                                                                                                                                                                                                                                // 2 sources

                                                                                                                                                                                                                                {
                                                                                                                                                                                                                                    v24 = true;
                                                                                                                                                                                                                                } else {
                                                                                                                                                                                                                                    v24 = false;
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                if (v24) break block173;
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            if (c != 65533) break block174;
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        return -1;
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    charCount += c < 65536 ? 1 : 2;
                                                                                                                                                                                                                    var18_21 = Unit.INSTANCE;
                                                                                                                                                                                                                    v17 = var16_19;
                                                                                                                                                                                                                    break block175;
                                                                                                                                                                                                                }
                                                                                                                                                                                                                v25 = 55296 <= codePoint$iv$iv ? codePoint$iv$iv < 57344 : false;
                                                                                                                                                                                                                if (!v25) break block176;
                                                                                                                                                                                                                it$iv = 65533;
                                                                                                                                                                                                                $i$a$-process3Utf8Bytes-Utf8$processUtf8CodePoints$2$iv = false;
                                                                                                                                                                                                                c = it$iv;
                                                                                                                                                                                                                $i$a$-processUtf8CodePoints--ByteString$codePointIndexToCharIndex$1 = false;
                                                                                                                                                                                                                $i$f$isIsoControl = j;
                                                                                                                                                                                                                j = $i$f$isIsoControl + 1;
                                                                                                                                                                                                                if ($i$f$isIsoControl == codePointCount) {
                                                                                                                                                                                                                    return charCount;
                                                                                                                                                                                                                }
                                                                                                                                                                                                                if (c == 10 || c == 13) break block177;
                                                                                                                                                                                                                $i$f$isIsoControl = 0;
                                                                                                                                                                                                                if (0 <= c ? c < 32 : false) ** GOTO lbl-1000
                                                                                                                                                                                                                v26 = 127 <= c ? c < 160 : false;
                                                                                                                                                                                                                if (v26) lbl-1000:
                                                                                                                                                                                                                // 2 sources

                                                                                                                                                                                                                {
                                                                                                                                                                                                                    v27 = true;
                                                                                                                                                                                                                } else {
                                                                                                                                                                                                                    v27 = false;
                                                                                                                                                                                                                }
                                                                                                                                                                                                                if (v27) break block178;
                                                                                                                                                                                                            }
                                                                                                                                                                                                            if (c != 65533) break block179;
                                                                                                                                                                                                        }
                                                                                                                                                                                                        return -1;
                                                                                                                                                                                                    }
                                                                                                                                                                                                    charCount += c < 65536 ? 1 : 2;
                                                                                                                                                                                                    var18_21 = Unit.INSTANCE;
                                                                                                                                                                                                    v17 = var16_19;
                                                                                                                                                                                                    break block175;
                                                                                                                                                                                                }
                                                                                                                                                                                                it$iv = codePoint$iv$iv;
                                                                                                                                                                                                $i$a$-process3Utf8Bytes-Utf8$processUtf8CodePoints$2$iv = false;
                                                                                                                                                                                                c = it$iv;
                                                                                                                                                                                                $i$a$-processUtf8CodePoints--ByteString$codePointIndexToCharIndex$1 = false;
                                                                                                                                                                                                $i$f$isIsoControl = j;
                                                                                                                                                                                                j = $i$f$isIsoControl + 1;
                                                                                                                                                                                                if ($i$f$isIsoControl == codePointCount) {
                                                                                                                                                                                                    return charCount;
                                                                                                                                                                                                }
                                                                                                                                                                                                if (c == 10 || c == 13) break block180;
                                                                                                                                                                                                $i$f$isIsoControl = 0;
                                                                                                                                                                                                if (0 <= c ? c < 32 : false) ** GOTO lbl-1000
                                                                                                                                                                                                v28 = 127 <= c ? c < 160 : false;
                                                                                                                                                                                                if (v28) lbl-1000:
                                                                                                                                                                                                // 2 sources

                                                                                                                                                                                                {
                                                                                                                                                                                                    v29 = true;
                                                                                                                                                                                                } else {
                                                                                                                                                                                                    v29 = false;
                                                                                                                                                                                                }
                                                                                                                                                                                                if (v29) break block181;
                                                                                                                                                                                            }
                                                                                                                                                                                            if (c != 65533) break block182;
                                                                                                                                                                                        }
                                                                                                                                                                                        return -1;
                                                                                                                                                                                    }
                                                                                                                                                                                    charCount += c < 65536 ? 1 : 2;
                                                                                                                                                                                    var18_21 = Unit.INSTANCE;
                                                                                                                                                                                    v17 = var16_19;
                                                                                                                                                                                }
                                                                                                                                                                                v18 = 3;
                                                                                                                                                                            }
                                                                                                                                                                            index$iv = v17 + v18;
                                                                                                                                                                            v4 = Unit.INSTANCE;
                                                                                                                                                                            continue;
                                                                                                                                                                        }
                                                                                                                                                                        $this$process3Utf8Bytes$iv$iv = b0$iv;
                                                                                                                                                                        other$iv$iv = 3;
                                                                                                                                                                        $i$f$shr = 0;
                                                                                                                                                                        if ($this$shr$iv$iv >> other$iv$iv != -2) break block183;
                                                                                                                                                                        $this$process4Utf8Bytes$iv$iv = $this$processUtf8CodePoints$iv;
                                                                                                                                                                        $i$f$process4Utf8Bytes = false;
                                                                                                                                                                        if (endIndex$iv > index$iv + 3) break block184;
                                                                                                                                                                        $i$f$shr = 65533;
                                                                                                                                                                        var16_19 = index$iv;
                                                                                                                                                                        $i$a$-process4Utf8Bytes-Utf8$processUtf8CodePoints$3$iv = false;
                                                                                                                                                                        c = it$iv;
                                                                                                                                                                        $i$a$-processUtf8CodePoints--ByteString$codePointIndexToCharIndex$1 = false;
                                                                                                                                                                        $i$f$isIsoControl = j;
                                                                                                                                                                        j = $i$f$isIsoControl + 1;
                                                                                                                                                                        if ($i$f$isIsoControl == codePointCount) {
                                                                                                                                                                            return charCount;
                                                                                                                                                                        }
                                                                                                                                                                        if (c == 10 || c == 13) break block185;
                                                                                                                                                                        $i$f$isIsoControl = 0;
                                                                                                                                                                        if (0 <= c ? c < 32 : false) ** GOTO lbl-1000
                                                                                                                                                                        v30 = 127 <= c ? c < 160 : false;
                                                                                                                                                                        if (v30) lbl-1000:
                                                                                                                                                                        // 2 sources

                                                                                                                                                                        {
                                                                                                                                                                            v31 = true;
                                                                                                                                                                        } else {
                                                                                                                                                                            v31 = false;
                                                                                                                                                                        }
                                                                                                                                                                        if (v31) break block186;
                                                                                                                                                                    }
                                                                                                                                                                    if (c != 65533) break block187;
                                                                                                                                                                }
                                                                                                                                                                return -1;
                                                                                                                                                            }
                                                                                                                                                            charCount += c < 65536 ? 1 : 2;
                                                                                                                                                            var18_21 = Unit.INSTANCE;
                                                                                                                                                            v32 = var16_19;
                                                                                                                                                            if (endIndex$iv <= index$iv + true) break block188;
                                                                                                                                                            byte$iv$iv$iv = $this$process4Utf8Bytes$iv$iv[index$iv + true];
                                                                                                                                                            $i$f$isUtf8Continuation = false;
                                                                                                                                                            b2$iv$iv = byte$iv$iv$iv;
                                                                                                                                                            other$iv$iv$iv$iv = 192;
                                                                                                                                                            $i$f$and = false;
                                                                                                                                                            if (($this$and$iv$iv$iv$iv & other$iv$iv$iv$iv) == 128) break block189;
                                                                                                                                                        }
                                                                                                                                                        v33 = 1;
                                                                                                                                                        break block190;
                                                                                                                                                    }
                                                                                                                                                    if (endIndex$iv <= index$iv + 2) ** GOTO lbl-1000
                                                                                                                                                    byte$iv$iv$iv = $this$process4Utf8Bytes$iv$iv[index$iv + 2];
                                                                                                                                                    $i$f$isUtf8Continuation = false;
                                                                                                                                                    $this$and$iv$iv$iv$iv = byte$iv$iv$iv;
                                                                                                                                                    other$iv$iv$iv$iv = 192;
                                                                                                                                                    $i$f$and = false;
                                                                                                                                                    if (!(($this$and$iv$iv$iv$iv & other$iv$iv$iv$iv) == 128)) lbl-1000:
                                                                                                                                                    // 2 sources

                                                                                                                                                    {
                                                                                                                                                        v33 = 2;
                                                                                                                                                    } else {
                                                                                                                                                        v33 = 3;
                                                                                                                                                    }
                                                                                                                                                    break block190;
                                                                                                                                                }
                                                                                                                                                b0$iv$iv = $this$process4Utf8Bytes$iv$iv[index$iv];
                                                                                                                                                b1$iv$iv = $this$process4Utf8Bytes$iv$iv[index$iv + true];
                                                                                                                                                $i$f$isUtf8Continuation = false;
                                                                                                                                                other$iv$iv$iv$iv = b1$iv$iv;
                                                                                                                                                other$iv$iv$iv$iv = 192;
                                                                                                                                                $i$f$and = false;
                                                                                                                                                if (($this$and$iv$iv$iv$iv & other$iv$iv$iv$iv) == 128) break block191;
                                                                                                                                                it$iv = 65533;
                                                                                                                                                $i$a$-process4Utf8Bytes-Utf8$processUtf8CodePoints$3$iv = false;
                                                                                                                                                c = it$iv;
                                                                                                                                                $i$a$-processUtf8CodePoints--ByteString$codePointIndexToCharIndex$1 = false;
                                                                                                                                                $i$f$isIsoControl = j;
                                                                                                                                                j = $i$f$isIsoControl + 1;
                                                                                                                                                if ($i$f$isIsoControl == codePointCount) {
                                                                                                                                                    return charCount;
                                                                                                                                                }
                                                                                                                                                if (c == 10 || c == 13) break block192;
                                                                                                                                                $i$f$isIsoControl = 0;
                                                                                                                                                if (0 <= c ? c < 32 : false) ** GOTO lbl-1000
                                                                                                                                                v34 = 127 <= c ? c < 160 : false;
                                                                                                                                                if (v34) lbl-1000:
                                                                                                                                                // 2 sources

                                                                                                                                                {
                                                                                                                                                    v35 = true;
                                                                                                                                                } else {
                                                                                                                                                    v35 = false;
                                                                                                                                                }
                                                                                                                                                if (v35) break block193;
                                                                                                                                            }
                                                                                                                                            if (c != 65533) break block194;
                                                                                                                                        }
                                                                                                                                        return -1;
                                                                                                                                    }
                                                                                                                                    charCount += c < 65536 ? 1 : 2;
                                                                                                                                    var18_21 = Unit.INSTANCE;
                                                                                                                                    v32 = var16_19;
                                                                                                                                    v33 = 1;
                                                                                                                                    break block190;
                                                                                                                                }
                                                                                                                                b2$iv$iv = $this$process4Utf8Bytes$iv$iv[index$iv + 2];
                                                                                                                                $i$f$isUtf8Continuation = false;
                                                                                                                                other$iv$iv$iv$iv = b2$iv$iv;
                                                                                                                                other$iv$iv$iv$iv = 192;
                                                                                                                                $i$f$and = false;
                                                                                                                                if (($this$and$iv$iv$iv$iv & other$iv$iv$iv$iv) == 128) break block195;
                                                                                                                                it$iv = 65533;
                                                                                                                                $i$a$-process4Utf8Bytes-Utf8$processUtf8CodePoints$3$iv = false;
                                                                                                                                c = it$iv;
                                                                                                                                $i$a$-processUtf8CodePoints--ByteString$codePointIndexToCharIndex$1 = false;
                                                                                                                                $i$f$isIsoControl = j;
                                                                                                                                j = $i$f$isIsoControl + 1;
                                                                                                                                if ($i$f$isIsoControl == codePointCount) {
                                                                                                                                    return charCount;
                                                                                                                                }
                                                                                                                                if (c == 10 || c == 13) break block196;
                                                                                                                                $i$f$isIsoControl = 0;
                                                                                                                                if (0 <= c ? c < 32 : false) ** GOTO lbl-1000
                                                                                                                                v36 = 127 <= c ? c < 160 : false;
                                                                                                                                if (v36) lbl-1000:
                                                                                                                                // 2 sources

                                                                                                                                {
                                                                                                                                    v37 = true;
                                                                                                                                } else {
                                                                                                                                    v37 = false;
                                                                                                                                }
                                                                                                                                if (v37) break block197;
                                                                                                                            }
                                                                                                                            if (c != 65533) break block198;
                                                                                                                        }
                                                                                                                        return -1;
                                                                                                                    }
                                                                                                                    charCount += c < 65536 ? 1 : 2;
                                                                                                                    var18_21 = Unit.INSTANCE;
                                                                                                                    v32 = var16_19;
                                                                                                                    v33 = 2;
                                                                                                                    break block190;
                                                                                                                }
                                                                                                                b3$iv$iv = $this$process4Utf8Bytes$iv$iv[index$iv + 3];
                                                                                                                $i$f$isUtf8Continuation = false;
                                                                                                                other$iv$iv$iv$iv = b3$iv$iv;
                                                                                                                other$iv$iv$iv$iv = 192;
                                                                                                                $i$f$and = false;
                                                                                                                if (($this$and$iv$iv$iv$iv & other$iv$iv$iv$iv) == 128) break block199;
                                                                                                                it$iv = 65533;
                                                                                                                $i$a$-process4Utf8Bytes-Utf8$processUtf8CodePoints$3$iv = false;
                                                                                                                c = it$iv;
                                                                                                                $i$a$-processUtf8CodePoints--ByteString$codePointIndexToCharIndex$1 = false;
                                                                                                                $i$f$isIsoControl = j;
                                                                                                                j = $i$f$isIsoControl + 1;
                                                                                                                if ($i$f$isIsoControl == codePointCount) {
                                                                                                                    return charCount;
                                                                                                                }
                                                                                                                if (c == 10 || c == 13) break block200;
                                                                                                                $i$f$isIsoControl = 0;
                                                                                                                if (0 <= c ? c < 32 : false) ** GOTO lbl-1000
                                                                                                                v38 = 127 <= c ? c < 160 : false;
                                                                                                                if (v38) lbl-1000:
                                                                                                                // 2 sources

                                                                                                                {
                                                                                                                    v39 = true;
                                                                                                                } else {
                                                                                                                    v39 = false;
                                                                                                                }
                                                                                                                if (v39) break block201;
                                                                                                            }
                                                                                                            if (c != 65533) break block202;
                                                                                                        }
                                                                                                        return -1;
                                                                                                    }
                                                                                                    charCount += c < 65536 ? 1 : 2;
                                                                                                    var18_21 = Unit.INSTANCE;
                                                                                                    v32 = var16_19;
                                                                                                    v33 = 3;
                                                                                                    break block190;
                                                                                                }
                                                                                                codePoint$iv$iv = 3678080 ^ b3$iv$iv ^ b2$iv$iv << 6 ^ b1$iv$iv << 12 ^ b0$iv$iv << 18;
                                                                                                if (codePoint$iv$iv <= 0x10FFFF) break block203;
                                                                                                it$iv = 65533;
                                                                                                $i$a$-process4Utf8Bytes-Utf8$processUtf8CodePoints$3$iv = false;
                                                                                                c = it$iv;
                                                                                                $i$a$-processUtf8CodePoints--ByteString$codePointIndexToCharIndex$1 = false;
                                                                                                $i$f$isIsoControl = j;
                                                                                                j = $i$f$isIsoControl + 1;
                                                                                                if ($i$f$isIsoControl == codePointCount) {
                                                                                                    return charCount;
                                                                                                }
                                                                                                if (c == 10 || c == 13) break block204;
                                                                                                $i$f$isIsoControl = 0;
                                                                                                if (0 <= c ? c < 32 : false) ** GOTO lbl-1000
                                                                                                v40 = 127 <= c ? c < 160 : false;
                                                                                                if (v40) lbl-1000:
                                                                                                // 2 sources

                                                                                                {
                                                                                                    v41 = true;
                                                                                                } else {
                                                                                                    v41 = false;
                                                                                                }
                                                                                                if (v41) break block205;
                                                                                            }
                                                                                            if (c != 65533) break block206;
                                                                                        }
                                                                                        return -1;
                                                                                    }
                                                                                    charCount += c < 65536 ? 1 : 2;
                                                                                    var18_21 = Unit.INSTANCE;
                                                                                    v32 = var16_19;
                                                                                    break block207;
                                                                                }
                                                                                v42 = 55296 <= codePoint$iv$iv ? codePoint$iv$iv < 57344 : false;
                                                                                if (!v42) break block208;
                                                                                it$iv = 65533;
                                                                                $i$a$-process4Utf8Bytes-Utf8$processUtf8CodePoints$3$iv = false;
                                                                                c = it$iv;
                                                                                $i$a$-processUtf8CodePoints--ByteString$codePointIndexToCharIndex$1 = false;
                                                                                $i$f$isIsoControl = j;
                                                                                j = $i$f$isIsoControl + 1;
                                                                                if ($i$f$isIsoControl == codePointCount) {
                                                                                    return charCount;
                                                                                }
                                                                                if (c == 10 || c == 13) break block209;
                                                                                $i$f$isIsoControl = 0;
                                                                                if (0 <= c ? c < 32 : false) ** GOTO lbl-1000
                                                                                v43 = 127 <= c ? c < 160 : false;
                                                                                if (v43) lbl-1000:
                                                                                // 2 sources

                                                                                {
                                                                                    v44 = true;
                                                                                } else {
                                                                                    v44 = false;
                                                                                }
                                                                                if (v44) break block210;
                                                                            }
                                                                            if (c != 65533) break block211;
                                                                        }
                                                                        return -1;
                                                                    }
                                                                    charCount += c < 65536 ? 1 : 2;
                                                                    var18_21 = Unit.INSTANCE;
                                                                    v32 = var16_19;
                                                                    break block207;
                                                                }
                                                                if (codePoint$iv$iv >= 65536) break block212;
                                                                it$iv = 65533;
                                                                $i$a$-process4Utf8Bytes-Utf8$processUtf8CodePoints$3$iv = false;
                                                                c = it$iv;
                                                                $i$a$-processUtf8CodePoints--ByteString$codePointIndexToCharIndex$1 = false;
                                                                $i$f$isIsoControl = j;
                                                                j = $i$f$isIsoControl + 1;
                                                                if ($i$f$isIsoControl == codePointCount) {
                                                                    return charCount;
                                                                }
                                                                if (c == 10 || c == 13) break block213;
                                                                $i$f$isIsoControl = 0;
                                                                if (0 <= c ? c < 32 : false) ** GOTO lbl-1000
                                                                v45 = 127 <= c ? c < 160 : false;
                                                                if (v45) lbl-1000:
                                                                // 2 sources

                                                                {
                                                                    v46 = true;
                                                                } else {
                                                                    v46 = false;
                                                                }
                                                                if (v46) break block214;
                                                            }
                                                            if (c != 65533) break block215;
                                                        }
                                                        return -1;
                                                    }
                                                    charCount += c < 65536 ? 1 : 2;
                                                    var18_21 = Unit.INSTANCE;
                                                    v32 = var16_19;
                                                    break block207;
                                                }
                                                it$iv = codePoint$iv$iv;
                                                $i$a$-process4Utf8Bytes-Utf8$processUtf8CodePoints$3$iv = false;
                                                c = it$iv;
                                                $i$a$-processUtf8CodePoints--ByteString$codePointIndexToCharIndex$1 = false;
                                                $i$f$isIsoControl = j;
                                                j = $i$f$isIsoControl + 1;
                                                if ($i$f$isIsoControl == codePointCount) {
                                                    return charCount;
                                                }
                                                if (c == 10 || c == 13) break block216;
                                                $i$f$isIsoControl = 0;
                                                if (0 <= c ? c < 32 : false) ** GOTO lbl-1000
                                                v47 = 127 <= c ? c < 160 : false;
                                                if (v47) lbl-1000:
                                                // 2 sources

                                                {
                                                    v48 = true;
                                                } else {
                                                    v48 = false;
                                                }
                                                if (v48) break block217;
                                            }
                                            if (c != 65533) break block218;
                                        }
                                        return -1;
                                    }
                                    charCount += c < 65536 ? 1 : 2;
                                    var18_21 = Unit.INSTANCE;
                                    v32 = var16_19;
                                }
                                v33 = 4;
                            }
                            index$iv = v32 + v33;
                            v4 = Unit.INSTANCE;
                            continue;
                        }
                        c = 65533;
                        $i$a$-processUtf8CodePoints--ByteString$codePointIndexToCharIndex$1 = false;
                        $i$f$isIsoControl = j;
                        j = $i$f$isIsoControl + 1;
                        if ($i$f$isIsoControl == codePointCount) {
                            return charCount;
                        }
                        if (c == 10 || c == 13) break block219;
                        $i$f$isIsoControl = 0;
                        if (0 <= c ? c < 32 : false) ** GOTO lbl-1000
                        v49 = 127 <= c ? c < 160 : false;
                        if (v49) lbl-1000:
                        // 2 sources

                        {
                            v50 = true;
                        } else {
                            v50 = false;
                        }
                        if (v50) break block220;
                    }
                    if (c != 65533) break block221;
                }
                return -1;
            }
            charCount += c < 65536 ? 1 : 2;
            v4 = (int)index$iv++;
        }
        return charCount;
    }

    static {
        char[] cArray = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        HEX_DIGIT_CHARS = cArray;
    }
}

