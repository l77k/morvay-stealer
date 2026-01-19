/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jcajce;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.internal.asn1.misc.MiscObjectIdentifiers;
import org.bouncycastle.jcajce.provider.asymmetric.compositesignatures.CompositeIndex;
import org.bouncycastle.jcajce.provider.asymmetric.compositesignatures.KeyFactorySpi;

public class CompositePublicKey
implements PublicKey {
    private final List<PublicKey> keys;
    private final ASN1ObjectIdentifier algorithmIdentifier;

    public CompositePublicKey(PublicKey ... publicKeyArray) {
        this(MiscObjectIdentifiers.id_composite_key, publicKeyArray);
    }

    public CompositePublicKey(ASN1ObjectIdentifier aSN1ObjectIdentifier, PublicKey ... publicKeyArray) {
        this.algorithmIdentifier = aSN1ObjectIdentifier;
        if (publicKeyArray == null || publicKeyArray.length == 0) {
            throw new IllegalArgumentException("at least one public key must be provided for the composite public key");
        }
        ArrayList<PublicKey> arrayList = new ArrayList<PublicKey>(publicKeyArray.length);
        for (int i2 = 0; i2 < publicKeyArray.length; ++i2) {
            arrayList.add(publicKeyArray[i2]);
        }
        this.keys = Collections.unmodifiableList(arrayList);
    }

    public CompositePublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        ASN1ObjectIdentifier aSN1ObjectIdentifier = subjectPublicKeyInfo.getAlgorithm().getAlgorithm();
        CompositePublicKey compositePublicKey = null;
        try {
            if (!CompositeIndex.isAlgorithmSupported(aSN1ObjectIdentifier)) {
                throw new IllegalStateException("unable to create CompositePublicKey from SubjectPublicKeyInfo");
            }
            KeyFactorySpi keyFactorySpi = new KeyFactorySpi();
            compositePublicKey = (CompositePublicKey)keyFactorySpi.generatePublic(subjectPublicKeyInfo);
            if (compositePublicKey == null) {
                throw new IllegalStateException("unable to create CompositePublicKey from SubjectPublicKeyInfo");
            }
        }
        catch (IOException iOException) {
            throw new IllegalStateException(iOException.getMessage(), iOException);
        }
        this.keys = compositePublicKey.getPublicKeys();
        this.algorithmIdentifier = compositePublicKey.getAlgorithmIdentifier();
    }

    public List<PublicKey> getPublicKeys() {
        return this.keys;
    }

    @Override
    public String getAlgorithm() {
        return CompositeIndex.getAlgorithmName(this.algorithmIdentifier);
    }

    public ASN1ObjectIdentifier getAlgorithmIdentifier() {
        return this.algorithmIdentifier;
    }

    @Override
    public String getFormat() {
        return "X.509";
    }

    @Override
    public byte[] getEncoded() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        for (int i2 = 0; i2 < this.keys.size(); ++i2) {
            if (this.algorithmIdentifier.equals(MiscObjectIdentifiers.id_composite_key)) {
                aSN1EncodableVector.add(SubjectPublicKeyInfo.getInstance(this.keys.get(i2).getEncoded()));
                continue;
            }
            SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(this.keys.get(i2).getEncoded());
            aSN1EncodableVector.add(subjectPublicKeyInfo.getPublicKeyData());
        }
        try {
            return new SubjectPublicKeyInfo(new AlgorithmIdentifier(this.algorithmIdentifier), new DERSequence(aSN1EncodableVector)).getEncoded("DER");
        }
        catch (IOException iOException) {
            throw new IllegalStateException("unable to encode composite public key: " + iOException.getMessage());
        }
    }

    public int hashCode() {
        return this.keys.hashCode();
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof CompositePublicKey) {
            boolean bl = true;
            CompositePublicKey compositePublicKey = (CompositePublicKey)object;
            if (!compositePublicKey.getAlgorithmIdentifier().equals(this.algorithmIdentifier) || !this.keys.equals(compositePublicKey.keys)) {
                bl = false;
            }
            return bl;
        }
        return false;
    }
}

