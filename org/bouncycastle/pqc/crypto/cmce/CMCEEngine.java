/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.cmce;

import java.security.SecureRandom;
import org.bouncycastle.crypto.digests.SHAKEDigest;
import org.bouncycastle.pqc.crypto.cmce.BENES;
import org.bouncycastle.pqc.crypto.cmce.BENES12;
import org.bouncycastle.pqc.crypto.cmce.BENES13;
import org.bouncycastle.pqc.crypto.cmce.GF;
import org.bouncycastle.pqc.crypto.cmce.GF12;
import org.bouncycastle.pqc.crypto.cmce.GF13;
import org.bouncycastle.pqc.crypto.cmce.Utils;
import org.bouncycastle.util.Arrays;

class CMCEEngine {
    private int SYS_N;
    private int SYS_T;
    private int GFBITS;
    private int IRR_BYTES;
    private int COND_BYTES;
    private int PK_NROWS;
    private int PK_NCOLS;
    private int PK_ROW_BYTES;
    private int SYND_BYTES;
    private int GFMASK;
    private int[] poly;
    private final int defaultKeySize;
    private GF gf;
    private BENES benes;
    private boolean usePadding;
    private boolean countErrorIndices;
    private boolean usePivots;

    public int getIrrBytes() {
        return this.IRR_BYTES;
    }

    public int getCondBytes() {
        return this.COND_BYTES;
    }

    public int getPrivateKeySize() {
        return this.COND_BYTES + this.IRR_BYTES + this.SYS_N / 8 + 40;
    }

    public int getPublicKeySize() {
        if (this.usePadding) {
            return this.PK_NROWS * (this.SYS_N / 8 - (this.PK_NROWS - 1) / 8);
        }
        return this.PK_NROWS * this.PK_NCOLS / 8;
    }

    public int getCipherTextSize() {
        return this.SYND_BYTES;
    }

    public CMCEEngine(int n2, int n3, int n4, int[] nArray, boolean bl, int n5) {
        this.usePivots = bl;
        this.SYS_N = n3;
        this.SYS_T = n4;
        this.GFBITS = n2;
        this.poly = nArray;
        this.defaultKeySize = n5;
        this.IRR_BYTES = this.SYS_T * 2;
        this.COND_BYTES = (1 << this.GFBITS - 4) * (2 * this.GFBITS - 1);
        this.PK_NROWS = this.SYS_T * this.GFBITS;
        this.PK_NCOLS = this.SYS_N - this.PK_NROWS;
        this.PK_ROW_BYTES = (this.PK_NCOLS + 7) / 8;
        this.SYND_BYTES = (this.PK_NROWS + 7) / 8;
        this.GFMASK = (1 << this.GFBITS) - 1;
        if (this.GFBITS == 12) {
            this.gf = new GF12();
            this.benes = new BENES12(this.SYS_N, this.SYS_T, this.GFBITS);
        } else {
            this.gf = new GF13();
            this.benes = new BENES13(this.SYS_N, this.SYS_T, this.GFBITS);
        }
        this.usePadding = this.SYS_T % 8 != 0;
        this.countErrorIndices = 1 << this.GFBITS > this.SYS_N;
    }

    public byte[] generate_public_key_from_private_key(byte[] byArray) {
        byte[] byArray2 = new byte[this.getPublicKeySize()];
        short[] sArray = new short[1 << this.GFBITS];
        long[] lArray = new long[]{0L};
        int[] nArray = new int[1 << this.GFBITS];
        byte[] byArray3 = new byte[this.SYS_N / 8 + (1 << this.GFBITS) * 4];
        int n2 = byArray3.length - 32 - this.IRR_BYTES - (1 << this.GFBITS) * 4;
        SHAKEDigest sHAKEDigest = new SHAKEDigest(256);
        sHAKEDigest.update((byte)64);
        sHAKEDigest.update(byArray, 0, 32);
        sHAKEDigest.doFinal(byArray3, 0, byArray3.length);
        for (int i2 = 0; i2 < 1 << this.GFBITS; ++i2) {
            nArray[i2] = Utils.load4(byArray3, n2 + i2 * 4);
        }
        this.pk_gen(byArray2, byArray, nArray, sArray, lArray);
        return byArray2;
    }

    public byte[] decompress_private_key(byte[] byArray) {
        int n2;
        Object[] objectArray;
        Object[] objectArray2;
        byte[] byArray2 = new byte[this.getPrivateKeySize()];
        System.arraycopy(byArray, 0, byArray2, 0, byArray.length);
        byte[] byArray3 = new byte[this.SYS_N / 8 + (1 << this.GFBITS) * 4 + this.IRR_BYTES + 32];
        int n3 = 0;
        SHAKEDigest sHAKEDigest = new SHAKEDigest(256);
        sHAKEDigest.update((byte)64);
        sHAKEDigest.update(byArray, 0, 32);
        sHAKEDigest.doFinal(byArray3, 0, byArray3.length);
        if (byArray.length <= 40) {
            objectArray2 = new short[this.SYS_T];
            objectArray = new byte[this.IRR_BYTES];
            n3 = byArray3.length - 32 - this.IRR_BYTES;
            for (n2 = 0; n2 < this.SYS_T; ++n2) {
                objectArray2[n2] = Utils.load_gf(byArray3, n3 + n2 * 2, this.GFMASK);
            }
            this.generate_irr_poly((short[])objectArray2);
            for (n2 = 0; n2 < this.SYS_T; ++n2) {
                Utils.store_gf(objectArray, n2 * 2, objectArray2[n2]);
            }
            System.arraycopy(objectArray, 0, byArray2, 40, this.IRR_BYTES);
        }
        if (byArray.length <= 40 + this.IRR_BYTES) {
            Object[] objectArray3;
            objectArray2 = new int[1 << this.GFBITS];
            objectArray = new short[1 << this.GFBITS];
            n3 = byArray3.length - 32 - this.IRR_BYTES - (1 << this.GFBITS) * 4;
            for (n2 = 0; n2 < 1 << this.GFBITS; ++n2) {
                objectArray2[n2] = Utils.load4(byArray3, n3 + n2 * 4);
            }
            if (this.usePivots) {
                objectArray3 = new long[]{0L};
                this.pk_gen(null, byArray2, (int[])objectArray2, (short[])objectArray, (long[])objectArray3);
            } else {
                objectArray3 = new long[1 << this.GFBITS];
                int n4 = 0;
                while (n4 < 1 << this.GFBITS) {
                    objectArray3[n4] = objectArray2[n4];
                    int n5 = n4;
                    objectArray3[n5] = objectArray3[n5] << 31;
                    int n6 = n4;
                    objectArray3[n6] = objectArray3[n6] | (long)n4;
                    int n7 = n4++;
                    objectArray3[n7] = objectArray3[n7] & Long.MAX_VALUE;
                }
                CMCEEngine.sort64(objectArray3, 0, objectArray3.length);
                for (n4 = 0; n4 < 1 << this.GFBITS; ++n4) {
                    objectArray[n4] = (short)(objectArray3[n4] & (long)this.GFMASK);
                }
            }
            objectArray3 = new byte[this.COND_BYTES];
            CMCEEngine.controlbitsfrompermutation((byte[])objectArray3, objectArray, this.GFBITS, 1 << this.GFBITS);
            System.arraycopy(objectArray3, 0, byArray2, this.IRR_BYTES + 40, objectArray3.length);
        }
        System.arraycopy(byArray3, 0, byArray2, this.getPrivateKeySize() - this.SYS_N / 8, this.SYS_N / 8);
        return byArray2;
    }

