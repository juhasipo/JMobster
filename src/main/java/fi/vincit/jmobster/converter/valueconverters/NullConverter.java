package fi.vincit.jmobster.converter.valueconverters;

public final class NullConverter extends BaseValueConverter {
    @Override
    protected String getTypeDefaultValue() {
        return "null";
    }
}
