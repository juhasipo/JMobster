package fi.vincit.jmobster.processor.languages.javascript.writer;/*
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

import fi.vincit.jmobster.util.ItemHandler;
import fi.vincit.jmobster.util.ItemProcessor;
import fi.vincit.jmobster.util.ItemStatus;
import fi.vincit.jmobster.util.writer.DataWriter;

/**
 * Higher level abstraction for DataWriter that can write
 * JavaScript to DataWriter. By default the writer checks that
 * all functions and blocks are closed when the writer is closed.
 * This feature can be turned of with {@link JavaScriptWriter#lenientModeOn}.
 */
public class JavaScriptWriter implements DataWriter {

    private static final String BLOCK_START = "{";
    private static final String BLOCK_END = "}";

    private static final String ARRAY_START = "[";
    private static final String ARRAY_END = "]";
    private static final String ARRAY_SEPARATOR = ", ";

    private static final String FUNCTION_ARG_START = "(";
    private static final String FUNCTION_ARG_END = ")";

    private static final String KEY_VALUE_SEPARATOR = ": ";
    private static final String LIST_SEPARATOR = ",";

    private static final String FUNCTION_DEF = "function";
    private String space = " ";

    private boolean lenientModeOn = false;

    // Sanity checks.
    private int functionsOpen = 0;
    private int blocksOpen = 0;

    private DataWriter writer;

    private abstract static class ItemWriter<T> implements ItemHandler<T> {
        private JavaScriptWriter writer;

        protected ItemWriter( JavaScriptWriter writer ) {
            this.writer = writer;
        }

        protected JavaScriptWriter getWriter() {
            return writer;
        }
    }

    private static ItemWriter<Object> arrayWriter;

    public JavaScriptWriter(DataWriter writer) {
        this.writer = writer;
        this.arrayWriter = new ItemWriter<Object>(this) {
            @Override
            public void process( Object item, ItemStatus status ) {
                getWriter().write(item.toString(), ARRAY_SEPARATOR, !status.isLastItem());
            }
        };
    }

    /**
     * Start named function and starts a new block.
     * @param name Function name
     * @param arguments Function arguments
     * @return Writer itself for chaining writes
     */
    public JavaScriptWriter startNamedFunction(String name, String... arguments) {
        ++functionsOpen;
        return write(FUNCTION_DEF + space).write( name ).writeFunctionArgsAndStartBlock(arguments);
    }

    /**
     * Start anonymous function and starts a new block.
     * @param arguments Function arguments
     * @return Writer itself for chaining writes
     */
    public JavaScriptWriter startAnonFunction(String... arguments) {
        ++functionsOpen;
        return write(FUNCTION_DEF).writeFunctionArgsAndStartBlock( arguments );
    }

    /**
     * Writes function arguments and starts block
     * @param arguments Function arguments
     * @return Writer itself for chaining writes
     */
    private JavaScriptWriter writeFunctionArgsAndStartBlock(String... arguments) {
        write(FUNCTION_ARG_START);
        ItemHandler<String> argumentProcessor = new ItemHandler<String>() {
            @Override
            public void process(String argument, ItemStatus status) {
                write(argument, "," + space, status.isNotLastItem());
            }
        };
        ItemProcessor.process(argumentProcessor, arguments);
        write(FUNCTION_ARG_END).writeSpace();
        return startBlock();
    }

    /**
     * Ends function and blocks.
     * @return Writer itself for chaining writes
     */
    public JavaScriptWriter endFunction() {
        --functionsOpen;
        return endBlock(true);
    }

    /**
     * Stars a new block. Following lines will be indented.
     * @return Writer itself for chaining writes
     */
    public JavaScriptWriter startBlock() {
        ++blocksOpen;
        return writeLine( BLOCK_START ).indent();
    }

    /**
     * Ends block. Indents back. Writes list separator (default ",") if isLast is false.
     * @param isLast Writes list separator if set to false.
     * @return Writer itself for chaining writes
     */
    public JavaScriptWriter endBlock(boolean isLast) {
        --blocksOpen;
        return indentBack().write( BLOCK_END ).writeLine( "", LIST_SEPARATOR, !isLast );
    }

