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
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.util.Optional;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class NumberRangeValidator extends BaseValidatorWriter<JavaScriptContext, JavaScriptWriter> {

    private static final String MIN_AND_MAX_KEY = "range";
    private static final String MIN_ONLY_KEY = "min";
    private static final String MAX_ONLY_KEY = "max";

    public void write(Optional<Min> min, Optional<Max> max) {
        if( min.isPresent() && max.isPresent() ) {
            getWriter().writeKey( MIN_AND_MAX_KEY );
            getWriter().writeArray( min.getValue().value(), max.getValue().value() );
        } else if( min.isPresent() ) {
            String value = String.valueOf(min.getValue().value());
            getWriter().writeKeyValue( MIN_ONLY_KEY, value );
        } else if( max.isPresent() ) {
            String value = String.valueOf(max.getValue().value());
            getWriter().writeKeyValue( MAX_ONLY_KEY, value );
        }
    }
}
