/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.cmce;

import org.bouncycastle.math.raw.Interleave;
import org.bouncycastle.pqc.crypto.cmce.GF;

final class GF13
extends GF {
    GF13() {
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
            for (s2 = 0; s2 < nArray.length; ++s2) {
                int n5 = n3 - n2 + nArray[s2];
                nArray2[n5] = nArray2[n5] ^ s3;
            }
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
            for (int i2 = 0; i2 < nArray.length; ++i2) {
                int n5 = n3 - n2 + nArray[i2];
                nArray2[n5] = nArray2[n5] ^ n4;
            }
        }
        for (n3 = 0; n3 < n2; ++n3) {
            sArray[n3] = this.gf_reduce(nArray2[n3]);
        }
    }

    @Override
    protected short gf_frac(short s2, short s3) {
        short s4 = this.gf_sqmul(s2, s2);
        short s5 = this.gf_sq2mul(s4, s4);
        short s6 = this.gf_sq2(s5);
        s6 = this.gf_sq2mul(s6, s5);
        s6 = this.gf_sq2(s6);
        s6 = this.gf_sq2mul(s6, s5);
        return this.gf_sqmul(s6, s3);
    }

    @Override
    protected short gf_inv(short s2) {
        return this.gf_frac(s2, (short)1);
    }

    @Override
    protected short gf_mul(short s2, short s3) {
        short s4 = s2;
        short s5 = s3;
        int n2 = s4 * (s5 & 1);
        for (int i2 = 1; i2 < 13; ++i2) {
            n2 ^= s4 * (s5 & 1 << i2);
        }
        return this.gf_reduce(n2);
    }

    @Override
    protected int gf_mul_ext(short s2, short s3) {
        short s4 = s2;
        short s5 = s3;
        int n2 = s4 * (s5 & 1);
        for (int i2 = 1; i2 < 13; ++i2) {
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
        for (int i2 = 1; i2 < 13; ++i2) {
            n2 ^= s6 * (s7 & 1 << i2);
            n3 ^= s8 * (s9 & 1 << i2);
        }
        return n2 ^ n3;
    }

    @Override
    protected short gf_reduce(int n2) {
        int n3 = n2 & 0x1FFF;
        int n4 = n2 >>> 13;
        int n5 = n4 << 4 ^ n4 << 3 ^ n4 << 1;
        int n6 = n5 >>> 13;
        int n7 = n5 & 0x1FFF;
        int n8 = n6 << 4 ^ n6 << 3 ^ n6 << 1;
        return (short)(n3 ^ n4 ^ n6 ^ n7 ^ n8);
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

    private short gf_sq2(short s2) {
        int n2 = Interleave.expand16to32(s2);
        s2 = this.gf_reduce(n2);
        int n3 = Interleave.expand16to32(s2);
        return this.gf_reduce(n3);
    }

    private short gf_sqmul(short s2, short s3) {
        long l2 = s2;
        long l3 = s3;
        long l4 = (l3 << 6) * (l2 & 0x40L);
        l2 ^= l2 << 7;
        l4 ^= (l3 << 0) * (l2 & 0x4001L);
        l4 ^= (l3 << 1) * (l2 & 0x8002L);
        l4 ^= (l3 << 2) * (l2 & 0x10004L);
        l4 ^= (l3 << 3) * (l2 & 0x20008L);
        l4 ^= (l3 << 4) * (l2 & 0x40010L);
        long l5 = (l4 ^= (l3 << 5) * (l2 & 0x80020L)) & 0x1FFC000000L;
        return this.gf_reduce((int)(l4 ^= l5 >>> 18 ^ l5 >>> 20 ^ l5 >>> 24 ^ l5 >>> 26) & 0x3FFFFFF);
    }

    private short gf_sq2mul(short s2, short s3) {
        long l2 = s2;
        long l3 = s3;
        long l4 = (l3 << 18) * (l2 & 0x40L);
        l2 ^= l2 << 21;
        l4 ^= (l3 << 0) * (l2 & 0x10000001L);
        l4 ^= (l3 << 3) * (l2 & 0x20000002L);
        l4 ^= (l3 << 6) * (l2 & 0x40000004L);
        l4 ^= (l3 << 9) * (l2 & 0x80000008L);
        l4 ^= (l3 << 12) * (l2 & 0x100000010L);
        long l5 = (l4 ^= (l3 << 15) * (l2 & 0x200000020L)) & 0x1FFFF80000000000L;
        l4 ^= l5 >>> 18 ^ l5 >>> 20 ^ l5 >>> 24 ^ l5 >>> 26;
        l5 = l4 & 0x7FFFC000000L;
        return this.gf_reduce((int)(l4 ^= l5 >>> 18 ^ l5 >>> 20 ^ l5 >>> 24 ^ l5 >>> 26) & 0x3FFFFFF);
    }
}

