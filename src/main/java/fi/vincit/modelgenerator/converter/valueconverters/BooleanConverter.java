package fi.vincit.modelgenerator.converter.valueconverters;

public final class BooleanConverter extends BaseValueConverter {
    @Override
    protected String getTypeDefaultValue() {
        return "false";
    }
}
