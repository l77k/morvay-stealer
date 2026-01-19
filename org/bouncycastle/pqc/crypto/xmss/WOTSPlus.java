/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.xmss;

import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.pqc.crypto.xmss.KeyedHashFunctions;
import org.bouncycastle.pqc.crypto.xmss.OTSHashAddress;
import org.bouncycastle.pqc.crypto.xmss.WOTSPlusParameters;
import org.bouncycastle.pqc.crypto.xmss.WOTSPlusPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.xmss.WOTSPlusPublicKeyParameters;
import org.bouncycastle.pqc.crypto.xmss.WOTSPlusSignature;
import org.bouncycastle.pqc.crypto.xmss.XMSSUtil;
import org.bouncycastle.util.Arrays;

final class WOTSPlus {
    private final WOTSPlusParameters params;
    private final KeyedHashFunctions khf;
    private byte[] secretKeySeed;
    private byte[] publicSeed;

    WOTSPlus(WOTSPlusParameters wOTSPlusParameters) {
        if (wOTSPlusParameters == null) {
            throw new NullPointerException("params == null");
        }
        this.params = wOTSPlusParameters;
        int n2 = wOTSPlusParameters.getTreeDigestSize();
        this.khf = new KeyedHashFunctions(wOTSPlusParameters.getTreeDigest(), n2);
        this.secretKeySeed = new byte[n2];
        this.publicSeed = new byte[n2];
    }

    void importKeys(byte[] byArray, byte[] byArray2) {
        if (byArray == null) {
            throw new NullPointerException("secretKeySeed == null");
        }
        if (byArray.length != this.params.getTreeDigestSize()) {
            throw new IllegalArgumentException("size of secretKeySeed needs to be equal to size of digest");
        }
        if (byArray2 == null) {
            throw new NullPointerException("publicSeed == null");
        }
        if (byArray2.length != this.params.getTreeDigestSize()) {
            throw new IllegalArgumentException("size of publicSeed needs to be equal to size of digest");
        }
        this.secretKeySeed = byArray;
        this.publicSeed = byArray2;
    }

