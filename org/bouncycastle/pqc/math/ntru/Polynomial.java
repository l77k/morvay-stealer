/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.math.ntru;

import org.bouncycastle.pqc.math.ntru.parameters.NTRUParameterSet;

public abstract class Polynomial {
    public short[] coeffs;
    protected NTRUParameterSet params;

    public Polynomial(NTRUParameterSet nTRUParameterSet) {
        this.coeffs = new short[nTRUParameterSet.n()];
        this.params = nTRUParameterSet;
    }

    static short bothNegativeMask(short s2, short s3) {
        return (short)((s2 & s3) >>> 15);
    }

    static short mod3(short s2) {
        return (short)((s2 & 0xFFFF) % 3);
    }

    static byte mod3(byte by) {
        return (byte)((by & 0xFF) % 3);
    }

    static int modQ(int n2, int n3) {
        return n2 % n3;
    }

    public void mod3PhiN() {
        int n2 = this.params.n();
        for (int i2 = 0; i2 < n2; ++i2) {
            this.coeffs[i2] = Polynomial.mod3((short)(this.coeffs[i2] + 2 * this.coeffs[n2 - 1]));
        }
    }

    public void modQPhiN() {
        int n2 = this.params.n();
        for (int i2 = 0; i2 < n2; ++i2) {
            this.coeffs[i2] = (short)(this.coeffs[i2] - this.coeffs[n2 - 1]);
        }
    }

    public abstract byte[] sqToBytes(int var1);

    public abstract void sqFromBytes(byte[] var1);

    public byte[] rqSumZeroToBytes(int n2) {
        return this.sqToBytes(n2);
    }

    public void rqSumZeroFromBytes(byte[] byArray) {
        int n2 = this.coeffs.length;
        this.sqFromBytes(byArray);
        this.coeffs[n2 - 1] = 0;
        for (int i2 = 0; i2 < this.params.packDegree(); ++i2) {
            int n3 = n2 - 1;
            this.coeffs[n3] = (short)(this.coeffs[n3] - this.coeffs[i2]);
        }
    }

    public byte[] s3ToBytes(int n2) {
        byte[] byArray = new byte[n2];
        this.s3ToBytes(byArray, 0);
        return byArray;
    }

    public void s3ToBytes(byte[] byArray, int n2) {
        int n3;
        int n4;
        int n5;
        int n6 = this.params.packDegree();
        int n7 = n6 - 5;
        for (n5 = 0; n5 <= n7; n5 += 5) {
            n4 = this.coeffs[n5 + 0] & 0xFF;
            n3 = (this.coeffs[n5 + 1] & 0xFF) * 3;
            int n8 = (this.coeffs[n5 + 2] & 0xFF) * 9;
            int n9 = (this.coeffs[n5 + 3] & 0xFF) * 27;
            int n10 = (this.coeffs[n5 + 4] & 0xFF) * 81;
            byArray[n2++] = (byte)(n4 + n3 + n8 + n9 + n10);
        }
        if (n5 < n6) {
            n4 = n6 - 1;
            n3 = this.coeffs[n4] & 0xFF;
            while (--n4 >= n5) {
                n3 *= 3;
                n3 += this.coeffs[n4] & 0xFF;
            }
            byArray[n2++] = (byte)n3;
        }
    }

    public void s3FromBytes(byte[] byArray) {
        byte by;
        int n2;
        int n3 = this.coeffs.length;
        for (n2 = 0; n2 < this.params.packDegree() / 5; ++n2) {
            by = byArray[n2];
            this.coeffs[5 * n2 + 0] = by;
            this.coeffs[5 * n2 + 1] = (short)((by & 0xFF) * 171 >>> 9);
            this.coeffs[5 * n2 + 2] = (short)((by & 0xFF) * 57 >>> 9);
            this.coeffs[5 * n2 + 3] = (short)((by & 0xFF) * 19 >>> 9);
            this.coeffs[5 * n2 + 4] = (short)((by & 0xFF) * 203 >>> 14);
        }
        if (this.params.packDegree() > this.params.packDegree() / 5 * 5) {
            n2 = this.params.packDegree() / 5;
            by = byArray[n2];
            int n4 = 0;
            while (5 * n2 + n4 < this.params.packDegree()) {
                this.coeffs[5 * n2 + n4] = by;
                by = (byte)((by & 0xFF) * 171 >> 9);
                ++n4;
            }
        }
        this.coeffs[n3 - 1] = 0;
        this.mod3PhiN();
    }

