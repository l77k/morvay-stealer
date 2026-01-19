/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.linearalgebra;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2nElement;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2nONBField;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GFElement;
import org.bouncycastle.util.Arrays;

public class GF2nONBElement
extends GF2nElement {
    private static final long[] mBitmask = new long[]{1L, 2L, 4L, 8L, 16L, 32L, 64L, 128L, 256L, 512L, 1024L, 2048L, 4096L, 8192L, 16384L, 32768L, 65536L, 131072L, 262144L, 524288L, 0x100000L, 0x200000L, 0x400000L, 0x800000L, 0x1000000L, 0x2000000L, 0x4000000L, 0x8000000L, 0x10000000L, 0x20000000L, 0x40000000L, 0x80000000L, 0x100000000L, 0x200000000L, 0x400000000L, 0x800000000L, 0x1000000000L, 0x2000000000L, 0x4000000000L, 0x8000000000L, 0x10000000000L, 0x20000000000L, 0x40000000000L, 0x80000000000L, 0x100000000000L, 0x200000000000L, 0x400000000000L, 0x800000000000L, 0x1000000000000L, 0x2000000000000L, 0x4000000000000L, 0x8000000000000L, 0x10000000000000L, 0x20000000000000L, 0x40000000000000L, 0x80000000000000L, 0x100000000000000L, 0x200000000000000L, 0x400000000000000L, 0x800000000000000L, 0x1000000000000000L, 0x2000000000000000L, 0x4000000000000000L, Long.MIN_VALUE};
    private static final long[] mMaxmask = new long[]{1L, 3L, 7L, 15L, 31L, 63L, 127L, 255L, 511L, 1023L, 2047L, 4095L, 8191L, 16383L, 32767L, 65535L, 131071L, 262143L, 524287L, 1048575L, 0x1FFFFFL, 0x3FFFFFL, 0x7FFFFFL, 0xFFFFFFL, 0x1FFFFFFL, 0x3FFFFFFL, 0x7FFFFFFL, 0xFFFFFFFL, 0x1FFFFFFFL, 0x3FFFFFFFL, Integer.MAX_VALUE, 0xFFFFFFFFL, 0x1FFFFFFFFL, 0x3FFFFFFFFL, 0x7FFFFFFFFL, 0xFFFFFFFFFL, 0x1FFFFFFFFFL, 0x3FFFFFFFFFL, 0x7FFFFFFFFFL, 0xFFFFFFFFFFL, 0x1FFFFFFFFFFL, 0x3FFFFFFFFFFL, 0x7FFFFFFFFFFL, 0xFFFFFFFFFFFL, 0x1FFFFFFFFFFFL, 0x3FFFFFFFFFFFL, 0x7FFFFFFFFFFFL, 0xFFFFFFFFFFFFL, 0x1FFFFFFFFFFFFL, 0x3FFFFFFFFFFFFL, 0x7FFFFFFFFFFFFL, 0xFFFFFFFFFFFFFL, 0x1FFFFFFFFFFFFFL, 0x3FFFFFFFFFFFFFL, 0x7FFFFFFFFFFFFFL, 0xFFFFFFFFFFFFFFL, 0x1FFFFFFFFFFFFFFL, 0x3FFFFFFFFFFFFFFL, 0x7FFFFFFFFFFFFFFL, 0xFFFFFFFFFFFFFFFL, 0x1FFFFFFFFFFFFFFFL, 0x3FFFFFFFFFFFFFFFL, Long.MAX_VALUE, -1L};
    private static final int[] mIBY64 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
    private static final int MAXLONG = 64;
    private int mLength;
    private int mBit;
    private long[] mPol;

    public GF2nONBElement(GF2nONBField gF2nONBField, SecureRandom secureRandom) {
        this.mField = gF2nONBField;
        this.mDegree = this.mField.getDegree();
        this.mLength = gF2nONBField.getONBLength();
        this.mBit = gF2nONBField.getONBBit();
        this.mPol = new long[this.mLength];
        if (this.mLength > 1) {
            for (int i2 = 0; i2 < this.mLength - 1; ++i2) {
                this.mPol[i2] = secureRandom.nextLong();
            }
            long l2 = secureRandom.nextLong();
            this.mPol[this.mLength - 1] = l2 >>> 64 - this.mBit;
        } else {
            this.mPol[0] = secureRandom.nextLong();
            this.mPol[0] = this.mPol[0] >>> 64 - this.mBit;
        }
    }

    public GF2nONBElement(GF2nONBField gF2nONBField, byte[] byArray) {
        this.mField = gF2nONBField;
        this.mDegree = this.mField.getDegree();
        this.mLength = gF2nONBField.getONBLength();
        this.mBit = gF2nONBField.getONBBit();
        this.mPol = new long[this.mLength];
        this.assign(byArray);
    }

    public GF2nONBElement(GF2nONBField gF2nONBField, BigInteger bigInteger) {
        this.mField = gF2nONBField;
        this.mDegree = this.mField.getDegree();
        this.mLength = gF2nONBField.getONBLength();
        this.mBit = gF2nONBField.getONBBit();
        this.mPol = new long[this.mLength];
        this.assign(bigInteger);
    }

    private GF2nONBElement(GF2nONBField gF2nONBField, long[] lArray) {
        this.mField = gF2nONBField;
        this.mDegree = this.mField.getDegree();
        this.mLength = gF2nONBField.getONBLength();
        this.mBit = gF2nONBField.getONBBit();
        this.mPol = lArray;
    }

    public GF2nONBElement(GF2nONBElement gF2nONBElement) {
        this.mField = gF2nONBElement.mField;
        this.mDegree = this.mField.getDegree();
        this.mLength = ((GF2nONBField)this.mField).getONBLength();
        this.mBit = ((GF2nONBField)this.mField).getONBBit();
        this.mPol = new long[this.mLength];
        this.assign(gF2nONBElement.getElement());
    }

    @Override
    public Object clone() {
        return new GF2nONBElement(this);
    }

    public static GF2nONBElement ZERO(GF2nONBField gF2nONBField) {
        long[] lArray = new long[gF2nONBField.getONBLength()];
        return new GF2nONBElement(gF2nONBField, lArray);
    }

    public static GF2nONBElement ONE(GF2nONBField gF2nONBField) {
        int n2 = gF2nONBField.getONBLength();
        long[] lArray = new long[n2];
        for (int i2 = 0; i2 < n2 - 1; ++i2) {
            lArray[i2] = -1L;
        }
        lArray[n2 - 1] = mMaxmask[gF2nONBField.getONBBit() - 1];
        return new GF2nONBElement(gF2nONBField, lArray);
    }

    @Override
    void assignZero() {
        this.mPol = new long[this.mLength];
    }

    @Override
    void assignOne() {
        for (int i2 = 0; i2 < this.mLength - 1; ++i2) {
            this.mPol[i2] = -1L;
        }
        this.mPol[this.mLength - 1] = mMaxmask[this.mBit - 1];
    }

    private void assign(BigInteger bigInteger) {
        this.assign(bigInteger.toByteArray());
    }

    private void assign(long[] lArray) {
        System.arraycopy(lArray, 0, this.mPol, 0, this.mLength);
    }

    private void assign(byte[] byArray) {
        this.mPol = new long[this.mLength];
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            int n2 = i2 >>> 3;
            this.mPol[n2] = this.mPol[n2] | ((long)byArray[byArray.length - 1 - i2] & 0xFFL) << ((i2 & 7) << 3);
        }
    }

    @Override
    public boolean isZero() {
        boolean bl = true;
        for (int i2 = 0; i2 < this.mLength && bl; ++i2) {
            bl = bl && (this.mPol[i2] & 0xFFFFFFFFFFFFFFFFL) == 0L;
        }
        return bl;
    }

    @Override
    public boolean isOne() {
        boolean bl = true;
        for (int i2 = 0; i2 < this.mLength - 1 && bl; ++i2) {
            bl = bl && (this.mPol[i2] & 0xFFFFFFFFFFFFFFFFL) == -1L;
        }
        if (bl) {
            bl = bl && (this.mPol[this.mLength - 1] & mMaxmask[this.mBit - 1]) == mMaxmask[this.mBit - 1];
        }
        return bl;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof GF2nONBElement)) {
            return false;
        }
        GF2nONBElement gF2nONBElement = (GF2nONBElement)object;
        for (int i2 = 0; i2 < this.mLength; ++i2) {
            if (this.mPol[i2] == gF2nONBElement.mPol[i2]) continue;
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.mPol);
    }

    @Override
    public boolean testRightmostBit() {
        return (this.mPol[this.mLength - 1] & mBitmask[this.mBit - 1]) != 0L;
    }

    @Override
    boolean testBit(int n2) {
        if (n2 < 0 || n2 > this.mDegree) {
            return false;
        }
        long l2 = this.mPol[n2 >>> 6] & mBitmask[n2 & 0x3F];
        return l2 != 0L;
    }

    private long[] getElement() {
        long[] lArray = new long[this.mPol.length];
        System.arraycopy(this.mPol, 0, lArray, 0, this.mPol.length);
        return lArray;
    }

    private long[] getElementReverseOrder() {
        long[] lArray = new long[this.mPol.length];
        for (int i2 = 0; i2 < this.mDegree; ++i2) {
            if (!this.testBit(this.mDegree - i2 - 1)) continue;
            int n2 = i2 >>> 6;
            lArray[n2] = lArray[n2] | mBitmask[i2 & 0x3F];
        }
        return lArray;
    }

    void reverseOrder() {
        this.mPol = this.getElementReverseOrder();
    }

    @Override
    public GFElement add(GFElement gFElement) throws RuntimeException {
        GF2nONBElement gF2nONBElement = new GF2nONBElement(this);
        gF2nONBElement.addToThis(gFElement);
        return gF2nONBElement;
    }

    @Override
    public void addToThis(GFElement gFElement) throws RuntimeException {
        if (!(gFElement instanceof GF2nONBElement)) {
            throw new RuntimeException();
        }
        if (!this.mField.equals(((GF2nONBElement)gFElement).mField)) {
            throw new RuntimeException();
        }
        for (int i2 = 0; i2 < this.mLength; ++i2) {
            int n2 = i2;
            this.mPol[n2] = this.mPol[n2] ^ ((GF2nONBElement)gFElement).mPol[i2];
        }
    }

    @Override
    public GF2nElement increase() {
        GF2nONBElement gF2nONBElement = new GF2nONBElement(this);
        gF2nONBElement.increaseThis();
        return gF2nONBElement;
    }

    @Override
    public void increaseThis() {
        this.addToThis(GF2nONBElement.ONE((GF2nONBField)this.mField));
    }

    @Override
    public GFElement multiply(GFElement gFElement) throws RuntimeException {
        GF2nONBElement gF2nONBElement = new GF2nONBElement(this);
        gF2nONBElement.multiplyThisBy(gFElement);
        return gF2nONBElement;
    }

    @Override
    public void multiplyThisBy(GFElement gFElement) throws RuntimeException {
        if (!(gFElement instanceof GF2nONBElement)) {
            throw new RuntimeException("The elements have different representation: not yet implemented");
        }
        if (!this.mField.equals(((GF2nONBElement)gFElement).mField)) {
            throw new RuntimeException();
        }
        if (this.equals(gFElement)) {
            this.squareThis();
        } else {
            long[] lArray = this.mPol;
            long[] lArray2 = ((GF2nONBElement)gFElement).mPol;
            long[] lArray3 = new long[this.mLength];
            int[][] nArray = ((GF2nONBField)this.mField).mMult;
            int n2 = this.mLength - 1;
            int n3 = this.mBit - 1;
            boolean bl = false;
            long l2 = mBitmask[63];
            long l3 = mBitmask[n3];
            for (int i2 = 0; i2 < this.mDegree; ++i2) {
                boolean bl2;
                int n4;
                int n5;
                int n6;
                bl = false;
                for (n6 = 0; n6 < this.mDegree; ++n6) {
                    n5 = mIBY64[n6];
                    n4 = n6 & 0x3F;
                    int n7 = mIBY64[nArray[n6][0]];
                    int n8 = nArray[n6][0] & 0x3F;
                    if ((lArray[n5] & mBitmask[n4]) == 0L) continue;
                    if ((lArray2[n7] & mBitmask[n8]) != 0L) {
                        bl ^= true;
                    }
                    if (nArray[n6][1] == -1 || (lArray2[n7 = mIBY64[nArray[n6][1]]] & mBitmask[n8 = nArray[n6][1] & 0x3F]) == 0L) continue;
                    bl ^= true;
                }
                n5 = mIBY64[i2];
                n4 = i2 & 0x3F;
                if (bl) {
                    int n9 = n5;
                    lArray3[n9] = lArray3[n9] ^ mBitmask[n4];
                }
                if (this.mLength > 1) {
                    boolean bl3;
                    bl2 = (lArray[n2] & 1L) == 1L;
                    for (n6 = n2 - 1; n6 >= 0; --n6) {
                        bl3 = (lArray[n6] & 1L) != 0L;
                        lArray[n6] = lArray[n6] >>> 1;
                        if (bl2) {
                            int n10 = n6;
                            lArray[n10] = lArray[n10] ^ l2;
                        }
                        bl2 = bl3;
                    }
                    lArray[n2] = lArray[n2] >>> 1;
                    if (bl2) {
                        int n11 = n2;
                        lArray[n11] = lArray[n11] ^ l3;
                    }
                    bl2 = (lArray2[n2] & 1L) == 1L;
                    for (n6 = n2 - 1; n6 >= 0; --n6) {
                        bl3 = (lArray2[n6] & 1L) != 0L;
                        lArray2[n6] = lArray2[n6] >>> 1;
                        if (bl2) {
                            int n12 = n6;
                            lArray2[n12] = lArray2[n12] ^ l2;
                        }
                        bl2 = bl3;
                    }
                    lArray2[n2] = lArray2[n2] >>> 1;
                    if (!bl2) continue;
                    int n13 = n2;
                    lArray2[n13] = lArray2[n13] ^ l3;
                    continue;
                }
                bl2 = (lArray[0] & 1L) == 1L;
                lArray[0] = lArray[0] >>> 1;
                if (bl2) {
                    lArray[0] = lArray[0] ^ l3;
                }
                bl2 = (lArray2[0] & 1L) == 1L;
                lArray2[0] = lArray2[0] >>> 1;
                if (!bl2) continue;
                lArray2[0] = lArray2[0] ^ l3;
            }
            this.assign(lArray3);
        }
    }

    @Override
    public GF2nElement square() {
        GF2nONBElement gF2nONBElement = new GF2nONBElement(this);
        gF2nONBElement.squareThis();
        return gF2nONBElement;
    }

    @Override
    public void squareThis() {
        boolean bl;
        long[] lArray = this.getElement();
        int n2 = this.mLength - 1;
        int n3 = this.mBit - 1;
        long l2 = mBitmask[63];
        boolean bl2 = (lArray[n2] & mBitmask[n3]) != 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            bl = (lArray[i2] & l2) != 0L;
            lArray[i2] = lArray[i2] << 1;
            if (bl2) {
                int n4 = i2;
                lArray[n4] = lArray[n4] ^ 1L;
            }
            bl2 = bl;
        }
        bl = (lArray[n2] & mBitmask[n3]) != 0L;
        lArray[n2] = lArray[n2] << 1;
        if (bl2) {
            int n5 = n2;
            lArray[n5] = lArray[n5] ^ 1L;
        }
        if (bl) {
            int n6 = n2;
            lArray[n6] = lArray[n6] ^ mBitmask[n3 + 1];
        }
        this.assign(lArray);
    }

    @Override
    public GFElement invert() throws ArithmeticException {
        GF2nONBElement gF2nONBElement = new GF2nONBElement(this);
        gF2nONBElement.invertThis();
        return gF2nONBElement;
    }

    public void invertThis() throws ArithmeticException {
        if (this.isZero()) {
            throw new ArithmeticException();
        }
        boolean bl = false;
        for (int i2 = 31; !bl && i2 >= 0; --i2) {
            if (((long)(this.mDegree - 1) & mBitmask[i2]) == 0L) continue;
            bl = true;
        }
        GF2nElement gF2nElement = GF2nONBElement.ZERO((GF2nONBField)this.mField);
        GF2nONBElement gF2nONBElement = new GF2nONBElement(this);
        int n2 = 1;
        for (int i3 = ++i2 - 1; i3 >= 0; --i3) {
            gF2nElement = (GF2nElement)((GF2nElement)gF2nONBElement).clone();
            for (int i4 = 1; i4 <= n2; ++i4) {
                gF2nElement.squareThis();
            }
            gF2nONBElement.multiplyThisBy(gF2nElement);
            n2 <<= 1;
            if (((long)(this.mDegree - 1) & mBitmask[i3]) == 0L) continue;
            ((GF2nElement)gF2nONBElement).squareThis();
            gF2nONBElement.multiplyThisBy(this);
            ++n2;
        }
        ((GF2nElement)gF2nONBElement).squareThis();
    }

    @Override
    public GF2nElement squareRoot() {
        GF2nONBElement gF2nONBElement = new GF2nONBElement(this);
        gF2nONBElement.squareRootThis();
        return gF2nONBElement;
    }

    @Override
    public void squareRootThis() {
        long[] lArray = this.getElement();
        int n2 = this.mLength - 1;
        int n3 = this.mBit - 1;
        long l2 = mBitmask[63];
        boolean bl = (lArray[0] & 1L) != 0L;
        for (int i2 = n2; i2 >= 0; --i2) {
            boolean bl2 = (lArray[i2] & 1L) != 0L;
            lArray[i2] = lArray[i2] >>> 1;
            if (bl) {
                if (i2 == n2) {
                    int n4 = i2;
                    lArray[n4] = lArray[n4] ^ mBitmask[n3];
                } else {
                    int n5 = i2;
                    lArray[n5] = lArray[n5] ^ l2;
                }
            }
            bl = bl2;
        }
        this.assign(lArray);
    }

    @Override
    public int trace() {
        int n2;
        int n3;
        int n4 = 0;
        int n5 = this.mLength - 1;
        for (n3 = 0; n3 < n5; ++n3) {
            for (n2 = 0; n2 < 64; ++n2) {
                if ((this.mPol[n3] & mBitmask[n2]) == 0L) continue;
                n4 ^= 1;
            }
        }
        n3 = this.mBit;
        for (n2 = 0; n2 < n3; ++n2) {
            if ((this.mPol[n5] & mBitmask[n2]) == 0L) continue;
            n4 ^= 1;
        }
        return n4;
    }

    @Override
    public GF2nElement solveQuadraticEquation() throws RuntimeException {
        int n2;
        if (this.trace() == 1) {
            throw new RuntimeException();
        }
        long l2 = mBitmask[63];
        long l3 = 0L;
        long l4 = 1L;
        long[] lArray = new long[this.mLength];
        long l5 = 0L;
        int n3 = 1;
        for (n2 = 0; n2 < this.mLength - 1; ++n2) {
            for (n3 = 1; n3 < 64; ++n3) {
                if ((mBitmask[n3] & this.mPol[n2]) != l3 && (l5 & mBitmask[n3 - 1]) != l3 || (this.mPol[n2] & mBitmask[n3]) == l3 && (l5 & mBitmask[n3 - 1]) == l3) continue;
                l5 ^= mBitmask[n3];
            }
            lArray[n2] = l5;
            l5 = (l2 & l5) != l3 && (l4 & this.mPol[n2 + 1]) == l4 || (l2 & l5) == l3 && (l4 & this.mPol[n2 + 1]) == l3 ? l3 : l4;
        }
        n2 = this.mDegree & 0x3F;
        long l6 = this.mPol[this.mLength - 1];
        for (n3 = 1; n3 < n2; ++n3) {
            if ((mBitmask[n3] & l6) != l3 && (mBitmask[n3 - 1] & l5) != l3 || (mBitmask[n3] & l6) == l3 && (mBitmask[n3 - 1] & l5) == l3) continue;
            l5 ^= mBitmask[n3];
        }
        lArray[this.mLength - 1] = l5;
        return new GF2nONBElement((GF2nONBField)this.mField, lArray);
    }

    @Override
    public String toString() {
        return this.toString(16);
    }

    @Override
    public String toString(int n2) {
        String string;
        block5: {
            long[] lArray;
            block4: {
                int n3;
                string = "";
                lArray = this.getElement();
                int n4 = this.mBit;
                if (n2 != 2) break block4;
                for (n3 = n4 - 1; n3 >= 0; --n3) {
                    string = (lArray[lArray.length - 1] & 1L << n3) == 0L ? string + "0" : string + "1";
                }
                for (n3 = lArray.length - 2; n3 >= 0; --n3) {
                    for (int i2 = 63; i2 >= 0; --i2) {
                        string = (lArray[n3] & mBitmask[i2]) == 0L ? string + "0" : string + "1";
                    }
                }
                break block5;
            }
            if (n2 != 16) break block5;
            char[] cArray = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            for (int i3 = lArray.length - 1; i3 >= 0; --i3) {
                string = string + cArray[(int)(lArray[i3] >>> 60) & 0xF];
                string = string + cArray[(int)(lArray[i3] >>> 56) & 0xF];
                string = string + cArray[(int)(lArray[i3] >>> 52) & 0xF];
                string = string + cArray[(int)(lArray[i3] >>> 48) & 0xF];
                string = string + cArray[(int)(lArray[i3] >>> 44) & 0xF];
                string = string + cArray[(int)(lArray[i3] >>> 40) & 0xF];
                string = string + cArray[(int)(lArray[i3] >>> 36) & 0xF];
                string = string + cArray[(int)(lArray[i3] >>> 32) & 0xF];
                string = string + cArray[(int)(lArray[i3] >>> 28) & 0xF];
                string = string + cArray[(int)(lArray[i3] >>> 24) & 0xF];
                string = string + cArray[(int)(lArray[i3] >>> 20) & 0xF];
                string = string + cArray[(int)(lArray[i3] >>> 16) & 0xF];
                string = string + cArray[(int)(lArray[i3] >>> 12) & 0xF];
                string = string + cArray[(int)(lArray[i3] >>> 8) & 0xF];
                string = string + cArray[(int)(lArray[i3] >>> 4) & 0xF];
                string = string + cArray[(int)lArray[i3] & 0xF];
                string = string + " ";
            }
        }
        return string;
    }

    @Override
    public BigInteger toFlexiBigInt() {
        return new BigInteger(1, this.toByteArray());
    }

    @Override
    public byte[] toByteArray() {
        int n2 = (this.mDegree - 1 >> 3) + 1;
        byte[] byArray = new byte[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            byArray[n2 - i2 - 1] = (byte)((this.mPol[i2 >>> 3] & 255L << ((i2 & 7) << 3)) >>> ((i2 & 7) << 3));
        }
        return byArray;
    }
}

