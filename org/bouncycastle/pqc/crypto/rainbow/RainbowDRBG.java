/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.rainbow;

import java.security.SecureRandom;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.pqc.crypto.rainbow.RainbowUtil;
import org.bouncycastle.util.Arrays;

class RainbowDRBG
extends SecureRandom {
    private byte[] seed;
    private byte[] key;
    private byte[] v;
    private Digest hashAlgo;

    public RainbowDRBG(byte[] byArray, Digest digest) {
        this.seed = byArray;
        this.hashAlgo = digest;
        this.init(256);
    }

    private void init(int n2) {
        if (this.seed.length >= 48) {
            this.randombytes_init(this.seed, n2);
        } else {
            byte[] byArray = RainbowUtil.hash(this.hashAlgo, this.seed, 48 - this.seed.length);
            this.randombytes_init(Arrays.concatenate(this.seed, byArray), n2);
        }
    }

    @Override
    public void nextBytes(byte[] byArray) {
        byte[] byArray2 = new byte[16];
        int n2 = 0;
        int n3 = byArray.length;
        while (n3 > 0) {
            for (int i2 = 15; i2 >= 0; --i2) {
                if ((this.v[i2] & 0xFF) != 255) {
                    int n4 = i2;
                    this.v[n4] = (byte)(this.v[n4] + 1);
                    break;
                }
                this.v[i2] = 0;
            }
            this.AES256_ECB(this.key, this.v, byArray2, 0);
            if (n3 > 15) {
                System.arraycopy(byArray2, 0, byArray, n2, byArray2.length);
                n2 += 16;
                n3 -= 16;
                continue;
            }
            System.arraycopy(byArray2, 0, byArray, n2, n3);
            n3 = 0;
        }
        this.AES256_CTR_DRBG_Update(null, this.key, this.v);
    }

    private void AES256_ECB(byte[] byArray, byte[] byArray2, byte[] byArray3, int n2) {
        try {
            AESEngine aESEngine = new AESEngine();
            aESEngine.init(true, new KeyParameter(byArray));
            for (int i2 = 0; i2 != byArray2.length; i2 += 16) {
                aESEngine.processBlock(byArray2, i2, byArray3, n2 + i2);
            }
        }
        catch (Throwable throwable) {
            throw new IllegalStateException("drbg failure: " + throwable.getMessage(), throwable);
        }
    }

    private void AES256_CTR_DRBG_Update(byte[] byArray, byte[] byArray2, byte[] byArray3) {
        int n2;
        byte[] byArray4 = new byte[48];
        for (n2 = 0; n2 < 3; ++n2) {
            for (int i2 = 15; i2 >= 0; --i2) {
                if ((byArray3[i2] & 0xFF) != 255) {
                    int n3 = i2;
                    byArray3[n3] = (byte)(byArray3[n3] + 1);
                    break;
                }
                byArray3[i2] = 0;
            }
            this.AES256_ECB(byArray2, byArray3, byArray4, 16 * n2);
        }
        if (byArray != null) {
            for (n2 = 0; n2 < 48; ++n2) {
                int n4 = n2;
                byArray4[n4] = (byte)(byArray4[n4] ^ byArray[n2]);
            }
        }
        System.arraycopy(byArray4, 0, byArray2, 0, byArray2.length);
        System.arraycopy(byArray4, 32, byArray3, 0, byArray3.length);
    }

    private void randombytes_init(byte[] byArray, int n2) {
        byte[] byArray2 = new byte[48];
        System.arraycopy(byArray, 0, byArray2, 0, byArray2.length);
        this.key = new byte[32];
        this.v = new byte[16];
        this.AES256_CTR_DRBG_Update(byArray2, this.key, this.v);
    }
}

