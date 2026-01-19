/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.falcon;

import org.bouncycastle.pqc.crypto.falcon.SHAKE256;

class FalconCommon {
    static final int[] l2bound = new int[]{0, 101498, 208714, 428865, 892039, 1852696, 3842630, 7959734, 16468416, 34034726, 70265242};

    FalconCommon() {
    }

    void hash_to_point_vartime(SHAKE256 sHAKE256, short[] sArray, int n2, int n3) {
        int n4 = 1 << n3;
        while (n4 > 0) {
            byte[] byArray = new byte[2];
            sHAKE256.inner_shake256_extract(byArray, 0, 2);
            int n5 = (byArray[0] & 0xFF) << 8 | byArray[1] & 0xFF;
            if (n5 >= 61445) continue;
            while (n5 >= 12289) {
                n5 -= 12289;
            }
            sArray[n2++] = (short)n5;
            --n4;
        }
    }

    void hash_to_point_ct(SHAKE256 sHAKE256, short[] sArray, int n2, int n3, short[] sArray2, int n4) {
        int n5;
        int n6;
        int n7;
        short[] sArray3 = new short[]{0, 65, 67, 71, 77, 86, 100, 122, 154, 205, 287};
        short[] sArray4 = new short[63];
        int n8 = 1 << n3;
        int n9 = n8 << 1;
        int n10 = sArray3[n3];
        int n11 = n8 + n10;
        int n12 = n4;
        for (n7 = 0; n7 < n11; ++n7) {
            byte[] byArray = new byte[2];
            sHAKE256.inner_shake256_extract(byArray, 0, byArray.length);
            n6 = (byArray[0] & 0xFF) << 8 | byArray[1] & 0xFF;
            n5 = n6 - (0x6002 & (n6 - 24578 >>> 31) - 1);
            n5 -= 0x6002 & (n5 - 24578 >>> 31) - 1;
            n5 -= 0x3001 & (n5 - 12289 >>> 31) - 1;
            n5 |= (n6 - 61445 >>> 31) - 1;
            if (n7 < n8) {
                sArray[n2 + n7] = (short)n5;
                continue;
            }
            if (n7 < n9) {
                sArray2[n12 + n7 - n8] = (short)n5;
                continue;
            }
            sArray4[n7 - n9] = (short)n5;
        }
        for (int i2 = 1; i2 <= n10; i2 <<= 1) {
            int n13 = 0;
            for (n7 = 0; n7 < n11; ++n7) {
                short s2;
                int n14;
                short s3;
                int n15;
                if (n7 < n8) {
                    n15 = 1;
                    n6 = n2 + n7;
                    s3 = sArray[n6];
                } else if (n7 < n9) {
                    n15 = 2;
                    n6 = n12 + n7 - n8;
                    s3 = sArray2[n6];
                } else {
                    n15 = 3;
                    n6 = n7 - n9;
                    s3 = sArray4[n6];
                }
                int n16 = n7 - n13;
                int n17 = (s3 >>> 15) - 1;
                n13 -= n17;
                if (n7 < i2) continue;
                if (n7 - i2 < n8) {
                    n14 = 1;
                    n5 = n2 + n7 - i2;
                    s2 = sArray[n5];
                } else if (n7 - i2 < n9) {
                    n14 = 2;
                    n5 = n12 + (n7 - i2) - n8;
                    s2 = sArray2[n5];
                } else {
                    n14 = 3;
                    n5 = n7 - i2 - n9;
                    s2 = sArray4[n5];
                }
                n17 &= -((n16 & i2) + 511 >> 9);
                if (n15 == 1) {
                    sArray[n6] = (short)(s3 ^ n17 & (s3 ^ s2));
                } else if (n15 == 2) {
                    sArray2[n6] = (short)(s3 ^ n17 & (s3 ^ s2));
                } else {
                    sArray4[n6] = (short)(s3 ^ n17 & (s3 ^ s2));
                }
                if (n14 == 1) {
                    sArray[n5] = (short)(s2 ^ n17 & (s3 ^ s2));
                    continue;
                }
                if (n14 == 2) {
                    sArray2[n5] = (short)(s2 ^ n17 & (s3 ^ s2));
                    continue;
                }
                sArray4[n5] = (short)(s2 ^ n17 & (s3 ^ s2));
            }
        }
    }

    int is_short(short[] sArray, int n2, short[] sArray2, int n3, int n4) {
        int n5 = 1 << n4;
        int n6 = 0;
        int n7 = 0;
        for (int i2 = 0; i2 < n5; ++i2) {
            short s2 = sArray[n2 + i2];
            n7 |= (n6 += s2 * s2);
            s2 = sArray2[n3 + i2];
            n7 |= (n6 += s2 * s2);
        }
        return ((long)(n6 |= -(n7 >>> 31)) & 0xFFFFFFFFL) <= (long)l2bound[n4] ? 1 : 0;
    }

    int is_short_half(int n2, short[] sArray, int n3, int n4) {
        int n5 = 1 << n4;
        int n6 = -(n2 >>> 31);
        for (int i2 = 0; i2 < n5; ++i2) {
            short s2 = sArray[n3 + i2];
            n6 |= (n2 += s2 * s2);
        }
        return ((long)(n2 |= -(n6 >>> 31)) & 0xFFFFFFFFL) <= (long)l2bound[n4] ? 1 : 0;
    }
}

