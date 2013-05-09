package fi.vincit.jmobster.processor.languages.javascript.valueconverters;
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

import fi.vincit.jmobster.processor.FieldValueConverter;
import fi.vincit.jmobster.processor.defaults.base.BaseValueConverter;

import java.util.Collection;

/**
 * Converter for Java Collections (Lists, Sets, ...).  Implementation
 * requires a {@link FieldValueConverter} in order to convert
 * the map item values. This structure supports nested collections
 * as well as e.g. arrays inside a collections.
 */
public final class CollectionConverter extends BaseValueConverter {

    private static final String EMPTY_COLLECTION = "[]";
    private static final String COLLECTION_START = "[";
    private static final String COLLECTION_END = "]";
    private static final String COLLECTION_ITEM_SEPARATOR = ", ";
    private final FieldValueConverter fieldValueConverter;

    public CollectionConverter( FieldValueConverter fieldValueConverter ) {
        this.fieldValueConverter = fieldValueConverter;
    }

    @Override
    protected String getTypeDefaultValue() {
        return EMPTY_COLLECTION;
    }

    @Override
    protected String getValueAsString( Object values ) {
        Collection collectionObject = (Collection)values;
        StringBuilder sb = new StringBuilder();
        sb.append( COLLECTION_START );
        final int size = collectionObject.size();
        int i = 0;
        for( Object value : collectionObject ) {
            String convertedValue = fieldValueConverter.convert(value.getClass(), value);
            sb.append(convertedValue);
            if( i != size - 1 ) {
                sb.append( COLLECTION_ITEM_SEPARATOR );
            }
            ++i;
        }

        sb.append( COLLECTION_END );

        return sb.toString();
    }
}
