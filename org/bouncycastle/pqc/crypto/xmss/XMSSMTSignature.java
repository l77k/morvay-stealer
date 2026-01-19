/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.xmss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters;
import org.bouncycastle.pqc.crypto.xmss.XMSSReducedSignature;
import org.bouncycastle.pqc.crypto.xmss.XMSSStoreableObjectInterface;
import org.bouncycastle.pqc.crypto.xmss.XMSSUtil;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Encodable;

public final class XMSSMTSignature
implements XMSSStoreableObjectInterface,
Encodable {
    private final XMSSMTParameters params;
    private final long index;
    private final byte[] random;
    private final List<XMSSReducedSignature> reducedSignatures;

    private XMSSMTSignature(Builder builder) {
        this.params = builder.params;
        if (this.params == null) {
            throw new NullPointerException("params == null");
        }
        int n2 = this.params.getTreeDigestSize();
        byte[] byArray = builder.signature;
        if (byArray != null) {
            int n3;
            int n4;
            int n5;
            int n6 = this.params.getWOTSPlus().getParams().getLen();
            int n7 = (int)Math.ceil((double)this.params.getHeight() / 8.0);
            int n8 = n7 + (n5 = n2) + (n4 = (n3 = (this.params.getHeight() / this.params.getLayers() + n6) * n2) * this.params.getLayers());
            if (byArray.length != n8) {
                throw new IllegalArgumentException("signature has wrong size");
            }
            int n9 = 0;
            this.index = XMSSUtil.bytesToXBigEndian(byArray, n9, n7);
            if (!XMSSUtil.isIndexValid(this.params.getHeight(), this.index)) {
                throw new IllegalArgumentException("index out of bounds");
            }
            this.random = XMSSUtil.extractBytesAtOffset(byArray, n9 += n7, n5);
            n9 += n5;
            this.reducedSignatures = new ArrayList<XMSSReducedSignature>();
            while (n9 < byArray.length) {
                XMSSReducedSignature xMSSReducedSignature = new XMSSReducedSignature.Builder(this.params.getXMSSParameters()).withReducedSignature(XMSSUtil.extractBytesAtOffset(byArray, n9, n3)).build();
                this.reducedSignatures.add(xMSSReducedSignature);
                n9 += n3;
            }
        } else {
            this.index = builder.index;
            byte[] byArray2 = builder.random;
            if (byArray2 != null) {
                if (byArray2.length != n2) {
                    throw new IllegalArgumentException("size of random needs to be equal to size of digest");
                }
                this.random = byArray2;
            } else {
                this.random = new byte[n2];
            }
            List<Object> list = builder.reducedSignatures;
            this.reducedSignatures = list != null ? list : new ArrayList<XMSSReducedSignature>();
        }
    }

    @Override
    public byte[] getEncoded() throws IOException {
        return this.toByteArray();
    }

    @Override
    public byte[] toByteArray() {
        int n2 = this.params.getTreeDigestSize();
        int n3 = this.params.getWOTSPlus().getParams().getLen();
        int n4 = (int)Math.ceil((double)this.params.getHeight() / 8.0);
        int n5 = n2;
        int n6 = (this.params.getHeight() / this.params.getLayers() + n3) * n2;
        int n7 = n6 * this.params.getLayers();
        int n8 = n4 + n5 + n7;
        byte[] byArray = new byte[n8];
        int n9 = 0;
        byte[] byArray2 = XMSSUtil.toBytesBigEndian(this.index, n4);
        XMSSUtil.copyBytesAtOffset(byArray, byArray2, n9);
        XMSSUtil.copyBytesAtOffset(byArray, this.random, n9 += n4);
        n9 += n5;
        for (XMSSReducedSignature xMSSReducedSignature : this.reducedSignatures) {
            byte[] byArray3 = xMSSReducedSignature.toByteArray();
            XMSSUtil.copyBytesAtOffset(byArray, byArray3, n9);
            n9 += n6;
        }
        return byArray;
    }

    public long getIndex() {
        return this.index;
    }

    public byte[] getRandom() {
        return XMSSUtil.cloneArray(this.random);
    }

    public List<XMSSReducedSignature> getReducedSignatures() {
        return this.reducedSignatures;
    }

    public static class Builder {
        private final XMSSMTParameters params;
        private long index = 0L;
        private byte[] random = null;
        private List<XMSSReducedSignature> reducedSignatures = null;
        private byte[] signature = null;

        public Builder(XMSSMTParameters xMSSMTParameters) {
            this.params = xMSSMTParameters;
        }

        public Builder withIndex(long l2) {
            this.index = l2;
            return this;
        }

        public Builder withRandom(byte[] byArray) {
            this.random = XMSSUtil.cloneArray(byArray);
            return this;
        }

        public Builder withReducedSignatures(List<XMSSReducedSignature> list) {
            this.reducedSignatures = list;
            return this;
        }

        public Builder withSignature(byte[] byArray) {
            this.signature = Arrays.clone(byArray);
            return this;
        }

        public XMSSMTSignature build() {
            return new XMSSMTSignature(this);
        }
    }
}

