/*
 * Decompiled with CFR 0.152.
 */
package org.java_websocket.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory
implements ThreadFactory {
    private final ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String threadPrefix;
    private final boolean daemon;

    public NamedThreadFactory(String threadPrefix) {
        this.threadPrefix = threadPrefix;
        this.daemon = false;
    }

    public NamedThreadFactory(String threadPrefix, boolean daemon) {
        this.threadPrefix = threadPrefix;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable runnable2) {
        Thread thread2 = this.defaultThreadFactory.newThread(runnable2);
        thread2.setDaemon(this.daemon);
        thread2.setName(this.threadPrefix + "-" + this.threadNumber);
        return thread2;
    }
}