    public void sqMul(Polynomial polynomial, Polynomial polynomial2) {
        this.rqMul(polynomial, polynomial2);
        this.modQPhiN();
    }

    public void rqMul(Polynomial polynomial, Polynomial polynomial2) {
        int n2 = this.coeffs.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3;
            this.coeffs[i2] = 0;
            for (n3 = 1; n3 < n2 - i2; ++n3) {
                int n4 = i2;
                this.coeffs[n4] = (short)(this.coeffs[n4] + polynomial.coeffs[i2 + n3] * polynomial2.coeffs[n2 - n3]);
            }
            for (n3 = 0; n3 < i2 + 1; ++n3) {
                int n5 = i2;
                this.coeffs[n5] = (short)(this.coeffs[n5] + polynomial.coeffs[i2 - n3] * polynomial2.coeffs[n3]);
            }
        }
    }

    public void s3Mul(Polynomial polynomial, Polynomial polynomial2) {
        this.rqMul(polynomial, polynomial2);
        this.mod3PhiN();
    }

    public abstract void lift(Polynomial var1);

    public void rqToS3(Polynomial polynomial) {
        int n2 = this.coeffs.length;
        int n3 = 0;
        while (n3 < n2) {
            this.coeffs[n3] = (short)Polynomial.modQ(polynomial.coeffs[n3] & 0xFFFF, this.params.q());
            short s2 = (short)(this.coeffs[n3] >>> this.params.logQ() - 1);
            int n4 = n3++;
            this.coeffs[n4] = (short)(this.coeffs[n4] + (s2 << 1 - (this.params.logQ() & 1)));
        }
        this.mod3PhiN();
    }

    public void r2Inv(Polynomial polynomial) {
        Polynomial polynomial2 = this.params.createPolynomial();
        Polynomial polynomial3 = this.params.createPolynomial();
        Polynomial polynomial4 = this.params.createPolynomial();
        Polynomial polynomial5 = this.params.createPolynomial();
        this.r2Inv(polynomial, polynomial2, polynomial3, polynomial4, polynomial5);
    }

    public void rqInv(Polynomial polynomial) {
        Polynomial polynomial2 = this.params.createPolynomial();
        Polynomial polynomial3 = this.params.createPolynomial();
        Polynomial polynomial4 = this.params.createPolynomial();
        Polynomial polynomial5 = this.params.createPolynomial();
        this.rqInv(polynomial, polynomial2, polynomial3, polynomial4, polynomial5);
    }

    public void s3Inv(Polynomial polynomial) {
        Polynomial polynomial2 = this.params.createPolynomial();
        Polynomial polynomial3 = this.params.createPolynomial();
        Polynomial polynomial4 = this.params.createPolynomial();
        Polynomial polynomial5 = this.params.createPolynomial();
        this.s3Inv(polynomial, polynomial2, polynomial3, polynomial4, polynomial5);
    }

    void r2Inv(Polynomial polynomial, Polynomial polynomial2, Polynomial polynomial3, Polynomial polynomial4, Polynomial polynomial5) {
        int n2;
        int n3 = this.coeffs.length;
        polynomial5.coeffs[0] = 1;
        for (n2 = 0; n2 < n3; ++n2) {
            polynomial2.coeffs[n2] = 1;
        }
        for (n2 = 0; n2 < n3 - 1; ++n2) {
            polynomial3.coeffs[n3 - 2 - n2] = (short)((polynomial.coeffs[n2] ^ polynomial.coeffs[n3 - 1]) & 1);
        }
        polynomial3.coeffs[n3 - 1] = 0;
        int n4 = 1;
        for (int i2 = 0; i2 < 2 * (n3 - 1) - 1; ++i2) {
            for (n2 = n3 - 1; n2 > 0; --n2) {
                polynomial4.coeffs[n2] = polynomial4.coeffs[n2 - 1];
            }
            polynomial4.coeffs[0] = 0;
            short s2 = (short)(polynomial3.coeffs[0] & polynomial2.coeffs[0]);
            short s3 = Polynomial.bothNegativeMask((short)(-n4), -polynomial3.coeffs[0]);
            n4 = (short)(n4 ^ s3 & (n4 ^ -n4));
            n4 = (short)(n4 + 1);
            n2 = 0;
            while (n2 < n3) {
                short s4 = (short)(s3 & (polynomial2.coeffs[n2] ^ polynomial3.coeffs[n2]));
                int n5 = n2;
                polynomial2.coeffs[n5] = (short)(polynomial2.coeffs[n5] ^ s4);
                int n6 = n2;
                polynomial3.coeffs[n6] = (short)(polynomial3.coeffs[n6] ^ s4);
                s4 = (short)(s3 & (polynomial4.coeffs[n2] ^ polynomial5.coeffs[n2]));
                int n7 = n2;
                polynomial4.coeffs[n7] = (short)(polynomial4.coeffs[n7] ^ s4);
                int n8 = n2++;
                polynomial5.coeffs[n8] = (short)(polynomial5.coeffs[n8] ^ s4);
            }
            for (n2 = 0; n2 < n3; ++n2) {
                polynomial3.coeffs[n2] = (short)(polynomial3.coeffs[n2] ^ s2 & polynomial2.coeffs[n2]);
            }
            for (n2 = 0; n2 < n3; ++n2) {
                polynomial5.coeffs[n2] = (short)(polynomial5.coeffs[n2] ^ s2 & polynomial4.coeffs[n2]);
            }
            for (n2 = 0; n2 < n3 - 1; ++n2) {
                polynomial3.coeffs[n2] = polynomial3.coeffs[n2 + 1];
            }
            polynomial3.coeffs[n3 - 1] = 0;
        }
        for (n2 = 0; n2 < n3 - 1; ++n2) {
            this.coeffs[n2] = polynomial4.coeffs[n3 - 2 - n2];
        }
        this.coeffs[n3 - 1] = 0;
    }

    void rqInv(Polynomial polynomial, Polynomial polynomial2, Polynomial polynomial3, Polynomial polynomial4, Polynomial polynomial5) {
        polynomial2.r2Inv(polynomial);
        this.r2InvToRqInv(polynomial2, polynomial, polynomial3, polynomial4, polynomial5);
    }

    private void r2InvToRqInv(Polynomial polynomial, Polynomial polynomial2, Polynomial polynomial3, Polynomial polynomial4, Polynomial polynomial5) {
        int n2;
        int n3 = this.coeffs.length;
        for (n2 = 0; n2 < n3; ++n2) {
            polynomial3.coeffs[n2] = -polynomial2.coeffs[n2];
        }
        for (n2 = 0; n2 < n3; ++n2) {
            this.coeffs[n2] = polynomial.coeffs[n2];
        }
        polynomial4.rqMul(this, polynomial3);
        polynomial4.coeffs[0] = (short)(polynomial4.coeffs[0] + 2);
        polynomial5.rqMul(polynomial4, this);
        polynomial4.rqMul(polynomial5, polynomial3);
        polynomial4.coeffs[0] = (short)(polynomial4.coeffs[0] + 2);
        this.rqMul(polynomial4, polynomial5);
        polynomial4.rqMul(this, polynomial3);
        polynomial4.coeffs[0] = (short)(polynomial4.coeffs[0] + 2);
        polynomial5.rqMul(polynomial4, this);
        polynomial4.rqMul(polynomial5, polynomial3);
        polynomial4.coeffs[0] = (short)(polynomial4.coeffs[0] + 2);
        this.rqMul(polynomial4, polynomial5);
    }

    void s3Inv(Polynomial polynomial, Polynomial polynomial2, Polynomial polynomial3, Polynomial polynomial4, Polynomial polynomial5) {
        short s2;
        int n2;
        int n3 = this.coeffs.length;
        polynomial5.coeffs[0] = 1;
        for (n2 = 0; n2 < n3; ++n2) {
            polynomial2.coeffs[n2] = 1;
        }
        for (n2 = 0; n2 < n3 - 1; ++n2) {
            polynomial3.coeffs[n3 - 2 - n2] = Polynomial.mod3((short)((polynomial.coeffs[n2] & 3) + 2 * (polynomial.coeffs[n3 - 1] & 3)));
        }
        polynomial3.coeffs[n3 - 1] = 0;
        int n4 = 1;
        for (int i2 = 0; i2 < 2 * (n3 - 1) - 1; ++i2) {
            for (n2 = n3 - 1; n2 > 0; --n2) {
                polynomial4.coeffs[n2] = polynomial4.coeffs[n2 - 1];
            }
            polynomial4.coeffs[0] = 0;
            s2 = Polynomial.mod3((byte)(2 * polynomial3.coeffs[0] * polynomial2.coeffs[0]));
            short s3 = Polynomial.bothNegativeMask((short)(-n4), -polynomial3.coeffs[0]);
            n4 = (short)(n4 ^ s3 & (n4 ^ -n4));
            n4 = (short)(n4 + 1);
            n2 = 0;
            while (n2 < n3) {
                short s4 = (short)(s3 & (polynomial2.coeffs[n2] ^ polynomial3.coeffs[n2]));
                int n5 = n2;
                polynomial2.coeffs[n5] = (short)(polynomial2.coeffs[n5] ^ s4);
                int n6 = n2;
                polynomial3.coeffs[n6] = (short)(polynomial3.coeffs[n6] ^ s4);
                s4 = (short)(s3 & (polynomial4.coeffs[n2] ^ polynomial5.coeffs[n2]));
                int n7 = n2;
                polynomial4.coeffs[n7] = (short)(polynomial4.coeffs[n7] ^ s4);
                int n8 = n2++;
                polynomial5.coeffs[n8] = (short)(polynomial5.coeffs[n8] ^ s4);
            }
            for (n2 = 0; n2 < n3; ++n2) {
                polynomial3.coeffs[n2] = Polynomial.mod3((byte)(polynomial3.coeffs[n2] + s2 * polynomial2.coeffs[n2]));
            }
            for (n2 = 0; n2 < n3; ++n2) {
                polynomial5.coeffs[n2] = Polynomial.mod3((byte)(polynomial5.coeffs[n2] + s2 * polynomial4.coeffs[n2]));
            }
            for (n2 = 0; n2 < n3 - 1; ++n2) {
                polynomial3.coeffs[n2] = polynomial3.coeffs[n2 + 1];
            }
            polynomial3.coeffs[n3 - 1] = 0;
        }
        s2 = polynomial2.coeffs[0];
        for (n2 = 0; n2 < n3 - 1; ++n2) {
            this.coeffs[n2] = Polynomial.mod3((byte)(s2 * polynomial4.coeffs[n3 - 2 - n2]));
        }
        this.coeffs[n3 - 1] = 0;
    }

    public void z3ToZq() {
        int n2 = this.coeffs.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            this.coeffs[i2] = (short)(this.coeffs[i2] | -(this.coeffs[i2] >>> 1) & this.params.q() - 1);
        }
    }

    public void trinaryZqToZ3() {
        int n2 = this.coeffs.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            this.coeffs[i2] = (short)Polynomial.modQ(this.coeffs[i2] & 0xFFFF, this.params.q());
            this.coeffs[i2] = (short)(3 & (this.coeffs[i2] ^ this.coeffs[i2] >>> this.params.logQ() - 1));
        }
    }
}

