/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.crystals.dilithium;

class Reduce {
    Reduce() {
    }

    static int montgomeryReduce(long l2) {
        int n2 = (int)(l2 * 58728449L);
        n2 = (int)(l2 - (long)n2 * 8380417L >>> 32);
        return n2;
    }

    static int reduce32(int n2) {
        int n3 = n2 + 0x400000 >> 23;
        n3 = n2 - n3 * 8380417;
        return n3;
    }

    static int conditionalAddQ(int n2) {
        n2 += n2 >> 31 & 0x7FE001;
        return n2;
    }
}

