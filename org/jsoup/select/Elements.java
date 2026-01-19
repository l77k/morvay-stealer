/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package org.jsoup.select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Evaluator;
import org.jsoup.select.NodeFilter;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Selector;
import org.jspecify.annotations.Nullable;

public class Elements
extends ArrayList<Element> {
    public Elements() {
    }

    public Elements(int initialCapacity) {
        super(initialCapacity);
    }

    public Elements(Collection<Element> elements) {
        super(elements);
    }

    public Elements(List<Element> elements) {
        super(elements);
    }

    public Elements(Element ... elements) {
        super(Arrays.asList(elements));
    }

    @Override
    public Elements clone() {
        Elements clone = new Elements(this.size());
        for (Element e2 : this) {
            clone.add(e2.clone());
        }
        return clone;
    }

    public String attr(String attributeKey) {
        for (Element element : this) {
            if (!element.hasAttr(attributeKey)) continue;
            return element.attr(attributeKey);
        }
        return "";
    }

    public boolean hasAttr(String attributeKey) {
        for (Element element : this) {
            if (!element.hasAttr(attributeKey)) continue;
            return true;
        }
        return false;
    }

    public List<String> eachAttr(String attributeKey) {
        ArrayList<String> attrs = new ArrayList<String>(this.size());
        for (Element element : this) {
            if (!element.hasAttr(attributeKey)) continue;
            attrs.add(element.attr(attributeKey));
        }
        return attrs;
    }

    public Elements attr(String attributeKey, String attributeValue) {
        for (Element element : this) {
            element.attr(attributeKey, attributeValue);
        }
        return this;
    }

    public Elements removeAttr(String attributeKey) {
        for (Element element : this) {
            element.removeAttr(attributeKey);
        }
        return this;
    }

    public Elements addClass(String className) {
        for (Element element : this) {
            element.addClass(className);
        }
        return this;
    }

    public Elements removeClass(String className) {
        for (Element element : this) {
            element.removeClass(className);
        }
        return this;
    }

    public Elements toggleClass(String className) {
        for (Element element : this) {
            element.toggleClass(className);
        }
        return this;
    }

    public boolean hasClass(String className) {
        for (Element element : this) {
            if (!element.hasClass(className)) continue;
            return true;
        }
        return false;
    }

    public String val() {
        if (this.size() > 0) {
            return this.first().val();
        }
        return "";
    }

    public Elements val(String value) {
        for (Element element : this) {
            element.val(value);
        }
        return this;
    }

    public String text() {
        return this.stream().map(Element::text).collect(StringUtil.joining(" "));
    }

    public boolean hasText() {
        for (Element element : this) {
            if (!element.hasText()) continue;
            return true;
        }
        return false;
    }

    public List<String> eachText() {
        ArrayList<String> texts = new ArrayList<String>(this.size());
        for (Element el : this) {
            if (!el.hasText()) continue;
            texts.add(el.text());
        }
        return texts;
    }

    public String html() {
        return this.stream().map(Element::html).collect(StringUtil.joining("\n"));
    }

    public String outerHtml() {
        return this.stream().map(Node::outerHtml).collect(StringUtil.joining("\n"));
    }

    @Override
    public String toString() {
        return this.outerHtml();
    }

    public Elements tagName(String tagName) {
        for (Element element : this) {
            element.tagName(tagName);
        }
        return this;
    }

    public Elements html(String html) {
        for (Element element : this) {
            element.html(html);
        }
        return this;
    }

    public Elements prepend(String html) {
        for (Element element : this) {
            element.prepend(html);
        }
        return this;
    }

    public Elements append(String html) {
        for (Element element : this) {
            element.append(html);
        }
        return this;
    }

    public Elements before(String html) {
        for (Element element : this) {
            element.before(html);
        }
        return this;
    }

    public Elements after(String html) {
        for (Element element : this) {
            element.after(html);
        }
        return this;
    }

    public Elements wrap(String html) {
        Validate.notEmpty(html);
        for (Element element : this) {
            element.wrap(html);
        }
        return this;
    }

    public Elements unwrap() {
        for (Element element : this) {
            element.unwrap();
        }
        return this;
    }

    public Elements empty() {
        for (Element element : this) {
            element.empty();
        }
        return this;
    }

    public Elements remove() {
        for (Element element : this) {
            element.remove();
        }
        return this;
    }

    public Elements select(String query) {
        return Selector.select(query, this);
    }

    public @Nullable Element selectFirst(String cssQuery) {
        return Selector.selectFirst(cssQuery, this);
    }

    public Element expectFirst(String cssQuery) {
        return (Element)Validate.ensureNotNull(Selector.selectFirst(cssQuery, this), "No elements matched the query '%s' in the elements.", cssQuery);
    }

    public Elements not(String query) {
        Elements out = Selector.select(query, this);
        return Selector.filterOut(this, out);
    }

    public Elements eq(int index) {
        return this.size() > index ? new Elements((Element)this.get(index)) : new Elements();
    }

    public boolean is(String query) {
        Evaluator eval = QueryParser.parse(query);
        for (Element e2 : this) {
            if (!e2.is(eval)) continue;
            return true;
        }
        return false;
    }

    public Elements next() {
        return this.siblings(null, true, false);
    }

    public Elements next(String query) {
        return this.siblings(query, true, false);
    }

    public Elements nextAll() {
        return this.siblings(null, true, true);
    }

    public Elements nextAll(String query) {
        return this.siblings(query, true, true);
    }

    public Elements prev() {
        return this.siblings(null, false, false);
    }

    public Elements prev(String query) {
        return this.siblings(query, false, false);
    }

    public Elements prevAll() {
        return this.siblings(null, false, true);
    }

    public Elements prevAll(String query) {
        return this.siblings(query, false, true);
    }

    private Elements siblings(@Nullable String query, boolean next, boolean all) {
        Elements els = new Elements();
        Evaluator eval = query != null ? QueryParser.parse(query) : null;
        block0: for (Element e2 : this) {
            Element sib;
            while ((sib = next ? e2.nextElementSibling() : e2.previousElementSibling()) != null) {
                if (eval == null || sib.is(eval)) {
                    els.add(sib);
                }
                e2 = sib;
                if (all) continue;
                continue block0;
            }
        }
        return els;
    }

    public Elements parents() {
        LinkedHashSet<Element> combo = new LinkedHashSet<Element>();
        for (Element e2 : this) {
            combo.addAll(e2.parents());
        }
        return new Elements((Collection<Element>)combo);
    }

    public @Nullable Element first() {
        return this.isEmpty() ? null : (Element)this.get(0);
    }

    public @Nullable Element last() {
        return this.isEmpty() ? null : (Element)this.get(this.size() - 1);
    }

    public Elements traverse(NodeVisitor nodeVisitor) {
        NodeTraversor.traverse(nodeVisitor, this);
        return this;
    }

    public Elements filter(NodeFilter nodeFilter) {
        NodeTraversor.filter(nodeFilter, this);
        return this;
    }

    public List<FormElement> forms() {
        ArrayList<FormElement> forms = new ArrayList<FormElement>();
        for (Element el : this) {
            if (!(el instanceof FormElement)) continue;
            forms.add((FormElement)el);
        }
        return forms;
    }

    public List<Comment> comments() {
        return this.childNodesOfType(Comment.class);
    }

    public List<TextNode> textNodes() {
        return this.childNodesOfType(TextNode.class);
    }

    public List<DataNode> dataNodes() {
        return this.childNodesOfType(DataNode.class);
    }

    private <T extends Node> List<T> childNodesOfType(Class<T> tClass) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        for (Element el : this) {
            for (int i2 = 0; i2 < el.childNodeSize(); ++i2) {
                Node node = el.childNode(i2);
                if (!tClass.isInstance(node)) continue;
                nodes.add((Node)tClass.cast(node));
            }
        }
        return nodes;
    }

    @Override
    public Element set(int index, Element element) {
        Validate.notNull(element);
        Element old = super.set(index, element);
        old.replaceWith(element);
        return old;
    }

    @Override
    public Element remove(int index) {
        Element old = (Element)super.remove(index);
        old.remove();
        return old;
    }

    @Override
    public boolean remove(Object o2) {
        int index = super.indexOf(o2);
        if (index == -1) {
            return false;
        }
        this.remove(index);
        return true;
    }

    @Override
    public void clear() {
        this.remove();
        super.clear();
    }

    @Override
    public boolean removeAll(Collection<?> c2) {
        boolean anyRemoved = false;
        for (Object o2 : c2) {
            anyRemoved |= this.remove(o2);
        }
        return anyRemoved;
    }

    @Override
    public boolean retainAll(Collection<?> c2) {
        boolean anyRemoved = false;
        Iterator it = this.iterator();
        while (it.hasNext()) {
            Element el = (Element)it.next();
            if (c2.contains(el)) continue;
            it.remove();
            anyRemoved = true;
        }
        return anyRemoved;
    }

    @Override
    public boolean removeIf(Predicate<? super Element> filter) {
        boolean anyRemoved = false;
        Iterator it = this.iterator();
        while (it.hasNext()) {
            Element el = (Element)it.next();
            if (!filter.test(el)) continue;
            it.remove();
            anyRemoved = true;
        }
        return anyRemoved;
    }

    @Override
    public void replaceAll(UnaryOperator<Element> operator) {
        for (int i2 = 0; i2 < this.size(); ++i2) {
            this.set(i2, (Element)operator.apply((Element)this.get(i2)));
        }
    }
}

