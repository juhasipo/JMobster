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

import java.util.Collection;

public final class CollectionConverter extends BaseValueConverter {

    private JavaToJSValueConverter javaToJSValueConverter;

    public CollectionConverter( JavaToJSValueConverter javaToJSValueConverter ) {
        this.javaToJSValueConverter = javaToJSValueConverter;
    }

    @Override
    protected String getTypeDefaultValue() {
        return "[]";
    }

    @Override
    protected String getValueAsString( Object values ) {
        Collection collectionObject = (Collection)values;
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        final int size = collectionObject.size();
        int i = 0;
        for( Object value : collectionObject ) {
            String convertedValue = javaToJSValueConverter.convert(value.getClass(), value);
            sb.append(convertedValue);
            if( i != size - 1 ) {
                sb.append(", ");
            }
            ++i;
        }

        sb.append("]");

        return sb.toString();
    }
}
