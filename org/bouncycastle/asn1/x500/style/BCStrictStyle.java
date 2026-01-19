/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1.x500.style;

import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameStyle;
import org.bouncycastle.asn1.x500.style.BCStyle;

public class BCStrictStyle
extends BCStyle {
    public static final X500NameStyle INSTANCE = new BCStrictStyle();

    @Override
    public boolean areEqual(X500Name x500Name, X500Name x500Name2) {
        if (x500Name.size() != x500Name2.size()) {
            return false;
        }
        RDN[] rDNArray = x500Name.getRDNs();
        RDN[] rDNArray2 = x500Name2.getRDNs();
        for (int i2 = 0; i2 != rDNArray.length; ++i2) {
            if (this.rdnAreEqual(rDNArray[i2], rDNArray2[i2])) continue;
            return false;
        }
        return true;
    }
}

