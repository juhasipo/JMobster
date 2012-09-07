package fi.vincit.jmobster.util;

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

import fi.vincit.jmobster.processor.model.FieldAnnotation;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * A bag of annotations. You can ask the object if it contains a certain
 * type of annotation and ask it to return an instance of that annotation.
 */
public class AnnotationBag {
    final private Map<Class, FieldAnnotation> annotations = new HashMap<Class, FieldAnnotation>();

    public <T extends Annotation> T getAnnotation(Class<T> clazz) {
        if( annotations.containsKey(clazz) ) {
            FieldAnnotation annotation = annotations.get(clazz);
            return (T)annotation.getAnnotation();
        } else {
            return null;
        }
    }

    /**
     * Checks if the bag contains an annotation of given type
     * @param annotationType Annotation type (Class of annotation)
     * @param <T> Annotation type
     * @return True if the bag contains the annotation, otherwise false.
     */
    public <T extends Annotation> boolean hasAnnotation(Class<T> annotationType) {
        return annotations.containsKey(annotationType);
    }

    /**
     * Add a new annotation to bag
     * @param annotation Annotation to add
     */
    public void addAnnotation(FieldAnnotation annotation) {
        annotations.put(annotation.getType(), annotation);
    }

    public boolean hasAnnotations() {
        return !annotations.isEmpty();
    }
}
