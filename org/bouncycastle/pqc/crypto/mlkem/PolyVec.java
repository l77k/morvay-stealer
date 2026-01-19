/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.mlkem;

import org.bouncycastle.pqc.crypto.mlkem.MLKEMEngine;
import org.bouncycastle.pqc.crypto.mlkem.Poly;
import org.bouncycastle.util.Arrays;

class PolyVec {
    Poly[] vec;
    private MLKEMEngine engine;
    private int kyberK;
    private int polyVecBytes;

    public PolyVec(MLKEMEngine mLKEMEngine) {
        this.engine = mLKEMEngine;
        this.kyberK = mLKEMEngine.getKyberK();
        this.polyVecBytes = mLKEMEngine.getKyberPolyVecBytes();
        this.vec = new Poly[this.kyberK];
        for (int i2 = 0; i2 < this.kyberK; ++i2) {
            this.vec[i2] = new Poly(mLKEMEngine);
        }
    }

    public PolyVec() throws Exception {
        throw new Exception("Requires Parameter");
    }

    public Poly getVectorIndex(int n2) {
        return this.vec[n2];
    }

    public void polyVecNtt() {
        for (int i2 = 0; i2 < this.kyberK; ++i2) {
            this.getVectorIndex(i2).polyNtt();
        }
    }

    public void polyVecInverseNttToMont() {
        for (int i2 = 0; i2 < this.kyberK; ++i2) {
            this.getVectorIndex(i2).polyInverseNttToMont();
        }
    }

    public byte[] compressPolyVec() {
        this.conditionalSubQ();
        byte[] byArray = new byte[this.engine.getKyberPolyVecCompressedBytes()];
        int n2 = 0;
        if (this.engine.getKyberPolyVecCompressedBytes() == this.kyberK * 320) {
            short[] sArray = new short[4];
            for (int i2 = 0; i2 < this.kyberK; ++i2) {
                for (int i3 = 0; i3 < 64; ++i3) {
                    for (int i4 = 0; i4 < 4; ++i4) {
                        long l2 = this.getVectorIndex(i2).getCoeffIndex(4 * i3 + i4);
                        l2 <<= 10;
                        l2 += 1665L;
                        l2 *= 1290167L;
                        l2 >>= 32;
                        sArray[i4] = (short)(l2 &= 0x3FFL);
                    }
                    byArray[n2 + 0] = (byte)(sArray[0] >> 0);
                    byArray[n2 + 1] = (byte)(sArray[0] >> 8 | sArray[1] << 2);
                    byArray[n2 + 2] = (byte)(sArray[1] >> 6 | sArray[2] << 4);
                    byArray[n2 + 3] = (byte)(sArray[2] >> 4 | sArray[3] << 6);
                    byArray[n2 + 4] = (byte)(sArray[3] >> 2);
                    n2 += 5;
                }
            }
        } else if (this.engine.getKyberPolyVecCompressedBytes() == this.kyberK * 352) {
            short[] sArray = new short[8];
            for (int i5 = 0; i5 < this.kyberK; ++i5) {
                for (int i6 = 0; i6 < 32; ++i6) {
                    for (int i7 = 0; i7 < 8; ++i7) {
                        long l3 = this.getVectorIndex(i5).getCoeffIndex(8 * i6 + i7);
                        l3 <<= 11;
                        l3 += 1664L;
                        l3 *= 645084L;
                        l3 >>= 31;
                        sArray[i7] = (short)(l3 &= 0x7FFL);
                    }
                    byArray[n2 + 0] = (byte)(sArray[0] >> 0);
                    byArray[n2 + 1] = (byte)(sArray[0] >> 8 | sArray[1] << 3);
                    byArray[n2 + 2] = (byte)(sArray[1] >> 5 | sArray[2] << 6);
                    byArray[n2 + 3] = (byte)(sArray[2] >> 2);
                    byArray[n2 + 4] = (byte)(sArray[2] >> 10 | sArray[3] << 1);
                    byArray[n2 + 5] = (byte)(sArray[3] >> 7 | sArray[4] << 4);
                    byArray[n2 + 6] = (byte)(sArray[4] >> 4 | sArray[5] << 7);
                    byArray[n2 + 7] = (byte)(sArray[5] >> 1);
                    byArray[n2 + 8] = (byte)(sArray[5] >> 9 | sArray[6] << 2);
                    byArray[n2 + 9] = (byte)(sArray[6] >> 6 | sArray[7] << 5);
                    byArray[n2 + 10] = (byte)(sArray[7] >> 3);
                    n2 += 11;
                }
            }
        } else {
            throw new RuntimeException("Kyber PolyVecCompressedBytes neither 320 * KyberK or 352 * KyberK!");
        }
        return byArray;
    }

