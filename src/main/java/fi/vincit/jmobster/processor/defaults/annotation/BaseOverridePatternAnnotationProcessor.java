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

import java.lang.annotation.Annotation;

/**
 * Base class for OverridePattern annotation. Handles group extraction for the annotation.
 */
public abstract class BaseOverridePatternAnnotationProcessor extends BaseValidationAnnotationProcessor {
    public BaseOverridePatternAnnotationProcessor( Class baseValidatorForClass ) {
        super( baseValidatorForClass );
    }

    @Override
    protected Class[] getGroupsInternal( Annotation annotation ) {
        return ((OverridePattern)annotation).groups();
    }
}
