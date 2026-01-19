/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.signers;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.SignerWithRecovery;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.signers.ISOTrailers;
import org.bouncycastle.util.Arrays;

public class ISO9796d2Signer
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
    private int trailer;
    private int keyBits;
    private byte[] block;
    private byte[] mBuf;
    private int messageLength;
    private boolean fullMessage;
    private byte[] recoveredMessage;
    private byte[] preSig;
    private byte[] preBlock;

    public ISO9796d2Signer(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, boolean bl) {
        this.cipher = asymmetricBlockCipher;
        this.digest = digest;
        if (bl) {
            this.trailer = 188;
        } else {
            Integer n2 = ISOTrailers.getTrailer(digest);
            if (n2 != null) {
                this.trailer = n2;
            } else {
                throw new IllegalArgumentException("no valid trailer for digest: " + digest.getAlgorithmName());
            }
        }
    }

    public ISO9796d2Signer(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest) {
        this(asymmetricBlockCipher, digest, false);
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        RSAKeyParameters rSAKeyParameters = (RSAKeyParameters)cipherParameters;
        this.cipher.init(bl, rSAKeyParameters);
        this.keyBits = rSAKeyParameters.getModulus().bitLength();
        this.block = new byte[(this.keyBits + 7) / 8];
        this.mBuf = this.trailer == 188 ? new byte[this.block.length - this.digest.getDigestSize() - 2] : new byte[this.block.length - this.digest.getDigestSize() - 3];
        this.reset();
    }

    private boolean isSameAs(byte[] byArray, byte[] byArray2) {
        boolean bl = true;
        if (this.messageLength > this.mBuf.length) {
            if (this.mBuf.length > byArray2.length) {
                bl = false;
            }
            for (int i2 = 0; i2 != this.mBuf.length; ++i2) {
                if (byArray[i2] == byArray2[i2]) continue;
                bl = false;
            }
        } else {
            if (this.messageLength != byArray2.length) {
                bl = false;
            }
            for (int i3 = 0; i3 != byArray2.length; ++i3) {
                if (byArray[i3] == byArray2[i3]) continue;
                bl = false;
            }
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
        byte[] byArray2 = this.cipher.processBlock(byArray, 0, byArray.length);
        if ((byArray2[0] & 0xC0 ^ 0x40) != 0) {
            throw new InvalidCipherTextException("malformed signature");
        }
        if ((byArray2[byArray2.length - 1] & 0xF ^ 0xC) != 0) {
            throw new InvalidCipherTextException("malformed signature");
        }
        int n3 = 0;
        if ((byArray2[byArray2.length - 1] & 0xFF ^ 0xBC) == 0) {
            n3 = 1;
        } else {
            n2 = (byArray2[byArray2.length - 2] & 0xFF) << 8 | byArray2[byArray2.length - 1] & 0xFF;
            Integer n4 = ISOTrailers.getTrailer(this.digest);
            if (n4 != null) {
                int n5 = n4;
                if (n2 != n5 && (n5 != 15052 || n2 != 16588)) {
                    throw new IllegalStateException("signer initialised with wrong digest for trailer " + n2);
                }
            } else {
                throw new IllegalArgumentException("unrecognised hash in signature");
            }
            n3 = 2;
        }
        n2 = 0;
        for (n2 = 0; n2 != byArray2.length && (byArray2[n2] & 0xF ^ 0xA) != 0; ++n2) {
        }
        int n6 = byArray2.length - n3 - this.digest.getDigestSize();
        if (n6 - ++n2 <= 0) {
            throw new InvalidCipherTextException("malformed block");
        }
        if ((byArray2[0] & 0x20) == 0) {
            this.fullMessage = true;
            this.recoveredMessage = new byte[n6 - n2];
            System.arraycopy(byArray2, n2, this.recoveredMessage, 0, this.recoveredMessage.length);
        } else {
            this.fullMessage = false;
            this.recoveredMessage = new byte[n6 - n2];
            System.arraycopy(byArray2, n2, this.recoveredMessage, 0, this.recoveredMessage.length);
        }
        this.preSig = byArray;
        this.preBlock = byArray2;
        this.digest.update(this.recoveredMessage, 0, this.recoveredMessage.length);
        this.messageLength = this.recoveredMessage.length;
        System.arraycopy(this.recoveredMessage, 0, this.mBuf, 0, this.recoveredMessage.length);
    }

    @Override
    public void update(byte by) {
        this.digest.update(by);
        if (this.messageLength < this.mBuf.length) {
            this.mBuf[this.messageLength] = by;
        }
        ++this.messageLength;
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) {
        while (n3 > 0 && this.messageLength < this.mBuf.length) {
            this.update(byArray[n2]);
            ++n2;
            --n3;
        }
        this.digest.update(byArray, n2, n3);
        this.messageLength += n3;
    }

    @Override
    public void reset() {
        this.digest.reset();
        this.messageLength = 0;
        this.clearBlock(this.mBuf);
        if (this.recoveredMessage != null) {
            this.clearBlock(this.recoveredMessage);
        }
        this.recoveredMessage = null;
        this.fullMessage = false;
        if (this.preSig != null) {
            this.preSig = null;
            this.clearBlock(this.preBlock);
            this.preBlock = null;
        }
    }

    @Override
    public byte[] generateSignature() throws CryptoException {
        int n2;
        int n3 = this.digest.getDigestSize();
        int n4 = 0;
        int n5 = 0;
        if (this.trailer == 188) {
            n4 = 8;
            n5 = this.block.length - n3 - 1;
            this.digest.doFinal(this.block, n5);
            this.block[this.block.length - 1] = -68;
        } else {
            n4 = 16;
            n5 = this.block.length - n3 - 2;
            this.digest.doFinal(this.block, n5);
            this.block[this.block.length - 2] = (byte)(this.trailer >>> 8);
            this.block[this.block.length - 1] = (byte)this.trailer;
        }
        int n6 = 0;
        int n7 = (n3 + this.messageLength) * 8 + n4 + 4 - this.keyBits;
        if (n7 > 0) {
            n2 = this.messageLength - (n7 + 7) / 8;
            n6 = 96;
            System.arraycopy(this.mBuf, 0, this.block, n5 -= n2, n2);
            this.recoveredMessage = new byte[n2];
        } else {
            n6 = 64;
            System.arraycopy(this.mBuf, 0, this.block, n5 -= this.messageLength, this.messageLength);
            this.recoveredMessage = new byte[this.messageLength];
        }
        if (n5 - 1 > 0) {
            for (n2 = n5 - 1; n2 != 0; --n2) {
                this.block[n2] = -69;
            }
            int n8 = n5 - 1;
            this.block[n8] = (byte)(this.block[n8] ^ 1);
            this.block[0] = 11;
            this.block[0] = (byte)(this.block[0] | n6);
        } else {
            this.block[0] = 10;
            this.block[0] = (byte)(this.block[0] | n6);
        }
        byte[] byArray = this.cipher.processBlock(this.block, 0, this.block.length);
        this.fullMessage = (n6 & 0x20) == 0;
        System.arraycopy(this.mBuf, 0, this.recoveredMessage, 0, this.recoveredMessage.length);
        this.messageLength = 0;
        this.clearBlock(this.mBuf);
        this.clearBlock(this.block);
        return byArray;
    }

    @Override
    public boolean verifySignature(byte[] byArray) {
        int n2;
        Object object;
        int n3;
        byte[] byArray2 = null;
        if (this.preSig == null) {
            try {
                byArray2 = this.cipher.processBlock(byArray, 0, byArray.length);
            }
            catch (Exception exception) {
                return false;
            }
        } else {
            if (!Arrays.areEqual(this.preSig, byArray)) {
                throw new IllegalStateException("updateWithRecoveredMessage called on different signature");
            }
            byArray2 = this.preBlock;
            this.preSig = null;
            this.preBlock = null;
        }
        if ((byArray2[0] & 0xC0 ^ 0x40) != 0) {
            return this.returnFalse(byArray2);
        }
        if ((byArray2[byArray2.length - 1] & 0xF ^ 0xC) != 0) {
            return this.returnFalse(byArray2);
        }
        int n4 = 0;
        if ((byArray2[byArray2.length - 1] & 0xFF ^ 0xBC) == 0) {
            n4 = 1;
        } else {
            n3 = (byArray2[byArray2.length - 2] & 0xFF) << 8 | byArray2[byArray2.length - 1] & 0xFF;
            object = ISOTrailers.getTrailer(this.digest);
            if (object != null) {
                n2 = object.intValue();
                if (n3 != n2 && (n2 != 15052 || n3 != 16588)) {
                    throw new IllegalStateException("signer initialised with wrong digest for trailer " + n3);
                }
            } else {
                throw new IllegalArgumentException("unrecognised hash in signature");
            }
            n4 = 2;
        }
        n3 = 0;
        for (n3 = 0; n3 != byArray2.length && (byArray2[n3] & 0xF ^ 0xA) != 0; ++n3) {
        }
        object = new byte[this.digest.getDigestSize()];
        n2 = byArray2.length - n4 - ((byte[])object).length;
        if (n2 - ++n3 <= 0) {
            return this.returnFalse(byArray2);
        }
        if ((byArray2[0] & 0x20) == 0) {
            this.fullMessage = true;
            if (this.messageLength > n2 - n3) {
                return this.returnFalse(byArray2);
            }
            this.digest.reset();
            this.digest.update(byArray2, n3, n2 - n3);
            this.digest.doFinal((byte[])object, 0);
            boolean bl = true;
            for (int i2 = 0; i2 != ((Object)object).length; ++i2) {
                int n5 = n2 + i2;
                byArray2[n5] = (byte)(byArray2[n5] ^ object[i2]);
                if (byArray2[n2 + i2] == 0) continue;
                bl = false;
            }
            if (!bl) {
                return this.returnFalse(byArray2);
            }
            this.recoveredMessage = new byte[n2 - n3];
            System.arraycopy(byArray2, n3, this.recoveredMessage, 0, this.recoveredMessage.length);
        } else {
            this.fullMessage = false;
            this.digest.doFinal((byte[])object, 0);
            boolean bl = true;
            for (int i3 = 0; i3 != ((Object)object).length; ++i3) {
                int n6 = n2 + i3;
                byArray2[n6] = (byte)(byArray2[n6] ^ object[i3]);
                if (byArray2[n2 + i3] == 0) continue;
                bl = false;
            }
            if (!bl) {
                return this.returnFalse(byArray2);
            }
            this.recoveredMessage = new byte[n2 - n3];
            System.arraycopy(byArray2, n3, this.recoveredMessage, 0, this.recoveredMessage.length);
        }
        if (this.messageLength != 0 && !this.isSameAs(this.mBuf, this.recoveredMessage)) {
            return this.returnFalse(byArray2);
        }
        this.clearBlock(this.mBuf);
        this.clearBlock(byArray2);
        this.messageLength = 0;
        return true;
    }

    private boolean returnFalse(byte[] byArray) {
        this.messageLength = 0;
        this.clearBlock(this.mBuf);
        this.clearBlock(byArray);
        return false;
    }

    @Override
    public boolean hasFullMessage() {
        return this.fullMessage;
    }

    @Override
    public byte[] getRecoveredMessage() {
        return this.recoveredMessage;
    }
}

