/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.mceliece;

import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.prng.DigestRandomGenerator;
import org.bouncycastle.pqc.crypto.MessageEncryptor;
import org.bouncycastle.pqc.legacy.crypto.mceliece.Conversions;
import org.bouncycastle.pqc.legacy.crypto.mceliece.McElieceCCA2KeyParameters;
import org.bouncycastle.pqc.legacy.crypto.mceliece.McElieceCCA2Primitives;
import org.bouncycastle.pqc.legacy.crypto.mceliece.McElieceCCA2PrivateKeyParameters;
import org.bouncycastle.pqc.legacy.crypto.mceliece.McElieceCCA2PublicKeyParameters;
import org.bouncycastle.pqc.legacy.crypto.mceliece.Utils;
import org.bouncycastle.pqc.legacy.math.linearalgebra.ByteUtils;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2Vector;
import org.bouncycastle.pqc.legacy.math.linearalgebra.IntegerFunctions;

public class McElieceKobaraImaiCipher
implements MessageEncryptor {
    public static final String OID = "1.3.6.1.4.1.8301.3.1.3.4.2.3";
    private static final String DEFAULT_PRNG_NAME = "SHA1PRNG";
    public static final byte[] PUBLIC_CONSTANT = "a predetermined public constant".getBytes();
    private Digest messDigest;
    private SecureRandom sr;
    McElieceCCA2KeyParameters key;
    private int n;
    private int k;
    private int t;
    private boolean forEncryption;

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.forEncryption = bl;
        if (bl) {
            if (cipherParameters instanceof ParametersWithRandom) {
                ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
                this.sr = parametersWithRandom.getRandom();
                this.key = (McElieceCCA2PublicKeyParameters)parametersWithRandom.getParameters();
                this.initCipherEncrypt((McElieceCCA2PublicKeyParameters)this.key);
            } else {
                this.sr = CryptoServicesRegistrar.getSecureRandom();
                this.key = (McElieceCCA2PublicKeyParameters)cipherParameters;
                this.initCipherEncrypt((McElieceCCA2PublicKeyParameters)this.key);
            }
        } else {
            this.key = (McElieceCCA2PrivateKeyParameters)cipherParameters;
            this.initCipherDecrypt((McElieceCCA2PrivateKeyParameters)this.key);
        }
    }

    public int getKeySize(McElieceCCA2KeyParameters mcElieceCCA2KeyParameters) {
        if (mcElieceCCA2KeyParameters instanceof McElieceCCA2PublicKeyParameters) {
            return ((McElieceCCA2PublicKeyParameters)mcElieceCCA2KeyParameters).getN();
        }
        if (mcElieceCCA2KeyParameters instanceof McElieceCCA2PrivateKeyParameters) {
            return ((McElieceCCA2PrivateKeyParameters)mcElieceCCA2KeyParameters).getN();
        }
        throw new IllegalArgumentException("unsupported type");
    }

    private void initCipherEncrypt(McElieceCCA2PublicKeyParameters mcElieceCCA2PublicKeyParameters) {
        this.messDigest = Utils.getDigest(mcElieceCCA2PublicKeyParameters.getDigest());
        this.n = mcElieceCCA2PublicKeyParameters.getN();
        this.k = mcElieceCCA2PublicKeyParameters.getK();
        this.t = mcElieceCCA2PublicKeyParameters.getT();
    }

    private void initCipherDecrypt(McElieceCCA2PrivateKeyParameters mcElieceCCA2PrivateKeyParameters) {
        this.messDigest = Utils.getDigest(mcElieceCCA2PrivateKeyParameters.getDigest());
        this.n = mcElieceCCA2PrivateKeyParameters.getN();
        this.k = mcElieceCCA2PrivateKeyParameters.getK();
        this.t = mcElieceCCA2PrivateKeyParameters.getT();
    }

    @Override
    public byte[] messageEncrypt(byte[] byArray) {
        if (!this.forEncryption) {
            throw new IllegalStateException("cipher initialised for decryption");
        }
        int n2 = this.messDigest.getDigestSize();
        int n3 = this.k >> 3;
        int n4 = IntegerFunctions.binomial(this.n, this.t).bitLength() - 1 >> 3;
        int n5 = n3 + n4 - n2 - PUBLIC_CONSTANT.length;
        if (byArray.length > n5) {
            n5 = byArray.length;
        }
        int n6 = n5 + PUBLIC_CONSTANT.length;
        int n7 = n6 + n2 - n3 - n4;
        byte[] byArray2 = new byte[n6];
        System.arraycopy(byArray, 0, byArray2, 0, byArray.length);
        System.arraycopy(PUBLIC_CONSTANT, 0, byArray2, n5, PUBLIC_CONSTANT.length);
        byte[] byArray3 = new byte[n2];
        this.sr.nextBytes(byArray3);
        DigestRandomGenerator digestRandomGenerator = new DigestRandomGenerator(new SHA1Digest());
        digestRandomGenerator.addSeedMaterial(byArray3);
        byte[] byArray4 = new byte[n6];
        digestRandomGenerator.nextBytes(byArray4);
        for (int i2 = n6 - 1; i2 >= 0; --i2) {
            int n8 = i2;
            byArray4[n8] = (byte)(byArray4[n8] ^ byArray2[i2]);
        }
        byte[] byArray5 = new byte[this.messDigest.getDigestSize()];
        this.messDigest.update(byArray4, 0, byArray4.length);
        this.messDigest.doFinal(byArray5, 0);
        for (int i3 = n2 - 1; i3 >= 0; --i3) {
            int n9 = i3;
            byArray5[n9] = (byte)(byArray5[n9] ^ byArray3[i3]);
        }
        byte[] byArray6 = ByteUtils.concatenate(byArray5, byArray4);
        byte[] byArray7 = new byte[]{};
        if (n7 > 0) {
            byArray7 = new byte[n7];
            System.arraycopy(byArray6, 0, byArray7, 0, n7);
        }
        byte[] byArray8 = new byte[n4];
        System.arraycopy(byArray6, n7, byArray8, 0, n4);
        byte[] byArray9 = new byte[n3];
        System.arraycopy(byArray6, n7 + n4, byArray9, 0, n3);
        GF2Vector gF2Vector = GF2Vector.OS2VP(this.k, byArray9);
        GF2Vector gF2Vector2 = Conversions.encode(this.n, this.t, byArray8);
        byte[] byArray10 = McElieceCCA2Primitives.encryptionPrimitive((McElieceCCA2PublicKeyParameters)this.key, gF2Vector, gF2Vector2).getEncoded();
        if (n7 > 0) {
            return ByteUtils.concatenate(byArray7, byArray10);
        }
        return byArray10;
    }

    @Override
    public byte[] messageDecrypt(byte[] byArray) throws InvalidCipherTextException {
        byte[] byArray2;
        byte[] byArray3;
        byte[] byArray4;
        byte[] byArray5;
        Object object;
        if (this.forEncryption) {
            throw new IllegalStateException("cipher initialised for decryption");
        }
        int n2 = this.n >> 3;
        if (byArray.length < n2) {
            throw new InvalidCipherTextException("Bad Padding: Ciphertext too short.");
        }
        int n3 = this.messDigest.getDigestSize();
        int n4 = this.k >> 3;
        int n5 = IntegerFunctions.binomial(this.n, this.t).bitLength() - 1 >> 3;
        int n6 = byArray.length - n2;
        if (n6 > 0) {
            object = ByteUtils.split(byArray, n6);
            byArray5 = object[0];
            byArray4 = object[1];
        } else {
            byArray5 = new byte[]{};
            byArray4 = byArray;
        }
        object = GF2Vector.OS2VP(this.n, byArray4);
        GF2Vector[] gF2VectorArray = McElieceCCA2Primitives.decryptionPrimitive((McElieceCCA2PrivateKeyParameters)this.key, (GF2Vector)object);
        byte[] byArray6 = gF2VectorArray[0].getEncoded();
        GF2Vector gF2Vector = gF2VectorArray[1];
        if (byArray6.length > n4) {
            byArray6 = ByteUtils.subArray(byArray6, 0, n4);
        }
        if ((byArray3 = Conversions.decode(this.n, this.t, gF2Vector)).length < n5) {
            byArray2 = new byte[n5];
            System.arraycopy(byArray3, 0, byArray2, n5 - byArray3.length, byArray3.length);
            byArray3 = byArray2;
        }
        byArray2 = ByteUtils.concatenate(byArray5, byArray3);
        byArray2 = ByteUtils.concatenate(byArray2, byArray6);
        int n7 = byArray2.length - n3;
        byte[][] byArray7 = ByteUtils.split(byArray2, n3);
        byte[] byArray8 = byArray7[0];
        byte[] byArray9 = byArray7[1];
        byte[] byArray10 = new byte[this.messDigest.getDigestSize()];
        this.messDigest.update(byArray9, 0, byArray9.length);
        this.messDigest.doFinal(byArray10, 0);
        for (int i2 = n3 - 1; i2 >= 0; --i2) {
            int n8 = i2;
            byArray10[n8] = (byte)(byArray10[n8] ^ byArray8[i2]);
        }
        DigestRandomGenerator digestRandomGenerator = new DigestRandomGenerator(new SHA1Digest());
        digestRandomGenerator.addSeedMaterial(byArray10);
        byte[] byArray11 = new byte[n7];
        digestRandomGenerator.nextBytes(byArray11);
        for (int i3 = n7 - 1; i3 >= 0; --i3) {
            int n9 = i3;
            byArray11[n9] = (byte)(byArray11[n9] ^ byArray9[i3]);
        }
        if (byArray11.length < n7) {
            throw new InvalidCipherTextException("Bad Padding: invalid ciphertext");
        }
        byte[][] byArray12 = ByteUtils.split(byArray11, n7 - PUBLIC_CONSTANT.length);
        byte[] byArray13 = byArray12[0];
        byte[] byArray14 = byArray12[1];
        if (!ByteUtils.equals(byArray14, PUBLIC_CONSTANT)) {
            throw new InvalidCipherTextException("Bad Padding: invalid ciphertext");
        }
        return byArray13;
    }
}

