package fi.vincit.jmobster.processor.defaults.base;
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

import fi.vincit.jmobster.processor.ValueConverter;

/**
 * <p>
 *     Base implementation for value converters. The class can be
 * extended to create custom value converters if the default
 * ones are not enough. The default implementation will
 * try to convert the value with Java's toString() method
 * and in some cases it won't give correct results.
 * </p>
 *
 * <p>
 *     All extended classes have to implement method
 * {@link BaseValueConverter#getTypeDefaultValue()}
 * which will be used for default values as well as, in the default
 * implementation, for null values. Additionally method
 * {@link BaseValueConverter#getValueAsString(Object)} can be overridden
 * to format the value as required. Note that string and similar
 * values (like enums) may need to have quotation marks added
 * around the value.
 * </p>
 */
public abstract class BaseValueConverter implements ValueConverter {
    /**
     * Return handled type default value. Returned value should
     * be formatted as it appears in target platform. E.g. string for
     * JavaScript should contain the quotation marks around the value.
     * @return Default value as string. Null must not be returned from this method.
     */
    protected abstract String getTypeDefaultValue();

    /**
     * <p>Converts the value to string format. Default implementation uses
     * toString() method and it works well for many cases like BigDecimals.
     * </p>
     * <p>Override this method to implement custom value converter.</p>
     * <p>Value parameter must not be null. The default implementation will makes
     * sure that null isn't passed, but custom implementations must handle this
     * by themselves.</p>
     * @param value Value as object
     * @return Value as string. If null is returned the default value is used.
     */
    protected String getValueAsString(Object value) {
        return value.toString();
    }

    @Override
    public String convertValue(Object value) {
        if( value != null ) {
            String valueAsString = getValueAsString(value);
            return valueAsString != null ? valueAsString : getTypeDefaultValue();
        } else {
            return getTypeDefaultValue();
        }
    }
}
