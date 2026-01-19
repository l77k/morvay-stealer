/*
 * Decompiled with CFR 0.152.
 */
package org.jsoup.nodes;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import org.jsoup.SerializationException;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.EntitiesData;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.Parser;

public class Entities {
    static final int ForText = 1;
    static final int ForAttribute = 2;
    static final int Normalise = 4;
    static final int TrimLeading = 8;
    static final int TrimTrailing = 16;
    private static final int empty = -1;
    private static final String emptyName = "";
    static final int codepointRadix = 36;
    private static final char[] codeDelims = new char[]{',', ';'};
    private static final HashMap<String, String> multipoints = new HashMap();
    private static final int BaseCount = 106;
    private static final ArrayList<String> baseSorted = new ArrayList(106);
    private static final ThreadLocal<char[]> charBuf = ThreadLocal.withInitial(() -> new char[2]);
    private static final ThreadLocal<CharsetEncoder> LocalEncoder = new ThreadLocal();

    private Entities() {
    }

    public static boolean isNamedEntity(String name) {
        return EscapeMode.extended.codepointForName(name) != -1;
    }

    public static boolean isBaseNamedEntity(String name) {
        return EscapeMode.base.codepointForName(name) != -1;
    }

    public static String getByName(String name) {
        String val = multipoints.get(name);
        if (val != null) {
            return val;
        }
        int codepoint = EscapeMode.extended.codepointForName(name);
        if (codepoint != -1) {
            return new String(new int[]{codepoint}, 0, 1);
        }
        return emptyName;
    }

    public static int codepointsForName(String name, int[] codepoints) {
        String val = multipoints.get(name);
        if (val != null) {
            codepoints[0] = val.codePointAt(0);
            codepoints[1] = val.codePointAt(1);
            return 2;
        }
        int codepoint = EscapeMode.extended.codepointForName(name);
        if (codepoint != -1) {
            codepoints[0] = codepoint;
            return 1;
        }
        return 0;
    }

    public static String findPrefix(String input) {
        for (String name : baseSorted) {
            if (!input.startsWith(name)) continue;
            return name;
        }
        return emptyName;
    }

    public static String escape(String data, Document.OutputSettings out) {
        return Entities.escapeString(data, out.escapeMode(), out.syntax(), out.charset());
    }

    public static String escape(String data) {
        return Entities.escapeString(data, EscapeMode.base, Document.OutputSettings.Syntax.html, DataUtil.UTF_8);
    }

    private static String escapeString(String data, EscapeMode escapeMode, Document.OutputSettings.Syntax syntax, Charset charset) {
        if (data == null) {
            return emptyName;
        }
        StringBuilder accum = StringUtil.borrowBuilder();
        try {
            Entities.doEscape(data, accum, escapeMode, syntax, charset, 3);
        }
        catch (IOException e2) {
            throw new SerializationException(e2);
        }
        return StringUtil.releaseBuilder(accum);
    }

    static void escape(Appendable accum, String data, Document.OutputSettings out, int options) throws IOException {
        Entities.doEscape(data, accum, out.escapeMode(), out.syntax(), out.charset(), options);
    }

    private static void doEscape(String data, Appendable accum, EscapeMode mode, Document.OutputSettings.Syntax syntax, Charset charset, int options) throws IOException {
        int codePoint;
        CoreCharset coreCharset = CoreCharset.byName(charset.name());
        CharsetEncoder fallback = Entities.encoderFor(charset);
        int length = data.length();
        boolean lastWasWhite = false;
        boolean reachedNonWhite = false;
        boolean skipped = false;
        for (int offset = 0; offset < length; offset += Character.charCount(codePoint)) {
            codePoint = data.codePointAt(offset);
            if ((options & 4) != 0) {
                if (StringUtil.isWhitespace(codePoint)) {
                    if ((options & 8) != 0 && !reachedNonWhite || lastWasWhite) continue;
                    if ((options & 0x10) != 0) {
                        skipped = true;
                        continue;
                    }
                    accum.append(' ');
                    lastWasWhite = true;
                    continue;
                }
                lastWasWhite = false;
                reachedNonWhite = true;
                if (skipped) {
                    accum.append(' ');
                    skipped = false;
                }
            }
            Entities.appendEscaped(codePoint, accum, options, mode, syntax, coreCharset, fallback);
        }
    }

