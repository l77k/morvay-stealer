/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jcajce.provider.asymmetric.compositesignatures;

import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.internal.asn1.misc.MiscObjectIdentifiers;
import org.bouncycastle.jcajce.CompositePrivateKey;
import org.bouncycastle.jcajce.CompositePublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.compositesignatures.CompositeIndex;

public class KeyPairGeneratorSpi
extends java.security.KeyPairGeneratorSpi {
    private final ASN1ObjectIdentifier algorithm;
    private final KeyPairGenerator[] generators;
    private SecureRandom secureRandom;
    private boolean parametersInitialized = false;

    KeyPairGeneratorSpi(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        this.algorithm = aSN1ObjectIdentifier;
        String[] stringArray = CompositeIndex.getPairing(aSN1ObjectIdentifier);
        AlgorithmParameterSpec[] algorithmParameterSpecArray = CompositeIndex.getKeyPairSpecs(aSN1ObjectIdentifier);
        this.generators = new KeyPairGenerator[stringArray.length];
        for (int i2 = 0; i2 != stringArray.length; ++i2) {
            try {
                this.generators[i2] = KeyPairGenerator.getInstance(CompositeIndex.getBaseName(stringArray[i2]), "BC");
                AlgorithmParameterSpec algorithmParameterSpec = algorithmParameterSpecArray[i2];
                if (algorithmParameterSpec == null) continue;
                this.generators[i2].initialize(algorithmParameterSpec);
                continue;
            }
            catch (Exception exception) {
                throw new IllegalStateException("unable to create base generator: " + exception.getMessage());
            }
        }
    }

    @Override
    public void initialize(int n2, SecureRandom secureRandom) {
        throw new IllegalArgumentException("use AlgorithmParameterSpec");
    }

    @Override
    public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if (algorithmParameterSpec != null) {
            throw new IllegalArgumentException("Use initialize only for custom SecureRandom. AlgorithmParameterSpec must be null because it is determined by algorithm name.");
        }
        AlgorithmParameterSpec[] algorithmParameterSpecArray = CompositeIndex.getKeyPairSpecs(this.algorithm);
        for (int i2 = 0; i2 != algorithmParameterSpecArray.length; ++i2) {
            AlgorithmParameterSpec algorithmParameterSpec2 = algorithmParameterSpecArray[i2];
            if (algorithmParameterSpec2 == null) continue;
            this.generators[i2].initialize(algorithmParameterSpec2, secureRandom);
        }
    }

    @Override
    public KeyPair generateKeyPair() {
        return this.getCompositeKeyPair();
    }

    private KeyPair getCompositeKeyPair() {
        Serializable serializable;
        PublicKey[] publicKeyArray = new PublicKey[this.generators.length];
        PrivateKey[] privateKeyArray = new PrivateKey[this.generators.length];
        for (int i2 = 0; i2 < this.generators.length; ++i2) {
            serializable = this.generators[i2].generateKeyPair();
            publicKeyArray[i2] = serializable.getPublic();
            privateKeyArray[i2] = serializable.getPrivate();
        }
        CompositePublicKey compositePublicKey = new CompositePublicKey(this.algorithm, publicKeyArray);
        serializable = new CompositePrivateKey(this.algorithm, privateKeyArray);
        return new KeyPair(compositePublicKey, (PrivateKey)serializable);
    }

    public static final class HashMLDSA44_ECDSA_P256_SHA256
    extends KeyPairGeneratorSpi {
        public HashMLDSA44_ECDSA_P256_SHA256() {
            super(MiscObjectIdentifiers.id_HashMLDSA44_ECDSA_P256_SHA256);
        }
    }

    public static final class HashMLDSA44_Ed25519_SHA512
    extends KeyPairGeneratorSpi {
        public HashMLDSA44_Ed25519_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA44_Ed25519_SHA512);
        }
    }

    public static final class HashMLDSA44_RSA2048_PKCS15_SHA256
    extends KeyPairGeneratorSpi {
        public HashMLDSA44_RSA2048_PKCS15_SHA256() {
            super(MiscObjectIdentifiers.id_HashMLDSA44_RSA2048_PKCS15_SHA256);
        }
    }

    public static final class HashMLDSA44_RSA2048_PSS_SHA256
    extends KeyPairGeneratorSpi {
        public HashMLDSA44_RSA2048_PSS_SHA256() {
            super(MiscObjectIdentifiers.id_HashMLDSA44_RSA2048_PSS_SHA256);
        }
    }

    public static final class HashMLDSA65_ECDSA_P384_SHA512
    extends KeyPairGeneratorSpi {
        public HashMLDSA65_ECDSA_P384_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA65_ECDSA_P384_SHA512);
        }
    }

    public static final class HashMLDSA65_ECDSA_brainpoolP256r1_SHA512
    extends KeyPairGeneratorSpi {
        public HashMLDSA65_ECDSA_brainpoolP256r1_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA65_ECDSA_brainpoolP256r1_SHA512);
        }
    }

    public static final class HashMLDSA65_Ed25519_SHA512
    extends KeyPairGeneratorSpi {
        public HashMLDSA65_Ed25519_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA65_Ed25519_SHA512);
        }
    }

    public static final class HashMLDSA65_RSA3072_PKCS15_SHA512
    extends KeyPairGeneratorSpi {
        public HashMLDSA65_RSA3072_PKCS15_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA65_RSA3072_PKCS15_SHA512);
        }
    }

    public static final class HashMLDSA65_RSA3072_PSS_SHA512
    extends KeyPairGeneratorSpi {
        public HashMLDSA65_RSA3072_PSS_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA65_RSA3072_PSS_SHA512);
        }
    }

    public static final class HashMLDSA65_RSA4096_PKCS15_SHA512
    extends KeyPairGeneratorSpi {
        public HashMLDSA65_RSA4096_PKCS15_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA65_RSA4096_PKCS15_SHA512);
        }
    }

    public static final class HashMLDSA65_RSA4096_PSS_SHA512
    extends KeyPairGeneratorSpi {
        public HashMLDSA65_RSA4096_PSS_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA65_RSA4096_PSS_SHA512);
        }
    }

    public static final class HashMLDSA87_ECDSA_P384_SHA512
    extends KeyPairGeneratorSpi {
        public HashMLDSA87_ECDSA_P384_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA87_ECDSA_P384_SHA512);
        }
    }

    public static final class HashMLDSA87_ECDSA_brainpoolP384r1_SHA512
    extends KeyPairGeneratorSpi {
        public HashMLDSA87_ECDSA_brainpoolP384r1_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA87_ECDSA_brainpoolP384r1_SHA512);
        }
    }

    public static final class HashMLDSA87_Ed448_SHA512
    extends KeyPairGeneratorSpi {
        public HashMLDSA87_Ed448_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA87_Ed448_SHA512);
        }
    }

    public static final class MLDSA44_ECDSA_P256_SHA256
    extends KeyPairGeneratorSpi {
        public MLDSA44_ECDSA_P256_SHA256() {
            super(MiscObjectIdentifiers.id_MLDSA44_ECDSA_P256_SHA256);
        }
    }

    public static final class MLDSA44_Ed25519_SHA512
    extends KeyPairGeneratorSpi {
        public MLDSA44_Ed25519_SHA512() {
            super(MiscObjectIdentifiers.id_MLDSA44_Ed25519_SHA512);
        }
    }

    public static final class MLDSA44_RSA2048_PKCS15_SHA256
    extends KeyPairGeneratorSpi {
        public MLDSA44_RSA2048_PKCS15_SHA256() {
            super(MiscObjectIdentifiers.id_MLDSA44_RSA2048_PKCS15_SHA256);
        }
    }

    public static final class MLDSA44_RSA2048_PSS_SHA256
    extends KeyPairGeneratorSpi {
        public MLDSA44_RSA2048_PSS_SHA256() {
            super(MiscObjectIdentifiers.id_MLDSA44_RSA2048_PSS_SHA256);
        }
    }

    public static final class MLDSA65_ECDSA_P384_SHA384
    extends KeyPairGeneratorSpi {
        public MLDSA65_ECDSA_P384_SHA384() {
            super(MiscObjectIdentifiers.id_MLDSA65_ECDSA_P384_SHA384);
        }
    }

    public static final class MLDSA65_ECDSA_brainpoolP256r1_SHA256
    extends KeyPairGeneratorSpi {
        public MLDSA65_ECDSA_brainpoolP256r1_SHA256() {
            super(MiscObjectIdentifiers.id_MLDSA65_ECDSA_brainpoolP256r1_SHA256);
        }
    }

    public static final class MLDSA65_Ed25519_SHA512
    extends KeyPairGeneratorSpi {
        public MLDSA65_Ed25519_SHA512() {
            super(MiscObjectIdentifiers.id_MLDSA65_Ed25519_SHA512);
        }
    }

    public static final class MLDSA65_RSA3072_PKCS15_SHA256
    extends KeyPairGeneratorSpi {
        public MLDSA65_RSA3072_PKCS15_SHA256() {
            super(MiscObjectIdentifiers.id_MLDSA65_RSA3072_PKCS15_SHA256);
        }
    }

    public static final class MLDSA65_RSA3072_PSS_SHA256
    extends KeyPairGeneratorSpi {
        public MLDSA65_RSA3072_PSS_SHA256() {
            super(MiscObjectIdentifiers.id_MLDSA65_RSA3072_PSS_SHA256);
        }
    }

    public static final class MLDSA65_RSA4096_PKCS15_SHA384
    extends KeyPairGeneratorSpi {
        public MLDSA65_RSA4096_PKCS15_SHA384() {
            super(MiscObjectIdentifiers.id_MLDSA65_RSA4096_PKCS15_SHA384);
        }
    }

    public static final class MLDSA65_RSA4096_PSS_SHA384
    extends KeyPairGeneratorSpi {
        public MLDSA65_RSA4096_PSS_SHA384() {
            super(MiscObjectIdentifiers.id_MLDSA65_RSA4096_PSS_SHA384);
        }
    }

    public static final class MLDSA87_ECDSA_P384_SHA384
    extends KeyPairGeneratorSpi {
        public MLDSA87_ECDSA_P384_SHA384() {
            super(MiscObjectIdentifiers.id_MLDSA87_ECDSA_P384_SHA384);
        }
    }

    public static final class MLDSA87_ECDSA_brainpoolP384r1_SHA384
    extends KeyPairGeneratorSpi {
        public MLDSA87_ECDSA_brainpoolP384r1_SHA384() {
            super(MiscObjectIdentifiers.id_MLDSA87_ECDSA_brainpoolP384r1_SHA384);
        }
    }

    public static final class MLDSA87_Ed448_SHA512
    extends KeyPairGeneratorSpi {
        public MLDSA87_Ed448_SHA512() {
            super(MiscObjectIdentifiers.id_MLDSA87_Ed448_SHA512);
        }
    }
}

