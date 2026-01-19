/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.mlkem;

import org.bouncycastle.pqc.crypto.mlkem.MLKEMEngine;
import org.bouncycastle.pqc.crypto.mlkem.Poly;
import org.bouncycastle.pqc.crypto.mlkem.PolyVec;
import org.bouncycastle.pqc.crypto.mlkem.Symmetric;
import org.bouncycastle.util.Arrays;

class MLKEMIndCpa {
    private MLKEMEngine engine;
    private int kyberK;
    private int indCpaPublicKeyBytes;
    private int polyVecBytes;
    private int indCpaBytes;
    private int polyVecCompressedBytes;
    private int polyCompressedBytes;
    private Symmetric symmetric;
    public final int KyberGenerateMatrixNBlocks;

    public MLKEMIndCpa(MLKEMEngine mLKEMEngine) {
        this.engine = mLKEMEngine;
        this.kyberK = mLKEMEngine.getKyberK();
        this.indCpaPublicKeyBytes = mLKEMEngine.getKyberPublicKeyBytes();
        this.polyVecBytes = mLKEMEngine.getKyberPolyVecBytes();
        this.indCpaBytes = mLKEMEngine.getKyberIndCpaBytes();
        this.polyVecCompressedBytes = mLKEMEngine.getKyberPolyVecCompressedBytes();
        this.polyCompressedBytes = mLKEMEngine.getKyberPolyCompressedBytes();
        this.symmetric = mLKEMEngine.getSymmetric();
        this.KyberGenerateMatrixNBlocks = (472 + this.symmetric.xofBlockBytes) / this.symmetric.xofBlockBytes;
    }

    byte[][] generateKeyPair(byte[] byArray) {
        int n2;
        PolyVec polyVec = new PolyVec(this.engine);
        PolyVec polyVec2 = new PolyVec(this.engine);
        PolyVec polyVec3 = new PolyVec(this.engine);
        byte[] byArray2 = new byte[64];
        this.symmetric.hash_g(byArray2, Arrays.append(byArray, (byte)this.kyberK));
        byte[] byArray3 = new byte[32];
        byte[] byArray4 = new byte[32];
        System.arraycopy(byArray2, 0, byArray3, 0, 32);
        System.arraycopy(byArray2, 32, byArray4, 0, 32);
        byte by = 0;
        PolyVec[] polyVecArray = new PolyVec[this.kyberK];
        for (n2 = 0; n2 < this.kyberK; ++n2) {
            polyVecArray[n2] = new PolyVec(this.engine);
        }
        this.generateMatrix(polyVecArray, byArray3, false);
        for (n2 = 0; n2 < this.kyberK; ++n2) {
            polyVec.getVectorIndex(n2).getEta1Noise(byArray4, by);
            by = (byte)(by + 1);
        }
        for (n2 = 0; n2 < this.kyberK; ++n2) {
            polyVec3.getVectorIndex(n2).getEta1Noise(byArray4, by);
            by = (byte)(by + 1);
        }
        polyVec.polyVecNtt();
        polyVec3.polyVecNtt();
        for (n2 = 0; n2 < this.kyberK; ++n2) {
            PolyVec.pointwiseAccountMontgomery(polyVec2.getVectorIndex(n2), polyVecArray[n2], polyVec, this.engine);
            polyVec2.getVectorIndex(n2).convertToMont();
        }
        polyVec2.addPoly(polyVec3);
        polyVec2.reducePoly();
        return new byte[][]{this.packPublicKey(polyVec2, byArray3), this.packSecretKey(polyVec)};
    }

    public byte[] encrypt(byte[] byArray, byte[] byArray2, byte[] byArray3) {
        int n2;
        byte by = 0;
        PolyVec polyVec = new PolyVec(this.engine);
        PolyVec polyVec2 = new PolyVec(this.engine);
        PolyVec polyVec3 = new PolyVec(this.engine);
        PolyVec polyVec4 = new PolyVec(this.engine);
        PolyVec[] polyVecArray = new PolyVec[this.engine.getKyberK()];
        Poly poly = new Poly(this.engine);
        Poly poly2 = new Poly(this.engine);
        Poly poly3 = new Poly(this.engine);
        byte[] byArray4 = this.unpackPublicKey(polyVec2, byArray);
        poly3.fromMsg(byArray2);
        for (n2 = 0; n2 < this.kyberK; ++n2) {
            polyVecArray[n2] = new PolyVec(this.engine);
        }
        this.generateMatrix(polyVecArray, byArray4, true);
        for (n2 = 0; n2 < this.kyberK; ++n2) {
            polyVec.getVectorIndex(n2).getEta1Noise(byArray3, by);
            by = (byte)(by + 1);
        }
        for (n2 = 0; n2 < this.kyberK; ++n2) {
            polyVec3.getVectorIndex(n2).getEta2Noise(byArray3, by);
            by = (byte)(by + 1);
        }
        poly.getEta2Noise(byArray3, by);
        polyVec.polyVecNtt();
        for (n2 = 0; n2 < this.kyberK; ++n2) {
            PolyVec.pointwiseAccountMontgomery(polyVec4.getVectorIndex(n2), polyVecArray[n2], polyVec, this.engine);
        }
        PolyVec.pointwiseAccountMontgomery(poly2, polyVec2, polyVec, this.engine);
        polyVec4.polyVecInverseNttToMont();
        poly2.polyInverseNttToMont();
        polyVec4.addPoly(polyVec3);
        poly2.addCoeffs(poly);
        poly2.addCoeffs(poly3);
        polyVec4.reducePoly();
        poly2.reduce();
        byte[] byArray5 = this.packCipherText(polyVec4, poly2);
        return byArray5;
    }

