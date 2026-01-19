/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jce.provider;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.RC5ParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.engines.TwofishEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.modes.CTSBlockCipher;
import org.bouncycastle.crypto.modes.OFBBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.RC2Parameters;
import org.bouncycastle.crypto.params.RC5Parameters;
import org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey;
import org.bouncycastle.jce.provider.BrokenPBE;
import org.bouncycastle.util.Strings;

public class BrokenJCEBlockCipher
implements BrokenPBE {
    private Class[] availableSpecs = new Class[]{IvParameterSpec.class, PBEParameterSpec.class, RC2ParameterSpec.class, RC5ParameterSpec.class};
    private BufferedBlockCipher cipher;
    private ParametersWithIV ivParam;
    private int pbeType = 2;
    private int pbeHash = 1;
    private int pbeKeySize;
    private int pbeIvSize;
    private int ivLength = 0;
    private AlgorithmParameters engineParams = null;

    protected BrokenJCEBlockCipher(BlockCipher blockCipher) {
        this.cipher = new PaddedBufferedBlockCipher(blockCipher);
    }

    protected BrokenJCEBlockCipher(BlockCipher blockCipher, int n2, int n3, int n4, int n5) {
        this.cipher = new PaddedBufferedBlockCipher(blockCipher);
        this.pbeType = n2;
        this.pbeHash = n3;
        this.pbeKeySize = n4;
        this.pbeIvSize = n5;
    }

    protected int engineGetBlockSize() {
        return this.cipher.getBlockSize();
    }

    protected byte[] engineGetIV() {
        return this.ivParam != null ? this.ivParam.getIV() : null;
    }

    protected int engineGetKeySize(Key key) {
        return key.getEncoded().length;
    }

    protected int engineGetOutputSize(int n2) {
        return this.cipher.getOutputSize(n2);
    }

    protected AlgorithmParameters engineGetParameters() {
        if (this.engineParams == null && this.ivParam != null) {
            String string = this.cipher.getUnderlyingCipher().getAlgorithmName();
            if (string.indexOf(47) >= 0) {
                string = string.substring(0, string.indexOf(47));
            }
            try {
                this.engineParams = AlgorithmParameters.getInstance(string, "BC");
                this.engineParams.init(this.ivParam.getIV());
            }
            catch (Exception exception) {
                throw new RuntimeException(exception.toString());
            }
        }
        return this.engineParams;
    }

    protected void engineSetMode(String string) {
        String string2 = Strings.toUpperCase(string);
        if (string2.equals("ECB")) {
            this.ivLength = 0;
            this.cipher = new PaddedBufferedBlockCipher(this.cipher.getUnderlyingCipher());
        } else if (string2.equals("CBC")) {
            this.ivLength = this.cipher.getUnderlyingCipher().getBlockSize();
            this.cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(this.cipher.getUnderlyingCipher()));
        } else if (string2.startsWith("OFB")) {
            this.ivLength = this.cipher.getUnderlyingCipher().getBlockSize();
            if (string2.length() != 3) {
                int n2 = Integer.parseInt(string2.substring(3));
                this.cipher = new PaddedBufferedBlockCipher(new OFBBlockCipher(this.cipher.getUnderlyingCipher(), n2));
            } else {
                this.cipher = new PaddedBufferedBlockCipher(new OFBBlockCipher(this.cipher.getUnderlyingCipher(), 8 * this.cipher.getBlockSize()));
            }
        } else if (string2.startsWith("CFB")) {
            this.ivLength = this.cipher.getUnderlyingCipher().getBlockSize();
            if (string2.length() != 3) {
                int n3 = Integer.parseInt(string2.substring(3));
                this.cipher = new PaddedBufferedBlockCipher(new CFBBlockCipher(this.cipher.getUnderlyingCipher(), n3));
            } else {
                this.cipher = new PaddedBufferedBlockCipher(new CFBBlockCipher(this.cipher.getUnderlyingCipher(), 8 * this.cipher.getBlockSize()));
            }
        } else {
            throw new IllegalArgumentException("can't support mode " + string);
        }
    }

    protected void engineSetPadding(String string) throws NoSuchPaddingException {
        String string2 = Strings.toUpperCase(string);
        if (string2.equals("NOPADDING")) {
            this.cipher = new BufferedBlockCipher(this.cipher.getUnderlyingCipher());
        } else if (string2.equals("PKCS5PADDING") || string2.equals("PKCS7PADDING") || string2.equals("ISO10126PADDING")) {
            this.cipher = new PaddedBufferedBlockCipher(this.cipher.getUnderlyingCipher());
        } else if (string2.equals("WITHCTS")) {
            this.cipher = new CTSBlockCipher(this.cipher.getUnderlyingCipher());
        } else {
            throw new NoSuchPaddingException("Padding " + string + " unknown.");
        }
    }

    protected void engineInit(int n2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        Object object;
        CipherParameters cipherParameters;
        if (key instanceof BCPBEKey) {
            cipherParameters = BrokenPBE.Util.makePBEParameters((BCPBEKey)key, algorithmParameterSpec, this.pbeType, this.pbeHash, this.cipher.getUnderlyingCipher().getAlgorithmName(), this.pbeKeySize, this.pbeIvSize);
            if (this.pbeIvSize != 0) {
                this.ivParam = (ParametersWithIV)cipherParameters;
            }
        } else if (algorithmParameterSpec == null) {
            cipherParameters = new KeyParameter(key.getEncoded());
        } else if (algorithmParameterSpec instanceof IvParameterSpec) {
            if (this.ivLength != 0) {
                cipherParameters = new ParametersWithIV(new KeyParameter(key.getEncoded()), ((IvParameterSpec)algorithmParameterSpec).getIV());
                this.ivParam = (ParametersWithIV)cipherParameters;
            } else {
                cipherParameters = new KeyParameter(key.getEncoded());
            }
        } else if (algorithmParameterSpec instanceof RC2ParameterSpec) {
            object = (RC2ParameterSpec)algorithmParameterSpec;
            cipherParameters = new RC2Parameters(key.getEncoded(), ((RC2ParameterSpec)algorithmParameterSpec).getEffectiveKeyBits());
            if (((RC2ParameterSpec)object).getIV() != null && this.ivLength != 0) {
                cipherParameters = new ParametersWithIV(cipherParameters, ((RC2ParameterSpec)object).getIV());
                this.ivParam = (ParametersWithIV)cipherParameters;
            }
        } else if (algorithmParameterSpec instanceof RC5ParameterSpec) {
            object = (RC5ParameterSpec)algorithmParameterSpec;
            cipherParameters = new RC5Parameters(key.getEncoded(), ((RC5ParameterSpec)algorithmParameterSpec).getRounds());
            if (((RC5ParameterSpec)object).getWordSize() != 32) {
                throw new IllegalArgumentException("can only accept RC5 word size 32 (at the moment...)");
            }
            if (((RC5ParameterSpec)object).getIV() != null && this.ivLength != 0) {
                cipherParameters = new ParametersWithIV(cipherParameters, ((RC5ParameterSpec)object).getIV());
                this.ivParam = (ParametersWithIV)cipherParameters;
            }
        } else {
            throw new InvalidAlgorithmParameterException("unknown parameter type.");
        }
        if (this.ivLength != 0 && !(cipherParameters instanceof ParametersWithIV)) {
            if (secureRandom == null) {
                secureRandom = CryptoServicesRegistrar.getSecureRandom();
            }
            if (n2 == 1 || n2 == 3) {
                object = new byte[this.ivLength];
                secureRandom.nextBytes((byte[])object);
                cipherParameters = new ParametersWithIV(cipherParameters, (byte[])object);
                this.ivParam = (ParametersWithIV)cipherParameters;
            } else {
                throw new InvalidAlgorithmParameterException("no IV set when one expected");
            }
        }
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
                throw new IllegalArgumentException("unknown opmode: " + n2);
            }
        }
    }

    protected void engineInit(int n2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        AlgorithmParameterSpec algorithmParameterSpec = null;
        if (algorithmParameters != null) {
            for (int i2 = 0; i2 != this.availableSpecs.length; ++i2) {
                try {
                    algorithmParameterSpec = (AlgorithmParameterSpec)algorithmParameters.getParameterSpec(this.availableSpecs[i2]);
                    break;
                }
                catch (Exception exception) {
                    continue;
                }
            }
            if (algorithmParameterSpec == null) {
                throw new InvalidAlgorithmParameterException("can't handle parameter " + algorithmParameters.toString());
            }
        }
        this.engineParams = algorithmParameters;
        this.engineInit(n2, key, algorithmParameterSpec, secureRandom);
    }

    protected void engineInit(int n2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        try {
            this.engineInit(n2, key, (AlgorithmParameterSpec)null, secureRandom);
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new IllegalArgumentException(invalidAlgorithmParameterException.getMessage());
        }
    }

    protected byte[] engineUpdate(byte[] byArray, int n2, int n3) {
        int n4 = this.cipher.getUpdateOutputSize(n3);
        if (n4 > 0) {
            byte[] byArray2 = new byte[n4];
            this.cipher.processBytes(byArray, n2, n3, byArray2, 0);
            return byArray2;
        }
        this.cipher.processBytes(byArray, n2, n3, null, 0);
        return null;
    }

    protected int engineUpdate(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) {
        return this.cipher.processBytes(byArray, n2, n3, byArray2, n4);
    }

    protected byte[] engineDoFinal(byte[] byArray, int n2, int n3) throws IllegalBlockSizeException, BadPaddingException {
        int n4 = 0;
        byte[] byArray2 = new byte[this.engineGetOutputSize(n3)];
        if (n3 != 0) {
            n4 = this.cipher.processBytes(byArray, n2, n3, byArray2, 0);
        }
        try {
            n4 += this.cipher.doFinal(byArray2, n4);
        }
        catch (DataLengthException dataLengthException) {
            throw new IllegalBlockSizeException(dataLengthException.getMessage());
        }
        catch (InvalidCipherTextException invalidCipherTextException) {
            throw new BadPaddingException(invalidCipherTextException.getMessage());
        }
        byte[] byArray3 = new byte[n4];
        System.arraycopy(byArray2, 0, byArray3, 0, n4);
        return byArray3;
    }

    protected int engineDoFinal(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws IllegalBlockSizeException, BadPaddingException {
        int n5 = 0;
        if (n3 != 0) {
            n5 = this.cipher.processBytes(byArray, n2, n3, byArray2, n4);
        }
        try {
            return n5 + this.cipher.doFinal(byArray2, n4 + n5);
        }
        catch (DataLengthException dataLengthException) {
            throw new IllegalBlockSizeException(dataLengthException.getMessage());
        }
        catch (InvalidCipherTextException invalidCipherTextException) {
            throw new BadPaddingException(invalidCipherTextException.getMessage());
        }
    }

    protected byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        byte[] byArray = key.getEncoded();
        if (byArray == null) {
            throw new InvalidKeyException("Cannot wrap key, null encoding.");
        }
        try {
            return this.engineDoFinal(byArray, 0, byArray.length);
        }
        catch (BadPaddingException badPaddingException) {
            throw new IllegalBlockSizeException(badPaddingException.getMessage());
        }
    }

    protected Key engineUnwrap(byte[] byArray, String string, int n2) throws InvalidKeyException {
        byte[] byArray2 = null;
        try {
            byArray2 = this.engineDoFinal(byArray, 0, byArray.length);
        }
        catch (BadPaddingException badPaddingException) {
            throw new InvalidKeyException(badPaddingException.getMessage());
        }
        catch (IllegalBlockSizeException illegalBlockSizeException) {
            throw new InvalidKeyException(illegalBlockSizeException.getMessage());
        }
        if (n2 == 3) {
            return new SecretKeySpec(byArray2, string);
        }
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(string, "BC");
            if (n2 == 1) {
                return keyFactory.generatePublic(new X509EncodedKeySpec(byArray2));
            }
            if (n2 == 2) {
                return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(byArray2));
            }
        }
        catch (NoSuchProviderException noSuchProviderException) {
            throw new InvalidKeyException("Unknown key type " + noSuchProviderException.getMessage());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new InvalidKeyException("Unknown key type " + noSuchAlgorithmException.getMessage());
        }
        catch (InvalidKeySpecException invalidKeySpecException) {
            throw new InvalidKeyException("Unknown key type " + invalidKeySpecException.getMessage());
        }
        throw new InvalidKeyException("Unknown key type " + n2);
    }

    public static class BrokePBEWithMD5AndDES
    extends BrokenJCEBlockCipher {
        public BrokePBEWithMD5AndDES() {
            super(new CBCBlockCipher(new DESEngine()), 0, 0, 64, 64);
        }
    }

    public static class BrokePBEWithSHA1AndDES
    extends BrokenJCEBlockCipher {
        public BrokePBEWithSHA1AndDES() {
            super(new CBCBlockCipher(new DESEngine()), 0, 1, 64, 64);
        }
    }

    public static class BrokePBEWithSHAAndDES2Key
    extends BrokenJCEBlockCipher {
        public BrokePBEWithSHAAndDES2Key() {
            super(new CBCBlockCipher(new DESedeEngine()), 2, 1, 128, 64);
        }
    }

    public static class BrokePBEWithSHAAndDES3Key
    extends BrokenJCEBlockCipher {
        public BrokePBEWithSHAAndDES3Key() {
            super(new CBCBlockCipher(new DESedeEngine()), 2, 1, 192, 64);
        }
    }

    public static class OldPBEWithSHAAndDES3Key
    extends BrokenJCEBlockCipher {
        public OldPBEWithSHAAndDES3Key() {
            super(new CBCBlockCipher(new DESedeEngine()), 3, 1, 192, 64);
        }
    }

    public static class OldPBEWithSHAAndTwofish
    extends BrokenJCEBlockCipher {
        public OldPBEWithSHAAndTwofish() {
            super(new CBCBlockCipher(new TwofishEngine()), 3, 1, 256, 128);
        }
    }
}