    public void kem_keypair(byte[] byArray, byte[] byArray2, SecureRandom secureRandom) {
        short[] sArray;
        byte[] byArray3 = new byte[1];
        byte[] byArray4 = new byte[32];
        byArray3[0] = 64;
        secureRandom.nextBytes(byArray4);
        byte[] byArray5 = new byte[this.SYS_N / 8 + (1 << this.GFBITS) * 4 + this.SYS_T * 2 + 32];
        int n2 = 0;
        byte[] byArray6 = byArray4;
        long[] lArray = new long[]{0L};
        SHAKEDigest sHAKEDigest = new SHAKEDigest(256);
        while (true) {
            int n3;
            int n4;
            sHAKEDigest.update(byArray3, 0, byArray3.length);
            sHAKEDigest.update(byArray4, 0, byArray4.length);
            sHAKEDigest.doFinal(byArray5, 0, byArray5.length);
            int n5 = byArray5.length - 32;
            byArray4 = Arrays.copyOfRange(byArray5, n5, n5 + 32);
            System.arraycopy(byArray6, 0, byArray2, 0, 32);
            byArray6 = Arrays.copyOfRange(byArray4, 0, 32);
            short[] sArray2 = new short[this.SYS_T];
            n5 = n4 = byArray5.length - 32 - 2 * this.SYS_T;
            for (n3 = 0; n3 < this.SYS_T; ++n3) {
                sArray2[n3] = Utils.load_gf(byArray5, n4 + n3 * 2, this.GFMASK);
            }
            if (this.generate_irr_poly(sArray2) == -1) continue;
            n2 = 40;
            for (n3 = 0; n3 < this.SYS_T; ++n3) {
                Utils.store_gf(byArray2, n2 + n3 * 2, sArray2[n3]);
            }
            int[] nArray = new int[1 << this.GFBITS];
            n5 -= (1 << this.GFBITS) * 4;
            for (int i2 = 0; i2 < 1 << this.GFBITS; ++i2) {
                nArray[i2] = Utils.load4(byArray5, n5 + i2 * 4);
            }
            sArray = new short[1 << this.GFBITS];
            if (this.pk_gen(byArray, byArray2, nArray, sArray, lArray) != -1) break;
        }
        byte[] byArray7 = new byte[this.COND_BYTES];
        CMCEEngine.controlbitsfrompermutation(byArray7, sArray, this.GFBITS, 1 << this.GFBITS);
        System.arraycopy(byArray7, 0, byArray2, this.IRR_BYTES + 40, byArray7.length);
        System.arraycopy(byArray5, n5 -= this.SYS_N / 8, byArray2, byArray2.length - this.SYS_N / 8, this.SYS_N / 8);
        if (!this.usePivots) {
            Utils.store8(byArray2, 32, 0xFFFFFFFFL);
        } else {
            Utils.store8(byArray2, 32, lArray[0]);
        }
    }

    private void syndrome(byte[] byArray, byte[] byArray2, byte[] byArray3) {
        int n2;
        short[] sArray = new short[this.SYS_N / 8];
        int n3 = 0;
        int n4 = this.PK_NROWS % 8;
        for (n2 = 0; n2 < this.SYND_BYTES; ++n2) {
            byArray[n2] = 0;
        }
        for (n2 = 0; n2 < this.PK_NROWS; ++n2) {
            int n5;
            for (n5 = 0; n5 < this.SYS_N / 8; ++n5) {
                sArray[n5] = 0;
            }
            for (n5 = 0; n5 < this.PK_ROW_BYTES; ++n5) {
                sArray[this.SYS_N / 8 - this.PK_ROW_BYTES + n5] = byArray2[n3 + n5];
            }
            if (this.usePadding) {
                for (n5 = this.SYS_N / 8 - 1; n5 >= this.SYS_N / 8 - this.PK_ROW_BYTES; --n5) {
                    sArray[n5] = (short)(((sArray[n5] & 0xFF) << n4 | (sArray[n5 - 1] & 0xFF) >>> 8 - n4) & 0xFF);
                }
            }
            int n6 = n2 / 8;
            sArray[n6] = (short)(sArray[n6] | 1 << n2 % 8);
            int n7 = 0;
            for (n5 = 0; n5 < this.SYS_N / 8; ++n5) {
                n7 = (byte)(n7 ^ sArray[n5] & byArray3[n5]);
            }
            n7 = (byte)(n7 ^ n7 >>> 4);
            n7 = (byte)(n7 ^ n7 >>> 2);
            n7 = (byte)(n7 ^ n7 >>> 1);
            n7 = (byte)(n7 & 1);
            int n8 = n2 / 8;
            byArray[n8] = (byte)(byArray[n8] | n7 << n2 % 8);
            n3 += this.PK_ROW_BYTES;
        }
    }

    private void generate_error_vector(byte[] byArray, SecureRandom secureRandom) {
        int n2;
        int n3;
        short s2;
        short[] sArray = new short[this.SYS_T * 2];
        short[] sArray2 = new short[this.SYS_T];
        byte[] byArray2 = new byte[this.SYS_T];
        while (true) {
            byte[] byArray3;
            if (this.countErrorIndices) {
                byArray3 = new byte[this.SYS_T * 4];
                secureRandom.nextBytes(byArray3);
                for (s2 = 0; s2 < this.SYS_T * 2; ++s2) {
                    sArray[s2] = Utils.load_gf(byArray3, s2 * 2, this.GFMASK);
                }
                s2 = 0;
                for (n3 = 0; n3 < this.SYS_T * 2 && s2 < this.SYS_T; ++n3) {
                    if (sArray[n3] >= this.SYS_N) continue;
                    sArray2[s2++] = sArray[n3];
                }
                if (s2 < this.SYS_T) {
                    continue;
                }
            } else {
                byArray3 = new byte[this.SYS_T * 2];
                secureRandom.nextBytes(byArray3);
                for (s2 = 0; s2 < this.SYS_T; ++s2) {
                    sArray2[s2] = Utils.load_gf(byArray3, s2 * 2, this.GFMASK);
                }
            }
            s2 = 0;
            block4: for (n3 = 1; n3 < this.SYS_T && s2 != 1; ++n3) {
                for (n2 = 0; n2 < n3; ++n2) {
                    if (sArray2[n3] != sArray2[n2]) continue;
                    s2 = 1;
                    continue block4;
                }
            }
            if (s2 == 0) break;
        }
        for (s2 = 0; s2 < this.SYS_T; ++s2) {
            byArray2[s2] = (byte)(1 << (sArray2[s2] & 7));
        }
        for (s2 = 0; s2 < this.SYS_N / 8; s2 = (short)((short)(s2 + 1))) {
            byArray[s2] = 0;
            for (n3 = 0; n3 < this.SYS_T; ++n3) {
                n2 = CMCEEngine.same_mask32(s2, (short)(sArray2[n3] >> 3));
                n2 = (short)(n2 & 0xFF);
                short s3 = s2;
                byArray[s3] = (byte)(byArray[s3] | byArray2[n3] & n2);
            }
        }
    }

