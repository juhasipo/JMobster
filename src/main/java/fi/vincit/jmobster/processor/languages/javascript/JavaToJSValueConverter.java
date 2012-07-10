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
import fi.vincit.jmobster.processor.FieldValueConverter;
import fi.vincit.jmobster.processor.languages.javascript.valueconverters.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Converts the given value to JavaScript string.
 * Note that null values in ALLOW_NULL mode are converted
 * to "null" (as a string).
 */
public class JavaToJSValueConverter implements FieldValueConverter {

    // TODO: Make more generic
    private static final Logger LOG = LoggerFactory
            .getLogger( JavaToJSValueConverter.class );

    public static final String NULL_VALUE = "null";
    private static final String UNDEFINED_VALUE = "undefined";

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
        addConverter( new DateTimeConverter("yyyy-MM-dd'T'HH:mm:ssZ"), Date.class);
    }

    protected void addConverter( ValueConverter proxy, Class... classes ) {
        for( Class clazz: classes ) {
            converters.put( clazz, proxy );
        }
    }

    @Override
    public String convert( Field field, Object defaultValueObject ) {
        try {
            Object defaultValue;
            if( !field.isAnnotationPresent( IgnoreDefaultValue.class ) ) {
                defaultValue = field.get( defaultValueObject );
            } else {
                defaultValue = null;
            }
            if( mode == ConverterMode.ALLOW_NULL && defaultValue == null ) {
                return NULL_VALUE;
            } else {
                return convertByClass( defaultValue, field.getType() );
            }

        } catch( IllegalAccessException e ) {
            return null;
        }
    }

    @Override
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
        ValueConverter converter = getConverterByClass( clazz );
        if( converter != null ) {
            return converter.convertValue(value);
        } else {
            return value != null ? value.toString() : UNDEFINED_VALUE;
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
        } else if( clazz.isArray() ) {
            return converters.get(Array.class);
        } else {
            ValueConverter converter = converters.get(clazz);
            if( converter == null ) {
                converter = getConverterBySuperClass( clazz );
                if( converter == null ) { converter = getConverterByInterface(clazz); }
                if( converter == null ) { converter = getConverterByClass(clazz.getSuperclass()); }
            }
            if( converter == null ) {
                LOG.error("Converter not found for class {}", clazz.getName());
            }
            return converter;
        }
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

}
