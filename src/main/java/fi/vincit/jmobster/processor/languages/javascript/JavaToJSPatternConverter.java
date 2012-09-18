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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Pattern;

/**
 * Converts Java regular expression pattern to
 * JavaScrip pattern
 */
@SuppressWarnings( { "HardcodedFileSeparator", "UtilityClassWithoutPrivateConstructor" } )
public final class JavaToJSPatternConverter {
    private static final Logger LOG = LoggerFactory
            .getLogger( JavaToJSPatternConverter.class );

    private static final String JAVASCRIPT_REGEXP_START = "/";
    private static final String JAVASCRIPT_REGEXP_END = "/";
    // JavaScrip regular expression modifiers
    private static final String JAVASCRIPT_CASE_INSENSITIVE_MOD = "i";
    private static final String JAVASCRIPT_MULTI_LINE_MOD = "m";

    /**
     * Coverts the given regular expression patter from Java form
     * to JavaScript form. The only supported flags are at the moment
     * Pattern.Flag.CASE_INSENSITIVE and Pattern.Flag.MULTILINE. The current
     * version may not produce accurate results for all special meta-characters
     * but support will be added later.
     * @param javaPattern Java regular expression pattern
     * @param flags 0..N flags
     * @return Pattern in JavaScript form. If empty pattern is given, an empty pattern is returned.
     */
    public static String convertFromJava(String javaPattern, Pattern.Flag... flags ) {
        assert javaPattern != null : "javaPattern must not be null";
        assert flags != null : "flags must not be null";

        if( javaPattern.trim().length() == 0 ) {
            return convertToJSForm("");
        }
        return convertToJSForm(javaPattern) + getModifiersFromFlags(flags);
    }

    /**
     * Modifies the given Java pattern to JavaScript pattern
     * without modifiers.
     * @param javaPattern Java regular expression pattern
     * @return Pattern in JavaScript form;
     */
    private static String convertToJSForm(String javaPattern) {
        return JAVASCRIPT_REGEXP_START + javaPattern + JAVASCRIPT_REGEXP_END;
    }

    /**
     * Returns modifiers in JavaScript from
     * @param flags Java pattern flags
     * @return Modifier string. Empty string if no modifiers
     */
    private static String getModifiersFromFlags( Pattern.Flag... flags ) {
        String modifiers = "";
        for( Pattern.Flag flag : flags ) {
            if( Pattern.Flag.CASE_INSENSITIVE.equals(flag) ) {
                modifiers += JAVASCRIPT_CASE_INSENSITIVE_MOD;
            } else if( Pattern.Flag.MULTILINE.equals(flag) ) {
                modifiers += JAVASCRIPT_MULTI_LINE_MOD;
            } else {
                LOG.warn("Regular Expression flag " + flag.name() + " is not supported");
            }
        }
        return modifiers;
    }
}
