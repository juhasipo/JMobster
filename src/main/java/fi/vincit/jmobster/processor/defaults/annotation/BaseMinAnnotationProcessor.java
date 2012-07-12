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
import fi.vincit.jmobster.util.RequiredTypes;

import javax.validation.constraints.Min;
import java.lang.annotation.Annotation;

/**
 * Base class for JSR-303 Min annotation. Handles group extraction for the annotation.
 */
public abstract class BaseMinAnnotationProcessor extends BaseValidationAnnotationProcessor {
    public BaseMinAnnotationProcessor( String requiredType, RequiredTypes requiredAnnotation ) {
        super( requiredType, requiredAnnotation );
    }

    @Override
    public Class[] getGroupsInternal(Annotation annotation) {
        return ((Min)annotation).groups();
    }
}
