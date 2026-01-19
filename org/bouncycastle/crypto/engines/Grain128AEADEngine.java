/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import java.io.ByteArrayOutputStream;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.engines.AEADBaseEngine;
import org.bouncycastle.util.Pack;

public class Grain128AEADEngine
extends AEADBaseEngine {
    private static final int STATE_SIZE = 4;
    private byte[] workingKey;
    private byte[] workingIV;
    private int[] lfsr;
    private int[] nfsr;
    private int[] authAcc;
    private int[] authSr;
    private boolean initialised = false;
    private boolean aadFinished = false;
    private final ErasableOutputStream aadData = new ErasableOutputStream();

    public Grain128AEADEngine() {
        this.algorithmName = "Grain-128AEAD";
        this.KEY_SIZE = 16;
        this.IV_SIZE = 12;
        this.MAC_SIZE = 8;
    }

    @Override
    protected void init(byte[] byArray, byte[] byArray2) throws IllegalArgumentException {
        this.workingIV = new byte[16];
        this.workingKey = byArray;
        this.lfsr = new int[4];
        this.nfsr = new int[4];
        this.authAcc = new int[2];
        this.authSr = new int[2];
        System.arraycopy(byArray2, 0, this.workingIV, 0, this.IV_SIZE);
        this.reset();
    }

    private void initGrain() {
        int n2;
        int n3;
        for (n3 = 0; n3 < 320; ++n3) {
            n2 = this.getOutput();
            this.nfsr = this.shift(this.nfsr, (this.getOutputNFSR() ^ this.lfsr[0] ^ n2) & 1);
            this.lfsr = this.shift(this.lfsr, (this.getOutputLFSR() ^ n2) & 1);
        }
        for (n3 = 0; n3 < 8; ++n3) {
            for (n2 = 0; n2 < 8; ++n2) {
                int n4 = this.getOutput();
                this.nfsr = this.shift(this.nfsr, (this.getOutputNFSR() ^ this.lfsr[0] ^ n4 ^ this.workingKey[n3] >> n2) & 1);
                this.lfsr = this.shift(this.lfsr, (this.getOutputLFSR() ^ n4 ^ this.workingKey[n3 + 8] >> n2) & 1);
            }
        }
        this.initGrain(this.authAcc);
        this.initGrain(this.authSr);
        this.initialised = true;
    }

    private void initGrain(int[] nArray) {
        for (int i2 = 0; i2 < 2; ++i2) {
            for (int i3 = 0; i3 < 32; ++i3) {
                int n2 = this.getOutput();
                this.nfsr = this.shift(this.nfsr, (this.getOutputNFSR() ^ this.lfsr[0]) & 1);
                this.lfsr = this.shift(this.lfsr, this.getOutputLFSR() & 1);
                int n3 = i2;
                nArray[n3] = nArray[n3] | n2 << i3;
            }
        }
    }

    private int getOutputNFSR() {
        int n2 = this.nfsr[0];
        int n3 = this.nfsr[0] >>> 3;
        int n4 = this.nfsr[0] >>> 11;
        int n5 = this.nfsr[0] >>> 13;
        int n6 = this.nfsr[0] >>> 17;
        int n7 = this.nfsr[0] >>> 18;
        int n8 = this.nfsr[0] >>> 22;
        int n9 = this.nfsr[0] >>> 24;
        int n10 = this.nfsr[0] >>> 25;
        int n11 = this.nfsr[0] >>> 26;
        int n12 = this.nfsr[0] >>> 27;
        int n13 = this.nfsr[1] >>> 8;
        int n14 = this.nfsr[1] >>> 16;
        int n15 = this.nfsr[1] >>> 24;
        int n16 = this.nfsr[1] >>> 27;
        int n17 = this.nfsr[1] >>> 29;
        int n18 = this.nfsr[2] >>> 1;
        int n19 = this.nfsr[2] >>> 3;
        int n20 = this.nfsr[2] >>> 4;
        int n21 = this.nfsr[2] >>> 6;
        int n22 = this.nfsr[2] >>> 14;
        int n23 = this.nfsr[2] >>> 18;
        int n24 = this.nfsr[2] >>> 20;
        int n25 = this.nfsr[2] >>> 24;
        int n26 = this.nfsr[2] >>> 27;
        int n27 = this.nfsr[2] >>> 28;
        int n28 = this.nfsr[2] >>> 29;
        int n29 = this.nfsr[2] >>> 31;
        int n30 = this.nfsr[3];
        return (n2 ^ n11 ^ n15 ^ n26 ^ n30 ^ n3 & n19 ^ n4 & n5 ^ n6 & n7 ^ n12 & n16 ^ n13 & n14 ^ n17 & n18 ^ n20 & n24 ^ n8 & n9 & n10 ^ n21 & n22 & n23 ^ n25 & n27 & n28 & n29) & 1;
    }

    private int getOutputLFSR() {
        int n2 = this.lfsr[0];
        int n3 = this.lfsr[0] >>> 7;
        int n4 = this.lfsr[1] >>> 6;
        int n5 = this.lfsr[2] >>> 6;
        int n6 = this.lfsr[2] >>> 17;
        int n7 = this.lfsr[3];
        return (n2 ^ n3 ^ n4 ^ n5 ^ n6 ^ n7) & 1;
    }

    private int getOutput() {
        int n2 = this.nfsr[0] >>> 2;
        int n3 = this.nfsr[0] >>> 12;
        int n4 = this.nfsr[0] >>> 15;
        int n5 = this.nfsr[1] >>> 4;
        int n6 = this.nfsr[1] >>> 13;
        int n7 = this.nfsr[2];
        int n8 = this.nfsr[2] >>> 9;
        int n9 = this.nfsr[2] >>> 25;
        int n10 = this.nfsr[2] >>> 31;
        int n11 = this.lfsr[0] >>> 8;
        int n12 = this.lfsr[0] >>> 13;
        int n13 = this.lfsr[0] >>> 20;
        int n14 = this.lfsr[1] >>> 10;
        int n15 = this.lfsr[1] >>> 28;
        int n16 = this.lfsr[2] >>> 15;
        int n17 = this.lfsr[2] >>> 29;
        int n18 = this.lfsr[2] >>> 30;
        return (n3 & n11 ^ n12 & n13 ^ n10 & n14 ^ n15 & n16 ^ n3 & n10 & n18 ^ n17 ^ n2 ^ n4 ^ n5 ^ n6 ^ n7 ^ n8 ^ n9) & 1;
    }

    private int[] shift(int[] nArray, int n2) {
        nArray[0] = nArray[0] >>> 1 | nArray[1] << 31;
        nArray[1] = nArray[1] >>> 1 | nArray[2] << 31;
        nArray[2] = nArray[2] >>> 1 | nArray[3] << 31;
        nArray[3] = nArray[3] >>> 1 | n2 << 31;
        return nArray;
    }

    private void setKey(byte[] byArray, byte[] byArray2) {
        byArray2[12] = -1;
        byArray2[13] = -1;
        byArray2[14] = -1;
        byArray2[15] = 127;
        this.workingKey = byArray;
        this.workingIV = byArray2;
        Pack.littleEndianToInt(this.workingKey, 0, this.nfsr);
        Pack.littleEndianToInt(this.workingIV, 0, this.lfsr);
    }

    @Override
    public int processBytes(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws DataLengthException {
        if (!this.initialised) {
            throw new IllegalStateException(this.getAlgorithmName() + " not initialised");
        }
        if (!this.aadFinished) {
            this.doProcessAADBytes(this.aadData.getBuf(), this.aadData.size());
            this.aadFinished = true;
        }
        if (n2 + n3 > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n4 + n3 > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        this.getKeyStream(byArray, n2, n3, byArray2, n4);
        return n3;
    }

    @Override
    protected void reset(boolean bl) {
        this.aadData.reset();
        this.aadFinished = false;
        this.setKey(this.workingKey, this.workingIV);
        this.initGrain();
        super.reset(bl);
    }

    private void getKeyStream(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) {
        for (int i2 = 0; i2 < n3; ++i2) {
            byte by = 0;
            byte by2 = byArray[n2 + i2];
            for (int i3 = 0; i3 < 8; ++i3) {
                int n5 = this.getOutput();
                this.nfsr = this.shift(this.nfsr, (this.getOutputNFSR() ^ this.lfsr[0]) & 1);
                this.lfsr = this.shift(this.lfsr, this.getOutputLFSR() & 1);
                int n6 = by2 >> i3 & 1;
                by = (byte)(by | (n6 ^ n5) << i3);
                this.updateInternalState(n6);
            }
            byArray2[n4 + i2] = by;
        }
    }

    private void updateInternalState(int n2) {
        int n3 = -n2;
        this.authAcc[0] = this.authAcc[0] ^ this.authSr[0] & n3;
        this.authAcc[1] = this.authAcc[1] ^ this.authSr[1] & n3;
        this.authShift(this.getOutput());
        this.nfsr = this.shift(this.nfsr, (this.getOutputNFSR() ^ this.lfsr[0]) & 1);
        this.lfsr = this.shift(this.lfsr, this.getOutputLFSR() & 1);
    }

    @Override
    public void processAADByte(byte by) {
        if (this.aadFinished) {
            throw new IllegalStateException("associated data must be added before plaintext/ciphertext");
        }
        this.aadData.write(by);
    }

    @Override
    public void processAADBytes(byte[] byArray, int n2, int n3) {
        if (this.aadFinished) {
            throw new IllegalStateException("associated data must be added before plaintext/ciphertext");
        }
        this.aadData.write(byArray, n2, n3);
    }

    private void doProcessAADBytes(byte[] byArray, int n2) {
        int n3;
        int n4;
        int n5;
        byte[] byArray2;
        if (n2 < 128) {
            byArray2 = new byte[1 + n2];
            byArray2[0] = (byte)n2;
            n5 = 0;
        } else {
            n5 = Grain128AEADEngine.len_length(n2);
            byArray2 = new byte[1 + n5 + n2];
            byArray2[0] = (byte)(0x80 | n5);
            n4 = n2;
            for (n3 = 0; n3 < n5; ++n3) {
                byArray2[1 + n3] = (byte)n4;
                n4 >>>= 8;
            }
        }
        for (n4 = 0; n4 < n2; ++n4) {
            byArray2[1 + n5 + n4] = byArray[n4];
        }
        for (n4 = 0; n4 < byArray2.length; ++n4) {
            n3 = byArray2[n4];
            for (int i2 = 0; i2 < 8; ++i2) {
                this.nfsr = this.shift(this.nfsr, (this.getOutputNFSR() ^ this.lfsr[0]) & 1);
                this.lfsr = this.shift(this.lfsr, this.getOutputLFSR() & 1);
                int n6 = n3 >> i2 & 1;
                this.updateInternalState(n6);
            }
        }
    }

    private void accumulate() {
        this.authAcc[0] = this.authAcc[0] ^ this.authSr[0];
        this.authAcc[1] = this.authAcc[1] ^ this.authSr[1];
    }

    private void authShift(int n2) {
        this.authSr[0] = this.authSr[0] >>> 1 | this.authSr[1] << 31;
        this.authSr[1] = this.authSr[1] >>> 1 | n2 << 31;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) throws IllegalStateException, InvalidCipherTextException {
        if (!this.aadFinished) {
            this.doProcessAADBytes(this.aadData.getBuf(), this.aadData.size());
            this.aadFinished = true;
        }
        this.accumulate();
        this.mac = Pack.intToLittleEndian(this.authAcc);
        System.arraycopy(this.mac, 0, byArray, n2, this.mac.length);
        this.reset(false);
        return this.mac.length;
    }

    @Override
    public int getUpdateOutputSize(int n2) {
        return n2;
    }

    @Override
    public int getOutputSize(int n2) {
        return n2 + 8;
    }

    private static int len_length(int n2) {
        if ((n2 & 0xFF) == n2) {
            return 1;
        }
        if ((n2 & 0xFFFF) == n2) {
            return 2;
        }
        if ((n2 & 0xFFFFFF) == n2) {
            return 3;
        }
        return 4;
    }

    private static final class ErasableOutputStream
    extends ByteArrayOutputStream {
        public byte[] getBuf() {
            return this.buf;
        }
    }
}

