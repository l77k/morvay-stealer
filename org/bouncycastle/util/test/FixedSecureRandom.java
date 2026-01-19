/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.util.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Provider;
import java.security.SecureRandom;
import org.bouncycastle.util.Pack;
import org.bouncycastle.util.encoders.Hex;

public class FixedSecureRandom
extends SecureRandom {
    private static java.math.BigInteger REGULAR = new java.math.BigInteger("01020304ffffffff0506070811111111", 16);
    private static java.math.BigInteger ANDROID = new java.math.BigInteger("1111111105060708ffffffff01020304", 16);
    private static java.math.BigInteger CLASSPATH = new java.math.BigInteger("3020104ffffffff05060708111111", 16);
    private static final boolean isAndroidStyle;
    private static final boolean isClasspathStyle;
    private static final boolean isRegularStyle;
    private byte[] _data;
    private int _index;

    public FixedSecureRandom(byte[] byArray) {
        this(new Source[]{new Data(byArray)});
    }

    public FixedSecureRandom(byte[][] byArray) {
        this(FixedSecureRandom.buildDataArray(byArray));
    }

    private static Data[] buildDataArray(byte[][] byArray) {
        Data[] dataArray = new Data[byArray.length];
        for (int i2 = 0; i2 != byArray.length; ++i2) {
            dataArray[i2] = new Data(byArray[i2]);
        }
        return dataArray;
    }

    public FixedSecureRandom(Source[] sourceArray) {
        super(null, new DummyProvider());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (isRegularStyle) {
            if (isClasspathStyle) {
                for (int i2 = 0; i2 != sourceArray.length; ++i2) {
                    try {
                        if (sourceArray[i2] instanceof BigInteger) {
                            int n2;
                            byte[] byArray = sourceArray[i2].data;
                            int n3 = byArray.length - byArray.length % 4;
                            for (n2 = byArray.length - n3 - 1; n2 >= 0; --n2) {
                                byteArrayOutputStream.write(byArray[n2]);
                            }
                            for (n2 = byArray.length - n3; n2 < byArray.length; n2 += 4) {
                                byteArrayOutputStream.write(byArray, n2, 4);
                            }
                            continue;
                        }
                        byteArrayOutputStream.write(sourceArray[i2].data);
                        continue;
                    }
                    catch (IOException iOException) {
                        throw new IllegalArgumentException("can't save value source.");
                    }
                }
            } else {
                for (int i3 = 0; i3 != sourceArray.length; ++i3) {
                    try {
                        byteArrayOutputStream.write(sourceArray[i3].data);
                        continue;
                    }
                    catch (IOException iOException) {
                        throw new IllegalArgumentException("can't save value source.");
                    }
                }
            }
        } else if (isAndroidStyle) {
            for (int i4 = 0; i4 != sourceArray.length; ++i4) {
                try {
                    if (sourceArray[i4] instanceof BigInteger) {
                        int n4;
                        byte[] byArray = sourceArray[i4].data;
                        int n5 = byArray.length - byArray.length % 4;
                        for (n4 = 0; n4 < n5; n4 += 4) {
                            byteArrayOutputStream.write(byArray, byArray.length - (n4 + 4), 4);
                        }
                        if (byArray.length - n5 != 0) {
                            for (n4 = 0; n4 != 4 - (byArray.length - n5); ++n4) {
                                byteArrayOutputStream.write(0);
                            }
                        }
                        for (n4 = 0; n4 != byArray.length - n5; ++n4) {
                            byteArrayOutputStream.write(byArray[n5 + n4]);
                        }
                        continue;
                    }
                    byteArrayOutputStream.write(sourceArray[i4].data);
                    continue;
                }
                catch (IOException iOException) {
                    throw new IllegalArgumentException("can't save value source.");
                }
            }
        } else {
            throw new IllegalStateException("Unrecognized BigInteger implementation");
        }
        this._data = byteArrayOutputStream.toByteArray();
    }

    @Override
    public void nextBytes(byte[] byArray) {
        System.arraycopy(this._data, this._index, byArray, 0, byArray.length);
        this._index += byArray.length;
    }

    @Override
    public byte[] generateSeed(int n2) {
        byte[] byArray = new byte[n2];
        this.nextBytes(byArray);
        return byArray;
    }

    @Override
    public int nextInt() {
        int n2 = 0;
        n2 |= this.nextValue() << 24;
        n2 |= this.nextValue() << 16;
        n2 |= this.nextValue() << 8;
        return n2 |= this.nextValue();
    }

    @Override
    public long nextLong() {
        long l2 = 0L;
        l2 |= (long)this.nextValue() << 56;
        l2 |= (long)this.nextValue() << 48;
        l2 |= (long)this.nextValue() << 40;
        l2 |= (long)this.nextValue() << 32;
        l2 |= (long)this.nextValue() << 24;
        l2 |= (long)this.nextValue() << 16;
        l2 |= (long)this.nextValue() << 8;
        return l2 |= (long)this.nextValue();
    }

    public boolean isExhausted() {
        return this._index == this._data.length;
    }

    private int nextValue() {
        return this._data[this._index++] & 0xFF;
    }

    private static byte[] expandToBitLength(int n2, byte[] byArray) {
        if ((n2 + 7) / 8 > byArray.length) {
            byte[] byArray2 = new byte[(n2 + 7) / 8];
            System.arraycopy(byArray, 0, byArray2, byArray2.length - byArray.length, byArray.length);
            if (isAndroidStyle && n2 % 8 != 0) {
                int n3 = Pack.bigEndianToInt(byArray2, 0);
                Pack.intToBigEndian(n3 << 8 - n2 % 8, byArray2, 0);
            }
            return byArray2;
        }
        if (isAndroidStyle && n2 < byArray.length * 8 && n2 % 8 != 0) {
            int n4 = Pack.bigEndianToInt(byArray, 0);
            Pack.intToBigEndian(n4 << 8 - n2 % 8, byArray, 0);
        }
        return byArray;
    }

    static {
        java.math.BigInteger bigInteger = new java.math.BigInteger(128, new RandomChecker());
        java.math.BigInteger bigInteger2 = new java.math.BigInteger(120, new RandomChecker());
        isAndroidStyle = bigInteger.equals(ANDROID);
        isRegularStyle = bigInteger.equals(REGULAR);
        isClasspathStyle = bigInteger2.equals(CLASSPATH);
    }

    public static class BigInteger
    extends Source {
        public BigInteger(byte[] byArray) {
            super(byArray);
        }

        public BigInteger(int n2, byte[] byArray) {
            super(FixedSecureRandom.expandToBitLength(n2, byArray));
        }

        public BigInteger(String string) {
            this(Hex.decode(string));
        }

        public BigInteger(int n2, String string) {
            super(FixedSecureRandom.expandToBitLength(n2, Hex.decode(string)));
        }
    }

    public static class Data
    extends Source {
        public Data(byte[] byArray) {
            super(byArray);
        }
    }

    private static class DummyProvider
    extends Provider {
        DummyProvider() {
            super("BCFIPS_FIXED_RNG", 1.0, "BCFIPS Fixed Secure Random Provider");
        }
    }

    private static class RandomChecker
    extends SecureRandom {
        byte[] data = Hex.decode("01020304ffffffff0506070811111111");
        int index = 0;

        RandomChecker() {
            super(null, new DummyProvider());
        }

        @Override
        public void nextBytes(byte[] byArray) {
            System.arraycopy(this.data, this.index, byArray, 0, byArray.length);
            this.index += byArray.length;
        }
    }

    public static class Source {
        byte[] data;

        Source(byte[] byArray) {
            this.data = byArray;
        }
    }
}

