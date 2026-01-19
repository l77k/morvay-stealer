/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jcajce.provider.asymmetric.compositesignatures;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bouncycastle.asn1.ASN1BitString;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.bc.BCObjectIdentifiers;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.sec.SECObjectIdentifiers;
import org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X962Parameters;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.internal.asn1.edec.EdECObjectIdentifiers;
import org.bouncycastle.internal.asn1.misc.MiscObjectIdentifiers;
import org.bouncycastle.jcajce.CompositePrivateKey;
import org.bouncycastle.jcajce.CompositePublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.compositesignatures.CompositeIndex;
import org.bouncycastle.jcajce.provider.asymmetric.util.BaseKeyFactorySpi;
import org.bouncycastle.jcajce.provider.util.AsymmetricKeyInfoConverter;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Exceptions;

public class KeyFactorySpi
extends BaseKeyFactorySpi
implements AsymmetricKeyInfoConverter {
    private static final AlgorithmIdentifier mlDsa44 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_ml_dsa_44);
    private static final AlgorithmIdentifier mlDsa65 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_ml_dsa_65);
    private static final AlgorithmIdentifier mlDsa87 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_ml_dsa_87);
    private static final AlgorithmIdentifier falcon512Identifier = new AlgorithmIdentifier(BCObjectIdentifiers.falcon_512);
    private static final AlgorithmIdentifier ed25519 = new AlgorithmIdentifier(EdECObjectIdentifiers.id_Ed25519);
    private static final AlgorithmIdentifier ecDsaP256 = new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, new X962Parameters(SECObjectIdentifiers.secp256r1));
    private static final AlgorithmIdentifier ecDsaBrainpoolP256r1 = new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, new X962Parameters(TeleTrusTObjectIdentifiers.brainpoolP256r1));
    private static final AlgorithmIdentifier rsa = new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption);
    private static final AlgorithmIdentifier ed448 = new AlgorithmIdentifier(EdECObjectIdentifiers.id_Ed448);
    private static final AlgorithmIdentifier ecDsaP384 = new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, new X962Parameters(SECObjectIdentifiers.secp384r1));
    private static final AlgorithmIdentifier ecDsaBrainpoolP384r1 = new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, new X962Parameters(TeleTrusTObjectIdentifiers.brainpoolP384r1));
    private static Map<ASN1ObjectIdentifier, AlgorithmIdentifier[]> pairings = new HashMap<ASN1ObjectIdentifier, AlgorithmIdentifier[]>();
    private static Map<ASN1ObjectIdentifier, int[]> componentKeySizes = new HashMap<ASN1ObjectIdentifier, int[]>();
    private JcaJceHelper helper;

    public KeyFactorySpi() {
        this(null);
    }

    public KeyFactorySpi(JcaJceHelper jcaJceHelper) {
        this.helper = jcaJceHelper;
    }

    @Override
    protected Key engineTranslateKey(Key key) throws InvalidKeyException {
        if (this.helper == null) {
            this.helper = new BCJcaJceHelper();
        }
        try {
            if (key instanceof PrivateKey) {
                return this.generatePrivate(PrivateKeyInfo.getInstance(key.getEncoded()));
            }
            if (key instanceof PublicKey) {
                return this.generatePublic(SubjectPublicKeyInfo.getInstance(key.getEncoded()));
            }
        }
        catch (IOException iOException) {
            throw new InvalidKeyException("Key could not be parsed: " + iOException.getMessage());
        }
        throw new InvalidKeyException("Key not recognized");
    }

    @Override
    public PrivateKey generatePrivate(PrivateKeyInfo privateKeyInfo) throws IOException {
        ASN1ObjectIdentifier aSN1ObjectIdentifier;
        if (this.helper == null) {
            this.helper = new BCJcaJceHelper();
        }
        if (MiscObjectIdentifiers.id_alg_composite.equals(aSN1ObjectIdentifier = privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm()) || MiscObjectIdentifiers.id_composite_key.equals(aSN1ObjectIdentifier)) {
            ASN1Sequence aSN1Sequence = DERSequence.getInstance(privateKeyInfo.parsePrivateKey());
            PrivateKey[] privateKeyArray = new PrivateKey[aSN1Sequence.size()];
            for (int i2 = 0; i2 != aSN1Sequence.size(); ++i2) {
                ASN1Sequence aSN1Sequence2 = ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(i2));
                PrivateKeyInfo privateKeyInfo2 = PrivateKeyInfo.getInstance(aSN1Sequence2);
                try {
                    privateKeyArray[i2] = this.helper.createKeyFactory(privateKeyInfo2.getPrivateKeyAlgorithm().getAlgorithm().getId()).generatePrivate(new PKCS8EncodedKeySpec(privateKeyInfo2.getEncoded()));
                    continue;
                }
                catch (Exception exception) {
                    throw new IOException("cannot decode generic composite: " + exception.getMessage(), exception);
                }
            }
            return new CompositePrivateKey(privateKeyArray);
        }
        try {
            Object[] objectArray;
            PrivateKey[] privateKeyArray;
            ASN1Sequence aSN1Sequence;
            Object object;
            try {
                object = privateKeyInfo.parsePrivateKey();
                aSN1Sequence = object instanceof ASN1OctetString ? DERSequence.getInstance(ASN1OctetString.getInstance(object).getOctets()) : DERSequence.getInstance(object);
            }
            catch (Exception exception) {
                privateKeyArray = new ASN1EncodableVector();
                objectArray = privateKeyInfo.getPrivateKey().getOctets();
                privateKeyArray.add(new DEROctetString(Arrays.copyOfRange(objectArray, 0, 32)));
                privateKeyArray.add(new DEROctetString(Arrays.copyOfRange(objectArray, 32, objectArray.length)));
                aSN1Sequence = new DERSequence((ASN1EncodableVector)privateKeyArray);
            }
            object = this.getKeyFactoriesFromIdentifier(aSN1ObjectIdentifier);
            privateKeyArray = new PrivateKey[aSN1Sequence.size()];
            objectArray = pairings.get(aSN1ObjectIdentifier);
            for (int i3 = 0; i3 < aSN1Sequence.size(); ++i3) {
                PKCS8EncodedKeySpec pKCS8EncodedKeySpec;
                Object object2;
                if (aSN1Sequence.getObjectAt(i3) instanceof ASN1OctetString) {
                    object2 = new ASN1EncodableVector(3);
                    ((ASN1EncodableVector)object2).add(privateKeyInfo.getVersion());
                    ((ASN1EncodableVector)object2).add((ASN1Encodable)objectArray[i3]);
                    ((ASN1EncodableVector)object2).add(aSN1Sequence.getObjectAt(i3));
                    pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(PrivateKeyInfo.getInstance(new DERSequence((ASN1EncodableVector)object2)).getEncoded());
                    privateKeyArray[i3] = ((KeyFactory)object.get(i3)).generatePrivate(pKCS8EncodedKeySpec);
                    continue;
                }
                object2 = ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(i3));
                pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(PrivateKeyInfo.getInstance(object2).getEncoded());
                privateKeyArray[i3] = ((KeyFactory)object.get(i3)).generatePrivate(pKCS8EncodedKeySpec);
            }
            return new CompositePrivateKey(aSN1ObjectIdentifier, privateKeyArray);
        }
        catch (GeneralSecurityException generalSecurityException) {
            throw Exceptions.ioException(generalSecurityException.getMessage(), generalSecurityException);
        }
    }

    @Override
    public PublicKey generatePublic(SubjectPublicKeyInfo subjectPublicKeyInfo) throws IOException {
        if (this.helper == null) {
            this.helper = new BCJcaJceHelper();
        }
        ASN1ObjectIdentifier aSN1ObjectIdentifier = subjectPublicKeyInfo.getAlgorithm().getAlgorithm();
        ASN1Sequence aSN1Sequence = null;
        Object object = new byte[2][];
        try {
            aSN1Sequence = DERSequence.getInstance(subjectPublicKeyInfo.getPublicKeyData().getBytes());
        }
        catch (Exception exception) {
            object = this.split(aSN1ObjectIdentifier, subjectPublicKeyInfo.getPublicKeyData());
        }
        if (MiscObjectIdentifiers.id_alg_composite.equals(aSN1ObjectIdentifier) || MiscObjectIdentifiers.id_composite_key.equals(aSN1ObjectIdentifier)) {
            ASN1Sequence aSN1Sequence2 = ASN1Sequence.getInstance(subjectPublicKeyInfo.getPublicKeyData().getBytes());
            PublicKey[] publicKeyArray = new PublicKey[aSN1Sequence2.size()];
            for (int i2 = 0; i2 != aSN1Sequence2.size(); ++i2) {
                SubjectPublicKeyInfo subjectPublicKeyInfo2 = SubjectPublicKeyInfo.getInstance(aSN1Sequence2.getObjectAt(i2));
                try {
                    publicKeyArray[i2] = this.helper.createKeyFactory(subjectPublicKeyInfo2.getAlgorithm().getAlgorithm().getId()).generatePublic(new X509EncodedKeySpec(subjectPublicKeyInfo2.getEncoded()));
                    continue;
                }
                catch (Exception exception) {
                    throw new IOException("cannot decode generic composite: " + exception.getMessage(), exception);
                }
            }
            return new CompositePublicKey(publicKeyArray);
        }
        try {
            int n2 = aSN1Sequence == null ? ((byte[][])object).length : aSN1Sequence.size();
            List<KeyFactory> list = this.getKeyFactoriesFromIdentifier(aSN1ObjectIdentifier);
            ASN1BitString[] aSN1BitStringArray = new ASN1BitString[n2];
            for (int i3 = 0; i3 < n2; ++i3) {
                if (aSN1Sequence != null) {
                    if (aSN1Sequence.getObjectAt(i3) instanceof DEROctetString) {
                        aSN1BitStringArray[i3] = new DERBitString(((DEROctetString)aSN1Sequence.getObjectAt(i3)).getOctets());
                        continue;
                    }
                    aSN1BitStringArray[i3] = (DERBitString)aSN1Sequence.getObjectAt(i3);
                    continue;
                }
                aSN1BitStringArray[i3] = new DERBitString(object[i3]);
            }
            X509EncodedKeySpec[] x509EncodedKeySpecArray = this.getKeysSpecs(aSN1ObjectIdentifier, aSN1BitStringArray);
            PublicKey[] publicKeyArray = new PublicKey[n2];
            for (int i4 = 0; i4 < n2; ++i4) {
                publicKeyArray[i4] = list.get(i4).generatePublic(x509EncodedKeySpecArray[i4]);
            }
            return new CompositePublicKey(aSN1ObjectIdentifier, publicKeyArray);
        }
        catch (GeneralSecurityException generalSecurityException) {
            throw Exceptions.ioException(generalSecurityException.getMessage(), generalSecurityException);
        }
    }

    byte[][] split(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1BitString aSN1BitString) {
        int[] nArray = componentKeySizes.get(aSN1ObjectIdentifier);
        byte[] byArray = aSN1BitString.getOctets();
        byte[][] byArrayArray = new byte[][]{new byte[nArray[0]], new byte[nArray[1]]};
        return byArrayArray;
    }

    private List<KeyFactory> getKeyFactoriesFromIdentifier(ASN1ObjectIdentifier aSN1ObjectIdentifier) throws NoSuchAlgorithmException, NoSuchProviderException {
        ArrayList<KeyFactory> arrayList = new ArrayList<KeyFactory>();
        ArrayList arrayList2 = new ArrayList();
        String[] stringArray = CompositeIndex.getPairing(aSN1ObjectIdentifier);
        if (stringArray == null) {
            throw new NoSuchAlgorithmException("Cannot create KeyFactories. Unsupported algorithm identifier.");
        }
        arrayList.add(this.helper.createKeyFactory(CompositeIndex.getBaseName(stringArray[0])));
        arrayList.add(this.helper.createKeyFactory(CompositeIndex.getBaseName(stringArray[1])));
        return Collections.unmodifiableList(arrayList);
    }

    private X509EncodedKeySpec[] getKeysSpecs(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1BitString[] aSN1BitStringArray) throws IOException {
        X509EncodedKeySpec[] x509EncodedKeySpecArray = new X509EncodedKeySpec[aSN1BitStringArray.length];
        SubjectPublicKeyInfo[] subjectPublicKeyInfoArray = new SubjectPublicKeyInfo[aSN1BitStringArray.length];
        AlgorithmIdentifier[] algorithmIdentifierArray = pairings.get(aSN1ObjectIdentifier);
        if (algorithmIdentifierArray == null) {
            throw new IOException("Cannot create key specs. Unsupported algorithm identifier.");
        }
        subjectPublicKeyInfoArray[0] = new SubjectPublicKeyInfo(algorithmIdentifierArray[0], aSN1BitStringArray[0]);
        subjectPublicKeyInfoArray[1] = new SubjectPublicKeyInfo(algorithmIdentifierArray[1], aSN1BitStringArray[1]);
        x509EncodedKeySpecArray[0] = new X509EncodedKeySpec(subjectPublicKeyInfoArray[0].getEncoded());
        x509EncodedKeySpecArray[1] = new X509EncodedKeySpec(subjectPublicKeyInfoArray[1].getEncoded());
        return x509EncodedKeySpecArray;
    }

    static {
        pairings.put(MiscObjectIdentifiers.id_MLDSA44_RSA2048_PSS_SHA256, new AlgorithmIdentifier[]{mlDsa44, rsa});
        pairings.put(MiscObjectIdentifiers.id_MLDSA44_RSA2048_PKCS15_SHA256, new AlgorithmIdentifier[]{mlDsa44, rsa});
        pairings.put(MiscObjectIdentifiers.id_MLDSA44_Ed25519_SHA512, new AlgorithmIdentifier[]{mlDsa44, ed25519});
        pairings.put(MiscObjectIdentifiers.id_MLDSA44_ECDSA_P256_SHA256, new AlgorithmIdentifier[]{mlDsa44, ecDsaP256});
        pairings.put(MiscObjectIdentifiers.id_MLDSA65_RSA3072_PSS_SHA256, new AlgorithmIdentifier[]{mlDsa65, rsa});
        pairings.put(MiscObjectIdentifiers.id_MLDSA65_RSA3072_PKCS15_SHA256, new AlgorithmIdentifier[]{mlDsa65, rsa});
        pairings.put(MiscObjectIdentifiers.id_MLDSA65_RSA4096_PSS_SHA384, new AlgorithmIdentifier[]{mlDsa65, rsa});
        pairings.put(MiscObjectIdentifiers.id_MLDSA65_RSA4096_PKCS15_SHA384, new AlgorithmIdentifier[]{mlDsa65, rsa});
        pairings.put(MiscObjectIdentifiers.id_MLDSA65_ECDSA_P384_SHA384, new AlgorithmIdentifier[]{mlDsa65, ecDsaP384});
        pairings.put(MiscObjectIdentifiers.id_MLDSA65_ECDSA_brainpoolP256r1_SHA256, new AlgorithmIdentifier[]{mlDsa65, ecDsaBrainpoolP256r1});
        pairings.put(MiscObjectIdentifiers.id_MLDSA65_Ed25519_SHA512, new AlgorithmIdentifier[]{mlDsa65, ed25519});
        pairings.put(MiscObjectIdentifiers.id_MLDSA87_ECDSA_P384_SHA384, new AlgorithmIdentifier[]{mlDsa87, ecDsaP384});
        pairings.put(MiscObjectIdentifiers.id_MLDSA87_ECDSA_brainpoolP384r1_SHA384, new AlgorithmIdentifier[]{mlDsa87, ecDsaBrainpoolP384r1});
        pairings.put(MiscObjectIdentifiers.id_MLDSA87_Ed448_SHA512, new AlgorithmIdentifier[]{mlDsa87, ed448});
        pairings.put(MiscObjectIdentifiers.id_HashMLDSA44_RSA2048_PSS_SHA256, new AlgorithmIdentifier[]{mlDsa44, rsa});
        pairings.put(MiscObjectIdentifiers.id_HashMLDSA44_RSA2048_PKCS15_SHA256, new AlgorithmIdentifier[]{mlDsa44, rsa});
        pairings.put(MiscObjectIdentifiers.id_HashMLDSA44_Ed25519_SHA512, new AlgorithmIdentifier[]{mlDsa44, ed25519});
        pairings.put(MiscObjectIdentifiers.id_HashMLDSA44_ECDSA_P256_SHA256, new AlgorithmIdentifier[]{mlDsa44, ecDsaP256});
        pairings.put(MiscObjectIdentifiers.id_HashMLDSA65_RSA3072_PSS_SHA512, new AlgorithmIdentifier[]{mlDsa65, rsa});
        pairings.put(MiscObjectIdentifiers.id_HashMLDSA65_RSA3072_PKCS15_SHA512, new AlgorithmIdentifier[]{mlDsa65, rsa});
        pairings.put(MiscObjectIdentifiers.id_HashMLDSA65_RSA4096_PSS_SHA512, new AlgorithmIdentifier[]{mlDsa65, rsa});
        pairings.put(MiscObjectIdentifiers.id_HashMLDSA65_RSA4096_PKCS15_SHA512, new AlgorithmIdentifier[]{mlDsa65, rsa});
        pairings.put(MiscObjectIdentifiers.id_HashMLDSA65_ECDSA_P384_SHA512, new AlgorithmIdentifier[]{mlDsa65, ecDsaP384});
        pairings.put(MiscObjectIdentifiers.id_HashMLDSA65_ECDSA_brainpoolP256r1_SHA512, new AlgorithmIdentifier[]{mlDsa65, ecDsaBrainpoolP256r1});
        pairings.put(MiscObjectIdentifiers.id_HashMLDSA65_Ed25519_SHA512, new AlgorithmIdentifier[]{mlDsa65, ed25519});
        pairings.put(MiscObjectIdentifiers.id_HashMLDSA87_ECDSA_P384_SHA512, new AlgorithmIdentifier[]{mlDsa87, ecDsaP384});
        pairings.put(MiscObjectIdentifiers.id_HashMLDSA87_ECDSA_brainpoolP384r1_SHA512, new AlgorithmIdentifier[]{mlDsa87, ecDsaBrainpoolP384r1});
        pairings.put(MiscObjectIdentifiers.id_HashMLDSA87_Ed448_SHA512, new AlgorithmIdentifier[]{mlDsa87, ed448});
        componentKeySizes.put(MiscObjectIdentifiers.id_MLDSA44_RSA2048_PSS_SHA256, new int[]{1328, 268});
        componentKeySizes.put(MiscObjectIdentifiers.id_MLDSA44_RSA2048_PKCS15_SHA256, new int[]{1312, 284});
        componentKeySizes.put(MiscObjectIdentifiers.id_MLDSA44_Ed25519_SHA512, new int[]{1312, 32});
        componentKeySizes.put(MiscObjectIdentifiers.id_MLDSA44_ECDSA_P256_SHA256, new int[]{1312, 76});
        componentKeySizes.put(MiscObjectIdentifiers.id_MLDSA65_RSA3072_PSS_SHA256, new int[]{1952, 256});
        componentKeySizes.put(MiscObjectIdentifiers.id_MLDSA65_RSA3072_PKCS15_SHA256, new int[]{1952, 256});
        componentKeySizes.put(MiscObjectIdentifiers.id_MLDSA65_RSA4096_PSS_SHA384, new int[]{1952, 542});
        componentKeySizes.put(MiscObjectIdentifiers.id_MLDSA65_RSA4096_PKCS15_SHA384, new int[]{1952, 542});
        componentKeySizes.put(MiscObjectIdentifiers.id_MLDSA65_ECDSA_P384_SHA384, new int[]{1952, 87});
        componentKeySizes.put(MiscObjectIdentifiers.id_MLDSA65_ECDSA_brainpoolP256r1_SHA256, new int[]{1952, 76});
        componentKeySizes.put(MiscObjectIdentifiers.id_MLDSA65_Ed25519_SHA512, new int[]{1952, 32});
        componentKeySizes.put(MiscObjectIdentifiers.id_MLDSA87_ECDSA_P384_SHA384, new int[]{2592, 87});
        componentKeySizes.put(MiscObjectIdentifiers.id_MLDSA87_ECDSA_brainpoolP384r1_SHA384, new int[]{2592, 87});
        componentKeySizes.put(MiscObjectIdentifiers.id_MLDSA87_Ed448_SHA512, new int[]{2592, 57});
        componentKeySizes.put(MiscObjectIdentifiers.id_HashMLDSA44_RSA2048_PSS_SHA256, new int[]{1328, 268});
        componentKeySizes.put(MiscObjectIdentifiers.id_HashMLDSA44_RSA2048_PKCS15_SHA256, new int[]{1312, 284});
        componentKeySizes.put(MiscObjectIdentifiers.id_HashMLDSA44_Ed25519_SHA512, new int[]{1312, 32});
        componentKeySizes.put(MiscObjectIdentifiers.id_HashMLDSA44_ECDSA_P256_SHA256, new int[]{1312, 76});
        componentKeySizes.put(MiscObjectIdentifiers.id_HashMLDSA65_RSA3072_PSS_SHA512, new int[]{1952, 256});
        componentKeySizes.put(MiscObjectIdentifiers.id_HashMLDSA65_RSA3072_PKCS15_SHA512, new int[]{1952, 256});
        componentKeySizes.put(MiscObjectIdentifiers.id_HashMLDSA65_RSA4096_PSS_SHA512, new int[]{1952, 542});
        componentKeySizes.put(MiscObjectIdentifiers.id_HashMLDSA65_RSA4096_PKCS15_SHA512, new int[]{1952, 542});
        componentKeySizes.put(MiscObjectIdentifiers.id_HashMLDSA65_ECDSA_P384_SHA512, new int[]{1952, 87});
        componentKeySizes.put(MiscObjectIdentifiers.id_HashMLDSA65_ECDSA_brainpoolP256r1_SHA512, new int[]{1952, 76});
        componentKeySizes.put(MiscObjectIdentifiers.id_HashMLDSA65_Ed25519_SHA512, new int[]{1952, 32});
        componentKeySizes.put(MiscObjectIdentifiers.id_HashMLDSA87_ECDSA_P384_SHA512, new int[]{2592, 87});
        componentKeySizes.put(MiscObjectIdentifiers.id_HashMLDSA87_ECDSA_brainpoolP384r1_SHA512, new int[]{2592, 87});
        componentKeySizes.put(MiscObjectIdentifiers.id_HashMLDSA87_Ed448_SHA512, new int[]{2592, 57});
    }
}

