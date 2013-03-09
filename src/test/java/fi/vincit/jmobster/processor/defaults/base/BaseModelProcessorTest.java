package fi.vincit.jmobster.processor.defaults.base;

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

import fi.vincit.jmobster.processor.FieldValueConverter;
import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.writer.DataWriter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

public class BaseModelProcessorTest {

    @Mock private DataWriter writer;
    @Mock private FieldValueConverter valueConverter;
    @Mock private ModelProcessor<DataWriter> validatorProcessor;
    @Mock private ModelProcessor<DataWriter> valueProcessor;

    private static enum BuildMode {
        ADD_MODEL_PROCESSORS,
        NO_MODEL_PROCESSORS
    }
/*
    private BackboneModelProcessor createProcessor(BuildMode buildMode) {
        BackboneModelProcessor.Builder processorBuilder =
                new BackboneModelProcessor
                        .Builder(writer, OutputMode.NORMAL)
                .setValueConverter(valueConverter);
        if( buildMode == BuildMode.ADD_MODEL_PROCESSORS ) {
            processorBuilder.setModelProcessors(validatorProcessor, valueProcessor);
        }
        return processorBuilder.build();
    }
    */

    private static class TestModelProcessor extends BaseModelProcessor<DataWriter> {
        private TestModelProcessor(String name, DataWriter writer, FieldValueConverter valueConverter) {
            super(name, writer, valueConverter);
        }

        @Override
        public void startProcessing(ItemStatus status) throws IOException {}

        @Override
        public void processModel(Model model, ItemStatus status) {}

        @Override
        public void endProcessing(ItemStatus status) throws IOException {}
    }

    private TestModelProcessor createProcessor(BuildMode buildMode) {
        TestModelProcessor processor = new TestModelProcessor("", writer, valueConverter);
        if( buildMode == BuildMode.ADD_MODEL_PROCESSORS ) {
            processor.addModelProcessor(validatorProcessor);
            processor.addModelProcessor(valueProcessor);
        }
        return processor;
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetNewWriterPropagation() {
        TestModelProcessor processor = createProcessor(BuildMode.ADD_MODEL_PROCESSORS);

        DataWriter anotherWriter = Mockito.mock(DataWriter.class);
        processor.setWriter(anotherWriter);

        Mockito.verify(validatorProcessor).setWriter(writer);
        Mockito.verify(valueProcessor).setWriter(writer);

        Mockito.verify(validatorProcessor).setWriter(anotherWriter);
        Mockito.verify(valueProcessor).setWriter(anotherWriter);
    }

    @Test
    public void testAddModelProcessor() {
        TestModelProcessor processor = createProcessor(BuildMode.NO_MODEL_PROCESSORS);

        processor.addModelProcessor(validatorProcessor);

        Mockito.verify(validatorProcessor).setWriter(writer);
    }
}
