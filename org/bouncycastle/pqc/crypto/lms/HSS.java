/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.lms;

import java.util.Arrays;
import java.util.List;
import org.bouncycastle.pqc.crypto.ExhaustedPrivateKeyException;
import org.bouncycastle.pqc.crypto.lms.HSSKeyGenerationParameters;
import org.bouncycastle.pqc.crypto.lms.HSSPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.lms.HSSPublicKeyParameters;
import org.bouncycastle.pqc.crypto.lms.HSSSignature;
import org.bouncycastle.pqc.crypto.lms.LMOtsParameters;
import org.bouncycastle.pqc.crypto.lms.LMOtsPrivateKey;
import org.bouncycastle.pqc.crypto.lms.LMS;
import org.bouncycastle.pqc.crypto.lms.LMSContext;
import org.bouncycastle.pqc.crypto.lms.LMSPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.lms.LMSPublicKeyParameters;
import org.bouncycastle.pqc.crypto.lms.LMSSignature;
import org.bouncycastle.pqc.crypto.lms.LMSSignedPubKey;
import org.bouncycastle.pqc.crypto.lms.LMSigParameters;

class HSS {
    HSS() {
    }

    public static HSSPrivateKeyParameters generateHSSKeyPair(HSSKeyGenerationParameters hSSKeyGenerationParameters) {
        LMSPrivateKeyParameters[] lMSPrivateKeyParametersArray = new LMSPrivateKeyParameters[hSSKeyGenerationParameters.getDepth()];
        LMSSignature[] lMSSignatureArray = new LMSSignature[hSSKeyGenerationParameters.getDepth() - 1];
        byte[] byArray = new byte[hSSKeyGenerationParameters.getLmsParameters()[0].getLMSigParam().getM()];
        hSSKeyGenerationParameters.getRandom().nextBytes(byArray);
        byte[] byArray2 = new byte[16];
        hSSKeyGenerationParameters.getRandom().nextBytes(byArray2);
        byte[] byArray3 = new byte[]{};
        long l2 = 1L;
        for (int i2 = 0; i2 < lMSPrivateKeyParametersArray.length; ++i2) {
            lMSPrivateKeyParametersArray[i2] = i2 == 0 ? new LMSPrivateKeyParameters(hSSKeyGenerationParameters.getLmsParameters()[i2].getLMSigParam(), hSSKeyGenerationParameters.getLmsParameters()[i2].getLMOTSParam(), 0, byArray2, 1 << hSSKeyGenerationParameters.getLmsParameters()[i2].getLMSigParam().getH(), byArray) : new PlaceholderLMSPrivateKey(hSSKeyGenerationParameters.getLmsParameters()[i2].getLMSigParam(), hSSKeyGenerationParameters.getLmsParameters()[i2].getLMOTSParam(), -1, byArray3, 1 << hSSKeyGenerationParameters.getLmsParameters()[i2].getLMSigParam().getH(), byArray3);
            l2 *= (long)(1 << hSSKeyGenerationParameters.getLmsParameters()[i2].getLMSigParam().getH());
        }
        if (l2 == 0L) {
            l2 = Long.MAX_VALUE;
        }
        return new HSSPrivateKeyParameters(hSSKeyGenerationParameters.getDepth(), Arrays.asList(lMSPrivateKeyParametersArray), Arrays.asList(lMSSignatureArray), 0L, l2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void incrementIndex(HSSPrivateKeyParameters hSSPrivateKeyParameters) {
        HSSPrivateKeyParameters hSSPrivateKeyParameters2 = hSSPrivateKeyParameters;
        synchronized (hSSPrivateKeyParameters2) {
            HSS.rangeTestKeys(hSSPrivateKeyParameters);
            hSSPrivateKeyParameters.incIndex();
            hSSPrivateKeyParameters.getKeys().get(hSSPrivateKeyParameters.getL() - 1).incIndex();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static void rangeTestKeys(HSSPrivateKeyParameters hSSPrivateKeyParameters) {
        HSSPrivateKeyParameters hSSPrivateKeyParameters2 = hSSPrivateKeyParameters;
        synchronized (hSSPrivateKeyParameters2) {
            int n2;
            if (hSSPrivateKeyParameters.getIndex() >= hSSPrivateKeyParameters.getIndexLimit()) {
                throw new ExhaustedPrivateKeyException("hss private key" + (hSSPrivateKeyParameters.isShard() ? " shard" : "") + " is exhausted");
            }
            int n3 = n2 = hSSPrivateKeyParameters.getL();
            List<LMSPrivateKeyParameters> list = hSSPrivateKeyParameters.getKeys();
            while (list.get(n3 - 1).getIndex() == 1 << list.get(n3 - 1).getSigParameters().getH()) {
                if (--n3 != 0) continue;
                throw new ExhaustedPrivateKeyException("hss private key" + (hSSPrivateKeyParameters.isShard() ? " shard" : "") + " is exhausted the maximum limit for this HSS private key");
            }
            while (n3 < n2) {
                hSSPrivateKeyParameters.replaceConsumedKey(n3);
                ++n3;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static HSSSignature generateSignature(HSSPrivateKeyParameters hSSPrivateKeyParameters, byte[] byArray) {
        LMSSignedPubKey[] lMSSignedPubKeyArray;
        LMSPrivateKeyParameters lMSPrivateKeyParameters;
        int n2 = hSSPrivateKeyParameters.getL();
        Object object = hSSPrivateKeyParameters;
        synchronized (object) {
            HSS.rangeTestKeys(hSSPrivateKeyParameters);
            List<LMSPrivateKeyParameters> list = hSSPrivateKeyParameters.getKeys();
            List<LMSSignature> list2 = hSSPrivateKeyParameters.getSig();
            lMSPrivateKeyParameters = hSSPrivateKeyParameters.getKeys().get(n2 - 1);
            lMSSignedPubKeyArray = new LMSSignedPubKey[n2 - 1];
            for (int i2 = 0; i2 < n2 - 1; ++i2) {
                lMSSignedPubKeyArray[i2] = new LMSSignedPubKey(list2.get(i2), list.get(i2 + 1).getPublicKey());
            }
            hSSPrivateKeyParameters.incIndex();
        }
        object = lMSPrivateKeyParameters.generateLMSContext().withSignedPublicKeys(lMSSignedPubKeyArray);
        ((LMSContext)object).update(byArray, 0, byArray.length);
        return HSS.generateSignature(n2, (LMSContext)object);
    }

    public static HSSSignature generateSignature(int n2, LMSContext lMSContext) {
        return new HSSSignature(n2 - 1, lMSContext.getSignedPubKeys(), LMS.generateSign(lMSContext));
    }

    public static boolean verifySignature(HSSPublicKeyParameters hSSPublicKeyParameters, HSSSignature hSSSignature, byte[] byArray) {
        int n2 = hSSSignature.getlMinus1();
        if (n2 + 1 != hSSPublicKeyParameters.getL()) {
            return false;
        }
        LMSSignature[] lMSSignatureArray = new LMSSignature[n2 + 1];
        LMSPublicKeyParameters[] lMSPublicKeyParametersArray = new LMSPublicKeyParameters[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            lMSSignatureArray[i2] = hSSSignature.getSignedPubKey()[i2].getSignature();
            lMSPublicKeyParametersArray[i2] = hSSSignature.getSignedPubKey()[i2].getPublicKey();
        }
        lMSSignatureArray[n2] = hSSSignature.getSignature();
        LMSPublicKeyParameters lMSPublicKeyParameters = hSSPublicKeyParameters.getLMSPublicKey();
        for (int i3 = 0; i3 < n2; ++i3) {
            LMSSignature lMSSignature = lMSSignatureArray[i3];
            byte[] byArray2 = lMSPublicKeyParametersArray[i3].toByteArray();
            if (!LMS.verifySignature(lMSPublicKeyParameters, lMSSignature, byArray2)) {
                return false;
            }
            try {
                lMSPublicKeyParameters = lMSPublicKeyParametersArray[i3];
                continue;
            }
            catch (Exception exception) {
                throw new IllegalStateException(exception.getMessage(), exception);
            }
        }
        return LMS.verifySignature(lMSPublicKeyParameters, lMSSignatureArray[n2], byArray);
    }

    static class PlaceholderLMSPrivateKey
    extends LMSPrivateKeyParameters {
        public PlaceholderLMSPrivateKey(LMSigParameters lMSigParameters, LMOtsParameters lMOtsParameters, int n2, byte[] byArray, int n3, byte[] byArray2) {
            super(lMSigParameters, lMOtsParameters, n2, byArray, n3, byArray2);
        }

        @Override
        LMOtsPrivateKey getNextOtsPrivateKey() {
            throw new RuntimeException("placeholder only");
        }

        @Override
        public LMSPublicKeyParameters getPublicKey() {
            throw new RuntimeException("placeholder only");
        }
    }
}

