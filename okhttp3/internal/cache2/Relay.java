/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.cache2;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.Util;
import okhttp3.internal.cache2.FileOperator;
import okio.Buffer;
import okio.ByteString;
import okio.Source;
import okio.Timeout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\t\u0018\u0000 :2\u00020\u0001:\u0002:;B3\b\u0002\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\u000bJ\u000e\u00102\u001a\u0002032\u0006\u00104\u001a\u00020\u0007J\u0006\u0010\b\u001a\u00020\tJ\b\u00105\u001a\u0004\u0018\u00010\u0005J \u00106\u001a\u0002032\u0006\u00107\u001a\u00020\t2\u0006\u00104\u001a\u00020\u00072\u0006\u00108\u001a\u00020\u0007H\u0002J\u0010\u00109\u001a\u0002032\u0006\u00104\u001a\u00020\u0007H\u0002R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\n\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u00020\u0013X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u0011\u0010\u001c\u001a\u00020\u00138F\u00a2\u0006\u0006\u001a\u0004\b\u001c\u0010\u0015R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001d\u001a\u00020\u001eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u0011\u0010'\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u000fR\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u0011\"\u0004\b*\u0010+R\u001c\u0010,\u001a\u0004\u0018\u00010-X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b.\u0010/\"\u0004\b0\u00101\u00a8\u0006<"}, d2={"Lokhttp3/internal/cache2/Relay;", "", "file", "Ljava/io/RandomAccessFile;", "upstream", "Lokio/Source;", "upstreamPos", "", "metadata", "Lokio/ByteString;", "bufferMaxSize", "(Ljava/io/RandomAccessFile;Lokio/Source;JLokio/ByteString;J)V", "buffer", "Lokio/Buffer;", "getBuffer", "()Lokio/Buffer;", "getBufferMaxSize", "()J", "complete", "", "getComplete", "()Z", "setComplete", "(Z)V", "getFile", "()Ljava/io/RandomAccessFile;", "setFile", "(Ljava/io/RandomAccessFile;)V", "isClosed", "sourceCount", "", "getSourceCount", "()I", "setSourceCount", "(I)V", "getUpstream", "()Lokio/Source;", "setUpstream", "(Lokio/Source;)V", "upstreamBuffer", "getUpstreamBuffer", "getUpstreamPos", "setUpstreamPos", "(J)V", "upstreamReader", "Ljava/lang/Thread;", "getUpstreamReader", "()Ljava/lang/Thread;", "setUpstreamReader", "(Ljava/lang/Thread;)V", "commit", "", "upstreamSize", "newSource", "writeHeader", "prefix", "metadataSize", "writeMetadata", "Companion", "RelaySource", "okhttp"})
public final class Relay {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @Nullable
    private RandomAccessFile file;
    @Nullable
    private Source upstream;
    private long upstreamPos;
    @NotNull
    private final ByteString metadata;
    private final long bufferMaxSize;
    @Nullable
    private Thread upstreamReader;
    @NotNull
    private final Buffer upstreamBuffer;
    private boolean complete;
    @NotNull
    private final Buffer buffer;
    private int sourceCount;
    private static final int SOURCE_UPSTREAM = 1;
    private static final int SOURCE_FILE = 2;
    @JvmField
    @NotNull
    public static final ByteString PREFIX_CLEAN = ByteString.Companion.encodeUtf8("OkHttp cache v1\n");
    @JvmField
    @NotNull
    public static final ByteString PREFIX_DIRTY = ByteString.Companion.encodeUtf8("OkHttp DIRTY :(\n");
    private static final long FILE_HEADER_SIZE = 32L;

    private Relay(RandomAccessFile file, Source upstream, long upstreamPos, ByteString metadata, long bufferMaxSize) {
        this.file = file;
        this.upstream = upstream;
        this.upstreamPos = upstreamPos;
        this.metadata = metadata;
        this.bufferMaxSize = bufferMaxSize;
        this.upstreamBuffer = new Buffer();
        this.complete = this.upstream == null;
        this.buffer = new Buffer();
    }

    @Nullable
    public final RandomAccessFile getFile() {
        return this.file;
    }

    public final void setFile(@Nullable RandomAccessFile randomAccessFile) {
        this.file = randomAccessFile;
    }

    @Nullable
    public final Source getUpstream() {
        return this.upstream;
    }

    public final void setUpstream(@Nullable Source source2) {
        this.upstream = source2;
    }

    public final long getUpstreamPos() {
        return this.upstreamPos;
    }

