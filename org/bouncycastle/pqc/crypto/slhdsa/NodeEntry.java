/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.slhdsa;

class NodeEntry {
    final byte[] nodeValue;
    final int nodeHeight;

    NodeEntry(byte[] byArray, int n2) {
        this.nodeValue = byArray;
        this.nodeHeight = n2;
    }
}

