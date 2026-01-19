/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.engines.Salsa20Engine;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Pack;

public class SCrypt {
    private SCrypt() {
    }

    public static byte[] generate(byte[] byArray, byte[] byArray2, int n2, int n3, int n4, int n5) {
        if (byArray == null) {
            throw new IllegalArgumentException("Passphrase P must be provided.");
        }
        if (byArray2 == null) {
            throw new IllegalArgumentException("Salt S must be provided.");
        }
        if (n2 <= 1 || !SCrypt.isPowerOf2(n2)) {
            throw new IllegalArgumentException("Cost parameter N must be > 1 and a power of 2");
        }
        if (n3 == 1 && n2 >= 65536) {
            throw new IllegalArgumentException("Cost parameter N must be > 1 and < 65536.");
        }
        if (n3 < 1) {
            throw new IllegalArgumentException("Block size r must be >= 1.");
        }
        int n6 = Integer.MAX_VALUE / (128 * n3 * 8);
        if (n4 < 1 || n4 > n6) {
            throw new IllegalArgumentException("Parallelisation parameter p must be >= 1 and <= " + n6 + " (based on block size r of " + n3 + ")");
        }
        if (n5 < 1) {
            throw new IllegalArgumentException("Generated key length dkLen must be >= 1.");
        }
        return SCrypt.MFcrypt(byArray, byArray2, n2, n3, n4, n5);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static byte[] MFcrypt(byte[] byArray, byte[] byArray2, int n2, int n3, int n4, int n5) {
        byte[] byArray3;
        int n6 = n3 * 128;
        byte[] byArray4 = SCrypt.SingleIterationPBKDF2(byArray, byArray2, n4 * n6);
        int[] nArray = null;
        try {
            int n7 = byArray4.length >>> 2;
            nArray = new int[n7];
            Pack.littleEndianToInt(byArray4, 0, nArray);
            int n8 = 0;
            for (int i2 = n2 * n3; n2 - n8 > 2 && i2 > 1024; i2 >>>= 1) {
                ++n8;
            }
            int n9 = n6 >>> 2;
            for (int i3 = 0; i3 < n7; i3 += n9) {
                SCrypt.SMix(nArray, i3, n2, n8, n3);
            }
            Pack.intToLittleEndian(nArray, byArray4, 0);
            byArray3 = SCrypt.SingleIterationPBKDF2(byArray, byArray4, n5);
        }
        catch (Throwable throwable) {
            SCrypt.Clear(byArray4);
            SCrypt.Clear(nArray);
            throw throwable;
        }
        SCrypt.Clear(byArray4);
        SCrypt.Clear(nArray);
        return byArray3;
    }

    private static byte[] SingleIterationPBKDF2(byte[] byArray, byte[] byArray2, int n2) {
        PKCS5S2ParametersGenerator pKCS5S2ParametersGenerator = new PKCS5S2ParametersGenerator(SHA256Digest.newInstance());
        pKCS5S2ParametersGenerator.init(byArray, byArray2, 1);
        KeyParameter keyParameter = (KeyParameter)((PBEParametersGenerator)pKCS5S2ParametersGenerator).generateDerivedMacParameters(n2 * 8);
        return keyParameter.getKey();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void SMix(int[] nArray, int n2, int n3, int n4, int n5) {
        int n6 = Integers.numberOfTrailingZeros(n3);
        int n7 = n3 >>> n4;
        int n8 = 1 << n4;
        int n9 = n7 - 1;
        int n10 = n6 - n4;
        int n11 = n5 * 32;
        int[] nArray2 = new int[16];
        int[] nArray3 = new int[16];
        int[] nArray4 = new int[n11];
        int[] nArray5 = new int[n11];
        int[][] nArrayArray = new int[n8][];
        try {
            int n12;
            int n13;
            System.arraycopy(nArray, n2, nArray5, 0, n11);
            for (n13 = 0; n13 < n8; ++n13) {
                int[] nArray6 = new int[n7 * n11];
                nArrayArray[n13] = nArray6;
                n12 = 0;
                for (int i2 = 0; i2 < n7; i2 += 2) {
                    System.arraycopy(nArray5, 0, nArray6, n12, n11);
                    SCrypt.BlockMix(nArray5, nArray2, nArray3, nArray4, n5);
                    System.arraycopy(nArray4, 0, nArray6, n12 += n11, n11);
                    n12 += n11;
                    SCrypt.BlockMix(nArray4, nArray2, nArray3, nArray5, n5);
                }
            }
            n13 = n3 - 1;
            for (int i3 = 0; i3 < n3; ++i3) {
                n12 = nArray5[n11 - 16] & n13;
                int[] nArray7 = nArrayArray[n12 >>> n10];
                int n14 = (n12 & n9) * n11;
                System.arraycopy(nArray7, n14, nArray4, 0, n11);
                SCrypt.Xor(nArray4, nArray5, 0, nArray4);
                SCrypt.BlockMix(nArray4, nArray2, nArray3, nArray5, n5);
            }
            System.arraycopy(nArray5, 0, nArray, n2, n11);
        }
        catch (Throwable throwable) {
            SCrypt.ClearAll(nArrayArray);
            SCrypt.ClearAll(new int[][]{nArray5, nArray2, nArray3, nArray4});
            throw throwable;
        }
        SCrypt.ClearAll(nArrayArray);
        SCrypt.ClearAll(new int[][]{nArray5, nArray2, nArray3, nArray4});
    }

    private static void BlockMix(int[] nArray, int[] nArray2, int[] nArray3, int[] nArray4, int n2) {
        System.arraycopy(nArray, nArray.length - 16, nArray2, 0, 16);
        int n3 = 0;
        int n4 = 0;
        int n5 = nArray.length >>> 1;
        for (int i2 = 2 * n2; i2 > 0; --i2) {
            SCrypt.Xor(nArray2, nArray, n3, nArray3);
            Salsa20Engine.salsaCore(8, nArray3, nArray2);
            System.arraycopy(nArray2, 0, nArray4, n4, 16);
            n4 = n5 + n3 - n4;
            n3 += 16;
        }
    }

    private static void Xor(int[] nArray, int[] nArray2, int n2, int[] nArray3) {
        for (int i2 = nArray3.length - 1; i2 >= 0; --i2) {
            nArray3[i2] = nArray[i2] ^ nArray2[n2 + i2];
        }
    }

    private static void Clear(byte[] byArray) {
        if (byArray != null) {
            Arrays.fill(byArray, (byte)0);
        }
    }

    private static void Clear(int[] nArray) {
        if (nArray != null) {
            Arrays.fill(nArray, 0);
        }
    }

    private static void ClearAll(int[][] nArray) {
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            SCrypt.Clear(nArray[i2]);
        }
    }

    private static boolean isPowerOf2(int n2) {
        return (n2 & n2 - 1) == 0;
    }
}

