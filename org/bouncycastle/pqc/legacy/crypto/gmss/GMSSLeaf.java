/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.gmss;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.legacy.crypto.gmss.util.GMSSRandom;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

public class GMSSLeaf {
    private Digest messDigestOTS;
    private int mdsize;
    private int keysize;
    private GMSSRandom gmssRandom;
    private byte[] leaf;
    private byte[] concHashs;
    private int i;
    private int j;
    private int two_power_w;
    private int w;
    private int steps;
    private byte[] seed;
    byte[] privateKeyOTS;

    public GMSSLeaf(Digest digest, byte[][] byArray, int[] nArray) {
        this.i = nArray[0];
        this.j = nArray[1];
        this.steps = nArray[2];
        this.w = nArray[3];
        this.messDigestOTS = digest;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.mdsize = this.messDigestOTS.getDigestSize();
        int n2 = this.mdsize << 3;
        int n3 = (int)Math.ceil((double)n2 / (double)this.w);
        int n4 = this.getLog((n3 << this.w) + 1);
        this.keysize = n3 + (int)Math.ceil((double)n4 / (double)this.w);
        this.two_power_w = 1 << this.w;
        this.privateKeyOTS = byArray[0];
        this.seed = byArray[1];
        this.concHashs = byArray[2];
        this.leaf = byArray[3];
    }

    GMSSLeaf(Digest digest, int n2, int n3) {
        this.w = n2;
        this.messDigestOTS = digest;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.mdsize = this.messDigestOTS.getDigestSize();
        int n4 = this.mdsize << 3;
        int n5 = (int)Math.ceil((double)n4 / (double)n2);
        int n6 = this.getLog((n5 << n2) + 1);
        this.keysize = n5 + (int)Math.ceil((double)n6 / (double)n2);
        this.two_power_w = 1 << n2;
        this.steps = (int)Math.ceil((double)(((1 << n2) - 1) * this.keysize + 1 + this.keysize) / (double)n3);
        this.seed = new byte[this.mdsize];
        this.leaf = new byte[this.mdsize];
        this.privateKeyOTS = new byte[this.mdsize];
        this.concHashs = new byte[this.mdsize * this.keysize];
    }

    public GMSSLeaf(Digest digest, int n2, int n3, byte[] byArray) {
        this.w = n2;
        this.messDigestOTS = digest;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.mdsize = this.messDigestOTS.getDigestSize();
        int n4 = this.mdsize << 3;
        int n5 = (int)Math.ceil((double)n4 / (double)n2);
        int n6 = this.getLog((n5 << n2) + 1);
        this.keysize = n5 + (int)Math.ceil((double)n6 / (double)n2);
        this.two_power_w = 1 << n2;
        this.steps = (int)Math.ceil((double)(((1 << n2) - 1) * this.keysize + 1 + this.keysize) / (double)n3);
        this.seed = new byte[this.mdsize];
        this.leaf = new byte[this.mdsize];
        this.privateKeyOTS = new byte[this.mdsize];
        this.concHashs = new byte[this.mdsize * this.keysize];
        this.initLeafCalc(byArray);
    }

    private GMSSLeaf(GMSSLeaf gMSSLeaf) {
        this.messDigestOTS = gMSSLeaf.messDigestOTS;
        this.mdsize = gMSSLeaf.mdsize;
        this.keysize = gMSSLeaf.keysize;
        this.gmssRandom = gMSSLeaf.gmssRandom;
        this.leaf = Arrays.clone(gMSSLeaf.leaf);
        this.concHashs = Arrays.clone(gMSSLeaf.concHashs);
        this.i = gMSSLeaf.i;
        this.j = gMSSLeaf.j;
        this.two_power_w = gMSSLeaf.two_power_w;
        this.w = gMSSLeaf.w;
        this.steps = gMSSLeaf.steps;
        this.seed = Arrays.clone(gMSSLeaf.seed);
        this.privateKeyOTS = Arrays.clone(gMSSLeaf.privateKeyOTS);
    }

    void initLeafCalc(byte[] byArray) {
        this.i = 0;
        this.j = 0;
        byte[] byArray2 = new byte[this.mdsize];
        System.arraycopy(byArray, 0, byArray2, 0, this.seed.length);
        this.seed = this.gmssRandom.nextSeed(byArray2);
    }

    GMSSLeaf nextLeaf() {
        GMSSLeaf gMSSLeaf = new GMSSLeaf(this);
        gMSSLeaf.updateLeafCalc();
        return gMSSLeaf;
    }

    private void updateLeafCalc() {
        byte[] byArray = new byte[this.messDigestOTS.getDigestSize()];
        for (int i2 = 0; i2 < this.steps + 10000; ++i2) {
            if (this.i == this.keysize && this.j == this.two_power_w - 1) {
                this.messDigestOTS.update(this.concHashs, 0, this.concHashs.length);
                this.leaf = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(this.leaf, 0);
                return;
            }
            if (this.i == 0 || this.j == this.two_power_w - 1) {
                ++this.i;
                this.j = 0;
                this.privateKeyOTS = this.gmssRandom.nextSeed(this.seed);
                continue;
            }
            this.messDigestOTS.update(this.privateKeyOTS, 0, this.privateKeyOTS.length);
            this.privateKeyOTS = byArray;
            this.messDigestOTS.doFinal(this.privateKeyOTS, 0);
            ++this.j;
            if (this.j != this.two_power_w - 1) continue;
            System.arraycopy(this.privateKeyOTS, 0, this.concHashs, this.mdsize * (this.i - 1), this.mdsize);
        }
        throw new IllegalStateException("unable to updateLeaf in steps: " + this.steps + " " + this.i + " " + this.j);
    }

    public byte[] getLeaf() {
        return Arrays.clone(this.leaf);
    }

    private int getLog(int n2) {
        int n3 = 1;
        int n4 = 2;
        while (n4 < n2) {
            n4 <<= 1;
            ++n3;
        }
        return n3;
    }

    public byte[][] getStatByte() {
        byte[][] byArrayArray = new byte[][]{this.privateKeyOTS, this.seed, this.concHashs, this.leaf};
        return byArrayArray;
    }

    public int[] getStatInt() {
        int[] nArray = new int[]{this.i, this.j, this.steps, this.w};
        return nArray;
    }

    public String toString() {
        String string = "";
        for (int i2 = 0; i2 < 4; ++i2) {
            string = string + this.getStatInt()[i2] + " ";
        }
        string = string + " " + this.mdsize + " " + this.keysize + " " + this.two_power_w + " ";
        byte[][] byArray = this.getStatByte();
        for (int i3 = 0; i3 < 4; ++i3) {
            string = byArray[i3] != null ? string + new String(Hex.encode(byArray[i3])) + " " : string + "null ";
        }
        return string;
    }
}

