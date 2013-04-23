package fi.vincit.jmobster.processor.frameworks.backbone;

import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.processor.languages.javascript.writer.OutputMode;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BackboneModelProcessorFullModeTest extends BaseBackboneModelProcessorTest {

    @Override
    protected OutputMode getMode() {
        return OutputMode.JAVASCRIPT;
    }

    @Test
    public void testStartProcessing() throws Exception {
        ModelProcessor processor = createProcessor();

        processor.startProcessing(ItemStatuses.firstAndLast());

        assertThat(writer.toString(), is(
                "/*\n" +
                " * Auto-generated file\n" +
                " */\n" +
                "var Models = {\n"
        ));
    }

    @Test
    public void testStartProcessingWithCustomNamespace() throws Exception {
        BackboneModelProcessor processor = createProcessor();
        processor.setNamespaceName("Foo");

        processor.startProcessing(ItemStatuses.firstAndLast());

        assertThat(writer.toString(), is(
                "/*\n" +
                " * Auto-generated file\n" +
                " */\n" +
                "var Foo = {\n"
        ));
    }

    @Test
    public void testStartProcessingWithCustomComment() throws Exception {
        BackboneModelProcessor processor = createProcessor();
        processor.setStartComment("Foo");

        processor.startProcessing(ItemStatuses.firstAndLast());

        assertThat(writer.toString(), is(
                "/*\n * Foo\n */\n" +
                "var Models = {\n"
        ));
    }

    @Test
    public void testEndProcessing() throws Exception {
        ModelProcessor processor = createProcessor();
        jsWriter.setLenientMode(true);
        processor.endProcessing(ItemStatuses.firstAndLast());

        assertThat(writer.toString(), is("};\n"));
        assertThat(writer.isOpen(), is(false));
    }

    @Test
    public void testProcessFirstModel() throws Exception{
        ModelProcessor processor = createProcessor();

        Model model = mockModel();
        processor.processModel(model, ItemStatuses.first());

        assertThat(writer.toString(), is(
                "TestModel: Backbone.Model.extend({\n" +
                "    validation: {\n" +
                "    },\n" +
                "    defaults: {\n" +
                "    }\n" +
                "}),\n"
        ));
    }

    @Test
    public void testProcessMiddleModel() throws Exception{
        ModelProcessor processor = createProcessor();

        Model model = mockModel();
        processor.processModel(model, ItemStatuses.notFirstNorLast());

        assertThat(writer.toString(), is(
                "TestModel: Backbone.Model.extend({\n" +
                "    validation: {\n" +
                "    },\n" +
                "    defaults: {\n" +
                "    }\n" +
                "}),\n"
        ));
    }

    @Test
    public void testProcessLastModel() throws Exception{
        ModelProcessor processor = createProcessor();

        Model model = mockModel();
        processor.processModel(model, ItemStatuses.last());

        assertThat(writer.toString(), is(
                "TestModel: Backbone.Model.extend({\n" +
                "    validation: {\n" +
                "    },\n" +
                "    defaults: {\n" +
                "    }\n" +
                "})\n"
        ));
    }


}
