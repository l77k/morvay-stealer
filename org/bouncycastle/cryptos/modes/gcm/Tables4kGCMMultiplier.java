/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.modes.gcm;

import org.bouncycastle.crypto.modes.gcm.GCMMultiplier;
import org.bouncycastle.crypto.modes.gcm.GCMUtil;
import org.bouncycastle.util.Pack;

public class Tables4kGCMMultiplier
implements GCMMultiplier {
    private byte[] H;
    private long[][] T;

    @Override
    public void init(byte[] byArray) {
        if (this.T == null) {
            this.T = new long[256][2];
        } else if (0 != GCMUtil.areEqual(this.H, byArray)) {
            return;
        }
        this.H = new byte[16];
        GCMUtil.copy(byArray, this.H);
        GCMUtil.asLongs(this.H, this.T[1]);
        GCMUtil.multiplyP7(this.T[1], this.T[1]);
        for (int i2 = 2; i2 < 256; i2 += 2) {
            GCMUtil.divideP(this.T[i2 >> 1], this.T[i2]);
            GCMUtil.xor(this.T[i2], this.T[1], this.T[i2 + 1]);
        }
    }

    @Override
    public void multiplyH(byte[] byArray) {
        long[] lArray = this.T[byArray[15] & 0xFF];
        long l2 = lArray[0];
        long l3 = lArray[1];
        for (int i2 = 14; i2 >= 0; --i2) {
            lArray = this.T[byArray[i2] & 0xFF];
            long l4 = l3 << 56;
            l3 = lArray[1] ^ (l3 >>> 8 | l2 << 56);
            l2 = lArray[0] ^ l2 >>> 8 ^ l4 ^ l4 >>> 1 ^ l4 >>> 2 ^ l4 >>> 7;
        }
        Pack.longToBigEndian(l2, byArray, 0);
        Pack.longToBigEndian(l3, byArray, 8);
    }
}

