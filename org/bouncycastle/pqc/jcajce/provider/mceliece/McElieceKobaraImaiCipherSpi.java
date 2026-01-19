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
import org.bouncycastle.pqc.legacy.crypto.mceliece.McElieceKobaraImaiCipher;

public class McElieceKobaraImaiCipherSpi
extends AsymmetricHybridCipher
implements PKCSObjectIdentifiers,
X509ObjectIdentifiers {
    private Digest digest;
    private McElieceKobaraImaiCipher cipher;
    private ByteArrayOutputStream buf = new ByteArrayOutputStream();

    public McElieceKobaraImaiCipherSpi() {
        this.buf = new ByteArrayOutputStream();
    }

    protected McElieceKobaraImaiCipherSpi(Digest digest, McElieceKobaraImaiCipher mcElieceKobaraImaiCipher) {
        this.digest = digest;
        this.cipher = mcElieceKobaraImaiCipher;
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
        if (this.opMode == 1) {
            return this.cipher.messageEncrypt(this.pad());
        }
        if (this.opMode == 2) {
            try {
                byte[] byArray2 = this.buf.toByteArray();
                this.buf.reset();
                return this.unpad(this.cipher.messageDecrypt(byArray2));
            }
            catch (InvalidCipherTextException invalidCipherTextException) {
                throw new BadPaddingException(invalidCipherTextException.getMessage());
            }
        }
        throw new IllegalStateException("unknown mode in doFinal");
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
        this.buf.reset();
        CipherParameters cipherParameters = McElieceCCA2KeysToParams.generatePublicKeyParameter((PublicKey)key);
        cipherParameters = new ParametersWithRandom(cipherParameters, secureRandom);
        this.digest.reset();
        this.cipher.init(true, cipherParameters);
    }

    @Override
    protected void initCipherDecrypt(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.buf.reset();
        AsymmetricKeyParameter asymmetricKeyParameter = McElieceCCA2KeysToParams.generatePrivateKeyParameter((PrivateKey)key);
        this.digest.reset();
        this.cipher.init(false, asymmetricKeyParameter);
    }

    @Override
    public String getName() {
        return "McElieceKobaraImaiCipher";
    }

    @Override
    public int getKeySize(Key key) throws InvalidKeyException {
        if (key instanceof PublicKey) {
            McElieceCCA2KeyParameters mcElieceCCA2KeyParameters = (McElieceCCA2KeyParameters)McElieceCCA2KeysToParams.generatePublicKeyParameter((PublicKey)key);
            return this.cipher.getKeySize(mcElieceCCA2KeyParameters);
        }
        if (key instanceof PrivateKey) {
            McElieceCCA2KeyParameters mcElieceCCA2KeyParameters = (McElieceCCA2KeyParameters)McElieceCCA2KeysToParams.generatePrivateKeyParameter((PrivateKey)key);
            return this.cipher.getKeySize(mcElieceCCA2KeyParameters);
        }
        throw new InvalidKeyException();
    }

    private byte[] pad() {
        this.buf.write(1);
        byte[] byArray = this.buf.toByteArray();
        this.buf.reset();
        return byArray;
    }

    private byte[] unpad(byte[] byArray) throws BadPaddingException {
        int n2;
        for (n2 = byArray.length - 1; n2 >= 0 && byArray[n2] == 0; --n2) {
        }
        if (byArray[n2] != 1) {
            throw new BadPaddingException("invalid ciphertext");
        }
        byte[] byArray2 = new byte[n2];
        System.arraycopy(byArray, 0, byArray2, 0, n2);
        return byArray2;
    }

    public static class McElieceKobaraImai
    extends McElieceKobaraImaiCipherSpi {
        public McElieceKobaraImai() {
            super(DigestFactory.createSHA1(), new McElieceKobaraImaiCipher());
        }
    }

    public static class McElieceKobaraImai224
    extends McElieceKobaraImaiCipherSpi {
        public McElieceKobaraImai224() {
            super(DigestFactory.createSHA224(), new McElieceKobaraImaiCipher());
        }
    }

    public static class McElieceKobaraImai256
    extends McElieceKobaraImaiCipherSpi {
        public McElieceKobaraImai256() {
            super(DigestFactory.createSHA256(), new McElieceKobaraImaiCipher());
        }
    }

    public static class McElieceKobaraImai384
    extends McElieceKobaraImaiCipherSpi {
        public McElieceKobaraImai384() {
            super(DigestFactory.createSHA384(), new McElieceKobaraImaiCipher());
        }
    }

    public static class McElieceKobaraImai512
    extends McElieceKobaraImaiCipherSpi {
        public McElieceKobaraImai512() {
            super(DigestFactory.createSHA512(), new McElieceKobaraImaiCipher());
        }
    }
}

