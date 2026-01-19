/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.util.encoders;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.util.encoders.Encoder;

public class HexEncoder
implements Encoder {
    protected final byte[] encodingTable = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
    protected final byte[] decodingTable = new byte[128];

    protected void initialiseDecodingTable() {
        int n2;
        for (n2 = 0; n2 < this.decodingTable.length; ++n2) {
            this.decodingTable[n2] = -1;
        }
        for (n2 = 0; n2 < this.encodingTable.length; ++n2) {
            this.decodingTable[this.encodingTable[n2]] = (byte)n2;
        }
        this.decodingTable[65] = this.decodingTable[97];
        this.decodingTable[66] = this.decodingTable[98];
        this.decodingTable[67] = this.decodingTable[99];
        this.decodingTable[68] = this.decodingTable[100];
        this.decodingTable[69] = this.decodingTable[101];
        this.decodingTable[70] = this.decodingTable[102];
    }

    public HexEncoder() {
        this.initialiseDecodingTable();
    }

    public int encode(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws IOException {
        int n5 = n2;
        int n6 = n2 + n3;
        int n7 = n4;
        while (n5 < n6) {
            int n8 = byArray[n5++] & 0xFF;
            byArray2[n7++] = this.encodingTable[n8 >>> 4];
            byArray2[n7++] = this.encodingTable[n8 & 0xF];
        }
        return n7 - n4;
    }

    @Override
    public int getEncodedLength(int n2) {
        return n2 * 2;
    }

    @Override
    public int getMaxDecodedLength(int n2) {
        return n2 / 2;
    }

    @Override
    public int encode(byte[] byArray, int n2, int n3, OutputStream outputStream2) throws IOException {
        int n4;
        if (n3 < 0) {
            return 0;
        }
        byte[] byArray2 = new byte[72];
        for (int i2 = n3; i2 > 0; i2 -= n4) {
            n4 = Math.min(36, i2);
            int n5 = this.encode(byArray, n2, n4, byArray2, 0);
            outputStream2.write(byArray2, 0, n5);
            n2 += n4;
        }
        return n3 * 2;
    }

    private static boolean ignore(char c2) {
        return c2 == '\n' || c2 == '\r' || c2 == '\t' || c2 == ' ';
    }

    @Override
    public int decode(byte[] byArray, int n2, int n3, OutputStream outputStream2) throws IOException {
        int n4;
        int n5 = 0;
        byte[] byArray2 = new byte[36];
        int n6 = 0;
        for (n4 = n2 + n3; n4 > n2 && HexEncoder.ignore((char)byArray[n4 - 1]); --n4) {
        }
        int n7 = n2;
        while (n7 < n4) {
            byte by;
            while (n7 < n4 && HexEncoder.ignore((char)byArray[n7])) {
                ++n7;
            }
            byte by2 = this.decodingTable[byArray[n7++]];
            while (n7 < n4 && HexEncoder.ignore((char)byArray[n7])) {
                ++n7;
            }
            if ((by2 | (by = this.decodingTable[byArray[n7++]])) < 0) {
                throw new IOException("invalid characters encountered in Hex data");
            }
            byArray2[n6++] = (byte)(by2 << 4 | by);
            if (n6 == byArray2.length) {
                outputStream2.write(byArray2);
                n6 = 0;
            }
            ++n5;
        }
        if (n6 > 0) {
            outputStream2.write(byArray2, 0, n6);
        }
        return n5;
    }

    @Override
    public int decode(String string, OutputStream outputStream2) throws IOException {
        int n2;
        int n3 = 0;
        byte[] byArray = new byte[36];
        int n4 = 0;
        for (n2 = string.length(); n2 > 0 && HexEncoder.ignore(string.charAt(n2 - 1)); --n2) {
        }
        int n5 = 0;
        while (n5 < n2) {
            byte by;
            while (n5 < n2 && HexEncoder.ignore(string.charAt(n5))) {
                ++n5;
            }
            byte by2 = this.decodingTable[string.charAt(n5++)];
            while (n5 < n2 && HexEncoder.ignore(string.charAt(n5))) {
                ++n5;
            }
            if ((by2 | (by = this.decodingTable[string.charAt(n5++)])) < 0) {
                throw new IOException("invalid characters encountered in Hex string");
            }
            byArray[n4++] = (byte)(by2 << 4 | by);
            if (n4 == byArray.length) {
                outputStream2.write(byArray);
                n4 = 0;
            }
            ++n3;
        }
        if (n4 > 0) {
            outputStream2.write(byArray, 0, n4);
        }
        return n3;
    }

    byte[] decodeStrict(String string, int n2, int n3) throws IOException {
        if (null == string) {
            throw new NullPointerException("'str' cannot be null");
        }
        if (n2 < 0 || n3 < 0 || n2 > string.length() - n3) {
            throw new IndexOutOfBoundsException("invalid offset and/or length specified");
        }
        if (0 != (n3 & 1)) {
            throw new IOException("a hexadecimal encoding must have an even number of characters");
        }
        int n4 = n3 >>> 1;
        byte[] byArray = new byte[n4];
        int n5 = n2;
        for (int i2 = 0; i2 < n4; ++i2) {
            byte by;
            byte by2;
            int n6;
            if ((n6 = (by2 = this.decodingTable[string.charAt(n5++)]) << 4 | (by = this.decodingTable[string.charAt(n5++)])) < 0) {
                throw new IOException("invalid characters encountered in Hex string");
            }
            byArray[i2] = (byte)n6;
        }
        return byArray;
    }
}

