/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package org.jsoup.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import org.jsoup.helper.Validate;
import org.jsoup.internal.ControllableInputStream;
import org.jsoup.internal.Normalizer;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.parser.StreamParser;
import org.jsoup.select.Elements;
import org.jspecify.annotations.Nullable;

public final class DataUtil {
    private static final Pattern charsetPattern = Pattern.compile("(?i)\\bcharset=\\s*(?:[\"'])?([^\\s,;\"']*)");
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    static final String defaultCharsetName = UTF_8.name();
    private static final int firstReadBufferSize = 5120;
    private static final char[] mimeBoundaryChars = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    static final int boundaryLength = 32;

    private DataUtil() {
    }

    public static Document load(File file, @Nullable String charsetName, String baseUri) throws IOException {
        return DataUtil.load(file.toPath(), charsetName, baseUri);
    }

    public static Document load(File file, @Nullable String charsetName, String baseUri, Parser parser) throws IOException {
        return DataUtil.load(file.toPath(), charsetName, baseUri, parser);
    }

    public static Document load(Path path, @Nullable String charsetName, String baseUri) throws IOException {
        return DataUtil.load(path, charsetName, baseUri, Parser.htmlParser());
    }

    public static Document load(Path path, @Nullable String charsetName, String baseUri, Parser parser) throws IOException {
        return DataUtil.parseInputStream(DataUtil.openStream(path), charsetName, baseUri, parser);
    }

    public static StreamParser streamParser(Path path, @Nullable Charset charset, String baseUri, Parser parser) throws IOException {
        StreamParser streamer = new StreamParser(parser);
        String charsetName = charset != null ? charset.name() : null;
        CharsetDoc charsetDoc = DataUtil.detectCharset(DataUtil.openStream(path), charsetName, baseUri, parser);
        BufferedReader reader = new BufferedReader(new InputStreamReader(charsetDoc.input, charsetDoc.charset), 8192);
        streamer.parse(reader, baseUri);
        return streamer;
    }

    private static ControllableInputStream openStream(Path path) throws IOException {
        SeekableByteChannel byteChannel = Files.newByteChannel(path, new OpenOption[0]);
        InputStream stream = Channels.newInputStream(byteChannel);
        String name = Normalizer.lowerCase(path.getFileName().toString());
        if (name.endsWith(".gz") || name.endsWith(".z")) {
            boolean zipped = stream.read() == 31 && stream.read() == 139;
            byteChannel.position(0L);
            if (zipped) {
                stream = new GZIPInputStream(stream);
            }
        }
        return ControllableInputStream.wrap(stream, 0);
    }

    public static Document load(InputStream in, @Nullable String charsetName, String baseUri) throws IOException {
        return DataUtil.parseInputStream(ControllableInputStream.wrap(in, 0), charsetName, baseUri, Parser.htmlParser());
    }

    public static Document load(InputStream in, @Nullable String charsetName, String baseUri, Parser parser) throws IOException {
        return DataUtil.parseInputStream(ControllableInputStream.wrap(in, 0), charsetName, baseUri, parser);
    }

