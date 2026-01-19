/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.engines.Zuc128CoreEngine;

public final class Zuc128Mac
implements Mac {
    private static final int TOPBIT = 128;
    private final InternalZuc128Engine theEngine = new InternalZuc128Engine();
    private int theMac;
    private final int[] theKeyStream = new int[2];
    private Zuc128CoreEngine theState;
    private int theWordIndex;
    private int theByteIndex;

    @Override
    public String getAlgorithmName() {
        return "Zuc128Mac";
    }

    @Override
    public int getMacSize() {
        return 4;
    }

    @Override
    public void init(CipherParameters cipherParameters) {
        this.theEngine.init(true, cipherParameters);
        this.theState = (Zuc128CoreEngine)this.theEngine.copy();
        this.initKeyStream();
    }

    private void initKeyStream() {
        this.theMac = 0;
        for (int i2 = 0; i2 < this.theKeyStream.length - 1; ++i2) {
            this.theKeyStream[i2] = this.theEngine.createKeyStreamWord();
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

    private void updateMac(int n2) {
        this.theMac ^= this.getKeyStreamWord(n2);
    }

    private int getKeyStreamWord(int n2) {
        int n3 = this.theKeyStream[this.theWordIndex];
        if (n2 == 0) {
            return n3;
        }
        int n4 = this.theKeyStream[(this.theWordIndex + 1) % this.theKeyStream.length];
        return n3 << n2 | n4 >>> 32 - n2;
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) {
        for (int i2 = 0; i2 < n3; ++i2) {
            this.update(byArray[n2 + i2]);
        }
    }

    private int getFinalWord() {
        if (this.theByteIndex != 0) {
            return this.theEngine.createKeyStreamWord();
        }
        this.theWordIndex = (this.theWordIndex + 1) % this.theKeyStream.length;
        return this.theKeyStream[this.theWordIndex];
    }

    @Override
    public int doFinal(byte[] byArray, int n2) {
        this.shift4NextByte();
        this.theMac ^= this.getKeyStreamWord(this.theByteIndex * 8);
        this.theMac ^= this.getFinalWord();
        Zuc128CoreEngine.encode32be(this.theMac, byArray, n2);
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

    private static class InternalZuc128Engine
    extends Zuc128CoreEngine {
        private InternalZuc128Engine() {
        }

        int createKeyStreamWord() {
            return super.makeKeyStreamWord();
        }
    }
}

