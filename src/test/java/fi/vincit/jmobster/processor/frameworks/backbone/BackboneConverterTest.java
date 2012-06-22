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

import fi.vincit.jmobster.processor.AnnotationProcessorProvider;
import fi.vincit.jmobster.processor.defaults.DefaultModelGenerator;
import fi.vincit.jmobster.processor.languages.javascript.JavaToJSValueConverter;
import fi.vincit.jmobster.processor.languages.javascript.valueconverters.ConverterMode;
import fi.vincit.jmobster.util.StreamModelWriter;
import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BackboneConverterTest {
    public static class TestModel {
        @NotNull
        @Size(min = 5, max = 255)
        private String string = "test string";

        @Size(min = 5, max = 255)
        @Pattern(regexp = "[a-z ]+")
        private String string2 = "test string";

        @Min(10)
        Long longValue = 42L;

        @Size(min = 5, max = 255)
        long[] longArray = {1L, 2L};

        @Size(min = 0, max = 100)
        String[] stringArray = {"Foo", "Bar", "FooBar", "123"};

        @NotNull
        List<Long> longList = new ArrayList<Long>();

        int fieldWithNoValidation = 12;

        public TestModel() {
            longList.add(1L);
            longList.add(100L);
        }

    }

    private OutputStream os;
    private StreamModelWriter modelWriter;

    @Before
    public void initModelWriter() {
        os = new ByteArrayOutputStream();
        modelWriter = new StreamModelWriter(os);
        modelWriter.setIndentation( 4 );
    }

    @Test
    public void testSimpleClass() {
        AnnotationProcessorProvider app = new DefaultAnnotationProcessorProvider();
        DefaultModelGenerator mg = new DefaultModelGenerator(new BackboneModelProcessor(modelWriter),
                new JavaToJSValueConverter( ConverterMode.ALLOW_NULL ), app);

        mg.process(TestModel.class);

        String referenceModel = "/*\n" +
                " * Auto-generated file\n" +
                " */\n" +
                "var Models = {\n" +
                "    TestModel: Backbone.Model.extend({\n" +
                "        defaults: function() {\n" +
                "            return {\n" +
                "                string: \"test string\",\n" +
                "                string2: \"test string\",\n" +
                "                longValue: 42,\n" +
                "                longArray: [1, 2],\n" +
                "                stringArray: [\"Foo\", \"Bar\", \"FooBar\", \"123\"],\n" +
                "                longList: [1, 100],\n" +
                "                fieldWithNoValidation: 12\n" +
                "            }\n" +
                "        },\n" +
                "        validate: {\n" +
                "            string: {\n" +
                "                required: true,\n" +
                "                minlength: 5,\n" +
                "                maxlength: 255\n" +
                "            },\n" +
                "            string2: {\n" +
                "                minlength: 5,\n" +
                "                maxlength: 255,\n" +
                "                pattern: /[a-z ]+/\n" +
                "            },\n" +
                "            longValue: {\n" +
                "                type: \"number\",\n" +
                "                min: 10\n" +
                "            },\n" +
                "            longArray: {\n" +
                "                minlength: 5,\n" +
                "                maxlength: 255\n" +
                "            },\n" +
                "            stringArray: {\n" +
                "                minlength: 0,\n" +
                "                maxlength: 100\n" +
                "            },\n" +
                "            longList: {\n" +
                "                required: true\n" +
                "            },\n" +
                "            fieldWithNoValidation: {\n" +
                "            }\n" +
                "        }\n" +
                "    })\n" +
                "};\n";
        assertEquals(referenceModel, os.toString());
    }

    public static class NoValidationsClass {
        private String stringValue = "String value";
        private double[] doubleArray = {1.0d, 1.5d};
        private float[] floatArray = { 0.0f, 0.1f, 0.2f };
        private boolean[] booleanArray = { true, false, true, true };
    }

    @Test
    public void testNoValidationsClass() {
        AnnotationProcessorProvider app = new DefaultAnnotationProcessorProvider();
        DefaultModelGenerator mg = new DefaultModelGenerator(new BackboneModelProcessor(modelWriter),
                new JavaToJSValueConverter( ConverterMode.ALLOW_NULL ), app);

        mg.process(NoValidationsClass.class);

        String referenceModel = "/*\n" +
                " * Auto-generated file\n" +
                " */\n" +
                "var Models = {\n" +
                "    NoValidationsClass: Backbone.Model.extend({\n" +
                "        defaults: function() {\n" +
                "            return {\n" +
                "                stringValue: \"String value\",\n" +
                "                doubleArray: [1.0, 1.5],\n" +
                "                floatArray: [0.0, 0.1, 0.2],\n" +
                "                booleanArray: [true, false, true, true]\n" +
                "            }\n" +
                "        }\n" +
                "    })\n" +
                "};\n";
        assertEquals(referenceModel, os.toString());
    }
}
