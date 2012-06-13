package fi.vincit.modelgenerator;

import fi.vincit.modelgenerator.ValidationAnnotationProcessor;

public abstract class BaseValidationAnnotationProcessor implements ValidationAnnotationProcessor {
    private String requiredType;

    public BaseValidationAnnotationProcessor() {
    }

    public BaseValidationAnnotationProcessor( String requiredType ) {
        this.requiredType = requiredType;
    }

    @Override
    public String requiredType() {
        return requiredType;
    }

    @Override
    public boolean requiresType() {
        return requiredType() != null;
    }

    @Override
    public void validateType( String type ) {
        if( requiresType() ) {
            if( !requiredType().equals(type) ) {
                throw new RuntimeException("Required type <" + requiredType() + "> given type <" + type + ">");
            }
        }
    }

}
