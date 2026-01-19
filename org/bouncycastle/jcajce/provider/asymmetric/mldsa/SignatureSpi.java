/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jcajce.provider.asymmetric.mldsa;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.jcajce.provider.asymmetric.mldsa.BCMLDSAPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.mldsa.BCMLDSAPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.util.BaseDeterministicOrRandomSignature;
import org.bouncycastle.jcajce.spec.MLDSAParameterSpec;
import org.bouncycastle.pqc.crypto.mldsa.MLDSAParameters;
import org.bouncycastle.pqc.crypto.mldsa.MLDSASigner;

public class SignatureSpi
extends BaseDeterministicOrRandomSignature {
    private MLDSASigner signer;
    private MLDSAParameters parameters;

    protected SignatureSpi(MLDSASigner mLDSASigner) {
        super("MLDSA");
        this.signer = mLDSASigner;
        this.parameters = null;
    }

    protected SignatureSpi(MLDSASigner mLDSASigner, MLDSAParameters mLDSAParameters) {
        super(MLDSAParameterSpec.fromName(mLDSAParameters.getName()).getName());
        this.signer = mLDSASigner;
        this.parameters = mLDSAParameters;
    }

    @Override
    protected void verifyInit(PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof BCMLDSAPublicKey) {
            String string;
            BCMLDSAPublicKey bCMLDSAPublicKey = (BCMLDSAPublicKey)publicKey;
            this.keyParams = bCMLDSAPublicKey.getKeyParams();
            if (this.parameters != null && !(string = MLDSAParameterSpec.fromName(this.parameters.getName()).getName()).equals(bCMLDSAPublicKey.getAlgorithm())) {
                throw new InvalidKeyException("signature configured for " + string);
            }
        } else {
            throw new InvalidKeyException("unknown public key passed to ML-DSA");
        }
    }

    @Override
    protected void signInit(PrivateKey privateKey, SecureRandom secureRandom) throws InvalidKeyException {
        this.appRandom = secureRandom;
        if (privateKey instanceof BCMLDSAPrivateKey) {
            String string;
            BCMLDSAPrivateKey bCMLDSAPrivateKey = (BCMLDSAPrivateKey)privateKey;
            this.keyParams = bCMLDSAPrivateKey.getKeyParams();
            if (this.parameters != null && !(string = MLDSAParameterSpec.fromName(this.parameters.getName()).getName()).equals(bCMLDSAPrivateKey.getAlgorithm())) {
                throw new InvalidKeyException("signature configured for " + string);
            }
        } else {
            throw new InvalidKeyException("unknown private key passed to ML-DSA");
        }
    }

    @Override
    protected void updateEngine(byte by) throws SignatureException {
        this.signer.update(by);
    }

    @Override
    protected void updateEngine(byte[] byArray, int n2, int n3) throws SignatureException {
        this.signer.update(byArray, n2, n3);
    }

    @Override
    protected byte[] engineSign() throws SignatureException {
        try {
            return this.signer.generateSignature();
        }
        catch (Exception exception) {
            throw new SignatureException(exception.toString());
        }
    }

    @Override
    protected boolean engineVerify(byte[] byArray) throws SignatureException {
        return this.signer.verifySignature(byArray);
    }

    @Override
    protected void reInitialize(boolean bl, CipherParameters cipherParameters) {
        this.signer.init(bl, cipherParameters);
    }

    public static class MLDSA
    extends SignatureSpi {
        public MLDSA() {
            super(new MLDSASigner());
        }
    }

    public static class MLDSA44
    extends SignatureSpi {
        public MLDSA44() {
            super(new MLDSASigner(), MLDSAParameters.ml_dsa_44);
        }
    }

    public static class MLDSA65
    extends SignatureSpi {
        public MLDSA65() {
            super(new MLDSASigner(), MLDSAParameters.ml_dsa_65);
        }
    }

    public static class MLDSA87
    extends SignatureSpi {
        public MLDSA87() throws NoSuchAlgorithmException {
            super(new MLDSASigner(), MLDSAParameters.ml_dsa_87);
        }
    }
}

