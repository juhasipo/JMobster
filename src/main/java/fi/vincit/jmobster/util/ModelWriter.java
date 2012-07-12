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

/**
 * Interface for writing model to a stream, file or other
 * output. Contains simple write and indentation methods. Write methods contain
 * simple write string and write line methods, but also methods that can be used
 * to write e.g. comma separated lists. When used with {@link ItemProcessor} the
 * final comma can be easily left out after the last item in the list.
 */
public interface ModelWriter {
    /**
     * Open the writer
     */
    void open();

    /**
     * Write the given string
     * @param modelString String to write
     * @return Writer for chaining calls.
     */
    ModelWriter write( String modelString );

    /**
     * Write the given string. If writeSeparator is set to true, also writes the separator
     * right after the modelString.
     * @param modelString String to write
     * @param separator Optional separator to write
     * @param writeSeparator Should separator be written. Set true if should, set false if not.
     * @return Writer for chaining calls.
     */
    ModelWriter write( String modelString, String separator, boolean writeSeparator );

    /**
     * Write the given string and start new line
     * @param modelStringLine String to write
     * @return Writer for chaining calls.
     */
    ModelWriter writeLine( String modelStringLine );

    /**
     * Write the given string and start new line. If writeSeparator is set to true, also writes the
     * separator right after the modelString. Line change will be performed after modelString and if
     * separator is written, it will be performed after separator.
     * @param modelStringLine String to write
     * @param separator Optional separator to write
     * @param writeSeparator Should separator be written. Set true if should, set false if not.
     * @return
     */
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
