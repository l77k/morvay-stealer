/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.params.KeyParameter;

public class DESParameters
extends KeyParameter {
    public static final int DES_KEY_LENGTH = 8;
    private static final int N_DES_WEAK_KEYS = 16;
    private static byte[] DES_weak_keys = new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 31, 31, 31, 31, 14, 14, 14, 14, -32, -32, -32, -32, -15, -15, -15, -15, -2, -2, -2, -2, -2, -2, -2, -2, 1, -2, 1, -2, 1, -2, 1, -2, 31, -32, 31, -32, 14, -15, 14, -15, 1, -32, 1, -32, 1, -15, 1, -15, 31, -2, 31, -2, 14, -2, 14, -2, 1, 31, 1, 31, 1, 14, 1, 14, -32, -2, -32, -2, -15, -2, -15, -2, -2, 1, -2, 1, -2, 1, -2, 1, -32, 31, -32, 31, -15, 14, -15, 14, -32, 1, -32, 1, -15, 1, -15, 1, -2, 31, -2, 31, -2, 14, -2, 14, 31, 1, 31, 1, 14, 1, 14, 1, -2, -32, -2, -32, -2, -15, -2, -15};

    public DESParameters(byte[] byArray) {
        super(byArray);
        if (DESParameters.isWeakKey(byArray, 0)) {
            throw new IllegalArgumentException("attempt to create weak DES key");
        }
    }

    public static boolean isWeakKey(byte[] byArray, int n2) {
        if (byArray.length - n2 < 8) {
            throw new IllegalArgumentException("key material too short.");
        }
        block0: for (int i2 = 0; i2 < 16; ++i2) {
            for (int i3 = 0; i3 < 8; ++i3) {
                if (byArray[i3 + n2] != DES_weak_keys[i2 * 8 + i3]) continue block0;
            }
            return true;
        }
        return false;
    }

    public static void setOddParity(byte[] byArray) {
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            byte by = byArray[i2];
            byArray[i2] = (byte)(by & 0xFE | (by >> 1 ^ by >> 2 ^ by >> 3 ^ by >> 4 ^ by >> 5 ^ by >> 6 ^ by >> 7 ^ 1) & 1);
        }
    }
}