    private static void appendEscaped(int codePoint, Appendable accum, int options, EscapeMode escapeMode, Document.OutputSettings.Syntax syntax, CoreCharset coreCharset, CharsetEncoder fallback) throws IOException {
        char c2 = (char)codePoint;
        if (codePoint < 65536) {
            switch (c2) {
                case '&': {
                    accum.append("&amp;");
                    break;
                }
                case '\u00a0': {
                    Entities.appendNbsp(accum, escapeMode);
                    break;
                }
                case '<': {
                    Entities.appendLt(accum, options, escapeMode, syntax);
                    break;
                }
                case '>': {
                    if ((options & 1) != 0) {
                        accum.append("&gt;");
                        break;
                    }
                    accum.append(c2);
                    break;
                }
                case '\"': {
                    if ((options & 2) != 0) {
                        accum.append("&quot;");
                        break;
                    }
                    accum.append(c2);
                    break;
                }
                case '\'': {
                    Entities.appendApos(accum, options, escapeMode);
                    break;
                }
                case '\t': 
                case '\n': 
                case '\r': {
                    accum.append(c2);
                    break;
                }
                default: {
                    if (c2 < ' ' || !Entities.canEncode(coreCharset, c2, fallback)) {
                        Entities.appendEncoded(accum, escapeMode, codePoint);
                        break;
                    }
                    accum.append(c2);
                    break;
                }
            }
        } else if (Entities.canEncode(coreCharset, c2, fallback)) {
            char[] chars = charBuf.get();
            int len = Character.toChars(codePoint, chars, 0);
            if (accum instanceof StringBuilder) {
                ((StringBuilder)accum).append(chars, 0, len);
            } else {
                accum.append(new String(chars, 0, len));
            }
        } else {
            Entities.appendEncoded(accum, escapeMode, codePoint);
        }
    }

    private static void appendNbsp(Appendable accum, EscapeMode escapeMode) throws IOException {
        if (escapeMode != EscapeMode.xhtml) {
            accum.append("&nbsp;");
        } else {
            accum.append("&#xa0;");
        }
    }

    private static void appendLt(Appendable accum, int options, EscapeMode escapeMode, Document.OutputSettings.Syntax syntax) throws IOException {
        if ((options & 1) != 0 || escapeMode == EscapeMode.xhtml || syntax == Document.OutputSettings.Syntax.xml) {
            accum.append("&lt;");
        } else {
            accum.append('<');
        }
    }

    private static void appendApos(Appendable accum, int options, EscapeMode escapeMode) throws IOException {
        if ((options & 2) != 0 && (options & 1) != 0) {
            if (escapeMode == EscapeMode.xhtml) {
                accum.append("&#x27;");
            } else {
                accum.append("&apos;");
            }
        } else {
            accum.append('\'');
        }
    }

    private static void appendEncoded(Appendable accum, EscapeMode escapeMode, int codePoint) throws IOException {
        String name = escapeMode.nameForCodepoint(codePoint);
        if (!emptyName.equals(name)) {
            accum.append('&').append(name).append(';');
        } else {
            accum.append("&#x").append(Integer.toHexString(codePoint)).append(';');
        }
    }

    public static String unescape(String string) {
        return Entities.unescape(string, false);
    }

    static String unescape(String string, boolean strict) {
        return Parser.unescapeEntities(string, strict);
    }

