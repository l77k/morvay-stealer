/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.jcajce.provider.gmss;

import java.security.PublicKey;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.pqc.asn1.GMSSPublicKey;
import org.bouncycastle.pqc.asn1.PQCObjectIdentifiers;
import org.bouncycastle.pqc.asn1.ParSet;
import org.bouncycastle.pqc.jcajce.provider.util.KeyUtil;
import org.bouncycastle.pqc.legacy.crypto.gmss.GMSSParameters;
import org.bouncycastle.pqc.legacy.crypto.gmss.GMSSPublicKeyParameters;
import org.bouncycastle.util.encoders.Hex;

public class BCGMSSPublicKey
implements CipherParameters,
PublicKey {
    private static final long serialVersionUID = 1L;
    private byte[] publicKeyBytes;
    private GMSSParameters gmssParameterSet;
    private GMSSParameters gmssParams;

    public BCGMSSPublicKey(byte[] byArray, GMSSParameters gMSSParameters) {
        this.gmssParameterSet = gMSSParameters;
        this.publicKeyBytes = byArray;
    }

    public BCGMSSPublicKey(GMSSPublicKeyParameters gMSSPublicKeyParameters) {
        this(gMSSPublicKeyParameters.getPublicKey(), gMSSPublicKeyParameters.getParameters());
    }

    @Override
    public String getAlgorithm() {
        return "GMSS";
    }

    public byte[] getPublicKeyBytes() {
        return this.publicKeyBytes;
    }

    public GMSSParameters getParameterSet() {
        return this.gmssParameterSet;
    }

    public String toString() {
        String string = "GMSS public key : " + new String(Hex.encode(this.publicKeyBytes)) + "\nHeight of Trees: \n";
        for (int i2 = 0; i2 < this.gmssParameterSet.getHeightOfTrees().length; ++i2) {
            string = string + "Layer " + i2 + " : " + this.gmssParameterSet.getHeightOfTrees()[i2] + " WinternitzParameter: " + this.gmssParameterSet.getWinternitzParameter()[i2] + " K: " + this.gmssParameterSet.getK()[i2] + "\n";
        }
        return string;
    }

    @Override
    public byte[] getEncoded() {
        return KeyUtil.getEncodedSubjectPublicKeyInfo(new AlgorithmIdentifier(PQCObjectIdentifiers.gmss, new ParSet(this.gmssParameterSet.getNumOfLayers(), this.gmssParameterSet.getHeightOfTrees(), this.gmssParameterSet.getWinternitzParameter(), this.gmssParameterSet.getK()).toASN1Primitive()), new GMSSPublicKey(this.publicKeyBytes));
    }

    @Override
    public String getFormat() {
        return "X.509";
    }
}

