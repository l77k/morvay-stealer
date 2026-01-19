/*
 * Decompiled with CFR 0.152.
 */
package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;

enum TokeniserState {
    Data{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            switch (r2.current()) {
                case '&': {
                    t2.advanceTransition(CharacterReferenceInData);
                    break;
                }
                case '<': {
                    t2.advanceTransition(TagOpen);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    t2.emit(r2.consume());
                    break;
                }
                case '\uffff': {
                    t2.emit(new Token.EOF());
                    break;
                }
                default: {
                    String data = r2.consumeData();
                    t2.emit(data);
                }
            }
        }
    }
    ,
    CharacterReferenceInData{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            TokeniserState.readCharRef(t2, 2.Data);
        }
    }
    ,
    Rcdata{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            switch (r2.current()) {
                case '&': {
                    t2.advanceTransition(CharacterReferenceInRcdata);
                    break;
                }
                case '<': {
                    t2.advanceTransition(RcdataLessthanSign);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    r2.advance();
                    t2.emit('\ufffd');
                    break;
                }
                case '\uffff': {
                    t2.emit(new Token.EOF());
                    break;
                }
                default: {
                    String data = r2.consumeData();
                    t2.emit(data);
                }
            }
        }
    }
    ,
    CharacterReferenceInRcdata{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            TokeniserState.readCharRef(t2, 4.Rcdata);
        }
    }
    ,
    Rawtext{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            TokeniserState.readRawData(t2, r2, (TokeniserState)this, 5.RawtextLessthanSign);
        }
    }
    ,
    ScriptData{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            TokeniserState.readRawData(t2, r2, (TokeniserState)this, 6.ScriptDataLessthanSign);
        }
    }
    ,
    PLAINTEXT{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            switch (r2.current()) {
                case '\u0000': {
                    t2.error(this);
                    r2.advance();
                    t2.emit('\ufffd');
                    break;
                }
                case '\uffff': {
                    t2.emit(new Token.EOF());
                    break;
                }
                default: {
                    String data = r2.consumeTo('\u0000');
                    t2.emit(data);
                }
            }
        }
    }
    ,
    TagOpen{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            switch (r2.current()) {
                case '!': {
                    t2.advanceTransition(MarkupDeclarationOpen);
                    break;
                }
                case '/': {
                    t2.advanceTransition(EndTagOpen);
                    break;
                }
                case '?': {
                    if (t2.syntax == Document.OutputSettings.Syntax.xml) {
                        t2.advanceTransition(MarkupProcessingOpen);
                        break;
                    }
                    t2.createBogusCommentPending();
                    t2.transition(BogusComment);
                    break;
                }
                default: {
                    if (r2.matchesAsciiAlpha()) {
                        t2.createTagPending(true);
                        t2.transition(TagName);
                        break;
                    }
                    t2.error(this);
                    t2.emit('<');
                    t2.transition(Data);
                }
            }
        }
    }
    ,
    EndTagOpen{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            if (r2.isEmpty()) {
                t2.eofError(this);
                t2.emit("</");
                t2.transition(Data);
            } else if (r2.matchesAsciiAlpha()) {
                t2.createTagPending(false);
                t2.transition(TagName);
            } else if (r2.matches('>')) {
                t2.error(this);
                t2.advanceTransition(Data);
            } else {
                t2.error(this);
                t2.createBogusCommentPending();
                t2.commentPending.append('/');
                t2.transition(BogusComment);
            }
        }
    }
    ,
    TagName{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            String tagName = r2.consumeTagName();
            t2.tagPending.appendTagName(tagName);
            char c2 = r2.consume();
            switch (c2) {
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': {
                    t2.transition(BeforeAttributeName);
                    break;
                }
                case '/': {
                    t2.transition(SelfClosingStartTag);
                    break;
                }
                case '>': {
                    t2.emitTagPending();
                    t2.transition(Data);
                    break;
                }
                case '\u0000': {
                    t2.tagPending.appendTagName(replacementStr);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.tagPending.appendTagName(c2);
                }
            }
        }
    }
    ,
    RcdataLessthanSign{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            if (r2.matches('/')) {
                t2.createTempBuffer();
                t2.advanceTransition(RCDATAEndTagOpen);
            } else if (r2.readFully() && r2.matchesAsciiAlpha() && t2.appropriateEndTagName() != null && !r2.containsIgnoreCase(t2.appropriateEndTagSeq())) {
                t2.tagPending = t2.createTagPending(false).name(t2.appropriateEndTagName());
                t2.emitTagPending();
                t2.transition(TagOpen);
            } else {
                t2.emit("<");
                t2.transition(Rcdata);
            }
        }
    }
    ,
    RCDATAEndTagOpen{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            if (r2.matchesAsciiAlpha()) {
                t2.createTagPending(false);
                t2.tagPending.appendTagName(r2.current());
                t2.dataBuffer.append(r2.current());
                t2.advanceTransition(RCDATAEndTagName);
            } else {
                t2.emit("</");
                t2.transition(Rcdata);
            }
        }
    }
    ,
    RCDATAEndTagName{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            if (r2.matchesAsciiAlpha()) {
                String name = r2.consumeLetterSequence();
                t2.tagPending.appendTagName(name);
                t2.dataBuffer.append(name);
                return;
            }
            char c2 = r2.consume();
            switch (c2) {
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': {
                    if (t2.isAppropriateEndTagToken()) {
                        t2.transition(BeforeAttributeName);
                        break;
                    }
                    this.anythingElse(t2, r2);
                    break;
                }
                case '/': {
                    if (t2.isAppropriateEndTagToken()) {
                        t2.transition(SelfClosingStartTag);
                        break;
                    }
                    this.anythingElse(t2, r2);
                    break;
                }
                case '>': {
                    if (t2.isAppropriateEndTagToken()) {
                        t2.emitTagPending();
                        t2.transition(Data);
                        break;
                    }
                    this.anythingElse(t2, r2);
                    break;
                }
                default: {
                    this.anythingElse(t2, r2);
                }
            }
        }

        private void anythingElse(Tokeniser t2, CharacterReader r2) {
            t2.emit("</");
            t2.emit(t2.dataBuffer);
            r2.unconsume();
            t2.transition(Rcdata);
        }
    }
    ,
    RawtextLessthanSign{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            if (r2.matches('/')) {
                t2.createTempBuffer();
                t2.advanceTransition(RawtextEndTagOpen);
            } else {
                t2.emit('<');
                t2.transition(Rawtext);
            }
        }
    }
    ,
    RawtextEndTagOpen{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            TokeniserState.readEndTag(t2, r2, 15.RawtextEndTagName, 15.Rawtext);
        }
    }
    ,
    RawtextEndTagName{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            TokeniserState.handleDataEndTag(t2, r2, 16.Rawtext);
        }
    }
    ,
    ScriptDataLessthanSign{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            switch (r2.consume()) {
                case '/': {
                    t2.createTempBuffer();
                    t2.transition(ScriptDataEndTagOpen);
                    break;
                }
                case '!': {
                    t2.emit("<!");
                    t2.transition(ScriptDataEscapeStart);
                    break;
                }
                case '\uffff': {
                    t2.emit("<");
                    t2.eofError(this);
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.emit("<");
                    r2.unconsume();
                    t2.transition(ScriptData);
                }
            }
        }
    }
    ,
    ScriptDataEndTagOpen{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            TokeniserState.readEndTag(t2, r2, 18.ScriptDataEndTagName, 18.ScriptData);
        }
    }
    ,
    ScriptDataEndTagName{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            TokeniserState.handleDataEndTag(t2, r2, 19.ScriptData);
        }
    }
    ,
    ScriptDataEscapeStart{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            if (r2.matches('-')) {
                t2.emit('-');
                t2.advanceTransition(ScriptDataEscapeStartDash);
            } else {
                t2.transition(ScriptData);
            }
        }
    }
    ,
    ScriptDataEscapeStartDash{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            if (r2.matches('-')) {
                t2.emit('-');
                t2.advanceTransition(ScriptDataEscapedDashDash);
            } else {
                t2.transition(ScriptData);
            }
        }
    }
    ,
    ScriptDataEscaped{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            if (r2.isEmpty()) {
                t2.eofError(this);
                t2.transition(Data);
                return;
            }
            switch (r2.current()) {
                case '-': {
                    t2.emit('-');
                    t2.advanceTransition(ScriptDataEscapedDash);
                    break;
                }
                case '<': {
                    t2.advanceTransition(ScriptDataEscapedLessthanSign);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    r2.advance();
                    t2.emit('\ufffd');
                    break;
                }
                default: {
                    String data = r2.consumeToAny('-', '<', '\u0000');
                    t2.emit(data);
                }
            }
        }
    }
    ,
    ScriptDataEscapedDash{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            if (r2.isEmpty()) {
                t2.eofError(this);
                t2.transition(Data);
                return;
            }
            char c2 = r2.consume();
            switch (c2) {
                case '-': {
                    t2.emit(c2);
                    t2.transition(ScriptDataEscapedDashDash);
                    break;
                }
                case '<': {
                    t2.transition(ScriptDataEscapedLessthanSign);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    t2.emit('\ufffd');
                    t2.transition(ScriptDataEscaped);
                    break;
                }
                default: {
                    t2.emit(c2);
                    t2.transition(ScriptDataEscaped);
                }
            }
        }
    }
    ,
    ScriptDataEscapedDashDash{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            if (r2.isEmpty()) {
                t2.eofError(this);
                t2.transition(Data);
                return;
            }
            char c2 = r2.consume();
            switch (c2) {
                case '-': {
                    t2.emit(c2);
                    break;
                }
                case '<': {
                    t2.transition(ScriptDataEscapedLessthanSign);
                    break;
                }
                case '>': {
                    t2.emit(c2);
                    t2.transition(ScriptData);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    t2.emit('\ufffd');
                    t2.transition(ScriptDataEscaped);
                    break;
                }
                default: {
                    t2.emit(c2);
                    t2.transition(ScriptDataEscaped);
                }
            }
        }
    }
    ,
    ScriptDataEscapedLessthanSign{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            if (r2.matchesAsciiAlpha()) {
                t2.createTempBuffer();
                t2.dataBuffer.append(r2.current());
                t2.emit("<");
                t2.emit(r2.current());
                t2.advanceTransition(ScriptDataDoubleEscapeStart);
            } else if (r2.matches('/')) {
                t2.createTempBuffer();
                t2.advanceTransition(ScriptDataEscapedEndTagOpen);
            } else {
                t2.emit('<');
                t2.transition(ScriptDataEscaped);
            }
        }
    }
    ,
    ScriptDataEscapedEndTagOpen{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            if (r2.matchesAsciiAlpha()) {
                t2.createTagPending(false);
                t2.tagPending.appendTagName(r2.current());
                t2.dataBuffer.append(r2.current());
                t2.advanceTransition(ScriptDataEscapedEndTagName);
            } else {
                t2.emit("</");
                t2.transition(ScriptDataEscaped);
            }
        }
    }
    ,
    ScriptDataEscapedEndTagName{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            TokeniserState.handleDataEndTag(t2, r2, 27.ScriptDataEscaped);
        }
    }
    ,
    ScriptDataDoubleEscapeStart{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            TokeniserState.handleDataDoubleEscapeTag(t2, r2, 28.ScriptDataDoubleEscaped, 28.ScriptDataEscaped);
        }
    }
    ,
    ScriptDataDoubleEscaped{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.current();
            switch (c2) {
                case '-': {
                    t2.emit(c2);
                    t2.advanceTransition(ScriptDataDoubleEscapedDash);
                    break;
                }
                case '<': {
                    t2.emit(c2);
                    t2.advanceTransition(ScriptDataDoubleEscapedLessthanSign);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    r2.advance();
                    t2.emit('\ufffd');
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.transition(Data);
                    break;
                }
                default: {
                    String data = r2.consumeToAny('-', '<', '\u0000');
                    t2.emit(data);
                }
            }
        }
    }
    ,
    ScriptDataDoubleEscapedDash{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '-': {
                    t2.emit(c2);
                    t2.transition(ScriptDataDoubleEscapedDashDash);
                    break;
                }
                case '<': {
                    t2.emit(c2);
                    t2.transition(ScriptDataDoubleEscapedLessthanSign);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    t2.emit('\ufffd');
                    t2.transition(ScriptDataDoubleEscaped);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.emit(c2);
                    t2.transition(ScriptDataDoubleEscaped);
                }
            }
        }
    }
    ,
    ScriptDataDoubleEscapedDashDash{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '-': {
                    t2.emit(c2);
                    break;
                }
                case '<': {
                    t2.emit(c2);
                    t2.transition(ScriptDataDoubleEscapedLessthanSign);
                    break;
                }
                case '>': {
                    t2.emit(c2);
                    t2.transition(ScriptData);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    t2.emit('\ufffd');
                    t2.transition(ScriptDataDoubleEscaped);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.emit(c2);
                    t2.transition(ScriptDataDoubleEscaped);
                }
            }
        }
    }
    ,
    ScriptDataDoubleEscapedLessthanSign{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            if (r2.matches('/')) {
                t2.emit('/');
                t2.createTempBuffer();
                t2.advanceTransition(ScriptDataDoubleEscapeEnd);
            } else {
                t2.transition(ScriptDataDoubleEscaped);
            }
        }
    }
    ,
    ScriptDataDoubleEscapeEnd{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            TokeniserState.handleDataDoubleEscapeTag(t2, r2, 33.ScriptDataEscaped, 33.ScriptDataDoubleEscaped);
        }
    }
    ,
    BeforeAttributeName{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': {
                    break;
                }
                case '/': {
                    t2.transition(SelfClosingStartTag);
                    break;
                }
                case '>': {
                    t2.emitTagPending();
                    t2.transition(Data);
                    break;
                }
                case '\u0000': {
                    r2.unconsume();
                    t2.error(this);
                    t2.tagPending.newAttribute();
                    t2.transition(AttributeName);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.transition(Data);
                    break;
                }
                case '\"': 
                case '\'': 
                case '=': {
                    t2.error(this);
                    t2.tagPending.newAttribute();
                    t2.tagPending.appendAttributeName(c2, r2.pos() - 1, r2.pos());
                    t2.transition(AttributeName);
                    break;
                }
                case '?': {
                    if (t2.tagPending instanceof Token.XmlDecl) break;
                }
                default: {
                    t2.tagPending.newAttribute();
                    r2.unconsume();
                    t2.transition(AttributeName);
                }
            }
        }
    }
    ,
    AttributeName{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            int pos = r2.pos();
            String name = r2.consumeToAnySorted(attributeNameCharsSorted);
            t2.tagPending.appendAttributeName(name, pos, r2.pos());
            pos = r2.pos();
            char c2 = r2.consume();
            switch (c2) {
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': {
                    t2.transition(AfterAttributeName);
                    break;
                }
                case '/': {
                    t2.transition(SelfClosingStartTag);
                    break;
                }
                case '=': {
                    t2.transition(BeforeAttributeValue);
                    break;
                }
                case '>': {
                    t2.emitTagPending();
                    t2.transition(Data);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.transition(Data);
                    break;
                }
                case '\"': 
                case '\'': 
                case '<': {
                    t2.error(this);
                    t2.tagPending.appendAttributeName(c2, pos, r2.pos());
                    break;
                }
                case '?': {
                    if (t2.syntax == Document.OutputSettings.Syntax.xml && t2.tagPending instanceof Token.XmlDecl) {
                        t2.transition(AfterAttributeName);
                        break;
                    }
                }
                default: {
                    t2.tagPending.appendAttributeName(c2, pos, r2.pos());
                }
            }
        }
    }
    ,
    AfterAttributeName{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': {
                    break;
                }
                case '/': {
                    t2.transition(SelfClosingStartTag);
                    break;
                }
                case '=': {
                    t2.transition(BeforeAttributeValue);
                    break;
                }
                case '>': {
                    t2.emitTagPending();
                    t2.transition(Data);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    t2.tagPending.appendAttributeName('\ufffd', r2.pos() - 1, r2.pos());
                    t2.transition(AttributeName);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.transition(Data);
                    break;
                }
                case '\"': 
                case '\'': 
                case '<': {
                    t2.error(this);
                    t2.tagPending.newAttribute();
                    t2.tagPending.appendAttributeName(c2, r2.pos() - 1, r2.pos());
                    t2.transition(AttributeName);
                    break;
                }
                default: {
                    t2.tagPending.newAttribute();
                    r2.unconsume();
                    t2.transition(AttributeName);
                }
            }
        }
    }
    ,
    BeforeAttributeValue{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': {
                    break;
                }
                case '\"': {
                    t2.transition(AttributeValue_doubleQuoted);
                    break;
                }
                case '&': {
                    r2.unconsume();
                    t2.transition(AttributeValue_unquoted);
                    break;
                }
                case '\'': {
                    t2.transition(AttributeValue_singleQuoted);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    t2.tagPending.appendAttributeValue('\ufffd', r2.pos() - 1, r2.pos());
                    t2.transition(AttributeValue_unquoted);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.emitTagPending();
                    t2.transition(Data);
                    break;
                }
                case '>': {
                    t2.error(this);
                    t2.emitTagPending();
                    t2.transition(Data);
                    break;
                }
                case '<': 
                case '=': 
                case '`': {
                    t2.error(this);
                    t2.tagPending.appendAttributeValue(c2, r2.pos() - 1, r2.pos());
                    t2.transition(AttributeValue_unquoted);
                    break;
                }
                default: {
                    r2.unconsume();
                    t2.transition(AttributeValue_unquoted);
                }
            }
        }
    }
    ,
    AttributeValue_doubleQuoted{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            int pos = r2.pos();
            String value = r2.consumeAttributeQuoted(false);
            if (value.length() > 0) {
                t2.tagPending.appendAttributeValue(value, pos, r2.pos());
            } else {
                t2.tagPending.setEmptyAttributeValue();
            }
            pos = r2.pos();
            char c2 = r2.consume();
            switch (c2) {
                case '\"': {
                    t2.transition(AfterAttributeValue_quoted);
                    break;
                }
                case '&': {
                    int[] ref = t2.consumeCharacterReference(Character.valueOf('\"'), true);
                    if (ref != null) {
                        t2.tagPending.appendAttributeValue(ref, pos, r2.pos());
                        break;
                    }
                    t2.tagPending.appendAttributeValue('&', pos, r2.pos());
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    t2.tagPending.appendAttributeValue('\ufffd', pos, r2.pos());
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.tagPending.appendAttributeValue(c2, pos, r2.pos());
                }
            }
        }
    }
    ,
    AttributeValue_singleQuoted{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            int pos = r2.pos();
            String value = r2.consumeAttributeQuoted(true);
            if (value.length() > 0) {
                t2.tagPending.appendAttributeValue(value, pos, r2.pos());
            } else {
                t2.tagPending.setEmptyAttributeValue();
            }
            pos = r2.pos();
            char c2 = r2.consume();
            switch (c2) {
                case '\'': {
                    t2.transition(AfterAttributeValue_quoted);
                    break;
                }
                case '&': {
                    int[] ref = t2.consumeCharacterReference(Character.valueOf('\''), true);
                    if (ref != null) {
                        t2.tagPending.appendAttributeValue(ref, pos, r2.pos());
                        break;
                    }
                    t2.tagPending.appendAttributeValue('&', pos, r2.pos());
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    t2.tagPending.appendAttributeValue('\ufffd', pos, r2.pos());
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.tagPending.appendAttributeValue(c2, pos, r2.pos());
                }
            }
        }
    }
    ,
    AttributeValue_unquoted{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            int pos = r2.pos();
            String value = r2.consumeToAnySorted(attributeValueUnquoted);
            if (value.length() > 0) {
                t2.tagPending.appendAttributeValue(value, pos, r2.pos());
            }
            pos = r2.pos();
            char c2 = r2.consume();
            switch (c2) {
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': {
                    t2.transition(BeforeAttributeName);
                    break;
                }
                case '&': {
                    int[] ref = t2.consumeCharacterReference(Character.valueOf('>'), true);
                    if (ref != null) {
                        t2.tagPending.appendAttributeValue(ref, pos, r2.pos());
                        break;
                    }
                    t2.tagPending.appendAttributeValue('&', pos, r2.pos());
                    break;
                }
                case '>': {
                    t2.emitTagPending();
                    t2.transition(Data);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    t2.tagPending.appendAttributeValue('\ufffd', pos, r2.pos());
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.transition(Data);
                    break;
                }
                case '\"': 
                case '\'': 
                case '<': 
                case '=': 
                case '`': {
                    t2.error(this);
                    t2.tagPending.appendAttributeValue(c2, pos, r2.pos());
                    break;
                }
                default: {
                    t2.tagPending.appendAttributeValue(c2, pos, r2.pos());
                }
            }
        }
    }
    ,
    AfterAttributeValue_quoted{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': {
                    t2.transition(BeforeAttributeName);
                    break;
                }
                case '/': {
                    t2.transition(SelfClosingStartTag);
                    break;
                }
                case '>': {
                    t2.emitTagPending();
                    t2.transition(Data);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.transition(Data);
                    break;
                }
                default: {
                    r2.unconsume();
                    t2.error(this);
                    t2.transition(BeforeAttributeName);
                }
            }
        }
    }
    ,
    SelfClosingStartTag{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '>': {
                    t2.tagPending.selfClosing = true;
                    t2.emitTagPending();
                    t2.transition(Data);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.transition(Data);
                    break;
                }
                default: {
                    r2.unconsume();
                    t2.error(this);
                    t2.transition(BeforeAttributeName);
                }
            }
        }
    }
    ,
    BogusComment{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            t2.commentPending.append(r2.consumeTo('>'));
            char next = r2.current();
            if (next == '>' || next == '\uffff') {
                r2.consume();
                t2.emitCommentPending();
                t2.transition(Data);
            }
        }
    }
    ,
    MarkupDeclarationOpen{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            if (r2.matchConsume("--")) {
                t2.createCommentPending();
                t2.transition(CommentStart);
            } else if (r2.matchConsumeIgnoreCase("DOCTYPE")) {
                t2.transition(Doctype);
            } else if (r2.matchConsume("[CDATA[")) {
                t2.createTempBuffer();
                t2.transition(CdataSection);
            } else if (t2.syntax == Document.OutputSettings.Syntax.xml && r2.matchesAsciiAlpha()) {
                t2.createXmlDeclPending(true);
                t2.transition(TagName);
            } else {
                t2.error(this);
                t2.createBogusCommentPending();
                t2.transition(BogusComment);
            }
        }
    }
    ,
    MarkupProcessingOpen{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            if (r2.matchesAsciiAlpha()) {
                t2.createXmlDeclPending(false);
                t2.transition(TagName);
            } else {
                t2.error(this);
                t2.createBogusCommentPending();
                t2.commentPending.append('?');
                t2.transition(BogusComment);
            }
        }
    }
    ,
    CommentStart{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '-': {
                    t2.transition(CommentStartDash);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    t2.commentPending.append('\ufffd');
                    t2.transition(Comment);
                    break;
                }
                case '>': {
                    t2.error(this);
                    t2.emitCommentPending();
                    t2.transition(Data);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.emitCommentPending();
                    t2.transition(Data);
                    break;
                }
                default: {
                    r2.unconsume();
                    t2.transition(Comment);
                }
            }
        }
    }
    ,
    CommentStartDash{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '-': {
                    t2.transition(CommentEnd);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    t2.commentPending.append('\ufffd');
                    t2.transition(Comment);
                    break;
                }
                case '>': {
                    t2.error(this);
                    t2.emitCommentPending();
                    t2.transition(Data);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.emitCommentPending();
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.commentPending.append(c2);
                    t2.transition(Comment);
                }
            }
        }
    }
    ,
    Comment{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.current();
            switch (c2) {
                case '-': {
                    t2.advanceTransition(CommentEndDash);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    r2.advance();
                    t2.commentPending.append('\ufffd');
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.emitCommentPending();
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.commentPending.append(r2.consumeToAny('-', '\u0000'));
                }
            }
        }
    }
    ,
    CommentEndDash{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '-': {
                    t2.transition(CommentEnd);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    t2.commentPending.append('-').append('\ufffd');
                    t2.transition(Comment);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.emitCommentPending();
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.commentPending.append('-').append(c2);
                    t2.transition(Comment);
                }
            }
        }
    }
    ,
    CommentEnd{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '>': {
                    t2.emitCommentPending();
                    t2.transition(Data);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    t2.commentPending.append("--").append('\ufffd');
                    t2.transition(Comment);
                    break;
                }
                case '!': {
                    t2.transition(CommentEndBang);
                    break;
                }
                case '-': {
                    t2.commentPending.append('-');
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.emitCommentPending();
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.commentPending.append("--").append(c2);
                    t2.transition(Comment);
                }
            }
        }
    }
    ,
    CommentEndBang{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '-': {
                    t2.commentPending.append("--!");
                    t2.transition(CommentEndDash);
                    break;
                }
                case '>': {
                    t2.emitCommentPending();
                    t2.transition(Data);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    t2.commentPending.append("--!").append('\ufffd');
                    t2.transition(Comment);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.emitCommentPending();
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.commentPending.append("--!").append(c2);
                    t2.transition(Comment);
                }
            }
        }
    }
    ,
    Doctype{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': {
                    t2.transition(BeforeDoctypeName);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                }
                case '>': {
                    t2.error(this);
                    t2.createDoctypePending();
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.error(this);
                    t2.transition(BeforeDoctypeName);
                }
            }
        }
    }
    ,
    BeforeDoctypeName{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            if (r2.matchesAsciiAlpha()) {
                t2.createDoctypePending();
                t2.transition(DoctypeName);
                return;
            }
            char c2 = r2.consume();
            switch (c2) {
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': {
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    t2.createDoctypePending();
                    t2.doctypePending.name.append('\ufffd');
                    t2.transition(DoctypeName);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.createDoctypePending();
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.createDoctypePending();
                    t2.doctypePending.name.append(c2);
                    t2.transition(DoctypeName);
                }
            }
        }
    }
    ,
    DoctypeName{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            if (r2.matchesLetter()) {
                String name = r2.consumeLetterSequence();
                t2.doctypePending.name.append(name);
                return;
            }
            char c2 = r2.consume();
            switch (c2) {
                case '>': {
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': {
                    t2.transition(AfterDoctypeName);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    t2.doctypePending.name.append('\ufffd');
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.doctypePending.name.append(c2);
                }
            }
        }
    }
    ,
    AfterDoctypeName{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            if (r2.isEmpty()) {
                t2.eofError(this);
                t2.doctypePending.forceQuirks = true;
                t2.emitDoctypePending();
                t2.transition(Data);
                return;
            }
            if (r2.matchesAny('\t', '\n', '\r', '\f', ' ')) {
                r2.advance();
            } else if (r2.matches('>')) {
                t2.emitDoctypePending();
                t2.advanceTransition(Data);
            } else if (r2.matchConsumeIgnoreCase("PUBLIC")) {
                t2.doctypePending.pubSysKey = "PUBLIC";
                t2.transition(AfterDoctypePublicKeyword);
            } else if (r2.matchConsumeIgnoreCase("SYSTEM")) {
                t2.doctypePending.pubSysKey = "SYSTEM";
                t2.transition(AfterDoctypeSystemKeyword);
            } else {
                t2.error(this);
                t2.doctypePending.forceQuirks = true;
                t2.advanceTransition(BogusDoctype);
            }
        }
    }
    ,
    AfterDoctypePublicKeyword{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': {
                    t2.transition(BeforeDoctypePublicIdentifier);
                    break;
                }
                case '\"': {
                    t2.error(this);
                    t2.transition(DoctypePublicIdentifier_doubleQuoted);
                    break;
                }
                case '\'': {
                    t2.error(this);
                    t2.transition(DoctypePublicIdentifier_singleQuoted);
                    break;
                }
                case '>': {
                    t2.error(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.error(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.transition(BogusDoctype);
                }
            }
        }
    }
    ,
    BeforeDoctypePublicIdentifier{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': {
                    break;
                }
                case '\"': {
                    t2.transition(DoctypePublicIdentifier_doubleQuoted);
                    break;
                }
                case '\'': {
                    t2.transition(DoctypePublicIdentifier_singleQuoted);
                    break;
                }
                case '>': {
                    t2.error(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.error(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.transition(BogusDoctype);
                }
            }
        }
    }
    ,
    DoctypePublicIdentifier_doubleQuoted{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '\"': {
                    t2.transition(AfterDoctypePublicIdentifier);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    t2.doctypePending.publicIdentifier.append('\ufffd');
                    break;
                }
                case '>': {
                    t2.error(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.doctypePending.publicIdentifier.append(c2);
                }
            }
        }
    }
    ,
    DoctypePublicIdentifier_singleQuoted{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '\'': {
                    t2.transition(AfterDoctypePublicIdentifier);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    t2.doctypePending.publicIdentifier.append('\ufffd');
                    break;
                }
                case '>': {
                    t2.error(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.doctypePending.publicIdentifier.append(c2);
                }
            }
        }
    }
    ,
    AfterDoctypePublicIdentifier{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': {
                    t2.transition(BetweenDoctypePublicAndSystemIdentifiers);
                    break;
                }
                case '>': {
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                case '\"': {
                    t2.error(this);
                    t2.transition(DoctypeSystemIdentifier_doubleQuoted);
                    break;
                }
                case '\'': {
                    t2.error(this);
                    t2.transition(DoctypeSystemIdentifier_singleQuoted);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.error(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.transition(BogusDoctype);
                }
            }
        }
    }
    ,
    BetweenDoctypePublicAndSystemIdentifiers{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': {
                    break;
                }
                case '>': {
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                case '\"': {
                    t2.error(this);
                    t2.transition(DoctypeSystemIdentifier_doubleQuoted);
                    break;
                }
                case '\'': {
                    t2.error(this);
                    t2.transition(DoctypeSystemIdentifier_singleQuoted);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.error(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.transition(BogusDoctype);
                }
            }
        }
    }
    ,
    AfterDoctypeSystemKeyword{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': {
                    t2.transition(BeforeDoctypeSystemIdentifier);
                    break;
                }
                case '>': {
                    t2.error(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                case '\"': {
                    t2.error(this);
                    t2.transition(DoctypeSystemIdentifier_doubleQuoted);
                    break;
                }
                case '\'': {
                    t2.error(this);
                    t2.transition(DoctypeSystemIdentifier_singleQuoted);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.error(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                }
            }
        }
    }
    ,
    BeforeDoctypeSystemIdentifier{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': {
                    break;
                }
                case '\"': {
                    t2.transition(DoctypeSystemIdentifier_doubleQuoted);
                    break;
                }
                case '\'': {
                    t2.transition(DoctypeSystemIdentifier_singleQuoted);
                    break;
                }
                case '>': {
                    t2.error(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.error(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.transition(BogusDoctype);
                }
            }
        }
    }
    ,
    DoctypeSystemIdentifier_doubleQuoted{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '\"': {
                    t2.transition(AfterDoctypeSystemIdentifier);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    t2.doctypePending.systemIdentifier.append('\ufffd');
                    break;
                }
                case '>': {
                    t2.error(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.doctypePending.systemIdentifier.append(c2);
                }
            }
        }
    }
    ,
    DoctypeSystemIdentifier_singleQuoted{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '\'': {
                    t2.transition(AfterDoctypeSystemIdentifier);
                    break;
                }
                case '\u0000': {
                    t2.error(this);
                    t2.doctypePending.systemIdentifier.append('\ufffd');
                    break;
                }
                case '>': {
                    t2.error(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.doctypePending.systemIdentifier.append(c2);
                }
            }
        }
    }
    ,
    AfterDoctypeSystemIdentifier{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': {
                    break;
                }
                case '>': {
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                case '\uffff': {
                    t2.eofError(this);
                    t2.doctypePending.forceQuirks = true;
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.error(this);
                    t2.transition(BogusDoctype);
                }
            }
        }
    }
    ,
    BogusDoctype{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            char c2 = r2.consume();
            switch (c2) {
                case '>': {
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
                case '\uffff': {
                    t2.emitDoctypePending();
                    t2.transition(Data);
                    break;
                }
            }
        }
    }
    ,
    CdataSection{

        @Override
        void read(Tokeniser t2, CharacterReader r2) {
            String data = r2.consumeTo("]]>");
            t2.dataBuffer.append(data);
            if (r2.matchConsume("]]>") || r2.isEmpty()) {
                t2.emit(new Token.CData(t2.dataBuffer.toString()));
                t2.transition(Data);
            }
        }
    };

    static final char nullChar = '\u0000';
    static final char[] attributeNameCharsSorted;
    static final char[] attributeValueUnquoted;
    private static final char replacementChar = '\ufffd';
    private static final String replacementStr;
    private static final char eof = '\uffff';

    abstract void read(Tokeniser var1, CharacterReader var2);

    private static void handleDataEndTag(Tokeniser t2, CharacterReader r2, TokeniserState elseTransition) {
        if (r2.matchesLetter()) {
            String name = r2.consumeLetterSequence();
            t2.tagPending.appendTagName(name);
            t2.dataBuffer.append(name);
            return;
        }
        boolean needsExitTransition = false;
        if (t2.isAppropriateEndTagToken() && !r2.isEmpty()) {
            char c2 = r2.consume();
            switch (c2) {
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': {
                    t2.transition(BeforeAttributeName);
                    break;
                }
                case '/': {
                    t2.transition(SelfClosingStartTag);
                    break;
                }
                case '>': {
                    t2.emitTagPending();
                    t2.transition(Data);
                    break;
                }
                default: {
                    t2.dataBuffer.append(c2);
                    needsExitTransition = true;
                    break;
                }
            }
        } else {
            needsExitTransition = true;
        }
        if (needsExitTransition) {
            t2.emit("</");
            t2.emit(t2.dataBuffer);
            t2.transition(elseTransition);
        }
    }

    private static void readRawData(Tokeniser t2, CharacterReader r2, TokeniserState current, TokeniserState advance) {
        switch (r2.current()) {
            case '<': {
                t2.advanceTransition(advance);
                break;
            }
            case '\u0000': {
                t2.error(current);
                r2.advance();
                t2.emit('\ufffd');
                break;
            }
            case '\uffff': {
                t2.emit(new Token.EOF());
                break;
            }
            default: {
                String data = r2.consumeRawData();
                t2.emit(data);
            }
        }
    }

    private static void readCharRef(Tokeniser t2, TokeniserState advance) {
        int[] c2 = t2.consumeCharacterReference(null, false);
        if (c2 == null) {
            t2.emit('&');
        } else {
            t2.emit(c2);
        }
        t2.transition(advance);
    }

    private static void readEndTag(Tokeniser t2, CharacterReader r2, TokeniserState a2, TokeniserState b2) {
        if (r2.matchesAsciiAlpha()) {
            t2.createTagPending(false);
            t2.transition(a2);
        } else {
            t2.emit("</");
            t2.transition(b2);
        }
    }

    private static void handleDataDoubleEscapeTag(Tokeniser t2, CharacterReader r2, TokeniserState primary, TokeniserState fallback) {
        if (r2.matchesLetter()) {
            String name = r2.consumeLetterSequence();
            t2.dataBuffer.append(name);
            t2.emit(name);
            return;
        }
        char c2 = r2.consume();
        switch (c2) {
            case '\t': 
            case '\n': 
            case '\f': 
            case '\r': 
            case ' ': 
            case '/': 
            case '>': {
                if (t2.dataBuffer.toString().equals("script")) {
                    t2.transition(primary);
                } else {
                    t2.transition(fallback);
                }
                t2.emit(c2);
                break;
            }
            default: {
                r2.unconsume();
                t2.transition(fallback);
            }
        }
    }

    static {
        attributeNameCharsSorted = new char[]{'\t', '\n', '\f', '\r', ' ', '\"', '\'', '/', '<', '=', '>', '?'};
        attributeValueUnquoted = new char[]{'\u0000', '\t', '\n', '\f', '\r', ' ', '\"', '&', '\'', '<', '=', '>', '`'};
        replacementStr = String.valueOf('\ufffd');
    }
}

