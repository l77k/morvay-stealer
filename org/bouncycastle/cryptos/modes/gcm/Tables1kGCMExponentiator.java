/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.modes.gcm;

import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.crypto.modes.gcm.GCMExponentiator;
import org.bouncycastle.crypto.modes.gcm.GCMUtil;

public class Tables1kGCMExponentiator
implements GCMExponentiator {
    private List lookupPowX2;

    @Override
    public void init(byte[] byArray) {
        long[] lArray = GCMUtil.asLongs(byArray);
        if (this.lookupPowX2 != null && 0L != GCMUtil.areEqual(lArray, (long[])this.lookupPowX2.get(0))) {
            return;
        }
        this.lookupPowX2 = new ArrayList(8);
        this.lookupPowX2.add(lArray);
    }

    @Override
    public void exponentiateX(long l2, byte[] byArray) {
        long[] lArray = GCMUtil.oneAsLongs();
        int n2 = 0;
        while (l2 > 0L) {
            if ((l2 & 1L) != 0L) {
                GCMUtil.multiply(lArray, this.getPowX2(n2));
            }
            ++n2;
            l2 >>>= 1;
        }
        GCMUtil.asBytes(lArray, byArray);
    }

    private long[] getPowX2(int n2) {
        int n3 = this.lookupPowX2.size() - 1;
        if (n3 < n2) {
            long[] lArray = (long[])this.lookupPowX2.get(n3);
            do {
                long[] lArray2 = new long[2];
                GCMUtil.square(lArray, lArray2);
                this.lookupPowX2.add(lArray2);
                lArray = lArray2;
            } while (++n3 < n2);
        }
        return (long[])this.lookupPowX2.get(n2);
    }
}

