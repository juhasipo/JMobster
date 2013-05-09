package fi.vincit.jmobster.util.writer;

/*
 * Copyright 2012-2013 Juha Siponen
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

import static org.junit.Assert.assertEquals;

public class StringBufferWriterTest {
    @Test
    public void testToString() throws Exception {
        StringBufferWriter writer = new StringBufferWriter();
        writer.write("Test string");
        assertEquals("Test string", writer.toString());
    }

    @Test
    public void testClear() throws Exception {
        StringBufferWriter writer = new StringBufferWriter();
        writer.write("Test string");
        assertEquals("Test string", writer.toString());

        writer.clear();
        assertEquals("", writer.toString());

        writer.write("Testing");
        assertEquals("Testing", writer.toString());
    }
}
