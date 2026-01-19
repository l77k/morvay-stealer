/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.sphincsplus;

import java.util.LinkedList;
import org.bouncycastle.pqc.crypto.sphincsplus.ADRS;
import org.bouncycastle.pqc.crypto.sphincsplus.NodeEntry;
import org.bouncycastle.pqc.crypto.sphincsplus.SIG_XMSS;
import org.bouncycastle.pqc.crypto.sphincsplus.SPHINCSPlusEngine;
import org.bouncycastle.pqc.crypto.sphincsplus.WotsPlus;
import org.bouncycastle.util.Arrays;

class HT {
    private final byte[] skSeed;
    private final byte[] pkSeed;
    SPHINCSPlusEngine engine;
    WotsPlus wots;
    final byte[] htPubKey;

    public HT(SPHINCSPlusEngine sPHINCSPlusEngine, byte[] byArray, byte[] byArray2) {
        this.skSeed = byArray;
        this.pkSeed = byArray2;
        this.engine = sPHINCSPlusEngine;
        this.wots = new WotsPlus(sPHINCSPlusEngine);
        ADRS aDRS = new ADRS();
        aDRS.setLayerAddress(sPHINCSPlusEngine.D - 1);
        aDRS.setTreeAddress(0L);
        this.htPubKey = (byte[])(byArray != null ? this.xmss_PKgen(byArray, byArray2, aDRS) : null);
    }

    byte[] sign(byte[] byArray, long l2, int n2) {
        ADRS aDRS = new ADRS();
        aDRS.setLayerAddress(0);
        aDRS.setTreeAddress(l2);
        SIG_XMSS sIG_XMSS = this.xmss_sign(byArray, this.skSeed, n2, this.pkSeed, aDRS);
        SIG_XMSS[] sIG_XMSSArray = new SIG_XMSS[this.engine.D];
        sIG_XMSSArray[0] = sIG_XMSS;
        aDRS.setLayerAddress(0);
        aDRS.setTreeAddress(l2);
        byte[] byArray2 = this.xmss_pkFromSig(n2, sIG_XMSS, byArray, this.pkSeed, aDRS);
        for (int i2 = 1; i2 < this.engine.D; ++i2) {
            n2 = (int)(l2 & (long)((1 << this.engine.H_PRIME) - 1));
            aDRS.setLayerAddress(i2);
            aDRS.setTreeAddress(l2 >>>= this.engine.H_PRIME);
            sIG_XMSSArray[i2] = sIG_XMSS = this.xmss_sign(byArray2, this.skSeed, n2, this.pkSeed, aDRS);
            if (i2 >= this.engine.D - 1) continue;
            byArray2 = this.xmss_pkFromSig(n2, sIG_XMSS, byArray2, this.pkSeed, aDRS);
        }
        byte[][] byArrayArray = new byte[sIG_XMSSArray.length][];
        for (int i3 = 0; i3 != byArrayArray.length; ++i3) {
            byArrayArray[i3] = Arrays.concatenate(sIG_XMSSArray[i3].sig, Arrays.concatenate(sIG_XMSSArray[i3].auth));
        }
        return Arrays.concatenate(byArrayArray);
    }

    byte[] xmss_PKgen(byte[] byArray, byte[] byArray2, ADRS aDRS) {
        return this.treehash(byArray, 0, this.engine.H_PRIME, byArray2, aDRS);
    }

    byte[] xmss_pkFromSig(int n2, SIG_XMSS sIG_XMSS, byte[] byArray, byte[] byArray2, ADRS aDRS) {
        ADRS aDRS2 = new ADRS(aDRS);
        aDRS2.setTypeAndClear(0);
        aDRS2.setKeyPairAddress(n2);
        byte[] byArray3 = sIG_XMSS.getWOTSSig();
        byte[][] byArray4 = sIG_XMSS.getXMSSAUTH();
        byte[] byArray5 = this.wots.pkFromSig(byArray3, byArray, byArray2, aDRS2);
        byte[] byArray6 = null;
        aDRS2.setTypeAndClear(2);
        aDRS2.setTreeIndex(n2);
        for (int i2 = 0; i2 < this.engine.H_PRIME; ++i2) {
            aDRS2.setTreeHeight(i2 + 1);
            if (n2 / (1 << i2) % 2 == 0) {
                aDRS2.setTreeIndex(aDRS2.getTreeIndex() / 2);
                byArray6 = this.engine.H(byArray2, aDRS2, byArray5, byArray4[i2]);
            } else {
                aDRS2.setTreeIndex((aDRS2.getTreeIndex() - 1) / 2);
                byArray6 = this.engine.H(byArray2, aDRS2, byArray4[i2], byArray5);
            }
            byArray5 = byArray6;
        }
        return byArray5;
    }

