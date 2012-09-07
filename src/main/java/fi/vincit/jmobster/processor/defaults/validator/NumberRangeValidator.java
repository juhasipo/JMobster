package fi.vincit.jmobster.processor.defaults.validator;

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

import fi.vincit.jmobster.util.AnnotationBag;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class NumberRangeValidator extends BaseValidator {

    private boolean hasMin;
    private boolean hasMax;
    private long min;
    private long max;

    @Override
    public void init( AnnotationBag annotationBag ) {
        if( annotationBag.hasAnnotation(Min.class) ) {
            hasMin = true;
            min = annotationBag.getAnnotation(Min.class).value();
        }
        if( annotationBag.hasAnnotation(Max.class) ) {
            hasMax = true;
            max = annotationBag.getAnnotation(Max.class).value();
        }
    }

    public boolean hasMin() {
        return hasMin;
    }

    public boolean hasMax() {
        return hasMax;
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }
}
