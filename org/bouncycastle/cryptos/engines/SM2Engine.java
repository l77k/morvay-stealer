/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.constraints.ConstraintUtils;
import org.bouncycastle.crypto.constraints.DefaultServiceProperties;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.engines.Utils;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.Bytes;
import org.bouncycastle.util.Memoable;
import org.bouncycastle.util.Pack;

public class SM2Engine {
    private final Digest digest;
    private final Mode mode;
    private boolean forEncryption;
    private ECKeyParameters ecKey;
    private ECDomainParameters ecParams;
    private int curveLength;
    private SecureRandom random;

    public SM2Engine() {
        this(new SM3Digest());
    }

    public SM2Engine(Mode mode) {
        this(new SM3Digest(), mode);
    }

    public SM2Engine(Digest digest) {
        this(digest, Mode.C1C2C3);
    }

    public SM2Engine(Digest digest, Mode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("mode cannot be NULL");
        }
        this.digest = digest;
        this.mode = mode;
    }

    public void init(boolean bl, CipherParameters cipherParameters) {
        this.forEncryption = bl;
        if (bl) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.ecKey = (ECKeyParameters)parametersWithRandom.getParameters();
            this.ecParams = this.ecKey.getParameters();
            ECPoint eCPoint = ((ECPublicKeyParameters)this.ecKey).getQ().multiply(this.ecParams.getH());
            if (eCPoint.isInfinity()) {
                throw new IllegalArgumentException("invalid key: [h]Q at infinity");
            }
            this.random = parametersWithRandom.getRandom();
        } else {
            this.ecKey = (ECKeyParameters)cipherParameters;
            this.ecParams = this.ecKey.getParameters();
        }
        this.curveLength = this.ecParams.getCurve().getFieldElementEncodingLength();
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties("SM2", ConstraintUtils.bitsOfSecurityFor(this.ecParams.getCurve()), this.ecKey, Utils.getPurpose(bl)));
    }

    public byte[] processBlock(byte[] byArray, int n2, int n3) throws InvalidCipherTextException {
        if (n2 + n3 > byArray.length || n3 == 0) {
            throw new DataLengthException("input buffer too short");
        }
        if (this.forEncryption) {
            return this.encrypt(byArray, n2, n3);
        }
        return this.decrypt(byArray, n2, n3);
    }

    public int getOutputSize(int n2) {
        return 1 + 2 * this.curveLength + n2 + this.digest.getDigestSize();
    }

    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }

    private byte[] encrypt(byte[] byArray, int n2, int n3) throws InvalidCipherTextException {
        ECPoint eCPoint;
        byte[] byArray2;
        Object object;
        byte[] byArray3 = new byte[n3];
        System.arraycopy(byArray, n2, byArray3, 0, byArray3.length);
        ECMultiplier eCMultiplier = this.createBasePointMultiplier();
        do {
            object = this.nextK();
            ECPoint eCPoint2 = eCMultiplier.multiply(this.ecParams.getG(), (BigInteger)object).normalize();
            byArray2 = eCPoint2.getEncoded(false);
            eCPoint = ((ECPublicKeyParameters)this.ecKey).getQ().multiply((BigInteger)object).normalize();
            this.kdf(this.digest, eCPoint, byArray3);
        } while (this.notEncrypted(byArray3, byArray, n2));
        object = new byte[this.digest.getDigestSize()];
        this.addFieldElement(this.digest, eCPoint.getAffineXCoord());
        this.digest.update(byArray, n2, n3);
        this.addFieldElement(this.digest, eCPoint.getAffineYCoord());
        this.digest.doFinal((byte[])object, 0);
        switch (this.mode.ordinal()) {
            case 1: {
                return Arrays.concatenate(byArray2, (byte[])object, byArray3);
            }
        }
        return Arrays.concatenate(byArray2, byArray3, (byte[])object);
    }

    private byte[] decrypt(byte[] byArray, int n2, int n3) throws InvalidCipherTextException {
        byte[] byArray2 = new byte[this.curveLength * 2 + 1];
        System.arraycopy(byArray, n2, byArray2, 0, byArray2.length);
        ECPoint eCPoint = this.ecParams.getCurve().decodePoint(byArray2);
        ECPoint eCPoint2 = eCPoint.multiply(this.ecParams.getH());
        if (eCPoint2.isInfinity()) {
            throw new InvalidCipherTextException("[h]C1 at infinity");
        }
        eCPoint = eCPoint.multiply(((ECPrivateKeyParameters)this.ecKey).getD()).normalize();
        int n4 = this.digest.getDigestSize();
        byte[] byArray3 = new byte[n3 - byArray2.length - n4];
        if (this.mode == Mode.C1C3C2) {
            System.arraycopy(byArray, n2 + byArray2.length + n4, byArray3, 0, byArray3.length);
        } else {
            System.arraycopy(byArray, n2 + byArray2.length, byArray3, 0, byArray3.length);
        }
        this.kdf(this.digest, eCPoint, byArray3);
        byte[] byArray4 = new byte[this.digest.getDigestSize()];
        this.addFieldElement(this.digest, eCPoint.getAffineXCoord());
        this.digest.update(byArray3, 0, byArray3.length);
        this.addFieldElement(this.digest, eCPoint.getAffineYCoord());
        this.digest.doFinal(byArray4, 0);
        int n5 = 0;
        if (this.mode == Mode.C1C3C2) {
            for (int i2 = 0; i2 != byArray4.length; ++i2) {
                n5 |= byArray4[i2] ^ byArray[n2 + byArray2.length + i2];
            }
        } else {
            for (int i3 = 0; i3 != byArray4.length; ++i3) {
                n5 |= byArray4[i3] ^ byArray[n2 + byArray2.length + byArray3.length + i3];
            }
        }
        Arrays.fill(byArray2, (byte)0);
        Arrays.fill(byArray4, (byte)0);
        if (n5 != 0) {
            Arrays.fill(byArray3, (byte)0);
            throw new InvalidCipherTextException("invalid cipher text");
        }
        return byArray3;
    }

    private boolean notEncrypted(byte[] byArray, byte[] byArray2, int n2) {
        for (int i2 = 0; i2 != byArray.length; ++i2) {
            if (byArray[i2] == byArray2[n2 + i2]) continue;
            return false;
        }
        return true;
    }

    private void kdf(Digest digest, ECPoint eCPoint, byte[] byArray) {
        int n2 = digest.getDigestSize();
        byte[] byArray2 = new byte[Math.max(4, n2)];
        int n3 = 0;
        Memoable memoable = null;
        Memoable memoable2 = null;
        if (digest instanceof Memoable) {
            this.addFieldElement(digest, eCPoint.getAffineXCoord());
            this.addFieldElement(digest, eCPoint.getAffineYCoord());
            memoable = (Memoable)((Object)digest);
            memoable2 = memoable.copy();
        }
        int n4 = 0;
        while (n3 < byArray.length) {
            if (memoable != null) {
                memoable.reset(memoable2);
            } else {
                this.addFieldElement(digest, eCPoint.getAffineXCoord());
                this.addFieldElement(digest, eCPoint.getAffineYCoord());
            }
            Pack.intToBigEndian(++n4, byArray2, 0);
            digest.update(byArray2, 0, 4);
            digest.doFinal(byArray2, 0);
            int n5 = Math.min(n2, byArray.length - n3);
            Bytes.xorTo(n5, byArray2, 0, byArray, n3);
            n3 += n5;
        }
    }

    private BigInteger nextK() {
        BigInteger bigInteger;
        int n2 = this.ecParams.getN().bitLength();
        while ((bigInteger = BigIntegers.createRandomBigInteger(n2, this.random)).equals(BigIntegers.ZERO) || bigInteger.compareTo(this.ecParams.getN()) >= 0) {
        }
        return bigInteger;
    }

    private void addFieldElement(Digest digest, ECFieldElement eCFieldElement) {
        byte[] byArray = BigIntegers.asUnsignedByteArray(this.curveLength, eCFieldElement.toBigInteger());
        digest.update(byArray, 0, byArray.length);
    }

    public static enum Mode {
        C1C2C3,
        C1C3C2;

    }
}

