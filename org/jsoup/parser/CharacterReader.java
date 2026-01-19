/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package org.jsoup.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import org.jsoup.helper.Validate;
import org.jsoup.internal.SoftPool;
import org.jspecify.annotations.Nullable;

public final class CharacterReader {
    static final char EOF = '\uffff';
    private static final int MaxStringCacheLen = 12;
    private static final int StringCacheSize = 512;
    private String[] stringCache;
    private static final SoftPool<String[]> StringPool = new SoftPool<String[]>(() -> new String[512]);
    static final int BufferSize = 2048;
    static final int RefillPoint = 1024;
    private static final int RewindLimit = 1024;
    private Reader reader;
    private char[] charBuf;
    private int bufPos;
    private int bufLength;
    private int fillPoint = 0;
    private int consumed;
    private int bufMark = -1;
    private boolean readFully;
    private static final SoftPool<char[]> BufferPool = new SoftPool<char[]>(() -> new char[2048]);
    private @Nullable ArrayList<Integer> newlinePositions = null;
    private int lineNumberOffset = 1;
    private @Nullable String lastIcSeq;
    private int lastIcIndex;

    public CharacterReader(Reader input, int sz) {
        this(input);
    }

    public CharacterReader(Reader input) {
        Validate.notNull(input);
        this.reader = input;
        this.charBuf = BufferPool.borrow();
        this.stringCache = StringPool.borrow();
        this.bufferUp();
    }

    public CharacterReader(String input) {
        this(new StringReader(input));
    }

    public void close() {
        if (this.reader == null) {
            return;
        }
        try {
            this.reader.close();
        }
        catch (IOException iOException) {
        }
        finally {
            this.reader = null;
            Arrays.fill(this.charBuf, '\u0000');
            BufferPool.release(this.charBuf);
            this.charBuf = null;
            StringPool.release(this.stringCache);
            this.stringCache = null;
        }
    }

    private void bufferUp() {
        if (this.readFully || this.bufPos < this.fillPoint || this.bufMark != -1) {
            return;
        }
        this.doBufferUp();
    }

    private void doBufferUp() {
        this.consumed += this.bufPos;
        this.bufLength -= this.bufPos;
        if (this.bufLength > 0) {
            System.arraycopy(this.charBuf, this.bufPos, this.charBuf, 0, this.bufLength);
        }
        this.bufPos = 0;
        while (this.bufLength < 2048) {
            try {
                int read = this.reader.read(this.charBuf, this.bufLength, this.charBuf.length - this.bufLength);
                if (read == -1) {
                    this.readFully = true;
                    break;
                }
                this.bufLength += read;
            }
            catch (IOException e2) {
                throw new UncheckedIOException(e2);
            }
        }
        this.fillPoint = Math.min(this.bufLength, 1024);
        this.scanBufferForNewlines();
        this.lastIcSeq = null;
    }

    void mark() {
        if (this.bufLength - this.bufPos < 1024) {
            this.fillPoint = 0;
        }
        this.bufferUp();
        this.bufMark = this.bufPos;
    }

    void unmark() {
        this.bufMark = -1;
    }

    void rewindToMark() {
        if (this.bufMark == -1) {
            throw new UncheckedIOException(new IOException("Mark invalid"));
        }
        this.bufPos = this.bufMark;
        this.unmark();
    }

    public int pos() {
        return this.consumed + this.bufPos;
    }

    boolean readFully() {
        return this.readFully;
    }

    public void trackNewlines(boolean track) {
        if (track && this.newlinePositions == null) {
            this.newlinePositions = new ArrayList(25);
            this.scanBufferForNewlines();
        } else if (!track) {
            this.newlinePositions = null;
        }
    }

    public boolean isTrackNewlines() {
        return this.newlinePositions != null;
    }

    public int lineNumber() {
        return this.lineNumber(this.pos());
    }

    int lineNumber(int pos) {
        if (!this.isTrackNewlines()) {
            return 1;
        }
        int i2 = this.lineNumIndex(pos);
        if (i2 == -1) {
            return this.lineNumberOffset;
        }
        return i2 + this.lineNumberOffset + 1;
    }

    public int columnNumber() {
        return this.columnNumber(this.pos());
    }

    int columnNumber(int pos) {
        if (!this.isTrackNewlines()) {
            return pos + 1;
        }
        int i2 = this.lineNumIndex(pos);
        if (i2 == -1) {
            return pos + 1;
        }
        return pos - this.newlinePositions.get(i2) + 1;
    }

    String posLineCol() {
        return this.lineNumber() + ":" + this.columnNumber();
    }

