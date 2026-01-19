/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.sphincsplus;

import org.bouncycastle.pqc.crypto.sphincsplus.HarakaSBase;

class HarakaSXof
extends HarakaSBase {
    public String getAlgorithmName() {
        return "Haraka-S";
    }

    public HarakaSXof(byte[] byArray) {
        byte[] byArray2 = new byte[640];
        this.update(byArray, 0, byArray.length);
        this.doFinal(byArray2, 0, byArray2.length);
        this.haraka512_rc = new long[10][8];
        this.haraka256_rc = new int[10][8];
        for (int i2 = 0; i2 < 10; ++i2) {
            this.interleaveConstant32(this.haraka256_rc[i2], byArray2, i2 << 5);
            this.interleaveConstant(this.haraka512_rc[i2], byArray2, i2 << 6);
        }
    }

    public void update(byte[] byArray, int n2, int n3) {
        int n4 = n2;
        int n5 = n3 + this.off >> 5;
        for (int i2 = 0; i2 < n5; ++i2) {
            while (this.off < 32) {
                int n6 = this.off++;
                this.buffer[n6] = (byte)(this.buffer[n6] ^ byArray[n4++]);
            }
            this.haraka512Perm(this.buffer);
            this.off = 0;
        }
        while (n4 < n2 + n3) {
            int n7 = this.off++;
            this.buffer[n7] = (byte)(this.buffer[n7] ^ byArray[n4++]);
        }
    }

    public void update(byte by) {
        int n2 = this.off++;
        this.buffer[n2] = (byte)(this.buffer[n2] ^ by);
        if (this.off == 32) {
            this.haraka512Perm(this.buffer);
            this.off = 0;
        }
    }

    public int doFinal(byte[] byArray, int n2, int n3) {
        int n4 = n3;
        int n5 = this.off;
        this.buffer[n5] = (byte)(this.buffer[n5] ^ 0x1F);
        this.buffer[31] = (byte)(this.buffer[31] ^ 0x80);
        while (n3 >= 32) {
            this.haraka512Perm(this.buffer);
            System.arraycopy(this.buffer, 0, byArray, n2, 32);
            n2 += 32;
            n3 -= 32;
        }
        if (n3 > 0) {
            this.haraka512Perm(this.buffer);
            System.arraycopy(this.buffer, 0, byArray, n2, n3);
        }
        this.reset();
        return n4;
    }
}

