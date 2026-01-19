/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.xmss;

import java.io.IOException;
import org.bouncycastle.pqc.crypto.xmss.BDS;
import org.bouncycastle.pqc.crypto.xmss.OTSHashAddress;
import org.bouncycastle.pqc.crypto.xmss.XMSSKeyParameters;
import org.bouncycastle.pqc.crypto.xmss.XMSSParameters;
import org.bouncycastle.pqc.crypto.xmss.XMSSStoreableObjectInterface;
import org.bouncycastle.pqc.crypto.xmss.XMSSUtil;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Encodable;
import org.bouncycastle.util.Pack;

public final class XMSSPrivateKeyParameters
extends XMSSKeyParameters
implements XMSSStoreableObjectInterface,
Encodable {
    private final XMSSParameters params;
    private final byte[] secretKeySeed;
    private final byte[] secretKeyPRF;
    private final byte[] publicSeed;
    private final byte[] root;
    private volatile BDS bdsState;

    private XMSSPrivateKeyParameters(Builder builder) {
        super(true, builder.params.getTreeDigest());
        this.params = builder.params;
        if (this.params == null) {
            throw new NullPointerException("params == null");
        }
        int n2 = this.params.getTreeDigestSize();
        byte[] byArray = builder.privateKey;
        if (byArray != null) {
            int n3 = this.params.getHeight();
            int n4 = 4;
            int n5 = n2;
            int n6 = n2;
            int n7 = n2;
            int n8 = n2;
            int n9 = 0;
            int n10 = Pack.bigEndianToInt(byArray, n9);
            if (!XMSSUtil.isIndexValid(n3, n10)) {
                throw new IllegalArgumentException("index out of bounds");
            }
            this.secretKeySeed = XMSSUtil.extractBytesAtOffset(byArray, n9 += n4, n5);
            this.secretKeyPRF = XMSSUtil.extractBytesAtOffset(byArray, n9 += n5, n6);
            this.publicSeed = XMSSUtil.extractBytesAtOffset(byArray, n9 += n6, n7);
            this.root = XMSSUtil.extractBytesAtOffset(byArray, n9 += n7, n8);
            byte[] byArray2 = XMSSUtil.extractBytesAtOffset(byArray, n9 += n8, byArray.length - n9);
            try {
                BDS bDS = (BDS)XMSSUtil.deserialize(byArray2, BDS.class);
                if (bDS.getIndex() != n10) {
                    throw new IllegalStateException("serialized BDS has wrong index");
                }
                this.bdsState = bDS.withWOTSDigest(builder.params.getTreeDigestOID());
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException(iOException.getMessage(), iOException);
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new IllegalArgumentException(classNotFoundException.getMessage(), classNotFoundException);
            }
        } else {
            byte[] byArray3 = builder.secretKeySeed;
            if (byArray3 != null) {
                if (byArray3.length != n2) {
                    throw new IllegalArgumentException("size of secretKeySeed needs to be equal size of digest");
                }
                this.secretKeySeed = byArray3;
            } else {
                this.secretKeySeed = new byte[n2];
            }
            byte[] byArray4 = builder.secretKeyPRF;
            if (byArray4 != null) {
                if (byArray4.length != n2) {
                    throw new IllegalArgumentException("size of secretKeyPRF needs to be equal size of digest");
                }
                this.secretKeyPRF = byArray4;
            } else {
                this.secretKeyPRF = new byte[n2];
            }
            byte[] byArray5 = builder.publicSeed;
            if (byArray5 != null) {
                if (byArray5.length != n2) {
                    throw new IllegalArgumentException("size of publicSeed needs to be equal size of digest");
                }
                this.publicSeed = byArray5;
            } else {
                this.publicSeed = new byte[n2];
            }
            byte[] byArray6 = builder.root;
            if (byArray6 != null) {
                if (byArray6.length != n2) {
                    throw new IllegalArgumentException("size of root needs to be equal size of digest");
                }
                this.root = byArray6;
            } else {
                this.root = new byte[n2];
            }
            BDS bDS = builder.bdsState;
            this.bdsState = bDS != null ? bDS : (builder.index < (1 << this.params.getHeight()) - 2 && byArray5 != null && byArray3 != null ? new BDS(this.params, byArray5, byArray3, (OTSHashAddress)new OTSHashAddress.Builder().build(), builder.index) : new BDS(this.params, (1 << this.params.getHeight()) - 1, builder.index));
            if (builder.maxIndex >= 0 && builder.maxIndex != this.bdsState.getMaxIndex()) {
                throw new IllegalArgumentException("maxIndex set but not reflected in state");
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public long getUsagesRemaining() {
        XMSSPrivateKeyParameters xMSSPrivateKeyParameters = this;
        synchronized (xMSSPrivateKeyParameters) {
            return this.bdsState.getMaxIndex() - this.getIndex() + 1;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte[] getEncoded() throws IOException {
        XMSSPrivateKeyParameters xMSSPrivateKeyParameters = this;
        synchronized (xMSSPrivateKeyParameters) {
            return this.toByteArray();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    XMSSPrivateKeyParameters rollKey() {
        XMSSPrivateKeyParameters xMSSPrivateKeyParameters = this;
        synchronized (xMSSPrivateKeyParameters) {
            this.bdsState = this.bdsState.getIndex() < this.bdsState.getMaxIndex() ? this.bdsState.getNextState(this.publicSeed, this.secretKeySeed, (OTSHashAddress)new OTSHashAddress.Builder().build()) : new BDS(this.params, this.bdsState.getMaxIndex(), this.bdsState.getMaxIndex() + 1);
            return this;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public XMSSPrivateKeyParameters getNextKey() {
        XMSSPrivateKeyParameters xMSSPrivateKeyParameters = this;
        synchronized (xMSSPrivateKeyParameters) {
            XMSSPrivateKeyParameters xMSSPrivateKeyParameters2 = this.extractKeyShard(1);
            return xMSSPrivateKeyParameters2;
        }
    }

    public XMSSPrivateKeyParameters extractKeyShard(int n2) {
        if (n2 < 1) {
            throw new IllegalArgumentException("cannot ask for a shard with 0 keys");
        }
        XMSSPrivateKeyParameters xMSSPrivateKeyParameters = this;
        synchronized (xMSSPrivateKeyParameters) {
            if ((long)n2 <= this.getUsagesRemaining()) {
                XMSSPrivateKeyParameters xMSSPrivateKeyParameters2 = new Builder(this.params).withSecretKeySeed(this.secretKeySeed).withSecretKeyPRF(this.secretKeyPRF).withPublicSeed(this.publicSeed).withRoot(this.root).withIndex(this.getIndex()).withBDSState(this.bdsState.withMaxIndex(this.bdsState.getIndex() + n2 - 1, this.params.getTreeDigestOID())).build();
                if ((long)n2 == this.getUsagesRemaining()) {
                    this.bdsState = new BDS(this.params, this.bdsState.getMaxIndex(), this.getIndex() + n2);
                } else {
                    OTSHashAddress oTSHashAddress = (OTSHashAddress)new OTSHashAddress.Builder().build();
                    for (int i2 = 0; i2 != n2; ++i2) {
                        this.bdsState = this.bdsState.getNextState(this.publicSeed, this.secretKeySeed, oTSHashAddress);
                    }
                }
                return xMSSPrivateKeyParameters2;
            }
            throw new IllegalArgumentException("usageCount exceeds usages remaining");
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte[] toByteArray() {
        XMSSPrivateKeyParameters xMSSPrivateKeyParameters = this;
        synchronized (xMSSPrivateKeyParameters) {
            int n2 = this.params.getTreeDigestSize();
            int n3 = 4;
            int n4 = n2;
            int n5 = n2;
            int n6 = n2;
            int n7 = n2;
            int n8 = n3 + n4 + n5 + n6 + n7;
            byte[] byArray = new byte[n8];
            int n9 = 0;
            Pack.intToBigEndian(this.bdsState.getIndex(), byArray, n9);
            XMSSUtil.copyBytesAtOffset(byArray, this.secretKeySeed, n9 += n3);
            XMSSUtil.copyBytesAtOffset(byArray, this.secretKeyPRF, n9 += n4);
            XMSSUtil.copyBytesAtOffset(byArray, this.publicSeed, n9 += n5);
            XMSSUtil.copyBytesAtOffset(byArray, this.root, n9 += n6);
            byte[] byArray2 = null;
            try {
                byArray2 = XMSSUtil.serialize(this.bdsState);
            }
            catch (IOException iOException) {
                throw new RuntimeException("error serializing bds state: " + iOException.getMessage());
            }
            return Arrays.concatenate(byArray, byArray2);
        }
    }

    public int getIndex() {
        return this.bdsState.getIndex();
    }

    public byte[] getSecretKeySeed() {
        return XMSSUtil.cloneArray(this.secretKeySeed);
    }

    public byte[] getSecretKeyPRF() {
        return XMSSUtil.cloneArray(this.secretKeyPRF);
    }

    public byte[] getPublicSeed() {
        return XMSSUtil.cloneArray(this.publicSeed);
    }

    public byte[] getRoot() {
        return XMSSUtil.cloneArray(this.root);
    }

    BDS getBDSState() {
        return this.bdsState;
    }

    public XMSSParameters getParameters() {
        return this.params;
    }

    public static class Builder {
        private final XMSSParameters params;
        private int index = 0;
        private int maxIndex = -1;
        private byte[] secretKeySeed = null;
        private byte[] secretKeyPRF = null;
        private byte[] publicSeed = null;
        private byte[] root = null;
        private BDS bdsState = null;
        private byte[] privateKey = null;

        public Builder(XMSSParameters xMSSParameters) {
            this.params = xMSSParameters;
        }

        public Builder withIndex(int n2) {
            this.index = n2;
            return this;
        }

        public Builder withMaxIndex(int n2) {
            this.maxIndex = n2;
            return this;
        }

        public Builder withSecretKeySeed(byte[] byArray) {
            this.secretKeySeed = XMSSUtil.cloneArray(byArray);
            return this;
        }

        public Builder withSecretKeyPRF(byte[] byArray) {
            this.secretKeyPRF = XMSSUtil.cloneArray(byArray);
            return this;
        }

        public Builder withPublicSeed(byte[] byArray) {
            this.publicSeed = XMSSUtil.cloneArray(byArray);
            return this;
        }

        public Builder withRoot(byte[] byArray) {
            this.root = XMSSUtil.cloneArray(byArray);
            return this;
        }

        public Builder withBDSState(BDS bDS) {
            this.bdsState = bDS;
            return this;
        }

        public Builder withPrivateKey(byte[] byArray) {
            this.privateKey = XMSSUtil.cloneArray(byArray);
            return this;
        }

        public XMSSPrivateKeyParameters build() {
            return new XMSSPrivateKeyParameters(this);
        }
    }
}

