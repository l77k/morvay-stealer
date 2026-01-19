/*
 * Decompiled with CFR 0.152.
 */
package org.jsoup.safety;

import java.util.List;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Safelist;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public class Cleaner {
    private final Safelist safelist;

    public Cleaner(Safelist safelist) {
        Validate.notNull(safelist);
        this.safelist = safelist;
    }

    public Document clean(Document dirtyDocument) {
        Validate.notNull(dirtyDocument);
        Document clean = Document.createShell(dirtyDocument.baseUri());
        this.copySafeNodes(dirtyDocument.body(), clean.body());
        clean.outputSettings(dirtyDocument.outputSettings().clone());
        return clean;
    }

    public boolean isValid(Document dirtyDocument) {
        Validate.notNull(dirtyDocument);
        Document clean = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = this.copySafeNodes(dirtyDocument.body(), clean.body());
        return numDiscarded == 0 && dirtyDocument.head().childNodes().isEmpty();
    }

    public boolean isValidBodyHtml(String bodyHtml) {
        String baseUri = this.safelist.preserveRelativeLinks() ? "https://dummy.example/" : "";
        Document clean = Document.createShell(baseUri);
        Document dirty = Document.createShell(baseUri);
        ParseErrorList errorList = ParseErrorList.tracking(1);
        List<Node> nodes = Parser.parseFragment(bodyHtml, dirty.body(), baseUri, errorList);
        dirty.body().insertChildren(0, nodes);
        int numDiscarded = this.copySafeNodes(dirty.body(), clean.body());
        return numDiscarded == 0 && errorList.isEmpty();
    }

    private int copySafeNodes(Element source2, Element dest) {
        CleaningVisitor cleaningVisitor = new CleaningVisitor(source2, dest);
        NodeTraversor.traverse((NodeVisitor)cleaningVisitor, source2);
        return cleaningVisitor.numDiscarded;
    }

    private ElementMeta createSafeElement(Element sourceEl) {
        Element dest = sourceEl.shallowClone();
        String sourceTag = sourceEl.tagName();
        Attributes destAttrs = dest.attributes();
        dest.clearAttributes();
        int numDiscarded = 0;
        Attributes sourceAttrs = sourceEl.attributes();
        for (Attribute sourceAttr : sourceAttrs) {
            if (this.safelist.isSafeAttribute(sourceTag, sourceEl, sourceAttr)) {
                destAttrs.put(sourceAttr);
                continue;
            }
            ++numDiscarded;
        }
        Attributes enforcedAttrs = this.safelist.getEnforcedAttributes(sourceTag);
        if (sourceEl.nameIs("a") && enforcedAttrs.get("rel").equals("nofollow")) {
            String href = sourceEl.absUrl("href");
            String sourceBase = sourceEl.baseUri();
            if (!href.isEmpty() && !sourceBase.isEmpty() && href.startsWith(sourceBase)) {
                enforcedAttrs.remove("rel");
            }
        }
        destAttrs.addAll(enforcedAttrs);
        dest.attributes().addAll(destAttrs);
        return new ElementMeta(dest, numDiscarded);
    }

    private static class ElementMeta {
        Element el;
        int numAttribsDiscarded;

        ElementMeta(Element el, int numAttribsDiscarded) {
            this.el = el;
            this.numAttribsDiscarded = numAttribsDiscarded;
        }
    }

    private final class CleaningVisitor
    implements NodeVisitor {
        private int numDiscarded = 0;
        private final Element root;
        private Element destination;

        private CleaningVisitor(Element root, Element destination) {
            this.root = root;
            this.destination = destination;
        }

        @Override
        public void head(Node source2, int depth) {
            if (source2 instanceof Element) {
                Element sourceEl = (Element)source2;
                if (Cleaner.this.safelist.isSafeTag(sourceEl.normalName())) {
                    ElementMeta meta = Cleaner.this.createSafeElement(sourceEl);
                    Element destChild = meta.el;
                    this.destination.appendChild(destChild);
                    this.numDiscarded += meta.numAttribsDiscarded;
                    this.destination = destChild;
                } else if (source2 != this.root) {
                    ++this.numDiscarded;
                }
            } else if (source2 instanceof TextNode) {
                TextNode sourceText = (TextNode)source2;
                TextNode destText = new TextNode(sourceText.getWholeText());
                this.destination.appendChild(destText);
            } else if (source2 instanceof DataNode && Cleaner.this.safelist.isSafeTag(source2.parent().normalName())) {
                DataNode sourceData = (DataNode)source2;
                DataNode destData = new DataNode(sourceData.getWholeData());
                this.destination.appendChild(destData);
            } else {
                ++this.numDiscarded;
            }
        }

        @Override
        public void tail(Node source2, int depth) {
            if (source2 instanceof Element && Cleaner.this.safelist.isSafeTag(source2.normalName())) {
                this.destination = this.destination.parent();
            }
        }
    }
}

