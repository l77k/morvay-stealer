/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Objects;
import java.util.function.BiConsumer;
import org.apache.commons.io.function.Constants;
import org.apache.commons.io.function.Uncheck;

@FunctionalInterface
public interface IOBiConsumer<T, U> {
    public static <T, U> IOBiConsumer<T, U> noop() {
        return Constants.IO_BI_CONSUMER;
    }

    public void accept(T var1, U var2) throws IOException;

    default public IOBiConsumer<T, U> andThen(IOBiConsumer<? super T, ? super U> after) {
        Objects.requireNonNull(after);
        return (t2, u2) -> {
            this.accept(t2, u2);
            after.accept(t2, u2);
        };
    }

    default public BiConsumer<T, U> asBiConsumer() {
        return (t2, u2) -> Uncheck.accept(this, t2, u2);
    }
}

