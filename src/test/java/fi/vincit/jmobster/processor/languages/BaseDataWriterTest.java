package fi.vincit.jmobster.processor.languages;

import fi.vincit.jmobster.util.writer.DataWriter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseDataWriterTest {

    public static final String STRING = "test";
    public static final String SEPARATOR = ",";
    public static final boolean SEPARATOR_STATUS = true;
    public static final char CHAR = 'c';
    @Mock private DataWriter mockWriter;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private static class TestDataWriter extends BaseDataWriter<TestDataWriter> {
        private TestDataWriter(DataWriter writer) {
            super(writer);
        }
    }

    @Test
    public void testIsOpen() throws Exception {
        TestDataWriter testDataWriter = new TestDataWriter(mockWriter);
        when(mockWriter.isOpen()).thenReturn(true);
        assertThat(testDataWriter.isOpen(), is(true));
    }

    @Test
    public void testWrite_String() throws Exception {
        TestDataWriter testDataWriter = new TestDataWriter(mockWriter);
        assertThat(testDataWriter.write(STRING), sameInstance(testDataWriter));
        verify(mockWriter).write(STRING);
    }

    @Test
    public void testWrite_StringAndSeparator() throws Exception {
        TestDataWriter testDataWriter = new TestDataWriter(mockWriter);
        assertThat(testDataWriter.write(STRING, SEPARATOR, SEPARATOR_STATUS), sameInstance(testDataWriter));
        verify(mockWriter).write(STRING, SEPARATOR, SEPARATOR_STATUS);
    }

    @Test
    public void testWrite_Char() throws Exception {
        TestDataWriter testDataWriter = new TestDataWriter(mockWriter);
        assertThat(testDataWriter.write(CHAR), sameInstance(testDataWriter));
        verify(mockWriter).write(CHAR);
    }

    @Test
    public void testWriteLine_String() throws Exception {
        TestDataWriter testDataWriter = new TestDataWriter(mockWriter);
        assertThat(testDataWriter.writeLine(STRING), sameInstance(testDataWriter));
        verify(mockWriter).writeLine(STRING);
    }

    @Test
    public void testWriteLine_StringAndSeparator() throws Exception {
        TestDataWriter testDataWriter = new TestDataWriter(mockWriter);
        assertThat(testDataWriter.writeLine(STRING, SEPARATOR, SEPARATOR_STATUS), sameInstance(testDataWriter));
        verify(mockWriter).writeLine(STRING, SEPARATOR, SEPARATOR_STATUS);
    }

    @Test
    public void testClose() throws Exception {
        TestDataWriter testDataWriter = new TestDataWriter(mockWriter);
        testDataWriter.close();
        verify(mockWriter).close();
    }

    @Test
    public void testSetIndentation() throws Exception {
        TestDataWriter testDataWriter = new TestDataWriter(mockWriter);
        testDataWriter.setIndentation(1);
        verify(mockWriter).setIndentation(1);
    }

    @Test
    public void testIndent() throws Exception {
        TestDataWriter testDataWriter = new TestDataWriter(mockWriter);
        assertThat(testDataWriter.indent(), sameInstance(testDataWriter));
        verify(mockWriter).indent();
    }

    @Test
    public void testIndentBack() throws Exception {
        TestDataWriter testDataWriter = new TestDataWriter(mockWriter);
        assertThat(testDataWriter.indentBack(), sameInstance(testDataWriter));
        verify(mockWriter).indentBack();
    }

    @Test
    public void testSetIndentationChar() throws Exception {
        TestDataWriter testDataWriter = new TestDataWriter(mockWriter);
        testDataWriter.setIndentationChar(CHAR, 1);
        verify(mockWriter).setIndentationChar(CHAR, 1);
    }

    @Test
    public void testSetLineSeparator() throws Exception {
        TestDataWriter testDataWriter = new TestDataWriter(mockWriter);
        testDataWriter.setLineSeparator(STRING);
        verify(mockWriter).setLineSeparator(STRING);
    }

    @Test
    public void testToString() throws Exception {
        TestDataWriter testDataWriter = new TestDataWriter(mockWriter);
        when(mockWriter.toString()).thenReturn("test");
        assertThat(testDataWriter.toString(), is("test"));
    }
}
