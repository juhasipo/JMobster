package fi.vincit.jmobster.processor.languages.javascript.valueconverters;
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

import fi.vincit.jmobster.processor.defaults.base.BaseValueConverter;

/**
 * Converter for Java Strings.
 */
public class StringConverter extends BaseValueConverter {
    private static final String JAVASCRIPT_STRING_QUOTE_MARK = "\"";

    @Override
    protected String getTypeDefaultValue() {
        return JAVASCRIPT_STRING_QUOTE_MARK + JAVASCRIPT_STRING_QUOTE_MARK;
    }

    @Override
    protected String getValueAsString( Object value ) {
        return JAVASCRIPT_STRING_QUOTE_MARK + value + JAVASCRIPT_STRING_QUOTE_MARK;
    }
}
