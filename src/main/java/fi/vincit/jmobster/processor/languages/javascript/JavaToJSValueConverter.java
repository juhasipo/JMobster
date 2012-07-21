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

import fi.vincit.jmobster.processor.defaults.BaseValueConverterManager;
import fi.vincit.jmobster.processor.languages.javascript.valueconverters.*;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Converts the given value to JavaScript string.
 * Note that null values in ALLOW_NULL mode are converted
 * to "null" (as a string).
 */
public class JavaToJSValueConverter extends BaseValueConverterManager {

    /**
     * ISO-8601 Date pattern
     */
    public static final String ISO_8601_DATE_PATTERN = "yyyy-MM-dd";
    /**
     * ISO-88601 Time patter with time zone
     */
    public static final String ISO_8601_TIME_TZ_PATTERN = "HH:mm:ssZ";
    /**
     * ISO-8601 Date time pattern with time zone
     */
    public static final String ISO_8601_DATE_TIME_TZ_PATTERN = ISO_8601_DATE_PATTERN + "'T'" + ISO_8601_TIME_TZ_PATTERN;

    /**
     * JavaScript null value
     */
    private static final String NULL_VALUE = "null";

    /**
     * Creates new Java to JavaScript value converter with
     * the given converter mode.
     * @param converterMode Converter mode to use
     * @param enumMode Enum mode to use
     * @param dateTimePattern Date time pattern to use
     */
    public JavaToJSValueConverter(ConverterMode converterMode, EnumConverter.EnumMode enumMode, String dateTimePattern) {
        super(converterMode, NULL_VALUE );

        setConverter( new BooleanConverter(), Boolean.class, boolean.class );
        setConverter( new LongConverter(), Long.class, long.class );
        setConverter( new IntegerConverter(), Integer.class, int.class );
        setConverter( new StringConverter(), String.class, Enum.class );
        setConverter( new DoubleConverter(), Double.class, double.class );
        setConverter( new FloatConverter(), Float.class, float.class );
        setConverter( new CollectionConverter( this ), Collection.class );
        setConverter( new MapConverter( this ), Map.class );
        setConverter( new EnumConverter( enumMode ), Enum.class );
        setConverter( new ArrayConverter( this ), Array.class );
        setConverter( new DateTimeConverter( dateTimePattern ), Date.class );
        setConverter( new CalendarConverter( dateTimePattern ), Calendar.class );
    }

    /**
     * Create new Java to JavaScript value converter with
     * ALLOW_NULL converter mode
     */
    public JavaToJSValueConverter() {
        this(ConverterMode.ALLOW_NULL, EnumConverter.EnumMode.STRING, ISO_8601_DATE_TIME_TZ_PATTERN );
    }

}
