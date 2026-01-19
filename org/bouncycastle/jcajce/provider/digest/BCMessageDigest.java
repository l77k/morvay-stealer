/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jcajce.provider.digest;

import java.security.DigestException;
import java.security.MessageDigest;
import org.bouncycastle.crypto.Digest;

public class BCMessageDigest
extends MessageDigest {
    protected Digest digest;
    protected int digestSize;

    protected BCMessageDigest(Digest digest) {
        super(digest.getAlgorithmName());
        this.digest = digest;
        this.digestSize = digest.getDigestSize();
    }

    @Override
    public void engineReset() {
        this.digest.reset();
    }

    @Override
    public void engineUpdate(byte by) {
        this.digest.update(by);
    }

    @Override
    public void engineUpdate(byte[] byArray, int n2, int n3) {
        this.digest.update(byArray, n2, n3);
    }

    @Override
    public int engineGetDigestLength() {
        return this.digestSize;
    }

    @Override
    public byte[] engineDigest() {
        byte[] byArray = new byte[this.digestSize];
        this.digest.doFinal(byArray, 0);
        return byArray;
    }

    @Override
    public int engineDigest(byte[] byArray, int n2, int n3) throws DigestException {
        if (n3 < this.digestSize) {
            throw new DigestException("partial digests not returned");
        }
        if (byArray.length - n2 < this.digestSize) {
            throw new DigestException("insufficient space in the output buffer to store the digest");
        }
        this.digest.doFinal(byArray, n2);
        return this.digestSize;
    }
}

