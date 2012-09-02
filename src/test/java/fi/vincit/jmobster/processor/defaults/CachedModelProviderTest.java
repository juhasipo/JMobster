package fi.vincit.jmobster.processor.defaults;

import fi.vincit.jmobster.util.writer.DataWriter;
import fi.vincit.jmobster.util.writer.StringBufferWriter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class CachedModelProviderTest {
    @Test
    public void testGetModelEmpty() throws Exception {
        DataWriter dataWriter = mock(DataWriter.class);
        when(dataWriter.toString()).thenReturn("");

        CachedModelProvider provider = new CachedModelProvider( CachedModelProvider.WriteMode.COMPACT, dataWriter );

        String model = provider.getModel();

        assertNotNull(model);
        assertEquals("", model);
    }

    @Test
    public void testCreateWithStringWriter() throws Exception {
        CachedModelProvider provider = CachedModelProvider.createWithStringWriter( CachedModelProvider.WriteMode.PRETTY );
        assertEquals( StringBufferWriter.class, provider.getDataWriter().getClass() );
    }

    @Test
    public void testConfigurePretty() throws Exception {
        DataWriter dataWriter = mock(DataWriter.class);
        when(dataWriter.toString()).thenReturn("");

        new CachedModelProvider( CachedModelProvider.WriteMode.PRETTY, dataWriter );

        verify(dataWriter).setIndentationChar(' ', 4);
        verify(dataWriter).setLineSeparator("\n");
    }

    @Test
    public void testConfigureCompact() throws Exception {
        DataWriter dataWriter = mock(DataWriter.class);
        when(dataWriter.toString()).thenReturn("");

        new CachedModelProvider( CachedModelProvider.WriteMode.COMPACT, dataWriter );

        verify(dataWriter).setIndentation(0);
        verify(dataWriter).setLineSeparator( "" );
    }

    @Test
    public void testGetDataWriter() throws Exception {
        DataWriter dataWriter = mock(DataWriter.class);
        when(dataWriter.toString()).thenReturn("");

        CachedModelProvider provider = new CachedModelProvider( CachedModelProvider.WriteMode.COMPACT, dataWriter );

        assertEquals(dataWriter, provider.getDataWriter());
    }

    @Test
    public void testClear() throws Exception {
        DataWriter dataWriter = mock(DataWriter.class);
        when(dataWriter.toString()).thenReturn("model", "model2");

        CachedModelProvider provider = new CachedModelProvider( CachedModelProvider.WriteMode.COMPACT, dataWriter );

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
