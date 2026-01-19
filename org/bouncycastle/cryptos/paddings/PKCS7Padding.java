/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;

public class PKCS7Padding
implements BlockCipherPadding {
    @Override
    public void init(SecureRandom secureRandom) throws IllegalArgumentException {
    }

    @Override
    public String getPaddingName() {
        return "PKCS7";
    }

    @Override
    public int addPadding(byte[] byArray, int n2) {
        byte by = (byte)(byArray.length - n2);
        while (n2 < byArray.length) {
            byArray[n2] = by;
            ++n2;
        }
        return by;
    }

    @Override
    public int padCount(byte[] byArray) throws InvalidCipherTextException {
        byte by = byArray[byArray.length - 1];
        int n2 = by & 0xFF;
        int n3 = byArray.length - n2;
        int n4 = (n3 | n2 - 1) >> 31;
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            n4 |= (byArray[i2] ^ by) & ~(i2 - n3 >> 31);
        }
        if (n4 != 0) {
            throw new InvalidCipherTextException("pad block corrupted");
        }
        return n2;
    }
}

