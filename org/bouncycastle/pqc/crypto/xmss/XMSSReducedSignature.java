/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.xmss;

import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.pqc.crypto.xmss.WOTSPlusSignature;
import org.bouncycastle.pqc.crypto.xmss.XMSSNode;
import org.bouncycastle.pqc.crypto.xmss.XMSSParameters;
import org.bouncycastle.pqc.crypto.xmss.XMSSStoreableObjectInterface;
import org.bouncycastle.pqc.crypto.xmss.XMSSUtil;

public class XMSSReducedSignature
implements XMSSStoreableObjectInterface {
    private final XMSSParameters params;
    private final WOTSPlusSignature wotsPlusSignature;
    private final List<XMSSNode> authPath;

    protected XMSSReducedSignature(Builder builder) {
        this.params = builder.params;
        if (this.params == null) {
            throw new NullPointerException("params == null");
        }
        int n2 = this.params.getTreeDigestSize();
        int n3 = this.params.getWOTSPlus().getParams().getLen();
        int n4 = this.params.getHeight();
        byte[] byArray = builder.reducedSignature;
        if (byArray != null) {
            int n5 = n3 * n2;
            int n6 = n4 * n2;
            int n7 = n5 + n6;
            if (byArray.length != n7) {
                throw new IllegalArgumentException("signature has wrong size");
            }
            int n8 = 0;
            byte[][] byArrayArray = new byte[n3][];
            for (int i2 = 0; i2 < byArrayArray.length; ++i2) {
                byArrayArray[i2] = XMSSUtil.extractBytesAtOffset(byArray, n8, n2);
                n8 += n2;
            }
            this.wotsPlusSignature = new WOTSPlusSignature(this.params.getWOTSPlus().getParams(), byArrayArray);
            ArrayList<XMSSNode> arrayList = new ArrayList<XMSSNode>();
            for (int i3 = 0; i3 < n4; ++i3) {
                arrayList.add(new XMSSNode(i3, XMSSUtil.extractBytesAtOffset(byArray, n8, n2)));
                n8 += n2;
            }
            this.authPath = arrayList;
        } else {
            WOTSPlusSignature wOTSPlusSignature = builder.wotsPlusSignature;
            this.wotsPlusSignature = wOTSPlusSignature != null ? wOTSPlusSignature : new WOTSPlusSignature(this.params.getWOTSPlus().getParams(), new byte[n3][n2]);
            List list = builder.authPath;
            if (list != null) {
                if (list.size() != n4) {
                    throw new IllegalArgumentException("size of authPath needs to be equal to height of tree");
                }
                this.authPath = list;
            } else {
                this.authPath = new ArrayList<XMSSNode>();
            }
        }
    }

    @Override
    public byte[] toByteArray() {
        int n2;
        int n3 = this.params.getTreeDigestSize();
        int n4 = this.params.getWOTSPlus().getParams().getLen() * n3;
        int n5 = this.params.getHeight() * n3;
        int n6 = n4 + n5;
        byte[] byArray = new byte[n6];
        int n7 = 0;
        byte[][] byArray2 = this.wotsPlusSignature.toByteArray();
        for (n2 = 0; n2 < byArray2.length; ++n2) {
            XMSSUtil.copyBytesAtOffset(byArray, byArray2[n2], n7);
            n7 += n3;
        }
        for (n2 = 0; n2 < this.authPath.size(); ++n2) {
            byte[] byArray3 = this.authPath.get(n2).getValue();
            XMSSUtil.copyBytesAtOffset(byArray, byArray3, n7);
            n7 += n3;
        }
        return byArray;
    }

    public XMSSParameters getParams() {
        return this.params;
    }

    public WOTSPlusSignature getWOTSPlusSignature() {
        return this.wotsPlusSignature;
    }

    public List<XMSSNode> getAuthPath() {
        return this.authPath;
    }

    public static class Builder {
        private final XMSSParameters params;
        private WOTSPlusSignature wotsPlusSignature = null;
        private List<XMSSNode> authPath = null;
        private byte[] reducedSignature = null;

        public Builder(XMSSParameters xMSSParameters) {
            this.params = xMSSParameters;
        }

        public Builder withWOTSPlusSignature(WOTSPlusSignature wOTSPlusSignature) {
            this.wotsPlusSignature = wOTSPlusSignature;
            return this;
        }

        public Builder withAuthPath(List<XMSSNode> list) {
            this.authPath = list;
            return this;
        }

        public Builder withReducedSignature(byte[] byArray) {
            this.reducedSignature = XMSSUtil.cloneArray(byArray);
            return this;
        }

        public XMSSReducedSignature build() {
            return new XMSSReducedSignature(this);
        }
    }
}

