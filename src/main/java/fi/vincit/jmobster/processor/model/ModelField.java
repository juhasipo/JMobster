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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Single model field that is converted to the target platform.
 */
public class ModelField {
    final private Class fieldType;
    final private List<Validator> validators;
    private String defaultValue;
    final private String name;

    public ModelField( Field field, Collection<Validator> validators ) {
        this.fieldType = field.getType();
        this.name = field.getName();
        this.validators = new ArrayList<Validator>();
        addValidators(validators);
    }

    public ModelField( PropertyDescriptor property, Collection<Validator> validators ) {
        this.fieldType  = property.getPropertyType();
        this.name = property.getName();
        this.validators = new ArrayList<Validator>();
        addValidators(validators);
    }

    public ModelField( ModelField field ) {
        this.fieldType = field.getFieldType();
        this.name = field.getName();
        this.validators = new ArrayList<Validator>();
        addValidators(field.getValidators());
    }

    public String getName() {
        return name;
    }

    public Class getFieldType() {
        return fieldType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue( String defaultValue ) {
        this.defaultValue = defaultValue;
    }

    public void addValidators(Collection<? extends Validator> validators) {
        this.validators.addAll(validators);
    }

    public Collection<Validator> getValidators() {
        return this.validators;
    }

    /**
     * @return True if the model field contains one or more validation annotations.
     */
    public boolean hasValidations() {
        return !validators.isEmpty();
    }
}
