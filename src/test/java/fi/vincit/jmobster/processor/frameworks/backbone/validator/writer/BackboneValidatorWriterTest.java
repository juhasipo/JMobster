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

import fi.vincit.jmobster.processor.defaults.validator.PatternValidator;
import fi.vincit.jmobster.processor.defaults.validator.SizeValidator;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptWriter;
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

    private SizeValidator mockSizeValidator(int min, int max) {
        final SizeValidator validator = mock(SizeValidator.class);

        when(validator.getMin()).thenReturn(min);
        when(validator.getMax()).thenReturn(max);
        when(validator.getType()).thenReturn(SizeValidator.class);

        return validator;
    }

    @Test
    public void testWriteMinAndMaxSize() {
        final SizeValidator validator = mockSizeValidator(1, 255);

        writerManager.write( validator, false );
        writer.close();

        final String result = writer.toString();
        final String expected = "rangeLength: [1, 255],\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMinAndMaxSizeAsLast() {
        final SizeValidator validator = mockSizeValidator( 1, 255 );

        writerManager.write( validator, true );
        writer.close();

        final String result = writer.toString();
        final String expected = "rangeLength: [1, 255]\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMinSize() {
        final SizeValidator validator = mockSizeValidator( 1, Integer.MAX_VALUE );

        writerManager.write( validator, false );
        writer.close();

        final String result = writer.toString();
        final String expected = "minLength: 1,\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMinSizeAsLast() {
        final SizeValidator validator = mockSizeValidator( 1, Integer.MAX_VALUE );

        writerManager.write( validator, true );
        writer.close();

        final String result = writer.toString();
        final String expected = "minLength: 1\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMaxSize() {
        final SizeValidator validator = mockSizeValidator( -1, 255 );

        writerManager.write( validator, false );
        writer.close();

        final String result = writer.toString();
        final String expected = "maxLength: 255,\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMaxSizeAsLast() {
        final SizeValidator validator = mockSizeValidator( -1, 255 );

        writerManager.write( validator, true );
        writer.close();

        final String result = writer.toString();
        final String expected = "maxLength: 255\n";
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

        writerManager.write( validator, false );
        writer.close();

        final String result = writer.toString();
        final String expected = "pattern: /[ABCdef]*/,\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWritePatternLast() {
        final PatternValidator validator = mockPatternValidator( "[ABCdef]*" );

        writerManager.write( validator, true );
        writer.close();

        final String result = writer.toString();
        final String expected = "pattern: /[ABCdef]*/\n";
        assertEquals(expected, result);
    }

}
