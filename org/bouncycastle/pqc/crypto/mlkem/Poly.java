/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.mlkem;

import org.bouncycastle.pqc.crypto.mlkem.CBD;
import org.bouncycastle.pqc.crypto.mlkem.MLKEMEngine;
import org.bouncycastle.pqc.crypto.mlkem.Ntt;
import org.bouncycastle.pqc.crypto.mlkem.Reduce;
import org.bouncycastle.pqc.crypto.mlkem.Symmetric;

class Poly {
    private short[] coeffs = new short[256];
    private MLKEMEngine engine;
    private int polyCompressedBytes;
    private int eta1;
    private int eta2;
    private Symmetric symmetric;

    public Poly(MLKEMEngine mLKEMEngine) {
        this.engine = mLKEMEngine;
        this.polyCompressedBytes = mLKEMEngine.getKyberPolyCompressedBytes();
        this.eta1 = mLKEMEngine.getKyberEta1();
        this.eta2 = MLKEMEngine.getKyberEta2();
        this.symmetric = mLKEMEngine.getSymmetric();
    }

    public short getCoeffIndex(int n2) {
        return this.coeffs[n2];
    }

    public short[] getCoeffs() {
        return this.coeffs;
    }

    public void setCoeffIndex(int n2, short s2) {
        this.coeffs[n2] = s2;
    }

    public void setCoeffs(short[] sArray) {
        this.coeffs = sArray;
    }

    public void polyNtt() {
        this.setCoeffs(Ntt.ntt(this.getCoeffs()));
        this.reduce();
    }

    public void polyInverseNttToMont() {
        this.setCoeffs(Ntt.invNtt(this.getCoeffs()));
    }

    public void reduce() {
        for (int i2 = 0; i2 < 256; ++i2) {
            this.setCoeffIndex(i2, Reduce.barretReduce(this.getCoeffIndex(i2)));
        }
    }

    public static void baseMultMontgomery(Poly poly, Poly poly2, Poly poly3) {
        for (int i2 = 0; i2 < 64; ++i2) {
            Ntt.baseMult(poly, 4 * i2, poly2.getCoeffIndex(4 * i2), poly2.getCoeffIndex(4 * i2 + 1), poly3.getCoeffIndex(4 * i2), poly3.getCoeffIndex(4 * i2 + 1), Ntt.nttZetas[64 + i2]);
            Ntt.baseMult(poly, 4 * i2 + 2, poly2.getCoeffIndex(4 * i2 + 2), poly2.getCoeffIndex(4 * i2 + 3), poly3.getCoeffIndex(4 * i2 + 2), poly3.getCoeffIndex(4 * i2 + 3), (short)(-1 * Ntt.nttZetas[64 + i2]));
        }
    }

    public void addCoeffs(Poly poly) {
        for (int i2 = 0; i2 < 256; ++i2) {
            this.setCoeffIndex(i2, (short)(this.getCoeffIndex(i2) + poly.getCoeffIndex(i2)));
        }
    }

    public void convertToMont() {
        for (int i2 = 0; i2 < 256; ++i2) {
            this.setCoeffIndex(i2, Reduce.montgomeryReduce(this.getCoeffIndex(i2) * 1353));
        }
    }

    public byte[] compressPoly() {
        byte[] byArray = new byte[8];
        byte[] byArray2 = new byte[this.polyCompressedBytes];
        int n2 = 0;
        this.conditionalSubQ();
        if (this.polyCompressedBytes == 128) {
            for (int i2 = 0; i2 < 32; ++i2) {
                for (int i3 = 0; i3 < 8; ++i3) {
                    int n3 = this.getCoeffIndex(8 * i2 + i3);
                    n3 <<= 4;
                    n3 += 1665;
                    n3 *= 80635;
                    n3 >>= 28;
                    byArray[i3] = (byte)(n3 &= 0xF);
                }
                byArray2[n2 + 0] = (byte)(byArray[0] | byArray[1] << 4);
                byArray2[n2 + 1] = (byte)(byArray[2] | byArray[3] << 4);
                byArray2[n2 + 2] = (byte)(byArray[4] | byArray[5] << 4);
                byArray2[n2 + 3] = (byte)(byArray[6] | byArray[7] << 4);
                n2 += 4;
            }
        } else if (this.polyCompressedBytes == 160) {
            for (int i4 = 0; i4 < 32; ++i4) {
                for (int i5 = 0; i5 < 8; ++i5) {
                    int n4 = this.getCoeffIndex(8 * i4 + i5);
                    n4 <<= 5;
                    n4 += 1664;
                    n4 *= 40318;
                    n4 >>= 27;
                    byArray[i5] = (byte)(n4 &= 0x1F);
                }
                byArray2[n2 + 0] = (byte)(byArray[0] >> 0 | byArray[1] << 5);
                byArray2[n2 + 1] = (byte)(byArray[1] >> 3 | byArray[2] << 2 | byArray[3] << 7);
                byArray2[n2 + 2] = (byte)(byArray[3] >> 1 | byArray[4] << 4);
                byArray2[n2 + 3] = (byte)(byArray[4] >> 4 | byArray[5] << 1 | byArray[6] << 6);
                byArray2[n2 + 4] = (byte)(byArray[6] >> 2 | byArray[7] << 3);
                n2 += 5;
            }
        } else {
            throw new RuntimeException("PolyCompressedBytes is neither 128 or 160!");
        }
        return byArray2;
    }

