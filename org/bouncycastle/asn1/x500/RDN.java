/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1.x500;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.x500.AttributeTypeAndValue;

public class RDN
extends ASN1Object {
    private ASN1Set values;

    private RDN(ASN1Set aSN1Set) {
        this.values = aSN1Set;
    }

    public static RDN getInstance(Object object) {
        if (object instanceof RDN) {
            return (RDN)object;
        }
        if (object != null) {
            return new RDN(ASN1Set.getInstance(object));
        }
        return null;
    }

    public static RDN getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return new RDN(ASN1Set.getInstance(aSN1TaggedObject, bl));
    }

    public RDN(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Encodable aSN1Encodable) {
        this(new AttributeTypeAndValue(aSN1ObjectIdentifier, aSN1Encodable));
    }

    public RDN(AttributeTypeAndValue attributeTypeAndValue) {
        this.values = new DERSet(attributeTypeAndValue);
    }

    public RDN(AttributeTypeAndValue[] attributeTypeAndValueArray) {
        this.values = new DERSet(attributeTypeAndValueArray);
    }

    public boolean isMultiValued() {
        return this.values.size() > 1;
    }

    public int size() {
        return this.values.size();
    }

    public AttributeTypeAndValue getFirst() {
        if (this.values.size() == 0) {
            return null;
        }
        return AttributeTypeAndValue.getInstance(this.values.getObjectAt(0));
    }

    public AttributeTypeAndValue[] getTypesAndValues() {
        AttributeTypeAndValue[] attributeTypeAndValueArray = new AttributeTypeAndValue[this.values.size()];
        for (int i2 = 0; i2 != attributeTypeAndValueArray.length; ++i2) {
            attributeTypeAndValueArray[i2] = AttributeTypeAndValue.getInstance(this.values.getObjectAt(i2));
        }
        return attributeTypeAndValueArray;
    }

    int collectAttributeTypes(ASN1ObjectIdentifier[] aSN1ObjectIdentifierArray, int n2) {
        int n3 = this.values.size();
        for (int i2 = 0; i2 < n3; ++i2) {
            AttributeTypeAndValue attributeTypeAndValue = AttributeTypeAndValue.getInstance(this.values.getObjectAt(i2));
            aSN1ObjectIdentifierArray[n2 + i2] = attributeTypeAndValue.getType();
        }
        return n3;
    }

    boolean containsAttributeType(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        int n2 = this.values.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            AttributeTypeAndValue attributeTypeAndValue = AttributeTypeAndValue.getInstance(this.values.getObjectAt(i2));
            if (!attributeTypeAndValue.getType().equals(aSN1ObjectIdentifier)) continue;
            return true;
        }
        return false;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.values;
    }
}

