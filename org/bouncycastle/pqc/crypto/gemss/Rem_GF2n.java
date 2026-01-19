/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.gemss;

abstract class Rem_GF2n {
    protected long mask;
    protected int ki;
    protected int ki64;

    Rem_GF2n() {
    }

    public abstract void rem_gf2n(long[] var1, int var2, long[] var3);

    public abstract void rem_gf2n_xor(long[] var1, int var2, long[] var3);

    public static class REM192_SPECIALIZED_TRINOMIAL_GF2X
    extends Rem_GF2n {
        private final int k3;
        private final int k364;
        private final int ki_k3;

        REM192_SPECIALIZED_TRINOMIAL_GF2X(int n2, int n3, int n4, int n5, long l2) {
            this.k3 = n2;
            this.ki = n3;
            this.ki64 = n4;
            this.k364 = n5;
            this.mask = l2;
            this.ki_k3 = n3 - n2;
        }

        @Override
        public void rem_gf2n(long[] lArray, int n2, long[] lArray2) {
            long l2 = lArray2[2] >>> this.ki ^ lArray2[3] << this.ki64;
            long l3 = lArray2[3] >>> this.ki ^ lArray2[4] << this.ki64;
            long l4 = lArray2[4] >>> this.ki ^ lArray2[5] << this.ki64;
            lArray[n2 + 1] = lArray2[1] ^ l3 ^ l2 >>> this.k364 ^ l3 << this.k3;
            lArray[n2 + 2] = (lArray2[2] ^ l4 ^ l3 >>> this.k364 ^ l4 << this.k3) & this.mask;
            lArray[n2] = lArray2[0] ^ (l2 ^= l4 >>> this.ki_k3) ^ l2 << this.k3;
        }

        @Override
        public void rem_gf2n_xor(long[] lArray, int n2, long[] lArray2) {
            long l2 = lArray2[2] >>> this.ki ^ lArray2[3] << this.ki64;
            long l3 = lArray2[3] >>> this.ki ^ lArray2[4] << this.ki64;
            long l4 = lArray2[4] >>> this.ki ^ lArray2[5] << this.ki64;
            int n3 = n2 + 1;
            lArray[n3] = lArray[n3] ^ (lArray2[1] ^ l3 ^ l2 >>> this.k364 ^ l3 << this.k3);
            int n4 = n2 + 2;
            lArray[n4] = lArray[n4] ^ (lArray2[2] ^ l4 ^ l3 >>> this.k364 ^ l4 << this.k3) & this.mask;
            int n5 = n2;
            lArray[n5] = lArray[n5] ^ (lArray2[0] ^ (l2 ^= l4 >>> this.ki_k3) ^ l2 << this.k3);
        }
    }

    public static class REM288_SPECIALIZED_TRINOMIAL_GF2X
    extends Rem_GF2n {
        private final int k3;
        private final int k364;
        private final int k364ki;
        private final int k3_ki;

        public REM288_SPECIALIZED_TRINOMIAL_GF2X(int n2, int n3, int n4, int n5, long l2) {
            this.k3 = n2;
            this.ki = n3;
            this.ki64 = n4;
            this.k364 = n5;
            this.mask = l2;
            this.k364ki = n5 + n3;
            this.k3_ki = n2 - n3;
        }

        @Override
        public void rem_gf2n(long[] lArray, int n2, long[] lArray2) {
            long l2 = lArray2[5] >>> this.ki ^ lArray2[6] << this.ki64;
            long l3 = lArray2[6] >>> this.ki ^ lArray2[7] << this.ki64;
            lArray[n2 + 2] = lArray2[2] ^ l3 ^ l2 >>> this.k364 ^ l3 << this.k3;
            long l4 = lArray2[7] >>> this.ki ^ lArray2[8] << this.ki64;
            lArray[n2 + 3] = lArray2[3] ^ l4 ^ l3 >>> this.k364 ^ l4 << this.k3;
            long l5 = lArray2[8] >>> this.ki;
            l3 = lArray2[4] >>> this.ki ^ lArray2[5] << this.ki64 ^ l4 >>> this.k364ki ^ l5 << this.k3_ki;
            lArray[n2 + 4] = (lArray2[4] ^ l5 ^ l4 >>> this.k364 ^ l5 << this.k3) & this.mask;
            lArray[n2] = lArray2[0] ^ l3 ^ l3 << this.k3;
            lArray[n2 + 1] = lArray2[1] ^ l2 ^ l2 << this.k3 ^ l3 >>> this.k364;
        }

        @Override
        public void rem_gf2n_xor(long[] lArray, int n2, long[] lArray2) {
            long l2 = lArray2[5] >>> this.ki ^ lArray2[6] << this.ki64;
            long l3 = lArray2[6] >>> this.ki ^ lArray2[7] << this.ki64;
            int n3 = n2 + 2;
            lArray[n3] = lArray[n3] ^ (lArray2[2] ^ l3 ^ l2 >>> this.k364 ^ l3 << this.k3);
            long l4 = lArray2[7] >>> this.ki ^ lArray2[8] << this.ki64;
            int n4 = n2 + 3;
            lArray[n4] = lArray[n4] ^ (lArray2[3] ^ l4 ^ l3 >>> this.k364 ^ l4 << this.k3);
            l3 = lArray2[8] >>> this.ki;
            int n5 = n2 + 4;
            lArray[n5] = lArray[n5] ^ (lArray2[4] ^ l3 ^ l4 >>> this.k364 ^ l3 << this.k3) & this.mask;
            l4 = lArray2[4] >>> this.ki ^ lArray2[5] << this.ki64 ^ l4 >>> this.k364ki ^ l3 << this.k3_ki;
            int n6 = n2;
            lArray[n6] = lArray[n6] ^ (lArray2[0] ^ l4 ^ l4 << this.k3);
            int n7 = n2 + 1;
            lArray[n7] = lArray[n7] ^ (lArray2[1] ^ l2 ^ l2 << this.k3 ^ l4 >>> this.k364);
        }
    }

