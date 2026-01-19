/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.jcajce.provider.mceliece;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.PrivateKey;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.jcajce.util.MessageDigestUtils;
import org.bouncycastle.pqc.asn1.McElieceCCA2PrivateKey;
import org.bouncycastle.pqc.asn1.PQCObjectIdentifiers;
import org.bouncycastle.pqc.crypto.util.PrivateKeyFactory;
import org.bouncycastle.pqc.legacy.crypto.mceliece.McElieceCCA2PrivateKeyParameters;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2Matrix;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2mField;
import org.bouncycastle.pqc.legacy.math.linearalgebra.Permutation;
import org.bouncycastle.pqc.legacy.math.linearalgebra.PolynomialGF2mSmallM;

public class BCMcElieceCCA2PrivateKey
implements PrivateKey {
    private static final long serialVersionUID = 1L;
    private transient McElieceCCA2PrivateKeyParameters params;

    public BCMcElieceCCA2PrivateKey(McElieceCCA2PrivateKeyParameters mcElieceCCA2PrivateKeyParameters) {
        this.params = mcElieceCCA2PrivateKeyParameters;
    }

    private void init(PrivateKeyInfo privateKeyInfo) throws IOException {
        this.params = (McElieceCCA2PrivateKeyParameters)PrivateKeyFactory.createKey(privateKeyInfo);
    }

    @Override
    public String getAlgorithm() {
        return "McEliece-CCA2";
    }

    public int getN() {
        return this.params.getN();
    }

    public int getK() {
        return this.params.getK();
    }

    public int getT() {
        return this.params.getGoppaPoly().getDegree();
    }

    public GF2mField getField() {
        return this.params.getField();
    }

    public PolynomialGF2mSmallM getGoppaPoly() {
        return this.params.getGoppaPoly();
    }

    public Permutation getP() {
        return this.params.getP();
    }

    public GF2Matrix getH() {
        return this.params.getH();
    }

    public PolynomialGF2mSmallM[] getQInv() {
        return this.params.getQInv();
    }

    public boolean equals(Object object) {
        if (object == null || !(object instanceof BCMcElieceCCA2PrivateKey)) {
            return false;
        }
        BCMcElieceCCA2PrivateKey bCMcElieceCCA2PrivateKey = (BCMcElieceCCA2PrivateKey)object;
        return this.getN() == bCMcElieceCCA2PrivateKey.getN() && this.getK() == bCMcElieceCCA2PrivateKey.getK() && this.getField().equals(bCMcElieceCCA2PrivateKey.getField()) && this.getGoppaPoly().equals(bCMcElieceCCA2PrivateKey.getGoppaPoly()) && this.getP().equals(bCMcElieceCCA2PrivateKey.getP()) && this.getH().equals(bCMcElieceCCA2PrivateKey.getH());
    }

    public int hashCode() {
        int n2 = this.params.getK();
        n2 = n2 * 37 + this.params.getN();
        n2 = n2 * 37 + this.params.getField().hashCode();
        n2 = n2 * 37 + this.params.getGoppaPoly().hashCode();
        n2 = n2 * 37 + this.params.getP().hashCode();
        return n2 * 37 + this.params.getH().hashCode();
    }

    @Override
    public byte[] getEncoded() {
        try {
            McElieceCCA2PrivateKey mcElieceCCA2PrivateKey = new McElieceCCA2PrivateKey(this.getN(), this.getK(), this.getField(), this.getGoppaPoly(), this.getP(), MessageDigestUtils.getDigestAlgID(this.params.getDigest()));
            AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PQCObjectIdentifiers.mcElieceCca2);
            PrivateKeyInfo privateKeyInfo = new PrivateKeyInfo(algorithmIdentifier, mcElieceCCA2PrivateKey);
            return privateKeyInfo.getEncoded();
        }
        catch (IOException iOException) {
            return null;
        }
    }

    @Override
    public String getFormat() {
        return "PKCS#8";
    }

    AsymmetricKeyParameter getKeyParams() {
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

