/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.mlkem;

import org.bouncycastle.pqc.crypto.mlkem.Poly;

final class CBD {
    CBD() {
    }

    public static void mlkemCBD(Poly poly, byte[] byArray, int n2) {
        switch (n2) {
            case 3: {
                for (int i2 = 0; i2 < 64; ++i2) {
                    long l2 = CBD.convertByteTo24BitUnsignedInt(byArray, 3 * i2);
                    long l3 = l2 & 0x249249L;
                    l3 += l2 >> 1 & 0x249249L;
                    l3 += l2 >> 2 & 0x249249L;
                    for (int i3 = 0; i3 < 4; ++i3) {
                        short s2 = (short)(l3 >> 6 * i3 + 0 & 7L);
                        short s3 = (short)(l3 >> 6 * i3 + 3 & 7L);
                        poly.setCoeffIndex(4 * i2 + i3, (short)(s2 - s3));
                    }
                }
                break;
            }
            default: {
                for (int i4 = 0; i4 < 32; ++i4) {
                    long l4 = CBD.convertByteTo32BitUnsignedInt(byArray, 4 * i4);
                    long l5 = l4 & 0x55555555L;
                    l5 += l4 >> 1 & 0x55555555L;
                    for (int i5 = 0; i5 < 8; ++i5) {
                        short s4 = (short)(l5 >> 4 * i5 + 0 & 3L);
                        short s5 = (short)(l5 >> 4 * i5 + n2 & 3L);
                        poly.setCoeffIndex(8 * i4 + i5, (short)(s4 - s5));
                    }
                }
            }
        }
    }

    private static long convertByteTo32BitUnsignedInt(byte[] byArray, int n2) {
        long l2 = byArray[n2] & 0xFF;
        l2 |= (long)(byArray[n2 + 1] & 0xFF) << 8;
        l2 |= (long)(byArray[n2 + 2] & 0xFF) << 16;
        return l2 |= (long)(byArray[n2 + 3] & 0xFF) << 24;
    }

    private static long convertByteTo24BitUnsignedInt(byte[] byArray, int n2) {
        long l2 = byArray[n2] & 0xFF;
        l2 |= (long)(byArray[n2 + 1] & 0xFF) << 8;
        return l2 |= (long)(byArray[n2 + 2] & 0xFF) << 16;
    }
}

