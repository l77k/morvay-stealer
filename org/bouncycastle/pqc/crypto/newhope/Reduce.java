/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.newhope;

class Reduce {
    static final int QInv = 12287;
    static final int RLog = 18;
    static final int RMask = 262143;

    Reduce() {
    }

    static short montgomery(int n2) {
        int n3 = n2 * 12287;
        n3 &= 0x3FFFF;
        n3 *= 12289;
        return (short)((n3 += n2) >>> 18);
    }

    static short barrett(short s2) {
        int n2 = s2 & 0xFFFF;
        int n3 = n2 * 5 >>> 16;
        return (short)(n2 - (n3 *= 12289));
    }
}

