/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.asn1;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.util.Arrays;

public class ParSet
extends ASN1Object {
    private int t;
    private int[] h;
    private int[] w;
    private int[] k;

    private static int checkBigIntegerInIntRangeAndPositive(ASN1Encodable aSN1Encodable) {
        ASN1Integer aSN1Integer = (ASN1Integer)aSN1Encodable;
        int n2 = aSN1Integer.intValueExact();
        if (n2 <= 0) {
            throw new IllegalArgumentException("BigInteger not in Range: " + n2);
        }
        return n2;
    }

    private ParSet(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() != 4) {
            throw new IllegalArgumentException("sie of seqOfParams = " + aSN1Sequence.size());
        }
        this.t = ParSet.checkBigIntegerInIntRangeAndPositive(aSN1Sequence.getObjectAt(0));
        ASN1Sequence aSN1Sequence2 = (ASN1Sequence)aSN1Sequence.getObjectAt(1);
        ASN1Sequence aSN1Sequence3 = (ASN1Sequence)aSN1Sequence.getObjectAt(2);
        ASN1Sequence aSN1Sequence4 = (ASN1Sequence)aSN1Sequence.getObjectAt(3);
        if (aSN1Sequence2.size() != this.t || aSN1Sequence3.size() != this.t || aSN1Sequence4.size() != this.t) {
            throw new IllegalArgumentException("invalid size of sequences");
        }
        this.h = new int[aSN1Sequence2.size()];
        this.w = new int[aSN1Sequence3.size()];
        this.k = new int[aSN1Sequence4.size()];
        for (int i2 = 0; i2 < this.t; ++i2) {
            this.h[i2] = ParSet.checkBigIntegerInIntRangeAndPositive(aSN1Sequence2.getObjectAt(i2));
            this.w[i2] = ParSet.checkBigIntegerInIntRangeAndPositive(aSN1Sequence3.getObjectAt(i2));
            this.k[i2] = ParSet.checkBigIntegerInIntRangeAndPositive(aSN1Sequence4.getObjectAt(i2));
        }
    }

    public ParSet(int n2, int[] nArray, int[] nArray2, int[] nArray3) {
        this.t = n2;
        this.h = nArray;
        this.w = nArray2;
        this.k = nArray3;
    }

    public static ParSet getInstance(Object object) {
        if (object instanceof ParSet) {
            return (ParSet)object;
        }
        if (object != null) {
            return new ParSet(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public int getT() {
        return this.t;
    }

    public int[] getH() {
        return Arrays.clone(this.h);
    }

    public int[] getW() {
        return Arrays.clone(this.w);
    }

    public int[] getK() {
        return Arrays.clone(this.k);
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector3 = new ASN1EncodableVector();
        for (int i2 = 0; i2 < this.h.length; ++i2) {
            aSN1EncodableVector.add(new ASN1Integer(this.h[i2]));
            aSN1EncodableVector2.add(new ASN1Integer(this.w[i2]));
            aSN1EncodableVector3.add(new ASN1Integer(this.k[i2]));
        }
        ASN1EncodableVector aSN1EncodableVector4 = new ASN1EncodableVector();
        aSN1EncodableVector4.add(new ASN1Integer(this.t));
        aSN1EncodableVector4.add(new DERSequence(aSN1EncodableVector));
        aSN1EncodableVector4.add(new DERSequence(aSN1EncodableVector2));
        aSN1EncodableVector4.add(new DERSequence(aSN1EncodableVector3));
        return new DERSequence(aSN1EncodableVector4);
    }
}

