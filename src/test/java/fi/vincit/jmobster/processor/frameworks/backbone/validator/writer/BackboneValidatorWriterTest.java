package fi.vincit.jmobster.processor.frameworks.backbone.validator.writer;

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

import fi.vincit.jmobster.processor.defaults.validator.jsr303.NotNullValidator;
import fi.vincit.jmobster.processor.defaults.validator.jsr303.NumberRangeValidator;
import fi.vincit.jmobster.processor.defaults.validator.jsr303.PatternValidator;
import fi.vincit.jmobster.processor.defaults.validator.jsr303.SizeValidator;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import fi.vincit.jmobster.util.writer.StringBufferWriter;
import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.Pattern;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BackboneValidatorWriterTest {

    private JavaScriptWriter javaScriptWriter;
    private StringBufferWriter writer;
    private BackboneValidatorWriterManager writerManager;

    @Before
    public void initTestMethod() {
        writer = new StringBufferWriter();
        javaScriptWriter = new JavaScriptWriter(writer);
        writerManager = new BackboneValidatorWriterManager(javaScriptWriter);
    }

    /**
     * Size validator
     */

    private final static int NO_MIN_SIZE = -1;
    private final static int NO_MAX_SIZE = Integer.MAX_VALUE;

    private SizeValidator mockSizeValidator(int min, int max) {
        final SizeValidator validator = mock(SizeValidator.class);

        when(validator.getMin()).thenReturn(min);
        when(validator.getMax()).thenReturn(max);
        when(validator.getType()).thenReturn(SizeValidator.class);
        when(validator.hasMin()).thenReturn(min != NO_MIN_SIZE);
        when(validator.hasMax()).thenReturn(max != NO_MAX_SIZE);
        return validator;
    }

    @Test
    public void testWriteMinAndMaxSize() {
        final SizeValidator validator = mockSizeValidator(1, 255);

        writerManager.write( validator, ItemStatuses.notFirstNorLast() );
        writer.close();

        final String result = writer.toString();
        final String expected = "rangeLength: [1, 255],\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteNoMinOrMaxSize() {
        final SizeValidator validator = mockSizeValidator(NO_MIN_SIZE, NO_MAX_SIZE);

        writerManager.write( validator, ItemStatuses.notFirstNorLast() );
        writer.close();

        final String result = writer.toString();
        final String expected = "";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteNoMinOrMaxSizeAsLast() {
        final SizeValidator validator = mockSizeValidator(NO_MIN_SIZE, NO_MAX_SIZE);

        writerManager.write( validator, ItemStatuses.last() );
        writer.close();

        final String result = writer.toString();
        final String expected = "";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMinAndMaxSizeAsLast() {
        final SizeValidator validator = mockSizeValidator( 1, 255 );

        writerManager.write( validator, ItemStatuses.last() );
        writer.close();

        final String result = writer.toString();
        final String expected = "rangeLength: [1, 255]\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMinSize() {
        final SizeValidator validator = mockSizeValidator( 1, NO_MAX_SIZE );

        writerManager.write( validator, ItemStatuses.notFirstNorLast() );
        writer.close();

        final String result = writer.toString();
        final String expected = "minLength: 1,\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMinSizeAsLast() {
        final SizeValidator validator = mockSizeValidator( 1, NO_MAX_SIZE );

        writerManager.write( validator, ItemStatuses.last() );
        writer.close();

        final String result = writer.toString();
        final String expected = "minLength: 1\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMaxSize() {
        final SizeValidator validator = mockSizeValidator( NO_MIN_SIZE, 255 );

        writerManager.write( validator, ItemStatuses.notFirstNorLast() );
        writer.close();

        final String result = writer.toString();
        final String expected = "maxLength: 255,\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMaxSizeAsLast() {
        final SizeValidator validator = mockSizeValidator( NO_MIN_SIZE, 255 );

        writerManager.write( validator, ItemStatuses.last() );
        writer.close();

        final String result = writer.toString();
        final String expected = "maxLength: 255\n";
        assertEquals(expected, result);
    }


    /**
     * Number Range Validator
     */

    private final static long NO_MIN_VALUE = Long.MIN_VALUE;
    private final static long NO_MAX_VALUE = Long.MAX_VALUE;

    private NumberRangeValidator mockNumberRangeValidator(long min, long max) {
        final NumberRangeValidator validator = mock(NumberRangeValidator.class);

        when(validator.getMin()).thenReturn(min);
        when(validator.getMax()).thenReturn(max);
        when(validator.getType()).thenReturn(NumberRangeValidator.class);
        when(validator.hasMin()).thenReturn(min != NO_MIN_VALUE);
        when(validator.hasMax()).thenReturn(max != NO_MAX_VALUE);
        return validator;
    }

    @Test
    public void testWriteMinAndMax() {
        final NumberRangeValidator validator = mockNumberRangeValidator(1, 255);

        writerManager.write( validator, ItemStatuses.notFirstNorLast() );
        writer.close();

        final String result = writer.toString();
        final String expected = "range: [1, 255],\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMinOrMax() {
        final NumberRangeValidator validator = mockNumberRangeValidator(NO_MIN_VALUE, NO_MAX_VALUE);

        writerManager.write( validator, ItemStatuses.notFirstNorLast() );
        writer.close();

        final String result = writer.toString();
        final String expected = "";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMinOrMaxAsLast() {
        final NumberRangeValidator validator = mockNumberRangeValidator(NO_MIN_VALUE, NO_MAX_VALUE);

        writerManager.write( validator, ItemStatuses.last() );
        writer.close();

        final String result = writer.toString();
        final String expected = "";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMinAndMaxAsLast() {
        final NumberRangeValidator validator = mockNumberRangeValidator(1, 255);

        writerManager.write( validator, ItemStatuses.last() );
        writer.close();

        final String result = writer.toString();
        final String expected = "range: [1, 255]\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMin() {
        final NumberRangeValidator validator = mockNumberRangeValidator(1, NO_MAX_VALUE);

        writerManager.write( validator, ItemStatuses.notFirstNorLast() );
        writer.close();

        final String result = writer.toString();
        final String expected = "min: 1,\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMinAsLast() {
        final NumberRangeValidator validator = mockNumberRangeValidator(1, NO_MAX_VALUE);

        writerManager.write( validator, ItemStatuses.last() );
        writer.close();

        final String result = writer.toString();
        final String expected = "min: 1\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMax() {
        final NumberRangeValidator validator = mockNumberRangeValidator(NO_MIN_VALUE, 255);

        writerManager.write( validator, ItemStatuses.notFirstNorLast() );
        writer.close();

        final String result = writer.toString();
        final String expected = "max: 255,\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMaxAsLast() {
        final NumberRangeValidator validator = mockNumberRangeValidator(NO_MIN_VALUE, 255);

        writerManager.write( validator, ItemStatuses.last() );
        writer.close();

        final String result = writer.toString();
        final String expected = "max: 255\n";
        assertEquals(expected, result);
    }

    /**
     * Pattern validator
     */

    private PatternValidator mockPatternValidator( String pattern ) {
        final PatternValidator validator = mock(PatternValidator.class);

        when(validator.getRegexp()).thenReturn(pattern);
        when(validator.getFlags()).thenReturn(new Pattern.Flag[0]);
        when(validator.getType()).thenReturn(PatternValidator.class);

        return validator;
    }

    @Test
    public void testWritePattern() {
        final PatternValidator validator = mockPatternValidator( "[ABCdef]*" );

        writerManager.write( validator, ItemStatuses.notFirstNorLast() );
        writer.close();

        final String result = writer.toString();
        final String expected = "pattern: /[ABCdef]*/,\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWritePatternLast() {
        final PatternValidator validator = mockPatternValidator( "[ABCdef]*" );

        writerManager.write( validator, ItemStatuses.last() );
        writer.close();

        final String result = writer.toString();
        final String expected = "pattern: /[ABCdef]*/\n";
        assertEquals(expected, result);
    }


    /**
     * Not Null
     */

    @Test
    public void testNotNull() {
        final NotNullValidator validator = new NotNullValidator();

        writerManager.write(validator, ItemStatuses.notFirstNorLast());
        writer.close();

        final String result = writer.toString();
        final String expected = "required: true,\n";
        assertEquals(expected, result);
    }

    @Test
    public void testNotNullAsLast() {
        final NotNullValidator validator = new NotNullValidator();

        writerManager.write(validator, ItemStatuses.last());
        writer.close();

        final String result = writer.toString();
        final String expected = "required: true\n";
        assertEquals(expected, result);
    }
}
