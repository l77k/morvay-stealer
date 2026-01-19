/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.hpke;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.AEADCipher;
import org.bouncycastle.crypto.modes.ChaCha20Poly1305;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class AEAD {
    private final short aeadId;
    private final byte[] key;
    private final byte[] baseNonce;
    private long seq = 0L;
    private AEADCipher cipher;

    public AEAD(short s2, byte[] byArray, byte[] byArray2) {
        this.key = byArray;
        this.baseNonce = byArray2;
        this.aeadId = s2;
        this.seq = 0L;
        switch (s2) {
            case 1: 
            case 2: {
                this.cipher = new GCMBlockCipher(new AESEngine());
                break;
            }
            case 3: {
                this.cipher = new ChaCha20Poly1305();
                break;
            }
        }
    }

    public byte[] seal(byte[] byArray, byte[] byArray2, int n2, int n3) throws InvalidCipherTextException {
        ParametersWithIV parametersWithIV;
        if (n2 < 0 || n2 > byArray2.length) {
            throw new IndexOutOfBoundsException("Invalid offset");
        }
        if (n2 + n3 > byArray2.length) {
            throw new IndexOutOfBoundsException("Invalid length");
        }
        switch (this.aeadId) {
            case 1: 
            case 2: 
            case 3: {
                parametersWithIV = new ParametersWithIV(new KeyParameter(this.key), this.ComputeNonce());
                break;
            }
            default: {
                throw new IllegalStateException("Export only mode, cannot be used to seal/open");
            }
        }
        this.cipher.init(true, parametersWithIV);
        this.cipher.processAADBytes(byArray, 0, byArray.length);
        byte[] byArray3 = new byte[this.cipher.getOutputSize(n3)];
        int n4 = this.cipher.processBytes(byArray2, n2, n3, byArray3, 0);
        this.cipher.doFinal(byArray3, n4);
        ++this.seq;
        return byArray3;
    }

    public byte[] seal(byte[] byArray, byte[] byArray2) throws InvalidCipherTextException {
        return this.seal(byArray, byArray2, 0, byArray2.length);
    }

    public byte[] open(byte[] byArray, byte[] byArray2, int n2, int n3) throws InvalidCipherTextException {
        ParametersWithIV parametersWithIV;
        if (n2 < 0 || n2 > byArray2.length) {
            throw new IndexOutOfBoundsException("Invalid offset");
        }
        if (n2 + n3 > byArray2.length) {
            throw new IndexOutOfBoundsException("Invalid length");
        }
        switch (this.aeadId) {
            case 1: 
            case 2: 
            case 3: {
                parametersWithIV = new ParametersWithIV(new KeyParameter(this.key), this.ComputeNonce());
                break;
            }
            default: {
                throw new IllegalStateException("Export only mode, cannot be used to seal/open");
            }
        }
        this.cipher.init(false, parametersWithIV);
        this.cipher.processAADBytes(byArray, 0, byArray.length);
        byte[] byArray3 = new byte[this.cipher.getOutputSize(n3)];
        int n4 = this.cipher.processBytes(byArray2, n2, n3, byArray3, 0);
        n4 += this.cipher.doFinal(byArray3, n4);
        ++this.seq;
        return byArray3;
    }

    public byte[] open(byte[] byArray, byte[] byArray2) throws InvalidCipherTextException {
        return this.open(byArray, byArray2, 0, byArray2.length);
    }

    private byte[] ComputeNonce() {
        byte[] byArray = Pack.longToBigEndian(this.seq);
        int n2 = this.baseNonce.length;
        byte[] byArray2 = Arrays.clone(this.baseNonce);
        for (int i2 = 0; i2 < 8; ++i2) {
            int n3 = n2 - 8 + i2;
            byArray2[n3] = (byte)(byArray2[n3] ^ byArray[i2]);
        }
        return byArray2;
    }
}

