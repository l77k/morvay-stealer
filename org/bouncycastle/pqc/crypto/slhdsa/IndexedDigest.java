/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.slhdsa;

class IndexedDigest {
    final long idx_tree;
    final int idx_leaf;
    final byte[] digest;

    IndexedDigest(long l2, int n2, byte[] byArray) {
        this.idx_tree = l2;
        this.idx_leaf = n2;
        this.digest = byArray;
    }
}

