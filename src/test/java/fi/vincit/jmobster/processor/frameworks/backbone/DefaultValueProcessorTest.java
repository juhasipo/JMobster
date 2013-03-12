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
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import fi.vincit.jmobster.util.writer.StringBufferWriter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class DefaultValueProcessorTest {

    private StringBufferWriter writer;
    @Mock private FieldValueConverter fieldValueConverter;

    @Before
    public void init() {
        writer = new StringBufferWriter();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStartProcessingFirst() throws Exception {
        DefaultValueProcessor processor = createProcessor();

        processor.startProcessing(ItemStatuses.first());

        assertThat(writer.toString(), is("function() {\n"));
    }

    @Test
    public void testStartProcessingMiddle() throws Exception {
        DefaultValueProcessor processor = createProcessor();

        processor.startProcessing(ItemStatuses.notFirstNorLast());

        assertThat(writer.toString(), is("function() {\n"));
    }

    @Test
    public void testStartProcessingLast() throws Exception {
        DefaultValueProcessor processor = createProcessor();

        processor.startProcessing(ItemStatuses.last());

        assertThat(writer.toString(), is("function() {\n"));
    }

    @Test
    public void testEndProcessingFirst() throws Exception {
        DefaultValueProcessor processor = createProcessor();

        processor.endProcessing(ItemStatuses.first());

        assertThat(writer.toString(), is("},\n"));
    }

    @Test
    public void testEndProcessingMiddle() throws Exception {
        DefaultValueProcessor processor = createProcessor();

        processor.endProcessing(ItemStatuses.notFirstNorLast());

        assertThat(writer.toString(), is("},\n"));
    }

    @Test
    public void testEndProcessingLast() throws Exception {
        DefaultValueProcessor processor = createProcessor();

        processor.endProcessing(ItemStatuses.last());

        assertThat(writer.toString(), is("}\n"));
    }

    @Test
    public void testProcessFirstModel() {
        DefaultValueProcessor processor = createProcessor();

        Model model = mock(Model.class);
        ModelField field1 = mock(ModelField.class); {
            when(field1.getFieldType()).thenReturn(String.class);
            when(field1.getName()).thenReturn("field1");
        }

        List<ModelField> fields = new ArrayList<ModelField>();
        fields.add(field1);
        when(model.getFields()).thenReturn(fields);
        mockFieldValueConverter();

        processor.processModel(model, ItemStatuses.first());

        assertThat(writer.toString(), is(
                "return {\n" +
                "    field1: 'Text'\n" +
                "}\n"
        ));
    }

    @Test
    public void testProcessFirstModelMultipleFields() {
        DefaultValueProcessor processor = createProcessor();

        Model model = mock(Model.class);

        ModelField field1 = mock(ModelField.class); {
            when(field1.getFieldType()).thenReturn(String.class);
            when(field1.getName()).thenReturn("field1");
        }
        ModelField field2 = mock(ModelField.class); {
            when(field2.getFieldType()).thenReturn(Long.class);
            when(field2.getName()).thenReturn("field2");
        }

        List<ModelField> fields = new ArrayList<ModelField>();
        fields.add(field1);
        fields.add(field2);
        when(model.getFields()).thenReturn(fields);
        mockFieldValueConverter();

        processor.processModel(model, ItemStatuses.first());

        assertThat(writer.toString(), is(
                "return {\n" +
                "    field1: 'Text',\n" +
                "    field2: 'Number'\n" +
                "}\n"
        ));
    }

    @Test
    public void testProcessLastModelMultipleFields() {
        DefaultValueProcessor processor = createProcessor();

        Model model = mock(Model.class);

        ModelField field1 = mock(ModelField.class); {
            when(field1.getFieldType()).thenReturn(String.class);
            when(field1.getName()).thenReturn("field1");
        }
        ModelField field2 = mock(ModelField.class); {
            when(field2.getFieldType()).thenReturn(Long.class);
            when(field2.getName()).thenReturn("field2");
        }

        List<ModelField> fields = new ArrayList<ModelField>();
        fields.add(field1);
        fields.add(field2);
        when(model.getFields()).thenReturn(fields);
        mockFieldValueConverter();

        processor.processModel(model, ItemStatuses.last());

        assertThat(writer.toString(), is(
                "return {\n" +
                        "    field1: 'Text',\n" +
                        "    field2: 'Number'\n" +
                        "}\n"
        ));
    }

    private void mockFieldValueConverter() {
        when(fieldValueConverter.convert(String.class, null)).thenReturn("'Text'");
        when(fieldValueConverter.convert(Long.class, null)).thenReturn("'Number'");
    }

    private DefaultValueProcessor createProcessor() {
        DefaultValueProcessor dp = new DefaultValueProcessor.Builder().setName("").build();
        dp.setWriter(new JavaScriptWriter(writer));
        dp.setFieldValueConverter(fieldValueConverter);
        return dp;
    }


}