    private void encrypt(byte[] byArray, byte[] byArray2, byte[] byArray3, SecureRandom secureRandom) {
        this.generate_error_vector(byArray3, secureRandom);
        this.syndrome(byArray, byArray2, byArray3);
    }

    public int kem_enc(byte[] byArray, byte[] byArray2, byte[] byArray3, SecureRandom secureRandom) {
        byte[] byArray4 = new byte[this.SYS_N / 8];
        int n2 = 0;
        if (this.usePadding) {
            n2 = this.check_pk_padding(byArray3);
        }
        this.encrypt(byArray, byArray3, byArray4, secureRandom);
        SHAKEDigest sHAKEDigest = new SHAKEDigest(256);
        sHAKEDigest.update((byte)1);
        sHAKEDigest.update(byArray4, 0, byArray4.length);
        sHAKEDigest.update(byArray, 0, byArray.length);
        sHAKEDigest.doFinal(byArray2, 0, byArray2.length);
        if (this.usePadding) {
            byte by = (byte)n2;
            by = (byte)(by ^ 0xFF);
            int n3 = 0;
            while (n3 < this.SYND_BYTES) {
                int n4 = n3++;
                byArray[n4] = (byte)(byArray[n4] & by);
            }
            n3 = 0;
            while (n3 < 32) {
                int n5 = n3++;
                byArray2[n5] = (byte)(byArray2[n5] & by);
            }
            return n2;
        }
        return 0;
    }

    public int kem_dec(byte[] byArray, byte[] byArray2, byte[] byArray3) {
        int n2;
        byte[] byArray4 = new byte[this.SYS_N / 8];
        byte[] byArray5 = new byte[1 + this.SYS_N / 8 + this.SYND_BYTES];
        int n3 = 0;
        if (this.usePadding) {
            n3 = this.check_c_padding(byArray2);
        }
        byte by = (byte)this.decrypt(byArray4, byArray3, byArray2);
        short s2 = by;
        s2 = (short)(s2 - 1);
        s2 = (short)(s2 >> 8);
        s2 = (short)(s2 & 0xFF);
        byArray5[0] = (byte)(s2 & 1);
        for (n2 = 0; n2 < this.SYS_N / 8; ++n2) {
            byArray5[1 + n2] = (byte)(~s2 & byArray3[n2 + 40 + this.IRR_BYTES + this.COND_BYTES] | s2 & byArray4[n2]);
        }
        for (n2 = 0; n2 < this.SYND_BYTES; ++n2) {
            byArray5[1 + this.SYS_N / 8 + n2] = byArray2[n2];
        }
        SHAKEDigest sHAKEDigest = new SHAKEDigest(256);
        sHAKEDigest.update(byArray5, 0, byArray5.length);
        sHAKEDigest.doFinal(byArray, 0, byArray.length);
        if (this.usePadding) {
            byte by2 = (byte)n3;
            n2 = 0;
            while (n2 < byArray.length) {
                int n4 = n2++;
                byArray[n4] = (byte)(byArray[n4] | by2);
            }
            return n3;
        }
        return 0;
    }

    private int decrypt(byte[] byArray, byte[] byArray2, byte[] byArray3) {
        int n2;
        int n3;
        short[] sArray = new short[this.SYS_T + 1];
        short[] sArray2 = new short[this.SYS_N];
        short[] sArray3 = new short[this.SYS_T * 2];
        short[] sArray4 = new short[this.SYS_T * 2];
        short[] sArray5 = new short[this.SYS_T + 1];
        short[] sArray6 = new short[this.SYS_N];
        byte[] byArray4 = new byte[this.SYS_N / 8];
        for (n3 = 0; n3 < this.SYND_BYTES; ++n3) {
            byArray4[n3] = byArray3[n3];
        }
        for (n3 = this.SYND_BYTES; n3 < this.SYS_N / 8; ++n3) {
            byArray4[n3] = 0;
        }
        for (n3 = 0; n3 < this.SYS_T; ++n3) {
            sArray[n3] = Utils.load_gf(byArray2, 40 + n3 * 2, this.GFMASK);
        }
        sArray[this.SYS_T] = 1;
        this.benes.support_gen(sArray2, byArray2);
        this.synd(sArray3, sArray, sArray2, byArray4);
        this.bm(sArray5, sArray3);
        this.root(sArray6, sArray5, sArray2);
        for (n3 = 0; n3 < this.SYS_N / 8; ++n3) {
            byArray[n3] = 0;
        }
        n3 = 0;
        for (n2 = 0; n2 < this.SYS_N; ++n2) {
            short s2 = (short)(this.gf.gf_iszero(sArray6[n2]) & 1);
            int n4 = n2 / 8;
            byArray[n4] = (byte)(byArray[n4] | s2 << n2 % 8);
            n3 += s2;
        }
        this.synd(sArray4, sArray, sArray2, byArray);
        n2 = n3;
        n2 ^= this.SYS_T;
        for (int i2 = 0; i2 < this.SYS_T * 2; ++i2) {
            n2 |= sArray3[i2] ^ sArray4[i2];
        }
        --n2;
        n2 >>= 15;
        if (((n2 &= 1) ^ 1) != 0) {
            // empty if block
        }
        return n2 ^ 1;
    }

    private static int min(short s2, int n2) {
        if (s2 < n2) {
            return s2;
        }
        return n2;
    }

