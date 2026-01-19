/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.gemss;

public class GeMSSUtils {
    static long NORBITS_UINT(long l2) {
        l2 |= l2 << 32;
        l2 >>>= 32;
        return --l2 >>> 63;
    }

    static long XORBITS_UINT(long l2) {
        l2 ^= l2 << 1;
        l2 ^= l2 << 2;
        return (l2 & 0x8888888888888888L) * 0x1111111111111111L >>> 63;
    }

    static long ORBITS_UINT(long l2) {
        l2 |= l2 << 32;
        l2 >>>= 32;
        return (l2 += 0xFFFFFFFFL) >>> 32;
    }

    static long CMP_LT_UINT(long l2, long l3) {
        return (l2 >>> 63 ^ l3 >>> 63) & (l2 >>> 63) - (l3 >>> 63) >>> 63 ^ (l2 >>> 63 ^ l3 >>> 63 ^ 1L) & (l2 & Long.MAX_VALUE) - (l3 & Long.MAX_VALUE) >>> 63;
    }

    static long maskUINT(int n2) {
        return n2 != 0 ? (1L << n2) - 1L : -1L;
    }

    static int Highest_One(int n2) {
        n2 |= n2 >>> 1;
        n2 |= n2 >>> 2;
        n2 |= n2 >>> 4;
        n2 |= n2 >>> 8;
        n2 |= n2 >>> 16;
        return n2 ^ n2 >>> 1;
    }
}

