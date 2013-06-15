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
import org.hibernate.validator.constraints.Email;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class EmailValidatorTest extends BaseValidatorTest {

    @Test
    public void test_JavaScript() {
        EmailValidator validator = new EmailValidator();
        Email email = mock(Email.class);

        JavaScriptContext context = mockWriter(OutputMode.JAVASCRIPT);
        validator.setContext(context);
        validator.write(email);

        context.getWriter().close();
        final String expected = "pattern: /"+ EmailValidator.EMAIL_REG_EXP + "/i\n";

        assertThat(context.getWriter().toString(), is(expected));
    }

    @Test
    public void test_JSON() {
        EmailValidator validator = new EmailValidator();
        Email email = mock(Email.class);

        JavaScriptContext context = mockWriter(OutputMode.JSON);
        validator.setContext(context);
        validator.write(email);

        context.getWriter().close();
        final String expected = "\"pattern__regexp\": [\"^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(\\\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@([a-z0-9!#$%&'*+/=?^_`{|}~-]+(\\\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\\\\[[0-9]{1,3}\\\\.[0-9]{1,3}\\\\.[0-9]{1,3}\\\\.[0-9]{1,3}\\\\])$\", \"i\"]\n";

        assertThat(context.getWriter().toString(), is(expected));
    }

}
