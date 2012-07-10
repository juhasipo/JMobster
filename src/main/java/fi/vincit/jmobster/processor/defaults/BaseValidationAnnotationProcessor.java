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

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;

import fi.vincit.jmobster.exception.InvalidType;
import fi.vincit.jmobster.processor.ValidationAnnotationProcessor;
import fi.vincit.jmobster.util.CombinationManager;
import fi.vincit.jmobster.util.ModelWriter;
import fi.vincit.jmobster.util.OptionalTypes;
import fi.vincit.jmobster.util.RequiredTypes;

/** Abstract base class for annotation processors.
 * <p>
 *     For each annotation type there should be at least one processor which is
 *     added to corresponding {@link fi.vincit.jmobster.processor.FieldAnnotationWriter} object. The purpose of
 *     these processors is to:
 *     <ol>
 *         <li>Write the annotation to model writer</li>
 *         <li>Provide and validate the type of a field if the type is required by a validator in the target platform</li>
 *         <li>Extract JSR-303 group information from an annotation</li>
 *         <li>Check if the processor can be used for a set of annotations</li>
 *     </ol>
 *     The processor can be set to be a base validator or non-base validator. Base validators
 *     represent an validation annotation for which it provides group and type information. Non-base
 *     validators don't provide this information, it only is used to process a set of annotations.
 *     Base validators may leave the required annotations empty. Then the processor only extracts the
 *     information from the annotation and does not write anything to model.
 * </p>
 *
 * <p>
 *    A non-base validator processor should have at least one required annotation to be set via constructors.
 *    These annotations determine whether the processor is used for a set of annotations. If it isn't set
 *    the validation processor won't be used to write the annotation to the model. Current implementation only
 *    supports one annotation per type to be stored.
 * </p>
 *
 * <p>
 *     In addition to required annotations, the processor can use optional annotations.
 *     If these optional annotations are not present for a field. The processor may still
 *     be used. If required annotations are not set, the optional annotations aren't used
 *     for anything.
 * </p>
 *
 * <p>
 *     When implementing the processing logic, the annotations to process are available via
 *     {@link BaseValidationAnnotationProcessor#findAnnotation(Class)} method. The presence
 *     of an annotation should be first checked with {@link BaseValidationAnnotationProcessor#containsAnnotation(Class)}
 *     method. For required methods this should always return true and the findAnnotation method should
 *     return a non-null value.
 * </p>
 *
 * <p>
 *     Annotation grouping is managed by implementing the
 *     {@link BaseValidationAnnotationProcessor#getGroupsInternal(java.lang.annotation.Annotation)}.
 *     This method extracts the group information of the annotation it represents. Base validation
 *     annotations must have this method implemented. For non-base validation annotations this is
 *     not called and may be left without an implementation.
 * </p>
 */
public abstract class BaseValidationAnnotationProcessor implements ValidationAnnotationProcessor {
    final private String requiredType;
    private CombinationManager combinationManager;
    private HashMap<Class, Annotation> annotationBag;
    private Class baseValidatorForClass;

    /**
     * Constructs base validator processor which isn't used for writing
     * the annotation.
     * @param baseValidatorForClass Base validator for class
     */
    protected BaseValidationAnnotationProcessor(Class baseValidatorForClass) {
        this.requiredType = null;
        setBaseValidatorForClass(baseValidatorForClass);
        this.combinationManager = new CombinationManager();
    }

    /**
     * Constructs base validator processor which isn't used for writing
     * the annotation but requires a type.
     * @param requiredType Required type
     * @param baseValidatorForClass Base validator for class
     */
    protected BaseValidationAnnotationProcessor(String requiredType, Class baseValidatorForClass) {
        this.requiredType = requiredType;
        setBaseValidatorForClass(baseValidatorForClass);
        this.combinationManager = new CombinationManager();
    }

    /**
     * Constructor for processor with required type and one or more required annotations.
     * @param requiredType Required type
     * @param requiredAnnotation Required annotations
     */
    protected BaseValidationAnnotationProcessor( String requiredType, RequiredTypes requiredAnnotation) {
        this.requiredType = requiredType;
        this.combinationManager = new CombinationManager(requiredAnnotation);
    }

