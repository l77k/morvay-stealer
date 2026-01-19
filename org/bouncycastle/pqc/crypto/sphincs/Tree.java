/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.sphincs;

import org.bouncycastle.pqc.crypto.sphincs.HashFunctions;
import org.bouncycastle.pqc.crypto.sphincs.Seed;
import org.bouncycastle.pqc.crypto.sphincs.Wots;

class Tree {
    Tree() {
    }

    static void l_tree(HashFunctions hashFunctions, byte[] byArray, int n2, byte[] byArray2, int n3, byte[] byArray3, int n4) {
        int n5 = 67;
        int n6 = 0;
        for (int i2 = 0; i2 < 7; ++i2) {
            for (n6 = 0; n6 < n5 >>> 1; ++n6) {
                hashFunctions.hash_2n_n_mask(byArray2, n3 + n6 * 32, byArray2, n3 + n6 * 2 * 32, byArray3, n4 + i2 * 2 * 32);
            }
            if ((n5 & 1) != 0) {
                System.arraycopy(byArray2, n3 + (n5 - 1) * 32, byArray2, n3 + (n5 >>> 1) * 32, 32);
                n5 = (n5 >>> 1) + 1;
                continue;
            }
            n5 >>>= 1;
        }
        System.arraycopy(byArray2, n3, byArray, n2, 32);
    }

    static void treehash(HashFunctions hashFunctions, byte[] byArray, int n2, int n3, byte[] byArray2, leafaddr leafaddr2, byte[] byArray3, int n4) {
        leafaddr leafaddr3 = new leafaddr(leafaddr2);
        byte[] byArray4 = new byte[(n3 + 1) * 32];
        int[] nArray = new int[n3 + 1];
        int n5 = 0;
        int n6 = (int)(leafaddr3.subleaf + (long)(1 << n3));
        while (leafaddr3.subleaf < (long)n6) {
            Tree.gen_leaf_wots(hashFunctions, byArray4, n5 * 32, byArray3, n4, byArray2, leafaddr3);
            nArray[n5] = 0;
            ++n5;
            while (n5 > 1 && nArray[n5 - 1] == nArray[n5 - 2]) {
                int n7 = 2 * (nArray[n5 - 1] + 7) * 32;
                hashFunctions.hash_2n_n_mask(byArray4, (n5 - 2) * 32, byArray4, (n5 - 2) * 32, byArray3, n4 + n7);
                int n8 = n5 - 2;
                nArray[n8] = nArray[n8] + 1;
                --n5;
            }
            ++leafaddr3.subleaf;
        }
        for (int i2 = 0; i2 < 32; ++i2) {
            byArray[n2 + i2] = byArray4[i2];
        }
    }

    static void gen_leaf_wots(HashFunctions hashFunctions, byte[] byArray, int n2, byte[] byArray2, int n3, byte[] byArray3, leafaddr leafaddr2) {
        byte[] byArray4 = new byte[32];
        byte[] byArray5 = new byte[2144];
        Wots wots = new Wots();
        Seed.get_seed(hashFunctions, byArray4, 0, byArray3, leafaddr2);
        wots.wots_pkgen(hashFunctions, byArray5, 0, byArray4, 0, byArray2, n3);
        Tree.l_tree(hashFunctions, byArray, n2, byArray5, 0, byArray2, n3);
    }

    static class leafaddr {
        int level;
        long subtree;
        long subleaf;

        public leafaddr() {
        }

        public leafaddr(leafaddr leafaddr2) {
            this.level = leafaddr2.level;
            this.subtree = leafaddr2.subtree;
            this.subleaf = leafaddr2.subleaf;
        }
    }
}

