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

import fi.vincit.jmobster.processor.FieldAnnotationWriter;
import fi.vincit.jmobster.processor.ValidationAnnotationProcessor;
import fi.vincit.jmobster.processor.model.ModelField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Abstract base class for field annotation writers. Can be configured
 * to contain necessary annotation processors.
 */
public abstract class BaseFieldAnnotationWriter implements FieldAnnotationWriter {
    private static final Logger LOG = LoggerFactory.getLogger( BaseFieldAnnotationWriter.class );

    protected Collection<ValidationAnnotationProcessor> annotationProcessors;
    protected Map<Class, ValidationAnnotationProcessor> baseAnnotationProcessors;

    /**
     * Initializes inner data structures. Add validators use {@link BaseFieldAnnotationWriter#BaseFieldAnnotationWriter(fi.vincit.jmobster.processor.ValidationAnnotationProcessor...)}
     * constructor or {@link BaseFieldAnnotationWriter#addAnnotationProcessor(fi.vincit.jmobster.processor.ValidationAnnotationProcessor)} method.
     */
    protected BaseFieldAnnotationWriter() {
        baseAnnotationProcessors = new HashMap<Class, ValidationAnnotationProcessor>();
        annotationProcessors = new ArrayList<ValidationAnnotationProcessor>();
    }

    /**
     * Creates provider with custom set of annotation processors
     * @param processors Processors to add
     */
    protected BaseFieldAnnotationWriter( ValidationAnnotationProcessor... processors ) {
        this();
        for( ValidationAnnotationProcessor processor : processors ) {
            addAnnotationProcessor( processor );
        }
    }

    @Override
    public final void addAnnotationProcessor( ValidationAnnotationProcessor annotationProcessor ) {
        annotationProcessors.add(annotationProcessor);
        if( annotationProcessor.isBaseValidator() ) {
            baseAnnotationProcessors.put(annotationProcessor.getBaseValidatorForClass(), annotationProcessor);
        }
    }

    @Override
    public boolean isAnnotationForValidation(Annotation annotation) {
        return baseAnnotationProcessors.get(annotation.annotationType()) != null;
    }

    @Override
    public ValidationAnnotationProcessor getBaseValidationProcessor( Annotation annotation ) {
        ValidationAnnotationProcessor processor = baseAnnotationProcessors.get(annotation.annotationType());
        if( processor == null ) {
            LOG.warn("ValidationAnnotationProcessor for annotation {} not found", annotation.annotationType().getName());
        }
        return processor;
    }

    /**
     * Filter the annotations for which a suitable processor is found.
     * @param field Field with all annotations
     * @return List of annotations that are used for generating validators
     */
    protected List<ValidationAnnotationProcessor> filterProcessorsToUse( ModelField field ) {
        List<ValidationAnnotationProcessor> processorsToUse =
                new ArrayList<ValidationAnnotationProcessor>(field.getAnnotations().size());
        for( ValidationAnnotationProcessor processor : annotationProcessors ) {
            if( processor.canProcess(field) ) {
                processorsToUse.add( processor );
            }
        }
        return processorsToUse;
    }
}
