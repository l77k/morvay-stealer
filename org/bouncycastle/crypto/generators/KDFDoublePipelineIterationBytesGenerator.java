/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.MacDerivationFunction;
import org.bouncycastle.crypto.params.KDFDoublePipelineIterationParameters;
import org.bouncycastle.crypto.params.KeyParameter;

public class KDFDoublePipelineIterationBytesGenerator
implements MacDerivationFunction {
    private static final BigInteger INTEGER_MAX = BigInteger.valueOf(Integer.MAX_VALUE);
    private static final BigInteger TWO = BigInteger.valueOf(2L);
    private final Mac prf;
    private final int h;
    private byte[] fixedInputData;
    private int maxSizeExcl;
    private byte[] ios;
    private boolean useCounter;
    private int generatedBytes;
    private byte[] a;
    private byte[] k;

    public KDFDoublePipelineIterationBytesGenerator(Mac mac) {
        this.prf = mac;
        this.h = mac.getMacSize();
        this.a = new byte[this.h];
        this.k = new byte[this.h];
    }

    @Override
    public void init(DerivationParameters derivationParameters) {
        BigInteger bigInteger;
        if (!(derivationParameters instanceof KDFDoublePipelineIterationParameters)) {
            throw new IllegalArgumentException("Wrong type of arguments given");
        }
        KDFDoublePipelineIterationParameters kDFDoublePipelineIterationParameters = (KDFDoublePipelineIterationParameters)derivationParameters;
        this.prf.init(new KeyParameter(kDFDoublePipelineIterationParameters.getKI()));
        this.fixedInputData = kDFDoublePipelineIterationParameters.getFixedInputData();
        int n2 = kDFDoublePipelineIterationParameters.getR();
        this.ios = new byte[n2 / 8];
        this.maxSizeExcl = kDFDoublePipelineIterationParameters.useCounter() ? ((bigInteger = TWO.pow(n2).multiply(BigInteger.valueOf(this.h))).compareTo(INTEGER_MAX) == 1 ? Integer.MAX_VALUE : bigInteger.intValue()) : Integer.MAX_VALUE;
        this.useCounter = kDFDoublePipelineIterationParameters.useCounter();
        this.generatedBytes = 0;
    }

    @Override
    public Mac getMac() {
        return this.prf;
    }

    @Override
    public int generateBytes(byte[] byArray, int n2, int n3) throws DataLengthException, IllegalArgumentException {
        int n4 = this.generatedBytes + n3;
        if (n4 < 0 || n4 >= this.maxSizeExcl) {
            throw new DataLengthException("Current KDFCTR may only be used for " + this.maxSizeExcl + " bytes");
        }
        if (this.generatedBytes % this.h == 0) {
            this.generateNext();
        }
        int n5 = n3;
        int n6 = this.generatedBytes % this.h;
        int n7 = this.h - this.generatedBytes % this.h;
        int n8 = Math.min(n7, n5);
        System.arraycopy(this.k, n6, byArray, n2, n8);
        this.generatedBytes += n8;
        n5 -= n8;
        n2 += n8;
        while (n5 > 0) {
            this.generateNext();
            n8 = Math.min(this.h, n5);
            System.arraycopy(this.k, 0, byArray, n2, n8);
            this.generatedBytes += n8;
            n5 -= n8;
            n2 += n8;
        }
        return n3;
    }

    private void generateNext() {
        if (this.generatedBytes == 0) {
            this.prf.update(this.fixedInputData, 0, this.fixedInputData.length);
            this.prf.doFinal(this.a, 0);
        } else {
            this.prf.update(this.a, 0, this.a.length);
            this.prf.doFinal(this.a, 0);
        }
        this.prf.update(this.a, 0, this.a.length);
        if (this.useCounter) {
            int n2 = this.generatedBytes / this.h + 1;
            switch (this.ios.length) {
                case 4: {
                    this.ios[0] = (byte)(n2 >>> 24);
                }
                case 3: {
                    this.ios[this.ios.length - 3] = (byte)(n2 >>> 16);
                }
                case 2: {
                    this.ios[this.ios.length - 2] = (byte)(n2 >>> 8);
                }
                case 1: {
                    this.ios[this.ios.length - 1] = (byte)n2;
                    break;
                }
                default: {
                    throw new IllegalStateException("Unsupported size of counter i");
                }
            }
            this.prf.update(this.ios, 0, this.ios.length);
        }
        this.prf.update(this.fixedInputData, 0, this.fixedInputData.length);
        this.prf.doFinal(this.k, 0);
    }
}

