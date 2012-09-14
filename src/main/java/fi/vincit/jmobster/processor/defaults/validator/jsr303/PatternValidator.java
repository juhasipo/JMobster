package fi.vincit.jmobster.processor.defaults.validator.jsr303;

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
import fi.vincit.jmobster.processor.defaults.validator.BaseValidator;
import fi.vincit.jmobster.util.collection.AnnotationBag;

import javax.validation.constraints.Pattern;

public class PatternValidator extends BaseValidator {
    private String regexp;
    private Pattern.Flag[] flags;

    @Override
    public void init( AnnotationBag annotationBag ) {
        Pattern pattern = annotationBag.getAnnotation(Pattern.class);
        this.flags = pattern.flags();

        if( annotationBag.hasAnnotation( OverridePattern.class ) ) {
            OverridePattern overriddenPattern = annotationBag.getAnnotation(OverridePattern.class);
            regexp = overriddenPattern.regexp();
        } else {
            regexp = pattern.regexp();
        }
    }

    public String getRegexp() {
        return regexp;
    }

    public Pattern.Flag[] getFlags() {
        return flags;
    }
}
