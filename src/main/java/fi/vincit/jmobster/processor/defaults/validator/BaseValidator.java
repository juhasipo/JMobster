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
 */
public abstract class BaseValidator implements Validator {
    private static final Logger LOG = LoggerFactory.getLogger(BaseValidator.class);
    private Class type;

    protected BaseValidator() {
        this.type = this.getClass();
    }

    public void init(AnnotationBag annotations) {
        List<Method> methods = findInitMethods();

        // TODO: BeforeInit
        // TODO: Init via reflection
        for( Method m : methods ) {
            Class[] paramTypes = m.getParameterTypes();
            Annotation[] params = new Annotation[paramTypes.length];

            int numberFound = getParams(annotations, paramTypes, params);
            if( numberFound == paramTypes.length ) {
                try {
                    m.invoke(this, params);
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        // TODO: AfterInit
    }

    private int getParams(AnnotationBag annotations, Class[] paramTypes, Annotation[] params) {
        int numberFound = 0;
        for( int i = 0; i < paramTypes.length; ++i ) {
            Class<?> paramType = paramTypes[i];
            if( Annotation.class.isAssignableFrom(paramType) ) {
                // Now we know that the paramType is an Annotation
                Class<? extends Annotation> annotationType = paramType.asSubclass(Annotation.class);
                Annotation annotation = annotations.getAnnotation(annotationType);
                if( annotation != null ) {
                    params[i] = annotation;
                    ++numberFound;
                }
            }
        }
        return numberFound;
    }

    private List<Method> findInitMethods() {
        List<Method> initMethods = new ArrayList<Method>();
        for( Method m : type.getMethods() ) {
            if( m.isAnnotationPresent(InitMethod.class) ) {
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
