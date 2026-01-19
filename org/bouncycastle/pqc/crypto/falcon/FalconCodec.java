/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.falcon;

class FalconCodec {
    final byte[] max_fg_bits = new byte[]{0, 8, 8, 8, 8, 8, 7, 7, 6, 6, 5};
    final byte[] max_FG_bits = new byte[]{0, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8};
    final byte[] max_sig_bits = new byte[]{0, 10, 11, 11, 12, 12, 12, 12, 12, 12, 12};

    FalconCodec() {
    }

    int modq_encode(byte[] byArray, int n2, int n3, short[] sArray, int n4, int n5) {
        int n6;
        int n7 = 1 << n5;
        for (n6 = 0; n6 < n7; ++n6) {
            if ((sArray[n4 + n6] & 0xFFFF) < 12289) continue;
            return 0;
        }
        int n8 = n7 * 14 + 7 >> 3;
        if (byArray == null) {
            return n8;
        }
        if (n8 > n3) {
            return 0;
        }
        int n9 = n2;
        int n10 = 0;
        int n11 = 0;
        for (n6 = 0; n6 < n7; ++n6) {
            n10 = n10 << 14 | sArray[n4 + n6] & 0xFFFF;
            n11 += 14;
            while (n11 >= 8) {
                byArray[n9++] = (byte)(n10 >> (n11 -= 8));
            }
        }
        if (n11 > 0) {
            byArray[n9] = (byte)(n10 << 8 - n11);
        }
        return n8;
    }

    int modq_decode(short[] sArray, int n2, int n3, byte[] byArray, int n4, int n5) {
        int n6 = 1 << n3;
        int n7 = n6 * 14 + 7 >> 3;
        if (n7 > n5) {
            return 0;
        }
        int n8 = n4;
        int n9 = 0;
        int n10 = 0;
        int n11 = 0;
        while (n11 < n6) {
            n9 = n9 << 8 | byArray[n8++] & 0xFF;
            if ((n10 += 8) < 14) continue;
            int n12 = n9 >>> (n10 -= 14) & 0x3FFF;
            if (n12 >= 12289) {
                return 0;
            }
            sArray[n2 + n11] = (short)n12;
            ++n11;
        }
        if ((n9 & (1 << n10) - 1) != 0) {
            return 0;
        }
        return n7;
    }

    int trim_i16_encode(byte[] byArray, int n2, int n3, short[] sArray, int n4, int n5, int n6) {
        int n7;
        int n8 = 1 << n5;
        int n9 = (1 << n6 - 1) - 1;
        int n10 = -n9;
        for (n7 = 0; n7 < n8; ++n7) {
            if (sArray[n4 + n7] >= n10 && sArray[n4 + n7] <= n9) continue;
            return 0;
        }
        int n11 = n8 * n6 + 7 >> 3;
        if (byArray == null) {
            return n11;
        }
        if (n11 > n3) {
            return 0;
        }
        int n12 = n2;
        int n13 = 0;
        int n14 = 0;
        int n15 = (1 << n6) - 1;
        for (n7 = 0; n7 < n8; ++n7) {
            n13 = n13 << n6 | sArray[n4 + n7] & 0xFFF & n15;
            n14 += n6;
            while (n14 >= 8) {
                byArray[n12++] = (byte)(n13 >> (n14 -= 8));
            }
        }
        if (n14 > 0) {
            byArray[n12++] = (byte)(n13 << 8 - n14);
        }
        return n11;
    }

    int trim_i16_decode(short[] sArray, int n2, int n3, int n4, byte[] byArray, int n5, int n6) {
        int n7 = 1 << n3;
        int n8 = n7 * n4 + 7 >> 3;
        if (n8 > n6) {
            return 0;
        }
        int n9 = n5;
        int n10 = 0;
        int n11 = 0;
        int n12 = 0;
        int n13 = (1 << n4) - 1;
        int n14 = 1 << n4 - 1;
        while (n10 < n7) {
            n11 = n11 << 8 | byArray[n9++] & 0xFF;
            n12 += 8;
            while (n12 >= n4 && n10 < n7) {
                int n15 = n11 >>> (n12 -= n4) & n13;
                if ((n15 |= -(n15 & n14)) == -n14) {
                    return 0;
                }
                n15 |= -(n15 & n14);
                sArray[n2 + n10] = (short)n15;
                ++n10;
            }
        }
        if ((n11 & (1 << n12) - 1) != 0) {
            return 0;
        }
        return n8;
    }

