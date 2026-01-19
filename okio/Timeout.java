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
import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.time.Duration;
import kotlin.time.DurationUnit;
import kotlin.time.DurationUnitKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0016\u0018\u0000 \u001f2\u00020\u0001:\u0001\u001fB\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0018\u0010\n\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\fH\u0016J\b\u0010\b\u001a\u00020\u0007H\u0016J\b\u0010\u0004\u001a\u00020\u0005H\u0016J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\u0006\u001a\u00020\u00002\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0016\u0010\r\u001a\u00020\u00002\u0006\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\fJ\b\u0010\u000f\u001a\u00020\u0000H\u0016J\b\u0010\u0010\u001a\u00020\u0000H\u0016J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\u0012H\u0016J\u0010\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\u0017\u001a\u00020\u00122\u0006\u0010\u0018\u001a\u00020\u0001H\u0016J-\u0010\u0019\u001a\u0002H\u001a\"\u0004\b\u0000\u0010\u001a2\u0006\u0010\u001b\u001a\u00020\u00002\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u0002H\u001a0\u001dH\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001eR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\u0001X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006 "}, d2={"Lokio/Timeout;", "", "<init>", "()V", "hasDeadline", "", "deadlineNanoTime", "", "timeoutNanos", "cancelMark", "timeout", "unit", "Ljava/util/concurrent/TimeUnit;", "deadline", "duration", "clearTimeout", "clearDeadline", "throwIfReached", "", "cancel", "awaitSignal", "condition", "Ljava/util/concurrent/locks/Condition;", "waitUntilNotified", "monitor", "intersectWith", "T", "other", "block", "Lkotlin/Function0;", "(Lokio/Timeout;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "Companion", "okio"})
@SourceDebugExtension(value={"SMAP\nTimeout.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Timeout.kt\nokio/Timeout\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,358:1\n1#2:359\n*E\n"})
public class Timeout {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private boolean hasDeadline;
    private long deadlineNanoTime;
    private long timeoutNanos;
    @Nullable
    private volatile Object cancelMark;
    @JvmField
    @NotNull
    public static final Timeout NONE = new Timeout(){

        public Timeout timeout(long timeout2, TimeUnit unit) {
            Intrinsics.checkNotNullParameter((Object)((Object)unit), "unit");
            return this;
        }

        public Timeout deadlineNanoTime(long deadlineNanoTime) {
            return this;
        }

        public void throwIfReached() {
        }
    };

    @NotNull
    public Timeout timeout(long timeout2, @NotNull TimeUnit unit) {
        Intrinsics.checkNotNullParameter((Object)unit, "unit");
        if (!(timeout2 >= 0L)) {
            boolean bl = false;
            String string = "timeout < 0: " + timeout2;
            throw new IllegalArgumentException(string.toString());
        }
        this.timeoutNanos = unit.toNanos(timeout2);
        return this;
    }

    public long timeoutNanos() {
        return this.timeoutNanos;
    }

    public boolean hasDeadline() {
        return this.hasDeadline;
    }

    public long deadlineNanoTime() {
        if (!this.hasDeadline) {
            boolean bl = false;
            String string = "No deadline";
            throw new IllegalStateException(string.toString());
        }
        return this.deadlineNanoTime;
    }

    @NotNull
    public Timeout deadlineNanoTime(long deadlineNanoTime) {
        this.hasDeadline = true;
        this.deadlineNanoTime = deadlineNanoTime;
        return this;
    }

    @NotNull
    public final Timeout deadline(long duration, @NotNull TimeUnit unit) {
        Intrinsics.checkNotNullParameter((Object)unit, "unit");
        if (!(duration > 0L)) {
            boolean bl = false;
            String string = "duration <= 0: " + duration;
            throw new IllegalArgumentException(string.toString());
        }
        return this.deadlineNanoTime(System.nanoTime() + unit.toNanos(duration));
    }

    @NotNull
    public Timeout clearTimeout() {
        this.timeoutNanos = 0L;
        return this;
    }

    @NotNull
    public Timeout clearDeadline() {
        this.hasDeadline = false;
        return this;
    }

    public void throwIfReached() throws IOException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedIOException("interrupted");
        }
        if (this.hasDeadline && this.deadlineNanoTime - System.nanoTime() <= 0L) {
            throw new InterruptedIOException("deadline reached");
        }
    }

    public void cancel() {
        this.cancelMark = new Object();
    }

    public void awaitSignal(@NotNull Condition condition) throws InterruptedIOException {
        Intrinsics.checkNotNullParameter(condition, "condition");
        try {
            long waitNanos;
            long l2;
            boolean hasDeadline = this.hasDeadline();
            long timeoutNanos = this.timeoutNanos();
            if (!hasDeadline && timeoutNanos == 0L) {
                condition.await();
                return;
            }
            if (hasDeadline && timeoutNanos != 0L) {
                long deadlineNanos = this.deadlineNanoTime() - System.nanoTime();
                l2 = Math.min(timeoutNanos, deadlineNanos);
            } else {
                l2 = waitNanos = hasDeadline ? this.deadlineNanoTime() - System.nanoTime() : timeoutNanos;
            }
            if (waitNanos <= 0L) {
                throw new InterruptedIOException("timeout");
            }
            Object cancelMarkBefore = this.cancelMark;
            long nanosRemaining = condition.awaitNanos(waitNanos);
            if (nanosRemaining > 0L) {
                return;
            }
            if (this.cancelMark != cancelMarkBefore) {
                return;
            }
            throw new InterruptedIOException("timeout");
        }
        catch (InterruptedException e2) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException("interrupted");
        }
    }

    public void waitUntilNotified(@NotNull Object monitor) throws InterruptedIOException {
        Intrinsics.checkNotNullParameter(monitor, "monitor");
        try {
            long waitNanos;
            long l2;
            boolean hasDeadline = this.hasDeadline();
            long timeoutNanos = this.timeoutNanos();
            if (!hasDeadline && timeoutNanos == 0L) {
                monitor.wait();
                return;
            }
            long start = System.nanoTime();
            if (hasDeadline && timeoutNanos != 0L) {
                long deadlineNanos = this.deadlineNanoTime() - start;
                l2 = Math.min(timeoutNanos, deadlineNanos);
            } else {
                l2 = waitNanos = hasDeadline ? this.deadlineNanoTime() - start : timeoutNanos;
            }
            if (waitNanos <= 0L) {
                throw new InterruptedIOException("timeout");
            }
            Object cancelMarkBefore = this.cancelMark;
            long waitMillis = waitNanos / 1000000L;
            monitor.wait(waitMillis, (int)(waitNanos - waitMillis * 1000000L));
            long elapsedNanos = System.nanoTime() - start;
            if (elapsedNanos < waitNanos) {
                return;
            }
            if (this.cancelMark != cancelMarkBefore) {
                return;
            }
            throw new InterruptedIOException("timeout");
        }
        catch (InterruptedException e2) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException("interrupted");
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final <T> T intersectWith(@NotNull Timeout other, @NotNull Function0<? extends T> block) {
        Intrinsics.checkNotNullParameter(other, "other");
        Intrinsics.checkNotNullParameter(block, "block");
        boolean $i$f$intersectWith = false;
        long originalTimeout = this.timeoutNanos();
        this.timeout(Companion.minTimeout(other.timeoutNanos(), this.timeoutNanos()), TimeUnit.NANOSECONDS);
        if (this.hasDeadline()) {
            long originalDeadline = this.deadlineNanoTime();
            if (other.hasDeadline()) {
                this.deadlineNanoTime(Math.min(this.deadlineNanoTime(), other.deadlineNanoTime()));
            }
            try {
                T t2 = block.invoke();
                return t2;
            }
            finally {
                InlineMarker.finallyStart(1);
                this.timeout(originalTimeout, TimeUnit.NANOSECONDS);
                if (other.hasDeadline()) {
                    this.deadlineNanoTime(originalDeadline);
                }
                InlineMarker.finallyEnd(1);
            }
        }
        if (other.hasDeadline()) {
            this.deadlineNanoTime(other.deadlineNanoTime());
        }
        try {
            T t3 = block.invoke();
            return t3;
        }
        finally {
            InlineMarker.finallyStart(1);
            this.timeout(originalTimeout, TimeUnit.NANOSECONDS);
            if (other.hasDeadline()) {
                this.clearDeadline();
            }
            InlineMarker.finallyEnd(1);
        }
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u001a\u0010\u0006\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tJ\u0019\u0010\u0006\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0004\b\f\u0010\rJ\u0016\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0010\u001a\u00020\u0007R\u0010\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2={"Lokio/Timeout$Companion;", "", "<init>", "()V", "NONE", "Lokio/Timeout;", "timeout", "", "unit", "Lkotlin/time/DurationUnit;", "duration", "Lkotlin/time/Duration;", "timeout-HG0u8IE", "(Lokio/Timeout;J)Lokio/Timeout;", "minTimeout", "aNanos", "bNanos", "okio"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final Timeout timeout(@NotNull Timeout $this$timeout, long timeout2, @NotNull DurationUnit unit) {
            Intrinsics.checkNotNullParameter($this$timeout, "<this>");
            Intrinsics.checkNotNullParameter((Object)unit, "unit");
            return $this$timeout.timeout(timeout2, DurationUnitKt.toTimeUnit(unit));
        }

        @NotNull
        public final Timeout timeout-HG0u8IE(@NotNull Timeout $this$timeout_u2dHG0u8IE, long duration) {
            Intrinsics.checkNotNullParameter($this$timeout_u2dHG0u8IE, "$this$timeout");
            return $this$timeout_u2dHG0u8IE.timeout(Duration.getInWholeNanoseconds-impl(duration), TimeUnit.NANOSECONDS);
        }

        public final long minTimeout(long aNanos, long bNanos) {
            return aNanos == 0L ? bNanos : (bNanos == 0L ? aNanos : (aNanos < bNanos ? aNanos : bNanos));
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

