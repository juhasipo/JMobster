package fi.vincit.jmobster.processor.frameworks.backbone.validator.writer;

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

import fi.vincit.jmobster.processor.defaults.hibernate.EmailValidator;
import fi.vincit.jmobster.processor.defaults.hibernate.LengthValidator;
import fi.vincit.jmobster.processor.defaults.hibernate.NotEmptyValidator;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptContext;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.processor.languages.javascript.writer.OutputMode;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import fi.vincit.jmobster.util.writer.StringBufferWriter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HibernateValidatorWriterJSONTest {

    private JavaScriptWriter javaScriptWriter;
    private StringBufferWriter writer;
    private HibernateValidatorWriters writerManager;

    private JavaScriptContext getTestContext() {
        return new JavaScriptContext(javaScriptWriter, OutputMode.JSON);
    }

    @Before
    public void initTestMethod() {
        writer = new StringBufferWriter();
        javaScriptWriter = new JavaScriptWriter(writer);
        javaScriptWriter.setJSONmode(true);
        writerManager = new HibernateValidatorWriters();
        writerManager.setLanguageContext(getTestContext());
    }

    /**
     * Size validator
     */

    private final static int NO_MIN_SIZE = -1;
    private final static int NO_MAX_SIZE = Integer.MAX_VALUE;

    private LengthValidator mockLengthValidator(int min, int max) {
        final LengthValidator validator = mock(LengthValidator.class);

        when(validator.getMin()).thenReturn(min);
        when(validator.getMax()).thenReturn(max);
        when(validator.getType()).thenReturn(LengthValidator.class);
        when(validator.hasMin()).thenReturn(min != NO_MIN_SIZE);
        when(validator.hasMax()).thenReturn(max != NO_MAX_SIZE);
        return validator;
    }

    @Test
    public void testWriteMinAndMaxSize() {
        final LengthValidator validator = mockLengthValidator(1, 255);

        writerManager.write( validator, ItemStatuses.notFirstNorLast() );
        writer.close();

        final String result = writer.toString();
        final String expected = "\"rangeLength\": [1, 255],\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteNoMinOrMaxSize() {
        final LengthValidator validator = mockLengthValidator(NO_MIN_SIZE, NO_MAX_SIZE);

        writerManager.write( validator, ItemStatuses.notFirstNorLast() );
        writer.close();

        final String result = writer.toString();
        final String expected = "";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteNoMinOrMaxSizeAsLast() {
        final LengthValidator validator = mockLengthValidator(NO_MIN_SIZE, NO_MAX_SIZE);

        writerManager.write( validator, ItemStatuses.last() );
        writer.close();

        final String result = writer.toString();
        final String expected = "";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMinAndMaxSizeAsLast() {
        final LengthValidator validator = mockLengthValidator( 1, 255 );

        writerManager.write( validator, ItemStatuses.last() );
        writer.close();

        final String result = writer.toString();
        final String expected = "\"rangeLength\": [1, 255]\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMinSize() {
        final LengthValidator validator = mockLengthValidator( 1, NO_MAX_SIZE );

        writerManager.write( validator, ItemStatuses.notFirstNorLast() );
        writer.close();

        final String result = writer.toString();
        final String expected = "\"minLength\": 1,\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMinSizeAsLast() {
        final LengthValidator validator = mockLengthValidator( 1, NO_MAX_SIZE );

        writerManager.write( validator, ItemStatuses.last() );
        writer.close();

        final String result = writer.toString();
        final String expected = "\"minLength\": 1\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMaxSize() {
        final LengthValidator validator = mockLengthValidator( NO_MIN_SIZE, 255 );

        writerManager.write( validator, ItemStatuses.notFirstNorLast() );
        writer.close();

        final String result = writer.toString();
        final String expected = "\"maxLength\": 255,\n";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteMaxSizeAsLast() {
        final LengthValidator validator = mockLengthValidator( NO_MIN_SIZE, 255 );

        writerManager.write( validator, ItemStatuses.last() );
        writer.close();

        final String result = writer.toString();
        final String expected = "\"maxLength\": 255\n";
        assertEquals(expected, result);
    }

    /**
     * Email validator
     */

    @Test
    public void testEmailValidatorAsLast() {
        EmailValidator validator = mock(EmailValidator.class);
        when(validator.getType()).thenReturn(EmailValidator.class);

        writerManager.write( validator, ItemStatuses.last() );
        writer.close();

        final String result = writer.toString();
        final String expected = "\"pattern__regexp\": [\"^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(\\\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@([a-z0-9!#$%&'*+/=?^_`{|}~-]+(\\\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\\\\[[0-9]{1,3}\\\\.[0-9]{1,3}\\\\.[0-9]{1,3}\\\\.[0-9]{1,3}\\\\])$\", \"i\"]\n";
        assertEquals(expected, result);
    }

    /**
     * NotEmpty validator
     */

    @Test
    public void testNotEmptyValidatorAsLast() {
        NotEmptyValidator validator = mock(NotEmptyValidator.class);
        when(validator.getType()).thenReturn(NotEmptyValidator.class);

        writerManager.write( validator, ItemStatuses.last() );
        writer.close();

        final String result = writer.toString();
        final String expected = "\"required\": true\n";
        assertEquals(expected, result);
    }
}
