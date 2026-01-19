/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.util.Strings;

public abstract class PBEParametersGenerator {
    protected byte[] password;
    protected byte[] salt;
    protected int iterationCount;

    protected PBEParametersGenerator() {
    }

    public void init(byte[] byArray, byte[] byArray2, int n2) {
        this.password = byArray;
        this.salt = byArray2;
        this.iterationCount = n2;
    }

    public byte[] getPassword() {
        return this.password;
    }

    public byte[] getSalt() {
        return this.salt;
    }

    public int getIterationCount() {
        return this.iterationCount;
    }

    public abstract CipherParameters generateDerivedParameters(int var1);

    public abstract CipherParameters generateDerivedParameters(int var1, int var2);

    public abstract CipherParameters generateDerivedMacParameters(int var1);

    public static byte[] PKCS5PasswordToBytes(char[] cArray) {
        if (cArray != null) {
            byte[] byArray = new byte[cArray.length];
            for (int i2 = 0; i2 != byArray.length; ++i2) {
                byArray[i2] = (byte)cArray[i2];
            }
            return byArray;
        }
        return new byte[0];
    }

    public static byte[] PKCS5PasswordToUTF8Bytes(char[] cArray) {
        if (cArray != null) {
            return Strings.toUTF8ByteArray(cArray);
        }
        return new byte[0];
    }

    public static byte[] PKCS12PasswordToBytes(char[] cArray) {
        if (cArray != null && cArray.length > 0) {
            byte[] byArray = new byte[(cArray.length + 1) * 2];
            for (int i2 = 0; i2 != cArray.length; ++i2) {
                byArray[i2 * 2] = (byte)(cArray[i2] >>> 8);
                byArray[i2 * 2 + 1] = (byte)cArray[i2];
            }
            return byArray;
        }
        return new byte[0];
    }
}

