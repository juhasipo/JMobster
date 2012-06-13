package fi.vincit.modelgenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FieldDefaultValueProcessor {
    private static final Logger LOG = LoggerFactory
            .getLogger( FieldDefaultValueProcessor.class );

    public String getDefaultValue(Field field, Object defaultObject) {
        Class<?> fieldType = field.getType();
        String defaultValue = extractDefaultValue( field, defaultObject );
        if( fieldType.equals( String.class ) ) {
            return defaultValue != null ? "\""+defaultValue+"\"" : "\"\"";
        } else if( fieldType.equals(Integer.class) || fieldType.equals(Long.class) || fieldType.equals(int.class) || fieldType.equals(long.class) ) {
            return defaultValue != null ? defaultValue : "0";
        } else if( fieldType.equals(Float.class) || fieldType.equals(Double.class) || fieldType.equals(float.class) || fieldType.equals(double.class) ) {
            return defaultValue != null ? defaultValue : "0.0";
        } else if( fieldType.equals(Array.class) || fieldType.isArray() || fieldType.equals(List.class) || fieldType.equals(Collection.class) ) {
            return "[]";
        } else if( fieldType.equals(Boolean.class) || fieldType.equals(boolean.class) ) {
            return defaultValue != null ? defaultValue : "false";
        } else if( fieldType.equals(Map.class) || fieldType.equals(Set.class) ) {
            return "{}";
        }

        LOG.error("Invalid type of class {} for field {}", field.getType().toString(), field.getName());
        return "INVALID";
    }

    private String extractDefaultValue( Field field, Object defaultObject ) {
        String defaultValue;
        try {
            field.setAccessible(true);
            Object fieldValue = field.get( defaultObject );
            defaultValue = fieldValue != null ? "" + fieldValue : null;
        } catch( IllegalAccessException e ) {
            LOG.error("Could not access field {}", field.getName());
            defaultValue = null;
        }
        return defaultValue;
    }
}
