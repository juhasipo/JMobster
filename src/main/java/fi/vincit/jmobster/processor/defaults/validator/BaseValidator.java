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

import fi.vincit.jmobster.annotation.AfterInit;
import fi.vincit.jmobster.annotation.BeforeInit;
import fi.vincit.jmobster.annotation.InitMethod;
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.Optional;
import fi.vincit.jmobster.util.collection.AnnotationBag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
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
 * bag. Optional parameters can be given with {@link Optional} generic
 * parameter.
 *
 * Required parameters are always non null. Optional parameters may not
 * be present so you should check that before getting the parameter out
 * from optional object.
 *
 * You can also use annotations {@link BeforeInit} and {@link AfterInit}
 * to mark classes that should be called before and after initialization.
 * These methods don't take any parameters.
 *
 * The order in which the init methods are called is not deterministic. Only
 * the order BeforeInit, InitMethod and AfterInit method types is quaranteed.
 */
public abstract class BaseValidator implements Validator {
    private static final Logger LOG = LoggerFactory.getLogger(BaseValidator.class);
    private Class type;

    protected BaseValidator() {
        this.type = this.getClass();
    }

    public void init(AnnotationBag annotations) {
        try {
            callMethods(findMethodsWithAnnotation(BeforeInit.class));

            callInitMethods(annotations, findMethodsWithAnnotation(InitMethod.class));

            callMethods(findMethodsWithAnnotation(AfterInit.class));
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void callMethods(List<Method> methods) throws InvocationTargetException, IllegalAccessException {
        for( Method m : methods ) {
            m.invoke(this);
        }
    }

    private void callInitMethods(AnnotationBag annotations, List<Method> methods) throws InvocationTargetException, IllegalAccessException {
        for( Method m : methods ) {
            Type[] paramTypes = m.getGenericParameterTypes();
            Object[] params = new Object[paramTypes.length];


            int numberFound = collectParams(annotations, paramTypes, params);
            if( numberFound == paramTypes.length ) {
                m.invoke(this, params);
            }
        }
    }

    private static class ParamType {
        private Class type;
        private boolean isOptional;

        private ParamType(Class type, boolean optional) {
            this.type = type;
            isOptional = optional;
        }

        public Class getType() {
            return type;
        }

        public boolean isOptional() {
            return isOptional;
        }

        public Object toParameter(Annotation annotation) {
            if( isOptional ) {
                return new Optional(annotation);
            } else {
                return annotation;
            }
        }

        public boolean isOfType(Class clazz) {
            return clazz.isAssignableFrom(type);
        }
    }

    private ParamType resolveParamType(Type type) {
        Type genericType = type;
        Class<?> paramType = null;
        boolean isOptional = false;
        if (genericType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType)genericType;
            if( pt.getRawType().equals(Optional.class) ) {
                Type actualType = ((ParameterizedType) genericType).getActualTypeArguments()[0];
                paramType = castGenericToClass(actualType);
                isOptional = true;
            } else {
                throw new IllegalArgumentException("Invalid generic parameter type. Optional or Annotation expected.");
            }
        } else {
            paramType = (Class)type;
        }

        return new ParamType(paramType, isOptional);
    }

    private Class castGenericToClass(Type actualType) {
        if( actualType instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType)actualType;
            return (Class)wildcardType.getUpperBounds()[0];
        } else {
            return (Class)actualType;
        }
    }

    private int collectParams(AnnotationBag annotations, Type[] paramTypes, Object[] params) {
        int numberFound = 0;
        for( int i = 0; i < paramTypes.length; ++i ) {
            ParamType type = resolveParamType(paramTypes[i]);
            if( type.isOfType(Annotation.class) ) {
                // Now we know that the paramType is an Annotation
                // We can cast it to correct type of class type
                Class<? extends Annotation> annotationType =
                        type.getType().asSubclass(Annotation.class);

                // Only add as found, if the annotation is actually found.
                // Ensures that init methods are always called with non null values.
                Annotation annotation = annotations.getAnnotation(annotationType);
                if( annotation != null || type.isOptional ) {
                    params[i] = type.toParameter(annotation);
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
