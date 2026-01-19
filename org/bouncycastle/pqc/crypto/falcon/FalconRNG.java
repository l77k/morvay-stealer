/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.falcon;

import org.bouncycastle.pqc.crypto.falcon.FalconConversions;
import org.bouncycastle.pqc.crypto.falcon.SHAKE256;

class FalconRNG {
    byte[] bd = new byte[512];
    long bdummy_u64 = 0L;
    int ptr = 0;
    byte[] sd = new byte[256];
    long sdummy_u64 = 0L;
    int type = 0;
    FalconConversions convertor = new FalconConversions();

    FalconRNG() {
    }

    void prng_init(SHAKE256 sHAKE256) {
        byte[] byArray = new byte[56];
        sHAKE256.inner_shake256_extract(byArray, 0, 56);
        for (int i2 = 0; i2 < 14; ++i2) {
            int n2 = byArray[(i2 << 2) + 0] & 0xFF | (byArray[(i2 << 2) + 1] & 0xFF) << 8 | (byArray[(i2 << 2) + 2] & 0xFF) << 16 | (byArray[(i2 << 2) + 3] & 0xFF) << 24;
            System.arraycopy(this.convertor.int_to_bytes(n2), 0, this.sd, i2 << 2, 4);
        }
        long l2 = (long)this.convertor.bytes_to_int(this.sd, 48) & 0xFFFFFFFFL;
        long l3 = (long)this.convertor.bytes_to_int(this.sd, 52) & 0xFFFFFFFFL;
        System.arraycopy(this.convertor.long_to_bytes(l2 + (l3 << 32)), 0, this.sd, 48, 8);
        this.prng_refill();
    }

    void prng_refill() {
        int[] nArray = new int[]{1634760805, 857760878, 2036477234, 1797285236};
        long l2 = this.convertor.bytes_to_long(this.sd, 48);
        for (int i2 = 0; i2 < 8; ++i2) {
            int n2;
            int[] nArray2 = new int[16];
            System.arraycopy(nArray, 0, nArray2, 0, nArray.length);
            System.arraycopy(this.convertor.bytes_to_int_array(this.sd, 0, 12), 0, nArray2, 4, 12);
            nArray2[14] = nArray2[14] ^ (int)l2;
            nArray2[15] = nArray2[15] ^ (int)(l2 >>> 32);
            for (int i3 = 0; i3 < 10; ++i3) {
                this.QROUND(0, 4, 8, 12, nArray2);
                this.QROUND(1, 5, 9, 13, nArray2);
                this.QROUND(2, 6, 10, 14, nArray2);
                this.QROUND(3, 7, 11, 15, nArray2);
                this.QROUND(0, 5, 10, 15, nArray2);
                this.QROUND(1, 6, 11, 12, nArray2);
                this.QROUND(2, 7, 8, 13, nArray2);
                this.QROUND(3, 4, 9, 14, nArray2);
            }
            for (n2 = 0; n2 < 4; ++n2) {
                int n3 = n2;
                nArray2[n3] = nArray2[n3] + nArray[n2];
            }
            for (n2 = 4; n2 < 14; ++n2) {
                int n4 = n2;
                nArray2[n4] = nArray2[n4] + this.convertor.bytes_to_int(this.sd, 4 * n2 - 16);
            }
            nArray2[14] = nArray2[14] + (this.convertor.bytes_to_int(this.sd, 40) ^ (int)l2);
            nArray2[15] = nArray2[15] + (this.convertor.bytes_to_int(this.sd, 44) ^ (int)(l2 >>> 32));
            ++l2;
            for (n2 = 0; n2 < 16; ++n2) {
                this.bd[(i2 << 2) + (n2 << 5) + 0] = (byte)nArray2[n2];
                this.bd[(i2 << 2) + (n2 << 5) + 1] = (byte)(nArray2[n2] >>> 8);
                this.bd[(i2 << 2) + (n2 << 5) + 2] = (byte)(nArray2[n2] >>> 16);
                this.bd[(i2 << 2) + (n2 << 5) + 3] = (byte)(nArray2[n2] >>> 24);
            }
        }
        System.arraycopy(this.convertor.long_to_bytes(l2), 0, this.sd, 48, 8);
        this.ptr = 0;
    }

    void prng_get_bytes(byte[] byArray, int n2, int n3) {
        int n4 = n2;
        while (n3 > 0) {
            int n5 = this.bd.length - this.ptr;
            if (n5 > n3) {
                n5 = n3;
            }
            System.arraycopy(this.bd, 0, byArray, n4, n5);
            n4 += n5;
            n3 -= n5;
            this.ptr += n5;
            if (this.ptr != this.bd.length) continue;
            this.prng_refill();
        }
    }

    private void QROUND(int n2, int n3, int n4, int n5, int[] nArray) {
        int n6 = n2;
        nArray[n6] = nArray[n6] + nArray[n3];
        int n7 = n5;
        nArray[n7] = nArray[n7] ^ nArray[n2];
        nArray[n5] = nArray[n5] << 16 | nArray[n5] >>> 16;
        int n8 = n4;
        nArray[n8] = nArray[n8] + nArray[n5];
        int n9 = n3;
        nArray[n9] = nArray[n9] ^ nArray[n4];
        nArray[n3] = nArray[n3] << 12 | nArray[n3] >>> 20;
        int n10 = n2;
        nArray[n10] = nArray[n10] + nArray[n3];
        int n11 = n5;
        nArray[n11] = nArray[n11] ^ nArray[n2];
        nArray[n5] = nArray[n5] << 8 | nArray[n5] >>> 24;
        int n12 = n4;
        nArray[n12] = nArray[n12] + nArray[n5];
        int n13 = n3;
        nArray[n13] = nArray[n13] ^ nArray[n4];
        nArray[n3] = nArray[n3] << 7 | nArray[n3] >>> 25;
    }

    long prng_get_u64() {
        int n2 = this.ptr;
        if (n2 >= this.bd.length - 9) {
            this.prng_refill();
            n2 = 0;
        }
        this.ptr = n2 + 8;
        return (long)this.bd[n2 + 0] & 0xFFL | ((long)this.bd[n2 + 1] & 0xFFL) << 8 | ((long)this.bd[n2 + 2] & 0xFFL) << 16 | ((long)this.bd[n2 + 3] & 0xFFL) << 24 | ((long)this.bd[n2 + 4] & 0xFFL) << 32 | ((long)this.bd[n2 + 5] & 0xFFL) << 40 | ((long)this.bd[n2 + 6] & 0xFFL) << 48 | ((long)this.bd[n2 + 7] & 0xFFL) << 56;
    }

    byte prng_get_u8() {
        byte by = this.bd[this.ptr++];
        if (this.ptr == this.bd.length) {
            this.prng_refill();
        }
        return by;
    }
}

