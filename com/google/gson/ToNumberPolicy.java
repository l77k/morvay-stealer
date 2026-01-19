/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson;

import com.google.gson.JsonParseException;
import com.google.gson.ToNumberStrategy;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.NumberLimits;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.math.BigDecimal;

public enum ToNumberPolicy implements ToNumberStrategy
{
    DOUBLE{

        @Override
        public Double readNumber(JsonReader in) throws IOException {
            return in.nextDouble();
        }
    }
    ,
    LAZILY_PARSED_NUMBER{

        @Override
        public Number readNumber(JsonReader in) throws IOException {
            return new LazilyParsedNumber(in.nextString());
        }
    }
    ,
    LONG_OR_DOUBLE{

        @Override
        public Number readNumber(JsonReader in) throws IOException, JsonParseException {
            String value = in.nextString();
            if (value.indexOf(46) >= 0) {
                return this.parseAsDouble(value, in);
            }
            try {
                return Long.parseLong(value);
            }
            catch (NumberFormatException e2) {
                return this.parseAsDouble(value, in);
            }
        }

        private Number parseAsDouble(String value, JsonReader in) throws IOException {
            try {
                Double d2 = Double.valueOf(value);
                if ((d2.isInfinite() || d2.isNaN()) && !in.isLenient()) {
                    throw new MalformedJsonException("JSON forbids NaN and infinities: " + d2 + "; at path " + in.getPreviousPath());
                }
                return d2;
            }
            catch (NumberFormatException e2) {
                throw new JsonParseException("Cannot parse " + value + "; at path " + in.getPreviousPath(), e2);
            }
        }
    }
    ,
    BIG_DECIMAL{

        @Override
        public BigDecimal readNumber(JsonReader in) throws IOException {
            String value = in.nextString();
            try {
                return NumberLimits.parseBigDecimal(value);
            }
            catch (NumberFormatException e2) {
                throw new JsonParseException("Cannot parse " + value + "; at path " + in.getPreviousPath(), e2);
            }
        }
    };

}

