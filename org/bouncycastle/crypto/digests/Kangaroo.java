/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicePurpose;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.Xof;
import org.bouncycastle.crypto.digests.Utils;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public final class Kangaroo {
    private static final int DIGESTLEN = 32;

    static abstract class KangarooBase
    implements ExtendedDigest,
    Xof {
        private static final int BLKSIZE = 8192;
        private static final byte[] SINGLE = new byte[]{7};
        private static final byte[] INTERMEDIATE = new byte[]{11};
        private static final byte[] FINAL = new byte[]{-1, -1, 6};
        private static final byte[] FIRST = new byte[]{3, 0, 0, 0, 0, 0, 0, 0};
        private final byte[] singleByte = new byte[1];
        private final KangarooSponge theTree;
        private final KangarooSponge theLeaf;
        private final int theChainLen;
        private byte[] thePersonal;
        private boolean squeezing;
        private int theCurrNode;
        private int theProcessed;
        private final CryptoServicePurpose purpose;

        KangarooBase(int n2, int n3, int n4, CryptoServicePurpose cryptoServicePurpose) {
            this.theTree = new KangarooSponge(n2, n3);
            this.theLeaf = new KangarooSponge(n2, n3);
            this.theChainLen = n2 >> 2;
            this.buildPersonal(null);
            this.purpose = cryptoServicePurpose;
            CryptoServicesRegistrar.checkConstraints(Utils.getDefaultProperties(this, n2, cryptoServicePurpose));
        }

        private void buildPersonal(byte[] byArray) {
            int n2 = byArray == null ? 0 : byArray.length;
            byte[] byArray2 = KangarooBase.lengthEncode(n2);
            this.thePersonal = byArray == null ? new byte[n2 + byArray2.length] : Arrays.copyOf(byArray, n2 + byArray2.length);
            System.arraycopy(byArray2, 0, this.thePersonal, n2, byArray2.length);
        }

        @Override
        public int getByteLength() {
            return this.theTree.theRateBytes;
        }

        @Override
        public int getDigestSize() {
            return this.theChainLen >> 1;
        }

        public void init(KangarooParameters kangarooParameters) {
            this.buildPersonal(kangarooParameters.getPersonalisation());
            this.reset();
        }

        @Override
        public void update(byte by) {
            this.singleByte[0] = by;
            this.update(this.singleByte, 0, 1);
        }

        @Override
        public void update(byte[] byArray, int n2, int n3) {
            this.processData(byArray, n2, n3);
        }

        @Override
        public int doFinal(byte[] byArray, int n2) {
            return this.doFinal(byArray, n2, this.getDigestSize());
        }

        @Override
        public int doFinal(byte[] byArray, int n2, int n3) {
            if (this.squeezing) {
                throw new IllegalStateException("Already outputting");
            }
            int n4 = this.doOutput(byArray, n2, n3);
            this.reset();
            return n4;
        }

        @Override
        public int doOutput(byte[] byArray, int n2, int n3) {
            if (!this.squeezing) {
                this.switchToSqueezing();
            }
            if (n3 < 0) {
                throw new IllegalArgumentException("Invalid output length");
            }
            this.theTree.squeeze(byArray, n2, n3);
            return n3;
        }

        private void processData(byte[] byArray, int n2, int n3) {
            int n4;
            if (this.squeezing) {
                throw new IllegalStateException("attempt to absorb while squeezing");
            }
            KangarooSponge kangarooSponge = this.theCurrNode == 0 ? this.theTree : this.theLeaf;
            int n5 = 8192 - this.theProcessed;
            if (n5 >= n3) {
                kangarooSponge.absorb(byArray, n2, n3);
                this.theProcessed += n3;
                return;
            }
            if (n5 > 0) {
                kangarooSponge.absorb(byArray, n2, n5);
                this.theProcessed += n5;
            }
            for (int i2 = n5; i2 < n3; i2 += n4) {
                if (this.theProcessed == 8192) {
                    this.switchLeaf(true);
                }
                n4 = Math.min(n3 - i2, 8192);
                this.theLeaf.absorb(byArray, n2 + i2, n4);
                this.theProcessed += n4;
            }
        }

        @Override
        public void reset() {
            this.theTree.initSponge();
            this.theLeaf.initSponge();
            this.theCurrNode = 0;
            this.theProcessed = 0;
            this.squeezing = false;
        }

        private void switchLeaf(boolean bl) {
            if (this.theCurrNode == 0) {
                this.theTree.absorb(KangarooBase.FIRST, 0, KangarooBase.FIRST.length);
            } else {
                this.theLeaf.absorb(KangarooBase.INTERMEDIATE, 0, KangarooBase.INTERMEDIATE.length);
                byte[] byArray = new byte[this.theChainLen];
                this.theLeaf.squeeze(byArray, 0, this.theChainLen);
                this.theTree.absorb(byArray, 0, this.theChainLen);
                this.theLeaf.initSponge();
            }
            if (bl) {
                ++this.theCurrNode;
            }
            this.theProcessed = 0;
        }

        private void switchToSqueezing() {
            this.processData(this.thePersonal, 0, this.thePersonal.length);
            if (this.theCurrNode == 0) {
                this.switchSingle();
            } else {
                this.switchFinal();
            }
        }

        private void switchSingle() {
            this.theTree.absorb(KangarooBase.SINGLE, 0, 1);
            this.theTree.padAndSwitchToSqueezingPhase();
        }

        private void switchFinal() {
            this.switchLeaf(false);
            byte[] byArray = KangarooBase.lengthEncode(this.theCurrNode);
            this.theTree.absorb(byArray, 0, byArray.length);
            this.theTree.absorb(KangarooBase.FINAL, 0, KangarooBase.FINAL.length);
            this.theTree.padAndSwitchToSqueezingPhase();
        }

        private static byte[] lengthEncode(long l2) {
            int n2 = 0;
            long l3 = l2;
            if (l3 != 0L) {
                n2 = 1;
                while ((l3 >>= 8) != 0L) {
                    n2 = (byte)(n2 + 1);
                }
            }
            byte[] byArray = new byte[n2 + 1];
            byArray[n2] = n2;
            for (int i2 = 0; i2 < n2; ++i2) {
                byArray[i2] = (byte)(l2 >> 8 * (n2 - i2 - 1));
            }
            return byArray;
        }
    }

    public static class KangarooParameters
    implements CipherParameters {
        private byte[] thePersonal;

        public byte[] getPersonalisation() {
            return Arrays.clone(this.thePersonal);
        }

        static /* synthetic */ byte[] access$002(KangarooParameters kangarooParameters, byte[] byArray) {
            kangarooParameters.thePersonal = byArray;
            return byArray;
        }

        public static class Builder {
            private byte[] thePersonal;

            public Builder setPersonalisation(byte[] byArray) {
                this.thePersonal = Arrays.clone(byArray);
                return this;
            }

            public KangarooParameters build() {
                KangarooParameters kangarooParameters = new KangarooParameters();
                if (this.thePersonal != null) {
                    KangarooParameters.access$002(kangarooParameters, this.thePersonal);
                }
                return kangarooParameters;
            }
        }
    }

    private static class KangarooSponge {
        private static long[] KeccakRoundConstants = new long[]{1L, 32898L, -9223372036854742902L, -9223372034707259392L, 32907L, 0x80000001L, -9223372034707259263L, -9223372036854743031L, 138L, 136L, 0x80008009L, 0x8000000AL, 0x8000808BL, -9223372036854775669L, -9223372036854742903L, -9223372036854743037L, -9223372036854743038L, -9223372036854775680L, 32778L, -9223372034707292150L, -9223372034707259263L, -9223372036854742912L, 0x80000001L, -9223372034707259384L};
        private final int theRounds;
        private final int theRateBytes;
        private final long[] theState = new long[25];
        private final byte[] theQueue;
        private int bytesInQueue;
        private boolean squeezing;

        KangarooSponge(int n2, int n3) {
            this.theRateBytes = 1600 - (n2 << 1) >> 3;
            this.theRounds = n3;
            this.theQueue = new byte[this.theRateBytes];
            this.initSponge();
        }

        private void initSponge() {
            Arrays.fill(this.theState, 0L);
            Arrays.fill(this.theQueue, (byte)0);
            this.bytesInQueue = 0;
            this.squeezing = false;
        }

        private void absorb(byte[] byArray, int n2, int n3) {
            if (this.squeezing) {
                throw new IllegalStateException("attempt to absorb while squeezing");
            }
            int n4 = 0;
            while (n4 < n3) {
                if (this.bytesInQueue == 0 && n4 <= n3 - this.theRateBytes) {
                    do {
                        this.KangarooAbsorb(byArray, n2 + n4);
                    } while ((n4 += this.theRateBytes) <= n3 - this.theRateBytes);
                    continue;
                }
                int n5 = Math.min(this.theRateBytes - this.bytesInQueue, n3 - n4);
                System.arraycopy(byArray, n2 + n4, this.theQueue, this.bytesInQueue, n5);
                this.bytesInQueue += n5;
                n4 += n5;
                if (this.bytesInQueue != this.theRateBytes) continue;
                this.KangarooAbsorb(this.theQueue, 0);
                this.bytesInQueue = 0;
            }
        }

        private void padAndSwitchToSqueezingPhase() {
            for (int i2 = this.bytesInQueue; i2 < this.theRateBytes; ++i2) {
                this.theQueue[i2] = 0;
            }
            int n2 = this.theRateBytes - 1;
            this.theQueue[n2] = (byte)(this.theQueue[n2] ^ 0x80);
            this.KangarooAbsorb(this.theQueue, 0);
            this.KangarooExtract();
            this.bytesInQueue = this.theRateBytes;
            this.squeezing = true;
        }

        private void squeeze(byte[] byArray, int n2, int n3) {
            int n4;
            if (!this.squeezing) {
                this.padAndSwitchToSqueezingPhase();
            }
            for (int i2 = 0; i2 < n3; i2 += n4) {
                if (this.bytesInQueue == 0) {
                    this.KangarooPermutation();
                    this.KangarooExtract();
                    this.bytesInQueue = this.theRateBytes;
                }
                n4 = Math.min(this.bytesInQueue, n3 - i2);
                System.arraycopy(this.theQueue, this.theRateBytes - this.bytesInQueue, byArray, n2 + i2, n4);
                this.bytesInQueue -= n4;
            }
        }

        private void KangarooAbsorb(byte[] byArray, int n2) {
            int n3 = this.theRateBytes >> 3;
            int n4 = n2;
            int n5 = 0;
            while (n5 < n3) {
                int n6 = n5++;
                this.theState[n6] = this.theState[n6] ^ Pack.littleEndianToLong(byArray, n4);
                n4 += 8;
            }
            this.KangarooPermutation();
        }

        private void KangarooExtract() {
            Pack.longToLittleEndian(this.theState, 0, this.theRateBytes >> 3, this.theQueue, 0);
        }

        private void KangarooPermutation() {
            long[] lArray = this.theState;
            long l2 = lArray[0];
            long l3 = lArray[1];
            long l4 = lArray[2];
            long l5 = lArray[3];
            long l6 = lArray[4];
            long l7 = lArray[5];
            long l8 = lArray[6];
            long l9 = lArray[7];
            long l10 = lArray[8];
            long l11 = lArray[9];
            long l12 = lArray[10];
            long l13 = lArray[11];
            long l14 = lArray[12];
            long l15 = lArray[13];
            long l16 = lArray[14];
            long l17 = lArray[15];
            long l18 = lArray[16];
            long l19 = lArray[17];
            long l20 = lArray[18];
            long l21 = lArray[19];
            long l22 = lArray[20];
            long l23 = lArray[21];
            long l24 = lArray[22];
            long l25 = lArray[23];
            long l26 = lArray[24];
            int n2 = KeccakRoundConstants.length - this.theRounds;
            for (int i2 = 0; i2 < this.theRounds; ++i2) {
                long l27 = l2 ^ l7 ^ l12 ^ l17 ^ l22;
                long l28 = l3 ^ l8 ^ l13 ^ l18 ^ l23;
                long l29 = l4 ^ l9 ^ l14 ^ l19 ^ l24;
                long l30 = l5 ^ l10 ^ l15 ^ l20 ^ l25;
                long l31 = l6 ^ l11 ^ l16 ^ l21 ^ l26;
                long l32 = (l28 << 1 | l28 >>> -1) ^ l31;
                long l33 = (l29 << 1 | l29 >>> -1) ^ l27;
                long l34 = (l30 << 1 | l30 >>> -1) ^ l28;
                long l35 = (l31 << 1 | l31 >>> -1) ^ l29;
                long l36 = (l27 << 1 | l27 >>> -1) ^ l30;
                l2 ^= l32;
                l7 ^= l32;
                l12 ^= l32;
                l17 ^= l32;
                l22 ^= l32;
                l3 ^= l33;
                l8 ^= l33;
                l13 ^= l33;
                l18 ^= l33;
                l23 ^= l33;
                l4 ^= l34;
                l9 ^= l34;
                l14 ^= l34;
                l19 ^= l34;
                l24 ^= l34;
                l5 ^= l35;
                l10 ^= l35;
                l15 ^= l35;
                l20 ^= l35;
                l25 ^= l35;
                l6 ^= l36;
                l11 ^= l36;
                l16 ^= l36;
                l21 ^= l36;
                l26 ^= l36;
                l28 = l3 << 1 | l3 >>> 63;
                l3 = l8 << 44 | l8 >>> 20;
                l8 = l11 << 20 | l11 >>> 44;
                l11 = l24 << 61 | l24 >>> 3;
                l24 = l16 << 39 | l16 >>> 25;
                l16 = l22 << 18 | l22 >>> 46;
                l22 = l4 << 62 | l4 >>> 2;
                l4 = l14 << 43 | l14 >>> 21;
                l14 = l15 << 25 | l15 >>> 39;
                l15 = l21 << 8 | l21 >>> 56;
                l21 = l25 << 56 | l25 >>> 8;
                l25 = l17 << 41 | l17 >>> 23;
                l17 = l6 << 27 | l6 >>> 37;
                l6 = l26 << 14 | l26 >>> 50;
                l26 = l23 << 2 | l23 >>> 62;
                l23 = l10 << 55 | l10 >>> 9;
                l10 = l18 << 45 | l18 >>> 19;
                l18 = l7 << 36 | l7 >>> 28;
                l7 = l5 << 28 | l5 >>> 36;
                l5 = l20 << 21 | l20 >>> 43;
                l20 = l19 << 15 | l19 >>> 49;
                l19 = l13 << 10 | l13 >>> 54;
                l13 = l9 << 6 | l9 >>> 58;
                l9 = l12 << 3 | l12 >>> 61;
                l12 = l28;
                l27 = l2 ^ (l3 ^ 0xFFFFFFFFFFFFFFFFL) & l4;
                l28 = l3 ^ (l4 ^ 0xFFFFFFFFFFFFFFFFL) & l5;
                l4 ^= (l5 ^ 0xFFFFFFFFFFFFFFFFL) & l6;
                l5 ^= (l6 ^ 0xFFFFFFFFFFFFFFFFL) & l2;
                l6 ^= (l2 ^ 0xFFFFFFFFFFFFFFFFL) & l3;
                l2 = l27;
                l3 = l28;
                l27 = l7 ^ (l8 ^ 0xFFFFFFFFFFFFFFFFL) & l9;
                l28 = l8 ^ (l9 ^ 0xFFFFFFFFFFFFFFFFL) & l10;
                l9 ^= (l10 ^ 0xFFFFFFFFFFFFFFFFL) & l11;
                l10 ^= (l11 ^ 0xFFFFFFFFFFFFFFFFL) & l7;
                l11 ^= (l7 ^ 0xFFFFFFFFFFFFFFFFL) & l8;
                l7 = l27;
                l8 = l28;
                l27 = l12 ^ (l13 ^ 0xFFFFFFFFFFFFFFFFL) & l14;
                l28 = l13 ^ (l14 ^ 0xFFFFFFFFFFFFFFFFL) & l15;
                l14 ^= (l15 ^ 0xFFFFFFFFFFFFFFFFL) & l16;
                l15 ^= (l16 ^ 0xFFFFFFFFFFFFFFFFL) & l12;
                l16 ^= (l12 ^ 0xFFFFFFFFFFFFFFFFL) & l13;
                l12 = l27;
                l13 = l28;
                l27 = l17 ^ (l18 ^ 0xFFFFFFFFFFFFFFFFL) & l19;
                l28 = l18 ^ (l19 ^ 0xFFFFFFFFFFFFFFFFL) & l20;
                l19 ^= (l20 ^ 0xFFFFFFFFFFFFFFFFL) & l21;
                l20 ^= (l21 ^ 0xFFFFFFFFFFFFFFFFL) & l17;
                l21 ^= (l17 ^ 0xFFFFFFFFFFFFFFFFL) & l18;
                l17 = l27;
                l18 = l28;
                l27 = l22 ^ (l23 ^ 0xFFFFFFFFFFFFFFFFL) & l24;
                l28 = l23 ^ (l24 ^ 0xFFFFFFFFFFFFFFFFL) & l25;
                l24 ^= (l25 ^ 0xFFFFFFFFFFFFFFFFL) & l26;
                l25 ^= (l26 ^ 0xFFFFFFFFFFFFFFFFL) & l22;
                l26 ^= (l22 ^ 0xFFFFFFFFFFFFFFFFL) & l23;
                l22 = l27;
                l23 = l28;
                l2 ^= KeccakRoundConstants[n2 + i2];
            }
            lArray[0] = l2;
            lArray[1] = l3;
            lArray[2] = l4;
            lArray[3] = l5;
            lArray[4] = l6;
            lArray[5] = l7;
            lArray[6] = l8;
            lArray[7] = l9;
            lArray[8] = l10;
            lArray[9] = l11;
            lArray[10] = l12;
            lArray[11] = l13;
            lArray[12] = l14;
            lArray[13] = l15;
            lArray[14] = l16;
            lArray[15] = l17;
            lArray[16] = l18;
            lArray[17] = l19;
            lArray[18] = l20;
            lArray[19] = l21;
            lArray[20] = l22;
            lArray[21] = l23;
            lArray[22] = l24;
            lArray[23] = l25;
            lArray[24] = l26;
        }
    }

    public static class KangarooTwelve
    extends KangarooBase {
        public KangarooTwelve() {
            this(32, CryptoServicePurpose.ANY);
        }

        public KangarooTwelve(int n2, CryptoServicePurpose cryptoServicePurpose) {
            super(128, 12, n2, cryptoServicePurpose);
        }

        public KangarooTwelve(CryptoServicePurpose cryptoServicePurpose) {
            this(32, cryptoServicePurpose);
        }

        @Override
        public String getAlgorithmName() {
            return "KangarooTwelve";
        }
    }

    public static class MarsupilamiFourteen
    extends KangarooBase {
        public MarsupilamiFourteen() {
            this(32, CryptoServicePurpose.ANY);
        }

        public MarsupilamiFourteen(int n2, CryptoServicePurpose cryptoServicePurpose) {
            super(256, 14, n2, cryptoServicePurpose);
        }

        public MarsupilamiFourteen(CryptoServicePurpose cryptoServicePurpose) {
            this(32, cryptoServicePurpose);
        }

        @Override
        public String getAlgorithmName() {
            return "MarsupilamiFourteen";
        }
    }
}

