/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

final class ASN1Tag {
    private final int tagClass;
    private final int tagNumber;

    static ASN1Tag create(int n2, int n3) {
        return new ASN1Tag(n2, n3);
    }

    private ASN1Tag(int n2, int n3) {
        this.tagClass = n2;
        this.tagNumber = n3;
    }

    int getTagClass() {
        return this.tagClass;
    }

    int getTagNumber() {
        return this.tagNumber;
    }
}