    private void bm(short[] sArray, short[] sArray2) {
        int n2;
        short s2 = 0;
        int n3 = 0;
        short[] sArray3 = new short[this.SYS_T + 1];
        short[] sArray4 = new short[this.SYS_T + 1];
        short[] sArray5 = new short[this.SYS_T + 1];
        short s3 = 1;
        for (n2 = 0; n2 < this.SYS_T + 1; ++n2) {
            sArray5[n2] = 0;
            sArray4[n2] = 0;
        }
        sArray4[0] = 1;
        sArray5[1] = 1;
        for (s2 = 0; s2 < 2 * this.SYS_T; s2 = (short)(s2 + 1)) {
            short s4;
            int n4;
            n2 = 0;
            for (n4 = 0; n4 <= CMCEEngine.min(s2, this.SYS_T); ++n4) {
                n2 ^= this.gf.gf_mul_ext(sArray4[n4], sArray2[s2 - n4]);
            }
            short s5 = s4 = this.gf.gf_reduce(n2);
            s5 = (short)(s5 - 1);
            s5 = (short)(s5 >> 15);
            s5 = (short)(s5 & 1);
            s5 = (short)(s5 - 1);
            short s6 = s2;
            s6 = (short)(s6 - 2 * n3);
            s6 = (short)(s6 >> 15);
            s6 = (short)(s6 & 1);
            s6 = (short)(s6 - 1);
            s6 = (short)(s6 & s5);
            for (n4 = 0; n4 <= this.SYS_T; ++n4) {
                sArray3[n4] = sArray4[n4];
            }
            short s7 = this.gf.gf_frac(s3, s4);
            for (n4 = 0; n4 <= this.SYS_T; ++n4) {
                int n5 = n4;
                sArray4[n5] = (short)(sArray4[n5] ^ this.gf.gf_mul(s7, sArray5[n4]) & s5);
            }
            n3 = (short)(n3 & ~s6 | s2 + 1 - n3 & s6);
            for (n4 = this.SYS_T - 1; n4 >= 0; --n4) {
                sArray5[n4 + 1] = (short)(sArray5[n4] & ~s6 | sArray3[n4] & s6);
            }
            sArray5[0] = 0;
            s3 = (short)(s3 & ~s6 | s4 & s6);
        }
        for (n2 = 0; n2 <= this.SYS_T; ++n2) {
            sArray[n2] = sArray4[this.SYS_T - n2];
        }
    }

    private void synd(short[] sArray, short[] sArray2, short[] sArray3, byte[] byArray) {
        short s2;
        short s3;
        int n2 = byArray[0] & 1;
        short s4 = sArray3[0];
        short s5 = this.eval(sArray2, s4);
        short s6 = this.gf.gf_inv(this.gf.gf_sq(s5));
        sArray[0] = s3 = (short)(s6 & -n2);
        for (s2 = 1; s2 < 2 * this.SYS_T; ++s2) {
            sArray[s2] = s3 = this.gf.gf_mul(s3, s4);
        }
        for (n2 = 1; n2 < this.SYS_N; ++n2) {
            s4 = (short)(byArray[n2 / 8] >> n2 % 8 & 1);
            s5 = sArray3[n2];
            s6 = this.eval(sArray2, s5);
            s3 = this.gf.gf_inv(this.gf.gf_sq(s6));
            s2 = this.gf.gf_mul(s3, s4);
            sArray[0] = (short)(sArray[0] ^ s2);
            int n3 = 1;
            while (n3 < 2 * this.SYS_T) {
                s2 = this.gf.gf_mul(s2, s5);
                int n4 = n3++;
                sArray[n4] = (short)(sArray[n4] ^ s2);
            }
        }
    }

    private int mov_columns(byte[][] byArray, short[] sArray, long[] lArray) {
        long l2;
        int n2;
        long l3;
        int n3;
        int n4;
        long[] lArray2 = new long[64];
        long[] lArray3 = new long[32];
        long l4 = 1L;
        byte[] byArray2 = new byte[9];
        int n5 = this.PK_NROWS - 32;
        int n6 = n5 / 8;
        int n7 = n5 % 8;
        if (this.usePadding) {
            for (n4 = 0; n4 < 32; ++n4) {
                for (n3 = 0; n3 < 9; ++n3) {
                    byArray2[n3] = byArray[n5 + n4][n6 + n3];
                }
                for (n3 = 0; n3 < 8; ++n3) {
                    byArray2[n3] = (byte)((byArray2[n3] & 0xFF) >> n7 | byArray2[n3 + 1] << 8 - n7);
                }
                lArray2[n4] = Utils.load8(byArray2, 0);
            }
        } else {
            for (n4 = 0; n4 < 32; ++n4) {
                lArray2[n4] = Utils.load8(byArray[n5 + n4], n6);
            }
        }
        lArray[0] = 0L;
        for (n4 = 0; n4 < 32; ++n4) {
            long l5;
            l3 = lArray2[n4];
            for (n3 = n4 + 1; n3 < 32; ++n3) {
                l3 |= lArray2[n3];
            }
            if (l3 == 0L) {
                return -1;
            }
            int n8 = CMCEEngine.ctz(l3);
            lArray3[n4] = n8;
            lArray[0] = lArray[0] | l4 << (int)lArray3[n4];
            for (n3 = n4 + 1; n3 < 32; ++n3) {
                l5 = lArray2[n4] >> n8 & 1L;
                int n9 = n4;
                lArray2[n9] = lArray2[n9] ^ lArray2[n3] & --l5;
            }
            n3 = n4 + 1;
            while (n3 < 32) {
                l5 = lArray2[n3] >> n8 & 1L;
                l5 = -l5;
                int n10 = n3++;
                lArray2[n10] = lArray2[n10] ^ lArray2[n4] & l5;
            }
        }
        for (n3 = 0; n3 < 32; ++n3) {
            for (n2 = n3 + 1; n2 < 64; ++n2) {
                l2 = sArray[n5 + n3] ^ sArray[n5 + n2];
                int n11 = n5 + n3;
                sArray[n11] = (short)((long)sArray[n11] ^ (l2 &= CMCEEngine.same_mask64((short)n2, (short)lArray3[n3])));
                int n12 = n5 + n2;
                sArray[n12] = (short)((long)sArray[n12] ^ l2);
            }
        }
        for (n4 = 0; n4 < this.PK_NROWS; ++n4) {
            if (this.usePadding) {
                for (n2 = 0; n2 < 9; ++n2) {
                    byArray2[n2] = byArray[n4][n6 + n2];
                }
                for (n2 = 0; n2 < 8; ++n2) {
                    byArray2[n2] = (byte)((byArray2[n2] & 0xFF) >> n7 | byArray2[n2 + 1] << 8 - n7);
                }
                l3 = Utils.load8(byArray2, 0);
            } else {
                l3 = Utils.load8(byArray[n4], n6);
            }
            for (n3 = 0; n3 < 32; ++n3) {
                l2 = l3 >> n3;
                l2 ^= l3 >> (int)lArray3[n3];
                l3 ^= (l2 &= 1L) << (int)lArray3[n3];
                l3 ^= l2 << n3;
            }
            if (this.usePadding) {
                Utils.store8(byArray2, 0, l3);
                byArray[n4][n6 + 8] = (byte)((byArray[n4][n6 + 8] & 0xFF) >>> n7 << n7 | (byArray2[7] & 0xFF) >>> 8 - n7);
                byArray[n4][n6 + 0] = (byte)((byArray2[0] & 0xFF) << n7 | (byArray[n4][n6] & 0xFF) << 8 - n7 >>> 8 - n7);
                for (n2 = 7; n2 >= 1; --n2) {
                    byArray[n4][n6 + n2] = (byte)((byArray2[n2] & 0xFF) << n7 | (byArray2[n2 - 1] & 0xFF) >>> 8 - n7);
                }
                continue;
            }
            Utils.store8(byArray[n4], n6, l3);
        }
        return 0;
    }

