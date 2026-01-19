/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jcajce;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.internal.asn1.misc.MiscObjectIdentifiers;
import org.bouncycastle.jcajce.provider.asymmetric.compositesignatures.CompositeIndex;
import org.bouncycastle.jcajce.provider.asymmetric.compositesignatures.KeyFactorySpi;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Exceptions;

public class CompositePrivateKey
implements PrivateKey {
    private final List<PrivateKey> keys;
    private ASN1ObjectIdentifier algorithmIdentifier;

    public CompositePrivateKey(PrivateKey ... privateKeyArray) {
        this(MiscObjectIdentifiers.id_composite_key, privateKeyArray);
    }

    public CompositePrivateKey(ASN1ObjectIdentifier aSN1ObjectIdentifier, PrivateKey ... privateKeyArray) {
        this.algorithmIdentifier = aSN1ObjectIdentifier;
        if (privateKeyArray == null || privateKeyArray.length == 0) {
            throw new IllegalArgumentException("at least one private key must be provided for the composite private key");
        }
        ArrayList<PrivateKey> arrayList = new ArrayList<PrivateKey>(privateKeyArray.length);
        for (int i2 = 0; i2 < privateKeyArray.length; ++i2) {
            arrayList.add(privateKeyArray[i2]);
        }
        this.keys = Collections.unmodifiableList(arrayList);
    }

    public CompositePrivateKey(PrivateKeyInfo privateKeyInfo) {
        CompositePrivateKey compositePrivateKey = null;
        ASN1ObjectIdentifier aSN1ObjectIdentifier = privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm();
        try {
            if (!CompositeIndex.isAlgorithmSupported(aSN1ObjectIdentifier)) {
                throw new IllegalStateException("Unable to create CompositePrivateKey from PrivateKeyInfo");
            }
            KeyFactorySpi keyFactorySpi = new KeyFactorySpi();
            compositePrivateKey = (CompositePrivateKey)keyFactorySpi.generatePrivate(privateKeyInfo);
            if (compositePrivateKey == null) {
                throw new IllegalStateException("Unable to create CompositePrivateKey from PrivateKeyInfo");
            }
        }
        catch (IOException iOException) {
            throw Exceptions.illegalStateException(iOException.getMessage(), iOException);
        }
        this.keys = compositePrivateKey.getPrivateKeys();
        this.algorithmIdentifier = compositePrivateKey.getAlgorithmIdentifier();
    }

    public List<PrivateKey> getPrivateKeys() {
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
        return "PKCS#8";
    }

    @Override
    public byte[] getEncoded() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.algorithmIdentifier.equals(MiscObjectIdentifiers.id_composite_key)) {
            for (int i2 = 0; i2 < this.keys.size(); ++i2) {
                PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(this.keys.get(i2).getEncoded());
                aSN1EncodableVector.add(privateKeyInfo);
            }
            try {
                return new PrivateKeyInfo(new AlgorithmIdentifier(this.algorithmIdentifier), new DERSequence(aSN1EncodableVector)).getEncoded("DER");
            }
            catch (IOException iOException) {
                throw new IllegalStateException("unable to encode composite private key: " + iOException.getMessage());
            }
        }
        byte[] byArray = null;
        for (int i3 = 0; i3 < this.keys.size(); ++i3) {
            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(this.keys.get(i3).getEncoded());
            byArray = Arrays.concatenate(byArray, privateKeyInfo.getPrivateKey().getOctets());
        }
        try {
            return new PrivateKeyInfo(new AlgorithmIdentifier(this.algorithmIdentifier), byArray).getEncoded("DER");
        }
        catch (IOException iOException) {
            throw new IllegalStateException("unable to encode composite private key: " + iOException.getMessage());
        }
    }

    public int hashCode() {
        return this.keys.hashCode();
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof CompositePrivateKey) {
            boolean bl = true;
            CompositePrivateKey compositePrivateKey = (CompositePrivateKey)object;
            if (!compositePrivateKey.getAlgorithmIdentifier().equals(this.algorithmIdentifier) || !this.keys.equals(compositePrivateKey.keys)) {
                bl = false;
            }
            return bl;
        }
        return false;
    }
}

