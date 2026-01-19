/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.build;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.io.IORandomAccessFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.RandomAccessFileMode;
import org.apache.commons.io.RandomAccessFiles;
import org.apache.commons.io.build.AbstractSupplier;
import org.apache.commons.io.file.spi.FileSystemProviders;
import org.apache.commons.io.input.BufferedFileChannelInputStream;
import org.apache.commons.io.input.CharSequenceInputStream;
import org.apache.commons.io.input.CharSequenceReader;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.io.output.RandomAccessFileOutputStream;
import org.apache.commons.io.output.WriterOutputStream;

public abstract class AbstractOrigin<T, B extends AbstractOrigin<T, B>>
extends AbstractSupplier<T, B> {
    final T origin;

    protected AbstractOrigin(T origin) {
        this.origin = Objects.requireNonNull(origin, "origin");
    }

    @Override
    public T get() {
        return this.origin;
    }

    public byte[] getByteArray() throws IOException {
        return Files.readAllBytes(this.getPath());
    }

    public byte[] getByteArray(long position, int length) throws IOException {
        byte[] bytes = this.getByteArray();
        int start = Math.toIntExact(position);
        if (start < 0 || length < 0 || start + length < 0 || start + length > bytes.length) {
            throw new IllegalArgumentException("Couldn't read array (start: " + start + ", length: " + length + ", data length: " + bytes.length + ").");
        }
        return Arrays.copyOfRange(bytes, start, start + length);
    }

    public CharSequence getCharSequence(Charset charset) throws IOException {
        return new String(this.getByteArray(), charset);
    }

    public File getFile() {
        throw new UnsupportedOperationException(String.format("%s#getFile() for %s origin %s", this.getSimpleClassName(), this.origin.getClass().getSimpleName(), this.origin));
    }

    public InputStream getInputStream(OpenOption ... options) throws IOException {
        return Files.newInputStream(this.getPath(), options);
    }

    public OutputStream getOutputStream(OpenOption ... options) throws IOException {
        return Files.newOutputStream(this.getPath(), options);
    }

    public Path getPath() {
        throw new UnsupportedOperationException(String.format("%s#getPath() for %s origin %s", this.getSimpleClassName(), this.origin.getClass().getSimpleName(), this.origin));
    }

    public RandomAccessFile getRandomAccessFile(OpenOption ... openOption) throws FileNotFoundException {
        return RandomAccessFileMode.valueOf(openOption).create(this.getFile());
    }

    public Reader getReader(Charset charset) throws IOException {
        return Files.newBufferedReader(this.getPath(), charset);
    }

    private String getSimpleClassName() {
        return this.getClass().getSimpleName();
    }

    public Writer getWriter(Charset charset, OpenOption ... options) throws IOException {
        return Files.newBufferedWriter(this.getPath(), charset, options);
    }

    public long size() throws IOException {
        return Files.size(this.getPath());
    }

    public String toString() {
        return this.getSimpleClassName() + "[" + this.origin.toString() + "]";
    }

    public static class WriterOrigin
    extends AbstractOrigin<Writer, WriterOrigin> {
        public WriterOrigin(Writer origin) {
            super(origin);
        }

        @Override
        public OutputStream getOutputStream(OpenOption ... options) throws IOException {
            return ((WriterOutputStream.Builder)WriterOutputStream.builder().setWriter((Writer)this.origin)).setCharset(Charset.defaultCharset()).get();
        }

        @Override
        public Writer getWriter(Charset charset, OpenOption ... options) throws IOException {
            return (Writer)this.get();
        }
    }

    public static class URIOrigin
    extends AbstractOrigin<URI, URIOrigin> {
        private static final String SCHEME_HTTPS = "https";
        private static final String SCHEME_HTTP = "http";

        public URIOrigin(URI origin) {
            super(origin);
        }

        @Override
        public File getFile() {
            return this.getPath().toFile();
        }

        @Override
        public InputStream getInputStream(OpenOption ... options) throws IOException {
            URI uri = (URI)this.get();
            String scheme = uri.getScheme();
            FileSystemProvider fileSystemProvider = FileSystemProviders.installed().getFileSystemProvider(scheme);
            if (fileSystemProvider != null) {
                return Files.newInputStream(fileSystemProvider.getPath(uri), options);
            }
            if (SCHEME_HTTP.equalsIgnoreCase(scheme) || SCHEME_HTTPS.equalsIgnoreCase(scheme)) {
                return uri.toURL().openStream();
            }
            return Files.newInputStream(this.getPath(), options);
        }

        @Override
        public Path getPath() {
            return Paths.get((URI)this.get());
        }
    }

    public static class ReaderOrigin
    extends AbstractOrigin<Reader, ReaderOrigin> {
        public ReaderOrigin(Reader origin) {
            super(origin);
        }

        @Override
        public byte[] getByteArray() throws IOException {
            return IOUtils.toByteArray((Reader)this.origin, Charset.defaultCharset());
        }

        @Override
        public CharSequence getCharSequence(Charset charset) throws IOException {
            return IOUtils.toString((Reader)this.origin);
        }

        @Override
        public InputStream getInputStream(OpenOption ... options) throws IOException {
            return ((ReaderInputStream.Builder)ReaderInputStream.builder().setReader((Reader)this.origin)).setCharset(Charset.defaultCharset()).get();
        }

        @Override
        public Reader getReader(Charset charset) throws IOException {
            return (Reader)this.get();
        }
    }

    public static class RandomAccessFileOrigin
    extends AbstractRandomAccessFileOrigin<RandomAccessFile, RandomAccessFileOrigin> {
        public RandomAccessFileOrigin(RandomAccessFile origin) {
            super(origin);
        }
    }

    public static class PathOrigin
    extends AbstractOrigin<Path, PathOrigin> {
        public PathOrigin(Path origin) {
            super(origin);
        }

        @Override
        public byte[] getByteArray(long position, int length) throws IOException {
            return RandomAccessFileMode.READ_ONLY.apply((Path)this.origin, raf -> RandomAccessFiles.read(raf, position, length));
        }

        @Override
        public File getFile() {
            return ((Path)this.get()).toFile();
        }

        @Override
        public Path getPath() {
            return (Path)this.get();
        }
    }

    public static class OutputStreamOrigin
    extends AbstractOrigin<OutputStream, OutputStreamOrigin> {
        public OutputStreamOrigin(OutputStream origin) {
            super(origin);
        }

        @Override
        public OutputStream getOutputStream(OpenOption ... options) {
            return (OutputStream)this.get();
        }

        @Override
        public Writer getWriter(Charset charset, OpenOption ... options) throws IOException {
            return new OutputStreamWriter((OutputStream)this.origin, charset);
        }
    }

    public static class IORandomAccessFileOrigin
    extends AbstractRandomAccessFileOrigin<IORandomAccessFile, IORandomAccessFileOrigin> {
        public IORandomAccessFileOrigin(IORandomAccessFile origin) {
            super(origin);
        }

        @Override
        public File getFile() {
            return ((IORandomAccessFile)this.get()).getFile();
        }

        @Override
        public Path getPath() {
            return this.getFile().toPath();
        }
    }

    public static class InputStreamOrigin
    extends AbstractOrigin<InputStream, InputStreamOrigin> {
        public InputStreamOrigin(InputStream origin) {
            super(origin);
        }

        @Override
        public byte[] getByteArray() throws IOException {
            return IOUtils.toByteArray((InputStream)this.origin);
        }

        @Override
        public InputStream getInputStream(OpenOption ... options) {
            return (InputStream)this.get();
        }

        @Override
        public Reader getReader(Charset charset) throws IOException {
            return new InputStreamReader(this.getInputStream(new OpenOption[0]), charset);
        }
    }

    public static class FileOrigin
    extends AbstractOrigin<File, FileOrigin> {
        public FileOrigin(File origin) {
            super(origin);
        }

        @Override
        public byte[] getByteArray(long position, int length) throws IOException {
            try (RandomAccessFile raf = RandomAccessFileMode.READ_ONLY.create((File)this.origin);){
                byte[] byArray = RandomAccessFiles.read(raf, position, length);
                return byArray;
            }
        }

        @Override
        public File getFile() {
            return (File)this.get();
        }

        @Override
        public Path getPath() {
            return ((File)this.get()).toPath();
        }
    }

    public static class CharSequenceOrigin
    extends AbstractOrigin<CharSequence, CharSequenceOrigin> {
        public CharSequenceOrigin(CharSequence origin) {
            super(origin);
        }

        @Override
        public byte[] getByteArray() {
            return ((CharSequence)this.origin).toString().getBytes(Charset.defaultCharset());
        }

        @Override
        public CharSequence getCharSequence(Charset charset) {
            return (CharSequence)this.get();
        }

        @Override
        public InputStream getInputStream(OpenOption ... options) throws IOException {
            return ((CharSequenceInputStream.Builder)CharSequenceInputStream.builder().setCharSequence(this.getCharSequence(Charset.defaultCharset()))).get();
        }

        @Override
        public Reader getReader(Charset charset) throws IOException {
            return new CharSequenceReader((CharSequence)this.get());
        }

        @Override
        public long size() throws IOException {
            return ((CharSequence)this.origin).length();
        }
    }

    public static class ByteArrayOrigin
    extends AbstractOrigin<byte[], ByteArrayOrigin> {
        public ByteArrayOrigin(byte[] origin) {
            super(origin);
        }

        @Override
        public byte[] getByteArray() {
            return (byte[])this.get();
        }

        @Override
        public InputStream getInputStream(OpenOption ... options) throws IOException {
            return new ByteArrayInputStream((byte[])this.origin);
        }

        @Override
        public Reader getReader(Charset charset) throws IOException {
            return new InputStreamReader(this.getInputStream(new OpenOption[0]), charset);
        }

        @Override
        public long size() throws IOException {
            return ((byte[])this.origin).length;
        }
    }

    public static abstract class AbstractRandomAccessFileOrigin<T extends RandomAccessFile, B extends AbstractRandomAccessFileOrigin<T, B>>
    extends AbstractOrigin<T, B> {
        public AbstractRandomAccessFileOrigin(T origin) {
            super(origin);
        }

        @Override
        public byte[] getByteArray() throws IOException {
            long longLen = ((RandomAccessFile)this.origin).length();
            if (longLen > Integer.MAX_VALUE) {
                throw new IllegalStateException("Origin too large.");
            }
            return RandomAccessFiles.read((RandomAccessFile)this.origin, 0L, (int)longLen);
        }

        @Override
        public byte[] getByteArray(long position, int length) throws IOException {
            return RandomAccessFiles.read((RandomAccessFile)this.origin, position, length);
        }

        @Override
        public CharSequence getCharSequence(Charset charset) throws IOException {
            return new String(this.getByteArray(), charset);
        }

        @Override
        public InputStream getInputStream(OpenOption ... options) throws IOException {
            return BufferedFileChannelInputStream.builder().setFileChannel(((RandomAccessFile)this.origin).getChannel()).get();
        }

        @Override
        public OutputStream getOutputStream(OpenOption ... options) throws IOException {
            return ((RandomAccessFileOutputStream.Builder)RandomAccessFileOutputStream.builder().setRandomAccessFile((RandomAccessFile)this.origin)).get();
        }

        public T getRandomAccessFile(OpenOption ... openOption) {
            return (T)((RandomAccessFile)this.get());
        }

        @Override
        public Reader getReader(Charset charset) throws IOException {
            return new InputStreamReader(this.getInputStream(new OpenOption[0]), charset);
        }

        @Override
        public Writer getWriter(Charset charset, OpenOption ... options) throws IOException {
            return new OutputStreamWriter(this.getOutputStream(options), charset);
        }

        @Override
        public long size() throws IOException {
            return ((RandomAccessFile)this.origin).length();
        }
    }
}

