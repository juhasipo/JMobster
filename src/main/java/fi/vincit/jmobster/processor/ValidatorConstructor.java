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

import fi.vincit.jmobster.processor.defaults.validator.AnnotationBag;
import fi.vincit.jmobster.processor.model.FieldAnnotation;
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.combination.CombinationManager;
import fi.vincit.jmobster.util.combination.OptionalTypes;
import fi.vincit.jmobster.util.combination.RequiredTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * Constructs validator instance of configured type. The class will
 * initialize the constructed validator with correct parameters.
 */
public class ValidatorConstructor {
    private static final Logger LOG = LoggerFactory.getLogger( ValidatorConstructor.class );
    private static final String INIT_METHOD_NAME = "init";

    private CombinationManager<FieldAnnotation> combinationManager;
    private Class validatorClass;

    /**
     * Configures the validator constructor. Validators the ValidatorConstructor
     * creates, must be extended from {@link fi.vincit.jmobster.processor.defaults.validator.BaseValidator}
     * class so that the validator can be initialized correctly.
     * @param validatorClass Type of validator this constructs (Validator class e.g. SizeValidator.class)
     * @param requiredTypes Required annotation types (classes) that are required for this validator
     * @param optionalTypes Optional annotation types (classes) that can be given to this validator
     */
    public ValidatorConstructor(Class validatorClass, RequiredTypes requiredTypes, OptionalTypes optionalTypes) {
        this.combinationManager = new CombinationManager(requiredTypes, optionalTypes);
        this.validatorClass = validatorClass;
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
            LOG.error("Error", e);
        } catch( IllegalAccessException e ) {
            LOG.error("Error", e);
        } catch( InvocationTargetException e ) {
            LOG.error("Error", e);
        } catch( NoSuchMethodException e ) {
            LOG.error("Error", e);
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
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
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
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final Validator validatorInstance = (Validator)validatorClass.getConstructor().newInstance();
        final Class validatorClass = validatorInstance.getClass();
        validatorClass
                .getMethod( INIT_METHOD_NAME, AnnotationBag.class)
                .invoke(validatorInstance, annotationBag );
        return validatorInstance;
    }
}
