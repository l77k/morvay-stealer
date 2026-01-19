/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import kotlin.Metadata;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okio.CipherSink;
import okio.CipherSource;
import okio.FileSystem;
import okio.HashingSink;
import okio.HashingSource;
import okio.InputStreamSource;
import okio.Okio;
import okio.OutputStreamSink;
import okio.Sink;
import okio.SocketAsyncTimeout;
import okio.Source;
import okio.Timeout;
import okio.internal.ResourceFileSystem;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={2, 1, 0}, k=5, xi=48, d1={"\u0000\u0080\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0002\u001a\n\u0010\u0003\u001a\u00020\u0004*\u00020\u0005\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0006\u001a\n\u0010\u0003\u001a\u00020\u0004*\u00020\u0006\u001a\u0016\u0010\u0000\u001a\u00020\u0001*\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\rH\u0007\u001a\n\u0010\u000e\u001a\u00020\u0001*\u00020\u000b\u001a\n\u0010\u0003\u001a\u00020\u0004*\u00020\u000b\u001a#\u0010\u0000\u001a\u00020\u0001*\u00020\u000f2\u0012\u0010\u0010\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00120\u0011\"\u00020\u0012\u00a2\u0006\u0002\u0010\u0013\u001a#\u0010\u0003\u001a\u00020\u0004*\u00020\u000f2\u0012\u0010\u0010\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00120\u0011\"\u00020\u0012\u00a2\u0006\u0002\u0010\u0014\u001a\u0012\u0010\u0015\u001a\u00020\u0016*\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0018\u001a\u0012\u0010\u0019\u001a\u00020\u001a*\u00020\u00042\u0006\u0010\u0017\u001a\u00020\u0018\u001a\u0012\u0010\u001b\u001a\u00020\u001c*\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u001e\u001a\u0012\u0010\u001f\u001a\u00020 *\u00020\u00042\u0006\u0010\u001d\u001a\u00020\u001e\u001a\u0012\u0010\u001b\u001a\u00020\u001c*\u00020\u00012\u0006\u0010!\u001a\u00020\"\u001a\u0012\u0010\u001f\u001a\u00020 *\u00020\u00042\u0006\u0010!\u001a\u00020\"\u001a\n\u0010#\u001a\u00020$*\u00020%\"\u0018\u0010\u0007\u001a\n \t*\u0004\u0018\u00010\b0\bX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\n\"\u001c\u0010&\u001a\u00020\r*\u00060'j\u0002`(8@X\u0080\u0004\u00a2\u0006\u0006\u001a\u0004\b&\u0010)\u00a8\u0006*"}, d2={"sink", "Lokio/Sink;", "Ljava/io/OutputStream;", "source", "Lokio/Source;", "Ljava/io/InputStream;", "Ljava/net/Socket;", "logger", "Ljava/util/logging/Logger;", "kotlin.jvm.PlatformType", "Ljava/util/logging/Logger;", "Ljava/io/File;", "append", "", "appendingSink", "Ljava/nio/file/Path;", "options", "", "Ljava/nio/file/OpenOption;", "(Ljava/nio/file/Path;[Ljava/nio/file/OpenOption;)Lokio/Sink;", "(Ljava/nio/file/Path;[Ljava/nio/file/OpenOption;)Lokio/Source;", "cipherSink", "Lokio/CipherSink;", "cipher", "Ljavax/crypto/Cipher;", "cipherSource", "Lokio/CipherSource;", "hashingSink", "Lokio/HashingSink;", "mac", "Ljavax/crypto/Mac;", "hashingSource", "Lokio/HashingSource;", "digest", "Ljava/security/MessageDigest;", "asResourceFileSystem", "Lokio/FileSystem;", "Ljava/lang/ClassLoader;", "isAndroidGetsocknameError", "Ljava/lang/AssertionError;", "Lkotlin/AssertionError;", "(Ljava/lang/AssertionError;)Z", "okio"}, xs="okio/Okio")
final class Okio__JvmOkioKt {
    private static final Logger logger = Logger.getLogger("okio.Okio");

    @NotNull
    public static final Sink sink(@NotNull OutputStream $this$sink) {
        Intrinsics.checkNotNullParameter($this$sink, "<this>");
        return new OutputStreamSink($this$sink, new Timeout());
    }

    @NotNull
    public static final Source source(@NotNull InputStream $this$source) {
        Intrinsics.checkNotNullParameter($this$source, "<this>");
        return new InputStreamSource($this$source, new Timeout());
    }

    @NotNull
    public static final Sink sink(@NotNull Socket $this$sink) throws IOException {
        Intrinsics.checkNotNullParameter($this$sink, "<this>");
        SocketAsyncTimeout timeout2 = new SocketAsyncTimeout($this$sink);
        OutputStream outputStream2 = $this$sink.getOutputStream();
        Intrinsics.checkNotNullExpressionValue(outputStream2, "getOutputStream(...)");
        OutputStreamSink sink2 = new OutputStreamSink(outputStream2, timeout2);
        return timeout2.sink(sink2);
    }

    @NotNull
    public static final Source source(@NotNull Socket $this$source) throws IOException {
        Intrinsics.checkNotNullParameter($this$source, "<this>");
        SocketAsyncTimeout timeout2 = new SocketAsyncTimeout($this$source);
        InputStream inputStream2 = $this$source.getInputStream();
        Intrinsics.checkNotNullExpressionValue(inputStream2, "getInputStream(...)");
        InputStreamSource source2 = new InputStreamSource(inputStream2, timeout2);
        return timeout2.source(source2);
    }

