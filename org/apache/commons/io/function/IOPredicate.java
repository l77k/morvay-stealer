/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Predicate;
import org.apache.commons.io.function.Constants;
import org.apache.commons.io.function.Uncheck;

@FunctionalInterface
public interface IOPredicate<T> {
    public static <T> IOPredicate<T> alwaysFalse() {
        return Constants.IO_PREDICATE_FALSE;
    }

    public static <T> IOPredicate<T> alwaysTrue() {
        return Constants.IO_PREDICATE_TRUE;
    }

    public static <T> IOPredicate<T> isEqual(Object target) {
        return null == target ? Objects::isNull : object -> target.equals(object);
    }

    default public IOPredicate<T> and(IOPredicate<? super T> other) {
        Objects.requireNonNull(other);
        return t2 -> this.test(t2) && other.test(t2);
    }

    default public Predicate<T> asPredicate() {
        return t2 -> Uncheck.test(this, t2);
    }

    default public IOPredicate<T> negate() {
        return t2 -> !this.test(t2);
    }

    default public IOPredicate<T> or(IOPredicate<? super T> other) {
        Objects.requireNonNull(other);
        return t2 -> this.test(t2) || other.test(t2);
    }

    public boolean test(T var1) throws IOException;
}

