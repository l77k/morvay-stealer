/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec.rfc8032;

abstract class Codec {
    Codec() {
    }

    static int decode16(byte[] byArray, int n2) {
        int n3 = byArray[n2] & 0xFF;
        return n3 |= (byArray[++n2] & 0xFF) << 8;
    }

    static int decode24(byte[] byArray, int n2) {
        int n3 = byArray[n2] & 0xFF;
        n3 |= (byArray[++n2] & 0xFF) << 8;
        return n3 |= (byArray[++n2] & 0xFF) << 16;
    }

    static int decode32(byte[] byArray, int n2) {
        int n3 = byArray[n2] & 0xFF;
        n3 |= (byArray[++n2] & 0xFF) << 8;
        n3 |= (byArray[++n2] & 0xFF) << 16;
        return n3 |= byArray[++n2] << 24;
    }

    static void decode32(byte[] byArray, int n2, int[] nArray, int n3, int n4) {
        for (int i2 = 0; i2 < n4; ++i2) {
            nArray[n3 + i2] = Codec.decode32(byArray, n2 + i2 * 4);
        }
    }

    static void encode24(int n2, byte[] byArray, int n3) {
        byArray[n3] = (byte)n2;
        byArray[++n3] = (byte)(n2 >>> 8);
        byArray[++n3] = (byte)(n2 >>> 16);
    }

    static void encode32(int n2, byte[] byArray, int n3) {
        byArray[n3] = (byte)n2;
        byArray[++n3] = (byte)(n2 >>> 8);
        byArray[++n3] = (byte)(n2 >>> 16);
        byArray[++n3] = (byte)(n2 >>> 24);
    }

    static void encode32(int[] nArray, int n2, int n3, byte[] byArray, int n4) {
        for (int i2 = 0; i2 < n3; ++i2) {
            Codec.encode32(nArray[n2 + i2], byArray, n4 + i2 * 4);
        }
    }

    static void encode56(long l2, byte[] byArray, int n2) {
        Codec.encode32((int)l2, byArray, n2);
        Codec.encode24((int)(l2 >>> 32), byArray, n2 + 4);
    }
}

