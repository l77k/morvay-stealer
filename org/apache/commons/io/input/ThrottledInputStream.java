/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.commons.io.input.ProxyInputStream;

public final class ThrottledInputStream
extends CountingInputStream {
    private final long maxBytesPerSecond;
    private final long startTime = System.currentTimeMillis();
    private Duration totalSleepDuration = Duration.ZERO;

    public static Builder builder() {
        return new Builder();
    }

    static long toSleepMillis(long bytesRead, long maxBytesPerSec, long elapsedMillis) {
        if (elapsedMillis < 0L) {
            throw new IllegalArgumentException("The elapsed time should be greater or equal to zero");
        }
        if (bytesRead <= 0L || maxBytesPerSec <= 0L || elapsedMillis == 0L) {
            return 0L;
        }
        long millis = (long)((double)bytesRead / (double)maxBytesPerSec * 1000.0 - (double)elapsedMillis);
        if (millis <= 0L) {
            return 0L;
        }
        return millis;
    }

    private ThrottledInputStream(Builder builder) throws IOException {
        super(builder);
        if (builder.maxBytesPerSecond <= 0L) {
            throw new IllegalArgumentException("Bandwidth " + builder.maxBytesPerSecond + " is invalid.");
        }
        this.maxBytesPerSecond = builder.maxBytesPerSecond;
    }

    @Override
    protected void beforeRead(int n2) throws IOException {
        this.throttle();
    }

    private long getBytesPerSecond() {
        long elapsedSeconds = (System.currentTimeMillis() - this.startTime) / 1000L;
        if (elapsedSeconds == 0L) {
            return this.getByteCount();
        }
        return this.getByteCount() / elapsedSeconds;
    }

    private long getSleepMillis() {
        return ThrottledInputStream.toSleepMillis(this.getByteCount(), this.maxBytesPerSecond, System.currentTimeMillis() - this.startTime);
    }

    Duration getTotalSleepDuration() {
        return this.totalSleepDuration;
    }

    private void throttle() throws InterruptedIOException {
        long sleepMillis = this.getSleepMillis();
        if (sleepMillis > 0L) {
            this.totalSleepDuration = this.totalSleepDuration.plus(sleepMillis, ChronoUnit.MILLIS);
            try {
                TimeUnit.MILLISECONDS.sleep(sleepMillis);
            }
            catch (InterruptedException e2) {
                throw new InterruptedIOException("Thread aborted");
            }
        }
    }

    public String toString() {
        return "ThrottledInputStream[bytesRead=" + this.getByteCount() + ", maxBytesPerSec=" + this.maxBytesPerSecond + ", bytesPerSec=" + this.getBytesPerSecond() + ", totalSleepDuration=" + this.totalSleepDuration + ']';
    }

    public static class Builder
    extends ProxyInputStream.AbstractBuilder<ThrottledInputStream, Builder> {
        private long maxBytesPerSecond = Long.MAX_VALUE;

        @Override
        public ThrottledInputStream get() throws IOException {
            return new ThrottledInputStream(this);
        }

        public void setMaxBytesPerSecond(long maxBytesPerSecond) {
            this.maxBytesPerSecond = maxBytesPerSecond;
        }
    }
}

