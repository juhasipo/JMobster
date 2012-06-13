package fi.vincit.modelgenerator;

import java.lang.reflect.Field;

public class FieldDefaultValueProcessor {
    public String getDefaultValue(Field field, Object defaultObject) {
        return "\"default\"";
    }
}
