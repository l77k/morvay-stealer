/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.sphincs;

import org.bouncycastle.pqc.crypto.sphincs.HashFunctions;
import org.bouncycastle.pqc.crypto.sphincs.Seed;

class Wots {
    static final int WOTS_LOGW = 4;
    static final int WOTS_W = 16;
    static final int WOTS_L1 = 64;
    static final int WOTS_L = 67;
    static final int WOTS_LOG_L = 7;
    static final int WOTS_SIGBYTES = 2144;

    Wots() {
    }

    static void expand_seed(byte[] byArray, int n2, byte[] byArray2, int n3) {
        Wots.clear(byArray, n2, 2144);
        Seed.prg(byArray, n2, 2144L, byArray2, n3);
    }

    private static void clear(byte[] byArray, int n2, int n3) {
        for (int i2 = 0; i2 != n3; ++i2) {
            byArray[i2 + n2] = 0;
        }
    }

    static void gen_chain(HashFunctions hashFunctions, byte[] byArray, int n2, byte[] byArray2, int n3, byte[] byArray3, int n4, int n5) {
        for (int i2 = 0; i2 < 32; ++i2) {
            byArray[i2 + n2] = byArray2[i2 + n3];
        }
        for (int i3 = 0; i3 < n5 && i3 < 16; ++i3) {
            hashFunctions.hash_n_n_mask(byArray, n2, byArray, n2, byArray3, n4 + i3 * 32);
        }
    }

    void wots_pkgen(HashFunctions hashFunctions, byte[] byArray, int n2, byte[] byArray2, int n3, byte[] byArray3, int n4) {
        Wots.expand_seed(byArray, n2, byArray2, n3);
        for (int i2 = 0; i2 < 67; ++i2) {
            Wots.gen_chain(hashFunctions, byArray, n2 + i2 * 32, byArray, n2 + i2 * 32, byArray3, n4, 15);
        }
    }

    void wots_sign(HashFunctions hashFunctions, byte[] byArray, int n2, byte[] byArray2, byte[] byArray3, byte[] byArray4) {
        int n3;
        int[] nArray = new int[67];
        int n4 = 0;
        for (n3 = 0; n3 < 64; n3 += 2) {
            nArray[n3] = byArray2[n3 / 2] & 0xF;
            nArray[n3 + 1] = (byArray2[n3 / 2] & 0xFF) >>> 4;
            n4 += 15 - nArray[n3];
            n4 += 15 - nArray[n3 + 1];
        }
        while (n3 < 67) {
            nArray[n3] = n4 & 0xF;
            n4 >>>= 4;
            ++n3;
        }
        Wots.expand_seed(byArray, n2, byArray3, 0);
        for (n3 = 0; n3 < 67; ++n3) {
            Wots.gen_chain(hashFunctions, byArray, n2 + n3 * 32, byArray, n2 + n3 * 32, byArray4, 0, nArray[n3]);
        }
    }

    void wots_verify(HashFunctions hashFunctions, byte[] byArray, byte[] byArray2, int n2, byte[] byArray3, byte[] byArray4) {
        int n3;
        int[] nArray = new int[67];
        int n4 = 0;
        for (n3 = 0; n3 < 64; n3 += 2) {
            nArray[n3] = byArray3[n3 / 2] & 0xF;
            nArray[n3 + 1] = (byArray3[n3 / 2] & 0xFF) >>> 4;
            n4 += 15 - nArray[n3];
            n4 += 15 - nArray[n3 + 1];
        }
        while (n3 < 67) {
            nArray[n3] = n4 & 0xF;
            n4 >>>= 4;
            ++n3;
        }
        for (n3 = 0; n3 < 67; ++n3) {
            Wots.gen_chain(hashFunctions, byArray, n3 * 32, byArray2, n2 + n3 * 32, byArray4, nArray[n3] * 32, 15 - nArray[n3]);
        }
    }
}

