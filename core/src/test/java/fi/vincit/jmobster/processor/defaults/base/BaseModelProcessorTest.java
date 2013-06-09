package fi.vincit.jmobster.processor.defaults.base;

/*
 * Copyright 2012-2013 Juha Siponen
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
import fi.vincit.jmobster.processor.languages.DataWriterContext;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.writer.DataWriter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.never;

public class BaseModelProcessorTest {

    @Mock private DataWriter writer;
    @Mock private FieldValueConverter valueConverter;
    @Mock private ModelProcessor<DataWriterContext, DataWriter> validatorProcessor;
    @Mock private ModelProcessor<DataWriterContext, DataWriter> valueProcessor;

    private static enum BuildMode {
        ADD_MODEL_PROCESSORS,
        NO_MODEL_PROCESSORS
    }
/*
    private BackboneModelProcessor createProcessor(BuildMode buildMode) {
        BackboneModelProcessor.Builder processorBuilder =
                new BackboneModelProcessor
                        .Builder(writer, OutputMode.JAVASCRIPT)
                .setValueConverter(valueConverter);
        if( buildMode == BuildMode.ADD_MODEL_PROCESSORS ) {
            processorBuilder.setModelProcessors(validatorProcessor, valueProcessor);
        }
        return processorBuilder.build();
    }
    */

    private DataWriterContext getTestContext() {
        return new DataWriterContext(writer);
    }

    private static class TestModelProcessor extends BaseModelProcessor<DataWriterContext, DataWriter> {
        private TestModelProcessor(String name) {
            super(name);
        }

        @Override
        public void startProcessing(ItemStatus status) throws IOException {}

        @Override
        public void processModel(Model model, ItemStatus status) {}

        @Override
        public void endProcessing(ItemStatus status) throws IOException {}
    }

    private final int USE_NONE = 0;
    private final int USE_FIELD_CONVERTER = 1;
    private final int USE_CONTEXT = 2;
    private final int USE_BOTH = USE_CONTEXT | USE_FIELD_CONVERTER;

    private TestModelProcessor createProcessor(BuildMode buildMode, DataWriterContext context, int flags) {
        TestModelProcessor processor = new TestModelProcessor("");
        if( buildMode == BuildMode.ADD_MODEL_PROCESSORS ) {
            processor.addModelProcessor(validatorProcessor);
            processor.addModelProcessor(valueProcessor);
        }
        if( (flags & USE_CONTEXT) > 0 ) {
            processor.setLanguageContext(context);
        }
        if( (flags & USE_FIELD_CONVERTER) > 0 ) {
            processor.setFieldValueConverter(valueConverter);
        }
        return processor;
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetNewLanguageContextPropagation() {
        DataWriterContext context1 = getTestContext();
        DataWriterContext context2 = getTestContext();
        TestModelProcessor processor = createProcessor(BuildMode.ADD_MODEL_PROCESSORS, context1, USE_BOTH);

        processor.setLanguageContext(context2);

        Mockito.verify(validatorProcessor).setLanguageContext(context1);
        Mockito.verify(valueProcessor).setLanguageContext(context1);

        Mockito.verify(validatorProcessor).setLanguageContext(context2);
        Mockito.verify(valueProcessor).setLanguageContext(context2);
    }

    @Test
    public void testAddModelProcessor() {
        DataWriterContext context = getTestContext();
        TestModelProcessor processor = createProcessor(BuildMode.NO_MODEL_PROCESSORS, context, USE_BOTH);

        processor.addModelProcessor(validatorProcessor);

        Mockito.verify(validatorProcessor).setLanguageContext(context);
        Mockito.verify(validatorProcessor).setFieldValueConverter(valueConverter);
    }

    @Test
    public void testAddModelProcessor_NoContext() {
        DataWriterContext context = getTestContext();
        TestModelProcessor processor = createProcessor(BuildMode.NO_MODEL_PROCESSORS, context, USE_FIELD_CONVERTER);

        processor.addModelProcessor(validatorProcessor);

        Mockito.verify(validatorProcessor, never()).setLanguageContext(context);
        Mockito.verify(validatorProcessor).setFieldValueConverter(valueConverter);
    }

    @Test
    public void testAddModelProcessor_NoFieldConverter() {
        DataWriterContext context = getTestContext();
        TestModelProcessor processor = createProcessor(BuildMode.NO_MODEL_PROCESSORS, context, USE_CONTEXT);

        processor.addModelProcessor(validatorProcessor);

        Mockito.verify(validatorProcessor).setLanguageContext(context);
        Mockito.verify(validatorProcessor, never()).setFieldValueConverter(valueConverter);
    }

    @Test
    public void testAddModelProcessor_NoContextNorFieldConverter() {
        DataWriterContext context = getTestContext();
        TestModelProcessor processor = createProcessor(BuildMode.NO_MODEL_PROCESSORS, context, USE_NONE);

        processor.addModelProcessor(validatorProcessor);

        Mockito.verify(validatorProcessor, never()).setLanguageContext(context);
        Mockito.verify(validatorProcessor, never()).setFieldValueConverter(valueConverter);
    }
}
