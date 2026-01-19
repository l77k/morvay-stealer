/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Comparator;
import org.apache.commons.io.function.Uncheck;

@FunctionalInterface
public interface IOComparator<T> {
    default public Comparator<T> asComparator() {
        return (t2, u2) -> Uncheck.compare(this, t2, u2);
    }

    public int compare(T var1, T var2) throws IOException;
}

