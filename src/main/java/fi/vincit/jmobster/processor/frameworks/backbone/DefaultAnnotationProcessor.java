package fi.vincit.jmobster.processor.frameworks.backbone;
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
import fi.vincit.jmobster.processor.AnnotationProcessorProvider;
import fi.vincit.jmobster.processor.GroupMode;
import fi.vincit.jmobster.processor.ValidationAnnotationProcessor;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptWriter;
import fi.vincit.jmobster.util.ModelWriter;
import fi.vincit.jmobster.util.ItemProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Default annotation processor containing the basic structure
 * for converting Backbone.js compatible validation properties.
 */
public class DefaultAnnotationProcessor implements AnnotationProcessor {

    private static final Logger LOG = LoggerFactory
            .getLogger( DefaultAnnotationProcessor.class );
    private static final String FIELD_TYPE_START = "type: \"";
    private static final String FIELD_TYPE_END = "\",";

    private AnnotationProcessorProvider annotationProcessorProvider;
    private Class[] groups;
    private GroupMode groupMode;
    private boolean includeValidationsWithoutGroup;

    public DefaultAnnotationProcessor( AnnotationProcessorProvider annotationProcessorProvider ) {
        this.annotationProcessorProvider = annotationProcessorProvider;
        this.groups = new Class[0];
        this.groupMode = GroupMode.ANY_OF_REQUIRED;
        this.includeValidationsWithoutGroup = false;
    }

    public DefaultAnnotationProcessor(AnnotationProcessorProvider annotationProcessorProvider, GroupMode groupMode, Class... groups) {
        this.annotationProcessorProvider = annotationProcessorProvider;
        this.groups = groups;
        this.groupMode = groupMode;
        this.includeValidationsWithoutGroup = false;
    }

    private static void appendType(boolean hasType, ModelWriter sb, String type) {
        if( !hasType ) {
            sb.write( FIELD_TYPE_START ).write( type ).writeLine( FIELD_TYPE_END );
        }
    }

    @Override
    public void writeValidation( List<Annotation> validationAnnotations, final JavaScriptWriter writer ) {
        ItemProcessor<Annotation> annotationItemProcessor = new ItemProcessor<Annotation>() {
            boolean hasPropertyTypeSet = false;
            @Override
            protected void process( Annotation annotation, boolean isLast ) {
                // TODO: Don't return null if annotation processor is not found. Instead return some object that just doesn't do anything
                ValidationAnnotationProcessor annotationProcessor = annotationProcessorProvider.getValidator( annotation );
                if( annotationProcessor != null ) {
                    if( annotationProcessor.requiresType() ) {
                        appendType(hasPropertyTypeSet, writer, annotationProcessor.requiredType() );
                        hasPropertyTypeSet = true;
                    }
                    annotationProcessor.writeValidatorsToStream( annotation, writer );
                    writer.writeLine("", ",", !isLast);
                } else {
                    LOG.debug("No validator processor found");
                }
            }
        };

        List<Annotation> filteredAnnotations = filterByGroupRules(validationAnnotations);
        annotationItemProcessor.process(filteredAnnotations);
    }

    private List<Annotation> filterByGroupRules(List<Annotation> validationAnnotations) {
        final boolean hasAnnotationsWithGroup = findIfAnnotationsWithGroups(validationAnnotations);
        List<Annotation> annotations = new ArrayList<Annotation>();
        for( Annotation annotation : validationAnnotations ) {
            ValidationAnnotationProcessor annotationProcessor = annotationProcessorProvider.getValidator( annotation );
            if( annotationProcessor != null ) {
                if( hasAnnotationsWithGroup ) {
                    final boolean hasAnnotationGroups = annotationProcessor.hasGroups(annotation);
                    final boolean shouldBeIncludedByGroups = checkGroups(annotation, annotationProcessor);
                    if( hasAnnotationGroups && shouldBeIncludedByGroups ) {
                        annotations.add(annotation);
                    } else if( !hasAnnotationGroups && includeValidationsWithoutGroup ) {
                        annotations.add(annotation);
                    }
                } else {
                    annotations.add(annotation);
                }
            }
        }
        return annotations;
    }

    private boolean checkGroups(Annotation annotation, ValidationAnnotationProcessor annotationProcessor) {
        Class[] groupsInAnnotation = annotationProcessor.getGroups(annotation);
        if( groupMode == GroupMode.ANY_OF_REQUIRED) {
            for( Class group : groupsInAnnotation ) {
                for( Class myGroup : this.groups ) {
                    if( group.equals(myGroup) ) {
                        return true;
                    }
                }
            }
        } else if( groupMode == GroupMode.AT_LEAST_REQUIRED) {
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
        } else if( groupMode == GroupMode.EXACTLY_REQUIRED) {
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
        return false;
    }

    private boolean findIfAnnotationsWithGroups(List<Annotation> validationAnnotations) {
        for(Annotation a : validationAnnotations) {
            ValidationAnnotationProcessor annotationProcessor = annotationProcessorProvider.getValidator( a );
            if( annotationProcessor != null && annotationProcessor.hasGroups(a) ) {
                return true;
            }
        }
        return false;
    }

    public AnnotationProcessorProvider getAnnotationProcessorProvider() {
        return annotationProcessorProvider;
    }

    public void setIncludeValidationsWithoutGroup(boolean includeValidationsWithoutGroup) {
        this.includeValidationsWithoutGroup = includeValidationsWithoutGroup;
    }
}
