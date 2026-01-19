/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package org.jsoup.nodes;

import java.nio.charset.Charset;
import java.util.List;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.jspecify.annotations.Nullable;

public class Document
extends Element {
    private @Nullable Connection connection;
    private OutputSettings outputSettings = new OutputSettings();
    private Parser parser;
    private QuirksMode quirksMode = QuirksMode.noQuirks;
    private final String location;
    private static final Evaluator titleEval = new Evaluator.Tag("title");

    public Document(String namespace, String baseUri) {
        super(Tag.valueOf("#root", namespace, ParseSettings.htmlDefault), baseUri);
        this.location = baseUri;
        this.parser = Parser.htmlParser();
    }

    public Document(String baseUri) {
        this("http://www.w3.org/1999/xhtml", baseUri);
    }

    public static Document createShell(String baseUri) {
        Validate.notNull(baseUri);
        Document doc = new Document(baseUri);
        Element html = doc.appendElement("html");
        html.appendElement("head");
        html.appendElement("body");
        return doc;
    }

    public String location() {
        return this.location;
    }

    public Connection connection() {
        if (this.connection == null) {
            return Jsoup.newSession();
        }
        return this.connection;
    }

    public @Nullable DocumentType documentType() {
        for (Node node : this.childNodes) {
            if (node instanceof DocumentType) {
                return (DocumentType)node;
            }
            if (node instanceof LeafNode) continue;
            break;
        }
        return null;
    }

    private Element htmlEl() {
        for (Element el = this.firstElementChild(); el != null; el = el.nextElementSibling()) {
            if (!el.nameIs("html")) continue;
            return el;
        }
        return this.appendElement("html");
    }

    public Element head() {
        Element html = this.htmlEl();
        for (Element el = html.firstElementChild(); el != null; el = el.nextElementSibling()) {
            if (!el.nameIs("head")) continue;
            return el;
        }
        return html.prependElement("head");
    }

    public Element body() {
        Element html = this.htmlEl();
        for (Element el = html.firstElementChild(); el != null; el = el.nextElementSibling()) {
            if (!el.nameIs("body") && !el.nameIs("frameset")) continue;
            return el;
        }
        return html.appendElement("body");
    }

    public List<FormElement> forms() {
        return this.select("form").forms();
    }

    public FormElement expectForm(String cssQuery) {
        Elements els = this.select(cssQuery);
        for (Element el : els) {
            if (!(el instanceof FormElement)) continue;
            return (FormElement)el;
        }
        Validate.fail("No form elements matched the query '%s' in the document.", cssQuery);
        return null;
    }

    public String title() {
        Element titleEl = this.head().selectFirst(titleEval);
        return titleEl != null ? StringUtil.normaliseWhitespace(titleEl.text()).trim() : "";
    }

    public void title(String title) {
        Validate.notNull(title);
        Element titleEl = this.head().selectFirst(titleEval);
        if (titleEl == null) {
            titleEl = this.head().appendElement("title");
        }
        titleEl.text(title);
    }

    public Element createElement(String tagName) {
        return new Element(Tag.valueOf(tagName, this.parser.defaultNamespace(), ParseSettings.preserveCase), this.baseUri());
    }

    @Override
    public String outerHtml() {
        return super.html();
    }

    @Override
    public Element text(String text) {
        this.body().text(text);
        return this;
    }

    @Override
    public String nodeName() {
        return "#document";
    }

    public void charset(Charset charset) {
        this.outputSettings.charset(charset);
        this.ensureMetaCharsetElement();
    }

    public Charset charset() {
        return this.outputSettings.charset();
    }

    @Deprecated
    public void updateMetaCharsetElement(boolean noop) {
    }

    @Deprecated
    public boolean updateMetaCharsetElement() {
        return true;
    }

    @Override
    public Document clone() {
        Document clone = (Document)super.clone();
        clone.outputSettings = this.outputSettings.clone();
        clone.parser = this.parser.clone();
        return clone;
    }

    @Override
    public Document shallowClone() {
        Document clone = new Document(this.tag().namespace(), this.baseUri());
        if (this.attributes != null) {
            clone.attributes = this.attributes.clone();
        }
        clone.outputSettings = this.outputSettings.clone();
        return clone;
    }

    private void ensureMetaCharsetElement() {
        OutputSettings.Syntax syntax = this.outputSettings().syntax();
        if (syntax == OutputSettings.Syntax.html) {
            Element metaCharset = this.selectFirst("meta[charset]");
            if (metaCharset != null) {
                metaCharset.attr("charset", this.charset().displayName());
            } else {
                this.head().appendElement("meta").attr("charset", this.charset().displayName());
            }
            this.select("meta[name=charset]").remove();
        } else if (syntax == OutputSettings.Syntax.xml) {
            XmlDeclaration decl = this.ensureXmlDecl();
            decl.attr("version", "1.0");
            decl.attr("encoding", this.charset().displayName());
        }
    }

    private XmlDeclaration ensureXmlDecl() {
        XmlDeclaration decl;
        Node node = this.firstChild();
        if (node instanceof XmlDeclaration && (decl = (XmlDeclaration)node).name().equals("xml")) {
            return decl;
        }
        decl = new XmlDeclaration("xml", false);
        this.prependChild(decl);
        return decl;
    }

    public OutputSettings outputSettings() {
        return this.outputSettings;
    }

    public Document outputSettings(OutputSettings outputSettings) {
        Validate.notNull(outputSettings);
        this.outputSettings = outputSettings;
        return this;
    }

    public QuirksMode quirksMode() {
        return this.quirksMode;
    }

    public Document quirksMode(QuirksMode quirksMode) {
        this.quirksMode = quirksMode;
        return this;
    }

    public Parser parser() {
        return this.parser;
    }

    public Document parser(Parser parser) {
        this.parser = parser;
        return this;
    }

    public Document connection(Connection connection) {
        Validate.notNull(connection);
        this.connection = connection;
        return this;
    }

    public static class OutputSettings
    implements Cloneable {
        private Entities.EscapeMode escapeMode = Entities.EscapeMode.base;
        private Charset charset = DataUtil.UTF_8;
        private boolean prettyPrint = true;
        private boolean outline = false;
        private int indentAmount = 1;
        private int maxPaddingWidth = 30;
        private Syntax syntax = Syntax.html;

        public Entities.EscapeMode escapeMode() {
            return this.escapeMode;
        }

        public OutputSettings escapeMode(Entities.EscapeMode escapeMode) {
            this.escapeMode = escapeMode;
            return this;
        }

        public Charset charset() {
            return this.charset;
        }

        public OutputSettings charset(Charset charset) {
            this.charset = charset;
            return this;
        }

        public OutputSettings charset(String charset) {
            this.charset(Charset.forName(charset));
            return this;
        }

        public Syntax syntax() {
            return this.syntax;
        }

        public OutputSettings syntax(Syntax syntax) {
            this.syntax = syntax;
            if (syntax == Syntax.xml) {
                this.escapeMode(Entities.EscapeMode.xhtml);
            }
            return this;
        }

        public boolean prettyPrint() {
            return this.prettyPrint;
        }

        public OutputSettings prettyPrint(boolean pretty) {
            this.prettyPrint = pretty;
            return this;
        }

        public boolean outline() {
            return this.outline;
        }

        public OutputSettings outline(boolean outlineMode) {
            this.outline = outlineMode;
            return this;
        }

        public int indentAmount() {
            return this.indentAmount;
        }

        public OutputSettings indentAmount(int indentAmount) {
            Validate.isTrue(indentAmount >= 0);
            this.indentAmount = indentAmount;
            return this;
        }

        public int maxPaddingWidth() {
            return this.maxPaddingWidth;
        }

        public OutputSettings maxPaddingWidth(int maxPaddingWidth) {
            Validate.isTrue(maxPaddingWidth >= -1);
            this.maxPaddingWidth = maxPaddingWidth;
            return this;
        }

        public OutputSettings clone() {
            OutputSettings clone;
            try {
                clone = (OutputSettings)super.clone();
            }
            catch (CloneNotSupportedException e2) {
                throw new RuntimeException(e2);
            }
            clone.charset(this.charset.name());
            clone.escapeMode = Entities.EscapeMode.valueOf(this.escapeMode.name());
            return clone;
        }

        public static enum Syntax {
            html,
            xml;

        }
    }

    public static enum QuirksMode {
        noQuirks,
        quirks,
        limitedQuirks;

    }
}

