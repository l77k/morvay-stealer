/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.cmce;

import org.bouncycastle.math.raw.Interleave;
import org.bouncycastle.pqc.crypto.cmce.GF;

final class GF12
extends GF {
    GF12() {
    }

    @Override
    protected void gf_mul_poly(int n2, int[] nArray, short[] sArray, short[] sArray2, short[] sArray3, int[] nArray2) {
        short s2;
        short s3;
        int n3;
        nArray2[0] = this.gf_mul_ext(sArray2[0], sArray3[0]);
        for (n3 = 1; n3 < n2; ++n3) {
            nArray2[n3 + n3 - 1] = 0;
            s3 = sArray2[n3];
            s2 = sArray3[n3];
            for (int i2 = 0; i2 < n3; ++i2) {
                int n4 = n3 + i2;
                nArray2[n4] = nArray2[n4] ^ this.gf_mul_ext_par(s3, sArray3[i2], sArray2[i2], s2);
            }
            nArray2[n3 + n3] = this.gf_mul_ext(s3, s2);
        }
        for (n3 = (n2 - 1) * 2; n3 >= n2; --n3) {
            s3 = nArray2[n3];
            for (s2 = 0; s2 < nArray.length - 1; ++s2) {
                int n5 = n3 - n2 + nArray[s2];
                nArray2[n5] = nArray2[n5] ^ s3;
            }
            int n6 = n3 - n2;
            nArray2[n6] = nArray2[n6] ^ s3 << 1;
        }
        for (n3 = 0; n3 < n2; ++n3) {
            sArray[n3] = this.gf_reduce(nArray2[n3]);
        }
    }

    @Override
    protected void gf_sqr_poly(int n2, int[] nArray, short[] sArray, short[] sArray2, int[] nArray2) {
        int n3;
        nArray2[0] = this.gf_sq_ext(sArray2[0]);
        for (n3 = 1; n3 < n2; ++n3) {
            nArray2[n3 + n3 - 1] = 0;
            nArray2[n3 + n3] = this.gf_sq_ext(sArray2[n3]);
        }
        for (n3 = (n2 - 1) * 2; n3 >= n2; --n3) {
            int n4 = nArray2[n3];
            for (int i2 = 0; i2 < nArray.length - 1; ++i2) {
                int n5 = n3 - n2 + nArray[i2];
                nArray2[n5] = nArray2[n5] ^ n4;
            }
            int n6 = n3 - n2;
            nArray2[n6] = nArray2[n6] ^ n4 << 1;
        }
        for (n3 = 0; n3 < n2; ++n3) {
            sArray[n3] = this.gf_reduce(nArray2[n3]);
        }
    }

    @Override
    protected short gf_frac(short s2, short s3) {
        return this.gf_mul(this.gf_inv(s2), s3);
    }

    @Override
    protected short gf_inv(short s2) {
        short s3 = s2;
        s3 = this.gf_sq(s3);
        short s4 = this.gf_mul(s3, s2);
        s3 = this.gf_sq(s4);
        s3 = this.gf_sq(s3);
        short s5 = this.gf_mul(s3, s4);
        s3 = this.gf_sq(s5);
        s3 = this.gf_sq(s3);
        s3 = this.gf_sq(s3);
        s3 = this.gf_sq(s3);
        s3 = this.gf_mul(s3, s5);
        s3 = this.gf_sq(s3);
        s3 = this.gf_sq(s3);
        s3 = this.gf_mul(s3, s4);
        s3 = this.gf_sq(s3);
        s3 = this.gf_mul(s3, s2);
        return this.gf_sq(s3);
    }

    @Override
    protected short gf_mul(short s2, short s3) {
        short s4 = s2;
        short s5 = s3;
        int n2 = s4 * (s5 & 1);
        for (int i2 = 1; i2 < 12; ++i2) {
            n2 ^= s4 * (s5 & 1 << i2);
        }
        return this.gf_reduce(n2);
    }

    @Override
    protected int gf_mul_ext(short s2, short s3) {
        short s4 = s2;
        short s5 = s3;
        int n2 = s4 * (s5 & 1);
        for (int i2 = 1; i2 < 12; ++i2) {
            n2 ^= s4 * (s5 & 1 << i2);
        }
        return n2;
    }

    private int gf_mul_ext_par(short s2, short s3, short s4, short s5) {
        short s6 = s2;
        short s7 = s3;
        short s8 = s4;
        short s9 = s5;
        int n2 = s6 * (s7 & 1);
        int n3 = s8 * (s9 & 1);
        for (int i2 = 1; i2 < 12; ++i2) {
            n2 ^= s6 * (s7 & 1 << i2);
            n3 ^= s8 * (s9 & 1 << i2);
        }
        return n2 ^ n3;
    }

    @Override
    protected short gf_reduce(int n2) {
        int n3 = n2 & 0xFFF;
        int n4 = n2 >>> 12;
        int n5 = (n2 & 0x1FF000) >>> 9;
        int n6 = (n2 & 0xE00000) >>> 18;
        int n7 = n2 >>> 21;
        return (short)(n3 ^ n4 ^ n5 ^ n6 ^ n7);
    }

    @Override
    protected short gf_sq(short s2) {
        int n2 = Interleave.expand16to32(s2);
        return this.gf_reduce(n2);
    }

    @Override
    protected int gf_sq_ext(short s2) {
        return Interleave.expand16to32(s2);
    }
}