    public final void setUpstreamPos(long l2) {
        this.upstreamPos = l2;
    }

    public final long getBufferMaxSize() {
        return this.bufferMaxSize;
    }

    @Nullable
    public final Thread getUpstreamReader() {
        return this.upstreamReader;
    }

    public final void setUpstreamReader(@Nullable Thread thread2) {
        this.upstreamReader = thread2;
    }

    @NotNull
    public final Buffer getUpstreamBuffer() {
        return this.upstreamBuffer;
    }

    public final boolean getComplete() {
        return this.complete;
    }

    public final void setComplete(boolean bl) {
        this.complete = bl;
    }

    @NotNull
    public final Buffer getBuffer() {
        return this.buffer;
    }

    public final int getSourceCount() {
        return this.sourceCount;
    }

    public final void setSourceCount(int n2) {
        this.sourceCount = n2;
    }

    public final boolean isClosed() {
        return this.file == null;
    }

    private final void writeHeader(ByteString prefix, long upstreamSize, long metadataSize) throws IOException {
        Buffer buffer;
        Buffer $this$writeHeader_u24lambda_u2d0 = buffer = new Buffer();
        boolean bl = false;
        $this$writeHeader_u24lambda_u2d0.write(prefix);
        $this$writeHeader_u24lambda_u2d0.writeLong(upstreamSize);
        $this$writeHeader_u24lambda_u2d0.writeLong(metadataSize);
        if (!($this$writeHeader_u24lambda_u2d0.size() == 32L)) {
            String string = "Failed requirement.";
            throw new IllegalArgumentException(string.toString());
        }
        Buffer header = buffer;
        RandomAccessFile randomAccessFile = this.file;
        Intrinsics.checkNotNull(randomAccessFile);
        FileChannel fileChannel = randomAccessFile.getChannel();
        Intrinsics.checkNotNullExpressionValue(fileChannel, "file!!.channel");
        FileOperator fileOperator = new FileOperator(fileChannel);
        fileOperator.write(0L, header, 32L);
    }

