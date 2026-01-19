/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.io.CipherIOException;
import org.bouncycastle.crypto.io.InvalidCipherTextIOException;
import org.bouncycastle.crypto.modes.AEADBlockCipher;

public class CipherOutputStream
extends FilterOutputStream {
    private BufferedBlockCipher bufferedBlockCipher;
    private StreamCipher streamCipher;
    private AEADBlockCipher aeadBlockCipher;
    private final byte[] oneByte = new byte[1];
    private byte[] buf;

    public CipherOutputStream(OutputStream outputStream2, BufferedBlockCipher bufferedBlockCipher) {
        super(outputStream2);
        this.bufferedBlockCipher = bufferedBlockCipher;
    }

    public CipherOutputStream(OutputStream outputStream2, StreamCipher streamCipher) {
        super(outputStream2);
        this.streamCipher = streamCipher;
    }

    public CipherOutputStream(OutputStream outputStream2, AEADBlockCipher aEADBlockCipher) {
        super(outputStream2);
        this.aeadBlockCipher = aEADBlockCipher;
    }

    @Override
    public void write(int n2) throws IOException {
        this.oneByte[0] = (byte)n2;
        if (this.streamCipher != null) {
            this.out.write(this.streamCipher.returnByte((byte)n2));
        } else {
            this.write(this.oneByte, 0, 1);
        }
    }

    @Override
    public void write(byte[] byArray) throws IOException {
        this.write(byArray, 0, byArray.length);
    }

    @Override
    public void write(byte[] byArray, int n2, int n3) throws IOException {
        this.ensureCapacity(n3, false);
        if (this.bufferedBlockCipher != null) {
            int n4 = this.bufferedBlockCipher.processBytes(byArray, n2, n3, this.buf, 0);
            if (n4 != 0) {
                this.out.write(this.buf, 0, n4);
            }
        } else if (this.aeadBlockCipher != null) {
            int n5 = this.aeadBlockCipher.processBytes(byArray, n2, n3, this.buf, 0);
            if (n5 != 0) {
                this.out.write(this.buf, 0, n5);
            }
        } else {
            this.streamCipher.processBytes(byArray, n2, n3, this.buf, 0);
            this.out.write(this.buf, 0, n3);
        }
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
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override
    public void close() throws IOException {
        IOException iOException;
        block13: {
            this.ensureCapacity(0, true);
            iOException = null;
            try {
                int n2;
                if (this.bufferedBlockCipher != null) {
                    n2 = this.bufferedBlockCipher.doFinal(this.buf, 0);
                    if (n2 != 0) {
                        this.out.write(this.buf, 0, n2);
                    }
                } else if (this.aeadBlockCipher != null) {
                    n2 = this.aeadBlockCipher.doFinal(this.buf, 0);
                    if (n2 != 0) {
                        this.out.write(this.buf, 0, n2);
                    }
                } else if (this.streamCipher != null) {
                    this.streamCipher.reset();
                }
            }
            catch (InvalidCipherTextException invalidCipherTextException) {
                iOException = new InvalidCipherTextIOException("Error finalising cipher data", invalidCipherTextException);
            }
            catch (Exception exception) {
                iOException = new CipherIOException("Error closing stream: ", exception);
            }
            try {
                this.flush();
                this.out.close();
            }
            catch (IOException iOException2) {
                if (iOException != null) break block13;
                iOException = iOException2;
            }
        }
        if (iOException != null) {
            throw iOException;
        }
    }
}

