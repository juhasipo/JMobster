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
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.combination.CombinationManager;
import fi.vincit.jmobster.util.combination.OptionalTypes;
import fi.vincit.jmobster.util.combination.RequiredTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 */
public class ValidatorConstructor {
    private static final Logger LOG = LoggerFactory.getLogger( ValidatorConstructor.class );

    private CombinationManager<Annotation> combinationManager;
    private Class validatorClass;

    public ValidatorConstructor(Class validatorClass, RequiredTypes requiredTypes, OptionalTypes optionalTypes) {
        this.combinationManager = new CombinationManager(requiredTypes, optionalTypes);
        this.validatorClass = validatorClass;
    }

    public Validator construct( Collection<Annotation> annotations ) {
        // Construct via reflection
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

    private Validator constructFromAnnotations( Collection<Annotation> annotations )
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        AnnotationBag annotationBag = new AnnotationBag();
        for( Annotation annotation : annotations ) {
            if( combinationManager.containsClass(annotation.annotationType()) ) {
                annotationBag.addAnnotation(annotation);
            }
        }
        return (Validator)validatorClass.getConstructor(AnnotationBag.class).newInstance(annotationBag);
    }
}
