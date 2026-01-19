/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.qtesla;

final class IntSlicer {
    private final int[] values;
    private int base;

    IntSlicer(int[] nArray, int n2) {
        this.values = nArray;
        this.base = n2;
    }

    final int at(int n2) {
        return this.values[this.base + n2];
    }

    final int at(int n2, int n3) {
        int n4 = n3;
        this.values[this.base + n2] = n4;
        return n4;
    }

    final int at(int n2, long l2) {
        int n3 = (int)l2;
        this.values[this.base + n2] = n3;
        return n3;
    }

    final IntSlicer from(int n2) {
        return new IntSlicer(this.values, this.base + n2);
    }

    final void incBase(int n2) {
        this.base += n2;
    }

    final IntSlicer copy() {
        return new IntSlicer(this.values, this.base);
    }
}

