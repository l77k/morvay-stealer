/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.agreement.kdf;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.agreement.kdf.DHKDFParameters;
import org.bouncycastle.util.Pack;

public class DHKEKGenerator
implements DerivationFunction {
    private final Digest digest;
    private ASN1ObjectIdentifier algorithm;
    private int keySize;
    private byte[] z;
    private byte[] partyAInfo;

    public DHKEKGenerator(Digest digest) {
        this.digest = digest;
    }

    @Override
    public void init(DerivationParameters derivationParameters) {
        DHKDFParameters dHKDFParameters = (DHKDFParameters)derivationParameters;
        this.algorithm = dHKDFParameters.getAlgorithm();
        this.keySize = dHKDFParameters.getKeySize();
        this.z = dHKDFParameters.getZ();
        this.partyAInfo = dHKDFParameters.getExtraInfo();
    }

    public Digest getDigest() {
        return this.digest;
    }

    @Override
    public int generateBytes(byte[] byArray, int n2, int n3) throws DataLengthException, IllegalArgumentException {
        if (byArray.length - n3 < n2) {
            throw new OutputLengthException("output buffer too small");
        }
        long l2 = n3;
        int n4 = this.digest.getDigestSize();
        if (l2 > 0x1FFFFFFFFL) {
            throw new IllegalArgumentException("Output length too large");
        }
        int n5 = (int)((l2 + (long)n4 - 1L) / (long)n4);
        byte[] byArray2 = new byte[this.digest.getDigestSize()];
        int n6 = 1;
        for (int i2 = 0; i2 < n5; ++i2) {
            this.digest.update(this.z, 0, this.z.length);
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
            aSN1EncodableVector2.add(this.algorithm);
            aSN1EncodableVector2.add(new DEROctetString(Pack.intToBigEndian(n6)));
            aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
            if (this.partyAInfo != null) {
                aSN1EncodableVector.add(new DERTaggedObject(true, 0, (ASN1Encodable)new DEROctetString(this.partyAInfo)));
            }
            aSN1EncodableVector.add(new DERTaggedObject(true, 2, (ASN1Encodable)new DEROctetString(Pack.intToBigEndian(this.keySize))));
            try {
                byte[] byArray3 = new DERSequence(aSN1EncodableVector).getEncoded("DER");
                this.digest.update(byArray3, 0, byArray3.length);
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("unable to encode parameter info: " + iOException.getMessage());
            }
            this.digest.doFinal(byArray2, 0);
            if (n3 > n4) {
                System.arraycopy(byArray2, 0, byArray, n2, n4);
                n2 += n4;
                n3 -= n4;
            } else {
                System.arraycopy(byArray2, 0, byArray, n2, n3);
            }
            ++n6;
        }
        this.digest.reset();
        return (int)l2;
    }
}

