/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberLimits {
    private static final int MAX_NUMBER_STRING_LENGTH = 10000;

    private NumberLimits() {
    }

    private static void checkNumberStringLength(String s2) {
        if (s2.length() > 10000) {
            throw new NumberFormatException("Number string too large: " + s2.substring(0, 30) + "...");
        }
    }

    public static BigDecimal parseBigDecimal(String s2) throws NumberFormatException {
        NumberLimits.checkNumberStringLength(s2);
        BigDecimal decimal = new BigDecimal(s2);
        if (Math.abs((long)decimal.scale()) >= 10000L) {
            throw new NumberFormatException("Number has unsupported scale: " + s2);
        }
        return decimal;
    }

    public static BigInteger parseBigInteger(String s2) throws NumberFormatException {
        NumberLimits.checkNumberStringLength(s2);
        return new BigInteger(s2);
    }
}

