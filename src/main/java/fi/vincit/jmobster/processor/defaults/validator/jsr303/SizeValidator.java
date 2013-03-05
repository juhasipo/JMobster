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

import fi.vincit.jmobster.annotation.InitMethod;
import fi.vincit.jmobster.processor.defaults.validator.BaseValidator;

import javax.validation.constraints.Size;

public class SizeValidator extends BaseValidator {
    private int min;
    private int max;
    private boolean hasMin;
    private boolean hasMax;

    @InitMethod
    public void init(Size size) {
        this.min = size.min();
        this.max = size.max();
        this.hasMin = min >= 0;
        this.hasMax = max < Integer.MAX_VALUE;
    }

    public int getMin() {
        return min;
    }
    public int getMax() {
        return max;
    }

    public boolean hasMin() {
        return hasMin;
    }

    public boolean hasMax() {
        return hasMax;
    }
}
