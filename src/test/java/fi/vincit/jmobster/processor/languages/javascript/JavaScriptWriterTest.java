package fi.vincit.jmobster.processor.languages.javascript;/*
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
import fi.vincit.jmobster.util.writer.StringBufferWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JavaScriptWriterTest {
    private DataWriter mw;
    private JavaScriptWriter writer;

    @Before
    public void initTest() {
        mw = new StringBufferWriter();
        writer = new JavaScriptWriter(mw);
    }

    @After
    public void tearDownTest() {
        try { writer.close(); } catch (Exception e) {}
    }

    @Test
    public void testIsOpen() {
        assertTrue(writer.isOpen());
    }

    @Test
    public void testIsOpenAfterClose() {
        writer.close();
        assertFalse( writer.isOpen() );
    }

    @Test
    public void testToString() {
        writer.startAnonFunction("arg1", "arg2", "arg3").endFunction();
        mw.close();
        final String result = writer.toString();

        assertEquals( "function(arg1, arg2, arg3) {\n}\n", result );
    }

    @Test
    public void testWrite() {
        writer.write("test");
        mw.close();
        final String result = writer.toString();

        assertEquals( "test", result );
    }

    @Test
    public void testWriteLine() {
        writer.writeLine("Line");
        mw.close();
        final String result = writer.toString();

        assertEquals( "Line\n", result );
    }

    @Test
    public void testWriteChar() {
        writer.write('c');
        mw.close();
        final String result = writer.toString();

        assertEquals( "c", result );
    }

    @Test
    public void testAnonFunction() {
        writer.startAnonFunction("arg1", "arg2", "arg3").endFunction();
        mw.close();

        assertEquals("function(arg1, arg2, arg3) {\n}\n", mw.toString());
    }

    @Test
    public void testNamedFunction() {
        writer.startNamedFunction("func", "arg1", "arg2", "arg3").endFunction();
        mw.close();

        assertEquals("function func(arg1, arg2, arg3) {\n}\n", mw.toString());
    }

    @Test
    public void testFunctionWithContent() {
        writer.startAnonFunction("arg1", "arg2", "arg3");
        writer.writeLine("return this;").endFunction();
        mw.close();

        assertEquals("function(arg1, arg2, arg3) {\n    return this;\n}\n", mw.toString());
    }

    @Test
    public void testKeyValuesFunction() {
        writer.writeKeyValue("key1", "1", false);
        writer.writeKeyValue("key2", "2", false);
        writer.writeKeyValue("key3", "3", true);
        mw.close();

        assertEquals("key1: 1,\nkey2: 2,\nkey3: 3\n", mw.toString());
    }

    @Test
    public void testStartBlock() {
        writer.startBlock();
        mw.close();

        assertEquals("{\n", mw.toString());
    }

    @Test
    public void testEndBlock() {
        writer.endBlock(false);
        mw.close();

        assertEquals("},\n", mw.toString());
    }

    @Test
    public void testEndBlockAsLast() {
        writer.endBlock(true);
        mw.close();

        assertEquals("}\n", mw.toString());
    }

    @Test
    public void testArray() {
        writer.writeArray(false, 1, 2, 3);
        mw.close();

        assertEquals("[1, 2, 3],\n", mw.toString());
    }

    @Test
    public void testArrayAsLast() {
        writer.writeArray(true, 1, 2, 3);
        mw.close();

        assertEquals("[1, 2, 3]\n", mw.toString());
    }

    @Test(expected = RuntimeException.class)
    public void testUnclosedAnonFunction() {
        writer.startAnonFunction();
        writer.close();
    }
    @Test(expected = RuntimeException.class)
    public void testUnclosedNamedFunction() {
        writer.startNamedFunction("func");
        writer.close();
    }
    @Test(expected = RuntimeException.class)
    public void testTooManyFunctionsClosedNonStarted() {
        writer.endFunction();
        writer.close();
    }
    @Test(expected = RuntimeException.class)
    public void testTooManyFunctionsClosed() {
        writer.startAnonFunction();
        writer.endFunction();
        writer.endFunction();
        writer.close();
    }
    @Test(expected = RuntimeException.class)
    public void testUnclosedBlock() {
        writer.startBlock();
        writer.close();
    }
    @Test(expected = RuntimeException.class)
    public void testTooManyBlockClosedNonStarted() {
        writer.endBlock(true);
        writer.close();
    }
    @Test(expected = RuntimeException.class)
    public void testTooManyBlockClosed() {
        writer.startBlock();
        writer.endBlock( false );
        writer.endBlock(true);
        writer.close();
    }

    @Test
    public void testNoSanityChecksAnonFunctions() {
        writer.setLenientMode( true );
        writer.startAnonFunction();
        writer.close();
    }

    @Test
    public void testNoSanityChecksNameFunctions() {
        writer.setLenientMode( true );
        writer.startNamedFunction("func");
        writer.close();
    }

    @Test
    public void testNoSanityChecksBlocks() {
        writer.setLenientMode( true );
        writer.startBlock();
        writer.close();
    }


    @Test
    public void testOpen() {
        writer.open();
    }

    @Test
    public void testSetIndentation() {
        writer.setIndentation(3);
        writer.startBlock();
        writer.endBlock(true);
        mw.close();

        assertEquals("{\n}\n", mw.toString());
    }

    @Test
    public void testSetIndentationChar() {
        writer.setIndentationChar('\t', 2);
        writer.startBlock();
        writer.write("test");
        writer.endBlock(true);
        mw.close();

        assertEquals("{\n\t\ttest\n}\n", mw.toString());
    }

    @Test
     public void testSetLineSeparator() {
        writer.setLineSeparator("l");
        writer.startBlock();
        writer.endBlock(true);
        mw.close();

        assertEquals("{l}l", mw.toString());
    }

    @Test
    public void testSetSpace() {
        writer.setSpace("_SPACE_");
        writer.startNamedFunction("func", "arg1");
        writer.endFunction();
        mw.close();

        assertEquals("function_SPACE_func(arg1)_SPACE_{\n}\n", mw.toString());
    }
}
