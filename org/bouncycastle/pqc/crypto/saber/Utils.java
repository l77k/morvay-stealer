/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.saber;

import org.bouncycastle.pqc.crypto.saber.SABEREngine;

class Utils {
    private final int SABER_N;
    private final int SABER_L;
    private final int SABER_ET;
    private final int SABER_POLYBYTES;
    private final int SABER_EP;
    private final int SABER_KEYBYTES;
    private final boolean usingEffectiveMasking;

    public Utils(SABEREngine sABEREngine) {
        this.SABER_N = sABEREngine.getSABER_N();
        this.SABER_L = sABEREngine.getSABER_L();
        this.SABER_ET = sABEREngine.getSABER_ET();
        this.SABER_POLYBYTES = sABEREngine.getSABER_POLYBYTES();
        this.SABER_EP = sABEREngine.getSABER_EP();
        this.SABER_KEYBYTES = sABEREngine.getSABER_KEYBYTES();
        this.usingEffectiveMasking = sABEREngine.usingEffectiveMasking;
    }

    public void POLT2BS(byte[] byArray, int n2, short[] sArray) {
        block4: {
            block5: {
                block3: {
                    if (this.SABER_ET != 3) break block3;
                    for (int n3 = 0; n3 < this.SABER_N / 8; n3 = (int)((short)(n3 + 1))) {
                        short s2 = (short)(3 * n3);
                        short s3 = (short)(8 * n3);
                        byArray[n2 + s2 + 0] = (byte)(sArray[s3 + 0] & 7 | (sArray[s3 + 1] & 7) << 3 | (sArray[s3 + 2] & 3) << 6);
                        byArray[n2 + s2 + 1] = (byte)(sArray[s3 + 2] >> 2 & 1 | (sArray[s3 + 3] & 7) << 1 | (sArray[s3 + 4] & 7) << 4 | (sArray[s3 + 5] & 1) << 7);
                        byArray[n2 + s2 + 2] = (byte)(sArray[s3 + 5] >> 1 & 3 | (sArray[s3 + 6] & 7) << 2 | (sArray[s3 + 7] & 7) << 5);
                    }
                    break block4;
                }
                if (this.SABER_ET != 4) break block5;
                for (int n4 = 0; n4 < this.SABER_N / 2; n4 = (int)((short)(n4 + 1))) {
                    int n5 = n4;
                    short s4 = (short)(2 * n4);
                    byArray[n2 + n5] = (byte)(sArray[s4] & 0xF | (sArray[s4 + 1] & 0xF) << 4);
                }
                break block4;
            }
            if (this.SABER_ET != 6) break block4;
            for (int n6 = 0; n6 < this.SABER_N / 4; n6 = (int)((short)(n6 + 1))) {
                short s5 = (short)(3 * n6);
                short s6 = (short)(4 * n6);
                byArray[n2 + s5 + 0] = (byte)(sArray[s6 + 0] & 0x3F | (sArray[s6 + 1] & 3) << 6);
                byArray[n2 + s5 + 1] = (byte)(sArray[s6 + 1] >> 2 & 0xF | (sArray[s6 + 2] & 0xF) << 4);
                byArray[n2 + s5 + 2] = (byte)(sArray[s6 + 2] >> 4 & 3 | (sArray[s6 + 3] & 0x3F) << 2);
            }
        }
    }

