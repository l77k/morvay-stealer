/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jcajce.provider.symmetric.util;

import java.io.Serializable;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.RC5ParameterSpec;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jcajce.PKCS12Key;
import org.bouncycastle.jcajce.PKCS12KeyWithParameters;
import org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseWrapCipher;
import org.bouncycastle.jcajce.provider.symmetric.util.PBE;
import org.bouncycastle.jcajce.provider.symmetric.util.SpecUtil;

public class BaseStreamCipher
extends BaseWrapCipher
implements PBE {
    private Class[] availableSpecs = new Class[]{RC2ParameterSpec.class, RC5ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class};
    private StreamCipher cipher;
    private int keySizeInBits;
    private int digest;
    private ParametersWithIV ivParam;
    private int ivLength = 0;
    private PBEParameterSpec pbeSpec = null;
    private String pbeAlgorithm = null;

    protected BaseStreamCipher(StreamCipher streamCipher, int n2) {
        this(streamCipher, n2, -1, -1);
    }

    protected BaseStreamCipher(StreamCipher streamCipher, int n2, int n3) {
        this(streamCipher, n2, n3, -1);
    }

    protected BaseStreamCipher(StreamCipher streamCipher, int n2, int n3, int n4) {
        this.cipher = streamCipher;
        this.ivLength = n2;
        this.keySizeInBits = n3;
        this.digest = n4;
    }

    @Override
    protected int engineGetBlockSize() {
        return 0;
    }

    @Override
    protected byte[] engineGetIV() {
        return this.ivParam != null ? this.ivParam.getIV() : null;
    }

    @Override
    protected int engineGetKeySize(Key key) {
        return key.getEncoded().length * 8;
    }

    @Override
    protected int engineGetOutputSize(int n2) {
        return n2;
    }

    @Override
    protected AlgorithmParameters engineGetParameters() {
        if (this.engineParams == null) {
            if (this.pbeSpec != null) {
                try {
                    AlgorithmParameters algorithmParameters = this.createParametersInstance(this.pbeAlgorithm);
                    algorithmParameters.init(this.pbeSpec);
                    return algorithmParameters;
                }
                catch (Exception exception) {
                    return null;
                }
            }
            if (this.ivParam != null) {
                String string = this.cipher.getAlgorithmName();
                if (string.indexOf(47) >= 0) {
                    string = string.substring(0, string.indexOf(47));
                }
                if (string.startsWith("ChaCha7539")) {
                    string = "ChaCha7539";
                } else if (string.startsWith("Grain")) {
                    string = "Grainv1";
                } else if (string.startsWith("HC")) {
                    int n2 = string.indexOf(45);
                    string = string.substring(0, n2) + string.substring(n2 + 1);
                }
                try {
                    this.engineParams = this.createParametersInstance(string);
                    this.engineParams.init(new IvParameterSpec(this.ivParam.getIV()));
                }
                catch (Exception exception) {
                    throw new RuntimeException(exception.toString());
                }
            }
        }
        return this.engineParams;
    }

    @Override
    protected void engineSetMode(String string) throws NoSuchAlgorithmException {
        if (!string.equalsIgnoreCase("ECB") && !string.equals("NONE")) {
            throw new NoSuchAlgorithmException("can't support mode " + string);
        }
    }

    @Override
    protected void engineSetPadding(String string) throws NoSuchPaddingException {
        if (!string.equalsIgnoreCase("NoPadding")) {
            throw new NoSuchPaddingException("Padding " + string + " unknown.");
        }
    }

    @Override
    protected void engineInit(int n2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        CipherParameters cipherParameters;
        Serializable serializable;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.engineParams = null;
        if (!(key instanceof SecretKey)) {
            throw new InvalidKeyException("Key for algorithm " + key.getAlgorithm() + " not suitable for symmetric enryption.");
        }
        if (key instanceof PKCS12Key) {
            serializable = (PKCS12Key)key;
            this.pbeSpec = (PBEParameterSpec)algorithmParameterSpec;
            if (serializable instanceof PKCS12KeyWithParameters && this.pbeSpec == null) {
                this.pbeSpec = new PBEParameterSpec(((PKCS12KeyWithParameters)serializable).getSalt(), ((PKCS12KeyWithParameters)serializable).getIterationCount());
            }
            cipherParameters = PBE.Util.makePBEParameters(((PKCS12Key)serializable).getEncoded(), 2, this.digest, this.keySizeInBits, this.ivLength * 8, this.pbeSpec, this.cipher.getAlgorithmName());
        } else if (key instanceof BCPBEKey) {
            serializable = (BCPBEKey)key;
            this.pbeAlgorithm = ((BCPBEKey)serializable).getOID() != null ? ((BCPBEKey)serializable).getOID().getId() : ((BCPBEKey)serializable).getAlgorithm();
            if (((BCPBEKey)serializable).getParam() != null) {
                cipherParameters = ((BCPBEKey)serializable).getParam();
                this.pbeSpec = new PBEParameterSpec(((BCPBEKey)serializable).getSalt(), ((BCPBEKey)serializable).getIterationCount());
            } else if (algorithmParameterSpec instanceof PBEParameterSpec) {
                cipherParameters = PBE.Util.makePBEParameters((BCPBEKey)serializable, algorithmParameterSpec, this.cipher.getAlgorithmName());
                this.pbeSpec = (PBEParameterSpec)algorithmParameterSpec;
            } else {
                throw new InvalidAlgorithmParameterException("PBE requires PBE parameters to be set.");
            }
            if (((BCPBEKey)serializable).getIvSize() != 0) {
                this.ivParam = (ParametersWithIV)cipherParameters;
            }
        } else if (algorithmParameterSpec == null) {
            if (this.digest > 0) {
                throw new InvalidKeyException("Algorithm requires a PBE key");
            }
            cipherParameters = new KeyParameter(key.getEncoded());
        } else if (algorithmParameterSpec instanceof IvParameterSpec) {
            cipherParameters = new ParametersWithIV(new KeyParameter(key.getEncoded()), ((IvParameterSpec)algorithmParameterSpec).getIV());
            this.ivParam = (ParametersWithIV)cipherParameters;
        } else {
            throw new InvalidAlgorithmParameterException("unknown parameter type.");
        }
        if (this.ivLength != 0 && !(cipherParameters instanceof ParametersWithIV)) {
            serializable = secureRandom;
            if (serializable == null) {
                serializable = CryptoServicesRegistrar.getSecureRandom();
            }
            if (n2 == 1 || n2 == 3) {
                byte[] byArray = new byte[this.ivLength];
                ((SecureRandom)serializable).nextBytes(byArray);
                cipherParameters = new ParametersWithIV(cipherParameters, byArray);
                this.ivParam = (ParametersWithIV)cipherParameters;
            } else {
                throw new InvalidAlgorithmParameterException("no IV set when one expected");
            }
        }
        try {
            switch (n2) {
                case 1: 
                case 3: {
                    this.cipher.init(true, cipherParameters);
                    break;
                }
                case 2: 
                case 4: {
                    this.cipher.init(false, cipherParameters);
                    break;
                }
                default: {
                    throw new InvalidParameterException("unknown opmode " + n2 + " passed");
                }
            }
        }
        catch (Exception exception) {
            throw new InvalidKeyException(exception.getMessage());
        }
    }

    @Override
    protected void engineInit(int n2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        AlgorithmParameterSpec algorithmParameterSpec = null;
        if (algorithmParameters != null && (algorithmParameterSpec = SpecUtil.extractSpec(algorithmParameters, this.availableSpecs)) == null) {
            throw new InvalidAlgorithmParameterException("can't handle parameter " + algorithmParameters.toString());
        }
        this.engineInit(n2, key, algorithmParameterSpec, secureRandom);
        this.engineParams = algorithmParameters;
    }

    @Override
    protected void engineInit(int n2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        try {
            this.engineInit(n2, key, (AlgorithmParameterSpec)null, secureRandom);
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new InvalidKeyException(invalidAlgorithmParameterException.getMessage());
        }
    }

    @Override
    protected byte[] engineUpdate(byte[] byArray, int n2, int n3) {
        byte[] byArray2 = new byte[n3];
        this.cipher.processBytes(byArray, n2, n3, byArray2, 0);
        return byArray2;
    }

    @Override
    protected int engineUpdate(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws ShortBufferException {
        if (n4 + n3 > byArray2.length) {
            throw new ShortBufferException("output buffer too short for input.");
        }
        try {
            this.cipher.processBytes(byArray, n2, n3, byArray2, n4);
            return n3;
        }
        catch (DataLengthException dataLengthException) {
            throw new IllegalStateException(dataLengthException.getMessage());
        }
    }

    @Override
    protected byte[] engineDoFinal(byte[] byArray, int n2, int n3) {
        if (n3 != 0) {
            byte[] byArray2 = this.engineUpdate(byArray, n2, n3);
            this.cipher.reset();
            return byArray2;
        }
        this.cipher.reset();
        return new byte[0];
    }

    @Override
    protected int engineDoFinal(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws ShortBufferException {
        if (n4 + n3 > byArray2.length) {
            throw new ShortBufferException("output buffer too short for input.");
        }
        if (n3 != 0) {
            this.cipher.processBytes(byArray, n2, n3, byArray2, n4);
        }
        this.cipher.reset();
        return n3;
    }
}

