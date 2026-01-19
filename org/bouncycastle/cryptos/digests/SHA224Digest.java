/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.CryptoServiceProperties;
import org.bouncycastle.crypto.CryptoServicePurpose;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.digests.EncodableDigest;
import org.bouncycastle.crypto.digests.GeneralDigest;
import org.bouncycastle.crypto.digests.Utils;
import org.bouncycastle.util.Memoable;
import org.bouncycastle.util.Pack;

public class SHA224Digest
extends GeneralDigest
implements EncodableDigest {
    private static final int DIGEST_LENGTH = 28;
    private int H1;
    private int H2;
    private int H3;
    private int H4;
    private int H5;
    private int H6;
    private int H7;
    private int H8;
    private int[] X = new int[64];
    private int xOff;
    static final int[] K = new int[]{1116352408, 1899447441, -1245643825, -373957723, 961987163, 1508970993, -1841331548, -1424204075, -670586216, 310598401, 607225278, 1426881987, 1925078388, -2132889090, -1680079193, -1046744716, -459576895, -272742522, 264347078, 604807628, 770255983, 1249150122, 1555081692, 1996064986, -1740746414, -1473132947, -1341970488, -1084653625, -958395405, -710438585, 113926993, 338241895, 666307205, 773529912, 1294757372, 1396182291, 1695183700, 1986661051, -2117940946, -1838011259, -1564481375, -1474664885, -1035236496, -949202525, -778901479, -694614492, -200395387, 275423344, 430227734, 506948616, 659060556, 883997877, 958139571, 1322822218, 1537002063, 1747873779, 1955562222, 2024104815, -2067236844, -1933114872, -1866530822, -1538233109, -1090935817, -965641998};

    public SHA224Digest() {
        this(CryptoServicePurpose.ANY);
    }

    public SHA224Digest(CryptoServicePurpose cryptoServicePurpose) {
        super(cryptoServicePurpose);
        CryptoServicesRegistrar.checkConstraints(this.cryptoServiceProperties());
        this.reset();
    }

    public SHA224Digest(SHA224Digest sHA224Digest) {
        super(sHA224Digest);
        CryptoServicesRegistrar.checkConstraints(this.cryptoServiceProperties());
        this.doCopy(sHA224Digest);
    }

    private void doCopy(SHA224Digest sHA224Digest) {
        super.copyIn(sHA224Digest);
        this.H1 = sHA224Digest.H1;
        this.H2 = sHA224Digest.H2;
        this.H3 = sHA224Digest.H3;
        this.H4 = sHA224Digest.H4;
        this.H5 = sHA224Digest.H5;
        this.H6 = sHA224Digest.H6;
        this.H7 = sHA224Digest.H7;
        this.H8 = sHA224Digest.H8;
        System.arraycopy(sHA224Digest.X, 0, this.X, 0, sHA224Digest.X.length);
        this.xOff = sHA224Digest.xOff;
    }

    public SHA224Digest(byte[] byArray) {
        super(byArray);
        CryptoServicesRegistrar.checkConstraints(this.cryptoServiceProperties());
        this.H1 = Pack.bigEndianToInt(byArray, 16);
        this.H2 = Pack.bigEndianToInt(byArray, 20);
        this.H3 = Pack.bigEndianToInt(byArray, 24);
        this.H4 = Pack.bigEndianToInt(byArray, 28);
        this.H5 = Pack.bigEndianToInt(byArray, 32);
        this.H6 = Pack.bigEndianToInt(byArray, 36);
        this.H7 = Pack.bigEndianToInt(byArray, 40);
        this.H8 = Pack.bigEndianToInt(byArray, 44);
        this.xOff = Pack.bigEndianToInt(byArray, 48);
        for (int i2 = 0; i2 != this.xOff; ++i2) {
            this.X[i2] = Pack.bigEndianToInt(byArray, 52 + i2 * 4);
        }
    }

    @Override
    public String getAlgorithmName() {
        return "SHA-224";
    }

    @Override
    public int getDigestSize() {
        return 28;
    }

    @Override
    protected void processWord(byte[] byArray, int n2) {
        this.X[this.xOff] = Pack.bigEndianToInt(byArray, n2);
        if (++this.xOff == 16) {
            this.processBlock();
        }
    }

    @Override
    protected void processLength(long l2) {
        if (this.xOff > 14) {
            this.processBlock();
        }
        this.X[14] = (int)(l2 >>> 32);
        this.X[15] = (int)(l2 & 0xFFFFFFFFFFFFFFFFL);
    }

    @Override
    public int doFinal(byte[] byArray, int n2) {
        this.finish();
        Pack.intToBigEndian(this.H1, byArray, n2);
        Pack.intToBigEndian(this.H2, byArray, n2 + 4);
        Pack.intToBigEndian(this.H3, byArray, n2 + 8);
        Pack.intToBigEndian(this.H4, byArray, n2 + 12);
        Pack.intToBigEndian(this.H5, byArray, n2 + 16);
        Pack.intToBigEndian(this.H6, byArray, n2 + 20);
        Pack.intToBigEndian(this.H7, byArray, n2 + 24);
        this.reset();
        return 28;
    }

    @Override
    public void reset() {
        super.reset();
        this.H1 = -1056596264;
        this.H2 = 914150663;
        this.H3 = 812702999;
        this.H4 = -150054599;
        this.H5 = -4191439;
        this.H6 = 1750603025;
        this.H7 = 1694076839;
        this.H8 = -1090891868;
        this.xOff = 0;
        for (int i2 = 0; i2 != this.X.length; ++i2) {
            this.X[i2] = 0;
        }
    }

    @Override
    protected void processBlock() {
        int n2;
        int n3;
        for (n3 = 16; n3 <= 63; ++n3) {
            this.X[n3] = this.Theta1(this.X[n3 - 2]) + this.X[n3 - 7] + this.Theta0(this.X[n3 - 15]) + this.X[n3 - 16];
        }
        n3 = this.H1;
        int n4 = this.H2;
        int n5 = this.H3;
        int n6 = this.H4;
        int n7 = this.H5;
        int n8 = this.H6;
        int n9 = this.H7;
        int n10 = this.H8;
        int n11 = 0;
        for (n2 = 0; n2 < 8; ++n2) {
            n6 += (n10 += this.Sum1(n7) + this.Ch(n7, n8, n9) + K[n11] + this.X[n11]);
            n10 += this.Sum0(n3) + this.Maj(n3, n4, n5);
            n5 += (n9 += this.Sum1(n6) + this.Ch(n6, n7, n8) + K[++n11] + this.X[n11]);
            n9 += this.Sum0(n10) + this.Maj(n10, n3, n4);
            n4 += (n8 += this.Sum1(n5) + this.Ch(n5, n6, n7) + K[++n11] + this.X[n11]);
            n8 += this.Sum0(n9) + this.Maj(n9, n10, n3);
            n3 += (n7 += this.Sum1(n4) + this.Ch(n4, n5, n6) + K[++n11] + this.X[n11]);
            n7 += this.Sum0(n8) + this.Maj(n8, n9, n10);
            n10 += (n6 += this.Sum1(n3) + this.Ch(n3, n4, n5) + K[++n11] + this.X[n11]);
            n6 += this.Sum0(n7) + this.Maj(n7, n8, n9);
            n9 += (n5 += this.Sum1(n10) + this.Ch(n10, n3, n4) + K[++n11] + this.X[n11]);
            n5 += this.Sum0(n6) + this.Maj(n6, n7, n8);
            n8 += (n4 += this.Sum1(n9) + this.Ch(n9, n10, n3) + K[++n11] + this.X[n11]);
            n4 += this.Sum0(n5) + this.Maj(n5, n6, n7);
            n7 += (n3 += this.Sum1(n8) + this.Ch(n8, n9, n10) + K[++n11] + this.X[n11]);
            n3 += this.Sum0(n4) + this.Maj(n4, n5, n6);
            ++n11;
        }
        this.H1 += n3;
        this.H2 += n4;
        this.H3 += n5;
        this.H4 += n6;
        this.H5 += n7;
        this.H6 += n8;
        this.H7 += n9;
        this.H8 += n10;
        this.xOff = 0;
        for (n2 = 0; n2 < 16; ++n2) {
            this.X[n2] = 0;
        }
    }

    private int Ch(int n2, int n3, int n4) {
        return n2 & n3 ^ ~n2 & n4;
    }

    private int Maj(int n2, int n3, int n4) {
        return n2 & n3 ^ n2 & n4 ^ n3 & n4;
    }

    private int Sum0(int n2) {
        return (n2 >>> 2 | n2 << 30) ^ (n2 >>> 13 | n2 << 19) ^ (n2 >>> 22 | n2 << 10);
    }

    private int Sum1(int n2) {
        return (n2 >>> 6 | n2 << 26) ^ (n2 >>> 11 | n2 << 21) ^ (n2 >>> 25 | n2 << 7);
    }

    private int Theta0(int n2) {
        return (n2 >>> 7 | n2 << 25) ^ (n2 >>> 18 | n2 << 14) ^ n2 >>> 3;
    }

    private int Theta1(int n2) {
        return (n2 >>> 17 | n2 << 15) ^ (n2 >>> 19 | n2 << 13) ^ n2 >>> 10;
    }

    @Override
    public Memoable copy() {
        return new SHA224Digest(this);
    }

    @Override
    public void reset(Memoable memoable) {
        SHA224Digest sHA224Digest = (SHA224Digest)memoable;
        this.doCopy(sHA224Digest);
    }

    @Override
    public byte[] getEncodedState() {
        byte[] byArray = new byte[52 + this.xOff * 4 + 1];
        super.populateState(byArray);
        Pack.intToBigEndian(this.H1, byArray, 16);
        Pack.intToBigEndian(this.H2, byArray, 20);
        Pack.intToBigEndian(this.H3, byArray, 24);
        Pack.intToBigEndian(this.H4, byArray, 28);
        Pack.intToBigEndian(this.H5, byArray, 32);
        Pack.intToBigEndian(this.H6, byArray, 36);
        Pack.intToBigEndian(this.H7, byArray, 40);
        Pack.intToBigEndian(this.H8, byArray, 44);
        Pack.intToBigEndian(this.xOff, byArray, 48);
        for (int i2 = 0; i2 != this.xOff; ++i2) {
            Pack.intToBigEndian(this.X[i2], byArray, 52 + i2 * 4);
        }
        byArray[byArray.length - 1] = (byte)this.purpose.ordinal();
        return byArray;
    }

    @Override
    protected CryptoServiceProperties cryptoServiceProperties() {
        return Utils.getDefaultProperties(this, 192, this.purpose);
    }
}

