/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.modes.gcm;

import org.bouncycastle.crypto.modes.gcm.GCMMultiplier;
import org.bouncycastle.crypto.modes.gcm.GCMUtil;
import org.bouncycastle.util.Pack;

public class Tables8kGCMMultiplier
implements GCMMultiplier {
    private byte[] H;
    private long[][][] T;

    @Override
    public void init(byte[] byArray) {
        if (this.T == null) {
            this.T = new long[2][256][2];
        } else if (0 != GCMUtil.areEqual(this.H, byArray)) {
            return;
        }
        this.H = new byte[16];
        GCMUtil.copy(byArray, this.H);
        for (int i2 = 0; i2 < 2; ++i2) {
            long[][] lArray = this.T[i2];
            if (i2 == 0) {
                GCMUtil.asLongs(this.H, lArray[1]);
                GCMUtil.multiplyP7(lArray[1], lArray[1]);
            } else {
                GCMUtil.multiplyP8(this.T[i2 - 1][1], lArray[1]);
            }
            for (int i3 = 2; i3 < 256; i3 += 2) {
                GCMUtil.divideP(lArray[i3 >> 1], lArray[i3]);
                GCMUtil.xor(lArray[i3], lArray[1], lArray[i3 + 1]);
            }
        }
    }

    @Override
    public void multiplyH(byte[] byArray) {
        long[][] lArray = this.T[0];
        long[][] lArray2 = this.T[1];
        long[] lArray3 = lArray[byArray[14] & 0xFF];
        long[] lArray4 = lArray2[byArray[15] & 0xFF];
        long l2 = lArray3[0] ^ lArray4[0];
        long l3 = lArray3[1] ^ lArray4[1];
        for (int i2 = 12; i2 >= 0; i2 -= 2) {
            lArray3 = lArray[byArray[i2] & 0xFF];
            lArray4 = lArray2[byArray[i2 + 1] & 0xFF];
            long l4 = l3 << 48;
            l3 = lArray3[1] ^ lArray4[1] ^ (l3 >>> 16 | l2 << 48);
            l2 = lArray3[0] ^ lArray4[0] ^ l2 >>> 16 ^ l4 ^ l4 >>> 1 ^ l4 >>> 2 ^ l4 >>> 7;
        }
        Pack.longToBigEndian(l2, byArray, 0);
        Pack.longToBigEndian(l3, byArray, 8);
    }
}

