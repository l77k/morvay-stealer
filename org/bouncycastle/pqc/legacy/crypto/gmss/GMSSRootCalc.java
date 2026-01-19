/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.gmss;

import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.legacy.crypto.gmss.GMSSDigestProvider;
import org.bouncycastle.pqc.legacy.crypto.gmss.GMSSUtils;
import org.bouncycastle.pqc.legacy.crypto.gmss.Treehash;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.encoders.Hex;

public class GMSSRootCalc {
    private int heightOfTree;
    private int mdLength;
    private Treehash[] treehash;
    private Vector[] retain;
    private byte[] root;
    private byte[][] AuthPath;
    private int K;
    private Vector tailStack;
    private Vector heightOfNodes;
    private Digest messDigestTree;
    private GMSSDigestProvider digestProvider;
    private int[] index;
    private boolean isInitialized;
    private boolean isFinished;
    private int indexForNextSeed;
    private int heightOfNextSeed;

    public GMSSRootCalc(int n2, int n3, GMSSDigestProvider gMSSDigestProvider) {
        this.heightOfTree = n2;
        this.digestProvider = gMSSDigestProvider;
        this.messDigestTree = gMSSDigestProvider.get();
        this.mdLength = this.messDigestTree.getDigestSize();
        this.K = n3;
        this.index = new int[n2];
        this.AuthPath = new byte[n2][this.mdLength];
        this.root = new byte[this.mdLength];
        this.retain = new Vector[this.K - 1];
        for (int i2 = 0; i2 < n3 - 1; ++i2) {
            this.retain[i2] = new Vector();
        }
    }

    public void initialize(Vector vector) {
        int n2;
        this.treehash = new Treehash[this.heightOfTree - this.K];
        for (n2 = 0; n2 < this.heightOfTree - this.K; ++n2) {
            this.treehash[n2] = new Treehash(vector, n2, this.digestProvider.get());
        }
        this.index = new int[this.heightOfTree];
        this.AuthPath = new byte[this.heightOfTree][this.mdLength];
        this.root = new byte[this.mdLength];
        this.tailStack = new Vector();
        this.heightOfNodes = new Vector();
        this.isInitialized = true;
        this.isFinished = false;
        for (n2 = 0; n2 < this.heightOfTree; ++n2) {
            this.index[n2] = -1;
        }
        this.retain = new Vector[this.K - 1];
        for (n2 = 0; n2 < this.K - 1; ++n2) {
            this.retain[n2] = new Vector();
        }
        this.indexForNextSeed = 3;
        this.heightOfNextSeed = 0;
    }

    public void update(byte[] byArray, byte[] byArray2) {
        if (this.heightOfNextSeed < this.heightOfTree - this.K && this.indexForNextSeed - 2 == this.index[0]) {
            this.initializeTreehashSeed(byArray, this.heightOfNextSeed);
            ++this.heightOfNextSeed;
            this.indexForNextSeed *= 2;
        }
        this.update(byArray2);
    }

