/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.gmss.util;

import org.bouncycastle.crypto.Digest;

public class WinternitzOTSVerify {
    private Digest messDigestOTS;
    private int mdsize;
    private int w;

    public WinternitzOTSVerify(Digest digest, int n2) {
        this.w = n2;
        this.messDigestOTS = digest;
        this.mdsize = this.messDigestOTS.getDigestSize();
    }

    public int getSignatureLength() {
        int n2 = this.messDigestOTS.getDigestSize();
        int n3 = ((n2 << 3) + (this.w - 1)) / this.w;
        int n4 = this.getLog((n3 << this.w) + 1);
        return n2 * (n3 += (n4 + this.w - 1) / this.w);
    }

    public byte[] Verify(byte[] byArray, byte[] byArray2) {
        int n2;
        byte[] byArray3 = new byte[this.mdsize];
        this.messDigestOTS.update(byArray, 0, byArray.length);
        this.messDigestOTS.doFinal(byArray3, 0);
        int n3 = ((this.mdsize << 3) + (this.w - 1)) / this.w;
        int n4 = this.getLog((n3 << this.w) + 1);
        int n5 = n3 + (n4 + this.w - 1) / this.w;
        int n6 = this.mdsize * n5;
        if (n6 != byArray2.length) {
            return null;
        }
        byte[] byArray4 = new byte[n6];
        int n7 = 0;
        int n8 = 0;
        if (8 % this.w == 0) {
            int n9;
            int n10;
            n2 = 8 / this.w;
            int n11 = (1 << this.w) - 1;
            for (n10 = 0; n10 < byArray3.length; ++n10) {
                for (int i2 = 0; i2 < n2; ++i2) {
                    n9 = byArray3[n10] & n11;
                    n7 += n9;
                    this.hashSignatureBlock(byArray2, n8 * this.mdsize, n11 - n9, byArray4, n8 * this.mdsize);
                    byArray3[n10] = (byte)(byArray3[n10] >>> this.w);
                    ++n8;
                }
            }
            n7 = (n3 << this.w) - n7;
            for (n10 = 0; n10 < n4; n10 += this.w) {
                n9 = n7 & n11;
                this.hashSignatureBlock(byArray2, n8 * this.mdsize, n11 - n9, byArray4, n8 * this.mdsize);
                n7 >>>= this.w;
                ++n8;
            }
        } else if (this.w < 8) {
            int n12;
            long l2;
            int n13;
            n2 = this.mdsize / this.w;
            int n14 = (1 << this.w) - 1;
            int n15 = 0;
            for (n13 = 0; n13 < n2; ++n13) {
                int n16;
                l2 = 0L;
                for (n16 = 0; n16 < this.w; ++n16) {
                    l2 ^= (long)((byArray3[n15] & 0xFF) << (n16 << 3));
                    ++n15;
                }
                for (n16 = 0; n16 < 8; ++n16) {
                    n12 = (int)(l2 & (long)n14);
                    n7 += n12;
                    this.hashSignatureBlock(byArray2, n8 * this.mdsize, n14 - n12, byArray4, n8 * this.mdsize);
                    l2 >>>= this.w;
                    ++n8;
                }
            }
            n2 = this.mdsize % this.w;
            l2 = 0L;
            for (n13 = 0; n13 < n2; ++n13) {
                l2 ^= (long)((byArray3[n15] & 0xFF) << (n13 << 3));
                ++n15;
            }
            n2 <<= 3;
            for (n13 = 0; n13 < n2; n13 += this.w) {
                n12 = (int)(l2 & (long)n14);
                n7 += n12;
                this.hashSignatureBlock(byArray2, n8 * this.mdsize, n14 - n12, byArray4, n8 * this.mdsize);
                l2 >>>= this.w;
                ++n8;
            }
            n7 = (n3 << this.w) - n7;
            for (n13 = 0; n13 < n4; n13 += this.w) {
                n12 = n7 & n14;
                this.hashSignatureBlock(byArray2, n8 * this.mdsize, n14 - n12, byArray4, n8 * this.mdsize);
                n7 >>>= this.w;
                ++n8;
            }
        } else if (this.w < 57) {
            long l3;
            int n17;
            int n18;
            long l4;
            int n19;
            int n20;
            n2 = (this.mdsize << 3) - this.w;
            int n21 = (1 << this.w) - 1;
            byte[] byArray5 = new byte[this.mdsize];
            int n22 = 0;
            while (n22 <= n2) {
                n20 = n22 >>> 3;
                n19 = n22 % 8;
                int n23 = (n22 += this.w) + 7 >>> 3;
                l4 = 0L;
                n18 = 0;
                for (n17 = n20; n17 < n23; ++n17) {
                    l4 ^= (long)((byArray3[n17] & 0xFF) << (n18 << 3));
                    ++n18;
                }
                n7 = (int)((long)n7 + l3);
                System.arraycopy(byArray2, n8 * this.mdsize, byArray5, 0, this.mdsize);
                for (l3 = (l4 >>>= n19) & (long)n21; l3 < (long)n21; ++l3) {
                    this.messDigestOTS.update(byArray5, 0, byArray5.length);
                    this.messDigestOTS.doFinal(byArray5, 0);
                }
                System.arraycopy(byArray5, 0, byArray4, n8 * this.mdsize, this.mdsize);
                ++n8;
            }
            n20 = n22 >>> 3;
            if (n20 < this.mdsize) {
                n19 = n22 % 8;
                l4 = 0L;
                n18 = 0;
                for (n17 = n20; n17 < this.mdsize; ++n17) {
                    l4 ^= (long)((byArray3[n17] & 0xFF) << (n18 << 3));
                    ++n18;
                }
                n7 = (int)((long)n7 + l3);
                System.arraycopy(byArray2, n8 * this.mdsize, byArray5, 0, this.mdsize);
                for (l3 = (l4 >>>= n19) & (long)n21; l3 < (long)n21; ++l3) {
                    this.messDigestOTS.update(byArray5, 0, byArray5.length);
                    this.messDigestOTS.doFinal(byArray5, 0);
                }
                System.arraycopy(byArray5, 0, byArray4, n8 * this.mdsize, this.mdsize);
                ++n8;
            }
            n7 = (n3 << this.w) - n7;
            for (n17 = 0; n17 < n4; n17 += this.w) {
                System.arraycopy(byArray2, n8 * this.mdsize, byArray5, 0, this.mdsize);
                for (l3 = (long)(n7 & n21); l3 < (long)n21; ++l3) {
                    this.messDigestOTS.update(byArray5, 0, byArray5.length);
                    this.messDigestOTS.doFinal(byArray5, 0);
                }
                System.arraycopy(byArray5, 0, byArray4, n8 * this.mdsize, this.mdsize);
                n7 >>>= this.w;
                ++n8;
            }
        }
        this.messDigestOTS.update(byArray4, 0, byArray4.length);
        byte[] byArray6 = new byte[this.mdsize];
        this.messDigestOTS.doFinal(byArray6, 0);
        return byArray6;
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

    private void hashSignatureBlock(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) {
        if (n3 < 1) {
            System.arraycopy(byArray, n2, byArray2, n4, this.mdsize);
        } else {
            this.messDigestOTS.update(byArray, n2, this.mdsize);
            this.messDigestOTS.doFinal(byArray2, n4);
            while (--n3 > 0) {
                this.messDigestOTS.update(byArray2, n4, this.mdsize);
                this.messDigestOTS.doFinal(byArray2, n4);
            }
        }
    }
}

