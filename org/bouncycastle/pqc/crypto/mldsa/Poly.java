/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.mldsa;

import org.bouncycastle.crypto.digests.SHAKEDigest;
import org.bouncycastle.pqc.crypto.mldsa.MLDSAEngine;
import org.bouncycastle.pqc.crypto.mldsa.Ntt;
import org.bouncycastle.pqc.crypto.mldsa.PolyVecL;
import org.bouncycastle.pqc.crypto.mldsa.Reduce;
import org.bouncycastle.pqc.crypto.mldsa.Rounding;
import org.bouncycastle.pqc.crypto.mldsa.Symmetric;

class Poly {
    private final int polyUniformNBlocks;
    private int[] coeffs;
    private final MLDSAEngine engine;
    private final int dilithiumN;
    private final Symmetric symmetric;

    public Poly(MLDSAEngine mLDSAEngine) {
        this.dilithiumN = 256;
        this.coeffs = new int[this.dilithiumN];
        this.engine = mLDSAEngine;
        this.symmetric = mLDSAEngine.GetSymmetric();
        this.polyUniformNBlocks = (768 + this.symmetric.stream128BlockBytes - 1) / this.symmetric.stream128BlockBytes;
    }

    public int getCoeffIndex(int n2) {
        return this.coeffs[n2];
    }

    public int[] getCoeffs() {
        return this.coeffs;
    }

    public void setCoeffIndex(int n2, int n3) {
        this.coeffs[n2] = n3;
    }

    public void setCoeffs(int[] nArray) {
        this.coeffs = nArray;
    }

    public void uniformBlocks(byte[] byArray, short s2) {
        int n2 = this.polyUniformNBlocks * this.symmetric.stream128BlockBytes;
        byte[] byArray2 = new byte[n2 + 2];
        this.symmetric.stream128init(byArray, s2);
        this.symmetric.stream128squeezeBlocks(byArray2, 0, n2);
        for (int i2 = Poly.rejectUniform(this, 0, this.dilithiumN, byArray2, n2); i2 < this.dilithiumN; i2 += Poly.rejectUniform(this, i2, this.dilithiumN - i2, byArray2, n2)) {
            int n3 = n2 % 3;
            for (int i3 = 0; i3 < n3; ++i3) {
                byArray2[i3] = byArray2[n2 - n3 + i3];
            }
            this.symmetric.stream128squeezeBlocks(byArray2, n3, this.symmetric.stream128BlockBytes);
            n2 = this.symmetric.stream128BlockBytes + n3;
        }
    }

    private static int rejectUniform(Poly poly, int n2, int n3, byte[] byArray, int n4) {
        int n5 = 0;
        int n6 = 0;
        while (n6 < n3 && n5 + 3 <= n4) {
            int n7 = byArray[n5++] & 0xFF;
            n7 |= (byArray[n5++] & 0xFF) << 8;
            n7 |= (byArray[n5++] & 0xFF) << 16;
            if ((n7 &= 0x7FFFFF) >= 8380417) continue;
            poly.setCoeffIndex(n2 + n6, n7);
            ++n6;
        }
        return n6;
    }

    public void uniformEta(byte[] byArray, short s2) {
        int n2;
        int n3 = this.engine.getDilithiumEta();
        if (this.engine.getDilithiumEta() == 2) {
            n2 = (136 + this.symmetric.stream256BlockBytes - 1) / this.symmetric.stream256BlockBytes;
        } else if (this.engine.getDilithiumEta() == 4) {
            n2 = (227 + this.symmetric.stream256BlockBytes - 1) / this.symmetric.stream256BlockBytes;
        } else {
            throw new RuntimeException("Wrong Dilithium Eta!");
        }
        int n4 = n2 * this.symmetric.stream256BlockBytes;
        byte[] byArray2 = new byte[n4];
        this.symmetric.stream256init(byArray, s2);
        this.symmetric.stream256squeezeBlocks(byArray2, 0, n4);
        for (int i2 = Poly.rejectEta(this, 0, this.dilithiumN, byArray2, n4, n3); i2 < 256; i2 += Poly.rejectEta(this, i2, this.dilithiumN - i2, byArray2, this.symmetric.stream256BlockBytes, n3)) {
            this.symmetric.stream256squeezeBlocks(byArray2, 0, this.symmetric.stream256BlockBytes);
        }
    }

