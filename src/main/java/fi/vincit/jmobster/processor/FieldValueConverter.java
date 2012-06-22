package fi.vincit.jmobster.processor;/*
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

import java.lang.reflect.Field;

/**
 * <p>
 *     Interface for converting field values by
 * using a default value object. The default value object is
 * an object from which the default values are get
 * via reflection.
 * </p>
 * <p>
 *     There is also a support for converting given field by
 * the field value type. Usually this type of converting
 * can be used in {@link fi.vincit.jmobster.processor.languages.javascript.valueconverters.ValueConverter}
 * classes when a recursive converting is required.</p>
 */
public interface FieldValueConverter {
    /**
     * Convert the field of the given defaultValueObject
     * to string.
     * @param field Field to convert
     * @param defaultValueObject Default object from which the field
     *                      value is get.
     * @return Value as string
     */
    String convert( Field field, Object defaultValueObject );

    /**
     * Convert the given fieldValue using the converter
     * for the given class.
     * @param type Converter type to use
     * @param fieldValue Field value to convert
     * @return Value as string
     */
    String convert( Class type, Object fieldValue );
}
