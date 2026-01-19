/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.macs;

import java.util.Hashtable;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Memoable;

public class HMac
implements Mac {
    private static final byte IPAD = 54;
    private static final byte OPAD = 92;
    private Digest digest;
    private int digestSize;
    private int blockLength;
    private Memoable ipadState;
    private Memoable opadState;
    private byte[] inputPad;
    private byte[] outputBuf;
    private static Hashtable blockLengths = new Hashtable();

    private static int getByteLength(Digest digest) {
        if (digest instanceof ExtendedDigest) {
            return ((ExtendedDigest)digest).getByteLength();
        }
        Integer n2 = (Integer)blockLengths.get(digest.getAlgorithmName());
        if (n2 == null) {
            throw new IllegalArgumentException("unknown digest passed: " + digest.getAlgorithmName());
        }
        return n2;
    }

    public HMac(Digest digest) {
        this(digest, HMac.getByteLength(digest));
    }

    private HMac(Digest digest, int n2) {
        this.digest = digest;
        this.digestSize = digest.getDigestSize();
        this.blockLength = n2;
        this.inputPad = new byte[this.blockLength];
        this.outputBuf = new byte[this.blockLength + this.digestSize];
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
        this.digest.reset();
        byte[] byArray = ((KeyParameter)cipherParameters).getKey();
        int n2 = byArray.length;
        if (n2 > this.blockLength) {
            this.digest.update(byArray, 0, n2);
            this.digest.doFinal(this.inputPad, 0);
            n2 = this.digestSize;
        } else {
            System.arraycopy(byArray, 0, this.inputPad, 0, n2);
        }
        for (int i2 = n2; i2 < this.inputPad.length; ++i2) {
            this.inputPad[i2] = 0;
        }
        System.arraycopy(this.inputPad, 0, this.outputBuf, 0, this.blockLength);
        HMac.xorPad(this.inputPad, this.blockLength, (byte)54);
        HMac.xorPad(this.outputBuf, this.blockLength, (byte)92);
        if (this.digest instanceof Memoable) {
            this.opadState = ((Memoable)((Object)this.digest)).copy();
            ((Digest)((Object)this.opadState)).update(this.outputBuf, 0, this.blockLength);
        }
        this.digest.update(this.inputPad, 0, this.inputPad.length);
        if (this.digest instanceof Memoable) {
            this.ipadState = ((Memoable)((Object)this.digest)).copy();
        }
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
        this.digest.doFinal(this.outputBuf, this.blockLength);
        if (this.opadState != null) {
            ((Memoable)((Object)this.digest)).reset(this.opadState);
            this.digest.update(this.outputBuf, this.blockLength, this.digest.getDigestSize());
        } else {
            this.digest.update(this.outputBuf, 0, this.outputBuf.length);
        }
        int n3 = this.digest.doFinal(byArray, n2);
        for (int i2 = this.blockLength; i2 < this.outputBuf.length; ++i2) {
            this.outputBuf[i2] = 0;
        }
        if (this.ipadState != null) {
            ((Memoable)((Object)this.digest)).reset(this.ipadState);
        } else {
            this.digest.update(this.inputPad, 0, this.inputPad.length);
        }
        return n3;
    }

    @Override
    public void reset() {
        if (this.ipadState != null) {
            ((Memoable)((Object)this.digest)).reset(this.ipadState);
        } else {
            this.digest.reset();
            this.digest.update(this.inputPad, 0, this.inputPad.length);
        }
    }

    private static void xorPad(byte[] byArray, int n2, byte by) {
        int n3 = 0;
        while (n3 < n2) {
            int n4 = n3++;
            byArray[n4] = (byte)(byArray[n4] ^ by);
        }
    }

    static {
        blockLengths.put("GOST3411", Integers.valueOf(32));
        blockLengths.put("MD2", Integers.valueOf(16));
        blockLengths.put("MD4", Integers.valueOf(64));
        blockLengths.put("MD5", Integers.valueOf(64));
        blockLengths.put("RIPEMD128", Integers.valueOf(64));
        blockLengths.put("RIPEMD160", Integers.valueOf(64));
        blockLengths.put("SHA-1", Integers.valueOf(64));
        blockLengths.put("SHA-224", Integers.valueOf(64));
        blockLengths.put("SHA-256", Integers.valueOf(64));
        blockLengths.put("SHA-384", Integers.valueOf(128));
        blockLengths.put("SHA-512", Integers.valueOf(128));
        blockLengths.put("Tiger", Integers.valueOf(64));
        blockLengths.put("Whirlpool", Integers.valueOf(64));
    }
}

