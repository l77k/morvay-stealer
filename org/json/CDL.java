/*
 * Decompiled with CFR 0.152.
 */
package org.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class CDL {
    private static String getValue(JSONTokener x2, char delimiter) throws JSONException {
        char c2;
        while ((c2 = x2.next()) == ' ' || c2 == '\t') {
        }
        if (c2 == '\u0000') {
            return null;
        }
        if (c2 == '\"' || c2 == '\'') {
            char q2 = c2;
            StringBuilder sb = new StringBuilder();
            while (true) {
                char nextC;
                if ((c2 = x2.next()) == q2 && (nextC = x2.next()) != '\"') {
                    if (nextC <= '\u0000') break;
                    x2.back();
                    break;
                }
                if (c2 == '\u0000' || c2 == '\n' || c2 == '\r') {
                    throw x2.syntaxError("Missing close quote '" + q2 + "'.");
                }
                sb.append(c2);
            }
            return sb.toString();
        }
        if (c2 == delimiter) {
            x2.back();
            return "";
        }
        x2.back();
        return x2.nextTo(delimiter);
    }

    public static JSONArray rowToJSONArray(JSONTokener x2) throws JSONException {
        return CDL.rowToJSONArray(x2, ',');
    }

    public static JSONArray rowToJSONArray(JSONTokener x2, char delimiter) throws JSONException {
        JSONArray ja = new JSONArray();
        block0: while (true) {
            String value = CDL.getValue(x2, delimiter);
            char c2 = x2.next();
            if (value == null || ja.length() == 0 && value.length() == 0 && c2 != delimiter) {
                return null;
            }
            ja.put(value);
            while (true) {
                if (c2 == delimiter) continue block0;
                if (c2 != ' ') {
                    if (c2 == '\n' || c2 == '\r' || c2 == '\u0000') {
                        return ja;
                    }
                    throw x2.syntaxError("Bad character '" + c2 + "' (" + c2 + ").");
                }
                c2 = x2.next();
            }
            break;
        }
    }

    public static JSONObject rowToJSONObject(JSONArray names, JSONTokener x2) throws JSONException {
        return CDL.rowToJSONObject(names, x2, ',');
    }

    public static JSONObject rowToJSONObject(JSONArray names, JSONTokener x2, char delimiter) throws JSONException {
        JSONArray ja = CDL.rowToJSONArray(x2, delimiter);
        return ja != null ? ja.toJSONObject(names) : null;
    }

    public static String rowToString(JSONArray ja) {
        return CDL.rowToString(ja, ',');
    }

    public static String rowToString(JSONArray ja, char delimiter) {
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < ja.length(); ++i2) {
            Object object;
            if (i2 > 0) {
                sb.append(delimiter);
            }
            if ((object = ja.opt(i2)) == null) continue;
            String string = object.toString();
            if (string.length() > 0 && (string.indexOf(delimiter) >= 0 || string.indexOf(10) >= 0 || string.indexOf(13) >= 0 || string.indexOf(0) >= 0 || string.charAt(0) == '\"')) {
                sb.append('\"');
                int length = string.length();
                for (int j2 = 0; j2 < length; ++j2) {
                    char c2 = string.charAt(j2);
                    if (c2 < ' ' || c2 == '\"') continue;
                    sb.append(c2);
                }
                sb.append('\"');
                continue;
            }
            sb.append(string);
        }
        sb.append('\n');
        return sb.toString();
    }

    public static JSONArray toJSONArray(String string) throws JSONException {
        return CDL.toJSONArray(string, ',');
    }

    public static JSONArray toJSONArray(String string, char delimiter) throws JSONException {
        return CDL.toJSONArray(new JSONTokener(string), delimiter);
    }

    public static JSONArray toJSONArray(JSONTokener x2) throws JSONException {
        return CDL.toJSONArray(x2, ',');
    }

    public static JSONArray toJSONArray(JSONTokener x2, char delimiter) throws JSONException {
        return CDL.toJSONArray(CDL.rowToJSONArray(x2, delimiter), x2, delimiter);
    }

    public static JSONArray toJSONArray(JSONArray names, String string) throws JSONException {
        return CDL.toJSONArray(names, string, ',');
    }

    public static JSONArray toJSONArray(JSONArray names, String string, char delimiter) throws JSONException {
        return CDL.toJSONArray(names, new JSONTokener(string), delimiter);
    }

    public static JSONArray toJSONArray(JSONArray names, JSONTokener x2) throws JSONException {
        return CDL.toJSONArray(names, x2, ',');
    }

    public static JSONArray toJSONArray(JSONArray names, JSONTokener x2, char delimiter) throws JSONException {
        JSONObject jo;
        if (names == null || names.length() == 0) {
            return null;
        }
        JSONArray ja = new JSONArray();
        while ((jo = CDL.rowToJSONObject(names, x2, delimiter)) != null) {
            ja.put(jo);
        }
        if (ja.length() == 0) {
            return null;
        }
        return ja;
    }

    public static String toString(JSONArray ja) throws JSONException {
        return CDL.toString(ja, ',');
    }

    public static String toString(JSONArray ja, char delimiter) throws JSONException {
        JSONArray names;
        JSONObject jo = ja.optJSONObject(0);
        if (jo != null && (names = jo.names()) != null) {
            return CDL.rowToString(names, delimiter) + CDL.toString(names, ja, delimiter);
        }
        return null;
    }

    public static String toString(JSONArray names, JSONArray ja) throws JSONException {
        return CDL.toString(names, ja, ',');
    }

    public static String toString(JSONArray names, JSONArray ja, char delimiter) throws JSONException {
        if (names == null || names.length() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < ja.length(); ++i2) {
            JSONObject jo = ja.optJSONObject(i2);
            if (jo == null) continue;
            sb.append(CDL.rowToString(jo.toJSONArray(names), delimiter));
        }
        return sb.toString();
    }
}

