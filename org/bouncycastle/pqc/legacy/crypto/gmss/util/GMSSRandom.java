/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.gmss.util;

import org.bouncycastle.crypto.Digest;

public class GMSSRandom {
    private Digest messDigestTree;

    public GMSSRandom(Digest digest) {
        this.messDigestTree = digest;
    }

    public byte[] nextSeed(byte[] byArray) {
        byte[] byArray2 = new byte[byArray.length];
        this.messDigestTree.update(byArray, 0, byArray.length);
        byArray2 = new byte[this.messDigestTree.getDigestSize()];
        this.messDigestTree.doFinal(byArray2, 0);
        this.addByteArrays(byArray, byArray2);
        this.addOne(byArray);
        return byArray2;
    }

    private void addByteArrays(byte[] byArray, byte[] byArray2) {
        int n2 = 0;
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            int n3 = (0xFF & byArray[i2]) + (0xFF & byArray2[i2]) + n2;
            byArray[i2] = (byte)n3;
            n2 = (byte)(n3 >> 8);
        }
    }

    private void addOne(byte[] byArray) {
        int n2 = 1;
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            int n3 = (0xFF & byArray[i2]) + n2;
            byArray[i2] = (byte)n3;
            n2 = (byte)(n3 >> 8);
        }
    }
}

