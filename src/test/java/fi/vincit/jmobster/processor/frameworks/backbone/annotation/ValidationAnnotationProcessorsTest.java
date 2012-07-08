package fi.vincit.jmobster.processor.frameworks.backbone.annotation;
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

import fi.vincit.jmobster.annotation.OverridePattern;
import fi.vincit.jmobster.util.StreamModelWriter;
import fi.vincit.jmobster.util.ModelWriter;
import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ValidationAnnotationProcessorsTest {

    private OutputStream os;
    private ModelWriter modelWriter;

    @Before
    public void initModelWriter() {
        os = new ByteArrayOutputStream();
        modelWriter = new StreamModelWriter(os);
        modelWriter.setIndentation( 0 );
    }

    @Test
    public void testMinAnnotationProcessor() {
        class T { @Min(1) int i; }
        MinAnnotationProcessor m = new MinAnnotationProcessor();
        m.writeValidatorsToStream(toList(get(T.class)), modelWriter);
        modelWriter.close();
        assertEquals( "min: 1", os.toString() );
    }

    @Test
    public void testMaxAnnotationProcessor() {
        class T { @Max(100) int i; }
        MaxAnnotationProcessor m = new MaxAnnotationProcessor();
        m.writeValidatorsToStream(toList(get(T.class)), modelWriter);
        modelWriter.close();
        assertEquals( "max: 100", os.toString() );
    }

    @Test
    public void testPatternAnnotationProcessorSimple() {
        class T { @Pattern(regexp = "[\\dA-Z]") int i; }
        PatternAnnotationProcessor m = new PatternAnnotationProcessor();
        m.writeValidatorsToStream(toList(get(T.class)), modelWriter);
        modelWriter.close();
        assertEquals("pattern: /[\\dA-Z]/", os.toString());
    }

    @Test
    public void testPatternWithOverrideAnnotationProcessor() {
        class T { @OverridePattern(regexp="/[abcde]/")@Pattern(regexp = "[\\dA-Z]") int i; }
        PatternAnnotationProcessor m = new PatternAnnotationProcessor();
        m.writeValidatorsToStream(toList(get(T.class, 0, 0), get(T.class, 0, 1)), modelWriter);
        modelWriter.close();
        assertEquals("pattern: /[abcde]/", os.toString());
    }

    @Test
    public void testSizeAnnotationProcessor() {
        class T { @Size(min = 1, max = 255) String s; }
        SizeAnnotationProcessor m = new SizeAnnotationProcessor();
        m.writeValidatorsToStream(toList(get(T.class)), modelWriter);
        modelWriter.close();
        assertEquals("minlength: 1,\nmaxlength: 255", os.toString());
    }

    @Test
    public void testSizeMinOnlyAnnotationProcessor() {
        class T { @Size(min = 10) String s; }
        SizeAnnotationProcessor m = new SizeAnnotationProcessor();
        m.writeValidatorsToStream(toList(get(T.class)), modelWriter);
        modelWriter.close();
        assertEquals("minlength: 10", os.toString());
    }

    @Test
    public void testSizeMaxOnlyAnnotationProcessor() {
        class T { @Size(max = 1000) String s; }
        SizeAnnotationProcessor m = new SizeAnnotationProcessor();
        m.writeValidatorsToStream(toList(get(T.class)), modelWriter);
        modelWriter.close();
        assertEquals("maxlength: 1000", os.toString());
    }

    @Test
    public void testNotNullAnnotationProcessor() {
        class T { @NotNull String s; }
        NotNullAnnotationProcessor m = new NotNullAnnotationProcessor();
        m.writeValidatorsToStream(toList(get(T.class)), modelWriter);
        modelWriter.close();
        assertEquals("required: true", os.toString());
    }

    private static List<Annotation> toList(Annotation...classes) {
        List<Annotation> list = new ArrayList<Annotation>(classes.length);
        for( Annotation c : classes ) {
            list.add(c);
        }
        return list;
    }

    private Annotation get(Class clazz) {
        return get(clazz, 0, 0);
    }

    private Annotation get(Class clazz, int field, int annotation) {
        return clazz.getDeclaredFields()[field].getAnnotations()[annotation];
    }

}
