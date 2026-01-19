/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.function;

import java.util.Objects;
import java.util.function.BinaryOperator;
import org.apache.commons.io.function.IOBiFunction;
import org.apache.commons.io.function.IOComparator;
import org.apache.commons.io.function.Uncheck;

@FunctionalInterface
public interface IOBinaryOperator<T>
extends IOBiFunction<T, T, T> {
    public static <T> IOBinaryOperator<T> maxBy(IOComparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return (a2, b2) -> comparator.compare(a2, b2) >= 0 ? a2 : b2;
    }

    public static <T> IOBinaryOperator<T> minBy(IOComparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return (a2, b2) -> comparator.compare(a2, b2) <= 0 ? a2 : b2;
    }

    default public BinaryOperator<T> asBinaryOperator() {
        return (t2, u2) -> Uncheck.apply(this, t2, u2);
    }
}

