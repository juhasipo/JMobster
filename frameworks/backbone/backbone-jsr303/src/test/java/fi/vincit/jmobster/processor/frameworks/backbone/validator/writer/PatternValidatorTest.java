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

import fi.vincit.jmobster.annotation.OverridePattern;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptContext;
import fi.vincit.jmobster.processor.languages.javascript.writer.OutputMode;
import fi.vincit.jmobster.util.Optional;
import org.junit.Test;

import javax.validation.constraints.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PatternValidatorTest extends BaseValidatorTest {
    @Test
    public void testWrite_JavaScript_NoFlags() throws Exception {
        PatternValidator validator = new PatternValidator();
        Pattern pattern = mockPattern("[abc]");

        JavaScriptContext context = mockWriter(OutputMode.JAVASCRIPT);

        validator.setContext(context);
        validator.write(pattern, Optional.empty());

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("pattern: /[abc]/\n"));
    }

    @Test
    public void testWrite_JavaScript_Override_NoFlags() throws Exception {
        PatternValidator validator = new PatternValidator();
        Pattern pattern = mockPattern("[abc]");
        OverridePattern override = mockOverride("[qwerty]");

        JavaScriptContext context = mockWriter(OutputMode.JAVASCRIPT);

        validator.setContext(context);
        validator.write(pattern, new Optional<OverridePattern>(override));

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("pattern: /[qwerty]/\n"));
    }

    @Test
    public void testWrite_JavaScript_CaseInsensitiveFlag() throws Exception {
        PatternValidator validator = new PatternValidator();
        Pattern pattern = mockPattern("[abc]", Pattern.Flag.CASE_INSENSITIVE);

        JavaScriptContext context = mockWriter(OutputMode.JAVASCRIPT);

        validator.setContext(context);
        validator.write(pattern, Optional.empty());

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("pattern: /[abc]/i\n"));
    }

    @Test
    public void testWrite_JavaScript_Override_CaseInsensitiveFlag() throws Exception {
        PatternValidator validator = new PatternValidator();
        Pattern pattern = mockPattern("[abc]", Pattern.Flag.CASE_INSENSITIVE);
        OverridePattern override = mockOverride("[qwerty]");

        JavaScriptContext context = mockWriter(OutputMode.JAVASCRIPT);

        validator.setContext(context);
        validator.write(pattern, new Optional<OverridePattern>(override));

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("pattern: /[qwerty]/i\n"));
    }

    @Test
    public void testWrite_JSON_NoFlags() throws Exception {
        PatternValidator validator = new PatternValidator();
        Pattern pattern = mockPattern("[abc]");

        JavaScriptContext context = mockWriter(OutputMode.JSON);

        validator.setContext(context);
        validator.write(pattern, Optional.empty());

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("\"pattern__regexp\": [\"[abc]\", \"\"]\n"));
    }

    @Test
    public void testWrite_JSON_Override_NoFlags() throws Exception {
        PatternValidator validator = new PatternValidator();
        Pattern pattern = mockPattern("[abc]");
        OverridePattern override = mockOverride("[qwerty]");

        JavaScriptContext context = mockWriter(OutputMode.JSON);

        validator.setContext(context);
        validator.write(pattern, new Optional<OverridePattern>(override));

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("\"pattern__regexp\": [\"[qwerty]\", \"\"]\n"));
    }

    @Test
    public void testWrite_JSON_Escape_Quote() throws Exception {
        PatternValidator validator = new PatternValidator();
        Pattern pattern = mockPattern("[\"ABCdef]*");

        JavaScriptContext context = mockWriter(OutputMode.JSON);

        validator.setContext(context);
        validator.write(pattern, Optional.empty());

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("\"pattern__regexp\": [\"[\\\"ABCdef]*\", \"\"]\n"));
    }

    @Test
    public void testWrite_JSON_Escape_Quote2() throws Exception {
        PatternValidator validator = new PatternValidator();
        Pattern pattern = mockPattern("\"[\"ABCdef\"]*\"\"");

        JavaScriptContext context = mockWriter(OutputMode.JSON);

        validator.setContext(context);
        validator.write(pattern, Optional.empty());

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("\"pattern__regexp\": [\"\\\"[\\\"ABCdef\\\"]*\\\"\\\"\", \"\"]\n"));
    }

    @Test
    public void testWrite_JSON_Escape_OtherChar() throws Exception {
        PatternValidator validator = new PatternValidator();
        Pattern pattern = mockPattern("[\\.ABCdef]*");

        JavaScriptContext context = mockWriter(OutputMode.JSON);

        validator.setContext(context);
        validator.write(pattern, Optional.empty());

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("\"pattern__regexp\": [\"[\\\\.ABCdef]*\", \"\"]\n"));
    }

    @Test
    public void testWrite_JSON_Escape_OtherChar2() throws Exception {
        PatternValidator validator = new PatternValidator();
        Pattern pattern = mockPattern("\\\"[\\.ABCdef]*");

        JavaScriptContext context = mockWriter(OutputMode.JSON);

        validator.setContext(context);
        validator.write(pattern, Optional.empty());

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("\"pattern__regexp\": [\"\\\\\\\"[\\\\.ABCdef]*\", \"\"]\n"));
    }

    @Test
    public void testWrite_JSON_Override_Escape_OtherChar() throws Exception {
        PatternValidator validator = new PatternValidator();
        Pattern pattern = mockPattern("[abc]");
        OverridePattern override = mockOverride("\\\"[\\.ABCdef]*");

        JavaScriptContext context = mockWriter(OutputMode.JSON);

        validator.setContext(context);
        validator.write(pattern, new Optional<OverridePattern>(override));

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("\"pattern__regexp\": [\"\\\\\\\"[\\\\.ABCdef]*\", \"\"]\n"));
    }

    @Test
    public void testWrite_JSON_CaseInsensitiveFlag() throws Exception {
        PatternValidator validator = new PatternValidator();
        Pattern pattern = mockPattern("[abc]", Pattern.Flag.CASE_INSENSITIVE);

        JavaScriptContext context = mockWriter(OutputMode.JSON);

        validator.setContext(context);
        validator.write(pattern, Optional.empty());

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("\"pattern__regexp\": [\"[abc]\", \"i\"]\n"));
    }

    @Test
    public void testWrite_JSON_Override_CaseInsensitiveFlag() throws Exception {
        PatternValidator validator = new PatternValidator();
        Pattern pattern = mockPattern("[abc]", Pattern.Flag.CASE_INSENSITIVE);
        OverridePattern override = mockOverride("[qwerty]");

        JavaScriptContext context = mockWriter(OutputMode.JSON);

        validator.setContext(context);
        validator.write(pattern, new Optional<OverridePattern>(override));

        context.getWriter().close();
        assertThat(context.getWriter().toString(), is("\"pattern__regexp\": [\"[qwerty]\", \"i\"]\n"));
    }

    private Pattern mockPattern(String regexp, Pattern.Flag... flags) {
        Pattern pattern = mock(Pattern.class);
        when(pattern.regexp()).thenReturn(regexp);
        when(pattern.flags()).thenReturn(flags);
        return pattern;
    }

    private OverridePattern mockOverride(String regexp) {
        OverridePattern pattern = mock(OverridePattern.class);
        when(pattern.regexp()).thenReturn(regexp);
        return pattern;
    }

}
