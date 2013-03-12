package fi.vincit.jmobster.processor;

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

import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.writer.DataWriter;

public interface ValidatorWriterManager<W extends DataWriter> {
    /**
     * Configure new validator writer.
     * @param validatorWriters One or more validation writers to add.
     */
    void setValidator(ValidatorWriter<? extends Validator, ? super W>... validatorWriters);

    /**
     * Writes the given validator with tie configured data writer
     * @param validator Validator to write
     * @param status Item status
     */
    void write(Validator validator, ItemStatus status);

    void setWriter(W dataWriter);
}
