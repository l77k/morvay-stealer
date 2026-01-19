/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.picnic;

import java.util.logging.Logger;
import org.bouncycastle.pqc.crypto.picnic.PicnicEngine;
import org.bouncycastle.pqc.crypto.picnic.Utils;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

class Tree {
    private static final Logger LOG = Logger.getLogger(Tree.class.getName());
    private static final int MAX_SEED_SIZE_BYTES = 32;
    private int depth;
    byte[][] nodes;
    private int dataSize;
    private boolean[] haveNode;
    private boolean[] exists;
    private int numNodes;
    private int numLeaves;
    private PicnicEngine engine;

    protected byte[][] getLeaves() {
        return this.nodes;
    }

    protected int getLeavesOffset() {
        return this.numNodes - this.numLeaves;
    }

    public Tree(PicnicEngine picnicEngine, int n2, int n3) {
        int n4;
        this.engine = picnicEngine;
        this.depth = Utils.ceil_log2(n2) + 1;
        this.numNodes = (1 << this.depth) - 1 - ((1 << this.depth - 1) - n2);
        this.numLeaves = n2;
        this.dataSize = n3;
        this.nodes = new byte[this.numNodes][n3];
        for (n4 = 0; n4 < this.numNodes; ++n4) {
            this.nodes[n4] = new byte[n3];
        }
        this.haveNode = new boolean[this.numNodes];
        this.exists = new boolean[this.numNodes];
        Arrays.fill(this.exists, this.numNodes - this.numLeaves, this.numNodes, true);
        for (n4 = this.numNodes - this.numLeaves; n4 > 0; --n4) {
            if (!this.exists(2 * n4 + 1) && !this.exists(2 * n4 + 2)) continue;
            this.exists[n4] = true;
        }
        this.exists[0] = true;
    }

    protected void buildMerkleTree(byte[][] byArray, byte[] byArray2) {
        int n2;
        int n3 = this.numNodes - this.numLeaves;
        for (n2 = 0; n2 < this.numLeaves; ++n2) {
            if (byArray[n2] == null) continue;
            System.arraycopy(byArray[n2], 0, this.nodes[n3 + n2], 0, this.dataSize);
            this.haveNode[n3 + n2] = true;
        }
        for (n2 = this.numNodes; n2 > 0; --n2) {
            this.computeParentHash(n2, byArray2);
        }
    }

    protected int verifyMerkleTree(byte[][] byArray, byte[] byArray2) {
        int n2;
        int n3 = this.numNodes - this.numLeaves;
        for (n2 = 0; n2 < this.numLeaves; ++n2) {
            if (byArray[n2] == null) continue;
            if (this.haveNode[n3 + n2]) {
                return -1;
            }
            if (byArray[n2] == null) continue;
            System.arraycopy(byArray[n2], 0, this.nodes[n3 + n2], 0, this.dataSize);
            this.haveNode[n3 + n2] = true;
        }
        for (n2 = this.numNodes; n2 > 0; --n2) {
            this.computeParentHash(n2, byArray2);
        }
        if (!this.haveNode[0]) {
            return -1;
        }
        return 0;
    }

    protected int reconstructSeeds(int[] nArray, int n2, byte[] byArray, int n3, byte[] byArray2, int n4) {
        int n5 = 0;
        int n6 = n3;
        int[] nArray2 = new int[]{0};
        int[] nArray3 = this.getRevealedNodes(nArray, n2, nArray2);
        for (int i2 = 0; i2 < nArray2[0]; ++i2) {
            if ((n6 -= this.engine.seedSizeBytes) < 0) {
                return -1;
            }
            System.arraycopy(byArray, i2 * this.engine.seedSizeBytes, this.nodes[nArray3[i2]], 0, this.engine.seedSizeBytes);
            this.haveNode[nArray3[i2]] = true;
        }
        this.expandSeeds(byArray2, n4);
        return n5;
    }

    protected byte[] openMerkleTree(int[] nArray, int n2, int[] nArray2) {
        byte[] byArray;
        int[] nArray3 = new int[1];
        int[] nArray4 = this.getRevealedMerkleNodes(nArray, n2, nArray3);
        nArray2[0] = nArray3[0] * this.dataSize;
        byte[] byArray2 = byArray = new byte[nArray2[0]];
        for (int i2 = 0; i2 < nArray3[0]; ++i2) {
            System.arraycopy(this.nodes[nArray4[i2]], 0, byArray, i2 * this.dataSize, this.dataSize);
        }
        return byArray2;
    }

