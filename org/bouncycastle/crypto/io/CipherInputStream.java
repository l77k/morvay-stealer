/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.SkippingCipher;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.io.CipherIOException;
import org.bouncycastle.crypto.io.InvalidCipherTextIOException;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.util.Arrays;

public class CipherInputStream
extends FilterInputStream {
    private static final int INPUT_BUF_SIZE = 2048;
    private SkippingCipher skippingCipher;
    private byte[] inBuf;
    private BufferedBlockCipher bufferedBlockCipher;
    private StreamCipher streamCipher;
    private AEADBlockCipher aeadBlockCipher;
    private byte[] buf;
    private byte[] markBuf;
    private int bufOff;
    private int maxBuf;
    private boolean finalized;
    private long markPosition;
    private int markBufOff;

    public CipherInputStream(InputStream inputStream2, BufferedBlockCipher bufferedBlockCipher) {
        this(inputStream2, bufferedBlockCipher, 2048);
    }

    public CipherInputStream(InputStream inputStream2, StreamCipher streamCipher) {
        this(inputStream2, streamCipher, 2048);
    }

    public CipherInputStream(InputStream inputStream2, AEADBlockCipher aEADBlockCipher) {
        this(inputStream2, aEADBlockCipher, 2048);
    }

    public CipherInputStream(InputStream inputStream2, BufferedBlockCipher bufferedBlockCipher, int n2) {
        super(inputStream2);
        this.bufferedBlockCipher = bufferedBlockCipher;
        this.inBuf = new byte[n2];
        this.skippingCipher = bufferedBlockCipher instanceof SkippingCipher ? (SkippingCipher)((Object)bufferedBlockCipher) : null;
    }

    public CipherInputStream(InputStream inputStream2, StreamCipher streamCipher, int n2) {
        super(inputStream2);
        this.streamCipher = streamCipher;
        this.inBuf = new byte[n2];
        this.skippingCipher = streamCipher instanceof SkippingCipher ? (SkippingCipher)((Object)streamCipher) : null;
    }

    public CipherInputStream(InputStream inputStream2, AEADBlockCipher aEADBlockCipher, int n2) {
        super(inputStream2);
        this.aeadBlockCipher = aEADBlockCipher;
        this.inBuf = new byte[n2];
        this.skippingCipher = aEADBlockCipher instanceof SkippingCipher ? (SkippingCipher)((Object)aEADBlockCipher) : null;
    }

    private int nextChunk() throws IOException {
        if (this.finalized) {
            return -1;
        }
        this.bufOff = 0;
        this.maxBuf = 0;
        while (this.maxBuf == 0) {
            int n2 = this.in.read(this.inBuf);
            if (n2 == -1) {
                this.finaliseCipher();
                if (this.maxBuf == 0) {
                    return -1;
                }
                return this.maxBuf;
            }
            try {
                this.ensureCapacity(n2, false);
                if (this.bufferedBlockCipher != null) {
                    this.maxBuf = this.bufferedBlockCipher.processBytes(this.inBuf, 0, n2, this.buf, 0);
                    continue;
                }
                if (this.aeadBlockCipher != null) {
                    this.maxBuf = this.aeadBlockCipher.processBytes(this.inBuf, 0, n2, this.buf, 0);
                    continue;
                }
                this.streamCipher.processBytes(this.inBuf, 0, n2, this.buf, 0);
                this.maxBuf = n2;
            }
            catch (Exception exception) {
                throw new CipherIOException("Error processing stream ", exception);
            }
        }
        return this.maxBuf;
    }

    private void finaliseCipher() throws IOException {
        try {
            this.finalized = true;
            this.ensureCapacity(0, true);
            this.maxBuf = this.bufferedBlockCipher != null ? this.bufferedBlockCipher.doFinal(this.buf, 0) : (this.aeadBlockCipher != null ? this.aeadBlockCipher.doFinal(this.buf, 0) : 0);
        }
        catch (InvalidCipherTextException invalidCipherTextException) {
            throw new InvalidCipherTextIOException("Error finalising cipher", invalidCipherTextException);
        }
        catch (Exception exception) {
            throw new IOException("Error finalising cipher " + exception);
        }
    }

    @Override
    public int read() throws IOException {
        if (this.bufOff >= this.maxBuf && this.nextChunk() < 0) {
            return -1;
        }
        return this.buf[this.bufOff++] & 0xFF;
    }

    @Override
    public int read(byte[] byArray) throws IOException {
        return this.read(byArray, 0, byArray.length);
    }

    @Override
    public int read(byte[] byArray, int n2, int n3) throws IOException {
        if (this.bufOff >= this.maxBuf && this.nextChunk() < 0) {
            return -1;
        }
        int n4 = Math.min(n3, this.available());
        System.arraycopy(this.buf, this.bufOff, byArray, n2, n4);
        this.bufOff += n4;
        return n4;
    }

    @Override
    public long skip(long l2) throws IOException {
        if (l2 <= 0L) {
            return 0L;
        }
        if (this.skippingCipher != null) {
            long l3;
            int n2 = this.available();
            if (l2 <= (long)n2) {
                this.bufOff = (int)((long)this.bufOff + l2);
                return l2;
            }
            this.bufOff = this.maxBuf;
            long l4 = this.in.skip(l2 - (long)n2);
            if (l4 != (l3 = this.skippingCipher.skip(l4))) {
                throw new IOException("Unable to skip cipher " + l4 + " bytes.");
            }
            return l4 + (long)n2;
        }
        int n3 = (int)Math.min(l2, (long)this.available());
        this.bufOff += n3;
        return n3;
    }

    @Override
    public int available() throws IOException {
        return this.maxBuf - this.bufOff;
    }

    private void ensureCapacity(int n2, boolean bl) {
        int n3 = n2;
        if (bl) {
            if (this.bufferedBlockCipher != null) {
                n3 = this.bufferedBlockCipher.getOutputSize(n2);
            } else if (this.aeadBlockCipher != null) {
                n3 = this.aeadBlockCipher.getOutputSize(n2);
            }
        } else if (this.bufferedBlockCipher != null) {
            n3 = this.bufferedBlockCipher.getUpdateOutputSize(n2);
        } else if (this.aeadBlockCipher != null) {
            n3 = this.aeadBlockCipher.getUpdateOutputSize(n2);
        }
        if (this.buf == null || this.buf.length < n3) {
            this.buf = new byte[n3];
        }
    }

    @Override
    public void close() throws IOException {
        try {
            this.in.close();
        }
        finally {
            if (!this.finalized) {
                this.finaliseCipher();
            }
        }
        this.bufOff = 0;
        this.maxBuf = 0;
        this.markBufOff = 0;
        this.markPosition = 0L;
        if (this.markBuf != null) {
            Arrays.fill(this.markBuf, (byte)0);
            this.markBuf = null;
        }
        if (this.buf != null) {
            Arrays.fill(this.buf, (byte)0);
            this.buf = null;
        }
        Arrays.fill(this.inBuf, (byte)0);
    }

    @Override
    public void mark(int n2) {
        this.in.mark(n2);
        if (this.skippingCipher != null) {
            this.markPosition = this.skippingCipher.getPosition();
        }
        if (this.buf != null) {
            this.markBuf = new byte[this.buf.length];
            System.arraycopy(this.buf, 0, this.markBuf, 0, this.buf.length);
        }
        this.markBufOff = this.bufOff;
    }

    @Override
    public void reset() throws IOException {
        if (this.skippingCipher == null) {
            throw new IOException("cipher must implement SkippingCipher to be used with reset()");
        }
        this.in.reset();
        this.skippingCipher.seekTo(this.markPosition);
        if (this.markBuf != null) {
            this.buf = this.markBuf;
        }
        this.bufOff = this.markBufOff;
    }

    @Override
    public boolean markSupported() {
        if (this.skippingCipher != null) {
            return this.in.markSupported();
        }
        return false;
    }
}

