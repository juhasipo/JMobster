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

import fi.vincit.jmobster.exception.InvalidType;

public abstract class BaseValidationAnnotationProcessor implements ValidationAnnotationProcessor {
    private String requiredType;

    public BaseValidationAnnotationProcessor() {
    }

    public BaseValidationAnnotationProcessor( String requiredType ) {
        this.requiredType = requiredType;
    }

    @Override
    public String requiredType() {
        return requiredType;
    }

    @Override
    public boolean requiresType() {
        return requiredType() != null;
    }

    @Override
    public void validateType( String type ) {
        if( requiresType() ) {
            if( !requiredType().equals(type) ) {
                throw new InvalidType("Required type <" + requiredType() + "> given type <" + type + ">");
            }
        }
    }

}
