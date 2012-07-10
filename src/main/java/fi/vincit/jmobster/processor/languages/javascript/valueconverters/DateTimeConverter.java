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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeConverter extends BaseValueConverter {

    private SimpleDateFormat simpleDateFormat;

    public DateTimeConverter(String pattern) {
        simpleDateFormat = new SimpleDateFormat(pattern);
    }

    public DateTimeConverter(String pattern, Locale locale) {
        simpleDateFormat = new SimpleDateFormat(pattern, locale);
    }

    public DateTimeConverter( SimpleDateFormat simpleDateFormat ) {
        this.simpleDateFormat = simpleDateFormat;
    }

    @Override
    protected String getTypeDefaultValue() {
        Date defaultDate = new Date(0);
        return simpleDateFormat.format(defaultDate);
    }

    @Override
    protected String getValueAsString( Object value ) {
        return simpleDateFormat.format((Date)value);
    }
}
