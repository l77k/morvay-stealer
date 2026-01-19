/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package org.jsoup.nodes;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.internal.SharedConstants;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;
import org.jspecify.annotations.Nullable;

public class FormElement
extends Element {
    private final Elements linkedEls = new Elements();
    private final Evaluator submittable = QueryParser.parse(StringUtil.join(SharedConstants.FormSubmitTags, ", "));

    public FormElement(Tag tag, @Nullable String baseUri, @Nullable Attributes attributes) {
        super(tag, baseUri, attributes);
    }

    public Elements elements() {
        Elements els = this.select(this.submittable);
        for (Element linkedEl : this.linkedEls) {
            if (linkedEl.ownerDocument() == null || els.contains(linkedEl)) continue;
            els.add(linkedEl);
        }
        return els;
    }

    public FormElement addElement(Element element) {
        this.linkedEls.add(element);
        return this;
    }

    @Override
    protected void removeChild(Node out) {
        super.removeChild(out);
        this.linkedEls.remove(out);
    }

    public Connection submit() {
        String action = this.hasAttr("action") ? this.absUrl("action") : this.baseUri();
        Validate.notEmpty(action, "Could not determine a form action URL for submit. Ensure you set a base URI when parsing.");
        Connection.Method method = this.attr("method").equalsIgnoreCase("POST") ? Connection.Method.POST : Connection.Method.GET;
        Document owner = this.ownerDocument();
        Connection connection = owner != null ? owner.connection().newRequest() : Jsoup.newSession();
        return connection.url(action).data(this.formData()).method(method);
    }

    public List<Connection.KeyVal> formData() {
        ArrayList<Connection.KeyVal> data = new ArrayList<Connection.KeyVal>();
        Elements formEls = this.elements();
        for (Element el : formEls) {
            String type;
            String name;
            if (!el.tag().isFormSubmittable() || el.hasAttr("disabled") || (name = el.attr("name")).length() == 0 || (type = el.attr("type")).equalsIgnoreCase("button") || type.equalsIgnoreCase("image")) continue;
            if (el.nameIs("select")) {
                Element option;
                Elements options = el.select("option[selected]");
                boolean set = false;
                for (Element option2 : options) {
                    data.add(HttpConnection.KeyVal.create(name, option2.val()));
                    set = true;
                }
                if (set || (option = el.selectFirst("option")) == null) continue;
                data.add(HttpConnection.KeyVal.create(name, option.val()));
                continue;
            }
            if ("checkbox".equalsIgnoreCase(type) || "radio".equalsIgnoreCase(type)) {
                if (!el.hasAttr("checked")) continue;
                String val = el.val().length() > 0 ? el.val() : "on";
                data.add(HttpConnection.KeyVal.create(name, val));
                continue;
            }
            data.add(HttpConnection.KeyVal.create(name, el.val()));
        }
        return data;
    }

    @Override
    public FormElement clone() {
        return (FormElement)super.clone();
    }
}

