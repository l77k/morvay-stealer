/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Pack;

public class SipHash
implements Mac {
    protected final int c;
    protected final int d;
    protected long k0;
    protected long k1;
    protected long v0;
    protected long v1;
    protected long v2;
    protected long v3;
    protected long m = 0L;
    protected int wordPos = 0;
    protected int wordCount = 0;

    public SipHash() {
        this.c = 2;
        this.d = 4;
    }

    public SipHash(int n2, int n3) {
        this.c = n2;
        this.d = n3;
    }

    @Override
    public String getAlgorithmName() {
        return "SipHash-" + this.c + "-" + this.d;
    }

    @Override
    public int getMacSize() {
        return 8;
    }

    @Override
    public void init(CipherParameters cipherParameters) throws IllegalArgumentException {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("'params' must be an instance of KeyParameter");
        }
        KeyParameter keyParameter = (KeyParameter)cipherParameters;
        byte[] byArray = keyParameter.getKey();
        if (byArray.length != 16) {
            throw new IllegalArgumentException("'params' must be a 128-bit key");
        }
        this.k0 = Pack.littleEndianToLong(byArray, 0);
        this.k1 = Pack.littleEndianToLong(byArray, 8);
        this.reset();
    }

    @Override
    public void update(byte by) throws IllegalStateException {
        this.m >>>= 8;
        this.m |= ((long)by & 0xFFL) << 56;
        if (++this.wordPos == 8) {
            this.processMessageWord();
            this.wordPos = 0;
        }
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) throws DataLengthException, IllegalStateException {
        int n4;
        int n5 = n3 & 0xFFFFFFF8;
        if (this.wordPos == 0) {
            for (n4 = 0; n4 < n5; n4 += 8) {
                this.m = Pack.littleEndianToLong(byArray, n2 + n4);
                this.processMessageWord();
            }
            while (n4 < n3) {
                this.m >>>= 8;
                this.m |= ((long)byArray[n2 + n4] & 0xFFL) << 56;
                ++n4;
            }
            this.wordPos = n3 - n5;
        } else {
            int n6 = this.wordPos << 3;
            while (n4 < n5) {
                long l2 = Pack.littleEndianToLong(byArray, n2 + n4);
                this.m = l2 << n6 | this.m >>> -n6;
                this.processMessageWord();
                this.m = l2;
                n4 += 8;
            }
            while (n4 < n3) {
                this.m >>>= 8;
                this.m |= ((long)byArray[n2 + n4] & 0xFFL) << 56;
                if (++this.wordPos == 8) {
                    this.processMessageWord();
                    this.wordPos = 0;
                }
                ++n4;
            }
        }
    }

    public long doFinal() throws DataLengthException, IllegalStateException {
        this.m >>>= 7 - this.wordPos << 3;
        this.m >>>= 8;
        this.m |= ((long)((this.wordCount << 3) + this.wordPos) & 0xFFL) << 56;
        this.processMessageWord();
        this.v2 ^= 0xFFL;
        this.applySipRounds(this.d);
        long l2 = this.v0 ^ this.v1 ^ this.v2 ^ this.v3;
        this.reset();
        return l2;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) throws DataLengthException, IllegalStateException {
        long l2 = this.doFinal();
        Pack.longToLittleEndian(l2, byArray, n2);
        return 8;
    }

    @Override
    public void reset() {
        this.v0 = this.k0 ^ 0x736F6D6570736575L;
        this.v1 = this.k1 ^ 0x646F72616E646F6DL;
        this.v2 = this.k0 ^ 0x6C7967656E657261L;
        this.v3 = this.k1 ^ 0x7465646279746573L;
        this.m = 0L;
        this.wordPos = 0;
        this.wordCount = 0;
    }

    protected void processMessageWord() {
        ++this.wordCount;
        this.v3 ^= this.m;
        this.applySipRounds(this.c);
        this.v0 ^= this.m;
    }

    protected void applySipRounds(int n2) {
        long l2 = this.v0;
        long l3 = this.v1;
        long l4 = this.v2;
        long l5 = this.v3;
        for (int i2 = 0; i2 < n2; ++i2) {
            l2 += l3;
            l4 += l5;
            l3 = SipHash.rotateLeft(l3, 13);
            l5 = SipHash.rotateLeft(l5, 16);
            l3 ^= l2;
            l5 ^= l4;
            l2 = SipHash.rotateLeft(l2, 32);
            l4 += l3;
            l2 += l5;
            l3 = SipHash.rotateLeft(l3, 17);
            l5 = SipHash.rotateLeft(l5, 21);
            l3 ^= l4;
            l5 ^= l2;
            l4 = SipHash.rotateLeft(l4, 32);
        }
        this.v0 = l2;
        this.v1 = l3;
        this.v2 = l4;
        this.v3 = l5;
    }

    protected static long rotateLeft(long l2, int n2) {
        return l2 << n2 | l2 >>> -n2;
    }
}

