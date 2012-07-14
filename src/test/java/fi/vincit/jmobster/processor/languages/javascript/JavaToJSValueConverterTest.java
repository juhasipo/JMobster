package fi.vincit.jmobster.processor.languages.javascript;
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

import fi.vincit.jmobster.annotation.IgnoreDefaultValue;
import fi.vincit.jmobster.processor.languages.javascript.valueconverters.ConverterMode;
import fi.vincit.jmobster.processor.languages.javascript.valueconverters.EnumConverter;
import org.junit.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

import static fi.vincit.jmobster.util.TestUtil.quoteString;
import static org.junit.Assert.assertEquals;

public class JavaToJSValueConverterTest {

    public static final String NULL_STRING = "null";

    /*
    Standard Java data types
     */

    // Boolean

    @Test
    public void testBooleanValue() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );
        boolean value = true;
        String result = valueConverter.convert( boolean.class, value );
        assertEquals( "true", result );
    }

    @Test
    public void testBooleanObject() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );
        Boolean value = true;
        String result = valueConverter.convert( Boolean.class, value );
        assertEquals( "true", result );
    }

    @Test
    public void testNullBooleanObject() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );
        Boolean value = null;
        String result = valueConverter.convert( Boolean.class, value );
        assertEquals( "null", result );
    }

    @Test
    public void testNullBooleanObjectAsDefault() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.NULL_AS_DEFAULT, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );
        Boolean value = null;
        String result = valueConverter.convert( Boolean.class, value );
        assertEquals( "false", result );
    }

    // Integer

    @Test
    public void testIntegerValue() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );
        int value = 10;
        String result = valueConverter.convert( int.class, value );
        assertEquals( "10", result );
    }

    @Test
    public void testIntegerObject() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );
        Integer value = 10;
        String result = valueConverter.convert( value.getClass(), value );
        assertEquals( "10", result );
    }

    @Test
    public void testNullIntegerObject() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );
        Integer value = null;
        String result = valueConverter.convert( Integer.class, value );
        assertEquals( NULL_STRING, result );
    }

    @Test
    public void testNullIntegerObjectAsDefault() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.NULL_AS_DEFAULT, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );
        Integer value = null;
        String result = valueConverter.convert( Integer.class, value );
        assertEquals( "0", result );
    }

    // Long

    @Test
    public void testLongObject() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );
        Long value = 50000000000L;
        String result = valueConverter.convert( value.getClass(), value );
        assertEquals( "50000000000", result );
    }

    @Test
    public void testNullLongObject() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );
        Long value = null;
        String result = valueConverter.convert( Long.class, value );
        assertEquals( NULL_STRING, result );
    }

    @Test
    public void testNullLongObjectAsDefault() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.NULL_AS_DEFAULT, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );
        Long value = null;
        String result = valueConverter.convert( Long.class, value );
        assertEquals( "0", result );
    }

    // String

    @Test
    public void testString() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );
        String value = "test string value";
        String result = valueConverter.convert( String.class, value );
        assertEquals( quoteString("test string value"), result );
    }

    @Test
    public void testNullString() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );
        String value = null;
        String result = valueConverter.convert( String.class, value );
        assertEquals( "null", result );
    }

    @Test
    public void testNullStringAsDefault() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.NULL_AS_DEFAULT, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );
        String value = null;
        String result = valueConverter.convert( String.class, value );
        assertEquals( "\"\"", result );
    }

    // Enum

    public enum TestEnum {
        ENUM_1,
        ENUM_2
    }

    @Test
    public void testEnumString() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        String result = valueConverter.convert( TestEnum.class, TestEnum.ENUM_2 );
        assertEquals( quoteString("ENUM_2"), result );
    }

    @Test
    public void testEnumOrdinal() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.ORDINAL, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        String result = valueConverter.convert( TestEnum.class, TestEnum.ENUM_2 );
        assertEquals( "1", result );
    }

    // ToString

    public static class TestClassWithToString {
        private String value;

        public TestClassWithToString( String value ) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value + ".toString";
        }
    }

    @Test
    public void testObjectToString() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        TestClassWithToString value = new TestClassWithToString( "Test value" );
        String result = valueConverter.convert( value.getClass(), value );
        assertEquals( "Test value.toString", result );
    }

    /*
    Other standard Java data types
     */

    // BigInteger

    @Test
    public void testBigInteger() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        BigInteger value = new BigInteger( "1024" );
        String result = valueConverter.convert( value.getClass(), value );
        assertEquals( "1024", result );
    }

    @Test
    public void testBigDecimal() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        BigDecimal value = new BigDecimal( "123456789.123456789" );
        String result = valueConverter.convert( value.getClass(), value );
        assertEquals( "123456789.123456789", result );
    }

    // Date

    @Test
    public void testDate() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String result = valueConverter.convert( now.getClass(), now );
        assertEquals( quoteString( formatter.format( now ) ), result );
    }


    /*
    Sets, lists and arrays
     */

    // Array

    @Test
    public void testIntValueArray() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        int[] array = { 1, 19, 100 };

        String result = valueConverter.convert( array.getClass(), array );
        assertEquals( "[1, 19, 100]", result );
    }

    @Test
    public void testLongValueArray() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        long[] array = { 1L, 19L, 100L };

        String result = valueConverter.convert( array.getClass(), array );
        assertEquals( "[1, 19, 100]", result );
    }

    @Test
    public void testFloatValueArray() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        float[] array = { 1f, 19.0f, 0.1f };

        String result = valueConverter.convert( array.getClass(), array );
        assertEquals( "[1.0, 19.0, 0.1]", result );
    }

    @Test
    public void testDoubleValueArray() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        double[] array = { 1d, 20.0d, 0.2d };

        String result = valueConverter.convert( array.getClass(), array );
        assertEquals( "[1.0, 20.0, 0.2]", result );
    }

    @Test
    public void testBooleanValueArray() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        boolean[] array = { true, false, false, true };

        String result = valueConverter.convert( array.getClass(), array );
        assertEquals( "[true, false, false, true]", result );
    }

    @Test
    public void testNullIntArray() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.NULL_AS_DEFAULT, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );
        String result = valueConverter.convert( int[].class, null );
        assertEquals( "[]", result );
    }

    @Test
    public void test2dIntValueArray() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        int[][] array = { {1, 19, 100}, {2, 29, 200} };

        String result = valueConverter.convert( array.getClass(), array );
        assertEquals( "[[1, 19, 100], [2, 29, 200]]", result );
    }

    // List

    @Test
    public void testIntegerList() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        List<Integer> list = new ArrayList<Integer>();
        list.add( 1 );
        list.add( 19 );
        list.add( 100 );

        String result = valueConverter.convert( list.getClass(), list );
        assertEquals( "[1, 19, 100]", result );
    }

    @Test
    public void testStringList() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        List<String> list = new ArrayList<String>();
        list.add( "Foo" );
        list.add( "Bar" );
        list.add( "Foobar" );

        String result = valueConverter.convert( list.getClass(), list );
        assertEquals( "[\"Foo\", \"Bar\", \"Foobar\"]", result );
    }

    @Test
    public void testEmptyList() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        List<String> list = new ArrayList<String>();

        String result = valueConverter.convert( list.getClass(), list );
        assertEquals( "[]", result );
    }

    @Test
    public void testNullList() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.NULL_AS_DEFAULT, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        List<String> list = null;

        String result = valueConverter.convert( List.class, list );
        assertEquals( "[]", result );
    }

    // Set

    @Test
    public void testStringSet() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        Set<String> set = new TreeSet<String>();
        set.add( "DEF" );
        set.add( "ABC" );
        set.add( "QWERTY" );

        String result = valueConverter.convert( set.getClass(), set );
        assertEquals( "[\"ABC\", \"DEF\", \"QWERTY\"]", result );
    }

    // Multi-dimension collections

    @Test
    public void test2dStringCollectionOfLists() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        Collection<List<String>> collection = new ArrayList<List<String>>();
        List<String> list1 = new Vector<String>();
        list1.add( "A1" );
        list1.add( "B1" );
        list1.add( "C1" );
        collection.add(list1);
        List<String> list2 = new Vector<String>();
        list2.add( "A2" );
        list2.add( "B2" );
        list2.add( "C2" );
        collection.add(list2);

        String result = valueConverter.convert( collection.getClass(), collection );
        assertEquals( "[[\"A1\", \"B1\", \"C1\"], [\"A2\", \"B2\", \"C2\"]]", result );
    }

    @Test
    public void test2dStringCollectionObjects() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        Collection<Object> collection = new ArrayList<Object>();
        List<String> list1 = new Vector<String>();
        list1.add( "A1" );
        list1.add( "B1" );
        list1.add( "C1" );
        collection.add(list1);
        collection.add("Foo");
        collection.add(42);

        String result = valueConverter.convert( collection.getClass(), collection );
        assertEquals( "[[\"A1\", \"B1\", \"C1\"], \"Foo\", 42]", result );
    }

    // Map

    @Test
    public void testStringStringMap() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        Map<String, String> map = new TreeMap<String, String>();
        map.put("A", "A entry");
        map.put("B", "B entry");
        map.put("C", "C entry");

        String result = valueConverter.convert( map.getClass(), map );
        assertEquals( "{\"A\": \"A entry\", \"B\": \"B entry\", \"C\": \"C entry\"}", result );
    }

    @Test
    public void testStringStringMapWithNullEntry() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        Map<String, String> map = new TreeMap<String, String>();
        map.put("A", "A entry");
        map.put("B", null);
        map.put("C", "C entry");

        String result = valueConverter.convert( map.getClass(), map );
        assertEquals( "{\"A\": \"A entry\", \"B\": null, \"C\": \"C entry\"}", result );
    }

    @Test
    public void testNullMap() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.NULL_AS_DEFAULT, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );

        Map<String, String> map = null;

        String result = valueConverter.convert( Map.class, map );
        assertEquals( "{}", result );
    }

    /*
    Other
     */

    @Test
    public void testIgnoreDefaultValueAllowNull() {
        class TestClass {
            @IgnoreDefaultValue
            public Integer id;
        }

        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );
        String result = valueConverter.convert(getField( TestClass.class ), new TestClass());
        assertEquals( "null", result );
    }

    @Test
    public void testIgnoreDefaultValueNullAsDefault() {
        class TestClass {
            @IgnoreDefaultValue
            public Integer id;
        }

        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.NULL_AS_DEFAULT, EnumConverter.EnumMode.STRING, JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN );
        String result = valueConverter.convert(getField( TestClass.class ), new TestClass());
        assertEquals( "0", result );
    }

    /*
    Utilities
     */

    /**
     * Get first field of the given class
     * @param clazz Class
     * @return First field
     */
    private Field getField(Class clazz) {
        return clazz.getDeclaredFields()[0];
    }
}