    private int lineNumIndex(int pos) {
        if (!this.isTrackNewlines()) {
            return 0;
        }
        int i2 = Collections.binarySearch(this.newlinePositions, pos);
        if (i2 < -1) {
            i2 = Math.abs(i2) - 2;
        }
        return i2;
    }

    private void scanBufferForNewlines() {
        if (!this.isTrackNewlines()) {
            return;
        }
        if (this.newlinePositions.size() > 0) {
            int index = this.lineNumIndex(this.consumed);
            if (index == -1) {
                index = 0;
            }
            int linePos = this.newlinePositions.get(index);
            this.lineNumberOffset += index;
            this.newlinePositions.clear();
            this.newlinePositions.add(linePos);
        }
        for (int i2 = this.bufPos; i2 < this.bufLength; ++i2) {
            if (this.charBuf[i2] != '\n') continue;
            this.newlinePositions.add(1 + this.consumed + i2);
        }
    }

    public boolean isEmpty() {
        this.bufferUp();
        return this.bufPos >= this.bufLength;
    }

    private boolean isEmptyNoBufferUp() {
        return this.bufPos >= this.bufLength;
    }

    public char current() {
        this.bufferUp();
        return this.isEmptyNoBufferUp() ? (char)'\uffff' : this.charBuf[this.bufPos];
    }

    char consume() {
        this.bufferUp();
        char val = this.isEmptyNoBufferUp() ? (char)'\uffff' : this.charBuf[this.bufPos];
        ++this.bufPos;
        return val;
    }

    void unconsume() {
        if (this.bufPos < 1) {
            throw new UncheckedIOException(new IOException("WTF: No buffer left to unconsume."));
        }
        --this.bufPos;
    }

    public void advance() {
        ++this.bufPos;
    }

    int nextIndexOf(char c2) {
        this.bufferUp();
        for (int i2 = this.bufPos; i2 < this.bufLength; ++i2) {
            if (c2 != this.charBuf[i2]) continue;
            return i2 - this.bufPos;
        }
        return -1;
    }

    int nextIndexOf(CharSequence seq) {
        this.bufferUp();
        char startChar = seq.charAt(0);
        for (int offset = this.bufPos; offset < this.bufLength; ++offset) {
            if (startChar != this.charBuf[offset]) {
                while (++offset < this.bufLength && startChar != this.charBuf[offset]) {
                }
            }
            int i2 = offset + 1;
            int last = i2 + seq.length() - 1;
            if (offset >= this.bufLength || last > this.bufLength) continue;
            int j2 = 1;
            while (i2 < last && seq.charAt(j2) == this.charBuf[i2]) {
                ++i2;
                ++j2;
            }
            if (i2 != last) continue;
            return offset - this.bufPos;
        }
        return -1;
    }

    public String consumeTo(char c2) {
        int offset = this.nextIndexOf(c2);
        if (offset != -1) {
            String consumed = CharacterReader.cacheString(this.charBuf, this.stringCache, this.bufPos, offset);
            this.bufPos += offset;
            return consumed;
        }
        return this.consumeToEnd();
    }

    String consumeTo(String seq) {
        int offset = this.nextIndexOf(seq);
        if (offset != -1) {
            String consumed = CharacterReader.cacheString(this.charBuf, this.stringCache, this.bufPos, offset);
            this.bufPos += offset;
            return consumed;
        }
        if (this.bufLength - this.bufPos < seq.length()) {
            return this.consumeToEnd();
        }
        int endPos = this.bufLength - seq.length() + 1;
        String consumed = CharacterReader.cacheString(this.charBuf, this.stringCache, this.bufPos, endPos - this.bufPos);
        this.bufPos = endPos;
        return consumed;
    }

    public String consumeToAny(char ... chars) {
        int pos;
        this.bufferUp();
        int start = pos = this.bufPos;
        int remaining = this.bufLength;
        char[] val = this.charBuf;
        int charLen = chars.length;
        block0: while (pos < remaining) {
            for (int i2 = 0; i2 < charLen; ++i2) {
                if (val[pos] == chars[i2]) break block0;
            }
            ++pos;
        }
        this.bufPos = pos;
        return pos > start ? CharacterReader.cacheString(this.charBuf, this.stringCache, start, pos - start) : "";
    }

    String consumeToAnySorted(char ... chars) {
        int pos;
        this.bufferUp();
        int start = pos = this.bufPos;
        int remaining = this.bufLength;
        char[] val = this.charBuf;
        while (pos < remaining && Arrays.binarySearch(chars, val[pos]) < 0) {
            ++pos;
        }
        this.bufPos = pos;
        return this.bufPos > start ? CharacterReader.cacheString(this.charBuf, this.stringCache, start, pos - start) : "";
    }

