/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.mlkem;

import java.security.SecureRandom;
import org.bouncycastle.pqc.crypto.mlkem.MLKEMIndCpa;
import org.bouncycastle.pqc.crypto.mlkem.PolyVec;
import org.bouncycastle.pqc.crypto.mlkem.Symmetric;
import org.bouncycastle.util.Arrays;

class MLKEMEngine {
    private SecureRandom random;
    private MLKEMIndCpa indCpa;
    public static final int KyberN = 256;
    public static final int KyberQ = 3329;
    public static final int KyberQinv = 62209;
    public static final int KyberSymBytes = 32;
    private static final int KyberSharedSecretBytes = 32;
    public static final int KyberPolyBytes = 384;
    private static final int KyberEta2 = 2;
    private static final int KyberIndCpaMsgBytes = 32;
    private final int KyberK;
    private final int KyberPolyVecBytes;
    private final int KyberPolyCompressedBytes;
    private final int KyberPolyVecCompressedBytes;
    private final int KyberEta1;
    private final int KyberIndCpaPublicKeyBytes;
    private final int KyberIndCpaSecretKeyBytes;
    private final int KyberIndCpaBytes;
    private final int KyberPublicKeyBytes;
    private final int KyberSecretKeyBytes;
    private final int KyberCipherTextBytes;
    private final int CryptoBytes;
    private final int CryptoSecretKeyBytes;
    private final int CryptoPublicKeyBytes;
    private final int CryptoCipherTextBytes;
    private final int sessionKeyLength;
    private final Symmetric symmetric;

    public Symmetric getSymmetric() {
        return this.symmetric;
    }

    public static int getKyberEta2() {
        return 2;
    }

    public static int getKyberIndCpaMsgBytes() {
        return 32;
    }

    public int getCryptoCipherTextBytes() {
        return this.CryptoCipherTextBytes;
    }

    public int getCryptoPublicKeyBytes() {
        return this.CryptoPublicKeyBytes;
    }

    public int getCryptoSecretKeyBytes() {
        return this.CryptoSecretKeyBytes;
    }

    public int getCryptoBytes() {
        return this.CryptoBytes;
    }

    public int getKyberCipherTextBytes() {
        return this.KyberCipherTextBytes;
    }

    public int getKyberSecretKeyBytes() {
        return this.KyberSecretKeyBytes;
    }

    public int getKyberIndCpaPublicKeyBytes() {
        return this.KyberIndCpaPublicKeyBytes;
    }

    public int getKyberIndCpaSecretKeyBytes() {
        return this.KyberIndCpaSecretKeyBytes;
    }

    public int getKyberIndCpaBytes() {
        return this.KyberIndCpaBytes;
    }

    public int getKyberPublicKeyBytes() {
        return this.KyberPublicKeyBytes;
    }

    public int getKyberPolyCompressedBytes() {
        return this.KyberPolyCompressedBytes;
    }

    public int getKyberK() {
        return this.KyberK;
    }

    public int getKyberPolyVecBytes() {
        return this.KyberPolyVecBytes;
    }

    public int getKyberPolyVecCompressedBytes() {
        return this.KyberPolyVecCompressedBytes;
    }

    public int getKyberEta1() {
        return this.KyberEta1;
    }

    public MLKEMEngine(int n2) {
        this.KyberK = n2;
        switch (n2) {
            case 2: {
                this.KyberEta1 = 3;
                this.KyberPolyCompressedBytes = 128;
                this.KyberPolyVecCompressedBytes = n2 * 320;
                this.sessionKeyLength = 32;
                break;
            }
            case 3: {
                this.KyberEta1 = 2;
                this.KyberPolyCompressedBytes = 128;
                this.KyberPolyVecCompressedBytes = n2 * 320;
                this.sessionKeyLength = 32;
                break;
            }
            case 4: {
                this.KyberEta1 = 2;
                this.KyberPolyCompressedBytes = 160;
                this.KyberPolyVecCompressedBytes = n2 * 352;
                this.sessionKeyLength = 32;
                break;
            }
            default: {
                throw new IllegalArgumentException("K: " + n2 + " is not supported for Crystals Kyber");
            }
        }
        this.KyberPolyVecBytes = n2 * 384;
        this.KyberIndCpaPublicKeyBytes = this.KyberPolyVecBytes + 32;
        this.KyberIndCpaSecretKeyBytes = this.KyberPolyVecBytes;
        this.KyberIndCpaBytes = this.KyberPolyVecCompressedBytes + this.KyberPolyCompressedBytes;
        this.KyberPublicKeyBytes = this.KyberIndCpaPublicKeyBytes;
        this.KyberSecretKeyBytes = this.KyberIndCpaSecretKeyBytes + this.KyberIndCpaPublicKeyBytes + 64;
        this.KyberCipherTextBytes = this.KyberIndCpaBytes;
        this.CryptoBytes = 32;
        this.CryptoSecretKeyBytes = this.KyberSecretKeyBytes;
        this.CryptoPublicKeyBytes = this.KyberPublicKeyBytes;
        this.CryptoCipherTextBytes = this.KyberCipherTextBytes;
        this.symmetric = new Symmetric.ShakeSymmetric();
        this.indCpa = new MLKEMIndCpa(this);
    }

