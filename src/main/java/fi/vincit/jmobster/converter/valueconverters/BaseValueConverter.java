package fi.vincit.jmobster.converter.valueconverters;
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

/**
 * Base implementation for value converters.
 */
public abstract class BaseValueConverter implements ValueConverter {
    /**
     * Return handled type default value. Returned value should
     * be formatted as it appears in target platform. E.g. string for
     * JavaScript should contain the quotation marks around the value.
     * @return Default value as string.
     */
    protected abstract String getTypeDefaultValue();

    @Override
    public String convertValue(Object value) {
        if( value != null ) {
            return value.toString();
        } else {
            return getTypeDefaultValue();
        }
    }
}
