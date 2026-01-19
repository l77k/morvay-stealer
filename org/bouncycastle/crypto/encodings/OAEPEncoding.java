/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.encodings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.util.DigestFactory;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Bytes;
import org.bouncycastle.util.Memoable;
import org.bouncycastle.util.Pack;

public class OAEPEncoding
implements AsymmetricBlockCipher {
    private final AsymmetricBlockCipher engine;
    private final Digest mgf1Hash;
    private final int mgf1NoMemoLimit;
    private final byte[] defHash;
    private SecureRandom random;
    private boolean forEncryption;

    private static int getMGF1NoMemoLimit(Digest digest) {
        if (digest instanceof Memoable && digest instanceof ExtendedDigest) {
            return ((ExtendedDigest)digest).getByteLength() - 1;
        }
        return Integer.MAX_VALUE;
    }

    public OAEPEncoding(AsymmetricBlockCipher asymmetricBlockCipher) {
        this(asymmetricBlockCipher, DigestFactory.createSHA1(), null);
    }

    public OAEPEncoding(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest) {
        this(asymmetricBlockCipher, digest, null);
    }

    public OAEPEncoding(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, byte[] byArray) {
        this(asymmetricBlockCipher, digest, digest, byArray);
    }

    public OAEPEncoding(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, Digest digest2, byte[] byArray) {
        this.engine = asymmetricBlockCipher;
        this.mgf1Hash = digest2;
        this.mgf1NoMemoLimit = OAEPEncoding.getMGF1NoMemoLimit(digest2);
        this.defHash = new byte[digest.getDigestSize()];
        digest.reset();
        if (byArray != null) {
            digest.update(byArray, 0, byArray.length);
        }
        digest.doFinal(this.defHash, 0);
    }

    public AsymmetricBlockCipher getUnderlyingCipher() {
        return this.engine;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        SecureRandom secureRandom = null;
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            secureRandom = parametersWithRandom.getRandom();
        }
        this.random = bl ? CryptoServicesRegistrar.getSecureRandom(secureRandom) : null;
        this.forEncryption = bl;
        this.engine.init(bl, cipherParameters);
    }

    @Override
    public int getInputBlockSize() {
        int n2 = this.engine.getInputBlockSize();
        if (this.forEncryption) {
            return n2 - 1 - 2 * this.defHash.length;
        }
        return n2;
    }

    @Override
    public int getOutputBlockSize() {
        int n2 = this.engine.getOutputBlockSize();
        if (this.forEncryption) {
            return n2;
        }
        return n2 - 1 - 2 * this.defHash.length;
    }

    @Override
    public byte[] processBlock(byte[] byArray, int n2, int n3) throws InvalidCipherTextException {
        if (this.forEncryption) {
            return this.encodeBlock(byArray, n2, n3);
        }
        return this.decodeBlock(byArray, n2, n3);
    }

    public byte[] encodeBlock(byte[] byArray, int n2, int n3) throws InvalidCipherTextException {
        int n4 = this.getInputBlockSize();
        if (n3 > n4) {
            throw new DataLengthException("input data too long");
        }
        byte[] byArray2 = new byte[n4 + 1 + 2 * this.defHash.length];
        System.arraycopy(byArray, n2, byArray2, byArray2.length - n3, n3);
        byArray2[byArray2.length - n3 - 1] = 1;
        System.arraycopy(this.defHash, 0, byArray2, this.defHash.length, this.defHash.length);
        byte[] byArray3 = new byte[this.defHash.length];
        this.random.nextBytes(byArray3);
        System.arraycopy(byArray3, 0, byArray2, 0, this.defHash.length);
        this.mgf1Hash.reset();
        this.maskGeneratorFunction1(byArray3, 0, byArray3.length, byArray2, this.defHash.length, byArray2.length - this.defHash.length);
        this.maskGeneratorFunction1(byArray2, this.defHash.length, byArray2.length - this.defHash.length, byArray2, 0, this.defHash.length);
        return this.engine.processBlock(byArray2, 0, byArray2.length);
    }

    public byte[] decodeBlock(byte[] byArray, int n2, int n3) throws InvalidCipherTextException {
        int n4;
        int n5 = this.getOutputBlockSize() >> 31;
        byte[] byArray2 = new byte[this.engine.getOutputBlockSize()];
        byte[] byArray3 = this.engine.processBlock(byArray, n2, n3);
        n5 |= byArray2.length - byArray3.length >> 31;
        int n6 = Math.min(byArray2.length, byArray3.length);
        System.arraycopy(byArray3, 0, byArray2, byArray2.length - n6, n6);
        Arrays.fill(byArray3, (byte)0);
        this.mgf1Hash.reset();
        this.maskGeneratorFunction1(byArray2, this.defHash.length, byArray2.length - this.defHash.length, byArray2, 0, this.defHash.length);
        this.maskGeneratorFunction1(byArray2, 0, this.defHash.length, byArray2, this.defHash.length, byArray2.length - this.defHash.length);
        for (n4 = 0; n4 != this.defHash.length; ++n4) {
            n5 |= this.defHash[n4] ^ byArray2[this.defHash.length + n4];
        }
        n4 = -1;
        for (n6 = 2 * this.defHash.length; n6 != byArray2.length; ++n6) {
            int n7 = byArray2[n6] & 0xFF;
            int n8 = (-n7 & n4) >> 31;
            n4 += n6 & n8;
        }
        n5 |= n4 >> 31;
        if ((n5 |= byArray2[++n4] ^ 1) != 0) {
            Arrays.fill(byArray2, (byte)0);
            throw new InvalidCipherTextException("data wrong");
        }
        byte[] byArray4 = new byte[byArray2.length - ++n4];
        System.arraycopy(byArray2, n4, byArray4, 0, byArray4.length);
        Arrays.fill(byArray2, (byte)0);
        return byArray4;
    }

    private void maskGeneratorFunction1(byte[] byArray, int n2, int n3, byte[] byArray2, int n4, int n5) {
        int n6;
        int n7 = this.mgf1Hash.getDigestSize();
        byte[] byArray3 = new byte[n7];
        byte[] byArray4 = new byte[4];
        int n8 = 0;
        int n9 = n4 + n5;
        int n10 = n9 - n7;
        this.mgf1Hash.update(byArray, n2, n3);
        if (n3 > this.mgf1NoMemoLimit) {
            Memoable memoable = (Memoable)((Object)this.mgf1Hash);
            Memoable memoable2 = memoable.copy();
            for (n6 = n4; n6 < n10; n6 += n7) {
                Pack.intToBigEndian(n8++, byArray4, 0);
                this.mgf1Hash.update(byArray4, 0, byArray4.length);
                this.mgf1Hash.doFinal(byArray3, 0);
                memoable.reset(memoable2);
                Bytes.xorTo(n7, byArray3, 0, byArray2, n6);
            }
        } else {
            while (n6 < n10) {
                Pack.intToBigEndian(n8++, byArray4, 0);
                this.mgf1Hash.update(byArray4, 0, byArray4.length);
                this.mgf1Hash.doFinal(byArray3, 0);
                this.mgf1Hash.update(byArray, n2, n3);
                Bytes.xorTo(n7, byArray3, 0, byArray2, n6);
                n6 += n7;
            }
        }
        Pack.intToBigEndian(n8, byArray4, 0);
        this.mgf1Hash.update(byArray4, 0, byArray4.length);
        this.mgf1Hash.doFinal(byArray3, 0);
        Bytes.xorTo(n9 - n6, byArray3, 0, byArray2, n6);
    }
}

