/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.xmss;

import java.io.IOException;
import org.bouncycastle.pqc.crypto.xmss.XMSSParameters;
import org.bouncycastle.pqc.crypto.xmss.XMSSReducedSignature;
import org.bouncycastle.pqc.crypto.xmss.XMSSStoreableObjectInterface;
import org.bouncycastle.pqc.crypto.xmss.XMSSUtil;
import org.bouncycastle.util.Encodable;
import org.bouncycastle.util.Pack;

public final class XMSSSignature
extends XMSSReducedSignature
implements XMSSStoreableObjectInterface,
Encodable {
    private final int index;
    private final byte[] random;

    private XMSSSignature(Builder builder) {
        super(builder);
        this.index = builder.index;
        int n2 = this.getParams().getTreeDigestSize();
        byte[] byArray = builder.random;
        if (byArray != null) {
            if (byArray.length != n2) {
                throw new IllegalArgumentException("size of random needs to be equal to size of digest");
            }
            this.random = byArray;
        } else {
            this.random = new byte[n2];
        }
    }

    @Override
    public byte[] getEncoded() throws IOException {
        return this.toByteArray();
    }

    @Override
    public byte[] toByteArray() {
        int n2;
        int n3 = this.getParams().getTreeDigestSize();
        int n4 = 4;
        int n5 = n3;
        int n6 = this.getParams().getWOTSPlus().getParams().getLen() * n3;
        int n7 = this.getParams().getHeight() * n3;
        int n8 = n4 + n5 + n6 + n7;
        byte[] byArray = new byte[n8];
        int n9 = 0;
        Pack.intToBigEndian(this.index, byArray, n9);
        XMSSUtil.copyBytesAtOffset(byArray, this.random, n9 += n4);
        n9 += n5;
        byte[][] byArray2 = this.getWOTSPlusSignature().toByteArray();
        for (n2 = 0; n2 < byArray2.length; ++n2) {
            XMSSUtil.copyBytesAtOffset(byArray, byArray2[n2], n9);
            n9 += n3;
        }
        for (n2 = 0; n2 < this.getAuthPath().size(); ++n2) {
            byte[] byArray3 = this.getAuthPath().get(n2).getValue();
            XMSSUtil.copyBytesAtOffset(byArray, byArray3, n9);
            n9 += n3;
        }
        return byArray;
    }

    public int getIndex() {
        return this.index;
    }

    public byte[] getRandom() {
        return XMSSUtil.cloneArray(this.random);
    }

    public static class Builder
    extends XMSSReducedSignature.Builder {
        private final XMSSParameters params;
        private int index = 0;
        private byte[] random = null;

        public Builder(XMSSParameters xMSSParameters) {
            super(xMSSParameters);
            this.params = xMSSParameters;
        }

        public Builder withIndex(int n2) {
            this.index = n2;
            return this;
        }

        public Builder withRandom(byte[] byArray) {
            this.random = XMSSUtil.cloneArray(byArray);
            return this;
        }

        public Builder withSignature(byte[] byArray) {
            if (byArray == null) {
                throw new NullPointerException("signature == null");
            }
            int n2 = this.params.getTreeDigestSize();
            int n3 = this.params.getWOTSPlus().getParams().getLen();
            int n4 = this.params.getHeight();
            int n5 = 4;
            int n6 = n2;
            int n7 = n3 * n2;
            int n8 = n4 * n2;
            int n9 = 0;
            this.index = Pack.bigEndianToInt(byArray, n9);
            this.random = XMSSUtil.extractBytesAtOffset(byArray, n9 += n5, n6);
            this.withReducedSignature(XMSSUtil.extractBytesAtOffset(byArray, n9 += n6, n7 + n8));
            return this;
        }

        @Override
        public XMSSSignature build() {
            return new XMSSSignature(this);
        }
    }
}