    private static int rejectEta(Poly poly, int n2, int n3, byte[] byArray, int n4, int n5) {
        int n6 = 0;
        int n7 = 0;
        while (n7 < n3 && n6 < n4) {
            int n8 = byArray[n6] & 0xFF & 0xF;
            int n9 = (byArray[n6++] & 0xFF) >> 4;
            if (n5 == 2) {
                if (n8 < 15) {
                    n8 -= (205 * n8 >> 10) * 5;
                    poly.setCoeffIndex(n2 + n7, 2 - n8);
                    ++n7;
                }
                if (n9 >= 15 || n7 >= n3) continue;
                n9 -= (205 * n9 >> 10) * 5;
                poly.setCoeffIndex(n2 + n7, 2 - n9);
                ++n7;
                continue;
            }
            if (n5 != 4) continue;
            if (n8 < 9) {
                poly.setCoeffIndex(n2 + n7, 4 - n8);
                ++n7;
            }
            if (n9 >= 9 || n7 >= n3) continue;
            poly.setCoeffIndex(n2 + n7, 4 - n9);
            ++n7;
        }
        return n7;
    }

    public void polyNtt() {
        this.setCoeffs(Ntt.ntt(this.coeffs));
    }

    public void pointwiseMontgomery(Poly poly, Poly poly2) {
        for (int i2 = 0; i2 < this.dilithiumN; ++i2) {
            this.setCoeffIndex(i2, Reduce.montgomeryReduce((long)poly.getCoeffIndex(i2) * (long)poly2.getCoeffIndex(i2)));
        }
    }

    public void pointwiseAccountMontgomery(PolyVecL polyVecL, PolyVecL polyVecL2) {
        Poly poly = new Poly(this.engine);
        this.pointwiseMontgomery(polyVecL.getVectorIndex(0), polyVecL2.getVectorIndex(0));
        for (int i2 = 1; i2 < this.engine.getDilithiumL(); ++i2) {
            poly.pointwiseMontgomery(polyVecL.getVectorIndex(i2), polyVecL2.getVectorIndex(i2));
            this.addPoly(poly);
        }
    }

    public void addPoly(Poly poly) {
        for (int i2 = 0; i2 < this.dilithiumN; ++i2) {
            this.setCoeffIndex(i2, this.getCoeffIndex(i2) + poly.getCoeffIndex(i2));
        }
    }

    public void reduce() {
        for (int i2 = 0; i2 < this.dilithiumN; ++i2) {
            this.setCoeffIndex(i2, Reduce.reduce32(this.getCoeffIndex(i2)));
        }
    }

    public void invNttToMont() {
        this.setCoeffs(Ntt.invNttToMont(this.getCoeffs()));
    }

    public void conditionalAddQ() {
        for (int i2 = 0; i2 < this.dilithiumN; ++i2) {
            this.setCoeffIndex(i2, Reduce.conditionalAddQ(this.getCoeffIndex(i2)));
        }
    }

    public void power2Round(Poly poly) {
        for (int i2 = 0; i2 < this.dilithiumN; ++i2) {
            int[] nArray = Rounding.power2Round(this.getCoeffIndex(i2));
            this.setCoeffIndex(i2, nArray[0]);
            poly.setCoeffIndex(i2, nArray[1]);
        }
    }

    public byte[] polyt1Pack() {
        byte[] byArray = new byte[320];
        for (int i2 = 0; i2 < this.dilithiumN / 4; ++i2) {
            byArray[5 * i2 + 0] = (byte)(this.coeffs[4 * i2 + 0] >> 0);
            byArray[5 * i2 + 1] = (byte)(this.coeffs[4 * i2 + 0] >> 8 | this.coeffs[4 * i2 + 1] << 2);
            byArray[5 * i2 + 2] = (byte)(this.coeffs[4 * i2 + 1] >> 6 | this.coeffs[4 * i2 + 2] << 4);
            byArray[5 * i2 + 3] = (byte)(this.coeffs[4 * i2 + 2] >> 4 | this.coeffs[4 * i2 + 3] << 6);
            byArray[5 * i2 + 4] = (byte)(this.coeffs[4 * i2 + 3] >> 2);
        }
        return byArray;
    }

