/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.internal.asn1.misc;

import org.bouncycastle.asn1.ASN1BitString;
import org.bouncycastle.asn1.DERBitString;

public class NetscapeCertType
extends DERBitString {
    public static final int sslClient = 128;
    public static final int sslServer = 64;
    public static final int smime = 32;
    public static final int objectSigning = 16;
    public static final int reserved = 8;
    public static final int sslCA = 4;
    public static final int smimeCA = 2;
    public static final int objectSigningCA = 1;

    public NetscapeCertType(int n2) {
        super(NetscapeCertType.getBytes(n2), NetscapeCertType.getPadBits(n2));
    }

    public NetscapeCertType(ASN1BitString aSN1BitString) {
        super(aSN1BitString.getBytes(), aSN1BitString.getPadBits());
    }

    public boolean hasUsages(int n2) {
        return (this.intValue() & n2) == n2;
    }

    @Override
    public String toString() {
        return "NetscapeCertType: 0x" + Integer.toHexString(this.intValue());
    }
}

