/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.signers;

import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.SignerWithRecovery;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.ParametersWithSalt;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.signers.ISOTrailers;
import org.bouncycastle.util.Arrays;

public class ISO9796d2PSSSigner
implements SignerWithRecovery {
    public static final int TRAILER_IMPLICIT = 188;
    public static final int TRAILER_RIPEMD160 = 12748;
    public static final int TRAILER_RIPEMD128 = 13004;
    public static final int TRAILER_SHA1 = 13260;
    public static final int TRAILER_SHA256 = 13516;
    public static final int TRAILER_SHA512 = 13772;
    public static final int TRAILER_SHA384 = 14028;
    public static final int TRAILER_WHIRLPOOL = 14284;
    private Digest digest;
    private AsymmetricBlockCipher cipher;
    private SecureRandom random;
    private byte[] standardSalt;
    private int hLen;
    private int trailer;
    private int keyBits;
    private byte[] block;
    private byte[] mBuf;
    private int messageLength;
    private int saltLength;
    private boolean fullMessage;
    private byte[] recoveredMessage;
    private byte[] preSig;
    private byte[] preBlock;
    private int preMStart;
    private int preTLength;

    public ISO9796d2PSSSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, int n2, boolean bl) {
        this.cipher = asymmetricBlockCipher;
        this.digest = digest;
        this.hLen = digest.getDigestSize();
        this.saltLength = n2;
        if (bl) {
            this.trailer = 188;
        } else {
            Integer n3 = ISOTrailers.getTrailer(digest);
            if (n3 != null) {
                this.trailer = n3;
            } else {
                throw new IllegalArgumentException("no valid trailer for digest: " + digest.getAlgorithmName());
            }
        }
    }

    public ISO9796d2PSSSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, int n2) {
        this(asymmetricBlockCipher, digest, n2, false);
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        RSAKeyParameters rSAKeyParameters;
        int n2 = this.saltLength;
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            rSAKeyParameters = (RSAKeyParameters)parametersWithRandom.getParameters();
            if (bl) {
                this.random = parametersWithRandom.getRandom();
            }
        } else if (cipherParameters instanceof ParametersWithSalt) {
            ParametersWithSalt parametersWithSalt = (ParametersWithSalt)cipherParameters;
            rSAKeyParameters = (RSAKeyParameters)parametersWithSalt.getParameters();
            this.standardSalt = parametersWithSalt.getSalt();
            n2 = this.standardSalt.length;
            if (this.standardSalt.length != this.saltLength) {
                throw new IllegalArgumentException("Fixed salt is of wrong length");
            }
        } else {
            rSAKeyParameters = (RSAKeyParameters)cipherParameters;
            if (bl) {
                this.random = CryptoServicesRegistrar.getSecureRandom();
            }
        }
        this.cipher.init(bl, rSAKeyParameters);
        this.keyBits = rSAKeyParameters.getModulus().bitLength();
        this.block = new byte[(this.keyBits + 7) / 8];
        this.mBuf = this.trailer == 188 ? new byte[this.block.length - this.digest.getDigestSize() - n2 - 1 - 1] : new byte[this.block.length - this.digest.getDigestSize() - n2 - 1 - 2];
        this.reset();
    }

    private boolean isSameAs(byte[] byArray, byte[] byArray2) {
        boolean bl = true;
        if (this.messageLength != byArray2.length) {
            bl = false;
        }
        for (int i2 = 0; i2 != byArray2.length; ++i2) {
            if (byArray[i2] == byArray2[i2]) continue;
            bl = false;
        }
        return bl;
    }

    private void clearBlock(byte[] byArray) {
        for (int i2 = 0; i2 != byArray.length; ++i2) {
            byArray[i2] = 0;
        }
    }

    @Override
    public void updateWithRecoveredMessage(byte[] byArray) throws InvalidCipherTextException {
        int n2;
        Object object;
        int n3;
        byte[] byArray2 = this.cipher.processBlock(byArray, 0, byArray.length);
        if (byArray2.length < (this.keyBits + 7) / 8) {
            byte[] byArray3 = new byte[(this.keyBits + 7) / 8];
            System.arraycopy(byArray2, 0, byArray3, byArray3.length - byArray2.length, byArray2.length);
            this.clearBlock(byArray2);
            byArray2 = byArray3;
        }
        if ((byArray2[byArray2.length - 1] & 0xFF ^ 0xBC) == 0) {
            n3 = 1;
        } else {
            int n4 = (byArray2[byArray2.length - 2] & 0xFF) << 8 | byArray2[byArray2.length - 1] & 0xFF;
            object = ISOTrailers.getTrailer(this.digest);
            if (object != null) {
                n2 = (Integer)object;
                if (n4 != n2 && (n2 != 15052 || n4 != 16588)) {
                    throw new IllegalStateException("signer initialised with wrong digest for trailer " + n4);
                }
            } else {
                throw new IllegalArgumentException("unrecognised hash in signature");
            }
            n3 = 2;
        }
        byte[] byArray4 = new byte[this.hLen];
        this.digest.doFinal(byArray4, 0);
        object = this.maskGeneratorFunction1(byArray2, byArray2.length - this.hLen - n3, this.hLen, byArray2.length - this.hLen - n3);
        for (n2 = 0; n2 != ((Object)object).length; ++n2) {
            int n5 = n2;
            byArray2[n5] = (byte)(byArray2[n5] ^ object[n2]);
        }
        byArray2[0] = (byte)(byArray2[0] & 0x7F);
        for (n2 = 0; n2 != byArray2.length && byArray2[n2] != 1; ++n2) {
        }
        if (++n2 >= byArray2.length) {
            this.clearBlock(byArray2);
        }
        this.fullMessage = n2 > 1;
        this.recoveredMessage = new byte[((Object)object).length - n2 - this.saltLength];
        System.arraycopy(byArray2, n2, this.recoveredMessage, 0, this.recoveredMessage.length);
        System.arraycopy(this.recoveredMessage, 0, this.mBuf, 0, this.recoveredMessage.length);
        this.preSig = byArray;
        this.preBlock = byArray2;
        this.preMStart = n2;
        this.preTLength = n3;
    }

    @Override
    public void update(byte by) {
        if (this.preSig == null && this.messageLength < this.mBuf.length) {
            this.mBuf[this.messageLength++] = by;
        } else {
            this.digest.update(by);
        }
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) {
        if (this.preSig == null) {
            while (n3 > 0 && this.messageLength < this.mBuf.length) {
                this.update(byArray[n2]);
                ++n2;
                --n3;
            }
        }
        if (n3 > 0) {
            this.digest.update(byArray, n2, n3);
        }
    }

    @Override
    public void reset() {
        this.digest.reset();
        this.messageLength = 0;
        if (this.mBuf != null) {
            this.clearBlock(this.mBuf);
        }
        if (this.recoveredMessage != null) {
            this.clearBlock(this.recoveredMessage);
            this.recoveredMessage = null;
        }
        this.fullMessage = false;
        if (this.preSig != null) {
            this.preSig = null;
            this.clearBlock(this.preBlock);
            this.preBlock = null;
        }
    }

    @Override
    public byte[] generateSignature() throws CryptoException {
        byte[] byArray;
        int n2 = this.digest.getDigestSize();
        byte[] byArray2 = new byte[n2];
        this.digest.doFinal(byArray2, 0);
        byte[] byArray3 = new byte[8];
        this.LtoOSP(this.messageLength * 8, byArray3);
        this.digest.update(byArray3, 0, byArray3.length);
        this.digest.update(this.mBuf, 0, this.messageLength);
        this.digest.update(byArray2, 0, byArray2.length);
        if (this.standardSalt != null) {
            byArray = this.standardSalt;
        } else {
            byArray = new byte[this.saltLength];
            this.random.nextBytes(byArray);
        }
        this.digest.update(byArray, 0, byArray.length);
        byte[] byArray4 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(byArray4, 0);
        int n3 = 2;
        if (this.trailer == 188) {
            n3 = 1;
        }
        int n4 = this.block.length - this.messageLength - byArray.length - this.hLen - n3 - 1;
        this.block[n4] = 1;
        System.arraycopy(this.mBuf, 0, this.block, n4 + 1, this.messageLength);
        System.arraycopy(byArray, 0, this.block, n4 + 1 + this.messageLength, byArray.length);
        byte[] byArray5 = this.maskGeneratorFunction1(byArray4, 0, byArray4.length, this.block.length - this.hLen - n3);
        for (int i2 = 0; i2 != byArray5.length; ++i2) {
            int n5 = i2;
            this.block[n5] = (byte)(this.block[n5] ^ byArray5[i2]);
        }
        System.arraycopy(byArray4, 0, this.block, this.block.length - this.hLen - n3, this.hLen);
        if (this.trailer == 188) {
            this.block[this.block.length - 1] = -68;
        } else {
            this.block[this.block.length - 2] = (byte)(this.trailer >>> 8);
            this.block[this.block.length - 1] = (byte)this.trailer;
        }
        this.block[0] = (byte)(this.block[0] & 0x7F);
        byte[] byArray6 = this.cipher.processBlock(this.block, 0, this.block.length);
        this.recoveredMessage = new byte[this.messageLength];
        this.fullMessage = this.messageLength <= this.mBuf.length;
        System.arraycopy(this.mBuf, 0, this.recoveredMessage, 0, this.recoveredMessage.length);
        this.clearBlock(this.mBuf);
        this.clearBlock(this.block);
        this.messageLength = 0;
        return byArray6;
    }

    @Override
    public boolean verifySignature(byte[] byArray) {
        byte[] byArray2 = new byte[this.hLen];
        this.digest.doFinal(byArray2, 0);
        int n2 = 0;
        if (this.preSig == null) {
            try {
                this.updateWithRecoveredMessage(byArray);
            }
            catch (Exception exception) {
                return false;
            }
        } else if (!Arrays.areEqual(this.preSig, byArray)) {
            throw new IllegalStateException("updateWithRecoveredMessage called on different signature");
        }
        byte[] byArray3 = this.preBlock;
        n2 = this.preMStart;
        int n3 = this.preTLength;
        this.preSig = null;
        this.preBlock = null;
        byte[] byArray4 = new byte[8];
        this.LtoOSP(this.recoveredMessage.length * 8, byArray4);
        this.digest.update(byArray4, 0, byArray4.length);
        if (this.recoveredMessage.length != 0) {
            this.digest.update(this.recoveredMessage, 0, this.recoveredMessage.length);
        }
        this.digest.update(byArray2, 0, byArray2.length);
        if (this.standardSalt != null) {
            this.digest.update(this.standardSalt, 0, this.standardSalt.length);
        } else {
            this.digest.update(byArray3, n2 + this.recoveredMessage.length, this.saltLength);
        }
        byte[] byArray5 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(byArray5, 0);
        int n4 = byArray3.length - n3 - byArray5.length;
        boolean bl = true;
        for (int i2 = 0; i2 != byArray5.length; ++i2) {
            if (byArray5[i2] == byArray3[n4 + i2]) continue;
            bl = false;
        }
        this.clearBlock(byArray3);
        this.clearBlock(byArray5);
        if (!bl) {
            this.fullMessage = false;
            this.messageLength = 0;
            this.clearBlock(this.recoveredMessage);
            return false;
        }
        if (this.messageLength != 0 && !this.isSameAs(this.mBuf, this.recoveredMessage)) {
            this.messageLength = 0;
            this.clearBlock(this.mBuf);
            return false;
        }
        this.messageLength = 0;
        this.clearBlock(this.mBuf);
        return true;
    }

    @Override
    public boolean hasFullMessage() {
        return this.fullMessage;
    }

    @Override
    public byte[] getRecoveredMessage() {
        return this.recoveredMessage;
    }

    private void ItoOSP(int n2, byte[] byArray) {
        byArray[0] = (byte)(n2 >>> 24);
        byArray[1] = (byte)(n2 >>> 16);
        byArray[2] = (byte)(n2 >>> 8);
        byArray[3] = (byte)(n2 >>> 0);
    }

    private void LtoOSP(long l2, byte[] byArray) {
        byArray[0] = (byte)(l2 >>> 56);
        byArray[1] = (byte)(l2 >>> 48);
        byArray[2] = (byte)(l2 >>> 40);
        byArray[3] = (byte)(l2 >>> 32);
        byArray[4] = (byte)(l2 >>> 24);
        byArray[5] = (byte)(l2 >>> 16);
        byArray[6] = (byte)(l2 >>> 8);
        byArray[7] = (byte)(l2 >>> 0);
    }

    private byte[] maskGeneratorFunction1(byte[] byArray, int n2, int n3, int n4) {
        int n5;
        byte[] byArray2 = new byte[n4];
        byte[] byArray3 = new byte[this.hLen];
        byte[] byArray4 = new byte[4];
        this.digest.reset();
        for (n5 = 0; n5 < n4 / this.hLen; ++n5) {
            this.ItoOSP(n5, byArray4);
            this.digest.update(byArray, n2, n3);
            this.digest.update(byArray4, 0, byArray4.length);
            this.digest.doFinal(byArray3, 0);
            System.arraycopy(byArray3, 0, byArray2, n5 * this.hLen, this.hLen);
        }
        if (n5 * this.hLen < n4) {
            this.ItoOSP(n5, byArray4);
            this.digest.update(byArray, n2, n3);
            this.digest.update(byArray4, 0, byArray4.length);
            this.digest.doFinal(byArray3, 0);
            System.arraycopy(byArray3, 0, byArray2, n5 * this.hLen, byArray2.length - n5 * this.hLen);
        }
        return byArray2;
    }
}

