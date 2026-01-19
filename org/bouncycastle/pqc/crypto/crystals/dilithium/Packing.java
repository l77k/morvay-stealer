/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.crystals.dilithium;

import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumEngine;
import org.bouncycastle.pqc.crypto.crystals.dilithium.PolyVecK;
import org.bouncycastle.pqc.crypto.crystals.dilithium.PolyVecL;
import org.bouncycastle.util.Arrays;

class Packing {
    Packing() {
    }

    static byte[] packPublicKey(PolyVecK polyVecK, DilithiumEngine dilithiumEngine) {
        byte[] byArray = new byte[dilithiumEngine.getCryptoPublicKeyBytes() - 32];
        for (int i2 = 0; i2 < dilithiumEngine.getDilithiumK(); ++i2) {
            System.arraycopy(polyVecK.getVectorIndex(i2).polyt1Pack(), 0, byArray, i2 * 320, 320);
        }
        return byArray;
    }

    static PolyVecK unpackPublicKey(PolyVecK polyVecK, byte[] byArray, DilithiumEngine dilithiumEngine) {
        for (int i2 = 0; i2 < dilithiumEngine.getDilithiumK(); ++i2) {
            polyVecK.getVectorIndex(i2).polyt1Unpack(Arrays.copyOfRange(byArray, i2 * 320, (i2 + 1) * 320));
        }
        return polyVecK;
    }

    static byte[][] packSecretKey(byte[] byArray, byte[] byArray2, byte[] byArray3, PolyVecK polyVecK, PolyVecL polyVecL, PolyVecK polyVecK2, DilithiumEngine dilithiumEngine) {
        int n2;
        byte[][] byArrayArray = new byte[6][];
        byArrayArray[0] = byArray;
        byArrayArray[1] = byArray3;
        byArrayArray[2] = byArray2;
        byArrayArray[3] = new byte[dilithiumEngine.getDilithiumL() * dilithiumEngine.getDilithiumPolyEtaPackedBytes()];
        for (n2 = 0; n2 < dilithiumEngine.getDilithiumL(); ++n2) {
            polyVecL.getVectorIndex(n2).polyEtaPack(byArrayArray[3], n2 * dilithiumEngine.getDilithiumPolyEtaPackedBytes());
        }
        byArrayArray[4] = new byte[dilithiumEngine.getDilithiumK() * dilithiumEngine.getDilithiumPolyEtaPackedBytes()];
        for (n2 = 0; n2 < dilithiumEngine.getDilithiumK(); ++n2) {
            polyVecK2.getVectorIndex(n2).polyEtaPack(byArrayArray[4], n2 * dilithiumEngine.getDilithiumPolyEtaPackedBytes());
        }
        byArrayArray[5] = new byte[dilithiumEngine.getDilithiumK() * 416];
        for (n2 = 0; n2 < dilithiumEngine.getDilithiumK(); ++n2) {
            polyVecK.getVectorIndex(n2).polyt0Pack(byArrayArray[5], n2 * 416);
        }
        return byArrayArray;
    }

    static void unpackSecretKey(PolyVecK polyVecK, PolyVecL polyVecL, PolyVecK polyVecK2, byte[] byArray, byte[] byArray2, byte[] byArray3, DilithiumEngine dilithiumEngine) {
        int n2;
        for (n2 = 0; n2 < dilithiumEngine.getDilithiumL(); ++n2) {
            polyVecL.getVectorIndex(n2).polyEtaUnpack(byArray2, n2 * dilithiumEngine.getDilithiumPolyEtaPackedBytes());
        }
        for (n2 = 0; n2 < dilithiumEngine.getDilithiumK(); ++n2) {
            polyVecK2.getVectorIndex(n2).polyEtaUnpack(byArray3, n2 * dilithiumEngine.getDilithiumPolyEtaPackedBytes());
        }
        for (n2 = 0; n2 < dilithiumEngine.getDilithiumK(); ++n2) {
            polyVecK.getVectorIndex(n2).polyt0Unpack(byArray, n2 * 416);
        }
    }

