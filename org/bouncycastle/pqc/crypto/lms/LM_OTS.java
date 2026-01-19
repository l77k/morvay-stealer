/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.lms;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.crypto.lms.Composer;
import org.bouncycastle.pqc.crypto.lms.DigestUtil;
import org.bouncycastle.pqc.crypto.lms.LMOtsParameters;
import org.bouncycastle.pqc.crypto.lms.LMOtsPrivateKey;
import org.bouncycastle.pqc.crypto.lms.LMOtsPublicKey;
import org.bouncycastle.pqc.crypto.lms.LMOtsSignature;
import org.bouncycastle.pqc.crypto.lms.LMSContext;
import org.bouncycastle.pqc.crypto.lms.LMSException;
import org.bouncycastle.pqc.crypto.lms.LMSSignature;
import org.bouncycastle.pqc.crypto.lms.LMSigParameters;
import org.bouncycastle.pqc.crypto.lms.LmsUtils;
import org.bouncycastle.pqc.crypto.lms.SeedDerive;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

class LM_OTS {
    private static final short D_PBLC = -32640;
    private static final int ITER_K = 20;
    private static final int ITER_PREV = 23;
    private static final int ITER_J = 22;
    static final int SEED_RANDOMISER_INDEX = -3;
    static final int MAX_HASH = 32;
    static final short D_MESG = -32383;

    LM_OTS() {
    }

    public static int coef(byte[] byArray, int n2, int n3) {
        int n4 = n2 * n3 / 8;
        int n5 = 8 / n3;
        int n6 = n3 * (~n2 & n5 - 1);
        int n7 = (1 << n3) - 1;
        return byArray[n4] >>> n6 & n7;
    }

    public static int cksm(byte[] byArray, int n2, LMOtsParameters lMOtsParameters) {
        int n3 = 0;
        int n4 = lMOtsParameters.getW();
        int n5 = (1 << n4) - 1;
        for (int i2 = 0; i2 < n2 * 8 / lMOtsParameters.getW(); ++i2) {
            n3 = n3 + n5 - LM_OTS.coef(byArray, i2, lMOtsParameters.getW());
        }
        return n3 << lMOtsParameters.getLs();
    }

    public static LMOtsPublicKey lms_ots_generatePublicKey(LMOtsPrivateKey lMOtsPrivateKey) {
        byte[] byArray = LM_OTS.lms_ots_generatePublicKey(lMOtsPrivateKey.getParameter(), lMOtsPrivateKey.getI(), lMOtsPrivateKey.getQ(), lMOtsPrivateKey.getMasterSecret());
        return new LMOtsPublicKey(lMOtsPrivateKey.getParameter(), lMOtsPrivateKey.getI(), lMOtsPrivateKey.getQ(), byArray);
    }

    static byte[] lms_ots_generatePublicKey(LMOtsParameters lMOtsParameters, byte[] byArray, int n2, byte[] byArray2) {
        Digest digest = DigestUtil.getDigest(lMOtsParameters);
        byte[] byArray3 = Composer.compose().bytes(byArray).u32str(n2).u16str(-32640).padUntil(0, 22).build();
        digest.update(byArray3, 0, byArray3.length);
        Digest digest2 = DigestUtil.getDigest(lMOtsParameters);
        byte[] byArray4 = Composer.compose().bytes(byArray).u32str(n2).padUntil(0, 23 + digest2.getDigestSize()).build();
        SeedDerive seedDerive = new SeedDerive(byArray, byArray2, DigestUtil.getDigest(lMOtsParameters));
        seedDerive.setQ(n2);
        seedDerive.setJ(0);
        int n3 = lMOtsParameters.getP();
        int n4 = lMOtsParameters.getN();
        int n5 = (1 << lMOtsParameters.getW()) - 1;
        for (int i2 = 0; i2 < n3; ++i2) {
            seedDerive.deriveSeed(byArray4, i2 < n3 - 1, 23);
            Pack.shortToBigEndian((short)i2, byArray4, 20);
            for (int i3 = 0; i3 < n5; ++i3) {
                byArray4[22] = (byte)i3;
                digest2.update(byArray4, 0, byArray4.length);
                digest2.doFinal(byArray4, 23);
            }
            digest.update(byArray4, 23, n4);
        }
        byte[] byArray5 = new byte[digest.getDigestSize()];
        digest.doFinal(byArray5, 0);
        return byArray5;
    }

    public static LMOtsSignature lm_ots_generate_signature(LMSigParameters lMSigParameters, LMOtsPrivateKey lMOtsPrivateKey, byte[][] byArray, byte[] byArray2, boolean bl) {
        byte[] byArray3;
        byte[] byArray4 = new byte[34];
        if (!bl) {
            LMSContext lMSContext = lMOtsPrivateKey.getSignatureContext(lMSigParameters, byArray);
            LmsUtils.byteArray(byArray2, 0, byArray2.length, lMSContext);
            byArray3 = lMSContext.getC();
            byArray4 = lMSContext.getQ();
        } else {
            int n2 = lMOtsPrivateKey.getParameter().getN();
            byArray3 = new byte[n2];
            System.arraycopy(byArray2, 0, byArray4, 0, n2);
        }
        return LM_OTS.lm_ots_generate_signature(lMOtsPrivateKey, byArray4, byArray3);
    }