    private byte[] packCipherText(PolyVec polyVec, Poly poly) {
        byte[] byArray = new byte[this.indCpaBytes];
        System.arraycopy(polyVec.compressPolyVec(), 0, byArray, 0, this.polyVecCompressedBytes);
        System.arraycopy(poly.compressPoly(), 0, byArray, this.polyVecCompressedBytes, this.polyCompressedBytes);
        return byArray;
    }

    private void unpackCipherText(PolyVec polyVec, Poly poly, byte[] byArray) {
        byte[] byArray2 = Arrays.copyOfRange(byArray, 0, this.engine.getKyberPolyVecCompressedBytes());
        polyVec.decompressPolyVec(byArray2);
        byte[] byArray3 = Arrays.copyOfRange(byArray, this.engine.getKyberPolyVecCompressedBytes(), byArray.length);
        poly.decompressPoly(byArray3);
    }

    public byte[] packPublicKey(PolyVec polyVec, byte[] byArray) {
        byte[] byArray2 = new byte[this.indCpaPublicKeyBytes];
        System.arraycopy(polyVec.toBytes(), 0, byArray2, 0, this.polyVecBytes);
        System.arraycopy(byArray, 0, byArray2, this.polyVecBytes, 32);
        return byArray2;
    }

    public byte[] unpackPublicKey(PolyVec polyVec, byte[] byArray) {
        byte[] byArray2 = new byte[32];
        polyVec.fromBytes(byArray);
        System.arraycopy(byArray, this.polyVecBytes, byArray2, 0, 32);
        return byArray2;
    }

    public byte[] packSecretKey(PolyVec polyVec) {
        return polyVec.toBytes();
    }

    public void unpackSecretKey(PolyVec polyVec, byte[] byArray) {
        polyVec.fromBytes(byArray);
    }

    public void generateMatrix(PolyVec[] polyVecArray, byte[] byArray, boolean bl) {
        byte[] byArray2 = new byte[this.KyberGenerateMatrixNBlocks * this.symmetric.xofBlockBytes + 2];
        for (int i2 = 0; i2 < this.kyberK; ++i2) {
            for (int i3 = 0; i3 < this.kyberK; ++i3) {
                if (bl) {
                    this.symmetric.xofAbsorb(byArray, (byte)i2, (byte)i3);
                } else {
                    this.symmetric.xofAbsorb(byArray, (byte)i3, (byte)i2);
                }
                this.symmetric.xofSqueezeBlocks(byArray2, 0, this.symmetric.xofBlockBytes * this.KyberGenerateMatrixNBlocks);
                int n2 = this.KyberGenerateMatrixNBlocks * this.symmetric.xofBlockBytes;
                for (int i4 = MLKEMIndCpa.rejectionSampling(polyVecArray[i2].getVectorIndex(i3), 0, 256, byArray2, n2); i4 < 256; i4 += MLKEMIndCpa.rejectionSampling(polyVecArray[i2].getVectorIndex(i3), i4, 256 - i4, byArray2, n2)) {
                    int n3 = n2 % 3;
                    for (int i5 = 0; i5 < n3; ++i5) {
                        byArray2[i5] = byArray2[n2 - n3 + i5];
                    }
                    this.symmetric.xofSqueezeBlocks(byArray2, n3, this.symmetric.xofBlockBytes * 2);
                    n2 = n3 + this.symmetric.xofBlockBytes;
                }
            }
        }
    }

    private static int rejectionSampling(Poly poly, int n2, int n3, byte[] byArray, int n4) {
        int n5 = 0;
        int n6 = 0;
        while (n6 < n3 && n5 + 3 <= n4) {
            short s2 = (short)(((short)(byArray[n5] & 0xFF) >> 0 | (short)(byArray[n5 + 1] & 0xFF) << 8) & 0xFFF);
            short s3 = (short)(((short)(byArray[n5 + 1] & 0xFF) >> 4 | (short)(byArray[n5 + 2] & 0xFF) << 4) & 0xFFF);
            n5 += 3;
            if (s2 < 3329) {
                poly.setCoeffIndex(n2 + n6, s2);
                ++n6;
            }
            if (n6 >= n3 || s3 >= 3329) continue;
            poly.setCoeffIndex(n2 + n6, s3);
            ++n6;
        }
        return n6;
    }

    public byte[] decrypt(byte[] byArray, byte[] byArray2) {
        byte[] byArray3 = new byte[MLKEMEngine.getKyberIndCpaMsgBytes()];
        PolyVec polyVec = new PolyVec(this.engine);
        PolyVec polyVec2 = new PolyVec(this.engine);
        Poly poly = new Poly(this.engine);
        Poly poly2 = new Poly(this.engine);
        this.unpackCipherText(polyVec, poly, byArray2);
        this.unpackSecretKey(polyVec2, byArray);
        polyVec.polyVecNtt();
        PolyVec.pointwiseAccountMontgomery(poly2, polyVec2, polyVec, this.engine);
        poly2.polyInverseNttToMont();
        poly2.polySubtract(poly);
        poly2.reduce();
        byArray3 = poly2.toMsg();
        return byArray3;
    }
}

