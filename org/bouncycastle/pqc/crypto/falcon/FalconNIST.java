/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.falcon;

import java.security.SecureRandom;
import org.bouncycastle.pqc.crypto.falcon.FalconCodec;
import org.bouncycastle.pqc.crypto.falcon.FalconCommon;
import org.bouncycastle.pqc.crypto.falcon.FalconFPR;
import org.bouncycastle.pqc.crypto.falcon.FalconKeyGen;
import org.bouncycastle.pqc.crypto.falcon.FalconSign;
import org.bouncycastle.pqc.crypto.falcon.FalconVrfy;
import org.bouncycastle.pqc.crypto.falcon.SHAKE256;
import org.bouncycastle.util.Arrays;

class FalconNIST {
    int NONCELEN;
    int LOGN;
    private int N;
    private SecureRandom rand;
    private int CRYPTO_SECRETKEYBYTES;
    private int CRYPTO_PUBLICKEYBYTES;
    int CRYPTO_BYTES;
    private FalconCodec codec = new FalconCodec();

    FalconNIST(int n2, int n3, SecureRandom secureRandom) {
        this.rand = secureRandom;
        this.LOGN = n2;
        this.NONCELEN = n3;
        this.N = 1 << n2;
        this.CRYPTO_PUBLICKEYBYTES = 1 + 14 * this.N / 8;
        if (n2 == 10) {
            this.CRYPTO_SECRETKEYBYTES = 2305;
            this.CRYPTO_BYTES = 1330;
        } else if (n2 == 9 || n2 == 8) {
            this.CRYPTO_SECRETKEYBYTES = 1 + 6 * this.N * 2 / 8 + this.N;
            this.CRYPTO_BYTES = 690;
        } else if (n2 == 7 || n2 == 6) {
            this.CRYPTO_SECRETKEYBYTES = 1 + 7 * this.N * 2 / 8 + this.N;
            this.CRYPTO_BYTES = 690;
        } else {
            this.CRYPTO_SECRETKEYBYTES = 1 + this.N * 2 + this.N;
            this.CRYPTO_BYTES = 690;
        }
    }

    byte[][] crypto_sign_keypair(byte[] byArray, int n2, byte[] byArray2, int n3) {
        byte[] byArray3 = new byte[this.N];
        byte[] byArray4 = new byte[this.N];
        byte[] byArray5 = new byte[this.N];
        short[] sArray = new short[this.N];
        byte[] byArray6 = new byte[48];
        SHAKE256 sHAKE256 = new SHAKE256();
        FalconKeyGen falconKeyGen = new FalconKeyGen();
        this.rand.nextBytes(byArray6);
        sHAKE256.inner_shake256_init();
        sHAKE256.inner_shake256_inject(byArray6, 0, byArray6.length);
        sHAKE256.i_shake256_flip();
        falconKeyGen.keygen(sHAKE256, byArray3, 0, byArray4, 0, byArray5, 0, null, 0, sArray, 0, this.LOGN);
        byArray2[n3 + 0] = (byte)(80 + this.LOGN);
        int n4 = 1;
        int n5 = this.codec.trim_i8_encode(byArray2, n3 + n4, this.CRYPTO_SECRETKEYBYTES - n4, byArray3, 0, this.LOGN, this.codec.max_fg_bits[this.LOGN]);
        if (n5 == 0) {
            throw new IllegalStateException("f encode failed");
        }
        byte[] byArray7 = Arrays.copyOfRange(byArray2, n3 + n4, n4 + n5);
        if ((n5 = this.codec.trim_i8_encode(byArray2, n3 + (n4 += n5), this.CRYPTO_SECRETKEYBYTES - n4, byArray4, 0, this.LOGN, this.codec.max_fg_bits[this.LOGN])) == 0) {
            throw new IllegalStateException("g encode failed");
        }
        byte[] byArray8 = Arrays.copyOfRange(byArray2, n3 + n4, n4 + n5);
        if ((n5 = this.codec.trim_i8_encode(byArray2, n3 + (n4 += n5), this.CRYPTO_SECRETKEYBYTES - n4, byArray5, 0, this.LOGN, this.codec.max_FG_bits[this.LOGN])) == 0) {
            throw new IllegalStateException("F encode failed");
        }
        byte[] byArray9 = Arrays.copyOfRange(byArray2, n3 + n4, n4 + n5);
        if ((n4 += n5) != this.CRYPTO_SECRETKEYBYTES) {
            throw new IllegalStateException("secret key encoding failed");
        }
        byArray[n2 + 0] = (byte)(0 + this.LOGN);
        n5 = this.codec.modq_encode(byArray, n2 + 1, this.CRYPTO_PUBLICKEYBYTES - 1, sArray, 0, this.LOGN);
        if (n5 != this.CRYPTO_PUBLICKEYBYTES - 1) {
            throw new IllegalStateException("public key encoding failed");
        }
        return new byte[][]{Arrays.copyOfRange(byArray, 1, byArray.length), byArray7, byArray8, byArray9};
    }

