package fi.vincit.jmobster.converter.valueconverters;

/**
 * Interface for converting Java values to target
 * platform's values.
 */
public interface ValueConverter {
    /**
     * Return given value in string form. Returned value should
     * be formatted as it appears in target platform. E.g. string for
     * JavaScript should contain the quotation marks around the value.
     * @param value Value to convert
     * @return Value as string.
     */
    String convertValue(Object value);
}
