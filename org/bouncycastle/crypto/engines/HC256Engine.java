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

public class HC256Engine
implements StreamCipher {
    private int[] p = new int[1024];
    private int[] q = new int[1024];
    private int cnt = 0;
    private byte[] key;
    private byte[] iv;
    private boolean initialised;
    private byte[] buf = new byte[4];
    private int idx = 0;

    private int step() {
        int n2;
        int n3 = this.cnt & 0x3FF;
        if (this.cnt < 1024) {
            int n4 = this.p[n3 - 3 & 0x3FF];
            int n5 = this.p[n3 - 1023 & 0x3FF];
            int n6 = n3;
            this.p[n6] = this.p[n6] + (this.p[n3 - 10 & 0x3FF] + (HC256Engine.rotateRight(n4, 10) ^ HC256Engine.rotateRight(n5, 23)) + this.q[(n4 ^ n5) & 0x3FF]);
            n4 = this.p[n3 - 12 & 0x3FF];
            n2 = this.q[n4 & 0xFF] + this.q[(n4 >> 8 & 0xFF) + 256] + this.q[(n4 >> 16 & 0xFF) + 512] + this.q[(n4 >> 24 & 0xFF) + 768] ^ this.p[n3];
        } else {
            int n7 = this.q[n3 - 3 & 0x3FF];
            int n8 = this.q[n3 - 1023 & 0x3FF];
            int n9 = n3;
            this.q[n9] = this.q[n9] + (this.q[n3 - 10 & 0x3FF] + (HC256Engine.rotateRight(n7, 10) ^ HC256Engine.rotateRight(n8, 23)) + this.p[(n7 ^ n8) & 0x3FF]);
            n7 = this.q[n3 - 12 & 0x3FF];
            n2 = this.p[n7 & 0xFF] + this.p[(n7 >> 8 & 0xFF) + 256] + this.p[(n7 >> 16 & 0xFF) + 512] + this.p[(n7 >> 24 & 0xFF) + 768] ^ this.q[n3];
        }
        this.cnt = this.cnt + 1 & 0x7FF;
        return n2;
    }

    private void init() {
        int n2;
        Object[] objectArray;
        if (this.key.length != 32 && this.key.length != 16) {
            throw new IllegalArgumentException("The key must be 128/256 bits long");
        }
        if (this.iv.length < 16) {
            throw new IllegalArgumentException("The IV must be at least 128 bits long");
        }
        if (this.key.length != 32) {
            objectArray = new byte[32];
            System.arraycopy(this.key, 0, objectArray, 0, this.key.length);
            System.arraycopy(this.key, 0, objectArray, 16, this.key.length);
            this.key = objectArray;
        }
        if (this.iv.length < 32) {
            objectArray = new byte[32];
            System.arraycopy(this.iv, 0, objectArray, 0, this.iv.length);
            System.arraycopy(this.iv, 0, objectArray, this.iv.length, objectArray.length - this.iv.length);
            this.iv = objectArray;
        }
        this.idx = 0;
        this.cnt = 0;
        objectArray = new int[2560];
        for (n2 = 0; n2 < 32; ++n2) {
            int n3 = n2 >> 2;
            objectArray[n3] = objectArray[n3] | (this.key[n2] & 0xFF) << 8 * (n2 & 3);
        }
        for (n2 = 0; n2 < 32; ++n2) {
            int n4 = (n2 >> 2) + 8;
            objectArray[n4] = objectArray[n4] | (this.iv[n2] & 0xFF) << 8 * (n2 & 3);
        }
        for (n2 = 16; n2 < 2560; ++n2) {
            byte by = objectArray[n2 - 2];
            byte by2 = objectArray[n2 - 15];
            objectArray[n2] = (HC256Engine.rotateRight(by, 17) ^ HC256Engine.rotateRight(by, 19) ^ by >>> 10) + objectArray[n2 - 7] + (HC256Engine.rotateRight(by2, 7) ^ HC256Engine.rotateRight(by2, 18) ^ by2 >>> 3) + objectArray[n2 - 16] + n2;
        }
        System.arraycopy(objectArray, 512, this.p, 0, 1024);
        System.arraycopy(objectArray, 1536, this.q, 0, 1024);
        for (n2 = 0; n2 < 4096; ++n2) {
            this.step();
        }
        this.cnt = 0;
    }

    @Override
    public String getAlgorithmName() {
        return "HC-256";
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) throws IllegalArgumentException {
        CipherParameters cipherParameters2 = cipherParameters;
        if (cipherParameters instanceof ParametersWithIV) {
            this.iv = ((ParametersWithIV)cipherParameters).getIV();
            cipherParameters2 = ((ParametersWithIV)cipherParameters).getParameters();
        } else {
            this.iv = new byte[0];
        }
        if (!(cipherParameters2 instanceof KeyParameter)) {
            throw new IllegalArgumentException("Invalid parameter passed to HC256 init - " + cipherParameters.getClass().getName());
        }
        this.key = ((KeyParameter)cipherParameters2).getKey();
        this.init();
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), this.key.length * 8, cipherParameters, Utils.getPurpose(bl)));
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

    private static int rotateRight(int n2, int n3) {
        return n2 >>> n3 | n2 << -n3;
    }
}

