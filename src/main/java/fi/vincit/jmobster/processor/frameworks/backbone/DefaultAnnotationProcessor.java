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
import fi.vincit.jmobster.processor.ValidationAnnotationProcessor;
import fi.vincit.jmobster.util.ModelWriter;
import fi.vincit.jmobster.util.ItemProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
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

    public DefaultAnnotationProcessor( AnnotationProcessorProvider annotationProcessorProvider ) {
        this.annotationProcessorProvider = annotationProcessorProvider;
    }

    private static void appendType(boolean hasType, ModelWriter sb, String type) {
        if( !hasType ) {
            sb.write( FIELD_TYPE_START ).write( type ).writeLine( FIELD_TYPE_END );
        }
    }

    @Override
    public void writeValidation( List<Annotation> validationAnnotations, final ModelWriter writer ) {
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
        annotationItemProcessor.process(validationAnnotations);
    }

    public AnnotationProcessorProvider getAnnotationProcessorProvider() {
        return annotationProcessorProvider;
    }
}