    /**
     * Writes object key (also the separator, default ":")
     * @param key Key name
     * @return Writer itself for chaining writes
     */
    public JavaScriptWriter writeKey(String key) {
        return write( key ).write( KEY_VALUE_SEPARATOR );
    }

    /**
     * Writes object key and value. Writes list separator (default ",") if isLast is false.
     * @param key Key name
     * @param value Value
     * @param isLast If set to false writes list separator.
     * @return Writer itself for chaining writes
     */
    public JavaScriptWriter writeKeyValue(String key, String value, boolean isLast) {
        return writeKey(key).write(value).writeLine("", LIST_SEPARATOR, !isLast);
    }

    /**
     * Writes array of objects. Objects must have {@link Object#toString()} method
     * implemented.
     * @param objects Objects to write
     * @return Writer itself for chaining writes
     */
    public JavaScriptWriter writeArray(boolean isLast, Object... objects) {
        write(ARRAY_START);
        ItemProcessor.process(arrayWriter, objects);
        write(ARRAY_END);
        write("", LIST_SEPARATOR, !isLast);
        writeLine("");
        return this;
    }

    /**
     * Writes space. Use this to enable easy to setup compact writing that
     * can ignore spaces. To disable spaces use {@link JavaScriptWriter#setSpace(String)}
     * method.
     * @return Writer itself for chaining writes.
     */
    public JavaScriptWriter writeSpace() {
        write(space);
        return this;
    }

    // Delegated methods

    @Override
    public JavaScriptWriter write(char c) {
        writer.write(c);
        return this;
    }

    @Override
    public JavaScriptWriter write(String modelString) {
        writer.write(modelString);
        return this;
    }

    @Override
    public JavaScriptWriter write(String modelString, String separator, boolean writeSeparator) {
        writer.write(modelString, separator, writeSeparator);
        return this;
    }

    @Override
    public JavaScriptWriter writeLine(String modelStringLine) {
        writer.writeLine(modelStringLine);
        return this;
    }

    @Override
    public JavaScriptWriter writeLine(String modelStringLine, String separator, boolean writeSeparator) {
        writer.writeLine(modelStringLine, separator, writeSeparator);
        return this;
    }

    @Override
    public JavaScriptWriter indent() {
        writer.indent();
        return this;
    }

    @Override
    public JavaScriptWriter indentBack() {
        writer.indentBack();
        return this;
    }

    @Override
    public void open() {
        // Do nothing
    }

    @Override
    public boolean isOpen() {
        return writer.isOpen();
    }

    @Override
    public void close() {
        writer.close();
        if( !lenientModeOn ) {
            if( functionsOpen > 0 ) {
                throw new RuntimeException("There are still " + functionsOpen + "unclosed functions");
            } else if( functionsOpen < 0 ) {
                throw new RuntimeException("Too many functions closed. " + Math.abs(functionsOpen) + " times too many.");
            }

            if( blocksOpen > 0 ) {
                throw new RuntimeException("There are still " + blocksOpen + "unclosed blocks");
            } else if( blocksOpen < 0 ) {
                throw new RuntimeException("Too many blocks closed. " + Math.abs(blocksOpen) + " times too many.");
            }
        }
    }

    @Override
    public void setIndentation(int spaces) {
        writer.setIndentation(spaces);
    }

    @Override
    public void setIndentationChar(char indentationChar, int characterCount) {
        writer.setIndentationChar(indentationChar, characterCount);
    }

    @Override
    public void setLineSeparator(String lineSeparator) {
        writer.setLineSeparator(lineSeparator);
    }

    // Setters and getters

    /**
     * If set to false (default) {@link JavaScriptWriter#close()} will
     * check that functions and blocks have been closed properly. If errors are found,
     * exception will be thrown. If set to true, no checks are made and no exceptions
     * are thrown.
     * @param lenientModeOn Should the writer ignore obvious errors.
     */
    public void setLenientMode( boolean lenientModeOn ) {
        this.lenientModeOn = lenientModeOn;
    }

    /**
     * Sets space sequence.
     * @param space Space sequence
     */
    public void setSpace(String space) {
        this.space = space;
    }

    @Override
    public String toString() {
        return writer.toString();
    }
}
