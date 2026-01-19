/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.PolicyInformation;

public class CertificatePolicies
extends ASN1Object {
    private final PolicyInformation[] policyInformation;

    private static PolicyInformation[] copy(PolicyInformation[] policyInformationArray) {
        PolicyInformation[] policyInformationArray2 = new PolicyInformation[policyInformationArray.length];
        System.arraycopy(policyInformationArray, 0, policyInformationArray2, 0, policyInformationArray.length);
        return policyInformationArray2;
    }

    public static CertificatePolicies getInstance(Object object) {
        if (object instanceof CertificatePolicies) {
            return (CertificatePolicies)object;
        }
        if (object != null) {
            return new CertificatePolicies(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static CertificatePolicies getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return CertificatePolicies.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public static CertificatePolicies fromExtensions(Extensions extensions) {
        return CertificatePolicies.getInstance(Extensions.getExtensionParsedValue(extensions, Extension.certificatePolicies));
    }

    public CertificatePolicies(PolicyInformation policyInformation) {
        this.policyInformation = new PolicyInformation[]{policyInformation};
    }

    public CertificatePolicies(PolicyInformation[] policyInformationArray) {
        this.policyInformation = CertificatePolicies.copy(policyInformationArray);
    }

    private CertificatePolicies(ASN1Sequence aSN1Sequence) {
        this.policyInformation = new PolicyInformation[aSN1Sequence.size()];
        for (int i2 = 0; i2 != aSN1Sequence.size(); ++i2) {
            this.policyInformation[i2] = PolicyInformation.getInstance(aSN1Sequence.getObjectAt(i2));
        }
    }

    public PolicyInformation[] getPolicyInformation() {
        return CertificatePolicies.copy(this.policyInformation);
    }

    public PolicyInformation getPolicyInformation(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        for (int i2 = 0; i2 != this.policyInformation.length; ++i2) {
            if (!aSN1ObjectIdentifier.equals(this.policyInformation[i2].getPolicyIdentifier())) continue;
            return this.policyInformation[i2];
        }
        return null;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(this.policyInformation);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < this.policyInformation.length; ++i2) {
            if (stringBuffer.length() != 0) {
                stringBuffer.append(", ");
            }
            stringBuffer.append(this.policyInformation[i2]);
        }
        return "CertificatePolicies: [" + stringBuffer + "]";
    }
}

