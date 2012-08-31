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

import fi.vincit.jmobster.processor.model.Validator;

/**
 * Base class for implementing validators. By default sets
 * the validator type with {@link Object#getClass()}. This can
 * be overridden with {@link BaseValidator#setType(Class)}
 */
public abstract class BaseValidator implements Validator {
    private Class type;

    protected BaseValidator() {
        this.type = this.getClass();
    }

    @Override
    public Class getType() {
        return type;
    }

    protected void setType(Class type) {
        this.type = type;
    }
}
