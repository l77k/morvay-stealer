/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.linearalgebra;

import java.security.SecureRandom;

public class RandUtils {
    static int nextInt(SecureRandom secureRandom, int n2) {
        int n3;
        int n4;
        if ((n2 & -n2) == n2) {
            return (int)((long)n2 * (long)(secureRandom.nextInt() >>> 1) >> 31);
        }
        while ((n4 = secureRandom.nextInt() >>> 1) - (n3 = n4 % n2) + (n2 - 1) < 0) {
        }
        return n3;
    }
}

