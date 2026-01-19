/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.util;

public class Integers {
    public static final int BYTES = 4;
    public static final int SIZE = 32;

    public static int bitCount(int n2) {
        return Integer.bitCount(n2);
    }

    public static int highestOneBit(int n2) {
        return Integer.highestOneBit(n2);
    }

    public static int lowestOneBit(int n2) {
        return Integer.lowestOneBit(n2);
    }

    public static int numberOfLeadingZeros(int n2) {
        return Integer.numberOfLeadingZeros(n2);
    }

    public static int numberOfTrailingZeros(int n2) {
        return Integer.numberOfTrailingZeros(n2);
    }

    public static int reverse(int n2) {
        return Integer.reverse(n2);
    }

    public static int reverseBytes(int n2) {
        return Integer.reverseBytes(n2);
    }

    public static int rotateLeft(int n2, int n3) {
        return Integer.rotateLeft(n2, n3);
    }

    public static int rotateRight(int n2, int n3) {
        return Integer.rotateRight(n2, n3);
    }

    public static Integer valueOf(int n2) {
        return n2;
    }
}

