/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.rainbow;

import org.bouncycastle.util.Pack;

class GF2Field {
    static final byte[][] gfMulTable;
    static final byte[] gfInvTable;
    public static final int MASK = 255;

    GF2Field() {
    }

    private static short gf4Mul2(short s2) {
        int n2 = s2 << 1;
        return (short)((n2 ^= (s2 >>> 1) * 7) & 0xFF);
    }

    private static short gf4Mul3(short s2) {
        int n2 = s2 - 2 >>> 1;
        int n3 = n2 & s2 * 3 | ~n2 & s2 - 1;
        return (short)(n3 & 0xFF);
    }

    private static short gf4Mul(short s2, short s3) {
        int n2 = s2 * (s3 & 1);
        return (short)((n2 ^= GF2Field.gf4Mul2(s2) * (s3 >>> 1)) & 0xFF);
    }

    private static short gf4Squ(short s2) {
        int n2 = s2 ^ s2 >>> 1;
        return (short)(n2 & 0xFF);
    }

    private static short gf16Mul(short s2, short s3) {
        short s4 = (short)(s2 & 3 & 0xFF);
        short s5 = (short)(s2 >>> 2 & 0xFF);
        short s6 = (short)(s3 & 3 & 0xFF);
        short s7 = (short)(s3 >>> 2 & 0xFF);
        short s8 = GF2Field.gf4Mul(s4, s6);
        short s9 = GF2Field.gf4Mul(s5, s7);
        short s10 = (short)(GF2Field.gf4Mul((short)(s4 ^ s5), (short)(s6 ^ s7)) ^ s8);
        short s11 = GF2Field.gf4Mul2(s9);
        return (short)((s10 << 2 ^ s8 ^ s11) & 0xFF);
    }

    private static short gf16Squ(short s2) {
        short s3 = (short)(s2 & 3 & 0xFF);
        short s4 = (short)(s2 >>> 2 & 0xFF);
        s4 = GF2Field.gf4Squ(s4);
        short s5 = GF2Field.gf4Mul2(s4);
        return (short)((s4 << 2 ^ s5 ^ GF2Field.gf4Squ(s3)) & 0xFF);
    }

    private static short gf16Mul8(short s2) {
        short s3 = (short)(s2 & 3 & 0xFF);
        short s4 = (short)(s2 >>> 2 & 0xFF);
        int n2 = GF2Field.gf4Mul2((short)(s3 ^ s4)) << 2;
        return (short)((n2 |= GF2Field.gf4Mul3(s4)) & 0xFF);
    }

    private static short gf256Mul(short s2, short s3) {
        short s4 = (short)(s2 & 0xF & 0xFF);
        short s5 = (short)(s2 >>> 4 & 0xFF);
        short s6 = (short)(s3 & 0xF & 0xFF);
        short s7 = (short)(s3 >>> 4 & 0xFF);
        short s8 = GF2Field.gf16Mul(s4, s6);
        short s9 = GF2Field.gf16Mul(s5, s7);
        short s10 = (short)(GF2Field.gf16Mul((short)(s4 ^ s5), (short)(s6 ^ s7)) ^ s8);
        short s11 = GF2Field.gf16Mul8(s9);
        return (short)((s10 << 4 ^ s8 ^ s11) & 0xFF);
    }

    private static short gf256Squ(short s2) {
        short s3 = (short)(s2 & 0xF & 0xFF);
        short s4 = (short)(s2 >>> 4 & 0xFF);
        s4 = GF2Field.gf16Squ(s4);
        short s5 = GF2Field.gf16Mul8(s4);
        return (short)((s4 << 4 ^ s5 ^ GF2Field.gf16Squ(s3)) & 0xFF);
    }

    private static short gf256Inv(short s2) {
        short s3 = GF2Field.gf256Squ(s2);
        short s4 = GF2Field.gf256Squ(s3);
        short s5 = GF2Field.gf256Squ(s4);
        short s6 = GF2Field.gf256Mul(s4, s3);
        short s7 = GF2Field.gf256Mul(s6, s5);
        short s8 = GF2Field.gf256Squ(s7);
        s8 = GF2Field.gf256Squ(s8);
        s8 = GF2Field.gf256Squ(s8);
        short s9 = GF2Field.gf256Mul(s8, s7);
        short s10 = GF2Field.gf256Squ(s9);
        return GF2Field.gf256Mul(s3, s10);
    }

    public static short addElem(short s2, short s3) {
        return (short)(s2 ^ s3);
    }

    public static long addElem_64(long l2, long l3) {
        return l2 ^ l3;
    }

    public static short invElem(short s2) {
        return (short)(gfInvTable[s2] & 0xFF);
    }

    public static long invElem_64(long l2) {
        return GF2Field.gf256Inv_64(l2);
    }

    public static short multElem(short s2, short s3) {
        return (short)(gfMulTable[s2][s3] & 0xFF);
    }

