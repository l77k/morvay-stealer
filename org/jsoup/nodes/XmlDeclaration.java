/*
 * Decompiled with CFR 0.152.
 */
package org.jsoup.nodes;

import java.io.IOException;
import org.jsoup.SerializationException;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.LeafNode;

public class XmlDeclaration
extends LeafNode {
    private final boolean isDeclaration;

    public XmlDeclaration(String name, boolean isDeclaration) {
        super(name);
        this.isDeclaration = isDeclaration;
    }

    @Override
    public String nodeName() {
        return "#declaration";
    }

    public String name() {
        return this.coreValue();
    }

    public String getWholeDeclaration() {
        StringBuilder sb = StringUtil.borrowBuilder();
        try {
            this.getWholeDeclaration(sb, new Document.OutputSettings());
        }
        catch (IOException e2) {
            throw new SerializationException(e2);
        }
        return StringUtil.releaseBuilder(sb).trim();
    }

    private void getWholeDeclaration(Appendable accum, Document.OutputSettings out) throws IOException {
        for (Attribute attribute : this.attributes()) {
            String key = attribute.getKey();
            String val = attribute.getValue();
            if (key.equals(this.nodeName())) continue;
            accum.append(' ');
            accum.append(key);
            if (val.isEmpty()) continue;
            accum.append("=\"");
            Entities.escape(accum, val, out, 2);
            accum.append('\"');
        }
    }

    @Override
    void outerHtmlHead(Appendable accum, int depth, Document.OutputSettings out) throws IOException {
        accum.append("<").append(this.isDeclaration ? "!" : "?").append(this.coreValue());
        this.getWholeDeclaration(accum, out);
        accum.append(this.isDeclaration ? "" : "?").append(">");
    }

    @Override
    void outerHtmlTail(Appendable accum, int depth, Document.OutputSettings out) {
    }

    @Override
    public String toString() {
        return this.outerHtml();
    }

    @Override
    public XmlDeclaration clone() {
        return (XmlDeclaration)super.clone();
    }
}

