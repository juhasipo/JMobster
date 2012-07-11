package fi.vincit.jmobster.processor.defaults;

import fi.vincit.jmobster.exception.InvalidType;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.util.ModelWriter;
import fi.vincit.jmobster.util.RequiredTypes;
import org.junit.Test;

import javax.validation.constraints.Max;
import java.lang.annotation.Annotation;

import static fi.vincit.jmobster.util.TestUtil.listFromObjects;
import static org.junit.Assert.*;
import static org.mockito.Matchers.intThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class BaseValidationAnnotationProcessorTest {
    public static class TestImplementation extends BaseValidationAnnotationProcessor {
        public TestImplementation() {
            super( RequiredTypes.get());
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
        assertTrue( ti.requiresType() );
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
        Annotation a = mock( Annotation.class );
        assertFalse( ti.hasGroups( a ) );
    }

    @Test
    public void testHasGroups() {
        // Spy used here in order to return groups for regular mocked
        // annotation so that we don't have create complex test class
        // with a real validation annotation. This test just wants to
        // test that the hasGroups method works when the internal
        // method returns groups.
        TestImplementation ti = spy(new TestImplementation());
        Annotation a = mock(Annotation.class);
        class Group {}
        when(ti.getGroupsInternal(a)).thenReturn( new Class[] { Group.class } );

        assertTrue( ti.hasGroups(a) );
    }

    @Test
    public void testHasNoGroupsEmptyArray() {
        // See note about the spy in testHasGroups method
        // Same thing here, now internal method just returns
        // an empty array
        TestImplementation ti = spy(new TestImplementation());
        Annotation a = mock(Annotation.class);
        class Group {}
        when(ti.getGroupsInternal(a)).thenReturn( new Class[] {} );

        assertFalse( ti.hasGroups( a ) );
    }

    @Test public void testFieldInformation() {
        TestImplementation ti = new TestImplementation();
        ModelField testField = mockField();

        ti.writeValidatorsToStream(testField, mock(ModelWriter.class));
        assertEquals("Default value", ti.getFieldDefaultValue());
        assertEquals(int.class, ti.getFieldType());
        assertNotNull(ti.getField());
    }

    private ModelField mockField() {
        class TestClass {
            public int testField;
        }
        ModelField testField = new ModelField(TestClass.class.getDeclaredFields()[0], listFromObjects(mock(Annotation.class)));
        testField.setDefaultValue("Default value");
        return testField;
    }
}