    public static class REM384_SPECIALIZED358_TRINOMIAL_GF2X
    extends Rem_GF2n {
        private final int k3;
        private final int k364;
        private final int k364ki;
        private final int k3_ki;

        public REM384_SPECIALIZED358_TRINOMIAL_GF2X(int n2, int n3, int n4, int n5, long l2) {
            this.k3 = n2;
            this.ki = n3;
            this.ki64 = n4;
            this.k364 = n5;
            this.mask = l2;
            this.k364ki = n5 + n3;
            this.k3_ki = n2 - n3;
        }

        @Override
        public void rem_gf2n(long[] lArray, int n2, long[] lArray2) {
            long l2 = lArray2[6] >>> this.ki ^ lArray2[7] << this.ki64;
            long l3 = lArray2[7] >>> this.ki ^ lArray2[8] << this.ki64;
            lArray[n2 + 2] = lArray2[2] ^ l3 ^ l2 >>> this.k364 ^ l3 << this.k3;
            long l4 = lArray2[8] >>> this.ki ^ lArray2[9] << this.ki64;
            lArray[n2 + 3] = lArray2[3] ^ l4 ^ l3 >>> this.k364 ^ l4 << this.k3;
            l3 = lArray2[9] >>> this.ki ^ lArray2[10] << this.ki64;
            lArray[n2 + 4] = lArray2[4] ^ l3 ^ l4 >>> this.k364 ^ l3 << this.k3;
            l4 = lArray2[10] >>> this.ki ^ lArray2[11] << this.ki64;
            long l5 = lArray2[5] >>> this.ki ^ lArray2[6] << this.ki64 ^ l3 >>> this.k364ki ^ l4 << this.k3_ki;
            lArray[n2 + 5] = (lArray2[5] ^ l4 ^ l3 >>> this.k364) & this.mask;
            lArray[n2] = lArray2[0] ^ l5 ^ l5 << this.k3;
            lArray[n2 + 1] = lArray2[1] ^ l2 ^ l5 >>> this.k364 ^ l2 << this.k3;
        }

        @Override
        public void rem_gf2n_xor(long[] lArray, int n2, long[] lArray2) {
            long l2 = lArray2[6] >>> this.ki ^ lArray2[7] << this.ki64;
            long l3 = lArray2[7] >>> this.ki ^ lArray2[8] << this.ki64;
            int n3 = n2 + 2;
            lArray[n3] = lArray[n3] ^ (lArray2[2] ^ l3 ^ l2 >>> this.k364 ^ l3 << this.k3);
            long l4 = lArray2[8] >>> this.ki ^ lArray2[9] << this.ki64;
            int n4 = n2 + 3;
            lArray[n4] = lArray[n4] ^ (lArray2[3] ^ l4 ^ l3 >>> this.k364 ^ l4 << this.k3);
            l3 = lArray2[9] >>> this.ki ^ lArray2[10] << this.ki64;
            int n5 = n2 + 4;
            lArray[n5] = lArray[n5] ^ (lArray2[4] ^ l3 ^ l4 >>> this.k364 ^ l3 << this.k3);
            l4 = lArray2[10] >>> this.ki ^ lArray2[11] << this.ki64;
            int n6 = n2 + 5;
            lArray[n6] = lArray[n6] ^ (lArray2[5] ^ l4 ^ l3 >>> this.k364) & this.mask;
            l3 = lArray2[5] >>> this.ki ^ lArray2[6] << this.ki64 ^ l3 >>> this.k364ki ^ l4 << this.k3_ki;
            int n7 = n2;
            lArray[n7] = lArray[n7] ^ (lArray2[0] ^ l3 ^ l3 << this.k3);
            int n8 = n2 + 1;
            lArray[n8] = lArray[n8] ^ (lArray2[1] ^ l2 ^ l3 >>> this.k364 ^ l2 << this.k3);
        }
    }