    private static int ctz(long l2) {
        long l3 = 0x101010101010101L;
        long l4 = 0L;
        long l5 = l2 ^ 0xFFFFFFFFFFFFFFFFL;
        for (int i2 = 0; i2 < 8; ++i2) {
            l4 += (l3 &= l5 >>> i2);
        }
        long l6 = l4 & 0x808080808080808L;
        l6 |= l6 >>> 1;
        l6 |= l6 >>> 2;
        long l7 = l4;
        l7 += (l4 >>>= 8) & l6;
        for (int i3 = 2; i3 < 8; ++i3) {
            l6 &= l6 >>> 8;
            l7 += (l4 >>>= 8) & l6;
        }
        return (int)l7 & 0xFF;
    }

    private static long same_mask64(short s2, short s3) {
        long l2 = s2 ^ s3;
        --l2;
        l2 >>>= 63;
        l2 = -l2;
        return l2;
    }

    private static byte same_mask32(short s2, short s3) {
        int n2 = s2 ^ s3;
        --n2;
        n2 >>>= 31;
        n2 = -n2;
        return (byte)(n2 & 0xFF);
    }

    private static void layer(short[] sArray, byte[] byArray, int n2, int n3, int n4) {
        int n5 = 1 << n3;
        int n6 = 0;
        for (int i2 = 0; i2 < n4; i2 += n5 * 2) {
            for (int i3 = 0; i3 < n5; ++i3) {
                int n7 = sArray[i2 + i3] ^ sArray[i2 + i3 + n5];
                int n8 = byArray[n2 + (n6 >> 3)] >> (n6 & 7) & 1;
                n8 = -n8;
                int n9 = i2 + i3;
                sArray[n9] = (short)(sArray[n9] ^ (n7 &= n8));
                int n10 = i2 + i3 + n5;
                sArray[n10] = (short)(sArray[n10] ^ n7);
                ++n6;
            }
        }
    }

    private static void controlbitsfrompermutation(byte[] byArray, short[] sArray, long l2, long l3) {
        int n2;
        int[] nArray = new int[(int)(2L * l3)];
        short[] sArray2 = new short[(int)l3];
        do {
            int n3 = 0;
            while ((long)n3 < ((2L * l2 - 1L) * l3 / 2L + 7L) / 8L) {
                byArray[n3] = 0;
                ++n3;
            }
            CMCEEngine.cbrecursion(byArray, 0L, 1L, sArray, 0, l2, l3, nArray);
            n3 = 0;
            while ((long)n3 < l3) {
                sArray2[n3] = (short)n3;
                ++n3;
            }
            int n4 = 0;
            n3 = 0;
            while ((long)n3 < l2) {
                CMCEEngine.layer(sArray2, byArray, n4, n3, (int)l3);
                n4 = (int)((long)n4 + (l3 >> 4));
                ++n3;
            }
            for (n3 = (int)(l2 - 2L); n3 >= 0; --n3) {
                CMCEEngine.layer(sArray2, byArray, n4, n3, (int)l3);
                n4 = (int)((long)n4 + (l3 >> 4));
            }
            n2 = 0;
            n3 = 0;
            while ((long)n3 < l3) {
                n2 = (short)(n2 | sArray[n3] ^ sArray2[n3]);
                ++n3;
            }
        } while (n2 != 0);
    }

    static short get_q_short(int[] nArray, int n2) {
        int n3 = n2 / 2;
        if (n2 % 2 == 0) {
            return (short)nArray[n3];
        }
        return (short)((nArray[n3] & 0xFFFF0000) >> 16);
    }