    public void BS2POLT(byte[] byArray, int n2, short[] sArray) {
        block4: {
            block5: {
                block3: {
                    if (this.SABER_ET != 3) break block3;
                    for (int n3 = 0; n3 < this.SABER_N / 8; n3 = (int)((short)(n3 + 1))) {
                        short s2 = (short)(3 * n3);
                        short s3 = (short)(8 * n3);
                        sArray[s3 + 0] = (short)(byArray[n2 + s2 + 0] & 7);
                        sArray[s3 + 1] = (short)(byArray[n2 + s2 + 0] >> 3 & 7);
                        sArray[s3 + 2] = (short)(byArray[n2 + s2 + 0] >> 6 & 3 | (byArray[n2 + s2 + 1] & 1) << 2);
                        sArray[s3 + 3] = (short)(byArray[n2 + s2 + 1] >> 1 & 7);
                        sArray[s3 + 4] = (short)(byArray[n2 + s2 + 1] >> 4 & 7);
                        sArray[s3 + 5] = (short)(byArray[n2 + s2 + 1] >> 7 & 1 | (byArray[n2 + s2 + 2] & 3) << 1);
                        sArray[s3 + 6] = (short)(byArray[n2 + s2 + 2] >> 2 & 7);
                        sArray[s3 + 7] = (short)(byArray[n2 + s2 + 2] >> 5 & 7);
                    }
                    break block4;
                }
                if (this.SABER_ET != 4) break block5;
                for (int n4 = 0; n4 < this.SABER_N / 2; n4 = (int)((short)(n4 + 1))) {
                    int n5 = n4;
                    short s4 = (short)(2 * n4);
                    sArray[s4] = (short)(byArray[n2 + n5] & 0xF);
                    sArray[s4 + 1] = (short)(byArray[n2 + n5] >> 4 & 0xF);
                }
                break block4;
            }
            if (this.SABER_ET != 6) break block4;
            for (int n6 = 0; n6 < this.SABER_N / 4; n6 = (int)((short)(n6 + 1))) {
                short s5 = (short)(3 * n6);
                short s6 = (short)(4 * n6);
                sArray[s6 + 0] = (short)(byArray[n2 + s5 + 0] & 0x3F);
                sArray[s6 + 1] = (short)(byArray[n2 + s5 + 0] >> 6 & 3 | (byArray[n2 + s5 + 1] & 0xF) << 2);
                sArray[s6 + 2] = (short)((byArray[n2 + s5 + 1] & 0xFF) >> 4 | (byArray[n2 + s5 + 2] & 3) << 4);
                sArray[s6 + 3] = (short)((byArray[n2 + s5 + 2] & 0xFF) >> 2);
            }
        }
    }

    private void POLq2BS(byte[] byArray, int n2, short[] sArray) {
        if (!this.usingEffectiveMasking) {
            for (int n3 = 0; n3 < this.SABER_N / 8; n3 = (int)((short)(n3 + 1))) {
                short s2 = (short)(13 * n3);
                short s3 = (short)(8 * n3);
                byArray[n2 + s2 + 0] = (byte)(sArray[s3 + 0] & 0xFF);
                byArray[n2 + s2 + 1] = (byte)(sArray[s3 + 0] >> 8 & 0x1F | (sArray[s3 + 1] & 7) << 5);
                byArray[n2 + s2 + 2] = (byte)(sArray[s3 + 1] >> 3 & 0xFF);
                byArray[n2 + s2 + 3] = (byte)(sArray[s3 + 1] >> 11 & 3 | (sArray[s3 + 2] & 0x3F) << 2);
                byArray[n2 + s2 + 4] = (byte)(sArray[s3 + 2] >> 6 & 0x7F | (sArray[s3 + 3] & 1) << 7);
                byArray[n2 + s2 + 5] = (byte)(sArray[s3 + 3] >> 1 & 0xFF);
                byArray[n2 + s2 + 6] = (byte)(sArray[s3 + 3] >> 9 & 0xF | (sArray[s3 + 4] & 0xF) << 4);
                byArray[n2 + s2 + 7] = (byte)(sArray[s3 + 4] >> 4 & 0xFF);
                byArray[n2 + s2 + 8] = (byte)(sArray[s3 + 4] >> 12 & 1 | (sArray[s3 + 5] & 0x7F) << 1);
                byArray[n2 + s2 + 9] = (byte)(sArray[s3 + 5] >> 7 & 0x3F | (sArray[s3 + 6] & 3) << 6);
                byArray[n2 + s2 + 10] = (byte)(sArray[s3 + 6] >> 2 & 0xFF);
                byArray[n2 + s2 + 11] = (byte)(sArray[s3 + 6] >> 10 & 7 | (sArray[s3 + 7] & 0x1F) << 3);
                byArray[n2 + s2 + 12] = (byte)(sArray[s3 + 7] >> 5 & 0xFF);
            }
        } else {
            for (int n4 = 0; n4 < this.SABER_N / 2; n4 = (int)((short)(n4 + 1))) {
                short s4 = (short)(3 * n4);
                short s5 = (short)(2 * n4);
                byArray[n2 + s4 + 0] = (byte)(sArray[s5 + 0] & 0xFF);
                byArray[n2 + s4 + 1] = (byte)(sArray[s5 + 0] >> 8 & 0xF | (sArray[s5 + 1] & 0xF) << 4);
                byArray[n2 + s4 + 2] = (byte)(sArray[s5 + 1] >> 4 & 0xFF);
            }
        }
    }

