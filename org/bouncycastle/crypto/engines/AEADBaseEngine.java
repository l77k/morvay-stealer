/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.constraints.DefaultServiceProperties;
import org.bouncycastle.crypto.engines.Utils;
import org.bouncycastle.crypto.modes.AEADCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

abstract class AEADBaseEngine
implements AEADCipher {
    protected boolean forEncryption;
    protected String algorithmName;
    protected int KEY_SIZE;
    protected int IV_SIZE;
    protected int MAC_SIZE;
    protected byte[] initialAssociatedText;
    protected byte[] mac;

    AEADBaseEngine() {
    }

    @Override
    public String getAlgorithmName() {
        return this.algorithmName;
    }

    public int getKeyBytesSize() {
        return this.KEY_SIZE;
    }

    public int getIVBytesSize() {
        return this.IV_SIZE;
    }

    @Override
    public byte[] getMac() {
        return this.mac;
    }

    @Override
    public void reset() {
        this.reset(true);
    }

    @Override
    public int processByte(byte by, byte[] byArray, int n2) throws DataLengthException {
        return this.processBytes(new byte[]{by}, 0, 1, byArray, n2);
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        byte[] byArray;
        KeyParameter keyParameter;
        this.forEncryption = bl;
        if (cipherParameters instanceof AEADParameters) {
            AEADParameters aEADParameters = (AEADParameters)cipherParameters;
            keyParameter = aEADParameters.getKey();
            byArray = aEADParameters.getNonce();
            this.initialAssociatedText = aEADParameters.getAssociatedText();
            int n2 = aEADParameters.getMacSize();
            if (n2 != this.MAC_SIZE * 8) {
                throw new IllegalArgumentException("Invalid value for MAC size: " + n2);
            }
        } else if (cipherParameters instanceof ParametersWithIV) {
            ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            keyParameter = (KeyParameter)parametersWithIV.getParameters();
            byArray = parametersWithIV.getIV();
            this.initialAssociatedText = null;
        } else {
            throw new IllegalArgumentException("invalid parameters passed to " + this.algorithmName);
        }
        if (keyParameter == null) {
            throw new IllegalArgumentException(this.algorithmName + " Init parameters must include a key");
        }
        if (byArray == null || byArray.length != this.IV_SIZE) {
            throw new IllegalArgumentException(this.algorithmName + " requires exactly " + this.IV_SIZE + " bytes of IV");
        }
        byte[] byArray2 = keyParameter.getKey();
        if (byArray2.length != this.KEY_SIZE) {
            throw new IllegalArgumentException(this.algorithmName + " key must be " + this.KEY_SIZE + " bytes long");
        }
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), 128, cipherParameters, Utils.getPurpose(bl)));
        this.init(byArray2, byArray);
        if (this.initialAssociatedText != null) {
            this.processAADBytes(this.initialAssociatedText, 0, this.initialAssociatedText.length);
        }
    }

    protected abstract void init(byte[] var1, byte[] var2);

    protected void reset(boolean bl) {
        if (bl) {
            this.mac = null;
        }
    }
}

