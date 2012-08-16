package fi.vincit.jmobster.processor.defaults.base;

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

import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.util.DataWriter;
import fi.vincit.jmobster.util.StreamDataWriter;

import java.io.IOException;

public abstract class BaseModelProcessor implements ModelProcessor {
    private DataWriter writer;
    private String modelFilePath;

    public BaseModelProcessor( String modelFilePath ) {
        this.modelFilePath = modelFilePath;
    }

    public BaseModelProcessor( DataWriter writer ) {
        this.writer = writer;
    }

    protected String getModelFilePath() {
        return modelFilePath;
    }

    /**
     * Returns writer. If no writer exists tries to create one
     * using model file path.
     * TODO: Make more intuitive
     * @return Writer
     */
    protected DataWriter getWriter() {
        try {
            if( !hasWriter() ) {
                setWriter(new StreamDataWriter(getModelFilePath()));
            }
            return writer;
        } catch( IOException e ) {
            throw new RuntimeException(e);
        }
    }

    protected boolean hasWriter() {
        return writer != null;
    }

    protected void setWriter( DataWriter dataWriter ) {
        this.writer = dataWriter;
    }
}
