package fi.vincit.jmobster.processor.languages.javascript.valueconverters;

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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Converter for Java Calendar objects. Converts values to quoted strings.
 */
public class CalendarConverter extends DateTimeConverter {

    public CalendarConverter( String pattern ) {
        super( pattern );
    }

    public CalendarConverter( String pattern, Locale locale ) {
        super( pattern, locale );
    }

    public CalendarConverter( DateFormat dateFormat ) {
        super( dateFormat );
    }

    @Override
    protected String getValueAsString( Object value ) {
        Calendar cal = (Calendar)value;
        return super.getValueAsString(cal.getTime());
    }
}