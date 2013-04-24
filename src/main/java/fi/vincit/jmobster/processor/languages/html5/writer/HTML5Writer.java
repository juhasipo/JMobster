package fi.vincit.jmobster.processor.languages.html5.writer;

import fi.vincit.jmobster.processor.languages.BaseDataWriter;
import fi.vincit.jmobster.util.writer.DataWriter;

import java.util.Stack;

public class HTML5Writer extends BaseDataWriter<HTML5Writer> {

    public static final String NO_VALUE = "__NO_VALUE__";

    private static final char TAG_BODY_START = '<';
    private static final char TAG_BODY_END = '>';
    private static final String TAG_END_BODY_START = "</";
    private static final String EMPTY_TAG_END = "/>";

    private static final String ATTR_START = "=\"";
    private static final char ATTR_END = '"';

    private Stack<String> tagNameStack = new Stack<String>();
    private boolean insideTagBody = false;

    public HTML5Writer writeEmptyTagEnd() {
        return writeLine(EMPTY_TAG_END);
    }

    public static enum FormMethod {
        GET, POST, PUT, DELETE, PATCH, NONE
    }

    public HTML5Writer(DataWriter writer) {
        super(writer);
    }

    public HTML5Writer writeFormStart(String name, FormMethod method, String action) {
        writeTagStart("form");
        writeTagAttr("name", name, NO_VALUE != name);
        writeTagAttr("method", method.name().toLowerCase(), FormMethod.NONE != method);
        writeTagAttr("action", action, NO_VALUE != action);
        return writeTagEnd();
    }

    public HTML5Writer writeInput(String type, String id, String name, String value) {
        writeTagStart("input").writeTagAttr("type", type);
        writeTagAttr("id", id, NO_VALUE != id);
        writeTagAttr("name", name, NO_VALUE != name);
        writeTagAttr("value", value, NO_VALUE != value);
        return writeEmptyTagEnd();
    }

    public HTML5Writer writeTagStart(String name) {
        if( insideTagBody ) {
            throw new IllegalStateException("Trying to start a tag, but still writing another tag's body");
        }

        tagNameStack.push(name);
        insideTagBody = true;
        return write(TAG_BODY_START).write(name);
    }

    public HTML5Writer writeTagEnd() {
        if( !insideTagBody ) {
            throw new IllegalStateException("Trying to end a tag body, but not writing a body");
        }
        insideTagBody = false;
        return write(TAG_BODY_END);
    }

    public HTML5Writer writeTagAttr(String name, String value) {
        return writeTagAttr(name, value, true);
    }

    public HTML5Writer writeTagAttr(String name, String value, boolean condition) {
        if( !insideTagBody ) {
            throw new IllegalStateException("Trying to write attributes, but not inside tag body");
        }
        if( condition ) {
            return write(' ').write(name).write(ATTR_START).write(value).write(ATTR_END);
        } else {
            return this;
        }
    }

    public HTML5Writer writeEndTag() {
        return writeEndTag(popTag());
    }

    public HTML5Writer writeEndTag(String name) {
        if( insideTagBody ) {
            throw new IllegalStateException("Trying to end a tag, but still writing tag body");
        }
        return write(TAG_END_BODY_START).write(name).write(TAG_BODY_END);
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
