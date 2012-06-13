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
        Class<?> t = field.getType();
        String defaultValue = extractDefaultValue( field, defaultObject );
        if( t.equals( String.class ) ) {
            return defaultValue != null ? "\""+defaultValue+"\"" : "\"\"";
        } else if( t.equals(Integer.class) || t.equals(Long.class) || t.equals(int.class) || t.equals(long.class) ) {
            return defaultValue != null ? defaultValue : "0";
        } else if( t.equals(Float.class) || t.equals(Double.class) || t.equals(float.class) || t.equals(double.class) ) {
            return defaultValue != null ? defaultValue : "0.0";
        } else if( t.equals(Array.class) || t.isArray() || t.equals(List.class) || t.equals(Collection.class) ) {
            return defaultValue != null ? defaultValue : "[]";
        } else if( t.equals(Boolean.class) || t.equals(boolean.class) ) {
            return defaultValue != null ? defaultValue : "false";
        } else if( t.equals(Map.class) || t.equals(Set.class) ) {
            return defaultValue != null ? defaultValue : "{}";
        }

        LOG.error("Invalid type of class {} for field {}", field.getType().toString(), field.getName());
        return "INVALID";
    }

    private String extractDefaultValue( Field field, Object defaultObject ) {
        String defaultValue;
        try {
            Object fieldValue = field.get( defaultObject );
            defaultValue = fieldValue != null ? "" + fieldValue : null;
        } catch( IllegalAccessException e ) {
            defaultValue = null;
        }
        return defaultValue;
    }
}
