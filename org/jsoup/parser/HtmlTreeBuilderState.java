/*
 * Decompiled with CFR 0.152.
 */
package org.jsoup.parser;

import java.util.ArrayList;
import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Range;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Tag;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TreeBuilder;

enum HtmlTreeBuilderState {
    Initial{

        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t2)) {
                return true;
            }
            if (t2.isComment()) {
                tb.insertCommentNode(t2.asComment());
            } else if (t2.isDoctype()) {
                Token.Doctype d2 = t2.asDoctype();
                DocumentType doctype = new DocumentType(tb.settings.normalizeTag(d2.getName()), d2.getPublicIdentifier(), d2.getSystemIdentifier());
                doctype.setPubSysKey(d2.getPubSysKey());
                tb.getDocument().appendChild(doctype);
                tb.onNodeInserted(doctype);
                if (d2.isForceQuirks() || !doctype.name().equals("html") || doctype.publicId().equalsIgnoreCase("HTML")) {
                    tb.getDocument().quirksMode(Document.QuirksMode.quirks);
                }
                tb.transition(BeforeHtml);
            } else {
                tb.getDocument().quirksMode(Document.QuirksMode.quirks);
                tb.transition(BeforeHtml);
                return tb.process(t2);
            }
            return true;
        }
    }
    ,
    BeforeHtml{

        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            if (t2.isDoctype()) {
                tb.error(this);
                return false;
            }
            if (t2.isComment()) {
                tb.insertCommentNode(t2.asComment());
            } else if (HtmlTreeBuilderState.isWhitespace(t2)) {
                tb.insertCharacterNode(t2.asCharacter());
            } else if (t2.isStartTag() && t2.asStartTag().normalName().equals("html")) {
                tb.insertElementFor(t2.asStartTag());
                tb.transition(BeforeHead);
            } else {
                if (t2.isEndTag() && StringUtil.inSorted(t2.asEndTag().normalName(), Constants.BeforeHtmlToHead)) {
                    return this.anythingElse(t2, tb);
                }
                if (t2.isEndTag()) {
                    tb.error(this);
                    return false;
                }
                return this.anythingElse(t2, tb);
            }
            return true;
        }

        private boolean anythingElse(Token t2, HtmlTreeBuilder tb) {
            tb.processStartTag("html");
            tb.transition(BeforeHead);
            return tb.process(t2);
        }
    }
    ,
    BeforeHead{

        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t2)) {
                tb.insertCharacterNode(t2.asCharacter());
            } else if (t2.isComment()) {
                tb.insertCommentNode(t2.asComment());
            } else {
                if (t2.isDoctype()) {
                    tb.error(this);
                    return false;
                }
                if (t2.isStartTag() && t2.asStartTag().normalName().equals("html")) {
                    return InBody.process(t2, tb);
                }
                if (t2.isStartTag() && t2.asStartTag().normalName().equals("head")) {
                    Element head = tb.insertElementFor(t2.asStartTag());
                    tb.setHeadElement(head);
                    tb.transition(InHead);
                } else {
                    if (t2.isEndTag() && StringUtil.inSorted(t2.asEndTag().normalName(), Constants.BeforeHtmlToHead)) {
                        tb.processStartTag("head");
                        return tb.process(t2);
                    }
                    if (t2.isEndTag()) {
                        tb.error(this);
                        return false;
                    }
                    tb.processStartTag("head");
                    return tb.process(t2);
                }
            }
            return true;
        }
    }
    ,
    InHead{

        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t2)) {
                tb.insertCharacterNode(t2.asCharacter());
                return true;
            }
            switch (t2.type) {
                case Comment: {
                    tb.insertCommentNode(t2.asComment());
                    break;
                }
                case Doctype: {
                    tb.error(this);
                    return false;
                }
                case StartTag: {
                    Token.StartTag start = t2.asStartTag();
                    String name = start.normalName();
                    if (name.equals("html")) {
                        return InBody.process(t2, tb);
                    }
                    if (StringUtil.inSorted(name, Constants.InHeadEmpty)) {
                        Element el = tb.insertEmptyElementFor(start);
                        if (!name.equals("base") || !el.hasAttr("href")) break;
                        tb.maybeSetBaseUri(el);
                        break;
                    }
                    if (name.equals("meta")) {
                        tb.insertEmptyElementFor(start);
                        break;
                    }
                    if (name.equals("title")) {
                        HtmlTreeBuilderState.handleRcData(start, tb);
                        break;
                    }
                    if (StringUtil.inSorted(name, Constants.InHeadRaw)) {
                        HtmlTreeBuilderState.handleRawtext(start, tb);
                        break;
                    }
                    if (name.equals("noscript")) {
                        tb.insertElementFor(start);
                        tb.transition(InHeadNoscript);
                        break;
                    }
                    if (name.equals("script")) {
                        tb.tokeniser.transition(TokeniserState.ScriptData);
                        tb.markInsertionMode();
                        tb.transition(Text);
                        tb.insertElementFor(start);
                        break;
                    }
                    if (name.equals("head")) {
                        tb.error(this);
                        return false;
                    }
                    if (name.equals("template")) {
                        tb.insertElementFor(start);
                        tb.insertMarkerToFormattingElements();
                        tb.framesetOk(false);
                        tb.transition(InTemplate);
                        tb.pushTemplateMode(InTemplate);
                        break;
                    }
                    return this.anythingElse(t2, tb);
                }
                case EndTag: {
                    Token.EndTag end = t2.asEndTag();
                    String name = end.normalName();
                    if (name.equals("head")) {
                        tb.pop();
                        tb.transition(AfterHead);
                        break;
                    }
                    if (StringUtil.inSorted(name, Constants.InHeadEnd)) {
                        return this.anythingElse(t2, tb);
                    }
                    if (name.equals("template")) {
                        if (!tb.onStack(name)) {
                            tb.error(this);
                            break;
                        }
                        tb.generateImpliedEndTags(true);
                        if (!tb.currentElementIs(name)) {
                            tb.error(this);
                        }
                        tb.popStackToClose(name);
                        tb.clearFormattingElementsToLastMarker();
                        tb.popTemplateMode();
                        tb.resetInsertionMode();
                        break;
                    }
                    tb.error(this);
                    return false;
                }
                default: {
                    return this.anythingElse(t2, tb);
                }
            }
            return true;
        }

        private boolean anythingElse(Token t2, TreeBuilder tb) {
            tb.processEndTag("head");
            return tb.process(t2);
        }
    }
    ,
    InHeadNoscript{

        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            if (t2.isDoctype()) {
                tb.error(this);
            } else {
                if (t2.isStartTag() && t2.asStartTag().normalName().equals("html")) {
                    return tb.process(t2, InBody);
                }
                if (t2.isEndTag() && t2.asEndTag().normalName().equals("noscript")) {
                    tb.pop();
                    tb.transition(InHead);
                } else {
                    if (HtmlTreeBuilderState.isWhitespace(t2) || t2.isComment() || t2.isStartTag() && StringUtil.inSorted(t2.asStartTag().normalName(), Constants.InHeadNoScriptHead)) {
                        return tb.process(t2, InHead);
                    }
                    if (t2.isEndTag() && t2.asEndTag().normalName().equals("br")) {
                        return this.anythingElse(t2, tb);
                    }
                    if (t2.isStartTag() && StringUtil.inSorted(t2.asStartTag().normalName(), Constants.InHeadNoscriptIgnore) || t2.isEndTag()) {
                        tb.error(this);
                        return false;
                    }
                    return this.anythingElse(t2, tb);
                }
            }
            return true;
        }

        private boolean anythingElse(Token t2, HtmlTreeBuilder tb) {
            tb.error(this);
            tb.insertCharacterNode(new Token.Character().data(t2.toString()));
            return true;
        }
    }
    ,
    AfterHead{

        /*
         * Enabled aggressive block sorting
         */
        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t2)) {
                tb.insertCharacterNode(t2.asCharacter());
                return true;
            }
            if (t2.isComment()) {
                tb.insertCommentNode(t2.asComment());
                return true;
            }
            if (t2.isDoctype()) {
                tb.error(this);
                return true;
            }
            if (t2.isStartTag()) {
                Token.StartTag startTag = t2.asStartTag();
                String name = startTag.normalName();
                if (name.equals("html")) {
                    return tb.process(t2, InBody);
                }
                if (name.equals("body")) {
                    tb.insertElementFor(startTag);
                    tb.framesetOk(false);
                    tb.transition(InBody);
                    return true;
                }
                if (name.equals("frameset")) {
                    tb.insertElementFor(startTag);
                    tb.transition(InFrameset);
                    return true;
                }
                if (StringUtil.inSorted(name, Constants.InBodyStartToHead)) {
                    tb.error(this);
                    Element head = tb.getHeadElement();
                    tb.push(head);
                    tb.process(t2, InHead);
                    tb.removeFromStack(head);
                    return true;
                }
                if (name.equals("head")) {
                    tb.error(this);
                    return false;
                }
                this.anythingElse(t2, tb);
                return true;
            }
            if (!t2.isEndTag()) {
                this.anythingElse(t2, tb);
                return true;
            }
            String name = t2.asEndTag().normalName();
            if (StringUtil.inSorted(name, Constants.AfterHeadBody)) {
                this.anythingElse(t2, tb);
                return true;
            }
            if (name.equals("template")) {
                tb.process(t2, InHead);
                return true;
            }
            tb.error(this);
            return false;
        }

        private boolean anythingElse(Token t2, HtmlTreeBuilder tb) {
            tb.processStartTag("body");
            tb.framesetOk(true);
            return tb.process(t2);
        }
    }
    ,
    InBody{
        private static final int MaxStackScan = 24;

        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            switch (t2.type) {
                case Character: {
                    Token.Character c2 = t2.asCharacter();
                    if (c2.getData().equals(nullString)) {
                        tb.error(this);
                        return false;
                    }
                    if (tb.framesetOk() && HtmlTreeBuilderState.isWhitespace(c2)) {
                        tb.reconstructFormattingElements();
                        tb.insertCharacterNode(c2);
                        break;
                    }
                    tb.reconstructFormattingElements();
                    tb.insertCharacterNode(c2);
                    tb.framesetOk(false);
                    break;
                }
                case Comment: {
                    tb.insertCommentNode(t2.asComment());
                    break;
                }
                case Doctype: {
                    tb.error(this);
                    return false;
                }
                case StartTag: {
                    return this.inBodyStartTag(t2, tb);
                }
                case EndTag: {
                    return this.inBodyEndTag(t2, tb);
                }
                case EOF: {
                    if (tb.templateModeSize() > 0) {
                        return tb.process(t2, InTemplate);
                    }
                    if (!tb.onStackNot(Constants.InBodyEndOtherErrors)) break;
                    tb.error(this);
                    break;
                }
                default: {
                    Validate.wtf("Unexpected state: " + (Object)((Object)t2.type));
                }
            }
            return true;
        }

        private boolean inBodyStartTag(Token t2, HtmlTreeBuilder tb) {
            String name;
            Token.StartTag startTag = t2.asStartTag();
            switch (name = startTag.normalName()) {
                case "a": {
                    if (tb.getActiveFormattingElement("a") != null) {
                        tb.error(this);
                        tb.processEndTag("a");
                        Element remainingA = tb.getFromStack("a");
                        if (remainingA != null) {
                            tb.removeFromActiveFormattingElements(remainingA);
                            tb.removeFromStack(remainingA);
                        }
                    }
                    tb.reconstructFormattingElements();
                    Element el = tb.insertElementFor(startTag);
                    tb.pushActiveFormattingElements(el);
                    break;
                }
                case "span": {
                    tb.reconstructFormattingElements();
                    tb.insertElementFor(startTag);
                    break;
                }
                case "li": {
                    tb.framesetOk(false);
                    ArrayList<Element> stack = tb.getStack();
                    for (int i2 = stack.size() - 1; i2 > 0; --i2) {
                        Element el = stack.get(i2);
                        if (el.nameIs("li")) {
                            tb.processEndTag("li");
                            break;
                        }
                        if (HtmlTreeBuilder.isSpecial(el) && !StringUtil.inSorted(el.normalName(), Constants.InBodyStartLiBreakers)) break;
                    }
                    if (tb.inButtonScope("p")) {
                        tb.processEndTag("p");
                    }
                    tb.insertElementFor(startTag);
                    break;
                }
                case "html": {
                    tb.error(this);
                    if (tb.onStack("template")) {
                        return false;
                    }
                    ArrayList<Element> stack = tb.getStack();
                    if (stack.size() <= 0) break;
                    Element html = tb.getStack().get(0);
                    HtmlTreeBuilderState.mergeAttributes(startTag, html);
                    break;
                }
                case "body": {
                    tb.error(this);
                    ArrayList<Element> stack = tb.getStack();
                    if (stack.size() == 1 || stack.size() > 2 && !stack.get(1).nameIs("body") || tb.onStack("template")) {
                        return false;
                    }
                    tb.framesetOk(false);
                    Element body = tb.getFromStack("body");
                    if (body == null) break;
                    HtmlTreeBuilderState.mergeAttributes(startTag, body);
                    break;
                }
                case "frameset": {
                    tb.error(this);
                    ArrayList<Element> stack = tb.getStack();
                    if (stack.size() == 1 || stack.size() > 2 && !stack.get(1).nameIs("body")) {
                        return false;
                    }
                    if (!tb.framesetOk()) {
                        return false;
                    }
                    Element second = stack.get(1);
                    if (second.parent() != null) {
                        second.remove();
                    }
                    while (stack.size() > 1) {
                        stack.remove(stack.size() - 1);
                    }
                    tb.insertElementFor(startTag);
                    tb.transition(InFrameset);
                    break;
                }
                case "form": {
                    if (tb.getFormElement() != null && !tb.onStack("template")) {
                        tb.error(this);
                        return false;
                    }
                    if (tb.inButtonScope("p")) {
                        tb.closeElement("p");
                    }
                    tb.insertFormElement(startTag, true, true);
                    break;
                }
                case "plaintext": {
                    if (tb.inButtonScope("p")) {
                        tb.processEndTag("p");
                    }
                    tb.insertElementFor(startTag);
                    tb.tokeniser.transition(TokeniserState.PLAINTEXT);
                    break;
                }
                case "button": {
                    if (tb.inButtonScope("button")) {
                        tb.error(this);
                        tb.processEndTag("button");
                        tb.process(startTag);
                        break;
                    }
                    tb.reconstructFormattingElements();
                    tb.insertElementFor(startTag);
                    tb.framesetOk(false);
                    break;
                }
                case "nobr": {
                    tb.reconstructFormattingElements();
                    if (tb.inScope("nobr")) {
                        tb.error(this);
                        tb.processEndTag("nobr");
                        tb.reconstructFormattingElements();
                    }
                    Element el = tb.insertElementFor(startTag);
                    tb.pushActiveFormattingElements(el);
                    break;
                }
                case "table": {
                    if (tb.getDocument().quirksMode() != Document.QuirksMode.quirks && tb.inButtonScope("p")) {
                        tb.processEndTag("p");
                    }
                    tb.insertElementFor(startTag);
                    tb.framesetOk(false);
                    tb.transition(InTable);
                    break;
                }
                case "input": {
                    tb.reconstructFormattingElements();
                    Element el = tb.insertEmptyElementFor(startTag);
                    if (el.attr("type").equalsIgnoreCase("hidden")) break;
                    tb.framesetOk(false);
                    break;
                }
                case "hr": {
                    if (tb.inButtonScope("p")) {
                        tb.processEndTag("p");
                    }
                    tb.insertEmptyElementFor(startTag);
                    tb.framesetOk(false);
                    break;
                }
                case "image": {
                    if (tb.getFromStack("svg") == null) {
                        return tb.process(startTag.name("img"));
                    }
                    tb.insertElementFor(startTag);
                    break;
                }
                case "textarea": {
                    tb.insertElementFor(startTag);
                    if (startTag.isSelfClosing()) break;
                    tb.tokeniser.transition(TokeniserState.Rcdata);
                    tb.markInsertionMode();
                    tb.framesetOk(false);
                    tb.transition(Text);
                    break;
                }
                case "xmp": {
                    if (tb.inButtonScope("p")) {
                        tb.processEndTag("p");
                    }
                    tb.reconstructFormattingElements();
                    tb.framesetOk(false);
                    HtmlTreeBuilderState.handleRawtext(startTag, tb);
                    break;
                }
                case "iframe": {
                    tb.framesetOk(false);
                    HtmlTreeBuilderState.handleRawtext(startTag, tb);
                    break;
                }
                case "noembed": {
                    HtmlTreeBuilderState.handleRawtext(startTag, tb);
                    break;
                }
                case "select": {
                    tb.reconstructFormattingElements();
                    tb.insertElementFor(startTag);
                    tb.framesetOk(false);
                    if (startTag.selfClosing) break;
                    HtmlTreeBuilderState state = tb.state();
                    if (state.equals((Object)InTable) || state.equals((Object)InCaption) || state.equals((Object)InTableBody) || state.equals((Object)InRow) || state.equals((Object)InCell)) {
                        tb.transition(InSelectInTable);
                        break;
                    }
                    tb.transition(InSelect);
                    break;
                }
                case "math": {
                    tb.reconstructFormattingElements();
                    tb.insertForeignElementFor(startTag, "http://www.w3.org/1998/Math/MathML");
                    break;
                }
                case "svg": {
                    tb.reconstructFormattingElements();
                    tb.insertForeignElementFor(startTag, "http://www.w3.org/2000/svg");
                    break;
                }
                case "h1": 
                case "h2": 
                case "h3": 
                case "h4": 
                case "h5": 
                case "h6": {
                    if (tb.inButtonScope("p")) {
                        tb.processEndTag("p");
                    }
                    if (StringUtil.inSorted(tb.currentElement().normalName(), Constants.Headings)) {
                        tb.error(this);
                        tb.pop();
                    }
                    tb.insertElementFor(startTag);
                    break;
                }
                case "pre": 
                case "listing": {
                    if (tb.inButtonScope("p")) {
                        tb.processEndTag("p");
                    }
                    tb.insertElementFor(startTag);
                    tb.reader.matchConsume("\n");
                    tb.framesetOk(false);
                    break;
                }
                case "dd": 
                case "dt": {
                    tb.framesetOk(false);
                    ArrayList<Element> stack = tb.getStack();
                    int bottom = stack.size() - 1;
                    int upper = bottom >= 24 ? bottom - 24 : 0;
                    for (int i3 = bottom; i3 >= upper; --i3) {
                        Element el = stack.get(i3);
                        if (StringUtil.inSorted(el.normalName(), Constants.DdDt)) {
                            tb.processEndTag(el.normalName());
                            break;
                        }
                        if (HtmlTreeBuilder.isSpecial(el) && !StringUtil.inSorted(el.normalName(), Constants.InBodyStartLiBreakers)) break;
                    }
                    if (tb.inButtonScope("p")) {
                        tb.processEndTag("p");
                    }
                    tb.insertElementFor(startTag);
                    break;
                }
                case "optgroup": 
                case "option": {
                    if (tb.currentElementIs("option")) {
                        tb.processEndTag("option");
                    }
                    tb.reconstructFormattingElements();
                    tb.insertElementFor(startTag);
                    break;
                }
                case "rb": 
                case "rtc": {
                    if (tb.inScope("ruby")) {
                        tb.generateImpliedEndTags();
                        if (!tb.currentElementIs("ruby")) {
                            tb.error(this);
                        }
                    }
                    tb.insertElementFor(startTag);
                    break;
                }
                case "rp": 
                case "rt": {
                    if (tb.inScope("ruby")) {
                        tb.generateImpliedEndTags("rtc");
                        if (!tb.currentElementIs("rtc") && !tb.currentElementIs("ruby")) {
                            tb.error(this);
                        }
                    }
                    tb.insertElementFor(startTag);
                    break;
                }
                case "area": 
                case "br": 
                case "embed": 
                case "img": 
                case "keygen": 
                case "wbr": {
                    tb.reconstructFormattingElements();
                    tb.insertEmptyElementFor(startTag);
                    tb.framesetOk(false);
                    break;
                }
                case "b": 
                case "big": 
                case "code": 
                case "em": 
                case "font": 
                case "i": 
                case "s": 
                case "small": 
                case "strike": 
                case "strong": 
                case "tt": 
                case "u": {
                    tb.reconstructFormattingElements();
                    Element el = tb.insertElementFor(startTag);
                    tb.pushActiveFormattingElements(el);
                    break;
                }
                default: {
                    if (!Tag.isKnownTag(name)) {
                        tb.insertElementFor(startTag);
                        break;
                    }
                    if (StringUtil.inSorted(name, Constants.InBodyStartPClosers)) {
                        if (tb.inButtonScope("p")) {
                            tb.processEndTag("p");
                        }
                        tb.insertElementFor(startTag);
                        break;
                    }
                    if (StringUtil.inSorted(name, Constants.InBodyStartToHead)) {
                        return tb.process(t2, InHead);
                    }
                    if (StringUtil.inSorted(name, Constants.InBodyStartApplets)) {
                        tb.reconstructFormattingElements();
                        tb.insertElementFor(startTag);
                        tb.insertMarkerToFormattingElements();
                        tb.framesetOk(false);
                        break;
                    }
                    if (StringUtil.inSorted(name, Constants.InBodyStartMedia)) {
                        tb.insertEmptyElementFor(startTag);
                        break;
                    }
                    if (StringUtil.inSorted(name, Constants.InBodyStartDrop)) {
                        tb.error(this);
                        return false;
                    }
                    tb.reconstructFormattingElements();
                    tb.insertElementFor(startTag);
                }
            }
            return true;
        }

        private boolean inBodyEndTag(Token t2, HtmlTreeBuilder tb) {
            String name;
            Token.EndTag endTag = t2.asEndTag();
            switch (name = endTag.normalName()) {
                case "template": {
                    tb.process(t2, InHead);
                    break;
                }
                case "sarcasm": 
                case "span": {
                    return this.anyOtherEndTag(t2, tb);
                }
                case "li": {
                    if (!tb.inListItemScope(name)) {
                        tb.error(this);
                        return false;
                    }
                    tb.generateImpliedEndTags(name);
                    if (!tb.currentElementIs(name)) {
                        tb.error(this);
                    }
                    tb.popStackToClose(name);
                    break;
                }
                case "body": {
                    if (!tb.inScope("body")) {
                        tb.error(this);
                        return false;
                    }
                    if (tb.onStackNot(Constants.InBodyEndOtherErrors)) {
                        tb.error(this);
                    }
                    tb.onNodeClosed(tb.getFromStack("body"));
                    tb.transition(AfterBody);
                    break;
                }
                case "html": {
                    if (!tb.onStack("body")) {
                        tb.error(this);
                        return false;
                    }
                    if (tb.onStackNot(Constants.InBodyEndOtherErrors)) {
                        tb.error(this);
                    }
                    tb.transition(AfterBody);
                    return tb.process(t2);
                }
                case "form": {
                    if (!tb.onStack("template")) {
                        FormElement currentForm = tb.getFormElement();
                        tb.setFormElement(null);
                        if (currentForm == null || !tb.inScope(name)) {
                            tb.error(this);
                            return false;
                        }
                        tb.generateImpliedEndTags();
                        if (!tb.currentElementIs(name)) {
                            tb.error(this);
                        }
                        tb.removeFromStack(currentForm);
                        break;
                    }
                    if (!tb.inScope(name)) {
                        tb.error(this);
                        return false;
                    }
                    tb.generateImpliedEndTags();
                    if (!tb.currentElementIs(name)) {
                        tb.error(this);
                    }
                    tb.popStackToClose(name);
                    break;
                }
                case "p": {
                    if (!tb.inButtonScope(name)) {
                        tb.error(this);
                        tb.processStartTag(name);
                        return tb.process(endTag);
                    }
                    tb.generateImpliedEndTags(name);
                    if (!tb.currentElementIs(name)) {
                        tb.error(this);
                    }
                    tb.popStackToClose(name);
                    break;
                }
                case "dd": 
                case "dt": {
                    if (!tb.inScope(name)) {
                        tb.error(this);
                        return false;
                    }
                    tb.generateImpliedEndTags(name);
                    if (!tb.currentElementIs(name)) {
                        tb.error(this);
                    }
                    tb.popStackToClose(name);
                    break;
                }
                case "h1": 
                case "h2": 
                case "h3": 
                case "h4": 
                case "h5": 
                case "h6": {
                    if (!tb.inScope(Constants.Headings)) {
                        tb.error(this);
                        return false;
                    }
                    tb.generateImpliedEndTags(name);
                    if (!tb.currentElementIs(name)) {
                        tb.error(this);
                    }
                    tb.popStackToClose(Constants.Headings);
                    break;
                }
                case "br": {
                    tb.error(this);
                    tb.processStartTag("br");
                    return false;
                }
                default: {
                    if (StringUtil.inSorted(name, Constants.InBodyEndAdoptionFormatters)) {
                        return this.inBodyEndTagAdoption(t2, tb);
                    }
                    if (StringUtil.inSorted(name, Constants.InBodyEndClosers)) {
                        if (!tb.inScope(name)) {
                            tb.error(this);
                            return false;
                        }
                        tb.generateImpliedEndTags();
                        if (!tb.currentElementIs(name)) {
                            tb.error(this);
                        }
                        tb.popStackToClose(name);
                        break;
                    }
                    if (StringUtil.inSorted(name, Constants.InBodyStartApplets)) {
                        if (tb.inScope("name")) break;
                        if (!tb.inScope(name)) {
                            tb.error(this);
                            return false;
                        }
                        tb.generateImpliedEndTags();
                        if (!tb.currentElementIs(name)) {
                            tb.error(this);
                        }
                        tb.popStackToClose(name);
                        tb.clearFormattingElementsToLastMarker();
                        break;
                    }
                    return this.anyOtherEndTag(t2, tb);
                }
            }
            return true;
        }

        boolean anyOtherEndTag(Token t2, HtmlTreeBuilder tb) {
            String name = t2.asEndTag().normalName;
            ArrayList<Element> stack = tb.getStack();
            Element elFromStack = tb.getFromStack(name);
            if (elFromStack == null) {
                tb.error(this);
                return false;
            }
            for (int pos = stack.size() - 1; pos >= 0; --pos) {
                Element node = stack.get(pos);
                if (node.nameIs(name)) {
                    tb.generateImpliedEndTags(name);
                    if (!tb.currentElementIs(name)) {
                        tb.error(this);
                    }
                    tb.popStackToClose(name);
                    break;
                }
                if (!HtmlTreeBuilder.isSpecial(node)) continue;
                tb.error(this);
                return false;
            }
            return true;
        }

        private boolean inBodyEndTagAdoption(Token t2, HtmlTreeBuilder tb) {
            Token.EndTag endTag = t2.asEndTag();
            String subject = endTag.normalName;
            if (tb.currentElement().normalName().equals(subject) && !tb.isInActiveFormattingElements(tb.currentElement())) {
                tb.pop();
                return true;
            }
            int outer = 0;
            while (outer < 8) {
                Element next;
                ++outer;
                Element formatEl = null;
                for (int i2 = tb.formattingElements.size() - 1; i2 >= 0 && (next = tb.formattingElements.get(i2)) != null; --i2) {
                    if (!next.normalName().equals(subject)) continue;
                    formatEl = next;
                    break;
                }
                if (formatEl == null) {
                    return this.anyOtherEndTag(t2, tb);
                }
                if (!tb.onStack(formatEl)) {
                    tb.error(this);
                    tb.removeFromActiveFormattingElements(formatEl);
                    return true;
                }
                if (!tb.inScope(formatEl.normalName())) {
                    tb.error(this);
                    return false;
                }
                if (tb.currentElement() != formatEl) {
                    tb.error(this);
                }
                Element furthestBlock = null;
                ArrayList<Element> stack = tb.getStack();
                int fei = stack.lastIndexOf(formatEl);
                if (fei != -1) {
                    for (int i3 = fei + 1; i3 < stack.size(); ++i3) {
                        Element el = stack.get(i3);
                        if (!HtmlTreeBuilder.isSpecial(el)) continue;
                        furthestBlock = el;
                        break;
                    }
                }
                if (furthestBlock == null) {
                    while (tb.currentElement() != formatEl) {
                        tb.pop();
                    }
                    tb.pop();
                    tb.removeFromActiveFormattingElements(formatEl);
                    return true;
                }
                Element commonAncestor = tb.aboveOnStack(formatEl);
                if (commonAncestor == null) {
                    tb.error(this);
                    return true;
                }
                int bookmark = tb.positionOfElement(formatEl);
                Element el = furthestBlock;
                Element lastEl = furthestBlock;
                int inner = 0;
                while (true) {
                    ++inner;
                    if ((el = !tb.onStack(el) ? el.parent() : tb.aboveOnStack(el)) == null) {
                        tb.error(this);
                        break;
                    }
                    if (el == formatEl) break;
                    if (inner > 3 && tb.isInActiveFormattingElements(el)) {
                        tb.removeFromActiveFormattingElements(el);
                        break;
                    }
                    if (!tb.isInActiveFormattingElements(el)) {
                        tb.removeFromStack(el);
                        continue;
                    }
                    Element replacement = new Element(tb.tagFor(el.nodeName(), el.normalName(), tb.defaultNamespace(), ParseSettings.preserveCase), tb.getBaseUri());
                    tb.replaceActiveFormattingElement(el, replacement);
                    tb.replaceOnStack(el, replacement);
                    el = replacement;
                    if (lastEl == furthestBlock) {
                        bookmark = tb.positionOfElement(el) + 1;
                    }
                    el.appendChild(lastEl);
                    lastEl = el;
                }
                commonAncestor.appendChild(lastEl);
                Element adoptor = new Element(formatEl.tag(), tb.getBaseUri());
                adoptor.attributes().addAll(formatEl.attributes());
                for (Node child : furthestBlock.childNodes()) {
                    adoptor.appendChild(child);
                }
                furthestBlock.appendChild(adoptor);
                tb.removeFromActiveFormattingElements(formatEl);
                tb.pushWithBookmark(adoptor, bookmark);
                tb.removeFromStack(formatEl);
                tb.insertOnStackAfter(furthestBlock, adoptor);
            }
            return true;
        }
    }
    ,
    Text{

        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            if (t2.isCharacter()) {
                tb.insertCharacterNode(t2.asCharacter());
            } else {
                if (t2.isEOF()) {
                    tb.error(this);
                    tb.pop();
                    tb.transition(tb.originalState());
                    return tb.process(t2);
                }
                if (t2.isEndTag()) {
                    tb.pop();
                    tb.transition(tb.originalState());
                }
            }
            return true;
        }
    }
    ,
    InTable{

        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            if (t2.isCharacter() && StringUtil.inSorted(tb.currentElement().normalName(), Constants.InTableFoster)) {
                tb.resetPendingTableCharacters();
                tb.markInsertionMode();
                tb.transition(InTableText);
                return tb.process(t2);
            }
            if (t2.isComment()) {
                tb.insertCommentNode(t2.asComment());
                return true;
            }
            if (t2.isDoctype()) {
                tb.error(this);
                return false;
            }
            if (t2.isStartTag()) {
                Token.StartTag startTag = t2.asStartTag();
                String name = startTag.normalName();
                if (name.equals("caption")) {
                    tb.clearStackToTableContext();
                    tb.insertMarkerToFormattingElements();
                    tb.insertElementFor(startTag);
                    tb.transition(InCaption);
                } else if (name.equals("colgroup")) {
                    tb.clearStackToTableContext();
                    tb.insertElementFor(startTag);
                    tb.transition(InColumnGroup);
                } else {
                    if (name.equals("col")) {
                        tb.clearStackToTableContext();
                        tb.processStartTag("colgroup");
                        return tb.process(t2);
                    }
                    if (StringUtil.inSorted(name, Constants.InTableToBody)) {
                        tb.clearStackToTableContext();
                        tb.insertElementFor(startTag);
                        tb.transition(InTableBody);
                    } else {
                        if (StringUtil.inSorted(name, Constants.InTableAddBody)) {
                            tb.clearStackToTableContext();
                            tb.processStartTag("tbody");
                            return tb.process(t2);
                        }
                        if (name.equals("table")) {
                            tb.error(this);
                            if (!tb.inTableScope(name)) {
                                return false;
                            }
                            tb.popStackToClose(name);
                            if (!tb.resetInsertionMode()) {
                                tb.insertElementFor(startTag);
                                return true;
                            }
                            return tb.process(t2);
                        }
                        if (StringUtil.inSorted(name, Constants.InTableToHead)) {
                            return tb.process(t2, InHead);
                        }
                        if (name.equals("input")) {
                            if (!startTag.hasAttributes() || !startTag.attributes.get("type").equalsIgnoreCase("hidden")) {
                                return this.anythingElse(t2, tb);
                            }
                            tb.insertEmptyElementFor(startTag);
                        } else if (name.equals("form")) {
                            tb.error(this);
                            if (tb.getFormElement() != null || tb.onStack("template")) {
                                return false;
                            }
                            tb.insertFormElement(startTag, false, false);
                        } else {
                            return this.anythingElse(t2, tb);
                        }
                    }
                }
                return true;
            }
            if (t2.isEndTag()) {
                Token.EndTag endTag = t2.asEndTag();
                String name = endTag.normalName();
                if (name.equals("table")) {
                    if (!tb.inTableScope(name)) {
                        tb.error(this);
                        return false;
                    }
                    tb.popStackToClose("table");
                    tb.resetInsertionMode();
                } else {
                    if (StringUtil.inSorted(name, Constants.InTableEndErr)) {
                        tb.error(this);
                        return false;
                    }
                    if (name.equals("template")) {
                        tb.process(t2, InHead);
                    } else {
                        return this.anythingElse(t2, tb);
                    }
                }
                return true;
            }
            if (t2.isEOF()) {
                if (tb.currentElementIs("html")) {
                    tb.error(this);
                }
                return true;
            }
            return this.anythingElse(t2, tb);
        }

        boolean anythingElse(Token t2, HtmlTreeBuilder tb) {
            tb.error(this);
            tb.setFosterInserts(true);
            tb.process(t2, InBody);
            tb.setFosterInserts(false);
            return true;
        }
    }
    ,
    InTableText{

        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            Token.Character c2;
            if (t2.type == Token.TokenType.Character) {
                c2 = t2.asCharacter();
                if (c2.getData().equals(nullString)) {
                    tb.error(this);
                    return false;
                }
            } else {
                if (tb.getPendingTableCharacters().size() > 0) {
                    Token og = tb.currentToken;
                    for (Token.Character c3 : tb.getPendingTableCharacters()) {
                        tb.currentToken = c3;
                        if (!HtmlTreeBuilderState.isWhitespace(c3)) {
                            tb.error(this);
                            if (StringUtil.inSorted(tb.currentElement().normalName(), Constants.InTableFoster)) {
                                tb.setFosterInserts(true);
                                tb.process(c3, InBody);
                                tb.setFosterInserts(false);
                                continue;
                            }
                            tb.process(c3, InBody);
                            continue;
                        }
                        tb.insertCharacterNode(c3);
                    }
                    tb.currentToken = og;
                    tb.resetPendingTableCharacters();
                }
                tb.transition(tb.originalState());
                return tb.process(t2);
            }
            tb.addPendingTableCharacters(c2);
            return true;
        }
    }
    ,
    InCaption{

        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            if (t2.isEndTag() && t2.asEndTag().normalName().equals("caption")) {
                if (!tb.inTableScope("caption")) {
                    tb.error(this);
                    return false;
                }
                tb.generateImpliedEndTags();
                if (!tb.currentElementIs("caption")) {
                    tb.error(this);
                }
                tb.popStackToClose("caption");
                tb.clearFormattingElementsToLastMarker();
                tb.transition(InTable);
            } else if (t2.isStartTag() && StringUtil.inSorted(t2.asStartTag().normalName(), Constants.InCellCol) || t2.isEndTag() && t2.asEndTag().normalName().equals("table")) {
                if (!tb.inTableScope("caption")) {
                    tb.error(this);
                    return false;
                }
                tb.generateImpliedEndTags(false);
                if (!tb.currentElementIs("caption")) {
                    tb.error(this);
                }
                tb.popStackToClose("caption");
                tb.clearFormattingElementsToLastMarker();
                tb.transition(InTable);
                InTable.process(t2, tb);
            } else {
                if (t2.isEndTag() && StringUtil.inSorted(t2.asEndTag().normalName(), Constants.InCaptionIgnore)) {
                    tb.error(this);
                    return false;
                }
                return tb.process(t2, InBody);
            }
            return true;
        }
    }
    ,
    InColumnGroup{

        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t2)) {
                tb.insertCharacterNode(t2.asCharacter());
                return true;
            }
            block0 : switch (t2.type) {
                case Comment: {
                    tb.insertCommentNode(t2.asComment());
                    break;
                }
                case Doctype: {
                    tb.error(this);
                    break;
                }
                case StartTag: {
                    Token.StartTag startTag = t2.asStartTag();
                    switch (startTag.normalName()) {
                        case "html": {
                            return tb.process(t2, InBody);
                        }
                        case "col": {
                            tb.insertEmptyElementFor(startTag);
                            break block0;
                        }
                        case "template": {
                            tb.process(t2, InHead);
                            break block0;
                        }
                    }
                    return this.anythingElse(t2, tb);
                }
                case EndTag: {
                    String name;
                    Token.EndTag endTag = t2.asEndTag();
                    switch (name = endTag.normalName()) {
                        case "colgroup": {
                            if (!tb.currentElementIs(name)) {
                                tb.error(this);
                                return false;
                            }
                            tb.pop();
                            tb.transition(InTable);
                            break block0;
                        }
                        case "template": {
                            tb.process(t2, InHead);
                            break block0;
                        }
                    }
                    return this.anythingElse(t2, tb);
                }
                case EOF: {
                    if (tb.currentElementIs("html")) {
                        return true;
                    }
                    return this.anythingElse(t2, tb);
                }
                default: {
                    return this.anythingElse(t2, tb);
                }
            }
            return true;
        }

        private boolean anythingElse(Token t2, HtmlTreeBuilder tb) {
            if (!tb.currentElementIs("colgroup")) {
                tb.error(this);
                return false;
            }
            tb.pop();
            tb.transition(InTable);
            tb.process(t2);
            return true;
        }
    }
    ,
    InTableBody{

        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            switch (t2.type) {
                case StartTag: {
                    Token.StartTag startTag = t2.asStartTag();
                    String name = startTag.normalName();
                    if (name.equals("tr")) {
                        tb.clearStackToTableBodyContext();
                        tb.insertElementFor(startTag);
                        tb.transition(InRow);
                        break;
                    }
                    if (StringUtil.inSorted(name, Constants.InCellNames)) {
                        tb.error(this);
                        tb.processStartTag("tr");
                        return tb.process(startTag);
                    }
                    if (StringUtil.inSorted(name, Constants.InTableBodyExit)) {
                        return this.exitTableBody(t2, tb);
                    }
                    return this.anythingElse(t2, tb);
                }
                case EndTag: {
                    Token.EndTag endTag = t2.asEndTag();
                    String name = endTag.normalName();
                    if (StringUtil.inSorted(name, Constants.InTableEndIgnore)) {
                        if (!tb.inTableScope(name)) {
                            tb.error(this);
                            return false;
                        }
                        tb.clearStackToTableBodyContext();
                        tb.pop();
                        tb.transition(InTable);
                        break;
                    }
                    if (name.equals("table")) {
                        return this.exitTableBody(t2, tb);
                    }
                    if (StringUtil.inSorted(name, Constants.InTableBodyEndIgnore)) {
                        tb.error(this);
                        return false;
                    }
                    return this.anythingElse(t2, tb);
                }
                default: {
                    return this.anythingElse(t2, tb);
                }
            }
            return true;
        }

        private boolean exitTableBody(Token t2, HtmlTreeBuilder tb) {
            if (!(tb.inTableScope("tbody") || tb.inTableScope("thead") || tb.inScope("tfoot"))) {
                tb.error(this);
                return false;
            }
            tb.clearStackToTableBodyContext();
            tb.processEndTag(tb.currentElement().normalName());
            return tb.process(t2);
        }

        private boolean anythingElse(Token t2, HtmlTreeBuilder tb) {
            return tb.process(t2, InTable);
        }
    }
    ,
    InRow{

        /*
         * Enabled aggressive block sorting
         */
        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            if (t2.isStartTag()) {
                Token.StartTag startTag = t2.asStartTag();
                String name = startTag.normalName();
                if (StringUtil.inSorted(name, Constants.InCellNames)) {
                    tb.clearStackToTableRowContext();
                    tb.insertElementFor(startTag);
                    tb.transition(InCell);
                    tb.insertMarkerToFormattingElements();
                    return true;
                }
                if (!StringUtil.inSorted(name, Constants.InRowMissing)) return this.anythingElse(t2, tb);
                if (!tb.inTableScope("tr")) {
                    tb.error(this);
                    return false;
                }
                tb.clearStackToTableRowContext();
                tb.pop();
                tb.transition(InTableBody);
                return tb.process(t2);
            }
            if (!t2.isEndTag()) return this.anythingElse(t2, tb);
            Token.EndTag endTag = t2.asEndTag();
            String name = endTag.normalName();
            if (name.equals("tr")) {
                if (!tb.inTableScope(name)) {
                    tb.error(this);
                    return false;
                }
                tb.clearStackToTableRowContext();
                tb.pop();
                tb.transition(InTableBody);
                return true;
            }
            if (name.equals("table")) {
                if (!tb.inTableScope("tr")) {
                    tb.error(this);
                    return false;
                }
                tb.clearStackToTableRowContext();
                tb.pop();
                tb.transition(InTableBody);
                return tb.process(t2);
            }
            if (!StringUtil.inSorted(name, Constants.InTableToBody)) {
                if (!StringUtil.inSorted(name, Constants.InRowIgnore)) return this.anythingElse(t2, tb);
                tb.error(this);
                return false;
            }
            if (!tb.inTableScope(name)) {
                tb.error(this);
                return false;
            }
            if (!tb.inTableScope("tr")) {
                return false;
            }
            tb.clearStackToTableRowContext();
            tb.pop();
            tb.transition(InTableBody);
            return tb.process(t2);
        }

        private boolean anythingElse(Token t2, HtmlTreeBuilder tb) {
            return tb.process(t2, InTable);
        }
    }
    ,
    InCell{

        /*
         * Enabled aggressive block sorting
         */
        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            if (t2.isEndTag()) {
                Token.EndTag endTag = t2.asEndTag();
                String name = endTag.normalName();
                if (StringUtil.inSorted(name, Constants.InCellNames)) {
                    if (!tb.inTableScope(name)) {
                        tb.error(this);
                        tb.transition(InRow);
                        return false;
                    }
                    tb.generateImpliedEndTags();
                    if (!tb.currentElementIs(name)) {
                        tb.error(this);
                    }
                    tb.popStackToClose(name);
                    tb.clearFormattingElementsToLastMarker();
                    tb.transition(InRow);
                    return true;
                }
                if (StringUtil.inSorted(name, Constants.InCellBody)) {
                    tb.error(this);
                    return false;
                }
                if (!StringUtil.inSorted(name, Constants.InCellTable)) return this.anythingElse(t2, tb);
                if (!tb.inTableScope(name)) {
                    tb.error(this);
                    return false;
                }
                this.closeCell(tb);
                return tb.process(t2);
            }
            if (!t2.isStartTag()) return this.anythingElse(t2, tb);
            if (!StringUtil.inSorted(t2.asStartTag().normalName(), Constants.InCellCol)) return this.anythingElse(t2, tb);
            if (!tb.inTableScope("td") && !tb.inTableScope("th")) {
                tb.error(this);
                return false;
            }
            this.closeCell(tb);
            return tb.process(t2);
        }

        private boolean anythingElse(Token t2, HtmlTreeBuilder tb) {
            return tb.process(t2, InBody);
        }

        private void closeCell(HtmlTreeBuilder tb) {
            if (tb.inTableScope("td")) {
                tb.processEndTag("td");
            } else {
                tb.processEndTag("th");
            }
        }
    }
    ,
    InSelect{

        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            block0 : switch (t2.type) {
                case Character: {
                    Token.Character c2 = t2.asCharacter();
                    if (c2.getData().equals(nullString)) {
                        tb.error(this);
                        return false;
                    }
                    tb.insertCharacterNode(c2);
                    break;
                }
                case Comment: {
                    tb.insertCommentNode(t2.asComment());
                    break;
                }
                case Doctype: {
                    tb.error(this);
                    return false;
                }
                case StartTag: {
                    Token.StartTag start = t2.asStartTag();
                    String name = start.normalName();
                    if (name.equals("html")) {
                        return tb.process(start, InBody);
                    }
                    if (name.equals("option")) {
                        if (tb.currentElementIs("option")) {
                            tb.processEndTag("option");
                        }
                        tb.insertElementFor(start);
                        break;
                    }
                    if (name.equals("optgroup")) {
                        if (tb.currentElementIs("option")) {
                            tb.processEndTag("option");
                        }
                        if (tb.currentElementIs("optgroup")) {
                            tb.processEndTag("optgroup");
                        }
                        tb.insertElementFor(start);
                        break;
                    }
                    if (name.equals("select")) {
                        tb.error(this);
                        return tb.processEndTag("select");
                    }
                    if (StringUtil.inSorted(name, Constants.InSelectEnd)) {
                        tb.error(this);
                        if (!tb.inSelectScope("select")) {
                            return false;
                        }
                        tb.processEndTag("select");
                        return tb.process(start);
                    }
                    if (name.equals("script") || name.equals("template")) {
                        return tb.process(t2, InHead);
                    }
                    return this.anythingElse(t2, tb);
                }
                case EndTag: {
                    String name;
                    Token.EndTag end = t2.asEndTag();
                    switch (name = end.normalName()) {
                        case "optgroup": {
                            if (tb.currentElementIs("option") && tb.aboveOnStack(tb.currentElement()) != null && tb.aboveOnStack(tb.currentElement()).nameIs("optgroup")) {
                                tb.processEndTag("option");
                            }
                            if (tb.currentElementIs("optgroup")) {
                                tb.pop();
                                break block0;
                            }
                            tb.error(this);
                            break block0;
                        }
                        case "option": {
                            if (tb.currentElementIs("option")) {
                                tb.pop();
                                break block0;
                            }
                            tb.error(this);
                            break block0;
                        }
                        case "select": {
                            if (!tb.inSelectScope(name)) {
                                tb.error(this);
                                return false;
                            }
                            tb.popStackToClose(name);
                            tb.resetInsertionMode();
                            break block0;
                        }
                        case "template": {
                            return tb.process(t2, InHead);
                        }
                    }
                    return this.anythingElse(t2, tb);
                }
                case EOF: {
                    if (tb.currentElementIs("html")) break;
                    tb.error(this);
                    break;
                }
                default: {
                    return this.anythingElse(t2, tb);
                }
            }
            return true;
        }

        private boolean anythingElse(Token t2, HtmlTreeBuilder tb) {
            tb.error(this);
            return false;
        }
    }
    ,
    InSelectInTable{

        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            if (t2.isStartTag() && StringUtil.inSorted(t2.asStartTag().normalName(), Constants.InSelectTableEnd)) {
                tb.error(this);
                tb.popStackToClose("select");
                tb.resetInsertionMode();
                return tb.process(t2);
            }
            if (t2.isEndTag() && StringUtil.inSorted(t2.asEndTag().normalName(), Constants.InSelectTableEnd)) {
                tb.error(this);
                if (tb.inTableScope(t2.asEndTag().normalName())) {
                    tb.popStackToClose("select");
                    tb.resetInsertionMode();
                    return tb.process(t2);
                }
                return false;
            }
            return tb.process(t2, InSelect);
        }
    }
    ,
    InTemplate{

        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            switch (t2.type) {
                case Comment: 
                case Doctype: 
                case Character: {
                    tb.process(t2, InBody);
                    break;
                }
                case StartTag: {
                    String name = t2.asStartTag().normalName();
                    if (StringUtil.inSorted(name, Constants.InTemplateToHead)) {
                        tb.process(t2, InHead);
                        break;
                    }
                    if (StringUtil.inSorted(name, Constants.InTemplateToTable)) {
                        tb.popTemplateMode();
                        tb.pushTemplateMode(InTable);
                        tb.transition(InTable);
                        return tb.process(t2);
                    }
                    if (name.equals("col")) {
                        tb.popTemplateMode();
                        tb.pushTemplateMode(InColumnGroup);
                        tb.transition(InColumnGroup);
                        return tb.process(t2);
                    }
                    if (name.equals("tr")) {
                        tb.popTemplateMode();
                        tb.pushTemplateMode(InTableBody);
                        tb.transition(InTableBody);
                        return tb.process(t2);
                    }
                    if (name.equals("td") || name.equals("th")) {
                        tb.popTemplateMode();
                        tb.pushTemplateMode(InRow);
                        tb.transition(InRow);
                        return tb.process(t2);
                    }
                    tb.popTemplateMode();
                    tb.pushTemplateMode(InBody);
                    tb.transition(InBody);
                    return tb.process(t2);
                }
                case EndTag: {
                    String name = t2.asEndTag().normalName();
                    if (name.equals("template")) {
                        tb.process(t2, InHead);
                        break;
                    }
                    tb.error(this);
                    return false;
                }
                case EOF: {
                    if (!tb.onStack("template")) {
                        return true;
                    }
                    tb.error(this);
                    tb.popStackToClose("template");
                    tb.clearFormattingElementsToLastMarker();
                    tb.popTemplateMode();
                    tb.resetInsertionMode();
                    if (tb.state() != InTemplate && tb.templateModeSize() < 12) {
                        return tb.process(t2);
                    }
                    return true;
                }
                default: {
                    Validate.wtf("Unexpected state: " + (Object)((Object)t2.type));
                }
            }
            return true;
        }
    }
    ,
    AfterBody{

        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            Element html = tb.getFromStack("html");
            if (HtmlTreeBuilderState.isWhitespace(t2)) {
                if (html != null) {
                    tb.insertCharacterToElement(t2.asCharacter(), html);
                } else {
                    tb.process(t2, InBody);
                }
            } else if (t2.isComment()) {
                tb.insertCommentNode(t2.asComment());
            } else {
                if (t2.isDoctype()) {
                    tb.error(this);
                    return false;
                }
                if (t2.isStartTag() && t2.asStartTag().normalName().equals("html")) {
                    return tb.process(t2, InBody);
                }
                if (t2.isEndTag() && t2.asEndTag().normalName().equals("html")) {
                    if (tb.isFragmentParsing()) {
                        tb.error(this);
                        return false;
                    }
                    if (html != null) {
                        tb.onNodeClosed(html);
                    }
                    tb.transition(AfterAfterBody);
                } else if (!t2.isEOF()) {
                    tb.error(this);
                    tb.resetBody();
                    return tb.process(t2);
                }
            }
            return true;
        }
    }
    ,
    InFrameset{

        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t2)) {
                tb.insertCharacterNode(t2.asCharacter());
            } else if (t2.isComment()) {
                tb.insertCommentNode(t2.asComment());
            } else {
                if (t2.isDoctype()) {
                    tb.error(this);
                    return false;
                }
                if (t2.isStartTag()) {
                    Token.StartTag start = t2.asStartTag();
                    switch (start.normalName()) {
                        case "html": {
                            return tb.process(start, InBody);
                        }
                        case "frameset": {
                            tb.insertElementFor(start);
                            break;
                        }
                        case "frame": {
                            tb.insertEmptyElementFor(start);
                            break;
                        }
                        case "noframes": {
                            return tb.process(start, InHead);
                        }
                        default: {
                            tb.error(this);
                            return false;
                        }
                    }
                } else if (t2.isEndTag() && t2.asEndTag().normalName().equals("frameset")) {
                    if (tb.currentElementIs("html")) {
                        tb.error(this);
                        return false;
                    }
                    tb.pop();
                    if (!tb.isFragmentParsing() && !tb.currentElementIs("frameset")) {
                        tb.transition(AfterFrameset);
                    }
                } else if (t2.isEOF()) {
                    if (!tb.currentElementIs("html")) {
                        tb.error(this);
                        return true;
                    }
                } else {
                    tb.error(this);
                    return false;
                }
            }
            return true;
        }
    }
    ,
    AfterFrameset{

        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t2)) {
                tb.insertCharacterNode(t2.asCharacter());
            } else if (t2.isComment()) {
                tb.insertCommentNode(t2.asComment());
            } else {
                if (t2.isDoctype()) {
                    tb.error(this);
                    return false;
                }
                if (t2.isStartTag() && t2.asStartTag().normalName().equals("html")) {
                    return tb.process(t2, InBody);
                }
                if (t2.isEndTag() && t2.asEndTag().normalName().equals("html")) {
                    tb.transition(AfterAfterFrameset);
                } else {
                    if (t2.isStartTag() && t2.asStartTag().normalName().equals("noframes")) {
                        return tb.process(t2, InHead);
                    }
                    if (!t2.isEOF()) {
                        tb.error(this);
                        return false;
                    }
                }
            }
            return true;
        }
    }
    ,
    AfterAfterBody{

        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            if (t2.isComment()) {
                tb.insertCommentNode(t2.asComment());
            } else {
                if (t2.isDoctype() || t2.isStartTag() && t2.asStartTag().normalName().equals("html")) {
                    return tb.process(t2, InBody);
                }
                if (HtmlTreeBuilderState.isWhitespace(t2)) {
                    Document doc = tb.getDocument();
                    tb.insertCharacterToElement(t2.asCharacter(), doc);
                } else if (!t2.isEOF()) {
                    tb.error(this);
                    tb.resetBody();
                    return tb.process(t2);
                }
            }
            return true;
        }
    }
    ,
    AfterAfterFrameset{

        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            if (t2.isComment()) {
                tb.insertCommentNode(t2.asComment());
            } else {
                if (t2.isDoctype() || HtmlTreeBuilderState.isWhitespace(t2) || t2.isStartTag() && t2.asStartTag().normalName().equals("html")) {
                    return tb.process(t2, InBody);
                }
                if (!t2.isEOF()) {
                    if (t2.isStartTag() && t2.asStartTag().normalName().equals("noframes")) {
                        return tb.process(t2, InHead);
                    }
                    tb.error(this);
                    return false;
                }
            }
            return true;
        }
    }
    ,
    ForeignContent{

        @Override
        boolean process(Token t2, HtmlTreeBuilder tb) {
            switch (t2.type) {
                case Character: {
                    Token.Character c2 = t2.asCharacter();
                    if (c2.getData().equals(nullString)) {
                        tb.error(this);
                        break;
                    }
                    if (HtmlTreeBuilderState.isWhitespace(c2)) {
                        tb.insertCharacterNode(c2);
                        break;
                    }
                    tb.insertCharacterNode(c2);
                    tb.framesetOk(false);
                    break;
                }
                case Comment: {
                    tb.insertCommentNode(t2.asComment());
                    break;
                }
                case Doctype: {
                    tb.error(this);
                    break;
                }
                case StartTag: {
                    Token.StartTag start = t2.asStartTag();
                    if (StringUtil.in(start.normalName, Constants.InForeignToHtml)) {
                        return this.processAsHtml(t2, tb);
                    }
                    if (start.normalName.equals("font") && (start.hasAttributeIgnoreCase("color") || start.hasAttributeIgnoreCase("face") || start.hasAttributeIgnoreCase("size"))) {
                        return this.processAsHtml(t2, tb);
                    }
                    tb.insertForeignElementFor(start, tb.currentElement().tag().namespace());
                    break;
                }
                case EndTag: {
                    int i2;
                    Element el;
                    Token.EndTag end = t2.asEndTag();
                    if (end.normalName.equals("br") || end.normalName.equals("p")) {
                        return this.processAsHtml(t2, tb);
                    }
                    if (end.normalName.equals("script") && tb.currentElementIs("script", "http://www.w3.org/2000/svg")) {
                        tb.pop();
                        return true;
                    }
                    ArrayList<Element> stack = tb.getStack();
                    if (stack.isEmpty()) {
                        Validate.wtf("Stack unexpectedly empty");
                    }
                    if (!(el = stack.get(i2 = stack.size() - 1)).nameIs(end.normalName)) {
                        tb.error(this);
                    }
                    while (i2 != 0) {
                        if (el.nameIs(end.normalName)) {
                            tb.popStackToCloseAnyNamespace(el.normalName());
                            return true;
                        }
                        if (!(el = stack.get(--i2)).tag().namespace().equals("http://www.w3.org/1999/xhtml")) continue;
                        return this.processAsHtml(t2, tb);
                    }
                    break;
                }
                case EOF: {
                    break;
                }
                default: {
                    Validate.wtf("Unexpected state: " + (Object)((Object)t2.type));
                }
            }
            return true;
        }

        boolean processAsHtml(Token t2, HtmlTreeBuilder tb) {
            return tb.state().process(t2, tb);
        }
    };

    private static final String nullString;

    private static void mergeAttributes(Token.StartTag source2, Element dest) {
        if (!source2.hasAttributes()) {
            return;
        }
        for (Attribute attr : source2.attributes) {
            Attributes destAttrs = dest.attributes();
            if (destAttrs.hasKey(attr.getKey())) continue;
            Range.AttributeRange range = attr.sourceRange();
            destAttrs.put(attr);
            if (!source2.trackSource) continue;
            destAttrs.sourceRange(attr.getKey(), range);
        }
    }

    abstract boolean process(Token var1, HtmlTreeBuilder var2);

    private static boolean isWhitespace(Token t2) {
        if (t2.isCharacter()) {
            String data = t2.asCharacter().getData();
            return StringUtil.isBlank(data);
        }
        return false;
    }

    private static void handleRcData(Token.StartTag startTag, HtmlTreeBuilder tb) {
        tb.tokeniser.transition(TokeniserState.Rcdata);
        tb.markInsertionMode();
        tb.transition(Text);
        tb.insertElementFor(startTag);
    }

    private static void handleRawtext(Token.StartTag startTag, HtmlTreeBuilder tb) {
        tb.tokeniser.transition(TokeniserState.Rawtext);
        tb.markInsertionMode();
        tb.transition(Text);
        tb.insertElementFor(startTag);
    }

    static {
        nullString = String.valueOf('\u0000');
    }

    static final class Constants {
        static final String[] InHeadEmpty = new String[]{"base", "basefont", "bgsound", "command", "link"};
        static final String[] InHeadRaw = new String[]{"noframes", "style"};
        static final String[] InHeadEnd = new String[]{"body", "br", "html"};
        static final String[] AfterHeadBody = new String[]{"body", "br", "html"};
        static final String[] BeforeHtmlToHead = new String[]{"body", "br", "head", "html"};
        static final String[] InHeadNoScriptHead = new String[]{"basefont", "bgsound", "link", "meta", "noframes", "style"};
        static final String[] InBodyStartToHead = new String[]{"base", "basefont", "bgsound", "command", "link", "meta", "noframes", "script", "style", "template", "title"};
        static final String[] InBodyStartPClosers = new String[]{"address", "article", "aside", "blockquote", "center", "details", "dir", "div", "dl", "fieldset", "figcaption", "figure", "footer", "header", "hgroup", "menu", "nav", "ol", "p", "section", "summary", "ul"};
        static final String[] Headings = new String[]{"h1", "h2", "h3", "h4", "h5", "h6"};
        static final String[] InBodyStartLiBreakers = new String[]{"address", "div", "p"};
        static final String[] DdDt = new String[]{"dd", "dt"};
        static final String[] InBodyStartApplets = new String[]{"applet", "marquee", "object"};
        static final String[] InBodyStartMedia = new String[]{"param", "source", "track"};
        static final String[] InBodyStartInputAttribs = new String[]{"action", "name", "prompt"};
        static final String[] InBodyStartDrop = new String[]{"caption", "col", "colgroup", "frame", "head", "tbody", "td", "tfoot", "th", "thead", "tr"};
        static final String[] InBodyEndClosers = new String[]{"address", "article", "aside", "blockquote", "button", "center", "details", "dir", "div", "dl", "fieldset", "figcaption", "figure", "footer", "header", "hgroup", "listing", "menu", "nav", "ol", "pre", "section", "summary", "ul"};
        static final String[] InBodyEndOtherErrors = new String[]{"body", "dd", "dt", "html", "li", "optgroup", "option", "p", "rb", "rp", "rt", "rtc", "tbody", "td", "tfoot", "th", "thead", "tr"};
        static final String[] InBodyEndAdoptionFormatters = new String[]{"a", "b", "big", "code", "em", "font", "i", "nobr", "s", "small", "strike", "strong", "tt", "u"};
        static final String[] InBodyEndTableFosters = new String[]{"table", "tbody", "tfoot", "thead", "tr"};
        static final String[] InTableToBody = new String[]{"tbody", "tfoot", "thead"};
        static final String[] InTableAddBody = new String[]{"td", "th", "tr"};
        static final String[] InTableToHead = new String[]{"script", "style", "template"};
        static final String[] InCellNames = new String[]{"td", "th"};
        static final String[] InCellBody = new String[]{"body", "caption", "col", "colgroup", "html"};
        static final String[] InCellTable = new String[]{"table", "tbody", "tfoot", "thead", "tr"};
        static final String[] InCellCol = new String[]{"caption", "col", "colgroup", "tbody", "td", "tfoot", "th", "thead", "tr"};
        static final String[] InTableEndErr = new String[]{"body", "caption", "col", "colgroup", "html", "tbody", "td", "tfoot", "th", "thead", "tr"};
        static final String[] InTableFoster = new String[]{"table", "tbody", "tfoot", "thead", "tr"};
        static final String[] InTableBodyExit = new String[]{"caption", "col", "colgroup", "tbody", "tfoot", "thead"};
        static final String[] InTableBodyEndIgnore = new String[]{"body", "caption", "col", "colgroup", "html", "td", "th", "tr"};
        static final String[] InRowMissing = new String[]{"caption", "col", "colgroup", "tbody", "tfoot", "thead", "tr"};
        static final String[] InRowIgnore = new String[]{"body", "caption", "col", "colgroup", "html", "td", "th"};
        static final String[] InSelectEnd = new String[]{"input", "keygen", "textarea"};
        static final String[] InSelectTableEnd = new String[]{"caption", "table", "tbody", "td", "tfoot", "th", "thead", "tr"};
        static final String[] InTableEndIgnore = new String[]{"tbody", "tfoot", "thead"};
        static final String[] InHeadNoscriptIgnore = new String[]{"head", "noscript"};
        static final String[] InCaptionIgnore = new String[]{"body", "col", "colgroup", "html", "tbody", "td", "tfoot", "th", "thead", "tr"};
        static final String[] InTemplateToHead = new String[]{"base", "basefont", "bgsound", "link", "meta", "noframes", "script", "style", "template", "title"};
        static final String[] InTemplateToTable = new String[]{"caption", "colgroup", "tbody", "tfoot", "thead"};
        static final String[] InForeignToHtml = new String[]{"b", "big", "blockquote", "body", "br", "center", "code", "dd", "div", "dl", "dt", "em", "embed", "h1", "h2", "h3", "h4", "h5", "h6", "head", "hr", "i", "img", "li", "listing", "menu", "meta", "nobr", "ol", "p", "pre", "ruby", "s", "small", "span", "strike", "strong", "sub", "sup", "table", "tt", "u", "ul", "var"};

        Constants() {
        }
    }
}

