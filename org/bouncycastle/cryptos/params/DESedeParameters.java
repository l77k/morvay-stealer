/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.params.DESParameters;

public class DESedeParameters
extends DESParameters {
    public static final int DES_EDE_KEY_LENGTH = 24;

    public DESedeParameters(byte[] byArray) {
        super(byArray);
        if (DESedeParameters.isWeakKey(byArray, 0, byArray.length)) {
            throw new IllegalArgumentException("attempt to create weak DESede key");
        }
    }

    public static boolean isWeakKey(byte[] byArray, int n2, int n3) {
        for (int i2 = n2; i2 < n3; i2 += 8) {
            if (!DESParameters.isWeakKey(byArray, i2)) continue;
            return true;
        }
        return false;
    }

    public static boolean isWeakKey(byte[] byArray, int n2) {
        return DESedeParameters.isWeakKey(byArray, n2, byArray.length - n2);
    }

    public static boolean isRealEDEKey(byte[] byArray, int n2) {
        return byArray.length == 16 ? DESedeParameters.isReal2Key(byArray, n2) : DESedeParameters.isReal3Key(byArray, n2);
    }

    public static boolean isReal2Key(byte[] byArray, int n2) {
        boolean bl = false;
        for (int i2 = n2; i2 != n2 + 8; ++i2) {
            if (byArray[i2] == byArray[i2 + 8]) continue;
            bl = true;
        }
        return bl;
    }

    public static boolean isReal3Key(byte[] byArray, int n2) {
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        for (int i2 = n2; i2 != n2 + 8; ++i2) {
            bl |= byArray[i2] != byArray[i2 + 8];
            bl2 |= byArray[i2] != byArray[i2 + 16];
            bl3 |= byArray[i2 + 8] != byArray[i2 + 16];
        }
        return bl && bl2 && bl3;
    }
}

