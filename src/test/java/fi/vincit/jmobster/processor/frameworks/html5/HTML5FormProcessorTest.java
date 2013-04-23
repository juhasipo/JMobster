package fi.vincit.jmobster.processor.frameworks.html5;

import fi.vincit.jmobster.processor.languages.html5.HTML5Context;
import fi.vincit.jmobster.processor.languages.html5.writer.HTML5Writer;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import fi.vincit.jmobster.util.writer.StringBufferWriter;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HTML5FormProcessorTest {

    private HTML5Writer html5Writer;
    protected StringBufferWriter writer;

    @Before
    public void init() {
        writer = new StringBufferWriter();
        html5Writer = new HTML5Writer(writer);
    }

    @Test
    public void testBasic() {
        HTML5FormProcessor html5FormProcessor = new HTML5FormProcessor();
        html5FormProcessor.setLanguageContext(new HTML5Context(html5Writer));

        Model model = mock(Model.class);
        when(model.getName()).thenReturn("Test");
        List<ModelField> fields = new ArrayList<ModelField>();
        ModelField field1 = mock(ModelField.class);
        when(field1.getName()).thenReturn("field1");
        fields.add(field1);

        when(model.getFields()).thenReturn(fields);
        html5FormProcessor.processModel(model, ItemStatuses.notFirst());

        writer.close();

        assertThat(writer.toString(), Is.is("<input type=\"text\" id=\"field1\" name=\"field1\">"));
    }
}
