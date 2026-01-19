/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.gmss.util;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.legacy.crypto.gmss.util.GMSSRandom;

public class WinternitzOTSignature {
    private Digest messDigestOTS;
    private int mdsize;
    private int keysize;
    private byte[][] privateKeyOTS;
    private int w;
    private GMSSRandom gmssRandom;
    private int messagesize;
    private int checksumsize;

    public WinternitzOTSignature(byte[] byArray, Digest digest, int n2) {
        this.w = n2;
        this.messDigestOTS = digest;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.mdsize = this.messDigestOTS.getDigestSize();
        this.messagesize = ((this.mdsize << 3) + n2 - 1) / n2;
        this.checksumsize = this.getLog((this.messagesize << n2) + 1);
        this.keysize = this.messagesize + (this.checksumsize + n2 - 1) / n2;
        this.privateKeyOTS = new byte[this.keysize][];
        byte[] byArray2 = new byte[this.mdsize];
        System.arraycopy(byArray, 0, byArray2, 0, byArray2.length);
        for (int i2 = 0; i2 < this.keysize; ++i2) {
            this.privateKeyOTS[i2] = this.gmssRandom.nextSeed(byArray2);
        }
    }

    public byte[][] getPrivateKey() {
        return this.privateKeyOTS;
    }

    public byte[] getPublicKey() {
        byte[] byArray = new byte[this.keysize * this.mdsize];
        int n2 = 0;
        int n3 = (1 << this.w) - 1;
        for (int i2 = 0; i2 < this.keysize; ++i2) {
            this.hashPrivateKeyBlock(i2, n3, byArray, n2);
            n2 += this.mdsize;
        }
        this.messDigestOTS.update(byArray, 0, byArray.length);
        byte[] byArray2 = new byte[this.mdsize];
        this.messDigestOTS.doFinal(byArray2, 0);
        return byArray2;
    }

