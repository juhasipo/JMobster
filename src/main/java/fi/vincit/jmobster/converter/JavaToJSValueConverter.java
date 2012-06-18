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

import fi.vincit.jmobster.converter.valueconverters.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Converts the given value to JavaScript string.
 * Note that null values in ALLOW_NULL mode are converted
 * to "null" (as a string).
 */
public class JavaToJSValueConverter {

    public static final String NULL_VALUE = "null";

    private ConverterMode mode;
    private Map<Class, ValueConverter> converters;
    private NullConverter nullConverter = new NullConverter();

    /**
     * Creates new Java to JavaScript value converter with
     * the given converter mode.
     * @param mode Converter mode to use
     */
    public JavaToJSValueConverter(ConverterMode mode) {
        this();
        this.mode = mode;
    }

    public String convert(Field field, Object defaultObject) {
        try {
            Object defaultValue = field.get(defaultObject);
            if( mode == ConverterMode.ALLOW_NULL && defaultValue == null ) {
                return NULL_VALUE;
            } else {
                return convertByClass( defaultValue, field.getType() );
            }
        } catch( IllegalAccessException e ) {
            return null;
        }
    }

    public String convert(Class type, Object fieldValue) {
        if( mode == ConverterMode.ALLOW_NULL && fieldValue == null ) {
            return NULL_VALUE;
        } else {
            return convertByClass( fieldValue, type );
        }
    }

    /**
     * Converts the given value using a suitable converter class
     * @param value Value to convert
     * @param clazz Value's class
     * @return Converted value depending on settings
     */
    private String convertByClass( Object value, Class clazz ) {
        ValueConverter c = getConverterByClass( clazz );
        if( c != null ) {
            return c.convertValue(value);
        } else {
            return value != null ? value.toString() : "undefined";
        }
    }

    /**
     * Tries to find a suitable converter for the given class type.
     * Uses also super classes and interfaces for the search.
     * @param clazz Class type
     * @return ValueConverter if found, otherwise null.
     */
    private ValueConverter getConverterByClass( Class clazz ) {
        if( clazz == null ) {
            return nullConverter;
        }
        if( clazz.isArray() ) {
            return converters.get(Array.class);
        }
        ValueConverter c = converters.get(clazz);
        if( c == null ) {
            c = getConverterBySuperClass( clazz );
            if( c == null ) { c = getConverterByInterface(clazz); }
            if( c == null ) { c = getConverterByClass(clazz.getSuperclass()); }
        }
        return c;
    }

    /**
     * Tries to find suitable converter by super class of
     * the given class.
     * @param clazz Class which super class is user for search
     * @return ValueConverter if found, otherwise null.
     */
    private ValueConverter getConverterBySuperClass( Class clazz ) {
        return converters.get(clazz.getSuperclass());
    }

    /**
     * Tries to find suitable converter class by the interfaces
     * of the given class
     * @param clazz Class which interface are used for search
     * @return ValueConverter if found, otherwise null.
     */
    private ValueConverter getConverterByInterface(Class clazz) {
        Class[] interfaces = clazz.getInterfaces();
        if( interfaces != null && interfaces.length > 0 ) {
            for( Class interfaceClass : interfaces ) {
                ValueConverter interfaceConverter = getConverterByClass(interfaceClass);
                if( interfaceConverter != null ) {
                    return interfaceConverter;
                }
            }
        }
        return null;
    }

    public JavaToJSValueConverter() {
        converters = new HashMap<Class, ValueConverter>();
        addConverter( new BooleanConverter(), Boolean.class, boolean.class );
        addConverter( new LongConverter(), Long.class, long.class );
        addConverter( new IntegerConverter(), Integer.class, int.class );
        addConverter( new StringConverter(), String.class, Enum.class );
        addConverter( new DoubleConverter(), Double.class, double.class );
        addConverter( new FloatConverter(), Float.class, float.class );
        addConverter( new CollectionConverter( this ), Collection.class );
        addConverter( new MapConverter( this ), Map.class );
        addConverter( new EnumConverter(), Enum.class );
        addConverter( new ArrayConverter( this ), Array.class );
    }

    protected void addConverter( ValueConverter proxy, Class... classes ) {
        for( Class clazz: classes ) {
            converters.put( clazz, proxy );
        }
    }

}