    public void polyt1Unpack(byte[] byArray) {
        for (int i2 = 0; i2 < this.dilithiumN / 4; ++i2) {
            this.setCoeffIndex(4 * i2 + 0, ((byArray[5 * i2 + 0] & 0xFF) >> 0 | (byArray[5 * i2 + 1] & 0xFF) << 8) & 0x3FF);
            this.setCoeffIndex(4 * i2 + 1, ((byArray[5 * i2 + 1] & 0xFF) >> 2 | (byArray[5 * i2 + 2] & 0xFF) << 6) & 0x3FF);
            this.setCoeffIndex(4 * i2 + 2, ((byArray[5 * i2 + 2] & 0xFF) >> 4 | (byArray[5 * i2 + 3] & 0xFF) << 4) & 0x3FF);
            this.setCoeffIndex(4 * i2 + 3, ((byArray[5 * i2 + 3] & 0xFF) >> 6 | (byArray[5 * i2 + 4] & 0xFF) << 2) & 0x3FF);
        }
    }

    public byte[] polyEtaPack(byte[] byArray, int n2) {
        byte[] byArray2 = new byte[8];
        if (this.engine.getDilithiumEta() == 2) {
            for (int i2 = 0; i2 < this.dilithiumN / 8; ++i2) {
                byArray2[0] = (byte)(this.engine.getDilithiumEta() - this.getCoeffIndex(8 * i2 + 0));
                byArray2[1] = (byte)(this.engine.getDilithiumEta() - this.getCoeffIndex(8 * i2 + 1));
                byArray2[2] = (byte)(this.engine.getDilithiumEta() - this.getCoeffIndex(8 * i2 + 2));
                byArray2[3] = (byte)(this.engine.getDilithiumEta() - this.getCoeffIndex(8 * i2 + 3));
                byArray2[4] = (byte)(this.engine.getDilithiumEta() - this.getCoeffIndex(8 * i2 + 4));
                byArray2[5] = (byte)(this.engine.getDilithiumEta() - this.getCoeffIndex(8 * i2 + 5));
                byArray2[6] = (byte)(this.engine.getDilithiumEta() - this.getCoeffIndex(8 * i2 + 6));
                byArray2[7] = (byte)(this.engine.getDilithiumEta() - this.getCoeffIndex(8 * i2 + 7));
                byArray[n2 + 3 * i2 + 0] = (byte)(byArray2[0] >> 0 | byArray2[1] << 3 | byArray2[2] << 6);
                byArray[n2 + 3 * i2 + 1] = (byte)(byArray2[2] >> 2 | byArray2[3] << 1 | byArray2[4] << 4 | byArray2[5] << 7);
                byArray[n2 + 3 * i2 + 2] = (byte)(byArray2[5] >> 1 | byArray2[6] << 2 | byArray2[7] << 5);
            }
        } else if (this.engine.getDilithiumEta() == 4) {
            for (int i3 = 0; i3 < this.dilithiumN / 2; ++i3) {
                byArray2[0] = (byte)(this.engine.getDilithiumEta() - this.getCoeffIndex(2 * i3 + 0));
                byArray2[1] = (byte)(this.engine.getDilithiumEta() - this.getCoeffIndex(2 * i3 + 1));
                byArray[n2 + i3] = (byte)(byArray2[0] | byArray2[1] << 4);
            }
        } else {
            throw new RuntimeException("Eta needs to be 2 or 4!");
        }
        return byArray;
    }

