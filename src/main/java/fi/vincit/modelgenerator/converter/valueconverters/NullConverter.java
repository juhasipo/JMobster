package fi.vincit.modelgenerator.converter.valueconverters;

public final class NullConverter extends BaseValueConverter {
    @Override
    protected String getTypeDefaultValue() {
        return "null";
    }
}
