/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package org.jsoup.select;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Stream;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.select.Collector;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;
import org.jspecify.annotations.Nullable;

public class Selector {
    private Selector() {
    }

    public static Elements select(String query, Element root) {
        Validate.notEmpty(query);
        return Selector.select(QueryParser.parse(query), root);
    }

    public static Elements select(Evaluator evaluator, Element root) {
        Validate.notNull(evaluator);
        Validate.notNull(root);
        return Collector.collect(evaluator, root);
    }

    public static Stream<Element> selectStream(String query, Element root) {
        Validate.notEmpty(query);
        return Selector.selectStream(QueryParser.parse(query), root);
    }

    public static Stream<Element> selectStream(Evaluator evaluator, Element root) {
        Validate.notNull(evaluator);
        Validate.notNull(root);
        return Collector.stream(evaluator, root);
    }

    public static Elements select(String query, Iterable<Element> roots) {
        Validate.notEmpty(query);
        Validate.notNull(roots);
        Evaluator evaluator = QueryParser.parse(query);
        Elements elements = new Elements();
        HashSet seenElements = new HashSet();
        for (Element root : roots) {
            Selector.selectStream(evaluator, root).filter(seenElements::add).forEach(elements::add);
        }
        return elements;
    }

    static Elements filterOut(Collection<Element> elements, Collection<Element> outs) {
        Elements output = new Elements();
        for (Element el : elements) {
            boolean found = false;
            for (Element out : outs) {
                if (!el.equals(out)) continue;
                found = true;
                break;
            }
            if (found) continue;
            output.add(el);
        }
        return output;
    }

    public static @Nullable Element selectFirst(String cssQuery, Element root) {
        Validate.notEmpty(cssQuery);
        return Collector.findFirst(QueryParser.parse(cssQuery), root);
    }

    public static @Nullable Element selectFirst(String cssQuery, Iterable<Element> roots) {
        Validate.notEmpty(cssQuery);
        Validate.notNull(roots);
        Evaluator evaluator = QueryParser.parse(cssQuery);
        for (Element root : roots) {
            Element first = Collector.findFirst(evaluator, root);
            if (first == null) continue;
            return first;
        }
        return null;
    }

    public static class SelectorParseException
    extends IllegalStateException {
        public SelectorParseException(String msg) {
            super(msg);
        }

        public SelectorParseException(String msg, Object ... msgArgs) {
            super(String.format(msg, msgArgs));
        }

        public SelectorParseException(Throwable cause, String msg, Object ... msgArgs) {
            super(String.format(msg, msgArgs), cause);
        }
    }
}