    public void polyEtaUnpack(byte[] byArray, int n2) {
        block3: {
            int n3;
            block2: {
                n3 = this.engine.getDilithiumEta();
                if (this.engine.getDilithiumEta() != 2) break block2;
                for (int i2 = 0; i2 < this.dilithiumN / 8; ++i2) {
                    int n4 = n2 + 3 * i2;
                    this.setCoeffIndex(8 * i2 + 0, (byArray[n4 + 0] & 0xFF) >> 0 & 7);
                    this.setCoeffIndex(8 * i2 + 1, (byArray[n4 + 0] & 0xFF) >> 3 & 7);
                    this.setCoeffIndex(8 * i2 + 2, (byArray[n4 + 0] & 0xFF) >> 6 | (byArray[n4 + 1] & 0xFF) << 2 & 7);
                    this.setCoeffIndex(8 * i2 + 3, (byArray[n4 + 1] & 0xFF) >> 1 & 7);
                    this.setCoeffIndex(8 * i2 + 4, (byArray[n4 + 1] & 0xFF) >> 4 & 7);
                    this.setCoeffIndex(8 * i2 + 5, (byArray[n4 + 1] & 0xFF) >> 7 | (byArray[n4 + 2] & 0xFF) << 1 & 7);
                    this.setCoeffIndex(8 * i2 + 6, (byArray[n4 + 2] & 0xFF) >> 2 & 7);
                    this.setCoeffIndex(8 * i2 + 7, (byArray[n4 + 2] & 0xFF) >> 5 & 7);
                    this.setCoeffIndex(8 * i2 + 0, n3 - this.getCoeffIndex(8 * i2 + 0));
                    this.setCoeffIndex(8 * i2 + 1, n3 - this.getCoeffIndex(8 * i2 + 1));
                    this.setCoeffIndex(8 * i2 + 2, n3 - this.getCoeffIndex(8 * i2 + 2));
                    this.setCoeffIndex(8 * i2 + 3, n3 - this.getCoeffIndex(8 * i2 + 3));
                    this.setCoeffIndex(8 * i2 + 4, n3 - this.getCoeffIndex(8 * i2 + 4));
                    this.setCoeffIndex(8 * i2 + 5, n3 - this.getCoeffIndex(8 * i2 + 5));
                    this.setCoeffIndex(8 * i2 + 6, n3 - this.getCoeffIndex(8 * i2 + 6));
                    this.setCoeffIndex(8 * i2 + 7, n3 - this.getCoeffIndex(8 * i2 + 7));
                }
                break block3;
            }
            if (this.engine.getDilithiumEta() != 4) break block3;
            for (int i3 = 0; i3 < this.dilithiumN / 2; ++i3) {
                this.setCoeffIndex(2 * i3 + 0, byArray[n2 + i3] & 0xF);
                this.setCoeffIndex(2 * i3 + 1, (byArray[n2 + i3] & 0xFF) >> 4);
                this.setCoeffIndex(2 * i3 + 0, n3 - this.getCoeffIndex(2 * i3 + 0));
                this.setCoeffIndex(2 * i3 + 1, n3 - this.getCoeffIndex(2 * i3 + 1));
            }
        }
    }

    public byte[] polyt0Pack(byte[] byArray, int n2) {
        int[] nArray = new int[8];
        for (int i2 = 0; i2 < this.dilithiumN / 8; ++i2) {
            nArray[0] = 4096 - this.getCoeffIndex(8 * i2 + 0);
            nArray[1] = 4096 - this.getCoeffIndex(8 * i2 + 1);
            nArray[2] = 4096 - this.getCoeffIndex(8 * i2 + 2);
            nArray[3] = 4096 - this.getCoeffIndex(8 * i2 + 3);
            nArray[4] = 4096 - this.getCoeffIndex(8 * i2 + 4);
            nArray[5] = 4096 - this.getCoeffIndex(8 * i2 + 5);
            nArray[6] = 4096 - this.getCoeffIndex(8 * i2 + 6);
            nArray[7] = 4096 - this.getCoeffIndex(8 * i2 + 7);
            int n3 = n2 + 13 * i2;
            byArray[n3 + 0] = (byte)nArray[0];
            byArray[n3 + 1] = (byte)(nArray[0] >> 8);
            byArray[n3 + 1] = (byte)(byArray[n3 + 1] | (byte)(nArray[1] << 5));
            byArray[n3 + 2] = (byte)(nArray[1] >> 3);
            byArray[n3 + 3] = (byte)(nArray[1] >> 11);
            byArray[n3 + 3] = (byte)(byArray[n3 + 3] | (byte)(nArray[2] << 2));
            byArray[n3 + 4] = (byte)(nArray[2] >> 6);
            byArray[n3 + 4] = (byte)(byArray[n3 + 4] | (byte)(nArray[3] << 7));
            byArray[n3 + 5] = (byte)(nArray[3] >> 1);
            byArray[n3 + 6] = (byte)(nArray[3] >> 9);
            byArray[n3 + 6] = (byte)(byArray[n3 + 6] | (byte)(nArray[4] << 4));
            byArray[n3 + 7] = (byte)(nArray[4] >> 4);
            byArray[n3 + 8] = (byte)(nArray[4] >> 12);
            byArray[n3 + 8] = (byte)(byArray[n3 + 8] | (byte)(nArray[5] << 1));
            byArray[n3 + 9] = (byte)(nArray[5] >> 7);
            byArray[n3 + 9] = (byte)(byArray[n3 + 9] | (byte)(nArray[6] << 6));
            byArray[n3 + 10] = (byte)(nArray[6] >> 2);
            byArray[n3 + 11] = (byte)(nArray[6] >> 10);
            byArray[n3 + 11] = (byte)(byArray[n3 + 11] | (byte)(nArray[7] << 3));
            byArray[n3 + 12] = (byte)(nArray[7] >> 5);
        }
        return byArray;
    }

