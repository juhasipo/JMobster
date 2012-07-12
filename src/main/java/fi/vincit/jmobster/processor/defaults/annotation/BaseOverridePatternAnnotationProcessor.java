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

import fi.vincit.jmobster.annotation.OverridePattern;
import fi.vincit.jmobster.processor.defaults.BaseValidationAnnotationProcessor;
import fi.vincit.jmobster.util.OptionalTypes;
import fi.vincit.jmobster.util.RequiredTypes;

import java.lang.annotation.Annotation;

/**
 * Base class for OverridePattern annotation. Handles group extraction for the annotation.
 */
public abstract class BaseOverridePatternAnnotationProcessor extends BaseValidationAnnotationProcessor {
    protected BaseOverridePatternAnnotationProcessor( Class baseValidatorForClass ) {
        super( baseValidatorForClass );
    }

    protected BaseOverridePatternAnnotationProcessor( String requiredType, Class baseValidatorForClass ) {
        super( requiredType, baseValidatorForClass );
    }

    protected BaseOverridePatternAnnotationProcessor( String requiredType, RequiredTypes requiredAnnotation ) {
        super( requiredType, requiredAnnotation );
    }

    protected BaseOverridePatternAnnotationProcessor( RequiredTypes requiredAnnotation ) {
        super( requiredAnnotation );
    }

    protected BaseOverridePatternAnnotationProcessor( String requiredType, RequiredTypes requiredAnnotation, OptionalTypes optionalAnnotations ) {
        super( requiredType, requiredAnnotation, optionalAnnotations );
    }

    protected BaseOverridePatternAnnotationProcessor( RequiredTypes requiredAnnotation, OptionalTypes optionalAnnotations ) {
        super( requiredAnnotation, optionalAnnotations );
    }

    @Override
    protected Class[] getGroupsInternal( Annotation annotation ) {
        return ((OverridePattern)annotation).groups();
    }
}
