package fi.vincit.jmobster.processor.defaults;/*
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

import fi.vincit.jmobster.processor.ModelProvider;
import fi.vincit.jmobster.util.writer.DataWriter;
import fi.vincit.jmobster.util.writer.StringBufferWriter;

/**
 * Cached model provider caches processed models so that the models
 * can be efficiently provided e.g. via HTTP to the client. This
 * way the client can get always the mode recent model without the
 * need to generate the models during build.
 */
public class CachedModelProvider implements ModelProvider {

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

    private String cachedModel;
    private final DataWriter dataWriter;

    public static CachedModelProvider createWithStringWriter(WriteMode writeMode) {
        return new CachedModelProvider(writeMode, new StringBufferWriter());
    }

    /**
     * Creates new model provider that caches the model output.
     * @param writeMode Write mode
     */
    public CachedModelProvider( WriteMode writeMode, DataWriter dataWriter ) {
        this.dataWriter = dataWriter;
        switch ( writeMode ) {
            case COMPACT: configureCompactMode( dataWriter ); break;
            case PRETTY: configurePrettyMode( dataWriter ); break;
        }
    }

    /**
     * Make necessary configurations for pretty write mode
     * @param dataWriter Model writer to configure
     */
    private void configurePrettyMode(DataWriter dataWriter ) {
        dataWriter.setIndentationChar(' ', 4);
        dataWriter.setLineSeparator("\n");
    }

    /**
     * Make necessary configurations for compact write mode
     * @param dataWriter Model writer to configure
     */
    private void configureCompactMode(DataWriter dataWriter ) {
        dataWriter.setIndentation(0);
        dataWriter.setLineSeparator("");
    }

    @Override
    public String getModel() {
        if( cachedModel == null ) {
            cachedModel = dataWriter.toString();
        }
        return cachedModel;
    }

    @Override
    public DataWriter getDataWriter() {
        return dataWriter;
    }

    @Override
    public void clear() {
        cachedModel = null;
    }
}
