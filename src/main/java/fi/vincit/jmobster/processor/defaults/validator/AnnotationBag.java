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

import fi.vincit.jmobster.processor.model.HasGroups;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class AnnotationBag implements HasGroups {
    final private Map<Class, Annotation> annotations = new HashMap<Class, Annotation>();

    public <T extends Annotation> T getAnnotation(Class<T> clazz) {
        if( annotations.containsKey(clazz) ) {
            Annotation annotation = annotations.get(clazz);
            return (T)annotation;
        } else {
            return null;
        }
    }

    public <T extends Annotation> boolean hasAnnotation(Class<T> clazz) {
        return annotations.containsKey(clazz);
    }

    public void addAnnotation(Annotation annotation) {
        annotations.put(annotation.annotationType(), annotation);
    }

    @Override
    public Class[] getGroups() {
        return new Class[0];
    }

    @Override
    public boolean hasGroups() {
        return false;
    }
}
