package fi.vincit.jmobster.converter.valueconverters;

public class StringConverter extends BaseValueConverter {
    @Override
    protected String getTypeDefaultValue() {
        return "\"\"";
    }

    @Override
    public String convertValue( Object value ) {
        return "\"" + value + "\"";
    }
}
