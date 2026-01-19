/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.ntruprime;

import java.security.SecureRandom;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CTRModeCipher;
import org.bouncycastle.crypto.modes.SICBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

class Utils {
    Utils() {
    }

    protected static int getRandomUnsignedInteger(SecureRandom secureRandom) {
        byte[] byArray = new byte[4];
        secureRandom.nextBytes(byArray);
        return Utils.bToUnsignedInt(byArray[0]) + (Utils.bToUnsignedInt(byArray[1]) << 8) + (Utils.bToUnsignedInt(byArray[2]) << 16) + (Utils.bToUnsignedInt(byArray[3]) << 24);
    }

    protected static void getRandomSmallPolynomial(SecureRandom secureRandom, byte[] byArray) {
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            byArray[i2] = (byte)(((Utils.getRandomUnsignedInteger(secureRandom) & 0x3FFFFFFF) * 3 >>> 30) - 1);
        }
    }

    protected static int getModFreeze(int n2, int n3) {
        return Utils.getSignedDivMod(n2 + (n3 - 1) / 2, n3)[1] - (n3 - 1) / 2;
    }

    protected static boolean isInvertiblePolynomialInR3(byte[] byArray, byte[] byArray2, int n2) {
        int n3;
        int n4;
        byte[] byArray3 = new byte[n2 + 1];
        byte[] byArray4 = new byte[n2 + 1];
        byte[] byArray5 = new byte[n2 + 1];
        byte[] byArray6 = new byte[n2 + 1];
        byArray5[0] = 1;
        byArray3[0] = 1;
        byArray3[n2 - 1] = -1;
        byArray3[n2] = -1;
        for (n4 = 0; n4 < n2; ++n4) {
            byArray4[n2 - 1 - n4] = byArray[n4];
        }
        byArray4[n2] = 0;
        int n5 = 1;
        for (int i2 = 0; i2 < 2 * n2 - 1; ++i2) {
            System.arraycopy(byArray6, 0, byArray6, 1, n2);
            byArray6[0] = 0;
            n3 = -byArray4[0] * byArray3[0];
            int n6 = Utils.checkLessThanZero(-n5) & Utils.checkNotEqualToZero(byArray4[0]);
            n5 ^= n6 & (n5 ^ -n5);
            ++n5;
            n4 = 0;
            while (n4 < n2 + 1) {
                int n7 = n6 & (byArray3[n4] ^ byArray4[n4]);
                int n8 = n4;
                byArray3[n8] = (byte)(byArray3[n8] ^ n7);
                int n9 = n4;
                byArray4[n9] = (byte)(byArray4[n9] ^ n7);
                n7 = n6 & (byArray6[n4] ^ byArray5[n4]);
                int n10 = n4;
                byArray6[n10] = (byte)(byArray6[n10] ^ n7);
                int n11 = n4++;
                byArray5[n11] = (byte)(byArray5[n11] ^ n7);
            }
            for (n4 = 0; n4 < n2 + 1; ++n4) {
                byArray4[n4] = (byte)Utils.getModFreeze(byArray4[n4] + n3 * byArray3[n4], 3);
            }
            for (n4 = 0; n4 < n2 + 1; ++n4) {
                byArray5[n4] = (byte)Utils.getModFreeze(byArray5[n4] + n3 * byArray6[n4], 3);
            }
            for (n4 = 0; n4 < n2; ++n4) {
                byArray4[n4] = byArray4[n4 + 1];
            }
            byArray4[n2] = 0;
        }
        n3 = byArray3[0];
        for (n4 = 0; n4 < n2; ++n4) {
            byArray2[n4] = (byte)(n3 * byArray6[n2 - 1 - n4]);
        }
        return n5 == 0;
    }

    protected static void minmax(int[] nArray, int n2, int n3) {
        int n4 = nArray[n2];
        int n5 = nArray[n3];
        int n6 = n4 ^ n5;
        int n7 = n5 - n4;
        n7 ^= n6 & (n7 ^ n5 ^ Integer.MIN_VALUE);
        n7 >>>= 31;
        n7 = -n7;
        nArray[n2] = n4 ^ (n7 &= n6);
        nArray[n3] = n5 ^ n7;
    }

    protected static void cryptoSort(int[] nArray, int n2) {
        if (n2 < 2) {
            return;
        }
        for (int i2 = 1; i2 < n2 - i2; i2 += i2) {
        }
        for (int i3 = i2; i3 > 0; i3 >>>= 1) {
            int n3;
            for (n3 = 0; n3 < n2 - i3; ++n3) {
                if ((n3 & i3) != 0) continue;
                Utils.minmax(nArray, n3, n3 + i3);
            }
            for (int i4 = i2; i4 > i3; i4 >>>= 1) {
                for (n3 = 0; n3 < n2 - i4; ++n3) {
                    if ((n3 & i3) != 0) continue;
                    Utils.minmax(nArray, n3 + i3, n3 + i4);
                }
            }
        }
    }

    protected static void sortGenerateShortPolynomial(byte[] byArray, int[] nArray, int n2, int n3) {
        int n4;
        for (n4 = 0; n4 < n3; ++n4) {
            nArray[n4] = nArray[n4] & 0xFFFFFFFE;
        }
        for (n4 = n3; n4 < n2; ++n4) {
            nArray[n4] = nArray[n4] & 0xFFFFFFFD | 1;
        }
        Utils.cryptoSort(nArray, n2);
        for (n4 = 0; n4 < n2; ++n4) {
            byArray[n4] = (byte)((nArray[n4] & 3) - 1);
        }
    }

    protected static void getRandomShortPolynomial(SecureRandom secureRandom, byte[] byArray, int n2, int n3) {
        int[] nArray = new int[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray[i2] = Utils.getRandomUnsignedInteger(secureRandom);
        }
        Utils.sortGenerateShortPolynomial(byArray, nArray, n2, n3);
    }

    protected static int getInverseInRQ(int n2, int n3) {
        int n4 = n2;
        for (int i2 = 1; i2 < n3 - 2; ++i2) {
            n4 = Utils.getModFreeze(n2 * n4, n3);
        }
        return n4;
    }

    protected static void getOneThirdInverseInRQ(short[] sArray, byte[] byArray, int n2, int n3) {
        int n4;
        short[] sArray2 = new short[n2 + 1];
        short[] sArray3 = new short[n2 + 1];
        short[] sArray4 = new short[n2 + 1];
        short[] sArray5 = new short[n2 + 1];
        sArray4[0] = (short)Utils.getInverseInRQ(3, n3);
        sArray2[0] = 1;
        sArray2[n2 - 1] = -1;
        sArray2[n2] = -1;
        for (n4 = 0; n4 < n2; ++n4) {
            sArray3[n2 - 1 - n4] = byArray[n4];
        }
        sArray3[n2] = 0;
        int n5 = 1;
        for (int i2 = 0; i2 < 2 * n2 - 1; ++i2) {
            System.arraycopy(sArray5, 0, sArray5, 1, n2);
            sArray5[0] = 0;
            int n6 = Utils.checkLessThanZero(-n5) & Utils.checkNotEqualToZero(sArray3[0]);
            n5 ^= n6 & (n5 ^ -n5);
            ++n5;
            n4 = 0;
            while (n4 < n2 + 1) {
                int n7 = n6 & (sArray2[n4] ^ sArray3[n4]);
                int n8 = n4;
                sArray2[n8] = (short)(sArray2[n8] ^ n7);
                int n9 = n4;
                sArray3[n9] = (short)(sArray3[n9] ^ n7);
                n7 = n6 & (sArray5[n4] ^ sArray4[n4]);
                int n10 = n4;
                sArray5[n10] = (short)(sArray5[n10] ^ n7);
                int n11 = n4++;
                sArray4[n11] = (short)(sArray4[n11] ^ n7);
            }
            short s2 = sArray2[0];
            short s3 = sArray3[0];
            for (n4 = 0; n4 < n2 + 1; ++n4) {
                sArray3[n4] = (short)Utils.getModFreeze(s2 * sArray3[n4] - s3 * sArray2[n4], n3);
            }
            for (n4 = 0; n4 < n2 + 1; ++n4) {
                sArray4[n4] = (short)Utils.getModFreeze(s2 * sArray4[n4] - s3 * sArray5[n4], n3);
            }
            for (n4 = 0; n4 < n2; ++n4) {
                sArray3[n4] = sArray3[n4 + 1];
            }
            sArray3[n2] = 0;
        }
        int n12 = Utils.getInverseInRQ(sArray2[0], n3);
        for (n4 = 0; n4 < n2; ++n4) {
            sArray[n4] = (short)Utils.getModFreeze(n12 * sArray5[n2 - 1 - n4], n3);
        }
    }

    protected static void multiplicationInRQ(short[] sArray, short[] sArray2, byte[] byArray, int n2, int n3) {
        int n4;
        short s2;
        int n5;
        short[] sArray3 = new short[n2 + n2 - 1];
        for (n5 = 0; n5 < n2; ++n5) {
            s2 = 0;
            for (n4 = 0; n4 <= n5; ++n4) {
                s2 = (short)Utils.getModFreeze(s2 + sArray2[n4] * byArray[n5 - n4], n3);
            }
            sArray3[n5] = s2;
        }
        for (n5 = n2; n5 < n2 + n2 - 1; ++n5) {
            s2 = 0;
            for (n4 = n5 - n2 + 1; n4 < n2; ++n4) {
                s2 = (short)Utils.getModFreeze(s2 + sArray2[n4] * byArray[n5 - n4], n3);
            }
            sArray3[n5] = s2;
        }
        for (n5 = n2 + n2 - 2; n5 >= n2; --n5) {
            sArray3[n5 - n2] = (short)Utils.getModFreeze(sArray3[n5 - n2] + sArray3[n5], n3);
            sArray3[n5 - n2 + 1] = (short)Utils.getModFreeze(sArray3[n5 - n2 + 1] + sArray3[n5], n3);
        }
        for (n5 = 0; n5 < n2; ++n5) {
            sArray[n5] = sArray3[n5];
        }
    }

    private static void encode(byte[] byArray, short[] sArray, short[] sArray2, int n2, int n3) {
        if (n2 == 1) {
            short s2 = sArray[0];
            short s3 = sArray2[0];
            while (s3 > 1) {
                byArray[n3++] = (byte)s2;
                s2 = (short)(s2 >>> 8);
                s3 = (short)(s3 + 255 >>> 8);
            }
        }
        if (n2 > 1) {
            int n4;
            short[] sArray3 = new short[(n2 + 1) / 2];
            short[] sArray4 = new short[(n2 + 1) / 2];
            for (n4 = 0; n4 < n2 - 1; n4 += 2) {
                short s4 = sArray2[n4];
                int n5 = sArray[n4] + sArray[n4 + 1] * s4;
                int n6 = sArray2[n4 + 1] * s4;
                while (n6 >= 16384) {
                    byArray[n3++] = (byte)n5;
                    n5 >>>= 8;
                    n6 = n6 + 255 >>> 8;
                }
                sArray3[n4 / 2] = (short)n5;
                sArray4[n4 / 2] = (short)n6;
            }
            if (n4 < n2) {
                sArray3[n4 / 2] = sArray[n4];
                sArray4[n4 / 2] = sArray2[n4];
            }
            Utils.encode(byArray, sArray3, sArray4, (n2 + 1) / 2, n3);
        }
    }

    protected static void getEncodedPolynomial(byte[] byArray, short[] sArray, int n2, int n3) {
        int n4;
        short[] sArray2 = new short[n2];
        short[] sArray3 = new short[n2];
        for (n4 = 0; n4 < n2; ++n4) {
            sArray2[n4] = (short)(sArray[n4] + (n3 - 1) / 2);
        }
        for (n4 = 0; n4 < n2; ++n4) {
            sArray3[n4] = (short)n3;
        }
        Utils.encode(byArray, sArray2, sArray3, n2, 0);
    }

    protected static void getEncodedSmallPolynomial(byte[] byArray, byte[] byArray2, int n2) {
        int n3 = 0;
        int n4 = 0;
        for (int i2 = 0; i2 < n2 / 4; ++i2) {
            byte by = (byte)(byArray2[n3++] + 1);
            by = (byte)(by + ((byte)(byArray2[n3++] + 1) << 2));
            by = (byte)(by + ((byte)(byArray2[n3++] + 1) << 4));
            by = (byte)(by + ((byte)(byArray2[n3++] + 1) << 6));
            byArray[n4++] = by;
        }
        byArray[n4] = (byte)(byArray2[n3] + 1);
    }

    private static void generateAES256CTRStream(byte[] byArray, byte[] byArray2, byte[] byArray3, byte[] byArray4) {
        CTRModeCipher cTRModeCipher = SICBlockCipher.newInstance(AESEngine.newInstance());
        cTRModeCipher.init(true, new ParametersWithIV(new KeyParameter(byArray4), byArray3));
        cTRModeCipher.processBytes(byArray, 0, byArray2.length, byArray2, 0);
    }

    protected static void expand(int[] nArray, byte[] byArray) {
        byte[] byArray2 = new byte[nArray.length * 4];
        byte[] byArray3 = new byte[nArray.length * 4];
        byte[] byArray4 = new byte[16];
        Utils.generateAES256CTRStream(byArray2, byArray3, byArray4, byArray);
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            nArray[i2] = Utils.bToUnsignedInt(byArray3[i2 * 4]) + (Utils.bToUnsignedInt(byArray3[i2 * 4 + 1]) << 8) + (Utils.bToUnsignedInt(byArray3[i2 * 4 + 2]) << 16) + (Utils.bToUnsignedInt(byArray3[i2 * 4 + 3]) << 24);
        }
    }

    private static int getUnsignedMod(int n2, int n3) {
        return Utils.getUnsignedDivMod(n2, n3)[1];
    }

    protected static void generatePolynomialInRQFromSeed(short[] sArray, byte[] byArray, int n2, int n3) {
        int[] nArray = new int[n2];
        Utils.expand(nArray, byArray);
        for (int i2 = 0; i2 < n2; ++i2) {
            sArray[i2] = (short)(Utils.getUnsignedMod(nArray[i2], n3) - (n3 - 1) / 2);
        }
    }

    protected static void roundPolynomial(short[] sArray, short[] sArray2) {
        for (int i2 = 0; i2 < sArray.length; ++i2) {
            sArray[i2] = (short)(sArray2[i2] - Utils.getModFreeze(sArray2[i2], 3));
        }
    }

    protected static void getRoundedEncodedPolynomial(byte[] byArray, short[] sArray, int n2, int n3) {
        short[] sArray2 = new short[n2];
        short[] sArray3 = new short[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            sArray2[i2] = (short)((sArray[i2] + (n3 - 1) / 2) * 10923 >>> 15);
            sArray3[i2] = (short)((n3 + 2) / 3);
        }
        Utils.encode(byArray, sArray2, sArray3, n2, 0);
    }

    protected static byte[] getHashWithPrefix(byte[] byArray, byte[] byArray2) {
        byte[] byArray3 = new byte[64];
        byte[] byArray4 = new byte[byArray.length + byArray2.length];
        System.arraycopy(byArray, 0, byArray4, 0, byArray.length);
        System.arraycopy(byArray2, 0, byArray4, byArray.length, byArray2.length);
        SHA512Digest sHA512Digest = new SHA512Digest();
        sHA512Digest.update(byArray4, 0, byArray4.length);
        sHA512Digest.doFinal(byArray3, 0);
        return byArray3;
    }

    private static void decode(short[] sArray, byte[] byArray, short[] sArray2, int n2, int n3, int n4) {
        if (n2 == 1) {
            sArray[n3] = sArray2[0] == 1 ? (short)0 : (sArray2[0] <= 256 ? (short)Utils.getUnsignedMod(Utils.bToUnsignedInt(byArray[n4]), sArray2[0]) : (short)Utils.getUnsignedMod(Utils.bToUnsignedInt(byArray[n4]) + (byArray[n4 + 1] << 8), sArray2[0]));
        }
        if (n2 > 1) {
            int n5;
            int n6;
            short[] sArray3 = new short[(n2 + 1) / 2];
            short[] sArray4 = new short[(n2 + 1) / 2];
            short[] sArray5 = new short[n2 / 2];
            int[] nArray = new int[n2 / 2];
            for (n6 = 0; n6 < n2 - 1; n6 += 2) {
                n5 = sArray2[n6] * sArray2[n6 + 1];
                if (n5 > 0x3FFF00) {
                    nArray[n6 / 2] = 65536;
                    sArray5[n6 / 2] = (short)(Utils.bToUnsignedInt(byArray[n4]) + 256 * Utils.bToUnsignedInt(byArray[n4 + 1]));
                    n4 += 2;
                    sArray4[n6 / 2] = (short)((n5 + 255 >>> 8) + 255 >>> 8);
                    continue;
                }
                if (n5 >= 16384) {
                    nArray[n6 / 2] = 256;
                    sArray5[n6 / 2] = (short)Utils.bToUnsignedInt(byArray[n4]);
                    ++n4;
                    sArray4[n6 / 2] = (short)(n5 + 255 >>> 8);
                    continue;
                }
                nArray[n6 / 2] = 1;
                sArray5[n6 / 2] = 0;
                sArray4[n6 / 2] = (short)n5;
            }
            if (n6 < n2) {
                sArray4[n6 / 2] = sArray2[n6];
            }
            Utils.decode(sArray3, byArray, sArray4, (n2 + 1) / 2, n3, n4);
            for (n6 = 0; n6 < n2 - 1; n6 += 2) {
                n5 = Utils.sToUnsignedInt(sArray5[n6 / 2]);
                int[] nArray2 = Utils.getUnsignedDivMod(n5 += nArray[n6 / 2] * Utils.sToUnsignedInt(sArray3[n6 / 2]), sArray2[n6]);
                sArray[n3++] = (short)nArray2[1];
                sArray[n3++] = (short)Utils.getUnsignedMod(nArray2[0], sArray2[n6 + 1]);
            }
            if (n6 < n2) {
                sArray[n3] = sArray3[n6 / 2];
            }
        }
    }

    protected static void getDecodedPolynomial(short[] sArray, byte[] byArray, int n2, int n3) {
        int n4;
        short[] sArray2 = new short[n2];
        short[] sArray3 = new short[n2];
        for (n4 = 0; n4 < n2; ++n4) {
            sArray3[n4] = (short)n3;
        }
        Utils.decode(sArray2, byArray, sArray3, n2, 0, 0);
        for (n4 = 0; n4 < n2; ++n4) {
            sArray[n4] = (short)(sArray2[n4] - (n3 - 1) / 2);
        }
    }

    protected static void getRandomInputs(SecureRandom secureRandom, byte[] byArray) {
        byte[] byArray2 = new byte[byArray.length / 8];
        secureRandom.nextBytes(byArray2);
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            byArray[i2] = (byte)(1 & byArray2[i2 >>> 3] >>> (i2 & 7));
        }
    }

    protected static void getEncodedInputs(byte[] byArray, byte[] byArray2) {
        for (int i2 = 0; i2 < byArray2.length; ++i2) {
            int n2 = i2 >>> 3;
            byArray[n2] = (byte)(byArray[n2] | byArray2[i2] << (i2 & 7));
        }
    }

    protected static void getRoundedDecodedPolynomial(short[] sArray, byte[] byArray, int n2, int n3) {
        int n4;
        short[] sArray2 = new short[n2];
        short[] sArray3 = new short[n2];
        for (n4 = 0; n4 < n2; ++n4) {
            sArray3[n4] = (short)((n3 + 2) / 3);
        }
        Utils.decode(sArray2, byArray, sArray3, n2, 0, 0);
        for (n4 = 0; n4 < n2; ++n4) {
            sArray[n4] = (short)(sArray2[n4] * 3 - (n3 - 1) / 2);
        }
    }

    protected static void top(byte[] byArray, short[] sArray, byte[] byArray2, int n2, int n3, int n4) {
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            byArray[i2] = (byte)(n4 * (Utils.getModFreeze(sArray[i2] + byArray2[i2] * ((n2 - 1) / 2), n2) + n3) + 16384 >>> 15);
        }
    }

    protected static void getTopEncodedPolynomial(byte[] byArray, byte[] byArray2) {
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            byArray[i2] = (byte)(byArray2[2 * i2] + (byArray2[2 * i2 + 1] << 4));
        }
    }

    protected static void getDecodedSmallPolynomial(byte[] byArray, byte[] byArray2, int n2) {
        byte by;
        int n3 = 0;
        int n4 = 0;
        for (int i2 = 0; i2 < n2 / 4; ++i2) {
            by = byArray2[n4++];
            byArray[n3++] = (byte)((Utils.bToUnsignedInt(by) & 3) - 1);
            by = (byte)(by >>> 2);
            byArray[n3++] = (byte)((Utils.bToUnsignedInt(by) & 3) - 1);
            by = (byte)(by >>> 2);
            byArray[n3++] = (byte)((Utils.bToUnsignedInt(by) & 3) - 1);
            by = (byte)(by >>> 2);
            byArray[n3++] = (byte)((Utils.bToUnsignedInt(by) & 3) - 1);
        }
        by = byArray2[n4];
        byArray[n3] = (byte)((Utils.bToUnsignedInt(by) & 3) - 1);
    }

    protected static void scalarMultiplicationInRQ(short[] sArray, short[] sArray2, int n2, int n3) {
        for (int i2 = 0; i2 < sArray2.length; ++i2) {
            sArray[i2] = (short)Utils.getModFreeze(n2 * sArray2[i2], n3);
        }
    }

    protected static void transformRQToR3(byte[] byArray, short[] sArray) {
        for (int i2 = 0; i2 < sArray.length; ++i2) {
            byArray[i2] = (byte)Utils.getModFreeze(sArray[i2], 3);
        }
    }

    protected static void multiplicationInR3(byte[] byArray, byte[] byArray2, byte[] byArray3, int n2) {
        int n3;
        byte by;
        int n4;
        byte[] byArray4 = new byte[n2 + n2 - 1];
        for (n4 = 0; n4 < n2; ++n4) {
            by = 0;
            for (n3 = 0; n3 <= n4; ++n3) {
                by = (byte)Utils.getModFreeze(by + byArray2[n3] * byArray3[n4 - n3], 3);
            }
            byArray4[n4] = by;
        }
        for (n4 = n2; n4 < n2 + n2 - 1; ++n4) {
            by = 0;
            for (n3 = n4 - n2 + 1; n3 < n2; ++n3) {
                by = (byte)Utils.getModFreeze(by + byArray2[n3] * byArray3[n4 - n3], 3);
            }
            byArray4[n4] = by;
        }
        for (n4 = n2 + n2 - 2; n4 >= n2; --n4) {
            byArray4[n4 - n2] = (byte)Utils.getModFreeze(byArray4[n4 - n2] + byArray4[n4], 3);
            byArray4[n4 - n2 + 1] = (byte)Utils.getModFreeze(byArray4[n4 - n2 + 1] + byArray4[n4], 3);
        }
        for (n4 = 0; n4 < n2; ++n4) {
            byArray[n4] = byArray4[n4];
        }
    }

    protected static void checkForSmallPolynomial(byte[] byArray, byte[] byArray2, int n2, int n3) {
        int n4;
        int n5;
        int n6 = 0;
        for (n5 = 0; n5 != byArray2.length; ++n5) {
            n6 += byArray2[n5] & 1;
        }
        n5 = Utils.checkNotEqualToZero(n6 - n3);
        for (n4 = 0; n4 < n3; ++n4) {
            byArray[n4] = (byte)((byArray2[n4] ^ 1) & ~n5 ^ 1);
        }
        for (n4 = n3; n4 < n2; ++n4) {
            byArray[n4] = (byte)(byArray2[n4] & ~n5);
        }
    }

    protected static void updateDiffMask(byte[] byArray, byte[] byArray2, int n2) {
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            int n3 = i2;
            byArray[n3] = (byte)(byArray[n3] ^ n2 & (byArray[i2] ^ byArray2[i2]));
        }
    }

    protected static void getTopDecodedPolynomial(byte[] byArray, byte[] byArray2) {
        for (int i2 = 0; i2 < byArray2.length; ++i2) {
            byArray[2 * i2] = (byte)(byArray2[i2] & 0xF);
            byArray[2 * i2 + 1] = (byte)(byArray2[i2] >>> 4);
        }
    }

    protected static void right(byte[] byArray, short[] sArray, byte[] byArray2, int n2, int n3, int n4, int n5) {
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            byArray[i2] = (byte)(-Utils.checkLessThanZero(Utils.getModFreeze(Utils.getModFreeze(n5 * byArray2[i2] - n4, n2) - sArray[i2] + 4 * n3 + 1, n2)));
        }
    }

    private static int[] getUnsignedDivMod(int n2, int n3) {
        long l2 = Utils.iToUnsignedLong(n2);
        long l3 = Utils.iToUnsignedLong(Integer.MIN_VALUE);
        long l4 = 0L;
        long l5 = l2 * (l3 /= (long)n3) >>> 31;
        l2 -= l5 * (long)n3;
        l4 += l5;
        l5 = l2 * l3 >>> 31;
        l2 -= l5 * (long)n3;
        l4 += l5;
        ++l4;
        long l6 = -((l2 -= (long)n3) >>> 63);
        return new int[]{Utils.toIntExact(l4 += l6), Utils.toIntExact(l2 += l6 & (long)n3)};
    }

    private static int[] getSignedDivMod(int n2, int n3) {
        int[] nArray = Utils.getUnsignedDivMod(Utils.toIntExact(Integer.MIN_VALUE + Utils.iToUnsignedLong(n2)), n3);
        int[] nArray2 = Utils.getUnsignedDivMod(Integer.MIN_VALUE, n3);
        int n4 = Utils.toIntExact(Utils.iToUnsignedLong(nArray[0]) - Utils.iToUnsignedLong(nArray2[0]));
        int n5 = Utils.toIntExact(Utils.iToUnsignedLong(nArray[1]) - Utils.iToUnsignedLong(nArray2[1]));
        int n6 = -(n5 >>> 31);
        return new int[]{n4 += n6, n5 += n6 & n3};
    }

    private static int checkLessThanZero(int n2) {
        return -(n2 >>> 31);
    }

    private static int checkNotEqualToZero(int n2) {
        long l2 = Utils.iToUnsignedLong(n2);
        l2 = -l2;
        return -((int)(l2 >>> 63));
    }

    static int bToUnsignedInt(byte by) {
        return by & 0xFF;
    }

    static int sToUnsignedInt(short s2) {
        return s2 & 0xFFFF;
    }

    static long iToUnsignedLong(int n2) {
        return (long)n2 & 0xFFFFFFFFL;
    }

    static int toIntExact(long l2) {
        int n2 = (int)l2;
        if ((long)n2 != l2) {
            throw new IllegalStateException("value out of integer range");
        }
        return n2;
    }
}