    public byte[] getSignature(byte[] byArray) {
        byte[] byArray2;
        block18: {
            long l2;
            int n2;
            int n3;
            long l3;
            int n4;
            int n5;
            int n6;
            int n7;
            byte[] byArray3;
            block19: {
                long l4;
                int n8;
                int n9;
                block17: {
                    int n10;
                    byArray2 = new byte[this.keysize * this.mdsize];
                    byArray3 = new byte[this.mdsize];
                    n7 = 0;
                    n6 = 0;
                    n9 = 0;
                    this.messDigestOTS.update(byArray, 0, byArray.length);
                    this.messDigestOTS.doFinal(byArray3, 0);
                    if (8 % this.w != 0) break block17;
                    int n11 = 8 / this.w;
                    int n12 = (1 << this.w) - 1;
                    for (n10 = 0; n10 < byArray3.length; ++n10) {
                        for (int i2 = 0; i2 < n11; ++i2) {
                            n9 = byArray3[n10] & n12;
                            n6 += n9;
                            this.hashPrivateKeyBlock(n7, n9, byArray2, n7 * this.mdsize);
                            byArray3[n10] = (byte)(byArray3[n10] >>> this.w);
                            ++n7;
                        }
                    }
                    n6 = (this.messagesize << this.w) - n6;
                    for (n10 = 0; n10 < this.checksumsize; n10 += this.w) {
                        n9 = n6 & n12;
                        this.hashPrivateKeyBlock(n7, n9, byArray2, n7 * this.mdsize);
                        n6 >>>= this.w;
                        ++n7;
                    }
                    break block18;
                }
                if (this.w >= 8) break block19;
                int n13 = this.mdsize / this.w;
                int n14 = (1 << this.w) - 1;
                int n15 = 0;
                for (n8 = 0; n8 < n13; ++n8) {
                    int n16;
                    l4 = 0L;
                    for (n16 = 0; n16 < this.w; ++n16) {
                        l4 ^= (long)((byArray3[n15] & 0xFF) << (n16 << 3));
                        ++n15;
                    }
                    for (n16 = 0; n16 < 8; ++n16) {
                        n9 = (int)l4 & n14;
                        n6 += n9;
                        this.hashPrivateKeyBlock(n7, n9, byArray2, n7 * this.mdsize);
                        l4 >>>= this.w;
                        ++n7;
                    }
                }
                n13 = this.mdsize % this.w;
                l4 = 0L;
                for (n8 = 0; n8 < n13; ++n8) {
                    l4 ^= (long)((byArray3[n15] & 0xFF) << (n8 << 3));
                    ++n15;
                }
                n13 <<= 3;
                for (n8 = 0; n8 < n13; n8 += this.w) {
                    n9 = (int)l4 & n14;
                    n6 += n9;
                    this.hashPrivateKeyBlock(n7, n9, byArray2, n7 * this.mdsize);
                    l4 >>>= this.w;
                    ++n7;
                }
                n6 = (this.messagesize << this.w) - n6;
                for (n8 = 0; n8 < this.checksumsize; n8 += this.w) {
                    n9 = n6 & n14;
                    this.hashPrivateKeyBlock(n7, n9, byArray2, n7 * this.mdsize);
                    n6 >>>= this.w;
                    ++n7;
                }
                break block18;
            }
            if (this.w >= 57) break block18;
            int n17 = (this.mdsize << 3) - this.w;
            int n18 = (1 << this.w) - 1;
            byte[] byArray4 = new byte[this.mdsize];
            int n19 = 0;
            while (n19 <= n17) {
                n5 = n19 >>> 3;
                n4 = n19 % 8;
                int n20 = (n19 += this.w) + 7 >>> 3;
                l3 = 0L;
                n3 = 0;
                for (n2 = n5; n2 < n20; ++n2) {
                    l3 ^= (long)((byArray3[n2] & 0xFF) << (n3 << 3));
                    ++n3;
                }
                n6 = (int)((long)n6 + l2);
                System.arraycopy(this.privateKeyOTS[n7], 0, byArray4, 0, this.mdsize);
                for (l2 = (l3 >>>= n4) & (long)n18; l2 > 0L; --l2) {
                    this.messDigestOTS.update(byArray4, 0, byArray4.length);
                    this.messDigestOTS.doFinal(byArray4, 0);
                }
                System.arraycopy(byArray4, 0, byArray2, n7 * this.mdsize, this.mdsize);
                ++n7;
            }
            n5 = n19 >>> 3;
            if (n5 < this.mdsize) {
                n4 = n19 % 8;
                l3 = 0L;
                n3 = 0;
                for (n2 = n5; n2 < this.mdsize; ++n2) {
                    l3 ^= (long)((byArray3[n2] & 0xFF) << (n3 << 3));
                    ++n3;
                }
                n6 = (int)((long)n6 + l2);
                System.arraycopy(this.privateKeyOTS[n7], 0, byArray4, 0, this.mdsize);
                for (l2 = (l3 >>>= n4) & (long)n18; l2 > 0L; --l2) {
                    this.messDigestOTS.update(byArray4, 0, byArray4.length);
                    this.messDigestOTS.doFinal(byArray4, 0);
                }
                System.arraycopy(byArray4, 0, byArray2, n7 * this.mdsize, this.mdsize);
                ++n7;
            }
            n6 = (this.messagesize << this.w) - n6;
            for (n2 = 0; n2 < this.checksumsize; n2 += this.w) {
                System.arraycopy(this.privateKeyOTS[n7], 0, byArray4, 0, this.mdsize);
                for (l2 = (long)(n6 & n18); l2 > 0L; --l2) {
                    this.messDigestOTS.update(byArray4, 0, byArray4.length);
                    this.messDigestOTS.doFinal(byArray4, 0);
                }
                System.arraycopy(byArray4, 0, byArray2, n7 * this.mdsize, this.mdsize);
                n6 >>>= this.w;
                ++n7;
            }
        }
        return byArray2;
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

    private void hashPrivateKeyBlock(int n2, int n3, byte[] byArray, int n4) {
        if (n3 < 1) {
            System.arraycopy(this.privateKeyOTS[n2], 0, byArray, n4, this.mdsize);
        } else {
            this.messDigestOTS.update(this.privateKeyOTS[n2], 0, this.mdsize);
            this.messDigestOTS.doFinal(byArray, n4);
            while (--n3 > 0) {
                this.messDigestOTS.update(byArray, n4, this.mdsize);
                this.messDigestOTS.doFinal(byArray, n4);
            }
        }
    }
}

