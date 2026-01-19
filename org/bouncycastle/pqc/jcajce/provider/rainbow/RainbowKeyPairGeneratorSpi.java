/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.jcajce.provider.rainbow;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.pqc.crypto.rainbow.RainbowKeyGenerationParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowKeyPairGenerator;
import org.bouncycastle.pqc.crypto.rainbow.RainbowParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPublicKeyParameters;
import org.bouncycastle.pqc.jcajce.provider.rainbow.BCRainbowPrivateKey;
import org.bouncycastle.pqc.jcajce.provider.rainbow.BCRainbowPublicKey;
import org.bouncycastle.pqc.jcajce.provider.util.SpecUtil;
import org.bouncycastle.pqc.jcajce.spec.RainbowParameterSpec;
import org.bouncycastle.util.Strings;

public class RainbowKeyPairGeneratorSpi
extends KeyPairGenerator {
    private static Map parameters = new HashMap();
    private final RainbowParameters rainbowParameters;
    RainbowKeyGenerationParameters param;
    RainbowKeyPairGenerator engine = new RainbowKeyPairGenerator();
    SecureRandom random = CryptoServicesRegistrar.getSecureRandom();
    boolean initialised = false;

    public RainbowKeyPairGeneratorSpi() {
        super("RAINBOW");
        this.rainbowParameters = null;
    }

    protected RainbowKeyPairGeneratorSpi(RainbowParameters rainbowParameters) {
        super(rainbowParameters.getName());
        this.rainbowParameters = rainbowParameters;
    }

    @Override
    public void initialize(int n2, SecureRandom secureRandom) {
        throw new IllegalArgumentException("use AlgorithmParameterSpec");
    }

    @Override
    public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        String string = RainbowKeyPairGeneratorSpi.getNameFromParams(algorithmParameterSpec);
        if (string != null && parameters.containsKey(string)) {
            RainbowParameters rainbowParameters = (RainbowParameters)parameters.get(string);
            this.param = new RainbowKeyGenerationParameters(secureRandom, rainbowParameters);
            if (this.rainbowParameters != null && !rainbowParameters.getName().equals(this.rainbowParameters.getName())) {
                throw new InvalidAlgorithmParameterException("key pair generator locked to " + Strings.toUpperCase(this.rainbowParameters.getName()));
            }
        } else {
            throw new InvalidAlgorithmParameterException("invalid ParameterSpec: " + algorithmParameterSpec);
        }
        this.engine.init(this.param);
        this.initialised = true;
    }

    private static String getNameFromParams(AlgorithmParameterSpec algorithmParameterSpec) {
        if (algorithmParameterSpec instanceof RainbowParameterSpec) {
            RainbowParameterSpec rainbowParameterSpec = (RainbowParameterSpec)algorithmParameterSpec;
            return rainbowParameterSpec.getName();
        }
        return Strings.toLowerCase(SpecUtil.getNameFrom(algorithmParameterSpec));
    }

    @Override
    public KeyPair generateKeyPair() {
        if (!this.initialised) {
            this.param = this.rainbowParameters != null ? new RainbowKeyGenerationParameters(this.random, this.rainbowParameters) : new RainbowKeyGenerationParameters(this.random, RainbowParameters.rainbowIIIclassic);
            this.engine.init(this.param);
            this.initialised = true;
        }
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = this.engine.generateKeyPair();
        RainbowPublicKeyParameters rainbowPublicKeyParameters = (RainbowPublicKeyParameters)asymmetricCipherKeyPair.getPublic();
        RainbowPrivateKeyParameters rainbowPrivateKeyParameters = (RainbowPrivateKeyParameters)asymmetricCipherKeyPair.getPrivate();
        return new KeyPair(new BCRainbowPublicKey(rainbowPublicKeyParameters), new BCRainbowPrivateKey(rainbowPrivateKeyParameters));
    }

    static {
        parameters.put(RainbowParameterSpec.rainbowIIIclassic.getName(), RainbowParameters.rainbowIIIclassic);
        parameters.put(RainbowParameterSpec.rainbowIIIcircumzenithal.getName(), RainbowParameters.rainbowIIIcircumzenithal);
        parameters.put(RainbowParameterSpec.rainbowIIIcompressed.getName(), RainbowParameters.rainbowIIIcompressed);
        parameters.put(RainbowParameterSpec.rainbowVclassic.getName(), RainbowParameters.rainbowVclassic);
        parameters.put(RainbowParameterSpec.rainbowVcircumzenithal.getName(), RainbowParameters.rainbowVcircumzenithal);
        parameters.put(RainbowParameterSpec.rainbowVcompressed.getName(), RainbowParameters.rainbowVcompressed);
    }

    public static class RainbowIIIcircum
    extends RainbowKeyPairGeneratorSpi {
        public RainbowIIIcircum() {
            super(RainbowParameters.rainbowIIIcircumzenithal);
        }
    }

    public static class RainbowIIIclassic
    extends RainbowKeyPairGeneratorSpi {
        public RainbowIIIclassic() {
            super(RainbowParameters.rainbowIIIclassic);
        }
    }

    public static class RainbowIIIcomp
    extends RainbowKeyPairGeneratorSpi {
        public RainbowIIIcomp() {
            super(RainbowParameters.rainbowIIIcompressed);
        }
    }

    public static class RainbowVcircum
    extends RainbowKeyPairGeneratorSpi {
        public RainbowVcircum() {
            super(RainbowParameters.rainbowVcircumzenithal);
        }
    }

    public static class RainbowVclassic
    extends RainbowKeyPairGeneratorSpi {
        public RainbowVclassic() {
            super(RainbowParameters.rainbowVclassic);
        }
    }

    public static class RainbowVcomp
    extends RainbowKeyPairGeneratorSpi {
        public RainbowVcomp() {
            super(RainbowParameters.rainbowVcompressed);
        }
    }
}

