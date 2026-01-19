/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1.x500.style;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1ParsingException;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;

public abstract class AbstractX500NameStyle
implements X500NameStyle {
    public static Hashtable copyHashTable(Hashtable hashtable) {
        Hashtable hashtable2 = new Hashtable();
        Enumeration enumeration = hashtable.keys();
        while (enumeration.hasMoreElements()) {
            Object k2 = enumeration.nextElement();
            hashtable2.put(k2, hashtable.get(k2));
        }
        return hashtable2;
    }

    private int calcHashCode(ASN1Encodable aSN1Encodable) {
        String string = IETFUtils.canonicalString(aSN1Encodable);
        return string.hashCode();
    }

    @Override
    public int calculateHashCode(X500Name x500Name) {
        int n2 = 0;
        RDN[] rDNArray = x500Name.getRDNs();
        for (int i2 = 0; i2 != rDNArray.length; ++i2) {
            if (rDNArray[i2].isMultiValued()) {
                AttributeTypeAndValue[] attributeTypeAndValueArray = rDNArray[i2].getTypesAndValues();
                for (int i3 = 0; i3 != attributeTypeAndValueArray.length; ++i3) {
                    n2 ^= attributeTypeAndValueArray[i3].getType().hashCode();
                    n2 ^= this.calcHashCode(attributeTypeAndValueArray[i3].getValue());
                }
                continue;
            }
            n2 ^= rDNArray[i2].getFirst().getType().hashCode();
            n2 ^= this.calcHashCode(rDNArray[i2].getFirst().getValue());
        }
        return n2;
    }

    @Override
    public ASN1Encodable stringToValue(ASN1ObjectIdentifier aSN1ObjectIdentifier, String string) {
        if (string.length() != 0 && string.charAt(0) == '#') {
            try {
                return IETFUtils.valueFromHexString(string, 1);
            }
            catch (IOException iOException) {
                throw new ASN1ParsingException("can't recode value for oid " + aSN1ObjectIdentifier.getId());
            }
        }
        if (string.length() != 0 && string.charAt(0) == '\\') {
            string = string.substring(1);
        }
        return this.encodeStringValue(aSN1ObjectIdentifier, string);
    }

    protected ASN1Encodable encodeStringValue(ASN1ObjectIdentifier aSN1ObjectIdentifier, String string) {
        return new DERUTF8String(string);
    }

    @Override
    public boolean areEqual(X500Name x500Name, X500Name x500Name2) {
        if (x500Name.size() != x500Name2.size()) {
            return false;
        }
        RDN[] rDNArray = x500Name.getRDNs();
        RDN[] rDNArray2 = x500Name2.getRDNs();
        boolean bl = false;
        AttributeTypeAndValue attributeTypeAndValue = rDNArray[0].getFirst();
        AttributeTypeAndValue attributeTypeAndValue2 = rDNArray2[0].getFirst();
        if (attributeTypeAndValue != null && attributeTypeAndValue2 != null) {
            bl = !attributeTypeAndValue.getType().equals(attributeTypeAndValue2.getType());
        }
        for (int i2 = 0; i2 != rDNArray.length; ++i2) {
            if (this.foundMatch(bl, rDNArray[i2], rDNArray2)) continue;
            return false;
        }
        return true;
    }

    private boolean foundMatch(boolean bl, RDN rDN, RDN[] rDNArray) {
        if (bl) {
            for (int i2 = rDNArray.length - 1; i2 >= 0; --i2) {
                if (rDNArray[i2] == null || !this.rdnAreEqual(rDN, rDNArray[i2])) continue;
                rDNArray[i2] = null;
                return true;
            }
        } else {
            for (int i3 = 0; i3 != rDNArray.length; ++i3) {
                if (rDNArray[i3] == null || !this.rdnAreEqual(rDN, rDNArray[i3])) continue;
                rDNArray[i3] = null;
                return true;
            }
        }
        return false;
    }

    protected boolean rdnAreEqual(RDN rDN, RDN rDN2) {
        return IETFUtils.rDNAreEqual(rDN, rDN2);
    }
}

