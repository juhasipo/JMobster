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

import fi.vincit.jmobster.processor.FieldValueConverter;
import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.processor.defaults.DummyDataWriter;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptContext;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.processor.languages.javascript.writer.OutputMode;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class BackboneModelProcessorBuilderTest {

    private JavaScriptWriter writer;
    @Mock private FieldValueConverter valueConverter;
    @Mock private ModelProcessor<JavaScriptContext, JavaScriptWriter> validatorProcessor;
    @Mock private ModelProcessor<JavaScriptContext, JavaScriptWriter> valueProcessor;
    @Mock private Model model;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(model.getName()).thenReturn("Test");
        writer = new JavaScriptWriter(DummyDataWriter.getInstance());
    }

    @Test
    public void testBuildWithDefaultProcessors() {
        BackboneModelProcessor processor = new BackboneModelProcessor
                .Builder(writer, OutputMode.JAVASCRIPT)
                .setValueConverter(valueConverter)
                .useDefaultModelProcessors()
                .build();
        processor.doProcessModel(model, ItemStatuses.first());
    }

    @Test
    public void testBuildWithOneProcessors() {
        BackboneModelProcessor processor = new BackboneModelProcessor
                .Builder(writer, OutputMode.JAVASCRIPT)
                .setValueConverter(valueConverter)
                .setModelProcessors(validatorProcessor)
                .build();
        processor.doProcessModel(model, ItemStatuses.first());
    }

    @Test
    public void testBuildWithTwoProcessors() {
        JavaScriptContext context = new JavaScriptContext(writer, OutputMode.JSON);
        BackboneModelProcessor processor = new BackboneModelProcessor
                .Builder(context)
                .setValueConverter(valueConverter)
                .setModelProcessors(validatorProcessor, valueProcessor)
                .build();

        processor.doProcessModel(model, ItemStatuses.first());

        verify(validatorProcessor).setLanguageContext(context);
        verify(valueProcessor).setLanguageContext(context);
    }

    @Test
    public void testBuildWithRequiredOnly() {
        BackboneModelProcessor processor = new BackboneModelProcessor
                .Builder(writer, OutputMode.JAVASCRIPT)
                .build();

        processor.doProcessModel(model, ItemStatuses.first());
    }
}
