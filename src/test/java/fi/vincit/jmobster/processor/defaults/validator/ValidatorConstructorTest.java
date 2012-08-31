package fi.vincit.jmobster.processor.defaults.validator;

import fi.vincit.jmobster.processor.model.FieldAnnotation;
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.AbstractAnnotation;
import fi.vincit.jmobster.util.TestUtil;
import fi.vincit.jmobster.util.combination.OptionalTypes;
import fi.vincit.jmobster.util.combination.RequiredTypes;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ValidatorConstructorTest {
    public static class TestValidator extends BaseValidator {
        static private int initCalled;

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

    @Test
    public void testInitValidatorConstructor() throws Exception {
        TestValidator.resetCalls();
        ValidatorConstructor constructor = new ValidatorConstructor(TestValidator.class, RequiredTypes.get(Type1.class), OptionalTypes.get());

        FieldAnnotation annotation = mock(FieldAnnotation.class);
        when(annotation.getType()).thenReturn(Type1.class);
        Validator validatorOut = constructor.construct( TestUtil.collectionFromObjects(annotation) );

        assertNotNull(validatorOut);
        assertEquals( TestValidator.initCalled(), 1 );
    }

    @Test
    public void testInitValidatorConstructorNotFound() throws Exception {
        TestValidator.resetCalls();
        ValidatorConstructor constructor = new ValidatorConstructor(TestValidator.class, RequiredTypes.get(Type1.class), OptionalTypes.get());

        FieldAnnotation annotation = mock(FieldAnnotation.class);
        when(annotation.getType()).thenReturn(Type2.class);
        Validator validatorOut = constructor.construct( TestUtil.collectionFromObjects(annotation) );

        assertNull( validatorOut );
        assertEquals( TestValidator.initCalled(), 0 );
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        TestValidator.resetCalls();
        ValidatorConstructor constructor = new ValidatorConstructor(PrivateConstructorValidator.class, RequiredTypes.get(Type1.class), OptionalTypes.get());

        FieldAnnotation annotation = mock(FieldAnnotation.class);
        when(annotation.getType()).thenReturn(Type1.class);
        Validator validatorOut = constructor.construct( TestUtil.collectionFromObjects(annotation) );

        assertNull( validatorOut );
    }

    @Test
    public void testInvalidConstructor() throws Exception {
        TestValidator.resetCalls();
        ValidatorConstructor constructor = new ValidatorConstructor(InvalidConstructorValidator.class, RequiredTypes.get(Type1.class), OptionalTypes.get());

        FieldAnnotation annotation = mock(FieldAnnotation.class);
        when(annotation.getType()).thenReturn(Type1.class);
        Validator validatorOut = constructor.construct( TestUtil.collectionFromObjects(annotation) );

        assertNull( validatorOut );
    }

    @Test
    public void testConstructorThrowsRuntimeException() throws Exception {
        TestValidator.resetCalls();
        ValidatorConstructor constructor = new ValidatorConstructor(ConstructorThrowsValidator.class, RequiredTypes.get(Type1.class), OptionalTypes.get());

        FieldAnnotation annotation = mock(FieldAnnotation.class);
        when(annotation.getType()).thenReturn(Type1.class);
        Validator validatorOut = constructor.construct( TestUtil.collectionFromObjects(annotation) );

        assertNull( validatorOut );
    }

    @Test
    public void testInitThrowsRuntimeException() throws Exception {
        TestValidator.resetCalls();
        ValidatorConstructor constructor = new ValidatorConstructor(InitThrowsValidator.class, RequiredTypes.get(Type1.class), OptionalTypes.get());

        FieldAnnotation annotation = mock(FieldAnnotation.class);
        when(annotation.getType()).thenReturn(Type1.class);
        Validator validatorOut = constructor.construct( TestUtil.collectionFromObjects(annotation) );

        assertNull( validatorOut );
    }

    @Test
    public void testAbstractValidator() throws Exception {
        TestValidator.resetCalls();
        ValidatorConstructor constructor = new ValidatorConstructor(AbstractClassValidator.class, RequiredTypes.get(Type1.class), OptionalTypes.get());

        FieldAnnotation annotation = mock(FieldAnnotation.class);
        when(annotation.getType()).thenReturn(Type1.class);
        Validator validatorOut = constructor.construct( TestUtil.collectionFromObjects(annotation) );

        assertNull( validatorOut );
    }


}