    SIG_XMSS xmss_sign(byte[] byArray, byte[] byArray2, int n2, byte[] byArray3, ADRS aDRS) {
        byte[][] byArrayArray = new byte[this.engine.H_PRIME][];
        ADRS aDRS2 = new ADRS(aDRS);
        aDRS2.setTypeAndClear(2);
        aDRS2.setLayerAddress(aDRS.getLayerAddress());
        aDRS2.setTreeAddress(aDRS.getTreeAddress());
        for (int i2 = 0; i2 < this.engine.H_PRIME; ++i2) {
            int n3 = n2 >>> i2 ^ 1;
            byArrayArray[i2] = this.treehash(byArray2, n3 << i2, i2, byArray3, aDRS2);
        }
        aDRS2 = new ADRS(aDRS);
        aDRS2.setTypeAndClear(0);
        aDRS2.setKeyPairAddress(n2);
        byte[] byArray4 = this.wots.sign(byArray, byArray2, byArray3, aDRS2);
        return new SIG_XMSS(byArray4, byArrayArray);
    }

    byte[] treehash(byte[] byArray, int n2, int n3, byte[] byArray2, ADRS aDRS) {
        if (n2 >>> n3 << n3 != n2) {
            return null;
        }
        LinkedList<NodeEntry> linkedList = new LinkedList<NodeEntry>();
        ADRS aDRS2 = new ADRS(aDRS);
        for (int i2 = 0; i2 < 1 << n3; ++i2) {
            aDRS2.setTypeAndClear(0);
            aDRS2.setKeyPairAddress(n2 + i2);
            byte[] byArray3 = this.wots.pkGen(byArray, byArray2, aDRS2);
            aDRS2.setTypeAndClear(2);
            aDRS2.setTreeHeight(1);
            aDRS2.setTreeIndex(n2 + i2);
            int n4 = 1;
            int n5 = n2 + i2;
            while (!linkedList.isEmpty() && ((NodeEntry)linkedList.get((int)0)).nodeHeight == n4) {
                n5 = (n5 - 1) / 2;
                aDRS2.setTreeIndex(n5);
                NodeEntry nodeEntry = (NodeEntry)linkedList.remove(0);
                byArray3 = this.engine.H(byArray2, aDRS2, nodeEntry.nodeValue, byArray3);
                aDRS2.setTreeHeight(++n4);
            }
            linkedList.add(0, new NodeEntry(byArray3, n4));
        }
        return ((NodeEntry)linkedList.get((int)0)).nodeValue;
    }

    public boolean verify(byte[] byArray, SIG_XMSS[] sIG_XMSSArray, byte[] byArray2, long l2, int n2, byte[] byArray3) {
        ADRS aDRS = new ADRS();
        SIG_XMSS sIG_XMSS = sIG_XMSSArray[0];
        aDRS.setLayerAddress(0);
        aDRS.setTreeAddress(l2);
        byte[] byArray4 = this.xmss_pkFromSig(n2, sIG_XMSS, byArray, byArray2, aDRS);
        for (int i2 = 1; i2 < this.engine.D; ++i2) {
            n2 = (int)(l2 & (long)((1 << this.engine.H_PRIME) - 1));
            sIG_XMSS = sIG_XMSSArray[i2];
            aDRS.setLayerAddress(i2);
            aDRS.setTreeAddress(l2 >>>= this.engine.H_PRIME);
            byArray4 = this.xmss_pkFromSig(n2, sIG_XMSS, byArray4, byArray2, aDRS);
        }
        return Arrays.areEqual(byArray3, byArray4);
    }
}

