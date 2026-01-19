/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.gemss;

import java.security.SecureRandom;
import java.util.Arrays;
import org.bouncycastle.pqc.crypto.gemss.GeMSSUtils;
import org.bouncycastle.pqc.crypto.gemss.PointerUnion;
import org.bouncycastle.util.Pack;

class Pointer {
    protected long[] array;
    protected int cp;

    public Pointer() {
        this.cp = 0;
    }

    public Pointer(int n2) {
        this.array = new long[n2];
        this.cp = 0;
    }

    public Pointer(Pointer pointer) {
        this.array = pointer.array;
        this.cp = pointer.cp;
    }

    public Pointer(Pointer pointer, int n2) {
        this.array = pointer.array;
        this.cp = pointer.cp + n2;
    }

    public long get(int n2) {
        return this.array[this.cp + n2];
    }

    public long get() {
        return this.array[this.cp];
    }

    public void set(int n2, long l2) {
        this.array[this.cp + n2] = l2;
    }

    public void set(long l2) {
        this.array[this.cp] = l2;
    }

    public void setXor(int n2, long l2) {
        int n3 = this.cp + n2;
        this.array[n3] = this.array[n3] ^ l2;
    }

    public void setXor(long l2) {
        int n2 = this.cp;
        this.array[n2] = this.array[n2] ^ l2;
    }

