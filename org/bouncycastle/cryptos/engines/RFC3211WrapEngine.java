/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import java.security.SecureRandom;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.util.Arrays;

public class RFC3211WrapEngine
implements Wrapper {
    private CBCBlockCipher engine;
    private ParametersWithIV param;
    private boolean forWrapping;
    private SecureRandom rand;

    public RFC3211WrapEngine(BlockCipher blockCipher) {
        this.engine = new CBCBlockCipher(blockCipher);
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.forWrapping = bl;
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.rand = parametersWithRandom.getRandom();
            if (!(parametersWithRandom.getParameters() instanceof ParametersWithIV)) {
                throw new IllegalArgumentException("RFC3211Wrap requires an IV");
            }
            this.param = (ParametersWithIV)parametersWithRandom.getParameters();
        } else {
            if (bl) {
                this.rand = CryptoServicesRegistrar.getSecureRandom();
            }
            if (!(cipherParameters instanceof ParametersWithIV)) {
                throw new IllegalArgumentException("RFC3211Wrap requires an IV");
            }
            this.param = (ParametersWithIV)cipherParameters;
        }
    }

    @Override
    public String getAlgorithmName() {
        return this.engine.getUnderlyingCipher().getAlgorithmName() + "/RFC3211Wrap";
    }

    @Override
    public byte[] wrap(byte[] byArray, int n2, int n3) {
        int n4;
        if (!this.forWrapping) {
            throw new IllegalStateException("not set for wrapping");
        }
        if (n3 > 255 || n3 < 0) {
            throw new IllegalArgumentException("input must be from 0 to 255 bytes");
        }
        this.engine.init(true, this.param);
        int n5 = this.engine.getBlockSize();
        byte[] byArray2 = n3 + 4 < n5 * 2 ? new byte[n5 * 2] : new byte[(n3 + 4) % n5 == 0 ? n3 + 4 : ((n3 + 4) / n5 + 1) * n5];
        byArray2[0] = (byte)n3;
        System.arraycopy(byArray, n2, byArray2, 4, n3);
        byte[] byArray3 = new byte[byArray2.length - (n3 + 4)];
        this.rand.nextBytes(byArray3);
        System.arraycopy(byArray3, 0, byArray2, n3 + 4, byArray3.length);
        byArray2[1] = ~byArray2[4];
        byArray2[2] = ~byArray2[5];
        byArray2[3] = ~byArray2[6];
        for (n4 = 0; n4 < byArray2.length; n4 += n5) {
            this.engine.processBlock(byArray2, n4, byArray2, n4);
        }
        for (n4 = 0; n4 < byArray2.length; n4 += n5) {
            this.engine.processBlock(byArray2, n4, byArray2, n4);
        }
        return byArray2;
    }

    @Override
    public byte[] unwrap(byte[] byArray, int n2, int n3) throws InvalidCipherTextException {
        int n4;
        if (this.forWrapping) {
            throw new IllegalStateException("not set for unwrapping");
        }
        int n5 = this.engine.getBlockSize();
        if (n3 < 2 * n5) {
            throw new InvalidCipherTextException("input too short");
        }
        byte[] byArray2 = new byte[n3];
        byte[] byArray3 = new byte[n5];
        System.arraycopy(byArray, n2, byArray2, 0, n3);
        System.arraycopy(byArray, n2, byArray3, 0, byArray3.length);
        this.engine.init(false, new ParametersWithIV(this.param.getParameters(), byArray3));
        for (n4 = n5; n4 < byArray2.length; n4 += n5) {
            this.engine.processBlock(byArray2, n4, byArray2, n4);
        }
        System.arraycopy(byArray2, byArray2.length - byArray3.length, byArray3, 0, byArray3.length);
        this.engine.init(false, new ParametersWithIV(this.param.getParameters(), byArray3));
        this.engine.processBlock(byArray2, 0, byArray2, 0);
        this.engine.init(false, this.param);
        for (n4 = 0; n4 < byArray2.length; n4 += n5) {
            this.engine.processBlock(byArray2, n4, byArray2, n4);
        }
        n4 = (byArray2[0] & 0xFF) > byArray2.length - 4 ? 1 : 0;
        byte[] byArray4 = n4 != 0 ? new byte[byArray2.length - 4] : new byte[byArray2[0] & 0xFF];
        System.arraycopy(byArray2, 4, byArray4, 0, byArray4.length);
        int n6 = 0;
        for (int i2 = 0; i2 != 3; ++i2) {
            byte by = ~byArray2[1 + i2];
            n6 |= by ^ byArray2[4 + i2];
        }
        Arrays.clear(byArray2);
        if (((n6 != 0 ? 1 : 0) | n4) != 0) {
            throw new InvalidCipherTextException("wrapped key corrupted");
        }
        return byArray4;
    }
}

