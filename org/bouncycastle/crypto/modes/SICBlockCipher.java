/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.StreamBlockCipher;
import org.bouncycastle.crypto.modes.CTRModeCipher;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class SICBlockCipher
extends StreamBlockCipher
implements CTRModeCipher {
    private final BlockCipher cipher;
    private final int blockSize;
    private byte[] IV;
    private byte[] counter;
    private byte[] counterOut;
    private int byteCount;

    public static CTRModeCipher newInstance(BlockCipher blockCipher) {
        return new SICBlockCipher(blockCipher);
    }

    public SICBlockCipher(BlockCipher blockCipher) {
        super(blockCipher);
        this.cipher = blockCipher;
        this.blockSize = this.cipher.getBlockSize();
        this.IV = new byte[this.blockSize];
        this.counter = new byte[this.blockSize];
        this.counterOut = new byte[this.blockSize];
        this.byteCount = 0;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) throws IllegalArgumentException {
        if (cipherParameters instanceof ParametersWithIV) {
            int n2;
            ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            this.IV = Arrays.clone(parametersWithIV.getIV());
            if (this.blockSize < this.IV.length) {
                throw new IllegalArgumentException("CTR/SIC mode requires IV no greater than: " + this.blockSize + " bytes.");
            }
            int n3 = n2 = 8 > this.blockSize / 2 ? this.blockSize / 2 : 8;
            if (this.blockSize - this.IV.length > n2) {
                throw new IllegalArgumentException("CTR/SIC mode requires IV of at least: " + (this.blockSize - n2) + " bytes.");
            }
            if (parametersWithIV.getParameters() != null) {
                this.cipher.init(true, parametersWithIV.getParameters());
            }
        } else {
            throw new IllegalArgumentException("CTR/SIC mode requires ParametersWithIV");
        }
        this.reset();
    }

    @Override
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/SIC";
    }

    @Override
    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }

    @Override
    public int processBlock(byte[] byArray, int n2, byte[] byArray2, int n3) throws DataLengthException, IllegalStateException {
        if (this.byteCount != 0) {
            this.processBytes(byArray, n2, this.blockSize, byArray2, n3);
            return this.blockSize;
        }
        if (n2 + this.blockSize > byArray.length) {
            throw new DataLengthException("input buffer too small");
        }
        if (n3 + this.blockSize > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        this.cipher.processBlock(this.counter, 0, this.counterOut, 0);
        for (int i2 = 0; i2 < this.blockSize; ++i2) {
            byArray2[n3 + i2] = (byte)(byArray[n2 + i2] ^ this.counterOut[i2]);
        }
        this.incrementCounter();
        return this.blockSize;
    }

    @Override
    public int processBytes(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws DataLengthException {
        if (n2 + n3 > byArray.length) {
            throw new DataLengthException("input buffer too small");
        }
        if (n4 + n3 > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        for (int i2 = 0; i2 < n3; ++i2) {
            byte by;
            if (this.byteCount == 0) {
                this.checkLastIncrement();
                this.cipher.processBlock(this.counter, 0, this.counterOut, 0);
                by = (byte)(byArray[n2 + i2] ^ this.counterOut[this.byteCount++]);
            } else {
                by = (byte)(byArray[n2 + i2] ^ this.counterOut[this.byteCount++]);
                if (this.byteCount == this.counter.length) {
                    this.byteCount = 0;
                    this.incrementCounter();
                }
            }
            byArray2[n4 + i2] = by;
        }
        return n3;
    }

    @Override
    protected byte calculateByte(byte by) throws DataLengthException, IllegalStateException {
        if (this.byteCount == 0) {
            this.checkLastIncrement();
            this.cipher.processBlock(this.counter, 0, this.counterOut, 0);
            return (byte)(this.counterOut[this.byteCount++] ^ by);
        }
        byte by2 = (byte)(this.counterOut[this.byteCount++] ^ by);
        if (this.byteCount == this.counter.length) {
            this.byteCount = 0;
            this.incrementCounter();
        }
        return by2;
    }

    private void checkCounter() {
        if (this.IV.length < this.blockSize) {
            for (int i2 = this.IV.length - 1; i2 >= 0; --i2) {
                if (this.counter[i2] == this.IV[i2]) continue;
                throw new IllegalStateException("Counter in CTR/SIC mode out of range.");
            }
        }
    }

    private void checkLastIncrement() {
        if (this.IV.length < this.blockSize && this.counter[this.IV.length - 1] != this.IV[this.IV.length - 1]) {
            throw new IllegalStateException("Counter in CTR/SIC mode out of range.");
        }
    }

    private void incrementCounter() {
        int n2 = this.counter.length;
        while (--n2 >= 0) {
            int n3 = n2;
            this.counter[n3] = (byte)(this.counter[n3] + 1);
            if (this.counter[n3] == 0) continue;
            break;
        }
    }

    private void incrementCounterAt(int n2) {
        int n3 = this.counter.length - n2;
        while (--n3 >= 0) {
            int n4 = n3;
            this.counter[n4] = (byte)(this.counter[n4] + 1);
            if (this.counter[n4] == 0) continue;
            break;
        }
    }

    private void incrementCounter(int n2) {
        byte by = this.counter[this.counter.length - 1];
        int n3 = this.counter.length - 1;
        this.counter[n3] = (byte)(this.counter[n3] + (byte)n2);
        if ((by & 0xFF) + n2 > 255) {
            this.incrementCounterAt(1);
        }
    }

    private void decrementCounterAt(int n2) {
        int n3 = this.counter.length - n2;
        while (--n3 >= 0) {
            int n4 = n3;
            this.counter[n4] = (byte)(this.counter[n4] - 1);
            if (this.counter[n4] == -1) continue;
            return;
        }
    }

    private void adjustCounter(long l2) {
        if (l2 >= 0L) {
            long l3 = (l2 + (long)this.byteCount) / (long)this.blockSize;
            long l4 = l3;
            if (l4 > 255L) {
                for (int i2 = 5; i2 >= 1; --i2) {
                    long l5 = 1L << 8 * i2;
                    while (l4 >= l5) {
                        this.incrementCounterAt(i2);
                        l4 -= l5;
                    }
                }
            }
            this.incrementCounter((int)l4);
            this.byteCount = (int)(l2 + (long)this.byteCount - (long)this.blockSize * l3);
        } else {
            long l6 = (-l2 - (long)this.byteCount) / (long)this.blockSize;
            long l7 = l6;
            if (l7 > 255L) {
                for (int i3 = 5; i3 >= 1; --i3) {
                    long l8 = 1L << 8 * i3;
                    while (l7 > l8) {
                        this.decrementCounterAt(i3);
                        l7 -= l8;
                    }
                }
            }
            for (long i4 = 0L; i4 != l7; ++i4) {
                this.decrementCounterAt(0);
            }
            int n2 = (int)((long)this.byteCount + l2 + (long)this.blockSize * l6);
            if (n2 >= 0) {
                this.byteCount = 0;
            } else {
                this.decrementCounterAt(0);
                this.byteCount = this.blockSize + n2;
            }
        }
    }

    @Override
    public void reset() {
        Arrays.fill(this.counter, (byte)0);
        System.arraycopy(this.IV, 0, this.counter, 0, this.IV.length);
        this.cipher.reset();
        this.byteCount = 0;
    }

    @Override
    public long skip(long l2) {
        this.adjustCounter(l2);
        this.checkCounter();
        this.cipher.processBlock(this.counter, 0, this.counterOut, 0);
        return l2;
    }

    @Override
    public long seekTo(long l2) {
        this.reset();
        return this.skip(l2);
    }

    @Override
    public long getPosition() {
        byte[] byArray = new byte[this.counter.length];
        System.arraycopy(this.counter, 0, byArray, 0, byArray.length);
        for (int i2 = byArray.length - 1; i2 >= 1; --i2) {
            int n2 = i2 < this.IV.length ? (byArray[i2] & 0xFF) - (this.IV[i2] & 0xFF) : byArray[i2] & 0xFF;
            if (n2 < 0) {
                int n3 = i2 - 1;
                byArray[n3] = (byte)(byArray[n3] - 1);
                n2 += 256;
            }
            byArray[i2] = (byte)n2;
        }
        return Pack.bigEndianToLong(byArray, byArray.length - 8) * (long)this.blockSize + (long)this.byteCount;
    }
}

