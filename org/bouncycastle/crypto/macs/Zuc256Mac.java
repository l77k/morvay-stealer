/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.engines.Zuc256CoreEngine;

public final class Zuc256Mac
implements Mac {
    private static final int TOPBIT = 128;
    private final InternalZuc256Engine theEngine;
    private final int theMacLength;
    private final int[] theMac;
    private final int[] theKeyStream;
    private Zuc256CoreEngine theState;
    private int theWordIndex;
    private int theByteIndex;

    public Zuc256Mac(int n2) {
        this.theEngine = new InternalZuc256Engine(n2);
        this.theMacLength = n2;
        int n3 = n2 / 32;
        this.theMac = new int[n3];
        this.theKeyStream = new int[n3 + 1];
    }

    @Override
    public String getAlgorithmName() {
        return "Zuc256Mac-" + this.theMacLength;
    }

    @Override
    public int getMacSize() {
        return this.theMacLength / 8;
    }

    @Override
    public void init(CipherParameters cipherParameters) {
        this.theEngine.init(true, cipherParameters);
        this.theState = (Zuc256CoreEngine)this.theEngine.copy();
        this.initKeyStream();
    }

    private void initKeyStream() {
        int n2;
        for (n2 = 0; n2 < this.theMac.length; ++n2) {
            this.theMac[n2] = this.theEngine.createKeyStreamWord();
        }
        for (n2 = 0; n2 < this.theKeyStream.length - 1; ++n2) {
            this.theKeyStream[n2] = this.theEngine.createKeyStreamWord();
        }
        this.theWordIndex = this.theKeyStream.length - 1;
        this.theByteIndex = 3;
    }

    @Override
    public void update(byte by) {
        this.shift4NextByte();
        int n2 = this.theByteIndex * 8;
        int n3 = 128;
        int n4 = 0;
        while (n3 > 0) {
            if ((by & n3) != 0) {
                this.updateMac(n2 + n4);
            }
            n3 >>= 1;
            ++n4;
        }
    }

    private void shift4NextByte() {
        this.theByteIndex = (this.theByteIndex + 1) % 4;
        if (this.theByteIndex == 0) {
            this.theKeyStream[this.theWordIndex] = this.theEngine.createKeyStreamWord();
            this.theWordIndex = (this.theWordIndex + 1) % this.theKeyStream.length;
        }
    }

    private void shift4Final() {
        this.theByteIndex = (this.theByteIndex + 1) % 4;
        if (this.theByteIndex == 0) {
            this.theWordIndex = (this.theWordIndex + 1) % this.theKeyStream.length;
        }
    }

    private void updateMac(int n2) {
        for (int i2 = 0; i2 < this.theMac.length; ++i2) {
            int n3 = i2;
            this.theMac[n3] = this.theMac[n3] ^ this.getKeyStreamWord(i2, n2);
        }
    }

    private int getKeyStreamWord(int n2, int n3) {
        int n4 = this.theKeyStream[(this.theWordIndex + n2) % this.theKeyStream.length];
        if (n3 == 0) {
            return n4;
        }
        int n5 = this.theKeyStream[(this.theWordIndex + n2 + 1) % this.theKeyStream.length];
        return n4 << n3 | n5 >>> 32 - n3;
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) {
        for (int i2 = 0; i2 < n3; ++i2) {
            this.update(byArray[n2 + i2]);
        }
    }

    @Override
    public int doFinal(byte[] byArray, int n2) {
        this.shift4Final();
        this.updateMac(this.theByteIndex * 8);
        for (int i2 = 0; i2 < this.theMac.length; ++i2) {
            Zuc256CoreEngine.encode32be(this.theMac[i2], byArray, n2 + i2 * 4);
        }
        this.reset();
        return this.getMacSize();
    }

    @Override
    public void reset() {
        if (this.theState != null) {
            this.theEngine.reset(this.theState);
        }
        this.initKeyStream();
    }

    private static class InternalZuc256Engine
    extends Zuc256CoreEngine {
        public InternalZuc256Engine(int n2) {
            super(n2);
        }

        int createKeyStreamWord() {
            return super.makeKeyStreamWord();
        }
    }
}

