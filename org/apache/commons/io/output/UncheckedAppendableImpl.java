/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.output;

import java.util.Objects;
import org.apache.commons.io.function.Uncheck;
import org.apache.commons.io.output.UncheckedAppendable;

final class UncheckedAppendableImpl
implements UncheckedAppendable {
    private final Appendable appendable;

    UncheckedAppendableImpl(Appendable appendable) {
        this.appendable = Objects.requireNonNull(appendable, "appendable");
    }

    @Override
    public UncheckedAppendable append(char c2) {
        Uncheck.apply(this.appendable::append, Character.valueOf(c2));
        return this;
    }

    @Override
    public UncheckedAppendable append(CharSequence csq) {
        Uncheck.apply(this.appendable::append, csq);
        return this;
    }

    @Override
    public UncheckedAppendable append(CharSequence csq, int start, int end) {
        Uncheck.apply(this.appendable::append, csq, start, end);
        return this;
    }

    public String toString() {
        return this.appendable.toString();
    }
}

