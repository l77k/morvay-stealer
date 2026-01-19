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
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={2, 1, 0}, k=2, xi=48, d1={"\u0000\u0014\n\u0000\n\u0002\u0010\u000e\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u0004\u001a\n\u0010\u0006\u001a\u00020\u0002*\u00020\u0001\u00a8\u0006\u0007"}, d2={"commonToUtf8String", "", "", "beginIndex", "", "endIndex", "commonAsUtf8ToByteArray", "okio"})
@SourceDebugExtension(value={"SMAP\n-Utf8.kt\nKotlin\n*S Kotlin\n*F\n+ 1 -Utf8.kt\nokio/internal/_Utf8Kt\n+ 2 Utf8.kt\nokio/Utf8\n+ 3 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,60:1\n260#2,16:61\n277#2:78\n397#2,9:79\n127#2:88\n406#2,20:90\n279#2,3:110\n440#2,4:113\n127#2:117\n446#2,10:118\n127#2:128\n456#2,5:129\n127#2:134\n461#2,24:135\n283#2,3:159\n500#2,3:162\n286#2,12:165\n503#2:177\n127#2:178\n506#2,2:179\n127#2:181\n510#2,10:182\n127#2:192\n520#2,5:193\n127#2:198\n525#2,5:199\n127#2:204\n530#2,28:205\n302#2,6:233\n138#2,67:239\n67#3:77\n73#3:89\n*S KotlinDebug\n*F\n+ 1 -Utf8.kt\nokio/internal/_Utf8Kt\n*L\n34#1:61,16\n34#1:78\n34#1:79,9\n34#1:88\n34#1:90,20\n34#1:110,3\n34#1:113,4\n34#1:117\n34#1:118,10\n34#1:128\n34#1:129,5\n34#1:134\n34#1:135,24\n34#1:159,3\n34#1:162,3\n34#1:165,12\n34#1:177\n34#1:178\n34#1:179,2\n34#1:181\n34#1:182,10\n34#1:192\n34#1:193,5\n34#1:198\n34#1:199,5\n34#1:204\n34#1:205,28\n34#1:233,6\n50#1:239,67\n34#1:77\n34#1:89\n*E\n"})
public final class _Utf8Kt {
    /*
     * Unable to fully structure code
     */
    @NotNull
    public static final String commonToUtf8String(@NotNull byte[] $this$commonToUtf8String, int beginIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$commonToUtf8String, "<this>");
        if (beginIndex < 0 || endIndex > $this$commonToUtf8String.length || beginIndex > endIndex) {
            throw new ArrayIndexOutOfBoundsException("size=" + $this$commonToUtf8String.length + " beginIndex=" + beginIndex + " endIndex=" + endIndex);
        }
        chars = new char[endIndex - beginIndex];
        length = 0;
        $this$processUtf16Chars$iv = $this$commonToUtf8String;
        $i$f$processUtf16Chars = false;
        index$iv = beginIndex;
        while (index$iv < endIndex) {
            block54: {
                block58: {
                    block55: {
                        block57: {
                            block56: {
                                block51: {
                                    block53: {
                                        block52: {
                                            b0$iv = $this$processUtf16Chars$iv[index$iv];
                                            if (b0$iv >= 0) {
                                                c = b0$iv;
                                                $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                                var11_11 = length;
                                                length = var11_11 + 1;
                                                chars[var11_11] = c;
                                                ++index$iv;
                                                while (index$iv < endIndex && $this$processUtf16Chars$iv[index$iv] >= 0) {
                                                    c = (char)$this$processUtf16Chars$iv[index$iv++];
                                                    $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                                    var11_11 = length;
                                                    length = var11_11 + 1;
                                                    chars[var11_11] = c;
                                                }
                                                v0 = Unit.INSTANCE;
                                                continue;
                                            }
                                            var12_12 = b0$iv;
                                            other$iv$iv = 5;
                                            $i$f$shr = 0;
                                            if ($this$shr$iv$iv >> other$iv$iv == -2) {
                                                $this$process2Utf8Bytes$iv$iv = $this$processUtf16Chars$iv;
                                                $i$f$process2Utf8Bytes = false;
                                                if (endIndex <= index$iv + 1) {
                                                    $i$f$shr = 65533;
                                                    var15_18 = index$iv;
                                                    $i$a$-process2Utf8Bytes-Utf8$processUtf16Chars$1$iv = false;
                                                    c = (char)it$iv;
                                                    $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                                    var11_11 = length;
                                                    length = var11_11 + 1;
                                                    chars[var11_11] = c;
                                                    var17_20 = Unit.INSTANCE;
                                                    v1 = var15_18;
                                                    v2 = 1;
                                                } else {
                                                    b0$iv$iv = $this$process2Utf8Bytes$iv$iv[index$iv];
                                                    b1$iv$iv = $this$process2Utf8Bytes$iv$iv[index$iv + 1];
                                                    $i$f$isUtf8Continuation = false;
                                                    var21_24 = b1$iv$iv;
                                                    other$iv$iv$iv$iv = 192;
                                                    $i$f$and = false;
                                                    if (!(($this$and$iv$iv$iv$iv & other$iv$iv$iv$iv) == 128)) {
                                                        it$iv = 65533;
                                                        $i$a$-process2Utf8Bytes-Utf8$processUtf16Chars$1$iv = false;
                                                        c = (char)it$iv;
                                                        $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                                        var11_11 = length;
                                                        length = var11_11 + 1;
                                                        chars[var11_11] = c;
                                                        var17_20 = Unit.INSTANCE;
                                                        v1 = var15_18;
                                                        v2 = 1;
                                                    } else {
                                                        codePoint$iv$iv = 3968 ^ b1$iv$iv ^ b0$iv$iv << 6;
                                                        if (codePoint$iv$iv < 128) {
                                                            it$iv = 65533;
                                                            $i$a$-process2Utf8Bytes-Utf8$processUtf16Chars$1$iv = false;
                                                            c = (char)it$iv;
                                                            $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                                            var11_11 = length;
                                                            length = var11_11 + 1;
                                                            chars[var11_11] = c;
                                                            var17_20 = Unit.INSTANCE;
                                                            v1 = var15_18;
                                                        } else {
                                                            it$iv = codePoint$iv$iv;
                                                            $i$a$-process2Utf8Bytes-Utf8$processUtf16Chars$1$iv = false;
                                                            c = (char)it$iv;
                                                            $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                                            var11_11 = length;
                                                            length = var11_11 + 1;
                                                            chars[var11_11] = c;
                                                            var17_20 = Unit.INSTANCE;
                                                            v1 = var15_18;
                                                        }
                                                        v2 = 2;
                                                    }
                                                }
                                                index$iv = v1 + v2;
                                                v0 = Unit.INSTANCE;
                                                continue;
                                            }
                                            $this$process2Utf8Bytes$iv$iv = b0$iv;
                                            other$iv$iv = 4;
                                            $i$f$shr = 0;
                                            if ($this$shr$iv$iv >> other$iv$iv != -2) break block51;
                                            $this$process3Utf8Bytes$iv$iv = $this$processUtf16Chars$iv;
                                            $i$f$process3Utf8Bytes = false;
                                            if (endIndex > index$iv + 2) break block52;
                                            $i$f$shr = 65533;
                                            var15_18 = index$iv;
                                            $i$a$-process3Utf8Bytes-Utf8$processUtf16Chars$2$iv = false;
                                            c = (char)it$iv;
                                            $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                            var11_11 = length;
                                            length = var11_11 + 1;
                                            chars[var11_11] = c;
                                            var17_20 = Unit.INSTANCE;
                                            v3 = var15_18;
                                            if (endIndex <= index$iv + 1) ** GOTO lbl-1000
                                            byte$iv$iv$iv = $this$process3Utf8Bytes$iv$iv[index$iv + 1];
                                            $i$f$isUtf8Continuation = false;
                                            codePoint$iv$iv = byte$iv$iv$iv;
                                            other$iv$iv$iv$iv = 192;
                                            $i$f$and = false;
                                            if (!(($this$and$iv$iv$iv$iv & other$iv$iv$iv$iv) == 128)) lbl-1000:
                                            // 2 sources

                                            {
                                                v4 = 1;
                                            } else {
                                                v4 = 2;
                                            }
                                            break block53;
                                        }
                                        b0$iv$iv = $this$process3Utf8Bytes$iv$iv[index$iv];
                                        b1$iv$iv = $this$process3Utf8Bytes$iv$iv[index$iv + 1];
                                        $i$f$isUtf8Continuation = false;
                                        other$iv$iv$iv$iv = b1$iv$iv;
                                        other$iv$iv$iv$iv = 192;
                                        $i$f$and = false;
                                        if (!(($this$and$iv$iv$iv$iv & other$iv$iv$iv$iv) == 128)) {
                                            it$iv = 65533;
                                            $i$a$-process3Utf8Bytes-Utf8$processUtf16Chars$2$iv = false;
                                            c = (char)it$iv;
                                            $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                            var11_11 = length;
                                            length = var11_11 + 1;
                                            chars[var11_11] = c;
                                            var17_20 = Unit.INSTANCE;
                                            v3 = var15_18;
                                            v4 = 1;
                                        } else {
                                            b2$iv$iv = $this$process3Utf8Bytes$iv$iv[index$iv + 2];
                                            $i$f$isUtf8Continuation = false;
                                            other$iv$iv$iv$iv = b2$iv$iv;
                                            other$iv$iv$iv$iv = 192;
                                            $i$f$and = false;
                                            if (!(($this$and$iv$iv$iv$iv & other$iv$iv$iv$iv) == 128)) {
                                                it$iv = 65533;
                                                $i$a$-process3Utf8Bytes-Utf8$processUtf16Chars$2$iv = false;
                                                c = (char)it$iv;
                                                $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                                var11_11 = length;
                                                length = var11_11 + 1;
                                                chars[var11_11] = c;
                                                var17_20 = Unit.INSTANCE;
                                                v3 = var15_18;
                                                v4 = 2;
                                            } else {
                                                codePoint$iv$iv = -123008 ^ b2$iv$iv ^ b1$iv$iv << 6 ^ b0$iv$iv << 12;
                                                if (codePoint$iv$iv < 2048) {
                                                    it$iv = 65533;
                                                    $i$a$-process3Utf8Bytes-Utf8$processUtf16Chars$2$iv = false;
                                                    c = (char)it$iv;
                                                    $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                                    var11_11 = length;
                                                    length = var11_11 + 1;
                                                    chars[var11_11] = c;
                                                    var17_20 = Unit.INSTANCE;
                                                    v3 = var15_18;
                                                } else {
                                                    v5 = 55296 <= codePoint$iv$iv ? codePoint$iv$iv < 57344 : false;
                                                    if (v5) {
                                                        it$iv = 65533;
                                                        $i$a$-process3Utf8Bytes-Utf8$processUtf16Chars$2$iv = false;
                                                        c = (char)it$iv;
                                                        $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                                        var11_11 = length;
                                                        length = var11_11 + 1;
                                                        chars[var11_11] = c;
                                                        var17_20 = Unit.INSTANCE;
                                                        v3 = var15_18;
                                                    } else {
                                                        it$iv = codePoint$iv$iv;
                                                        $i$a$-process3Utf8Bytes-Utf8$processUtf16Chars$2$iv = false;
                                                        c = (char)it$iv;
                                                        $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                                        var11_11 = length;
                                                        length = var11_11 + 1;
                                                        chars[var11_11] = c;
                                                        var17_20 = Unit.INSTANCE;
                                                        v3 = var15_18;
                                                    }
                                                }
                                                v4 = 3;
                                            }
                                        }
                                    }
                                    index$iv = v3 + v4;
                                    v0 = Unit.INSTANCE;
                                    continue;
                                }
                                $this$process3Utf8Bytes$iv$iv = b0$iv;
                                other$iv$iv = 3;
                                $i$f$shr = 0;
                                if ($this$shr$iv$iv >> other$iv$iv != -2) break block54;
                                $this$process4Utf8Bytes$iv$iv = $this$processUtf16Chars$iv;
                                $i$f$process4Utf8Bytes = false;
                                if (endIndex > index$iv + 3) break block55;
                                $i$f$shr = 65533;
                                var15_18 = index$iv;
                                $i$a$-process4Utf8Bytes-Utf8$processUtf16Chars$3$iv = false;
                                if (codePoint$iv != 65533) {
                                    c = (char)((codePoint$iv >>> 10) + 55232);
                                    $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                    var11_11 = length;
                                    length = var11_11 + 1;
                                    chars[var11_11] = c;
                                    c = (char)((codePoint$iv & 1023) + 56320);
                                    $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                    var11_11 = length;
                                    length = var11_11 + 1;
                                    chars[var11_11] = c;
                                } else {
                                    c = 65533;
                                    $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                    var11_11 = length;
                                    length = var11_11 + 1;
                                    chars[var11_11] = c;
                                }
                                var17_20 = Unit.INSTANCE;
                                v6 = var15_18;
                                if (endIndex <= index$iv + 1) break block56;
                                byte$iv$iv$iv = $this$process4Utf8Bytes$iv$iv[index$iv + 1];
                                $i$f$isUtf8Continuation = false;
                                b2$iv$iv = byte$iv$iv$iv;
                                other$iv$iv$iv$iv = 192;
                                $i$f$and = false;
                                if (($this$and$iv$iv$iv$iv & other$iv$iv$iv$iv) == 128) break block57;
                            }
                            v7 = 1;
                            break block58;
                        }
                        if (endIndex <= index$iv + 2) ** GOTO lbl-1000
                        byte$iv$iv$iv = $this$process4Utf8Bytes$iv$iv[index$iv + 2];
                        $i$f$isUtf8Continuation = false;
                        $this$and$iv$iv$iv$iv = byte$iv$iv$iv;
                        other$iv$iv$iv$iv = 192;
                        $i$f$and = false;
                        if (!(($this$and$iv$iv$iv$iv & other$iv$iv$iv$iv) == 128)) lbl-1000:
                        // 2 sources

                        {
                            v7 = 2;
                        } else {
                            v7 = 3;
                        }
                        break block58;
                    }
                    b0$iv$iv = $this$process4Utf8Bytes$iv$iv[index$iv];
                    b1$iv$iv = $this$process4Utf8Bytes$iv$iv[index$iv + 1];
                    $i$f$isUtf8Continuation = false;
                    other$iv$iv$iv$iv = b1$iv$iv;
                    other$iv$iv$iv$iv = 192;
                    $i$f$and = false;
                    if (!(($this$and$iv$iv$iv$iv & other$iv$iv$iv$iv) == 128)) {
                        codePoint$iv = 65533;
                        $i$a$-process4Utf8Bytes-Utf8$processUtf16Chars$3$iv = false;
                        if (codePoint$iv != 65533) {
                            c = (char)((codePoint$iv >>> 10) + 55232);
                            $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                            var11_11 = length;
                            length = var11_11 + 1;
                            chars[var11_11] = c;
                            c = (char)((codePoint$iv & 1023) + 56320);
                            $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                            var11_11 = length;
                            length = var11_11 + 1;
                            chars[var11_11] = c;
                        } else {
                            c = 65533;
                            $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                            var11_11 = length;
                            length = var11_11 + 1;
                            chars[var11_11] = c;
                        }
                        var17_20 = Unit.INSTANCE;
                        v6 = var15_18;
                        v7 = 1;
                    } else {
                        b2$iv$iv = $this$process4Utf8Bytes$iv$iv[index$iv + 2];
                        $i$f$isUtf8Continuation = false;
                        other$iv$iv$iv$iv = b2$iv$iv;
                        other$iv$iv$iv$iv = 192;
                        $i$f$and = false;
                        if (!(($this$and$iv$iv$iv$iv & other$iv$iv$iv$iv) == 128)) {
                            codePoint$iv = 65533;
                            $i$a$-process4Utf8Bytes-Utf8$processUtf16Chars$3$iv = false;
                            if (codePoint$iv != 65533) {
                                c = (char)((codePoint$iv >>> 10) + 55232);
                                $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                var11_11 = length;
                                length = var11_11 + 1;
                                chars[var11_11] = c;
                                c = (char)((codePoint$iv & 1023) + 56320);
                                $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                var11_11 = length;
                                length = var11_11 + 1;
                                chars[var11_11] = c;
                            } else {
                                c = 65533;
                                $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                var11_11 = length;
                                length = var11_11 + 1;
                                chars[var11_11] = c;
                            }
                            var17_20 = Unit.INSTANCE;
                            v6 = var15_18;
                            v7 = 2;
                        } else {
                            b3$iv$iv = $this$process4Utf8Bytes$iv$iv[index$iv + 3];
                            $i$f$isUtf8Continuation = false;
                            other$iv$iv$iv$iv = b3$iv$iv;
                            other$iv$iv$iv$iv = 192;
                            $i$f$and = false;
                            if (!(($this$and$iv$iv$iv$iv & other$iv$iv$iv$iv) == 128)) {
                                codePoint$iv = 65533;
                                $i$a$-process4Utf8Bytes-Utf8$processUtf16Chars$3$iv = false;
                                if (codePoint$iv != 65533) {
                                    c = (char)((codePoint$iv >>> 10) + 55232);
                                    $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                    var11_11 = length;
                                    length = var11_11 + 1;
                                    chars[var11_11] = c;
                                    c = (char)((codePoint$iv & 1023) + 56320);
                                    $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                    var11_11 = length;
                                    length = var11_11 + 1;
                                    chars[var11_11] = c;
                                } else {
                                    c = 65533;
                                    $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                    var11_11 = length;
                                    length = var11_11 + 1;
                                    chars[var11_11] = c;
                                }
                                var17_20 = Unit.INSTANCE;
                                v6 = var15_18;
                                v7 = 3;
                            } else {
                                codePoint$iv$iv = 3678080 ^ b3$iv$iv ^ b2$iv$iv << 6 ^ b1$iv$iv << 12 ^ b0$iv$iv << 18;
                                if (codePoint$iv$iv > 0x10FFFF) {
                                    codePoint$iv = 65533;
                                    $i$a$-process4Utf8Bytes-Utf8$processUtf16Chars$3$iv = false;
                                    if (codePoint$iv != 65533) {
                                        c = (char)((codePoint$iv >>> 10) + 55232);
                                        $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                        var11_11 = length;
                                        length = var11_11 + 1;
                                        chars[var11_11] = c;
                                        c = (char)((codePoint$iv & 1023) + 56320);
                                        $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                        var11_11 = length;
                                        length = var11_11 + 1;
                                        chars[var11_11] = c;
                                    } else {
                                        c = 65533;
                                        $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                        var11_11 = length;
                                        length = var11_11 + 1;
                                        chars[var11_11] = c;
                                    }
                                    var17_20 = Unit.INSTANCE;
                                    v6 = var15_18;
                                } else {
                                    v8 = 55296 <= codePoint$iv$iv ? codePoint$iv$iv < 57344 : false;
                                    if (v8) {
                                        codePoint$iv = 65533;
                                        $i$a$-process4Utf8Bytes-Utf8$processUtf16Chars$3$iv = false;
                                        if (codePoint$iv != 65533) {
                                            c = (char)((codePoint$iv >>> 10) + 55232);
                                            $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                            var11_11 = length;
                                            length = var11_11 + 1;
                                            chars[var11_11] = c;
                                            c = (char)((codePoint$iv & 1023) + 56320);
                                            $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                            var11_11 = length;
                                            length = var11_11 + 1;
                                            chars[var11_11] = c;
                                        } else {
                                            c = 65533;
                                            $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                            var11_11 = length;
                                            length = var11_11 + 1;
                                            chars[var11_11] = c;
                                        }
                                        var17_20 = Unit.INSTANCE;
                                        v6 = var15_18;
                                    } else if (codePoint$iv$iv < 65536) {
                                        codePoint$iv = 65533;
                                        $i$a$-process4Utf8Bytes-Utf8$processUtf16Chars$3$iv = false;
                                        if (codePoint$iv != 65533) {
                                            c = (char)((codePoint$iv >>> 10) + 55232);
                                            $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                            var11_11 = length;
                                            length = var11_11 + 1;
                                            chars[var11_11] = c;
                                            c = (char)((codePoint$iv & 1023) + 56320);
                                            $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                            var11_11 = length;
                                            length = var11_11 + 1;
                                            chars[var11_11] = c;
                                        } else {
                                            c = 65533;
                                            $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                            var11_11 = length;
                                            length = var11_11 + 1;
                                            chars[var11_11] = c;
                                        }
                                        var17_20 = Unit.INSTANCE;
                                        v6 = var15_18;
                                    } else {
                                        codePoint$iv = codePoint$iv$iv;
                                        $i$a$-process4Utf8Bytes-Utf8$processUtf16Chars$3$iv = false;
                                        if (codePoint$iv != 65533) {
                                            c = (char)((codePoint$iv >>> 10) + 55232);
                                            $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                            var11_11 = length;
                                            length = var11_11 + 1;
                                            chars[var11_11] = c;
                                            c = (char)((codePoint$iv & 1023) + 56320);
                                            $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                            var11_11 = length;
                                            length = var11_11 + 1;
                                            chars[var11_11] = c;
                                        } else {
                                            c = 65533;
                                            $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
                                            var11_11 = length;
                                            length = var11_11 + 1;
                                            chars[var11_11] = c;
                                        }
                                        var17_20 = Unit.INSTANCE;
                                        v6 = var15_18;
                                    }
                                }
                                v7 = 4;
                            }
                        }
                    }
                }
                index$iv = v6 + v7;
                v0 = Unit.INSTANCE;
                continue;
            }
            c = 65533;
            $i$a$-processUtf16Chars-_Utf8Kt$commonToUtf8String$1 = false;
            var11_11 = length;
            length = var11_11 + 1;
            chars[var11_11] = c;
            v0 = index$iv++;
        }
        return StringsKt.concatToString(chars, 0, length);
    }

    public static /* synthetic */ String commonToUtf8String$default(byte[] byArray, int n2, int n3, int n4, Object object) {
        if ((n4 & 1) != 0) {
            n2 = 0;
        }
        if ((n4 & 2) != 0) {
            n3 = byArray.length;
        }
        return _Utf8Kt.commonToUtf8String(byArray, n2, n3);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final byte[] commonAsUtf8ToByteArray(@NotNull String $this$commonAsUtf8ToByteArray) {
        Intrinsics.checkNotNullParameter($this$commonAsUtf8ToByteArray, "<this>");
        byte[] bytes = new byte[4 * $this$commonAsUtf8ToByteArray.length()];
        int n2 = $this$commonAsUtf8ToByteArray.length();
        for (int index = 0; index < n2; ++index) {
            char b0;
            block10: {
                b0 = $this$commonAsUtf8ToByteArray.charAt(index);
                if (Intrinsics.compare(b0, 128) < 0) break block10;
                int size = 0;
                size = index;
                String string = $this$commonAsUtf8ToByteArray;
                int endIndex$iv = $this$commonAsUtf8ToByteArray.length();
                boolean $i$f$processUtf8Bytes = false;
                int index$iv = index;
                while (index$iv < endIndex$iv) {
                    int n3;
                    int n4;
                    char c$iv;
                    void $this$processUtf8Bytes$iv;
                    block12: {
                        block11: {
                            boolean bl;
                            int n42;
                            c$iv = $this$processUtf8Bytes$iv.charAt(index$iv);
                            if (Intrinsics.compare(c$iv, 128) < 0) {
                                n4 = c$iv;
                                boolean bl2 = false;
                                n42 = size;
                                size = n42 + 1;
                                bytes[n42] = n4;
                                ++index$iv;
                                while (index$iv < endIndex$iv && Intrinsics.compare($this$processUtf8Bytes$iv.charAt(index$iv), 128) < 0) {
                                    n4 = (byte)$this$processUtf8Bytes$iv.charAt(index$iv++);
                                    bl = false;
                                    n42 = size;
                                    size = n42 + 1;
                                    bytes[n42] = n4;
                                }
                                continue;
                            }
                            if (Intrinsics.compare(c$iv, 2048) < 0) {
                                n4 = (byte)(c$iv >> 6 | 0xC0);
                                bl = false;
                                n42 = size;
                                size = n42 + 1;
                                bytes[n42] = n4;
                                n4 = (byte)(c$iv & 0x3F | 0x80);
                                bl = false;
                                n42 = size;
                                size = n42 + 1;
                                bytes[n42] = n4;
                                ++index$iv;
                                continue;
                            }
                            if (!('\ud800' <= c$iv ? c$iv < '\ue000' : false)) {
                                n4 = (byte)(c$iv >> 12 | 0xE0);
                                bl = false;
                                n42 = size;
                                size = n42 + 1;
                                bytes[n42] = n4;
                                n4 = (byte)(c$iv >> 6 & 0x3F | 0x80);
                                bl = false;
                                n42 = size;
                                size = n42 + 1;
                                bytes[n42] = n4;
                                n4 = (byte)(c$iv & 0x3F | 0x80);
                                bl = false;
                                n42 = size;
                                size = n42 + 1;
                                bytes[n42] = n4;
                                ++index$iv;
                                continue;
                            }
                            if (Intrinsics.compare(c$iv, 56319) > 0 || endIndex$iv <= index$iv + 1) break block11;
                            n4 = $this$processUtf8Bytes$iv.charAt(index$iv + 1);
                            if (56320 <= n4 ? n4 < 57344 : false) break block12;
                        }
                        int c2 = 63;
                        boolean bl = false;
                        n3 = size;
                        size = n3 + 1;
                        bytes[n3] = c2;
                        ++index$iv;
                        continue;
                    }
                    n4 = (c$iv << 10) + $this$processUtf8Bytes$iv.charAt(index$iv + 1) + -56613888;
                    byte c2 = (byte)(n4 >> 18 | 0xF0);
                    boolean bl = false;
                    n3 = size;
                    size = n3 + 1;
                    bytes[n3] = c2;
                    c2 = (byte)(n4 >> 12 & 0x3F | 0x80);
                    bl = false;
                    n3 = size;
                    size = n3 + 1;
                    bytes[n3] = c2;
                    c2 = (byte)(n4 >> 6 & 0x3F | 0x80);
                    bl = false;
                    n3 = size;
                    size = n3 + 1;
                    bytes[n3] = c2;
                    c2 = (byte)(n4 & 0x3F | 0x80);
                    bl = false;
                    n3 = size;
                    size = n3 + 1;
                    bytes[n3] = c2;
                    index$iv += 2;
                }
                byte[] byArray = Arrays.copyOf(bytes, size);
                Intrinsics.checkNotNullExpressionValue(byArray, "copyOf(...)");
                return byArray;
            }
            bytes[index] = (byte)b0;
        }
        byte[] byArray = Arrays.copyOf(bytes, $this$commonAsUtf8ToByteArray.length());
        Intrinsics.checkNotNullExpressionValue(byArray, "copyOf(...)");
        return byArray;
    }
}