    public static class REM384_SPECIALIZED_TRINOMIAL_GF2X
    extends Rem_GF2n {
        private final int k3;
        private final int k364;
        private final int k364ki;
        private final int k3_ki;

        public REM384_SPECIALIZED_TRINOMIAL_GF2X(int n2, int n3, int n4, int n5, long l2) {
            this.k3 = n2;
            this.ki = n3;
            this.ki64 = n4;
            this.k364 = n5;
            this.mask = l2;
            this.k364ki = n5 + n3;
            this.k3_ki = n2 - n3;
        }

        @Override
        public void rem_gf2n(long[] lArray, int n2, long[] lArray2) {
            long l2 = lArray2[7] >>> this.ki ^ lArray2[8] << this.ki64;
            long l3 = lArray2[8] >>> this.ki ^ lArray2[9] << this.ki64;
            long l4 = lArray2[9] >>> this.ki ^ lArray2[10] << this.ki64;
            long l5 = lArray2[10] >>> this.ki ^ lArray2[11] << this.ki64;
            long l6 = lArray2[5] >>> this.ki ^ lArray2[6] << this.ki64 ^ l3 >>> this.k364ki ^ l4 << this.k3_ki;
            long l7 = lArray2[6] >>> this.ki ^ lArray2[7] << this.ki64 ^ l4 >>> this.k364ki ^ l5 << this.k3_ki;
            lArray[n2] = lArray2[0] ^ l6;
            lArray[n2 + 1] = lArray2[1] ^ l7 ^ l6 << this.k3;
            lArray[n2 + 2] = lArray2[2] ^ l2 ^ l6 >>> this.k364 ^ l7 << this.k3;
            lArray[n2 + 3] = lArray2[3] ^ l3 ^ l7 >>> this.k364 ^ l2 << this.k3;
            lArray[n2 + 4] = lArray2[4] ^ l4 ^ l2 >>> this.k364 ^ l3 << this.k3;
            lArray[n2 + 5] = (lArray2[5] ^ l5 ^ l3 >>> this.k364) & this.mask;
        }

        @Override
        public void rem_gf2n_xor(long[] lArray, int n2, long[] lArray2) {
            long l2 = lArray2[7] >>> this.ki ^ lArray2[8] << this.ki64;
            long l3 = lArray2[8] >>> this.ki ^ lArray2[9] << this.ki64;
            long l4 = lArray2[9] >>> this.ki ^ lArray2[10] << this.ki64;
            long l5 = lArray2[10] >>> this.ki ^ lArray2[11] << this.ki64;
            long l6 = lArray2[5] >>> this.ki ^ lArray2[6] << this.ki64 ^ l3 >>> this.k364ki ^ l4 << this.k3_ki;
            long l7 = lArray2[6] >>> this.ki ^ lArray2[7] << this.ki64 ^ l4 >>> this.k364ki ^ l5 << this.k3_ki;
            int n3 = n2;
            lArray[n3] = lArray[n3] ^ (lArray2[0] ^ l6);
            int n4 = n2 + 1;
            lArray[n4] = lArray[n4] ^ (lArray2[1] ^ l7 ^ l6 << this.k3);
            int n5 = n2 + 2;
            lArray[n5] = lArray[n5] ^ (lArray2[2] ^ l2 ^ l6 >>> this.k364 ^ l7 << this.k3);
            int n6 = n2 + 3;
            lArray[n6] = lArray[n6] ^ (lArray2[3] ^ l3 ^ l7 >>> this.k364 ^ l2 << this.k3);
            int n7 = n2 + 4;
            lArray[n7] = lArray[n7] ^ (lArray2[4] ^ l4 ^ l2 >>> this.k364 ^ l3 << this.k3);
            int n8 = n2 + 5;
            lArray[n8] = lArray[n8] ^ (lArray2[5] ^ l5 ^ l3 >>> this.k364) & this.mask;
        }
    }

