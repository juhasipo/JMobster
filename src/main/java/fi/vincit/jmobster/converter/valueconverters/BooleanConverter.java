package fi.vincit.jmobster.converter.valueconverters;

public final class BooleanConverter extends BaseValueConverter {
    @Override
    protected String getTypeDefaultValue() {
        return "false";
    }
}