    WOTSPlusSignature sign(byte[] byArray, OTSHashAddress oTSHashAddress) {
        int n2;
        if (byArray == null) {
            throw new NullPointerException("messageDigest == null");
        }
        if (byArray.length != this.params.getTreeDigestSize()) {
            throw new IllegalArgumentException("size of messageDigest needs to be equal to size of digest");
        }
        if (oTSHashAddress == null) {
            throw new NullPointerException("otsHashAddress == null");
        }
        List<Integer> list = this.convertToBaseW(byArray, this.params.getWinternitzParameter(), this.params.getLen1());
        int n3 = 0;
        for (n2 = 0; n2 < this.params.getLen1(); ++n2) {
            n3 += this.params.getWinternitzParameter() - 1 - list.get(n2);
        }
        n2 = (int)Math.ceil((double)(this.params.getLen2() * XMSSUtil.log2(this.params.getWinternitzParameter())) / 8.0);
        List<Integer> list2 = this.convertToBaseW(XMSSUtil.toBytesBigEndian(n3 <<= 8 - this.params.getLen2() * XMSSUtil.log2(this.params.getWinternitzParameter()) % 8, n2), this.params.getWinternitzParameter(), this.params.getLen2());
        list.addAll(list2);
        byte[][] byArrayArray = new byte[this.params.getLen()][];
        for (int i2 = 0; i2 < this.params.getLen(); ++i2) {
            oTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)((OTSHashAddress.Builder)new OTSHashAddress.Builder().withLayerAddress(oTSHashAddress.getLayerAddress())).withTreeAddress(oTSHashAddress.getTreeAddress())).withOTSAddress(oTSHashAddress.getOTSAddress()).withChainAddress(i2).withHashAddress(oTSHashAddress.getHashAddress()).withKeyAndMask(oTSHashAddress.getKeyAndMask())).build();
            byArrayArray[i2] = this.chain(this.expandSecretKeySeed(i2), 0, list.get(i2), oTSHashAddress);
        }
        return new WOTSPlusSignature(this.params, byArrayArray);
    }

    WOTSPlusPublicKeyParameters getPublicKeyFromSignature(byte[] byArray, WOTSPlusSignature wOTSPlusSignature, OTSHashAddress oTSHashAddress) {
        int n2;
        if (byArray == null) {
            throw new NullPointerException("messageDigest == null");
        }
        if (byArray.length != this.params.getTreeDigestSize()) {
            throw new IllegalArgumentException("size of messageDigest needs to be equal to size of digest");
        }
        if (wOTSPlusSignature == null) {
            throw new NullPointerException("signature == null");
        }
        if (oTSHashAddress == null) {
            throw new NullPointerException("otsHashAddress == null");
        }
        List<Integer> list = this.convertToBaseW(byArray, this.params.getWinternitzParameter(), this.params.getLen1());
        int n3 = 0;
        for (n2 = 0; n2 < this.params.getLen1(); ++n2) {
            n3 += this.params.getWinternitzParameter() - 1 - list.get(n2);
        }
        n2 = (int)Math.ceil((double)(this.params.getLen2() * XMSSUtil.log2(this.params.getWinternitzParameter())) / 8.0);
        List<Integer> list2 = this.convertToBaseW(XMSSUtil.toBytesBigEndian(n3 <<= 8 - this.params.getLen2() * XMSSUtil.log2(this.params.getWinternitzParameter()) % 8, n2), this.params.getWinternitzParameter(), this.params.getLen2());
        list.addAll(list2);
        byte[][] byArrayArray = new byte[this.params.getLen()][];
        for (int i2 = 0; i2 < this.params.getLen(); ++i2) {
            oTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)((OTSHashAddress.Builder)new OTSHashAddress.Builder().withLayerAddress(oTSHashAddress.getLayerAddress())).withTreeAddress(oTSHashAddress.getTreeAddress())).withOTSAddress(oTSHashAddress.getOTSAddress()).withChainAddress(i2).withHashAddress(oTSHashAddress.getHashAddress()).withKeyAndMask(oTSHashAddress.getKeyAndMask())).build();
            byArrayArray[i2] = this.chain(wOTSPlusSignature.toByteArray()[i2], list.get(i2), this.params.getWinternitzParameter() - 1 - list.get(i2), oTSHashAddress);
        }
        return new WOTSPlusPublicKeyParameters(this.params, byArrayArray);
    }

    private byte[] chain(byte[] byArray, int n2, int n3, OTSHashAddress oTSHashAddress) {
        int n4 = this.params.getTreeDigestSize();
        if (byArray == null) {
            throw new NullPointerException("startHash == null");
        }
        if (byArray.length != n4) {
            throw new IllegalArgumentException("startHash needs to be " + n4 + "bytes");
        }
        if (oTSHashAddress == null) {
            throw new NullPointerException("otsHashAddress == null");
        }
        if (oTSHashAddress.toByteArray() == null) {
            throw new NullPointerException("otsHashAddress byte array == null");
        }
        if (n2 + n3 > this.params.getWinternitzParameter() - 1) {
            throw new IllegalArgumentException("max chain length must not be greater than w");
        }
        if (n3 == 0) {
            return byArray;
        }
        byte[] byArray2 = this.chain(byArray, n2, n3 - 1, oTSHashAddress);
        oTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)((OTSHashAddress.Builder)new OTSHashAddress.Builder().withLayerAddress(oTSHashAddress.getLayerAddress())).withTreeAddress(oTSHashAddress.getTreeAddress())).withOTSAddress(oTSHashAddress.getOTSAddress()).withChainAddress(oTSHashAddress.getChainAddress()).withHashAddress(n2 + n3 - 1).withKeyAndMask(0)).build();
        byte[] byArray3 = this.khf.PRF(this.publicSeed, oTSHashAddress.toByteArray());
        oTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)((OTSHashAddress.Builder)new OTSHashAddress.Builder().withLayerAddress(oTSHashAddress.getLayerAddress())).withTreeAddress(oTSHashAddress.getTreeAddress())).withOTSAddress(oTSHashAddress.getOTSAddress()).withChainAddress(oTSHashAddress.getChainAddress()).withHashAddress(oTSHashAddress.getHashAddress()).withKeyAndMask(1)).build();
        byte[] byArray4 = this.khf.PRF(this.publicSeed, oTSHashAddress.toByteArray());
        byte[] byArray5 = new byte[n4];
        for (int i2 = 0; i2 < n4; ++i2) {
            byArray5[i2] = (byte)(byArray2[i2] ^ byArray4[i2]);
        }
        byArray2 = this.khf.F(byArray3, byArray5);
        return byArray2;
    }

    private List<Integer> convertToBaseW(byte[] byArray, int n2, int n3) {
        if (byArray == null) {
            throw new NullPointerException("msg == null");
        }
        if (n2 != 4 && n2 != 16) {
            throw new IllegalArgumentException("w needs to be 4 or 16");
        }
        int n4 = XMSSUtil.log2(n2);
        if (n3 > 8 * byArray.length / n4) {
            throw new IllegalArgumentException("outLength too big");
        }
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            for (int i3 = 8 - n4; i3 >= 0; i3 -= n4) {
                arrayList.add(byArray[i2] >> i3 & n2 - 1);
                if (arrayList.size() != n3) continue;
                return arrayList;
            }
        }
        return arrayList;
    }

    protected byte[] getWOTSPlusSecretKey(byte[] byArray, OTSHashAddress oTSHashAddress) {
        oTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)new OTSHashAddress.Builder().withLayerAddress(oTSHashAddress.getLayerAddress())).withTreeAddress(oTSHashAddress.getTreeAddress())).withOTSAddress(oTSHashAddress.getOTSAddress()).build();
        return this.khf.PRF(byArray, oTSHashAddress.toByteArray());
    }

    private byte[] expandSecretKeySeed(int n2) {
        if (n2 < 0 || n2 >= this.params.getLen()) {
            throw new IllegalArgumentException("index out of bounds");
        }
        return this.khf.PRF(this.secretKeySeed, XMSSUtil.toBytesBigEndian(n2, 32));
    }

    protected WOTSPlusParameters getParams() {
        return this.params;
    }

    protected KeyedHashFunctions getKhf() {
        return this.khf;
    }

    protected byte[] getSecretKeySeed() {
        return Arrays.clone(this.secretKeySeed);
    }

    protected byte[] getPublicSeed() {
        return Arrays.clone(this.publicSeed);
    }

    protected WOTSPlusPrivateKeyParameters getPrivateKey() {
        byte[][] byArrayArray = new byte[this.params.getLen()][];
        for (int i2 = 0; i2 < byArrayArray.length; ++i2) {
            byArrayArray[i2] = this.expandSecretKeySeed(i2);
        }
        return new WOTSPlusPrivateKeyParameters(this.params, byArrayArray);
    }

    WOTSPlusPublicKeyParameters getPublicKey(OTSHashAddress oTSHashAddress) {
        if (oTSHashAddress == null) {
            throw new NullPointerException("otsHashAddress == null");
        }
        byte[][] byArrayArray = new byte[this.params.getLen()][];
        for (int i2 = 0; i2 < this.params.getLen(); ++i2) {
            oTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)((OTSHashAddress.Builder)new OTSHashAddress.Builder().withLayerAddress(oTSHashAddress.getLayerAddress())).withTreeAddress(oTSHashAddress.getTreeAddress())).withOTSAddress(oTSHashAddress.getOTSAddress()).withChainAddress(i2).withHashAddress(oTSHashAddress.getHashAddress()).withKeyAndMask(oTSHashAddress.getKeyAndMask())).build();
            byArrayArray[i2] = this.chain(this.expandSecretKeySeed(i2), 0, this.params.getWinternitzParameter() - 1, oTSHashAddress);
        }
        return new WOTSPlusPublicKeyParameters(this.params, byArrayArray);
    }
}

