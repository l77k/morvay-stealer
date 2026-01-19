/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.sphincs;

import org.bouncycastle.pqc.crypto.sphincs.HashFunctions;
import org.bouncycastle.pqc.crypto.sphincs.Seed;

class Horst {
    static final int HORST_LOGT = 16;
    static final int HORST_T = 65536;
    static final int HORST_K = 32;
    static final int HORST_SKBYTES = 32;
    static final int HORST_SIGBYTES = 13312;
    static final int N_MASKS = 32;

    Horst() {
    }

    static void expand_seed(byte[] byArray, byte[] byArray2) {
        Seed.prg(byArray, 0, 0x200000L, byArray2, 0);
    }

    static int horst_sign(HashFunctions hashFunctions, byte[] byArray, int n2, byte[] byArray2, byte[] byArray3, byte[] byArray4, byte[] byArray5) {
        int n3;
        int n4;
        byte[] byArray6 = new byte[0x200000];
        int n5 = n2;
        byte[] byArray7 = new byte[4194272];
        Horst.expand_seed(byArray6, byArray3);
        for (n4 = 0; n4 < 65536; ++n4) {
            hashFunctions.hash_n_n(byArray7, (65535 + n4) * 32, byArray6, n4 * 32);
        }
        for (n4 = 0; n4 < 16; ++n4) {
            long l2 = (1 << 16 - n4) - 1;
            long l3 = (1 << 16 - n4 - 1) - 1;
            for (n3 = 0; n3 < 1 << 16 - n4 - 1; ++n3) {
                hashFunctions.hash_2n_n_mask(byArray7, (int)((l3 + (long)n3) * 32L), byArray7, (int)((l2 + (long)(2 * n3)) * 32L), byArray4, 2 * n4 * 32);
            }
        }
        for (n3 = 2016; n3 < 4064; ++n3) {
            byArray[n5++] = byArray7[n3];
        }
        for (n4 = 0; n4 < 32; ++n4) {
            int n6;
            int n7 = (byArray5[2 * n4] & 0xFF) + ((byArray5[2 * n4 + 1] & 0xFF) << 8);
            for (n6 = 0; n6 < 32; ++n6) {
                byArray[n5++] = byArray6[n7 * 32 + n6];
            }
            n7 += 65535;
            for (n3 = 0; n3 < 10; ++n3) {
                n7 = (n7 & 1) != 0 ? n7 + 1 : n7 - 1;
                for (n6 = 0; n6 < 32; ++n6) {
                    byArray[n5++] = byArray7[n7 * 32 + n6];
                }
                n7 = (n7 - 1) / 2;
            }
        }
        for (n4 = 0; n4 < 32; ++n4) {
            byArray2[n4] = byArray7[n4];
        }
        return 13312;
    }

    static int horst_verify(HashFunctions hashFunctions, byte[] byArray, byte[] byArray2, int n2, byte[] byArray3, byte[] byArray4) {
        int n3;
        byte[] byArray5 = new byte[1024];
        int n4 = n2 + 2048;
        for (int i2 = 0; i2 < 32; ++i2) {
            int n5;
            int n6 = (byArray4[2 * i2] & 0xFF) + ((byArray4[2 * i2 + 1] & 0xFF) << 8);
            if ((n6 & 1) == 0) {
                hashFunctions.hash_n_n(byArray5, 0, byArray2, n4);
                for (n5 = 0; n5 < 32; ++n5) {
                    byArray5[32 + n5] = byArray2[n4 + 32 + n5];
                }
            } else {
                hashFunctions.hash_n_n(byArray5, 32, byArray2, n4);
                for (n5 = 0; n5 < 32; ++n5) {
                    byArray5[n5] = byArray2[n4 + 32 + n5];
                }
            }
            n4 += 64;
            for (n3 = 1; n3 < 10; ++n3) {
                if (((n6 >>>= 1) & 1) == 0) {
                    hashFunctions.hash_2n_n_mask(byArray5, 0, byArray5, 0, byArray3, 2 * (n3 - 1) * 32);
                    for (n5 = 0; n5 < 32; ++n5) {
                        byArray5[32 + n5] = byArray2[n4 + n5];
                    }
                } else {
                    hashFunctions.hash_2n_n_mask(byArray5, 32, byArray5, 0, byArray3, 2 * (n3 - 1) * 32);
                    for (n5 = 0; n5 < 32; ++n5) {
                        byArray5[n5] = byArray2[n4 + n5];
                    }
                }
                n4 += 32;
            }
            n6 >>>= 1;
            hashFunctions.hash_2n_n_mask(byArray5, 0, byArray5, 0, byArray3, 576);
            for (n5 = 0; n5 < 32; ++n5) {
                if (byArray2[n2 + n6 * 32 + n5] == byArray5[n5]) continue;
                for (n5 = 0; n5 < 32; ++n5) {
                    byArray[n5] = 0;
                }
                return -1;
            }
        }
        for (n3 = 0; n3 < 32; ++n3) {
            hashFunctions.hash_2n_n_mask(byArray5, n3 * 32, byArray2, n2 + 2 * n3 * 32, byArray3, 640);
        }
        for (n3 = 0; n3 < 16; ++n3) {
            hashFunctions.hash_2n_n_mask(byArray5, n3 * 32, byArray5, 2 * n3 * 32, byArray3, 704);
        }
        for (n3 = 0; n3 < 8; ++n3) {
            hashFunctions.hash_2n_n_mask(byArray5, n3 * 32, byArray5, 2 * n3 * 32, byArray3, 768);
        }
        for (n3 = 0; n3 < 4; ++n3) {
            hashFunctions.hash_2n_n_mask(byArray5, n3 * 32, byArray5, 2 * n3 * 32, byArray3, 832);
        }
        for (n3 = 0; n3 < 2; ++n3) {
            hashFunctions.hash_2n_n_mask(byArray5, n3 * 32, byArray5, 2 * n3 * 32, byArray3, 896);
        }
        hashFunctions.hash_2n_n_mask(byArray, 0, byArray5, 0, byArray3, 960);
        return 0;
    }
}

