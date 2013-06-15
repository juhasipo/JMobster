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
import fi.vincit.jmobster.processor.defaults.validator.ValidatorWriterSet;
import fi.vincit.jmobster.processor.languages.javascript.writer.OutputMode;
import fi.vincit.jmobster.processor.model.FieldAnnotation;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import fi.vincit.jmobster.util.writer.StringBufferWriter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ValidatorProcessorTest {

    private StringBufferWriter writer;
    @Mock private FieldValueConverter fieldValueConverter;
    @Mock private ValidatorWriterSet validatorWriterManager;
    @Before
    public void init() {
        writer = new StringBufferWriter();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStartProcessingFirst() throws Exception {
        ValidatorProcessor processor = createProcessor();

        processor.doStartProcessing(ItemStatuses.first());

        assertThat(writer.toString(), is("{\n"));
    }

    @Test
    public void testStartProcessingLast() throws Exception {
        ValidatorProcessor processor = createProcessor();

        processor.doStartProcessing(ItemStatuses.last());

        assertThat(writer.toString(), is("{\n"));
    }

    @Test
    public void testEndProcessingFirst() throws Exception {
        ValidatorProcessor processor = createProcessor();

        processor.doEndProcessing(ItemStatuses.first());

        assertThat(writer.toString(), is("},\n"));
    }

    @Test
    public void testEndProcessingLast() throws Exception {
        ValidatorProcessor processor = createProcessor();

        processor.doEndProcessing(ItemStatuses.last());

        assertThat(writer.toString(), is("}\n"));
    }

    @Test
    public void testProcessModel() throws Exception {
        ValidatorProcessor processor = createProcessor();

        Model model = mock(Model.class);
        ModelField field1 = mock(ModelField.class); {
            when(field1.getFieldType()).thenReturn(String.class);
            when(field1.getName()).thenReturn("field1");
        }

        List<ModelField> fields = new ArrayList<ModelField>();
        fields.add(field1);
        when(model.getFields()).thenReturn(fields);
        mockFieldValueConverter();

        processor.doProcessModel(model, ItemStatuses.first());

        assertThat(writer.toString(), is(
                "field1: {\n" +
                "}" +
                "\n"
        ));
    }

    @Test
    public void testProcessModelWithValidator() throws Exception {
        ValidatorProcessor processor = createProcessor();

        Model model = mock(Model.class);
        ModelField field1 = mock(ModelField.class); {
            when(field1.getFieldType()).thenReturn(String.class);
            when(field1.getName()).thenReturn("field1");
            when(field1.hasAnnotations()).thenReturn(true);

            List<FieldAnnotation> validators = new ArrayList<FieldAnnotation>();
            when(field1.getAnnotations()).thenReturn(validators);
            validators.add(mock(FieldAnnotation.class));
        }

        List<ModelField> fields = new ArrayList<ModelField>();
        fields.add(field1);
        when(model.getFields()).thenReturn(fields);
        mockFieldValueConverter();

        mockValidators("Foo: bar");

        processor.doProcessModel(model, ItemStatuses.first());

        assertThat(writer.toString(), is(
                "field1: {\n" +
                "    Foo: bar\n" +
                "}\n"
        ));
    }

    @Test
    public void testProcessModelWithValidators() throws Exception {
        ValidatorProcessor processor = createProcessor();

        Model model = mock(Model.class);
        ModelField field1 = mock(ModelField.class); {
            when(field1.getFieldType()).thenReturn(String.class);
            when(field1.getName()).thenReturn("field1");
            when(field1.hasAnnotations()).thenReturn(true);

            List<FieldAnnotation> validators = new ArrayList<FieldAnnotation>();
            when(field1.getAnnotations()).thenReturn(validators);
            validators.add(mock(FieldAnnotation.class));
            validators.add(mock(FieldAnnotation.class));
            validators.add(mock(FieldAnnotation.class));
        }

        List<ModelField> fields = new ArrayList<ModelField>();
        fields.add(field1);
        when(model.getFields()).thenReturn(fields);
        mockFieldValueConverter();

        mockValidators("validator1: test1,\n", "    validator2: test2,\n", "    validator3: test3");

        processor.doProcessModel(model, ItemStatuses.first());

        assertThat(writer.toString(), is(
                "field1: {\n" +
                "    validator1: test1,\n" +
                "    validator2: test2,\n" +
                "    validator3: test3\n" +
                "}\n"
        ));
    }

    private ValidatorProcessor createProcessor() {
        return new ValidatorProcessor.Builder()
                .setValueConverter(fieldValueConverter)
                .setWriter(writer, OutputMode.JSON)
                .setValidatorWriters(validatorWriterManager)
                .build();
    }

    private void mockFieldValueConverter() {
        when(fieldValueConverter.convert(String.class, null)).thenReturn("'Text'");
        when(fieldValueConverter.convert(Long.class, null)).thenReturn("'Number'");
    }

    private void mockValidators(final String... validatorStrings) {
        if( validatorStrings.length == 0 ) {
            doNothing().when(validatorWriterManager).write(anyCollection(), any(ItemStatus.class));
        } else {
            doAnswer(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    int numOfAnnotations = ((Collection)invocation.getArguments()[0]).size();
                    for( int i = 0; i < numOfAnnotations; ++i ) {
                        assert i <= validatorStrings.length : "Out of validator strings";
                        writer.write(validatorStrings[i]);
                    }
                    return null;
                }
            }).when(validatorWriterManager).write(anyCollection(), any(ItemStatus.class));
        }
    }
}
