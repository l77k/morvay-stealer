/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.xmss;

import org.bouncycastle.pqc.crypto.xmss.HashTreeAddress;
import org.bouncycastle.pqc.crypto.xmss.LTreeAddress;
import org.bouncycastle.pqc.crypto.xmss.WOTSPlus;
import org.bouncycastle.pqc.crypto.xmss.WOTSPlusPublicKeyParameters;
import org.bouncycastle.pqc.crypto.xmss.XMSSAddress;
import org.bouncycastle.pqc.crypto.xmss.XMSSNode;

class XMSSNodeUtil {
    XMSSNodeUtil() {
    }

    static XMSSNode lTree(WOTSPlus wOTSPlus, WOTSPlusPublicKeyParameters wOTSPlusPublicKeyParameters, LTreeAddress lTreeAddress) {
        int n2;
        if (wOTSPlusPublicKeyParameters == null) {
            throw new NullPointerException("publicKey == null");
        }
        if (lTreeAddress == null) {
            throw new NullPointerException("address == null");
        }
        int n3 = wOTSPlus.getParams().getLen();
        byte[][] byArray = wOTSPlusPublicKeyParameters.toByteArray();
        XMSSNode[] xMSSNodeArray = new XMSSNode[byArray.length];
        for (n2 = 0; n2 < byArray.length; ++n2) {
            xMSSNodeArray[n2] = new XMSSNode(0, byArray[n2]);
        }
        lTreeAddress = (LTreeAddress)((LTreeAddress.Builder)((LTreeAddress.Builder)((LTreeAddress.Builder)new LTreeAddress.Builder().withLayerAddress(lTreeAddress.getLayerAddress())).withTreeAddress(lTreeAddress.getTreeAddress())).withLTreeAddress(lTreeAddress.getLTreeAddress()).withTreeHeight(0).withTreeIndex(lTreeAddress.getTreeIndex()).withKeyAndMask(lTreeAddress.getKeyAndMask())).build();
        while (n3 > 1) {
            for (n2 = 0; n2 < (int)Math.floor(n3 / 2); ++n2) {
                lTreeAddress = (LTreeAddress)((LTreeAddress.Builder)((LTreeAddress.Builder)((LTreeAddress.Builder)new LTreeAddress.Builder().withLayerAddress(lTreeAddress.getLayerAddress())).withTreeAddress(lTreeAddress.getTreeAddress())).withLTreeAddress(lTreeAddress.getLTreeAddress()).withTreeHeight(lTreeAddress.getTreeHeight()).withTreeIndex(n2).withKeyAndMask(lTreeAddress.getKeyAndMask())).build();
                xMSSNodeArray[n2] = XMSSNodeUtil.randomizeHash(wOTSPlus, xMSSNodeArray[2 * n2], xMSSNodeArray[2 * n2 + 1], lTreeAddress);
            }
            if (n3 % 2 == 1) {
                xMSSNodeArray[(int)Math.floor((double)((double)(n3 / 2)))] = xMSSNodeArray[n3 - 1];
            }
            n3 = (int)Math.ceil((double)n3 / 2.0);
            lTreeAddress = (LTreeAddress)((LTreeAddress.Builder)((LTreeAddress.Builder)((LTreeAddress.Builder)new LTreeAddress.Builder().withLayerAddress(lTreeAddress.getLayerAddress())).withTreeAddress(lTreeAddress.getTreeAddress())).withLTreeAddress(lTreeAddress.getLTreeAddress()).withTreeHeight(lTreeAddress.getTreeHeight() + 1).withTreeIndex(lTreeAddress.getTreeIndex()).withKeyAndMask(lTreeAddress.getKeyAndMask())).build();
        }
        return xMSSNodeArray[0];
    }

