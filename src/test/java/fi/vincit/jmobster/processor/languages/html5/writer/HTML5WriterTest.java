package fi.vincit.jmobster.processor.languages.html5.writer;

import fi.vincit.jmobster.util.writer.DataWriter;
import fi.vincit.jmobster.util.writer.StringBufferWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HTML5WriterTest {

    private DataWriter mw;
    private HTML5Writer writer;

    @Before
    public void initTest() {
        mw = new StringBufferWriter();
        writer = new HTML5Writer(mw);
    }

    @After
    public void tearDownTest() {
        try { writer.close(); } catch (Exception e) {}
    }

    @Test
    public void testWriteTagStart() {
        writer.writeTagStart("test");
        mw.close();
        assertThat(mw.toString(), is("<test"));
    }

    @Test
    public void testWriteTagEnd() {
        writer.writeTagEnd();
        mw.close();
        assertThat(mw.toString(), is(">"));
    }

    @Test
    public void testWriteAttr() {
        writer.writeTagAttr("attr", "value");
        mw.close();
        assertThat(mw.toString(), is(" attr=\"value\""));
    }

    @Test
    public void testWriteEndTag() {
        writer.writeEndTag("test");
        mw.close();
        assertThat(mw.toString(), is("</test>"));
    }

    @Test
    public void testWriteInput() {
        writer.writeInput("text", "text-id", "text-name", "text-value");
        mw.close();
        assertThat(mw.toString(), is("<input type=\"text\" id=\"text-id\" name=\"text-name\" value=\"text-value\">"));
    }

    @Test
    public void testWriteInput_NoID() {
        writer.writeInput("text", HTML5Writer.NO_VALUE, "text-name", "text-value");
        mw.close();
        assertThat(mw.toString(), is("<input type=\"text\" name=\"text-name\" value=\"text-value\">"));
    }

    @Test
    public void testWriteInput_NoName() {
        writer.writeInput("text", "text-id", HTML5Writer.NO_VALUE, "text-value");
        mw.close();
        assertThat(mw.toString(), is("<input type=\"text\" id=\"text-id\" value=\"text-value\">"));
    }

    @Test
    public void testWriteInput_NoValue() {
        writer.writeInput("text", "text-id", "text-name", HTML5Writer.NO_VALUE);
        mw.close();
        assertThat(mw.toString(), is("<input type=\"text\" id=\"text-id\" name=\"text-name\">"));
    }

    @Test
    public void testEndTag() {
        writer.writeTagStart("test").writeTagEnd().writeEndTag();
        mw.close();
        assertThat(mw.toString(), is("<test></test>"));
    }

    @Test
    public void testPopTag() {
        writer.writeTagStart("test");
        assertThat(writer.popTag(), is("test"));
    }

    @Test
    public void testClear() {
        writer.writeTagStart("test1").writeTagStart("test2");
        writer.clearTags();
        boolean exceptionThrown = false;
        try {
            writer.popTag();
        } catch (IllegalStateException e) {
            exceptionThrown = true;
        }
        assertThat(exceptionThrown, is(true));
    }

    @Test(expected = IllegalStateException.class)
    public void testEndTag_WithoutStartTag() {
        writer.writeEndTag();
    }

    @Test(expected = IllegalStateException.class)
    public void testPopEmptyStack() {
        writer.popTag();
    }
}
