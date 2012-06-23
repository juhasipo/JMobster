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

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import fi.vincit.jmobster.processor.ModelProvider;
import fi.vincit.jmobster.util.ModelWriter;
import fi.vincit.jmobster.util.StreamModelWriter;

/**
 * Cached model provider caches processed models so that the models
 * can be efficiently provided e.g. via HTTP to the client. This
 * way the client can get always the mode recent model without the
 * need to generate the models during build.
 */
public class CachedModelProvider implements ModelProvider {

    public static enum Mode {
        COMPACT,
        PRETTY
    }

    private String cachedModel;
    private ModelWriter modelWriter;
    private ByteOutputStream bos;
    public CachedModelProvider(Mode mode) {
        this.bos = new ByteOutputStream();
        this.modelWriter = new StreamModelWriter(bos);
        switch (mode) {
            case COMPACT: configureCompactMode(modelWriter); break;
            case PRETTY: configurePrettyMode(modelWriter); break;
        }
    }

    private void configurePrettyMode(ModelWriter modelWriter) {
        modelWriter.setIndentationChar(' ', 4);
        modelWriter.setLineSeparator("\n");
    }

    private void configureCompactMode(ModelWriter modelWriter) {
        modelWriter.setIndentation(0);
        modelWriter.setLineSeparator("");
    }


    @Override
    public String getModel() {
        if( cachedModel == null ) {
            cachedModel = bos.toString();
        }
        return cachedModel;
    }

    @Override
    public ModelWriter getModelWriter() {
        return modelWriter;
    }
}