    String consumeData() {
        int pos;
        int start = pos = this.bufPos;
        int remaining = this.bufLength;
        char[] val = this.charBuf;
        block3: while (pos < remaining) {
            switch (val[pos]) {
                case '\u0000': 
                case '&': 
                case '<': {
                    break block3;
                }
                default: {
                    ++pos;
                    continue block3;
                }
            }
        }
        this.bufPos = pos;
        return pos > start ? CharacterReader.cacheString(this.charBuf, this.stringCache, start, pos - start) : "";
    }

    /*
     * Enabled aggressive block sorting
     */
    String consumeAttributeQuoted(boolean single) {
        int pos;
        int start = pos = this.bufPos;
        int remaining = this.bufLength;
        char[] val = this.charBuf;
        block5: while (pos < remaining) {
            switch (val[pos]) {
                case '\u0000': 
                case '&': {
                    break block5;
                }
                case '\'': {
                    if (!single) break;
                    break block5;
                }
                case '\"': {
                    if (!single) break block5;
                }
            }
            ++pos;
        }
        this.bufPos = pos;
        if (pos <= start) return "";
        String string = CharacterReader.cacheString(this.charBuf, this.stringCache, start, pos - start);
        return string;
    }

    String consumeRawData() {
        int pos;
        int start = pos = this.bufPos;
        int remaining = this.bufLength;
        char[] val = this.charBuf;
        block3: while (pos < remaining) {
            switch (val[pos]) {
                case '\u0000': 
                case '<': {
                    break block3;
                }
                default: {
                    ++pos;
                    continue block3;
                }
            }
        }
        this.bufPos = pos;
        return pos > start ? CharacterReader.cacheString(this.charBuf, this.stringCache, start, pos - start) : "";
    }

    String consumeTagName() {
        int pos;
        this.bufferUp();
        int start = pos = this.bufPos;
        int remaining = this.bufLength;
        char[] val = this.charBuf;
        block3: while (pos < remaining) {
            switch (val[pos]) {
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': 
                case '/': 
                case '>': {
                    break block3;
                }
                default: {
                    ++pos;
                    continue block3;
                }
            }
        }
        this.bufPos = pos;
        return pos > start ? CharacterReader.cacheString(this.charBuf, this.stringCache, start, pos - start) : "";
    }

    String consumeToEnd() {
        this.bufferUp();
        String data = CharacterReader.cacheString(this.charBuf, this.stringCache, this.bufPos, this.bufLength - this.bufPos);
        this.bufPos = this.bufLength;
        return data;
    }

    String consumeLetterSequence() {
        char c2;
        this.bufferUp();
        int start = this.bufPos;
        while (this.bufPos < this.bufLength && ((c2 = this.charBuf[this.bufPos]) >= 'A' && c2 <= 'Z' || c2 >= 'a' && c2 <= 'z' || Character.isLetter(c2))) {
            ++this.bufPos;
        }
        return CharacterReader.cacheString(this.charBuf, this.stringCache, start, this.bufPos - start);
    }

    String consumeLetterThenDigitSequence() {
        char c2;
        this.bufferUp();
        int start = this.bufPos;
        while (this.bufPos < this.bufLength && ((c2 = this.charBuf[this.bufPos]) >= 'A' && c2 <= 'Z' || c2 >= 'a' && c2 <= 'z' || Character.isLetter(c2))) {
            ++this.bufPos;
        }
        while (!this.isEmptyNoBufferUp() && (c2 = this.charBuf[this.bufPos]) >= '0' && c2 <= '9') {
            ++this.bufPos;
        }
        return CharacterReader.cacheString(this.charBuf, this.stringCache, start, this.bufPos - start);
    }

    String consumeHexSequence() {
        char c2;
        this.bufferUp();
        int start = this.bufPos;
        while (this.bufPos < this.bufLength && ((c2 = this.charBuf[this.bufPos]) >= '0' && c2 <= '9' || c2 >= 'A' && c2 <= 'F' || c2 >= 'a' && c2 <= 'f')) {
            ++this.bufPos;
        }
        return CharacterReader.cacheString(this.charBuf, this.stringCache, start, this.bufPos - start);
    }

    String consumeDigitSequence() {
        char c2;
        this.bufferUp();
        int start = this.bufPos;
        while (this.bufPos < this.bufLength && (c2 = this.charBuf[this.bufPos]) >= '0' && c2 <= '9') {
            ++this.bufPos;
        }
        return CharacterReader.cacheString(this.charBuf, this.stringCache, start, this.bufPos - start);
    }

