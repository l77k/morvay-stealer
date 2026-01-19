/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.rainbow;

import java.security.SecureRandom;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.util.Arrays;

class RainbowUtil {
    RainbowUtil() {
    }

    public static short[] convertArray(byte[] byArray) {
        short[] sArray = new short[byArray.length];
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            sArray[i2] = (short)(byArray[i2] & 0xFF);
        }
        return sArray;
    }

    public static byte[] convertArray(short[] sArray) {
        byte[] byArray = new byte[sArray.length];
        for (int i2 = 0; i2 < sArray.length; ++i2) {
            byArray[i2] = (byte)sArray[i2];
        }
        return byArray;
    }

    public static boolean equals(short[] sArray, short[] sArray2) {
        if (sArray.length != sArray2.length) {
            return false;
        }
        boolean bl = true;
        for (int i2 = sArray.length - 1; i2 >= 0; --i2) {
            bl &= sArray[i2] == sArray2[i2];
        }
        return bl;
    }

    public static boolean equals(short[][] sArray, short[][] sArray2) {
        if (sArray.length != sArray2.length) {
            return false;
        }
        boolean bl = true;
        for (int i2 = sArray.length - 1; i2 >= 0; --i2) {
            bl &= RainbowUtil.equals(sArray[i2], sArray2[i2]);
        }
        return bl;
    }

    public static boolean equals(short[][][] sArray, short[][][] sArray2) {
        if (sArray.length != sArray2.length) {
            return false;
        }
        boolean bl = true;
        for (int i2 = sArray.length - 1; i2 >= 0; --i2) {
            bl &= RainbowUtil.equals(sArray[i2], sArray2[i2]);
        }
        return bl;
    }

    public static short[][] cloneArray(short[][] sArray) {
        short[][] sArrayArray = new short[sArray.length][];
        for (int i2 = 0; i2 < sArray.length; ++i2) {
            sArrayArray[i2] = Arrays.clone(sArray[i2]);
        }
        return sArrayArray;
    }

    public static short[][][] cloneArray(short[][][] sArray) {
        short[][][] sArray2 = new short[sArray.length][sArray[0].length][];
        for (int i2 = 0; i2 < sArray.length; ++i2) {
            for (int i3 = 0; i3 < sArray[0].length; ++i3) {
                sArray2[i2][i3] = Arrays.clone(sArray[i2][i3]);
            }
        }
        return sArray2;
    }

    public static byte[] hash(Digest digest, byte[] byArray, byte[] byArray2, byte[] byArray3) {
        int n2 = digest.getDigestSize();
        digest.update(byArray, 0, byArray.length);
        digest.update(byArray2, 0, byArray2.length);
        if (byArray3.length == n2) {
            digest.doFinal(byArray3, 0);
            return byArray3;
        }
        byte[] byArray4 = new byte[n2];
        digest.doFinal(byArray4, 0);
        if (byArray3.length < n2) {
            System.arraycopy(byArray4, 0, byArray3, 0, byArray3.length);
            return byArray3;
        }
        System.arraycopy(byArray4, 0, byArray3, 0, byArray4.length);
        int n3 = byArray3.length - n2;
        int n4 = n2;
        while (n3 >= byArray4.length) {
            digest.update(byArray4, 0, byArray4.length);
            digest.doFinal(byArray4, 0);
            System.arraycopy(byArray4, 0, byArray3, n4, byArray4.length);
            n3 -= byArray4.length;
            n4 += byArray4.length;
        }
        if (n3 > 0) {
            digest.update(byArray4, 0, byArray4.length);
            digest.doFinal(byArray4, 0);
            System.arraycopy(byArray4, 0, byArray3, n4, n3);
        }
        return byArray3;
    }

    public static byte[] hash(Digest digest, byte[] byArray, int n2) {
        int n3;
        int n4 = digest.getDigestSize();
        digest.update(byArray, 0, byArray.length);
        byte[] byArray2 = new byte[n4];
        digest.doFinal(byArray2, 0);
        if (n2 == n4) {
            return byArray2;
        }
        if (n2 < n4) {
            return Arrays.copyOf(byArray2, n2);
        }
        byte[] byArray3 = Arrays.copyOf(byArray2, n4);
        for (n3 = n2 - n4; n3 >= n4; n3 -= n4) {
            digest.update(byArray2, 0, n4);
            byArray2 = new byte[n4];
            digest.doFinal(byArray2, 0);
            byArray3 = Arrays.concatenate(byArray3, byArray2);
        }
        if (n3 > 0) {
            digest.update(byArray2, 0, n4);
            byArray2 = new byte[n4];
            digest.doFinal(byArray2, 0);
            int n5 = byArray3.length;
            byArray3 = Arrays.copyOf(byArray3, n5 + n3);
            System.arraycopy(byArray2, 0, byArray3, n5, n3);
        }
        return byArray3;
    }

    public static short[][] generate_random_2d(SecureRandom secureRandom, int n2, int n3) {
        byte[] byArray = new byte[n2 * n3];
        secureRandom.nextBytes(byArray);
        short[][] sArray = new short[n2][n3];
        for (int i2 = 0; i2 < n3; ++i2) {
            for (int i3 = 0; i3 < n2; ++i3) {
                sArray[i3][i2] = (short)(byArray[i2 * n2 + i3] & 0xFF);
            }
        }
        return sArray;
    }

    public static short[][][] generate_random(SecureRandom secureRandom, int n2, int n3, int n4, boolean bl) {
        int n5 = bl ? n2 * (n3 * (n3 + 1) / 2) : n2 * n3 * n4;
        byte[] byArray = new byte[n5];
        secureRandom.nextBytes(byArray);
        int n6 = 0;
        short[][][] sArray = new short[n2][n3][n4];
        for (int i2 = 0; i2 < n3; ++i2) {
            for (int i3 = 0; i3 < n4; ++i3) {
                for (int i4 = 0; i4 < n2; ++i4) {
                    if (bl && i2 > i3) continue;
                    sArray[i4][i2][i3] = (short)(byArray[n6++] & 0xFF);
                }
            }
        }
        return sArray;
    }

    public static byte[] getEncoded(short[][] sArray) {
        int n2 = sArray.length;
        int n3 = sArray[0].length;
        byte[] byArray = new byte[n2 * n3];
        for (int i2 = 0; i2 < n3; ++i2) {
            for (int i3 = 0; i3 < n2; ++i3) {
                byArray[i2 * n2 + i3] = (byte)sArray[i3][i2];
            }
        }
        return byArray;
    }

    public static byte[] getEncoded(short[][][] sArray, boolean bl) {
        int n2 = sArray.length;
        int n3 = sArray[0].length;
        int n4 = sArray[0][0].length;
        int n5 = bl ? n2 * (n3 * (n3 + 1) / 2) : n2 * n3 * n4;
        byte[] byArray = new byte[n5];
        int n6 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            for (int i3 = 0; i3 < n4; ++i3) {
                for (int i4 = 0; i4 < n2; ++i4) {
                    if (bl && i2 > i3) continue;
                    byArray[n6] = (byte)sArray[i4][i2][i3];
                    ++n6;
                }
            }
        }
        return byArray;
    }

    public static int loadEncoded(short[][] sArray, byte[] byArray, int n2) {
        int n3 = sArray.length;
        int n4 = sArray[0].length;
        for (int i2 = 0; i2 < n4; ++i2) {
            for (int i3 = 0; i3 < n3; ++i3) {
                sArray[i3][i2] = (short)(byArray[n2 + i2 * n3 + i3] & 0xFF);
            }
        }
        return n3 * n4;
    }

    public static int loadEncoded(short[][][] sArray, byte[] byArray, int n2, boolean bl) {
        int n3 = sArray.length;
        int n4 = sArray[0].length;
        int n5 = sArray[0][0].length;
        int n6 = 0;
        for (int i2 = 0; i2 < n4; ++i2) {
            for (int i3 = 0; i3 < n5; ++i3) {
                for (int i4 = 0; i4 < n3; ++i4) {
                    if (bl && i2 > i3) continue;
                    sArray[i4][i2][i3] = (short)(byArray[n2 + n6++] & 0xFF);
                }
            }
        }
        return n6;
    }
}

