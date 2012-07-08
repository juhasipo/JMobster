package fi.vincit.jmobster.processor;

import fi.vincit.jmobster.exception.InvalidType;
import fi.vincit.jmobster.util.ModelWriter;
import org.junit.Test;

import javax.validation.constraints.Max;
import java.lang.annotation.Annotation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseValidationAnnotationProcessorTest {
    public static class TestImplementation extends BaseValidationAnnotationProcessor {
        public TestImplementation() {
            super(RequiredTypes.get());
        }

        public TestImplementation( String requiredType ) {
            super( requiredType, RequiredTypes.get() );
        }

        @Override
        protected Class[] getGroupsInternal( Annotation annotation ) {
            return new Class[] {};
        }

        @Override
        protected void writeValidatorsToStreamInternal( ModelWriter writer ) {
        }
    }

    @Test
    public void testRequireType() {
        final String requiredType = "number";
        TestImplementation ti = new TestImplementation(requiredType);
        assertTrue(ti.requiresType());
        assertEquals(requiredType, ti.requiredType());
    }

    @Test
    public void testNoRequireType() {
        TestImplementation ti = new TestImplementation();
        assertFalse( ti.requiresType() );
        assertNull( ti.requiredType() );
    }

    @Test
    public void testValidateRequireType() {
        final String requiredType = "number";
        TestImplementation ti = new TestImplementation(requiredType);
        ti.validateType( "number" );
    }

    @Test( expected = InvalidType.class )
    public void testValidateInvalidRequireType() {
        final String requiredType = "number";
        TestImplementation ti = new TestImplementation(requiredType);
        ti.validateType("foo");
    }

    @Test
    public void testValidateRequireTypeWhenNoType() {
        TestImplementation ti = new TestImplementation();
        ti.validateType("foo");
    }

    @Test
    public void testHasNoGroups() {
        TestImplementation ti = new TestImplementation();
        Annotation a = mock(Annotation.class);
        assertFalse(ti.hasGroups(a));
    }
}