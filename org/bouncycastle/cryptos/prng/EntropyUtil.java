/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.prng;

import org.bouncycastle.crypto.prng.EntropySource;

public class EntropyUtil {
    public static byte[] generateSeed(EntropySource entropySource, int n2) {
        byte[] byArray = new byte[n2];
        if (n2 * 8 <= entropySource.entropySize()) {
            byte[] byArray2 = entropySource.getEntropy();
            System.arraycopy(byArray2, 0, byArray, 0, byArray.length);
        } else {
            int n3 = entropySource.entropySize() / 8;
            for (int i2 = 0; i2 < byArray.length; i2 += n3) {
                byte[] byArray3 = entropySource.getEntropy();
                if (byArray3.length <= byArray.length - i2) {
                    System.arraycopy(byArray3, 0, byArray, i2, byArray3.length);
                    continue;
                }
                System.arraycopy(byArray3, 0, byArray, i2, byArray.length - i2);
            }
        }
        return byArray;
    }
}

