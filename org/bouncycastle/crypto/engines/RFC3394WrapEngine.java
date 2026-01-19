/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.util.Arrays;

public class RFC3394WrapEngine
implements Wrapper {
    private static final byte[] DEFAULT_IV = new byte[]{-90, -90, -90, -90, -90, -90, -90, -90};
    private final BlockCipher engine;
    private final boolean wrapCipherMode;
    private final byte[] iv = new byte[8];
    private KeyParameter param = null;
    private boolean forWrapping = true;

    public RFC3394WrapEngine(BlockCipher blockCipher) {
        this(blockCipher, false);
    }

    public RFC3394WrapEngine(BlockCipher blockCipher, boolean bl) {
        this.engine = blockCipher;
        this.wrapCipherMode = !bl;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.forWrapping = bl;
        if (cipherParameters instanceof ParametersWithRandom) {
            cipherParameters = ((ParametersWithRandom)cipherParameters).getParameters();
        }
        if (cipherParameters instanceof KeyParameter) {
            this.param = (KeyParameter)cipherParameters;
            System.arraycopy(DEFAULT_IV, 0, this.iv, 0, 8);
        } else if (cipherParameters instanceof ParametersWithIV) {
            ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            byte[] byArray = parametersWithIV.getIV();
            if (byArray.length != 8) {
                throw new IllegalArgumentException("IV not equal to 8");
            }
            this.param = (KeyParameter)parametersWithIV.getParameters();
            System.arraycopy(byArray, 0, this.iv, 0, 8);
        }
    }

    @Override
    public String getAlgorithmName() {
        return this.engine.getAlgorithmName();
    }

    @Override
    public byte[] wrap(byte[] byArray, int n2, int n3) {
        if (!this.forWrapping) {
            throw new IllegalStateException("not set for wrapping");
        }
        if (n3 < 8) {
            throw new DataLengthException("wrap data must be at least 8 bytes");
        }
        int n4 = n3 / 8;
        if (n4 * 8 != n3) {
            throw new DataLengthException("wrap data must be a multiple of 8 bytes");
        }
        this.engine.init(this.wrapCipherMode, this.param);
        byte[] byArray2 = new byte[n3 + this.iv.length];
        System.arraycopy(this.iv, 0, byArray2, 0, this.iv.length);
        System.arraycopy(byArray, n2, byArray2, this.iv.length, n3);
        if (n4 == 1) {
            this.engine.processBlock(byArray2, 0, byArray2, 0);
        } else {
            byte[] byArray3 = new byte[8 + this.iv.length];
            for (int i2 = 0; i2 != 6; ++i2) {
                for (int i3 = 1; i3 <= n4; ++i3) {
                    System.arraycopy(byArray2, 0, byArray3, 0, this.iv.length);
                    System.arraycopy(byArray2, 8 * i3, byArray3, this.iv.length, 8);
                    this.engine.processBlock(byArray3, 0, byArray3, 0);
                    int n5 = n4 * i2 + i3;
                    int n6 = 1;
                    while (n5 != 0) {
                        byte by = (byte)n5;
                        int n7 = this.iv.length - n6;
                        byArray3[n7] = (byte)(byArray3[n7] ^ by);
                        n5 >>>= 8;
                        ++n6;
                    }
                    System.arraycopy(byArray3, 0, byArray2, 0, 8);
                    System.arraycopy(byArray3, 8, byArray2, 8 * i3, 8);
                }
            }
        }
        return byArray2;
    }

    @Override
    public byte[] unwrap(byte[] byArray, int n2, int n3) throws InvalidCipherTextException {
        int n4;
        int n5;
        int n6;
        int n7;
        if (this.forWrapping) {
            throw new IllegalStateException("not set for unwrapping");
        }
        if (n3 < 16) {
            throw new InvalidCipherTextException("unwrap data too short");
        }
        int n8 = n3 / 8;
        if (n8 * 8 != n3) {
            throw new InvalidCipherTextException("unwrap data must be a multiple of 8 bytes");
        }
        this.engine.init(!this.wrapCipherMode, this.param);
        byte[] byArray2 = new byte[n3 - this.iv.length];
        byte[] byArray3 = new byte[this.iv.length];
        byte[] byArray4 = new byte[8 + this.iv.length];
        if (--n8 == 1) {
            this.engine.processBlock(byArray, n2, byArray4, 0);
            System.arraycopy(byArray4, 0, byArray3, 0, this.iv.length);
            System.arraycopy(byArray4, this.iv.length, byArray2, 0, 8);
        } else {
            System.arraycopy(byArray, n2, byArray3, 0, this.iv.length);
            System.arraycopy(byArray, n2 + this.iv.length, byArray2, 0, n3 - this.iv.length);
            for (n7 = 5; n7 >= 0; --n7) {
                for (n6 = n8; n6 >= 1; --n6) {
                    System.arraycopy(byArray3, 0, byArray4, 0, this.iv.length);
                    System.arraycopy(byArray2, 8 * (n6 - 1), byArray4, this.iv.length, 8);
                    n5 = n8 * n7 + n6;
                    n4 = 1;
                    while (n5 != 0) {
                        byte by = (byte)n5;
                        int n9 = this.iv.length - n4;
                        byArray4[n9] = (byte)(byArray4[n9] ^ by);
                        n5 >>>= 8;
                        ++n4;
                    }
                    this.engine.processBlock(byArray4, 0, byArray4, 0);
                    System.arraycopy(byArray4, 0, byArray3, 0, 8);
                    System.arraycopy(byArray4, 8, byArray2, 8 * (n6 - 1), 8);
                }
            }
        }
        if (n8 != 1) {
            if (!Arrays.constantTimeAreEqual(byArray3, this.iv)) {
                throw new InvalidCipherTextException("checksum failed");
            }
        } else if (!Arrays.constantTimeAreEqual(byArray3, this.iv)) {
            System.arraycopy(byArray, n2, byArray3, 0, this.iv.length);
            System.arraycopy(byArray, n2 + this.iv.length, byArray2, 0, n3 - this.iv.length);
            for (n7 = 5; n7 >= 0; --n7) {
                System.arraycopy(byArray3, 0, byArray4, 0, this.iv.length);
                System.arraycopy(byArray2, 0, byArray4, this.iv.length, 8);
                n6 = n8 * n7 + 1;
                n5 = 1;
                while (n6 != 0) {
                    n4 = (byte)n6;
                    int n10 = this.iv.length - n5;
                    byArray4[n10] = (byte)(byArray4[n10] ^ n4);
                    n6 >>>= 8;
                    ++n5;
                }
                this.engine.processBlock(byArray4, 0, byArray4, 0);
                System.arraycopy(byArray4, 0, byArray3, 0, 8);
                System.arraycopy(byArray4, 8, byArray2, 0, 8);
            }
            if (!Arrays.constantTimeAreEqual(byArray3, this.iv)) {
                throw new InvalidCipherTextException("checksum failed");
            }
        }
        return byArray2;
    }
}

