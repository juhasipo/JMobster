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

import fi.vincit.jmobster.processor.ValidationAnnotationProcessor;

import java.lang.annotation.Annotation;

/**
 * Provides annotation processors
 */
public interface AnnotationProcessorProvider {
    /**
     * Returns the most suitable annotation processor for the given
     * annotation
     * @param annotation Annotation
     * @return Best suitable annotation processor. Null if no processor
     *         suits for the given annotation.
     */
    ValidationAnnotationProcessor getValidator( Annotation annotation );

    /**
     * Checks if the given annotation is used to validate data. This
     * mostly depends on the validation processors set in the
     * provider.
     * @param annotation Annotation
     * @return True if the given annotation is for validation.
     */
    boolean isAnnotationForValidation(Annotation annotation);
}
