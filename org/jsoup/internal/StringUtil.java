/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package org.jsoup.internal;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import org.jsoup.helper.Validate;
import org.jsoup.internal.SoftPool;
import org.jspecify.annotations.Nullable;

public final class StringUtil {
    static final String[] padding = new String[]{"", " ", "  ", "   ", "    ", "     ", "      ", "       ", "        ", "         ", "          ", "           ", "            ", "             ", "              ", "               ", "                ", "                 ", "                  ", "                   ", "                    "};
    private static final Pattern extraDotSegmentsPattern = Pattern.compile("^/(?>(?>\\.\\.?/)+)");
    private static final Pattern validUriScheme = Pattern.compile("^[a-zA-Z][a-zA-Z0-9+-.]*:");
    private static final Pattern controlChars = Pattern.compile("[\\x00-\\x1f]*");
    private static final int InitBuilderSize = 1024;
    private static final int MaxBuilderSize = 8192;
    private static final SoftPool<StringBuilder> BuilderPool = new SoftPool<StringBuilder>(() -> new StringBuilder(1024));

    public static String join(Collection<?> strings, String sep) {
        return StringUtil.join(strings.iterator(), sep);
    }

    public static String join(Iterator<?> strings, String sep) {
        if (!strings.hasNext()) {
            return "";
        }
        String start = strings.next().toString();
        if (!strings.hasNext()) {
            return start;
        }
        StringJoiner j2 = new StringJoiner(sep);
        j2.add(start);
        while (strings.hasNext()) {
            j2.add(strings.next());
        }
        return j2.complete();
    }

    public static String join(String[] strings, String sep) {
        return StringUtil.join(Arrays.asList(strings), sep);
    }

    public static String padding(int width) {
        return StringUtil.padding(width, 30);
    }

    public static String padding(int width, int maxPaddingWidth) {
        Validate.isTrue(width >= 0, "width must be >= 0");
        Validate.isTrue(maxPaddingWidth >= -1);
        if (maxPaddingWidth != -1) {
            width = Math.min(width, maxPaddingWidth);
        }
        if (width < padding.length) {
            return padding[width];
        }
        char[] out = new char[width];
        for (int i2 = 0; i2 < width; ++i2) {
            out[i2] = 32;
        }
        return String.valueOf(out);
    }

    public static boolean isBlank(String string) {
        if (string == null || string.length() == 0) {
            return true;
        }
        int l2 = string.length();
        for (int i2 = 0; i2 < l2; ++i2) {
            if (StringUtil.isWhitespace(string.codePointAt(i2))) continue;
            return false;
        }
        return true;
    }

    public static boolean startsWithNewline(String string) {
        if (string == null || string.length() == 0) {
            return false;
        }
        return string.charAt(0) == '\n';
    }

    public static boolean isNumeric(String string) {
        if (string == null || string.length() == 0) {
            return false;
        }
        int l2 = string.length();
        for (int i2 = 0; i2 < l2; ++i2) {
            if (Character.isDigit(string.codePointAt(i2))) continue;
            return false;
        }
        return true;
    }

    public static boolean isWhitespace(int c2) {
        return c2 == 32 || c2 == 9 || c2 == 10 || c2 == 12 || c2 == 13;
    }

    public static boolean isActuallyWhitespace(int c2) {
        return c2 == 32 || c2 == 9 || c2 == 10 || c2 == 12 || c2 == 13 || c2 == 160;
    }

    public static boolean isInvisibleChar(int c2) {
        return c2 == 8203 || c2 == 173;
    }

    public static String normaliseWhitespace(String string) {
        StringBuilder sb = StringUtil.borrowBuilder();
        StringUtil.appendNormalisedWhitespace(sb, string, false);
        return StringUtil.releaseBuilder(sb);
    }

