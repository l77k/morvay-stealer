/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.sphincs;

import org.bouncycastle.crypto.engines.ChaChaEngine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.pqc.crypto.sphincs.HashFunctions;
import org.bouncycastle.pqc.crypto.sphincs.Tree;
import org.bouncycastle.util.Pack;

class Seed {
    Seed() {
    }

    static void get_seed(HashFunctions hashFunctions, byte[] byArray, int n2, byte[] byArray2, Tree.leafaddr leafaddr2) {
        byte[] byArray3 = new byte[40];
        for (int i2 = 0; i2 < 32; ++i2) {
            byArray3[i2] = byArray2[i2];
        }
        long l2 = leafaddr2.level;
        l2 |= leafaddr2.subtree << 4;
        Pack.longToLittleEndian(l2 |= leafaddr2.subleaf << 59, byArray3, 32);
        hashFunctions.varlen_hash(byArray, n2, byArray3, byArray3.length);
    }

    static void prg(byte[] byArray, int n2, long l2, byte[] byArray2, int n3) {
        byte[] byArray3 = new byte[8];
        ChaChaEngine chaChaEngine = new ChaChaEngine(12);
        chaChaEngine.init(true, new ParametersWithIV(new KeyParameter(byArray2, n3, 32), byArray3));
        chaChaEngine.processBytes(byArray, n2, (int)l2, byArray, n2);
    }
}

