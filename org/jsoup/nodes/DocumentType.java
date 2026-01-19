/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package org.jsoup.nodes;

import java.io.IOException;
import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.LeafNode;
import org.jspecify.annotations.Nullable;

public class DocumentType
extends LeafNode {
    public static final String PUBLIC_KEY = "PUBLIC";
    public static final String SYSTEM_KEY = "SYSTEM";
    private static final String NameKey = "name";
    private static final String PubSysKey = "pubSysKey";
    private static final String PublicId = "publicId";
    private static final String SystemId = "systemId";

    public DocumentType(String name, String publicId, String systemId) {
        super(name);
        Validate.notNull(publicId);
        Validate.notNull(systemId);
        this.attr(NameKey, name);
        this.attr(PublicId, publicId);
        this.attr(SystemId, systemId);
        this.updatePubSyskey();
    }

    public void setPubSysKey(@Nullable String value) {
        if (value != null) {
            this.attr(PubSysKey, value);
        }
    }

    private void updatePubSyskey() {
        if (this.has(PublicId)) {
            this.attr(PubSysKey, PUBLIC_KEY);
        } else if (this.has(SystemId)) {
            this.attr(PubSysKey, SYSTEM_KEY);
        }
    }

    public String name() {
        return this.attr(NameKey);
    }

    public String publicId() {
        return this.attr(PublicId);
    }

    public String systemId() {
        return this.attr(SystemId);
    }

    @Override
    public String nodeName() {
        return "#doctype";
    }

    @Override
    void outerHtmlHead(Appendable accum, int depth, Document.OutputSettings out) throws IOException {
        if (this.siblingIndex > 0 && out.prettyPrint()) {
            accum.append('\n');
        }
        if (out.syntax() == Document.OutputSettings.Syntax.html && !this.has(PublicId) && !this.has(SystemId)) {
            accum.append("<!doctype");
        } else {
            accum.append("<!DOCTYPE");
        }
        if (this.has(NameKey)) {
            accum.append(" ").append(this.attr(NameKey));
        }
        if (this.has(PubSysKey)) {
            accum.append(" ").append(this.attr(PubSysKey));
        }
        if (this.has(PublicId)) {
            accum.append(" \"").append(this.attr(PublicId)).append('\"');
        }
        if (this.has(SystemId)) {
            accum.append(" \"").append(this.attr(SystemId)).append('\"');
        }
        accum.append('>');
    }

    @Override
    void outerHtmlTail(Appendable accum, int depth, Document.OutputSettings out) {
    }

    private boolean has(String attribute) {
        return !StringUtil.isBlank(this.attr(attribute));
    }
}

