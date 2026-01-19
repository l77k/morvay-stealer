/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.prng.drbg;

import java.util.Hashtable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.util.Integers;

class Utils {
    static final Hashtable maxSecurityStrengths = new Hashtable();

    Utils() {
    }

    static int getMaxSecurityStrength(Digest digest) {
        return (Integer)maxSecurityStrengths.get(digest.getAlgorithmName());
    }

    static int getMaxSecurityStrength(Mac mac) {
        String string = mac.getAlgorithmName();
        return (Integer)maxSecurityStrengths.get(string.substring(0, string.indexOf("/")));
    }

    static byte[] hash_df(Digest digest, byte[] byArray, int n2) {
        int n3;
        int n4;
        byte[] byArray2 = new byte[(n2 + 7) / 8];
        int n5 = byArray2.length / digest.getDigestSize();
        int n6 = 1;
        byte[] byArray3 = new byte[digest.getDigestSize()];
        for (n4 = 0; n4 <= n5; ++n4) {
            digest.update((byte)n6);
            digest.update((byte)(n2 >> 24));
            digest.update((byte)(n2 >> 16));
            digest.update((byte)(n2 >> 8));
            digest.update((byte)n2);
            digest.update(byArray, 0, byArray.length);
            digest.doFinal(byArray3, 0);
            n3 = byArray2.length - n4 * byArray3.length > byArray3.length ? byArray3.length : byArray2.length - n4 * byArray3.length;
            System.arraycopy(byArray3, 0, byArray2, n4 * byArray3.length, n3);
            ++n6;
        }
        if (n2 % 8 != 0) {
            n4 = 8 - n2 % 8;
            n3 = 0;
            for (int i2 = 0; i2 != byArray2.length; ++i2) {
                int n7 = byArray2[i2] & 0xFF;
                byArray2[i2] = (byte)(n7 >>> n4 | n3 << 8 - n4);
                n3 = n7;
            }
        }
        return byArray2;
    }

    static boolean isTooLarge(byte[] byArray, int n2) {
        return byArray != null && byArray.length > n2;
    }

    static {
        maxSecurityStrengths.put("SHA-1", Integers.valueOf(128));
        maxSecurityStrengths.put("SHA-224", Integers.valueOf(192));
        maxSecurityStrengths.put("SHA-256", Integers.valueOf(256));
        maxSecurityStrengths.put("SHA-384", Integers.valueOf(256));
        maxSecurityStrengths.put("SHA-512", Integers.valueOf(256));
        maxSecurityStrengths.put("SHA-512/224", Integers.valueOf(192));
        maxSecurityStrengths.put("SHA-512/256", Integers.valueOf(256));
    }
}

