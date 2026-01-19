/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import org.apache.commons.io.function.Uncheck;

@FunctionalInterface
public interface IOIntConsumer {
    public static final IOIntConsumer NOOP = i2 -> {};

    public void accept(int var1) throws IOException;

    default public IOIntConsumer andThen(IOIntConsumer after) {
        Objects.requireNonNull(after);
        return i2 -> {
            this.accept(i2);
            after.accept(i2);
        };
    }

    default public Consumer<Integer> asConsumer() {
        return i2 -> Uncheck.accept(this, i2);
    }

    default public IntConsumer asIntConsumer() {
        return i2 -> Uncheck.accept(this, i2);
    }
}