    public void update(byte[] byArray) {
        if (this.isFinished) {
            System.out.print("Too much updates for Tree!!");
            return;
        }
        if (!this.isInitialized) {
            System.err.println("GMSSRootCalc not initialized!");
            return;
        }
        this.index[0] = this.index[0] + 1;
        if (this.index[0] == 1) {
            System.arraycopy(byArray, 0, this.AuthPath[0], 0, this.mdLength);
        } else if (this.index[0] == 3 && this.heightOfTree > this.K) {
            this.treehash[0].setFirstNode(byArray);
        }
        if ((this.index[0] - 3) % 2 == 0 && this.index[0] >= 3 && this.heightOfTree == this.K) {
            this.retain[0].insertElementAt(byArray, 0);
        }
        if (this.index[0] == 0) {
            this.tailStack.addElement(byArray);
            this.heightOfNodes.addElement(Integers.valueOf(0));
        } else {
            byte[] byArray2 = new byte[this.mdLength];
            byte[] byArray3 = new byte[this.mdLength << 1];
            System.arraycopy(byArray, 0, byArray2, 0, this.mdLength);
            int n2 = 0;
            while (this.tailStack.size() > 0 && n2 == (Integer)this.heightOfNodes.lastElement()) {
                System.arraycopy(this.tailStack.lastElement(), 0, byArray3, 0, this.mdLength);
                this.tailStack.removeElementAt(this.tailStack.size() - 1);
                this.heightOfNodes.removeElementAt(this.heightOfNodes.size() - 1);
                System.arraycopy(byArray2, 0, byArray3, this.mdLength, this.mdLength);
                this.messDigestTree.update(byArray3, 0, byArray3.length);
                byArray2 = new byte[this.messDigestTree.getDigestSize()];
                this.messDigestTree.doFinal(byArray2, 0);
                if (++n2 >= this.heightOfTree) continue;
                int n3 = n2;
                this.index[n3] = this.index[n3] + 1;
                if (this.index[n2] == 1) {
                    System.arraycopy(byArray2, 0, this.AuthPath[n2], 0, this.mdLength);
                }
                if (n2 >= this.heightOfTree - this.K) {
                    if (n2 == 0) {
                        System.out.println("M\ufffd\ufffd\ufffdP");
                    }
                    if ((this.index[n2] - 3) % 2 != 0 || this.index[n2] < 3) continue;
                    this.retain[n2 - (this.heightOfTree - this.K)].insertElementAt(byArray2, 0);
                    continue;
                }
                if (this.index[n2] != 3) continue;
                this.treehash[n2].setFirstNode(byArray2);
            }
            this.tailStack.addElement(byArray2);
            this.heightOfNodes.addElement(Integers.valueOf(n2));
            if (n2 == this.heightOfTree) {
                this.isFinished = true;
                this.isInitialized = false;
                this.root = (byte[])this.tailStack.lastElement();
            }
        }
    }

    public void initializeTreehashSeed(byte[] byArray, int n2) {
        this.treehash[n2].initializeSeed(byArray);
    }

    public boolean wasInitialized() {
        return this.isInitialized;
    }

    public boolean wasFinished() {
        return this.isFinished;
    }

    public byte[][] getAuthPath() {
        return GMSSUtils.clone(this.AuthPath);
    }

    public Treehash[] getTreehash() {
        return GMSSUtils.clone(this.treehash);
    }

    public Vector[] getRetain() {
        return GMSSUtils.clone(this.retain);
    }

    public byte[] getRoot() {
        return Arrays.clone(this.root);
    }

    public Vector getStack() {
        Vector vector = new Vector();
        Enumeration enumeration = this.tailStack.elements();
        while (enumeration.hasMoreElements()) {
            vector.addElement(enumeration.nextElement());
        }
        return vector;
    }

    public byte[][] getStatByte() {
        int n2;
        int n3 = this.tailStack == null ? 0 : this.tailStack.size();
        byte[][] byArray = new byte[1 + this.heightOfTree + n3][64];
        byArray[0] = this.root;
        for (n2 = 0; n2 < this.heightOfTree; ++n2) {
            byArray[1 + n2] = this.AuthPath[n2];
        }
        for (n2 = 0; n2 < n3; ++n2) {
            byArray[1 + this.heightOfTree + n2] = (byte[])this.tailStack.elementAt(n2);
        }
        return byArray;
    }

    public int[] getStatInt() {
        int n2;
        int n3 = this.tailStack == null ? 0 : this.tailStack.size();
        int[] nArray = new int[8 + this.heightOfTree + n3];
        nArray[0] = this.heightOfTree;
        nArray[1] = this.mdLength;
        nArray[2] = this.K;
        nArray[3] = this.indexForNextSeed;
        nArray[4] = this.heightOfNextSeed;
        nArray[5] = this.isFinished ? 1 : 0;
        nArray[6] = this.isInitialized ? 1 : 0;
        nArray[7] = n3;
        for (n2 = 0; n2 < this.heightOfTree; ++n2) {
            nArray[8 + n2] = this.index[n2];
        }
        for (n2 = 0; n2 < n3; ++n2) {
            nArray[8 + this.heightOfTree + n2] = (Integer)this.heightOfNodes.elementAt(n2);
        }
        return nArray;
    }

    public String toString() {
        int n2;
        String string = "";
        int n3 = this.tailStack == null ? 0 : this.tailStack.size();
        for (n2 = 0; n2 < 8 + this.heightOfTree + n3; ++n2) {
            string = string + this.getStatInt()[n2] + " ";
        }
        for (n2 = 0; n2 < 1 + this.heightOfTree + n3; ++n2) {
            string = string + new String(Hex.encode(this.getStatByte()[n2])) + " ";
        }
        string = string + "  " + this.digestProvider.get().getDigestSize();
        return string;
    }
}

