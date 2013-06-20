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
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith( Parameterized.class )
public class NotNullValidatorTest extends BaseValidatorTest {

    private String output;

    public NotNullValidatorTest( OutputMode mode, String output ) {
        super(mode);
        this.output = output;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                { OutputMode.JAVASCRIPT, "required: true" },
                { OutputMode.JSON, "\"required\": true" }
        };
        return Arrays.asList( data );
    }

    @Test
    public void testWrite_FirstNotLast() throws Exception {
        NotNullValidator validator = new NotNullValidator();
        JavaScriptContext context = createAndInjectContext( validator, ItemStatuses.first() );

        validator.write(mock(NotNull.class));

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is(output + ",\n"));
    }

    @Test
    public void testWrite_Middle() throws Exception {
        NotNullValidator validator = new NotNullValidator();
        JavaScriptContext context = createAndInjectContext( validator, ItemStatuses.notFirstNorLast() );

        validator.write(mock(NotNull.class));

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is(output + ",\n"));
    }

    @Test
    public void testWrite_FirstAndLast() throws Exception {
        NotNullValidator validator = new NotNullValidator();
        JavaScriptContext context = createAndInjectContext( validator, ItemStatuses.last() );

        validator.write(mock(NotNull.class));

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is(output + "\n"));
    }

    @Test
    public void testWrite_Last() throws Exception {
        NotNullValidator validator = new NotNullValidator();
        JavaScriptContext context = createAndInjectContext( validator, ItemStatuses.last() );

        validator.write(mock(NotNull.class));

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is(output + "\n"));
    }

}
