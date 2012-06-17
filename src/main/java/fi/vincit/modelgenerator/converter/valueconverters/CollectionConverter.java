package fi.vincit.modelgenerator.converter.valueconverters;

import fi.vincit.modelgenerator.converter.JavaToJSValueConverter;

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
    public String convertValue( Object values ) {
        if( values == null ) {
            return getTypeDefaultValue();
        }
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
