/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import java.util.ArrayList;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.engines.DSTU7624Engine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.util.Arrays;

public class DSTU7624WrapEngine
implements Wrapper {
    private static final int BYTES_IN_INTEGER = 4;
    private boolean forWrapping;
    private DSTU7624Engine engine;
    private byte[] B;
    private byte[] intArray;
    private byte[] checkSumArray;
    private byte[] zeroArray;
    private ArrayList<byte[]> Btemp;

    public DSTU7624WrapEngine(int n2) {
        this.engine = new DSTU7624Engine(n2);
        this.B = new byte[this.engine.getBlockSize() / 2];
        this.checkSumArray = new byte[this.engine.getBlockSize()];
        this.zeroArray = new byte[this.engine.getBlockSize()];
        this.Btemp = new ArrayList();
        this.intArray = new byte[4];
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithRandom) {
            cipherParameters = ((ParametersWithRandom)cipherParameters).getParameters();
        }
        this.forWrapping = bl;
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("invalid parameters passed to DSTU7624WrapEngine");
        }
        this.engine.init(bl, cipherParameters);
    }

    @Override
    public String getAlgorithmName() {
        return "DSTU7624WrapEngine";
    }

    @Override
    public byte[] wrap(byte[] byArray, int n2, int n3) {
        int n4;
        if (!this.forWrapping) {
            throw new IllegalStateException("not set for wrapping");
        }
        if (n3 % this.engine.getBlockSize() != 0) {
            throw new DataLengthException("wrap data must be a multiple of " + this.engine.getBlockSize() + " bytes");
        }
        if (n2 + n3 > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        int n5 = 2 * (1 + n3 / this.engine.getBlockSize());
        int n6 = (n5 - 1) * 6;
        byte[] byArray2 = new byte[n3 + this.engine.getBlockSize()];
        System.arraycopy(byArray, n2, byArray2, 0, n3);
        System.arraycopy(byArray2, 0, this.B, 0, this.engine.getBlockSize() / 2);
        this.Btemp.clear();
        int n7 = byArray2.length - this.engine.getBlockSize() / 2;
        int n8 = this.engine.getBlockSize() / 2;
        while (n7 != 0) {
            byte[] byArray3 = new byte[this.engine.getBlockSize() / 2];
            System.arraycopy(byArray2, n8, byArray3, 0, this.engine.getBlockSize() / 2);
            this.Btemp.add(byArray3);
            n7 -= this.engine.getBlockSize() / 2;
            n8 += this.engine.getBlockSize() / 2;
        }
        for (n4 = 0; n4 < n6; ++n4) {
            int n9;
            System.arraycopy(this.B, 0, byArray2, 0, this.engine.getBlockSize() / 2);
            System.arraycopy(this.Btemp.get(0), 0, byArray2, this.engine.getBlockSize() / 2, this.engine.getBlockSize() / 2);
            this.engine.processBlock(byArray2, 0, byArray2, 0);
            this.intToBytes(n4 + 1, this.intArray, 0);
            for (n9 = 0; n9 < 4; ++n9) {
                int n10 = n9 + this.engine.getBlockSize() / 2;
                byArray2[n10] = (byte)(byArray2[n10] ^ this.intArray[n9]);
            }
            System.arraycopy(byArray2, this.engine.getBlockSize() / 2, this.B, 0, this.engine.getBlockSize() / 2);
            for (n9 = 2; n9 < n5; ++n9) {
                System.arraycopy(this.Btemp.get(n9 - 1), 0, this.Btemp.get(n9 - 2), 0, this.engine.getBlockSize() / 2);
            }
            System.arraycopy(byArray2, 0, this.Btemp.get(n5 - 2), 0, this.engine.getBlockSize() / 2);
        }
        System.arraycopy(this.B, 0, byArray2, 0, this.engine.getBlockSize() / 2);
        n8 = this.engine.getBlockSize() / 2;
        for (n4 = 0; n4 < n5 - 1; ++n4) {
            System.arraycopy(this.Btemp.get(n4), 0, byArray2, n8, this.engine.getBlockSize() / 2);
            n8 += this.engine.getBlockSize() / 2;
        }
        return byArray2;
    }

    @Override
    public byte[] unwrap(byte[] byArray, int n2, int n3) throws InvalidCipherTextException {
        int n4;
        if (this.forWrapping) {
            throw new IllegalStateException("not set for unwrapping");
        }
        if (n3 % this.engine.getBlockSize() != 0) {
            throw new DataLengthException("unwrap data must be a multiple of " + this.engine.getBlockSize() + " bytes");
        }
        int n5 = 2 * n3 / this.engine.getBlockSize();
        int n6 = (n5 - 1) * 6;
        byte[] byArray2 = new byte[n3];
        System.arraycopy(byArray, n2, byArray2, 0, n3);
        byte[] byArray3 = new byte[this.engine.getBlockSize() / 2];
        System.arraycopy(byArray2, 0, byArray3, 0, this.engine.getBlockSize() / 2);
        this.Btemp.clear();
        int n7 = byArray2.length - this.engine.getBlockSize() / 2;
        int n8 = this.engine.getBlockSize() / 2;
        while (n7 != 0) {
            byte[] byArray4 = new byte[this.engine.getBlockSize() / 2];
            System.arraycopy(byArray2, n8, byArray4, 0, this.engine.getBlockSize() / 2);
            this.Btemp.add(byArray4);
            n7 -= this.engine.getBlockSize() / 2;
            n8 += this.engine.getBlockSize() / 2;
        }
        for (n4 = 0; n4 < n6; ++n4) {
            int n9;
            System.arraycopy(this.Btemp.get(n5 - 2), 0, byArray2, 0, this.engine.getBlockSize() / 2);
            System.arraycopy(byArray3, 0, byArray2, this.engine.getBlockSize() / 2, this.engine.getBlockSize() / 2);
            this.intToBytes(n6 - n4, this.intArray, 0);
            for (n9 = 0; n9 < 4; ++n9) {
                int n10 = n9 + this.engine.getBlockSize() / 2;
                byArray2[n10] = (byte)(byArray2[n10] ^ this.intArray[n9]);
            }
            this.engine.processBlock(byArray2, 0, byArray2, 0);
            System.arraycopy(byArray2, 0, byArray3, 0, this.engine.getBlockSize() / 2);
            for (n9 = 2; n9 < n5; ++n9) {
                System.arraycopy(this.Btemp.get(n5 - n9 - 1), 0, this.Btemp.get(n5 - n9), 0, this.engine.getBlockSize() / 2);
            }
            System.arraycopy(byArray2, this.engine.getBlockSize() / 2, this.Btemp.get(0), 0, this.engine.getBlockSize() / 2);
        }
        System.arraycopy(byArray3, 0, byArray2, 0, this.engine.getBlockSize() / 2);
        n8 = this.engine.getBlockSize() / 2;
        for (n4 = 0; n4 < n5 - 1; ++n4) {
            System.arraycopy(this.Btemp.get(n4), 0, byArray2, n8, this.engine.getBlockSize() / 2);
            n8 += this.engine.getBlockSize() / 2;
        }
        System.arraycopy(byArray2, byArray2.length - this.engine.getBlockSize(), this.checkSumArray, 0, this.engine.getBlockSize());
        byte[] byArray5 = new byte[byArray2.length - this.engine.getBlockSize()];
        if (!Arrays.areEqual(this.checkSumArray, this.zeroArray)) {
            throw new InvalidCipherTextException("checksum failed");
        }
        System.arraycopy(byArray2, 0, byArray5, 0, byArray2.length - this.engine.getBlockSize());
        return byArray5;
    }

    private void intToBytes(int n2, byte[] byArray, int n3) {
        byArray[n3 + 3] = (byte)(n2 >> 24);
        byArray[n3 + 2] = (byte)(n2 >> 16);
        byArray[n3 + 1] = (byte)(n2 >> 8);
        byArray[n3] = (byte)n2;
    }
}

