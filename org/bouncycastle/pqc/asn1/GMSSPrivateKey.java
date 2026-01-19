/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.asn1;

import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.pqc.legacy.crypto.gmss.GMSSLeaf;
import org.bouncycastle.pqc.legacy.crypto.gmss.GMSSParameters;
import org.bouncycastle.pqc.legacy.crypto.gmss.GMSSRootCalc;
import org.bouncycastle.pqc.legacy.crypto.gmss.GMSSRootSig;
import org.bouncycastle.pqc.legacy.crypto.gmss.Treehash;

public class GMSSPrivateKey
extends ASN1Object {
    private ASN1Primitive primitive;

    public static GMSSPrivateKey getInstance(Object object) {
        if (object instanceof GMSSPrivateKey) {
            return (GMSSPrivateKey)object;
        }
        if (object != null) {
            return new GMSSPrivateKey(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    private GMSSPrivateKey(ASN1Sequence aSN1Sequence) {
        ASN1Sequence aSN1Sequence2 = (ASN1Sequence)aSN1Sequence.getObjectAt(0);
        int[] nArray = new int[aSN1Sequence2.size()];
        for (int i2 = 0; i2 < aSN1Sequence2.size(); ++i2) {
            nArray[i2] = GMSSPrivateKey.checkBigIntegerInIntRange(aSN1Sequence2.getObjectAt(i2));
        }
        ASN1Sequence aSN1Sequence3 = (ASN1Sequence)aSN1Sequence.getObjectAt(1);
        byte[][] byArrayArray = new byte[aSN1Sequence3.size()][];
        for (int i3 = 0; i3 < byArrayArray.length; ++i3) {
            byArrayArray[i3] = ((DEROctetString)aSN1Sequence3.getObjectAt(i3)).getOctets();
        }
        ASN1Sequence aSN1Sequence4 = (ASN1Sequence)aSN1Sequence.getObjectAt(2);
        byte[][] byArrayArray2 = new byte[aSN1Sequence4.size()][];
        for (int i4 = 0; i4 < byArrayArray2.length; ++i4) {
            byArrayArray2[i4] = ((DEROctetString)aSN1Sequence4.getObjectAt(i4)).getOctets();
        }
        ASN1Sequence aSN1Sequence5 = (ASN1Sequence)aSN1Sequence.getObjectAt(3);
        byte[][][] byArrayArray3 = new byte[aSN1Sequence5.size()][][];
        for (int i5 = 0; i5 < byArrayArray3.length; ++i5) {
            ASN1Sequence aSN1Sequence6 = (ASN1Sequence)aSN1Sequence5.getObjectAt(i5);
            byArrayArray3[i5] = new byte[aSN1Sequence6.size()][];
            for (int i6 = 0; i6 < byArrayArray3[i5].length; ++i6) {
                byArrayArray3[i5][i6] = ((DEROctetString)aSN1Sequence6.getObjectAt(i6)).getOctets();
            }
        }
        ASN1Sequence aSN1Sequence7 = (ASN1Sequence)aSN1Sequence.getObjectAt(4);
        byte[][][] byArrayArray4 = new byte[aSN1Sequence7.size()][][];
        for (int i7 = 0; i7 < byArrayArray4.length; ++i7) {
            ASN1Sequence aSN1Sequence8 = (ASN1Sequence)aSN1Sequence7.getObjectAt(i7);
            byArrayArray4[i7] = new byte[aSN1Sequence8.size()][];
            for (int i8 = 0; i8 < byArrayArray4[i7].length; ++i8) {
                byArrayArray4[i7][i8] = ((DEROctetString)aSN1Sequence8.getObjectAt(i8)).getOctets();
            }
        }
        ASN1Sequence aSN1Sequence9 = (ASN1Sequence)aSN1Sequence.getObjectAt(5);
        Treehash[][] treehashArrayArray = new Treehash[aSN1Sequence9.size()][];
    }

    public GMSSPrivateKey(int[] nArray, byte[][] byArray, byte[][] byArray2, byte[][][] byArray3, byte[][][] byArray4, Treehash[][] treehashArray, Treehash[][] treehashArray2, Vector[] vectorArray, Vector[] vectorArray2, Vector[][] vectorArray3, Vector[][] vectorArray4, byte[][][] byArray5, GMSSLeaf[] gMSSLeafArray, GMSSLeaf[] gMSSLeafArray2, GMSSLeaf[] gMSSLeafArray3, int[] nArray2, byte[][] byArray6, GMSSRootCalc[] gMSSRootCalcArray, byte[][] byArray7, GMSSRootSig[] gMSSRootSigArray, GMSSParameters gMSSParameters, AlgorithmIdentifier algorithmIdentifier) {
        AlgorithmIdentifier[] algorithmIdentifierArray = new AlgorithmIdentifier[]{algorithmIdentifier};
        this.primitive = this.encode(nArray, byArray, byArray2, byArray3, byArray4, byArray5, treehashArray, treehashArray2, vectorArray, vectorArray2, vectorArray3, vectorArray4, gMSSLeafArray, gMSSLeafArray2, gMSSLeafArray3, nArray2, byArray6, gMSSRootCalcArray, byArray7, gMSSRootSigArray, gMSSParameters, algorithmIdentifierArray);
    }

    private ASN1Primitive encode(int[] nArray, byte[][] byArray, byte[][] byArray2, byte[][][] byArray3, byte[][][] byArray4, byte[][][] byArray5, Treehash[][] treehashArray, Treehash[][] treehashArray2, Vector[] vectorArray, Vector[] vectorArray2, Vector[][] vectorArray3, Vector[][] vectorArray4, GMSSLeaf[] gMSSLeafArray, GMSSLeaf[] gMSSLeafArray2, GMSSLeaf[] gMSSLeafArray3, int[] nArray2, byte[][] byArray6, GMSSRootCalc[] gMSSRootCalcArray, byte[][] byArray7, GMSSRootSig[] gMSSRootSigArray, GMSSParameters gMSSParameters, AlgorithmIdentifier[] algorithmIdentifierArray) {
        int n2;
        Object object;
        Object object2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            aSN1EncodableVector2.add(new ASN1Integer(nArray[i2]));
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
        ASN1EncodableVector aSN1EncodableVector3 = new ASN1EncodableVector();
        for (int i3 = 0; i3 < byArray.length; ++i3) {
            aSN1EncodableVector3.add(new DEROctetString(byArray[i3]));
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector3));
        ASN1EncodableVector aSN1EncodableVector4 = new ASN1EncodableVector();
        for (int i4 = 0; i4 < byArray2.length; ++i4) {
            aSN1EncodableVector4.add(new DEROctetString(byArray2[i4]));
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector4));
        ASN1EncodableVector aSN1EncodableVector5 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector6 = new ASN1EncodableVector();
        for (int i5 = 0; i5 < byArray3.length; ++i5) {
            for (int i6 = 0; i6 < byArray3[i5].length; ++i6) {
                aSN1EncodableVector5.add(new DEROctetString(byArray3[i5][i6]));
            }
            aSN1EncodableVector6.add(new DERSequence(aSN1EncodableVector5));
            aSN1EncodableVector5 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector6));
        ASN1EncodableVector aSN1EncodableVector7 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector8 = new ASN1EncodableVector();
        for (int i7 = 0; i7 < byArray4.length; ++i7) {
            for (int i8 = 0; i8 < byArray4[i7].length; ++i8) {
                aSN1EncodableVector7.add(new DEROctetString(byArray4[i7][i8]));
            }
            aSN1EncodableVector8.add(new DERSequence(aSN1EncodableVector7));
            aSN1EncodableVector7 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector8));
        ASN1EncodableVector aSN1EncodableVector9 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector10 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector11 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector12 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector13 = new ASN1EncodableVector();
        for (n7 = 0; n7 < treehashArray.length; ++n7) {
            for (n6 = 0; n6 < treehashArray[n7].length; ++n6) {
                aSN1EncodableVector11.add(new DERSequence(algorithmIdentifierArray[0]));
                n5 = treehashArray[n7][n6].getStatInt()[1];
                aSN1EncodableVector12.add(new DEROctetString(treehashArray[n7][n6].getStatByte()[0]));
                aSN1EncodableVector12.add(new DEROctetString(treehashArray[n7][n6].getStatByte()[1]));
                aSN1EncodableVector12.add(new DEROctetString(treehashArray[n7][n6].getStatByte()[2]));
                for (n4 = 0; n4 < n5; ++n4) {
                    aSN1EncodableVector12.add(new DEROctetString(treehashArray[n7][n6].getStatByte()[3 + n4]));
                }
                aSN1EncodableVector11.add(new DERSequence(aSN1EncodableVector12));
                aSN1EncodableVector12 = new ASN1EncodableVector();
                aSN1EncodableVector13.add(new ASN1Integer(treehashArray[n7][n6].getStatInt()[0]));
                aSN1EncodableVector13.add(new ASN1Integer(n5));
                aSN1EncodableVector13.add(new ASN1Integer(treehashArray[n7][n6].getStatInt()[2]));
                aSN1EncodableVector13.add(new ASN1Integer(treehashArray[n7][n6].getStatInt()[3]));
                aSN1EncodableVector13.add(new ASN1Integer(treehashArray[n7][n6].getStatInt()[4]));
                aSN1EncodableVector13.add(new ASN1Integer(treehashArray[n7][n6].getStatInt()[5]));
                for (n4 = 0; n4 < n5; ++n4) {
                    aSN1EncodableVector13.add(new ASN1Integer(treehashArray[n7][n6].getStatInt()[6 + n4]));
                }
                aSN1EncodableVector11.add(new DERSequence(aSN1EncodableVector13));
                aSN1EncodableVector13 = new ASN1EncodableVector();
                aSN1EncodableVector10.add(new DERSequence(aSN1EncodableVector11));
                aSN1EncodableVector11 = new ASN1EncodableVector();
            }
            aSN1EncodableVector9.add(new DERSequence(aSN1EncodableVector10));
            aSN1EncodableVector10 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector9));
        aSN1EncodableVector9 = new ASN1EncodableVector();
        aSN1EncodableVector10 = new ASN1EncodableVector();
        aSN1EncodableVector11 = new ASN1EncodableVector();
        aSN1EncodableVector12 = new ASN1EncodableVector();
        aSN1EncodableVector13 = new ASN1EncodableVector();
        for (n7 = 0; n7 < treehashArray2.length; ++n7) {
            for (n6 = 0; n6 < treehashArray2[n7].length; ++n6) {
                aSN1EncodableVector11.add(new DERSequence(algorithmIdentifierArray[0]));
                n5 = treehashArray2[n7][n6].getStatInt()[1];
                aSN1EncodableVector12.add(new DEROctetString(treehashArray2[n7][n6].getStatByte()[0]));
                aSN1EncodableVector12.add(new DEROctetString(treehashArray2[n7][n6].getStatByte()[1]));
                aSN1EncodableVector12.add(new DEROctetString(treehashArray2[n7][n6].getStatByte()[2]));
                for (n4 = 0; n4 < n5; ++n4) {
                    aSN1EncodableVector12.add(new DEROctetString(treehashArray2[n7][n6].getStatByte()[3 + n4]));
                }
                aSN1EncodableVector11.add(new DERSequence(aSN1EncodableVector12));
                aSN1EncodableVector12 = new ASN1EncodableVector();
                aSN1EncodableVector13.add(new ASN1Integer(treehashArray2[n7][n6].getStatInt()[0]));
                aSN1EncodableVector13.add(new ASN1Integer(n5));
                aSN1EncodableVector13.add(new ASN1Integer(treehashArray2[n7][n6].getStatInt()[2]));
                aSN1EncodableVector13.add(new ASN1Integer(treehashArray2[n7][n6].getStatInt()[3]));
                aSN1EncodableVector13.add(new ASN1Integer(treehashArray2[n7][n6].getStatInt()[4]));
                aSN1EncodableVector13.add(new ASN1Integer(treehashArray2[n7][n6].getStatInt()[5]));
                for (n4 = 0; n4 < n5; ++n4) {
                    aSN1EncodableVector13.add(new ASN1Integer(treehashArray2[n7][n6].getStatInt()[6 + n4]));
                }
                aSN1EncodableVector11.add(new DERSequence(aSN1EncodableVector13));
                aSN1EncodableVector13 = new ASN1EncodableVector();
                aSN1EncodableVector10.add(new DERSequence(aSN1EncodableVector11));
                aSN1EncodableVector11 = new ASN1EncodableVector();
            }
            aSN1EncodableVector9.add(new DERSequence(new DERSequence(aSN1EncodableVector10)));
            aSN1EncodableVector10 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector9));
        ASN1EncodableVector aSN1EncodableVector14 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector15 = new ASN1EncodableVector();
        for (n5 = 0; n5 < byArray5.length; ++n5) {
            for (n4 = 0; n4 < byArray5[n5].length; ++n4) {
                aSN1EncodableVector14.add(new DEROctetString(byArray5[n5][n4]));
            }
            aSN1EncodableVector15.add(new DERSequence(aSN1EncodableVector14));
            aSN1EncodableVector14 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector15));
        ASN1EncodableVector aSN1EncodableVector16 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector17 = new ASN1EncodableVector();
        for (int i9 = 0; i9 < vectorArray.length; ++i9) {
            for (int i10 = 0; i10 < vectorArray[i9].size(); ++i10) {
                aSN1EncodableVector16.add(new DEROctetString((byte[])vectorArray[i9].elementAt(i10)));
            }
            aSN1EncodableVector17.add(new DERSequence(aSN1EncodableVector16));
            aSN1EncodableVector16 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector17));
        ASN1EncodableVector aSN1EncodableVector18 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector19 = new ASN1EncodableVector();
        for (int i11 = 0; i11 < vectorArray2.length; ++i11) {
            for (int i12 = 0; i12 < vectorArray2[i11].size(); ++i12) {
                aSN1EncodableVector18.add(new DEROctetString((byte[])vectorArray2[i11].elementAt(i12)));
            }
            aSN1EncodableVector19.add(new DERSequence(aSN1EncodableVector18));
            aSN1EncodableVector18 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector19));
        ASN1EncodableVector aSN1EncodableVector20 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector21 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector22 = new ASN1EncodableVector();
        for (int i13 = 0; i13 < vectorArray3.length; ++i13) {
            for (int i14 = 0; i14 < vectorArray3[i13].length; ++i14) {
                for (int i15 = 0; i15 < vectorArray3[i13][i14].size(); ++i15) {
                    aSN1EncodableVector20.add(new DEROctetString((byte[])vectorArray3[i13][i14].elementAt(i15)));
                }
                aSN1EncodableVector21.add(new DERSequence(aSN1EncodableVector20));
                aSN1EncodableVector20 = new ASN1EncodableVector();
            }
            aSN1EncodableVector22.add(new DERSequence(aSN1EncodableVector21));
            aSN1EncodableVector21 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector22));
        ASN1EncodableVector aSN1EncodableVector23 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector24 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector25 = new ASN1EncodableVector();
        for (int i16 = 0; i16 < vectorArray4.length; ++i16) {
            for (n3 = 0; n3 < vectorArray4[i16].length; ++n3) {
                for (int i17 = 0; i17 < vectorArray4[i16][n3].size(); ++i17) {
                    aSN1EncodableVector23.add(new DEROctetString((byte[])vectorArray4[i16][n3].elementAt(i17)));
                }
                aSN1EncodableVector24.add(new DERSequence(aSN1EncodableVector23));
                aSN1EncodableVector23 = new ASN1EncodableVector();
            }
            aSN1EncodableVector25.add(new DERSequence(aSN1EncodableVector24));
            aSN1EncodableVector24 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector25));
        ASN1EncodableVector aSN1EncodableVector26 = new ASN1EncodableVector();
        aSN1EncodableVector11 = new ASN1EncodableVector();
        aSN1EncodableVector12 = new ASN1EncodableVector();
        aSN1EncodableVector13 = new ASN1EncodableVector();
        for (n3 = 0; n3 < gMSSLeafArray.length; ++n3) {
            aSN1EncodableVector11.add(new DERSequence(algorithmIdentifierArray[0]));
            byte[][] byArray8 = gMSSLeafArray[n3].getStatByte();
            aSN1EncodableVector12.add(new DEROctetString(byArray8[0]));
            aSN1EncodableVector12.add(new DEROctetString(byArray8[1]));
            aSN1EncodableVector12.add(new DEROctetString(byArray8[2]));
            aSN1EncodableVector12.add(new DEROctetString(byArray8[3]));
            aSN1EncodableVector11.add(new DERSequence(aSN1EncodableVector12));
            aSN1EncodableVector12 = new ASN1EncodableVector();
            object2 = gMSSLeafArray[n3].getStatInt();
            aSN1EncodableVector13.add(new ASN1Integer((long)object2[0]));
            aSN1EncodableVector13.add(new ASN1Integer((long)object2[1]));
            aSN1EncodableVector13.add(new ASN1Integer((long)object2[2]));
            aSN1EncodableVector13.add(new ASN1Integer((long)object2[3]));
            aSN1EncodableVector11.add(new DERSequence(aSN1EncodableVector13));
            aSN1EncodableVector13 = new ASN1EncodableVector();
            aSN1EncodableVector26.add(new DERSequence(aSN1EncodableVector11));
            aSN1EncodableVector11 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector26));
        ASN1EncodableVector aSN1EncodableVector27 = new ASN1EncodableVector();
        aSN1EncodableVector11 = new ASN1EncodableVector();
        aSN1EncodableVector12 = new ASN1EncodableVector();
        aSN1EncodableVector13 = new ASN1EncodableVector();
        for (int i18 = 0; i18 < gMSSLeafArray2.length; ++i18) {
            aSN1EncodableVector11.add(new DERSequence(algorithmIdentifierArray[0]));
            object2 = gMSSLeafArray2[i18].getStatByte();
            aSN1EncodableVector12.add(new DEROctetString(object2[0]));
            aSN1EncodableVector12.add(new DEROctetString(object2[1]));
            aSN1EncodableVector12.add(new DEROctetString(object2[2]));
            aSN1EncodableVector12.add(new DEROctetString(object2[3]));
            aSN1EncodableVector11.add(new DERSequence(aSN1EncodableVector12));
            aSN1EncodableVector12 = new ASN1EncodableVector();
            object = gMSSLeafArray2[i18].getStatInt();
            aSN1EncodableVector13.add(new ASN1Integer((long)object[0]));
            aSN1EncodableVector13.add(new ASN1Integer((long)object[1]));
            aSN1EncodableVector13.add(new ASN1Integer((long)object[2]));
            aSN1EncodableVector13.add(new ASN1Integer((long)object[3]));
            aSN1EncodableVector11.add(new DERSequence(aSN1EncodableVector13));
            aSN1EncodableVector13 = new ASN1EncodableVector();
            aSN1EncodableVector27.add(new DERSequence(aSN1EncodableVector11));
            aSN1EncodableVector11 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector27));
        ASN1EncodableVector aSN1EncodableVector28 = new ASN1EncodableVector();
        aSN1EncodableVector11 = new ASN1EncodableVector();
        aSN1EncodableVector12 = new ASN1EncodableVector();
        aSN1EncodableVector13 = new ASN1EncodableVector();
        for (int i19 = 0; i19 < gMSSLeafArray3.length; ++i19) {
            aSN1EncodableVector11.add(new DERSequence(algorithmIdentifierArray[0]));
            object = gMSSLeafArray3[i19].getStatByte();
            aSN1EncodableVector12.add(new DEROctetString(object[0]));
            aSN1EncodableVector12.add(new DEROctetString(object[1]));
            aSN1EncodableVector12.add(new DEROctetString(object[2]));
            aSN1EncodableVector12.add(new DEROctetString(object[3]));
            aSN1EncodableVector11.add(new DERSequence(aSN1EncodableVector12));
            aSN1EncodableVector12 = new ASN1EncodableVector();
            int[] nArray3 = gMSSLeafArray3[i19].getStatInt();
            aSN1EncodableVector13.add(new ASN1Integer(nArray3[0]));
            aSN1EncodableVector13.add(new ASN1Integer(nArray3[1]));
            aSN1EncodableVector13.add(new ASN1Integer(nArray3[2]));
            aSN1EncodableVector13.add(new ASN1Integer(nArray3[3]));
            aSN1EncodableVector11.add(new DERSequence(aSN1EncodableVector13));
            aSN1EncodableVector13 = new ASN1EncodableVector();
            aSN1EncodableVector28.add(new DERSequence(aSN1EncodableVector11));
            aSN1EncodableVector11 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector28));
        ASN1EncodableVector aSN1EncodableVector29 = new ASN1EncodableVector();
        for (int i20 = 0; i20 < nArray2.length; ++i20) {
            aSN1EncodableVector29.add(new ASN1Integer(nArray2[i20]));
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector29));
        ASN1EncodableVector aSN1EncodableVector30 = new ASN1EncodableVector();
        for (int i21 = 0; i21 < byArray6.length; ++i21) {
            aSN1EncodableVector30.add(new DEROctetString(byArray6[i21]));
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector30));
        ASN1EncodableVector aSN1EncodableVector31 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector32 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector33 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector34 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector35 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector36 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector37 = new ASN1EncodableVector();
        for (int i22 = 0; i22 < gMSSRootCalcArray.length; ++i22) {
            int n8;
            int n9;
            aSN1EncodableVector32.add(new DERSequence(algorithmIdentifierArray[0]));
            aSN1EncodableVector33 = new ASN1EncodableVector();
            n2 = gMSSRootCalcArray[i22].getStatInt()[0];
            int n10 = gMSSRootCalcArray[i22].getStatInt()[7];
            aSN1EncodableVector34.add(new DEROctetString(gMSSRootCalcArray[i22].getStatByte()[0]));
            for (n9 = 0; n9 < n2; ++n9) {
                aSN1EncodableVector34.add(new DEROctetString(gMSSRootCalcArray[i22].getStatByte()[1 + n9]));
            }
            for (n9 = 0; n9 < n10; ++n9) {
                aSN1EncodableVector34.add(new DEROctetString(gMSSRootCalcArray[i22].getStatByte()[1 + n2 + n9]));
            }
            aSN1EncodableVector32.add(new DERSequence(aSN1EncodableVector34));
            aSN1EncodableVector34 = new ASN1EncodableVector();
            aSN1EncodableVector35.add(new ASN1Integer(n2));
            aSN1EncodableVector35.add(new ASN1Integer(gMSSRootCalcArray[i22].getStatInt()[1]));
            aSN1EncodableVector35.add(new ASN1Integer(gMSSRootCalcArray[i22].getStatInt()[2]));
            aSN1EncodableVector35.add(new ASN1Integer(gMSSRootCalcArray[i22].getStatInt()[3]));
            aSN1EncodableVector35.add(new ASN1Integer(gMSSRootCalcArray[i22].getStatInt()[4]));
            aSN1EncodableVector35.add(new ASN1Integer(gMSSRootCalcArray[i22].getStatInt()[5]));
            aSN1EncodableVector35.add(new ASN1Integer(gMSSRootCalcArray[i22].getStatInt()[6]));
            aSN1EncodableVector35.add(new ASN1Integer(n10));
            for (n9 = 0; n9 < n2; ++n9) {
                aSN1EncodableVector35.add(new ASN1Integer(gMSSRootCalcArray[i22].getStatInt()[8 + n9]));
            }
            for (n9 = 0; n9 < n10; ++n9) {
                aSN1EncodableVector35.add(new ASN1Integer(gMSSRootCalcArray[i22].getStatInt()[8 + n2 + n9]));
            }
            aSN1EncodableVector32.add(new DERSequence(aSN1EncodableVector35));
            aSN1EncodableVector35 = new ASN1EncodableVector();
            aSN1EncodableVector11 = new ASN1EncodableVector();
            aSN1EncodableVector12 = new ASN1EncodableVector();
            aSN1EncodableVector13 = new ASN1EncodableVector();
            if (gMSSRootCalcArray[i22].getTreehash() != null) {
                for (n9 = 0; n9 < gMSSRootCalcArray[i22].getTreehash().length; ++n9) {
                    aSN1EncodableVector11.add(new DERSequence(algorithmIdentifierArray[0]));
                    n10 = gMSSRootCalcArray[i22].getTreehash()[n9].getStatInt()[1];
                    aSN1EncodableVector12.add(new DEROctetString(gMSSRootCalcArray[i22].getTreehash()[n9].getStatByte()[0]));
                    aSN1EncodableVector12.add(new DEROctetString(gMSSRootCalcArray[i22].getTreehash()[n9].getStatByte()[1]));
                    aSN1EncodableVector12.add(new DEROctetString(gMSSRootCalcArray[i22].getTreehash()[n9].getStatByte()[2]));
                    for (n8 = 0; n8 < n10; ++n8) {
                        aSN1EncodableVector12.add(new DEROctetString(gMSSRootCalcArray[i22].getTreehash()[n9].getStatByte()[3 + n8]));
                    }
                    aSN1EncodableVector11.add(new DERSequence(aSN1EncodableVector12));
                    aSN1EncodableVector12 = new ASN1EncodableVector();
                    aSN1EncodableVector13.add(new ASN1Integer(gMSSRootCalcArray[i22].getTreehash()[n9].getStatInt()[0]));
                    aSN1EncodableVector13.add(new ASN1Integer(n10));
                    aSN1EncodableVector13.add(new ASN1Integer(gMSSRootCalcArray[i22].getTreehash()[n9].getStatInt()[2]));
                    aSN1EncodableVector13.add(new ASN1Integer(gMSSRootCalcArray[i22].getTreehash()[n9].getStatInt()[3]));
                    aSN1EncodableVector13.add(new ASN1Integer(gMSSRootCalcArray[i22].getTreehash()[n9].getStatInt()[4]));
                    aSN1EncodableVector13.add(new ASN1Integer(gMSSRootCalcArray[i22].getTreehash()[n9].getStatInt()[5]));
                    for (n8 = 0; n8 < n10; ++n8) {
                        aSN1EncodableVector13.add(new ASN1Integer(gMSSRootCalcArray[i22].getTreehash()[n9].getStatInt()[6 + n8]));
                    }
                    aSN1EncodableVector11.add(new DERSequence(aSN1EncodableVector13));
                    aSN1EncodableVector13 = new ASN1EncodableVector();
                    aSN1EncodableVector36.add(new DERSequence(aSN1EncodableVector11));
                    aSN1EncodableVector11 = new ASN1EncodableVector();
                }
            }
            aSN1EncodableVector32.add(new DERSequence(aSN1EncodableVector36));
            aSN1EncodableVector36 = new ASN1EncodableVector();
            aSN1EncodableVector20 = new ASN1EncodableVector();
            if (gMSSRootCalcArray[i22].getRetain() != null) {
                for (n9 = 0; n9 < gMSSRootCalcArray[i22].getRetain().length; ++n9) {
                    for (n8 = 0; n8 < gMSSRootCalcArray[i22].getRetain()[n9].size(); ++n8) {
                        aSN1EncodableVector20.add(new DEROctetString((byte[])gMSSRootCalcArray[i22].getRetain()[n9].elementAt(n8)));
                    }
                    aSN1EncodableVector37.add(new DERSequence(aSN1EncodableVector20));
                    aSN1EncodableVector20 = new ASN1EncodableVector();
                }
            }
            aSN1EncodableVector32.add(new DERSequence(aSN1EncodableVector37));
            aSN1EncodableVector37 = new ASN1EncodableVector();
            aSN1EncodableVector31.add(new DERSequence(aSN1EncodableVector32));
            aSN1EncodableVector32 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector31));
        ASN1EncodableVector aSN1EncodableVector38 = new ASN1EncodableVector();
        for (n2 = 0; n2 < byArray7.length; ++n2) {
            aSN1EncodableVector38.add(new DEROctetString(byArray7[n2]));
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector38));
        ASN1EncodableVector aSN1EncodableVector39 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector40 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector41 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector42 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector43 = new ASN1EncodableVector();
        for (int i23 = 0; i23 < gMSSRootSigArray.length; ++i23) {
            aSN1EncodableVector40.add(new DERSequence(algorithmIdentifierArray[0]));
            aSN1EncodableVector41 = new ASN1EncodableVector();
            aSN1EncodableVector42.add(new DEROctetString(gMSSRootSigArray[i23].getStatByte()[0]));
            aSN1EncodableVector42.add(new DEROctetString(gMSSRootSigArray[i23].getStatByte()[1]));
            aSN1EncodableVector42.add(new DEROctetString(gMSSRootSigArray[i23].getStatByte()[2]));
            aSN1EncodableVector42.add(new DEROctetString(gMSSRootSigArray[i23].getStatByte()[3]));
            aSN1EncodableVector42.add(new DEROctetString(gMSSRootSigArray[i23].getStatByte()[4]));
            aSN1EncodableVector40.add(new DERSequence(aSN1EncodableVector42));
            aSN1EncodableVector42 = new ASN1EncodableVector();
            aSN1EncodableVector43.add(new ASN1Integer(gMSSRootSigArray[i23].getStatInt()[0]));
            aSN1EncodableVector43.add(new ASN1Integer(gMSSRootSigArray[i23].getStatInt()[1]));
            aSN1EncodableVector43.add(new ASN1Integer(gMSSRootSigArray[i23].getStatInt()[2]));
            aSN1EncodableVector43.add(new ASN1Integer(gMSSRootSigArray[i23].getStatInt()[3]));
            aSN1EncodableVector43.add(new ASN1Integer(gMSSRootSigArray[i23].getStatInt()[4]));
            aSN1EncodableVector43.add(new ASN1Integer(gMSSRootSigArray[i23].getStatInt()[5]));
            aSN1EncodableVector43.add(new ASN1Integer(gMSSRootSigArray[i23].getStatInt()[6]));
            aSN1EncodableVector43.add(new ASN1Integer(gMSSRootSigArray[i23].getStatInt()[7]));
            aSN1EncodableVector43.add(new ASN1Integer(gMSSRootSigArray[i23].getStatInt()[8]));
            aSN1EncodableVector40.add(new DERSequence(aSN1EncodableVector43));
            aSN1EncodableVector43 = new ASN1EncodableVector();
            aSN1EncodableVector39.add(new DERSequence(aSN1EncodableVector40));
            aSN1EncodableVector40 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector39));
        ASN1EncodableVector aSN1EncodableVector44 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector45 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector46 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector47 = new ASN1EncodableVector();
        for (int i24 = 0; i24 < gMSSParameters.getHeightOfTrees().length; ++i24) {
            aSN1EncodableVector45.add(new ASN1Integer(gMSSParameters.getHeightOfTrees()[i24]));
            aSN1EncodableVector46.add(new ASN1Integer(gMSSParameters.getWinternitzParameter()[i24]));
            aSN1EncodableVector47.add(new ASN1Integer(gMSSParameters.getK()[i24]));
        }
        aSN1EncodableVector44.add(new ASN1Integer(gMSSParameters.getNumOfLayers()));
        aSN1EncodableVector44.add(new DERSequence(aSN1EncodableVector45));
        aSN1EncodableVector44.add(new DERSequence(aSN1EncodableVector46));
        aSN1EncodableVector44.add(new DERSequence(aSN1EncodableVector47));
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector44));
        ASN1EncodableVector aSN1EncodableVector48 = new ASN1EncodableVector();
        for (int i25 = 0; i25 < algorithmIdentifierArray.length; ++i25) {
            aSN1EncodableVector48.add(algorithmIdentifierArray[i25]);
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector48));
        return new DERSequence(aSN1EncodableVector);
    }

    private static int checkBigIntegerInIntRange(ASN1Encodable aSN1Encodable) {
        return ((ASN1Integer)aSN1Encodable).intValueExact();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.primitive;
    }
}

