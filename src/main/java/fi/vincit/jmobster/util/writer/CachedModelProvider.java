package fi.vincit.jmobster.util.writer;/*
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

import fi.vincit.jmobster.processor.ModelProvider;

import static fi.vincit.jmobster.util.writer.WriterUtil.configureCompactMode;
import static fi.vincit.jmobster.util.writer.WriterUtil.configurePrettyMode;

/**
 * Cached model provider caches processed models so that the models
 * can be efficiently provided e.g. via HTTP to the client. This
 * way the client can get always the mode recent model without the
 * need to generate the models during build.
 */
public class CachedModelProvider implements ModelProvider {

    private String cachedModel;
    private final DataWriter dataWriter;

    public static CachedModelProvider createWithStringWriter(WriterUtil.WriteMode writeMode) {
        return new CachedModelProvider(writeMode, new StringBufferWriter());
    }

    /**
     * Creates new model provider that caches the model output.
     * @param writeMode Write mode
     */
    public CachedModelProvider( WriterUtil.WriteMode writeMode, DataWriter dataWriter ) {
        this.dataWriter = dataWriter;
        switch ( writeMode ) {
            case COMPACT: configureCompactMode( dataWriter ); break;
            case PRETTY: configurePrettyMode( dataWriter ); break;
        }
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
