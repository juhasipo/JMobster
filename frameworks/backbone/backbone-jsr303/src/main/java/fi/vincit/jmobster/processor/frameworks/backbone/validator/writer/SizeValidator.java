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

import javax.validation.constraints.Size;

public class SizeValidator extends BaseValidatorWriter<JavaScriptContext, JavaScriptWriter> {

    private static final String MIN_AND_MAX_KEY = "rangeLength";
    private static final String MIN_ONLY_KEY = "minLength";
    private static final String MAX_ONLY_KEY = "maxLength";

    public void write(Size size) {
        final boolean hasMin = size.min() > 0;
        final boolean hasMax = size.max() < Integer.MAX_VALUE;
        if( hasMin && hasMax ) {
            getWriter().writeKey( MIN_AND_MAX_KEY );
            getWriter().writeArray( size.min(), size.max() );
        } else if( hasMin ) {
            String value = String.valueOf(size.min());
            getWriter().writeKeyValue( MIN_ONLY_KEY, value);
        } else if( hasMax ) {
            String value = String.valueOf(size.max());
            getWriter().writeKeyValue( MAX_ONLY_KEY, value);
        }
    }
}
