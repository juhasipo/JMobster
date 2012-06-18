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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Single model field that is converted
 * to the target platform.
 */
public class ModelField {
    private Field field;
    private List<Annotation> annotations;
    private String defaultValue;

    public ModelField( Field field, List<Annotation> validationAnnotations ) {
        this.field = field;
        this.annotations = validationAnnotations;
    }

    public Field getField() {
        return field;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue( String defaultValue ) {
        this.defaultValue = defaultValue;
    }

    /**
     *
     * @return True if the model field contains one or more validation annotations.
     */
    public boolean hasValidations() {
        return !annotations.isEmpty();
    }
}
