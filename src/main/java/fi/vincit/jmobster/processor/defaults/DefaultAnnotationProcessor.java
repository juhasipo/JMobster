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

import fi.vincit.jmobster.processor.AnnotationProcessor;
import fi.vincit.jmobster.processor.FieldAnnotationWriter;
import fi.vincit.jmobster.processor.GroupMode;
import fi.vincit.jmobster.processor.ValidationAnnotationProcessor;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptWriter;
import fi.vincit.jmobster.processor.model.ModelField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Default annotation processor containing the basic structure
 * for converting validation properties.
 */
public class DefaultAnnotationProcessor implements AnnotationProcessor {

    private static final Logger LOG = LoggerFactory
            .getLogger( DefaultAnnotationProcessor.class );

    private static final Class[] EMPTY_GROUPS = new Class[0];

    private FieldAnnotationWriter fieldAnnotationWriter;
    private Class[] groups;
    private GroupMode groupMode;
    private boolean includeValidationsWithoutGroup;

    public DefaultAnnotationProcessor( FieldAnnotationWriter fieldAnnotationWriter ) {
        this.fieldAnnotationWriter = fieldAnnotationWriter;
        this.groups = EMPTY_GROUPS;
        this.groupMode = GroupMode.ANY_OF_REQUIRED;
        this.includeValidationsWithoutGroup = false;
    }

    public DefaultAnnotationProcessor(FieldAnnotationWriter fieldAnnotationWriter, GroupMode groupMode, Class... groups) {
        this.fieldAnnotationWriter = fieldAnnotationWriter;
        this.groups = groups;
        this.groupMode = groupMode;
        this.includeValidationsWithoutGroup = false;
    }

    @Override
    public void writeValidation( final ModelField field, final JavaScriptWriter writer ) {
        List<Annotation> filteredAnnotations = filterByGroupRules(field.getAnnotations());
        ModelField filteredField = new ModelField(field, filteredAnnotations);
        filteredField.setDefaultValue(field.getDefaultValue());
        fieldAnnotationWriter.writeValidatorsForField( filteredField, writer );
    }

    /**
     * Filter annotations by groups. If groups are provided for this processor in constructor,
     * only the annotations that match the group rules are processed.
     * @param validationAnnotations All annotations
     * @return Filtered annotations
     */
    private List<Annotation> filterByGroupRules(List<Annotation> validationAnnotations) {
        final boolean hasAnnotationsWithGroup = findIfAnnotationsWithGroups(validationAnnotations);
        List<Annotation> annotations = new ArrayList<Annotation>();
        for( Annotation annotation : validationAnnotations ) {
            ValidationAnnotationProcessor annotationProcessor = fieldAnnotationWriter.getBaseValidationProcessor( annotation );
            if( annotationProcessor != null ) {
                if( hasAnnotationsWithGroup ) {
                    addByAnnotationGroup( annotation, annotationProcessor, annotations );
                } else {
                    annotations.add(annotation);
                }
            }
        }
        return annotations;
    }

    /**
     * Adds annotation to the given Collection if the annotation matches
     * group checks.
     * @param annotation Annotation to add
     * @param annotationProcessor Processor to use for fetching annotation groups
     * @param annotations Collection to which annotation should be added
     */
    private void addByAnnotationGroup( Annotation annotation, ValidationAnnotationProcessor annotationProcessor, Collection<Annotation> annotations ) {
        final boolean hasAnnotationGroups = annotationProcessor.hasGroups(annotation);
        final boolean shouldBeIncludedByGroups = checkGroups(annotation, annotationProcessor);
        if( hasAnnotationGroups && shouldBeIncludedByGroups ) {
            annotations.add(annotation);
        } else if( !hasAnnotationGroups && includeValidationsWithoutGroup ) {
            annotations.add(annotation);
        }
    }


    /**
     * Checks annotation groups using the current groupMode.
     * @param annotation Annotation to check
     * @param annotationProcessor Processor to use for getting groups for annotation
     * @return True if groups match according to groupMode, otherwise false.
     */
    private boolean checkGroups(Annotation annotation, ValidationAnnotationProcessor annotationProcessor) {
        Class[] groupsInAnnotation = annotationProcessor.getGroups(annotation);
        if( groupMode == GroupMode.ANY_OF_REQUIRED) {
            return checkAnyRequiredGroups( groupsInAnnotation );
        } else if( groupMode == GroupMode.AT_LEAST_REQUIRED) {
            return checkAtLeastRequiredGroups( groupsInAnnotation );
        } else if( groupMode == GroupMode.EXACTLY_REQUIRED) {
            return checkExactlyRequiredGroups( groupsInAnnotation );
        }
        return false;
    }

    private boolean checkAnyRequiredGroups( Class[] groupsInAnnotation ) {
        for( Class group : groupsInAnnotation ) {
            for( Class myGroup : this.groups ) {
                if( group.equals(myGroup) ) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkAtLeastRequiredGroups( Class[] groupsInAnnotation ) {
        final int groupsNeededCount = this.groups.length;
        if( groupsInAnnotation.length < groupsNeededCount ) {
            return false;
        }
        int groupsFoundCount = 0;
        for( Class aGroup : groupsInAnnotation ) {
            for( Class vGroup : this.groups ) {
                if( aGroup.equals(vGroup) ) {
                    ++groupsFoundCount;
                }
            }
        }
        return groupsFoundCount == groupsNeededCount;
    }

    private boolean checkExactlyRequiredGroups( Class[] groupsInAnnotation ) {
        final int groupsNeededCount = this.groups.length;
        if( groupsInAnnotation.length != groupsNeededCount ) {
            return false;
        }
        int groupsFoundCount = 0;
        for( Class aGroup : groupsInAnnotation ) {
            for( Class vGroup : this.groups ) {
                if( aGroup.equals(vGroup) ) {
                    ++groupsFoundCount;
                }
            }
        }
        return groupsFoundCount == groupsNeededCount;
    }




    private boolean findIfAnnotationsWithGroups(List<Annotation> validationAnnotations) {
        for(Annotation a : validationAnnotations) {
            ValidationAnnotationProcessor annotationProcessor = fieldAnnotationWriter.getBaseValidationProcessor( a );
            if( annotationProcessor != null && annotationProcessor.hasGroups(a) ) {
                return true;
            }
        }
        return false;
    }

    public void setIncludeValidationsWithoutGroup(boolean includeValidationsWithoutGroup) {
        this.includeValidationsWithoutGroup = includeValidationsWithoutGroup;
    }
}
