/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.util;

import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.crypto.AlphabetMapper;

public class BasicAlphabetMapper
implements AlphabetMapper {
    private Map<Character, Integer> indexMap = new HashMap<Character, Integer>();
    private Map<Integer, Character> charMap = new HashMap<Integer, Character>();

    public BasicAlphabetMapper(String string) {
        this(string.toCharArray());
    }

    public BasicAlphabetMapper(char[] cArray) {
        for (int i2 = 0; i2 != cArray.length; ++i2) {
            if (this.indexMap.containsKey(Character.valueOf(cArray[i2]))) {
                throw new IllegalArgumentException("duplicate key detected in alphabet: " + cArray[i2]);
            }
            this.indexMap.put(Character.valueOf(cArray[i2]), i2);
            this.charMap.put(i2, Character.valueOf(cArray[i2]));
        }
    }

    @Override
    public int getRadix() {
        return this.indexMap.size();
    }

    @Override
    public byte[] convertToIndexes(char[] cArray) {
        byte[] byArray;
        if (this.indexMap.size() <= 256) {
            byArray = new byte[cArray.length];
            for (int i2 = 0; i2 != cArray.length; ++i2) {
                byArray[i2] = this.indexMap.get(Character.valueOf(cArray[i2])).byteValue();
            }
        } else {
            byArray = new byte[cArray.length * 2];
            for (int i3 = 0; i3 != cArray.length; ++i3) {
                int n2 = this.indexMap.get(Character.valueOf(cArray[i3]));
                byArray[i3 * 2] = (byte)(n2 >> 8 & 0xFF);
                byArray[i3 * 2 + 1] = (byte)(n2 & 0xFF);
            }
        }
        return byArray;
    }

    @Override
    public char[] convertToChars(byte[] byArray) {
        char[] cArray;
        if (this.charMap.size() <= 256) {
            cArray = new char[byArray.length];
            for (int i2 = 0; i2 != byArray.length; ++i2) {
                cArray[i2] = this.charMap.get(byArray[i2] & 0xFF).charValue();
            }
        } else {
            if ((byArray.length & 1) != 0) {
                throw new IllegalArgumentException("two byte radix and input string odd length");
            }
            cArray = new char[byArray.length / 2];
            for (int i3 = 0; i3 != byArray.length; i3 += 2) {
                cArray[i3 / 2] = this.charMap.get(byArray[i3] << 8 & 0xFF00 | byArray[i3 + 1] & 0xFF).charValue();
            }
        }
        return cArray;
    }
}

