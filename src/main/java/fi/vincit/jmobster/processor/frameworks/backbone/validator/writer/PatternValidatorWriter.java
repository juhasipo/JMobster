package fi.vincit.jmobster.processor.frameworks.backbone.validator.writer;

/*
 * Copyright 2012 Juha Siponen
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

import fi.vincit.jmobster.processor.defaults.validator.PatternValidator;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptValidatorWriter;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptWriter;
import fi.vincit.jmobster.processor.languages.javascript.JavaToJSPatternConverter;

public class PatternValidatorWriter extends JavaScriptValidatorWriter<PatternValidator> {
    public PatternValidatorWriter() {
        super( PatternValidator.class );
    }

    @Override
    protected void write( JavaScriptWriter writer, PatternValidator validator, boolean isLast ) {
        String pattern = JavaToJSPatternConverter.convertFromJava(validator.getRegexp(), validator.getFlags());
        writer.writeKeyValue("pattern", pattern, isLast);
    }
}