    private int[] getRevealedNodes(int[] nArray, int n2, int[] nArray2) {
        int n3;
        int n4;
        int n5 = this.depth - 1;
        int[][] nArray3 = new int[n5][n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            n4 = 0;
            nArray3[n4][i2] = n3 = nArray[i2] + (this.numNodes - this.numLeaves);
            ++n4;
            while ((n3 = this.getParent(n3)) != 0) {
                nArray3[n4][i2] = n3;
                ++n4;
            }
        }
        int[] nArray4 = new int[this.numLeaves];
        n4 = 0;
        for (n3 = 0; n3 < n5; ++n3) {
            for (int i3 = 0; i3 < n2; ++i3) {
                int n6;
                if (!this.hasSibling(nArray3[n3][i3]) || this.contains(nArray3[n3], n2, n6 = this.getSibling(nArray3[n3][i3]))) continue;
                while (!this.hasRightChild(n6) && !this.isLeafNode(n6)) {
                    n6 = 2 * n6 + 1;
                }
                if (this.contains(nArray4, n4, n6)) continue;
                nArray4[n4] = n6;
                ++n4;
            }
        }
        nArray2[0] = n4;
        return nArray4;
    }

    private int getSibling(int n2) {
        if (this.isLeftChild(n2)) {
            if (n2 + 1 < this.numNodes) {
                return n2 + 1;
            }
            LOG.fine("getSibling: request for node with not sibling");
            return 0;
        }
        return n2 - 1;
    }

    private boolean isLeafNode(int n2) {
        return 2 * n2 + 1 >= this.numNodes;
    }

    private boolean hasSibling(int n2) {
        if (!this.exists(n2)) {
            return false;
        }
        return !this.isLeftChild(n2) || this.exists(n2 + 1);
    }

    protected int revealSeedsSize(int[] nArray, int n2) {
        int[] nArray2 = new int[]{0};
        this.getRevealedNodes(nArray, n2, nArray2);
        return nArray2[0] * this.engine.seedSizeBytes;
    }

    protected int revealSeeds(int[] nArray, int n2, byte[] byArray, int n3) {
        int[] nArray2 = new int[]{0};
        int n4 = n3;
        int[] nArray3 = this.getRevealedNodes(nArray, n2, nArray2);
        for (int i2 = 0; i2 < nArray2[0]; ++i2) {
            if ((n4 -= this.engine.seedSizeBytes) < 0) {
                LOG.fine("Insufficient sized buffer provided to revealSeeds");
                return 0;
            }
            System.arraycopy(this.nodes[nArray3[i2]], 0, byArray, i2 * this.engine.seedSizeBytes, this.engine.seedSizeBytes);
        }
        return byArray.length - n4;
    }

    protected int openMerkleTreeSize(int[] nArray, int n2) {
        int[] nArray2 = new int[1];
        this.getRevealedMerkleNodes(nArray, n2, nArray2);
        return nArray2[0] * this.engine.digestSizeBytes;
    }

    private int[] getRevealedMerkleNodes(int[] nArray, int n2, int[] nArray2) {
        int n3;
        int n4 = this.numNodes - this.numLeaves;
        boolean[] blArray = new boolean[this.numNodes];
        for (n3 = 0; n3 < n2; ++n3) {
            blArray[n4 + nArray[n3]] = true;
        }
        for (int i2 = n3 = this.getParent(this.numNodes - 1); i2 > 0; --i2) {
            if (!this.exists(i2)) continue;
            if (this.exists(2 * i2 + 2)) {
                if (!blArray[2 * i2 + 1] || !blArray[2 * i2 + 2]) continue;
                blArray[i2] = true;
                continue;
            }
            if (!blArray[2 * i2 + 1]) continue;
            blArray[i2] = true;
        }
        int[] nArray3 = new int[this.numLeaves];
        int n5 = 0;
        block2: for (int i3 = 0; i3 < n2; ++i3) {
            int n6 = nArray[i3] + n4;
            do {
                if (blArray[this.getParent(n6)]) continue;
                if (this.contains(nArray3, n5, n6)) continue block2;
                nArray3[n5] = n6;
                ++n5;
                continue block2;
            } while ((n6 = this.getParent(n6)) != 0);
        }
        nArray2[0] = n5;
        return nArray3;
    }

