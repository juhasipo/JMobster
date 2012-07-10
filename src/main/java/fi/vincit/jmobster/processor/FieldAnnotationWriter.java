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

import fi.vincit.jmobster.util.ModelWriter;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Writes field annotations
 */
public interface FieldAnnotationWriter {
    /**
     * Checks if the given annotation is used to validate data. This
     * mostly depends on the validation processors set in the
     * provider.
     * @param annotation Annotation
     * @return True if the given annotation is for validation.
     */
    boolean isAnnotationForValidation(Annotation annotation);

    /**
     * Write the validators for a field
     * @param annotations Validation annotations
     * @param writer Model writer
     */
    void writeValidatorsForField( List<Annotation> annotations, ModelWriter writer );

    /**
     * Get base validation annotation processor for the given annotation
     * @param annotation Annotation
     * @return Base validator. If not found, returns null.
     */
    ValidationAnnotationProcessor getBaseValidationProcessor( Annotation annotation );

    /**
     * Adds annotation processor to provider.
     * @param annotationProcessor Processor to add
     */
    void addAnnotationProcessor( ValidationAnnotationProcessor annotationProcessor );
}
