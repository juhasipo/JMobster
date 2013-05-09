package fi.vincit.jmobster.processor;
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

import fi.vincit.jmobster.processor.languages.LanguageContext;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.writer.DataWriter;

import java.io.IOException;

/**
 * ModelProcessor controls how the models given from {@link fi.vincit.jmobster.ModelGenerator}
 * are processed.
 */
public interface ModelProcessor<C extends LanguageContext<W>, W extends DataWriter> {
    /**
     * Called before the first model is processed.
     * @status Item status
     * @throws IOException If something goes wrong with writing the data
     */
    void doStartProcessing(ItemStatus status) throws IOException;

    /**
     * Called exactly once for each model once in the order the models
     * were given to the {@link fi.vincit.jmobster.ModelGenerator}.
     * @param model Model to process
     * @param status Item status
     */
    void doProcessModel(Model model, ItemStatus status);

    /**
     * Called when the last model has been processed.
     * @status Item status
     * @throws IOException If something goes wrong with writing the data
     */
    void doEndProcessing(ItemStatus status) throws IOException;

    void setFieldValueConverter(FieldValueConverter valueConverter);

    String getName();

    void setLanguageContext(C context);

    C getLanguageContext();
}