    private boolean contains(int[] nArray, int n2, int n3) {
        for (int i2 = 0; i2 < n2; ++i2) {
            if (nArray[i2] != n3) continue;
            return true;
        }
        return false;
    }

    private void computeParentHash(int n2, byte[] byArray) {
        if (!this.exists(n2)) {
            return;
        }
        int n3 = this.getParent(n2);
        if (this.haveNode[n3]) {
            return;
        }
        if (!this.haveNode[2 * n3 + 1]) {
            return;
        }
        if (this.exists(2 * n3 + 2) && !this.haveNode[2 * n3 + 2]) {
            return;
        }
        this.engine.digest.update((byte)3);
        this.engine.digest.update(this.nodes[2 * n3 + 1], 0, this.engine.digestSizeBytes);
        if (this.hasRightChild(n3)) {
            this.engine.digest.update(this.nodes[2 * n3 + 2], 0, this.engine.digestSizeBytes);
        }
        this.engine.digest.update(byArray, 0, 32);
        this.engine.digest.update(Pack.intToLittleEndian(n3), 0, 2);
        this.engine.digest.doFinal(this.nodes[n3], 0, this.engine.digestSizeBytes);
        this.haveNode[n3] = true;
    }

    protected byte[] getLeaf(int n2) {
        int n3 = this.numNodes - this.numLeaves;
        return this.nodes[n3 + n2];
    }

    protected int addMerkleNodes(int[] nArray, int n2, byte[] byArray, int n3) {
        int n4 = n3;
        int[] nArray2 = new int[]{0};
        int[] nArray3 = this.getRevealedMerkleNodes(nArray, n2, nArray2);
        for (int i2 = 0; i2 < nArray2[0]; ++i2) {
            if ((n4 -= this.dataSize) < 0) {
                return -1;
            }
            System.arraycopy(byArray, i2 * this.dataSize, this.nodes[nArray3[i2]], 0, this.dataSize);
            this.haveNode[nArray3[i2]] = true;
        }
        if (n4 != 0) {
            return -1;
        }
        return 0;
    }

    protected void generateSeeds(byte[] byArray, byte[] byArray2, int n2) {
        this.nodes[0] = byArray;
        this.haveNode[0] = true;
        this.expandSeeds(byArray2, n2);
    }

    private void expandSeeds(byte[] byArray, int n2) {
        byte[] byArray2 = new byte[64];
        int n3 = this.getParent(this.numNodes - 1);
        for (int i2 = 0; i2 <= n3; ++i2) {
            if (!this.haveNode[i2]) continue;
            this.hashSeed(byArray2, this.nodes[i2], byArray, (byte)1, n2, i2);
            if (!this.haveNode[2 * i2 + 1]) {
                System.arraycopy(byArray2, 0, this.nodes[2 * i2 + 1], 0, this.engine.seedSizeBytes);
                this.haveNode[2 * i2 + 1] = true;
            }
            if (!this.exists(2 * i2 + 2) || this.haveNode[2 * i2 + 2]) continue;
            System.arraycopy(byArray2, this.engine.seedSizeBytes, this.nodes[2 * i2 + 2], 0, this.engine.seedSizeBytes);
            this.haveNode[2 * i2 + 2] = true;
        }
    }

    private void hashSeed(byte[] byArray, byte[] byArray2, byte[] byArray3, byte by, int n2, int n3) {
        this.engine.digest.update(by);
        this.engine.digest.update(byArray2, 0, this.engine.seedSizeBytes);
        this.engine.digest.update(byArray3, 0, 32);
        this.engine.digest.update(Pack.shortToLittleEndian((short)(n2 & 0xFFFF)), 0, 2);
        this.engine.digest.update(Pack.shortToLittleEndian((short)(n3 & 0xFFFF)), 0, 2);
        this.engine.digest.doFinal(byArray, 0, 2 * this.engine.seedSizeBytes);
    }

    private boolean isLeftChild(int n2) {
        return n2 % 2 == 1;
    }

    private boolean hasRightChild(int n2) {
        return 2 * n2 + 2 < this.numNodes && this.exists(n2);
    }

    boolean hasLeftChild(Tree tree, int n2) {
        return 2 * n2 + 1 < this.numNodes;
    }

    private int getParent(int n2) {
        if (this.isLeftChild(n2)) {
            return (n2 - 1) / 2;
        }
        return (n2 - 2) / 2;
    }

    private boolean exists(int n2) {
        if (n2 >= this.numNodes) {
            return false;
        }
        return this.exists[n2];
    }
}

