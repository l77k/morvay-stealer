/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.signers;

import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.Xof;
import org.bouncycastle.crypto.digests.Prehash;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.RSABlindingParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.util.Arrays;

public class PSSSigner
implements Signer {
    public static final byte TRAILER_IMPLICIT = -68;
    private Digest contentDigest1;
    private Digest contentDigest2;
    private Digest mgfDigest;
    private AsymmetricBlockCipher cipher;
    private SecureRandom random;
    private int hLen;
    private int mgfhLen;
    private boolean sSet;
    private int sLen;
    private int emBits;
    private byte[] salt;
    private byte[] mDash;
    private byte[] block;
    private byte trailer;

    public static PSSSigner createRawSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest) {
        return new PSSSigner(asymmetricBlockCipher, (Digest)Prehash.forDigest(digest), digest, digest, digest.getDigestSize(), -68);
    }

    public static PSSSigner createRawSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, Digest digest2, int n2, byte by) {
        return new PSSSigner(asymmetricBlockCipher, (Digest)Prehash.forDigest(digest), digest, digest2, n2, by);
    }

    public static PSSSigner createRawSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, Digest digest2, byte[] byArray, byte by) {
        return new PSSSigner(asymmetricBlockCipher, (Digest)Prehash.forDigest(digest), digest, digest2, byArray, by);
    }

    public PSSSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, int n2) {
        this(asymmetricBlockCipher, digest, n2, -68);
    }

    public PSSSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, Digest digest2, int n2) {
        this(asymmetricBlockCipher, digest, digest2, n2, -68);
    }

    public PSSSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, int n2, byte by) {
        this(asymmetricBlockCipher, digest, digest, n2, by);
    }

    public PSSSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, Digest digest2, int n2, byte by) {
        this(asymmetricBlockCipher, digest, digest, digest2, n2, by);
    }

    private PSSSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, Digest digest2, Digest digest3, int n2, byte by) {
        this.cipher = asymmetricBlockCipher;
        this.contentDigest1 = digest;
        this.contentDigest2 = digest2;
        this.mgfDigest = digest3;
        this.hLen = digest2.getDigestSize();
        this.mgfhLen = digest3.getDigestSize();
        this.sSet = false;
        this.sLen = n2;
        this.salt = new byte[n2];
        this.mDash = new byte[8 + n2 + this.hLen];
        this.trailer = by;
    }

    public PSSSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, byte[] byArray) {
        this(asymmetricBlockCipher, digest, digest, byArray, -68);
    }

    public PSSSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, Digest digest2, byte[] byArray) {
        this(asymmetricBlockCipher, digest, digest2, byArray, -68);
    }

    public PSSSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, Digest digest2, byte[] byArray, byte by) {
        this(asymmetricBlockCipher, digest, digest, digest2, byArray, by);
    }

    private PSSSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, Digest digest2, Digest digest3, byte[] byArray, byte by) {
        this.cipher = asymmetricBlockCipher;
        this.contentDigest1 = digest;
        this.contentDigest2 = digest2;
        this.mgfDigest = digest3;
        this.hLen = digest2.getDigestSize();
        this.mgfhLen = digest3.getDigestSize();
        this.sSet = true;
        this.sLen = byArray.length;
        this.salt = byArray;
        this.mDash = new byte[8 + this.sLen + this.hLen];
        this.trailer = by;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        CipherParameters cipherParameters2;
        CipherParameters cipherParameters3;
        if (cipherParameters instanceof ParametersWithRandom) {
            cipherParameters3 = (ParametersWithRandom)cipherParameters;
            cipherParameters2 = ((ParametersWithRandom)cipherParameters3).getParameters();
            this.random = ((ParametersWithRandom)cipherParameters3).getRandom();
        } else {
            cipherParameters2 = cipherParameters;
            if (bl) {
                this.random = CryptoServicesRegistrar.getSecureRandom();
            }
        }
        if (cipherParameters2 instanceof RSABlindingParameters) {
            cipherParameters3 = ((RSABlindingParameters)cipherParameters2).getPublicKey();
            this.cipher.init(bl, cipherParameters);
        } else {
            cipherParameters3 = (RSAKeyParameters)cipherParameters2;
            this.cipher.init(bl, cipherParameters2);
        }
        this.emBits = ((RSAKeyParameters)cipherParameters3).getModulus().bitLength() - 1;
        if (this.emBits < 8 * this.hLen + 8 * this.sLen + 9) {
            throw new IllegalArgumentException("key too small for specified hash and salt lengths");
        }
        this.block = new byte[(this.emBits + 7) / 8];
        this.reset();
    }

    private void clearBlock(byte[] byArray) {
        for (int i2 = 0; i2 != byArray.length; ++i2) {
            byArray[i2] = 0;
        }
    }

    @Override
    public void update(byte by) {
        this.contentDigest1.update(by);
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) {
        this.contentDigest1.update(byArray, n2, n3);
    }

    @Override
    public void reset() {
        this.contentDigest1.reset();
    }

    @Override
    public byte[] generateSignature() throws CryptoException, DataLengthException {
        int n2;
        if (this.contentDigest1.getDigestSize() != this.hLen) {
            throw new IllegalStateException();
        }
        this.contentDigest1.doFinal(this.mDash, this.mDash.length - this.hLen - this.sLen);
        if (this.sLen != 0) {
            if (!this.sSet) {
                this.random.nextBytes(this.salt);
            }
            System.arraycopy(this.salt, 0, this.mDash, this.mDash.length - this.sLen, this.sLen);
        }
        byte[] byArray = new byte[this.hLen];
        this.contentDigest2.update(this.mDash, 0, this.mDash.length);
        this.contentDigest2.doFinal(byArray, 0);
        this.block[this.block.length - this.sLen - 1 - this.hLen - 1] = 1;
        System.arraycopy(this.salt, 0, this.block, this.block.length - this.sLen - this.hLen - 1, this.sLen);
        byte[] byArray2 = this.maskGenerator(byArray, 0, byArray.length, this.block.length - this.hLen - 1);
        for (n2 = 0; n2 != byArray2.length; ++n2) {
            int n3 = n2;
            this.block[n3] = (byte)(this.block[n3] ^ byArray2[n2]);
        }
        System.arraycopy(byArray, 0, this.block, this.block.length - this.hLen - 1, this.hLen);
        n2 = 255 >>> this.block.length * 8 - this.emBits;
        this.block[0] = (byte)(this.block[0] & n2);
        this.block[this.block.length - 1] = this.trailer;
        byte[] byArray3 = this.cipher.processBlock(this.block, 0, this.block.length);
        this.clearBlock(this.block);
        return byArray3;
    }

    @Override
    public boolean verifySignature(byte[] byArray) {
        int n2;
        if (this.contentDigest1.getDigestSize() != this.hLen) {
            throw new IllegalStateException();
        }
        this.contentDigest1.doFinal(this.mDash, this.mDash.length - this.hLen - this.sLen);
        try {
            byte[] byArray2 = this.cipher.processBlock(byArray, 0, byArray.length);
            Arrays.fill(this.block, 0, this.block.length - byArray2.length, (byte)0);
            System.arraycopy(byArray2, 0, this.block, this.block.length - byArray2.length, byArray2.length);
        }
        catch (Exception exception) {
            return false;
        }
        int n3 = 255 >>> this.block.length * 8 - this.emBits;
        if ((this.block[0] & 0xFF) != (this.block[0] & n3) || this.block[this.block.length - 1] != this.trailer) {
            this.clearBlock(this.block);
            return false;
        }
        byte[] byArray3 = this.maskGenerator(this.block, this.block.length - this.hLen - 1, this.hLen, this.block.length - this.hLen - 1);
        for (n2 = 0; n2 != byArray3.length; ++n2) {
            int n4 = n2;
            this.block[n4] = (byte)(this.block[n4] ^ byArray3[n2]);
        }
        this.block[0] = (byte)(this.block[0] & n3);
        for (n2 = 0; n2 != this.block.length - this.hLen - this.sLen - 2; ++n2) {
            if (this.block[n2] == 0) continue;
            this.clearBlock(this.block);
            return false;
        }
        if (this.block[this.block.length - this.hLen - this.sLen - 2] != 1) {
            this.clearBlock(this.block);
            return false;
        }
        if (this.sSet) {
            System.arraycopy(this.salt, 0, this.mDash, this.mDash.length - this.sLen, this.sLen);
        } else {
            System.arraycopy(this.block, this.block.length - this.sLen - this.hLen - 1, this.mDash, this.mDash.length - this.sLen, this.sLen);
        }
        this.contentDigest2.update(this.mDash, 0, this.mDash.length);
        this.contentDigest2.doFinal(this.mDash, this.mDash.length - this.hLen);
        n2 = this.block.length - this.hLen - 1;
        for (int i2 = this.mDash.length - this.hLen; i2 != this.mDash.length; ++i2) {
            if ((this.block[n2] ^ this.mDash[i2]) != 0) {
                this.clearBlock(this.mDash);
                this.clearBlock(this.block);
                return false;
            }
            ++n2;
        }
        this.clearBlock(this.mDash);
        this.clearBlock(this.block);
        return true;
    }

    private void ItoOSP(int n2, byte[] byArray) {
        byArray[0] = (byte)(n2 >>> 24);
        byArray[1] = (byte)(n2 >>> 16);
        byArray[2] = (byte)(n2 >>> 8);
        byArray[3] = (byte)(n2 >>> 0);
    }

    private byte[] maskGenerator(byte[] byArray, int n2, int n3, int n4) {
        if (this.mgfDigest instanceof Xof) {
            byte[] byArray2 = new byte[n4];
            this.mgfDigest.update(byArray, n2, n3);
            ((Xof)this.mgfDigest).doFinal(byArray2, 0, byArray2.length);
            return byArray2;
        }
        return this.maskGeneratorFunction1(byArray, n2, n3, n4);
    }

    private byte[] maskGeneratorFunction1(byte[] byArray, int n2, int n3, int n4) {
        int n5;
        byte[] byArray2 = new byte[n4];
        byte[] byArray3 = new byte[this.mgfhLen];
        byte[] byArray4 = new byte[4];
        this.mgfDigest.reset();
        for (n5 = 0; n5 < n4 / this.mgfhLen; ++n5) {
            this.ItoOSP(n5, byArray4);
            this.mgfDigest.update(byArray, n2, n3);
            this.mgfDigest.update(byArray4, 0, byArray4.length);
            this.mgfDigest.doFinal(byArray3, 0);
            System.arraycopy(byArray3, 0, byArray2, n5 * this.mgfhLen, this.mgfhLen);
        }
        if (n5 * this.mgfhLen < n4) {
            this.ItoOSP(n5, byArray4);
            this.mgfDigest.update(byArray, n2, n3);
            this.mgfDigest.update(byArray4, 0, byArray4.length);
            this.mgfDigest.doFinal(byArray3, 0);
            System.arraycopy(byArray3, 0, byArray2, n5 * this.mgfhLen, byArray2.length - n5 * this.mgfhLen);
        }
        return byArray2;
    }
}

