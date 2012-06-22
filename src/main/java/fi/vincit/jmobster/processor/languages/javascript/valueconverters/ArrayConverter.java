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

/**
 * Converter for Java primitive and object arrays.
 */
public final class ArrayConverter extends BaseValueConverter {

    private static final String ARRAY_START = "[";
    private static final String EMPTY_ARRAY = "[]";
    private static final String ARRAY_END = "]";
    private static final String ARRAY_ITEM_SEPARATOR = ", ";
    private FieldValueConverter fieldValueConverter;

    public ArrayConverter( FieldValueConverter fieldValueConverter ) {
        this.fieldValueConverter = fieldValueConverter;
    }

    @Override
    protected String getTypeDefaultValue() {
        return EMPTY_ARRAY;
    }

    @Override
    protected String getValueAsString( Object value ) {
        Class componentType = value.getClass().getComponentType();

        //  Need to figure out the type for primitives by hand
        //  and make the cast manually because the primitive arrays
        //  cannot be casted to Object[].

        if( componentType == int.class ) {
            return convertArray((int[])value);
        } else if( componentType == long.class ) {
            return convertArray((long[])value);
        } else if( componentType == float.class ) {
            return convertArray((float[])value);
        } else if( componentType == double.class ) {
            return convertArray((double[])value);
        } else if( componentType == boolean.class ) {
            return convertArray((boolean[])value);
        }
        // Not a primitive, then it should be OK to cast
        // to a Object array because the array consists of
        // objects
        return convertArray((Object[])value);
    }

    private String convertArray( Object[] values ) {
        StringBuilder sb = new StringBuilder();
        sb.append( ARRAY_START );
        final int size = values.length;
        int i = 0;
        for( Object value : values ) {
            i = convertObject( sb, size, i, value );
        }

        sb.append( ARRAY_END );

        return sb.toString();
    }


    private String convertArray( int[] values ) {
        StringBuilder sb = new StringBuilder();
        sb.append( ARRAY_START );
        final int size = values.length;
        int i = 0;
        for( Object value : values ) {
            i = convertObject( sb, size, i, value );
        }

        sb.append( ARRAY_END );

        return sb.toString();
    }

    private String convertArray( long[] values ) {
        StringBuilder sb = new StringBuilder();
        sb.append( ARRAY_START );
        final int size = values.length;
        int i = 0;
        for( Object value : values ) {
            i = convertObject( sb, size, i, value );
        }

        sb.append( ARRAY_END );

        return sb.toString();
    }

    private String convertArray( float[] values ) {
        StringBuilder sb = new StringBuilder();
        sb.append( ARRAY_START );
        final int size = values.length;
        int i = 0;
        for( Object value : values ) {
            i = convertObject( sb, size, i, value );
        }

        sb.append( ARRAY_END );

        return sb.toString();
    }

    private String convertArray( double[] values ) {
        StringBuilder sb = new StringBuilder();
        sb.append( ARRAY_START );
        final int size = values.length;
        int i = 0;
        for( Object value : values ) {
            i = convertObject( sb, size, i, value );
        }

        sb.append( ARRAY_END );

        return sb.toString();
    }

    private String convertArray( boolean[] values ) {
        StringBuilder sb = new StringBuilder();
        sb.append( ARRAY_START );
        final int size = values.length;
        int i = 0;
        for( Object value : values ) {
            i = convertObject( sb, size, i, value );
        }

        sb.append( ARRAY_END );

        return sb.toString();
    }

    private int convertObject( StringBuilder sb, final int size, final int i, final Object value ) {
        String convertedValue = fieldValueConverter.convert( value.getClass(), value );
        sb.append(convertedValue);
        if( i != size - 1 ) {
            sb.append( ARRAY_ITEM_SEPARATOR );
        }
        return i + 1;
    }
}
