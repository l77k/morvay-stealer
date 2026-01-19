/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.modes.gcm;

import org.bouncycastle.crypto.modes.gcm.GCMExponentiator;
import org.bouncycastle.crypto.modes.gcm.GCMUtil;

public class BasicGCMExponentiator
implements GCMExponentiator {
    private long[] x;

    @Override
    public void init(byte[] byArray) {
        this.x = GCMUtil.asLongs(byArray);
    }

    @Override
    public void exponentiateX(long l2, byte[] byArray) {
        long[] lArray = GCMUtil.oneAsLongs();
        if (l2 > 0L) {
            long[] lArray2 = new long[2];
            GCMUtil.copy(this.x, lArray2);
            do {
                if ((l2 & 1L) != 0L) {
                    GCMUtil.multiply(lArray, lArray2);
                }
                GCMUtil.square(lArray2, lArray2);
            } while ((l2 >>>= 1) > 0L);
        }
        GCMUtil.asBytes(lArray, byArray);
    }
}

