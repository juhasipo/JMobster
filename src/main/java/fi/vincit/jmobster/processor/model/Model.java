package fi.vincit.jmobster.processor.model;
/*
 * Copyright 2012 Juha Siponen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

import java.util.List;

/**
 * A single model that is converted to the target platform.
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
