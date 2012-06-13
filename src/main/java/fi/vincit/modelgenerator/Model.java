package fi.vincit.modelgenerator;

import java.util.List;

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

    public boolean hasValidations() {
        return validations;
    }
    public void setValidations(boolean validations) {
        this.validations = validations;
    }
}