    private final void writeMetadata(long upstreamSize) throws IOException {
        Buffer metadataBuffer = new Buffer();
        metadataBuffer.write(this.metadata);
        RandomAccessFile randomAccessFile = this.file;
        Intrinsics.checkNotNull(randomAccessFile);
        FileChannel fileChannel = randomAccessFile.getChannel();
        Intrinsics.checkNotNullExpressionValue(fileChannel, "file!!.channel");
        FileOperator fileOperator = new FileOperator(fileChannel);
        fileOperator.write(32L + upstreamSize, metadataBuffer, this.metadata.size());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final void commit(long upstreamSize) throws IOException {
        this.writeMetadata(upstreamSize);
        RandomAccessFile randomAccessFile = this.file;
        Intrinsics.checkNotNull(randomAccessFile);
        randomAccessFile.getChannel().force(false);
        this.writeHeader(PREFIX_CLEAN, upstreamSize, this.metadata.size());
        RandomAccessFile randomAccessFile2 = this.file;
        Intrinsics.checkNotNull(randomAccessFile2);
        randomAccessFile2.getChannel().force(false);
        Relay relay = this;
        synchronized (relay) {
            boolean bl = false;
            this.setComplete(true);
            Unit unit = Unit.INSTANCE;
        }
        Source source2 = this.upstream;
        if (source2 != null) {
            Util.closeQuietly(source2);
        }
        this.upstream = null;
    }

    @NotNull
    public final ByteString metadata() {
        return this.metadata;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Nullable
    public final Source newSource() {
        Relay relay = this;
        synchronized (relay) {
            block4: {
                boolean bl = false;
                if (this.getFile() != null) break block4;
                Source source2 = null;
                return source2;
            }
            int n2 = this.getSourceCount();
            this.setSourceCount(n2 + 1);
            int n3 = n2;
        }
        return new RelaySource();
    }

    public /* synthetic */ Relay(RandomAccessFile file, Source upstream, long upstreamPos, ByteString metadata, long bufferMaxSize, DefaultConstructorMarker $constructor_marker) {
        this(file, upstream, upstreamPos, metadata, bufferMaxSize);
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0080\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0006H\u0016J\b\u0010\u0007\u001a\u00020\bH\u0016R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2={"Lokhttp3/internal/cache2/Relay$RelaySource;", "Lokio/Source;", "(Lokhttp3/internal/cache2/Relay;)V", "fileOperator", "Lokhttp3/internal/cache2/FileOperator;", "sourcePos", "", "timeout", "Lokio/Timeout;", "close", "", "read", "sink", "Lokio/Buffer;", "byteCount", "okhttp"})
    public final class RelaySource
    implements Source {
        @NotNull
        private final Timeout timeout;
        @Nullable
        private FileOperator fileOperator;
        private long sourcePos;

        public RelaySource() {
            Intrinsics.checkNotNullParameter(Relay.this, "this$0");
            this.timeout = new Timeout();
            RandomAccessFile randomAccessFile = Relay.this.getFile();
            Intrinsics.checkNotNull(randomAccessFile);
            FileChannel fileChannel = randomAccessFile.getChannel();
            Intrinsics.checkNotNullExpressionValue(fileChannel, "file!!.channel");
            this.fileOperator = new FileOperator(fileChannel);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public long read(@NotNull Buffer sink2, long byteCount) throws IOException {
            Object object;
            long upstreamBytesRead;
            block21: {
                Relay relay;
                Relay relay2;
                long l2;
                int n2;
                Intrinsics.checkNotNullParameter(sink2, "sink");
                if (!(this.fileOperator != null)) {
                    String string = "Check failed.";
                    throw new IllegalStateException(string.toString());
                }
                Relay relay3 = Relay.this;
                Relay relay4 = Relay.this;
                Relay relay5 = relay3;
                synchronized (relay5) {
                    int n3;
                    block22: {
                        block20: {
                            boolean bl = false;
                            while (true) {
                                long upstreamPos;
                                if (this.sourcePos != (upstreamPos = relay4.getUpstreamPos())) {
                                    long bufferPos22 = relay4.getUpstreamPos() - relay4.getBuffer().size();
                                    if (this.sourcePos >= bufferPos22) {
                                        long l3 = relay4.getUpstreamPos() - this.sourcePos;
                                        long bytesToRead = Math.min(byteCount, l3);
                                        relay4.getBuffer().copyTo(sink2, this.sourcePos - bufferPos22, bytesToRead);
                                        this.sourcePos += bytesToRead;
                                        return bytesToRead;
                                    }
                                    break block20;
                                }
                                if (relay4.getComplete()) {
                                    return -1L;
                                }
                                if (relay4.getUpstreamReader() == null) break;
                                this.timeout.waitUntilNotified(relay4);
                            }
                            relay4.setUpstreamReader(Thread.currentThread());
                            n3 = 1;
                            break block22;
                        }
                        n3 = 2;
                    }
                    n2 = n3;
                }
                int source2 = n2;
                if (source2 == 2) {
                    long l4 = Relay.this.getUpstreamPos() - this.sourcePos;
                    long bytesToRead = Math.min(byteCount, l4);
                    FileOperator fileOperator = this.fileOperator;
                    Intrinsics.checkNotNull(fileOperator);
                    fileOperator.read(32L + this.sourcePos, sink2, bytesToRead);
                    this.sourcePos += bytesToRead;
                    return bytesToRead;
                }
                try {
                    Source source3 = Relay.this.getUpstream();
                    Intrinsics.checkNotNull(source3);
                    upstreamBytesRead = source3.read(Relay.this.getUpstreamBuffer(), Relay.this.getBufferMaxSize());
                    if (upstreamBytesRead != -1L) break block21;
                    Relay.this.commit(Relay.this.getUpstreamPos());
                    l2 = -1L;
                    Relay bufferPos22 = Relay.this;
                    relay2 = Relay.this;
                    relay = bufferPos22;
                }
                catch (Throwable throwable) {
                    relay4 = Relay.this;
                    relay5 = Relay.this;
                    Relay relay6 = relay4;
                    synchronized (relay6) {
                        boolean bl = false;
                        relay5.setUpstreamReader(null);
                        Relay $this$notifyAll$iv = relay5;
                        boolean $i$f$notifyAll = false;
                        ((Object)$this$notifyAll$iv).notifyAll();
                        Unit unit = Unit.INSTANCE;
                        throw throwable;
                    }
                }
                synchronized (relay) {
                    boolean $i$a$-synchronized-Relay$RelaySource$read$32 = false;
                    relay2.setUpstreamReader(null);
                    Relay $this$notifyAll$iv = relay2;
                    boolean $i$f$notifyAll = false;
                    ((Object)$this$notifyAll$iv).notifyAll();
                    Unit $i$a$-synchronized-Relay$RelaySource$read$32 = Unit.INSTANCE;
                    return l2;
                }
            }
            long bytesRead = Math.min(upstreamBytesRead, byteCount);
            Relay.this.getUpstreamBuffer().copyTo(sink2, 0L, bytesRead);
            this.sourcePos += bytesRead;
            FileOperator fileOperator = this.fileOperator;
            Intrinsics.checkNotNull(fileOperator);
            fileOperator.write(32L + Relay.this.getUpstreamPos(), Relay.this.getUpstreamBuffer().clone(), upstreamBytesRead);
            Relay bufferPos22 = Relay.this;
            Relay relay = Relay.this;
            Relay relay7 = bufferPos22;
            synchronized (relay7) {
                boolean bl = false;
                relay.getBuffer().write(relay.getUpstreamBuffer(), upstreamBytesRead);
                if (relay.getBuffer().size() > relay.getBufferMaxSize()) {
                    relay.getBuffer().skip(relay.getBuffer().size() - relay.getBufferMaxSize());
                }
                relay.setUpstreamPos(relay.getUpstreamPos() + upstreamBytesRead);
                object = Unit.INSTANCE;
            }
            long bufferPos22 = bytesRead;
            relay7 = Relay.this;
            object = Relay.this;
            Relay relay8 = relay7;
            synchronized (relay8) {
                boolean bl = false;
                ((Relay)object).setUpstreamReader(null);
                Object $this$notifyAll$iv = object;
                boolean $i$f$notifyAll = false;
                $this$notifyAll$iv.notifyAll();
                Unit unit = Unit.INSTANCE;
                return bufferPos22;
            }
        }

        @Override
        @NotNull
        public Timeout timeout() {
            return this.timeout;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void close() throws IOException {
            if (this.fileOperator == null) {
                return;
            }
            this.fileOperator = null;
            RandomAccessFile fileToClose = null;
            Relay relay = Relay.this;
            Relay relay2 = Relay.this;
            Relay relay3 = relay;
            synchronized (relay3) {
                boolean bl = false;
                int n2 = relay2.getSourceCount();
                relay2.setSourceCount(n2 + -1);
                if (relay2.getSourceCount() == 0) {
                    fileToClose = relay2.getFile();
                    relay2.setFile(null);
                }
                Unit unit = Unit.INSTANCE;
            }
            RandomAccessFile randomAccessFile = fileToClose;
            if (randomAccessFile != null) {
                Util.closeQuietly(randomAccessFile);
            }
        }
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J&\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004J\u000e\u0010\u0013\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2={"Lokhttp3/internal/cache2/Relay$Companion;", "", "()V", "FILE_HEADER_SIZE", "", "PREFIX_CLEAN", "Lokio/ByteString;", "PREFIX_DIRTY", "SOURCE_FILE", "", "SOURCE_UPSTREAM", "edit", "Lokhttp3/internal/cache2/Relay;", "file", "Ljava/io/File;", "upstream", "Lokio/Source;", "metadata", "bufferMaxSize", "read", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final Relay edit(@NotNull File file, @NotNull Source upstream, @NotNull ByteString metadata, long bufferMaxSize) throws IOException {
            Intrinsics.checkNotNullParameter(file, "file");
            Intrinsics.checkNotNullParameter(upstream, "upstream");
            Intrinsics.checkNotNullParameter(metadata, "metadata");
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            Relay result = new Relay(randomAccessFile, upstream, 0L, metadata, bufferMaxSize, null);
            randomAccessFile.setLength(0L);
            result.writeHeader(PREFIX_DIRTY, -1L, -1L);
            return result;
        }

        @NotNull
        public final Relay read(@NotNull File file) throws IOException {
            Intrinsics.checkNotNullParameter(file, "file");
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            FileChannel fileChannel = randomAccessFile.getChannel();
            Intrinsics.checkNotNullExpressionValue(fileChannel, "randomAccessFile.channel");
            FileOperator fileOperator = new FileOperator(fileChannel);
            Buffer header = new Buffer();
            fileOperator.read(0L, header, 32L);
            ByteString prefix = header.readByteString(PREFIX_CLEAN.size());
            if (!Intrinsics.areEqual(prefix, PREFIX_CLEAN)) {
                throw new IOException("unreadable cache file");
            }
            long upstreamSize = header.readLong();
            long metadataSize = header.readLong();
            Buffer metadataBuffer = new Buffer();
            fileOperator.read(32L + upstreamSize, metadataBuffer, metadataSize);
            ByteString metadata = metadataBuffer.readByteString();
            return new Relay(randomAccessFile, null, upstreamSize, metadata, 0L, null);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

