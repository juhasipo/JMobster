package fi.vincit.jmobster.processor.model;
/*
 * Copyright 2012-2013 Juha Siponen
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

import java.util.Collection;
import java.util.Collections;

/**
 * A single model that is converted to the target platform.
 */
public class Model {
    private final Class modelClass;
    private final Collection<ModelField> fields;
    private boolean validations;
    private final String name;

    /**
     * Model constructed from fields
     * @param modelClass Class this model represents
     * @param name Name in target platform
     * @param fields Fields
     */
    public Model( Class modelClass, String name, Collection<ModelField> fields ) {
        this.name = name;
        this.modelClass = modelClass;
        this.fields = fields;
        this.validations = false;
        checkIfValidationsInFields();
    }

    private void checkIfValidationsInFields() {
        for( ModelField field : this.fields ) {
            if( field.hasAnnotations() ) {
                this.validations = true;
                break;
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public Class getModelClass() {
        return modelClass;
    }

    public Collection<ModelField> getFields() {
        return Collections.unmodifiableCollection( fields );
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

    @Override
    public String toString() {
        return this.name;
    }
}
