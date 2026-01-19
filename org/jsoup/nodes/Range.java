/*
 * Decompiled with CFR 0.152.
 */
package org.jsoup.nodes;

import java.util.Objects;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Node;

public class Range {
    private static final Position UntrackedPos = new Position(-1, -1, -1);
    private final Position start;
    private final Position end;
    static final Range Untracked = new Range(UntrackedPos, UntrackedPos);

    public Range(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    public Position start() {
        return this.start;
    }

    public int startPos() {
        return this.start.pos;
    }

    public Position end() {
        return this.end;
    }

    public int endPos() {
        return this.end.pos;
    }

    public boolean isTracked() {
        return this != Untracked;
    }

    public boolean isImplicit() {
        if (!this.isTracked()) {
            return false;
        }
        return this.start.equals(this.end);
    }

    static Range of(Node node, boolean start) {
        String key;
        String string = key = start ? "jsoup.start" : "jsoup.end";
        if (!node.hasAttributes()) {
            return Untracked;
        }
        Object range = node.attributes().userData(key);
        return range != null ? (Range)range : Untracked;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        Range range = (Range)o2;
        if (!this.start.equals(range.start)) {
            return false;
        }
        return this.end.equals(range.end);
    }

    public int hashCode() {
        return Objects.hash(this.start, this.end);
    }

    public String toString() {
        return this.start + "-" + this.end;
    }

    public static class Position {
        private final int pos;
        private final int lineNumber;
        private final int columnNumber;

        public Position(int pos, int lineNumber, int columnNumber) {
            this.pos = pos;
            this.lineNumber = lineNumber;
            this.columnNumber = columnNumber;
        }

        public int pos() {
            return this.pos;
        }

        public int lineNumber() {
            return this.lineNumber;
        }

        public int columnNumber() {
            return this.columnNumber;
        }

        public boolean isTracked() {
            return this != UntrackedPos;
        }

        public String toString() {
            return this.lineNumber + "," + this.columnNumber + ":" + this.pos;
        }

        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            if (o2 == null || this.getClass() != o2.getClass()) {
                return false;
            }
            Position position = (Position)o2;
            if (this.pos != position.pos) {
                return false;
            }
            if (this.lineNumber != position.lineNumber) {
                return false;
            }
            return this.columnNumber == position.columnNumber;
        }

        public int hashCode() {
            return Objects.hash(this.pos, this.lineNumber, this.columnNumber);
        }
    }

    public static class AttributeRange {
        static final AttributeRange UntrackedAttr = new AttributeRange(Untracked, Untracked);
        private final Range nameRange;
        private final Range valueRange;

        public AttributeRange(Range nameRange, Range valueRange) {
            this.nameRange = nameRange;
            this.valueRange = valueRange;
        }

        public Range nameRange() {
            return this.nameRange;
        }

        public Range valueRange() {
            return this.valueRange;
        }

        public String toString() {
            StringBuilder sb = StringUtil.borrowBuilder().append(this.nameRange).append('=').append(this.valueRange);
            return StringUtil.releaseBuilder(sb);
        }

        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            if (o2 == null || this.getClass() != o2.getClass()) {
                return false;
            }
            AttributeRange that = (AttributeRange)o2;
            if (!this.nameRange.equals(that.nameRange)) {
                return false;
            }
            return this.valueRange.equals(that.valueRange);
        }

        public int hashCode() {
            return Objects.hash(this.nameRange, this.valueRange);
        }
    }
}

