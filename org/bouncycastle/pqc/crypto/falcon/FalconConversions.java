/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.falcon;

class FalconConversions {
    FalconConversions() {
    }

    byte[] int_to_bytes(int n2) {
        byte[] byArray = new byte[]{(byte)(n2 >>> 0), (byte)(n2 >>> 8), (byte)(n2 >>> 16), (byte)(n2 >>> 24)};
        return byArray;
    }

    int bytes_to_int(byte[] byArray, int n2) {
        int n3 = 0;
        n3 = this.toUnsignedInt(byArray[n2 + 0]) << 0 | this.toUnsignedInt(byArray[n2 + 1]) << 8 | this.toUnsignedInt(byArray[n2 + 2]) << 16 | this.toUnsignedInt(byArray[n2 + 3]) << 24;
        return n3;
    }

    int[] bytes_to_int_array(byte[] byArray, int n2, int n3) {
        int[] nArray = new int[n3];
        for (int i2 = 0; i2 < n3; ++i2) {
            nArray[i2] = this.bytes_to_int(byArray, n2 + 4 * i2);
        }
        return nArray;
    }

    byte[] long_to_bytes(long l2) {
        byte[] byArray = new byte[]{(byte)(l2 >>> 0), (byte)(l2 >>> 8), (byte)(l2 >>> 16), (byte)(l2 >>> 24), (byte)(l2 >>> 32), (byte)(l2 >>> 40), (byte)(l2 >>> 48), (byte)(l2 >>> 56)};
        return byArray;
    }

    long bytes_to_long(byte[] byArray, int n2) {
        long l2 = 0L;
        l2 = this.toUnsignedLong(byArray[n2 + 0]) << 0 | this.toUnsignedLong(byArray[n2 + 1]) << 8 | this.toUnsignedLong(byArray[n2 + 2]) << 16 | this.toUnsignedLong(byArray[n2 + 3]) << 24 | this.toUnsignedLong(byArray[n2 + 4]) << 32 | this.toUnsignedLong(byArray[n2 + 5]) << 40 | this.toUnsignedLong(byArray[n2 + 6]) << 48 | this.toUnsignedLong(byArray[n2 + 7]) << 56;
        return l2;
    }

    private int toUnsignedInt(byte by) {
        return by & 0xFF;
    }

    private long toUnsignedLong(byte by) {
        return (long)by & 0xFFL;
    }
}

