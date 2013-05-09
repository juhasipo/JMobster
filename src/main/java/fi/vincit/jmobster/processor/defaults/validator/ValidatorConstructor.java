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

import fi.vincit.jmobster.annotation.InitMethod;
import fi.vincit.jmobster.annotation.RequiresAnnotations;
import fi.vincit.jmobster.processor.model.FieldAnnotation;
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.collection.AnnotationBag;
import fi.vincit.jmobster.util.combination.CombinationManager;
import fi.vincit.jmobster.util.combination.OptionalTypes;
import fi.vincit.jmobster.util.combination.RequiredTypes;
import fi.vincit.jmobster.util.reflection.CastUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *     Constructs validator instance of configured type. The class will
 * initialize the constructed validator with correct parameters. The required and
 * optional parameters are determined from {@link RequiresAnnotations} annotation
 * and init method parameters.
 * </p>
 * <p>
 *     If required annotations are given, the constructor constructs the validator
 * only if all required annotations are found. Optional annotations are then added
 * if found.
 * </p>
 * <p>
 *     If no required annotations are given, the constructor constructs the validator
 * if at least one optional validator is found. All optional annotations are then added
 * like when required annotations would be given.
 * </p>
 */
public class ValidatorConstructor {
    private static final Logger LOG = LoggerFactory.getLogger( ValidatorConstructor.class );

    private final CombinationManager<FieldAnnotation> combinationManager;
    private final Class validatorClass;

    /**
     * Configures the validator constructor. Validators the ValidatorConstructor
     * creates, must be extended from {@link fi.vincit.jmobster.processor.defaults.validator.BaseValidator}
     * class so that the validator can be initialized correctly.
     * @param validatorClass Type of validator this constructs (Validator class e.g. SizeValidator.class)
     */
    public ValidatorConstructor(Class validatorClass) {
        RequiredTypes requiredTypes = findRequiredTypes(validatorClass);
        OptionalTypes optionalTypes = findOptionalTypes(validatorClass);
        this.combinationManager = new CombinationManager<FieldAnnotation>(requiredTypes, optionalTypes);
        this.validatorClass = validatorClass;
    }

    private OptionalTypes findOptionalTypes(Class validatorClass) {
        return OptionalTypes.get(findTypes(validatorClass, false));
    }

    private RequiredTypes findRequiredTypes(Class validatorClass) {
        return RequiredTypes.get(findTypes(validatorClass, true));
    }
    
    Class[] findTypes(Class validatorClass, boolean required) {
        List<Method> methods = findInitMethods(validatorClass);
        List<Class> types = new ArrayList<Class>();
        resolveParameters(required, methods, types);
        resolveAnnotationParameters(validatorClass, types);
        return types.toArray(new Class[types.size()]);
    }

    private void resolveAnnotationParameters(Class validatorClass, List<Class> types) {
        RequiresAnnotations requiredAnnotation = (RequiresAnnotations)validatorClass.getAnnotation(RequiresAnnotations.class);
        if( requiredAnnotation != null ) {
            Collections.addAll(types, requiredAnnotation.value());
        }
    }

    private List<Class> resolveParameters(boolean required, List<Method> methods, List<Class> types) {
        for( Method method : methods ) {
            Type[] parameters = method.getGenericParameterTypes();
            for( Type parameter : parameters ) {
                CastUtil.ParamType resolvedType = CastUtil.resolveParamType(parameter);
                Class type = resolvedType.getType();
                if( !resolvedType.isOptional() && required ) {
                    types.add(type);
                } else if( resolvedType.isOptional() && !required ) {
                    types.add(type);
                }
            }
        }
        return types;
    }

    private List<Method> findInitMethods(Class validatorClass) {
        List<Method> methods = new ArrayList<Method>();
        for( Method method : validatorClass.getDeclaredMethods() ) {
            if( method.isAnnotationPresent(InitMethod.class) ) {
                methods.add(method);
            }
        }
        return methods;
    }

    /**
     * Constructs a validator using the given FieldAnnotations. If the given
     * annotations contain all required annotations, the new validator is constructed
     * with required and optional annotation types.
     *
     * @param annotations All annotations for a field
     * @return Validator if the annotations contained all required annotations and the validator could be initialized. Otherwise null.
     */
    public Validator construct( Collection<FieldAnnotation> annotations ) {
        try {
            if( combinationManager.matches(annotations) ) {
                return constructFromAnnotations( annotations );
            } else {
                return null;
            }
        } catch( InstantiationException e ) {
            LOG.error("Could not instantiate validator", e);
        } catch( IllegalAccessException e ) {
            LOG.error("Could not access constructor of the validator", e);
        } catch( InvocationTargetException e ) {
            LOG.error("Could not invoke validator constructor", e);
        } catch( NoSuchMethodException e ) {
            LOG.error("Constructor not found", e);
        } catch (Exception e ) {
            LOG.error("Unknown exception", e);
        }
        return null;
    }

    /**
     * Constructs validator from the given field annotations
     * @param annotations Field annotations
     * @return Constructed class if it could be initialized
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    private Validator constructFromAnnotations( Collection<FieldAnnotation> annotations )
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        AnnotationBag annotationBag = constructAnnotationBag( annotations );
        return constructAndInitValidator( annotationBag );
    }

    /**
     * Constructs an annotation bag containing all required and optional
     * annotations found from given annotations.
     * @param annotations Annotations that are filtered to the bag
     * @return Annotation bag with required and possibly optional annotations.
     */
    private AnnotationBag constructAnnotationBag( Collection<FieldAnnotation> annotations ) {
        AnnotationBag annotationBag = new AnnotationBag();
        for( FieldAnnotation annotation : annotations ) {
            if( combinationManager.containsClass(annotation.getType()) ) {
                annotationBag.addAnnotation(annotation);
            }
        }
        return annotationBag;
    }

    /**
     * Constructs and initializes the validator
     * @param annotationBag Annotations to give to the init method
     * @return Initialized validator
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    private Validator constructAndInitValidator( AnnotationBag annotationBag )
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        final BaseValidator validatorInstance = (BaseValidator)validatorClass.getConstructor().newInstance();
        validatorInstance.init( annotationBag );
        return validatorInstance;
    }
}
