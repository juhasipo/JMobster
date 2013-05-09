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
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class CachedModelProviderTest {
    @Test
    public void testGetModelEmpty() throws Exception {
        DataWriter dataWriter = mock(DataWriter.class);
        when(dataWriter.toString()).thenReturn("");

        CachedModelProvider provider = new CachedModelProvider( WriterUtil.WriteMode.COMPACT, dataWriter );

        String model = provider.getModel();

        assertNotNull(model);
        assertEquals("", model);
    }

    @Test
    public void testCreateWithStringWriter() throws Exception {
        CachedModelProvider provider = CachedModelProvider.createWithStringWriter( WriterUtil.WriteMode.PRETTY );
        assertEquals( StringBufferWriter.class, provider.getDataWriter().getClass() );
    }

    @Test
    public void testConfigurePretty() throws Exception {
        DataWriter dataWriter = mock(DataWriter.class);
        when(dataWriter.toString()).thenReturn("");

        new CachedModelProvider( WriterUtil.WriteMode.PRETTY, dataWriter );

        verify(dataWriter).setIndentationChar(' ', 4);
        verify(dataWriter).setLineSeparator("\n");
    }

    @Test
    public void testConfigureCompact() throws Exception {
        DataWriter dataWriter = mock(DataWriter.class);
        when(dataWriter.toString()).thenReturn("");

        new CachedModelProvider( WriterUtil.WriteMode.COMPACT, dataWriter );

        verify(dataWriter).setIndentation(0);
        verify(dataWriter).setLineSeparator( "" );
    }

    @Test
    public void testGetDataWriter() throws Exception {
        DataWriter dataWriter = mock(DataWriter.class);
        when(dataWriter.toString()).thenReturn("");

        CachedModelProvider provider = new CachedModelProvider( WriterUtil.WriteMode.COMPACT, dataWriter );

        assertEquals(dataWriter, provider.getDataWriter());
    }

    @Test
    public void testClear() throws Exception {
        DataWriter dataWriter = mock(DataWriter.class);
        when(dataWriter.toString()).thenReturn("model", "model2");

        CachedModelProvider provider = new CachedModelProvider( WriterUtil.WriteMode.COMPACT, dataWriter );

        String model = provider.getModel();
        assertNotNull(model);
        assertEquals("model", model);

        model = provider.getModel();
        assertNotNull(model);
        assertEquals("model", model);

        provider.clear();
        model = provider.getModel();
        assertNotNull(model);
        assertEquals("model2", model);
    }
}
