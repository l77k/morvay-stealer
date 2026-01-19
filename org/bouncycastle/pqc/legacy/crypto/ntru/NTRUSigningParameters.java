/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.ntru;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;

public class NTRUSigningParameters
implements Cloneable {
    public int N;
    public int q;
    public int d;
    public int d1;
    public int d2;
    public int d3;
    public int B;
    double beta;
    public double betaSq;
    double normBound;
    public double normBoundSq;
    public int signFailTolerance = 100;
    int bitsF = 6;
    public Digest hashAlg;

    public NTRUSigningParameters(int n2, int n3, int n4, int n5, double d2, double d3, Digest digest) {
        this.N = n2;
        this.q = n3;
        this.d = n4;
        this.B = n5;
        this.beta = d2;
        this.normBound = d3;
        this.hashAlg = digest;
        this.init();
    }

    public NTRUSigningParameters(int n2, int n3, int n4, int n5, int n6, int n7, double d2, double d3, double d4, Digest digest) {
        this.N = n2;
        this.q = n3;
        this.d1 = n4;
        this.d2 = n5;
        this.d3 = n6;
        this.B = n7;
        this.beta = d2;
        this.normBound = d3;
        this.hashAlg = digest;
        this.init();
    }

    private void init() {
        this.betaSq = this.beta * this.beta;
        this.normBoundSq = this.normBound * this.normBound;
    }

    public NTRUSigningParameters(InputStream inputStream2) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(inputStream2);
        this.N = dataInputStream.readInt();
        this.q = dataInputStream.readInt();
        this.d = dataInputStream.readInt();
        this.d1 = dataInputStream.readInt();
        this.d2 = dataInputStream.readInt();
        this.d3 = dataInputStream.readInt();
        this.B = dataInputStream.readInt();
        this.beta = dataInputStream.readDouble();
        this.normBound = dataInputStream.readDouble();
        this.signFailTolerance = dataInputStream.readInt();
        this.bitsF = dataInputStream.readInt();
        String string = dataInputStream.readUTF();
        if ("SHA-512".equals(string)) {
            this.hashAlg = new SHA512Digest();
        } else if ("SHA-256".equals(string)) {
            this.hashAlg = new SHA256Digest();
        }
        this.init();
    }

    public void writeTo(OutputStream outputStream2) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream2);
        dataOutputStream.writeInt(this.N);
        dataOutputStream.writeInt(this.q);
        dataOutputStream.writeInt(this.d);
        dataOutputStream.writeInt(this.d1);
        dataOutputStream.writeInt(this.d2);
        dataOutputStream.writeInt(this.d3);
        dataOutputStream.writeInt(this.B);
        dataOutputStream.writeDouble(this.beta);
        dataOutputStream.writeDouble(this.normBound);
        dataOutputStream.writeInt(this.signFailTolerance);
        dataOutputStream.writeInt(this.bitsF);
        dataOutputStream.writeUTF(this.hashAlg.getAlgorithmName());
    }

    public NTRUSigningParameters clone() {
        return new NTRUSigningParameters(this.N, this.q, this.d, this.B, this.beta, this.normBound, this.hashAlg);
    }

    public int hashCode() {
        int n2 = 1;
        n2 = 31 * n2 + this.B;
        n2 = 31 * n2 + this.N;
        long l2 = Double.doubleToLongBits(this.beta);
        n2 = 31 * n2 + (int)(l2 ^ l2 >>> 32);
        l2 = Double.doubleToLongBits(this.betaSq);
        n2 = 31 * n2 + (int)(l2 ^ l2 >>> 32);
        n2 = 31 * n2 + this.bitsF;
        n2 = 31 * n2 + this.d;
        n2 = 31 * n2 + this.d1;
        n2 = 31 * n2 + this.d2;
        n2 = 31 * n2 + this.d3;
        n2 = 31 * n2 + (this.hashAlg == null ? 0 : this.hashAlg.getAlgorithmName().hashCode());
        l2 = Double.doubleToLongBits(this.normBound);
        n2 = 31 * n2 + (int)(l2 ^ l2 >>> 32);
        l2 = Double.doubleToLongBits(this.normBoundSq);
        n2 = 31 * n2 + (int)(l2 ^ l2 >>> 32);
        n2 = 31 * n2 + this.q;
        n2 = 31 * n2 + this.signFailTolerance;
        return n2;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof NTRUSigningParameters)) {
            return false;
        }
        NTRUSigningParameters nTRUSigningParameters = (NTRUSigningParameters)object;
        if (this.B != nTRUSigningParameters.B) {
            return false;
        }
        if (this.N != nTRUSigningParameters.N) {
            return false;
        }
        if (Double.doubleToLongBits(this.beta) != Double.doubleToLongBits(nTRUSigningParameters.beta)) {
            return false;
        }
        if (Double.doubleToLongBits(this.betaSq) != Double.doubleToLongBits(nTRUSigningParameters.betaSq)) {
            return false;
        }
        if (this.bitsF != nTRUSigningParameters.bitsF) {
            return false;
        }
        if (this.d != nTRUSigningParameters.d) {
            return false;
        }
        if (this.d1 != nTRUSigningParameters.d1) {
            return false;
        }
        if (this.d2 != nTRUSigningParameters.d2) {
            return false;
        }
        if (this.d3 != nTRUSigningParameters.d3) {
            return false;
        }
        if (this.hashAlg == null ? nTRUSigningParameters.hashAlg != null : !this.hashAlg.getAlgorithmName().equals(nTRUSigningParameters.hashAlg.getAlgorithmName())) {
            return false;
        }
        if (Double.doubleToLongBits(this.normBound) != Double.doubleToLongBits(nTRUSigningParameters.normBound)) {
            return false;
        }
        if (Double.doubleToLongBits(this.normBoundSq) != Double.doubleToLongBits(nTRUSigningParameters.normBoundSq)) {
            return false;
        }
        if (this.q != nTRUSigningParameters.q) {
            return false;
        }
        return this.signFailTolerance == nTRUSigningParameters.signFailTolerance;
    }

    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        StringBuilder stringBuilder = new StringBuilder("SignatureParameters(N=" + this.N + " q=" + this.q);
        stringBuilder.append(" B=" + this.B + " beta=" + decimalFormat.format(this.beta) + " normBound=" + decimalFormat.format(this.normBound) + " hashAlg=" + this.hashAlg + ")");
        return stringBuilder.toString();
    }
}

