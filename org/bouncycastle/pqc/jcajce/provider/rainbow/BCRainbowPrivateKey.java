/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.jcajce.provider.rainbow;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPublicKeyParameters;
import org.bouncycastle.pqc.crypto.util.PrivateKeyFactory;
import org.bouncycastle.pqc.jcajce.interfaces.RainbowPrivateKey;
import org.bouncycastle.pqc.jcajce.interfaces.RainbowPublicKey;
import org.bouncycastle.pqc.jcajce.provider.rainbow.BCRainbowPublicKey;
import org.bouncycastle.pqc.jcajce.provider.util.KeyUtil;
import org.bouncycastle.pqc.jcajce.spec.RainbowParameterSpec;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

public class BCRainbowPrivateKey
implements RainbowPrivateKey {
    private static final long serialVersionUID = 1L;
    private transient RainbowPrivateKeyParameters params;
    private transient String algorithm;
    private transient byte[] encoding;
    private transient ASN1Set attributes;

    public BCRainbowPrivateKey(RainbowPrivateKeyParameters rainbowPrivateKeyParameters) {
        this.init(rainbowPrivateKeyParameters, null);
    }

    public BCRainbowPrivateKey(PrivateKeyInfo privateKeyInfo) throws IOException {
        this.init(privateKeyInfo);
    }

    private void init(PrivateKeyInfo privateKeyInfo) throws IOException {
        this.init((RainbowPrivateKeyParameters)PrivateKeyFactory.createKey(privateKeyInfo), privateKeyInfo.getAttributes());
    }

    private void init(RainbowPrivateKeyParameters rainbowPrivateKeyParameters, ASN1Set aSN1Set) {
        this.attributes = aSN1Set;
        this.params = rainbowPrivateKeyParameters;
        this.algorithm = Strings.toUpperCase(rainbowPrivateKeyParameters.getParameters().getName());
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof BCRainbowPrivateKey) {
            BCRainbowPrivateKey bCRainbowPrivateKey = (BCRainbowPrivateKey)object;
            return Arrays.areEqual(this.getEncoded(), bCRainbowPrivateKey.getEncoded());
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
            this.encoding = KeyUtil.getEncodedPrivateKeyInfo(this.params, this.attributes);
        }
        return Arrays.clone(this.encoding);
    }

    @Override
    public RainbowParameterSpec getParameterSpec() {
        return RainbowParameterSpec.fromName(this.params.getParameters().getName());
    }

    @Override
    public String getFormat() {
        return "PKCS#8";
    }

    @Override
    public RainbowPublicKey getPublicKey() {
        return new BCRainbowPublicKey(new RainbowPublicKeyParameters(this.params.getParameters(), this.params.getPublicKey()));
    }

    RainbowPrivateKeyParameters getKeyParams() {
        return this.params;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        byte[] byArray = (byte[])objectInputStream.readObject();
        this.init(PrivateKeyInfo.getInstance(byArray));
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.getEncoded());
    }
}