    public void setXorRange(Pointer pointer, int n2) {
        int n3 = this.cp;
        int n4 = pointer.cp;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n5 = n3++;
            this.array[n5] = this.array[n5] ^ pointer.array[n4++];
        }
    }

    public void setXorRange(Pointer pointer, int n2, int n3) {
        int n4 = this.cp;
        n2 += pointer.cp;
        for (int i2 = 0; i2 < n3; ++i2) {
            int n5 = n4++;
            this.array[n5] = this.array[n5] ^ pointer.array[n2++];
        }
    }

    public void setXorRange(int n2, Pointer pointer, int n3, int n4) {
        n2 += this.cp;
        n3 += pointer.cp;
        for (int i2 = 0; i2 < n4; ++i2) {
            int n5 = n2++;
            this.array[n5] = this.array[n5] ^ pointer.array[n3++];
        }
    }

    public void setXorRange_SelfMove(Pointer pointer, int n2) {
        int n3 = pointer.cp;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n4 = this.cp++;
            this.array[n4] = this.array[n4] ^ pointer.array[n3++];
        }
    }

    public void setXorMatrix_NoMove(Pointer pointer, int n2, int n3) {
        int n4 = this.cp;
        for (int i2 = 0; i2 < n3; ++i2) {
            int n5 = n4;
            for (int i3 = 0; i3 < n2; ++i3) {
                int n6 = n5++;
                this.array[n6] = this.array[n6] ^ pointer.array[pointer.cp++];
            }
        }
    }

    public void setXorMatrix(Pointer pointer, int n2, int n3) {
        int n4 = this.cp;
        for (int i2 = 0; i2 < n3; ++i2) {
            int n5 = n4;
            for (int i3 = 0; i3 < n2; ++i3) {
                int n6 = n5++;
                this.array[n6] = this.array[n6] ^ pointer.array[pointer.cp++];
            }
        }
        this.cp += n2;
    }

    public void setXorRangeXor(int n2, Pointer pointer, int n3, Pointer pointer2, int n4, int n5) {
        n2 += this.cp;
        n3 += pointer.cp;
        n4 += pointer2.cp;
        for (int i2 = 0; i2 < n5; ++i2) {
            int n6 = n2++;
            this.array[n6] = this.array[n6] ^ (pointer.array[n3++] ^ pointer2.array[n4++]);
        }
    }

    public void setXorRange(int n2, PointerUnion pointerUnion, int n3, int n4) {
        n2 += this.cp;
        n3 += pointerUnion.cp;
        if (pointerUnion.remainder == 0) {
            for (int i2 = 0; i2 < n4; ++i2) {
                int n5 = n2++;
                this.array[n5] = this.array[n5] ^ pointerUnion.array[n3++];
            }
        } else {
            int n6 = pointerUnion.remainder << 3;
            int n7 = 8 - pointerUnion.remainder << 3;
            for (int i3 = 0; i3 < n4; ++i3) {
                int n8 = n2++;
                this.array[n8] = this.array[n8] ^ (pointerUnion.array[n3] >>> n6 | pointerUnion.array[++n3] << n7);
            }
        }
    }

    public void setXorRangeAndMask(Pointer pointer, int n2, long l2) {
        int n3 = this.cp;
        int n4 = pointer.cp;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n5 = n3++;
            this.array[n5] = this.array[n5] ^ pointer.array[n4++] & l2;
        }
    }

    public void setXorRangeAndMaskMove(Pointer pointer, int n2, long l2) {
        int n3 = this.cp;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n4 = n3++;
            this.array[n4] = this.array[n4] ^ pointer.array[pointer.cp++] & l2;
        }
    }

    public void setRangeRotate(int n2, Pointer pointer, int n3, int n4, int n5) {
        int n6 = 64 - n5;
        n2 += this.cp;
        n3 += pointer.cp;
        for (int i2 = 0; i2 < n4; ++i2) {
            this.array[n2++] = pointer.array[n3] >>> n6 ^ pointer.array[++n3] << n5;
        }
    }

    public void move(int n2) {
        this.cp += n2;
    }

    public void moveIncremental() {
        ++this.cp;
    }

    public long[] getArray() {
        return this.array;
    }

    public int getIndex() {
        return this.cp;
    }

    public void setAnd(int n2, long l2) {
        int n3 = this.cp + n2;
        this.array[n3] = this.array[n3] & l2;
    }

    public void setAnd(long l2) {
        int n2 = this.cp;
        this.array[n2] = this.array[n2] & l2;
    }

    public void setClear(int n2) {
        this.array[this.cp + n2] = 0L;
    }

    public void changeIndex(Pointer pointer) {
        this.array = pointer.array;
        this.cp = pointer.cp;
    }

    public void changeIndex(int n2) {
        this.cp = n2;
    }

    public void changeIndex(Pointer pointer, int n2) {
        this.array = pointer.array;
        this.cp = pointer.cp + n2;
    }

    public void setRangeClear(int n2, int n3) {
        Arrays.fill(this.array, n2 += this.cp, n2 + n3, 0L);
    }

    public int getLength() {
        return this.array.length - this.cp;
    }

    public void copyFrom(Pointer pointer, int n2) {
        System.arraycopy(pointer.array, pointer.cp, this.array, this.cp, n2);
    }

    public void copyFrom(int n2, Pointer pointer, int n3, int n4) {
        System.arraycopy(pointer.array, pointer.cp + n3, this.array, this.cp + n2, n4);
    }

    public void set1_gf2n(int n2, int n3) {
        int n4 = this.cp + n2;
        this.array[n4++] = 1L;
        for (int i2 = 1; i2 < n3; ++i2) {
            this.array[n4++] = 0L;
        }
    }

    public byte[] toBytes(int n2) {
        byte[] byArray = new byte[n2];
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            byArray[i2] = (byte)(this.array[this.cp + (i2 >>> 3)] >>> ((i2 & 7) << 3));
        }
        return byArray;
    }

    public void indexReset() {
        this.cp = 0;
    }

    public void fillRandom(int n2, SecureRandom secureRandom, int n3) {
        byte[] byArray = new byte[n3];
        secureRandom.nextBytes(byArray);
        this.fill(n2, byArray, 0, byArray.length);
    }

    public void fill(int n2, byte[] byArray, int n3, int n4) {
        int n5;
        int n6 = 0;
        for (n5 = this.cp + n2; n5 < this.array.length && n6 + 8 <= n4; ++n5) {
            this.array[n5] = Pack.littleEndianToLong(byArray, n3);
            n3 += 8;
            n6 += 8;
        }
        if (n6 < n4 && n5 < this.array.length) {
            int n7 = 0;
            this.array[n5] = 0L;
            while (n7 < 8 && n6 < n4) {
                int n8 = n5;
                this.array[n8] = this.array[n8] | ((long)byArray[n3] & 0xFFL) << (n7 << 3);
                ++n7;
                ++n3;
                ++n6;
            }
        }
    }

    public void setRangeFromXor(int n2, Pointer pointer, int n3, Pointer pointer2, int n4, int n5) {
        n2 += this.cp;
        n3 += pointer.cp;
        n4 += pointer2.cp;
        for (int i2 = 0; i2 < n5; ++i2) {
            this.array[n2++] = pointer.array[n3++] ^ pointer2.array[n4++];
        }
    }

    public void setRangeFromXor(Pointer pointer, Pointer pointer2, int n2) {
        int n3 = this.cp;
        int n4 = pointer.cp;
        int n5 = pointer2.cp;
        for (int i2 = 0; i2 < n2; ++i2) {
            this.array[n3++] = pointer.array[n4++] ^ pointer2.array[n5++];
        }
    }

    public void setRangeFromXorAndMask_xor(Pointer pointer, Pointer pointer2, long l2, int n2) {
        int n3 = this.cp;
        int n4 = pointer.cp;
        int n5 = pointer2.cp;
        for (int i2 = 0; i2 < n2; ++i2) {
            this.array[n3] = (pointer.array[n4] ^ pointer2.array[n5]) & l2;
            int n6 = n4++;
            pointer.array[n6] = pointer.array[n6] ^ this.array[n3];
            int n7 = n5++;
            pointer2.array[n7] = pointer2.array[n7] ^ this.array[n3++];
        }
    }

    public int is0_gf2n(int n2, int n3) {
        long l2 = this.get(n2);
        for (int i2 = 1; i2 < n3; ++i2) {
            l2 |= this.get(n2 + i2);
        }
        return (int)GeMSSUtils.NORBITS_UINT(l2);
    }

    public long getDotProduct(int n2, Pointer pointer, int n3, int n4) {
        n2 += this.cp;
        n3 += pointer.cp;
        long l2 = this.array[n2++] & pointer.array[n3++];
        for (int i2 = 1; i2 < n4; ++i2) {
            l2 ^= this.array[n2++] & pointer.array[n3++];
        }
        return l2;
    }

    public int getD_for_not0_or_plus(int n2, int n3) {
        int n4 = 0;
        long l2 = 0L;
        int n5 = this.cp;
        for (int i2 = n3; i2 > 0; --i2) {
            long l3 = this.array[n5++];
            for (int i3 = 1; i3 < n2; ++i3) {
                l3 |= this.array[n5++];
            }
            n4 = (int)((long)n4 + (l2 |= GeMSSUtils.ORBITS_UINT(l3)));
        }
        return n4;
    }

    public int setRange_xi(long l2, int n2, int n3) {
        int n4 = 0;
        while (n4 < n3) {
            this.array[this.cp + n2] = -(l2 >>> n4 & 1L);
            ++n4;
            ++n2;
        }
        return n2;
    }

    public int searchDegree(int n2, int n3, int n4) {
        while (this.is0_gf2n(n2 * n4, n4) != 0 && n2 >= n3) {
            --n2;
        }
        return n2;
    }

    public void setRangePointerUnion(PointerUnion pointerUnion, int n2) {
        if (pointerUnion.remainder == 0) {
            System.arraycopy(pointerUnion.array, pointerUnion.cp, this.array, this.cp, n2);
        } else {
            int n3 = 8 - pointerUnion.remainder << 3;
            int n4 = pointerUnion.remainder << 3;
            int n5 = this.cp;
            int n6 = pointerUnion.cp;
            for (int i2 = 0; i2 < n2; ++i2) {
                this.array[n5++] = pointerUnion.array[n6] >>> n4 ^ pointerUnion.array[++n6] << n3;
            }
        }
    }

    public void setRangePointerUnion(PointerUnion pointerUnion, int n2, int n3) {
        int n4 = n3 & 0x3F;
        int n5 = 64 - n4;
        int n6 = this.cp;
        int n7 = pointerUnion.cp;
        if (pointerUnion.remainder == 0) {
            for (int i2 = 0; i2 < n2; ++i2) {
                this.array[n6++] = pointerUnion.array[n7] >>> n4 ^ pointerUnion.array[++n7] << n5;
            }
        } else {
            int n8 = pointerUnion.remainder << 3;
            int n9 = 8 - pointerUnion.remainder << 3;
            for (int i3 = 0; i3 < n2; ++i3) {
                this.array[n6++] = (pointerUnion.array[n7] >>> n8 | pointerUnion.array[++n7] << n9) >>> n4 ^ (pointerUnion.array[n7] >>> n8 | pointerUnion.array[n7 + 1] << n9) << n5;
            }
        }
    }

    public void setRangePointerUnion_Check(PointerUnion pointerUnion, int n2, int n3) {
        int n4 = n3 & 0x3F;
        int n5 = 64 - n4;
        int n6 = this.cp;
        int n7 = pointerUnion.cp;
        if (pointerUnion.remainder == 0) {
            int n8;
            for (n8 = 0; n8 < n2 && n7 < pointerUnion.array.length - 1; ++n8) {
                this.array[n6++] = pointerUnion.array[n7] >>> n4 ^ pointerUnion.array[++n7] << n5;
            }
            if (n8 < n2) {
                this.array[n6] = pointerUnion.array[n7] >>> n4;
            }
        } else {
            int n9;
            int n10 = pointerUnion.remainder << 3;
            int n11 = 8 - pointerUnion.remainder << 3;
            for (n9 = 0; n9 < n2 && n7 < pointerUnion.array.length - 2; ++n9) {
                this.array[n6++] = (pointerUnion.array[n7] >>> n10 | pointerUnion.array[++n7] << n11) >>> n4 ^ (pointerUnion.array[n7] >>> n10 | pointerUnion.array[n7 + 1] << n11) << n5;
            }
            if (n9 < n2) {
                this.array[n6] = (pointerUnion.array[n7] >>> n10 | pointerUnion.array[++n7] << n11) >>> n4 ^ pointerUnion.array[n7] >>> n10 << n5;
            }
        }
    }

    public int isEqual_nocst_gf2(Pointer pointer, int n2) {
        int n3 = pointer.cp;
        int n4 = this.cp;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (this.array[n4++] == pointer.array[n3++]) continue;
            return 0;
        }
        return 1;
    }

    public void swap(Pointer pointer) {
        long[] lArray = pointer.array;
        int n2 = pointer.cp;
        pointer.array = this.array;
        pointer.cp = this.cp;
        this.array = lArray;
        this.cp = n2;
    }
}

