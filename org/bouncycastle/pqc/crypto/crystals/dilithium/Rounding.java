/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.crystals.dilithium;

import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumEngine;

class Rounding {
    Rounding() {
    }

    public static int[] power2Round(int n2) {
        int[] nArray;
        nArray = new int[]{n2 + 4096 - 1 >> 13, n2 - (nArray[0] << 13)};
        return nArray;
    }

    public static int[] decompose(int n2, int n3) {
        int n4 = n2 + 127 >> 7;
        if (n3 == 261888) {
            n4 = n4 * 1025 + 0x200000 >> 22;
            n4 &= 0xF;
        } else if (n3 == 95232) {
            n4 = n4 * 11275 + 0x800000 >> 24;
            n4 ^= 43 - n4 >> 31 & n4;
        } else {
            throw new RuntimeException("Wrong Gamma2!");
        }
        int n5 = n2 - n4 * 2 * n3;
        n5 -= 0x3FF000 - n5 >> 31 & 0x7FE001;
        return new int[]{n5, n4};
    }

    public static int makeHint(int n2, int n3, DilithiumEngine dilithiumEngine) {
        int n4 = dilithiumEngine.getDilithiumGamma2();
        int n5 = 8380417;
        if (n2 <= n4 || n2 > n5 - n4 || n2 == n5 - n4 && n3 == 0) {
            return 0;
        }
        return 1;
    }

    public static int useHint(int n2, int n3, int n4) {
        int[] nArray = Rounding.decompose(n2, n4);
        int n5 = nArray[0];
        int n6 = nArray[1];
        if (n3 == 0) {
            return n6;
        }
        if (n4 == 261888) {
            if (n5 > 0) {
                return n6 + 1 & 0xF;
            }
            return n6 - 1 & 0xF;
        }
        if (n4 == 95232) {
            if (n5 > 0) {
                return n6 == 43 ? 0 : n6 + 1;
            }
            return n6 == 0 ? 43 : n6 - 1;
        }
        throw new RuntimeException("Wrong Gamma2!");
    }
}

