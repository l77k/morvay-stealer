/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.SourceDebugExtension
 */
package okio;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.-SegmentedByteString;
import okio.Buffer;
import okio.RealBufferedSink;
import okio.RealBufferedSource;
import okio.Segment;
import okio.SegmentPool;
import okio.Sink;
import okio.Source;
import okio.Timeout;
import okio._JvmPlatformKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\b&\u0018\u00002\u00060\u0001j\u0002`\u0002:\u0002/0B\u000f\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0004\b\u0005\u0010\u0006J&\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0018\u001a\u00020\u000bJ\u001e\u0010\u0012\u001a\u00020\u00142\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0018\u001a\u00020\u0014J\u0006\u0010\u001b\u001a\u00020\u0014J\u000e\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001b\u001a\u00020\u0014J&\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0018\u001a\u00020\u000bJ\u001e\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u001f\u001a\u00020\u001a2\u0006\u0010\u0018\u001a\u00020\u0014J\u0006\u0010 \u001a\u00020\u001dJ\u0010\u0010\u001f\u001a\u00020!2\b\b\u0002\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\"\u001a\u00020\u00142\u0006\u0010\u001f\u001a\u00020!J\u0016\u0010#\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020!2\u0006\u0010\"\u001a\u00020\u0014J\u0010\u0010\u0019\u001a\u00020$2\b\b\u0002\u0010\u0013\u001a\u00020\u0014J\u0006\u0010%\u001a\u00020$J\u000e\u0010\"\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020$J\u0016\u0010#\u001a\u00020\u001d2\u0006\u0010\u0019\u001a\u00020$2\u0006\u0010\"\u001a\u00020\u0014J\u0006\u0010&\u001a\u00020\u001dJ(\u0010'\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0018\u001a\u00020\u000bH$J(\u0010(\u001a\u00020\u001d2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0018\u001a\u00020\u000bH$J\b\u0010)\u001a\u00020\u001dH$J\u0010\u0010*\u001a\u00020\u001d2\u0006\u0010\u001b\u001a\u00020\u0014H$J\b\u0010+\u001a\u00020\u0014H$J\b\u0010,\u001a\u00020\u001dH$J \u0010-\u001a\u00020\u00142\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0018\u001a\u00020\u0014H\u0002J \u0010.\u001a\u00020\u001d2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u001f\u001a\u00020\u001a2\u0006\u0010\u0018\u001a\u00020\u0014H\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\u00060\rj\u0002`\u000e\u00a2\u0006\n\n\u0002\u0010\u0011\u001a\u0004\b\u000f\u0010\u0010\u00a8\u00061"}, d2={"Lokio/FileHandle;", "Ljava/io/Closeable;", "Lokio/Closeable;", "readWrite", "", "<init>", "(Z)V", "getReadWrite", "()Z", "closed", "openStreamCount", "", "lock", "Ljava/util/concurrent/locks/ReentrantLock;", "Lokio/Lock;", "getLock", "()Ljava/util/concurrent/locks/ReentrantLock;", "Ljava/util/concurrent/locks/ReentrantLock;", "read", "fileOffset", "", "array", "", "arrayOffset", "byteCount", "sink", "Lokio/Buffer;", "size", "resize", "", "write", "source", "flush", "Lokio/Source;", "position", "reposition", "Lokio/Sink;", "appendingSink", "close", "protectedRead", "protectedWrite", "protectedFlush", "protectedResize", "protectedSize", "protectedClose", "readNoCloseCheck", "writeNoCloseCheck", "FileHandleSink", "FileHandleSource", "okio"})
@SourceDebugExtension(value={"SMAP\nFileHandle.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FileHandle.kt\nokio/FileHandle\n+ 2 -JvmPlatform.kt\nokio/_JvmPlatformKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 RealBufferedSource.kt\nokio/RealBufferedSource\n+ 5 RealBufferedSink.kt\nokio/RealBufferedSink\n+ 6 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,444:1\n33#2:445\n33#2:447\n33#2:448\n33#2:449\n33#2:450\n33#2:451\n33#2:452\n33#2:453\n33#2:457\n33#2:459\n1#3:446\n63#4:454\n63#4:455\n63#4:456\n51#5:458\n85#6:460\n85#6:461\n*S KotlinDebug\n*F\n+ 1 FileHandle.kt\nokio/FileHandle\n*L\n69#1:445\n81#1:447\n92#1:448\n105#1:449\n119#1:450\n129#1:451\n139#1:452\n151#1:453\n221#1:457\n287#1:459\n169#1:454\n195#1:455\n202#1:456\n248#1:458\n345#1:460\n374#1:461\n*E\n"})
public abstract class FileHandle
implements Closeable {
    private final boolean readWrite;
    private boolean closed;
    private int openStreamCount;
    @NotNull
    private final ReentrantLock lock;

    public FileHandle(boolean readWrite) {
        this.readWrite = readWrite;
        this.lock = _JvmPlatformKt.newLock();
    }

    public final boolean getReadWrite() {
        return this.readWrite;
    }

    @NotNull
    public final ReentrantLock getLock() {
        return this.lock;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final int read(long fileOffset, @NotNull byte[] array, int arrayOffset, int byteCount) throws IOException {
        Intrinsics.checkNotNullParameter(array, "array");
        ReentrantLock $this$withLock$iv = this.lock;
        boolean $i$f$withLock = false;
        Lock lock = $this$withLock$iv;
        lock.lock();
        try {
            boolean bl = false;
            if (!(!this.closed)) {
                boolean bl2 = false;
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            Unit unit = Unit.INSTANCE;
        }
        finally {
            lock.unlock();
        }
        return this.protectedRead(fileOffset, array, arrayOffset, byteCount);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final long read(long fileOffset, @NotNull Buffer sink2, long byteCount) throws IOException {
        Intrinsics.checkNotNullParameter(sink2, "sink");
        ReentrantLock $this$withLock$iv = this.lock;
        boolean $i$f$withLock = false;
        Lock lock = $this$withLock$iv;
        lock.lock();
        try {
            boolean bl = false;
            if (!(!this.closed)) {
                boolean bl2 = false;
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            Unit unit = Unit.INSTANCE;
        }
        finally {
            lock.unlock();
        }
        return this.readNoCloseCheck(fileOffset, sink2, byteCount);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final long size() throws IOException {
        ReentrantLock $this$withLock$iv = this.lock;
        boolean $i$f$withLock = false;
        Lock lock = $this$withLock$iv;
        lock.lock();
        try {
            boolean bl = false;
            if (!(!this.closed)) {
                boolean bl2 = false;
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            Unit unit = Unit.INSTANCE;
        }
        finally {
            lock.unlock();
        }
        return this.protectedSize();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final void resize(long size) throws IOException {
        if (!this.readWrite) {
            boolean $i$a$-check-FileHandle$resize$22 = false;
            String $i$a$-check-FileHandle$resize$22 = "file handle is read-only";
            throw new IllegalStateException($i$a$-check-FileHandle$resize$22.toString());
        }
        ReentrantLock $this$withLock$iv = this.lock;
        boolean $i$f$withLock = false;
        Lock lock = $this$withLock$iv;
        lock.lock();
        try {
            boolean bl = false;
            if (!(!this.closed)) {
                boolean bl2 = false;
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            Unit unit = Unit.INSTANCE;
        }
        finally {
            lock.unlock();
        }
        this.protectedResize(size);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final void write(long fileOffset, @NotNull byte[] array, int arrayOffset, int byteCount) {
        Intrinsics.checkNotNullParameter(array, "array");
        if (!this.readWrite) {
            boolean $i$a$-check-FileHandle$write$22 = false;
            String $i$a$-check-FileHandle$write$22 = "file handle is read-only";
            throw new IllegalStateException($i$a$-check-FileHandle$write$22.toString());
        }
        ReentrantLock $this$withLock$iv = this.lock;
        boolean $i$f$withLock = false;
        Lock lock = $this$withLock$iv;
        lock.lock();
        try {
            boolean bl = false;
            if (!(!this.closed)) {
                boolean bl2 = false;
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            Unit unit = Unit.INSTANCE;
        }
        finally {
            lock.unlock();
        }
        this.protectedWrite(fileOffset, array, arrayOffset, byteCount);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final void write(long fileOffset, @NotNull Buffer source2, long byteCount) throws IOException {
        Intrinsics.checkNotNullParameter(source2, "source");
        if (!this.readWrite) {
            boolean $i$a$-check-FileHandle$write$42 = false;
            String $i$a$-check-FileHandle$write$42 = "file handle is read-only";
            throw new IllegalStateException($i$a$-check-FileHandle$write$42.toString());
        }
        ReentrantLock $this$withLock$iv = this.lock;
        boolean $i$f$withLock = false;
        Lock lock = $this$withLock$iv;
        lock.lock();
        try {
            boolean bl = false;
            if (!(!this.closed)) {
                boolean bl2 = false;
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            Unit unit = Unit.INSTANCE;
        }
        finally {
            lock.unlock();
        }
        this.writeNoCloseCheck(fileOffset, source2, byteCount);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final void flush() throws IOException {
        if (!this.readWrite) {
            boolean $i$a$-check-FileHandle$flush$22 = false;
            String $i$a$-check-FileHandle$flush$22 = "file handle is read-only";
            throw new IllegalStateException($i$a$-check-FileHandle$flush$22.toString());
        }
        ReentrantLock $this$withLock$iv = this.lock;
        boolean $i$f$withLock = false;
        Lock lock = $this$withLock$iv;
        lock.lock();
        try {
            boolean bl = false;
            if (!(!this.closed)) {
                boolean bl2 = false;
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            Unit unit = Unit.INSTANCE;
        }
        finally {
            lock.unlock();
        }
        this.protectedFlush();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @NotNull
    public final Source source(long fileOffset) throws IOException {
        ReentrantLock $this$withLock$iv = this.lock;
        boolean $i$f$withLock = false;
        Lock lock = $this$withLock$iv;
        lock.lock();
        try {
            boolean bl = false;
            if (!(!this.closed)) {
                boolean bl2 = false;
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            int n2 = this.openStreamCount;
            this.openStreamCount = n2 + 1;
            int n3 = n2;
        }
        finally {
            lock.unlock();
        }
        return new FileHandleSource(this, fileOffset);
    }

    public static /* synthetic */ Source source$default(FileHandle fileHandle, long l2, int n2, Object object) throws IOException {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: source");
        }
        if ((n2 & 1) != 0) {
            l2 = 0L;
        }
        return fileHandle.source(l2);
    }

    public final long position(@NotNull Source source2) throws IOException {
        Intrinsics.checkNotNullParameter(source2, "source");
        Source source3 = source2;
        long bufferSize = 0L;
        if (source3 instanceof RealBufferedSource) {
            RealBufferedSource this_$iv = (RealBufferedSource)source3;
            boolean $i$f$getBuffer = false;
            bufferSize = this_$iv.bufferField.size();
            source3 = ((RealBufferedSource)source3).source;
        }
        if (!(source3 instanceof FileHandleSource && ((FileHandleSource)source3).getFileHandle() == this)) {
            boolean $i$a$-require-FileHandle$position$22 = false;
            String $i$a$-require-FileHandle$position$22 = "source was not created by this FileHandle";
            throw new IllegalArgumentException($i$a$-require-FileHandle$position$22.toString());
        }
        if (!(!((FileHandleSource)source3).getClosed())) {
            boolean bl = false;
            String string = "closed";
            throw new IllegalStateException(string.toString());
        }
        return ((FileHandleSource)source3).getPosition() - bufferSize;
    }

    public final void reposition(@NotNull Source source2, long position) throws IOException {
        Intrinsics.checkNotNullParameter(source2, "source");
        if (source2 instanceof RealBufferedSource) {
            Source fileHandleSource = ((RealBufferedSource)source2).source;
            if (!(fileHandleSource instanceof FileHandleSource && ((FileHandleSource)fileHandleSource).getFileHandle() == this)) {
                boolean $i$a$-require-FileHandle$reposition$22 = false;
                String $i$a$-require-FileHandle$reposition$22 = "source was not created by this FileHandle";
                throw new IllegalArgumentException($i$a$-require-FileHandle$reposition$22.toString());
            }
            if (!(!((FileHandleSource)fileHandleSource).getClosed())) {
                boolean bl = false;
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            RealBufferedSource this_$iv = (RealBufferedSource)source2;
            boolean $i$f$getBuffer = false;
            long bufferSize = this_$iv.bufferField.size();
            long toSkip = position - (((FileHandleSource)fileHandleSource).getPosition() - bufferSize);
            boolean bl = 0L <= toSkip ? toSkip < bufferSize : false;
            if (bl) {
                ((RealBufferedSource)source2).skip(toSkip);
            } else {
                RealBufferedSource this_$iv2 = (RealBufferedSource)source2;
                boolean $i$f$getBuffer2 = false;
                this_$iv2.bufferField.clear();
                ((FileHandleSource)fileHandleSource).setPosition(position);
            }
        } else {
            if (!(source2 instanceof FileHandleSource && ((FileHandleSource)source2).getFileHandle() == this)) {
                boolean $i$a$-require-FileHandle$reposition$42 = false;
                String $i$a$-require-FileHandle$reposition$42 = "source was not created by this FileHandle";
                throw new IllegalArgumentException($i$a$-require-FileHandle$reposition$42.toString());
            }
            if (!(!((FileHandleSource)source2).getClosed())) {
                boolean bl = false;
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            ((FileHandleSource)source2).setPosition(position);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @NotNull
    public final Sink sink(long fileOffset) throws IOException {
        if (!this.readWrite) {
            boolean $i$a$-check-FileHandle$sink$22 = false;
            String $i$a$-check-FileHandle$sink$22 = "file handle is read-only";
            throw new IllegalStateException($i$a$-check-FileHandle$sink$22.toString());
        }
        ReentrantLock $this$withLock$iv = this.lock;
        boolean $i$f$withLock = false;
        Lock lock = $this$withLock$iv;
        lock.lock();
        try {
            boolean bl = false;
            if (!(!this.closed)) {
                boolean bl2 = false;
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            int n2 = this.openStreamCount;
            this.openStreamCount = n2 + 1;
            int n3 = n2;
        }
        finally {
            lock.unlock();
        }
        return new FileHandleSink(this, fileOffset);
    }

    public static /* synthetic */ Sink sink$default(FileHandle fileHandle, long l2, int n2, Object object) throws IOException {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: sink");
        }
        if ((n2 & 1) != 0) {
            l2 = 0L;
        }
        return fileHandle.sink(l2);
    }

    @NotNull
    public final Sink appendingSink() throws IOException {
        return this.sink(this.size());
    }

    public final long position(@NotNull Sink sink2) throws IOException {
        Intrinsics.checkNotNullParameter(sink2, "sink");
        Sink sink3 = sink2;
        long bufferSize = 0L;
        if (sink3 instanceof RealBufferedSink) {
            RealBufferedSink this_$iv = (RealBufferedSink)sink3;
            boolean $i$f$getBuffer = false;
            bufferSize = this_$iv.bufferField.size();
            sink3 = ((RealBufferedSink)sink3).sink;
        }
        if (!(sink3 instanceof FileHandleSink && ((FileHandleSink)sink3).getFileHandle() == this)) {
            boolean $i$a$-require-FileHandle$position$42 = false;
            String $i$a$-require-FileHandle$position$42 = "sink was not created by this FileHandle";
            throw new IllegalArgumentException($i$a$-require-FileHandle$position$42.toString());
        }
        if (!(!((FileHandleSink)sink3).getClosed())) {
            boolean bl = false;
            String string = "closed";
            throw new IllegalStateException(string.toString());
        }
        return ((FileHandleSink)sink3).getPosition() + bufferSize;
    }

    public final void reposition(@NotNull Sink sink2, long position) throws IOException {
        Intrinsics.checkNotNullParameter(sink2, "sink");
        if (sink2 instanceof RealBufferedSink) {
            Sink fileHandleSink = ((RealBufferedSink)sink2).sink;
            if (!(fileHandleSink instanceof FileHandleSink && ((FileHandleSink)fileHandleSink).getFileHandle() == this)) {
                boolean $i$a$-require-FileHandle$reposition$62 = false;
                String $i$a$-require-FileHandle$reposition$62 = "sink was not created by this FileHandle";
                throw new IllegalArgumentException($i$a$-require-FileHandle$reposition$62.toString());
            }
            if (!(!((FileHandleSink)fileHandleSink).getClosed())) {
                boolean bl = false;
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            ((RealBufferedSink)sink2).emit();
            ((FileHandleSink)fileHandleSink).setPosition(position);
        } else {
            if (!(sink2 instanceof FileHandleSink && ((FileHandleSink)sink2).getFileHandle() == this)) {
                boolean $i$a$-require-FileHandle$reposition$82 = false;
                String $i$a$-require-FileHandle$reposition$82 = "sink was not created by this FileHandle";
                throw new IllegalArgumentException($i$a$-require-FileHandle$reposition$82.toString());
            }
            if (!(!((FileHandleSink)sink2).getClosed())) {
                boolean bl = false;
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            ((FileHandleSink)sink2).setPosition(position);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public final void close() throws IOException {
        ReentrantLock $this$withLock$iv = this.lock;
        boolean $i$f$withLock = false;
        Lock lock = $this$withLock$iv;
        lock.lock();
        try {
            boolean bl = false;
            if (this.closed) {
                return;
            }
            this.closed = true;
            if (this.openStreamCount != 0) {
                return;
            }
            Unit unit = Unit.INSTANCE;
        }
        finally {
            lock.unlock();
        }
        this.protectedClose();
    }

    protected abstract int protectedRead(long var1, @NotNull byte[] var3, int var4, int var5) throws IOException;

    protected abstract void protectedWrite(long var1, @NotNull byte[] var3, int var4, int var5) throws IOException;

    protected abstract void protectedFlush() throws IOException;

    protected abstract void protectedResize(long var1) throws IOException;

    protected abstract long protectedSize() throws IOException;

    protected abstract void protectedClose() throws IOException;

    /*
     * WARNING - void declaration
     */
    private final long readNoCloseCheck(long fileOffset, Buffer sink2, long byteCount) {
        long currentOffset;
        int readByteCount;
        if (!(byteCount >= 0L)) {
            boolean bl = false;
            String string = "byteCount < 0: " + byteCount;
            throw new IllegalArgumentException(string.toString());
        }
        long targetOffset = fileOffset + byteCount;
        for (currentOffset = fileOffset; currentOffset < targetOffset; currentOffset += (long)readByteCount) {
            void a$iv;
            Segment tail = sink2.writableSegment$okio(1);
            long l2 = targetOffset - currentOffset;
            int b$iv = 8192 - tail.limit;
            boolean $i$f$minOf = false;
            readByteCount = this.protectedRead(currentOffset, tail.data, tail.limit, (int)Math.min((long)a$iv, (long)b$iv));
            if (readByteCount == -1) {
                if (tail.pos == tail.limit) {
                    sink2.head = tail.pop();
                    SegmentPool.recycle(tail);
                }
                if (fileOffset != currentOffset) break;
                return -1L;
            }
            tail.limit += readByteCount;
            sink2.setSize$okio(sink2.size() + (long)readByteCount);
        }
        return currentOffset - fileOffset;
    }

    /*
     * WARNING - void declaration
     */
    private final void writeNoCloseCheck(long fileOffset, Buffer source2, long byteCount) {
        int toCopy;
        -SegmentedByteString.checkOffsetAndCount(source2.size(), 0L, byteCount);
        long targetOffset = fileOffset + byteCount;
        for (long currentOffset = fileOffset; currentOffset < targetOffset; currentOffset += (long)toCopy) {
            void a$iv;
            Segment head;
            Intrinsics.checkNotNull(source2.head);
            long l2 = targetOffset - currentOffset;
            int b$iv = head.limit - head.pos;
            boolean $i$f$minOf = false;
            toCopy = (int)Math.min((long)a$iv, (long)b$iv);
            this.protectedWrite(currentOffset, head.data, head.pos, toCopy);
            head.pos += toCopy;
            source2.setSize$okio(source2.size() - (long)toCopy);
            if (head.pos != head.limit) continue;
            source2.head = head.pop();
            SegmentPool.recycle(head);
        }
    }

    @Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0004\b\u0006\u0010\u0007J\u0018\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0005H\u0016J\b\u0010\u0019\u001a\u00020\u0015H\u0016J\b\u0010\u001a\u001a\u00020\u001bH\u0016J\b\u0010\u001c\u001a\u00020\u0015H\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001a\u0010\u000e\u001a\u00020\u000fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013\u00a8\u0006\u001d"}, d2={"Lokio/FileHandle$FileHandleSink;", "Lokio/Sink;", "fileHandle", "Lokio/FileHandle;", "position", "", "<init>", "(Lokio/FileHandle;J)V", "getFileHandle", "()Lokio/FileHandle;", "getPosition", "()J", "setPosition", "(J)V", "closed", "", "getClosed", "()Z", "setClosed", "(Z)V", "write", "", "source", "Lokio/Buffer;", "byteCount", "flush", "timeout", "Lokio/Timeout;", "close", "okio"})
    @SourceDebugExtension(value={"SMAP\nFileHandle.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FileHandle.kt\nokio/FileHandle$FileHandleSink\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 -JvmPlatform.kt\nokio/_JvmPlatformKt\n*L\n1#1,444:1\n1#2:445\n33#3:446\n*S KotlinDebug\n*F\n+ 1 FileHandle.kt\nokio/FileHandle$FileHandleSink\n*L\n410#1:446\n*E\n"})
    private static final class FileHandleSink
    implements Sink {
        @NotNull
        private final FileHandle fileHandle;
        private long position;
        private boolean closed;

        public FileHandleSink(@NotNull FileHandle fileHandle, long position) {
            Intrinsics.checkNotNullParameter(fileHandle, "fileHandle");
            this.fileHandle = fileHandle;
            this.position = position;
        }

        @NotNull
        public final FileHandle getFileHandle() {
            return this.fileHandle;
        }

        public final long getPosition() {
            return this.position;
        }

        public final void setPosition(long l2) {
            this.position = l2;
        }

        public final boolean getClosed() {
            return this.closed;
        }

        public final void setClosed(boolean bl) {
            this.closed = bl;
        }

        @Override
        public void write(@NotNull Buffer source2, long byteCount) {
            Intrinsics.checkNotNullParameter(source2, "source");
            if (!(!this.closed)) {
                boolean bl = false;
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            this.fileHandle.writeNoCloseCheck(this.position, source2, byteCount);
            this.position += byteCount;
        }

        @Override
        public void flush() {
            if (!(!this.closed)) {
                boolean bl = false;
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            this.fileHandle.protectedFlush();
        }

        @Override
        @NotNull
        public Timeout timeout() {
            return Timeout.NONE;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void close() {
            if (this.closed) {
                return;
            }
            this.closed = true;
            ReentrantLock $this$withLock$iv = this.fileHandle.getLock();
            boolean $i$f$withLock = false;
            Lock lock = $this$withLock$iv;
            lock.lock();
            try {
                boolean bl = false;
                FileHandle fileHandle = this.fileHandle;
                int n2 = fileHandle.openStreamCount;
                fileHandle.openStreamCount = n2 + -1;
                if (this.fileHandle.openStreamCount != 0 || !this.fileHandle.closed) {
                    return;
                }
                Unit unit = Unit.INSTANCE;
            }
            finally {
                lock.unlock();
            }
            this.fileHandle.protectedClose();
        }
    }

    @Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0004\b\u0006\u0010\u0007J\u0018\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0005H\u0016J\b\u0010\u0018\u001a\u00020\u0019H\u0016J\b\u0010\u001a\u001a\u00020\u001bH\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001a\u0010\u000e\u001a\u00020\u000fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013\u00a8\u0006\u001c"}, d2={"Lokio/FileHandle$FileHandleSource;", "Lokio/Source;", "fileHandle", "Lokio/FileHandle;", "position", "", "<init>", "(Lokio/FileHandle;J)V", "getFileHandle", "()Lokio/FileHandle;", "getPosition", "()J", "setPosition", "(J)V", "closed", "", "getClosed", "()Z", "setClosed", "(Z)V", "read", "sink", "Lokio/Buffer;", "byteCount", "timeout", "Lokio/Timeout;", "close", "", "okio"})
    @SourceDebugExtension(value={"SMAP\nFileHandle.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FileHandle.kt\nokio/FileHandle$FileHandleSource\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 -JvmPlatform.kt\nokio/_JvmPlatformKt\n*L\n1#1,444:1\n1#2:445\n33#3:446\n*S KotlinDebug\n*F\n+ 1 FileHandle.kt\nokio/FileHandle$FileHandleSource\n*L\n436#1:446\n*E\n"})
    private static final class FileHandleSource
    implements Source {
        @NotNull
        private final FileHandle fileHandle;
        private long position;
        private boolean closed;

        public FileHandleSource(@NotNull FileHandle fileHandle, long position) {
            Intrinsics.checkNotNullParameter(fileHandle, "fileHandle");
            this.fileHandle = fileHandle;
            this.position = position;
        }

        @NotNull
        public final FileHandle getFileHandle() {
            return this.fileHandle;
        }

        public final long getPosition() {
            return this.position;
        }

        public final void setPosition(long l2) {
            this.position = l2;
        }

        public final boolean getClosed() {
            return this.closed;
        }

        public final void setClosed(boolean bl) {
            this.closed = bl;
        }

        @Override
        public long read(@NotNull Buffer sink2, long byteCount) {
            Intrinsics.checkNotNullParameter(sink2, "sink");
            if (!(!this.closed)) {
                boolean bl = false;
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            long result = this.fileHandle.readNoCloseCheck(this.position, sink2, byteCount);
            if (result != -1L) {
                this.position += result;
            }
            return result;
        }

        @Override
        @NotNull
        public Timeout timeout() {
            return Timeout.NONE;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void close() {
            if (this.closed) {
                return;
            }
            this.closed = true;
            ReentrantLock $this$withLock$iv = this.fileHandle.getLock();
            boolean $i$f$withLock = false;
            Lock lock = $this$withLock$iv;
            lock.lock();
            try {
                boolean bl = false;
                FileHandle fileHandle = this.fileHandle;
                int n2 = fileHandle.openStreamCount;
                fileHandle.openStreamCount = n2 + -1;
                if (this.fileHandle.openStreamCount != 0 || !this.fileHandle.closed) {
                    return;
                }
                Unit unit = Unit.INSTANCE;
            }
            finally {
                lock.unlock();
            }
            this.fileHandle.protectedClose();
        }
    }
}

