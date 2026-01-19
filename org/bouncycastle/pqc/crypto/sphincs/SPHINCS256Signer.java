/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.sphincs;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.pqc.crypto.MessageSigner;
import org.bouncycastle.pqc.crypto.sphincs.HashFunctions;
import org.bouncycastle.pqc.crypto.sphincs.Horst;
import org.bouncycastle.pqc.crypto.sphincs.SPHINCSPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.sphincs.SPHINCSPublicKeyParameters;
import org.bouncycastle.pqc.crypto.sphincs.Seed;
import org.bouncycastle.pqc.crypto.sphincs.Tree;
import org.bouncycastle.pqc.crypto.sphincs.Wots;
import org.bouncycastle.util.Pack;

public class SPHINCS256Signer
implements MessageSigner {
    private final HashFunctions hashFunctions;
    private byte[] keyData;

    public SPHINCS256Signer(Digest digest, Digest digest2) {
        if (digest.getDigestSize() != 32) {
            throw new IllegalArgumentException("n-digest needs to produce 32 bytes of output");
        }
        if (digest2.getDigestSize() != 64) {
            throw new IllegalArgumentException("2n-digest needs to produce 64 bytes of output");
        }
        this.hashFunctions = new HashFunctions(digest, digest2);
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.keyData = bl ? (cipherParameters instanceof ParametersWithRandom ? ((SPHINCSPrivateKeyParameters)((ParametersWithRandom)cipherParameters).getParameters()).getKeyData() : ((SPHINCSPrivateKeyParameters)cipherParameters).getKeyData()) : ((SPHINCSPublicKeyParameters)cipherParameters).getKeyData();
    }

    @Override
    public byte[] generateSignature(byte[] byArray) {
        return this.crypto_sign(this.hashFunctions, byArray, this.keyData);
    }

    @Override
    public boolean verifySignature(byte[] byArray, byte[] byArray2) {
        return this.verify(this.hashFunctions, byArray, byArray2, this.keyData);
    }

    static void validate_authpath(HashFunctions hashFunctions, byte[] byArray, byte[] byArray2, int n2, byte[] byArray3, int n3, byte[] byArray4, int n4) {
        int n5;
        byte[] byArray5 = new byte[64];
        if ((n2 & 1) != 0) {
            for (n5 = 0; n5 < 32; ++n5) {
                byArray5[32 + n5] = byArray2[n5];
            }
            for (n5 = 0; n5 < 32; ++n5) {
                byArray5[n5] = byArray3[n3 + n5];
            }
        } else {
            for (n5 = 0; n5 < 32; ++n5) {
                byArray5[n5] = byArray2[n5];
            }
            for (n5 = 0; n5 < 32; ++n5) {
                byArray5[32 + n5] = byArray3[n3 + n5];
            }
        }
        int n6 = n3 + 32;
        for (int i2 = 0; i2 < n4 - 1; ++i2) {
            if (((n2 >>>= 1) & 1) != 0) {
                hashFunctions.hash_2n_n_mask(byArray5, 32, byArray5, 0, byArray4, 2 * (7 + i2) * 32);
                for (n5 = 0; n5 < 32; ++n5) {
                    byArray5[n5] = byArray3[n6 + n5];
                }
            } else {
                hashFunctions.hash_2n_n_mask(byArray5, 0, byArray5, 0, byArray4, 2 * (7 + i2) * 32);
                for (n5 = 0; n5 < 32; ++n5) {
                    byArray5[n5 + 32] = byArray3[n6 + n5];
                }
            }
            n6 += 32;
        }
        hashFunctions.hash_2n_n_mask(byArray, 0, byArray5, 0, byArray4, 2 * (7 + n4 - 1) * 32);
    }

    static void compute_authpath_wots(HashFunctions hashFunctions, byte[] byArray, byte[] byArray2, int n2, Tree.leafaddr leafaddr2, byte[] byArray3, byte[] byArray4, int n3) {
        int n4;
        Tree.leafaddr leafaddr3 = new Tree.leafaddr(leafaddr2);
        byte[] byArray5 = new byte[2048];
        byte[] byArray6 = new byte[1024];
        byte[] byArray7 = new byte[68608];
        leafaddr3.subleaf = 0L;
        while (leafaddr3.subleaf < 32L) {
            Seed.get_seed(hashFunctions, byArray6, (int)(leafaddr3.subleaf * 32L), byArray3, leafaddr3);
            ++leafaddr3.subleaf;
        }
        Wots wots = new Wots();
        leafaddr3.subleaf = 0L;
        while (leafaddr3.subleaf < 32L) {
            wots.wots_pkgen(hashFunctions, byArray7, (int)(leafaddr3.subleaf * 67L * 32L), byArray6, (int)(leafaddr3.subleaf * 32L), byArray4, 0);
            ++leafaddr3.subleaf;
        }
        leafaddr3.subleaf = 0L;
        while (leafaddr3.subleaf < 32L) {
            Tree.l_tree(hashFunctions, byArray5, (int)(1024L + leafaddr3.subleaf * 32L), byArray7, (int)(leafaddr3.subleaf * 67L * 32L), byArray4, 0);
            ++leafaddr3.subleaf;
        }
        int n5 = 0;
        for (n4 = 32; n4 > 0; n4 >>>= 1) {
            for (int i2 = 0; i2 < n4; i2 += 2) {
                hashFunctions.hash_2n_n_mask(byArray5, (n4 >>> 1) * 32 + (i2 >>> 1) * 32, byArray5, n4 * 32 + i2 * 32, byArray4, 2 * (7 + n5) * 32);
            }
            ++n5;
        }
        int n6 = (int)leafaddr2.subleaf;
        for (n4 = 0; n4 < n3; ++n4) {
            System.arraycopy(byArray5, (32 >>> n4) * 32 + (n6 >>> n4 ^ 1) * 32, byArray2, n2 + n4 * 32, 32);
        }
        System.arraycopy(byArray5, 32, byArray, 0, 32);
    }

    byte[] crypto_sign(HashFunctions hashFunctions, byte[] byArray, byte[] byArray2) {
        int n2;
        byte[] byArray3 = new byte[41000];
        byte[] byArray4 = new byte[32];
        byte[] byArray5 = new byte[64];
        long[] lArray = new long[8];
        byte[] byArray6 = new byte[32];
        byte[] byArray7 = new byte[32];
        byte[] byArray8 = new byte[1024];
        byte[] byArray9 = new byte[1088];
        for (n2 = 0; n2 < 1088; ++n2) {
            byArray9[n2] = byArray2[n2];
        }
        int n3 = 40968;
        System.arraycopy(byArray9, 1056, byArray3, n3, 32);
        Digest digest = hashFunctions.getMessageHash();
        Object object = new byte[digest.getDigestSize()];
        digest.update(byArray3, n3, 32);
        digest.update(byArray, 0, byArray.length);
        digest.doFinal((byte[])object, 0);
        this.zerobytes(byArray3, n3, 32);
        for (int i2 = 0; i2 != lArray.length; ++i2) {
            lArray[i2] = Pack.littleEndianToLong(object, i2 * 8);
        }
        long l2 = lArray[0] & 0xFFFFFFFFFFFFFFFL;
        System.arraycopy(object, 16, byArray4, 0, 32);
        n3 = 39912;
        System.arraycopy(byArray4, 0, byArray3, n3, 32);
        Tree.leafaddr leafaddr2 = new Tree.leafaddr();
        leafaddr2.level = 11;
        leafaddr2.subtree = 0L;
        leafaddr2.subleaf = 0L;
        int n4 = n3 + 32;
        System.arraycopy(byArray9, 32, byArray3, n4, 1024);
        Tree.treehash(hashFunctions, byArray3, n4 + 1024, 5, byArray9, leafaddr2, byArray3, n4);
        digest = hashFunctions.getMessageHash();
        digest.update(byArray3, n3, 1088);
        digest.update(byArray, 0, byArray.length);
        digest.doFinal(byArray5, 0);
        Tree.leafaddr leafaddr3 = new Tree.leafaddr();
        leafaddr3.level = 12;
        leafaddr3.subleaf = (int)(l2 & 0x1FL);
        leafaddr3.subtree = l2 >>> 5;
        for (n2 = 0; n2 < 32; ++n2) {
            byArray3[n2] = byArray4[n2];
        }
        int n5 = 32;
        System.arraycopy(byArray9, 32, byArray8, 0, 1024);
        for (n2 = 0; n2 < 8; ++n2) {
            byArray3[n5 + n2] = (byte)(l2 >>> 8 * n2 & 0xFFL);
        }
        Seed.get_seed(hashFunctions, byArray7, 0, byArray9, leafaddr3);
        object = new Horst();
        int n6 = Horst.horst_sign(hashFunctions, byArray3, n5 += 8, byArray6, byArray7, byArray8, byArray5);
        n5 += n6;
        Wots wots = new Wots();
        n2 = 0;
        while (n2 < 12) {
            leafaddr3.level = n2++;
            Seed.get_seed(hashFunctions, byArray7, 0, byArray9, leafaddr3);
            wots.wots_sign(hashFunctions, byArray3, n5, byArray6, byArray7, byArray8);
            SPHINCS256Signer.compute_authpath_wots(hashFunctions, byArray6, byArray3, n5 += 2144, leafaddr3, byArray9, byArray8, 5);
            n5 += 160;
            leafaddr3.subleaf = (int)(leafaddr3.subtree & 0x1FL);
            leafaddr3.subtree >>>= 5;
        }
        this.zerobytes(byArray9, 0, 1088);
        return byArray3;
    }

    private void zerobytes(byte[] byArray, int n2, int n3) {
        for (int i2 = 0; i2 != n3; ++i2) {
            byArray[n2 + i2] = 0;
        }
    }

    boolean verify(HashFunctions hashFunctions, byte[] byArray, byte[] byArray2, byte[] byArray3) {
        int n2;
        int n3 = byArray2.length;
        long l2 = 0L;
        byte[] byArray4 = new byte[2144];
        byte[] byArray5 = new byte[32];
        byte[] byArray6 = new byte[32];
        byte[] byArray7 = new byte[41000];
        byte[] byArray8 = new byte[1056];
        if (n3 != 41000) {
            throw new IllegalArgumentException("signature wrong size");
        }
        byte[] byArray9 = new byte[64];
        for (n2 = 0; n2 < 1056; ++n2) {
            byArray8[n2] = byArray3[n2];
        }
        Object object = new byte[32];
        for (n2 = 0; n2 < 32; ++n2) {
            object[n2] = byArray2[n2];
        }
        System.arraycopy(byArray2, 0, byArray7, 0, 41000);
        Digest digest = hashFunctions.getMessageHash();
        digest.update((byte[])object, 0, 32);
        digest.update(byArray8, 0, 1056);
        digest.update(byArray, 0, byArray.length);
        digest.doFinal(byArray9, 0);
        int n4 = 0;
        n4 += 32;
        n3 -= 32;
        for (n2 = 0; n2 < 8; ++n2) {
            l2 ^= (long)(byArray7[n4 + n2] & 0xFF) << 8 * n2;
        }
        new Horst();
        Horst.horst_verify(hashFunctions, byArray6, byArray7, n4 + 8, byArray8, byArray9);
        n4 += 8;
        n3 -= 8;
        n4 += 13312;
        n3 -= 13312;
        object = new Wots();
        for (n2 = 0; n2 < 12; ++n2) {
            ((Wots)object).wots_verify(hashFunctions, byArray4, byArray7, n4, byArray6, byArray8);
            n3 -= 2144;
            Tree.l_tree(hashFunctions, byArray5, 0, byArray4, 0, byArray8, 0);
            SPHINCS256Signer.validate_authpath(hashFunctions, byArray6, byArray5, (int)(l2 & 0x1FL), byArray7, n4 += 2144, byArray8, 5);
            l2 >>= 5;
            n4 += 160;
            n3 -= 160;
        }
        boolean bl = true;
        for (n2 = 0; n2 < 32; ++n2) {
            bl &= byArray6[n2] == byArray8[n2 + 1024];
        }
        return bl;
    }
}

