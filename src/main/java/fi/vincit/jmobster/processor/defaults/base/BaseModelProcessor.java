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
import fi.vincit.jmobster.processor.languages.LanguageContext;
import fi.vincit.jmobster.util.writer.DataWriter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseModelProcessor<C extends LanguageContext<W>, W extends DataWriter> implements ModelProcessor<C, W> {
    private C context;
    private FieldValueConverter valueConverter;
    private String name;

    final private List<ModelProcessor<C, W>> modelProcessors = new ArrayList<ModelProcessor<C, W>>();

    /**
     * Initializes model processor with writer
     * @param name   Name
     */
    public BaseModelProcessor( String name ) {
        this.name = name;
    }

    /**
     * Returns writer.
     * @return Writer
     */
    protected W getWriter() {
        return context.getWriter();
    }

    protected FieldValueConverter getValueConverter() {
        return valueConverter;
    }

    @Override
    public void setLanguageContext(C context) {
        this.context = context;
        for( ModelProcessor<C, W> modelProcessor : modelProcessors ) {
            modelProcessor.setLanguageContext(this.context);
        }
    }

    protected C getContext() {
        return context;
    }

    @Override
    public void setFieldValueConverter(FieldValueConverter valueConverter) {
        this.valueConverter = valueConverter;
        for( ModelProcessor<C, W> modelProcessor : modelProcessors ) {
            modelProcessor.setFieldValueConverter(valueConverter);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public void addModelProcessor(ModelProcessor<C, W> modelProcessor) {
        modelProcessors.add(modelProcessor);
        modelProcessor.setFieldValueConverter(valueConverter);
        modelProcessor.setLanguageContext(context);
    }

    protected Collection<ModelProcessor<C, W>> getModelProcessors() {
        return modelProcessors;
    }
}
