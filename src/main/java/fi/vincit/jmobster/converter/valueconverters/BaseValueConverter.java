package fi.vincit.jmobster.converter.valueconverters;

/**
 * Base implementation for value converters.
 */
public abstract class BaseValueConverter implements ValueConverter {
    /**
     * Return handled type default value. Returned value should
     * be formatted as it appears in target platform. E.g. string for
     * JavaScript should contain the quotation marks around the value.
     * @return Default value as string.
     */
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