    boolean matches(char c2) {
        return !this.isEmpty() && this.charBuf[this.bufPos] == c2;
    }

    boolean matches(String seq) {
        this.bufferUp();
        int scanLength = seq.length();
        if (scanLength > this.bufLength - this.bufPos) {
            return false;
        }
        for (int offset = 0; offset < scanLength; ++offset) {
            if (seq.charAt(offset) == this.charBuf[this.bufPos + offset]) continue;
            return false;
        }
        return true;
    }

    boolean matchesIgnoreCase(String seq) {
        this.bufferUp();
        int scanLength = seq.length();
        if (scanLength > this.bufLength - this.bufPos) {
            return false;
        }
        for (int offset = 0; offset < scanLength; ++offset) {
            char upTarget;
            char upScan = Character.toUpperCase(seq.charAt(offset));
            if (upScan == (upTarget = Character.toUpperCase(this.charBuf[this.bufPos + offset]))) continue;
            return false;
        }
        return true;
    }

    boolean matchesAny(char ... seq) {
        if (this.isEmpty()) {
            return false;
        }
        this.bufferUp();
        char c2 = this.charBuf[this.bufPos];
        for (char seek : seq) {
            if (seek != c2) continue;
            return true;
        }
        return false;
    }

    boolean matchesAnySorted(char[] seq) {
        this.bufferUp();
        return !this.isEmpty() && Arrays.binarySearch(seq, this.charBuf[this.bufPos]) >= 0;
    }

    boolean matchesLetter() {
        if (this.isEmpty()) {
            return false;
        }
        char c2 = this.charBuf[this.bufPos];
        return c2 >= 'A' && c2 <= 'Z' || c2 >= 'a' && c2 <= 'z' || Character.isLetter(c2);
    }

    boolean matchesAsciiAlpha() {
        if (this.isEmpty()) {
            return false;
        }
        char c2 = this.charBuf[this.bufPos];
        return c2 >= 'A' && c2 <= 'Z' || c2 >= 'a' && c2 <= 'z';
    }

    boolean matchesDigit() {
        if (this.isEmpty()) {
            return false;
        }
        char c2 = this.charBuf[this.bufPos];
        return c2 >= '0' && c2 <= '9';
    }

    boolean matchConsume(String seq) {
        this.bufferUp();
        if (this.matches(seq)) {
            this.bufPos += seq.length();
            return true;
        }
        return false;
    }

    boolean matchConsumeIgnoreCase(String seq) {
        if (this.matchesIgnoreCase(seq)) {
            this.bufPos += seq.length();
            return true;
        }
        return false;
    }

    boolean containsIgnoreCase(String seq) {
        if (seq.equals(this.lastIcSeq)) {
            if (this.lastIcIndex == -1) {
                return false;
            }
            if (this.lastIcIndex >= this.bufPos) {
                return true;
            }
        }
        this.lastIcSeq = seq;
        String loScan = seq.toLowerCase(Locale.ENGLISH);
        int lo = this.nextIndexOf(loScan);
        if (lo > -1) {
            this.lastIcIndex = this.bufPos + lo;
            return true;
        }
        String hiScan = seq.toUpperCase(Locale.ENGLISH);
        int hi = this.nextIndexOf(hiScan);
        boolean found = hi > -1;
        this.lastIcIndex = found ? this.bufPos + hi : -1;
        return found;
    }

    public String toString() {
        if (this.bufLength - this.bufPos < 0) {
            return "";
        }
        return new String(this.charBuf, this.bufPos, this.bufLength - this.bufPos);
    }

    private static String cacheString(char[] charBuf, String[] stringCache, int start, int count) {
        if (count > 12) {
            return new String(charBuf, start, count);
        }
        if (count < 1) {
            return "";
        }
        int hash = 0;
        int end = count + start;
        for (int i2 = start; i2 < end; ++i2) {
            hash = 31 * hash + charBuf[i2];
        }
        int index = hash & 0x1FF;
        String cached = stringCache[index];
        if (cached != null && CharacterReader.rangeEquals(charBuf, start, count, cached)) {
            return cached;
        }
        stringCache[index] = cached = new String(charBuf, start, count);
        return cached;
    }

    static boolean rangeEquals(char[] charBuf, int start, int count, String cached) {
        if (count == cached.length()) {
            int i2 = start;
            int j2 = 0;
            while (count-- != 0) {
                if (charBuf[i2++] == cached.charAt(j2++)) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    boolean rangeEquals(int start, int count, String cached) {
        return CharacterReader.rangeEquals(this.charBuf, start, count, cached);
    }
}

