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

import fi.vincit.jmobster.annotation.InitMethod;
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.collection.AnnotationBag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for implementing validators. By default sets
 * the validator type with {@link Object#getClass()}. This can
 * be overridden with {@link BaseValidator#setType(Class)}
 *
 * To initialize the subclass with correct validation data, use
 * {@link InitMethod} annotation and create an init method. The name
 * of the method doesn't matter. The only thing that matters is that
 * the method parameters exist. The init method is only called when
 * every required method parameter is found from the given annotation
 * bag.
 */
public abstract class BaseValidator implements Validator {
    private static final Logger LOG = LoggerFactory.getLogger(BaseValidator.class);
    private Class type;

    protected BaseValidator() {
        this.type = this.getClass();
    }

    public void init(AnnotationBag annotations) {
        // TODO: BeforeInit
        callInitMethods(annotations, findMethodsWithAnnotation(InitMethod.class));
        // TODO: AfterInit
    }

    private void callMethods(List<Method> methods, Object... params) throws Exception {
        for( Method m : methods ) {
            m.invoke(this, params);
        }
    }

    private void callInitMethods(AnnotationBag annotations, List<Method> methods) {
        for( Method m : methods ) {
            Class[] paramTypes = m.getParameterTypes();
            Annotation[] params = new Annotation[paramTypes.length];

            int numberFound = collectParams(annotations, paramTypes, params);
            if( numberFound == paramTypes.length ) {
                try {
                    m.invoke(this, params);
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }

    private int collectParams(AnnotationBag annotations, Class[] paramTypes, Annotation[] params) {
        int numberFound = 0;
        for( int i = 0; i < paramTypes.length; ++i ) {
            Class<?> paramType = paramTypes[i];
            if( Annotation.class.isAssignableFrom(paramType) ) {
                // Now we know that the paramType is an Annotation
                // We can cast it to correct type of class type
                Class<? extends Annotation> annotationType = paramType.asSubclass(Annotation.class);

                // Only add as found, if the annotation is actually found.
                // Ensures that init methods are always called with non null values.
                Annotation annotation = annotations.getAnnotation(annotationType);
                if( annotation != null ) {
                    params[i] = annotation;
                    ++numberFound;
                }
            }
        }
        return numberFound;
    }

    private List<Method> findMethodsWithAnnotation(Class<? extends Annotation> methodAnnotation) {
        List<Method> initMethods = new ArrayList<Method>();
        for( Method m : type.getMethods() ) {
            if( m.isAnnotationPresent(methodAnnotation) ) {
                initMethods.add(m);
            }
        }
        return initMethods;
    }

    @Override
    public Class getType() {
        return type;
    }

    protected void setType(Class type) {
        this.type = type;
    }
}
