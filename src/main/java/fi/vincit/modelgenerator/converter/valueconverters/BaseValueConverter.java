package fi.vincit.modelgenerator.converter.valueconverters;

import java.lang.reflect.Field;

public abstract class BaseValueConverter implements ValueConverter {
    protected abstract String getTypeDefaultValue();

    @Override
    public String convertValue(Object value) {
        if( value != null ) {
            return value.toString();
        } else {
            return getTypeDefaultValue();
        }
    }
}
