/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1.x509;

import java.math.BigInteger;
import java.util.Hashtable;
import org.bouncycastle.asn1.ASN1Enumerated;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.util.Integers;

public class CRLReason
extends ASN1Object {
    public static final int UNSPECIFIED = 0;
    public static final int KEY_COMPROMISE = 1;
    public static final int CA_COMPROMISE = 2;
    public static final int AFFILIATION_CHANGED = 3;
    public static final int SUPERSEDED = 4;
    public static final int CESSATION_OF_OPERATION = 5;
    public static final int CERTIFICATE_HOLD = 6;
    public static final int REMOVE_FROM_CRL = 8;
    public static final int PRIVILEGE_WITHDRAWN = 9;
    public static final int AA_COMPROMISE = 10;
    public static final int unspecified = 0;
    public static final int keyCompromise = 1;
    public static final int cACompromise = 2;
    public static final int affiliationChanged = 3;
    public static final int superseded = 4;
    public static final int cessationOfOperation = 5;
    public static final int certificateHold = 6;
    public static final int removeFromCRL = 8;
    public static final int privilegeWithdrawn = 9;
    public static final int aACompromise = 10;
    private static final String[] reasonString = new String[]{"unspecified", "keyCompromise", "cACompromise", "affiliationChanged", "superseded", "cessationOfOperation", "certificateHold", "unknown", "removeFromCRL", "privilegeWithdrawn", "aACompromise"};
    private static final Hashtable table = new Hashtable();
    private ASN1Enumerated value;

    public static CRLReason getInstance(Object object) {
        if (object instanceof CRLReason) {
            return (CRLReason)object;
        }
        if (object != null) {
            return CRLReason.lookup(ASN1Enumerated.getInstance(object).intValueExact());
        }
        return null;
    }

    private CRLReason(int n2) {
        if (n2 < 0) {
            throw new IllegalArgumentException("Invalid CRL reason : not in (0..MAX)");
        }
        this.value = new ASN1Enumerated(n2);
    }

    public String toString() {
        int n2 = this.getValue().intValue();
        String string = n2 < 0 || n2 > 10 ? "invalid" : reasonString[n2];
        return "CRLReason: " + string;
    }

    public BigInteger getValue() {
        return this.value.getValue();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.value;
    }

    public static CRLReason lookup(int n2) {
        Integer n3 = Integers.valueOf(n2);
        if (!table.containsKey(n3)) {
            table.put(n3, new CRLReason(n2));
        }
        return (CRLReason)table.get(n3);
    }
}

