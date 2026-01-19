/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jce.provider;

import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.generators.PKCS12ParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS5S1ParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey;
import org.bouncycastle.jce.provider.OldPKCS12ParametersGenerator;

public interface BrokenPBE {
    public static final int MD5 = 0;
    public static final int SHA1 = 1;
    public static final int RIPEMD160 = 2;
    public static final int PKCS5S1 = 0;
    public static final int PKCS5S2 = 1;
    public static final int PKCS12 = 2;
    public static final int OLD_PKCS12 = 3;

    public static class Util {
        private static void setOddParity(byte[] byArray) {
            for (int i2 = 0; i2 < byArray.length; ++i2) {
                byte by = byArray[i2];
                byArray[i2] = (byte)(by & 0xFE | by >> 1 ^ by >> 2 ^ by >> 3 ^ by >> 4 ^ by >> 5 ^ by >> 6 ^ by >> 7 ^ 1);
            }
        }

        private static PBEParametersGenerator makePBEGenerator(int n2, int n3) {
            PBEParametersGenerator pBEParametersGenerator;
            block15: {
                block17: {
                    block16: {
                        block14: {
                            if (n2 != 0) break block14;
                            switch (n3) {
                                case 0: {
                                    pBEParametersGenerator = new PKCS5S1ParametersGenerator(new MD5Digest());
                                    break block15;
                                }
                                case 1: {
                                    pBEParametersGenerator = new PKCS5S1ParametersGenerator(new SHA1Digest());
                                    break block15;
                                }
                                default: {
                                    throw new IllegalStateException("PKCS5 scheme 1 only supports only MD5 and SHA1.");
                                }
                            }
                        }
                        if (n2 != 1) break block16;
                        pBEParametersGenerator = new PKCS5S2ParametersGenerator();
                        break block15;
                    }
                    if (n2 != 3) break block17;
                    switch (n3) {
                        case 0: {
                            pBEParametersGenerator = new OldPKCS12ParametersGenerator(new MD5Digest());
                            break block15;
                        }
                        case 1: {
                            pBEParametersGenerator = new OldPKCS12ParametersGenerator(new SHA1Digest());
                            break block15;
                        }
                        case 2: {
                            pBEParametersGenerator = new OldPKCS12ParametersGenerator(new RIPEMD160Digest());
                            break block15;
                        }
                        default: {
                            throw new IllegalStateException("unknown digest scheme for PBE encryption.");
                        }
                    }
                }
                switch (n3) {
                    case 0: {
                        pBEParametersGenerator = new PKCS12ParametersGenerator(new MD5Digest());
                        break;
                    }
                    case 1: {
                        pBEParametersGenerator = new PKCS12ParametersGenerator(new SHA1Digest());
                        break;
                    }
                    case 2: {
                        pBEParametersGenerator = new PKCS12ParametersGenerator(new RIPEMD160Digest());
                        break;
                    }
                    default: {
                        throw new IllegalStateException("unknown digest scheme for PBE encryption.");
                    }
                }
            }
            return pBEParametersGenerator;
        }

        static CipherParameters makePBEParameters(BCPBEKey bCPBEKey, AlgorithmParameterSpec algorithmParameterSpec, int n2, int n3, String string, int n4, int n5) {
            if (algorithmParameterSpec == null || !(algorithmParameterSpec instanceof PBEParameterSpec)) {
                throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
            }
            PBEParameterSpec pBEParameterSpec = (PBEParameterSpec)algorithmParameterSpec;
            PBEParametersGenerator pBEParametersGenerator = Util.makePBEGenerator(n2, n3);
            byte[] byArray = bCPBEKey.getEncoded();
            pBEParametersGenerator.init(byArray, pBEParameterSpec.getSalt(), pBEParameterSpec.getIterationCount());
            CipherParameters cipherParameters = n5 != 0 ? pBEParametersGenerator.generateDerivedParameters(n4, n5) : pBEParametersGenerator.generateDerivedParameters(n4);
            if (string.startsWith("DES")) {
                KeyParameter keyParameter;
                if (cipherParameters instanceof ParametersWithIV) {
                    keyParameter = (KeyParameter)((ParametersWithIV)cipherParameters).getParameters();
                    Util.setOddParity(keyParameter.getKey());
                } else {
                    keyParameter = (KeyParameter)cipherParameters;
                    Util.setOddParity(keyParameter.getKey());
                }
            }
            for (int i2 = 0; i2 != byArray.length; ++i2) {
                byArray[i2] = 0;
            }
            return cipherParameters;
        }

        static CipherParameters makePBEMacParameters(BCPBEKey bCPBEKey, AlgorithmParameterSpec algorithmParameterSpec, int n2, int n3, int n4) {
            if (algorithmParameterSpec == null || !(algorithmParameterSpec instanceof PBEParameterSpec)) {
                throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
            }
            PBEParameterSpec pBEParameterSpec = (PBEParameterSpec)algorithmParameterSpec;
            PBEParametersGenerator pBEParametersGenerator = Util.makePBEGenerator(n2, n3);
            byte[] byArray = bCPBEKey.getEncoded();
            pBEParametersGenerator.init(byArray, pBEParameterSpec.getSalt(), pBEParameterSpec.getIterationCount());
            CipherParameters cipherParameters = pBEParametersGenerator.generateDerivedMacParameters(n4);
            for (int i2 = 0; i2 != byArray.length; ++i2) {
                byArray[i2] = 0;
            }
            return cipherParameters;
        }
    }
}

