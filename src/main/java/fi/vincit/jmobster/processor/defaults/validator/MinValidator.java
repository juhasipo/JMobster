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

import javax.validation.constraints.Min;

public class MinValidator extends BaseValidator {
    private long min;

    @Override
    public void init( AnnotationBag annotationBag ) {
        Min maxValue = annotationBag.getAnnotation(Min.class);
        this.min = maxValue.value();
    }

    public long getMin() {
        return min;
    }
}
