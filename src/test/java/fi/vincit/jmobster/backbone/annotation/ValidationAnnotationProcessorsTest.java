package fi.vincit.jmobster.backbone.annotation;
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

import fi.vincit.jmobster.util.ModelWriter;
import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;

public class ValidationAnnotationProcessorsTest {

    private OutputStream os;
    private ModelWriter modelWriter;

    @Before
    public void initModelWriter() {
        os = new ByteArrayOutputStream();
        modelWriter = new ModelWriter(os);
        modelWriter.setIndentation( 0 );
    }

    @Test
    public void testMinAnnotationProcessor() {
        class T { @Min(1) int i; }
        MinAnnotationProcessor m = new MinAnnotationProcessor();
        m.writeValidatorsToStream(T.class.getDeclaredFields()[0].getAnnotations()[0], modelWriter);
        modelWriter.close();
        assertEquals( "min: 1", os.toString() );
    }

    @Test
    public void testMaxAnnotationProcessor() {
        class T { @Max(100) int i; }
        MaxAnnotationProcessor m = new MaxAnnotationProcessor();
        m.writeValidatorsToStream(T.class.getDeclaredFields()[0].getAnnotations()[0], modelWriter);
        modelWriter.close();
        assertEquals("max: 100", os.toString());
    }

    @Test
    public void testPatternAnnotationProcessorSimple() {
        class T { @Pattern(regexp = "[\\dA-Z]") int i; }
        PatternAnnotationProcessor m = new PatternAnnotationProcessor();
        m.writeValidatorsToStream(T.class.getDeclaredFields()[0].getAnnotations()[0], modelWriter);
        modelWriter.close();
        assertEquals("pattern: /[\\dA-Z]/", os.toString());
    }

    @Test
    public void testSizeAnnotationProcessor() {
        class T { @Size(min = 1, max = 255) String s; }
        SizeAnnotationProcessor m = new SizeAnnotationProcessor();
        m.writeValidatorsToStream(T.class.getDeclaredFields()[0].getAnnotations()[0], modelWriter);
        modelWriter.close();
        assertEquals("minlength: 1,\nmaxlength: 255", os.toString());
    }

    @Test
    public void testNotNullAnnotationProcessor() {
        class T { @NotNull String s; }
        NotNullAnnotationProcessor m = new NotNullAnnotationProcessor();
        m.writeValidatorsToStream(T.class.getDeclaredFields()[0].getAnnotations()[0], modelWriter);
        modelWriter.close();
        assertEquals("required: true", os.toString());
    }

}