    public static LMOtsSignature lm_ots_generate_signature(LMOtsPrivateKey lMOtsPrivateKey, byte[] byArray, byte[] byArray2) {
        LMOtsParameters lMOtsParameters = lMOtsPrivateKey.getParameter();
        int n2 = lMOtsParameters.getN();
        int n3 = lMOtsParameters.getP();
        int n4 = lMOtsParameters.getW();
        byte[] byArray3 = new byte[n3 * n2];
        Digest digest = DigestUtil.getDigest(lMOtsParameters);
        SeedDerive seedDerive = lMOtsPrivateKey.getDerivationFunction();
        int n5 = LM_OTS.cksm(byArray, n2, lMOtsParameters);
        byArray[n2] = (byte)(n5 >>> 8 & 0xFF);
        byArray[n2 + 1] = (byte)n5;
        byte[] byArray4 = Composer.compose().bytes(lMOtsPrivateKey.getI()).u32str(lMOtsPrivateKey.getQ()).padUntil(0, 23 + n2).build();
        seedDerive.setJ(0);
        for (int i2 = 0; i2 < n3; ++i2) {
            Pack.shortToBigEndian((short)i2, byArray4, 20);
            seedDerive.deriveSeed(byArray4, i2 < n3 - 1, 23);
            int n6 = LM_OTS.coef(byArray, i2, n4);
            for (int i3 = 0; i3 < n6; ++i3) {
                byArray4[22] = (byte)i3;
                digest.update(byArray4, 0, 23 + n2);
                digest.doFinal(byArray4, 23);
            }
            System.arraycopy(byArray4, 23, byArray3, n2 * i2, n2);
        }
        return new LMOtsSignature(lMOtsParameters, byArray2, byArray3);
    }

    public static boolean lm_ots_validate_signature(LMOtsPublicKey lMOtsPublicKey, LMOtsSignature lMOtsSignature, byte[] byArray, boolean bl) throws LMSException {
        if (!lMOtsSignature.getType().equals(lMOtsPublicKey.getParameter())) {
            throw new LMSException("public key and signature ots types do not match");
        }
        return Arrays.areEqual(LM_OTS.lm_ots_validate_signature_calculate(lMOtsPublicKey, lMOtsSignature, byArray), lMOtsPublicKey.getK());
    }

    public static byte[] lm_ots_validate_signature_calculate(LMOtsPublicKey lMOtsPublicKey, LMOtsSignature lMOtsSignature, byte[] byArray) {
        LMSContext lMSContext = lMOtsPublicKey.createOtsContext(lMOtsSignature);
        LmsUtils.byteArray(byArray, lMSContext);
        return LM_OTS.lm_ots_validate_signature_calculate(lMSContext);
    }

    public static byte[] lm_ots_validate_signature_calculate(LMSContext lMSContext) {
        LMOtsPublicKey lMOtsPublicKey = lMSContext.getPublicKey();
        LMOtsParameters lMOtsParameters = lMOtsPublicKey.getParameter();
        Object object = lMSContext.getSignature();
        LMOtsSignature lMOtsSignature = object instanceof LMSSignature ? ((LMSSignature)object).getOtsSignature() : (LMOtsSignature)object;
        int n2 = lMOtsParameters.getN();
        int n3 = lMOtsParameters.getW();
        int n4 = lMOtsParameters.getP();
        byte[] byArray = lMSContext.getQ();
        int n5 = LM_OTS.cksm(byArray, n2, lMOtsParameters);
        byArray[n2] = (byte)(n5 >>> 8 & 0xFF);
        byArray[n2 + 1] = (byte)n5;
        byte[] byArray2 = lMOtsPublicKey.getI();
        int n6 = lMOtsPublicKey.getQ();
        Digest digest = DigestUtil.getDigest(lMOtsParameters);
        LmsUtils.byteArray(byArray2, digest);
        LmsUtils.u32str(n6, digest);
        LmsUtils.u16str((short)-32640, digest);
        byte[] byArray3 = Composer.compose().bytes(byArray2).u32str(n6).padUntil(0, 23 + n2).build();
        int n7 = (1 << n3) - 1;
        byte[] byArray4 = lMOtsSignature.getY();
        Digest digest2 = DigestUtil.getDigest(lMOtsParameters);
        for (int i2 = 0; i2 < n4; ++i2) {
            int n8;
            Pack.shortToBigEndian((short)i2, byArray3, 20);
            System.arraycopy(byArray4, i2 * n2, byArray3, 23, n2);
            for (int i3 = n8 = LM_OTS.coef(byArray, i2, n3); i3 < n7; ++i3) {
                byArray3[22] = (byte)i3;
                digest2.update(byArray3, 0, 23 + n2);
                digest2.doFinal(byArray3, 23);
            }
            digest.update(byArray3, 23, n2);
        }
        byte[] byArray5 = new byte[n2];
        digest.doFinal(byArray5, 0);
        return byArray5;
    }
}