    public static class REM384_TRINOMIAL_GF2X
    extends Rem_GF2n {
        private final int k3;
        private final int k364;
        private final int ki_k3;

        public REM384_TRINOMIAL_GF2X(int n2, int n3, int n4, int n5, long l2) {
            this.k3 = n2;
            this.ki = n3;
            this.ki64 = n4;
            this.k364 = n5;
            this.mask = l2;
            this.ki_k3 = n3 - n2;
        }

        @Override
        public void rem_gf2n(long[] lArray, int n2, long[] lArray2) {
            long l2 = lArray2[5] >>> this.ki ^ lArray2[6] << this.ki64;
            long l3 = lArray2[6] >>> this.ki ^ lArray2[7] << this.ki64;
            long l4 = lArray2[7] >>> this.ki ^ lArray2[8] << this.ki64;
            long l5 = lArray2[8] >>> this.ki ^ lArray2[9] << this.ki64;
            long l6 = lArray2[9] >>> this.ki ^ lArray2[10] << this.ki64;
            long l7 = lArray2[10] >>> this.ki ^ lArray2[11] << this.ki64;
            long l8 = l2 ^ l7 >>> this.ki_k3;
            lArray[n2] = lArray2[0] ^ l8 ^ l8 << this.k3;
            lArray[n2 + 1] = lArray2[1] ^ l3 ^ l2 >>> this.k364 ^ l3 << this.k3;
            lArray[n2 + 2] = lArray2[2] ^ l4 ^ l3 >>> this.k364 ^ l4 << this.k3;
            lArray[n2 + 3] = lArray2[3] ^ l5 ^ l4 >>> this.k364 ^ l5 << this.k3;
            lArray[n2 + 4] = lArray2[4] ^ l6 ^ l5 >>> this.k364 ^ l6 << this.k3;
            lArray[n2 + 5] = (lArray2[5] ^ l7 ^ l6 >>> this.k364 ^ l7 << this.k3) & this.mask;
        }

        @Override
        public void rem_gf2n_xor(long[] lArray, int n2, long[] lArray2) {
            long l2 = lArray2[5] >>> this.ki ^ lArray2[6] << this.ki64;
            long l3 = lArray2[6] >>> this.ki ^ lArray2[7] << this.ki64;
            long l4 = lArray2[7] >>> this.ki ^ lArray2[8] << this.ki64;
            long l5 = lArray2[8] >>> this.ki ^ lArray2[9] << this.ki64;
            long l6 = lArray2[9] >>> this.ki ^ lArray2[10] << this.ki64;
            long l7 = lArray2[10] >>> this.ki ^ lArray2[11] << this.ki64;
            long l8 = l2 ^ l7 >>> this.ki_k3;
            int n3 = n2;
            lArray[n3] = lArray[n3] ^ (lArray2[0] ^ l8 ^ l8 << this.k3);
            int n4 = n2 + 1;
            lArray[n4] = lArray[n4] ^ (lArray2[1] ^ l3 ^ l2 >>> this.k364 ^ l3 << this.k3);
            int n5 = n2 + 2;
            lArray[n5] = lArray[n5] ^ (lArray2[2] ^ l4 ^ l3 >>> this.k364 ^ l4 << this.k3);
            int n6 = n2 + 3;
            lArray[n6] = lArray[n6] ^ (lArray2[3] ^ l5 ^ l4 >>> this.k364 ^ l5 << this.k3);
            int n7 = n2 + 4;
            lArray[n7] = lArray[n7] ^ (lArray2[4] ^ l6 ^ l5 >>> this.k364 ^ l6 << this.k3);
            int n8 = n2 + 5;
            lArray[n8] = lArray[n8] ^ (lArray2[5] ^ l7 ^ l6 >>> this.k364 ^ l7 << this.k3) & this.mask;
        }
    }

