package fi.vincit.jmobster.util;/*
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

import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.processor.model.ModelField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * General utility methods for unit and integration tests.
 */
public class TestUtil {

    /**
     * Assert that the given fields list contains the field with the given name.
     * On assertion failure index isn't naturally returned.
     * @param modelFields Model to check
     * @param fieldName Field name
     * @return Index in which the found field is.
     */
    public static int assertFieldFound(List<ModelField> modelFields, String fieldName) {
        for( int i = 0; i < modelFields.size(); ++i ) {
            ModelField field = modelFields.get(i);
            if( field.getName().equals(fieldName) ) {
                return i;
            }
        }
        assertTrue( "Field with name <" + fieldName + "> not found.", false );
        return -1; // Never returned due to assertion
    }

    /**
     * Assert that the given fields list doesn't contain the field with the given name.
     * On assertion failure index isn't naturally returned.
     * @param modelFields Model to check
     * @param fieldName Field name
     */
    public static void assertFieldNotFound(List<ModelField> modelFields, String fieldName) {
        for( ModelField field : modelFields ) {
            if( field.getName().equals(fieldName) ) {
                assertTrue( "Field with name <" + fieldName + "> found when it should be ignored.", false );
            }
        }
    }

    /**
     * Generates a List from the given objects
     * @param objects Object to include in the list
     * @param <T> Type of list
     * @return List containing the given objects. Empty list if no objects given.
     */
    public static <T>List<T> listFromObjects(T... objects) {
        List<T> arrayList = new ArrayList<T>(objects.length);
        for( T t : objects ) {
            arrayList.add(t);
        }
        return arrayList;
    }

    /**
     * Generates a List from the given objects
     * @param objects Object to include in the list
     * @param <T> Type of list
     * @return List containing the given objects. Empty list if no objects given.
     */
    public static <T>Collection<T> collectionFromObjects(T... objects) {
        Collection<T> arrayList = new ArrayList<T>(objects.length);
        for( T t : objects ) {
            arrayList.add(t);
        }
        return arrayList;
    }

    /**
     * Generates a Model field with the given annotations. Model field
     * default value will be "Test value" and the type of the model field
     * will be int.class.
     * @param validators Annotations
     * @return Model field with given annotations
     */
    public static ModelField getField(List<Validator> validators) {
        class T {int field;}
        ModelField field = new ModelField(T.class.getDeclaredFields()[0], validators);
        field.setDefaultValue("Test value");
        return field;
    }

    /**
     * Adds quotation marks around the given string
     * @param stringToQuote String to quote
     * @return Given string with quotation marks around it
     */
    public static String quoteString(String stringToQuote) {
        return "\"" + stringToQuote + "\"";
    }
}
