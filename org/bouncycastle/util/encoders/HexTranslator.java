/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.util.encoders;

import org.bouncycastle.util.encoders.Translator;

public class HexTranslator
implements Translator {
    private static final byte[] hexTable = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};

    @Override
    public int getEncodedBlockSize() {
        return 2;
    }

    @Override
    public int encode(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) {
        int n5 = 0;
        int n6 = 0;
        while (n5 < n3) {
            byArray2[n4 + n6] = hexTable[byArray[n2] >> 4 & 0xF];
            byArray2[n4 + n6 + 1] = hexTable[byArray[n2] & 0xF];
            ++n2;
            ++n5;
            n6 += 2;
        }
        return n3 * 2;
    }

    @Override
    public int getDecodedBlockSize() {
        return 1;
    }

    @Override
    public int decode(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) {
        int n5 = n3 / 2;
        for (int i2 = 0; i2 < n5; ++i2) {
            byte by = byArray[n2 + i2 * 2];
            byte by2 = byArray[n2 + i2 * 2 + 1];
            byArray2[n4] = by < 97 ? (byte)(by - 48 << 4) : (byte)(by - 97 + 10 << 4);
            if (by2 < 97) {
                int n6 = n4;
                byArray2[n6] = (byte)(byArray2[n6] + (byte)(by2 - 48));
            } else {
                int n7 = n4;
                byArray2[n7] = (byte)(byArray2[n7] + (byte)(by2 - 97 + 10));
            }
            ++n4;
        }
        return n5;
    }
}

