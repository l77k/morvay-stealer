/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.slhdsa;

import org.bouncycastle.pqc.crypto.slhdsa.SIG_FORS;
import org.bouncycastle.pqc.crypto.slhdsa.SIG_XMSS;

class SIG {
    private final byte[] r;
    private final SIG_FORS[] sig_fors;
    private final SIG_XMSS[] sig_ht;

    public SIG(int n2, int n3, int n4, int n5, int n6, int n7, byte[] byArray) {
        int n8;
        byte[][] byArrayArray;
        byte[] byArray2;
        int n9;
        this.r = new byte[n2];
        System.arraycopy(byArray, 0, this.r, 0, n2);
        this.sig_fors = new SIG_FORS[n3];
        int n10 = n2;
        for (n9 = 0; n9 != n3; ++n9) {
            byArray2 = new byte[n2];
            System.arraycopy(byArray, n10, byArray2, 0, n2);
            n10 += n2;
            byArrayArray = new byte[n4][];
            for (n8 = 0; n8 != n4; ++n8) {
                byArrayArray[n8] = new byte[n2];
                System.arraycopy(byArray, n10, byArrayArray[n8], 0, n2);
                n10 += n2;
            }
            this.sig_fors[n9] = new SIG_FORS(byArray2, byArrayArray);
        }
        this.sig_ht = new SIG_XMSS[n5];
        for (n9 = 0; n9 != n5; ++n9) {
            byArray2 = new byte[n7 * n2];
            System.arraycopy(byArray, n10, byArray2, 0, byArray2.length);
            n10 += byArray2.length;
            byArrayArray = new byte[n6][];
            for (n8 = 0; n8 != n6; ++n8) {
                byArrayArray[n8] = new byte[n2];
                System.arraycopy(byArray, n10, byArrayArray[n8], 0, n2);
                n10 += n2;
            }
            this.sig_ht[n9] = new SIG_XMSS(byArray2, byArrayArray);
        }
        if (n10 != byArray.length) {
            throw new IllegalArgumentException("signature wrong length");
        }
    }

    public byte[] getR() {
        return this.r;
    }

    public SIG_FORS[] getSIG_FORS() {
        return this.sig_fors;
    }

    public SIG_XMSS[] getSIG_HT() {
        return this.sig_ht;
    }
}