    public void polyt0Unpack(byte[] byArray, int n2) {
        for (int i2 = 0; i2 < this.dilithiumN / 8; ++i2) {
            int n3 = n2 + 13 * i2;
            this.setCoeffIndex(8 * i2 + 0, (byArray[n3 + 0] & 0xFF | (byArray[n3 + 1] & 0xFF) << 8) & 0x1FFF);
            this.setCoeffIndex(8 * i2 + 1, ((byArray[n3 + 1] & 0xFF) >> 5 | (byArray[n3 + 2] & 0xFF) << 3 | (byArray[n3 + 3] & 0xFF) << 11) & 0x1FFF);
            this.setCoeffIndex(8 * i2 + 2, ((byArray[n3 + 3] & 0xFF) >> 2 | (byArray[n3 + 4] & 0xFF) << 6) & 0x1FFF);
            this.setCoeffIndex(8 * i2 + 3, ((byArray[n3 + 4] & 0xFF) >> 7 | (byArray[n3 + 5] & 0xFF) << 1 | (byArray[n3 + 6] & 0xFF) << 9) & 0x1FFF);
            this.setCoeffIndex(8 * i2 + 4, ((byArray[n3 + 6] & 0xFF) >> 4 | (byArray[n3 + 7] & 0xFF) << 4 | (byArray[n3 + 8] & 0xFF) << 12) & 0x1FFF);
            this.setCoeffIndex(8 * i2 + 5, ((byArray[n3 + 8] & 0xFF) >> 1 | (byArray[n3 + 9] & 0xFF) << 7) & 0x1FFF);
            this.setCoeffIndex(8 * i2 + 6, ((byArray[n3 + 9] & 0xFF) >> 6 | (byArray[n3 + 10] & 0xFF) << 2 | (byArray[n3 + 11] & 0xFF) << 10) & 0x1FFF);
            this.setCoeffIndex(8 * i2 + 7, ((byArray[n3 + 11] & 0xFF) >> 3 | (byArray[n3 + 12] & 0xFF) << 5) & 0x1FFF);
            this.setCoeffIndex(8 * i2 + 0, 4096 - this.getCoeffIndex(8 * i2 + 0));
            this.setCoeffIndex(8 * i2 + 1, 4096 - this.getCoeffIndex(8 * i2 + 1));
            this.setCoeffIndex(8 * i2 + 2, 4096 - this.getCoeffIndex(8 * i2 + 2));
            this.setCoeffIndex(8 * i2 + 3, 4096 - this.getCoeffIndex(8 * i2 + 3));
            this.setCoeffIndex(8 * i2 + 4, 4096 - this.getCoeffIndex(8 * i2 + 4));
            this.setCoeffIndex(8 * i2 + 5, 4096 - this.getCoeffIndex(8 * i2 + 5));
            this.setCoeffIndex(8 * i2 + 6, 4096 - this.getCoeffIndex(8 * i2 + 6));
            this.setCoeffIndex(8 * i2 + 7, 4096 - this.getCoeffIndex(8 * i2 + 7));
        }
    }

    public void uniformGamma1(byte[] byArray, short s2) {
        byte[] byArray2 = new byte[this.engine.getPolyUniformGamma1NBlocks() * this.symmetric.stream256BlockBytes];
        this.symmetric.stream256init(byArray, s2);
        this.symmetric.stream256squeezeBlocks(byArray2, 0, this.engine.getPolyUniformGamma1NBlocks() * this.symmetric.stream256BlockBytes);
        this.unpackZ(byArray2);
    }

