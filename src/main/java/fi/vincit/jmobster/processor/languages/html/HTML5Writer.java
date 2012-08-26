package fi.vincit.jmobster.processor.languages.html;

/*
 * Copyright 2012 Juha Siponen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import fi.vincit.jmobster.util.writer.DataWriter;

import java.util.Stack;

public class HTML5Writer implements DataWriter {
    final private Stack<String> tagStack;
    final private DataWriter writer;

    public HTML5Writer( DataWriter writer ) {
        this.tagStack = new Stack<String>();
        this.writer = writer;
    }

    public HTML5Writer startTag(String name) {
        return startTagWithAttr(name).endStartTagWithAttr(false);
    }

    public HTML5Writer startTagWithAttr(String name) {
        writer.write("<" + name);
        tagStack.push(name);
        return this;
    }

    // TODO: Use enum, better name
    public HTML5Writer endStartTagWithAttr(boolean alsoEnd) {
        if( alsoEnd ) {
            tagStack.pop();
            writer.writeLine("/>");
        } else {
            writer.writeLine(">").indent();
        }
        return this;
    }

    public HTML5Writer endTag() {
        String lastTag = tagStack.pop();
        writer.indentBack().write("</").write(lastTag).writeLine(">");
        return this;
    }

    public HTML5Writer writeAttr(String name, String value) {
        writer.write(" ").write(name).write("=\"").write(value).write("\"");
        return this;
    }

    @Override
    public HTML5Writer write(String modelString) {
        writer.write(modelString);
        return this;
    }

    @Override
    public HTML5Writer write(String modelString, String separator, boolean writeSeparator) {
        writer.write(modelString, separator, writeSeparator);
        return this;
    }

    @Override
    public HTML5Writer writeLine(String modelStringLine) {
        writer.writeLine(modelStringLine);
        return this;
    }

    @Override
    public HTML5Writer writeLine(String modelStringLine, String separator, boolean writeSeparator) {
        writer.writeLine(modelStringLine, separator, writeSeparator);
        return this;
    }

    @Override
    public HTML5Writer indent() {
        writer.indent();
        return this;
    }

    @Override
    public HTML5Writer indentBack() {
        writer.indentBack();
        return this;
    }

    @Override
    public void open() {
        // Do nothing
    }

    @Override
    public void close() {
        writer.close();
    }

    @Override
    public void setIndentation(int spaces) {
        writer.setIndentation(spaces);
    }

    @Override
    public void setIndentationChar(char indentationChar, int characterCount) {
        writer.setIndentationChar(indentationChar, characterCount);
    }

    @Override
    public void setLineSeparator(String lineSeparator) {
        writer.setLineSeparator(lineSeparator);
    }
}
