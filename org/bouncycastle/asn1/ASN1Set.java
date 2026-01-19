/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1SetParser;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1UniversalType;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.DLSet;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Iterable;

public abstract class ASN1Set
extends ASN1Primitive
implements Iterable<ASN1Encodable> {
    static final ASN1UniversalType TYPE = new ASN1UniversalType(ASN1Set.class, 17){

        @Override
        ASN1Primitive fromImplicitConstructed(ASN1Sequence aSN1Sequence) {
            return aSN1Sequence.toASN1Set();
        }
    };
    protected final ASN1Encodable[] elements;
    protected ASN1Encodable[] sortedElements;

    public static ASN1Set getInstance(Object object) {
        if (object == null || object instanceof ASN1Set) {
            return (ASN1Set)object;
        }
        if (object instanceof ASN1Encodable) {
            ASN1Primitive aSN1Primitive = ((ASN1Encodable)object).toASN1Primitive();
            if (aSN1Primitive instanceof ASN1Set) {
                return (ASN1Set)aSN1Primitive;
            }
        } else if (object instanceof byte[]) {
            try {
                return (ASN1Set)TYPE.fromByteArray((byte[])object);
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("failed to construct set from byte[]: " + iOException.getMessage());
            }
        }
        throw new IllegalArgumentException("unknown object in getInstance: " + object.getClass().getName());
    }

    public static ASN1Set getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return (ASN1Set)TYPE.getContextInstance(aSN1TaggedObject, bl);
    }

    protected ASN1Set() {
        this.elements = ASN1EncodableVector.EMPTY_ELEMENTS;
        this.sortedElements = this.elements;
    }

    protected ASN1Set(ASN1Encodable aSN1Encodable) {
        if (null == aSN1Encodable) {
            throw new NullPointerException("'element' cannot be null");
        }
        this.elements = new ASN1Encodable[]{aSN1Encodable};
        this.sortedElements = this.elements;
    }

    protected ASN1Set(ASN1EncodableVector aSN1EncodableVector, boolean bl) {
        ASN1Encodable[] aSN1EncodableArray;
        if (null == aSN1EncodableVector) {
            throw new NullPointerException("'elementVector' cannot be null");
        }
        if (bl && aSN1EncodableVector.size() >= 2) {
            aSN1EncodableArray = aSN1EncodableVector.copyElements();
            ASN1Set.sort(aSN1EncodableArray);
        } else {
            aSN1EncodableArray = aSN1EncodableVector.takeElements();
        }
        this.elements = aSN1EncodableArray;
        this.sortedElements = bl || aSN1EncodableArray.length < 2 ? this.elements : null;
    }

    protected ASN1Set(ASN1Encodable[] aSN1EncodableArray, boolean bl) {
        if (Arrays.isNullOrContainsNull(aSN1EncodableArray)) {
            throw new NullPointerException("'elements' cannot be null, or contain null");
        }
        ASN1Encodable[] aSN1EncodableArray2 = ASN1EncodableVector.cloneElements(aSN1EncodableArray);
        if (bl && aSN1EncodableArray2.length >= 2) {
            ASN1Set.sort(aSN1EncodableArray2);
        }
        this.elements = aSN1EncodableArray2;
        this.sortedElements = bl || aSN1EncodableArray2.length < 2 ? aSN1EncodableArray : null;
    }

    ASN1Set(boolean bl, ASN1Encodable[] aSN1EncodableArray) {
        this.elements = aSN1EncodableArray;
        this.sortedElements = bl || aSN1EncodableArray.length < 2 ? aSN1EncodableArray : null;
    }

    ASN1Set(ASN1Encodable[] aSN1EncodableArray, ASN1Encodable[] aSN1EncodableArray2) {
        this.elements = aSN1EncodableArray;
        this.sortedElements = aSN1EncodableArray2;
    }

    public Enumeration getObjects() {
        return new Enumeration(){
            private int pos = 0;

            @Override
            public boolean hasMoreElements() {
                return this.pos < ASN1Set.this.elements.length;
            }

            public Object nextElement() {
                if (this.pos < ASN1Set.this.elements.length) {
                    return ASN1Set.this.elements[this.pos++];
                }
                throw new NoSuchElementException();
            }
        };
    }

    public ASN1Encodable getObjectAt(int n2) {
        return this.elements[n2];
    }

    public int size() {
        return this.elements.length;
    }

    public ASN1Encodable[] toArray() {
        return ASN1EncodableVector.cloneElements(this.elements);
    }

    public ASN1SetParser parser() {
        final int n2 = this.size();
        return new ASN1SetParser(){
            private int pos = 0;

            @Override
            public ASN1Encodable readObject() throws IOException {
                ASN1Encodable aSN1Encodable;
                if (n2 == this.pos) {
                    return null;
                }
                if ((aSN1Encodable = ASN1Set.this.elements[this.pos++]) instanceof ASN1Sequence) {
                    return ((ASN1Sequence)aSN1Encodable).parser();
                }
                if (aSN1Encodable instanceof ASN1Set) {
                    return ((ASN1Set)aSN1Encodable).parser();
                }
                return aSN1Encodable;
            }

            @Override
            public ASN1Primitive getLoadedObject() {
                return ASN1Set.this;
            }

            @Override
            public ASN1Primitive toASN1Primitive() {
                return ASN1Set.this;
            }
        };
    }

    @Override
    public int hashCode() {
        int n2 = this.elements.length;
        int n3 = n2 + 1;
        while (--n2 >= 0) {
            n3 += this.elements[n2].toASN1Primitive().hashCode();
        }
        return n3;
    }

    @Override
    ASN1Primitive toDERObject() {
        if (this.sortedElements == null) {
            this.sortedElements = (ASN1Encodable[])this.elements.clone();
            ASN1Set.sort(this.sortedElements);
        }
        return new DERSet(true, this.sortedElements);
    }

    @Override
    ASN1Primitive toDLObject() {
        return new DLSet(this.elements, this.sortedElements);
    }

    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof ASN1Set)) {
            return false;
        }
        ASN1Set aSN1Set = (ASN1Set)aSN1Primitive;
        int n2 = this.size();
        if (aSN1Set.size() != n2) {
            return false;
        }
        DERSet dERSet = (DERSet)this.toDERObject();
        DERSet dERSet2 = (DERSet)aSN1Set.toDERObject();
        for (int i2 = 0; i2 < n2; ++i2) {
            ASN1Primitive aSN1Primitive2;
            ASN1Primitive aSN1Primitive3 = dERSet.elements[i2].toASN1Primitive();
            if (aSN1Primitive3 == (aSN1Primitive2 = dERSet2.elements[i2].toASN1Primitive()) || aSN1Primitive3.asn1Equals(aSN1Primitive2)) continue;
            return false;
        }
        return true;
    }

    @Override
    boolean encodeConstructed() {
        return true;
    }

    public String toString() {
        int n2 = this.size();
        if (0 == n2) {
            return "[]";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('[');
        int n3 = 0;
        while (true) {
            stringBuffer.append(this.elements[n3]);
            if (++n3 >= n2) break;
            stringBuffer.append(", ");
        }
        stringBuffer.append(']');
        return stringBuffer.toString();
    }

    @Override
    public Iterator<ASN1Encodable> iterator() {
        return new Arrays.Iterator<ASN1Encodable>(this.toArray());
    }

    private static byte[] getDEREncoded(ASN1Encodable aSN1Encodable) {
        try {
            return aSN1Encodable.toASN1Primitive().getEncoded("DER");
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException("cannot encode object added to SET");
        }
    }

    private static boolean lessThanOrEqual(byte[] byArray, byte[] byArray2) {
        int n2 = byArray[0] & 0xDF;
        int n3 = byArray2[0] & 0xDF;
        if (n2 != n3) {
            return n2 < n3;
        }
        int n4 = Math.min(byArray.length, byArray2.length) - 1;
        for (int i2 = 1; i2 < n4; ++i2) {
            if (byArray[i2] == byArray2[i2]) continue;
            return (byArray[i2] & 0xFF) < (byArray2[i2] & 0xFF);
        }
        return (byArray[n4] & 0xFF) <= (byArray2[n4] & 0xFF);
    }

    private static void sort(ASN1Encodable[] aSN1EncodableArray) {
        Object object;
        int n2 = aSN1EncodableArray.length;
        if (n2 < 2) {
            return;
        }
        Object object2 = aSN1EncodableArray[0];
        Object object3 = aSN1EncodableArray[1];
        byte[] byArray = ASN1Set.getDEREncoded((ASN1Encodable)object2);
        byte[] byArray2 = ASN1Set.getDEREncoded((ASN1Encodable)object3);
        if (ASN1Set.lessThanOrEqual(byArray2, byArray)) {
            ASN1Encodable aSN1Encodable = object3;
            object3 = object2;
            object2 = aSN1Encodable;
            object = byArray2;
            byArray2 = byArray;
            byArray = object;
        }
        for (int i2 = 2; i2 < n2; ++i2) {
            ASN1Encodable aSN1Encodable;
            byte[] byArray3;
            object = aSN1EncodableArray[i2];
            byte[] byArray4 = ASN1Set.getDEREncoded((ASN1Encodable)object);
            if (ASN1Set.lessThanOrEqual(byArray2, byArray4)) {
                aSN1EncodableArray[i2 - 2] = object2;
                object2 = object3;
                byArray = byArray2;
                object3 = object;
                byArray2 = byArray4;
                continue;
            }
            if (ASN1Set.lessThanOrEqual(byArray, byArray4)) {
                aSN1EncodableArray[i2 - 2] = object2;
                object2 = object;
                byArray = byArray4;
                continue;
            }
            int n3 = i2 - 1;
            while (--n3 > 0 && !ASN1Set.lessThanOrEqual(byArray3 = ASN1Set.getDEREncoded(aSN1Encodable = aSN1EncodableArray[n3 - 1]), byArray4)) {
                aSN1EncodableArray[n3] = aSN1Encodable;
            }
            aSN1EncodableArray[n3] = object;
        }
        aSN1EncodableArray[n2 - 2] = object2;
        aSN1EncodableArray[n2 - 1] = object3;
    }
}

