/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.jcajce.provider.mceliece;

import java.io.ByteArrayOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.util.DigestFactory;
import org.bouncycastle.pqc.jcajce.provider.mceliece.McElieceCCA2KeysToParams;
import org.bouncycastle.pqc.jcajce.provider.util.AsymmetricHybridCipher;
import org.bouncycastle.pqc.legacy.crypto.mceliece.McElieceCCA2KeyParameters;
import org.bouncycastle.pqc.legacy.crypto.mceliece.McEliecePointchevalCipher;

public class McEliecePointchevalCipherSpi
extends AsymmetricHybridCipher
implements PKCSObjectIdentifiers,
X509ObjectIdentifiers {
    private Digest digest;
    private McEliecePointchevalCipher cipher;
    private ByteArrayOutputStream buf = new ByteArrayOutputStream();

    protected McEliecePointchevalCipherSpi(Digest digest, McEliecePointchevalCipher mcEliecePointchevalCipher) {
        this.digest = digest;
        this.cipher = mcEliecePointchevalCipher;
        this.buf = new ByteArrayOutputStream();
    }

    @Override
    public byte[] update(byte[] byArray, int n2, int n3) {
        this.buf.write(byArray, n2, n3);
        return new byte[0];
    }

    @Override
    public byte[] doFinal(byte[] byArray, int n2, int n3) throws BadPaddingException {
        this.update(byArray, n2, n3);
        byte[] byArray2 = this.buf.toByteArray();
        this.buf.reset();
        if (this.opMode == 1) {
            return this.cipher.messageEncrypt(byArray2);
        }
        if (this.opMode == 2) {
            try {
                return this.cipher.messageDecrypt(byArray2);
            }
            catch (InvalidCipherTextException invalidCipherTextException) {
                throw new BadPaddingException(invalidCipherTextException.getMessage());
            }
        }
        return null;
    }

    @Override
    protected int encryptOutputSize(int n2) {
        return 0;
    }

    @Override
    protected int decryptOutputSize(int n2) {
        return 0;
    }

    @Override
    protected void initCipherEncrypt(Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        CipherParameters cipherParameters = McElieceCCA2KeysToParams.generatePublicKeyParameter((PublicKey)key);
        cipherParameters = new ParametersWithRandom(cipherParameters, secureRandom);
        this.digest.reset();
        this.cipher.init(true, cipherParameters);
    }

    @Override
    protected void initCipherDecrypt(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        AsymmetricKeyParameter asymmetricKeyParameter = McElieceCCA2KeysToParams.generatePrivateKeyParameter((PrivateKey)key);
        this.digest.reset();
        this.cipher.init(false, asymmetricKeyParameter);
    }

    @Override
    public String getName() {
        return "McEliecePointchevalCipher";
    }

    @Override
    public int getKeySize(Key key) throws InvalidKeyException {
        McElieceCCA2KeyParameters mcElieceCCA2KeyParameters = key instanceof PublicKey ? (McElieceCCA2KeyParameters)McElieceCCA2KeysToParams.generatePublicKeyParameter((PublicKey)key) : (McElieceCCA2KeyParameters)McElieceCCA2KeysToParams.generatePrivateKeyParameter((PrivateKey)key);
        return this.cipher.getKeySize(mcElieceCCA2KeyParameters);
    }

    public static class McEliecePointcheval
    extends McEliecePointchevalCipherSpi {
        public McEliecePointcheval() {
            super(DigestFactory.createSHA1(), new McEliecePointchevalCipher());
        }
    }

    public static class McEliecePointcheval224
    extends McEliecePointchevalCipherSpi {
        public McEliecePointcheval224() {
            super(DigestFactory.createSHA224(), new McEliecePointchevalCipher());
        }
    }

    public static class McEliecePointcheval256
    extends McEliecePointchevalCipherSpi {
        public McEliecePointcheval256() {
            super(DigestFactory.createSHA256(), new McEliecePointchevalCipher());
        }
    }

    public static class McEliecePointcheval384
    extends McEliecePointchevalCipherSpi {
        public McEliecePointcheval384() {
            super(DigestFactory.createSHA384(), new McEliecePointchevalCipher());
        }
    }

    public static class McEliecePointcheval512
    extends McEliecePointchevalCipherSpi {
        public McEliecePointcheval512() {
            super(DigestFactory.createSHA512(), new McEliecePointchevalCipher());
        }
    }
}

