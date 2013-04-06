package fi.vincit.jmobster.processor.languages.html5.writer;

import fi.vincit.jmobster.processor.languages.BaseDataWriter;
import fi.vincit.jmobster.util.writer.DataWriter;

public class HTML5Writer extends BaseDataWriter<HTML5Writer> {

    public static final String NO_VALUE = "__NO_VALUE__";

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
        return write('<').write(name);
    }

    public HTML5Writer writeTagEnd() {
        return write('>');
    }

    public HTML5Writer writeTagAttr(String name, String value) {
        return write(' ').write(name).write('=').write('"').write(value).write('"');
    }
}
