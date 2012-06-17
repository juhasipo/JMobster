package fi.vincit.jmobster.converter.valueconverters;

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
