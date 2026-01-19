/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.sphincs;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.crypto.sphincs.Permute;
import org.bouncycastle.util.Strings;

class HashFunctions {
    private static final byte[] hashc = Strings.toByteArray("expand 32-byte to 64-byte state!");
    private final Digest dig256;
    private final Digest dig512;
    private final Permute perm = new Permute();

    HashFunctions(Digest digest) {
        this(digest, null);
    }

    HashFunctions(Digest digest, Digest digest2) {
        this.dig256 = digest;
        this.dig512 = digest2;
    }

    int varlen_hash(byte[] byArray, int n2, byte[] byArray2, int n3) {
        this.dig256.update(byArray2, 0, n3);
        this.dig256.doFinal(byArray, n2);
        return 0;
    }

    Digest getMessageHash() {
        return this.dig512;
    }

    int hash_2n_n(byte[] byArray, int n2, byte[] byArray2, int n3) {
        int n4;
        byte[] byArray3 = new byte[64];
        for (n4 = 0; n4 < 32; ++n4) {
            byArray3[n4] = byArray2[n3 + n4];
            byArray3[n4 + 32] = hashc[n4];
        }
        this.perm.chacha_permute(byArray3, byArray3);
        for (n4 = 0; n4 < 32; ++n4) {
            byArray3[n4] = (byte)(byArray3[n4] ^ byArray2[n3 + n4 + 32]);
        }
        this.perm.chacha_permute(byArray3, byArray3);
        for (n4 = 0; n4 < 32; ++n4) {
            byArray[n2 + n4] = byArray3[n4];
        }
        return 0;
    }

    int hash_2n_n_mask(byte[] byArray, int n2, byte[] byArray2, int n3, byte[] byArray3, int n4) {
        byte[] byArray4 = new byte[64];
        for (int i2 = 0; i2 < 64; ++i2) {
            byArray4[i2] = (byte)(byArray2[n3 + i2] ^ byArray3[n4 + i2]);
        }
        int n5 = this.hash_2n_n(byArray, n2, byArray4, 0);
        return n5;
    }

    int hash_n_n(byte[] byArray, int n2, byte[] byArray2, int n3) {
        int n4;
        byte[] byArray3 = new byte[64];
        for (n4 = 0; n4 < 32; ++n4) {
            byArray3[n4] = byArray2[n3 + n4];
            byArray3[n4 + 32] = hashc[n4];
        }
        this.perm.chacha_permute(byArray3, byArray3);
        for (n4 = 0; n4 < 32; ++n4) {
            byArray[n2 + n4] = byArray3[n4];
        }
        return 0;
    }

    int hash_n_n_mask(byte[] byArray, int n2, byte[] byArray2, int n3, byte[] byArray3, int n4) {
        byte[] byArray4 = new byte[32];
        for (int i2 = 0; i2 < 32; ++i2) {
            byArray4[i2] = (byte)(byArray2[n3 + i2] ^ byArray3[n4 + i2]);
        }
        return this.hash_n_n(byArray, n2, byArray4, 0);
    }
}

