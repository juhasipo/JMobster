package fi.vincit.modelgenerator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public class ModelField {
    private Field field;
    private List<Annotation> annotations;
    private String defaultValue;

    public ModelField( Field field, List<Annotation> validationAnnotations ) {
        this.field = field;
        this.annotations = validationAnnotations;
    }

    public Field getField() {
        return field;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue( String defaultValue ) {
        this.defaultValue = defaultValue;
    }

    public boolean hasValidations() {
        return !annotations.isEmpty();
    }
}
