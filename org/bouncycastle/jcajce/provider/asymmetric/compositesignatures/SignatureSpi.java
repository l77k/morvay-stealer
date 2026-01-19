/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jcajce.provider.asymmetric.compositesignatures;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bouncycastle.asn1.ASN1BitString;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.internal.asn1.misc.MiscObjectIdentifiers;
import org.bouncycastle.jcajce.CompositePrivateKey;
import org.bouncycastle.jcajce.CompositePublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.compositesignatures.CompositeIndex;
import org.bouncycastle.jcajce.spec.ContextParameterSpec;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.util.Exceptions;

public class SignatureSpi
extends java.security.SignatureSpi {
    private static final Map<String, String> canonicalNames = new HashMap<String, String>();
    private static final String ML_DSA_44 = "ML-DSA-44";
    private static final String ML_DSA_65 = "ML-DSA-65";
    private static final String ML_DSA_87 = "ML-DSA-87";
    private Key compositeKey;
    private final ASN1ObjectIdentifier algorithm;
    private final Signature[] componentSignatures;
    private final byte[] domain;
    private final Digest preHashDigest;
    private final byte[] hashOID;
    private final JcaJceHelper helper = new BCJcaJceHelper();
    private ContextParameterSpec contextSpec;
    private AlgorithmParameters engineParams = null;
    private boolean unprimed = true;

    SignatureSpi(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        this(aSN1ObjectIdentifier, null, null);
    }

    SignatureSpi(ASN1ObjectIdentifier aSN1ObjectIdentifier, Digest digest, ASN1ObjectIdentifier aSN1ObjectIdentifier2) {
        this.algorithm = aSN1ObjectIdentifier;
        this.preHashDigest = digest;
        String[] stringArray = CompositeIndex.getPairing(aSN1ObjectIdentifier);
        if (digest != null) {
            try {
                this.hashOID = aSN1ObjectIdentifier2.getEncoded();
            }
            catch (IOException iOException) {
                throw new IllegalStateException("unable to encode domain value");
            }
        } else {
            this.hashOID = null;
        }
        try {
            this.domain = aSN1ObjectIdentifier.getEncoded();
        }
        catch (IOException iOException) {
            throw new IllegalStateException("unable to encode domain value");
        }
        this.componentSignatures = new Signature[stringArray.length];
        try {
            for (int i2 = 0; i2 != this.componentSignatures.length; ++i2) {
                this.componentSignatures[i2] = Signature.getInstance(stringArray[i2], "BC");
            }
        }
        catch (GeneralSecurityException generalSecurityException) {
            throw Exceptions.illegalStateException(generalSecurityException.getMessage(), generalSecurityException);
        }
    }

    @Override
    protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        if (!(publicKey instanceof CompositePublicKey)) {
            throw new InvalidKeyException("Public key is not composite.");
        }
        this.compositeKey = publicKey;
        CompositePublicKey compositePublicKey = (CompositePublicKey)this.compositeKey;
        if (!compositePublicKey.getAlgorithmIdentifier().equals(this.algorithm)) {
            throw new InvalidKeyException("Provided composite public key cannot be used with the composite signature algorithm.");
        }
        this.sigInitVerify();
    }

    private void sigInitVerify() throws InvalidKeyException {
        CompositePublicKey compositePublicKey = (CompositePublicKey)this.compositeKey;
        for (int i2 = 0; i2 < this.componentSignatures.length; ++i2) {
            this.componentSignatures[i2].initVerify(compositePublicKey.getPublicKeys().get(i2));
        }
        this.unprimed = true;
    }

    @Override
    protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
        if (!(privateKey instanceof CompositePrivateKey)) {
            throw new InvalidKeyException("Private key is not composite.");
        }
        this.compositeKey = privateKey;
        CompositePrivateKey compositePrivateKey = (CompositePrivateKey)privateKey;
        if (!compositePrivateKey.getAlgorithmIdentifier().equals(this.algorithm)) {
            throw new InvalidKeyException("Provided composite private key cannot be used with the composite signature algorithm.");
        }
        this.sigInitSign();
    }

    private void sigInitSign() throws InvalidKeyException {
        CompositePrivateKey compositePrivateKey = (CompositePrivateKey)this.compositeKey;
        for (int i2 = 0; i2 < this.componentSignatures.length; ++i2) {
            this.componentSignatures[i2].initSign(compositePrivateKey.getPrivateKeys().get(i2));
        }
        this.unprimed = true;
    }

    private void baseSigInit() throws SignatureException {
        try {
            this.componentSignatures[0].setParameter(new ContextParameterSpec(this.domain));
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new IllegalStateException("unable to set context on ML-DSA");
        }
        if (this.preHashDigest == null) {
            for (int i2 = 0; i2 < this.componentSignatures.length; ++i2) {
                Signature signature = this.componentSignatures[i2];
                signature.update(this.domain);
                if (this.contextSpec == null) {
                    signature.update((byte)0);
                    continue;
                }
                byte[] byArray = this.contextSpec.getContext();
                signature.update((byte)byArray.length);
                signature.update(byArray);
            }
        }
        this.unprimed = false;
    }

    @Override
    protected void engineUpdate(byte by) throws SignatureException {
        if (this.unprimed) {
            this.baseSigInit();
        }
        if (this.preHashDigest != null) {
            this.preHashDigest.update(by);
        } else {
            for (int i2 = 0; i2 < this.componentSignatures.length; ++i2) {
                Signature signature = this.componentSignatures[i2];
                signature.update(by);
            }
        }
    }

    @Override
    protected void engineUpdate(byte[] byArray, int n2, int n3) throws SignatureException {
        if (this.unprimed) {
            this.baseSigInit();
        }
        if (this.preHashDigest != null) {
            this.preHashDigest.update(byArray, n2, n3);
        } else {
            for (int i2 = 0; i2 < this.componentSignatures.length; ++i2) {
                Signature signature = this.componentSignatures[i2];
                signature.update(byArray, n2, n3);
            }
        }
    }

    @Override
    protected byte[] engineSign() throws SignatureException {
        if (this.preHashDigest != null) {
            this.processPreHashedMessage();
        }
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        try {
            for (int i2 = 0; i2 < this.componentSignatures.length; ++i2) {
                byte[] byArray = this.componentSignatures[i2].sign();
                aSN1EncodableVector.add(new DERBitString(byArray));
            }
            return new DERSequence(aSN1EncodableVector).getEncoded("DER");
        }
        catch (IOException iOException) {
            throw new SignatureException(iOException.getMessage());
        }
    }

    private void processPreHashedMessage() throws SignatureException {
        byte[] byArray = new byte[this.preHashDigest.getDigestSize()];
        this.preHashDigest.doFinal(byArray, 0);
        for (int i2 = 0; i2 < this.componentSignatures.length; ++i2) {
            Signature signature = this.componentSignatures[i2];
            signature.update(this.domain, 0, this.domain.length);
            if (this.contextSpec == null) {
                signature.update((byte)0);
            } else {
                byte[] byArray2 = this.contextSpec.getContext();
                signature.update((byte)byArray2.length);
                signature.update(byArray2);
            }
            signature.update(this.hashOID, 0, this.hashOID.length);
            signature.update(byArray, 0, byArray.length);
        }
    }

    @Override
    protected boolean engineVerify(byte[] byArray) throws SignatureException {
        ASN1Sequence aSN1Sequence = DERSequence.getInstance(byArray);
        if (aSN1Sequence.size() != this.componentSignatures.length) {
            return false;
        }
        if (this.preHashDigest != null && this.preHashDigest != null) {
            this.processPreHashedMessage();
        }
        boolean bl = false;
        for (int i2 = 0; i2 < this.componentSignatures.length; ++i2) {
            if (this.componentSignatures[i2].verify(ASN1BitString.getInstance(aSN1Sequence.getObjectAt(i2)).getOctets())) continue;
            bl = true;
        }
        return !bl;
    }

    @Override
    protected void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        if (!this.unprimed) {
            throw new InvalidAlgorithmParameterException("attempt to set parameter after update");
        }
        if (algorithmParameterSpec instanceof ContextParameterSpec) {
            this.contextSpec = (ContextParameterSpec)algorithmParameterSpec;
            try {
                if (this.compositeKey instanceof PublicKey) {
                    this.sigInitVerify();
                }
                this.sigInitSign();
            }
            catch (InvalidKeyException invalidKeyException) {
                throw new InvalidAlgorithmParameterException("keys invalid on reset: " + invalidKeyException.getMessage(), invalidKeyException);
            }
        } else {
            throw new InvalidAlgorithmParameterException("unknown parameterSpec passed to composite signature");
        }
    }

    private void setSigParameter(Signature signature, String string, List<String> list, List<AlgorithmParameterSpec> list2) throws InvalidAlgorithmParameterException {
        for (int i2 = 0; i2 != list.size(); ++i2) {
            String string2 = this.getCanonicalName(list.get(i2));
            if (!list.get(i2).equals(string)) continue;
            signature.setParameter(list2.get(i2));
        }
    }

    private String getCanonicalName(String string) {
        String string2 = canonicalNames.get(string);
        if (string2 != null) {
            return string2;
        }
        return string;
    }

    @Override
    protected void engineSetParameter(String string, Object object) throws InvalidParameterException {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    @Override
    protected Object engineGetParameter(String string) throws InvalidParameterException {
        throw new UnsupportedOperationException("engineGetParameter unsupported");
    }

    @Override
    protected final AlgorithmParameters engineGetParameters() {
        if (this.engineParams == null && this.contextSpec != null) {
            try {
                this.engineParams = this.helper.createAlgorithmParameters("CONTEXT");
                this.engineParams.init(this.contextSpec);
            }
            catch (Exception exception) {
                throw Exceptions.illegalStateException(exception.toString(), exception);
            }
        }
        return this.engineParams;
    }

    static {
        canonicalNames.put("MLDSA44", ML_DSA_44);
        canonicalNames.put("MLDSA65", ML_DSA_65);
        canonicalNames.put("MLDSA87", ML_DSA_87);
        canonicalNames.put(NISTObjectIdentifiers.id_ml_dsa_44.getId(), ML_DSA_44);
        canonicalNames.put(NISTObjectIdentifiers.id_ml_dsa_65.getId(), ML_DSA_65);
        canonicalNames.put(NISTObjectIdentifiers.id_ml_dsa_87.getId(), ML_DSA_87);
    }

    public static final class HashMLDSA44_ECDSA_P256_SHA256
    extends SignatureSpi {
        public HashMLDSA44_ECDSA_P256_SHA256() {
            super(MiscObjectIdentifiers.id_HashMLDSA44_ECDSA_P256_SHA256, new SHA256Digest(), NISTObjectIdentifiers.id_sha256);
        }
    }

    public static final class HashMLDSA44_Ed25519_SHA512
    extends SignatureSpi {
        public HashMLDSA44_Ed25519_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA44_Ed25519_SHA512, new SHA512Digest(), NISTObjectIdentifiers.id_sha512);
        }
    }

    public static final class HashMLDSA44_RSA2048_PKCS15_SHA256
    extends SignatureSpi {
        public HashMLDSA44_RSA2048_PKCS15_SHA256() {
            super(MiscObjectIdentifiers.id_HashMLDSA44_RSA2048_PKCS15_SHA256, new SHA256Digest(), NISTObjectIdentifiers.id_sha256);
        }
    }

    public static final class HashMLDSA44_RSA2048_PSS_SHA256
    extends SignatureSpi {
        public HashMLDSA44_RSA2048_PSS_SHA256() {
            super(MiscObjectIdentifiers.id_HashMLDSA44_RSA2048_PSS_SHA256, new SHA256Digest(), NISTObjectIdentifiers.id_sha256);
        }
    }

    public static final class HashMLDSA65_ECDSA_P384_SHA512
    extends SignatureSpi {
        public HashMLDSA65_ECDSA_P384_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA65_ECDSA_P384_SHA512, new SHA512Digest(), NISTObjectIdentifiers.id_sha512);
        }
    }

    public static final class HashMLDSA65_ECDSA_brainpoolP256r1_SHA512
    extends SignatureSpi {
        public HashMLDSA65_ECDSA_brainpoolP256r1_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA65_ECDSA_brainpoolP256r1_SHA512, new SHA512Digest(), NISTObjectIdentifiers.id_sha512);
        }
    }

    public static final class HashMLDSA65_Ed25519_SHA512
    extends SignatureSpi {
        public HashMLDSA65_Ed25519_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA65_Ed25519_SHA512, new SHA512Digest(), NISTObjectIdentifiers.id_sha512);
        }
    }

    public static final class HashMLDSA65_RSA3072_PKCS15_SHA512
    extends SignatureSpi {
        public HashMLDSA65_RSA3072_PKCS15_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA65_RSA3072_PKCS15_SHA512, new SHA512Digest(), NISTObjectIdentifiers.id_sha512);
        }
    }

    public static final class HashMLDSA65_RSA3072_PSS_SHA512
    extends SignatureSpi {
        public HashMLDSA65_RSA3072_PSS_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA65_RSA3072_PSS_SHA512, new SHA512Digest(), NISTObjectIdentifiers.id_sha512);
        }
    }

    public static final class HashMLDSA65_RSA4096_PKCS15_SHA512
    extends SignatureSpi {
        public HashMLDSA65_RSA4096_PKCS15_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA65_RSA4096_PKCS15_SHA512, new SHA512Digest(), NISTObjectIdentifiers.id_sha512);
        }
    }

    public static final class HashMLDSA65_RSA4096_PSS_SHA512
    extends SignatureSpi {
        public HashMLDSA65_RSA4096_PSS_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA65_RSA4096_PSS_SHA512, new SHA512Digest(), NISTObjectIdentifiers.id_sha512);
        }
    }

    public static final class HashMLDSA87_ECDSA_P384_SHA512
    extends SignatureSpi {
        public HashMLDSA87_ECDSA_P384_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA87_ECDSA_P384_SHA512, new SHA512Digest(), NISTObjectIdentifiers.id_sha512);
        }
    }

    public static final class HashMLDSA87_ECDSA_brainpoolP384r1_SHA512
    extends SignatureSpi {
        public HashMLDSA87_ECDSA_brainpoolP384r1_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA87_ECDSA_brainpoolP384r1_SHA512, new SHA512Digest(), NISTObjectIdentifiers.id_sha512);
        }
    }

    public static final class HashMLDSA87_Ed448_SHA512
    extends SignatureSpi {
        public HashMLDSA87_Ed448_SHA512() {
            super(MiscObjectIdentifiers.id_HashMLDSA87_Ed448_SHA512, new SHA512Digest(), NISTObjectIdentifiers.id_sha512);
        }
    }

    public static final class MLDSA44_ECDSA_P256_SHA256
    extends SignatureSpi {
        public MLDSA44_ECDSA_P256_SHA256() {
            super(MiscObjectIdentifiers.id_MLDSA44_ECDSA_P256_SHA256);
        }
    }

    public static final class MLDSA44_Ed25519_SHA512
    extends SignatureSpi {
        public MLDSA44_Ed25519_SHA512() {
            super(MiscObjectIdentifiers.id_MLDSA44_Ed25519_SHA512);
        }
    }

    public static final class MLDSA44_RSA2048_PKCS15_SHA256
    extends SignatureSpi {
        public MLDSA44_RSA2048_PKCS15_SHA256() {
            super(MiscObjectIdentifiers.id_MLDSA44_RSA2048_PKCS15_SHA256);
        }
    }

    public static final class MLDSA44_RSA2048_PSS_SHA256
    extends SignatureSpi {
        public MLDSA44_RSA2048_PSS_SHA256() {
            super(MiscObjectIdentifiers.id_MLDSA44_RSA2048_PSS_SHA256);
        }
    }

    public static final class MLDSA65_ECDSA_P384_SHA384
    extends SignatureSpi {
        public MLDSA65_ECDSA_P384_SHA384() {
            super(MiscObjectIdentifiers.id_MLDSA65_ECDSA_P384_SHA384);
        }
    }

    public static final class MLDSA65_ECDSA_brainpoolP256r1_SHA256
    extends SignatureSpi {
        public MLDSA65_ECDSA_brainpoolP256r1_SHA256() {
            super(MiscObjectIdentifiers.id_MLDSA65_ECDSA_brainpoolP256r1_SHA256);
        }
    }

    public static final class MLDSA65_Ed25519_SHA512
    extends SignatureSpi {
        public MLDSA65_Ed25519_SHA512() {
            super(MiscObjectIdentifiers.id_MLDSA65_Ed25519_SHA512);
        }
    }

    public static final class MLDSA65_RSA3072_PKCS15_SHA256
    extends SignatureSpi {
        public MLDSA65_RSA3072_PKCS15_SHA256() {
            super(MiscObjectIdentifiers.id_MLDSA65_RSA3072_PKCS15_SHA256);
        }
    }

    public static final class MLDSA65_RSA3072_PSS_SHA256
    extends SignatureSpi {
        public MLDSA65_RSA3072_PSS_SHA256() {
            super(MiscObjectIdentifiers.id_MLDSA65_RSA3072_PSS_SHA256);
        }
    }

    public static final class MLDSA65_RSA4096_PKCS15_SHA384
    extends SignatureSpi {
        public MLDSA65_RSA4096_PKCS15_SHA384() {
            super(MiscObjectIdentifiers.id_MLDSA65_RSA4096_PKCS15_SHA384);
        }
    }

    public static final class MLDSA65_RSA4096_PSS_SHA384
    extends SignatureSpi {
        public MLDSA65_RSA4096_PSS_SHA384() {
            super(MiscObjectIdentifiers.id_MLDSA65_RSA4096_PSS_SHA384);
        }
    }

    public static final class MLDSA87_ECDSA_P384_SHA384
    extends SignatureSpi {
        public MLDSA87_ECDSA_P384_SHA384() {
            super(MiscObjectIdentifiers.id_MLDSA87_ECDSA_P384_SHA384);
        }
    }

    public static final class MLDSA87_ECDSA_brainpoolP384r1_SHA384
    extends SignatureSpi {
        public MLDSA87_ECDSA_brainpoolP384r1_SHA384() {
            super(MiscObjectIdentifiers.id_MLDSA87_ECDSA_brainpoolP384r1_SHA384);
        }
    }

    public static final class MLDSA87_Ed448_SHA512
    extends SignatureSpi {
        public MLDSA87_Ed448_SHA512() {
            super(MiscObjectIdentifiers.id_MLDSA87_Ed448_SHA512);
        }
    }
}

