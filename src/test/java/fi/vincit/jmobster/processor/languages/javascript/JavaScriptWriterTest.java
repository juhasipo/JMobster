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

import fi.vincit.jmobster.util.DataWriter;
import fi.vincit.jmobster.util.StreamDataWriter;
import fi.vincit.jmobster.util.StringBufferWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;

public class JavaScriptWriterTest {
    private DataWriter mw;
    private JavaScriptWriter writer;

    @Before
    public void initTest() {
        mw = new StringBufferWriter();
        writer = new JavaScriptWriter(mw);
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
    public void testUnclosedBlock() {
        writer.startBlock();
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
        writer.endBlock();
        mw.close();

        assertEquals("{\n}\n", mw.toString());
    }

    @Test
    public void testSetIndentationChar() {
        writer.setIndentationChar('\t', 2);
        writer.startBlock();
        writer.write("test");
        writer.endBlock();
        mw.close();

        assertEquals("{\n\t\ttest\n}\n", mw.toString());
    }

    @Test
     public void testSetLineSeparator() {
        writer.setLineSeparator("l");
        writer.startBlock();
        writer.endBlock();
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
