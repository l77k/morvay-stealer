/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.raw;

public abstract class Bits {
    public static int bitPermuteStep(int n2, int n3, int n4) {
        int n5 = (n2 ^ n2 >>> n4) & n3;
        return n5 ^ n5 << n4 ^ n2;
    }

    public static long bitPermuteStep(long l2, long l3, int n2) {
        long l4 = (l2 ^ l2 >>> n2) & l3;
        return l4 ^ l4 << n2 ^ l2;
    }

    public static int bitPermuteStepSimple(int n2, int n3, int n4) {
        return (n2 & n3) << n4 | n2 >>> n4 & n3;
    }

    public static long bitPermuteStepSimple(long l2, long l3, int n2) {
        return (l2 & l3) << n2 | l2 >>> n2 & l3;
    }
}

