package fi.vincit.jmobster.processor.defaults.annotation;

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

import fi.vincit.jmobster.processor.defaults.BaseValidationAnnotationProcessor;
import fi.vincit.jmobster.util.OptionalTypes;
import fi.vincit.jmobster.util.RequiredTypes;

import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;

/**
 * Base class for JSR-303 NotNull annotation. Handles group extraction for the annotation.
 */
public abstract class BaseNotNullAnnotationProcessor extends BaseValidationAnnotationProcessor {
    protected BaseNotNullAnnotationProcessor( RequiredTypes requiredAnnotation ) {
        super( requiredAnnotation );
    }

    protected BaseNotNullAnnotationProcessor( String requiredType, RequiredTypes requiredAnnotation, OptionalTypes optionalAnnotations ) {
        super( requiredType, requiredAnnotation, optionalAnnotations );
    }

    protected BaseNotNullAnnotationProcessor( RequiredTypes requiredAnnotation, OptionalTypes optionalAnnotations ) {
        super( requiredAnnotation, optionalAnnotations );
    }

    protected BaseNotNullAnnotationProcessor( Class baseValidatorForClass ) {
        super( baseValidatorForClass );
    }

    protected BaseNotNullAnnotationProcessor( String requiredType, Class baseValidatorForClass ) {
        super( requiredType, baseValidatorForClass );
    }

    protected BaseNotNullAnnotationProcessor( String requiredType, RequiredTypes requiredAnnotation ) {
        super( requiredType, requiredAnnotation );
    }

    @Override
    public Class[] getGroupsInternal(Annotation annotation) {
        return ((NotNull)annotation).groups();
    }
}