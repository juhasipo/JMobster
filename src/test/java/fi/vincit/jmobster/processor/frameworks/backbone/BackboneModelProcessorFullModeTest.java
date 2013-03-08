package fi.vincit.jmobster.processor.frameworks.backbone;

import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import org.junit.Test;
import org.mockito.InOrder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;

public class BackboneModelProcessorFullModeTest extends BaseBackboneModelProcessorTest {

    @Override
    protected BackboneModelProcessor.Mode getMode() {
        return BackboneModelProcessor.Mode.FULL;
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
        processor.setStartComment("/* Foo */");

        processor.startProcessing(ItemStatuses.firstAndLast());

        assertThat(writer.toString(), is(
                "/* Foo */\n" +
                        "var Models = {\n"
        ));
    }

    @Test
    public void testEndProcessing() throws Exception {
        ModelProcessor processor = createProcessor();

        processor.endProcessing(ItemStatuses.firstAndLast());

        assertThat(writer.toString(), is("};\n"));
        assertThat(writer.isOpen(), is(false));
    }

    @Test
    public void testProcessFirstModel() throws Exception{
        ModelProcessor processor = createProcessor();

        Model model = mockModel();
        processor.processModel(model, ItemStatuses.first());

        InOrder order = inOrder(validatorProcessor, valueProcessor);
        order.verify(validatorProcessor).startProcessing(ItemStatuses.first());
        order.verify(validatorProcessor).processModel(model, ItemStatuses.firstAndLast());
        order.verify(validatorProcessor).endProcessing(ItemStatuses.first());

        order.verify(valueProcessor).startProcessing(ItemStatuses.last());
        order.verify(valueProcessor).processModel(model, ItemStatuses.firstAndLast());
        order.verify(valueProcessor).endProcessing(ItemStatuses.last());

        assertThat(writer.toString(), is(
                "TestModel: Backbone.Model.extend({\n" +
                        "    validation: defaults: \n" +
                        "})," +
                        "\n"
        ));
    }

    @Test
    public void testProcessMiddleModel() throws Exception{
        ModelProcessor processor = createProcessor();

        Model model = mockModel();
        processor.processModel(model, ItemStatuses.notFirstNorLast());

        InOrder order = inOrder(validatorProcessor, valueProcessor);
        order.verify(validatorProcessor).startProcessing(ItemStatuses.first());
        order.verify(validatorProcessor).processModel(model, ItemStatuses.firstAndLast());
        order.verify(validatorProcessor).endProcessing(ItemStatuses.first());

        order.verify(valueProcessor).startProcessing(ItemStatuses.last());
        order.verify(valueProcessor).processModel(model, ItemStatuses.firstAndLast());
        order.verify(valueProcessor).endProcessing(ItemStatuses.last());

        assertThat(writer.toString(), is(
                "TestModel: Backbone.Model.extend({\n" +
                        "    validation: defaults: \n" +
                        "})," +
                        "\n"
        ));
    }

    @Test
    public void testProcessLastModel() throws Exception{
        ModelProcessor processor = createProcessor();

        Model model = mockModel();
        processor.processModel(model, ItemStatuses.last());

        InOrder order = inOrder(validatorProcessor, valueProcessor);
        order.verify(validatorProcessor).startProcessing(ItemStatuses.first());
        order.verify(validatorProcessor).processModel(model, ItemStatuses.firstAndLast());
        order.verify(validatorProcessor).endProcessing(ItemStatuses.first());

        order.verify(valueProcessor).startProcessing(ItemStatuses.last());
        order.verify(valueProcessor).processModel(model, ItemStatuses.firstAndLast());
        order.verify(valueProcessor).endProcessing(ItemStatuses.last());

        assertThat(writer.toString(), is(
                "TestModel: Backbone.Model.extend({\n" +
                        "    validation: defaults: \n" +
                        "})" +
                        "\n"));
    }


}
