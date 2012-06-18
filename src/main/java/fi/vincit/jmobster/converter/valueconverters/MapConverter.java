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

import fi.vincit.jmobster.converter.JavaToJSValueConverter;

import java.util.Map;

public class MapConverter extends BaseValueConverter {
    private JavaToJSValueConverter javaToJSValueConverter;

    public MapConverter( JavaToJSValueConverter javaToJSValueConverter ) {
        this.javaToJSValueConverter = javaToJSValueConverter;
    }

    @Override
    protected String getTypeDefaultValue() {
        return "{}";
    }

    @Override
    protected String getValueAsString( Object values ) {
        if( values == null ) {
            return getTypeDefaultValue();
        }
        Map<Object,Object> map = (Map)values;
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        final int size = map.size();
        int i = 0;
        for( Map.Entry<Object,Object> entry : map.entrySet() ) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            String convertedKey = javaToJSValueConverter.convert(key.getClass(), key);
            String convertedValue = getEntryValue( value );

            sb.append(convertedKey).append(": ");
            sb.append(convertedValue);
            if( i != size - 1 ) {
                sb.append(", ");
            }
            ++i;
        }

        sb.append("}");

        return sb.toString();
    }

    private String getEntryValue( Object value ) {
        if( value != null ) {
            return javaToJSValueConverter.convert(value.getClass(), value);
        } else {
            return javaToJSValueConverter.convert((Class)null, null);
        }
    }
}
