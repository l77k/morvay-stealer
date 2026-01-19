/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.Intrinsics;
import okio.Buffer;
import okio.ForwardingSink;
import okio.ForwardingSource;
import okio.Sink;
import okio.Source;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0011\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005B\t\b\u0016\u00a2\u0006\u0004\b\u0004\u0010\u0006J$\u0010\u0007\u001a\u00020\u00122\u0006\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u0003H\u0007J\u0015\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u0003H\u0000\u00a2\u0006\u0002\b\u0015J\u001d\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u0003H\u0000\u00a2\u0006\u0002\b\u0018J\f\u0010\u0019\u001a\u00020\u0003*\u00020\u0003H\u0002J\f\u0010\u001a\u001a\u00020\u0003*\u00020\u0003H\u0002J\u000e\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001b\u001a\u00020\u001cJ\u000e\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001d\u001a\u00020\u001eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u001f"}, d2={"Lokio/Throttler;", "", "allocatedUntil", "", "<init>", "(J)V", "()V", "bytesPerSecond", "waitByteCount", "maxByteCount", "lock", "Ljava/util/concurrent/locks/ReentrantLock;", "getLock", "()Ljava/util/concurrent/locks/ReentrantLock;", "condition", "Ljava/util/concurrent/locks/Condition;", "getCondition", "()Ljava/util/concurrent/locks/Condition;", "", "take", "byteCount", "take$okio", "byteCountOrWaitNanos", "now", "byteCountOrWaitNanos$okio", "nanosToBytes", "bytesToNanos", "source", "Lokio/Source;", "sink", "Lokio/Sink;", "okio"})
public final class Throttler {
    private long allocatedUntil;
    private long bytesPerSecond;
    private long waitByteCount;
    private long maxByteCount;
    @NotNull
    private final ReentrantLock lock;
    @NotNull
    private final Condition condition;

    public Throttler(long allocatedUntil) {
        this.allocatedUntil = allocatedUntil;
        this.waitByteCount = 8192L;
        this.maxByteCount = 262144L;
        this.lock = new ReentrantLock();
        Condition condition = this.lock.newCondition();
        Intrinsics.checkNotNullExpressionValue(condition, "newCondition(...)");
        this.condition = condition;
    }

    @NotNull
    public final ReentrantLock getLock() {
        return this.lock;
    }

    @NotNull
    public final Condition getCondition() {
        return this.condition;
    }