    static void crossStreams(InputStream in, OutputStream out) throws IOException {
        int len;
        byte[] buffer = new byte[8192];
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static Document parseInputStream(@Nullable ControllableInputStream input, @Nullable String charsetName, String baseUri, Parser parser) throws IOException {
        Document doc;
        if (input == null) {
            return new Document(baseUri);
        }
        CharsetDoc charsetDoc = null;
        try {
            charsetDoc = DataUtil.detectCharset(input, charsetName, baseUri, parser);
            doc = DataUtil.parseInputStream(charsetDoc, baseUri, parser);
        }
        finally {
            if (charsetDoc != null) {
                charsetDoc.input.close();
            }
        }
        return doc;
    }

    static CharsetDoc detectCharset(ControllableInputStream input, @Nullable String charsetName, String baseUri, Parser parser) throws IOException {
        Document doc = null;
        String bomCharset = DataUtil.detectCharsetFromBom(input);
        if (bomCharset != null) {
            charsetName = bomCharset;
        }
        if (charsetName == null) {
            int origMax = input.max();
            input.max(5120);
            input.mark(5120);
            input.allowClose(false);
            try {
                InputStreamReader reader = new InputStreamReader((InputStream)input, UTF_8);
                doc = parser.parseInput(reader, baseUri);
                input.reset();
                input.max(origMax);
            }
            catch (UncheckedIOException e2) {
                throw e2.getCause();
            }
            finally {
                input.allowClose(true);
            }
            Elements metaElements = doc.select("meta[http-equiv=content-type], meta[charset]");
            String foundCharset = null;
            for (Element meta : metaElements) {
                if (meta.hasAttr("http-equiv")) {
                    foundCharset = DataUtil.getCharsetFromContentType(meta.attr("content"));
                }
                if (foundCharset == null && meta.hasAttr("charset")) {
                    foundCharset = meta.attr("charset");
                }
                if (foundCharset == null) continue;
                break;
            }
            if (foundCharset == null && doc.childNodeSize() > 0) {
                Comment comment;
                Node first = doc.childNode(0);
                XmlDeclaration decl = null;
                if (first instanceof XmlDeclaration) {
                    decl = (XmlDeclaration)first;
                } else if (first instanceof Comment && (comment = (Comment)first).isXmlDeclaration()) {
                    decl = comment.asXmlDeclaration();
                }
                if (decl != null && decl.name().equalsIgnoreCase("xml")) {
                    foundCharset = decl.attr("encoding");
                }
            }
            if ((foundCharset = DataUtil.validateCharset(foundCharset)) != null && !foundCharset.equalsIgnoreCase(defaultCharsetName)) {
                charsetName = foundCharset = foundCharset.trim().replaceAll("[\"']", "");
                doc = null;
            } else if (input.baseReadFully()) {
                input.close();
            } else {
                doc = null;
            }
        } else {
            Validate.notEmpty(charsetName, "Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML");
        }
        if (charsetName == null) {
            charsetName = defaultCharsetName;
        }
        Charset charset = charsetName.equals(defaultCharsetName) ? UTF_8 : Charset.forName(charsetName);
        return new CharsetDoc(charset, doc, input);
    }

    static Document parseInputStream(CharsetDoc charsetDoc, String baseUri, Parser parser) throws IOException {
        Document doc;
        if (charsetDoc.doc != null) {
            return charsetDoc.doc;
        }
        InputStream input = charsetDoc.input;
        Validate.notNull(input);
        Charset charset = charsetDoc.charset;
        try (InputStreamReader reader = new InputStreamReader(input, charset);){
            try {
                doc = parser.parseInput(reader, baseUri);
            }
            catch (UncheckedIOException e2) {
                throw e2.getCause();
            }
            doc.outputSettings().charset(charset);
            if (!charset.canEncode()) {
                doc.charset(UTF_8);
            }
        }
        return doc;
    }

    public static ByteBuffer readToByteBuffer(InputStream inStream, int maxSize) throws IOException {
        return ControllableInputStream.readToByteBuffer(inStream, maxSize);
    }

    static ByteBuffer emptyByteBuffer() {
        return ByteBuffer.allocate(0);
    }

    static @Nullable String getCharsetFromContentType(@Nullable String contentType) {
        if (contentType == null) {
            return null;
        }
        Matcher m2 = charsetPattern.matcher(contentType);
        if (m2.find()) {
            String charset = m2.group(1).trim();
            charset = charset.replace("charset=", "");
            return DataUtil.validateCharset(charset);
        }
        return null;
    }

    private static @Nullable String validateCharset(@Nullable String cs) {
        if (cs == null || cs.length() == 0) {
            return null;
        }
        cs = cs.trim().replaceAll("[\"']", "");
        try {
            if (Charset.isSupported(cs)) {
                return cs;
            }
            if (Charset.isSupported(cs = cs.toUpperCase(Locale.ENGLISH))) {
                return cs;
            }
        }
        catch (IllegalCharsetNameException illegalCharsetNameException) {
            // empty catch block
        }
        return null;
    }

    static String mimeBoundary() {
        StringBuilder mime = StringUtil.borrowBuilder();
        Random rand = new Random();
        for (int i2 = 0; i2 < 32; ++i2) {
            mime.append(mimeBoundaryChars[rand.nextInt(mimeBoundaryChars.length)]);
        }
        return StringUtil.releaseBuilder(mime);
    }

    private static @Nullable String detectCharsetFromBom(ControllableInputStream input) throws IOException {
        byte[] bom = new byte[4];
        input.mark(bom.length);
        input.read(bom, 0, 4);
        input.reset();
        if (bom[0] == 0 && bom[1] == 0 && bom[2] == -2 && bom[3] == -1 || bom[0] == -1 && bom[1] == -2 && bom[2] == 0 && bom[3] == 0) {
            return "UTF-32";
        }
        if (bom[0] == -2 && bom[1] == -1 || bom[0] == -1 && bom[1] == -2) {
            return "UTF-16";
        }
        if (bom[0] == -17 && bom[1] == -69 && bom[2] == -65) {
            input.read(bom, 0, 3);
            return "UTF-8";
        }
        return null;
    }

    static class CharsetDoc {
        Charset charset;
        InputStream input;
        @Nullable Document doc;

        CharsetDoc(Charset charset, @Nullable Document doc, InputStream input) {
            this.charset = charset;
            this.input = input;
            this.doc = doc;
        }
    }
}

