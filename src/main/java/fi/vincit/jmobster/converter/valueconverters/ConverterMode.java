package fi.vincit.jmobster.converter.valueconverters;

/**
 * Describes how null values should be handled.
 */
public enum ConverterMode {
    /**
     * Allow nulls as they are. E.g. null string will have JS value null
     * if the given value is null.
     */
    ALLOW_NULL,
    /**
     * Null values are replaced with default value. E.g. null Long will
     * be replaced with 0 and null string with "".
     */
    NULL_AS_DEFAULT
}
