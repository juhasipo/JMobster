package fi.vincit.jmobster.converter.valueconverters;

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
    public String convertValue( Object values ) {
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
