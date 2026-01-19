/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.gmss;

import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle.pqc.legacy.crypto.gmss.GMSSLeaf;
import org.bouncycastle.pqc.legacy.crypto.gmss.GMSSRootCalc;
import org.bouncycastle.pqc.legacy.crypto.gmss.GMSSRootSig;
import org.bouncycastle.pqc.legacy.crypto.gmss.Treehash;
import org.bouncycastle.util.Arrays;

class GMSSUtils {
    GMSSUtils() {
    }

    static GMSSLeaf[] clone(GMSSLeaf[] gMSSLeafArray) {
        if (gMSSLeafArray == null) {
            return null;
        }
        GMSSLeaf[] gMSSLeafArray2 = new GMSSLeaf[gMSSLeafArray.length];
        System.arraycopy(gMSSLeafArray, 0, gMSSLeafArray2, 0, gMSSLeafArray.length);
        return gMSSLeafArray2;
    }

    static GMSSRootCalc[] clone(GMSSRootCalc[] gMSSRootCalcArray) {
        if (gMSSRootCalcArray == null) {
            return null;
        }
        GMSSRootCalc[] gMSSRootCalcArray2 = new GMSSRootCalc[gMSSRootCalcArray.length];
        System.arraycopy(gMSSRootCalcArray, 0, gMSSRootCalcArray2, 0, gMSSRootCalcArray.length);
        return gMSSRootCalcArray2;
    }

    static GMSSRootSig[] clone(GMSSRootSig[] gMSSRootSigArray) {
        if (gMSSRootSigArray == null) {
            return null;
        }
        GMSSRootSig[] gMSSRootSigArray2 = new GMSSRootSig[gMSSRootSigArray.length];
        System.arraycopy(gMSSRootSigArray, 0, gMSSRootSigArray2, 0, gMSSRootSigArray.length);
        return gMSSRootSigArray2;
    }

    static byte[][] clone(byte[][] byArray) {
        if (byArray == null) {
            return null;
        }
        byte[][] byArrayArray = new byte[byArray.length][];
        for (int i2 = 0; i2 != byArray.length; ++i2) {
            byArrayArray[i2] = Arrays.clone(byArray[i2]);
        }
        return byArrayArray;
    }

    static byte[][][] clone(byte[][][] byArray) {
        if (byArray == null) {
            return null;
        }
        byte[][][] byArrayArray = new byte[byArray.length][][];
        for (int i2 = 0; i2 != byArray.length; ++i2) {
            byArrayArray[i2] = GMSSUtils.clone(byArray[i2]);
        }
        return byArrayArray;
    }

    static Treehash[] clone(Treehash[] treehashArray) {
        if (treehashArray == null) {
            return null;
        }
        Treehash[] treehashArray2 = new Treehash[treehashArray.length];
        System.arraycopy(treehashArray, 0, treehashArray2, 0, treehashArray.length);
        return treehashArray2;
    }

    static Treehash[][] clone(Treehash[][] treehashArray) {
        if (treehashArray == null) {
            return null;
        }
        Treehash[][] treehashArray2 = new Treehash[treehashArray.length][];
        for (int i2 = 0; i2 != treehashArray.length; ++i2) {
            treehashArray2[i2] = GMSSUtils.clone(treehashArray[i2]);
        }
        return treehashArray2;
    }

    static Vector[] clone(Vector[] vectorArray) {
        if (vectorArray == null) {
            return null;
        }
        Vector[] vectorArray2 = new Vector[vectorArray.length];
        for (int i2 = 0; i2 != vectorArray.length; ++i2) {
            vectorArray2[i2] = new Vector();
            Enumeration enumeration = vectorArray[i2].elements();
            while (enumeration.hasMoreElements()) {
                vectorArray2[i2].addElement(enumeration.nextElement());
            }
        }
        return vectorArray2;
    }

    static Vector[][] clone(Vector[][] vectorArray) {
        if (vectorArray == null) {
            return null;
        }
        Vector[][] vectorArray2 = new Vector[vectorArray.length][];
        for (int i2 = 0; i2 != vectorArray.length; ++i2) {
            vectorArray2[i2] = GMSSUtils.clone(vectorArray[i2]);
        }
        return vectorArray2;
    }
}

