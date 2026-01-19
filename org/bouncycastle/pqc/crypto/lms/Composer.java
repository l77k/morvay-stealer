/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.lms;

import java.io.ByteArrayOutputStream;
import org.bouncycastle.util.Encodable;

public class Composer {
    private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

    private Composer() {
    }

    public static Composer compose() {
        return new Composer();
    }

    public Composer u64str(long l2) {
        this.u32str((int)(l2 >>> 32));
        this.u32str((int)l2);
        return this;
    }

    public Composer u32str(int n2) {
        this.bos.write((byte)(n2 >>> 24));
        this.bos.write((byte)(n2 >>> 16));
        this.bos.write((byte)(n2 >>> 8));
        this.bos.write((byte)n2);
        return this;
    }

    public Composer u16str(int n2) {
        this.bos.write((byte)((n2 &= 0xFFFF) >>> 8));
        this.bos.write((byte)n2);
        return this;
    }

    public Composer bytes(Encodable[] encodableArray) {
        try {
            for (Encodable encodable : encodableArray) {
                this.bos.write(encodable.getEncoded());
            }
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.getMessage(), exception);
        }
        return this;
    }

    public Composer bytes(Encodable encodable) {
        try {
            this.bos.write(encodable.getEncoded());
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.getMessage(), exception);
        }
        return this;
    }

    public Composer pad(int n2, int n3) {
        while (n3 >= 0) {
            try {
                this.bos.write(n2);
            }
            catch (Exception exception) {
                throw new RuntimeException(exception.getMessage(), exception);
            }
            --n3;
        }
        return this;
    }

    public Composer bytes(byte[][] byArray) {
        try {
            for (byte[] byArray2 : byArray) {
                this.bos.write(byArray2);
            }
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.getMessage(), exception);
        }
        return this;
    }

    public Composer bytes(byte[][] byArray, int n2, int n3) {
        try {
            for (int i2 = n2; i2 != n3; ++i2) {
                this.bos.write(byArray[i2]);
            }
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.getMessage(), exception);
        }
        return this;
    }

    public Composer bytes(byte[] byArray) {
        try {
            this.bos.write(byArray);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.getMessage(), exception);
        }
        return this;
    }

    public Composer bytes(byte[] byArray, int n2, int n3) {
        try {
            this.bos.write(byArray, n2, n3);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.getMessage(), exception);
        }
        return this;
    }

    public byte[] build() {
        return this.bos.toByteArray();
    }

    public Composer padUntil(int n2, int n3) {
        while (this.bos.size() < n3) {
            this.bos.write(n2);
        }
        return this;
    }

    public Composer bool(boolean bl) {
        this.bos.write(bl ? 1 : 0);
        return this;
    }
}

