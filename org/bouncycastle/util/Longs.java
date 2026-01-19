/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.util;

public class Longs {
    public static final int BYTES = 8;
    public static final int SIZE = 64;

    public static long highestOneBit(long l2) {
        return Long.highestOneBit(l2);
    }

    public static long lowestOneBit(long l2) {
        return Long.lowestOneBit(l2);
    }

    public static int numberOfLeadingZeros(long l2) {
        return Long.numberOfLeadingZeros(l2);
    }

    public static int numberOfTrailingZeros(long l2) {
        return Long.numberOfTrailingZeros(l2);
    }

    public static long reverse(long l2) {
        return Long.reverse(l2);
    }

    public static long reverseBytes(long l2) {
        return Long.reverseBytes(l2);
    }

    public static long rotateLeft(long l2, int n2) {
        return Long.rotateLeft(l2, n2);
    }

    public static long rotateRight(long l2, int n2) {
        return Long.rotateRight(l2, n2);
    }

    public static Long valueOf(long l2) {
        return l2;
    }
}

