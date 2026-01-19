/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.xmss;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.util.HashSet;
import java.util.Set;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

public class XMSSUtil {
    public static int log2(int n2) {
        int n3 = 0;
        while ((n2 >>= 1) != 0) {
            ++n3;
        }
        return n3;
    }

    public static byte[] toBytesBigEndian(long l2, int n2) {
        byte[] byArray = new byte[n2];
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            byArray[i2] = (byte)l2;
            l2 >>>= 8;
        }
        return byArray;
    }

    public static void longToBigEndian(long l2, byte[] byArray, int n2) {
        if (byArray == null) {
            throw new NullPointerException("in == null");
        }
        if (byArray.length - n2 < 8) {
            throw new IllegalArgumentException("not enough space in array");
        }
        byArray[n2] = (byte)(l2 >> 56 & 0xFFL);
        byArray[n2 + 1] = (byte)(l2 >> 48 & 0xFFL);
        byArray[n2 + 2] = (byte)(l2 >> 40 & 0xFFL);
        byArray[n2 + 3] = (byte)(l2 >> 32 & 0xFFL);
        byArray[n2 + 4] = (byte)(l2 >> 24 & 0xFFL);
        byArray[n2 + 5] = (byte)(l2 >> 16 & 0xFFL);
        byArray[n2 + 6] = (byte)(l2 >> 8 & 0xFFL);
        byArray[n2 + 7] = (byte)(l2 & 0xFFL);
    }

    public static long bytesToXBigEndian(byte[] byArray, int n2, int n3) {
        if (byArray == null) {
            throw new NullPointerException("in == null");
        }
        long l2 = 0L;
        for (int i2 = n2; i2 < n2 + n3; ++i2) {
            l2 = l2 << 8 | (long)(byArray[i2] & 0xFF);
        }
        return l2;
    }

    public static byte[] cloneArray(byte[] byArray) {
        if (byArray == null) {
            throw new NullPointerException("in == null");
        }
        byte[] byArray2 = new byte[byArray.length];
        System.arraycopy(byArray, 0, byArray2, 0, byArray.length);
        return byArray2;
    }

    public static byte[][] cloneArray(byte[][] byArray) {
        if (XMSSUtil.hasNullPointer(byArray)) {
            throw new NullPointerException("in has null pointers");
        }
        byte[][] byArrayArray = new byte[byArray.length][];
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            byArrayArray[i2] = new byte[byArray[i2].length];
            System.arraycopy(byArray[i2], 0, byArrayArray[i2], 0, byArray[i2].length);
        }
        return byArrayArray;
    }

    public static boolean areEqual(byte[][] byArray, byte[][] byArray2) {
        if (XMSSUtil.hasNullPointer(byArray) || XMSSUtil.hasNullPointer(byArray2)) {
            throw new NullPointerException("a or b == null");
        }
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            if (Arrays.areEqual(byArray[i2], byArray2[i2])) continue;
            return false;
        }
        return true;
    }

    public static void dumpByteArray(byte[][] byArray) {
        if (XMSSUtil.hasNullPointer(byArray)) {
            throw new NullPointerException("x has null pointers");
        }
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            System.out.println(Hex.toHexString(byArray[i2]));
        }
    }

    public static boolean hasNullPointer(byte[][] byArray) {
        if (byArray == null) {
            return true;
        }
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            if (byArray[i2] != null) continue;
            return true;
        }
        return false;
    }

    public static void copyBytesAtOffset(byte[] byArray, byte[] byArray2, int n2) {
        if (byArray == null) {
            throw new NullPointerException("dst == null");
        }
        if (byArray2 == null) {
            throw new NullPointerException("src == null");
        }
        if (n2 < 0) {
            throw new IllegalArgumentException("offset hast to be >= 0");
        }
        if (byArray2.length + n2 > byArray.length) {
            throw new IllegalArgumentException("src length + offset must not be greater than size of destination");
        }
        for (int i2 = 0; i2 < byArray2.length; ++i2) {
            byArray[n2 + i2] = byArray2[i2];
        }
    }

    public static byte[] extractBytesAtOffset(byte[] byArray, int n2, int n3) {
        if (byArray == null) {
            throw new NullPointerException("src == null");
        }
        if (n2 < 0) {
            throw new IllegalArgumentException("offset hast to be >= 0");
        }
        if (n3 < 0) {
            throw new IllegalArgumentException("length hast to be >= 0");
        }
        if (n2 + n3 > byArray.length) {
            throw new IllegalArgumentException("offset + length must not be greater then size of source array");
        }
        byte[] byArray2 = new byte[n3];
        for (int i2 = 0; i2 < byArray2.length; ++i2) {
            byArray2[i2] = byArray[n2 + i2];
        }
        return byArray2;
    }

    public static boolean isIndexValid(int n2, long l2) {
        if (l2 < 0L) {
            throw new IllegalStateException("index must not be negative");
        }
        return l2 < 1L << n2;
    }

    public static int getDigestSize(Digest digest) {
        if (digest == null) {
            throw new NullPointerException("digest == null");
        }
        String string = digest.getAlgorithmName();
        if (string.equals("SHAKE128")) {
            return 32;
        }
        if (string.equals("SHAKE256")) {
            return 64;
        }
        return digest.getDigestSize();
    }

    public static long getTreeIndex(long l2, int n2) {
        return l2 >> n2;
    }

    public static int getLeafIndex(long l2, int n2) {
        return (int)(l2 & (1L << n2) - 1L);
    }

    public static byte[] serialize(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    public static Object deserialize(byte[] byArray, Class clazz) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byArray);
        CheckingStream checkingStream = new CheckingStream(clazz, byteArrayInputStream);
        Object object = checkingStream.readObject();
        if (checkingStream.available() != 0) {
            throw new IOException("unexpected data found at end of ObjectInputStream");
        }
        if (clazz.isInstance(object)) {
            return object;
        }
        throw new IOException("unexpected class found in ObjectInputStream");
    }

    public static int calculateTau(int n2, int n3) {
        int n4 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            if ((n2 >> i2 & 1) != 0) continue;
            n4 = i2;
            break;
        }
        return n4;
    }

    public static boolean isNewBDSInitNeeded(long l2, int n2, int n3) {
        if (l2 == 0L) {
            return false;
        }
        return l2 % (long)Math.pow(1 << n2, n3 + 1) == 0L;
    }

    public static boolean isNewAuthenticationPathNeeded(long l2, int n2, int n3) {
        if (l2 == 0L) {
            return false;
        }
        return (l2 + 1L) % (long)Math.pow(1 << n2, n3) == 0L;
    }

    private static class CheckingStream
    extends ObjectInputStream {
        private static final Set components = new HashSet();
        private final Class mainClass;
        private boolean found = false;

        CheckingStream(Class clazz, InputStream inputStream2) throws IOException {
            super(inputStream2);
            this.mainClass = clazz;
        }

        @Override
        protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
            if (!this.found) {
                if (!objectStreamClass.getName().equals(this.mainClass.getName())) {
                    throw new InvalidClassException("unexpected class: ", objectStreamClass.getName());
                }
                this.found = true;
            } else if (!components.contains(objectStreamClass.getName())) {
                throw new InvalidClassException("unexpected class: ", objectStreamClass.getName());
            }
            return super.resolveClass(objectStreamClass);
        }

        static {
            components.add("java.util.TreeMap");
            components.add("java.lang.Integer");
            components.add("java.lang.Number");
            components.add("org.bouncycastle.pqc.crypto.xmss.BDS");
            components.add("java.util.ArrayList");
            components.add("org.bouncycastle.pqc.crypto.xmss.XMSSNode");
            components.add("[B");
            components.add("java.util.LinkedList");
            components.add("java.util.Stack");
            components.add("java.util.Vector");
            components.add("[Ljava.lang.Object;");
            components.add("org.bouncycastle.pqc.crypto.xmss.BDSTreeHash");
        }
    }
}

