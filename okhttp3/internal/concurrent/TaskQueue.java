/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.concurrent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.Util;
import okhttp3.internal.concurrent.Task;
import okhttp3.internal.concurrent.TaskLoggerKt;
import okhttp3.internal.concurrent.TaskRunner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001:\u00013B\u0017\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010!\u001a\u00020\"J\r\u0010#\u001a\u00020\u000eH\u0000\u00a2\u0006\u0002\b$J8\u0010%\u001a\u00020\"2\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010&\u001a\u00020'2\b\b\u0002\u0010(\u001a\u00020\u000e2\u000e\b\u0004\u0010)\u001a\b\u0012\u0004\u0012\u00020\"0*H\u0086\b\u00f8\u0001\u0000J\u0006\u0010+\u001a\u00020,J.\u0010-\u001a\u00020\"2\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010&\u001a\u00020'2\u000e\b\u0004\u0010)\u001a\b\u0012\u0004\u0012\u00020'0*H\u0086\b\u00f8\u0001\u0000J\u0018\u0010-\u001a\u00020\"2\u0006\u0010.\u001a\u00020\b2\b\b\u0002\u0010&\u001a\u00020'J%\u0010/\u001a\u00020\u000e2\u0006\u0010.\u001a\u00020\b2\u0006\u0010&\u001a\u00020'2\u0006\u00100\u001a\u00020\u000eH\u0000\u00a2\u0006\u0002\b1J\u0006\u0010\u001c\u001a\u00020\"J\b\u00102\u001a\u00020\u0005H\u0016R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\u000eX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\b0\u0014X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0014\u0010\u0004\u001a\u00020\u0005X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\b0\u001a8F\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u0016R\u001a\u0010\u001c\u001a\u00020\u000eX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0010\"\u0004\b\u001e\u0010\u0012R\u0014\u0010\u0002\u001a\u00020\u0003X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 \u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u00064"}, d2={"Lokhttp3/internal/concurrent/TaskQueue;", "", "taskRunner", "Lokhttp3/internal/concurrent/TaskRunner;", "name", "", "(Lokhttp3/internal/concurrent/TaskRunner;Ljava/lang/String;)V", "activeTask", "Lokhttp3/internal/concurrent/Task;", "getActiveTask$okhttp", "()Lokhttp3/internal/concurrent/Task;", "setActiveTask$okhttp", "(Lokhttp3/internal/concurrent/Task;)V", "cancelActiveTask", "", "getCancelActiveTask$okhttp", "()Z", "setCancelActiveTask$okhttp", "(Z)V", "futureTasks", "", "getFutureTasks$okhttp", "()Ljava/util/List;", "getName$okhttp", "()Ljava/lang/String;", "scheduledTasks", "", "getScheduledTasks", "shutdown", "getShutdown$okhttp", "setShutdown$okhttp", "getTaskRunner$okhttp", "()Lokhttp3/internal/concurrent/TaskRunner;", "cancelAll", "", "cancelAllAndDecide", "cancelAllAndDecide$okhttp", "execute", "delayNanos", "", "cancelable", "block", "Lkotlin/Function0;", "idleLatch", "Ljava/util/concurrent/CountDownLatch;", "schedule", "task", "scheduleAndDecide", "recurrence", "scheduleAndDecide$okhttp", "toString", "AwaitIdleTask", "okhttp"})
public final class TaskQueue {
    @NotNull
    private final TaskRunner taskRunner;
    @NotNull
    private final String name;
    private boolean shutdown;
    @Nullable
    private Task activeTask;
    @NotNull
    private final List<Task> futureTasks;
    private boolean cancelActiveTask;

    public TaskQueue(@NotNull TaskRunner taskRunner, @NotNull String name) {
        Intrinsics.checkNotNullParameter(taskRunner, "taskRunner");
        Intrinsics.checkNotNullParameter(name, "name");
        this.taskRunner = taskRunner;
        this.name = name;
        this.futureTasks = new ArrayList();
    }