    private void BS2POLq(byte[] byArray, int n2, short[] sArray) {
        if (!this.usingEffectiveMasking) {
            for (int n3 = 0; n3 < this.SABER_N / 8; n3 = (int)((short)(n3 + 1))) {
                short s2 = (short)(13 * n3);
                short s3 = (short)(8 * n3);
                sArray[s3 + 0] = (short)(byArray[n2 + s2 + 0] & 0xFF | (byArray[n2 + s2 + 1] & 0x1F) << 8);
                sArray[s3 + 1] = (short)(byArray[n2 + s2 + 1] >> 5 & 7 | (byArray[n2 + s2 + 2] & 0xFF) << 3 | (byArray[n2 + s2 + 3] & 3) << 11);
                sArray[s3 + 2] = (short)(byArray[n2 + s2 + 3] >> 2 & 0x3F | (byArray[n2 + s2 + 4] & 0x7F) << 6);
                sArray[s3 + 3] = (short)(byArray[n2 + s2 + 4] >> 7 & 1 | (byArray[n2 + s2 + 5] & 0xFF) << 1 | (byArray[n2 + s2 + 6] & 0xF) << 9);
                sArray[s3 + 4] = (short)(byArray[n2 + s2 + 6] >> 4 & 0xF | (byArray[n2 + s2 + 7] & 0xFF) << 4 | (byArray[n2 + s2 + 8] & 1) << 12);
                sArray[s3 + 5] = (short)(byArray[n2 + s2 + 8] >> 1 & 0x7F | (byArray[n2 + s2 + 9] & 0x3F) << 7);
                sArray[s3 + 6] = (short)(byArray[n2 + s2 + 9] >> 6 & 3 | (byArray[n2 + s2 + 10] & 0xFF) << 2 | (byArray[n2 + s2 + 11] & 7) << 10);
                sArray[s3 + 7] = (short)(byArray[n2 + s2 + 11] >> 3 & 0x1F | (byArray[n2 + s2 + 12] & 0xFF) << 5);
            }
        } else {
            for (int n4 = 0; n4 < this.SABER_N / 2; n4 = (int)((short)(n4 + 1))) {
                short s4 = (short)(3 * n4);
                short s5 = (short)(2 * n4);
                sArray[s5 + 0] = (short)(byArray[n2 + s4 + 0] & 0xFF | (byArray[n2 + s4 + 1] & 0xF) << 8);
                sArray[s5 + 1] = (short)(byArray[n2 + s4 + 1] >> 4 & 0xF | (byArray[n2 + s4 + 2] & 0xFF) << 4);
            }
        }
    }

