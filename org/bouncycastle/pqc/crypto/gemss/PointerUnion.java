/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.gemss;

import java.security.SecureRandom;
import org.bouncycastle.pqc.crypto.gemss.Pointer;

class PointerUnion
extends Pointer {
    protected int remainder;

    public PointerUnion(byte[] byArray) {
        super((byArray.length >> 3) + ((byArray.length & 7) != 0 ? 1 : 0));
        int n2 = 0;
        for (int i2 = 0; n2 < byArray.length && i2 < this.array.length; ++i2) {
            for (int i3 = 0; i3 < 8 && n2 < byArray.length; ++i3, ++n2) {
                int n3 = i2;
                this.array[n3] = this.array[n3] | ((long)byArray[n2] & 0xFFL) << (i3 << 3);
            }
        }
        this.remainder = 0;
    }

    public PointerUnion(int n2) {
        super((n2 >>> 3) + ((n2 & 7) != 0 ? 1 : 0));
        this.remainder = 0;
    }

    public PointerUnion(PointerUnion pointerUnion) {
        super(pointerUnion);
        this.remainder = pointerUnion.remainder;
    }

    public PointerUnion(Pointer pointer) {
        super(pointer);
        this.remainder = 0;
    }

    public void moveNextBytes(int n2) {
        this.remainder += n2;
        this.cp += this.remainder >>> 3;
        this.remainder &= 7;
    }

    public void moveNextByte() {
        ++this.remainder;
        this.cp += this.remainder >>> 3;
        this.remainder &= 7;
    }

    @Override
    public long get() {
        if (this.remainder == 0) {
            return this.array[this.cp];
        }
        return this.array[this.cp] >>> (this.remainder << 3) | this.array[this.cp + 1] << (8 - this.remainder << 3);
    }

    public long getWithCheck() {
        if (this.cp >= this.array.length) {
            return 0L;
        }
        if (this.remainder == 0) {
            return this.array[this.cp];
        }
        if (this.cp == this.array.length - 1) {
            return this.array[this.cp] >>> (this.remainder << 3);
        }
        return this.array[this.cp] >>> (this.remainder << 3) | this.array[this.cp + 1] << (8 - this.remainder << 3);
    }

    public long getWithCheck(int n2) {
        if ((n2 += this.cp) >= this.array.length) {
            return 0L;
        }
        if (this.remainder == 0) {
            return this.array[n2];
        }
        if (n2 == this.array.length - 1) {
            return this.array[n2] >>> (this.remainder << 3);
        }
        return this.array[n2] >>> (this.remainder << 3) | this.array[n2 + 1] << (8 - this.remainder << 3);
    }

    @Override
    public long get(int n2) {
        if (this.remainder == 0) {
            return this.array[this.cp + n2];
        }
        return this.array[this.cp + n2] >>> (this.remainder << 3) | this.array[this.cp + n2 + 1] << (8 - this.remainder << 3);
    }

    public byte getByte() {
        return (byte)(this.array[this.cp] >>> (this.remainder << 3));
    }

    public byte getByte(int n2) {
        int n3 = this.cp + (n2 + this.remainder >>> 3);
        int n4 = this.remainder + n2 & 7;
        return (byte)(this.array[n3] >>> (n4 << 3));
    }

    @Override
    public void setRangeClear(int n2, int n3) {
        if (this.remainder == 0) {
            super.setRangeClear(n2, n3);
        } else {
            int n4 = this.cp + n2;
            this.array[n4] = this.array[n4] & -1L >>> (8 - this.remainder << 3);
            super.setRangeClear(n2 + 1, n3);
            int n5 = this.cp + n3 + 1;
            this.array[n5] = this.array[n5] & -1L << (this.remainder << 3);
        }
    }

    @Override
    public void setAnd(int n2, long l2) {
        if (this.remainder == 0) {
            super.setAnd(n2, l2);
        } else {
            int n3 = this.remainder << 3;
            int n4 = 8 - this.remainder << 3;
            int n5 = this.cp + n2;
            this.array[n5] = this.array[n5] & (l2 << n3 | -1L >>> n4);
            int n6 = this.cp + n2 + 1;
            this.array[n6] = this.array[n6] & (l2 >>> n4 | -1L << n3);
        }
    }

    @Override
    public void indexReset() {
        this.cp = 0;
        this.remainder = 0;
    }

    public void setByteIndex(int n2) {
        this.remainder = n2 & 7;
        this.cp = n2 >>> 3;
    }

    @Override
    public byte[] toBytes(int n2) {
        byte[] byArray = new byte[n2];
        for (int i2 = this.remainder; i2 < byArray.length + this.remainder; ++i2) {
            byArray[i2 - this.remainder] = (byte)(this.array[this.cp + (i2 >>> 3)] >>> ((i2 & 7) << 3));
        }
        return byArray;
    }

    public int toBytesMove(byte[] byArray, int n2, int n3) {
        for (int i2 = 0; i2 < n3; ++i2) {
            byArray[n2++] = (byte)(this.array[this.cp] >>> (this.remainder++ << 3));
            if (this.remainder != 8) continue;
            this.remainder = 0;
            ++this.cp;
        }
        return n2;
    }

    @Override
    public void setXor(int n2, long l2) {
        if (this.remainder == 0) {
            super.setXor(n2, l2);
        } else {
            int n3 = this.cp + n2;
            this.array[n3] = this.array[n3] ^ l2 << (this.remainder << 3);
            int n4 = this.cp + n2 + 1;
            this.array[n4] = this.array[n4] ^ l2 >>> (8 - this.remainder << 3);
        }
    }

    @Override
    public void setXor(long l2) {
        if (this.remainder == 0) {
            super.setXor(l2);
        } else {
            int n2 = this.cp;
            this.array[n2] = this.array[n2] ^ l2 << (this.remainder << 3);
            int n3 = this.cp + 1;
            this.array[n3] = this.array[n3] ^ l2 >>> (8 - this.remainder << 3);
        }
    }

    @Override
    public void setXorRangeAndMask(Pointer pointer, int n2, long l2) {
        if (this.remainder == 0) {
            super.setXorRangeAndMask(pointer, n2, l2);
            return;
        }
        int n3 = this.cp;
        int n4 = pointer.cp;
        int n5 = this.remainder << 3;
        int n6 = 8 - this.remainder << 3;
        for (int i2 = 0; i2 < n2; ++i2) {
            long l3 = pointer.array[n4++] & l2;
            int n7 = n3++;
            this.array[n7] = this.array[n7] ^ l3 << n5;
            int n8 = n3;
            this.array[n8] = this.array[n8] ^ l3 >>> n6;
        }
    }

    public void setXorByte(int n2) {
        int n3 = this.cp;
        this.array[n3] = this.array[n3] ^ ((long)n2 & 0xFFL) << (this.remainder << 3);
    }

    public void setAndByte(int n2, long l2) {
        int n3 = n2 + this.remainder + (this.cp << 3);
        int n4 = n3 >>> 3;
        int n5 = n4;
        this.array[n5] = this.array[n5] & ((l2 & 0xFFL) << ((n3 &= 7) << 3) | 255L << (n3 << 3) ^ 0xFFFFFFFFFFFFFFFFL);
    }

    public void setAndThenXorByte(int n2, long l2, long l3) {
        int n3 = n2 + this.remainder + (this.cp << 3);
        int n4 = n3 >>> 3;
        int n5 = n4;
        this.array[n5] = this.array[n5] & ((l2 & 0xFFL) << ((n3 &= 7) << 3) | 255L << (n3 << 3) ^ 0xFFFFFFFFFFFFFFFFL);
        int n6 = n4;
        this.array[n6] = this.array[n6] ^ (l3 & 0xFFL) << (n3 << 3);
    }

    @Override
    public void set(int n2, long l2) {
        if (this.remainder == 0) {
            super.setXor(n2, l2);
        } else {
            int n3 = this.remainder << 3;
            int n4 = 8 - this.remainder << 3;
            this.array[this.cp + n2] = l2 << n3 | this.array[this.cp + n2] & -1L >>> n4;
            this.array[this.cp + n2 + 1] = l2 >>> n4 | this.array[this.cp + n2 + 1] & -1L << n3;
        }
    }

    public void setByte(int n2) {
        this.array[this.cp] = ((long)n2 & 0xFFL) << (this.remainder << 3) | this.array[this.cp] & -1L >>> (8 - this.remainder << 3);
    }

    @Override
    public void fill(int n2, byte[] byArray, int n3, int n4) {
        if (this.remainder != 0) {
            int n5 = this.cp + n2;
            int n6 = this.remainder;
            int n7 = n5;
            this.array[n7] = this.array[n7] & (-1L << (n6 << 3) ^ 0xFFFFFFFFFFFFFFFFL);
            for (int i2 = 0; n6 < 8 && i2 < n4; ++i2, ++n6) {
                int n8 = n5;
                this.array[n8] = this.array[n8] | ((long)byArray[n3] & 0xFFL) << (n6 << 3);
                ++n3;
            }
            ++n2;
            n4 -= 8 - this.remainder;
        }
        super.fill(n2, byArray, n3, n4);
    }

    public void fillBytes(int n2, byte[] byArray, int n3, int n4) {
        int n5 = n2 + this.remainder;
        int n6 = this.cp + (n5 >>> 3);
        if ((n5 &= 7) != 0) {
            int n7;
            int n8 = n6;
            this.array[n8] = this.array[n8] & (-1L << (n5 << 3) ^ 0xFFFFFFFFFFFFFFFFL);
            for (n7 = 0; n5 < 8 && n7 < n4; ++n7, ++n5) {
                int n9 = n6;
                this.array[n9] = this.array[n9] | ((long)byArray[n3] & 0xFFL) << (n5 << 3);
                ++n3;
            }
            ++n6;
            n4 -= n7;
        }
        super.fill(n6 - this.cp, byArray, n3, n4);
    }

    public void fillRandomBytes(int n2, SecureRandom secureRandom, int n3) {
        byte[] byArray = new byte[n3];
        secureRandom.nextBytes(byArray);
        this.fillBytes(n2, byArray, 0, byArray.length);
    }

    public void changeIndex(PointerUnion pointerUnion) {
        this.array = pointerUnion.array;
        this.cp = pointerUnion.cp;
        this.remainder = pointerUnion.remainder;
    }
}

