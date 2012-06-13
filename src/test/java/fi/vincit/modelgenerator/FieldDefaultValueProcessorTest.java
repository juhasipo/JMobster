package fi.vincit.modelgenerator;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class FieldDefaultValueProcessorTest {

    private FieldDefaultValueProcessor testProcessor;
    private static SimpleDefaultValueObject testClass;

    private static class SimpleDefaultValueObject {
        public String stringValue = "stringValue";
        
        public Float floatObjectValue = 0.0f;
        public Double doubleObjectValue = 0.0d;
        public float floatValue = 0.0f;
        public double doubleValue = 0.0d;

        public Integer integerObjectValue = 3;
        public int intValue = 4;
        public Long longObjectValue = 5L;
        public long longValue = 6L;

        public boolean booleanTrueValue = true;
        public boolean booleanFalseValue = false;
        public Boolean booleanObjectTrueValue = true;
        public Boolean booleanObjectFalseValue = false;
        public Boolean booleanObjectNullValue = null;

        public String nullString = null;

        public String[] stringArray = { "String1", "String2", "String3"};
        public int[] intArray = {1, 2, 3};
        public float[] floatArray = {0.0f, 1.0f, 2.0f };
    }

    @BeforeClass
    public static void initTestClass() {
        testClass = new SimpleDefaultValueObject();
    }

    public static Field getField(String fieldName) throws Exception {
        return SimpleDefaultValueObject.class.getDeclaredField(fieldName);
    }
    public static String s(String string) {
        return "\"" + string + "\"";
    }

    @Before
    public void initProcessor() {
        testProcessor = new FieldDefaultValueProcessor();
    }

    @Test
    public void testString() throws Exception {
        assertEquals( s( "stringValue" ), testProcessor.getDefaultValue( getField( "stringValue" ), testClass ) );
    }

    @Test
    public void testNullString() throws Exception {
        assertEquals( s(""), testProcessor.getDefaultValue( getField( "nullString" ), testClass ) );
    }

    @Test
    public void testIntegerObject() throws Exception {
        assertEquals( "3", testProcessor.getDefaultValue( getField( "integerObjectValue" ), testClass ) );
    }

    @Test
    public void testIntValue() throws Exception {
        assertEquals( "4", testProcessor.getDefaultValue( getField( "intValue" ), testClass ) );
    }

    @Test
    public void testLongObject() throws Exception {
        assertEquals( "5", testProcessor.getDefaultValue( getField( "longObjectValue" ), testClass ) );
    }

    @Test
    public void testLongValue() throws Exception {
        assertEquals( "6", testProcessor.getDefaultValue( getField( "longValue" ), testClass ) );
    }

    @Test
    public void testFloatObject() throws Exception {
        assertEquals( "0.0", testProcessor.getDefaultValue( getField( "floatObjectValue" ), testClass ) );
    }

    @Test
    public void testFloatValue() throws Exception {
        assertEquals( "0.0", testProcessor.getDefaultValue( getField( "floatValue" ), testClass ) );
    }

    @Test
    public void testDoubleObject() throws Exception {
        assertEquals( "0.0", testProcessor.getDefaultValue( getField( "doubleObjectValue" ), testClass ) );
    }

    @Test
    public void testDoubleValue() throws Exception {
        assertEquals( "0.0", testProcessor.getDefaultValue( getField( "doubleValue" ), testClass ) );
    }

    @Test
    public void testBooleanValueTrue() throws Exception {
        assertEquals( "true", testProcessor.getDefaultValue( getField( "booleanTrueValue" ), testClass ) );
    }

    @Test
    public void testBooleanValueFalse() throws Exception {
        assertEquals( "false", testProcessor.getDefaultValue( getField( "booleanFalseValue" ), testClass ) );
    }

    @Test
    public void testBooleanObjectValueTrue() throws Exception {
        assertEquals( "true", testProcessor.getDefaultValue( getField( "booleanObjectTrueValue" ), testClass ) );
    }

    @Test
    public void testBooleanObjectValueFalse() throws Exception {
        assertEquals( "false", testProcessor.getDefaultValue( getField( "booleanObjectFalseValue" ), testClass ) );
    }

    @Test
    public void testBooleanObjectValueNull() throws Exception {
        assertEquals( "false", testProcessor.getDefaultValue( getField( "booleanObjectNullValue" ), testClass ) );
    }
}
