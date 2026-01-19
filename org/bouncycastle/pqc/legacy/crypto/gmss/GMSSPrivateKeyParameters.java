/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.gmss;

import java.util.Vector;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.legacy.crypto.gmss.GMSSDigestProvider;
import org.bouncycastle.pqc.legacy.crypto.gmss.GMSSKeyParameters;
import org.bouncycastle.pqc.legacy.crypto.gmss.GMSSLeaf;
import org.bouncycastle.pqc.legacy.crypto.gmss.GMSSParameters;
import org.bouncycastle.pqc.legacy.crypto.gmss.GMSSRootCalc;
import org.bouncycastle.pqc.legacy.crypto.gmss.GMSSRootSig;
import org.bouncycastle.pqc.legacy.crypto.gmss.Treehash;
import org.bouncycastle.pqc.legacy.crypto.gmss.util.GMSSRandom;
import org.bouncycastle.pqc.legacy.crypto.gmss.util.WinternitzOTSignature;
import org.bouncycastle.util.Arrays;

public class GMSSPrivateKeyParameters
extends GMSSKeyParameters {
    private int[] index;
    private byte[][] currentSeeds;
    private byte[][] nextNextSeeds;
    private byte[][][] currentAuthPaths;
    private byte[][][] nextAuthPaths;
    private Treehash[][] currentTreehash;
    private Treehash[][] nextTreehash;
    private Vector[] currentStack;
    private Vector[] nextStack;
    private Vector[][] currentRetain;
    private Vector[][] nextRetain;
    private byte[][][] keep;
    private GMSSLeaf[] nextNextLeaf;
    private GMSSLeaf[] upperLeaf;
    private GMSSLeaf[] upperTreehashLeaf;
    private int[] minTreehash;
    private GMSSParameters gmssPS;
    private byte[][] nextRoot;
    private GMSSRootCalc[] nextNextRoot;
    private byte[][] currentRootSig;
    private GMSSRootSig[] nextRootSig;
    private GMSSDigestProvider digestProvider;
    private boolean used = false;
    private int[] heightOfTrees;
    private int[] otsIndex;
    private int[] K;
    private int numLayer;
    private Digest messDigestTrees;
    private int mdLength;
    private GMSSRandom gmssRandom;
    private int[] numLeafs;

    public GMSSPrivateKeyParameters(byte[][] byArray, byte[][] byArray2, byte[][][] byArray3, byte[][][] byArray4, Treehash[][] treehashArray, Treehash[][] treehashArray2, Vector[] vectorArray, Vector[] vectorArray2, Vector[][] vectorArray3, Vector[][] vectorArray4, byte[][] byArray5, byte[][] byArray6, GMSSParameters gMSSParameters, GMSSDigestProvider gMSSDigestProvider) {
        this(null, byArray, byArray2, byArray3, byArray4, null, treehashArray, treehashArray2, vectorArray, vectorArray2, vectorArray3, vectorArray4, null, null, null, null, byArray5, null, byArray6, null, gMSSParameters, gMSSDigestProvider);
    }

    public GMSSPrivateKeyParameters(int[] nArray, byte[][] byArray, byte[][] byArray2, byte[][][] byArray3, byte[][][] byArray4, byte[][][] byArray5, Treehash[][] treehashArray, Treehash[][] treehashArray2, Vector[] vectorArray, Vector[] vectorArray2, Vector[][] vectorArray3, Vector[][] vectorArray4, GMSSLeaf[] gMSSLeafArray, GMSSLeaf[] gMSSLeafArray2, GMSSLeaf[] gMSSLeafArray3, int[] nArray2, byte[][] byArray6, GMSSRootCalc[] gMSSRootCalcArray, byte[][] byArray7, GMSSRootSig[] gMSSRootSigArray, GMSSParameters gMSSParameters, GMSSDigestProvider gMSSDigestProvider) {
        super(true, gMSSParameters);
        int n2;
        this.messDigestTrees = gMSSDigestProvider.get();
        this.mdLength = this.messDigestTrees.getDigestSize();
        this.gmssPS = gMSSParameters;
        this.otsIndex = gMSSParameters.getWinternitzParameter();
        this.K = gMSSParameters.getK();
        this.heightOfTrees = gMSSParameters.getHeightOfTrees();
        this.numLayer = this.gmssPS.getNumOfLayers();
        if (nArray == null) {
            this.index = new int[this.numLayer];
            for (n2 = 0; n2 < this.numLayer; ++n2) {
                this.index[n2] = 0;
            }
        } else {
            this.index = nArray;
        }
        this.currentSeeds = byArray;
        this.nextNextSeeds = byArray2;
        this.currentAuthPaths = Arrays.clone(byArray3);
        this.nextAuthPaths = byArray4;
        if (byArray5 == null) {
            this.keep = new byte[this.numLayer][][];
            for (n2 = 0; n2 < this.numLayer; ++n2) {
                this.keep[n2] = new byte[(int)Math.floor(this.heightOfTrees[n2] / 2)][this.mdLength];
            }
        } else {
            this.keep = byArray5;
        }
        if (vectorArray == null) {
            this.currentStack = new Vector[this.numLayer];
            for (n2 = 0; n2 < this.numLayer; ++n2) {
                this.currentStack[n2] = new Vector();
            }
        } else {
            this.currentStack = vectorArray;
        }
        if (vectorArray2 == null) {
            this.nextStack = new Vector[this.numLayer - 1];
            for (n2 = 0; n2 < this.numLayer - 1; ++n2) {
                this.nextStack[n2] = new Vector();
            }
        } else {
            this.nextStack = vectorArray2;
        }
        this.currentTreehash = treehashArray;
        this.nextTreehash = treehashArray2;
        this.currentRetain = vectorArray3;
        this.nextRetain = vectorArray4;
        this.nextRoot = byArray6;
        this.digestProvider = gMSSDigestProvider;
        if (gMSSRootCalcArray == null) {
            this.nextNextRoot = new GMSSRootCalc[this.numLayer - 1];
            for (n2 = 0; n2 < this.numLayer - 1; ++n2) {
                this.nextNextRoot[n2] = new GMSSRootCalc(this.heightOfTrees[n2 + 1], this.K[n2 + 1], this.digestProvider);
            }
        } else {
            this.nextNextRoot = gMSSRootCalcArray;
        }
        this.currentRootSig = byArray7;
        this.numLeafs = new int[this.numLayer];
        for (n2 = 0; n2 < this.numLayer; ++n2) {
            this.numLeafs[n2] = 1 << this.heightOfTrees[n2];
        }
        this.gmssRandom = new GMSSRandom(this.messDigestTrees);
        if (this.numLayer > 1) {
            if (gMSSLeafArray == null) {
                this.nextNextLeaf = new GMSSLeaf[this.numLayer - 2];
                for (n2 = 0; n2 < this.numLayer - 2; ++n2) {
                    this.nextNextLeaf[n2] = new GMSSLeaf(gMSSDigestProvider.get(), this.otsIndex[n2 + 1], this.numLeafs[n2 + 2], this.nextNextSeeds[n2]);
                }
            } else {
                this.nextNextLeaf = gMSSLeafArray;
            }
        } else {
            this.nextNextLeaf = new GMSSLeaf[0];
        }
        if (gMSSLeafArray2 == null) {
            this.upperLeaf = new GMSSLeaf[this.numLayer - 1];
            for (n2 = 0; n2 < this.numLayer - 1; ++n2) {
                this.upperLeaf[n2] = new GMSSLeaf(gMSSDigestProvider.get(), this.otsIndex[n2], this.numLeafs[n2 + 1], this.currentSeeds[n2]);
            }
        } else {
            this.upperLeaf = gMSSLeafArray2;
        }
        if (gMSSLeafArray3 == null) {
            this.upperTreehashLeaf = new GMSSLeaf[this.numLayer - 1];
            for (n2 = 0; n2 < this.numLayer - 1; ++n2) {
                this.upperTreehashLeaf[n2] = new GMSSLeaf(gMSSDigestProvider.get(), this.otsIndex[n2], this.numLeafs[n2 + 1]);
            }
        } else {
            this.upperTreehashLeaf = gMSSLeafArray3;
        }
        if (nArray2 == null) {
            this.minTreehash = new int[this.numLayer - 1];
            for (n2 = 0; n2 < this.numLayer - 1; ++n2) {
                this.minTreehash[n2] = -1;
            }
        } else {
            this.minTreehash = nArray2;
        }
        byte[] byArray8 = new byte[this.mdLength];
        byte[] byArray9 = new byte[this.mdLength];
        if (gMSSRootSigArray == null) {
            this.nextRootSig = new GMSSRootSig[this.numLayer - 1];
            for (int i2 = 0; i2 < this.numLayer - 1; ++i2) {
                System.arraycopy(byArray[i2], 0, byArray8, 0, this.mdLength);
                this.gmssRandom.nextSeed(byArray8);
                byArray9 = this.gmssRandom.nextSeed(byArray8);
                this.nextRootSig[i2] = new GMSSRootSig(gMSSDigestProvider.get(), this.otsIndex[i2], this.heightOfTrees[i2 + 1]);
                this.nextRootSig[i2].initSign(byArray9, byArray6[i2]);
            }
        } else {
            this.nextRootSig = gMSSRootSigArray;
        }
    }

    private GMSSPrivateKeyParameters(GMSSPrivateKeyParameters gMSSPrivateKeyParameters) {
        super(true, gMSSPrivateKeyParameters.getParameters());
        this.index = Arrays.clone(gMSSPrivateKeyParameters.index);
        this.currentSeeds = Arrays.clone(gMSSPrivateKeyParameters.currentSeeds);
        this.nextNextSeeds = Arrays.clone(gMSSPrivateKeyParameters.nextNextSeeds);
        this.currentAuthPaths = Arrays.clone(gMSSPrivateKeyParameters.currentAuthPaths);
        this.nextAuthPaths = Arrays.clone(gMSSPrivateKeyParameters.nextAuthPaths);
        this.currentTreehash = gMSSPrivateKeyParameters.currentTreehash;
        this.nextTreehash = gMSSPrivateKeyParameters.nextTreehash;
        this.currentStack = gMSSPrivateKeyParameters.currentStack;
        this.nextStack = gMSSPrivateKeyParameters.nextStack;
        this.currentRetain = gMSSPrivateKeyParameters.currentRetain;
        this.nextRetain = gMSSPrivateKeyParameters.nextRetain;
        this.keep = Arrays.clone(gMSSPrivateKeyParameters.keep);
        this.nextNextLeaf = gMSSPrivateKeyParameters.nextNextLeaf;
        this.upperLeaf = gMSSPrivateKeyParameters.upperLeaf;
        this.upperTreehashLeaf = gMSSPrivateKeyParameters.upperTreehashLeaf;
        this.minTreehash = gMSSPrivateKeyParameters.minTreehash;
        this.gmssPS = gMSSPrivateKeyParameters.gmssPS;
        this.nextRoot = Arrays.clone(gMSSPrivateKeyParameters.nextRoot);
        this.nextNextRoot = gMSSPrivateKeyParameters.nextNextRoot;
        this.currentRootSig = gMSSPrivateKeyParameters.currentRootSig;
        this.nextRootSig = gMSSPrivateKeyParameters.nextRootSig;
        this.digestProvider = gMSSPrivateKeyParameters.digestProvider;
        this.heightOfTrees = gMSSPrivateKeyParameters.heightOfTrees;
        this.otsIndex = gMSSPrivateKeyParameters.otsIndex;
        this.K = gMSSPrivateKeyParameters.K;
        this.numLayer = gMSSPrivateKeyParameters.numLayer;
        this.messDigestTrees = gMSSPrivateKeyParameters.messDigestTrees;
        this.mdLength = gMSSPrivateKeyParameters.mdLength;
        this.gmssRandom = gMSSPrivateKeyParameters.gmssRandom;
        this.numLeafs = gMSSPrivateKeyParameters.numLeafs;
    }

    public boolean isUsed() {
        return this.used;
    }

    public void markUsed() {
        this.used = true;
    }

    public GMSSPrivateKeyParameters nextKey() {
        GMSSPrivateKeyParameters gMSSPrivateKeyParameters = new GMSSPrivateKeyParameters(this);
        gMSSPrivateKeyParameters.nextKey(this.gmssPS.getNumOfLayers() - 1);
        return gMSSPrivateKeyParameters;
    }

    private void nextKey(int n2) {
        if (n2 == this.numLayer - 1) {
            int n3 = n2;
            this.index[n3] = this.index[n3] + 1;
        }
        if (this.index[n2] == this.numLeafs[n2]) {
            if (this.numLayer != 1) {
                this.nextTree(n2);
                this.index[n2] = 0;
            }
        } else {
            this.updateKey(n2);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void nextTree(int n2) {
        if (n2 > 0) {
            int n3 = n2 - 1;
            this.index[n3] = this.index[n3] + 1;
            boolean bl = true;
            int n4 = n2;
            do {
                if (this.index[--n4] >= this.numLeafs[n4]) continue;
                bl = false;
            } while (bl && n4 > 0);
            if (!bl) {
                int n5;
                this.gmssRandom.nextSeed(this.currentSeeds[n2]);
                this.nextRootSig[n2 - 1].updateSign();
                if (n2 > 1) {
                    this.nextNextLeaf[n2 - 1 - 1] = this.nextNextLeaf[n2 - 1 - 1].nextLeaf();
                }
                this.upperLeaf[n2 - 1] = this.upperLeaf[n2 - 1].nextLeaf();
                if (this.minTreehash[n2 - 1] >= 0) {
                    this.upperTreehashLeaf[n2 - 1] = this.upperTreehashLeaf[n2 - 1].nextLeaf();
                    byte[] byArray = this.upperTreehashLeaf[n2 - 1].getLeaf();
                    try {
                        this.currentTreehash[n2 - 1][this.minTreehash[n2 - 1]].update(this.gmssRandom, byArray);
                        if (!this.currentTreehash[n2 - 1][this.minTreehash[n2 - 1]].wasFinished()) {
                            // empty if block
                        }
                    }
                    catch (Exception exception) {
                        System.out.println(exception);
                    }
                }
                this.updateNextNextAuthRoot(n2);
                this.currentRootSig[n2 - 1] = this.nextRootSig[n2 - 1].getSig();
                for (n5 = 0; n5 < this.heightOfTrees[n2] - this.K[n2]; ++n5) {
                    this.currentTreehash[n2][n5] = this.nextTreehash[n2 - 1][n5];
                    this.nextTreehash[n2 - 1][n5] = this.nextNextRoot[n2 - 1].getTreehash()[n5];
                }
                for (n5 = 0; n5 < this.heightOfTrees[n2]; ++n5) {
                    System.arraycopy(this.nextAuthPaths[n2 - 1][n5], 0, this.currentAuthPaths[n2][n5], 0, this.mdLength);
                    System.arraycopy(this.nextNextRoot[n2 - 1].getAuthPath()[n5], 0, this.nextAuthPaths[n2 - 1][n5], 0, this.mdLength);
                }
                for (n5 = 0; n5 < this.K[n2] - 1; ++n5) {
                    this.currentRetain[n2][n5] = this.nextRetain[n2 - 1][n5];
                    this.nextRetain[n2 - 1][n5] = this.nextNextRoot[n2 - 1].getRetain()[n5];
                }
                this.currentStack[n2] = this.nextStack[n2 - 1];
                this.nextStack[n2 - 1] = this.nextNextRoot[n2 - 1].getStack();
                this.nextRoot[n2 - 1] = this.nextNextRoot[n2 - 1].getRoot();
                byte[] byArray = new byte[this.mdLength];
                byte[] byArray2 = new byte[this.mdLength];
                System.arraycopy(this.currentSeeds[n2 - 1], 0, byArray2, 0, this.mdLength);
                byArray = this.gmssRandom.nextSeed(byArray2);
                byArray = this.gmssRandom.nextSeed(byArray2);
                byArray = this.gmssRandom.nextSeed(byArray2);
                this.nextRootSig[n2 - 1].initSign(byArray, this.nextRoot[n2 - 1]);
                this.nextKey(n2 - 1);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void updateKey(int n2) {
        this.computeAuthPaths(n2);
        if (n2 > 0) {
            if (n2 > 1) {
                this.nextNextLeaf[n2 - 1 - 1] = this.nextNextLeaf[n2 - 1 - 1].nextLeaf();
            }
            this.upperLeaf[n2 - 1] = this.upperLeaf[n2 - 1].nextLeaf();
            int n3 = (int)Math.floor((double)(this.getNumLeafs(n2) * 2) / (double)(this.heightOfTrees[n2 - 1] - this.K[n2 - 1]));
            if (this.index[n2] % n3 == 1) {
                byte[] byArray;
                if (this.index[n2] > 1 && this.minTreehash[n2 - 1] >= 0) {
                    byArray = this.upperTreehashLeaf[n2 - 1].getLeaf();
                    try {
                        this.currentTreehash[n2 - 1][this.minTreehash[n2 - 1]].update(this.gmssRandom, byArray);
                        if (!this.currentTreehash[n2 - 1][this.minTreehash[n2 - 1]].wasFinished()) {
                            // empty if block
                        }
                    }
                    catch (Exception exception) {
                        System.out.println(exception);
                    }
                }
                this.minTreehash[n2 - 1] = this.getMinTreehashIndex(n2 - 1);
                if (this.minTreehash[n2 - 1] >= 0) {
                    byArray = this.currentTreehash[n2 - 1][this.minTreehash[n2 - 1]].getSeedActive();
                    this.upperTreehashLeaf[n2 - 1] = new GMSSLeaf(this.digestProvider.get(), this.otsIndex[n2 - 1], n3, byArray);
                    this.upperTreehashLeaf[n2 - 1] = this.upperTreehashLeaf[n2 - 1].nextLeaf();
                }
            } else if (this.minTreehash[n2 - 1] >= 0) {
                this.upperTreehashLeaf[n2 - 1] = this.upperTreehashLeaf[n2 - 1].nextLeaf();
            }
            this.nextRootSig[n2 - 1].updateSign();
            if (this.index[n2] == 1) {
                this.nextNextRoot[n2 - 1].initialize(new Vector());
            }
            this.updateNextNextAuthRoot(n2);
        }
    }

    private int getMinTreehashIndex(int n2) {
        int n3 = -1;
        for (int i2 = 0; i2 < this.heightOfTrees[n2] - this.K[n2]; ++i2) {
            if (!this.currentTreehash[n2][i2].wasInitialized() || this.currentTreehash[n2][i2].wasFinished()) continue;
            if (n3 == -1) {
                n3 = i2;
                continue;
            }
            if (this.currentTreehash[n2][i2].getLowestNodeHeight() >= this.currentTreehash[n2][n3].getLowestNodeHeight()) continue;
            n3 = i2;
        }
        return n3;
    }

    private void computeAuthPaths(int n2) {
        int n3;
        Object object;
        int n4;
        int n5 = this.index[n2];
        int n6 = this.heightOfTrees[n2];
        int n7 = this.K[n2];
        for (n4 = 0; n4 < n6 - n7; ++n4) {
            this.currentTreehash[n2][n4].updateNextSeed(this.gmssRandom);
        }
        n4 = this.heightOfPhi(n5);
        byte[] byArray = new byte[this.mdLength];
        byArray = this.gmssRandom.nextSeed(this.currentSeeds[n2]);
        int n8 = n5 >>> n4 + 1 & 1;
        byte[] byArray2 = new byte[this.mdLength];
        if (n4 < n6 - 1 && n8 == 0) {
            System.arraycopy(this.currentAuthPaths[n2][n4], 0, byArray2, 0, this.mdLength);
        }
        byte[] byArray3 = new byte[this.mdLength];
        if (n4 == 0) {
            if (n2 == this.numLayer - 1) {
                object = new WinternitzOTSignature(byArray, this.digestProvider.get(), this.otsIndex[n2]);
                byArray3 = ((WinternitzOTSignature)object).getPublicKey();
            } else {
                object = new byte[this.mdLength];
                System.arraycopy(this.currentSeeds[n2], 0, object, 0, this.mdLength);
                this.gmssRandom.nextSeed((byte[])object);
                byArray3 = this.upperLeaf[n2].getLeaf();
                this.upperLeaf[n2].initLeafCalc((byte[])object);
            }
            System.arraycopy(byArray3, 0, this.currentAuthPaths[n2][0], 0, this.mdLength);
        } else {
            object = new byte[this.mdLength << 1];
            System.arraycopy(this.currentAuthPaths[n2][n4 - 1], 0, object, 0, this.mdLength);
            System.arraycopy(this.keep[n2][(int)Math.floor((n4 - 1) / 2)], 0, object, this.mdLength, this.mdLength);
            this.messDigestTrees.update((byte[])object, 0, ((Object)object).length);
            this.currentAuthPaths[n2][n4] = new byte[this.messDigestTrees.getDigestSize()];
            this.messDigestTrees.doFinal(this.currentAuthPaths[n2][n4], 0);
            for (n3 = 0; n3 < n4; ++n3) {
                int n9;
                if (n3 < n6 - n7) {
                    if (this.currentTreehash[n2][n3].wasFinished()) {
                        System.arraycopy(this.currentTreehash[n2][n3].getFirstNode(), 0, this.currentAuthPaths[n2][n3], 0, this.mdLength);
                        this.currentTreehash[n2][n3].destroy();
                    } else {
                        System.err.println("Treehash (" + n2 + "," + n3 + ") not finished when needed in AuthPathComputation");
                    }
                }
                if (n3 < n6 - 1 && n3 >= n6 - n7 && this.currentRetain[n2][n3 - (n6 - n7)].size() > 0) {
                    System.arraycopy(this.currentRetain[n2][n3 - (n6 - n7)].lastElement(), 0, this.currentAuthPaths[n2][n3], 0, this.mdLength);
                    this.currentRetain[n2][n3 - (n6 - n7)].removeElementAt(this.currentRetain[n2][n3 - (n6 - n7)].size() - 1);
                }
                if (n3 >= n6 - n7 || (n9 = n5 + 3 * (1 << n3)) >= this.numLeafs[n2]) continue;
                this.currentTreehash[n2][n3].initialize();
            }
        }
        if (n4 < n6 - 1 && n8 == 0) {
            System.arraycopy(byArray2, 0, this.keep[n2][(int)Math.floor(n4 / 2)], 0, this.mdLength);
        }
        if (n2 == this.numLayer - 1) {
            for (int i2 = 1; i2 <= (n6 - n7) / 2; ++i2) {
                n3 = this.getMinTreehashIndex(n2);
                if (n3 < 0) continue;
                try {
                    byte[] byArray4 = new byte[this.mdLength];
                    System.arraycopy(this.currentTreehash[n2][n3].getSeedActive(), 0, byArray4, 0, this.mdLength);
                    byte[] byArray5 = this.gmssRandom.nextSeed(byArray4);
                    WinternitzOTSignature winternitzOTSignature = new WinternitzOTSignature(byArray5, this.digestProvider.get(), this.otsIndex[n2]);
                    byte[] byArray6 = winternitzOTSignature.getPublicKey();
                    this.currentTreehash[n2][n3].update(this.gmssRandom, byArray6);
                    continue;
                }
                catch (Exception exception) {
                    System.out.println(exception);
                }
            }
        } else {
            this.minTreehash[n2] = this.getMinTreehashIndex(n2);
        }
    }

    private int heightOfPhi(int n2) {
        if (n2 == 0) {
            return -1;
        }
        int n3 = 0;
        int n4 = 1;
        while (n2 % n4 == 0) {
            n4 *= 2;
            ++n3;
        }
        return n3 - 1;
    }

    private void updateNextNextAuthRoot(int n2) {
        byte[] byArray = new byte[this.mdLength];
        byArray = this.gmssRandom.nextSeed(this.nextNextSeeds[n2 - 1]);
        if (n2 == this.numLayer - 1) {
            WinternitzOTSignature winternitzOTSignature = new WinternitzOTSignature(byArray, this.digestProvider.get(), this.otsIndex[n2]);
            this.nextNextRoot[n2 - 1].update(this.nextNextSeeds[n2 - 1], winternitzOTSignature.getPublicKey());
        } else {
            this.nextNextRoot[n2 - 1].update(this.nextNextSeeds[n2 - 1], this.nextNextLeaf[n2 - 1].getLeaf());
            this.nextNextLeaf[n2 - 1].initLeafCalc(this.nextNextSeeds[n2 - 1]);
        }
    }

    public int[] getIndex() {
        return this.index;
    }

    public int getIndex(int n2) {
        return this.index[n2];
    }

    public byte[][] getCurrentSeeds() {
        return Arrays.clone(this.currentSeeds);
    }

    public byte[][][] getCurrentAuthPaths() {
        return Arrays.clone(this.currentAuthPaths);
    }

    public byte[] getSubtreeRootSig(int n2) {
        return this.currentRootSig[n2];
    }

    public GMSSDigestProvider getName() {
        return this.digestProvider;
    }

    public int getNumLeafs(int n2) {
        return this.numLeafs[n2];
    }
}

