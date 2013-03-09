package fi.vincit.jmobster.processor.frameworks.backbone;

/*
 * Copyright 2012 Juha Siponen
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

import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BackboneModelProcessorJSONModeTest extends BaseBackboneModelProcessorTest {

    @Override
    protected BackboneModelProcessor.Mode getMode() {
        return BackboneModelProcessor.Mode.JSON;
    }

    @Test
    public void testStartProcessing() throws Exception {
        ModelProcessor processor = createProcessor();

        processor.startProcessing(ItemStatuses.firstAndLast());

        assertThat(writer.toString(), is("{\n"
        ));
    }

    @Test
    public void testStartProcessingWithCustomNamespace() throws Exception {
        BackboneModelProcessor processor = createProcessor();
        processor.setNamespaceName("Foo");

        processor.startProcessing(ItemStatuses.firstAndLast());

        assertThat(writer.toString(), is("{\n"));
    }

    @Test
    public void testStartProcessingWithCustomComment() throws Exception {
        BackboneModelProcessor processor = createProcessor();
        processor.setStartComment("/* Foo */");

        processor.startProcessing(ItemStatuses.firstAndLast());

        assertThat(writer.toString(), is("{\n"));
    }

    @Test
    public void testEndProcessing() throws Exception {
        ModelProcessor processor = createProcessor();

        processor.endProcessing(ItemStatuses.firstAndLast());

        assertThat(writer.toString(), is("}\n"));
        assertThat(writer.isOpen(), is(false));
    }

    @Test
    public void testProcessFirstModel() throws Exception{
        ModelProcessor processor = createProcessor();

        Model model = mockModel();
        processor.processModel(model, ItemStatuses.first());

        assertThat(writer.toString(), is(
                "\"TestModel\": {\n" +
                "    \"validation\": {\n" +
                "    },\n" +
                "    \"defaults\": {\n" +
                "    }\n" +
                "},\n"
        ));
    }

    @Test
    public void testProcessMiddleModel() throws Exception{
        ModelProcessor processor = createProcessor();

        Model model = mockModel();
        processor.processModel(model, ItemStatuses.notFirstNorLast());

        assertThat(writer.toString(), is(
                "\"TestModel\": {\n" +
                "    \"validation\": {\n" +
                "    },\n" +
                "    \"defaults\": {\n" +
                "    }\n" +
                "},\n"
        ));
    }

    @Test
    public void testProcessLastModel() throws Exception{
        ModelProcessor processor = createProcessor();

        Model model = mockModel();
        processor.processModel(model, ItemStatuses.last());

        assertThat(writer.toString(), is(
                "\"TestModel\": {\n" +
                "    \"validation\": {\n" +
                "    },\n" +
                "    \"defaults\": {\n" +
                "    }\n" +
                "}\n"
        ));
    }
}
