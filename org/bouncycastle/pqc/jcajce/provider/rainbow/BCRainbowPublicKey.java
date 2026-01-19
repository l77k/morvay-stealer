/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.jcajce.provider.rainbow;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPublicKeyParameters;
import org.bouncycastle.pqc.crypto.util.PublicKeyFactory;
import org.bouncycastle.pqc.jcajce.interfaces.RainbowPublicKey;
import org.bouncycastle.pqc.jcajce.provider.util.KeyUtil;
import org.bouncycastle.pqc.jcajce.spec.RainbowParameterSpec;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

public class BCRainbowPublicKey
implements RainbowPublicKey {
    private static final long serialVersionUID = 1L;
    private transient RainbowPublicKeyParameters params;
    private transient String algorithm;
    private transient byte[] encoding;

    public BCRainbowPublicKey(RainbowPublicKeyParameters rainbowPublicKeyParameters) {
        this.init(rainbowPublicKeyParameters);
    }

    public BCRainbowPublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) throws IOException {
        this.init(subjectPublicKeyInfo);
    }

    private void init(SubjectPublicKeyInfo subjectPublicKeyInfo) throws IOException {
        this.init((RainbowPublicKeyParameters)PublicKeyFactory.createKey(subjectPublicKeyInfo));
    }

    private void init(RainbowPublicKeyParameters rainbowPublicKeyParameters) {
        this.params = rainbowPublicKeyParameters;
        this.algorithm = Strings.toUpperCase(rainbowPublicKeyParameters.getParameters().getName());
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof BCRainbowPublicKey) {
            BCRainbowPublicKey bCRainbowPublicKey = (BCRainbowPublicKey)object;
            return Arrays.areEqual(this.getEncoded(), bCRainbowPublicKey.getEncoded());
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(this.getEncoded());
    }

    @Override
    public final String getAlgorithm() {
        return this.algorithm;
    }

    @Override
    public byte[] getEncoded() {
        if (this.encoding == null) {
            this.encoding = KeyUtil.getEncodedSubjectPublicKeyInfo(this.params);
        }
        return Arrays.clone(this.encoding);
    }

    @Override
    public String getFormat() {
        return "X.509";
    }

    @Override
    public RainbowParameterSpec getParameterSpec() {
        return RainbowParameterSpec.fromName(this.params.getParameters().getName());
    }

    RainbowPublicKeyParameters getKeyParams() {
        return this.params;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        byte[] byArray = (byte[])objectInputStream.readObject();
        this.init(SubjectPublicKeyInfo.getInstance(byArray));
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.getEncoded());
    }
}

