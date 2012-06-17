package fi.vincit.jmobster.util;

import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class ModelWriterTest {

    @Test
    public void testWriteLines() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ModelWriter mw = new ModelWriter(os);
        mw.setIndentation(2);

        mw.writeLine("{");
        mw.writeLine("Foo");
        mw.writeLine("}");
        mw.close();

        String actual = os.toString();
        String expected = "{\nFoo\n}\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testWriteLinesWithSeparator() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ModelWriter mw = new ModelWriter(os);
        mw.setIndentation(2);

        mw.writeLine("Foo", ",", true);
        mw.close();

        String actual = os.toString();
        String expected = "Foo,\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testWriteLinesWithoutSeparator() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ModelWriter mw = new ModelWriter(os);
        mw.setIndentation(2);

        mw.writeLine("Foo", ",", false);
        mw.close();

        String actual = os.toString();
        String expected = "Foo\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testWriteWithSeparator() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ModelWriter mw = new ModelWriter(os);
        mw.setIndentation(2);

        mw.write( "Foo", ",", true );
        mw.close();

        String actual = os.toString();
        String expected = "Foo,";
        assertEquals(expected, actual);
    }

    @Test
    public void testWriteWithoutSeparator() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ModelWriter mw = new ModelWriter(os);
        mw.setIndentation(2);

        mw.write( "Foo", ",", false );
        mw.close();

        String actual = os.toString();
        String expected = "Foo";
        assertEquals(expected, actual);
    }

    @Test
    public void testWriteChained() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ModelWriter mw = new ModelWriter(os);
        mw.setIndentation(2);

        mw.write("{").write("Foo").write("}").close();

        String actual = os.toString();
        String expected = "{Foo}";
        assertEquals(expected, actual);
    }

    @Test
    public void testIndentationLines() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ModelWriter mw = new ModelWriter(os);
        mw.setIndentation(2);

        mw.writeLine( "{" );
        mw.indent();
        mw.writeLine("Foo");
        mw.indentBack();
        mw.writeLine("}");
        mw.close();

        String actual = os.toString();
        String expected = "{\n  Foo\n}\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testIndentationChained() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ModelWriter mw = new ModelWriter(os);
        mw.setIndentation(2);

        mw.write( "{" ).indent();
        mw.writeLine("Foo").writeLine("Bar").writeLine("FooBar").indentBack();
        mw.writeLine("}");
        mw.close();

        String actual = os.toString();
        String expected = "{Foo\n  Bar\n  FooBar\n}\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testIndentationChained2() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ModelWriter mw = new ModelWriter(os);
        mw.setIndentation(2);

        mw.write( "{" ).indent();
        mw.writeLine("Foo").writeLine("Bar").write("FooBar").indentBack();
        mw.writeLine("}");
        mw.close();

        String actual = os.toString();
        String expected = "{Foo\n  Bar\n  FooBar\n}\n";
        assertEquals(expected, actual);
    }

}