    int trim_i8_encode(byte[] byArray, int n2, int n3, byte[] byArray2, int n4, int n5, int n6) {
        int n7;
        int n8 = 1 << n5;
        int n9 = (1 << n6 - 1) - 1;
        int n10 = -n9;
        for (n7 = 0; n7 < n8; ++n7) {
            if (byArray2[n4 + n7] >= n10 && byArray2[n4 + n7] <= n9) continue;
            return 0;
        }
        int n11 = n8 * n6 + 7 >> 3;
        if (byArray == null) {
            return n11;
        }
        if (n11 > n3) {
            return 0;
        }
        int n12 = n2;
        int n13 = 0;
        int n14 = 0;
        int n15 = (1 << n6) - 1;
        for (n7 = 0; n7 < n8; ++n7) {
            n13 = n13 << n6 | byArray2[n4 + n7] & 0xFFFF & n15;
            n14 += n6;
            while (n14 >= 8) {
                byArray[n12++] = (byte)(n13 >>> (n14 -= 8));
            }
        }
        if (n14 > 0) {
            byArray[n12++] = (byte)(n13 << 8 - n14);
        }
        return n11;
    }

    int trim_i8_decode(byte[] byArray, int n2, int n3, int n4, byte[] byArray2, int n5, int n6) {
        int n7 = 1 << n3;
        int n8 = n7 * n4 + 7 >> 3;
        if (n8 > n6) {
            return 0;
        }
        int n9 = n5;
        int n10 = 0;
        int n11 = 0;
        int n12 = 0;
        int n13 = (1 << n4) - 1;
        int n14 = 1 << n4 - 1;
        while (n10 < n7) {
            n11 = n11 << 8 | byArray2[n9++] & 0xFF;
            n12 += 8;
            while (n12 >= n4 && n10 < n7) {
                int n15 = n11 >>> (n12 -= n4) & n13;
                if ((n15 |= -(n15 & n14)) == -n14) {
                    return 0;
                }
                byArray[n2 + n10] = (byte)n15;
                ++n10;
            }
        }
        if ((n11 & (1 << n12) - 1) != 0) {
            return 0;
        }
        return n8;
    }

    int comp_encode(byte[] byArray, int n2, int n3, short[] sArray, int n4, int n5) {
        int n6;
        int n7 = 1 << n5;
        int n8 = n2;
        for (n6 = 0; n6 < n7; ++n6) {
            if (sArray[n4 + n6] >= -2047 && sArray[n4 + n6] <= 2047) continue;
            return 0;
        }
        int n9 = 0;
        int n10 = 0;
        int n11 = 0;
        for (n6 = 0; n6 < n7; ++n6) {
            n9 <<= 1;
            int n12 = sArray[n4 + n6];
            if (n12 < 0) {
                n12 = -n12;
                n9 |= 1;
            }
            int n13 = n12;
            n9 <<= 7;
            n9 |= n13 & 0x7F;
            n10 += 8;
            n9 <<= (n13 >>>= 7) + 1;
            n9 |= 1;
            n10 += n13 + 1;
            while (n10 >= 8) {
                n10 -= 8;
                if (byArray != null) {
                    if (n11 >= n3) {
                        return 0;
                    }
                    byArray[n8 + n11] = (byte)(n9 >>> n10);
                }
                ++n11;
            }
        }
        if (n10 > 0) {
            if (byArray != null) {
                if (n11 >= n3) {
                    return 0;
                }
                byArray[n8 + n11] = (byte)(n9 << 8 - n10);
            }
            ++n11;
        }
        return n11;
    }

    int comp_decode(short[] sArray, int n2, int n3, byte[] byArray, int n4, int n5) {
        int n6 = 1 << n3;
        int n7 = n4;
        int n8 = 0;
        int n9 = 0;
        int n10 = 0;
        for (int i2 = 0; i2 < n6; ++i2) {
            int n11;
            int n12;
            block7: {
                if (n10 >= n5) {
                    return 0;
                }
                n8 = n8 << 8 | byArray[n7 + n10] & 0xFF;
                ++n10;
                int n13 = n8 >>> n9;
                n12 = n13 & 0x80;
                n11 = n13 & 0x7F;
                do {
                    if (n9 == 0) {
                        if (n10 >= n5) {
                            return 0;
                        }
                        n8 = n8 << 8 | byArray[n7 + n10] & 0xFF;
                        ++n10;
                        n9 = 8;
                    }
                    if ((n8 >>> --n9 & 1) != 0) break block7;
                } while ((n11 += 128) <= 2047);
                return 0;
            }
            if (n12 != 0 && n11 == 0) {
                return 0;
            }
            sArray[n2 + i2] = (short)(n12 != 0 ? -n11 : n11);
        }
        if ((n8 & (1 << n9) - 1) != 0) {
            return 0;
        }
        return n10;
    }
}

