/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.jcajce.provider.rainbow;

import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.pqc.crypto.rainbow.RainbowParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowSigner;
import org.bouncycastle.pqc.jcajce.provider.rainbow.BCRainbowPrivateKey;
import org.bouncycastle.pqc.jcajce.provider.rainbow.BCRainbowPublicKey;
import org.bouncycastle.util.Strings;

public class SignatureSpi
extends Signature {
    private ByteArrayOutputStream bOut;
    private RainbowSigner signer;
    private SecureRandom random;
    private RainbowParameters parameters;

    protected SignatureSpi(RainbowSigner rainbowSigner) {
        super("RAINBOW");
        this.bOut = new ByteArrayOutputStream();
        this.signer = rainbowSigner;
        this.parameters = null;
    }

    protected SignatureSpi(RainbowSigner rainbowSigner, RainbowParameters rainbowParameters) {
        super(Strings.toUpperCase(rainbowParameters.getName()));
        this.parameters = rainbowParameters;
        this.bOut = new ByteArrayOutputStream();
        this.signer = rainbowSigner;
    }

    @Override
    protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        String string;
        if (!(publicKey instanceof BCRainbowPublicKey)) {
            try {
                publicKey = new BCRainbowPublicKey(SubjectPublicKeyInfo.getInstance(publicKey.getEncoded()));
            }
            catch (Exception exception) {
                throw new InvalidKeyException("unknown public key passed to Rainbow: " + exception.getMessage(), exception);
            }
        }
        BCRainbowPublicKey bCRainbowPublicKey = (BCRainbowPublicKey)publicKey;
        if (this.parameters != null && !(string = Strings.toUpperCase(this.parameters.getName())).equals(bCRainbowPublicKey.getAlgorithm())) {
            throw new InvalidKeyException("signature configured for " + string);
        }
        this.signer.init(false, bCRainbowPublicKey.getKeyParams());
    }

    @Override
    protected void engineInitSign(PrivateKey privateKey, SecureRandom secureRandom) throws InvalidKeyException {
        this.random = secureRandom;
        this.engineInitSign(privateKey);
    }

    @Override
    protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
        if (privateKey instanceof BCRainbowPrivateKey) {
            String string;
            BCRainbowPrivateKey bCRainbowPrivateKey = (BCRainbowPrivateKey)privateKey;
            RainbowPrivateKeyParameters rainbowPrivateKeyParameters = bCRainbowPrivateKey.getKeyParams();
            if (this.parameters != null && !(string = Strings.toUpperCase(this.parameters.getName())).equals(bCRainbowPrivateKey.getAlgorithm())) {
                throw new InvalidKeyException("signature configured for " + string);
            }
            if (this.random != null) {
                this.signer.init(true, new ParametersWithRandom(rainbowPrivateKeyParameters, this.random));
            } else {
                this.signer.init(true, rainbowPrivateKeyParameters);
            }
        } else {
            throw new InvalidKeyException("unknown private key passed to Rainbow");
        }
    }

    @Override
    protected void engineUpdate(byte by) throws SignatureException {
        this.bOut.write(by);
    }

    @Override
    protected void engineUpdate(byte[] byArray, int n2, int n3) throws SignatureException {
        this.bOut.write(byArray, n2, n3);
    }

    @Override
    protected byte[] engineSign() throws SignatureException {
        try {
            byte[] byArray = this.bOut.toByteArray();
            this.bOut.reset();
            return this.signer.generateSignature(byArray);
        }
        catch (Exception exception) {
            throw new SignatureException(exception.toString());
        }
    }

    @Override
    protected boolean engineVerify(byte[] byArray) throws SignatureException {
        byte[] byArray2 = this.bOut.toByteArray();
        this.bOut.reset();
        return this.signer.verifySignature(byArray2, byArray);
    }

    @Override
    protected void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    @Override
    protected void engineSetParameter(String string, Object object) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    @Override
    protected Object engineGetParameter(String string) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    public static class Base
    extends SignatureSpi {
        public Base() {
            super(new RainbowSigner());
        }
    }

    public static class RainbowIIIcircum
    extends SignatureSpi {
        public RainbowIIIcircum() {
            super(new RainbowSigner(), RainbowParameters.rainbowIIIcircumzenithal);
        }
    }

    public static class RainbowIIIclassic
    extends SignatureSpi {
        public RainbowIIIclassic() {
            super(new RainbowSigner(), RainbowParameters.rainbowIIIclassic);
        }
    }

    public static class RainbowIIIcomp
    extends SignatureSpi {
        public RainbowIIIcomp() {
            super(new RainbowSigner(), RainbowParameters.rainbowIIIcompressed);
        }
    }

    public static class RainbowVcircum
    extends SignatureSpi {
        public RainbowVcircum() {
            super(new RainbowSigner(), RainbowParameters.rainbowVcircumzenithal);
        }
    }

    public static class RainbowVclassic
    extends SignatureSpi {
        public RainbowVclassic() {
            super(new RainbowSigner(), RainbowParameters.rainbowVclassic);
        }
    }

    public static class RainbowVcomp
    extends SignatureSpi {
        public RainbowVcomp() {
            super(new RainbowSigner(), RainbowParameters.rainbowVcompressed);
        }
    }
}