    static byte[] packSignature(byte[] byArray, PolyVecL polyVecL, PolyVecK polyVecK, DilithiumEngine dilithiumEngine) {
        int n2;
        int n3 = 0;
        byte[] byArray2 = new byte[dilithiumEngine.getCryptoBytes()];
        System.arraycopy(byArray, 0, byArray2, 0, dilithiumEngine.getDilithiumCTilde());
        n3 += dilithiumEngine.getDilithiumCTilde();
        for (n2 = 0; n2 < dilithiumEngine.getDilithiumL(); ++n2) {
            System.arraycopy(polyVecL.getVectorIndex(n2).zPack(), 0, byArray2, n3 + n2 * dilithiumEngine.getDilithiumPolyZPackedBytes(), dilithiumEngine.getDilithiumPolyZPackedBytes());
        }
        n3 += dilithiumEngine.getDilithiumL() * dilithiumEngine.getDilithiumPolyZPackedBytes();
        for (n2 = 0; n2 < dilithiumEngine.getDilithiumOmega() + dilithiumEngine.getDilithiumK(); ++n2) {
            byArray2[n3 + n2] = 0;
        }
        int n4 = 0;
        for (n2 = 0; n2 < dilithiumEngine.getDilithiumK(); ++n2) {
            for (int i2 = 0; i2 < 256; ++i2) {
                if (polyVecK.getVectorIndex(n2).getCoeffIndex(i2) == 0) continue;
                byArray2[n3 + n4++] = (byte)i2;
            }
            byArray2[n3 + dilithiumEngine.getDilithiumOmega() + n2] = (byte)n4;
        }
        return byArray2;
    }

    static boolean unpackSignature(PolyVecL polyVecL, PolyVecK polyVecK, byte[] byArray, DilithiumEngine dilithiumEngine) {
        int n2;
        int n3;
        int n4 = dilithiumEngine.getDilithiumCTilde();
        for (n3 = 0; n3 < dilithiumEngine.getDilithiumL(); ++n3) {
            polyVecL.getVectorIndex(n3).zUnpack(Arrays.copyOfRange(byArray, n4 + n3 * dilithiumEngine.getDilithiumPolyZPackedBytes(), n4 + (n3 + 1) * dilithiumEngine.getDilithiumPolyZPackedBytes()));
        }
        n4 += dilithiumEngine.getDilithiumL() * dilithiumEngine.getDilithiumPolyZPackedBytes();
        int n5 = 0;
        for (n3 = 0; n3 < dilithiumEngine.getDilithiumK(); ++n3) {
            for (n2 = 0; n2 < 256; ++n2) {
                polyVecK.getVectorIndex(n3).setCoeffIndex(n2, 0);
            }
            if ((byArray[n4 + dilithiumEngine.getDilithiumOmega() + n3] & 0xFF) < n5 || (byArray[n4 + dilithiumEngine.getDilithiumOmega() + n3] & 0xFF) > dilithiumEngine.getDilithiumOmega()) {
                return false;
            }
            for (n2 = n5; n2 < (byArray[n4 + dilithiumEngine.getDilithiumOmega() + n3] & 0xFF); ++n2) {
                if (n2 > n5 && (byArray[n4 + n2] & 0xFF) <= (byArray[n4 + n2 - 1] & 0xFF)) {
                    return false;
                }
                polyVecK.getVectorIndex(n3).setCoeffIndex(byArray[n4 + n2] & 0xFF, 1);
            }
            n5 = byArray[n4 + dilithiumEngine.getDilithiumOmega() + n3];
        }
        for (n2 = n5; n2 < dilithiumEngine.getDilithiumOmega(); ++n2) {
            if ((byArray[n4 + n2] & 0xFF) == 0) continue;
            return false;
        }
        return true;
    }
}

