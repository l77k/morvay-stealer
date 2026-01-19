/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.input;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;
import org.apache.commons.io.input.ProxyReader;

public class TeeReader
extends ProxyReader {
    private final Writer branch;
    private final boolean closeBranch;

    public TeeReader(Reader input, Writer branch) {
        this(input, branch, false);
    }

    public TeeReader(Reader input, Writer branch, boolean closeBranch) {
        super(input);
        this.branch = branch;
        this.closeBranch = closeBranch;
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        }
        finally {
            if (this.closeBranch) {
                this.branch.close();
            }
        }
    }

    @Override
    public int read() throws IOException {
        int ch = super.read();
        if (ch != -1) {
            this.branch.write(ch);
        }
        return ch;
    }

    @Override
    public int read(char[] chr) throws IOException {
        int n2 = super.read(chr);
        if (n2 != -1) {
            this.branch.write(chr, 0, n2);
        }
        return n2;
    }

    @Override
    public int read(char[] chr, int st, int end) throws IOException {
        int n2 = super.read(chr, st, end);
        if (n2 != -1) {
            this.branch.write(chr, st, n2);
        }
        return n2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int read(CharBuffer target) throws IOException {
        int originalPosition = target.position();
        int n2 = super.read(target);
        if (n2 != -1) {
            int newPosition = target.position();
            int newLimit = target.limit();
            try {
                target.position(originalPosition).limit(newPosition);
                this.branch.append(target);
            }
            finally {
                target.position(newPosition).limit(newLimit);
            }
        }
        return n2;
    }
}

