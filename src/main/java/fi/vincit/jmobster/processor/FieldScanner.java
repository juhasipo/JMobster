package fi.vincit.jmobster.processor;
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

import fi.vincit.jmobster.annotation.IgnoreDefaultValue;
import fi.vincit.jmobster.annotation.IgnoreField;
import fi.vincit.jmobster.backbone.AnnotationProcessorProvider;
import fi.vincit.jmobster.converter.FieldValueConverter;
import fi.vincit.jmobster.exception.CannotAccessDefaultConstructorError;
import fi.vincit.jmobster.exception.DefaultConstructorMissingError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Scans the given model for fields that should be
 * included in the client side model.
 */
public class FieldScanner {
    private static final Logger LOG = LoggerFactory
            .getLogger( FieldScanner.class );

    private FieldValueConverter fieldDefaultValueProcessor;
    private AnnotationProcessorProvider annotationProcessorProvider;

    /**
     * Creates new field scanner
     * @param fieldDefaultValueProcessor Field default value processor
     * @param annotationProcessorProvider Annotation processor provider
     */
    public FieldScanner( FieldValueConverter fieldDefaultValueProcessor, AnnotationProcessorProvider annotationProcessorProvider ) {
        this.fieldDefaultValueProcessor = fieldDefaultValueProcessor;
        this.annotationProcessorProvider = annotationProcessorProvider;
    }

    /**
     * Scans the given class for model fields.
     * @param clazz Class to scan
     * @return List of model fields. Empty list if nothing found.
     * @throws CannotAccessDefaultConstructorError If the default constructor exists but cannot be accessed
     * @throws DefaultConstructorMissingError If the given model does not have a default constructor
     */
    public List<ModelField> getFields(Class clazz) {
        List<ModelField> fields = new ArrayList<ModelField>();

        try {
            Object defaultObject = clazz.newInstance();
            for( Field field : clazz.getDeclaredFields() ) {
                field.setAccessible(true);
                if( shouldAddField(field) ) {
                    ModelField modelField = new ModelField(field, getValidationAnnotations(field));
                    modelField.setDefaultValue(fieldDefaultValueProcessor.convert( field, defaultObject ));
                    fields.add( modelField );
                } else {
                    LOG.warn( "Field {} not added to model fields", field.getName() );
                }
            }
        } catch( InstantiationException e ) {
            LOG.error("Instantiation failed", e);
            throw new DefaultConstructorMissingError("Class " + clazz + " does not have a default constructor");
        } catch( IllegalAccessException e ) {
            LOG.error( "Illegal access", e );
            throw new CannotAccessDefaultConstructorError(e.getMessage());
        }

        return fields;
    }

    /**
     * Gets the validation annotations for the given field
     * @param field Field to scan for annotations
     * @return Validation annotation. If nothing found returns an empty list.
     */
    private List<Annotation> getValidationAnnotations( Field field ) {
        List<Annotation> validationAnnotations = new ArrayList<Annotation>();
        for( Annotation annotation : field.getAnnotations() ) {
            if( isValidationAnnotation(annotation) ) {
                validationAnnotations.add(annotation);
            }
        }
        return validationAnnotations;
    }

    /**
     * Checks if the given annotation is a validation annotation.
     * @param annotation Annotation to check
     * @return True if the given annotation is a validation annotation. Otherwise false.
     */
    private boolean isValidationAnnotation( Annotation annotation ) {
        return annotationProcessorProvider.isAnnotationForValidation( annotation );

    }

    /**
     * Checks if the field be added to model fields.
     * @param field Field to checks
     * @return True if the field should be included in model fields. Otherwise false.
     */
    private boolean shouldAddField(Field field) {
        return !field.isAnnotationPresent( IgnoreField.class );
    }
}