    private void POLp2BS(byte[] byArray, int n2, short[] sArray) {
        for (int n3 = 0; n3 < this.SABER_N / 4; n3 = (int)((short)(n3 + 1))) {
            short s2 = (short)(5 * n3);
            short s3 = (short)(4 * n3);
            byArray[n2 + s2 + 0] = (byte)(sArray[s3 + 0] & 0xFF);
            byArray[n2 + s2 + 1] = (byte)(sArray[s3 + 0] >> 8 & 3 | (sArray[s3 + 1] & 0x3F) << 2);
            byArray[n2 + s2 + 2] = (byte)(sArray[s3 + 1] >> 6 & 0xF | (sArray[s3 + 2] & 0xF) << 4);
            byArray[n2 + s2 + 3] = (byte)(sArray[s3 + 2] >> 4 & 0x3F | (sArray[s3 + 3] & 3) << 6);
            byArray[n2 + s2 + 4] = (byte)(sArray[s3 + 3] >> 2 & 0xFF);
        }
    }

    public void BS2POLp(byte[] byArray, int n2, short[] sArray) {
        for (int n3 = 0; n3 < this.SABER_N / 4; n3 = (int)((short)(n3 + 1))) {
            short s2 = (short)(5 * n3);
            short s3 = (short)(4 * n3);
            sArray[s3 + 0] = (short)(byArray[n2 + s2 + 0] & 0xFF | (byArray[n2 + s2 + 1] & 3) << 8);
            sArray[s3 + 1] = (short)(byArray[n2 + s2 + 1] >> 2 & 0x3F | (byArray[n2 + s2 + 2] & 0xF) << 6);
            sArray[s3 + 2] = (short)(byArray[n2 + s2 + 2] >> 4 & 0xF | (byArray[n2 + s2 + 3] & 0x3F) << 4);
            sArray[s3 + 3] = (short)(byArray[n2 + s2 + 3] >> 6 & 3 | (byArray[n2 + s2 + 4] & 0xFF) << 2);
        }
    }

    public void POLVECq2BS(byte[] byArray, short[][] sArray) {
        for (int n2 = 0; n2 < this.SABER_L; n2 = (int)((byte)(n2 + 1))) {
            this.POLq2BS(byArray, n2 * this.SABER_POLYBYTES, sArray[n2]);
        }
    }

    public void BS2POLVECq(byte[] byArray, int n2, short[][] sArray) {
        for (int n3 = 0; n3 < this.SABER_L; n3 = (int)((byte)(n3 + 1))) {
            this.BS2POLq(byArray, n2 + n3 * this.SABER_POLYBYTES, sArray[n3]);
        }
    }

    public void POLVECp2BS(byte[] byArray, short[][] sArray) {
        for (int n2 = 0; n2 < this.SABER_L; n2 = (int)((byte)(n2 + 1))) {
            this.POLp2BS(byArray, n2 * (this.SABER_EP * this.SABER_N / 8), sArray[n2]);
        }
    }

    public void BS2POLVECp(byte[] byArray, short[][] sArray) {
        for (int n2 = 0; n2 < this.SABER_L; n2 = (int)((byte)(n2 + 1))) {
            this.BS2POLp(byArray, n2 * (this.SABER_EP * this.SABER_N / 8), sArray[n2]);
        }
    }

    public void BS2POLmsg(byte[] byArray, short[] sArray) {
        for (int n2 = 0; n2 < this.SABER_KEYBYTES; n2 = (int)((byte)(n2 + 1))) {
            for (int n3 = 0; n3 < 8; n3 = (int)((byte)(n3 + 1))) {
                sArray[n2 * 8 + n3] = (short)(byArray[n2] >> n3 & 1);
            }
        }
    }

    public void POLmsg2BS(byte[] byArray, short[] sArray) {
        for (int n2 = 0; n2 < this.SABER_KEYBYTES; n2 = (int)((byte)(n2 + 1))) {
            for (int n3 = 0; n3 < 8; n3 = (int)((byte)(n3 + 1))) {
                byArray[n2] = (byte)(byArray[n2] | (sArray[n2 * 8 + n3] & 1) << n3);
            }
        }
    }
}

