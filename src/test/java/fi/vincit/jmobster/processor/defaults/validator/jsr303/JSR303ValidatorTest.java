package fi.vincit.jmobster.processor.defaults.validator.jsr303;

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

import fi.vincit.jmobster.annotation.OverridePattern;
import fi.vincit.jmobster.util.collection.AnnotationBag;
import fi.vincit.jmobster.util.TestUtil;
import org.junit.Test;

import javax.validation.constraints.*;
import java.lang.annotation.Annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JSR303ValidatorTest {
    @Test
    public void testMin() throws Exception {
        class MinClass { @Min(1) public int min; }

        Annotation minAnnotation = TestUtil.getAnnotationFromClass( MinClass.class, 0, 0 );
        AnnotationBag annotationBag = TestUtil.generateAnnotationBag( minAnnotation );

        NumberRangeValidator minValidator = new NumberRangeValidator();
        minValidator.init(annotationBag);

        assertTrue( minValidator.hasMin() );
        assertFalse( minValidator.hasMax() );
        assertEquals( 1, minValidator.getMin() );
    }

    @Test
    public void testMax() throws Exception {
        class MaxClass { @Max(255) public int max; }

        Annotation maxAnnotation = TestUtil.getAnnotationFromClass( MaxClass.class, 0, 0 );
        AnnotationBag annotationBag = TestUtil.generateAnnotationBag( maxAnnotation );

        NumberRangeValidator maxValidator = new NumberRangeValidator();
        maxValidator.init( annotationBag );

        assertFalse( maxValidator.hasMin() );
        assertTrue( maxValidator.hasMax() );
        assertEquals( 255, maxValidator.getMax() );
    }

    @Test
    public void testMinAndMax() throws Exception {
        class MinMaxClass { @Min(1) @Max(255) public int max; }

        Annotation[] minAndMaxAnnotations = TestUtil.getAnnotationsFromClassField(MinMaxClass.class, 0);
        AnnotationBag annotationBag = TestUtil.generateAnnotationBag( minAndMaxAnnotations );

        NumberRangeValidator minAndMaxValidator = new NumberRangeValidator();
        minAndMaxValidator.init( annotationBag );

        assertTrue( minAndMaxValidator.hasMin() );
        assertTrue( minAndMaxValidator.hasMax() );
        assertEquals( 1, minAndMaxValidator.getMin() );
        assertEquals( 255, minAndMaxValidator.getMax() );
    }

    @Test
    public void testSizeWithMin() throws Exception {
        class SizeClass { @Size(min=1) public int size; }

        Annotation sizeAnnotation = TestUtil.getAnnotationFromClass( SizeClass.class, 0, 0 );
        AnnotationBag annotationBag = TestUtil.generateAnnotationBag( sizeAnnotation );

        SizeValidator sizeValidator = new SizeValidator();
        sizeValidator.init( annotationBag );

        assertEquals( 1, sizeValidator.getMin() );
        assertEquals( Integer.MAX_VALUE, sizeValidator.getMax() );
        assertTrue( sizeValidator.hasMin() );
        assertFalse( sizeValidator.hasMax() );
    }

    @Test
    public void testSizeWithMax() throws Exception {
        class SizeClass { @Size(max=255) public int size; }

        Annotation sizeAnnotation = TestUtil.getAnnotationFromClass( SizeClass.class, 0, 0 );
        AnnotationBag annotationBag = TestUtil.generateAnnotationBag( sizeAnnotation );

        SizeValidator sizeValidator = new SizeValidator();
        sizeValidator.init( annotationBag );

        assertEquals( 0, sizeValidator.getMin() );
        assertEquals( 255, sizeValidator.getMax() );
        assertTrue( sizeValidator.hasMin() );
        assertTrue( sizeValidator.hasMax() );
    }

    @Test
    public void testSizeWithMinAndMax() throws Exception {
        class SizeClass { @Size(min=1,max=255) public int size; }

        Annotation sizeAnnotation = TestUtil.getAnnotationFromClass( SizeClass.class, 0, 0 );
        AnnotationBag annotationBag = TestUtil.generateAnnotationBag( sizeAnnotation );

        SizeValidator sizeValidator = new SizeValidator();
        sizeValidator.init( annotationBag );

        assertEquals( 1, sizeValidator.getMin() );
        assertEquals( 255, sizeValidator.getMax() );
        assertTrue( sizeValidator.hasMin() );
        assertTrue( sizeValidator.hasMax() );
    }

    @Test
    public void testNotNull() throws Exception {
        class NotNullClass { @NotNull public int size; }

        Annotation notNullAnnotation = TestUtil.getAnnotationFromClass( NotNullClass.class, 0, 0 );
        AnnotationBag annotationBag = TestUtil.generateAnnotationBag( notNullAnnotation );

        NotNullValidator notNullValidator = new NotNullValidator();
        notNullValidator.init( annotationBag );
    }

    @Test
    public void testPattern() throws Exception {
        class PatternClass { @Pattern(regexp = "testregexp") public int size; }

        Annotation patternAnnotation = TestUtil.getAnnotationFromClass( PatternClass.class, 0, 0 );
        AnnotationBag annotationBag = TestUtil.generateAnnotationBag( patternAnnotation );

        PatternValidator patternValidator = new PatternValidator();
        patternValidator.init( annotationBag );

        assertEquals( "testregexp", patternValidator.getRegexp() );
        assertEquals( 0, patternValidator.getFlags().length );
    }

    @Test
    public void testPatternWithFlags() throws Exception {
        class PatternClass { @Pattern(regexp = "testregexp", flags = { Pattern.Flag.CASE_INSENSITIVE }) public int size; }

        Annotation patternAnnotation = TestUtil.getAnnotationFromClass( PatternClass.class, 0, 0 );
        AnnotationBag annotationBag = TestUtil.generateAnnotationBag( patternAnnotation );

        PatternValidator patternValidator = new PatternValidator();
        patternValidator.init( annotationBag );

        assertEquals( "testregexp", patternValidator.getRegexp() );
        assertEquals( 1, patternValidator.getFlags().length );
        assertEquals( Pattern.Flag.CASE_INSENSITIVE, patternValidator.getFlags()[0] );
    }

    @Test
    public void testOverriddenPattern() throws Exception {
        class PatternClass { @Pattern(regexp = "testregexp") @OverridePattern(regexp = "overridden")public int size; }

        Annotation patternAnnotation = TestUtil.getAnnotationFromClass( PatternClass.class, 0, 0 );
        Annotation overrideAnnotation = TestUtil.getAnnotationFromClass( PatternClass.class, 0, 1 );
        AnnotationBag annotationBag = TestUtil.generateAnnotationBag( patternAnnotation, overrideAnnotation );

        PatternValidator patternValidator = new PatternValidator();
        patternValidator.init( annotationBag );

        assertEquals( "overridden", patternValidator.getRegexp() );
        assertEquals( 0, patternValidator.getFlags().length );
    }

}
