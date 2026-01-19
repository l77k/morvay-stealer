/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.constraints.DefaultServiceProperties;
import org.bouncycastle.crypto.engines.Utils;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Pack;

public class NoekeonEngine
implements BlockCipher {
    private static final int SIZE = 16;
    private static final byte[] roundConstants = new byte[]{-128, 27, 54, 108, -40, -85, 77, -102, 47, 94, -68, 99, -58, -105, 53, 106, -44};
    private final int[] k = new int[4];
    private boolean _initialised = false;
    private boolean _forEncryption;

    @Override
    public String getAlgorithmName() {
        return "Noekeon";
    }

    @Override
    public int getBlockSize() {
        return 16;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("invalid parameter passed to Noekeon init - " + cipherParameters.getClass().getName());
        }
        KeyParameter keyParameter = (KeyParameter)cipherParameters;
        byte[] byArray = keyParameter.getKey();
        if (byArray.length != 16) {
            throw new IllegalArgumentException("Key length not 128 bits.");
        }
        Pack.bigEndianToInt(byArray, 0, this.k, 0, 4);
        if (!bl) {
            int n2 = this.k[0];
            int n3 = this.k[1];
            int n4 = this.k[2];
            int n5 = this.k[3];
            int n6 = n2 ^ n4;
            n6 ^= Integers.rotateLeft(n6, 8) ^ Integers.rotateLeft(n6, 24);
            int n7 = n3 ^ n5;
            n7 ^= Integers.rotateLeft(n7, 8) ^ Integers.rotateLeft(n7, 24);
            this.k[0] = n2 ^= n7;
            this.k[1] = n3 ^= n6;
            this.k[2] = n4 ^= n7;
            this.k[3] = n5 ^= n6;
        }
        this._forEncryption = bl;
        this._initialised = true;
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), 128, cipherParameters, Utils.getPurpose(bl)));
    }

    @Override
    public int processBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        if (!this._initialised) {
            throw new IllegalStateException(this.getAlgorithmName() + " not initialised");
        }
        if (n2 > byArray.length - 16) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 > byArray2.length - 16) {
            throw new OutputLengthException("output buffer too short");
        }
        return this._forEncryption ? this.encryptBlock(byArray, n2, byArray2, n3) : this.decryptBlock(byArray, n2, byArray2, n3);
    }

    @Override
    public void reset() {
    }

    private int encryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        int n4 = Pack.bigEndianToInt(byArray, n2);
        int n5 = Pack.bigEndianToInt(byArray, n2 + 4);
        int n6 = Pack.bigEndianToInt(byArray, n2 + 8);
        int n7 = Pack.bigEndianToInt(byArray, n2 + 12);
        int n8 = this.k[0];
        int n9 = this.k[1];
        int n10 = this.k[2];
        int n11 = this.k[3];
        int n12 = 0;
        while (true) {
            int n13 = (n4 ^= roundConstants[n12] & 0xFF) ^ n6;
            n13 ^= Integers.rotateLeft(n13, 8) ^ Integers.rotateLeft(n13, 24);
            n4 ^= n8;
            n6 ^= n10;
            int n14 = (n5 ^= n9) ^ (n7 ^= n11);
            n14 ^= Integers.rotateLeft(n14, 8) ^ Integers.rotateLeft(n14, 24);
            n4 ^= n14;
            n5 ^= n13;
            n6 ^= n14;
            n7 ^= n13;
            if (++n12 > 16) break;
            n5 = Integers.rotateLeft(n5, 1);
            n6 = Integers.rotateLeft(n6, 5);
            n13 = n7 = Integers.rotateLeft(n7, 2);
            n5 ^= n7 | n6;
            n7 = n4 ^ n6 & ~n5;
            n6 = n13 ^ ~n5 ^ n6 ^ n7;
            n4 = n13 ^ n6 & (n5 ^= n7 | n6);
            n5 = Integers.rotateLeft(n5, 31);
            n6 = Integers.rotateLeft(n6, 27);
            n7 = Integers.rotateLeft(n7, 30);
        }
        Pack.intToBigEndian(n4, byArray2, n3);
        Pack.intToBigEndian(n5, byArray2, n3 + 4);
        Pack.intToBigEndian(n6, byArray2, n3 + 8);
        Pack.intToBigEndian(n7, byArray2, n3 + 12);
        return 16;
    }

    private int decryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        int n4 = Pack.bigEndianToInt(byArray, n2);
        int n5 = Pack.bigEndianToInt(byArray, n2 + 4);
        int n6 = Pack.bigEndianToInt(byArray, n2 + 8);
        int n7 = Pack.bigEndianToInt(byArray, n2 + 12);
        int n8 = this.k[0];
        int n9 = this.k[1];
        int n10 = this.k[2];
        int n11 = this.k[3];
        int n12 = 16;
        while (true) {
            int n13 = n4 ^ n6;
            n13 ^= Integers.rotateLeft(n13, 8) ^ Integers.rotateLeft(n13, 24);
            n4 ^= n8;
            n6 ^= n10;
            int n14 = (n5 ^= n9) ^ (n7 ^= n11);
            n14 ^= Integers.rotateLeft(n14, 8) ^ Integers.rotateLeft(n14, 24);
            n4 ^= n14;
            n5 ^= n13;
            n6 ^= n14;
            n7 ^= n13;
            n4 ^= roundConstants[n12] & 0xFF;
            if (--n12 < 0) break;
            n5 = Integers.rotateLeft(n5, 1);
            n6 = Integers.rotateLeft(n6, 5);
            n13 = n7 = Integers.rotateLeft(n7, 2);
            n5 ^= n7 | n6;
            n7 = n4 ^ n6 & ~n5;
            n6 = n13 ^ ~n5 ^ n6 ^ n7;
            n4 = n13 ^ n6 & (n5 ^= n7 | n6);
            n5 = Integers.rotateLeft(n5, 31);
            n6 = Integers.rotateLeft(n6, 27);
            n7 = Integers.rotateLeft(n7, 30);
        }
        Pack.intToBigEndian(n4, byArray2, n3);
        Pack.intToBigEndian(n5, byArray2, n3 + 4);
        Pack.intToBigEndian(n6, byArray2, n3 + 8);
        Pack.intToBigEndian(n7, byArray2, n3 + 12);
        return 16;
    }
}

