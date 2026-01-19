/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithID;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.DSAEncoding;
import org.bouncycastle.crypto.signers.DSAKCalculator;
import org.bouncycastle.crypto.signers.RandomDSAKCalculator;
import org.bouncycastle.crypto.signers.StandardDSAEncoding;
import org.bouncycastle.crypto.signers.Utils;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.encoders.Hex;

public class SM2Signer
implements Signer,
ECConstants {
    private final DSAKCalculator kCalculator = new RandomDSAKCalculator();
    private final Digest digest;
    private final DSAEncoding encoding;
    private int state = 0;
    private ECDomainParameters ecParams;
    private ECPoint pubPoint;
    private ECKeyParameters ecKey;
    private byte[] z;

    public SM2Signer() {
        this(StandardDSAEncoding.INSTANCE, new SM3Digest());
    }

    public SM2Signer(Digest digest) {
        this(StandardDSAEncoding.INSTANCE, digest);
    }

    public SM2Signer(DSAEncoding dSAEncoding) {
        this.encoding = dSAEncoding;
        this.digest = new SM3Digest();
    }

    public SM2Signer(DSAEncoding dSAEncoding, Digest digest) {
        this.encoding = dSAEncoding;
        this.digest = digest;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        byte[] byArray;
        CipherParameters cipherParameters2;
        if (cipherParameters instanceof ParametersWithID) {
            cipherParameters2 = ((ParametersWithID)cipherParameters).getParameters();
            byArray = ((ParametersWithID)cipherParameters).getID();
            if (byArray.length >= 8192) {
                throw new IllegalArgumentException("SM2 user ID must be less than 2^13 bits long");
            }
        } else {
            cipherParameters2 = cipherParameters;
            byArray = Hex.decodeStrict("31323334353637383132333435363738");
        }
        if (bl) {
            Object object;
            if (cipherParameters2 instanceof ParametersWithRandom) {
                object = (ParametersWithRandom)cipherParameters2;
                this.ecKey = (ECKeyParameters)((ParametersWithRandom)object).getParameters();
                this.ecParams = this.ecKey.getParameters();
                this.kCalculator.init(this.ecParams.getN(), ((ParametersWithRandom)object).getRandom());
            } else {
                this.ecKey = (ECKeyParameters)cipherParameters2;
                this.ecParams = this.ecKey.getParameters();
                this.kCalculator.init(this.ecParams.getN(), CryptoServicesRegistrar.getSecureRandom());
            }
            object = ((ECPrivateKeyParameters)this.ecKey).getD();
            BigInteger bigInteger = this.ecParams.getN().subtract(BigIntegers.ONE);
            if (((BigInteger)object).compareTo(ONE) < 0 || ((BigInteger)object).compareTo(bigInteger) >= 0) {
                throw new IllegalArgumentException("SM2 private key out of range");
            }
            this.pubPoint = this.createBasePointMultiplier().multiply(this.ecParams.getG(), (BigInteger)object).normalize();
        } else {
            this.ecKey = (ECKeyParameters)cipherParameters2;
            this.ecParams = this.ecKey.getParameters();
            this.pubPoint = ((ECPublicKeyParameters)this.ecKey).getQ();
        }
        CryptoServicesRegistrar.checkConstraints(Utils.getDefaultProperties("ECNR", this.ecKey, bl));
        this.digest.reset();
        this.z = this.getZ(byArray);
        this.state = 1;
    }

    @Override
    public void update(byte by) {
        this.checkData();
        this.digest.update(by);
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) {
        this.checkData();
        this.digest.update(byArray, n2, n3);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean verifySignature(byte[] byArray) {
        this.checkData();
        try {
            BigInteger[] bigIntegerArray = this.encoding.decode(this.ecParams.getN(), byArray);
            boolean bl = this.verifySignature(bigIntegerArray[0], bigIntegerArray[1]);
            return bl;
        }
        catch (Exception exception) {
        }
        finally {
            this.reset();
        }
        return false;
    }

    @Override
    public void reset() {
        switch (this.state) {
            case 1: {
                return;
            }
            case 2: {
                break;
            }
            default: {
                throw new IllegalStateException("SM2Signer needs to be initialized");
            }
        }
        this.digest.reset();
        this.state = 1;
    }

    @Override
    public byte[] generateSignature() throws CryptoException {
        BigInteger bigInteger;
        BigInteger bigInteger2;
        Object object;
        this.checkData();
        byte[] byArray = this.digestDoFinal();
        BigInteger bigInteger3 = this.ecParams.getN();
        BigInteger bigInteger4 = this.calculateE(bigInteger3, byArray);
        BigInteger bigInteger5 = ((ECPrivateKeyParameters)this.ecKey).getD();
        ECMultiplier eCMultiplier = this.createBasePointMultiplier();
        while (true) {
            object = this.kCalculator.nextK();
            Object object2 = eCMultiplier.multiply(this.ecParams.getG(), (BigInteger)object).normalize();
            bigInteger2 = bigInteger4.add(((ECPoint)object2).getAffineXCoord().toBigInteger()).mod(bigInteger3);
            if (bigInteger2.equals(ZERO) || bigInteger2.add((BigInteger)object).equals(bigInteger3)) continue;
            object2 = BigIntegers.modOddInverse(bigInteger3, bigInteger5.add(ONE));
            bigInteger = ((BigInteger)object).subtract(bigInteger2.multiply(bigInteger5)).mod(bigInteger3);
            if (!(bigInteger = ((BigInteger)object2).multiply(bigInteger).mod(bigInteger3)).equals(ZERO)) break;
        }
        try {
            object = this.encoding.encode(this.ecParams.getN(), bigInteger2, bigInteger);
            return object;
        }
        catch (Exception exception) {
            throw new CryptoException("unable to encode signature: " + exception.getMessage(), exception);
        }
        finally {
            this.reset();
        }
    }

    private boolean verifySignature(BigInteger bigInteger, BigInteger bigInteger2) {
        BigInteger bigInteger3 = this.ecParams.getN();
        if (bigInteger.compareTo(ONE) < 0 || bigInteger.compareTo(bigInteger3) >= 0) {
            return false;
        }
        if (bigInteger2.compareTo(ONE) < 0 || bigInteger2.compareTo(bigInteger3) >= 0) {
            return false;
        }
        byte[] byArray = this.digestDoFinal();
        BigInteger bigInteger4 = this.calculateE(bigInteger3, byArray);
        BigInteger bigInteger5 = bigInteger.add(bigInteger2).mod(bigInteger3);
        if (bigInteger5.equals(ZERO)) {
            return false;
        }
        ECPoint eCPoint = ((ECPublicKeyParameters)this.ecKey).getQ();
        ECPoint eCPoint2 = ECAlgorithms.sumOfTwoMultiplies(this.ecParams.getG(), bigInteger2, eCPoint, bigInteger5).normalize();
        if (eCPoint2.isInfinity()) {
            return false;
        }
        BigInteger bigInteger6 = bigInteger4.add(eCPoint2.getAffineXCoord().toBigInteger()).mod(bigInteger3);
        return bigInteger6.equals(bigInteger);
    }

    private void checkData() {
        switch (this.state) {
            case 1: {
                break;
            }
            case 2: {
                return;
            }
            default: {
                throw new IllegalStateException("SM2Signer needs to be initialized");
            }
        }
        this.digest.update(this.z, 0, this.z.length);
        this.state = 2;
    }

    private byte[] digestDoFinal() {
        byte[] byArray = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(byArray, 0);
        return byArray;
    }

    private byte[] getZ(byte[] byArray) {
        this.addUserID(this.digest, byArray);
        this.addFieldElement(this.digest, this.ecParams.getCurve().getA());
        this.addFieldElement(this.digest, this.ecParams.getCurve().getB());
        this.addFieldElement(this.digest, this.ecParams.getG().getAffineXCoord());
        this.addFieldElement(this.digest, this.ecParams.getG().getAffineYCoord());
        this.addFieldElement(this.digest, this.pubPoint.getAffineXCoord());
        this.addFieldElement(this.digest, this.pubPoint.getAffineYCoord());
        return this.digestDoFinal();
    }

    private void addUserID(Digest digest, byte[] byArray) {
        int n2 = byArray.length * 8;
        digest.update((byte)(n2 >>> 8));
        digest.update((byte)n2);
        digest.update(byArray, 0, byArray.length);
    }

    private void addFieldElement(Digest digest, ECFieldElement eCFieldElement) {
        byte[] byArray = eCFieldElement.getEncoded();
        digest.update(byArray, 0, byArray.length);
    }

    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }

    protected BigInteger calculateE(BigInteger bigInteger, byte[] byArray) {
        return new BigInteger(1, byArray);
    }

    private static final class State {
        static final int UNINITIALIZED = 0;
        static final int INIT = 1;
        static final int DATA = 2;

        private State() {
        }
    }
}

