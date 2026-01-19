/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1.bc;

import java.util.Iterator;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.bc.ObjectData;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Iterable;

public class ObjectDataSequence
extends ASN1Object
implements Iterable<ASN1Encodable> {
    private final ASN1Encodable[] dataSequence;

    public ObjectDataSequence(ObjectData[] objectDataArray) {
        this.dataSequence = new ASN1Encodable[objectDataArray.length];
        System.arraycopy(objectDataArray, 0, this.dataSequence, 0, objectDataArray.length);
    }

    private ObjectDataSequence(ASN1Sequence aSN1Sequence) {
        this.dataSequence = new ASN1Encodable[aSN1Sequence.size()];
        for (int i2 = 0; i2 != this.dataSequence.length; ++i2) {
            this.dataSequence[i2] = ObjectData.getInstance(aSN1Sequence.getObjectAt(i2));
        }
    }

    public static ObjectDataSequence getInstance(Object object) {
        if (object instanceof ObjectDataSequence) {
            return (ObjectDataSequence)object;
        }
        if (object != null) {
            return new ObjectDataSequence(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(this.dataSequence);
    }

    @Override
    public Iterator<ASN1Encodable> iterator() {
        return new Arrays.Iterator<ASN1Encodable>(this.dataSequence);
    }
}

