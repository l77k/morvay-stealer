/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;

public class X923Padding
implements BlockCipherPadding {
    SecureRandom random = null;

    @Override
    public void init(SecureRandom secureRandom) throws IllegalArgumentException {
        this.random = secureRandom;
    }

    @Override
    public String getPaddingName() {
        return "X9.23";
    }

    @Override
    public int addPadding(byte[] byArray, int n2) {
        byte by = (byte)(byArray.length - n2);
        while (n2 < byArray.length - 1) {
            byArray[n2] = this.random == null ? (byte)0 : (byte)this.random.nextInt();
            ++n2;
        }
        byArray[n2] = by;
        return by;
    }

    @Override
    public int padCount(byte[] byArray) throws InvalidCipherTextException {
        int n2 = byArray[byArray.length - 1] & 0xFF;
        int n3 = byArray.length - n2;
        int n4 = (n3 | n2 - 1) >> 31;
        if (n4 != 0) {
            throw new InvalidCipherTextException("pad block corrupted");
        }
        return n2;
    }
}

