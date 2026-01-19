/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1.x509;

import java.util.Vector;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;

public class GeneralNamesBuilder {
    private Vector names = new Vector();

    public GeneralNamesBuilder addNames(GeneralNames generalNames) {
        GeneralName[] generalNameArray = generalNames.getNames();
        for (int i2 = 0; i2 != generalNameArray.length; ++i2) {
            this.names.addElement(generalNameArray[i2]);
        }
        return this;
    }

    public GeneralNamesBuilder addName(GeneralName generalName) {
        this.names.addElement(generalName);
        return this;
    }

    public GeneralNames build() {
        GeneralName[] generalNameArray = new GeneralName[this.names.size()];
        for (int i2 = 0; i2 != generalNameArray.length; ++i2) {
            generalNameArray[i2] = (GeneralName)this.names.elementAt(i2);
        }
        return new GeneralNames(generalNameArray);
    }
}

