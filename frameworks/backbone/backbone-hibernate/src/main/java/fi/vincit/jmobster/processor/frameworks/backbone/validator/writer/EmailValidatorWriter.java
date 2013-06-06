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
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptContext;
import fi.vincit.jmobster.processor.languages.javascript.JavaToJSPatternConverter;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptValidatorWriter;
import fi.vincit.jmobster.processor.languages.javascript.writer.OutputMode;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;

import javax.validation.constraints.Pattern;

public class EmailValidatorWriter extends JavaScriptValidatorWriter<EmailValidator> {
    /*
     * From Hibernate Validator 4.2.0 Final source code
     */
    private static String ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~-]";
    private static String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)*";
    private static String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";

    static String EMAIL_REG_EXP = "^" + ATOM + "+(\\." + ATOM + "+)*@"
            + DOMAIN + "|" + IP_DOMAIN + ")$";

    // ^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@([a-z0-9!#$%&'*+/=?^_`{|}~-]+(\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])$
    @Override
    protected void write(JavaScriptContext context, EmailValidator validator, ItemStatus status) {
        if( context.getOutputMode() == OutputMode.JSON ) {
            String pattern = JavaToJSPatternConverter.convertFromJavaToJSON(EMAIL_REG_EXP);
            pattern = pattern.replaceAll("\\\\", "\\\\\\\\");
            pattern = pattern.replaceAll("\"", "\\\\\"");
            pattern = "\"" + pattern + "\"";
            String flags = JavaToJSPatternConverter.getModifiersFromFlags(Pattern.Flag.CASE_INSENSITIVE);
            flags = "\"" + flags + "\"";
            context.getWriter().writeKey("pattern__regexp");
            context.getWriter().writeArray(status, pattern, flags);
        } else {
            String pattern = JavaToJSPatternConverter.convertFromJava(EMAIL_REG_EXP, Pattern.Flag.CASE_INSENSITIVE);
            context.getWriter().writeKeyValue("pattern", pattern, status);
        }
    }
}
