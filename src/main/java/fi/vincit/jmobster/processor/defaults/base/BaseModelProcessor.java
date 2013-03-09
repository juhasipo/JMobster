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

import fi.vincit.jmobster.processor.FieldValueConverter;
import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.util.writer.DataWriter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseModelProcessor<W extends DataWriter> implements ModelProcessor<W> {
    private W writer;
    final private FieldValueConverter valueConverter;
    private String name;

    final private List<ModelProcessor<W>> modelProcessors = new ArrayList<ModelProcessor<W>>();

    /**
     * Initializes model processor with writer
     * @param name   Name
     * @param writer Writer to use
     * @param valueConverter Value converter
     */
    public BaseModelProcessor( String name, W writer, FieldValueConverter valueConverter ) {
        this.valueConverter = valueConverter;
        this.writer = writer;
        this.name = name;
    }

    /**
     * Returns writer.
     * @return Writer
     */
    protected W getWriter() {
        return writer;
    }

    protected FieldValueConverter getValueConverter() {
        return valueConverter;
    }

    /**
     * Sets writer
     * @param dataWriter Writer to set
     */
    @Override
    public void setWriter( W dataWriter ) {
        this.writer = dataWriter;
    }

    @Override
    public String getName() {
        return name;
    }

    public void addModelProcessor(ModelProcessor<W> modelProcessor) {
        modelProcessors.add(modelProcessor);
        modelProcessor.setWriter(writer);
    }

    protected Collection<ModelProcessor<W>> getModelProcessors() {
        return modelProcessors;
    }
}
