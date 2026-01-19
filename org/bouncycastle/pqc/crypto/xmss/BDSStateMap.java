/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.xmss;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.pqc.crypto.xmss.BDS;
import org.bouncycastle.pqc.crypto.xmss.OTSHashAddress;
import org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters;
import org.bouncycastle.pqc.crypto.xmss.XMSSParameters;
import org.bouncycastle.pqc.crypto.xmss.XMSSUtil;
import org.bouncycastle.util.Integers;

public class BDSStateMap
implements Serializable {
    private static final long serialVersionUID = -3464451825208522308L;
    private final Map<Integer, BDS> bdsState = new TreeMap<Integer, BDS>();
    private transient long maxIndex;

    BDSStateMap(long l2) {
        this.maxIndex = l2;
    }

    BDSStateMap(BDSStateMap bDSStateMap, long l2) {
        for (Integer n2 : bDSStateMap.bdsState.keySet()) {
            this.bdsState.put(n2, new BDS(bDSStateMap.bdsState.get(n2)));
        }
        this.maxIndex = l2;
    }

    BDSStateMap(XMSSMTParameters xMSSMTParameters, long l2, byte[] byArray, byte[] byArray2) {
        this.maxIndex = (1L << xMSSMTParameters.getHeight()) - 1L;
        for (long i2 = 0L; i2 < l2; ++i2) {
            this.updateState(xMSSMTParameters, i2, byArray, byArray2);
        }
    }

    public long getMaxIndex() {
        return this.maxIndex;
    }

    void updateState(XMSSMTParameters xMSSMTParameters, long l2, byte[] byArray, byte[] byArray2) {
        XMSSParameters xMSSParameters = xMSSMTParameters.getXMSSParameters();
        int n2 = xMSSParameters.getHeight();
        long l3 = XMSSUtil.getTreeIndex(l2, n2);
        int n3 = XMSSUtil.getLeafIndex(l2, n2);
        OTSHashAddress oTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)new OTSHashAddress.Builder().withTreeAddress(l3)).withOTSAddress(n3).build();
        if (n3 < (1 << n2) - 1) {
            if (this.get(0) == null || n3 == 0) {
                this.put(0, new BDS(xMSSParameters, byArray, byArray2, oTSHashAddress));
            }
            this.update(0, byArray, byArray2, oTSHashAddress);
        }
        for (int i2 = 1; i2 < xMSSMTParameters.getLayers(); ++i2) {
            n3 = XMSSUtil.getLeafIndex(l3, n2);
            l3 = XMSSUtil.getTreeIndex(l3, n2);
            oTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)new OTSHashAddress.Builder().withLayerAddress(i2)).withTreeAddress(l3)).withOTSAddress(n3).build();
            if (this.bdsState.get(i2) == null || XMSSUtil.isNewBDSInitNeeded(l2, n2, i2)) {
                this.bdsState.put(i2, new BDS(xMSSParameters, byArray, byArray2, oTSHashAddress));
            }
            if (n3 >= (1 << n2) - 1 || !XMSSUtil.isNewAuthenticationPathNeeded(l2, n2, i2)) continue;
            this.update(i2, byArray, byArray2, oTSHashAddress);
        }
    }

    public boolean isEmpty() {
        return this.bdsState.isEmpty();
    }

    BDS get(int n2) {
        return this.bdsState.get(Integers.valueOf(n2));
    }

    BDS update(int n2, byte[] byArray, byte[] byArray2, OTSHashAddress oTSHashAddress) {
        return this.bdsState.put(Integers.valueOf(n2), this.bdsState.get(Integers.valueOf(n2)).getNextState(byArray, byArray2, oTSHashAddress));
    }

    void put(int n2, BDS bDS) {
        this.bdsState.put(Integers.valueOf(n2), bDS);
    }

    public BDSStateMap withWOTSDigest(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        BDSStateMap bDSStateMap = new BDSStateMap(this.maxIndex);
        for (Integer n2 : this.bdsState.keySet()) {
            bDSStateMap.bdsState.put(n2, this.bdsState.get(n2).withWOTSDigest(aSN1ObjectIdentifier));
        }
        return bDSStateMap;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.maxIndex = objectInputStream.available() != 0 ? objectInputStream.readLong() : 0L;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeLong(this.maxIndex);
    }
}

