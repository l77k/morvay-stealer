/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal;

import java.net.IDN;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.internal.Util;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u0000&\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a0\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0005H\u0002\u001a\"\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0002\u001a\u0010\u0010\f\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0002\u001a\f\u0010\r\u001a\u00020\u0001*\u00020\u0003H\u0002\u001a\f\u0010\u000e\u001a\u0004\u0018\u00010\u0003*\u00020\u0003\u00a8\u0006\u000f"}, d2={"decodeIpv4Suffix", "", "input", "", "pos", "", "limit", "address", "", "addressOffset", "decodeIpv6", "Ljava/net/InetAddress;", "inet6AddressToAscii", "containsInvalidHostnameAsciiCodes", "toCanonicalHost", "okhttp"})
public final class HostnamesKt {
    @Nullable
    public static final String toCanonicalHost(@NotNull String $this$toCanonicalHost) {
        Intrinsics.checkNotNullParameter($this$toCanonicalHost, "<this>");
        String host = $this$toCanonicalHost;
        if (StringsKt.contains$default((CharSequence)host, ":", false, 2, null)) {
            InetAddress inetAddress;
            InetAddress inetAddress2 = inetAddress = StringsKt.startsWith$default(host, "[", false, 2, null) && StringsKt.endsWith$default(host, "]", false, 2, null) ? HostnamesKt.decodeIpv6(host, 1, host.length() - 1) : HostnamesKt.decodeIpv6(host, 0, host.length());
            if (inetAddress == null) {
                return null;
            }
            InetAddress inetAddress3 = inetAddress;
            byte[] address = inetAddress3.getAddress();
            if (address.length == 16) {
                Intrinsics.checkNotNullExpressionValue(address, "address");
                return HostnamesKt.inet6AddressToAscii(address);
            }
            if (address.length == 4) {
                return inetAddress3.getHostAddress();
            }
            throw new AssertionError((Object)("Invalid IPv6 address: '" + host + '\''));
        }
        try {
            String string = IDN.toASCII(host);
            Intrinsics.checkNotNullExpressionValue(string, "toASCII(host)");
            Locale locale = Locale.US;
            Intrinsics.checkNotNullExpressionValue(locale, "US");
            String string2 = string.toLowerCase(locale);
            Intrinsics.checkNotNullExpressionValue(string2, "this as java.lang.String).toLowerCase(locale)");
            String result = string2;
            if (((CharSequence)result).length() == 0) {
                return null;
            }
            return HostnamesKt.containsInvalidHostnameAsciiCodes(result) ? (String)null : result;
        }
        catch (IllegalArgumentException _) {
            return null;
        }
    }

    private static final boolean containsInvalidHostnameAsciiCodes(String $this$containsInvalidHostnameAsciiCodes) {
        int n2 = 0;
        int n3 = $this$containsInvalidHostnameAsciiCodes.length();
        while (n2 < n3) {
            int i2;
            char c2;
            if (Intrinsics.compare(c2 = $this$containsInvalidHostnameAsciiCodes.charAt(i2 = n2++), 31) <= 0 || Intrinsics.compare(c2, 127) >= 0) {
                return true;
            }
            if (StringsKt.indexOf$default((CharSequence)" #%/:?@[\\]", c2, 0, false, 6, null) == -1) continue;
            return true;
        }
        return false;
    }

    private static final InetAddress decodeIpv6(String input, int pos, int limit) {
        byte[] address = new byte[16];
        int b2 = 0;
        int compress = -1;
        int groupOffset = -1;
        int i2 = pos;
        while (i2 < limit) {
            int n2;
            int hexDigit;
            if (b2 == address.length) {
                return null;
            }
            if (i2 + 2 <= limit && StringsKt.startsWith$default(input, "::", i2, false, 4, null)) {
                if (compress != -1) {
                    return null;
                }
                compress = b2 += 2;
                if ((i2 += 2) == limit) {
                    break;
                }
            } else if (b2 != 0) {
                if (StringsKt.startsWith$default(input, ":", i2, false, 4, null)) {
                    int n3 = i2;
                    i2 = n3 + 1;
                } else {
                    if (StringsKt.startsWith$default(input, ".", i2, false, 4, null)) {
                        if (!HostnamesKt.decodeIpv4Suffix(input, groupOffset, limit, address, b2 - 2)) {
                            return null;
                        }
                        b2 += 2;
                        break;
                    }
                    return null;
                }
            }
            int value = 0;
            groupOffset = i2;
            while (i2 < limit && (hexDigit = Util.parseHexDigit(input.charAt(i2))) != -1) {
                value = (value << 4) + hexDigit;
                n2 = i2;
                i2 = n2 + 1;
            }
            int groupLength = i2 - groupOffset;
            if (groupLength == 0 || groupLength > 4) {
                return null;
            }
            n2 = b2;
            b2 = n2 + 1;
            address[n2] = (byte)(value >>> 8 & 0xFF);
            n2 = b2;
            b2 = n2 + 1;
            address[n2] = (byte)(value & 0xFF);
        }
        if (b2 != address.length) {
            if (compress == -1) {
                return null;
            }
            System.arraycopy(address, compress, address, address.length - (b2 - compress), b2 - compress);
            Arrays.fill(address, compress, compress + (address.length - b2), (byte)0);
        }
        return InetAddress.getByAddress(address);
    }

    private static final boolean decodeIpv4Suffix(String input, int pos, int limit, byte[] address, int addressOffset) {
        int b2 = addressOffset;
        int i2 = pos;
        while (i2 < limit) {
            int n2;
            char c2;
            if (b2 == address.length) {
                return false;
            }
            if (b2 != addressOffset) {
                if (input.charAt(i2) != '.') {
                    return false;
                }
                int n3 = i2;
                i2 = n3 + 1;
            }
            int value = 0;
            int groupOffset = i2;
            while (i2 < limit && Intrinsics.compare(c2 = input.charAt(i2), 48) >= 0 && Intrinsics.compare(c2, 57) <= 0) {
                if (value == 0 && groupOffset != i2) {
                    return false;
                }
                if ((value = value * 10 + c2 - 48) > 255) {
                    return false;
                }
                n2 = i2;
                i2 = n2 + 1;
            }
            int groupLength = i2 - groupOffset;
            if (groupLength == 0) {
                return false;
            }
            n2 = b2;
            b2 = n2 + 1;
            address[n2] = (byte)value;
        }
        return b2 == addressOffset + 4;
    }

    private static final String inet6AddressToAscii(byte[] address) {
        int i2;
        int longestRunOffset = 0;
        longestRunOffset = -1;
        int longestRunLength = 0;
        boolean bl = false;
        for (i2 = 0; i2 < address.length; i2 += 2) {
            int currentRunOffset = i2;
            while (i2 < 16 && address[i2] == 0 && address[i2 + 1] == 0) {
                i2 += 2;
            }
            int currentRunLength = i2 - currentRunOffset;
            if (currentRunLength <= longestRunLength || currentRunLength < 4) continue;
            longestRunOffset = currentRunOffset;
            longestRunLength = currentRunLength;
        }
        Buffer result = new Buffer();
        i2 = 0;
        while (i2 < address.length) {
            if (i2 == longestRunOffset) {
                result.writeByte(58);
                if ((i2 += longestRunLength) != 16) continue;
                result.writeByte(58);
                continue;
            }
            if (i2 > 0) {
                result.writeByte(58);
            }
            int group = Util.and(address[i2], 255) << 8 | Util.and(address[i2 + 1], 255);
            result.writeHexadecimalUnsignedLong(group);
            i2 += 2;
        }
        return result.readUtf8();
    }
}

