/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Supplier;
import org.apache.commons.io.function.IOBiConsumer;
import org.apache.commons.io.function.IOBiFunction;
import org.apache.commons.io.function.IOComparator;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.function.IOFunction;
import org.apache.commons.io.function.IOIntConsumer;
import org.apache.commons.io.function.IOIntSupplier;
import org.apache.commons.io.function.IOLongSupplier;
import org.apache.commons.io.function.IOPredicate;
import org.apache.commons.io.function.IOQuadFunction;
import org.apache.commons.io.function.IORunnable;
import org.apache.commons.io.function.IOSupplier;
import org.apache.commons.io.function.IOTriConsumer;
import org.apache.commons.io.function.IOTriFunction;

public final class Uncheck {
    public static <T, U> void accept(IOBiConsumer<T, U> consumer, T t2, U u2) {
        try {
            consumer.accept(t2, u2);
        }
        catch (IOException e2) {
            throw Uncheck.wrap(e2);
        }
    }

    public static <T> void accept(IOConsumer<T> consumer, T t2) {
        try {
            consumer.accept(t2);
        }
        catch (IOException e2) {
            throw Uncheck.wrap(e2);
        }
    }

    public static void accept(IOIntConsumer consumer, int i2) {
        try {
            consumer.accept(i2);
        }
        catch (IOException e2) {
            throw Uncheck.wrap(e2);
        }
    }

    public static <T, U, V> void accept(IOTriConsumer<T, U, V> consumer, T t2, U u2, V v2) {
        try {
            consumer.accept(t2, u2, v2);
        }
        catch (IOException e2) {
            throw Uncheck.wrap(e2);
        }
    }

    public static <T, U, R> R apply(IOBiFunction<T, U, R> function, T t2, U u2) {
        try {
            return function.apply(t2, u2);
        }
        catch (IOException e2) {
            throw Uncheck.wrap(e2);
        }
    }

    public static <T, R> R apply(IOFunction<T, R> function, T t2) {
        try {
            return function.apply(t2);
        }
        catch (IOException e2) {
            throw Uncheck.wrap(e2);
        }
    }

    public static <T, U, V, W, R> R apply(IOQuadFunction<T, U, V, W, R> function, T t2, U u2, V v2, W w2) {
        try {
            return function.apply(t2, u2, v2, w2);
        }
        catch (IOException e2) {
            throw Uncheck.wrap(e2);
        }
    }

    public static <T, U, V, R> R apply(IOTriFunction<T, U, V, R> function, T t2, U u2, V v2) {
        try {
            return function.apply(t2, u2, v2);
        }
        catch (IOException e2) {
            throw Uncheck.wrap(e2);
        }
    }

    public static <T> int compare(IOComparator<T> comparator, T t2, T u2) {
        try {
            return comparator.compare(t2, u2);
        }
        catch (IOException e2) {
            throw Uncheck.wrap(e2);
        }
    }

    public static <T> T get(IOSupplier<T> supplier) {
        try {
            return supplier.get();
        }
        catch (IOException e2) {
            throw Uncheck.wrap(e2);
        }
    }

    public static <T> T get(IOSupplier<T> supplier, Supplier<String> message) {
        try {
            return supplier.get();
        }
        catch (IOException e2) {
            throw Uncheck.wrap(e2, message);
        }
    }

    public static int getAsInt(IOIntSupplier supplier) {
        try {
            return supplier.getAsInt();
        }
        catch (IOException e2) {
            throw Uncheck.wrap(e2);
        }
    }

    public static int getAsInt(IOIntSupplier supplier, Supplier<String> message) {
        try {
            return supplier.getAsInt();
        }
        catch (IOException e2) {
            throw Uncheck.wrap(e2, message);
        }
    }

    public static long getAsLong(IOLongSupplier supplier) {
        try {
            return supplier.getAsLong();
        }
        catch (IOException e2) {
            throw Uncheck.wrap(e2);
        }
    }

    public static long getAsLong(IOLongSupplier supplier, Supplier<String> message) {
        try {
            return supplier.getAsLong();
        }
        catch (IOException e2) {
            throw Uncheck.wrap(e2, message);
        }
    }

    public static void run(IORunnable runnable2) {
        try {
            runnable2.run();
        }
        catch (IOException e2) {
            throw Uncheck.wrap(e2);
        }
    }

    public static void run(IORunnable runnable2, Supplier<String> message) {
        try {
            runnable2.run();
        }
        catch (IOException e2) {
            throw Uncheck.wrap(e2, message);
        }
    }

    public static <T> boolean test(IOPredicate<T> predicate, T t2) {
        try {
            return predicate.test(t2);
        }
        catch (IOException e2) {
            throw Uncheck.wrap(e2);
        }
    }

    private static UncheckedIOException wrap(IOException e2) {
        return new UncheckedIOException(e2);
    }

    private static UncheckedIOException wrap(IOException e2, Supplier<String> message) {
        return new UncheckedIOException(message.get(), e2);
    }

    private Uncheck() {
    }
}

