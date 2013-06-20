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

import fi.vincit.jmobster.processor.defaults.validator.BaseValidatorWriter;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptContext;
import fi.vincit.jmobster.processor.languages.javascript.JavaToJSPatternConverter;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.processor.languages.javascript.writer.OutputMode;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Pattern;

public class EmailValidator extends BaseValidatorWriter<JavaScriptContext, JavaScriptWriter> {

    /*
     * From Hibernate Validator 4.2.0 Final source code
     */
    private static String ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~-]";
    private static String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)*";
    private static String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";

    static String EMAIL_REG_EXP = "^" + ATOM + "+(\\." + ATOM + "+)*@"
                        + DOMAIN + "|" + IP_DOMAIN + ")$";

    private static final String BACKSLASH_ESCAPE_PATTERN = "\\\\";
    private static final String BACKSLASH_REPLACEMENT = "\\\\\\\\";

    private static final String QUOTE_ESCAPE_PATTER = "\"";
    private static final String QUOTE_REPLACEMENT = "\\\\\"";

    private static final Character QUOTE_CHAR = '\"';
    private static final String PATTERN_KEY_JSON = "pattern__regexp";
    private static final String PATTERN_KEY_JAVASCRIPT = "pattern";

    final static Pattern.Flag[] flags = {Pattern.Flag.CASE_INSENSITIVE};

    public void write(Email email) {
        /*
            JSON can't output JS regexp. Change the reg exp to string
         */
        if( getContext().getOutputMode() == OutputMode.JSON ) {
            String patternString = convertPatternToJSON(EMAIL_REG_EXP);
            String flagsString = convertFlagsToJSON(flags);

            getWriter().writeKey(PATTERN_KEY_JSON);
            getWriter().writeArray(getItemStatus(), patternString, flagsString);
        } else {
            String patternString = JavaToJSPatternConverter.convertFromJava(EMAIL_REG_EXP, flags);
            getWriter().writeKeyValue(PATTERN_KEY_JAVASCRIPT, patternString, getItemStatus());
        }
    }

    private String convertFlagsToJSON(Pattern.Flag[] flags) {
        String flagsString = JavaToJSPatternConverter.getModifiersFromFlags(flags);
        return QUOTE_CHAR + flagsString + QUOTE_CHAR;
    }

    private String convertPatternToJSON(String regexp) {
        String patternString = JavaToJSPatternConverter.convertFromJavaToJSON(regexp);
        patternString = patternString
                .replaceAll(BACKSLASH_ESCAPE_PATTERN, BACKSLASH_REPLACEMENT)
                .replaceAll(QUOTE_ESCAPE_PATTER, QUOTE_REPLACEMENT);
        return QUOTE_CHAR + patternString + QUOTE_CHAR;
    }
}
