/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.encodings;

import java.math.BigInteger;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.RSAKeyParameters;

public class ISO9796d1Encoding
implements AsymmetricBlockCipher {
    private static final BigInteger SIXTEEN = BigInteger.valueOf(16L);
    private static final BigInteger SIX = BigInteger.valueOf(6L);
    private static byte[] shadows = new byte[]{14, 3, 5, 8, 9, 4, 2, 15, 0, 13, 11, 6, 7, 10, 12, 1};
    private static byte[] inverse = new byte[]{8, 15, 6, 1, 5, 2, 11, 12, 3, 4, 13, 10, 14, 9, 0, 7};
    private AsymmetricBlockCipher engine;
    private boolean forEncryption;
    private int bitSize;
    private int padBits = 0;
    private BigInteger modulus;

    public ISO9796d1Encoding(AsymmetricBlockCipher asymmetricBlockCipher) {
        this.engine = asymmetricBlockCipher;
    }

    public AsymmetricBlockCipher getUnderlyingCipher() {
        return this.engine;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        RSAKeyParameters rSAKeyParameters = null;
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            rSAKeyParameters = (RSAKeyParameters)parametersWithRandom.getParameters();
        } else {
            rSAKeyParameters = (RSAKeyParameters)cipherParameters;
        }
        this.engine.init(bl, cipherParameters);
        this.modulus = rSAKeyParameters.getModulus();
        this.bitSize = this.modulus.bitLength();
        this.forEncryption = bl;
    }

    @Override
    public int getInputBlockSize() {
        int n2 = this.engine.getInputBlockSize();
        if (this.forEncryption) {
            return (n2 + 1) / 2;
        }
        return n2;
    }

    @Override
    public int getOutputBlockSize() {
        int n2 = this.engine.getOutputBlockSize();
        if (this.forEncryption) {
            return n2;
        }
        return (n2 + 1) / 2;
    }

    public void setPadBits(int n2) {
        if (n2 > 7) {
            throw new IllegalArgumentException("padBits > 7");
        }
        this.padBits = n2;
    }

    public int getPadBits() {
        return this.padBits;
    }

    @Override
    public byte[] processBlock(byte[] byArray, int n2, int n3) throws InvalidCipherTextException {
        if (this.forEncryption) {
            return this.encodeBlock(byArray, n2, n3);
        }
        return this.decodeBlock(byArray, n2, n3);
    }

    private byte[] encodeBlock(byte[] byArray, int n2, int n3) throws InvalidCipherTextException {
        byte by;
        int n4;
        byte[] byArray2 = new byte[(this.bitSize + 7) / 8];
        int n5 = this.padBits + 1;
        int n6 = n3;
        int n7 = (this.bitSize + 13) / 16;
        for (n4 = 0; n4 < n7; n4 += n6) {
            if (n4 > n7 - n6) {
                System.arraycopy(byArray, n2 + n3 - (n7 - n4), byArray2, byArray2.length - n7, n7 - n4);
                continue;
            }
            System.arraycopy(byArray, n2, byArray2, byArray2.length - (n4 + n6), n6);
        }
        for (n4 = byArray2.length - 2 * n7; n4 != byArray2.length; n4 += 2) {
            by = byArray2[byArray2.length - n7 + n4 / 2];
            byArray2[n4] = (byte)(shadows[(by & 0xFF) >>> 4] << 4 | shadows[by & 0xF]);
            byArray2[n4 + 1] = by;
        }
        int n8 = byArray2.length - 2 * n6;
        byArray2[n8] = (byte)(byArray2[n8] ^ n5);
        byArray2[byArray2.length - 1] = (byte)(byArray2[byArray2.length - 1] << 4 | 6);
        n4 = 8 - (this.bitSize - 1) % 8;
        by = 0;
        if (n4 != 8) {
            byArray2[0] = (byte)(byArray2[0] & 255 >>> n4);
            byArray2[0] = (byte)(byArray2[0] | 128 >>> n4);
        } else {
            byArray2[0] = 0;
            byArray2[1] = (byte)(byArray2[1] | 0x80);
            by = 1;
        }
        return this.engine.processBlock(byArray2, by, byArray2.length - by);
    }

    private byte[] decodeBlock(byte[] byArray, int n2, int n3) throws InvalidCipherTextException {
        int n4;
        BigInteger bigInteger;
        byte[] byArray2 = this.engine.processBlock(byArray, n2, n3);
        int n5 = 1;
        int n6 = (this.bitSize + 13) / 16;
        BigInteger bigInteger2 = new BigInteger(1, byArray2);
        if (bigInteger2.mod(SIXTEEN).equals(SIX)) {
            bigInteger = bigInteger2;
        } else if (this.modulus.subtract(bigInteger2).mod(SIXTEEN).equals(SIX)) {
            bigInteger = this.modulus.subtract(bigInteger2);
        } else {
            throw new InvalidCipherTextException("resulting integer iS or (modulus - iS) is not congruent to 6 mod 16");
        }
        byArray2 = ISO9796d1Encoding.convertOutputDecryptOnly(bigInteger);
        if ((byArray2[byArray2.length - 1] & 0xF) != 6) {
            throw new InvalidCipherTextException("invalid forcing byte in block");
        }
        byArray2[byArray2.length - 1] = (byte)((byArray2[byArray2.length - 1] & 0xFF) >>> 4 | inverse[(byArray2[byArray2.length - 2] & 0xFF) >> 4] << 4);
        byArray2[0] = (byte)(shadows[(byArray2[1] & 0xFF) >>> 4] << 4 | shadows[byArray2[1] & 0xF]);
        boolean bl = false;
        int n7 = 0;
        for (int i2 = byArray2.length - 1; i2 >= byArray2.length - 2 * n6; i2 -= 2) {
            n4 = shadows[(byArray2[i2] & 0xFF) >>> 4] << 4 | shadows[byArray2[i2] & 0xF];
            if (((byArray2[i2 - 1] ^ n4) & 0xFF) == 0) continue;
            if (!bl) {
                bl = true;
                n5 = (byArray2[i2 - 1] ^ n4) & 0xFF;
                n7 = i2 - 1;
                continue;
            }
            throw new InvalidCipherTextException("invalid tsums in block");
        }
        byArray2[n7] = 0;
        byte[] byArray3 = new byte[(byArray2.length - n7) / 2];
        for (n4 = 0; n4 < byArray3.length; ++n4) {
            byArray3[n4] = byArray2[2 * n4 + n7 + 1];
        }
        this.padBits = n5 - 1;
        return byArray3;
    }

    private static byte[] convertOutputDecryptOnly(BigInteger bigInteger) {
        byte[] byArray = bigInteger.toByteArray();
        if (byArray[0] == 0) {
            byte[] byArray2 = new byte[byArray.length - 1];
            System.arraycopy(byArray, 1, byArray2, 0, byArray2.length);
            return byArray2;
        }
        return byArray;
    }
}

