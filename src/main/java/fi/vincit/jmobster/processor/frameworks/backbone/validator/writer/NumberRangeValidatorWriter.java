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

import fi.vincit.jmobster.processor.defaults.validator.NumberRangeValidator;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptValidatorWriter;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptWriter;

public class NumberRangeValidatorWriter extends JavaScriptValidatorWriter<NumberRangeValidator> {

    private static final String MIN_AND_MAX_KEY = "range";
    private static final String MIN_ONLY_KEY = "min";
    private static final String MAX_ONLY_KEY = "max";

    public NumberRangeValidatorWriter() {
        super( NumberRangeValidator.class );
    }

    @Override
    protected void write( JavaScriptWriter writer, NumberRangeValidator validator, boolean isLast ) {
        if( validator.hasMin() && validator.hasMax() ) {
            writer.writeKey( MIN_AND_MAX_KEY );
            writer.writeArray( isLast, validator.getMin(), validator.getMax() );
        } else if( validator.hasMin() ) {
            String value = String.valueOf(validator.getMin());
            writer.writeKeyValue( MIN_ONLY_KEY, value, isLast);
        } else if( validator.hasMax() ) {
            String value = String.valueOf(validator.getMax());
            writer.writeKeyValue( MAX_ONLY_KEY, value, isLast);
        }
    }
}