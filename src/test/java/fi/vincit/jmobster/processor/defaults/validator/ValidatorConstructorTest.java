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
import fi.vincit.jmobster.util.AbstractAnnotation;
import fi.vincit.jmobster.util.Optional;
import fi.vincit.jmobster.util.TestUtil;
import fi.vincit.jmobster.util.collection.AnnotationBag;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValidatorConstructorTest {
    public static class TestValidator extends BaseValidator {
        static protected int initCalled;

    public TestValidator() {}

        @Override
        public void init( AnnotationBag annotationBag ) {
            ++initCalled;
            assertTrue(annotationBag.hasAnnotation(Type1.class));
            assertFalse( annotationBag.hasAnnotation( Type2.class ) );
        }

        public static int initCalled() { return initCalled; }
        public static void resetCalls() { initCalled = 0; }
        @Override public Class getType() { return Type1.class; }
    }

    public static class PrivateConstructorValidator extends BaseValidator {
        private PrivateConstructorValidator() {}
        @Override public void init( AnnotationBag annotationBag ) { }
        @Override public Class getType() { return Type1.class; }
    }

    public static class InvalidConstructorValidator extends BaseValidator {
        public InvalidConstructorValidator( int shouldNotBeHere ) {}
        @Override public void init( AnnotationBag annotationBag ) {}
        @Override public Class getType() { return Type1.class; }
    }

    public static class ConstructorThrowsValidator extends BaseValidator {
        public ConstructorThrowsValidator() { throw new RuntimeException(""); }
        @Override public void init( AnnotationBag annotationBag ) {}
        @Override public Class getType() { return Type1.class; }
    }

    public static class InitThrowsValidator extends BaseValidator {
        public InitThrowsValidator() {}
        @Override public void init( AnnotationBag annotationBag ) { throw new RuntimeException(""); }
        @Override public Class getType() { return Type1.class; }
    }

    public abstract static class AbstractClassValidator extends BaseValidator {
        public AbstractClassValidator() {}
        @Override public void init( AnnotationBag annotationBag ) {}
        @Override public Class getType() { return Type1.class; }
    }

    public static class Type1 extends AbstractAnnotation {}
    public static class Type2 extends AbstractAnnotation {}

    public static class OneRequired extends TestValidator {
        @InitMethod
        public void init(Type1 required1) {
        }
    }

    public static class TwoRequired extends TestValidator {
        @InitMethod
        public void init(Type1 required1, Type2 required2) {
        }
    }

    public static class OnlyOptional extends TestValidator {
        @InitMethod
        public void init(Optional<Type1> optional1) {
        }
    }

    @RequiresAnnotations(Type1.class)
    public static class NoParameters extends TestValidator {
    }

    @Test
    public void testInitValidatorConstructor_OnlyRequired() throws Exception {
        TestValidator.resetCalls();
        ValidatorConstructor constructor = new ValidatorConstructor(OneRequired.class);

        FieldAnnotation annotation = mock(FieldAnnotation.class);
        when(annotation.getType()).thenReturn(Type1.class);
        Validator validatorOut = constructor.construct( TestUtil.collectionFromObjects(annotation) );

        assertNotNull(validatorOut);
        assertEquals( TestValidator.initCalled(), 1 );
    }

    @Test
    public void testInitValidatorConstructor_TwoRequired_OneGiven() throws Exception {
        TestValidator.resetCalls();
        ValidatorConstructor constructor = new ValidatorConstructor(TwoRequired.class);

        FieldAnnotation annotation = mock(FieldAnnotation.class);
        when(annotation.getType()).thenReturn(Type1.class);
        Validator validatorOut = constructor.construct( TestUtil.collectionFromObjects(annotation) );

        assertNull(validatorOut);
        assertEquals( TestValidator.initCalled(), 0 );
    }



    @Test
    public void testInitValidatorConstructorOnlyOptional() throws Exception {

        TestValidator.resetCalls();
        ValidatorConstructor constructor = new ValidatorConstructor(OnlyOptional.class);

        FieldAnnotation annotation = mock(FieldAnnotation.class);
        when(annotation.getType()).thenReturn(Type1.class);
        Validator validatorOut = constructor.construct( TestUtil.collectionFromObjects(annotation) );

        assertNotNull(validatorOut);
        assertEquals( TestValidator.initCalled(), 1 );
    }

    @Test
    public void testInitValidatorConstructorNoValidatorInInitMethod() throws Exception {

        TestValidator.resetCalls();
        ValidatorConstructor constructor = new ValidatorConstructor(NoParameters.class);

        FieldAnnotation annotation = mock(FieldAnnotation.class);
        when(annotation.getType()).thenReturn(Type1.class);
        Validator validatorOut = constructor.construct( TestUtil.collectionFromObjects(annotation) );

        assertNotNull(validatorOut);
        assertEquals( TestValidator.initCalled(), 1 );
    }

    @Test
    public void testInitValidatorConstructorOnlyOptionalDontGenerate() throws Exception {
        TestValidator.resetCalls();
        ValidatorConstructor constructor = new ValidatorConstructor(TestValidator.class);

        FieldAnnotation annotation = mock(FieldAnnotation.class);
        when(annotation.getType()).thenReturn(Type1.class);
        Validator validatorOut = constructor.construct( TestUtil.collectionFromObjects(annotation) );

        assertNull(validatorOut);
        assertEquals( TestValidator.initCalled(), 0 );
    }

    @Test
    public void testInitValidatorConstructorNotFound() throws Exception {
        TestValidator.resetCalls();
        ValidatorConstructor constructor = new ValidatorConstructor(TestValidator.class);

        FieldAnnotation annotation = mock(FieldAnnotation.class);
        when(annotation.getType()).thenReturn(Type2.class);
        Validator validatorOut = constructor.construct( TestUtil.collectionFromObjects(annotation) );

        assertNull( validatorOut );
        assertEquals( TestValidator.initCalled(), 0 );
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        TestValidator.resetCalls();
        ValidatorConstructor constructor = new ValidatorConstructor(PrivateConstructorValidator.class);

        FieldAnnotation annotation = mock(FieldAnnotation.class);
        when(annotation.getType()).thenReturn(Type1.class);
        Validator validatorOut = constructor.construct( TestUtil.collectionFromObjects(annotation) );

        assertNull( validatorOut );
    }

    @Test
    public void testInvalidConstructor() throws Exception {
        TestValidator.resetCalls();
        ValidatorConstructor constructor = new ValidatorConstructor(InvalidConstructorValidator.class);

        FieldAnnotation annotation = mock(FieldAnnotation.class);
        when(annotation.getType()).thenReturn(Type1.class);
        Validator validatorOut = constructor.construct( TestUtil.collectionFromObjects(annotation) );

        assertNull( validatorOut );
    }

    @Test
    public void testConstructorThrowsRuntimeException() throws Exception {
        TestValidator.resetCalls();
        ValidatorConstructor constructor = new ValidatorConstructor(ConstructorThrowsValidator.class);

        FieldAnnotation annotation = mock(FieldAnnotation.class);
        when(annotation.getType()).thenReturn(Type1.class);
        Validator validatorOut = constructor.construct( TestUtil.collectionFromObjects(annotation) );

        assertNull( validatorOut );
    }

    @Test
    public void testInitThrowsRuntimeException() throws Exception {
        TestValidator.resetCalls();
        ValidatorConstructor constructor = new ValidatorConstructor(InitThrowsValidator.class);

        FieldAnnotation annotation = mock(FieldAnnotation.class);
        when(annotation.getType()).thenReturn(Type1.class);
        Validator validatorOut = constructor.construct( TestUtil.collectionFromObjects(annotation) );

        assertNull( validatorOut );
    }

    @Test
    public void testAbstractValidator() throws Exception {
        TestValidator.resetCalls();
        ValidatorConstructor constructor = new ValidatorConstructor(AbstractClassValidator.class);

        FieldAnnotation annotation = mock(FieldAnnotation.class);
        when(annotation.getType()).thenReturn(Type1.class);
        Validator validatorOut = constructor.construct( TestUtil.collectionFromObjects(annotation) );

        assertNull( validatorOut );
    }


}
