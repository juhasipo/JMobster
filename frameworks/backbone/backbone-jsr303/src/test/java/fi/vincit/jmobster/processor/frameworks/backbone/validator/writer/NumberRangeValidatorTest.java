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
import fi.vincit.jmobster.util.Optional;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import org.junit.Test;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NumberRangeValidatorTest extends BaseValidatorTest {
    @Test
    public void test_JavaAScript_MinAndMax() throws Exception {
        NumberRangeValidator validator = new NumberRangeValidator();
        Min min = mockMin(10);
        Max max = mockMax(200);

        JavaScriptContext context = mockWriter(OutputMode.JAVASCRIPT);
        validator.setItemStatus( ItemStatuses.last() );
        validator.setContext(context);
        validator.write(new Optional<Min>(min), new Optional<Max>(max));

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("range: [10, 200]\n"));
    }

    @Test
    public void test_JavaAScript_Min() throws Exception {
        NumberRangeValidator validator = new NumberRangeValidator();
        Min min = mockMin( 10 );

        JavaScriptContext context = mockWriter(OutputMode.JAVASCRIPT);
        validator.setItemStatus( ItemStatuses.last() );
        validator.setContext(context);
        validator.write(new Optional<Min>(min), Optional.empty());

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("min: 10\n"));
    }

    @Test
    public void test_JavaAScript_Max() throws Exception {
        NumberRangeValidator validator = new NumberRangeValidator();
        Max max = mockMax( 1000 );

        JavaScriptContext context = mockWriter(OutputMode.JAVASCRIPT);
        validator.setItemStatus( ItemStatuses.last() );
        validator.setContext(context);
        validator.write(Optional.empty(), new Optional<Max>(max));

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("max: 1000\n"));
    }

    @Test
    public void test_JavaAScript_None() throws Exception {
        NumberRangeValidator validator = new NumberRangeValidator();

        JavaScriptContext context = mockWriter(OutputMode.JAVASCRIPT);
        validator.setItemStatus( ItemStatuses.last() );
        validator.setContext(context);
        validator.write(Optional.empty(), Optional.empty());

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is(""));
    }


    @Test
    public void test_JSON_MinAndMax() throws Exception {
        NumberRangeValidator validator = new NumberRangeValidator();
        Min min = mockMin(10);
        Max max = mockMax(200);

        JavaScriptContext context = mockWriter(OutputMode.JSON);
        validator.setItemStatus( ItemStatuses.last() );
        validator.setContext(context);
        validator.write(new Optional<Min>(min), new Optional<Max>(max));

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("\"range\": [10, 200]\n"));
    }

    @Test
    public void test_JSON_Min() throws Exception {
        NumberRangeValidator validator = new NumberRangeValidator();
        Min min = mockMin(10);

        JavaScriptContext context = mockWriter(OutputMode.JSON);
        validator.setItemStatus( ItemStatuses.last() );
        validator.setContext(context);
        validator.write(new Optional<Min>(min), Optional.empty());

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("\"min\": 10\n"));
    }

    @Test
    public void test_JSON_Max() throws Exception {
        NumberRangeValidator validator = new NumberRangeValidator();
        Max max = mockMax(1000);

        JavaScriptContext context = mockWriter(OutputMode.JSON);
        validator.setItemStatus( ItemStatuses.last() );
        validator.setContext(context);
        validator.write(Optional.empty(), new Optional<Max>(max));

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("\"max\": 1000\n"));
    }

    @Test
    public void test_JSON_None() throws Exception {
        NumberRangeValidator validator = new NumberRangeValidator();

        JavaScriptContext context = mockWriter(OutputMode.JSON);
        validator.setItemStatus( ItemStatuses.last() );
        validator.setContext(context);
        validator.write(Optional.empty(), Optional.empty());

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is(""));
    }

    private Min mockMin(long value) {
        Min min = mock(Min.class);
        when(min.value()).thenReturn(value);
        return min;
    }

    private Max mockMax(long value) {
        Max max = mock(Max.class);
        when(max.value()).thenReturn(value);
        return max;
    }
}
