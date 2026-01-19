/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.xwing;

import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.X25519KeyPairGenerator;
import org.bouncycastle.crypto.params.X25519KeyGenerationParameters;
import org.bouncycastle.pqc.crypto.mlkem.MLKEMKeyGenerationParameters;
import org.bouncycastle.pqc.crypto.mlkem.MLKEMKeyPairGenerator;
import org.bouncycastle.pqc.crypto.mlkem.MLKEMParameters;
import org.bouncycastle.pqc.crypto.xwing.XWingPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.xwing.XWingPublicKeyParameters;

public class XWingKeyPairGenerator
implements AsymmetricCipherKeyPairGenerator {
    private SecureRandom random;

    private void initialize(KeyGenerationParameters keyGenerationParameters) {
        this.random = keyGenerationParameters.getRandom();
    }

    private AsymmetricCipherKeyPair genKeyPair() {
        MLKEMKeyPairGenerator mLKEMKeyPairGenerator = new MLKEMKeyPairGenerator();
        mLKEMKeyPairGenerator.init(new MLKEMKeyGenerationParameters(this.random, MLKEMParameters.ml_kem_768));
        X25519KeyPairGenerator x25519KeyPairGenerator = new X25519KeyPairGenerator();
        x25519KeyPairGenerator.init(new X25519KeyGenerationParameters(this.random));
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = mLKEMKeyPairGenerator.generateKeyPair();
        AsymmetricCipherKeyPair asymmetricCipherKeyPair2 = x25519KeyPairGenerator.generateKeyPair();
        return new AsymmetricCipherKeyPair(new XWingPublicKeyParameters(asymmetricCipherKeyPair.getPublic(), asymmetricCipherKeyPair2.getPublic()), new XWingPrivateKeyParameters(asymmetricCipherKeyPair.getPrivate(), asymmetricCipherKeyPair2.getPrivate()));
    }

    @Override
    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.initialize(keyGenerationParameters);
    }

    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        return this.genKeyPair();
    }
}