    public static void appendNormalisedWhitespace(StringBuilder accum, String string, boolean stripLeading) {
        int c2;
        boolean lastWasWhite = false;
        boolean reachedNonWhite = false;
        int len = string.length();
        for (int i2 = 0; i2 < len; i2 += Character.charCount(c2)) {
            c2 = string.codePointAt(i2);
            if (StringUtil.isActuallyWhitespace(c2)) {
                if (stripLeading && !reachedNonWhite || lastWasWhite) continue;
                accum.append(' ');
                lastWasWhite = true;
                continue;
            }
            if (StringUtil.isInvisibleChar(c2)) continue;
            accum.appendCodePoint(c2);
            lastWasWhite = false;
            reachedNonWhite = true;
        }
    }

    public static boolean in(String needle, String ... haystack) {
        int len = haystack.length;
        for (int i2 = 0; i2 < len; ++i2) {
            if (!haystack[i2].equals(needle)) continue;
            return true;
        }
        return false;
    }

    public static boolean inSorted(String needle, String[] haystack) {
        return Arrays.binarySearch(haystack, needle) >= 0;
    }

    public static boolean isAscii(String string) {
        Validate.notNull(string);
        for (int i2 = 0; i2 < string.length(); ++i2) {
            char c2 = string.charAt(i2);
            if (c2 <= '\u007f') continue;
            return false;
        }
        return true;
    }

    public static URL resolve(URL base, String relUrl) throws MalformedURLException {
        if ((relUrl = StringUtil.stripControlChars(relUrl)).startsWith("?")) {
            relUrl = base.getPath() + relUrl;
        }
        URL url = new URL(base, relUrl);
        String fixedFile = extraDotSegmentsPattern.matcher(url.getFile()).replaceFirst("/");
        if (url.getRef() != null) {
            fixedFile = fixedFile + "#" + url.getRef();
        }
        return new URL(url.getProtocol(), url.getHost(), url.getPort(), fixedFile);
    }

    public static String resolve(String baseUrl, String relUrl) {
        baseUrl = StringUtil.stripControlChars(baseUrl);
        relUrl = StringUtil.stripControlChars(relUrl);
        try {
            URL base;
            try {
                base = new URL(baseUrl);
            }
            catch (MalformedURLException e2) {
                URL abs = new URL(relUrl);
                return abs.toExternalForm();
            }
            return StringUtil.resolve(base, relUrl).toExternalForm();
        }
        catch (MalformedURLException e3) {
            return validUriScheme.matcher(relUrl).find() ? relUrl : "";
        }
    }

    private static String stripControlChars(String input) {
        return controlChars.matcher(input).replaceAll("");
    }

    public static StringBuilder borrowBuilder() {
        return BuilderPool.borrow();
    }

    public static String releaseBuilder(StringBuilder sb) {
        Validate.notNull(sb);
        String string = sb.toString();
        if (sb.length() <= 8192) {
            sb.delete(0, sb.length());
            BuilderPool.release(sb);
        }
        return string;
    }

    public static Collector<CharSequence, ?, String> joining(String delimiter) {
        return Collector.of(() -> new StringJoiner(delimiter), StringJoiner::add, (j1, j2) -> {
            j1.append(j2.complete());
            return j1;
        }, StringJoiner::complete, new Collector.Characteristics[0]);
    }

    public static class StringJoiner {
        @Nullable StringBuilder sb = StringUtil.borrowBuilder();
        final String separator;
        boolean first = true;

        public StringJoiner(String separator) {
            this.separator = separator;
        }

        public StringJoiner add(Object stringy) {
            Validate.notNull(this.sb);
            if (!this.first) {
                this.sb.append(this.separator);
            }
            this.sb.append(stringy);
            this.first = false;
            return this;
        }

        public StringJoiner append(Object stringy) {
            Validate.notNull(this.sb);
            this.sb.append(stringy);
            return this;
        }

        public String complete() {
            String string = StringUtil.releaseBuilder(this.sb);
            this.sb = null;
            return string;
        }
    }
}

