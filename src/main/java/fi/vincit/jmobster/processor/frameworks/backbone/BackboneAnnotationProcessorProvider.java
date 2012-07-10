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

import fi.vincit.jmobster.processor.ValidationAnnotationProcessor;
import fi.vincit.jmobster.processor.defaults.BaseAnnotationProcessorProvider;
import fi.vincit.jmobster.processor.frameworks.backbone.annotation.*;
import fi.vincit.jmobster.util.ItemProcessor;
import fi.vincit.jmobster.util.ModelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * The class will return a suitable annotation processor for the
 * given type of JSR-303 validation annotation. The class can
 * be extended to support custom validation annotation. For now
 * only single property annotations are supported.
 */
public class BackboneAnnotationProcessorProvider extends BaseAnnotationProcessorProvider {

    private static final Logger LOG = LoggerFactory.getLogger( BackboneAnnotationProcessorProvider.class );

    private String requiredType = null;
    private boolean typeInfoWritten = false;

    private static final String ANNOTATION_SEPARATOR = ",";
    private static final String FIELD_TYPE_START = "type: \"";
    private static final String FIELD_TYPE_END = "\",";

    /**
     * Creates provider with default JSR-303 of annotation processors
     */
    public BackboneAnnotationProcessorProvider() {
        // TODO FIX ME: Support concurrency. Can't have instances created here because BaseValidationAnnotationProcessor have state
        super( new NotNullAnnotationProcessor(),
                new SizeAnnotationProcessor(),
                new MinAnnotationProcessor(),
                new MaxAnnotationProcessor(),
                new PatternAnnotationProcessor()
        );
    }

    public BackboneAnnotationProcessorProvider( ValidationAnnotationProcessor... processors ) {
        super(processors);
    }

    @Override
    public void writeValidatorsForField( final List<Annotation> annotations, final ModelWriter writer ) {
        writeTypeForField( annotations, writer );

        // First find processors that actually do somethings so that we can
        // add commas to right places in the item processor
        List<ValidationAnnotationProcessor> processorsToUse = filterProcessorsToUse( annotations );

        ItemProcessor<ValidationAnnotationProcessor> processor = new ItemProcessor<ValidationAnnotationProcessor>() {
            @Override
            protected void process( ValidationAnnotationProcessor processor, boolean isLast ) {
                processor.writeValidatorsToStream( annotations, writer );
                writer.writeLine("", ANNOTATION_SEPARATOR, !isLast);
            }
        };
        processor.process( processorsToUse );
    }

    /**
     * Write type information for field
     * @param annotations Annotations for the field
     * @param writer Model writer
     */
    private void writeTypeForField( List<Annotation> annotations, ModelWriter writer ) {
        for( ValidationAnnotationProcessor processor : annotationProcessors ) {
            if( processor.canProcess(annotations) ) {
                writeTypeForAnnotation( processor, writer );
            }
        }
    }

    /**
     * Writes type information for a single annotation. Only writes the
     * information if the field doesn't have type. If the field already has
     * a type, it ignores the other types.
     * @param processor Processor to use for type information extraction
     * @param writer Model writer
     */
    private void writeTypeForAnnotation( ValidationAnnotationProcessor processor, ModelWriter writer ) {
        if( processor.requiresType() ) {
            if( typeInfoWritten ) {
                if( !requiredType.equals(processor.requiredType()) ) {
                    LOG.warn("Field already has a type: {}  but another annotation requires type: {}",
                            requiredType,
                                processor.requiredType());
                }
            } else {
                requiredType = processor.requiredType();
                typeInfoWritten = true;
                writer.write( BackboneAnnotationProcessorProvider.FIELD_TYPE_START )
                        .write( requiredType )
                        .writeLine( BackboneAnnotationProcessorProvider.FIELD_TYPE_END );
            }
        }
    }

}
