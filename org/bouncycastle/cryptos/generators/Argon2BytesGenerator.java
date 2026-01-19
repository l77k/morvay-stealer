/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.Blake2bDigest;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Longs;
import org.bouncycastle.util.Pack;

public class Argon2BytesGenerator {
    private static final int ARGON2_BLOCK_SIZE = 1024;
    private static final int ARGON2_QWORDS_IN_BLOCK = 128;
    private static final int ARGON2_ADDRESSES_IN_BLOCK = 128;
    private static final int ARGON2_PREHASH_DIGEST_LENGTH = 64;
    private static final int ARGON2_PREHASH_SEED_LENGTH = 72;
    private static final int ARGON2_SYNC_POINTS = 4;
    private static final int MIN_PARALLELISM = 1;
    private static final int MAX_PARALLELISM = 0xFFFFFF;
    private static final int MIN_OUTLEN = 4;
    private static final int MIN_ITERATIONS = 1;
    private static final long M32L = 0xFFFFFFFFL;
    private static final byte[] ZERO_BYTES = new byte[4];
    private Argon2Parameters parameters;
    private Block[] memory;
    private int segmentLength;
    private int laneLength;

    public void init(Argon2Parameters argon2Parameters) {
        if (argon2Parameters.getVersion() != 16 && argon2Parameters.getVersion() != 19) {
            throw new UnsupportedOperationException("unknown Argon2 version");
        }
        if (argon2Parameters.getType() != 0 && argon2Parameters.getType() != 1 && argon2Parameters.getType() != 2) {
            throw new UnsupportedOperationException("unknown Argon2 type");
        }
        if (argon2Parameters.getLanes() < 1) {
            throw new IllegalStateException("lanes must be at least 1");
        }
        if (argon2Parameters.getLanes() > 0xFFFFFF) {
            throw new IllegalStateException("lanes must be at most 16777215");
        }
        if (argon2Parameters.getIterations() < 1) {
            throw new IllegalStateException("iterations is less than: 1");
        }
        this.parameters = argon2Parameters;
        int n2 = Math.max(argon2Parameters.getMemory(), 8 * argon2Parameters.getLanes());
        this.segmentLength = n2 / (4 * argon2Parameters.getLanes());
        this.laneLength = this.segmentLength * 4;
        n2 = argon2Parameters.getLanes() * this.laneLength;
        this.memory = new Block[n2];
        for (int i2 = 0; i2 < this.memory.length; ++i2) {
            this.memory[i2] = new Block();
        }
    }

    public int generateBytes(char[] cArray, byte[] byArray) {
        return this.generateBytes(this.parameters.getCharToByteConverter().convert(cArray), byArray);
    }

    public int generateBytes(char[] cArray, byte[] byArray, int n2, int n3) {
        return this.generateBytes(this.parameters.getCharToByteConverter().convert(cArray), byArray, n2, n3);
    }

    public int generateBytes(byte[] byArray, byte[] byArray2) {
        return this.generateBytes(byArray, byArray2, 0, byArray2.length);
    }

    public int generateBytes(byte[] byArray, byte[] byArray2, int n2, int n3) {
        if (n3 < 4) {
            throw new IllegalStateException("output length less than 4");
        }
        byte[] byArray3 = new byte[1024];
        this.initialize(byArray3, byArray, n3);
        this.fillMemoryBlocks();
        this.digest(byArray3, byArray2, n2, n3);
        this.reset();
        return n3;
    }

    private void reset() {
        if (null != this.memory) {
            for (int i2 = 0; i2 < this.memory.length; ++i2) {
                Block block = this.memory[i2];
                if (null == block) continue;
                block.clear();
            }
        }
    }

    private void fillMemoryBlocks() {
        FillBlock fillBlock = new FillBlock();
        Position position = new Position();
        for (int i2 = 0; i2 < this.parameters.getIterations(); ++i2) {
            position.pass = i2;
            for (int i3 = 0; i3 < 4; ++i3) {
                position.slice = i3;
                int n2 = 0;
                while (n2 < this.parameters.getLanes()) {
                    position.lane = n2++;
                    this.fillSegment(fillBlock, position);
                }
            }
        }
    }

