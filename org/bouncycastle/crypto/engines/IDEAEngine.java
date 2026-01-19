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

public class IDEAEngine
implements BlockCipher {
    protected static final int BLOCK_SIZE = 8;
    private int[] workingKey = null;
    private boolean forEncryption;
    private static final int MASK = 65535;
    private static final int BASE = 65537;

    public IDEAEngine() {
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), 128));
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            byte[] byArray = ((KeyParameter)cipherParameters).getKey();
            this.workingKey = this.generateWorkingKey(bl, byArray);
            this.forEncryption = bl;
            CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), byArray.length * 8, cipherParameters, Utils.getPurpose(bl)));
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to IDEA init - " + cipherParameters.getClass().getName());
    }

    @Override
    public String getAlgorithmName() {
        return "IDEA";
    }

    @Override
    public int getBlockSize() {
        return 8;
    }

    @Override
    public int processBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        if (this.workingKey == null) {
            throw new IllegalStateException("IDEA engine not initialised");
        }
        if (n2 + 8 > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + 8 > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        this.ideaFunc(this.workingKey, byArray, n2, byArray2, n3);
        return 8;
    }

    @Override
    public void reset() {
    }

    private int bytesToWord(byte[] byArray, int n2) {
        return (byArray[n2] << 8 & 0xFF00) + (byArray[n2 + 1] & 0xFF);
    }

    private void wordToBytes(int n2, byte[] byArray, int n3) {
        byArray[n3] = (byte)(n2 >>> 8);
        byArray[n3 + 1] = (byte)n2;
    }

    private int mul(int n2, int n3) {
        int n4;
        n2 = n2 == 0 ? 65537 - n3 : (n3 == 0 ? 65537 - n2 : n3 - n2 + ((n3 = (n4 = n2 * n3) & 0xFFFF) < (n2 = n4 >>> 16) ? 1 : 0));
        return n2 & 0xFFFF;
    }

    private void ideaFunc(int[] nArray, byte[] byArray, int n2, byte[] byArray2, int n3) {
        int n4 = 0;
        int n5 = this.bytesToWord(byArray, n2);
        int n6 = this.bytesToWord(byArray, n2 + 2);
        int n7 = this.bytesToWord(byArray, n2 + 4);
        int n8 = this.bytesToWord(byArray, n2 + 6);
        for (int i2 = 0; i2 < 8; ++i2) {
            n5 = this.mul(n5, nArray[n4++]);
            n6 += nArray[n4++];
            n7 += nArray[n4++];
            n8 = this.mul(n8, nArray[n4++]);
            int n9 = n6 &= 0xFFFF;
            int n10 = n7 &= 0xFFFF;
            n7 ^= n5;
            n6 ^= n8;
            n7 = this.mul(n7, nArray[n4++]);
            n6 += n7;
            n6 &= 0xFFFF;
            n6 = this.mul(n6, nArray[n4++]);
            n7 += n6;
            n5 ^= n6;
            n8 ^= (n7 &= 0xFFFF);
            n6 ^= n10;
            n7 ^= n9;
        }
        this.wordToBytes(this.mul(n5, nArray[n4++]), byArray2, n3);
        this.wordToBytes(n7 + nArray[n4++], byArray2, n3 + 2);
        this.wordToBytes(n6 + nArray[n4++], byArray2, n3 + 4);
        this.wordToBytes(this.mul(n8, nArray[n4]), byArray2, n3 + 6);
    }

    private int[] expandKey(byte[] byArray) {
        int n2;
        int[] nArray = new int[52];
        if (byArray.length < 16) {
            byte[] byArray2 = new byte[16];
            System.arraycopy(byArray, 0, byArray2, byArray2.length - byArray.length, byArray.length);
            byArray = byArray2;
        }
        for (n2 = 0; n2 < 8; ++n2) {
            nArray[n2] = this.bytesToWord(byArray, n2 * 2);
        }
        for (n2 = 8; n2 < 52; ++n2) {
            nArray[n2] = (n2 & 7) < 6 ? ((nArray[n2 - 7] & 0x7F) << 9 | nArray[n2 - 6] >> 7) & 0xFFFF : ((n2 & 7) == 6 ? ((nArray[n2 - 7] & 0x7F) << 9 | nArray[n2 - 14] >> 7) & 0xFFFF : ((nArray[n2 - 15] & 0x7F) << 9 | nArray[n2 - 14] >> 7) & 0xFFFF);
        }
        return nArray;
    }

    private int mulInv(int n2) {
        if (n2 < 2) {
            return n2;
        }
        int n3 = 1;
        int n4 = 65537 / n2;
        for (int i2 = 65537 % n2; i2 != 1; i2 %= n2) {
            int n5 = n2 / i2;
            n3 = n3 + n4 * n5 & 0xFFFF;
            if ((n2 %= i2) == 1) {
                return n3;
            }
            n5 = i2 / n2;
            n4 = n4 + n3 * n5 & 0xFFFF;
        }
        return 1 - n4 & 0xFFFF;
    }

    int addInv(int n2) {
        return 0 - n2 & 0xFFFF;
    }

    private int[] invertKey(int[] nArray) {
        int n2 = 52;
        int[] nArray2 = new int[52];
        int n3 = 0;
        int n4 = this.mulInv(nArray[n3++]);
        int n5 = this.addInv(nArray[n3++]);
        int n6 = this.addInv(nArray[n3++]);
        int n7 = this.mulInv(nArray[n3++]);
        nArray2[--n2] = n7;
        nArray2[--n2] = n6;
        nArray2[--n2] = n5;
        nArray2[--n2] = n4;
        for (int i2 = 1; i2 < 8; ++i2) {
            n4 = nArray[n3++];
            n5 = nArray[n3++];
            nArray2[--n2] = n5;
            nArray2[--n2] = n4;
            n4 = this.mulInv(nArray[n3++]);
            n5 = this.addInv(nArray[n3++]);
            n6 = this.addInv(nArray[n3++]);
            n7 = this.mulInv(nArray[n3++]);
            nArray2[--n2] = n7;
            nArray2[--n2] = n5;
            nArray2[--n2] = n6;
            nArray2[--n2] = n4;
        }
        n4 = nArray[n3++];
        n5 = nArray[n3++];
        nArray2[--n2] = n5;
        nArray2[--n2] = n4;
        n4 = this.mulInv(nArray[n3++]);
        n5 = this.addInv(nArray[n3++]);
        n6 = this.addInv(nArray[n3++]);
        n7 = this.mulInv(nArray[n3]);
        nArray2[--n2] = n7;
        nArray2[--n2] = n6;
        nArray2[--n2] = n5;
        nArray2[--n2] = n4;
        return nArray2;
    }

    private int[] generateWorkingKey(boolean bl, byte[] byArray) {
        if (bl) {
            return this.expandKey(byArray);
        }
        return this.invertKey(this.expandKey(byArray));
    }
}