    public static class REM402_SPECIALIZED_TRINOMIAL_GF2X
    extends Rem_GF2n {
        private final int k3;
        private final int k364;

        public REM402_SPECIALIZED_TRINOMIAL_GF2X(int n2, int n3, int n4, int n5, long l2) {
            this.k3 = n2;
            this.ki = n3;
            this.ki64 = n4;
            this.k364 = n5;
            this.mask = l2;
        }

        @Override
        public void rem_gf2n(long[] lArray, int n2, long[] lArray2) {
            long l2 = lArray2[9] >>> this.ki ^ lArray2[10] << this.ki64;
            long l3 = lArray2[10] >>> this.ki ^ lArray2[11] << this.ki64;
            long l4 = lArray2[11] >>> this.ki ^ lArray2[12] << this.ki64;
            long l5 = lArray2[12] >>> this.ki;
            long l6 = l2 >>> 39 ^ l3 << 25 ^ lArray2[6] >>> this.ki ^ lArray2[7] << this.ki64;
            long l7 = l3 >>> 39 ^ l4 << 25 ^ lArray2[7] >>> this.ki ^ lArray2[8] << this.ki64;
            long l8 = l4 >>> 39 ^ l5 << 25 ^ lArray2[8] >>> this.ki ^ lArray2[9] << this.ki64;
            lArray[n2] = lArray2[0] ^ l6;
            lArray[n2 + 1] = lArray2[1] ^ l7;
            lArray[n2 + 2] = lArray2[2] ^ l8 ^ l6 << this.k3;
            lArray[n2 + 3] = lArray2[3] ^ l2 ^ l6 >>> this.k364 ^ l7 << this.k3;
            lArray[n2 + 4] = lArray2[4] ^ l3 ^ l7 >>> this.k364 ^ l8 << this.k3;
            lArray[n2 + 5] = lArray2[5] ^ l4 ^ l8 >>> this.k364 ^ l2 << this.k3;
            lArray[n2 + 6] = (lArray2[6] ^ l5 ^ l2 >>> this.k364) & this.mask;
        }

        @Override
        public void rem_gf2n_xor(long[] lArray, int n2, long[] lArray2) {
            long l2 = lArray2[9] >>> this.ki ^ lArray2[10] << this.ki64;
            long l3 = lArray2[10] >>> this.ki ^ lArray2[11] << this.ki64;
            long l4 = lArray2[11] >>> this.ki ^ lArray2[12] << this.ki64;
            long l5 = lArray2[12] >>> this.ki;
            long l6 = l2 >>> 39 ^ l3 << 25 ^ lArray2[6] >>> this.ki ^ lArray2[7] << this.ki64;
            long l7 = l3 >>> 39 ^ l4 << 25 ^ lArray2[7] >>> this.ki ^ lArray2[8] << this.ki64;
            long l8 = l4 >>> 39 ^ l5 << 25 ^ lArray2[8] >>> this.ki ^ lArray2[9] << this.ki64;
            int n3 = n2;
            lArray[n3] = lArray[n3] ^ (lArray2[0] ^ l6);
            int n4 = n2 + 1;
            lArray[n4] = lArray[n4] ^ (lArray2[1] ^ l7);
            int n5 = n2 + 2;
            lArray[n5] = lArray[n5] ^ (lArray2[2] ^ l8 ^ l6 << this.k3);
            int n6 = n2 + 3;
            lArray[n6] = lArray[n6] ^ (lArray2[3] ^ l2 ^ l6 >>> this.k364 ^ l7 << this.k3);
            int n7 = n2 + 4;
            lArray[n7] = lArray[n7] ^ (lArray2[4] ^ l3 ^ l7 >>> this.k364 ^ l8 << this.k3);
            int n8 = n2 + 5;
            lArray[n8] = lArray[n8] ^ (lArray2[5] ^ l4 ^ l8 >>> this.k364 ^ l2 << this.k3);
            int n9 = n2 + 6;
            lArray[n9] = lArray[n9] ^ (lArray2[6] ^ l5 ^ l2 >>> this.k364) & this.mask;
        }
    }