    static XMSSNode randomizeHash(WOTSPlus wOTSPlus, XMSSNode xMSSNode, XMSSNode xMSSNode2, XMSSAddress xMSSAddress) {
        int n2;
        Object object;
        Object object2;
        Object object3;
        if (xMSSNode == null) {
            throw new NullPointerException("left == null");
        }
        if (xMSSNode2 == null) {
            throw new NullPointerException("right == null");
        }
        if (xMSSNode.getHeight() != xMSSNode2.getHeight()) {
            throw new IllegalStateException("height of both nodes must be equal");
        }
        if (xMSSAddress == null) {
            throw new NullPointerException("address == null");
        }
        byte[] byArray = wOTSPlus.getPublicSeed();
        if (xMSSAddress instanceof LTreeAddress) {
            object3 = (LTreeAddress)xMSSAddress;
            xMSSAddress = (LTreeAddress)((LTreeAddress.Builder)((LTreeAddress.Builder)((LTreeAddress.Builder)new LTreeAddress.Builder().withLayerAddress(((XMSSAddress)object3).getLayerAddress())).withTreeAddress(((XMSSAddress)object3).getTreeAddress())).withLTreeAddress(((LTreeAddress)object3).getLTreeAddress()).withTreeHeight(((LTreeAddress)object3).getTreeHeight()).withTreeIndex(((LTreeAddress)object3).getTreeIndex()).withKeyAndMask(0)).build();
        } else if (xMSSAddress instanceof HashTreeAddress) {
            object3 = (HashTreeAddress)xMSSAddress;
            xMSSAddress = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)new HashTreeAddress.Builder().withLayerAddress(((XMSSAddress)object3).getLayerAddress())).withTreeAddress(((XMSSAddress)object3).getTreeAddress())).withTreeHeight(((HashTreeAddress)object3).getTreeHeight()).withTreeIndex(((HashTreeAddress)object3).getTreeIndex()).withKeyAndMask(0)).build();
        }
        object3 = wOTSPlus.getKhf().PRF(byArray, xMSSAddress.toByteArray());
        if (xMSSAddress instanceof LTreeAddress) {
            object2 = (LTreeAddress)xMSSAddress;
            xMSSAddress = (LTreeAddress)((LTreeAddress.Builder)((LTreeAddress.Builder)((LTreeAddress.Builder)new LTreeAddress.Builder().withLayerAddress(((XMSSAddress)object2).getLayerAddress())).withTreeAddress(((XMSSAddress)object2).getTreeAddress())).withLTreeAddress(((LTreeAddress)object2).getLTreeAddress()).withTreeHeight(((LTreeAddress)object2).getTreeHeight()).withTreeIndex(((LTreeAddress)object2).getTreeIndex()).withKeyAndMask(1)).build();
        } else if (xMSSAddress instanceof HashTreeAddress) {
            object2 = (HashTreeAddress)xMSSAddress;
            xMSSAddress = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)new HashTreeAddress.Builder().withLayerAddress(((XMSSAddress)object2).getLayerAddress())).withTreeAddress(((XMSSAddress)object2).getTreeAddress())).withTreeHeight(((HashTreeAddress)object2).getTreeHeight()).withTreeIndex(((HashTreeAddress)object2).getTreeIndex()).withKeyAndMask(1)).build();
        }
        object2 = wOTSPlus.getKhf().PRF(byArray, xMSSAddress.toByteArray());
        if (xMSSAddress instanceof LTreeAddress) {
            object = (LTreeAddress)xMSSAddress;
            xMSSAddress = (LTreeAddress)((LTreeAddress.Builder)((LTreeAddress.Builder)((LTreeAddress.Builder)new LTreeAddress.Builder().withLayerAddress(((XMSSAddress)object).getLayerAddress())).withTreeAddress(((XMSSAddress)object).getTreeAddress())).withLTreeAddress(((LTreeAddress)object).getLTreeAddress()).withTreeHeight(((LTreeAddress)object).getTreeHeight()).withTreeIndex(((LTreeAddress)object).getTreeIndex()).withKeyAndMask(2)).build();
        } else if (xMSSAddress instanceof HashTreeAddress) {
            object = (HashTreeAddress)xMSSAddress;
            xMSSAddress = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)new HashTreeAddress.Builder().withLayerAddress(((XMSSAddress)object).getLayerAddress())).withTreeAddress(((XMSSAddress)object).getTreeAddress())).withTreeHeight(((HashTreeAddress)object).getTreeHeight()).withTreeIndex(((HashTreeAddress)object).getTreeIndex()).withKeyAndMask(2)).build();
        }
        object = wOTSPlus.getKhf().PRF(byArray, xMSSAddress.toByteArray());
        int n3 = wOTSPlus.getParams().getTreeDigestSize();
        byte[] byArray2 = new byte[2 * n3];
        for (n2 = 0; n2 < n3; ++n2) {
            byArray2[n2] = (byte)(xMSSNode.getValue()[n2] ^ object2[n2]);
        }
        for (n2 = 0; n2 < n3; ++n2) {
            byArray2[n2 + n3] = (byte)(xMSSNode2.getValue()[n2] ^ object[n2]);
        }
        byte[] byArray3 = wOTSPlus.getKhf().H((byte[])object3, byArray2);
        return new XMSSNode(xMSSNode.getHeight(), byArray3);
    }
}

