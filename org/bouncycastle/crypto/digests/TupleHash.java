/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Xof;
import org.bouncycastle.crypto.digests.CSHAKEDigest;
import org.bouncycastle.crypto.digests.XofUtils;
import org.bouncycastle.util.Strings;

public class TupleHash
implements Xof,
Digest {
    private static final byte[] N_TUPLE_HASH = Strings.toByteArray("TupleHash");
    private final CSHAKEDigest cshake;
    private final int bitLength;
    private final int outputLength;
    private boolean firstOutput;

    public TupleHash(int n2, byte[] byArray) {
        this(n2, byArray, n2 * 2);
    }

    public TupleHash(int n2, byte[] byArray, int n3) {
        this.cshake = new CSHAKEDigest(n2, N_TUPLE_HASH, byArray);
        this.bitLength = n2;
        this.outputLength = (n3 + 7) / 8;
        this.reset();
    }

    public TupleHash(TupleHash tupleHash) {
        this.cshake = new CSHAKEDigest(tupleHash.cshake);
        this.bitLength = this.cshake.fixedOutputLength;
        this.outputLength = this.bitLength * 2 / 8;
        this.firstOutput = tupleHash.firstOutput;
    }

    @Override
    public String getAlgorithmName() {
        return "TupleHash" + this.cshake.getAlgorithmName().substring(6);
    }

    @Override
    public int getByteLength() {
        return this.cshake.getByteLength();
    }

    @Override
    public int getDigestSize() {
        return this.outputLength;
    }

    @Override
    public void update(byte by) throws IllegalStateException {
        byte[] byArray = XofUtils.encode(by);
        this.cshake.update(byArray, 0, byArray.length);
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) throws DataLengthException, IllegalStateException {
        byte[] byArray2 = XofUtils.encode(byArray, n2, n3);
        this.cshake.update(byArray2, 0, byArray2.length);
    }

    private void wrapUp(int n2) {
        byte[] byArray = XofUtils.rightEncode((long)n2 * 8L);
        this.cshake.update(byArray, 0, byArray.length);
        this.firstOutput = false;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) throws DataLengthException, IllegalStateException {
        if (this.firstOutput) {
            this.wrapUp(this.getDigestSize());
        }
        int n3 = this.cshake.doFinal(byArray, n2, this.getDigestSize());
        this.reset();
        return n3;
    }

    @Override
    public int doFinal(byte[] byArray, int n2, int n3) {
        if (this.firstOutput) {
            this.wrapUp(this.getDigestSize());
        }
        int n4 = this.cshake.doFinal(byArray, n2, n3);
        this.reset();
        return n4;
    }

    @Override
    public int doOutput(byte[] byArray, int n2, int n3) {
        if (this.firstOutput) {
            this.wrapUp(0);
        }
        return this.cshake.doOutput(byArray, n2, n3);
    }

    @Override
    public void reset() {
        this.cshake.reset();
        this.firstOutput = true;
    }
}

