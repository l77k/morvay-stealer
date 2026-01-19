/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.agreement.kdf;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.crypto.DerivationParameters;

public class DHKDFParameters
implements DerivationParameters {
    private ASN1ObjectIdentifier algorithm;
    private int keySize;
    private byte[] z;
    private byte[] extraInfo;

    public DHKDFParameters(ASN1ObjectIdentifier aSN1ObjectIdentifier, int n2, byte[] byArray) {
        this(aSN1ObjectIdentifier, n2, byArray, null);
    }

    public DHKDFParameters(ASN1ObjectIdentifier aSN1ObjectIdentifier, int n2, byte[] byArray, byte[] byArray2) {
        this.algorithm = aSN1ObjectIdentifier;
        this.keySize = n2;
        this.z = byArray;
        this.extraInfo = byArray2;
    }

    public ASN1ObjectIdentifier getAlgorithm() {
        return this.algorithm;
    }

    public int getKeySize() {
        return this.keySize;
    }

    public byte[] getZ() {
        return this.z;
    }

    public byte[] getExtraInfo() {
        return this.extraInfo;
    }
}

