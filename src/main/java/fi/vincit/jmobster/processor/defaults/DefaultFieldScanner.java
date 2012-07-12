package fi.vincit.jmobster.processor.defaults;
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

import fi.vincit.jmobster.annotation.IgnoreField;
import fi.vincit.jmobster.exception.CannotAccessDefaultConstructorError;
import fi.vincit.jmobster.exception.DefaultConstructorMissingError;
import fi.vincit.jmobster.processor.FieldAnnotationWriter;
import fi.vincit.jmobster.processor.FieldScanner;
import fi.vincit.jmobster.processor.FieldValueConverter;
import fi.vincit.jmobster.processor.model.ModelField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Scans the given model for fields that should be
 * included in the client side model.
 */
public class DefaultFieldScanner implements FieldScanner {
    private static final Logger LOG = LoggerFactory.getLogger( DefaultFieldScanner.class );

    private final FieldValueConverter fieldDefaultValueProcessor;
    private final FieldAnnotationWriter fieldAnnotationWriter;
    private boolean allowStaticFields;
    private boolean allowFinalFields;
    private final FieldScanMode scanMode;

    /**
     * Creates new field scanner
     * @param fieldDefaultValueProcessor Field default value processor
     * @param fieldAnnotationWriter Annotation processor provider
     */
    public DefaultFieldScanner( FieldScanMode scanMode, FieldValueConverter fieldDefaultValueProcessor, FieldAnnotationWriter fieldAnnotationWriter ) {
        this.fieldDefaultValueProcessor = fieldDefaultValueProcessor;
        this.fieldAnnotationWriter = fieldAnnotationWriter;
        this.allowStaticFields = false;
        this.allowFinalFields = true;
        this.scanMode = scanMode;
    }

    @Override
    public List<ModelField> getFields( Class clazz ) {
        switch( scanMode ) {
            case BEAN_PROPERTY: return getFieldsByGetters(clazz);
            case DIRECT_FIELD_ACCESS: return getFieldsByDirectFieldAccess( clazz );
            default: throw new RuntimeException("Invalid field scan mode: " + scanMode);
        }
    }

    /**
     * Scans the given class for model fields. Accesses bean properties (getter methods).
     * @param clazz Class to scan
     * @return List of model fields. Empty list if nothing found.
     * @throws CannotAccessDefaultConstructorError If the default constructor exists but cannot be accessed
     * @throws DefaultConstructorMissingError If the given model does not have a default constructor
     */    private List<ModelField> getFieldsByGetters(Class clazz) {
        List<ModelField> fields = new ArrayList<ModelField>();
        try {
            Object defaultObject = clazz.newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo( clazz );
            for( PropertyDescriptor property : beanInfo.getPropertyDescriptors() ) {
                if( shouldAddField(property) ) {
                    final String name = property.getName();
                    if( name.equals("class") ) { continue; }
                    ModelField field = new ModelField(property, getValidationAnnotations(property));
                    field.setDefaultValue(fieldDefaultValueProcessor.convert(property.getPropertyType(), property.getReadMethod().invoke(defaultObject)));
                    fields.add(field);
                }
            }
        } catch( IntrospectionException e ) {
            LOG.error("Introspection failed", e);
            throw new RuntimeException("Introspection failed", e);
        } catch( InvocationTargetException e ) {
            LOG.error("Invocation failed", e);
            throw new RuntimeException("Invocation failed", e);
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
     * Scans the given class for model fields. Accesses field directly via member variables.
     * @param clazz Class to scan
     * @return List of model fields. Empty list if nothing found.
     * @throws CannotAccessDefaultConstructorError If the default constructor exists but cannot be accessed
     * @throws DefaultConstructorMissingError If the given model does not have a default constructor
     */
    private List<ModelField> getFieldsByDirectFieldAccess( Class clazz ) {
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
     * Gets the validation annotations for the given bean property
     * @param property Bean property
     * @return Validation annotations. If nothing found returns an empty list.
     */
    private List<Annotation> getValidationAnnotations( PropertyDescriptor property ) {
        List<Annotation> validationAnnotations = new ArrayList<Annotation>();
        for( Annotation annotation : property.getReadMethod().getAnnotations() ) {
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
        return fieldAnnotationWriter.isAnnotationForValidation( annotation );
    }

    /**
     * Checks if the field should be added to model fields.
     * @param field Field to checks
     * @return True if the field should be included in model fields. Otherwise false.
     */
    private boolean shouldAddField(Field field) {
        return allowedByAnnotation(field) && allowedByModifiers(field.getModifiers());
    }

    /**
     * Checks if the property should be added to model fields. Uses
     * the getter method properties to determine this.
     * @param property Bean property to check
     * @return True if the property should be inclided in the model fields. Otherwise false.
     */
    private boolean shouldAddField(PropertyDescriptor property) {
        Method method = property.getReadMethod();
        return allowedByAnnotation(method) && allowedByModifiers(method.getModifiers());
    }

    /**
     * Checks if the given modifiers allow a field or property to
     * be added to model fields
     * @param modifiers Modifiers
     * @return True if the field or property should be included, otherwise false.
     */
    private boolean allowedByModifiers(int modifiers) {
       return  (allowStaticFields || !Modifier.isStatic(modifiers))
                && (allowFinalFields  || !Modifier.isFinal(modifiers));
    }

    /**
     * Checks if the given field or property should be added to model
     * fields. For this method the bean property getter method should
     * be given.
     * @param element Bean property getter or field
     * @return True if the field or property should be included, otherwise false.
     */
    private boolean allowedByAnnotation(AnnotatedElement element) {
        return !element.isAnnotationPresent(IgnoreField.class);
    }

    @Override
    public void setAllowStaticFields( boolean allowStaticFields ) {
        this.allowStaticFields = allowStaticFields;
    }

    @Override
    public void setAllowFinalFields( boolean allowFinalFields ) {
        this.allowFinalFields = allowFinalFields;
    }
}
