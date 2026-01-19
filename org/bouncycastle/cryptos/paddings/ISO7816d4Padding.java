/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;

public class ISO7816d4Padding
implements BlockCipherPadding {
    @Override
    public void init(SecureRandom secureRandom) throws IllegalArgumentException {
    }

    @Override
    public String getPaddingName() {
        return "ISO7816-4";
    }

    @Override
    public int addPadding(byte[] byArray, int n2) {
        int n3 = byArray.length - n2;
        byArray[n2] = -128;
        ++n2;
        while (n2 < byArray.length) {
            byArray[n2] = 0;
            ++n2;
        }
        return n3;
    }

    @Override
    public int padCount(byte[] byArray) throws InvalidCipherTextException {
        int n2 = -1;
        int n3 = -1;
        int n4 = byArray.length;
        while (--n4 >= 0) {
            int n5 = byArray[n4] & 0xFF;
            int n6 = (n5 ^ 0) - 1 >> 31;
            int n7 = (n5 ^ 0x80) - 1 >> 31;
            n2 ^= (n4 ^ n2) & (n3 & n7);
            n3 &= n6;
        }
        if (n2 < 0) {
            throw new InvalidCipherTextException("pad block corrupted");
        }
        return byArray.length - n2;
    }
}