    private void unpackZ(byte[] byArray) {
        if (this.engine.getDilithiumGamma1() == 131072) {
            for (int i2 = 0; i2 < this.dilithiumN / 4; ++i2) {
                this.setCoeffIndex(4 * i2 + 0, (byArray[9 * i2 + 0] & 0xFF | (byArray[9 * i2 + 1] & 0xFF) << 8 | (byArray[9 * i2 + 2] & 0xFF) << 16) & 0x3FFFF);
                this.setCoeffIndex(4 * i2 + 1, ((byArray[9 * i2 + 2] & 0xFF) >> 2 | (byArray[9 * i2 + 3] & 0xFF) << 6 | (byArray[9 * i2 + 4] & 0xFF) << 14) & 0x3FFFF);
                this.setCoeffIndex(4 * i2 + 2, ((byArray[9 * i2 + 4] & 0xFF) >> 4 | (byArray[9 * i2 + 5] & 0xFF) << 4 | (byArray[9 * i2 + 6] & 0xFF) << 12) & 0x3FFFF);
                this.setCoeffIndex(4 * i2 + 3, ((byArray[9 * i2 + 6] & 0xFF) >> 6 | (byArray[9 * i2 + 7] & 0xFF) << 2 | (byArray[9 * i2 + 8] & 0xFF) << 10) & 0x3FFFF);
                this.setCoeffIndex(4 * i2 + 0, this.engine.getDilithiumGamma1() - this.getCoeffIndex(4 * i2 + 0));
                this.setCoeffIndex(4 * i2 + 1, this.engine.getDilithiumGamma1() - this.getCoeffIndex(4 * i2 + 1));
                this.setCoeffIndex(4 * i2 + 2, this.engine.getDilithiumGamma1() - this.getCoeffIndex(4 * i2 + 2));
                this.setCoeffIndex(4 * i2 + 3, this.engine.getDilithiumGamma1() - this.getCoeffIndex(4 * i2 + 3));
            }
        } else if (this.engine.getDilithiumGamma1() == 524288) {
            for (int i3 = 0; i3 < this.dilithiumN / 2; ++i3) {
                this.setCoeffIndex(2 * i3 + 0, (byArray[5 * i3 + 0] & 0xFF | (byArray[5 * i3 + 1] & 0xFF) << 8 | (byArray[5 * i3 + 2] & 0xFF) << 16) & 0xFFFFF);
                this.setCoeffIndex(2 * i3 + 1, ((byArray[5 * i3 + 2] & 0xFF) >> 4 | (byArray[5 * i3 + 3] & 0xFF) << 4 | (byArray[5 * i3 + 4] & 0xFF) << 12) & 0xFFFFF);
                this.setCoeffIndex(2 * i3 + 0, this.engine.getDilithiumGamma1() - this.getCoeffIndex(2 * i3 + 0));
                this.setCoeffIndex(2 * i3 + 1, this.engine.getDilithiumGamma1() - this.getCoeffIndex(2 * i3 + 1));
            }
        } else {
            throw new RuntimeException("Wrong Dilithiumn Gamma1!");
        }
    }

    public void decompose(Poly poly) {
        for (int i2 = 0; i2 < this.dilithiumN; ++i2) {
            int[] nArray = Rounding.decompose(this.getCoeffIndex(i2), this.engine.getDilithiumGamma2());
            this.setCoeffIndex(i2, nArray[1]);
            poly.setCoeffIndex(i2, nArray[0]);
        }
    }

    public byte[] w1Pack() {
        byte[] byArray;
        block3: {
            block2: {
                byArray = new byte[this.engine.getDilithiumPolyW1PackedBytes()];
                if (this.engine.getDilithiumGamma2() != 95232) break block2;
                for (int i2 = 0; i2 < this.dilithiumN / 4; ++i2) {
                    byArray[3 * i2 + 0] = (byte)((byte)this.getCoeffIndex(4 * i2 + 0) | this.getCoeffIndex(4 * i2 + 1) << 6);
                    byArray[3 * i2 + 1] = (byte)((byte)(this.getCoeffIndex(4 * i2 + 1) >> 2) | this.getCoeffIndex(4 * i2 + 2) << 4);
                    byArray[3 * i2 + 2] = (byte)((byte)(this.getCoeffIndex(4 * i2 + 2) >> 4) | this.getCoeffIndex(4 * i2 + 3) << 2);
                }
                break block3;
            }
            if (this.engine.getDilithiumGamma2() != 261888) break block3;
            for (int i3 = 0; i3 < this.dilithiumN / 2; ++i3) {
                byArray[i3] = (byte)(this.getCoeffIndex(2 * i3 + 0) | this.getCoeffIndex(2 * i3 + 1) << 4);
            }
        }
        return byArray;
    }

