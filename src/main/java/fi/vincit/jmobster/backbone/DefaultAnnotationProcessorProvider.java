package fi.vincit.jmobster.backbone;
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
import fi.vincit.jmobster.backbone.annotation.*;

import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * The class will return a suitable annotation processor for the
 * given type of JSR-303 validation annotation. The class can
 * be extended to support custom validation annotation. For now
 * only single property annotations are supported.
 */
public class DefaultAnnotationProcessorProvider implements AnnotationProcessorProvider {
    private static Map<Class, ValidationAnnotationProcessor> annotationProcessors;
    static {
        annotationProcessors = new HashMap<Class, ValidationAnnotationProcessor>();
        addValidator( NotNull.class, new NotNullAnnotationProcessor() );
        addValidator( Size.class, new SizeAnnotationProcessor() );
        addValidator( Min.class, new MinAnnotationProcessor() );
        addValidator( Max.class, new MaxAnnotationProcessor() );
        addValidator( Pattern.class, new PatternAnnotationProcessor() );
    }

    protected static void addValidator( Class clazz, ValidationAnnotationProcessor vap ) {
        annotationProcessors.put(clazz, vap);
    }

    @Override
    public boolean isAnnotationForValidation(Annotation annotation) {
        return annotationProcessors.containsKey(annotation.annotationType());
    }

    /**
     * Returns the best annotation processor for the given annotation type.
     * @param annotation Annotation for which the validator should be returned.
     * @return Annotation processor if found, otherwise null.
     */
    @Override
    public ValidationAnnotationProcessor getValidator( Annotation annotation ) {
        return annotationProcessors.get(annotation.annotationType());
    }
}
