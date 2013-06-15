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

import fi.vincit.jmobster.processor.languages.javascript.JavaScriptContext;
import fi.vincit.jmobster.processor.languages.javascript.writer.OutputMode;
import org.hibernate.validator.constraints.Length;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LengthValidatorTest extends BaseValidatorTest {
    @Test
    public void testWrite_JavaScript_Min() throws Exception {
        LengthValidator validator = new LengthValidator();
        Length length = mockLength(1, null);

        JavaScriptContext context = mockWriter(OutputMode.JAVASCRIPT);
        validator.setContext(context);
        validator.write(length);

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("minLength: 1\n"));
    }

    @Test
    public void testWrite_JavaScript_Max() throws Exception {
        LengthValidator validator = new LengthValidator();
        Length length = mockLength(null, 100);

        JavaScriptContext context = mockWriter(OutputMode.JAVASCRIPT);
        validator.setContext(context);
        validator.write(length);

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("maxLength: 100\n"));
    }

    @Test
    public void testWrite_JavaScript_MinAndMax() throws Exception {
        LengthValidator validator = new LengthValidator();
        Length length = mockLength(2, 255);

        JavaScriptContext context = mockWriter(OutputMode.JAVASCRIPT);
        validator.setContext(context);
        validator.write(length);

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("rangeLength: [2, 255]\n"));
    }

    @Test
    public void testWrite_JavaScript_NoMinOrdMax() throws Exception {
        LengthValidator validator = new LengthValidator();
        Length length = mockLength(null, null);

        JavaScriptContext context = mockWriter(OutputMode.JAVASCRIPT);
        validator.setContext(context);
        validator.write(length);

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is(""));
    }



    @Test
    public void testWrite_JSON_Min() throws Exception {
        LengthValidator validator = new LengthValidator();
        Length length = mockLength(1, null);

        JavaScriptContext context = mockWriter(OutputMode.JSON);
        validator.setContext(context);
        validator.write(length);

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("\"minLength\": 1\n"));
    }

    @Test
    public void testWrite_JSON_Max() throws Exception {
        LengthValidator validator = new LengthValidator();
        Length length = mockLength(null, 100);

        JavaScriptContext context = mockWriter(OutputMode.JSON);
        validator.setContext(context);
        validator.write(length);

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("\"maxLength\": 100\n"));
    }

    @Test
    public void testWrite_JSON_MinAndMax() throws Exception {
        LengthValidator validator = new LengthValidator();
        Length length = mockLength(2, 255);

        JavaScriptContext context = mockWriter(OutputMode.JSON);
        validator.setContext(context);
        validator.write(length);

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("\"rangeLength\": [2, 255]\n"));
    }

    @Test
    public void testWrite_JSON_NoMinOrdMax() throws Exception {
        LengthValidator validator = new LengthValidator();
        Length length = mockLength(null, null);

        JavaScriptContext context = mockWriter(OutputMode.JSON);
        validator.setContext(context);
        validator.write(length);

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is(""));
    }

    private Length mockLength(Integer min, Integer max) {
        Length length = mock(Length.class);
        if( min != null ) {
            when(length.min()).thenReturn(min);
        } else {
            when(length.min()).thenReturn(0);
        }

        if( max != null ) {
            when(length.max()).thenReturn(max);
        } else {
            when(length.max()).thenReturn(Integer.MAX_VALUE);
        }
        return length;
    }
}