    public void challenge(byte[] byArray) {
        int n2;
        int n3 = 0;
        byte[] byArray2 = new byte[this.symmetric.stream256BlockBytes];
        SHAKEDigest sHAKEDigest = new SHAKEDigest(256);
        sHAKEDigest.update(byArray, 0, this.engine.getDilithiumCTilde());
        sHAKEDigest.doOutput(byArray2, 0, this.symmetric.stream256BlockBytes);
        long l2 = 0L;
        for (n2 = 0; n2 < 8; ++n2) {
            l2 |= (long)(byArray2[n2] & 0xFF) << 8 * n2;
        }
        int n4 = 8;
        for (n2 = 0; n2 < this.dilithiumN; ++n2) {
            this.setCoeffIndex(n2, 0);
        }
        for (n2 = this.dilithiumN - this.engine.getDilithiumTau(); n2 < this.dilithiumN; ++n2) {
            do {
                if (n4 < this.symmetric.stream256BlockBytes) continue;
                sHAKEDigest.doOutput(byArray2, 0, this.symmetric.stream256BlockBytes);
                n4 = 0;
            } while ((n3 = byArray2[n4++] & 0xFF) > n2);
            this.setCoeffIndex(n2, this.getCoeffIndex(n3));
            this.setCoeffIndex(n3, (int)(1L - 2L * (l2 & 1L)));
            l2 >>= 1;
        }
    }

    public boolean checkNorm(int n2) {
        if (n2 > 1047552) {
            return true;
        }
        for (int i2 = 0; i2 < this.dilithiumN; ++i2) {
            int n3 = this.getCoeffIndex(i2) >> 31;
            n3 = this.getCoeffIndex(i2) - (n3 & 2 * this.getCoeffIndex(i2));
            if (n3 < n2) continue;
            return true;
        }
        return false;
    }

    public void subtract(Poly poly) {
        for (int i2 = 0; i2 < this.dilithiumN; ++i2) {
            this.setCoeffIndex(i2, this.getCoeffIndex(i2) - poly.getCoeffIndex(i2));
        }
    }

    public int polyMakeHint(Poly poly, Poly poly2) {
        int n2 = 0;
        for (int i2 = 0; i2 < this.dilithiumN; ++i2) {
            this.setCoeffIndex(i2, Rounding.makeHint(poly.getCoeffIndex(i2), poly2.getCoeffIndex(i2), this.engine));
            n2 += this.getCoeffIndex(i2);
        }
        return n2;
    }

    public void polyUseHint(Poly poly, Poly poly2) {
        for (int i2 = 0; i2 < this.dilithiumN; ++i2) {
            this.setCoeffIndex(i2, Rounding.useHint(poly.getCoeffIndex(i2), poly2.getCoeffIndex(i2), this.engine.getDilithiumGamma2()));
        }
    }

    public byte[] zPack() {
        byte[] byArray = new byte[this.engine.getDilithiumPolyZPackedBytes()];
        int[] nArray = new int[4];
        if (this.engine.getDilithiumGamma1() == 131072) {
            for (int i2 = 0; i2 < this.dilithiumN / 4; ++i2) {
                nArray[0] = this.engine.getDilithiumGamma1() - this.getCoeffIndex(4 * i2 + 0);
                nArray[1] = this.engine.getDilithiumGamma1() - this.getCoeffIndex(4 * i2 + 1);
                nArray[2] = this.engine.getDilithiumGamma1() - this.getCoeffIndex(4 * i2 + 2);
                nArray[3] = this.engine.getDilithiumGamma1() - this.getCoeffIndex(4 * i2 + 3);
                byArray[9 * i2 + 0] = (byte)nArray[0];
                byArray[9 * i2 + 1] = (byte)(nArray[0] >> 8);
                byArray[9 * i2 + 2] = (byte)((byte)(nArray[0] >> 16) | nArray[1] << 2);
                byArray[9 * i2 + 3] = (byte)(nArray[1] >> 6);
                byArray[9 * i2 + 4] = (byte)((byte)(nArray[1] >> 14) | nArray[2] << 4);
                byArray[9 * i2 + 5] = (byte)(nArray[2] >> 4);
                byArray[9 * i2 + 6] = (byte)((byte)(nArray[2] >> 12) | nArray[3] << 6);
                byArray[9 * i2 + 7] = (byte)(nArray[3] >> 2);
                byArray[9 * i2 + 8] = (byte)(nArray[3] >> 10);
            }
        } else if (this.engine.getDilithiumGamma1() == 524288) {
            for (int i3 = 0; i3 < this.dilithiumN / 2; ++i3) {
                nArray[0] = this.engine.getDilithiumGamma1() - this.getCoeffIndex(2 * i3 + 0);
                nArray[1] = this.engine.getDilithiumGamma1() - this.getCoeffIndex(2 * i3 + 1);
                byArray[5 * i3 + 0] = (byte)nArray[0];
                byArray[5 * i3 + 1] = (byte)(nArray[0] >> 8);
                byArray[5 * i3 + 2] = (byte)((byte)(nArray[0] >> 16) | nArray[1] << 4);
                byArray[5 * i3 + 3] = (byte)(nArray[1] >> 4);
                byArray[5 * i3 + 4] = (byte)(nArray[1] >> 12);
            }
        } else {
            throw new RuntimeException("Wrong Dilithium Gamma1!");
        }
        return byArray;
    }

