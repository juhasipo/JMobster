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

import fi.vincit.jmobster.util.groups.HasGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Field annotation is a wrapper for Java annotations. This
 * class encapsulates information required in later processing
 * phases. The annotations given to the constructor should have
 * method called "groups" like JSR-303 annotations. This method
 * is used for filtering annotations by group. If the method doesn't
 * exist, the annotation cannot be filtered with groups.
 */
public class FieldAnnotation implements HasGroups<Class>, HasType {

    private static final Logger LOG = LoggerFactory.getLogger( FieldAnnotation.class );
    private static final Class[] NO_GROUPS = new Class[0];
    private static final String GROUPS_METHOD_NAME = "groups";

    final private Class[] groups;
    final private Class type;
    final private Annotation annotation;

    /**
     * Constructs a new FieldAnnotation. Constructor tries to extract
     * group information from annotation. If that information is not
     * found, the annotation cannot be filtered with groups.
     * @param annotation Annotation to wrap.
     */
    public FieldAnnotation(Annotation annotation) {
        this.groups = extractGroupsFromAnnotation(annotation);
        this.type = annotation.annotationType();
        this.annotation = annotation;
    }

    public static Collection<FieldAnnotation> convertToFieldAnnotations(Annotation[] annotations) {
        Collection<FieldAnnotation> fieldAnnotations = new ArrayList<FieldAnnotation>(annotations.length);
        for( Annotation annotation : annotations ) {
            FieldAnnotation fieldAnnotation = new FieldAnnotation(annotation);
            fieldAnnotations.add(fieldAnnotation);
        }
        return fieldAnnotations;
    }

    private Class[] extractGroupsFromAnnotation( Annotation annotation ) {
        try {
            final Method groupsMethod = annotation.getClass().getMethod( GROUPS_METHOD_NAME );
            final Object result = groupsMethod.invoke(annotation);
            if( result != null ) {
                return (Class[])result;
            }
        } catch( NoSuchMethodException e ) {
            //LOG.warn( "Validator {} doesn't have groups. Ignoring grouping for that annotation", annotation.getClass().getName() );
        } catch( InvocationTargetException e ) {
            LOG.error( "Error e", e );
        } catch( IllegalAccessException e ) {
            LOG.error( "Error e", e );
        }
        return NO_GROUPS;
    }

    @Override
    public Class[] getGroups() {
        return groups;
    }

    @Override
    public boolean hasGroups() {
        return groups.length > 0;
    }

    @Override
    public Class getType() {
        return type;
    }

    public Annotation getAnnotation() {
        return annotation;
    }
}
