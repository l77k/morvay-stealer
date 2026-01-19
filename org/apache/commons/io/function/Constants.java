/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.function;

import org.apache.commons.io.function.IOBiConsumer;
import org.apache.commons.io.function.IOBiFunction;
import org.apache.commons.io.function.IOFunction;
import org.apache.commons.io.function.IOPredicate;
import org.apache.commons.io.function.IORunnable;
import org.apache.commons.io.function.IOTriConsumer;

final class Constants {
    static final IOBiConsumer IO_BI_CONSUMER = (t2, u2) -> {};
    static final IORunnable IO_RUNNABLE = () -> {};
    static final IOBiFunction IO_BI_FUNCTION = (t2, u2) -> null;
    static final IOFunction IO_FUNCTION_ID = t2 -> t2;
    static final IOPredicate<Object> IO_PREDICATE_FALSE = t2 -> false;
    static final IOPredicate<Object> IO_PREDICATE_TRUE = t2 -> true;
    static final IOTriConsumer IO_TRI_CONSUMER = (t2, u2, v2) -> {};

    private Constants() {
    }
}

