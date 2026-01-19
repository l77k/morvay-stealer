/*
 * Decompiled with CFR 0.152.
 */
package org.jsoup.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class Functions {
    private static final Function ListFunction = key -> new ArrayList();
    private static final Function SetFunction = key -> new HashSet();
    private static final Function MapFunction = key -> new HashMap();
    private static final Function IdentityMapFunction = key -> new IdentityHashMap();

    private Functions() {
    }

    public static <T, U> Function<T, List<U>> listFunction() {
        return ListFunction;
    }

    public static <T, U> Function<T, Set<U>> setFunction() {
        return SetFunction;
    }

    public static <T, K, V> Function<T, Map<K, V>> mapFunction() {
        return MapFunction;
    }

    public static <T, K, V> Function<T, IdentityHashMap<K, V>> identityMapFunction() {
        return IdentityMapFunction;
    }
}

