/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.xmss;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.pqc.crypto.StateAwareMessageSigner;
import org.bouncycastle.pqc.crypto.xmss.BDS;
import org.bouncycastle.pqc.crypto.xmss.BDSStateMap;
import org.bouncycastle.pqc.crypto.xmss.OTSHashAddress;
import org.bouncycastle.pqc.crypto.xmss.WOTSPlus;
import org.bouncycastle.pqc.crypto.xmss.WOTSPlusSignature;
import org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters;
import org.bouncycastle.pqc.crypto.xmss.XMSSMTPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.xmss.XMSSMTPublicKeyParameters;
import org.bouncycastle.pqc.crypto.xmss.XMSSMTSignature;
import org.bouncycastle.pqc.crypto.xmss.XMSSNode;
import org.bouncycastle.pqc.crypto.xmss.XMSSParameters;
import org.bouncycastle.pqc.crypto.xmss.XMSSReducedSignature;
import org.bouncycastle.pqc.crypto.xmss.XMSSUtil;
import org.bouncycastle.pqc.crypto.xmss.XMSSVerifierUtil;
import org.bouncycastle.util.Arrays;

public class XMSSMTSigner
implements StateAwareMessageSigner {
    private XMSSMTPrivateKeyParameters privateKey;
    private XMSSMTPublicKeyParameters publicKey;
    private XMSSMTParameters params;
    private XMSSParameters xmssParams;
    private WOTSPlus wotsPlus;
    private boolean hasGenerated;
    private boolean initSign;

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (bl) {
            this.initSign = true;
            this.hasGenerated = false;
            this.privateKey = (XMSSMTPrivateKeyParameters)cipherParameters;
            this.params = this.privateKey.getParameters();
            this.xmssParams = this.params.getXMSSParameters();
        } else {
            this.initSign = false;
            this.publicKey = (XMSSMTPublicKeyParameters)cipherParameters;
            this.params = this.publicKey.getParameters();
            this.xmssParams = this.params.getXMSSParameters();
        }
        this.wotsPlus = this.params.getWOTSPlus();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte[] generateSignature(byte[] byArray) {
        if (byArray == null) {
            throw new NullPointerException("message == null");
        }
        if (this.initSign) {
            if (this.privateKey == null) {
                throw new IllegalStateException("signing key no longer usable");
            }
        } else {
            throw new IllegalStateException("signer not initialized for signature generation");
        }
        XMSSMTPrivateKeyParameters xMSSMTPrivateKeyParameters = this.privateKey;
        synchronized (xMSSMTPrivateKeyParameters) {
            byte[] byArray2;
            if (this.privateKey.getUsagesRemaining() <= 0L) {
                throw new IllegalStateException("no usages of private key remaining");
            }
            if (this.privateKey.getBDSState().isEmpty()) {
                throw new IllegalStateException("not initialized");
            }
            try {
                BDSStateMap bDSStateMap = this.privateKey.getBDSState();
                long l2 = this.privateKey.getIndex();
                int n2 = this.params.getHeight();
                int n3 = this.xmssParams.getHeight();
                if (this.privateKey.getUsagesRemaining() <= 0L) {
                    throw new IllegalStateException("index out of bounds");
                }
                byte[] byArray3 = this.wotsPlus.getKhf().PRF(this.privateKey.getSecretKeyPRF(), XMSSUtil.toBytesBigEndian(l2, 32));
                byte[] byArray4 = Arrays.concatenate(byArray3, this.privateKey.getRoot(), XMSSUtil.toBytesBigEndian(l2, this.params.getTreeDigestSize()));
                byte[] byArray5 = this.wotsPlus.getKhf().HMsg(byArray4, byArray);
                this.hasGenerated = true;
                XMSSMTSignature xMSSMTSignature = new XMSSMTSignature.Builder(this.params).withIndex(l2).withRandom(byArray3).build();
                long l3 = XMSSUtil.getTreeIndex(l2, n3);
                int n4 = XMSSUtil.getLeafIndex(l2, n3);
                this.wotsPlus.importKeys(new byte[this.params.getTreeDigestSize()], this.privateKey.getPublicSeed());
                OTSHashAddress oTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)new OTSHashAddress.Builder().withTreeAddress(l3)).withOTSAddress(n4).build();
                if (bDSStateMap.get(0) == null || n4 == 0) {
                    bDSStateMap.put(0, new BDS(this.xmssParams, this.privateKey.getPublicSeed(), this.privateKey.getSecretKeySeed(), oTSHashAddress));
                }
                WOTSPlusSignature wOTSPlusSignature = this.wotsSign(byArray5, oTSHashAddress);
                XMSSReducedSignature xMSSReducedSignature = new XMSSReducedSignature.Builder(this.xmssParams).withWOTSPlusSignature(wOTSPlusSignature).withAuthPath(bDSStateMap.get(0).getAuthenticationPath()).build();
                xMSSMTSignature.getReducedSignatures().add(xMSSReducedSignature);
                for (int i2 = 1; i2 < this.params.getLayers(); ++i2) {
                    XMSSNode xMSSNode = bDSStateMap.get(i2 - 1).getRoot();
                    n4 = XMSSUtil.getLeafIndex(l3, n3);
                    l3 = XMSSUtil.getTreeIndex(l3, n3);
                    oTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)new OTSHashAddress.Builder().withLayerAddress(i2)).withTreeAddress(l3)).withOTSAddress(n4).build();
                    wOTSPlusSignature = this.wotsSign(xMSSNode.getValue(), oTSHashAddress);
                    if (bDSStateMap.get(i2) == null || XMSSUtil.isNewBDSInitNeeded(l2, n3, i2)) {
                        bDSStateMap.put(i2, new BDS(this.xmssParams, this.privateKey.getPublicSeed(), this.privateKey.getSecretKeySeed(), oTSHashAddress));
                    }
                    xMSSReducedSignature = new XMSSReducedSignature.Builder(this.xmssParams).withWOTSPlusSignature(wOTSPlusSignature).withAuthPath(bDSStateMap.get(i2).getAuthenticationPath()).build();
                    xMSSMTSignature.getReducedSignatures().add(xMSSReducedSignature);
                }
                byArray2 = xMSSMTSignature.toByteArray();
                this.privateKey.rollKey();
            }
            catch (Throwable throwable) {
                this.privateKey.rollKey();
                throw throwable;
            }
            return byArray2;
        }
    }

    @Override
    public boolean verifySignature(byte[] byArray, byte[] byArray2) {
        if (byArray == null) {
            throw new NullPointerException("message == null");
        }
        if (byArray2 == null) {
            throw new NullPointerException("signature == null");
        }
        if (this.publicKey == null) {
            throw new NullPointerException("publicKey == null");
        }
        XMSSMTSignature xMSSMTSignature = new XMSSMTSignature.Builder(this.params).withSignature(byArray2).build();
        byte[] byArray3 = Arrays.concatenate(xMSSMTSignature.getRandom(), this.publicKey.getRoot(), XMSSUtil.toBytesBigEndian(xMSSMTSignature.getIndex(), this.params.getTreeDigestSize()));
        byte[] byArray4 = this.wotsPlus.getKhf().HMsg(byArray3, byArray);
        long l2 = xMSSMTSignature.getIndex();
        int n2 = this.xmssParams.getHeight();
        long l3 = XMSSUtil.getTreeIndex(l2, n2);
        int n3 = XMSSUtil.getLeafIndex(l2, n2);
        this.wotsPlus.importKeys(new byte[this.params.getTreeDigestSize()], this.publicKey.getPublicSeed());
        OTSHashAddress oTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)new OTSHashAddress.Builder().withTreeAddress(l3)).withOTSAddress(n3).build();
        XMSSReducedSignature xMSSReducedSignature = xMSSMTSignature.getReducedSignatures().get(0);
        XMSSNode xMSSNode = XMSSVerifierUtil.getRootNodeFromSignature(this.wotsPlus, n2, byArray4, xMSSReducedSignature, oTSHashAddress, n3);
        for (int i2 = 1; i2 < this.params.getLayers(); ++i2) {
            xMSSReducedSignature = xMSSMTSignature.getReducedSignatures().get(i2);
            n3 = XMSSUtil.getLeafIndex(l3, n2);
            l3 = XMSSUtil.getTreeIndex(l3, n2);
            oTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)new OTSHashAddress.Builder().withLayerAddress(i2)).withTreeAddress(l3)).withOTSAddress(n3).build();
            xMSSNode = XMSSVerifierUtil.getRootNodeFromSignature(this.wotsPlus, n2, xMSSNode.getValue(), xMSSReducedSignature, oTSHashAddress, n3);
        }
        return Arrays.constantTimeAreEqual(xMSSNode.getValue(), this.publicKey.getRoot());
    }

    private WOTSPlusSignature wotsSign(byte[] byArray, OTSHashAddress oTSHashAddress) {
        if (byArray.length != this.params.getTreeDigestSize()) {
            throw new IllegalArgumentException("size of messageDigest needs to be equal to size of digest");
        }
        if (oTSHashAddress == null) {
            throw new NullPointerException("otsHashAddress == null");
        }
        this.wotsPlus.importKeys(this.wotsPlus.getWOTSPlusSecretKey(this.privateKey.getSecretKeySeed(), oTSHashAddress), this.privateKey.getPublicSeed());
        return this.wotsPlus.sign(byArray, oTSHashAddress);
    }

    public long getUsagesRemaining() {
        return this.privateKey.getUsagesRemaining();
    }

    @Override
    public AsymmetricKeyParameter getUpdatedPrivateKey() {
        if (this.hasGenerated) {
            XMSSMTPrivateKeyParameters xMSSMTPrivateKeyParameters = this.privateKey;
            this.privateKey = null;
            return xMSSMTPrivateKeyParameters;
        }
        XMSSMTPrivateKeyParameters xMSSMTPrivateKeyParameters = this.privateKey;
        if (xMSSMTPrivateKeyParameters != null) {
            this.privateKey = this.privateKey.getNextKey();
        }
        return xMSSMTPrivateKeyParameters;
    }
}

