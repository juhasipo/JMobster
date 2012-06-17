package fi.vincit.jmobster;

import java.util.List;

/**
 * A single model that is converted to
 * the target platform.
 */
public class Model {
    private Class modelClass;
    private List<ModelField> fields;
    private boolean validations;

    public Model( Class modelClass, List<ModelField> fields ) {
        this.modelClass = modelClass;
        this.fields = fields;
    }

    public Class getModelClass() {
        return modelClass;
    }

    public List<ModelField> getFields() {
        return fields;
    }

    /**
     *
     * @return True if one or more model fields has one or more validation annotations.
     */
    public boolean hasValidations() {
        return validations;
    }
    public void setValidations(boolean validations) {
        this.validations = validations;
    }
}
