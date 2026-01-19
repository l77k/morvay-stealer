/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.params.KeyParameter;

public class OldHMac
implements Mac {
    private static final int BLOCK_LENGTH = 64;
    private static final byte IPAD = 54;
    private static final byte OPAD = 92;
    private Digest digest;
    private int digestSize;
    private byte[] inputPad = new byte[64];
    private byte[] outputPad = new byte[64];

    public OldHMac(Digest digest) {
        this.digest = digest;
        this.digestSize = digest.getDigestSize();
    }

    @Override
    public String getAlgorithmName() {
        return this.digest.getAlgorithmName() + "/HMAC";
    }

    public Digest getUnderlyingDigest() {
        return this.digest;
    }

    @Override
    public void init(CipherParameters cipherParameters) {
        int n2;
        this.digest.reset();
        byte[] byArray = ((KeyParameter)cipherParameters).getKey();
        if (byArray.length > 64) {
            this.digest.update(byArray, 0, byArray.length);
            this.digest.doFinal(this.inputPad, 0);
            for (n2 = this.digestSize; n2 < this.inputPad.length; ++n2) {
                this.inputPad[n2] = 0;
            }
        } else {
            System.arraycopy(byArray, 0, this.inputPad, 0, byArray.length);
            for (n2 = byArray.length; n2 < this.inputPad.length; ++n2) {
                this.inputPad[n2] = 0;
            }
        }
        this.outputPad = new byte[this.inputPad.length];
        System.arraycopy(this.inputPad, 0, this.outputPad, 0, this.inputPad.length);
        n2 = 0;
        while (n2 < this.inputPad.length) {
            int n3 = n2++;
            this.inputPad[n3] = (byte)(this.inputPad[n3] ^ 0x36);
        }
        n2 = 0;
        while (n2 < this.outputPad.length) {
            int n4 = n2++;
            this.outputPad[n4] = (byte)(this.outputPad[n4] ^ 0x5C);
        }
        this.digest.update(this.inputPad, 0, this.inputPad.length);
    }

    @Override
    public int getMacSize() {
        return this.digestSize;
    }

    @Override
    public void update(byte by) {
        this.digest.update(by);
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) {
        this.digest.update(byArray, n2, n3);
    }

    @Override
    public int doFinal(byte[] byArray, int n2) {
        byte[] byArray2 = new byte[this.digestSize];
        this.digest.doFinal(byArray2, 0);
        this.digest.update(this.outputPad, 0, this.outputPad.length);
        this.digest.update(byArray2, 0, byArray2.length);
        int n3 = this.digest.doFinal(byArray, n2);
        this.reset();
        return n3;
    }

    @Override
    public void reset() {
        this.digest.reset();
        this.digest.update(this.inputPad, 0, this.inputPad.length);
    }
}