    public Throttler() {
        this(System.nanoTime());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @JvmOverloads
    public final void bytesPerSecond(long bytesPerSecond, long waitByteCount, long maxByteCount) {
        Lock lock = this.lock;
        lock.lock();
        try {
            boolean bl = false;
            if (!(bytesPerSecond >= 0L)) {
                String string = "Failed requirement.";
                throw new IllegalArgumentException(string.toString());
            }
            if (!(waitByteCount > 0L)) {
                String string = "Failed requirement.";
                throw new IllegalArgumentException(string.toString());
            }
            if (!(maxByteCount >= waitByteCount)) {
                String string = "Failed requirement.";
                throw new IllegalArgumentException(string.toString());
            }
            this.bytesPerSecond = bytesPerSecond;
            this.waitByteCount = waitByteCount;
            this.maxByteCount = maxByteCount;
            this.condition.signalAll();
            Unit unit = Unit.INSTANCE;
        }
        finally {
            lock.unlock();
        }
    }

    public static /* synthetic */ void bytesPerSecond$default(Throttler throttler, long l2, long l3, long l4, int n2, Object object) {
        if ((n2 & 2) != 0) {
            l3 = throttler.waitByteCount;
        }
        if ((n2 & 4) != 0) {
            l4 = throttler.maxByteCount;
        }
        throttler.bytesPerSecond(l2, l3, l4);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final long take$okio(long byteCount) {
        if (!(byteCount > 0L)) {
            String string = "Failed requirement.";
            throw new IllegalArgumentException(string.toString());
        }
        Lock lock = this.lock;
        lock.lock();
        try {
            boolean bl = false;
            while (true) {
                long now;
                long byteCountOrWaitNanos;
                if ((byteCountOrWaitNanos = this.byteCountOrWaitNanos$okio(now = System.nanoTime(), byteCount)) >= 0L) {
                    long l2 = byteCountOrWaitNanos;
                    return l2;
                }
                this.condition.awaitNanos(-byteCountOrWaitNanos);
            }
        }
        finally {
            lock.unlock();
        }
    }

    public final long byteCountOrWaitNanos$okio(long now, long byteCount) {
        if (this.bytesPerSecond == 0L) {
            return byteCount;
        }
        long idleInNanos = Math.max(this.allocatedUntil - now, 0L);
        long immediateBytes = this.maxByteCount - this.nanosToBytes(idleInNanos);
        if (immediateBytes >= byteCount) {
            this.allocatedUntil = now + idleInNanos + this.bytesToNanos(byteCount);
            return byteCount;
        }
        if (immediateBytes >= this.waitByteCount) {
            this.allocatedUntil = now + this.bytesToNanos(this.maxByteCount);
            return immediateBytes;
        }
        long minByteCount = Math.min(this.waitByteCount, byteCount);
        long minWaitNanos = idleInNanos + this.bytesToNanos(minByteCount - this.maxByteCount);
        if (minWaitNanos == 0L) {
            this.allocatedUntil = now + this.bytesToNanos(this.maxByteCount);
            return minByteCount;
        }
        return -minWaitNanos;
    }

    private final long nanosToBytes(long $this$nanosToBytes) {
        return $this$nanosToBytes * this.bytesPerSecond / 1000000000L;
    }

    private final long bytesToNanos(long $this$bytesToNanos) {
        return $this$bytesToNanos * 1000000000L / this.bytesPerSecond;
    }

    @NotNull
    public final Source source(@NotNull Source source2) {
        Intrinsics.checkNotNullParameter(source2, "source");
        return new ForwardingSource(source2, this){
            final /* synthetic */ Throttler this$0;
            {
                this.this$0 = $receiver;
                super($source);
            }

            public long read(Buffer sink2, long byteCount) {
                Intrinsics.checkNotNullParameter(sink2, "sink");
                try {
                    long toRead = this.this$0.take$okio(byteCount);
                    return super.read(sink2, toRead);
                }
                catch (InterruptedException e2) {
                    Thread.currentThread().interrupt();
                    throw new InterruptedIOException("interrupted");
                }
            }
        };
    }

    @NotNull
    public final Sink sink(@NotNull Sink sink2) {
        Intrinsics.checkNotNullParameter(sink2, "sink");
        return new ForwardingSink(sink2, this){
            final /* synthetic */ Throttler this$0;
            {
                this.this$0 = $receiver;
                super($sink);
            }

            public void write(Buffer source2, long byteCount) throws IOException {
                Intrinsics.checkNotNullParameter(source2, "source");
                try {
                    long toWrite;
                    for (long remaining = byteCount; remaining > 0L; remaining -= toWrite) {
                        toWrite = this.this$0.take$okio(remaining);
                        super.write(source2, toWrite);
                    }
                }
                catch (InterruptedException e2) {
                    Thread.currentThread().interrupt();
                    throw new InterruptedIOException("interrupted");
                }
            }
        };
    }

    @JvmOverloads
    public final void bytesPerSecond(long bytesPerSecond, long waitByteCount) {
        Throttler.bytesPerSecond$default(this, bytesPerSecond, waitByteCount, 0L, 4, null);
    }

    @JvmOverloads
    public final void bytesPerSecond(long bytesPerSecond) {
        Throttler.bytesPerSecond$default(this, bytesPerSecond, 0L, 0L, 6, null);
    }
}

