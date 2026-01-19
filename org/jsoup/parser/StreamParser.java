/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package org.jsoup.parser;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.select.Evaluator;
import org.jsoup.select.NodeVisitor;
import org.jsoup.select.QueryParser;
import org.jspecify.annotations.Nullable;

public class StreamParser
implements Closeable {
    private final Parser parser;
    private final TreeBuilder treeBuilder;
    private final ElementIterator it = new ElementIterator();
    private @Nullable Document document;
    private boolean stopped = false;

    public StreamParser(Parser parser) {
        this.parser = parser;
        this.treeBuilder = parser.getTreeBuilder();
        this.treeBuilder.nodeListener(this.it);
    }

    public StreamParser parse(Reader input, String baseUri) {
        this.close();
        this.it.reset();
        this.treeBuilder.initialiseParse(input, baseUri, this.parser);
        this.document = this.treeBuilder.doc;
        return this;
    }

    public StreamParser parse(String input, String baseUri) {
        return this.parse(new StringReader(input), baseUri);
    }

    public StreamParser parseFragment(Reader input, @Nullable Element context, String baseUri) {
        this.parse(input, baseUri);
        this.treeBuilder.initialiseParseFragment(context);
        return this;
    }

    public StreamParser parseFragment(String input, @Nullable Element context, String baseUri) {
        return this.parseFragment(new StringReader(input), context, baseUri);
    }

    public Stream<Element> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this.it, 273), false);
    }

    public Iterator<Element> iterator() {
        return this.it;
    }

    public StreamParser stop() {
        this.stopped = true;
        return this;
    }

    @Override
    public void close() {
        this.treeBuilder.completeParse();
    }

    public Document document() {
        this.document = this.treeBuilder.doc;
        Validate.notNull(this.document, "Must run parse() before calling.");
        return this.document;
    }

    public Document complete() throws IOException {
        Document doc = this.document();
        this.treeBuilder.runParser();
        return doc;
    }

    public List<Node> completeFragment() throws IOException {
        this.treeBuilder.runParser();
        return this.treeBuilder.completeParseFragment();
    }

    public @Nullable Element selectFirst(String query) throws IOException {
        return this.selectFirst(QueryParser.parse(query));
    }

    public Element expectFirst(String query) throws IOException {
        return (Element)Validate.ensureNotNull(this.selectFirst(query), "No elements matched the query '%s' in the document.", query);
    }

    public @Nullable Element selectFirst(Evaluator eval) throws IOException {
        Document doc = this.document();
        Element first = doc.selectFirst(eval);
        if (first != null) {
            return first;
        }
        return this.selectNext(eval);
    }

    public @Nullable Element selectNext(String query) throws IOException {
        return this.selectNext(QueryParser.parse(query));
    }

    public Element expectNext(String query) throws IOException {
        return (Element)Validate.ensureNotNull(this.selectNext(query), "No elements matched the query '%s' in the document.", query);
    }

    public @Nullable Element selectNext(Evaluator eval) throws IOException {
        try {
            Document doc = this.document();
            return this.stream().filter(eval.asPredicate(doc)).findFirst().orElse(null);
        }
        catch (UncheckedIOException e2) {
            throw e2.getCause();
        }
    }

    final class ElementIterator
    implements Iterator<Element>,
    NodeVisitor {
        private final Queue<Element> emitQueue = new LinkedList<Element>();
        private @Nullable Element current;
        private @Nullable Element next;
        private @Nullable Element tail;

        ElementIterator() {
        }

        void reset() {
            this.emitQueue.clear();
            this.tail = null;
            this.next = null;
            this.current = null;
            StreamParser.this.stopped = false;
        }

        @Override
        public boolean hasNext() {
            this.maybeFindNext();
            return this.next != null;
        }

        @Override
        public Element next() {
            this.maybeFindNext();
            if (this.next == null) {
                throw new NoSuchElementException();
            }
            this.current = this.next;
            this.next = null;
            return this.current;
        }

        private void maybeFindNext() {
            if (StreamParser.this.stopped || this.next != null) {
                return;
            }
            if (!this.emitQueue.isEmpty()) {
                this.next = this.emitQueue.remove();
                return;
            }
            while (StreamParser.this.treeBuilder.stepParser()) {
                if (this.emitQueue.isEmpty()) continue;
                this.next = this.emitQueue.remove();
                return;
            }
            StreamParser.this.stop();
            StreamParser.this.close();
            if (this.tail != null) {
                this.next = this.tail;
                this.tail = null;
            }
        }

        @Override
        public void remove() {
            if (this.current == null) {
                throw new NoSuchElementException();
            }
            this.current.remove();
        }

        @Override
        public void head(Node node, int depth) {
            Element prev;
            if (node instanceof Element && (prev = ((Element)node).previousElementSibling()) != null) {
                this.emitQueue.add(prev);
            }
        }

        @Override
        public void tail(Node node, int depth) {
            if (node instanceof Element) {
                this.tail = (Element)node;
                Element lastChild = this.tail.lastElementChild();
                if (lastChild != null) {
                    this.emitQueue.add(lastChild);
                }
            }
        }
    }
}