    private void fillSegment(FillBlock fillBlock, Position position) {
        Block block = null;
        Block block2 = null;
        boolean bl = this.isDataIndependentAddressing(position);
        int n2 = Argon2BytesGenerator.getStartingIndex(position);
        int n3 = position.lane * this.laneLength + position.slice * this.segmentLength + n2;
        int n4 = this.getPrevOffset(n3);
        if (bl) {
            block = fillBlock.addressBlock.clear();
            block2 = fillBlock.inputBlock.clear();
            this.initAddressBlocks(fillBlock, position, block2, block);
        }
        boolean bl2 = this.isWithXor(position);
        for (int i2 = n2; i2 < this.segmentLength; ++i2) {
            long l2;
            int n5 = this.getRefLane(position, l2 = this.getPseudoRandom(fillBlock, i2, block, block2, n4, bl));
            int n6 = this.getRefColumn(position, i2, l2, n5 == position.lane);
            Block block3 = this.memory[n4];
            Block block4 = this.memory[this.laneLength * n5 + n6];
            Block block5 = this.memory[n3];
            if (bl2) {
                fillBlock.fillBlockWithXor(block3, block4, block5);
            } else {
                fillBlock.fillBlock(block3, block4, block5);
            }
            n4 = n3++;
        }
    }

    private boolean isDataIndependentAddressing(Position position) {
        return this.parameters.getType() == 1 || this.parameters.getType() == 2 && position.pass == 0 && position.slice < 2;
    }

    private void initAddressBlocks(FillBlock fillBlock, Position position, Block block, Block block2) {
        ((Block)block).v[0] = this.intToLong(position.pass);
        ((Block)block).v[1] = this.intToLong(position.lane);
        ((Block)block).v[2] = this.intToLong(position.slice);
        ((Block)block).v[3] = this.intToLong(this.memory.length);
        ((Block)block).v[4] = this.intToLong(this.parameters.getIterations());
        ((Block)block).v[5] = this.intToLong(this.parameters.getType());
        if (position.pass == 0 && position.slice == 0) {
            this.nextAddresses(fillBlock, block, block2);
        }
    }

    private boolean isWithXor(Position position) {
        return position.pass != 0 && this.parameters.getVersion() != 16;
    }

    private int getPrevOffset(int n2) {
        if (n2 % this.laneLength == 0) {
            return n2 + this.laneLength - 1;
        }
        return n2 - 1;
    }

    private static int getStartingIndex(Position position) {
        if (position.pass == 0 && position.slice == 0) {
            return 2;
        }
        return 0;
    }

    private void nextAddresses(FillBlock fillBlock, Block block, Block block2) {
        long[] lArray = block.v;
        lArray[6] = lArray[6] + 1L;
        fillBlock.fillBlock(block, block2);
        fillBlock.fillBlock(block2, block2);
    }

    private long getPseudoRandom(FillBlock fillBlock, int n2, Block block, Block block2, int n3, boolean bl) {
        if (bl) {
            int n4 = n2 % 128;
            if (n4 == 0) {
                this.nextAddresses(fillBlock, block2, block);
            }
            return block.v[n4];
        }
        return this.memory[n3].v[0];
    }

    private int getRefLane(Position position, long l2) {
        int n2 = (int)((l2 >>> 32) % (long)this.parameters.getLanes());
        if (position.pass == 0 && position.slice == 0) {
            n2 = position.lane;
        }
        return n2;
    }

    private int getRefColumn(Position position, int n2, long l2, boolean bl) {
        int n3;
        int n4;
        if (position.pass == 0) {
            n4 = 0;
            n3 = bl ? position.slice * this.segmentLength + n2 - 1 : position.slice * this.segmentLength + (n2 == 0 ? -1 : 0);
        } else {
            n4 = (position.slice + 1) * this.segmentLength % this.laneLength;
            n3 = bl ? this.laneLength - this.segmentLength + n2 - 1 : this.laneLength - this.segmentLength + (n2 == 0 ? -1 : 0);
        }
        long l3 = l2 & 0xFFFFFFFFL;
        l3 = l3 * l3 >>> 32;
        l3 = (long)(n3 - 1) - ((long)n3 * l3 >>> 32);
        return (int)((long)n4 + l3) % this.laneLength;
    }

    private void digest(byte[] byArray, byte[] byArray2, int n2, int n3) {
        Block block = this.memory[this.laneLength - 1];
        for (int i2 = 1; i2 < this.parameters.getLanes(); ++i2) {
            int n4 = i2 * this.laneLength + (this.laneLength - 1);
            block.xorWith(this.memory[n4]);
        }
        block.toBytes(byArray);
        this.hash(byArray, byArray2, n2, n3);
    }