    public static long multElem_64(long l2, long l3) {
        return GF2Field.gf256Mul_64(l2, l3);
    }

    private static long gf4Mul2_64(long l2) {
        long l3 = l2 & 0x5555555555555555L;
        long l4 = l2 & 0xAAAAAAAAAAAAAAAAL;
        return l4 ^ l3 << 1 ^ l4 >>> 1;
    }

    private static long gf4Mul_64(long l2, long l3) {
        long l4 = (l2 << 1 & l3 ^ l3 << 1 & l2) & 0xAAAAAAAAAAAAAAAAL;
        long l5 = l2 & l3;
        return l5 ^ l4 ^ (l5 & 0xAAAAAAAAAAAAAAAAL) >>> 1;
    }

    private static long gf4Squ_64(long l2) {
        long l3 = l2 & 0xAAAAAAAAAAAAAAAAL;
        return l2 ^ l3 >>> 1;
    }

    private static long gf16Mul_64(long l2, long l3) {
        long l4 = GF2Field.gf4Mul_64(l2, l3);
        long l5 = l4 & 0x3333333333333333L;
        long l6 = l4 & 0xCCCCCCCCCCCCCCCCL;
        long l7 = (l2 << 2 ^ l2) & 0xCCCCCCCCCCCCCCCCL ^ l6 >>> 2;
        long l8 = (l3 << 2 ^ l3) & 0xCCCCCCCCCCCCCCCCL ^ 0x2222222222222222L;
        long l9 = GF2Field.gf4Mul_64(l7, l8);
        return l9 ^ l5 << 2 ^ l5;
    }

    private static long gf16Squ_64(long l2) {
        long l3 = GF2Field.gf4Squ_64(l2);
        long l4 = GF2Field.gf4Mul2_64(l3 & 0xCCCCCCCCCCCCCCCCL);
        return l3 ^ l4 >>> 2;
    }

    private static long gf16Mul8_64(long l2) {
        long l3 = l2 & 0x3333333333333333L;
        long l4 = l2 & 0xCCCCCCCCCCCCCCCCL;
        long l5 = l3 << 2 ^ l4 ^ l4 >>> 2;
        long l6 = GF2Field.gf4Mul2_64(l5);
        return l6 ^ l4 >>> 2;
    }

    private static long gf256Mul_64(long l2, long l3) {
        long l4 = GF2Field.gf16Mul_64(l2, l3);
        long l5 = l4 & 0xF0F0F0F0F0F0F0FL;
        long l6 = l4 & 0xF0F0F0F0F0F0F0F0L;
        long l7 = (l2 << 4 ^ l2) & 0xF0F0F0F0F0F0F0F0L ^ l6 >>> 4;
        long l8 = (l3 << 4 ^ l3) & 0xF0F0F0F0F0F0F0F0L ^ 0x808080808080808L;
        long l9 = GF2Field.gf16Mul_64(l7, l8);
        return l9 ^ l5 << 4 ^ l5;
    }

    private static long gf256Squ_64(long l2) {
        long l3 = GF2Field.gf16Squ_64(l2);
        long l4 = l3 & 0xF0F0F0F0F0F0F0F0L;
        long l5 = GF2Field.gf16Mul8_64(l4);
        return l3 ^ l5 >>> 4;
    }

    private static long gf256Inv_64(long l2) {
        long l3 = GF2Field.gf256Squ_64(l2);
        long l4 = GF2Field.gf256Squ_64(l3);
        long l5 = GF2Field.gf256Squ_64(l4);
        long l6 = GF2Field.gf256Mul_64(l4, l3);
        long l7 = GF2Field.gf256Mul_64(l6, l5);
        long l8 = GF2Field.gf256Squ_64(l7);
        l8 = GF2Field.gf256Squ_64(l8);
        l8 = GF2Field.gf256Squ_64(l8);
        long l9 = GF2Field.gf256Mul_64(l8, l7);
        long l10 = GF2Field.gf256Squ_64(l9);
        return GF2Field.gf256Mul_64(l3, l10);
    }

    static {
        long l2;
        int n2;
        gfMulTable = new byte[256][256];
        gfInvTable = new byte[256];
        long l3 = 0x101010101010101L;
        for (n2 = 1; n2 <= 255; ++n2) {
            l2 = 506097522914230528L;
            for (int i2 = 0; i2 < 256; i2 += 8) {
                long l4 = GF2Field.gf256Mul_64(l3, l2);
                Pack.longToLittleEndian(l4, gfMulTable[n2], i2);
                l2 += 0x808080808080808L;
            }
            l3 += 0x101010101010101L;
        }
        l3 = 506097522914230528L;
        for (n2 = 0; n2 < 256; n2 += 8) {
            l2 = GF2Field.gf256Inv_64(l3);
            Pack.longToLittleEndian(l2, gfInvTable, n2);
            l3 += 0x808080808080808L;
        }
    }
}

