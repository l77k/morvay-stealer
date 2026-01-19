/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.engines.AEADBaseEngine;

public class ElephantEngine
extends AEADBaseEngine {
    private final ElephantParameters parameters;
    private final int BLOCK_SIZE;
    private int nBits;
    private int nSBox;
    private final int nRounds;
    private byte lfsrIV;
    private byte[] npub;
    private byte[] expanded_key;
    private boolean initialised;
    private int nb_its;
    private byte[] ad;
    private int adOff;
    private int adlen;
    private final byte[] tag_buffer;
    private byte[] previous_mask;
    private byte[] current_mask;
    private byte[] next_mask;
    private final byte[] buffer;
    private final byte[] previous_outputMessage;
    private State m_state = State.Uninitialized;
    private final ByteArrayOutputStream aadData = new ByteArrayOutputStream();
    private int inputOff;
    private byte[] inputMessage;
    private int messageLen;
    private final byte[] sBoxLayer = new byte[]{-18, -19, -21, -32, -30, -31, -28, -17, -25, -22, -24, -27, -23, -20, -29, -26, -34, -35, -37, -48, -46, -47, -44, -33, -41, -38, -40, -43, -39, -36, -45, -42, -66, -67, -69, -80, -78, -79, -76, -65, -73, -70, -72, -75, -71, -68, -77, -74, 14, 13, 11, 0, 2, 1, 4, 15, 7, 10, 8, 5, 9, 12, 3, 6, 46, 45, 43, 32, 34, 33, 36, 47, 39, 42, 40, 37, 41, 44, 35, 38, 30, 29, 27, 16, 18, 17, 20, 31, 23, 26, 24, 21, 25, 28, 19, 22, 78, 77, 75, 64, 66, 65, 68, 79, 71, 74, 72, 69, 73, 76, 67, 70, -2, -3, -5, -16, -14, -15, -12, -1, -9, -6, -8, -11, -7, -4, -13, -10, 126, 125, 123, 112, 114, 113, 116, 127, 119, 122, 120, 117, 121, 124, 115, 118, -82, -83, -85, -96, -94, -95, -92, -81, -89, -86, -88, -91, -87, -84, -93, -90, -114, -115, -117, -128, -126, -127, -124, -113, -121, -118, -120, -123, -119, -116, -125, -122, 94, 93, 91, 80, 82, 81, 84, 95, 87, 90, 88, 85, 89, 92, 83, 86, -98, -99, -101, -112, -110, -111, -108, -97, -105, -102, -104, -107, -103, -100, -109, -106, -50, -51, -53, -64, -62, -63, -60, -49, -57, -54, -56, -59, -55, -52, -61, -58, 62, 61, 59, 48, 50, 49, 52, 63, 55, 58, 56, 53, 57, 60, 51, 54, 110, 109, 107, 96, 98, 97, 100, 111, 103, 106, 104, 101, 105, 108, 99, 102};
    private final byte[] KeccakRoundConstants = new byte[]{1, -126, -118, 0, -117, 1, -127, 9, -118, -120, 9, 10, -117, -117, -119, 3, 2, -128};
    private final int[] KeccakRhoOffsets = new int[]{0, 1, 6, 4, 3, 4, 4, 6, 7, 4, 3, 2, 3, 1, 7, 1, 5, 7, 5, 0, 2, 2, 5, 0, 6};

    public ElephantEngine(ElephantParameters elephantParameters) {
        this.KEY_SIZE = 16;
        this.IV_SIZE = 12;
        switch (elephantParameters.ordinal()) {
            case 0: {
                this.BLOCK_SIZE = 20;
                this.nBits = 160;
                this.nSBox = 20;
                this.nRounds = 80;
                this.lfsrIV = (byte)117;
                this.MAC_SIZE = 8;
                this.algorithmName = "Elephant 160 AEAD";
                break;
            }
            case 1: {
                this.BLOCK_SIZE = 22;
                this.nBits = 176;
                this.nSBox = 22;
                this.nRounds = 90;
                this.lfsrIV = (byte)69;
                this.algorithmName = "Elephant 176 AEAD";
                this.MAC_SIZE = 8;
                break;
            }
            case 2: {
                this.BLOCK_SIZE = 25;
                this.nRounds = 18;
                this.algorithmName = "Elephant 200 AEAD";
                this.MAC_SIZE = 16;
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid parameter settings for Elephant");
            }
        }
        this.parameters = elephantParameters;
        this.tag_buffer = new byte[this.BLOCK_SIZE];
        this.previous_mask = new byte[this.BLOCK_SIZE];
        this.current_mask = new byte[this.BLOCK_SIZE];
        this.next_mask = new byte[this.BLOCK_SIZE];
        this.buffer = new byte[this.BLOCK_SIZE];
        this.previous_outputMessage = new byte[this.BLOCK_SIZE];
        this.initialised = false;
        this.reset(false);
    }

    private void permutation(byte[] byArray) {
        switch (this.parameters.ordinal()) {
            case 0: 
            case 1: {
                byte by = this.lfsrIV;
                byte[] byArray2 = new byte[this.nSBox];
                for (int i2 = 0; i2 < this.nRounds; ++i2) {
                    int n2;
                    byArray[0] = (byte)(byArray[0] ^ by);
                    int n3 = this.nSBox - 1;
                    byArray[n3] = (byte)(byArray[n3] ^ (byte)((by & 1) << 7 | (by & 2) << 5 | (by & 4) << 3 | (by & 8) << 1 | (by & 0x10) >>> 1 | (by & 0x20) >>> 3 | (by & 0x40) >>> 5 | (by & 0x80) >>> 7));
                    by = (byte)((by << 1 | (0x40 & by) >>> 6 ^ (0x20 & by) >>> 5) & 0x7F);
                    for (n2 = 0; n2 < this.nSBox; ++n2) {
                        byArray[n2] = this.sBoxLayer[byArray[n2] & 0xFF];
                    }
                    Arrays.fill(byArray2, (byte)0);
                    for (int i3 = 0; i3 < this.nSBox; ++i3) {
                        for (int i4 = 0; i4 < 8; ++i4) {
                            n2 = (i3 << 3) + i4;
                            if (n2 != this.nBits - 1) {
                                n2 = (n2 * this.nBits >> 2) % (this.nBits - 1);
                            }
                            int n4 = n2 >>> 3;
                            byArray2[n4] = (byte)(byArray2[n4] ^ ((byArray[i3] & 0xFF) >>> i4 & 1) << (n2 & 7));
                        }
                    }
                    System.arraycopy(byArray2, 0, byArray, 0, this.nSBox);
                }
                break;
            }
            case 2: {
                for (int i5 = 0; i5 < this.nRounds; ++i5) {
                    this.KeccakP200Round(byArray, i5);
                }
                break;
            }
        }
    }

    private byte rotl(byte by) {
        return (byte)((by & 0xFF) << 1 | (by & 0xFF) >>> 7);
    }

    private byte ROL8(byte n2, int n3) {
        return (byte)(n3 != 0 ? (n2 & 0xFF) << n3 ^ (n2 & 0xFF) >>> 8 - n3 : n2);
    }

    private int index(int n2, int n3) {
        return n2 + n3 * 5;
    }

    private void KeccakP200Round(byte[] byArray, int n2) {
        int n3;
        int n4;
        byte[] byArray2 = new byte[25];
        for (n4 = 0; n4 < 5; ++n4) {
            for (n3 = 0; n3 < 5; ++n3) {
                int n5 = n4;
                byArray2[n5] = (byte)(byArray2[n5] ^ byArray[this.index(n4, n3)]);
            }
        }
        for (n4 = 0; n4 < 5; ++n4) {
            byArray2[n4 + 5] = (byte)(this.ROL8(byArray2[(n4 + 1) % 5], 1) ^ byArray2[(n4 + 4) % 5]);
        }
        for (n4 = 0; n4 < 5; ++n4) {
            for (n3 = 0; n3 < 5; ++n3) {
                int n6 = this.index(n4, n3);
                byArray[n6] = (byte)(byArray[n6] ^ byArray2[n4 + 5]);
            }
        }
        for (n4 = 0; n4 < 5; ++n4) {
            for (n3 = 0; n3 < 5; ++n3) {
                byArray2[this.index((int)n4, (int)n3)] = this.ROL8(byArray[this.index(n4, n3)], this.KeccakRhoOffsets[this.index(n4, n3)]);
            }
        }
        for (n4 = 0; n4 < 5; ++n4) {
            for (n3 = 0; n3 < 5; ++n3) {
                byArray[this.index((int)n3, (int)((2 * n4 + 3 * n3) % 5))] = byArray2[this.index(n4, n3)];
            }
        }
        for (n3 = 0; n3 < 5; ++n3) {
            for (n4 = 0; n4 < 5; ++n4) {
                byArray2[n4] = (byte)(byArray[this.index(n4, n3)] ^ ~byArray[this.index((n4 + 1) % 5, n3)] & byArray[this.index((n4 + 2) % 5, n3)]);
            }
            for (n4 = 0; n4 < 5; ++n4) {
                byArray[this.index((int)n4, (int)n3)] = byArray2[n4];
            }
        }
        byArray[0] = (byte)(byArray[0] ^ this.KeccakRoundConstants[n2]);
    }

    private void lfsr_step(byte[] byArray, byte[] byArray2) {
        switch (this.parameters.ordinal()) {
            case 0: {
                byArray[this.BLOCK_SIZE - 1] = (byte)(((byArray2[0] & 0xFF) << 3 | (byArray2[0] & 0xFF) >>> 5) ^ (byArray2[3] & 0xFF) << 7 ^ (byArray2[13] & 0xFF) >>> 7);
                break;
            }
            case 1: {
                byArray[this.BLOCK_SIZE - 1] = (byte)(this.rotl(byArray2[0]) ^ (byArray2[3] & 0xFF) << 7 ^ (byArray2[19] & 0xFF) >>> 7);
                break;
            }
            case 2: {
                byArray[this.BLOCK_SIZE - 1] = (byte)(this.rotl(byArray2[0]) ^ this.rotl(byArray2[2]) ^ byArray2[13] << 1);
            }
        }
        System.arraycopy(byArray2, 1, byArray, 0, this.BLOCK_SIZE - 1);
    }

    private void xor_block(byte[] byArray, byte[] byArray2, int n2, int n3) {
        for (int i2 = 0; i2 < n3; ++i2) {
            int n4 = i2;
            byArray[n4] = (byte)(byArray[n4] ^ byArray2[i2 + n2]);
        }
    }

    @Override
    protected void init(byte[] byArray, byte[] byArray2) throws IllegalArgumentException {
        this.npub = byArray2;
        this.expanded_key = new byte[this.BLOCK_SIZE];
        System.arraycopy(byArray, 0, this.expanded_key, 0, this.KEY_SIZE);
        this.permutation(this.expanded_key);
        this.initialised = true;
        this.m_state = this.forEncryption ? State.EncInit : State.DecInit;
        this.inputMessage = new byte[this.BLOCK_SIZE * 2 + (this.forEncryption ? 0 : this.MAC_SIZE)];
        this.reset(false);
    }

    @Override
    public void processAADByte(byte by) {
        this.aadData.write(by);
    }

    @Override
    public void processAADBytes(byte[] byArray, int n2, int n3) {
        if (n2 + n3 > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        this.aadData.write(byArray, n2, n3);
    }

    @Override
    public int processBytes(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws DataLengthException {
        if (n2 + n3 > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (this.inputOff + n3 - (this.forEncryption ? 0 : this.MAC_SIZE) >= this.BLOCK_SIZE) {
            int n5 = this.inputOff + this.messageLen + n3 - (this.forEncryption ? 0 : this.MAC_SIZE);
            int n6 = this.processAADBytes();
            int n7 = 1 + n5 / this.BLOCK_SIZE;
            int n8 = n5 % this.BLOCK_SIZE != 0 ? n7 : n7 - 1;
            int n9 = 1 + (this.IV_SIZE + n6) / this.BLOCK_SIZE;
            int n10 = Math.max(n7 + 1, n9 - 1);
            byte[] byArray3 = new byte[Math.max(n7, 1) * this.BLOCK_SIZE];
            System.arraycopy(this.inputMessage, 0, byArray3, 0, this.inputOff);
            System.arraycopy(byArray, n2, byArray3, this.inputOff, Math.min(n3, byArray3.length - this.inputOff));
            int n11 = this.processBytes(byArray3, byArray2, n4, n10, n8, n7, n5, n9, false);
            int n12 = n11 - this.inputOff;
            if (n12 >= 0) {
                this.inputOff = this.inputOff + n3 - n11;
                System.arraycopy(byArray, n2 + n12, this.inputMessage, 0, this.inputOff);
            } else {
                System.arraycopy(this.inputMessage, this.inputOff + n12, this.inputMessage, 0, -n12);
                System.arraycopy(byArray, n2, this.inputMessage, -n12, n3);
                this.inputOff = n3 - n12;
            }
            this.messageLen += n11;
            return n11;
        }
        System.arraycopy(byArray, n2, this.inputMessage, this.inputOff, n3);
        this.inputOff += n3;
        return 0;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) throws IllegalStateException, InvalidCipherTextException {
        if (!this.initialised) {
            throw new IllegalArgumentException(this.algorithmName + " needs call init function before doFinal");
        }
        int n3 = this.inputOff;
        if (this.forEncryption && n3 + n2 + this.MAC_SIZE > byArray.length || !this.forEncryption && n3 + n2 - this.MAC_SIZE > byArray.length) {
            throw new OutputLengthException("output buffer is too short");
        }
        int n4 = n3 + this.messageLen - (this.forEncryption ? 0 : this.MAC_SIZE);
        int n5 = n4 - this.messageLen;
        int n6 = this.processAADBytes();
        int n7 = 1 + n4 / this.BLOCK_SIZE;
        int n8 = n4 % this.BLOCK_SIZE != 0 ? n7 : n7 - 1;
        int n9 = 1 + (this.IV_SIZE + n6) / this.BLOCK_SIZE;
        int n10 = Math.max(n7 + 1, n9 - 1);
        n2 += this.processBytes(this.inputMessage, byArray, n2, n10, n8, n7, n4, n9, true);
        this.mac = new byte[this.MAC_SIZE];
        this.xor_block(this.tag_buffer, this.expanded_key, 0, this.BLOCK_SIZE);
        this.permutation(this.tag_buffer);
        this.xor_block(this.tag_buffer, this.expanded_key, 0, this.BLOCK_SIZE);
        if (this.forEncryption) {
            System.arraycopy(this.tag_buffer, 0, this.mac, 0, this.MAC_SIZE);
            System.arraycopy(this.mac, 0, byArray, n2, this.mac.length);
            n5 += this.MAC_SIZE;
        } else {
            this.inputOff -= this.MAC_SIZE;
            for (int i2 = 0; i2 < this.MAC_SIZE; ++i2) {
                if (this.tag_buffer[i2] == this.inputMessage[this.inputOff + i2]) continue;
                throw new IllegalArgumentException("Mac does not match");
            }
        }
        this.reset(false);
        return n5;
    }

    @Override
    public int getUpdateOutputSize(int n2) {
        switch (this.m_state.ordinal()) {
            case 0: {
                throw new IllegalArgumentException(this.algorithmName + " needs call init function before getUpdateOutputSize");
            }
            case 4: 
            case 8: {
                return 0;
            }
            case 1: 
            case 2: 
            case 3: {
                int n3 = this.inputOff + n2;
                return n3 - n3 % this.BLOCK_SIZE;
            }
            case 5: 
            case 6: 
            case 7: {
                int n4 = Math.max(0, this.inputOff + n2 - this.MAC_SIZE);
                return n4 - n4 % this.BLOCK_SIZE;
            }
        }
        return Math.max(0, n2 + this.inputOff - this.MAC_SIZE);
    }

    @Override
    public int getOutputSize(int n2) {
        switch (this.m_state.ordinal()) {
            case 0: {
                throw new IllegalArgumentException(this.algorithmName + " needs call init function before getUpdateOutputSize");
            }
            case 4: 
            case 8: {
                return 0;
            }
            case 1: 
            case 2: 
            case 3: {
                return n2 + this.inputOff + this.MAC_SIZE;
            }
        }
        return Math.max(0, n2 + this.inputOff - this.MAC_SIZE);
    }

    private int processAADBytes() {
        byte[] byArray = this.aadData.toByteArray();
        switch (this.m_state.ordinal()) {
            case 1: 
            case 5: {
                this.processAADBytes(this.tag_buffer);
            }
        }
        return byArray.length;
    }

    @Override
    protected void reset(boolean bl) {
        this.aadData.reset();
        Arrays.fill(this.tag_buffer, (byte)0);
        Arrays.fill(this.previous_outputMessage, (byte)0);
        this.inputOff = 0;
        this.nb_its = 0;
        this.adOff = -1;
        this.messageLen = 0;
        super.reset(bl);
    }

    public int getBlockSize() {
        return this.BLOCK_SIZE;
    }

    private void checkAad() {
        switch (this.m_state.ordinal()) {
            case 7: {
                throw new IllegalArgumentException(this.algorithmName + " cannot process AAD when the length of the plaintext to be processed exceeds the a block size");
            }
            case 3: {
                throw new IllegalArgumentException(this.algorithmName + " cannot process AAD when the length of the ciphertext to be processed exceeds the a block size");
            }
            case 4: {
                throw new IllegalArgumentException(this.algorithmName + " cannot be reused for encryption");
            }
        }
    }

    private void processAADBytes(byte[] byArray) {
        this.checkAad();
        if (this.adOff == -1) {
            this.adlen = this.aadData.size();
            this.ad = this.aadData.toByteArray();
            this.adOff = 0;
        }
        int n2 = 0;
        switch (this.m_state.ordinal()) {
            case 5: {
                System.arraycopy(this.expanded_key, 0, this.current_mask, 0, this.BLOCK_SIZE);
                System.arraycopy(this.npub, 0, byArray, 0, this.IV_SIZE);
                n2 += this.IV_SIZE;
                this.m_state = State.DecAad;
                break;
            }
            case 1: {
                System.arraycopy(this.expanded_key, 0, this.current_mask, 0, this.BLOCK_SIZE);
                System.arraycopy(this.npub, 0, byArray, 0, this.IV_SIZE);
                n2 += this.IV_SIZE;
                this.m_state = State.EncAad;
                break;
            }
            case 2: 
            case 6: {
                if (this.adOff != this.adlen) break;
                Arrays.fill(byArray, 0, this.BLOCK_SIZE, (byte)0);
                byArray[0] = 1;
                return;
            }
            case 7: {
                throw new IllegalArgumentException(this.algorithmName + " cannot process AAD when the length of the plaintext to be processed exceeds the a block size");
            }
            case 3: {
                throw new IllegalArgumentException(this.algorithmName + " cannot process AAD when the length of the ciphertext to be processed exceeds the a block size");
            }
            case 4: {
                throw new IllegalArgumentException(this.algorithmName + " cannot be reused for encryption");
            }
        }
        int n3 = this.BLOCK_SIZE - n2;
        int n4 = this.adlen - this.adOff;
        if (n3 <= n4) {
            System.arraycopy(this.ad, this.adOff, byArray, n2, n3);
            this.adOff += n3;
        } else {
            if (n4 > 0) {
                System.arraycopy(this.ad, this.adOff, byArray, n2, n4);
                this.adOff += n4;
            }
            Arrays.fill(byArray, n2 + n4, n2 + n3, (byte)0);
            byArray[n2 + n4] = 1;
            switch (this.m_state.ordinal()) {
                case 6: {
                    this.m_state = State.DecData;
                    break;
                }
                case 2: {
                    this.m_state = State.EncData;
                }
            }
        }
    }

    private int processBytes(byte[] byArray, byte[] byArray2, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl) {
        int n8;
        int n9 = 0;
        byte[] byArray3 = new byte[this.BLOCK_SIZE];
        for (n8 = this.nb_its; n8 < n3; ++n8) {
            int n10;
            int n11 = n10 = n8 == n4 - 1 ? n6 - n8 * this.BLOCK_SIZE : this.BLOCK_SIZE;
            if (!bl && (n6 <= n8 * this.BLOCK_SIZE || n10 % this.BLOCK_SIZE != 0)) break;
            this.lfsr_step(this.next_mask, this.current_mask);
            if (n8 < n4) {
                System.arraycopy(this.npub, 0, this.buffer, 0, this.IV_SIZE);
                Arrays.fill(this.buffer, this.IV_SIZE, this.BLOCK_SIZE, (byte)0);
                this.xor_block(this.buffer, this.current_mask, 0, this.BLOCK_SIZE);
                this.xor_block(this.buffer, this.next_mask, 0, this.BLOCK_SIZE);
                this.permutation(this.buffer);
                this.xor_block(this.buffer, this.current_mask, 0, this.BLOCK_SIZE);
                this.xor_block(this.buffer, this.next_mask, 0, this.BLOCK_SIZE);
                this.xor_block(this.buffer, byArray, n9, n10);
                System.arraycopy(this.buffer, 0, byArray2, n2, n10);
                if (this.forEncryption) {
                    System.arraycopy(this.buffer, 0, byArray3, 0, n10);
                } else {
                    System.arraycopy(byArray, n9, byArray3, 0, n10);
                }
                n2 += n10;
                n9 += n10;
            }
            if (n8 > 0 && n8 <= n5) {
                int n12 = (n8 - 1) * this.BLOCK_SIZE;
                if (n12 == n6) {
                    Arrays.fill(this.buffer, 0, this.BLOCK_SIZE, (byte)0);
                    this.buffer[0] = 1;
                } else {
                    int n13 = n6 - n12;
                    if (this.BLOCK_SIZE <= n13) {
                        System.arraycopy(this.previous_outputMessage, 0, this.buffer, 0, this.BLOCK_SIZE);
                    } else if (n13 > 0) {
                        System.arraycopy(this.previous_outputMessage, 0, this.buffer, 0, n13);
                        Arrays.fill(this.buffer, n13, this.BLOCK_SIZE, (byte)0);
                        this.buffer[n13] = 1;
                    }
                }
                this.xor_block(this.buffer, this.previous_mask, 0, this.BLOCK_SIZE);
                this.xor_block(this.buffer, this.next_mask, 0, this.BLOCK_SIZE);
                this.permutation(this.buffer);
                this.xor_block(this.buffer, this.previous_mask, 0, this.BLOCK_SIZE);
                this.xor_block(this.buffer, this.next_mask, 0, this.BLOCK_SIZE);
                this.xor_block(this.tag_buffer, this.buffer, 0, this.BLOCK_SIZE);
            }
            if (n8 + 1 < n7) {
                this.processAADBytes(this.buffer);
                this.xor_block(this.buffer, this.next_mask, 0, this.BLOCK_SIZE);
                this.permutation(this.buffer);
                this.xor_block(this.buffer, this.next_mask, 0, this.BLOCK_SIZE);
                this.xor_block(this.tag_buffer, this.buffer, 0, this.BLOCK_SIZE);
            }
            byte[] byArray4 = this.previous_mask;
            this.previous_mask = this.current_mask;
            this.current_mask = this.next_mask;
            this.next_mask = byArray4;
            System.arraycopy(byArray3, 0, this.previous_outputMessage, 0, this.BLOCK_SIZE);
        }
        this.nb_its = n8;
        return n9;
    }

    public static enum ElephantParameters {
        elephant160,
        elephant176,
        elephant200;

    }

    private static enum State {
        Uninitialized,
        EncInit,
        EncAad,
        EncData,
        EncFinal,
        DecInit,
        DecAad,
        DecData,
        DecFinal;

    }
}