    public void init(SecureRandom secureRandom) {
        this.random = secureRandom;
    }

    public byte[][] generateKemKeyPair() {
        byte[] byArray = new byte[32];
        byte[] byArray2 = new byte[32];
        this.random.nextBytes(byArray);
        this.random.nextBytes(byArray2);
        return this.generateKemKeyPairInternal(byArray, byArray2);
    }

    public byte[][] generateKemKeyPairInternal(byte[] byArray, byte[] byArray2) {
        byte[][] byArray3 = this.indCpa.generateKeyPair(byArray);
        byte[] byArray4 = new byte[this.KyberIndCpaSecretKeyBytes];
        System.arraycopy(byArray3[1], 0, byArray4, 0, this.KyberIndCpaSecretKeyBytes);
        byte[] byArray5 = new byte[32];
        this.symmetric.hash_h(byArray5, byArray3[0], 0);
        byte[] byArray6 = new byte[this.KyberIndCpaPublicKeyBytes];
        System.arraycopy(byArray3[0], 0, byArray6, 0, this.KyberIndCpaPublicKeyBytes);
        return new byte[][]{Arrays.copyOfRange(byArray6, 0, byArray6.length - 32), Arrays.copyOfRange(byArray6, byArray6.length - 32, byArray6.length), byArray4, byArray5, byArray2, Arrays.concatenate(byArray, byArray2)};
    }

    public byte[][] kemEncryptInternal(byte[] byArray, byte[] byArray2) {
        byte[] byArray3 = new byte[64];
        byte[] byArray4 = new byte[64];
        System.arraycopy(byArray2, 0, byArray3, 0, 32);
        this.symmetric.hash_h(byArray3, byArray, 32);
        this.symmetric.hash_g(byArray4, byArray3);
        byte[] byArray5 = this.indCpa.encrypt(byArray, Arrays.copyOfRange(byArray3, 0, 32), Arrays.copyOfRange(byArray4, 32, byArray4.length));
        byte[] byArray6 = new byte[this.sessionKeyLength];
        System.arraycopy(byArray4, 0, byArray6, 0, byArray6.length);
        byte[][] byArrayArray = new byte[][]{byArray6, byArray5};
        return byArrayArray;
    }

    public byte[] kemDecryptInternal(byte[] byArray, byte[] byArray2) {
        byte[] byArray3 = new byte[64];
        byte[] byArray4 = new byte[64];
        byte[] byArray5 = Arrays.copyOfRange(byArray, this.KyberIndCpaSecretKeyBytes, byArray.length);
        System.arraycopy(this.indCpa.decrypt(byArray, byArray2), 0, byArray3, 0, 32);
        System.arraycopy(byArray, this.KyberSecretKeyBytes - 64, byArray3, 32, 32);
        this.symmetric.hash_g(byArray4, byArray3);
        byte[] byArray6 = new byte[32 + this.KyberCipherTextBytes];
        System.arraycopy(byArray, this.KyberSecretKeyBytes - 32, byArray6, 0, 32);
        System.arraycopy(byArray2, 0, byArray6, 32, this.KyberCipherTextBytes);
        this.symmetric.kdf(byArray6, byArray6);
        byte[] byArray7 = this.indCpa.encrypt(byArray5, Arrays.copyOfRange(byArray3, 0, 32), Arrays.copyOfRange(byArray4, 32, byArray4.length));
        boolean bl = !Arrays.constantTimeAreEqual(byArray2, byArray7);
        this.cmov(byArray4, byArray6, 32, bl);
        return Arrays.copyOfRange(byArray4, 0, this.sessionKeyLength);
    }

    public byte[][] kemEncrypt(byte[] byArray, byte[] byArray2) {
        if (byArray.length != this.KyberIndCpaPublicKeyBytes) {
            throw new IllegalArgumentException("Input validation Error: Type check failed for ml-kem encapsulation");
        }
        PolyVec polyVec = new PolyVec(this);
        byte[] byArray3 = this.indCpa.unpackPublicKey(polyVec, byArray);
        byte[] byArray4 = this.indCpa.packPublicKey(polyVec, byArray3);
        if (!Arrays.areEqual(byArray4, byArray)) {
            throw new IllegalArgumentException("Input validation: Modulus check failed for ml-kem encapsulation");
        }
        return this.kemEncryptInternal(byArray, byArray2);
    }

    public byte[] kemDecrypt(byte[] byArray, byte[] byArray2) {
        return this.kemDecryptInternal(byArray, byArray2);
    }

    private void cmov(byte[] byArray, byte[] byArray2, int n2, boolean bl) {
        if (bl) {
            System.arraycopy(byArray2, 0, byArray, 0, n2);
        } else {
            System.arraycopy(byArray, 0, byArray, 0, n2);
        }
    }

    public void getRandomBytes(byte[] byArray) {
        this.random.nextBytes(byArray);
    }
}

