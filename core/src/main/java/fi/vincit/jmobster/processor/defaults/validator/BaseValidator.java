package fi.vincit.jmobster.processor.defaults.validator;

/*
 * Copyright 2012-2013 Juha Siponen
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
import fi.vincit.jmobster.util.reflection.CastUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

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
 * If the validator doesn't require any parameters the required validators
 * can be given to the class with {@link fi.vincit.jmobster.annotation.RequiresAnnotations}
 * annotation.
 *
 * It is important to give either init method parameters or
 * {@link fi.vincit.jmobster.annotation.RequiresAnnotations} annotation.
 * Otherwise the validator won't appear since it can't be associated
 * to any validator.
 *
 * An annotation is used instead of constructor because this way allows
 * the BaseValidator class to initialize the object in constructor and
 * the user doesn't have to call super() in each constructor. Forgetting
 * to make that call in the constructor would cause strange bugs so using
 * annotated methods is safer way.
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
            Map<Class<? extends Annotation>, Set<Method>> methodsByAnnotation = sortMethodsByAnnotations();

            callMethods(findMethodsWithAnnotation(methodsByAnnotation, BeforeInit.class));

            callInitMethods(annotations, findMethodsWithAnnotation(methodsByAnnotation, InitMethod.class));

            callMethods(findMethodsWithAnnotation(methodsByAnnotation, AfterInit.class));
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method improves performance significantly. With 1000 classes
     * findMethodsWithAnnotation invocation time dropped from 450 ms to 25 ms
     */
    private Map<Class<? extends Annotation>, Set<Method>> sortMethodsByAnnotations() {
        Map<Class<? extends Annotation>, Set<Method>> methodsByAnnotation =
                new HashMap<Class<? extends Annotation>, Set<Method>>();

        for( Method method : type.getMethods() ) {
            for( Annotation annotation : method.getAnnotations() ) {
                final Class<? extends Annotation> annotationType =
                        annotation.annotationType();

                if( !methodsByAnnotation.containsKey(annotationType) ) {
                    methodsByAnnotation.put(annotationType, new HashSet<Method>());
                }
                methodsByAnnotation.get(annotationType).add(method);
            }
        }

        return methodsByAnnotation;
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

    private int collectParams(AnnotationBag annotations, Type[] paramTypes, Object[] params) {
        int numberFound = 0;
        for( int i = 0; i < paramTypes.length; ++i ) {
            CastUtil.ParamType type = CastUtil.resolveParamType(paramTypes[i]);
            if( type.isOfType(Annotation.class) ) {
                // Now we know that the paramType is an Annotation
                // We can cast it to correct type of class type
                Class<? extends Annotation> annotationType =
                        type.getType().asSubclass(Annotation.class);

                // Only add as found, if the annotation is actually found.
                // Ensures that init methods are always called with non null values.
                Annotation annotation = annotations.getAnnotation(annotationType);
                if( annotation != null || type.isOptional() ) {
                    params[i] = type.toParameter(annotation);
                    ++numberFound;
                }
            }
        }
        return numberFound;
    }

    private List<Method> findMethodsWithAnnotation(
                Map<Class<? extends Annotation>,
                Set<Method>> methodsByAnnotation, Class<? extends Annotation> methodAnnotation) {
        if( methodsByAnnotation.containsKey(methodAnnotation) ) {
            Set<Method> methods = methodsByAnnotation.get(methodAnnotation);
            List<Method> initMethods = new ArrayList<Method>();
            initMethods.addAll(methods);
            return initMethods;
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public Class getType() {
        return type;
    }

    protected void setType(Class type) {
        this.type = type;
    }

}
