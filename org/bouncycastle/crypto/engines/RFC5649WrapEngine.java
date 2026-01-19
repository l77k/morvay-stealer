/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.engines.RFC3394WrapEngine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class RFC5649WrapEngine
implements Wrapper {
    private static final byte[] DEFAULT_IV = new byte[]{-90, 89, 89, -90};
    private final BlockCipher engine;
    private final byte[] preIV = new byte[4];
    private KeyParameter param = null;
    private boolean forWrapping = true;

    public RFC5649WrapEngine(BlockCipher blockCipher) {
        this.engine = blockCipher;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.forWrapping = bl;
        if (cipherParameters instanceof ParametersWithRandom) {
            cipherParameters = ((ParametersWithRandom)cipherParameters).getParameters();
        }
        if (cipherParameters instanceof KeyParameter) {
            this.param = (KeyParameter)cipherParameters;
            System.arraycopy(DEFAULT_IV, 0, this.preIV, 0, 4);
        } else if (cipherParameters instanceof ParametersWithIV) {
            ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            byte[] byArray = parametersWithIV.getIV();
            if (byArray.length != 4) {
                throw new IllegalArgumentException("IV length not equal to 4");
            }
            this.param = (KeyParameter)parametersWithIV.getParameters();
            System.arraycopy(byArray, 0, this.preIV, 0, 4);
        }
    }

    @Override
    public String getAlgorithmName() {
        return this.engine.getAlgorithmName();
    }

    private byte[] padPlaintext(byte[] byArray) {
        int n2 = byArray.length;
        int n3 = (8 - n2 % 8) % 8;
        byte[] byArray2 = new byte[n2 + n3];
        System.arraycopy(byArray, 0, byArray2, 0, n2);
        if (n3 != 0) {
            byte[] byArray3 = new byte[n3];
            System.arraycopy(byArray3, 0, byArray2, n2, n3);
        }
        return byArray2;
    }

    @Override
    public byte[] wrap(byte[] byArray, int n2, int n3) {
        if (!this.forWrapping) {
            throw new IllegalStateException("not set for wrapping");
        }
        byte[] byArray2 = new byte[8];
        System.arraycopy(this.preIV, 0, byArray2, 0, 4);
        Pack.intToBigEndian(n3, byArray2, 4);
        byte[] byArray3 = new byte[n3];
        System.arraycopy(byArray, n2, byArray3, 0, n3);
        byte[] byArray4 = this.padPlaintext(byArray3);
        if (byArray4.length == 8) {
            byte[] byArray5 = new byte[byArray4.length + byArray2.length];
            System.arraycopy(byArray2, 0, byArray5, 0, byArray2.length);
            System.arraycopy(byArray4, 0, byArray5, byArray2.length, byArray4.length);
            this.engine.init(true, this.param);
            int n4 = this.engine.getBlockSize();
            for (int i2 = 0; i2 < byArray5.length; i2 += n4) {
                this.engine.processBlock(byArray5, i2, byArray5, i2);
            }
            return byArray5;
        }
        RFC3394WrapEngine rFC3394WrapEngine = new RFC3394WrapEngine(this.engine);
        ParametersWithIV parametersWithIV = new ParametersWithIV(this.param, byArray2);
        rFC3394WrapEngine.init(true, parametersWithIV);
        return rFC3394WrapEngine.wrap(byArray4, 0, byArray4.length);
    }

    @Override
    public byte[] unwrap(byte[] byArray, int n2, int n3) throws InvalidCipherTextException {
        int n4;
        byte[] byArray2;
        int n5;
        if (this.forWrapping) {
            throw new IllegalStateException("not set for unwrapping");
        }
        int n6 = n3 / 8;
        if (n6 * 8 != n3) {
            throw new InvalidCipherTextException("unwrap data must be a multiple of 8 bytes");
        }
        if (n6 <= 1) {
            throw new InvalidCipherTextException("unwrap data must be at least 16 bytes");
        }
        byte[] byArray3 = new byte[n3];
        System.arraycopy(byArray, n2, byArray3, 0, n3);
        byte[] byArray4 = new byte[n3];
        byte[] byArray5 = new byte[8];
        if (n6 == 2) {
            this.engine.init(false, this.param);
            n5 = this.engine.getBlockSize();
            for (int i2 = 0; i2 < byArray3.length; i2 += n5) {
                this.engine.processBlock(byArray3, i2, byArray4, i2);
            }
            System.arraycopy(byArray4, 0, byArray5, 0, byArray5.length);
            byArray2 = new byte[byArray4.length - byArray5.length];
            System.arraycopy(byArray4, byArray5.length, byArray2, 0, byArray2.length);
        } else {
            byArray2 = byArray4 = this.rfc3394UnwrapNoIvCheck(byArray, n2, n3, byArray5);
        }
        byte[] byArray6 = new byte[4];
        System.arraycopy(byArray5, 0, byArray6, 0, 4);
        n5 = Pack.bigEndianToInt(byArray5, 4);
        boolean bl = Arrays.constantTimeAreEqual(byArray6, this.preIV);
        int n7 = byArray2.length;
        int n8 = n7 - 8;
        if (n5 <= n8) {
            bl = false;
        }
        if (n5 > n7) {
            bl = false;
        }
        if ((n4 = n7 - n5) >= 8 || n4 < 0) {
            bl = false;
            n4 = 4;
        }
        byte[] byArray7 = new byte[n4];
        byte[] byArray8 = new byte[n4];
        System.arraycopy(byArray2, byArray2.length - n4, byArray8, 0, n4);
        if (!Arrays.constantTimeAreEqual(byArray8, byArray7)) {
            bl = false;
        }
        if (!bl) {
            throw new InvalidCipherTextException("checksum failed");
        }
        byte[] byArray9 = new byte[n5];
        System.arraycopy(byArray2, 0, byArray9, 0, byArray9.length);
        return byArray9;
    }

    private byte[] rfc3394UnwrapNoIvCheck(byte[] byArray, int n2, int n3, byte[] byArray2) {
        byte[] byArray3 = new byte[n3 - 8];
        byte[] byArray4 = new byte[16];
        System.arraycopy(byArray, n2, byArray4, 0, 8);
        System.arraycopy(byArray, n2 + 8, byArray3, 0, n3 - 8);
        this.engine.init(false, this.param);
        int n4 = n3 / 8;
        --n4;
        for (int i2 = 5; i2 >= 0; --i2) {
            for (int i3 = n4; i3 >= 1; --i3) {
                System.arraycopy(byArray3, 8 * (i3 - 1), byArray4, 8, 8);
                int n5 = n4 * i2 + i3;
                int n6 = 1;
                while (n5 != 0) {
                    int n7 = 8 - n6;
                    byArray4[n7] = (byte)(byArray4[n7] ^ (byte)n5);
                    n5 >>>= 8;
                    ++n6;
                }
                this.engine.processBlock(byArray4, 0, byArray4, 0);
                System.arraycopy(byArray4, 8, byArray3, 8 * (i3 - 1), 8);
            }
        }
        System.arraycopy(byArray4, 0, byArray2, 0, 8);
        return byArray3;
    }
}

