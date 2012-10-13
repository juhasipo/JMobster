package fi.vincit.jmobster.util.writer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringBufferWriterTest {
    @Test
    public void testToString() throws Exception {
        StringBufferWriter writer = new StringBufferWriter();
        writer.write("Test string");
        assertEquals("Test string", writer.toString());
    }
}
