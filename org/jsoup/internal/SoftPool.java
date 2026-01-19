/*
 * Decompiled with CFR 0.152.
 */
package org.jsoup.internal;

import java.lang.ref.SoftReference;
import java.util.ArrayDeque;
import java.util.function.Supplier;

public class SoftPool<T> {
    final ThreadLocal<SoftReference<ArrayDeque<T>>> threadLocalStack;
    private final Supplier<T> initializer;
    static final int MaxIdle = 12;

    public SoftPool(Supplier<T> initializer) {
        this.initializer = initializer;
        this.threadLocalStack = ThreadLocal.withInitial(() -> new SoftReference(new ArrayDeque()));
    }

    public T borrow() {
        ArrayDeque<T> stack = this.getStack();
        if (!stack.isEmpty()) {
            return stack.pop();
        }
        return this.initializer.get();
    }

    public void release(T value) {
        ArrayDeque<T> stack = this.getStack();
        if (stack.size() < 12) {
            stack.push(value);
        }
    }

    ArrayDeque<T> getStack() {
        ArrayDeque<Object> stack = this.threadLocalStack.get().get();
        if (stack == null) {
            stack = new ArrayDeque();
            this.threadLocalStack.set(new SoftReference<ArrayDeque<T>>(stack));
        }
        return stack;
    }
}

