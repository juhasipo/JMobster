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

import fi.vincit.jmobster.processor.FieldValueConverter;

import java.util.Map;

/**
 * Converter for Java Map objects. Implementation
 * requires a {@link FieldValueConverter} in order to convert
 * the map item values. This structure supports nested maps
 * as well as e.g. arrays inside a map.
 */
public class MapConverter extends BaseValueConverter {
    private static final String EMPTY_MAP = "{}";
    private static final String MAP_START = "{";
    private static final String MAP_END = "}";
    private static final String MAP_KEY_VALUE_SEPARATOR = ": ";
    private static final String MAP_KEY_VALUE_PAIR_SEPARATOR = ", ";

    private FieldValueConverter fieldValueConverter;

    public MapConverter( FieldValueConverter fieldValueConverter ) {
        this.fieldValueConverter = fieldValueConverter;
    }

    @Override
    protected String getTypeDefaultValue() {
        return EMPTY_MAP;
    }

    @Override
    protected String getValueAsString( Object values ) {
        Map<Object,Object> map = (Map)values;
        StringBuilder sb = new StringBuilder();
        sb.append( MAP_START );
        final int size = map.size();
        int i = 0;
        for( Map.Entry<Object,Object> entry : map.entrySet() ) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            String convertedKey = fieldValueConverter.convert(key.getClass(), key);
            String convertedValue = getEntryValue( value );

            sb.append(convertedKey).append( MAP_KEY_VALUE_SEPARATOR );
            sb.append(convertedValue);
            if( i != size - 1 ) {
                sb.append( MAP_KEY_VALUE_PAIR_SEPARATOR );
            }
            ++i;
        }

        sb.append( MAP_END );

        return sb.toString();
    }

    private String getEntryValue( Object value ) {
        if( value != null ) {
            return fieldValueConverter.convert(value.getClass(), value);
        } else {
            return fieldValueConverter.convert((Class)null, null);
        }
    }
}
