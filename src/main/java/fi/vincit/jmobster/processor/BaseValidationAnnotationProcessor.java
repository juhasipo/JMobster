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

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;

import fi.vincit.jmobster.exception.InvalidType;
import fi.vincit.jmobster.util.ModelWriter;

public abstract class BaseValidationAnnotationProcessor implements ValidationAnnotationProcessor {
    private String requiredType;
    private CombinationManager combinationManager;
    private HashMap<Class, Annotation> annotationBag;
    private Class baseValidatorForClass;

    protected BaseValidationAnnotationProcessor( String requiredType, RequiredTypes requiredAnnotation) {
        this.requiredType = requiredType;
        this.combinationManager = new CombinationManager(requiredAnnotation);
    }

    protected BaseValidationAnnotationProcessor( RequiredTypes requiredAnnotation) {
        this.combinationManager = new CombinationManager(requiredAnnotation);
    }

    protected BaseValidationAnnotationProcessor( String requiredType, RequiredTypes requiredAnnotation, OptionalTypes supportedAnnotations) {
        this.requiredType = requiredType;
        this.combinationManager = new CombinationManager(requiredAnnotation, supportedAnnotations);
    }

    protected BaseValidationAnnotationProcessor(RequiredTypes requiredAnnotation, OptionalTypes supportedAnnotations) {
        this.combinationManager = new CombinationManager(requiredAnnotation, supportedAnnotations);
    }

    protected void setBaseValidatorForClass( Class baseValidatorForClass ) {
        this.baseValidatorForClass = baseValidatorForClass;
    }

    @Override
    public Class getBaseValidatorForClass() {
        return baseValidatorForClass;
    }

    @Override
    public boolean isBaseValidator() {
        return getBaseValidatorForClass() != null;
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
                throw new InvalidType("Required type <" + requiredType() + "> given type <" + type + ">");
            }
        }
    }

    @Override
    public Class[] getGroups(Annotation annotation) {
        Class[] groups = getGroupsInternal(annotation);
        return groups != null ? groups : new Class[0];
    }

    protected abstract Class[] getGroupsInternal(Annotation annotation);

    @Override
    public boolean hasGroups(Annotation annotation) {
        Class[] groups = getGroups(annotation);
        return groups != null && groups.length > 0;
    }

    @Override
    public boolean canProcess( List<Annotation> annotations ) {
        return combinationManager.supports(annotations);
    }


    private void prepareForWrite(List<Annotation> annotations) {
        annotationBag = new HashMap<Class, Annotation>();
        for( Annotation a : annotations ) {
            if( combinationManager.containsClass(a.annotationType()) ) {
                annotationBag.put(a.annotationType(), a);
            }
        }
    }

    private void finishWrite() {
        annotationBag.clear();
        annotationBag = null;
    }

    protected <T> boolean containsAnnotation(Class<T> clazz) {
        return annotationBag.containsKey(clazz);
    }

    protected <T> T findAnnotation(Class<T> clazz) {
        if( annotationBag.containsKey(clazz) ) {
            return (T)annotationBag.get(clazz);
        } else {
            throw new RuntimeException("Annotation of type " + clazz.getName() + " not found");
        }
    }

    @Override
    public void writeValidatorsToStream( List<Annotation> annotations, ModelWriter writer ) {
        prepareForWrite(annotations);
        writeValidatorsToStreamInternal(writer);
        finishWrite();
    }

    protected abstract void writeValidatorsToStreamInternal(ModelWriter writer);


}
