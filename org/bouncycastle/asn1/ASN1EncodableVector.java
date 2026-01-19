/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

import org.bouncycastle.asn1.ASN1Encodable;

public class ASN1EncodableVector {
    static final ASN1Encodable[] EMPTY_ELEMENTS = new ASN1Encodable[0];
    private static final int DEFAULT_CAPACITY = 10;
    private ASN1Encodable[] elements;
    private int elementCount;
    private boolean copyOnWrite;

    public ASN1EncodableVector() {
        this(10);
    }

    public ASN1EncodableVector(int n2) {
        if (n2 < 0) {
            throw new IllegalArgumentException("'initialCapacity' must not be negative");
        }
        this.elements = n2 == 0 ? EMPTY_ELEMENTS : new ASN1Encodable[n2];
        this.elementCount = 0;
        this.copyOnWrite = false;
    }

    public void add(ASN1Encodable aSN1Encodable) {
        if (null == aSN1Encodable) {
            throw new NullPointerException("'element' cannot be null");
        }
        int n2 = this.elementCount + 1;
        int n3 = this.elements.length;
        if (n2 > n3 | this.copyOnWrite) {
            this.reallocate(n2);
        }
        this.elements[this.elementCount] = aSN1Encodable;
        this.elementCount = n2;
    }

    public void addAll(ASN1Encodable[] aSN1EncodableArray) {
        if (null == aSN1EncodableArray) {
            throw new NullPointerException("'others' cannot be null");
        }
        this.doAddAll(aSN1EncodableArray, "'others' elements cannot be null");
    }

    public void addAll(ASN1EncodableVector aSN1EncodableVector) {
        if (null == aSN1EncodableVector) {
            throw new NullPointerException("'other' cannot be null");
        }
        this.doAddAll(aSN1EncodableVector.elements, "'other' elements cannot be null");
    }

    private void doAddAll(ASN1Encodable[] aSN1EncodableArray, String string) {
        int n2 = aSN1EncodableArray.length;
        if (n2 < 1) {
            return;
        }
        int n3 = this.elementCount + n2;
        int n4 = this.elements.length;
        if (n3 > n4 | this.copyOnWrite) {
            this.reallocate(n3);
        }
        int n5 = 0;
        do {
            ASN1Encodable aSN1Encodable;
            if (null == (aSN1Encodable = aSN1EncodableArray[n5])) {
                throw new NullPointerException(string);
            }
            this.elements[this.elementCount + n5] = aSN1Encodable;
        } while (++n5 < n2);
        this.elementCount = n3;
    }

    public ASN1Encodable get(int n2) {
        if (n2 >= this.elementCount) {
            throw new ArrayIndexOutOfBoundsException(n2 + " >= " + this.elementCount);
        }
        return this.elements[n2];
    }

    public int size() {
        return this.elementCount;
    }

    ASN1Encodable[] copyElements() {
        if (0 == this.elementCount) {
            return EMPTY_ELEMENTS;
        }
        ASN1Encodable[] aSN1EncodableArray = new ASN1Encodable[this.elementCount];
        System.arraycopy(this.elements, 0, aSN1EncodableArray, 0, this.elementCount);
        return aSN1EncodableArray;
    }

    ASN1Encodable[] takeElements() {
        if (0 == this.elementCount) {
            return EMPTY_ELEMENTS;
        }
        if (this.elements.length == this.elementCount) {
            this.copyOnWrite = true;
            return this.elements;
        }
        ASN1Encodable[] aSN1EncodableArray = new ASN1Encodable[this.elementCount];
        System.arraycopy(this.elements, 0, aSN1EncodableArray, 0, this.elementCount);
        return aSN1EncodableArray;
    }

    private void reallocate(int n2) {
        int n3 = this.elements.length;
        int n4 = Math.max(n3, n2 + (n2 >> 1));
        ASN1Encodable[] aSN1EncodableArray = new ASN1Encodable[n4];
        System.arraycopy(this.elements, 0, aSN1EncodableArray, 0, this.elementCount);
        this.elements = aSN1EncodableArray;
        this.copyOnWrite = false;
    }

    static ASN1Encodable[] cloneElements(ASN1Encodable[] aSN1EncodableArray) {
        return aSN1EncodableArray.length < 1 ? EMPTY_ELEMENTS : (ASN1Encodable[])aSN1EncodableArray.clone();
    }
}