    @JvmOverloads
    @NotNull
    public static final Sink sink(@NotNull File $this$sink, boolean append) throws FileNotFoundException {
        Intrinsics.checkNotNullParameter($this$sink, "<this>");
        return Okio.sink(new FileOutputStream($this$sink, append));
    }

    public static /* synthetic */ Sink sink$default(File file, boolean bl, int n2, Object object) throws FileNotFoundException {
        if ((n2 & 1) != 0) {
            bl = false;
        }
        return Okio.sink(file, bl);
    }

    @NotNull
    public static final Sink appendingSink(@NotNull File $this$appendingSink) throws FileNotFoundException {
        Intrinsics.checkNotNullParameter($this$appendingSink, "<this>");
        return Okio.sink(new FileOutputStream($this$appendingSink, true));
    }

    @NotNull
    public static final Source source(@NotNull File $this$source) throws FileNotFoundException {
        Intrinsics.checkNotNullParameter($this$source, "<this>");
        return new InputStreamSource(new FileInputStream($this$source), Timeout.NONE);
    }

    @NotNull
    public static final Sink sink(@NotNull Path $this$sink, OpenOption ... options) throws IOException {
        Intrinsics.checkNotNullParameter($this$sink, "<this>");
        Intrinsics.checkNotNullParameter(options, "options");
        OutputStream outputStream2 = Files.newOutputStream($this$sink, Arrays.copyOf(options, options.length));
        Intrinsics.checkNotNullExpressionValue(outputStream2, "newOutputStream(...)");
        return Okio.sink(outputStream2);
    }

    @NotNull
    public static final Source source(@NotNull Path $this$source, OpenOption ... options) throws IOException {
        Intrinsics.checkNotNullParameter($this$source, "<this>");
        Intrinsics.checkNotNullParameter(options, "options");
        InputStream inputStream2 = Files.newInputStream($this$source, Arrays.copyOf(options, options.length));
        Intrinsics.checkNotNullExpressionValue(inputStream2, "newInputStream(...)");
        return Okio.source(inputStream2);
    }

    @NotNull
    public static final CipherSink cipherSink(@NotNull Sink $this$cipherSink, @NotNull Cipher cipher) {
        Intrinsics.checkNotNullParameter($this$cipherSink, "<this>");
        Intrinsics.checkNotNullParameter(cipher, "cipher");
        return new CipherSink(Okio.buffer($this$cipherSink), cipher);
    }

    @NotNull
    public static final CipherSource cipherSource(@NotNull Source $this$cipherSource, @NotNull Cipher cipher) {
        Intrinsics.checkNotNullParameter($this$cipherSource, "<this>");
        Intrinsics.checkNotNullParameter(cipher, "cipher");
        return new CipherSource(Okio.buffer($this$cipherSource), cipher);
    }

    @NotNull
    public static final HashingSink hashingSink(@NotNull Sink $this$hashingSink, @NotNull Mac mac) {
        Intrinsics.checkNotNullParameter($this$hashingSink, "<this>");
        Intrinsics.checkNotNullParameter(mac, "mac");
        return new HashingSink($this$hashingSink, mac);
    }

    @NotNull
    public static final HashingSource hashingSource(@NotNull Source $this$hashingSource, @NotNull Mac mac) {
        Intrinsics.checkNotNullParameter($this$hashingSource, "<this>");
        Intrinsics.checkNotNullParameter(mac, "mac");
        return new HashingSource($this$hashingSource, mac);
    }

    @NotNull
    public static final HashingSink hashingSink(@NotNull Sink $this$hashingSink, @NotNull MessageDigest digest) {
        Intrinsics.checkNotNullParameter($this$hashingSink, "<this>");
        Intrinsics.checkNotNullParameter(digest, "digest");
        return new HashingSink($this$hashingSink, digest);
    }

    @NotNull
    public static final HashingSource hashingSource(@NotNull Source $this$hashingSource, @NotNull MessageDigest digest) {
        Intrinsics.checkNotNullParameter($this$hashingSource, "<this>");
        Intrinsics.checkNotNullParameter(digest, "digest");
        return new HashingSource($this$hashingSource, digest);
    }

    @NotNull
    public static final FileSystem asResourceFileSystem(@NotNull ClassLoader $this$asResourceFileSystem) {
        Intrinsics.checkNotNullParameter($this$asResourceFileSystem, "<this>");
        return new ResourceFileSystem($this$asResourceFileSystem, true, null, 4, null);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final boolean isAndroidGetsocknameError(@NotNull AssertionError $this$isAndroidGetsocknameError) {
        Intrinsics.checkNotNullParameter($this$isAndroidGetsocknameError, "<this>");
        if (((Throwable)((Object)$this$isAndroidGetsocknameError)).getCause() == null) return false;
        String string = ((Throwable)((Object)$this$isAndroidGetsocknameError)).getMessage();
        if (string == null) return false;
        boolean bl = StringsKt.contains$default((CharSequence)string, "getsockname failed", false, 2, null);
        if (!bl) return false;
        return true;
    }

    @JvmOverloads
    @NotNull
    public static final Sink sink(@NotNull File $this$sink) throws FileNotFoundException {
        Intrinsics.checkNotNullParameter($this$sink, "<this>");
        return Okio.sink$default($this$sink, false, 1, null);
    }

    public static final /* synthetic */ Logger access$getLogger$p() {
        return logger;
    }
}

