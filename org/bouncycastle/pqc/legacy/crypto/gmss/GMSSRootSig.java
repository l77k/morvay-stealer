/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.gmss;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.legacy.crypto.gmss.util.GMSSRandom;
import org.bouncycastle.util.encoders.Hex;

public class GMSSRootSig {
    private Digest messDigestOTS;
    private int mdsize;
    private int keysize;
    private byte[] privateKeyOTS;
    private byte[] hash;
    private byte[] sign;
    private int w;
    private GMSSRandom gmssRandom;
    private int messagesize;
    private int k;
    private int r;
    private int test;
    private int counter;
    private int ii;
    private long test8;
    private long big8;
    private int steps;
    private int checksum;
    private int height;
    private byte[] seed;

    public GMSSRootSig(Digest digest, byte[][] byArray, int[] nArray) {
        this.messDigestOTS = digest;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.counter = nArray[0];
        this.test = nArray[1];
        this.ii = nArray[2];
        this.r = nArray[3];
        this.steps = nArray[4];
        this.keysize = nArray[5];
        this.height = nArray[6];
        this.w = nArray[7];
        this.checksum = nArray[8];
        this.mdsize = this.messDigestOTS.getDigestSize();
        this.k = (1 << this.w) - 1;
        int n2 = this.mdsize << 3;
        this.messagesize = (int)Math.ceil((double)n2 / (double)this.w);
        this.privateKeyOTS = byArray[0];
        this.seed = byArray[1];
        this.hash = byArray[2];
        this.sign = byArray[3];
        this.test8 = (long)(byArray[4][0] & 0xFF) | (long)(byArray[4][1] & 0xFF) << 8 | (long)(byArray[4][2] & 0xFF) << 16 | (long)(byArray[4][3] & 0xFF) << 24 | (long)(byArray[4][4] & 0xFF) << 32 | (long)(byArray[4][5] & 0xFF) << 40 | (long)(byArray[4][6] & 0xFF) << 48 | (long)(byArray[4][7] & 0xFF) << 56;
        this.big8 = (long)(byArray[4][8] & 0xFF) | (long)(byArray[4][9] & 0xFF) << 8 | (long)(byArray[4][10] & 0xFF) << 16 | (long)(byArray[4][11] & 0xFF) << 24 | (long)(byArray[4][12] & 0xFF) << 32 | (long)(byArray[4][13] & 0xFF) << 40 | (long)(byArray[4][14] & 0xFF) << 48 | (long)(byArray[4][15] & 0xFF) << 56;
    }

    public GMSSRootSig(Digest digest, int n2, int n3) {
        this.messDigestOTS = digest;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.mdsize = this.messDigestOTS.getDigestSize();
        this.w = n2;
        this.height = n3;
        this.k = (1 << n2) - 1;
        int n4 = this.mdsize << 3;
        this.messagesize = (int)Math.ceil((double)n4 / (double)n2);
    }

