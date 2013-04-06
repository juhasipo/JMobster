package fi.vincit.jmobster.processor.languages.html5.writer;

import fi.vincit.jmobster.processor.languages.BaseDataWriter;
import fi.vincit.jmobster.util.writer.DataWriter;

import java.util.Stack;

public class HTML5Writer extends BaseDataWriter<HTML5Writer> {

    public static final String NO_VALUE = "__NO_VALUE__";

    private Stack<String> tagNameStack = new Stack<String>();
    private boolean insideTagBody = false;

    public HTML5Writer(DataWriter writer) {
        super(writer);
    }

    public HTML5Writer writeInput(String type, String id, String name, String value) {
        writeTagStart("input").writeTagAttr("type", type);
        if( NO_VALUE != id ) {
            writeTagAttr("id", id);
        }
        if( NO_VALUE != name ) {
            writeTagAttr("name", name);
        }
        if( NO_VALUE != value ) {
            writeTagAttr("value", value);
        }
        return writeTagEnd();
    }

    public HTML5Writer writeTagStart(String name) {
        if( insideTagBody ) {
            throw new IllegalStateException("Trying to start a tag, but still writing another tag's body");
        }

        tagNameStack.push(name);
        insideTagBody = true;
        return write('<').write(name);
    }

    public HTML5Writer writeTagEnd() {
        if( !insideTagBody ) {
            throw new IllegalStateException("Trying to end a tag body, but not writing a body");
        }
        insideTagBody = false;
        return write('>');
    }

    public HTML5Writer writeTagAttr(String name, String value) {
        if( !insideTagBody ) {
            throw new IllegalStateException("Trying to write attributes, but not inside tag body");
        }
        return write(' ').write(name).write('=').write('"').write(value).write('"');
    }

    public HTML5Writer writeEndTag() {
        return writeEndTag(popTag());
    }

    public HTML5Writer writeEndTag(String name) {
        if( insideTagBody ) {
            throw new IllegalStateException("Trying to end a tag, but still writing tag body");
        }
        return write("</").write(name).write('>');
    }

    public String popTag() {
        if( tagNameStack.empty() ) {
            throw new IllegalStateException("No open tags");
        }
        return tagNameStack.pop();
    }

    public void clearTags() {
        tagNameStack.clear();
    }


}
