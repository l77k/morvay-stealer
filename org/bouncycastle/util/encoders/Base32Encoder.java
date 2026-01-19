/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.util.encoders;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Encoder;

public class Base32Encoder
implements Encoder {
    private static final byte[] DEAULT_ENCODING_TABLE = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 50, 51, 52, 53, 54, 55};
    private static final byte DEFAULT_PADDING = 61;
    private final byte[] encodingTable;
    private final byte padding;
    private final byte[] decodingTable = new byte[128];

    protected void initialiseDecodingTable() {
        int n2;
        for (n2 = 0; n2 < this.decodingTable.length; ++n2) {
            this.decodingTable[n2] = -1;
        }
        for (n2 = 0; n2 < this.encodingTable.length; ++n2) {
            this.decodingTable[this.encodingTable[n2]] = (byte)n2;
        }
    }

    public Base32Encoder() {
        this.encodingTable = DEAULT_ENCODING_TABLE;
        this.padding = (byte)61;
        this.initialiseDecodingTable();
    }

    public Base32Encoder(byte[] byArray, byte by) {
        if (byArray.length != 32) {
            throw new IllegalArgumentException("encoding table needs to be length 32");
        }
        this.encodingTable = Arrays.clone(byArray);
        this.padding = by;
        this.initialiseDecodingTable();
    }

    public int encode(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws IOException {
        int n5 = n2;
        int n6 = n2 + n3 - 4;
        int n7 = n4;
        while (n5 < n6) {
            this.encodeBlock(byArray, n5, byArray2, n7);
            n5 += 5;
            n7 += 8;
        }
        int n8 = n3 - (n5 - n2);
        if (n8 > 0) {
            byte[] byArray3 = new byte[5];
            System.arraycopy(byArray, n5, byArray3, 0, n8);
            this.encodeBlock(byArray3, 0, byArray2, n7);
            switch (n8) {
                case 1: {
                    byArray2[n7 + 2] = this.padding;
                    byArray2[n7 + 3] = this.padding;
                    byArray2[n7 + 4] = this.padding;
                    byArray2[n7 + 5] = this.padding;
                    byArray2[n7 + 6] = this.padding;
                    byArray2[n7 + 7] = this.padding;
                    break;
                }
                case 2: {
                    byArray2[n7 + 4] = this.padding;
                    byArray2[n7 + 5] = this.padding;
                    byArray2[n7 + 6] = this.padding;
                    byArray2[n7 + 7] = this.padding;
                    break;
                }
                case 3: {
                    byArray2[n7 + 5] = this.padding;
                    byArray2[n7 + 6] = this.padding;
                    byArray2[n7 + 7] = this.padding;
                    break;
                }
                case 4: {
                    byArray2[n7 + 7] = this.padding;
                }
            }
            n7 += 8;
        }
        return n7 - n4;
    }

    private void encodeBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        byte by = byArray[n2++];
        int n4 = byArray[n2++] & 0xFF;
        int n5 = byArray[n2++] & 0xFF;
        int n6 = byArray[n2++] & 0xFF;
        int n7 = byArray[n2] & 0xFF;
        byArray2[n3++] = this.encodingTable[by >>> 3 & 0x1F];
        byArray2[n3++] = this.encodingTable[(by << 2 | n4 >>> 6) & 0x1F];
        byArray2[n3++] = this.encodingTable[n4 >>> 1 & 0x1F];
        byArray2[n3++] = this.encodingTable[(n4 << 4 | n5 >>> 4) & 0x1F];
        byArray2[n3++] = this.encodingTable[(n5 << 1 | n6 >>> 7) & 0x1F];
        byArray2[n3++] = this.encodingTable[n6 >>> 2 & 0x1F];
        byArray2[n3++] = this.encodingTable[(n6 << 3 | n7 >>> 5) & 0x1F];
        byArray2[n3] = this.encodingTable[n7 & 0x1F];
    }

    @Override
    public int getEncodedLength(int n2) {
        return (n2 + 4) / 5 * 8;
    }

    @Override
    public int getMaxDecodedLength(int n2) {
        return n2 / 8 * 5;
    }

    @Override
    public int encode(byte[] byArray, int n2, int n3, OutputStream outputStream2) throws IOException {
        int n4;
        if (n3 < 0) {
            return 0;
        }
        byte[] byArray2 = new byte[72];
        for (int i2 = n3; i2 > 0; i2 -= n4) {
            n4 = Math.min(45, i2);
            int n5 = this.encode(byArray, n2, n4, byArray2, 0);
            outputStream2.write(byArray2, 0, n5);
            n2 += n4;
        }
        return (n3 + 2) / 3 * 4;
    }

    private boolean ignore(char c2) {
        return c2 == '\n' || c2 == '\r' || c2 == '\t' || c2 == ' ';
    }

    @Override
    public int decode(byte[] byArray, int n2, int n3, OutputStream outputStream2) throws IOException {
        int n4;
        int n5;
        byte[] byArray2 = new byte[55];
        int n6 = 0;
        int n7 = 0;
        for (n5 = n2 + n3; n5 > n2 && this.ignore((char)byArray[n5 - 1]); --n5) {
        }
        if (n5 == 0) {
            return 0;
        }
        int n8 = 0;
        for (n4 = n5; n4 > n2 && n8 != 8; --n4) {
            if (this.ignore((char)byArray[n4 - 1])) continue;
            ++n8;
        }
        n8 = this.nextI(byArray, n2, n4);
        while (n8 < n4) {
            byte by;
            byte by2 = this.decodingTable[byArray[n8++]];
            n8 = this.nextI(byArray, n8, n4);
            byte by3 = this.decodingTable[byArray[n8++]];
            n8 = this.nextI(byArray, n8, n4);
            byte by4 = this.decodingTable[byArray[n8++]];
            n8 = this.nextI(byArray, n8, n4);
            byte by5 = this.decodingTable[byArray[n8++]];
            n8 = this.nextI(byArray, n8, n4);
            byte by6 = this.decodingTable[byArray[n8++]];
            n8 = this.nextI(byArray, n8, n4);
            byte by7 = this.decodingTable[byArray[n8++]];
            n8 = this.nextI(byArray, n8, n4);
            byte by8 = this.decodingTable[byArray[n8++]];
            n8 = this.nextI(byArray, n8, n4);
            if ((by2 | by3 | by4 | by5 | by6 | by7 | by8 | (by = this.decodingTable[byArray[n8++]])) < 0) {
                throw new IOException("invalid characters encountered in base32 data");
            }
            byArray2[n6++] = (byte)(by2 << 3 | by3 >> 2);
            byArray2[n6++] = (byte)(by3 << 6 | by4 << 1 | by5 >> 4);
            byArray2[n6++] = (byte)(by5 << 4 | by6 >> 1);
            byArray2[n6++] = (byte)(by6 << 7 | by7 << 2 | by8 >> 3);
            byArray2[n6++] = (byte)(by8 << 5 | by);
            if (n6 == byArray2.length) {
                outputStream2.write(byArray2);
                n6 = 0;
            }
            n7 += 5;
            n8 = this.nextI(byArray, n8, n4);
        }
        if (n6 > 0) {
            outputStream2.write(byArray2, 0, n6);
        }
        int n9 = this.nextI(byArray, n8, n5);
        int n10 = this.nextI(byArray, n9 + 1, n5);
        int n11 = this.nextI(byArray, n10 + 1, n5);
        int n12 = this.nextI(byArray, n11 + 1, n5);
        int n13 = this.nextI(byArray, n12 + 1, n5);
        int n14 = this.nextI(byArray, n13 + 1, n5);
        int n15 = this.nextI(byArray, n14 + 1, n5);
        int n16 = this.nextI(byArray, n15 + 1, n5);
        return n7 += this.decodeLastBlock(outputStream2, (char)byArray[n9], (char)byArray[n10], (char)byArray[n11], (char)byArray[n12], (char)byArray[n13], (char)byArray[n14], (char)byArray[n15], (char)byArray[n16]);
    }

    private int nextI(byte[] byArray, int n2, int n3) {
        while (n2 < n3 && this.ignore((char)byArray[n2])) {
            ++n2;
        }
        return n2;
    }

    @Override
    public int decode(String string, OutputStream outputStream2) throws IOException {
        byte[] byArray = Strings.toByteArray(string);
        return this.decode(byArray, 0, byArray.length, outputStream2);
    }

    private int decodeLastBlock(OutputStream outputStream2, char c2, char c3, char c4, char c5, char c6, char c7, char c8, char c9) throws IOException {
        if (c9 == this.padding) {
            if (c8 != this.padding) {
                byte by = this.decodingTable[c2];
                byte by2 = this.decodingTable[c3];
                byte by3 = this.decodingTable[c4];
                byte by4 = this.decodingTable[c5];
                byte by5 = this.decodingTable[c6];
                byte by6 = this.decodingTable[c7];
                byte by7 = this.decodingTable[c8];
                if ((by | by2 | by3 | by4 | by5 | by6 | by7) < 0) {
                    throw new IOException("invalid characters encountered at end of base32 data");
                }
                outputStream2.write(by << 3 | by2 >> 2);
                outputStream2.write(by2 << 6 | by3 << 1 | by4 >> 4);
                outputStream2.write(by4 << 4 | by5 >> 1);
                outputStream2.write(by5 << 7 | by6 << 2 | by7 >> 3);
                return 4;
            }
            if (c7 != this.padding) {
                throw new IOException("invalid characters encountered at end of base32 data");
            }
            if (c6 != this.padding) {
                byte by = this.decodingTable[c2];
                byte by8 = this.decodingTable[c3];
                byte by9 = this.decodingTable[c4];
                byte by10 = this.decodingTable[c5];
                byte by11 = this.decodingTable[c6];
                if ((by | by8 | by9 | by10 | by11) < 0) {
                    throw new IOException("invalid characters encountered at end of base32 data");
                }
                outputStream2.write(by << 3 | by8 >> 2);
                outputStream2.write(by8 << 6 | by9 << 1 | by10 >> 4);
                outputStream2.write(by10 << 4 | by11 >> 1);
                return 3;
            }
            if (c5 != this.padding) {
                byte by = this.decodingTable[c2];
                byte by12 = this.decodingTable[c3];
                byte by13 = this.decodingTable[c4];
                byte by14 = this.decodingTable[c5];
                if ((by | by12 | by13 | by14) < 0) {
                    throw new IOException("invalid characters encountered at end of base32 data");
                }
                outputStream2.write(by << 3 | by12 >> 2);
                outputStream2.write(by12 << 6 | by13 << 1 | by14 >> 4);
                return 2;
            }
            if (c4 != this.padding) {
                throw new IOException("invalid characters encountered at end of base32 data");
            }
            byte by = this.decodingTable[c2];
            byte by15 = this.decodingTable[c3];
            if ((by | by15) < 0) {
                throw new IOException("invalid characters encountered at end of base32 data");
            }
            outputStream2.write(by << 3 | by15 >> 2);
            return 1;
        }
        byte by = this.decodingTable[c2];
        byte by16 = this.decodingTable[c3];
        byte by17 = this.decodingTable[c4];
        byte by18 = this.decodingTable[c5];
        byte by19 = this.decodingTable[c6];
        byte by20 = this.decodingTable[c7];
        byte by21 = this.decodingTable[c8];
        byte by22 = this.decodingTable[c9];
        if ((by | by16 | by17 | by18 | by19 | by20 | by21 | by22) < 0) {
            throw new IOException("invalid characters encountered at end of base32 data");
        }
        outputStream2.write(by << 3 | by16 >> 2);
        outputStream2.write(by16 << 6 | by17 << 1 | by18 >> 4);
        outputStream2.write(by18 << 4 | by19 >> 1);
        outputStream2.write(by19 << 7 | by20 << 2 | by21 >> 3);
        outputStream2.write(by21 << 5 | by22);
        return 5;
    }
}