    public static class REM544_PENTANOMIAL_GF2X
    extends Rem_GF2n {
        private final int k1;
        private final int k2;
        private final int k3;
        private final int k164;
        private final int k264;
        private final int k364;
        private final int ki_k3;
        private final int ki_k2;
        private final int ki_k1;

        public REM544_PENTANOMIAL_GF2X(int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, long l2) {
            this.k1 = n2;
            this.k2 = n3;
            this.k3 = n4;
            this.ki = n5;
            this.ki64 = n6;
            this.k164 = n7;
            this.k264 = n8;
            this.k364 = n9;
            this.mask = l2;
            this.ki_k3 = n5 - n4;
            this.ki_k2 = n5 - n3;
            this.ki_k1 = n5 - n2;
        }

        @Override
        public void rem_gf2n(long[] lArray, int n2, long[] lArray2) {
            long l2 = lArray2[16] >>> this.ki;
            long l3 = lArray2[8] >>> this.ki ^ lArray2[9] << this.ki64;
            long l4 = lArray2[9] >>> this.ki ^ lArray2[10] << this.ki64;
            lArray[n2 + 1] = lArray2[1] ^ l4 ^ l3 >>> this.k164 ^ l4 << this.k1 ^ l3 >>> this.k264 ^ l4 << this.k2 ^ l3 >>> this.k364 ^ l4 << this.k3;
            lArray[n2] = lArray2[0] ^ (l3 ^= l2 >>> this.ki_k3 ^ l2 >>> this.ki_k2 ^ l2 >>> this.ki_k1) ^ l3 << this.k1 ^ l3 << this.k2 ^ l3 << this.k3;
            l3 = lArray2[10] >>> this.ki ^ lArray2[11] << this.ki64;
            lArray[n2 + 2] = lArray2[2] ^ l3 ^ l4 >>> this.k164 ^ l3 << this.k1 ^ l4 >>> this.k264 ^ l3 << this.k2 ^ l4 >>> this.k364 ^ l3 << this.k3;
            l4 = lArray2[11] >>> this.ki ^ lArray2[12] << this.ki64;
            lArray[n2 + 3] = lArray2[3] ^ l4 ^ l3 >>> this.k164 ^ l4 << this.k1 ^ l3 >>> this.k264 ^ l4 << this.k2 ^ l3 >>> this.k364 ^ l4 << this.k3;
            l3 = lArray2[12] >>> this.ki ^ lArray2[13] << this.ki64;
            lArray[n2 + 4] = lArray2[4] ^ l3 ^ l4 >>> this.k164 ^ l3 << this.k1 ^ l4 >>> this.k264 ^ l3 << this.k2 ^ l4 >>> this.k364 ^ l3 << this.k3;
            l4 = lArray2[13] >>> this.ki ^ lArray2[14] << this.ki64;
            lArray[n2 + 5] = lArray2[5] ^ l4 ^ l3 >>> this.k164 ^ l4 << this.k1 ^ l3 >>> this.k264 ^ l4 << this.k2 ^ l3 >>> this.k364 ^ l4 << this.k3;
            l3 = lArray2[14] >>> this.ki ^ lArray2[15] << this.ki64;
            lArray[n2 + 6] = lArray2[6] ^ l3 ^ l4 >>> this.k164 ^ l3 << this.k1 ^ l4 >>> this.k264 ^ l3 << this.k2 ^ l4 >>> this.k364 ^ l3 << this.k3;
            l4 = lArray2[15] >>> this.ki ^ lArray2[16] << this.ki64;
            lArray[n2 + 7] = lArray2[7] ^ l4 ^ l3 >>> this.k164 ^ l4 << this.k1 ^ l3 >>> this.k264 ^ l4 << this.k2 ^ l3 >>> this.k364 ^ l4 << this.k3;
            lArray[n2 + 8] = (lArray2[8] ^ l2 ^ l4 >>> this.k164 ^ l2 << this.k1 ^ l4 >>> this.k264 ^ l2 << this.k2 ^ l4 >>> this.k364 ^ l2 << this.k3) & this.mask;
        }

        @Override
        public void rem_gf2n_xor(long[] lArray, int n2, long[] lArray2) {
            long l2 = lArray2[16] >>> this.ki;
            long l3 = lArray2[8] >>> this.ki ^ lArray2[9] << this.ki64;
            long l4 = lArray2[9] >>> this.ki ^ lArray2[10] << this.ki64;
            int n3 = n2 + 1;
            lArray[n3] = lArray[n3] ^ (lArray2[1] ^ l4 ^ l3 >>> this.k164 ^ l4 << this.k1 ^ l3 >>> this.k264 ^ l4 << this.k2 ^ l3 >>> this.k364 ^ l4 << this.k3);
            int n4 = n2;
            lArray[n4] = lArray[n4] ^ (lArray2[0] ^ (l3 ^= l2 >>> this.ki_k3 ^ l2 >>> this.ki_k2 ^ l2 >>> this.ki_k1) ^ l3 << this.k1 ^ l3 << this.k2 ^ l3 << this.k3);
            l3 = lArray2[10] >>> this.ki ^ lArray2[11] << this.ki64;
            int n5 = n2 + 2;
            lArray[n5] = lArray[n5] ^ (lArray2[2] ^ l3 ^ l4 >>> this.k164 ^ l3 << this.k1 ^ l4 >>> this.k264 ^ l3 << this.k2 ^ l4 >>> this.k364 ^ l3 << this.k3);
            l4 = lArray2[11] >>> this.ki ^ lArray2[12] << this.ki64;
            int n6 = n2 + 3;
            lArray[n6] = lArray[n6] ^ (lArray2[3] ^ l4 ^ l3 >>> this.k164 ^ l4 << this.k1 ^ l3 >>> this.k264 ^ l4 << this.k2 ^ l3 >>> this.k364 ^ l4 << this.k3);
            l3 = lArray2[12] >>> this.ki ^ lArray2[13] << this.ki64;
            int n7 = n2 + 4;
            lArray[n7] = lArray[n7] ^ (lArray2[4] ^ l3 ^ l4 >>> this.k164 ^ l3 << this.k1 ^ l4 >>> this.k264 ^ l3 << this.k2 ^ l4 >>> this.k364 ^ l3 << this.k3);
            l4 = lArray2[13] >>> this.ki ^ lArray2[14] << this.ki64;
            int n8 = n2 + 5;
            lArray[n8] = lArray[n8] ^ (lArray2[5] ^ l4 ^ l3 >>> this.k164 ^ l4 << this.k1 ^ l3 >>> this.k264 ^ l4 << this.k2 ^ l3 >>> this.k364 ^ l4 << this.k3);
            l3 = lArray2[14] >>> this.ki ^ lArray2[15] << this.ki64;
            int n9 = n2 + 6;
            lArray[n9] = lArray[n9] ^ (lArray2[6] ^ l3 ^ l4 >>> this.k164 ^ l3 << this.k1 ^ l4 >>> this.k264 ^ l3 << this.k2 ^ l4 >>> this.k364 ^ l3 << this.k3);
            l4 = lArray2[15] >>> this.ki ^ lArray2[16] << this.ki64;
            int n10 = n2 + 7;
            lArray[n10] = lArray[n10] ^ (lArray2[7] ^ l4 ^ l3 >>> this.k164 ^ l4 << this.k1 ^ l3 >>> this.k264 ^ l4 << this.k2 ^ l3 >>> this.k364 ^ l4 << this.k3);
            int n11 = n2 + 8;
            lArray[n11] = lArray[n11] ^ (lArray2[8] ^ l2 ^ l4 >>> this.k164 ^ l2 << this.k1 ^ l4 >>> this.k264 ^ l2 << this.k2 ^ l4 >>> this.k364 ^ l2 << this.k3) & this.mask;
        }
    }

