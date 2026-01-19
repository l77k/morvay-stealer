/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.sphincsplus;

import java.util.LinkedList;
import org.bouncycastle.pqc.crypto.sphincsplus.ADRS;
import org.bouncycastle.pqc.crypto.sphincsplus.NodeEntry;
import org.bouncycastle.pqc.crypto.sphincsplus.SIG_FORS;
import org.bouncycastle.pqc.crypto.sphincsplus.SPHINCSPlusEngine;
import org.bouncycastle.util.Arrays;

class Fors {
    SPHINCSPlusEngine engine;

    public Fors(SPHINCSPlusEngine sPHINCSPlusEngine) {
        this.engine = sPHINCSPlusEngine;
    }

    byte[] treehash(byte[] byArray, int n2, int n3, byte[] byArray2, ADRS aDRS) {
        if (n2 >>> n3 << n3 != n2) {
            return null;
        }
        LinkedList<NodeEntry> linkedList = new LinkedList<NodeEntry>();
        ADRS aDRS2 = new ADRS(aDRS);
        for (int i2 = 0; i2 < 1 << n3; ++i2) {
            aDRS2.setTypeAndClear(6);
            aDRS2.setKeyPairAddress(aDRS.getKeyPairAddress());
            aDRS2.setTreeHeight(0);
            aDRS2.setTreeIndex(n2 + i2);
            byte[] byArray3 = this.engine.PRF(byArray2, byArray, aDRS2);
            aDRS2.changeType(3);
            byte[] byArray4 = this.engine.F(byArray2, aDRS2, byArray3);
            aDRS2.setTreeHeight(1);
            int n4 = 1;
            int n5 = n2 + i2;
            while (!linkedList.isEmpty() && ((NodeEntry)linkedList.get((int)0)).nodeHeight == n4) {
                n5 = (n5 - 1) / 2;
                aDRS2.setTreeIndex(n5);
                NodeEntry nodeEntry = (NodeEntry)linkedList.remove(0);
                byArray4 = this.engine.H(byArray2, aDRS2, nodeEntry.nodeValue, byArray4);
                aDRS2.setTreeHeight(++n4);
            }
            linkedList.add(0, new NodeEntry(byArray4, n4));
        }
        return ((NodeEntry)linkedList.get((int)0)).nodeValue;
    }

    public SIG_FORS[] sign(byte[] byArray, byte[] byArray2, byte[] byArray3, ADRS aDRS) {
        ADRS aDRS2 = new ADRS(aDRS);
        int[] nArray = Fors.message_to_idxs(byArray, this.engine.K, this.engine.A);
        SIG_FORS[] sIG_FORSArray = new SIG_FORS[this.engine.K];
        int n2 = this.engine.T;
        for (int i2 = 0; i2 < this.engine.K; ++i2) {
            int n3 = nArray[i2];
            aDRS2.setTypeAndClear(6);
            aDRS2.setKeyPairAddress(aDRS.getKeyPairAddress());
            aDRS2.setTreeHeight(0);
            aDRS2.setTreeIndex(i2 * n2 + n3);
            byte[] byArray4 = this.engine.PRF(byArray3, byArray2, aDRS2);
            aDRS2.changeType(3);
            byte[][] byArrayArray = new byte[this.engine.A][];
            for (int i3 = 0; i3 < this.engine.A; ++i3) {
                int n4 = n3 / (1 << i3) ^ 1;
                byArrayArray[i3] = this.treehash(byArray2, i2 * n2 + n4 * (1 << i3), i3, byArray3, aDRS2);
            }
            sIG_FORSArray[i2] = new SIG_FORS(byArray4, byArrayArray);
        }
        return sIG_FORSArray;
    }

    public byte[] pkFromSig(SIG_FORS[] sIG_FORSArray, byte[] byArray, byte[] byArray2, ADRS aDRS) {
        byte[][] byArrayArray = new byte[2][];
        byte[][] byArrayArray2 = new byte[this.engine.K][];
        int n2 = this.engine.T;
        int[] nArray = Fors.message_to_idxs(byArray, this.engine.K, this.engine.A);
        for (int i2 = 0; i2 < this.engine.K; ++i2) {
            int n3 = nArray[i2];
            byte[] byArray3 = sIG_FORSArray[i2].getSK();
            aDRS.setTreeHeight(0);
            aDRS.setTreeIndex(i2 * n2 + n3);
            byArrayArray[0] = this.engine.F(byArray2, aDRS, byArray3);
            byte[][] byArray4 = sIG_FORSArray[i2].getAuthPath();
            aDRS.setTreeIndex(i2 * n2 + n3);
            for (int i3 = 0; i3 < this.engine.A; ++i3) {
                aDRS.setTreeHeight(i3 + 1);
                if (n3 / (1 << i3) % 2 == 0) {
                    aDRS.setTreeIndex(aDRS.getTreeIndex() / 2);
                    byArrayArray[1] = this.engine.H(byArray2, aDRS, byArrayArray[0], byArray4[i3]);
                } else {
                    aDRS.setTreeIndex((aDRS.getTreeIndex() - 1) / 2);
                    byArrayArray[1] = this.engine.H(byArray2, aDRS, byArray4[i3], byArrayArray[0]);
                }
                byArrayArray[0] = byArrayArray[1];
            }
            byArrayArray2[i2] = byArrayArray[0];
        }
        ADRS aDRS2 = new ADRS(aDRS);
        aDRS2.setTypeAndClear(4);
        aDRS2.setKeyPairAddress(aDRS.getKeyPairAddress());
        return this.engine.T_l(byArray2, aDRS2, Arrays.concatenate(byArrayArray2));
    }

    static int[] message_to_idxs(byte[] byArray, int n2, int n3) {
        int n4 = 0;
        int[] nArray = new int[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray[i2] = 0;
            for (int i3 = 0; i3 < n3; ++i3) {
                int n5 = i2;
                nArray[n5] = nArray[n5] ^ (byArray[n4 >> 3] >> (n4 & 7) & 1) << i3;
                ++n4;
            }
        }
        return nArray;
    }
}

