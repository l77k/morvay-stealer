/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.mldsa;

import org.bouncycastle.pqc.crypto.mldsa.MLDSAEngine;
import org.bouncycastle.pqc.crypto.mldsa.PolyVecK;
import org.bouncycastle.pqc.crypto.mldsa.PolyVecL;
import org.bouncycastle.util.Arrays;

class Packing {
    Packing() {
    }

    static byte[] packPublicKey(PolyVecK polyVecK, MLDSAEngine mLDSAEngine) {
        byte[] byArray = new byte[mLDSAEngine.getCryptoPublicKeyBytes() - 32];
        for (int i2 = 0; i2 < mLDSAEngine.getDilithiumK(); ++i2) {
            System.arraycopy(polyVecK.getVectorIndex(i2).polyt1Pack(), 0, byArray, i2 * 320, 320);
        }
        return byArray;
    }

    static PolyVecK unpackPublicKey(PolyVecK polyVecK, byte[] byArray, MLDSAEngine mLDSAEngine) {
        for (int i2 = 0; i2 < mLDSAEngine.getDilithiumK(); ++i2) {
            polyVecK.getVectorIndex(i2).polyt1Unpack(Arrays.copyOfRange(byArray, i2 * 320, (i2 + 1) * 320));
        }
        return polyVecK;
    }

    static byte[][] packSecretKey(byte[] byArray, byte[] byArray2, byte[] byArray3, PolyVecK polyVecK, PolyVecL polyVecL, PolyVecK polyVecK2, MLDSAEngine mLDSAEngine) {
        int n2;
        byte[][] byArrayArray = new byte[6][];
        byArrayArray[0] = byArray;
        byArrayArray[1] = byArray3;
        byArrayArray[2] = byArray2;
        byArrayArray[3] = new byte[mLDSAEngine.getDilithiumL() * mLDSAEngine.getDilithiumPolyEtaPackedBytes()];
        for (n2 = 0; n2 < mLDSAEngine.getDilithiumL(); ++n2) {
            polyVecL.getVectorIndex(n2).polyEtaPack(byArrayArray[3], n2 * mLDSAEngine.getDilithiumPolyEtaPackedBytes());
        }
        byArrayArray[4] = new byte[mLDSAEngine.getDilithiumK() * mLDSAEngine.getDilithiumPolyEtaPackedBytes()];
        for (n2 = 0; n2 < mLDSAEngine.getDilithiumK(); ++n2) {
            polyVecK2.getVectorIndex(n2).polyEtaPack(byArrayArray[4], n2 * mLDSAEngine.getDilithiumPolyEtaPackedBytes());
        }
        byArrayArray[5] = new byte[mLDSAEngine.getDilithiumK() * 416];
        for (n2 = 0; n2 < mLDSAEngine.getDilithiumK(); ++n2) {
            polyVecK.getVectorIndex(n2).polyt0Pack(byArrayArray[5], n2 * 416);
        }
        return byArrayArray;
    }

    static void unpackSecretKey(PolyVecK polyVecK, PolyVecL polyVecL, PolyVecK polyVecK2, byte[] byArray, byte[] byArray2, byte[] byArray3, MLDSAEngine mLDSAEngine) {
        int n2;
        for (n2 = 0; n2 < mLDSAEngine.getDilithiumL(); ++n2) {
            polyVecL.getVectorIndex(n2).polyEtaUnpack(byArray2, n2 * mLDSAEngine.getDilithiumPolyEtaPackedBytes());
        }
        for (n2 = 0; n2 < mLDSAEngine.getDilithiumK(); ++n2) {
            polyVecK2.getVectorIndex(n2).polyEtaUnpack(byArray3, n2 * mLDSAEngine.getDilithiumPolyEtaPackedBytes());
        }
        for (n2 = 0; n2 < mLDSAEngine.getDilithiumK(); ++n2) {
            polyVecK.getVectorIndex(n2).polyt0Unpack(byArray, n2 * 416);
        }
    }

