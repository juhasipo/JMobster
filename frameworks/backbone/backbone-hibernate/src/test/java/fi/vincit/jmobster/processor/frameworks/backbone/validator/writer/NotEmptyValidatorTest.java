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
import org.hibernate.validator.constraints.NotEmpty;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class NotEmptyValidatorTest extends BaseValidatorTest {
    @Test
    public void testWrite_JavaScript() throws Exception {
        NotEmptyValidator validator = new NotEmptyValidator();

        JavaScriptContext context = mockWriter(OutputMode.JAVASCRIPT);
        validator.setItemStatus( ItemStatuses.last() );
        validator.setContext(context);

        validator.write(mock(NotEmpty.class));

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("required: true\n"));
    }

    @Test
    public void testWrite_JSON() throws Exception {
        NotEmptyValidator validator = new NotEmptyValidator();

        JavaScriptContext context = mockWriter(OutputMode.JSON);
        validator.setItemStatus( ItemStatuses.last() );
        validator.setContext(context);

        validator.write(mock(NotEmpty.class));

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("\"required\": true\n"));
    }
}
