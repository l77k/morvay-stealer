/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package org.jsoup.nodes;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.jsoup.internal.Normalizer;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Range;
import org.jsoup.parser.ParseSettings;
import org.jspecify.annotations.Nullable;

public class Attributes
implements Iterable<Attribute>,
Cloneable {
    static final char InternalPrefix = '/';
    protected static final String dataPrefix = "data-";
    private static final int InitialCapacity = 3;
    private static final int GrowthFactor = 2;
    static final int NotFound = -1;
    private static final String EmptyString = "";
    private int size = 0;
    @Nullable String[] keys = new String[3];
    @Nullable Object[] vals = new Object[3];

    private void checkCapacity(int minNewSize) {
        int newCap;
        Validate.isTrue(minNewSize >= this.size);
        int curCap = this.keys.length;
        if (curCap >= minNewSize) {
            return;
        }
        int n2 = newCap = curCap >= 3 ? this.size * 2 : 3;
        if (minNewSize > newCap) {
            newCap = minNewSize;
        }
        this.keys = Arrays.copyOf(this.keys, newCap);
        this.vals = Arrays.copyOf(this.vals, newCap);
    }

    int indexOfKey(String key) {
        Validate.notNull(key);
        for (int i2 = 0; i2 < this.size; ++i2) {
            if (!key.equals(this.keys[i2])) continue;
            return i2;
        }
        return -1;
    }

    private int indexOfKeyIgnoreCase(String key) {
        Validate.notNull(key);
        for (int i2 = 0; i2 < this.size; ++i2) {
            if (!key.equalsIgnoreCase(this.keys[i2])) continue;
            return i2;
        }
        return -1;
    }

    static String checkNotNull(@Nullable Object val) {
        return val == null ? EmptyString : (String)val;
    }

    public String get(String key) {
        int i2 = this.indexOfKey(key);
        return i2 == -1 ? EmptyString : Attributes.checkNotNull(this.vals[i2]);
    }

    public @Nullable Attribute attribute(String key) {
        int i2 = this.indexOfKey(key);
        return i2 == -1 ? null : new Attribute(key, Attributes.checkNotNull(this.vals[i2]), this);
    }

    public String getIgnoreCase(String key) {
        int i2 = this.indexOfKeyIgnoreCase(key);
        return i2 == -1 ? EmptyString : Attributes.checkNotNull(this.vals[i2]);
    }

    public Attributes add(String key, @Nullable String value) {
        this.addObject(key, value);
        return this;
    }

    private void addObject(String key, @Nullable Object value) {
        this.checkCapacity(this.size + 1);
        this.keys[this.size] = key;
        this.vals[this.size] = value;
        ++this.size;
    }

    public Attributes put(String key, @Nullable String value) {
        Validate.notNull(key);
        int i2 = this.indexOfKey(key);
        if (i2 != -1) {
            this.vals[i2] = value;
        } else {
            this.add(key, value);
        }
        return this;
    }

    Map<String, Object> userData() {
        HashMap<String, Object> userData;
        int i2 = this.indexOfKey("/jsoup.userdata");
        if (i2 == -1) {
            userData = new HashMap();
            this.addObject("/jsoup.userdata", userData);
        } else {
            userData = (Map)this.vals[i2];
        }
        assert (userData != null);
        return userData;
    }

    public @Nullable Object userData(String key) {
        Validate.notNull(key);
        if (!this.hasKey("/jsoup.userdata")) {
            return null;
        }
        Map<String, Object> userData = this.userData();
        return userData.get(key);
    }

    public Attributes userData(String key, Object value) {
        Validate.notNull(key);
        this.userData().put(key, value);
        return this;
    }

    void putIgnoreCase(String key, @Nullable String value) {
        int i2 = this.indexOfKeyIgnoreCase(key);
        if (i2 != -1) {
            this.vals[i2] = value;
            String old = this.keys[i2];
            assert (old != null);
            if (!old.equals(key)) {
                this.keys[i2] = key;
            }
        } else {
            this.add(key, value);
        }
    }

    public Attributes put(String key, boolean value) {
        if (value) {
            this.putIgnoreCase(key, null);
        } else {
            this.remove(key);
        }
        return this;
    }

    public Attributes put(Attribute attribute) {
        Validate.notNull(attribute);
        this.put(attribute.getKey(), attribute.getValue());
        attribute.parent = this;
        return this;
    }

    private void remove(int index) {
        Validate.isFalse(index >= this.size);
        int shifted = this.size - index - 1;
        if (shifted > 0) {
            System.arraycopy(this.keys, index + 1, this.keys, index, shifted);
            System.arraycopy(this.vals, index + 1, this.vals, index, shifted);
        }
        --this.size;
        this.keys[this.size] = null;
        this.vals[this.size] = null;
    }

    public void remove(String key) {
        int i2 = this.indexOfKey(key);
        if (i2 != -1) {
            this.remove(i2);
        }
    }

    public void removeIgnoreCase(String key) {
        int i2 = this.indexOfKeyIgnoreCase(key);
        if (i2 != -1) {
            this.remove(i2);
        }
    }

    public boolean hasKey(String key) {
        return this.indexOfKey(key) != -1;
    }

    public boolean hasKeyIgnoreCase(String key) {
        return this.indexOfKeyIgnoreCase(key) != -1;
    }

    public boolean hasDeclaredValueForKey(String key) {
        int i2 = this.indexOfKey(key);
        return i2 != -1 && this.vals[i2] != null;
    }

    public boolean hasDeclaredValueForKeyIgnoreCase(String key) {
        int i2 = this.indexOfKeyIgnoreCase(key);
        return i2 != -1 && this.vals[i2] != null;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public void addAll(Attributes incoming) {
        if (incoming.size() == 0) {
            return;
        }
        this.checkCapacity(this.size + incoming.size);
        boolean needsPut = this.size != 0;
        for (Attribute attr : incoming) {
            if (needsPut) {
                this.put(attr);
                continue;
            }
            this.add(attr.getKey(), attr.getValue());
        }
    }

    public Range.AttributeRange sourceRange(String key) {
        if (!this.hasKey(key)) {
            return Range.AttributeRange.UntrackedAttr;
        }
        Map<String, Range.AttributeRange> ranges = this.getRanges();
        if (ranges == null) {
            return Range.AttributeRange.UntrackedAttr;
        }
        Range.AttributeRange range = ranges.get(key);
        return range != null ? range : Range.AttributeRange.UntrackedAttr;
    }

    @Nullable Map<String, Range.AttributeRange> getRanges() {
        return (Map)this.userData("jsoup.attrs");
    }

    public Attributes sourceRange(String key, Range.AttributeRange range) {
        Validate.notNull(key);
        Validate.notNull(range);
        Map<String, Range.AttributeRange> ranges = this.getRanges();
        if (ranges == null) {
            ranges = new HashMap<String, Range.AttributeRange>();
            this.userData("jsoup.attrs", ranges);
        }
        ranges.put(key, range);
        return this;
    }

    @Override
    public Iterator<Attribute> iterator() {
        return new Iterator<Attribute>(){
            int expectedSize;
            int i;
            {
                this.expectedSize = Attributes.this.size;
                this.i = 0;
            }

            @Override
            public boolean hasNext() {
                this.checkModified();
                while (this.i < Attributes.this.size) {
                    String key = Attributes.this.keys[this.i];
                    assert (key != null);
                    if (!Attributes.isInternalKey(key)) break;
                    ++this.i;
                }
                return this.i < Attributes.this.size;
            }

            @Override
            public Attribute next() {
                this.checkModified();
                if (this.i >= Attributes.this.size) {
                    throw new NoSuchElementException();
                }
                String key = Attributes.this.keys[this.i];
                assert (key != null);
                Attribute attr = new Attribute(key, (String)Attributes.this.vals[this.i], Attributes.this);
                ++this.i;
                return attr;
            }

            private void checkModified() {
                if (Attributes.this.size != this.expectedSize) {
                    throw new ConcurrentModificationException("Use Iterator#remove() instead to remove attributes while iterating.");
                }
            }

            @Override
            public void remove() {
                Attributes.this.remove(--this.i);
                --this.expectedSize;
            }
        };
    }

    public List<Attribute> asList() {
        ArrayList<Attribute> list = new ArrayList<Attribute>(this.size);
        for (int i2 = 0; i2 < this.size; ++i2) {
            String key = this.keys[i2];
            assert (key != null);
            if (Attributes.isInternalKey(key)) continue;
            Attribute attr = new Attribute(key, (String)this.vals[i2], this);
            list.add(attr);
        }
        return Collections.unmodifiableList(list);
    }

    public Map<String, String> dataset() {
        return new Dataset(this);
    }

    public String html() {
        StringBuilder sb = StringUtil.borrowBuilder();
        try {
            this.html(sb, new Document(EmptyString).outputSettings());
        }
        catch (IOException e2) {
            throw new SerializationException(e2);
        }
        return StringUtil.releaseBuilder(sb);
    }

    final void html(Appendable accum, Document.OutputSettings out) throws IOException {
        int sz = this.size;
        for (int i2 = 0; i2 < sz; ++i2) {
            String validated;
            String key = this.keys[i2];
            assert (key != null);
            if (Attributes.isInternalKey(key) || (validated = Attribute.getValidKey(key, out.syntax())) == null) continue;
            Attribute.htmlNoValidate(validated, (String)this.vals[i2], accum.append(' '), out);
        }
    }

    public String toString() {
        return this.html();
    }

    public boolean equals(@Nullable Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        Attributes that = (Attributes)o2;
        if (this.size != that.size) {
            return false;
        }
        for (int i2 = 0; i2 < this.size; ++i2) {
            String key = this.keys[i2];
            assert (key != null);
            int thatI = that.indexOfKey(key);
            if (thatI != -1 && Objects.equals(this.vals[i2], that.vals[thatI])) continue;
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.size;
        result = 31 * result + Arrays.hashCode(this.keys);
        result = 31 * result + Arrays.hashCode(this.vals);
        return result;
    }

    public Attributes clone() {
        Attributes clone;
        try {
            clone = (Attributes)super.clone();
        }
        catch (CloneNotSupportedException e2) {
            throw new RuntimeException(e2);
        }
        clone.size = this.size;
        clone.keys = Arrays.copyOf(this.keys, this.size);
        clone.vals = Arrays.copyOf(this.vals, this.size);
        return clone;
    }

    public void normalize() {
        for (int i2 = 0; i2 < this.size; ++i2) {
            assert (this.keys[i2] != null);
            String key = this.keys[i2];
            assert (key != null);
            if (Attributes.isInternalKey(key)) continue;
            this.keys[i2] = Normalizer.lowerCase(key);
        }
    }

    public int deduplicate(ParseSettings settings) {
        if (this.isEmpty()) {
            return 0;
        }
        boolean preserve = settings.preserveAttributeCase();
        int dupes = 0;
        for (int i2 = 0; i2 < this.size; ++i2) {
            String keyI = this.keys[i2];
            assert (keyI != null);
            for (int j2 = i2 + 1; j2 < this.size; ++j2) {
                if ((!preserve || !keyI.equals(this.keys[j2])) && (preserve || !keyI.equalsIgnoreCase(this.keys[j2]))) continue;
                ++dupes;
                this.remove(j2);
                --j2;
            }
        }
        return dupes;
    }

    private static String dataKey(String key) {
        return dataPrefix + key;
    }

    static String internalKey(String key) {
        return '/' + key;
    }

    static boolean isInternalKey(String key) {
        return key.length() > 1 && key.charAt(0) == '/';
    }

    private static class Dataset
    extends AbstractMap<String, String> {
        private final Attributes attributes;

        private Dataset(Attributes attributes) {
            this.attributes = attributes;
        }

        @Override
        public Set<Map.Entry<String, String>> entrySet() {
            return new EntrySet();
        }

        @Override
        public String put(String key, String value) {
            String dataKey = Attributes.dataKey(key);
            String oldValue = this.attributes.hasKey(dataKey) ? this.attributes.get(dataKey) : null;
            this.attributes.put(dataKey, value);
            return oldValue;
        }

        private class EntrySet
        extends AbstractSet<Map.Entry<String, String>> {
            private EntrySet() {
            }

            @Override
            public Iterator<Map.Entry<String, String>> iterator() {
                return new DatasetIterator();
            }

            @Override
            public int size() {
                int count = 0;
                DatasetIterator iter = new DatasetIterator();
                while (iter.hasNext()) {
                    ++count;
                }
                return count;
            }
        }

        private class DatasetIterator
        implements Iterator<Map.Entry<String, String>> {
            private final Iterator<Attribute> attrIter;
            private Attribute attr;

            private DatasetIterator() {
                this.attrIter = Dataset.this.attributes.iterator();
            }

            @Override
            public boolean hasNext() {
                while (this.attrIter.hasNext()) {
                    this.attr = this.attrIter.next();
                    if (!this.attr.isDataAttribute()) continue;
                    return true;
                }
                return false;
            }

            @Override
            public Map.Entry<String, String> next() {
                return new Attribute(this.attr.getKey().substring(Attributes.dataPrefix.length()), this.attr.getValue());
            }

            @Override
            public void remove() {
                Dataset.this.attributes.remove(this.attr.getKey());
            }
        }
    }
}

