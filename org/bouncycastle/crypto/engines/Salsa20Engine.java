/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.MaxBytesExceededException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.SkippingStreamCipher;
import org.bouncycastle.crypto.constraints.DefaultServiceProperties;
import org.bouncycastle.crypto.engines.Utils;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Pack;
import org.bouncycastle.util.Strings;

public class Salsa20Engine
implements SkippingStreamCipher {
    public static final int DEFAULT_ROUNDS = 20;
    private static final int STATE_SIZE = 16;
    private static final int[] TAU_SIGMA = Pack.littleEndianToInt(Strings.toByteArray("expand 16-byte kexpand 32-byte k"), 0, 8);
    protected static final byte[] sigma = Strings.toByteArray("expand 32-byte k");
    protected static final byte[] tau = Strings.toByteArray("expand 16-byte k");
    protected int rounds;
    private int index = 0;
    protected int[] engineState = new int[16];
    protected int[] x = new int[16];
    private byte[] keyStream = new byte[64];
    private boolean initialised = false;
    private int cW0;
    private int cW1;
    private int cW2;

    protected void packTauOrSigma(int n2, int[] nArray, int n3) {
        int n4 = (n2 - 16) / 4;
        nArray[n3] = TAU_SIGMA[n4];
        nArray[n3 + 1] = TAU_SIGMA[n4 + 1];
        nArray[n3 + 2] = TAU_SIGMA[n4 + 2];
        nArray[n3 + 3] = TAU_SIGMA[n4 + 3];
    }

    public Salsa20Engine() {
        this(20);
    }

    public Salsa20Engine(int n2) {
        if (n2 <= 0 || (n2 & 1) != 0) {
            throw new IllegalArgumentException("'rounds' must be a positive, even number");
        }
        this.rounds = n2;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof ParametersWithIV)) {
            throw new IllegalArgumentException(this.getAlgorithmName() + " Init parameters must include an IV");
        }
        ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
        byte[] byArray = parametersWithIV.getIV();
        if (byArray == null || byArray.length != this.getNonceSize()) {
            throw new IllegalArgumentException(this.getAlgorithmName() + " requires exactly " + this.getNonceSize() + " bytes of IV");
        }
        CipherParameters cipherParameters2 = parametersWithIV.getParameters();
        if (cipherParameters2 == null) {
            if (!this.initialised) {
                throw new IllegalStateException(this.getAlgorithmName() + " KeyParameter can not be null for first initialisation");
            }
            this.setKey(null, byArray);
        } else if (cipherParameters2 instanceof KeyParameter) {
            byte[] byArray2 = ((KeyParameter)cipherParameters2).getKey();
            this.setKey(byArray2, byArray);
            CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), byArray2.length * 8, cipherParameters, Utils.getPurpose(bl)));
        } else {
            throw new IllegalArgumentException(this.getAlgorithmName() + " Init parameters must contain a KeyParameter (or null for re-init)");
        }
        this.reset();
        this.initialised = true;
    }

    protected int getNonceSize() {
        return 8;
    }

    @Override
    public String getAlgorithmName() {
        String string = "Salsa20";
        if (this.rounds != 20) {
            string = string + "/" + this.rounds;
        }
        return string;
    }

    @Override
    public byte returnByte(byte by) {
        if (this.limitExceeded()) {
            throw new MaxBytesExceededException("2^70 byte limit per IV; Change IV");
        }
        byte by2 = (byte)(this.keyStream[this.index] ^ by);
        this.index = this.index + 1 & 0x3F;
        if (this.index == 0) {
            this.advanceCounter();
            this.generateKeyStream(this.keyStream);
        }
        return by2;
    }

    protected void advanceCounter(long l2) {
        int n2 = (int)(l2 >>> 32);
        int n3 = (int)l2;
        if (n2 > 0) {
            this.engineState[9] = this.engineState[9] + n2;
        }
        int n4 = this.engineState[8];
        this.engineState[8] = this.engineState[8] + n3;
        if (n4 != 0 && this.engineState[8] < n4) {
            this.engineState[9] = this.engineState[9] + 1;
        }
    }

    protected void advanceCounter() {
        this.engineState[8] = this.engineState[8] + 1;
        if (this.engineState[8] == 0) {
            this.engineState[9] = this.engineState[9] + 1;
        }
    }

    protected void retreatCounter(long l2) {
        int n2 = (int)(l2 >>> 32);
        int n3 = (int)l2;
        if (n2 != 0) {
            if (((long)this.engineState[9] & 0xFFFFFFFFL) >= ((long)n2 & 0xFFFFFFFFL)) {
                this.engineState[9] = this.engineState[9] - n2;
            } else {
                throw new IllegalStateException("attempt to reduce counter past zero.");
            }
        }
        if (((long)this.engineState[8] & 0xFFFFFFFFL) >= ((long)n3 & 0xFFFFFFFFL)) {
            this.engineState[8] = this.engineState[8] - n3;
        } else if (this.engineState[9] != 0) {
            this.engineState[9] = this.engineState[9] - 1;
            this.engineState[8] = this.engineState[8] - n3;
        } else {
            throw new IllegalStateException("attempt to reduce counter past zero.");
        }
    }

    protected void retreatCounter() {
        if (this.engineState[8] == 0 && this.engineState[9] == 0) {
            throw new IllegalStateException("attempt to reduce counter past zero.");
        }
        this.engineState[8] = this.engineState[8] - 1;
        if (this.engineState[8] == -1) {
            this.engineState[9] = this.engineState[9] - 1;
        }
    }

    @Override
    public int processBytes(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) {
        if (!this.initialised) {
            throw new IllegalStateException(this.getAlgorithmName() + " not initialised");
        }
        if (n2 + n3 > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n4 + n3 > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this.limitExceeded(n3)) {
            throw new MaxBytesExceededException("2^70 byte limit per IV would be exceeded; Change IV");
        }
        for (int i2 = 0; i2 < n3; ++i2) {
            byArray2[i2 + n4] = (byte)(this.keyStream[this.index] ^ byArray[i2 + n2]);
            this.index = this.index + 1 & 0x3F;
            if (this.index != 0) continue;
            this.advanceCounter();
            this.generateKeyStream(this.keyStream);
        }
        return n3;
    }

    @Override
    public long skip(long l2) {
        if (l2 >= 0L) {
            long l3 = l2;
            if (l3 >= 64L) {
                long l4 = l3 / 64L;
                this.advanceCounter(l4);
                l3 -= l4 * 64L;
            }
            int n2 = this.index;
            this.index = this.index + (int)l3 & 0x3F;
            if (this.index < n2) {
                this.advanceCounter();
            }
        } else {
            long l5;
            long l6 = -l2;
            if (l6 >= 64L) {
                l5 = l6 / 64L;
                this.retreatCounter(l5);
                l6 -= l5 * 64L;
            }
            for (l5 = 0L; l5 < l6; ++l5) {
                if (this.index == 0) {
                    this.retreatCounter();
                }
                this.index = this.index - 1 & 0x3F;
            }
        }
        this.generateKeyStream(this.keyStream);
        return l2;
    }

    @Override
    public long seekTo(long l2) {
        this.reset();
        return this.skip(l2);
    }

    @Override
    public long getPosition() {
        return this.getCounter() * 64L + (long)this.index;
    }

    @Override
    public void reset() {
        this.index = 0;
        this.resetLimitCounter();
        this.resetCounter();
        this.generateKeyStream(this.keyStream);
    }

    protected long getCounter() {
        return (long)this.engineState[9] << 32 | (long)this.engineState[8] & 0xFFFFFFFFL;
    }

    protected void resetCounter() {
        this.engineState[9] = 0;
        this.engineState[8] = 0;
    }

    protected void setKey(byte[] byArray, byte[] byArray2) {
        if (byArray != null) {
            if (byArray.length != 16 && byArray.length != 32) {
                throw new IllegalArgumentException(this.getAlgorithmName() + " requires 128 bit or 256 bit key");
            }
            int n2 = (byArray.length - 16) / 4;
            this.engineState[0] = TAU_SIGMA[n2];
            this.engineState[5] = TAU_SIGMA[n2 + 1];
            this.engineState[10] = TAU_SIGMA[n2 + 2];
            this.engineState[15] = TAU_SIGMA[n2 + 3];
            Pack.littleEndianToInt(byArray, 0, this.engineState, 1, 4);
            Pack.littleEndianToInt(byArray, byArray.length - 16, this.engineState, 11, 4);
        }
        Pack.littleEndianToInt(byArray2, 0, this.engineState, 6, 2);
    }

    protected void generateKeyStream(byte[] byArray) {
        Salsa20Engine.salsaCore(this.rounds, this.engineState, this.x);
        Pack.intToLittleEndian(this.x, byArray, 0);
    }

    public static void salsaCore(int n2, int[] nArray, int[] nArray2) {
        if (nArray.length != 16) {
            throw new IllegalArgumentException();
        }
        if (nArray2.length != 16) {
            throw new IllegalArgumentException();
        }
        if (n2 % 2 != 0) {
            throw new IllegalArgumentException("Number of rounds must be even");
        }
        int n3 = nArray[0];
        int n4 = nArray[1];
        int n5 = nArray[2];
        int n6 = nArray[3];
        int n7 = nArray[4];
        int n8 = nArray[5];
        int n9 = nArray[6];
        int n10 = nArray[7];
        int n11 = nArray[8];
        int n12 = nArray[9];
        int n13 = nArray[10];
        int n14 = nArray[11];
        int n15 = nArray[12];
        int n16 = nArray[13];
        int n17 = nArray[14];
        int n18 = nArray[15];
        for (int i2 = n2; i2 > 0; i2 -= 2) {
            n11 ^= Integers.rotateLeft((n7 ^= Integers.rotateLeft(n3 + n15, 7)) + n3, 9);
            n3 ^= Integers.rotateLeft((n15 ^= Integers.rotateLeft(n11 + n7, 13)) + n11, 18);
            n16 ^= Integers.rotateLeft((n12 ^= Integers.rotateLeft(n8 + n4, 7)) + n8, 9);
            n8 ^= Integers.rotateLeft((n4 ^= Integers.rotateLeft(n16 + n12, 13)) + n16, 18);
            n5 ^= Integers.rotateLeft((n17 ^= Integers.rotateLeft(n13 + n9, 7)) + n13, 9);
            n13 ^= Integers.rotateLeft((n9 ^= Integers.rotateLeft(n5 + n17, 13)) + n5, 18);
            n10 ^= Integers.rotateLeft((n6 ^= Integers.rotateLeft(n18 + n14, 7)) + n18, 9);
            n18 ^= Integers.rotateLeft((n14 ^= Integers.rotateLeft(n10 + n6, 13)) + n10, 18);
            n5 ^= Integers.rotateLeft((n4 ^= Integers.rotateLeft(n3 + n6, 7)) + n3, 9);
            n3 ^= Integers.rotateLeft((n6 ^= Integers.rotateLeft(n5 + n4, 13)) + n5, 18);
            n10 ^= Integers.rotateLeft((n9 ^= Integers.rotateLeft(n8 + n7, 7)) + n8, 9);
            n8 ^= Integers.rotateLeft((n7 ^= Integers.rotateLeft(n10 + n9, 13)) + n10, 18);
            n11 ^= Integers.rotateLeft((n14 ^= Integers.rotateLeft(n13 + n12, 7)) + n13, 9);
            n13 ^= Integers.rotateLeft((n12 ^= Integers.rotateLeft(n11 + n14, 13)) + n11, 18);
            n16 ^= Integers.rotateLeft((n15 ^= Integers.rotateLeft(n18 + n17, 7)) + n18, 9);
            n18 ^= Integers.rotateLeft((n17 ^= Integers.rotateLeft(n16 + n15, 13)) + n16, 18);
        }
        nArray2[0] = n3 + nArray[0];
        nArray2[1] = n4 + nArray[1];
        nArray2[2] = n5 + nArray[2];
        nArray2[3] = n6 + nArray[3];
        nArray2[4] = n7 + nArray[4];
        nArray2[5] = n8 + nArray[5];
        nArray2[6] = n9 + nArray[6];
        nArray2[7] = n10 + nArray[7];
        nArray2[8] = n11 + nArray[8];
        nArray2[9] = n12 + nArray[9];
        nArray2[10] = n13 + nArray[10];
        nArray2[11] = n14 + nArray[11];
        nArray2[12] = n15 + nArray[12];
        nArray2[13] = n16 + nArray[13];
        nArray2[14] = n17 + nArray[14];
        nArray2[15] = n18 + nArray[15];
    }

    private void resetLimitCounter() {
        this.cW0 = 0;
        this.cW1 = 0;
        this.cW2 = 0;
    }

    private boolean limitExceeded() {
        if (++this.cW0 == 0 && ++this.cW1 == 0) {
            return (++this.cW2 & 0x20) != 0;
        }
        return false;
    }

    private boolean limitExceeded(int n2) {
        this.cW0 += n2;
        if (this.cW0 < n2 && this.cW0 >= 0 && ++this.cW1 == 0) {
            return (++this.cW2 & 0x20) != 0;
        }
        return false;
    }
}