    public void initSign(byte[] byArray, byte[] byArray2) {
        this.hash = new byte[this.mdsize];
        this.messDigestOTS.update(byArray2, 0, byArray2.length);
        this.hash = new byte[this.messDigestOTS.getDigestSize()];
        this.messDigestOTS.doFinal(this.hash, 0);
        byte[] byArray3 = new byte[this.mdsize];
        System.arraycopy(this.hash, 0, byArray3, 0, this.mdsize);
        int n2 = 0;
        int n3 = 0;
        int n4 = this.getLog((this.messagesize << this.w) + 1);
        if (8 % this.w == 0) {
            int n5;
            int n6 = 8 / this.w;
            for (n5 = 0; n5 < this.mdsize; ++n5) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    n3 += byArray3[n5] & this.k;
                    byArray3[n5] = (byte)(byArray3[n5] >>> this.w);
                }
            }
            n2 = this.checksum = (this.messagesize << this.w) - n3;
            for (n5 = 0; n5 < n4; n5 += this.w) {
                n3 += n2 & this.k;
                n2 >>>= this.w;
            }
        } else if (this.w < 8) {
            long l2;
            int n7;
            int n8 = 0;
            int n9 = this.mdsize / this.w;
            for (n7 = 0; n7 < n9; ++n7) {
                int n10;
                l2 = 0L;
                for (n10 = 0; n10 < this.w; ++n10) {
                    l2 ^= (long)((byArray3[n8] & 0xFF) << (n10 << 3));
                    ++n8;
                }
                for (n10 = 0; n10 < 8; ++n10) {
                    n3 += (int)(l2 & (long)this.k);
                    l2 >>>= this.w;
                }
            }
            n9 = this.mdsize % this.w;
            l2 = 0L;
            for (n7 = 0; n7 < n9; ++n7) {
                l2 ^= (long)((byArray3[n8] & 0xFF) << (n7 << 3));
                ++n8;
            }
            n9 <<= 3;
            for (n7 = 0; n7 < n9; n7 += this.w) {
                n3 += (int)(l2 & (long)this.k);
                l2 >>>= this.w;
            }
            n2 = this.checksum = (this.messagesize << this.w) - n3;
            for (n7 = 0; n7 < n4; n7 += this.w) {
                n3 += n2 & this.k;
                n2 >>>= this.w;
            }
        } else if (this.w < 57) {
            int n11;
            int n12;
            long l3;
            int n13;
            int n14;
            int n15 = 0;
            while (n15 <= (this.mdsize << 3) - this.w) {
                n14 = n15 >>> 3;
                n13 = n15 % 8;
                int n16 = (n15 += this.w) + 7 >>> 3;
                l3 = 0L;
                n12 = 0;
                for (n11 = n14; n11 < n16; ++n11) {
                    l3 ^= (long)((byArray3[n11] & 0xFF) << (n12 << 3));
                    ++n12;
                }
                n3 = (int)((long)n3 + ((l3 >>>= n13) & (long)this.k));
            }
            n14 = n15 >>> 3;
            if (n14 < this.mdsize) {
                n13 = n15 % 8;
                l3 = 0L;
                n12 = 0;
                for (n11 = n14; n11 < this.mdsize; ++n11) {
                    l3 ^= (long)((byArray3[n11] & 0xFF) << (n12 << 3));
                    ++n12;
                }
                n3 = (int)((long)n3 + ((l3 >>>= n13) & (long)this.k));
            }
            n2 = this.checksum = (this.messagesize << this.w) - n3;
            for (n11 = 0; n11 < n4; n11 += this.w) {
                n3 += n2 & this.k;
                n2 >>>= this.w;
            }
        }
        this.keysize = this.messagesize + (int)Math.ceil((double)n4 / (double)this.w);
        this.steps = (int)Math.ceil((double)(this.keysize + n3) / (double)(1 << this.height));
        this.sign = new byte[this.keysize * this.mdsize];
        this.counter = 0;
        this.test = 0;
        this.ii = 0;
        this.test8 = 0L;
        this.r = 0;
        this.privateKeyOTS = new byte[this.mdsize];
        this.seed = new byte[this.mdsize];
        System.arraycopy(byArray, 0, this.seed, 0, this.mdsize);
    }

    public boolean updateSign() {
        for (int i2 = 0; i2 < this.steps; ++i2) {
            if (this.counter < this.keysize) {
                this.oneStep();
            }
            if (this.counter != this.keysize) continue;
            return true;
        }
        return false;
    }

    public byte[] getSig() {
        return this.sign;
    }

    private void oneStep() {
        if (8 % this.w == 0) {
            if (this.test == 0) {
                this.privateKeyOTS = this.gmssRandom.nextSeed(this.seed);
                if (this.ii < this.mdsize) {
                    this.test = this.hash[this.ii] & this.k;
                    this.hash[this.ii] = (byte)(this.hash[this.ii] >>> this.w);
                } else {
                    this.test = this.checksum & this.k;
                    this.checksum >>>= this.w;
                }
            } else if (this.test > 0) {
                this.messDigestOTS.update(this.privateKeyOTS, 0, this.privateKeyOTS.length);
                this.privateKeyOTS = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(this.privateKeyOTS, 0);
                --this.test;
            }
            if (this.test == 0) {
                System.arraycopy(this.privateKeyOTS, 0, this.sign, this.counter * this.mdsize, this.mdsize);
                ++this.counter;
                if (this.counter % (8 / this.w) == 0) {
                    ++this.ii;
                }
            }
        } else if (this.w < 8) {
            if (this.test == 0) {
                if (this.counter % 8 == 0 && this.ii < this.mdsize) {
                    this.big8 = 0L;
                    if (this.counter < this.mdsize / this.w << 3) {
                        for (int i2 = 0; i2 < this.w; ++i2) {
                            this.big8 ^= (long)((this.hash[this.ii] & 0xFF) << (i2 << 3));
                            ++this.ii;
                        }
                    } else {
                        for (int i3 = 0; i3 < this.mdsize % this.w; ++i3) {
                            this.big8 ^= (long)((this.hash[this.ii] & 0xFF) << (i3 << 3));
                            ++this.ii;
                        }
                    }
                }
                if (this.counter == this.messagesize) {
                    this.big8 = this.checksum;
                }
                this.test = (int)(this.big8 & (long)this.k);
                this.privateKeyOTS = this.gmssRandom.nextSeed(this.seed);
            } else if (this.test > 0) {
                this.messDigestOTS.update(this.privateKeyOTS, 0, this.privateKeyOTS.length);
                this.privateKeyOTS = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(this.privateKeyOTS, 0);
                --this.test;
            }
            if (this.test == 0) {
                System.arraycopy(this.privateKeyOTS, 0, this.sign, this.counter * this.mdsize, this.mdsize);
                this.big8 >>>= this.w;
                ++this.counter;
            }
        } else if (this.w < 57) {
            if (this.test8 == 0L) {
                this.big8 = 0L;
                this.ii = 0;
                int n2 = this.r % 8;
                int n3 = this.r >>> 3;
                if (n3 < this.mdsize) {
                    int n4;
                    if (this.r <= (this.mdsize << 3) - this.w) {
                        this.r += this.w;
                        n4 = this.r + 7 >>> 3;
                    } else {
                        n4 = this.mdsize;
                        this.r += this.w;
                    }
                    for (int i4 = n3; i4 < n4; ++i4) {
                        this.big8 ^= (long)((this.hash[i4] & 0xFF) << (this.ii << 3));
                        ++this.ii;
                    }
                    this.big8 >>>= n2;
                    this.test8 = this.big8 & (long)this.k;
                } else {
                    this.test8 = this.checksum & this.k;
                    this.checksum >>>= this.w;
                }
                this.privateKeyOTS = this.gmssRandom.nextSeed(this.seed);
            } else if (this.test8 > 0L) {
                this.messDigestOTS.update(this.privateKeyOTS, 0, this.privateKeyOTS.length);
                this.privateKeyOTS = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(this.privateKeyOTS, 0);
                --this.test8;
            }
            if (this.test8 == 0L) {
                System.arraycopy(this.privateKeyOTS, 0, this.sign, this.counter * this.mdsize, this.mdsize);
                ++this.counter;
            }
        }
    }

    public int getLog(int n2) {
        int n3 = 1;
        int n4 = 2;
        while (n4 < n2) {
            n4 <<= 1;
            ++n3;
        }
        return n3;
    }

    public byte[][] getStatByte() {
        byte[][] byArray = new byte[5][this.mdsize];
        byArray[0] = this.privateKeyOTS;
        byArray[1] = this.seed;
        byArray[2] = this.hash;
        byArray[3] = this.sign;
        byArray[4] = this.getStatLong();
        return byArray;
    }

    public int[] getStatInt() {
        int[] nArray = new int[]{this.counter, this.test, this.ii, this.r, this.steps, this.keysize, this.height, this.w, this.checksum};
        return nArray;
    }

    public byte[] getStatLong() {
        byte[] byArray = new byte[]{(byte)(this.test8 & 0xFFL), (byte)(this.test8 >> 8 & 0xFFL), (byte)(this.test8 >> 16 & 0xFFL), (byte)(this.test8 >> 24 & 0xFFL), (byte)(this.test8 >> 32 & 0xFFL), (byte)(this.test8 >> 40 & 0xFFL), (byte)(this.test8 >> 48 & 0xFFL), (byte)(this.test8 >> 56 & 0xFFL), (byte)(this.big8 & 0xFFL), (byte)(this.big8 >> 8 & 0xFFL), (byte)(this.big8 >> 16 & 0xFFL), (byte)(this.big8 >> 24 & 0xFFL), (byte)(this.big8 >> 32 & 0xFFL), (byte)(this.big8 >> 40 & 0xFFL), (byte)(this.big8 >> 48 & 0xFFL), (byte)(this.big8 >> 56 & 0xFFL)};
        return byArray;
    }

    public String toString() {
        int n2;
        String string = "" + this.big8 + "  ";
        int[] nArray = new int[9];
        nArray = this.getStatInt();
        byte[][] byArray = new byte[5][this.mdsize];
        byArray = this.getStatByte();
        for (n2 = 0; n2 < 9; ++n2) {
            string = string + nArray[n2] + " ";
        }
        for (n2 = 0; n2 < 5; ++n2) {
            string = string + new String(Hex.encode(byArray[n2])) + " ";
        }
        return string;
    }
}

