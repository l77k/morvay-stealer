/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Objects;
import org.apache.commons.io.function.IOFunction;

@FunctionalInterface
public interface IOTriFunction<T, U, V, R> {
    default public <W> IOTriFunction<T, U, V, W> andThen(IOFunction<? super R, ? extends W> after) {
        Objects.requireNonNull(after);
        return (t2, u2, v2) -> after.apply((R)this.apply(t2, u2, v2));
    }

    public R apply(T var1, U var2, V var3) throws IOException;
}

