/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;

public class TBCPadding
implements BlockCipherPadding {
    @Override
    public void init(SecureRandom secureRandom) throws IllegalArgumentException {
    }

    @Override
    public String getPaddingName() {
        return "TBC";
    }

    @Override
    public int addPadding(byte[] byArray, int n2) {
        int n3 = byArray.length - n2;
        byte by = n2 > 0 ? (byte)((byArray[n2 - 1] & 1) == 0 ? 255 : 0) : (byte)((byArray[byArray.length - 1] & 1) == 0 ? 255 : 0);
        while (n2 < byArray.length) {
            byArray[n2] = by;
            ++n2;
        }
        return n3;
    }

    @Override
    public int padCount(byte[] byArray) throws InvalidCipherTextException {
        int n2 = byArray.length;
        int n3 = byArray[--n2] & 0xFF;
        int n4 = 1;
        int n5 = -1;
        while (--n2 >= 0) {
            int n6 = byArray[n2] & 0xFF;
            int n7 = (n6 ^ n3) - 1 >> 31;
            n4 -= (n5 &= n7);
        }
        return n4;
    }
}