    public static class REM544_PENTANOMIAL_K3_IS_128_GF2X
    extends Rem_GF2n {
        private final int k1;
        private final int k2;
        private final int k164;
        private final int k264;

        public REM544_PENTANOMIAL_K3_IS_128_GF2X(int n2, int n3, int n4, int n5, int n6, int n7, long l2) {
            this.k1 = n2;
            this.k2 = n3;
            this.ki = n4;
            this.ki64 = n5;
            this.k164 = n6;
            this.k264 = n7;
            this.mask = l2;
        }

        @Override
        public void rem_gf2n(long[] lArray, int n2, long[] lArray2) {
            long l2 = lArray2[10] >>> this.ki ^ lArray2[11] << this.ki64;
            long l3 = lArray2[11] >>> this.ki ^ lArray2[12] << this.ki64;
            long l4 = lArray2[12] >>> this.ki ^ lArray2[13] << this.ki64;
            lArray[n2 + 4] = lArray2[4] ^ l4 ^ l2 ^ l3 >>> this.k164 ^ l4 << this.k1 ^ l3 >>> this.k264 ^ l4 << this.k2;
            long l5 = lArray2[13] >>> this.ki ^ lArray2[14] << this.ki64;
            lArray[n2 + 5] = lArray2[5] ^ l5 ^ l3 ^ l4 >>> this.k164 ^ l5 << this.k1 ^ l4 >>> this.k264 ^ l5 << this.k2;
            long l6 = lArray2[14] >>> this.ki ^ lArray2[15] << this.ki64;
            lArray[n2 + 6] = lArray2[6] ^ l6 ^ l4 ^ l5 >>> this.k164 ^ l6 << this.k1 ^ l5 >>> this.k264 ^ l6 << this.k2;
            l4 = lArray2[15] >>> this.ki ^ lArray2[16] << this.ki64;
            lArray[n2 + 7] = lArray2[7] ^ l4 ^ l5 ^ l6 >>> this.k164 ^ l4 << this.k1 ^ l6 >>> this.k264 ^ l4 << this.k2;
            l5 = lArray2[16] >>> this.ki;
            lArray[n2 + 8] = (lArray2[8] ^ l5 ^ l6 ^ l4 >>> this.k164 ^ l5 << this.k1 ^ l4 >>> this.k264 ^ l5 << this.k2) & this.mask;
            l6 = (lArray2[8] ^ l6) >>> this.ki ^ (lArray2[9] ^ l4) << this.ki64 ^ lArray2[16] >>> this.k264;
            l4 = (lArray2[9] ^ l4) >>> this.ki ^ (lArray2[10] ^ l5) << this.ki64;
            lArray[n2] = lArray2[0] ^ l6 ^ l6 << this.k1 ^ l6 << this.k2;
            lArray[n2 + 1] = lArray2[1] ^ l4 ^ l6 >>> this.k164 ^ l4 << this.k1 ^ l6 >>> this.k264 ^ l4 << this.k2;
            lArray[n2 + 2] = lArray2[2] ^ l2 ^ l6 ^ l4 >>> this.k164 ^ l2 << this.k1 ^ l4 >>> this.k264 ^ l2 << this.k2;
            lArray[n2 + 3] = lArray2[3] ^ l3 ^ l4 ^ l2 >>> this.k164 ^ l3 << this.k1 ^ l2 >>> this.k264 ^ l3 << this.k2;
        }

        @Override
        public void rem_gf2n_xor(long[] lArray, int n2, long[] lArray2) {
            long l2 = lArray2[10] >>> this.ki ^ lArray2[11] << this.ki64;
            long l3 = lArray2[11] >>> this.ki ^ lArray2[12] << this.ki64;
            long l4 = lArray2[12] >>> this.ki ^ lArray2[13] << this.ki64;
            int n3 = n2 + 4;
            lArray[n3] = lArray[n3] ^ (lArray2[4] ^ l4 ^ l2 ^ l3 >>> this.k164 ^ l4 << this.k1 ^ l3 >>> this.k264 ^ l4 << this.k2);
            long l5 = lArray2[13] >>> this.ki ^ lArray2[14] << this.ki64;
            int n4 = n2 + 5;
            lArray[n4] = lArray[n4] ^ (lArray2[5] ^ l5 ^ l3 ^ l4 >>> this.k164 ^ l5 << this.k1 ^ l4 >>> this.k264 ^ l5 << this.k2);
            long l6 = lArray2[14] >>> this.ki ^ lArray2[15] << this.ki64;
            int n5 = n2 + 6;
            lArray[n5] = lArray[n5] ^ (lArray2[6] ^ l6 ^ l4 ^ l5 >>> this.k164 ^ l6 << this.k1 ^ l5 >>> this.k264 ^ l6 << this.k2);
            l4 = lArray2[15] >>> this.ki ^ lArray2[16] << this.ki64;
            int n6 = n2 + 7;
            lArray[n6] = lArray[n6] ^ (lArray2[7] ^ l4 ^ l5 ^ l6 >>> this.k164 ^ l4 << this.k1 ^ l6 >>> this.k264 ^ l4 << this.k2);
            l5 = lArray2[16] >>> this.ki;
            int n7 = n2 + 8;
            lArray[n7] = lArray[n7] ^ (lArray2[8] ^ l5 ^ l6 ^ l4 >>> this.k164 ^ l5 << this.k1 ^ l4 >>> this.k264 ^ l5 << this.k2) & this.mask;
            l6 = (lArray2[8] ^ l6) >>> this.ki ^ (lArray2[9] ^ l4) << this.ki64 ^ lArray2[16] >>> this.k264;
            l4 = (lArray2[9] ^ l4) >>> this.ki ^ (lArray2[10] ^ l5) << this.ki64;
            int n8 = n2;
            lArray[n8] = lArray[n8] ^ (lArray2[0] ^ l6 ^ l6 << this.k1 ^ l6 << this.k2);
            int n9 = n2 + 1;
            lArray[n9] = lArray[n9] ^ (lArray2[1] ^ l4 ^ l6 >>> this.k164 ^ l4 << this.k1 ^ l6 >>> this.k264 ^ l4 << this.k2);
            int n10 = n2 + 2;
            lArray[n10] = lArray[n10] ^ (lArray2[2] ^ l2 ^ l6 ^ l4 >>> this.k164 ^ l2 << this.k1 ^ l4 >>> this.k264 ^ l2 << this.k2);
            int n11 = n2 + 3;
            lArray[n11] = lArray[n11] ^ (lArray2[3] ^ l3 ^ l4 ^ l2 >>> this.k164 ^ l3 << this.k1 ^ l2 >>> this.k264 ^ l3 << this.k2);
        }
    }
}

