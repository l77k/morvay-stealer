/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.util;

import java.util.List;
import java.util.stream.Collectors;

public class QueryUtils {
    public static String valuesQuery(List<String> columns, List<List<Object>> valuesList) {
        valuesList.forEach(list -> {
            if (list.size() != columns.size()) {
                throw new IllegalArgumentException("values and columns must have the same size");
            }
        });
        return "with cte(" + String.join((CharSequence)",", columns) + ") as (values " + valuesList.stream().map(values2 -> "(" + values2.stream().map(o2 -> {
            if (o2 instanceof String) {
                return "'" + o2 + "'";
            }
            if (o2 == null) {
                return "null";
            }
            return o2.toString();
        }).collect(Collectors.joining(",")) + ")").collect(Collectors.joining(",")) + ") select * from cte";
    }
}