    public void decompressPolyVec(byte[] byArray) {
        int n2 = 0;
        if (this.engine.getKyberPolyVecCompressedBytes() == this.kyberK * 320) {
            short[] sArray = new short[4];
            for (int i2 = 0; i2 < this.kyberK; ++i2) {
                for (int i3 = 0; i3 < 64; ++i3) {
                    sArray[0] = (short)((byArray[n2] & 0xFF) >> 0 | (short)((byArray[n2 + 1] & 0xFF) << 8));
                    sArray[1] = (short)((byArray[n2 + 1] & 0xFF) >> 2 | (short)((byArray[n2 + 2] & 0xFF) << 6));
                    sArray[2] = (short)((byArray[n2 + 2] & 0xFF) >> 4 | (short)((byArray[n2 + 3] & 0xFF) << 4));
                    sArray[3] = (short)((byArray[n2 + 3] & 0xFF) >> 6 | (short)((byArray[n2 + 4] & 0xFF) << 2));
                    n2 += 5;
                    for (int i4 = 0; i4 < 4; ++i4) {
                        this.vec[i2].setCoeffIndex(4 * i3 + i4, (short)((sArray[i4] & 0x3FF) * 3329 + 512 >> 10));
                    }
                }
            }
        } else if (this.engine.getKyberPolyVecCompressedBytes() == this.kyberK * 352) {
            short[] sArray = new short[8];
            for (int i5 = 0; i5 < this.kyberK; ++i5) {
                for (int i6 = 0; i6 < 32; ++i6) {
                    sArray[0] = (short)((byArray[n2] & 0xFF) >> 0 | (short)(byArray[n2 + 1] & 0xFF) << 8);
                    sArray[1] = (short)((byArray[n2 + 1] & 0xFF) >> 3 | (short)(byArray[n2 + 2] & 0xFF) << 5);
                    sArray[2] = (short)((byArray[n2 + 2] & 0xFF) >> 6 | (short)(byArray[n2 + 3] & 0xFF) << 2 | (short)((byArray[n2 + 4] & 0xFF) << 10));
                    sArray[3] = (short)((byArray[n2 + 4] & 0xFF) >> 1 | (short)(byArray[n2 + 5] & 0xFF) << 7);
                    sArray[4] = (short)((byArray[n2 + 5] & 0xFF) >> 4 | (short)(byArray[n2 + 6] & 0xFF) << 4);
                    sArray[5] = (short)((byArray[n2 + 6] & 0xFF) >> 7 | (short)(byArray[n2 + 7] & 0xFF) << 1 | (short)((byArray[n2 + 8] & 0xFF) << 9));
                    sArray[6] = (short)((byArray[n2 + 8] & 0xFF) >> 2 | (short)(byArray[n2 + 9] & 0xFF) << 6);
                    sArray[7] = (short)((byArray[n2 + 9] & 0xFF) >> 5 | (short)(byArray[n2 + 10] & 0xFF) << 3);
                    n2 += 11;
                    for (int i7 = 0; i7 < 8; ++i7) {
                        this.vec[i5].setCoeffIndex(8 * i6 + i7, (short)((sArray[i7] & 0x7FF) * 3329 + 1024 >> 11));
                    }
                }
            }
        } else {
            throw new RuntimeException("Kyber PolyVecCompressedBytes neither 320 * KyberK or 352 * KyberK!");
        }
    }

    public static void pointwiseAccountMontgomery(Poly poly, PolyVec polyVec, PolyVec polyVec2, MLKEMEngine mLKEMEngine) {
        Poly poly2 = new Poly(mLKEMEngine);
        Poly.baseMultMontgomery(poly, polyVec.getVectorIndex(0), polyVec2.getVectorIndex(0));
        for (int i2 = 1; i2 < mLKEMEngine.getKyberK(); ++i2) {
            Poly.baseMultMontgomery(poly2, polyVec.getVectorIndex(i2), polyVec2.getVectorIndex(i2));
            poly.addCoeffs(poly2);
        }
        poly.reduce();
    }

    public void reducePoly() {
        for (int i2 = 0; i2 < this.kyberK; ++i2) {
            this.getVectorIndex(i2).reduce();
        }
    }

    public void addPoly(PolyVec polyVec) {
        for (int i2 = 0; i2 < this.kyberK; ++i2) {
            this.getVectorIndex(i2).addCoeffs(polyVec.getVectorIndex(i2));
        }
    }

    public byte[] toBytes() {
        byte[] byArray = new byte[this.polyVecBytes];
        for (int i2 = 0; i2 < this.kyberK; ++i2) {
            System.arraycopy(this.vec[i2].toBytes(), 0, byArray, i2 * 384, 384);
        }
        return byArray;
    }

    public void fromBytes(byte[] byArray) {
        for (int i2 = 0; i2 < this.kyberK; ++i2) {
            this.getVectorIndex(i2).fromBytes(Arrays.copyOfRange(byArray, i2 * 384, (i2 + 1) * 384));
        }
    }

    public void conditionalSubQ() {
        for (int i2 = 0; i2 < this.kyberK; ++i2) {
            this.getVectorIndex(i2).conditionalSubQ();
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[");
        for (int i2 = 0; i2 < this.kyberK; ++i2) {
            stringBuffer.append(this.vec[i2].toString());
            if (i2 == this.kyberK - 1) continue;
            stringBuffer.append(", ");
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
}