    static void cbrecursion(byte[] byArray, long l2, long l3, short[] sArray, int n2, long l4, long l5, int[] nArray) {
        int n3;
        int n4;
        long l6;
        long l7;
        int n5;
        int n6;
        int n7;
        long l8;
        if (l4 == 1L) {
            int n8 = (int)(l2 >> 3);
            byArray[n8] = (byte)(byArray[n8] ^ CMCEEngine.get_q_short(nArray, n2) << (int)(l2 & 7L));
            return;
        }
        if (sArray != null) {
            for (l8 = 0L; l8 < l5; ++l8) {
                nArray[(int)l8] = (sArray[(int)l8] ^ 1) << 16 | sArray[(int)(l8 ^ 1L)];
            }
        } else {
            for (l8 = 0L; l8 < l5; ++l8) {
                nArray[(int)l8] = (CMCEEngine.get_q_short(nArray, (int)((long)n2 + l8)) ^ 1) << 16 | CMCEEngine.get_q_short(nArray, (int)((long)n2 + (l8 ^ 1L)));
            }
        }
        CMCEEngine.sort32(nArray, 0, (int)l5);
        for (l8 = 0L; l8 < l5; ++l8) {
            n7 = nArray[(int)l8];
            n6 = n7 & 0xFFFF;
            n5 = n6;
            if (l8 < (long)n5) {
                n5 = (int)l8;
            }
            nArray[(int)(l5 + l8)] = n6 << 16 | n5;
        }
        for (l8 = 0L; l8 < l5; ++l8) {
            nArray[(int)l8] = (int)((long)(nArray[(int)l8] << 16) | l8);
        }
        CMCEEngine.sort32(nArray, 0, (int)l5);
        for (l8 = 0L; l8 < l5; ++l8) {
            nArray[(int)l8] = (nArray[(int)l8] << 16) + (nArray[(int)(l5 + l8)] >> 16);
        }
        CMCEEngine.sort32(nArray, 0, (int)l5);
        if (l4 <= 10L) {
            for (l8 = 0L; l8 < l5; ++l8) {
                nArray[(int)(l5 + l8)] = (nArray[(int)l8] & 0xFFFF) << 10 | nArray[(int)(l5 + l8)] & 0x3FF;
            }
            for (l7 = 1L; l7 < l4 - 1L; ++l7) {
                for (l8 = 0L; l8 < l5; ++l8) {
                    nArray[(int)l8] = (int)((long)((nArray[(int)(l5 + l8)] & 0xFFFFFC00) << 6) | l8);
                }
                CMCEEngine.sort32(nArray, 0, (int)l5);
                for (l8 = 0L; l8 < l5; ++l8) {
                    nArray[(int)l8] = nArray[(int)l8] << 20 | nArray[(int)(l5 + l8)];
                }
                CMCEEngine.sort32(nArray, 0, (int)l5);
                for (l8 = 0L; l8 < l5; ++l8) {
                    n7 = nArray[(int)l8] & 0xFFFFF;
                    n6 = nArray[(int)l8] & 0xFFC00 | nArray[(int)(l5 + l8)] & 0x3FF;
                    if (n7 < n6) {
                        n6 = n7;
                    }
                    nArray[(int)(l5 + l8)] = n6;
                }
            }
            for (l8 = 0L; l8 < l5; ++l8) {
                int n9 = (int)(l5 + l8);
                nArray[n9] = nArray[n9] & 0x3FF;
            }
        } else {
            for (l8 = 0L; l8 < l5; ++l8) {
                nArray[(int)(l5 + l8)] = nArray[(int)l8] << 16 | nArray[(int)(l5 + l8)] & 0xFFFF;
            }
            for (l7 = 1L; l7 < l4 - 1L; ++l7) {
                for (l8 = 0L; l8 < l5; ++l8) {
                    nArray[(int)l8] = (int)((long)(nArray[(int)(l5 + l8)] & 0xFFFF0000) | l8);
                }
                CMCEEngine.sort32(nArray, 0, (int)l5);
                for (l8 = 0L; l8 < l5; ++l8) {
                    nArray[(int)l8] = nArray[(int)l8] << 16 | nArray[(int)(l5 + l8)] & 0xFFFF;
                }
                if (l7 < l4 - 2L) {
                    for (l8 = 0L; l8 < l5; ++l8) {
                        nArray[(int)(l5 + l8)] = nArray[(int)l8] & 0xFFFF0000 | nArray[(int)(l5 + l8)] >> 16;
                    }
                    CMCEEngine.sort32(nArray, (int)l5, (int)(l5 * 2L));
                    for (l8 = 0L; l8 < l5; ++l8) {
                        nArray[(int)(l5 + l8)] = nArray[(int)(l5 + l8)] << 16 | nArray[(int)l8] & 0xFFFF;
                    }
                }
                CMCEEngine.sort32(nArray, 0, (int)l5);
                for (l8 = 0L; l8 < l5; ++l8) {
                    n7 = nArray[(int)(l5 + l8)] & 0xFFFF0000 | nArray[(int)l8] & 0xFFFF;
                    if (n7 >= nArray[(int)(l5 + l8)]) continue;
                    nArray[(int)(l5 + l8)] = n7;
                }
            }
            for (l8 = 0L; l8 < l5; ++l8) {
                int n10 = (int)(l5 + l8);
                nArray[n10] = nArray[n10] & 0xFFFF;
            }
        }
        if (sArray != null) {
            for (l8 = 0L; l8 < l5; ++l8) {
                nArray[(int)l8] = (int)((long)(sArray[(int)l8] << 16) + l8);
            }
        } else {
            for (l8 = 0L; l8 < l5; ++l8) {
                nArray[(int)l8] = (int)((long)(CMCEEngine.get_q_short(nArray, (int)((long)n2 + l8)) << 16) + l8);
            }
        }
        CMCEEngine.sort32(nArray, 0, (int)l5);
        for (l6 = 0L; l6 < l5 / 2L; ++l6) {
            long l9 = 2L * l6;
            n5 = nArray[(int)(l5 + l9)] & 1;
            n4 = (int)(l9 + (long)n5);
            n3 = n4 ^ 1;
            int n11 = (int)(l2 >> 3);
            byArray[n11] = (byte)(byArray[n11] ^ n5 << (int)(l2 & 7L));
            l2 += l3;
            nArray[(int)(l5 + l9)] = nArray[(int)l9] << 16 | n4;
            nArray[(int)(l5 + l9 + 1L)] = nArray[(int)(l9 + 1L)] << 16 | n3;
        }
        CMCEEngine.sort32(nArray, (int)l5, (int)(l5 * 2L));
        l2 += (2L * l4 - 3L) * l3 * (l5 / 2L);
        for (long i2 = 0L; i2 < l5 / 2L; ++i2) {
            long l10 = 2L * i2;
            n5 = nArray[(int)(l5 + l10)] & 1;
            n4 = (int)(l10 + (long)n5);
            n3 = n4 ^ 1;
            int n12 = (int)(l2 >> 3);
            byArray[n12] = (byte)(byArray[n12] ^ n5 << (int)(l2 & 7L));
            l2 += l3;
            nArray[(int)l10] = n4 << 16 | nArray[(int)(l5 + l10)] & 0xFFFF;
            nArray[(int)(l10 + 1L)] = n3 << 16 | nArray[(int)(l5 + l10 + 1L)] & 0xFFFF;
        }
        CMCEEngine.sort32(nArray, 0, (int)l5);
        l2 -= (2L * l4 - 2L) * l3 * (l5 / 2L);
        short[] sArray2 = new short[(int)l5 * 4];
        for (l7 = 0L; l7 < l5 * 2L; ++l7) {
            sArray2[(int)(l7 * 2L + 0L)] = (short)nArray[(int)l7];
            sArray2[(int)(l7 * 2L + 1L)] = (short)((nArray[(int)l7] & 0xFFFF0000) >> 16);
        }
        for (l6 = 0L; l6 < l5 / 2L; ++l6) {
            sArray2[(int)l6] = (short)((nArray[(int)(2L * l6)] & 0xFFFF) >>> 1);
            sArray2[(int)(l6 + l5 / 2L)] = (short)((nArray[(int)(2L * l6 + 1L)] & 0xFFFF) >>> 1);
        }
        for (l7 = 0L; l7 < l5 / 2L; ++l7) {
            nArray[(int)(l5 + l5 / 4L + l7)] = sArray2[(int)(l7 * 2L + 1L)] << 16 | sArray2[(int)(l7 * 2L)];
        }
        CMCEEngine.cbrecursion(byArray, l2, l3 * 2L, null, (int)(l5 + l5 / 4L) * 2, l4 - 1L, l5 / 2L, nArray);
        CMCEEngine.cbrecursion(byArray, l2 + l3, l3 * 2L, null, (int)((l5 + l5 / 4L) * 2L + l5 / 2L), l4 - 1L, l5 / 2L, nArray);
    }

