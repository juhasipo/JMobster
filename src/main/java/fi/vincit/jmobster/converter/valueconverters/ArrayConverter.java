package fi.vincit.jmobster.converter.valueconverters;

import fi.vincit.jmobster.converter.JavaToJSValueConverter;

public final class ArrayConverter extends BaseValueConverter {

    private JavaToJSValueConverter javaToJSValueConverter;

    public ArrayConverter( JavaToJSValueConverter javaToJSValueConverter ) {
        this.javaToJSValueConverter = javaToJSValueConverter;
    }

    @Override
    protected String getTypeDefaultValue() {
        return "[]";
    }

    @Override
    public String convertValue( Object values ) {
        if( values == null ) {
            return getTypeDefaultValue();
        }
        return convertArrayFromObject( values );
    }

    private String convertArrayFromObject( Object value ) {
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
        sb.append("[");
        final int size = values.length;
        int i = 0;
        for( Object value : values ) {
            i = convertObject( sb, size, i, value );
        }

        sb.append("]");

        return sb.toString();
    }


    private String convertArray( int[] values ) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        final int size = values.length;
        int i = 0;
        for( Object value : values ) {
            i = convertObject( sb, size, i, value );
        }

        sb.append("]");

        return sb.toString();
    }

    private String convertArray( long[] values ) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        final int size = values.length;
        int i = 0;
        for( Object value : values ) {
            i = convertObject( sb, size, i, value );
        }

        sb.append("]");

        return sb.toString();
    }

    private String convertArray( float[] values ) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        final int size = values.length;
        int i = 0;
        for( Object value : values ) {
            i = convertObject( sb, size, i, value );
        }

        sb.append("]");

        return sb.toString();
    }

    private String convertArray( double[] values ) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        final int size = values.length;
        int i = 0;
        for( Object value : values ) {
            i = convertObject( sb, size, i, value );
        }

        sb.append("]");

        return sb.toString();
    }

    private String convertArray( boolean[] values ) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        final int size = values.length;
        int i = 0;
        for( Object value : values ) {
            i = convertObject( sb, size, i, value );
        }

        sb.append("]");

        return sb.toString();
    }

    private int convertObject( StringBuilder sb, final int size, final int i, final Object value ) {
        String convertedValue = javaToJSValueConverter.convert( value.getClass(), value );
        sb.append(convertedValue);
        if( i != size - 1 ) {
            sb.append(", ");
        }
        return i + 1;
    }
}
