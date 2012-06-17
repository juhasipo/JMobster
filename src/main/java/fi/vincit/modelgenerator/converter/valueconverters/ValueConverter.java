package fi.vincit.modelgenerator.converter.valueconverters;

import java.lang.reflect.Field;

public interface ValueConverter {
    String convertValue(Object value);
}