    private static boolean canEncode(CoreCharset charset, char c2, CharsetEncoder fallback) {
        switch (charset) {
            case ascii: {
                return c2 < '\u0080';
            }
            case utf: {
                return c2 < '\ud800' || c2 >= '\ue000';
            }
        }
        return fallback.canEncode(c2);
    }

    private static CharsetEncoder encoderFor(Charset charset) {
        CharsetEncoder encoder = LocalEncoder.get();
        if (encoder == null || !encoder.charset().equals(charset)) {
            encoder = charset.newEncoder();
            LocalEncoder.set(encoder);
        }
        return encoder;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void load(EscapeMode e2, String pointsData, int size) {
        EscapeMode.access$202(e2, new String[size]);
        EscapeMode.access$302(e2, new int[size]);
        EscapeMode.access$402(e2, new int[size]);
        EscapeMode.access$502(e2, new String[size]);
        int i2 = 0;
        try (CharacterReader reader = new CharacterReader(pointsData);){
            while (!reader.isEmpty()) {
                int cp2;
                String name = reader.consumeTo('=');
                reader.advance();
                int cp1 = Integer.parseInt(reader.consumeToAny(codeDelims), 36);
                char codeDelim = reader.current();
                reader.advance();
                if (codeDelim == ',') {
                    cp2 = Integer.parseInt(reader.consumeTo(';'), 36);
                    reader.advance();
                } else {
                    cp2 = -1;
                }
                String indexS = reader.consumeTo('&');
                int index = Integer.parseInt(indexS, 36);
                reader.advance();
                ((EscapeMode)e2).nameKeys[i2] = name;
                ((EscapeMode)e2).codeVals[i2] = cp1;
                ((EscapeMode)e2).codeKeys[index] = cp1;
                ((EscapeMode)e2).nameVals[index] = name;
                if (cp2 != -1) {
                    multipoints.put(name, new String(new int[]{cp1, cp2}, 0, 2));
                }
                ++i2;
            }
            Validate.isTrue(i2 == size, "Unexpected count of entities loaded");
        }
    }

    public static enum EscapeMode {
        xhtml(EntitiesData.xmlPoints, 4),
        base(EntitiesData.basePoints, 106),
        extended(EntitiesData.fullPoints, 2125);

        private String[] nameKeys;
        private int[] codeVals;
        private int[] codeKeys;
        private String[] nameVals;

        private EscapeMode(String file, int size) {
            Entities.load(this, file, size);
        }

        int codepointForName(String name) {
            int index = Arrays.binarySearch(this.nameKeys, name);
            return index >= 0 ? this.codeVals[index] : -1;
        }

        String nameForCodepoint(int codepoint) {
            int index = Arrays.binarySearch(this.codeKeys, codepoint);
            if (index >= 0) {
                return index < this.nameVals.length - 1 && this.codeKeys[index + 1] == codepoint ? this.nameVals[index + 1] : this.nameVals[index];
            }
            return Entities.emptyName;
        }

        static /* synthetic */ String[] access$202(EscapeMode x0, String[] x1) {
            x0.nameKeys = x1;
            return x1;
        }

        static /* synthetic */ int[] access$302(EscapeMode x0, int[] x1) {
            x0.codeVals = x1;
            return x1;
        }

        static /* synthetic */ int[] access$402(EscapeMode x0, int[] x1) {
            x0.codeKeys = x1;
            return x1;
        }

        static /* synthetic */ String[] access$502(EscapeMode x0, String[] x1) {
            x0.nameVals = x1;
            return x1;
        }

        static {
            Collections.addAll(baseSorted, EscapeMode.base.nameKeys);
            baseSorted.sort((a2, b2) -> b2.length() - a2.length());
        }
    }

    static enum CoreCharset {
        ascii,
        utf,
        fallback;


        static CoreCharset byName(String name) {
            if (name.equals("US-ASCII")) {
                return ascii;
            }
            if (name.startsWith("UTF-")) {
                return utf;
            }
            return fallback;
        }
    }
}