    byte[] crypto_sign(boolean bl, byte[] byArray, byte[] byArray2, int n2, int n3, byte[] byArray3, int n4) {
        int n5;
        byte[] byArray4 = new byte[this.N];
        byte[] byArray5 = new byte[this.N];
        byte[] byArray6 = new byte[this.N];
        byte[] byArray7 = new byte[this.N];
        short[] sArray = new short[this.N];
        short[] sArray2 = new short[this.N];
        byte[] byArray8 = new byte[48];
        byte[] byArray9 = new byte[this.NONCELEN];
        SHAKE256 sHAKE256 = new SHAKE256();
        FalconSign falconSign = new FalconSign();
        FalconVrfy falconVrfy = new FalconVrfy();
        FalconCommon falconCommon = new FalconCommon();
        int n6 = 0;
        int n7 = this.codec.trim_i8_decode(byArray4, 0, this.LOGN, this.codec.max_fg_bits[this.LOGN], byArray3, n4 + n6, this.CRYPTO_SECRETKEYBYTES - n6);
        if (n7 == 0) {
            throw new IllegalStateException("f decode failed");
        }
        if ((n7 = this.codec.trim_i8_decode(byArray5, 0, this.LOGN, this.codec.max_fg_bits[this.LOGN], byArray3, n4 + (n6 += n7), this.CRYPTO_SECRETKEYBYTES - n6)) == 0) {
            throw new IllegalStateException("g decode failed");
        }
        if ((n7 = this.codec.trim_i8_decode(byArray6, 0, this.LOGN, this.codec.max_FG_bits[this.LOGN], byArray3, n4 + (n6 += n7), this.CRYPTO_SECRETKEYBYTES - n6)) == 0) {
            throw new IllegalArgumentException("F decode failed");
        }
        if ((n6 += n7) != this.CRYPTO_SECRETKEYBYTES - 1) {
            throw new IllegalStateException("full key not used");
        }
        if (!falconVrfy.complete_private(byArray7, 0, byArray4, 0, byArray5, 0, byArray6, 0, this.LOGN, new short[2 * this.N], 0)) {
            throw new IllegalStateException("complete_private failed");
        }
        this.rand.nextBytes(byArray9);
        sHAKE256.inner_shake256_init();
        sHAKE256.inner_shake256_inject(byArray9, 0, this.NONCELEN);
        sHAKE256.inner_shake256_inject(byArray2, n2, n3);
        sHAKE256.i_shake256_flip();
        falconCommon.hash_to_point_vartime(sHAKE256, sArray2, 0, this.LOGN);
        this.rand.nextBytes(byArray8);
        sHAKE256.inner_shake256_init();
        sHAKE256.inner_shake256_inject(byArray8, 0, byArray8.length);
        sHAKE256.i_shake256_flip();
        falconSign.sign_dyn(sArray, 0, sHAKE256, byArray4, 0, byArray5, 0, byArray6, 0, byArray7, 0, sArray2, 0, this.LOGN, new FalconFPR[10 * this.N], 0);
        byte[] byArray10 = new byte[this.CRYPTO_BYTES - 2 - this.NONCELEN];
        if (bl) {
            byArray10[0] = (byte)(32 + this.LOGN);
            n5 = this.codec.comp_encode(byArray10, 1, byArray10.length - 1, sArray, 0, this.LOGN);
            if (n5 == 0) {
                throw new IllegalStateException("signature failed to generate");
            }
            ++n5;
        } else {
            n5 = this.codec.comp_encode(byArray10, 0, byArray10.length, sArray, 0, this.LOGN);
            if (n5 == 0) {
                throw new IllegalStateException("signature failed to generate");
            }
        }
        byArray[0] = (byte)(48 + this.LOGN);
        System.arraycopy(byArray9, 0, byArray, 1, this.NONCELEN);
        System.arraycopy(byArray10, 0, byArray, 1 + this.NONCELEN, n5);
        return Arrays.copyOfRange(byArray, 0, 1 + this.NONCELEN + n5);
    }

    int crypto_sign_open(boolean bl, byte[] byArray, byte[] byArray2, byte[] byArray3, byte[] byArray4, int n2) {
        short[] sArray = new short[this.N];
        short[] sArray2 = new short[this.N];
        short[] sArray3 = new short[this.N];
        SHAKE256 sHAKE256 = new SHAKE256();
        FalconVrfy falconVrfy = new FalconVrfy();
        FalconCommon falconCommon = new FalconCommon();
        if (this.codec.modq_decode(sArray, 0, this.LOGN, byArray4, n2, this.CRYPTO_PUBLICKEYBYTES - 1) != this.CRYPTO_PUBLICKEYBYTES - 1) {
            return -1;
        }
        falconVrfy.to_ntt_monty(sArray, 0, this.LOGN);
        int n3 = byArray.length;
        int n4 = byArray3.length;
        if (bl) {
            if (n3 < 1 || byArray[0] != (byte)(32 + this.LOGN)) {
                return -1;
            }
            if (this.codec.comp_decode(sArray3, 0, this.LOGN, byArray, 1, n3 - 1) != n3 - 1) {
                return -1;
            }
        } else if (n3 < 1 || this.codec.comp_decode(sArray3, 0, this.LOGN, byArray, 0, n3) != n3) {
            return -1;
        }
        sHAKE256.inner_shake256_init();
        sHAKE256.inner_shake256_inject(byArray2, 0, this.NONCELEN);
        sHAKE256.inner_shake256_inject(byArray3, 0, n4);
        sHAKE256.i_shake256_flip();
        falconCommon.hash_to_point_vartime(sHAKE256, sArray2, 0, this.LOGN);
        if (falconVrfy.verify_raw(sArray2, 0, sArray3, 0, sArray, 0, this.LOGN, new short[this.N], 0) == 0) {
            return -1;
        }
        return 0;
    }
}