    static byte[] packSignature(byte[] byArray, PolyVecL polyVecL, PolyVecK polyVecK, MLDSAEngine mLDSAEngine) {
        int n2;
        int n3 = 0;
        byte[] byArray2 = new byte[mLDSAEngine.getCryptoBytes()];
        System.arraycopy(byArray, 0, byArray2, 0, mLDSAEngine.getDilithiumCTilde());
        n3 += mLDSAEngine.getDilithiumCTilde();
        for (n2 = 0; n2 < mLDSAEngine.getDilithiumL(); ++n2) {
            System.arraycopy(polyVecL.getVectorIndex(n2).zPack(), 0, byArray2, n3 + n2 * mLDSAEngine.getDilithiumPolyZPackedBytes(), mLDSAEngine.getDilithiumPolyZPackedBytes());
        }
        n3 += mLDSAEngine.getDilithiumL() * mLDSAEngine.getDilithiumPolyZPackedBytes();
        for (n2 = 0; n2 < mLDSAEngine.getDilithiumOmega() + mLDSAEngine.getDilithiumK(); ++n2) {
            byArray2[n3 + n2] = 0;
        }
        int n4 = 0;
        for (n2 = 0; n2 < mLDSAEngine.getDilithiumK(); ++n2) {
            for (int i2 = 0; i2 < 256; ++i2) {
                if (polyVecK.getVectorIndex(n2).getCoeffIndex(i2) == 0) continue;
                byArray2[n3 + n4++] = (byte)i2;
            }
            byArray2[n3 + mLDSAEngine.getDilithiumOmega() + n2] = (byte)n4;
        }
        return byArray2;
    }

    static boolean unpackSignature(PolyVecL polyVecL, PolyVecK polyVecK, byte[] byArray, MLDSAEngine mLDSAEngine) {
        int n2;
        int n3;
        int n4 = mLDSAEngine.getDilithiumCTilde();
        for (n3 = 0; n3 < mLDSAEngine.getDilithiumL(); ++n3) {
            polyVecL.getVectorIndex(n3).zUnpack(Arrays.copyOfRange(byArray, n4 + n3 * mLDSAEngine.getDilithiumPolyZPackedBytes(), n4 + (n3 + 1) * mLDSAEngine.getDilithiumPolyZPackedBytes()));
        }
        n4 += mLDSAEngine.getDilithiumL() * mLDSAEngine.getDilithiumPolyZPackedBytes();
        int n5 = 0;
        for (n3 = 0; n3 < mLDSAEngine.getDilithiumK(); ++n3) {
            for (n2 = 0; n2 < 256; ++n2) {
                polyVecK.getVectorIndex(n3).setCoeffIndex(n2, 0);
            }
            if ((byArray[n4 + mLDSAEngine.getDilithiumOmega() + n3] & 0xFF) < n5 || (byArray[n4 + mLDSAEngine.getDilithiumOmega() + n3] & 0xFF) > mLDSAEngine.getDilithiumOmega()) {
                return false;
            }
            for (n2 = n5; n2 < (byArray[n4 + mLDSAEngine.getDilithiumOmega() + n3] & 0xFF); ++n2) {
                if (n2 > n5 && (byArray[n4 + n2] & 0xFF) <= (byArray[n4 + n2 - 1] & 0xFF)) {
                    return false;
                }
                polyVecK.getVectorIndex(n3).setCoeffIndex(byArray[n4 + n2] & 0xFF, 1);
            }
            n5 = byArray[n4 + mLDSAEngine.getDilithiumOmega() + n3];
        }
        for (n2 = n5; n2 < mLDSAEngine.getDilithiumOmega(); ++n2) {
            if ((byArray[n4 + n2] & 0xFF) == 0) continue;
            return false;
        }
        return true;
    }
}

