/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec.rfc7748;

import java.security.SecureRandom;
import org.bouncycastle.math.ec.rfc7748.X25519Field;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.bouncycastle.util.Arrays;

public abstract class X25519 {
    public static final int POINT_SIZE = 32;
    public static final int SCALAR_SIZE = 32;
    private static final int C_A = 486662;
    private static final int C_A24 = 121666;

    public static boolean calculateAgreement(byte[] byArray, int n2, byte[] byArray2, int n3, byte[] byArray3, int n4) {
        X25519.scalarMult(byArray, n2, byArray2, n3, byArray3, n4);
        return !Arrays.areAllZeroes(byArray3, n4, 32);
    }

    private static int decode32(byte[] byArray, int n2) {
        int n3 = byArray[n2] & 0xFF;
        n3 |= (byArray[++n2] & 0xFF) << 8;
        n3 |= (byArray[++n2] & 0xFF) << 16;
        return n3 |= byArray[++n2] << 24;
    }

    private static void decodeScalar(byte[] byArray, int n2, int[] nArray) {
        for (int i2 = 0; i2 < 8; ++i2) {
            nArray[i2] = X25519.decode32(byArray, n2 + i2 * 4);
        }
        nArray[0] = nArray[0] & 0xFFFFFFF8;
        nArray[7] = nArray[7] & Integer.MAX_VALUE;
        nArray[7] = nArray[7] | 0x40000000;
    }

    public static void generatePrivateKey(SecureRandom secureRandom, byte[] byArray) {
        if (byArray.length != 32) {
            throw new IllegalArgumentException("k");
        }
        secureRandom.nextBytes(byArray);
        byArray[0] = (byte)(byArray[0] & 0xF8);
        byArray[31] = (byte)(byArray[31] & 0x7F);
        byArray[31] = (byte)(byArray[31] | 0x40);
    }

    public static void generatePublicKey(byte[] byArray, int n2, byte[] byArray2, int n3) {
        X25519.scalarMultBase(byArray, n2, byArray2, n3);
    }

    private static void pointDouble(int[] nArray, int[] nArray2) {
        int[] nArray3 = F.create();
        int[] nArray4 = F.create();
        F.apm(nArray, nArray2, nArray3, nArray4);
        F.sqr(nArray3, nArray3);
        F.sqr(nArray4, nArray4);
        F.mul(nArray3, nArray4, nArray);
        F.sub(nArray3, nArray4, nArray3);
        F.mul(nArray3, 121666, nArray2);
        F.add(nArray2, nArray4, nArray2);
        F.mul(nArray2, nArray3, nArray2);
    }

    public static void precompute() {
        Ed25519.precompute();
    }

    public static void scalarMult(byte[] byArray, int n2, byte[] byArray2, int n3, byte[] byArray3, int n4) {
        int n5;
        int[] nArray = new int[8];
        X25519.decodeScalar(byArray, n2, nArray);
        int[] nArray2 = F.create();
        F.decode(byArray2, n3, nArray2);
        int[] nArray3 = F.create();
        F.copy(nArray2, 0, nArray3, 0);
        int[] nArray4 = F.create();
        nArray4[0] = 1;
        int[] nArray5 = F.create();
        nArray5[0] = 1;
        int[] nArray6 = F.create();
        int[] nArray7 = F.create();
        int[] nArray8 = F.create();
        int n6 = 254;
        int n7 = 1;
        do {
            F.apm(nArray5, nArray6, nArray7, nArray5);
            F.apm(nArray3, nArray4, nArray6, nArray3);
            F.mul(nArray7, nArray3, nArray7);
            F.mul(nArray5, nArray6, nArray5);
            F.sqr(nArray6, nArray6);
            F.sqr(nArray3, nArray3);
            F.sub(nArray6, nArray3, nArray8);
            F.mul(nArray8, 121666, nArray4);
            F.add(nArray4, nArray3, nArray4);
            F.mul(nArray4, nArray8, nArray4);
            F.mul(nArray3, nArray6, nArray3);
            F.apm(nArray7, nArray5, nArray5, nArray6);
            F.sqr(nArray5, nArray5);
            F.sqr(nArray6, nArray6);
            F.mul(nArray6, nArray2, nArray6);
            n5 = --n6 >>> 5;
            int n8 = n6 & 0x1F;
            int n9 = nArray[n5] >>> n8 & 1;
            F.cswap(n7 ^= n9, nArray3, nArray5);
            F.cswap(n7, nArray4, nArray6);
            n7 = n9;
        } while (n6 >= 3);
        for (n5 = 0; n5 < 3; ++n5) {
            X25519.pointDouble(nArray3, nArray4);
        }
        F.inv(nArray4, nArray4);
        F.mul(nArray3, nArray4, nArray3);
        F.normalize(nArray3);
        F.encode(nArray3, byArray3, n4);
    }

    public static void scalarMultBase(byte[] byArray, int n2, byte[] byArray2, int n3) {
        int[] nArray = F.create();
        int[] nArray2 = F.create();
        Ed25519.scalarMultBaseYZ(Friend.INSTANCE, byArray, n2, nArray, nArray2);
        F.apm(nArray2, nArray, nArray, nArray2);
        F.inv(nArray2, nArray2);
        F.mul(nArray, nArray2, nArray);
        F.normalize(nArray);
        F.encode(nArray, byArray2, n3);
    }

    private static class F
    extends X25519Field {
        private F() {
        }
    }

    public static class Friend {
        private static final Friend INSTANCE = new Friend();

        private Friend() {
        }
    }
}

