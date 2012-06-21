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

public interface ModelWriter {
    @SuppressWarnings( "EmptyMethod" )
    void open();

    ModelWriter write( String modelString );

    ModelWriter write( String modelString, String separator, boolean writeSeparator );

    ModelWriter writeLine( String modelStringLine );

    ModelWriter writeLine( String modelStringLine, String separator, boolean writeSeparator );

    /**
     * Close the stream and file if necessary.
     */
    void close();

    /**
     * Sets indentation size in spaces
     * @param spaces Indentation size in spaces
     */
    void setIndentation( int spaces );

    /**
     * Indent (right)
     * @return ModelWriter to enable chaining calls.
     */
    ModelWriter indent();

    /**
     * Indent back (left). If not new line, will change line.
     * @return ModelWriter to enable chaining calls.
     */
    ModelWriter indentBack();

    /**
     * Sets character that is used for indentation. Usually
     * a space or a tab. Default value is space.
     * @param indentationChar Character to use for indentation
     * @param characterCount Number or characters to indent with the given character
     */
    void setIndentationChar( char indentationChar, int characterCount );

    /**
     * Sets line separation string.
     * @param lineSeparator String to use for line changes
     */
    void setLineSeparator( String lineSeparator );
}
