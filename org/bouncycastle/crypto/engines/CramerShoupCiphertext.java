/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import java.math.BigInteger;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class CramerShoupCiphertext {
    BigInteger u1;
    BigInteger u2;
    BigInteger e;
    BigInteger v;

    public CramerShoupCiphertext() {
    }

    public CramerShoupCiphertext(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
        this.u1 = bigInteger;
        this.u2 = bigInteger2;
        this.e = bigInteger3;
        this.v = bigInteger4;
    }

    public CramerShoupCiphertext(byte[] byArray) {
        int n2 = 0;
        int n3 = Pack.bigEndianToInt(byArray, n2);
        byte[] byArray2 = Arrays.copyOfRange(byArray, n2 += 4, n2 + n3);
        n2 += n3;
        this.u1 = new BigInteger(byArray2);
        n3 = Pack.bigEndianToInt(byArray, n2);
        byArray2 = Arrays.copyOfRange(byArray, n2 += 4, n2 + n3);
        n2 += n3;
        this.u2 = new BigInteger(byArray2);
        n3 = Pack.bigEndianToInt(byArray, n2);
        byArray2 = Arrays.copyOfRange(byArray, n2 += 4, n2 + n3);
        n2 += n3;
        this.e = new BigInteger(byArray2);
        n3 = Pack.bigEndianToInt(byArray, n2);
        byArray2 = Arrays.copyOfRange(byArray, n2 += 4, n2 + n3);
        n2 += n3;
        this.v = new BigInteger(byArray2);
    }

    public BigInteger getU1() {
        return this.u1;
    }

    public void setU1(BigInteger bigInteger) {
        this.u1 = bigInteger;
    }

    public BigInteger getU2() {
        return this.u2;
    }

    public void setU2(BigInteger bigInteger) {
        this.u2 = bigInteger;
    }

    public BigInteger getE() {
        return this.e;
    }

    public void setE(BigInteger bigInteger) {
        this.e = bigInteger;
    }

    public BigInteger getV() {
        return this.v;
    }

    public void setV(BigInteger bigInteger) {
        this.v = bigInteger;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("u1: " + this.u1.toString());
        stringBuffer.append("\nu2: " + this.u2.toString());
        stringBuffer.append("\ne: " + this.e.toString());
        stringBuffer.append("\nv: " + this.v.toString());
        return stringBuffer.toString();
    }

    public byte[] toByteArray() {
        byte[] byArray = this.u1.toByteArray();
        int n2 = byArray.length;
        byte[] byArray2 = this.u2.toByteArray();
        int n3 = byArray2.length;
        byte[] byArray3 = this.e.toByteArray();
        int n4 = byArray3.length;
        byte[] byArray4 = this.v.toByteArray();
        int n5 = byArray4.length;
        int n6 = 0;
        byte[] byArray5 = new byte[n2 + n3 + n4 + n5 + 16];
        Pack.intToBigEndian(n2, byArray5, n6);
        System.arraycopy(byArray, 0, byArray5, n6 += 4, n2);
        Pack.intToBigEndian(n3, byArray5, n6 += n2);
        System.arraycopy(byArray2, 0, byArray5, n6 += 4, n3);
        Pack.intToBigEndian(n4, byArray5, n6 += n3);
        System.arraycopy(byArray3, 0, byArray5, n6 += 4, n4);
        Pack.intToBigEndian(n5, byArray5, n6 += n4);
        System.arraycopy(byArray4, 0, byArray5, n6 += 4, n5);
        n6 += n5;
        return byArray5;
    }
}

