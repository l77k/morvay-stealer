/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jcajce.provider.symmetric.util;

import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEKeySpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseSecretKeyFactory;
import org.bouncycastle.jcajce.provider.symmetric.util.PBE;

public class PBESecretKeyFactory
extends BaseSecretKeyFactory
implements PBE {
    private boolean forCipher;
    private int scheme;
    private int digest;
    private int keySize;
    private int ivSize;

    public PBESecretKeyFactory(String string, ASN1ObjectIdentifier aSN1ObjectIdentifier, boolean bl, int n2, int n3, int n4, int n5) {
        super(string, aSN1ObjectIdentifier);
        this.forCipher = bl;
        this.scheme = n2;
        this.digest = n3;
        this.keySize = n4;
        this.ivSize = n5;
    }

    @Override
    protected SecretKey engineGenerateSecret(KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof PBEKeySpec) {
            PBEKeySpec pBEKeySpec = (PBEKeySpec)keySpec;
            if (pBEKeySpec.getSalt() == null) {
                return new BCPBEKey(this.algName, this.algOid, this.scheme, this.digest, this.keySize, this.ivSize, pBEKeySpec, null);
            }
            CipherParameters cipherParameters = this.forCipher ? PBE.Util.makePBEParameters(pBEKeySpec, this.scheme, this.digest, this.keySize, this.ivSize) : PBE.Util.makePBEMacParameters(pBEKeySpec, this.scheme, this.digest, this.keySize);
            return new BCPBEKey(this.algName, this.algOid, this.scheme, this.digest, this.keySize, this.ivSize, pBEKeySpec, cipherParameters);
        }
        throw new InvalidKeySpecException("Invalid KeySpec");
    }
}

