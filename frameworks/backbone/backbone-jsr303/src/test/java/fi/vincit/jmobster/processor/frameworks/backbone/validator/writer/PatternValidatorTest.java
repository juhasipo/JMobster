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
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.validation.constraints.Pattern;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith( Parameterized.class )
public class PatternValidatorTest extends BaseValidatorTest {

    private String output;
    private String regexp;
    private String overriddenRegexp;
    private Pattern.Flag[] flags;

    private static final Pattern.Flag[] EMPTY_FLAGS = new Pattern.Flag[0];
    private static final Pattern.Flag[] I_FLAG = new Pattern.Flag[] {Pattern.Flag.CASE_INSENSITIVE};
    private static final Pattern.Flag[] TWO_FLAGS = new Pattern.Flag[] {Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.MULTILINE};
    private static final String NO_OVERRIDDEN = "";

    public PatternValidatorTest( OutputMode mode,
                                 String regexp,
                                 String overriddenRegexp,
                                 Pattern.Flag[] flags,
                                 String output) {
        super(mode);
        this.output = output;
        this.regexp = regexp;
        this.overriddenRegexp = overriddenRegexp;
        this.flags = flags;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                { OutputMode.JAVASCRIPT, "[abc]", NO_OVERRIDDEN, EMPTY_FLAGS, "pattern: /[abc]/" },
                { OutputMode.JAVASCRIPT, "[abc]", "[qwerty]", EMPTY_FLAGS, "pattern: /[qwerty]/" },
                { OutputMode.JAVASCRIPT, "[abc]", NO_OVERRIDDEN, I_FLAG, "pattern: /[abc]/i" },
                { OutputMode.JAVASCRIPT, "[abc]", NO_OVERRIDDEN, TWO_FLAGS, "pattern: /[abc]/im" },
                { OutputMode.JAVASCRIPT, "[abc]", "[qwerty]", I_FLAG, "pattern: /[qwerty]/i" },
                { OutputMode.JAVASCRIPT, "[abc]", "[qwerty]", TWO_FLAGS, "pattern: /[qwerty]/im" },

                { OutputMode.JAVASCRIPT, "[\"ABCdef]*", NO_OVERRIDDEN, EMPTY_FLAGS, "pattern: /[\"ABCdef]*/" },
                { OutputMode.JAVASCRIPT, "\"[\"ABCdef\"]*\"\"", NO_OVERRIDDEN, EMPTY_FLAGS, "pattern: /\"[\"ABCdef\"]*\"\"/" },
                { OutputMode.JAVASCRIPT, "[\\.ABCdef]*", NO_OVERRIDDEN, EMPTY_FLAGS, "pattern: /[\\.ABCdef]*/" },
                { OutputMode.JAVASCRIPT, "\\\"[\\.ABCdef]*", NO_OVERRIDDEN, EMPTY_FLAGS, "pattern: /\\\"[\\.ABCdef]*/" },

                { OutputMode.JAVASCRIPT, "[abc]", "[\"ABCdef]*", EMPTY_FLAGS, "pattern: /[\"ABCdef]*/" },
                { OutputMode.JAVASCRIPT, "[abc]", "\"[\"ABCdef\"]*\"\"", EMPTY_FLAGS, "pattern: /\"[\"ABCdef\"]*\"\"/" },
                { OutputMode.JAVASCRIPT, "[abc]", "[\\.ABCdef]*", EMPTY_FLAGS, "pattern: /[\\.ABCdef]*/" },
                { OutputMode.JAVASCRIPT, "[abc]", "\\\"[\\.ABCdef]*", EMPTY_FLAGS, "pattern: /\\\"[\\.ABCdef]*/" },

                { OutputMode.JSON, "[abc]", NO_OVERRIDDEN, EMPTY_FLAGS, "\"pattern__regexp\": [\"[abc]\", \"\"]" },
                { OutputMode.JSON, "[abc]", "[qwerty]", EMPTY_FLAGS, "\"pattern__regexp\": [\"[qwerty]\", \"\"]" },
                { OutputMode.JSON, "[abc]", NO_OVERRIDDEN, I_FLAG, "\"pattern__regexp\": [\"[abc]\", \"i\"]" },
                { OutputMode.JSON, "[abc]", NO_OVERRIDDEN, TWO_FLAGS, "\"pattern__regexp\": [\"[abc]\", \"im\"]" },
                { OutputMode.JSON, "[abc]", "[qwerty]", I_FLAG, "\"pattern__regexp\": [\"[qwerty]\", \"i\"]" },
                { OutputMode.JSON, "[abc]", "[qwerty]", TWO_FLAGS, "\"pattern__regexp\": [\"[qwerty]\", \"im\"]" },

                { OutputMode.JSON, "[\"ABCdef]*", NO_OVERRIDDEN, EMPTY_FLAGS, "\"pattern__regexp\": [\"[\\\"ABCdef]*\", \"\"]" },
                { OutputMode.JSON, "\"[\"ABCdef\"]*\"\"", NO_OVERRIDDEN, EMPTY_FLAGS, "\"pattern__regexp\": [\"\\\"[\\\"ABCdef\\\"]*\\\"\\\"\", \"\"]" },
                { OutputMode.JSON, "[\\.ABCdef]*", NO_OVERRIDDEN, EMPTY_FLAGS, "\"pattern__regexp\": [\"[\\\\.ABCdef]*\", \"\"]" },
                { OutputMode.JSON, "\\\"[\\.ABCdef]*", NO_OVERRIDDEN, EMPTY_FLAGS, "\"pattern__regexp\": [\"\\\\\\\"[\\\\.ABCdef]*\", \"\"]" },

                { OutputMode.JSON, "[abc]", "[\"ABCdef]*", EMPTY_FLAGS, "\"pattern__regexp\": [\"[\\\"ABCdef]*\", \"\"]" },
                { OutputMode.JSON, "[abc]", "\"[\"ABCdef\"]*\"\"", EMPTY_FLAGS, "\"pattern__regexp\": [\"\\\"[\\\"ABCdef\\\"]*\\\"\\\"\", \"\"]" },
                { OutputMode.JSON, "[abc]", "[\\.ABCdef]*", EMPTY_FLAGS, "\"pattern__regexp\": [\"[\\\\.ABCdef]*\", \"\"]" },
                { OutputMode.JSON, "[abc]", "\\\"[\\.ABCdef]*", EMPTY_FLAGS, "\"pattern__regexp\": [\"\\\\\\\"[\\\\.ABCdef]*\", \"\"]" },

                { OutputMode.JSON, "[\"ABCdef]*", NO_OVERRIDDEN, I_FLAG, "\"pattern__regexp\": [\"[\\\"ABCdef]*\", \"i\"]" },
                { OutputMode.JSON, "\"[\"ABCdef\"]*\"\"", NO_OVERRIDDEN, I_FLAG, "\"pattern__regexp\": [\"\\\"[\\\"ABCdef\\\"]*\\\"\\\"\", \"i\"]" },
                { OutputMode.JSON, "[\\.ABCdef]*", NO_OVERRIDDEN, I_FLAG, "\"pattern__regexp\": [\"[\\\\.ABCdef]*\", \"i\"]" },
                { OutputMode.JSON, "\\\"[\\.ABCdef]*", NO_OVERRIDDEN, I_FLAG, "\"pattern__regexp\": [\"\\\\\\\"[\\\\.ABCdef]*\", \"i\"]" },
        };
        return Arrays.asList( data );
    }


    @Test
    public void testWrite_FirstNotLast() throws Exception {
        PatternValidator validator = new PatternValidator();
        JavaScriptContext context = createAndInjectContext( validator, ItemStatuses.first() );

        executeWrite( validator );

        context.getWriter().close();
        assertOutput( context, ItemStatuses.first() );
    }

    @Test
    public void testWrite_Middle() throws Exception {
        PatternValidator validator = new PatternValidator();
        JavaScriptContext context = createAndInjectContext( validator, ItemStatuses.notFirstNorLast() );

        executeWrite( validator );

        context.getWriter().close();
        assertOutput( context, ItemStatuses.notFirstNorLast() );
    }

    @Test
    public void testWrite_FirstAndLast() throws Exception {
        PatternValidator validator = new PatternValidator();
        JavaScriptContext context = createAndInjectContext( validator, ItemStatuses.last() );

        executeWrite( validator );

        context.getWriter().close();
        assertOutput( context, ItemStatuses.last() );
    }

    @Test
    public void testWrite_Last() throws Exception {
        PatternValidator validator = new PatternValidator();
        JavaScriptContext context = createAndInjectContext( validator, ItemStatuses.last() );

        executeWrite( validator );

        context.getWriter().close();
        assertOutput( context, ItemStatuses.last() );
    }

    private void executeWrite( PatternValidator validator ) {
        Pattern pattern = mockPattern( this.regexp, flags );
        if( this.overriddenRegexp.equals( NO_OVERRIDDEN ) ) {
            validator.write(pattern, Optional.empty());
        } else {
            OverridePattern overridePattern = mockOverride(this.overriddenRegexp);
            validator.write(pattern, new Optional<OverridePattern>( overridePattern ));
        }

    }

    private void assertOutput( JavaScriptContext context, ItemStatus status ) {
        String prefix;
        if( status.isLastItem() ) {
            prefix = "\n";
        } else {
            prefix = ",\n";
        }
        assertThat(context.getWriter().toString(), is(output + prefix));
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