    private int pk_gen(byte[] byArray, byte[] byArray2, int[] nArray, short[] sArray, long[] lArray) {
        block26: {
            int n2;
            int n3;
            int n4;
            short[] sArray2 = new short[this.SYS_T + 1];
            sArray2[this.SYS_T] = 1;
            for (n4 = 0; n4 < this.SYS_T; ++n4) {
                sArray2[n4] = Utils.load_gf(byArray2, 40 + n4 * 2, this.GFMASK);
            }
            long[] lArray2 = new long[1 << this.GFBITS];
            n4 = 0;
            while (n4 < 1 << this.GFBITS) {
                lArray2[n4] = nArray[n4];
                int n5 = n4;
                lArray2[n5] = lArray2[n5] << 31;
                int n6 = n4;
                lArray2[n6] = lArray2[n6] | (long)n4;
                int n7 = n4++;
                lArray2[n7] = lArray2[n7] & Long.MAX_VALUE;
            }
            CMCEEngine.sort64(lArray2, 0, lArray2.length);
            for (n4 = 1; n4 < 1 << this.GFBITS; ++n4) {
                if (lArray2[n4 - 1] >> 31 != lArray2[n4] >> 31) continue;
                return -1;
            }
            short[] sArray3 = new short[this.SYS_N];
            for (n4 = 0; n4 < 1 << this.GFBITS; ++n4) {
                sArray[n4] = (short)(lArray2[n4] & (long)this.GFMASK);
            }
            for (n4 = 0; n4 < this.SYS_N; ++n4) {
                sArray3[n4] = Utils.bitrev(sArray[n4], this.GFBITS);
            }
            short[] sArray4 = new short[this.SYS_N];
            this.root(sArray4, sArray2, sArray3);
            for (n4 = 0; n4 < this.SYS_N; ++n4) {
                sArray4[n4] = this.gf.gf_inv(sArray4[n4]);
            }
            byte[][] byArray3 = new byte[this.PK_NROWS][this.SYS_N / 8];
            for (n4 = 0; n4 < this.PK_NROWS; ++n4) {
                for (n3 = 0; n3 < this.SYS_N / 8; ++n3) {
                    byArray3[n4][n3] = 0;
                }
            }
            for (n4 = 0; n4 < this.SYS_T; ++n4) {
                for (n3 = 0; n3 < this.SYS_N; n3 += 8) {
                    for (n2 = 0; n2 < this.GFBITS; ++n2) {
                        byte by = (byte)(sArray4[n3 + 7] >>> n2 & 1);
                        by = (byte)(by << 1);
                        by = (byte)(by | sArray4[n3 + 6] >>> n2 & 1);
                        by = (byte)(by << 1);
                        by = (byte)(by | sArray4[n3 + 5] >>> n2 & 1);
                        by = (byte)(by << 1);
                        by = (byte)(by | sArray4[n3 + 4] >>> n2 & 1);
                        by = (byte)(by << 1);
                        by = (byte)(by | sArray4[n3 + 3] >>> n2 & 1);
                        by = (byte)(by << 1);
                        by = (byte)(by | sArray4[n3 + 2] >>> n2 & 1);
                        by = (byte)(by << 1);
                        by = (byte)(by | sArray4[n3 + 1] >>> n2 & 1);
                        by = (byte)(by << 1);
                        byArray3[n4 * this.GFBITS + n2][n3 / 8] = by = (byte)(by | sArray4[n3 + 0] >>> n2 & 1);
                    }
                }
                for (n3 = 0; n3 < this.SYS_N; ++n3) {
                    sArray4[n3] = this.gf.gf_mul(sArray4[n3], sArray3[n3]);
                }
            }
            for (int i2 = 0; i2 < this.PK_NROWS; ++i2) {
                int n8;
                byte by;
                n4 = i2 >>> 3;
                n3 = i2 & 7;
                if (this.usePivots && i2 == this.PK_NROWS - 32 && this.mov_columns(byArray3, sArray, lArray) != 0) {
                    return -1;
                }
                for (n2 = i2 + 1; n2 < this.PK_NROWS; ++n2) {
                    by = (byte)(byArray3[i2][n4] ^ byArray3[n2][n4]);
                    by = (byte)(by >> n3);
                    by = (byte)(by & 1);
                    by = -by;
                    for (n8 = 0; n8 < this.SYS_N / 8; ++n8) {
                        byte[] byArray4 = byArray3[i2];
                        int n9 = n8;
                        byArray4[n9] = (byte)(byArray4[n9] ^ byArray3[n2][n8] & by);
                    }
                }
                if ((byArray3[i2][n4] >> n3 & 1) == 0) {
                    return -1;
                }
                for (n2 = 0; n2 < this.PK_NROWS; ++n2) {
                    if (n2 == i2) continue;
                    by = (byte)(byArray3[n2][n4] >> n3);
                    by = (byte)(by & 1);
                    by = -by;
                    for (n8 = 0; n8 < this.SYS_N / 8; ++n8) {
                        byte[] byArray5 = byArray3[n2];
                        int n10 = n8;
                        byArray5[n10] = (byte)(byArray5[n10] ^ byArray3[i2][n8] & by);
                    }
                }
            }
            if (byArray == null) break block26;
            if (this.usePadding) {
                int n11 = 0;
                int n12 = this.PK_NROWS % 8;
                if (n12 == 0) {
                    System.arraycopy(byArray3[n4], (this.PK_NROWS - 1) / 8, byArray, n11, this.SYS_N / 8);
                    n11 += this.SYS_N / 8;
                } else {
                    for (n4 = 0; n4 < this.PK_NROWS; ++n4) {
                        for (n3 = (this.PK_NROWS - 1) / 8; n3 < this.SYS_N / 8 - 1; ++n3) {
                            byArray[n11++] = (byte)((byArray3[n4][n3] & 0xFF) >>> n12 | byArray3[n4][n3 + 1] << 8 - n12);
                        }
                        byArray[n11++] = (byte)((byArray3[n4][n3] & 0xFF) >>> n12);
                    }
                }
            } else {
                int n13 = (this.SYS_N - this.PK_NROWS + 7) / 8;
                for (n4 = 0; n4 < this.PK_NROWS; ++n4) {
                    System.arraycopy(byArray3[n4], this.PK_NROWS / 8, byArray, n13 * n4, n13);
                }
            }
        }
        return 0;
    }

    private short eval(short[] sArray, short s2) {
        short s3 = sArray[this.SYS_T];
        for (int i2 = this.SYS_T - 1; i2 >= 0; --i2) {
            s3 = (short)(this.gf.gf_mul(s3, s2) ^ sArray[i2]);
        }
        return s3;
    }

    private void root(short[] sArray, short[] sArray2, short[] sArray3) {
        for (int i2 = 0; i2 < this.SYS_N; ++i2) {
            sArray[i2] = this.eval(sArray2, sArray3[i2]);
        }
    }

