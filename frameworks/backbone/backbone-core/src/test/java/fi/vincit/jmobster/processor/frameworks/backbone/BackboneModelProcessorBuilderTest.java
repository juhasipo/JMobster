package fi.vincit.jmobster.processor.frameworks.backbone;

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
import fi.vincit.jmobster.processor.defaults.DummyDataWriter;
import fi.vincit.jmobster.processor.defaults.validator.ValidatorWriterSet;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptContext;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.processor.languages.javascript.writer.OutputMode;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BackboneModelProcessorBuilderTest {

    private JavaScriptWriter writer;
    private JavaScriptContext context;
    @Mock private FieldValueConverter valueConverter;
    @Mock private ModelProcessor<JavaScriptContext, JavaScriptWriter> validatorProcessor;
    @Mock private ModelProcessor<JavaScriptContext, JavaScriptWriter> valueProcessor;
    @Mock private Model model;
    @Mock private ValidatorWriterSet validatorWriterManager;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(model.getName()).thenReturn("Test");
        writer = new JavaScriptWriter(DummyDataWriter.getInstance());
        context = new JavaScriptContext(writer, OutputMode.JAVASCRIPT);
    }

    @Test
    public void testBuildWithDefaultProcessors() {
        BackboneModelProcessor processor = new BackboneModelProcessor
                .Builder(context)
                .useDefaultModelProcessors(validatorWriterManager)
                .build();
        processor.doProcessModel(model, ItemStatuses.first());
    }

    @Test
    public void testBuildWithOneProcessors() {
        BackboneModelProcessor processor = new BackboneModelProcessor
                .Builder(context)
                .setModelProcessors(validatorProcessor)
                .build();
        processor.doProcessModel(model, ItemStatuses.first());
    }

    @Test
    public void testBuildWithTwoProcessors() {
        BackboneModelProcessor processor = new BackboneModelProcessor
                .Builder(context)
                .setModelProcessors(validatorProcessor, valueProcessor)
                .build();

        processor.doProcessModel(model, ItemStatuses.first());

        verify(validatorProcessor).setLanguageContext(context);
        verify(valueProcessor).setLanguageContext(context);
    }

    @Test
    public void testBuildWithRequiredOnly() {
        BackboneModelProcessor processor = new BackboneModelProcessor
                .Builder(context)
                .build();

        processor.doProcessModel(model, ItemStatuses.first());
    }
}