    @NotNull
    public final TaskRunner getTaskRunner$okhttp() {
        return this.taskRunner;
    }

    @NotNull
    public final String getName$okhttp() {
        return this.name;
    }

    public final boolean getShutdown$okhttp() {
        return this.shutdown;
    }

    public final void setShutdown$okhttp(boolean bl) {
        this.shutdown = bl;
    }

    @Nullable
    public final Task getActiveTask$okhttp() {
        return this.activeTask;
    }

    public final void setActiveTask$okhttp(@Nullable Task task) {
        this.activeTask = task;
    }

    @NotNull
    public final List<Task> getFutureTasks$okhttp() {
        return this.futureTasks;
    }

    public final boolean getCancelActiveTask$okhttp() {
        return this.cancelActiveTask;
    }

    public final void setCancelActiveTask$okhttp(boolean bl) {
        this.cancelActiveTask = bl;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @NotNull
    public final List<Task> getScheduledTasks() {
        List<Task> list;
        TaskRunner taskRunner = this.taskRunner;
        synchronized (taskRunner) {
            boolean bl = false;
            list = CollectionsKt.toList((Iterable)this.getFutureTasks$okhttp());
        }
        return list;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final void schedule(@NotNull Task task, long delayNanos) {
        Intrinsics.checkNotNullParameter(task, "task");
        TaskRunner taskRunner = this.taskRunner;
        synchronized (taskRunner) {
            boolean bl = false;
            if (this.getShutdown$okhttp()) {
                if (task.getCancelable()) {
                    boolean $i$f$taskLog = false;
                    if (TaskRunner.Companion.getLogger().isLoggable(Level.FINE)) {
                        TaskQueue taskQueue = this;
                        Task task2 = task;
                        boolean bl2 = false;
                        String string = "schedule canceled (queue is shutdown)";
                        TaskLoggerKt.access$log(task2, taskQueue, string);
                    }
                    return;
                }
                boolean $i$f$taskLog = false;
                if (TaskRunner.Companion.getLogger().isLoggable(Level.FINE)) {
                    TaskQueue taskQueue = this;
                    Task task3 = task;
                    boolean bl3 = false;
                    String string = "schedule failed (queue is shutdown)";
                    TaskLoggerKt.access$log(task3, taskQueue, string);
                }
                throw new RejectedExecutionException();
            }
            if (this.scheduleAndDecide$okhttp(task, delayNanos, false)) {
                this.getTaskRunner$okhttp().kickCoordinator$okhttp(this);
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    public static /* synthetic */ void schedule$default(TaskQueue taskQueue, Task task, long l2, int n2, Object object) {
        if ((n2 & 2) != 0) {
            l2 = 0L;
        }
        taskQueue.schedule(task, l2);
    }

    public final void schedule(@NotNull String name, long delayNanos, @NotNull Function0<Long> block) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(block, "block");
        boolean $i$f$schedule = false;
        this.schedule(new Task(name, block){
            final /* synthetic */ String $name;
            final /* synthetic */ Function0<Long> $block;
            {
                this.$name = $name;
                this.$block = $block;
                super($name, false, 2, null);
            }

            public long runOnce() {
                return ((Number)this.$block.invoke()).longValue();
            }
        }, delayNanos);
    }

    public static /* synthetic */ void schedule$default(TaskQueue this_, String name, long delayNanos, Function0 block, int n2, Object object) {
        if ((n2 & 2) != 0) {
            delayNanos = 0L;
        }
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(block, "block");
        boolean $i$f$schedule = false;
        this_.schedule(new /* invalid duplicate definition of identical inner class */, delayNanos);
    }

    public final void execute(@NotNull String name, long delayNanos, boolean cancelable, @NotNull Function0<Unit> block) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(block, "block");
        boolean $i$f$execute = false;
        this.schedule(new Task(name, cancelable, block){
            final /* synthetic */ String $name;
            final /* synthetic */ boolean $cancelable;
            final /* synthetic */ Function0<Unit> $block;
            {
                this.$name = $name;
                this.$cancelable = $cancelable;
                this.$block = $block;
                super($name, $cancelable);
            }

            public long runOnce() {
                this.$block.invoke();
                return -1L;
            }
        }, delayNanos);
    }

    public static /* synthetic */ void execute$default(TaskQueue this_, String name, long delayNanos, boolean cancelable, Function0 block, int n2, Object object) {
        if ((n2 & 2) != 0) {
            delayNanos = 0L;
        }
        if ((n2 & 4) != 0) {
            cancelable = true;
        }
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(block, "block");
        boolean $i$f$execute = false;
        this_.schedule(new /* invalid duplicate definition of identical inner class */, delayNanos);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @NotNull
    public final CountDownLatch idleLatch() {
        TaskRunner taskRunner = this.taskRunner;
        synchronized (taskRunner) {
            block9: {
                block8: {
                    boolean bl = false;
                    if (this.getActiveTask$okhttp() != null || !this.getFutureTasks$okhttp().isEmpty()) break block8;
                    CountDownLatch countDownLatch = new CountDownLatch(0);
                    return countDownLatch;
                }
                Task existingTask = this.getActiveTask$okhttp();
                if (!(existingTask instanceof AwaitIdleTask)) break block9;
                CountDownLatch countDownLatch = ((AwaitIdleTask)existingTask).getLatch();
                return countDownLatch;
            }
            for (Task futureTask : this.getFutureTasks$okhttp()) {
                if (!(futureTask instanceof AwaitIdleTask)) continue;
                CountDownLatch countDownLatch = ((AwaitIdleTask)futureTask).getLatch();
                return countDownLatch;
            }
            AwaitIdleTask newTask = new AwaitIdleTask();
            if (this.scheduleAndDecide$okhttp(newTask, 0L, false)) {
                this.getTaskRunner$okhttp().kickCoordinator$okhttp(this);
            }
            CountDownLatch countDownLatch = newTask.getLatch();
            return countDownLatch;
        }
    }

    public final boolean scheduleAndDecide$okhttp(@NotNull Task task, long delayNanos, boolean recurrence) {
        int insertAt;
        block7: {
            int n2;
            Intrinsics.checkNotNullParameter(task, "task");
            task.initQueue$okhttp(this);
            long now = this.taskRunner.getBackend().nanoTime();
            long executeNanoTime = now + delayNanos;
            int existingIndex = this.futureTasks.indexOf(task);
            if (existingIndex != -1) {
                if (task.getNextExecuteNanoTime$okhttp() <= executeNanoTime) {
                    boolean $i$f$taskLog = false;
                    if (TaskRunner.Companion.getLogger().isLoggable(Level.FINE)) {
                        TaskQueue taskQueue = this;
                        Task task2 = task;
                        boolean bl = false;
                        String string = "already scheduled";
                        TaskLoggerKt.access$log(task2, taskQueue, string);
                    }
                    return false;
                }
                this.futureTasks.remove(existingIndex);
            }
            task.setNextExecuteNanoTime$okhttp(executeNanoTime);
            boolean $i$f$taskLog = false;
            if (TaskRunner.Companion.getLogger().isLoggable(Level.FINE)) {
                TaskQueue taskQueue = this;
                Task task3 = task;
                boolean bl = false;
                String string = recurrence ? Intrinsics.stringPlus("run again after ", TaskLoggerKt.formatDuration(executeNanoTime - now)) : Intrinsics.stringPlus("scheduled after ", TaskLoggerKt.formatDuration(executeNanoTime - now));
                TaskLoggerKt.access$log(task3, taskQueue, string);
            }
            List<Task> $this$indexOfFirst$iv = this.futureTasks;
            boolean $i$f$indexOfFirst = false;
            int index$iv = 0;
            Iterator<Task> iterator2 = $this$indexOfFirst$iv.iterator();
            while (iterator2.hasNext()) {
                Task item$iv;
                Task it = item$iv = iterator2.next();
                boolean bl = false;
                if (it.getNextExecuteNanoTime$okhttp() - now > delayNanos) {
                    n2 = index$iv;
                    break block7;
                }
                ++index$iv;
            }
            n2 = insertAt = -1;
        }
        if (insertAt == -1) {
            insertAt = this.futureTasks.size();
        }
        this.futureTasks.add(insertAt, task);
        return insertAt == 0;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final void cancelAll() {
        TaskQueue $this$assertThreadDoesntHoldLock$iv = this;
        boolean $i$f$assertThreadDoesntHoldLock = false;
        if (Util.assertionsEnabled && Thread.holdsLock($this$assertThreadDoesntHoldLock$iv)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST NOT hold lock on " + $this$assertThreadDoesntHoldLock$iv));
        }
        TaskRunner taskRunner = this.taskRunner;
        synchronized (taskRunner) {
            boolean bl = false;
            if (this.cancelAllAndDecide$okhttp()) {
                this.getTaskRunner$okhttp().kickCoordinator$okhttp(this);
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final void shutdown() {
        TaskQueue $this$assertThreadDoesntHoldLock$iv = this;
        boolean $i$f$assertThreadDoesntHoldLock = false;
        if (Util.assertionsEnabled && Thread.holdsLock($this$assertThreadDoesntHoldLock$iv)) {
            throw new AssertionError((Object)("Thread " + Thread.currentThread().getName() + " MUST NOT hold lock on " + $this$assertThreadDoesntHoldLock$iv));
        }
        TaskRunner taskRunner = this.taskRunner;
        synchronized (taskRunner) {
            boolean bl = false;
            this.setShutdown$okhttp(true);
            if (this.cancelAllAndDecide$okhttp()) {
                this.getTaskRunner$okhttp().kickCoordinator$okhttp(this);
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    public final boolean cancelAllAndDecide$okhttp() {
        if (this.activeTask != null) {
            Task task = this.activeTask;
            Intrinsics.checkNotNull(task);
            if (task.getCancelable()) {
                this.cancelActiveTask = true;
            }
        }
        boolean tasksCanceled = false;
        int n2 = this.futureTasks.size() - 1;
        if (0 <= n2) {
            do {
                int i2;
                if (!this.futureTasks.get(i2 = n2--).getCancelable()) continue;
                Task task$iv = this.futureTasks.get(i2);
                boolean $i$f$taskLog = false;
                if (TaskRunner.Companion.getLogger().isLoggable(Level.FINE)) {
                    TaskQueue taskQueue = this;
                    Task task = task$iv;
                    boolean bl = false;
                    String string = "canceled";
                    TaskLoggerKt.access$log(task, taskQueue, string);
                }
                tasksCanceled = true;
                this.futureTasks.remove(i2);
            } while (0 <= n2);
        }
        return tasksCanceled;
    }

    @NotNull
    public String toString() {
        return this.name;
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0016R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\t"}, d2={"Lokhttp3/internal/concurrent/TaskQueue$AwaitIdleTask;", "Lokhttp3/internal/concurrent/Task;", "()V", "latch", "Ljava/util/concurrent/CountDownLatch;", "getLatch", "()Ljava/util/concurrent/CountDownLatch;", "runOnce", "", "okhttp"})
    private static final class AwaitIdleTask
    extends Task {
        @NotNull
        private final CountDownLatch latch = new CountDownLatch(1);

        public AwaitIdleTask() {
            super(Intrinsics.stringPlus(Util.okHttpName, " awaitIdle"), false);
        }

        @NotNull
        public final CountDownLatch getLatch() {
            return this.latch;
        }

        @Override
        public long runOnce() {
            this.latch.countDown();
            return -1L;
        }
    }
}

