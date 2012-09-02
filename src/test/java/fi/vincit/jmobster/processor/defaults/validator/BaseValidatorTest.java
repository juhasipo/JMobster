package fi.vincit.jmobster.processor.defaults.validator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseValidatorTest {
    @Test
    public void testDefaultType() throws Exception {
        class TestClass1 extends BaseValidator {
            TestClass1() {}
            @Override public void init( AnnotationBag annotationBag ) {}
        }

        TestClass1 testClass1 = new TestClass1();
        assertEquals(testClass1.getType(), TestClass1.class);
    }

    @Test
    public void testSetType() throws Exception {
        class TestClass1 extends BaseValidator {
            TestClass1() {}
            @Override public void init( AnnotationBag annotationBag ) {}
        }

        TestClass1 testClass1 = new TestClass1();
        testClass1.setType(String.class);
        assertEquals(testClass1.getType(), String.class);
    }
}