    private int generate_irr_poly(short[] sArray) {
        short s2;
        short[][] sArray2 = new short[this.SYS_T + 1][this.SYS_T];
        sArray2[0][0] = 1;
        System.arraycopy(sArray, 0, sArray2[1], 0, this.SYS_T);
        int[] nArray = new int[this.SYS_T * 2 - 1];
        for (s2 = 2; s2 < this.SYS_T; s2 += 2) {
            this.gf.gf_sqr_poly(this.SYS_T, this.poly, sArray2[s2], sArray2[s2 >>> 1], nArray);
            this.gf.gf_mul_poly(this.SYS_T, this.poly, sArray2[s2 + 1], sArray2[s2], sArray, nArray);
        }
        if (s2 == this.SYS_T) {
            this.gf.gf_sqr_poly(this.SYS_T, this.poly, sArray2[s2], sArray2[s2 >>> 1], nArray);
        }
        for (int i2 = 0; i2 < this.SYS_T; ++i2) {
            int n2;
            int n3;
            for (s2 = i2 + 1; s2 < this.SYS_T; ++s2) {
                n3 = this.gf.gf_iszero(sArray2[i2][i2]);
                for (n2 = i2; n2 < this.SYS_T + 1; ++n2) {
                    short[] sArray3 = sArray2[n2];
                    int n4 = i2;
                    sArray3[n4] = (short)(sArray3[n4] ^ (short)(sArray2[n2][s2] & n3));
                }
            }
            if (sArray2[i2][i2] == 0) {
                return -1;
            }
            s2 = this.gf.gf_inv(sArray2[i2][i2]);
            for (n3 = i2; n3 < this.SYS_T + 1; ++n3) {
                sArray2[n3][i2] = this.gf.gf_mul(sArray2[n3][i2], s2);
            }
            for (n3 = 0; n3 < this.SYS_T; ++n3) {
                if (n3 == i2) continue;
                n2 = sArray2[i2][n3];
                for (int i3 = i2; i3 <= this.SYS_T; ++i3) {
                    short[] sArray4 = sArray2[i3];
                    int n5 = n3;
                    sArray4[n5] = (short)(sArray4[n5] ^ this.gf.gf_mul(sArray2[i3][i2], (short)n2));
                }
            }
        }
        System.arraycopy(sArray2[this.SYS_T], 0, sArray, 0, this.SYS_T);
        return 0;
    }

    int check_pk_padding(byte[] byArray) {
        int n2 = 0;
        for (int i2 = 0; i2 < this.PK_NROWS; ++i2) {
            n2 = (byte)(n2 | byArray[i2 * this.PK_ROW_BYTES + this.PK_ROW_BYTES - 1]);
        }
        n2 = (byte)((n2 & 0xFF) >>> this.PK_NCOLS % 8);
        n2 = (byte)(n2 - 1);
        int n3 = n2 = (int)((byte)((n2 & 0xFF) >>> 7));
        return n3 - 1;
    }

    int check_c_padding(byte[] byArray) {
        byte by = (byte)((byArray[this.SYND_BYTES - 1] & 0xFF) >>> this.PK_NROWS % 8);
        by = (byte)(by - 1);
        byte by2 = by = (byte)((by & 0xFF) >>> 7);
        return by2 - 1;
    }

    public int getDefaultSessionKeySize() {
        return this.defaultKeySize;
    }

    private static void sort32(int[] nArray, int n2, int n3) {
        int n4 = n3 - n2;
        if (n4 < 2) {
            return;
        }
        for (int i2 = 1; i2 < n4 - i2; i2 += i2) {
        }
        for (int i3 = i2; i3 > 0; i3 >>>= 1) {
            int n5;
            int n6;
            int n7;
            for (n7 = 0; n7 < n4 - i3; ++n7) {
                if ((n7 & i3) != 0) continue;
                n6 = nArray[n2 + n7 + i3] ^ nArray[n2 + n7];
                n5 = nArray[n2 + n7 + i3] - nArray[n2 + n7];
                n5 ^= n6 & (n5 ^ nArray[n2 + n7 + i3]);
                n5 >>= 31;
                int n8 = n2 + n7;
                nArray[n8] = nArray[n8] ^ (n5 &= n6);
                int n9 = n2 + n7 + i3;
                nArray[n9] = nArray[n9] ^ n5;
            }
            n7 = 0;
            for (int i4 = i2; i4 > i3; i4 >>>= 1) {
                while (n7 < n4 - i4) {
                    if ((n7 & i3) == 0) {
                        n6 = nArray[n2 + n7 + i3];
                        for (int i5 = i4; i5 > i3; i5 >>>= 1) {
                            n5 = nArray[n2 + n7 + i5] ^ n6;
                            int n10 = nArray[n2 + n7 + i5] - n6;
                            n10 ^= n5 & (n10 ^ nArray[n2 + n7 + i5]);
                            n10 >>= 31;
                            n6 ^= (n10 &= n5);
                            int n11 = n2 + n7 + i5;
                            nArray[n11] = nArray[n11] ^ n10;
                        }
                        nArray[n2 + n7 + i3] = n6;
                    }
                    ++n7;
                }
            }
        }
    }

    private static void sort64(long[] lArray, int n2, int n3) {
        int n4 = n3 - n2;
        if (n4 < 2) {
            return;
        }
        for (int i2 = 1; i2 < n4 - i2; i2 += i2) {
        }
        for (int i3 = i2; i3 > 0; i3 >>>= 1) {
            long l2;
            int n5;
            for (n5 = 0; n5 < n4 - i3; ++n5) {
                if ((n5 & i3) != 0) continue;
                l2 = lArray[n2 + n5 + i3] - lArray[n2 + n5];
                l2 >>>= 63;
                l2 = -l2;
                int n6 = n2 + n5;
                lArray[n6] = lArray[n6] ^ (l2 &= lArray[n2 + n5] ^ lArray[n2 + n5 + i3]);
                int n7 = n2 + n5 + i3;
                lArray[n7] = lArray[n7] ^ l2;
            }
            n5 = 0;
            for (int i4 = i2; i4 > i3; i4 >>>= 1) {
                while (n5 < n4 - i4) {
                    if ((n5 & i3) == 0) {
                        l2 = lArray[n2 + n5 + i3];
                        for (int i5 = i4; i5 > i3; i5 >>>= 1) {
                            long l3 = lArray[n2 + n5 + i5] - l2;
                            l3 >>>= 63;
                            l3 = -l3;
                            l2 ^= (l3 &= l2 ^ lArray[n2 + n5 + i5]);
                            int n8 = n2 + n5 + i5;
                            lArray[n8] = lArray[n8] ^ l3;
                        }
                        lArray[n2 + n5 + i3] = l2;
                    }
                    ++n5;
                }
            }
        }
    }
}