    public void decompressPoly(byte[] byArray) {
        int n2 = 0;
        if (this.engine.getKyberPolyCompressedBytes() == 128) {
            for (int i2 = 0; i2 < 128; ++i2) {
                this.setCoeffIndex(2 * i2 + 0, (short)((short)(byArray[n2] & 0xFF & 0xF) * 3329 + 8 >> 4));
                this.setCoeffIndex(2 * i2 + 1, (short)((short)((byArray[n2] & 0xFF) >> 4) * 3329 + 8 >> 4));
                ++n2;
            }
        } else if (this.engine.getKyberPolyCompressedBytes() == 160) {
            byte[] byArray2 = new byte[8];
            for (int i3 = 0; i3 < 32; ++i3) {
                byArray2[0] = (byte)((byArray[n2 + 0] & 0xFF) >> 0);
                byArray2[1] = (byte)((byArray[n2 + 0] & 0xFF) >> 5 | (byArray[n2 + 1] & 0xFF) << 3);
                byArray2[2] = (byte)((byArray[n2 + 1] & 0xFF) >> 2);
                byArray2[3] = (byte)((byArray[n2 + 1] & 0xFF) >> 7 | (byArray[n2 + 2] & 0xFF) << 1);
                byArray2[4] = (byte)((byArray[n2 + 2] & 0xFF) >> 4 | (byArray[n2 + 3] & 0xFF) << 4);
                byArray2[5] = (byte)((byArray[n2 + 3] & 0xFF) >> 1);
                byArray2[6] = (byte)((byArray[n2 + 3] & 0xFF) >> 6 | (byArray[n2 + 4] & 0xFF) << 2);
                byArray2[7] = (byte)((byArray[n2 + 4] & 0xFF) >> 3);
                n2 += 5;
                for (int i4 = 0; i4 < 8; ++i4) {
                    this.setCoeffIndex(8 * i3 + i4, (short)((byArray2[i4] & 0x1F) * 3329 + 16 >> 5));
                }
            }
        } else {
            throw new RuntimeException("PolyCompressedBytes is neither 128 or 160!");
        }
    }

    public byte[] toBytes() {
        byte[] byArray = new byte[384];
        this.conditionalSubQ();
        for (int i2 = 0; i2 < 128; ++i2) {
            short s2 = this.getCoeffIndex(2 * i2);
            short s3 = this.getCoeffIndex(2 * i2 + 1);
            byArray[3 * i2] = (byte)(s2 >> 0);
            byArray[3 * i2 + 1] = (byte)(s2 >> 8 | s3 << 4);
            byArray[3 * i2 + 2] = (byte)(s3 >> 4);
        }
        return byArray;
    }

    public void fromBytes(byte[] byArray) {
        for (int i2 = 0; i2 < 128; ++i2) {
            this.setCoeffIndex(2 * i2, (short)(((byArray[3 * i2 + 0] & 0xFF) >> 0 | (byArray[3 * i2 + 1] & 0xFF) << 8) & 0xFFF));
            this.setCoeffIndex(2 * i2 + 1, (short)(((long)((byArray[3 * i2 + 1] & 0xFF) >> 4) | (long)((byArray[3 * i2 + 2] & 0xFF) << 4)) & 0xFFFL));
        }
    }

    public byte[] toMsg() {
        int n2 = 832;
        int n3 = 3329 - n2;
        byte[] byArray = new byte[MLKEMEngine.getKyberIndCpaMsgBytes()];
        this.conditionalSubQ();
        for (int i2 = 0; i2 < 32; ++i2) {
            byArray[i2] = 0;
            for (int i3 = 0; i3 < 8; ++i3) {
                short s2 = this.getCoeffIndex(8 * i2 + i3);
                int n4 = (n2 - s2 & s2 - n3) >>> 31;
                int n5 = i2;
                byArray[n5] = (byte)(byArray[n5] | (byte)(n4 << i3));
            }
        }
        return byArray;
    }

    public void fromMsg(byte[] byArray) {
        if (byArray.length != 32) {
            throw new RuntimeException("KYBER_INDCPA_MSGBYTES must be equal to KYBER_N/8 bytes!");
        }
        for (int i2 = 0; i2 < 32; ++i2) {
            for (int i3 = 0; i3 < 8; ++i3) {
                short s2 = (short)(-1 * (short)((byArray[i2] & 0xFF) >> i3 & 1));
                this.setCoeffIndex(8 * i2 + i3, (short)(s2 & 0x681));
            }
        }
    }

    public void conditionalSubQ() {
        for (int i2 = 0; i2 < 256; ++i2) {
            this.setCoeffIndex(i2, Reduce.conditionalSubQ(this.getCoeffIndex(i2)));
        }
    }

    public void getEta1Noise(byte[] byArray, byte by) {
        byte[] byArray2 = new byte[256 * this.eta1 / 4];
        this.symmetric.prf(byArray2, byArray, by);
        CBD.mlkemCBD(this, byArray2, this.eta1);
    }

    public void getEta2Noise(byte[] byArray, byte by) {
        byte[] byArray2 = new byte[256 * this.eta2 / 4];
        this.symmetric.prf(byArray2, byArray, by);
        CBD.mlkemCBD(this, byArray2, this.eta2);
    }

    public void polySubtract(Poly poly) {
        for (int i2 = 0; i2 < 256; ++i2) {
            this.setCoeffIndex(i2, (short)(poly.getCoeffIndex(i2) - this.getCoeffIndex(i2)));
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[");
        for (int i2 = 0; i2 < this.coeffs.length; ++i2) {
            stringBuffer.append(this.coeffs[i2]);
            if (i2 == this.coeffs.length - 1) continue;
            stringBuffer.append(", ");
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
}

