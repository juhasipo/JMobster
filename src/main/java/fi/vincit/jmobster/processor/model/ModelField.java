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

import fi.vincit.jmobster.util.AnnotationBag;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Single model field that is converted to the target platform.
 */
public class ModelField {
    final private Class fieldType;
    final private Collection<Validator> validators;
    final private AnnotationBag fieldAnnotations;
    final private String name;

    /**
     * Model field constructed from Java object field
     * @param field Java field
     * @param validators Validators
     */
    public ModelField( Field field, Collection<Validator> validators ) {
        this.fieldType = field.getType();
        this.name = field.getName();
        this.validators = new ArrayList<Validator>();
        this.fieldAnnotations = new AnnotationBag();
        addValidators(validators);
    }

    /**
     * Model field constructed from bean property
     * @param property Bean property
     * @param validators Validators
     */
    public ModelField( PropertyDescriptor property, Collection<Validator> validators ) {
        this.fieldType  = property.getPropertyType();
        this.name = property.getName();
        this.validators = new ArrayList<Validator>();
        this.fieldAnnotations = new AnnotationBag();
        addValidators(validators);
    }

    /**
     * Model field without validators.
     * @param field
     */
    public ModelField( ModelField field ) {
        this.fieldType = field.getFieldType();
        this.name = field.getName();
        this.validators = new ArrayList<Validator>();
        this.fieldAnnotations = new AnnotationBag();
        addValidators(field.getValidators());
    }

    public String getName() {
        return name;
    }

    public Class getFieldType() {
        return fieldType;
    }

    public void addValidators(Collection<? extends Validator> validators) {
        this.validators.addAll(validators);
    }

    public Collection<Validator> getValidators() {
        return Collections.unmodifiableCollection(this.validators);
    }

    public void addAnnotation(FieldAnnotation annotation) {
        this.fieldAnnotations.addAnnotation(annotation);
    }

    public <T extends Annotation> T getAnnotation( Class<T> clazz ) {
        return fieldAnnotations.getAnnotation( clazz );
    }

    public <T extends Annotation> boolean hasAnnotation( Class<T> annotationType ) {
        return fieldAnnotations.hasAnnotation( annotationType );
    }
}
