package fi.vincit.jmobster.processor.frameworks.backbone.type;

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

import fi.vincit.jmobster.processor.model.ModelField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class BackboneFieldTypeConverterManagerTest {

    public static final String NUMBER = "'Number'";
    public static final String TEXT = "'Text'";
    public static final String NOTHING = "";
    private Class input;
    private String expected;

    public BackboneFieldTypeConverterManagerTest(Class input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection primeNumbers() {
        return Arrays.asList(new Object[][]{
                {Integer.class, NUMBER},
                {Long.class, NUMBER},
                {Double.class, NUMBER},
                {Float.class, NUMBER},
                {String.class, TEXT},
                {Object.class, NOTHING}
        });
    }

    @Test
    public void test() {
        BackboneFieldTypeConverterManager converter =
                new BackboneFieldTypeConverterManager();
        ModelField field = mock(ModelField.class);
        when(field.getFieldType()).thenReturn(input);
        assertThat(converter.getType(field), is(expected));
    }
}