    private void hash(byte[] byArray, byte[] byArray2, int n2, int n3) {
        byte[] byArray3 = new byte[4];
        Pack.intToLittleEndian(n3, byArray3, 0);
        int n4 = 64;
        if (n3 <= n4) {
            Blake2bDigest blake2bDigest = new Blake2bDigest(n3 * 8);
            blake2bDigest.update(byArray3, 0, byArray3.length);
            blake2bDigest.update(byArray, 0, byArray.length);
            blake2bDigest.doFinal(byArray2, n2);
        } else {
            Blake2bDigest blake2bDigest = new Blake2bDigest(n4 * 8);
            byte[] byArray4 = new byte[n4];
            blake2bDigest.update(byArray3, 0, byArray3.length);
            blake2bDigest.update(byArray, 0, byArray.length);
            blake2bDigest.doFinal(byArray4, 0);
            int n5 = n4 / 2;
            int n6 = n2;
            System.arraycopy(byArray4, 0, byArray2, n6, n5);
            n6 += n5;
            int n7 = (n3 + 31) / 32 - 2;
            int n8 = 2;
            while (n8 <= n7) {
                blake2bDigest.update(byArray4, 0, byArray4.length);
                blake2bDigest.doFinal(byArray4, 0);
                System.arraycopy(byArray4, 0, byArray2, n6, n5);
                ++n8;
                n6 += n5;
            }
            n8 = n3 - 32 * n7;
            blake2bDigest = new Blake2bDigest(n8 * 8);
            blake2bDigest.update(byArray4, 0, byArray4.length);
            blake2bDigest.doFinal(byArray2, n6);
        }
    }

    private static void roundFunction(Block block, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10, int n11, int n12, int n13, int n14, int n15, int n16, int n17) {
        long[] lArray = block.v;
        Argon2BytesGenerator.F(lArray, n2, n6, n10, n14);
        Argon2BytesGenerator.F(lArray, n3, n7, n11, n15);
        Argon2BytesGenerator.F(lArray, n4, n8, n12, n16);
        Argon2BytesGenerator.F(lArray, n5, n9, n13, n17);
        Argon2BytesGenerator.F(lArray, n2, n7, n12, n17);
        Argon2BytesGenerator.F(lArray, n3, n8, n13, n14);
        Argon2BytesGenerator.F(lArray, n4, n9, n10, n15);
        Argon2BytesGenerator.F(lArray, n5, n6, n11, n16);
    }

    private static void F(long[] lArray, int n2, int n3, int n4, int n5) {
        Argon2BytesGenerator.quarterRound(lArray, n2, n3, n5, 32);
        Argon2BytesGenerator.quarterRound(lArray, n4, n5, n3, 24);
        Argon2BytesGenerator.quarterRound(lArray, n2, n3, n5, 16);
        Argon2BytesGenerator.quarterRound(lArray, n4, n5, n3, 63);
    }

    private static void quarterRound(long[] lArray, int n2, int n3, int n4, int n5) {
        long l2 = lArray[n2];
        long l3 = lArray[n3];
        long l4 = lArray[n4];
        l2 += l3 + 2L * (l2 & 0xFFFFFFFFL) * (l3 & 0xFFFFFFFFL);
        l4 = Longs.rotateRight(l4 ^ l2, n5);
        lArray[n2] = l2;
        lArray[n4] = l4;
    }

    private void initialize(byte[] byArray, byte[] byArray2, int n2) {
        Blake2bDigest blake2bDigest = new Blake2bDigest(512);
        int[] nArray = new int[]{this.parameters.getLanes(), n2, this.parameters.getMemory(), this.parameters.getIterations(), this.parameters.getVersion(), this.parameters.getType()};
        Pack.intToLittleEndian(nArray, byArray, 0);
        blake2bDigest.update(byArray, 0, nArray.length * 4);
        Argon2BytesGenerator.addByteString(byArray, blake2bDigest, byArray2);
        Argon2BytesGenerator.addByteString(byArray, blake2bDigest, this.parameters.getSalt());
        Argon2BytesGenerator.addByteString(byArray, blake2bDigest, this.parameters.getSecret());
        Argon2BytesGenerator.addByteString(byArray, blake2bDigest, this.parameters.getAdditional());
        byte[] byArray3 = new byte[72];
        blake2bDigest.doFinal(byArray3, 0);
        this.fillFirstBlocks(byArray, byArray3);
    }

    private static void addByteString(byte[] byArray, Digest digest, byte[] byArray2) {
        if (null == byArray2) {
            digest.update(ZERO_BYTES, 0, 4);
            return;
        }
        Pack.intToLittleEndian(byArray2.length, byArray, 0);
        digest.update(byArray, 0, 4);
        digest.update(byArray2, 0, byArray2.length);
    }

