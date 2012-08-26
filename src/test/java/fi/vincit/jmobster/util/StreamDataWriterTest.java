package fi.vincit.jmobster.util;
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

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;

import static org.junit.Assert.assertEquals;

public class StreamDataWriterTest {

    @Test
    public void testWriteLines() throws Exception {
        DataWriter mw = new StringBufferWriter();
        mw.setIndentation(2);

        mw.writeLine("{");
        mw.writeLine("Foo");
        mw.writeLine("}");
        mw.close();

        String actual = mw.toString();
        String expected = "{\nFoo\n}\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testWriteLinesWithSeparator() throws Exception {
        DataWriter mw = new StringBufferWriter();
        mw.setIndentation(2);

        mw.writeLine("Foo", ",", true);
        mw.close();

        String actual = mw.toString();
        String expected = "Foo,\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testWriteLinesWithoutSeparator() throws Exception {
        DataWriter mw = new StringBufferWriter();
        mw.setIndentation(2);

        mw.writeLine("Foo", ",", false);
        mw.close();

        String actual = mw.toString();
        String expected = "Foo\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testWriteWithSeparator() throws Exception {
        DataWriter mw = new StringBufferWriter();
        mw.setIndentation(2);

        mw.write( "Foo", ",", true );
        mw.close();

        String actual = mw.toString();
        String expected = "Foo,";
        assertEquals(expected, actual);
    }

    @Test
    public void testWriteWithoutSeparator() throws Exception {
        DataWriter mw = new StringBufferWriter();
        mw.setIndentation(2);

        mw.write( "Foo", ",", false );
        mw.close();

        String actual = mw.toString();
        String expected = "Foo";
        assertEquals(expected, actual);
    }

    @Test
    public void testWriteChained() throws Exception {
        DataWriter mw = new StringBufferWriter();
        mw.setIndentation(2);

        mw.write("{").write("Foo").write("}").close();

        String actual = mw.toString();
        String expected = "{Foo}";
        assertEquals(expected, actual);
    }

    @Test
    public void testIndentationLines() throws Exception {
        DataWriter mw = new StringBufferWriter();
        mw.setIndentation(2);

        mw.writeLine( "{" );
        mw.indent();
        mw.writeLine("Foo");
        mw.indentBack();
        mw.writeLine("}");
        mw.close();

        String actual = mw.toString();
        String expected = "{\n  Foo\n}\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testIndentationChained() throws Exception {
        DataWriter mw = new StringBufferWriter();
        mw.setIndentation(2);

        mw.write( "{" ).indent();
        mw.writeLine("Foo").writeLine("Bar").writeLine("FooBar").indentBack();
        mw.writeLine("}");
        mw.close();

        String actual = mw.toString();
        String expected = "{Foo\n  Bar\n  FooBar\n}\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testIndentationChained2() throws Exception {
        DataWriter mw = new StringBufferWriter();
        mw.setIndentation(2);

        mw.write( "{" ).indent();
        mw.writeLine("Foo").writeLine("Bar").write("FooBar").indentBack();
        mw.writeLine("}");
        mw.close();

        String actual = mw.toString();
        String expected = "{Foo\n  Bar\n  FooBar\n}\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultIndentationForOutputStream() {
        DataWriter mw = new StringBufferWriter();
        mw.indent();
        mw.writeLine("Stuff");
        mw.close();

        assertEquals("    Stuff\n", mw.toString());
    }

    @Test
    public void testDefaultIndentationForBufferedStream() {
        DataWriter mw = new StringBufferWriter();
        mw.indent();
        mw.writeLine("Stuff");
        mw.close();

        assertEquals("    Stuff\n", mw.toString());
    }

    @Test
    public void testIndentationWithTabs() throws Exception {
        DataWriter mw = new StringBufferWriter();
        mw.setIndentationChar('\t', 1);

        mw.write( "{" ).indent();
        mw.writeLine("Foo").writeLine("Bar").write("FooBar").indentBack();
        mw.writeLine("}");
        mw.close();

        String actual = mw.toString();
        String expected = "{Foo\n\tBar\n\tFooBar\n}\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testLineSeparatorChange() throws Exception {
        DataWriter mw = new StringBufferWriter();
        mw.setLineSeparator("\r\n");
        mw.setIndentation(4);

        mw.write( "{" ).indent();
        mw.writeLine("Foo").writeLine("Bar").write("FooBar").indentBack();
        mw.writeLine("}");
        mw.close();

        String actual = mw.toString();
        String expected = "{Foo\r\n    Bar\r\n    FooBar\r\n}\r\n";
        assertEquals(expected, actual);
    }

}
