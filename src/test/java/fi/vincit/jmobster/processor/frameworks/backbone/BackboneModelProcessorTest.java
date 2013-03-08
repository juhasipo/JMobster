package fi.vincit.jmobster.processor.frameworks.backbone;

import fi.vincit.jmobster.processor.FieldValueConverter;
import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import fi.vincit.jmobster.util.writer.DataWriter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class BackboneModelProcessorTest {

    public static final String COMMENT = "/*\n * Auto-generated file\n */\n";
    public static final String FIRST_LINE = "var Models = {\n";
    public static final String TEST_MODEL_NAME = "TestModel";
    private DataWriter writer;
    private MockWriter mockWriter;
    @Mock private FieldValueConverter fieldValueConverter;
    @Mock private ValidatorProcessor validatorProcessor;
    @Mock private ModelProcessor<JavaScriptWriter> valueProcessor;

    @Before
    public void init() {
        mockWriter();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStartProcessing() throws Exception {
        ModelProcessor processor = createProcessor();

        processor.startProcessing(ItemStatuses.firstAndLast());

        verify(writer).write(COMMENT);
        verify(writer).write(FIRST_LINE);
    }

    @Test
    public void testStartProcessingWithCustomNamespace() throws Exception {
        BackboneModelProcessor processor = createProcessor();
        processor.setNamespaceName("Foo");

        processor.startProcessing(ItemStatuses.firstAndLast());

        verify(writer).write(COMMENT);
        verify(writer).write("var Foo = {\n");
    }

    @Test
    public void testStartProcessingWithCustomComment() throws Exception {
        BackboneModelProcessor processor = createProcessor();
        processor.setStartComment("/* Foo */");

        processor.startProcessing(ItemStatuses.firstAndLast());

        verify(writer).write("/* Foo */\n");
        verify(writer).write(FIRST_LINE);
    }

    @Test
    public void testEndProcessing() throws Exception {
        ModelProcessor processor = createProcessor();

        processor.endProcessing(ItemStatuses.firstAndLast());

        verify(writer).write("};\n");
        assertThat(writer.isOpen(), is(false));
    }

    @Test
    public void testProcessFirstModel() throws Exception{
        ModelProcessor processor = createProcessor();

        Model model = mockModel();
        processor.processModel(model, ItemStatuses.first());

        InOrder order = inOrder(writer, validatorProcessor, valueProcessor);
        order.verify(writer).write(TEST_MODEL_NAME);
        order.verify(writer).write(": Backbone.Model.extend({\n");

        order.verify(writer).write("validation");
        order.verify(writer).write(": ");
        verify(validatorProcessor).startProcessing(ItemStatuses.first());
        verify(validatorProcessor).processModel(model, ItemStatuses.firstAndLast());
        verify(validatorProcessor).endProcessing(ItemStatuses.first());

        order.verify(writer).write("defaults");
        order.verify(writer).write(": ");
        verify(valueProcessor).startProcessing(ItemStatuses.last());
        verify(valueProcessor).processModel(model, ItemStatuses.firstAndLast());
        verify(valueProcessor).endProcessing(ItemStatuses.last());

        order.verify(writer).write("}),\n");
    }

    @Test
    public void testProcessMiddleModel() throws Exception{
        ModelProcessor processor = createProcessor();

        Model model = mockModel();
        processor.processModel(model, ItemStatuses.notFirstNorLast());

        InOrder order = inOrder(writer, validatorProcessor, valueProcessor);
        order.verify(writer).write(TEST_MODEL_NAME);
        order.verify(writer).write(": Backbone.Model.extend({\n");

        order.verify(writer).write("validation");
        order.verify(writer).write(": ");
        verify(validatorProcessor).startProcessing(ItemStatuses.first());
        verify(validatorProcessor).processModel(model, ItemStatuses.firstAndLast());
        verify(validatorProcessor).endProcessing(ItemStatuses.first());

        order.verify(writer).write("defaults");
        order.verify(writer).write(": ");
        verify(valueProcessor).startProcessing(ItemStatuses.last());
        verify(valueProcessor).processModel(model, ItemStatuses.firstAndLast());
        verify(valueProcessor).endProcessing(ItemStatuses.last());

        order.verify(writer).write("}),\n");
    }

    @Test
    public void testProcessLastModel() throws Exception{
        ModelProcessor processor = createProcessor();

        Model model = mockModel();
        processor.processModel(model, ItemStatuses.last());

        InOrder order = inOrder(writer, validatorProcessor, valueProcessor);
        order.verify(writer).write(TEST_MODEL_NAME);
        order.verify(writer).write(": Backbone.Model.extend({\n");

        order.verify(writer).write("validation");
        order.verify(writer).write(": ");
        verify(validatorProcessor).startProcessing(ItemStatuses.first());
        verify(validatorProcessor).processModel(model, ItemStatuses.firstAndLast());
        verify(validatorProcessor).endProcessing(ItemStatuses.first());

        order.verify(writer).write("defaults");
        order.verify(writer).write(": ");
        verify(valueProcessor).startProcessing(ItemStatuses.last());
        verify(valueProcessor).processModel(model, ItemStatuses.firstAndLast());
        verify(valueProcessor).endProcessing(ItemStatuses.last());

        order.verify(writer).write("})\n");
    }

    private Model mockModel() {
        Model model = mock(Model.class);
        when(model.getName()).thenReturn(TEST_MODEL_NAME);
        return model;
    }


    private BackboneModelProcessor createProcessor() {
        BackboneModelProcessor processor = new BackboneModelProcessor(
                mockWriter,
                fieldValueConverter,
                validatorProcessor,
                valueProcessor);

        return processor;
    }

    private MockWriter mockWriter() {
        writer = mock(DataWriter.class);
        mockWriter = new MockWriter(writer);

        when(writer.indent()).thenReturn(writer);
        when(writer.write(anyString())).thenReturn(writer);
        when(writer.write(anyString(), anyString(), anyBoolean())).thenReturn(writer);
        when(writer.writeLine(anyString())).thenReturn(writer);
        when(writer.indentBack()).thenReturn(writer);
        when(writer.writeLine(anyString(), anyString(), anyBoolean())).thenReturn(writer);


        return new MockWriter(writer);
    }

    private class MockWriter implements DataWriter {

        private DataWriter mockWriter;

        private MockWriter(DataWriter mockWriter) {
            this.mockWriter = mockWriter;
        }

        @Override
        public boolean isOpen() {
            return mockWriter.isOpen();
        }

        @Override
        public DataWriter write(char c) {
            mockWriter.write("" + c);
            return this;
        }

        @Override
        public DataWriter write(String modelString) {
            mockWriter.write(modelString);
            return this;
        }

        @Override
        public DataWriter write(String modelString, String separator, boolean writeSeparator) {
            if( writeSeparator ) {
                mockWriter.write(modelString + separator);
            } else {
                mockWriter.write(modelString);
            }
            return this;
        }

        @Override
        public DataWriter writeLine(String modelStringLine) {
            mockWriter.write(modelStringLine + "\n");
            return this;
        }

        @Override
        public DataWriter writeLine(String modelStringLine, String separator, boolean writeSeparator) {

            if( writeSeparator ) {
                mockWriter.write(modelStringLine + separator + "\n");
            } else {
                mockWriter.write(modelStringLine + "\n");
            }
            return this;
        }

        @Override
        public void close() {
            mockWriter.close();
        }

        @Override
        public void setIndentation(int spaces) {
        }

        @Override
        public DataWriter indent() {
            return this;
        }

        @Override
        public DataWriter indentBack() {
            return this;
        }

        @Override
        public void setIndentationChar(char indentationChar, int characterCount) {
        }

        @Override
        public void setLineSeparator(String lineSeparator) {
        }
    }
}
