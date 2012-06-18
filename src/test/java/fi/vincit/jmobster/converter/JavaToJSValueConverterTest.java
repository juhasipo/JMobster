package fi.vincit.jmobster.converter;
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

import fi.vincit.jmobster.converter.valueconverters.ConverterMode;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class JavaToJSValueConverterTest {

    public static final String NULL_STRING = "null";

    /*
    Standard Java data types
     */

    @Test
    public void testBooleanValue() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );
        boolean value = true;
        String result = valueConverter.convert( boolean.class, value );
        assertEquals( "true", result );
    }

    @Test
    public void testBooleanObject() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );
        Boolean value = true;
        String result = valueConverter.convert( Boolean.class, value );
        assertEquals( "true", result );
    }

    @Test
    public void testNullBooleanObject() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );
        Boolean value = null;
        String result = valueConverter.convert( Boolean.class, value );
        assertEquals( "null", result );
    }

    @Test
    public void testNullBooleanObjectAsDefault() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.NULL_AS_DEFAULT );
        Boolean value = null;
        String result = valueConverter.convert( Boolean.class, value );
        assertEquals( "false", result );
    }

    @Test
    public void testIntegerValue() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );
        int value = 10;
        String result = valueConverter.convert( int.class, value );
        assertEquals( "10", result );
    }

    @Test
    public void testIntegerObject() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );
        Integer value = 10;
        String result = valueConverter.convert( value.getClass(), value );
        assertEquals( "10", result );
    }

    @Test
    public void testNullIntegerObject() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );
        Integer value = null;
        String result = valueConverter.convert( Integer.class, value );
        assertEquals( NULL_STRING, result );
    }

    @Test
    public void testNullIntegerObjectAsDefault() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.NULL_AS_DEFAULT );
        Integer value = null;
        String result = valueConverter.convert( Integer.class, value );
        assertEquals( "0", result );
    }

    @Test
    public void testLongObject() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );
        Long value = 50000000000L;
        String result = valueConverter.convert( value.getClass(), value );
        assertEquals( "50000000000", result );
    }

    @Test
    public void testNullLongObject() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );
        Long value = null;
        String result = valueConverter.convert( Long.class, value );
        assertEquals( NULL_STRING, result );
    }

    @Test
    public void testNullLongObjectAsDefault() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.NULL_AS_DEFAULT );
        Long value = null;
        String result = valueConverter.convert( Long.class, value );
        assertEquals( "0", result );
    }

    @Test
    public void testString() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );
        String value = "test string value";
        String result = valueConverter.convert( String.class, value );
        assertEquals( "\"test string value\"", result );
    }

    @Test
    public void testNullString() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );
        String value = null;
        String result = valueConverter.convert( String.class, value );
        assertEquals( "null", result );
    }

    @Test
    public void testNullStringAsDefault() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.NULL_AS_DEFAULT );
        String value = null;
        String result = valueConverter.convert( String.class, value );
        assertEquals( "\"\"", result );
    }

    public enum TestEnum {
        ENUM_1,
        ENUM_2
    }

    @Test
    public void testEnum() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );

        String result = valueConverter.convert( TestEnum.class, TestEnum.ENUM_2 );
        assertEquals( "\"ENUM_2\"", result );
    }

    public static class TestClassWithToString {
        private String value;

        public TestClassWithToString( String value ) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    @Test
    public void testObjectToString() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );

        TestClassWithToString value = new TestClassWithToString( "Test value" );
        String result = valueConverter.convert( value.getClass(), value );
        assertEquals( "Test value", result );
    }

    /*
    Other standard Java data types
     */

    @Test
    public void testBigInteger() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );

        BigInteger value = new BigInteger( "1024" );
        String result = valueConverter.convert( value.getClass(), value );
        assertEquals( "1024", result );
    }

    @Test
    public void testBigDecimal() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );

        BigDecimal value = new BigDecimal( "123456789.123456789" );
        String result = valueConverter.convert( value.getClass(), value );
        assertEquals( "123456789.123456789", result );
    }


    /*
    Sets, lists and arrays
     */

    @Test
    public void testIntegerList() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );

        List<Integer> list = new ArrayList<Integer>();
        list.add( 1 );
        list.add( 19 );
        list.add( 100 );

        String result = valueConverter.convert( list.getClass(), list );
        assertEquals( "[1, 19, 100]", result );
    }


    @Test
    public void testIntValueArray() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );

        int[] array = { 1, 19, 100 };

        String result = valueConverter.convert( array.getClass(), array );
        assertEquals( "[1, 19, 100]", result );
    }

    @Test
    public void testNullIntArray() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.NULL_AS_DEFAULT );
        String result = valueConverter.convert( int[].class, null );
        assertEquals( "[]", result );
    }

    @Test
    public void test2dIntValueArray() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );

        int[][] array = { {1, 19, 100}, {2, 29, 200} };

        String result = valueConverter.convert( array.getClass(), array );
        assertEquals( "[[1, 19, 100], [2, 29, 200]]", result );
    }


    @Test
    public void testStringList() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );

        List<String> list = new ArrayList<String>();
        list.add( "Foo" );
        list.add( "Bar" );
        list.add( "Foobar" );

        String result = valueConverter.convert( list.getClass(), list );
        assertEquals( "[\"Foo\", \"Bar\", \"Foobar\"]", result );
    }

    @Test
    public void testEmptyList() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );

        List<String> list = new ArrayList<String>();

        String result = valueConverter.convert( list.getClass(), list );
        assertEquals( "[]", result );
    }

    @Test
    public void testNullList() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.NULL_AS_DEFAULT );

        List<String> list = null;

        String result = valueConverter.convert( List.class, list );
        assertEquals( "[]", result );
    }

    @Test
    public void testStringSet() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );

        Set<String> set = new TreeSet<String>();
        set.add( "DEF" );
        set.add( "ABC" );
        set.add( "QWERTY" );

        String result = valueConverter.convert( set.getClass(), set );
        assertEquals( "[\"ABC\", \"DEF\", \"QWERTY\"]", result );
    }

    @Test
    public void test2dStringCollectionOfLists() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );

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
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );

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

    /*
    Maps
     */

    @Test
    public void testStringStringMap() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );

        Map<String, String> map = new TreeMap<String, String>();
        map.put("A", "A entry");
        map.put("B", "B entry");
        map.put("C", "C entry");

        String result = valueConverter.convert( map.getClass(), map );
        assertEquals( "{\"A\": \"A entry\", \"B\": \"B entry\", \"C\": \"C entry\"}", result );
    }

    @Test
    public void testStringStringMapWithNullEntry() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.ALLOW_NULL );

        Map<String, String> map = new TreeMap<String, String>();
        map.put("A", "A entry");
        map.put("B", null);
        map.put("C", "C entry");

        String result = valueConverter.convert( map.getClass(), map );
        assertEquals( "{\"A\": \"A entry\", \"B\": null, \"C\": \"C entry\"}", result );
    }

    @Test
    public void testNullMap() {
        JavaToJSValueConverter valueConverter = new JavaToJSValueConverter( ConverterMode.NULL_AS_DEFAULT );

        Map<String, String> map = null;

        String result = valueConverter.convert( Map.class, map );
        assertEquals( "{}", result );
    }
}
