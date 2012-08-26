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
import fi.vincit.jmobster.util.writer.DataWriter;

/**
 * <p>
 *     Interface for validator writers that write validator with given
 * writer. You should not implement this interface. Instead use {@link fi.vincit.jmobster.processor.defaults.validator.BaseValidatorWriter}
 * which handles necessary type casts internally so that the validator writer will get the parameters
 * correctly.
 * </p>
 *
 * @param <V> Validator type the writer uses
 * @param <W> Data writer type. Should be compatible with corresponding {@link fi.vincit.jmobster.processor.defaults.validator.BaseValidatorWriterManager}
 */
public interface ValidatorWriter<V extends Validator, W extends DataWriter> {
    /**
     * Writes the given validator with the given writer
     * @param writer Writer to use
     * @param validator Validator to write
     * @param isLast True if the validator is the last validator to be written for the field
     */
    void write( W writer, Object validator, boolean isLast );

    /**
     * Returns the type this validator writer supports
     * @return Type of validator (Class of the {@link Validator})
     */
    Class getSupportedType();
}
