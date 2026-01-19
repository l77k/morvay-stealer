/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicePurpose;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.constraints.DefaultServiceProperties;
import org.bouncycastle.crypto.params.KeyParameter;

public abstract class SerpentEngineBase
implements BlockCipher {
    protected static final int BLOCK_SIZE = 16;
    static final int ROUNDS = 32;
    static final int PHI = -1640531527;
    protected boolean encrypting;
    protected int[] wKey;
    protected int keyBits;

    SerpentEngineBase() {
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), 256));
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.encrypting = bl;
            byte[] byArray = ((KeyParameter)cipherParameters).getKey();
            this.wKey = this.makeWorkingKey(byArray);
            CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), byArray.length * 8, cipherParameters, this.getPurpose()));
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to " + this.getAlgorithmName() + " init - " + cipherParameters.getClass().getName());
    }

    @Override
    public String getAlgorithmName() {
        return "Serpent";
    }

    @Override
    public int getBlockSize() {
        return 16;
    }

    @Override
    public final int processBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        if (this.wKey == null) {
            throw new IllegalStateException(this.getAlgorithmName() + " not initialised");
        }
        if (n2 + 16 > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + 16 > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this.encrypting) {
            this.encryptBlock(byArray, n2, byArray2, n3);
        } else {
            this.decryptBlock(byArray, n2, byArray2, n3);
        }
        return 16;
    }

    @Override
    public void reset() {
    }

    protected static int rotateLeft(int n2, int n3) {
        return n2 << n3 | n2 >>> -n3;
    }

    protected static int rotateRight(int n2, int n3) {
        return n2 >>> n3 | n2 << -n3;
    }

    protected final void sb0(int[] nArray, int n2, int n3, int n4, int n5) {
        int n6 = n2 ^ n5;
        int n7 = n4 ^ n6;
        int n8 = n3 ^ n7;
        nArray[3] = n2 & n5 ^ n8;
        int n9 = n2 ^ n3 & n6;
        nArray[2] = n8 ^ (n4 | n9);
        int n10 = nArray[3] & (n7 ^ n9);
        nArray[1] = ~n7 ^ n10;
        nArray[0] = n10 ^ ~n9;
    }

    protected final void ib0(int[] nArray, int n2, int n3, int n4, int n5) {
        int n6 = ~n2;
        int n7 = n2 ^ n3;
        int n8 = n5 ^ (n6 | n7);
        int n9 = n4 ^ n8;
        nArray[2] = n7 ^ n9;
        int n10 = n6 ^ n5 & n7;
        nArray[1] = n8 ^ nArray[2] & n10;
        nArray[3] = n2 & n8 ^ (n9 | nArray[1]);
        nArray[0] = nArray[3] ^ (n9 ^ n10);
    }

    protected final void sb1(int[] nArray, int n2, int n3, int n4, int n5) {
        int n6 = n3 ^ ~n2;
        int n7 = n4 ^ (n2 | n6);
        nArray[2] = n5 ^ n7;
        int n8 = n3 ^ (n5 | n6);
        int n9 = n6 ^ nArray[2];
        nArray[3] = n9 ^ n7 & n8;
        int n10 = n7 ^ n8;
        nArray[1] = nArray[3] ^ n10;
        nArray[0] = n7 ^ n9 & n10;
    }

    protected final void ib1(int[] nArray, int n2, int n3, int n4, int n5) {
        int n6 = n3 ^ n5;
        int n7 = n2 ^ n3 & n6;
        int n8 = n6 ^ n7;
        nArray[3] = n4 ^ n8;
        int n9 = n3 ^ n6 & n7;
        int n10 = nArray[3] | n9;
        nArray[1] = n7 ^ n10;
        int n11 = ~nArray[1];
        int n12 = nArray[3] ^ n9;
        nArray[0] = n11 ^ n12;
        nArray[2] = n8 ^ (n11 | n12);
    }

    protected final void sb2(int[] nArray, int n2, int n3, int n4, int n5) {
        int n6 = ~n2;
        int n7 = n3 ^ n5;
        int n8 = n4 & n6;
        nArray[0] = n7 ^ n8;
        int n9 = n4 ^ n6;
        int n10 = n4 ^ nArray[0];
        int n11 = n3 & n10;
        nArray[3] = n9 ^ n11;
        nArray[2] = n2 ^ (n5 | n11) & (nArray[0] | n9);
        nArray[1] = n7 ^ nArray[3] ^ (nArray[2] ^ (n5 | n6));
    }

    protected final void ib2(int[] nArray, int n2, int n3, int n4, int n5) {
        int n6 = n3 ^ n5;
        int n7 = ~n6;
        int n8 = n2 ^ n4;
        int n9 = n4 ^ n6;
        int n10 = n3 & n9;
        nArray[0] = n8 ^ n10;
        int n11 = n2 | n7;
        int n12 = n5 ^ n11;
        int n13 = n8 | n12;
        nArray[3] = n6 ^ n13;
        int n14 = ~n9;
        int n15 = nArray[0] | nArray[3];
        nArray[1] = n14 ^ n15;
        nArray[2] = n5 & n14 ^ (n8 ^ n15);
    }

    protected final void sb3(int[] nArray, int n2, int n3, int n4, int n5) {
        int n6 = n2 ^ n3;
        int n7 = n2 & n4;
        int n8 = n2 | n5;
        int n9 = n4 ^ n5;
        int n10 = n6 & n8;
        int n11 = n7 | n10;
        nArray[2] = n9 ^ n11;
        int n12 = n3 ^ n8;
        int n13 = n11 ^ n12;
        int n14 = n9 & n13;
        nArray[0] = n6 ^ n14;
        int n15 = nArray[2] & nArray[0];
        nArray[1] = n13 ^ n15;
        nArray[3] = (n3 | n5) ^ (n9 ^ n15);
    }

    protected final void ib3(int[] nArray, int n2, int n3, int n4, int n5) {
        int n6 = n2 | n3;
        int n7 = n3 ^ n4;
        int n8 = n3 & n7;
        int n9 = n2 ^ n8;
        int n10 = n4 ^ n9;
        int n11 = n5 | n9;
        nArray[0] = n7 ^ n11;
        int n12 = n7 | n11;
        int n13 = n5 ^ n12;
        nArray[2] = n10 ^ n13;
        int n14 = n6 ^ n13;
        int n15 = nArray[0] & n14;
        nArray[3] = n9 ^ n15;
        nArray[1] = nArray[3] ^ (nArray[0] ^ n14);
    }

    protected final void sb4(int[] nArray, int n2, int n3, int n4, int n5) {
        int n6 = n2 ^ n5;
        int n7 = n5 & n6;
        int n8 = n4 ^ n7;
        int n9 = n3 | n8;
        nArray[3] = n6 ^ n9;
        int n10 = ~n3;
        int n11 = n6 | n10;
        nArray[0] = n8 ^ n11;
        int n12 = n2 & nArray[0];
        int n13 = n6 ^ n10;
        int n14 = n9 & n13;
        nArray[2] = n12 ^ n14;
        nArray[1] = n2 ^ n8 ^ n13 & nArray[2];
    }

    protected final void ib4(int[] nArray, int n2, int n3, int n4, int n5) {
        int n6 = n4 | n5;
        int n7 = n2 & n6;
        int n8 = n3 ^ n7;
        int n9 = n2 & n8;
        int n10 = n4 ^ n9;
        nArray[1] = n5 ^ n10;
        int n11 = ~n2;
        int n12 = n10 & nArray[1];
        nArray[3] = n8 ^ n12;
        int n13 = nArray[1] | n11;
        int n14 = n5 ^ n13;
        nArray[0] = nArray[3] ^ n14;
        nArray[2] = n8 & n14 ^ (nArray[1] ^ n11);
    }

    protected final void sb5(int[] nArray, int n2, int n3, int n4, int n5) {
        int n6 = ~n2;
        int n7 = n2 ^ n3;
        int n8 = n2 ^ n5;
        int n9 = n4 ^ n6;
        int n10 = n7 | n8;
        nArray[0] = n9 ^ n10;
        int n11 = n5 & nArray[0];
        int n12 = n7 ^ nArray[0];
        nArray[1] = n11 ^ n12;
        int n13 = n6 | nArray[0];
        int n14 = n7 | n11;
        int n15 = n8 ^ n13;
        nArray[2] = n14 ^ n15;
        nArray[3] = n3 ^ n11 ^ nArray[1] & n15;
    }

    protected final void ib5(int[] nArray, int n2, int n3, int n4, int n5) {
        int n6 = ~n4;
        int n7 = n3 & n6;
        int n8 = n5 ^ n7;
        int n9 = n2 & n8;
        int n10 = n3 ^ n6;
        nArray[3] = n9 ^ n10;
        int n11 = n3 | nArray[3];
        int n12 = n2 & n11;
        nArray[1] = n8 ^ n12;
        int n13 = n2 | n5;
        int n14 = n6 ^ n11;
        nArray[0] = n13 ^ n14;
        nArray[2] = n3 & n13 ^ (n9 | n2 ^ n4);
    }

    protected final void sb6(int[] nArray, int n2, int n3, int n4, int n5) {
        int n6 = ~n2;
        int n7 = n2 ^ n5;
        int n8 = n3 ^ n7;
        int n9 = n6 | n7;
        int n10 = n4 ^ n9;
        nArray[1] = n3 ^ n10;
        int n11 = n7 | nArray[1];
        int n12 = n5 ^ n11;
        int n13 = n10 & n12;
        nArray[2] = n8 ^ n13;
        int n14 = n10 ^ n12;
        nArray[0] = nArray[2] ^ n14;
        nArray[3] = ~n10 ^ n8 & n14;
    }

    protected final void ib6(int[] nArray, int n2, int n3, int n4, int n5) {
        int n6 = ~n2;
        int n7 = n2 ^ n3;
        int n8 = n4 ^ n7;
        int n9 = n4 | n6;
        int n10 = n5 ^ n9;
        nArray[1] = n8 ^ n10;
        int n11 = n8 & n10;
        int n12 = n7 ^ n11;
        int n13 = n3 | n12;
        nArray[3] = n10 ^ n13;
        int n14 = n3 | nArray[3];
        nArray[0] = n12 ^ n14;
        nArray[2] = n5 & n6 ^ (n8 ^ n14);
    }

    protected final void sb7(int[] nArray, int n2, int n3, int n4, int n5) {
        int n6 = n3 ^ n4;
        int n7 = n4 & n6;
        int n8 = n5 ^ n7;
        int n9 = n2 ^ n8;
        int n10 = n5 | n6;
        int n11 = n9 & n10;
        nArray[1] = n3 ^ n11;
        int n12 = n8 | nArray[1];
        int n13 = n2 & n9;
        nArray[3] = n6 ^ n13;
        int n14 = n9 ^ n12;
        int n15 = nArray[3] & n14;
        nArray[2] = n8 ^ n15;
        nArray[0] = ~n14 ^ nArray[3] & nArray[2];
    }

    protected final void ib7(int[] nArray, int n2, int n3, int n4, int n5) {
        int n6 = n4 | n2 & n3;
        int n7 = n5 & (n2 | n3);
        nArray[3] = n6 ^ n7;
        int n8 = ~n5;
        int n9 = n3 ^ n7;
        int n10 = n9 | nArray[3] ^ n8;
        nArray[1] = n2 ^ n10;
        nArray[0] = n4 ^ n9 ^ (n5 | nArray[1]);
        nArray[2] = n6 ^ nArray[1] ^ (nArray[0] ^ n2 & nArray[3]);
    }

    protected final void LT(int[] nArray) {
        int n2 = SerpentEngineBase.rotateLeft(nArray[0], 13);
        int n3 = SerpentEngineBase.rotateLeft(nArray[2], 3);
        int n4 = nArray[1] ^ n2 ^ n3;
        int n5 = nArray[3] ^ n3 ^ n2 << 3;
        nArray[1] = SerpentEngineBase.rotateLeft(n4, 1);
        nArray[3] = SerpentEngineBase.rotateLeft(n5, 7);
        nArray[0] = SerpentEngineBase.rotateLeft(n2 ^ nArray[1] ^ nArray[3], 5);
        nArray[2] = SerpentEngineBase.rotateLeft(n3 ^ nArray[3] ^ nArray[1] << 7, 22);
    }

    protected final void inverseLT(int[] nArray) {
        int n2 = SerpentEngineBase.rotateRight(nArray[2], 22) ^ nArray[3] ^ nArray[1] << 7;
        int n3 = SerpentEngineBase.rotateRight(nArray[0], 5) ^ nArray[1] ^ nArray[3];
        int n4 = SerpentEngineBase.rotateRight(nArray[3], 7);
        int n5 = SerpentEngineBase.rotateRight(nArray[1], 1);
        nArray[3] = n4 ^ n2 ^ n3 << 3;
        nArray[1] = n5 ^ n3 ^ n2;
        nArray[2] = SerpentEngineBase.rotateRight(n2, 3);
        nArray[0] = SerpentEngineBase.rotateRight(n3, 13);
    }

    protected abstract int[] makeWorkingKey(byte[] var1);

    protected abstract void encryptBlock(byte[] var1, int var2, byte[] var3, int var4);

    protected abstract void decryptBlock(byte[] var1, int var2, byte[] var3, int var4);

    private CryptoServicePurpose getPurpose() {
        if (this.wKey == null) {
            return CryptoServicePurpose.ANY;
        }
        return this.encrypting ? CryptoServicePurpose.ENCRYPTION : CryptoServicePurpose.DECRYPTION;
    }
}

