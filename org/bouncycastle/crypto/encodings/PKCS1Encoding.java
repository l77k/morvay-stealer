/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.encodings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Properties;

public class PKCS1Encoding
implements AsymmetricBlockCipher {
    public static final String STRICT_LENGTH_ENABLED_PROPERTY = "org.bouncycastle.pkcs1.strict";
    public static final String NOT_STRICT_LENGTH_ENABLED_PROPERTY = "org.bouncycastle.pkcs1.not_strict";
    private static final int HEADER_LENGTH = 10;
    private SecureRandom random;
    private AsymmetricBlockCipher engine;
    private boolean forEncryption;
    private boolean forPrivateKey;
    private boolean useStrictLength;
    private int pLen = -1;
    private byte[] fallback = null;
    private byte[] blockBuffer;

    public PKCS1Encoding(AsymmetricBlockCipher asymmetricBlockCipher) {
        this.engine = asymmetricBlockCipher;
        this.useStrictLength = this.useStrict();
    }

    public PKCS1Encoding(AsymmetricBlockCipher asymmetricBlockCipher, int n2) {
        this.engine = asymmetricBlockCipher;
        this.useStrictLength = this.useStrict();
        this.pLen = n2;
    }

    public PKCS1Encoding(AsymmetricBlockCipher asymmetricBlockCipher, byte[] byArray) {
        this.engine = asymmetricBlockCipher;
        this.useStrictLength = this.useStrict();
        this.fallback = byArray;
        this.pLen = byArray.length;
    }

    private boolean useStrict() {
        if (Properties.isOverrideSetTo(NOT_STRICT_LENGTH_ENABLED_PROPERTY, true)) {
            return false;
        }
        return !Properties.isOverrideSetTo(STRICT_LENGTH_ENABLED_PROPERTY, false);
    }

    public AsymmetricBlockCipher getUnderlyingCipher() {
        return this.engine;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        AsymmetricKeyParameter asymmetricKeyParameter;
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.random = parametersWithRandom.getRandom();
            asymmetricKeyParameter = (AsymmetricKeyParameter)parametersWithRandom.getParameters();
        } else {
            asymmetricKeyParameter = (AsymmetricKeyParameter)cipherParameters;
            if (!asymmetricKeyParameter.isPrivate() && bl) {
                this.random = CryptoServicesRegistrar.getSecureRandom();
            }
        }
        this.engine.init(bl, cipherParameters);
        this.forPrivateKey = asymmetricKeyParameter.isPrivate();
        this.forEncryption = bl;
        this.blockBuffer = new byte[this.engine.getOutputBlockSize()];
        if (this.pLen > 0 && this.fallback == null && this.random == null) {
            throw new IllegalArgumentException("encoder requires random");
        }
    }

    @Override
    public int getInputBlockSize() {
        int n2 = this.engine.getInputBlockSize();
        if (this.forEncryption) {
            return n2 - 10;
        }
        return n2;
    }

    @Override
    public int getOutputBlockSize() {
        int n2 = this.engine.getOutputBlockSize();
        if (this.forEncryption) {
            return n2;
        }
        return n2 - 10;
    }

    @Override
    public byte[] processBlock(byte[] byArray, int n2, int n3) throws InvalidCipherTextException {
        if (this.forEncryption) {
            return this.encodeBlock(byArray, n2, n3);
        }
        return this.decodeBlock(byArray, n2, n3);
    }

    private byte[] encodeBlock(byte[] byArray, int n2, int n3) throws InvalidCipherTextException {
        if (n3 > this.getInputBlockSize()) {
            throw new IllegalArgumentException("input data too large");
        }
        byte[] byArray2 = new byte[this.engine.getInputBlockSize()];
        if (this.forPrivateKey) {
            byArray2[0] = 1;
            for (int i2 = 1; i2 != byArray2.length - n3 - 1; ++i2) {
                byArray2[i2] = -1;
            }
        } else {
            this.random.nextBytes(byArray2);
            byArray2[0] = 2;
            for (int i3 = 1; i3 != byArray2.length - n3 - 1; ++i3) {
                while (byArray2[i3] == 0) {
                    byArray2[i3] = (byte)this.random.nextInt();
                }
            }
        }
        byArray2[byArray2.length - n3 - 1] = 0;
        System.arraycopy(byArray, n2, byArray2, byArray2.length - n3, n3);
        return this.engine.processBlock(byArray2, 0, byArray2.length);
    }

    private static int checkPkcs1Encoding1(byte[] byArray) {
        int n2;
        int n3 = 0;
        int n4 = 0;
        int n5 = -(byArray[0] & 0xFF ^ 1);
        for (n2 = 1; n2 < byArray.length; ++n2) {
            int n6 = byArray[n2] & 0xFF;
            int n7 = (n6 ^ 0) - 1 >> 31;
            int n8 = (n6 ^ 0xFF) - 1 >> 31;
            n4 ^= n2 & ~n3 & n7;
            n5 |= ~((n3 |= n7) | n8);
        }
        n2 = byArray.length - 1 - n4;
        return n2 | (n5 |= n4 - 9) >> 31;
    }

    private static int checkPkcs1Encoding2(byte[] byArray) {
        int n2;
        int n3 = 0;
        int n4 = 0;
        int n5 = -(byArray[0] & 0xFF ^ 2);
        for (n2 = 1; n2 < byArray.length; ++n2) {
            int n6 = byArray[n2] & 0xFF;
            int n7 = (n6 ^ 0) - 1 >> 31;
            n4 ^= n2 & ~n3 & n7;
            n3 |= n7;
        }
        n2 = byArray.length - 1 - n4;
        return n2 | (n5 |= n4 - 9) >> 31;
    }

    private static int checkPkcs1Encoding2(byte[] byArray, int n2) {
        int n3 = -(byArray[0] & 0xFF ^ 2);
        int n4 = byArray.length - 1 - n2;
        n3 |= n4 - 9;
        for (int i2 = 1; i2 < n4; ++i2) {
            n3 |= (byArray[i2] & 0xFF) - 1;
        }
        return (n3 |= -(byArray[n4] & 0xFF)) >> 31;
    }

    private byte[] decodeBlockOrRandom(byte[] byArray, int n2, int n3) throws InvalidCipherTextException {
        byte[] byArray2;
        if (!this.forPrivateKey) {
            throw new InvalidCipherTextException("sorry, this method is only for decryption, not for signing");
        }
        int n4 = this.pLen;
        byte[] byArray3 = this.fallback;
        if (this.fallback == null) {
            byArray3 = new byte[n4];
            this.random.nextBytes(byArray3);
        }
        int n5 = 0;
        int n6 = this.engine.getOutputBlockSize();
        byte[] byArray4 = byArray2 = this.engine.processBlock(byArray, n2, n3);
        if (byArray2.length != n6 && (this.useStrictLength || byArray2.length < n6)) {
            byArray4 = this.blockBuffer;
        }
        n5 |= PKCS1Encoding.checkPkcs1Encoding2(byArray4, n4);
        int n7 = byArray4.length - n4;
        byte[] byArray5 = new byte[n4];
        for (int i2 = 0; i2 < n4; ++i2) {
            byArray5[i2] = (byte)(byArray4[n7 + i2] & ~n5 | byArray3[i2] & n5);
        }
        Arrays.fill(byArray2, (byte)0);
        Arrays.fill(this.blockBuffer, 0, Math.max(0, this.blockBuffer.length - byArray2.length), (byte)0);
        return byArray5;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private byte[] decodeBlock(byte[] byArray, int n2, int n3) throws InvalidCipherTextException {
        if (this.forPrivateKey && this.pLen != -1) {
            return this.decodeBlockOrRandom(byArray, n2, n3);
        }
        int n4 = this.engine.getOutputBlockSize();
        byte[] byArray2 = this.engine.processBlock(byArray, n2, n3);
        boolean bl = this.useStrictLength & byArray2.length != n4;
        byte[] byArray3 = byArray2;
        if (byArray2.length < n4) {
            byArray3 = this.blockBuffer;
        }
        int n5 = this.forPrivateKey ? PKCS1Encoding.checkPkcs1Encoding2(byArray3) : PKCS1Encoding.checkPkcs1Encoding1(byArray3);
        try {
            if (n5 < 0) {
                throw new InvalidCipherTextException("block incorrect");
            }
            if (bl) {
                throw new InvalidCipherTextException("block incorrect size");
            }
            byte[] byArray4 = new byte[n5];
            System.arraycopy(byArray3, byArray3.length - n5, byArray4, 0, n5);
            byte[] byArray5 = byArray4;
            return byArray5;
        }
        finally {
            Arrays.fill(byArray2, (byte)0);
            Arrays.fill(this.blockBuffer, 0, Math.max(0, this.blockBuffer.length - byArray2.length), (byte)0);
        }
    }
}

