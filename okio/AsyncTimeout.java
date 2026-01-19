/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.SourceDebugExtension
 */
package okio;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.-SegmentedByteString;
import okio.Buffer;
import okio.Segment;
import okio.Sink;
import okio.Source;
import okio.Timeout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0016\u0018\u0000 \u001f2\u00020\u0001:\u0002\u001e\u001fB\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0006\u0010\t\u001a\u00020\nJ\u0006\u0010\u000b\u001a\u00020\fJ\b\u0010\r\u001a\u00020\nH\u0016J\u0010\u0010\u000e\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\bH\u0002J\b\u0010\u0010\u001a\u00020\nH\u0014J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0013\u001a\u00020\u0014J%\u0010\u0015\u001a\u0002H\u0016\"\u0004\b\u0000\u0010\u00162\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0018H\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0019J\u0012\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001bH\u0001J\u0012\u0010\u001d\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001bH\u0014R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0000X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006 "}, d2={"Lokio/AsyncTimeout;", "Lokio/Timeout;", "<init>", "()V", "state", "", "next", "timeoutAt", "", "enter", "", "exit", "", "cancel", "remainingNanos", "now", "timedOut", "sink", "Lokio/Sink;", "source", "Lokio/Source;", "withTimeout", "T", "block", "Lkotlin/Function0;", "(Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "access$newTimeoutException", "Ljava/io/IOException;", "cause", "newTimeoutException", "Watchdog", "Companion", "okio"})
@SourceDebugExtension(value={"SMAP\nAsyncTimeout.kt\nKotlin\n*S Kotlin\n*F\n+ 1 AsyncTimeout.kt\nokio/AsyncTimeout\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,382:1\n1#2:383\n*E\n"})
public class AsyncTimeout
extends Timeout {
    @NotNull
    private static final Companion Companion = new Companion(null);
    private int state;
    @Nullable
    private AsyncTimeout next;
    private long timeoutAt;
    @NotNull
    private static final ReentrantLock lock = new ReentrantLock();
    @NotNull
    private static final Condition condition;
    private static final int TIMEOUT_WRITE_SIZE = 65536;
    private static final long IDLE_TIMEOUT_MILLIS;
    private static final long IDLE_TIMEOUT_NANOS;
    private static final int STATE_IDLE = 0;
    private static final int STATE_IN_QUEUE = 1;
    private static final int STATE_TIMED_OUT = 2;
    private static final int STATE_CANCELED = 3;
    @Nullable
    private static AsyncTimeout head;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final void enter() {
        long timeoutNanos = this.timeoutNanos();
        boolean hasDeadline = this.hasDeadline();
        if (timeoutNanos == 0L && !hasDeadline) {
            return;
        }
        Lock lock = AsyncTimeout.lock;
        lock.lock();
        try {
            boolean bl = false;
            if (!(this.state == 0)) {
                boolean bl2 = false;
                String string = "Unbalanced enter/exit";
                throw new IllegalStateException(string.toString());
            }
            this.state = 1;
            AsyncTimeout.Companion.insertIntoQueue(this, timeoutNanos, hasDeadline);
            Unit unit = Unit.INSTANCE;
        }
        finally {
            lock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final boolean exit() {
        Lock lock = AsyncTimeout.lock;
        lock.lock();
        try {
            boolean bl = false;
            int oldState = this.state;
            this.state = 0;
            if (oldState == 1) {
                AsyncTimeout.Companion.removeFromQueue(this);
                boolean bl2 = false;
                return bl2;
            }
            boolean bl3 = oldState == 2;
            return bl3;
        }
        finally {
            lock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void cancel() {
        super.cancel();
        Lock lock = AsyncTimeout.lock;
        lock.lock();
        try {
            boolean bl = false;
            if (this.state == 1) {
                AsyncTimeout.Companion.removeFromQueue(this);
                this.state = 3;
            }
            Unit unit = Unit.INSTANCE;
        }
        finally {
            lock.unlock();
        }
    }

    private final long remainingNanos(long now) {
        return this.timeoutAt - now;
    }

    protected void timedOut() {
    }

    @NotNull
    public final Sink sink(@NotNull Sink sink2) {
        Intrinsics.checkNotNullParameter(sink2, "sink");
        return new Sink(this, sink2){
            final /* synthetic */ AsyncTimeout this$0;
            final /* synthetic */ Sink $sink;
            {
                this.this$0 = $receiver;
                this.$sink = $sink;
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             * WARNING - void declaration
             */
            public void write(Buffer source2, long byteCount) {
                long toWrite;
                Intrinsics.checkNotNullParameter(source2, "source");
                -SegmentedByteString.checkOffsetAndCount(source2.size(), 0L, byteCount);
                for (long remaining = byteCount; remaining > 0L; remaining -= toWrite) {
                    boolean timedOut$iv;
                    void this_$iv;
                    toWrite = 0L;
                    Intrinsics.checkNotNull(source2.head);
                    while (toWrite < 65536L) {
                        Segment s2;
                        int segmentSize = s2.limit - s2.pos;
                        if ((toWrite += (long)segmentSize) >= remaining) {
                            toWrite = remaining;
                            break;
                        }
                        Intrinsics.checkNotNull(s2.next);
                    }
                    AsyncTimeout segmentSize = this.this$0;
                    Sink sink2 = this.$sink;
                    boolean $i$f$withTimeout = false;
                    boolean throwOnTimeout$iv = false;
                    this_$iv.enter();
                    try {
                        boolean bl = false;
                        sink2.write(source2, toWrite);
                        Unit result$iv = Unit.INSTANCE;
                        throwOnTimeout$iv = true;
                        Unit unit = result$iv;
                        timedOut$iv = this_$iv.exit();
                        if (!timedOut$iv) continue;
                    }
                    catch (IOException e$iv) {
                        try {
                            throw !this_$iv.exit() ? (Throwable)e$iv : (Throwable)this_$iv.access$newTimeoutException(e$iv);
                        }
                        catch (Throwable throwable) {
                            timedOut$iv = this_$iv.exit();
                            if (timedOut$iv && throwOnTimeout$iv) {
                                throw this_$iv.access$newTimeoutException(null);
                            }
                            throw throwable;
                        }
                    }
                    throw this_$iv.access$newTimeoutException(null);
                }
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             * WARNING - void declaration
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            public void flush() {
                void this_$iv;
                AsyncTimeout asyncTimeout = this.this$0;
                Sink sink2 = this.$sink;
                boolean $i$f$withTimeout = false;
                boolean throwOnTimeout$iv = false;
                this_$iv.enter();
                try {
                    boolean bl = false;
                    sink2.flush();
                    Unit result$iv = Unit.INSTANCE;
                    throwOnTimeout$iv = true;
                    Unit unit = result$iv;
                    boolean timedOut$iv = this_$iv.exit();
                    if (!timedOut$iv) return;
                }
                catch (IOException e$iv) {
                    try {
                        throw !this_$iv.exit() ? (Throwable)e$iv : (Throwable)this_$iv.access$newTimeoutException(e$iv);
                    }
                    catch (Throwable throwable) {
                        boolean timedOut$iv = this_$iv.exit();
                        if (!timedOut$iv || !throwOnTimeout$iv) throw throwable;
                        throw this_$iv.access$newTimeoutException(null);
                    }
                }
                throw this_$iv.access$newTimeoutException(null);
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             * WARNING - void declaration
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            public void close() {
                void this_$iv;
                AsyncTimeout asyncTimeout = this.this$0;
                Sink sink2 = this.$sink;
                boolean $i$f$withTimeout = false;
                boolean throwOnTimeout$iv = false;
                this_$iv.enter();
                try {
                    boolean bl = false;
                    sink2.close();
                    Unit result$iv = Unit.INSTANCE;
                    throwOnTimeout$iv = true;
                    Unit unit = result$iv;
                    boolean timedOut$iv = this_$iv.exit();
                    if (!timedOut$iv) return;
                }
                catch (IOException e$iv) {
                    try {
                        throw !this_$iv.exit() ? (Throwable)e$iv : (Throwable)this_$iv.access$newTimeoutException(e$iv);
                    }
                    catch (Throwable throwable) {
                        boolean timedOut$iv = this_$iv.exit();
                        if (!timedOut$iv || !throwOnTimeout$iv) throw throwable;
                        throw this_$iv.access$newTimeoutException(null);
                    }
                }
                throw this_$iv.access$newTimeoutException(null);
            }

            public AsyncTimeout timeout() {
                return this.this$0;
            }

            public String toString() {
                return "AsyncTimeout.sink(" + this.$sink + ')';
            }
        };
    }

    @NotNull
    public final Source source(@NotNull Source source2) {
        Intrinsics.checkNotNullParameter(source2, "source");
        return new Source(this, source2){
            final /* synthetic */ AsyncTimeout this$0;
            final /* synthetic */ Source $source;
            {
                this.this$0 = $receiver;
                this.$source = $source;
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             * WARNING - void declaration
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            public long read(Buffer sink2, long byteCount) {
                void this_$iv;
                Intrinsics.checkNotNullParameter(sink2, "sink");
                AsyncTimeout asyncTimeout = this.this$0;
                Source source2 = this.$source;
                boolean $i$f$withTimeout = false;
                boolean throwOnTimeout$iv = false;
                this_$iv.enter();
                try {
                    boolean bl = false;
                    long result$iv = source2.read(sink2, byteCount);
                    throwOnTimeout$iv = true;
                    long l2 = result$iv;
                    boolean timedOut$iv = this_$iv.exit();
                    if (!timedOut$iv) return l2;
                }
                catch (IOException e$iv) {
                    try {
                        throw !this_$iv.exit() ? (Throwable)e$iv : (Throwable)this_$iv.access$newTimeoutException(e$iv);
                    }
                    catch (Throwable throwable) {
                        boolean timedOut$iv = this_$iv.exit();
                        if (!timedOut$iv || !throwOnTimeout$iv) throw throwable;
                        throw this_$iv.access$newTimeoutException(null);
                    }
                }
                throw this_$iv.access$newTimeoutException(null);
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             * WARNING - void declaration
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            public void close() {
                void this_$iv;
                AsyncTimeout asyncTimeout = this.this$0;
                Source source2 = this.$source;
                boolean $i$f$withTimeout = false;
                boolean throwOnTimeout$iv = false;
                this_$iv.enter();
                try {
                    boolean bl = false;
                    source2.close();
                    Unit result$iv = Unit.INSTANCE;
                    throwOnTimeout$iv = true;
                    Unit unit = result$iv;
                    boolean timedOut$iv = this_$iv.exit();
                    if (!timedOut$iv) return;
                }
                catch (IOException e$iv) {
                    try {
                        throw !this_$iv.exit() ? (Throwable)e$iv : (Throwable)this_$iv.access$newTimeoutException(e$iv);
                    }
                    catch (Throwable throwable) {
                        boolean timedOut$iv = this_$iv.exit();
                        if (!timedOut$iv || !throwOnTimeout$iv) throw throwable;
                        throw this_$iv.access$newTimeoutException(null);
                    }
                }
                throw this_$iv.access$newTimeoutException(null);
            }

            public AsyncTimeout timeout() {
                return this.this$0;
            }

            public String toString() {
                return "AsyncTimeout.source(" + this.$source + ')';
            }
        };
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final <T> T withTimeout(@NotNull Function0<? extends T> block) {
        T t2;
        Intrinsics.checkNotNullParameter(block, "block");
        boolean $i$f$withTimeout = false;
        boolean throwOnTimeout = false;
        this.enter();
        try {
            T result = block.invoke();
            throwOnTimeout = true;
            t2 = result;
        }
        catch (IOException e2) {
            try {
                throw !this.exit() ? (Throwable)e2 : (Throwable)this.access$newTimeoutException(e2);
            }
            catch (Throwable throwable) {
                InlineMarker.finallyStart(1);
                boolean timedOut = this.exit();
                if (timedOut && throwOnTimeout) {
                    throw this.access$newTimeoutException(null);
                }
                InlineMarker.finallyEnd(1);
                throw throwable;
            }
        }
        InlineMarker.finallyStart(1);
        boolean timedOut = this.exit();
        if (timedOut) {
            throw this.access$newTimeoutException(null);
        }
        InlineMarker.finallyEnd(1);
        return t2;
    }

    @PublishedApi
    @NotNull
    public final IOException access$newTimeoutException(@Nullable IOException cause) {
        return this.newTimeoutException(cause);
    }

    @NotNull
    protected IOException newTimeoutException(@Nullable IOException cause) {
        InterruptedIOException e2 = new InterruptedIOException("timeout");
        if (cause != null) {
            e2.initCause(cause);
        }
        return e2;
    }

    static {
        Condition condition = lock.newCondition();
        Intrinsics.checkNotNullExpressionValue(condition, "newCondition(...)");
        AsyncTimeout.condition = condition;
        IDLE_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(60L);
        IDLE_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(IDLE_TIMEOUT_MILLIS);
    }

    @Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J \u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00162\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u0010\u0010\u001d\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0016H\u0002J\b\u0010\u001e\u001a\u0004\u0018\u00010\u0016R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\rX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\rX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\rX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\rX\u0082T\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2={"Lokio/AsyncTimeout$Companion;", "", "<init>", "()V", "lock", "Ljava/util/concurrent/locks/ReentrantLock;", "getLock", "()Ljava/util/concurrent/locks/ReentrantLock;", "condition", "Ljava/util/concurrent/locks/Condition;", "getCondition", "()Ljava/util/concurrent/locks/Condition;", "TIMEOUT_WRITE_SIZE", "", "IDLE_TIMEOUT_MILLIS", "", "IDLE_TIMEOUT_NANOS", "STATE_IDLE", "STATE_IN_QUEUE", "STATE_TIMED_OUT", "STATE_CANCELED", "head", "Lokio/AsyncTimeout;", "insertIntoQueue", "", "node", "timeoutNanos", "hasDeadline", "", "removeFromQueue", "awaitTimeout", "okio"})
    private static final class Companion {
        private Companion() {
        }

        @NotNull
        public final ReentrantLock getLock() {
            return lock;
        }

        @NotNull
        public final Condition getCondition() {
            return condition;
        }

        private final void insertIntoQueue(AsyncTimeout node, long timeoutNanos, boolean hasDeadline) {
            if (head == null) {
                head = new AsyncTimeout();
                new Watchdog().start();
            }
            long now = System.nanoTime();
            if (timeoutNanos != 0L && hasDeadline) {
                node.timeoutAt = now + Math.min(timeoutNanos, node.deadlineNanoTime() - now);
            } else if (timeoutNanos != 0L) {
                node.timeoutAt = now + timeoutNanos;
            } else if (hasDeadline) {
                node.timeoutAt = node.deadlineNanoTime();
            } else {
                throw new AssertionError();
            }
            long remainingNanos = node.remainingNanos(now);
            AsyncTimeout asyncTimeout = head;
            Intrinsics.checkNotNull(asyncTimeout);
            AsyncTimeout prev = asyncTimeout;
            while (true) {
                block12: {
                    block11: {
                        if (prev.next == null) break block11;
                        AsyncTimeout asyncTimeout2 = prev.next;
                        Intrinsics.checkNotNull(asyncTimeout2);
                        if (remainingNanos >= asyncTimeout2.remainingNanos(now)) break block12;
                    }
                    node.next = prev.next;
                    prev.next = node;
                    if (prev != head) break;
                    this.getCondition().signal();
                    break;
                }
                Intrinsics.checkNotNull(prev.next);
            }
        }

        private final void removeFromQueue(AsyncTimeout node) {
            AsyncTimeout prev = head;
            while (prev != null) {
                if (prev.next == node) {
                    prev.next = node.next;
                    node.next = null;
                    return;
                }
                prev = prev.next;
            }
            throw new IllegalStateException("node was not found in the queue".toString());
        }

        @Nullable
        public final AsyncTimeout awaitTimeout() throws InterruptedException {
            AsyncTimeout asyncTimeout = head;
            Intrinsics.checkNotNull(asyncTimeout);
            AsyncTimeout node = asyncTimeout.next;
            if (node == null) {
                long startNanos = System.nanoTime();
                this.getCondition().await(IDLE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
                AsyncTimeout asyncTimeout2 = head;
                Intrinsics.checkNotNull(asyncTimeout2);
                return asyncTimeout2.next == null && System.nanoTime() - startNanos >= IDLE_TIMEOUT_NANOS ? head : null;
            }
            long waitNanos = node.remainingNanos(System.nanoTime());
            if (waitNanos > 0L) {
                this.getCondition().await(waitNanos, TimeUnit.NANOSECONDS);
                return null;
            }
            AsyncTimeout asyncTimeout3 = head;
            Intrinsics.checkNotNull(asyncTimeout3);
            asyncTimeout3.next = node.next;
            node.next = null;
            node.state = 2;
            return node;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }

    @Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0016\u00a8\u0006\u0006"}, d2={"Lokio/AsyncTimeout$Watchdog;", "Ljava/lang/Thread;", "<init>", "()V", "run", "", "okio"})
    private static final class Watchdog
    extends Thread {
        public Watchdog() {
            super("Okio Watchdog");
            this.setDaemon(true);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            while (true) {
                try {
                    AsyncTimeout timedOut = null;
                    Lock lock = Companion.getLock();
                    lock.lock();
                    try {
                        boolean bl = false;
                        timedOut = Companion.awaitTimeout();
                        if (timedOut == head) {
                            Companion;
                            head = null;
                            return;
                        }
                        Unit unit = Unit.INSTANCE;
                    }
                    finally {
                        lock.unlock();
                    }
                    AsyncTimeout asyncTimeout = timedOut;
                    if (asyncTimeout != null) {
                        asyncTimeout.timedOut();
                    }
                }
                catch (InterruptedException interruptedException) {
                }
            }
        }
    }
}

