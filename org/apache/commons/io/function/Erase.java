/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.function;

import java.io.IOException;
import org.apache.commons.io.function.IOBiConsumer;
import org.apache.commons.io.function.IOBiFunction;
import org.apache.commons.io.function.IOComparator;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.function.IOFunction;
import org.apache.commons.io.function.IOPredicate;
import org.apache.commons.io.function.IORunnable;
import org.apache.commons.io.function.IOSupplier;

public final class Erase {
    static <T, U> void accept(IOBiConsumer<T, U> consumer, T t2, U u2) {
        try {
            consumer.accept(t2, u2);
        }
        catch (IOException ex) {
            Erase.rethrow(ex);
        }
    }

    static <T> void accept(IOConsumer<T> consumer, T t2) {
        try {
            consumer.accept(t2);
        }
        catch (IOException ex) {
            Erase.rethrow(ex);
        }
    }

    static <T, U, R> R apply(IOBiFunction<? super T, ? super U, ? extends R> mapper, T t2, U u2) {
        try {
            return mapper.apply(t2, u2);
        }
        catch (IOException e2) {
            throw Erase.rethrow(e2);
        }
    }

    static <T, R> R apply(IOFunction<? super T, ? extends R> mapper, T t2) {
        try {
            return mapper.apply(t2);
        }
        catch (IOException e2) {
            throw Erase.rethrow(e2);
        }
    }

    static <T> int compare(IOComparator<? super T> comparator, T t2, T u2) {
        try {
            return comparator.compare(t2, u2);
        }
        catch (IOException e2) {
            throw Erase.rethrow(e2);
        }
    }

    static <T> T get(IOSupplier<T> supplier) {
        try {
            return supplier.get();
        }
        catch (IOException e2) {
            throw Erase.rethrow(e2);
        }
    }

    public static <T extends Throwable> RuntimeException rethrow(Throwable throwable) throws T {
        throw throwable;
    }

    static void run(IORunnable runnable2) {
        try {
            runnable2.run();
        }
        catch (IOException e2) {
            throw Erase.rethrow(e2);
        }
    }

    static <T> boolean test(IOPredicate<? super T> predicate, T t2) {
        try {
            return predicate.test(t2);
        }
        catch (IOException e2) {
            throw Erase.rethrow(e2);
        }
    }

    private Erase() {
    }
}