    void zUnpack(byte[] byArray) {
        if (this.engine.getDilithiumGamma1() == 131072) {
            for (int i2 = 0; i2 < this.dilithiumN / 4; ++i2) {
                this.setCoeffIndex(4 * i2 + 0, (byArray[9 * i2 + 0] & 0xFF | (byArray[9 * i2 + 1] & 0xFF) << 8 | (byArray[9 * i2 + 2] & 0xFF) << 16) & 0x3FFFF);
                this.setCoeffIndex(4 * i2 + 1, ((byArray[9 * i2 + 2] & 0xFF) >>> 2 | (byArray[9 * i2 + 3] & 0xFF) << 6 | (byArray[9 * i2 + 4] & 0xFF) << 14) & 0x3FFFF);
                this.setCoeffIndex(4 * i2 + 2, ((byArray[9 * i2 + 4] & 0xFF) >>> 4 | (byArray[9 * i2 + 5] & 0xFF) << 4 | (byArray[9 * i2 + 6] & 0xFF) << 12) & 0x3FFFF);
                this.setCoeffIndex(4 * i2 + 3, ((byArray[9 * i2 + 6] & 0xFF) >>> 6 | (byArray[9 * i2 + 7] & 0xFF) << 2 | (byArray[9 * i2 + 8] & 0xFF) << 10) & 0x3FFFF);
                this.setCoeffIndex(4 * i2 + 0, this.engine.getDilithiumGamma1() - this.getCoeffIndex(4 * i2 + 0));
                this.setCoeffIndex(4 * i2 + 1, this.engine.getDilithiumGamma1() - this.getCoeffIndex(4 * i2 + 1));
                this.setCoeffIndex(4 * i2 + 2, this.engine.getDilithiumGamma1() - this.getCoeffIndex(4 * i2 + 2));
                this.setCoeffIndex(4 * i2 + 3, this.engine.getDilithiumGamma1() - this.getCoeffIndex(4 * i2 + 3));
            }
        } else if (this.engine.getDilithiumGamma1() == 524288) {
            for (int i3 = 0; i3 < this.dilithiumN / 2; ++i3) {
                this.setCoeffIndex(2 * i3 + 0, (byArray[5 * i3 + 0] & 0xFF | (byArray[5 * i3 + 1] & 0xFF) << 8 | (byArray[5 * i3 + 2] & 0xFF) << 16) & 0xFFFFF);
                this.setCoeffIndex(2 * i3 + 1, ((byArray[5 * i3 + 2] & 0xFF) >>> 4 | (byArray[5 * i3 + 3] & 0xFF) << 4 | (byArray[5 * i3 + 4] & 0xFF) << 12) & 0xFFFFF);
                this.setCoeffIndex(2 * i3 + 0, this.engine.getDilithiumGamma1() - this.getCoeffIndex(2 * i3 + 0));
                this.setCoeffIndex(2 * i3 + 1, this.engine.getDilithiumGamma1() - this.getCoeffIndex(2 * i3 + 1));
            }
        } else {
            throw new RuntimeException("Wrong Dilithium Gamma1!");
        }
    }

    public void shiftLeft() {
        for (int i2 = 0; i2 < this.dilithiumN; ++i2) {
            this.setCoeffIndex(i2, this.getCoeffIndex(i2) << 13);
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