    private void fillFirstBlocks(byte[] byArray, byte[] byArray2) {
        byte[] byArray3 = new byte[72];
        System.arraycopy(byArray2, 0, byArray3, 0, 64);
        byArray3[64] = 1;
        for (int i2 = 0; i2 < this.parameters.getLanes(); ++i2) {
            Pack.intToLittleEndian(i2, byArray2, 68);
            Pack.intToLittleEndian(i2, byArray3, 68);
            this.hash(byArray2, byArray, 0, 1024);
            this.memory[i2 * this.laneLength + 0].fromBytes(byArray);
            this.hash(byArray3, byArray, 0, 1024);
            this.memory[i2 * this.laneLength + 1].fromBytes(byArray);
        }
    }

    private long intToLong(int n2) {
        return (long)n2 & 0xFFFFFFFFL;
    }

    private static class Block {
        private static final int SIZE = 128;
        private final long[] v = new long[128];

        private Block() {
        }

        void fromBytes(byte[] byArray) {
            if (byArray.length < 1024) {
                throw new IllegalArgumentException("input shorter than blocksize");
            }
            Pack.littleEndianToLong(byArray, 0, this.v);
        }

        void toBytes(byte[] byArray) {
            if (byArray.length < 1024) {
                throw new IllegalArgumentException("output shorter than blocksize");
            }
            Pack.longToLittleEndian(this.v, byArray, 0);
        }

        private void copyBlock(Block block) {
            System.arraycopy(block.v, 0, this.v, 0, 128);
        }

        private void xor(Block block, Block block2) {
            long[] lArray = this.v;
            long[] lArray2 = block.v;
            long[] lArray3 = block2.v;
            for (int i2 = 0; i2 < 128; ++i2) {
                lArray[i2] = lArray2[i2] ^ lArray3[i2];
            }
        }

        private void xorWith(Block block) {
            long[] lArray = this.v;
            long[] lArray2 = block.v;
            for (int i2 = 0; i2 < 128; ++i2) {
                int n2 = i2;
                lArray[n2] = lArray[n2] ^ lArray2[i2];
            }
        }

        private void xorWith(Block block, Block block2) {
            long[] lArray = this.v;
            long[] lArray2 = block.v;
            long[] lArray3 = block2.v;
            for (int i2 = 0; i2 < 128; ++i2) {
                int n2 = i2;
                lArray[n2] = lArray[n2] ^ (lArray2[i2] ^ lArray3[i2]);
            }
        }

        public Block clear() {
            Arrays.fill(this.v, 0L);
            return this;
        }
    }

    private static class FillBlock {
        Block R = new Block();
        Block Z = new Block();
        Block addressBlock = new Block();
        Block inputBlock = new Block();

        private FillBlock() {
        }

        private void applyBlake() {
            int n2;
            int n3;
            for (n3 = 0; n3 < 8; ++n3) {
                n2 = 16 * n3;
                Argon2BytesGenerator.roundFunction(this.Z, n2, n2 + 1, n2 + 2, n2 + 3, n2 + 4, n2 + 5, n2 + 6, n2 + 7, n2 + 8, n2 + 9, n2 + 10, n2 + 11, n2 + 12, n2 + 13, n2 + 14, n2 + 15);
            }
            for (n3 = 0; n3 < 8; ++n3) {
                n2 = 2 * n3;
                Argon2BytesGenerator.roundFunction(this.Z, n2, n2 + 1, n2 + 16, n2 + 17, n2 + 32, n2 + 33, n2 + 48, n2 + 49, n2 + 64, n2 + 65, n2 + 80, n2 + 81, n2 + 96, n2 + 97, n2 + 112, n2 + 113);
            }
        }

        private void fillBlock(Block block, Block block2) {
            this.Z.copyBlock(block);
            this.applyBlake();
            block2.xor(block, this.Z);
        }

        private void fillBlock(Block block, Block block2, Block block3) {
            this.R.xor(block, block2);
            this.Z.copyBlock(this.R);
            this.applyBlake();
            block3.xor(this.R, this.Z);
        }

        private void fillBlockWithXor(Block block, Block block2, Block block3) {
            this.R.xor(block, block2);
            this.Z.copyBlock(this.R);
            this.applyBlake();
            block3.xorWith(this.R, this.Z);
        }
    }

    private static class Position {
        int pass;
        int lane;
        int slice;

        Position() {
        }
    }
}