    /**
     * Constructor for processor with required annotations but no type information.
     * @param requiredAnnotation Required annotations
     */
    protected BaseValidationAnnotationProcessor( RequiredTypes requiredAnnotation) {
        this.requiredType = null;
        this.combinationManager = new CombinationManager(requiredAnnotation);
    }

    /**
     * Constructor for processor with type, required annotations and optional annotations.
     * @param requiredType Type
     * @param requiredAnnotation Required annotations
     * @param optionalAnnotations Optional annotations
     */
    protected BaseValidationAnnotationProcessor( String requiredType, RequiredTypes requiredAnnotation, OptionalTypes optionalAnnotations) {
        this.requiredType = requiredType;
        this.combinationManager = new CombinationManager(requiredAnnotation, optionalAnnotations);
    }

    /**
     * Constructor for processor required annotations and optional annotations but no type information.
     * @param requiredAnnotation Required annotations
     * @param optionalAnnotations Optional annotations
     */
    protected BaseValidationAnnotationProcessor(RequiredTypes requiredAnnotation, OptionalTypes optionalAnnotations) {
        this.requiredType = null;
        this.combinationManager = new CombinationManager(requiredAnnotation, optionalAnnotations);
    }

    /**
     * Sets the class for which this processor acts as a base validator. Once
     * this method is called, the class will act as a base validator and the
     * {@link fi.vincit.jmobster.processor.defaults.BaseValidationAnnotationProcessor#isBaseValidator()}
     * method will return true.
     * @param baseValidatorForClass Class for which the processor should act as base validator
     */
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

    /**
     * Internal implementation for extracting group information
     * from an annotation. Should return groups from the annotation,
     * but if no groups are not found, it should return an empty Class array.
     * @param annotation Annotation from which the group information is extracted.
     * @return Array of groups extracted from the given annotation. If no groups, returns an empty array.
     */
    protected abstract Class[] getGroupsInternal(Annotation annotation);

    @Override
    public boolean hasGroups(Annotation annotation) {
        Class[] groups = getGroups( annotation );
        return groups != null && groups.length > 0;
    }

    @Override
    public boolean canProcess( List<Annotation> annotations ) {
        return combinationManager.matches( annotations );
    }

    /**
     * Prepares the processor for writing. Initializes the
     * annotations the processor uses so that they can be found via
     * {@link BaseValidationAnnotationProcessor#findAnnotation(Class)}.
     * @param annotations Annotations for a field.
     */
    private void prepareForWrite(List<Annotation> annotations) {
        annotationBag = new HashMap<Class, Annotation>();
        for( Annotation annotation : annotations ) {
            if( combinationManager.containsClass( annotation.annotationType() ) ) {
                annotationBag.put( annotation.annotationType(), annotation );
            }
        }
    }

    @Override
    public void writeValidatorsToStream( List<Annotation> annotations, ModelWriter writer ) {
        prepareForWrite(annotations);
        writeValidatorsToStreamInternal(writer);
        finishWrite();
    }

    /**
     * Internal implementation of writeValidatorsToStream method. When
     * this method is called the required and optional annotations are loaded
     * to internal data structure and they can be fetched by using the
     * {@link BaseValidationAnnotationProcessor#findAnnotation(Class)} method.
     * @param writer Writer to use
     */
    protected abstract void writeValidatorsToStreamInternal(ModelWriter writer);

    /**
     * Cleans up annotations from internal data structure.
     */
    private void finishWrite() {
        annotationBag.clear();
        annotationBag = null;
    }

    /**
     * Checks if the processor contains annotation of given type
     * @param clazz Annotation class
     * @param <T> Annotation type
     * @return True if processor contains annotation, otherwise false
     */
    protected <T> boolean containsAnnotation(Class<T> clazz) {
        return annotationBag.containsKey(clazz);
    }

    /**
     * Returns the annotation of given type. Always returns non-null value
     * but if the annotation is not found, throws an exception. Therefore
     * method {@link BaseValidationAnnotationProcessor#containsAnnotation(Class)}
     * should be called before this method.
     * @param clazz Annotation class
     * @param <T> Annotation type
     * @return Found annotation
     * @throws RuntimeException if annotation is not found
     */
    protected <T> T findAnnotation(Class<T> clazz) {
        if( annotationBag.containsKey(clazz) ) {
            return (T)annotationBag.get(clazz);
        } else {
            throw new RuntimeException("Annotation of type " + clazz.getName() + " not found");
        }
    }


}
