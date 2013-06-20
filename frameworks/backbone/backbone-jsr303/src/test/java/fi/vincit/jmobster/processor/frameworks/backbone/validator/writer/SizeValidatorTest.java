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
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith( Parameterized.class )
public class SizeValidatorTest extends BaseValidatorTest {

    private String output;
    private Integer min;
    private Integer max;

    private static final String EMPTY = "";

    public SizeValidatorTest( OutputMode mode, Integer min, Integer max, String output ) {
        super(mode);
        this.output = output;
        this.min = min;
        this.max = max;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                { OutputMode.JAVASCRIPT, 10, 200, "rangeLength: [10, 200]" },
                { OutputMode.JAVASCRIPT, 11, null, "minLength: 11" },
                { OutputMode.JAVASCRIPT, null, 100, "maxLength: 100" },
                { OutputMode.JAVASCRIPT, null, null, EMPTY },
                { OutputMode.JSON, 10, 200, "\"rangeLength\": [10, 200]" },
                { OutputMode.JSON, 11, null, "\"minLength\": 11" },
                { OutputMode.JSON, null, 100, "\"maxLength\": 100" },
                { OutputMode.JSON, null, null, EMPTY },
        };
        return Arrays.asList( data );
    }

    @Test
    public void testWrite_FirstNotLast() throws Exception {
        SizeValidator validator = new SizeValidator();
        JavaScriptContext context = createAndInjectContext( validator, ItemStatuses.first() );

        executeWrite( validator );

        context.getWriter().close();
        assertOutput( context, ItemStatuses.first() );
    }

    @Test
    public void testWrite_Middle() throws Exception {
        SizeValidator validator = new SizeValidator();
        JavaScriptContext context = createAndInjectContext( validator, ItemStatuses.notFirstNorLast() );

        executeWrite( validator );

        context.getWriter().close();
        assertOutput( context, ItemStatuses.notFirstNorLast() );
    }

    @Test
    public void testWrite_FirstAndLast() throws Exception {
        SizeValidator validator = new SizeValidator();
        JavaScriptContext context = createAndInjectContext( validator, ItemStatuses.last() );

        executeWrite( validator );

        context.getWriter().close();
        assertOutput( context, ItemStatuses.last() );
    }

    @Test
    public void testWrite_Last() throws Exception {
        SizeValidator validator = new SizeValidator();
        JavaScriptContext context = createAndInjectContext( validator, ItemStatuses.last() );

        executeWrite( validator );

        context.getWriter().close();
        assertOutput( context, ItemStatuses.last() );
    }

    private void executeWrite( SizeValidator validator ) {
        Size size = mockSize( this.min, this.max );
        validator.write(size);
    }

    private void assertOutput( JavaScriptContext context, ItemStatus status ) {
        String prefix;
        if( output.equals( EMPTY ) ) {
            prefix = EMPTY;
        } else if( status.isLastItem() ) {
            prefix = "\n";
        } else {
            prefix = ",\n";
        }
        assertThat(context.getWriter().toString(), is(output + prefix));
    }

    private Size mockSize(Integer min, Integer max) {
        Size size = mock(Size.class);
        if( min != null ) {
            when(size.min()).thenReturn(min);
        } else {
            when(size.min()).thenReturn(0);
        }

        if( max != null ) {
            when(size.max()).thenReturn(max);
        } else {
            when(size.max()).thenReturn(Integer.MAX_VALUE);
        }
        return size;
    }

}
