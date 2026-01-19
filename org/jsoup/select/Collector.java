/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package org.jsoup.select;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.jspecify.annotations.Nullable;

public class Collector {
    private Collector() {
    }

    public static Elements collect(Evaluator eval, Element root) {
        return Collector.stream(eval, root).collect(Collectors.toCollection(Elements::new));
    }

    public static Stream<Element> stream(Evaluator evaluator, Element root) {
        evaluator.reset();
        return root.stream().filter(evaluator.asPredicate(root));
    }

    public static @Nullable Element findFirst(Evaluator eval, Element root) {
        return Collector.stream(eval, root).findFirst().orElse(null);
    }
}

