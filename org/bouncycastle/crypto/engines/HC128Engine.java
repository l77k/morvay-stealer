/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.constraints.DefaultServiceProperties;
import org.bouncycastle.crypto.engines.Utils;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class HC128Engine
implements StreamCipher {
    private int[] p = new int[512];
    private int[] q = new int[512];
    private int cnt = 0;
    private byte[] key;
    private byte[] iv;
    private boolean initialised;
    private byte[] buf = new byte[4];
    private int idx = 0;

    private static int f1(int n2) {
        return HC128Engine.rotateRight(n2, 7) ^ HC128Engine.rotateRight(n2, 18) ^ n2 >>> 3;
    }

    private static int f2(int n2) {
        return HC128Engine.rotateRight(n2, 17) ^ HC128Engine.rotateRight(n2, 19) ^ n2 >>> 10;
    }

    private int g1(int n2, int n3, int n4) {
        return (HC128Engine.rotateRight(n2, 10) ^ HC128Engine.rotateRight(n4, 23)) + HC128Engine.rotateRight(n3, 8);
    }

    private int g2(int n2, int n3, int n4) {
        return (HC128Engine.rotateLeft(n2, 10) ^ HC128Engine.rotateLeft(n4, 23)) + HC128Engine.rotateLeft(n3, 8);
    }

    private static int rotateLeft(int n2, int n3) {
        return n2 << n3 | n2 >>> -n3;
    }

    private static int rotateRight(int n2, int n3) {
        return n2 >>> n3 | n2 << -n3;
    }

    private int h1(int n2) {
        return this.q[n2 & 0xFF] + this.q[(n2 >> 16 & 0xFF) + 256];
    }

    private int h2(int n2) {
        return this.p[n2 & 0xFF] + this.p[(n2 >> 16 & 0xFF) + 256];
    }

    private static int mod1024(int n2) {
        return n2 & 0x3FF;
    }

    private static int mod512(int n2) {
        return n2 & 0x1FF;
    }

    private static int dim(int n2, int n3) {
        return HC128Engine.mod512(n2 - n3);
    }

    private int step() {
        int n2;
        int n3 = HC128Engine.mod512(this.cnt);
        if (this.cnt < 512) {
            int n4 = n3;
            this.p[n4] = this.p[n4] + this.g1(this.p[HC128Engine.dim(n3, 3)], this.p[HC128Engine.dim(n3, 10)], this.p[HC128Engine.dim(n3, 511)]);
            n2 = this.h1(this.p[HC128Engine.dim(n3, 12)]) ^ this.p[n3];
        } else {
            int n5 = n3;
            this.q[n5] = this.q[n5] + this.g2(this.q[HC128Engine.dim(n3, 3)], this.q[HC128Engine.dim(n3, 10)], this.q[HC128Engine.dim(n3, 511)]);
            n2 = this.h2(this.q[HC128Engine.dim(n3, 12)]) ^ this.q[n3];
        }
        this.cnt = HC128Engine.mod1024(this.cnt + 1);
        return n2;
    }

    private void init() {
        int n2;
        if (this.key.length != 16) {
            throw new IllegalArgumentException("The key must be 128 bits long");
        }
        if (this.iv.length != 16) {
            throw new IllegalArgumentException("The IV must be 128 bits long");
        }
        this.idx = 0;
        this.cnt = 0;
        int[] nArray = new int[1280];
        for (n2 = 0; n2 < 16; ++n2) {
            int n3 = n2 >> 2;
            nArray[n3] = nArray[n3] | (this.key[n2] & 0xFF) << 8 * (n2 & 3);
        }
        System.arraycopy(nArray, 0, nArray, 4, 4);
        for (n2 = 0; n2 < this.iv.length && n2 < 16; ++n2) {
            int n4 = (n2 >> 2) + 8;
            nArray[n4] = nArray[n4] | (this.iv[n2] & 0xFF) << 8 * (n2 & 3);
        }
        System.arraycopy(nArray, 8, nArray, 12, 4);
        for (n2 = 16; n2 < 1280; ++n2) {
            nArray[n2] = HC128Engine.f2(nArray[n2 - 2]) + nArray[n2 - 7] + HC128Engine.f1(nArray[n2 - 15]) + nArray[n2 - 16] + n2;
        }
        System.arraycopy(nArray, 256, this.p, 0, 512);
        System.arraycopy(nArray, 768, this.q, 0, 512);
        for (n2 = 0; n2 < 512; ++n2) {
            this.p[n2] = this.step();
        }
        for (n2 = 0; n2 < 512; ++n2) {
            this.q[n2] = this.step();
        }
        this.cnt = 0;
    }

    @Override
    public String getAlgorithmName() {
        return "HC-128";
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) throws IllegalArgumentException {
        if (!(cipherParameters instanceof ParametersWithIV)) {
            throw new IllegalArgumentException("no IV passed");
        }
        this.iv = ((ParametersWithIV)cipherParameters).getIV();
        CipherParameters cipherParameters2 = ((ParametersWithIV)cipherParameters).getParameters();
        if (!(cipherParameters2 instanceof KeyParameter)) {
            throw new IllegalArgumentException("Invalid parameter passed to HC128 init - " + cipherParameters.getClass().getName());
        }
        this.key = ((KeyParameter)cipherParameters2).getKey();
        this.init();
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), 128, cipherParameters, Utils.getPurpose(bl)));
        this.initialised = true;
    }

    private byte getByte() {
        int n2;
        if (this.idx == 0) {
            n2 = this.step();
            this.buf[0] = (byte)(n2 & 0xFF);
            this.buf[1] = (byte)((n2 >>= 8) & 0xFF);
            this.buf[2] = (byte)((n2 >>= 8) & 0xFF);
            this.buf[3] = (byte)((n2 >>= 8) & 0xFF);
        }
        n2 = this.buf[this.idx];
        this.idx = this.idx + 1 & 3;
        return (byte)n2;
    }

    @Override
    public int processBytes(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws DataLengthException {
        if (!this.initialised) {
            throw new IllegalStateException(this.getAlgorithmName() + " not initialised");
        }
        if (n2 + n3 > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n4 + n3 > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        for (int i2 = 0; i2 < n3; ++i2) {
            byArray2[n4 + i2] = (byte)(byArray[n2 + i2] ^ this.getByte());
        }
        return n3;
    }

    @Override
    public void reset() {
        this.init();
    }

    @Override
    public byte returnByte(byte by) {
        return (byte)(by ^ this.getByte());
    }
}

