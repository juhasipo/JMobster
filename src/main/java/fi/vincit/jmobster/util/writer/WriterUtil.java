package fi.vincit.jmobster.util.writer;

/*
 * Copyright 2012-2013 Juha Siponen
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

import fi.vincit.jmobster.processor.languages.LanguageContext;

public class WriterUtil {
    /**
     * How the model should be written
     */
    public static enum WriteMode {
        /**
         * Compact mode. No additional spaces, indentations or lines. Compact size but, hard to read by human.
         */
        COMPACT,
        /**
         * Pretty mode. Normal spaces, indentations and line changes. Human readable.
         */
        PRETTY
    }

    public static void configureMode(DataWriter writer, WriteMode mode) {
        switch (mode) {
            case COMPACT:
                configureCompactMode(writer);
                break;
            case PRETTY:
                configurePrettyMode(writer);
                break;
        }
    }

    public static void configureMode(LanguageContext<?> writer, WriteMode mode) {
        switch (mode) {
            case COMPACT:
                configureCompactMode(writer.getWriter());
                break;
            case PRETTY:
                configurePrettyMode(writer.getWriter());
                break;
        }
    }

    /**
     * Make necessary configurations for pretty write mode
     * @param dataWriter Model writer to configure
     */
    public static void configurePrettyMode(DataWriter dataWriter ) {
        dataWriter.setIndentationChar(' ', 4);
        dataWriter.setLineSeparator("\n");
    }

    /**
     * Make necessary configurations for compact write mode
     * @param dataWriter Model writer to configure
     */
    public static void configureCompactMode(DataWriter dataWriter ) {
        dataWriter.setIndentation(0);
        dataWriter.setLineSeparator("");
    }
}
