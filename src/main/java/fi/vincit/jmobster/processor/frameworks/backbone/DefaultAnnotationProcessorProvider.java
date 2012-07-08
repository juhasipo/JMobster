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

import fi.vincit.jmobster.processor.AnnotationProcessorProvider;
import fi.vincit.jmobster.processor.CombinationManager;
import fi.vincit.jmobster.processor.ValidationAnnotationProcessor;
import fi.vincit.jmobster.processor.frameworks.backbone.annotation.*;
import fi.vincit.jmobster.util.ItemProcessor;
import fi.vincit.jmobster.util.ModelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * The class will return a suitable annotation processor for the
 * given type of JSR-303 validation annotation. The class can
 * be extended to support custom validation annotation. For now
 * only single property annotations are supported.
 */
public class DefaultAnnotationProcessorProvider implements AnnotationProcessorProvider {
    private static final Logger LOG = LoggerFactory
            .getLogger( BackboneModelProcessor.class );

    private Collection<ValidationAnnotationProcessor> annotationProcessors;
    private Map<Class, ValidationAnnotationProcessor> baseAnnotationProcessors;

    protected void addValidator( ValidationAnnotationProcessor annotationProcessor ) {
        annotationProcessors.add(annotationProcessor);
        if( annotationProcessor.isBaseValidator() ) {
            baseAnnotationProcessors.put(annotationProcessor.getBaseValidatorForClass(), annotationProcessor);
        }
    }

    public DefaultAnnotationProcessorProvider() {
        this( new NotNullAnnotationProcessor(),
                new SizeAnnotationProcessor(),
                new MinAnnotationProcessor(),
                new MaxAnnotationProcessor(),
                new PatternAnnotationProcessor()
        );
    }

    public DefaultAnnotationProcessorProvider(ValidationAnnotationProcessor... processors) {
        // TODO FIX ME: Support concurrency. Can't have instances created here because BaseValidationAnnotationProcessor have state
        annotationProcessors = new ArrayList<ValidationAnnotationProcessor>();
        baseAnnotationProcessors = new HashMap<Class, ValidationAnnotationProcessor>();
        for( ValidationAnnotationProcessor processor : processors ) {
            addValidator(processor);
        }
    }

    @Override
    public boolean isAnnotationForValidation(Annotation annotation) {
        return baseAnnotationProcessors.get(annotation.annotationType()) != null;
    }

    private static final String FIELD_TYPE_START = "type: \"";
    private static final String FIELD_TYPE_END = "\",";

    private void appendType(boolean hasType, ModelWriter writer, String type) {
        if( !hasType ) {
            writer.write( FIELD_TYPE_START ).write( type ).writeLine( FIELD_TYPE_END );
        }
    }

    @Override
    public void process( final List<Annotation> annotations, final ModelWriter writer ) {
        writeTypeInfo( annotations, writer );

        // First find processors that actually do somethings so that we can
        // add commas to right places later
        List<ValidationAnnotationProcessor> processorsToUse = filterProcessorsToUse( annotations );
        ItemProcessor<ValidationAnnotationProcessor> processor = new ItemProcessor<ValidationAnnotationProcessor>() {
            @Override
            protected void process( ValidationAnnotationProcessor processor, boolean isLast ) {
                processor.writeValidatorsToStream( annotations, writer );
                writer.writeLine("", ",", !isLast);
            }
        };
        processor.process( processorsToUse );
    }

    private List<ValidationAnnotationProcessor> filterProcessorsToUse( List<Annotation> annotations ) {
        List<ValidationAnnotationProcessor> processorsToUse =
                new ArrayList<ValidationAnnotationProcessor>(annotations.size());
        for( ValidationAnnotationProcessor processor : annotationProcessors ) {
            if( processor.canProcess(annotations) ) {
                processorsToUse.add( processor );
            }
        }
        return processorsToUse;
    }

    private void writeTypeInfo( List<Annotation> annotations, ModelWriter writer ) {
        String typeRequired = null;
        boolean typeWritten = false;
        for( ValidationAnnotationProcessor processor : annotationProcessors ) {
            if( processor.canProcess(annotations) ) {
                if( processor.requiresType() ) {
                    if( typeWritten ) {
                        if( !typeRequired.equals(processor.requiredType()) ) {
                            LOG.warn("Field already has a type: {}  but another annotation requires type: {}",
                                        typeRequired,
                                        processor.requiredType());
                        }
                    } else {
                        typeRequired = processor.requiredType();
                        typeWritten = true;
                        appendType(false, writer, typeRequired);
                    }
                }
            }
        }
    }

    @Override
    public ValidationAnnotationProcessor getValidator( Annotation annotation ) {
        return baseAnnotationProcessors.get(annotation.annotationType());
    }
}
